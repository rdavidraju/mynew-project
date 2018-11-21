package com.nspl.app.repository;

import java.util.List;

import com.nspl.app.domain.RoleFunctionAssignment;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the RoleFunctionAssignment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoleFunctionAssignmentRepository extends JpaRepository<RoleFunctionAssignment,Long> {

	List<RoleFunctionAssignment> findByRoleId(Long id);
	
	RoleFunctionAssignment findByFunctionIdAndRoleId(Long functionId, Long roleId);

	List<RoleFunctionAssignment> findByFunctionId(Long functId);
    
}
