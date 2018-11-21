package com.nspl.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DataViewConditions.
 */
@Entity
@Table(name = "t_data_view_conditions")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DataViewConditions implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ref_src_type")
    private String refSrcType;

    @Column(name = "ref_src_id")
    private Long refSrcId;

    @Column(name = "ref_src_col_id")
    private Long refSrcColId;

    @Column(name = "filter_operator")
    private String filterOperator;

    @Column(name = "ref_src_type_2")
    private String refSrcType2;

    @Column(name = "ref_src_id_2")
    private Long refSrcId2;

    @Column(name = "ref_src_col_id_2")
    private Long refSrcColId2;

    @Column(name = "logical_operator")
    private String logicalOperator;
    
    @Column(name = "data_view_id")
    private Long dataViewId;

    public Long getDataViewId() {
		return dataViewId;
	}

	public void setDataViewId(Long dataViewId) {
		this.dataViewId = dataViewId;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRefSrcType() {
        return refSrcType;
    }

    public DataViewConditions refSrcType(String refSrcType) {
        this.refSrcType = refSrcType;
        return this;
    }

    public void setRefSrcType(String refSrcType) {
        this.refSrcType = refSrcType;
    }

    public Long getRefSrcId() {
        return refSrcId;
    }

    public DataViewConditions refSrcId(Long refSrcId) {
        this.refSrcId = refSrcId;
        return this;
    }

    public void setRefSrcId(Long refSrcId) {
        this.refSrcId = refSrcId;
    }

    public Long getRefSrcColId() {
        return refSrcColId;
    }

    public DataViewConditions refSrcColId(Long refSrcColId) {
        this.refSrcColId = refSrcColId;
        return this;
    }

    public void setRefSrcColId(Long refSrcColId) {
        this.refSrcColId = refSrcColId;
    }

    public String getFilterOperator() {
        return filterOperator;
    }

    public DataViewConditions filterOperator(String filterOperator) {
        this.filterOperator = filterOperator;
        return this;
    }

    public void setFilterOperator(String filterOperator) {
        this.filterOperator = filterOperator;
    }

    public String getRefSrcType2() {
        return refSrcType2;
    }

    public DataViewConditions refSrcType2(String refSrcType2) {
        this.refSrcType2 = refSrcType2;
        return this;
    }

    public void setRefSrcType2(String refSrcType2) {
        this.refSrcType2 = refSrcType2;
    }

    public Long getRefSrcId2() {
        return refSrcId2;
    }

    public DataViewConditions refSrcId2(Long refSrcId2) {
        this.refSrcId2 = refSrcId2;
        return this;
    }

    public void setRefSrcId2(Long refSrcId2) {
        this.refSrcId2 = refSrcId2;
    }

    public Long getRefSrcColId2() {
        return refSrcColId2;
    }

    public DataViewConditions refSrcColId2(Long refSrcColId2) {
        this.refSrcColId2 = refSrcColId2;
        return this;
    }

    public void setRefSrcColId2(Long refSrcColId2) {
        this.refSrcColId2 = refSrcColId2;
    }

    public String getLogicalOperator() {
        return logicalOperator;
    }

    public DataViewConditions logicalOperator(String logicalOperator) {
        this.logicalOperator = logicalOperator;
        return this;
    }

    public void setLogicalOperator(String logicalOperator) {
        this.logicalOperator = logicalOperator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DataViewConditions dataViewConditions = (DataViewConditions) o;
        if (dataViewConditions.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), dataViewConditions.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DataViewConditions{" +
            "id=" + getId() +
            ", refSrcType='" + getRefSrcType() + "'" +
            ", refSrcId='" + getRefSrcId() + "'" +
            ", refSrcColId='" + getRefSrcColId() + "'" +
            ", filterOperator='" + getFilterOperator() + "'" +
            ", refSrcType2='" + getRefSrcType2() + "'" +
            ", refSrcId2='" + getRefSrcId2() + "'" +
            ", refSrcColId2='" + getRefSrcColId2() + "'" +
            ", logicalOperator='" + getLogicalOperator() + "'" +
            "}";
    }
}
