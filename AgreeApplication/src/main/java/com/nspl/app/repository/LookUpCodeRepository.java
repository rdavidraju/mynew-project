package com.nspl.app.repository;

import com.nspl.app.domain.DataViewsColumns;
import com.nspl.app.domain.LookUpCode;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the LookUpCode entity.
 */
@SuppressWarnings("unused")
public interface LookUpCodeRepository extends JpaRepository<LookUpCode,Long> {

	@Query(value="select * from look_up_code where tenant_id=:tenant_id and look_up_type=:look_up_type and ((enable_flag =1 AND (DATEDIFF(CURDATE(),active_start_date)) >= 0) and if(active_end_date is null, sysdate()+1,(DATEDIFF(active_end_date,CURDATE())) >= 0)) ORDER  BY ABS(secure_attribute_1) ASC", nativeQuery=true)
	List<LookUpCode> findByTenantAndLookUpTypeAndActiveState(@Param(value="tenant_id") Long tenantId,@Param(value = "look_up_type") String look_up_type);

	@Query(value="select * from look_up_code where tenant_id=:tenant_id and look_up_type=:look_up_type ORDER BY creation_date desc", nativeQuery=true)
	List<LookUpCode> fetchByTenantIdAndLookUpType(@Param(value="tenant_id") Long tenantId,@Param(value = "look_up_type") String look_up_type);

	
	@Query(value="select * from look_up_code where tenant_id=:tenant_id and look_up_code=:look_up_code and look_up_type=:look_up_type and ((enable_flag =1 AND (DATEDIFF(CURDATE(),active_start_date)) >= 0) and if(active_end_date is null, sysdate()+1,(DATEDIFF(active_end_date,CURDATE())) >= 0)) ORDER BY ABS(secure_attribute_1) ASC", nativeQuery=true)
	LookUpCode findByTenantIdAndLookUpCodeAndLookUpType(@Param(value="tenant_id") Long tenantId,@Param(value = "look_up_code") String look_up_code,@Param(value = "look_up_type") String look_up_type);

	@Query(value="select * from look_up_code where tenant_id=:tenant_id and look_up_type=:look_up_type and ((enable_flag =1 AND (DATEDIFF(CURDATE(),active_start_date)) >= 0) and if(active_end_date is null, sysdate()+1,(DATEDIFF(active_end_date,CURDATE())) >= 0)) ORDER BY ABS(secure_attribute_1) ASC", nativeQuery=true)
	List<LookUpCode> findByTenantIdAndLookUpType(@Param(value="tenant_id") Long tenantId,@Param(value = "look_up_type") String look_up_type);
	
	@Query(value="select look_up_code from look_up_code where tenant_id=:tenant_id and look_up_type=:look_up_type and ((enable_flag =1 AND (DATEDIFF(CURDATE(),active_start_date)) >= 0) and if(active_end_date is null, sysdate()+1,(DATEDIFF(active_end_date,CURDATE())) >= 0)) ORDER BY ABS(secure_attribute_1) ASC", nativeQuery=true)
	List<String> fetchLookupsByTenantIdAndLookUpType(@Param(value="tenant_id") Long tenantId,@Param(value = "look_up_type") String look_up_type);


	LookUpCode findByTenantIdAndLookUpCode(Long tenantId, String connectionType);


	LookUpCode findByLookUpTypeAndMeaningAndTenantId(String delimeter, String delimiterChar, Long tenantId);
	
	//LookUpCode findByLookUpTypeAndLookUpCodeAndTenantId(String delimeter, String string, long l);


	LookUpCode findByLookUpTypeAndLookUpCodeAndTenantId(String delimeter,
			String string, Long tenantId);


	LookUpCode findByTenantIdAndLookUpTypeAndDescription(Long tenantId,String string, String delimiter);
	
	@Query(value="select * from look_up_code where tenant_id=:tenant_id and ((enable_flag =1 AND (DATEDIFF(CURDATE(),active_start_date)) >= 0) and if(active_end_date is null, sysdate()+1,(DATEDIFF(active_end_date,CURDATE())) >= 0)) ORDER BY id desc", nativeQuery=true)
	List<LookUpCode> fetchByTenanatIdAndActiveState(@Param(value="tenant_id") Long tenantId);
	
