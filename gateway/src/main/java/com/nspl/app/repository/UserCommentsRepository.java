package com.nspl.app.repository;

import java.util.List;
import com.nspl.app.domain.UserComments;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the UserComments entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserCommentsRepository extends JpaRepository<UserComments,Long> {
    
	List<UserComments> findByTenantIdAndRepliedToCommentIdIsNull(Long tenantId);
	
	List<UserComments> findByTenantIdAndRepliedToCommentId(Long tenantId, Long repliedToCommentId);
	
	List<UserComments> findByTenantIdAndIsRead(Long tenantId, Boolean isRead);
	
/*	@Query(value =  "SELECT * FROM t_rule_group where tenant_id = :tenantId and name = :name",nativeQuery=true)
	List<RuleGroup> fetchByTenantIdAndName(@Param("tenantId") Long tenantId, @Param("name") String name);
*/	
	
	@Query(value = "SELECT * FROM t_user_comments where tenant_id = :tenantId and date(creation_date) between :rangeFrom and :rangeTo and replied_to_comment_id is null",nativeQuery=true)
	List<UserComments> fetchByTenantIdAndCreationDateRange(@Param("tenantId") Long tenantId, @Param("rangeFrom") String rangeFrom, @Param("rangeTo") String rangeTo);

}
