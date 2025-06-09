package study.online.content.model.dto;

import study.online.content.model.po.Teachplan;
import study.online.content.model.po.TeachplanMedia;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * <p>课程计划树形结构DTO</p>
 *
 * @since 2026/6/9
 * @author newmaster
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class TeachplanDTO extends Teachplan {
	/*课程计划关联的媒体资料信息*/
	TeachplanMedia teachplanMedia;

	/*子节点*/
	List<TeachplanDTO> teachplanTreeNodes;
}
