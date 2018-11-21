package com.nspl.app.repository;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nspl.app.domain.ReconciliationResult;

/**
 * Spring Data JPA repository for the ReconciliationResult entity.
 */
@SuppressWarnings("unused")
public interface ReconciliationResultRepository extends JpaRepository<ReconciliationResult,Long> {
	
	//shiva get max recon reference id
	@Query(value =  "SELECT max(CAST(recon_reference as unsigned integer)) from t_reconciliation_result",nativeQuery=true)
	Long fetchMaxReconReference();

	@Query(value =  "SELECT * from t_reconciliation_result where recon_reference in(:reconReferenceIds) and (final_status is null or final_status = 'REJECTED')",nativeQuery=true)
	List<ReconciliationResult> fetchRecordsByReconReferenceIds(@Param("reconReferenceIds") List<String> reconReferenceIds);
	
	@Query(value =  "SELECT * from t_reconciliation_result where recon_reference in(:reconReferenceIds) and final_status in('IN_PROCESS', 'APPROVED')",nativeQuery=true)
	List<ReconciliationResult> fetchRecordsByApprovalReconReferenceIds(@Param("reconReferenceIds") List<String> reconReferenceIds);

	@Query(value =  "select * from t_reconciliation_result where original_row_id = :originalRowId and tenant_id = :tenantId and original_view_id = :originalViewId and reconciliation_rule_group_id = :groupId and recon_status = 'RECONCILED' and current_record_flag is true",nativeQuery=true)
	ReconciliationResult fetchSourceUniqueRecord(@Param("originalRowId") Long originalRowId, @Param("tenantId") Long tenantId, @Param("originalViewId") Long originalViewId, @Param("groupId") Long groupId);
	
	@Query(value =  "select * from t_reconciliation_result where target_row_id = :targetRowId and tenant_id = :tenantId and target_view_id = :targetViewId and reconciliation_rule_group_id = :groupId and recon_status = 'RECONCILED' and current_record_flag is true;",nativeQuery=true)
	ReconciliationResult fetchTargetUniqueRecord(@Param("targetRowId") Long targetRowId, @Param("tenantId") Long tenantId, @Param("targetViewId") Long targetViewId, @Param("groupId") Long groupId);
	
	List<ReconciliationResult> findByApprovalBatchId(Long id);

	@Query(value =  "SELECT original_row_id FROM t_reconciliation_result  where reconciliation_rule_group_id =:groupId AND tenant_id =:tenantId and original_view_id =:viewId  and original_row_id in (:srcIds)",nativeQuery=true)
	List<BigInteger> fetchOrginalRowIds(@Param("tenantId") Long tenantId,@Param("groupId") Long groupId, @Param("viewId") Long viewId, @Param("srcIds") List<BigInteger> srcIds);

	//ravali to get reconciled srcIds for a rule group
	@Query(value =  "SELECT distinct original_row_id FROM t_reconciliation_result  where reconciliation_rule_group_id =:groupId and original_view_id = :viewId and current_record_flag is true and recon_status='reconciled' and original_row_id is not null",nativeQuery=true)
	List<BigInteger> fetchOrginalRowIdsByRuleGroupId(@Param("groupId") Long groupId,@Param("viewId") Long viewId);
		
	//ravali to get reconciled srcIds for a rule group
	@Query(value =  "SELECT distinct target_row_id FROM t_reconciliation_result  where reconciliation_rule_group_id =:groupId and target_view_id = :viewId and current_record_flag is true and recon_status='reconciled' and target_row_id is not null ",nativeQuery=true)
	List<BigInteger> fetchTargetRowIdsByRuleGroupId(@Param("groupId") Long groupId,@Param("viewId") Long viewId);

	//Reconcile
	@Query(value =  "SELECT"+
        " DATEDIFF( r.reconciled_date,`v`.`file_date`) as `rule_age`, count(`v`.`id`), concat((DATEDIFF( r.reconciled_date,`v`.`file_date`)-1),'-' ,DATEDIFF( r.reconciled_date,`v`.`file_date`)) as "+
        "period, coalesce (sum(r.final_status = 'APPROVED'),0) as Approved FROM"+
        " (SELECT * FROM t_reconciliation_result  where reconciliation_rule_group_id = :groupId AND tenant_id= :tenantId and original_view_id =:viewId  and original_row_id in (select id from t_data_master where tenant_id =:tenantId and file_date between :fmDate and :toDate))r"+
		" JOIN t_data_master `v`"+
        " where ( `r`.`original_row_id` = `v`.`id` )   group by rule_age, period",nativeQuery=true)
	List<Object[]> fetchAgeAnalysisForSrcReconciliation(@Param("tenantId") Long tenantId,@Param("groupId") Long groupId, @Param("viewId") Long viewId,@Param("fmDate") String fmDate,@Param("toDate") String toDate);
		
