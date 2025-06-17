package study.online.media.service;

import org.springframework.web.multipart.MultipartFile;
import study.online.base.model.RestResponse;
import study.online.media.model.dto.UploadFileParamsDTO;
import study.online.media.model.dto.UploadFileResultDTO;
import study.online.media.model.po.MediaFiles;

public interface IMediaFileService {
	/**
	 * 上传普通文件
	 *
	 * @param companyId           机构id
	 * @param uploadFileParamsDTO 上传文件的记录
	 * @param file                上传的文件
	 * @return UploadFileResultDTO
	 */
	UploadFileResultDTO uploadFile(Long companyId, UploadFileParamsDTO uploadFileParamsDTO, MultipartFile file);

	/**
	 * 添加普通文件的资料到数据库里
	 *
	 * @param companyId           机构id
	 * @param fileHash            文件MD5值
	 * @param uploadFileParamsDTO 添加文件的资料
	 * @param objectName          文件名
	 * @return MediaFiles
	 */
	MediaFiles addMediaFilesToDB(Long companyId, String fileHash, UploadFileParamsDTO uploadFileParamsDTO, String objectName);

	/**
	 * 检查上传的大文件是否存在
	 *
	 * @param fileMD5 文件MD5值
	 * @return {@code RestResponse<Boolean>}
	 */
	RestResponse<Boolean> checkFile(String fileMD5);

	/**
	 * 检查分块文件是否存在
	 *
	 * @param fileMD5    文件MD5值
	 * @param chunkIndex 分块的序号
	 * @return {@code RestResponse<Boolean>}
	 */
	RestResponse<Boolean> checkChunk(String fileMD5, Integer chunkIndex);
}
