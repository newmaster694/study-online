package study.online.content.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.multipart.MultipartFile;
import study.online.api.client.FileClient;
import study.online.api.model.dto.UploadFileResultDTO;
import study.online.base.exception.BaseException;
import study.online.content.mapper.CourseMarketMapper;
import study.online.content.mapper.CoursePublishMapper;
import study.online.content.mapper.CoursePublishPreMapper;
import study.online.content.model.dto.CourseBaseInfoDTO;
import study.online.content.model.dto.CoursePreviewDTO;
import study.online.content.model.dto.TeachplanDTO;
import study.online.content.model.po.CourseBase;
import study.online.content.model.po.CourseMarket;
import study.online.content.model.po.CoursePublish;
import study.online.content.model.po.CoursePublishPre;
import study.online.content.service.ICourseBaseInfoService;
import study.online.content.service.ICoursePublishService;
import study.online.content.service.ITeachplanService;
import study.online.content.utils.MultipartFileSupportUtil;
import study.online.messagesdk.model.po.MqMessage;
import study.online.messagesdk.service.MqMessageService;

import java.io.File;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import static study.online.base.constant.ErrorMessage.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoursePreviewServiceImpl implements ICoursePublishService {

	private final ICourseBaseInfoService courseBaseInfoService;
	private final ITeachplanService teachplanService;
	private final CourseMarketMapper courseMarketMapper;
	private final CoursePublishPreMapper coursePublishPreMapper;
	private final CoursePublishMapper coursePublishMapper;
	private final MqMessageService mqMessageService;
	private final FileClient fileClient;

	@Override
	public CoursePreviewDTO getCoursePreviewInfo(Long courseId) {
		CoursePreviewDTO coursePreviewDTO = new CoursePreviewDTO();

		//查询课程基本信息/营销信息
		CourseBaseInfoDTO courseBaseInfo = courseBaseInfoService.getCourseBaseInfo(courseId);

		//查询课程计划信息
		List<TeachplanDTO> teachplanTree = teachplanService.findTeachplanTree(courseId);

		return coursePreviewDTO.setCourseBase(courseBaseInfo).setTeachplans(teachplanTree);
	}

	@Override
	@Transactional
	public void commitAudit(Long companyId, Long courseId) {
		CourseBaseInfoDTO courseBaseInfo = courseBaseInfoService.getCourseBaseInfo(courseId);
		if (courseBaseInfo == null) {
			BaseException.cast(QUERY_NULL);
		}

		//如果课程审核状态为“已提交”则不允许提交
		if (courseBaseInfo.getAuditStatus().equals("202003")) {
			BaseException.cast(AUDIT_STATUS_ERROR);
		}

		//课程的图片、计划信息等没有填写也不允许提交
		if (StrUtil.isBlank(courseBaseInfo.getPic())) {
			BaseException.cast(COURSE_PIC_NULL_ERROR);
		}

		List<TeachplanDTO> teachplanTree = teachplanService.findTeachplanTree(courseId);
		if (teachplanTree == null || teachplanTree.isEmpty()) {
			BaseException.cast(TEACH_PLAN_NULL_ERROR);
		}

		//TODO 机构权限验证

		//查询课程基本信息、营销信息、计划信息等插入到课程预发布表
		CoursePublishPre coursePublishPre = new CoursePublishPre();
		BeanUtil.copyProperties(courseBaseInfo, coursePublishPre, true);

		CourseMarket courseMarket = courseMarketMapper.selectById(courseId);

		String courseMarketJsonStr = JSONUtil.toJsonStr(courseMarket);
		String teachplanJsonStr = JSONUtil.toJsonStr(teachplanTree);

		coursePublishPre
			.setMarket(courseMarketJsonStr)
			.setTeachplan(teachplanJsonStr)
			.setStatus("202003")
			.setCreateDate(LocalDateTime.now());

		//查询预发布表，如果有记录则更新，没有则插入
		if (coursePublishPreMapper.selectById(courseId) != null) {
			coursePublishPreMapper.updateById(coursePublishPre);
		} else {
			coursePublishPreMapper.insert(coursePublishPre);
		}

		//更新课程基本信息表的审核状态为“已提交”
		CourseBase courseBase = courseBaseInfoService.getById(courseId);
		courseBaseInfoService.updateById(courseBase.setAuditStatus("202003"));
	}

	@Override
	@Transactional
	public void publish(Long companyId, Long courseId) {
		//查询预发布表
		CoursePublishPre coursePublishPre = coursePublishPreMapper.selectById(courseId);
		if (coursePublishPre == null) {
			BaseException.cast(QUERY_NULL);
		}

		//审核不通过则不允许发布
		if (!coursePublishPre.getStatus().equals("202004")) {
			BaseException.cast(AUDIT_STATUS_MISMATCH);
		}

		//TODO 校验机构身份的合法性

		//向课程发布表写入数据
		CoursePublish coursePublish = new CoursePublish();
		BeanUtil.copyProperties(coursePublishPre, coursePublish, true);
		if (coursePublishMapper.selectById(courseId) == null) {
			coursePublishMapper.insert(coursePublish);
		} else {
			coursePublishMapper.updateById(coursePublish);
		}

		//向消息表写入数据
		MqMessage mqMessage = mqMessageService.addMessage(
			"course_publish",
			String.valueOf(courseId),
			null, null);
		if (mqMessage == null) {
			BaseException.cast(UNKNOW_ERROR);
		}

		//将预发布表的数据删除（其实也可以不删除的貌似）
		coursePublishPreMapper.deleteById(coursePublishPre);
	}

	@Override
	public File generateCourseHtml(Long courseId) {
		File htmlFile = null;

		try {
			//配置FreeMarker
			Configuration configuration = new Configuration(Configuration.getVersion());

			String classPath = this.getClass().getResource("/").getPath();
			configuration.setDirectoryForTemplateLoading(new File(classPath + "/template/"));

			configuration.setDefaultEncoding("UTF-8");

			Template template = configuration.getTemplate("course_template.ftl");

			CoursePreviewDTO coursePreviewInfo = this.getCoursePreviewInfo(courseId);

			HashMap<String, Object> map = new HashMap<>();
			map.put("model", coursePreviewInfo);

			String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);

			//将静态化页面输出到文件中
			htmlFile = File.createTempFile("course", "html");

			FileUtil.writeString(content, htmlFile, "UTF-8");
		} catch (Exception exception) {
			log.error("课程静态化异常-{}", exception.getMessage());
			BaseException.cast(COURSE_STATIC_ERROR);
		}

		return htmlFile;
	}

	@Override
	public void uploadCourseHtml(Long courseId, File file) {
		MultipartFile multipartFile = MultipartFileSupportUtil.fileToMultipartFile(file);

		if (multipartFile == null) {
			throw new BaseException("将File转换为MultipartFile出错");
		}

		UploadFileResultDTO uploadfile = fileClient
			.uploadfile(multipartFile, "course/" + courseId + ".html");

		if (uploadfile == null) {
			BaseException.cast(SAVE_FILE_ERROR);
		}
	}

	@Override
	public CoursePublish getCoursePublish(Long courseId) {
		return coursePublishMapper.selectById(courseId);
	}
}
