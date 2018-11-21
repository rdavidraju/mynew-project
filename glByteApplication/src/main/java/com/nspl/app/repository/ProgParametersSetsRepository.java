package com.nspl.app.repository;

import com.nspl.app.domain.ProgParametersSets;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

/**
 * Spring Data JPA repository for the ProgParametersSets entity.
 */
@SuppressWarnings("unused")
public interface ProgParametersSetsRepository extends JpaRepository<ProgParametersSets,Long> {

	List<ProgParametersSets> findByProgramId(Long id);

	ProgParametersSets findByProgramIdAndParameterName(Long programId, String parameterName);

	List<ProgParametersSets> findByProgramIdAndStatus(Long id, String string);

	
	@Query(value = "select * FROM t_prog_parameters_sets where program_id =:progIds ",nativeQuery=true)
	List<ProgParametersSets> findByProgramIdIn(@Param("progIds") Long progIds
			);

	List<ProgParametersSets> findByProgramIdAndStatusAndRequestFormIsTrue(
			Long id, String string);

}
