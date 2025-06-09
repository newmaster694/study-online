package study.online.content.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import study.online.content.model.dto.CourseCategoryTreeDTO;
import study.online.content.model.po.CourseCategory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>课程分类 Mapper 接口</p>
 *
 * @author newmaster
 * @since 2025-06-04
 */
@Mapper
public interface CourseCategoryMapper extends BaseMapper<CourseCategory> {

	/**
	 * <p>查找课程分类接口</p>
	 *
	 * @return List(CourseCategoryTreeDTO)
	 */
	List<CourseCategoryTreeDTO> selectTreeNodes(String id);
}
