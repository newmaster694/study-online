package com.study.online.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

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
@Accessors(chain = true)
public class QueryCourseParamsDTO {
	/*课程名称*/
	private String courseName;
	
	/*审核状态*/
	private String auditStatus;
	
	/*发布状态*/
	private String publishStatus;
}
