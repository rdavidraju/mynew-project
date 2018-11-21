package com.nspl.app.repository;

import com.nspl.app.domain.ApplicationPrograms;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

/**
 * Spring Data JPA repository for the ApplicationPrograms entity.
 */
@SuppressWarnings("unused")
public interface ApplicationProgramsRepository extends JpaRepository<ApplicationPrograms,Long> {

	List<ApplicationPrograms> findByTenantId(Long tenantId);

	@Query(value = "select distinct(prgm_name) FROM t_application_programs where tenant_id= :tenantId",nativeQuery=true)
	List<String> findByTenantIdAndDistinctPrgmName(@Param("tenantId")Long tenantId);

	@Query(value = "select id FROM t_application_programs where tenant_id= :tenantId and prgm_name= :prgmName",nativeQuery=true)
	List<BigInteger> findIdsByTenantIdAndPrgmName(@Param("tenantId")Long tenantId,@Param("prgmName") String prgmName);

	ApplicationPrograms findByPrgmNameAndTenantId(String paramType,
			Long tenantId);

	List<ApplicationPrograms> findByTenantIdAndEnableIsTrue(Long tenantId);

	ApplicationPrograms findByPrgmNameAndTenantIdAndEnableIsTrue(
			String progType, Long tenantId);

	ApplicationPrograms findByPrgmNameAndTenantId(Long tenantId, String prog);

	@Query(value = "select id FROM t_application_programs where tenant_id= :tenantId and prgm_name in (:prgmName)",nativeQuery=true)
	List<BigInteger> findByPrgmNameInAndTenantId(@Param("tenantId")Long tenantId,@Param("prgmName")List<String> prgmName);

	

}
