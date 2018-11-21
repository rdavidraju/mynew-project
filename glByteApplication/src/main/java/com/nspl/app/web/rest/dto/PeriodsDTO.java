package com.nspl.app.web.rest.dto;

import java.time.LocalDate;
import java.time.ZonedDateTime;

public class PeriodsDTO {
	
	private Long id;
	private String calId;
	private String periodName;
	private ZonedDateTime fromDate;
	private ZonedDateTime toDate;
	private Integer periodNum;
	private Integer quarter;
	private Integer year;
	private Boolean adjustment;
	private Long createdBy;
	private ZonedDateTime createdDate;
	private Long lastUpdatedBy;
	private ZonedDateTime lastUpdatedDate;
	private Boolean enabledFlag;
	private String status;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCalId() {
		return calId;
	}
	public void setCalId(String calId) {
		this.calId = calId;
	}
	public String getPeriodName() {
		return periodName;
	}
	public void setPeriodName(String periodName) {
		this.periodName = periodName;
	}
	public ZonedDateTime getFromDate() {
		return fromDate;
	}
	public void setFromDate(ZonedDateTime fromDate) {
		this.fromDate = fromDate;
	}
	public ZonedDateTime getToDate() {
		return toDate;
	}
	public void setToDate(ZonedDateTime toDate) {
		this.toDate = toDate;
	}
	public Integer getPeriodNum() {
		return periodNum;
	}
	public void setPeriodNum(Integer periodNum) {
		this.periodNum = periodNum;
	}
	public Integer getQuarter() {
		return quarter;
	}
	public void setQuarter(Integer quarter) {
		this.quarter = quarter;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public Boolean getAdjustment() {
		return adjustment;
	}
	public void setAdjustment(Boolean adjustment) {
		this.adjustment = adjustment;
	}
	public Long getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}
	public ZonedDateTime getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(ZonedDateTime createdDate) {
		this.createdDate = createdDate;
	}
	public Long getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(Long lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	public ZonedDateTime getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	public void setLastUpdatedDate(ZonedDateTime lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
	public Boolean getEnabledFlag() {
		return enabledFlag;
	}
	public void setEnabledFlag(Boolean enabledFlag) {
		this.enabledFlag = enabledFlag;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}
