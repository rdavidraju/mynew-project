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
 * A LookUpType.
 */
@Entity
@Table(name = "look_up_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "lookuptype")
public class LookUpType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "look_up_type")
    private String lookUpType;

    @Column(name = "meaning")
    private String meaning;

    @Column(name = "description")
    private String description;

    @Column(name = "enable_flag")
    private Boolean enableFlag;

    @Column(name = "active_start_date")
    private ZonedDateTime activeStartDate;

    @Column(name = "active_end_date")
    private ZonedDateTime activeEndDate;

    @Column(name = "validation_type")
    private String validationType;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "creation_date")
    private ZonedDateTime creationDate;

    @Column(name = "last_updated_by")
    private Long lastUpdatedBy;

    @Column(name = "last_updated_date")
    private ZonedDateTime lastUpdatedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public LookUpType tenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getLookUpType() {
        return lookUpType;
    }

    public LookUpType lookUpType(String lookUpType) {
        this.lookUpType = lookUpType;
        return this;
    }

    public void setLookUpType(String lookUpType) {
        this.lookUpType = lookUpType;
    }

    public String getMeaning() {
        return meaning;
    }

    public LookUpType meaning(String meaning) {
        this.meaning = meaning;
        return this;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getDescription() {
        return description;
    }

    public LookUpType description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isEnableFlag() {
        return enableFlag;
    }

    public LookUpType enableFlag(Boolean enableFlag) {
        this.enableFlag = enableFlag;
        return this;
    }

    public void setEnableFlag(Boolean enableFlag) {
        this.enableFlag = enableFlag;
    }

    public ZonedDateTime getActiveStartDate() {
        return activeStartDate;
    }

    public LookUpType activeStartDate(ZonedDateTime activeStartDate) {
        this.activeStartDate = activeStartDate;
        return this;
    }

    public void setActiveStartDate(ZonedDateTime activeStartDate) {
        this.activeStartDate = activeStartDate;
    }

    public ZonedDateTime getActiveEndDate() {
        return activeEndDate;
    }

    public LookUpType activeEndDate(ZonedDateTime activeEndDate) {
        this.activeEndDate = activeEndDate;
        return this;
    }

    public void setActiveEndDate(ZonedDateTime activeEndDate) {
        this.activeEndDate = activeEndDate;
    }

    public String getValidationType() {
        return validationType;
    }

    public LookUpType validationType(String validationType) {
        this.validationType = validationType;
        return this;
    }

    public void setValidationType(String validationType) {
        this.validationType = validationType;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public LookUpType createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public LookUpType creationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public LookUpType lastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public ZonedDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public LookUpType lastUpdatedDate(ZonedDateTime lastUpdatedDate) {
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
        LookUpType lookUpType = (LookUpType) o;
        if (lookUpType.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, lookUpType.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "LookUpType{" +
            "id=" + id +
            ", tenantId='" + tenantId + "'" +
            ", lookUpType='" + lookUpType + "'" +
            ", meaning='" + meaning + "'" +
            ", description='" + description + "'" +
            ", enableFlag='" + enableFlag + "'" +
            ", activeStartDate='" + activeStartDate + "'" +
            ", activeEndDate='" + activeEndDate + "'" +
            ", validationType='" + validationType + "'" +
            ", createdBy='" + createdBy + "'" +
            ", creationDate='" + creationDate + "'" +
            ", lastUpdatedBy='" + lastUpdatedBy + "'" +
            ", lastUpdatedDate='" + lastUpdatedDate + "'" +
            '}';
    }
}
