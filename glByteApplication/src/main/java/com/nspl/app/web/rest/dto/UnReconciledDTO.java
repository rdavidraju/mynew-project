package com.nspl.app.web.rest.dto;

import java.util.HashMap;

public class UnReconciledDTO {

	private String groupId;
	private String dataViewId;
	private String sourceOrTarget;
	private HashMap filters;
	private Boolean selectAll;
	private String rangeFrom;
	private String rangeTo;
	private Long pageNumber;
	private Long pageSize;
	private String searchWord;
	private String periodFactor;
	
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getSourceOrTarget() {
		return sourceOrTarget;
	}
	public void setSourceOrTarget(String sourceOrTarget) {
		this.sourceOrTarget = sourceOrTarget;
	}
	public HashMap getFilters() {
		return filters;
	}
	public void setFilters(HashMap filters) {
		this.filters = filters;
	}
	public Boolean getSelectAll() {
		return selectAll;
	}
	public void setSelectAll(Boolean selectAll) {
		this.selectAll = selectAll;
	}
	public String getDataViewId() {
		return dataViewId;
	}
	public void setDataViewId(String dataViewId) {
		this.dataViewId = dataViewId;
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
	public String getSearchWord() {
		return searchWord;
	}
	public void setSearchWord(String searchWord) {
		this.searchWord = searchWord;
	}
	public String getPeriodFactor() {
		return periodFactor;
	}
	public void setPeriodFactor(String periodFactor) {
		this.periodFactor = periodFactor;
	}
	
}
