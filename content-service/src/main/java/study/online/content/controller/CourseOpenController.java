package study.online.content.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.online.content.model.dto.CoursePreviewDTO;
import study.online.content.service.ICoursePublishService;

/**
 * 开放api接口（未登录也可访问）
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/open")
public class CourseOpenController {

	private final ICoursePublishService coursePublishService;

	/**
	 * 课程预览接口
	 */
	@GetMapping("/course/whole/{courseId}")
	public CoursePreviewDTO getPreviewInfo(@PathVariable Long courseId) {
		return coursePublishService.getCoursePreviewInfo(courseId);
	}
}
