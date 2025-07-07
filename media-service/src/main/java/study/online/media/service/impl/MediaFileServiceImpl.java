package study.online.media.service.impl;

import cn.hutool.core.bean.BeanUtil;
import io.minio.ComposeSource;
import io.minio.ObjectWriteResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import study.online.base.exception.BaseException;
import study.online.base.model.RestResponse;
import study.online.media.mapper.MediaFilesMapper;
import study.online.media.model.dto.UploadFileParamsDTO;
import study.online.media.model.dto.UploadFileResultDTO;
import study.online.media.model.po.MediaFiles;
import study.online.media.service.IMediaFileService;
import study.online.media.utils.FileUtil;
import study.online.media.utils.MinioUtil;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

import static study.online.base.constant.MediaFilePathConstant.MEDIA_CHUNK_PATH_BUCKET;
import static study.online.base.constant.MediaFilePathConstant.MEDIA_FILE_PATH_BUCKET;

@Service
@Slf4j
@RequiredArgsConstructor
public class MediaFileServiceImpl implements IMediaFileService {

	private final MinioUtil minioUtil;
	private final MediaFilesMapper mediaFilesMapper;
	private final FileUtil fileUtil;

	private IMediaFileService proxy;

	@Override
	public UploadFileResultDTO uploadFile(Long companyId, UploadFileParamsDTO uploadFileParamsDTO, MultipartFile file) {
		if (file.isEmpty()) {
			BaseException.cast("文件不存在");
		}

		String filename = uploadFileParamsDTO.getFilename();
		String extension = filename.substring(filename.lastIndexOf("."));

		String md5 = fileUtil.getFileMd5(file);
		String defaultFolderPath = fileUtil.getDefaultFolderPath();

		//拼接objectname
		String objectname = defaultFolderPath + md5 + extension;

		String contentType = fileUtil.getContentType(filename);

		//文件上传
		minioUtil.uploadFile(MEDIA_FILE_PATH_BUCKET, file, objectname, contentType);

		//代理对象
		proxy = (IMediaFileService) AopContext.currentProxy();

		MediaFiles mediaFiles = proxy.addMediaFilesToDB(companyId, md5, uploadFileParamsDTO, objectname);

		UploadFileResultDTO result = new UploadFileResultDTO();
		BeanUtil.copyProperties(mediaFiles, result, true);

		return result;
	}

	@Override
	@Transactional
	public MediaFiles addMediaFilesToDB(Long companyId, String fileHash, UploadFileParamsDTO uploadFileParamsDTO, String objectName) {
		MediaFiles mediaFiles = mediaFilesMapper.selectById(fileHash);
		if (mediaFiles == null) {
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
				log.error("保存文件信息到数据库失败,{}", mediaFiles);
				BaseException.cast("保存文件信息失败");
			}

			log.info("保存文件信息到数据库成功,{}", mediaFiles);
		}

		return mediaFiles;
	}

	@Override
	public RestResponse<Boolean> checkFile(String fileMD5) {
		MediaFiles mediaFiles = mediaFilesMapper.selectById(fileMD5);
		if (mediaFiles != null) {
			String bucket = mediaFiles.getBucket();
			String filePath = mediaFiles.getFilePath();
			if (minioUtil.getObject(bucket, filePath) != null) {
				return RestResponse.success(true);
			}
		}

		return RestResponse.success(false);
	}

	@Override
	public RestResponse<Boolean> checkChunk(String fileMD5, Integer chunkIndex) {
		String chunkFileFolderPath = fileUtil.getChunkFileFolderPath(fileMD5);
		String chunkFilePath = chunkFileFolderPath + chunkIndex;

		if (minioUtil.getObject(MEDIA_CHUNK_PATH_BUCKET, chunkFilePath) != null) {
			return RestResponse.success(true);
		}

		return RestResponse.success(false);
	}

	@Override
	public RestResponse<Boolean> uploadChunk(String fileMD5, int chunk, String localChunkFilePath) {
		String chunkFileFolderPath = fileUtil.getChunkFileFolderPath(fileMD5);
		String chunkFilePath = chunkFileFolderPath + chunk;

		ObjectWriteResponse response = minioUtil.uploadFile(MEDIA_CHUNK_PATH_BUCKET, chunkFilePath, localChunkFilePath);
		if (response == null) {
			log.debug("上传分块文件失败:{}", chunkFilePath);
			return RestResponse.validFail(false, "上传分块失败");
		}

		return RestResponse.success(true);
	}

	@Override
	public RestResponse<Boolean> mergechunks(Long companyId, String fileMd5, int chunkTotal, UploadFileParamsDTO uploadFileParamsDTO) throws Exception {
		/*获取分块文件路径*/
		String chunkFileFolderPath = fileUtil.getChunkFileFolderPath(fileMd5);

		/*将分块文件路径组成List<ComposeSource>*/
		List<String> sortedChunkFiles = fileUtil.getSortedChunkFiles(chunkFileFolderPath);
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
		String mergeFilePath = fileUtil.getFilePathByMd5(fileMd5, extName);

		try {
			ObjectWriteResponse response = minioUtil.mergeFile(MEDIA_FILE_PATH_BUCKET, mergeFilePath, sourceList);

			log.info("合并文件成功:{}-{}", mergeFilePath, response.toString());
		} catch (Exception exception) {
			log.error("文件合并失败：{}", exception.getMessage());
			return RestResponse.validFail(false, "合并文件失败");
		}

		/*验证MD5*/
		try (InputStream inputStream = minioUtil.getObject(MEDIA_FILE_PATH_BUCKET, mergeFilePath)) {
			String md5Hex = DigestUtils.md5Hex(inputStream);
			if (!fileMd5.equals(md5Hex)) {
				return RestResponse.validFail(false, "文件合并校验失败，最终上传失败");
			}

			uploadFileParamsDTO.setFileSize(minioUtil.getObjectInfo(MEDIA_FILE_PATH_BUCKET, mergeFilePath).size());
		} catch (IOException e) {
			log.debug("校验文件失败,fileMd5:{},异常:{}", fileMd5, e.getMessage(), e);
			return RestResponse.validFail(false, "文件合并校验失败，最终上传失败。");
		}

		proxy.addMediaFilesToDB(companyId, fileMd5, uploadFileParamsDTO, mergeFilePath);

		/*清除分块文件*/
		fileUtil.clearChunkFiles(chunkFileFolderPath);
		return RestResponse.success(true);
	}
}
