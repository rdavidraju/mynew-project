package com.nspl.app.web.rest.dto;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.nspl.app.domain.DataViewsColumns;

public class DataViewsToViewColumnsDTO {

    private String id;
    private Long tenantId;
    private String dataViewDispName;
    private String dataViewName;
    private String dataViewObject;
    private Boolean enabledFlag;
    private Long createdBy;
    private Long lastUpdatedBy;
    private ZonedDateTime creationDate;
    private ZonedDateTime lastUpdatedDate;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private String description;
    private String type;
    private boolean endDated;
    
   List<DataViewsColumns> dataViewsColumnsList;

   
   
	public boolean isEndDated() {
		return endDated;
	}
	
	public void setEndDated(boolean endDated) {
		this.endDated = endDated;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
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
	
	public String getDataViewDispName() {
		return dataViewDispName;
	}
	
	public void setDataViewDispName(String dataViewDispName) {
		this.dataViewDispName = dataViewDispName;
	}
	
	public String getDataViewName() {
		return dataViewName;
	}
	
	public void setDataViewName(String dataViewName) {
		this.dataViewName = dataViewName;
	}
	
	public String getDataViewObject() {
		return dataViewObject;
	}
	
	public void setDataViewObject(String dataViewObject) {
		this.dataViewObject = dataViewObject;
	}
	
	public Boolean getEnabledFlag() {
		return enabledFlag;
	}
	
	public void setEnabledFlag(Boolean enabledFlag) {
		this.enabledFlag = enabledFlag;
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
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public List<DataViewsColumns> getDataViewsColumnsList() {
		return dataViewsColumnsList;
	}
	
	public void setDataViewsColumnsList(List<DataViewsColumns> dataViewsColumnsList) {
		this.dataViewsColumnsList = dataViewsColumnsList;
	}

	@Override
	public String toString() {
		return "DataViewsToViewColumnsDTO [id=" + id + ", tenantId=" + tenantId
				+ ", dataViewDispName=" + dataViewDispName + ", dataViewName="
				+ dataViewName + ", dataViewObject=" + dataViewObject
				+ ", enabledFlag=" + enabledFlag + ", createdBy=" + createdBy
				+ ", lastUpdatedBy=" + lastUpdatedBy + ", creationDate="
				+ creationDate + ", lastUpdatedDate=" + lastUpdatedDate
				+ ", startDate=" + startDate + ", endDate=" + endDate
				+ ", description=" + description + ", type=" + type
				+ ", endDated=" + endDated + ", dataViewsColumnsList="
				+ dataViewsColumnsList + "]";
	}
   
	
}
