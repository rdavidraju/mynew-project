package com.nspl.app.web.rest.dto;

import java.time.LocalDate;
import java.time.ZonedDateTime;

public class CalendarDTO {
	
	private String id;
	private Long tenantId;
	private String name;
	private String description;
	private String calendarType;
	private ZonedDateTime startDate;
	private ZonedDateTime endDate;
	private boolean enabledFlag;
	private Long createdBy;
	private ZonedDateTime createdDate;
	private Long lastUpdatedBy;
	private ZonedDateTime lastUpdatedDate;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Long getTenantId() {
		return tenantId;
	}
	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCalendarType() {
		return calendarType;
	}
	public void setCalendarType(String calendarType) {
		this.calendarType = calendarType;
	}
	public ZonedDateTime getStartDate() {
		return startDate;
	}
	public void setStartDate(ZonedDateTime startDate) {
		this.startDate = startDate;
	}
	public ZonedDateTime getEndDate() {
		return endDate;
	}
	public void setEndDate(ZonedDateTime endDate) {
		this.endDate = endDate;
	}
	public boolean isEnabledFlag() {
		return enabledFlag;
	}
	public void setEnabledFlag(boolean enabledFlag) {
		this.enabledFlag = enabledFlag;
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
	
}
