package com.nspl.app.repository;

import java.util.List;

import com.nspl.app.domain.Functionality;

import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the Functionality entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FunctionalityRepository extends JpaRepository<Functionality,Long> {

	@Query(value =  "SELECT * FROM functionality where id in(SELECT function_id FROM role_function_assignment where role_id=:role_id)",nativeQuery=true)
	List<Functionality> fetchAssignedFunctionsByRoleId(@Param("role_id")Long role_id);

/*	@Query(value =  "SELECT * FROM functionality where id in(SELECT function_id FROM role_function_assignment where role_id=:role_id)",nativeQuery=true)
	List<Functionality> fetchByTenantId(@Param("tenantId") Long tenantId);
*/	//select * from functionality where tenant_id = :tenantId and ((active_ind =1 AND (DATEDIFF(CURDATE(),start_date)) >= 0) and if(end_date is null, sysdate()+1,(DATEDIFF(end_date,CURDATE())) >= 0)) order by creation_date desc
	@Query(value =  "select * from functionality where tenant_id = :tenantId and ((active_ind =1 AND (DATEDIFF(CURDATE(),start_date)) >= 0) and if(end_date is null, sysdate()+1,(DATEDIFF(end_date,CURDATE())) >= 0)) order by creation_date desc",nativeQuery=true)
	List<Functionality> fetchByTenantId(@Param("tenantId") Long tenantId);
	
	List<Functionality> findByTenantIdOrderByIdDesc(Long tenantId);

	Page<Functionality> findByTenantIdOrderByIdDesc(Long tenantId,
			Pageable generatePageRequest2);
	
	Functionality findByIdAndFuncNameAndTenantId(Long id, String funcName, Long tenantId);
	
	List<Functionality> findByTenantIdAndFuncName(Long tenantId, String funName);
	
}
