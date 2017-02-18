package com.sourcetech.pandaoffice.dao;

import java.io.Serializable;
import java.sql.Timestamp;

public class MemberAbnormalRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1893285923731498115L;
	private int userId;
	private Timestamp engenderDateTime;
	private Timestamp invalidDateTime;
	private int abnormalCode;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Timestamp getEngenderDateTime() {
		return engenderDateTime;
	}

	public void setEngenderDateTime(Timestamp engenderDateTime) {
		this.engenderDateTime = engenderDateTime;
	}

	public Timestamp getInvalidDateTime() {
		return invalidDateTime;
	}

	public void setInvalidDateTime(Timestamp invalidDateTime) {
		this.invalidDateTime = invalidDateTime;
	}

	public int getAbnormalCode() {
		return abnormalCode;
	}

	public void setAbnormalCode(int abnormalCode) {
		this.abnormalCode = abnormalCode;
	}

}
