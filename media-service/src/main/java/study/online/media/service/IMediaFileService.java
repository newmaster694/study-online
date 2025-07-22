package study.online.media.service;

import org.springframework.web.multipart.MultipartFile;
import study.online.base.model.RestResponse;
import study.online.media.model.dto.UploadFileParamsDTO;
import study.online.media.model.dto.UploadFileResultDTO;
import study.online.media.model.po.MediaFiles;

import java.io.File;

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

	/**
	 * 上传分块文件
	 *
	 * @param fileMD5 文件MD5值
	 * @param chunk   分块文件序号
	 * @param file    上传的分块文件
	 * @return {@code RestResponse<Boolean>}
	 */
	RestResponse<Boolean> uploadChunk(String fileMD5, int chunk, MultipartFile file);

	/**
	 * 合并分块
	 *
	 * @param companyId           机构id
	 * @param fileMd5             文件Hash值
	 * @param chunkTotal          分块总和
	 * @param uploadFileParamsDTO 文件信息
	 * @return {@code RestResponse<Boolean>}
	 */
	RestResponse<Boolean> mergechunks(Long companyId, String fileMd5, int chunkTotal, UploadFileParamsDTO uploadFileParamsDTO) throws Exception;

	/**
	 * 从minio上下载文件到服务器
	 *
	 * @param bucket     存储桶名称
	 * @param objectName 文件路径
	 * @return File
	 */
	File getFile(String bucket, String objectName);

	/**
	 * 清除分块文件
	 *
	 * @param chunkFileFolderPath 分块文件路径
	 */
	void clearChunkFiles(String chunkFileFolderPath);

	/**
	 * 根据id获取媒资信息
	 *
	 * @param mediaId 媒资id
	 * @return MediaFiles
	 */
	MediaFiles getById(String mediaId);
}
