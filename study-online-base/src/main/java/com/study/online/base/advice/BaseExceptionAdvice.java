package com.study.online.base.advice;

import com.study.online.base.enums.CommonErrror;
import com.study.online.base.execption.BaseException;
import com.study.online.base.result.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理（必须要保证这个类被其他的Spring Boot微服务的运行主类被扫描到才能够正确使用！！）
 */
@RestControllerAdvice
@Slf4j
public class BaseExceptionAdvice {

	/**
	 * <p>捕获业务异常</p>
	 *
	 * @param exception 捕获的异常
	 * @return 返回错误响应
	 */
	@ExceptionHandler(BaseException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorResponse bussinessExceptionHandler(BaseException exception) {
		log.error("异常信息：{}", exception.getMessage());
		return new ErrorResponse(exception.getMessage());
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorResponse execption(Exception exception) {
		log.error("异常信息：{}", exception.getMessage());
		return new ErrorResponse(CommonErrror.UNKNOW_ERROR.getErrMessage());
	}
}
