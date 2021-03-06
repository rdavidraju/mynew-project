package com.nspl.app.repository;

import com.nspl.app.domain.MappingSetValues;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the MappingSetValues entity.
 */
@SuppressWarnings("unused")
public interface MappingSetValuesRepository extends JpaRepository<MappingSetValues,Long> {

	
	@Query(value =  "select * from t_mapping_set_values where mapping_set_id=:mappingSetId and ((status =1 AND (DATEDIFF(CURDATE(),start_date)) >= 0) and if(end_date is null, sysdate()+1,(DATEDIFF(end_date,CURDATE())) >= 0))",nativeQuery=true)
	List<MappingSetValues> fetchActiveMappingSetValuesByMappingId(@Param("mappingSetId") Long mappingSetId);
	
	
	List<MappingSetValues> findByMappingSetId(Long mappingSetId);





	MappingSetValues findByMappingSetIdAndSourceValue(Long valueId,
			String string);

	
}
