package com.nspl.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A IntermediateTable.
 */
@Entity
@Table(name = "t_intermediate_table")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class IntermediateTable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "template_id")
    private Long templateId;

    @Column(name = "row_identifier")
    private String rowIdentifier;

    @Column(name = "row_identifier_criteria")
    private String rowIdentifierCriteria;

    @Column(name = "header_info")
    private String headerInfo;

    @Column(name = "row_info")
    private String rowInfo;

    @Column(name = "position_start")
    private Integer positionStart;

    @Column(name = "position_end")
    private Integer positionEnd;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "last_updated_by")
    private Long lastUpdatedBy;

    @Column(name = "creation_date")
    private ZonedDateTime creationDate;

    @Column(name = "last_updated_date")
    private ZonedDateTime lastUpdatedDate;
    
    @Lob
    @Column(name = "data")
    private String data;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public IntermediateTable templateId(Long templateId) {
        this.templateId = templateId;
        return this;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public String getRowIdentifier() {
        return rowIdentifier;
    }

    public IntermediateTable rowIdentifier(String rowIdentifier) {
        this.rowIdentifier = rowIdentifier;
        return this;
    }

    public void setRowIdentifier(String rowIdentifier) {
        this.rowIdentifier = rowIdentifier;
    }

    public String getRowIdentifierCriteria() {
        return rowIdentifierCriteria;
    }

    public IntermediateTable rowIdentifierCriteria(String rowIdentifierCriteria) {
        this.rowIdentifierCriteria = rowIdentifierCriteria;
        return this;
    }

    public void setRowIdentifierCriteria(String rowIdentifierCriteria) {
        this.rowIdentifierCriteria = rowIdentifierCriteria;
    }

    public String getHeaderInfo() {
        return headerInfo;
    }

    public IntermediateTable headerInfo(String headerInfo) {
        this.headerInfo = headerInfo;
        return this;
    }

    public void setHeaderInfo(String headerInfo) {
        this.headerInfo = headerInfo;
    }

    public String getRowInfo() {
        return rowInfo;
    }

    public IntermediateTable rowInfo(String rowInfo) {
        this.rowInfo = rowInfo;
        return this;
    }

    public void setRowInfo(String rowInfo) {
        this.rowInfo = rowInfo;
    }

    public Integer getPositionStart() {
        return positionStart;
    }

    public IntermediateTable positionStart(Integer positionStart) {
        this.positionStart = positionStart;
        return this;
    }

    public void setPositionStart(Integer positionStart) {
        this.positionStart = positionStart;
    }

    public Integer getPositionEnd() {
        return positionEnd;
    }

    public IntermediateTable positionEnd(Integer positionEnd) {
        this.positionEnd = positionEnd;
        return this;
    }

    public void setPositionEnd(Integer positionEnd) {
        this.positionEnd = positionEnd;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public IntermediateTable tenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public IntermediateTable createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public IntermediateTable lastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public IntermediateTable creationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public ZonedDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public IntermediateTable lastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
        return this;
    }

    public void setLastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }
    
    

    public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IntermediateTable intermediateTable = (IntermediateTable) o;
        if (intermediateTable.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), intermediateTable.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "IntermediateTable{" +
            "id=" + getId() +
            ", templateId='" + getTemplateId() + "'" +
            ", rowIdentifier='" + getRowIdentifier() + "'" +
            ", rowIdentifierCriteria='" + getRowIdentifierCriteria() + "'" +
            ", headerInfo='" + getHeaderInfo() + "'" +
            ", rowInfo='" + getRowInfo() + "'" +
            ", positionStart='" + getPositionStart() + "'" +
            ", positionEnd='" + getPositionEnd() + "'" +
            ", tenantId='" + getTenantId() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastUpdatedBy='" + getLastUpdatedBy() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", lastUpdatedDate='" + getLastUpdatedDate() + "'" +
            "}";
    }
}
