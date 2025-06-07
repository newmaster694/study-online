package com.study.online.base.enums;

import lombok.Getter;

@Getter
public enum CommonErrror {
	UNKNOW_ERROR("执行过程异常，请重试！"),
	PARAMS_ERROR("非法参数"),
	object_null("对象为空"),
	QUERY_NULL("查询结果为空"),
	REQUEST_NULL("请求参数为空");

	private final String errMessage;

	CommonErrror(String errMessage) {
		this.errMessage = errMessage;
	}
}
