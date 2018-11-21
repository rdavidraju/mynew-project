package com.nspl.app.repository;

import java.math.BigInteger;
import java.util.List;

import com.nspl.app.domain.Roles;

import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the Roles entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RolesRepository extends JpaRepository<Roles,Long> {

	List<Roles> findByTenantId(Long tenantId);

	@Query(value =  "SELECT * FROM roles where id in(SELECT role_id FROM user_role_assignment where user_id =:user_id)",nativeQuery=true)
	List<Roles> fetchAssignedRolesByUserId(@Param("user_id")Long user_id);

	@Query(value =  "SELECT id FROM roles where role_code =:roleCode",nativeQuery=true)
	List<BigInteger> findByRoleCode(@Param("roleCode") String roleCode);
	
	@Query(value =  "SELECT id FROM roles where role_code =:roleCode",nativeQuery=true)
	Long findByRoleCodeWith(@Param("roleCode") String roleCode);

	// List<Roles> findByTenantIdInOrderByIdDesc(Long tenantId);

	/* Page<Roles> findByTenantIdOrderByIdDesc(Long tenantId,
			Pageable generatePageRequest2);

	Roles findByRoleNameAndTenantId(String roleName, Long tenantId);

	Roles findByIdAndRoleNameAndTenantId(Long id, String roleName, Long tenantId);
	
	List<Roles> findByTenantIdAndRoleName(Long tenantId, String roleName); */

	List<Roles> findByTenantIdInOrderByIdDesc(List<Long> tenantIds);
	 Page<Roles> findByTenantIdInOrderByIdDesc(List<Long> tenantIds,
			Pageable generatePageRequest2);
	 
	 Roles findByRoleNameAndTenantIdIn(String roleName, List<Long> tenantIds);
	 
	 Roles findByRoleCodeAndTenantIdIn(String roleName, List<Long> tenantIds);

	 List<Roles> findByTenantIdIn(List<Long> tenantIds);

	 List<Roles> findByTenantIdInAndRoleName(List<Long> tenantIds, String name);
	
	 List<Roles> findByTenantIdInAndRoleCode(List<Long> tenantIds, String roleCode); 

	Roles findByIdAndRoleNameAndTenantIdIn(Long id, String name,List<Long> tenantIds);
	
	Roles findByIdAndRoleCodeAndTenantIdIn(Long id, String name,List<Long> tenantIds);

}
