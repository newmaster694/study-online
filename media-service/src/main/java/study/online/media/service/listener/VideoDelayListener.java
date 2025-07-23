package study.online.media.service.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import study.online.base.constant.MQConstant;
import study.online.media.model.po.MediaProcess;
import study.online.media.service.IMediaFileService;
import study.online.media.service.IMediaProcessService;
import study.online.media.utils.FileUtil;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class VideoDelayListener {

	private final IMediaProcessService mediaProcessService;
	private final IMediaFileService mediaFileService;

	private final StringRedisTemplate redisTemplate;

	@RabbitListener(bindings = @QueueBinding(
		value = @Queue(name = MQConstant.DELAY_QUEUE_NAME),
		exchange = @Exchange(name = MQConstant.DELAY_EXCHANGE_NAME, delayed = "true"),
		key = MQConstant.DELAY_VIDEO_KEY))
	public void timeoutProcessHandler(Long mediaProcessId) {
		MediaProcess mediaProcess = mediaProcessService.getOne(mediaProcessId);
		if (mediaProcess.getStatus().equals("2")) {
			return;
		}

		mediaProcessService.saveProcessFinishStatus(
			mediaProcess.getId(),
			"3",
			mediaProcess.getFileId(),
			null,
			"处理文件超时");
	}

	@RabbitListener(bindings = @QueueBinding(
		value = @Queue(name = MQConstant.FILE_INTERRUPT_QUEUE_NAME),
		exchange = @Exchange(name = MQConstant.FILE_INTERRUPT_EXCHANGE_NAME, delayed = "true"),
		key = MQConstant.FILE_INTERRUPT_KEY))
	public void interruptFileHandler(String fileMd5) {
		String pattern = "chunk:" + fileMd5 + ":*";

		Set<String> keys = redisTemplate.keys(pattern);
		if (!keys.isEmpty()) {
			redisTemplate.delete(keys);
			mediaFileService.clearChunkFiles(FileUtil.getChunkFileFolderPath(fileMd5));
		}
	}
}
