package com.nspl.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A AppModuleSummary.
 */
@Entity
@Table(name = "t_app_module_summary")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AppModuleSummary implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "insert_date")
    private LocalDate insertDate;

    @Column(name = "file_date")
    private LocalDate fileDate;

    @Column(name = "module")
    private String module;

    @Column(name = "rule_group_id")
    private Long ruleGroupId;

    @Column(name = "rule_id")
    private Long ruleId;

    @Column(name = "view_id")
    private Long viewId;

    @Column(name = "type")
    private String type;

    @Column(name = "dv_count", precision=10, scale=2)
    private BigDecimal dvCount;

    @Column(name = "type_count"/*, precision=10, scale=2*/)
    private Long typeCount;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "last_updated_by")
    private Long lastUpdatedBy;

    @Column(name = "last_updated_date")
    private ZonedDateTime lastUpdatedDate;
    
    
    @Column(name = "dv_amt", precision=15, scale=2)
    private BigDecimal dvAmount;

    @Column(name = "type_amt", precision=15, scale=2)
    private BigDecimal typeAmount;
    
    @Column(name = "approval_count")
    private Long approvalCount;
    
    @Column(name = "multi_currency")
    private Boolean multiCurrency;
    
    @Column(name = "currency_code")
    private String currencyCode;
    
    @Column(name = "initiated_count")
    private Long initiatedCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getInsertDate() {
        return insertDate;
    }

    public AppModuleSummary insertDate(LocalDate insertDate) {
        this.insertDate = insertDate;
        return this;
    }

    public void setInsertDate(LocalDate insertDate) {
        this.insertDate = insertDate;
    }

    public LocalDate getFileDate() {
        return fileDate;
    }

    public AppModuleSummary fileDate(LocalDate fileDate) {
        this.fileDate = fileDate;
        return this;
    }

    public void setFileDate(LocalDate fileDate) {
        this.fileDate = fileDate;
    }

    public String getModule() {
        return module;
    }

    public AppModuleSummary module(String module) {
        this.module = module;
        return this;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public Long getRuleGroupId() {
        return ruleGroupId;
    }

    public AppModuleSummary ruleGroupId(Long ruleGroupId) {
        this.ruleGroupId = ruleGroupId;
        return this;
    }

    public void setRuleGroupId(Long ruleGroupId) {
        this.ruleGroupId = ruleGroupId;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public AppModuleSummary ruleId(Long ruleId) {
        this.ruleId = ruleId;
        return this;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public Long getViewId() {
        return viewId;
    }

    public AppModuleSummary viewId(Long viewId) {
        this.viewId = viewId;
        return this;
    }

    public void setViewId(Long viewId) {
        this.viewId = viewId;
    }

    public String getType() {
        return type;
    }

    public AppModuleSummary type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getDvCount() {
        return dvCount;
    }

    public AppModuleSummary dvCount(BigDecimal dvCount) {
        this.dvCount = dvCount;
        return this;
    }

    public void setDvCount(BigDecimal dvCount) {
        this.dvCount = dvCount;
    }

    public Long getTypeCount() {
		return typeCount;
	}

	public void setTypeCount(Long typeCount) {
		this.typeCount = typeCount;
	}

	 public AppModuleSummary typeCount(Long typeCount) {
	        this.typeCount = typeCount;
	        return this;
	    }
	/*    public BigDecimal getTypeCount() {
        return typeCount;
    }

    public AppModuleSummary typeCount(BigDecimal typeCount) {
        this.typeCount = typeCount;
        return this;
    }

    public void setTypeCount(BigDecimal typeCount) {
        this.typeCount = typeCount;
    }
*/
    public Long getCreatedBy() {
        return createdBy;
    }

    public AppModuleSummary createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public AppModuleSummary createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public AppModuleSummary lastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public ZonedDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public AppModuleSummary lastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
        return this;
    }

    public void setLastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }
    
    

    public BigDecimal getDvAmount() {
		return dvAmount;
	}

	public void setDvAmount(BigDecimal dvAmount) {
		this.dvAmount = dvAmount;
	}

	public BigDecimal getTypeAmount() {
		return typeAmount;
	}

	public void setTypeAmount(BigDecimal typeAmount) {
		this.typeAmount = typeAmount;
	}
	
	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public Long getApprovalCount() {
		return approvalCount;
	}

	public void setApprovalCount(Long approvalCount) {
		this.approvalCount = approvalCount;
	}
	
	public Boolean getMultiCurrency() {
		return multiCurrency;
	}

	public void setMultiCurrency(Boolean multiCurrency) {
		this.multiCurrency = multiCurrency;
	}
	
	
	
	

	public Long getInitiatedCount() {
		return initiatedCount;
	}

	public void setInitiatedCount(Long initiatedCount) {
		this.initiatedCount = initiatedCount;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AppModuleSummary appModuleSummary = (AppModuleSummary) o;
        if (appModuleSummary.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), appModuleSummary.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AppModuleSummary{" +
            "id=" + getId() +
            ", insertDate='" + getInsertDate() + "'" +
            ", fileDate='" + getFileDate() + "'" +
            ", module='" + getModule() + "'" +
            ", ruleGroupId='" + getRuleGroupId() + "'" +
            ", ruleId='" + getRuleId() + "'" +
            ", viewId='" + getViewId() + "'" +
            ", type='" + getType() + "'" +
            ", dvCount='" + getDvCount() + "'" +
            ", typeCount='" + getTypeCount() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastUpdatedBy='" + getLastUpdatedBy() + "'" +
            ", lastUpdatedDate='" + getLastUpdatedDate() + "'" +
            ", multiCurrency='" + getMultiCurrency() + "'" +
            ", currencyCode='" + getCurrencyCode() + "'" +
            "}";
    }
}
