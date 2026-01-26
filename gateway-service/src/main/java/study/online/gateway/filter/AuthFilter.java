package study.online.gateway.filter;

import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import study.online.base.model.RestErrorResponse;
import study.online.gateway.config.properties.SystemProperties;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

/**
 * @author Mr.M
 * @version 1.0
 * @description 网关认证过虑器
 * @date 2022/9/27 12:10
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthFilter implements GlobalFilter, Ordered {

	//白名单
	private final SystemProperties systemProperties;

	private final JwtDecoder jwtDecoder;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		String requestUrl = exchange.getRequest().getPath().value();
		AntPathMatcher pathMatcher = new AntPathMatcher();

		String[] whitelist = systemProperties.getSecurityWhitelistPath();

		//白名单放行
		for (String url : whitelist) {
			if (pathMatcher.match(url, requestUrl)) {
				return chain.filter(exchange);
			}
		}

		//检查token是否存在
		String token = getToken(exchange);
		if (StringUtils.isBlank(token)) {
			return buildReturnMono("没有认证", exchange);
		}

		try {
			// 使用JwtDecoder验证JWT token
			Jwt jwt = jwtDecoder.decode(token);

			// 检查token是否过期
			if (jwt.getExpiresAt() != null && jwt.getExpiresAt().isBefore(Instant.now())) {
				return buildReturnMono("认证令牌已过期", exchange);
			}

			//传递用户信息
			String userInfo = jwt.getClaimAsString("user_name");

			exchange = exchange.mutate()
				.request(builder -> builder.header("user-info", userInfo))
				.build();

			return chain.filter(exchange);
		} catch (JwtValidationException e) {
			log.info("认证令牌无效: {}", token);
			return buildReturnMono("认证令牌无效", exchange);
		}
	}

	/**
	 * 获取token
	 */
	private String getToken(ServerWebExchange exchange) {
		String tokenStr = exchange.getRequest().getHeaders().getFirst("Authorization");
		if (StringUtils.isBlank(tokenStr)) {
			return null;
		}
		String token = tokenStr.split(" ")[1];
		if (StringUtils.isBlank(token)) {
			return null;
		}
		return token;
	}


	private Mono<Void> buildReturnMono(String error, ServerWebExchange exchange) {
		ServerHttpResponse response = exchange.getResponse();
		String jsonString = JSON.toJSONString(new RestErrorResponse(error));
		byte[] bits = jsonString.getBytes(StandardCharsets.UTF_8);
		DataBuffer buffer = response.bufferFactory().wrap(bits);
		response.setStatusCode(HttpStatus.UNAUTHORIZED);
		response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
		return response.writeWith(Mono.just(buffer));
	}


	@Override
	public int getOrder() {
		return 0;
	}
}
