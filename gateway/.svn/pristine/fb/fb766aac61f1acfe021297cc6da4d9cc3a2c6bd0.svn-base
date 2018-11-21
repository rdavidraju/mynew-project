package com.nspl.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A UserComments.
 */
@Entity
@Table(name = "t_user_comments")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserComments implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subject")
    private String subject;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "recipient_user_id")
    private Long recipientUserId;

    @Column(name = "message_body")
    private String messageBody;

    @Column(name = "replied_to_comment_id")
    private Long repliedToCommentId;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_read")
    private Boolean isRead;

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

    public String getSubject() {
        return subject;
    }

    public UserComments subject(String subject) {
        this.subject = subject;
        return this;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public UserComments tenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getUserId() {
        return userId;
    }

    public UserComments userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRecipientUserId() {
        return recipientUserId;
    }

    public UserComments recipientUserId(Long recipientUserId) {
        this.recipientUserId = recipientUserId;
        return this;
    }

    public void setRecipientUserId(Long recipientUserId) {
        this.recipientUserId = recipientUserId;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public UserComments messageBody(String messageBody) {
        this.messageBody = messageBody;
        return this;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public Long getRepliedToCommentId() {
        return repliedToCommentId;
    }

    public UserComments repliedToCommentId(Long repliedToCommentId) {
        this.repliedToCommentId = repliedToCommentId;
        return this;
    }

    public void setRepliedToCommentId(Long repliedToCommentId) {
        this.repliedToCommentId = repliedToCommentId;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public UserComments isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean isIsRead() {
        return isRead;
    }

    public UserComments isRead(Boolean isRead) {
        this.isRead = isRead;
        return this;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public UserComments creationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public UserComments createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public UserComments lastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
        return this;
    }

    public void setLastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public UserComments lastUpdatedBy(Long lastUpdatedBy) {
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
        UserComments userComments = (UserComments) o;
        if (userComments.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userComments.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserComments{" +
            "id=" + getId() +
            ", subject='" + getSubject() + "'" +
            ", tenantId='" + getTenantId() + "'" +
            ", userId='" + getUserId() + "'" +
            ", recipientUserId='" + getRecipientUserId() + "'" +
            ", messageBody='" + getMessageBody() + "'" +
            ", repliedToCommentId='" + getRepliedToCommentId() + "'" +
            ", isActive='" + isIsActive() + "'" +
            ", isRead='" + isIsRead() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastUpdatedDate='" + getLastUpdatedDate() + "'" +
            ", lastUpdatedBy='" + getLastUpdatedBy() + "'" +
            "}";
    }
}
