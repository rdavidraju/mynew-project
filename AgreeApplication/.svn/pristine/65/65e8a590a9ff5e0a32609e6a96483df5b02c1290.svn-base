package com.nspl.app.repository;

import com.nspl.app.domain.ReportRequests;
import com.nspl.app.domain.ReportingDashboard;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the ReportingDashboard entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReportingDashboardRepository extends JpaRepository<ReportingDashboard,Long> {
	
	ArrayList<ReportingDashboard> findByTenantIdAndUserId(Long tenantId,Long userId);
	
	@Query(value =  "SELECT * FROM t_reporting_dashboard where UPPER(name)=:dbName",nativeQuery=true)
	ArrayList<ReportingDashboard> fetchDashboardListByName(@Param("dbName") String dbName);
	
	@Query(value =  "Update t_reporting_dashboard set default_flag=false where user_id=:userId",nativeQuery=true)
	void clearDefaultFlag(@Param("userId") Long userId);
	
	@Query(value =  "SELECT * FROM t_reporting_dashboard where user_id=:userId and default_flag=true",nativeQuery=true)
	ArrayList<ReportingDashboard> getDefaultDashboard(@Param("userId") Long userId);
	
	@Query(value="SELECT db_usecases.seq_num, db_usecases.usecase_name, db_usecases.groupby_col, db_usecases.output_type, db_usecases.x_axis, db_usecases.y_axis, db_usecases.width, db_usecases.height, rpt_reqs.req_name, rpt_reqs.output_path, db_usecases.id, db_usecases.colby_col, db_usecases.valby_col, rpt_reqs1.created_date, rpts.id_for_display rep_id_for_display, rpt_reqs.id_for_display req_id_for_display FROM (select distinct report_id, max(created_date) created_date from t_report_requests rq1 where rq1.status = 'SUCCEEDED' group by report_id) rpt_reqs1, t_report_requests rpt_reqs, t_reporting_dashboard_usecases db_usecases, t_reports rpts where db_usecases.dashboard_id=:dbId and db_usecases.report_temp_id=rpts.id_for_display and rpts.id=rpt_reqs.report_id and rpt_reqs.report_id=rpt_reqs1.report_id and rpt_reqs.created_date=rpt_reqs1.created_date order by db_usecases.seq_num;", nativeQuery=true)
	ArrayList<Object[]> getReportRequestsByDashboard(@Param("dbId") Long dbId);
	
	@Query(value="SELECT db_usecases.seq_num, db_usecases.usecase_name, db_usecases.groupby_col, db_usecases.output_type, db_usecases.x_axis, db_usecases.y_axis, db_usecases.width, db_usecases.height, rpt_reqs.req_name, rpt_reqs.output_path, db_usecases.id, db_usecases.colby_col, db_usecases.valby_col, rpt_reqs1.created_date, rpts.id_for_display rep_id_for_display, rpt_reqs.id_for_display req_id_for_display FROM (select distinct report_id, max(created_date) created_date from t_report_requests rq1 where rq1.status = 'SUCCEEDED' group by report_id) rpt_reqs1, t_report_requests rpt_reqs, t_reporting_dashboard_usecases db_usecases, t_reports rpts where db_usecases.id=:id and db_usecases.report_temp_id=rpts.id_for_display and rpts.id=rpt_reqs.report_id and rpt_reqs.report_id=rpt_reqs1.report_id and rpt_reqs.created_date=rpt_reqs1.created_date order by db_usecases.seq_num;", nativeQuery=true)
	ArrayList<Object[]> getReportRequestsByUsecase(@Param("id") Long id);
}
