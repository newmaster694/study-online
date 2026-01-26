package study.online.auth.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthWeChatOpenRequest;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.online.auth.config.properties.WxProPerties;
import study.online.auth.service.IAuthService;
import study.online.auth.service.IWXAuthService;
import study.online.ucenter.mapper.XcUserMapper;
import study.online.ucenter.mapper.XcUserRoleMapper;
import study.online.ucenter.model.dto.AuthParamsDTO;
import study.online.ucenter.model.dto.XcUserExt;
import study.online.ucenter.model.po.XcUser;
import study.online.ucenter.model.po.XcUserRole;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service("wx_authentication")
public class CustomWxAuthenticationService implements IAuthService, IWXAuthService {

	private final XcUserMapper xcUserMapper;
	private final XcUserRoleMapper xcUserRoleMapper;

	private CustomWxAuthenticationService currentProxy;

	private final WxProPerties wxProPerties;

	@Override
	public XcUserExt execute(AuthParamsDTO authParamsDTO) {
		LambdaQueryWrapper<XcUser> queryWrapper = new LambdaQueryWrapper<XcUser>()
			.eq(XcUser::getUsername, authParamsDTO.getUsername());

		XcUser user = xcUserMapper.selectOne(queryWrapper);

		//拷贝Bean并返回
		return BeanUtil.copyProperties(user, XcUserExt.class);
	}

	@Override
	public XcUser wxAuth(String code) {

		AuthConfig authConfig = AuthConfig.builder()
			.clientId(wxProPerties.getAppid())
			.clientSecret(wxProPerties.getSecret())
			.build();

		AuthWeChatOpenRequest authRequest = new AuthWeChatOpenRequest(authConfig);

		try {
			// 使用code获取用户信息
			AuthCallback callback = AuthCallback.builder().code(code).build();
			AuthResponse authResponse = authRequest.login(callback);

			if (!authResponse.ok()) {
				log.error("微信登录失败-{}", authResponse.getMsg());
				return null;
			}

			// 解析用户信息
			AuthUser authUser = (AuthUser) authResponse.getData();
			Map<String, String> userInfo = new HashMap<>();
			userInfo.put("unionid", authUser.getRawUserInfo().get("unionid").toString());
			userInfo.put("nickname", authUser.getNickname());
			userInfo.put("headimgurl", authUser.getAvatar());
			userInfo.put("openid", authUser.getRawUserInfo().get("openid").toString());

			currentProxy = (CustomWxAuthenticationService) AopContext.currentProxy();

			return currentProxy.addVXUser(userInfo);
		} catch (Exception e) {
			log.error("微信登录异常", e);
			return null;
		}
	}

	@Transactional
	public XcUser addVXUser(Map<String, String> userInfo) {
		String unionid = userInfo.get("unionid");

		//根据unionid查询数据库
		XcUser xcUser = xcUserMapper
			.selectOne(new LambdaQueryWrapper<XcUser>()
				.eq(XcUser::getWxUnionid, unionid));

		if (xcUser != null) {
			return xcUser;
		}

		xcUser = new XcUser()
			.setWxUnionid(unionid)
			.setNickname(userInfo.get("nickname"))//记录从微信得到的昵称
			.setUserpic(userInfo.get("headimgurl"))
			.setName(userInfo.get("nickname"))
			.setUsername(unionid)
			.setPassword(unionid)
			.setUtype("101001")//学生类型
			.setStatus("1")//用户状态
			.setCreateTime(LocalDateTime.now());
		xcUserMapper.insert(xcUser);

		XcUserRole xcUserRole = new XcUserRole();
		xcUserRole.setId(UUID.randomUUID().toString())
			.setUserId(String.valueOf(xcUser.getId()))
			.setRoleId("17");//学生角色
		xcUserRoleMapper.insert(xcUserRole);

		return xcUser;
	}
}
