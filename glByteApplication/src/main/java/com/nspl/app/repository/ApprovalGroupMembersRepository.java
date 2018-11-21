package com.nspl.app.repository;

import com.nspl.app.domain.ApprovalGroupMembers;
import com.nspl.app.domain.DataViews;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the ApprovalGroupMembers entity.
 */
@SuppressWarnings("unused")
public interface ApprovalGroupMembersRepository extends JpaRepository<ApprovalGroupMembers,Long> {

	List<ApprovalGroupMembers> findByGroupId(Long groupId);
	
	List<ApprovalGroupMembers> findByUserId(Long userId);

	@Query(value="SELECT max(seq) FROM t_approval_group_members where group_id = :groupId",nativeQuery=true)
	Integer fetchMaxSeqByGroupId(@Param(value = "groupId") Long groupId);

	List<ApprovalGroupMembers> findByGroupIdOrderBySeqAsc(Long assigneeId);

	ApprovalGroupMembers findByUserIdAndGroupId(Long userId, Long assigneeId); 

}
