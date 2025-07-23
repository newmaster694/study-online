package study.online.content.service;

import study.online.content.model.dto.BindTeachPlanMediaDTO;
import study.online.content.model.dto.SaveTeachplanDTO;
import study.online.content.model.dto.TeachplanDTO;
import study.online.content.model.po.TeachplanMedia;

import java.util.List;

/**
 * <p>课程计划信息接口</p>
 *
 * @author newmaster
 * @since 2025/6/9
 */
public interface ITeachplanService {

	/**
	 * <p>查询课程计划树形结构</p>
	 *
	 * @param courseId 课程id
	 * @return {@code List<TeachplanDTO>}
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

	/**
	 * 教学计划绑定媒资
	 *
	 * @param bindTeachPlanMediaDTO 绑定的媒资记录
	 * @return {@code TeachplanMedia}
	 */
	TeachplanMedia associationMedia(BindTeachPlanMediaDTO bindTeachPlanMediaDTO);

	/**
	 * 教学计划取消绑定媒资
	 *
	 * @param teachplanId 教学计划id
	 * @param mediaId     媒体资料id
	 */
	void unbindMedia(Long teachplanId, String mediaId);
}
