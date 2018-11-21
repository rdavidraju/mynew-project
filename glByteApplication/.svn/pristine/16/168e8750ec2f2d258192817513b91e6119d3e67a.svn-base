package com.nspl.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DataViewsColumns.
 */
@Entity
@Table(name = "t_data_views_columns")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "reconapplication", type="dataviewscolumns")
public class DataViewsColumns implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_view_id")
    private Long dataViewId;

    @Column(name = "ref_dv_type")
    private String refDvType;

    @Column(name = "ref_dv_name")
    private String refDvName;

    @Column(name = "ref_dv_column")
    private String refDvColumn;

    @Column(name = "column_name")
    private String columnName;

    @Column(name = "col_data_type")
    private String colDataType;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "last_updated_by")
    private Long lastUpdatedBy;

    @Column(name = "creation_date")
    private ZonedDateTime creationDate;

    @Column(name = "last_updated_date")
    private ZonedDateTime lastUpdatedDate;
    
    @Column(name = "formula")
    private String formula;
    
    @Column(name = "qualifier")
    private String qualifier;
    
    @Column(name = "formula_alias")
    private String formulaAlias;
    
    @Column(name = "group_by")
    private Boolean groupBy;
    
    @Column(name = "intermediate_id")
    private Long intermediateId;

    public Long getIntermediateId() {
		return intermediateId;
	}

	public void setIntermediateId(Long intermediateId) {
		this.intermediateId = intermediateId;
	}

	public Boolean getGroupBy() {
		return groupBy;
	}

	public void setGroupBy(Boolean groupBy) {
		this.groupBy = groupBy;
	}

	public String getQualifier() {
		return qualifier;
	}

	public void setQualifier(String qualifier) {
		this.qualifier = qualifier;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDataViewId() {
        return dataViewId;
    }

    public DataViewsColumns dataViewId(Long dataViewId) {
        this.dataViewId = dataViewId;
        return this;
    }

    public void setDataViewId(Long dataViewId) {
        this.dataViewId = dataViewId;
    }

    public String getRefDvType() {
        return refDvType;
    }

    public DataViewsColumns refDvType(String refDvType) {
        this.refDvType = refDvType;
        return this;
    }

    public void setRefDvType(String refDvType) {
        this.refDvType = refDvType;
    }

    public String getRefDvName() {
        return refDvName;
    }

    public DataViewsColumns refDvName(String refDvName) {
        this.refDvName = refDvName;
        return this;
    }

    public void setRefDvName(String refDvName) {
        this.refDvName = refDvName;
    }

    public String getRefDvColumn() {
        return refDvColumn;
    }

    public DataViewsColumns refDvColumn(String refDvColumn) {
        this.refDvColumn = refDvColumn;
        return this;
    }

    public void setRefDvColumn(String refDvColumn) {
        this.refDvColumn = refDvColumn;
    }

    public String getColumnName() {
        return columnName;
    }

    public DataViewsColumns columnName(String columnName) {
        this.columnName = columnName;
        return this;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColDataType() {
        return colDataType;
    }

    public DataViewsColumns colDataType(String colDataType) {
        this.colDataType = colDataType;
        return this;
    }

    public void setColDataType(String colDataType) {
        this.colDataType = colDataType;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public DataViewsColumns createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public DataViewsColumns lastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public DataViewsColumns creationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public ZonedDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public DataViewsColumns lastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
        return this;
    }

    public void setLastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }
    
    
    public String getFormulaAlias() {
        return formulaAlias;
    }

    public DataViewsColumns formulaAlias(String formulaAlias) {
        this.formulaAlias = formulaAlias;
        return this;
    }

    public void setFormulaAlias(String formulaAlias) {
        this.formulaAlias = formulaAlias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DataViewsColumns dataViewsColumns = (DataViewsColumns) o;
        if (dataViewsColumns.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, dataViewsColumns.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
	public String toString() {
		return "DataViewsColumns [id=" + id + ", dataViewId=" + dataViewId
				+ ", refDvType=" + refDvType + ", refDvName=" + refDvName
				+ ", refDvColumn=" + refDvColumn + ", columnName=" + columnName
				+ ", colDataType=" + colDataType + ", createdBy=" + createdBy
				+ ", lastUpdatedBy=" + lastUpdatedBy + ", creationDate="
				+ creationDate + ", lastUpdatedDate=" + lastUpdatedDate
				+ ", formula=" + formula + ", qualifier=" + qualifier
				+ ", formulaAlias=" + formulaAlias + ", groupBy=" + groupBy
				+ ", intermediateId=" + intermediateId + "]";
	}
}
