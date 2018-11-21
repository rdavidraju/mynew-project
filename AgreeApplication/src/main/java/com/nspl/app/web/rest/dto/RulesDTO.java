package com.nspl.app.web.rest.dto;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

import com.nspl.app.domain.RuleConditions;
import com.nspl.app.domain.Rules;

public class RulesDTO {
	
	private Long id;
	private String itemName;
	public boolean hideRule;
	private RuleDTO rule;
	private List<RuleConditionsDTO> ruleConditions;
	private String ruleGroupService;
	private boolean hasAmountQualifier;
	private boolean mandAmtMatch = false;
	private List<String> sourceDataViewsAndColumns ;
	private List<String> targetDataViewsAndColumns;
	public RuleDTO getRule() {
		return rule;
	}
	public void setRule(RuleDTO rule) {
		this.rule = rule;
	}
	
	public List<RuleConditionsDTO> getRuleConditions() {
		return ruleConditions;
	}
	public void setRuleConditions(List<RuleConditionsDTO> ruleconditions) {
		this.ruleConditions = ruleconditions;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getRuleGroupService() {
		return ruleGroupService;
	}
	public void setRuleGroupService(String ruleGroupService) {
		this.ruleGroupService = ruleGroupService;
	}
	public boolean isHideRule() {
		return hideRule;
	}
	public void setHideRule(boolean hideRule) {
		this.hideRule = hideRule;
	}
	public boolean isHasAmountQualifier() {
		return hasAmountQualifier;
	}
	public void setHasAmountQualifier(boolean hasAmountQualifier) {
		this.hasAmountQualifier = hasAmountQualifier;
	}
	public boolean isMandAmtMatch() {
		return mandAmtMatch;
	}
	public void setMandAmtMatch(boolean mandAmtMatch) {
		this.mandAmtMatch = mandAmtMatch;
	}
	public List<String> getSourceDataViewsAndColumns() {
		return sourceDataViewsAndColumns;
	}
	public void setSourceDataViewsAndColumns(List<String> sourceDataViewsAndColumns) {
		this.sourceDataViewsAndColumns = sourceDataViewsAndColumns;
	}
	public List<String> getTargetDataViewsAndColumns() {
		return targetDataViewsAndColumns;
	}
	public void setTargetDataViewsAndColumns(List<String> targetDataViewsAndColumns) {
		this.targetDataViewsAndColumns = targetDataViewsAndColumns;
	}
	
	
}
