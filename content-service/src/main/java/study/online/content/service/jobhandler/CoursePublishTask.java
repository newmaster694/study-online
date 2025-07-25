package study.online.content.service.jobhandler;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import study.online.messagesdk.model.po.MqMessage;
import study.online.messagesdk.service.MessageProcessAbstract;
import study.online.messagesdk.service.MqMessageService;

@Slf4j
@Component
public class CoursePublishTask extends MessageProcessAbstract {

	public CoursePublishTask(MqMessageService mqMessageService) {
		super(mqMessageService);
	}

	/*任务调度入口*/
	@XxlJob("course-publish-job-handler")
	public void coursePublishJobHandler() {
		int shardIndex = XxlJobHelper.getShardIndex();
		int shardTotal = XxlJobHelper.getShardTotal();

		this.process(shardIndex, shardTotal, "course_publish", 30, 60);


	}

	@Override
	public boolean execute(MqMessage mqMessage) {
		//从MqMessage拿到课程id
		long courseId = Long.parseLong(mqMessage.getBusinessKey1());

		//课程静态化上传到minio
		this.generateCourseHtml(mqMessage, courseId);

		//向elasticsearch写索引数据
		this.saveCourseIndex(mqMessage, courseId);

		//向Redis写缓存

		//返回true表示任务完成
		return true;
	}

	/**
	 * 生成课程静态化页面，并上传至文件系统
	 *
	 * @param mqMessage 消息
	 * @param courseId  课程id
	 */
	private void generateCourseHtml(MqMessage mqMessage, Long courseId) {
		Long taskId = mqMessage.getId();

		//查询数据库取出该阶段执行状态
		MqMessageService mqMessageService = this.getMqMessageService();
		int stageOne = mqMessageService.getStageOne(taskId);
		if (stageOne > 0) {//完成
			log.info("课程静态化任务完成-{}", taskId);
			return;
		}

		//TODO 开始进行课程静态化处理

		mqMessageService.completedStageOne(taskId);
	}

	/**
	 * 保存课程索引信息到elasticsearch
	 *
	 * @param mqMessage 任务消息
	 * @param courseId  课程id
	 */
	private void saveCourseIndex(MqMessage mqMessage, Long courseId) {
		Long taskId = mqMessage.getId();

		MqMessageService mqMessageService = this.getMqMessageService();
		int stageTwo = mqMessageService.getStageTwo(taskId);

		if (stageTwo > 0) {
			log.info("课程的索引信息已写入-{}", taskId);
			return;
		}

		//TODO 查询课程信息，调用搜索服务添加索引

		mqMessageService.completedStageTwo(taskId);
	}
}
