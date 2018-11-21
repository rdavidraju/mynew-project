package com.nspl.app.repository;

import java.util.List;

import com.nspl.app.domain.Hierarchy;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the Hierarchy entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HierarchyRepository extends JpaRepository<Hierarchy,Long> {
    
	@Query(value="SELECT * FROM t_hierarchy where tenant_id =:tenant_id and object_name =:objectName and object_type =:object_type and enable_flag = 1", nativeQuery=true)
	Hierarchy findByTenantIdAndObjectNameAndObjectType(@Param("tenant_id")Long tenantId, @Param("objectName")String objectName,@Param("object_type")String object_type);
	
	@Query(value="SELECT * FROM t_hierarchy where tenant_id =:tenantId and object_type =:object_type and parent_id =:parent_id and enable_flag = 1", nativeQuery=true)
	List<Hierarchy> findByTenantIdAndObjectTypeAndParentId(@Param("tenantId")Long tenantId,@Param("object_type")String object_type,@Param("parent_id")Long parent_id );

	List<Hierarchy> findByParentId(Long userId);

	Hierarchy findByObjectName(String string);

	Hierarchy findByTenantIdAndParentId(Long tenantId, Long parentId);
	
	List<Hierarchy> findByTenantId(Long tenantId);

	Hierarchy findByObjectNameAndParentId(String string, Long managerId);

	List<Hierarchy> findByIdForDisplay(String tenantId);

	
}
