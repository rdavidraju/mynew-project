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
 * A JobDetails.
 */
@Entity
@Table(name = "t_job_details")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "tjobdetails")
public class JobDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_for_display")
    private String idForDisplay;
    
    @Column(name = "programm_id")
    private Long programmId;

    @Column(name = "job_name")
    private String jobName;

    @Column(name = "job_description")
    private String jobDescription;

    @Column(name = "jhi_enable")
    private Boolean enable;

    @Column(name = "start_date")
    private ZonedDateTime startDate;

    @Column(name = "end_date")
    private ZonedDateTime endDate;

    @Column(name = "parameter_arguments_1")
    private String parameterArguments1;

    @Column(name = "parameter_arguments_2")
    private String parameterArguments2;

    @Column(name = "parameter_arguments_3")
    private String parameterArguments3;

    @Column(name = "parameter_arguments_4")
    private String parameterArguments4;

    @Column(name = "parameter_arguments_5")
    private String parameterArguments5;

    @Column(name = "parameter_arguments_6")
    private String parameterArguments6;

    @Column(name = "parameter_arguments_7")
    private String parameterArguments7;

    @Column(name = "parameter_arguments_8")
    private String parameterArguments8;

    @Column(name = "parameter_arguments_9")
    private String parameterArguments9;

    @Column(name = "parameter_arguments_10")
    private String parameterArguments10;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "creation_date")
    private ZonedDateTime creationDate;

    @Column(name = "last_updated_by")
    private Long lastUpdatedBy;

    @Column(name = "last_updated_date")
    private ZonedDateTime lastUpdatedDate;

    @Column(name = "tenant_id")
    private Long tenantId;
    
    
    @Column(name = "job_status")
    private String jobStatus;
    
    @Column(name = "max_consecutive_fails")
    private Long maxConsecutiveFails;
    
    @Column(name = "send_fail_alerts")
    private Boolean sendFailAlerts;
    
    @Column(name = "transform_immediately")
    private Boolean transformImmediately;
    

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

    
    
    public Long getProgrammId() {
        return programmId;
    }

    public JobDetails programmId(Long programmId) {
        this.programmId = programmId;
        return this;
    }

    public void setProgrammId(Long programmId) {
        this.programmId = programmId;
    }

    public String getJobName() {
        return jobName;
    }

    public JobDetails jobName(String jobName) {
        this.jobName = jobName;
        return this;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public JobDetails jobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
        return this;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public Boolean isEnable() {
        return enable;
    }

    public JobDetails enable(Boolean enable) {
        this.enable = enable;
        return this;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public JobDetails startDate(ZonedDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public JobDetails endDate(ZonedDateTime endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public String getParameterArguments1() {
        return parameterArguments1;
    }

    public JobDetails parameterArguments1(String parameterArguments1) {
        this.parameterArguments1 = parameterArguments1;
        return this;
    }

    public void setParameterArguments1(String parameterArguments1) {
        this.parameterArguments1 = parameterArguments1;
    }

    public String getParameterArguments2() {
        return parameterArguments2;
    }

    public JobDetails parameterArguments2(String parameterArguments2) {
        this.parameterArguments2 = parameterArguments2;
        return this;
    }

    public void setParameterArguments2(String parameterArguments2) {
        this.parameterArguments2 = parameterArguments2;
    }

    public String getParameterArguments3() {
        return parameterArguments3;
    }

    public JobDetails parameterArguments3(String parameterArguments3) {
        this.parameterArguments3 = parameterArguments3;
        return this;
    }

    public void setParameterArguments3(String parameterArguments3) {
        this.parameterArguments3 = parameterArguments3;
    }

    public String getParameterArguments4() {
        return parameterArguments4;
    }

    public JobDetails parameterArguments4(String parameterArguments4) {
        this.parameterArguments4 = parameterArguments4;
        return this;
    }

    public void setParameterArguments4(String parameterArguments4) {
        this.parameterArguments4 = parameterArguments4;
    }

    public String getParameterArguments5() {
        return parameterArguments5;
    }

    public JobDetails parameterArguments5(String parameterArguments5) {
        this.parameterArguments5 = parameterArguments5;
        return this;
    }

    public void setParameterArguments5(String parameterArguments5) {
        this.parameterArguments5 = parameterArguments5;
    }

    public String getParameterArguments6() {
        return parameterArguments6;
    }

    public JobDetails parameterArguments6(String parameterArguments6) {
        this.parameterArguments6 = parameterArguments6;
        return this;
    }

    public void setParameterArguments6(String parameterArguments6) {
        this.parameterArguments6 = parameterArguments6;
    }

    public String getParameterArguments7() {
        return parameterArguments7;
    }

    public JobDetails parameterArguments7(String parameterArguments7) {
        this.parameterArguments7 = parameterArguments7;
        return this;
    }

    public void setParameterArguments7(String parameterArguments7) {
        this.parameterArguments7 = parameterArguments7;
    }

    public String getParameterArguments8() {
        return parameterArguments8;
    }

    public JobDetails parameterArguments8(String parameterArguments8) {
        this.parameterArguments8 = parameterArguments8;
        return this;
    }

    public void setParameterArguments8(String parameterArguments8) {
        this.parameterArguments8 = parameterArguments8;
    }

    public String getParameterArguments9() {
        return parameterArguments9;
    }

    public JobDetails parameterArguments9(String parameterArguments9) {
        this.parameterArguments9 = parameterArguments9;
        return this;
    }

    public void setParameterArguments9(String parameterArguments9) {
        this.parameterArguments9 = parameterArguments9;
    }

    public String getParameterArguments10() {
        return parameterArguments10;
    }

    public JobDetails parameterArguments10(String parameterArguments10) {
        this.parameterArguments10 = parameterArguments10;
        return this;
    }

    public void setParameterArguments10(String parameterArguments10) {
        this.parameterArguments10 = parameterArguments10;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public JobDetails createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public JobDetails creationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public JobDetails lastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public ZonedDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public JobDetails lastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
        return this;
    }

    public void setLastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public JobDetails tenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
    
    public String getJobStatus() {
        return jobStatus;
    }

    public JobDetails jobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
        return this;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }
    
    
    public Long getMaxConsecutiveFails() {
        return maxConsecutiveFails;
    }

    public JobDetails maxConsecutiveFails(Long maxConsecutiveFails) {
        this.maxConsecutiveFails = maxConsecutiveFails;
        return this;
    }

    public void setMaxConsecutiveFails(Long maxConsecutiveFails) {
        this.maxConsecutiveFails = maxConsecutiveFails;
    }
    
    
    public Boolean isSendFailAlerts() {
        return sendFailAlerts;
    }

    public JobDetails sendFailAlerts(Boolean sendFailAlerts) {
        this.sendFailAlerts = sendFailAlerts;
        return this;
    }

    public void setSendFailAlerts(Boolean sendFailAlerts) {
        this.sendFailAlerts = sendFailAlerts;
    }

    public Boolean getTransformImmediately() {
		return transformImmediately;
	}

	public void setTransformImmediately(Boolean transformImmediately) {
		this.transformImmediately = transformImmediately;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JobDetails jobDetails = (JobDetails) o;
        if (jobDetails.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, jobDetails.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "JobDetails{" +
            "id=" + id +
            "idForDisplay=" + idForDisplay +
            ", programmId='" + programmId + "'" +
            ", jobName='" + jobName + "'" +
            ", jobDescription='" + jobDescription + "'" +
            ", enable='" + enable + "'" +
            ", startDate='" + startDate + "'" +
            ", endDate='" + endDate + "'" +
            ", parameterArguments1='" + parameterArguments1 + "'" +
            ", parameterArguments2='" + parameterArguments2 + "'" +
            ", parameterArguments3='" + parameterArguments3 + "'" +
            ", parameterArguments4='" + parameterArguments4 + "'" +
            ", parameterArguments5='" + parameterArguments5 + "'" +
            ", parameterArguments6='" + parameterArguments6 + "'" +
            ", parameterArguments7='" + parameterArguments7 + "'" +
            ", parameterArguments8='" + parameterArguments8 + "'" +
            ", parameterArguments9='" + parameterArguments9 + "'" +
            ", parameterArguments10='" + parameterArguments10 + "'" +
            ", createdBy='" + createdBy + "'" +
            ", creationDate='" + creationDate + "'" +
            ", lastUpdatedBy='" + lastUpdatedBy + "'" +
            ", lastUpdatedDate='" + lastUpdatedDate + "'" +
            ", tenantId='" + tenantId + "'" +
            '}';
    }
}
