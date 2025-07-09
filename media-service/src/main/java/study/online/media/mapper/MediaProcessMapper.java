package study.online.media.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import study.online.media.model.po.MediaProcess;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author newmaster
 * @since 2025-06-14
 */
@Mapper
public interface MediaProcessMapper extends BaseMapper<MediaProcess> {

	/**
	 * 根据分片参数获取待处理任务
	 *
	 * @param shardTotal 分片总数
	 * @param shardIndex 分片序号
	 * @param count      任务数
	 * @return {@code List<MediaProcess>}
	 */
	@Select("select * from media_process mp " +
		"where mp.id % #{shardTotal} = #{shardIndex}" +
		"and (mp.status='1' or mp.status='3')" +
		"and mp.fail_count < 3 " +
		"limit #{count}")
	List<MediaProcess> selectListByShardIndex(
		@Param("shardTotal") int shardTotal,
		@Param("shardIndex") int shardIndex,
		@Param("count") int count
	);
}
