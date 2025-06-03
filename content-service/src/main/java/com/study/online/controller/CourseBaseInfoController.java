package com.study.online.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.study.online.model.PageParams;
import com.study.online.model.dto.QueryCourseParamsDTO;
import com.study.online.model.po.CourseBase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/content")
public class CourseBaseInfoController {
	
	@RequestMapping("/course/list")
	public Page<CourseBase> list(
			PageParams pageParams,
			@RequestBody QueryCourseParamsDTO queryCourseParamsDTO) {
		return null;
	}
}
