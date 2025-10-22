package study.online.search.service;


import study.online.base.model.PageParams;
import study.online.search.dto.SearchCourseParamDto;
import study.online.search.dto.SearchPageResultDto;
import study.online.search.po.CourseIndex;

/**
 * 课程搜索service
 *
 * @author Mr.M
 * @version 1.0
 * @since 2022/9/24 22:40
 */
public interface ICourseSearchService {


	/**
	 * 搜索课程列表
	 *
	 * @param pageParams           分页参数
	 * @param searchCourseParamDto 搜索条件
	 * @return com.xuecheng.base.model.PageResult<com.xuecheng.search.po.CourseIndex> 课程列表
	 * @author Mr.M
	 * @since 2022/9/24 22:45
	 */
	SearchPageResultDto<CourseIndex> queryCoursePubIndex(PageParams pageParams, SearchCourseParamDto searchCourseParamDto);

}
