package study.online.media.service.impl;

import cn.hutool.core.bean.BeanUtil;
import io.minio.ComposeSource;
import io.minio.ObjectWriteResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.aop.framework.AopContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import study.online.base.constant.MQConstant;
import study.online.base.exception.BaseException;
import study.online.base.model.RestResponse;
import study.online.media.mapper.MediaFilesMapper;
import study.online.media.mapper.MediaProcessMapper;
import study.online.media.model.dto.UploadFileParamsDTO;
import study.online.media.model.dto.UploadFileResultDTO;
import study.online.media.model.po.MediaFiles;
import study.online.media.model.po.MediaProcess;
import study.online.media.service.IMediaFileService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

import static study.online.base.constant.ErrorMessageConstant.FILE_EXIST_ERROR;
import static study.online.base.constant.ErrorMessageConstant.SAVE_FILE_ERROR;
import static study.online.base.constant.MediaFilePathConstant.MEDIA_CHUNK_PATH_BUCKET;
import static study.online.base.constant.MediaFilePathConstant.MEDIA_FILE_PATH_BUCKET;
import static study.online.base.constant.RedisConstant.CHUNK_KEY;

@Slf4j
@Service
@RequiredArgsConstructor
public class MediaFileServiceImpl implements IMediaFileService {

	private final MinioService minioService;
	private final FileService fileService;

	private final MediaFilesMapper mediaFilesMapper;
	private final MediaProcessMapper mediaProcessMapper;

	private final StringRedisTemplate redisTemplate;
	private final RabbitTemplate rabbitTemplate;

	private IMediaFileService proxy;

	@Override
	public UploadFileResultDTO uploadFile(Long companyId, UploadFileParamsDTO uploadFileParamsDTO, MultipartFile file) {
		if (file.isEmpty()) {
			BaseException.cast(FILE_EXIST_ERROR);
		}

		String filename = uploadFileParamsDTO.getFilename();
		String extension = filename.substring(filename.lastIndexOf("."));

		String md5 = fileService.getFileMd5(file);
		String defaultFolderPath = fileService.getDefaultFolderPath();

		//拼接objectname
		String objectname = defaultFolderPath + md5 + extension;

		String contentType = fileService.getContentType(filename);

		//文件上传
		minioService.uploadFile(MEDIA_FILE_PATH_BUCKET, file, objectname, contentType);

		//代理对象
		proxy = (IMediaFileService) AopContext.currentProxy();
		MediaFiles mediaFiles = proxy.addMediaFilesToDB(companyId, md5, uploadFileParamsDTO, objectname);

		UploadFileResultDTO result = new UploadFileResultDTO();
		BeanUtil.copyProperties(mediaFiles, result, true);

		return result;
	}

	@Override
	@Transactional
	public MediaFiles addMediaFilesToDB(Long companyId,
	                                    String fileHash,
	                                    UploadFileParamsDTO uploadFileParamsDTO,
	                                    String objectName) {
		MediaFiles mediaFiles = mediaFilesMapper.selectById(fileHash);
		if (mediaFiles == null) {
			/*不存在，创建新的文件记录*/
			mediaFiles = new MediaFiles();

			BeanUtil.copyProperties(uploadFileParamsDTO, mediaFiles, true);
			mediaFiles
				.setId(fileHash)
				.setFileId(fileHash)
				.setCompanyId(companyId)
				.setUrl("/" + MEDIA_FILE_PATH_BUCKET + "/" + objectName)
				.setBucket(MEDIA_FILE_PATH_BUCKET)
				.setFilePath(objectName)
				.setCreateDate(LocalDateTime.now())
				.setAuditStatus("002003")
				.setStatus("1");

			int insert = mediaFilesMapper.insert(mediaFiles);
			if (insert < 0) {
				log.error("保存文件信息到数据库失败-{}", mediaFiles);
				BaseException.cast(SAVE_FILE_ERROR);
			}

			log.info("保存文件信息到数据库成功-{}", mediaFiles);
		}

		/*记录待处理任务
		 *   判断如果是avi视频则写入待处理任务
		 *   向MediaProcess写入记录
		 */
		this.addWaitingTask(mediaFiles);

		return mediaFiles;
	}

	@Override
	public RestResponse<Boolean> checkFile(String fileMD5) {
		MediaFiles mediaFiles = mediaFilesMapper.selectById(fileMD5);
		if (mediaFiles != null) {
			/*文件存在，直接返回真结果*/
			String bucket = mediaFiles.getBucket();
			String filePath = mediaFiles.getFilePath();

			try {
				minioService.getObjectInfo(bucket, filePath);
				return RestResponse.success(true);
			} catch (Exception e) {
				return RestResponse.success(false);
			}

		}

		/*文件不存在，发送一条延时消息告诉RabbitMQ，一天之后还没上传完就是中断了，直接删除相关文件与redis记录*/
		rabbitTemplate.convertAndSend(
			MQConstant.FILE_INTERRUPT_EXCHANGE_NAME,
			MQConstant.FILE_INTERRUPT_KEY,
			fileMD5,
			message -> {
				message.getMessageProperties().setDelay(86400000);
				return message;
			}
		);
		return RestResponse.success(false);
	}

