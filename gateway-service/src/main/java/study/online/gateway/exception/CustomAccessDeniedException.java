package study.online.gateway.exception;

import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import study.online.base.model.RestResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Component
public class CustomAccessDeniedException implements AccessDeniedHandler {
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.setHeader("Content-Type", "application/json;charset=utf-8");

		RestResponse<String> body = new RestResponse<String>()
			.setCode(-1).setErrormsg(List.of("权限不足，请联系管理员"));

		PrintWriter out = response.getWriter();
		out.write(JSONUtil.toJsonStr(body));
		out.flush();
	}
}
