package study.online.search.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.online.base.exception.BaseException;
import study.online.search.po.CourseIndex;
import study.online.search.service.IIndexService;

/**
 * 课程索引接口
 *
 * @author Mr.M
 * @version 1.0
 * @since 2022/9/24 22:31
 */
@RestController
@RequestMapping("/index")
@RequiredArgsConstructor
public class CourseIndexController {

	private final IIndexService indexService;

	@Value("${elasticsearch.course.index}")
	private String courseIndexStore;

	@PostMapping("course")
	public Boolean add(@RequestBody CourseIndex courseIndex) {

		Long id = courseIndex.getId();
		if (id == null) {
			BaseException.cast("课程id为空");
		}
		Boolean result = indexService.addCourseIndex(courseIndexStore, String.valueOf(id), courseIndex);
		if (!result) {
			BaseException.cast("添加课程索引失败");
		}
		return result;
	}
}
