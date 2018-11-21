package com.nspl.app.repository;

import java.util.List;

import com.nspl.app.domain.TenantConfig;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TenantConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TenantConfigRepository extends JpaRepository<TenantConfig,Long> {
 
	List<TenantConfig> findByTenantId(Long tenantId);

	List<TenantConfig> findByTenantIdAndMeaning(Long tenantId, String string);

	TenantConfig findByTenantIdAndKeyAndMeaning(Long tenantId, String string,
			String string2);


	TenantConfig findByTenantIdAndKey(Long tenantId, String string);

}
