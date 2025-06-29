package com.springboot.assetsphere.exception;

public class AssetNotFoundException extends Exception {

private static final long serialVersionUID = 1L;
	
	private  String message;

	public AssetNotFoundException(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
