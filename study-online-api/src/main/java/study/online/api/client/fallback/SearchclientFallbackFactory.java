package study.online.api.client.fallback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import study.online.api.client.SearchClient;

@Slf4j
@Component
public class SearchclientFallbackFactory implements FallbackFactory<SearchClient> {
	@Override
	public SearchClient create(Throwable cause) {
		return courseIndex -> {
			log.error("添加课程索引错误，触发服务熔断降级机制，索引信息-{}，异常信息-{}", courseIndex, cause.toString());
			return false;//走降级逻辑返回false
		};
	}
}
