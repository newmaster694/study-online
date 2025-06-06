package com.study.online.base.advice;

import com.study.online.base.execption.BaseExecption;
import com.study.online.base.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 */
@RestControllerAdvice
@Slf4j
public class BaseExecptionAdvice {

	/**
	 * 捕获业务异常
	 *
	 * @param execption 捕获的异常
	 * @return 通用返回接口Result.error()
	 */
	@ExceptionHandler
	public Result<String> bussinessExecptionHandler(BaseExecption execption) {
		log.error("异常信息：{}", execption.getMessage());
		return Result.error(execption.getMessage());
	}
}
