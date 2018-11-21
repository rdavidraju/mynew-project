package com.nspl.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A JournalsHeaderData.
 */
@Entity
@Table(name = "t_journals_header_data")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class JournalsHeaderData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "je_temp_id")
    private Long jeTempId;

    @Column(name = "je_batch_id")
    private Long jeBatchId;
    
    @Column(name = "je_batch_name")
    private String jeBatchName;

    @Column(name = "je_batch_date")
    private LocalDate jeBatchDate;

    @Column(name = "gl_date")
    private LocalDate glDate;

    @Column(name = "currency")
    private String currency;

    @Column(name = "conversion_type")
    private String conversionType;

    @Column(name = "category")
    private String category;

    @Column(name = "source")
    private String source;

    @Column(name = "ledger_name")
    private String ledgerName;

    @Column(name = "batch_total")
    private Integer batchTotal;
    
    @Column(name = "job_reference")
    private String jobReference;
    
    @Column(name = "rev_details")
    private String revDetails;

    @Column(name = "run_date")
    private LocalDate runDate;

    @Column(name = "submitted_by")
    private String submittedBy;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "last_updated_by")
    private Long lastUpdatedBy;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "last_updated_date")
    private ZonedDateTime lastUpdatedDate;
    
    
    
    @Column(name = "period")
    private String period;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public JournalsHeaderData tenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getJeTempId() {
        return jeTempId;
    }

    public JournalsHeaderData jeTempId(Long jeTempId) {
        this.jeTempId = jeTempId;
        return this;
    }

    public void setJeTempId(Long jeTempId) {
        this.jeTempId = jeTempId;
    }

    public Long getJeBatchId() {
		return jeBatchId;
	}

	public void setJeBatchId(Long jeBatchId) {
		this.jeBatchId = jeBatchId;
	}

	public String getJeBatchName() {
        return jeBatchName;
    }

    public JournalsHeaderData jeBatchName(String jeBatchName) {
        this.jeBatchName = jeBatchName;
        return this;
    }

    public void setJeBatchName(String jeBatchName) {
        this.jeBatchName = jeBatchName;
    }
    
    

    public LocalDate getJeBatchDate() {
        return jeBatchDate;
    }

    public JournalsHeaderData jeBatchDate(LocalDate jeBatchDate) {
        this.jeBatchDate = jeBatchDate;
        return this;
    }

    public void setJeBatchDate(LocalDate jeBatchDate) {
        this.jeBatchDate = jeBatchDate;
    }

    public LocalDate getGlDate() {
        return glDate;
    }

    public JournalsHeaderData glDate(LocalDate glDate) {
        this.glDate = glDate;
        return this;
    }

    public void setGlDate(LocalDate glDate) {
        this.glDate = glDate;
    }

    public String getCurrency() {
        return currency;
    }

    public JournalsHeaderData currency(String currency) {
        this.currency = currency;
        return this;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getConversionType() {
        return conversionType;
    }

    public JournalsHeaderData conversionType(String conversionType) {
        this.conversionType = conversionType;
        return this;
    }

    public void setConversionType(String conversionType) {
        this.conversionType = conversionType;
    }

    public String getCategory() {
        return category;
    }

    public JournalsHeaderData category(String category) {
        this.category = category;
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSource() {
        return source;
    }

    public JournalsHeaderData source(String source) {
        this.source = source;
        return this;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getLedgerName() {
        return ledgerName;
    }

    public JournalsHeaderData ledgerName(String ledgerName) {
        this.ledgerName = ledgerName;
        return this;
    }

    public void setLedgerName(String ledgerName) {
        this.ledgerName = ledgerName;
    }

    public Integer getBatchTotal() {
        return batchTotal;
    }

    public JournalsHeaderData batchTotal(Integer batchTotal) {
        this.batchTotal = batchTotal;
        return this;
    }

    public void setBatchTotal(Integer batchTotal) {
        this.batchTotal = batchTotal;
    }

    
    public String getJobReference() {
        return jobReference;
    }

    public JournalsHeaderData jobReference(String jobReference) {
        this.jobReference = jobReference;
        return this;
    }

    public void setJobReference(String jobReference) {
        this.jobReference = jobReference;
    }
    
    
    public String getRevDetails() {
		return revDetails;
	}

	public void setRevDetails(String revDetails) {
		this.revDetails = revDetails;
	}

	public LocalDate getRunDate() {
        return runDate;
    }

    public JournalsHeaderData runDate(LocalDate runDate) {
        this.runDate = runDate;
        return this;
    }

    public void setRunDate(LocalDate runDate) {
        this.runDate = runDate;
    }

    public String getSubmittedBy() {
        return submittedBy;
    }

    public JournalsHeaderData submittedBy(String submittedBy) {
        this.submittedBy = submittedBy;
        return this;
    }

    public void setSubmittedBy(String submittedBy) {
        this.submittedBy = submittedBy;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public JournalsHeaderData createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public JournalsHeaderData lastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public JournalsHeaderData createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public JournalsHeaderData lastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
        return this;
    }

    public void setLastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    
    public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JournalsHeaderData journalsHeaderData = (JournalsHeaderData) o;
        if (journalsHeaderData.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, journalsHeaderData.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
	public String toString() {
		return "JournalsHeaderData [id=" + id + ", tenantId=" + tenantId
				+ ", jeTempId=" + jeTempId + ", jeBatchId=" + jeBatchId
				+ ", jeBatchName=" + jeBatchName + ", jeBatchDate="
				+ jeBatchDate + ", glDate=" + glDate + ", currency=" + currency
				+ ", conversionType=" + conversionType + ", category="
				+ category + ", source=" + source + ", ledgerName="
				+ ledgerName + ", batchTotal=" + batchTotal + ", jobReference="
				+ jobReference + ", revDetails=" + revDetails + ", runDate="
				+ runDate + ", submittedBy=" + submittedBy + ", createdBy="
				+ createdBy + ", lastUpdatedBy=" + lastUpdatedBy
				+ ", createdDate=" + createdDate + ", lastUpdatedDate="
				+ lastUpdatedDate + ", period=" + period + "]";
	}
}
