package com.nspl.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DataViewFilters.
 */
@Entity
@Table(name = "t_data_view_filters")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DataViewFilters implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_view_id")
    private Long dataViewId;

    @Column(name = "ref_src_type")
    private String refSrcType;

    @Column(name = "ref_src_id")
    private Long refSrcId;

    @Column(name = "ref_src_col_id")
    private Long refSrcColId;

    @Column(name = "filter_operator")
    private String filterOperator;

    @Column(name = "filter_value")
    private String filterValue;

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

    public Long getDataViewId() {
        return dataViewId;
    }

    public DataViewFilters dataViewId(Long dataViewId) {
        this.dataViewId = dataViewId;
        return this;
    }

    public void setDataViewId(Long dataViewId) {
        this.dataViewId = dataViewId;
    }

    public String getRefSrcType() {
        return refSrcType;
    }

    public DataViewFilters refSrcType(String refSrcType) {
        this.refSrcType = refSrcType;
        return this;
    }

    public void setRefSrcType(String refSrcType) {
        this.refSrcType = refSrcType;
    }

    public Long getRefSrcId() {
        return refSrcId;
    }

    public DataViewFilters refSrcId(Long refSrcId) {
        this.refSrcId = refSrcId;
        return this;
    }

    public void setRefSrcId(Long refSrcId) {
        this.refSrcId = refSrcId;
    }

    public Long getRefSrcColId() {
        return refSrcColId;
    }

    public DataViewFilters refSrcColId(Long refSrcColId) {
        this.refSrcColId = refSrcColId;
        return this;
    }

    public void setRefSrcColId(Long refSrcColId) {
        this.refSrcColId = refSrcColId;
    }

    public String getFilterOperator() {
        return filterOperator;
    }

    public DataViewFilters filterOperator(String filterOperator) {
        this.filterOperator = filterOperator;
        return this;
    }

    public void setFilterOperator(String filterOperator) {
        this.filterOperator = filterOperator;
    }

    public String getFilterValue() {
        return filterValue;
    }

    public DataViewFilters filterValue(String filterValue) {
        this.filterValue = filterValue;
        return this;
    }

    public void setFilterValue(String filterValue) {
        this.filterValue = filterValue;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public DataViewFilters createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public DataViewFilters creationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public DataViewFilters lastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public ZonedDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public DataViewFilters lastUpdatedDate(ZonedDateTime lastUpdatedDate) {
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
        DataViewFilters dataViewFilters = (DataViewFilters) o;
        if (dataViewFilters.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), dataViewFilters.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DataViewFilters{" +
            "id=" + getId() +
            ", dataViewId='" + getDataViewId() + "'" +
            ", refSrcType='" + getRefSrcType() + "'" +
            ", refSrcId='" + getRefSrcId() + "'" +
            ", refSrcColId='" + getRefSrcColId() + "'" +
            ", filterOperator='" + getFilterOperator() + "'" +
            ", filterValue='" + getFilterValue() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", lastUpdatedBy='" + getLastUpdatedBy() + "'" +
            ", lastUpdatedDate='" + getLastUpdatedDate() + "'" +
            "}";
    }
}
