package com.nspl.app.web.rest.dto;

import java.math.BigDecimal;

import org.apache.spark.sql.types.Decimal;

public class JeLinesDTO {
/*	private Long id;
	private Long jeBatchId;
	private Long rowId;
	private int lineNum;
	private String descriptionAttribute;
*/	private String codeCombination;
/*	private String currency;
	private BigDecimal debitAmount;
	private BigDecimal creditAmount;
*/	private BigDecimal accountedDebit;
	private BigDecimal accountedCredit;
	private BigDecimal debitAmount;
	private BigDecimal creditAmount;
/*	private String comments;*/
	private String descriptionAttribute;
	
	public String getCodeCombination() {
		return codeCombination;
	}
	public void setCodeCombination(String codeCombination) {
		this.codeCombination = codeCombination;
	}
	public BigDecimal getAccountedDebit() {
		return accountedDebit;
	}
	public void setAccountedDebit(BigDecimal accountedDebit) {
		this.accountedDebit = accountedDebit;
	}
	public BigDecimal getAccountedCredit() {
		return accountedCredit;
	}
	public void setAccountedCredit(BigDecimal accountedCredit) {
		this.accountedCredit = accountedCredit;
	}
	public String getDescriptionAttribute() {
		return descriptionAttribute;
	}
	public void setDescriptionAttribute(String descriptionAttribute) {
		this.descriptionAttribute = descriptionAttribute;
	}
	public BigDecimal getDebitAmount() {
		return debitAmount;
	}
	public void setDebitAmount(BigDecimal debitAmount) {
		this.debitAmount = debitAmount;
	}
	public BigDecimal getCreditAmount() {
		return creditAmount;
	}
	public void setCreditAmount(BigDecimal creditAmount) {
		this.creditAmount = creditAmount;
	}
	
}
