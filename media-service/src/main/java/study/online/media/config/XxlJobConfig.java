package study.online.media.config;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class XxlJobConfig {
	private Logger logger = LoggerFactory.getLogger(XxlJobConfig.class);

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
		logger.info("xxl-job config init...");

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
