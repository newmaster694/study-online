package com.study.online.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.study.online.model.dto.CourseCategoryTreeDTO;
import com.study.online.model.po.CourseCategory;

import java.util.List;

public interface ICourseCategoryService extends IService<CourseCategory> {
	/**
	 * <p>课程分类树形结构查询</p>
	 *
	 * @param id 父节点id
	 * @return List(CourseCategoryTreeDTO)
	 */
	List<CourseCategoryTreeDTO> queryTreeNodes(String id);
}
