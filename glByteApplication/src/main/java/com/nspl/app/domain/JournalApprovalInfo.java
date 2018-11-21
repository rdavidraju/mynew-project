package com.nspl.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A JournalApprovalInfo.
 */
@Entity
@Table(name = "t_journal_approval_info")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class JournalApprovalInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "je_header_id")
    private Long jeHeaderId;

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

    @Column(name = "final_action_date")
    private ZonedDateTime finalActionDate;

    @Column(name = "final_status")
    private String finalStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getJeHeaderId() {
        return jeHeaderId;
    }

    public JournalApprovalInfo jeHeaderId(Long jeHeaderId) {
        this.jeHeaderId = jeHeaderId;
        return this;
    }

    public void setJeHeaderId(Long jeHeaderId) {
        this.jeHeaderId = jeHeaderId;
    }

    public Long getApprovalGroupId() {
        return approvalGroupId;
    }

    public JournalApprovalInfo approvalGroupId(Long approvalGroupId) {
        this.approvalGroupId = approvalGroupId;
        return this;
    }

    public void setApprovalGroupId(Long approvalGroupId) {
        this.approvalGroupId = approvalGroupId;
    }

    public Long getApprovalRuleId() {
        return approvalRuleId;
    }

    public JournalApprovalInfo approvalRuleId(Long approvalRuleId) {
        this.approvalRuleId = approvalRuleId;
        return this;
    }

    public void setApprovalRuleId(Long approvalRuleId) {
        this.approvalRuleId = approvalRuleId;
    }

    public ZonedDateTime getApprovalInitiationDate() {
        return approvalInitiationDate;
    }

    public JournalApprovalInfo approvalInitiationDate(ZonedDateTime approvalInitiationDate) {
        this.approvalInitiationDate = approvalInitiationDate;
        return this;
    }

    public void setApprovalInitiationDate(ZonedDateTime approvalInitiationDate) {
        this.approvalInitiationDate = approvalInitiationDate;
    }

    public Long getApprovalBatchId() {
        return approvalBatchId;
    }

    public JournalApprovalInfo approvalBatchId(Long approvalBatchId) {
        this.approvalBatchId = approvalBatchId;
        return this;
    }

    public void setApprovalBatchId(Long approvalBatchId) {
        this.approvalBatchId = approvalBatchId;
    }

    public String getApprRef01() {
        return apprRef01;
    }

    public JournalApprovalInfo apprRef01(String apprRef01) {
        this.apprRef01 = apprRef01;
        return this;
    }

    public void setApprRef01(String apprRef01) {
        this.apprRef01 = apprRef01;
    }

    public String getApprRef02() {
        return apprRef02;
    }

    public JournalApprovalInfo apprRef02(String apprRef02) {
        this.apprRef02 = apprRef02;
        return this;
    }

    public void setApprRef02(String apprRef02) {
        this.apprRef02 = apprRef02;
    }

    public String getApprRef03() {
        return apprRef03;
    }

    public JournalApprovalInfo apprRef03(String apprRef03) {
        this.apprRef03 = apprRef03;
        return this;
    }

    public void setApprRef03(String apprRef03) {
        this.apprRef03 = apprRef03;
    }

    public String getApprRef04() {
        return apprRef04;
    }

    public JournalApprovalInfo apprRef04(String apprRef04) {
        this.apprRef04 = apprRef04;
        return this;
    }

    public void setApprRef04(String apprRef04) {
        this.apprRef04 = apprRef04;
    }

    public String getApprRef05() {
        return apprRef05;
    }

    public JournalApprovalInfo apprRef05(String apprRef05) {
        this.apprRef05 = apprRef05;
        return this;
    }

    public void setApprRef05(String apprRef05) {
        this.apprRef05 = apprRef05;
    }

    public String getApprRef06() {
        return apprRef06;
    }

    public JournalApprovalInfo apprRef06(String apprRef06) {
        this.apprRef06 = apprRef06;
        return this;
    }

    public void setApprRef06(String apprRef06) {
        this.apprRef06 = apprRef06;
    }

    public String getApprRef07() {
        return apprRef07;
    }

    public JournalApprovalInfo apprRef07(String apprRef07) {
        this.apprRef07 = apprRef07;
        return this;
    }

    public void setApprRef07(String apprRef07) {
        this.apprRef07 = apprRef07;
    }

    public String getApprRef08() {
        return apprRef08;
    }

    public JournalApprovalInfo apprRef08(String apprRef08) {
        this.apprRef08 = apprRef08;
        return this;
    }

    public void setApprRef08(String apprRef08) {
        this.apprRef08 = apprRef08;
    }

    public String getApprRef09() {
        return apprRef09;
    }

    public JournalApprovalInfo apprRef09(String apprRef09) {
        this.apprRef09 = apprRef09;
        return this;
    }

    public void setApprRef09(String apprRef09) {
        this.apprRef09 = apprRef09;
    }

    public String getApprRef10() {
        return apprRef10;
    }

    public JournalApprovalInfo apprRef10(String apprRef10) {
        this.apprRef10 = apprRef10;
        return this;
    }

    public void setApprRef10(String apprRef10) {
        this.apprRef10 = apprRef10;
    }

    public String getApprRef11() {
        return apprRef11;
    }

    public JournalApprovalInfo apprRef11(String apprRef11) {
        this.apprRef11 = apprRef11;
        return this;
    }

    public void setApprRef11(String apprRef11) {
        this.apprRef11 = apprRef11;
    }

    public String getApprRef12() {
        return apprRef12;
    }

    public JournalApprovalInfo apprRef12(String apprRef12) {
        this.apprRef12 = apprRef12;
        return this;
    }

    public void setApprRef12(String apprRef12) {
        this.apprRef12 = apprRef12;
    }

    public String getApprRef13() {
        return apprRef13;
    }

    public JournalApprovalInfo apprRef13(String apprRef13) {
        this.apprRef13 = apprRef13;
        return this;
    }

    public void setApprRef13(String apprRef13) {
        this.apprRef13 = apprRef13;
    }

    public String getApprRef14() {
        return apprRef14;
    }

    public JournalApprovalInfo apprRef14(String apprRef14) {
        this.apprRef14 = apprRef14;
        return this;
    }

    public void setApprRef14(String apprRef14) {
        this.apprRef14 = apprRef14;
    }

    public String getApprRef15() {
        return apprRef15;
    }

    public JournalApprovalInfo apprRef15(String apprRef15) {
        this.apprRef15 = apprRef15;
        return this;
    }

    public void setApprRef15(String apprRef15) {
        this.apprRef15 = apprRef15;
    }

    public ZonedDateTime getFinalActionDate() {
        return finalActionDate;
    }

    public JournalApprovalInfo finalActionDate(ZonedDateTime finalActionDate) {
        this.finalActionDate = finalActionDate;
        return this;
    }

    public void setFinalActionDate(ZonedDateTime finalActionDate) {
        this.finalActionDate = finalActionDate;
    }

    public String getFinalStatus() {
        return finalStatus;
    }

    public JournalApprovalInfo finalStatus(String finalStatus) {
        this.finalStatus = finalStatus;
        return this;
    }

    public void setFinalStatus(String finalStatus) {
        this.finalStatus = finalStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JournalApprovalInfo journalApprovalInfo = (JournalApprovalInfo) o;
        if (journalApprovalInfo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), journalApprovalInfo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "JournalApprovalInfo{" +
            "id=" + getId() +
            ", jeHeaderId='" + getJeHeaderId() + "'" +
            ", approvalGroupId='" + getApprovalGroupId() + "'" +
            ", approvalRuleId='" + getApprovalRuleId() + "'" +
            ", approvalInitiationDate='" + getApprovalInitiationDate() + "'" +
            ", approvalBatchId='" + getApprovalBatchId() + "'" +
            ", apprRef01='" + getApprRef01() + "'" +
            ", apprRef02='" + getApprRef02() + "'" +
            ", apprRef03='" + getApprRef03() + "'" +
            ", apprRef04='" + getApprRef04() + "'" +
            ", apprRef05='" + getApprRef05() + "'" +
            ", apprRef06='" + getApprRef06() + "'" +
            ", apprRef07='" + getApprRef07() + "'" +
            ", apprRef08='" + getApprRef08() + "'" +
            ", apprRef09='" + getApprRef09() + "'" +
            ", apprRef10='" + getApprRef10() + "'" +
            ", apprRef11='" + getApprRef11() + "'" +
            ", apprRef12='" + getApprRef12() + "'" +
            ", apprRef13='" + getApprRef13() + "'" +
            ", apprRef14='" + getApprRef14() + "'" +
            ", apprRef15='" + getApprRef15() + "'" +
            ", finalActionDate='" + getFinalActionDate() + "'" +
            ", finalStatus='" + getFinalStatus() + "'" +
            "}";
    }
}
