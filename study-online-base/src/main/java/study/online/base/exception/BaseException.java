package study.online.base.exception;

import study.online.base.constant.CommonErrrorEnum;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
	private final String errMessage;

	public BaseException(String message) {
		super(message);
		this.errMessage = message;
	}

	public static void cast(CommonErrrorEnum commonErrrorEnum) {
		throw new BaseException(commonErrrorEnum.getErrMessage());
	}

	public static void cast(String errMessage) {
		throw new BaseException(errMessage);
	}
}
