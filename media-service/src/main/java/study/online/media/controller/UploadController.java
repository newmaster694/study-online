package study.online.media.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import study.online.media.model.dto.UploadFileParamsDTO;
import study.online.media.model.dto.UploadFileResultDTO;
import study.online.media.service.IUploadService;

import java.io.File;
import java.io.IOException;

@RestController
@Slf4j
public class UploadController {

	@Resource
	private IUploadService uploadService;

	/**
	 * 上传文件接口
	 * @param file 文件
	 * @param folder 文件夹
	 * @param objectname 文件名
	 * @return UploadFileResultDTO
	 * @throws IOException IO异常
	 */
	@PostMapping(value = "/upload/coursefile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public UploadFileResultDTO upload(
		@RequestPart("filedata")MultipartFile file,
		@RequestParam(value = "folder", required = false) String folder,
		@RequestParam(value = "objectname", required = false) String objectname) throws IOException {

		Long companyId = 1232141425L;

		UploadFileParamsDTO uploadFileParamsDTO = new UploadFileParamsDTO();
		uploadFileParamsDTO
			.setFileSize(file.getSize())
			.setFileType("001001")
			.setFilename(file.getOriginalFilename());

		File tempFile = File.createTempFile("minio", "temp");
		file.transferTo(tempFile);
		String absolutePath = tempFile.getAbsolutePath();//获取绝对路径

		return uploadService.uploadFile(companyId, uploadFileParamsDTO, absolutePath);
	}
}