	@Query(value="select * from look_up_code where tenant_id=:tenant_id ORDER BY creation_date desc", nativeQuery=true)
	List<LookUpCode> fetchByTenanatId(@Param(value="tenant_id") Long tenantId);
	
	@Query(value="select meaning from look_up_code where tenant_id=:tenantId and look_up_code=:lookUpCode and look_up_type =:lookUpType and ((enable_flag =1 AND (DATEDIFF(CURDATE(),active_start_date)) >= 0) and if(active_end_date is null, sysdate()+1,(DATEDIFF(active_end_date,CURDATE())) >= 0))", nativeQuery=true)
	String fetchLookUpMeaningByLookUpCodeAndLookUpTypeAndTenantId(@Param(value="tenantId") Long tenantId, @Param(value="lookUpCode") String lookUpCode, @Param(value="lookUpType") String lookUpType);


	List<LookUpCode> findByTenantId(Long tenantId);
	
	LookUpCode findByIdAndLookUpTypeAndLookUpCodeAndTenantId(Long id, String type, String code, Long tenantId);
	
	LookUpCode findByTenantIdAndLookUpTypeAndLookUpCode(Long tenantId, String type, String code);
	
	LookUpCode findByTenantIdAndLookUpTypeAndMeaning(Long tenantId, String type, String code);

	LookUpCode findByLookUpCodeAndTenantIdAndCreatedBy(String string,
			Long tenantId, Long userId);
	
	
/*	LookUpCode findByIdAndGroupNameAndTenantId(Long id, );*/
	
	@Query(value="select * from look_up_code where tenant_id=:tenant_id and look_up_type=:look_up_type and module = :module and ((enable_flag =1 AND (DATEDIFF(CURDATE(),active_start_date)) >= 0) and if(active_end_date is null, sysdate()+1,(DATEDIFF(active_end_date,CURDATE())) >= 0)) ORDER BY ABS(secure_attribute_1) ASC", nativeQuery=true)
	List<LookUpCode> fetchActiveReportingActiveStaus(@Param(value="tenant_id") Long tenantId,@Param(value = "look_up_type") String look_up_type, @Param(value = "module") String module);
	
	
	@Query(value="select * from look_up_code where tenant_id=0 and look_up_type=:look_up_type and ((enable_flag =1 AND (DATEDIFF(CURDATE(),active_start_date)) >= 0) and if(active_end_date is null, sysdate()+1,(DATEDIFF(active_end_date,CURDATE())) >= 0)) ORDER BY ABS(secure_attribute_1) ASC", nativeQuery=true)
	List<LookUpCode> findByTenantAndLookUpTypeAndActiveStateForZero(@Param(value = "look_up_type") String look_up_type);

	List<LookUpCode> findByTenantIdAndLookUpTypeAndModule(Long tenantId,
			String string, String string2);

	@Query(value="select * from look_up_code where tenant_id=:tenantId and look_up_type=:colDataType and meaning=:operator and ((enable_flag =1 AND (DATEDIFF(CURDATE(),active_start_date)) >= 0) "
			+ "and if(active_end_date is null, sysdate()+1,(DATEDIFF(active_end_date,CURDATE())) >= 0)) ORDER  BY ABS(secure_attribute_1) ASC", nativeQuery=true)
	LookUpCode findByMeaningAndLookUpTypeAndTenantId(@Param(value = "operator") String operator,@Param(value = "colDataType") String colDataType, @Param(value = "tenantId") Long tenantId);
	
	@Query(value="select * from look_up_code where tenant_id=:tenant_id and look_up_type=:look_up_type and ((enable_flag =1 AND (DATEDIFF(CURDATE(),active_start_date)) >= 0) and if(active_end_date is null, sysdate()+1,(DATEDIFF(active_end_date,CURDATE())) >= 0)) ORDER  BY ABS(secure_attribute_2) ASC", nativeQuery=true)
	List<LookUpCode> findByTenantAndLookUpTypeOrderBySecAttribute2(@Param(value="tenant_id") Long tenantId,@Param(value = "look_up_type") String look_up_type);
}
