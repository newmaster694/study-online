package com.study.online.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 分页查询参数
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class PageParams {
	/*当前页码*/
	private Long pageNo = 1L;
	
	/*每页记录数*/
	private Long pageSize = 1L;
}
