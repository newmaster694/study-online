package study.online.order.utils;

import cn.hutool.extra.qrcode.QrCodeUtil;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Slf4j
public class QRCodeUtil {

	/**
	 * 生成二维码
	 *
	 * @param content 二维码对应的URL
	 * @param width   二维码图片宽度
	 * @param height  二维码图片高度
	 */
	public String createQRCode(String content, int width, int height) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		BufferedImage bufferedImage = QrCodeUtil.generate(content, width, height);

		ImageIO.write(bufferedImage, "png", os);
		/*原生转码前面没有 data:image/png;base64 这些字段，返回给前端是无法被解析，所以加上前缀*/
		return "data:image/png;base64," + Base64.getEncoder().encodeToString(os.toByteArray());
	}
}