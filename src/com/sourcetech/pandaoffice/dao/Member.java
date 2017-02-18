package com.sourcetech.pandaoffice.dao;

import java.io.Serializable;

public class Member implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8527888304112989709L;
	
	public static final int MEMBER_EXISTS = 0X01;
	public static final int MEMBER_NOT_EXISTS = 0X02;

	public static final int MEMBER_CREATE_SUCCESS = 0x03;
	
	private int userId;
	private String hashPassword;
	private String passwordSalt;
	private String email;
	private String phoneNumber;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getHashPassword() {
		return hashPassword;
	}

	public void setHashPassword(String hashPassword) {
		this.hashPassword = hashPassword;
	}

	public String getPasswordSalt() {
		return passwordSalt;
	}

	public void setPasswordSalt(String passwordSalt) {
		this.passwordSalt = passwordSalt;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

}
