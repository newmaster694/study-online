package study.online.base.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * <p>注册web层相关组件</p>
 */
@Slf4j
@Configuration
@ConditionalOnClass(DispatcherServlet.class)
public class MvcConfig extends WebMvcConfigurationSupport {
}
