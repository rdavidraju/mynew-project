package com.nspl.app.web.rest.dto;

import java.util.List;

public class ReconUnReconAmountsDTO {
	
	public String ruleGroupId;
	public List<String> sViewIds;
	public List<String> tViewIds;
	/*public Long tenantId;*/
	public String rangeFrom;
	public String rangeTo;
	public String periodFactor;
	
	public String getRuleGroupId() {
		return ruleGroupId;
	}
	public void setRuleGroupId(String ruleGroupId) {
		this.ruleGroupId = ruleGroupId;
	}
	public List<String> getsViewIds() {
		return sViewIds;
	}
	public void setsViewIds(List<String> sViewIds) {
		this.sViewIds = sViewIds;
	}
	public List<String> gettViewIds() {
		return tViewIds;
	}
	public void settViewIds(List<String> tViewIds) {
		this.tViewIds = tViewIds;
	}
	/*	public Long getTenantId() {
		return tenantId;
	}
	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}*/
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
	public String getPeriodFactor() {
		return periodFactor;
	}
	public void setPeriodFactor(String periodFactor) {
		this.periodFactor = periodFactor;
	}

}
