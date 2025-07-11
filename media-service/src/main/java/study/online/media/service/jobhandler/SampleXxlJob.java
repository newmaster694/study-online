package study.online.media.service.jobhandler;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

@Component
public class SampleXxlJob {

	/*分片广播任务*/
	@XxlJob("shardingJobHandler")
	public void shardingJobHandler() {
		/*分片参数*/
		int shardIndex = XxlJobHelper.getShardIndex();
		int shardTotal = XxlJobHelper.getShardTotal();

		XxlJobHelper.log("分片参数:当前分片序号-{};总分片数-{}", shardIndex, shardTotal);

		/*业务逻辑*/
		for (int i = 0; i < shardTotal; i++) {
			if (i == shardIndex) {
				XxlJobHelper.log("第{}片,命中分片开始处理", i);
			} else {
				XxlJobHelper.log("第{}片,忽略", i);
			}
		}
	}
}
