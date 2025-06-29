package com.java.exception;

public class InvalidCategory extends Exception{

	private static final long serialVersionUID = 1L;
	private String message;
	public String getMessage() {
		return message;
	}
	public InvalidCategory(String message) {
		super();
		this.message = message;
	}

}
