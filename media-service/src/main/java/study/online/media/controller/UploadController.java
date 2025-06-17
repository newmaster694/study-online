package study.online.media.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import study.online.base.model.RestResponse;
import study.online.media.model.dto.UploadFileParamsDTO;
import study.online.media.model.dto.UploadFileResultDTO;
import study.online.media.service.IMediaFileService;

@RestController
@Slf4j
@RequestMapping("/upload")
public class UploadController {

	@Resource
	private IMediaFileService mediaFileService;

	/**
	 * 上传课程相关（图片）文件接口
	 *
	 * @param file 文件
	 * @return UploadFileResultDTO
	 */
	@PostMapping(value = "/coursefile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public UploadFileResultDTO upload(@RequestPart("filedata") MultipartFile file) {

		Long companyId = 1232141425L;

		UploadFileParamsDTO uploadFileParamsDTO = new UploadFileParamsDTO();
		uploadFileParamsDTO
			.setFileSize(file.getSize())
			.setFileType("001001")
			.setFilename(file.getOriginalFilename());

		return mediaFileService.uploadFile(companyId, uploadFileParamsDTO, file);
	}

	/**
	 * 文件上传前检查文件接口
	 *
	 * @param fileMD5 上传文件的md5值
	 * @return {@code  RestResponse<Boolean>}
	 */
	@PostMapping("/checkfile")
	public RestResponse<Boolean> checkFile(@RequestParam("fileMd5") String fileMD5) {
		return mediaFileService.checkFile(fileMD5);
	}

	/**
	 * 分块文件上传前的检测接口
	 *
	 * @param fileMD5 上传文件的md5值
	 * @param chunk   分块文件的序号
	 * @return {@code RestResponse<Boolean>}
	 */
	@PostMapping("/checkchunk")
	public RestResponse<Boolean> checkChunk(
		@RequestParam("fileMd5") String fileMD5,
		@RequestParam("chunk") int chunk
	) {
		return mediaFileService.checkChunk(fileMD5, chunk);
	}

	/**
	 * 上传分块文件接口
	 *
	 * @param file 上传的分块文件
	 * @param fileMD5 文件的MD5值
	 * @param chunk 分块文件的序号
	 * @return {@code RestResponse<Object>}
	 */
	@PostMapping("/uploadchunk")
	public RestResponse<Object> uploadchunk(
		@RequestParam("file") MultipartFile file,
		@RequestParam("fileMd5") String fileMD5,
		@RequestParam("chunk") int chunk
		) {
		return null;
	}
}
