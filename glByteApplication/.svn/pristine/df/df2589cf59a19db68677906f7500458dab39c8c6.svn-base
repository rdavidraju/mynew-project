package com.nspl.app.repository;

import com.nspl.app.domain.AcctRuleConditions;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

/**
 * Spring Data JPA repository for the AcctRuleConditions entity.
 */
@SuppressWarnings("unused")
public interface AcctRuleConditionsRepository extends JpaRepository<AcctRuleConditions,Long> {

	List<AcctRuleConditions> findByRuleActionId(Long id);
	
	//SELECT distinct(s_view_column_id) FROM agree_application_2909.t_acct_rule_conditions where rule_id in(102);
	
	@Query(value =  "SELECT distinct(s_view_column_id) FROM t_acct_rule_conditions where rule_id in(:ruleIds)",nativeQuery=true)
	List<BigInteger> fetchRuleIdsByTenantIdAndRuleId(@Param("ruleIds") List<BigInteger> ruleIds);

}
