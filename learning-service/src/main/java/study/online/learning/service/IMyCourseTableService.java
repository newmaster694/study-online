package study.online.learning.service;

import study.online.learning.model.dto.XcChooseCourseDTO;
import study.online.learning.model.dto.XcCourseTablesDTO;

public interface IMyCourseTableService {
	/**
	 * 添加选课
	 *
	 * @param userId   用户id
	 * @param courseId 课程id
	 * @return com.xuecheng.learning.model.dto.XcChooseCourseDTO
	 */
	XcChooseCourseDTO addChooseCourse(Long userId, Long courseId);

	/**
	 * 判断学习资格 [{"code":"702001","desc":"正常学习"},{"code":"702002","desc":"没有选课或选课后没有支付"},{"code":"702003","desc":"已过期需要申请续期或重新支付"}]
	 */
	XcCourseTablesDTO getLearningStatus(Long userId, Long courseId);
}
