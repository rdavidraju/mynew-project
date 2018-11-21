package com.nspl.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A ReportDefination.
 */
@Entity
@Table(name = "t_report_defination")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ReportDefination implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "report_id")
    private Long reportId;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "ref_type_id")
    private String refTypeId;

    @Column(name = "ref_src_id")
    private Long refSrcId;

    @Column(name = "ref_col_id")
    private Long refColId;

    @Column(name = "data_type")
    private String dataType;

    @Column(name = "goup_by")
    private Boolean goupBy;

    @Column(name = "creation_date")
    private ZonedDateTime creationDate;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "last_updated_date")
    private ZonedDateTime lastUpdatedDate;

    @Column(name = "last_updated_by")
    private Long lastUpdatedBy;
    
    @Column(name = "layout_val")
    private String layoutVal;
    
    @Column(name = "conditional_operator")
    private String conditionalOperator;
    
    @Column(name = "conditional_val")
    private String conditionalVal;
    
    public String getLayoutVal() {
		return layoutVal;
	}

	public void setLayoutVal(String layoutVal) {
		this.layoutVal = layoutVal;
	}

	public String getConditionalOperator() {
		return conditionalOperator;
	}

	public void setConditionalOperator(String conditionalOperator) {
		this.conditionalOperator = conditionalOperator;
	}

	public String getConditionalVal() {
		return conditionalVal;
	}

	public void setConditionalVal(String conditionalVal) {
		this.conditionalVal = conditionalVal;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReportId() {
        return reportId;
    }

    public ReportDefination reportId(Long reportId) {
        this.reportId = reportId;
        return this;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ReportDefination displayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getRefTypeId() {
        return refTypeId;
    }

    public ReportDefination refTypeId(String refTypeId) {
        this.refTypeId = refTypeId;
        return this;
    }

    public void setRefTypeId(String refTypeId) {
        this.refTypeId = refTypeId;
    }

    public Long getRefSrcId() {
        return refSrcId;
    }

    public ReportDefination refSrcId(Long refSrcId) {
        this.refSrcId = refSrcId;
        return this;
    }

    public void setRefSrcId(Long refSrcId) {
        this.refSrcId = refSrcId;
    }

    public Long getRefColId() {
        return refColId;
    }

    public ReportDefination refColId(Long refColId) {
        this.refColId = refColId;
        return this;
    }

    public void setRefColId(Long refColId) {
        this.refColId = refColId;
    }

    public String getDataType() {
        return dataType;
    }

    public ReportDefination dataType(String dataType) {
        this.dataType = dataType;
        return this;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Boolean isGoupBy() {
        return goupBy;
    }

    public ReportDefination goupBy(Boolean goupBy) {
        this.goupBy = goupBy;
        return this;
    }

    public void setGoupBy(Boolean goupBy) {
        this.goupBy = goupBy;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public ReportDefination creationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public ReportDefination createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public ReportDefination lastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
        return this;
    }

    public void setLastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public ReportDefination lastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
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
        ReportDefination reportDefination = (ReportDefination) o;
        if (reportDefination.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reportDefination.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ReportDefination{" +
            "id=" + getId() +
            ", reportId='" + getReportId() + "'" +
            ", displayName='" + getDisplayName() + "'" +
            ", refTypeId='" + getRefTypeId() + "'" +
            ", refSrcId='" + getRefSrcId() + "'" +
            ", refColId='" + getRefColId() + "'" +
            ", dataType='" + getDataType() + "'" +
            ", goupBy='" + isGoupBy() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastUpdatedDate='" + getLastUpdatedDate() + "'" +
            ", lastUpdatedBy='" + getLastUpdatedBy() + "'" +
            ", layoutVal='" + getLayoutVal() + "'" +
            ", conditionalOperator='" + getConditionalOperator() + "'" +
            ", conditionalVal='" + getConditionalVal() + "'" +
            "}";
    }
}
