package com.nspl.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Hierarchy.
 */
@Entity
@Table(name = "t_hierarchy")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Hierarchy implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "object_name")
    private String objectName;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "object_type")
    private String objectType;

    @Column(name = "enable_flag")
    private Boolean enableFlag;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "last_updated_date")
    private ZonedDateTime lastUpdatedDate;

    @Column(name = "last_updated_by")
    private Long lastUpdatedBy;
    
    @Column(name = "id_for_display")
    private String idForDisplay;
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObjectName() {
        return objectName;
    }

    public Hierarchy objectName(String objectName) {
        this.objectName = objectName;
        return this;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public Long getParentId() {
        return parentId;
    }

    public Hierarchy parentId(Long parentId) {
        this.parentId = parentId;
        return this;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getObjectType() {
        return objectType;
    }

    public Hierarchy objectType(String objectType) {
        this.objectType = objectType;
        return this;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public Boolean isEnableFlag() {
        return enableFlag;
    }

    public Hierarchy enableFlag(Boolean enableFlag) {
        this.enableFlag = enableFlag;
        return this;
    }

    public void setEnableFlag(Boolean enableFlag) {
        this.enableFlag = enableFlag;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public Hierarchy tenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public Hierarchy startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Hierarchy endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public Hierarchy createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public Hierarchy createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public Hierarchy lastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
        return this;
    }

    public void setLastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public Hierarchy lastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    
    public String getIdForDisplay() {
		return idForDisplay;
	}

	public void setIdForDisplay(String idForDisplay) {
		this.idForDisplay = idForDisplay;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Hierarchy hierarchy = (Hierarchy) o;
        if (hierarchy.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), hierarchy.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Hierarchy{" +
            "id=" + getId() +
            ", objectName='" + getObjectName() + "'" +
            ", parentId='" + getParentId() + "'" +
            ", objectType='" + getObjectType() + "'" +
            ", enableFlag='" + isEnableFlag() + "'" +
            ", tenantId='" + getTenantId() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastUpdatedDate='" + getLastUpdatedDate() + "'" +
            ", lastUpdatedBy='" + getLastUpdatedBy() + "'" +
            "}";
    }
}
