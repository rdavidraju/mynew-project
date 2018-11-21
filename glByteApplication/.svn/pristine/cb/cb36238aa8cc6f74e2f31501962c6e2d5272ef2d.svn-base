package com.nspl.app.repository;

import com.nspl.app.domain.AccountingLineTypes;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

/**
 * Spring Data JPA repository for the AccountingLineTypes entity.
 */
@SuppressWarnings("unused")
public interface AccountingLineTypesRepository extends JpaRepository<AccountingLineTypes,Long> {

	List<AccountingLineTypes> findByRuleId(Long id);
	
	@Query(value =  "SELECT distinct data_view_id FROM t_accounting_line_types where rule_id in (select rule_id from t_rule_group_details where rule_group_id=:ruleGroupId) and tenant_id=:tenantId ",nativeQuery=true)
	List<BigInteger> fetchDataViewIdsByTenantIdAndRuleId(@Param("tenantId") Long tenantId,@Param("ruleGroupId") Long ruleGroupId);
	
	@Query(value =  "SELECT distinct rule_id FROM t_accounting_line_types where rule_id in (select rule_id from t_rule_group_details where rule_group_id=:ruleGroupId and tenant_id=:tenantId) and data_view_id = :viewId and tenant_id=:tenantId",nativeQuery=true)
	List<BigInteger> fetchRuleIdsByTenantIdAndRuleId(@Param("tenantId") Long tenantId,@Param("ruleGroupId") Long ruleGroupId, @Param("viewId") Long viewId);

	@Query(value =  "SELECT distinct amount_column_id FROM t_accounting_line_types where rule_id in (:ids)",nativeQuery=true)
	List<BigInteger> fetchAmoutColIdsByRuleIds(@Param ("ids") List<Long> ids);
	
}
