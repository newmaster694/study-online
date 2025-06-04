package com.study.online.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.study.online.model.po.CourseCategory;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>课程分类 Mapper 接口</p>
 *
 * @author newmaster
 * @since 2025-06-04
 */
@Mapper
public interface CourseCategoryMapper extends BaseMapper<CourseCategory> {
}
