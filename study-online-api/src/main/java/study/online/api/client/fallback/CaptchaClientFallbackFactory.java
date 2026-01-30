package study.online.api.client.fallback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import study.online.api.client.CaptchaClient;

@Slf4j
public class CaptchaClientFallbackFactory implements FallbackFactory<CaptchaClient> {
	@Override
	public CaptchaClient create(Throwable cause) {
		return (key, code) -> {
			log.debug("调用验证码服务熔断异常：{}", cause.getMessage());
			return null;
		};
	}
}
