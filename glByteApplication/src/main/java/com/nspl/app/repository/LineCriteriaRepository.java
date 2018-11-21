package com.nspl.app.repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nspl.app.domain.LineCriteria;

/**
 * Spring Data JPA repository for the LineCriteria entity.
 */
@SuppressWarnings("unused")
public interface LineCriteriaRepository extends JpaRepository<LineCriteria,Long> {
	
	List<LineCriteria> findByJeTempId(Long templateId);
	
	@Query(value="select id from  t_line_criteria where je_temp_id=:templateId", nativeQuery=true)
	List<BigInteger> fetchIdsByJeTempId(@Param(value = "templateId") Long templateId);
}
