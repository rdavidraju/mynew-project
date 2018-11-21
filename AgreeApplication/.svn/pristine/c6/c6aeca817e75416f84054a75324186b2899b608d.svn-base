package com.nspl.app.repository;

import com.nspl.app.domain.SummaryFileTemplateLines;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the SummaryFileTemplateLines entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SummaryFileTemplateLinesRepository extends JpaRepository<SummaryFileTemplateLines,Long> {
	List<SummaryFileTemplateLines> findByFileTemplateId(int FileTemplateId);
}
