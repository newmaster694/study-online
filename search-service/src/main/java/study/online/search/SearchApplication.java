package study.online.search;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class SearchApplication {
	public static void main(String[] args) {
		SpringApplication.run(SearchApplication.class, args);
		log.info("搜索服务启动成功");
	}
}
