package study.online.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import study.online.system.model.po.Dictionary;


/**
 * 数据字典 Mapper 接口
 *
 * @author newmaster
 */
@Mapper
public interface DictionaryMapper extends BaseMapper<Dictionary> {

}
