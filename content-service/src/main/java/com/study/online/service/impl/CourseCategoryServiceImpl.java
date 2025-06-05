package com.study.online.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.online.mapper.CourseCategoryMapper;
import com.study.online.model.dto.CourseCategoryTreeDTO;
import com.study.online.model.po.CourseCategory;
import com.study.online.service.ICourseCategoryService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CourseCategoryServiceImpl extends ServiceImpl<CourseCategoryMapper, CourseCategory>
	implements ICourseCategoryService {

	@Resource
	private CourseCategoryMapper courseCategoryMapper;

	@Override
	public List<CourseCategoryTreeDTO> queryTreeNodes(String id) {
		List<CourseCategoryTreeDTO> courseCategoryTreeDTOs = courseCategoryMapper.selectTreeNodes(id);

		//将List转Map，以备使用，排除根节点
		Map<String, CourseCategoryTreeDTO> mapTemp = courseCategoryTreeDTOs.stream()
			.filter(item -> !id.equals(item.getId()))
			.collect(Collectors.toMap(
				CourseCategory::getId, value -> value,
				(key1, key2) -> key2//这里假设有两个重复的key使用第二个
			));

		//最终返回的List
		List<CourseCategoryTreeDTO> results = new ArrayList<>();

		//依次遍历每个元素，排除根节点
		courseCategoryTreeDTOs.stream()
			.filter(item -> !id.equals(item.getId()))
			.forEach(item -> {
				//找到二级节点，放入返回的列表中
				if (item.getParentid().equals(id)) {
					results.add(item);
				}

				//找到节点的父节点
				CourseCategoryTreeDTO courseCategoryParent = mapTemp.get(item.getParentid());
				if (courseCategoryParent != null) {
					if (courseCategoryParent.getChildrenTreeNodes() == null) {
						courseCategoryParent.setChildrenTreeNodes(new ArrayList<>());
					}

					//找到子节点，放到childrenTreeNodes中
					courseCategoryParent.getChildrenTreeNodes().add(item);
				}
			});

		return results;
	}
}
