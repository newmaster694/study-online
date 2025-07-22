package study.online.content.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.online.content.model.dto.CourseBaseInfoDTO;
import study.online.content.model.dto.CoursePreviewDTO;
import study.online.content.model.dto.TeachplanDTO;
import study.online.content.service.ICourseBaseInfoService;
import study.online.content.service.ICoursePublishService;
import study.online.content.service.ITeachplanService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoursePreviewServiceImpl implements ICoursePublishService {

	private final ICourseBaseInfoService courseBaseInfoService;
	private final ITeachplanService teachplanService;

	@Override
	public CoursePreviewDTO getCoursePreviewInfo(Long courseId) {
		CoursePreviewDTO coursePreviewDTO = new CoursePreviewDTO();

		//查询课程基本信息/营销信息
		CourseBaseInfoDTO courseBaseInfo = courseBaseInfoService.getCourseBaseInfo(courseId);

		//查询课程计划信息
		List<TeachplanDTO> teachplanTree = teachplanService.findTeachplanTree(courseId);

		return coursePreviewDTO.setCourseBase(courseBaseInfo).setTeachplans(teachplanTree);
	}
}
