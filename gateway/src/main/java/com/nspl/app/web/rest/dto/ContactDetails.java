package com.nspl.app.web.rest.dto;

import java.util.HashSet;
import java.util.Set;

import com.nspl.app.domain.Authority;

public class ContactDetails {

	private String login;
	private String firstName;
	private String lastName;
	private String email;
	private String imageUrl;
	private String langKey;
	private String password;

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	private Set<Authority> authorities = new HashSet<>();
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
	public String getLangKey() {
		return langKey;
	}
	public void setLangKey(String langKey) {
		this.langKey = langKey;
	}
	public Set<Authority> getAuthorities() {
		return authorities;
	}
	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}

}
