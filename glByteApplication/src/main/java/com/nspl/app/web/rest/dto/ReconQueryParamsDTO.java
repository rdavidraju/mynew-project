package com.nspl.app.web.rest.dto;

import java.util.List;

public class ReconQueryParamsDTO {

	private Long groupId;
	private Long dataViewId;
	private String sourceOrTarget;
	private String groupBy;
	private String rangeFrom;
	private String rangeTo;
	private String status;
	private Long tenantId;
	private Long userId;
	private String searchWord;
	private List<Long> originalRowIds;
	private List<String> groupedParamsList;
	private String jobReference;
	private Long sortByColumnId;
	private String sortOrderBy;
	private String periodFactor;
	
	private RWQDataFetchDTO keyValues;
	private AWQGroupByParamsDTO groupByParams;
	
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public Long getDataViewId() {
		return dataViewId;
	}
	public void setDataViewId(Long dataViewId) {
		this.dataViewId = dataViewId;
	}
	public String getSourceOrTarget() {
		return sourceOrTarget;
	}
	public void setSourceOrTarget(String sourceOrTarget) {
		this.sourceOrTarget = sourceOrTarget;
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
	public Long getTenantId() {
		return tenantId;
	}
	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public RWQDataFetchDTO getKeyValues() {
		return keyValues;
	}
	public void setKeyValues(RWQDataFetchDTO keyValues) {
		this.keyValues = keyValues;
	}
	public AWQGroupByParamsDTO getGroupByParams() {
		return groupByParams;
	}
	public void setGroupByParams(AWQGroupByParamsDTO groupByParams) {
		this.groupByParams = groupByParams;
	}
	public String getSearchWord() {
		return searchWord;
	}
	public void setSearchWord(String searchWord) {
		this.searchWord = searchWord;
	}
	public List<Long> getOriginalRowIds() {
		return originalRowIds;
	}
	public void setOriginalRowIds(List<Long> originalRowIds) {
		this.originalRowIds = originalRowIds;
	}
	public List<String> getGroupedParamsList() {
		return groupedParamsList;
	}
	public void setGroupedParamsList(List<String> groupedParamsList) {
		this.groupedParamsList = groupedParamsList;
	}
	public String getJobReference() {
		return jobReference;
	}
	public void setJobReference(String jobReference) {
		this.jobReference = jobReference;
	}
	public Long getSortByColumnId() {
		return sortByColumnId;
	}
	public void setSortByColumnId(Long sortByColumnId) {
		this.sortByColumnId = sortByColumnId;
	}
	public String getSortOrderBy() {
		return sortOrderBy;
	}
	public void setSortOrderBy(String sortOrderBy) {
		this.sortOrderBy = sortOrderBy;
	}
	public String getPeriodFactor() {
		return periodFactor;
	}
	public void setPeriodFactor(String periodFactor) {
		this.periodFactor = periodFactor;
	}
	
}
