package study.online.base.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import study.online.base.exception.BaseException;
import study.online.base.model.RestErrorResponse;

import java.util.ArrayList;
import java.util.List;

import static study.online.base.constant.ErrorMessage.UNKNOW_ERROR;

/**
 * 全局异常处理（必须要保证这个类被其他的Spring Boot微服务的运行主类被扫描到才能够正确使用！！）
 */
@Slf4j
@RestControllerAdvice
public class BaseExceptionAdvice {

	/**
	 * <p>捕获业务异常</p>
	 *
	 * @param exception 捕获的异常
	 * @return {@code RestResponse<Object>}
	 */
	@ExceptionHandler(BaseException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public RestErrorResponse bussinessExceptionHandler(BaseException exception) {
		log.error("业务异常-{}", exception.getMessage());
		return new RestErrorResponse(exception.getMessage());
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public RestErrorResponse execption(Exception exception) {
		log.error("未知异常-{}", exception.getMessage());
		return new RestErrorResponse(UNKNOW_ERROR);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public RestErrorResponse methodArgumentNotValidException(MethodArgumentNotValidException exception) {
		BindingResult bindingResult = exception.getBindingResult();
		List<String> msgList = new ArrayList<>();

		//将错误信息放在msgList
		bindingResult.getFieldErrors().forEach(item -> msgList.add(item.getDefaultMessage()));

		//拼接错误信息
		String msg = String.join(";", msgList);
		log.error("参数异常-{}", msg);

		return new RestErrorResponse(msg);
	}

	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public RestErrorResponse exception(Exception e) {

		log.error("【系统异常】-{}", e.getMessage(), e);
		return new RestErrorResponse("没有操作此功能的权限");
	}
}
