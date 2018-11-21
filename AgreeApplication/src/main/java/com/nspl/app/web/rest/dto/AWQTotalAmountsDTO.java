package com.nspl.app.web.rest.dto;

import java.util.List;

public class AWQTotalAmountsDTO {
	
	private List<Long> viewIds;
	private String periodFactor;
	private String rangeFrom;
	private String rangeTo;
	
	public List<Long> getViewIds() {
		return viewIds;
	}
	public void setViewIds(List<Long> viewIds) {
		this.viewIds = viewIds;
	}
	public String getPeriodFactor() {
		return periodFactor;
	}
	public void setPeriodFactor(String periodFactor) {
		this.periodFactor = periodFactor;
	}
	public String getRangeFrom() {
		return rangeFrom;
	}
	public void setRangeFrom(String rangeFrom) {
		this.rangeFrom = rangeFrom;
	}
	public String getRangeTo() {
		return rangeTo;
	}
	public void setRangeTo(String rangeTo) {
		this.rangeTo = rangeTo;
	}

}
