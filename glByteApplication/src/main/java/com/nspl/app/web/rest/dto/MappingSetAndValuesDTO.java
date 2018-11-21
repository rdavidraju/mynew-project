package com.nspl.app.web.rest.dto;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;

import com.nspl.app.domain.MappingSetValues;

public class MappingSetAndValuesDTO {
	
	private Long id;
	private String name;
	private String description;
	private List<HashMap> purpose;
	private String lookUppurpose;
	private Long tenantId;
	private Long createdBy;
	private Long lastUpdatedBy;
	private ZonedDateTime createdDate;
	private ZonedDateTime lastUpdatedDate;
	private ZonedDateTime startDate;
	private ZonedDateTime endDate;
	private Boolean enabledFlag;
	
	private List<MappingSetValues> mappingSetValues;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public List<HashMap> getPurpose() {
		return purpose;
	}
	public void setPurpose(List<HashMap> purpose) {
		this.purpose = purpose;
	}
	public List<MappingSetValues> getMappingSetValues() {
		return mappingSetValues;
	}
	public void setMappingSetValues(List<MappingSetValues> mappingSetValues) {
		this.mappingSetValues = mappingSetValues;
	}
	public Long getTenantId() {
		return tenantId;
	}
	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
	public Long getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}
	public Long getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(Long lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	public ZonedDateTime getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(ZonedDateTime createdDate) {
		this.createdDate = createdDate;
	}
	public ZonedDateTime getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	public void setLastUpdatedDate(ZonedDateTime lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
	public String getLookUppurpose() {
		return lookUppurpose;
	}
	public void setLookUppurpose(String lookUppurpose) {
		this.lookUppurpose = lookUppurpose;
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
	public Boolean getEnabledFlag() {
		return enabledFlag;
	}
	public void setEnabledFlag(Boolean enabledFlag) {
		this.enabledFlag = enabledFlag;
	}
	
}
