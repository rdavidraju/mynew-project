package com.nspl.app.repository;

import java.util.List;

import com.nspl.app.domain.ReportType;
import com.nspl.app.domain.Reports;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the ReportType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReportTypeRepository extends JpaRepository<ReportType,Long> {
    
	@Query(value = "select * from t_report_type where tenant_id= :tenantId and ((enable_flag =1 AND (DATEDIFF(CURDATE(),start_date)) >= 0) and if(end_date is null, sysdate()+1, (DATEDIFF(end_date,CURDATE()) >= 0)))",nativeQuery=true)
	List<ReportType> fetchActiveReportTypesByTenant(@Param("tenantId") Long tenantId);

	List<String> findDistinctTypeBytenantId(Long tenantId);

	
	@Query(value = "select distinct(type) from t_report_type where tenant_id= :tenantId and ((enable_flag =1 AND (DATEDIFF(CURDATE(),start_date)) >= 0) and if(end_date is null, sysdate()+1, (DATEDIFF(end_date,CURDATE()) >= 0)))",nativeQuery=true)
	List<String> findDistinctTypeBytenantIdAndEnableFlagIsTrue(@Param("tenantId") Long tenantId);

	ReportType findByTypeAndTenantId(String report, Long tenantId);

}
