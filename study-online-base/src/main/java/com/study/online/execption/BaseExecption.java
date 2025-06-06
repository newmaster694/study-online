package com.study.online.execption;

public class BaseExecption extends RuntimeException {
	public BaseExecption() {}

	public BaseExecption(String message) {
		super(message);
	}
}
