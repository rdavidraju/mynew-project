package com.nspl.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DataChild.
 */
@Entity
@Table(name = "t_data_child")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "datachild")
public class DataChild implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "data_view_id")
    private Long dataViewId;

    @Column(name = "master_row_id")
    private Long masterRowId;

    @Column(name = "row_description")
    private String rowDescription;

    @Column(name = "adjustment_type")
    private String adjustmentType;

    @Column(name = "adjustment_method")
    private String adjustmentMethod;

    @Column(name = "percent_value")
    private Double percentValue;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "last_updated_by")
    private Long lastUpdatedBy;

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

    public Long getTenantId() {
        return tenantId;
    }

    public DataChild tenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getDataViewId() {
        return dataViewId;
    }

    public DataChild dataViewId(Long dataViewId) {
        this.dataViewId = dataViewId;
        return this;
    }

    public void setDataViewId(Long dataViewId) {
        this.dataViewId = dataViewId;
    }

    public Long getMasterRowId() {
        return masterRowId;
    }

    public DataChild masterRowId(Long masterRowId) {
        this.masterRowId = masterRowId;
        return this;
    }

    public void setMasterRowId(Long masterRowId) {
        this.masterRowId = masterRowId;
    }

    public String getRowDescription() {
        return rowDescription;
    }

    public DataChild rowDescription(String rowDescription) {
        this.rowDescription = rowDescription;
        return this;
    }

    public void setRowDescription(String rowDescription) {
        this.rowDescription = rowDescription;
    }

    public String getAdjustmentType() {
        return adjustmentType;
    }

    public DataChild adjustmentType(String adjustmentType) {
        this.adjustmentType = adjustmentType;
        return this;
    }

    public void setAdjustmentType(String adjustmentType) {
        this.adjustmentType = adjustmentType;
    }

    public String getAdjustmentMethod() {
        return adjustmentMethod;
    }

    public DataChild adjustmentMethod(String adjustmentMethod) {
        this.adjustmentMethod = adjustmentMethod;
        return this;
    }

    public void setAdjustmentMethod(String adjustmentMethod) {
        this.adjustmentMethod = adjustmentMethod;
    }

    public Double getPercentValue() {
        return percentValue;
    }

    public DataChild percentValue(Double percentValue) {
        this.percentValue = percentValue;
        return this;
    }

    public void setPercentValue(Double percentValue) {
        this.percentValue = percentValue;
    }

    public Double getAmount() {
        return amount;
    }

    public DataChild amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public DataChild createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public DataChild lastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public DataChild creationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public ZonedDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public DataChild lastUpdatedDate(ZonedDateTime lastUpdatedDate) {
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
        DataChild dataChild = (DataChild) o;
        if (dataChild.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, dataChild.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DataChild{" +
            "id=" + id +
            ", tenantId='" + tenantId + "'" +
            ", dataViewId='" + dataViewId + "'" +
            ", masterRowId='" + masterRowId + "'" +
            ", rowDescription='" + rowDescription + "'" +
            ", adjustmentType='" + adjustmentType + "'" +
            ", adjustmentMethod='" + adjustmentMethod + "'" +
            ", percentValue='" + percentValue + "'" +
            ", amount='" + amount + "'" +
            ", createdBy='" + createdBy + "'" +
            ", lastUpdatedBy='" + lastUpdatedBy + "'" +
            ", creationDate='" + creationDate + "'" +
            ", lastUpdatedDate='" + lastUpdatedDate + "'" +
            '}';
    }
}
