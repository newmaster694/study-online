package study.online.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import study.online.api.client.fallback.SearchServiceFallbackFactory;
import study.online.api.config.FeignConfig;
import study.online.api.model.po.CourseIndex;

@FeignClient(value = "search-service", configuration = FeignConfig.class, fallbackFactory = SearchServiceFallbackFactory.class)
public interface SearchClient {

	@PostMapping("/search/index/course")
	Boolean add(@RequestBody CourseIndex courseIndex);
}
