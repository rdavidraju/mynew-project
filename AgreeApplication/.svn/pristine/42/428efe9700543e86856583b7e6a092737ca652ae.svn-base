package com.nspl.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A RuleGroupDetails.
 */
@Entity
@Table(name = "t_rule_group_details")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "reconapplication", type="rulegroupdetails")
public class RuleGroupDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rule_group_id")
    private Long ruleGroupId;

    @Column(name = "rule_id")
    private Long ruleId;

    @Column(name = "priority")
    private Integer priority;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "last_updated_by")
    private Long lastUpdatedBy;

    @Column(name = "creation_date")
    private ZonedDateTime creationDate;

    @Column(name = "last_updated_date")
    private ZonedDateTime lastUpdatedDate;
    
    
    @Column(name = "tenant_id")
    private Long tenantId;
    
    @Column(name = "enabled_flag")
    private Boolean enabledFlag;

    @Column(name = "suggestion_rule")
    private Boolean suggestionRule;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRuleGroupId() {
        return ruleGroupId;
    }

    public RuleGroupDetails ruleGroupId(Long ruleGroupId) {
        this.ruleGroupId = ruleGroupId;
        return this;
    }

    public void setRuleGroupId(Long ruleGroupId) {
        this.ruleGroupId = ruleGroupId;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public RuleGroupDetails ruleId(Long ruleId) {
        this.ruleId = ruleId;
        return this;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public Integer getPriority() {
        return priority;
    }

    public RuleGroupDetails priority(Integer priority) {
        this.priority = priority;
        return this;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public RuleGroupDetails createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public RuleGroupDetails lastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public RuleGroupDetails creationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public ZonedDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public RuleGroupDetails lastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
        return this;
    }

    public void setLastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }
    
    
    public Long getTenantId() {
        return tenantId;
    }

    public RuleGroupDetails tenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
    
    public Boolean isEnabledFlag() {
        return enabledFlag;
    }

    public RuleGroupDetails enabledFlag(Boolean enabledFlag) {
        this.enabledFlag = enabledFlag;
        return this;
    }

    public void setEnabledFlag(Boolean enabledFlag) {
        this.enabledFlag = enabledFlag;
    }

    public Boolean getSuggestionRule() {
		return suggestionRule;
	}

	public void setSuggestionRule(Boolean suggestionRule) {
		this.suggestionRule = suggestionRule;
	}


	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RuleGroupDetails ruleGroupDetails = (RuleGroupDetails) o;
        if (ruleGroupDetails.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, ruleGroupDetails.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "RuleGroupDetails{" +
            "id=" + id +
            ", ruleGroupId='" + ruleGroupId + "'" +
            ", ruleId='" + ruleId + "'" +
            ", priority='" + priority + "'" +
            ", createdBy='" + createdBy + "'" +
            ", lastUpdatedBy='" + lastUpdatedBy + "'" +
            ", creationDate='" + creationDate + "'" +
            ", lastUpdatedDate='" + lastUpdatedDate + "'" +
            '}';
    }
}
