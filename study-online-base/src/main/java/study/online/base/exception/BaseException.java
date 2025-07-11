package study.online.base.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
	private final String errMessage;

	public BaseException(String message) {
		super(message);
		this.errMessage = message;
	}

	public static void cast(String errMessage) {
		throw new BaseException(errMessage);
	}
}
