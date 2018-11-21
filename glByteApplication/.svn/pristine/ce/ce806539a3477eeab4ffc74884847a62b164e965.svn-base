package com.nspl.app.repository;

import java.util.List;

import com.nspl.app.domain.AttachmentFiles;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the AttachmentFiles entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AttachmentFilesRepository extends JpaRepository<AttachmentFiles,Long> {

	List<AttachmentFiles> findByAttachmentId(Long id);
    
}
