package com.nspl.app.web.rest.dto;

public class SourceProfilesAndTemplateDTO {
	
	private String templateId;
	private String sourceProfileName;
	private String connectionId;
	private String fileNameFormat;
	private String fileExtension;
	private String fileDescription;
	private String sourceDirectoryPath;
	private Boolean extractAndTransform;
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	public String getSourceProfileName() {
		return sourceProfileName;
	}
	public void setSourceProfileName(String sourceProfileName) {
		this.sourceProfileName = sourceProfileName;
	}
	public String getConnectionId() {
		return connectionId;
	}
	public void setConnectionId(String connectionId) {
		this.connectionId = connectionId;
	}
	public String getFileNameFormat() {
		return fileNameFormat;
	}
	public void setFileNameFormat(String fileNameFormat) {
		this.fileNameFormat = fileNameFormat;
	}
	public String getFileExtension() {
		return fileExtension;
	}
	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}
	public String getFileDescription() {
		return fileDescription;
	}
	public void setFileDescription(String fileDescription) {
		this.fileDescription = fileDescription;
	}
	public String getSourceDirectoryPath() {
		return sourceDirectoryPath;
	}
	public void setSourceDirectoryPath(String sourceDirectoryPath) {
		this.sourceDirectoryPath = sourceDirectoryPath;
	}
	public Boolean getExtractAndTransform() {
		return extractAndTransform;
	}
	public void setExtractAndTransform(Boolean extractAndTransform) {
		this.extractAndTransform = extractAndTransform;
	}
	
	
}
