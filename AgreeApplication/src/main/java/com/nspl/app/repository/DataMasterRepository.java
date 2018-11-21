package com.nspl.app.repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nspl.app.domain.DataMaster;

/**
 * Spring Data JPA repository for the DataMaster entity.
 */
@SuppressWarnings("unused")
public interface DataMasterRepository extends JpaRepository<DataMaster,Long> {

	List<DataMaster> findByTenantIdAndTemplateId(Long tenantId, Long templateId);

	
	DataMaster findById(Long id);
	
	//shiva
	@Query(value =  "SELECT :fieldName FROM t_data_master where id = :id",nativeQuery=true)
	String fetchColumnValueByIdAndFieldName(@Param("fieldName") String fieldName,@Param("id") Long id);
		
	//shiva
	/*@Query(value =  "SELECT * FROM t_data_master where id in (:ids)",nativeQuery=false)*/
	Page<DataMaster> findByIdIn(@Param("ids") List<Long> ids, Pageable pageable);

	List<DataMaster> findByIdIn(@Param("ids") List<Long> ids);


	List<DataMaster> findBySrcFileInbId(Long srcFileHistInbId);


	Page<DataMaster> findBySrcFileInbId(Long srcFileHistInbId,
			Pageable generatePageRequest);


	List<DataMaster> findBySrcFileInbIdAndIntermediateId(Long srcFileHistInbId, Long intermediateId);


	Page<DataMaster> findBySrcFileInbIdAndIntermediateId(Long intermediateId, Long srcFileHistInbId,
			Pageable generatePageRequest2);


	List<DataMaster> findBySrcFileInbIdAndIntermediateId(String srcFileHistInbId, Long id);
	
	
	@Query(value =  "select id from t_data_master where tenant_id =:tenantId and file_date between :fmDate and :toDate",nativeQuery=true)
	List<BigInteger> fetchIdsByFileDates(@Param("tenantId") Long tenantId,@Param("fmDate") String fmDate,@Param("toDate") String toDate);
	
}
