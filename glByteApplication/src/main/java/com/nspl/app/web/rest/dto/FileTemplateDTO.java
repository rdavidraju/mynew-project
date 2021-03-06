package com.nspl.app.web.rest.dto;

import java.time.LocalDate;
import java.time.ZonedDateTime;

public class FileTemplateDTO {
	
	private Long SPAid;
	private String source;
	private Long sourceProfileId;
	private String fileNameFormat;
	private String fileDescription;
	private String fileExtension;
	private String frequencyType;
	private String dueTime;
	private Integer day;
	private String sourceDirectoryPath;
	private String LocalDirectoryPath;
	private Long templateId;
	private String templateName;
	private String templateStatus;
	private Boolean SPAenabledFlag;
	private String assignmentStatus = null;
	private Boolean hold;
	private Long SPAcreatedBy;
	private ZonedDateTime SPAcreationDate;
	private Long SPAlastUpdatedBy;
	private ZonedDateTime SPAlastUpdatedDate;
	private String sourceProfileName;
	private String description;
    private ZonedDateTime startDate;
	private ZonedDateTime endDate;
	private Boolean sourceProfileEnableFlag;
	private Long connectionId;
	private Long tenantId;
	private Long sourceProfileCreatedBy;
	private ZonedDateTime sourceProfileCreationDate; 
	private Long sourceProfileLastUpdatedBy;
	private ZonedDateTime sourceProfileLastUpdateDate;
	private boolean allHold;
	private String holdReason;
	private int count;
	private String idForDisplay;
	private Boolean defaultTemplate;
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public Long getSPAid() {
		return SPAid;
	}
	public void setSPAId(Long sPAid) {
		SPAid = sPAid;
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
		return LocalDirectoryPath;
	}
	public void setLocalDirectoryPath(String localDirectoryPath) {
		LocalDirectoryPath = localDirectoryPath;
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
	public String getTemplateStatus() {
		return templateStatus;
	}
	public void setTemplateStatus(String templateStatus) {
		this.templateStatus = templateStatus;
	}
	public Boolean getSPAenabledFlag() {
		return SPAenabledFlag;
	}
	public void setSPAEnabledFlag(Boolean sPAenabledFlag) {
		SPAenabledFlag = sPAenabledFlag;
	}
	public String getAssignmentStatus() {
		return assignmentStatus;
	}
	public void setAssignmentStatus(String assignmentStatus) {
		this.assignmentStatus = assignmentStatus;
	}
	public Long getSPAcreatedBy() {
		return SPAcreatedBy;
	}
	public void setSPACreatedBy(Long sPAcreatedBy) {
		SPAcreatedBy = sPAcreatedBy;
	}
	public ZonedDateTime getSPAcreationDate() {
		return SPAcreationDate;
	}
	public void setSPACreationDate(ZonedDateTime sPAcreationDate) {
		SPAcreationDate = sPAcreationDate;
	}
	public Long getSPAlastUpdatedBy() {
		return SPAlastUpdatedBy;
	}
	public void setSPALastUpdatedBy(Long sPAlastUpdatedBy) {
		SPAlastUpdatedBy = sPAlastUpdatedBy;
	}
	public ZonedDateTime getSPAlastUpdatedDate() {
		return SPAlastUpdatedDate;
	}
	public void setSPALastUpdatedDate(ZonedDateTime sPAlastUpdatedDate) {
		SPAlastUpdatedDate = sPAlastUpdatedDate;
	}
	public String getSourceProfileName() {
		return sourceProfileName;
	}
	public void setSourceProfileName(String sourceProfileName) {
		this.sourceProfileName = sourceProfileName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	public Boolean getSourceProfileEnableFlag() {
		return sourceProfileEnableFlag;
	}
	public void setSourceProfileEnableFlag(Boolean sourceProfileEnableFlag) {
		this.sourceProfileEnableFlag = sourceProfileEnableFlag;
	}
	public Long getConnectionId() {
		return connectionId;
	}
	public void setConnectionId(Long connectionId) {
		this.connectionId = connectionId;
	}
	public Long getTenantId() {
		return tenantId;
	}
	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
	public Long getSourceProfileCreatedBy() {
		return sourceProfileCreatedBy;
	}
	public void setSourceProfileCreatedBy(Long sourceProfileCreatedBy) {
		this.sourceProfileCreatedBy = sourceProfileCreatedBy;
	}
	public ZonedDateTime getSourceProfileCreationDate() {
		return sourceProfileCreationDate;
	}
	public void setSourceProfileCreationDate(ZonedDateTime sourceProfileCreationDate) {
		this.sourceProfileCreationDate = sourceProfileCreationDate;
	}
	public Long getSourceProfileLastUpdatedBy() {
		return sourceProfileLastUpdatedBy;
	}
	public void setSourceProfileLastUpdatedBy(Long sourceProfileLastUpdatedBy) {
		this.sourceProfileLastUpdatedBy = sourceProfileLastUpdatedBy;
	}
	public ZonedDateTime getSourceProfileLastUpdateDate() {
		return sourceProfileLastUpdateDate;
	}
	public void setSourceProfileLastUpdateDate(
			ZonedDateTime sourceProfileLastUpdateDate) {
		this.sourceProfileLastUpdateDate = sourceProfileLastUpdateDate;
	}
	public Boolean getHold() {
		return hold;
	}
	public void setHold(Boolean hold) {
		this.hold = hold;
	}
	public boolean isAllHold() {
		return allHold;
	}
	public void setAllHold(boolean allHold) {
		this.allHold = allHold;
	}
	public String getHoldReason() {
		return holdReason;
	}
	public void setHoldReason(String holdReason) {
		this.holdReason = holdReason;
	}
	public String getIdForDisplay() {
		return idForDisplay;
	}
	public void setIdForDisplay(String idForDisplay) {
		this.idForDisplay = idForDisplay;
	}
	public Boolean getDefaultTemplate() {
		return defaultTemplate;
	}
	public void setDefaultTemplate(Boolean defaultTemplate) {
		this.defaultTemplate = defaultTemplate;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}

	
	
	
	
	

}
