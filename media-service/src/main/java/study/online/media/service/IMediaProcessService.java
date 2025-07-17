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
	 * 根据id获取mediaProcess
	 *
	 * @param id mediaProcessId
	 * @return MediaProcess
	 */
	MediaProcess getOne(Long id);
}
