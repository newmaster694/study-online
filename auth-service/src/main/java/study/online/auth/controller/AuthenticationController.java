package study.online.auth.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import study.online.auth.service.IWXAuthService;
import study.online.ucenter.model.po.XcUser;

/**
 * @author Mr.M
 * @version 1.0
 * @description 测试controller
 * @date 2022/9/27 17:25
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class AuthenticationController {

	private final IWXAuthService wxAuthService;

	@RequestMapping("/wx-login")
	public String login(String code, String state) {
		log.debug("微信扫码回调:code-{};state-{}", code, state);

		//请求微信申请openid,拿到openid查询用户信息，将用户信息写入数据库
		XcUser user = wxAuthService.wxAuth(code);

		if (user == null) {
			return "redirect:http://www.51xuecheng.cn/error.html";
		}

		String username = user.getUsername();
		return "redirect:http://www.51xuecheng.cn/sign.html?username="+username+"&authType=wx";
	}
}
