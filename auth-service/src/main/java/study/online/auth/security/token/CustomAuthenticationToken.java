package study.online.auth.security.token;

import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CustomAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {
	public static final AuthorizationGrantType CUSTOM = new AuthorizationGrantType("custom");

	@Getter
	private final Set<String> scopes;

	public CustomAuthenticationToken(Authentication clientPrincipal, Set<String> scopes, Map<String, Object> additionalParameters) {
		super(CUSTOM, clientPrincipal, additionalParameters);
		this.scopes = Collections.unmodifiableSet(scopes != null ? new HashSet<>(scopes) : Collections.emptySet());
	}

	@Override
	public Object getPrincipal() {
		return this.getAdditionalParameters().get(OAuth2ParameterNames.CODE);
	}
}
