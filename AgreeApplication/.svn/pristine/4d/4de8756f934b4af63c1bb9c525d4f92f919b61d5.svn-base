package com.nspl.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A FileTemplateSummaryToDetailMapping.
 */
@Entity
@Table(name = "t_file_template_summary_to_detail_mapping")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FileTemplateSummaryToDetailMapping implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "detail_template_id")
    private Long detailTemplateId;

    @Column(name = "summary_template_id")
    private Long summaryTemplateId;

    @Column(name = "detail_row_id")
    private Long detailRowId;

    @Column(name = "summary_row_id")
    private Long summaryRowId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDetailTemplateId() {
        return detailTemplateId;
    }

    public FileTemplateSummaryToDetailMapping detailTemplateId(Long detailTemplateId) {
        this.detailTemplateId = detailTemplateId;
        return this;
    }

    public void setDetailTemplateId(Long detailTemplateId) {
        this.detailTemplateId = detailTemplateId;
    }

    public Long getSummaryTemplateId() {
        return summaryTemplateId;
    }

    public FileTemplateSummaryToDetailMapping summaryTemplateId(Long summaryTemplateId) {
        this.summaryTemplateId = summaryTemplateId;
        return this;
    }

    public void setSummaryTemplateId(Long summaryTemplateId) {
        this.summaryTemplateId = summaryTemplateId;
    }

    public Long getDetailRowId() {
        return detailRowId;
    }

    public FileTemplateSummaryToDetailMapping detailRowId(Long detailRowId) {
        this.detailRowId = detailRowId;
        return this;
    }

    public void setDetailRowId(Long detailRowId) {
        this.detailRowId = detailRowId;
    }

    public Long getSummaryRowId() {
        return summaryRowId;
    }

    public FileTemplateSummaryToDetailMapping summaryRowId(Long summaryRowId) {
        this.summaryRowId = summaryRowId;
        return this;
    }

    public void setSummaryRowId(Long summaryRowId) {
        this.summaryRowId = summaryRowId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FileTemplateSummaryToDetailMapping fileTemplateSummaryToDetailMapping = (FileTemplateSummaryToDetailMapping) o;
        if (fileTemplateSummaryToDetailMapping.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), fileTemplateSummaryToDetailMapping.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FileTemplateSummaryToDetailMapping{" +
            "id=" + getId() +
            ", detailTemplateId='" + getDetailTemplateId() + "'" +
            ", summaryTemplateId='" + getSummaryTemplateId() + "'" +
            ", detailRowId='" + getDetailRowId() + "'" +
            ", summaryRowId='" + getSummaryRowId() + "'" +
            "}";
    }
}
