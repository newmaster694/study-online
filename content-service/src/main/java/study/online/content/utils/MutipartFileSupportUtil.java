package study.online.content.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;


@Slf4j
public class MutipartFileSupportUtil {

	public static MultipartFile fileToMultipartFile(File file) {
		FileItem item = new DiskFileItemFactory().createItem(
			"file",
			MediaType.MULTIPART_FORM_DATA_VALUE,
			true,
			file.getName());

		try (FileInputStream inputStream = new FileInputStream(file);
		     OutputStream outputStream = item.getOutputStream()) {
			IOUtils.copy(inputStream, outputStream);

		} catch (Exception e) {
			log.error("文件转换失败-{}", e.getMessage());
		}
		return new CommonsMultipartFile(item);
	}

}
