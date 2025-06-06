package com.study.online.base.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class MybatisConfig {
	@Bean
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		log.info("初始化Mybatis-Plus分页插件");

		//初始化核心插件
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

		//添加分页插件
		interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
		return interceptor;
	}
}
