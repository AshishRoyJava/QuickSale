package com.quicksale.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Custom application exception class
 * 
 * @author ashishr
 *
 */
public class APIException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private final String errorMessage;
	private final HttpStatus status;

	public APIException(String message, HttpStatus status) {
		this.errorMessage = message;
		this.status = status;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public HttpStatus getStatus() {
		return status;
	}
}
