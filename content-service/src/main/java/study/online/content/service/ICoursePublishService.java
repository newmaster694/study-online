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

	/**
	 * 提交审核接口
	 *
	 * @param companyId 机构名称
	 * @param courseId  课程id
	 */
	void commitAudit(Long companyId, Long courseId);
}
