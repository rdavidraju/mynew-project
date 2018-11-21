package com.nspl.app.web.rest.dto;

import java.util.ArrayList;

import com.nspl.app.domain.ReportingDashboard;
import com.nspl.app.domain.ReportingDashboardUsecases;

public class ReportingDashboardDTO {

	private ReportingDashboard reportingDashboard;
	private ArrayList<ReportingDashboardUsecases> reportingDashboardUsecasesList;
	
	public ReportingDashboard getReportingDashboard() {
		return reportingDashboard;
	}
	public void setReportingDashboard(ReportingDashboard reportingDashboard) {
		this.reportingDashboard = reportingDashboard;
	}
	public ArrayList<ReportingDashboardUsecases> getReportingDashboardUsecasesList() {
		return reportingDashboardUsecasesList;
	}
	public void setReportingDashboardUsecasesList(ArrayList<ReportingDashboardUsecases> reportingDashboardUsecasesList) {
		this.reportingDashboardUsecasesList = reportingDashboardUsecasesList;
	}
	
}
