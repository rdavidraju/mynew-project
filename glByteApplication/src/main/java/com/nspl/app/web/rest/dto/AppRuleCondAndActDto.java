package com.nspl.app.web.rest.dto;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

import com.nspl.app.domain.AppRuleConditions;
import com.nspl.app.web.rest.AppRuleCondDto;

public class AppRuleCondAndActDto {
	
	private Long id;
	private String ruleCode;
	private Integer priority ;
	private Boolean editRule;
	private ZonedDateTime startDate;
	private ZonedDateTime endDate;
	private String sourceDataViewName;
	private String sourceDataViewId;
	private Boolean enabledFlag;
	private Boolean assignmentFlag;
	private Long tenantId;
	private Long createdBy;
	private Long lastUpdatedBy;
	private String approvalNeededType;
	private String approvalNeededTypeMeaning;
	private Long ruleGroupAssignId;
	private ZonedDateTime createdDate;
	private ZonedDateTime lastUpdatedDate;
	private List<AppRuleCondDto> approvalConditions;
	private ApprovalActionDto approvalActions;
	
	public String getApprovalNeededType() {
		return approvalNeededType;
	}
	public void setApprovalNeededType(String approvalNeededType) {
		this.approvalNeededType = approvalNeededType;
	}
	public Boolean getEditRule() {
		return editRule;
	}
	public void setEditRule(Boolean editRule) {
		this.editRule = editRule;
	}
	public Long getRuleGroupAssignId() {
		return ruleGroupAssignId;
	}
	public void setRuleGroupAssignId(Long ruleGroupAssignId) {
		this.ruleGroupAssignId = ruleGroupAssignId;
	}
	public String getApprovalNeededTypeMeaning() {
		return approvalNeededTypeMeaning;
	}
	public void setApprovalNeededTypeMeaning(String approvalNeededTypeMeaning) {
		this.approvalNeededTypeMeaning = approvalNeededTypeMeaning;
	}
	public Boolean getAssignmentFlag() {
		return assignmentFlag;
	}
	public void setAssignmentFlag(Boolean assignmentFlag) {
		this.assignmentFlag = assignmentFlag;
	}

	public Long getTenantId() {
		return tenantId;
	}
	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
	public Boolean getEnabledFlag() {
		return enabledFlag;
	}
	public void setEnabledFlag(Boolean enabledFlag) {
		this.enabledFlag = enabledFlag;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRuleCode() {
		return ruleCode;
	}
	public void setRuleCode(String ruleCode) {
		this.ruleCode = ruleCode;
	}
	public ZonedDateTime getStartDate() {
		return startDate;
	}
	public void setStartDate(ZonedDateTime startDate) {
		this.startDate = startDate;
	}
	public ZonedDateTime getEndDate() {
		return endDate;
	}
	public void setEndDate(ZonedDateTime endDate) {
		this.endDate = endDate;
	}
	public String getSourceDataViewName() {
		return sourceDataViewName;
	}
	public void setSourceDataViewName(String sourceDataViewName) {
		this.sourceDataViewName = sourceDataViewName;
	}
	public String getSourceDataViewId() {
		return sourceDataViewId;
	}
	public void setSourceDataViewId(String sourceDataViewId) {
		this.sourceDataViewId = sourceDataViewId;
	}
	public Long getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}
	public Long getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(Long lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	public ZonedDateTime getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(ZonedDateTime createdDate) {
		this.createdDate = createdDate;
	}
	public ZonedDateTime getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	public void setLastUpdatedDate(ZonedDateTime lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
	public List<AppRuleCondDto> getApprovalConditions() {
		return approvalConditions;
	}
	public void setApprovalConditions(List<AppRuleCondDto> approvalConditions) {
		this.approvalConditions = approvalConditions;
	}
	public ApprovalActionDto getApprovalActions() {
		return approvalActions;
	}
	public void setApprovalActions(ApprovalActionDto approvalActions) {
		this.approvalActions = approvalActions;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	
	
	

}
