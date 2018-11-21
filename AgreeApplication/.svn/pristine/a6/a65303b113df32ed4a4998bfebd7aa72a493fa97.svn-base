package com.nspl.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A SourceProfileFileAssignments.
 */
@Entity
@Table(name = "t_source_profile_file_assignments")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "reconapplication", type="sourceprofilefileassignments")
public class SourceProfileFileAssignments implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source_profile_id")
    private Long sourceProfileId;

    @Column(name = "file_name_format")
    private String fileNameFormat;

    @Column(name = "file_description")
    private String fileDescription;

    @Column(name = "file_extension")
    private String fileExtension;

    @Column(name = "frequency_type")
    private String frequencyType;

    @Size(max = 5)
    @Column(name = "due_time", length = 5)
    private String dueTime;

    @Column(name = "day")
    private Integer day;

    @Column(name = "source_directory_path")
    private String sourceDirectoryPath;

    @Column(name = "local_directory_path")
    private String localDirectoryPath;

    @Column(name = "template_id")
    private Long templateId;

    @Column(name = "enabled_flag")
    private Boolean enabledFlag;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "last_updated_by")
    private Long lastUpdatedBy;

    @Column(name = "last_updated_date")
    private ZonedDateTime lastUpdatedDate;
    
    @Column(name="hold")
    private Boolean hold;
    
    @Column(name="hold_reason")
    private String holdReason;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSourceProfileId() {
        return sourceProfileId;
    }

    public SourceProfileFileAssignments sourceProfileId(Long sourceProfileId) {
        this.sourceProfileId = sourceProfileId;
        return this;
    }

    public void setSourceProfileId(Long sourceProfileId) {
        this.sourceProfileId = sourceProfileId;
    }

    public String getFileNameFormat() {
        return fileNameFormat;
    }

    public SourceProfileFileAssignments fileNameFormat(String fileNameFormat) {
        this.fileNameFormat = fileNameFormat;
        return this;
    }

    public void setFileNameFormat(String fileNameFormat) {
        this.fileNameFormat = fileNameFormat;
    }

    public String getFileDescription() {
        return fileDescription;
    }

    public SourceProfileFileAssignments fileDescription(String fileDescription) {
        this.fileDescription = fileDescription;
        return this;
    }

    public void setFileDescription(String fileDescription) {
        this.fileDescription = fileDescription;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public SourceProfileFileAssignments fileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
        return this;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getFrequencyType() {
        return frequencyType;
    }

    public SourceProfileFileAssignments frequencyType(String frequencyType) {
        this.frequencyType = frequencyType;
        return this;
    }

    public void setFrequencyType(String frequencyType) {
        this.frequencyType = frequencyType;
    }

    public String getDueTime() {
        return dueTime;
    }

    public SourceProfileFileAssignments dueTime(String dueTime) {
        this.dueTime = dueTime;
        return this;
    }

    public void setDueTime(String dueTime) {
        this.dueTime = dueTime;
    }

    public Integer getDay() {
        return day;
    }

    public SourceProfileFileAssignments day(Integer day) {
        this.day = day;
        return this;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public String getSourceDirectoryPath() {
        return sourceDirectoryPath;
    }

    public SourceProfileFileAssignments sourceDirectoryPath(String sourceDirectoryPath) {
        this.sourceDirectoryPath = sourceDirectoryPath;
        return this;
    }

    public void setSourceDirectoryPath(String sourceDirectoryPath) {
        this.sourceDirectoryPath = sourceDirectoryPath;
    }

    public String getLocalDirectoryPath() {
        return localDirectoryPath;
    }

    public SourceProfileFileAssignments localDirectoryPath(String localDirectoryPath) {
        this.localDirectoryPath = localDirectoryPath;
        return this;
    }

    public void setLocalDirectoryPath(String localDirectoryPath) {
        this.localDirectoryPath = localDirectoryPath;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public SourceProfileFileAssignments templateId(Long templateId) {
        this.templateId = templateId;
        return this;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public Boolean isEnabledFlag() {
        return enabledFlag;
    }

    public SourceProfileFileAssignments enabledFlag(Boolean enabledFlag) {
        this.enabledFlag = enabledFlag;
        return this;
    }

    public void setEnabledFlag(Boolean enabledFlag) {
        this.enabledFlag = enabledFlag;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public SourceProfileFileAssignments createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public SourceProfileFileAssignments createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public SourceProfileFileAssignments lastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public ZonedDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public SourceProfileFileAssignments lastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
        return this;
    }

    public void setLastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public Boolean getHold() {
		return hold;
	}

	public void setHold(Boolean hold) {
		this.hold = hold;
	}
	
	

	public String getHoldReason() {
		return holdReason;
	}

	public void setHoldReason(String holdReason) {
		this.holdReason = holdReason;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SourceProfileFileAssignments sourceProfileFileAssignments = (SourceProfileFileAssignments) o;
        if (sourceProfileFileAssignments.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sourceProfileFileAssignments.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SourceProfileFileAssignments{" +
            "id=" + getId() +
            ", sourceProfileId='" + getSourceProfileId() + "'" +
            ", fileNameFormat='" + getFileNameFormat() + "'" +
            ", fileDescription='" + getFileDescription() + "'" +
            ", fileExtension='" + getFileExtension() + "'" +
            ", frequencyType='" + getFrequencyType() + "'" +
            ", dueTime='" + getDueTime() + "'" +
            ", day='" + getDay() + "'" +
            ", sourceDirectoryPath='" + getSourceDirectoryPath() + "'" +
            ", localDirectoryPath='" + getLocalDirectoryPath() + "'" +
            ", templateId='" + getTemplateId() + "'" +
            ", enabledFlag='" + isEnabledFlag() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastUpdatedBy='" + getLastUpdatedBy() + "'" +
            ", lastUpdatedDate='" + getLastUpdatedDate() + "'" +
            ", hold='" + getHold() + "'" +
            ", holdReason='" + getHoldReason() + "'" +
            "}";
    }
}
