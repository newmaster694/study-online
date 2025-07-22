package study.online.content.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
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
public class CourseTeacherServiceImpl extends ServiceImpl<CourseTeacherMapper, CourseTeacher>
	implements ICourseTeacherService {

	@Resource
	private CourseBaseMapper courseBaseMapper;

	@Override
	public List<CourseTeacher> getTeacherList(Long courseId) {
		return lambdaQuery().eq(CourseTeacher::getCourseId, courseId).list();
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

		CourseTeacher courseTeacher = lambdaQuery()
			.eq(!Objects.isNull(courseTeacherDTO.getId()), CourseTeacher::getId, courseTeacherDTO.getId())
			.one();

		if (courseTeacher == null) {
			//新增逻辑
			courseTeacher = new CourseTeacher();
			BeanUtil.copyProperties(courseTeacherDTO, courseTeacher, true);

			courseTeacher.setCreateDate(LocalDateTime.now());
			boolean flag = this.save(courseTeacher);
			if (!flag) {
				BaseException.cast(FAIL_ADD_TEACHER_INFO);
			}

			return courseTeacher;
		}

		//更新逻辑
		BeanUtil.copyProperties(courseTeacherDTO, courseTeacher, true);

		boolean flag = this.updateById(courseTeacher);
		if (!flag) {
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

		CourseTeacher courseTeacher = this.getById(courseTeacherId);
		if (courseTeacher == null) {
			BaseException.cast(OBJECT_NULL);
		}

		this.removeById(courseTeacherId);
	}
}
