import cn.hutool.core.io.FileUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.multipart.MultipartFile;
import study.online.api.client.UploadFileClient;
import study.online.content.ContentApplication;
import study.online.content.model.dto.CoursePreviewDTO;
import study.online.content.service.ICoursePublishService;
import study.online.content.utils.MultipartFileSupportUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

@SpringBootTest(classes = ContentApplication.class)
class FreeMarkerTest {

	@Resource
	private ICoursePublishService coursePublishService;

	@Resource
	private UploadFileClient uploadFileClient;

	@Test
	void testGenerateHtmlTemplate() throws IOException, TemplateException {

		//获取模板
		Configuration configuration = new Configuration();

		String classPath = Objects.requireNonNull(this.getClass().getResource("/")).getPath();
		configuration.setDirectoryForTemplateLoading(new File(classPath + "/template/"));

		configuration.setDefaultEncoding("utf-8");

		Template template = configuration.getTemplate("course_template.ftl");

		//获取数据
		CoursePreviewDTO coursePreviewInfo = coursePublishService.getCoursePreviewInfo(120L);
		HashMap<String, Object> map = new HashMap<>();

		map.put("model", coursePreviewInfo);

		//生成模板-字符串
		String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);

		//使用流将字符串写入到文件里
		FileUtil.writeString(html, new File("C:\\Users\\newmaster\\Desktop\\upload"),
			"utf-8");

		MultipartFile multipartFile = MultipartFileSupportUtil
			.fileToMultipartFile(new File("C:\\Users\\newmaster\\Desktop\\upload"));

		uploadFileClient.uploadfile(
			multipartFile,
			"/course/120.html");
	}
}
