package study.online.gateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import study.online.gateway.config.properties.SystemProperties;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class DefaultSecurityConfig {

	private static final String[] whiteList = new SystemProperties().getSecurityWhitelistPath();

	@Bean
	public SecurityFilterChain defaultSecurityConfig(HttpSecurity http) throws Exception {

		http
			.authorizeHttpRequests(auth ->
				auth
					.requestMatchers(whiteList).permitAll()
					.anyRequest().authenticated()
			)
			.csrf(AbstractHttpConfigurer::disable)
			.cors(AbstractHttpConfigurer::disable);

		return http.build();
	}
}
