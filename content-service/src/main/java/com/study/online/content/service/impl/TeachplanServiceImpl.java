package com.study.online.content.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.online.content.mapper.TeachplanMapper;
import com.study.online.content.model.dto.TeachplanDTO;
import com.study.online.content.model.po.Teachplan;
import com.study.online.content.service.ITeachplanService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeachplanServiceImpl extends ServiceImpl<TeachplanMapper, Teachplan> implements ITeachplanService {

	@Resource
	private TeachplanMapper teachplanMapper;

	@Override
	public List<TeachplanDTO> findTeachplanTree(long courseId) {
		return teachplanMapper.selectTreeNodes(courseId);
	}
}
