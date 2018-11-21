package com.nspl.app.web.rest.dto;

import java.util.HashMap;
import java.util.List;

public class DebitLineDTO {

	private Long amountColId;
	private List<HashMap> debitSegs;
	private String lineTypeDetial;
	
	public Long getAmountColId() {
		return amountColId;
	}
	public void setAmountColId(Long amountColId) {
		this.amountColId = amountColId;
	}
	public List<HashMap> getDebitSegs() {
		return debitSegs;
	}
	public void setDebitSegs(List<HashMap> debitSegs) {
		this.debitSegs = debitSegs;
	}
	public String getLineTypeDetial() {
		return lineTypeDetial;
	}
	public void setLineTypeDetial(String lineTypeDetial) {
		this.lineTypeDetial = lineTypeDetial;
	}
	
	
}
