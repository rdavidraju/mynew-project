package com.nspl.app.repository;

import java.util.List;

import com.nspl.app.domain.IntermediateTable;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the IntermediateTable entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IntermediateTableRepository extends JpaRepository<IntermediateTable,Long> {

	List<IntermediateTable> findByTemplateId(Long tempLateId);
	
	IntermediateTable findByTemplateIdAndRowIdentifier(Long templateId, String rowIdentifier);

	IntermediateTable findByTemplateIdAndRowIdentifierAndRowIdentifierCriteria(Long templateId, String rowIdentifier, String criteria);
    
}
