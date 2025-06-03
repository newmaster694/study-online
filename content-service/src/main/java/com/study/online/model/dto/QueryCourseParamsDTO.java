package com.study.online.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * <p>课程查询参数DTO</p>
 *
 * @author newmaster
 * @version 1.0
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QueryCourseParamsDTO {
	/*审核状态*/
	private String auditStatus;
	
	/*课程名称*/
	private String courseName;
	
	/*发布状态*/
	private String publishStatus;
}
