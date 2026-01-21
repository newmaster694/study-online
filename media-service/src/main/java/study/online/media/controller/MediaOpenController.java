package study.online.media.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.online.base.exception.BaseException;
import study.online.base.model.RestResponse;
import study.online.media.model.po.MediaFiles;
import study.online.media.service.IMediaFileService;

import static study.online.base.constant.ErrorMessage.QUERY_NULL;

@RestController
@RequiredArgsConstructor
@RequestMapping("/open")
public class MediaOpenController {

	private final IMediaFileService mediaFileService;

	@GetMapping("/preview/{mediaId}")
	public RestResponse<String> getPlayURLByMediaId(@PathVariable String mediaId) {
		MediaFiles mediaFiles = mediaFileService.getById(mediaId);
		if (mediaFiles == null) {
			BaseException.cast(QUERY_NULL);
		}

		if (mediaFiles.getUrl() == null) {
			BaseException.cast(QUERY_NULL);
		}

		return RestResponse.success(mediaFiles.getUrl());
	}
}
