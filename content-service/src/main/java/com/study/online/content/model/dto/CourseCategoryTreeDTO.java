package com.study.online.content.model.dto;

import com.study.online.content.model.po.CourseCategory;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * <p>课程分类树形节点DTO</p>
 *
 * @since 2025/6/5
 * @author newmaster
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CourseCategoryTreeDTO extends CourseCategory implements Serializable {
	List<CourseCategoryTreeDTO> childrenTreeNodes;
}
