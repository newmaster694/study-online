package study.online.content.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import study.online.base.exception.BaseException;
import study.online.content.model.dto.CoursePreviewDTO;
import study.online.content.service.ICoursePublishService;

import static study.online.base.constant.ErrorMessageConstant.PREVIEW_MODEL_ERROR;

@Controller
@RequiredArgsConstructor
public class CoursePublishController {

	private final ICoursePublishService coursePublishService;

	@GetMapping("/coursepreview/{courseId}")
	public ModelAndView preview(@PathVariable Long courseId) {
		ModelAndView modelAndView = new ModelAndView();

		CoursePreviewDTO coursePreviewInfo = coursePublishService.getCoursePreviewInfo(courseId);
		if (coursePreviewInfo == null) {
			BaseException.cast(PREVIEW_MODEL_ERROR);
		}

		modelAndView.addObject("model", coursePreviewInfo);
		modelAndView.setViewName("course_template");

		return modelAndView;
	}
}
