package com.nspl.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A TemplAttributeMapping.
 */
@Entity
@Table(name = "t_templ_attribute_mapping")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TemplAttributeMapping implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "je_temp_id")
    private Long jeTempId;

    @Column(name = "attribute_name")
    private String attributeName;

    @Column(name = "mapping_type")
    private String mappingType;

    @Column(name = "value")
    private String value;

    @Column(name = "source_view_column_id")
    private Long sourceViewColumnId;

    @Column(name = "cretaed_by")
    private Long cretaedBy;

    @Column(name = "last_updated_by")
    private Long lastUpdatedBy;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "last_updated_date")
    private ZonedDateTime lastUpdatedDate;
    
    @Column(name = "view_id")
    private Long viewId;
    
    
    @Column(name = "alias_name")
    private String aliasName;
    
    @Column(name = "rule_level")
    private String ruleLevel;

    
    
    
    public String getAliasName() {
        return aliasName;
    }

    public TemplAttributeMapping aliasName(String aliasName) {
        this.aliasName = aliasName;
        return this;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }
    public String getRuleLevel() {
        return ruleLevel;
    }

    public TemplAttributeMapping ruleLevel(String ruleLevel) {
        this.ruleLevel = ruleLevel;
        return this;
    }

    public void setRuleLevel(String ruleLevel) {
        this.ruleLevel = ruleLevel;
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

    public TemplAttributeMapping jeTempId(Long jeTempId) {
        this.jeTempId = jeTempId;
        return this;
    }

    public void setJeTempId(Long jeTempId) {
        this.jeTempId = jeTempId;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public TemplAttributeMapping attributeName(String attributeName) {
        this.attributeName = attributeName;
        return this;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getMappingType() {
        return mappingType;
    }

    public TemplAttributeMapping mappingType(String mappingType) {
        this.mappingType = mappingType;
        return this;
    }

    public void setMappingType(String mappingType) {
        this.mappingType = mappingType;
    }

    public String getValue() {
        return value;
    }

    public TemplAttributeMapping value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getSourceViewColumnId() {
        return sourceViewColumnId;
    }

    public TemplAttributeMapping sourceViewColumnId(Long sourceViewColumnId) {
        this.sourceViewColumnId = sourceViewColumnId;
        return this;
    }

    public void setSourceViewColumnId(Long sourceViewColumnId) {
        this.sourceViewColumnId = sourceViewColumnId;
    }

    public Long getCretaedBy() {
        return cretaedBy;
    }

    public TemplAttributeMapping cretaedBy(Long cretaedBy) {
        this.cretaedBy = cretaedBy;
        return this;
    }

    public void setCretaedBy(Long cretaedBy) {
        this.cretaedBy = cretaedBy;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public TemplAttributeMapping lastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public TemplAttributeMapping createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public TemplAttributeMapping lastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
        return this;
    }

    public void setLastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }
    public Long getViewId() {
		return viewId;
	}

	public void setViewId(Long viewId) {
		this.viewId = viewId;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TemplAttributeMapping templAttributeMapping = (TemplAttributeMapping) o;
        if (templAttributeMapping.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, templAttributeMapping.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TemplAttributeMapping{" +
            "id=" + id +
            ", jeTempId='" + jeTempId + "'" +
            ", attributeName='" + attributeName + "'" +
            ", mappingType='" + mappingType + "'" +
            ", value='" + value + "'" +
            ", sourceViewColumnId='" + sourceViewColumnId + "'" +
            ", cretaedBy='" + cretaedBy + "'" +
            ", lastUpdatedBy='" + lastUpdatedBy + "'" +
            ", createdDate='" + createdDate + "'" +
            ", lastUpdatedDate='" + lastUpdatedDate + "'" +
            ", viewId='" + viewId + "'" +
            '}';
    }
}
