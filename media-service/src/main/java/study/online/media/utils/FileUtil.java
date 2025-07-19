package study.online.media.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class FileUtil {

	/*得到合并后的文件的地址*/
	public static String getFilePathByMd5(String fileMd5, String fileExt) {
		return fileMd5.charAt(0) + "/" + fileMd5.charAt(1) + "/" + fileMd5 + "/" + fileMd5 + fileExt;
	}

	/*得到分块文件目录*/
	public static String getChunkFileFolderPath(String fileMd5) {
		return fileMd5.charAt(0) + "/" + fileMd5.charAt(1) + "/" + fileMd5 + "/" + "chunk" + "/";
	}

	/*获取文件的md5*/
	public static String getFileMd5(MultipartFile file) {
		try (InputStream inputStream = file.getInputStream()) {
			return DigestUtils.md5Hex(inputStream);
		} catch (Exception e) {
			log.error("获取文件md5值失败:{}", e.getMessage());
			return null;
		}
	}

	/*获取文件默认存储目录路径 年/月/日*/
	public static String getDefaultFolderPath() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new Date()).replace("-", "/") + "/";
	}

	/*根据文件名，设置HttpServletResponse的ContentType*/
	public static String getContentType(String fileName) {
		Optional<MediaType> mediaType = MediaTypeFactory.getMediaType(fileName);
		return String.valueOf(mediaType.orElse(MediaType.APPLICATION_OCTET_STREAM));
	}
}
