package com.nspl.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Reports.
 */
@Entity
@Table(name = "t_reports")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Reports implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "id_for_display")
    private String idForDisplay;

    @Column(name = "report_type_id")
    private Long reportTypeId;

    @Column(name = "report_name")
    private String reportName;

    @Column(name = "source_view_id")
    private Long sourceViewId;

    @Column(name = "enable_flag")
    private Boolean enableFlag;

    @Column(name = "start_date")
    private ZonedDateTime startDate;

    @Column(name = "end_date")
    private ZonedDateTime endDate;

    @Column(name = "creation_date")
    private ZonedDateTime creationDate;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "last_updated_date")
    private ZonedDateTime lastUpdatedDate;

    @Column(name = "last_updated_by")
    private Long lastUpdatedBy;
    
    @Column(name = "tenant_id")
    private Long tenantId;
    
    @Column(name = "report_mode")
    private String reportMode;
    
    @Column(name = "allow_drill_down")
    private Boolean allowDrillDown;
    
    @Column(name = "acc_val")
    private String accVal;
    
    @Column(name = "rec_val")
    private String recVal;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "report_view_type")
    private String reportViewType;
    
    @Column(name = "report_val_01")
    private String reportVal01;
    
    @Column(name = "report_val_02")
    private String reportVal02;
    
    @Column(name = "recon_view_id")
    private Long reconViewId;
    
    public String getReportVal01() {
		return reportVal01;
	}

	public void setReportVal01(String reportVal01) {
		this.reportVal01 = reportVal01;
	}

	public String getReportVal02() {
		return reportVal02;
	}

	public void setReportVal02(String reportVal02) {
		this.reportVal02 = reportVal02;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRecVal() {
		return recVal;
	}

	public void setRecVal(String recVal) {
		this.recVal = recVal;
	}

	public String getAccVal() {
		return accVal;
	}

	public void setAccVal(String accVal) {
		this.accVal = accVal;
	}

	public Boolean getAllowDrillDown() {
		return allowDrillDown;
	}

	public void setAllowDrillDown(Boolean allowDrillDown) {
		this.allowDrillDown = allowDrillDown;
	}

    public String getReportViewType() {
		return reportViewType;
	}

	public void setReportViewType(String reportViewType) {
		this.reportViewType = reportViewType;
	}

	public String getReportMode() {
		return reportMode;
	}

	public void setReportMode(String reportMode) {
		this.reportMode = reportMode;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getIdForDisplay() {
		return idForDisplay;
	}

	public void setIdForDisplay(String idForDisplay) {
		this.idForDisplay = idForDisplay;
	}


    public Long getReportTypeId() {
        return reportTypeId;
    }

    public Reports reportTypeId(Long reportTypeId) {
        this.reportTypeId = reportTypeId;
        return this;
    }

    public void setReportTypeId(Long reportTypeId) {
        this.reportTypeId = reportTypeId;
    }

    public String getReportName() {
        return reportName;
    }

    public Reports reportName(String reportName) {
        this.reportName = reportName;
        return this;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public Long getSourceViewId() {
        return sourceViewId;
    }

    public Reports sourceViewId(Long sourceViewId) {
        this.sourceViewId = sourceViewId;
        return this;
    }

    public void setSourceViewId(Long sourceViewId) {
        this.sourceViewId = sourceViewId;
    }

    public Boolean isEnableFlag() {
        return enableFlag;
    }

    public Reports enableFlag(Boolean enableFlag) {
        this.enableFlag = enableFlag;
        return this;
    }

    public void setEnableFlag(Boolean enableFlag) {
        this.enableFlag = enableFlag;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public Reports startDate(ZonedDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public Reports endDate(ZonedDateTime endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public Reports creationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public Reports createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public Reports lastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
        return this;
    }

    public void setLastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public Reports lastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }
    
    public Long getReconViewId() {
		return reconViewId;
	}

	public void setReconViewId(Long reconViewId) {
		this.reconViewId = reconViewId;
	}

	public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reports reports = (Reports) o;
        if (reports.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reports.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
	public String toString() {
		return "Reports [id=" + id + ", idForDisplay=" + idForDisplay
				+ ", reportTypeId=" + reportTypeId + ", reportName="
				+ reportName + ", sourceViewId=" + sourceViewId
				+ ", enableFlag=" + enableFlag + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", creationDate=" + creationDate
				+ ", createdBy=" + createdBy + ", lastUpdatedDate="
				+ lastUpdatedDate + ", lastUpdatedBy=" + lastUpdatedBy
				+ ", tenantId=" + tenantId + ", reportMode=" + reportMode
				+ ", allowDrillDown=" + allowDrillDown + ", accVal=" + accVal
				+ ", recVal=" + recVal + ", description=" + description
				+ ", reportViewType=" + reportViewType + ", reportVal01="
				+ reportVal01 + ", reportVal02=" + reportVal02
				+ ", reconViewId=" + reconViewId + "]";
	}
}
