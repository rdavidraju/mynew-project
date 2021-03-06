package com.nspl.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A ReportRequests.
 */
@Entity
@Table(name = "t_report_requests")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ReportRequests implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "id_for_display")
    private String idForDisplay;

    @Column(name = "req_name")
    private String reqName;

    @Column(name = "report_id")
    private Long reportId;

    @Column(name = "output_path")
    private String outputPath;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "status")
    private String status;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "last_updated_by")
    private Long lastUpdatedBy;

    @Column(name = "last_updated_date")
    private ZonedDateTime lastUpdatedDate;
    
    @Column(name = "submitted_time")
    private ZonedDateTime submittedTime;
    
    @Column(name = "generated_time")
    private ZonedDateTime generatedTime;
    
    @Column(name = "job_id")
    private String jobId;
    
    @Column(name = "filter_map")
    private String filterMap;
    
    @Column(name = "request_type")
    private String requestType;
    
    @Column(name = "pivot_path")
    private String pivotPath;
    
    @Column(name = "sys_req_name")
    private String sysReqName;

    public String getPivotPath() {
		return pivotPath;
	}

	public void setPivotPath(String pivotPath) {
		this.pivotPath = pivotPath;
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

	public String getReqName() {
        return reqName;
    }

    public ReportRequests reqName(String reqName) {
        this.reqName = reqName;
        return this;
    }

    public void setReqName(String reqName) {
        this.reqName = reqName;
    }

    public Long getReportId() {
        return reportId;
    }

    public ReportRequests reportId(Long reportId) {
        this.reportId = reportId;
        return this;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public ReportRequests outputPath(String outputPath) {
        this.outputPath = outputPath;
        return this;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public String getFileName() {
        return fileName;
    }

    public ReportRequests fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public ReportRequests tenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getStatus() {
        return status;
    }

    public ReportRequests status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public ReportRequests createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public ReportRequests createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public ReportRequests lastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public ZonedDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public ReportRequests lastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
        return this;
    }

    public void setLastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }
    
    
    
    

    public ZonedDateTime getSubmittedTime() {
		return submittedTime;
	}

	public void setSubmittedTime(ZonedDateTime submittedTime) {
		this.submittedTime = submittedTime;
	}

	public ZonedDateTime getGeneratedTime() {
		return generatedTime;
	}

	public void setGeneratedTime(ZonedDateTime generatedTime) {
		this.generatedTime = generatedTime;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getFilterMap() {
		return filterMap;
	}

	public void setFilterMap(String filterMap) {
		this.filterMap = filterMap;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getSysReqName() {
		return sysReqName;
	}

	public void setSysReqName(String sysReqName) {
		this.sysReqName = sysReqName;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReportRequests reportRequests = (ReportRequests) o;
        if (reportRequests.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reportRequests.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
	public String toString() {
		return "ReportRequests [id=" + id + ", idForDisplay=" + idForDisplay
				+ ", reqName=" + reqName + ", reportId=" + reportId
				+ ", outputPath=" + outputPath + ", fileName=" + fileName
				+ ", tenantId=" + tenantId + ", status=" + status
				+ ", createdDate=" + createdDate + ", createdBy=" + createdBy
				+ ", lastUpdatedBy=" + lastUpdatedBy + ", lastUpdatedDate="
				+ lastUpdatedDate + ", submittedTime=" + submittedTime
				+ ", generatedTime=" + generatedTime + ", jobId=" + jobId
				+ ", filterMap=" + filterMap + ", requestType=" + requestType
				+ ", pivotPath=" + pivotPath + ", sysReqName=" + sysReqName
				+ "]";
	}
    
    
    
}
