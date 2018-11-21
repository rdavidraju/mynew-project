package com.nspl.app.web.rest.dto;

import java.util.List;

public class ManualAccountingDTO {
//	RowIdCreditsDebitsDTO
	private String viewId;
	private String groupId;
	private List<Long> rows;
	private String coaRef;
	private String ledgerRef;
	private String categoryRef;
	private String sourceRef;
	private Long enteredCurrency;
	private String accountedCurrency;
	private Long fxRateId;
	private String conversionDate;
	private List<CreditLineDTO> credits;
	private List<DebitLineDTO> debits;
	public String getViewId() {
		return viewId;
	}
	public void setViewId(String viewId) {
		this.viewId = viewId;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public List<Long> getRows() {
		return rows;
	}
	public void setRows(List<Long> rows) {
		this.rows = rows;
	}
	public String getCoaRef() {
		return coaRef;
	}
	public void setCoaRef(String coaRef) {
		this.coaRef = coaRef;
	}
	public String getLedgerRef() {
		return ledgerRef;
	}
	public void setLedgerRef(String ledgerRef) {
		this.ledgerRef = ledgerRef;
	}
	public String getCategoryRef() {
		return categoryRef;
	}
	public void setCategoryRef(String categoryRef) {
		this.categoryRef = categoryRef;
	}
	public String getSourceRef() {
		return sourceRef;
	}
	public void setSourceRef(String sourceRef) {
		this.sourceRef = sourceRef;
	}
	public Long getEnteredCurrency() {
		return enteredCurrency;
	}
	public void setEnteredCurrency(Long enteredCurrency) {
		this.enteredCurrency = enteredCurrency;
	}
	public String getAccountedCurrency() {
		return accountedCurrency;
	}
	public void setAccountedCurrency(String accountedCurrency) {
		this.accountedCurrency = accountedCurrency;
	}
	public Long getFxRateId() {
		return fxRateId;
	}
	public void setFxRateId(Long fxRateId) {
		this.fxRateId = fxRateId;
	}
	public String getConversionDate() {
		return conversionDate;
	}
	public void setConversionDate(String conversionDate) {
		this.conversionDate = conversionDate;
	}
	public List<CreditLineDTO> getCredits() {
		return credits;
	}
	public void setCredits(List<CreditLineDTO> credits) {
		this.credits = credits;
	}
	public List<DebitLineDTO> getDebits() {
		return debits;
	}
	public void setDebits(List<DebitLineDTO> debits) {
		this.debits = debits;
	}
	


}
