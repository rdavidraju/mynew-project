package com.nspl.app.service.dto;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

import com.nspl.app.domain.RoleFunctionAssignment;
import com.nspl.app.domain.UserRoleAssignment;

public class RoleFuncUsersDTO {
	
	private Long id;
	private Long tenantId;
	private String roleCode;
	private String roleName;
	private String roleDescription;
	private ZonedDateTime startDate;
	private ZonedDateTime endDate;
	private Boolean activeInd;
	private List<RoleFunctionsDTO> roleFncs;
	private List<RoleUsersDTO> usrRol;
	private Boolean isDefaultRole;
	
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
	public String getRoleCode() {
		return roleCode;
	}
	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
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
	
	public Boolean getIsDefaultRole() {
		return isDefaultRole;
	}
	public void setIsDefaultRole(Boolean isDefaultRole) {
		this.isDefaultRole = isDefaultRole;
	}
	@Override
	public String toString() {
		return "RoleFuncUsersDTO [id=" + id + ", tenantId=" + tenantId
				+ ", roleCode=" + roleCode + ", roleName=" + roleName
				+ ", roleDescription=" + roleDescription + ", startDate="
				+ startDate + ", endDate=" + endDate + ", activeInd="
				+ activeInd + ", roleFncs=" + roleFncs + ", usrRol=" + usrRol
				+ ", isDefaultRole=" + isDefaultRole
				+ "]";
	}

}
