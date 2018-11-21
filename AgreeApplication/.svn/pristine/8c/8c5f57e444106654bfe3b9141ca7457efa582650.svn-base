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
 * A ApplicationPrograms.
 */
@Entity
@Table(name = "t_application_programs")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "tapplicationprograms")
public class ApplicationPrograms implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "prgm_name")
    private String prgmName;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "prgm_description")
    private String prgmDescription;

    @Column(name = "generated_path")
    private String generatedPath;

    @Column(name = "target_path")
    private String targetPath;

    @Column(name = "prgm_path")
    private String prgmPath;

    @Column(name = "prgm_or_class_name")
    private String prgmOrClassName;

    @Column(name = "prgm_type")
    private String prgmType;

    @Column(name = "jhi_enable")
    private Boolean enable;

    @Column(name = "start_date")
    private ZonedDateTime startDate;

    @Column(name = "end_date")
    private ZonedDateTime endDate;

    @Column(name = "creation_date")
    private ZonedDateTime creationDate;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "last_updation_date")
    private ZonedDateTime lastUpdationDate;

    @Column(name = "last_updated_by")
    private Long lastUpdatedBy;

    @Column(name = "queue_name")
    private String queueName;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrgmName() {
        return prgmName;
    }

    public ApplicationPrograms prgmName(String prgmName) {
        this.prgmName = prgmName;
        return this;
    }

    public void setPrgmName(String prgmName) {
        this.prgmName = prgmName;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public ApplicationPrograms tenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getPrgmDescription() {
        return prgmDescription;
    }

    public ApplicationPrograms prgmDescription(String prgmDescription) {
        this.prgmDescription = prgmDescription;
        return this;
    }

    public void setPrgmDescription(String prgmDescription) {
        this.prgmDescription = prgmDescription;
    }

    public String getGeneratedPath() {
        return generatedPath;
    }

    public ApplicationPrograms generatedPath(String generatedPath) {
        this.generatedPath = generatedPath;
        return this;
    }

    public void setGeneratedPath(String generatedPath) {
        this.generatedPath = generatedPath;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public ApplicationPrograms targetPath(String targetPath) {
        this.targetPath = targetPath;
        return this;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    public String getPrgmPath() {
        return prgmPath;
    }

    public ApplicationPrograms prgmPath(String prgmPath) {
        this.prgmPath = prgmPath;
        return this;
    }

    public void setPrgmPath(String prgmPath) {
        this.prgmPath = prgmPath;
    }

    public String getPrgmOrClassName() {
        return prgmOrClassName;
    }

    public ApplicationPrograms prgmOrClassName(String prgmOrClassName) {
        this.prgmOrClassName = prgmOrClassName;
        return this;
    }

    public void setPrgmOrClassName(String prgmOrClassName) {
        this.prgmOrClassName = prgmOrClassName;
    }

    public String getPrgmType() {
        return prgmType;
    }

    public ApplicationPrograms prgmType(String prgmType) {
        this.prgmType = prgmType;
        return this;
    }

    public void setPrgmType(String prgmType) {
        this.prgmType = prgmType;
    }

    public Boolean isEnable() {
        return enable;
    }

    public ApplicationPrograms enable(Boolean enable) {
        this.enable = enable;
        return this;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public ApplicationPrograms startDate(ZonedDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public ApplicationPrograms endDate(ZonedDateTime endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public ApplicationPrograms creationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public ApplicationPrograms createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getLastUpdationDate() {
        return lastUpdationDate;
    }

    public ApplicationPrograms lastUpdationDate(ZonedDateTime lastUpdationDate) {
        this.lastUpdationDate = lastUpdationDate;
        return this;
    }

    public void setLastUpdationDate(ZonedDateTime lastUpdationDate) {
        this.lastUpdationDate = lastUpdationDate;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public ApplicationPrograms lastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }
    
    public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ApplicationPrograms applicationPrograms = (ApplicationPrograms) o;
        if (applicationPrograms.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, applicationPrograms.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
	public String toString() {
		return "ApplicationPrograms [id=" + id + ", prgmName=" + prgmName
				+ ", tenantId=" + tenantId + ", prgmDescription="
				+ prgmDescription + ", generatedPath=" + generatedPath
				+ ", targetPath=" + targetPath + ", prgmPath=" + prgmPath
				+ ", prgmOrClassName=" + prgmOrClassName + ", prgmType="
				+ prgmType + ", enable=" + enable + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", creationDate=" + creationDate
				+ ", createdBy=" + createdBy + ", lastUpdationDate="
				+ lastUpdationDate + ", lastUpdatedBy=" + lastUpdatedBy
				+ ", queueName=" + queueName + "]";
	}
}
