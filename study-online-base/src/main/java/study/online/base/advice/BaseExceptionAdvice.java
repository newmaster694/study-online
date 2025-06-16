package study.online.base.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import study.online.base.exception.BaseException;
import study.online.base.model.RestResponse;

import java.util.ArrayList;
import java.util.List;

import static study.online.base.constant.ErrorMessageConstant.UNKNOW_ERROR;

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
	 * @return {@code RestResponse<Object>}
	 */
	@ExceptionHandler(BaseException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public RestResponse<Object> bussinessExceptionHandler(BaseException exception) {
		log.error("异常信息：{}", exception.getMessage());
		return RestResponse.validFail(exception.getMessage());
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public RestResponse<Object> execption(Exception exception) {
		log.error("异常信息：{}", exception.getMessage());
		return RestResponse.validFail(UNKNOW_ERROR);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public RestResponse<Object> methodArgumentNotValidException(MethodArgumentNotValidException exception) {
		BindingResult bindingResult = exception.getBindingResult();
		List<String> msgList = new ArrayList<>();

		//将错误信息放在msgList
		bindingResult.getFieldErrors().forEach(item -> msgList.add(item.getDefaultMessage()));

		//拼接错误信息
		String msg = String.join(";", msgList);
		log.error("异常信息：{}", msg);

		return RestResponse.validFail(msg);
	}
}
