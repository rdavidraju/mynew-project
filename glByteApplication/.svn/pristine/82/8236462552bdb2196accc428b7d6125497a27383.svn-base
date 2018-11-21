package com.nspl.app.repository;

import com.nspl.app.domain.AppRuleConditions;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

/**
 * Spring Data JPA repository for the AppRuleConditions entity.
 */
@SuppressWarnings("unused")
public interface AppRuleConditionsRepository extends JpaRepository<AppRuleConditions,Long> {

	List<AppRuleConditions> findByRuleId(Long id);

	
	@Query(value =  "SELECT distinct(column_id) FROM t_app_rule_conditions where rule_id in(:ruleIds)",nativeQuery=true)
	List<BigInteger> fetchColumnIdsByRuleIds(@Param("ruleIds") List<BigInteger> ids);

}
