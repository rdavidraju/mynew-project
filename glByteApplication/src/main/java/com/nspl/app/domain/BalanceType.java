package com.nspl.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A BalanceType.
 */
@Entity
@Table(name = "t_balance_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BalanceType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "src_id")
    private Long srcId;

    @Column(name = "rule_group_id")
    private Long ruleGroupId;
    
    @Column(name = "rule_id")
    private Long ruleId;
    
    @Column(name = "field_01")
    private String field01;

    @Column(name = "field_02")
    private String field02;

    @Column(name = "field_03")
    private String field03;

    @Column(name = "field_04")
    private String field04;

    @Column(name = "field_05")
    private String field05;

    @Column(name = "field_06")
    private String field06;

    @Column(name = "field_07")
    private String field07;

    @Column(name = "field_08")
    private String field08;

    @Column(name = "field_09")
    private String field09;

    @Column(name = "field_10")
    private String field10;

    @Column(name = "field_11")
    private String field11;

    @Column(name = "field_12")
    private String field12;

    @Column(name = "field_13")
    private String field13;

    @Column(name = "field_14")
    private String field14;

    @Column(name = "field_15")
    private String field15;

    @Column(name = "module")
    private String module;
    
    @Column(name = "type")
    private String type;

    @Column(name = "processed_date")
    private ZonedDateTime processedDate;
    
    @Column(name = "opening_balance", precision=15, scale=2)
    private BigDecimal openingBalance;

    @Column(name = "additions_amt", precision=15, scale=2)
    private BigDecimal additionsAmt;

    @Column(name = "type_amt", precision=15, scale=2)
    private BigDecimal typeAmt;

    @Column(name = "closing_balance", precision=15, scale=2)
    private BigDecimal closingBalance;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "creation_date")
    private ZonedDateTime creationDate;

    @Column(name = "last_updated_by")
    private Long lastUpdatedBy;

    @Column(name = "last_updated_date")
    private ZonedDateTime lastUpdatedDate;
    
    @Column(name = "frequency")
    private String frequency;
    
    private String groupByField;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public BalanceType tenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getSrcId() {
        return srcId;
    }

    public BalanceType srcId(Long srcId) {
        this.srcId = srcId;
        return this;
    }

    public void setSrcId(Long srcId) {
        this.srcId = srcId;
    }

    public Long getRuleGroupId() {
		return ruleGroupId;
	}

	public void setRuleGroupId(Long ruleGroupId) {
		this.ruleGroupId = ruleGroupId;
	}

	public Long getRuleId() {
		return ruleId;
	}

	public void setRuleId(Long ruleId) {
		this.ruleId = ruleId;
	}

	public String getField01() {
        return field01;
    }

    public BalanceType field01(String field01) {
        this.field01 = field01;
        return this;
    }

    public void setField01(String field01) {
        this.field01 = field01;
    }

    public String getField02() {
        return field02;
    }

    public BalanceType field02(String field02) {
        this.field02 = field02;
        return this;
    }

    public void setField02(String field02) {
        this.field02 = field02;
    }

    public String getField03() {
        return field03;
    }

    public BalanceType field03(String field03) {
        this.field03 = field03;
        return this;
    }

    public void setField03(String field03) {
        this.field03 = field03;
    }

    public String getField04() {
        return field04;
    }

    public BalanceType field04(String field04) {
        this.field04 = field04;
        return this;
    }

    public void setField04(String field04) {
        this.field04 = field04;
    }

    public String getField05() {
        return field05;
    }

    public BalanceType field05(String field05) {
        this.field05 = field05;
        return this;
    }

    public void setField05(String field05) {
        this.field05 = field05;
    }

    public String getField06() {
        return field06;
    }

    public BalanceType field06(String field06) {
        this.field06 = field06;
        return this;
    }

    public void setField06(String field06) {
        this.field06 = field06;
    }

    public String getField07() {
        return field07;
    }

    public BalanceType field07(String field07) {
        this.field07 = field07;
        return this;
    }

    public void setField07(String field07) {
        this.field07 = field07;
    }

    public String getField08() {
        return field08;
    }

    public BalanceType field08(String field08) {
        this.field08 = field08;
        return this;
    }

    public void setField08(String field08) {
        this.field08 = field08;
    }

    public String getField09() {
        return field09;
    }

    public BalanceType field09(String field09) {
        this.field09 = field09;
        return this;
    }

    public void setField09(String field09) {
        this.field09 = field09;
    }

    public String getField10() {
        return field10;
    }

    public BalanceType field10(String field10) {
        this.field10 = field10;
        return this;
    }

    public void setField10(String field10) {
        this.field10 = field10;
    }

    public String getField11() {
        return field11;
    }

    public BalanceType field11(String field11) {
        this.field11 = field11;
        return this;
    }

    public void setField11(String field11) {
        this.field11 = field11;
    }

    public String getField12() {
        return field12;
    }

    public BalanceType field12(String field12) {
        this.field12 = field12;
        return this;
    }

    public void setField12(String field12) {
        this.field12 = field12;
    }

    public String getField13() {
        return field13;
    }

    public BalanceType field13(String field13) {
        this.field13 = field13;
        return this;
    }

    public void setField13(String field13) {
        this.field13 = field13;
    }

    public String getField14() {
        return field14;
    }

    public BalanceType field14(String field14) {
        this.field14 = field14;
        return this;
    }

    public void setField14(String field14) {
        this.field14 = field14;
    }

    public String getField15() {
        return field15;
    }

    public BalanceType field15(String field15) {
        this.field15 = field15;
        return this;
    }

    public void setField15(String field15) {
        this.field15 = field15;
    }

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public ZonedDateTime getProcessedDate() {
		return processedDate;
	}

	public void setProcessedDate(ZonedDateTime processedDate) {
		this.processedDate = processedDate;
	}

	public BigDecimal getOpeningBalance() {
        return openingBalance;
    }

    public BalanceType openingBalance(BigDecimal openingBalance) {
        this.openingBalance = openingBalance;
        return this;
    }

    public void setOpeningBalance(BigDecimal openingBalance) {
        this.openingBalance = openingBalance;
    }

    public BigDecimal getAdditionsAmt() {
        return additionsAmt;
    }

    public BalanceType additionsAmt(BigDecimal additionsAmt) {
        this.additionsAmt = additionsAmt;
        return this;
    }

    public void setAdditionsAmt(BigDecimal additionsAmt) {
        this.additionsAmt = additionsAmt;
    }
    
	public BigDecimal getTypeAmt() {
		return typeAmt;
	}
	
	public void setTypeAmt(BigDecimal typeAmt) {
		this.typeAmt = typeAmt;
	}
	
	public BigDecimal getClosingBalance() {
		return closingBalance;
	}
	
	public void setClosingBalance(BigDecimal closingBalance) {
		this.closingBalance = closingBalance;
	}
	
	public Long getCreatedBy() {
        return createdBy;
    }

    public BalanceType createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public BalanceType creationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public BalanceType lastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public ZonedDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public BalanceType lastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
        return this;
    }

    public void setLastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getGroupByField() {
		return groupByField;
	}

	public void setGroupByField(String groupByField) {
		this.groupByField = groupByField;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BalanceType balanceType = (BalanceType) o;
        if (balanceType.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), balanceType.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

	@Override
	public String toString() {
		return "BalanceType [id=" + id + ", tenantId=" + tenantId + ", srcId="
				+ srcId + ", ruleGroupId=" + ruleGroupId + ", ruleId=" + ruleId
				+ ", field01=" + field01 + ", field02=" + field02
				+ ", field03=" + field03 + ", field04=" + field04
				+ ", field05=" + field05 + ", field06=" + field06
				+ ", field07=" + field07 + ", field08=" + field08
				+ ", field09=" + field09 + ", field10=" + field10
				+ ", field11=" + field11 + ", field12=" + field12
				+ ", field13=" + field13 + ", field14=" + field14
				+ ", field15=" + field15 + ", module=" + module + ", type="
				+ type + ", processedDate=" + processedDate
				+ ", openingBalance=" + openingBalance + ", additionsAmt="
				+ additionsAmt + ", typeAmt=" + typeAmt + ", closingBalance="
				+ closingBalance + ", createdBy=" + createdBy
				+ ", creationDate=" + creationDate + ", lastUpdatedBy="
				+ lastUpdatedBy + ", lastUpdatedDate=" + lastUpdatedDate + ", frequency=" + frequency +"]";
	}
}
