package study.online.captcha.service.impl;

import org.springframework.stereotype.Component;
import study.online.captcha.service.ICheckCodeService;

import java.util.Random;

/**
 * @author Mr.M
 * @version 1.0
 * @description 数字字母生成器
 * @date 2022/9/29 18:28
 */
@Component("NumberLetterCheckCodeGenerator")
public class NumberLetterCheckCodeGenerator implements ICheckCodeService.CheckCodeGenerator {


    @Override
    public String generate(int length) {
        String str="ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(36);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }


}
