package study.online.content.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;

		String userInfo = httpRequest.getHeader("user-info");
		if (userInfo != null && !userInfo.isEmpty()) {
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userInfo, null);
			authentication.setAuthenticated(true);

			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		chain.doFilter(request, response);
	}
}
