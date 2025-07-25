package study.online.content.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.online.base.exception.BaseException;
import study.online.base.model.PageParams;
import study.online.content.mapper.CourseBaseMapper;
import study.online.content.mapper.CourseCategoryMapper;
import study.online.content.mapper.CourseMarketMapper;
import study.online.content.model.dto.*;
import study.online.content.model.po.CourseBase;
import study.online.content.model.po.CourseCategory;
import study.online.content.model.po.CourseMarket;
import study.online.content.model.po.CourseTeacher;
import study.online.content.service.ICourseBaseInfoService;
import study.online.content.service.ICourseTeacherService;
import study.online.content.service.ITeachplanService;

import java.time.LocalDateTime;
import java.util.List;

import static study.online.base.constant.ErrorMessageConstant.*;

@Service
@RequiredArgsConstructor
public class CourseBaseInfoServiceImpl implements ICourseBaseInfoService {

	private final ITeachplanService teachplanService;
	private final ICourseTeacherService courseTeacherService;

	private final CourseBaseMapper courseBaseMapper;
	private final CourseMarketMapper courseMarketMapper;
	private final CourseCategoryMapper courseCategoryMapper;

	@Override
	public Page<CourseBase> queryCourseBaseList(PageParams pageParams, QueryCourseParamsDTO queryCourseParamsDTO) {
		LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<>();

		//模糊匹配课程名字
		queryWrapper.like(StrUtil.isNotBlank(queryCourseParamsDTO.getCourseName()),
				CourseBase::getName,
				queryCourseParamsDTO.getCourseName());

		//匹配课程审核状态
		queryWrapper.eq(StrUtil.isNotBlank(queryCourseParamsDTO.getAuditStatus()),
				CourseBase::getAuditStatus,
				queryCourseParamsDTO.getAuditStatus());

		//匹配课程发布状态
		queryWrapper.eq(StrUtil.isNotBlank(queryCourseParamsDTO.getAuditStatus()),
				CourseBase::getStatus,
				queryCourseParamsDTO.getPublishStatus());

		return courseBaseMapper.selectPage(new Page<>(
			pageParams.getPageNo(),
			pageParams.getPageSize()),
			queryWrapper);
	}

	@Override
	@Transactional
	public CourseBaseInfoDTO createCourseBase(Long companyId, AddCourseDTO addCourseDTO) {
		//保存课程基本信息
		CourseBase courseBase = new CourseBase();
		BeanUtil.copyProperties(addCourseDTO, courseBase, true);

		courseBase
				.setAuditStatus("202002")//设置审核状态
				.setStatus("203001")//设置发布状态
				.setCompanyId(companyId)//设置机构id
				.setCreateDate(LocalDateTime.now());//设置添加时间

		int flagOfSaveCourseBase = courseBaseMapper.insert(courseBase);

		if (flagOfSaveCourseBase <= 0) {
			throw new BaseException(FAIL_CREATE_COURSE_INFO);
		}

		//向课程营销表保存课程营销信息
		CourseMarket courseMarket = new CourseMarket();
		BeanUtil.copyProperties(addCourseDTO, courseMarket, true);
		courseMarket.setId(courseBase.getId());

		int flagOfSaveCourseMarket = this.saveCourseMarket(courseMarket);

		if (flagOfSaveCourseMarket <= 0) {
			throw new BaseException(FAIL_CREATE_COURSE_MARKET);
		}

		//查询课程基本信息及营销信息并返回
		return this.getCourseBaseInfo(courseBase.getId());
	}

	@Override
	public CourseBaseInfoDTO getCourseBaseInfo(long courseId) {
		CourseBase courseBase = courseBaseMapper.selectById(courseId);

		if (courseBase == null) {
			return null;
		}

		CourseMarket courseMarket = courseMarketMapper.selectById(courseId);

		CourseBaseInfoDTO courseBaseInfoDTO = new CourseBaseInfoDTO();

		BeanUtil.copyProperties(courseBase, courseBaseInfoDTO, true);
		if (courseMarket != null) {
			BeanUtil.copyProperties(courseMarket, courseBaseInfoDTO, true);
		}

		//查询分类名称
		CourseCategory categoryBySt = courseCategoryMapper.selectById(courseBase.getSt());
		CourseCategory categoryByMt = courseCategoryMapper.selectById(courseBase.getMt());

		courseBaseInfoDTO
				.setStName(categoryBySt.getName())
				.setMtName(categoryByMt.getName());

		return courseBaseInfoDTO;
	}

