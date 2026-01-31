package study.online.order.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "pay.alipay")
public class AliPayProperties {
	private String appId;
	private String privateKey;
	private String publicKey;
}
