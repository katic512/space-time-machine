package com.gamesys.timetravel.spacetimemachine.exception;

public class DataNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private String message;
	
	public DataNotFoundException(String msg) {
		super(msg);
		this.message = msg;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
