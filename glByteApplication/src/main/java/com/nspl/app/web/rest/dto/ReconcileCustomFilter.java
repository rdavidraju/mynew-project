package com.nspl.app.web.rest.dto;

import java.util.List;

public class ReconcileCustomFilter {
	
	private Long viewId;
	private String status;
	private Long tenantId;
	private String sourceOrTarget;
	private Integer page;
	private Integer limit;
	private Long groupId;
	private List<FilterColumns> filterColumns;
	private List<String> groupedParams;
	
	public Long getViewId() {
		return viewId;
	}
	public void setViewId(Long viewId) {
		this.viewId = viewId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getTenantId() {
		return tenantId;
	}
	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
	public String getSourceOrTarget() {
		return sourceOrTarget;
	}
	public void setSourceOrTarget(String sourceOrTarget) {
		this.sourceOrTarget = sourceOrTarget;
	}
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getLimit() {
		return limit;
	}
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	public List<FilterColumns> getFilterColumns() {
		return filterColumns;
	}
	public void setFilterColumns(List<FilterColumns> filterColumns) {
		this.filterColumns = filterColumns;
	}
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public List<String> getGroupedParams() {
		return groupedParams;
	}
	public void setGroupedParams(List<String> groupedParams) {
		this.groupedParams = groupedParams;
	}
	
}
