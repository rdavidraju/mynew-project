package com.nspl.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A MappingSetValues.
 */
@Entity
@Table(name = "t_mapping_set_values")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MappingSetValues implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mapping_set_id")
    private Long mappingSetId;

    @Column(name = "source_value")
    private String sourceValue;

    @Column(name = "target_value")
    private String targetValue;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "last_updated_by")
    private Long lastUpdatedBy;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "last_updated_date")
    private ZonedDateTime lastUpdatedDate;
    
    @Column(name = "status")
    private Boolean status;
    
    @Column(name = "start_date")
    private ZonedDateTime startDate;

    @Column(name = "end_date")
    private ZonedDateTime endDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMappingSetId() {
        return mappingSetId;
    }

    public MappingSetValues mappingSetId(Long mappingSetId) {
        this.mappingSetId = mappingSetId;
        return this;
    }

    public void setMappingSetId(Long mappingSetId) {
        this.mappingSetId = mappingSetId;
    }

    public String getSourceValue() {
        return sourceValue;
    }

    public MappingSetValues sourceValue(String sourceValue) {
        this.sourceValue = sourceValue;
        return this;
    }

    public void setSourceValue(String sourceValue) {
        this.sourceValue = sourceValue;
    }

    public String getTargetValue() {
        return targetValue;
    }

    public MappingSetValues targetValue(String targetValue) {
        this.targetValue = targetValue;
        return this;
    }

    public void setTargetValue(String targetValue) {
        this.targetValue = targetValue;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public MappingSetValues createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public MappingSetValues lastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public MappingSetValues createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public MappingSetValues lastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
        return this;
    }

    public void setLastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

	public ZonedDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(ZonedDateTime startDate) {
		this.startDate = startDate;
	}

	public ZonedDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(ZonedDateTime endDate) {
		this.endDate = endDate;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MappingSetValues mappingSetValues = (MappingSetValues) o;
        if (mappingSetValues.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, mappingSetValues.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "MappingSetValues{" +
            "id=" + id +
            ", mappingSetId='" + mappingSetId + "'" +
            ", sourceValue='" + sourceValue + "'" +
            ", targetValue='" + targetValue + "'" +
            ", createdBy='" + createdBy + "'" +
            ", lastUpdatedBy='" + lastUpdatedBy + "'" +
            ", createdDate='" + createdDate + "'" +
            ", lastUpdatedDate='" + lastUpdatedDate + "'" +
            ", status='" + status + "'" +
            ", startDate='" + startDate + "'" +
            ", endDate='" + endDate + "'" +
            '}';
    }
}
