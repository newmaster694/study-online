package study.online.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import study.online.api.client.fallback.CaptchaClientFactory;
import study.online.api.config.FeignConfig;

@FeignClient(value = "captcha-service", configuration = FeignConfig.class, fallback = CaptchaClientFactory.class)
public interface CaptchaClient {

	@PostMapping("/captcha/verify")
	Boolean verify(@RequestParam("key") String key, @RequestParam("code") String code);
}
