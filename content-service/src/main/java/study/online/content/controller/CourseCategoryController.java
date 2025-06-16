package study.online.content.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.online.content.model.dto.CourseCategoryTreeDTO;
import study.online.content.service.ICourseCategoryService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/course-category")
public class CourseCategoryController {

	@Resource
	private ICourseCategoryService courseCategoryService;

	/**
	 * 查询课程分类树节点接口
	 *
	 * @return {@code List<CourseCategoryTreeDTO>}
	 */
	@GetMapping("/tree-nodes")
	public List<CourseCategoryTreeDTO> queryTreeNodes() {
		return courseCategoryService.queryTreeNodes("1");
	}
}
