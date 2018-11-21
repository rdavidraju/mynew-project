package com.nspl.app.web.rest.dto;


public class OozieJobDTO {
	private String jobType;
	private String path;
	private String hdfsPath;
	private String startDate;
	private String endDate;
	private String frequency;
	private String className;
	private String jarFilePath;
	private String jobName;
	private String programName;
	private String mailId;
	private String frequencyType;
	private Long programId;
	private Long jobId;
	private String userId;
	private String RuleType;
	private String queueName;
	
	
	
	
	
	
	
	
	
	public String getRuleType() {
		return RuleType;
	}
	public void setRuleType(String ruleType) {
		RuleType = ruleType;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Long getJobId() {
		return jobId;
	}
	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}
	public Long getProgramId() {
		return programId;
	}
	public void setProgramId(Long programId) {
		this.programId = programId;
	}
	public String getMailId() {
		return mailId;
	}
	public void setMailId(String mailId) {
		this.mailId = mailId;
	}
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobType() {
		return jobType;
	}
	public void setJobType(String jobType) {
		this.jobType = jobType;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getHdfsPath() {
		return hdfsPath;
	}
	public void setHdfsPath(String hdfsPath) {
		this.hdfsPath = hdfsPath;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getJarFilePath() {
		return jarFilePath;
	}
	public void setJarFilePath(String jarFilePath) {
		this.jarFilePath = jarFilePath;
	}
	public String getFrequencyType() {
		return frequencyType;
	}
	public void setFrequencyType(String frequencyType) {
		this.frequencyType = frequencyType;
	}
	public String getQueueName() {
		return queueName;
	}
	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	
	
	
	

}