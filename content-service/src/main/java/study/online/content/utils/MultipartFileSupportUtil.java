package study.online.content.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Slf4j
public class MultipartFileSupportUtil {
	public static MultipartFile fileToMultipartFile(File file) {
		try(FileInputStream fis = new FileInputStream(file)) {
			return new MockMultipartFile(file.getName(), fis);
		} catch (IOException exception) {
			log.error("将File转换为MultipartFile出错-{}", file.getName());
			return null;
		}
	}
}
