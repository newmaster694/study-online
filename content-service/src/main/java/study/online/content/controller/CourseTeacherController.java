package study.online.content.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import study.online.base.exception.ValidationGroup;
import study.online.content.model.dto.SaveCourseTeacherDTO;
import study.online.content.model.po.CourseTeacher;
import study.online.content.service.ICourseTeacherService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/course-teacher")
public class CourseTeacherController {

	@Resource
	private ICourseTeacherService courseTeacherService;

	/**
	 * 师资信息列表查询接口
	 *
	 * @param courseId 课程id
	 * @return {@code List<CourseTeacher>}
	 */
	@GetMapping("/list/{courseId}")
	public List<CourseTeacher> getList(@PathVariable Long courseId) {
		return courseTeacherService.getTeacherList(courseId);
	}

	/**
	 * 新增/修改教师接口
	 *
	 * @param courseTeacherDTO 新增的教师记录
	 * @return 新增的教师记录
	 */
	@PostMapping
	public CourseTeacher add(@RequestBody @Validated(
		{ValidationGroup.Inster.class, ValidationGroup.Update.class}) SaveCourseTeacherDTO courseTeacherDTO) {
		Long companyId = 1232141425L;
		return courseTeacherService.addTeacherInfo(courseTeacherDTO, companyId);
	}

	/**
	 * 删除教师资料接口
	 *
	 * @param courseId        相关课程信息id
	 * @param courseTeacherId 相关教师资料id
	 */
	@DeleteMapping("/course/{courseId}/{courseTeacherId}")
	public void deleteItem(@PathVariable Long courseId, @PathVariable Long courseTeacherId) {
		Long companyId = 1232141425L;
		courseTeacherService.deleteItem(courseId, courseTeacherId, companyId);
	}
}
