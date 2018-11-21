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

	@Query(value =  "SELECT id FROM roles where role_name =:roleName",nativeQuery=true)
	List<BigInteger> findByRoleName(@Param("roleName") String roleName);

	List<Roles> findByTenantIdOrderByIdDesc(Long tenantId);

	Page<Roles> findByTenantIdOrderByIdDesc(Long tenantId,
			Pageable generatePageRequest2);

	Roles findByRoleNameAndTenantId(String roleName, Long tenantId);

	Roles findByIdAndRoleNameAndTenantId(Long id, String roleName, Long tenantId);
	
	List<Roles> findByTenantIdAndRoleName(Long tenantId, String roleName);

	//List<BigInteger> findByRoleNameAndTenantId(String roleName, Long tenantId);

    
}
