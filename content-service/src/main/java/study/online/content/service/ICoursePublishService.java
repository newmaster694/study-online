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

	/**
	 * 课程发布接口
	 *
	 * @param companyId 机构id
	 * @param courseId  课程id
	 */
	void publish(Long companyId, Long courseId);
}
