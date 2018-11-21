


package com.nspl.app.repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import com.nspl.app.domain.AppModuleSummary;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the AppModuleSummary entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppModuleSummaryRepository extends JpaRepository<AppModuleSummary,Long> {


	/**dashBoard v3 & v4**/
	@Query(value =  "SELECT avg(dv_count),sum(type_count),round((avg(dv_count)-sum(type_count)),2) as unProcessedCount,"
			+ "case when round((sum(type_count)/avg(dv_count) * 100),2) is null then 0 else round((sum(type_count)/avg(dv_count) * 100),2) end as processedPercent,"
			+ "case when round(100-(sum(type_count)/avg(dv_count) * 100),2) is null then 0 else round(100-(sum(type_count)/avg(dv_count) * 100),2) end as unProcessedPer,view_id,"
			+ "case when round((avg(dv_amt)-sum(type_amt)),2) is null then 0 else round((avg(dv_amt)-sum(type_amt)),2) end as unProcessedAmt,"
			+ "case when round((sum(type_count)-sum(approval_count)),2) is null then 0 else round((sum(type_count)-sum(approval_count)),2) end as unApprovedCount,"
			+ "case when round(sum(type_amt),2) is null then 0 else  round(sum(type_amt),2) end as processedAmt,"
			+ "case when round(sum(approval_count),2) is null then 0 else round(sum(approval_count),2) end as approvalCount,"
			+ "case when round(avg(dv_amt),2) is null then 0 else round(avg(dv_amt),2) end as totalReconAmt,file_date"
			+ " FROM t_app_module_summary where rule_group_id=:groupId and (file_date>=:fmDate and file_date<=:toDate)"
			+ " group by view_id,file_date",nativeQuery=true)
	List<Object[]> fetchRecCountsByGroupIdAndFileDate(@Param("groupId") Long groupId,@Param("fmDate") LocalDate fmDate,@Param("toDate") LocalDate toDate);


	/**dashBoard v3 & v4**/
	@Query(value =  "SELECT case when avg(dv_count) is null then 0 else avg(dv_count) end as totalDvCount,case when avg(dv_amt) is null then 0 else avg(dv_amt) end as totalDvAmt,file_date FROM t_app_module_summary where rule_group_id=:groupId and (file_date>=:fmDate and file_date<=:toDate) GROUP BY file_date",nativeQuery=true)
	List<Object[]> fetchTotalDvCountAndAmount(@Param("groupId") Long groupId,@Param("fmDate") LocalDate fmDate,@Param("toDate") LocalDate toDate);


	@Query(value =  "SELECT case when avg(dv_count) is null then 0 else avg(dv_count) end as totalDvCount,case when avg(dv_amt) is null then 0 else avg(dv_amt) end as totalDvAmt,file_date FROM t_app_module_summary where rule_group_id=:groupId and view_id =:viewId and (file_date>=:fmDate and file_date<=:toDate) GROUP BY file_date",nativeQuery=true)
	List<Object[]> fetchTotalDvCountAndAmountByViewId(@Param("groupId") Long groupId,@Param("fmDate") LocalDate fmDate,@Param("toDate") LocalDate toDate,@Param("viewId") Long viewId);

	
	/**dashBoard v3 & v4**/
	@Query(value =  "SELECT case when sum(type_count) is null then 0 else sum(type_count) end as totalTypect,case when sum(type_amt) is null then 0 else  sum(type_amt) end as typeAmount FROM t_app_module_summary where rule_group_id=:groupId and (file_date>=:fmDate and file_date<=:toDate)",nativeQuery=true)
	List<Object[]> fetchTotalReconciledCountAndAmount(@Param("groupId") Long groupId,@Param("fmDate") LocalDate fmDate,@Param("toDate") LocalDate toDate);


	/**dashBoard v3 & v4**/
	@Query(value =  "SELECT case when sum(approval_count) is null then 0 else sum(approval_count) end as totalApprovalct FROM t_app_module_summary where rule_group_id=:groupId and (file_date>=:fmDate and file_date<=:toDate)",nativeQuery=true)
	List<Object[]> fetchTotalApprovalCount(@Param("groupId") Long groupId,@Param("fmDate") LocalDate fmDate,@Param("toDate") LocalDate toDate);

	/**dashBoard v8**/
	@Query(value =  "SELECT case when sum(approval_count) is null then 0 else sum(approval_count) end as totalApprovalct FROM t_app_module_summary where rule_group_id=:groupId and (file_date>=:fmDate and file_date<=:toDate) and view_id =:viewId",nativeQuery=true)
	List<Object[]> fetchTotalApprovalCountByViewIdAndRuleGroupId(@Param("groupId") Long groupId,@Param("fmDate") LocalDate fmDate,@Param("toDate") LocalDate toDate,@Param("viewId") Long viewId);


	//@Query(value =  "SELECT (sum(dv_count)-sum(type_count)) as unProcessedCount FROM t_app_module_summary where rule_group_id=:groupId and file_date between :fmDate and :toDate",nativeQuery=true)
	//Object[] fetchUnRecCountsByGroupIdAndFileDate(@Param("groupId") Long groupId,@Param("fmDate") LocalDate fmDate,@Param("toDate") LocalDate toDate);



	//@Query(value =  "SELECT sum(type_count) FROM t_app_module_summary where rule_group_id=:groupId and type='Not accounted' and file_date between :fmDate and :toDate",nativeQuery=true)
	//	Object[] fetchUnActCountsByGroupIdAndFileDate(@Param("groupId") Long groupId,@Param("fmDate") LocalDate fmDate,@Param("toDate") LocalDate toDate);


	/** in app module summary for 1st dashboard**/
	@Query(value =  "SELECT sum(distinct(dv_count)),sum(type_count),round(sum(type_count)/sum(distinct(dv_count))*100,2) as percet,type,rule_id,view_id,case when (sum(type_count)-sum(approval_count)) is null then 0 else (sum(type_count)-sum(approval_count)) end as unApprovedCount"
			+ " FROM t_app_module_summary where rule_group_id=:groupId and type=:type and "
			+ "file_date between :fmDate and :toDate group by rule_id,view_id,type",nativeQuery=true)
	List<Object[]> fetchActCountsByGroupIdAndFileDate(@Param("groupId") Long groupId,@Param("type") String type,@Param("fmDate") LocalDate fmDate,@Param("toDate") LocalDate toDate);


	/**dashboard v3 & v4**/
	@Query(value =  "SELECT (select sum(dv_count) from (select distinct(view_id),dv_count FROM t_app_module_summary where view_id =:viewId and rule_group_id=:groupId and (file_date>=:fmDate and file_date<=:toDate) ) a) as dvCount,"
			+ "case when sum(type_count) is null then 0 else sum(type_count) end as typeCount,"
			+ "case when round(sum(type_count)/(select sum(dv_count) from (select distinct(view_id),dv_count FROM t_app_module_summary where view_id =:viewId and rule_group_id=:groupId and (file_date>=:fmDate and file_date<=:toDate) ) a)*100,2) is null then 0 else round(sum(type_count)/(select sum(dv_count) from (select distinct(view_id),dv_count FROM t_app_module_summary where view_id =:viewId and rule_group_id=:groupId and (file_date>=:fmDate and file_date<=:toDate) ) a)*100,2) end as percet,type,view_id,"
			+ "case when (select sum(dv_amt) from (select distinct(view_id),dv_amt FROM t_app_module_summary where view_id =:viewId and rule_group_id=:groupId and (file_date>=:fmDate and file_date<=:toDate) ) a)-sum(type_amt) is null then 0 else (select sum(dv_amt) from (select distinct(view_id),dv_amt FROM t_app_module_summary where view_id =:viewId and rule_group_id=:groupId and (file_date>=:fmDate and file_date<=:toDate) ) a)-sum(type_amt) end as amount,"
			+ "case when (sum(type_count)-sum(approval_count)) is null then 0 else (sum(type_count)-sum(approval_count)) end as unApprovedCount,"
			+ "case when sum(approval_count) is  null then 0 else sum(approval_count) end as approvalCt,"
			+ "case when (select sum(dv_amt) from (select distinct(view_id),dv_amt FROM t_app_module_summary where view_id =:viewId and rule_group_id=:groupId and (file_date>=:fmDate and file_date<=:toDate) ) a) is  null then 0 else (select sum(dv_amt) from (select distinct(view_id),dv_amt FROM t_app_module_summary where view_id =:viewId and rule_group_id=:groupId and (file_date>=:fmDate and file_date<=:toDate) ) a) end as totalDvAmt,"
			+ "case when sum(type_amt) is  null then 0 else sum(type_amt) end as totalTypeAmt"
			+ " FROM t_app_module_summary where view_id =:viewId and rule_group_id=:groupId and "
			+ "file_date>=:fmDate and file_date<=:toDate group by view_id,type",nativeQuery=true)
	List<Object[]> fetchActCountsByGroupIdAndViewIdAndFileDate(@Param("groupId") Long groupId,@Param("viewId") Long viewId,@Param("fmDate") LocalDate fmDate,@Param("toDate") LocalDate toDate);


	/**dashboard v2**/
	@Query(value =  "SELECT case when sum(type_amt) is null then 0 else sum(type_amt) end as totalTypeamt FROM t_app_module_summary where rule_group_id=:groupId and view_id =:viewId and type='final accounted' and "
			+ "file_date between :fmDate and :toDate group by view_id",nativeQuery=true)
	BigDecimal fetchFinalActAmtByGroupIdAndViewIdAndFileDate(@Param("groupId") Long groupId,@Param("viewId") Long viewId,@Param("fmDate") LocalDate fmDate,@Param("toDate") LocalDate toDate);


	/** in app module summary for 1st dashboard**/
	@Query(value =  "select DATEDIFF(:toDate,file_date) as `rule_age`, sum(dv_count),(sum(dv_count)-sum(type_count)) as unreconcilied,round((sum(dv_count)-sum(type_count))/sum(dv_count) *100,2) as unReconciledPer,"
			+ " concat((DATEDIFF(:toDate,file_date)-1),'-' ,DATEDIFF(:toDate,file_date)) as period from "
			+ "t_app_module_summary where file_date between :fmDate and :toDate and   rule_group_id =:groupId and rule_id =:ruleId and view_id =:viewId group by rule_age, period",nativeQuery=true)
	List<Object[]> fetchAgingForUnReconCount(@Param("groupId") Long groupId,@Param("fmDate") LocalDate fmDate,@Param("toDate") LocalDate toDate,@Param("ruleId") Long ruleId,@Param("viewId") Long viewId);




	/** in app module summary for 1st dashboard**/
	@Query(value =  "SELECT sum(dv_count),sum(type_count),case when round(sum(type_count)/sum(dv_count) *100,2) is null then 0 else round(sum(type_count)/sum(dv_count) *100,2) end as reconciledPer,round(100-(sum(type_count)/sum(dv_count) * 100),2) as unRecPer FROM "
			+ "t_app_module_summary where rule_group_id =:groupId and file_date=:fmDate",nativeQuery=true)
	List< Object[]> fetchReconCountAndUnReconciledCountPerDay(@Param("groupId") Long groupId,@Param("fmDate") LocalDate fmDate);




	/*@Query(value =  "SELECT sum(dv_count),sum(type_count),case when round(sum(type_count)/sum(dv_count) *100,2) is null then 0 else round(sum(type_count)/sum(dv_count) *100,2) end as reconciledPer,round(100-(sum(type_count)/sum(dv_count) * 100),2) as unRecPer,"
			+ "sum(dv_amt),sum(type_amt),case when round(sum(type_amt)/sum(dv_amt) *100,2) is null then 0 else round(sum(type_amt)/sum(dv_amt) *100,2) end as reconciledAmtPer,round(100-(sum(type_amt)/sum(dv_amt) * 100),2) as unRecAmtPer FROM "
			+ "t_app_module_summary where rule_group_id =:groupId and file_date >= :fDate and file_date <=:tDate",nativeQuery=true)
	List< Object[]> fetchReconCountAndUnReconciledCountBetweenGivenDates(@Param("groupId") Long groupId,@Param("fDate") LocalDate fDate,@Param("tDate") LocalDate tDate);*/

	
	/**dashboard v3**/
	
	/**sample**/
	@Query(value =  "SELECT rule_group_id, ROUND(SUM(countPer), 2) AS reconciledPer, 100 - ROUND(SUM(countPer), 2) AS unRecPer, ROUND(SUM(amtPer), 2) reconcileAmtPer, 100 - ROUND(SUM(amtPer), 2) AS unRecAmtPer,"
			+ "CASE WHEN ((select sum(dvAmt) from (SELECT file_date,count(distinct view_id),sum( distinct dv_amt) as dvAmt FROM t_app_module_summary WHERE rule_group_id =:groupId and file_date >= :fDate and file_date <=:tDate group by file_date)f) - SUM(recAmt)) IS NULL THEN 0 "
			+ " ELSE ((select sum(dvAmt) from (SELECT  file_date,count(distinct view_id),sum( distinct dv_amt) as dvAmt FROM t_app_module_summary WHERE rule_group_id =:groupId and file_date >= :fDate and file_date <=:tDate group by file_date)f) - SUM(recAmt)) END AS unRecAmt, SUM(appCt), SUM(initiatedCount), "
			+ " CASE WHEN SUM(recAmt) IS NULL THEN 0 ELSE SUM(recAmt) END AS recAmt, CASE WHEN ((select sum(dvCount) from (SELECT file_date,count(distinct view_id),sum( distinct dv_count) as dvCount "
			+ "FROM t_app_module_summary WHERE rule_group_id =:groupId and file_date >= :fDate and file_date <=:tDate group by file_date)f) - SUM(recCt)) IS NULL THEN 0 "
			+ "ELSE ((select sum(dvCount) from (SELECT file_date,count(distinct view_id),sum( distinct dv_count) as dvCount FROM t_app_module_summary WHERE rule_group_id =:groupId and file_date >= :fDate and file_date <=:tDate group by file_date)f) - SUM(recCt)) END AS unRecCt, "
			+ "CASE WHEN SUM(recCt) IS NULL THEN 0 ELSE SUM(recCt) END AS recCt, CASE WHEN SUM(initiatedCount) - SUM(appCt) IS NULL THEN 0 ELSE SUM(initiatedCount) - SUM(appCt) END AS awaitingCount, "
			+ "CASE WHEN ROUND(((SUM(initiatedCount) - SUM(appCt)) / SUM(initiatedCount)) * 100, 2) IS NULL THEN 0 ELSE ROUND(((SUM(initiatedCount) - SUM(appCt)) / SUM(initiatedCount)) * 100, 2) END AS awaitingCountPer "
			+ "FROM (SELECT  rule_group_id, rule_id, SUM(countPer)/2  AS countPer, SUM(amtper)/2  AS amtPer, "
			+ "avg(dv_count) AS dvCt, avg(dv_amt) AS dvAmt,"
			+ " SUM(recon_count) AS recCt,  SUM(recon_amt) AS recAmt, SUM(approval_count) AS appCt, SUM(initiatedCount) AS initiatedCount, view_id FROM "
			+ "(SELECT  rule_group_id, rule_id,  view_id, type, CASE WHEN sum(dv_count) IS NULL THEN 0 ELSE sum(dv_count) END AS dv_count, "
			+ "CASE WHEN SUM(type_count) IS NULL THEN 0 ELSE SUM(type_count) END AS recon_count, CASE WHEN sum(dv_amt) IS NULL THEN 0 ELSE sum(dv_amt) END AS dv_amt, "
			+ "CASE WHEN SUM(type_amt) IS NULL THEN 0 ELSE SUM(type_amt) END AS recon_amt, "
			+ "CASE WHEN ROUND((SUM(type_count) / sum(dv_count)) * 100, 2) IS NULL THEN 0 ELSE ROUND((SUM(type_count) / sum(dv_count)) * 100, 2) END AS countPer, "
			+ "CASE WHEN ROUND((SUM(type_amt) / sum(dv_amt)) * 100, 2) IS NULL THEN 0 ELSE ROUND((SUM(type_amt) / sum(dv_amt)) * 100, 2) END AS amtPer,"
			+ " CASE WHEN SUM(initiated_count) IS NULL THEN 0 ELSE SUM(initiated_count) END AS initiatedCount, CASE WHEN SUM(approval_count) IS NULL THEN 0 ELSE SUM(approval_count) END AS approval_count "
			+ "FROM t_app_module_summary WHERE rule_group_id =:groupId and file_date >= :fDate and file_date <=:tDate GROUP BY rule_group_id , rule_id , view_id , type) d GROUP BY rule_group_id , rule_id,view_id) p "
			+ "GROUP BY rule_group_id",nativeQuery=true)
	List< Object[]> fetchReconCountAndUnReconciledCountBetweenGivenDates(@Param("groupId") Long groupId,@Param("fDate") LocalDate fDate,@Param("tDate") LocalDate tDate);

	
/** dashboard v4 for accounting**/
	
	@Query(value =  "select sum(dv_count) from (select distinct (dv_count),file_date,view_id,type from  t_app_module_summary where rule_group_id =:groupId and file_date >= :fDate and file_date <=:tDate and type=:type) f",nativeQuery=true)
	BigDecimal fetchTotalDvcountForActGroupByType(@Param("groupId") Long groupId,@Param("fDate") LocalDate fDate,@Param("tDate") LocalDate tDate,@Param("type") String type);
	
	/** dashboard v4 for accounting**/
	@Query(value =  "select sum(dv_amt) from (select distinct (dv_amt),file_date,view_id,type from  t_app_module_summary where rule_group_id =:groupId and file_date >= :fDate and file_date <=:tDate and type=:type) f",nativeQuery=true)
	BigDecimal fetchTotalDvAmtForActGroupByType(@Param("groupId") Long groupId,@Param("fDate") LocalDate fDate,@Param("tDate") LocalDate tDate,@Param("type") String type);
	
	
	
	
	
	/** dashboard v4**/
	
	@Query(value =  "select sum(dv_count) from (select distinct (dv_count),file_date,view_id,type from  t_app_module_summary where rule_group_id =:groupId and file_date >= :fDate and file_date <=:tDate) f",nativeQuery=true)
	BigDecimal fetchTotalDvcountForReconGroup(@Param("groupId") Long groupId,@Param("fDate") LocalDate fDate,@Param("tDate") LocalDate tDate);
	
	/** dashboard v4**/
	@Query(value =  "select sum(dv_amt) from (select distinct (dv_amt),file_date,view_id,type from  t_app_module_summary where rule_group_id =:groupId and file_date >= :fDate and file_date <=:tDate) f",nativeQuery=true)
	BigDecimal fetchTotalDvAmtForReconGroup(@Param("groupId") Long groupId,@Param("fDate") LocalDate fDate,@Param("tDate") LocalDate tDate);
	
	
	/** dashboard v3,v4**/
	@Query(value =  "select rule_group_id,case when sum(type_count) is null then 0 else sum(type_count) end as recCt,"
      +" case when ROUND((sum(type_count)/:totDvCt)*100 ,2) is null then 0 else  ROUND((sum(type_count)/:totDvCt)*100 ,2) end as recCtPer ,"
      +"  case when :totDvCt-sum(type_count) is null then 0 else :totDvCt-sum(type_count) end as unRecCt,"
      +"  case when  ROUND(((:totDvCt-sum(type_count))/:totDvCt) *100,2) is null then 0 else ROUND(((:totDvCt-sum(type_count))/:totDvCt)*100 ,2) end as unRecCtPer,"
      +"  case when ROUND(sum(type_amt),2) is null then 0 else ROUND(sum(type_amt),2) end as recAmt,"
      +"  case when ROUND((sum(type_amt)/:totDvAmt)*100,2) is null then 0 else  ROUND((sum(type_amt)/:totDvAmt)*100 ,2) end as recAmtPer ,"
      +"  case when ROUND(:totDvAmt-sum(type_amt),2) is null then 0 else ROUND(:totDvAmt-sum(type_amt),2) end as unRecAmt,"
      +"  case when  ROUND(((:totDvAmt-sum(type_amt))/:totDvAmt)*100 ,2) is null then 0 else ROUND(((:totDvAmt-sum(type_amt))/:totDvAmt)*100 ,2) end as unRecAmtPer,"
      + " CASE WHEN SUM(initiated_count) - SUM(approval_count) IS NULL THEN 0 ELSE SUM(initiated_count) - SUM(approval_count) END AS awaitingCount,"
      + "CASE WHEN ROUND(((SUM(initiated_count) - SUM(approval_count)) / SUM(initiated_count)) * 100, 2) IS NULL THEN 0 ELSE ROUND(((SUM(initiated_count) - SUM(approval_count)) / SUM(initiated_count)) * 100, 2) END AS awaitingCountPer "
      +"  from t_app_module_summary"
      +"  WHERE rule_group_id =:groupId and file_date >= :fDate and file_date <=:tDate",nativeQuery=true)

	List< Object[]> fetchReconciledAndUnReconciledAmountsAndCounts(@Param("groupId") Long groupId,@Param("fDate") LocalDate fDate,@Param("tDate") LocalDate tDate
			,@Param("totDvCt") BigDecimal totDvCt,@Param("totDvAmt") BigDecimal totDvAmt);

	
	
	
/** dashboard v4**/
	
	@Query(value =  "select sum(dv_count) from (select distinct (dv_count),file_date,view_id,type from  t_app_module_summary where rule_group_id =:groupId and file_date >= :fDate and file_date <=:tDate and view_id=:viewId and type=:type) f",nativeQuery=true)
	BigDecimal fetchTotalDvcountForReconGroupByViewIdAndType(@Param("groupId") Long groupId,@Param("fDate") LocalDate fDate,@Param("tDate") LocalDate tDate,@Param("viewId") Long viewId,@Param("type") String type);
	
	/** dashboard v4**/
	@Query(value =  "select sum(dv_amt) from (select distinct (dv_amt),file_date,view_id,type from  t_app_module_summary where rule_group_id =:groupId and file_date >= :fDate and file_date <=:tDate and view_id=:viewId and type=:type) f",nativeQuery=true)
	BigDecimal fetchTotalDvAmtForReconGroupByViewIdAndType(@Param("groupId") Long groupId,@Param("fDate") LocalDate fDate,@Param("tDate") LocalDate tDate,@Param("viewId") Long viewId,@Param("type") String type);
	
	
	/** dashboard v3, v4**/
	@Query(value =  "select rule_group_id,case when sum(type_count) is null then 0 else sum(type_count) end as recCt,"
      +" case when ROUND((sum(type_count)/:totDvCt)*100 ,2) is null then 0 else  ROUND((sum(type_count)/:totDvCt)*100 ,2) end as recCtPer ,"
      +"  case when :totDvCt-sum(type_count) is null then 0 else :totDvCt-sum(type_count) end as unRecCt,"
      +"  case when  ROUND(((:totDvCt-sum(type_count))/:totDvCt) *100,2) is null then 0 else ROUND(((:totDvCt-sum(type_count))/:totDvCt)*100 ,2) end as unRecCtPer,"
      +"  case when ROUND(sum(type_amt),2) is null then 0 else ROUND(sum(type_amt),2) end as recAmt,"
      +"  case when ROUND((sum(type_amt)/:totDvAmt)*100,2) is null then 0 else  ROUND((sum(type_amt)/:totDvAmt)*100 ,2) end as recAmtPer ,"
      +"  case when ROUND(:totDvAmt-sum(type_amt),2) is null then 0 else ROUND(:totDvAmt-sum(type_amt),2) end as unRecAmt,"
      +"  case when  ROUND(((:totDvAmt-sum(type_amt))/:totDvAmt)*100 ,2) is null then 0 else ROUND(((:totDvAmt-sum(type_amt))/:totDvAmt)*100 ,2) end as unRecAmtPer,"
      + " CASE WHEN SUM(initiated_count) - SUM(approval_count) IS NULL THEN 0 ELSE SUM(initiated_count) - SUM(approval_count) END AS awaitingCount,"
      + "CASE WHEN ROUND(((SUM(initiated_count) - SUM(approval_count)) / SUM(initiated_count)) * 100, 2) IS NULL THEN 0 ELSE ROUND(((SUM(initiated_count) - SUM(approval_count)) / SUM(initiated_count)) * 100, 2) END AS awaitingCountPer "
       
      +"  from t_app_module_summary"
      +"  WHERE rule_group_id =:groupId and file_date >= :fDate and file_date <=:tDate and view_id=:viewId and type=:type",nativeQuery=true)

	List< Object[]> fetchReconciledAndUnReconciledAmountsAndCountsByViewIdAndType(@Param("groupId") Long groupId,@Param("fDate") LocalDate fDate,@Param("tDate") LocalDate tDate
			,@Param("totDvCt") BigDecimal totDvCt,@Param("totDvAmt") BigDecimal totDvAmt,@Param("viewId") Long viewId,@Param("type") String type);
	
	
	
	
	/**dashboard v3**/
	@Query(value =  "SELECT rule_group_id, round(SUM(countPer),2) as reconciledPer , 100-round(SUM(countPer),2) as unRecPer, round(SUM(amtPer),2) reconcileAmtPer,"
			+ " 100-round(SUM(amtPer),2) as unRecAmtPer"
			+ " FROM(SELECT rule_group_id, "
			+ "rule_id, SUM(countPer) / 2 AS countPer, SUM(amtper) / 2 AS amtPer FROM (SELECT rule_group_id, rule_id, view_id, type, "
			+ "case when sum(dv_count) is null then 0 else SUM(dv_count) end AS dv_count, case when SUM(type_count) is null then 0 else SUM(type_count) end AS recon_count, "
			+ "case when SUM(dv_amt) is null then 0 else SUM(dv_amt) end AS dv_amt, case when SUM(type_amt) is null then 0 else SUM(type_amt) end AS recon_amt, "
			+ "case when round((SUM(type_count) / SUM(dv_count)) * 100,2) is null then 0 else round((SUM(type_count) / SUM(dv_count)) * 100,2) end AS countPer,"
			+ " case when round( (SUM(type_amt) / SUM(dv_amt)) * 100,2) is null then 0 else round( (SUM(type_amt) / SUM(dv_amt)) * 100,2) end AS amtPer"
			+ " FROM t_app_module_summary WHERE rule_group_id =:groupId and file_date = :tDate GROUP BY rule_group_id , "
			+ "rule_id , view_id , type) d GROUP BY rule_group_id , rule_id) p GROUP BY rule_group_id",nativeQuery=true)
	List< Object[]> fetchReconCountAndUnReconciledCountDatewise(@Param("groupId") Long groupId,@Param("tDate") LocalDate tDate);
	
	
	
	/**dashboard v8**/
	@Query(value =  "SELECT rule_group_id, round(SUM(countPer),2) as reconciledPer , 100-round(SUM(countPer),2) as unRecPer, round(SUM(amtPer),2) reconcileAmtPer,"
			+ " 100-round(SUM(amtPer),2) as unRecAmtPer"
			+ " FROM(SELECT rule_group_id, "
			+ "rule_id, SUM(countPer) / 2 AS countPer, SUM(amtper) / 2 AS amtPer FROM (SELECT rule_group_id, rule_id, view_id, type, "
			+ "case when sum(dv_count) is null then 0 else SUM(dv_count) end AS dv_count, case when SUM(type_count) is null then 0 else SUM(type_count) end AS recon_count, "
			+ "case when SUM(dv_amt) is null then 0 else SUM(dv_amt) end AS dv_amt, case when SUM(type_amt) is null then 0 else SUM(type_amt) end AS recon_amt, "
			+ "case when round((SUM(type_count) / SUM(dv_count)) * 100,2) is null then 0 else round((SUM(type_count) / SUM(dv_count)) * 100,2) end AS countPer,"
			+ " case when round( (SUM(type_amt) / SUM(dv_amt)) * 100,2) is null then 0 else round( (SUM(type_amt) / SUM(dv_amt)) * 100,2) end AS amtPer"
			+ " FROM t_app_module_summary WHERE rule_group_id =:groupId and file_date = :tDate and view_id =:viewId GROUP BY rule_group_id , "
			+ "rule_id , view_id , type) d GROUP BY rule_group_id , rule_id) p GROUP BY rule_group_id",nativeQuery=true)
	List< Object[]> fetchReconCountAndUnReconciledCountDatewiseByRuleGroupIdAndViewId(@Param("groupId") Long groupId,
			@Param("tDate") LocalDate tDate,@Param("viewId") Long viewId);


	/** in app module summary for 1st dashboard**/
	@Query(value =  "select distinct type from t_app_module_summary where rule_group_id=:groupId",nativeQuery=true)
	List<String> findDistinctTypeByRuleGroupId(
			@Param("groupId") Long groupId);



	@Query(value =  "select distinct view_id from t_app_module_summary where rule_group_id=:groupId and file_date between :fmDate and :toDate",nativeQuery=true)
	List<BigInteger> findDistinctViewIdByRuleGroupId(
			@Param("groupId") Long groupId,@Param("fmDate") LocalDate fmDate,@Param("toDate") LocalDate toDate);

	/**dashboard v3**/
	@Query(value =  "select view_id,avg(dv_amt),avg(dv_count),file_date from t_app_module_summary where rule_group_id=:groupId and (file_date >=:fmDate and file_date<=:toDate) group by view_id,file_date",nativeQuery=true)
	List<Object[]> findToatlDvAmountAndDvCount(
			@Param("groupId") Long groupId,@Param("fmDate") LocalDate fmDate,@Param("toDate") LocalDate toDate);


	/**dashboard v3**/
	@Query(value =  "select view_id,case when sum(type_amt) is null then 0 else sum(type_amt) end as typeAmt,case when sum(type_count) is null then 0 else sum(type_count) end as typecount from t_app_module_summary where rule_group_id=:groupId and (file_date >=:fmDate and file_date<=:toDate) and type in (:type) group by view_id",nativeQuery=true)
	List<Object[]> findToatlTypeAmountAndTypeCountByType(
			@Param("groupId") Long groupId,@Param("fmDate") LocalDate fmDate,@Param("toDate") LocalDate toDate,@Param("type") List<String> type);


	/**dashboard 1**/
	@Query(value =  "select DATEDIFF(:toDate,file_date) as `rule_age`, sum(dv_count),sum(type_count),round((sum(dv_count)-sum(type_count))/sum(dv_count) *100,2) as actPer,"
			+ " concat((DATEDIFF(:toDate,file_date)-1),'-' ,DATEDIFF(:toDate,file_date)) as period from "
			+ "t_app_module_summary where file_date between :fmDate and :toDate and  rule_group_id =:groupId and type =:type group by rule_age, period",nativeQuery=true)
	List<Object[]> fetchAgingForAccountingSummary(@Param("groupId") Long groupId,@Param("type") String type,@Param("fmDate") LocalDate fmDate,@Param("toDate") LocalDate toDate);



	/**Each Day analysis for accted summary dashboard 1
	 */

	@Query(value =  "SELECT sum(dv_count),sum(type_count),case when round(sum(type_count)/sum(dv_count) *100,2) is null then 0 else round(sum(type_count)/sum(dv_count) *100,2) end as per FROM "
			+ "t_app_module_summary where rule_group_id =:groupId and type=:type and file_date=:fmDate",nativeQuery=true)
	List<Object[]> fetchProcessedAndUnProcessedCountPerDay(@Param("groupId") Long groupId,@Param("type") String type,@Param("fmDate") LocalDate fmDate);

	/*@Query(value =  "SELECT (select sum(dv_count) from (select distinct(view_id), dv_count  FROM t_app_module_summary  where"
             +" rule_group_id =:groupId and (file_date >=:fDate and file_date <=:tDate)) a) as dv_count,"
             +" sum(type_count),"
             +" case when round(sum(type_count)/(select sum(dv_count) from (select distinct(view_id), dv_count  FROM t_app_module_summary  where"
             +" rule_group_id =:groupId and (file_date >=:fDate and file_date <=:tDate)) a) *100,2) is null then 0 else round(sum(type_count)/(select sum(dv_count) from (select distinct(view_id), dv_count  FROM t_app_module_summary  where"
             +" rule_group_id =:groupId and (file_date >=:fDate and file_date <=:tDate)) a) *100,2) end as per,"
             +" sum(type_amt),"
             +" case when round(sum(type_amt)/(select sum(dv_amt) from (select distinct(view_id), dv_amt  FROM t_app_module_summary  where"
             +" rule_group_id =:groupId and (file_date >=:fDate and file_date <=:tDate)) a) *100,2) is null then 0 else round(sum(type_amt)/(select sum(dv_amt) from (select distinct(view_id), dv_amt  FROM t_app_module_summary  where"
             +" rule_group_id =:groupId and (file_date >=:fDate and file_date <=:tDate)) a) *100,2) end as amtPer FROM "
			+ "t_app_module_summary where rule_group_id =:groupId and type=:type and (file_date>=:fDate and file_date<=:tDate)",nativeQuery=true)
	List<Object[]> fetchProcessedAndUnProcessedCountBetweenGivenDates(@Param("groupId") Long groupId,@Param("type") String type,@Param("fDate") LocalDate fDate,@Param("tDate") LocalDate tDate);
	 */


	/**dashboard v2**/
	@Query(value =  "select g.*, round(((type_count/dv_count)*100),2) as countper, round(((type_amt/dv_amt)*100)) as amtPer "
			+ " from (select case when sum(dv_count) is null then 0 else sum(dv_count) end as dv_count, "
			+ "case when sum(type_count) is null then 0 else sum(type_count) end as type_count, case when sum(dv_amt) is null then 0 else sum(dv_amt) end as dv_amt, "
			+ "case when sum(type_amt) is null then 0 else round(sum(type_amt),2) end as type_amt from t_app_module_summary where rule_group_id = :groupId and type= :type AND (file_date >= :fDate"
			+" AND file_date <= :tDate)) g",nativeQuery=true)
	List<Object[]> fetchAccNUnAccInfoBetweenGivenDates(@Param("groupId") Long groupId,@Param("type") String type,@Param("fDate") LocalDate fDate,@Param("tDate") LocalDate tDate);       



	/**dashBoard v2 & v3**/
	@Query(value =  "SELECT case when sum(dv_count) is null then 0 else sum(dv_count) end as dv_count,"
			+ "case when sum(type_count) is null then 0 else sum(type_count) end as type_count,"
			+ "case when round(sum(type_count)/sum(dv_count) *100,2) is null then 0 else round(sum(type_count)/sum(dv_count) *100,2) end as reconciledPer,"
			+ "case when round((sum(dv_count)-sum(type_count))/sum(dv_count) * 100,2) is null then 0 else round((sum(dv_count)-sum(type_count))/sum(dv_count) * 100,2) end as unRecPer,"
			+ "case when sum(dv_amt) is null then 0 else sum(dv_amt) end as dvAmt,"
			+ "case when sum(type_amt) is null then 0 else sum(type_amt) end as typeAmt,"
			+ "case when round(sum(type_amt)/sum(dv_amt) *100,2) is null then 0 else round(sum(type_amt)/sum(dv_amt) *100,2) end as reconcileAmtPer,"
			//+ "case when round(100-(sum(type_amt)/sum(dv_amt) * 100),2) is null then 0 else round(100-(sum(type_amt)/sum(dv_amt) * 100),2) end as unRecAmtPer,"
			//+ "case when round((sum(dv_amt)-sum(type_amt))/sum(dv_amt) * 100),2) is null then 0 else round(sum(dv_amt)-sum(type_amt))/sum(dv_amt) * 100),2) end as unRecAmtPer,"
			+ "case when round((sum(dv_amt)-sum(type_amt))/sum(dv_amt) * 100,2) is null then 0 else round((sum(dv_amt)-sum(type_amt))/sum(dv_amt) * 100,2) end as unRecAmtPer,"
			+ "DATE(file_date) as date FROM "
			+ "t_app_module_summary where rule_group_id =:groupId and file_date>:fmDate and file_date<=:toDate GROUP BY DATE(file_date)",nativeQuery=true)
	List< Object[]> fetchReconCountAndUnReconciledCountWithDates(@Param("groupId") Long groupId,@Param("fmDate") LocalDate fmDate,@Param("toDate") LocalDate toDate);



	/*@Query(value =  "SELECT case when sum(dv_count) is null then 0 else sum(dv_count) end as dv_count,"
			+ "case when sum(type_count) is null then 0 else sum(type_count) end as type_count,"
			+ "case when round(sum(type_count)/sum(dv_count) *100,2) is null then 0 else round(sum(type_count)/sum(dv_count) *100,2) end as reconciledPer,"
			+ "case when round((sum(dv_count)-sum(type_count))/sum(dv_count) * 100,2) is null then 0 else round((sum(dv_count)-sum(type_count))/sum(dv_count) * 100,2) end as unRecPer,"
			+ "case when sum(dv_amt) is null then 0 else sum(dv_amt) end as dvAmt,"
			+ "case when sum(type_amt) is null then 0 else sum(type_amt) end as typeAmt,"
			+ "case when round(sum(type_amt)/sum(dv_amt) *100,2) is null then 0 else round(sum(type_amt)/sum(dv_amt) *100,2) end as reconcileAmtPer,"
	        + "case when round((sum(dv_amt)-sum(type_amt))/sum(dv_amt) * 100,2) is null then 0 else round((sum(dv_amt)-sum(type_amt))/sum(dv_amt) * 100,2) end as unRecAmtPer FROM "
			+ "t_app_module_summary where rule_group_id =:groupId and file_date=:fmDate",nativeQuery=true)
	List< Object[]> fetchReconCountAndUnReconciledCountForDate(@Param("groupId") Long groupId,@Param("fmDate") LocalDate fmDate);

	 */




	/*@Query(value =  "SELECT sum(dv_count),sum(type_count),"
			+ "case when round(sum(type_count)/sum(dv_count) *100,2) is null then 0 else round(sum(type_count)/sum(dv_count) *100,2) end as reconciledPer,"
			+ "case when round((sum(dv_count)-sum(type_count))/sum(dv_count) * 100,2) is null then 0 else round((sum(dv_count)-sum(type_count))/sum(dv_count) * 100,2) end as unRecPer,"
			+ "case when sum(dv_amt) is null then 0 else sum(dv_amt) end as dvAmt,"
			+ "case when sum(type_amt) is null then 0 else sum(type_amt) end as typeAmt,"
			+ "case when round(sum(dv_amt)/sum(dv_amt) *100,2) is null then 0 else round(sum(dv_amt)/sum(dv_amt) *100,2) end as reconcileAmtPer,"
			+ "case when round((sum(dv_amt)-sum(type_amt))/sum(dv_amt) * 100),2) is null then 0 else round(sum(dv_amt)-sum(type_amt))/sum(dv_amt) * 100),2) end as unRecAmtPer,"
			+ "FLOOR((DayOfMonth(file_date)-1)/7)+1 as week FROM "
			+ "t_app_module_summary where rule_group_id =:groupId and file_date between :fmDate and :toDate GROUP BY FLOOR((DayOfMonth(file_date)-1)/7)+1",nativeQuery=true)
	List< Object[]> fetchReconCountAndUnReconciledCountWithweeks(@Param("groupId") Long groupId,@Param("fmDate") LocalDate fmDate,@Param("toDate") LocalDate toDate);

	 */

	/*@Query(value =  "select type,Date(file_date), sum(dv_count),sum(type_count),sum(dv_amt),sum(type_amt),case when round(sum(type_count)/sum(dv_count) *100,2) is null then 0 else  round(sum(type_count)/sum(dv_count) *100,2) end as countPer, case when round(sum(type_amt)/sum(dv_amt) *100,2) is null then 0 else  round(sum(type_count)/sum(dv_count) *100,2) end as amtPer from t_app_module_summary where rule_group_id =216 and file_date between '2018-01-30' and '2018-02-28' group by type,Date(file_date)",nativeQuery=true)
	List<Object[]> fetchAccountingAnalysisWithDates();*/


	/*	@Query(value = "select type,Date(file_date), sum(dv_count),sum(type_count),sum(dv_amt),sum(type_amt),"
			+ "case when round(sum(type_count)/sum(dv_count) *100,2) is null then 0 else round(sum(type_count)/sum(dv_count) *100,2) end as countPer,"
			+ "case when round(sum(type_amt)/sum(dv_amt) *100,2) is null then 0 else round(sum(type_amt)/sum(dv_amt) *100,2) end as amtPer "
			+ "from t_app_module_summary where rule_group_id =:groupId and (file_date >:fmDate and file_date <=:toDate) group by type,Date(file_date)",nativeQuery=true)
			List<Object[]> fetchAccountingAnalysisWithDates(@Param("groupId") Long groupId,@Param("fmDate") LocalDate fmDate,@Param("toDate") LocalDate toDate);*/




	/** dash board v2**/

	@Query(value = "select type,Date(file_date),(select sum(dv_count) from (select distinct(view_id), dv_count  FROM t_app_module_summary  where"
			+" rule_group_id =:groupId and (file_date >:fmDate and file_date <=:toDate)) a) as dv_count,sum(type_count),(select sum(dv_amt) from (select distinct(view_id),dv_amt"
			+" FROM t_app_module_summary where rule_group_id =:groupId and (file_date >:fmDate and file_date <=:toDate)) a) as dvAmt,sum(type_amt),case when round(sum(type_count) / (select"
			+" sum(dv_count) from (select distinct(view_id),dv_count FROM t_app_module_summary where rule_group_id =:groupId and (file_date >:fmDate and file_date <=:toDate)) a) *100,2) is null"
			+" then 0 else round(sum(type_count) / (select sum(dv_count) from (select distinct(view_id),dv_count FROM t_app_module_summary where rule_group_id =:groupId  and (file_date >:fmDate and file_date <=:toDate))"
			+"  a) *100,2) end as countPer,case when round(sum(type_amt) / (select sum(dv_amt) from (select distinct (view_id), dv_amt FROM t_app_module_summary where rule_group_id =:groupId and (file_date >:fmDate and file_date <=:toDate) ) a) *100, 2) is null then 0 else"
			+" round(sum(type_amt) / (select sum(dv_amt) from (select distinct(view_id),dv_amt FROM t_app_module_summary where rule_group_id =:groupId  and (file_date >:fmDate and file_date <=:toDate))"
			+"  a) *100,2) end as amtPer from  t_app_module_summary where rule_group_id =:groupId  and (file_date >:fmDate and file_date <=:toDate)"
			+" group by type, Date(file_date)",nativeQuery=true)
	List<Object[]> fetchAccountingAnalysisWithDates(@Param("groupId") Long groupId,@Param("fmDate") LocalDate fmDate,@Param("toDate") LocalDate toDate);



	/**dashboard v2**/

	@Query(value = "select type,case when (select sum(dv_count) from (select distinct(view_id), dv_count  FROM t_app_module_summary  where"
			+" rule_group_id =:groupId and (file_date >:fmDate and file_date <=:toDate)) a) is null then 0 else (select sum(dv_count) from (select distinct(view_id), dv_count  FROM t_app_module_summary  where"
			+" rule_group_id =:groupId and (file_date >:fmDate and file_date <=:toDate)) a) end as dvCount,"
			+ "case when sum(type_count) is null then 0 else sum(type_count) end as typeCount,"
			+ "case when (select sum(dv_amt) from (select distinct (view_id), dv_amt FROM t_app_module_summary where rule_group_id =:groupId and (file_date between :fmDate and :toDate)) a) is null then 0 else "
			+ "(select sum(dv_amt) from (select distinct (view_id), dv_amt FROM t_app_module_summary where rule_group_id =:groupId and (file_date between :fmDate and :toDate)) a) end as dvAmt,"
			+ "case when sum(type_amt) is null then 0 else sum(type_amt) end as typeAmt,"
			+ "case when round(sum(type_count)/(select sum(dv_count) from (select distinct(view_id), dv_count  FROM t_app_module_summary  where"
			+" rule_group_id =:groupId and (file_date >:fmDate and file_date <=:toDate)) a) *100,2) is null then 0 else "
			+ "round(sum(type_count)/(select sum(dv_count) from (select distinct(view_id), dv_count  FROM t_app_module_summary  where"
			+" rule_group_id =:groupId and (file_date >:fmDate and file_date <=:toDate)) a) *100,2) end as countPer,"
			+ "case when round(sum(type_amt)/(select sum(dv_amt) from (select distinct(view_id), dv_amt  FROM t_app_module_summary  where"
			+" rule_group_id =:groupId and (file_date >:fmDate and file_date <=:toDate)) a) *100,2) is null then 0 else "
			+ "round(sum(type_amt)/(select sum(dv_amt) from (select distinct(view_id), dv_amt  FROM t_app_module_summary  where"
			+" rule_group_id =:groupId and (file_date >:fmDate and file_date <=:toDate)) a) *100,2) end as amtPer "
			+ "from t_app_module_summary where rule_group_id =:groupId and file_date>:fmDate and file_date<=:toDate group by type",nativeQuery=true)
	List<Object[]> fetchAccountingAnalysisWithWeeks(@Param("groupId") Long groupId,@Param("fmDate") LocalDate fmDate,@Param("toDate") LocalDate toDate);




	/**for dashboard v3**/


	@Query(value = "select type,case when (select sum(dv_count) from (select distinct(view_id), dv_count  FROM t_app_module_summary  where"
			+" rule_group_id =:groupId and (file_date >=:fmDate and file_date <=:toDate)) a) is null then 0 else (select sum(dv_count) from (select distinct(view_id), dv_count  FROM t_app_module_summary  where"
			+" rule_group_id =:groupId and (file_date >=:fmDate and file_date <=:toDate)) a) end as dvCount,"
			+ "case when sum(type_count) is null then 0 else sum(type_count) end as typeCount,"
			+ "case when (select sum(dv_amt) from (select distinct (view_id), dv_amt FROM t_app_module_summary where rule_group_id =:groupId and (file_date >=:fmDate and file_date <=:toDate)) a) is null then 0 else "
			+ "(select sum(dv_amt) from (select distinct (view_id), dv_amt FROM t_app_module_summary where rule_group_id =:groupId and (file_date >=:fmDate and file_date <=:toDate)) a) end as dvAmt,"
			+ "case when sum(type_amt) is null then 0 else sum(type_amt) end as typeAmt,"
			+ "case when round(sum(type_count)/(select sum(dv_count) from (select distinct(view_id), dv_count  FROM t_app_module_summary  where"
			+" rule_group_id =:groupId and (file_date >=:fmDate and file_date <=:toDate)) a) *100,2) is null then 0 else "
			+ "round(sum(type_count)/(select sum(dv_count) from (select distinct(view_id), dv_count  FROM t_app_module_summary  where"
			+" rule_group_id =:groupId and (file_date >=:fmDate and file_date <=:toDate)) a) *100,2) end as countPer,"
			+ "case when round(sum(type_amt)/(select sum(dv_amt) from (select distinct(view_id), dv_amt  FROM t_app_module_summary  where"
			+" rule_group_id =:groupId and (file_date >=:fmDate and file_date <=:toDate)) a) *100,2) is null then 0 else "
			+ "round(sum(type_amt)/(select sum(dv_amt) from (select distinct(view_id), dv_amt  FROM t_app_module_summary  where"
			+" rule_group_id =:groupId and (file_date >=:fmDate and file_date <=:toDate)) a) *100,2) end as amtPer "
			+ "from t_app_module_summary where rule_group_id =:groupId and file_date>=:fmDate and file_date<=:toDate group by type",nativeQuery=true)
	List<Object[]> fetchAccountingAnalysisFromAndToDate(@Param("groupId") Long groupId,@Param("fmDate") LocalDate fmDate,@Param("toDate") LocalDate toDate);

	//@@@@	
	/**dash board v3**/
	@Query(value = "select f.*, round(((type_count/dv_count)*100),2) as countper, round(((type_amt/dv_amt)*100),2) as amtPer from "
			+ "(select type, sum(dv_count) as dv_count, case when sum(type_count) is null then 0 else sum(type_count) end as type_count, "
			+ "case when sum(dv_amt) is null then 0 else sum(dv_amt) end as dv_amt, "
			+ "case when sum(type_amt) is null then 0 else sum(type_amt) end as type_amt "
			+ "from t_app_module_summary where rule_group_id = :groupId  AND (file_date >= :fmDate  AND file_date <= :toDate) group by type ) f",nativeQuery=true)
	List<Object[]> fetchAccountingInfoFromAndToDate(@Param("groupId") Long groupId,@Param("fmDate") LocalDate fmDate,@Param("toDate") LocalDate toDate);	



	//added type fileter	
	/**dash board v4 **/			
	/*@Query(value = "SELECT type,sum(f.dv_count), sum(f.type_count), sum(f.dv_amt),sum(f.type_amt),  avg(countper), avg(amtPer) "
			+ "FROM (SELECT  view_id, file_date, type, CASE WHEN :totDvCt IS NULL THEN 0 ELSE :totDvCt END AS dv_count,"
			+ " CASE WHEN SUM(type_count) IS NULL THEN 0 ELSE SUM(type_count) END AS type_count, "
			+ " CASE WHEN :totDvAmt IS NULL THEN 0 ELSE :totDvAmt END AS dv_amt, "
			+ " CASE WHEN SUM(type_amt) IS NULL THEN 0 ELSE SUM(type_amt) END AS type_amt, "
			+ " CASE WHEN ROUND(((sum(type_count) / :totDvCt) * 100), 2) IS NULL THEN 0 ELSE ROUND(((sum(type_count)/ :totDvCt) * 100), 2) END AS countper,"
			+ " CASE WHEN ROUND(((sum(type_amt) / :totDvAmt) * 100), 2) IS NULL THEN 0 ELSE ROUND(((sum(type_amt) / :totDvAmt) * 100), 2) END AS amtPer "
			+ " FROM t_app_module_summary WHERE rule_group_id = :groupId AND (file_date >= :fmDate  AND file_date <= :toDate) AND type =:type group by view_id, file_date, type)f group by type",nativeQuery=true)
	List<Object[]> fetchAccountingInfoFromAndToDateAndType(@Param("groupId") Long groupId,@Param("fmDate") LocalDate fmDate,@Param("toDate") LocalDate toDate,@Param("type") String type,@Param("totDvCt") BigInteger totDvCt,@Param("totDvAmt") BigInteger totDvAmt);	*/
	
	
	@Query(value = " select type,case when :totDvCt is null then 0 else :totDvCt end as dvCount,"
			+ "case when sum(type_count) is null then 0 else sum(type_count) end as typeCount,"
			+ "case when :totDvAmt is null then 0 else :totDvAmt end as dvAmt,"
			+ "case when sum(type_amt) is null then 0 else sum(type_amt) end as typeAmt,"
			+ "CASE WHEN ROUND(((sum(type_count) / :totDvCt) * 100), 2) IS NULL THEN 0 ELSE ROUND(((sum(type_count)/ :totDvCt) * 100), 2) END AS countper,"
			+ "CASE WHEN ROUND(((sum(type_amt) / :totDvAmt) * 100), 2) IS NULL THEN 0 ELSE ROUND(((sum(type_amt) / :totDvAmt) * 100), 2) END AS amtPer  "
			+ "FROM t_app_module_summary WHERE rule_group_id = :groupId AND (file_date >= :fmDate  AND file_date <= :toDate) AND type =:type group by type",nativeQuery=true)
	List<Object[]> fetchAccountingInfoFromAndToDateAndType(@Param("groupId") Long groupId,@Param("fmDate") LocalDate fmDate,@Param("toDate") LocalDate toDate,
			@Param("type") String type,@Param("totDvCt") BigDecimal totDvCt,@Param("totDvAmt") BigDecimal totDvAmt);
	
	
	
	


	/**dashboard v8 added viewId filter **/
/*	@Query(value = "SELECT type,sum(f.dv_count) as dvCt, sum(f.type_count), sum(f.dv_amt) as dvamt,sum(f.type_amt),  sum(countper), sum(amtPer) "
			+ "FROM (SELECT  view_id, file_date, type, CASE WHEN avg(dv_count) IS NULL THEN 0 ELSE avg(dv_count) END AS dv_count,"
			+ " CASE WHEN SUM(type_count) IS NULL THEN 0 ELSE SUM(type_count) END AS type_count, "
			+ " CASE WHEN avg(dv_amt) IS NULL THEN 0 ELSE avg(dv_amt) END AS dv_amt, "
			+ " CASE WHEN SUM(type_amt) IS NULL THEN 0 ELSE SUM(type_amt) END AS type_amt, "
			+ " CASE WHEN ROUND(((sum(type_count) / :totDvCt) * 100), 2) IS NULL THEN 0 ELSE ROUND(((sum(type_count)/ :totDvCt) * 100), 2) END AS countper,"
			+ " CASE WHEN ROUND(((sum(type_amt) / :totDvAmt) * 100), 2) IS NULL THEN 0 ELSE ROUND(((sum(type_amt) / :totDvAmt) * 100), 2) END AS amtPer "
			+ " FROM t_app_module_summary WHERE rule_group_id = :groupId AND (file_date >= :fmDate  AND file_date <= :toDate) and view_id =:viewId AND type =:type group by view_id, file_date, type)f group by type",nativeQuery=true)
	List<Object[]> fetchAccountingInfoFromAndToDateAndTypeAndViewId(@Param("groupId") Long groupId,@Param("fmDate") LocalDate fmDate,
			@Param("toDate") LocalDate toDate,@Param("type") String type,@Param("viewId") Long viewId,@Param("totDvCt") BigInteger totDvCt,@Param("totDvAmt") BigInteger totDvAmt );	
*/
	
	
	
	@Query(value = " select type,case when :totDvCt is null then 0 else :totDvCt end as dvCount,"
			+ "case when sum(type_count) is null then 0 else sum(type_count) end as typeCount,"
			+ "case when :totDvAmt is null then 0 else :totDvAmt end as dvAmt,"
			+ "case when sum(type_amt) is null then 0 else sum(type_amt) end as typeAmt,"
			+ "CASE WHEN ROUND(((sum(type_count) / :totDvCt) * 100), 2) IS NULL THEN 0 ELSE ROUND(((sum(type_count)/ :totDvCt) * 100), 2) END AS countper,"
			+ "CASE WHEN ROUND(((sum(type_amt) / :totDvAmt) * 100), 2) IS NULL THEN 0 ELSE ROUND(((sum(type_amt) / :totDvAmt) * 100), 2) END AS amtPer  "
			+ "FROM t_app_module_summary WHERE rule_group_id = :groupId AND (file_date >= :fmDate  AND file_date <= :toDate) AND type =:type and view_id =:viewId group by type",nativeQuery=true)
	List<Object[]> fetchAccountingInfoFromAndToDateAndTypeAndViewId(@Param("groupId") Long groupId,@Param("fmDate") LocalDate fmDate,
			@Param("toDate") LocalDate toDate,@Param("type") String type,@Param("viewId") Long viewId,@Param("totDvCt") BigDecimal totDvCt,@Param("totDvAmt") BigDecimal totDvAmt );	
	
	
	
	/**for process board**/
	@Query(value = " select case when :totDvCt is null then 0 else :totDvCt end as dvCount,"
			+ "case when sum(type_count) is null then 0 else sum(type_count) end as typeCount,"
			+ "case when :totDvAmt is null then 0 else :totDvAmt end as dvAmt,"
			+ "case when sum(type_amt) is null then 0 else sum(type_amt) end as typeAmt,"
			+ "CASE WHEN ROUND(((sum(type_count) / :totDvCt) * 100), 2) IS NULL THEN 0 ELSE ROUND(((sum(type_count)/ :totDvCt) * 100), 2) END AS countper,"
			+ "CASE WHEN ROUND(((sum(type_amt) / :totDvAmt) * 100), 2) IS NULL THEN 0 ELSE ROUND(((sum(type_amt) / :totDvAmt) * 100), 2) END AS amtPer  "
			+ "FROM t_app_module_summary WHERE rule_group_id = :groupId AND (file_date >= :fmDate  AND file_date <= :toDate) AND type in (:type) and view_id =:viewId",nativeQuery=true)
	List<Object[]> fetchAccountingAndUnAccountingInfoFromAndToDateAndTypeAndViewId(@Param("groupId") Long groupId,@Param("fmDate") LocalDate fmDate,
			@Param("toDate") LocalDate toDate,@Param("type") List<String> type,@Param("viewId") Long viewId,@Param("totDvCt") BigDecimal totDvCt,@Param("totDvAmt") BigDecimal totDvAmt );
	
	
	
	/*
					@Query(value = "select type,case when (select sum(dv_count) from (select distinct(view_id), dv_count  FROM t_app_module_summary  where"
				             +" rule_group_id =:groupId and (file_date=:fmDate)) a) is null then 0 else (select sum(dv_count) from (select distinct(view_id), dv_count  FROM t_app_module_summary  where"
				             +" rule_group_id =:groupId and (file_date =:fmDate)) a) end as dvCount,"
									+ "case when sum(type_count) is null then 0 else sum(type_count) end as typeCount,"
									+ "case when (select sum(dv_amt) from (select distinct (view_id), dv_amt FROM t_app_module_summary where rule_group_id =:groupId and (file_date =:fmDate)) a) is null then 0 else "
									+ "(select sum(dv_amt) from (select distinct (view_id), dv_amt FROM t_app_module_summary where rule_group_id =:groupId and (file_date=:fmDate)) a) end as dvAmt,"
									+ "case when round(sum(type_amt)*100,2) is null then 0 else round(sum(type_amt)*100,2) end as typeAmt,"
									+ "case when round(sum(type_count)/(select sum(dv_count) from (select distinct(view_id), dv_count  FROM t_app_module_summary  where"
				             +" rule_group_id =:groupId and (file_date=:fmDate)) a) *100,2) is null then 0 else "
				             + "round(sum(type_count)/(select sum(dv_count) from (select distinct(view_id), dv_count  FROM t_app_module_summary  where"
				             +" rule_group_id =:groupId and (file_date =:fmDate)) a) *100,2) end as countPer,"
									+ "case when round(sum(type_amt)/(select sum(dv_amt) from (select distinct(view_id), dv_amt  FROM t_app_module_summary  where"
				             +" rule_group_id =:groupId and (file_date =:fmDate)) a) *100,2) is null then 0 else "
				             + "round(sum(type_amt)/(select sum(dv_amt) from (select distinct(view_id), dv_amt  FROM t_app_module_summary  where"
				             +" rule_group_id =:groupId and (file_date =:fmDate)) a) *100,2) end as amtPer "
									+ "from t_app_module_summary where rule_group_id =:groupId and file_date=:fmDate group by type",nativeQuery=true)
									List<Object[]> fetchAccountingAnalysisForADate(@Param("groupId") Long groupId,@Param("fmDate") LocalDate fmDate);
	 */

	/**dash board v3**/
	@Query(value = "select f.*, round(((type_count/dv_count)*100),2) as countper, round(((type_amt/dv_amt)*100),2) as amtPer from "
			+ "(select type, sum(dv_count) as dv_count, case when sum(type_count) is null then 0 else sum(type_count) end as type_count, "
			+ "case when sum(dv_amt) is null then 0 else sum(dv_amt) end as dv_amt, "
			+ "case when sum(type_amt) is null then 0 else sum(type_amt) end as type_amt "
			+ "from t_app_module_summary where rule_group_id = :groupId  AND file_date = :fmDate group by type ) f",nativeQuery=true)
	List<Object[]> fetchAccountingInfoForADate(@Param("groupId") Long groupId,@Param("fmDate") LocalDate fmDate);


	//for 7 days adding type filter
	/**dash board v3**/
	@Query(value = "select f.*, case when round(((type_count/dv_count)*100),2) is null then 0 else round(((type_count/dv_count)*100),2) end as countper, "
			+ "case when  round(((type_amt/dv_amt)*100),2) is null then 0 else round(((type_amt/dv_amt)*100),2) end as amtPer from "
			+ "(select type, case when sum(dv_count) is null then 0 else sum(dv_count) end as dv_count,"
			+ " case when sum(type_count) is null then 0 else sum(type_count) end as type_count, "
			+ "case when sum(dv_amt) is null then 0 else sum(dv_amt) end as dv_amt, "
			+ "case when sum(type_amt) is null then 0 else sum(type_amt) end as type_amt "
			+ "from t_app_module_summary where rule_group_id = :groupId  AND file_date = :fmDate and type =:type ) f",nativeQuery=true)
	List<Object[]> fetchAccountingInfoForADateAndType(@Param("groupId") Long groupId,@Param("fmDate") LocalDate fmDate,@Param("type") String type);
	
	/**dashboard v8 added viewId filter**/
	@Query(value = "select f.*, case when round(((type_count/dv_count)*100),2) is null then 0 else round(((type_count/dv_count)*100),2) end as countper, "
			+ "case when  round(((type_amt/dv_amt)*100),2) is null then 0 else round(((type_amt/dv_amt)*100),2) end as amtPer from "
			+ "(select type, case when sum(dv_count) is null then 0 else sum(dv_count) end as dv_count,"
			+ " case when sum(type_count) is null then 0 else sum(type_count) end as type_count, "
			+ "case when sum(dv_amt) is null then 0 else sum(dv_amt) end as dv_amt, "
			+ "case when sum(type_amt) is null then 0 else sum(type_amt) end as type_amt "
			+ "from t_app_module_summary where rule_group_id = :groupId  AND file_date = :fmDate and type =:type and view_id =:viewId) f",nativeQuery=true)
	List<Object[]> fetchAccountingInfoForADateAndTypeAndViewId(@Param("groupId") Long groupId,@Param("fmDate") LocalDate fmDate,
			@Param("type") String type,@Param("viewId") Long viewId);




	AppModuleSummary findByFileDateAndModule(LocalDate parse, String string);



	AppModuleSummary findByFileDateAndModuleAndRuleGroupId(LocalDate parse,
			String string, Long id);



	AppModuleSummary findByFileDateAndModuleAndRuleGroupIdAndRuleId(
			LocalDate parse, String string, Long id, Long valueOf);


	@Query(value =  "select * from t_app_module_summary where file_date =:fileDate and module =:module and rule_group_id=:groupId and rule_id =:ruleId and view_id=:viewId",nativeQuery=true)
	AppModuleSummary findByFileDateAndModuleAndRuleGroupIdAndRuleIdAndViewId(
			@Param("fileDate")	LocalDate fileDate,@Param("module") String module,@Param("groupId") Long groupId,@Param("ruleId") Long ruleId,@Param("viewId") Long viewId);



	AppModuleSummary findByFileDateAndModuleAndRuleGroupIdAndRuleIdAndViewIdAndType(
			LocalDate parse, String string, Long id, Long valueOf,
			Long valueOf2, String acct);







	/*	AppModuleSummary findByFileDateAndModuleAndRuleGroupIdAndRuleIdAndTypeAndViewId(
			LocalDate parse, String string2, Long id, Long longValue,
			Long longValue2);*/

	AppModuleSummary findByModuleAndRuleGroupIdAndRuleIdAndTypeAndViewId(String module, Long ruleGroupId, Long ruleId, String type, Long viewId);

	/**dash board v2**/
	@Query(value =  "SELECT case when sum(dv_count) is null then 0 else sum(dv_count) end totalDVCount,"
			+ "case when sum(type_count) is null then 0 else sum(type_count) end as totalTypeCount,"
			+ "case when round(sum(type_count)/sum(dv_count) *100,2) is null then 0 else round(sum(type_count)/sum(dv_count) *100,2) end as reconciledPer,"
			+ "case when round(100-(sum(type_count)/sum(dv_count) * 100),2) is null then 0 else round(100-(sum(type_count)/sum(dv_count) * 100),2) end as unRecPer,"
			+ "case when sum(dv_amt) is null then 0 else sum(dv_amt) end as dvAmt,"
			+ "case when sum(type_amt) is null then 0 else sum(type_amt) end as typeAmt,"
			+ "case when round(sum(type_amt)/sum(dv_amt) *100,2) is null then 0 else round(sum(type_amt)/sum(dv_amt) *100,2) end as reconcileAmtPer,"
			+ "case when round(100-(sum(type_amt)/sum(dv_amt) * 100),2) is null then 0 else round(100-(sum(type_amt)/sum(dv_amt) * 100),2) end as unRecAmtPer"
			+ " FROM "
			+ "t_app_module_summary where rule_group_id =:groupId and file_date>=:fmDate and file_date<=:toDate",nativeQuery=true)
	List<Object[]> fetchReconCountAndUnReconciledCountForWeek(@Param("groupId") Long groupId,@Param("fmDate") LocalDate fmDate,@Param("toDate") LocalDate toDate);



	/*@Query(value =  "SELECT case when sum(dv_count) is null then 0 else sum(dv_count) end totalDVCount,"
			+ "case when sum(type_count) is null then 0 else sum(type_count) end as totalTypeCount,"
			+ "case when round(sum(type_count)/sum(dv_count) *100,2) is null then 0 else round(sum(type_count)/sum(dv_count) *100,2) end as reconciledPer,"
			+ "case when round(100-(sum(type_count)/sum(dv_count) * 100),2) is null then 0 else round(100-(sum(type_count)/sum(dv_count) * 100),2) end as unRecPer,"
			+ "case when sum(dv_amt) is null then 0 else sum(dv_amt) end as dvAmt,"
			+ "case when sum(type_amt) is null then 0 else sum(type_amt) end as typeAmt,"
			+ "case when round(sum(type_amt)/sum(dv_amt) *100,2) is null then 0 else round(sum(type_amt)/sum(dv_amt) *100,2) end as reconcileAmtPer,"
			+ "case when round(100-(sum(type_amt)/sum(dv_amt) * 100),2) is null then 0 else round(100-(sum(type_amt)/sum(dv_amt) * 100),2) end as unRecAmtPer"
			+ " FROM "
			+ "t_app_module_summary where rule_group_id =:groupId and file_date=:fmDate",nativeQuery=true)
	List<Object[]> fetchReconCountAndUnReconciledCountForADate(@Param("groupId") Long groupId,@Param("fmDate") LocalDate fmDate);*/



	@Query(value = "select distinct file_date from t_app_module_summary where module=:module and rule_group_id=:groupId and file_date is not null",nativeQuery=true)
	List<Date> findDistinctFileDateByModuleAndRuleGroupId(@Param("module") String module,
			@Param("groupId") Long groupId);


	AppModuleSummary findByFileDateAndModuleAndRuleGroupIdAndViewIdAndTypeAndRuleIdIsNull(LocalDate fileDate, String module, Long ruleGroupId, Long viewId, String type);


	List<AppModuleSummary> findByModuleAndRuleGroupIdAndViewIdAndType(String module, Long ruleGroupId, Long viewId, String type);


	@Query(value = "SELECT rule_id,type FROM t_app_module_summary where rule_group_id=:groupId and view_id=:viewId "
			+ "and (file_date>=:fDate and file_date<=:tDate) group by rule_id,type",nativeQuery=true)
	List<Object[]> findRuleIdAndTypeIdByRuleGroupIdAndViewId(@Param("groupId") Long groupId,
			@Param("viewId")Long viewId,@Param("fDate") LocalDate fDate,@Param("tDate") LocalDate tDate);



	@Query(value = "SELECT rule_id FROM t_app_module_summary where rule_group_id=:groupId and view_id=:viewId and rule_id is not null "
			+ "and (file_date>=:fDate and file_date<=:tDate) group by rule_id",nativeQuery=true)
	List<BigInteger> findRuleIdByRuleGroupIdAndViewId(@Param("groupId") Long groupId,
			@Param("viewId")Long viewId,@Param("fDate") LocalDate fDate,@Param("tDate") LocalDate tDate);

	/*@Query(value = "select *, (t.type_count/d.dv_count)*100 as countPer, (t.type_amt/d.dv_amt)*100 as amtPer , 100-(t.type_count/d.dv_count)*100 as unreconCountPer, 100-(t.type_amt/d.dv_amt)*100 as unReconAmtPer from "
			+" (select rule_id as r1, view_id as v1, type as t1, sum(dv_count) as dv_count , sum(dv_amt) as dv_amt from t_app_module_summary where rule_group_id= :ruleGroupId and rule_id= :ruleId group by rule_id, t1, view_id ) d "  
			+"	join (select rule_id as r2,view_id as v2, type as t2, sum(type_count) as type_count, sum(type_amt) as type_amt from t_app_module_summary where rule_group_id= :ruleGroupId and rule_id= :ruleId group by rule_id, t2, view_id ) t "
			+" where d.v1 = t.v2",nativeQuery=true)
	List<Object[]> fetchAmountByRuleNViews(@Param("ruleGroupId") Long ruleGroupId, @Param("ruleId") Long ruleId);*/


	/**dashboard v3**/
	@Query(value = "select *, case when ROUND(((t.type_count / d.dv_count) * 100), 2) is null then 0 else ROUND(((t.type_count / d.dv_count) * 100), 2) end AS countPer,"
			+ "case when ROUND(((t.type_amt / d.dv_amt) * 100), 2) is null then 0 else ROUND(((t.type_amt / d.dv_amt) * 100), 2) end AS amtPer , "
			+"case when round((100-(t.type_count/d.dv_count)*100),2) is null then 0 else ROUND((100 - (t.type_count / d.dv_count) * 100),2) end AS unreconCountPer,  "
			+ "case when ROUND((100 - (t.type_amt / d.dv_amt) * 100), 2) is null then 0 else ROUND((100 - (t.type_amt / d.dv_amt) * 100), 2) end AS unReconAmtPer,"
			+"case when ROUND(((t.approval_count / d.dv_count) * 100),2) is null then 0 else ROUND(((t.approval_count / d.dv_count) * 100),2) end AS approvalPer, "
			+ "(d.dv_count-t.approval_count) as unApprovedCount, "
			+"case when ROUND((100 - (t.approval_count / d.dv_count) * 100),2) is null then 0 else ROUND((100 - (t.approval_count / d.dv_count) * 100),2) end AS unappctPer"
			+" from  (select rule_id as r1,  view_id as v1,  type as t1,  case when sum(dv_count) is null then 0 else round(sum(dv_count),2) end as dv_count ," 
			+" case when sum(dv_amt) is null  then 0 else round(sum(dv_amt),2) end as dv_amt from t_app_module_summary where rule_group_id= :ruleGroupId and rule_id= :ruleId and "
			+" file_date>= :fmDate and file_date<= :toDate group by rule_id, t1, view_id ) d " 
			+" join  (select view_id as v2, type as t2,  round(sum(type_count),2) as type_count, round(sum(type_amt),2) as type_amt, "
			+"case when ROUND(SUM(approval_count), 2) is null then 0 else ROUND(SUM(approval_count), 2) end as approval_count"
			+" from t_app_module_summary where rule_group_id= :ruleGroupId and file_date>= :fmDate and file_date<= :toDate group by t2, view_id ) t where d.v1 = t.v2 ",nativeQuery=true)
	List<Object[]> fetchAmountByRuleNViews(@Param("ruleGroupId") Long ruleGroupId, @Param("ruleId") Long ruleId, @Param("fmDate") LocalDate fmDate, @Param("toDate") LocalDate toDate);


	/**dashboard v3**/
	@Query(value = "select *, round((t.type_count/d.dv_count)*100,2) as countPer, round((t.type_amt/d.dv_amt)*100,2) as amtPer , 100-(round((t.type_count/d.dv_count)*100,2)) as unreconCntPer , 100-(round((t.type_amt/d.dv_amt)*100,2)) as unreconAmtPer from "
			+" (select rule_id r1, view_id v1, type t1, case when sum(dv_count) is null then 0 else round(sum(dv_count),2) end as dv_count , " 
			+" case when sum(dv_amt) is null then 0 else round(sum(dv_amt),2) end as dv_amt from t_app_module_summary "
			+" where rule_group_id= :ruleGroupId and type= :type and view_id= :viewId and rule_id= :ruleId and file_date>= :fmDate and file_date<= :toDate group by r1, t1, v1 ) d " 
			+" join (select rule_id r2, view_id v2,  type t2, case when sum(type_count) is null then 0 else round(sum(type_count),2) end as type_count , " 
			+" case when sum(type_amt) is null then 0 else round(sum(type_amt),2) end as type_amt  from t_app_module_summary "
			+" where rule_group_id= :ruleGroupId and type= :type and view_id= :viewId and rule_id= :ruleId and file_date>= :fmDate and file_date<= :toDate group by r2, t2, v2 ) t where d.v1 = t.v2 ",nativeQuery=true)
	List<Object[]> fetchRulewiseSrcTgtComboReconInfo(@Param("ruleGroupId") Long ruleGroupId,@Param("viewId") Long viewId, @Param("type") String type, @Param("ruleId") Long ruleId ,@Param("fmDate") LocalDate fmDate, @Param("toDate") LocalDate toDate);



	@Query(value = "SELECT DISTINCT rule_group_id FROM t_app_module_summary where rule_group_id in (:ruleGrpIds) and module =:module AND file_date >=:fDate AND file_date <=:tDate",nativeQuery=true)
	List<BigInteger> findDistinctRuleGroupIdByModuleAndFileDateBetween(@Param("ruleGrpIds")
	List<BigInteger> ruleGrpIds,@Param("module") String module,@Param("fDate") LocalDate fDate,@Param("tDate")
	LocalDate tDate);
	
	
	/**dashboard v8**/
	/*@Query(value =  "SELECT rule_group_id, round(SUM(countPer),2) as reconciledPer , 100-round(SUM(countPer),2) as unRecPer, round(SUM(amtPer),2) reconcileAmtPer,"
			+ " 100-round(SUM(amtPer),2) as unRecAmtPer, CASE WHEN  (sum(dvAmt)-sum(recAmt)) is NULL then 0 else  (sum(dvAmt)-sum(recAmt)) end as unRecAmt, "
			+ "  sum(appCt),sum(appCtper), "
			+ " CASE WHEN SUM(recAmt) IS NULL THEN 0 ELSE SUM(recAmt) end as recAmt,"
			+ " CASE WHEN (sum(dvCt) - SUM(recCt)) IS NULL THEN 0 ELSE (sum(dvCt) - SUM(recCt)) END AS unRecCt,"
			+ " CASE WHEN SUM(recCt) IS NULL THEN 0 ELSE SUM(recCt) END AS recCt "
			+ "FROM (SELECT rule_group_id, "
			+ "rule_id, SUM(countPer) AS countPer, SUM(amtper) AS amtPer,      sum(dv_count)  as dvCt, sum(dv_amt)  as dvAmt, sum(recon_count)  as recCt,"
			+ "sum(recon_amt)  as recAmt,  SUM(approval_count) AS appCt,SUM(initiatedCount) AS initiatedCount   FROM (SELECT rule_group_id, rule_id, view_id, type, "
			+ "case when AVG(dv_count) is null then 0 else AVG(dv_count) end AS dv_count, case when SUM(type_count) is null then 0 else SUM(type_count) end AS recon_count, "
			+ "case when AVG(dv_amt) is null then 0 else AVG(dv_amt) end AS dv_amt, case when SUM(type_amt) is null then 0 else SUM(type_amt) end AS recon_amt, "
			+ "case when round((SUM(type_count) / AVG(dv_count)) * 100,2) is null then 0 else round((SUM(type_count) / AVG(dv_count)) * 100,2) end AS countPer,"
			+ " case when round( (SUM(type_amt) / AVG(dv_amt)) * 100,2) is null then 0 else round( (SUM(type_amt) / AVG(dv_amt)) * 100,2) end AS amtPer,"
			+ "        CASE WHEN sum(approval_count) IS NULL THEN 0 ELSE sum(approval_count)  END AS approval_count, "
			+ " CASE WHEN round((sum(approval_count) / AVG(dv_count)) * 100,2) IS NULL THEN 0 ELSE round((sum(approval_count) / AVG(dv_count)) * 100,2) END AS appCountPer"
			+ " CASE WHEN SUM(initiated_count) IS NULL THEN 0 ELSE SUM(initiated_count) END AS initiatedCount,"
			+ "CASE WHEN SUM(approval_count) IS NULL THEN 0 ELSE SUM(approval_count) END AS approval_count"
			+ "FROM t_app_module_summary WHERE rule_group_id =:groupId and file_date >= :fDate and file_date <=:tDate and view_id=:viewId GROUP BY rule_group_id , "
			+ "rule_id , view_id , type) d GROUP BY rule_group_id , rule_id) p GROUP BY rule_group_id",nativeQuery=true)*/
	
	
	
	/*@Query(value =  "SELECT rule_group_id, ROUND(SUM(countPer), 2) AS reconciledPer,100 - ROUND(SUM(countPer), 2) AS unRecPer,ROUND(SUM(amtPer), 2) reconcileAmtPer,100 - ROUND(SUM(amtPer), 2) AS unRecAmtPer,"
			+ " CASE WHEN (avg(dvAmt) - SUM(recAmt)) IS NULL THEN 0 ELSE (avg(dvAmt) - SUM(recAmt)) END AS unRecAmt,"
			+ "SUM(appCt),"
			+ "SUM(initiatedCount),"
			+ "CASE WHEN SUM(recAmt) IS NULL THEN 0 ELSE SUM(recAmt) END AS recAmt,"
			+ "CASE WHEN (AVG(dvCt) - SUM(recCt)) IS NULL THEN 0 ELSE (AVG(dvCt) - SUM(recCt)) END AS unRecCt,"
			+ "CASE WHEN SUM(recCt) IS NULL THEN 0 ELSE SUM(recCt) END AS recCt, "
			+ "CASE WHEN SUM(initiatedCount) - SUM(appCt) IS NULL THEN 0 ELSE SUM(initiatedCount) - SUM(appCt) END AS awaitingCount, "
			+ "CASE WHEN ROUND(((SUM(initiatedCount) - SUM(appCt)) / SUM(initiatedCount)) * 100, 2) IS NULL THEN 0 "
			+ "ELSE ROUND(((SUM(initiatedCount) - SUM(appCt)) / SUM(initiatedCount)) * 100, 2) END AS awaitingCountPer "
			+ "FROM (SELECT rule_group_id, rule_id, SUM(countPer) AS countPer, SUM(amtper) AS amtPer, "
			+ "SUM(dv_count) AS dvCt, SUM(dv_amt) AS dvAmt, SUM(recon_count) AS recCt, SUM(recon_amt) AS recAmt, SUM(approval_count) AS appCt, SUM(initiatedCount) AS initiatedCount "
			+ " FROM  (SELECT  rule_group_id, rule_id, view_id, type, CASE WHEN sum(dv_count) IS NULL THEN 0 ELSE sum(dv_count) END AS dv_count, "
			+ "CASE WHEN SUM(type_count) IS NULL THEN 0 ELSE SUM(type_count) END AS recon_count, CASE WHEN AVG(dv_amt) IS NULL THEN 0 ELSE AVG(dv_amt) END AS dv_amt, "
			+ "CASE WHEN SUM(type_amt) IS NULL THEN 0 ELSE SUM(type_amt) END AS recon_amt, "
			+ "CASE WHEN ROUND((SUM(type_count) / sum(dv_count)) * 100, 2) IS NULL THEN 0 ELSE ROUND((SUM(type_count) / sum(dv_count)) * 100, 2) END AS countPer,"
			+ "CASE WHEN ROUND((SUM(type_amt) / sum(dv_amt)) * 100, 2) IS NULL THEN 0 ELSE ROUND((SUM(type_amt) / sum(dv_amt)) * 100, 2) END AS amtPer, "
			+ "CASE WHEN SUM(initiated_count) IS NULL THEN 0 ELSE SUM(initiated_count) END AS initiatedCount,"
			+ " CASE WHEN SUM(approval_count) IS NULL THEN 0 ELSE SUM(approval_count) END AS approval_count FROM t_app_module_summary WHERE rule_group_id =:groupId and file_date >= :fDate and file_date <=:tDate and view_id=:viewId GROUP BY rule_group_id , "
			+ "rule_id , view_id , type) d GROUP BY rule_group_id , rule_id) p GROUP BY rule_group_id",nativeQuery=true)*/
	
	
	
	
	
	
	
	@Query(value =  "SELECT rule_group_id, ROUND(SUM(countPer), 2) AS reconciledPer, 100 - ROUND(SUM(countPer), 2) AS unRecPer, ROUND(SUM(amtPer), 2) reconcileAmtPer, 100 - ROUND(SUM(amtPer), 2) AS unRecAmtPer,"
			+ "CASE WHEN ((select sum(dvAmt) from (SELECT file_date,count(distinct view_id),sum( distinct dv_amt) as dvAmt FROM t_app_module_summary WHERE rule_group_id =:groupId and file_date >= :fDate and file_date <=:tDate and view_id=:viewId group by file_date)f) - SUM(recAmt)) IS NULL THEN 0 "
			+ " ELSE ((select sum(dvAmt) from (SELECT file_date,count(distinct view_id),sum( distinct dv_amt) as dvAmt FROM t_app_module_summary WHERE rule_group_id =:groupId and file_date >= :fDate and file_date <=:tDate and view_id=:viewId group by file_date)f) - SUM(recAmt)) END AS unRecAmt, SUM(appCt), SUM(initiatedCount), "
			+ " CASE WHEN SUM(recAmt) IS NULL THEN 0 ELSE SUM(recAmt) END AS recAmt, CASE WHEN ((select sum(dvCount) from (SELECT file_date,count(distinct view_id),sum( distinct dv_count) as dvCount "
			+ "FROM t_app_module_summary WHERE rule_group_id =:groupId and file_date >= :fDate and file_date <=:tDate and view_id=:viewId group by file_date)f) - SUM(recCt)) IS NULL THEN 0 "
			+ "ELSE ((select sum(dvCount) from (SELECT file_date,count(distinct view_id),sum( distinct dv_count) as dvCount FROM t_app_module_summary WHERE rule_group_id =:groupId and file_date >= :fDate and file_date <=:tDate and view_id=:viewId group by file_date)f) - SUM(recCt)) END AS unRecCt, "
			+ "CASE WHEN SUM(recCt) IS NULL THEN 0 ELSE SUM(recCt) END AS recCt, CASE WHEN SUM(initiatedCount) - SUM(appCt) IS NULL THEN 0 ELSE SUM(initiatedCount) - SUM(appCt) END AS awaitingCount, "
			+ "CASE WHEN ROUND(((SUM(initiatedCount) - SUM(appCt)) / SUM(initiatedCount)) * 100, 2) IS NULL THEN 0 ELSE ROUND(((SUM(initiatedCount) - SUM(appCt)) / SUM(initiatedCount)) * 100, 2) END AS awaitingCountPer "
			+ "FROM (SELECT  rule_group_id, rule_id, SUM(countPer)  AS countPer, SUM(amtper)  AS amtPer, "
			+ "avg(dv_count) AS dvCt, avg(dv_amt) AS dvAmt,"
			+ " SUM(recon_count) AS recCt,  SUM(recon_amt) AS recAmt, SUM(approval_count) AS appCt, SUM(initiatedCount) AS initiatedCount, view_id FROM "
			+ "(SELECT  rule_group_id, rule_id,  view_id, type, CASE WHEN sum(dv_count) IS NULL THEN 0 ELSE sum(dv_count) END AS dv_count, "
			+ "CASE WHEN SUM(type_count) IS NULL THEN 0 ELSE SUM(type_count) END AS recon_count, CASE WHEN sum(dv_amt) IS NULL THEN 0 ELSE sum(dv_amt) END AS dv_amt, "
			+ "CASE WHEN SUM(type_amt) IS NULL THEN 0 ELSE SUM(type_amt) END AS recon_amt, "
			+ "CASE WHEN ROUND((SUM(type_count) / sum(dv_count)) * 100, 2) IS NULL THEN 0 ELSE ROUND((SUM(type_count) / sum(dv_count)) * 100, 2) END AS countPer, "
			+ "CASE WHEN ROUND((SUM(type_amt) / sum(dv_amt)) * 100, 2) IS NULL THEN 0 ELSE ROUND((SUM(type_amt) / sum(dv_amt)) * 100, 2) END AS amtPer,"
			+ " CASE WHEN SUM(initiated_count) IS NULL THEN 0 ELSE SUM(initiated_count) END AS initiatedCount, CASE WHEN SUM(approval_count) IS NULL THEN 0 ELSE SUM(approval_count) END AS approval_count "
			+ "FROM t_app_module_summary WHERE rule_group_id =:groupId and file_date >= :fDate and file_date <=:tDate and view_id=:viewId GROUP BY rule_group_id , rule_id , view_id , type) d GROUP BY rule_group_id , rule_id,view_id) p "
			+ "GROUP BY rule_group_id",nativeQuery=true)
	
	List< Object[]> fetchReconCountAndUnReconciledCountBetweenGivenDatesForARuleGroupAndViewId(@Param("groupId") Long groupId,@Param("fDate") LocalDate fDate,@Param("tDate") LocalDate tDate,@Param("viewId") Long viewId);


	
	/**dashboard v8**/
	
	@Query(value = "SELECT DISTINCT(currency_code) FROM t_app_module_summary where currency_code is not null and multi_currency is false and rule_group_id=:ruleGroupId and view_id=:viewId AND file_date >=:fDate AND file_date <=:tDate",nativeQuery=true)
	List<String> findByRuleGroupIdAndViewId(@Param("ruleGroupId") Long ruleGroupId,@Param("fDate") LocalDate fDate,@Param("tDate") LocalDate tDate,@Param("viewId") Long viewId);

	@Query(value = "SELECT DISTINCT(currency_code) FROM t_app_module_summary where currency_code is not null and multi_currency is false and rule_group_id=:ruleGroupId AND file_date >=:fDate AND file_date <=:tDate",nativeQuery=true)
	List<String> findDistinctCurrencyCodeByRuleGroupId(@Param("ruleGroupId") Long ruleGroupId,@Param("fDate") LocalDate fDate,@Param("tDate") LocalDate tDate);
	
	
	/**dashboard v4,v3,v8**/
	@Query(value =  "SELECT "
			+ "CASE WHEN SUM(initiated_count) - SUM(approval_count) IS NULL THEN 0 ELSE SUM(initiated_count) - SUM(approval_count) END AS awaitingCount,"
			+ "CASE WHEN ROUND(((SUM(initiated_count) - SUM(approval_count)) / SUM(initiated_count)) * 100,2) IS NULL THEN 0 ELSE "
			+ "ROUND(((SUM(initiated_count) - SUM(approval_count)) / SUM(initiated_count)) * 100,2) END AS awaitingCountPer,"
			+ "CASE WHEN SUM(initiated_count) IS NULL THEN 0 ELSE SUM(initiated_count) END AS initiatedCount,"
			+ " CASE WHEN SUM(approval_count) IS NULL THEN 0 ELSE SUM(approval_count) END AS appCt FROM t_app_module_summary WHERE"
			+ " rule_group_id = :groupId AND view_id = :viewId AND (file_date >= :fmDate AND file_date <=:toDate) AND type = 'accounted'",nativeQuery=true)
	List<Object[]> fetchAwaitingApprovalCountAndPer(@Param("groupId") Long groupId,@Param("viewId") Long viewId,@Param("fmDate") LocalDate fmDate,@Param("toDate") LocalDate toDate);


	List<AppModuleSummary> findByRuleGroupId(Long ruleGroupId);
	
	/**dashboard v8**/
	@Query(value = "SELECT  view_id, file_date, type, CASE WHEN avg(dv_count) IS NULL THEN 0 ELSE avg(dv_count) END AS dv_count,"
			+ " CASE WHEN SUM(type_count) IS NULL THEN 0 ELSE SUM(type_count) END AS type_count, "
			+ " CASE WHEN avg(dv_amt) IS NULL THEN 0 ELSE avg(dv_amt) END AS dv_amt, "
			+ " CASE WHEN SUM(type_amt) IS NULL THEN 0 ELSE SUM(type_amt) END AS type_amt, "
			+ " CASE WHEN ROUND(((sum(type_count) / avg(dv_count)) * 100), 2) IS NULL THEN 0 ELSE ROUND(((sum(type_count)/ avg(dv_count)) * 100), 2) END AS countper,"
			+ " CASE WHEN ROUND(((sum(type_amt) / avg(dv_amt)) * 100), 2) IS NULL THEN 0 ELSE ROUND(((sum(type_amt) / avg(dv_amt)) * 100), 2) END AS amtPer "
			+ " FROM t_app_module_summary WHERE rule_group_id = :groupId AND (file_date >= :fmDate  AND file_date <= :toDate) AND type =:type group by view_id, file_date, type",nativeQuery=true)
	List<Object[]> fetchAccountingViewSpecificInfoFromAndToDateAndTypeAndViewId(@Param("groupId") Long groupId,@Param("fmDate") LocalDate fmDate,
			@Param("toDate") LocalDate toDate,@Param("type") String type);	
	
}



