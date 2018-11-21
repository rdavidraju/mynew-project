package com.nspl.app.repository;

import java.util.List;

import com.nspl.app.domain.DataViewFilters;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the DataViewFilters entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DataViewFiltersRepository extends JpaRepository<DataViewFilters,Long> {
    
	List<DataViewFilters> findByDataViewIdAndRefSrcTypeAndRefSrcId(Long dataViewId, String refSrcType, Long refSrcId);
	
	DataViewFilters findByDataViewIdAndRefSrcTypeAndRefSrcIdAndRefSrcColId(Long dataViewId, String refSrcType, Long refSrcId,Long refSrcColId);
	
	DataViewFilters findByDataViewIdAndRefSrcTypeAndRefSrcColId(Long dataViewId, String refSrcType, Long refSrcColId);

	List<DataViewFilters> findByDataViewId(Long viewId);
}
