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
 * A ReconciliationResult.
 */
@Entity
@Table(name = "t_reconciliation_result")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "reconciliationresult")
public class ReconciliationResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "original_row_id")
    private Long originalRowId;

    @Column(name = "original_view_id")
    private Long originalViewId;

    @Column(name = "original_view")
    private String originalView;

    @Column(name = "target_row_id")
    private Long targetRowId;

    @Column(name = "target_view_id")
    private Long targetViewId;

    @Column(name = "target_view")
    private String targetView;

    @Column(name = "recon_reference")
    private String reconReference;

    @Column(name = "reconciliation_rule_name")
    private String reconciliationRuleName;

    @Column(name = "reconciliation_rule_group_id")
    private Long reconciliationRuleGroupId;
    
    @Column(name = "reconciliation_rule_id")
    private Long reconciliationRuleId;
    
    @Column(name = "amount", precision=15, scale=2)
    private BigDecimal amount;

    @Column(name = "original_amount", precision=30, scale=4)
    private BigDecimal originalAmount;
    
    @Column(name = "tolerance_amount", precision=30, scale=4)
    private BigDecimal toleranceAmount;

    @Column(name = "target_amount", precision=30, scale=4)
    private BigDecimal targetAmount;
    
    @Column(name = "reconciliation_user_id")
    private Long reconciliationUserId;

    @Column(name = "recon_job_reference")
    private String reconJobReference;

    @Column(name = "reconciled_date")
    private ZonedDateTime reconciledDate;
    
    @Column(name = "recon_status")
    private String reconStatus;

    @Column(name = "current_record_flag")
    private Boolean currentRecordFlag;
    
    @Column(name = "tenant_id")
    private Long tenantId;
    
    @Column(name = "approval_group_id")
    private Long approvalGroupId;
    
    @Column(name = "approval_rule_id")
    private Long approvalRuleId;
    
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

	public void setFinalActionDate(ZonedDateTime finalActionDate) {
		this.finalActionDate = finalActionDate;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOriginalRowId() {
        return originalRowId;
    }

    public ReconciliationResult originalRowId(Long originalRowId) {
        this.originalRowId = originalRowId;
        return this;
    }

    public void setOriginalRowId(Long originalRowId) {
        this.originalRowId = originalRowId;
    }

    public Long getOriginalViewId() {
        return originalViewId;
    }

    public ReconciliationResult originalViewId(Long originalViewId) {
        this.originalViewId = originalViewId;
        return this;
    }

    public void setOriginalViewId(Long originalViewId) {
        this.originalViewId = originalViewId;
    }

    public String getOriginalView() {
        return originalView;
    }

    public ReconciliationResult originalView(String originalView) {
        this.originalView = originalView;
        return this;
    }

    public void setOriginalView(String originalView) {
        this.originalView = originalView;
    }

    public Long getTargetRowId() {
        return targetRowId;
    }

    public ReconciliationResult targetRowId(Long targetRowId) {
        this.targetRowId = targetRowId;
        return this;
    }

    public void setTargetRowId(Long targetRowId) {
        this.targetRowId = targetRowId;
    }

    public Long getTargetViewId() {
        return targetViewId;
    }

    public ReconciliationResult targetViewId(Long targetViewId) {
        this.targetViewId = targetViewId;
        return this;
    }

    public void setTargetViewId(Long targetViewId) {
        this.targetViewId = targetViewId;
    }

    public String getTargetView() {
        return targetView;
    }

    public ReconciliationResult targetView(String targetView) {
        this.targetView = targetView;
        return this;
    }

    public void setTargetView(String targetView) {
        this.targetView = targetView;
    }

    public String getReconReference() {
        return reconReference;
    }

    public ReconciliationResult reconReference(String reconReference) {
        this.reconReference = reconReference;
        return this;
    }

    public void setReconReference(String reconReference) {
        this.reconReference = reconReference;
    }

    public String getReconciliationRuleName() {
        return reconciliationRuleName;
    }

    public ReconciliationResult reconciliationRuleName(String reconciliationRuleName) {
        this.reconciliationRuleName = reconciliationRuleName;
        return this;
    }

    public void setReconciliationRuleName(String reconciliationRuleName) {
        this.reconciliationRuleName = reconciliationRuleName;
    }

    public Long getReconciliationRuleGroupId() {
		return reconciliationRuleGroupId;
	}

	public void setReconciliationRuleGroupId(Long reconciliationRuleGroupId) {
		this.reconciliationRuleGroupId = reconciliationRuleGroupId;
	}

	public Long getReconciliationRuleId() {
        return reconciliationRuleId;
    }

    public ReconciliationResult reconciliationRuleId(Long reconciliationRuleId) {
        this.reconciliationRuleId = reconciliationRuleId;
        return this;
    }

    public void setReconciliationRuleId(Long reconciliationRuleId) {
        this.reconciliationRuleId = reconciliationRuleId;
    }

    public Long getReconciliationUserId() {
        return reconciliationUserId;
    }

    public ReconciliationResult reconciliationUserId(Long reconciliationUserId) {
        this.reconciliationUserId = reconciliationUserId;
        return this;
    }

    public void setReconciliationUserId(Long reconciliationUserId) {
        this.reconciliationUserId = reconciliationUserId;
    }

    public String getReconJobReference() {
        return reconJobReference;
    }

    public ReconciliationResult reconJobReference(String reconJobReference) {
        this.reconJobReference = reconJobReference;
        return this;
    }

    public void setReconJobReference(String reconJobReference) {
        this.reconJobReference = reconJobReference;
    }

    public ZonedDateTime getReconciledDate() {
        return reconciledDate;
    }

    public ReconciliationResult reconciledDate(ZonedDateTime reconciledDate) {
        this.reconciledDate = reconciledDate;
        return this;
    }

    public void setReconciledDate(ZonedDateTime reconciledDate) {
        this.reconciledDate = reconciledDate;
    }

    public String getReconStatus() {
		return reconStatus;
	}

	public void setReconStatus(String reconStatus) {
		this.reconStatus = reconStatus;
	}

	public Boolean getCurrentRecordFlag() {
		return currentRecordFlag;
	}

	public void setCurrentRecordFlag(Boolean currentRecordFlag) {
		this.currentRecordFlag = currentRecordFlag;
	}

	public Long getTenantId() {
        return tenantId;
    }

    public ReconciliationResult tenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
    
    public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getOriginalAmount() {
		return originalAmount;
	}

	public void setOriginalAmount(BigDecimal originalAmount) {
		this.originalAmount = originalAmount;
	}

	public BigDecimal getTargetAmount() {
		return targetAmount;
	}

	public void setTargetAmount(BigDecimal targetAmount) {
		this.targetAmount = targetAmount;
	}

	public BigDecimal getToleranceAmount() {
		return toleranceAmount;
	}

	public void setToleranceAmount(BigDecimal toleranceAmount) {
		this.toleranceAmount = toleranceAmount;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReconciliationResult reconciliationResult = (ReconciliationResult) o;
        if (reconciliationResult.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, reconciliationResult.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
	public String toString() {
		return "ReconciliationResult [id=" + id + ", originalRowId="
				+ originalRowId + ", originalViewId=" + originalViewId
				+ ", originalView=" + originalView + ", targetRowId="
				+ targetRowId + ", targetViewId=" + targetViewId
				+ ", targetView=" + targetView + ", reconReference="
				+ reconReference + ", reconciliationRuleName="
				+ reconciliationRuleName + ", reconciliationRuleGroupId="
				+ reconciliationRuleGroupId + ", reconciliationRuleId="
				+ reconciliationRuleId + ", amount=" + amount
				+ ", originalAmount=" + originalAmount + ", toleranceAmount="
				+ toleranceAmount + ", targetAmount=" + targetAmount
				+ ", reconciliationUserId=" + reconciliationUserId
				+ ", reconJobReference=" + reconJobReference
				+ ", reconciledDate=" + reconciledDate + ", reconStatus="
				+ reconStatus + ", currentRecordFlag=" + currentRecordFlag
				+ ", tenantId=" + tenantId + ", approvalGroupId="
				+ approvalGroupId + ", approvalRuleId=" + approvalRuleId
				+ ", approvalInitiationDate=" + approvalInitiationDate
				+ ", approvalBatchId=" + approvalBatchId + ", apprRef01="
				+ apprRef01 + ", apprRef02=" + apprRef02 + ", apprRef03="
				+ apprRef03 + ", apprRef04=" + apprRef04 + ", apprRef05="
				+ apprRef05 + ", apprRef06=" + apprRef06 + ", apprRef07="
				+ apprRef07 + ", apprRef08=" + apprRef08 + ", apprRef09="
				+ apprRef09 + ", apprRef10=" + apprRef10 + ", apprRef11="
				+ apprRef11 + ", apprRef12=" + apprRef12 + ", apprRef13="
				+ apprRef13 + ", apprRef14=" + apprRef14 + ", apprRef15="
				+ apprRef15 + ", finalStatus=" + finalStatus
				+ ", finalActionDate=" + finalActionDate + "]";
	}
}
