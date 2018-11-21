package com.nspl.app.repository;

import java.util.List;

import com.nspl.app.domain.BalanceType;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the BalanceType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BalanceTypeRepository extends JpaRepository<BalanceType,Long> {
    
	@Query(value = "select column_name from information_schema.columns where table_name= :table_name and column_name like 'field_%'",nativeQuery=true)
	List<String> fetchBalancesTableColumns(@Param("table_name") String table_name);

	List<BalanceType> findByModuleAndSrcIdAndTenantIdAndRuleGroupIdAndRuleId(
			String processType, Long dataViewId, Long tenantId,
			Long ruleGroupId, Long ruleId);
}
