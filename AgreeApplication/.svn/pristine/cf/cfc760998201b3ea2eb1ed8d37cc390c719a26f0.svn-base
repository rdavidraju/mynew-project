package com.nspl.app.repository;

import com.nspl.app.domain.ReportingDashboard;
import com.nspl.app.domain.ReportingDashboardUsecases;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the ReportingDashboardUsecases entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReportingDashboardUsecasesRepository extends JpaRepository<ReportingDashboardUsecases,Long> {
	
	
	ArrayList<ReportingDashboardUsecases> findByDashboardId(Long id);
	
	ArrayList<ReportingDashboardUsecases> findByDashboardIdOrderBySeqNum(Long dashboardId);
	
	@Query(value = "select * from t_reporting_dashboard_usecases where dashboard_id=:dbId and id not in(:ids)",nativeQuery=true)
	ArrayList<ReportingDashboardUsecases> fetchUsecasesByNotInIds(@Param("dbId") Long dbId, @Param("ids") ArrayList<Long> ids);
	
	
}
