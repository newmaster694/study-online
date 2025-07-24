package study.online.base.config;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author weiz
 */
@Data
@Configuration
public class MinioConfig {

	/*访问地址*/
	@Value("${minio.endpoint}")
	private String endpoint;

	/*accessKey类似于用户ID，用于唯一标识你的账户*/
	@Value("${minio.accessKey}")
	private String accessKey;

	/*secretKey是你账户的密码*/
	@Value("${minio.secretKey}")
	private String secretKey;

	@Bean
	public MinioClient minioClient() {
		return MinioClient.builder()
			.endpoint(endpoint)
			.credentials(accessKey, secretKey)
			.build();
	}
}