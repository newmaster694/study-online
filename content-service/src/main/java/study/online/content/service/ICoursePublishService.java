package study.online.content.service;

import study.online.content.model.dto.CoursePreviewDTO;

public interface ICoursePublishService {

	/**
	 * 获取课程预览信息
	 *
	 * @param courseId 课程id
	 * @return CoursePreviewDTO
	 */
	CoursePreviewDTO getCoursePreviewInfo(Long courseId);
}
