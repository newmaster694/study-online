package study.online.media.service.impl;

import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.online.media.mapper.MediaFilesMapper;
import study.online.media.mapper.MediaProcessHistoryMapper;
import study.online.media.mapper.MediaProcessMapper;
import study.online.media.model.po.MediaFiles;
import study.online.media.model.po.MediaProcess;
import study.online.media.model.po.MediaProcessHistory;
import study.online.media.service.IMediaProcessService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MediaProcessServiceImpl implements IMediaProcessService {

	private final MediaProcessMapper mediaProcessMapper;
	private final MediaFilesMapper mediaFilesMapper;
	private final MediaProcessHistoryMapper mediaProcessHistoryMapper;

	@Override
	public List<MediaProcess> getMediaProcessList(int shardIndex, int shardTotal, int count) {
		return mediaProcessMapper.selectListByShardIndex(shardTotal, shardIndex, count);
	}

	@Override
	@Transactional
	public void saveProcessFinishStatus(Long taskId, String status, String fileId, String url, String errmsg) {
		//查出任务，如果不存在则直接返回
		MediaProcess mediaProcess = mediaProcessMapper.selectById(taskId);
		if (mediaProcess == null) {
			return;
		}

		//处理失败，更新任务处理结果
		if (status.equals("3")) {
			mediaProcess
				.setStatus("3")
				.setErrormsg(errmsg)
				.setFailCount(mediaProcess.getFailCount() + 1);

			mediaProcessMapper.updateById(mediaProcess);
			log.debug("更新任务处理状态为失败,任务信息:{}", mediaProcess.getId());
			return;
		}

		//任务处理成功
		MediaFiles mediaFiles = mediaFilesMapper.selectById(fileId);
		if (mediaFiles != null) {
			//更新媒资文件中的访问url
			mediaFilesMapper.updateById(mediaFiles.setUrl(url));
		}

		//处理成功，更新mediaProcess中的url与状态
		mediaProcess
			.setUrl(url)
			.setStatus("2")
			.setFinishDate(LocalDateTime.now());

		mediaProcessMapper.updateById(mediaProcess);

		//添加到历史记录
		MediaProcessHistory mediaProcessHistory = new MediaProcessHistory();
		BeanUtil.copyProperties(mediaProcess, mediaProcessHistory, true);
		mediaProcessHistoryMapper.insert(mediaProcessHistory);

		//删除mediaProcess
		mediaProcessMapper.deleteById(mediaProcess.getId());
	}

	@Override
	public void startVideoTask(MediaProcess mediaProcess) {
		mediaProcessMapper.updateById(mediaProcess);
	}

	@Override
	public List<MediaProcess> getTimeoutProcessList() {
		return mediaProcessMapper.selectTimeout();
	}
}
