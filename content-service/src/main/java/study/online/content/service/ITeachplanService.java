package study.online.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import study.online.content.model.dto.SaveTeachplanDTO;
import study.online.content.model.dto.TeachplanDTO;
import study.online.content.model.po.Teachplan;

import java.util.List;

/**
 * <p>课程计划信息接口</p>
 *
 * @author newmaster
 * @since 2025/6/9
 */
public interface ITeachplanService extends IService<Teachplan> {

	/**
	 * <p>查询课程计划树形结构</p>
	 *
	 * @param courseId 课程id
	 * @return TeachplanDTO-列表
	 */
	List<TeachplanDTO> findTeachplanTree(long courseId);

	/**
	 * 新增/修改课程计划
	 *
	 * @param saveTeachplanDTO 课程计划记录
	 */
	void saveTeachplan(SaveTeachplanDTO saveTeachplanDTO);

	/**
	 * 删除课程计划
	 *
	 * @param teachplanId 课程计划id
	 */
	void deleteTeachplan(Long teachplanId);

	/**
	 * 章节的上移与下移操作接口
	 *
	 * @param moveType    动作，上移还是下移
	 * @param teachplanId 课程计划id
	 */
	void move(String moveType, Long teachplanId);
}
