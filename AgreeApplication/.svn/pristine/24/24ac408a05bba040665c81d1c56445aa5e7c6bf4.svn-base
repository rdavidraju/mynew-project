package com.nspl.app.repository;

import com.nspl.app.domain.DataStaging;
import com.nspl.app.domain.RuleGroup;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;

import java.math.BigInteger;
import java.util.List;

/**
 * Spring Data JPA repository for the DataStaging entity.
 */
@SuppressWarnings("unused")
public interface DataStagingRepository extends JpaRepository<DataStaging,Long> {

	List<DataStaging> findBytemplateIdAndTenantId(Long templateId, Long tenantId);

	List<DataStaging> findByTenantId(Long tenantId);

	@Query(value =  "SELECT file_name FROM t_data_staging where template_id in (SELECT template_id FROM t_data_views_src_mappings where data_view_id  in(:dataViewIds) )",nativeQuery=true)
	List<Object[]> findFileNamesByDataviewIds(@Param("dataViewIds") List<Long> dataViewIds);

	List<DataStaging> findBySrcFileInbId(Long srcFileHistInbId);

	Page<DataStaging> findBySrcFileInbIdAndRecordStatus(Long srcFileHistInbId,String recordStatus,
			Pageable generatePageRequest2);
	@Query(value =  "DELETE  FROM t_data_staging where id in(:stagingIds)",nativeQuery=true)
	void deleteByIdIn(@Param("stagingIds")List<Long> stagingIds);

	Page<DataStaging> findBySrcFileInbId(Long srcFileHistInbId,
			Pageable generatePageRequest2);
	
	// Kiran
	@Query(value =  "SELECT count(*) FROM t_data_staging where src_file_inb_id =:srcFileInbId ",nativeQuery=true)
	Integer findDataStagingSize(@Param("srcFileInbId") Long srcFileInbId);
	

	void deleteBySrcFileInbId(Long srcFileInbId);

	List<DataStaging> findBySrcFileInbIdAndRecordStatus(Long srcFileHistInbId,String recordStatus);

	void deleteBySrcFileInbId(BigInteger valueOf);

	List<DataStaging> findBySrcFileInbIdAndIntermediateIdAndRecordStatus(Long intermediateId, Long srcFileHistInbId,
			String recordStatus);

	Page<DataStaging> findBySrcFileInbIdAndIntermediateId(Long srcFileHistInbId, Long intermediateId,
			Pageable generatePageRequest2);

	Page<DataStaging> findBySrcFileInbIdAndIntermediateIdAndRecordStatus(Long srcFileHistInbId, Long intermediateId,
			String recordStatus, Pageable generatePageRequest2);

	HttpHeaders findBySrcFileInbIdAndIntermediateId(Long srcFileHistInbId, Long intermediateId);

	List<DataStaging> findBySrcFileInbIdAndIntermediateId(String srcFileHistInbId, Long id);

	@Query(value =  "SELECT * FROM t_data_staging where profile_id in (:profileId) and Date(file_date)>=:fDate and Date(file_date)<=:tDate",nativeQuery=true)
	List<DataStaging> findByProfileIdIn(@Param("profileId") List<BigInteger> profileId,@Param("fDate") String fDate,@Param("tDate") String tDate);

	
	@Query(value =  "SELECT * FROM t_data_staging where profile_id in (:profileId) and record_status =:recordStatus and Date(file_date)>=:fDate and Date(file_date)<=:tDate",nativeQuery=true)
	List<DataStaging> findByProfileIdInAndRecordStatus(@Param("profileId") List<BigInteger> profileId,@Param("recordStatus")
			String recordStatus,@Param("fDate") String fDate,@Param("tDate") String tDate);
	
	
	
	
	@Query(value =  "SELECT * FROM t_data_staging where profile_id =:profileId and template_id =:tempId and record_status =:recordStatus and Date(file_date)>=:fDate and Date(file_date)<=:tDate",nativeQuery=true)
	List<DataStaging> findByProfileIdAndTemplateIdAndRecordStatusAndFileDateBetween(@Param("profileId") Long profileId,@Param("tempId") Long tempId,@Param("recordStatus")
			String recordStatus,@Param("fDate") String fDate,@Param("tDate") String tDate);
	

	
}
