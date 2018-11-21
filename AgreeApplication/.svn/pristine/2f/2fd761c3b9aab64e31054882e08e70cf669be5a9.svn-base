package com.nspl.app.web.rest;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.lang.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.config.ApplicationContextProvider;
import com.nspl.app.domain.DataViews;
import com.nspl.app.domain.DataViewsColumns;
import com.nspl.app.domain.FileTemplateLines;
import com.nspl.app.domain.LookUpCode;
import com.nspl.app.domain.ProcessDetails;
import com.nspl.app.domain.ReconciliationResult;
import com.nspl.app.domain.RuleGroup;
import com.nspl.app.domain.Rules;
import com.nspl.app.repository.AccountedSummaryRepository;
import com.nspl.app.repository.AccountingDataRepository;
import com.nspl.app.repository.AppModuleSummaryRepository;
import com.nspl.app.repository.ApprovalRuleAssignmentRepository;
import com.nspl.app.repository.BucketDetailsRepository;
import com.nspl.app.repository.DataViewsColumnsRepository;
import com.nspl.app.repository.DataViewsRepository;
import com.nspl.app.repository.FileTemplateLinesRepository;
import com.nspl.app.repository.LookUpCodeRepository;
import com.nspl.app.repository.NotificationBatchRepository;
import com.nspl.app.repository.ReconciliationResultRepository;
import com.nspl.app.repository.RuleGroupDetailsRepository;
import com.nspl.app.repository.RuleGroupRepository;
import com.nspl.app.repository.RulesRepository;
import com.nspl.app.service.DashBoardV2Service;
import com.nspl.app.service.DashBoardV3Service;
import com.nspl.app.service.DashBoardV4Service;
import com.nspl.app.service.FileExportService;
import com.nspl.app.service.ReconciliationResultService;
import com.nspl.app.service.UserJdbcService;


@RestController
@RequestMapping("/api")
public class DashBoardResourceV8 {
	private final Logger log = LoggerFactory.getLogger(DashBoardResourceV8.class);
	
	
	
	
	@Inject
	RuleGroupRepository ruleGroupRepository;


	@Inject
	UserJdbcService userJdbcService;

	@Inject
	AppModuleSummaryRepository appModuleSummaryRepository;


	@Inject
	LookUpCodeRepository lookUpCodeRepository;

	@Inject
	DashBoardV4Service dashBoardV4Service;


	@Inject
	RuleGroupDetailsRepository ruleGroupDetailsRepository;


	@Inject
	RulesRepository rulesRepository;


	@Inject
	DataViewsRepository dataViewsRepository;


	@Inject
	ReconciliationResultRepository reconciliationResultRepository;


	@Inject
	BucketDetailsRepository bucketDetailsRepository;
	
	
	@Inject
	AccountedSummaryRepository accountedSummaryRepository;
	
	
	@Inject
	ReconciliationResultService reconciliationResultService;
	
	
	@Inject
	DataViewsColumnsRepository dataViewsColumnsRepository;
	
	@Inject
	FileTemplateLinesRepository fileTemplateLinesRepository;
	
	@Inject
	NotificationBatchRepository notificationBatchRepository;
	
	
	@Inject
	ApprovalRuleAssignmentRepository approvalRuleAssignmentRepository;
	
	@Inject
	 AccountingDataRepository accountingDataRepository;
	
	@Inject
	DashBoardV3Service dashBoardV3Service;
	
	@Inject
	FileExportService fileExportService;
	
	
	@Inject
	DashBoardV2Service dashBoardV2Service;
	
	@PersistenceContext(unitName="default")
  	private EntityManager em;





	private DecimalFormat dform = new DecimalFormat("####0.00");
	
