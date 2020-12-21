package com.challenge.campsitereservationapi.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class ReservationNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ReservationNotFoundException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ReservationNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public ReservationNotFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ReservationNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ReservationNotFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
 
}
