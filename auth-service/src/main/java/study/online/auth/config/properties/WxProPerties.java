package study.online.auth.config.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class WxProPerties {
	@Value("${weixin.appid}")
	private String appid;

	@Value("${weixin.secret}")
	private String secret;
}
