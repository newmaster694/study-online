package study.online.order.config;

import com.ijpay.alipay.AliPayApiConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import study.online.order.config.properties.AliPayProperties;

@Configuration
@RequiredArgsConstructor
public class DefaultAlipayConfig {

	private final AliPayProperties aliPayProperties;

	@Bean
	public AliPayApiConfig aliPayApiConfig() {
		return AliPayApiConfig.builder()
			.setAppId(aliPayProperties.getAppId())
			.setPrivateKey(aliPayProperties.getPrivateKey())
			.setAliPayPublicKey(aliPayProperties.getPublicKey())
			.setCharset("UTF-8")
			.setSignType("RSA2")
			.setServiceUrl("https://openapi.alipaydev.com/gateway.do")
			.setDomain("https://study.online")
			.build();
	}
}
