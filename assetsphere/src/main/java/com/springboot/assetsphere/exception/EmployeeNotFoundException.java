package com.springboot.assetsphere.exception;

public class EmployeeNotFoundException  extends Exception {

	
private static final long serialVersionUID = 1L;
	
	private  String message;

	public EmployeeNotFoundException(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	
}
