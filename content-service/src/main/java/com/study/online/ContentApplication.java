package com.study.online;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class ContentApplication {
	public static void main(String[] args) {
		SpringApplication.run(ContentApplication.class, args);
		log.info("内容管理模块启动成功...");
	}
}
