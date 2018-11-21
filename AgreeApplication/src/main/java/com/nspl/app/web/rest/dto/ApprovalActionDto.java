package com.nspl.app.web.rest.dto;

import java.util.List;

import com.nspl.app.domain.ApprovalRuleAssignment;

public class ApprovalActionDto {
	public String getAssigneeType() {
		return assigneeType;
	}
	public void setAssigneeType(String assigneeType) {
		this.assigneeType = assigneeType;
	}
	public List<ApprRuleAssgnDto> getActionDetails() {
		return actionDetails;
	}
	public void setActionDetails(List<ApprRuleAssgnDto> actionDetails) {
		this.actionDetails = actionDetails;
	}
	
	private String assigneeType;
	private List<ApprRuleAssgnDto> actionDetails;
	

}
