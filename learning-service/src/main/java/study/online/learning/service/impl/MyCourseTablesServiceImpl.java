package study.online.learning.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.online.api.client.ContentClient;
import study.online.api.model.po.CoursePublish;
import study.online.base.constant.ErrorMessage;
import study.online.base.exception.BaseException;
import study.online.learning.mapper.XcChooseCourseMapper;
import study.online.learning.mapper.XcCourseTablesMapper;
import study.online.learning.model.dto.XcChooseCourseDTO;
import study.online.learning.model.dto.XcCourseTablesDTO;
import study.online.learning.model.po.XcChooseCourse;
import study.online.learning.model.po.XcCourseTables;
import study.online.learning.service.IMyCourseTableService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyCourseTablesServiceImpl implements IMyCourseTableService {

	private final ContentClient contentClient;
	private final XcChooseCourseMapper chooseCourseMapper;
	private final XcCourseTablesMapper courseTablesMapper;

	@Override
	public XcChooseCourseDTO addChooseCourse(Long userId, Long courseId) {
		CoursePublish coursePublish = contentClient.getCoursePublish(courseId);
		String charge = coursePublish.getCharge();

		XcChooseCourse chooseCourse;//选课记录
		if ("20100".equals(charge)) {
			//课程免费
			chooseCourse = this.addFreeCoruse(userId, coursePublish);//在选课记录中添加该免费课程
			XcCourseTables courseTables = this.addCourseTabls(chooseCourse);//在课程表中添加该选课记录
		} else {
			chooseCourse = this.addChargeCoruse(userId, coursePublish);
		}

		// 获取学习资格
		XcChooseCourseDTO chooseCourseDTO = BeanUtil.copyProperties(chooseCourse, XcChooseCourseDTO.class);
		XcCourseTablesDTO courseTablesDTO = this.getLearningStatus(userId, courseId);

		chooseCourseDTO.setLearnStatus(courseTablesDTO.getLearnStatus());
		return null;
	}

	@Override
	public XcCourseTablesDTO getLearningStatus(Long userId, Long courseId) {
		XcCourseTables courseTables = this.getCourseTables(userId, courseId);

		if (courseTables == null) {
			return XcCourseTablesDTO.builder()
				.learnStatus("702002").build();
		}

		XcCourseTablesDTO courseTablesDTO = BeanUtil.copyProperties(courseTables, XcCourseTablesDTO.class);
		boolean isExpires = courseTables.getValidtimeEnd().isBefore(LocalDateTime.now());
		if (!isExpires) {//正常学习
			return courseTablesDTO.setLearnStatus("702001");
		} else {//已到期，续费逻辑
			return courseTablesDTO.setLearnStatus("702003");
		}
	}

	//添加免费课程,免费课程加入选课记录表、我的课程表
	private XcChooseCourse addFreeCoruse(Long userId, CoursePublish coursepublish) {
		//查询选课记录表是否存在免费的且选课成功的订单
		LambdaQueryWrapper<XcChooseCourse> queryWrapper = new LambdaQueryWrapper<>();

		queryWrapper = queryWrapper.eq(XcChooseCourse::getUserId, userId)
			.eq(XcChooseCourse::getCourseId, coursepublish.getId())
			.eq(XcChooseCourse::getOrderType, "700001")//免费课程
			.eq(XcChooseCourse::getStatus, "701001");//选课成功
		List<XcChooseCourse> xcChooseCourses = chooseCourseMapper.selectList(queryWrapper);

		if (xcChooseCourses != null && !xcChooseCourses.isEmpty()) {
			return xcChooseCourses.get(0);
		}

		//添加选课记录信息
		XcChooseCourse xcChooseCourse = XcChooseCourse.builder()
			.courseId(coursepublish.getId())
			.courseName(coursepublish.getName())
			.coursePrice(0f)//免费课程价格为0
			.userId(userId)
			.companyId(coursepublish.getCompanyId())
			.orderType("700001")//免费课程
			.createDate(LocalDateTime.now())
			.status("701001")//选课成功
			.validDays(365)
			.validtimeStart(LocalDateTime.now())
			.validtimeEnd(LocalDateTime.now().plusDays(365))
			.build();

		chooseCourseMapper.insert(xcChooseCourse);

		return xcChooseCourse;
	}

	//添加收费课程
	private XcChooseCourse addChargeCoruse(Long userId, CoursePublish coursepublish) {
		//如果存在支付记录直接返回
		LambdaQueryWrapper<XcChooseCourse> queryWrapper = new LambdaQueryWrapper<XcChooseCourse>()
			.eq(XcChooseCourse::getUserId, userId)
			.eq(XcChooseCourse::getCourseId, coursepublish.getId())
			.eq(XcChooseCourse::getOrderType, "700002")//收费课程
			.eq(XcChooseCourse::getStatus, "701002")//待支付
		;

		List<XcChooseCourse> chooseCourseList = chooseCourseMapper.selectList(queryWrapper);
		if (chooseCourseList != null && !chooseCourseList.isEmpty()) {
			return chooseCourseList.get(0); //存在，返回列表的第一条记录
		}

		XcChooseCourse chooseCourse = XcChooseCourse.builder()
			.courseId(coursepublish.getId())
			.courseName(coursepublish.getName())
			.coursePrice(coursepublish.getPrice())
			.userId(userId)
			.companyId(coursepublish.getCompanyId())
			.orderType("700002")
			.createDate(LocalDateTime.now())
			.status("701002")
			.validDays(coursepublish.getValidDays())
			.validtimeStart(LocalDateTime.now())
			.validtimeEnd(LocalDateTime.now().plusDays(coursepublish.getValidDays()))
			.build();

		chooseCourseMapper.insert(chooseCourse);
		return chooseCourse;
	}

	//添加到我的课程表
	private XcCourseTables addCourseTabls(XcChooseCourse chooseCourse) {
		String status = chooseCourse.getStatus();
		if (!status.equals("701001")) {
			BaseException.cast(ErrorMessage.ERROR_COURSE_STATUS);
		}

		//这里的作用是看我的课表里面是否存在这门选课记录，存在就直接返回，不存在添加记录
		XcCourseTables courseTables = this.getCourseTables(chooseCourse.getUserId(), chooseCourse.getCourseId());
		if (courseTables != null) {
			return courseTables;
		}

		courseTables = XcCourseTables.builder()
			.chooseCourseId(chooseCourse.getId())
			.userId(chooseCourse.getUserId())
			.courseId(chooseCourse.getCourseId())
			.companyId(chooseCourse.getCompanyId())
			.courseName(chooseCourse.getCourseName())
			.createDate(LocalDateTime.now())
			.validtimeStart(chooseCourse.getValidtimeStart())
			.validtimeEnd(chooseCourse.getValidtimeEnd())
			.courseType(chooseCourse.getOrderType())
			.build();

		courseTablesMapper.insert(courseTables);

		return courseTables;
	}

	//根据课程和用户查询我的课程表中某一门课程
	private XcCourseTables getCourseTables(Long userId, Long courseId) {
		return courseTablesMapper.selectOne(
			new LambdaQueryWrapper<XcCourseTables>()
				.eq(XcCourseTables::getUserId, userId)
				.eq(XcCourseTables::getCourseId, courseId)
		);
	}
}
