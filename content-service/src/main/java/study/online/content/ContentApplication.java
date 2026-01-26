package study.online.content;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Slf4j
@ServletComponentScan
@EnableTransactionManagement
@MapperScan("study.online.content.mapper")
@EnableFeignClients(basePackages = "study.online.api.client")
@SpringBootApplication(scanBasePackages = {"study.online.content", "study.online.messagesdk"})
public class ContentApplication {
	public static void main(String[] args) {
		SpringApplication.run(ContentApplication.class, args);
		log.info("内容管理模块启动成功...");
	}
}
