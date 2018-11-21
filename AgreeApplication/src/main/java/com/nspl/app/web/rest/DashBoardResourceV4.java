package com.nspl.app.web.rest;

import java.io.IOException;
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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.ws.rs.core.MultivaluedHashMap;

import org.apache.commons.collections.MultiHashMap;
import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;







import com.codahale.metrics.annotation.Timed;
import com.nspl.app.config.ApplicationContextProvider;
import com.nspl.app.domain.AccountedSummary;
import com.nspl.app.domain.ApprovalRuleAssignment;
import com.nspl.app.domain.BucketDetails;
import com.nspl.app.domain.DataMaster;
import com.nspl.app.domain.DataViews;
import com.nspl.app.domain.DataViewsColumns;
import com.nspl.app.domain.FileTemplateLines;
import com.nspl.app.domain.LookUpCode;
import com.nspl.app.domain.NotificationBatch;
import com.nspl.app.domain.ProcessDetails;
import com.nspl.app.domain.Processes;
import com.nspl.app.domain.RuleGroup;
import com.nspl.app.repository.AccountedSummaryRepository;
import com.nspl.app.repository.AccountingDataRepository;
import com.nspl.app.repository.AppModuleSummaryRepository;
import com.nspl.app.repository.ApprovalRuleAssignmentRepository;
import com.nspl.app.repository.BucketDetailsRepository;
import com.nspl.app.repository.DataMasterRepository;
import com.nspl.app.repository.DataViewsColumnsRepository;
import com.nspl.app.repository.DataViewsRepository;
import com.nspl.app.repository.FileTemplateLinesRepository;
import com.nspl.app.repository.LookUpCodeRepository;
import com.nspl.app.repository.NotificationBatchRepository;
import com.nspl.app.repository.ProcessDetailsRepository;
import com.nspl.app.repository.ProcessesRepository;
import com.nspl.app.repository.ReconciliationResultRepository;
import com.nspl.app.repository.RuleGroupDetailsRepository;
import com.nspl.app.repository.RuleGroupRepository;
import com.nspl.app.repository.RulesRepository;
import com.nspl.app.service.DashBoardV4Service;
import com.nspl.app.service.FileExportService;
import com.nspl.app.service.ReconciliationResultService;
import com.nspl.app.service.UserJdbcService;


@RestController
@RequestMapping("/api")
public class DashBoardResourceV4 {

	private final Logger log = LoggerFactory.getLogger(DashBoardResourceV4.class);


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
	FileExportService fileExportService;
	
	@Inject
	ProcessesRepository processesRepository;
	
	@Inject
	ProcessDetailsRepository processDetailsRepository;
	
	@PersistenceContext(unitName="default")
  	private EntityManager em;





	private DecimalFormat dform = new DecimalFormat("####0.00");

	@PostMapping("/reconciliationRuleGroupSpecificInformationV4")
	@Timed
	public List<LinkedHashMap> reconciliationRuleGroupSpecificInformationV4(@RequestBody HashMap dates,HttpServletRequest request) throws SQLException, ParseException 
	{
		log.info("Rest Request to get aging analysis in service:"+dates);

		HashMap map0=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map0.get("tenantId").toString());

		ZonedDateTime fmDate=ZonedDateTime.parse(dates.get("startDate").toString());

		ZonedDateTime toDate=ZonedDateTime.parse(dates.get("endDate").toString());


		LocalDate fDate=fmDate.toLocalDate();

		LocalDate tDate=toDate.toLocalDate();


		List<LinkedHashMap> finallist=new ArrayList<LinkedHashMap>();
		
		List<LinkedHashMap> notReconciledList=new ArrayList<LinkedHashMap>();

		List<BigInteger> ruleGrpIds=ruleGroupRepository.fetchRuleGrpIdsByTenantIdAndRulePurpose(tenantId, "RECONCILIATION");
		log.info("ruleGrpIds :"+ruleGrpIds.size());

		if(ruleGrpIds.size()>0)
		{
		List<BigInteger> ruleGrpIdsAMS=appModuleSummaryRepository.findDistinctRuleGroupIdByModuleAndFileDateBetween(ruleGrpIds,"RECONCILIATION",fDate,tDate);

		log.info("ruleGrpIdsAMS :"+ruleGrpIdsAMS.size());
		for(BigInteger rgId:ruleGrpIdsAMS)
		{
			LinkedHashMap monthMap=new LinkedHashMap();
			RuleGroup rg=ruleGroupRepository.findOne(rgId.longValue());
			
			monthMap=dashBoardV4Service.getCurrencyCodeStatusAndValueFromAppModuleSummary(rg.getId(), fDate, tDate, tenantId);
			log.info("monthMap at rule groupId :"+rgId+" is "+monthMap);
			if(monthMap.get("multiCurrency").toString().equalsIgnoreCase("false"))
			{
				List<String> currencyCode=appModuleSummaryRepository.findDistinctCurrencyCodeByRuleGroupId(rg.getId(),fDate,tDate);
				monthMap.put("currencyCode", currencyCode);
			}
			
			
			monthMap.put("ruleGroupId", rgId);
			monthMap.put("ruleGroupIdForDisplay", rg.getIdForDisplay());
			monthMap.put("ruleGroupName", rg.getName());

		//	if(ruleGrpIdsAMS.contains(rgId))
		//	{
			
			BigDecimal totalDvCount=appModuleSummaryRepository.fetchTotalDvcountForReconGroup(rgId.longValue(), fDate, tDate);
			BigDecimal totalDvAmt=appModuleSummaryRepository.fetchTotalDvAmtForReconGroup(rgId.longValue(), fDate, tDate);
			
			
			
				//List<Object[]> recon1WSummary=appModuleSummaryRepository.fetchReconCountAndUnReconciledCountBetweenGivenDates(rgId.longValue(), fDate, tDate);

			List<Object[]> recon1WSummary=appModuleSummaryRepository.fetchReconciledAndUnReconciledAmountsAndCounts(rgId.longValue(), fDate, tDate,totalDvCount,totalDvAmt);
			
				LinkedHashMap recon=new LinkedHashMap();
				recon.put("amount", Double.valueOf(dform.format(Double.valueOf(recon1WSummary.get(0)[5].toString()))));
				recon.put("amountPer", Double.valueOf(dform.format(Double.valueOf(recon1WSummary.get(0)[6].toString()))));
				recon.put("count",  Double.valueOf(dform.format(Double.valueOf(recon1WSummary.get(0)[1].toString()))));
				recon.put("countPer", Double.valueOf(dform.format(Double.valueOf(recon1WSummary.get(0)[2].toString()))));
				monthMap.put("recon", recon);
				LinkedHashMap unRecon=new LinkedHashMap();
				unRecon.put("amount", Double.valueOf(dform.format(Double.valueOf(recon1WSummary.get(0)[7].toString()))));
				unRecon.put("amountPer",Double.valueOf(dform.format(Double.valueOf(recon1WSummary.get(0)[8].toString()))));
				unRecon.put("count", Double.valueOf(dform.format(Double.valueOf(recon1WSummary.get(0)[3].toString()))));
				unRecon.put("countPer", Double.valueOf(dform.format(Double.valueOf(recon1WSummary.get(0)[4].toString()))));
				monthMap.put("unRecon", unRecon);
				finallist.add(monthMap);


		//	}
			/*else
			{
				LinkedHashMap recon=new LinkedHashMap();
				recon.put("amount", 0);
				recon.put("amountPer", 0);
				recon.put("count", 0);
				recon.put("countPer", 0);
				monthMap.put("recon", recon);
				LinkedHashMap unRecon=new LinkedHashMap();
				unRecon.put("amount", 0);
				unRecon.put("amountPer",0);
				unRecon.put("count", 0);
				unRecon.put("countPer",0);
				monthMap.put("unRecon", unRecon);
				notReconciledList.add(monthMap);

			}*/
			

		}
		}
		finallist.addAll(notReconciledList);
		
