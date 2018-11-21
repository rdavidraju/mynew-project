package com.nspl.app.web.rest.dto;

import java.util.List;

import com.nspl.app.domain.RowConditions;

public class ExcelConditionsDTO {
	private String ftlService;
	private String formCntrl;
	//private List<String> columnHeadings;
	private List<String > columnsList;
	private List<RowConditionsDTO> endConditionsList;
	private List<RowConditionsDTO> skipConditionsList;
	public List<String> getColumnsList() {
		return columnsList;
	}
	public void setColumnsList(List<String> columnsList) {
		this.columnsList = columnsList;
	}
	public List<RowConditionsDTO> getEndConditionsList() {
		return endConditionsList;
	}
	public void setEndConditionsList(List<RowConditionsDTO> endConditionsList) {
		this.endConditionsList = endConditionsList;
	}
	public List<RowConditionsDTO> getSkipConditionsList() {
		return skipConditionsList;
	}
	public void setSkipConditionsList(List<RowConditionsDTO> skipConditionsList) {
		this.skipConditionsList = skipConditionsList;
	}
	public String getFtlService() {
		return ftlService;
	}
	public void setFtlService(String ftlService) {
		this.ftlService = ftlService;
	}
	public String getFormCntrl() {
		return formCntrl;
	}
	public void setFormCntrl(String formCntrl) {
		this.formCntrl = formCntrl;
	}
//	public List<String> getColumnHeadings() {
//		return columnHeadings;
//	}
//	public void setColumnHeadings(List<String> columnHeadings) {
//		this.columnHeadings = columnHeadings;
//	}
//	
	
	
}
