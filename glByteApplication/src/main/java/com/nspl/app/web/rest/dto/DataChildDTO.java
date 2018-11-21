package com.nspl.app.web.rest.dto;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class DataChildDTO {

    private Long tenantId;
    private String dataViewId;
    private Long masterRowId;
    private String rowDescription;
    private String adjustmentType;
    private String adjustmentMethod;
    private Double percentValue;
    private Double amount;
    private Long createdBy;
    private Long lastUpdatedBy;
    private ZonedDateTime creationDate;
    private ZonedDateTime lastUpdatedDate;
    
	public Long getTenantId() {
		return tenantId;
	}
	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
	public String getDataViewId() {
		return dataViewId;
	}
	public void setDataViewId(String dataViewId) {
		this.dataViewId = dataViewId;
	}
	public Long getMasterRowId() {
		return masterRowId;
	}
	public void setMasterRowId(Long masterRowId) {
		this.masterRowId = masterRowId;
	}
	public String getRowDescription() {
		return rowDescription;
	}
	public void setRowDescription(String rowDescription) {
		this.rowDescription = rowDescription;
	}
	public String getAdjustmentType() {
		return adjustmentType;
	}
	public void setAdjustmentType(String adjustmentType) {
		this.adjustmentType = adjustmentType;
	}
	public String getAdjustmentMethod() {
		return adjustmentMethod;
	}
	public void setAdjustmentMethod(String adjustmentMethod) {
		this.adjustmentMethod = adjustmentMethod;
	}
	public Double getPercentValue() {
		return percentValue;
	}
	public void setPercentValue(Double percentValue) {
		this.percentValue = percentValue;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
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
	public ZonedDateTime getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(ZonedDateTime creationDate) {
		this.creationDate = creationDate;
	}
	public ZonedDateTime getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	public void setLastUpdatedDate(ZonedDateTime lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
    
}
