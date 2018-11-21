package com.nspl.app.repository;

import java.math.BigInteger;
import java.util.List;

import com.nspl.app.domain.ReportRequests;
import com.nspl.app.domain.RuleGroup;

import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the ReportRequests entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReportRequestsRepository extends JpaRepository<ReportRequests,Long> {

	List<ReportRequests> findByTenantId(Long tenantId);

	List<ReportRequests> findByTenantIdOrderByIdDesc(Long tenantId);

	List<ReportRequests> findByReportIdAndTenantId(Long repId, Long tenantId);

	Page<ReportRequests> findByReportIdAndTenantIdOrderByIdDesc(Long repId,
			Long tenantId, Pageable generatePageRequest2);

	Page<ReportRequests> findByTenantIdOrderByIdDesc(Long tenantId,
			Pageable generatePageRequest2);

	List<ReportRequests> findByReportIdAndStatusOrderByGeneratedTimeDesc(Long reportId,String status);

	List<ReportRequests> findByReportIdAndTenantIdAndRequestType(Long repId,
			Long tenantId, String requestType);

	List<ReportRequests> findByTenantIdAndRequestTypeOrderByIdDesc(
			Long tenantId, String requestType);

	Page<ReportRequests> findByTenantIdAndRequestTypeOrderByIdDesc(
			Long tenantId, String requestType, Pageable generatePageRequest2);

	
	
	Page<ReportRequests> findByReportIdAndTenantIdAndRequestTypeOrderByIdDesc(
			Long repId, Long tenantId, String requestType,
			Pageable generatePageRequest);

	Page<ReportRequests> findByReportIdAndStatusOrderByGeneratedTimeDesc(
			Long id, String string, Pageable generatePageRequestWithSortColumn);
	
	@Query(value = "select distinct report_id from t_report_requests where tenant_id=:tenantId and created_by=:userId and status = 'SUCCEEDED' group by report_id order by max(created_date) desc",nativeQuery=true)
	List<BigInteger> fectchCurrentUserRecentRanReportIds(@Param("userId") Long userId,@Param("tenantId") Long tenantId);
	
	ReportRequests findOneByTenantIdAndSysReqName(Long tenantId, String sysRequestName);
	
	ReportRequests findOneByTenantIdAndReqName(Long tenantId, String requestName);
	
	ReportRequests findByTenantIdAndIdForDisplay(Long tenantId,String idForDisplay);
	
	@Query(value = "select count(*) from t_report_requests where tenant_id= :tenantId and req_name= :request_name",nativeQuery=true)
	Long fetchRequestNameCount(@Param("tenantId") Long tenantId, @Param("request_name") String request_name);
	
	List<ReportRequests> findByReportId(Long repId);
    
}
