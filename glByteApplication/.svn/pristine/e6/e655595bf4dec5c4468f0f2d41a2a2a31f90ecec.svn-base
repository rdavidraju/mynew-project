package com.nspl.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A ReconciliationDuplicateResult.
 */
@Entity
@Table(name = "t_reconciliation_suggestion_result")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ReconciliationDuplicateResult implements Serializable {

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
    
    @Column(name = "notes")
    private String notes;
    
    @Column(name = "rule_type")
    private String ruleType;

/*    @Column(name = "final_status")
    private String finalStatus;

    @Column(name = "final_action_date")
    private ZonedDateTime finalActionDate;*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOriginalRowId() {
        return originalRowId;
    }

    public ReconciliationDuplicateResult originalRowId(Long originalRowId) {
        this.originalRowId = originalRowId;
        return this;
    }

    public void setOriginalRowId(Long originalRowId) {
        this.originalRowId = originalRowId;
    }

    public Long getOriginalViewId() {
        return originalViewId;
    }

    public ReconciliationDuplicateResult originalViewId(Long originalViewId) {
        this.originalViewId = originalViewId;
        return this;
    }

    public void setOriginalViewId(Long originalViewId) {
        this.originalViewId = originalViewId;
    }

    public String getOriginalView() {
        return originalView;
    }

    public ReconciliationDuplicateResult originalView(String originalView) {
        this.originalView = originalView;
        return this;
    }

    public void setOriginalView(String originalView) {
        this.originalView = originalView;
    }

    public Long getTargetRowId() {
        return targetRowId;
    }

    public ReconciliationDuplicateResult targetRowId(Long targetRowId) {
        this.targetRowId = targetRowId;
        return this;
    }

    public void setTargetRowId(Long targetRowId) {
        this.targetRowId = targetRowId;
    }

    public Long getTargetViewId() {
        return targetViewId;
    }

    public ReconciliationDuplicateResult targetViewId(Long targetViewId) {
        this.targetViewId = targetViewId;
        return this;
    }

    public void setTargetViewId(Long targetViewId) {
        this.targetViewId = targetViewId;
    }

    public String getTargetView() {
        return targetView;
    }

    public ReconciliationDuplicateResult targetView(String targetView) {
        this.targetView = targetView;
        return this;
    }

    public void setTargetView(String targetView) {
        this.targetView = targetView;
    }

    public String getReconReference() {
        return reconReference;
    }

    public ReconciliationDuplicateResult reconReference(String reconReference) {
        this.reconReference = reconReference;
        return this;
    }

    public void setReconReference(String reconReference) {
        this.reconReference = reconReference;
    }

    public String getReconciliationRuleName() {
        return reconciliationRuleName;
    }

    public ReconciliationDuplicateResult reconciliationRuleName(String reconciliationRuleName) {
        this.reconciliationRuleName = reconciliationRuleName;
        return this;
    }

    public void setReconciliationRuleName(String reconciliationRuleName) {
        this.reconciliationRuleName = reconciliationRuleName;
    }

    public Long getReconciliationRuleGroupId() {
        return reconciliationRuleGroupId;
    }

    public ReconciliationDuplicateResult reconciliationRuleGroupId(Long reconciliationRuleGroupId) {
        this.reconciliationRuleGroupId = reconciliationRuleGroupId;
        return this;
    }

    public void setReconciliationRuleGroupId(Long reconciliationRuleGroupId) {
        this.reconciliationRuleGroupId = reconciliationRuleGroupId;
    }

    public Long getReconciliationRuleId() {
        return reconciliationRuleId;
    }

    public ReconciliationDuplicateResult reconciliationRuleId(Long reconciliationRuleId) {
        this.reconciliationRuleId = reconciliationRuleId;
        return this;
    }

    public void setReconciliationRuleId(Long reconciliationRuleId) {
        this.reconciliationRuleId = reconciliationRuleId;
    }

    public Long getReconciliationUserId() {
        return reconciliationUserId;
    }

    public ReconciliationDuplicateResult reconciliationUserId(Long reconciliationUserId) {
        this.reconciliationUserId = reconciliationUserId;
        return this;
    }

    public void setReconciliationUserId(Long reconciliationUserId) {
        this.reconciliationUserId = reconciliationUserId;
    }

    public String getReconJobReference() {
        return reconJobReference;
    }

    public ReconciliationDuplicateResult reconJobReference(String reconJobReference) {
        this.reconJobReference = reconJobReference;
        return this;
    }

    public void setReconJobReference(String reconJobReference) {
        this.reconJobReference = reconJobReference;
    }

    public ZonedDateTime getReconciledDate() {
        return reconciledDate;
    }

    public ReconciliationDuplicateResult reconciledDate(ZonedDateTime reconciledDate) {
        this.reconciledDate = reconciledDate;
        return this;
    }

    public void setReconciledDate(ZonedDateTime reconciledDate) {
        this.reconciledDate = reconciledDate;
    }

    public String getReconStatus() {
        return reconStatus;
    }

    public ReconciliationDuplicateResult reconStatus(String reconStatus) {
        this.reconStatus = reconStatus;
        return this;
    }

    public void setReconStatus(String reconStatus) {
        this.reconStatus = reconStatus;
    }

    public Boolean isCurrentRecordFlag() {
        return currentRecordFlag;
    }

    public ReconciliationDuplicateResult currentRecordFlag(Boolean currentRecordFlag) {
        this.currentRecordFlag = currentRecordFlag;
        return this;
    }

    public void setCurrentRecordFlag(Boolean currentRecordFlag) {
        this.currentRecordFlag = currentRecordFlag;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public ReconciliationDuplicateResult tenantId(Long tenantId) {
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

	public Boolean getCurrentRecordFlag() {
		return currentRecordFlag;
	}
	
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public String getRuleType() {
		return ruleType;
	}

	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}

	/*    public String getFinalStatus() {
        return finalStatus;
    }

    public ReconciliationDuplicateResult finalStatus(String finalStatus) {
        this.finalStatus = finalStatus;
        return this;
    }

    public void setFinalStatus(String finalStatus) {
        this.finalStatus = finalStatus;
    }

    public ZonedDateTime getFinalActionDate() {
        return finalActionDate;
    }

    public ReconciliationDuplicateResult finalActionDate(ZonedDateTime finalActionDate) {
        this.finalActionDate = finalActionDate;
        return this;
    }

    public void setFinalActionDate(ZonedDateTime finalActionDate) {
        this.finalActionDate = finalActionDate;
    }
*/
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReconciliationDuplicateResult reconciliationDuplicateResult = (ReconciliationDuplicateResult) o;
        if (reconciliationDuplicateResult.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reconciliationDuplicateResult.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
	public String toString() {
		return "ReconciliationDuplicateResult [id=" + id + ", originalRowId="
				+ originalRowId + ", originalViewId=" + originalViewId
				+ ", originalView=" + originalView + ", targetRowId="
				+ targetRowId + ", targetViewId=" + targetViewId
				+ ", targetView=" + targetView + ", reconReference="
				+ reconReference + ", reconciliationRuleName="
				+ reconciliationRuleName + ", reconciliationRuleGroupId="
				+ reconciliationRuleGroupId + ", reconciliationRuleId="
				+ reconciliationRuleId + ", amount=" + amount
				+ ", originalAmount=" + originalAmount + ", targetAmount="
				+ targetAmount + ", reconciliationUserId="
				+ reconciliationUserId + ", reconJobReference="
				+ reconJobReference + ", reconciledDate=" + reconciledDate
				+ ", reconStatus=" + reconStatus + ", currentRecordFlag="
				+ currentRecordFlag + ", tenantId=" + tenantId + ", notes="
				+ notes + ", ruleType=" + ruleType + "]";
	}
}
