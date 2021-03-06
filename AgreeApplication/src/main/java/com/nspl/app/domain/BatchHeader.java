package com.nspl.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A BatchHeader.
 */
@Entity
@Table(name = "t_batch_header")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "reconapplication", type="batchheader")
public class BatchHeader implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "batch_name")
    private String batchName;

    @Column(name = "jhi_type")
    private String type;

    @Column(name = "ext_ref")
    private String extRef;

    @Column(name = "job_ref")
    private String jobRef;

    @Column(name = "extracted_datetime")
    private ZonedDateTime extractedDatetime;

    @Column(name = "extraction_status")
    private String extractionStatus;

    @Column(name = "transformation_status")
    private String transformationStatus;

    @Column(name = "transformed_datetime")
    private ZonedDateTime transformedDatetime;

    @Column(name = "next_schedule")
    private String nextSchedule;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "last_updated_by")
    private Long lastUpdatedBy;

    @Column(name = "last_updatedate")
    private ZonedDateTime lastUpdatedate;
    
    @Column(name = "hold")
    private Boolean hold;

    @Column(name = "hold_reason")
    private String holReason;
    
    
    @Column(name = "enabled_flag")
    private Boolean enabledFlag;
    
    @Column(name = "profile_id")
    private Long profileId;
    
    @Column(name = "src_prf_asmt_id")
    private String srcPrfAsmtId;
    
    public Boolean getEnabledFlag() {
		return enabledFlag;
	}

	public void setEnabledFlag(Boolean enabledFlag) {
		this.enabledFlag = enabledFlag;
	}

	public Boolean getHold() {
		return hold;
	}

	public void setHold(Boolean hold) {
		this.hold = hold;
	}

	public String getHolReason() {
		return holReason;
	}

	public void setHolReason(String holReason) {
		this.holReason = holReason;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public BatchHeader tenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getBatchName() {
        return batchName;
    }

    public BatchHeader batchName(String batchName) {
        this.batchName = batchName;
        return this;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public String getType() {
        return type;
    }

    public BatchHeader type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExtRef() {
        return extRef;
    }

    public BatchHeader extRef(String extRef) {
        this.extRef = extRef;
        return this;
    }

    public void setExtRef(String extRef) {
        this.extRef = extRef;
    }

    public String getJobRef() {
        return jobRef;
    }

    public BatchHeader jobRef(String jobRef) {
        this.jobRef = jobRef;
        return this;
    }

    public void setJobRef(String jobRef) {
        this.jobRef = jobRef;
    }

    public ZonedDateTime getExtractedDatetime() {
        return extractedDatetime;
    }

    public BatchHeader extractedDatetime(ZonedDateTime extractedDatetime) {
        this.extractedDatetime = extractedDatetime;
        return this;
    }

    public void setExtractedDatetime(ZonedDateTime extractedDatetime) {
        this.extractedDatetime = extractedDatetime;
    }

    public String getExtractionStatus() {
        return extractionStatus;
    }

    public BatchHeader extractionStatus(String extractionStatus) {
        this.extractionStatus = extractionStatus;
        return this;
    }

    public void setExtractionStatus(String extractionStatus) {
        this.extractionStatus = extractionStatus;
    }

    public String getTransformationStatus() {
        return transformationStatus;
    }

    public BatchHeader transformationStatus(String transformationStatus) {
        this.transformationStatus = transformationStatus;
        return this;
    }

    public void setTransformationStatus(String transformationStatus) {
        this.transformationStatus = transformationStatus;
    }

    public ZonedDateTime getTransformedDatetime() {
        return transformedDatetime;
    }

    public BatchHeader transformedDatetime(ZonedDateTime transformedDatetime) {
        this.transformedDatetime = transformedDatetime;
        return this;
    }

    public void setTransformedDatetime(ZonedDateTime transformedDatetime) {
        this.transformedDatetime = transformedDatetime;
    }

    public String getNextSchedule() {
        return nextSchedule;
    }

    public BatchHeader nextSchedule(String nextSchedule) {
        this.nextSchedule = nextSchedule;
        return this;
    }

    public void setNextSchedule(String nextSchedule) {
        this.nextSchedule = nextSchedule;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public BatchHeader createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public BatchHeader createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public BatchHeader lastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public ZonedDateTime getLastUpdatedate() {
        return lastUpdatedate;
    }

    public BatchHeader lastUpdatedate(ZonedDateTime lastUpdatedate) {
        this.lastUpdatedate = lastUpdatedate;
        return this;
    }

    public void setLastUpdatedate(ZonedDateTime lastUpdatedate) {
        this.lastUpdatedate = lastUpdatedate;
    }
    
    

    public Long getProfileId() {
		return profileId;
	}

	public void setProfileId(Long profileId) {
		this.profileId = profileId;
	}

	public String getSrcPrfAsmtId() {
		return srcPrfAsmtId;
	}

	public void setSrcPrfAsmtId(String srcPrfAsmtId) {
		this.srcPrfAsmtId = srcPrfAsmtId;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BatchHeader batchHeader = (BatchHeader) o;
        if (batchHeader.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), batchHeader.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BatchHeader{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", batchName='" + getBatchName() + "'" +
            ", type='" + getType() + "'" +
            ", extRef='" + getExtRef() + "'" +
            ", jobRef='" + getJobRef() + "'" +
            ", extractedDatetime='" + getExtractedDatetime() + "'" +
            ", extractionStatus='" + getExtractionStatus() + "'" +
            ", transformationStatus='" + getTransformationStatus() + "'" +
            ", transformedDatetime='" + getTransformedDatetime() + "'" +
            ", nextSchedule='" + getNextSchedule() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastUpdatedBy='" + getLastUpdatedBy() + "'" +
            ", lastUpdatedate='" + getLastUpdatedate() + "'" +
            "}";
    }
}
