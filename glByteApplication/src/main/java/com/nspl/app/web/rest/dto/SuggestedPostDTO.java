package com.nspl.app.web.rest.dto;

import java.math.BigInteger;

public class SuggestedPostDTO {
	
	private Long rowId;
	private Long ruleId;
	private String jobReference;
	private String reconReference;
	
	public Long getRowId() {
		return rowId;
	}
	public void setRowId(Long rowId) {
		this.rowId = rowId;
	}
	public Long getRuleId() {
		return ruleId;
	}
	public void setRuleId(Long ruleId) {
		this.ruleId = ruleId;
	}
	public String getJobReference() {
		return jobReference;
	}
	public void setJobReference(String jobReference) {
		this.jobReference = jobReference;
	}
	public String getReconReference() {
		return reconReference;
	}
	public void setReconReference(String reconReference) {
		this.reconReference = reconReference;
	}
	
}
