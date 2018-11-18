package com.ch.dataclean.common.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

public class KettleDcException extends RuntimeException {

	static final long serialVersionUID = 1662900257135756746L;

	public KettleDcException() {
		super();
	}

	public KettleDcException(String message) {
		super(message);
	}

	public KettleDcException(String message, Throwable cause) {
		super(message, cause);
	}

	public KettleDcException(Throwable cause) {
		super(cause);
	}

	/**
	 * 将CheckedException转换为UncheckedException.
	 */
	public static RuntimeException unchecked(Exception e) {
		if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		} else {
			return new RuntimeException(e);
		}
	}

	/**
	 * 将ErrorStack转化为String.
	 */
	public static String getStackTraceAsString(Exception e) {
		StringWriter stringWriter = new StringWriter();
		e.printStackTrace(new PrintWriter(stringWriter));
		return stringWriter.toString();
	}

	/**
	 * 判断异常是否由某些底层的异常引起.
	 */
	public static boolean isCausedBy(Exception ex,
			@SuppressWarnings("unchecked") Class<? extends Exception>... causeExceptionClasses) {
		Throwable cause = ex.getCause();
		while (cause != null) {
			for (Class<? extends Exception> causeClass : causeExceptionClasses) {
				if (causeClass.isInstance(cause)) {
					return true;
				}
			}
			cause = cause.getCause();
		}
		return false;
	}
}