package study.online.content.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import study.online.content.mapper.CourseMarketMapper;
import study.online.content.model.po.CourseMarket;
import study.online.content.service.ICourseMarketService;
import org.springframework.stereotype.Service;

@Service
public class CourseMarketServiceImpl extends ServiceImpl<CourseMarketMapper, CourseMarket>
	implements ICourseMarketService {
}
