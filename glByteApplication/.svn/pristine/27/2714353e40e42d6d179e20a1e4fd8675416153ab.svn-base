package com.nspl.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A ReportingDashboard.
 */
@Entity
@Table(name = "t_reporting_dashboard")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "reportingdashboard")
public class ReportingDashboard implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "name")
    private String name;

    @Column(name = "default_flag")
    private Boolean defaultFlag;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "udated_date")
    private LocalDate udatedDate;
    
    @Column(name = "id_for_display")
    private String idForDisplay;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public ReportingDashboard tenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getUserId() {
        return userId;
    }

    public ReportingDashboard userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public ReportingDashboard name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isDefaultFlag() {
        return defaultFlag;
    }

    public ReportingDashboard defaultFlag(Boolean defaultFlag) {
        this.defaultFlag = defaultFlag;
        return this;
    }

    public void setDefaultFlag(Boolean defaultFlag) {
        this.defaultFlag = defaultFlag;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public ReportingDashboard createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public ReportingDashboard createdDate(LocalDate createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public ReportingDashboard updatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDate getUdatedDate() {
        return udatedDate;
    }

    public ReportingDashboard udatedDate(LocalDate udatedDate) {
        this.udatedDate = udatedDate;
        return this;
    }

    public void setUdatedDate(LocalDate udatedDate) {
        this.udatedDate = udatedDate;
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
        ReportingDashboard reportingDashboard = (ReportingDashboard) o;
        if (reportingDashboard.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reportingDashboard.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

	@Override
	public String toString() {
		return "ReportingDashboard [id=" + id + ", tenantId=" + tenantId + ", userId=" + userId + ", name=" + name
				+ ", defaultFlag=" + defaultFlag + ", createdBy=" + createdBy + ", createdDate=" + createdDate
				+ ", updatedBy=" + updatedBy + ", udatedDate=" + udatedDate + ", idForDisplay=" + idForDisplay + "]";
	}

    
}
