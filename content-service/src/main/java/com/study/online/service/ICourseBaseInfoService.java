package com.study.online.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.study.online.model.PageParams;
import com.study.online.model.dto.QueryCourseParamsDTO;
import com.study.online.model.po.CourseBase;

/**
 * <p>课程基本信息管理业务接口</p>
 *
 * @since 2025/6/4
 */
public interface ICourseBaseInfoService extends IService<CourseBase> {
	
	/**
	 * <p>课程查询接口</p>
	 *
	 * @param pageParams           分页参数
	 * @param queryCourseParamsDTO 分页条件
	 * @return Page(CourseBase)
	 * @since 2025/6/4
	 * @author newmaster
	 */
	Page<CourseBase> queryCourseBaseList(PageParams pageParams, QueryCourseParamsDTO queryCourseParamsDTO);
}
