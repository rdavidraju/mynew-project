package com.nspl.app.repository;

import java.math.BigInteger;
import java.util.List;

import com.nspl.app.domain.ProcessDetails;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the ProcessDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProcessDetailsRepository extends JpaRepository<ProcessDetails,Long> {

	List<ProcessDetails> findByProcessId(Long processId);

	@Query(value =  "select Id from t_process_details where process_id=:processId and tag_type =:tagType",nativeQuery=true)
	List<BigInteger> findIdByProcessIdAndTagType(
			@Param("processId") Long processId,@Param("tagType") String tagType);
	
	@Query(value =  "select type_id from t_process_details where process_id=:processId and tag_type =:tagType",nativeQuery=true)
	List<BigInteger> findTypeIdByProcessIdAndTagType(
			@Param("processId") Long processId,@Param("tagType") String tagType);

	ProcessDetails findByProcessIdAndTagType(Long processId, String string);
	
    
}
