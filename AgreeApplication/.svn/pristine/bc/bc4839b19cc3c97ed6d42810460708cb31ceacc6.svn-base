package com.nspl.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A JobActions.
 */
@Entity
@Table(name = "t_job_actions")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "tjobactions")
public class JobActions implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "step")
    private Long step;

    @Column(name = "action_name")
    private String actionName;

    @Column(name = "scheduler_id")
    private Long schedulerId;

    @Column(name = "status")
    private String status;
    
    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "last_updated_by")
    private Long lastUpdatedBy;

    @Column(name = "last_updated_date")
    private ZonedDateTime lastUpdatedDate;
    
    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "job_id")
    private String jobId;
    
    @Column(name = "start_time")
    private ZonedDateTime startTime;

    @Column(name = "end_time")
    private ZonedDateTime endTime;
    
    @Column(name = "enabled_flag")
    private Boolean enabledFlag;
    
    
    
    

    public Boolean getEnabledFlag() {
		return enabledFlag;
	}

	public void setEnabledFlag(Boolean enabledFlag) {
		this.enabledFlag = enabledFlag;
	}

	public ZonedDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(ZonedDateTime startTime) {
		this.startTime = startTime;
	}

	public ZonedDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(ZonedDateTime endTime) {
		this.endTime = endTime;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public ZonedDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(ZonedDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public Long getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(Long lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public ZonedDateTime getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(ZonedDateTime lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStep() {
        return step;
    }

    public JobActions step(Long step) {
        this.step = step;
        return this;
    }

    public void setStep(Long step) {
        this.step = step;
    }

    public String getActionName() {
        return actionName;
    }

    public JobActions actionName(String actionName) {
        this.actionName = actionName;
        return this;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public Long getSchedulerId() {
        return schedulerId;
    }

    public JobActions schedulerId(Long schedulerId) {
        this.schedulerId = schedulerId;
        return this;
    }

    public void setSchedulerId(Long schedulerId) {
        this.schedulerId = schedulerId;
    }

    public String getStatus() {
        return status;
    }

    public JobActions status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JobActions jobActions = (JobActions) o;
        if (jobActions.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, jobActions.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "JobActions{" +
            "id=" + id +
            ", step='" + step + "'" +
            ", actionName='" + actionName + "'" +
            ", schedulerId='" + schedulerId + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
