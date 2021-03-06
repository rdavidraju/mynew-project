package com.nspl.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A AccountingData.
 */
@Entity
@Table(name = "t_accounting_data")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "taccountingdata")
public class AccountingData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "accounted_summary_id")
    private Long accountedSummaryId;
    
    @Column(name = "original_row_id")
    private Long originalRowId;

    @Column(name = "accounting_ref_1")
    private String accountingRef1;

    @Column(name = "accounting_ref_2")
    private String accountingRef2;

    @Column(name = "accounting_ref_3")
    private String accountingRef3;

    @Column(name = "accounting_ref_4")
    private String accountingRef4;

    @Column(name = "accounting_ref_5")
    private String accountingRef5;

    @Column(name = "accounting_ref_6")
    private String accountingRef6;

    @Column(name = "accounting_ref_7")
    private String accountingRef7;

    @Column(name = "accounting_ref_8")
    private String accountingRef8;

    @Column(name = "accounting_ref_9")
    private String accountingRef9;

    @Column(name = "accounting_ref_10")
    private String accountingRef10;

    @Column(name = "ledger_ref")
    private String ledgerRef;

    @Column(name = "ledger_ref_type")
    private String ledgerRefType;
    
    @Column(name = "source_ref")
    private String sourceRef;
    
    @Column(name = "category_ref")
    private String categoryRef;
    
    @Column(name = "currency_ref")
    private String currencyRef;
    
    @Column(name = "amount_col_id")
    private Long amountColId;
    
    @Column(name = "line_type_id")
    private Long lineTypeId;
    
    @Column(name = "line_type_detail")
    private String lineTypeDetail;
    
    @Column(name = "line_type")
    private String lineType;

    @Column(name = "coa_ref")
    private String coaRef;

    @Column(name = "job_reference")
    private String jobReference;
    
    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "last_updated_by")
    private Long lastUpdatedBy;

    @Column(name = "last_updated_date")
    private ZonedDateTime lastUpdatedDate;
    
    @Column(name = "status")
    private String status;
    
    @Column(name = "acct_group_id")
    private Long acctGroupId;
    
    @Column(name = "acct_rule_id")
    private Long acctRuleId;
    
    @Column(name = "original_view_id")
    private Long originalViewId;
    
    @Column(name = "reverse_ref_id")
    private Long reverseRefId;
    
	@Column(name = "approval_group_id")
    private Long approvalGroupId;
    
    @Column(name = "approval_rule_id")
    private Long approvalRuleId;
    
    @Column(name = "je_line_id")
    private Long jeLineId;
    
    @Column(name = "approval_initiation_date")
    private ZonedDateTime approvalInitiationDate;
    
    @Column(name = "approval_batch_id")
    private Long approvalBatchId;
    
    @Column(name = "appr_ref_01")
    private String apprRef01;
    
    @Column(name = "appr_ref_02")
    private String apprRef02;
    
    @Column(name = "appr_ref_03")
    private String apprRef03;
    
    @Column(name = "appr_ref_04")
    private String apprRef04;
    
    @Column(name = "appr_ref_05")
    private String apprRef05;
    
    @Column(name = "appr_ref_06")
    private String apprRef06;
    
    @Column(name = "appr_ref_07")
    private String apprRef07;
    
    @Column(name = "appr_ref_08")
    private String apprRef08;
    
    @Column(name = "appr_ref_09")
    private String apprRef09;
    
    @Column(name = "appr_ref_10")
    private String apprRef10;
    
    @Column(name = "appr_ref_11")
    private String apprRef11;
    
    @Column(name = "appr_ref_12")
    private String apprRef12;
    
    @Column(name = "appr_ref_13")
    private String apprRef13;
    
    @Column(name = "appr_ref_14")
    private String apprRef14;
    
    @Column(name = "appr_ref_15")
    private String apprRef15;
    
    @Column(name = "final_status")
    private String finalStatus;
    
    @Column(name = "final_action_date")
    private ZonedDateTime finalActionDate;
    
    @Column(name = "amount", precision=15, scale=5)
    private BigDecimal amount;
    
    @Column(name = "fx_rate", precision=5, scale=2)
    private BigDecimal fxRate;
    
    @Column(name = "fx_rate_id")
    private Long fxRateId;
    
    @Column(name = "accounted_amount", precision=15, scale=5)
    private BigDecimal accountedAmount;
    
    @Column(name = "ledger_currency")
    private String ledgerCurrency;
    
    @Column(name = "accounted_date")
    private ZonedDateTime accountedDate;
    
    @Column(name = "transaction_date_fx")
    private ZonedDateTime transactionDateFx;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountedSummaryId() {
		return accountedSummaryId;
	}

	public void setAccountedSummaryId(Long accountedSummaryId) {
		this.accountedSummaryId = accountedSummaryId;
	}

	public Long getTenantId() {
        return tenantId;
    }

    public AccountingData tenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getOriginalRowId() {
        return originalRowId;
    }

    public AccountingData originalRowId(Long originalRowId) {
        this.originalRowId = originalRowId;
        return this;
    }

    public void setOriginalRowId(Long originalRowId) {
        this.originalRowId = originalRowId;
    }

    public Long getJeLineId() {
		return jeLineId;
	}

	public void setJeLineId(Long jeLineId) {
		this.jeLineId = jeLineId;
	}

	public String getAccountingRef1() {
        return accountingRef1;
    }

    public AccountingData accountingRef1(String accountingRef1) {
        this.accountingRef1 = accountingRef1;
        return this;
    }

    public void setAccountingRef1(String accountingRef1) {
        this.accountingRef1 = accountingRef1;
    }

    public String getAccountingRef2() {
        return accountingRef2;
    }

    public AccountingData accountingRef2(String accountingRef2) {
        this.accountingRef2 = accountingRef2;
        return this;
    }

    public void setAccountingRef2(String accountingRef2) {
        this.accountingRef2 = accountingRef2;
    }

    public String getAccountingRef3() {
        return accountingRef3;
    }

    public AccountingData accountingRef3(String accountingRef3) {
        this.accountingRef3 = accountingRef3;
        return this;
    }

    public void setAccountingRef3(String accountingRef3) {
        this.accountingRef3 = accountingRef3;
    }

    public String getAccountingRef4() {
        return accountingRef4;
    }

    public AccountingData accountingRef4(String accountingRef4) {
        this.accountingRef4 = accountingRef4;
        return this;
    }

    public void setAccountingRef4(String accountingRef4) {
        this.accountingRef4 = accountingRef4;
    }

    public String getAccountingRef5() {
        return accountingRef5;
    }

    public AccountingData accountingRef5(String accountingRef5) {
        this.accountingRef5 = accountingRef5;
        return this;
    }

    public void setAccountingRef5(String accountingRef5) {
        this.accountingRef5 = accountingRef5;
    }

    public String getAccountingRef6() {
        return accountingRef6;
    }

    public AccountingData accountingRef6(String accountingRef6) {
        this.accountingRef6 = accountingRef6;
        return this;
    }

    public void setAccountingRef6(String accountingRef6) {
        this.accountingRef6 = accountingRef6;
    }

    public String getAccountingRef7() {
        return accountingRef7;
    }

    public AccountingData accountingRef7(String accountingRef7) {
        this.accountingRef7 = accountingRef7;
        return this;
    }

    public void setAccountingRef7(String accountingRef7) {
        this.accountingRef7 = accountingRef7;
    }

    public String getAccountingRef8() {
        return accountingRef8;
    }

    public AccountingData accountingRef8(String accountingRef8) {
        this.accountingRef8 = accountingRef8;
        return this;
    }

    public void setAccountingRef8(String accountingRef8) {
        this.accountingRef8 = accountingRef8;
    }

    public String getAccountingRef9() {
        return accountingRef9;
    }

    public AccountingData accountingRef9(String accountingRef9) {
        this.accountingRef9 = accountingRef9;
        return this;
    }

    public void setAccountingRef9(String accountingRef9) {
        this.accountingRef9 = accountingRef9;
    }

    public String getAccountingRef10() {
        return accountingRef10;
    }

    public AccountingData accountingRef10(String accountingRef10) {
        this.accountingRef10 = accountingRef10;
        return this;
    }

    public void setAccountingRef10(String accountingRef10) {
        this.accountingRef10 = accountingRef10;
    }

    public String getLedgerRef() {
        return ledgerRef;
    }

    public AccountingData ledgerRef(String ledgerRef) {
        this.ledgerRef = ledgerRef;
        return this;
    }

    public void setLedgerRef(String ledgerRef) {
        this.ledgerRef = ledgerRef;
    }

    public String getLedgerRefType() {
		return ledgerRefType;
	}

	public void setLedgerRefType(String ledgerRefType) {
		this.ledgerRefType = ledgerRefType;
	}

	public String getSourceRef() {
		return sourceRef;
	}

	public void setSourceRef(String sourceRef) {
		this.sourceRef = sourceRef;
	}

	public String getCategoryRef() {
		return categoryRef;
	}

	public void setCategoryRef(String categoryRef) {
		this.categoryRef = categoryRef;
	}

	public Long getAmountColId() {
		return amountColId;
	}

	public void setAmountColId(Long amountColId) {
		this.amountColId = amountColId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getCurrencyRef() {
        return currencyRef;
    }

    public AccountingData currencyRef(String currencyRef) {
        this.currencyRef = currencyRef;
        return this;
    }

    public void setCurrencyRef(String currencyRef) {
        this.currencyRef = currencyRef;
    }

    public Long getLineTypeId() {
		return lineTypeId;
	}

	public void setLineTypeId(Long lineTypeId) {
		this.lineTypeId = lineTypeId;
	}

	public String getLineTypeDetail() {
		return lineTypeDetail;
	}

	public void setLineTypeDetail(String lineTypeDetail) {
		this.lineTypeDetail = lineTypeDetail;
	}

	public String getLineType() {
        return lineType;
    }

    public AccountingData lineType(String lineType) {
        this.lineType = lineType;
        return this;
    }

    public void setLineType(String lineType) {
        this.lineType = lineType;
    }

    public String getCoaRef() {
        return coaRef;
    }

    public AccountingData coaRef(String coaRef) {
        this.coaRef = coaRef;
        return this;
    }

    public void setCoaRef(String coaRef) {
        this.coaRef = coaRef;
    }

    public String getJobReference() {
		return jobReference;
	}

	public void setJobReference(String jobReference) {
		this.jobReference = jobReference;
	}

	public Long getCreatedBy() {
        return createdBy;
    }

    public AccountingData createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public AccountingData createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public AccountingData lastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public ZonedDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public AccountingData lastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
        return this;
    }

    public void setLastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getAcctGroupId() {
		return acctGroupId;
	}

	public void setAcctGroupId(Long acctGroupId) {
		this.acctGroupId = acctGroupId;
	}

	public Long getAcctRuleId() {
		return acctRuleId;
	}

	public void setAcctRuleId(Long acctRuleId) {
		this.acctRuleId = acctRuleId;
	}

	public Long getOriginalViewId() {
		return originalViewId;
	}

	public void setOriginalViewId(Long originalViewId) {
		this.originalViewId = originalViewId;
	}

	public Long getApprovalGroupId() {
		return approvalGroupId;
	}

	public void setApprovalGroupId(Long approvalGroupId) {
		this.approvalGroupId = approvalGroupId;
	}

	public Long getApprovalRuleId() {
		return approvalRuleId;
	}

	public void setApprovalRuleId(Long approvalRuleId) {
		this.approvalRuleId = approvalRuleId;
	}

	public ZonedDateTime getApprovalInitiationDate() {
		return approvalInitiationDate;
	}

	public void setApprovalInitiationDate(ZonedDateTime approvalInitiationDate) {
		this.approvalInitiationDate = approvalInitiationDate;
	}

	public Long getApprovalBatchId() {
		return approvalBatchId;
	}

	public void setApprovalBatchId(Long approvalBatchId) {
		this.approvalBatchId = approvalBatchId;
	}

	public String getApprRef01() {
		return apprRef01;
	}

	public void setApprRef01(String apprRef01) {
		this.apprRef01 = apprRef01;
	}

	public String getApprRef02() {
		return apprRef02;
	}

	public void setApprRef02(String apprRef02) {
		this.apprRef02 = apprRef02;
	}

	public String getApprRef03() {
		return apprRef03;
	}

	public void setApprRef03(String apprRef03) {
		this.apprRef03 = apprRef03;
	}

	public String getApprRef04() {
		return apprRef04;
	}

	public void setApprRef04(String apprRef04) {
		this.apprRef04 = apprRef04;
	}

	public String getApprRef05() {
		return apprRef05;
	}

	public void setApprRef05(String apprRef05) {
		this.apprRef05 = apprRef05;
	}

	public String getApprRef06() {
		return apprRef06;
	}

	public void setApprRef06(String apprRef06) {
		this.apprRef06 = apprRef06;
	}

	public String getApprRef07() {
		return apprRef07;
	}

	public void setApprRef07(String apprRef07) {
		this.apprRef07 = apprRef07;
	}

	public String getApprRef08() {
		return apprRef08;
	}

	public void setApprRef08(String apprRef08) {
		this.apprRef08 = apprRef08;
	}

	public String getApprRef09() {
		return apprRef09;
	}

	public void setApprRef09(String apprRef09) {
		this.apprRef09 = apprRef09;
	}

	public String getApprRef10() {
		return apprRef10;
	}

	public void setApprRef10(String apprRef10) {
		this.apprRef10 = apprRef10;
	}

	public String getApprRef11() {
		return apprRef11;
	}

	public void setApprRef11(String apprRef11) {
		this.apprRef11 = apprRef11;
	}

	public String getApprRef12() {
		return apprRef12;
	}

	public void setApprRef12(String apprRef12) {
		this.apprRef12 = apprRef12;
	}

	public String getApprRef13() {
		return apprRef13;
	}

	public void setApprRef13(String apprRef13) {
		this.apprRef13 = apprRef13;
	}

	public String getApprRef14() {
		return apprRef14;
	}

	public void setApprRef14(String apprRef14) {
		this.apprRef14 = apprRef14;
	}

	public String getApprRef15() {
		return apprRef15;
	}

	public void setApprRef15(String apprRef15) {
		this.apprRef15 = apprRef15;
	}

	public String getFinalStatus() {
		return finalStatus;
	}

	public void setFinalStatus(String finalStatus) {
		this.finalStatus = finalStatus;
	}

	public ZonedDateTime getFinalActionDate() {
		return finalActionDate;
	}

	public Long getReverseRefId() {
		return reverseRefId;
	}

	public void setReverseRefId(Long reverseRefId) {
		this.reverseRefId = reverseRefId;
	}

	public void setFinalActionDate(ZonedDateTime finalActionDate) {
		this.finalActionDate = finalActionDate;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getFxRate() {
		return fxRate;
	}

	public void setFxRate(BigDecimal fxRate) {
		this.fxRate = fxRate;
	}

	public Long getFxRateId() {
		return fxRateId;
	}

	public void setFxRateId(Long fxRateId) {
		this.fxRateId = fxRateId;
	}

	public BigDecimal getAccountedAmount() {
		return accountedAmount;
	}

	public void setAccountedAmount(BigDecimal accountedAmount) {
		this.accountedAmount = accountedAmount;
	}

	public String getLedgerCurrency() {
		return ledgerCurrency;
	}

	public void setLedgerCurrency(String ledgerCurrency) {
		this.ledgerCurrency = ledgerCurrency;
	}

	public ZonedDateTime getAccountedDate() {
		return accountedDate;
	}

	public void setAccountedDate(ZonedDateTime accountedDate) {
		this.accountedDate = accountedDate;
	}

	public ZonedDateTime getTransactionDateFx() {
		return transactionDateFx;
	}

	public void setTransactionDateFx(ZonedDateTime transactionDateFx) {
		this.transactionDateFx = transactionDateFx;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AccountingData accountingData = (AccountingData) o;
        if (accountingData.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), accountingData.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
	public String toString() {
		return "AccountingData [id=" + id + ", tenantId=" + tenantId
				+ ", accountedSummaryId=" + accountedSummaryId
				+ ", originalRowId=" + originalRowId + ", accountingRef1="
				+ accountingRef1 + ", accountingRef2=" + accountingRef2
				+ ", accountingRef3=" + accountingRef3 + ", accountingRef4="
				+ accountingRef4 + ", accountingRef5=" + accountingRef5
				+ ", accountingRef6=" + accountingRef6 + ", accountingRef7="
				+ accountingRef7 + ", accountingRef8=" + accountingRef8
				+ ", accountingRef9=" + accountingRef9 + ", accountingRef10="
				+ accountingRef10 + ", ledgerRef=" + ledgerRef
				+ ", ledgerRefType=" + ledgerRefType + ", sourceRef="
				+ sourceRef + ", categoryRef=" + categoryRef + ", currencyRef="
				+ currencyRef + ", amountColId=" + amountColId
				+ ", lineTypeId=" + lineTypeId + ", lineTypeDetail="
				+ lineTypeDetail + ", lineType=" + lineType + ", coaRef="
				+ coaRef + ", jobReference=" + jobReference + ", createdBy="
				+ createdBy + ", createdDate=" + createdDate
				+ ", lastUpdatedBy=" + lastUpdatedBy + ", lastUpdatedDate="
				+ lastUpdatedDate + ", status=" + status + ", acctGroupId="
				+ acctGroupId + ", acctRuleId=" + acctRuleId
				+ ", originalViewId=" + originalViewId + ", reverseRefId="
				+ reverseRefId + ", approvalGroupId=" + approvalGroupId
				+ ", approvalRuleId=" + approvalRuleId + ", jeLineId="
				+ jeLineId + ", approvalInitiationDate="
				+ approvalInitiationDate + ", approvalBatchId="
				+ approvalBatchId + ", apprRef01=" + apprRef01 + ", apprRef02="
				+ apprRef02 + ", apprRef03=" + apprRef03 + ", apprRef04="
				+ apprRef04 + ", apprRef05=" + apprRef05 + ", apprRef06="
				+ apprRef06 + ", apprRef07=" + apprRef07 + ", apprRef08="
				+ apprRef08 + ", apprRef09=" + apprRef09 + ", apprRef10="
				+ apprRef10 + ", apprRef11=" + apprRef11 + ", apprRef12="
				+ apprRef12 + ", apprRef13=" + apprRef13 + ", apprRef14="
				+ apprRef14 + ", apprRef15=" + apprRef15 + ", finalStatus="
				+ finalStatus + ", finalActionDate=" + finalActionDate
				+ ", amount=" + amount + ", fxRate=" + fxRate + ", fxRateId="
				+ fxRateId + ", accountedAmount=" + accountedAmount
				+ ", ledgerCurrency=" + ledgerCurrency + ", accountedDate="
				+ accountedDate + ", transactionDateFx=" + transactionDateFx
				+ "]";
	}
}
