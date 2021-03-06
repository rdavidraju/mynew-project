package com.nspl.app.web.rest.dto;

public class ReconCountNAmountsDTO {

	private String ruleGroupId;
	private String sViewId;
	private String tViewId;
	private String rangeFrom;
	private String rangeTo;
	private String groupBy;
	private String sColumnName;
	private String tColumnName;
	private String periodFactor;
	private Long tenantId;
	private String status;
	private String dateQualifierType;
	private Long pageNumber;	
	private Long pageSize;
	private String groupByColumnName;
	private Boolean exportToExcel;
	private String exportFileType;
	
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
	public String getGroupBy() {
		return groupBy;
	}
	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}
	public String getPeriodFactor() {
		return periodFactor;
	}
	public void setPeriodFactor(String periodFactor) {
		this.periodFactor = periodFactor;
	}
	public Long getTenantId() {
		return tenantId;
	}
	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getsColumnName() {
		return sColumnName;
	}
	public void setsColumnName(String sColumnName) {
		this.sColumnName = sColumnName;
	}
	public String gettColumnName() {
		return tColumnName;
	}
	public void settColumnName(String tColumnName) {
		this.tColumnName = tColumnName;
	}
	public String getRuleGroupId() {
		return ruleGroupId;
	}
	public void setRuleGroupId(String ruleGroupId) {
		this.ruleGroupId = ruleGroupId;
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
	public String getDateQualifierType() {
		return dateQualifierType;
	}
	public void setDateQualifierType(String dateQualifierType) {
		this.dateQualifierType = dateQualifierType;
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
	public String getGroupByColumnName() {
		return groupByColumnName;
	}
	public void setGroupByColumnName(String groupByColumnName) {
		this.groupByColumnName = groupByColumnName;
	}
	public Boolean getExportToExcel() {
		return exportToExcel;
	}
	public void setExportToExcel(Boolean exportToExcel) {
		this.exportToExcel = exportToExcel;
	}
	public String getExportFileType() {
		return exportFileType;
	}
	public void setExportFileType(String exportFileType) {
		this.exportFileType = exportFileType;
	}
	
}
