package com.nspl.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A RoleFunctionAssignment.
 */
@Entity
@Table(name = "role_function_assignment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RoleFunctionAssignment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "function_id")
    private Long functionId;

    @Column(name = "assigned_by")
    private Long assignedBy;

    @Column(name = "active_flag")
    private Boolean activeFlag;

    @Column(name = "delete_flag")
    private Boolean deleteFlag;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "can_view")
    private Boolean canView;

    @Column(name = "can_insert")
    private Boolean canInsert;

    @Column(name = "can_update")
    private Boolean canUpdate;

    @Column(name = "can_execute")
    private Boolean canExecute;

    @Column(name = "can_delete")
    private Boolean canDelete;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "last_update_by")
    private Long lastUpdateBy;

    @Column(name = "creation_date")
    private ZonedDateTime creationDate;

    @Column(name = "last_updated_date")
    private ZonedDateTime lastUpdatedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoleId() {
        return roleId;
    }

    public RoleFunctionAssignment roleId(Long roleId) {
        this.roleId = roleId;
        return this;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getFunctionId() {
        return functionId;
    }

    public RoleFunctionAssignment functionId(Long functionId) {
        this.functionId = functionId;
        return this;
    }

    public void setFunctionId(Long functionId) {
        this.functionId = functionId;
    }

    public Long getAssignedBy() {
        return assignedBy;
    }

    public RoleFunctionAssignment assignedBy(Long assignedBy) {
        this.assignedBy = assignedBy;
        return this;
    }

    public void setAssignedBy(Long assignedBy) {
        this.assignedBy = assignedBy;
    }

    public Boolean isActiveFlag() {
        return activeFlag;
    }

    public RoleFunctionAssignment activeFlag(Boolean activeFlag) {
        this.activeFlag = activeFlag;
        return this;
    }

    public void setActiveFlag(Boolean activeFlag) {
        this.activeFlag = activeFlag;
    }

    public Boolean isDeleteFlag() {
        return deleteFlag;
    }

    public RoleFunctionAssignment deleteFlag(Boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
        return this;
    }

    public void setDeleteFlag(Boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public RoleFunctionAssignment startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public RoleFunctionAssignment endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Boolean isCanView() {
        return canView;
    }

    public RoleFunctionAssignment canView(Boolean canView) {
        this.canView = canView;
        return this;
    }

    public void setCanView(Boolean canView) {
        this.canView = canView;
    }

    public Boolean isCanInsert() {
        return canInsert;
    }

    public RoleFunctionAssignment canInsert(Boolean canInsert) {
        this.canInsert = canInsert;
        return this;
    }

    public void setCanInsert(Boolean canInsert) {
        this.canInsert = canInsert;
    }

    public Boolean isCanUpdate() {
        return canUpdate;
    }

    public RoleFunctionAssignment canUpdate(Boolean canUpdate) {
        this.canUpdate = canUpdate;
        return this;
    }

    public void setCanUpdate(Boolean canUpdate) {
        this.canUpdate = canUpdate;
    }

    public Boolean isCanExecute() {
        return canExecute;
    }

    public RoleFunctionAssignment canExecute(Boolean canExecute) {
        this.canExecute = canExecute;
        return this;
    }

    public void setCanExecute(Boolean canExecute) {
        this.canExecute = canExecute;
    }

    public Boolean isCanDelete() {
        return canDelete;
    }

    public RoleFunctionAssignment canDelete(Boolean canDelete) {
        this.canDelete = canDelete;
        return this;
    }

    public void setCanDelete(Boolean canDelete) {
        this.canDelete = canDelete;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public RoleFunctionAssignment createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getLastUpdateBy() {
        return lastUpdateBy;
    }

    public RoleFunctionAssignment lastUpdateBy(Long lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
        return this;
    }

    public void setLastUpdateBy(Long lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public RoleFunctionAssignment creationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public ZonedDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public RoleFunctionAssignment lastUpdatedDate(ZonedDateTime lastUpdatedDate) {
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
        RoleFunctionAssignment roleFunctionAssignment = (RoleFunctionAssignment) o;
        if (roleFunctionAssignment.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), roleFunctionAssignment.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RoleFunctionAssignment{" +
            "id=" + getId() +
            ", roleId='" + getRoleId() + "'" +
            ", functionId='" + getFunctionId() + "'" +
            ", assignedBy='" + getAssignedBy() + "'" +
            ", activeFlag='" + isActiveFlag() + "'" +
            ", deleteFlag='" + isDeleteFlag() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", canView='" + isCanView() + "'" +
            ", canInsert='" + isCanInsert() + "'" +
            ", canUpdate='" + isCanUpdate() + "'" +
            ", canExecute='" + isCanExecute() + "'" +
            ", canDelete='" + isCanDelete() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastUpdateBy='" + getLastUpdateBy() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", lastUpdatedDate='" + getLastUpdatedDate() + "'" +
            "}";
    }
}
