package study.online.base.constant;

import lombok.Getter;

@Getter
public enum CommonErrrorEnum {
	UNKNOW_ERROR("执行过程异常，请重试！"),
	PARAMS_ERROR("非法参数"),
	OBJECT_NULL("对象为空"),
	QUERY_NULL("查询结果为空"),
	REQUEST_NULL("必要请求参数为空");

	private final String errMessage;

	CommonErrrorEnum(String errMessage) {
		this.errMessage = errMessage;
	}
}