		return finallist;

	}



	@PostMapping("/accountingRuleGroupSpecificInformationV4")
	@Timed
	public List<LinkedHashMap> accountingRuleGroupSpecificInformationV4(@RequestBody HashMap dates,HttpServletRequest request) throws SQLException, ParseException 
	{
		log.info("Rest Request to get aging analysis in service:"+dates);

		HashMap map0=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map0.get("tenantId").toString());

		ZonedDateTime fmDate=ZonedDateTime.parse(dates.get("startDate").toString());

		ZonedDateTime toDate=ZonedDateTime.parse(dates.get("endDate").toString());


		LocalDate fDate=fmDate.toLocalDate();

		LocalDate tDate=toDate.toLocalDate();


		List<LinkedHashMap> finallist=new ArrayList<LinkedHashMap>();
		
		List<LinkedHashMap> unaccountedList=new ArrayList<LinkedHashMap>();


		List<BigInteger> actRuleGrpIds=ruleGroupRepository.fetchRuleGrpIdsByTenantIdAndRulePurpose(tenantId, "ACCOUNTING");
		log.info("actRuleGrpIds :"+actRuleGrpIds.size());

		if(actRuleGrpIds.size()>0)
		{
		List<BigInteger> ruleGrpIdsAMS=appModuleSummaryRepository.findDistinctRuleGroupIdByModuleAndFileDateBetween(actRuleGrpIds,"ACCOUNTING",fDate,tDate);

		log.info("ruleGrpIdsAMS :"+ruleGrpIdsAMS.size());
		for(BigInteger rgId:ruleGrpIdsAMS)
		{




			LinkedHashMap monthMap=new LinkedHashMap();
			LinkedHashMap currencyMap=new LinkedHashMap();
			RuleGroup rg=ruleGroupRepository.findOne(rgId.longValue());
			currencyMap=dashBoardV4Service.getCurrencyCodeStatusAndValueFromAppModuleSummary(rg.getId(), fDate, tDate, tenantId);
			log.info("monthMap at rule groupId :"+rgId+" is "+monthMap);
			

		//	if(ruleGrpIdsAMS.contains(rgId))
		//	{
				
				monthMap=dashBoardV4Service.accountingRuleGroupSpecificInformationV4Service(fDate.toString(),tDate.toString(), request, rgId.longValue(),null);
				monthMap.put("multiCurrency", currencyMap.get("multiCurrency"));
				if(currencyMap.get("multiCurrency").toString().equalsIgnoreCase("false"))
				{
					List<String> currencyCode=appModuleSummaryRepository.findDistinctCurrencyCodeByRuleGroupId(rg.getId(),fDate,tDate);
					monthMap.put("currencyCode", currencyCode);
				}
				if(rg.getActivityBased())
					monthMap.put("activityBased", true);
				else
					monthMap.put("activityBased", false);
				monthMap.put("ruleGroupId", rgId);
				monthMap.put("ruleGroupName", rg.getName());
				monthMap.put("ruleGroupIdForDisplay", rg.getIdForDisplay());
				finallist.add(monthMap);

		//	}
		/*	else
			{
				LinkedHashMap acctMap=new LinkedHashMap();
				acctMap.put("amount", 0);
				acctMap.put("count", 0);
				acctMap.put("amountPer",  0);
				acctMap.put("countPer", 0);
				monthMap.put("accounted", acctMap);


				LinkedHashMap notActMap=new LinkedHashMap();
				notActMap.put("amount", 0);
				notActMap.put("count", 0);
				notActMap.put("amountPer",  0);
				notActMap.put("countPer",0);
				monthMap.put("notAccounted", notActMap);

				LinkedHashMap finalActMap=new LinkedHashMap();
				finalActMap.put("amount",0);
				finalActMap.put("count", 0);
				finalActMap.put("amountPer", 0);
				finalActMap.put("countPer", 0);
				monthMap.put("finalAccounted", finalActMap);
				RuleGroup rg=ruleGroupRepository.findOne(rgId.longValue());
				monthMap.put("ruleGroupId", rgId);
				monthMap.put("ruleGroupName", rg.getName());
				
				unaccountedList.add(monthMap);
				
			}*/
		

			
		}
	}
		finallist.addAll(unaccountedList);

		return finallist;

	}


	@PostMapping("/agingAnalysisForUnReconciliationOrUnAccounted")
	@Timed
	public List<LinkedHashMap> agingAnalysisForUnReconciliationOrUnAccounted(@RequestBody HashMap dates,@RequestParam Long ruleGroupId,@RequestParam Long bucketId,HttpServletRequest request,@RequestParam String type,
			@RequestParam(value="viewId",required=false) Long viewId,@RequestParam(value="viewType",required=false) String viewType)
	{
		log.info("API to fetch for aging analysis for agingAnalysisOfReconciliation");
		log.info("**startTime****"+ZonedDateTime.now());

		ZonedDateTime fmDate=ZonedDateTime.parse(dates.get("startDate").toString());

		ZonedDateTime toDate=ZonedDateTime.parse(dates.get("endDate").toString());


		LocalDate fDate=fmDate.toLocalDate();

		LocalDate tDate=toDate.toLocalDate();




		HashMap map0=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map0.get("tenantId").toString());

		List<Long> recRuleIds= ruleGroupDetailsRepository.fetchByRuleGroupIdAndTenantId(ruleGroupId,tenantId);

		List<BigInteger> allDataViewIds=new ArrayList<BigInteger>();

		if(viewId!=null)
			allDataViewIds.add(BigInteger.valueOf(viewId));
		else
		{
		List<BigInteger> srcViewId=rulesRepository.fetchDistictSrcViewIdsByRuleId(recRuleIds);
		allDataViewIds.addAll(srcViewId);

		List<BigInteger> trgViewId=rulesRepository.fetchDistictTargetViewIdsByRuleId(recRuleIds);
		allDataViewIds.addAll(trgViewId);
		}
		allDataViewIds=allDataViewIds.stream().distinct().collect(Collectors.toList());

		log.info("allDataViewIds :"+allDataViewIds);

		List<BigInteger> totalSrcIdsList=new ArrayList<BigInteger>();

		//	List<LinkedHashMap> finalMap=new ArrayList<LinkedHashMap>();

		List<LinkedHashMap> finalMap=new ArrayList<LinkedHashMap>();

		for(int i=0;i<allDataViewIds.size();i++)
		{
			/**to get fileDate or qualifier Date**/

		     String date=dashBoardV4Service.getFileDateOrQualifier(allDataViewIds.get(i).longValue(), tenantId);
		     
			List<LinkedHashMap> ruleAge=dashBoardV4Service.agiganalysisForUnReconciledDataOrUnAccounted(fDate.toString(), tDate.toString(), allDataViewIds.get(i).longValue(), ruleGroupId,type,bucketId,date,viewType);

			
			//log.info("ruleAge at "+i+" is "+ruleAge);
			finalMap.addAll(ruleAge);

		}
		//log.info("finalMap :"+finalMap);
		LinkedHashMap ageMap=new LinkedHashMap();
		for(int i=0;i<finalMap.size();i++)
		{


			if(i==0)
			{
				ageMap.put(finalMap.get(i).get("age"), finalMap.get(i));
			}
			else
			{
				//log.info("finalMap at i"+i+" is :"+finalMap.get(i));
				//log.info("ageMap at i:"+i+" is :"+ageMap);
				if(ageMap.containsKey(finalMap.get(i).get("age")))
				{
					//log.info("finalMap at i age "+i+" is :"+finalMap.get(i).get("age"));
					LinkedHashMap eachMap=(LinkedHashMap) ageMap.get(finalMap.get(i).get("age"));
					//log.info("eachMap at i:"+i+"is "+eachMap);
					Long count=Long.valueOf(eachMap.get("count").toString())+Long.valueOf(finalMap.get(i).get("count").toString());
					Double amount=Double.valueOf(eachMap.get("amount").toString())+Double.valueOf(finalMap.get(i).get("amount").toString());
					eachMap.put("count", count);
					eachMap.put("amount", amount);
					ageMap.put(finalMap.get(i).get("age"), eachMap);

				}
				else
				{
					LinkedHashMap eachMap=(LinkedHashMap) ageMap.get(finalMap.get(i).get("age"));
					//log.info("eachMap at i:"+i+"is "+eachMap);
					//Long count=Long.valueOf((eachMap.get("count")).toString());
					//Double amount=Double.valueOf((eachMap.get("amount")).toString());
					
					//eachMap.put("count", Long.valueOf(finalMap.get(i).get("count").toString()));
					//eachMap.put("amount", Double.valueOf(finalMap.get(i).get("amount").toString()));
					ageMap.put(finalMap.get(i).get("age"), finalMap.get(i));

				}
			}

		}

		//log.info("ageMap :"+ageMap);




		List<BucketDetails> buckDetails=bucketDetailsRepository.findByBucketId(bucketId);
		//List<LinkedHashMap> finalBucketList=new ArrayList<LinkedHashMap>();




		List<LinkedHashMap> finalUnProcessData=new ArrayList<LinkedHashMap>();
		for(BucketDetails buck:buckDetails)
		{
			//log.info("at bucket :"+buck);
			LinkedHashMap map=new LinkedHashMap();
			if(buck.getFromValue()!=null && buck.getToValue()!=null)
			{

				if(ageMap.containsKey(buck.getFromValue()+"-"+buck.getToValue()))
				{
					//log.info("no need to add zero");
					map=(LinkedHashMap) ageMap.get(buck.getFromValue()+"-"+buck.getToValue());
					finalUnProcessData.add(map);
				}
				else
				{
					//log.info("add zero at buck :"+buck);
					map.put("age", buck.getFromValue()+"-"+buck.getToValue());
					map.put("count",0);
					map.put("amount",0);
					finalUnProcessData.add(map);

				}
			}
			else if(buck.getToValue()==null && buck.getFromValue()!=null)
			{
				if(ageMap.containsKey(">="+buck.getFromValue()))
				{
					//log.info("no need to add zero");
					//log.info("finalBucketList.get>="+ageMap.get(">="+buck.getFromValue()));
					map=(LinkedHashMap) ageMap.get(">="+buck.getFromValue());
					finalUnProcessData.add(map);
				}
				else
				{

					map.put("age", ">="+buck.getFromValue());
					map.put("count",0);
					map.put("amount",0);
					finalUnProcessData.add(map);

				}
			}
			else if(buck.getFromValue()==null && buck.getToValue()!=null)
			{
				if(ageMap.containsKey("<="+buck.getToValue()))
				{
					//log.info("no need to add zero");
					//log.info("finalBucketList.get>="+ageMap.get("<="+buck.getToValue()));
					map=(LinkedHashMap) ageMap.get("<="+buck.getToValue());
					finalUnProcessData.add(map);
				}
				else
				{

					map.put("age", "<="+buck.getToValue());
					map.put("count",0);
					map.put("amount",0);
					finalUnProcessData.add(map);

				}
			}
			
		}
		if(ageMap.containsKey("Others"))
		{
			//log.info("else if others :"+ageMap);
			LinkedHashMap map=(LinkedHashMap) ageMap.get("Others");
			finalUnProcessData.add(map);
		}

		log.info("**endTime****"+ZonedDateTime.now());
		return finalUnProcessData;


	}
	
	
	
	/**
	 * @param ruleGroupId
	 * @param viewId
	 * @param viewType
	 * @param rgType
	 * @param dates
	 * @param request
	 * @param page
	 * @param size
	 * @param groupByColumnNames
	 * @param age
	 * @param violation
	 * @return
	 * @throws SQLException
	 * @throws ParseException
	 * Desc :Api to fectch dv records by applying group by columns 
	 * @throws IOException 
	 */
	@PostMapping("/getProcessedORUnProcesseGroupByColumnsInfoV4")
	@Timed 
	public LinkedHashMap getProcessedORUnProcesseGroupByColumnsInfoV4(@RequestParam String ruleGroupId,@RequestParam Long viewId,@RequestParam String viewType,@RequestParam String rgType,
			@RequestBody HashMap dates,HttpServletRequest request,@RequestParam(value = "page" , required = false) Integer page,
    		@RequestParam(value = "per_page", required = false) Integer size,@RequestParam List<String> groupByColumnNames,@RequestParam(value = "age", required = false) String age,
    		@RequestParam(value = "violation", required = false) Integer violation,
    		@RequestParam(value="fileExport",required=false) boolean fileExport,
    		@RequestParam(value="fileType",required=false) String fileType,
    		HttpServletResponse response,@RequestParam(value = "bucketId", required = false) Long bucketId, 
    		@RequestParam(required=false) String filterKey,@RequestParam(required=false) List<String> filterValues)
	{
		log.info("Rest Request to getUnReconciledOrUnAccountedGroupByInfoV4 with ruleGroupId: "+ruleGroupId+" viewId: "+viewId+" and  groupType: "+rgType+" viewType: "+viewType+" and groupByColumnNames:"+groupByColumnNames);
		LinkedHashMap lhm=new LinkedHashMap();
		if(age!=null)
		{
			lhm=getProcessedORUnProcesseGroupByColumnsInfoV4ForAllAges(ruleGroupId, viewId, viewType, rgType, dates, request, page, size, groupByColumnNames, age, violation, fileExport, fileType, response, bucketId, filterKey, filterValues);
		}
		else 
		{
			Boolean checkConnection = false;
			HashMap map=userJdbcService.getuserInfoFromToken(request);
			Long tenantId=Long.parseLong(map.get("tenantId").toString());
			RuleGroup rgId=ruleGroupRepository.findByIdForDisplayAndTenantId(ruleGroupId, tenantId);
			ZonedDateTime fmDate=ZonedDateTime.parse(dates.get("startDate").toString());
			ZonedDateTime toDate=ZonedDateTime.parse(dates.get("endDate").toString());

			java.time.LocalDate fDate=fmDate.toLocalDate();
			java.time.LocalDate tDate=toDate.toLocalDate();

			String amountQualifier=reconciliationResultService.getViewColumnQualifier(BigInteger.valueOf(viewId), "AMOUNT");
			log.info("amountQualifier :"+amountQualifier);

			/**to get fileDate or qualifier Date**/

			String dateColumn=dashBoardV4Service.getFileDateOrQualifier(viewId, tenantId);
			int accountingSummary=0;


			List<BigInteger> reconciledOrAccountedIds=new ArrayList<BigInteger>();
			if(rgType.equalsIgnoreCase("un-reconciled") || rgType.equalsIgnoreCase("reconciled"))
			{

				if(viewType.equalsIgnoreCase("source"))
				{
					List<BigInteger> orginalRowIds=reconciliationResultRepository.fetchOrginalRowIdsByRuleGroupId(rgId.getId(),viewId);
					log.info("orginalRowIds :"+orginalRowIds.size());
					reconciledOrAccountedIds.addAll(orginalRowIds);
				}

				else if(viewType.equalsIgnoreCase("target"))
				{
					List<BigInteger> targetRowIds=reconciliationResultRepository.fetchTargetRowIdsByRuleGroupId(rgId.getId(),viewId);
					log.info("targetRowIds :"+targetRowIds.size());
					reconciledOrAccountedIds.addAll(targetRowIds);
				}
			}
			else if(rgType.equalsIgnoreCase("un-accounted"))
			{

				List<BigInteger> accountedRowIds=accountedSummaryRepository.fetchAccountingIdsByStatusNGroupIdNViewId(rgId.getId(), viewId,"ACCOUNTED");
				if(accountedRowIds.size()>0)
					reconciledOrAccountedIds.addAll(accountedRowIds);
				log.info("if unaccounted :"+accountedRowIds.size());
			}
			else if(rgType.equalsIgnoreCase("JE Pending"))
			{
				List<BigInteger> accountedRowIds=accountedSummaryRepository.fetchJEPendingIdsByStatusNGroupIdNViewId(rgId.getId(), viewId,"ACCOUNTED");
				if(accountedRowIds.size()>0)
					reconciledOrAccountedIds.addAll(accountedRowIds);
			}
			else if(rgType.equalsIgnoreCase("Je Created"))
			{
				List<BigInteger> accountedRowIds=accountedSummaryRepository.fetchPostedRowIdsByRuleGrpIdAndViewId(rgId.getId(), viewId);
				reconciledOrAccountedIds.addAll(accountedRowIds);
			}
			else if(rgType.equalsIgnoreCase("approvals"))
			{
				List<BigInteger> appInprocRowIds=accountingDataRepository.fetchDistinctApprovedInProcessRowIds(rgId.getId(), viewId,"IN_PROCESS");
				reconciledOrAccountedIds.addAll(appInprocRowIds);
			}

			String reconciledOrAccountedIdList=reconciledOrAccountedIds.toString().replaceAll("\\[", "").replaceAll("\\]", "");
			log.info("finalSrcIdList :"+reconciledOrAccountedIds.size());
			LinkedHashMap groupByColumns=new LinkedHashMap();
			groupByColumns=dashBoardV4Service.getDVGroupByColumns(viewId,true);

			LinkedHashMap finalColumnsList=new LinkedHashMap();
			finalColumnsList.put("amtQualifier", groupByColumns.get("amtQualifier"));


			LinkedHashMap groupByColumnsForQualifier=new LinkedHashMap();
			groupByColumnsForQualifier=dashBoardV4Service.getDVGroupByColumns(viewId,false);

			LinkedHashMap qualifierMap= (LinkedHashMap) groupByColumnsForQualifier.get("amtQualifier");
			//log.info("qualifierMap :"+qualifierMap);
			LinkedHashMap columnMap=new LinkedHashMap();
			columnMap.put(qualifierMap.get("amtQualifierAliasName"), qualifierMap.get("amtQualifier"));

			List<LinkedHashMap> finalGroupByColumnList=new ArrayList<LinkedHashMap>();
			//log.info("groupByColumnNames :"+groupByColumnNames);
			for(int i=0;i<groupByColumnNames.size();i++)
			{
				List<LinkedHashMap> lmp=(List<LinkedHashMap>) groupByColumns.get("columnsList");
				for(int c=0;c<lmp.size();c++)
				{
					String columnName=(lmp.get(c).get("columnAliasName")).toString();
					if(columnName.equalsIgnoreCase(groupByColumnNames.get(i)))
					{
						LinkedHashMap column=new LinkedHashMap();
						column.put("columnName", lmp.get(c).get("columnName"));
						column.put("columnAliasName",groupByColumnNames.get(i));
						column.put("dataType", lmp.get(c).get("dataType"));
						if(!lmp.get(c).get("dataType").toString().equalsIgnoreCase("Decimal"))
							column.put("align", "left");
						else
							column.put("align", "right");
						column.put("width", "150px");
						columnMap.put(groupByColumnNames.get(i), lmp.get(c).get("columnName"));
						finalGroupByColumnList.add(column);
					}
				}
			}
			//log.info("columnMap after setting evry thing:"+columnMap);
			if(rgType.equalsIgnoreCase("un-accounted")  || rgType.equalsIgnoreCase("JE Pending") || rgType.equalsIgnoreCase("JE Created"))
			{
				if(rgId.getActivityBased() != null && rgId.getActivityBased())
				{
					LinkedHashMap activityMap=new LinkedHashMap();
					activityMap.put("columnName", "Status");
					activityMap.put("columnAliasName","reconciliation_status");
					activityMap.put("align", "left");
					finalGroupByColumnList.add(activityMap);

				}
			}
			finalColumnsList.put("columnsList", finalGroupByColumnList);

			String groupByColNames =	groupByColumnNames.stream().collect(Collectors.joining("`,`", "`", "`"));
			log.info("groupByColNames :"+groupByColNames);
			Connection conn = null;
			Statement stmtDv = null;
			Statement stmtMaxCount = null;
			Statement stmtTcAndAmtDv = null;

			/* Filtration */
			/*if(filterObj!=null && !(filterObj.isEmpty())){
			Set keyset=filterObj.keySet();
			Iterator entries2 = filterObj.entrySet().iterator();
	     	String filterQuery="";
	     	int keysetSz=keyset.size();
	     	if(keysetSz>0){
	     		filterQuery=filterQuery+" where ";
	     	}
	     	int count=0;
			while (entries2.hasNext()) {
				count++;
				String filterSubQuery="";
			    Map.Entry entry = (Map.Entry) entries2.next();
			    String key = (String)entry.getKey();
			    List<String> valueList = (List<String>) entry.getValue();
			    System.out.println("key = " + key + ", valueList = " + valueList);
			    String valStr=valueList.stream().collect(Collectors.joining("','", "'", "'"));
			    filterSubQuery=filterSubQuery+key+" in ("+valStr+")";
			    filterQuery=filterQuery+filterSubQuery;
			    if(count>=0 && count<=keysetSz-1){
			    	filterQuery=filterQuery+" and ";
			    }
			    System.out.println("filterQuery at count : "+count+" is: "+filterQuery);
			}

			log.info("Final filterQuery: "+filterQuery);

		}*/
			String filterQuery="";
			if(filterKey!=null){
				String valStr=filterValues.stream().collect(Collectors.joining("','", "'", "'"));
				filterQuery=" and "+filterKey+" in ("+valStr+")";
			}
			ResultSet resultDv=null;
			ResultSet resultMaxCount=null;
			ResultSet resultTcAndAmtDv=null;


			try{
				DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
				conn = ds.getConnection();
				checkConnection = true;
				log.info("Checking Connection Open in getProcessedORUnProcesseGroupByColumnsInfoV4:"+checkConnection);
				log.info("Connected database successfully...");
				stmtDv = conn.createStatement();
				stmtTcAndAmtDv = conn.createStatement();
				stmtMaxCount = conn.createStatement();


				//	ResultSet resultAct=null;
				DataViews dvName=dataViewsRepository.findOne(viewId);




				String query="";
				String totalCtAndTotalAmt="";
				List<LinkedHashMap> finalMap=new ArrayList<LinkedHashMap>();
				List<LinkedHashMap> ExportMap=new ArrayList<LinkedHashMap>();

				//log.info("age :"+age);

				/*	String totalAmtCountQuery="select count(scrIds) as count,case when sum(`"+amountQualifier+"`) is null then 0 else sum(`"+amountQualifier+"`) end as amount from "
				+ "`"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where "+dateColumn+" >= '"+fDate+"' and "+dateColumn+" <='"+tDate+"'";

		Integer totalCount=0;
		Double totalAmount=0d;
		log.info("totalAmtCountQuery :"+totalAmtCountQuery);
		resultTcAndAmtDv=stmtTcAndAmtDv.executeQuery(totalAmtCountQuery);
		while(resultTcAndAmtDv.next())
		{
			totalCount=totalCount+Integer.valueOf(resultTcAndAmtDv.getString("count"));
			totalAmount=totalAmount+Double.valueOf(resultTcAndAmtDv.getString("amount"));
		}
		log.info("totalCount :"+totalCount);
		log.info("totalAmount :"+totalAmount);*/
				if(reconciledOrAccountedIds.size()>0)
				{
					if(page!=null && size!=null)
					{
						if(rgType.equalsIgnoreCase("un-reconciled") || rgType.equalsIgnoreCase("un-accounted"))
						{
							/**for violation query And ageing**/
							String totalAmtCountQuery="select count(scrIds) as count,case when sum(`"+amountQualifier+"`) is null then 0 else sum(`"+amountQualifier+"`) end as amount from "
									+ "`"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where scrIds not in ("+reconciledOrAccountedIdList+") and "+dateColumn+" >= '"+fDate+"' and "+dateColumn+" <='"+tDate+"'";

							Integer totalCount=0;
							Double totalAmount=0d;
							//log.info("totalAmtCountQuery :"+totalAmtCountQuery);
							resultTcAndAmtDv=stmtTcAndAmtDv.executeQuery(totalAmtCountQuery);
							while(resultTcAndAmtDv.next())
							{
								totalCount=totalCount+Integer.valueOf(resultTcAndAmtDv.getString("count"));
								totalAmount=totalAmount+Double.valueOf(resultTcAndAmtDv.getString("amount"));
							}
							log.info("totalCount :"+totalCount);
							log.info("totalAmount :"+totalAmount);



							if(violation!=null || age!=null)
							{

								if(rgType.equalsIgnoreCase("un-accounted"))
								{
									RuleGroup recRgId=ruleGroupRepository.findOne(rgId.getId());

									if(recRgId.getActivityBased())
									{

										query="select DATEDIFF(CURDATE(), `"+dateColumn+"`) as age,"+groupByColNames+","
												+ "round(sum(dv_unacc.`"+amountQualifier+"`),2) as `"+amountQualifier+"`,"
												+ "count(scrIds) as count,"
												+ "round((count(scrIds)/"+totalCount+"*100),2) as countPer,"
												+ "round((sum(dv_unacc.`"+amountQualifier+"`)/"+totalAmount+"*100),2) as amtPer,"
												+ "(CASE WHEN (recon_reference IS NULL) THEN 'Un-Reconciled' ELSE 'Reconciled' END) reconciliation_status from "
												+ "(select * from `"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where Date("+dateColumn+") >= '"+fDate+"' and Date("+dateColumn+") <='"+tDate+"' "
												+ " and scrIds not in ("+reconciledOrAccountedIdList+") "+filterQuery+") dv_unacc "
												+ "  LEFT OUTER JOIN t_reconciliation_result recon on dv_unacc.scrIds = recon.original_row_id "
												+ " and recon.original_view_id ="+dvName.getId()+" AND recon.reconciliation_rule_group_id ="+recRgId.getReconciliationGroupId()+" and recon.recon_status = 'RECONCILED' and recon.current_record_flag is true group by "+groupByColNames+",age,reconciliation_status order by age desc   Limit "+(page-1) * size+", "+size;
									}
									else
									{
										query="select DATEDIFF(CURDATE(), `"+dateColumn+"`) as age,"+groupByColNames+",round(sum(`"+amountQualifier+"`),2) as `"+amountQualifier+"`,count(scrIds) as count,round((count(scrIds)/"+totalCount+"*100),2) as countPer,round((sum(`"+amountQualifier+"`)/"+totalAmount+"*100),2) as amtPer from `"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where Date("+dateColumn+") >= '"+fDate+"' and Date("+dateColumn+") <='"+tDate+"' "
												+ " and scrIds not in ("+reconciledOrAccountedIdList+") "+filterQuery+" group by "+groupByColNames+",age order by age desc   Limit "+(page-1) * size+", "+size;
									}
								}
								else
								{
									query="select DATEDIFF(CURDATE(), `"+dateColumn+"`) as age,"+groupByColNames+",round(sum(`"+amountQualifier+"`),2) as `"+amountQualifier+"`,count(scrIds) as count,round((count(scrIds)/"+totalCount+"*100),2) as countPer,round((sum(`"+amountQualifier+"`)/"+totalAmount+"*100),2) as amtPer from `"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where Date("+dateColumn+") >= '"+fDate+"' and Date("+dateColumn+") <='"+tDate+"' "
											+ " and scrIds not in ("+reconciledOrAccountedIdList+") "+filterQuery+" group by "+groupByColNames+",age order by age desc   Limit "+(page-1) * size+", "+size;
								}
							}
							else if(violation==null && age==null)
							{

								if(rgType.equalsIgnoreCase("un-accounted"))
								{
									RuleGroup recRgId=ruleGroupRepository.findOne(rgId.getId());

									if(recRgId.getActivityBased())
									{

										query="select "+groupByColNames+","
												+ "round(sum(dv_unacc.`"+amountQualifier+"`),2) as `"+amountQualifier+"`,"
												+ "count(scrIds) as count,"
												+ "round((count(scrIds)/"+totalCount+"*100),2) as countPer,"
												+ "round((sum(dv_unacc.`"+amountQualifier+"`)/"+totalAmount+"*100),2) as amtPer,"
												+ "(CASE WHEN (recon_reference IS NULL) THEN 'Un-Reconciled' ELSE 'Reconciled' END) reconciliation_status from "
												+ "(select * from `"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where Date("+dateColumn+") >= '"+fDate+"' and Date("+dateColumn+") <='"+tDate+"' "
												+ " and scrIds not in ("+reconciledOrAccountedIdList+") "+filterQuery+") dv_unacc "
												+ "  LEFT OUTER JOIN t_reconciliation_result recon on dv_unacc.scrIds = recon.original_row_id "
												+ " and recon.original_view_id ="+dvName.getId()+" AND recon.reconciliation_rule_group_id ="+recRgId.getReconciliationGroupId()+" and recon.recon_status = 'RECONCILED' and recon.current_record_flag is true group by "+groupByColNames+",reconciliation_status order by count desc   Limit "+(page-1) * size+", "+size;
									}
									else
									{
										query="select "+groupByColNames+",round(sum(`"+amountQualifier+"`),2) as `"+amountQualifier+"`,count(scrIds) as count,round((count(scrIds)/"+totalCount+"*100),2) as countPer,round((sum(`"+amountQualifier+"`)/"+totalAmount+"*100),2) as amtPer from `"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where Date("+dateColumn+") >= '"+fDate+"' and Date("+dateColumn+") <='"+tDate+"' "
												+ " and scrIds not in ("+reconciledOrAccountedIdList+") "+filterQuery+" group by "+groupByColNames+" order by count desc  Limit "+(page-1) * size+", "+size;
									}
								}
								else
								{

									query="select "+groupByColNames+",round(sum(`"+amountQualifier+"`),2) as `"+amountQualifier+"`,count(scrIds) as count,round((count(scrIds)/"+totalCount+"*100),2) as countPer,round((sum(`"+amountQualifier+"`)/"+totalAmount+"*100),2) as amtPer from `"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where Date("+dateColumn+") >= '"+fDate+"' and Date("+dateColumn+") <='"+tDate+"' "
											+ " and scrIds not in ("+reconciledOrAccountedIdList+") "+filterQuery+" group by "+groupByColNames+" order by count desc  Limit "+(page-1) * size+", "+size;
								}
							}
							//log.info("query if not accounted :"+query);
						}
						else if(rgType.equalsIgnoreCase("reconciled") || rgType.equalsIgnoreCase("JE Created") || rgType.equalsIgnoreCase("approvals") || rgType.equalsIgnoreCase("JE Pending"))
						{

							String totalAmtCountQuery="select count(scrIds) as count,case when sum(`"+amountQualifier+"`) is null then 0 else sum(`"+amountQualifier+"`) end as amount from "
									+ "`"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where scrIds in ("+reconciledOrAccountedIdList+") and "+dateColumn+" >= '"+fDate+"' and "+dateColumn+" <='"+tDate+"'";

							Integer totalCount=0;
							Double totalAmount=0d;
							//log.info("totalAmtCountQuery :"+totalAmtCountQuery);
							resultTcAndAmtDv=stmtTcAndAmtDv.executeQuery(totalAmtCountQuery);
							while(resultTcAndAmtDv.next())
							{
								totalCount=totalCount+Integer.valueOf(resultTcAndAmtDv.getString("count"));
								totalAmount=totalAmount+Double.valueOf(resultTcAndAmtDv.getString("amount"));
							}
							log.info("totalCount :"+totalCount);
							log.info("totalAmount :"+totalAmount);


							if(rgType.equalsIgnoreCase("JE Created") || rgType.equalsIgnoreCase("JE Pending"))
							{
								RuleGroup recRgId=ruleGroupRepository.findOne(rgId.getId());

								if(recRgId.getActivityBased())
								{
									query="select "+groupByColNames+",round(sum(dv_act.`"+amountQualifier+"`),2) as `"+amountQualifier+"`,count(scrIds) as count,"
											+ "round((count(scrIds)/"+totalCount+"*100),2) as countPer,round((sum(dv_act.`"+amountQualifier+"`)/"+totalAmount+"*100),2) as amtPer,"
											+ "  (CASE WHEN (recon_reference IS NULL) THEN 'Un-Reconciled' ELSE 'Reconciled' END) reconciliation_status from "
											+ "(select * from `"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where Date("+dateColumn+") >= '"+fDate+"' and Date("+dateColumn+") <='"+tDate+"' "
											+ " and scrIds in ("+reconciledOrAccountedIdList+") "+filterQuery+") dv_act "
											+ "LEFT OUTER JOIN"
											+ " t_reconciliation_result recon ON dv_act.scrIds = recon.original_row_id "
											+ "AND recon.original_view_id ="+dvName.getId()+" AND recon.reconciliation_rule_group_id = "+recRgId.getReconciliationGroupId()+" and recon.recon_status = 'RECONCILED' and recon.current_record_flag is true group by "+groupByColNames+",reconciliation_status order by count desc  Limit "+(page-1) * size+", "+size;
								}
								else
								{
									query="select "+groupByColNames+",round(sum(`"+amountQualifier+"`),2) as `"+amountQualifier+"`,count(scrIds) as count,round((count(scrIds)/"+totalCount+"*100),2) as countPer,round((sum(`"+amountQualifier+"`)/"+totalAmount+"*100),2) as amtPer from `"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where Date("+dateColumn+") >= '"+fDate+"' and Date("+dateColumn+") <='"+tDate+"' "
											+ " and scrIds in ("+reconciledOrAccountedIdList+") "+filterQuery+" group by "+groupByColNames+" order by count desc  Limit "+(page-1) * size+", "+size;
								}
							}
							else
							{
								query="select "+groupByColNames+",round(sum(`"+amountQualifier+"`),2) as `"+amountQualifier+"`,count(scrIds) as count,round((count(scrIds)/"+totalCount+"*100),2) as countPer,round((sum(`"+amountQualifier+"`)/"+totalAmount+"*100),2) as amtPer from `"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where Date("+dateColumn+") >= '"+fDate+"' and Date("+dateColumn+") <='"+tDate+"' "
										+ " and scrIds in ("+reconciledOrAccountedIdList+") "+filterQuery+" group by "+groupByColNames+" order by count desc  Limit "+(page-1) * size+", "+size;
							}

						}


					}
					else
					{
						if(rgType.equalsIgnoreCase("un-reconciled") || rgType.equalsIgnoreCase("un-accounted"))
						{
							String totalAmtCountQuery="select count(scrIds) as count,case when sum(`"+amountQualifier+"`) is null then 0 else sum(`"+amountQualifier+"`) end as amount from "
									+ "`"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where scrIds not in ("+reconciledOrAccountedIdList+") and "+dateColumn+" >= '"+fDate+"' and "+dateColumn+" <='"+tDate+"'";

							Integer totalCount=0;
							Double totalAmount=0d;
							//log.info("totalAmtCountQuery :"+totalAmtCountQuery);
							resultTcAndAmtDv=stmtTcAndAmtDv.executeQuery(totalAmtCountQuery);
							while(resultTcAndAmtDv.next())
							{
								totalCount=totalCount+Integer.valueOf(resultTcAndAmtDv.getString("count"));
								totalAmount=totalAmount+Double.valueOf(resultTcAndAmtDv.getString("amount"));
							}
							log.info("totalCount :"+totalCount);
							log.info("totalAmount :"+totalAmount);

							if(violation!=null || age!=null)
							{


								if(rgType.equalsIgnoreCase("un-accounted"))
								{
									RuleGroup recRgId=ruleGroupRepository.findOne(rgId.getId());

									if(recRgId.getActivityBased())
									{

										query="select DATEDIFF(CURDATE(), `"+dateColumn+"`) as age,"+groupByColNames+","
												+ "round(sum(dv_unacc.`"+amountQualifier+"`),2) as `"+amountQualifier+"`,"
												+ "count(scrIds) as count,"
												+ "round((count(scrIds)/"+totalCount+"*100),2) as countPer,"
												+ "round((sum(dv_unacc.`"+amountQualifier+"`)/"+totalAmount+"*100),2) as amtPer,"
												+ "(CASE WHEN (recon_reference IS NULL) THEN 'Un-Reconciled' ELSE 'Reconciled' END) reconciliation_status from "
												+ "(select * from `"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where Date("+dateColumn+") >= '"+fDate+"' and Date("+dateColumn+") <='"+tDate+"' "
												+ " and scrIds not in ("+reconciledOrAccountedIdList+") "+filterQuery+") dv_unacc "
												+ "  LEFT OUTER JOIN t_reconciliation_result recon on dv_unacc.scrIds = recon.original_row_id "
												+ " and recon.original_view_id ="+dvName.getId()+" AND recon.reconciliation_rule_group_id ="+recRgId.getReconciliationGroupId()+" and recon.recon_status = 'RECONCILED' and recon.current_record_flag is true group by "+groupByColNames+",age,reconciliation_status order by age desc";
									}
								}
								else
								{
									query="select DATEDIFF(CURDATE(), `"+dateColumn+"`) as age,"+groupByColNames+",round(sum(`"+amountQualifier+"`),2) as `"+amountQualifier+"`,count(scrIds) as count,round((count(scrIds)/"+totalCount+"*100),2) as countPer,round((sum(`"+amountQualifier+"`)/"+totalAmount+"*100),2) as amtPer from `"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where Date("+dateColumn+") >= '"+fDate+"' and Date("+dateColumn+") <='"+tDate+"' "
											+ " and scrIds not in ("+reconciledOrAccountedIdList+") "+filterQuery+" group by "+groupByColNames+",age order by age desc";
								}
							}
							else if(violation==null && age==null)
							{

								if(rgType.equalsIgnoreCase("un-accounted"))
								{
									RuleGroup recRgId=ruleGroupRepository.findOne(rgId.getId());

									if(recRgId.getActivityBased())
									{

										query="select "+groupByColNames+","
												+ "round(sum(dv_unacc.`"+amountQualifier+"`),2) as `"+amountQualifier+"`,"
												+ "count(scrIds) as count,"
												+ "round((count(scrIds)/"+totalCount+"*100),2) as countPer,"
												+ "round((sum(dv_unacc.`"+amountQualifier+"`)/"+totalAmount+"*100),2) as amtPer,"
												+ "(CASE WHEN (recon_reference IS NULL) THEN 'Un-Reconciled' ELSE 'Reconciled' END) reconciliation_status from "
												+ "(select * from `"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where Date("+dateColumn+") >= '"+fDate+"' and Date("+dateColumn+") <='"+tDate+"' "
												+ " and scrIds not in ("+reconciledOrAccountedIdList+") "+filterQuery+") dv_unacc "
												+ "  LEFT OUTER JOIN t_reconciliation_result recon on dv_unacc.scrIds = recon.original_row_id "
												+ " and recon.original_view_id ="+dvName.getId()+" AND recon.reconciliation_rule_group_id ="+recRgId.getReconciliationGroupId()+" and recon.recon_status = 'RECONCILED' and recon.current_record_flag is true group by "+groupByColNames+",reconciliation_status order by count desc";
									}
									else
									{
										query="select "+groupByColNames+",round(sum(`"+amountQualifier+"`),2) as `"+amountQualifier+"`,count(scrIds) as count,round((count(scrIds)/"+totalCount+"*100),2) as countPer,round((sum(`"+amountQualifier+"`)/"+totalAmount+"*100),2) as amtPer from `"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where Date("+dateColumn+") >= '"+fDate+"' and Date("+dateColumn+") <='"+tDate+"' "
												+ " and scrIds not in ("+reconciledOrAccountedIdList+")  "+filterQuery+" group by "+groupByColNames+" order by count desc";	
									}
								}
								else
								{
									query="select "+groupByColNames+",round(sum(`"+amountQualifier+"`),2) as `"+amountQualifier+"`,count(scrIds) as count,round((count(scrIds)/"+totalCount+"*100),2) as countPer,round((sum(`"+amountQualifier+"`)/"+totalAmount+"*100),2) as amtPer from `"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where Date("+dateColumn+") >= '"+fDate+"' and Date("+dateColumn+") <='"+tDate+"' "
											+ " and scrIds not in ("+reconciledOrAccountedIdList+")  "+filterQuery+" group by "+groupByColNames+" order by count desc";
								}
							}
						}
						else if(rgType.equalsIgnoreCase("reconciled") || rgType.equalsIgnoreCase("JE Created") || rgType.equalsIgnoreCase("approvals") || rgType.equalsIgnoreCase("JE Pending"))
						{
							String totalAmtCountQuery="select count(scrIds) as count,case when sum(`"+amountQualifier+"`) is null then 0 else sum(`"+amountQualifier+"`) end as amount from "
									+ "`"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where scrIds in ("+reconciledOrAccountedIdList+") and "+dateColumn+" >= '"+fDate+"' and "+dateColumn+" <='"+tDate+"'";

							Integer totalCount=0;
							Double totalAmount=0d;
							//log.info("totalAmtCountQuery :"+totalAmtCountQuery);
							resultTcAndAmtDv=stmtTcAndAmtDv.executeQuery(totalAmtCountQuery);
							while(resultTcAndAmtDv.next())
							{
								totalCount=totalCount+Integer.valueOf(resultTcAndAmtDv.getString("count"));
								totalAmount=totalAmount+Double.valueOf(resultTcAndAmtDv.getString("amount"));
							}
							log.info("totalCount :"+totalCount);
							log.info("totalAmount :"+totalAmount);

							if(rgType.equalsIgnoreCase("JE Created") || rgType.equalsIgnoreCase("JE Pending"))
							{
								RuleGroup recRgId=ruleGroupRepository.findOne(rgId.getId());

								if(recRgId.getActivityBased())
								{
									query="select "+groupByColNames+","
											+ "round(sum(dv_act.`"+amountQualifier+"`),2) as `"+amountQualifier+"`,"
											+ "count(scrIds) as count,round((count(scrIds)/"+totalCount+"*100),2) as countPer,"
											+ "round((sum(dv_act.`"+amountQualifier+"`)/"+totalAmount+"*100),2) as amtPer ,"
											+ " (CASE WHEN (recon_reference IS NULL) THEN 'Un-Reconciled'  ELSE 'Reconciled' END) reconciliation_status "
											+ "from ( select * from`"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where Date("+dateColumn+") >= '"+fDate+"' and Date("+dateColumn+") <='"+tDate+"' "
											+ " and scrIds in ("+reconciledOrAccountedIdList+") "+filterQuery+") dv_act "
											+ "LEFT OUTER JOIN t_reconciliation_result recon ON dv_act.scrIds = recon.original_row_id "
											+ "AND recon.original_view_id = "+dvName.getId()+" AND recon.reconciliation_rule_group_id = "+recRgId.getReconciliationGroupId()
											+ " and recon.recon_status = 'RECONCILED' and recon.current_record_flag is true group by "+groupByColNames+",reconciliation_status order by count desc";
								}
								else
								{
									query="select "+groupByColNames+",round(sum(`"+amountQualifier+"`),2) as `"+amountQualifier+"`,count(scrIds) as count,round((count(scrIds)/"+totalCount+"*100),2) as countPer,round((sum(`"+amountQualifier+"`)/"+totalAmount+"*100),2) as amtPer from `"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where Date("+dateColumn+") >= '"+fDate+"' and Date("+dateColumn+") <='"+tDate+"' "
											+ " and scrIds in ("+reconciledOrAccountedIdList+") "+filterQuery+" group by "+groupByColNames+" order by count desc";
								}
							}
							else
							{
								query="select "+groupByColNames+",round(sum(`"+amountQualifier+"`),2) as `"+amountQualifier+"`,count(scrIds) as count,round((count(scrIds)/"+totalCount+"*100),2) as countPer,round((sum(`"+amountQualifier+"`)/"+totalAmount+"*100),2) as amtPer from `"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where Date("+dateColumn+") >= '"+fDate+"' and Date("+dateColumn+") <='"+tDate+"' "
										+ " and scrIds in ("+reconciledOrAccountedIdList+") "+filterQuery+" group by "+groupByColNames+" order by count desc";
							}
						}
					}

				}
				else
				{

					log.info("if size is zero");

					String totalAmtCountQuery="select count(scrIds) as count,case when sum(`"+amountQualifier+"`) is null then 0 else sum(`"+amountQualifier+"`) end as amount from "
							+ "`"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where "+dateColumn+" >= '"+fDate+"' and "+dateColumn+" <='"+tDate+"'";

					Integer totalCount=0;
					Double totalAmount=0d;
					//log.info("totalAmtCountQuery :"+totalAmtCountQuery);
					resultTcAndAmtDv=stmtTcAndAmtDv.executeQuery(totalAmtCountQuery);
					while(resultTcAndAmtDv.next())
					{
						totalCount=totalCount+Integer.valueOf(resultTcAndAmtDv.getString("count"));
						totalAmount=totalAmount+Double.valueOf(resultTcAndAmtDv.getString("amount"));
					}
					log.info("totalCount :"+totalCount);
					log.info("totalAmount :"+totalAmount);


					if(page!=null && size!=null)
					{


						if((violation!=null || age !=null) && !(rgType.equalsIgnoreCase("reconciled") || rgType.equalsIgnoreCase("JE Created") || rgType.equalsIgnoreCase("JE Pending")))
						{
							if(rgType.equalsIgnoreCase("un-accounted"))
							{
								RuleGroup recRgId=ruleGroupRepository.findOne(rgId.getId());

								if(recRgId.getActivityBased())
								{

									query="select DATEDIFF(CURDATE(), `"+dateColumn+"`) as age,"+groupByColNames+","
											+ "round(sum(dv_unacc.`"+amountQualifier+"`),2) as `"+amountQualifier+"`,"
											+ "count(scrIds) as count,"
											+ "round((count(scrIds)/"+totalCount+"*100),2) as countPer,"
											+ "round((sum(dv_unacc.`"+amountQualifier+"`)/"+totalAmount+"*100),2) as amtPer,"
											+ "(CASE WHEN (recon_reference IS NULL) THEN 'Un-Reconciled' ELSE 'Reconciled' END) reconciliation_status from "
											+ "(select * from `"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where Date("+dateColumn+") >= '"+fDate+"' and Date("+dateColumn+") <='"+tDate+"' "
											+ filterQuery+") dv_unacc "
											+ "  LEFT OUTER JOIN t_reconciliation_result recon on dv_unacc.scrIds = recon.original_row_id "
											+ " and recon.original_view_id ="+dvName.getId()+" AND recon.reconciliation_rule_group_id ="+recRgId.getReconciliationGroupId()+" and recon.recon_status = 'RECONCILED' and recon.current_record_flag is true group by "+groupByColNames+",age,reconciliation_status order by age desc  Limit "+(page-1) * size+", "+size;
								}
								else
								{
									query="select DATEDIFF(CURDATE(), `"+dateColumn+"`) as age,"+groupByColNames+",round(sum(`"+amountQualifier+"`),2) as `"+amountQualifier+"`,count(scrIds) as count,round((count(scrIds)/"+totalCount+"*100),2) as countPer,round((sum(`"+amountQualifier+"`)/"+totalAmount+"*100),2) as amtPer "
											+ "from `"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where Date(fileDate) >= '"+fDate+"' and Date(fileDate) <='"+tDate+"' "+filterQuery
											+ "group by "+groupByColNames+",age order by age desc  Limit "+(page-1) * size+", "+size;
								}
							}
							else
							{

								query="select DATEDIFF(CURDATE(), `"+dateColumn+"`) as age,"+groupByColNames+",round(sum(`"+amountQualifier+"`),2) as `"+amountQualifier+"`,count(scrIds) as count,round((count(scrIds)/"+totalCount+"*100),2) as countPer,round((sum(`"+amountQualifier+"`)/"+totalAmount+"*100),2) as amtPer "
										+ "from `"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where Date(fileDate) >= '"+fDate+"' and Date(fileDate) <='"+tDate+"' "+filterQuery
										+ "group by "+groupByColNames+",age order by age desc  Limit "+(page-1) * size+", "+size;
							}
						}
						else if(violation==null && age ==null && !(rgType.equalsIgnoreCase("reconciled") || rgType.equalsIgnoreCase("JE Created") || rgType.equalsIgnoreCase("JE Pending")))
						{

							if(rgType.equalsIgnoreCase("un-accounted"))
							{
								RuleGroup recRgId=ruleGroupRepository.findOne(rgId.getId());

								if(recRgId.getActivityBased())
								{

									query="select "+groupByColNames+","
											+ "round(sum(dv_unacc.`"+amountQualifier+"`),2) as `"+amountQualifier+"`,"
											+ "count(scrIds) as count,"
											+ "round((count(scrIds)/"+totalCount+"*100),2) as countPer,"
											+ "round((sum(dv_unacc.`"+amountQualifier+"`)/"+totalAmount+"*100),2) as amtPer,"
											+ "(CASE WHEN (recon_reference IS NULL) THEN 'Un-Reconciled' ELSE 'Reconciled' END) reconciliation_status from "
											+ "(select * from `"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where Date("+dateColumn+") >= '"+fDate+"' and Date("+dateColumn+") <='"+tDate+"' "
											+ filterQuery+") dv_unacc "
											+ "  LEFT OUTER JOIN t_reconciliation_result recon on dv_unacc.scrIds = recon.original_row_id "
											+ " and recon.original_view_id ="+dvName.getId()+" AND recon.reconciliation_rule_group_id ="+recRgId.getReconciliationGroupId()+" and recon.recon_status = 'RECONCILED' and recon.current_record_flag is true group by "+groupByColNames+",reconciliation_status order by count desc Limit "+(page-1) * size+", "+size;
								}
								else
								{
									query="select "+groupByColNames+",round(sum(`"+amountQualifier+"`),2) as `"+amountQualifier+"`,count(scrIds) as count,round((count(scrIds)/"+totalCount+"*100),2) as countPer,round((sum(`"+amountQualifier+"`)/"+totalAmount+"*100),2) as amtPer from `"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where Date("+dateColumn+") >= '"+fDate+"' and Date("+dateColumn+") <='"+tDate+"' "
											+filterQuery+ "group by "+groupByColNames+" order by count desc  Limit "+(page-1) * size+", "+size;
								}
							}
							else
							{
								query="select "+groupByColNames+",round(sum(`"+amountQualifier+"`),2) as `"+amountQualifier+"`,count(scrIds) as count,round((count(scrIds)/"+totalCount+"*100),2) as countPer,round((sum(`"+amountQualifier+"`)/"+totalAmount+"*100),2) as amtPer from `"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where Date("+dateColumn+") >= '"+fDate+"' and Date("+dateColumn+") <='"+tDate+"' "
										+filterQuery+ "group by "+groupByColNames+" order by count desc  Limit "+(page-1) * size+", "+size;
							}
						}
					}
					else
					{
						if((violation!=null || age!=null) && !(rgType.equalsIgnoreCase("reconciled") || rgType.equalsIgnoreCase("JE Created") || rgType.equalsIgnoreCase("JE Pending")))
						{


							if(rgType.equalsIgnoreCase("un-accounted"))
							{
								RuleGroup recRgId=ruleGroupRepository.findOne(rgId.getId());

								if(recRgId.getActivityBased())
								{

									query="select DATEDIFF(CURDATE(), `"+dateColumn+"`) as age,"+groupByColNames+","
											+ "round(sum(dv_unacc.`"+amountQualifier+"`),2) as `"+amountQualifier+"`,"
											+ "count(scrIds) as count,"
											+ "round((count(scrIds)/"+totalCount+"*100),2) as countPer,"
											+ "round((sum(dv_unacc.`"+amountQualifier+"`)/"+totalAmount+"*100),2) as amtPer,"
											+ "(CASE WHEN (recon_reference IS NULL) THEN 'Un-Reconciled' ELSE 'Reconciled' END) reconciliation_status from "
											+ "(select * from `"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where Date("+dateColumn+") >= '"+fDate+"' and Date("+dateColumn+") <='"+tDate+"' "
											+ filterQuery+") dv_unacc "
											+ "  LEFT OUTER JOIN t_reconciliation_result recon on dv_unacc.scrIds = recon.original_row_id "
											+ " and recon.original_view_id ="+dvName.getId()+" AND recon.reconciliation_rule_group_id ="+recRgId.getReconciliationGroupId()+" and recon.recon_status = 'RECONCILED' and recon.current_record_flag is true group by "+groupByColNames+",age,reconciliation_status order by age desc";
								}
								else
								{
									query="select DATEDIFF(CURDATE(), `fileDate`) as age,"+groupByColNames+",round(sum(`"+amountQualifier+"`),2) as `"+amountQualifier+"`,count(scrIds) as count,round((count(scrIds)/"+totalCount+"*100),2) as countPer,round((sum(`"+amountQualifier+"`)/"+totalAmount+"*100),2) as amtPer from `"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where Date("+dateColumn+") >= '"+fDate+"' and Date("+dateColumn+") <='"+tDate+"' "
											+filterQuery+ "group by "+groupByColNames+",age order by age desc";
								}
							}
							else
							{

								query="select DATEDIFF(CURDATE(), `fileDate`) as age,"+groupByColNames+",round(sum(`"+amountQualifier+"`),2) as `"+amountQualifier+"`,count(scrIds) as count,round((count(scrIds)/"+totalCount+"*100),2) as countPer,round((sum(`"+amountQualifier+"`)/"+totalAmount+"*100),2) as amtPer from `"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where Date("+dateColumn+") >= '"+fDate+"' and Date("+dateColumn+") <='"+tDate+"' "
										+filterQuery+ "group by "+groupByColNames+",age order by age desc";
							}
						}
						else if((violation==null && age==null) && !(rgType.equalsIgnoreCase("reconciled") || rgType.equalsIgnoreCase("JE Created") || rgType.equalsIgnoreCase("JE Pending")))
						{

							if(rgType.equalsIgnoreCase("un-accounted"))
							{
								RuleGroup recRgId=ruleGroupRepository.findOne(rgId.getId());

								if(recRgId.getActivityBased())
								{

									query="select "+groupByColNames+","
											+ "round(sum(dv_unacc.`"+amountQualifier+"`),2) as `"+amountQualifier+"`,"
											+ "count(scrIds) as count,"
											+ "round((count(scrIds)/"+totalCount+"*100),2) as countPer,"
											+ "round((sum(dv_unacc.`"+amountQualifier+"`)/"+totalAmount+"*100),2) as amtPer,"
											+ "(CASE WHEN (recon_reference IS NULL) THEN 'Un_Reconciled' ELSE 'Reconciled' END) reconciliation_status from "
											+ "(select * from `"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where Date("+dateColumn+") >= '"+fDate+"' and Date("+dateColumn+") <='"+tDate+"' "
											+ filterQuery+") dv_unacc "
											+ "  LEFT OUTER JOIN t_reconciliation_result recon on dv_unacc.scrIds = recon.original_row_id "
											+ " and recon.original_view_id ="+dvName.getId()+" AND recon.reconciliation_rule_group_id ="+recRgId.getReconciliationGroupId()+" and recon.recon_status = 'RECONCILED' and recon.current_record_flag is true group by "+groupByColNames+",reconciliation_status order by count desc";
								}
							}
							else
							{
								query="select "+groupByColNames+",round(sum(`"+amountQualifier+"`),2) as `"+amountQualifier+"`,count(scrIds) as count,round((count(scrIds)/"+totalCount+"*100),2) as countPer,round((sum(`"+amountQualifier+"`)/"+totalAmount+"*100),2) as amtPer from `"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where Date("+dateColumn+") >= '"+fDate+"' and Date("+dateColumn+") <='"+tDate+"' "
										+filterQuery+" group by "+groupByColNames+" order by count desc";
							}


						}
					}


				}
				//log.info("query in getProcessedORUnProcesseGroupByColumnsInfoV4 :"+query);
				if(!query.isEmpty())
				{
					String maxCtQuery="";
					//log.info("page :"+page+" and size :"+size);
					if(page!=null && size!=null)
						maxCtQuery=query.replaceAll("Limit "+(page-1) * size+", "+size, "");
					else
						maxCtQuery=query;
					//log.info("maxCtQuery :"+maxCtQuery);
					//log.info("*********query*******:"+query);
					resultMaxCount=stmtMaxCount.executeQuery(maxCtQuery);

					resultDv=stmtDv.executeQuery(query);
					ResultSetMetaData rsmd2 = resultDv.getMetaData();
					int columnCount = rsmd2.getColumnCount();
					Double maxAmtValue=0d;
					Long mxCtValue=0l;
					while(resultMaxCount.next())
					{
						if(mxCtValue<Long.valueOf(resultMaxCount.getString("count").toString()))
							mxCtValue=Long.valueOf(resultMaxCount.getString("count").toString());
						if(maxAmtValue<Double.valueOf(resultMaxCount.getString(amountQualifier).toString()))
							maxAmtValue=Double.valueOf(resultMaxCount.getString(amountQualifier).toString());

					}
					log.info("mxCtValue :"+mxCtValue);
					log.info("maxAmtValue :"+maxAmtValue);

					while(resultDv.next())
					{

						LinkedHashMap map2=new LinkedHashMap();
						LinkedHashMap exportmap2=new LinkedHashMap();
						for (int i = 1; i <= columnCount; i++ ) {  
							String name=rsmd2.getColumnName(i);
							if(name.equalsIgnoreCase(amountQualifier))
							{
								//log.info("rsmd2.getColumnName(i) **:"+rsmd2.getColumnName(i));
								//log.info("columnMap ** :"+columnMap);
								if(resultDv.getString(rsmd2.getColumnName(i))!=null)
									map2.put(rsmd2.getColumnName(i), Double.valueOf(resultDv.getString(rsmd2.getColumnName(i))));
								else
									map2.put(rsmd2.getColumnName(i), 0d);

								if(resultDv.getString(rsmd2.getColumnName(i))!=null)
									exportmap2.put("Amount", Double.valueOf(resultDv.getString(rsmd2.getColumnName(i))));
								//exportmap2.put(columnMap.get(rsmd2.getColumnName(i)), Double.valueOf(resultDv.getString(rsmd2.getColumnName(i))));
								else
									exportmap2.put("Amount", 0d);



								Double avgAmtPer=0d;
								if(maxAmtValue>0)
									avgAmtPer=(Double.valueOf(resultDv.getString(rsmd2.getColumnName(i)))/maxAmtValue)*100;

								map2.put("avgAmtPer", avgAmtPer);
								//exportmap2.put("avgAmtPer", avgAmtPer);

							}
							else if(name.equalsIgnoreCase("count"))
							{
								map2.put(rsmd2.getColumnName(i), Integer.valueOf(resultDv.getString(rsmd2.getColumnName(i))));

								exportmap2.put("Count", Integer.valueOf(resultDv.getString(rsmd2.getColumnName(i))));

								Double avgCtPer=0d;
								if(mxCtValue>0)
									avgCtPer=(Double.valueOf(resultDv.getString(rsmd2.getColumnName(i)))/mxCtValue)*100;

								map2.put("avgCtPer", avgCtPer);

								//exportmap2.put("avgCtPer", avgCtPer);

							}
							else if(name.equalsIgnoreCase("countPer"))
							{

								if(resultDv.getString(rsmd2.getColumnName(i))!=null)
									map2.put(rsmd2.getColumnName(i), Double.valueOf(resultDv.getString(rsmd2.getColumnName(i))));
								else
									map2.put(rsmd2.getColumnName(i), 0d);


								if(resultDv.getString(rsmd2.getColumnName(i))!=null)
									exportmap2.put("Count Percentage", Double.valueOf(resultDv.getString(rsmd2.getColumnName(i))));
								else
									exportmap2.put("Count Percentage", 0d);
							}
							else if(name.equalsIgnoreCase("amtPer"))
							{
								if(resultDv.getString(rsmd2.getColumnName(i))!=null)
									map2.put(rsmd2.getColumnName(i), Double.valueOf(resultDv.getString(rsmd2.getColumnName(i))));
								else
									map2.put(rsmd2.getColumnName(i), 0d);

								/*if(resultDv.getString(rsmd2.getColumnName(i))!=null)
								exportmap2.put(rsmd2.getColumnName(i), Double.valueOf(resultDv.getString(rsmd2.getColumnName(i))));
							else
								exportmap2.put(rsmd2.getColumnName(i), 0d);*/
							}
							else
							{
								//log.info("rsmd2.getColumnName(i) **:"+rsmd2.getColumnName(i));
								//log.info("columnMap ** :"+columnMap);

								if(resultDv.getString(rsmd2.getColumnName(i))!=null)
									map2.put(rsmd2.getColumnName(i), resultDv.getString(rsmd2.getColumnName(i)));
								else
									map2.put(rsmd2.getColumnName(i), "blank");

								if(resultDv.getString(rsmd2.getColumnName(i))!=null)
									exportmap2.put(columnMap.get(rsmd2.getColumnName(i)), resultDv.getString(rsmd2.getColumnName(i)));
								else
									exportmap2.put(columnMap.get(rsmd2.getColumnName(i)), "blank");
							}

						}
						if(violation!=null && age==null)
						{
							int ruleAge=Integer.valueOf(resultDv.getString("age").toString());
							if(ruleAge>=violation)
							{
								finalMap.add(map2);
								ExportMap.add(exportmap2);
							}
						}
						else if(violation==null && age!=null)
						{
							Integer ruleAge=Integer.valueOf(resultDv.getString("age").toString());
							//log.info("ruleAge :"+ruleAge);
							if(age.contains("-"))
							{
								//log.info("age contains -");
								String[] str=age.split("-");
								//log.info("str at 0:"+str[0]);
								//log.info("str at 1:"+str[1]);
								if(ruleAge>=Integer.valueOf(str[0]) && ruleAge<=Integer.valueOf(str[1]))
								{
									finalMap.add(map2);
									ExportMap.add(exportmap2);
								}
							}
							else if(age.contains(">") || (age.contains("<")))
							{
								String[] str=age.split("=");

								if(age.contains(">") )
								{
									if(ruleAge>=Integer.valueOf(str[1]))
									{
										finalMap.add(map2);
										ExportMap.add(exportmap2);
									}
								}
								else if(age.contains("<"))
								{
									if(ruleAge<=Integer.valueOf(str[1]))
									{
										finalMap.add(map2);
										ExportMap.add(exportmap2);
									}
								}
							}
							else if(age.equalsIgnoreCase("others"))
							{     
								//log.info("in others group :"+map2);
								Boolean others=true;
								List<BucketDetails> buckDetails=bucketDetailsRepository.findByBucketId(bucketId);

								for(int j=0;j<buckDetails.size();j++){
									int buckVal=j+1;
									BucketDetails bucketDet=buckDetails.get(j);
									Long bucketDetId =bucketDet.getId();
									if(bucketDet!=null)
									{
										Integer frmVal=0;
										Integer toVal=0;
										Integer from=0;
										Integer to=0;
										from=bucketDet.getFromValue();
										to=bucketDet.getToValue();
										log.info("from: "+from+" to: "+to);

										if(from!=null && to!=null){
											if(from>to){

												frmVal=to;
												toVal=from;
											}
											else{
												frmVal=from;
												toVal=to;
											}

											//0>=1 && 0<=3 --fail--not satisfied
											//0>=4 && 0<=6 --fail--not satisfied
											//0>=7 && 0<=10 --fail--not satisfied
											//0>=11 && 0<=20 --fail--not satisfied
											if(ruleAge>=frmVal && ruleAge<=toVal)
											{
												others=false;
												//log.info("map 2 when false :"+map2+" at :"+frmVal+"-"+toVal);
												break;
											}
											else
											{
												//log.info("map 2 when true :"+map2+" at :"+frmVal+"-"+toVal);
												others=true;
												break;
											}

										}
										else{

											log.info("any one of the bucket limits are null");

											if(to==null){
												String recAge=">="+from;
												//0>=21--fail

												//log.info(ruleAge+" is ruleAge at map 2 is "+map2);
												//log.info(from+" is frmVal at map 2 is "+map2);
												if(ruleAge>=from)
												{
													others=false;
													//log.info("map 2 when false :"+map2+" at :"+recAge);
													break;
												}
												else
												{
													others=true;
													//log.info("map 2 when true :"+map2+" at :"+recAge);
													break;
												}
											}
											if(from==null){
												String recAge="<="+to;
												if(ruleAge<=to)
												{
													//log.info("map 2 when false :"+map2+" at :"+recAge);
													others=false;
													break;
												}
												else
												{
													others=true;
													//log.info("map 2 when true :"+map2+" at :"+recAge);
													break;
												}
											}


										}
									}
								}
								//	log.info("in others group at :"+map2+"boolean is "+others);
								if(others)
									finalMap.add(map2); 
								ExportMap.add(exportmap2);
							}

						}
						else
						{
							finalMap.add(map2);
							ExportMap.add(exportmap2);
						}
					}

				}


				lhm.put("columnNames", finalColumnsList);
				lhm.put("detailList", finalMap);
				log.info("finalMap.size :"+finalMap.size());
				log.info("after adding values to final map"+ZonedDateTime.now());


				if(fileExport)
				{
					Set<String> keyset=ExportMap.get(0).keySet();
					List<String> keyList = new ArrayList<String>(keyset);
					log.info("keyList :"+keyList);


					if(fileType.equalsIgnoreCase("csv"))
					{
						response.setContentType ("application/csv");
						response.setHeader ("Content-Disposition", "attachment; filename=\"groupedData.csv\"");

						fileExportService.jsonToCSV(ExportMap,keyList,response.getWriter());
					}
					else if(fileType.equalsIgnoreCase("excel"))
					{
						/*response.setContentType("application/vnd.ms-excel");
					response.setHeader(
							"Content-Disposition",
							"attachment; filename=\"groupedData.xlsx\""
							);
					fileExportService.jsonToCSV(ExportMap, keyList,response.getWriter());*/

						response=fileExportService.exportToExcel(response, keyList, ExportMap);
					}

				}
			}
			catch (SQLException se) {
				log.info("Error while executing query: " + se);
			}
			catch(Exception ex)
			{
				log.info("Exception in getProcessedORUnProcesseGroupByColumnsInfoV4 :"+ex);
			}

			finally
			{
				if(resultDv!=null)
				{
					try {
						resultDv.close();
						log.info(" while closing resultDv in getProcessedORUnProcesseGroupByColumnsInfoV4 :");
						checkConnection = false;
					} catch (SQLException e) {
						log.info("exception while closing resultDv in getProcessedORUnProcesseGroupByColumnsInfoV4 :"+e);
					}

				}
				if(resultMaxCount!=null)
				{
					try {
						resultMaxCount.close();
						log.info(" while closing resultMaxCount in getProcessedORUnProcesseGroupByColumnsInfoV4 :");
						checkConnection = false;
					} catch (SQLException e) {
						log.info("exception while closing resultMaxCount in getProcessedORUnProcesseGroupByColumnsInfoV4 :"+e);
					}

				}


				if(resultTcAndAmtDv!=null)
				{
					try {
						resultTcAndAmtDv.close();
						log.info(" while closing resultTcAndAmtDv in getProcessedORUnProcesseGroupByColumnsInfoV4 :");
						checkConnection = false;
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						log.info("exception while closing resultTcAndAmtDv in getProcessedORUnProcesseGroupByColumnsInfoV4 :"+e);
					}

				}
				if(stmtDv!=null)
				{
					try {
						stmtDv.close();
						log.info(" while closing stmtDv in getProcessedORUnProcesseGroupByColumnsInfoV4 :");
						checkConnection = false;
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						log.info("exception while closing stmtDv in getProcessedORUnProcesseGroupByColumnsInfoV4 :"+e);
					}

				}
				if(stmtTcAndAmtDv!=null)
				{
					try {
						stmtTcAndAmtDv.close();
						log.info(" while closing stmtTcAndAmtDv in getProcessedORUnProcesseGroupByColumnsInfoV4 :");
						checkConnection = false;
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						log.info("exception while closing stmtTcAndAmtDv in getProcessedORUnProcesseGroupByColumnsInfoV4 :"+e);
					}

				}
				if(stmtMaxCount!=null)
				{
					try {
						stmtMaxCount.close();
						log.info(" while closing stmtMaxCount in getProcessedORUnProcesseGroupByColumnsInfoV4 :");
						checkConnection = false;
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						log.info("exception while closing stmtMaxCount in getProcessedORUnProcesseGroupByColumnsInfoV4 :"+e);
					}

				}

				if(conn!=null)
				{
					try {
						conn.close();
						log.info(" while closing conn in getProcessedORUnProcesseGroupByColumnsInfoV4 :");
						checkConnection = false;
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						log.info("exception while closing conn in getProcessedORUnProcesseGroupByColumnsInfoV4 :"+e);
					}

				}

			}

			//else
			log.info("after closing all connections:"+ZonedDateTime.now());
			log.info("Checking Connection Close in getProcessedORUnProcesseGroupByColumnsInfoV4:"+checkConnection);

		}
		return lhm;

	}
	
	
	
	
	
	
	
	
	@PostMapping("/getSrcTargetCombinationViewsByRuleGrpOfUnReconForViolationAndAging")
    @Timed
    public List<HashMap> getSrcTargetCombinationViewsByRuleGrpOfUnReconForViolationAndAging(HttpServletRequest request,@RequestParam String groupId, @RequestBody HashMap dates,
    		@RequestParam(value="age",required=false) String age,@RequestParam(value="violation",required=false) Integer violation,@RequestParam(value="bucketId",required=false) Long bucketId){
		log.info("Rest API to getSrcTargetCombinationViewsByRuleGrp for "+ "rule group id: "+ groupId);

		/** formatting date**/

		ZonedDateTime fmDate=ZonedDateTime.parse(dates.get("startDate").toString());
		ZonedDateTime toDate=ZonedDateTime.parse(dates.get("endDate").toString());
		log.info("fmDate :"+fmDate);
		log.info("toDate :"+toDate);
		java.time.LocalDate fDate=fmDate.toLocalDate();
		java.time.LocalDate tDate=toDate.toLocalDate();
		log.info("fDate :"+fDate);
		log.info("tDate :"+tDate);

		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		List<HashMap> finalMap = new ArrayList<HashMap>();

		RuleGroup ruleGrp = ruleGroupRepository.findByIdForDisplayAndTenantId(groupId, tenantId);

		if(ruleGrp != null)
		{
			List<HashMap> source = new ArrayList<HashMap>();

			log.info("Rule Group Name: "+ ruleGrp.getName());

			// Fetching Distinct Source and Target View Ids
			HashMap<String, List<BigInteger>> distinctViewIdMap = reconciliationResultService.getDistinctDVIdsforRuleGrp(ruleGrp.getId(), tenantId);
			List<BigInteger> distSrcIds = distinctViewIdMap.get("sourceViewIds");
		
			List<Long> ruleIds = ruleGroupDetailsRepository.fetchByRuleGroupIdAndTenantId(ruleGrp.getId(), tenantId);
			
		

			log.info("ruleIds: "+ruleIds);
			if(distSrcIds.size()>0)
			{
				for(BigInteger srcViewId : distSrcIds)
				{
					log.info("srcViewId: "+srcViewId);

					HashMap sourceMap = new HashMap();
					List<HashMap> innerTargetViews = new ArrayList<HashMap>();
					DataViews dv = dataViewsRepository.findOne(srcViewId.longValue());
					if(dv != null)
					{
						
						// Fetching Inner Target Views
						List<BigInteger> innerTrgtViews = rulesRepository.fetchDistinctTargetViewIdsBySourceId(srcViewId.longValue(), tenantId, ruleIds);
						log.info("innerTrgtViews: "+innerTrgtViews);
						if(innerTrgtViews.size()>0)
						{
							for(BigInteger innerTrgtViewId : innerTrgtViews)
							{       
								//log.info("Shv");
								LinkedHashMap srcTrgCombinationMap=dashBoardV4Service.getSrcTargetCombinationViewsByRuleGrpofUnReconciledForViolationAndAgingService(fDate, tDate, srcViewId, innerTrgtViewId.longValue(), ruleGrp.getId(), age, violation,bucketId);
								finalMap.add(srcTrgCombinationMap);
							}

						}
					}

				}
			}

		}
		return finalMap;
	}  
	
	
	
	/*@PostMapping("/getSrcViewsOfUnAccountedOrAccountedOrJeEnteredOrViolationAndAging")
    @Timed
    public List<HashMap> getSrcViewsOfUnAccountedOrAccountedOrJeEnteredOrViolationAndAging(HttpServletRequest request,@RequestParam String groupId, @RequestBody HashMap dates,
    		@RequestParam(value="age",required=false) String age,@RequestParam(value="violation",required=false) Integer violation,
    		@RequestParam String type,@RequestParam(value="bucketId",required=false) Long bucketId) throws SQLException{
		log.info("Rest API to getSrcTargetCombinationViewsByRuleGrp for "+ "rule group id: "+ groupId);


		
		*//** formatting date**//*
		log.info("**API startTime***"+ZonedDateTime.now());

		ZonedDateTime fmDate=ZonedDateTime.parse(dates.get("startDate").toString());
		ZonedDateTime toDate=ZonedDateTime.parse(dates.get("endDate").toString());
		log.info("fmDate :"+fmDate);
		log.info("toDate :"+toDate);
		java.time.LocalDate fDate=fmDate.toLocalDate();
		java.time.LocalDate tDate=toDate.toLocalDate();
		log.info("fDate :"+fDate);
		log.info("tDate :"+tDate);

		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		List<HashMap> finalMap = new ArrayList<HashMap>();

		RuleGroup ruleGrp = ruleGroupRepository.findByIdForDisplayAndTenantId(groupId, tenantId);

		if(ruleGrp != null)
		{
			List<HashMap> source = new ArrayList<HashMap>();

			log.info("Rule Group Name: "+ ruleGrp.getName());

			// Fetching Distinct Source and Target View Ids
			HashMap<String, List<BigInteger>> distinctViewIdMap = reconciliationResultService.getDistinctDVIdsforRuleGrp(ruleGrp.getId(), tenantId);
			List<BigInteger> distSrcIds = distinctViewIdMap.get("sourceViewIds");
			log.info("distSrcIds :"+distSrcIds);

			if(distSrcIds.size()>0)
			{
				for(BigInteger srcViewId : distSrcIds)
				{
					log.info("srcViewId: "+srcViewId);
					*//**to get fileDate or qualifier Date**//*

				     String date=dashBoardV4Service.getFileDateOrQualifier(srcViewId.longValue(), tenantId);

					HashMap sourceMap = new HashMap();
					List<HashMap> innerTargetViews = new ArrayList<HashMap>();
					DataViews dv = dataViewsRepository.findOne(srcViewId.longValue());
					if(dv != null)
					{
						List<LinkedHashMap> srcMaps=new ArrayList<LinkedHashMap>();
						if(age==null && violation==null)
							srcMaps=dashBoardV4Service.getSrcTargetCombinationViewsByRuleGrpofUnAccountedOrAccountedOrJeCreated(fDate, tDate, srcViewId,  ruleGrp.getId(), type,date);
						else
							srcMaps=dashBoardV4Service.getSrcTargetCombinationViewsByRuleGrpofUnAccountedForViolationAndAgingService(fDate, tDate, srcViewId, ruleGrp.getId(), age, violation,type,bucketId,date);
						finalMap.addAll(srcMaps);


					}

				}
			}

		}
		log.info("**API endTime***"+ZonedDateTime.now());
		return finalMap;
	}  */
	
	
	@PostMapping("/getSrcViewsOfUnAccountedOrAccountedOrJeEnteredOrViolationAndAging")
    @Timed
    public List<HashMap> getSrcViewsOfUnAccountedOrAccountedOrJeEnteredOrViolationAndAging(HttpServletRequest request,@RequestParam(value="groupId",required=false) String groupId, @RequestBody HashMap dates,
    		@RequestParam(value="age",required=false) String age,@RequestParam(value="violation",required=false) Integer violation,
    		@RequestParam String type,@RequestParam(value="bucketId",required=false) Long bucketId,@RequestParam(value="processId",required=false) String processId) throws SQLException{
		log.info("Rest API to getSrcTargetCombinationViewsByRuleGrp for "+ "rule group id: "+ groupId);

		Long recRuleGrpId=0l;

		HashMap map0=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map0.get("tenantId").toString());

		if(processId!=null && groupId==null)

		{
			Processes processes = processesRepository.findByTenantIdAndIdForDisplay(tenantId, processId);

			ProcessDetails procesRecDet=processDetailsRepository.findByProcessIdAndTagType(processes.getId(), "accountingRuleGroup");
			if(procesRecDet!=null)
				recRuleGrpId=procesRecDet.getTypeId();
		}
		else if(groupId!=null && processId==null)
		{
			RuleGroup ruleGrp=ruleGroupRepository.findByIdForDisplayAndTenantId(groupId, tenantId);
			recRuleGrpId=ruleGrp.getId();
		}

		/** formatting date**/
		log.info("**API startTime***"+ZonedDateTime.now());

		List<String> accountingType=new ArrayList<String>();
		if(type.equalsIgnoreCase("Accounted") || type.equalsIgnoreCase("Pending Journals"))
			accountingType.add("ACCOUNTED");
		if(type.equalsIgnoreCase("Je Created") || type.equalsIgnoreCase("JE Creation"))
			accountingType.add("JOURNALS_ENTERED");
		if(type.equalsIgnoreCase("un accounted") || type.equalsIgnoreCase("Not Accounted"))
		{
			accountingType.add("IN_PROCESS");
			accountingType.add("UN_ACCOUNTED");
		}

		ZonedDateTime fmDate=ZonedDateTime.parse(dates.get("startDate").toString());
		ZonedDateTime toDate=ZonedDateTime.parse(dates.get("endDate").toString());
		log.info("fmDate :"+fmDate);
		log.info("toDate :"+toDate);
		java.time.LocalDate fDate=fmDate.toLocalDate();
		java.time.LocalDate tDate=toDate.toLocalDate();
		log.info("fDate :"+fDate);
		log.info("tDate :"+tDate);





		List<HashMap> finalMap = new ArrayList<HashMap>();
		if(recRuleGrpId != null)
		{

			//log.info("ruleGrp.getId() :"+recRuleGrpId);


			HashMap<String, List<BigInteger>> distinctViewIdMap = reconciliationResultService.getDistinctDVIdsforRuleGrp(recRuleGrpId, tenantId);
			List<BigInteger> distSrcIds = distinctViewIdMap.get("sourceViewIds");
			log.info("distSrcIds :"+distSrcIds);

			if(distSrcIds.size()>0)
			{
				for(BigInteger srcViewId : distSrcIds)
				{
					List<BigInteger> ruleIds=rulesRepository.fetchRulesForSrcNViewsForAccounting(srcViewId.longValue(), recRuleGrpId);
					LinkedHashMap eachSrcMap=new LinkedHashMap();
					BigDecimal totalDvCount=appModuleSummaryRepository.fetchTotalDvcountForReconGroupByViewIdAndType(recRuleGrpId, fDate, tDate,srcViewId.longValue(),"ACCOUNTED");
					//log.info("totalDvCount :"+totalDvCount);
					BigDecimal totalDvAmt=appModuleSummaryRepository.fetchTotalDvAmtForReconGroupByViewIdAndType(recRuleGrpId, fDate, tDate,srcViewId.longValue(),"ACCOUNTED");
					//log.info("totalDvAmt :"+totalDvAmt);

					if(totalDvCount!=null && totalDvAmt!=null)
					{

						//log.info("accountingType :"+accountingType);
						List<Object[]> accountedSummary=appModuleSummaryRepository.fetchAccountingAndUnAccountingInfoFromAndToDateAndTypeAndViewId
								(recRuleGrpId,fDate,tDate,accountingType,srcViewId.longValue(),totalDvCount,totalDvAmt);



						//log.info("accountedSummary :"+accountedSummary.size());
						if(accountedSummary!=null && !accountedSummary.isEmpty())
						{
							DataViews dv=dataViewsRepository.findOne(srcViewId.longValue());

							String dateColumn=dashBoardV4Service.getFileDateOrQualifier(dv.getId(), tenantId);
							eachSrcMap.put("dateColumn", dateColumn);
							if(accountedSummary.get(0)[0]!=null)
								eachSrcMap.put("dvCount", Double.valueOf(accountedSummary.get(0)[0].toString()));
							else
								eachSrcMap.put("dvCount", Double.valueOf(0));
							if(accountedSummary.get(0)[2]!=null)
								eachSrcMap.put("dvAmt", Double.valueOf(accountedSummary.get(0)[2].toString()));
							else
								eachSrcMap.put("dvAmt", 0);
							eachSrcMap.put("viewId", dv.getId());
							eachSrcMap.put("viewIdDisplay", dv.getIdForDisplay());
							eachSrcMap.put("viewName", dv.getDataViewDispName());
							eachSrcMap.put("viewType", "Source");
							if(accountedSummary.get(0)[5]!=null)
								eachSrcMap.put("amountPer",Double.valueOf(accountedSummary.get(0)[5].toString()));
							else
								eachSrcMap.put("amountPer",Double.valueOf(0));
							if(accountedSummary.get(0)[4]!=null)
								eachSrcMap.put("countPer",Double.valueOf(accountedSummary.get(0)[4].toString()));
							else
								eachSrcMap.put("countPer",Double.valueOf(0));

							eachSrcMap.put("ruleIdList", ruleIds.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
							finalMap.add(eachSrcMap);

						}
						else
						{
							DataViews dv=dataViewsRepository.findOne(srcViewId.longValue());
							String dateColumn=dashBoardV4Service.getFileDateOrQualifier(dv.getId(), tenantId);
							eachSrcMap.put("dateColumn", dateColumn);

							eachSrcMap.put("dvCount", Double.valueOf(0));
							eachSrcMap.put("dvAmt", 0);
							eachSrcMap.put("viewId", dv.getId());
							eachSrcMap.put("viewIdDisplay", dv.getIdForDisplay());
							eachSrcMap.put("viewName", dv.getDataViewDispName());
							eachSrcMap.put("viewType", "Source");
							eachSrcMap.put("amountPer",Double.valueOf(0));
							eachSrcMap.put("countPer",Double.valueOf(0));
							eachSrcMap.put("ruleIdList", ruleIds.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
							finalMap.add(eachSrcMap);
						}
					}
					else
					{
						DataViews dv=dataViewsRepository.findOne(srcViewId.longValue());
						String dateColumn=dashBoardV4Service.getFileDateOrQualifier(dv.getId(), tenantId);
						eachSrcMap.put("dateColumn", dateColumn);
						eachSrcMap.put("dvCount", Double.valueOf(0));
						eachSrcMap.put("dvAmt", 0);
						eachSrcMap.put("viewId", dv.getId());
						eachSrcMap.put("viewIdDisplay", dv.getIdForDisplay());
						eachSrcMap.put("viewName", dv.getDataViewDispName());
						eachSrcMap.put("viewType", "Source");
						eachSrcMap.put("amountPer",Double.valueOf(0));
						eachSrcMap.put("countPer",Double.valueOf(0));
						eachSrcMap.put("ruleIdList", ruleIds.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
						finalMap.add(eachSrcMap);
					}
				}
			}


		}
		log.info("**API endTime***"+ZonedDateTime.now());
		return finalMap;
	}  
	
	
	
	
	
	
	
	
	
	
	
	@PostMapping("/awaitingApprovalsInfo")
	@Timed
	public List<LinkedHashMap> detailInformationForApprovals(HttpServletRequest request,@RequestParam String ruleGroupId,@RequestParam(value="viewId",required=false) Long viewId,@RequestBody HashMap dates,@RequestParam String module) throws ClassNotFoundException, SQLException
	{ 
		log.info("Rest Request to get aging analysis :"+dates);
		
		HashMap tenMap=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(tenMap.get("tenantId").toString());
		
		List<LinkedHashMap> finalMap=new ArrayList<LinkedHashMap>();

		//HashMap map=userJdbcService.getuserInfoFromToken(request);
		//Long tenantId=Long.parseLong(map.get("tenantId").toString());

		ZonedDateTime fmDate=ZonedDateTime.parse(dates.get("startDate").toString());

		ZonedDateTime toDate=ZonedDateTime.parse(dates.get("endDate").toString());

		log.info("fmDate :"+fmDate);

		log.info("toDate :"+toDate);

		LocalDate fDate=fmDate.toLocalDate();

		LocalDate tDate=toDate.toLocalDate();
		
		List<BigInteger> viewIdsList=new ArrayList<BigInteger>();
		RuleGroup rgId=ruleGroupRepository.findByIdForDisplayAndTenantId(ruleGroupId,tenantId);
		if(viewId!=null)
			viewIdsList.add(BigInteger.valueOf(viewId));
		else 
		{
			List<Long> recRuleIds= ruleGroupDetailsRepository.fetchByRuleGroupIdAndTenantId(rgId.getId(),tenantId);
			List<BigInteger> srcViewId=rulesRepository.fetchDistictSrcViewIdsByRuleId(recRuleIds);
			viewIdsList.addAll(srcViewId);

			List<BigInteger> trgViewId=rulesRepository.fetchDistictTargetViewIdsByRuleId(recRuleIds);
			viewIdsList.addAll(trgViewId);

			
		}
		
		String allDataViewIds=viewIdsList.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", "");
		log.info("allDataViewIds :"+allDataViewIds);
		
			RuleGroup appRuleGrp=ruleGroupRepository.findOne(rgId.getId());
			List<BigInteger> ruleIdList=ruleGroupDetailsRepository.fetchRuleIdsByGroupAndTenantId(appRuleGrp.getApprRuleGrpId(), tenantId);

			if(appRuleGrp.getApprRuleGrpId()!=null)
			{
				List<BigInteger> currentAppList=notificationBatchRepository.findCurrentApproversByRuleGroupId(appRuleGrp.getApprRuleGrpId());
				

				for(BigInteger currentApp:currentAppList)
				{
					List<NotificationBatch> notBatchList=notificationBatchRepository.findByCurrentApproverAndRuleGroupIdAndStatus(currentApp.longValue(),appRuleGrp.getApprRuleGrpId(),"IN_PROCESS");
					for(NotificationBatch notBatch:notBatchList)
					{
						Integer I = null;
						if(notBatch.getRefLevel()!=null)
						{
							I=notBatch.getRefLevel();
						}
						String refNum=String.valueOf(I);
						if(refNum.length()<=1)
							refNum="0"+refNum;
						log.info("refNum :"+refNum);

						String userAndBatchId =notBatch.getCurrentApprover()+"|"+notBatch.getId();
						log.info("userAndBatchId :"+userAndBatchId);
						String queryCount="";
						String queryTrgCount="";
						
						
						if(module.equalsIgnoreCase("reconciliation"))
						{
							queryCount="select originalRowId from ReconciliationResult where reconciliationRuleGroupId="+rgId.getId()+" and originalViewId in ("+allDataViewIds+") and originalRowId is not null and apprRef"+refNum+" LIKE '"+userAndBatchId+"%'";
							//log.info("queryCount :"+queryCount);
							queryTrgCount="select targetRowId from ReconciliationResult where reconciliationRuleGroupId="+rgId.getId()+" and targetViewId in ("+allDataViewIds+") and targetRowId is not null and apprRef"+refNum+" LIKE '"+userAndBatchId+"%'";
							//log.info("queryTrgCount :"+queryTrgCount);

						}
						else
						{
							queryCount="select originalRowId from AccountingData where acctGroupId="+rgId.getId()+" and (originalViewId in ("+allDataViewIds+")) and apprRef"+refNum+" LIKE '"+userAndBatchId+"%'";
						}

						//log.info("queryCount :"+queryCount);
						Query distReconRefQueryCount=em.createQuery(queryCount);
						List<BigInteger> count=distReconRefQueryCount.getResultList();
						if(!queryTrgCount.isEmpty() && queryTrgCount!=null)
						{
							Query distReconRefQueryCountForTarget=em.createQuery(queryTrgCount);
							List<BigInteger> counttrg=distReconRefQueryCountForTarget.getResultList();
							log.info("counttrg :"+counttrg.size());
							count.addAll(counttrg);
						}
						log.info("count.size :"+count);
						Integer appInprocessCt = 0;
						if(count.size()>0)
						{
							
						String srcIds=count.toString().replaceAll("\\[", "").replaceAll("\\]", "");
						for(int i=0;i<viewIdsList.size();i++)
						{
		    				String fileDateOrQualifierDate=dashBoardV4Service.getFileDateOrQualifierAndGetColumnName(viewIdsList.get(i).longValue(), notBatch.getTenantId());

						String totalInprocessQuery="select id from DataMaster "
								+ "where id in ("+srcIds+") and DATE("+fileDateOrQualifierDate+")>='"+fDate+"' and DATE("+fileDateOrQualifierDate+") <='"+tDate+"'";
						//log.info("totalInprocessQuery :"+totalInprocessQuery);
						Query inprocessQuery=em.createQuery(totalInprocessQuery);
						List countOfInprocess=inprocessQuery.getResultList();
						appInprocessCt=appInprocessCt+countOfInprocess.size();
						}
						}
						
						log.info("count.get(0) :"+count.size());
						log.info("appInprocessCt :"+appInprocessCt);
						LinkedHashMap appMap=new LinkedHashMap();
						appMap.put("batchId", notBatch.getId());
						appMap.put("batchName", notBatch.getNotificationName());
						HashMap map=userJdbcService.jdbcConnc(notBatch.getCurrentApprover(),notBatch.getTenantId());
						if(map!=null && map.get("assigneeName")!=null)
						{
							appMap.put("currentApprover",map.get("assigneeName"));
							appMap.put("count",count.size());
							List<ApprovalRuleAssignment> appRuleAsnmtList=approvalRuleAssignmentRepository.findByRuleId(notBatch.getRuleId());
							log.info("appRuleAsnmtList.size :"+appRuleAsnmtList.size());
							if(appRuleAsnmtList.size()>notBatch.getRefLevel())
							{
								HashMap map2=userJdbcService.jdbcConnc(appRuleAsnmtList.get(notBatch.getRefLevel()).getAssigneeId(),notBatch.getTenantId());
								appMap.put("nextApprover",map2.get("assigneeName"));
							}
							else
								appMap.put("nextApprover","none");
							if(appInprocessCt!=null && appInprocessCt>0)
							finalMap.add(appMap);
						}

					}
				}
			}


		//}

		return finalMap;
	}
	
	
	
	
	
	
	
	
	
	
	
	@PostMapping("/getProcessedORUnProcesseGroupByColumnsInfoV4ForAllAges")
	@Timed 
	public LinkedHashMap getProcessedORUnProcesseGroupByColumnsInfoV4ForAllAges(@RequestParam String ruleGroupId,@RequestParam Long viewId,@RequestParam String viewType,@RequestParam String rgType,
			@RequestBody HashMap dates,HttpServletRequest request,@RequestParam(value = "page" , required = false) Integer page,
    		@RequestParam(value = "per_page", required = false) Integer size,@RequestParam List<String> groupByColumnNames,@RequestParam(value = "age", required = false) String age,
    		@RequestParam(value = "violation", required = false) Integer violation,
    		@RequestParam(value="fileExport",required=false) boolean fileExport,
    		@RequestParam(value="fileType",required=false) String fileType,
    		HttpServletResponse response,@RequestParam Long bucketId, 
    		@RequestParam(required=false) String filterKey,@RequestParam(required=false) List<String> filterValues)
	{
		log.info("Rest Request to getUnReconciledOrUnAccountedGroupByInfoV4 with ruleGroupId: "+ruleGroupId+" viewId: "+viewId+" and  groupType: "+rgType+" viewType: "+viewType+" and groupByColumnNames:"+groupByColumnNames);
		Boolean checkConnection = false;
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		RuleGroup rgId=ruleGroupRepository.findByIdForDisplayAndTenantId(ruleGroupId, tenantId);
		ZonedDateTime fmDate=ZonedDateTime.parse(dates.get("startDate").toString());
		ZonedDateTime toDate=ZonedDateTime.parse(dates.get("endDate").toString());

		java.time.LocalDate fDate=fmDate.toLocalDate();
		java.time.LocalDate tDate=toDate.toLocalDate();

		String amountQualifier=reconciliationResultService.getViewColumnQualifier(BigInteger.valueOf(viewId), "AMOUNT");
		log.info("amountQualifier :"+amountQualifier);

		/**to get fileDate or qualifier Date**/

		String dateColumn=dashBoardV4Service.getFileDateOrQualifier(viewId, tenantId);
		int accountingSummary=0;


		List<BigInteger> reconciledOrAccountedIds=new ArrayList<BigInteger>();
		if(rgType.equalsIgnoreCase("un-reconciled") || rgType.equalsIgnoreCase("reconciled"))
		{

			if(viewType.equalsIgnoreCase("source"))
			{
				List<BigInteger> orginalRowIds=reconciliationResultRepository.fetchOrginalRowIdsByRuleGroupId(rgId.getId(),viewId);
				log.info("orginalRowIds :"+orginalRowIds.size());
				reconciledOrAccountedIds.addAll(orginalRowIds);
			}

			else if(viewType.equalsIgnoreCase("target"))
			{
				List<BigInteger> targetRowIds=reconciliationResultRepository.fetchTargetRowIdsByRuleGroupId(rgId.getId(),viewId);
				log.info("targetRowIds :"+targetRowIds.size());
				reconciledOrAccountedIds.addAll(targetRowIds);
			}
		}
		else if(rgType.equalsIgnoreCase("un-accounted"))
		{

			List<BigInteger> accountedRowIds=accountedSummaryRepository.fetchAccountingIdsByStatusNGroupIdNViewId(rgId.getId(), viewId,"ACCOUNTED");
			if(accountedRowIds.size()>0)
				reconciledOrAccountedIds.addAll(accountedRowIds);
			log.info("if unaccounted :"+accountedRowIds.size());
		}
		else if(rgType.equalsIgnoreCase("JE Pending"))
		{
			List<BigInteger> accountedRowIds=accountedSummaryRepository.fetchJEPendingIdsByStatusNGroupIdNViewId(rgId.getId(), viewId,"ACCOUNTED");
			if(accountedRowIds.size()>0)
				reconciledOrAccountedIds.addAll(accountedRowIds);
		}
		else if(rgType.equalsIgnoreCase("Je Created"))
		{
			List<BigInteger> accountedRowIds=accountedSummaryRepository.fetchPostedRowIdsByRuleGrpIdAndViewId(rgId.getId(), viewId);
			reconciledOrAccountedIds.addAll(accountedRowIds);
		}
		else if(rgType.equalsIgnoreCase("approvals"))
		{
			List<BigInteger> appInprocRowIds=accountingDataRepository.fetchDistinctApprovedInProcessRowIds(rgId.getId(), viewId,"IN_PROCESS");
			reconciledOrAccountedIds.addAll(appInprocRowIds);
		}

		String reconciledOrAccountedIdList=reconciledOrAccountedIds.toString().replaceAll("\\[", "").replaceAll("\\]", "");
		log.info("finalSrcIdList :"+reconciledOrAccountedIds.size());
		LinkedHashMap groupByColumns=new LinkedHashMap();
		groupByColumns=dashBoardV4Service.getDVGroupByColumns(viewId,true);

		LinkedHashMap finalColumnsList=new LinkedHashMap();
		finalColumnsList.put("amtQualifier", groupByColumns.get("amtQualifier"));


		LinkedHashMap groupByColumnsForQualifier=new LinkedHashMap();
		groupByColumnsForQualifier=dashBoardV4Service.getDVGroupByColumns(viewId,false);

		LinkedHashMap qualifierMap= (LinkedHashMap) groupByColumnsForQualifier.get("amtQualifier");
		//log.info("qualifierMap :"+qualifierMap);
		LinkedHashMap columnMap=new LinkedHashMap();
		columnMap.put(qualifierMap.get("amtQualifierAliasName"), qualifierMap.get("amtQualifier"));

		List<LinkedHashMap> finalGroupByColumnList=new ArrayList<LinkedHashMap>();
		//log.info("groupByColumnNames :"+groupByColumnNames);
		for(int i=0;i<groupByColumnNames.size();i++)
		{
			List<LinkedHashMap> lmp=(List<LinkedHashMap>) groupByColumns.get("columnsList");
			for(int c=0;c<lmp.size();c++)
			{
				String columnName=(lmp.get(c).get("columnAliasName")).toString();
				if(columnName.equalsIgnoreCase(groupByColumnNames.get(i)))
				{
					LinkedHashMap column=new LinkedHashMap();
					column.put("columnName", lmp.get(c).get("columnName"));
					column.put("columnAliasName",groupByColumnNames.get(i));
					column.put("dataType", lmp.get(c).get("dataType"));
					if(!lmp.get(c).get("dataType").toString().equalsIgnoreCase("Decimal"))
						column.put("align", "left");
					else
						column.put("align", "right");
					column.put("width", "150px");
					columnMap.put(groupByColumnNames.get(i), lmp.get(c).get("columnName"));
					finalGroupByColumnList.add(column);
				}
			}
		}
		//log.info("columnMap after setting evry thing:"+columnMap);
		
		if(rgType.equalsIgnoreCase("un-accounted"))
		{
			if(rgId.getActivityBased() != null && rgId.getActivityBased())
			{
				LinkedHashMap activityMap=new LinkedHashMap();
				activityMap.put("columnName", "Status");
				activityMap.put("columnAliasName","reconciliation_status");
				activityMap.put("align", "left");
				finalGroupByColumnList.add(activityMap);
				
			}
		}
		
		finalColumnsList.put("columnsList", finalGroupByColumnList);
		
		

		String groupByColNames =	groupByColumnNames.stream().collect(Collectors.joining("`,`", "`", "`"));
		log.info("groupByColNames :"+groupByColNames);
		Connection conn = null;
		Statement stmtDv = null;
		Statement stmtMaxCount = null;
		Statement stmtTcAndAmtDv = null;

		/* Filtration */
		/*if(filterObj!=null && !(filterObj.isEmpty())){
			Set keyset=filterObj.keySet();
			Iterator entries2 = filterObj.entrySet().iterator();
	     	String filterQuery="";
	     	int keysetSz=keyset.size();
	     	if(keysetSz>0){
	     		filterQuery=filterQuery+" where ";
	     	}
	     	int count=0;
			while (entries2.hasNext()) {
				count++;
				String filterSubQuery="";
			    Map.Entry entry = (Map.Entry) entries2.next();
			    String key = (String)entry.getKey();
			    List<String> valueList = (List<String>) entry.getValue();
			    System.out.println("key = " + key + ", valueList = " + valueList);
			    String valStr=valueList.stream().collect(Collectors.joining("','", "'", "'"));
			    filterSubQuery=filterSubQuery+key+" in ("+valStr+")";
			    filterQuery=filterQuery+filterSubQuery;
			    if(count>=0 && count<=keysetSz-1){
			    	filterQuery=filterQuery+" and ";
			    }
			    System.out.println("filterQuery at count : "+count+" is: "+filterQuery);
			}

			log.info("Final filterQuery: "+filterQuery);

		}*/
		String filterQuery="";
		if(filterKey!=null){
			String valStr=filterValues.stream().collect(Collectors.joining("','", "'", "'"));
			filterQuery=" and "+filterKey+" in ("+valStr+")";
		}
		ResultSet resultDv=null;
		ResultSet resultMaxCount=null;
		ResultSet resultTcAndAmtDv=null;
		LinkedHashMap lhm=new LinkedHashMap();

		try{
			DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
			conn = ds.getConnection();
			checkConnection = true;
			log.info("Checking Connection Open in getProcessedORUnProcesseGroupByColumnsInfoV4:"+checkConnection);
			log.info("Connected database successfully...");
			stmtDv = conn.createStatement();
			stmtTcAndAmtDv = conn.createStatement();
			stmtMaxCount = conn.createStatement();


			//	ResultSet resultAct=null;
			DataViews dvName=dataViewsRepository.findOne(viewId);




			String query="";
			String totalCtAndTotalAmt="";
			List<LinkedHashMap> finalMap=new ArrayList<LinkedHashMap>();
			List<LinkedHashMap> ExportMap=new ArrayList<LinkedHashMap>();

			//log.info("age :"+age);

			/*	String totalAmtCountQuery="select count(scrIds) as count,case when sum(`"+amountQualifier+"`) is null then 0 else sum(`"+amountQualifier+"`) end as amount from "
				+ "`"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where "+dateColumn+" >= '"+fDate+"' and "+dateColumn+" <='"+tDate+"'";

		Integer totalCount=0;
		Double totalAmount=0d;
		log.info("totalAmtCountQuery :"+totalAmtCountQuery);
		resultTcAndAmtDv=stmtTcAndAmtDv.executeQuery(totalAmtCountQuery);
		while(resultTcAndAmtDv.next())
		{
			totalCount=totalCount+Integer.valueOf(resultTcAndAmtDv.getString("count"));
			totalAmount=totalAmount+Double.valueOf(resultTcAndAmtDv.getString("amount"));
		}
		log.info("totalCount :"+totalCount);
		log.info("totalAmount :"+totalAmount);*/
			if(reconciledOrAccountedIds.size()>0)
			{
				if(page!=null && size!=null)
				{
					if(rgType.equalsIgnoreCase("un-reconciled") || rgType.equalsIgnoreCase("un-accounted"))
					{
						/**for violation query And ageing**/
						String totalAmtCountQuery="select count(scrIds) as count,case when sum(`"+amountQualifier+"`) is null then 0 else sum(`"+amountQualifier+"`) end as amount from "
								+ "`"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where scrIds not in ("+reconciledOrAccountedIdList+") and "+dateColumn+" >= '"+fDate+"' and "+dateColumn+" <='"+tDate+"'";

						Integer totalCount=0;
						Double totalAmount=0d;
						//log.info("totalAmtCountQuery :"+totalAmtCountQuery);
						resultTcAndAmtDv=stmtTcAndAmtDv.executeQuery(totalAmtCountQuery);
						while(resultTcAndAmtDv.next())
						{
							totalCount=totalCount+Integer.valueOf(resultTcAndAmtDv.getString("count"));
							totalAmount=totalAmount+Double.valueOf(resultTcAndAmtDv.getString("amount"));
						}
						log.info("totalCount :"+totalCount);
						log.info("totalAmount :"+totalAmount);



						if( age!=null)
						{
							String subQuery="";
							String subQuery2="";

							List<BucketDetails> buckDetails=bucketDetailsRepository.findByBucketId(bucketId);

							for(int j=0;j<buckDetails.size();j++){
								int buckVal=j+1;
								BucketDetails bucketDet=buckDetails.get(j);
								Long bucketDetId =bucketDet.getId();
								if(bucketDet!=null)
								{
									Integer frmVal=0;
									Integer toVal=0;
									Integer from=0;
									Integer to=0;
									from=bucketDet.getFromValue();
									to=bucketDet.getToValue();
									log.info("from: "+from+" to: "+to);

									if(from!=null && to!=null){
										if(from>to){

											frmVal=to;
											toVal=from;
										}
										else{
											frmVal=from;
											toVal=to;
										}


										String ruleAge=frmVal+"-"+toVal;
										subQuery=subQuery+" when DATEDIFF(CURDATE(),`"+dateColumn+"`) between "+frmVal+" and "+toVal+" then '"+ruleAge+"'";
										subQuery2=subQuery2+" when DATEDIFF(CURDATE(),`"+dateColumn+"`) between "+frmVal+" and "+toVal+" then "+buckVal;


									}
									else{

										log.info("any one of the bucket limits are null");

										if(to==null){
											String ruleAge=">="+from;
											subQuery=subQuery+" when DATEDIFF(CURDATE(),`"+dateColumn+"`) > "+from+" then '"+ruleAge+"'";
											subQuery2=subQuery2+" when DATEDIFF(CURDATE(),`"+dateColumn+"`) > "+from+" then "+buckVal;
										}
										if(from==null){
											String ruleAge="<="+to;
											subQuery=subQuery+" when DATEDIFF(CURDATE(),`"+dateColumn+"`) < "+to+" then '"+ruleAge+"'";
											subQuery2=subQuery2+" when DATEDIFF(CURDATE(),`"+dateColumn+"`) < "+to+" then "+buckVal;
										}


									}
								}
							}
							log.info("bucket query b4 others : "+subQuery);
							log.info("buckDetails.size() : "+buckDetails.size());
							subQuery=subQuery+" else 'Others' end as bucket";

							subQuery2=subQuery2+" else "+buckDetails.size()+1+" end as bucketNum";

							if(rgType.equalsIgnoreCase("un-accounted"))
							{
								RuleGroup recRgId=ruleGroupRepository.findOne(rgId.getId());

								if(recRgId.getActivityBased())
								{

									query="select case "+subQuery+","+groupByColNames+","
											+ "round(sum(`"+amountQualifier+"`),2) as `"+amountQualifier+"`,"
											+ "count(scrIds) as count,round((count(scrIds)/"+totalCount+"*100),2) as countPer,"
											+ "round((sum(`"+amountQualifier+"`)/"+totalAmount+"*100),2) as amtPer,"
											+ "case "+subQuery2+","
											+ "(CASE WHEN (recon_reference IS NULL) THEN 'Un-Reconciled' ELSE 'Reconciled' END) reconciliation_status from"
											+ "(select * from `"+dvName.getDataViewName().toLowerCase().toLowerCase()+"`"
											+ " where Date("+dateColumn+") >= '"+fDate+"' and Date("+dateColumn+") <='"+tDate+"' "
											+ "and scrIds not in ("+reconciledOrAccountedIdList+") "+filterQuery+") dv_unacc "
											+ "LEFT OUTER JOIN t_reconciliation_result recon ON dv_unacc.scrIds = recon.original_row_id  "
											+ "AND recon.original_view_id ="+dvName.getId()+" AND recon.reconciliation_rule_group_id="+recRgId.getReconciliationGroupId()+" and recon.recon_status = 'RECONCILED' and recon.current_record_flag is true group by "+groupByColNames+",bucket,bucketNum,reconciliation_status order by bucketNum asc Limit "+(page-1) * size+", "+size;

								}
								else
								{

									query="select case "+subQuery+","+groupByColNames+",round(sum(`"+amountQualifier+"`),2) as `"+amountQualifier+"`,count(scrIds) as count,round((count(scrIds)/"+totalCount+"*100),2) as countPer,round((sum(`"+amountQualifier+"`)/"+totalAmount+"*100),2) as amtPer,case "+subQuery2+" from `"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where Date("+dateColumn+") >= '"+fDate+"' and Date("+dateColumn+") <='"+tDate+"' "
											+ " and scrIds not in ("+reconciledOrAccountedIdList+") "+filterQuery+" group by "+groupByColNames+",bucket,bucketNum order by bucketNum asc Limit "+(page-1) * size+", "+size;
								}
							}
							else
							{

								query="select case "+subQuery+","+groupByColNames+",round(sum(`"+amountQualifier+"`),2) as `"+amountQualifier+"`,count(scrIds) as count,round((count(scrIds)/"+totalCount+"*100),2) as countPer,round((sum(`"+amountQualifier+"`)/"+totalAmount+"*100),2) as amtPer,case "+subQuery2+" from `"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where Date("+dateColumn+") >= '"+fDate+"' and Date("+dateColumn+") <='"+tDate+"' "
										+ " and scrIds not in ("+reconciledOrAccountedIdList+") "+filterQuery+" group by "+groupByColNames+",bucket,bucketNum order by bucketNum asc Limit "+(page-1) * size+", "+size;
							}

						}

						//log.info("query if not accounted :"+query);
					}



				}
				else
				{
					if(rgType.equalsIgnoreCase("un-reconciled") || rgType.equalsIgnoreCase("un-accounted"))
					{
						String totalAmtCountQuery="select count(scrIds) as count,case when sum(`"+amountQualifier+"`) is null then 0 else sum(`"+amountQualifier+"`) end as amount from "
								+ "`"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where scrIds not in ("+reconciledOrAccountedIdList+") and "+dateColumn+" >= '"+fDate+"' and "+dateColumn+" <='"+tDate+"'";

						Integer totalCount=0;
						Double totalAmount=0d;
						//log.info("totalAmtCountQuery :"+totalAmtCountQuery);
						resultTcAndAmtDv=stmtTcAndAmtDv.executeQuery(totalAmtCountQuery);
						while(resultTcAndAmtDv.next())
						{
							totalCount=totalCount+Integer.valueOf(resultTcAndAmtDv.getString("count"));
							totalAmount=totalAmount+Double.valueOf(resultTcAndAmtDv.getString("amount"));
						}
						log.info("totalCount :"+totalCount);
						log.info("totalAmount :"+totalAmount);

						if(age!=null)
						{


							String subQuery="";
							String subQuery2="";
							List<BucketDetails> buckDetails=bucketDetailsRepository.findByBucketId(bucketId);

							for(int j=0;j<buckDetails.size();j++){
								int buckVal=j+1;
								BucketDetails bucketDet=buckDetails.get(j);
								Long bucketDetId =bucketDet.getId();
								if(bucketDet!=null)
								{
									Integer frmVal=0;
									Integer toVal=0;
									Integer from=0;
									Integer to=0;
									from=bucketDet.getFromValue();
									to=bucketDet.getToValue();
									log.info("from: "+from+" to: "+to);

									if(from!=null && to!=null){
										if(from>to){

											frmVal=to;
											toVal=from;
										}
										else{
											frmVal=from;
											toVal=to;
										}


										String ruleAge=frmVal+"-"+toVal;
										subQuery=subQuery+" when DATEDIFF(CURDATE(),`"+dateColumn+"`) between "+frmVal+" and "+toVal+" then '"+ruleAge+"'";
										subQuery2=subQuery2+" when DATEDIFF(CURDATE(),`"+dateColumn+"`) between "+frmVal+" and "+toVal+" then "+buckVal;


									}
									else{

										log.info("any one of the bucket limits are null");

										if(to==null){
											String ruleAge=">="+from;
											subQuery=subQuery+" when DATEDIFF(CURDATE(),`"+dateColumn+"`) > "+from+" then '"+ruleAge+"'";
											subQuery2=subQuery2+" when DATEDIFF(CURDATE(),`"+dateColumn+"`) > "+from+" then "+buckVal;
										}
										if(from==null){
											String ruleAge="<="+to;
											subQuery=subQuery+" when DATEDIFF(CURDATE(),`"+dateColumn+"`) < "+to+" then '"+ruleAge+"'";
											subQuery2=subQuery2+" when DATEDIFF(CURDATE(),`"+dateColumn+"`) < "+to+" then "+buckVal;
										}


									}
								}
							}
							log.info("bucket query b4 others : "+subQuery);
							log.info("buckDetails.size() : "+buckDetails.size());
							subQuery=subQuery+" else 'Others' end as bucket";

							subQuery2=subQuery2+" else "+buckDetails.size()+1+" end as bucketNum";


							if(rgType.equalsIgnoreCase("un-accounted"))
							{

								RuleGroup recRgId=ruleGroupRepository.findOne(rgId.getId());

								if(recRgId.getActivityBased())
								{

									query="select case "+subQuery+","+groupByColNames+","
											+ "round(sum(`"+amountQualifier+"`),2) as `"+amountQualifier+"`,"
											+ "count(scrIds) as count,round((count(scrIds)/"+totalCount+"*100),2) as countPer,"
											+ "round((sum(`"+amountQualifier+"`)/"+totalAmount+"*100),2) as amtPer,"
											+ "case "+subQuery2+","
											+ "(CASE WHEN (recon_reference IS NULL) THEN 'Un-Reconciled' ELSE 'Reconciled' END) reconciliation_status from"
											+ "(select * from `"+dvName.getDataViewName().toLowerCase().toLowerCase()+"`"
											+ " where Date("+dateColumn+") >= '"+fDate+"' and Date("+dateColumn+") <='"+tDate+"' "
											+ "and scrIds not in ("+reconciledOrAccountedIdList+") "+filterQuery+") dv_unacc "
											+ "LEFT OUTER JOIN t_reconciliation_result recon ON dv_unacc.scrIds = recon.original_row_id  "
											+ "AND recon.original_view_id ="+dvName.getId()+" AND recon.reconciliation_rule_group_id="+recRgId.getReconciliationGroupId()+" and recon.recon_status = 'RECONCILED' and recon.current_record_flag is true group by "+groupByColNames+",bucket,bucketNum,reconciliation_status order by bucketNum asc";

								}
								else
								{
									query="select case "+subQuery+","+groupByColNames+",round(sum(`"+amountQualifier+"`),2) as `"+amountQualifier+"`,count(scrIds) as count,round((count(scrIds)/"+totalCount+"*100),2) as countPer,round((sum(`"+amountQualifier+"`)/"+totalAmount+"*100),2) as amtPer,case "+subQuery2+" from `"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where Date("+dateColumn+") >= '"+fDate+"' and Date("+dateColumn+") <='"+tDate+"' "
											+ " and scrIds not in ("+reconciledOrAccountedIdList+") "+filterQuery+" group by "+groupByColNames+",bucket,bucketNum order by bucketNum asc";
								}
							}
							else
							{
								query="select case "+subQuery+","+groupByColNames+",round(sum(`"+amountQualifier+"`),2) as `"+amountQualifier+"`,count(scrIds) as count,round((count(scrIds)/"+totalCount+"*100),2) as countPer,round((sum(`"+amountQualifier+"`)/"+totalAmount+"*100),2) as amtPer,case "+subQuery2+" from `"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where Date("+dateColumn+") >= '"+fDate+"' and Date("+dateColumn+") <='"+tDate+"' "
										+ " and scrIds not in ("+reconciledOrAccountedIdList+") "+filterQuery+" group by "+groupByColNames+",bucket,bucketNum order by bucketNum asc";	
							}



						}

					}

				}

			}
			else
			{

				log.info("if size is zero");

				String totalAmtCountQuery="select count(scrIds) as count,case when sum(`"+amountQualifier+"`) is null then 0 else sum(`"+amountQualifier+"`) end as amount from "
						+ "`"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where "+dateColumn+" >= '"+fDate+"' and "+dateColumn+" <='"+tDate+"'";

				Integer totalCount=0;
				Double totalAmount=0d;
				//log.info("totalAmtCountQuery :"+totalAmtCountQuery);
				resultTcAndAmtDv=stmtTcAndAmtDv.executeQuery(totalAmtCountQuery);
				while(resultTcAndAmtDv.next())
				{
					totalCount=totalCount+Integer.valueOf(resultTcAndAmtDv.getString("count"));
					totalAmount=totalAmount+Double.valueOf(resultTcAndAmtDv.getString("amount"));
				}
				log.info("totalCount :"+totalCount);
				log.info("totalAmount :"+totalAmount);


				if(page!=null && size!=null)
				{


					if((age !=null) && !(rgType.equalsIgnoreCase("reconciled") || rgType.equalsIgnoreCase("JE Created") || rgType.equalsIgnoreCase("JE Pending")))
					{


						String subQuery2="";
						String subQuery="";
						List<BucketDetails> buckDetails=bucketDetailsRepository.findByBucketId(bucketId);

						for(int j=0;j<buckDetails.size();j++){
							int buckVal=j+1;
							BucketDetails bucketDet=buckDetails.get(j);
							Long bucketDetId =bucketDet.getId();
							if(bucketDet!=null)
							{
								Integer frmVal=0;
								Integer toVal=0;
								Integer from=0;
								Integer to=0;
								from=bucketDet.getFromValue();
								to=bucketDet.getToValue();
								log.info("from: "+from+" to: "+to);

								if(from!=null && to!=null){
									if(from>to){

										frmVal=to;
										toVal=from;
									}
									else{
										frmVal=from;
										toVal=to;
									}


									String ruleAge=frmVal+"-"+toVal;
									subQuery=subQuery+" when DATEDIFF(CURDATE(),`"+dateColumn+"`) between "+frmVal+" and "+toVal+" then '"+ruleAge+"'";
									subQuery2=subQuery2+" when DATEDIFF(CURDATE(),`"+dateColumn+"`) between "+frmVal+" and "+toVal+" then "+buckVal;


								}
								else{

									log.info("any one of the bucket limits are null");

									if(to==null){
										String ruleAge=">="+from;
										subQuery=subQuery+" when DATEDIFF(CURDATE(),`"+dateColumn+"`) > "+from+" then '"+ruleAge+"'";
										subQuery2=subQuery2+" when DATEDIFF(CURDATE(),`"+dateColumn+"`) > "+from+" then "+buckVal;
									}
									if(from==null){
										String ruleAge="<="+to;
										subQuery=subQuery+" when DATEDIFF(CURDATE(),`"+dateColumn+"`) < "+to+" then '"+ruleAge+"'";
										subQuery2=subQuery2+" when DATEDIFF(CURDATE(),`"+dateColumn+"`) < "+to+" then "+buckVal;
									}


								}
							}
						}
						log.info("bucket query b4 others : "+subQuery);
						log.info("buckDetails.size() : "+buckDetails.size());
						subQuery=subQuery+" else 'Others' end as bucket";

						subQuery2=subQuery2+" else "+buckDetails.size()+1+" end as bucketNum";


						if(rgType.equalsIgnoreCase("un-accounted"))
						{
							RuleGroup recRgId=ruleGroupRepository.findOne(rgId.getId());

							if(recRgId.getActivityBased())
							{

								query="select case "+subQuery+","+groupByColNames+","
										+ "round(sum(`"+amountQualifier+"`),2) as `"+amountQualifier+"`,"
										+ "count(scrIds) as count,round((count(scrIds)/"+totalCount+"*100),2) as countPer,"
										+ "round((sum(`"+amountQualifier+"`)/"+totalAmount+"*100),2) as amtPer,"
										+ "case "+subQuery2+","
										+ "(CASE WHEN (recon_reference IS NULL) THEN 'Un-Reconciled' ELSE 'Reconciled' END) reconciliation_status from"
										+ "(select * from `"+dvName.getDataViewName().toLowerCase().toLowerCase()+"`"
										+ " where Date("+dateColumn+") >= '"+fDate+"' and Date("+dateColumn+") <='"+tDate+"' "
										+filterQuery+") dv_unacc "
										+ "LEFT OUTER JOIN t_reconciliation_result recon ON dv_unacc.scrIds = recon.original_row_id  "
										+ "AND recon.original_view_id ="+dvName.getId()+" AND recon.reconciliation_rule_group_id="+recRgId.getReconciliationGroupId()+" and recon.recon_status = 'RECONCILED' and recon.current_record_flag is true group by "+groupByColNames+",bucket,bucketNum,reconciliation_status order by bucketNum asc Limit "+(page-1) * size+", "+size;

							}
							else
							{

								query="select case "+subQuery+","+groupByColNames+",round(sum(`"+amountQualifier+"`),2) as `"+amountQualifier+"`,count(scrIds) as count,round((count(scrIds)/"+totalCount+"*100),2) as countPer,"
										+ "round((sum(`"+amountQualifier+"`)/"+totalAmount+"*100),2) as amtPer,case "+subQuery2+" from `"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` "
										+ "where Date(fileDate) >= '"+fDate+"' and Date(fileDate) <='"+tDate+"' "+filterQuery
										+ "group by "+groupByColNames+",bucket,bucketNum order by bucketNum asc Limit "+(page-1) * size+", "+size;

							}
						}
						else
						{

							query="select case "+subQuery+","+groupByColNames+",round(sum(`"+amountQualifier+"`),2) as `"+amountQualifier+"`,count(scrIds) as count,round((count(scrIds)/"+totalCount+"*100),2) as countPer,"
									+ "round((sum(`"+amountQualifier+"`)/"+totalAmount+"*100),2) as amtPer,case "+subQuery2+" from `"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` "
									+ "where Date(fileDate) >= '"+fDate+"' and Date(fileDate) <='"+tDate+"' "+filterQuery
									+ "group by "+groupByColNames+",bucket,bucketNum order by bucketNum asc Limit "+(page-1) * size+", "+size;

						}


					}

				}
				else
				{
					if(( age!=null) && !(rgType.equalsIgnoreCase("reconciled") || rgType.equalsIgnoreCase("JE Created") || rgType.equalsIgnoreCase("JE Pending")))
					{




						String subQuery="";
						String subQuery2="";
						List<BucketDetails> buckDetails=bucketDetailsRepository.findByBucketId(bucketId);

						for(int j=0;j<buckDetails.size();j++){
							int buckVal=j+1;
							BucketDetails bucketDet=buckDetails.get(j);
							Long bucketDetId =bucketDet.getId();
							if(bucketDet!=null)
							{
								Integer frmVal=0;
								Integer toVal=0;
								Integer from=0;
								Integer to=0;
								from=bucketDet.getFromValue();
								to=bucketDet.getToValue();
								log.info("from: "+from+" to: "+to);

								if(from!=null && to!=null){
									if(from>to){

										frmVal=to;
										toVal=from;
									}
									else{
										frmVal=from;
										toVal=to;
									}


									String ruleAge=frmVal+"-"+toVal;
									subQuery=subQuery+" when DATEDIFF(CURDATE(),`"+dateColumn+"`) between "+frmVal+" and "+toVal+" then '"+ruleAge+"'";
									subQuery2=subQuery2+" when DATEDIFF(CURDATE(),`"+dateColumn+"`) between "+frmVal+" and "+toVal+" then "+buckVal;


								}
								else{

									log.info("any one of the bucket limits are null");

									if(to==null){
										String ruleAge=">="+from;
										subQuery=subQuery+" when DATEDIFF(CURDATE(),`"+dateColumn+"`) > "+from+" then '"+ruleAge+"'";
										subQuery2=subQuery2+" when DATEDIFF(CURDATE(),`"+dateColumn+"`) > "+from+" then "+buckVal;
									}
									if(from==null){
										String ruleAge="<="+to;
										subQuery=subQuery+" when DATEDIFF(CURDATE(),`"+dateColumn+"`) < "+to+" then '"+ruleAge+"'";
										subQuery2=subQuery2+" when DATEDIFF(CURDATE(),`"+dateColumn+"`) < "+to+" then "+buckVal;
									}


								}
							}
						}
						log.info("buckDetails.size() :"+buckDetails.size());
						log.info("bucket query b4 others : "+subQuery);
						subQuery=subQuery+" else 'Others' end as bucket";
						subQuery2=subQuery2+" else "+buckDetails.size()+1+" end as bucketNum";

						if(rgType.equalsIgnoreCase("un-accounted"))
						{
							RuleGroup recRgId=ruleGroupRepository.findOne(rgId.getId());

							if(recRgId.getActivityBased())
							{

								query="select case "+subQuery+","+groupByColNames+","
										+ "round(sum(`"+amountQualifier+"`),2) as `"+amountQualifier+"`,"
										+ "count(scrIds) as count,round((count(scrIds)/"+totalCount+"*100),2) as countPer,"
										+ "round((sum(`"+amountQualifier+"`)/"+totalAmount+"*100),2) as amtPer,"
										+ "case "+subQuery2+","
										+ "(CASE WHEN (recon_reference IS NULL) THEN 'Un-Reconciled' ELSE 'Reconciled' END) reconciliation_status from"
										+ "(select * from `"+dvName.getDataViewName().toLowerCase().toLowerCase()+"`"
										+ " where Date("+dateColumn+") >= '"+fDate+"' and Date("+dateColumn+") <='"+tDate+"' "
										+ filterQuery+") dv_unacc "
										+ "LEFT OUTER JOIN t_reconciliation_result recon ON dv_unacc.scrIds = recon.original_row_id "
										+ "AND recon.original_view_id ="+dvName.getId()+" AND recon.reconciliation_rule_group_id="+recRgId.getReconciliationGroupId()+" and recon.recon_status = 'RECONCILED' and recon.current_record_flag is true group by "+groupByColNames+",bucket,bucketNum,reconciliation_status order by bucketNum asc";

							}
						}
						else
						{

							query="select case "+subQuery+","+groupByColNames+",round(sum(`"+amountQualifier+"`),2) as `"+amountQualifier+"`,count(scrIds) as count,round((count(scrIds)/"+totalCount+"*100),2) as countPer,round((sum(`"+amountQualifier+"`)/"+totalAmount+"*100),2) as amtPer,case "+subQuery2+" from `"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where Date("+dateColumn+") >= '"+fDate+"' and Date("+dateColumn+") <='"+tDate+"' "
									+filterQuery+ "group by "+groupByColNames+",bucket,bucketNum order by bucketNum asc";
						}






					}

				}


			}
			//log.info("query in getProcessedORUnProcesseGroupByColumnsInfoV4 :"+query);
			if(!query.isEmpty())
			{
				String maxCtQuery="";
				//log.info("page :"+page+" and size :"+size);
				if(page!=null && size!=null)
					maxCtQuery=query.replaceAll("Limit "+(page-1) * size+", "+size, "");
				else
					maxCtQuery=query;
				//log.info("maxCtQuery :"+maxCtQuery);
				//log.info("*********query*******:"+query);
				resultMaxCount=stmtMaxCount.executeQuery(maxCtQuery);
				
				resultDv=stmtDv.executeQuery(query);
				ResultSetMetaData rsmd2 = resultDv.getMetaData();
				int columnCount = rsmd2.getColumnCount();
				Double maxAmtValue=0d;
				Long mxCtValue=0l;
				while(resultMaxCount.next())
				{
					if(mxCtValue<Long.valueOf(resultMaxCount.getString("count").toString()))
						mxCtValue=Long.valueOf(resultMaxCount.getString("count").toString());
					if(maxAmtValue<Double.valueOf(resultMaxCount.getString(amountQualifier).toString()))
						maxAmtValue=Double.valueOf(resultMaxCount.getString(amountQualifier).toString());

				}
				log.info("mxCtValue :"+mxCtValue);
				log.info("maxAmtValue :"+maxAmtValue);

				while(resultDv.next())
				{

					LinkedHashMap map2=new LinkedHashMap();
					LinkedHashMap exportmap2=new LinkedHashMap();

					for (int i = 1; i <= columnCount; i++ ) {  
						String name=rsmd2.getColumnName(i);
						if(name.equalsIgnoreCase(amountQualifier))
						{
							//log.info("rsmd2.getColumnName(i) **:"+rsmd2.getColumnName(i));
							//log.info("columnMap ** :"+columnMap);
							if(resultDv.getString(rsmd2.getColumnName(i))!=null)
								map2.put(rsmd2.getColumnName(i), Double.valueOf(resultDv.getString(rsmd2.getColumnName(i))));
							else
								map2.put(rsmd2.getColumnName(i), 0d);

							if(resultDv.getString(rsmd2.getColumnName(i))!=null)
								exportmap2.put("Amount", Double.valueOf(resultDv.getString(rsmd2.getColumnName(i))));
							//exportmap2.put(columnMap.get(rsmd2.getColumnName(i)), Double.valueOf(resultDv.getString(rsmd2.getColumnName(i))));
							else
								exportmap2.put("Amount", 0d);



							Double avgAmtPer=0d;
							if(maxAmtValue>0)
								avgAmtPer=(Double.valueOf(resultDv.getString(rsmd2.getColumnName(i)))/maxAmtValue)*100;

							map2.put("avgAmtPer", avgAmtPer);
							//exportmap2.put("avgAmtPer", avgAmtPer);

						}
						else if(name.equalsIgnoreCase("count"))
						{
							map2.put(rsmd2.getColumnName(i), Integer.valueOf(resultDv.getString(rsmd2.getColumnName(i))));

							exportmap2.put("Count", Integer.valueOf(resultDv.getString(rsmd2.getColumnName(i))));

							Double avgCtPer=0d;
							if(mxCtValue>0)
								avgCtPer=(Double.valueOf(resultDv.getString(rsmd2.getColumnName(i)))/mxCtValue)*100;

							map2.put("avgCtPer", avgCtPer);

							//exportmap2.put("avgCtPer", avgCtPer);

						}
						else if(name.equalsIgnoreCase("countPer"))
						{

							if(resultDv.getString(rsmd2.getColumnName(i))!=null)
								map2.put(rsmd2.getColumnName(i), Double.valueOf(resultDv.getString(rsmd2.getColumnName(i))));
							else
								map2.put(rsmd2.getColumnName(i), 0d);


							if(resultDv.getString(rsmd2.getColumnName(i))!=null)
								exportmap2.put("Count Percentage", Double.valueOf(resultDv.getString(rsmd2.getColumnName(i))));
							else
								exportmap2.put("Count Percentage", 0d);
						}
						else if(name.equalsIgnoreCase("amtPer"))
						{
							if(resultDv.getString(rsmd2.getColumnName(i))!=null)
								map2.put(rsmd2.getColumnName(i), Double.valueOf(resultDv.getString(rsmd2.getColumnName(i))));
							else
								map2.put(rsmd2.getColumnName(i), 0d);

							/*if(resultDv.getString(rsmd2.getColumnName(i))!=null)
								exportmap2.put(rsmd2.getColumnName(i), Double.valueOf(resultDv.getString(rsmd2.getColumnName(i))));
							else
								exportmap2.put(rsmd2.getColumnName(i), 0d);*/
						}
						else if(name.equalsIgnoreCase("bucket"))
						{

							if(resultDv.getString(rsmd2.getColumnName(i))!=null)
								map2.put("age", resultDv.getString(rsmd2.getColumnName(i)));
							else
								map2.put("age", 0d);


							if(resultDv.getString(rsmd2.getColumnName(i))!=null)
								exportmap2.put("Age", resultDv.getString(rsmd2.getColumnName(i)));
							else
								exportmap2.put("Age", 0d);
						}
						else
						{
							//log.info("rsmd2.getColumnName(i) **:"+rsmd2.getColumnName(i));
							//log.info("columnMap ** :"+columnMap);

							if(resultDv.getString(rsmd2.getColumnName(i))!=null)
								map2.put(rsmd2.getColumnName(i), resultDv.getString(rsmd2.getColumnName(i)));
							else
								map2.put(rsmd2.getColumnName(i), "blank");

							if(resultDv.getString(rsmd2.getColumnName(i))!=null)
								exportmap2.put(columnMap.get(rsmd2.getColumnName(i)), resultDv.getString(rsmd2.getColumnName(i)));
							else
								exportmap2.put(columnMap.get(rsmd2.getColumnName(i)), "blank");
						}

					}



					if(fileExport)
					{
						if(age.equalsIgnoreCase("all"))
							finalMap.add(exportmap2);
						else if(age.equalsIgnoreCase(resultDv.getString("bucket")))
							finalMap.add(exportmap2);

					}
					else
					{
						if(age.equalsIgnoreCase("all"))
							finalMap.add(map2);
						else if(age.equalsIgnoreCase(resultDv.getString("bucket")))
							finalMap.add(map2);
					}
					/*else
					finalMap.add(map2);*/

				}

			}


			lhm.put("columnNames", finalColumnsList);
			lhm.put("detailList", finalMap);
			log.info("finalMap.size :"+finalMap.size());
			log.info("after adding values to final map"+ZonedDateTime.now());


			if(fileExport)
			{
				Set<String> keyset=ExportMap.get(0).keySet();
				List<String> keyList = new ArrayList<String>(keyset);
				log.info("keyList :"+keyList);


				if(fileType.equalsIgnoreCase("csv"))
				{
					response.setContentType ("application/csv");
					response.setHeader ("Content-Disposition", "attachment; filename=\"groupedData.csv\"");

					fileExportService.jsonToCSV(ExportMap,keyList,response.getWriter());
				}
				else if(fileType.equalsIgnoreCase("excel"))
				{
					/*response.setContentType("application/vnd.ms-excel");
					response.setHeader(
							"Content-Disposition",
							"attachment; filename=\"groupedData.xlsx\""
							);
					fileExportService.jsonToCSV(ExportMap, keyList,response.getWriter());*/

					response=fileExportService.exportToExcel(response, keyList, ExportMap);
				}

			}
		}
		catch (SQLException se) {
			log.info("Error while executing query: " + se);
		}
		catch(Exception ex)
		{
			log.info("Exception in getProcessedORUnProcesseGroupByColumnsInfoV4 :"+ex);
		}

		finally
		{
			if(resultDv!=null)
			{
				try {
					resultDv.close();
					log.info(" while closing resultDv in getProcessedORUnProcesseGroupByColumnsInfoV4 :");
					checkConnection = false;
				} catch (SQLException e) {
					log.info("exception while closing resultDv in getProcessedORUnProcesseGroupByColumnsInfoV4 :"+e);
				}

			}
			if(resultMaxCount!=null)
			{
				try {
					resultMaxCount.close();
					log.info(" while closing resultMaxCount in getProcessedORUnProcesseGroupByColumnsInfoV4 :");
					checkConnection = false;
				} catch (SQLException e) {
					log.info("exception while closing resultMaxCount in getProcessedORUnProcesseGroupByColumnsInfoV4 :"+e);
				}

			}


			if(resultTcAndAmtDv!=null)
			{
				try {
					resultTcAndAmtDv.close();
					log.info(" while closing resultTcAndAmtDv in getProcessedORUnProcesseGroupByColumnsInfoV4 :");
					checkConnection = false;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					log.info("exception while closing resultTcAndAmtDv in getProcessedORUnProcesseGroupByColumnsInfoV4 :"+e);
				}

			}
			if(stmtDv!=null)
			{
				try {
					stmtDv.close();
					log.info(" while closing stmtDv in getProcessedORUnProcesseGroupByColumnsInfoV4 :");
					checkConnection = false;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					log.info("exception while closing stmtDv in getProcessedORUnProcesseGroupByColumnsInfoV4 :"+e);
				}

			}
			if(stmtTcAndAmtDv!=null)
			{
				try {
					stmtTcAndAmtDv.close();
					log.info(" while closing stmtTcAndAmtDv in getProcessedORUnProcesseGroupByColumnsInfoV4 :");
					checkConnection = false;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					log.info("exception while closing stmtTcAndAmtDv in getProcessedORUnProcesseGroupByColumnsInfoV4 :"+e);
				}

			}
			if(stmtMaxCount!=null)
			{
				try {
					stmtMaxCount.close();
					log.info(" while closing stmtMaxCount in getProcessedORUnProcesseGroupByColumnsInfoV4 :");
					checkConnection = false;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					log.info("exception while closing stmtMaxCount in getProcessedORUnProcesseGroupByColumnsInfoV4 :"+e);
				}

			}

			if(conn!=null)
			{
				try {
					conn.close();
					log.info(" while closing conn in getProcessedORUnProcesseGroupByColumnsInfoV4 :");
					checkConnection = false;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					log.info("exception while closing conn in getProcessedORUnProcesseGroupByColumnsInfoV4 :"+e);
				}

			}

		}

		//else
		log.info("after closing all connections:"+ZonedDateTime.now());
		log.info("Checking Connection Close in getProcessedORUnProcesseGroupByColumnsInfoV4:"+checkConnection);

		return lhm;

	}
	
	
}

