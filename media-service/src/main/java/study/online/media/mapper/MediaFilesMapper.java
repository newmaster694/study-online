package study.online.media.mapper;

import org.apache.ibatis.annotations.Mapper;
import study.online.media.model.po.MediaFiles;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 媒资信息 Mapper 接口
 *
 * @author newmaster
 * @since 2025-06-14
 */
@Mapper
public interface MediaFilesMapper extends BaseMapper<MediaFiles> {

}
