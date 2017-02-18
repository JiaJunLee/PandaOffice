package com.sourcetech.pandaoffice.dao;

import java.io.Serializable;
import java.sql.Timestamp;

public class MemberLoginLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4384426929833396345L;
	private int userId;
	private Timestamp loginDateTime;
	private String loginIp;
	private String loginMac;
	private int loginClientType;
	private int loginResult;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Timestamp getLoginDateTime() {
		return loginDateTime;
	}

	public void setLoginDateTime(Timestamp loginDateTime) {
		this.loginDateTime = loginDateTime;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public String getLoginMac() {
		return loginMac;
	}

	public void setLoginMac(String loginMac) {
		this.loginMac = loginMac;
	}

	public int getLoginClientType() {
		return loginClientType;
	}

	public void setLoginClientType(int loginClientType) {
		this.loginClientType = loginClientType;
	}

	public int getLoginResult() {
		return loginResult;
	}

	public void setLoginResult(int loginResult) {
		this.loginResult = loginResult;
	}

}
