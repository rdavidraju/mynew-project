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
 * A TemplateDetails.
 */
@Entity
@Table(name = "t_template_details")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "reconapplication", type="templatedetails")
public class TemplateDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "id_for_display")
    private String idForDisplay;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "target_app_source")
    private String targetAppSource;

    @Column(name = "template_name")
    private String templateName;

    @Column(name = "description")
    private String description;

    @Column(name = "view_id")
    private Long viewId;

    @Column(name = "view_name")
    private String viewName;

    @Column(name = "start_date")
    private ZonedDateTime startDate;

    @Column(name = "end_date")
    private ZonedDateTime endDate;

    @Column(name = "enabled_flag")
    private Boolean enabledFlag;


    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "last_updated_by")
    private Long lastUpdatedBy;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "last_updated_date")
    private ZonedDateTime lastUpdatedDate;
    
    @Column(name = "coa_id")
    private Long coaId;
    
    @Column(name = "rule_grp_id")
    private Long ruleGrpId;
    
    @Column(name = "appr_rule_grp_id")
    private Long apprRuleGrpId;
    
    
    
    
    

    public Long getCoaId() {
		return coaId;
	}

	public void setCoaId(Long coaId) {
		this.coaId = coaId;
	}

	public Long getRuleGrpId() {
		return ruleGrpId;
	}

	public void setRuleGrpId(Long ruleGrpId) {
		this.ruleGrpId = ruleGrpId;
	}

	public Boolean getEnabledFlag() {
		return enabledFlag;
	}

	public Long getId() {
        return id;
    }
	
	 public String getIdForDisplay() {
			return idForDisplay;
		}

		public void setIdForDisplay(String idForDisplay) {
			this.idForDisplay = idForDisplay;
		}


    public void setId(Long id) {
        this.id = id;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public TemplateDetails tenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getTargetAppSource() {
        return targetAppSource;
    }

    public TemplateDetails targetAppSource(String targetAppSource) {
        this.targetAppSource = targetAppSource;
        return this;
    }

    public void setTargetAppSource(String targetAppSource) {
        this.targetAppSource = targetAppSource;
    }

    public String getTemplateName() {
        return templateName;
    }

    public TemplateDetails templateName(String templateName) {
        this.templateName = templateName;
        return this;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getDescription() {
        return description;
    }

    public TemplateDetails description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getViewId() {
        return viewId;
    }

    public TemplateDetails viewId(Long viewId) {
        this.viewId = viewId;
        return this;
    }

    public void setViewId(Long viewId) {
        this.viewId = viewId;
    }

    public String getViewName() {
        return viewName;
    }

    public TemplateDetails viewName(String viewName) {
        this.viewName = viewName;
        return this;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public TemplateDetails startDate(ZonedDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public TemplateDetails endDate(ZonedDateTime endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public Boolean isEnabledFlag() {
        return enabledFlag;
    }

    public TemplateDetails enabledFlag(Boolean enabledFlag) {
        this.enabledFlag = enabledFlag;
        return this;
    }

    public void setEnabledFlag(Boolean enabledFlag) {
        this.enabledFlag = enabledFlag;
    }
    public Long getCreatedBy() {
        return createdBy;
    }

    public TemplateDetails createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public TemplateDetails lastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public TemplateDetails createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public TemplateDetails lastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
        return this;
    }

    public void setLastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }
    
    
    public Long getApprRuleGrpId() {
        return apprRuleGrpId;
    }

    public TemplateDetails apprRuleGrpId(Long apprRuleGrpId) {
        this.apprRuleGrpId = apprRuleGrpId;
        return this;
    }

    public void setApprRuleGrpId(Long apprRuleGrpId) {
        this.apprRuleGrpId = apprRuleGrpId;
    }
    

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TemplateDetails templateDetails = (TemplateDetails) o;
        if (templateDetails.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, templateDetails.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TemplateDetails{" +
            "id=" + id +
            ", tenantId='" + tenantId + "'" +
            ", targetAppSource='" + targetAppSource + "'" +
            ", templateName='" + templateName + "'" +
            ", description='" + description + "'" +
            ", viewId='" + viewId + "'" +
            ", viewName='" + viewName + "'" +
            ", startDate='" + startDate + "'" +
            ", endDate='" + endDate + "'" +
            ", enabledFlag='" + enabledFlag + "'" +
            ", createdBy='" + createdBy + "'" +
            ", lastUpdatedBy='" + lastUpdatedBy + "'" +
            ", createdDate='" + createdDate + "'" +
            ", lastUpdatedDate='" + lastUpdatedDate + "'" +
            '}';
    }
}
