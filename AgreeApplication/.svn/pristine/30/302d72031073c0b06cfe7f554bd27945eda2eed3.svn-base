package com.nspl.app.repository;

import com.nspl.app.domain.AttachmentList;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the AttachmentList entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AttachmentListRepository extends JpaRepository<AttachmentList,Long> {

	

	AttachmentList findByAttachmentKeyAndTenantId(String attachmentKey,
			Long tenantId);
    
}
