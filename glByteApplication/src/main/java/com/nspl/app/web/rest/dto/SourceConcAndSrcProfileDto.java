package com.nspl.app.web.rest.dto;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

//import javax.persistence.Column;

import com.nspl.app.domain.SourceConnectionDetails;
import com.nspl.app.domain.SourceProfiles;

public class SourceConcAndSrcProfileDto {
	
	private Long id;
    private String name;
    private String description;
    private String protocol;
    private String clientKey;
    private String clientSecret;
    private String authEndpointUrl;
    private String tokenEndpointUrl;
    private String callBackUrl;
    private String host; 
    private String userName;
    private String password;
    private String url;
    private Long tenantId;
    private ZonedDateTime createdDate;
    private Long createdBy;
    private Long lastUpdatedBy;
    private ZonedDateTime lastUpdatedDate;
    private String connectionType;
    private String accessToken;
    private String port;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private Boolean enabledFlag;
	private List<SourceProfiles> sourceProfileList;
	private String idForDisplay;
	private int activeProfileCnt;
	
	/*public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}*/
	
	
	public String getName() {
		return name;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getClientKey() {
		return clientKey;
	}
	public void setClientKey(String clientKey) {
		this.clientKey = clientKey;
	}
	public String getClientSecret() {
		return clientSecret;
	}
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	public String getAuthEndpointUrl() {
		return authEndpointUrl;
	}
	public void setAuthEndpointUrl(String authEndpointUrl) {
		this.authEndpointUrl = authEndpointUrl;
	}
	public String getTokenEndpointUrl() {
		return tokenEndpointUrl;
	}
	public void setTokenEndpointUrl(String tokenEndpointUrl) {
		this.tokenEndpointUrl = tokenEndpointUrl;
	}
	public String getCallBackUrl() {
		return callBackUrl;
	}
	public void setCallBackUrl(String callBackUrl) {
		this.callBackUrl = callBackUrl;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Long getTenantId() {
		return tenantId;
	}
	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
	public ZonedDateTime getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(ZonedDateTime createdDate) {
		this.createdDate = createdDate;
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
	public ZonedDateTime getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	public void setLastUpdatedDate(ZonedDateTime lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
	public String getConnectionType() {
		return connectionType;
	}
	public void setConnectionType(String connectionType) {
		this.connectionType = connectionType;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
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
	public Boolean getEnabledFlag() {
		return enabledFlag;
	}
	public void setEnabledFlag(Boolean enabledFlag) {
		this.enabledFlag = enabledFlag;
	}
	public List<SourceProfiles> getSourceProfileList() {
		return sourceProfileList;
	}
	public void setSourceProfileList(List<SourceProfiles> sourceProfileList) {
		this.sourceProfileList = sourceProfileList;
	}
	public String getIdForDisplay() {
		return idForDisplay;
	}
	public void setIdForDisplay(String idForDisplay) {
		this.idForDisplay = idForDisplay;
	}
	
	
	public int getActiveProfileCnt() {
		return activeProfileCnt;
	}
	public void setActiveProfileCnt(int activeProfileCnt) {
		this.activeProfileCnt = activeProfileCnt;
	}
	@Override
	public String toString() {
		return "SourceConcAndSrcProfileDto [id=" + id + ", name=" + name
				+ ", description=" + description + ", protocol=" + protocol
				+ ", clientKey=" + clientKey + ", clientSecret=" + clientSecret
				+ ", authEndpointUrl=" + authEndpointUrl
				+ ", tokenEndpointUrl=" + tokenEndpointUrl + ", callBackUrl="
				+ callBackUrl + ", host=" + host + ", userName=" + userName
				+ ", password=" + password + ", url=" + url + ", tenantId="
				+ tenantId + ", createdDate=" + createdDate + ", createdBy="
				+ createdBy + ", lastUpdatedBy=" + lastUpdatedBy
				+ ", lastUpdatedDate=" + lastUpdatedDate + ", connectionType="
				+ connectionType + ", accessToken=" + accessToken + ", port="
				+ port + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", enabledFlag=" + enabledFlag + ", sourceProfileList="
				+ sourceProfileList + "]";
	}
	
	

}
