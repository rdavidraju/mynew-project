package com.nspl.app.repository;

import com.nspl.app.domain.DataViewUnion;

import org.json.simple.JSONObject;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

/**
 * Spring Data JPA repository for the DataViewUnion entity.
 */
@SuppressWarnings("unused")
public interface DataViewUnionRepository extends JpaRepository<DataViewUnion,Long> {

	
	List<DataViewUnion> findByDataViewLineId(Long id);

	@Query(value="select distinct ref_dv_name from t_data_view_union where data_view_line_id in (select id from t_data_views_columns where data_view_id = :dataViewId ) order by ref_dv_name asc", nativeQuery=true)
	List<BigInteger> fetchDistinctTemplateIdsByViewId(@Param("dataViewId") Long dataViewId);
	
	@Query(value="select relation from t_data_views_src_mappings where data_view_id = :dataViewId order by id desc", nativeQuery=true)
	List<String> fetchRelationByViewId(@Param("dataViewId") Long dataViewId);
	
	//SELECT * FROM applicationjagan.t_data_view_union where data_view_line_id in (select id from t_data_views_columns where data_view_id=42) and ref_dv_name=1
	@Query(value="SELECT * FROM t_data_view_union where data_view_line_id in (select id from t_data_views_columns where data_view_id= :dataViewId) and ref_dv_name= :refDvName", nativeQuery=true)
	List<DataViewUnion> fetchDataByTemplateIdByViewId(@Param("dataViewId") Long dataViewId, @Param("refDvName") Long refDvName);

	List<DataViewUnion> findByDataViewLineIdIn(List<Long> dvcIds);
	
	@Query(value="select ref_dv_name,intermediate_id FROM t_data_view_union where data_view_line_id in (SELECT id FROM t_data_views_columns where data_view_id = :dataViewId) group by ref_dv_name,intermediate_id order by ref_dv_name,intermediate_id ", nativeQuery=true)
	List<Object[]> fetchUniqueTemplateCombination(@Param("dataViewId") Long dataViewId);
	
	@Query(value="SELECT * FROM t_data_view_union where ref_dv_name = :refDvname and intermediate_id =:intermediateId and data_view_line_id in (SELECT id FROM t_data_views_columns where data_view_id = :dataViewId)", nativeQuery=true)
	List<DataViewUnion> fetchByRefDvNameAndIntermediateId(@Param("refDvname") Long refDvname, @Param("intermediateId") Long intermediateId, @Param("dataViewId") Long dataViewId);

	@Query(value="SELECT * FROM t_data_view_union where ref_dv_name = :refDvname and data_view_line_id in (SELECT id FROM t_data_views_columns where data_view_id = :dataViewId)", nativeQuery=true)
	List<DataViewUnion> fetchByRefDvNameAndDataviewId(@Param("refDvname") Long refDvname, @Param("dataViewId") Long dataViewId);
	
	/*@Query(value="select count(*) from t_data_view_union where data_view_line_id in (SELECT id FROM t_data_views_columns where data_view_id = :dataViewId) and formula is not null ", nativeQuery=true)
	int fetchFormulaColCount(@Param("dataViewId") Long dataViewId);*/
	
	
}
