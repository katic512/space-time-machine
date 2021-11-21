package com.gamesys.timetravel.spacetimemachine.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gamesys.timetravel.spacetimemachine.model.Traveller;

@Table(name="TRAVELLER")
@Entity
public class TravellerEntity {

	@Id
	@Column(name="PGI")
	private String pgi;
	
	@Column(name="NAME")
	private String name;
	
	@Column(name="EMAIL_ID")
	private String emailId;
	
	public String getPgi() {
		return pgi;
	}
	public void setPgi(String pgi) {
		this.pgi = pgi;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public TravellerEntity() {
		super();
	}
	
	public TravellerEntity(String pgi, String name, String emailId) {
		super();
		this.pgi = pgi;
		this.name = name;
		this.emailId = emailId;
	}

	public Traveller convertToTraveller()  {
		return new Traveller(this.pgi, this.name, this.emailId);
	}
}
