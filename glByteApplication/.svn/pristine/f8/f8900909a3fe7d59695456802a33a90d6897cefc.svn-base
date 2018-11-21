package com.nspl.app.repository;

import java.util.List;

import com.nspl.app.domain.Processes;

import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Processes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProcessesRepository extends JpaRepository<Processes,Long> {

	List<Processes> findByTenantIdOrderByIdDesc(Long tenantId);

	Page<Processes> findByTenantIdOrderByIdDesc(Long tenantId,
			Pageable generatePageRequest2);


	List<Processes> findByProcessNameAndTenantId(String name, Long tenantId);

	List<Processes> findByIdForDisplayAndProcessNameAndTenantId(String idForDisplay, String name, Long tenantId);

	Processes findByTenantIdAndIdForDisplay(Long tenantId, String id);
    
}
