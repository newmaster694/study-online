package study.online.media.utils;

import cn.hutool.core.codec.Base64;
import com.alibaba.cloud.commons.lang.StringUtils;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.DeleteObject;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class MinioUtil {

	private final MinioClient minioClient;


	/**
	 * 判断文件是否存在
	 *
	 * @param bucketName 存储桶名称
	 * @param objectName 文件名称
	 * @return 布尔值
	 */
	public boolean isObjectExist(String bucketName, String objectName) {
		boolean exist = true;
		try {
			minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(objectName).build());
		} catch (Exception e) {
			log.error("[Minio工具类]>>>> 判断文件是否存在, 异常：", e);
			exist = false;
		}
		return exist;
	}

	/**
	 * 获取文件流
	 *
	 * @param bucketName 存储桶
	 * @param objectName 文件名
	 * @return 二进制流
	 */
	@SneakyThrows(Exception.class)
	public InputStream getObject(String bucketName, String objectName) {
		return minioClient.getObject(
			GetObjectArgs.builder()
				.bucket(bucketName)
				.object(objectName)
				.build());
	}

	/**
	 * 断点下载
	 *
	 * @param bucketName 存储桶
	 * @param objectName 文件名称
	 * @param offset     起始字节的位置
	 * @param length     要读取的长度
	 * @return 二进制流
	 */
	@SneakyThrows(Exception.class)
	public InputStream getObject(String bucketName, String objectName, long offset, long length) {
		return minioClient.getObject(
			GetObjectArgs.builder()
				.bucket(bucketName)
				.object(objectName)
				.offset(offset)
				.length(length)
				.build());
	}

	/**
	 * 使用MultipartFile进行文件上传
	 *
	 * @param bucketName  存储桶
	 * @param file        文件名
	 * @param objectName  对象名
	 * @param contentType 类型
	 * @return ObjectWriteResponse
	 */
	@SneakyThrows(Exception.class)
	public ObjectWriteResponse uploadFile(String bucketName, MultipartFile file, String objectName, String contentType) {
		InputStream inputStream = file.getInputStream();
		return minioClient.putObject(
			PutObjectArgs.builder()
				.bucket(bucketName)
				.object(objectName)
				.contentType(contentType)
				.stream(inputStream, inputStream.available(), -1)
				.build());
	}

	/**
	 * 图片上传
	 *
	 * @param bucketName 存储桶名称
	 * @param imageBase64 Base64编码的文件
	 * @param imageName 文件名
	 * @return ObjectWriteResponse
	 */
	public ObjectWriteResponse uploadImage(String bucketName, String imageBase64, String imageName) {
		Calendar calendar = Calendar.getInstance();
		if (!StringUtils.isEmpty(imageBase64)) {
			InputStream in = base64ToInputStream(imageBase64);
			String newName = System.currentTimeMillis() + "_" + imageName + ".jpg";
			String year = String.valueOf(calendar.get(Calendar.YEAR));
			String month = String.valueOf(calendar.get(Calendar.MONTH));
			return uploadFile(bucketName, year + "/" + month + "/" + newName, in);

		}
		return null;
	}

	public static InputStream base64ToInputStream(String base64) {
		ByteArrayInputStream stream = null;
		try {
			byte[] bytes = Base64.decode(base64.trim());
			stream = new ByteArrayInputStream(bytes);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return stream;
	}


	/**
	 * 上传本地文件
	 *
	 * @param bucketName 存储桶
	 * @param objectName 对象名称
	 * @param fileName   本地文件路径
	 * @return ObjectWriteResponse
	 */
	@SneakyThrows(Exception.class)
	public ObjectWriteResponse uploadFile(String bucketName, String objectName, String fileName) {
		return minioClient.uploadObject(
			UploadObjectArgs.builder()
				.bucket(bucketName)
				.object(objectName)
				.filename(fileName)
				.build());
	}

	/**
	 * 通过流上传文件
	 *
	 * @param bucketName  存储桶
	 * @param objectName  文件对象
	 * @param inputStream 文件流
	 * @return ObjectWriteResponse
	 */
	@SneakyThrows(Exception.class)
	public ObjectWriteResponse uploadFile(String bucketName, String objectName, InputStream inputStream) {
		return minioClient.putObject(
			PutObjectArgs.builder()
				.bucket(bucketName)
				.object(objectName)
				.stream(inputStream, inputStream.available(), -1)
				.build());
	}

	/**
	 * 拷贝文件
	 *
	 * @param bucketName    存储桶
	 * @param objectName    文件名
	 * @param srcBucketName 目标存储桶
	 * @param srcObjectName 目标文件名
	 */
	@SneakyThrows(Exception.class)
	public ObjectWriteResponse copyFile(String bucketName, String objectName, String srcBucketName, String srcObjectName) {
		return minioClient.copyObject(
			CopyObjectArgs.builder()
				.source(CopySource.builder().bucket(bucketName).object(objectName).build())
				.bucket(srcBucketName)
				.object(srcObjectName)
				.build());
	}

	/**
	 * 删除文件
	 *
	 * @param bucketName 存储桶
	 * @param objectName 文件名称
	 */
	@SneakyThrows(Exception.class)
	public void removeFile(String bucketName, String objectName) {
		minioClient.removeObject(
			RemoveObjectArgs.builder()
				.bucket(bucketName)
				.object(objectName)
				.build());
	}

	/**
	 * 批量删除文件
	 *
	 * @param bucketName 存储桶
	 * @param keys       需要删除的文件列表
	 */
	public void removeFiles(String bucketName, List<String> keys) {
		List<DeleteObject> objects = new LinkedList<>();
		keys.forEach(s -> {
			objects.add(new DeleteObject(s));
			try {
				removeFile(bucketName, s);
			} catch (Exception e) {
				log.error("[Minio工具类]>>>> 批量删除文件，异常：", e);
			}
		});
	}

	/**
	 * 获取文件外链
	 *
	 * @param bucketName 存储桶
	 * @param objectName 文件名
	 * @param expires    过期时间 <=7 秒 （外链有效时间（单位：秒））
	 * @return url
	 */
	@SneakyThrows(Exception.class)
	public String getPresignedObjectUrl(String bucketName, String objectName, Integer expires) {
		GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder().expiry(expires).bucket(bucketName).object(objectName).build();
		return minioClient.getPresignedObjectUrl(args);
	}

	/**
	 * 获得文件外链
	 *
	 * @param bucketName 存储桶名称
	 * @param objectName 文件/文件夹名称
	 * @return url
	 */
	@SneakyThrows(Exception.class)
	public String getPresignedObjectUrl(String bucketName, String objectName) {
		GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder()
			.bucket(bucketName)
			.object(objectName)
			.method(Method.GET).build();
		return minioClient.getPresignedObjectUrl(args);
	}

	/**
	 * 将URLDecoder编码转成UTF8
	 *
	 * @param str url
	 * @return utf-8字符串
	 */
	public String getUtf8ByURLDecoder(String str) {
		String url = str.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
		return URLDecoder.decode(url, StandardCharsets.UTF_8);
	}
}
