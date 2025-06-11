package study.online.content.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import study.online.base.execption.ValidationGroup;
import study.online.base.model.PageParams;
import study.online.content.model.dto.AddCourseDTO;
import study.online.content.model.dto.CourseBaseInfoDTO;
import study.online.content.model.dto.EditCourseDTO;
import study.online.content.model.dto.QueryCourseParamsDTO;
import study.online.content.model.po.CourseBase;
import study.online.content.service.ICourseBaseInfoService;

@RestController
@Slf4j
@RequestMapping("/course")
public class CourseBaseInfoController {

	@Resource
	private ICourseBaseInfoService courseBaseInfoService;

	/**
	 * 查询课程列表（分页）接口
	 *
	 * @param pageParams           基础分页查询参数
	 * @param queryCourseParamsDTO 课程分页查询参数
	 * @return Mybatis-Plus->Page(CourseBase)
	 */
	@PostMapping("/list")
	public Page<CourseBase> list(
		PageParams pageParams,
		@RequestBody QueryCourseParamsDTO queryCourseParamsDTO) {
		return courseBaseInfoService.queryCourseBaseList(pageParams, queryCourseParamsDTO);
	}

	/**
	 * 创建课程接口
	 *
	 * @param addCourseDTO 创建课程信息
	 * @return 课程基础信息&课程营销信息
	 */
	@PostMapping
	public CourseBaseInfoDTO createCourseBase(
		@RequestBody @Validated({ValidationGroup.Inster.class}) AddCourseDTO addCourseDTO) {
		//机构ID，由于认证系统还未上线，先硬编码
		Long companyId = 1232141425L;
		return courseBaseInfoService.createCourseBase(companyId, addCourseDTO);
	}

	/**
	 * 根据id查询课程信息接口
	 *
	 * @param courseId 课程id
	 * @return 课程基础信息&课程营销信息
	 */
	@GetMapping("/{courseId}")
	public CourseBaseInfoDTO getCourseBaseById(@PathVariable Long courseId) {
		return courseBaseInfoService.getCourseBaseInfo(courseId);
	}

	/**
	 * 修改课程基础信息接口
	 *
	 * @param editCourseDTO 要修改的课程记录
	 * @return 课程基础信息&课程营销信息
	 */
	@PutMapping
	public CourseBaseInfoDTO modifyCourseBase(
		@RequestBody @Validated(ValidationGroup.Update.class) EditCourseDTO editCourseDTO) {
		//机构id，由于认证系统没有上线暂时硬编码
		Long companyId = 1232141425L;
		return courseBaseInfoService.updateCourseBase(companyId, editCourseDTO);
	}
}
