package com.nspl.app.web.rest.dto;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

import org.json.JSONObject;
import org.json.simple.JSONArray;



public class FileTempDTO {
	private String ftlService;
	private String id;
	private String templateName;
	private String Description;
	private ZonedDateTime startDate;
	private ZonedDateTime endDate;
	private String status;
	private String fileType;
	private String delimiter;
	private String source;
	private Integer skipRowsStart;
	private Integer skipRowsEnd;
	private Integer numberOfColumns;
	private Integer headerRowNumber;
	private String data;
	private JSONArray sampleData;
	private List<String> colHeaders;
	private Long tenantId;
	private Long createdBy;
	private Long lastUpdatedBy;
	private String sdFilename;
	private String rowIdentifier;
	private Boolean multipleRowIdentifiers;
	private List<String> startRowColumns;
	private Boolean skipEmptyLines;
	private ExcelConditionsDTO excelConditions ;
	private Boolean defaultTemplate; 
	public String getSdFilename() {
		return sdFilename;
	}
	public void setSdFilename(String sdFilename) {
		this.sdFilename = sdFilename;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getDelimiter() {
		return delimiter;
	}
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public Integer getSkipRowsStart() {
		return skipRowsStart;
	}
	public void setSkipRowsStart(Integer skipRowsStart) {
		this.skipRowsStart = skipRowsStart;
	}
	public Integer getSkipRowsEnd() {
		return skipRowsEnd;
	}
	public void setSkipRowsEnd(Integer skipRowsEnd) {
		this.skipRowsEnd = skipRowsEnd;
	}
	public Integer getNumberOfColumns() {
		return numberOfColumns;
	}
	public void setNumberOfColumns(Integer numberOfColumns) {
		this.numberOfColumns = numberOfColumns;
	}
	public Integer getHeaderRowNumber() {
		return headerRowNumber;
	}
	public void setHeaderRowNumber(Integer headerRowNumber) {
		this.headerRowNumber = headerRowNumber;
	}
	public Long getTenantId() {
		return tenantId;
	}
	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
	public Long getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}
	public Long getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(Long lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public List<String> getColHeaders() {
		return colHeaders;
	}
	public void setColHeaders(List<String> colHeaders) {
		this.colHeaders = colHeaders;
	}
	public String getRowIdentifier() {
		return rowIdentifier;
	}
	public void setRowIdentifier(String rowIdentifier) {
		this.rowIdentifier = rowIdentifier;
	}
	public Boolean getMultipleRowIdentifiers() {
		return multipleRowIdentifiers;
	}
	public void setMultipleRowIdentifiers(Boolean multipleRowIdentifiers) {
		this.multipleRowIdentifiers = multipleRowIdentifiers;
	}
	public JSONArray getSampleData() {
		return sampleData;
	}
	public void setSampleData(JSONArray newJObject) {
		this.sampleData = newJObject;
	}
	public List<String> getStartRowColumns() {
		return startRowColumns;
	}
	public void setStartRowColumns(List<String> startRowColumns) {
		this.startRowColumns = startRowColumns;
	}
	public Boolean getSkipEmptyLines() {
		return skipEmptyLines;
	}
	public void setSkipEmptyLines(Boolean skipEmptyLines) {
		this.skipEmptyLines = skipEmptyLines;
	}
	public ExcelConditionsDTO getExcelConditions() {
		return excelConditions;
	}
	public void setExcelConditions(ExcelConditionsDTO excelConditions) {
		this.excelConditions = excelConditions;
	}
	public String getFtlService() {
		return ftlService;
	}
	public void setFtlService(String ftlService) {
		this.ftlService = ftlService;
	}
	public Boolean getDefaultTemplate() {
		return defaultTemplate;
	}
	public void setDefaultTemplate(Boolean defaultTemplate) {
		this.defaultTemplate = defaultTemplate;
	}
	
}
