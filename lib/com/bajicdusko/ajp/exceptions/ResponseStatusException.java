package com.bajicdusko.ajp.exceptions;

public class ResponseStatusException extends Exception {

	public ResponseStatusException(int statusCode) {
		super(String.valueOf(statusCode));
	}
    public ResponseStatusException(String message) { super(message); }
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
