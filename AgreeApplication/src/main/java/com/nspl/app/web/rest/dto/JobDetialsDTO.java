package com.nspl.app.web.rest.dto;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

public class JobDetialsDTO {
	private String id;
	private String jobName;
	private String jobDesc;
	private Long programId;
	private String programName;
	private String jobStatus;
	private List<Parameters> parameters;
	private Boolean sendFailAlerts;
	private Long maxConsecutiveFails;
	private ZonedDateTime scheStartDate;
	private ZonedDateTime scheEndDate;
	private List<Frequencies> frequencies;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getJobDesc() {
		return jobDesc;
	}
	public void setJobDesc(String jobDesc) {
		this.jobDesc = jobDesc;
	}
	public Long getProgramId() {
		return programId;
	}
	public void setProgramId(Long programId) {
		this.programId = programId;
	}
	
	public String getJobStatus() {
		return jobStatus;
	}
	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}
	public List<Parameters> getParameters() {
		return parameters;
	}
	public void setParameters(List<Parameters> parameters) {
		this.parameters = parameters;
	}
	public Boolean getSendFailAlerts() {
		return sendFailAlerts;
	}
	public void setSendFailAlerts(Boolean sendFailAlerts) {
		this.sendFailAlerts = sendFailAlerts;
	}
	public Long getMaxConsecutiveFails() {
		return maxConsecutiveFails;
	}
	public void setMaxConsecutiveFails(Long maxConsecutiveFails) {
		this.maxConsecutiveFails = maxConsecutiveFails;
	}
	public ZonedDateTime getScheStartDate() {
		return scheStartDate;
	}
	public void setScheStartDate(ZonedDateTime scheStartDate) {
		this.scheStartDate = scheStartDate;
	}
	public ZonedDateTime getScheEndDate() {
		return scheEndDate;
	}
	public void setScheEndDate(ZonedDateTime scheEndDate) {
		this.scheEndDate = scheEndDate;
	}
	public List<Frequencies> getFrequencies() {
		return frequencies;
	}
	public void setFrequencies(List<Frequencies> frequencies) {
		this.frequencies = frequencies;
	}
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	

}
