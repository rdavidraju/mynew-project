package com.nspl.app.web.rest.dto;

import java.time.ZonedDateTime;
import java.util.HashMap;

public class BatchHeaderDTO {
	
private Long id;
private String batchName;
private String reference;
private String batchType;
private String jobRef;
private String extractionStatus;
private String extractionStatusMeaning;
private ZonedDateTime extractedDateTime;
private String batchDetailsExtractionStatus;
private String transformationStatus;
private String transformationStatusMeaning;
private ZonedDateTime transformedDateTime;
private String batchDetailsTransformationStatus;
private String nextSchedule;
private Integer totalCount;
private Boolean selected ;
private boolean hold;
private String holdReason;
public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}
public String getBatchName() {
	return batchName;
}
public void setBatchName(String batchName) {
	this.batchName = batchName;
}
public String getReference() {
	return reference;
}
public void setReference(String reference) {
	this.reference = reference;
}
public String getExtractionStatus() {
	return extractionStatus;
}
public void setExtractionStatus(String extractionStatus) {
	this.extractionStatus = extractionStatus;
}
public ZonedDateTime getExtractedDateTime() {
	return extractedDateTime;
}
public void setExtractedDateTime(ZonedDateTime extractedDateTime) {
	this.extractedDateTime = extractedDateTime;
}
public String getBatchDetailsExtractionStatus() {
	return batchDetailsExtractionStatus;
}
public void setBatchDetailsExtractionStatus(String batchDetailsExtractionStatus) {
	this.batchDetailsExtractionStatus = batchDetailsExtractionStatus;
}
public String getTransformationStatus() {
	return transformationStatus;
}
public void setTransformationStatus(String transformationStatus) {
	this.transformationStatus = transformationStatus;
}
public ZonedDateTime getTransformedDateTime() {
	return transformedDateTime;
}
public void setTransformedDateTime(ZonedDateTime transformedDateTime) {
	this.transformedDateTime = transformedDateTime;
}
public String getBatchDetailsTransformationStatus() {
	return batchDetailsTransformationStatus;
}
public void setBatchDetailsTransformationStatus(
		String batchDetailsTransformationStatus) {
	this.batchDetailsTransformationStatus = batchDetailsTransformationStatus;
}
public String getNextSchedule() {
	return nextSchedule;
}
public void setNextSchedule(String nextSchedule) {
	this.nextSchedule = nextSchedule;
}
public String getBatchType() {
	return batchType;
}
public void setBatchType(String batchType) {
	this.batchType = batchType;
}
public Integer getTotalCount() {
	return totalCount;
}
public void setTotalCount(Integer totalCount) {
	this.totalCount = totalCount;
}
public String getJobRef() {
	return jobRef;
}
public void setJobRef(String jobRef) {
	this.jobRef = jobRef;
}
public Boolean getSelected() {
	return selected;
}
public void setSelected(Boolean selected) {
	this.selected = selected;
}
public boolean isHold() {
	return hold;
}
public void setHold(boolean hold) {
	this.hold = hold;
}
public String getHoldReason() {
	return holdReason;
}
public void setHoldReason(String holdReason) {
	this.holdReason = holdReason;
}
public String getExtractionStatusMeaning() {
	return extractionStatusMeaning;
}
public void setExtractionStatusMeaning(String extractionStatusMeaning) {
	this.extractionStatusMeaning = extractionStatusMeaning;
}
public String getTransformationStatusMeaning() {
	return transformationStatusMeaning;
}
public void setTransformationStatusMeaning(String transformationStatusMeaning) {
	this.transformationStatusMeaning = transformationStatusMeaning;
}


}
