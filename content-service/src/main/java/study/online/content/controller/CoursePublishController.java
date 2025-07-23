package study.online.content.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import study.online.base.exception.BaseException;
import study.online.content.model.dto.CoursePreviewDTO;
import study.online.content.service.ICoursePublishService;

import static study.online.base.constant.ErrorMessageConstant.PREVIEW_MODEL_ERROR;

@Controller
@RequiredArgsConstructor
public class CoursePublishController {

	private final ICoursePublishService coursePublishService;

	/**
	 * 预览课程接口
	 *
	 * @param courseId 课程id
	 * @return ModelAndView
	 */
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

	/**
	 * 提交审核接口
	 *
	 * @param courseId 课程id
	 */
	@ResponseBody
	@PostMapping("/courseaudit/commit/{courseId}")
	public void commitAudit(@PathVariable Long courseId) {
		Long companyId = 1232141425L;
		coursePublishService.commitAudit(companyId, courseId);
	}

	/**
	 * 课程发布接口
	 *
	 * @param courseId 课程id
	 */
	@ResponseBody
	@PostMapping("/coursepublish/{courseId}")
	public void coursePublish(@PathVariable Long courseId) {
		Long companyId = 1232141425L;
		coursePublishService.publish(companyId, courseId);
	}
}
