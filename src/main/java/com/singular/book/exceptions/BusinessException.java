package com.singular.book.exceptions;

public class BusinessException extends Exception {

	private static final long serialVersionUID = 4249113268424359541L;

	private String message;

	public BusinessException(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
