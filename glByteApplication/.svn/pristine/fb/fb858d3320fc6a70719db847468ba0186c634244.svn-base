package com.nspl.app.repository;

import java.time.LocalDate;
import java.util.List;

import com.nspl.app.domain.FileTemplates;
import com.nspl.app.domain.FormConfig;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the FormConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormConfigRepository extends JpaRepository<FormConfig,Long> {
 
	FormConfig findByTenantId(Long tenantId);

	FormConfig findByFormConfig(String formConfig);
	
	
	FormConfig findByTenantIdAndFormConfigAndFormLevel(Long tenantId, String formConfig, String formLevel);

	FormConfig findByUserIdAndFormConfigAndFormLevel(Long userId, String formConfig, String formLevel);

	FormConfig findByUserIdAndTenantIdAndFormConfigAndFormLevel(Long userId, Long tenantId,String formConfig, String formLevel);
	
	
//	@Query(value =  "select * from t_form_config where tenant_id=:tenantId and form_config=:formConfig and form_level=:formLevel",nativeQuery=true)
//	FormConfig fetchByTenantIdAndFormConfigAndFormLevel(@Param("tenantId") Long tenantId, @Param("formConfig") String formConfig, @Param("formLevel") String formLevel);

	
	
	
}
