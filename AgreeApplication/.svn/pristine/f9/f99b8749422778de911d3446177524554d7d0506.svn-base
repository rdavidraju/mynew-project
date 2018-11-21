package com.nspl.app.jbpm.web.rest.dto;
import com.nspl.app.domain.util.CustomDateTimeDeserializer;
import com.nspl.app.domain.util.CustomDateTimeSerializer;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class ProcessInstanceInfoDTO {

	private Long instanceId;
	
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@JsonSerialize(using = CustomDateTimeSerializer.class)
	@JsonDeserialize(using = CustomDateTimeDeserializer.class)
	private DateTime lastModificationDatSe;
	
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@JsonSerialize(using = CustomDateTimeSerializer.class)
	@JsonDeserialize(using = CustomDateTimeDeserializer.class)
	private DateTime lastReadDate;
	
	private String processId;
	private byte[] processInstanceByteArray;
	
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@JsonSerialize(using = CustomDateTimeSerializer.class)
	@JsonDeserialize(using = CustomDateTimeDeserializer.class)
	private DateTime startDate;
	
	private int state;
	private int optlook;
	private String processName;
	
	private DateTime lastModificationDate;
	public Long getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
	}
	public DateTime getLastModificationDate() {
		return lastModificationDate;
	}
	public void setLastModificationDate(DateTime lastModificationDate) {
		this.lastModificationDate = lastModificationDate;
	}
	public DateTime getLastReadDate() {
		return lastReadDate;
	}
	public void setLastReadDate(DateTime lastReadDate) {
		this.lastReadDate = lastReadDate;
	}
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public byte[] getProcessInstanceByteArray() {
		return processInstanceByteArray;
	}
	public void setProcessInstanceByteArray(byte[] processInstanceByteArray) {
		this.processInstanceByteArray = processInstanceByteArray;
	}
	public DateTime getStartDate() {
		return startDate;
	}
	public void setStartDate(DateTime startDate) {
		this.startDate = startDate;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getOptlook() {
		return optlook;
	}
	public void setOptlook(int optlook) {
		this.optlook = optlook;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	
}
