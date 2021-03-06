package com.nspl.app.web.rest.dto;

import java.time.LocalDate;
import java.time.ZonedDateTime;

public class TenantConfigModulesDTO {

	private Long id;
    private Long tenantId;
    private String modules;
    private Boolean enabledFlag;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private String contractNum;
    private Long createdBy;
    private ZonedDateTime createdDate;
    private Long lastUpdatedBy;
    private ZonedDateTime lastUpdatedDate;
    private Boolean mandatory;
    private String purpose;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getTenantId() {
		return tenantId;
	}
	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
	public String getModules() {
		return modules;
	}
	public void setModules(String modules) {
		this.modules = modules;
	}
	public Boolean getEnabledFlag() {
		return enabledFlag;
	}
	public void setEnabledFlag(Boolean enabledFlag) {
		this.enabledFlag = enabledFlag;
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
	public String getContractNum() {
		return contractNum;
	}
	public void setContractNum(String contractNum) {
		this.contractNum = contractNum;
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
	public Boolean getMandatory() {
		return mandatory;
	}
	public void setMandatory(Boolean mandatory) {
		this.mandatory = mandatory;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
    
}
