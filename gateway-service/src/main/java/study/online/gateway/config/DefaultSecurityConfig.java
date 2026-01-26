package study.online.gateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import study.online.gateway.config.properties.SystemProperties;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class DefaultSecurityConfig {

	private final SystemProperties systemProperties;

	@Bean
	public SecurityWebFilterChain defaultGatewaySecurityConfig(ServerHttpSecurity http) throws Exception {

		http
			.authorizeExchange(exchange ->
				exchange
					.pathMatchers(systemProperties.getSecurityWhitelistPath()).permitAll()
					.anyExchange().authenticated()
			)
			.csrf(ServerHttpSecurity.CsrfSpec::disable)
			.cors(ServerHttpSecurity.CorsSpec::disable)
		;

		return http.build();
	}
}
