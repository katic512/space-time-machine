package com.gamesys.timetravel.spacetimemachine.exception;

import java.time.LocalDateTime;

public class ExceptionDetail {
	private LocalDateTime timestamp;
	private String message;

	public ExceptionDetail(LocalDateTime timestamp, String message) {
		super();
		this.timestamp = timestamp;
		this.message = message;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
