package com.nspl.app.web.rest.dto;

import java.time.LocalDate;
import java.time.ZonedDateTime;

public class SrcProfileConnectionDetailsDTO {
	
	
	private Long profileId;
	private String idForDisplay;
	private String sourceProfileName;
	private String profileDescription;
	private ZonedDateTime startDate;
	private ZonedDateTime endDate;
	private Boolean enabledFlag;
	private Long connectionId;
	private boolean endDated =false;
	private String name;
	private String connectionDescription;
	private String protocol;
	private String clientKey;
	private String clientSecret;
	private String authEndPointUrl;
	private String tokenEndPointUrl;
	private String callBackUrl;
	private String host;
	private String userName;
	private String jhiPassword;
	private String connectionType;
	private String accessToken;
	private String port;
	private ZonedDateTime createdDate;
	private Long createdBy;
	private int totalCount;
	

	public Long getId() {
		return profileId;
	}

	public void setId(Long id) {
		profileId = id;
	}

	public String getSourceProfileName() {
		return sourceProfileName;
	}

	public void setSourceProfileName(String sourceProfileName) {
		this.sourceProfileName = sourceProfileName;
	}

	public String getProfileDescription() {
		return profileDescription;
	}

	public void setProfileDescription(String profileDescription) {
		this.profileDescription = profileDescription;
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

	public Long getConnectionId() {
		return connectionId;
	}

	public void setConnectionId(Long connectionId) {
		this.connectionId = connectionId;
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getConnectionDescription() {
		return connectionDescription;
	}
	public void setConnectionDescription(String connectionDescription) {
		this.connectionDescription = connectionDescription;
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
	public String getAuthEndPointUrl() {
		return authEndPointUrl;
	}
	public void setAuthEndPointUrl(String authEndPointUrl) {
		this.authEndPointUrl = authEndPointUrl;
	}
	public String getTokenEndPointUrl() {
		return tokenEndPointUrl;
	}
	public void setTokenEndPointUrl(String tokenEndPointUrl) {
		this.tokenEndPointUrl = tokenEndPointUrl;
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
	public String getJhiPassword() {
		return jhiPassword;
	}
	public void setJhiPassword(String jhiPassword) {
		this.jhiPassword = jhiPassword;
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

	public boolean isEndDated() {
		return endDated;
	}

	public void setEndDated(boolean endDated) {
		this.endDated = endDated;
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

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public String getIdForDisplay() {
		return idForDisplay;
	}

	public void setIdForDisplay(String idForDisplay) {
		this.idForDisplay = idForDisplay;
	}
	
	
}