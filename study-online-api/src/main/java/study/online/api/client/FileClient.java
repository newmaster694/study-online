package study.online.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import study.online.api.config.FeignConfig;
import study.online.api.model.dto.UploadFileResultDTO;

@FeignClient(value = "media-service", configuration = FeignConfig.class)
public interface FileClient {

	@PostMapping(value = "/media/upload/coursefile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	UploadFileResultDTO uploadfile(@RequestPart("filedata") MultipartFile file,
	                               @RequestParam(value = "objectName", required = false) String objectName);
}
