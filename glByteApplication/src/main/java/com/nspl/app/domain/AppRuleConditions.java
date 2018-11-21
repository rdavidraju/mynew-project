package com.nspl.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A AppRuleConditions.
 */
@Entity
@Table(name = "t_app_rule_conditions")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AppRuleConditions implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rule_id")
    private Long ruleId;

    @Column(name = "open_bracket")
    private String openBracket;

    @Column(name = "operator")
    private String operator;

    @Column(name = "value")
    private String value;

    @Column(name = "close_bracket")
    private String closeBracket;

    @Column(name = "logical_operator")
    private String logicalOperator;

    @Column(name = "column_id")
    private Long columnId;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "last_updated_by")
    private Long lastUpdatedBy;

    @Column(name = "last_updated_date")
    private ZonedDateTime lastUpdatedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public AppRuleConditions ruleId(Long ruleId) {
        this.ruleId = ruleId;
        return this;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public String getOpenBracket() {
        return openBracket;
    }

    public AppRuleConditions openBracket(String openBracket) {
        this.openBracket = openBracket;
        return this;
    }

    public void setOpenBracket(String openBracket) {
        this.openBracket = openBracket;
    }

    public String getOperator() {
        return operator;
    }

    public AppRuleConditions operator(String operator) {
        this.operator = operator;
        return this;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public AppRuleConditions value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCloseBracket() {
        return closeBracket;
    }

    public AppRuleConditions closeBracket(String closeBracket) {
        this.closeBracket = closeBracket;
        return this;
    }

    public void setCloseBracket(String closeBracket) {
        this.closeBracket = closeBracket;
    }

    public String getLogicalOperator() {
        return logicalOperator;
    }

    public AppRuleConditions logicalOperator(String logicalOperator) {
        this.logicalOperator = logicalOperator;
        return this;
    }

    public void setLogicalOperator(String logicalOperator) {
        this.logicalOperator = logicalOperator;
    }

    public Long getColumnId() {
        return columnId;
    }

    public AppRuleConditions columnId(Long columnId) {
        this.columnId = columnId;
        return this;
    }

    public void setColumnId(Long columnId) {
        this.columnId = columnId;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public AppRuleConditions createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public AppRuleConditions createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public AppRuleConditions lastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public ZonedDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public AppRuleConditions lastUpdatedDate(ZonedDateTime lastUpdatedDate) {
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
        AppRuleConditions appRuleConditions = (AppRuleConditions) o;
        if (appRuleConditions.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, appRuleConditions.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AppRuleConditions{" +
            "id=" + id +
            ", ruleId='" + ruleId + "'" +
            ", openBracket='" + openBracket + "'" +
            ", operator='" + operator + "'" +
            ", value='" + value + "'" +
            ", closeBracket='" + closeBracket + "'" +
            ", logicalOperator='" + logicalOperator + "'" +
            ", columnId='" + columnId + "'" +
            ", createdBy='" + createdBy + "'" +
            ", createdDate='" + createdDate + "'" +
            ", lastUpdatedBy='" + lastUpdatedBy + "'" +
            ", lastUpdatedDate='" + lastUpdatedDate + "'" +
            '}';
    }
}
