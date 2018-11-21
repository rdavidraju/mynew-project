package com.nspl.app.repository;

import java.util.List;

import com.nspl.app.domain.DataViewConditions;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the DataViewConditions entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DataViewConditionsRepository extends JpaRepository<DataViewConditions,Long> {
    
	List<DataViewConditions> findByRefSrcTypeAndRefSrcId(String refSrcType, Long refSrcId);
	
	List<DataViewConditions> findByDataViewId(Long dataViewId);

}
