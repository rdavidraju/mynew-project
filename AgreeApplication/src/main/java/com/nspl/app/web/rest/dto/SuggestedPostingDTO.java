package com.nspl.app.web.rest.dto;

import java.util.List;

public class SuggestedPostingDTO {

	private Long groupId;
	private Long sViewId;
	private Long tViewId;
	private List<SuggestedPostDTO> source;
	private List<SuggestedPostDTO> target;
	
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public Long getsViewId() {
		return sViewId;
	}
	public void setsViewId(Long sViewId) {
		this.sViewId = sViewId;
	}
	public Long gettViewId() {
		return tViewId;
	}
	public void settViewId(Long tViewId) {
		this.tViewId = tViewId;
	}
	public List<SuggestedPostDTO> getSource() {
		return source;
	}
	public void setSource(List<SuggestedPostDTO> source) {
		this.source = source;
	}
	public List<SuggestedPostDTO> getTarget() {
		return target;
	}
	public void setTarget(List<SuggestedPostDTO> target) {
		this.target = target;
	}
	
}
