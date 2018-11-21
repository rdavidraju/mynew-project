package com.nspl.app.repository;

import java.util.List;

import com.nspl.app.domain.JournalApprovalInfo;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the JournalApprovalInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JournalApprovalInfoRepository extends JpaRepository<JournalApprovalInfo,Long> {

	List<JournalApprovalInfo> findByApprovalBatchId(Long id);
    
}
