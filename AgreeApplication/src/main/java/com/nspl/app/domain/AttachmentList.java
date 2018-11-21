package com.nspl.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A AttachmentList.
 */
@Entity
@Table(name = "t_attachment_list")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AttachmentList implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    @Column(name = "tenant_id")
    private Long tenantId;
    
    @Column(name = "attachment_key")
    private String attachmentKey;

    @Column(name = "attachment_category")
    private String attachmentCategory;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "creation_date")
    private ZonedDateTime creationDate;

    @Column(name = "last_updated_by")
    private Long lastUpdatedBy;

    @Column(name = "last_updated_date")
    private ZonedDateTime lastUpdatedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getAttachmentKey() {
        return attachmentKey;
    }

    public AttachmentList attachmentKey(String attachmentKey) {
        this.attachmentKey = attachmentKey;
        return this;
    }

    public void setAttachmentKey(String attachmentKey) {
        this.attachmentKey = attachmentKey;
    }

    public String getAttachmentCategory() {
        return attachmentCategory;
    }

    public AttachmentList attachmentCategory(String attachmentCategory) {
        this.attachmentCategory = attachmentCategory;
        return this;
    }

    public void setAttachmentCategory(String attachmentCategory) {
        this.attachmentCategory = attachmentCategory;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public AttachmentList createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public AttachmentList creationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public AttachmentList lastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public ZonedDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public AttachmentList lastUpdatedDate(ZonedDateTime lastUpdatedDate) {
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
        AttachmentList attachmentList = (AttachmentList) o;
        if (attachmentList.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), attachmentList.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AttachmentList{" +
            "id=" + getId() +
            ", attachmentKey='" + getAttachmentKey() + "'" +
            ", attachmentCategory='" + getAttachmentCategory() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", lastUpdatedBy='" + getLastUpdatedBy() + "'" +
            ", lastUpdatedDate='" + getLastUpdatedDate() + "'" +
            "}";
    }
}
