package com.nspl.app.repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nspl.app.domain.RuleConditions;

/**
 * Spring Data JPA repository for the RuleConditions entity.
 */
@SuppressWarnings("unused")
public interface RuleConditionsRepository extends JpaRepository<RuleConditions,Long> {

	List<RuleConditions> findByRuleId(Long id);
	
	//Source
	@Query(value =  "SELECT distinct(s_column_id) FROM t_rule_conditions where rule_id in(:ruleIds)",nativeQuery=true)
	List<BigInteger> fetchSourceColumnIdsByRuleIds(@Param("ruleIds") List<BigInteger> ruleIds);
	
	//Source
	// Author Bhagath
	@Query(value =  "SELECT distinct(s_column_id) FROM t_rule_conditions where rule_id = :ruleId",nativeQuery=true)
	List<BigInteger> fetchSourceColumnIdsByRuleId(@Param("ruleId") Long ruleId);
	
	//Target
	@Query(value =  "SELECT distinct(t_column_id) FROM t_rule_conditions where rule_id in(:ruleIds)",nativeQuery=true)
	List<BigInteger> fetchTargetColumnIdsByRuleIds(@Param("ruleIds") List<BigInteger> ruleIds);
	
	//Target
	// Author Bhagath
	@Query(value =  "SELECT distinct(t_column_id) FROM t_rule_conditions where rule_id = :ruleId",nativeQuery=true)
	List<BigInteger> fetchTargetColumnIdsByRuleId(@Param("ruleId") Long ruleId);
	
	// Author Rk POC Purpose
//	List<RuleConditions> findByRuleId(Long id);

}
