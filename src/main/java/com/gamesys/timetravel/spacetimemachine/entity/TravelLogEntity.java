package com.gamesys.timetravel.spacetimemachine.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gamesys.timetravel.spacetimemachine.model.TravelLog;
/**
 * 
 * @author Karthick Narasimhan
 *
 */
@Table(name="TRAVEL_LOG")
@Entity
public class TravelLogEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name="PGI")
	private String pgi;
	
	@Column(name="PLACE")
	private String place;
	
	@Column(name="DATE")
	private Date date;

	public TravelLogEntity() {
		super();
	}

	public TravelLogEntity(String pgi, String place, Date date) {
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

	public TravelLog convertToTravelLog() {
		return new TravelLog(this.pgi, this.place, this.date);
	}
}
