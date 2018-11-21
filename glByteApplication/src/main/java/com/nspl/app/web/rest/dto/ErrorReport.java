package com.nspl.app.web.rest.dto;

import java.util.List;

public class ErrorReport {
	
	private String taskName;
	private String taskStatus;
	private List<ErrorReport> subTasksList;
	private String details;
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public List<ErrorReport> getSubTasksList() {
		return subTasksList;
	}
	public void setSubTasksList(List<ErrorReport> subTasksList) {
		this.subTasksList = subTasksList;
	}
	
	@Override
	public String toString() {
		return "ErrorReport [taskName=" + taskName + ", taskStatus="
				+ taskStatus + ", subTasksList=" + subTasksList + ", details="
				+ details + "]";
	}
	
	
	
	

}