	@Override
	@Transactional
	public CourseBaseInfoDTO updateCourseBase(Long companyId, EditCourseDTO editCourseDTO) {
		CourseBase courseBase = courseBaseMapper.selectById(editCourseDTO.getId());

		if (courseBase == null) {
			BaseException.cast(QUERY_NULL);
		}

		if (!companyId.equals(courseBase.getCompanyId())) {
			BaseException.cast(COMPANY_INFORMATION_MISMATCH);
		}

		//封装基本信息数据
		BeanUtil.copyProperties(editCourseDTO, courseBase, true);
		courseBase.setChangeDate(LocalDateTime.now());

		courseBaseMapper.updateById(courseBase);

		//封装营销数据基本信息
		CourseMarket courseMarket = new CourseMarket();
		BeanUtil.copyProperties(editCourseDTO, courseMarket, true);

		this.saveCourseMarket(courseMarket);

		//查询课程信息
		return this.getCourseBaseInfo(editCourseDTO.getId());
	}

	@Override
	@Transactional
	public void deleteItem(Long courseId, Long companyId) {
		CourseBase courseBase = courseBaseMapper.selectById(courseId);

		if (courseBase == null) {
			BaseException.cast(QUERY_NULL);
		}

		if (!companyId.equals(courseBase.getCompanyId())) {
			BaseException.cast(COMPANY_INFORMATION_MISMATCH);
		}

		if (!courseBase.getAuditStatus().equals("202002")) {
			BaseException.cast(AUDIT_STATUS_MISMATCH);
		}

		courseBaseMapper.deleteById(courseId);
		courseMarketMapper.deleteById(courseId);

		//删除课程计划
		List<TeachplanDTO> teachplanTree = teachplanService.findTeachplanTree(courseId);
		for (TeachplanDTO teachplanDTO : teachplanTree) {
			for (TeachplanDTO teachplanTreeNode : teachplanDTO.getTeachplanTreeNodes()) {
				teachplanService.deleteTeachplan(teachplanTreeNode.getId());
			}
			teachplanService.deleteTeachplan(teachplanDTO.getId());
		}

		//删除课程教师信息
		List<CourseTeacher> teacherList = courseTeacherService.getTeacherList(courseId);
		List<Long> teacherIds = teacherList.stream()
			.map(CourseTeacher::getId)
			.toList();
		courseTeacherService.removeByIds(teacherIds);
	}

	@Override
	public CourseBase getById(Long id) {
		return courseBaseMapper.selectById(id);
	}

	@Override
	public void updateById(CourseBase courseBase) {
		courseBaseMapper.updateById(courseBase);
	}

	/**
	 * <p>保存课程营销信息方法</p>
	 *
	 * @param courseMarket 要保存的课程营销记录
	 * @return boolean-是否保存成功标志
	 */
	private int saveCourseMarket(CourseMarket courseMarket) {
		//收费规则
		if (StrUtil.isBlank(courseMarket.getCharge())) {
			throw new BaseException(CHARGING_RULES_NOT_SELECTED);
		}

		if (courseMarket.getCharge().equals("201001")) {
			if (courseMarket.getPrice() == null || courseMarket.getPrice() <= 0) {
				throw new BaseException(ILLEGAL_CHARGES);
			}
		}

		CourseMarket selectCourseMarket = courseMarketMapper.selectById(courseMarket.getId());
		//判断数据库中是否已经存在这个营销数据：不存在=>新增；存在=>更新
		if (selectCourseMarket == null) {
			return courseMarketMapper.insert(courseMarket);
		} else {
			return courseMarketMapper.updateById(courseMarket);
		}
	}
}
