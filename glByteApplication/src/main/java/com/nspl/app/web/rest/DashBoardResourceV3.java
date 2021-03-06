package com.nspl.app.web.rest;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.lang.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.config.ApplicationContextProvider;
import com.nspl.app.domain.ApplicationPrograms;
import com.nspl.app.domain.DataStaging;
import com.nspl.app.domain.DataViews;
import com.nspl.app.domain.DataViewsColumns;
import com.nspl.app.domain.FileTemplateLines;
import com.nspl.app.domain.FileTemplates;
import com.nspl.app.domain.LookUpCode;
import com.nspl.app.domain.ProcessDetails;
import com.nspl.app.domain.Processes;
import com.nspl.app.domain.RuleGroup;
import com.nspl.app.domain.Rules;
import com.nspl.app.domain.SourceFileInbHistory;
import com.nspl.app.domain.SourceProfileFileAssignments;
import com.nspl.app.domain.SourceProfiles;
import com.nspl.app.repository.AccountedSummaryRepository;
import com.nspl.app.repository.AppModuleSummaryRepository;
import com.nspl.app.repository.ApplicationProgramsRepository;
import com.nspl.app.repository.DataStagingRepository;
import com.nspl.app.repository.DataViewsColumnsRepository;
import com.nspl.app.repository.DataViewsRepository;
import com.nspl.app.repository.FileTemplateLinesRepository;
import com.nspl.app.repository.FileTemplatesRepository;
import com.nspl.app.repository.JobDetailsRepository;
import com.nspl.app.repository.LookUpCodeRepository;
import com.nspl.app.repository.ProcessDetailsRepository;
import com.nspl.app.repository.ProcessesRepository;
import com.nspl.app.repository.ReconciliationResultRepository;
import com.nspl.app.repository.RuleGroupDetailsRepository;
import com.nspl.app.repository.RuleGroupRepository;
import com.nspl.app.repository.RulesRepository;
import com.nspl.app.repository.SourceFileInbHistoryRepository;
import com.nspl.app.repository.SourceProfileFileAssignmentsRepository;
import com.nspl.app.repository.SourceProfilesRepository;
import com.nspl.app.service.DashBoardV2Service;
import com.nspl.app.service.DashBoardV3Service;
import com.nspl.app.service.DashBoardV4Service;
import com.nspl.app.service.ReconciliationResultService;
import com.nspl.app.service.UserJdbcService;


@RestController
@RequestMapping("/api")
public class DashBoardResourceV3 {
	private final Logger log = LoggerFactory.getLogger(DashBoardResourceV3.class);

	@Inject
	AppModuleSummaryRepository appModuleSummaryRepository;

	@Inject
	ProcessDetailsRepository processDetailsRepository;

	@Inject
	DashBoardV3Service dashBoardV3Service;

	@Inject
	DataViewsRepository dataViewsRepository;
	
	
	@Inject
	DashBoardV2Service dashBoardV2Service;


	@Inject
	ReconciliationResultRepository reconciliationResultRepository;


	@Inject
	ProcessesRepository processesRepository;

	@Inject
	RulesRepository rulesRepository;


	@Autowired
	Environment env;


	@Inject
	DataViewsColumnsRepository dataViewsColumnsRepository;


	@Inject
	FileTemplateLinesRepository fileTemplateLinesRepository;

	@Inject
	UserJdbcService userJdbcService;


	@Inject
	AccountedSummaryRepository accountedSummaryRepository;
	
	
	@Inject
	SourceFileInbHistoryRepository sourceFileInbHistoryRepository;
	
	
	@Inject
	JobDetailsRepository jobDetailsRepository;
	
	@Inject
	ApplicationProgramsRepository applicationProgramsRepository;
	
	@Inject
	SourceProfileFileAssignmentsRepository sourceProfileFileAssignmentsRepository;
	
	@Inject
	FileTemplatesRepository fileTemplatesRepository;
	
	
	@Inject
	LookUpCodeRepository lookUpCodeRepository;
	
	@Inject
	SourceProfilesRepository sourceProfilesRepository;

	
	@PersistenceContext(unitName="default")
	private EntityManager em;
	
	@Inject
	RuleGroupRepository ruleGroupRepository;
	
	@Inject
	ReconciliationResultService reconciliationResultService;
	
	@Inject
	RuleGroupDetailsRepository ruleGroupDetailsRepository;
	
	
	@Inject
	DataStagingRepository dataStagingRepository;
	
	
	@Inject
	DashBoardV4Service dashBoardV4Service;
	
	private int round=2;


	DecimalFormat dform = new DecimalFormat("####0.00");
	


	@PostMapping("/reconciliationAnalysisforGivenPeriodV3")
	@Timed
	public List<LinkedHashMap> reconciliationAnalysisforGivenPeriodV3(HttpServletRequest request,@RequestParam(value="processId",required=false) String processId,@RequestBody HashMap dates,@RequestParam String type,HttpServletResponse response,
			@RequestParam(value="ruleGroupId",required=false) Long  ruleGroupId,@RequestParam(value="viewId",required=false) Long  viewId,@RequestParam(value="viewType",required=false) String  viewType) throws SQLException, ParseException 
			{
		log.info("Rest Request to get aging analysis in service:"+dates);

		HashMap map0=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map0.get("tenantId").toString());
		
		
		//Processes process=processesRepository.findOne(processId);
		//	LinkedHashMap eachMap=new LinkedHashMap();
		List<LinkedHashMap> finallist=new ArrayList<LinkedHashMap>();

		Long recRuleGrpId=0l;

		if(processId!=null && ruleGroupId==null)

		{
			Processes processes = processesRepository.findByTenantIdAndIdForDisplay(tenantId, processId);

			ProcessDetails procesRecDet=processDetailsRepository.findByProcessIdAndTagType(processes.getId(), "reconciliationRuleGroup");
			if(procesRecDet!=null)
			recRuleGrpId=procesRecDet.getTypeId();
		}
		else if(ruleGroupId!=null && processId==null)
			recRuleGrpId=ruleGroupId;


		ZonedDateTime fmDate=ZonedDateTime.parse(dates.get("startDate").toString());

		ZonedDateTime toDate=ZonedDateTime.parse(dates.get("endDate").toString());


		LocalDate fDate=fmDate.toLocalDate();

		LocalDate tDate=toDate.toLocalDate();

		Long days=Duration.between(fDate.atStartOfDay(), tDate.atStartOfDay()).toDays()+1;
		log.info("days :::"+ days);
		LookUpCode lookUpCode=new LookUpCode();

		//if(type.equalsIgnoreCase("months"))
		if(days>=30 && type.equalsIgnoreCase("none"))
		{
			lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("DASHBOARD", "MONTHS", tenantId);
			response.addHeader("reconPeriodAnalysisType",lookUpCode.getMeaning() );
			List<LinkedHashMap> dateMapList=dashBoardV3Service.datesService(fDate.minusMonths(1), tDate.minusMonths(1));
			log.info("dateMapList :"+dateMapList.size());
			for(int i=dateMapList.size()-1;i>=0;i--)
			{
				//log.info("i: "+i);

				//log.info("date at "+i+" is :"+dateMapList.get(i).get("startDate").toString());
				LinkedHashMap monthMap=new LinkedHashMap();
				List<Object[]> recon1WSummary=new ArrayList<Object[]>();
				
				BigDecimal totalDvCount=BigDecimal.ZERO;
				BigDecimal totalDvAmt=BigDecimal.ZERO;
				if(viewId!=null && viewType !=null)
				{
				totalDvCount=appModuleSummaryRepository.fetchTotalDvcountForReconGroupByViewIdAndType(recRuleGrpId, LocalDate.parse(dateMapList.get(i).get("startDate").toString()), LocalDate.parse(dateMapList.get(i).get("endDate").toString()),viewId,viewType);
				totalDvAmt=appModuleSummaryRepository.fetchTotalDvAmtForReconGroupByViewIdAndType(recRuleGrpId,LocalDate.parse(dateMapList.get(i).get("startDate").toString()), LocalDate.parse(dateMapList.get(i).get("endDate").toString()),viewId,viewType);
				recon1WSummary=appModuleSummaryRepository.fetchReconciledAndUnReconciledAmountsAndCountsByViewIdAndType(recRuleGrpId, LocalDate.parse(dateMapList.get(i).get("startDate").toString()), LocalDate.parse(dateMapList.get(i).get("endDate").toString()),totalDvCount,totalDvAmt,viewId,viewType);

				}
				else
				{
					totalDvCount=appModuleSummaryRepository.fetchTotalDvcountForReconGroup(recRuleGrpId, LocalDate.parse(dateMapList.get(i).get("startDate").toString()), LocalDate.parse(dateMapList.get(i).get("endDate").toString()));
					totalDvAmt=appModuleSummaryRepository.fetchTotalDvAmtForReconGroup(recRuleGrpId, LocalDate.parse(dateMapList.get(i).get("startDate").toString()), LocalDate.parse(dateMapList.get(i).get("endDate").toString()));
					recon1WSummary=appModuleSummaryRepository.fetchReconciledAndUnReconciledAmountsAndCounts(recRuleGrpId, LocalDate.parse(dateMapList.get(i).get("startDate").toString()), LocalDate.parse(dateMapList.get(i).get("endDate").toString()),totalDvCount,totalDvAmt);

				}
				


				if(recon1WSummary!=null && !(recon1WSummary.isEmpty()))
				{
					

					monthMap.put("labelValue", dateMapList.get(i).get("month"));
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
					monthMap.put("dateRange", dateMapList.get(i));

					finallist.add(monthMap);


				}
				else{
					log.info("data doesnt exists");


					Double unReconAmt=0d;
					Double unReconCt=0d;
					//unReconAmt=Double.valueOf(recon1WSummary.get(0)[4].toString())-Double.valueOf(recon1WSummary.get(0)[5].toString());
					//unReconCt=Double.valueOf(recon1WSummary.get(0)[0].toString())-Double.valueOf(recon1WSummary.get(0)[1].toString());

					monthMap.put("labelValue", dateMapList.get(i).get("month"));
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
					monthMap.put("dateRange", dateMapList.get(i));

					finallist.add(monthMap);




				}
			}

		}


		/**Reconciliation weeks**/


		//if(type.equalsIgnoreCase("weeks"))
		if((days>7 && days<30) || type.equalsIgnoreCase("months"))
		{

			lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("DASHBOARD", "WEEKS", tenantId);
			response.addHeader("reconPeriodAnalysisType",lookUpCode.getMeaning() );

			List<LinkedHashMap> dateMapList=dashBoardV3Service.weeksOfAMonth(fDate,tDate);
			log.info("dateMapList :"+dateMapList);
			log.info("dateMapList :"+dateMapList.size());
			for(int i=0;i<dateMapList.size();i++)
			{

			//	log.info("date at "+i+" is :"+dateMapList.get(i).get("startDate").toString());

				int weekNum=i+1;
				LinkedHashMap monthMap=new LinkedHashMap();
				/*List<Object[]> recon1WSummary=appModuleSummaryRepository.fetchReconCountAndUnReconciledCountForWeek
							(procesRecDet.getTypeId(),LocalDate.parse(dateMapList.get(i).get("startDate").toString()),LocalDate.parse(dateMapList.get(i).get("endDate").toString()));*/
				List<Object[]> recon1WSummary=new ArrayList<Object[]>();
				
				
				
				BigDecimal totalDvCount=BigDecimal.ZERO;
				BigDecimal totalDvAmt=BigDecimal.ZERO;
				if(viewId!=null && viewType !=null)
				{
				totalDvCount=appModuleSummaryRepository.fetchTotalDvcountForReconGroupByViewIdAndType(recRuleGrpId, LocalDate.parse(dateMapList.get(i).get("startDate").toString()), LocalDate.parse(dateMapList.get(i).get("endDate").toString()),viewId,viewType);
				totalDvAmt=appModuleSummaryRepository.fetchTotalDvAmtForReconGroupByViewIdAndType(recRuleGrpId,LocalDate.parse(dateMapList.get(i).get("startDate").toString()), LocalDate.parse(dateMapList.get(i).get("endDate").toString()),viewId,viewType);
				recon1WSummary=appModuleSummaryRepository.fetchReconciledAndUnReconciledAmountsAndCountsByViewIdAndType(recRuleGrpId, LocalDate.parse(dateMapList.get(i).get("startDate").toString()), LocalDate.parse(dateMapList.get(i).get("endDate").toString()),totalDvCount,totalDvAmt,viewId,viewType);

				}
				else
				{
					totalDvCount=appModuleSummaryRepository.fetchTotalDvcountForReconGroup(recRuleGrpId, LocalDate.parse(dateMapList.get(i).get("startDate").toString()), LocalDate.parse(dateMapList.get(i).get("endDate").toString()));
					totalDvAmt=appModuleSummaryRepository.fetchTotalDvAmtForReconGroup(recRuleGrpId, LocalDate.parse(dateMapList.get(i).get("startDate").toString()), LocalDate.parse(dateMapList.get(i).get("endDate").toString()));
					recon1WSummary=appModuleSummaryRepository.fetchReconciledAndUnReconciledAmountsAndCounts(recRuleGrpId, LocalDate.parse(dateMapList.get(i).get("startDate").toString()), LocalDate.parse(dateMapList.get(i).get("endDate").toString()),totalDvCount,totalDvAmt);

				}
				


				/*if(viewId!=null)
					recon1WSummary=appModuleSummaryRepository.fetchReconCountAndUnReconciledCountBetweenGivenDatesForARuleGroupAndViewId(recRuleGrpId, LocalDate.parse(dateMapList.get(i).get("startDate").toString()), LocalDate.parse(dateMapList.get(i).get("endDate").toString()),viewId);
				else
					recon1WSummary=appModuleSummaryRepository.fetchReconCountAndUnReconciledCountBetweenGivenDates(recRuleGrpId, LocalDate.parse(dateMapList.get(i).get("startDate").toString()), LocalDate.parse(dateMapList.get(i).get("endDate").toString()));
*/
				
				
				
				
				//log.info("recon1WSummary: "+recon1WSummary);
				if(recon1WSummary!=null && !(recon1WSummary.isEmpty()))
				{

					monthMap.put("labelValue","week-"+weekNum);
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
					monthMap.put("dateRange", dateMapList.get(i));

					finallist.add(monthMap);


				}
				else
				{

					/**setting Zeros if data doesn't exist**/

					log.info("if data doesn't exists");
					monthMap.put("labelValue","week-"+weekNum);
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
					unRecon.put("countPer", 0);
					monthMap.put("unRecon", unRecon);
					monthMap.put("dateRange", dateMapList.get(i));

					finallist.add(monthMap);



				}


			}

		}

		//if(type.equalsIgnoreCase("days"))
		if(days<=7)
		{

			lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("DASHBOARD", "DAYS", tenantId);
			response.addHeader("reconPeriodAnalysisType",lookUpCode.getMeaning() );

			while(tDate.plusDays(1).isAfter(fDate)){
				String[] month=dashBoardV2Service.dateSpecifiedFormat(tDate.toString());
				LinkedHashMap monthMap=new LinkedHashMap();
				List<Object[]> recon1WSummary=new ArrayList<Object[]>();
				
				
				
				
				BigDecimal totalDvCount=BigDecimal.ZERO;
				BigDecimal totalDvAmt=BigDecimal.ZERO;
				if(viewId!=null && viewType !=null)
				{
				totalDvCount=appModuleSummaryRepository.fetchTotalDvcountForReconGroupByViewIdAndType(recRuleGrpId, tDate, tDate,viewId,viewType);
				totalDvAmt=appModuleSummaryRepository.fetchTotalDvAmtForReconGroupByViewIdAndType(recRuleGrpId, tDate, tDate,viewId,viewType);
				recon1WSummary=appModuleSummaryRepository.fetchReconciledAndUnReconciledAmountsAndCountsByViewIdAndType(recRuleGrpId, fDate, tDate,totalDvCount,totalDvAmt,viewId,viewType);
				}
				else
				{
					totalDvCount=appModuleSummaryRepository.fetchTotalDvcountForReconGroup(recRuleGrpId, tDate, tDate);
					totalDvAmt=appModuleSummaryRepository.fetchTotalDvAmtForReconGroup(recRuleGrpId, tDate, tDate);
					recon1WSummary=appModuleSummaryRepository.fetchReconciledAndUnReconciledAmountsAndCounts(recRuleGrpId, tDate, tDate,totalDvCount,totalDvAmt);
				}
				
				

				
				/*if(viewId!=null)
					recon1WSummary=appModuleSummaryRepository.fetchReconCountAndUnReconciledCountDatewiseByRuleGroupIdAndViewId(recRuleGrpId,tDate,viewId);
				else
					recon1WSummary=appModuleSummaryRepository.fetchReconCountAndUnReconciledCountDatewise(recRuleGrpId,tDate);*/

				if(recon1WSummary!=null && !(recon1WSummary.isEmpty()))
				{

					//unReconAmt=Double.valueOf(recon1WSummary.get(0)[4].toString())-Double.valueOf(recon1WSummary.get(0)[5].toString());
					//unReconCt=Double.valueOf(recon1WSummary.get(0)[0].toString())-Double.valueOf(recon1WSummary.get(0)[1].toString());


					monthMap.put("labelValue",month[0]+" "+month[1]);
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
					LinkedHashMap datesMap=new LinkedHashMap();
					datesMap.put("startDate", tDate);
					datesMap.put("endDate", tDate);
					monthMap.put("dateRange",datesMap);

					finallist.add(monthMap);


				}
				else
				{

					Double unReconAmt=0d;
					Double unReconCt=0d;
					//unReconAmt=Double.valueOf(recon1WSummary.get(0)[4].toString())-Double.valueOf(recon1WSummary.get(0)[5].toString());
					//unReconCt=Double.valueOf(recon1WSummary.get(0)[0].toString())-Double.valueOf(recon1WSummary.get(0)[1].toString());


					monthMap.put("labelValue",month[0]+" "+month[1]);
					LinkedHashMap recon=new LinkedHashMap();
					recon.put("amount", 0);
					recon.put("amountPer",0);
					recon.put("count", 0);
					recon.put("countPer", 0);
					monthMap.put("recon", recon);
					LinkedHashMap unRecon=new LinkedHashMap();
					unRecon.put("amount", 0);
					unRecon.put("amountPer",0);
					unRecon.put("count", 0);
					unRecon.put("countPer",0);
					monthMap.put("unRecon",unRecon);
					LinkedHashMap datesMap=new LinkedHashMap();
					datesMap.put("startDate", tDate);
					datesMap.put("endDate", tDate);
					monthMap.put("dateRange",datesMap);

					finallist.add(monthMap);



				}
				tDate=tDate.minusDays(1);
			}

		}


