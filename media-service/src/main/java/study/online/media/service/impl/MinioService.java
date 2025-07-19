package study.online.media.service.impl;

import io.minio.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MinioService {

	private final MinioClient minioClient;

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
	 * 获取文件信息
	 *
	 * @param bucketName bucket名称
	 * @param objectName 文件名称
	 */
	@SneakyThrows(Exception.class)
	public StatObjectResponse getObjectInfo(String bucketName, String objectName) {
		return minioClient.statObject(StatObjectArgs.builder()
			.bucket(bucketName)
			.object(objectName)
			.build());
	}


	/**
	 * 上传图片文件
	 *
	 * @param bucketName  存储桶
	 * @param file        文件名
	 * @param objectName  对象名
	 * @param contentType 类型
	 */
	@SneakyThrows(Exception.class)
	public void uploadFile(String bucketName, MultipartFile file, String objectName, String contentType) {
		InputStream inputStream = file.getInputStream();
		minioClient.putObject(
			PutObjectArgs.builder()
				.bucket(bucketName)
				.object(objectName)
				.contentType(contentType)
				.stream(inputStream, inputStream.available(), -1)
				.build());
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
	 */
	@SneakyThrows(Exception.class)
	public void uploadFile(String bucketName, String objectName, InputStream inputStream) {
		minioClient.putObject(
			PutObjectArgs.builder()
				.bucket(bucketName)
				.object(objectName)
				.stream(inputStream, inputStream.available(), -1)
				.build());
	}

	/**
	 * 批量删除文件
	 *
	 * @param bucketName  存储桶名称
	 * @param objectNames 需要删除的文件对象名列表
	 */
	public void removeFiles(String bucketName, List<String> objectNames) {
		if (objectNames == null || objectNames.isEmpty()) {
			return;
		}

		// 构建待删除对象列表
		List<DeleteObject> deleteObjects = objectNames.stream()
			.map(DeleteObject::new)
			.collect(Collectors.toList());

		// 构造删除请求
		RemoveObjectsArgs args = RemoveObjectsArgs.builder()
			.bucket(bucketName)
			.objects(deleteObjects)
			.build();

		try {
			// 执行批量删除
			Iterable<Result<DeleteError>> results = minioClient.removeObjects(args);

			// 处理结果（记录失败项）
			for (Result<DeleteError> result : results) {
				DeleteError error = result.get();
				if (error != null) {
					log.warn("删除失败: {}，错误信息: {}", error.objectName(), error.message());
				}
			}
		} catch (Exception e) {
			log.error("[Minio工具类] 批量删除文件时发生异常", e);
		}
	}

	@SneakyThrows(Exception.class)
	public void removeFile(String bucketName, String objectName) {
		minioClient.removeObject(RemoveObjectArgs.builder()
			.bucket(bucketName)
			.object(objectName)
			.build());
	}

	/**
	 * 获取路径下文件列表
	 *
	 * @param bucketName 存储桶
	 * @param prefix     文件名称
	 * @param recursive  是否递归查找，false：模拟文件夹结构查找
	 * @return 二进制流
	 */
	public Iterable<Result<Item>> listObjects(String bucketName, String prefix, boolean recursive) {
		return minioClient.listObjects(
			ListObjectsArgs.builder()
				.bucket(bucketName)
				.prefix(prefix)
				.recursive(recursive)
				.build());
	}

	/**
	 * 合并大文件
	 *
	 * @param bucket     存储桶名称
	 * @param path       文件路径
	 * @param sourceList 分片文件列表
	 * @return ObjectWriteResponse
	 */
	@SneakyThrows(Exception.class)
	public ObjectWriteResponse mergeFile(String bucket, String path, List<ComposeSource> sourceList) {
		return minioClient.composeObject(ComposeObjectArgs.builder()
			.bucket(bucket)
			.object(path)
			.sources(sourceList)
			.build());
	}
}
