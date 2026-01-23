package study.online.auth.service.impl;

import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import study.online.auth.service.IAuthService;
import study.online.base.constant.ErrorMessage;
import study.online.base.exception.BaseException;
import study.online.ucenter.model.dto.AuthParamsDTO;
import study.online.ucenter.model.dto.XcUserExt;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService {

	private final ApplicationContext applicationContext;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		//实现多种认证方式校验
		AuthParamsDTO authParamsDTO;

		//将认证参数转为AuthParamsDto类型
		try {
			authParamsDTO = JSON.parseObject(username, AuthParamsDTO.class);
		} catch (Exception e) {
			log.info("认证请求不符合项目要求");
			throw new BaseException(ErrorMessage.VALDATE_PARAMETER_ERROR);
		}

		IAuthService authService = applicationContext
			.getBean(
				authParamsDTO.getAuthType() + "_authentication",
				IAuthService.class);

		XcUserExt userExt = authService.execute(authParamsDTO);

		return this.getUserPrincipal(userExt);
	}

	/**
	 * 查询用户信息并返回生成JWT令牌
	 */
	private UserDetails getUserPrincipal(XcUserExt userExt) {
		//用户权限,如果不加报Cannot pass a null GrantedAuthority collection
		String[] authorities = {"p1"};

		String password = userExt.getPassword();

		//为了安全在令牌中不放密码
		userExt.setPassword(null);

		//将user对象转json
		String userString = JSON.toJSONString(userExt);

		//创建UserDetails对象
		return User
			.withUsername(userString)
			.password(password)
			.authorities(authorities).build();
	}
}
