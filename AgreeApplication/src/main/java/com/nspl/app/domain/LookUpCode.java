package com.nspl.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A LookUpCode.
 */
@Entity
@Table(name = "look_up_code")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "lookupcode")
public class LookUpCode implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "look_up_code")
    private String lookUpCode;

    @Column(name = "look_up_type")
    private String lookUpType;

    @Column(name = "meaning")
    private String meaning;

    @Column(name = "description")
    private String description;

    @Column(name = "enable_flag")
    private Boolean enableFlag;

    @Column(name = "active_start_date")
    private ZonedDateTime activeStartDate;

    @Column(name = "active_end_date")
    private ZonedDateTime activeEndDate;

    @Column(name = "secure_attribute_1")
    private String secureAttribute1;

    @Column(name = "secure_attribute_2")
    private String secureAttribute2;

    @Column(name = "secure_attribute_3")
    private String secureAttribute3;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "creation_date")
    private ZonedDateTime creationDate;

    @Column(name = "last_updated_by")
    private Long lastUpdatedBy;

    @Column(name = "last_updated_date")
    private ZonedDateTime lastUpdatedDate;
    
    @Column(name = "module")
    private String module;
    
    @Column(name = "is_default")
    private Boolean isDefault;

    public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public LookUpCode tenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getLookUpCode() {
        return lookUpCode;
    }

    public LookUpCode lookUpCode(String lookUpCode) {
        this.lookUpCode = lookUpCode;
        return this;
    }

    public void setLookUpCode(String lookUpCode) {
        this.lookUpCode = lookUpCode;
    }

    public String getLookUpType() {
        return lookUpType;
    }

    public LookUpCode lookUpType(String lookUpType) {
        this.lookUpType = lookUpType;
        return this;
    }

    public void setLookUpType(String lookUpType) {
        this.lookUpType = lookUpType;
    }

    public String getMeaning() {
        return meaning;
    }

    public LookUpCode meaning(String meaning) {
        this.meaning = meaning;
        return this;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getDescription() {
        return description;
    }

    public LookUpCode description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isEnableFlag() {
        return enableFlag;
    }

    public LookUpCode enableFlag(Boolean enableFlag) {
        this.enableFlag = enableFlag;
        return this;
    }

    public void setEnableFlag(Boolean enableFlag) {
        this.enableFlag = enableFlag;
    }

    public ZonedDateTime getActiveStartDate() {
        return activeStartDate;
    }

    public LookUpCode activeStartDate(ZonedDateTime activeStartDate) {
        this.activeStartDate = activeStartDate;
        return this;
    }

    public void setActiveStartDate(ZonedDateTime activeStartDate) {
        this.activeStartDate = activeStartDate;
    }

    public ZonedDateTime getActiveEndDate() {
        return activeEndDate;
    }

    public LookUpCode activeEndDate(ZonedDateTime activeEndDate) {
        this.activeEndDate = activeEndDate;
        return this;
    }

    public void setActiveEndDate(ZonedDateTime activeEndDate) {
        this.activeEndDate = activeEndDate;
    }

    public String getSecureAttribute1() {
        return secureAttribute1;
    }

    public LookUpCode secureAttribute1(String secureAttribute1) {
        this.secureAttribute1 = secureAttribute1;
        return this;
    }

    public void setSecureAttribute1(String secureAttribute1) {
        this.secureAttribute1 = secureAttribute1;
    }

    public String getSecureAttribute2() {
        return secureAttribute2;
    }

    public LookUpCode secureAttribute2(String secureAttribute2) {
        this.secureAttribute2 = secureAttribute2;
        return this;
    }

    public void setSecureAttribute2(String secureAttribute2) {
        this.secureAttribute2 = secureAttribute2;
    }

    public String getSecureAttribute3() {
        return secureAttribute3;
    }

    public LookUpCode secureAttribute3(String secureAttribute3) {
        this.secureAttribute3 = secureAttribute3;
        return this;
    }

    public void setSecureAttribute3(String secureAttribute3) {
        this.secureAttribute3 = secureAttribute3;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public LookUpCode createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public LookUpCode creationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public LookUpCode lastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public ZonedDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public LookUpCode lastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
        return this;
    }
    
    public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
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
        LookUpCode lookUpCode = (LookUpCode) o;
        if (lookUpCode.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, lookUpCode.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
	public String toString() {
		return "LookUpCode [id=" + id + ", tenantId=" + tenantId
				+ ", lookUpCode=" + lookUpCode + ", lookUpType=" + lookUpType
				+ ", meaning=" + meaning + ", description=" + description
				+ ", enableFlag=" + enableFlag + ", activeStartDate="
				+ activeStartDate + ", activeEndDate=" + activeEndDate
				+ ", secureAttribute1=" + secureAttribute1
				+ ", secureAttribute2=" + secureAttribute2
				+ ", secureAttribute3=" + secureAttribute3 + ", createdBy="
				+ createdBy + ", creationDate=" + creationDate
				+ ", lastUpdatedBy=" + lastUpdatedBy + ", lastUpdatedDate="
				+ lastUpdatedDate + ", module=" + module + ", isDefault=" + isDefault + "]";
	}
}