		return finallist;

			}

	
	
	@PostMapping("/AccountingAnalysisforGivenPeriodV3")
	@Timed
	public List<LinkedHashMap> accountingAnalysisforGivenPeriodV3(HttpServletRequest request,@RequestParam(value="processId",required=false) String processId,@RequestBody HashMap dates,@RequestParam String type,@RequestParam(value="ruleGroupId",required=false) Long ruleGroupId,
			HttpServletResponse response,@RequestParam(value="viewId",required=false) Long viewId) throws SQLException, ParseException 
			{
		log.info("Rest Request to get aging analysis in service:"+dates);
		HashMap map0=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map0.get("tenantId").toString());
		
		

		//	LinkedHashMap eachMap=new LinkedHashMap();
		List<LinkedHashMap> finallist=new ArrayList<LinkedHashMap>();
		//Processes process=processesRepository.findOne(processId);
		Long recRuleGrpId=0l;

		if(processId!=null && ruleGroupId==null)

		{
			Processes processes = processesRepository.findByTenantIdAndIdForDisplay(tenantId, processId);
			ProcessDetails procesRecDet=processDetailsRepository.findByProcessIdAndTagType(processes.getId(), "accountingRuleGroup");
			if(procesRecDet!=null)
			recRuleGrpId=procesRecDet.getTypeId();
		}
		else if(ruleGroupId!=null && processId==null)
			recRuleGrpId=ruleGroupId;


		ZonedDateTime fmDate=ZonedDateTime.parse(dates.get("startDate").toString());

		ZonedDateTime toDate=ZonedDateTime.parse(dates.get("endDate").toString());


		LocalDate fDate=fmDate.toLocalDate();

		LocalDate tDate=toDate.toLocalDate();
		Long days=Duration.between(fDate.atStartOfDay(), tDate.atStartOfDay()).toDays()+1;

		LookUpCode lookUpCode=new LookUpCode();


		//if(type.equalsIgnoreCase("months"))
		if(days>=30 && type.equalsIgnoreCase("none"))
		{
			lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("DASHBOARD", "MONTHS", tenantId);
			response.addHeader("accountingPeriodAnalysisType",lookUpCode.getMeaning() );
			List<LinkedHashMap> dateMapList=dashBoardV3Service.datesService(fDate.minusMonths(1), tDate.minusMonths(1));
			log.info("dateMapList :"+dateMapList.size());

			for(int d=dateMapList.size()-1;d>=0;d--)
			{

				//log.info("date at "+d+" is :"+dateMapList.get(d).get("startDate").toString());
				LinkedHashMap monthMap=new LinkedHashMap();

				monthMap=dashBoardV4Service.accountingRuleGroupSpecificInformationV4Service(dateMapList.get(d).get("startDate").toString(),dateMapList.get(d).get("endDate").toString(), request, recRuleGrpId,viewId);
				if(monthMap.isEmpty())
				{
					LinkedHashMap acctMap=new LinkedHashMap();
					acctMap.put("amount", 0d);
					acctMap.put("count", 0d);
					acctMap.put("amountPer",  0d);
					acctMap.put("countPer", 0d);
					monthMap.put("accounted", acctMap);

					LinkedHashMap finalActMap=new LinkedHashMap();
					finalActMap.put("amount", 0d);
					finalActMap.put("count",0d);
					finalActMap.put("amountPer",  0d);
					finalActMap.put("countPer", 0d);
					monthMap.put("finalAccounted", finalActMap);


					LinkedHashMap notActMap=new LinkedHashMap();
					notActMap.put("amount", 0d);
					notActMap.put("count", 0d);
					notActMap.put("amountPer", 0d);
					notActMap.put("countPer", 0d);
					monthMap.put("notAccounted", notActMap);
				}
				monthMap.put("labelValue", dateMapList.get(d).get("month"));
				monthMap.put("dateRange", dateMapList.get(d));
				finallist.add(monthMap);
			}

		}


		/**accounting weeks**/


		//if(type.equalsIgnoreCase("weeks"))
		if((days>7 && days<30) || type.equalsIgnoreCase("months"))
		{

			lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("DASHBOARD", "WEEKS", tenantId);
			response.addHeader("accountingPeriodAnalysisType",lookUpCode.getMeaning() );


			List<LinkedHashMap> dateMapList=dashBoardV3Service.weeksOfAMonth(fDate,tDate);

			log.info("dateMapList :"+dateMapList.size());
			for(int d=0;d<dateMapList.size();d++)
			{

				//log.info("date at "+d+" is :"+dateMapList.get(d).get("startDate").toString());

				LinkedHashMap monthMap=new LinkedHashMap();
				monthMap=dashBoardV4Service.accountingRuleGroupSpecificInformationV4Service(dateMapList.get(d).get("startDate").toString(),dateMapList.get(d).get("endDate").toString(), request, recRuleGrpId,viewId);
				if(monthMap.isEmpty())
				{
					LinkedHashMap acctMap=new LinkedHashMap();
					acctMap.put("amount", 0d);
					acctMap.put("count", 0d);
					acctMap.put("amountPer",  0d);
					acctMap.put("countPer", 0d);
					monthMap.put("accounted", acctMap);

					LinkedHashMap finalActMap=new LinkedHashMap();
					finalActMap.put("amount", 0d);
					finalActMap.put("count",0d);
					finalActMap.put("amountPer",  0d);
					finalActMap.put("countPer", 0d);
					monthMap.put("finalAccounted", finalActMap);


					LinkedHashMap notActMap=new LinkedHashMap();
					notActMap.put("amount", 0d);
					notActMap.put("count", 0d);
					notActMap.put("amountPer", 0d);
					notActMap.put("countPer", 0d);
					monthMap.put("notAccounted", notActMap);
				}
				int weekNum=d+1;
				monthMap.put("labelValue","week-"+weekNum);
				monthMap.put("dateRange", dateMapList.get(d));
				finallist.add(monthMap);


			}

		}

		//if(type.equalsIgnoreCase("days"))
		if(days<=7)
		{

			lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("DASHBOARD", "DAYS", tenantId);
			response.addHeader("accountingPeriodAnalysisType",lookUpCode.getMeaning() );


			while(tDate.plusDays(1).isAfter(fDate)){

				LinkedHashMap monthMap=new LinkedHashMap();
				Double notAccountedCt=0d;
				Double notAccountedCtPer=0d;
				Double notAccountedAmt=0d;
				Double notAccountedAmtPer=0d;
				List<LookUpCode> lookUpcodes=lookUpCodeRepository.findByTenantIdAndLookUpTypeAndModule(tenantId, "ACCOUNTING_STATUS", "REPORTING");
				for(LookUpCode actLC:lookUpcodes)
				{
					List<Object[]> acct1WSummary=new ArrayList<Object[]>();
					
					if(viewId!=null)
					{
						acct1WSummary=appModuleSummaryRepository.fetchAccountingInfoForADateAndTypeAndViewId
						(recRuleGrpId,tDate,actLC.getLookUpCode(),viewId);
					}
					else
					{
						acct1WSummary=appModuleSummaryRepository.fetchAccountingInfoForADateAndType
						(recRuleGrpId,tDate,actLC.getLookUpCode());
					}
				
						

					//log.info("acct1WSummary :"+acct1WSummary.size());

					if(acct1WSummary!=null && acct1WSummary.size()>0)
					{



						if(actLC.getLookUpCode().equalsIgnoreCase("Accounting inprocess") || actLC.getLookUpCode().equalsIgnoreCase("IN_PROCESS"))
						{

							notAccountedCt=notAccountedCt+Double.valueOf(acct1WSummary.get(0)[2].toString());
							notAccountedAmt=notAccountedAmt+Double.valueOf(acct1WSummary.get(0)[4].toString());
							notAccountedCtPer=notAccountedCtPer+Double.valueOf(acct1WSummary.get(0)[5].toString());
							notAccountedAmtPer=notAccountedAmtPer+Double.valueOf(acct1WSummary.get(0)[6].toString());

						}	
						else if(actLC.getLookUpCode().equalsIgnoreCase("Not accounted") || actLC.getLookUpCode().equalsIgnoreCase("UN_ACCOUNTED"))
						{

							notAccountedCt=notAccountedCt+Double.valueOf(acct1WSummary.get(0)[2].toString());
							notAccountedAmt=notAccountedAmt+Double.valueOf(acct1WSummary.get(0)[4].toString());
							notAccountedCtPer=notAccountedCtPer+Double.valueOf(acct1WSummary.get(0)[5].toString());
							notAccountedAmtPer=notAccountedAmtPer+Double.valueOf(acct1WSummary.get(0)[6].toString());

						}
						else
						{
							LinkedHashMap acctMap=new LinkedHashMap();
							acctMap.put("amount", Double.valueOf(acct1WSummary.get(0)[4].toString()));
							acctMap.put("count", Double.valueOf(acct1WSummary.get(0)[2].toString()));
							acctMap.put("amountPer",  Double.valueOf(acct1WSummary.get(0)[6].toString()));
							acctMap.put("countPer", Double.valueOf(acct1WSummary.get(0)[5].toString()));

							if(actLC.getLookUpCode().equalsIgnoreCase("ACCOUNTED"))
								monthMap.put("accounted", acctMap);
							if(actLC.getLookUpCode().equalsIgnoreCase("JOURNALS_ENTERED"))
								monthMap.put("finalAccounted", acctMap);

						}


					}
					else
					{
						LinkedHashMap acctMap=new LinkedHashMap();
						acctMap.put("amount", 0d);
						acctMap.put("count", 0d);
						acctMap.put("amountPer",  0d);
						acctMap.put("countPer", 0d);
						if(actLC.getLookUpCode().equalsIgnoreCase("ACCOUNTED"))
							monthMap.put("accounted", acctMap);
						if(actLC.getLookUpCode().equalsIgnoreCase("JOURNALS_ENTERED"))
							monthMap.put("finalAccounted", acctMap);

					}


				}

				LinkedHashMap notActMap=new LinkedHashMap();
				notActMap.put("amount", Double.valueOf(dform.format(notAccountedAmt)));
				notActMap.put("count", Double.valueOf(dform.format(notAccountedCt)));
				notActMap.put("amountPer",  Double.valueOf(dform.format(notAccountedAmtPer)));
				notActMap.put("countPer", Double.valueOf(dform.format(notAccountedCtPer)));
				monthMap.put("notAccounted", notActMap);


				if(monthMap.isEmpty())
				{
					LinkedHashMap acctMap=new LinkedHashMap();
					acctMap.put("amount", 0d);
					acctMap.put("count", 0d);
					acctMap.put("amountPer",  0d);
					acctMap.put("countPer", 0d);
					monthMap.put("accounted", acctMap);

					LinkedHashMap finalActMap=new LinkedHashMap();
					finalActMap.put("amount", 0d);
					finalActMap.put("count",0d);
					finalActMap.put("amountPer",  0d);
					finalActMap.put("countPer", 0d);
					monthMap.put("finalAccounted", finalActMap);


					LinkedHashMap notActtMap=new LinkedHashMap();
					notActtMap.put("amount", 0d);
					notActtMap.put("count", 0d);
					notActtMap.put("amountPer", 0d);
					notActtMap.put("countPer", 0d);
					monthMap.put("notAccounted", notActMap);
				}

				String[] month=dashBoardV2Service.dateSpecifiedFormat(tDate.toString());
				monthMap.put("labelValue",month[0]+" "+month[1]);
				LinkedHashMap datesMap=new LinkedHashMap();
				datesMap.put("startDate", tDate);
				datesMap.put("endDate", tDate);
				monthMap.put("dateRange",datesMap);
				finallist.add(monthMap);
				tDate=tDate.minusDays(1);
			}

		}


		return finallist;

			}
	
	/**query need to be changed**/
