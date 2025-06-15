package study.online.media.service;

import org.springframework.web.multipart.MultipartFile;
import study.online.media.model.dto.UploadFileParamsDTO;
import study.online.media.model.dto.UploadFileResultDTO;
import study.online.media.model.po.MediaFiles;

public interface IUploadService {
	UploadFileResultDTO uploadFile(Long companyId, UploadFileParamsDTO uploadFileParamsDTO, MultipartFile file);

	MediaFiles addMediaFilesToDB(Long companyId, String fileHash, UploadFileParamsDTO uploadFileParamsDTO, String objectName);
}
