package com.nspl.app.repository;

import com.nspl.app.domain.RuleGroupDetails;
import com.nspl.app.domain.Rules;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

/**
 * Spring Data JPA repository for the RuleGroupDetails entity.
 */
@SuppressWarnings("unused")
public interface RuleGroupDetailsRepository extends JpaRepository<RuleGroupDetails,Long> {

	List<RuleGroupDetails> findByRuleGroupId(Long groupId);

	
	@Query(value =  "SELECT rule_id FROM t_rule_group_details where rule_group_id in(:ruleGroupIds) ",nativeQuery=true)
	List<Long> fetchByRuleGroupIds(@Param("ruleGroupIds") List<Long> ruleGroupIds);
	
	@Query(value =  "SELECT id,rule_id FROM t_rule_group_details where rule_group_id in(:ruleGroupIds) ",nativeQuery=true)
	List<Object[]> fetchRuleIdsByRuleGroupIds(@Param("ruleGroupIds") List<Long> ruleGroupIds);
	
	
	@Query(value =  "SELECT * FROM t_rule_group_details where rule_group_id in(:ruleGroupIds) ",nativeQuery=true)
	List<RuleGroupDetails> fetchGrpDetailByRuleGroupIds(@Param("ruleGroupIds") List<Long> ruleGroupIds);



	RuleGroupDetails findByRuleGroupIdAndRuleId(Long id, Long ruleId);
	
	//shiva
	@Query(value =  "select rule_id FROM t_rule_group_details where tenant_id=:tenantId and rule_group_id=:ruleGroupId",nativeQuery=true)
	List<Long> fetchByRuleGroupIdAndTenantId(@Param("ruleGroupId") Long ruleGroupId, @Param("tenantId") Long tenantId);
	
	
	//shiva
		@Query(value =  "SELECT rule_id FROM t_rule_group_details where rule_group_id =:groupId and tenant_id =:tenantId ",nativeQuery=true)
		List<Long> fetchRuleIdsByGroupIdAndTenantId(@Param("groupId") Long groupId, @Param("tenantId") Long tenantId);
		//ravali
		@Query(value =  "SELECT rule_id FROM t_rule_group_details where rule_group_id =:groupId and tenant_id =:tenantId ",nativeQuery=true)
		List<BigInteger> fetchRuleIdsByGroupAndTenantId(@Param("groupId") Long groupId, @Param("tenantId") Long tenantId);
		
		
		//shiva
		@Query(value =  "select rule_id FROM t_rule_group_details where tenant_id=:tenantId and rule_group_id=:ruleGroupId",nativeQuery=true)
	List<BigInteger> fetchByRuleGroupIdAndTenantIds(@Param("ruleGroupId") Long ruleGroupId, @Param("tenantId") Long tenantId);

		
		List<RuleGroupDetails> findByRuleId(Long long1);


		List<RuleGroupDetails> findByRuleGroupIdOrderByPriorityAsc(Long groupId);
		
		@Query(value =  "select max(priority) FROM t_rule_group_details where rule_group_id=:ruleGroupId",nativeQuery=true)
		int getLatestPriority(@Param("ruleGroupId") Long ruleGroupId);

		@Query(value =  "select * FROM t_rules where id in (select rule_id from t_rule_group_details where rule_group_id in(:ids))",nativeQuery=true)
		List<Rules> fetchRulesByRuleGroupIdIn(@Param("ids") List<Long> ids);

		@Query(value =  "select count(*) FROM t_rule_group_details where rule_group_id=:id",nativeQuery=true)
		int findRuleCountByGroupId(@Param("id") Long id); 
		
}
