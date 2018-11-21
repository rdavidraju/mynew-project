package com.nspl.app.web.rest.dto;

import java.time.ZonedDateTime;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;



public class IntermediateRecordsDTO {
	
	private Long id;
	private Long templateId;
	private String rowIdentifier;
	private String criteria;
	private String criteriaMeaning;
	private String headerInfo;
	private String rowInfo;
	private Integer positionStart;
	private Integer positionEnd;
	private Long tenantId;
	private Long createdBy;
	private Long lastUpdatedBy;
	private ZonedDateTime creationDate;
	private ZonedDateTime lastUpdatedDate;
	private String data;
	private JSONArray sampleData;
	private Boolean display=true;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getTemplateId() {
		return templateId;
	}
	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}
	public String getRowIdentifier() {
		return rowIdentifier;
	}
	public void setRowIdentifier(String rowIdentifier) {
		this.rowIdentifier = rowIdentifier;
	}
	
	public String getCriteria() {
		return criteria;
	}
	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}
	public String getHeaderInfo() {
		return headerInfo;
	}
	public void setHeaderInfo(String headerInfo) {
		this.headerInfo = headerInfo;
	}
	public String getRowInfo() {
		return rowInfo;
	}
	public void setRowInfo(String rowInfo) {
		this.rowInfo = rowInfo;
	}
	public Integer getPositionStart() {
		return positionStart;
	}
	public void setPositionStart(Integer positionStart) {
		this.positionStart = positionStart;
	}
	public Integer getPositionEnd() {
		return positionEnd;
	}
	public void setPositionEnd(Integer positionEnd) {
		this.positionEnd = positionEnd;
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
	public ZonedDateTime getCreationDate() {
		return creationDate;
	}
	public String getCriteriaMeaning() {
		return criteriaMeaning;
	}
	public void setCriteriaMeaning(String criteriaMeaning) {
		this.criteriaMeaning = criteriaMeaning;
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
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public JSONArray getSampleData() {
		return sampleData;
	}
	public void setSampleData(JSONArray newJObject) {
		this.sampleData = newJObject;
	}
	public Boolean getDisplay() {
		return display;
	}
	public void setDisplay(Boolean display) {
		this.display = display;
	}
	
	
}
