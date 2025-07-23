package study.online.content.service;

import study.online.content.model.dto.SaveCourseTeacherDTO;
import study.online.content.model.po.CourseTeacher;

import java.util.List;

/**
 * <p>师资信息接口</p>
 *
 * @author newmaster
 * @since 2026/6/12
 */
public interface ICourseTeacherService {

	/**
	 * 师资信息列表查询
	 *
	 * @param courseId 课程id
	 * @return {@code List<CourseTeacher>}
	 */
	List<CourseTeacher> getTeacherList(Long courseId);

	/**
	 * 新增/修改教师接口
	 *
	 * @param courseTeacherDTO 新增的教师记录
	 * @param companyId        机构id
	 * @return 新增的教师记录
	 */
	CourseTeacher addTeacherInfo(SaveCourseTeacherDTO courseTeacherDTO, Long companyId);

	/**
	 * 删除课程夫关联教师资料接口
	 *
	 * @param courseId        相关课程信息id
	 * @param courseTeacherId 相关教师资料id
	 * @param companyId       机构id
	 */
	void deleteItem(Long courseId, Long courseTeacherId, Long companyId);

	void removeByIds(List<Long> teacherIds);
}
