package com.nspl.app.web.rest.dto;

import java.time.LocalDate;
import java.util.List;

import com.nspl.app.domain.Functionality;

public class RolesDTO {
	private Long id;
    private String roleName;
    private String roleDesc;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean activeInd;
    private List<FunctionalityDTO> functions;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getRoleDesc() {
		return roleDesc;
	}
	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	public Boolean getActiveInd() {
		return activeInd;
	}
	public void setActiveInd(Boolean activeInd) {
		this.activeInd = activeInd;
	}
	public List<FunctionalityDTO> getFunctions() {
		return functions;
	}
	public void setFunctions(List<FunctionalityDTO> functions) {
		this.functions = functions;
	}
	
	
}
