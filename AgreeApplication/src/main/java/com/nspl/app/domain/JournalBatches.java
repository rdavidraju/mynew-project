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
 * A JournalBatches.
 */
@Entity
@Table(name = "t_journal_batches")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "journalbatches")
public class JournalBatches implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "journal_batch_name")
    private String journalBatchName;

    @Column(name = "je_temp_id")
    private Long jeTempId;

    @Column(name = "je_batch_date")
    private LocalDate jeBatchDate;

    @Column(name = "job_reference")
    private String jobReference;
    
    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "last_updated_by")
    private Long lastUpdatedBy;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "last_updated_date")
    private ZonedDateTime lastUpdatedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJournalBatchName() {
        return journalBatchName;
    }

    public JournalBatches journalBatchName(String journalBatchName) {
        this.journalBatchName = journalBatchName;
        return this;
    }

    public void setJournalBatchName(String journalBatchName) {
        this.journalBatchName = journalBatchName;
    }

    public Long getJeTempId() {
        return jeTempId;
    }

    public JournalBatches jeTempId(Long jeTempId) {
        this.jeTempId = jeTempId;
        return this;
    }

    public void setJeTempId(Long jeTempId) {
        this.jeTempId = jeTempId;
    }

    public LocalDate getJeBatchDate() {
        return jeBatchDate;
    }

    public JournalBatches jeBatchDate(LocalDate jeBatchDate) {
        this.jeBatchDate = jeBatchDate;
        return this;
    }

    public void setJeBatchDate(LocalDate jeBatchDate) {
        this.jeBatchDate = jeBatchDate;
    }

    public String getJobReference() {
		return jobReference;
	}

	public void setJobReference(String jobReference) {
		this.jobReference = jobReference;
	}

	public Long getTenantId() {
        return tenantId;
    }

    public JournalBatches tenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public JournalBatches createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public JournalBatches lastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public JournalBatches createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public JournalBatches lastUpdatedDate(ZonedDateTime lastUpdatedDate) {
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
        JournalBatches journalBatches = (JournalBatches) o;
        if (journalBatches.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, journalBatches.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
	public String toString() {
		return "JournalBatches [id=" + id + ", journalBatchName="
				+ journalBatchName + ", jeTempId=" + jeTempId
				+ ", jeBatchDate=" + jeBatchDate + ", jobReference="
				+ jobReference + ", tenantId=" + tenantId + ", createdBy="
				+ createdBy + ", lastUpdatedBy=" + lastUpdatedBy
				+ ", createdDate=" + createdDate + ", lastUpdatedDate="
				+ lastUpdatedDate + "]";
	}
}
