package com.nspl.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A JeLdrDetails.
 */
@Entity
@Table(name = "t_je_ldr_details")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class JeLdrDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "je_temp_id")
    private Long jeTempId;

    @Column(name = "col_type")
    private String colType;

    @Column(name = "col_value")
    private Long colValue;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "last_updated_by")
    private Long lastUpdatedBy;

    @Column(name = "last_updated_date")
    private ZonedDateTime lastUpdatedDate;
    
    @Column(name = "ref_Column")
    private Integer refColumn;
    
    
    
    public Integer getRefColumn() {
        return refColumn;
    }

    public JeLdrDetails refColumn(Integer refColumn) {
        this.refColumn = refColumn;
        return this;
    }

    public void setRefColumn(Integer refColumn) {
        this.refColumn = refColumn;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getJeTempId() {
        return jeTempId;
    }

    public JeLdrDetails jeTempId(Long jeTempId) {
        this.jeTempId = jeTempId;
        return this;
    }

    public void setJeTempId(Long jeTempId) {
        this.jeTempId = jeTempId;
    }

    public String getColType() {
        return colType;
    }

    public JeLdrDetails colType(String colType) {
        this.colType = colType;
        return this;
    }

    public void setColType(String colType) {
        this.colType = colType;
    }

    public Long getColValue() {
        return colValue;
    }

    public JeLdrDetails colValue(Long colValue) {
        this.colValue = colValue;
        return this;
    }

    public void setColValue(Long colValue) {
        this.colValue = colValue;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public JeLdrDetails createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public JeLdrDetails createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public JeLdrDetails lastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public ZonedDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public JeLdrDetails lastUpdatedDate(ZonedDateTime lastUpdatedDate) {
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
        JeLdrDetails jeLdrDetails = (JeLdrDetails) o;
        if (jeLdrDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), jeLdrDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "JeLdrDetails{" +
            "id=" + getId() +
            ", jeTempId='" + getJeTempId() + "'" +
            ", colType='" + getColType() + "'" +
            ", colValue='" + getColValue() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastUpdatedBy='" + getLastUpdatedBy() + "'" +
            ", lastUpdatedDate='" + getLastUpdatedDate() + "'" +
            "}";
    }
}
