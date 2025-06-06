import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.study.online.content.ContentApplication;
import com.study.online.content.mapper.CourseBaseMapper;
import com.study.online.base.content.model.PageParams;
import com.study.online.content.model.dto.QueryCourseParamsDTO;
import com.study.online.content.model.po.CourseBase;
import com.study.online.content.service.ICourseBaseInfoService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@SpringBootTest(classes = ContentApplication.class)
public class ContentApplicationTest {

	@Resource
	private CourseBaseMapper courseBaseMapper;

	@Resource
	private ICourseBaseInfoService courseBaseInfoService;

	@Test
	void testCourseBaseMapper() {
		CourseBase courseBase = courseBaseMapper.selectById(74L);
		Assertions.assertNotNull(courseBase);

		LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<>();

		//根据课程名称模糊查询 name like "%java%"
		queryWrapper.like(CourseBase::getName, "java");

		//根据课程审核状态查询
		queryWrapper.eq(CourseBase::getAuditStatus, "202004");

		//分页
		Page<CourseBase> page = courseBaseMapper.selectPage(new Page<>(1L, 3L), queryWrapper);

		System.out.println(page.toString());
	}

	@Test
	void testCourseBaseInfoService() {
		QueryCourseParamsDTO queryCourseParamsDTO = new QueryCourseParamsDTO();
		queryCourseParamsDTO.setCourseName("java")
				.setAuditStatus("202004")
				.setPublishStatus("203001");

		PageParams pageParams = new PageParams();
		pageParams.setPageNo(1L)
				.setPageSize(3L);

		Page<CourseBase> page = courseBaseInfoService.queryCourseBaseList(pageParams, queryCourseParamsDTO);

		List<CourseBase> records = page.getRecords();
		for (CourseBase item : records) {
			System.out.println(item.toString());
		}
	}

	@Test
	void generateSecretKey() {
		//生成32字节的随机密钥
		SecureRandom secureRandom = new SecureRandom();
		byte[] secretKeyBytes = new byte[32];
		secureRandom.nextBytes(secretKeyBytes);

		//将字节数组转换为Base64字符串
		String encodeKey = Base64.getEncoder().encodeToString(secretKeyBytes);
		System.out.println(encodeKey);
	}
}
