package study.online.media.service;

import study.online.media.model.po.MediaProcess;

import java.util.List;

public interface IMediaProcessService {

	/**
	 * 获取待处理任务
	 *
	 * @param shardIndex 分片序号
	 * @param shardTotal 分片总数
	 * @param count      任务数
	 * @return {@code List<MediaProcess>}
	 */
	List<MediaProcess> getMediaProcessList(int shardIndex, int shardTotal, int count);

	/**
	 * 保存任务结果
	 *
	 * @param taskId 任务id
	 * @param status 任务状态
	 * @param fileId 文件id
	 * @param url    文件外链
	 * @param errmsg 错误信息
	 */
	void saveProcessFinishStatus(Long taskId, String status, String fileId, String url, String errmsg);

	/**
	 * 更新任务的开始时间
	 *
	 * @param mediaProcess 视频处理任务
	 */
	void startVideoTask(MediaProcess mediaProcess);

	/**
	 * 获取超时任务接口
	 *
	 * @return {@code List<MediaProcess>}
	 */
	List<MediaProcess> getTimeoutProcessList();
}
