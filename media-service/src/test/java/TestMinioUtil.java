import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import study.online.media.MediaApplication;
import study.online.media.utils.MinioUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@SpringBootTest(classes = MediaApplication.class)
public class TestMinioUtil {

	@Resource
	private MinioUtil minioUtil;

	@Test
	public void testUpload() throws FileNotFoundException {
		FileInputStream fis = new FileInputStream("C:\\Users\\newmaster\\Desktop\\新建 Markdown File.md");
		minioUtil.uploadFile("test-bucket", "aaa.md", fis);
	}
}
