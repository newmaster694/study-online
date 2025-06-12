package study.online.content.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import study.online.base.contant.CommonErrror;
import study.online.base.execption.BaseException;
import study.online.content.mapper.CourseTeacherMapper;
import study.online.content.model.dto.SaveCourseTeacherDTO;
import study.online.content.model.po.CourseBase;
import study.online.content.model.po.CourseTeacher;
import study.online.content.service.ICourseBaseInfoService;
import study.online.content.service.ICourseTeacherService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class CourseTeacherServiceImpl extends ServiceImpl<CourseTeacherMapper, CourseTeacher>
	implements ICourseTeacherService {

	@Resource
	private ICourseBaseInfoService courseBaseInfoService;

	@Override
	public List<CourseTeacher> getTeacherList(Long courseId) {
		return lambdaQuery().eq(CourseTeacher::getCourseId, courseId).list();
	}

	@Override
	public CourseTeacher addTeacherInfo(SaveCourseTeacherDTO courseTeacherDTO, Long companyId) {
		if (courseTeacherDTO == null) {
			BaseException.cast(CommonErrror.REQUEST_NULL);
		}

		if (companyId == null) {
			BaseException.cast("机构id不能为空");
		}

		CourseBase courseBase = courseBaseInfoService.getById(courseTeacherDTO.getCourseId());
		if (courseBase == null) {
			BaseException.cast("课程不存在");
		}

		if (!companyId.equals(courseBase.getCompanyId())) {
			BaseException.cast("无权限，本机构只能修改本机构的课程");
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
				BaseException.cast("新增教师资料失败，请联系管理员");
			}

			return courseTeacher;
		}

		BeanUtil.copyProperties(courseTeacherDTO, courseTeacher, true);

		boolean flag = this.updateById(courseTeacher);
		if (!flag) {
			BaseException.cast("更新失败，请联系管理员");
		}

		return courseTeacher;
	}

	@Override
	public void deleteItem(Long courseId, Long courseTeacherId, Long companyId) {
		if (courseId == null || courseTeacherId == null || companyId == null) {
			BaseException.cast(CommonErrror.REQUEST_NULL);
		}

		if (courseBaseInfoService.getById(courseId).getCompanyId().equals(companyId)) {
			BaseException.cast("无权限，本机构只能修改本机构的课程");
		}

		CourseTeacher courseTeacher = this.getById(courseTeacherId);
		if (courseTeacher == null) {
			BaseException.cast(CommonErrror.OBJECT_NULL);
		}

		this.removeById(courseTeacherId);
	}
}
