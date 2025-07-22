package study.online.content.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 课程预览模型
 */
@Data
@Accessors(chain = true)
public class CoursePreviewDTO {

	/*课程基本信息*/
	private CourseBaseInfoDTO courseBase;

	/*课程计划信息*/
	private List<TeachplanDTO> teachplans;
}
