package com.nspl.app.repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import com.nspl.app.domain.SourceProfileFileAssignments;

/**
 * Spring Data JPA repository for the SourceProfileFileAssignments entity.
 */
@SuppressWarnings("unused")
public interface SourceProfileFileAssignmentsRepository extends JpaRepository<SourceProfileFileAssignments,Long> {

	List<SourceProfileFileAssignments> findBySourceProfileId(Long sourceProfileId);
	
	@Query(value =  "SELECT template_id FROM t_source_profile_file_assignments where source_profile_id =:sp and enabled_flag is true ",nativeQuery=true)
	List<BigInteger> fetchTempIdsBySrcProfile(@Param("sp")Long sp);
	
	SourceProfileFileAssignments findBySourceProfileIdAndTemplateId(Long id,
			Long templateId);
	
	@Query(value =  "SELECT id FROM t_source_profile_file_assignments where source_profile_id in (:profileId) ",nativeQuery=true)
	List<BigInteger> findBySourceProfileIdIn(
			@Param("profileId") List<Long> profileId);
	
	@Query(value =  "SELECT template_id FROM t_source_profile_file_assignments where source_profile_id in(:sp) and enabled_flag is true ",nativeQuery=true)
	List<BigInteger> fetchTempIdsBySrcProfileIn(@Param("sp")List<BigInteger> sp);

	List<SourceProfileFileAssignments> findByTemplateId(Long id);

	

}
