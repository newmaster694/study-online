package study.online.auth.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import study.online.ucenter.mapper.XcUserMapper;
import study.online.ucenter.model.po.XcUser;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService {

	private final XcUserMapper userMapper;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		XcUser user = userMapper.selectOne(new LambdaQueryWrapper<>(new XcUser())
			.eq(XcUser::getUsername, username));

		if (user == null) {
			return null;
		}

		String[] authorities= {"test"};

		//这里的思路是将用户信息转换为JSON字符串存入UserDetails中，因为JWT令牌的生成看的就是UserDetails的数据
		return User.builder()
			.username(JSON.toJSONString(user.setPassword(null)))
			.password(user.getPassword())
			.authorities(authorities)
			.build();
	}
}
