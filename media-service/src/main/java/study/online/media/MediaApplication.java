package study.online.media;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import study.online.base.config.SpringConfig;

@Slf4j
@Import(SpringConfig.class)
@EnableTransactionManagement
@EnableAspectJAutoProxy(exposeProxy = true) //暴露代理对象注解
@SpringBootApplication(scanBasePackages = {"study.online", "study.online.base"})
public class MediaApplication {
	public static void main(String[] args) {
		SpringApplication.run(MediaApplication.class, args);
		log.info("媒体资料服务已启动...");
	}
}
