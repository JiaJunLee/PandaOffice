package com.sourcetech.pandaoffice.dao;

import java.io.Serializable;
import java.sql.Timestamp;

public class MemberInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7947699920321013687L;
	private int userId;
	private String userIconFileId;
	private Timestamp userCreateDateTime;
	private String userName;
	private int userSex;
	private Timestamp userBirth;
	private String userJob;
	private String userAddress;
	private String userWebsite;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserIconFileId() {
		return userIconFileId;
	}

	public void setUserIconFileId(String userIconFileId) {
		this.userIconFileId = userIconFileId;
	}

	public Timestamp getUserCreateDateTime() {
		return userCreateDateTime;
	}

	public void setUserCreateDateTime(Timestamp userCreateDateTime) {
		this.userCreateDateTime = userCreateDateTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getUserSex() {
		return userSex;
	}

	public void setUserSex(int userSex) {
		this.userSex = userSex;
	}

	public Timestamp getUserBirth() {
		return userBirth;
	}

	public void setUserBirth(Timestamp userBirth) {
		this.userBirth = userBirth;
	}

	public String getUserJob() {
		return userJob;
	}

	public void setUserJob(String userJob) {
		this.userJob = userJob;
	}

	public String getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}

	public String getUserWebsite() {
		return userWebsite;
	}

	public void setUserWebsite(String userWebsite) {
		this.userWebsite = userWebsite;
	}

}
