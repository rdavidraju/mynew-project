package com.nspl.app.repository;

import java.math.BigInteger;
import java.util.List;

import com.nspl.app.domain.FavouriteReports;

import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the FavouriteReports entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FavouriteReportsRepository extends JpaRepository<FavouriteReports,Long> {
    
	 FavouriteReports findByReportIdAndTenantIdAndUserId(Long rptId,Long userId,Long tenantId);
	 
	 @Query(value = "select report_id from t_favourite_reports where tenant_id=:tenantId and user_id=:userId order by id desc",nativeQuery=true)
		List<BigInteger> fectchCurrentUserFavouriteReportIds(@Param("userId") Long userId,@Param("tenantId") Long tenantId);
}
