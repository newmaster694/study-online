package study.online.media.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.online.media.mapper.MediaProcessMapper;
import study.online.media.model.po.MediaProcess;
import study.online.media.service.IMediaProcessService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MediaProcessServiceImpl implements IMediaProcessService {

	private final MediaProcessMapper mediaProcessMapper;

	@Override
	public List<MediaProcess> getMediaProcessList(int shardIndex, int shardTotal, int count) {
		return mediaProcessMapper.selectListByShardIndex(shardTotal, shardIndex, count);
	}
}
