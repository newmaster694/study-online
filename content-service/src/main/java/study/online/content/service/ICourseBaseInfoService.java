package study.online.content.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import study.online.base.content.model.PageParams;
import study.online.content.model.dto.AddCourseDTO;
import study.online.content.model.dto.CourseBaseInfoDTO;
import study.online.content.model.dto.EditCourseDTO;
import study.online.content.model.dto.QueryCourseParamsDTO;
import study.online.content.model.po.CourseBase;

/**
 * <p>课程基本信息管理业务接口</p>
 *
 * @since 2025/6/4
 * @author newmaster
 */
public interface ICourseBaseInfoService extends IService<CourseBase> {

	/**
	 * <p>课程查询接口</p>
	 *
	 * @param pageParams           分页参数
	 * @param queryCourseParamsDTO 分页条件
	 * @return Page(CourseBase)
	 * @since 2025/6/4
	 */
	Page<CourseBase> queryCourseBaseList(PageParams pageParams, QueryCourseParamsDTO queryCourseParamsDTO);

	/**
	 * <p>添加课程基本信息</p>
	 *
	 * @param companyId 教学机构id
	 * @param addCourseDTO 课程基本信息
	 * @return CourseBaseInfoDTO
	 *
	 * @since 2025/6/6
	 */
	CourseBaseInfoDTO createCourseBase(Long companyId, AddCourseDTO addCourseDTO);

	/**
	 * <p>根据id查询课程基本信息</p>
	 *
	 * @param courseId 课程id
	 * @return CourseBaseInfoDTO
	 */
	CourseBaseInfoDTO getCourseBaseInfo(long courseId);

	/**
	 * <p>修改课程信息</p>
	 * @param companyId 机构id
	 * @param editCourseDTO 课程信息
	 * @return CourseBaseInfoDTO
	 */
	CourseBaseInfoDTO updateCourseBase(Long companyId, EditCourseDTO editCourseDTO);
}
