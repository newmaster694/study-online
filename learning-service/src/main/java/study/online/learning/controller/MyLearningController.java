package study.online.learning.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import study.online.base.constant.ErrorMessage;
import study.online.base.exception.BaseException;
import study.online.base.utils.SecurityUtil;
import study.online.learning.model.dto.XcChooseCourseDTO;
import study.online.learning.model.dto.XcCourseTablesDTO;
import study.online.learning.service.IMyCourseTableService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/choose-course")
public class MyLearningController {

	private final IMyCourseTableService courseTableService;

	/**
	 * 添加选课
	 *
	 * @param courseId 课程id
	 */
	@PostMapping("/{courseId}")
	public XcChooseCourseDTO addChooseCourse(@PathVariable Long courseId) {
		SecurityUtil.XcUser user = SecurityUtil.getUser();
		if (user == null) {
			throw new BaseException(ErrorMessage.UNKNOW_USER_WITH_CHOOSE_COURSE);
		}

		return courseTableService.addChooseCourse(user.getId(), courseId);
	}

	/**
	 * 获取学习资格
	 *
	 * @param courseId 课程id
	 */
	@GetMapping("/learning-status/{courseId}")
	public XcCourseTablesDTO getLearningStatus(@PathVariable Long courseId) {
		SecurityUtil.XcUser user = SecurityUtil.getUser();
		if (user == null) {
			throw new BaseException(ErrorMessage.UNKNOW_USER_WITH_CHOOSE_COURSE);
		}

		return courseTableService.getLearningStatus(user.getId(), courseId);
	}
}