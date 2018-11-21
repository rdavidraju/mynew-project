package com.nspl.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A AcctRuleDerivations.
 */
@Entity
@Table(name = "t_acct_rule_derivations")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AcctRuleDerivations implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "acct_rule_action_id")
    private Long acctRuleActionId;

    @Column(name = "data_view_column")
    private String dataViewColumn;

    @Column(name = "accounting_references")
    private String accountingReferences;

    @Column(name = "constant_value")
    private String constantValue;

    @Column(name = "mapping_set_id")
    private Long mappingSetId;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "last_updated_by")
    private Long lastUpdatedBy;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "last_updated_date")
    private ZonedDateTime lastUpdatedDate;
    
    
    @Column(name = "operator")
    private String operator;
    
    @Column(name = "value")
    private String value;
    
    @Column(name = "criteria")
    private String criteria;
    
    @Column(name = "type")
    private String type;
    
    @Column(name = "seg_value")
    private String segValue;
    
    @Column(name = "rule_id")
    private Long ruleId;
    
    
    
    public String getSegValue() {
        return segValue;
    }

    public AcctRuleDerivations segValue(String segValue) {
        this.segValue = segValue;
        return this;
    }

    public void setSegValue(String segValue) {
        this.segValue = segValue;
    }
    
    
    
    
    public String getCriteria() {
        return criteria;
    }

    public AcctRuleDerivations criteria(String criteria) {
        this.criteria = criteria;
        return this;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }
    
    
    
    
    public String getValue() {
        return value;
    }

    public AcctRuleDerivations value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    
    public String getOperator() {
        return operator;
    }

    public AcctRuleDerivations operator(String operator) {
        this.operator = operator;
        return this;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }


    public String getType() {
        return type;
    }

    public AcctRuleDerivations type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAcctRuleActionId() {
        return acctRuleActionId;
    }

    public AcctRuleDerivations acctRuleActionId(Long acctRuleActionId) {
        this.acctRuleActionId = acctRuleActionId;
        return this;
    }

    public void setAcctRuleActionId(Long acctRuleActionId) {
        this.acctRuleActionId = acctRuleActionId;
    }

    public String getDataViewColumn() {
        return dataViewColumn;
    }

    public AcctRuleDerivations dataViewColumn(String dataViewColumn) {
        this.dataViewColumn = dataViewColumn;
        return this;
    }

    public void setDataViewColumn(String dataViewColumn) {
        this.dataViewColumn = dataViewColumn;
    }

    public String getAccountingReferences() {
        return accountingReferences;
    }

    public AcctRuleDerivations accountingReferences(String accountingReferences) {
        this.accountingReferences = accountingReferences;
        return this;
    }

    public void setAccountingReferences(String accountingReferences) {
        this.accountingReferences = accountingReferences;
    }

    public String getConstantValue() {
        return constantValue;
    }

    public AcctRuleDerivations constantValue(String constantValue) {
        this.constantValue = constantValue;
        return this;
    }

    public void setConstantValue(String constantValue) {
        this.constantValue = constantValue;
    }

    public Long getMappingSetId() {
        return mappingSetId;
    }

    public AcctRuleDerivations mappingSetId(Long mappingSetId) {
        this.mappingSetId = mappingSetId;
        return this;
    }

    public void setMappingSetId(Long mappingSetId) {
        this.mappingSetId = mappingSetId;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public AcctRuleDerivations createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public AcctRuleDerivations lastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public AcctRuleDerivations createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public AcctRuleDerivations lastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
        return this;
    }

    public void setLastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }
    
    public Long getRuleId() {
        return ruleId;
    }

    public AcctRuleDerivations ruleId(Long ruleId) {
        this.ruleId = ruleId;
        return this;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AcctRuleDerivations acctRuleDerivations = (AcctRuleDerivations) o;
        if (acctRuleDerivations.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, acctRuleDerivations.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

   
    @Override
    public String toString() {
        return "AcctRuleDerivations{" +
            "id=" + id +
            ", acctRuleActionId='" + acctRuleActionId + "'" +
            ", dataViewColumn='" + dataViewColumn + "'" +
            ", accountingReferences='" + accountingReferences + "'" +
            ", constantValue='" + constantValue + "'" +
            ", mappingSetId='" + mappingSetId + "'" +
            ", createdBy='" + createdBy + "'" +
            ", lastUpdatedBy='" + lastUpdatedBy + "'" +
            ", createdDate='" + createdDate + "'" +
            ", lastUpdatedDate='" + lastUpdatedDate + "'" +
            ", operator='" + operator + "'" +
            ", value='" + value + "'" +
            ", criteria='" + criteria + "'" +
            ", type='" + type + "'" +
            ", segValue='" + segValue + "'" +
            ", ruleId='" + ruleId + "'" +
            '}';
    }
}
