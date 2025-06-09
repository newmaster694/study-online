package study.online.content.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import study.online.content.model.po.CourseAudit;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>审核记录Mapper</p>
 *
 * @author newmaster
 * @since 2025-06-04
 */
@Mapper
public interface CourseAuditMapper extends BaseMapper<CourseAudit> {
}
