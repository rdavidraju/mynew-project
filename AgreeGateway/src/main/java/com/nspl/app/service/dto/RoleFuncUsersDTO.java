package com.nspl.app.service.dto;

import java.time.LocalDate;
import java.util.List;

import com.nspl.app.domain.RoleFunctionAssignment;
import com.nspl.app.domain.UserRoleAssignment;

public class RoleFuncUsersDTO {
	
	private Long id;
	private Long tenantId;
	private String roleName;
	private String roleDescription;
	private LocalDate startDate;
	private LocalDate endDate;
	private Boolean activeInd;
	private List<RoleFunctionsDTO> roleFncs;
	private List<RoleUsersDTO> usrRol;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getTenantId() {
		return tenantId;
	}
	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getRoleDescription() {
		return roleDescription;
	}
	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
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
	public List<RoleFunctionsDTO> getRoleFncs() {
		return roleFncs;
	}
	public void setRoleFncs(List<RoleFunctionsDTO> roleFncs) {
		this.roleFncs = roleFncs;
	}
	public List<RoleUsersDTO> getUsrRol() {
		return usrRol;
	}
	public void setUsrRol(List<RoleUsersDTO> usrRol) {
		this.usrRol = usrRol;
	}

}
