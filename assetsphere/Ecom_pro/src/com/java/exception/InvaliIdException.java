package com.java.exception;

public class InvaliIdException	 extends Exception {

	private static final long serialVersionUID = 1L;
	private String message;
	public String getMessage() {
		return message;
	}
	public InvaliIdException(String message) {
		super();
		this.message = message;
	}

}
