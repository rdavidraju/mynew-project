package com.nspl.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A ApprovalGroups.
 */
@Entity
@Table(name = "t_approval_groups")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ApprovalGroups implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "description")
    private String description;

   /* @Column(name = "status")
    private String status;*/
    
    @Column(name = "enabled_flag")
    private Boolean enabledFlag;

    @Column(name = "start_date")
    private ZonedDateTime startDate;

    @Column(name = "end_date")
    private ZonedDateTime endDate;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "last_updated_by")
    private Long lastUpdatedBy;

    @Column(name = "last_updated_date")
    private ZonedDateTime lastUpdatedDate;
    
    @Column(name = "type")
    private String type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public ApprovalGroups groupName(String groupName) {
        this.groupName = groupName;
        return this;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public ApprovalGroups tenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getDescription() {
        return description;
    }

    public ApprovalGroups description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

   /* public String getStatus() {
        return status;
    }

    public ApprovalGroups status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }*/

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public ApprovalGroups startDate(ZonedDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public ApprovalGroups endDate(ZonedDateTime endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public ApprovalGroups createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public ApprovalGroups createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public ApprovalGroups lastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public ZonedDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public ApprovalGroups lastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
        return this;
    }

    public void setLastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }
    
    
    
    public Boolean isEnabledFlag() {
        return enabledFlag;
    }

    public ApprovalGroups enabledFlag(Boolean enabledFlag) {
        this.enabledFlag = enabledFlag;
        return this;
    }

    public void setEnabledFlag(Boolean enabledFlag) {
        this.enabledFlag = enabledFlag;
    }
    
    
    

    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ApprovalGroups approvalGroups = (ApprovalGroups) o;
        if (approvalGroups.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, approvalGroups.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ApprovalGroups{" +
            "id=" + id +
            ", groupName='" + groupName + "'" +
            ", tenantId='" + tenantId + "'" +
            ", description='" + description + "'" +
          //  ", status='" + status + "'" +
            ", startDate='" + startDate + "'" +
            ", endDate='" + endDate + "'" +
            ", createdDate='" + createdDate + "'" +
            ", createdBy='" + createdBy + "'" +
            ", lastUpdatedBy='" + lastUpdatedBy + "'" +
            ", lastUpdatedDate='" + lastUpdatedDate + "'" +
            ", enabledFlag='" + enabledFlag + "'" +
            ", type='" + type + "'" +
            '}';
    }
}
