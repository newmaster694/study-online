package study.online.api.client.fallback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import study.online.api.client.ContentClient;

@Slf4j
public class ContentClientFallbackFactory implements FallbackFactory<ContentClient> {
	@Override
	public ContentClient create(Throwable cause) {
		return courseId -> {
			log.debug("内容服务调用异常;courseid-{}\n,错误信息-{}", courseId, cause.getMessage());
			return null;
		};
	}
}
