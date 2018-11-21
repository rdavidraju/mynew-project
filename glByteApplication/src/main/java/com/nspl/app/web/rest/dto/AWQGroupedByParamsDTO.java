package com.nspl.app.web.rest.dto;

import java.util.List;

public class AWQGroupedByParamsDTO {

	private Long tenantId;
	private Long groupId;
	private Long dataViewId;
	private String groupBy;
	private String rangeFrom;
	private String rangeTo;
	private String status;
	private String searchWord;
	private String exportFile;
	private Long pageNumber;
	private Long pageSize;
	private String day;
	private String columnValue;
	private Long columnId;
	private Long ruleId;
	private String batchName;
	private Long sortByColumnId;
	private List<String> groupedParamsList;

	public Long getTenantId() {
		return tenantId;
	}
	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public String getGroupBy() {
		return groupBy;
	}
	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}
	public String getRangeFrom() {
		return rangeFrom;
	}
	public void setRangeFrom(String rangeFrom) {
		this.rangeFrom = rangeFrom;
	}
	public String getRangeTo() {
		return rangeTo;
	}
	public void setRangeTo(String rangeTo) {
		this.rangeTo = rangeTo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSearchWord() {
		return searchWord;
	}
	public void setSearchWord(String searchWord) {
		this.searchWord = searchWord;
	}
	public String getExportFile() {
		return exportFile;
	}
	public void setExportFile(String exportFile) {
		this.exportFile = exportFile;
	}
	public Long getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(Long pageNumber) {
		this.pageNumber = pageNumber;
	}
	public Long getPageSize() {
		return pageSize;
	}
	public void setPageSize(Long pageSize) {
		this.pageSize = pageSize;
	}
	public Long getDataViewId() {
		return dataViewId;
	}
	public void setDataViewId(Long dataViewId) {
		this.dataViewId = dataViewId;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getColumnValue() {
		return columnValue;
	}
	public void setColumnValue(String columnValue) {
		this.columnValue = columnValue;
	}
	public Long getColumnId() {
		return columnId;
	}
	public void setColumnId(Long columnId) {
		this.columnId = columnId;
	}
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
	public List<String> getGroupedParamsList() {
		return groupedParamsList;
	}
	public void setGroupedParamsList(List<String> groupedParamsList) {
		this.groupedParamsList = groupedParamsList;
	}
	public Long getSortByColumnId() {
		return sortByColumnId;
	}
	public void setSortByColumnId(Long sortByColumnId) {
		this.sortByColumnId = sortByColumnId;
	}
	
}
