package com.nspl.app.service.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

import com.nspl.app.domain.Roles;

public class UserRolesDTO {
	
	private Long id;
	private String login;
	private String firstName;
	private String lastName;
	private String email;
	private String imageUrl;
	private Boolean activated;
	private String langKey;
	private String activationKey;
	private String resetKey;
	private String createdBy;
	private LocalDate effectiveDate;
	private ZonedDateTime startDate;
	private ZonedDateTime endDate;
	private Instant createdDate;
	private Instant resetDate;
	private String lastModifiedBy;
	private Instant lastModifiedDate;
	private byte[] image;
	private Long tenantId;
	private String businessTile;
	private Long managerId;
	private String workLocation;
	private LocalDate dateOfBirth;
	private String address;
	private String contactNum;
	private String timeZone;
	private String organizationName;
	private String managerName;
	private byte[] managerImage;
	private String gender;
	private List<UserRoleAssignmentDTO> userRoleAssnmt;
	private List<Roles> userRoleAssignments;
	
	
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
	public String getManagerName() {
		return managerName;
	}
	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}
	public byte[] getManagerImage() {
		return managerImage;
	}
	public void setManagerImage(byte[] managerImage) {
		this.managerImage = managerImage;
	}
	
	
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public LocalDate getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(LocalDate effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public Boolean getActivated() {
		return activated;
	}
	public void setActivated(Boolean activated) {
		this.activated = activated;
	}
	public String getLangKey() {
		return langKey;
	}
	public void setLangKey(String langKey) {
		this.langKey = langKey;
	}
	public String getActivationKey() {
		return activationKey;
	}
	public void setActivationKey(String activationKey) {
		this.activationKey = activationKey;
	}
	public String getResetKey() {
		return resetKey;
	}
	public void setResetKey(String resetKey) {
		this.resetKey = resetKey;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Instant getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Instant createdDate) {
		this.createdDate = createdDate;
	}
	public Instant getResetDate() {
		return resetDate;
	}
	public void setResetDate(Instant resetDate) {
		this.resetDate = resetDate;
	}
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
	public Instant getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(Instant lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public Long getTenantId() {
		return tenantId;
	}
	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public String getBusinessTile() {
		return businessTile;
	}
	public void setBusinessTile(String businessTile) {
		this.businessTile = businessTile;
	}
	public Long getManagerId() {
		return managerId;
	}
	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}
	public String getWorkLocation() {
		return workLocation;
	}
	public void setWorkLocation(String workLocation) {
		this.workLocation = workLocation;
	}
	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getContactNum() {
		return contactNum;
	}
	public void setContactNum(String contactNum) {
		this.contactNum = contactNum;
	}
	public String getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	public List<UserRoleAssignmentDTO> getUserRoleAssnmt() {
		return userRoleAssnmt;
	}
	public void setUserRoleAssnmt(List<UserRoleAssignmentDTO> userRoleAssnmt) {
		this.userRoleAssnmt = userRoleAssnmt;
	}
	public List<Roles> getUserRoleAssignments() {
		return userRoleAssignments;
	}
	public void setUserRoleAssignments(List<Roles> userRoleAssignments) {
		this.userRoleAssignments = userRoleAssignments;
	}
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	} 
	
	

}
