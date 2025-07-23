package study.online.content.service;

import study.online.content.model.dto.CourseCategoryTreeDTO;

import java.util.List;

public interface ICourseCategoryService {
	/**
	 * <p>课程分类树形结构查询</p>
	 *
	 * @param id 父节点id
	 * @return {@code List<CourseCategoryTreeDTO>}
	 */
	List<CourseCategoryTreeDTO> queryTreeNodes(String id);
}
