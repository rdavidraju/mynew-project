package com.nspl.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A SummaryFileTemplateLines.
 */
@Entity
@Table(name = "t_summary_file_template_lines")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SummaryFileTemplateLines implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "template_line_id")
    private Integer templateLineId;

    @Column(name = "file_template_id")
    private Integer fileTemplateId;

    @Column(name = "grouping")
    private Boolean grouping;

    @Column(name = "aggregate")
    private Boolean aggregate;

    @Column(name = "aggregate_method")
    private String aggregateMethod;
    
    @Column(name = "summary_template_id")
    private Long summaryTemplateId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTemplateLineId() {
        return templateLineId;
    }

    public SummaryFileTemplateLines templateLineId(Integer templateLineId) {
        this.templateLineId = templateLineId;
        return this;
    }

    public void setTemplateLineId(Integer templateLineId) {
        this.templateLineId = templateLineId;
    }
    
    

	public Integer getFileTemplateId() {
		return fileTemplateId;
	}

	public void setFileTemplateId(Integer fileTemplateId) {
		this.fileTemplateId = fileTemplateId;
	}

	public Boolean getGrouping() {
		return grouping;
	}

	public Boolean getAggregate() {
		return aggregate;
	}

	public Boolean isGrouping() {
        return grouping;
    }

    public SummaryFileTemplateLines grouping(Boolean grouping) {
        this.grouping = grouping;
        return this;
    }

    public void setGrouping(Boolean grouping) {
        this.grouping = grouping;
    }

    public Boolean isAggregate() {
        return aggregate;
    }

    public SummaryFileTemplateLines aggregate(Boolean aggregate) {
        this.aggregate = aggregate;
        return this;
    }

    public void setAggregate(Boolean aggregate) {
        this.aggregate = aggregate;
    }

    public String getAggregateMethod() {
        return aggregateMethod;
    }

    public SummaryFileTemplateLines aggregateMethod(String aggregateMethod) {
        this.aggregateMethod = aggregateMethod;
        return this;
    }

    public void setAggregateMethod(String aggregateMethod) {
        this.aggregateMethod = aggregateMethod;
    }

    public Long getSummaryTemplateId() {
		return summaryTemplateId;
	}

	public void setSummaryTemplateId(Long summaryTemplateId) {
		this.summaryTemplateId = summaryTemplateId;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SummaryFileTemplateLines summaryFileTemplateLines = (SummaryFileTemplateLines) o;
        if (summaryFileTemplateLines.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), summaryFileTemplateLines.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SummaryFileTemplateLines{" +
            "id=" + getId() +
            ", templateLineId='" + getTemplateLineId() + "'" +
            ", fileTemplateId='" + getFileTemplateId() + "'" +
            ", grouping='" + isGrouping() + "'" +
            ", aggregate='" + isAggregate() + "'" +
            ", aggregateMethod='" + getAggregateMethod() + "'" +
            ", summaryTemplateId='" + getSummaryTemplateId() + "'" +
            "}";
    }
}
