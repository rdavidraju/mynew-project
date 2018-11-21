package com.nspl.app.web.rest.dto;

import java.util.HashMap;

public class ReconciledDTO {
	
	private String groupId;
	private String sViewId;
	private String tViewId;
	private String rangeFrom;
	private String rangeTo;
	private HashMap filters;
	private Boolean selectAll;
	private String periodFactor;
	private Long pageNumber;
	private Long pageSize;
	private String sortBy;
	
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getsViewId() {
		return sViewId;
	}
	public void setsViewId(String sViewId) {
		this.sViewId = sViewId;
	}
	public String gettViewId() {
		return tViewId;
	}
	public void settViewId(String tViewId) {
		this.tViewId = tViewId;
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
	public String getPeriodFactor() {
		return periodFactor;
	}
	public void setPeriodFactor(String periodFactor) {
		this.periodFactor = periodFactor;
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
	public String getSortBy() {
		return sortBy;
	}
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}
	
}
