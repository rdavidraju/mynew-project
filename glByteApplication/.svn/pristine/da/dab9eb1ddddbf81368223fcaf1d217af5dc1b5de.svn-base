package com.nspl.app.repository;

import com.nspl.app.domain.LedgerDefinition;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the LedgerDefinition entity.
 */
@SuppressWarnings("unused")
public interface LedgerDefinitionRepository extends JpaRepository<LedgerDefinition,Long> {
	
	List<LedgerDefinition> findByTenantId(Long tenantId);

	List<LedgerDefinition> findByTenantIdOrderByIdDesc(Long tenantId);

	Page<LedgerDefinition> findByTenantIdOrderByIdDesc(Long tenantId,
			Pageable generatePageRequest2);

	List<LedgerDefinition> findByTenantIdAndEnabledFlagOrderByIdDesc(
			Long tenantId, Boolean activeFlag);

	@Query(value="select * from t_ledger_definition where tenant_id=:tenant_id and coa_id =:coa and ((enabled_flag =1 AND (DATEDIFF(CURDATE(),start_date)) >= 0) and if(end_date is null, sysdate()+1,(DATEDIFF(end_date,CURDATE())) >= 0)) order by name asc", nativeQuery=true)
	List<LedgerDefinition> findByTenantIdAndCoaIdAndActive(@Param(value="tenant_id") Long tenantId, @Param(value="coa") Long coa);
	
	LedgerDefinition findByIdForDisplayAndNameAndTenantId(String id, String name, Long tenantId);
	
	List<LedgerDefinition> findByTenantIdAndName(Long tenantId, String name);
	
	LedgerDefinition findByIdForDisplayAndTenantId(String idForDisplay, Long tenantId);

	LedgerDefinition findByTenantIdAndIdForDisplay(Long tenantId,
			String segValue);
}