	@PostMapping("/getSummaryInfoForReconciliationV8")
	@Timed 
	public LinkedHashMap getSummaryInfoForReconciliationV8(HttpServletRequest request,@RequestBody HashMap dates,
			@RequestParam int violation,@RequestParam Long ruleGroupId,@RequestParam Long viewId,@RequestParam String viewType) 
	{
		log.info("Rest request to getSummaryInfoForReconciliationV2 for a ruleGroupId:"+ruleGroupId);
		HashMap tenMap=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(tenMap.get("tenantId").toString());
		LinkedHashMap finalMap=new LinkedHashMap();

		Long recRuleGrpId=0l;
		recRuleGrpId=ruleGroupId;
		log.info("recRuleGrpId :"+recRuleGrpId);
		ZonedDateTime fmDate=ZonedDateTime.parse(dates.get("startDate").toString());
		ZonedDateTime toDate=ZonedDateTime.parse(dates.get("endDate").toString());

		java.time.LocalDate fDate=fmDate.toLocalDate();
		java.time.LocalDate tDate=toDate.toLocalDate();
		List<String> rulesList=new ArrayList<String>();
		List<Long> rulesIdList=new ArrayList<Long>();


		List<BigInteger> finalSrcIdList=new ArrayList<BigInteger>();
		List<BigInteger> reconciliedSrcIds=reconciliationResultRepository.fetchReconciledSourceIds(tenantId, recRuleGrpId, viewId);	 
		finalSrcIdList.addAll(reconciliedSrcIds);
		List<BigInteger> reconciliedTrgIds=reconciliationResultRepository.fetchReconciledTargetIds(tenantId, recRuleGrpId, viewId);	 
		finalSrcIdList.addAll(reconciliedTrgIds);

		DataViews dv=dataViewsRepository.findOne(viewId);
		Double totalUnRecCount=0d;

		Connection conn = null;
		Statement stmtDv = null;
		ResultSet resultDv=null;


		String amountQualifier=reconciliationResultService.getViewColumnQualifier(BigInteger.valueOf(viewId), "AMOUNT");
		log.info("amountQualifier :"+amountQualifier);



		/**to get fileDate or qualifier Date**/

		String date=dashBoardV4Service.getFileDateOrQualifier(dv.getId(), tenantId);

		try
		{
			DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
			conn = ds.getConnection();

			log.info("Connected database successfully...");
			stmtDv = conn.createStatement();



			String query="";

			if(finalSrcIdList!=null && !finalSrcIdList.isEmpty())
			{
				String finalSrcIds=finalSrcIdList.toString().replaceAll("\\[", "").replaceAll("\\]", "");

				query="select DATEDIFF( SYSDATE(), `v`.`"+date+"`) as `rule_age`,count(scrIds),sum(`"+amountQualifier+"`) as `"+amountQualifier+"` from `"+dv.getDataViewName().toLowerCase().toLowerCase()+"` v where "+date+" >= '"+fDate+"' and "+date+"<='"+tDate+"' "
						+ " and scrIds not in ("+finalSrcIds+")group by rule_age";
				//log.info("query in if  :"+query);

			}
			else
			{
				query="select DATEDIFF( SYSDATE(), `v`.`"+date+"`) as `rule_age`,count(scrIds),sum(`"+amountQualifier+"`) as `"+amountQualifier+"` from `"+dv.getDataViewName().toLowerCase().toLowerCase()+"` v where "+date+" >= '"+fDate+"' and "+date+"<='"+tDate+"' "
						+ "group by rule_age";
				//log.info("query in else  :"+query);
			}



			resultDv=stmtDv.executeQuery(query);
			ResultSetMetaData rsmd2 = resultDv.getMetaData();
			int columnCount = rsmd2.getColumnCount();

			Long violationCount=0l;

			log.info("violation :"+violation);

			while(resultDv.next())
			{
				log.info("resultDv.getString(rule_age) :"+resultDv.getString("rule_age"));
				totalUnRecCount=	totalUnRecCount+Double.valueOf(resultDv.getString("count(scrIds)"));

				int ruleAge=Integer.valueOf(resultDv.getString("rule_age").toString());

				// int ruleAge=1;

				log.info("ruleAge :"+ruleAge+" and violation :"+violation);
				if(ruleAge>=violation)
				{
					//log.info("if rule age is greater than violation");
					violationCount=violationCount+Long.valueOf(resultDv.getString("count(scrIds)").toString());
				}


			}

			BigDecimal totalDvCount = BigDecimal.ZERO;
			BigDecimal totalDvAmt=BigDecimal.ZERO;
			if(viewId!=null && viewType !=null)
			{
				totalDvCount=appModuleSummaryRepository.fetchTotalDvcountForReconGroupByViewIdAndType(recRuleGrpId, fDate, tDate,viewId,viewType);
				totalDvAmt=appModuleSummaryRepository.fetchTotalDvAmtForReconGroupByViewIdAndType(recRuleGrpId, fDate, tDate,viewId,viewType);
			}

			List< Object[]> reconSummaryForUnItemsByValue=appModuleSummaryRepository.fetchReconciledAndUnReconciledAmountsAndCountsByViewIdAndType(recRuleGrpId, fDate, tDate,totalDvCount,totalDvAmt,viewId,viewType);

			//List< Object[]> reconSummaryForUnItemsByValue=appModuleSummaryRepository.fetchReconCountAndUnReconciledCountBetweenGivenDatesForARuleGroupAndViewId(recRuleGrpId,fDate,tDate,viewId);
			if(reconSummaryForUnItemsByValue.size()>0)
			{
				finalMap.put("unReconItemsValue", reconSummaryForUnItemsByValue.get(0)[7]);
				finalMap.put("unReconItemsValuePer", reconSummaryForUnItemsByValue.get(0)[8]);
				finalMap.put("unReconItemsCount",reconSummaryForUnItemsByValue.get(0)[3]);
				finalMap.put("reconItemsCount",reconSummaryForUnItemsByValue.get(0)[1]);
				finalMap.put("reconItemsCountPer",reconSummaryForUnItemsByValue.get(0)[2]);
				finalMap.put("unReconItemsCountPer",reconSummaryForUnItemsByValue.get(0)[4]);


				/*List<ReconciliationResult> ReconResultFinalStatusNotNull=reconciliationResultRepository.findByRuleGroupIdAndViewIdAndFinalStatusNotNull(recRuleGrpId.longValue(), viewId);
			log.info("ReconResult :"+ReconResultFinalStatusNotNull.size());
			Double totalApprovalsCt=Double.valueOf(ReconResultFinalStatusNotNull.size());

			List<ReconciliationResult> ReconResultApprovalInProcess=reconciliationResultRepository.findByRuleGroupIdAndViewIdAndFinalStatusInProcess(recRuleGrpId.longValue(), viewId);
			log.info("ReconResultApprovalInProcess :"+ReconResultApprovalInProcess.size());
			Double awaitingAppCount=Double.valueOf(ReconResultApprovalInProcess.size());

			Double awaitingAppCountPer=0d;
			if(totalApprovalsCt>0)
				awaitingAppCountPer=(awaitingAppCount/totalApprovalsCt)*100;*/

				finalMap.put("awaitingAppCount", reconSummaryForUnItemsByValue.get(0)[9]);
				if(Long.valueOf(reconSummaryForUnItemsByValue.get(0)[9].toString())>0)
					finalMap.put("awaitingAppCountPer",  reconSummaryForUnItemsByValue.get(0)[10]);
				else
					finalMap.put("awaitingAppCountPer",  0d);
			}
			else
			{
				finalMap.put("unReconItemsValue", 0d);
				finalMap.put("unReconItemsValuePer", 0d);
				finalMap.put("awaitingAppCount", 0d);
				finalMap.put("awaitingAppCountPer",  0d);
				finalMap.put("unReconItemsCount",0d);
				finalMap.put("reconItemsCount",0d);
				finalMap.put("reconItemsCountPer",0d);
				finalMap.put("unReconItemsCountPer",0d);
			}

			finalMap.put("rulesList", rulesList);
			finalMap.put("rulesIdList", rulesIdList);

			finalMap.put("unReconItemsViolation", violationCount);

			Double unReconItemsViolationPer=0d;
			if(totalUnRecCount>0)
			{
				unReconItemsViolationPer=((double)violationCount/totalUnRecCount)*100;
			}
			finalMap.put("unReconItemsViolationPer", Double.valueOf(dform.format(unReconItemsViolationPer)));
		}
		catch(Exception e)
		{
			log.info("Exception in getSummaryInfoForReconciliationV8 :"+e);
		}


		finally
		{
			if(resultDv!=null)
				try {
					resultDv.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					log.info("Exception while closing resultDv in getSummaryInfoForReconciliationV8 :"+e);
				}
			if(stmtDv!=null)
				try {
					stmtDv.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					log.info("Exception while closing stmtDv in getSummaryInfoForReconciliationV8 :"+e);
				}
			if(conn!=null)
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					log.info("Exception while closing conn in getSummaryInfoForReconciliationV8 :"+e);
				}
		}
		log.info("******end Time : "+ZonedDateTime.now()+"*******");
		return finalMap;
	}
	
	
	
	@PostMapping("/getSummaryInfoForAccountingV8")
	@Timed 
	public LinkedHashMap getSummaryInfoForAccountingV8(@RequestParam Long dvId ,@RequestBody HashMap dates,@RequestParam int violation,
			@RequestParam(value="ruleGroupId",required=false) Long ruleGroupId) 
	{
		log.info("getSummaryInfoForAccountingV2 of ruleGroupId "+ruleGroupId+" and dates"+dates);
		LinkedHashMap finalMap=new LinkedHashMap();
		List<LinkedHashMap> dataMap=new ArrayList<LinkedHashMap>();


		Long recRuleGrpId=0l;


		recRuleGrpId=ruleGroupId;

		ZonedDateTime fmDate=ZonedDateTime.parse(dates.get("startDate").toString());
		ZonedDateTime toDate=ZonedDateTime.parse(dates.get("endDate").toString());

		java.time.LocalDate fDate=fmDate.toLocalDate();
		java.time.LocalDate tDate=toDate.toLocalDate();
		List<String> rulesList=new ArrayList<String>();
		List<Long> rulesIdList=new ArrayList<Long>();
		Double totalActct=0d;
		Double totalActAmt=0d;
		Double totalNotActAmt=0d;
		Double totalNotActCt=0d;
		Double totalActInProcAmt=0d;
		Double totalActInProCt=0d;
		Double totalFinalAccountedCt=0d;
		Double totalFinalAccountedAmt=0d;
		Double dvCount=0d;
		Double dvAmt=0d;


		Double totalDvAmount=0d;
		Double totalDvCount=0d;


		List<Object[]> totalDvAmtAndDvCt=appModuleSummaryRepository.findToatlDvAmountAndDvCount(recRuleGrpId,fDate,tDate);
		for(int t=0;t<totalDvAmtAndDvCt.size();t++)
		{
			totalDvAmount=totalDvAmount+Double.valueOf(totalDvAmtAndDvCt.get(t)[1].toString());
			totalDvCount=totalDvCount+Double.valueOf(totalDvAmtAndDvCt.get(t)[2].toString());
		}



		//log.info("**totalDvAmount***** :"+totalDvAmount);
		//log.info("**totalDvCount***** :"+totalDvCount);
		List<String> acounted=new ArrayList<String>();
		acounted.add("accounted");
		Double totalTypeAmountAccounted=0d;
		Double totalTypeCountAccounted=0d;

		List<Object[]> totalTypeAmtAndTypeCtAct=appModuleSummaryRepository.findToatlTypeAmountAndTypeCountByType(recRuleGrpId,fDate,tDate,acounted);
		log.info("totalTypeAmtAndTypeCtActsize :"+totalTypeAmtAndTypeCtAct.size());
		for(int a=0;a<totalTypeAmtAndTypeCtAct.size();a++)
		{
			//log.info("totalTypeAmtAndTypeCtAct while loop :"+totalTypeAmtAndTypeCtAct);
			totalTypeAmountAccounted=totalTypeAmountAccounted+Double.valueOf(totalTypeAmtAndTypeCtAct.get(a)[1].toString());
			totalTypeCountAccounted=totalTypeCountAccounted+Double.valueOf(totalTypeAmtAndTypeCtAct.get(a)[2].toString());
		}

		List<String> notAcounted=new ArrayList<String>();
		notAcounted.add("Not accounted");
		notAcounted.add("Accounting inprocess");
		notAcounted.add("UN_ACCOUNTED");
		notAcounted.add("IN_PROCESS");

		Double totalTypeAmountNtAct=0d;
		Double totalTypeCountNtAct=0d;

		List<Object[]> totalTypeAmtAndTypeCtNtAct=appModuleSummaryRepository.findToatlTypeAmountAndTypeCountByType(recRuleGrpId,fDate,tDate,notAcounted);
		log.info("totalTypeAmtAndTypeCtNtActsize :"+totalTypeAmtAndTypeCtNtAct.size());
		for(int n=0;n<totalTypeAmtAndTypeCtNtAct.size();n++)
		{
			log.info("totalTypeAmountNtAct while loop :"+totalTypeAmountNtAct);
			totalTypeAmountNtAct=totalTypeAmountNtAct+Double.valueOf(totalTypeAmtAndTypeCtNtAct.get(n)[1].toString());
			totalTypeCountNtAct=totalTypeCountNtAct+Double.valueOf(totalTypeAmtAndTypeCtNtAct.get(n)[2].toString());
			log.info("totalTypeAmountNtAct end loop :"+totalTypeAmountNtAct);
		}

		List<String> finalAcounted=new ArrayList<String>();
		finalAcounted.add("Final accounted");
		finalAcounted.add("JOURNALS_ENTERED");

		Double totalTypeAmountfinalAct=0d;
		Double totalTypeCountfinalAct=0d;

		List<Object[]> totalTypeAmtAndTypeCtFinalAct=appModuleSummaryRepository.findToatlTypeAmountAndTypeCountByType(recRuleGrpId,fDate,tDate,finalAcounted);
		log.info("totalTypeAmtAndTypeCtFinalActsize :"+totalTypeAmtAndTypeCtFinalAct.size());
		for(int f=0;f<totalTypeAmtAndTypeCtFinalAct.size();f++)
		{
			//log.info("totalTypeAmountfinalAct while loop :"+totalTypeAmountfinalAct);
			totalTypeAmountfinalAct=totalTypeAmountfinalAct+Double.valueOf(totalTypeAmtAndTypeCtFinalAct.get(f)[1].toString());
			totalTypeCountfinalAct=totalTypeCountfinalAct+Double.valueOf(totalTypeAmtAndTypeCtFinalAct.get(f)[2].toString());
			//log.info("totalTypeAmountfinalAct end loop :"+totalTypeAmountfinalAct);
		}


		List<BigInteger> distViewsIds=new ArrayList<BigInteger>();
		if(dvId!=null)
			distViewsIds.add(BigInteger.valueOf(dvId));
		else
			distViewsIds=appModuleSummaryRepository.findDistinctViewIdByRuleGroupId(recRuleGrpId,fDate,tDate);
		log.info("distViewsIds :"+distViewsIds);
		Long violationCount=0l;
		//	Long totalViolationCount=0l;
		Long totalUnapprovedCount=0l;
		Long totalinitiatedCount=0l;
		Double totalUnAccountedCount=0d;
		for(BigInteger viewId:distViewsIds)
		{
			List<Object[]> awaitingApp=appModuleSummaryRepository.fetchAwaitingApprovalCountAndPer(recRuleGrpId, viewId.longValue(), fDate,tDate) ;                
			totalUnapprovedCount=totalUnapprovedCount+Long.valueOf(awaitingApp.get(0)[0].toString());
			totalinitiatedCount=totalinitiatedCount+Long.valueOf(awaitingApp.get(0)[2].toString());
			log.info("totalinitiatedCount :"+totalinitiatedCount);
			log.info("totalUnapprovedCount** :"+totalUnapprovedCount);

			LinkedHashMap map=new LinkedHashMap();
			map.put("viewId", viewId);
			log.info("view Id :"+viewId);
			List<Object[]> accountingSummary=appModuleSummaryRepository.fetchActCountsByGroupIdAndViewIdAndFileDate(Long.valueOf(recRuleGrpId),viewId.longValue(),fDate,tDate);
			DataViews dv=dataViewsRepository.findOne(viewId.longValue());
			map.put("labelValue", dv.getDataViewName());
			List<LinkedHashMap>  dvRuleDetailList=new ArrayList<LinkedHashMap>();

			List<BigInteger> ruleDetailsForView=appModuleSummaryRepository.findRuleIdByRuleGroupIdAndViewId(recRuleGrpId,viewId.longValue(),fDate,tDate);
			for(int r=0;r<ruleDetailsForView.size();r++)
			{
				LinkedHashMap dvRuleDetail=new LinkedHashMap();
				dvRuleDetail.put("ruleId", ruleDetailsForView.get(r));
				if(Long.valueOf(ruleDetailsForView.get(r).toString())==0)
					dvRuleDetail.put("ruleName", "Manual");
				else
				{
					Rules rule=rulesRepository.findOne(Long.valueOf(ruleDetailsForView.get(r).toString()));
					dvRuleDetail.put("ruleName",rule.getRuleCode());
				}
				// dvRuleDetail.put("type", ruleDetailsForView.get(r)[1]);
				dvRuleDetailList.add(dvRuleDetail);
			}
			map.put("dvRuleDetails", dvRuleDetailList);
			log.info("accountingSummary.size() :"+accountingSummary.size());
			for(int i=0;i<accountingSummary.size();i++)
			{
				/*log.info("dvCount :"+accountingSummary.get(0)[0].toString());
					log.info("dvAmt :"+accountingSummary.get(0)[8].toString());
					log.info("dvAmt at "+i+" is :"+dvAmt);
					log.info("dvCount at "+i+" is :"+dvCount);*/
				if(i==0)
				{
					dvCount=dvCount+Double.valueOf(accountingSummary.get(0)[0].toString());
					dvAmt=dvAmt+Double.valueOf(accountingSummary.get(0)[8].toString());
				}
				String capitalized = WordUtils.capitalizeFully(accountingSummary.get(i)[3].toString());
				log.info("capitalized :"+capitalized);
				map.put("dvCount", Double.valueOf(accountingSummary.get(i)[0].toString()));
				String process=capitalized.substring(0, 1).toLowerCase() + capitalized.substring(1);
				log.info("process :"+process);
				if(accountingSummary.get(i)[1]!=null)
					map.put(process.replaceAll("\\s", "")+"Count", Double.valueOf(accountingSummary.get(i)[1].toString()));
				else
					map.put(process.replaceAll("\\s", "")+"Count", 0);



				DataViewsColumns dvColumn=dataViewsColumnsRepository.findByDataViewIdAndQualifier(dv.getId(), "AMOUNT");
				String ammountQualifier="";
				if(dvColumn.getRefDvType()!=null && dvColumn.getRefDvType().equalsIgnoreCase("File Template"))
				{
					FileTemplateLines ftl=fileTemplateLinesRepository.findOne(Long.valueOf(dvColumn.getRefDvColumn()));
					ammountQualifier=ftl.getColumnAlias();
				}
				else
					ammountQualifier=dvColumn.getColumnName();

				/**to get fileDate or qualifier Date**/

				String date=dashBoardV4Service.getFileDateOrQualifier(dv.getId(), dv.getTenantId());


				map.put(process.replaceAll("\\s", "")+"per", Double.valueOf(accountingSummary.get(i)[2].toString()));
				map.put(process.replaceAll("\\s", "")+"amt", Double.valueOf(accountingSummary.get(i)[9].toString()));
				//	log.info("total dv amount :"+accountingSummary.get(i)[8].toString());
				Double amtPer=0d;
				if(Double.valueOf(accountingSummary.get(i)[8].toString())>0)
					amtPer=(Double.valueOf(accountingSummary.get(i)[9].toString())/Double.valueOf(accountingSummary.get(i)[8].toString()))*100;


				map.put(process.replaceAll("\\s", "")+"amtPer", Double.valueOf(dform.format(amtPer)));

				//	log.info("map.get(notAccountedCount) :"+map.get("notAccountedCount"));
				//	log.info("map.get(accountingInprocessCount) :"+map.get("accountingInprocessCount"));

				if(process.equalsIgnoreCase("Accounted"))
				{
					totalActAmt=totalActAmt+Double.valueOf(accountingSummary.get(i)[9].toString());
					totalActct=totalActct+Double.valueOf(accountingSummary.get(i)[1].toString());


					Double amountPer=(Double.valueOf(accountingSummary.get(i)[9].toString())/Double.valueOf(accountingSummary.get(i)[8].toString()))*100;

					LinkedHashMap acctMap=new LinkedHashMap();
					acctMap.put("amount", Double.valueOf(accountingSummary.get(i)[9].toString()));
					acctMap.put("count", Double.valueOf(accountingSummary.get(i)[1].toString()));
					//	log.info("totalTypeAmountAccounted :"+totalTypeAmountAccounted);
					//	log.info("Double.valueOf(accountingSummary.get(i)[9].toString()) :"+Double.valueOf(accountingSummary.get(i)[9].toString()));
					Double amountPerAct= 0d;
					if(totalTypeAmountAccounted>0)
						amountPerAct=((Double.valueOf(accountingSummary.get(i)[9].toString()))/totalTypeAmountAccounted)*100;
					acctMap.put("amountPer",  Double.valueOf(dform.format(amountPerAct)));
					Double countPerAct= 0d;
					if(totalTypeCountAccounted>0)
						countPerAct=((Double.valueOf(accountingSummary.get(i)[1].toString()))/totalTypeCountAccounted)*100;
					acctMap.put("countPer",Double.valueOf(dform.format(countPerAct)));
					map.put("accounted", acctMap);
				}

				if(process.equalsIgnoreCase("Final accounted") || process.equalsIgnoreCase("JOURNALS_ENTERED"))
				{
					Double amountPer=(Double.valueOf(accountingSummary.get(i)[9].toString())/Double.valueOf(accountingSummary.get(i)[8].toString()))*100;

					LinkedHashMap finalActMap=new LinkedHashMap();
					finalActMap.put("amount", Double.valueOf(accountingSummary.get(i)[9].toString()));
					finalActMap.put("count", Double.valueOf(accountingSummary.get(i)[1].toString()));
					Double amountPerFA= 0d;
					if(totalTypeAmountfinalAct>0)
						amountPerFA=	((Double.valueOf(accountingSummary.get(i)[9].toString()))/totalTypeAmountfinalAct)*100;
					finalActMap.put("amountPer",  Double.valueOf(dform.format(amountPerFA)));
					Double countPerFA= 0d;
					if(totalTypeCountfinalAct>0)
						countPerFA=	((Double.valueOf(accountingSummary.get(i)[1].toString()))/totalTypeCountfinalAct)*100;
					finalActMap.put("countPer", Double.valueOf(accountingSummary.get(i)[2].toString()));
					map.put("finalaccounted", finalActMap);
				}


				if(process.equalsIgnoreCase("Not Accounted") || process.equalsIgnoreCase("UN_ACCOUNTED"))
				{

					Double notAcctAndAcctInProcCt=0d;
					Double notAcctAndAcctInProcAmt=0d;

					Double notAcctAndAcctInProcCtPer=0d;
					Double notAcctAndAcctInProcAmtPer=0d;

					if(map.get("un_accountedCount")!=null)
					{
						if(map.get("in_processCount")!=null)
							notAcctAndAcctInProcCt=Double.valueOf(dform.format( Double.valueOf(map.get("un_accountedCount").toString())+Double.valueOf(map.get("in_processCount").toString())));
						else
							notAcctAndAcctInProcCt= Double.valueOf(dform.format(Double.valueOf(map.get("un_accountedCount").toString())));
					}

					if(map.get("un_accountedamt")!=null)
					{
						if(map.get("in_processamt")!=null)
							notAcctAndAcctInProcAmt=Double.valueOf(dform.format( Double.valueOf(map.get("un_accountedamt").toString())+Double.valueOf(map.get("in_processamt").toString())));
						else
							notAcctAndAcctInProcAmt= Double.valueOf(dform.format(Double.valueOf(map.get("un_accountedamt").toString())));
					}


					if(map.get("un_accountedper")!=null)
					{
						if(map.get("in_processper")!=null)
							notAcctAndAcctInProcCtPer=Double.valueOf(dform.format( Double.valueOf(map.get("un_accountedper").toString())+Double.valueOf(map.get("in_processper").toString())));
						else
							notAcctAndAcctInProcCtPer= Double.valueOf(dform.format(Double.valueOf(map.get("un_accountedper").toString())));
					}

					if(map.get("un_accountedamtPer")!=null)
					{
						if(map.get("in_processamtPer")!=null)
							notAcctAndAcctInProcAmtPer=Double.valueOf(dform.format( Double.valueOf(map.get("un_accountedamtPer").toString())+Double.valueOf(map.get("in_processamtPer").toString())));
						else
							notAcctAndAcctInProcAmtPer= Double.valueOf(dform.format(Double.valueOf(map.get("un_accountedamtPer").toString())));
					}
					if(map.get("finalAccountedCount")==null)
					{
						map.put("finalAccountedCount", 0d);
						map.put("finalAccountedper", 0d);
					}
					if(map.get("finalAccountedamt")==null)
					{
						map.put("finalAccountedamt", 0d);
						map.put("finalAccountedamtPer", 0d);
					}

					map.put("notAcctAndAcctInProcCt",Double.valueOf(notAcctAndAcctInProcCt));
					map.put("notAcctAndAcctInProcAmt",Double.valueOf(notAcctAndAcctInProcAmt));

					map.put("notAcctAndAcctInProcCtPer", Double.valueOf(notAcctAndAcctInProcCtPer));
					map.put("notAcctAndAcctInProcAmtPer",Double.valueOf(notAcctAndAcctInProcAmtPer));


					LinkedHashMap notAcctMap=new LinkedHashMap();
					notAcctMap.put("count", Double.valueOf(notAcctAndAcctInProcCt));
					notAcctMap.put("amount",Double.valueOf(notAcctAndAcctInProcAmt));



					Double amountPerNA= 0d;
					if(totalTypeAmountNtAct>0)
						amountPerNA=(Double.valueOf(notAcctAndAcctInProcAmt)/totalTypeAmountNtAct)*100;
					notAcctMap.put("amountPer",  Double.valueOf(dform.format(amountPerNA)));
					Double countPerNA=0d;
					if(totalTypeCountNtAct>0)
						countPerNA= (Double.valueOf(notAcctAndAcctInProcCt)/totalTypeCountNtAct)*100;
					notAcctMap.put("countPer",  Double.valueOf(dform.format(countPerNA)));
					map.put("notAccounted", notAcctMap);
				}


				Connection conn = null;
				Statement stmtDv = null;
				Statement stmtApp=null;
				ResultSet resultDv=null;
				ResultSet resultApp=null;
				try
				{

					DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
					conn = ds.getConnection();
					log.info("Connected database successfully...");
					stmtDv = conn.createStatement();
					stmtApp = conn.createStatement();


					String query="";
					if(process .equalsIgnoreCase("UN_ACCOUNTED") || process .equalsIgnoreCase("IN_PROCESS"))
					{
						totalNotActAmt=totalNotActAmt+Double.valueOf(accountingSummary.get(i)[9].toString());
						totalNotActCt=totalNotActCt+Double.valueOf(accountingSummary.get(i)[1].toString());
						List<BigInteger> finalSrcIdList=new ArrayList<BigInteger>();
						//only accounted
						List<BigInteger> accountedViewIds=accountedSummaryRepository.fetchAccountedRowIdsByRuleGrpIdAndViewId(recRuleGrpId, viewId.longValue()) ;
						finalSrcIdList.addAll(accountedViewIds);

						if(finalSrcIdList!=null && !finalSrcIdList.isEmpty())
						{
							String finalSrcIds=finalSrcIdList.toString().replaceAll("\\[", "").replaceAll("\\]", "");

							query="select DATEDIFF( SYSDATE(), `v`.`"+date+"`) as `rule_age`,count(scrIds),sum(`"+ammountQualifier+"`) as `"+ammountQualifier+"` from `"+dv.getDataViewName().toLowerCase().toLowerCase()+"` v where Date("+date+") >= '"+fDate+"' and Date("+date+")<='"+tDate+"' "
									+ " and scrIds not in ("+finalSrcIds+")group by rule_age";
							//log.info("in if query :"+query);

						}
						else
						{
							query="select DATEDIFF( SYSDATE(), `v`.`"+date+"`) as `rule_age`,count(scrIds),sum(`"+ammountQualifier+"`) as `"+ammountQualifier+"` from `"+dv.getDataViewName().toLowerCase().toLowerCase()+"` v where Date("+date+")>= '"+fDate+"' and Date("+date+")<='"+tDate+"' "
									+ "group by rule_age";
							//	log.info("in if else :"+query);
						}



						if(finalSrcIdList.size()>0)
						{
							resultDv=stmtDv.executeQuery(query);
							ResultSetMetaData rsmd2 = resultDv.getMetaData();
							int columnCount = rsmd2.getColumnCount();



							while(resultDv.next())
							{

								int ruleAge=Integer.valueOf(resultDv.getString("rule_age").toString());
								totalUnAccountedCount=	totalUnAccountedCount+Double.valueOf(resultDv.getString("count(scrIds)"));


								log.info("ruleAge"+ruleAge);
								if(ruleAge>=violation)
								{
									//log.info("if rule age is greater than violation");
									map.put("violationCount", Long.valueOf(resultDv.getString("count(scrIds)").toString()));
									violationCount=violationCount+Long.valueOf(resultDv.getString("count(scrIds)").toString());
									map.put("violationAmount",Double.valueOf(resultDv.getString(ammountQualifier).toString()));
								}
								else
								{
									map.put("violationAmount",0);
									map.put("violationCount", 0);
								}
							}

						}
						else
						{
							map.put("violationAmount",0);
							map.put("violationCount", 0);
						}

					}
					/*if(process .equalsIgnoreCase("accounted"))
					{
						log.info("accountingSummary.get(i)[9] :"+accountingSummary.get(i)[9]);

						Object[] awaitingApp=appModuleSummaryRepository.fetchAwaitingApprovalCountAndPer(recRuleGrpId, viewId.longValue(), fmDate, toDate) ;                


						//map.put("unApprovedCount", Double.valueOf(dform.format(Double.valueOf(accountingSummary.get(i)[6].toString()))));
						totalUnapprovedCount=totalUnapprovedCount+Long.valueOf(accountingSummary.get(i)[6].toString());
						List<BigInteger> finalApprovedSrcIds=accountedSummaryRepository.fetchDistinctApprovedRowIds(recRuleGrpId, viewId.longValue()) ;
						finalApprovedSrcIds.addAll(finalApprovedSrcIds);
						totalapprovedCount=totalapprovedCount+Long.valueOf(accountingSummary.get(i)[7].toString());


					}*/
					if(process .equalsIgnoreCase("Accounting Inprocess"))
					{
						totalActInProcAmt=totalActInProcAmt+Double.valueOf(accountingSummary.get(i)[9].toString());
						totalActInProCt=totalActInProCt+Double.valueOf(accountingSummary.get(i)[1].toString());
					}
					if(process .equalsIgnoreCase("Final Accounted"))
					{
						totalFinalAccountedCt=totalFinalAccountedCt+Double.valueOf(accountingSummary.get(i)[1].toString());
						totalFinalAccountedAmt=totalFinalAccountedAmt+Double.valueOf(accountingSummary.get(i)[9].toString());
					}

				}
				catch(Exception e)
				{
					log.info("Exception in getSummaryInfoForAccountingV8 while fetching data :"+e);
				}
				finally
				{
					if(resultDv!=null)
						try {
							resultDv.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							log.info("Exception while closing resultDv in getSummaryInfoForAccountingV8 :"+e);
						}
					if(resultApp!=null)
						try {
							resultApp.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							log.info("Exception while closing resultApp in getSummaryInfoForAccountingV8 :"+e);
						}
					if(stmtApp!=null)
						try {
							stmtApp.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							log.info("Exception while closing stmtApp in getSummaryInfoForAccountingV8 :"+e);
						}
					if(stmtDv!=null)
						try {
							stmtDv.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							log.info("Exception while closing stmtDv in getSummaryInfoForAccountingV8 :"+e);
						}
					if(conn!=null)
						try {
							conn.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							log.info("Exception while closing conn in getSummaryInfoForAccountingV8 :"+e);
						}
				}
			}

			dataMap.add(map);



		}
		//	log.info("totalapprovedCount :"+totalapprovedCount);
		//	log.info("totalUnapprovedCount :"+totalUnapprovedCount);
		double violationCountPer=0;
		if(totalUnAccountedCount>0)
		{
			violationCountPer =((double)violationCount/totalUnAccountedCount)*100;
		}
		double totalUnapprovedCountPer=0;
		if(totalinitiatedCount>0)
		{
			totalUnapprovedCountPer=((double)totalUnapprovedCount/totalinitiatedCount)*100;
		}
		//	log.info("totalUnapprovedCountPer :"+totalUnapprovedCountPer);
		Double totalNotActAmtPer=0d;
		if(totalDvAmount>0)
		{
			//	log.info("totalTypeAmountNtAct in if : "+totalTypeAmountNtAct);
			totalNotActAmtPer=Double.valueOf(dform.format(((totalTypeAmountNtAct)/totalDvAmount)*100));
		}
		log.info("rulesList :"+rulesList);
		finalMap.put("rulesList", rulesList);
		finalMap.put("rulesIdList", rulesIdList);
		finalMap.put("unAccountedItemsValue", totalTypeAmountNtAct);
		finalMap.put("unAccountedItemsValuePer", totalNotActAmtPer);
		finalMap.put("unAccountedItemsViolation",violationCount);
		finalMap.put("unAccountedItemsViolationPer",Double.valueOf(dform.format(violationCountPer)));
		log.info("totalUnapprovedCount :"+totalUnapprovedCount);
		finalMap.put("awaitingAppCount", totalUnapprovedCount);
		finalMap.put("awaitingAppCountPer", totalUnapprovedCountPer);
		finalMap.put("finalUnpostedCt", dvCount-totalFinalAccountedCt);
		finalMap.put("accountedCt", totalTypeCountAccounted);

		if(totalDvCount>0)
		{
			Double totalActCtPer=Double.valueOf(dform.format((totalTypeCountAccounted/totalDvCount)*100));
			finalMap.put("accountedCtPer", totalActCtPer);
		}
		else
			finalMap.put("accountedCtPer", 0);
		finalMap.put("accountedAmt", Double.valueOf(dform.format(totalTypeAmountAccounted)));
		log.info("totalActAmt :"+totalActAmt);
		log.info("totalDvAmount :"+totalDvAmount);
		if(totalDvAmount>0)
		{
			Double totalActAmtPer=Double.valueOf(dform.format((totalTypeAmountAccounted/totalDvAmount)*100));
			finalMap.put("accountedAmtPer", totalActAmtPer);
		}
		else
			finalMap.put("accountedAmtPer", 0);
		Double finalUnpostedCtPer=0d;
		if(dvCount>0)
		{
			finalUnpostedCtPer=Double.valueOf(dform.format(((dvCount-totalFinalAccountedCt)/dvCount)*100));
		}
		finalMap.put("finalUnpostedCtPer",finalUnpostedCtPer);
		finalMap.put("finalUnpostedAmt", dvAmt-totalFinalAccountedAmt);
		Double finalUnpostedAmtPer=0d;
		if(dvAmt>0)
		{
			finalUnpostedAmtPer=Double.valueOf(dform.format(((dvAmt-totalFinalAccountedAmt)/dvAmt)*100));
		}
		finalMap.put("finalUnpostedAmtPer", finalUnpostedAmtPer);
		finalMap.put("accountingData", dataMap);




		return finalMap;
	}




	

}
