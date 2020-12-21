package com.challenge.campsitereservationapi.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class DateNotAvailableException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DateNotAvailableException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DateNotAvailableException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public DateNotAvailableException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public DateNotAvailableException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public DateNotAvailableException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
