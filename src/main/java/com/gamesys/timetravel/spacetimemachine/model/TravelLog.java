package com.gamesys.timetravel.spacetimemachine.model;

import java.util.Date;

import javax.validation.constraints.NotEmpty;

import org.springframework.lang.NonNull;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TravelLog {

	@NotEmpty(message = "PGI cannot be empty")
	private String pgi;
	@NotEmpty(message = "Place cannot be empty")
	private String place;
	@NonNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date date;

	public TravelLog() {
		super();
	}

	public TravelLog(String pgi, String place, Date date) {
		super();
		this.pgi = pgi;
		this.place = place;
		this.date = date;
	}

	public String getPgi() {
		return pgi;
	}

	public void setPgi(String pgi) {
		this.pgi = pgi;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
}
