package com.nspl.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A JeLines.
 */
@Entity
@Table(name = "t_je_lines")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class JeLines implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "je_batch_id")
    private Long jeBatchId;

    @Column(name = "je_header_id")
    private Long jeHeaderId;
    
    @Column(name = "row_id")
    private Long rowId;

    @Column(name = "line_num")
    private Integer lineNum;

    @Column(name = "description_attribute")
    private String descriptionAttribute;

    @Column(name = "currency")
    private String currency;

    @Column(name = "debit_amount", precision=10, scale=2)
    private BigDecimal debitAmount;

    @Column(name = "credit_amount", precision=10, scale=2)
    private BigDecimal creditAmount;

    @Column(name = "accounted_debit", precision=10, scale=2)
    private BigDecimal accountedDebit;

    @Column(name = "accounted_credit", precision=10, scale=2)
    private BigDecimal accountedCredit;

    @Column(name = "comments")
    private String comments;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "last_updated_by")
    private Long lastUpdatedBy;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "last_updated_date")
    private ZonedDateTime lastUpdatedDate;
    
    @Column(name = "code_combination")
    private String codeCombination;
    
    
    @Column(name = "attribute_ref_1")
    private String attributeRef1;
    
    @Column(name = "attribute_ref_2")
    private String attributeRef2;
    @Column(name = "attribute_ref_3")
    private String attributeRef3;
    @Column(name = "attribute_ref_4")
    private String attributeRef4;
    @Column(name = "attribute_ref_5")
    private String attributeRef5;
    @Column(name = "attribute_ref_6")
    private String attributeRef6;
    
    
    
    @Column(name = "attr_ref_1_type")
    private String attrRef1Type;
    
    @Column(name = "attr_ref_2_type")
    private String attrRef2Type;
    @Column(name = "attr_ref_3_type")
    private String attrRef3Type;
    @Column(name = "attr_ref_4_type")
    private String attrRef4Type;
    @Column(name = "attr_ref_5_type")
    private String attrRef5Type;
    @Column(name = "attr_ref_6_type")
    private String attrRef6Type;
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getJeBatchId() {
        return jeBatchId;
    }

    public JeLines jeBatchId(Long jeBatchId) {
        this.jeBatchId = jeBatchId;
        return this;
    }

    public void setJeBatchId(Long jeBatchId) {
        this.jeBatchId = jeBatchId;
    }

    public Long getJeHeaderId() {
		return jeHeaderId;
	}

	public void setJeHeaderId(Long jeHeaderId) {
		this.jeHeaderId = jeHeaderId;
	}

	public Long getRowId() {
        return rowId;
    }

    public JeLines rowId(Long rowId) {
        this.rowId = rowId;
        return this;
    }

    public void setRowId(Long rowId) {
        this.rowId = rowId;
    }

    public Integer getLineNum() {
        return lineNum;
    }

    public JeLines lineNum(Integer lineNum) {
        this.lineNum = lineNum;
        return this;
    }

    public void setLineNum(Integer lineNum) {
        this.lineNum = lineNum;
    }

    public String getDescriptionAttribute() {
        return descriptionAttribute;
    }

    public JeLines descriptionAttribute(String descriptionAttribute) {
        this.descriptionAttribute = descriptionAttribute;
        return this;
    }

    public void setDescriptionAttribute(String descriptionAttribute) {
        this.descriptionAttribute = descriptionAttribute;
    }

    public String getCurrency() {
        return currency;
    }

    public JeLines currency(String currency) {
        this.currency = currency;
        return this;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getDebitAmount() {
        return debitAmount;
    }

    public JeLines debitAmount(BigDecimal debitAmount) {
        this.debitAmount = debitAmount;
        return this;
    }

    public void setDebitAmount(BigDecimal debitAmount) {
        this.debitAmount = debitAmount;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public JeLines creditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
        return this;
    }

    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    public BigDecimal getAccountedDebit() {
        return accountedDebit;
    }

    public JeLines accountedDebit(BigDecimal accountedDebit) {
        this.accountedDebit = accountedDebit;
        return this;
    }

    public void setAccountedDebit(BigDecimal accountedDebit) {
        this.accountedDebit = accountedDebit;
    }

    public BigDecimal getAccountedCredit() {
        return accountedCredit;
    }

    public JeLines accountedCredit(BigDecimal accountedCredit) {
        this.accountedCredit = accountedCredit;
        return this;
    }

    public void setAccountedCredit(BigDecimal accountedCredit) {
        this.accountedCredit = accountedCredit;
    }

    public String getComments() {
        return comments;
    }

    public JeLines comments(String comments) {
        this.comments = comments;
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public JeLines createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public JeLines lastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public JeLines createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public JeLines lastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
        return this;
    }

    public void setLastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public String getCodeCombination() {
		return codeCombination;
	}

	public void setCodeCombination(String codeCombination) {
		this.codeCombination = codeCombination;
	}
	
	public String getAttributeRef1() {
        return attributeRef1;
    }

    public JeLines attributeRef1(String attributeRef1) {
        this.attributeRef1 = attributeRef1;
        return this;
    }

    public void setAttributeRef1(String attributeRef1) {
        this.attributeRef1 = attributeRef1;
    }
    
	public String getAttributeRef2() {
        return attributeRef2;
    }

    public JeLines attributeRef2(String attributeRef2) {
        this.attributeRef2 = attributeRef2;
        return this;
    }

    public void setAttributeRef2(String attributeRef2) {
        this.attributeRef2 = attributeRef2;
    }
    
    
	public String getAttributeRef3() {
        return attributeRef3;
    }

    public JeLines attributeRef3(String attributeRef3) {
        this.attributeRef3 = attributeRef3;
        return this;
    }

    public void setAttributeRef3(String attributeRef3) {
        this.attributeRef3 = attributeRef3;
    }

    
    public String getAttributeRef4() {
        return attributeRef4;
    }

    public JeLines attributeRef4(String attributeRef4) {
        this.attributeRef4 = attributeRef4;
        return this;
    }

    public void setAttributeRef4(String attributeRef4) {
        this.attributeRef4 = attributeRef4;
    }
	
    
    public String getAttributeRef5() {
        return attributeRef5;
    }

    public JeLines attributeRef5(String attributeRef5) {
        this.attributeRef5 = attributeRef5;
        return this;
    }

    public void setAttributeRef5(String attributeRef5) {
        this.attributeRef5 = attributeRef5;
    }
    
    public String getAttributeRef6() {
        return attributeRef6;
    }

    public JeLines attributeRef6(String attributeRef6) {
        this.attributeRef6 = attributeRef6;
        return this;
    }

    public void setAttributeRef6(String attributeRef6) {
        this.attributeRef6 = attributeRef6;
    }
    
 
	public String getAttrRef1Type() {
		return attrRef1Type;
	}

	public void setAttrRef1Type(String attrRef1Type) {
		this.attrRef1Type = attrRef1Type;
	}

	public String getAttrRef2Type() {
		return attrRef2Type;
	}

	public void setAttrRef2Type(String attrRef2Type) {
		this.attrRef2Type = attrRef2Type;
	}

	public String getAttrRef3Type() {
		return attrRef3Type;
	}

	public void setAttrRef3Type(String attrRef3Type) {
		this.attrRef3Type = attrRef3Type;
	}

	public String getAttrRef4Type() {
		return attrRef4Type;
	}

	public void setAttrRef4Type(String attrRef4Type) {
		this.attrRef4Type = attrRef4Type;
	}

	public String getAttrRef5Type() {
		return attrRef5Type;
	}

	public void setAttrRef5Type(String attrRef5Type) {
		this.attrRef5Type = attrRef5Type;
	}

	public String getAttrRef6Type() {
		return attrRef6Type;
	}

	public void setAttrRef6Type(String attrRef6Type) {
		this.attrRef6Type = attrRef6Type;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JeLines jeLines = (JeLines) o;
        if (jeLines.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, jeLines.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
	public String toString() {
		return "JeLines [id=" + id + ", jeBatchId=" + jeBatchId
				+ ", jeHeaderId=" + jeHeaderId + ", rowId=" + rowId
				+ ", lineNum=" + lineNum + ", descriptionAttribute="
				+ descriptionAttribute + ", currency=" + currency
				+ ", debitAmount=" + debitAmount + ", creditAmount="
				+ creditAmount + ", accountedDebit=" + accountedDebit
				+ ", accountedCredit=" + accountedCredit + ", comments="
				+ comments + ", createdBy=" + createdBy + ", lastUpdatedBy="
				+ lastUpdatedBy + ", createdDate=" + createdDate
				+ ", lastUpdatedDate=" + lastUpdatedDate + ", codeCombination="
				+ codeCombination + ", attributeRef1=" + attributeRef1
				+ ", attributeRef2=" + attributeRef2 + ", attributeRef3="
				+ attributeRef3 + ", attributeRef4=" + attributeRef4
				+ ", attributeRef5=" + attributeRef5 + ", attributeRef6="
				+ attributeRef6 + "]";
	}
}
