package study.online.media.utils;

import io.minio.Result;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static study.online.base.constant.MediaFilePathConstant.MEDIA_CHUNK_PATH_BUCKET;

@Slf4j
@RequiredArgsConstructor
@Component
public class FileUtil {
	private final MinioUtil minioUtil;

	/*清除分块文件*/
	public void clearChunkFiles(String chunkFileFolderPath) {

		try {
			List<String> sortedChunkFiles = this.getSortedChunkFiles(chunkFileFolderPath);
			minioUtil.removeFiles(MEDIA_CHUNK_PATH_BUCKET, sortedChunkFiles);
		} catch (Exception e) {
			log.error(e.getMessage());
			log.error("清除分块文件失败,chunkFileFolderPath:{}", chunkFileFolderPath, e);
		}
	}

	/*得到合并后的文件的地址*/
	public String getFilePathByMd5(String fileMd5, String fileExt) {
		return fileMd5.charAt(0) + "/" + fileMd5.charAt(1) + "/" + fileMd5 + "/" + fileMd5 + fileExt;
	}

	/*获取指定路径下的所有分块文件，并按文件名数字升序排序*/
	public List<String> getSortedChunkFiles(String prefix) throws Exception {
		List<String> files = new ArrayList<>();

		// 调用封装好的 listObjects 方法
		Iterable<Result<Item>> results = minioUtil.listObjects(MEDIA_CHUNK_PATH_BUCKET, prefix, false);

		for (Result<Item> result : results) {
			Item item = result.get();
			if (!item.isDir()) {
				String objectName = item.objectName();
				String fileName = objectName.substring(prefix.length());
				if (fileName.matches("\\d+")) { // 只保留纯数字命名的文件
					files.add(objectName);
				}
			}
		}

		// 按文件名数字升序排序
		files.sort((a, b) -> {
			int numA = Integer.parseInt(a.substring(prefix.length()));
			int numB = Integer.parseInt(b.substring(prefix.length()));
			return Integer.compare(numA, numB);
		});

		return files;
	}

	/*得到分块文件目录*/
	public String getChunkFileFolderPath(String fileMd5) {
		return fileMd5.charAt(0) + "/" + fileMd5.charAt(1) + "/" + fileMd5 + "/" + "chunk" + "/";
	}

	/*获取文件的md5*/
	public String getFileMd5(MultipartFile file) {
		try (InputStream inputStream = file.getInputStream()) {
			return DigestUtils.md5Hex(inputStream);
		} catch (Exception e) {
			log.error("获取文件md5值失败:{}", e.getMessage());
			return null;
		}
	}

	/*获取文件默认存储目录路径 年/月/日*/
	public String getDefaultFolderPath() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new Date()).replace("-", "/") + "/";
	}

	/*根据文件名，设置HttpServletResponse的ContentType*/
	public String getContentType(String fileName) {
		Optional<MediaType> mediaType = MediaTypeFactory.getMediaType(fileName);
		return String.valueOf(mediaType.orElse(MediaType.APPLICATION_OCTET_STREAM));
	}
}
