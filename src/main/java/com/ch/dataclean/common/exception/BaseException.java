package com.ch.dataclean.common.exception;

public class BaseException extends Exception{

	private final long serialVersionUID = 1L;

	private String message;

	public BaseException(String message) {
		super(message);
		this.message = message;
	}

	public String getMessage(){
		return this.message;
	}

	/**
	 * 重写printStackTrace方法
	 */
	@Override
	public void printStackTrace() {
		StringBuilder sb = new StringBuilder();
		sb.append("*****系统自定义异常start*****\r\n")
				.append("msg:")
				.append(this.message)
				.append("\n*****系统自定义异常end*****");
		super.printStackTrace();
	}
}
