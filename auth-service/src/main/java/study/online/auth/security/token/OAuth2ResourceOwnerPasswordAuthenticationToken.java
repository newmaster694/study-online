package study.online.auth.security.token;

import lombok.Getter;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.util.Assert;

import java.io.Serial;
import java.util.*;

/**
 * 自定义Oauth2 密码模式认证Token
 * @author zhongyu
 */
public class OAuth2ResourceOwnerPasswordAuthenticationToken extends AbstractAuthenticationToken {

	@Serial
	private static final long serialVersionUID = -6067207202119450764L;

	private final AuthorizationGrantType authorizationGrantType;
	private final Authentication clientPrincipal;

	@Getter
	private final Set<String> scopes;

	@Getter
	private final Map<String, Object> additionalParameters;

	/**
	 * Constructs an {@code OAuth2ClientCredentialsAuthenticationToken} using the provided parameters.
	 *
	 * @param clientPrincipal the authenticated client principal
	 */
	public OAuth2ResourceOwnerPasswordAuthenticationToken(AuthorizationGrantType authorizationGrantType,
	                                                      Authentication clientPrincipal,  @Nullable Set<String> scopes, @Nullable Map<String, Object> additionalParameters) {
		super(Collections.emptyList());
		Assert.notNull(authorizationGrantType, "authorizationGrantType cannot be null");
		Assert.notNull(clientPrincipal, "clientPrincipal cannot be null");
		this.authorizationGrantType = authorizationGrantType;
		this.clientPrincipal = clientPrincipal;
		this.scopes = Collections.unmodifiableSet(scopes != null ? new HashSet<>(scopes) : Collections.emptySet());
		this.additionalParameters = Collections.unmodifiableMap(additionalParameters != null ? new HashMap<>(additionalParameters) : Collections.emptyMap());
	}

	/**
	 * Returns the authorization grant type.
	 *
	 * @return the authorization grant type
	 */
	public AuthorizationGrantType getGrantType() {
		return this.authorizationGrantType;
	}

	@Override
	public Object getPrincipal() {
		return this.clientPrincipal;
	}

	@Override
	public Object getCredentials() {
		return "";
	}

}
