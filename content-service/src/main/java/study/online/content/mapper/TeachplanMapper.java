package study.online.content.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import study.online.content.model.dto.TeachplanDTO;
import study.online.content.model.po.Teachplan;

import java.util.List;

/**
 * <p>课程计划 Mapper 接口</p>
 *
 * @author newmaster
 * @since 2025-06-04
 */
public interface TeachplanMapper extends BaseMapper<Teachplan> {

	/**
	 * <p>查询某课程的课程计划，组成树形结构</p>
	 *
	 * @param courseId 课程id
	 * @return TeachPlanDTO列表
	 */
	List<TeachplanDTO> selectTreeNodes(Long courseId);
}
