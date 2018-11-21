package com.nspl.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A AccountingEvents.
 */
@Entity
@Table(name = "t_accounting_events")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "accountingevents")
public class AccountingEvents implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_view_id")
    private Long dataViewId;

    @Column(name = "row_id")
    private Long rowId;

    @Column(name = "event_type")
    private String eventType;

    @Column(name = "acct_status")
    private String acctStatus;

    @Column(name = "acct_rule_group_id")
    private Long acctRuleGroupId;

    @Column(name = "rule_id")
    private Long ruleId;

    @Column(name = "event_time")
    private ZonedDateTime eventTime;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "last_updated_by")
    private Long lastUpdatedBy;

    @Column(name = "creation_date")
    private ZonedDateTime creationDate;

    @Column(name = "last_updated_date")
    private ZonedDateTime lastUpdatedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDataViewId() {
        return dataViewId;
    }

    public AccountingEvents dataViewId(Long dataViewId) {
        this.dataViewId = dataViewId;
        return this;
    }

    public void setDataViewId(Long dataViewId) {
        this.dataViewId = dataViewId;
    }

    public Long getRowId() {
        return rowId;
    }

    public AccountingEvents rowId(Long rowId) {
        this.rowId = rowId;
        return this;
    }

    public void setRowId(Long rowId) {
        this.rowId = rowId;
    }

    public String getEventType() {
        return eventType;
    }

    public AccountingEvents eventType(String eventType) {
        this.eventType = eventType;
        return this;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getAcctStatus() {
        return acctStatus;
    }

    public AccountingEvents acctStatus(String acctStatus) {
        this.acctStatus = acctStatus;
        return this;
    }

    public void setAcctStatus(String acctStatus) {
        this.acctStatus = acctStatus;
    }

    public Long getAcctRuleGroupId() {
        return acctRuleGroupId;
    }

    public AccountingEvents acctRuleGroupId(Long acctRuleGroupId) {
        this.acctRuleGroupId = acctRuleGroupId;
        return this;
    }

    public void setAcctRuleGroupId(Long acctRuleGroupId) {
        this.acctRuleGroupId = acctRuleGroupId;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public AccountingEvents ruleId(Long ruleId) {
        this.ruleId = ruleId;
        return this;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public ZonedDateTime getEventTime() {
        return eventTime;
    }

    public AccountingEvents eventTime(ZonedDateTime eventTime) {
        this.eventTime = eventTime;
        return this;
    }

    public void setEventTime(ZonedDateTime eventTime) {
        this.eventTime = eventTime;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public AccountingEvents createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public AccountingEvents lastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public AccountingEvents creationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public ZonedDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public AccountingEvents lastUpdatedDate(ZonedDateTime lastUpdatedDate) {
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
        AccountingEvents accountingEvents = (AccountingEvents) o;
        if (accountingEvents.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, accountingEvents.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AccountingEvents{" +
            "id=" + id +
            ", dataViewId='" + dataViewId + "'" +
            ", rowId='" + rowId + "'" +
            ", eventType='" + eventType + "'" +
            ", acctStatus='" + acctStatus + "'" +
            ", acctRuleGroupId='" + acctRuleGroupId + "'" +
            ", ruleId='" + ruleId + "'" +
            ", eventTime='" + eventTime + "'" +
            ", createdBy='" + createdBy + "'" +
            ", lastUpdatedBy='" + lastUpdatedBy + "'" +
            ", creationDate='" + creationDate + "'" +
            ", lastUpdatedDate='" + lastUpdatedDate + "'" +
            '}';
    }
}
