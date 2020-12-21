package com.challenge.campsitereservationapi.exception;

public class InactiveBookingException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InactiveBookingException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public InactiveBookingException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public InactiveBookingException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public InactiveBookingException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public InactiveBookingException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
