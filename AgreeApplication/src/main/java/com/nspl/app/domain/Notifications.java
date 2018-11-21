package com.nspl.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Notifications.
 */
@Entity
@Table(name = "t_notifications")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Notifications implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "module")
    private String module;

    @Column(name = "message")
    private String message;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "group_id")
    private Long groupId;
    
    @Column(name = "group_id_for_display")
    private String groupIdForDisplay;
    
    @Column(name = "is_viewed")
    private Boolean isViewed;

    @Column(name = "action_type")
    private String actionType;

    @Column(name = "action_value")
    private String actionValue;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "creation_date")
    private ZonedDateTime creationDate;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "last_updated_date")
    private ZonedDateTime lastUpdatedDate;

    @Column(name = "last_updated_by")
    private Long lastUpdatedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModule() {
        return module;
    }

    public Notifications module(String module) {
        this.module = module;
        return this;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getMessage() {
        return message;
    }

    public Notifications message(String message) {
        this.message = message;
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public Notifications userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getGroupIdForDisplay() {
		return groupIdForDisplay;
	}

	public void setGroupIdForDisplay(String groupIdForDisplay) {
		this.groupIdForDisplay = groupIdForDisplay;
	}

	public Boolean getIsViewed() {
		return isViewed;
	}

	public Boolean isIsViewed() {
        return isViewed;
    }

    public Notifications isViewed(Boolean isViewed) {
        this.isViewed = isViewed;
        return this;
    }

    public void setIsViewed(Boolean isViewed) {
        this.isViewed = isViewed;
    }

    public String getActionType() {
        return actionType;
    }

    public Notifications actionType(String actionType) {
        this.actionType = actionType;
        return this;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getActionValue() {
        return actionValue;
    }

    public Notifications actionValue(String actionValue) {
        this.actionValue = actionValue;
        return this;
    }

    public void setActionValue(String actionValue) {
        this.actionValue = actionValue;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public Notifications tenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public Notifications creationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public Notifications createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public Notifications lastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
        return this;
    }

    public void setLastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public Notifications lastUpdatedBy(Long lastUpdatedBy) {
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
        Notifications notifications = (Notifications) o;
        if (notifications.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, notifications.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
	public String toString() {
		return "Notifications [id=" + id + ", module=" + module + ", message="
				+ message + ", userId=" + userId + ", groupId=" + groupId
				+ ", groupIdForDisplay=" + groupIdForDisplay + ", isViewed="
				+ isViewed + ", actionType=" + actionType + ", actionValue="
				+ actionValue + ", tenantId=" + tenantId + ", creationDate="
				+ creationDate + ", createdBy=" + createdBy
				+ ", lastUpdatedDate=" + lastUpdatedDate + ", lastUpdatedBy="
				+ lastUpdatedBy + "]";
	}
}
