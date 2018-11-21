package com.nspl.app.web.rest.dto;

import java.time.ZonedDateTime;

public class BatchDetailsDTO {
private Long srcInbHistId;
private String batchHeaderName;
private String profileName;
private Long templateId;
private String templateName;
private String fileSize;
private String status;
private String statusMeaning;
private String failedReason;
private boolean hold=false;
private String fileName;
private Long srcProfAssId;
private int totalCount;
private ZonedDateTime fileTransformedDate;
public Long getSrcInbHistId() {
	return srcInbHistId;
}
public void setSrcInbHistId(Long srcInbHistId) {
	this.srcInbHistId = srcInbHistId;
}
public String getProfileName() {
	return profileName;
}
public void setProfileName(String profileName) {
	this.profileName = profileName;
}
public String getTemplateName() {
	return templateName;
}
public void setTemplateName(String templateName) {
	this.templateName = templateName;
}
public String getFileSize() {
	return fileSize;
}
public void setFileSize(String fileSize) {
	this.fileSize = fileSize;
}
public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
}
public boolean isHold() {
	return hold;
}
public void setHold(boolean hold) {
	this.hold = hold;
}
public String getFileName() {
	return fileName;
}
public void setFileName(String fileName) {
	this.fileName = fileName;
}
public Long getSrcProfAssId() {
	return srcProfAssId;
}
public void setSrcProfAssId(Long srcProfAssId) {
	this.srcProfAssId = srcProfAssId;
}
public String getBatchHeaderName() {
	return batchHeaderName;
}
public void setBatchHeaderName(String batchHeaderName) {
	this.batchHeaderName = batchHeaderName;
}
public Long getTemplateId() {
	return templateId;
}
public void setTemplateId(Long templateId) {
	this.templateId = templateId;
}
public ZonedDateTime getFileTransformedDate() {
	return fileTransformedDate;
}
public void setFileTransformedDate(ZonedDateTime fileTransformedDate) {
	this.fileTransformedDate = fileTransformedDate;
}
public int getTotalCount() {
	return totalCount;
}
public void setTotalCount(int totalCount) {
	this.totalCount = totalCount;
}
public String getStatusMeaning() {
	return statusMeaning;
}
public void setStatusMeaning(String statusMeaning) {
	this.statusMeaning = statusMeaning;
}
public String getFailedReason() {
	return failedReason;
}
public void setFailedReason(String failedReason) {
	this.failedReason = failedReason;
}


}
