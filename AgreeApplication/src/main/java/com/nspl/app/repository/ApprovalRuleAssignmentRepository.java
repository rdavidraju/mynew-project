package com.nspl.app.repository;

import com.nspl.app.domain.ApprovalRuleAssignment;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ApprovalRuleAssignment entity.
 */
@SuppressWarnings("unused")
public interface ApprovalRuleAssignmentRepository extends JpaRepository<ApprovalRuleAssignment,Long> {

	List<ApprovalRuleAssignment> findByRuleId(Long id);

}
