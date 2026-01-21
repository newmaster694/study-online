package study.online.captcha.service.impl;

import cn.hutool.core.codec.Base64;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import study.online.captcha.model.CheckCodeParamsDto;
import study.online.captcha.model.CheckCodeResultDto;
import study.online.captcha.service.AbstractCheckCodeService;
import study.online.captcha.service.ICheckCodeService;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Mr.M
 * @version 1.0
 * @description 图片验证码生成器
 * @date 2022/9/29 16:16
 */
@Slf4j
@Service("PicCheckCodeService")
public class PicCheckCodeServiceImpl extends AbstractCheckCodeService implements ICheckCodeService {


    @Autowired
    private DefaultKaptcha kaptcha;


	@Override
    public CheckCodeResultDto generate(CheckCodeParamsDto checkCodeParamsDto) {
        GenerateResult generate = generate(4, "checkcode:", 60);
        String key = generate.getKey();
        String code = generate.getCode();
        String pic = createPic(code);
        CheckCodeResultDto checkCodeResultDto = new CheckCodeResultDto();
        checkCodeResultDto.setAliasing(pic);
        checkCodeResultDto.setKey(key);
        return checkCodeResultDto;

    }

    private String createPic(String code) {
        // 生成图片验证码
        ByteArrayOutputStream outputStream;
        BufferedImage image = kaptcha.createImage(code);

        outputStream = new ByteArrayOutputStream();
        String imgBase64Encoder = null;
        try {
            // 对字节数组Base64编码
	        ImageIO.write(image, "png", outputStream);
            imgBase64Encoder = "data:image/png;base64," + Base64.encode(outputStream.toByteArray());
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        return imgBase64Encoder;
    }
}
