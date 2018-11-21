package com.nspl.app.repository;

import java.math.BigInteger;
import java.util.List;

import com.nspl.app.domain.ReconciliationDuplicateResult;
import com.nspl.app.domain.ReconciliationResult;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the ReconciliationDuplicateResult entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReconciliationDuplicateResultRepository extends JpaRepository<ReconciliationDuplicateResult,Long> {
    
	
	@Query(value =  "SELECT distinct(reconciliation_rule_id) FROM t_reconciliation_suggestion_result where tenant_id = :tenantId and reconciliation_rule_group_id = :groupId and original_view_id = :viewId and recon_status = 'RECONCILED' AND current_record_flag is true", nativeQuery=true)
	List<BigInteger> fetchRuleIdsByTenantIdAndGroupIdAndViewId(@Param("tenantId") Long tenantId, @Param("groupId") Long groupId, @Param("viewId") Long viewId);
		
	@Query(value =  "SELECT original_row_id FROM t_reconciliation_suggestion_result where tenant_id =:tenantId and reconciliation_rule_id in (:ruleIds) and reconciliation_rule_group_id = :groupId and original_row_id in(:totalIds) and original_view_id = :viewId and recon_status = 'RECONCILED' and current_record_flag is true",nativeQuery=true)
	List<BigInteger> fetchSuggestionIdsWithRulesForSource(@Param("tenantId") Long tenantId, @Param("ruleIds") List<Long> ruleIds, @Param("groupId") Long groupId, @Param("totalIds") List<BigInteger> totalIds, @Param("viewId") Long viewId);

	@Query(value =  "SELECT target_row_id FROM t_reconciliation_suggestion_result where tenant_id =:tenantId and reconciliation_rule_id in (:ruleIds) and reconciliation_rule_group_id = :groupId and target_row_id in(:totalIds) and target_view_id = :viewId and recon_status = 'RECONCILED' and current_record_flag is true",nativeQuery=true)
	List<BigInteger> fetchSuggestionIdsWithRulesForTarget(@Param("tenantId") Long tenantId, @Param("ruleIds") List<Long> ruleIds, @Param("groupId") Long groupId, @Param("totalIds") List<BigInteger> totalIds, @Param("viewId") Long viewId);

	@Query(value =  "SELECT distinct(recon_job_reference) from t_reconciliation_suggestion_result where reconciliation_rule_group_id = :groupId AND tenant_id= :tenantId and original_view_id = :viewId and recon_status = 'RECONCILED' AND current_record_flag is true",nativeQuery=true)
	List<String> fetchDistinctbatchesByGroupNView(@Param("groupId") Long groupId, @Param("tenantId") Long tenantId, @Param("viewId") Long viewId);
		
	@Query(value =  "SELECT original_row_id FROM t_reconciliation_suggestion_result where tenant_id =:tenantId and reconciliation_rule_group_id =:groupId and original_row_id in(:totalIds) and recon_job_reference = :jobReference and original_view_id = :viewId and recon_status = 'RECONCILED' and current_record_flag is true",nativeQuery=true)
	List<BigInteger> fetchReconciledIdswithJobReferenceForSource(@Param("tenantId") Long tenantId, @Param("groupId") Long groupId, @Param("totalIds") List<BigInteger> totalIds, @Param("jobReference") String jobReference, @Param("viewId") Long viewId);
	
	@Query(value =  "SELECT target_row_id FROM t_reconciliation_suggestion_result where tenant_id =:tenantId and reconciliation_rule_group_id =:groupId and target_row_id in(:totalIds) and recon_job_reference = :jobReference and target_view_id = :viewId and recon_status = 'RECONCILED' and current_record_flag is true",nativeQuery=true)
	List<BigInteger> fetchReconciledIdswithJobReferenceForTarget(@Param("tenantId") Long tenantId, @Param("groupId") Long groupId, @Param("totalIds") List<BigInteger> totalIds, @Param("jobReference") String jobReference, @Param("viewId") Long viewId);

	@Query(value =  "SELECT original_row_id FROM t_reconciliation_suggestion_result where tenant_id =:tenantId and reconciliation_rule_group_id =:groupId and original_row_id in(:totalIds) and original_view_id = :viewId and recon_status = 'RECONCILED' and current_record_flag is true order by recon_reference asc",nativeQuery=true)
	List<BigInteger> fetchReconciledIdswithStatusUnreconciledForSource(@Param("tenantId") Long tenantId, @Param("groupId") Long groupId, @Param("totalIds") List<BigInteger> totalIds, @Param("viewId") Long viewId);
	
	@Query(value =  "SELECT original_row_id FROM t_reconciliation_suggestion_result where tenant_id =:tenantId and reconciliation_rule_id in (:ruleIds) and reconciliation_rule_group_id = :groupId and original_row_id in(:totalIds) and original_view_id = :viewId and recon_status = 'RECONCILED' and current_record_flag is true order by recon_reference asc",nativeQuery=true)
	List<BigInteger> fetchReconciledIdsWithOutJobRefWithStatusReconciledForSource(@Param("tenantId") Long tenantId, @Param("ruleIds") List<Long> ruleIds, @Param("groupId") Long groupId, @Param("totalIds") List<BigInteger> totalIds, @Param("viewId") Long viewId);

	@Query(value =  "SELECT distinct(reconciliation_rule_id) FROM t_reconciliation_suggestion_result where tenant_id =:tenantId and reconciliation_rule_group_id = :groupId and original_view_id=:viewId and recon_status = 'RECONCILED' AND current_record_flag is true", nativeQuery=true)
	List<BigInteger> fetchRuleIdsByGroupIdAndTenantId(@Param("tenantId") Long tenantId, @Param("groupId") Long groupId, @Param("viewId") Long viewId);

	@Query(value =  "SELECT original_row_id FROM t_reconciliation_suggestion_result where tenant_id =:tenantId and reconciliation_rule_group_id =:groupId and original_row_id in(:totalIds) and recon_job_reference in (:jobReference) and original_view_id = :viewId and recon_status = 'RECONCILED' and current_record_flag is true order by recon_reference asc",nativeQuery=true)
	List<BigInteger> fetchReconciledIdswittBatchesForSource(@Param("tenantId") Long tenantId, @Param("groupId") Long groupId, @Param("totalIds") List<BigInteger> totalIds, @Param("jobReference") List<String> jobReference, @Param("viewId") Long viewId);

	@Query(value =  "SELECT target_row_id FROM t_reconciliation_suggestion_result where tenant_id =:tenantId and reconciliation_rule_group_id =:groupId and target_row_id in(:totalIds) and recon_job_reference in (:jobReferences) and target_view_id = :viewId and recon_status = 'RECONCILED' and current_record_flag is true order by recon_reference asc",nativeQuery=true)
	List<BigInteger> fetchReconciledIdswithJobReferencesForTarget(@Param("tenantId") Long tenantId, @Param("groupId") Long groupId, @Param("totalIds") List<BigInteger> totalIds, @Param("jobReferences") List<String> jobReferences, @Param("viewId") Long viewId);

	@Query(value =  "SELECT target_row_id FROM t_reconciliation_suggestion_result where tenant_id =:tenantId and reconciliation_rule_group_id = :groupId and target_row_id in(:totalIds) and target_view_id = :viewId and recon_status ='RECONCILED' and current_record_flag is true order by recon_reference asc",nativeQuery=true)
	List<BigInteger> fetchReconciledIdswithStatusUnreconciledForTarget(@Param("tenantId") Long tenantId, @Param("groupId") Long groupId, @Param("totalIds") List<BigInteger> totalIds, @Param("viewId") Long viewId);

	@Query(value =  "SELECT distinct(recon_job_reference) from t_reconciliation_suggestion_result where reconciliation_rule_group_id = :groupId AND tenant_id= :tenantId and target_view_id = :viewId and recon_status = 'RECONCILED' AND current_record_flag is true",nativeQuery=true)
	List<String> fetchDistinctbatchesTarget(@Param("groupId") Long groupId, @Param("tenantId") Long tenantId, @Param("viewId") Long viewId);

	@Query(value =  "SELECT target_row_id FROM t_reconciliation_suggestion_result where tenant_id =:tenantId and reconciliation_rule_id in (:ruleIds) and reconciliation_rule_group_id = :groupId and target_row_id in(:totalIds) and target_view_id = :viewId and recon_status = 'RECONCILED' and current_record_flag is true order by recon_reference asc",nativeQuery=true)
	List<BigInteger> fetchReconciledIdsWithOutJobRefWithStatusReconciledForTarget(@Param("tenantId") Long tenantId, @Param("ruleIds") List<Long> ruleIds, @Param("groupId") Long groupId, @Param("totalIds") List<BigInteger> totalIds, @Param("viewId") Long viewId);

	@Query(value =  "SELECT distinct(reconciliation_rule_id) FROM t_reconciliation_suggestion_result where tenant_id =:tenantId and reconciliation_rule_group_id = :groupId and target_view_id=:viewId and recon_status = 'RECONCILED' AND current_record_flag is true", nativeQuery=true)
	List<BigInteger> fetchRuleIdsByGroupIdAndTenantIdForTarget(@Param("tenantId") Long tenantId, @Param("groupId") Long groupId, @Param("viewId") Long viewId);

	List<ReconciliationDuplicateResult> findByReconReferenceIn(List<String> reconReference);
	
	@Query(value =  "select * from t_reconciliation_suggestion_result where original_row_id = :originalRowId and tenant_id = :tenantId and original_view_id = :originalViewId and reconciliation_rule_group_id = :groupId and recon_status = 'RECONCILED' and current_record_flag is true",nativeQuery=true)
	List<ReconciliationDuplicateResult> fetchSourceUniqueRecord(@Param("originalRowId") Long originalRowId, @Param("tenantId") Long tenantId, @Param("originalViewId") Long originalViewId, @Param("groupId") Long groupId);

	@Query(value =  "select * from t_reconciliation_suggestion_result where target_row_id = :targetRowId and tenant_id = :tenantId and target_view_id = :targetViewId and reconciliation_rule_group_id = :groupId and recon_status = 'RECONCILED' and current_record_flag is true;",nativeQuery=true)
	List<ReconciliationDuplicateResult> fetchTargetUniqueRecord(@Param("targetRowId") Long targetRowId, @Param("tenantId") Long tenantId, @Param("targetViewId") Long targetViewId, @Param("groupId") Long groupId);
	
	@Query(value =  "SELECT * from t_reconciliation_suggestion_result where reconciliation_rule_group_id = :groupId AND tenant_id= :tenantId and (original_row_id = :originalRowId || target_row_id = :originalRowId)  and recon_status = 'RECONCILED' and current_record_flag is true",nativeQuery=true)
	List<ReconciliationDuplicateResult> fetchRecRefByRowIdAndReconciliationRuleGroupId(@Param("tenantId") Long tenantId, @Param("groupId") Long groupId, @Param("originalRowId") Long originalRowId);
	
	@Query(value =  "SELECT target_row_id from t_reconciliation_suggestion_result where recon_reference in (:reconReferenceIds) and target_view_id = :targetViewId",nativeQuery=true)
	List<BigInteger> fetchByTargetIdsByReconReferences(@Param("reconReference") List<String> reconReferenceIds, @Param("targetViewId") Long targetViewId);

	@Query(value =  "SELECT distinct(original_row_id) from t_reconciliation_suggestion_result where recon_reference in(:reconReferenceIds) and original_view_id = :sourceViewId and target_view_id is null and recon_status = 'RECONCILED' and current_record_flag is true",nativeQuery=true)
	List<BigInteger> fetchByReconSourceRefIdsAndTenantId(@Param("reconReferenceIds") List<String> reconReferenceIds , @Param("sourceViewId") Long sourceViewId);

	
	@Query(value =  "SELECT distinct(target_row_id) from t_reconciliation_suggestion_result where recon_reference in(:reconReferenceIds) and target_view_id = :targetViewId and original_view_id is null and sign(target_row_id)=1 ",nativeQuery=true)
	List<BigInteger> fetchByTargetReconRefIdsAndTenantId(@Param("reconReferenceIds") List<String> reconReferenceIds, @Param("targetViewId") Long targetViewId);

}
