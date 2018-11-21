package com.nspl.app.repository;

import com.nspl.app.domain.DataViews;
import com.nspl.app.domain.MappingSet;
import com.nspl.app.domain.MappingSetValues;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the MappingSet entity.
 */
@SuppressWarnings("unused")
public interface MappingSetRepository extends JpaRepository<MappingSet,Long> {

	List<MappingSet> findByTenantId(Long tenantId);
	
	@Query(value="select * from t_mapping_set where tenant_id = :tenantId order by id desc",nativeQuery=true)
	List<MappingSet> fetchByTenantId(@Param(value = "tenantId") Long tenantId);
	
	
	@Query(value="select * from t_mapping_set where tenant_id = :tenantId and ((enabled_flag =1 AND (DATEDIFF(CURDATE(),start_date)) >= 0) and if(end_date is null, sysdate()+1,(DATEDIFF(end_date,CURDATE())) >= 0))",nativeQuery=true)
	List<MappingSet> fetchActiveMappingSetsByTenantId(@Param(value = "tenantId") Long tenantId);
	
	@Query(value="select * from t_mapping_set where tenant_id = :tenantId and purpose =:purpose and ((enabled_flag =1 AND (DATEDIFF(CURDATE(),start_date)) >= 0) and if(end_date is null, sysdate()+1,(DATEDIFF(end_date,CURDATE())) >= 0))",nativeQuery=true)
	List<MappingSet> fetchActiveMappingSetsByTenantIdAndPurpose(@Param(value = "tenantId") Long tenantId,@Param(value = "purpose") String purpose);
	
	List<MappingSet> findByTenantIdOrderByIdDesc(Long tenantId, Pageable pageable);
	
	@Query(value="select * from t_mapping_set where tenant_id = :tenantId and purpose not in('CHART_OF_ACCOUNT') order by id desc limit :limit, :pageSize",nativeQuery=true)
	List<MappingSet> fetchByTenantIdOrderByIdDesc(@Param(value = "tenantId") Long tenantId, @Param(value = "limit") Long limit, @Param(value = "pageSize") Long pageSize);
	
	@Query(value="select * from t_mapping_set where tenant_id = :tenantId and purpose = :purpose order by id desc limit :limit, :pageSize",nativeQuery=true)
	List<MappingSet> fetchByTenantIdAndPurposeOrderByIdDesc(@Param(value = "tenantId") Long tenantId,@Param(value = "purpose") String purpose, @Param(value = "limit") Long limit, @Param(value = "pageSize") Long pageSize);
	
	@Query(value="select * from t_mapping_set where tenant_id = :tenantId and purpose not in('CHART_OF_ACCOUNT')",nativeQuery=true)
	List<MappingSet> fetchOtherThancoa(@Param(value = "tenantId") Long tenantId);
	
	
	@Query(value="select * from t_mapping_set where tenant_id = :tenantId and purpose = :purpose",nativeQuery=true)
	List<MappingSet> fetchOnlycoa(@Param(value = "tenantId") Long tenantId,@Param(value = "purpose") String purpose);

	//@Query(value="select * from t_mapping_set where tenant_id = :tenantId and purpose like '%':purpose'%' order by id desc",nativeQuery=true)
	List<MappingSet> findByTenantIdAndPurposeIgnoreCaseContaining(Long tenantId, String purpose);
	
	MappingSet findByTenantIdAndName(Long tenantId, String name);

	@Query(value="select id from t_mapping_set where id = :valueId and purpose =:purpose ",nativeQuery=true)
	Long findIdByIdAndPurpose(@Param(value = "valueId") Long valueId, @Param(value = "purpose") String purpose);
	
	@Query(value="select distinct(name) from t_mapping_set where tenant_id = :tenantId ",nativeQuery=true)
	List<String> fetchDistinctNamesByTenantId(@Param(value = "tenantId") Long tenantId);
	
	@Query(value =  "select * from t_mapping_set where id in (:valueIds) and tenant_id =:tenantId",nativeQuery=true)
	List<MappingSet> findByIdInAndTenantId(@Param("valueIds") List<Long> valueIds,@Param("tenantId") Long tenantId);



}