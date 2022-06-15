package com.gamesys.timetravel.spacetimemachine.exception;
/**
 * 
 * @author Karthick Narasimhan
 *
 */
public class DuplicateDataException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private String message;
	
	public DuplicateDataException(String msg) {
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
