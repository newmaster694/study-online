package study.online.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import study.online.api.client.fallback.ContentClientFallbackFactory;
import study.online.api.model.po.CoursePublish;

@FeignClient(value = "content-service", fallback = ContentClientFallbackFactory.class)
public interface ContentClient {

	@GetMapping("/course-publish/{courseId}")
	CoursePublish getCoursePublish(@PathVariable Long courseId);
}
