package study.online.auth.service;

import study.online.ucenter.model.dto.AuthParamsDTO;
import study.online.ucenter.model.dto.XcUserExt;

public interface IAuthService {

	/**
	 * 认证方法
	 * @param authParamsDTO 认证参数
	 * @return 用户信息
	 */
	XcUserExt execute(AuthParamsDTO authParamsDTO);
}
