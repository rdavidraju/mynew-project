package com.nspl.app.web.rest.dto;


public class JournalsResultDTO {
	
	private Long id;
	private String templateName;
	private String jeBatchName;
	private String period;
	private JournalHeaderDTO journalHeader;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public JournalHeaderDTO getJournalHeader() {
		return journalHeader;
	}
	public void setJournalHeader(JournalHeaderDTO journalHeader) {
		this.journalHeader = journalHeader;
	}
	
}
