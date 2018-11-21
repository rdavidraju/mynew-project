package com.nspl.app.web.rest.dto;
import com.nspl.app.domain.SourceConnectionDetails;

import java.util.HashMap;
import java.util.List;

public class SrcConnectionDetailsDTO {
	
	private String connectionTypeCode;
	private String connectionTypeMeaning;
	private List<HashMap> displayColumns;
	private List<SourceConnectionDetails> SourceConnectionDetails;
	public String getConnectionTypeCode() {
		return connectionTypeCode;
	}
	public void setConnectionTypeCode(String connectionTypeCode) {
		this.connectionTypeCode = connectionTypeCode;
	}
	public String getConnectionTypeMeaning() {
		return connectionTypeMeaning;
	}
	public void setConnectionTypeMeaning(String connectionTypeMeaning) {
		this.connectionTypeMeaning = connectionTypeMeaning;
	}
	
	public List<HashMap> getDisplayColumns() {
		return displayColumns;
	}
	public void setDisplayColumns(List<HashMap> displayColumns) {
		this.displayColumns = displayColumns;
	}
	public List<SourceConnectionDetails> getSourceConnectionDetails() {
		return SourceConnectionDetails;
	}
	public void setSourceConnectionDetails(
			List<SourceConnectionDetails> sourceConnectionDetails) {
		SourceConnectionDetails = sourceConnectionDetails;
	}
	
	
	
	

}
