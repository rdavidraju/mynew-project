package com.nspl.app.web.rest.dto;

import java.util.List;


public class DriveConnectionDTO {

	private String client_id;
	private String client_secret;
	private String project_id;
	private String auth_uri;
	private String token_uri;
	private String auth_provider_x509_cert_url;	
	private List<String> redirect_uris;
	
	public String getClient_id() {
		return client_id;
	}
	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}
	public String getClient_secret() {
		return client_secret;
	}
	public void setClient_secret(String client_secret) {
		this.client_secret = client_secret;
	}
	public String getProject_id() {
		return project_id;
	}
	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}
	public String getAuth_uri() {
		return auth_uri;
	}
	public void setAuth_uri(String auth_uri) {
		this.auth_uri = auth_uri;
	}
	public List<String> getRedirect_uris() {
		return redirect_uris;
	}
	public void setRedirect_uris(List<String> redirect_uris) {
		this.redirect_uris = redirect_uris;
	}
	public String getToken_uri() {
		return token_uri;
	}
	public void setToken_uri(String token_uri) {
		this.token_uri = token_uri;
	}
	public String getAuth_provider_x509_cert_url() {
		return auth_provider_x509_cert_url;
	}
	public void setAuth_provider_x509_cert_url(String auth_provider_x509_cert_url) {
		this.auth_provider_x509_cert_url = auth_provider_x509_cert_url;
	}
	
	


}
