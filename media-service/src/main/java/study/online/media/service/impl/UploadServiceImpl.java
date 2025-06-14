package study.online.media.service.impl;

import cn.hutool.core.bean.BeanUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.online.base.exception.BaseException;
import study.online.media.mapper.MediaFilesMapper;
import study.online.media.model.dto.UploadFileParamsDTO;
import study.online.media.model.dto.UploadFileResultDTO;
import study.online.media.model.po.MediaFiles;
import study.online.media.service.IUploadService;
import study.online.media.utils.MinioUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

@Service
@Slf4j
public class UploadServiceImpl implements IUploadService {

	@Resource
	private MinioUtil minioUtil;

	@Resource
	private MediaFilesMapper mediaFilesMapper;

	@Override
	public UploadFileResultDTO uploadFile(Long companyId, UploadFileParamsDTO uploadFileParamsDTO, String absolutePath) throws FileNotFoundException {
		File file = new File(absolutePath);
		if (file.exists()) {
			BaseException.cast("文件不存在！");
		}

		String filename = uploadFileParamsDTO.getFilename();
		String extension = filename.substring(filename.lastIndexOf("."));

		String md5 = this.getFileMd5(file);
		String defaultFolderPath = this.getDefaultFolderPath();

		//拼接objectname
		String objectname = defaultFolderPath + md5 + extension;

		//文件上传
		minioUtil.uploadFile(
			"study-online-mediafiles",
			objectname,
			new FileInputStream(file));

		uploadFileParamsDTO.setFileSize(file.length());

		//代理对象
		IUploadService proxy = (IUploadService) AopContext.currentProxy();

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
				.setUrl("/" + "study-online-mediafiles" + "/" + objectName)
				.setBucket("study-online-mediafiles")
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

	/*获取文件的md5*/
	private String getFileMd5(File file) {
		try (FileInputStream fileInputStream = new FileInputStream(file)) {
			return DigestUtils.md5Hex(fileInputStream);
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

}
