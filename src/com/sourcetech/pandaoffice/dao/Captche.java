package com.sourcetech.pandaoffice.dao;

import java.io.Serializable;
import java.sql.Timestamp;

public class Captche implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6410992305163626220L;
	
	public static final int CAPTCHE_REQUEST_OK = 0x00;
	public static final int CAPTCHE_REQUEST_ERROR = -0x01;
	
	public static final int CAPTCHE_TYPE_REG_MEMBER = 0X01;

	private int id;
	private String captche;
	private Timestamp invalidDateTime;
	private int captcheType;
	private String phoneNumber;
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public int getCaptcheType() {
		return captcheType;
	}

	public void setCaptcheType(int captcheType) {
		this.captcheType = captcheType;
	}

	public String getCaptche() {
		return captche;
	}

	public void setCaptche(String captche) {
		this.captche = captche;
	}

	public Timestamp getInvalidDateTime() {
		return invalidDateTime;
	}

	public void setInvalidDateTime(Timestamp invalidDateTime) {
		this.invalidDateTime = invalidDateTime;
	}

}