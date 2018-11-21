package com.nspl.app.repository;

import com.nspl.app.domain.ApprovalGroups;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ApprovalGroups entity.
 */
@SuppressWarnings("unused")
public interface ApprovalGroupsRepository extends JpaRepository<ApprovalGroups,Long> {

	List<ApprovalGroups> findByTenantId(Long tenantId);

	Page<ApprovalGroups> findByTenantId(Long tenantId, Pageable pageable);

	Page<ApprovalGroups> findByTenantIdOrderByIdDesc(Long tenantId,
			Pageable pageable);
	
	ApprovalGroups findByIdAndGroupNameAndTenantId(Long id, String groupName, Long tenantId);
	
	List<ApprovalGroups> findByTenantIdAndGroupName(Long tenantId, String groupName);
}
