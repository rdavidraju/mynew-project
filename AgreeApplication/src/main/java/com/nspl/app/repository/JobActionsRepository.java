package com.nspl.app.repository;

import com.nspl.app.domain.JobActions;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the JobActions entity.
 */
@SuppressWarnings("unused")
public interface JobActionsRepository extends JpaRepository<JobActions,Long> {

//	List<JobActions> findBySchedulerId(Long schdlrId);

	List<JobActions> findByJobId(String jobId);

	@Query(value =  "SELECT * FROM t_job_actions where job_id = :jobId and tenant_id = :tenantId and action_name like 'Report output path for reportId:%'",nativeQuery=true)
	JobActions findReportOutputPath(@Param("jobId") String jobId, @Param("tenantId") Long tenantId);
	
	@Query(value =  "SELECT * FROM t_job_actions where job_id = :jobId and tenant_id = :tenantId and action_name like 'Report PivotOutputPath for reportId:%'",nativeQuery=true)
	JobActions findReportPivoutOutputPath(@Param("jobId") String jobId, @Param("tenantId") Long tenantId);
}
