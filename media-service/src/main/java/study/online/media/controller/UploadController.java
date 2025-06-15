package study.online.media.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import study.online.media.model.dto.UploadFileParamsDTO;
import study.online.media.model.dto.UploadFileResultDTO;
import study.online.media.service.IUploadService;

@RestController
@Slf4j
public class UploadController {

	@Resource
	private IUploadService uploadService;

	/**
	 * 上传文件接口
	 *
	 * @param file 文件
	 * @return UploadFileResultDTO
	 */
	@PostMapping(value = "/upload/coursefile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public UploadFileResultDTO upload(
		@RequestPart("filedata") MultipartFile file) {

		Long companyId = 1232141425L;

		UploadFileParamsDTO uploadFileParamsDTO = new UploadFileParamsDTO();
		uploadFileParamsDTO
			.setFileSize(file.getSize())
			.setFileType("001001")
			.setFilename(file.getOriginalFilename());

		return uploadService.uploadFile(companyId, uploadFileParamsDTO, file);
	}
}
