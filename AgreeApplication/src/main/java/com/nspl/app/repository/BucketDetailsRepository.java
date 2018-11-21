package com.nspl.app.repository;

import java.util.List;

import com.nspl.app.domain.BucketDetails;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the BucketDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BucketDetailsRepository extends JpaRepository<BucketDetails,Long> {
    
	List<BucketDetails> findByBucketId(Long bucketId);
}
