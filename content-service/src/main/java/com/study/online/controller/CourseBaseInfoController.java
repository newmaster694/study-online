package com.study.online.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.study.online.model.PageParams;
import com.study.online.model.dto.QueryCourseParamsDTO;
import com.study.online.model.po.CourseBase;
import com.study.online.service.ICourseBaseInfoService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/content")
public class CourseBaseInfoController {
	
	@Resource
	private ICourseBaseInfoService courseBaseInfoService;
	
	@PostMapping("/course/list")
	public Page<CourseBase> list(PageParams pageParams,
			@RequestBody QueryCourseParamsDTO queryCourseParamsDTO) {
		return courseBaseInfoService.queryCourseBaseList(pageParams, queryCourseParamsDTO);
	}
}
