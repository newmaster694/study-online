package study.online.base.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({JsonConfig.class, MinioConfig.class, MvcConfig.class, MybatisConfig.class})
public class SpringConfig {
}
