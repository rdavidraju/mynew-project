package com.nspl.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Rules.
 */
@Entity
@Table(name = "t_rules")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "rules")
public class Rules implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "rule_code")
    private String ruleCode;

    @Column(name = "start_date")
    private ZonedDateTime startDate;

    @Column(name = "end_date")
    private ZonedDateTime endDate;

    @Column(name = "enabled_flag")
    private Boolean enabledFlag;

    @Column(name = "rule_type")
    private String ruleType;
    
    @Column(name = "category")
    private String category;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "last_updated_by")
    private Long lastUpdatedBy;

    @Column(name = "creation_date")
    private ZonedDateTime creationDate;

    @Column(name = "last_updated_date")
    private ZonedDateTime lastUpdatedDate;

    @Column(name = "source_data_view_id")
    private Long sourceDataViewId;

    @Column(name = "target_data_view_id")
    private Long targetDataViewId;
     
    @Column(name = "reconciliation_view_id")
    private Long reconciliationViewId;
    
    @Column(name = "reconciliation_status")
    private String reconciliationStatus;
    
    @Column(name = "accounting_status")
    private String accountingStatus;
    
    @Column(name = "COA")
    private String cOA;
    
    @Column(name="condition_expression")
    private String conditionExpression;
    
	public Long getReconciliationViewId() {
		return reconciliationViewId;
	}

	public void setReconciliationViewId(Long reconciliationViewId) {
		this.reconciliationViewId = reconciliationViewId;
	}

	public String getReconciliationStatus() {
		return reconciliationStatus;
	}

	public void setReconciliationStatus(String reconciliationStatus) {
		this.reconciliationStatus = reconciliationStatus;
	}

	public String getAccountingStatus() {
		return accountingStatus;
	}

	public void setAccountingStatus(String accountingStatus) {
		this.accountingStatus = accountingStatus;
	}

	public String getcOA() {
		return cOA;
	}

	public void setcOA(String cOA) {
		this.cOA = cOA;
	}

	public Boolean getEnabledFlag() {
		return enabledFlag;
	}

	@Column(name = "currency_column_id")
    private Long currencyColumnId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public Rules tenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public Rules ruleCode(String ruleCode) {
        this.ruleCode = ruleCode;
        return this;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public Rules startDate(ZonedDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public Rules endDate(ZonedDateTime endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public Boolean isEnabledFlag() {
        return enabledFlag;
    }

    public Rules enabledFlag(Boolean enabledFlag) {
        this.enabledFlag = enabledFlag;
        return this;
    }

    public void setEnabledFlag(Boolean enabledFlag) {
        this.enabledFlag = enabledFlag;
    }

    public String getRuleType() {
        return ruleType;
    }

    public Rules ruleType(String ruleType) {
        this.ruleType = ruleType;
        return this;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    
    
    public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Long getCreatedBy() {
        return createdBy;
    }

    public Rules createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public Rules lastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public Rules creationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public ZonedDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public Rules lastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
        return this;
    }

    public void setLastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public Long getSourceDataViewId() {
        return sourceDataViewId;
    }

    public Rules sourceDataViewId(Long sourceDataViewId) {
        this.sourceDataViewId = sourceDataViewId;
        return this;
    }

    public void setSourceDataViewId(Long sourceDataViewId) {
        this.sourceDataViewId = sourceDataViewId;
    }

    public Long getTargetDataViewId() {
        return targetDataViewId;
    }

    public Rules targetDataViewId(Long targetDataViewId) {
        this.targetDataViewId = targetDataViewId;
        return this;
    }

    public void setTargetDataViewId(Long targetDataViewId) {
        this.targetDataViewId = targetDataViewId;
    }

    public Long getCurrencyColumnId() {
        return currencyColumnId;
    }

    public Rules currencyColumnId(Long currencyColumnId) {
        this.currencyColumnId = currencyColumnId;
        return this;
    }

    public void setCurrencyColumnId(Long currencyColumnId) {
        this.currencyColumnId = currencyColumnId;
    }
    
    public String getConditionExpression() {
		return conditionExpression;
	}

	public void setConditionExpression(String conditionExpression) {
		this.conditionExpression = conditionExpression;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Rules rules = (Rules) o;
        if (rules.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, rules.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
	public String toString() {
		return "Rules [id=" + id + ", tenantId=" + tenantId + ", ruleCode="
				+ ruleCode + ", startDate=" + startDate + ", endDate="
				+ endDate + ", enabledFlag=" + enabledFlag + ", ruleType="
				+ ruleType + ", category=" + category + ", createdBy="
				+ createdBy + ", lastUpdatedBy=" + lastUpdatedBy
				+ ", creationDate=" + creationDate + ", lastUpdatedDate="
				+ lastUpdatedDate + ", sourceDataViewId=" + sourceDataViewId
				+ ", targetDataViewId=" + targetDataViewId
				+ ", reconciliationViewId=" + reconciliationViewId
				+ ", reconciliationStatus=" + reconciliationStatus
				+ ", accountingStatus=" + accountingStatus + ", cOA=" + cOA
				+ ", currencyColumnId=" + currencyColumnId + "]";
	}
}
