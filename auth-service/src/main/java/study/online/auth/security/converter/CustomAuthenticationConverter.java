package study.online.auth.security.converter;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import study.online.auth.security.token.CustomAuthenticationToken;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Slf4j
public class CustomAuthenticationConverter implements AuthenticationConverter {
	@Override
	public Authentication convert(HttpServletRequest request) {
		// grant_type (REQUIRED)
		String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
		if (!"custom".equals(grantType)) {
			return null;
		}

		MultiValueMap<String, String> parameters = getParameters(request);

		String clientId = parameters.getFirst(OAuth2ParameterNames.CLIENT_ID);
		if (!StringUtils.hasText(clientId)) {
			throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_REQUEST);
		}

		// authentication body
		String authBody = parameters.getFirst(OAuth2ParameterNames.CODE);
		if (!StringUtils.hasText(authBody) ||
			parameters.get(OAuth2ParameterNames.CODE).size() != 1) {
			throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_REQUEST);
		}

		Map<String, Object> additionalParameters = new HashMap<>();
		parameters.forEach((key, value) -> { // 保留 client_id / authBody 用于后续校验
			if (!key.equals(OAuth2ParameterNames.GRANT_TYPE) && !key.equals(OAuth2ParameterNames.CLIENT_SECRET)) {
				additionalParameters.put(key, value.get(0));
			}
		});

		log.debug("交给下游 provider 处理");

		/*构造一个已认证的clientPrincipal*/
		OAuth2ClientAuthenticationToken oAuth2ClientAuthenticationToken = new OAuth2ClientAuthenticationToken(clientId, ClientAuthenticationMethod.CLIENT_SECRET_POST, null, additionalParameters);
		oAuth2ClientAuthenticationToken.setAuthenticated(true);

		String[] scopeArr = Objects.requireNonNull(parameters.getFirst(OAuth2ParameterNames.SCOPE)).split(",");
		Set<String> scope = Set.of(scopeArr);

		log.debug("converter 中的参数：{}", additionalParameters);
		return new CustomAuthenticationToken(oAuth2ClientAuthenticationToken, scope, additionalParameters);
	}

	private static MultiValueMap<String, String> getParameters(HttpServletRequest request) {
		Map<String, String[]> parameterMap = request.getParameterMap();
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>(parameterMap.size());
		parameterMap.forEach((key, values) -> {
			for (String value : values) {
				parameters.add(key, value);
			}
		});
		return parameters;
	}
}
