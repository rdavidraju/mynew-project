package com.nspl.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A RowConditions.
 */
@Entity
@Table(name = "t_row_conditions")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RowConditions implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "template_line_id")
    private Long templateLineId;

    @Column(name = "operator")
    private String operator;

    @Column(name = "jhi_value")
    private String value;

    @Column(name = "jhi_type")
    private String type;
    
    @Column(name="logical_operator")
    private String logicalOperator;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTemplateLineId() {
        return templateLineId;
    }

    public RowConditions templateLineId(Long templateLineId) {
        this.templateLineId = templateLineId;
        return this;
    }

    public void setTemplateLineId(Long templateLineId) {
        this.templateLineId = templateLineId;
    }

    public String getOperator() {
        return operator;
    }

    public RowConditions operator(String operator) {
        this.operator = operator;
        return this;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public RowConditions value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public RowConditions type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }
    

    public String getLogicalOperator() {
		return logicalOperator;
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
        RowConditions rowConditions = (RowConditions) o;
        if (rowConditions.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), rowConditions.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RowConditions{" +
            "id=" + getId() +
            ", templateLineId='" + getTemplateLineId() + "'" +
            ", operator='" + getOperator() + "'" +
            ", value='" + getValue() + "'" +
            ", type='" + getType() + "'" +
            ", logicalOperator='" + getLogicalOperator() + "'" +
            "}";
    }
}
