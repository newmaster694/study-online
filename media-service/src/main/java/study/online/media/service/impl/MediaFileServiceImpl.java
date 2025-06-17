package study.online.media.service.impl;

import cn.hutool.core.bean.BeanUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
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
import study.online.media.utils.MinioUtil;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import static study.online.base.constant.MediaFilePathConstant.MEDIA_CHUNK_PATH_BUCKET;
import static study.online.base.constant.MediaFilePathConstant.MEDIA_FILE_PATH_BUCKET;

@Service
@Slf4j
public class MediaFileServiceImpl implements IMediaFileService {

	@Resource
	private MinioUtil minioUtil;

	@Resource
	private MediaFilesMapper mediaFilesMapper;

	@Override
	public UploadFileResultDTO uploadFile(Long companyId, UploadFileParamsDTO uploadFileParamsDTO, MultipartFile file) {
		if (file.isEmpty()) {
			BaseException.cast("文件不存在");
		}

		String filename = uploadFileParamsDTO.getFilename();
		String extension = filename.substring(filename.lastIndexOf("."));

		String md5 = this.getFileMd5(file);
		String defaultFolderPath = this.getDefaultFolderPath();

		//拼接objectname
		String objectname = defaultFolderPath + md5 + extension;

		String contentType = this.getContentType(filename);

		//文件上传
		minioUtil.uploadFile(MEDIA_FILE_PATH_BUCKET, file, objectname, contentType);

		//代理对象
		IMediaFileService proxy = (IMediaFileService) AopContext.currentProxy();

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
		String chunkFileFolderPath = this.getChunkFileFolderPath(fileMD5);
		String chunkFilePath = chunkFileFolderPath + chunkIndex;

		if (minioUtil.getObject(MEDIA_CHUNK_PATH_BUCKET, chunkFilePath) != null) {
			return RestResponse.success(true);
		}

		return RestResponse.success(false);
	}

	/*得到分块文件目录*/
	private String getChunkFileFolderPath(String fileMd5) {
		return fileMd5.charAt(0) + "/" + fileMd5.charAt(1) + "/" + fileMd5 + "/" + "chunk" + "/";
	}

	/*获取文件的md5*/
	private String getFileMd5(MultipartFile file) {
		try (InputStream inputStream = file.getInputStream()) {
			return DigestUtils.md5Hex(inputStream);
		} catch (Exception e) {
			log.error("获取文件md5值失败:{}", e.getMessage());
			return null;
		}
	}

	/*获取文件默认存储目录路径 年/月/日*/
	private String getDefaultFolderPath() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new Date()).replace("-", "/") + "/";
	}

	/*根据文件名，设置HttpServletResponse的ContentType*/
	private String getContentType(String fileName) {
		Optional<MediaType> mediaType = MediaTypeFactory.getMediaType(fileName);
		return String.valueOf(mediaType.orElse(MediaType.APPLICATION_OCTET_STREAM));
	}
}
