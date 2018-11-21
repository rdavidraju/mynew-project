package com.nspl.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A AcctRuleConditions.
 */
@Entity
@Table(name = "t_acct_rule_conditions")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AcctRuleConditions implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rule_id")
    private Long ruleId;

    @Column(name = "rule_action_id")
    private Long ruleActionId;

    @Column(name = "open_bracket")
    private String openBracket;

    @Column(name = "s_view_column_id")
    private Long sViewColumnId;

    @Column(name = "function")
    private String function;
    
    @Column(name = "operator")
    private String operator;

    @Column(name = "value")
    private String value;

    @Column(name = "close_bracket")
    private String closeBracket;

    @Column(name = "logical_operator")
    private String logicalOperator;

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

    public AcctRuleConditions ruleId(Long ruleId) {
        this.ruleId = ruleId;
        return this;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public Long getRuleActionId() {
        return ruleActionId;
    }

    public AcctRuleConditions ruleActionId(Long ruleActionId) {
        this.ruleActionId = ruleActionId;
        return this;
    }

    public void setRuleActionId(Long ruleActionId) {
        this.ruleActionId = ruleActionId;
    }

    public String getOpenBracket() {
        return openBracket;
    }

    public AcctRuleConditions openBracket(String openBracket) {
        this.openBracket = openBracket;
        return this;
    }

    public void setOpenBracket(String openBracket) {
        this.openBracket = openBracket;
    }

    public Long getsViewColumnId() {
        return sViewColumnId;
    }

    public AcctRuleConditions sViewColumnId(Long sViewColumnId) {
        this.sViewColumnId = sViewColumnId;
        return this;
    }

    public void setsViewColumnId(Long sViewColumnId) {
        this.sViewColumnId = sViewColumnId;
    }

    public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public String getOperator() {
        return operator;
    }

    public AcctRuleConditions operator(String operator) {
        this.operator = operator;
        return this;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public AcctRuleConditions value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCloseBracket() {
        return closeBracket;
    }

    public AcctRuleConditions closeBracket(String closeBracket) {
        this.closeBracket = closeBracket;
        return this;
    }

    public void setCloseBracket(String closeBracket) {
        this.closeBracket = closeBracket;
    }

    public String getLogicalOperator() {
        return logicalOperator;
    }

    public AcctRuleConditions logicalOperator(String logicalOperator) {
        this.logicalOperator = logicalOperator;
        return this;
    }

    public void setLogicalOperator(String logicalOperator) {
        this.logicalOperator = logicalOperator;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public AcctRuleConditions createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public AcctRuleConditions createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public AcctRuleConditions lastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public ZonedDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public AcctRuleConditions lastUpdatedDate(ZonedDateTime lastUpdatedDate) {
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
        AcctRuleConditions acctRuleConditions = (AcctRuleConditions) o;
        if (acctRuleConditions.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, acctRuleConditions.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
	public String toString() {
		return "AcctRuleConditions [id=" + id + ", ruleId=" + ruleId
				+ ", ruleActionId=" + ruleActionId + ", openBracket="
				+ openBracket + ", sViewColumnId=" + sViewColumnId
				+ ", function=" + function + ", operator=" + operator
				+ ", value=" + value + ", closeBracket=" + closeBracket
				+ ", logicalOperator=" + logicalOperator + ", createdBy="
				+ createdBy + ", createdDate=" + createdDate
				+ ", lastUpdatedBy=" + lastUpdatedBy + ", lastUpdatedDate="
				+ lastUpdatedDate + "]";
	}
}
