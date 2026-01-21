package study.online.captcha.service.impl;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import study.online.captcha.service.ICheckCodeService;

import java.util.concurrent.TimeUnit;

/**
 * @author Mr.M
 * @version 1.0
 * @description 使用redis存储验证码，测试用
 * @date 2022/9/29 18:36
 */
@Component("RedisCheckCodeStore")
public class RedisCheckCodeStore implements ICheckCodeService.CheckCodeStore {

	@Resource
	private RedisTemplate<String, Object> redisTemplate;


	@Override
	public void set(String key, String value, Integer expire) {
		redisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
	}

	@Override
	public String get(String key) {
		return (String) redisTemplate.opsForValue().get(key);
	}

	@Override
	public void remove(String key) {
		redisTemplate.delete(key);
	}
}
