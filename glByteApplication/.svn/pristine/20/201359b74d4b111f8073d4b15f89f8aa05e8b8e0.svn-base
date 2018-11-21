package com.nspl.app.repository;

import java.math.BigInteger;
import java.util.List;

import com.nspl.app.domain.Reports;

import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the Reports entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReportsRepository extends JpaRepository<Reports,Long> {
					 
	@Query(value = "select id from t_reports where tenant_id=:tenantId order by id desc",nativeQuery=true)
	List<BigInteger> fectchActiveReportsByTenantId(@Param("tenantId") Long tenantId);
	
	@Query(value = "select count(*) from t_reports where tenant_id= :tenantId and report_name= :report_name",nativeQuery=true)
	Long fetchReportNameCount(@Param("tenantId") Long tenantId, @Param("report_name") String report_name);
	
	Reports findByTenantIdAndReportName(Long tenanatId, String reportNama);
	
	List<Reports> findListByTenantIdAndReportName(Long tenanatId, String reportNama);
	
	Page<Reports> findByTenantIdOrderByIdDesc(Long tenantId, Pageable pageable);
	
	@Query(value = "select report_name from t_reports where tenant_id=:tenantId and enable_flag is true order by id desc",nativeQuery=true)
	List<String> fectchActiveReportNamesByTenantId(@Param("tenantId") Long tenantId);
	
	@Query(value = "select id from t_reports where tenant_id=:tenantId and report_type_id=:report_type_id order by id desc",nativeQuery=true)
	List<BigInteger> fectchActiveReportsByTenantIdAndReportTypeId(@Param("tenantId") Long tenantId,@Param("report_type_id") Long report_type_id);
	
	Page<Reports> findByTenantIdAndIdIn(Long tenantId,List<Long>ids, Pageable pageable);

	List<Reports> findByReportTypeId(Long id);

	List<Reports> findByTenantIdAndIdIn(Long tenantId,
			List<Long> reportIdLongList);

	Reports findByTenantIdAndIdForDisplay(Long tenantId, String reportId);
	
	Reports findByTenantIdAndId(Long tenantId, Long id);
	
	List<Reports> findByTenantIdAndIdInOrderByIdDesc(Long tenantId,List<Long>ids);
	
	List<Reports> findByTenantIdOrderByIdDesc(Long tenantId);
	
	/*@Query(value = "select test.repIdForDp,test.reportName,test.reportType,test.description,test.lastRun,test.lastRunBy,test.rep_parameters,test.id from("
					+ " SELECT  r.report_name as reportName, rt.type as reportType, r.description as description,req.created_date AS lastRun,"
					+" req.created_by AS lastRunBy,req.filter_map AS rep_parameters,req.req.id FROM t_reports r, t_report_type rt,"
					+" (select report_id,max(id) as id, max(last_updated_by) as last_updated_by, max(created_by) as created_by,max(created_date) as created_date , "
					+" max(filter_map) as filter_map from t_report_requests where status = 'SUCCEEDED' and report_id in (select id from t_reports where tenant_id=4) group by report_id) req "
					+" WHERE r.tenant_id = rt.tenant_id  AND r.report_type_id = rt.id AND (r.id = req.report_id)  order by report_id desc) test",nativeQuery=true)
	List<Object[]> fectchReportsByTenantId(@Param("tenantId") Long tenantId);*/
	
	@Query(value = "select test.repIdForDp,test.reportName,test.reportTypeCode,test.reportType,test.description,test.dataViewName,test.lastRun,test.lastRunBy,"
			+ " test.rep_parameters,test.id from(SELECT r.id_for_display as repIdForDp, dv.data_view_disp_name as dataViewName, r.report_name as reportName,"
			+ " rt.type as reportTypeCode, rt.display_name as reportType, r.description as description,req.created_date AS lastRun, req.created_by AS lastRunBy,"
			+ " req.filter_map AS rep_parameters,req.id FROM t_reports r, t_report_type rt, t_data_views dv, "
			+ " (select report_id,max(id) as id, max(last_updated_by) as last_updated_by, max(created_by) as created_by,max(created_date) as created_date , "
			+ " max(filter_map) as filter_map from t_report_requests where status = 'SUCCEEDED' and report_id in "
			+ " (select id from t_reports where tenant_id= :tenantId) group by report_id) req WHERE r.tenant_id = rt.tenant_id AND r.report_type_id = rt.id "
			+ " AND r.id = req.report_id and r.source_view_id = dv.id order by report_id desc) test",nativeQuery=true)
	List<Object[]> fectchReportsByTenantId(@Param("tenantId") Long tenantId);
	
}
