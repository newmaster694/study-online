package study.online.auth.security.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

	@Autowired
	public void setUserDetailService(UserDetailsService userDetailsService) {
		super.setUserDetailsService(userDetailsService);
	}

	/**
	 * 屏蔽密码对比
	 */
	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
	}
}
