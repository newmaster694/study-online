package study.online.content.model.dto;

import lombok.Data;

@Data
public class BindTeachPlanMediaDTO {
	/*媒资文件id*/
	private String mediaId;

	/*媒资文件名*/
	private String fileName;

	/*课程计划标识*/
	private Long teachPlanId;
}
