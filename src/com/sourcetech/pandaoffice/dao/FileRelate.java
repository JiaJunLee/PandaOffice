package com.sourcetech.pandaoffice.dao;

import java.io.Serializable;
import java.sql.Timestamp;

public class FileRelate implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8216747173631797103L;
	private Integer shareId;
	private String fileId;
	private Integer userId;
	private Timestamp updateDateTime;
	public Integer getShareId() {
		return shareId;
	}
	public void setShareId(Integer shareId) {
		this.shareId = shareId;
	}
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Timestamp getUpdateDateTime() {
		return updateDateTime;
	}
	public void setUpdateDateTime(Timestamp updateDateTime) {
		this.updateDateTime = updateDateTime;
	}
	
	

}
