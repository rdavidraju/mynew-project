package com.nspl.app.repository;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nspl.app.domain.DataMaster;
import com.nspl.app.domain.DataViews;

/**
 * Spring Data JPA repository for the DataViews entity.
 */
@SuppressWarnings("unused")
public interface DataViewsRepository extends JpaRepository<DataViews,Long> {

	//List<DataViews> findByTenantId(Long tenantId);
	
	@Query(value="select * from  t_data_views where tenant_id= :tenantId and ((enabled_flag =1 AND (DATEDIFF(CURDATE(),start_date)) >= 0) and if(end_date is null, sysdate()+1, (DATEDIFF(end_date,CURDATE()) >= 0))) order by id desc", nativeQuery=true)
	List<DataViews> fetchActiveDataViews(@Param(value = "tenantId") Long tenantId);

	List<DataViews> findByTenantIdOrderByIdDesc(Long tenantId);
	
	Page<DataViews> findByTenantIdOrderByIdDesc(Long tenantId, Pageable pageable);
	
	Page<DataViews> findByTenantIdAndIdIn(Long tenantId,List<Long>ids, Pageable pageable);
	
	DataViews findById(Long id);
	
	List<DataViews> findByTenantIdAndIdInOrderByIdDesc(Long tenantId,List<Long>ids);
	
	@Query(value="select id_for_display as idForDisplay, data_view_name as dataViewName, data_view_disp_name as dataViewDispName from t_data_views where id in (select data_view_id from t_data_views_src_mappings where relation= :relation) and tenant_id= :tenant_id",nativeQuery=true)
	List<Object[]> fetchDataViewsByRelation(@Param(value = "relation") String relation, @Param(value = "tenant_id") Long tenant_id);
	
	@Query(value="select * from t_data_views where id in (select data_view_id from t_data_views_src_mappings where relation is null) and tenant_id= :tenant_id",nativeQuery=true)
	List<DataViews> fetchSingleTemplateDataViews(@Param(value = "tenant_id") Long tenant_id);
	
	@Query(value="select id from t_data_views where id in (select data_view_id from t_data_views_src_mappings where relation= :relation) and tenant_id= :tenant_id",nativeQuery=true)
	List<BigInteger> fetchDataViewIdsByRelation(@Param(value = "relation") String relation, @Param(value = "tenant_id") Long tenant_id);
	
	@Query(value="select id from t_data_views where id in (select data_view_id from t_data_views_src_mappings where relation in(:relations)) and tenant_id= :tenant_id",nativeQuery=true)
	List<BigInteger> fetchDataViewIdsByRelationIn(@Param(value = "relations") List<String> relations, @Param(value = "tenant_id") Long tenant_id);

	@Query(value="select id from t_data_views where id in (select data_view_id from t_data_views_src_mappings where relation is null) and tenant_id= :tenant_id",nativeQuery=true)
	List<BigInteger> fetchDataViewIdsByRelationIsNull(@Param(value = "tenant_id") Long tenant_id);

	@Query(value="SELECT id FROM t_data_views where id in(:dataViewIds) order by data_view_disp_name asc",nativeQuery=true)
	List<BigInteger> fetchDataViewOrderByNameAsc(@Param(value = "dataViewIds") List<BigInteger> dataViewIds);

	List<DataViews> findByTenantIdOrderByDataViewDispNameAsc(Long tenantId);

	Page<DataViews> findByTenantIdOrderByDataViewDispNameAsc(Long tenantId,
			Pageable generatePageRequest2);
	
	DataViews findByIdAndDataViewNameAndTenantId(Long id, String viewName, Long tenantId);
	
	DataViews findByIdAndDataViewDispNameAndTenantId(Long id, String viewName, Long tenantId);
	
	List<DataViews> findByTenantIdAndDataViewName(Long tenantId, String dataViewName);
	
	DataViews findByDataViewNameAndTenantId(String dataViewName, Long tenantId);
	
	List<DataViews> findByTenantIdAndDataViewDispName(Long tenantId, String dataViewName);
	
	// kiran
	@Query(value="select * from t_data_views where id in (:dataViewIds) ",nativeQuery=true)
	List<DataViews> fetchByIds(@Param(value = "dataViewIds") List<Long> dataViewIds);

	Page<DataViews> findByTenantIdAndIdInOrderByIdDesc(Long tenantId,
			List<Long> ids, Pageable generatePageRequest2);

	DataViews findByTenantIdAndIdForDisplay(Long tenantId, String dataViewId);

	List<DataViews> findByTenantId(Long tenantId);

	List<DataViews> findByIdIn(Set<Long> dataViewIdFromRule);
	
	//List<DataViews> findByTenantIdAndIdInOrderByIdDesc(Long tenantId,List<Long>ids);
	
	/*@Query(value="SELECT table_name,column_name,ordinal_position, data_type,column_type FROM information_schema.columns "
				  +" WHERE table_schema=DATABASE() AND table_name= :viewName and column_name not in ( SELECT "
				  + " column_name FROM information_schema.columns WHERE table_schema=DATABASE() AND table_name= :tableName	)",nativeQuery=true)
	List<Object[]> fetchMissingColumns(@Param(value="viewName") String viewName, @Param(value="tableName") String tableName);*/
	
	@Query(value="SELECT table_name,column_name,ordinal_position, data_type,column_type FROM information_schema.columns "
			     +" WHERE table_schema=DATABASE() AND table_name = :tableName and column_name= :columnName ",nativeQuery=true)
	List<Object[]> fetchColumnInfo(@Param(value="tableName") String tableName, @Param(value="columnName") String columnName);
	
	@Query(value="SELECT table_name,column_name,ordinal_position, data_type,column_type FROM information_schema.columns "
			  +" WHERE table_schema=DATABASE() AND table_name= :tableName and column_name not in ( SELECT "
			  + " column_name FROM information_schema.columns WHERE table_schema=DATABASE() AND table_name= :viewName	)",nativeQuery=true)
	List<Object[]> fetchMissingViewColumns(@Param(value="tableName") String tableName,@Param(value="viewName") String viewName);

	@Query(value="help \"string functions\" ",nativeQuery=true)
	List<Object[]> fetchStringFunctions();
	
	@Query(value="select * from  t_data_views where tenant_id= :tenantId and ((enabled_flag =1 AND (DATEDIFF(CURDATE(),start_date)) >= 0) and if(end_date is null, sysdate()+1, (DATEDIFF(end_date,CURDATE()) >= 0))) order by data_view_disp_name asc", nativeQuery=true)
	List<DataViews> fetchDataViewByTenantIdOrderByDataViewDispNameAsc(@Param(value="tenantId") Long tenantId);
	
	/*Page<DataViews> findByTenantIdOrderByDataViewDispNameAsc(Long tenantId,
			Pageable generatePageRequest2);*/
}
