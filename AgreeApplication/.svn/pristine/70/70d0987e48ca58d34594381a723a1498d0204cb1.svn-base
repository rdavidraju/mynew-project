package com.nspl.app.repository;

import java.util.List;

import com.nspl.app.domain.JeLdrDetails;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the JeLdrDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JeLdrDetailsRepository extends JpaRepository<JeLdrDetails,Long> {

	List<JeLdrDetails> findByJeTempId(Long id);

	List<JeLdrDetails> findByJeTempIdAndRefColumnNotNull(Long jeTempId);

	JeLdrDetails findByJeTempIdAndRefColumn(Long jeTempId, int i);
    
}
