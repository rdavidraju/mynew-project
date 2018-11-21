package com.nspl.app.repository;

import com.nspl.app.domain.LookUpType;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the LookUpType entity.
 */
@SuppressWarnings("unused")
public interface LookUpTypeRepository extends JpaRepository<LookUpType,Long> {

	List<LookUpType> findByTenantId(Long tenantId);
	
	LookUpType findByTenantIdAndLookUpType(Long tenantId, String lookUpType);

}
