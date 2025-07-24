package study.online.content;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import study.online.base.config.SpringConfig;


@Slf4j
@Import(SpringConfig.class)
@EnableTransactionManagement
@MapperScan("study.online.content.mapper")
@SpringBootApplication(scanBasePackages = {"study.online.content", "study.online.messagesdk", "study.online.base"})
public class ContentApplication {
	public static void main(String[] args) {
		SpringApplication.run(ContentApplication.class, args);
		log.info("内容管理模块启动成功...");
	}
}
