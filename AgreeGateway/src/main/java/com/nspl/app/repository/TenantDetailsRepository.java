package com.nspl.app.repository;

import java.util.List;

import com.nspl.app.domain.TenantDetails;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TenantDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TenantDetailsRepository extends JpaRepository<TenantDetails,Long> {

	/*List<TenantDetails> findByTenantName(String name);*/
	
	TenantDetails findByTenantName(String tenantName);

	TenantDetails findByDomainName(String domainName);
    
}
