package study.online.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import study.online.content.model.dto.CourseCategoryTreeDTO;
import study.online.content.model.po.CourseCategory;

import java.util.List;

public interface ICourseCategoryService extends IService<CourseCategory> {
	/**
	 * <p>课程分类树形结构查询</p>
	 *
	 * @param id 父节点id
	 * @return {@code List<CourseCategoryTreeDTO>}
	 */
	List<CourseCategoryTreeDTO> queryTreeNodes(String id);
}
