package com.nspl.app.repository;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nspl.app.domain.AccountingData;

/**
 * Spring Data JPA repository for the AccountingData entity.
 */
@SuppressWarnings("unused")
public interface AccountingDataRepository extends JpaRepository<AccountingData,Long> {
		
	AccountingData findByTenantIdAndOriginalRowIdAndLineTypeAndOriginalViewIdAndAcctGroupIdAndReverseRefIdIsNull(Long tenantId, Long originalRowId, String lineType, Long viewId, Long groupId);

	@Query(value =  "SELECT * from t_accounting_data where tenant_id =:tenantId and original_row_id in(:orignalRowIds) and original_view_id = :dataViewId and acct_group_id = :groupId and acct_rule_id = :ruleId",nativeQuery=true)
	List<AccountingData> fetchRecordsByRuleId(@Param("orignalRowIds") List<BigInteger> orignalRowIds, @Param("tenantId") Long tenantId, @Param("dataViewId") Long dataViewId, @Param("groupId") Long groupId, @Param("ruleId") Long ruleId);
	
	@Query(value =  "SELECT distinct(job_reference) FROM t_accounting_data where tenant_id=:tenantId and acct_group_id = :groupId",nativeQuery=true)
	List<String> fetchDistinctBatches(@Param("tenantId") Long tenantId, @Param("groupId") Long groupId);	
	
	@Query(value =  "SELECT distinct original_row_id FROM t_accounting_data where id in (:ids)",nativeQuery=true)
	List<BigInteger> fetchByIds(@Param("ids") List<Long> ids);

	@Query(value =  "SELECT distinct(coa_ref) from t_accounting_data where tenant_id =:tenantId and original_row_id in(:orignalRowIds) and acct_group_id = :groupId",nativeQuery=true)
	List<Long> findDistinctCoaRefByTenantIdAndOriginalRowIdInAndAcctGroupId(@Param("tenantId") Long tenantId,@Param("orignalRowIds") List<Long> orignalRowIds,@Param("groupId") Long groupId);

	@Query(value =  "SELECT distinct(coa_ref) from t_accounting_data where tenant_id =:tenantId and original_row_id in(:orignalRowIds) and acct_group_id = :groupId and original_view_id =:viewId",nativeQuery=true)
	List<Long> findDistinctCoaRefByTenantIdAndOriginalRowIdInAndAcctGroupIdAndViewId(@Param("tenantId") Long tenantId,@Param("orignalRowIds") List<BigInteger> orignalRowIds,@Param("groupId") Long groupId,@Param("viewId") Long viewId);
	
	@Query(value =  "SELECT distinct(original_row_id) FROM t_accounting_data where acct_group_id = :groupId and original_view_id =:viewId and final_status=:finalStatus",nativeQuery=true)
	List<BigInteger> fetchDistinctApprovedInProcessRowIds( @Param("groupId") Long groupId, @Param("viewId") Long viewId,@Param("finalStatus") String finalStatus);

	@Query(value =  "SELECT original_view_id,acct_rule_id FROM t_accounting_data where acct_group_id = :groupId and id in (:recIdsList) group by original_view_id,acct_rule_id",nativeQuery=true)
	List<Object[]> fetchDistinctRuleIdAndViewIdByRuleGroupId(@Param("groupId") Long groupId, @Param("recIdsList") List<Long> recIdsList);

	@Query(value =  "select distinct coa_ref from t_accounting_data where original_view_id=:viewId",nativeQuery=true)
	List<String> findCoaForViewId(@Param("viewId") Long viewId);

	List<AccountingData> findByTenantIdAndAccountedSummaryId(Long tenantId, Long id);

	@Query(value =  "select distinct accounted_summary_id from t_accounting_data where id in (:idList)",nativeQuery=true)
	List<BigInteger> findAccountedSummaryIdByIdIn(@Param("idList") List<Long> idList);

	List<AccountingData> findByApprovalBatchId(Long id);

}
