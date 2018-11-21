package com.nspl.app.web.rest.dto;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.nspl.app.domain.RuleConditions;
import com.nspl.app.domain.Rules;

public class RuleDTO {

    private Long id;
    private Long tenantId;
    private String ruleCode;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private Boolean enabledFlag;
    private Boolean suggestionFlag;
    private Boolean assignmentFlag;
    private String ruleType;
    private String category;
    private String ruleTypeMeaning;
    private int priority;
    private Long createdBy;
    private Long lastUpdatedBy;
    private ZonedDateTime creationDate;
    private ZonedDateTime lastUpdatedDate;
    private String sourceDataViewId;
    private String targetDataViewId;
    private String sDataViewName;
    private String sDataViewDisplayName;
    private String tDataViewName;
    private String tDataViewDisplayName;
    private Long ruleGroupAssignId;
    private Boolean editRule;
    private String ruleGroupService;
    private List<String> ruletypeLOVArray;
    
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public Long getRuleGroupAssignId() {
		return ruleGroupAssignId;
	}
	public void setRuleGroupAssignId(Long ruleGroupAssignId) {
		this.ruleGroupAssignId = ruleGroupAssignId;
	}
	public String getRuleTypeMeaning() {
		return ruleTypeMeaning;
	}
	public void setRuleTypeMeaning(String ruleTypeMeaning) {
		this.ruleTypeMeaning = ruleTypeMeaning;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getTenantId() {
		return tenantId;
	}
	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
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
	public Boolean getEnabledFlag() {
		return enabledFlag;
	}
	public void setEnabledFlag(Boolean enabledFlag) {
		this.enabledFlag = enabledFlag;
	}
	public Boolean getSuggestionFlag() {
		return suggestionFlag;
	}
	public void setSuggestionFlag(Boolean suggestionFlag) {
		this.suggestionFlag = suggestionFlag;
	}
	public String getRuleType() {
		return ruleType;
	}
	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
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
	public ZonedDateTime getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(ZonedDateTime creationDate) {
		this.creationDate = creationDate;
	}
	public ZonedDateTime getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	public void setLastUpdatedDate(ZonedDateTime lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
	/*public Long getSourceDataViewId() {
		return sourceDataViewId;
	}
	public void setSourceDataViewId(Long sourceDataViewId) {
		this.sourceDataViewId = sourceDataViewId;
	}
	public Long getTargetDataViewId() {
		return targetDataViewId;
	}
	public void setTargetDataViewId(Long targetDataViewId) {
		this.targetDataViewId = targetDataViewId;
	}*/
	public String getsDataViewName() {
		return sDataViewName;
	}
	public void setsDataViewName(String sDataViewName) {
		this.sDataViewName = sDataViewName;
	}
	public String gettDataViewName() {
		return tDataViewName;
	}
	public void settDataViewName(String tDataViewName) {
		this.tDataViewName = tDataViewName;
	}
	public String getsDataViewDisplayName() {
		return sDataViewDisplayName;
	}
	public void setsDataViewDisplayName(String sDataViewDisplayName) {
		this.sDataViewDisplayName = sDataViewDisplayName;
	}
	public String gettDataViewDisplayName() {
		return tDataViewDisplayName;
	}
	public void settDataViewDisplayName(String tDataViewDisplayName) {
		this.tDataViewDisplayName = tDataViewDisplayName;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public Boolean getAssignmentFlag() {
		return assignmentFlag;
	}
	public void setAssignmentFlag(Boolean assignmentFlag) {
		this.assignmentFlag = assignmentFlag;
	}
	public String getSourceDataViewId() {
		return sourceDataViewId;
	}
	public void setSourceDataViewId(String sourceDataViewId) {
		this.sourceDataViewId = sourceDataViewId;
	}
	public String getTargetDataViewId() {
		return targetDataViewId;
	}
	public void setTargetDataViewId(String targetDataViewId) {
		this.targetDataViewId = targetDataViewId;
	}
	public Boolean getEditRule() {
		return editRule;
	}
	public void setEditRule(Boolean editRule) {
		this.editRule = editRule;
	}
	public String getRuleGroupService() {
		return ruleGroupService;
	}
	public void setRuleGroupService(String ruleGroupService) {
		this.ruleGroupService = ruleGroupService;
	}
	public List<String> getRuletypeLOVArray() {
		return ruletypeLOVArray;
	}
	public void setRuletypeLOVArray(List<String> ruletypeLOVArray) {
		this.ruletypeLOVArray = ruletypeLOVArray;
	}

    
}