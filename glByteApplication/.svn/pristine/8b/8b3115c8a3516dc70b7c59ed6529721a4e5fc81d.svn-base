package com.nspl.app.repository;

import java.math.BigInteger;
import java.util.List;

import com.nspl.app.domain.Segments;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the Segments entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SegmentsRepository extends JpaRepository<Segments,Long> {

	/*@Query(value =  "SELECT * FROM t_segments where coa_id = :coaId order by segment_name asc",nativeQuery=true)*/
	List<Segments> findByCoaId(Long coaId);
	
	@Query(value =  "SELECT * FROM t_segments where coa_id = :coaId order by sequence asc",nativeQuery=true)
	List<Segments> findByCoaIdOrderBySequenceAsc(@Param("coaId") Long coaId);


	@Query(value =  "SELECT * FROM t_segments where coa_id in (:coaList) and qualifier =:qualifier",nativeQuery=true)
	List<Segments> findByCoaIdInAndQualifier(@Param("coaList") List<Long> coaList,@Param("qualifier") String qualifier);
	
	@Query(value =  "SELECT * FROM t_segments where coa_id = :coaId and qualifier=:qualifier order by sequence asc",nativeQuery=true)
	List<Segments> findByCoaIdAndQualifierOrderBySequenceAsc(@Param("coaId") Long coaId, @Param("qualifier") String qualifier);


	@Query(value = "SELECT * FROM t_segments where coa_id in (select distinct coa_ref from t_accounting_data where original_view_id=:viewId) and qualifier='NATURAL_ACCOUNT'",nativeQuery=true)
	Segments fetchAccQualifySeg(@Param("viewId") Long viewId);
	
	@Query(value = "SELECT * FROM t_segments where coa_id in (select distinct COA from t_rules where source_data_view_id=:viewId) and qualifier=:qualifier",nativeQuery=true)
	Segments fetchSegByQualifier(@Param("viewId") Long viewId,@Param("qualifier") String qualifier);

	Segments findByCoaIdAndQualifier(Long coaId, String string);
	
	@Query(value = "SELECT id,segment_name,sequence,coa_id FROM t_segments where coa_id in (select distinct coa_ref from t_accounting_data where original_view_id=:viewId) order by sequence asc",nativeQuery=true)
	List<Object[]> fetchSegmentsListforViewCoa(@Param("viewId") Long viewId);
	
	@Query(value = "SELECT segments.segment_name, map_set_vals.source_value, map_set_vals.target_value FROM t_segments segments,t_mapping_set_values map_set_vals where "
	 +" segments.coa_id in (select distinct coa_ref from t_accounting_data acc_data where acc_data.original_view_id= :viewId) and segments.value_id=map_set_vals.mapping_set_id and segment_name= :segmentName",nativeQuery=true)
	List<Object[]> fetchSegmentsValueSet(@Param("viewId") Long viewId,@Param("segmentName") String segmentName);

	
	@Query(value = "SELECT distinct value_id FROM t_segments where segment_length =:segmentLength and qualifier =:qualifier",nativeQuery=true)
	List<Long> findBySegmentLengthAndQualifier(@Param("segmentLength") int segmentLength,
			@Param("qualifier") String qualifier);
	
	@Query(value = "SELECT distinct value_id FROM t_segments where segment_length =:segmentLength and value_id not in (:valueIds)",nativeQuery=true)
	List<Long> findBySegmentLengthAndValueIdNotIn(@Param("segmentLength") int segmentLength,
			@Param("valueIds") List<Long> valueIds);
	
	@Query(value = "SELECT distinct value_id FROM t_segments where segment_length =:segmentLength",nativeQuery=true)
	List<Long> findBySegmentLength(@Param("segmentLength") int segmentLength
			);
	
}
