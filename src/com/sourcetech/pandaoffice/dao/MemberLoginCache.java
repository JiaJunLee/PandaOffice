package com.sourcetech.pandaoffice.dao;

import java.io.Serializable;
import java.sql.Timestamp;

public class MemberLoginCache implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8948001629188756164L;
	private int userId;
	private String authorizeSalt;
	private int clientType;
	private Timestamp invalidDateTime;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getAuthorizeSalt() {
		return authorizeSalt;
	}

	public void setAuthorizeSalt(String authorizeSalt) {
		this.authorizeSalt = authorizeSalt;
	}

	public int getClientType() {
		return clientType;
	}

	public void setClientType(int clientType) {
		this.clientType = clientType;
	}

	public Timestamp getInvalidDateTime() {
		return invalidDateTime;
	}

	public void setInvalidDateTime(Timestamp invalidDateTime) {
		this.invalidDateTime = invalidDateTime;
	}

}