	@Query(value =  "SELECT"+
		" DATEDIFF( r.reconciled_date,`v`.`file_date`) as `rule_age`, count(`v`.`id`), concat((DATEDIFF( r.reconciled_date,`v`.`file_date`)-1),'-' ,DATEDIFF( r.reconciled_date,`v`.`file_date`)) as "+
		"period, coalesce (sum(r.final_status = 'APPROVED'),0) as Approved FROM"+
        " (SELECT * FROM t_reconciliation_result  where reconciliation_rule_group_id = :groupId AND tenant_id= :tenantId and target_view_id =:viewId  and target_row_id in (select id from t_data_master where tenant_id =:tenantId and file_date between :fmDate and :toDate))r"+
        " JOIN t_data_master `v`"+
        " where ( `r`.`target_row_id` = `v`.`id` )   group by rule_age, period",nativeQuery=true)
	List<Object[]> fetchAgeAnalysisForTrgReconciliation(@Param("tenantId") Long tenantId,@Param("groupId") Long groupId, @Param("viewId") Long viewId,@Param("fmDate") String fmDate,@Param("toDate") String toDate);

	@Query(value =  "SELECT distinct(original_row_id) FROM t_reconciliation_result  where reconciliation_rule_group_id = :groupId AND tenant_id = :tenantId and original_view_id = :viewId and target_view_id is null",nativeQuery=true)
	List<BigInteger> fetchReconciledSourceIds(@Param("tenantId") Long tenantId,@Param("groupId") Long groupId, @Param("viewId") Long viewId);

	@Query(value =  "SELECT distinct(original_row_id) FROM t_reconciliation_result  where reconciliation_rule_group_id = :groupId AND tenant_id = :tenantId and original_view_id = :viewId and target_view_id is null and final_status='APPROVED'",nativeQuery=true)
	List<BigInteger> fetchApprovedSourceIds(@Param("tenantId") Long tenantId,@Param("groupId") Long groupId, @Param("viewId") Long viewId);

	@Query(value =  "SELECT distinct(target_row_id) FROM t_reconciliation_result where reconciliation_rule_group_id = :groupId AND tenant_id = :tenantId and target_view_id = :viewId and original_view_id is null",nativeQuery=true)
	List<BigInteger> fetchReconciledTargetIds(@Param("tenantId") Long tenantId, @Param("groupId") Long groupId, @Param("viewId") Long viewId);
		
		
	@Query(value =  "SELECT distinct(target_row_id) FROM t_reconciliation_result where reconciliation_rule_group_id = :groupId AND tenant_id = :tenantId and target_view_id = :viewId and original_view_id is null and final_status='APPROVED'",nativeQuery=true)
	List<BigInteger> fetchApprovedTargetIds(@Param("tenantId") Long tenantId, @Param("groupId") Long groupId, @Param("viewId") Long viewId);

	@Query(value =  "SELECT original_view_id,target_view_id,reconciliation_rule_id FROM t_reconciliation_result  where reconciliation_rule_group_id = :groupId and id in (:recIdsList) group by original_view_id,target_view_id,reconciliation_rule_id",nativeQuery=true)
	List<Object[]> fetchDistinctRuleIdAndViewIdByRuleGroupId(@Param("groupId") Long groupId,@Param("recIdsList") List<Long> recIdsList);

	@Query(value =  "SELECT count(*) FROM t_reconciliation_result  where reconciliation_rule_group_id = :groupId and reconciliation_rule_id=:ruleId and recon_status='RECONCILED' and current_record_flag is true ",nativeQuery=true)
	Object fetchRecordCountByGroupIdAndRuleId(@Param("groupId") Long groupId,@Param("ruleId") Long ruleId);
		
		/**dashboard v8**/
	@Query(value =  "SELECT distinct(original_view_id) FROM t_reconciliation_result where recon_reference in (:reconRef) and original_view_id is not null",nativeQuery=true)
	List<BigInteger> findOrginalViewIdByReconReference(@Param("reconRef") List<String> reconRef);
		
	@Query(value =  "SELECT distinct(target_view_id) FROM t_reconciliation_result where recon_reference in (:reconRef) and target_view_id is not null",nativeQuery=true)
	List<BigInteger> findTargetViewIdByReconReference(@Param("reconRef") List<String> reconRef);
		
	@Query(value =  "SELECT original_row_id FROM t_reconciliation_result where recon_reference in (:reconRef) and original_view_id=:viewId",nativeQuery=true)
	List<BigInteger> findOrginalRowIdsByOrginalViewIdAndReconReference(@Param("reconRef") List<String> reconRef,@Param("viewId") BigInteger viewId);
		
	@Query(value =  "SELECT target_row_id FROM t_reconciliation_result where recon_reference in (:reconRef) and target_view_id=:viewId",nativeQuery=true)
	List<BigInteger> findTargetRowIdsByOrginalViewIdAndReconReference(@Param("reconRef") List<String> reconRef,@Param("viewId") BigInteger viewId);
	
}