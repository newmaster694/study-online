package study.online.content.config;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class XxlJobConfig {

	@Value("${xxl.job.admin.addresses}")
	private String adminAddress;

	@Value("${xxl.job.accessToken}")
	private String accessToken;

	@Value("${xxl.job.executor.appname}")
	private String appname;

	@Value("${xxl.job.executor.port}")
	private Integer port;

	@Value("${xxl.job.executor.logpath}")
	private String logPath;

	@Value("${xxl.job.executor.logretentiondays}")
	private Integer logRetentionDays;

	@Bean
	public XxlJobSpringExecutor xxlJobSpringExecutor() {
		log.info("xxl-job config init...");

		XxlJobSpringExecutor executor = new XxlJobSpringExecutor();

		executor.setAdminAddresses(adminAddress);
		executor.setAppname(appname);
		executor.setPort(port);
		executor.setAccessToken(accessToken);
		executor.setLogPath(logPath);
		executor.setLogRetentionDays(logRetentionDays);

		return executor;
	}
}
