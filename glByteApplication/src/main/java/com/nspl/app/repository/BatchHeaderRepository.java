package com.nspl.app.repository;

import com.nspl.app.domain.BatchHeader;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the BatchHeader entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BatchHeaderRepository extends JpaRepository<BatchHeader,Long> {

	Page<BatchHeader> findByIdInOrderByExtractedDatetime(List<Long> batchIds, Pageable generatePageRequest2);

	Page<BatchHeader> findByIdInAndExtractionStatusContainsOrTransformationStatusContainsOrderByExtractedDatetime(List<Long> batchesIds,
			Pageable generatePageRequest2, String status, String status2);

	Page<BatchHeader> findByIdInAndExtractionStatusContainsOrTransformationStatusContains(List<Long> batchesIds,
			Pageable generatePageRequest2, String status, String status2);

	Page<BatchHeader> findByIdIn(List<Long> batchesIds, Pageable generatePageRequest);

	@Query(value =  "select *, case when transformed_datetime is null then extracted_datetime else transformed_datetime end as recent_processed_date from t_batch_header where id in(:batchIdsFiltered) order by recent_processed_date desc;",nativeQuery=true)
	List<BatchHeader> orderBatchesByExtractedAndTransformedTimeStamp(@Param("batchIdsFiltered") List<Long> batchIdsFiltered);

/*	@Query(value =  "SELECT * FROM t_batch_header where tenant_id=:tenantId and concat(id, batch_name, created_by, created_date, ext_ref, extracted_datetime, extraction_status,hold, job_ref, last_updated_by, last_updatedate, tenant_id, transformation_status, transformed_datetime, jhi_type) Like '%:keyWord%'",nativeQuery=true)
	List<BatchHeader> fetchRecordsWithKeyWord(@Param("tenantId") Long tenantId, @Param("keyWord") String keyWord);*/
	
	@Query(value =  "SELECT * FROM t_batch_header where tenant_id=:tenantId and match(batch_name,ext_ref, extraction_status, hold_reason, job_ref,next_schedule, transformation_status, jhi_type) against (:keyWord) limit :pageNumber, :limit",nativeQuery=true)
	List<BatchHeader> fetchRecordsWithKeyWord(@Param("tenantId") Long tenantId, @Param("keyWord") String keyWord, @Param("pageNumber") Long pageNumber, @Param("limit") Long limit);
	
	BatchHeader findById(Long id);

	Page<BatchHeader> findByIdInAndHoldIsTrue(List<Long> batchesIds, Pageable pageable);

	Page<BatchHeader> findByIdInAndTypeContains(List<Long> batchesIds, Pageable pageable, String string);

	Page<BatchHeader> findByIdInAndTypeNotIn(List<Long> batchesIds, Pageable pageable, List<String> scheduled);

	Page<BatchHeader> findByIdInAndTransformationStatusContains(List<Long> batchesIds, Pageable pageable,
			String status);

	Page<BatchHeader> findByIdInAndExtractionStatusContains(List<Long> batchesIds, Pageable pageable, String status);

	List<BatchHeader> findByIdIn(List<Long> batchesIds);

	List<BatchHeader> findByIdInAndHoldIsTrue(List<Long> batchesIds);

	List<BatchHeader> findByIdInAndTypeNotIn(List<Long> batchesIds, List<String> scheduled);

	List<BatchHeader> findByIdInAndTypeContains(List<Long> batchesIds, String string);
	
	@Query(value =  "SELECT * FROM t_batch_header where id in (:ids) and ( (extraction_status like CONCAT('%',:status,'%')) or (transformation_status like CONCAT('%',:status2,'%')))",nativeQuery=true)
	List<BatchHeader> fetchCountByStatus(@Param("ids") List<Long> ids, @Param("status") String status, @Param("status2") String status2);

	List<BatchHeader> findByTenantId(Long tenantId);

	@Query(value =  "SELECT id FROM t_batch_header where tenant_id=:tenantId",nativeQuery=true)
	List<Object> findBatchIdsByTenantId(@Param("tenantId")Long tenantId);

	@Query(value =  "SELECT id FROM t_batch_header where src_prf_asmt_id=:srcProfAssgnId",nativeQuery=true)
	List<Object> findBySrcPrfAsmtId(@Param("srcProfAssgnId")Long srcProfAssgnId);
	
	@Query(value =  "SELECT id FROM t_batch_header where profile_id in(:profileId)",nativeQuery=true)
	List<Object> findByProfileIdIn(@Param("profileId")List<Long> profileId);

	
}
