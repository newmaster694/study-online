package study.online.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.time.Duration;

@Configuration
public class AuthorizationServerConfig {
	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public SecurityFilterChain authorizationServerSecurityFilterChian(HttpSecurity http) throws Exception {

		OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

		http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
			.oidc(Customizer.withDefaults()); // 启用 OpenID Connect 1.0

		http
			.exceptionHandling(e ->
				e.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")))
			.oauth2ResourceServer( oauth2 ->
				oauth2.jwt(Customizer.withDefaults())
			);

		return http.build();

	}

	@Bean
	public RegisteredClientRepository registeredClientRepository() {
		RegisteredClient registeredClient = RegisteredClient
			.withId("xuecheng-plus")//配置客户端唯一标识符

			//设置客户端id与密钥,{noop}表示不进行编码
			.clientId("xc_web_app")
			.clientSecret("{noop}xc_web_app")

			//配置客户端使用基本认证方式【client_id:client_secret 进行base64编码】
			.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)

			.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)//授权码模式

			.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)//启用刷新令牌功能

			.redirectUri("https://localhost:80")//设置回调地址
			.scope("all")//定义客户端请求的权限范围

			//配置要求客户端授权验证
			.clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())

			.tokenSettings(
				TokenSettings.builder()
					.authorizationCodeTimeToLive(Duration.ofMinutes(5))//授权码有效期为5分钟
					.accessTokenTimeToLive(Duration.ofHours(1))//访问令牌有效期为1小时
					.refreshTokenTimeToLive(Duration.ofDays(30))//刷新令牌有效期为30天
					.build()
			)

			.build();

		return new InMemoryRegisteredClientRepository(registeredClient);
	}
}
