package study.online.learning;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@Slf4j
@SpringBootApplication
@MapperScan(basePackages = "study.online.learning.mapper")
@EnableFeignClients(basePackages = "study.online.api.client")
public class LearningApplication {
	public static void main(String[] args) {
		SpringApplication.run(LearningApplication.class, args);
		log.info("选课服务启动成功");
	}
}
