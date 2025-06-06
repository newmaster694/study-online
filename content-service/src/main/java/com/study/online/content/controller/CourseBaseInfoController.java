package com.study.online.content.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.study.online.base.content.model.PageParams;
import com.study.online.content.model.dto.AddCourseDTO;
import com.study.online.content.model.dto.CourseBaseInfoDTO;
import com.study.online.content.model.dto.CourseCategoryTreeDTO;
import com.study.online.content.model.dto.QueryCourseParamsDTO;
import com.study.online.content.model.po.CourseBase;
import com.study.online.content.service.ICourseBaseInfoService;
import com.study.online.content.service.ICourseCategoryService;
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
			@RequestBody(required = false) QueryCourseParamsDTO queryCourseParamsDTO) {
		return courseBaseInfoService.queryCourseBaseList(pageParams, queryCourseParamsDTO);
	}

	@GetMapping("/course-category/tree-nodes")
	public List<CourseCategoryTreeDTO> queryTreeNodes() {
		return courseCategoryService.queryTreeNodes("1");
	}

	@PostMapping("/course")
	public CourseBaseInfoDTO createCourseBase(@RequestBody AddCourseDTO addCourseDTO) {
		//机构ID，由于认证系统还未上线，先硬编码
		Long companyId = 1232141425L;
		return courseBaseInfoService.createCourseBase(companyId, addCourseDTO);
	}
}
