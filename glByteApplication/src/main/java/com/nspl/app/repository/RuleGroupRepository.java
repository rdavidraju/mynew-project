package com.nspl.app.repository;

import com.nspl.app.domain.RuleGroup;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

/**
 * Spring Data JPA repository for the RuleGroup entity.
 */
@SuppressWarnings("unused")
public interface RuleGroupRepository extends JpaRepository<RuleGroup,Long> {

	List<RuleGroup> findByTenantIdOrderByIdDesc(Long tenantId);

	Page<RuleGroup> findByTenantIdOrderByIdDesc(Long tenantId, Pageable generatePageRequest2);

	
	@Query(value =  "SELECT ID FROM t_rule_group where rule_purpose= :rulePurpose and tenant_id=:tenantId and enabled_flag is true",nativeQuery=true)
	List<Long> fetchIdsByTenantIdAndRulePurpose(@Param("tenantId") Long tenantId,@Param("rulePurpose") String rulePurpose);
	
	@Query(value =  "SELECT ID FROM t_rule_group where rule_purpose= :rulePurpose and tenant_id=:tenantId and enabled_flag is true and activity_based is true",nativeQuery=true)
	List<Long> fetchIdsByTenantIdAndRulePurposeAndActivityBasedIsTrue(@Param("tenantId") Long tenantId,@Param("rulePurpose") String rulePurpose);
	
	@Query(value =  "SELECT ID FROM t_rule_group where rule_purpose= :rulePurpose and tenant_id=:tenantId and enabled_flag is true and( activity_based is null or activity_based is false)",nativeQuery=true)
	List<Long> fetchIdsByTenantIdAndRulePurposeAndActivityBasedIsNullOrFalse(@Param("tenantId") Long tenantId,@Param("rulePurpose") String rulePurpose);
	
	@Query(value =  "SELECT ID FROM t_rule_group where rule_purpose= :rulePurpose and tenant_id=:tenantId order by name asc",nativeQuery=true)
	List<BigInteger> fetchRuleGrpIdsByTenantIdAndRulePurpose(@Param("tenantId") Long tenantId,@Param("rulePurpose") String rulePurpose);
	
	RuleGroup findByTenantIdAndName(Long tenanatId, String groupName);
		
	//shiva
	RuleGroup findByTenantIdAndNameAndRulePurpose(Long tenanatId, String groupName, String rulePurpose);
	
	RuleGroup findByTenantIdAndIdAndRulePurpose(Long tenanatId, Long ruleId, String rulePurpose);

	//for orphan rules
	List<RuleGroup> findByTenantIdAndRulePurpose(Long tenantId, String purpose);
	
	@Query(value =  "SELECT * FROM t_rule_group where rule_purpose= :rulePurpose and tenant_id=:tenantId order by creation_date desc , name asc ",nativeQuery=true)
	List<RuleGroup> fetchByTenantIdAndRulePurpose(@Param("tenantId") Long tenantId,@Param("rulePurpose") String rulePurpose);

	@Query(value =  "SELECT * FROM t_rule_group where tenant_id = :tenantId and name = :name",nativeQuery=true)
	List<RuleGroup> fetchByTenantIdAndName(@Param("tenantId") Long tenantId, @Param("name") String name);
	
	RuleGroup findByIdAndNameAndTenantId(Long id, String name, Long tenantId);
	
	@Query(value = "select distinct(rule_purpose) FROM t_rule_group where tenant_id= :tenantId",nativeQuery=true)
	List<String> findDistRulePurposeByTenantId(@Param("tenantId") Long tenantId);

	List<RuleGroup> findByTenantIdAndRulePurposeOrderByIdDesc(Long tenantId, String rulePurpose);

	Page<RuleGroup> findByTenantIdAndRulePurposeOrderByIdDesc(Long tenantId, String rulePurpose,
			Pageable generatePageRequest2);
	
	@Query(value = "SELECT * FROM t_rule_group where tenant_id=:tenantId and rule_purpose= :rulePurpose and id  not in (SELECT appr_rule_grp_id FROM t_rule_group"
			+ "  where rule_purpose = 'APPROVALS' and appr_rule_grp_id is not null);",nativeQuery=true)
	List<RuleGroup> findUnTaggedRuleGroups(@Param("tenantId") Long tenantId, @Param("rulePurpose")String rulePurpose);

	
	
	@Query(value = "select id_for_display,name FROM t_rule_group where id in (SELECT distinct rule_group_id FROM t_rule_group_details where rule_id in (SELECT id FROM t_rules where source_data_view_id = :dvId)) and rule_purpose= :purpose",nativeQuery=true)
	List<Object[]> findRuleGrpNamesByDataViewIdAndPurPose(@Param("dvId") Long dvId,
			@Param("purpose") String purpose);

	List<RuleGroup> findByTenantIdAndRulePurposeAndReconciliationGroupId(Long tenantId, String string, Long groupId);
	
	@Query(value = "select * FROM t_rule_group where appr_rule_grp_id =:id ",nativeQuery=true)
	RuleGroup findTaggedGroup(@Param("id") Long id);
	
	// kiran
	Page<RuleGroup> findByTenantId(Long tenantId, Pageable generatePageRequest2);
	
	Page<RuleGroup> findByTenantIdAndRulePurpose(Long tenantId, String rulePurpose, Pageable generatePageRequest2);

	RuleGroup findByIdAndTenantId(Long groupId, Long tenantId);

	RuleGroup findByIdForDisplayAndTenantId(String groupId, Long tenantId);

	List<RuleGroup> findByApprRuleGrpId(Long ruleGroupId);
	
	@Query(value = "select source_data_view_id FROM t_rules where id in ( select rule_id from t_rule_group_details where rule_group_id = (select id from t_rule_group where id_for_display=:id))",nativeQuery=true)
	List<BigInteger> fetchAccViewIdsByRuleGrpId(@Param("id") String id);
	
}
