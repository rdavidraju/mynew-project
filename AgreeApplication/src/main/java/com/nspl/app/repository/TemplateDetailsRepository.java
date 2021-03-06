package com.nspl.app.repository;

import com.nspl.app.domain.TemplateDetails;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

/**
 * Spring Data JPA repository for the TemplateDetails entity.
 */
@SuppressWarnings("unused")
public interface TemplateDetailsRepository extends JpaRepository<TemplateDetails,Long> {
	
	@Query(value="select * from t_template_details where tenant_id= :tenantId and ((enabled_flag =1 AND (DATEDIFF(CURDATE(),start_date)) >= 0) and if(end_date is null, sysdate()+1, (DATEDIFF(end_date,CURDATE()) >= 0))) order by id desc", nativeQuery=true)
	List<TemplateDetails> fetchTempDetailsByTenantId(@Param(value = "tenantId") Long tenantId);

	List<TemplateDetails> findByTenantIdAndTemplateName(Long tenantId,
			String name);

	
	
	@Query(value="select id from t_template_details where tenant_id= :tenantId and enabled_flag =1 ", nativeQuery=true)
	List<BigInteger> fetchByTenantIdAndActive(@Param(value = "tenantId") Long tenantId);
	
	
	@Query(value="select template_name from t_template_details where id in (:tempIds) ", nativeQuery=true)
	List<String> fetchTemplateNamesByIds(@Param(value = "tempIds") List<BigInteger> tempIds);

	List<TemplateDetails> findByTenantIdOrderByIdDesc(Long tenantId);

	Page<TemplateDetails> findByTenantIdOrderByIdDesc(Long tenantId,
			Pageable generatePageRequest2);

	List<TemplateDetails> findByTenantIdAndViewId(Long tenantId, Long dataViewId);

	TemplateDetails findByTenantIdAndIdForDisplay(Long tenantId, String id);

	

}
