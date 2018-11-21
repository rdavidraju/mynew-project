package com.nspl.app.web.rest.dto;

import java.util.HashMap;
import java.util.List;


public class FileTemplateDataDTO {

	private String status;
	private String delimiter;
	private String delimeterDescription;
	private String rowIdentifier;
	private String fileType;
	private Integer lastLineNumber ;
	private Boolean multipleIdentifierFlag;
	
	private List<String> headers;
	
	private List<HashMap<String,List<List<String[]>>>> extractedData;
	
	private List<List<String[]>> extractedDataOld;
	
	private List<HashMap<String, String>> data;
	private List<FileTemplateLinesDTO> templateLines;
	
	private List<String> listOfSampledataLines;
	public List<String> getHeaders() {
		return headers;
	}
	public void setHeaders(List<String> headersList) {
		this.headers =  headersList;
	}
	public String getDelimiter() {
		return delimiter;
	}
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}
	public List<FileTemplateLinesDTO> getTemplateLines() {
		return templateLines;
	}
	public void setTemplateLines(List<FileTemplateLinesDTO> templateLines) {
		this.templateLines = templateLines;
	}
	public List<HashMap<String, String>> getData() {
		return data;
	}
	public void setData(List<HashMap<String, String>> rows) {
		this.data = rows;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<List<String[]>> getExtractedDataOld() {
		return extractedDataOld;
	}
	public void setExtractedDataOld(List<List<String[]>> ExtractedDataOld) {
		this.extractedDataOld = ExtractedDataOld;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public Integer getLastLineNumber() {
		return lastLineNumber;
	}
	public void setLastLineNumber(Integer lastLineNumber) {
		this.lastLineNumber = lastLineNumber;
	}
	public String getDelimeterDescription() {
		return delimeterDescription;
	}
	public void setDelimeterDescription(String delimeterDescription) {
		this.delimeterDescription = delimeterDescription;
	}
	public String getRowIdentifier() {
		return rowIdentifier;
	}
	public void setRowIdentifier(String rowIdentifier) {
		this.rowIdentifier = rowIdentifier;
	}
	public List<HashMap<String, List<List<String[]>>>> getExtractedData() {
		return extractedData;
	}
	public void setExtractedData(
			List<HashMap<String, List<List<String[]>>>> extractedData) {
		this.extractedData = extractedData;
	}
	public List<String> getListOfSampledataLines() {
		return listOfSampledataLines;
	}
	public void setListOfSampledataLines(List<String> listOfSampledataLines) {
		this.listOfSampledataLines = listOfSampledataLines;
	}
	public Boolean getMultipleIdentifierFlag() {
		return multipleIdentifierFlag;
	}
	public void setMultipleIdentifierFlag(Boolean multipleIdentifierFlag) {
		this.multipleIdentifierFlag = multipleIdentifierFlag;
	}
	
	


}