/*	@PostMapping("/getSummaryInfoForReconciliationV3")
	@Timed 
	public LinkedHashMap getSummaryInfoForReconciliationV3(@RequestParam Long processId ,@RequestBody HashMap dates,@RequestParam int violation) throws SQLException, ParseException
	{
		log.info("Rest request to getSummaryInfoForReconciliationV2 for a process:"+processId);
		LinkedHashMap finalMap=new LinkedHashMap();
		List<LinkedHashMap> dataMap=new ArrayList<LinkedHashMap>();
		Processes processes=processesRepository.findOne(processId);
		ProcessDetails procesDet=processDetailsRepository.findByProcessIdAndTagType(processId, "reconciliationRuleGroup");
		if(procesDet!=null)
		{
			ZonedDateTime fmDate=ZonedDateTime.parse(dates.get("startDate").toString());
			ZonedDateTime toDate=ZonedDateTime.parse(dates.get("endDate").toString());
		
			java.time.LocalDate fDate=fmDate.toLocalDate();
			java.time.LocalDate tDate=toDate.toLocalDate();
			List<String> rulesList=new ArrayList<String>();
			List<Long> rulesIdList=new ArrayList<Long>();
			List<Object[]> reconTotalDvAmountAndCount=appModuleSummaryRepository.fetchTotalDvCountAndAmount(procesDet.getTypeId(),fDate,tDate);	
			Double totaldvCount= 0d;
			Double totaldvAmt=0d;
			if(reconTotalDvAmountAndCount.size()>0)
			{
				totaldvCount= Double.valueOf(reconTotalDvAmountAndCount.get(0)[0].toString());
				totaldvAmt= Double.valueOf(reconTotalDvAmountAndCount.get(0)[1].toString());
			}
			
			List<Object[]> reconTotalReconciledAmountAndCount=appModuleSummaryRepository.fetchTotalReconciledCountAndAmount(procesDet.getTypeId(),fDate,tDate);	
			
			Double totalReconciledDvCount= Double.valueOf(reconTotalReconciledAmountAndCount.get(0)[0].toString());
			Double totalReconciledDvAmt= Double.valueOf(reconTotalReconciledAmountAndCount.get(0)[1].toString());
		//	log.info("totalReconciledDvCount :"+totalReconciledDvCount);
		//	log.info("totalReconciledDvAmt :"+totalReconciledDvAmt);
			
			Double totalUnReconciledDvCount= totaldvCount-totalReconciledDvCount;
			Double totalUnReconciledDvAmt= totaldvAmt-totalReconciledDvAmt;
		//	log.info("totalUnReconciledDvCount :"+totalUnReconciledDvCount);
		//	log.info("totalUnReconciledDvAmt :"+totalUnReconciledDvAmt);
			
			Double totalUnReconciledCountPer=0d;
			if(totaldvCount>0)
				totalUnReconciledCountPer=(totalUnReconciledDvCount/totaldvCount)*100;
			List<Object[]> reconSummary=appModuleSummaryRepository.fetchRecCountsByGroupIdAndFileDate(procesDet.getTypeId(),fDate,tDate);
			Double totalUnReconAmt=0d;
			Double totalReconAmt=0d;
			Double totalUnApprovedCt=0d;
			Long violationCount=0l;
			Long totalViolationCount=0l;
			Double unApprovedCount=0d;
			Double approvedCount=0d;
			Double totalDVCount=0d;
			Double totalDVAmt=0d;
			for(int i=0;i<reconSummary.size();i++)
			{

				totalDVCount=totalDVCount+Double.valueOf(reconSummary.get(i)[0].toString());
				LinkedHashMap map=new LinkedHashMap();
				log.info("**Double.valueOf(reconSummary.get(i)[10].toString()*****at ***"+i+"is "+Double.valueOf(reconSummary.get(i)[10].toString()));
				totalDVAmt=totalDVAmt+Double.valueOf(reconSummary.get(i)[10].toString());
				DataViews dv=dataViewsRepository.findOne(Long.valueOf(reconSummary.get(i)[5].toString()));
				map.put("labelValue", dv.getDataViewDispName());
				map.put("viewId", reconSummary.get(i)[5]);
				LinkedHashMap reconMap=new LinkedHashMap();
				reconMap.put("amount", Double.valueOf( reconSummary.get(i)[8].toString()));
				reconMap.put("count", Double.valueOf( reconSummary.get(i)[1].toString()));
				Double amountPer= 0d;
				if(totalReconciledDvAmt>0)
				amountPer=	(Double.valueOf( reconSummary.get(i)[8].toString())/totalReconciledDvAmt)*100d;
				reconMap.put("amountPer",  Double.valueOf(dform.format(amountPer)));
				Double countPer= 0d;
				if(totalReconciledDvCount>0)
				countPer=(Double.valueOf( reconSummary.get(i)[1].toString())/totalReconciledDvCount)*100d;
				reconMap.put("countPer", Double.valueOf(dform.format(countPer)));
				map.put("recon", reconMap);

				LinkedHashMap unReconMap=new LinkedHashMap();
				unReconMap.put("amount", Double.valueOf(reconSummary.get(i)[6].toString()));
				unReconMap.put("count", Double.valueOf( reconSummary.get(i)[2].toString()));
				Double unAmountPer= (Double.valueOf( reconSummary.get(i)[6].toString())/totalUnReconciledDvAmt)*100;
				unReconMap.put("amountPer",  Double.valueOf(dform.format(unAmountPer)));
				Double unCountPer= (Double.valueOf( reconSummary.get(i)[2].toString())/totalUnReconciledDvCount)*100;
				unReconMap.put("countPer", Double.valueOf(dform.format(unCountPer)));
				map.put("unRecon", unReconMap);

				map.put("DvCount", reconSummary.get(i)[0]);
				totalDVCount=totalDVCount+Double.valueOf(reconSummary.get(i)[0].toString());
				map.put("ReconciledCount", reconSummary.get(i)[1]);
				map.put("unReconciledCount", reconSummary.get(i)[2]);
				map.put("reconciledPer", reconSummary.get(i)[3]);
				map.put("unReconciledPer", reconSummary.get(i)[4].toString());
				map.put("reconciledAmt", reconSummary.get(i)[8].toString());
				map.put("unReconciledAmt", reconSummary.get(i)[6].toString());



				map.put("reconciledAmtPer", Double.valueOf(dform.format(reconciledAmtPer)));
				map.put("unReconciledAmtPer",Double.valueOf(dform.format(unReconciledAmtPer)));


				totalReconAmt=totalReconAmt+Double.valueOf(reconSummary.get(i)[8].toString());
				totalUnReconAmt=totalUnReconAmt+Double.valueOf(reconSummary.get(i)[6].toString());

				approvedCount=approvedCount+Double.valueOf(reconSummary.get(i)[9].toString());
				map.put("unApprovedCount", Double.valueOf(reconSummary.get(i)[7].toString()));
				totalUnApprovedCt=totalUnApprovedCt+Double.valueOf(reconSummary.get(i)[7].toString());


				List<BigInteger> finalSrcIdList=new ArrayList<BigInteger>();
				List<BigInteger> reconciliedSrcIds=reconciliationResultRepository.fetchReconciledSourceIds(processes.getTenantId(), procesDet.getTypeId(), dv.getId());	 
				finalSrcIdList.addAll(reconciliedSrcIds);
				List<BigInteger> reconciliedTrgIds=reconciliationResultRepository.fetchReconciledTargetIds(processes.getTenantId(), procesDet.getTypeId(), dv.getId());	 
				finalSrcIdList.addAll(reconciliedTrgIds);
				
				
				

				List<BigInteger> finalApprovedSrcIds=new ArrayList<BigInteger>();
				List<BigInteger> approvedSrcIds=reconciliationResultRepository.fetchApprovedSourceIds(processes.getTenantId(), procesDet.getTypeId(), dv.getId());	
				log.info("reconciliedSrcIds.size :"+approvedSrcIds.size());
				finalApprovedSrcIds.addAll(approvedSrcIds);
				List<BigInteger> approvedTrgIds=reconciliationResultRepository.fetchApprovedTargetIds(processes.getTenantId(), procesDet.getTypeId(), dv.getId());	
				log.info("reconciliedTrgIds.size :"+approvedTrgIds.size());
				finalApprovedSrcIds.addAll(approvedTrgIds);
				List<LinkedHashMap>  dvRuleDetailList=new ArrayList<LinkedHashMap>();
				log.info("ViewId :"+reconSummary.get(i)[5]);
				List<Object[]> ruleDetailsForView=appModuleSummaryRepository.findRuleIdAndTypeIdByRuleGroupIdAndViewId(procesDet.getTypeId(),Long.valueOf(reconSummary.get(i)[5].toString()),fDate,tDate);
				for(int r=0;r<ruleDetailsForView.size();r++)
				{
					log.info("ruleDetailsForView.get(r)[0] :"+ruleDetailsForView.get(r)[0]);
					LinkedHashMap dvRuleDetail=new LinkedHashMap();
					dvRuleDetail.put("ruleId", ruleDetailsForView.get(r)[0]);
					dvRuleDetail.put("type", ruleDetailsForView.get(r)[1]);
					dvRuleDetailList.add(dvRuleDetail);
				}
				map.put("dvRuleDetails", dvRuleDetailList);
				String dbUrl=env.getProperty("spring.datasource.url");
				String[] parts=dbUrl.split("[\\s@&?$+-]+");
				String host = parts[0].split("/")[2].split(":")[0];
				log.info("host :"+host);
				String schemaName=parts[0].split("/")[3];
				log.info("schemaName :"+schemaName);
				String userName = env.getProperty("spring.datasource.username");
				String password = env.getProperty("spring.datasource.password");
				String jdbcDriver = env.getProperty("spring.datasource.jdbcdriver");

				Connection conn = null;
				Statement stmtDv = null;
				Statement stmtApprovedAmt = null;

				DataViewsColumns dvColumn=dataViewsColumnsRepository.findByDataViewIdAndQualifier(dv.getId(), "AMOUNT");
				String ammountQualifier="";
				if(dvColumn.getRefDvType()!=null &&dvColumn.getRefDvType().equalsIgnoreCase("File Template"))
				{
					FileTemplateLines ftl=fileTemplateLinesRepository.findOne(Long.valueOf(dvColumn.getRefDvColumn()));
					ammountQualifier=ftl.getColumnAlias();
				}
				else
					ammountQualifier=dvColumn.getColumnName();

				conn = DriverManager.getConnection(dbUrl, userName, password);
				log.info("Connected database successfully...");
				stmtDv = conn.createStatement();
				stmtApprovedAmt = conn.createStatement();

				ResultSet resultDv=null;
				ResultSet resultTotalViolationQuery=null;
				ResultSet resultApprovedAmt=null;
				String query="";

				if(finalSrcIdList!=null && !finalSrcIdList.isEmpty())
				{
					String finalSrcIds=finalSrcIdList.toString().replaceAll("\\[", "").replaceAll("\\]", "");

					query="select DATEDIFF( SYSDATE(), `v`.`fileDate`) as `rule_age`,count(scrIds),sum(`"+ammountQualifier+"`) as `"+ammountQualifier+"` from "+schemaName+"."+dv.getDataViewName().toLowerCase().toLowerCase()+" v where fileDate between '"+fDate+"' and '"+tDate+"' "
							+ " and scrIds not in ("+finalSrcIds+")group by rule_age";
					//log.info("query in if  :"+query);

				}
				else
				{
					query="select DATEDIFF( SYSDATE(), `v`.`fileDate`) as `rule_age`,count(scrIds),sum(`"+ammountQualifier+"`) as `"+ammountQualifier+"` from "+schemaName+"."+dv.getDataViewName().toLowerCase().toLowerCase()+" v where fileDate between '"+fDate+"' and '"+tDate+"' "
							+ "group by rule_age";
					//log.info("query in else  :"+query);
				}
				
				
				String	TotalcountQuery="select count(scrIds) from "+schemaName+".`"+dv.getDataViewName().toLowerCase().toLowerCase()+"` v where fileDate between '"+fDate+"' and '"+tDate+"' ";
					//log.info("query in TotalcountQuery  :"+TotalcountQuery);
				
				//log.info("query :"+query);
					resultTotalViolationQuery=stmtDv.executeQuery(TotalcountQuery);
					while(resultTotalViolationQuery.next())
					{
						totalViolationCount=totalViolationCount+Long.valueOf(resultTotalViolationQuery.getString("count(scrIds)").toString());
					}
					log.info("totalViolationCount :"+totalViolationCount);
				resultDv=stmtDv.executeQuery(query);
				ResultSetMetaData rsmd2 = resultDv.getMetaData();
				int columnCount = rsmd2.getColumnCount();
				map.put("violationAmount",0);
				map.put("violationCount", 0);
				log.info("violation :"+violation);
				while(resultDv.next())
				{
					log.info("resultDv.getString(rule_age) :"+resultDv.getString("rule_age"));
					int ruleAge=Integer.valueOf(resultDv.getString("rule_age").toString());

					// int ruleAge=1;
					
					log.info("ruleAge :"+ruleAge+" and violation :"+violation);
					if(ruleAge>=violation)
					{
						log.info("if rule age is greater than violation");
						map.put("violationCount", Long.valueOf(resultDv.getString("count(scrIds)").toString()));
						violationCount=violationCount+Long.valueOf(resultDv.getString("count(scrIds)").toString());
						map.put("violationAmount",dform.format(Double.valueOf(resultDv.getString(ammountQualifier).toString())));
					}
					else
					{
						map.put("violationAmount",0);
						map.put("violationCount", 0);
					}
				}

				if(finalApprovedSrcIds!=null && !finalApprovedSrcIds.isEmpty())
				{
					String finalSrcIds=finalSrcIdList.toString().replaceAll("\\[", "").replaceAll("\\]", "");

					query="select sum(`"+ammountQualifier+"`) as `"+ammountQualifier+"` from "+schemaName+"."+dv.getDataViewName().toLowerCase().toLowerCase()+" v where fileDate between '"+fDate+"' and '"+tDate+"' "
							+ " and scrIds not in ("+finalSrcIds+")";

				}
				else
				{
					query="select sum(`"+ammountQualifier+"`) as `"+ammountQualifier+"` from "+schemaName+"."+dv.getDataViewName().toLowerCase().toLowerCase()+" v where fileDate between '"+fDate+"' and '"+tDate+"'";


				}
				//log.info("query for unapproved amt :"+query);
				resultApprovedAmt=stmtApprovedAmt.executeQuery(query);


				while(resultApprovedAmt.next())
				{
					if(resultApprovedAmt.getString(ammountQualifier)!=null)
						map.put("unApprovedAmount",dform.format(Double.valueOf(resultApprovedAmt.getString(ammountQualifier).toString())));
					else
						map.put("unApprovedAmount",0);
				}
				dataMap.add(map);
				if(stmtDv!=null)
					stmtDv.close();
				if(stmtApprovedAmt!=null)
					stmtApprovedAmt.close();
				if(resultDv!=null)
					resultDv.close();
				if(resultApprovedAmt!=null)
					resultApprovedAmt.close();
			}
		//	log.info("totalUnReconAmt :"+totalUnReconAmt);
		//	log.info("totalDVAmt :"+totalDVAmt);
			Double unReconItemsValuePer=0d;
			Double unApprovedCtPer=0d;
			double unReconItemsViolationPer=0l;
			Double totalReconPer=0d;
			if(totalReconAmt>0)
			{
				totalReconPer=(totalReconAmt/totalDVAmt)*100;
				unReconItemsValuePer=100-totalReconPer;
			//	unReconItemsValuePer=(totalUnReconAmt/totalDVAmt)*100;
				
			}
			if(totalDVCount>0)
			{
				log.info("totalDVCount :"+totalDVCount);
				log.info("totalUnApprovedCt :"+totalUnApprovedCt);
				unApprovedCtPer=(totalUnApprovedCt/totalDVCount)*100;
			}
			if(totalViolationCount>0)
			{
				//log.info("violationCount :"+violationCount);
				//log.info("totalViolationCount :"+totalViolationCount);
				unReconItemsViolationPer=((double)violationCount/totalViolationCount)*100;
			}
		//	log.info("rulesList :"+rulesList);
			finalMap.put("rulesList", rulesList);
			finalMap.put("rulesIdList", rulesIdList);
			finalMap.put("unReconItemsValue", totalUnReconAmt);
			finalMap.put("unReconItemsValuePer", Double.valueOf(dform.format(unReconItemsValuePer)));
			finalMap.put("unReconItemsViolation", violationCount);
			finalMap.put("unReconItemsViolationPer", Double.valueOf(dform.format(unReconItemsViolationPer)));
			finalMap.put("awaitingAppCount", totalUnApprovedCt);
			finalMap.put("awaitingAppCountPer",  Double.valueOf(dform.format(unApprovedCtPer)));
			finalMap.put("unReconItemsByCount", totalUnReconciledDvCount);
			finalMap.put("unReconItemsByCountPer", Double.valueOf(dform.format( totalUnReconciledCountPer)));
			finalMap.put("reconciliationData", dataMap);


		}

		log.info("******end Time : "+ZonedDateTime.now()+"*******");
		return finalMap;
	}*/
	
	
	
	
	@PostMapping("/getSummaryInfoForReconciliationV3")
	@Timed 
	public LinkedHashMap getSummaryInfoForReconciliationV3(HttpServletRequest request,@RequestParam(value="processId",required=false) String processId ,@RequestBody HashMap dates,@RequestParam int violation,
			@RequestParam(value="ruleGroupId",required=false) Long ruleGroupId)
	{
		log.info("Rest request to getSummaryInfoForReconciliationV2 for a process:"+processId);
		Boolean checkConnection = false;
		HashMap tenMap=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(tenMap.get("tenantId").toString());
		Processes processes = processesRepository.findByTenantIdAndIdForDisplay(tenantId, processId);
		LinkedHashMap finalMap=new LinkedHashMap();
		List<LinkedHashMap> dataMap=new ArrayList<LinkedHashMap>();
		Long recRuleGrpId=0l;

		if(processId!=null && ruleGroupId==null)

		{

			ProcessDetails procesRecDet=processDetailsRepository.findByProcessIdAndTagType(processes.getId(), "reconciliationRuleGroup");
			if(procesRecDet!=null)
			recRuleGrpId=procesRecDet.getTypeId();
		}
		else if(ruleGroupId!=null && processId==null)
			recRuleGrpId=ruleGroupId;
		log.info("recRuleGrpId :"+recRuleGrpId);
		ZonedDateTime fmDate=ZonedDateTime.parse(dates.get("startDate").toString());
		ZonedDateTime toDate=ZonedDateTime.parse(dates.get("endDate").toString());

		java.time.LocalDate fDate=fmDate.toLocalDate();
		java.time.LocalDate tDate=toDate.toLocalDate();
		List<String> rulesList=new ArrayList<String>();
		List<Long> rulesIdList=new ArrayList<Long>();
		List<Object[]> reconTotalDvAmountAndCount=appModuleSummaryRepository.fetchTotalDvCountAndAmount(recRuleGrpId,fDate,tDate);	
		Double totaldvCount= 0d;
		Double totaldvAmt=0d;
		if(reconTotalDvAmountAndCount.size()>0)
		{
			totaldvCount= Double.valueOf(reconTotalDvAmountAndCount.get(0)[0].toString());
			totaldvAmt= Double.valueOf(reconTotalDvAmountAndCount.get(0)[1].toString());
		}

		List<Object[]> reconTotalReconciledAmountAndCount=appModuleSummaryRepository.fetchTotalReconciledCountAndAmount(recRuleGrpId,fDate,tDate);	

		Double totalReconciledDvCount= Double.valueOf(reconTotalReconciledAmountAndCount.get(0)[0].toString());
		Double totalReconciledDvAmt= Double.valueOf(reconTotalReconciledAmountAndCount.get(0)[1].toString());
		//	log.info("totalReconciledDvCount :"+totalReconciledDvCount);
		//	log.info("totalReconciledDvAmt :"+totalReconciledDvAmt);

		Double totalUnReconciledDvCount= totaldvCount-totalReconciledDvCount;
		Double totalUnReconciledDvAmt= totaldvAmt-totalReconciledDvAmt;
		//	log.info("totalUnReconciledDvCount :"+totalUnReconciledDvCount);
		//	log.info("totalUnReconciledDvAmt :"+totalUnReconciledDvAmt);

		Double totalUnReconciledCountPer=0d;
		if(totaldvCount>0)
			totalUnReconciledCountPer=(totalUnReconciledDvCount/totaldvCount)*100;
		List<Object[]> reconSummary=appModuleSummaryRepository.fetchRecCountsByGroupIdAndFileDate(recRuleGrpId,fDate,tDate);
		Double totalUnReconAmt=0d;
		Double totalReconAmt=0d;
		Double totalUnApprovedCt=0d;
		Long violationCount=0l;
		Long totalViolationCount=0l;
		Double unApprovedCount=0d;
		Double approvedCount=0d;
		Double totalDVCount=0d;
		Double totalDVAmt=0d;
		Double totalUnRecCount=0d;
		for(int i=0;i<reconSummary.size();i++)
		{

			totalDVCount=totalDVCount+Double.valueOf(reconSummary.get(i)[0].toString());
			LinkedHashMap map=new LinkedHashMap();
			log.info("**Double.valueOf(reconSummary.get(i)[10].toString()*****at ***"+i+"is "+Double.valueOf(reconSummary.get(i)[10].toString()));
			totalDVAmt=totalDVAmt+Double.valueOf(reconSummary.get(i)[10].toString());
			DataViews dv=dataViewsRepository.findOne(Long.valueOf(reconSummary.get(i)[5].toString()));
			
			
			
			
			map.put("labelValue", dv.getDataViewDispName());
			map.put("viewId", reconSummary.get(i)[5]);
			LinkedHashMap reconMap=new LinkedHashMap();
			reconMap.put("amount", Double.valueOf( reconSummary.get(i)[8].toString()));
			reconMap.put("count", Double.valueOf( reconSummary.get(i)[1].toString()));
			Double amountPer= 0d;
			if(totalReconciledDvAmt>0)
				amountPer=	(Double.valueOf( reconSummary.get(i)[8].toString())/totalReconciledDvAmt)*100d;
			reconMap.put("amountPer",  Double.valueOf(dform.format(amountPer)));
			Double countPer= 0d;
			if(totalReconciledDvCount>0)
				countPer=(Double.valueOf( reconSummary.get(i)[1].toString())/totalReconciledDvCount)*100d;
			reconMap.put("countPer", Double.valueOf(dform.format(countPer)));
			map.put("recon", reconMap);

			LinkedHashMap unReconMap=new LinkedHashMap();
			unReconMap.put("amount", Double.valueOf(reconSummary.get(i)[6].toString()));
			unReconMap.put("count", Double.valueOf( reconSummary.get(i)[2].toString()));
			Double unAmountPer= (Double.valueOf( reconSummary.get(i)[6].toString())/totalUnReconciledDvAmt)*100;
			unReconMap.put("amountPer",  Double.valueOf(dform.format(unAmountPer)));
			Double unCountPer= (Double.valueOf( reconSummary.get(i)[2].toString())/totalUnReconciledDvCount)*100;
			unReconMap.put("countPer", Double.valueOf(dform.format(unCountPer)));
			map.put("unRecon", unReconMap);

			/*map.put("DvCount", reconSummary.get(i)[0]);
				totalDVCount=totalDVCount+Double.valueOf(reconSummary.get(i)[0].toString());
				map.put("ReconciledCount", reconSummary.get(i)[1]);
				map.put("unReconciledCount", reconSummary.get(i)[2]);
				map.put("reconciledPer", reconSummary.get(i)[3]);
				map.put("unReconciledPer", reconSummary.get(i)[4].toString());
				map.put("reconciledAmt", reconSummary.get(i)[8].toString());
				map.put("unReconciledAmt", reconSummary.get(i)[6].toString());



				map.put("reconciledAmtPer", Double.valueOf(dform.format(reconciledAmtPer)));
				map.put("unReconciledAmtPer",Double.valueOf(dform.format(unReconciledAmtPer)));*/


			totalReconAmt=totalReconAmt+Double.valueOf(reconSummary.get(i)[8].toString());
			totalUnReconAmt=totalUnReconAmt+Double.valueOf(reconSummary.get(i)[6].toString());

			approvedCount=approvedCount+Double.valueOf(reconSummary.get(i)[9].toString());
			map.put("unApprovedCount", Double.valueOf(reconSummary.get(i)[7].toString()));
			totalUnApprovedCt=totalUnApprovedCt+Double.valueOf(reconSummary.get(i)[7].toString());


			List<BigInteger> finalSrcIdList=new ArrayList<BigInteger>();
			List<BigInteger> reconciliedSrcIds=reconciliationResultRepository.fetchReconciledSourceIds(tenantId, recRuleGrpId, dv.getId());	 
			finalSrcIdList.addAll(reconciliedSrcIds);
			List<BigInteger> reconciliedTrgIds=reconciliationResultRepository.fetchReconciledTargetIds(tenantId, recRuleGrpId, dv.getId());	 
			finalSrcIdList.addAll(reconciliedTrgIds);




			List<BigInteger> finalApprovedSrcIds=new ArrayList<BigInteger>();
			List<BigInteger> approvedSrcIds=reconciliationResultRepository.fetchApprovedSourceIds(tenantId, recRuleGrpId, dv.getId());	
			log.info("reconciliedSrcIds.size :"+approvedSrcIds.size());
			finalApprovedSrcIds.addAll(approvedSrcIds);
			List<BigInteger> approvedTrgIds=reconciliationResultRepository.fetchApprovedTargetIds(tenantId, recRuleGrpId, dv.getId());	
			log.info("reconciliedTrgIds.size :"+approvedTrgIds.size());
			finalApprovedSrcIds.addAll(approvedTrgIds);
			List<LinkedHashMap>  dvRuleDetailList=new ArrayList<LinkedHashMap>();
			log.info("ViewId :"+reconSummary.get(i)[5]);
			List<Object[]> ruleDetailsForView=appModuleSummaryRepository.findRuleIdAndTypeIdByRuleGroupIdAndViewId(recRuleGrpId,Long.valueOf(reconSummary.get(i)[5].toString()),fDate,tDate);
			for(int r=0;r<ruleDetailsForView.size();r++)
			{
				//log.info("ruleDetailsForView.get(r)[0] :"+ruleDetailsForView.get(r)[0]);
				LinkedHashMap dvRuleDetail=new LinkedHashMap();
				dvRuleDetail.put("ruleId", ruleDetailsForView.get(r)[0]);
				/*					if(Long.valueOf(ruleDetailsForView.get(r)[0].toString())==0)
						dvRuleDetail.put("ruleName", "Manual");
					else
					{
						Rules rule=rulesRepository.findOne(Long.valueOf(ruleDetailsForView.get(r)[0].toString()));
						dvRuleDetail.put("ruleName",rule.getRuleCode());
					}*/
				dvRuleDetail.put("type", ruleDetailsForView.get(r)[1]);
				dvRuleDetailList.add(dvRuleDetail);
			}
			map.put("dvRuleDetails", dvRuleDetailList);
		

			Connection conn = null;
			Statement stmtDv = null;
			Statement stmtApprovedAmt = null;

			DataViewsColumns dvColumn=dataViewsColumnsRepository.findByDataViewIdAndQualifier(dv.getId(), "AMOUNT");
			String ammountQualifier="";
			if(dvColumn.getRefDvType()!=null &&dvColumn.getRefDvType().equalsIgnoreCase("File Template"))
			{
				FileTemplateLines ftl=fileTemplateLinesRepository.findOne(Long.valueOf(dvColumn.getRefDvColumn()));
				ammountQualifier=ftl.getColumnAlias();
			}
			else
				ammountQualifier=dvColumn.getColumnName();
			
			/**to get fileDate or qualifier Date**/
			String date=dashBoardV4Service.getFileDateOrQualifier(dv.getId(), tenantId);
			ResultSet resultDv=null;
			//	ResultSet resultTotalViolationQuery=null;
				ResultSet resultApprovedAmt=null;
try{
	
			DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
			conn = ds.getConnection();
			checkConnection = true;
			log.info("Checking Connection Open in getSummaryInfoForReconciliationV3:"+checkConnection);

			log.info("Connected database successfully...");
			stmtDv = conn.createStatement();
			stmtApprovedAmt = conn.createStatement();

			
			String query="";

			if(finalSrcIdList!=null && !finalSrcIdList.isEmpty())
			{
				String finalSrcIds=finalSrcIdList.toString().replaceAll("\\[", "").replaceAll("\\]", "");

				query="select DATEDIFF( SYSDATE(), `v`.`"+date+"`) as `rule_age`,count(scrIds),sum(`"+ammountQualifier+"`) as `"+ammountQualifier+"` from `"+dv.getDataViewName().toLowerCase().toLowerCase()+"` v where "+date+">='"+fDate+"' and "+date+"<='"+tDate+"' "
						+ " and scrIds not in ("+finalSrcIds+")group by rule_age";
				//log.info("query in if  :"+query);

			}
			else
			{
				query="select DATEDIFF( SYSDATE(), `v`.`"+date+"`) as `rule_age`,count(scrIds),sum(`"+ammountQualifier+"`) as `"+ammountQualifier+"` from `"+dv.getDataViewName().toLowerCase().toLowerCase()+"` v where "+date+">='"+fDate+"' and "+date+"<='"+tDate+"' "
						+ "group by rule_age";
				//log.info("query in else  :"+query);
			}


		//	String	TotalcountQuery="select count(scrIds) from `"+dv.getDataViewName().toLowerCase().toLowerCase()+"` v where fileDate between '"+fDate+"' and '"+tDate+"' ";
			//log.info("query in TotalcountQuery  :"+TotalcountQuery);

			//log.info("query :"+query);
			/*resultTotalViolationQuery=stmtDv.executeQuery(TotalcountQuery);
			while(resultTotalViolationQuery.next())
			{
				totalViolationCount=totalViolationCount+Long.valueOf(resultTotalViolationQuery.getString("count(scrIds)").toString());
			}*/
			
			
			//log.info("query for violation:"+query);
			resultDv=stmtDv.executeQuery(query);
			ResultSetMetaData rsmd2 = resultDv.getMetaData();
			int columnCount = rsmd2.getColumnCount();
			map.put("violationAmount",0);
			map.put("violationCount", 0);
			log.info("violation :"+violation);
		
			while(resultDv.next())
			{
				//log.info("resultDv.getString(rule_age) :"+resultDv.getString("rule_age"));
				totalUnRecCount=	totalUnRecCount+Double.valueOf(resultDv.getString("count(scrIds)"));
				
				int ruleAge=Integer.valueOf(resultDv.getString("rule_age").toString());
			
				// int ruleAge=1;

				//log.info("ruleAge :"+ruleAge+" and violation :"+violation);
				if(ruleAge>=violation)
				{
					//log.info("if rule age is greater than violation");
					map.put("violationCount", Long.valueOf(resultDv.getString("count(scrIds)").toString()));
					violationCount=violationCount+Long.valueOf(resultDv.getString("count(scrIds)").toString());
					map.put("violationAmount",dform.format(Double.valueOf(resultDv.getString(ammountQualifier).toString())));
				}
				else
				{
					map.put("violationAmount",0);
					map.put("violationCount", 0);
				}
			}
			
			//log.info("totalUnRecCount after violation query:"+totalUnRecCount);
			dataMap.add(map);
		}
catch(Exception e)
{
	log.info("exception in getSummaryInfoForReconciliationV3 :"+e);
}

		/*	if(finalApprovedSrcIds!=null && !finalApprovedSrcIds.isEmpty())
			{
				String finalSrcIds=finalSrcIdList.toString().replaceAll("\\[", "").replaceAll("\\]", "");

				query="select sum(`"+ammountQualifier+"`) as `"+ammountQualifier+"` from `"+dv.getDataViewName().toLowerCase().toLowerCase()+"` v where fileDate between '"+fDate+"' and '"+tDate+"' "
						+ " and scrIds not in ("+finalSrcIds+")";

			}
			else
			{
				query="select sum(`"+ammountQualifier+"`) as `"+ammountQualifier+"` from `"+dv.getDataViewName().toLowerCase().toLowerCase()+"` v where fileDate between '"+fDate+"' and '"+tDate+"'";


			}
			//log.info("query for unapproved amt :"+query);
			resultApprovedAmt=stmtApprovedAmt.executeQuery(query);


			while(resultApprovedAmt.next())
			{
				if(resultApprovedAmt.getString(ammountQualifier)!=null)
					map.put("unApprovedAmount",dform.format(Double.valueOf(resultApprovedAmt.getString(ammountQualifier).toString())));
				else
					map.put("unApprovedAmount",0);
			}*/
	
finally
{
			if(resultDv!=null)
			{
				try {
					resultDv.close();
					checkConnection=false;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					log.info("Exception while closing resultDv in getSummaryInfoForReconciliationV3 : "+e);
				}
				
			}
			if(resultApprovedAmt!=null)
			{
				try {
					resultApprovedAmt.close();
					checkConnection=false;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					log.info("Exception while closing resultApprovedAmt in getSummaryInfoForReconciliationV3 : "+e);
				}
				
			}
			if(stmtDv!=null)
			{
				try {
					stmtDv.close();
					checkConnection=false;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					log.info("Exception while closing stmtDv in getSummaryInfoForReconciliationV3 : "+e);
				}
				
				
			}
			if(stmtApprovedAmt!=null)
			{
				try {
					stmtApprovedAmt.close();
					checkConnection=false;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					log.info("Exception while closing stmtApprovedAmt in getSummaryInfoForReconciliationV3 : "+e);
				}
				
			}
			if(conn!=null)
			{
				try {
					conn.close();
					checkConnection=false;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					log.info("Exception while closing conn in getSummaryInfoForReconciliationV3 : "+e);
				}
				
			}
}
		}
		//	log.info("totalUnReconAmt :"+totalUnReconAmt);
		//	log.info("totalDVAmt :"+totalDVAmt);
		Double unReconItemsValuePer=0d;
		Double unApprovedCtPer=0d;
		double unReconItemsViolationPer=0l;
		Double totalReconPer=0d;
		if(totalReconAmt>0)
		{
			totalReconPer=(totalReconAmt/totalDVAmt)*100;
			unReconItemsValuePer=100-totalReconPer;
			//	unReconItemsValuePer=(totalUnReconAmt/totalDVAmt)*100;

		}
		if(totalDVCount>0)
		{
			log.info("totalDVCount :"+totalDVCount);
			log.info("totalUnApprovedCt :"+totalUnApprovedCt);
			unApprovedCtPer=(totalUnApprovedCt/totalDVCount)*100;
		}
		if(totalUnRecCount>0)
		{
			//log.info("violationCount :"+violationCount);
			//log.info("totalViolationCount :"+totalViolationCount);
			unReconItemsViolationPer=((double)violationCount/totalUnRecCount)*100;
		}
		//	log.info("rulesList :"+rulesList);
		List<Object[]> reconTotalApprovalCount=appModuleSummaryRepository.fetchTotalApprovalCount(recRuleGrpId,fDate,tDate);	
		Double totalApprovalCt=0d;
		Double totalUnApprovalCt=0d;
		Double totalUnApprovalCtPer=0d;
		if(reconTotalApprovalCount.size()>0)
		{
			totalApprovalCt= Double.valueOf(reconTotalReconciledAmountAndCount.get(0)[0].toString());
			totalUnApprovalCt= totaldvCount-totalApprovalCt;
			totalUnApprovalCtPer=totalApprovalCt/totaldvCount*100;
		}
		log.info("****totalUnApprovalCtPer*:"+totalUnApprovalCtPer);

		//List< Object[]> reconSummaryForUnItemsByValue=appModuleSummaryRepository.fetchReconCountAndUnReconciledCountBetweenGivenDates(recRuleGrpId,fDate,tDate);
		
		BigDecimal totalDvCount=appModuleSummaryRepository.fetchTotalDvcountForReconGroup(recRuleGrpId, fDate, tDate);
		BigDecimal totalDvAmt=appModuleSummaryRepository.fetchTotalDvAmtForReconGroup(recRuleGrpId, fDate, tDate);
		
		
		List< Object[]> reconSummaryForUnItemsByValue=appModuleSummaryRepository.fetchReconciledAndUnReconciledAmountsAndCounts(recRuleGrpId, fDate, tDate,totalDvCount,totalDvAmt);

		if(reconSummaryForUnItemsByValue.size()>0)
		{
			finalMap.put("unReconItemsValue", reconSummaryForUnItemsByValue.get(0)[7]);
			finalMap.put("unReconItemsValuePer", reconSummaryForUnItemsByValue.get(0)[8]);
			finalMap.put("unReconItemsCount",reconSummaryForUnItemsByValue.get(0)[3]);
			finalMap.put("reconItemsCount",reconSummaryForUnItemsByValue.get(0)[1]);
			finalMap.put("reconItemsCountPer",reconSummaryForUnItemsByValue.get(0)[2]);
			finalMap.put("unReconItemsCountPer",reconSummaryForUnItemsByValue.get(0)[4]);


		
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
		}

		finalMap.put("rulesList", rulesList);
		finalMap.put("rulesIdList", rulesIdList);

		finalMap.put("unReconItemsViolation", violationCount);
		finalMap.put("unReconItemsViolationPer", Double.valueOf(dform.format(unReconItemsViolationPer)));
		finalMap.put("unReconItemsByCount", totalUnReconciledDvCount);
		finalMap.put("unReconItemsByCountPer", Double.valueOf(dform.format( totalUnReconciledCountPer)));
		finalMap.put("reconciliationData", dataMap);





		log.info("******end Time : "+ZonedDateTime.now()+"*******");
		log.info("Checking Connection Close in getSummaryInfoForReconciliationV3:"+checkConnection);
		return finalMap;
	}

	
	
	@PostMapping("/getSummaryInfoForAccountingV3")
	@Timed 
	public LinkedHashMap getSummaryInfoForAccountingV3(HttpServletRequest request,@RequestParam(value="processId",required=false) String processId ,
			@RequestBody HashMap dates,@RequestParam int violation,@RequestParam(value="ruleGroupId",required=false) Long ruleGroupId)
	{
		log.info("getSummaryInfoForAccountingV2 of processId:"+processId +"dates"+dates);
		Boolean checkConnection=true;
		LinkedHashMap finalMap=new LinkedHashMap();
		List<LinkedHashMap> dataMap=new ArrayList<LinkedHashMap>();
		HashMap map2=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map2.get("tenantId").toString());
		Processes processes = processesRepository.findByTenantIdAndIdForDisplay(tenantId, processId);
		Long recRuleGrpId=0l;

		if(processId!=null && ruleGroupId==null)

		{

			ProcessDetails procesRecDet=processDetailsRepository.findByProcessIdAndTagType(processes.getId(), "accountingRuleGroup");
			if(procesRecDet!=null)
				recRuleGrpId=procesRecDet.getTypeId();
		}
		else if(ruleGroupId!=null && processId==null)
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
		Double totalUnAccountedCount=0d;

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
			//log.info("totalTypeAmountNtAct while loop :"+totalTypeAmountNtAct);
			totalTypeAmountNtAct=totalTypeAmountNtAct+Double.valueOf(totalTypeAmtAndTypeCtNtAct.get(n)[1].toString());
			totalTypeCountNtAct=totalTypeCountNtAct+Double.valueOf(totalTypeAmtAndTypeCtNtAct.get(n)[2].toString());
			//log.info("totalTypeAmountNtAct end loop :"+totalTypeAmountNtAct);
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


		List<BigInteger> distViewsIds=appModuleSummaryRepository.findDistinctViewIdByRuleGroupId(recRuleGrpId,fDate,tDate);
		log.info("distViewsIds :"+distViewsIds);
		Long violationCount=0l;
		Long totalViolationCount=0l;
		Long totalUnapprovedCount=0l;
		//Long totalapprovedCount=0l;
		Long totalinitiatedCount=0l;
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

				try{
					DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
					conn = ds.getConnection();
					checkConnection = true;
					log.info("Checking Connection Open in getSummaryInfoForAccountingV3:"+checkConnection);
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

							query="select DATEDIFF( SYSDATE(), `v`.`"+date+"`) as `rule_age`,count(scrIds),sum(`"+ammountQualifier+"`) as `"+ammountQualifier+"` from `"+dv.getDataViewName().toLowerCase().toLowerCase()+"` v where "+date+" >= '"+fDate+"' and "+date+"<='"+tDate+"' "
									+ " and scrIds not in ("+finalSrcIds+")group by rule_age";
							//log.info("in if query :"+query);

						}
						else
						{
							query="select DATEDIFF( SYSDATE(), `v`.`"+date+"`) as `rule_age`,count(scrIds),sum(`"+ammountQualifier+"`) as `"+ammountQualifier+"` from `"+dv.getDataViewName().toLowerCase().toLowerCase()+"` v where "+date+" >= '"+fDate+"' and "+date+"<='"+tDate+"' "
									+ "group by rule_age";
							//	log.info("in if else :"+query);
						}



						if(finalSrcIdList.size()>0)
						{

							resultDv=stmtDv.executeQuery(query);
							ResultSetMetaData rsmd2 = resultDv.getMetaData();
							int columnCount = rsmd2.getColumnCount();
							map.put("violationAmount",0);
							map.put("violationCount", 0);
							//	log.info("violation :"+violation);


							while(resultDv.next())
							{
								//log.info("resultDv.getString(rule_age) :"+resultDv.getString("rule_age"));
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
					/*	if(process .equalsIgnoreCase("accounted"))
					{
						log.info("accountingSummary.get(i)[9] :"+accountingSummary.get(i)[9]);
						totalActAmt=totalActAmt+Double.valueOf(accountingSummary.get(i)[9].toString());
						totalActct=totalActct+Double.valueOf(accountingSummary.get(i)[1].toString());
						map.put("unApprovedCount", Double.valueOf(dform.format(Double.valueOf(accountingSummary.get(i)[6].toString()))));
					//	totalUnapprovedCount=totalUnapprovedCount+Long.valueOf(accountingSummary.get(i)[6].toString());
					//	List<BigInteger> finalApprovedSrcIds=accountedSummaryRepository.fetchDistinctApprovedRowIds(recRuleGrpId, viewId.longValue()) ;
					//	finalApprovedSrcIds.addAll(finalApprovedSrcIds);
						//totalapprovedCount=totalapprovedCount+Long.valueOf(accountingSummary.get(i)[7].toString());

						if(finalApprovedSrcIds!=null && !finalApprovedSrcIds.isEmpty())
						{
							String finalSrcIds=finalApprovedSrcIds.toString().replaceAll("\\[", "").replaceAll("\\]", "");

							query="select case when sum(`"+ammountQualifier+"`) is null then 0 else sum(`"+ammountQualifier+"`) end as `"+ammountQualifier+"` from `"+dv.getDataViewName().toLowerCase().toLowerCase()+"` v where fileDate between '"+fDate+"' and '"+tDate+"' "
									+ " and scrIds not in ("+finalSrcIds+")";

						}
						else
						{
							query="select case when sum(`"+ammountQualifier+"`) is null then 0 else sum(`"+ammountQualifier+"`) end as `"+ammountQualifier+"` from `"+dv.getDataViewName().toLowerCase().toLowerCase()+"` v where fileDate between '"+fDate+"' and '"+tDate+"'";


						}
						//log.info("query for unapproved amt :"+query);
						resultApp=stmtApp.executeQuery(query);



						while(resultApp.next())
						{
							map.put("unApprovedAmount", Double.valueOf(dform.format(Double.valueOf(resultApp.getString(ammountQualifier).toString()))));
						}
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

					dataMap.add(map);
				}
				catch(Exception e)
				{
					log.info("Exception in getSummaryInfoForAccountingV3 :"+e);
				}
				finally
				{
					if(resultDv!=null)
					{
						try {
							resultDv.close();
							checkConnection=false;
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							log.info("Exception while closing resultDv in getSummaryInfoForAccountingV3 :"+e);
						}

					}

					if(resultApp!=null)
					{
						try {
							resultApp.close();
							checkConnection=false;
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							log.info("Exception while closing resultApp in getSummaryInfoForAccountingV3 :"+e);
						}

					}
					if(stmtApp!=null)
					{
						try {
							stmtApp.close();
							checkConnection=false;
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							log.info("Exception while closing stmtApp in getSummaryInfoForAccountingV3 :"+e);
						}

					}
					if(stmtDv!=null)
					{
						try {
							stmtDv.close();
							checkConnection=false;
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							log.info("Exception while closing stmtDv in getSummaryInfoForAccountingV3 :"+e);
						}

					}
					if(conn!=null)
					{
						try {
							conn.close();
							checkConnection=false;
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							log.info("Exception while closing conn in getSummaryInfoForAccountingV3 :"+e);
						}

					}
				}
			}





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


		log.info("Checking Connection close in getSummaryInfoForAccountingV3:"+checkConnection);

		return finalMap;
	}

	
	@PostMapping("/transformationAnalysisforGivenPeriodV3")
	@Timed
	public List<LinkedHashMap> transformationAnalysisforGivenPeriodV3(@RequestParam String processId,@RequestBody HashMap dates,HttpServletRequest request,@RequestParam String type,HttpServletResponse response) throws SQLException, ParseException 
	{
		log.info("Rest Request to get aging analysis in service:"+dates);

		//	LinkedHashMap eachMap=new LinkedHashMap();
		List<LinkedHashMap> finallist=new ArrayList<LinkedHashMap>();
		HashMap map=userJdbcService.getuserInfoFromToken(request);
      	Long tenantId=Long.parseLong(map.get("tenantId").toString());
		Processes processes = processesRepository.findByTenantIdAndIdForDisplay(tenantId, processId);
		Processes process=processesRepository.findOne(processes.getId());

		if(processId!=null)

		{

			List<BigInteger> profileId=processDetailsRepository.findTypeIdByProcessIdAndTagType(processes.getId(), "sourceProfile");

			ZonedDateTime fmDate=ZonedDateTime.parse(dates.get("startDate").toString());

			ZonedDateTime toDate=ZonedDateTime.parse(dates.get("endDate").toString());



			LocalDate fDate=fmDate.toLocalDate();

			LocalDate tDate=toDate.toLocalDate();

			Long days=Duration.between(fDate.atStartOfDay(), tDate.atStartOfDay()).toDays()+1;
			LookUpCode lookUpCode=new LookUpCode();
			//	if(type.equalsIgnoreCase("months"))

			if(profileId.size()>0)
			{
				if(days>=30 && type.equalsIgnoreCase("none"))
				{

					lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("DASHBOARD", "MONTHS", process.getTenantId());
					response.addHeader("tarnsformationPeriodAnalysisType",lookUpCode.getMeaning() );

					List<LinkedHashMap> dateMapList=dashBoardV3Service.datesService(fDate.minusMonths(1), tDate.minusMonths(1));
					log.info("dateMapList :"+dateMapList.size());
					for(int i=dateMapList.size()-1;i>=0;i--)
					{
						//log.info("i: "+i);

						//log.info("date at "+i+" is :"+dateMapList.get(i).get("startDate").toString());
						LinkedHashMap monthMap=new LinkedHashMap();
						List<Object[]> filesExtracted1WSummary=sourceFileInbHistoryRepository.fetchTransfomedCountBetweenGivenDateForWeek
								(profileId,LocalDate.parse(dateMapList.get(i).get("startDate").toString())+"%",LocalDate.parse(dateMapList.get(i).get("endDate").toString())+"%");


						if(filesExtracted1WSummary!=null)
						{

							monthMap.put("labelValue", dateMapList.get(i).get("month"));
							LinkedHashMap transformed=new LinkedHashMap();
							transformed.put("count", Double.valueOf(dform.format(Double.valueOf(filesExtracted1WSummary.get(0)[1].toString()))));
							transformed.put("countPer", Double.valueOf(dform.format(Double.valueOf(filesExtracted1WSummary.get(0)[2].toString()))));
							monthMap.put("transformed", transformed);
							LinkedHashMap ntTransformed=new LinkedHashMap();
							ntTransformed.put("count", Double.valueOf(dform.format(Double.valueOf(filesExtracted1WSummary.get(0)[4].toString()))));
							ntTransformed.put("countPer", Double.valueOf(dform.format(Double.valueOf(filesExtracted1WSummary.get(0)[3].toString()))));
							monthMap.put("ntTransformed", ntTransformed);
							monthMap.put("dateRange", dateMapList.get(i));
							finallist.add(monthMap);

						}

					}

				}


				/**tarnsformationPeriodAnalysisType weeks**/


				if((days>7 && days<30) || type.equalsIgnoreCase("months"))
				{
					lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("DASHBOARD", "WEEKS", process.getTenantId());
					response.addHeader("tarnsformationPeriodAnalysisType",lookUpCode.getMeaning() );

					List<LinkedHashMap> dateMapList=dashBoardV3Service.weeksOfAMonth(fDate,tDate);


					log.info("dateMapList :"+dateMapList.size());
					for(int i=0;i<dateMapList.size();i++)
					{
						log.info("i: "+i);

						//log.info("date at "+i+" is :"+dateMapList.get(i).get("startDate").toString());

						LinkedHashMap monthMap=new LinkedHashMap();


						List<Object[]> filesExtracted1WSummary=sourceFileInbHistoryRepository.fetchTransfomedCountBetweenGivenDateForWeek
								(profileId,LocalDate.parse(dateMapList.get(i).get("startDate").toString())+"%",LocalDate.parse(dateMapList.get(i).get("endDate").toString())+"%");


						if(filesExtracted1WSummary!=null)
						{

							int weekNum=i+1;
							monthMap.put("labelValue","week-"+weekNum);
							LinkedHashMap transformed=new LinkedHashMap();
							transformed.put("count", Double.valueOf(dform.format(Double.valueOf(filesExtracted1WSummary.get(0)[1].toString()))));
							transformed.put("countPer", Double.valueOf(dform.format(Double.valueOf(filesExtracted1WSummary.get(0)[2].toString()))));
							monthMap.put("transformed", transformed);
							LinkedHashMap ntTransformed=new LinkedHashMap();
							ntTransformed.put("count", Double.valueOf(dform.format(Double.valueOf(filesExtracted1WSummary.get(0)[4].toString()))));
							ntTransformed.put("countPer", Double.valueOf(dform.format(Double.valueOf(filesExtracted1WSummary.get(0)[3].toString()))));
							monthMap.put("ntTransformed", ntTransformed);
							monthMap.put("dateRange", dateMapList.get(i));
							finallist.add(monthMap);

						}




					}

				}

				//if(type.equalsIgnoreCase("days"))
				if(days<=7)
				{
					lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("DASHBOARD", "DAYS", process.getTenantId());
					response.addHeader("tarnsformationPeriodAnalysisType",lookUpCode.getMeaning() );
					while(tDate.plusDays(1).isAfter(fDate)){




						LinkedHashMap monthMap=new LinkedHashMap();
						List<Object[]> totalFilesExtracted=sourceFileInbHistoryRepository.fetchTransfomedCountBetweenForADate
								(profileId,tDate+"%");
						if(totalFilesExtracted!=null)
						{
							String[] month=dashBoardV2Service.dateSpecifiedFormat(tDate.toString());
							monthMap.put("labelValue",month[0]+" "+month[1]);
							LinkedHashMap transformed=new LinkedHashMap();
							transformed.put("count", Double.valueOf(dform.format(Double.valueOf(totalFilesExtracted.get(0)[1].toString()))));
							transformed.put("countPer", Double.valueOf(dform.format(Double.valueOf(totalFilesExtracted.get(0)[2].toString()))));
							monthMap.put("transformed", transformed);
							LinkedHashMap ntTransformed=new LinkedHashMap();
							ntTransformed.put("count", Double.valueOf(dform.format(Double.valueOf(totalFilesExtracted.get(0)[4].toString()))));
							ntTransformed.put("countPer", Double.valueOf(dform.format(Double.valueOf(totalFilesExtracted.get(0)[3].toString()))));
							monthMap.put("ntTransformed", ntTransformed);
							LinkedHashMap datesMap=new LinkedHashMap();
							datesMap.put("startDate", tDate);
							datesMap.put("endDate", tDate);
							monthMap.put("dateRange",datesMap);
							finallist.add(monthMap);

						}
						tDate=tDate.minusDays(1);
					}

				}
			}

		}
		return finallist;

	}
	
	
	
	
	
	
	
	
	
