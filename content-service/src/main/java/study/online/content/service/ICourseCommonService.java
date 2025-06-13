package study.online.content.service;

public interface ICourseCommonService {

	// 校验机构是否有权限操作课程
	void checkCoursePermission(Long courseId, Long companyId);

	// 校验课程是否存在
	void checkCourseExistence(Long courseId);
}
