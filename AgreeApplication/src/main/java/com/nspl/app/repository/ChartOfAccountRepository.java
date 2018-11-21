package com.nspl.app.repository;

import java.util.List;

import com.nspl.app.domain.ChartOfAccount;

import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the ChartOfAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChartOfAccountRepository extends JpaRepository<ChartOfAccount,Long> {

	Page<ChartOfAccount> findByTenantIdOrderByIdDesc(Long tenantId,
			Pageable generatePageRequest2);

	List<ChartOfAccount> findByTenantIdOrderByIdDesc(Long tenantId);

	ChartOfAccount findByIdForDisplayAndTenantId(String idForDisplay, Long tenantId);
	
	//@Query(value="select * from t_chart_of_account where tenant_id= :tenantId and enabled_flag =1", nativeQuery=true)
	List<ChartOfAccount> findByTenantIdAndEnabledFlagIsTrue(Long tenantId);

	
	Page<ChartOfAccount> findByTenantIdAndEnabledFlagIsTrue(Long tenantId,
			Pageable generatePageRequest);
	
	ChartOfAccount findByIdAndNameAndTenantId(Long id, String name, Long tenantId);
	
	List<ChartOfAccount> findByTenantIdAndName(Long tenantId, String name);

	ChartOfAccount findById(Long coaId);

	ChartOfAccount findByIdForDisplayAndNameAndTenantId(String idForDisplay, String name, Long tenantId);

	ChartOfAccount findByTenantIdAndIdForDisplay(Long tenantId, String coa);

	@Query(value="select * from t_chart_of_account where tenant_id=:tenant_id and ((enabled_flag =1 AND (DATEDIFF(CURDATE(),start_date)) >= 0) and if(end_date is null, sysdate()+1,(DATEDIFF(end_date,CURDATE())) >= 0)) order by name asc", nativeQuery=true)
	List<ChartOfAccount> findByTenantIdAndActiveState(@Param(value="tenant_id") Long tenantId);
    
}