	@Override
	public RestResponse<Boolean> checkChunk(String fileMD5, Integer chunkIndex) {
		String key = CHUNK_KEY + fileMD5 + ":" + chunkIndex;

		String result = redisTemplate.opsForValue().get(key);
		if (result != null) {
			return RestResponse.success(true);
		}

		return RestResponse.success(false);
	}

	@Override
	public RestResponse<Boolean> uploadChunk(String fileMD5, int chunk, String localChunkFilePath) {
		String chunkFileFolderPath = fileService.getChunkFileFolderPath(fileMD5);
		String chunkFilePath = chunkFileFolderPath + chunk;

		ObjectWriteResponse response = minioService.uploadFile(MEDIA_CHUNK_PATH_BUCKET, chunkFilePath, localChunkFilePath);
		if (response == null) {
			log.debug("上传分块文件失败:{}", chunkFilePath);
			return RestResponse.validFail(false, "上传分块失败");
		}

		String key = CHUNK_KEY + fileMD5 + ":" + chunk;
		redisTemplate.opsForValue().set(key, "upload");

		return RestResponse.success(true);
	}

	@Override
	public RestResponse<Boolean> mergechunks(Long companyId, String fileMd5, int chunkTotal, UploadFileParamsDTO uploadFileParamsDTO) throws Exception {
		/*获取分块文件路径*/
		String chunkFileFolderPath = fileService.getChunkFileFolderPath(fileMd5);

		/*将分块文件路径组成List<ComposeSource>*/
		List<String> sortedChunkFiles = fileService.getSortedChunkFiles(chunkFileFolderPath);
		if (sortedChunkFiles.isEmpty()) {
			return RestResponse.validFail(false, "未找到有效的分块文件");
		}

		/*构建composeSource列表*/
		List<ComposeSource> sourceList = sortedChunkFiles.stream()
			.map(objectName -> ComposeSource.builder()
				.bucket(MEDIA_CHUNK_PATH_BUCKET)
				.object(objectName)
				.build())
			.toList();

		String filename = uploadFileParamsDTO.getFilename();
		String extName = filename.substring(filename.lastIndexOf("."));
		String mergeFilePath = fileService.getFilePathByMd5(fileMd5, extName);

		try {
			ObjectWriteResponse response = minioService.mergeFile(MEDIA_FILE_PATH_BUCKET, mergeFilePath, sourceList);

			log.info("合并文件成功:{}-{}", mergeFilePath, response.toString());
		} catch (Exception exception) {
			log.error("文件合并失败：{}", exception.getMessage());
			return RestResponse.validFail(false, "合并文件失败");
		}

		/*验证MD5*/
		try (InputStream inputStream = minioService.getObject(MEDIA_FILE_PATH_BUCKET, mergeFilePath)) {
			String md5Hex = DigestUtils.md5Hex(inputStream);
			if (!fileMd5.equals(md5Hex)) {
				return RestResponse.validFail(false, "文件合并校验失败，最终上传失败");
			}

			uploadFileParamsDTO.setFileSize(minioService.getObjectInfo(MEDIA_FILE_PATH_BUCKET, mergeFilePath).size());
		} catch (IOException e) {
			log.debug("校验文件失败,fileMd5:{},异常:{}", fileMd5, e.getMessage(), e);
			return RestResponse.validFail(false, "文件合并校验失败，最终上传失败");
		}

		//代理对象
		proxy = (IMediaFileService) AopContext.currentProxy();
		proxy.addMediaFilesToDB(companyId, fileMd5, uploadFileParamsDTO, mergeFilePath);

		/*清除分块文件*/
		fileService.clearChunkFiles(chunkFileFolderPath);

		/*清除Redis中的分块数据*/
		redisTemplate.delete(CHUNK_KEY + fileMd5);

		return RestResponse.success(true);
	}

	@Override
	public File getFile(String bucket, String objectName) {
		//创建临时文件
		File minioFile;
		FileOutputStream outputStream = null;

		try {
			InputStream inputStream = minioService.getObject(bucket, objectName);
			minioFile = File.createTempFile("minio", ".merge");

			outputStream = new FileOutputStream(minioFile);
			IOUtils.copy(inputStream, outputStream);

			return minioFile;
		} catch (IOException exception) {
			log.error(exception.getMessage());
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException exception) {
					log.error(exception.getMessage());
				}
			}
		}

		return null;
	}

	/**
	 * 添加待处理任务
	 *
	 * @param mediaFiles 媒资文件信息
	 */
	private void addWaitingTask(MediaFiles mediaFiles) {
		String mimeType = fileService.getContentType(mediaFiles.getFilename());
		if (mimeType.equals("video/x-msvideo")) {
			MediaProcess mediaProcess = new MediaProcess();

			BeanUtil.copyProperties(mediaFiles, mediaProcess, true);

			mediaProcess
				.setStatus("1")
				.setCreateDate(LocalDateTime.now())
				.setFailCount(0);

			mediaProcessMapper.insert(mediaProcess);
		}
	}
}
