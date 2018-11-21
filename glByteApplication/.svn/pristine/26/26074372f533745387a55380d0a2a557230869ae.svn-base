package com.nspl.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A BucketDetails.
 */
@Entity
@Table(name = "t_bucket_details")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BucketDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bucket_id")
    private Long bucketId;

    @Column(name = "from_value")
    private Integer fromValue;

    @Column(name = "to_value")
    private Integer toValue;

    @Column(name = "seq_num")
    private Integer seqNum;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

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

    public Long getBucketId() {
        return bucketId;
    }

    public BucketDetails bucketId(Long bucketId) {
        this.bucketId = bucketId;
        return this;
    }

    public void setBucketId(Long bucketId) {
        this.bucketId = bucketId;
    }

    public Integer getFromValue() {
        return fromValue;
    }

    public BucketDetails fromValue(Integer fromValue) {
        this.fromValue = fromValue;
        return this;
    }

    public void setFromValue(Integer fromValue) {
        this.fromValue = fromValue;
    }

    public Integer getToValue() {
        return toValue;
    }

    public BucketDetails toValue(Integer toValue) {
        this.toValue = toValue;
        return this;
    }

    public void setToValue(Integer toValue) {
        this.toValue = toValue;
    }

    public Integer getSeqNum() {
        return seqNum;
    }

    public BucketDetails seqNum(Integer seqNum) {
        this.seqNum = seqNum;
        return this;
    }

    public void setSeqNum(Integer seqNum) {
        this.seqNum = seqNum;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public BucketDetails createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public BucketDetails createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public BucketDetails lastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
        return this;
    }

    public void setLastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public BucketDetails lastUpdatedBy(Long lastUpdatedBy) {
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
        BucketDetails bucketDetails = (BucketDetails) o;
        if (bucketDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), bucketDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BucketDetails{" +
            "id=" + getId() +
            ", bucketId='" + getBucketId() + "'" +
            ", fromValue='" + getFromValue() + "'" +
            ", toValue='" + getToValue() + "'" +
            ", seqNum='" + getSeqNum() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastUpdatedDate='" + getLastUpdatedDate() + "'" +
            ", lastUpdatedBy='" + getLastUpdatedBy() + "'" +
            "}";
    }
}
