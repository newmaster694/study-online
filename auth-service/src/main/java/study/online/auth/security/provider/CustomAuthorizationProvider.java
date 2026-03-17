package study.online.auth.security.provider;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import study.online.auth.security.token.CustomAuthenticationToken;
import study.online.ucenter.model.dto.AuthParamsDTO;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CustomAuthorizationProvider implements AuthenticationProvider {
	private final OAuth2AuthorizationService authorizationService;
	private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;
	private final RegisteredClientRepository registeredClientRepository;

	public CustomAuthorizationProvider(OAuth2AuthorizationService authorizationService, OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator, RegisteredClientRepository registeredClientRepository) {
		Assert.notNull(authorizationService, "authorizationService cannot be null");
		Assert.notNull(tokenGenerator, "tokenGenerator cannot be null");
		this.authorizationService = authorizationService;
		this.tokenGenerator = tokenGenerator;
		this.registeredClientRepository = registeredClientRepository;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		log.debug("进入 provider 处理");
		CustomAuthenticationToken customAuthenticationToken = (CustomAuthenticationToken) authentication;

		Map<String, Object> additionalParameters = customAuthenticationToken.getAdditionalParameters();

		/*Ensure the client is authenticated - 这里自定义一波 client 认证方式*/
		String clientId = (String) additionalParameters.get(OAuth2ParameterNames.CLIENT_ID);
		log.debug("provider 中的参数：{}", additionalParameters);
		if (!StringUtils.hasText(clientId)) {
			log.debug("client_id 出错了");
			throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
		}

		/*这里注入并校验RegisteredClient是否合规*/
		RegisteredClient registeredClient = registeredClientRepository.findByClientId(clientId);
		if (registeredClient == null) {
			log.debug("registeredClientRepository 出错了");
			throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
		}

		/*检查是否是合法的 grant_type*/
		if (!registeredClient.getAuthorizationGrantTypes().contains(CustomAuthenticationToken.CUSTOM)) {
			throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
		}

		/*开始校验参数*/
		log.debug("开始校验参数");
		String authBodyStr = (String) additionalParameters.get(OAuth2ParameterNames.CODE);
		AuthParamsDTO authParamsDTO = JSON.parseObject(authBodyStr, AuthParamsDTO.class);

		log.debug(authParamsDTO.toString());

		// Generate the access token
		log.debug("校验参数成功，开始生成 token");
		OAuth2TokenContext tokenContext = DefaultOAuth2TokenContext.builder()
			.registeredClient(registeredClient)
			.principal(customAuthenticationToken)
			.authorizationServerContext(AuthorizationServerContextHolder.getContext())
			.tokenType(OAuth2TokenType.ACCESS_TOKEN)
			.authorizationGrantType(CustomAuthenticationToken.CUSTOM)
			.authorizationGrant(customAuthenticationToken)
			.build();

		OAuth2Token generatedAccessToken = this.tokenGenerator.generate(tokenContext);
		if (generatedAccessToken == null) {
			OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
				"The token generator failed to generate the access token.", null);
			throw new OAuth2AuthenticationException(error);
		}
		OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
			generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(),
			generatedAccessToken.getExpiresAt(), null);

		// Initialize the OAuth2Authorization
		OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.withRegisteredClient(registeredClient)
			.principalName(authBodyStr)
			.authorizationGrantType(CustomAuthenticationToken.CUSTOM);

		if (generatedAccessToken instanceof ClaimAccessor) {
			authorizationBuilder.token(accessToken, (metadata) ->
				metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, ((ClaimAccessor) generatedAccessToken).getClaims()));
		} else {
			authorizationBuilder.accessToken(accessToken);
		}

		// Generate Refresh Token
		OAuth2RefreshToken refreshToken = null;
		if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN)) {
			OAuth2TokenContext refreshTokenContext = DefaultOAuth2TokenContext.builder()
				.registeredClient(registeredClient)
				.principal(customAuthenticationToken)
				.authorizationServerContext(AuthorizationServerContextHolder.getContext())
				.tokenType(OAuth2TokenType.REFRESH_TOKEN)
				.authorizationGrantType(CustomAuthenticationToken.CUSTOM)
				.authorizationGrant(customAuthenticationToken)
				.build();

			OAuth2Token generatedRefreshToken = this.tokenGenerator.generate(refreshTokenContext);
			if (generatedRefreshToken != null) {
				refreshToken = new OAuth2RefreshToken(
					generatedRefreshToken.getTokenValue(),
					generatedRefreshToken.getIssuedAt(),
					generatedRefreshToken.getExpiresAt());
				authorizationBuilder.refreshToken(refreshToken);
			}
		}

		/*构造响应请求*/
		Map<String, Object> additionalResponseParams = new HashMap<>();
		if (refreshToken != null) {
			additionalResponseParams.put(OAuth2ParameterNames.REFRESH_TOKEN, refreshToken.getTokenValue());
		}

		OAuth2Authorization authorization = authorizationBuilder.build();
		authorizationService.save(authorization);

		return new OAuth2AccessTokenAuthenticationToken(registeredClient, customAuthenticationToken, accessToken, refreshToken, additionalResponseParams);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return CustomAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
