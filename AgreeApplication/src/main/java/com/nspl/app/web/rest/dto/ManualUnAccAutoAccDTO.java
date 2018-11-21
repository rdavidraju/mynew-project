package com.nspl.app.web.rest.dto;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;

public class ManualUnAccAutoAccDTO {
	
	private Long tenantId;
	private String groupId;
	private String viewId;
	private String status;
	private String rangeFrom;
	private String rangeTo;
	private String periodFactor;
	private String type;
	private List<BigInteger> originalRowIds;
	private Long userId;
	private List<HashMap> filters;
	private String searchWord;
	private List<HashMap> columnSearch;
	private String sortByColumnName;
	private String sortOrderBy;
	private String accountingType;
	
	public Long getTenantId() {
		return tenantId;
	}
	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getViewId() {
		return viewId;
	}
	public void setViewId(String viewId) {
		this.viewId = viewId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public String getPeriodFactor() {
		return periodFactor;
	}
	public void setPeriodFactor(String periodFactor) {
		this.periodFactor = periodFactor;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<BigInteger> getOriginalRowIds() {
		return originalRowIds;
	}
	public void setOriginalRowIds(List<BigInteger> originalRowIds) {
		this.originalRowIds = originalRowIds;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public List<HashMap> getFilters() {
		return filters;
	}
	public void setFilters(List<HashMap> filters) {
		this.filters = filters;
	}
	public String getSearchWord() {
		return searchWord;
	}
	public void setSearchWord(String searchWord) {
		this.searchWord = searchWord;
	}
	public List<HashMap> getColumnSearch() {
		return columnSearch;
	}
	public void setColumnSearch(List<HashMap> columnSearch) {
		this.columnSearch = columnSearch;
	}
	public String getSortByColumnName() {
		return sortByColumnName;
	}
	public void setSortByColumnName(String sortByColumnName) {
		this.sortByColumnName = sortByColumnName;
	}
	public String getSortOrderBy() {
		return sortOrderBy;
	}
	public void setSortOrderBy(String sortOrderBy) {
		this.sortOrderBy = sortOrderBy;
	}
	public String getAccountingType() {
		return accountingType;
	}
	public void setAccountingType(String accountingType) {
		this.accountingType = accountingType;
	}
	
}
