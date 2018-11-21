package com.nspl.app.web.rest.dto;

public class ReconOutboundParams {
	
	private Long groupId;
	private Long sViewId;
	private Long tViewId;
	private String periodFactor;
	private String rangeFrom;
	private String rangeTo;
	private String fileExport;
	private Long pageNumber;
	private Long pageSize;
	private String status;
	private String fileType;
	
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public Long getsViewId() {
		return sViewId;
	}
	public void setsViewId(Long sViewId) {
		this.sViewId = sViewId;
	}
	public Long gettViewId() {
		return tViewId;
	}
	public void settViewId(Long tViewId) {
		this.tViewId = tViewId;
	}
	public String getPeriodFactor() {
		return periodFactor;
	}
	public void setPeriodFactor(String periodFactor) {
		this.periodFactor = periodFactor;
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
	public String getFileExport() {
		return fileExport;
	}
	public void setFileExport(String fileExport) {
		this.fileExport = fileExport;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	
}
