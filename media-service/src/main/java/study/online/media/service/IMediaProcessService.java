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
}
