package com.sourcetech.pandaoffice.dao;

import java.sql.Timestamp;

public class Task {
	
	private int id;
	private String name;
	private String marks;
	private int rank;
	private Timestamp createDateTime;
	private Timestamp overdueDateTime;
	private int listId;
	private int executeUserId;
	private int createUserId;
	private int childTaskId;
	private int type;
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMarks() {
		return marks;
	}
	public void setMarks(String marks) {
		this.marks = marks;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public Timestamp getCreateDateTime() {
		return createDateTime;
	}
	public void setCreateDateTime(Timestamp createDateTime) {
		this.createDateTime = createDateTime;
	}
	public Timestamp getOverdueDateTime() {
		return overdueDateTime;
	}
	public void setOverdueDateTime(Timestamp overdueDateTime) {
		this.overdueDateTime = overdueDateTime;
	}
	public int getListId() {
		return listId;
	}
	public void setListId(int listId) {
		this.listId = listId;
	}
	public int getExecuteUserId() {
		return executeUserId;
	}
	public void setExecuteUserId(int executeUserId) {
		this.executeUserId = executeUserId;
	}
	public int getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(int createUserId) {
		this.createUserId = createUserId;
	}
	public int getChildTaskId() {
		return childTaskId;
	}
	public void setChildTaskId(int childTaskId) {
		this.childTaskId = childTaskId;
	}
	
	
	

}
