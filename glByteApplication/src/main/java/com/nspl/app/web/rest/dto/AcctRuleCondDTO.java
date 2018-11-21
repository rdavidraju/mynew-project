package com.nspl.app.web.rest.dto;

public class AcctRuleCondDTO {
	
	private Long Id;
	private Integer sequence;
	private String openBracket;
	private String sViewColumnName;
	private Long sViewColumnId;
	private String dataType;
	private String operator;
	private String operatorMeaning;
	private String value;
	private String closeBracket;
	private String logicalOperator;
	private String logicalOperatorMeaning;
	private String func;
	
	
	
	
	public String getFunc() {
		return func;
	}
	public void setFunc(String func) {
		this.func = func;
	}
	public Long getId() {
		return Id;
	}
	public void setId(Long id) {
		Id = id;
	}
	public String getOpenBracket() {
		return openBracket;
	}
	public void setOpenBracket(String openBracket) {
		this.openBracket = openBracket;
	}
	public String getsViewColumnName() {
		return sViewColumnName;
	}
	public void setsViewColumnName(String sViewColumnName) {
		this.sViewColumnName = sViewColumnName;
	}
	public Long getsViewColumnId() {
		return sViewColumnId;
	}
	public void setsViewColumnId(Long sViewColumnId) {
		this.sViewColumnId = sViewColumnId;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getCloseBracket() {
		return closeBracket;
	}
	public void setCloseBracket(String closeBracket) {
		this.closeBracket = closeBracket;
	}
	public String getLogicalOperator() {
		return logicalOperator;
	}
	public void setLogicalOperator(String logicalOperator) {
		this.logicalOperator = logicalOperator;
	}
	public Integer getSequence() {
		return sequence;
	}
	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
	public String getOperatorMeaning() {
		return operatorMeaning;
	}
	public void setOperatorMeaning(String operatorMeaning) {
		this.operatorMeaning = operatorMeaning;
	}
	public String getLogicalOperatorMeaning() {
		return logicalOperatorMeaning;
	}
	public void setLogicalOperatorMeaning(String logicalOperatorMeaning) {
		this.logicalOperatorMeaning = logicalOperatorMeaning;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	

}
