package study.online.media.service.jobhandler;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import study.online.base.constant.MQConstant;
import study.online.media.model.po.MediaProcess;
import study.online.media.service.IMediaFileService;
import study.online.media.service.IMediaProcessService;
import study.online.media.utils.FileUtil;
import study.online.media.utils.MinioUtil;
import study.online.media.utils.Mp4VideoUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static study.online.base.constant.RedisConstant.VIDEO_JOB_HANDLE_KEY;

@Component
@Slf4j
@RequiredArgsConstructor
public class VideoTask {

	private final IMediaFileService mediaFileService;
	private final IMediaProcessService mediaProcessService;

	private final RedissonClient redissonClient;
	private final RabbitTemplate rabbitTemplate;

	private final FileUtil fileUtil;
	private final MinioUtil minioUtil;

	@Value("${videoprocess.ffmpegpath}")
	private String ffmpegPath;

	@XxlJob("videoJobHandler")
	public void videoJobHandler() throws Exception {
		int shardIndex = XxlJobHelper.getShardIndex();
		int shardTotal = XxlJobHelper.getShardTotal();

		List<MediaProcess> mediaProcessList;
		int size;

		try {
			//取出cpu核心数作为一次处理数据的条数
			int processors = Runtime.getRuntime().availableProcessors();

			//一次处理视频数量不要超过cpu核心数
			mediaProcessList = mediaProcessService.getMediaProcessList(shardIndex, shardTotal, processors);
			size = mediaProcessList.size();
			log.debug("取出待处理视频任务{}条", size);
			if (size == 0) {
				return;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			return;
		}

		//启动size个线程的线程池
		ExecutorService pool = Executors.newFixedThreadPool(size);

		//计数器
		CountDownLatch countDownLatch = new CountDownLatch(size);

		//将处理任务加入线程池
		mediaProcessList.forEach(mediaProcess -> pool.execute(() -> {
			Long taskId = mediaProcess.getId();

			RLock lock = redissonClient.getLock(VIDEO_JOB_HANDLE_KEY + taskId);
			boolean flag = lock.tryLock();
			if (!flag) {
				return;
			}

			log.info("开始执行任务:{}", mediaProcess.getId());

			//发送延时消息
			rabbitTemplate.convertAndSend(
				MQConstant.DELAY_EXCHANGE_NAME,
				MQConstant.DELAY_VIDEO_KEY,
				mediaProcess.getId(),
				message -> {
					message.getMessageProperties().setDelay(1800000);//这里设置成30分钟
					return message;
				}
			);

			String bucket = mediaProcess.getBucket();
			String filePath = mediaProcess.getFilePath();
			String fileId = mediaProcess.getFileId(); //原始视频的md5值

			/*下载待处理文件到服务器*/
			File originalFile = mediaFileService.getFile(bucket, filePath);
			if (originalFile == null) {
				log.debug("下载待处理文件失败");
				mediaProcessService.saveProcessFinishStatus(mediaProcess.getId(), "3", fileId, null, "下载待处理文件失败");
				return;
			}

			File mp4File;
			try {
				mp4File = File.createTempFile("mp4", "mp4");
			} catch (IOException exception) {
				log.error("创建mp4临时文件失败");
				mediaProcessService.saveProcessFinishStatus(mediaProcess.getId(), "3", fileId, null, "创建mp4临时文件失败");
				return;
			}

			//视频处理结果
			String result = "";
			try {
				Mp4VideoUtil videoUtil = new Mp4VideoUtil(
					ffmpegPath,
					originalFile.getAbsolutePath(),
					mp4File.getName(),
					mp4File.getAbsolutePath());
				result = videoUtil.generateMp4();
			} catch (Exception e) {
				log.error("处理视频文件出错:{}-{}", mediaProcess.getFilePath(), e.getMessage());
			}

			if (!result.equals("success")) {
				log.error("处理视频失败:{}-{}", bucket + filePath, result);
				mediaProcessService.saveProcessFinishStatus(mediaProcess.getId(), "3", fileId, null, result);
				return;
			}

			/*将处理后的视频上传至minio*/
			String objectName = fileUtil.getFilePathByMd5(fileId, ".mp4");
			String url = "/" + bucket + "/" + objectName;

			try {
				minioUtil.uploadFile(bucket, objectName, originalFile.getAbsolutePath());
				mediaProcessService.saveProcessFinishStatus(mediaProcess.getId(), "2", fileId, url, null);
			} catch (Exception exception) {
				log.error("处理视频失败:{}-{}", bucket + objectName, exception.getMessage());
				mediaProcessService.saveProcessFinishStatus(mediaProcess.getId(), "3", fileId, null, "处理后视频上传或入库失败");
			} finally {
				countDownLatch.countDown();
				lock.unlock();
			}
		}));

		//等待,给一个充裕的超时时间,防止无限等待，到达超时时间还没有处理完成则结束任务
		countDownLatch.await(30, TimeUnit.MINUTES);
	}

	@RabbitListener(queues = MQConstant.DELAY_QUEUE_NAME)
	public void timeoutProcessHandler(Long mediaProcessId) {
		MediaProcess mediaProcess = mediaProcessService.getOne(mediaProcessId);
		mediaProcessService.saveProcessFinishStatus(
			mediaProcess.getId(),
			"3",
			mediaProcess.getFileId(),
			null,
			"处理文件超时");
	}
}
