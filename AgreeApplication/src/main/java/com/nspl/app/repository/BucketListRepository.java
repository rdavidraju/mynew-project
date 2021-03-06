package com.nspl.app.repository;

import java.util.List;

import com.nspl.app.domain.BucketList;

import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the BucketList entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BucketListRepository extends JpaRepository<BucketList,Long> {
    
	@Query(value = "select count(*) from t_bucket_list where tenant_id= :tenantId and bucket_name= :bucket_name",nativeQuery=true)
	Long fetchBucketNameCount(@Param("tenantId") Long tenantId, @Param("bucket_name") String bucket_name);
	
	BucketList findByTenantIdAndBucketName(Long tenantId, String bucketName);
	
	Page<BucketList> findByTenantIdAndEnabledFlagTrueOrderByIdDesc(Long tenantId,Pageable pageable);
	
	Page<BucketList> findByTenantIdAndTypeAndEnabledFlagTrueOrderByIdDesc(Long tenantId,String bucketType,Pageable pageable);
	
	List<BucketList> findByTenantIdOrderByIdDesc(Long tenantId);
}
