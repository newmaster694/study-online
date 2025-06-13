package study.online.content.service.impl;

import jakarta.annotation.Resource;
import study.online.base.exception.BaseException;
import study.online.content.mapper.CourseBaseMapper;
import study.online.content.model.po.CourseBase;
import study.online.content.service.ICourseCommonService;
import org.springframework.stereotype.Service;

@Service
public class CourseCommonServiceImpl implements ICourseCommonService {

	@Resource
	private CourseBaseMapper courseBaseMapper;

	@Override
	public void checkCoursePermission(Long courseId, Long companyId) {
		CourseBase courseBase = courseBaseMapper.selectById(courseId);
		if (courseBase == null) {
			throw new BaseException("课程不存在");
		}
		if (!companyId.equals(courseBase.getCompanyId())) {
			throw new BaseException("无权限，本机构只能操作本机构的课程");
		}
	}

	@Override
	public void checkCourseExistence(Long courseId) {
		if (courseBaseMapper.selectById(courseId) == null) {
			throw new BaseException("课程不存在");
		}
	}
}