package study.online.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import study.online.api.client.CaptchaClient;
import study.online.auth.service.IAuthService;
import study.online.base.constant.ErrorMessage;
import study.online.base.exception.BaseException;
import study.online.ucenter.mapper.XcUserMapper;
import study.online.ucenter.model.dto.AuthParamsDTO;
import study.online.ucenter.model.dto.XcUserExt;
import study.online.ucenter.model.po.XcUser;

@RequiredArgsConstructor
@Service("password_authentication")
public class PasswordAuthServiceImpl implements IAuthService {

	private final XcUserMapper userMapper;
	private final PasswordEncoder passwordEncoder;
	private final CaptchaClient captchaClient;

	@Override
	public XcUserExt execute(AuthParamsDTO authParamsDTO) {
		//校验验证码
		String checkcode = authParamsDTO.getCheckcode();
		String checkcodekey = authParamsDTO.getCheckcodekey();

		if (StringUtils.isBlank(checkcode) || StringUtils.isBlank(checkcodekey)) {
			throw new BaseException(ErrorMessage.EMPTY_CAPTCHA);
		}

		Boolean flag = captchaClient.verify(checkcodekey, checkcode);
		if (!flag) {
			throw new BaseException(ErrorMessage.ERROR_CAPTCHA);
		}

		//账号
		String username = authParamsDTO.getUsername();
		XcUser user = userMapper.selectOne(new LambdaQueryWrapper<XcUser>().eq(XcUser::getUsername, username));

		if (user == null) {
			//返回空表示用户不存在
			throw new RuntimeException("账号不存在");
		}

		XcUserExt xcUserExt = new XcUserExt();
		BeanUtils.copyProperties(user, xcUserExt);

		//校验密码
		//取出数据库存储的正确密码
		String passwordDb = user.getPassword();
		String passwordForm = authParamsDTO.getPassword();
		boolean matches = passwordEncoder.matches(passwordForm, passwordDb);
		if (!matches) {
			throw new BaseException(ErrorMessage.ERROR_AUTHENTICATION_PASSWORD);
		}

		return xcUserExt;
	}
}
