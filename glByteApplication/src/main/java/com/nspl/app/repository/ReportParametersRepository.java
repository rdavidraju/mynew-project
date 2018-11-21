package com.nspl.app.repository;

import java.util.List;

import com.nspl.app.domain.ReportParameters;
import com.nspl.app.domain.Reports;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the ReportParameters entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReportParametersRepository extends JpaRepository<ReportParameters,Long> {
    
	ReportParameters findByReportIdAndRefTypeidAndRefSrcIdAndRefColId(Long reportId,String reportTypeid, Long refSrcId,Long refColId);
	
	List<ReportParameters> findByReportId(Long reportId);
	
	ReportParameters findByReportIdAndRefTypeidAndRefSrcId(Long reportId,String reportTypeid, Long refSrcId);
	
	List<ReportParameters> findByReportIdAndRefSrcIdAndRefTypeid(Long reportId, Long refSrcId,String reportTypeid);
	
	//select column_name from information_schema.columns where table_name='t_report_parameters';
	
	@Query(value = "select column_name from information_schema.columns where table_name= :table_name",nativeQuery=true)
	List<String> fetchTableColumns(@Param("table_name") String table_name);
	
	List<ReportParameters> findByReportIdAndRefTypeid(Long reportId, String refTypeId);

	List<ReportParameters> findByReportIdOrderByIdAsc(Long reportId);
}
