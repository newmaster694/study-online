package com.study.online.content.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.online.base.execption.CreateCourseBaseExecption;
import com.study.online.base.execption.CreateCourseMarketExecption;
import com.study.online.base.execption.NullValueExecption;
import com.study.online.content.mapper.CourseBaseMapper;
import com.study.online.base.content.model.PageParams;
import com.study.online.content.model.dto.AddCourseDTO;
import com.study.online.content.model.dto.CourseBaseInfoDTO;
import com.study.online.content.model.dto.QueryCourseParamsDTO;
import com.study.online.content.model.po.CourseBase;
import com.study.online.content.model.po.CourseCategory;
import com.study.online.content.model.po.CourseMarket;
import com.study.online.content.service.ICourseBaseInfoService;
import com.study.online.content.service.ICourseCategoryService;
import com.study.online.content.service.ICourseMarketService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class CourseBaseInfoServiceImpl extends ServiceImpl<CourseBaseMapper, CourseBase>
	implements ICourseBaseInfoService {

	@Resource
	private ICourseMarketService courseMarketService;

	@Resource
	private ICourseCategoryService courseCategoryService;

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

	@Override
	@Transactional
	public CourseBaseInfoDTO createCourseBase(Long companyId, AddCourseDTO addCourseDTO) {
		//合法性校验
		if (StrUtil.isBlank(addCourseDTO.getName())) {
			throw new NullValueExecption("课程名称为空");
		}

		if (StrUtil.isBlank(addCourseDTO.getMt()) || StrUtil.isBlank(addCourseDTO.getSt())) {
			throw new NullValueExecption("课程分类为空，大分类与小分类都要填哦");
		}

		if (StrUtil.isBlank(addCourseDTO.getGrade())) {
			throw new NullValueExecption("课程等级为空");
		}

		if (StrUtil.isBlank(addCourseDTO.getTeachmode())) {
			throw new NullValueExecption("教育模式为空");
		}

		if (StrUtil.isBlank(addCourseDTO.getUsers())) {
			throw new NullValueExecption("适应人群为空");
		}

		if (StrUtil.isBlank(addCourseDTO.getCharge())) {
			throw new NullValueExecption("收费规则为空");
		}

		//保存课程基本信息
		CourseBase courseBase = new CourseBase();
		BeanUtil.copyProperties(addCourseDTO, courseBase, true);

		courseBase
			.setAuditStatus("202002")//设置审核状态
			.setStatus("203001")//设置发布状态
			.setCompanyId(companyId)//设置机构id
			.setCreateDate(LocalDateTime.now());//设置添加时间

		boolean flagOfSaveCourseBase = this.save(courseBase);

		if (!flagOfSaveCourseBase) {
			throw new CreateCourseBaseExecption("新增课程基本信息失败");
		}

		//向课程营销表保存课程营销信息
		CourseMarket courseMarket = new CourseMarket();
		BeanUtil.copyProperties(addCourseDTO, courseMarket, true);
		courseMarket.setId(courseBase.getId());

		boolean flagOfSaveCourseMarket = this.saveCourseMarket(courseMarket);

		if (!flagOfSaveCourseMarket) {
			throw new CreateCourseMarketExecption("新增课营销信息失败");
		}

		//查询课程基本信息及营销信息并返回
		return this.getCourseBaseInfo(courseBase.getId());
	}

	/**
	 * <p>保存课程营销信息方法</p>
	 *
	 * @param courseMarket 要保存的课程营销记录
	 * @return boolean-是否保存成功标志
	 */
	private boolean saveCourseMarket(CourseMarket courseMarket) {
		//收费规则
		if (StrUtil.isBlank(courseMarket.getCharge())) {
			throw new NullValueExecption("收费规则没有选择");
		}

		if (courseMarket.getCharge().equals("201001")) {
			if (courseMarket.getPrice() == null || courseMarket.getPrice() <= 0) {
				throw new CreateCourseMarketExecption("收费课程的收费金额必须要大于0！");
			}
		}

		CourseMarket selectCourseMarket = courseMarketService.getById(courseMarket.getId());
		//判断数据库中是否已经存在这个营销数据：不存在=>新增；存在=>更新
		if (selectCourseMarket == null) {
			return courseMarketService.save(courseMarket);
		} else {
			return courseMarketService.updateById(courseMarket);
		}
	}

	/**
	 * <p>根据id查询课程基本信息</p>
	 *
	 * @param courseId 课程id
	 * @return CourseBaseInfoDTO
	 */
	private CourseBaseInfoDTO getCourseBaseInfo(long courseId) {
		CourseBase courseBase = this.getById(courseId);

		if (courseBase==null) {
			return null;
		}

		CourseMarket courseMarket = courseMarketService.getById(courseId);

		CourseBaseInfoDTO courseBaseInfoDTO = new CourseBaseInfoDTO();

		BeanUtil.copyProperties(courseBase, courseBaseInfoDTO, true);
		if (courseMarket != null) {
			BeanUtil.copyProperties(courseMarket, courseBaseInfoDTO, true);
		}

		//查询分类名称
		CourseCategory categoryBySt = courseCategoryService.getById(courseBase.getSt());
		CourseCategory categoryByMt = courseCategoryService.getById(courseBase.getMt());

		courseBaseInfoDTO
			.setStName(categoryBySt.getName())
			.setMtName(categoryByMt.getName());

		return courseBaseInfoDTO;
	}
}
