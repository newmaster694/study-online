package study.online.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import study.online.auth.provider.CustomAuthenticationProvider;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class DefaultSecurityConfig {

	private final CustomAuthenticationProvider customAuthenticationProvider;

	@Bean
	public SecurityFilterChain defaultAuthenticationSecurityConfig(HttpSecurity http) throws Exception {
		http.authenticationProvider(customAuthenticationProvider);

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
