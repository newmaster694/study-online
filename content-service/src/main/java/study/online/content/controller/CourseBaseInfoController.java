package study.online.content.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import study.online.base.content.model.PageParams;
import study.online.base.execption.ValidationGroup;
import study.online.content.model.dto.*;
import study.online.content.model.po.CourseBase;
import study.online.content.service.ICourseBaseInfoService;
import study.online.content.service.ICourseCategoryService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/content")
public class CourseBaseInfoController {

	@Resource
	private ICourseBaseInfoService courseBaseInfoService;

	@Resource
	private ICourseCategoryService courseCategoryService;

	/**
	 * <p>查询课程列表（分页）接口</p>
	 *
	 * @param pageParams           基础分页查询参数
	 * @param queryCourseParamsDTO 课程分页查询参数
	 * @return Mybatis-Plus->Page(CourseBase)
	 */
	@PostMapping("/course/list")
	public Page<CourseBase> list(
			PageParams pageParams,
			@RequestBody QueryCourseParamsDTO queryCourseParamsDTO) {
		return courseBaseInfoService.queryCourseBaseList(pageParams, queryCourseParamsDTO);
	}

	/**
	 * <p>查询课程分类树节点接口</p>
	 *
	 * @return List(CourseCategoryTreeDTO)
	 */
	@GetMapping("/course-category/tree-nodes")
	public List<CourseCategoryTreeDTO> queryTreeNodes() {
		return courseCategoryService.queryTreeNodes("1");
	}

	/**
	 * <p>创建课程接口</p>
	 *
	 * @param addCourseDTO 创建课程信息
	 * @return 课程基础信息&课程营销信息
	 */
	@PostMapping("/course")
	public CourseBaseInfoDTO createCourseBase(
			@RequestBody @Validated({ValidationGroup.Inster.class}) AddCourseDTO addCourseDTO) {
		//机构ID，由于认证系统还未上线，先硬编码
		Long companyId = 1232141425L;
		return courseBaseInfoService.createCourseBase(companyId, addCourseDTO);
	}

	/**
	 * <p>根据id查询课程信息接口</p>
	 *
	 * @param courseId 课程id
	 * @return 课程基础信息&课程营销信息
	 */
	@GetMapping("/course/{courseId}")
	public CourseBaseInfoDTO getCourseBaseById(@PathVariable Long courseId) {
		return courseBaseInfoService.getCourseBaseInfo(courseId);
	}

	/**
	 * <p>修改课程基础信息接口</p>
	 *
	 * @param editCourseDTO 要修改的课程记录
	 * @return 课程基础信息&课程营销信息
	 */
	@PutMapping("/course")
	public CourseBaseInfoDTO modifyCourseBase(
			@RequestBody @Validated(ValidationGroup.Update.class) EditCourseDTO editCourseDTO) {
		//机构id，由于认证系统没有上线暂时硬编码
		Long companyId = 1232141425L;
		return courseBaseInfoService.updateCourseBase(companyId, editCourseDTO);
	}
}
