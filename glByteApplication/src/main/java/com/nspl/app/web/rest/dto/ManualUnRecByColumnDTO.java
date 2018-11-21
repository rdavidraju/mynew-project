package com.nspl.app.web.rest.dto;

import java.util.HashMap;
import java.util.List;

public class ManualUnRecByColumnDTO {
	
	private Long sColumnId;
	private Long tColumnId;
	private Long columnId;
	
	public Long getsColumnId() {
		return sColumnId;
	}
	public void setsColumnId(Long sColumnId) {
		this.sColumnId = sColumnId;
	}
	public Long gettColumnId() {
		return tColumnId;
	}
	public void settColumnId(Long tColumnId) {
		this.tColumnId = tColumnId;
	}
	public Long getColumnId() {
		return columnId;
	}
	public void setColumnId(Long columnId) {
		this.columnId = columnId;
	}
	
}