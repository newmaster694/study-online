package study.online.captcha.controller;


import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import study.online.captcha.model.CheckCodeParamsDto;
import study.online.captcha.model.CheckCodeResultDto;
import study.online.captcha.service.ICheckCodeService;


/**
 * @author Mr.M
 * @version 1.0
 * @description 验证码服务接口
 * @date 2022/9/29 18:39
 */
@RestController
public class CheckCodeController {

    @Resource(name = "PicCheckCodeService")
    private ICheckCodeService picCheckCodeService;

	/**
	 * 生成验证码接口
	 */
    @PostMapping(value = "/pic")
    public CheckCodeResultDto generatePicCheckCode(CheckCodeParamsDto checkCodeParamsDto){
        return picCheckCodeService.generate(checkCodeParamsDto);
    }

	/**
	 * 校验验证码接口
	 */
    @PostMapping(value = "/verify")
    public Boolean verify(String key, String code){
	    return picCheckCodeService.verify(key,code);
    }
}
