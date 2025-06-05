package com.study.online.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.online.mapper.CourseBaseMapper;
import com.study.online.model.PageParams;
import com.study.online.model.dto.QueryCourseParamsDTO;
import com.study.online.model.po.CourseBase;
import com.study.online.service.ICourseBaseInfoService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CourseBaseInfoServiceImpl extends ServiceImpl<CourseBaseMapper, CourseBase>
	implements ICourseBaseInfoService {
	@Override
	public Page<CourseBase> queryCourseBaseList(PageParams pageParams, QueryCourseParamsDTO queryCourseParamsDTO) {
		LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<>();

		if (queryCourseParamsDTO != null) {
			//模糊匹配课程名字
			queryWrapper.like(!Objects.isNull(queryCourseParamsDTO.getCourseName()),
				CourseBase::getName,
				queryCourseParamsDTO.getCourseName());

			//匹配课程审核状态
			queryWrapper.eq(!Objects.isNull(queryCourseParamsDTO.getAuditStatus()),
				CourseBase::getAuditStatus,
				queryCourseParamsDTO.getAuditStatus());

			//匹配课程发布状态
			queryWrapper.eq(!Objects.isNull(queryCourseParamsDTO.getAuditStatus()),
				CourseBase::getStatus,
				queryCourseParamsDTO.getPublishStatus());
		}

		return this.page(new Page<>(pageParams.getPageNo(), pageParams.getPageSize()), queryWrapper);
	}
}
