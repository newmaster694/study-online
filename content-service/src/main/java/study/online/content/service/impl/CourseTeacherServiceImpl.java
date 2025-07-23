package study.online.content.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.online.base.exception.BaseException;
import study.online.content.mapper.CourseBaseMapper;
import study.online.content.mapper.CourseTeacherMapper;
import study.online.content.model.dto.SaveCourseTeacherDTO;
import study.online.content.model.po.CourseTeacher;
import study.online.content.service.ICourseTeacherService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static study.online.base.constant.ErrorMessageConstant.*;

@Service
@RequiredArgsConstructor
public class CourseTeacherServiceImpl implements ICourseTeacherService {

	private final CourseBaseMapper courseBaseMapper;
	private final CourseTeacherMapper courseTeacherMapper;

	@Override
	public List<CourseTeacher> getTeacherList(Long courseId) {
		LambdaQueryWrapper<CourseTeacher> queryWrapper = new LambdaQueryWrapper<>();

		queryWrapper.eq(CourseTeacher::getCourseId, courseId);
		return courseTeacherMapper.selectList(queryWrapper);
	}

	@Override
	public CourseTeacher addTeacherInfo(SaveCourseTeacherDTO courseTeacherDTO, Long companyId) {
		if (courseTeacherDTO == null) {
			BaseException.cast(REQUEST_NULL);
		}

		if (courseBaseMapper.selectById(courseTeacherDTO.getCourseId()) == null) {
			BaseException.cast(QUERY_NULL);
		}

		if (!companyId.equals(courseBaseMapper.selectById(courseTeacherDTO.getCourseId()).getCompanyId())) {
			BaseException.cast(COMPANY_INFORMATION_MISMATCH);
		}

		LambdaQueryWrapper<CourseTeacher> queryWrapper = new LambdaQueryWrapper<>();

		queryWrapper.eq(
			!Objects.isNull(courseTeacherDTO.getId()),
			CourseTeacher::getId,
			courseTeacherDTO.getId());

		CourseTeacher courseTeacher = courseTeacherMapper.selectOne(queryWrapper);

		if (courseTeacher == null) {
			//新增逻辑
			courseTeacher = new CourseTeacher();
			BeanUtil.copyProperties(courseTeacherDTO, courseTeacher, true);

			courseTeacher.setCreateDate(LocalDateTime.now());
			int flag = courseTeacherMapper.insert(courseTeacher);
			if (flag <= 0) {
				BaseException.cast(FAIL_ADD_TEACHER_INFO);
			}

			return courseTeacher;
		}

		//更新逻辑
		BeanUtil.copyProperties(courseTeacherDTO, courseTeacher, true);

		int flag = courseTeacherMapper.updateById(courseTeacher);
		if (flag <= 0) {
			BaseException.cast(FAIL_UPDATE_TEACHER_INFO);
		}

		return courseTeacher;
	}

	@Override
	public void deleteItem(Long courseId, Long courseTeacherId, Long companyId) {
		if (courseBaseMapper.selectById(courseId) == null) {
			BaseException.cast(QUERY_NULL);
		}

		if (!companyId.equals(courseBaseMapper.selectById(courseId).getCompanyId())) {
			BaseException.cast(COMPANY_INFORMATION_MISMATCH);
		}

		CourseTeacher courseTeacher = courseTeacherMapper.selectById(courseTeacherId);
		if (courseTeacher == null) {
			BaseException.cast(OBJECT_NULL);
		}

		courseTeacherMapper.deleteById(courseTeacherId);
	}

	@Override
	public void removeByIds(List<Long> teacherIds) {
		courseTeacherMapper.deleteBatchIds(teacherIds);
	}
}
