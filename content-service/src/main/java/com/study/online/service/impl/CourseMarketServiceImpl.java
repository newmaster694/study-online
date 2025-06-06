package com.study.online.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.online.mapper.CourseMarketMapper;
import com.study.online.model.po.CourseMarket;
import com.study.online.service.ICourseMarketService;
import org.springframework.stereotype.Service;

@Service
public class CourseMarketServiceImpl extends ServiceImpl<CourseMarketMapper, CourseMarket>
	implements ICourseMarketService {
}
