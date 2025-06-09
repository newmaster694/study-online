package com.study.online.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.study.online.content.model.dto.TeachplanDTO;
import com.study.online.content.model.po.Teachplan;

import java.util.List;

/**
 * <p>课程计划信息接口</p>
 *
 * @since 2025/6/9
 * @author newmaster
 */
public interface ITeachplanService extends IService<Teachplan> {

	/**
	 * <p>查询课程计划树形结构</p>
	 *
	 * @param courseId 课程id
	 * @return TeachplanDTO-列表
	 */
	List<TeachplanDTO> findTeachplanTree(long courseId);
}
