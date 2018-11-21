package com.nspl.app.repository;

import com.nspl.app.domain.TemplAttributeMapping;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TemplAttributeMapping entity.
 */
@SuppressWarnings("unused")
public interface TemplAttributeMappingRepository extends JpaRepository<TemplAttributeMapping,Long> {

	
	List<TemplAttributeMapping> findByJeTempId(Long templateId);
	
	TemplAttributeMapping findByJeTempIdAndAttributeName(Long jeTempId, String attributeName);
	
}
