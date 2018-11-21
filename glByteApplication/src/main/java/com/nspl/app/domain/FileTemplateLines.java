
package com.nspl.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A FileTemplateLines.
 */
@Entity
@Table(name = "t_file_template_lines")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "reconapplication", type="filetemplatelines")
public class FileTemplateLines implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "template_id")
    private Long templateId;

    @Column(name = "line_number")
    private Integer lineNumber;

    @Column(name = "master_table_reference_column")
    private String masterTableReferenceColumn;

    @Column(name = "record_t_ype")
    private String recordTYpe;

    @Column(name = "record_identifier")
    private String recordIdentifier;

    @Column(name = "column_number")
    private Integer columnNumber;

    @Column(name = "enclosed_char")
    private String enclosedChar;

    @Column(name = "position_begin")
    private Integer positionBegin;

    @Column(name = "position_end")
    private Integer positionEnd;

    @Column(name = "column_header")
    private String columnHeader;

    @Column(name = "constant_value")
    private String constantValue;

    @Column(name = "zero_fill")
    private String zeroFill;

    @Column(name = "align")
    private String align;

    @Column(name = "formula")
    private String formula;

    @Column(name = "date_format")
    private String dateFormat;

    @Column(name = "time_format")
    private String timeFormat;

    @Column(name = "amount_format")
    private String amountFormat;

    @Column(name = "over_flow")
    private String overFlow;

    @Column(name = "skip_column")
    private String skipColumn;

    @Column(name = "column_delimiter")
    private String columnDelimiter;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "last_updated_by")
    private Long lastUpdatedBy;

    @Column(name = "last_updated_date")
    private ZonedDateTime lastUpdatedDate;
    
    @Column(name = "column_alias")
    private String columnAlias;
    
    
    @Column(name = "intermediate_id")
    private Long intermediateId;
    
    @Column(name = "record_start_row")
    private String recordStartRow;
    
	public String getRecordStartRow() {
		return recordStartRow;
	}

	public void setRecordStartRow(String recordStartRow) {
		this.recordStartRow = recordStartRow;
	}
	
	public FileTemplateLines recordStartRow(String recordStartRow) {
        this.recordStartRow = recordStartRow;
        return this;
    }

	public String getColumnAlias() {
		return columnAlias;
	}

	public void setColumnAlias(String columnAlias) {
		this.columnAlias = columnAlias;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public FileTemplateLines templateId(Long templateId) {
        this.templateId = templateId;
        return this;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public FileTemplateLines lineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
        return this;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getMasterTableReferenceColumn() {
        return masterTableReferenceColumn;
    }

    public FileTemplateLines masterTableReferenceColumn(String masterTableReferenceColumn) {
        this.masterTableReferenceColumn = masterTableReferenceColumn;
        return this;
    }

    public void setMasterTableReferenceColumn(String masterTableReferenceColumn) {
        this.masterTableReferenceColumn = masterTableReferenceColumn;
    }

    public String getRecordTYpe() {
        return recordTYpe;
    }

    public FileTemplateLines recordTYpe(String recordTYpe) {
        this.recordTYpe = recordTYpe;
        return this;
    }

    public void setRecordTYpe(String recordTYpe) {
        this.recordTYpe = recordTYpe;
    }

    public String getRecordIdentifier() {
        return recordIdentifier;
    }

    public FileTemplateLines recordIdentifier(String recordIdentifier) {
        this.recordIdentifier = recordIdentifier;
        return this;
    }

    public void setRecordIdentifier(String recordIdentifier) {
        this.recordIdentifier = recordIdentifier;
    }

    public Integer getColumnNumber() {
        return columnNumber;
    }

    public FileTemplateLines columnNumber(Integer columnNumber) {
        this.columnNumber = columnNumber;
        return this;
    }

    public void setColumnNumber(Integer columnNumber) {
        this.columnNumber = columnNumber;
    }

    public String getEnclosedChar() {
        return enclosedChar;
    }

    public FileTemplateLines enclosedChar(String enclosedChar) {
        this.enclosedChar = enclosedChar;
        return this;
    }

    public void setEnclosedChar(String enclosedChar) {
        this.enclosedChar = enclosedChar;
    }

    public Integer getPositionBegin() {
        return positionBegin;
    }

    public FileTemplateLines positionBegin(Integer positionBegin) {
        this.positionBegin = positionBegin;
        return this;
    }

    public void setPositionBegin(Integer positionBegin) {
        this.positionBegin = positionBegin;
    }

    public Integer getPositionEnd() {
        return positionEnd;
    }

    public FileTemplateLines positionEnd(Integer positionEnd) {
        this.positionEnd = positionEnd;
        return this;
    }

    public void setPositionEnd(Integer positionEnd) {
        this.positionEnd = positionEnd;
    }

    public String getColumnHeader() {
        return columnHeader;
    }

    public FileTemplateLines columnHeader(String columnHeader) {
        this.columnHeader = columnHeader;
        return this;
    }

    public void setColumnHeader(String columnHeader) {
        this.columnHeader = columnHeader;
    }

    public String getConstantValue() {
        return constantValue;
    }

    public FileTemplateLines constantValue(String constantValue) {
        this.constantValue = constantValue;
        return this;
    }

    public void setConstantValue(String constantValue) {
        this.constantValue = constantValue;
    }

    public String getZeroFill() {
        return zeroFill;
    }

    public FileTemplateLines zeroFill(String zeroFill) {
        this.zeroFill = zeroFill;
        return this;
    }

    public void setZeroFill(String zeroFill) {
        this.zeroFill = zeroFill;
    }

    public String getAlign() {
        return align;
    }

    public FileTemplateLines align(String align) {
        this.align = align;
        return this;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public String getFormula() {
        return formula;
    }

    public FileTemplateLines formula(String formula) {
        this.formula = formula;
        return this;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public FileTemplateLines dateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
        return this;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getTimeFormat() {
        return timeFormat;
    }

    public FileTemplateLines timeFormat(String timeFormat) {
        this.timeFormat = timeFormat;
        return this;
    }

    public void setTimeFormat(String timeFormat) {
        this.timeFormat = timeFormat;
    }

    public String getAmountFormat() {
        return amountFormat;
    }

    public FileTemplateLines amountFormat(String amountFormat) {
        this.amountFormat = amountFormat;
        return this;
    }

    public void setAmountFormat(String amountFormat) {
        this.amountFormat = amountFormat;
    }

    public String getOverFlow() {
        return overFlow;
    }

    public FileTemplateLines overFlow(String overFlow) {
        this.overFlow = overFlow;
        return this;
    }

    public void setOverFlow(String overFlow) {
        this.overFlow = overFlow;
    }

    public String getSkipColumn() {
        return skipColumn;
    }

    public FileTemplateLines skipColumn(String skipColumn) {
        this.skipColumn = skipColumn;
        return this;
    }

    public void setSkipColumn(String skipColumn) {
        this.skipColumn = skipColumn;
    }

    public String getColumnDelimiter() {
        return columnDelimiter;
    }

    public FileTemplateLines columnDelimiter(String columnDelimiter) {
        this.columnDelimiter = columnDelimiter;
        return this;
    }

    public void setColumnDelimiter(String columnDelimiter) {
        this.columnDelimiter = columnDelimiter;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public FileTemplateLines createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public FileTemplateLines createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public FileTemplateLines lastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public ZonedDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public FileTemplateLines lastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
        return this;
    }

    public void setLastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }
    
    

    public Long getIntermediateId() {
		return intermediateId;
	}

	public void setIntermediateId(Long intermediateId) {
		this.intermediateId = intermediateId;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FileTemplateLines fileTemplateLines = (FileTemplateLines) o;
        if (fileTemplateLines.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), fileTemplateLines.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
    

    @Override
	public String toString() {
		return "FileTemplateLines [id=" + id + ", templateId=" + templateId
				+ ", lineNumber=" + lineNumber
				+ ", masterTableReferenceColumn=" + masterTableReferenceColumn
				+ ", recordTYpe=" + recordTYpe + ", recordIdentifier="
				+ recordIdentifier + ", columnNumber=" + columnNumber
				+ ", enclosedChar=" + enclosedChar + ", positionBegin="
				+ positionBegin + ", positionEnd=" + positionEnd
				+ ", columnHeader=" + columnHeader + ", constantValue="
				+ constantValue + ", zeroFill=" + zeroFill + ", align=" + align
				+ ", formula=" + formula + ", dateFormat=" + dateFormat
				+ ", timeFormat=" + timeFormat + ", amountFormat="
				+ amountFormat + ", overFlow=" + overFlow + ", skipColumn="
				+ skipColumn + ", columnDelimiter=" + columnDelimiter
				+ ", createdBy=" + createdBy + ", createdDate=" + createdDate
				+ ", lastUpdatedBy=" + lastUpdatedBy + ", lastUpdatedDate="
				+ lastUpdatedDate + ", columnAlias=" + columnAlias
				+ ", intermediateId=" + intermediateId + ", recordStartRow="
				+ recordStartRow + "]";
	}
}
