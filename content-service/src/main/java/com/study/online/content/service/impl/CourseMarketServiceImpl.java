package com.study.online.content.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.online.content.mapper.CourseMarketMapper;
import com.study.online.content.model.po.CourseMarket;
import com.study.online.content.service.ICourseMarketService;
import org.springframework.stereotype.Service;

@Service
public class CourseMarketServiceImpl extends ServiceImpl<CourseMarketMapper, CourseMarket>
	implements ICourseMarketService {
}
