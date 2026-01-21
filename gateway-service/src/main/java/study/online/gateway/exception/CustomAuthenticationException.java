package study.online.gateway.exception;

import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import study.online.base.model.RestResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Component
public class CustomAuthenticationException implements AuthenticationEntryPoint {
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setHeader("Content-Type", "application/json;charset=utf-8");

		RestResponse<String> body = new RestResponse<String>()
			.setCode(-1).setErrormsg(List.of("未认证的异常"));

		PrintWriter out = response.getWriter();
		out.write(JSONUtil.toJsonStr(body));
		out.flush();
	}
}
