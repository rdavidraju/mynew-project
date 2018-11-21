package com.nspl.app.web.rest.dto;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

public class JournalHeaderDTO {

	private Long id;
	private Long tenantId;
	private Long jeTempId;
	private String templateName;
	private String jeBatchName;
	private LocalDate jeBatchDate;
	private LocalDate glDate;
	private String currency;
	private String period;
	private String conversionType;
	private String category;
	private String source;
	private String ledgerName;
	private int batchTotal;
	private LocalDate runDate;
	private String submittedBy;
	private Long createdBy;
	private Long lastUpdatedBy;
	private ZonedDateTime createdDate;
	private ZonedDateTime lastUpdatedDate;
	private List<JeLinesDTO> jeLinesDTO;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getTenantId() {
		return tenantId;
	}
	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
	public Long getJeTempId() {
		return jeTempId;
	}
	public void setJeTempId(Long jeTempId) {
		this.jeTempId = jeTempId;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public String getJeBatchName() {
		return jeBatchName;
	}
	public void setJeBatchName(String jeBatchName) {
		this.jeBatchName = jeBatchName;
	}
	public LocalDate getJeBatchDate() {
		return jeBatchDate;
	}
	public void setJeBatchDate(LocalDate jeBatchDate) {
		this.jeBatchDate = jeBatchDate;
	}
	public LocalDate getGlDate() {
		return glDate;
	}
	public void setGlDate(LocalDate glDate) {
		this.glDate = glDate;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public String getConversionType() {
		return conversionType;
	}
	public void setConversionType(String conversionType) {
		this.conversionType = conversionType;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getLedgerName() {
		return ledgerName;
	}
	public void setLedgerName(String ledgerName) {
		this.ledgerName = ledgerName;
	}
	public int getBatchTotal() {
		return batchTotal;
	}
	public void setBatchTotal(int batchTotal) {
		this.batchTotal = batchTotal;
	}
	public LocalDate getRunDate() {
		return runDate;
	}
	public void setRunDate(LocalDate runDate) {
		this.runDate = runDate;
	}
	public String getSubmittedBy() {
		return submittedBy;
	}
	public void setSubmittedBy(String submittedBy) {
		this.submittedBy = submittedBy;
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
	public ZonedDateTime getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(ZonedDateTime createdDate) {
		this.createdDate = createdDate;
	}
	public ZonedDateTime getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	public void setLastUpdatedDate(ZonedDateTime lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
	public List<JeLinesDTO> getJeLinesDTO() {
		return jeLinesDTO;
	}
	public void setJeLinesDTO(List<JeLinesDTO> jeLinesDTO) {
		this.jeLinesDTO = jeLinesDTO;
	}

}
