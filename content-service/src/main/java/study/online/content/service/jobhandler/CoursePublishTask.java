package study.online.content.service.jobhandler;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import study.online.base.utils.MinioUtil;
import study.online.messagesdk.model.po.MqMessage;
import study.online.messagesdk.service.MessageProcessAbstract;
import study.online.messagesdk.service.MqMessageService;

@Slf4j
@Component
public class CoursePublishTask extends MessageProcessAbstract {

	@Resource
	private MinioUtil minioUtil;

	public CoursePublishTask(MqMessageService mqMessageService) {
		super(mqMessageService);
	}

	@Override
	public boolean execute(MqMessage mqMessage) {
		//从MqMessage拿到课程id
		long courseId = Long.parseLong(mqMessage.getBusinessKey1());

		//向elasticsearch写索引数据

		//向Redis写缓存

		//课程静态化上传到minio
		this.generateCourseHtml(mqMessage, courseId);

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
	}
}