/*	@PostMapping("/extractionAnalysisforGivenPeriodV3")
	@Timed
	public List<LinkedHashMap> extractionAnalysisforGivenPeriodV3(@RequestParam Long processId,@RequestBody HashMap dates,HttpServletRequest request,@RequestParam String type,HttpServletResponse response) throws SQLException, ParseException 
	{
		log.info("Rest Request to get aging analysis in service:"+dates);

		//	LinkedHashMap eachMap=new LinkedHashMap();
		List<LinkedHashMap> finallist=new ArrayList<LinkedHashMap>();
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());


		ApplicationPrograms app=applicationProgramsRepository.findByPrgmNameAndTenantId("DataExtraction", tenantId);

		if(processId!=null)

		{

			List<BigInteger> profileId=processDetailsRepository.findTypeIdByProcessIdAndTagType(processId, "sourceProfile");

			ZonedDateTime fmDate=ZonedDateTime.parse(dates.get("startDate").toString());

			ZonedDateTime toDate=ZonedDateTime.parse(dates.get("endDate").toString());

			log.info("fmDate :"+fmDate);

			log.info("toDate :"+toDate);


			LocalDate fDate=fmDate.toLocalDate();

			LocalDate tDate=toDate.toLocalDate();
			Long days=Duration.between(fDate.atStartOfDay(), tDate.atStartOfDay()).toDays();
			LookUpCode lookUpCode=new LookUpCode();
			List<BigInteger> jobDetails=jobDetailsRepository.findByTenantIdAndProgrammIdAndParameterArgument1(app.getId(),profileId,fDate+"%",tDate+"%");

			//	if(type.equalsIgnoreCase("months"))
			if(days>30)
			{

				lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("DASHBOARD", "DAYS", tenantId);
				response.addHeader("tarnsformationPeriodAnalysisType",lookUpCode.getMeaning() );
				List<LinkedHashMap> dateMapList=dashBoardV3Service.datesService(fDate.minusMonths(1), tDate.minusMonths(1));
				log.info("dateMapList :"+dateMapList.size());
				for(int i=dateMapList.size()-1;i>=0;i--)
				{
					log.info("i: "+i);

					log.info("date at "+i+" is :"+dateMapList.get(i).get("startDate").toString());
					LinkedHashMap monthMap=new LinkedHashMap();

					LinkedHashMap extraction=dashBoardV2Service.extractionAnalysisforGivenPeriod(jobDetails, LocalDate.parse(dateMapList.get(i).get("startDate").toString()), LocalDate.parse(dateMapList.get(i).get("endDate").toString()), toDate);

					monthMap.put("labelValue", dateMapList.get(i).get("month"));
					LinkedHashMap extracted=new LinkedHashMap();
					extracted.put("count", extraction.get("totalExtracted"));
					extracted.put("countPer",extraction.get("totalExtractedPer"));
					monthMap.put("extracted", extracted);
					LinkedHashMap ntExtracted=new LinkedHashMap();
					ntExtracted.put("count", extraction.get("totalExtractionFailedCt"));
					ntExtracted.put("countPer",extraction.get("totalExtractionFailedCtPer"));
					monthMap.put("ntExtracted", ntExtracted);
					monthMap.put("dateRange", dateMapList.get(i));
					finallist.add(monthMap);



				}

			}


			*//**Reconciliation weeks**//*


			//	if(type.equalsIgnoreCase("weeks"))
			if(days>7 && days<30)
			{
				lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("DASHBOARD", "WEEKS",tenantId);
				response.addHeader("tarnsformationPeriodAnalysisType",lookUpCode.getMeaning() );

				List<LinkedHashMap> dateMapList=dashBoardV3Service.weeksOfAMonth(tDate);
				log.info("dateMapList :"+dateMapList);

				log.info("dateMapList :"+dateMapList.size());
				for(int i=0;i<dateMapList.size();i++)
				{
					log.info("i: "+i);

					log.info("date at "+i+" is :"+dateMapList.get(i).get("startDate").toString());
					if(LocalDate.parse(dateMapList.get(i).get("startDate").toString()).isAfter(fDate) || LocalDate.parse(dateMapList.get(i).get("startDate").toString()).equals(fDate))
					{
						LinkedHashMap monthMap=new LinkedHashMap();


						LinkedHashMap extraction=dashBoardV2Service.extractionAnalysisforGivenPeriod(jobDetails, LocalDate.parse(dateMapList.get(i).get("startDate").toString()), LocalDate.parse(dateMapList.get(i).get("endDate").toString()), toDate);
						int weekNum=i+1;
						monthMap.put("labelValue","week-"+weekNum);
						LinkedHashMap extracted=new LinkedHashMap();
						extracted.put("count", extraction.get("totalExtracted"));
						extracted.put("countPer",extraction.get("totalExtractedPer"));
						monthMap.put("extracted", extracted);
						LinkedHashMap ntExtracted=new LinkedHashMap();
						ntExtracted.put("count", extraction.get("totalExtractionFailedCt"));
						ntExtracted.put("countPer",extraction.get("totalExtractionFailedCtPer"));
						monthMap.put("ntExtracted", ntExtracted);
						monthMap.put("dateRange", dateMapList.get(i));
						finallist.add(monthMap);



					}
				}

			}

			//if(type.equalsIgnoreCase("days"))
			if(days<=7)
			{
				lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("DASHBOARD", "DAYS", tenantId);
				response.addHeader("tarnsformationPeriodAnalysisType",lookUpCode.getMeaning() );
				while(tDate.plusDays(1).isAfter(fDate)){



					log.info("date at "+tDate+" is :"+tDate);

					LinkedHashMap monthMap=new LinkedHashMap();
					String[] month=dashBoardV2Service.dateSpecifiedFormat(tDate.toString());
					monthMap.put("labelValue",month[0]+" "+month[1]);
					LinkedHashMap extraction=dashBoardV2Service.extractionAnalysisforGivenPeriod(jobDetails, tDate, tDate, toDate);


					LinkedHashMap extracted=new LinkedHashMap();
					extracted.put("count", extraction.get("totalExtracted"));
					extracted.put("countPer",extraction.get("totalExtractedPer"));
					monthMap.put("extracted", extracted);
					LinkedHashMap ntExtracted=new LinkedHashMap();
					ntExtracted.put("count", extraction.get("totalExtractionFailedCt"));
					ntExtracted.put("countPer",extraction.get("totalExtractionFailedCtPer"));
					monthMap.put("ntExtracted", ntExtracted);
					LinkedHashMap datesMap=new LinkedHashMap();
					datesMap.put("startDate", tDate);
					datesMap.put("endDate", tDate);
					monthMap.put("dateRange",datesMap);
					finallist.add(monthMap);




					tDate=tDate.minusDays(1);
				}

			}

		}
		return finallist;

	}*/
	
	
	
	
	
	
	@PostMapping("/extractionAnalysisforGivenPeriodV3")
	@Timed
	public List<LinkedHashMap> extractionAnalysisforGivenPeriodV3(@RequestParam String processId,@RequestBody HashMap dates,HttpServletRequest request,@RequestParam String type,HttpServletResponse response) throws SQLException, ParseException 
	{
		log.info("Rest Request to get aging analysis in service:"+dates);

		//	LinkedHashMap eachMap=new LinkedHashMap();
		List<LinkedHashMap> finallist=new ArrayList<LinkedHashMap>();
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());

		Processes processes = processesRepository.findByTenantIdAndIdForDisplay(tenantId, processId);

		ApplicationPrograms app=applicationProgramsRepository.findByPrgmNameAndTenantId("DataExtraction", tenantId);

		if(processId!=null)

		{

			List<BigInteger> profileId=processDetailsRepository.findTypeIdByProcessIdAndTagType(processes.getId(), "sourceProfile");

			ZonedDateTime fmDate=ZonedDateTime.parse(dates.get("startDate").toString());

			ZonedDateTime toDate=ZonedDateTime.parse(dates.get("endDate").toString());



			LocalDate fDate=fmDate.toLocalDate();

			LocalDate tDate=toDate.toLocalDate();
			Long days=Duration.between(fDate.atStartOfDay(), tDate.atStartOfDay()).toDays()+1;
			LookUpCode lookUpCode=new LookUpCode();
			//	List<BigInteger> jobDetails=jobDetailsRepository.findByTenantIdAndProgrammIdAndParameterArgument1(app.getId(),profileId,fDate+"%",tDate+"%");

			//	if(type.equalsIgnoreCase("months"))
			if(profileId.size()>0)
			{

				if(days>=30 && type.equalsIgnoreCase("none"))
				{

					lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("DASHBOARD", "MONTHS", tenantId);
					response.addHeader("tarnsformationPeriodAnalysisType",lookUpCode.getMeaning() );
					List<LinkedHashMap> dateMapList=dashBoardV3Service.datesService(fDate.minusMonths(1), tDate.minusMonths(1));
					log.info("dateMapList :"+dateMapList.size());
					for(int i=dateMapList.size()-1;i>=0;i--)
					{


						log.info("date at "+i+" is :"+dateMapList.get(i).get("startDate").toString());
						LinkedHashMap monthMap=new LinkedHashMap();
						List<SourceFileInbHistory> filesExtracted1WSummary=sourceFileInbHistoryRepository.findByProfileIdInAndFileReceivedDateBetween
								(profileId,LocalDate.parse(dateMapList.get(i).get("startDate").toString())+"%",LocalDate.parse(dateMapList.get(i).get("endDate").toString())+"%");

						//	LinkedHashMap extraction=dashBoardV2Service.extractionAnalysisforGivenPeriod(jobDetails, LocalDate.parse(dateMapList.get(i).get("startDate").toString()), LocalDate.parse(dateMapList.get(i).get("endDate").toString()), toDate);

						monthMap.put("labelValue", dateMapList.get(i).get("month"));
						LinkedHashMap extracted=new LinkedHashMap();
						extracted.put("count", filesExtracted1WSummary.size());
						//	extracted.put("countPer",extraction.get("totalExtractedPer"));
						monthMap.put("extracted", extracted);
						/*LinkedHashMap ntExtracted=new LinkedHashMap();
					ntExtracted.put("count", extraction.get("totalExtractionFailedCt"));
					ntExtracted.put("countPer",extraction.get("totalExtractionFailedCtPer"));
					monthMap.put("ntExtracted", ntExtracted);*/
						monthMap.put("dateRange", dateMapList.get(i));
						finallist.add(monthMap);



					}

				}


				/**Reconciliation weeks**/


				//	if(type.equalsIgnoreCase("weeks"))
				if((days>7 && days<30) || type.equalsIgnoreCase("months"))
				{
					lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("DASHBOARD", "WEEKS",tenantId);
					response.addHeader("tarnsformationPeriodAnalysisType",lookUpCode.getMeaning() );

					List<LinkedHashMap> dateMapList=dashBoardV3Service.weeksOfAMonth(fDate,tDate);


					log.info("dateMapList :"+dateMapList.size());
					for(int i=0;i<dateMapList.size();i++)
					{
						log.info("i: "+i);

						//log.info("date at "+i+" is :"+dateMapList.get(i).get("startDate").toString());

						LinkedHashMap monthMap=new LinkedHashMap();
						List<SourceFileInbHistory> filesExtracted1WSummary=sourceFileInbHistoryRepository.findByProfileIdInAndFileReceivedDateBetween
								(profileId,LocalDate.parse(dateMapList.get(i).get("startDate").toString())+"%",LocalDate.parse(dateMapList.get(i).get("endDate").toString())+"%");


						int weekNum=i+1;
						monthMap.put("labelValue","week-"+weekNum);
						LinkedHashMap extracted=new LinkedHashMap();
						extracted.put("count",filesExtracted1WSummary.size());
						//	extracted.put("countPer",extraction.get("totalExtractedPer"));
						monthMap.put("extracted", extracted);
						/*	LinkedHashMap ntExtracted=new LinkedHashMap();
						ntExtracted.put("count", extraction.get("totalExtractionFailedCt"));
						ntExtracted.put("countPer",extraction.get("totalExtractionFailedCtPer"));
						monthMap.put("ntExtracted", ntExtracted);*/
						monthMap.put("dateRange", dateMapList.get(i));
						finallist.add(monthMap);




					}

				}

				//if(type.equalsIgnoreCase("days"))
				if(days<=7)
				{
					lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("DASHBOARD", "DAYS", tenantId);
					response.addHeader("tarnsformationPeriodAnalysisType",lookUpCode.getMeaning() );
					while(tDate.plusDays(1).isAfter(fDate)){




						LinkedHashMap monthMap=new LinkedHashMap();
						String[] month=dashBoardV2Service.dateSpecifiedFormat(tDate.toString());
						monthMap.put("labelValue",month[0]+" "+month[1]);
						//	LinkedHashMap extraction=dashBoardV2Service.extractionAnalysisforGivenPeriod(jobDetails, tDate, tDate, toDate);

						List<SourceFileInbHistory> filesExtracted1WSummary=sourceFileInbHistoryRepository.findByProfileIdInAndFileReceivedDate
								(profileId,tDate+"%");

						LinkedHashMap extracted=new LinkedHashMap();
						extracted.put("count", filesExtracted1WSummary.size());
						//	extracted.put("countPer",extraction.get("totalExtractedPer"));
						monthMap.put("extracted", extracted);
						/*LinkedHashMap ntExtracted=new LinkedHashMap();
					ntExtracted.put("count", extraction.get("totalExtractionFailedCt"));
					ntExtracted.put("countPer",extraction.get("totalExtractionFailedCtPer"));
					monthMap.put("ntExtracted", ntExtracted);*/
						LinkedHashMap datesMap=new LinkedHashMap();
						datesMap.put("startDate", tDate);
						datesMap.put("endDate", tDate);
						monthMap.put("dateRange",datesMap);
						finallist.add(monthMap);




						tDate=tDate.minusDays(1);
					}

				}
			}

		}
		return finallist;

	}
	
	
	
	@PostMapping("/getUnProcessedOrProcessedDataForGroupByV3")
	@Timed 
	public LinkedHashMap getUnProcessedOrProcessedDataForGroupByV3(@RequestParam String processId,@RequestParam Long viewId,@RequestParam String module,@RequestParam String amtQuailifier
			,@RequestParam String groupByColmn,@RequestBody HashMap dates, @RequestParam String ruleIdList, @RequestParam String viewType,
			HttpServletRequest request) 
	{
		log.info("Rest Request to getUnProcessedOrProcessedDataForGroupByV3 with processId: "+processId+" viewId: "+viewId+" amtQuailifier: "+amtQuailifier+" groupByColmn: "+groupByColmn+" viewType: "+viewType);
		log.info("dates: "+dates+" ruleIdList: "+ruleIdList);
		Boolean checkConnection = false;

		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		Processes processes = processesRepository.findByTenantIdAndIdForDisplay(tenantId, processId);


		ProcessDetails procesRecDet=processDetailsRepository.findByProcessIdAndTagType(processes.getId(), "reconciliationRuleGroup");
		ProcessDetails procesActDet=processDetailsRepository.findByProcessIdAndTagType(processes.getId(), "accountingRuleGroup");
		ZonedDateTime fmDate=ZonedDateTime.parse(dates.get("startDate").toString());
		ZonedDateTime toDate=ZonedDateTime.parse(dates.get("endDate").toString());

		java.time.LocalDate fDate=fmDate.toLocalDate();
		java.time.LocalDate tDate=toDate.toLocalDate();


		List<BigInteger> finalSrcIdList=new ArrayList<BigInteger>();
		if(module.equalsIgnoreCase("reconciled") || module.equalsIgnoreCase("un-reconciled"))
		{
			
			
			


			if(viewType.equalsIgnoreCase("source"))
			{
				List<BigInteger> orginalRowIds=reconciliationResultRepository.fetchOrginalRowIdsByRuleGroupId(procesActDet.getTypeId(),viewId);
				log.info("orginalRowIds :"+orginalRowIds.size());
				finalSrcIdList.addAll(orginalRowIds);
			}

			else if(viewType.equalsIgnoreCase("target"))
			{
				List<BigInteger> targetRowIds=reconciliationResultRepository.fetchTargetRowIdsByRuleGroupId(procesActDet.getTypeId(),viewId);
				log.info("targetRowIds :"+targetRowIds.size());
				finalSrcIdList.addAll(targetRowIds);
			}
		
		}
		else if(module.equalsIgnoreCase("not accounted"))
		{

			List<BigInteger> accountedRowIds=accountedSummaryRepository.fetchAccountingIdsByStatusNGroupIdNViewId(procesActDet.getTypeId(), viewId,"ACCOUNTED");
			if(accountedRowIds.size()>0)
				finalSrcIdList.addAll(accountedRowIds);
			log.info("if unaccounted :"+accountedRowIds.size());
		}
		else if(module.equalsIgnoreCase("pending journals") )
		{
			log.info(" in else accounted");
			//where status = accounted and journal not entered
			List<BigInteger> accountedRowIds=accountedSummaryRepository.fetchJEPendingIdsByStatusNGroupIdNViewId(procesActDet.getTypeId(), viewId,"ACCOUNTED");
			if(accountedRowIds.size()>0)
				finalSrcIdList.addAll(accountedRowIds);
			
			
			//List<BigInteger> accountedViewIds=accountedSummaryRepository.fetchUnPostedRowIdsByRuleGrpIdAndViewId(procesActDet.getTypeId(), viewId) ;
			//finalSrcIdList.addAll(accountedViewIds);
		}

		else if(module.equalsIgnoreCase("JE Creation"))
		{
			log.info(" in else JE Creation");
			List<BigInteger> accountedRowIds=accountedSummaryRepository.fetchPostedRowIdsByRuleGrpIdAndViewId(procesActDet.getTypeId(), viewId);

			//List<BigInteger> accountedViewIds=accountedSummaryRepository.fetchPostedRowIdsByRuleGrpIdAndViewId(procesActDet.getTypeId(), viewId) ;
			finalSrcIdList.addAll(accountedRowIds);
		}
		log.info("finalSrcIdList :"+finalSrcIdList.size());
		/*		String dbUrl=env.getProperty("spring.datasource.url");
		String[] parts=dbUrl.split("[\\s@&?$+-]+");
		String host = parts[0].split("/")[2].split(":")[0];
		log.info("host :"+host);
		String schemaName=parts[0].split("/")[3];
		log.info("schemaName :"+schemaName);
		String userName = env.getProperty("spring.datasource.username");
		String password = env.getProperty("spring.datasource.password");
		String jdbcDriver = env.getProperty("spring.datasource.jdbcdriver");*/

		Connection conn = null;
		Statement stmtDv = null;
		Statement stmtTcAndAmtDv = null;
		ResultSet resultDv=null;
		ResultSet resultTcAndAmtDv=null;
		LinkedHashMap lhm=new LinkedHashMap();

		try
		{
			DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
			conn = ds.getConnection();
			checkConnection = true;
			log.info("Checking Connection Open in getUnProcessedOrProcessedDataForGroupByV3:"+checkConnection);

			log.info("Connected database successfully...");
			stmtDv = conn.createStatement();
			stmtTcAndAmtDv = conn.createStatement();


			//	ResultSet resultAct=null;
			DataViews dvName=dataViewsRepository.findOne(viewId);

			/**to get fileDate or qualifier Date**/
			String date=dashBoardV4Service.getFileDateOrQualifier(viewId, tenantId);






			String groupBy=groupByColmn;
			//	String groupBy=groupByColmns.toString().replaceAll("\\[", "").replaceAll("\\]", "");
			String finalSrcIds=finalSrcIdList.toString().replaceAll("\\[", "").replaceAll("\\]", "");
			log.info("groupBy :"+groupBy);
			String query="";
			String totalCtAndTotalAmt="";
			List<LinkedHashMap> finalMap=new ArrayList<LinkedHashMap>();

			if(module.equalsIgnoreCase("un-reconciled") || module.equalsIgnoreCase("not accounted"))
			{
				if(finalSrcIds!=null && !finalSrcIds.isEmpty())
				{
					query="select `"+groupBy+"`,round(sum(`"+amtQuailifier+"`),"+round+") as `"+amtQuailifier+"`,count(scrIds) from `"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where Date("+date+") >= '"+fDate+"' and Date("+date+") <='"+tDate+"' "
							+ " and scrIds not in ("+finalSrcIds+")group by `"+groupBy+"` order by `"+amtQuailifier+"` desc limit 10";
					//log.info("query in unprocessed:"+query);

					totalCtAndTotalAmt="select sum(`"+amtQuailifier+"`) as totalAmt,count(scrIds) as count from `"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where Date("+date+") >= '"+fDate+"' and Date("+date+") <='"+tDate+"' "
							+ " and scrIds not in ("+finalSrcIds+")";
					//log.info("totalCtAndTotalAmt :"+totalCtAndTotalAmt);
				}

				else if(finalSrcIds.isEmpty() && (module.equalsIgnoreCase("un-reconciled") || module.equalsIgnoreCase("not accounted")))
				{
					query="select `"+groupBy+"`,round(sum(`"+amtQuailifier+"`),"+round+") as `"+amtQuailifier+"`,count(scrIds) from `"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where Date("+date+") >= '"+fDate+"' and Date("+date+") <='"+tDate+"' "
							+ " group by `"+groupBy+"` order by `"+amtQuailifier+"` desc limit 10";
					//	log.info("query in unprocessed2:"+query);

					totalCtAndTotalAmt="select sum(`"+amtQuailifier+"`) as totalAmt,count(scrIds) as count from `"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where Date("+date+") >= '"+fDate+"' and Date("+date+") <='"+tDate+"'";
					//	log.info("totalCtAndTotalAmt :"+totalCtAndTotalAmt);
				}

			}
			if(module.equalsIgnoreCase("reconciled") || module.equalsIgnoreCase("pending journals"))
			{
				if(finalSrcIds!=null && !(finalSrcIds.isEmpty())){
					query="select `"+groupBy+"`,round(sum(`"+amtQuailifier+"`),"+round+") as `"+amtQuailifier+"`,count(scrIds) from `"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where Date("+date+") >= '"+fDate+"' and Date("+date+") <='"+tDate+"' "
							+ " and scrIds in ("+finalSrcIds+") group by `"+groupBy+"` order by `"+amtQuailifier+"` desc limit 10";
					//log.info("query :"+query);


					totalCtAndTotalAmt="select sum(`"+amtQuailifier+"`) as totalAmt,count(scrIds) as count from `"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where Date("+date+") >= '"+fDate+"' and Date("+date+") <='"+tDate+"' "
							+ " and scrIds in ("+finalSrcIds+")";
				}
				//log.info("totalCtAndTotalAmt :"+totalCtAndTotalAmt);
			}
			if(module.equalsIgnoreCase("JE Creation"))
			{
				if(finalSrcIds!=null && !(finalSrcIds.isEmpty())){
					query="select `"+groupBy+"`,round(sum(`"+amtQuailifier+"`),"+round+") as `"+amtQuailifier+"`,count(scrIds) from `"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where Date("+date+") >= '"+fDate+"' and Date("+date+") <='"+tDate+"' "
							+ " and scrIds in ("+finalSrcIds+") group by `"+groupBy+"` order by `"+amtQuailifier+"` desc limit 10";
					//log.info("query :"+query);

					totalCtAndTotalAmt="select sum(`"+amtQuailifier+"`) as totalAmt,count(scrIds) as count from `"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where Date("+date+") >= '"+fDate+"' and Date("+date+") <='"+tDate+"' "
							+ " and scrIds in ("+finalSrcIds+")";
				}
				//log.info("totalCtAndTotalAmt :"+totalCtAndTotalAmt);
			}
			LinkedHashMap mapTotalAmtAndcount=new LinkedHashMap();
			//log.info("totalCtAndTotalAmt: "+totalCtAndTotalAmt);
			if(!totalCtAndTotalAmt.isEmpty())
			{
				resultTcAndAmtDv=stmtTcAndAmtDv.executeQuery(totalCtAndTotalAmt);
				ResultSetMetaData rsmd2 = resultTcAndAmtDv.getMetaData();
				int columnCount = rsmd2.getColumnCount();

				while(resultTcAndAmtDv.next())
				{
					mapTotalAmtAndcount.put("totalAmount", resultTcAndAmtDv.getString(1));
					mapTotalAmtAndcount.put("totalCount", resultTcAndAmtDv.getString(2));

				}
			}
			//log.info("mapTotalAmtAndcount :"+mapTotalAmtAndcount);

			if(!query.isEmpty())
			{
				//log.info("query: "+query);
				resultDv=stmtDv.executeQuery(query);
				ResultSetMetaData rsmd2 = resultDv.getMetaData();
				int columnCount = rsmd2.getColumnCount();

				while(resultDv.next())
				{
					LinkedHashMap map2=new LinkedHashMap();
					for (int i = 1; i <= columnCount; i++ ) {
						String name = rsmd2.getColumnName(i); 
						if(i==1 && resultDv.getString(i).toString().isEmpty() || resultDv.getString(i)==null)
							map2.put("name","Blank Value");
						else if (i==1)
							map2.put("name", resultDv.getString(i));
						if(i==2)
						{
							map2.put("amount", Double.valueOf(resultDv.getString(i).toString()));
							Double amountPer=(Double.valueOf(resultDv.getString(i).toString())/Double.valueOf(mapTotalAmtAndcount.get("totalAmount").toString()))*100;
							map2.put("amountPer", Double.valueOf(dform.format(amountPer)));
						}
						if(i==3)
						{
							map2.put("count", Double.valueOf(resultDv.getString(i).toString()));
							Double countPer=(Double.valueOf(resultDv.getString(i).toString())/Double.valueOf(mapTotalAmtAndcount.get("totalCount").toString()))*100;
							map2.put("countPer", Double.valueOf(dform.format(countPer)));
						}
					}
					finalMap.add(map2);
				}
			}
			lhm.put(groupBy, finalMap);
		}
		catch(Exception e)
		{
			log.info("exception in getUnProcessedOrProcessedDataForGroupByV3 :"+e);
		}
		finally
		{
			if(resultDv!=null)
			{
				try {
					resultDv.close();
					checkConnection = false;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					log.info("Exception while resultDv getUnProcessedOrProcessedDataForGroupByV3 :"+e);
				}

			}
			if(resultTcAndAmtDv!=null)
			{
				try {
					resultTcAndAmtDv.close();
					checkConnection = false;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					log.info("Exception while resultTcAndAmtDv getUnProcessedOrProcessedDataForGroupByV3 :"+e);
				}

			}
			if(stmtDv!=null)
			{
				try {
					stmtDv.close();
					checkConnection = false;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					log.info("Exception while stmtDv getUnProcessedOrProcessedDataForGroupByV3 :"+e);
				}

			}
			if(stmtTcAndAmtDv!=null)
			{
				try {
					stmtTcAndAmtDv.close();
					checkConnection = false;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					log.info("Exception while stmtTcAndAmtDv getUnProcessedOrProcessedDataForGroupByV3 :"+e);
				}
			}
			if(conn!=null)
			{
				try {
					conn.close();
					checkConnection = false;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					log.info("Exception while closing getUnProcessedOrProcessedDataForGroupByV3 :"+e);
				}

			}
		}
		log.info("Checking Connection Close in getUnProcessedOrProcessedDataForGroupByV3:"+checkConnection);

		return lhm;

	}
	
	
	
	@PostMapping("/getSummaryInfomartionForExtractionAndTransformationV3")
	@Timed 
	public List<LinkedHashMap> getSummaryInfomartionForExtractionAndTransformationV3(@RequestParam String processId
			,@RequestBody HashMap dates,HttpServletRequest request) throws SQLException, ParseException 
			{
		HashMap map=userJdbcService.getuserInfoFromToken(request);
      	Long tenantId=Long.parseLong(map.get("tenantId").toString());
		Processes processes = processesRepository.findByTenantIdAndIdForDisplay(tenantId, processId);
		Processes process=processesRepository.findOne(processes.getId());
		List<LinkedHashMap> finalProfileMap=new ArrayList<LinkedHashMap>();
		ZonedDateTime fmDate=ZonedDateTime.parse(dates.get("startDate").toString());
		ZonedDateTime toDate=ZonedDateTime.parse(dates.get("endDate").toString());

		java.time.LocalDate fDate=fmDate.toLocalDate();
		java.time.LocalDate tDate=toDate.toLocalDate();
		ApplicationPrograms app=applicationProgramsRepository.findByPrgmNameAndTenantId("DataExtraction",process.getTenantId());
		LinkedHashMap<String,Integer> dtTrOrExtMAp=dashBoardV3Service.dtTrOrExtParamMap(app.getId());
		log.info("dtTrOrExtMAp :"+dtTrOrExtMAp);
		List<BigInteger> profileId=processDetailsRepository.findTypeIdByProcessIdAndTagType(processes.getId(), "sourceProfile");
		for(int i=0;i<profileId.size();i++)
		{
			LinkedHashMap profileMap=new LinkedHashMap();
			SourceProfiles prof=sourceProfilesRepository.findOne(profileId.get(i).longValue());

			/** extraction per profile**/
			//	List<BigInteger> jobDetails=jobDetailsRepository.findByTenantIdAndProgrammIdAndParameterArgument1ForProfie(app.getId(),prof.getId().toString(),fDate+"%",tDate+"%");
			//	log.info("jobDetails size :"+jobDetails);
			//LinkedHashMap extractionPerProfile=dashBoardV2Service.extractionAnalysisforGivenPeriod(jobDetails,fDate, tDate, toDate);

			List<SourceFileInbHistory> extractionPerProfile=sourceFileInbHistoryRepository.findByProfileIdAndFileReceivedDateBetween
					(profileId.get(i),fDate+"%",tDate+"%");



			LinkedHashMap extractedPerProfile=new LinkedHashMap();
			extractedPerProfile.put("count", extractionPerProfile.size());
			//	extractedPerProfile.put("countPer",extractionPerProfile.get("totalExtractedPer"));
			profileMap.put("extracted", extractedPerProfile);
			/*LinkedHashMap ntExtractedPerProfile=new LinkedHashMap();
			ntExtractedPerProfile.put("count", extractionPerProfile.get("totalExtractionFailedCt"));
			ntExtractedPerProfile.put("countPer",extractionPerProfile.get("totalExtractionFailedCtPer"));
			profileMap.put("ntExtracted", ntExtractedPerProfile);*/



			/**transformation per profile**/

			List<Object[]> tasnformationDetailsForProfile=sourceFileInbHistoryRepository.fetchTransfomedCountBetweenGivenDateForProfile(profileId.get(i), fDate.toString(), tDate.toString());
			LinkedHashMap trasnformationForProfile=new LinkedHashMap();
			trasnformationForProfile.put("transfomed", Double.valueOf(tasnformationDetailsForProfile.get(0)[1].toString()));
			trasnformationForProfile.put("transfomedPer", Double.valueOf(tasnformationDetailsForProfile.get(0)[2].toString()));
			profileMap.put("trasnformed", trasnformationForProfile);
			LinkedHashMap trasnformationFailedForProfile=new LinkedHashMap();
			trasnformationFailedForProfile.put("transfomedFailed", Double.valueOf(tasnformationDetailsForProfile.get(0)[4].toString()));
			trasnformationFailedForProfile.put("transfomedFailedPer", Double.valueOf(tasnformationDetailsForProfile.get(0)[3].toString()));
			profileMap.put("trasnformFailed", trasnformationFailedForProfile);



			List<LinkedHashMap> finalTempMap=new ArrayList<LinkedHashMap>();
			List<BigInteger> profileassignmentIds=sourceFileInbHistoryRepository.findDistinctSrcPrfAsmtIdByProfileId(profileId.get(i).longValue());
			log.info("profileassignmentIds :"+profileassignmentIds.size());
			for(BigInteger asignId:profileassignmentIds)
			{
				LinkedHashMap templateMap=new LinkedHashMap();

				SourceProfileFileAssignments tempId=sourceProfileFileAssignmentsRepository.findOne(asignId.longValue());
				log.info("tempId :"+tempId);
				if(tempId!=null)
				{
					FileTemplates template=fileTemplatesRepository.findOne(tempId.getTemplateId());

					/** to fect jobIds for template specific**/
					/*	String param2 = "parameterArguments";
				param2 = param2+dtTrOrExtMAp.get("TemplateName");
				String finalQuery="select id from JobDetails where tenantId="+process.getTenantId() +" and programmId ="+app.getId()+" and parameterArguments"+
						dtTrOrExtMAp.get("SourceProfileName")+" ="+profileId.get(i)+" and (startDate>='"+fDate+"%' and endDate<='"+tDate+"%') and "
						+ "("+param2 +"= " +tempId.getTemplateId()+" or "+param2 +" like '"+tempId.getTemplateId()+",%' or "+ param2 +" like '%,"+tempId.getTemplateId()+"%' or "+param2+" like '%,"+tempId.getTemplateId()+",%' or "+param2+" is null)";
				log.info("finalQuery :"+finalQuery);
				List jobIds=em.createQuery(finalQuery).getResultList();

				log.info("jobIds :"+jobIds.size());*/
					/**extraction per template**/
					//	LinkedHashMap extraction=dashBoardV2Service.extractionAnalysisforGivenPeriod(jobIds,fDate, tDate, toDate);



					List<SourceFileInbHistory> extraction=sourceFileInbHistoryRepository.findByProfileIdAndAssignmentIdFileReceivedDateBetween
							(profileId.get(i),asignId.longValue(),fDate+"%",tDate+"%");

					LinkedHashMap extracted=new LinkedHashMap();
					extracted.put("count", extraction.size());
					//	extracted.put("countPer",extraction.get("totalExtractedPer"));
					templateMap.put("extracted", extracted);
					/*	LinkedHashMap ntExtracted=new LinkedHashMap();
				ntExtracted.put("count", extraction.get("totalExtractionFailedCt"));
				ntExtracted.put("countPer",extraction.get("totalExtractionFailedCtPer"));
				templateMap.put("ntExtracted", ntExtracted);*/
					templateMap.put("tempId", template.getId());
					templateMap.put("tempName", template.getTemplateName());

					/**transformation per template**/
					List<Object[]> tasnformationDetails=sourceFileInbHistoryRepository.fetchTransfomedCountForProfileAndTemplate(profileId.get(i).longValue(), fDate.toString(), tDate.toString(), asignId.longValue());
					LinkedHashMap trasnformation=new LinkedHashMap();
					trasnformation.put("transfomed", Double.valueOf(tasnformationDetails.get(0)[1].toString()));
					trasnformation.put("transfomedPer", Double.valueOf(tasnformationDetails.get(0)[2].toString()));
					templateMap.put("trasnformed", trasnformation);
					LinkedHashMap trasnformationFailed=new LinkedHashMap();
					trasnformationFailed.put("transfomedFailed", Double.valueOf(tasnformationDetails.get(0)[4].toString()));
					trasnformationFailed.put("transfomedFailedPer", Double.valueOf(tasnformationDetails.get(0)[3].toString()));

					templateMap.put("trasnformFailed", trasnformationFailed);
					finalTempMap.add(templateMap);
				}

			}
			profileMap.put("profileId", prof.getId());
			profileMap.put("profileName", prof.getSourceProfileName());
			profileMap.put("templateDetails", finalTempMap);
			finalProfileMap.add(profileMap);
		}
		return finalProfileMap;
			}
	
	
	@PostMapping("/getViolationDetailsV3")
	@Timed 
	public LinkedHashMap getViolationDetailsV3(HttpServletRequest request,@RequestParam(value="processId",required=false) String processId ,@RequestBody HashMap dates,@RequestParam int violation,@RequestParam String type,@RequestParam(value="ruleGroupId",required=false) Long ruleGroupId) 
	{
		log.info("Rest request to getSummaryInfoForReconciliationV2 for a process:"+processId);
		Boolean checkConnection = false;
		LinkedHashMap finalMap=new LinkedHashMap();
		HashMap map1=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map1.get("tenantId").toString());

		Processes processes1 = processesRepository.findByTenantIdAndIdForDisplay(tenantId, processId);
		List<LinkedHashMap> dataMap=new ArrayList<LinkedHashMap>();
		Long ruleGrpId=0l;
		if(processId!=null && ruleGroupId==null)
		{
			Processes processes=processesRepository.findOne(processes1.getId());
			ProcessDetails procesDet=new ProcessDetails();
			if(type.equalsIgnoreCase("reconciliation"))
				procesDet=processDetailsRepository.findByProcessIdAndTagType(processes1.getId(), "reconciliationRuleGroup");
			else
				procesDet=processDetailsRepository.findByProcessIdAndTagType(processes1.getId(), "accountingRuleGroup");
			if(procesDet.getTypeId()!=null)
				ruleGrpId=procesDet.getTypeId();
		}
		else if(processId==null && ruleGroupId!=null)
		{
			ruleGrpId=ruleGroupId;
		}

		RuleGroup ruleGrp=ruleGroupRepository.findOne(ruleGrpId);
		ZonedDateTime fmDate=ZonedDateTime.parse(dates.get("startDate").toString());
		ZonedDateTime toDate=ZonedDateTime.parse(dates.get("endDate").toString());

		java.time.LocalDate fDate=fmDate.toLocalDate();
		java.time.LocalDate tDate=toDate.toLocalDate();

		if(type.equalsIgnoreCase("reconciliation"))
		{	
			//ProcessDetails procesDet=processDetailsRepository.findByProcessIdAndTagType(processId, "reconciliationRuleGroup");

			List<Object[]> reconSummary=appModuleSummaryRepository.fetchRecCountsByGroupIdAndFileDate(ruleGrpId,fDate,tDate);

			Double violationCount=0d;
			Double totalViolationCount=0d;


			for(int i=0;i<reconSummary.size();i++)
			{


				LinkedHashMap map=new LinkedHashMap();

				DataViews dv=dataViewsRepository.findOne(Long.valueOf(reconSummary.get(i)[5].toString()));

				List<BigInteger> finalSrcIdList=new ArrayList<BigInteger>();
				List<BigInteger> reconciliedSrcIds=reconciliationResultRepository.fetchReconciledSourceIds(ruleGrp.getTenantId(), ruleGrpId, dv.getId());	 
				finalSrcIdList.addAll(reconciliedSrcIds);
				List<BigInteger> reconciliedTrgIds=reconciliationResultRepository.fetchReconciledTargetIds(ruleGrp.getTenantId(), ruleGrpId, dv.getId());	 
				finalSrcIdList.addAll(reconciliedTrgIds);


				/*		String dbUrl=env.getProperty("spring.datasource.url");
					String[] parts=dbUrl.split("[\\s@&?$+-]+");
					String host = parts[0].split("/")[2].split(":")[0];
					log.info("host :"+host);
					String schemaName=parts[0].split("/")[3];
					log.info("schemaName :"+schemaName);
					String userName = env.getProperty("spring.datasource.username");
					String password = env.getProperty("spring.datasource.password");
					String jdbcDriver = env.getProperty("spring.datasource.jdbcdriver");
				 */
				Connection conn = null;
				Statement stmtDv = null;
				Statement stmtApprovedAmt = null;

				DataViewsColumns dvColumn=dataViewsColumnsRepository.findByDataViewIdAndQualifier(dv.getId(), "AMOUNT");
				String ammountQualifier="";
				if(dvColumn.getRefDvType()!=null &&dvColumn.getRefDvType().equalsIgnoreCase("File Template"))
				{
					FileTemplateLines ftl=fileTemplateLinesRepository.findOne(Long.valueOf(dvColumn.getRefDvColumn()));
					ammountQualifier=ftl.getColumnAlias();
				}
				else
					ammountQualifier=dvColumn.getColumnName();

				ResultSet resultDv=null;
				ResultSet resultTotalViolationQuery=null;
				try
				{
					DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
					conn = ds.getConnection();
					checkConnection = true;
					log.info("Checking Connection Open in getViolationDetailsV3:"+checkConnection);
					log.info("Connected database successfully...");
					stmtDv = conn.createStatement();
					stmtApprovedAmt = conn.createStatement();



					String query="";

					if(finalSrcIdList!=null && !finalSrcIdList.isEmpty())
					{
						String finalSrcIds=finalSrcIdList.toString().replaceAll("\\[", "").replaceAll("\\]", "");

						query="select DATEDIFF( SYSDATE(), `v`.`fileDate`) as `rule_age`,count(scrIds),sum(`"+ammountQualifier+"`) as `"+ammountQualifier+"` from `"+dv.getDataViewName().toLowerCase().toLowerCase()+"` v where fileDate between '"+fDate+"' and '"+tDate+"' "
								+ " and scrIds not in ("+finalSrcIds+")group by rule_age";
						//log.info("query in if  :"+query);

					}
					else
					{
						query="select DATEDIFF( SYSDATE(), `v`.`fileDate`) as `rule_age`,count(scrIds),sum(`"+ammountQualifier+"`) as `"+ammountQualifier+"` from `"+dv.getDataViewName().toLowerCase().toLowerCase()+"` v where fileDate between '"+fDate+"' and '"+tDate+"' "
								+ "group by rule_age";
						//log.info("query in else  :"+query);
					}


					//	String	TotalcountQuery="select count(scrIds) from `"+dv.getDataViewName().toLowerCase().toLowerCase()+"` v where fileDate between '"+fDate+"' and '"+tDate+"' ";
					//log.info("query in TotalcountQuery  :"+TotalcountQuery);

					//log.info("query :"+query);
					/*resultTotalViolationQuery=stmtDv.executeQuery(TotalcountQuery);
					while(resultTotalViolationQuery.next())
					{
						totalViolationCount=totalViolationCount+Long.valueOf(resultTotalViolationQuery.getString("count(scrIds)").toString());
					}*/
					log.info("totalViolationCount :"+totalViolationCount);
					resultDv=stmtDv.executeQuery(query);
					ResultSetMetaData rsmd2 = resultDv.getMetaData();
					int columnCount = rsmd2.getColumnCount();
					map.put("violationAmount",0);
					map.put("violationCount", 0);
					log.info("violation :"+violation);
					while(resultDv.next())
					{
						totalViolationCount=	totalViolationCount+Double.valueOf(resultDv.getString("count(scrIds)"));
						log.info("resultDv.getString(rule_age) :"+resultDv.getString("rule_age"));
						int ruleAge=Integer.valueOf(resultDv.getString("rule_age").toString());

						// int ruleAge=1;

						log.info("ruleAge :"+ruleAge+" and violation :"+violation);
						if(ruleAge>=violation)
						{
							//log.info("if rule age is greater than violation");
							map.put("violationCount", Long.valueOf(resultDv.getString("count(scrIds)").toString()));
							violationCount=violationCount+Double.valueOf(resultDv.getString("count(scrIds)").toString());
							map.put("violationAmount",dform.format(Double.valueOf(resultDv.getString(ammountQualifier).toString())));
						}
						else
						{
							map.put("violationAmount",0);
							map.put("violationCount", 0);
						}
					}




					dataMap.add(map);
				}
				catch(Exception e)
				{
					log.info("Exception in getViolationDetailsV3 :"+e);
				}
				finally
				{
					if(resultDv!=null)
					{
						try {
							resultDv.close();
							checkConnection = false;
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							log.info("Exception while closing resultDv in getViolationDetailsV3:"+e);
						}

					}
					if(resultTotalViolationQuery!=null)
					{
						try {
							resultTotalViolationQuery.close();
							checkConnection = false;
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							log.info("Exception while closing resultTotalViolationQuery in getViolationDetailsV3:"+e);
						}

					}
					if(stmtDv!=null)
					{
						try {
							stmtDv.close();
							checkConnection = false;
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							log.info("Exception while closing stmtDv in getViolationDetailsV3:"+e);
						}

					}
					if(stmtApprovedAmt!=null)
					{
						try {
							stmtApprovedAmt.close();
							checkConnection = false;
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							log.info("Exception while closing stmtApprovedAmt in getViolationDetailsV3:"+e);
						}

					}
					if(conn!=null)
					{
						try {
							conn.close();
							checkConnection = false;
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							log.info("Exception while closing conn in getViolationDetailsV3:"+e);
						}

					}
				}

			}


			double unReconItemsViolationPer=0l;

			if(totalViolationCount>0)
			{
				//log.info("violationCount :"+violationCount);
				//log.info("totalViolationCount :"+totalViolationCount);
				unReconItemsViolationPer=((double)violationCount/totalViolationCount)*100;
			}


			finalMap.put("unReconItemsViolation", violationCount);
			finalMap.put("unReconItemsViolationPer", Double.valueOf(dform.format(unReconItemsViolationPer)));



		}
		else
		{

			log.info("getSummaryInfoForAccountingV2 of processId:"+processId +"dates"+dates);

			List<BigInteger> distViewsIds=appModuleSummaryRepository.findDistinctViewIdByRuleGroupId(ruleGrpId,fDate,tDate);
			log.info("distViewsIds :"+distViewsIds);
			Double violationCount=0d;
			Double totalViolationCount=0d;

			for(BigInteger viewId:distViewsIds)
			{

				LinkedHashMap map=new LinkedHashMap();
				map.put("viewId", viewId);
				List<Object[]> accountingSummary=appModuleSummaryRepository.fetchActCountsByGroupIdAndViewIdAndFileDate(Long.valueOf(ruleGrpId),viewId.longValue(),fDate,tDate);
				DataViews dv=dataViewsRepository.findOne(viewId.longValue());

				log.info("accountingSummary.size() :"+accountingSummary.size());
				for(int i=0;i<accountingSummary.size();i++)
				{


					String capitalized = WordUtils.capitalizeFully(accountingSummary.get(i)[3].toString());
					log.info("capitalized :"+capitalized);
					map.put("dvCount", Double.valueOf(accountingSummary.get(i)[0].toString()));
					String process=capitalized.substring(0, 1).toLowerCase() + capitalized.substring(1);
					log.info("process :"+process);
					map.put(process.replaceAll("\\s", "")+"Count", Double.valueOf(accountingSummary.get(i)[1].toString()));


					DataViewsColumns dvColumn=dataViewsColumnsRepository.findByDataViewIdAndQualifier(dv.getId(), "AMOUNT");
					String ammountQualifier="";
					if(dvColumn.getRefDvType()!=null && dvColumn.getRefDvType().equalsIgnoreCase("File Template"))
					{
						FileTemplateLines ftl=fileTemplateLinesRepository.findOne(Long.valueOf(dvColumn.getRefDvColumn()));
						ammountQualifier=ftl.getColumnAlias();
					}
					else
						ammountQualifier=dvColumn.getColumnName();


					/*String dbUrl=env.getProperty("spring.datasource.url");
							String[] parts=dbUrl.split("[\\s@&?$+-]+");
							String host = parts[0].split("/")[2].split(":")[0];
							log.info("host :"+host);
							String schemaName=parts[0].split("/")[3];
							log.info("schemaName :"+schemaName);
							String userName = env.getProperty("spring.datasource.username");
							String password = env.getProperty("spring.datasource.password");
							String jdbcDriver = env.getProperty("spring.datasource.jdbcdriver");*/

					Connection conn = null;
					Statement stmtDv = null;
					Statement stmtApp=null;
					ResultSet resultDv=null;
					ResultSet resultTotalViolationQuery=null;
					ResultSet resultApp=null;

					try
					{
						DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
						conn = ds.getConnection();
						checkConnection = true;
						log.info("Checking Connection Open in getViolationDetailsV3:"+checkConnection);
						log.info("Connected database successfully...");
						stmtDv = conn.createStatement();
						stmtApp = conn.createStatement();


						String query="";


						List<BigInteger> finalSrcIdList=new ArrayList<BigInteger>();
						//only accounted
						List<BigInteger> accountedViewIds=accountedSummaryRepository.fetchAccountedRowIdsByRuleGrpIdAndViewId(ruleGrpId, viewId.longValue()) ;
						finalSrcIdList.addAll(accountedViewIds);

						if(finalSrcIdList!=null && !finalSrcIdList.isEmpty())
						{
							String finalSrcIds=finalSrcIdList.toString().replaceAll("\\[", "").replaceAll("\\]", "");

							query="select DATEDIFF( SYSDATE(), `v`.`fileDate`) as `rule_age`,count(scrIds),sum(`"+ammountQualifier+"`) as `"+ammountQualifier+"` from `"+dv.getDataViewName().toLowerCase().toLowerCase()+"` v where fileDate between '"+fDate+"' and '"+tDate+"' "
									+ " and scrIds not in ("+finalSrcIds+")group by rule_age";
							//log.info("in if query :"+query);

						}
						else
						{
							query="select DATEDIFF( SYSDATE(), `v`.`fileDate`) as `rule_age`,count(scrIds),sum(`"+ammountQualifier+"`) as `"+ammountQualifier+"` from `"+dv.getDataViewName().toLowerCase().toLowerCase()+"` v where fileDate between '"+fDate+"' and '"+tDate+"' "
									+ "group by rule_age";
							//	log.info("in if else :"+query);
						}

						//String	TotalcountQuery="select count(scrIds) from `"+dv.getDataViewName().toLowerCase().toLowerCase()+"` v where fileDate between '"+fDate+"' and '"+tDate+"' ";
						//log.info("query in TotalcountQuery  :"+TotalcountQuery);

						//log.info("query :"+query);
						//resultTotalViolationQuery=stmtDv.executeQuery(TotalcountQuery);
						/*while(resultTotalViolationQuery.next())
								{
									totalViolationCount=totalViolationCount+Long.valueOf(resultTotalViolationQuery.getString("count(scrIds)").toString());
								}*/
						//log.info("totalViolationCount :"+totalViolationCount);

						//log.info("query*** :"+query);
						if(finalSrcIdList.size()>0)
						{
							resultDv=stmtDv.executeQuery(query);
							ResultSetMetaData rsmd2 = resultDv.getMetaData();
							int columnCount = rsmd2.getColumnCount();
							map.put("violationAmount",0);
							map.put("violationCount", 0);
							//	log.info("violation :"+violation);

							while(resultDv.next())
							{
								//log.info("resultDv.getString(rule_age) :"+resultDv.getString("rule_age"));
								totalViolationCount=	totalViolationCount+Double.valueOf(resultDv.getString("count(scrIds)"));

								int ruleAge=Integer.valueOf(resultDv.getString("rule_age").toString());

								// int ruleAge=1;
								//	totalViolationCount=totalViolationCount+Long.valueOf(resultDv.getString("count(scrIds)").toString());
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
					catch(Exception e)
					{
						log.info("Exception in getViolationDetailsV3:"+e);
					}
					finally
					{

						if(resultDv!=null)
						{
							try {
								resultDv.close();
								checkConnection = false;
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								log.info("Exception while closing resultDv in getViolationDetailsV3 :"+e);
							}

						}
						if(resultTotalViolationQuery!=null)
						{
							try {
								resultTotalViolationQuery.close();
								checkConnection = false;
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								log.info("Exception while closing resultTotalViolationQuery in getViolationDetailsV3 :"+e);
							}

						}
						if(resultDv!=null)
						{
							try {
								resultDv.close();
								checkConnection = false;
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								log.info("Exception while closing resultDv in getViolationDetailsV3 :"+e);
							}

						}
						if(stmtDv!=null)
						{
							try {
								stmtDv.close();
								checkConnection = false;
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								log.info("Exception while closing stmtDv in getViolationDetailsV3 :"+e);
							}

						}
						if(stmtApp!=null)
						{
							try {
								stmtApp.close();
								checkConnection = false;
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								log.info("Exception while closing stmtApp in getViolationDetailsV3 :"+e);
							}

						}
						if(conn!=null)
						{
							try {
								conn.close();
								checkConnection = false;
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								log.info("Exception while closing conn in getViolationDetailsV3 :"+e);

							}

						}


					}
				}
				double violationCountPer=0;
				if(totalViolationCount>0)
				{
					violationCountPer =((double)violationCount/totalViolationCount)*100;
				}

				finalMap.put("unAccountedItemsViolation",violationCount);
				finalMap.put("unAccountedItemsViolationPer",Double.valueOf(dform.format(violationCountPer)));




			}


		}

		log.info("Checking Connection Close in getViolationDetailsV3:"+checkConnection);

		log.info("******end Time : "+ZonedDateTime.now()+"*******");
		return finalMap;
	}

	/**
	 * Author: Swetha
	 * @param request
	 * @param groupId
	 * @return
	 */
	/*@PostMapping("/getSrcTargetCombinationViewsByRuleGrp")
    @Timed
    public List<HashMap> getSrcTargetCombinationViewsByRuleGrp(HttpServletRequest request,@RequestParam String groupId, @RequestParam String type, @RequestBody HashMap dates){
		log.info("Rest API to getSrcTargetCombinationViewsByRuleGrp for "+ "rule group id: "+ groupId);
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		List<HashMap> finalMap = new ArrayList<HashMap>();

		ZonedDateTime fmDate=ZonedDateTime.parse(dates.get("startDate").toString());
		ZonedDateTime toDate=ZonedDateTime.parse(dates.get("endDate").toString());
		log.info("fmDate :"+fmDate);
		log.info("toDate :"+toDate);
		java.time.LocalDate fDate=fmDate.toLocalDate();
		java.time.LocalDate tDate=toDate.toLocalDate();
		log.info("fDate :"+fDate);
		log.info("tDate :"+tDate);
		RuleGroup ruleGrp = ruleGroupRepository.findByIdForDisplayAndTenantId(groupId, tenantId);
		//RuleGroup ruleGrp = ruleGroupRepository.findOne(groupId);
		if(ruleGrp != null)
		{
			List<HashMap> source = new ArrayList<HashMap>();
			//List<HashMap> target = new ArrayList<HashMap>();
			log.info("Rule Group Name: "+ ruleGrp.getName());

			// Fetching Distinct Source and Target View Ids
			HashMap<String, List<BigInteger>> distinctViewIdMap = reconciliationResultService.getDistinctDVIdsforRuleGrp(ruleGrp.getId(), tenantId);
			List<BigInteger> distSrcIds = distinctViewIdMap.get("sourceViewIds");
			List<BigInteger> distTargetIds = distinctViewIdMap.get("targeViewIds");
			log.info("Tentnt Id: "+tenantId+"Group Id: "+groupId+", Source View Ids: "+distSrcIds+", Target View Ids: "+distTargetIds);
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
						sourceMap.put("viewId", dv.getId());
						sourceMap.put("viewIdDisplay", dv.getIdForDisplay());
						sourceMap.put("viewName", dv.getDataViewDispName());
						sourceMap.put("amountPer", 0);
						sourceMap.put("countPer", 0);
						// Fetching Inner Target Views
						List<BigInteger> innerTrgtViews = rulesRepository.fetchDistinctTargetViewIdsBySourceId(srcViewId.longValue(), tenantId, ruleIds);
						log.info("innerTrgtViews: "+innerTrgtViews);
						if(innerTrgtViews.size()>0)
						{
							for(BigInteger innerTrgtViewId : innerTrgtViews)
							{
								List<HashMap> combMap=new ArrayList<HashMap>();
								HashMap combination=new HashMap();
								log.info("innerTrgtViewId: "+innerTrgtViewId);
								log.info("combination at viewId "+innerTrgtViewId+" is :"+combination);
								HashMap targetMap = new HashMap();
								DataViews innerTargetDv = dataViewsRepository.findOne(innerTrgtViewId.longValue());
								log.info("innerTargetDv: "+innerTargetDv);
								if(innerTargetDv != null)
								{
									targetMap.put("viewId", innerTargetDv.getId());
									targetMap.put("viewIdDisplay", innerTargetDv.getIdForDisplay());
									targetMap.put("viewName", innerTargetDv.getDataViewDispName());
									targetMap.put("amountPer",0 );
									targetMap.put("countPer",0 );

									List<Object[]> ruleIdData=rulesRepository.fetchRulesForSrcNTrgtViews(dv.getId(), innerTargetDv.getId(), ruleGrp.getId());
									log.info("ruleIdData: "+ruleIdData);
									List<String> rIdList=new ArrayList<String>();
									for(int i=0;i<ruleIdData.size();i++){
										rIdList.add(ruleIdData.get(i)[0].toString());
									}
									log.info("rIdList for "+rIdList);
									String rIdListStr = rIdList.stream().collect(Collectors.joining(","));
									sourceMap.put("ruleIdList", rIdListStr);
									sourceMap.put("viewType","Source");
									targetMap.put("ruleIdList", rIdListStr);
									targetMap.put("viewType", "Target");
									if(ruleIdData!=null && !(ruleIdData.isEmpty())){
										Object[] objarr=ruleIdData.get(0);
										Long ruleId=Long.parseLong(objarr[0].toString());
										log.info("ruleId: "+ruleId);
										List<Object[]> analysisDataList=appModuleSummaryRepository.fetchAmountByRuleNViews(ruleGrp.getId(), ruleId,fDate,tDate);
										log.info("analysisDataList sz: "+analysisDataList.size());
										log.info("analysisDataList: "+analysisDataList);
										if(analysisDataList!=null && !(analysisDataList.isEmpty())){

											Object[] srcDataAnalysis=analysisDataList.get(0);
											sourceMap.put("dvCount", Double.valueOf(dform.format(Double.valueOf(srcDataAnalysis[3].toString()))));
											sourceMap.put("dvAmt", Double.valueOf(dform.format(Double.valueOf(srcDataAnalysis[4].toString()))));
											sourceMap.put("approvalCount", Double.valueOf(dform.format(Double.valueOf(srcDataAnalysis[9].toString()))));
											sourceMap.put("unAppCount", Double.valueOf(dform.format(Double.valueOf(srcDataAnalysis[15].toString()))));
											if(srcDataAnalysis[14]!=null)
												sourceMap.put("appCtPer", Double.valueOf(dform.format(Double.valueOf(srcDataAnalysis[14].toString()))));
											else
												sourceMap.put("appCtPer", 0d);
											if(srcDataAnalysis[16]!=null)
												sourceMap.put("unAppCtPer", Double.valueOf(dform.format(Double.valueOf(srcDataAnalysis[16].toString()))));
											else
												sourceMap.put("unAppCtPer", 0d);
											log.info("srcDataAnalysis[2].toString(): "+srcDataAnalysis[2].toString());
											if(type.equalsIgnoreCase("Reconciled")){
												if(srcDataAnalysis[11]!=null)
													sourceMap.put("amountPer", Double.valueOf(dform.format(Double.valueOf(srcDataAnalysis[11].toString()))));
												else sourceMap.put("amountPer", 0);
												if(srcDataAnalysis[10].toString()!=null)
													sourceMap.put("countPer", Double.valueOf(dform.format(Double.valueOf(srcDataAnalysis[10].toString()))));
												else
													sourceMap.put("countPer", 0);
											}
											if(type.equalsIgnoreCase("Un-Reconciled")){
												if(srcDataAnalysis[13]!=null && !(srcDataAnalysis[13].toString().isEmpty()))
													sourceMap.put("amountPer", Double.valueOf(dform.format(Double.valueOf(srcDataAnalysis[12].toString()))));
												else sourceMap.put("amountPer",0);
												if(srcDataAnalysis[12]!=null && !(srcDataAnalysis[12].toString().isEmpty()))
													sourceMap.put("countPer", Double.valueOf(dform.format(Double.valueOf(srcDataAnalysis[11].toString()))));
												else sourceMap.put("countPer", 0);
											}
											if(analysisDataList.size()>1){
												Object[] tgtDataAnalysis=analysisDataList.get(1);
												log.info("tgtDataAnalysis: "+tgtDataAnalysis);
												if(tgtDataAnalysis!=null){
													if(tgtDataAnalysis[3]!=null && !(tgtDataAnalysis[3].toString().isEmpty()))
														targetMap.put("dvCount", Double.valueOf(dform.format(Double.valueOf(tgtDataAnalysis[3].toString()))));
													else targetMap.put("dvCount",0);
													if(tgtDataAnalysis[4]!=null && !(tgtDataAnalysis[4].toString().isEmpty()))
														targetMap.put("dvAmt", Double.valueOf(dform.format(Double.valueOf(tgtDataAnalysis[4].toString()))));
													else targetMap.put("dvAmt",0);
													targetMap.put("approvalCount", Double.valueOf(dform.format(Double.valueOf(srcDataAnalysis[9].toString()))));
													targetMap.put("unAppCount", Double.valueOf(dform.format(Double.valueOf(srcDataAnalysis[15].toString()))));
													if(srcDataAnalysis[14]!=null)
														targetMap.put("appCtPer", Double.valueOf(dform.format(Double.valueOf(srcDataAnalysis[14].toString()))));
													else
														targetMap.put("appCtPer", 0d);
													if(srcDataAnalysis[16]!=null)
														targetMap.put("unAppCtPer", Double.valueOf(dform.format(Double.valueOf(srcDataAnalysis[16].toString()))));
													else
														targetMap.put("unAppCtPer", 0d);

													if(type.equalsIgnoreCase("Reconciled")){
														if(tgtDataAnalysis[11]!=null)
															targetMap.put("amountPer", Double.valueOf(dform.format(Double.valueOf(tgtDataAnalysis[11].toString()))));
														else
															targetMap.put("amountPer", 0);
														if(tgtDataAnalysis[10]!=null)
															targetMap.put("countPer", Double.valueOf(dform.format(Double.valueOf(tgtDataAnalysis[10].toString()))));
														else
															targetMap.put("countPer", 0);
													}
													if(type.equalsIgnoreCase("Un-Reconciled")){
														if(tgtDataAnalysis[13]!=null && !(tgtDataAnalysis[13].toString().isEmpty()))
															targetMap.put("amountPer", Double.valueOf(dform.format(Double.valueOf(tgtDataAnalysis[12].toString()))));
														else targetMap.put("amountPer",0);
														if(tgtDataAnalysis[12]!=null && !(tgtDataAnalysis[12].toString().isEmpty()))
															targetMap.put("countPer", Double.valueOf(dform.format(Double.valueOf(tgtDataAnalysis[11].toString()))));
														else targetMap.put("countPer",0);
													}
												}
											}
										}
									}
									log.info("sourceMap at target view id:"+innerTrgtViewId+" is :"+sourceMap);
									log.info("targetMap at target view id:"+innerTrgtViewId+" is :"+targetMap);
									combMap.add(sourceMap);
									combMap.add(targetMap);
									combination.put("combination", combMap);
									finalMap.add(combination);

								}
							}

						}
					}

				}
			}

		}
		return finalMap;
	}   */
	
	
	
	@PostMapping("/getSrcTargetCombinationViewsByRuleGrp")
    @Timed
    public List<HashMap> getSrcTargetCombinationViewsByRuleGrp(HttpServletRequest request,@RequestParam String groupId, @RequestParam String type, @RequestBody HashMap dates){
		log.info("Rest API to getSrcTargetCombinationViewsByRuleGrp for "+ "rule group id: "+ groupId);
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		List<HashMap> finalMap = new ArrayList<HashMap>();

		ZonedDateTime fmDate=ZonedDateTime.parse(dates.get("startDate").toString());
		ZonedDateTime toDate=ZonedDateTime.parse(dates.get("endDate").toString());
		log.info("fmDate :"+fmDate);
		log.info("toDate :"+toDate);
		java.time.LocalDate fDate=fmDate.toLocalDate();
		java.time.LocalDate tDate=toDate.toLocalDate();
		log.info("fDate :"+fDate);
		log.info("tDate :"+tDate);
		RuleGroup ruleGrp = ruleGroupRepository.findByIdForDisplayAndTenantId(groupId, tenantId);
		//RuleGroup ruleGrp = ruleGroupRepository.findOne(groupId);
		if(ruleGrp != null)
		{
			List<HashMap> source = new ArrayList<HashMap>();
			//List<HashMap> target = new ArrayList<HashMap>();
			log.info("Rule Group Name: "+ ruleGrp.getName());

			// Fetching Distinct Source and Target View Ids
			HashMap<String, List<BigInteger>> distinctViewIdMap = reconciliationResultService.getDistinctDVIdsforRuleGrp(ruleGrp.getId(), tenantId);
			List<BigInteger> distSrcIds = distinctViewIdMap.get("sourceViewIds");
			List<BigInteger> distTargetIds = distinctViewIdMap.get("targeViewIds");
			log.info("Tentnt Id: "+tenantId+"Group Id: "+groupId+", Source View Ids: "+distSrcIds+", Target View Ids: "+distTargetIds);
			List<Long> ruleIds = ruleGroupDetailsRepository.fetchByRuleGroupIdAndTenantId(ruleGrp.getId(), tenantId);
			log.info("ruleIds: "+ruleIds);
			if(distSrcIds.size()>0)
			{
				for(BigInteger srcViewId : distSrcIds)
				{
					log.info("srcViewId: "+srcViewId);

					String dateColumnSrc=dashBoardV4Service.getFileDateOrQualifier(srcViewId.longValue(), tenantId);
					HashMap sourceMap = new HashMap();
					List<HashMap> innerTargetViews = new ArrayList<HashMap>();
					DataViews dv = dataViewsRepository.findOne(srcViewId.longValue());
					if(dv != null)
					{	BigDecimal totalDvCount=appModuleSummaryRepository.fetchTotalDvcountForReconGroupByViewIdAndType(ruleGrp.getId(), fDate, tDate, dv.getId(), "SOURCE");
					BigDecimal totalDvAmt=appModuleSummaryRepository.fetchTotalDvAmtForReconGroupByViewIdAndType(ruleGrp.getId(), fDate, tDate, dv.getId(), "SOURCE");
					List<Object[]> recon1WSummary=appModuleSummaryRepository.fetchReconciledAndUnReconciledAmountsAndCountsByViewIdAndType(ruleGrp.getId(), fDate, tDate, totalDvCount,totalDvAmt, dv.getId(), "SOURCE");


					sourceMap.put("dateColumn", dateColumnSrc);
					sourceMap.put("viewId", dv.getId());
					sourceMap.put("viewIdDisplay", dv.getIdForDisplay());
					sourceMap.put("viewName", dv.getDataViewDispName());
					if(type.equalsIgnoreCase("reconciled"))
					{
						sourceMap.put("amountPer", Double.valueOf(dform.format(Double.valueOf(recon1WSummary.get(0)[6].toString()))));
						sourceMap.put("countPer", Double.valueOf(dform.format(Double.valueOf(recon1WSummary.get(0)[2].toString()))));
					}
					else 
					{
						sourceMap.put("amountPer", Double.valueOf(dform.format(Double.valueOf(recon1WSummary.get(0)[8].toString()))));
						sourceMap.put("countPer", Double.valueOf(dform.format(Double.valueOf(recon1WSummary.get(0)[4].toString()))));	
					}
					// Fetching Inner Target Views
					List<BigInteger> innerTrgtViews = rulesRepository.fetchDistinctTargetViewIdsBySourceId(srcViewId.longValue(), tenantId, ruleIds);
					log.info("innerTrgtViews: "+innerTrgtViews);
					if(innerTrgtViews.size()>0)
					{
						for(BigInteger innerTrgtViewId : innerTrgtViews)
						{
							List<HashMap> combMap=new ArrayList<HashMap>();
							HashMap combination=new HashMap();
							log.info("innerTrgtViewId: "+innerTrgtViewId);
							log.info("combination at viewId "+innerTrgtViewId+" is :"+combination);
							HashMap targetMap = new HashMap();
							DataViews innerTargetDv = dataViewsRepository.findOne(innerTrgtViewId.longValue());
							log.info("innerTargetDv: "+innerTargetDv);
							if(innerTargetDv != null)
							{
								String dateColumnTrg=dashBoardV4Service.getFileDateOrQualifier(innerTrgtViewId.longValue(), tenantId);
								BigDecimal totaltrgDvCount=appModuleSummaryRepository.fetchTotalDvcountForReconGroupByViewIdAndType(ruleGrp.getId(), fDate, tDate, innerTargetDv.getId(), "TARGET");
								BigDecimal totaltrgDvAmt=appModuleSummaryRepository.fetchTotalDvAmtForReconGroupByViewIdAndType(ruleGrp.getId(), fDate, tDate, innerTargetDv.getId(), "TARGET");
								List<Object[]> reconytrg1WSummary=appModuleSummaryRepository.fetchReconciledAndUnReconciledAmountsAndCountsByViewIdAndType(ruleGrp.getId(), fDate, tDate,totaltrgDvCount,totaltrgDvAmt, innerTargetDv.getId(), "TARGET");
								targetMap.put("dateColumn", dateColumnTrg);
								targetMap.put("viewId", innerTargetDv.getId());
								targetMap.put("viewIdDisplay", innerTargetDv.getIdForDisplay());
								targetMap.put("viewName", innerTargetDv.getDataViewDispName());
								if(type.equalsIgnoreCase("reconciled"))
								{
									targetMap.put("amountPer",Double.valueOf(dform.format(Double.valueOf(reconytrg1WSummary.get(0)[6].toString()))));
									targetMap.put("countPer", Double.valueOf(dform.format(Double.valueOf(reconytrg1WSummary.get(0)[2].toString()))));
								}
								else
								{
									targetMap.put("amountPer",Double.valueOf(dform.format(Double.valueOf(reconytrg1WSummary.get(0)[8].toString()))));
									targetMap.put("countPer", Double.valueOf(dform.format(Double.valueOf(reconytrg1WSummary.get(0)[4].toString()))));
								}

								List<Object[]> ruleIdData=rulesRepository.fetchRulesForSrcNTrgtViews(dv.getId(), innerTargetDv.getId(), ruleGrp.getId());
								log.info("ruleIdData: "+ruleIdData);
								List<String> rIdList=new ArrayList<String>();
								for(int i=0;i<ruleIdData.size();i++){
									rIdList.add(ruleIdData.get(i)[0].toString());
								}
								log.info("rIdList for "+rIdList);
								String rIdListStr = rIdList.stream().collect(Collectors.joining(","));
								sourceMap.put("ruleIdList", rIdListStr);
								sourceMap.put("viewType","Source");
								targetMap.put("ruleIdList", rIdListStr);
								targetMap.put("viewType", "Target");
								/*	if(ruleIdData!=null && !(ruleIdData.isEmpty())){
										Object[] objarr=ruleIdData.get(0);
										Long ruleId=Long.parseLong(objarr[0].toString());
										log.info("ruleId: "+ruleId);
										List<Object[]> analysisDataList=appModuleSummaryRepository.fetchAmountByRuleNViews(ruleGrp.getId(), ruleId,fDate,tDate);
										log.info("analysisDataList sz: "+analysisDataList.size());
										log.info("analysisDataList: "+analysisDataList);
										if(analysisDataList!=null && !(analysisDataList.isEmpty())){

											Object[] srcDataAnalysis=analysisDataList.get(0);
											sourceMap.put("dvCount", Double.valueOf(dform.format(Double.valueOf(srcDataAnalysis[3].toString()))));
											sourceMap.put("dvAmt", Double.valueOf(dform.format(Double.valueOf(srcDataAnalysis[4].toString()))));
											sourceMap.put("approvalCount", Double.valueOf(dform.format(Double.valueOf(srcDataAnalysis[9].toString()))));
											sourceMap.put("unAppCount", Double.valueOf(dform.format(Double.valueOf(srcDataAnalysis[15].toString()))));
											if(srcDataAnalysis[14]!=null)
												sourceMap.put("appCtPer", Double.valueOf(dform.format(Double.valueOf(srcDataAnalysis[14].toString()))));
											else
												sourceMap.put("appCtPer", 0d);
											if(srcDataAnalysis[16]!=null)
												sourceMap.put("unAppCtPer", Double.valueOf(dform.format(Double.valueOf(srcDataAnalysis[16].toString()))));
											else
												sourceMap.put("unAppCtPer", 0d);
											log.info("srcDataAnalysis[2].toString(): "+srcDataAnalysis[2].toString());
											if(type.equalsIgnoreCase("Reconciled")){
												if(srcDataAnalysis[11]!=null)
													sourceMap.put("amountPer", Double.valueOf(dform.format(Double.valueOf(srcDataAnalysis[11].toString()))));
												else sourceMap.put("amountPer", 0);
												if(srcDataAnalysis[10].toString()!=null)
													sourceMap.put("countPer", Double.valueOf(dform.format(Double.valueOf(srcDataAnalysis[10].toString()))));
												else
													sourceMap.put("countPer", 0);
											}
											if(type.equalsIgnoreCase("Un-Reconciled")){
												if(srcDataAnalysis[13]!=null && !(srcDataAnalysis[13].toString().isEmpty()))
													sourceMap.put("amountPer", Double.valueOf(dform.format(Double.valueOf(srcDataAnalysis[12].toString()))));
												else sourceMap.put("amountPer",0);
												if(srcDataAnalysis[12]!=null && !(srcDataAnalysis[12].toString().isEmpty()))
													sourceMap.put("countPer", Double.valueOf(dform.format(Double.valueOf(srcDataAnalysis[11].toString()))));
												else sourceMap.put("countPer", 0);
											}
											if(analysisDataList.size()>1){
												Object[] tgtDataAnalysis=analysisDataList.get(1);
												log.info("tgtDataAnalysis: "+tgtDataAnalysis);
												if(tgtDataAnalysis!=null){
													if(tgtDataAnalysis[3]!=null && !(tgtDataAnalysis[3].toString().isEmpty()))
														targetMap.put("dvCount", Double.valueOf(dform.format(Double.valueOf(tgtDataAnalysis[3].toString()))));
													else targetMap.put("dvCount",0);
													if(tgtDataAnalysis[4]!=null && !(tgtDataAnalysis[4].toString().isEmpty()))
														targetMap.put("dvAmt", Double.valueOf(dform.format(Double.valueOf(tgtDataAnalysis[4].toString()))));
													else targetMap.put("dvAmt",0);
													targetMap.put("approvalCount", Double.valueOf(dform.format(Double.valueOf(srcDataAnalysis[9].toString()))));
													targetMap.put("unAppCount", Double.valueOf(dform.format(Double.valueOf(srcDataAnalysis[15].toString()))));
													if(srcDataAnalysis[14]!=null)
														targetMap.put("appCtPer", Double.valueOf(dform.format(Double.valueOf(srcDataAnalysis[14].toString()))));
													else
														targetMap.put("appCtPer", 0d);
													if(srcDataAnalysis[16]!=null)
														targetMap.put("unAppCtPer", Double.valueOf(dform.format(Double.valueOf(srcDataAnalysis[16].toString()))));
													else
														targetMap.put("unAppCtPer", 0d);

													if(type.equalsIgnoreCase("Reconciled")){
														if(tgtDataAnalysis[11]!=null)
															targetMap.put("amountPer", Double.valueOf(dform.format(Double.valueOf(tgtDataAnalysis[11].toString()))));
														else
															targetMap.put("amountPer", 0);
														if(tgtDataAnalysis[10]!=null)
															targetMap.put("countPer", Double.valueOf(dform.format(Double.valueOf(tgtDataAnalysis[10].toString()))));
														else
															targetMap.put("countPer", 0);
													}
													if(type.equalsIgnoreCase("Un-Reconciled")){
														if(tgtDataAnalysis[13]!=null && !(tgtDataAnalysis[13].toString().isEmpty()))
															targetMap.put("amountPer", Double.valueOf(dform.format(Double.valueOf(tgtDataAnalysis[12].toString()))));
														else targetMap.put("amountPer",0);
														if(tgtDataAnalysis[12]!=null && !(tgtDataAnalysis[12].toString().isEmpty()))
															targetMap.put("countPer", Double.valueOf(dform.format(Double.valueOf(tgtDataAnalysis[11].toString()))));
														else targetMap.put("countPer",0);
													}
												}
											}
										}
									}*/
								log.info("sourceMap at target view id:"+innerTrgtViewId+" is :"+sourceMap);
								log.info("targetMap at target view id:"+innerTrgtViewId+" is :"+targetMap);
								combMap.add(sourceMap);
								combMap.add(targetMap);
								combination.put("combination", combMap);
								finalMap.add(combination);

							}
						}

					}
					}

				}
			}

		}
		return finalMap;
	}   
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Author: Swetha
	 * @param request
	 * @param groupId
	 * @param type
	 * @param srcViewId
	 * @param tgtViewId
	 * @param dates
	 * @return
	 */
	@PostMapping("/rulewiseSrcTargetCombinationReconData")
    @Timed
    public List<HashMap> rulewiseSrcTargetCombinationReconData(HttpServletRequest request,@RequestParam String groupId, @RequestParam String type,
    		@RequestParam Long srcViewId, @RequestParam Long tgtViewId,  @RequestBody HashMap dates){
    	log.info("Rest API for rulewiseSrcTargetCombinationReconData for "+ "rule group id: "+ groupId+" type: "+type);
    	log.info("type: "+type+" srcViewId: "+srcViewId+" tgtViewId: "+tgtViewId+" and dates: "+dates);
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	
    	List<HashMap> finData=new ArrayList<HashMap>();
    	
    	ZonedDateTime fmDate=ZonedDateTime.parse(dates.get("startDate").toString());
		ZonedDateTime toDate=ZonedDateTime.parse(dates.get("endDate").toString());
		log.info("fmDate :"+fmDate);
		log.info("toDate :"+toDate);
		java.time.LocalDate fDate=fmDate.toLocalDate();
		java.time.LocalDate tDate=toDate.toLocalDate();
		log.info("fDate :"+fDate);
		log.info("tDate :"+tDate);
		
		List<Object[]> srcDataList;
		List<Object[]> tgtDataList;
		RuleGroup ruleGrp = ruleGroupRepository.findByIdForDisplayAndTenantId(groupId, tenantId);
		if(ruleGrp!=null)
		{
			List<Object[]> ruleIdData=rulesRepository.fetchRulesForSrcNTrgtViews(srcViewId, tgtViewId, ruleGrp.getId());
			log.info("ruleIdData: "+ruleIdData);
			List<Long> rIdList=new ArrayList<Long>();
			for(int i=0;i<ruleIdData.size();i++){
				rIdList.add(Long.parseLong(ruleIdData.get(i)[0].toString()));
			}
			log.info("rIdList for "+rIdList);
			rIdList.add(0L);
		
			/*List<Long> rIdList=new ArrayList<Long>();
			rIdList.add(ruleId);*/
			
			for(int k=0;k<rIdList.size();k++){
				Long rId=rIdList.get(k);
				HashMap dataMap=new HashMap();
				log.info("procesing RuleId: "+rId);
				DataViews dvdata=dataViewsRepository.findOne(srcViewId);
				DataViews dvdata2=dataViewsRepository.findOne(tgtViewId);
				dataMap.put("tgtDvName",dvdata2.getDataViewDispName() );
				dataMap.put("srcDvName",dvdata.getDataViewDispName() );
				dataMap.put("srcDvId",srcViewId);
				dataMap.put("tgtDvId",tgtViewId);
				dataMap.put(dvdata.getDataViewDispName(), dvdata2.getDataViewDispName());
				dataMap.put("ruleId", rId);
				
				if(rId>0){
				Rules rulesData=rulesRepository.findOne(rId);
				dataMap.put("ruleName", rulesData.getRuleCode());
				}
				else{
					dataMap.put("ruleName", "Manual");
				}
				srcDataList=appModuleSummaryRepository.fetchRulewiseSrcTgtComboReconInfo(ruleGrp.getId(), srcViewId, "source",rId, fDate, tDate);
				log.info("srcDataList: "+srcDataList);
				if(type.equalsIgnoreCase("Reconciled")){
				if(srcDataList!=null && !(srcDataList.isEmpty())){
					Object[] srcData=srcDataList.get(0);
					if(srcData[8]!=null && !(srcData[8].toString().isEmpty()))
						dataMap.put("srcCount",srcData[8] );
					else dataMap.put("srcCount",0);
					if(srcData[9]!=null && !(srcData[9].toString().isEmpty()))
						dataMap.put("srcAmount",srcData[9] );
					else dataMap.put("srcAmount",srcData[9] );
					if(srcData[10]!=null && !(srcData[10].toString().isEmpty()))	
						dataMap.put("srcCountPer",srcData[10] );
					else dataMap.put("srcCountPer",0);
					if(srcData[11]!=null && !(srcData[11].toString().isEmpty()))
						dataMap.put("srcAmountPer",srcData[11] );
					else dataMap.put("srcAmountPer",0);
				}
				else{
					dataMap.put("srcCount",0);
					dataMap.put("srcAmount",0 );
					dataMap.put("srcCountPer",0);
					dataMap.put("srcAmountPer",0);
					
				}
				}
				else{
					if(srcDataList!=null && !(srcDataList.isEmpty())){
						Object[] srcData=srcDataList.get(0);
					if(srcData[10]!=null && !(srcData[10].toString().isEmpty()))
						dataMap.put("srcCountPer",srcData[12] );
					else dataMap.put("srcCountPer",0); 
					if(srcData[11]!=null && !(srcData[11].toString().isEmpty()))
						dataMap.put("srcAmountPer",srcData[13] );
					dataMap.put("srcAmountPer",0);
					}
					else{
						dataMap.put("srcCount",0);
						dataMap.put("srcAmount",0 );
						dataMap.put("srcCountPer",0);
						dataMap.put("srcAmountPer",0);
						
					}
				}
				
				tgtDataList=appModuleSummaryRepository.fetchRulewiseSrcTgtComboReconInfo(ruleGrp.getId(), tgtViewId, "target",rId, fDate, tDate);
				log.info("tgtData: "+tgtDataList);
				if(type.equalsIgnoreCase("Reconciled")){
				if(tgtDataList!=null && !(tgtDataList.isEmpty())){
					Object[] tgtData=tgtDataList.get(0);
					if(tgtData[8]!=null && !(tgtData[8].toString().isEmpty()))
						dataMap.put("tgtCount",tgtData[8] );
					else dataMap.put("tgtCount",0 );
					if(tgtData[9]!=null && !(tgtData[9].toString().isEmpty()))
						dataMap.put("tgtAmount",tgtData[9]);
					else dataMap.put("tgtAmount",0);
					if(tgtData[10]!=null && !(tgtData[10].toString().isEmpty()))
						dataMap.put("tgtCountPer",tgtData[10] );
					else dataMap.put("tgtCountPer",0 );
					if(tgtData[11]!=null && !(tgtData[11].toString().isEmpty()))
						dataMap.put("tgtAmountPer",tgtData[11] );
					else dataMap.put("tgtAmountPer",0 );
				}
				else{
					dataMap.put("tgtCount",0);
					dataMap.put("tgtAmount",0 );
					dataMap.put("tgtCountPer",0);
					dataMap.put("tgtAmountPer",0);
					
				}
				}
				else{
					if(tgtDataList!=null && !(tgtDataList.isEmpty())){
						Object[] tgtData=tgtDataList.get(0);
					if(tgtData[10]!=null && !(tgtData[10].toString().isEmpty()))
						dataMap.put("tgtCountPer",tgtData[12] );
					else dataMap.put("tgtCountPer",0);
					if(tgtData[11]!=null && !(tgtData[11].toString().isEmpty()))
						dataMap.put("tgtAmountPer",tgtData[13] );
					else dataMap.put("tgtAmountPer",0);
					}
					else{
						dataMap.put("tgtCount",0);
						dataMap.put("tgtAmount",0 );
						dataMap.put("tgtCountPer",0);
						dataMap.put("tgtAmountPer",0);
						
					}
				}
				
				finData.add(dataMap);
			}
	}
		return finData;
	}
	
	
	
	/**
	 * API to fetch detail information of trans failed records for profile and template specific
	 */
	@PostMapping("/detailInfoForTransFailedRec")
    @Timed
	public List<LinkedHashMap> detailInfoForTransFailedRec(HttpServletRequest request,@RequestParam String processId,@RequestBody HashMap dates)
	{
		ZonedDateTime fmDate=ZonedDateTime.parse(dates.get("startDate").toString());
		ZonedDateTime toDate=ZonedDateTime.parse(dates.get("endDate").toString());
		log.info("fmDate :"+fmDate);
		log.info("toDate :"+toDate);
		java.time.LocalDate fDate=fmDate.toLocalDate();
		java.time.LocalDate tDate=toDate.toLocalDate();
		List<LinkedHashMap> finalMap=new ArrayList<LinkedHashMap>();
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
      	Long tenantId=Long.parseLong(map.get("tenantId").toString());
		 Processes processes = processesRepository.findByTenantIdAndIdForDisplay(tenantId, processId);
		List<BigInteger> profileId=processDetailsRepository.findTypeIdByProcessIdAndTagType(processes.getId(), "sourceProfile");

		for(BigInteger profId:profileId)
		{
			LinkedHashMap profileMap=new LinkedHashMap();
			profileMap.put("profileId", profId);
			SourceProfiles profileName=sourceProfilesRepository.findOne(profId.longValue());
			profileMap.put("profileName", profileName.getSourceProfileName());
			List<BigInteger> templateIds=sourceProfileFileAssignmentsRepository.fetchTempIdsBySrcProfile(profId.longValue());
			List<LinkedHashMap> templatesMapList=new ArrayList<LinkedHashMap>();
			for(BigInteger temId:templateIds)
			{
				LinkedHashMap tempMap=new LinkedHashMap();
				FileTemplates template=fileTemplatesRepository.findOne(temId.longValue());
				tempMap.put("templateName", template.getTemplateName());
				List<DataStaging> dataStatingTotal=dataStagingRepository.findByProfileIdAndTemplateIdAndRecordStatusAndFileDateBetween(profId.longValue(),temId.longValue(),"FAIL",fDate+"%",tDate+"%");
				tempMap.put("failedRecCount", dataStatingTotal.size());
				if(dataStatingTotal.size()>0)
				templatesMapList.add(tempMap);
			}
			profileMap.put("templatesList", templatesMapList);
			finalMap.add(profileMap);
		}
		return finalMap;

	}
	
	
	
	
	
	
	
	
	
	
	

	
}
