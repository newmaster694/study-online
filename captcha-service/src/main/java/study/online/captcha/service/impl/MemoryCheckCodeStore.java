package study.online.captcha.service.impl;

import org.springframework.stereotype.Component;
import study.online.captcha.service.ICheckCodeService;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mr.M
 * @version 1.0
 * @description 使用本地内存存储验证码，测试用
 * @date 2022/9/29 18:36
 */
@Component("MemoryCheckCodeStore")
public class MemoryCheckCodeStore implements ICheckCodeService.CheckCodeStore {

    Map<String,String> map = new HashMap<>();

    @Override
    public void set(String key, String value, Integer expire) {
        map.put(key,value);
    }

    @Override
    public String get(String key) {
        return map.get(key);
    }

    @Override
    public void remove(String key) {
        map.remove(key);
    }
}
