package com.nspl.app.web.rest.dto;

import java.util.List;

public class RowIdCreditDebitDTO {
	
	private Long rowId;
	private String credit;
	private String debit;
	private String coaRef;
	
	public Long getRowId() {
		return rowId;
	}
	public void setRowId(Long rowId) {
		this.rowId = rowId;
	}
	public String getCredit() {
		return credit;
	}
	public void setCredit(String credit) {
		this.credit = credit;
	}
	public String getDebit() {
		return debit;
	}
	public void setDebit(String debit) {
		this.debit = debit;
	}
	public String getCoaRef() {
		return coaRef;
	}
	public void setCoaRef(String coaRef) {
		this.coaRef = coaRef;
	}
		
}
