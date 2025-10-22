package study.online.search.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.online.base.model.PageParams;
import study.online.search.dto.SearchCourseParamDto;
import study.online.search.dto.SearchPageResultDto;
import study.online.search.po.CourseIndex;
import study.online.search.service.ICourseSearchService;

/**
 * 课程搜索接口
 *
 * @author Mr.M
 * @version 1.0
 * @since 2022/9/24 22:31
 */
@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseSearchController {

	private final ICourseSearchService courseSearchService;

	@GetMapping("/list")
	public SearchPageResultDto<CourseIndex> list(PageParams pageParams, SearchCourseParamDto searchCourseParamDto) {
		return courseSearchService.queryCoursePubIndex(pageParams, searchCourseParamDto);
	}
}
