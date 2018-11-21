package com.nspl.app.web.rest.dto;

import java.util.HashMap;
import java.util.List;

import com.nspl.app.domain.AcctRuleConditions;
import com.nspl.app.domain.AcctRuleDerivations;

public class LineItems {
	
	private Long Id;
	private String sourceDataviewName;
	private String sourceDataviewDisplayName;
	private Long  sourceDataviewId;
	private String lineType;
	private String lineTypeMeaning;
	private String lineTypeDetail;
	private Long ruleId;
	private String lineTypeDetailMeaning;
	private Long enteredAmtColId;
	private String enteredAmtColName;
	private Long createdBy;
	private Long lastUpdatedBy;
	private List<HashMap> columnsForDV;
	private List<AcctRuleCondDTO> accountingRuleConditions;
	private List<AcctRuleDerivationDTO> accountingRuleDerivations;
	
	public Long getId() {
		return Id;
	}
	public void setId(Long id) {
		Id = id;
	}
	public String getSourceDataviewName() {
		return sourceDataviewName;
	}
	public void setSourceDataviewName(String sourceDataviewName) {
		this.sourceDataviewName = sourceDataviewName;
	}
	public Long getSourceDataviewId() {
		return sourceDataviewId;
	}
	public void setSourceDataviewId(Long sourceDataviewId) {
		this.sourceDataviewId = sourceDataviewId;
	}
	public String getLineType() {
		return lineType;
	}
	public void setLineType(String lineType) {
		this.lineType = lineType;
	}
	public List<AcctRuleCondDTO> getAccountingRuleConditions() {
		return accountingRuleConditions;
	}
	public void setAccountingRuleConditions(
			List<AcctRuleCondDTO> accountingRuleConditions) {
		this.accountingRuleConditions = accountingRuleConditions;
	}
	public List<AcctRuleDerivationDTO> getAccountingRuleDerivations() {
		return accountingRuleDerivations;
	}
	public void setAccountingRuleDerivations(
			List<AcctRuleDerivationDTO> accountingRuleDerivations) {
		this.accountingRuleDerivations = accountingRuleDerivations;
	}
	public List<HashMap> getColumnsForDV() {
		return columnsForDV;
	}
	public void setColumnsForDV(List<HashMap> columnsForDV) {
		this.columnsForDV = columnsForDV;
	}
	public String getSourceDataviewDisplayName() {
		return sourceDataviewDisplayName;
	}
	public void setSourceDataviewDisplayName(String sourceDataviewDisplayName) {
		this.sourceDataviewDisplayName = sourceDataviewDisplayName;
	}
	public String getLineTypeMeaning() {
		return lineTypeMeaning;
	}
	public void setLineTypeMeaning(String lineTypeMeaning) {
		this.lineTypeMeaning = lineTypeMeaning;
	}
	public String getLineTypeDetail() {
		return lineTypeDetail;
	}
	public void setLineTypeDetail(String lineTypeDetail) {
		this.lineTypeDetail = lineTypeDetail;
	}
	public Long getRuleId() {
		return ruleId;
	}
	public void setRuleId(Long ruleId) {
		this.ruleId = ruleId;
	}
	public String getLineTypeDetailMeaning() {
		return lineTypeDetailMeaning;
	}
	public void setLineTypeDetailMeaning(String lineTypeDetailMeaning) {
		this.lineTypeDetailMeaning = lineTypeDetailMeaning;
	}
	public Long getEnteredAmtColId() {
		return enteredAmtColId;
	}
	public void setEnteredAmtColId(Long enteredAmtColId) {
		this.enteredAmtColId = enteredAmtColId;
	}
	public String getEnteredAmtColName() {
		return enteredAmtColName;
	}
	public void setEnteredAmtColName(String enteredAmtColName) {
		this.enteredAmtColName = enteredAmtColName;
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
	
	

}
