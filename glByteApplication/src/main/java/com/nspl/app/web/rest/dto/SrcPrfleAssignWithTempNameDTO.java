package com.nspl.app.web.rest.dto;

import java.time.ZonedDateTime;

public class SrcPrfleAssignWithTempNameDTO {
	
	private Long id;
	private Long sourceProfileId;
	private String sourceProfileIdForDisplay;
	private String fileNameFormat;
	private String fileDescription;
	private String fileExtension;
	private String frequencyType;
	private String dueTime;
	private Integer day;
	private String sourceDirectoryPath;
	private String localDirectoryPath;
	private Long templateId;
	private String templateName;
	private Boolean enableFlag;
	private Long createdBy;
	private ZonedDateTime creationDate;
	private Long lastUpdatedBy;
	private ZonedDateTime lastUpdatedDate;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getSourceProfileId() {
		return sourceProfileId;
	}
	public void setSourceProfileId(Long sourceProfileId) {
		this.sourceProfileId = sourceProfileId;
	}
	public String getFileNameFormat() {
		return fileNameFormat;
	}
	public void setFileNameFormat(String fileNameFormat) {
		this.fileNameFormat = fileNameFormat;
	}
	public String getFileDescription() {
		return fileDescription;
	}
	public void setFileDescription(String fileDescription) {
		this.fileDescription = fileDescription;
	}
	public String getFileExtension() {
		return fileExtension;
	}
	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}
	public String getFrequencyType() {
		return frequencyType;
	}
	public void setFrequencyType(String frequencyType) {
		this.frequencyType = frequencyType;
	}
	public String getDueTime() {
		return dueTime;
	}
	public void setDueTime(String dueTime) {
		this.dueTime = dueTime;
	}
	public Integer getDay() {
		return day;
	}
	public void setDay(Integer day) {
		this.day = day;
	}
	public String getSourceDirectoryPath() {
		return sourceDirectoryPath;
	}
	public void setSourceDirectoryPath(String sourceDirectoryPath) {
		this.sourceDirectoryPath = sourceDirectoryPath;
	}
	public String getLocalDirectoryPath() {
		return localDirectoryPath;
	}
	public void setLocalDirectoryPath(String localDirectoryPath) {
		this.localDirectoryPath = localDirectoryPath;
	}
	public Long getTemplateId() {
		return templateId;
	}
	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public Boolean getEnableFlag() {
		return enableFlag;
	}
	public void setEnableFlag(Boolean enableFlag) {
		this.enableFlag = enableFlag;
	}
	public Long getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}
	public ZonedDateTime getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(ZonedDateTime creationDate) {
		this.creationDate = creationDate;
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
	public String getSourceProfileIdForDisplay() {
		return sourceProfileIdForDisplay;
	}
	public void setSourceProfileIdForDisplay(String sourceProfileIdForDisplay) {
		this.sourceProfileIdForDisplay = sourceProfileIdForDisplay;
	}
	
	
	
	
	
}
