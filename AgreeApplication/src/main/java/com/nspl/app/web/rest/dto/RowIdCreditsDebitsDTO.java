package com.nspl.app.web.rest.dto;

import java.util.List;

public class RowIdCreditsDebitsDTO {
	
	private Long rowId;
	private List<CreditLineDTO> credits;
	private List<DebitLineDTO> debits;
	private Long coaRef;
	private Long ledgerRef;
	private String categoryRef;
	private String sourceRef;
	private Long enteredCurrency;
	private String accountedCurrency;
	private Long fxRateId;
	private String conversionDate;
	public Long getRowId() {
		return rowId;
	}
	public void setRowId(Long rowId) {
		this.rowId = rowId;
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
	public Long getCoaRef() {
		return coaRef;
	}
	public void setCoaRef(Long coaRef) {
		this.coaRef = coaRef;
	}
	public Long getLedgerRef() {
		return ledgerRef;
	}
	public void setLedgerRef(Long ledgerRef) {
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
	
	
		
}
