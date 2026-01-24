package study.online.auth.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import study.online.auth.service.IAuthService;
import study.online.auth.service.IWXAuthService;
import study.online.ucenter.mapper.XcUserMapper;
import study.online.ucenter.mapper.XcUserRoleMapper;
import study.online.ucenter.model.dto.AuthParamsDTO;
import study.online.ucenter.model.dto.XcUserExt;
import study.online.ucenter.model.po.XcUser;
import study.online.ucenter.model.po.XcUserRole;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service("wx_authentication")
public class WXAuthenticationServiceImpl implements IAuthService, IWXAuthService {

	private final XcUserMapper xcUserMapper;
	private final RestTemplate restTemplate;
	private final XcUserRoleMapper xcUserRoleMapper;
	private final WXAuthenticationServiceImpl currentProxy;

	@Value("${weixin.appid}")
	private String appid;

	@Value("${weixin.secret}")
	private String secret;

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
		//收到code调用微信接口申请access_token
		Map<String, String> access_token_map = this.getAccessToken(code);

		if (access_token_map == null) {
			return null;
		}

		System.out.println(access_token_map);
		String openid = access_token_map.get("openid");
		String access_token = access_token_map.get("access_token");

		//拿access_token查询用户信息
		Map<String, String> userinfo = getUserinfo(access_token, openid);

		if (userinfo == null) {
			return null;
		}
		//添加用户到数据库
		return currentProxy.addVXUser(userinfo);
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

	/**
	 * 申请访问令牌
	 * <p>
	 * 响应示例：
	 * <p>
	 * {@code     {
	 * "access_token":"ACCESS_TOKEN",
	 * "expires_in":7200,
	 * "refresh_token":"REFRESH_TOKEN",
	 * "openid":"OPENID",
	 * "scope":"SCOPE",
	 * "unionid": "o6_bmasdasdsad6_2sgVt7hMZOPfL"
	 * }}
	 */
	private Map<String, String> getAccessToken(String code) {

		String wxUrl_template = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";

		//请求微信地址
		String wxUrl = String.format(wxUrl_template, appid, secret, code);

		log.info("getAccessToken-调用微信接口申请access_token, url-{}", wxUrl);

		ResponseEntity<String> exchange = restTemplate.exchange(wxUrl, HttpMethod.POST, null, String.class);

		String result = exchange.getBody();
		log.info("getAccessToken-调用微信接口申请access_token, 返回值-{}", result);

		return JSON.parseObject(result, Map.class);
	}

	/**
	 * 获取用户信息
	 * <p>
	 * 示例如下
	 * <p>
	 * {@code     {
	 * "openid":"OPENID",
	 * "nickname":"NICKNAME",
	 * "sex":1,
	 * "province":"PROVINCE",
	 * "city":"CITY",
	 * "country":"COUNTRY",
	 * "headimgurl": "https://thirdwx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/0",
	 * "privilege":[
	 * "PRIVILEGE1",
	 * "PRIVILEGE2"
	 * ],
	 * "unionid": " o6_bmasdasdsad6_2sgVt7hMZOPfL"
	 * }}
	 */
	private Map<String, String> getUserinfo(String access_token, String openid) {

		String wxUrl_template = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s";
		//请求微信地址
		String wxUrl = String.format(wxUrl_template, access_token, openid);

		log.info("getUserinfo-调用微信接口申请access_token, url-{}", wxUrl);

		ResponseEntity<String> exchange = restTemplate.exchange(wxUrl, HttpMethod.POST, null, String.class);

		//防止乱码进行转码
		String result = new String(exchange.getBody().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
		log.info("getUserinfo-调用微信接口申请access_token, 返回值-{}", result);

		return JSON.parseObject(result, Map.class);
	}
}
