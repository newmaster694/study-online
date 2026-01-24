package study.online.auth.service;

import study.online.ucenter.model.po.XcUser;

/**
 * 微信认证接口
 */
public interface IWXAuthService {

	XcUser wxAuth(String code);
}
