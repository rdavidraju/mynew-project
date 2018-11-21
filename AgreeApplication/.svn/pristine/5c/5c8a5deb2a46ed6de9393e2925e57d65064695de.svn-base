package com.nspl.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A NotificationBatch.
 */
@Entity
@Table(name = "t_notification_batch")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class NotificationBatch implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "notification_name")
    private String notificationName;

    @Column(name = "current_approver")
    private Long currentApprover;

    @Column(name = "parent_batch")
    private Long parentBatch;

    @Column(name = "ref_level")
    private Integer refLevel;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "last_updated_by")
    private Long lastUpdatedBy;

    @Column(name = "last_updated_date")
    private ZonedDateTime lastUpdatedDate;
    
    
    @Column(name = "rule_group_id")
    private Long ruleGroupId;
    
    @Column(name = "rule_id")
    private Long ruleId;
    
    @Column(name = "module")
    private String module;
    
    @Column(name = "status")
    private String status;
    
    @Column(name = "tenant_id")
    private Long tenantId;
    
    @Column(name = "process_instance_id")
    private Long processInstanceId;
    
    @Column(name = "appr_ref")
    private Integer apprRef;

    public Integer getApprRef() {
		return apprRef;
	}

	public void setApprRef(Integer apprRef) {
		this.apprRef = apprRef;
	}

	public Long getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(Long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public Long getTenantId() {
        return tenantId;
    }

    public NotificationBatch tenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
    
    public Long getRuleGroupId() {
        return ruleGroupId;
    }

    public NotificationBatch ruleGroupId(Long ruleGroupId) {
        this.ruleGroupId = ruleGroupId;
        return this;
    }

    public void setRuleGroupId(Long ruleGroupId) {
        this.ruleGroupId = ruleGroupId;
    }
    
    public Long getRuleId() {
        return ruleId;
    }

    public NotificationBatch ruleId(Long ruleId) {
        this.ruleId = ruleId;
        return this;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }
    
    
    public String getModule() {
        return module;
    }

    public NotificationBatch module(String module) {
        this.module = module;
        return this;
    }

    public void setModule(String module) {
        this.module = module;
    }
    
    public String getStatus() {
        return status;
    }

    public NotificationBatch status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNotificationName() {
        return notificationName;
    }

    public NotificationBatch notificationName(String notificationName) {
        this.notificationName = notificationName;
        return this;
    }

    public void setNotificationName(String notificationName) {
        this.notificationName = notificationName;
    }

    public Long getCurrentApprover() {
        return currentApprover;
    }

    public NotificationBatch currentApprover(Long currentApprover) {
        this.currentApprover = currentApprover;
        return this;
    }

    public void setCurrentApprover(Long currentApprover) {
        this.currentApprover = currentApprover;
    }

    public Long getParentBatch() {
        return parentBatch;
    }

    public NotificationBatch parentBatch(Long parentBatch) {
        this.parentBatch = parentBatch;
        return this;
    }

    public void setParentBatch(Long parentBatch) {
        this.parentBatch = parentBatch;
    }

    public Integer getRefLevel() {
        return refLevel;
    }

    public NotificationBatch refLevel(Integer refLevel) {
        this.refLevel = refLevel;
        return this;
    }

    public void setRefLevel(Integer refLevel) {
        this.refLevel = refLevel;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public NotificationBatch createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public NotificationBatch createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public NotificationBatch lastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public ZonedDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public NotificationBatch lastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
        return this;
    }

    public void setLastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NotificationBatch notificationBatch = (NotificationBatch) o;
        if (notificationBatch.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, notificationBatch.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "NotificationBatch{" +
            "id=" + id +
            ", notificationName='" + notificationName + "'" +
            ", currentApprover='" + currentApprover + "'" +
            ", parentBatch='" + parentBatch + "'" +
            ", refLevel='" + refLevel + "'" +
            ", createdBy='" + createdBy + "'" +
            ", createdDate='" + createdDate + "'" +
            ", lastUpdatedBy='" + lastUpdatedBy + "'" +
            ", lastUpdatedDate='" + lastUpdatedDate + "'" +
            ", rule_group_id='" + ruleGroupId + "'" +
            ", rule_id='" + ruleId + "'" +
            ", module='" + module + "'" +
            ", status='" + status + "'" +
            ", process_instance_id='" + processInstanceId + "'" +
            ", apprRef='" + apprRef + "'" +
            '}';
    }
}
