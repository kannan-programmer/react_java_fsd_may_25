package com.java.exception;

public class InvalidCouponException extends Exception {

	private static final long serialVersionUID = 1L;
	private String message;
	public String getMessage() {
		return message;
	}
	public InvalidCouponException(String message) {
		super();
		this.message = message;
	}
}
