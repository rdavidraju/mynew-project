package com.nspl.app.web.rest.dto;

public class AWQGroupByParamsDTO {

	private Long ruleId;
	private String batchName;
	private String day;
	private Long columnId;
	private Long viewId;
	private String columnValue;
	private String othersRuleName;
	
	public Long getRuleId() {
		return ruleId;
	}
	public void setRuleId(Long ruleId) {
		this.ruleId = ruleId;
	}
	public String getBatchName() {
		return batchName;
	}
	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public Long getColumnId() {
		return columnId;
	}
	public void setColumnId(Long columnId) {
		this.columnId = columnId;
	}
	public Long getViewId() {
		return viewId;
	}
	public void setViewId(Long viewId) {
		this.viewId = viewId;
	}
	public String getColumnValue() {
		return columnValue;
	}
	public void setColumnValue(String columnValue) {
		this.columnValue = columnValue;
	}
	public String getOthersRuleName() {
		return othersRuleName;
	}
	public void setOthersRuleName(String othersRuleName) {
		this.othersRuleName = othersRuleName;
	}
	
}
