package com.nspl.app.web.rest.dto;

import java.time.LocalDate;
import java.time.ZonedDateTime;

public class LedgerDefinitionDTO {

		private Long tenantId;
		private String id;
		private String name;
		private String description;
		private String ledgerType;
		private ZonedDateTime startDate;
		private ZonedDateTime endDate;
		private String currency;
		private String calendarId;
		private String status;
		private boolean enabledFlag;
		private Long createdBy;
		private ZonedDateTime createdDate;
		private Long lastUpdatedBy;
		private ZonedDateTime lastUpdatedDate;
		private String coaId;
		
		public Long getTenantId() {
			return tenantId;
		}
		public void setTenantId(Long tenantId) {
			this.tenantId = tenantId;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
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
		public String getLedgerType() {
			return ledgerType;
		}
		public void setLedgerType(String ledgerType) {
			this.ledgerType = ledgerType;
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
		public String getCurrency() {
			return currency;
		}
		public void setCurrency(String currency) {
			this.currency = currency;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public boolean isEnabledFlag() {
			return enabledFlag;
		}
		public void setEnabledFlag(boolean enabledFlag) {
			this.enabledFlag = enabledFlag;
		}
		public Long getCreatedBy() {
			return createdBy;
		}
		public void setCreatedBy(Long createdBy) {
			this.createdBy = createdBy;
		}
		public ZonedDateTime getCreatedDate() {
			return createdDate;
		}
		public void setCreatedDate(ZonedDateTime createdDate) {
			this.createdDate = createdDate;
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
		public String getCalendarId() {
			return calendarId;
		}
		public void setCalendarId(String calendarId) {
			this.calendarId = calendarId;
		}
		public String getCoaId() {
			return coaId;
		}
		public void setCoaId(String coaId) {
			this.coaId = coaId;
		}
		
}