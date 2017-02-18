package com.sourcetech.pandaoffice.dao;

import java.io.Serializable;

public class File implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7767715298894355946L;
	
	private String id;
	private String md5;
	private String path;
	private String type;
	private String name;
	private double size;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMd5() {
		return md5;
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public double getSize() {
		return size;
	}
	public void setSize(double size) {
		this.size = size;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
