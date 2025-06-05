package com.study.online.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.study.online.model.PageParams;
import com.study.online.model.dto.CourseCategoryTreeDTO;
import com.study.online.model.dto.QueryCourseParamsDTO;
import com.study.online.model.po.CourseBase;
import com.study.online.service.ICourseBaseInfoService;
import com.study.online.service.ICourseCategoryService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/content")
public class CourseBaseInfoController {

	@Resource
	private ICourseBaseInfoService courseBaseInfoService;

	@Resource
	private ICourseCategoryService courseCategoryService;

	@PostMapping("/course/list")
	public Page<CourseBase> list(PageParams pageParams,
			@RequestBody QueryCourseParamsDTO queryCourseParamsDTO) {
		return courseBaseInfoService.queryCourseBaseList(pageParams, queryCourseParamsDTO);
	}

	@GetMapping("/course-category/tree-nodes")
	public List<CourseCategoryTreeDTO> queryTreeNodes() {
		return courseCategoryService.queryTreeNodes("1");
	}
}
