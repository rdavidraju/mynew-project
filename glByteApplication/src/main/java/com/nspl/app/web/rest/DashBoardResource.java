package com.nspl.app.web.rest;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.xmlbeans.impl.jam.internal.elements.SourcePositionImpl;
import org.joda.time.LocalDate;
import org.joda.time.Weeks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.domain.AccountedSummary;
import com.nspl.app.domain.ApplicationPrograms;
import com.nspl.app.domain.DataViews;
import com.nspl.app.domain.FileTemplates;
import com.nspl.app.domain.JobDetails;
import com.nspl.app.domain.LookUpCode;
import com.nspl.app.domain.Notifications;
import com.nspl.app.domain.ProcessDetails;
import com.nspl.app.domain.ReconciliationResult;
import com.nspl.app.domain.SchedulerDetails;
import com.nspl.app.domain.SourceFileInbHistory;
import com.nspl.app.domain.SourceProfileFileAssignments;
import com.nspl.app.domain.SourceProfiles;
import com.nspl.app.repository.AccountedSummaryRepository;
import com.nspl.app.repository.ApplicationProgramsRepository;
import com.nspl.app.repository.DataMasterRepository;
import com.nspl.app.repository.DataViewsRepository;
import com.nspl.app.repository.FileTemplatesRepository;
import com.nspl.app.repository.JobDetailsRepository;
import com.nspl.app.repository.LookUpCodeRepository;
import com.nspl.app.repository.NotificationsRepository;
import com.nspl.app.repository.ProcessDetailsRepository;
import com.nspl.app.repository.ReconciliationResultRepository;
import com.nspl.app.repository.RuleGroupDetailsRepository;
import com.nspl.app.repository.RulesRepository;
import com.nspl.app.repository.SchedulerDetailsRepository;
import com.nspl.app.repository.SourceFileInbHistoryRepository;
import com.nspl.app.repository.SourceProfileFileAssignmentsRepository;
import com.nspl.app.repository.SourceProfilesRepository;
import com.nspl.app.service.PropertiesUtilService;
import com.nspl.app.service.UserJdbcService;


@RestController
@RequestMapping("/api")
public class DashBoardResource {
	
	 private final Logger log = LoggerFactory.getLogger(SourceFileInbHistoryResource.class);

	 
	  @Inject
	    SourceProfileFileAssignmentsRepository sourceProfileFileAssignmentsRepository;
	    
	    @Inject
	    SourceProfilesRepository sourceProfilesRepository;
	    
	    @Inject
	    FileTemplatesRepository fileTemplatesRepository;
	    
	    @Autowired
	   	Environment env;
	    
	    @Inject
	    RuleGroupDetailsRepository ruleGroupDetailsRepository;
	    
	    @Inject
	    RulesRepository rulesRepository;
	    
	    @Inject
	    DataViewsRepository dataViewsRepository;
	    
	    @Inject
	    ReconciliationResultRepository reconciliationResultRepository;
	    
	    @Inject
	    AccountedSummaryRepository accountedSummaryRepository;
	    
	    @Inject
		PropertiesUtilService propertiesUtilService;
	    
	    @Inject
	    SourceFileInbHistoryRepository sourceFileInbHistoryRepository;
	    
	    @Inject
	    NotificationsRepository notificationsRepository;
	    
	    @Inject
	    LookUpCodeRepository lookUpCodeRepository;
	    
	    @Inject
	    DataMasterRepository dataMasterRepository;
	    
	    @Inject
	    JobDetailsRepository jobDetailsRepository;
	    @Inject
	    ApplicationProgramsRepository applicationProgramsRepository;
	    
	    @Inject
	    SchedulerDetailsRepository schedulerDetailsRepository;
	    
	    @Inject
	    ProcessDetailsRepository processDetailsRepository;
	    
	    @Inject
	    UserJdbcService userJdbcService;
	
	 
    /**
     * author :ravali
     * desc : APi to get count for extraction and transformation of template and reconciled and accounted
     * @param process
     * @param profileId
     * @param tenantId
     * @param days
     * @return
     * @throws SQLException 
     */
	    @PostMapping("/getCountsForDashBoard")
	    @Timed 
	    public LinkedHashMap getExtractedCount(HttpServletRequest request,@RequestParam Long processId ,@RequestBody HashMap dates) throws SQLException
	    {
	    	HashMap map=userJdbcService.getuserInfoFromToken(request);
	      	Long tenantId=Long.parseLong(map.get("tenantId").toString());
	    	log.info("Rest Request to get count of templates with respect to profiles for data extraction and transformation");
	    
	    	 ZonedDateTime startDate=ZonedDateTime.parse(dates.get("startDate").toString());
	     	  ZonedDateTime endDate=ZonedDateTime.parse(dates.get("endDate").toString());
	     	  log.info("startDate :"+startDate);
	     	  log.info("endDate :"+endDate);
	    	LinkedHashMap finalMap=new LinkedHashMap();
	    	List<LinkedHashMap> extMap=new ArrayList<LinkedHashMap>();
	    	List<LinkedHashMap> trMap=new ArrayList<LinkedHashMap>();
	    	Properties props =  propertiesUtilService.getPropertiesFromClasspath("File.properties");
	    	/*String profiles=props.getProperty("profiles");
	    	log.info("profiles :"+profiles);
	    	Pattern pattern = Pattern.compile("\\d+");
	    	Matcher matcher = pattern.matcher(profiles);

	    //	List<Long> profileId = new ArrayList<Long>();

	    	/* while (matcher.find()) {
	    	System.out.println(matcher.group());
	    	profileId.add(Long.parseLong(matcher.group())); // Add the value to the list
	    	}
	    	log.info("profileId Long:"+profileId);*/
	    	
	    	List<BigInteger> profileId=processDetailsRepository.findTypeIdByProcessIdAndTagType(processId, "sourceProfile");
	    	log.info("profileId :"+profileId);
	    	for(int i=0;i<profileId.size();i++)
	    	{
	    		SourceProfiles profile=sourceProfilesRepository.findOne(profileId.get(i).longValue());
	    		LinkedHashMap profileMap=new LinkedHashMap();
	    		profileMap.put("sourceProfileName", profile.getSourceProfileName());
	    		int count=0;
	    		List<LinkedHashMap> tempMapList=new ArrayList<LinkedHashMap>();
	    		List<SourceProfileFileAssignments> spfaList=sourceProfileFileAssignmentsRepository.findBySourceProfileId(profileId.get(i).longValue());
	    		log.info("spfaList size:"+spfaList.size());
	    		int tempExtCount=0;
	    		for(SourceProfileFileAssignments spfa:spfaList)
	    		{
	    			log.info("spfa :"+spfa.getId());
	    			FileTemplates fileTemp=fileTemplatesRepository.findOne(spfa.getTemplateId());
	    			LinkedHashMap tempMap=new LinkedHashMap();
	    			tempMap.put("templateName", fileTemp.getTemplateName());
	    			
	    		/*	LinkedHashMap temextMap=getExtractionPerProfile(dates, tenantId, profileId.get(i).longValue(), fileTemp.getId());
	    			log.info("temextMap :"+temextMap);*/
	    		
	    			
	    			List<SourceFileInbHistory> sourceProfileExtractedList=sourceFileInbHistoryRepository.findByProfileIdAndSrcPrfFileAsmtIdAndTenantIdAndFileReceivedDateBetween(profileId.get(i).longValue(),spfa.getId(),tenantId,startDate,endDate);
	    			log.info("sourceProfileExtractedList :"+sourceProfileExtractedList.size());
	    			tempMap.put("count ", sourceProfileExtractedList.size());
	    			count=count+sourceProfileExtractedList.size();
	    			tempMapList.add(tempMap);
	    		}
	    		profileMap.put("extractionCount", count);
	    		profileMap.put("templates", tempMapList);
	    		extMap.add(profileMap);
	    	}
	    	finalMap.put("dataExtraction", extMap);

	    	//transformation

	    	for(int i=0;i<profileId.size();i++)
	    	{
	    		SourceProfiles profile=sourceProfilesRepository.findOne(profileId.get(i).longValue());
	    		LinkedHashMap profileMap=new LinkedHashMap();
	    		profileMap.put("sourceProfileName", profile.getSourceProfileName());
	    		int count=0;
	    		List<LinkedHashMap> tempMapList=new ArrayList<LinkedHashMap>();
	    		List<SourceProfileFileAssignments> spfaList=sourceProfileFileAssignmentsRepository.findBySourceProfileId(profileId.get(i).longValue());
	    		log.info("spfaList size:"+spfaList.size());
	    		for(SourceProfileFileAssignments spfa:spfaList)
	    		{
	    			log.info("spfa :"+spfa.getId());
	    			FileTemplates fileTemp=fileTemplatesRepository.findOne(spfa.getTemplateId());
	    			LinkedHashMap tempMap=new LinkedHashMap();
	    			tempMap.put("templateName", fileTemp.getTemplateName());
	    			List<SourceFileInbHistory> sourceProfileExtractedList=sourceFileInbHistoryRepository.findByProfileIdAndSrcPrfFileAsmtIdAndTenantIdAndfileTransformedDateBetween(profileId.get(i).longValue(),spfa.getId(),tenantId,startDate,endDate);
	    			log.info("sourceProfileExtractedList :"+sourceProfileExtractedList.size());
	    			tempMap.put("count ", sourceProfileExtractedList.size());
	    			count=count+sourceProfileExtractedList.size();
	    			tempMapList.add(tempMap);
	    		}
	    		profileMap.put("transformationCount", count);
	    		profileMap.put("templates", tempMapList);
	    		trMap.add(profileMap);
	    	}
	    	finalMap.put("dataTransformation", trMap);

	    	//Reconcilied and unreconciled counts

	    	log.info("to get count for reconciled data");
	    	//1.List<rules> get rules tagged to rule grp id
	    	//2.get distinct (src and target) data views of that List<Rules>
	    	//3.Query Dv get srcId -->total count
	    	//4.query reconciliation result where orginal rowIds in (srcId) || target rowIds in(srcId)

	    	/*List<LinkedHashMap> recltFinalMap=new ArrayList<LinkedHashMap>();


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
	    	Statement stmtRec = null;
	    	Statement stmtAct = null;


	    	conn = DriverManager.getConnection(dbUrl, userName, password);
	    	log.info("Connected database successfully...");
	    	stmtRec = conn.createStatement();
	    	stmtAct = conn.createStatement();
	    	ResultSet resultRec=null;
	    	ResultSet resultAct=null;

	    	String recRuleGroupId=props.getProperty("reconciliationRuleGroup");
	    	log.info("recRuleGroup :"+recRuleGroupId);
	    	
	    	
	    	
	    	List<Long> recRuleIds= ruleGroupDetailsRepository.fetchByRuleGroupIdAndTenantId(Long.valueOf(recRuleGroupId),tenantId);
	    	log.info("recRuleIds :"+recRuleIds);
	    	if(recRuleIds.size()!=0)
	    	{
	    	List<BigInteger> recRulesSrcDv=rulesRepository.fetchDistictSrcViewIdsByRuleId( recRuleIds);
	    	log.info("rulesSrcDv :"+recRulesSrcDv);
	    	List<BigInteger> recRulesTrgDv=rulesRepository.fetchDistictTargetViewIdsByRuleId( recRuleIds);
	    	log.info("rulesTrgDv :"+recRulesTrgDv);
	    	//adding trgDav to srcDvList

	    	for(int i=0;i<recRulesTrgDv.size();i++)
	    	{
	    		if(!recRulesSrcDv.contains(recRulesTrgDv.get(i)))
	    		{
	    			recRulesSrcDv.add(recRulesTrgDv.get(i));
	    		}

	    	}
	    //	log.info("finalDvList :"+recRulesSrcDv);

	    	for (int i=0;i<recRulesSrcDv.size();i++)
	    	{
	    		if(recRulesSrcDv.get(i)!=null)
	    		{
	    			LinkedHashMap dvMap=new LinkedHashMap();
	    			DataViews dv=dataViewsRepository.findOne(recRulesSrcDv.get(i).longValue());
	    			dvMap.put("dvId", dv.getId());
	    			dvMap.put("dataViewName", dv.getDataViewName());
	    			String query="select scrIds from "+schemaName+"."+dv.getDataViewName().toLowerCase()+" where fileDate between '"+startDate+"' and '"+endDate+"'";
	    			log.info("query :"+query);
	    			resultRec=stmtRec.executeQuery(query);
	    			List<Long> srcIds=new ArrayList<Long>();
	    			while(resultRec.next())
	    			{
	    				srcIds.add(Long.valueOf(resultRec.getString("scrIds")));
	    			}
	    			log.info("srcIds.size :"+srcIds.size());
	    			dvMap.put("actualCount", srcIds.size());
	    			if(srcIds.size()!=0)
	    			{
	    			Integer reconciliationCount=reconciliationResultRepository.fetchCountReconciledByViewId(tenantId,Long.valueOf(recRuleGroupId), dv.getId(), srcIds);
	    			log.info("reconciliationCount :"+reconciliationCount);
	    			dvMap.put("reconciledCount", reconciliationCount);
	    			int percent=(reconciliationCount/srcIds.size())*100;
	    			log.info("percent :"+percent);

	    			float percentage=((float) reconciliationCount/(float)srcIds.size())*100;
	    			log.info("percentage :"+percentage);
	    			dvMap.put("reconciledPercent",percentage);
	    			
	    			if(reconciliationCount!=0)
	    			{
	    			int unRecCount = srcIds.size() - reconciliationCount;
	    			dvMap.put("unReconciledCount",unRecCount);
	    			float unRecPercent=100-percentage;
	    			log.info("unActPercent :"+unRecPercent);
	    			dvMap.put("unReconciledPercent",unRecPercent);
	    			}
	    			else
	    				dvMap.put("unReconciledPercent",0);
	    			
	    		}
	    			recltFinalMap.add(dvMap);
	    			

	    		}
	    	}
	    }
	    	finalMap.put("reconciliation", recltFinalMap);


	    	//Accounted and unaccounted count

	    	log.info("to get count for accounted data");
	    	//1.List<rules> get rules tagged to rule grp id
	    	//2.get distinct (src and target) data views of that List<Rules>
	    	//3.Query Dv get srcId -->total count
	    	//4.query AccountedSummary where orginal rowIds in (srcId) 
	    	String actRuleGroupId=props.getProperty("accountingRuleGroup");
	    	log.info("recRuleGroup :"+actRuleGroupId);
	    	

	    	List<LinkedHashMap> AcctFinalMap=new ArrayList<LinkedHashMap>();

	    	
	     	List<Long> actRuleIds= ruleGroupDetailsRepository.fetchByRuleGroupIdAndTenantId(Long.valueOf(actRuleGroupId),tenantId);
	     	if(actRuleIds.size()!=0)
	     	{
	    	List<BigInteger> actRulesSrcDv=rulesRepository.fetchDistictSrcViewIdsByRuleId( recRuleIds);
	    	log.info("actRulesSrcDv :"+actRulesSrcDv);
	    	List<BigInteger> actRulesTrgDv=rulesRepository.fetchDistictTargetViewIdsByRuleId( recRuleIds);
	    	log.info("actRulesTrgDv :"+actRulesTrgDv);
	    	//adding trgDav to srcDvList

	    	for(int i=0;i<actRulesTrgDv.size();i++)
	    	{
	    		if(!actRulesSrcDv.contains(actRulesTrgDv.get(i)))
	    		{
	    			actRulesSrcDv.add(actRulesTrgDv.get(i));
	    		}

	    	}*/
	    	//log.info("Final actRulesSrcDv :"+actRulesSrcDv);
	   

	    	/*for (int i=0;i<actRulesSrcDv.size();i++)
	    	{
	    		if(actRulesSrcDv.get(i)!=null)
	    		{
	    			LinkedHashMap dvMap=new LinkedHashMap();
	    			DataViews dv=dataViewsRepository.findOne(actRulesSrcDv.get(i).longValue());
	    			dvMap.put("dvId", dv.getId());
	    			dvMap.put("dataViewName", dv.getDataViewName());
	    			String query="select scrIds from "+schemaName+"."+dv.getDataViewName().toLowerCase()+" where fileDate between '"+startDate+"' and '"+endDate+"'";
	    			log.info("query :"+query);
	    			resultAct=stmtAct.executeQuery(query);
	    			List<BigInteger> srcIds=new ArrayList<BigInteger>();
	    			while(resultAct.next())
	    			{
	    				srcIds.add(BigInteger.valueOf(Long.parseLong(resultAct.getString("scrIds"))));
	    			}
	    			log.info("srcIds.size :"+srcIds.size());
	    			if(srcIds.size()!=0)
	    			{
	    			dvMap.put("actualCount", srcIds.size());
	    			List<AccountedSummary> ActSummaryCount=accountedSummaryRepository.fetchIdsByRowIdsAndGroupIdAndViewIdAcctedStatus(srcIds, Long.valueOf(actRuleGroupId), dv.getId());
	    			log.info("ActSummaryCount :"+ActSummaryCount.size());
	    			dvMap.put("AccountedCount", ActSummaryCount.size());

	    			float percentage=((float) ActSummaryCount.size()/(float)srcIds.size())*100;
	    			log.info("percentage :"+percentage);
	    			dvMap.put("acountedPercent",percentage);
	    			if(ActSummaryCount.size()!=0)
	    			{
	    			int unAccCount = srcIds.size() - ActSummaryCount.size();
	        		dvMap.put("unAccountedCount",unAccCount);
	    			float unActPercent=100-percentage;
	    			log.info("unActPercent :"+unActPercent);
	    			dvMap.put("unAccountedPercent",unActPercent);
	    			}
	    			else
	    				dvMap.put("unAccountedPercent",0);
	    			}
	    			AcctFinalMap.add(dvMap);
	    		}

	    	}
	    }
	    	finalMap.put("accounting", AcctFinalMap);*/

	    	return finalMap;
	    }
    
      /**
       * author :ravali
       * @param agingAnalysisParameters
       * @return
       * @throws SQLException
       */
	    //previous aging
   
	     @PostMapping("/AgingWiseAnalysisForDashBoardQuery")
    @Timed 
    public LinkedHashMap AgingWiseAnalysisForDashBoardQuery(@RequestBody HashMap agingAnalysisParameters) throws SQLException 
    {
    	log.info("Rest Request to get aging analysis :"+agingAnalysisParameters);
    	LinkedHashMap finalMap=new LinkedHashMap();
    	LinkedHashMap agingWithAnalysis=new LinkedHashMap();


    	Properties props =  propertiesUtilService.getPropertiesFromClasspath("File.properties");

    	String status=agingAnalysisParameters.get("status").toString();
    	String dataviewName=agingAnalysisParameters.get("dataviewName").toString();
    	Long tenantId =Long.valueOf(agingAnalysisParameters.get("tenantId").toString());
    	Long dvId =Long.valueOf(agingAnalysisParameters.get("dvId").toString());

    	String recRuleGroupId=props.getProperty("reconciliationRuleGroup");
    	log.info("recRuleGroup :"+recRuleGroupId);

    	String actRuleGroupId=props.getProperty("accountingRuleGroup");
    	log.info("actRuleGroupId :"+actRuleGroupId);

    	ZonedDateTime fmDate=ZonedDateTime.parse(agingAnalysisParameters.get("startDate").toString());
    	ZonedDateTime toDate=ZonedDateTime.parse(agingAnalysisParameters.get("endDate").toString());
    	log.info("fmDate :"+fmDate);
    	log.info("toDate :"+toDate);

    	String dbUrl=env.getProperty("spring.datasource.url");
    	String[] parts=dbUrl.split("[\\s@&?$+-]+");
    	String host = parts[0].split("/")[2].split(":")[0];
    	log.info("host :"+host);
    	String schemaName=parts[0].split("/")[3];
    	log.info("schemaName :"+schemaName);
    	String userName = env.getProperty("spring.datasource.username");
    	String password = env.getProperty("spring.datasource.password");


    	Connection conn = null;
    	Statement stmtRecAndAct = null;



    	conn = DriverManager.getConnection(dbUrl, userName, password);
    	log.info("Connected database successfully...");
    
    	stmtRecAndAct = conn.createStatement();


    	ResultSet resultSrc=null;
    	ResultSet resultRecAndAct=null;

    	DataViews dv=dataViewsRepository.findOne(dvId);

    	String query="select scrIds from "+schemaName+"."+dv.getDataViewName().toLowerCase()+" where fileDate between '"+fmDate+"' and '"+toDate+"'";
    	log.info("query :"+query);
    	//resultSrc=stmtsrc.executeQuery(query);

    	List<BigInteger> srcIds=new ArrayList<BigInteger>();
		resultSrc=stmtRecAndAct.executeQuery(query);
		while(resultSrc.next())
		{
			srcIds.add(new BigInteger(resultSrc.getString("scrIds")));
		}
    	log.info("srcIds.size :"+srcIds.size());


    	agingWithAnalysis.put("viewType", dataviewName);


    	List<LinkedHashMap> dataMapList=new ArrayList<LinkedHashMap>();
    	if(status.equalsIgnoreCase("reconciled"))
    	{
    		agingWithAnalysis.put("type","reconcilied");
    		if(srcIds.size()!=0)
    		{
    			log.info("tenantId***********"+tenantId);
    			log.info("recRuleGroupId***********"+Long.valueOf(recRuleGroupId));
    			log.info("dv.getId()***********"+ dv.getId());

    			List<BigInteger> orginalRowIds=reconciliationResultRepository.fetchOrginalRowIds(tenantId,Long.valueOf(recRuleGroupId), dv.getId(), srcIds);
    			log.info(" if reconciled orginalRowIds :"+orginalRowIds.size());

    			String orginalRows=orginalRowIds.toString().replaceAll("\\[", "").replaceAll("\\]", "");


    			if(orginalRowIds.size()!=0)
    			{

    				String queryRec="SELECT "+
    						"DATEDIFF( r.reconciled_date,`v`.`fileDate`) as `rule_age`, count(`v`.`scrIds`), concat(DATEDIFF( r.reconciled_date,`v`.`fileDate`)-1,'-' ,DATEDIFF( r.reconciled_date,`v`.`fileDate`)) as period, coalesce (sum(r.final_status = 'APPROVED'),0) as Approved "+"FROM "+
    						"(select * from "+schemaName+"."+dataviewName.toLowerCase()+" where fileDate between '"+fmDate+"' and '"+toDate+"' and scrIds in ("+orginalRows+")) v"+
    						" JOIN `t_reconciliation_result` `r`"+
    						"on ( `r`.`original_row_id` = `v`.`scrIds` )   group by rule_age, period";
    				log.info("queryRec :"+queryRec);

    				resultRecAndAct=stmtRecAndAct.executeQuery(queryRec);

    				while(resultRecAndAct.next())
    				{
    					LinkedHashMap data=new LinkedHashMap();
    					Long ruleAge=Long.valueOf(resultRecAndAct.getString("rule_age"));
    					if(ruleAge.equals(0l))
    						data.put("age",0);
    					else
    						data.put("age",resultRecAndAct.getString("period"));
    					data.put("Count", resultRecAndAct.getString("count(`v`.`scrIds`)"));
    					data.put("approvalCount", resultRecAndAct.getString("Approved"));
    					dataMapList.add(data);
    				}
    			}
    		}

    	}
    	else if(status.equalsIgnoreCase("Not Reconciled"))
    	{
    		agingWithAnalysis.put("type","Not Reconciled");

    		if(srcIds.size()!=0)
    		{
    			List<BigInteger> orginalRowIds=reconciliationResultRepository.fetchOrginalRowIds(tenantId,Long.valueOf(recRuleGroupId), dv.getId(), srcIds);
    			log.info("if Not Reconciled :"+orginalRowIds.size());

    			String orginalRows=orginalRowIds.toString().replaceAll("\\[", "").replaceAll("\\]", "");
    			log.info("orginalRows :"+orginalRows);

    			if(orginalRowIds.size()!=0)
    			{
    				String queryUnRec="select DATEDIFF('"+toDate+"',`fileDate`) as `rule_age`, count(`scrIds`), concat((DATEDIFF('"+toDate+"',`fileDate`)-1),'-' ,DATEDIFF('"+toDate+"',`fileDate`)) as period from "+schemaName+"."+dataviewName.toLowerCase()+" where fileDate between '"+fmDate+"' and '"+toDate+"' and scrIds not in ("+orginalRows+") group by rule_age, period";
    				log.info("queryUnRec :"+queryUnRec);

    				resultRecAndAct=stmtRecAndAct.executeQuery(queryUnRec);

    				while(resultRecAndAct.next())
    				{
    					LinkedHashMap data=new LinkedHashMap();
    					Long ruleAge=Long.valueOf(resultRecAndAct.getString("rule_age"));
    					if(ruleAge.equals(0l))
    						data.put("age",0);
    					else
    						data.put("age",resultRecAndAct.getString("period"));
    					data.put("Count", resultRecAndAct.getString("count(`scrIds`)"));
    					dataMapList.add(data);
    				}
    			}
    		}
    	}
    	else if(status.equalsIgnoreCase("Accounted"))
    	{
    		agingWithAnalysis.put("type","Accounted");
    		if(srcIds.size()!=0)
    		{

    			List<BigInteger> acctRowIds=accountedSummaryRepository.fetchAllAccountedRowIdsBySrcIds(srcIds, Long.valueOf(actRuleGroupId), dv.getId());

    			String acctRows=acctRowIds.toString().replaceAll("\\[", "").replaceAll("\\]", "");

    			 /* SELECT
       DATEDIFF( a.created_date,`v`.`fileDate`) as `rule_age`, count(`v`.`scrIds`), concat((DATEDIFF( a.created_date,`v`.`fileDate`)-1),"-" ,DATEDIFF( a.created_date,`v`.`fileDate`)) as
       period, coalesce (sum(a.approval_status = "APPROVED"),0) as Approved, coalesce (sum(a.journal_status = "ENTERED"),0) as Journals
FROM
               (select * from agree_application_2712.sourcedv8thjan1_9 where fileDate between '2017-12-01T15:58:18+05:30' and '2017-12-07T15:58:18+05:30' and scrIds in (SELECT row_id FROM agree_application_2712.t_accounted_summary  where rule_group_id = 33 and view_id =37  and row_id in (select scrIds from agree_application_2712.sourcedv8thjan1_9 where fileDate between '2017-12-01T15:58:18+05:30' and '2017-12-07T15:58:18+05:30'))) v
                                                               JOIN `agree_application_2712`.`t_accounted_summary` `a`
               where ( `a`.`row_id` = `v`.`scrIds` )   group by rule_age, period*/

    			if(acctRowIds.size()!=0)
    			{
    				String queryact=" SELECT "+
    						"DATEDIFF( a.created_date,`v`.`fileDate`) as `rule_age`, count(`v`.`scrIds`), concat((DATEDIFF( a.created_date,`v`.`fileDate`)-1),'-' ,DATEDIFF( a.created_date,`v`.`fileDate`)) as"+
    						" period, coalesce (sum(a.approval_status = 'APPROVED'),0) as Approved, coalesce (sum(a.journal_status = 'ENTERED'),0) as Journals FROM "+
    						"(select * from "+schemaName+"."+dataviewName.toLowerCase()+" where fileDate between '"+fmDate+"' and '"+toDate+"' and scrIds in ("+acctRows+" )) v"
    						+" JOIN `agree_application_2712`.`t_accounted_summary` `a`"
    						+" where ( `a`.`row_id` = `v`.`scrIds` )   group by rule_age, period";
    				log.info("queryact :"+queryact);

    				resultRecAndAct=stmtRecAndAct.executeQuery(queryact);

    				while(resultRecAndAct.next())
    				{
    					LinkedHashMap data=new LinkedHashMap();
    					log.info("resultRecAndAct.getString('rule_age') :"+resultRecAndAct.getString("rule_age"));
    					Long ruleAge=Long.valueOf(resultRecAndAct.getString("rule_age"));
    					if(ruleAge.equals(0l))
    						data.put("age",0);
    					else
    						data.put("age",resultRecAndAct.getString("period"));
    					data.put("Count", resultRecAndAct.getString("count(`v`.`scrIds`)"));
    					data.put("approvedCount", resultRecAndAct.getString("Approved"));
    					data.put("journalCount", resultRecAndAct.getString("Journals"));

    					dataMapList.add(data);
    				}
    			}
    		}
    	}
    	else if(status.equalsIgnoreCase("Not Accounted"))
    	{

    		agingWithAnalysis.put("type","Not Accounted");


    		 /*select 
	       DATEDIFF(`fileDate`, '2017-12-01T15:58:18+05:30') as `rule_age`, count(`scrIds`), concat((DATEDIFF(`fileDate`, '2017-12-01T15:58:18+05:30')-1),"-" ,DATEDIFF(`fileDate`, '2017-12-01T15:58:18+05:30')) as period 
	       from agree_application_2712.sourcedv8thjan1_9 where fileDate between '2017-12-01T15:58:18+05:30' and '2017-12-07T15:58:18+05:30' and scrIds not in 
	       (SELECT row_id FROM agree_application_2712.t_accounted_summary  where rule_group_id = 33 and view_id =37  and row_id in 
	       (select scrIds from agree_application_2712.sourcedv8thjan1_9 where fileDate between '2017-12-01T15:58:18+05:30' and '2017-12-07T15:58:18+05:30')) group by rule_age, period
*/
    		if(srcIds.size()!=0)
    		{
    			List<BigInteger> acctRowIds=accountedSummaryRepository.fetchAllAccountedRowIdsBySrcIds(srcIds, Long.valueOf(actRuleGroupId), dv.getId());
    			log.info("acctRowIds :"+acctRowIds.size());

    			String acctRows=acctRowIds.toString().replaceAll("\\[", "").replaceAll("\\]", "");
    			log.info("acctRows :"+acctRows);

    			if(acctRowIds.size()!=0)
    			{

    				String queryact=" SELECT "+
    						"DATEDIFF('"+toDate+"',`fileDate`) as `rule_age`, count(`scrIds`), concat((DATEDIFF('"+toDate+"',`fileDate`)-1),'-' ,DATEDIFF('"+toDate+"',`fileDate`)) as"+
    						" period FROM "+
    						schemaName+"."+dataviewName.toLowerCase()+" where fileDate between '"+fmDate+"' and '"+toDate+"' and scrIds not in ("+acctRows+" ) "
    						+"group by rule_age, period";
    				log.info("queryact :"+queryact);

    				resultRecAndAct=stmtRecAndAct.executeQuery(queryact);

    				while(resultRecAndAct.next())
    				{
    					LinkedHashMap data=new LinkedHashMap();
    					log.info("resultRecAndAct.getString('rule_age') :"+resultRecAndAct.getString("rule_age"));
    					Long ruleAge=Long.valueOf(resultRecAndAct.getString("rule_age"));
    					if(ruleAge.equals(0l))
    						data.put("age",0);
    					else
    						data.put("age",resultRecAndAct.getString("period"));
    					data.put("Count", resultRecAndAct.getString("count(`scrIds`)"));
    					dataMapList.add(data);
    				}
    			}
    		}

    	}

    	agingWithAnalysis.put("data", dataMapList);
    	finalMap.put("agingWithAnalysis", agingWithAnalysis);
    	if(resultSrc!=null)
    		resultSrc.close();
    	if(resultRecAndAct!=null)
    		resultRecAndAct.close();
    	if(resultRecAndAct!=null)
    		resultRecAndAct.close();
    	if(stmtRecAndAct!=null)
    		stmtRecAndAct.close();
    	if(conn!=null)
    		conn.close();
    	log.info("*****end*****");
    	return finalMap;
    }
    

    /**
     * author:ravali
     * API tp fetch notification by appliying filters
     * @param tenantId
     * @param userId
     * @param module
     * @param day
     * @return
     */
    @GetMapping("/getNotificationsDataByUserIdAndTenantId")
    @Timed
    public HashMap getNotificationsDataByUserIdAndTenantId( @RequestParam String module,HttpServletRequest request,
    		@RequestParam(required=false,value="day") String day){
    	
    	 HashMap map=userJdbcService.getuserInfoFromToken(request);
    	 Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	 Long userId=Long.parseLong(map.get("userId").toString());
    	 
    	 log.info("Rest api for getting notifications by user id: "+ userId +", tenant id: "+ tenantId);
     	
    	
    	HashMap finalMap = new HashMap();
    	List<Notifications> notifications=new ArrayList<Notifications>();
    	if(module.equalsIgnoreCase("all"))
    	{
    		log.info("LocalDate.now() :"+LocalDate.now());
    		log.info("LocalDate.now().minus :"+LocalDate.now().minusDays(1));
    		/*if(day==null)
    		{
    			notifications = notificationsRepository.findByTenantIdAndUserIdOrderByIdDesc(tenantId, userId);
    			log.info("notifications0 :"+notifications);
    		}*/
    	   if(day.equalsIgnoreCase("today"))
    		{
    			notifications = notificationsRepository.findByTenantIdAndUserIdAndCreationDate(tenantId, userId,LocalDate.now()+"%");
    			log.info("notifications1 :"+notifications.size());
    		}
    		if(day.equalsIgnoreCase("yesterday"))
    		{
    			notifications = notificationsRepository.findByTenantIdAndUserIdAndCreationDate(tenantId, userId,LocalDate.now().minusDays(1)+"%");
    			log.info("notifications2 :"+notifications.size());
    		}
    		if(day.equalsIgnoreCase("7days"))
    		{
    			LocalDate startDate=LocalDate.now().minusDays(7);
    			LocalDate endDate=LocalDate.now();
    			log.info("startDate :"+startDate.toString()+"%");
    			log.info("endDate :"+endDate.toString()+"%");
    			notifications = notificationsRepository.findByTenantIdAndUserIdAndCreationDateBetween(tenantId, userId,startDate.toString()+"%",endDate.toString()+"%");
    			log.info("notifications3 :"+notifications.size());
    		}


    	}
    	else 
    	{

    		log.info("LocalDate.now() :"+LocalDate.now());
    		log.info("LocalDate.now().minus :"+LocalDate.now().minusDays(1));
    		log.info("day else :"+day);
    		if(day.equalsIgnoreCase("today"))
    		{
    			log.info("in today");
    			notifications = notificationsRepository.findByTenantIdAndUserIdAndModuleAndCreationDate(tenantId, userId,module,LocalDate.now()+"%");
    			log.info("notifications today :"+notifications.size());
    		}
    		if(day.equalsIgnoreCase("yesterday"))
    		{
    			log.info("in yesterday");
    			notifications = notificationsRepository.findByTenantIdAndUserIdAndModuleAndCreationDate(tenantId, userId,module,LocalDate.now().minusDays(1)+"%");
    			log.info("notifications today :"+notifications.size());
    		}
    	    if(day.equalsIgnoreCase("7days"))
    		{
    			LocalDate startDate=LocalDate.now().minusDays(7);
    			LocalDate endDate=LocalDate.now();
    			log.info("startDate :"+startDate.toString()+"%");
    			log.info("endDate :"+endDate.toString()+"%");
    			notifications = notificationsRepository.findByTenantIdAndUserIdAndModuleAndCreationDateBetween(tenantId, userId,module,startDate.toString()+"%",endDate.toString()+"%");
    			log.info("notifications 7days:"+notifications.size());
    		}

    		

    	}
    	log.info("No of records fetched: "+ notifications.size());
    	List<HashMap> data = new ArrayList<HashMap>();
    	if(notifications.size()>0)
    	{
    		for(Notifications not : notifications)
    		{
    			HashMap hm = new HashMap();
    			hm.put("id", not.getId());
    			hm.put("message", not.getMessage());
    			hm.put("module", not.getModule());
    			hm.put("actionType", not.getActionType());
    			hm.put("actionValue", not.getActionValue());
    			hm.put("creationDate", not.getCreationDate());
    			hm.put("isViewed", not.isIsViewed());
    			//Getting time difference in Seconds, Minuites, Hours
    			String time = "";
    			ZonedDateTime start = not.getCreationDate();
    			ZonedDateTime stop = ZonedDateTime.now();
    			Duration dur = Duration.between(start, stop);
    			long hours = dur.toHours();
    			long minutes = dur.toMinutes();
    			long days = dur.toDays();
    			long seconds = dur.getSeconds();
    			if(days>0)
    			{
    				time = time + days+" days ago";
    			}
    			else if(hours>0)
    			{
    				time = time +hours + " hrs ago";
    			}
    			else if(minutes>0)
    			{
    				time = time+minutes + " mins ago";
    			}
    			else if(seconds>0)
    			{
    				time = time + seconds + " secs ago";
    			}
    			hm.put("time", time);		
    			data.add(hm);
    		}
    	}
    	finalMap.put("data", data);
    	
    	
    	
    	//to return individual count 
    		LookUpCode appMod=new LookUpCode();
    		int totalCount=0;
    		//List<String> distModule=notificationsRepository.findDinstinctModuleByTenantIdAndUserId(tenantId, userId);
    		/**in order to show notification one for below mentioned 3 modules**/
    		
    		List<String> distModule=new ArrayList<String>();
    		
    		distModule.add("RECON");
    		distModule.add("ACCOUNTING");
    		distModule.add("JOURNAL_ENTRY");
    		
    		if(day.equalsIgnoreCase("today"))
    		{
    			
    			for(String mod:distModule)
    			{
    			notifications = notificationsRepository.findByTenantIdAndUserIdAndModuleAndCreationDate(tenantId, userId,mod,LocalDate.now()+"%");
    			appMod=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("APP_MODULES", mod, tenantId);
    			log.info("appMod :"+appMod);
    			finalMap.put(appMod.getMeaning().replaceAll("\\s",""), notifications.size());
    			totalCount=totalCount+notifications.size();
    			}
    			log.info("notifications1 :"+notifications);
    		}
    		else if(day.equalsIgnoreCase("yesterday"))
    		{
    			
    			for(String mod:distModule)
    			{
    			notifications = notificationsRepository.findByTenantIdAndUserIdAndModuleAndCreationDate(tenantId, userId,mod,LocalDate.now().minusDays(1)+"%");
    			appMod=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("APP_MODULES", mod, tenantId);
    			finalMap.put(appMod.getMeaning().replaceAll("\\s",""), notifications.size());
    			totalCount=totalCount+notifications.size();
    			}
    			log.info("notifications2 :"+notifications);
    		}
    		 if(day.equalsIgnoreCase("7days"))
    		{
    			LocalDate startDate=LocalDate.now().minusDays(7);
    			LocalDate endDate=LocalDate.now();
    			log.info("startDate :"+startDate.toString()+"%");
    			log.info("endDate :"+endDate.toString()+"%");
    			for(String mod:distModule)
    			{
    				
    			notifications = notificationsRepository.findByTenantIdAndUserIdAndModuleAndCreationDateBetween(tenantId, userId,mod,startDate.toString()+"%",endDate.toString()+"%");
    			appMod=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("APP_MODULES", mod, tenantId);
    			if(appMod!=null)
    			finalMap.put(appMod.getMeaning().replaceAll("\\s",""), notifications.size());
    			totalCount=totalCount+notifications.size();
    			log.info("notifications3 :"+notifications);
    			}
    		}
    		if(finalMap.get("Accounting")==null)
    			finalMap.put("Accounting", 0);
    		if(finalMap.get("Recon")==null)
    			finalMap.put("Recon", 0);
    		if(finalMap.get("JournalEntry")==null)
    			finalMap.put("JournalEntry", 0);
    		
    		finalMap.put("count", totalCount);
    	
    	return finalMap;
    }
    
    
    /**
     * author:ravali
     * desc:to get each day count
     * @param agingAnalysisParameters
     * @return
     * @throws SQLException
     */
	
    @PostMapping("/getReconciliationAccountingAndJournalCountForViews")
    @Timed
    public List<LinkedHashMap> getReconciliationAccountingAndJournalCountForAll(@RequestBody HashMap agingAnalysisParameters) throws SQLException 
    {
    	log.info("Rest Request to get aging analysis :"+agingAnalysisParameters);
    	List<LinkedHashMap> finalMap=new ArrayList<LinkedHashMap>();


    	Properties props =  propertiesUtilService.getPropertiesFromClasspath("File.properties");

    	String status=agingAnalysisParameters.get("status").toString();
    	String dataviewName=agingAnalysisParameters.get("dataviewName").toString();
    	Long tenantId =Long.valueOf(agingAnalysisParameters.get("tenantId").toString());
    	/*Long dvId =Long.valueOf(agingAnalysisParameters.get("dvId").toString());*/

    	String recRuleGroupId=props.getProperty("reconciliationRuleGroup");
    	log.info("recRuleGroup :"+recRuleGroupId);

    	String actRuleGroupId=props.getProperty("accountingRuleGroup");
    	log.info("actRuleGroupId :"+actRuleGroupId);


    	ZonedDateTime fmDate=ZonedDateTime.parse(agingAnalysisParameters.get("startDate").toString());
    	ZonedDateTime toDate=ZonedDateTime.parse(agingAnalysisParameters.get("endDate").toString());
    	log.info("fmDate :"+fmDate);
    	log.info("toDate :"+toDate);
    	java.time.LocalDate fDate=fmDate.toLocalDate();
    	java.time.LocalDate tDate=toDate.toLocalDate();
    	log.info("fDate :"+fDate);
    	log.info("tDate :"+tDate);
    	String dbUrl=env.getProperty("spring.datasource.url");
    	String[] parts=dbUrl.split("[\\s@&?$+-]+");
    	String host = parts[0].split("/")[2].split(":")[0];
    	log.info("host :"+host);
    	String schemaName=parts[0].split("/")[3];
    	log.info("schemaName :"+schemaName);
    	String userName = env.getProperty("spring.datasource.username");
    	String password = env.getProperty("spring.datasource.password");


    	Connection conn = null;
    	
    	Statement stmtRecAndAct = null;



    	conn = DriverManager.getConnection(dbUrl, userName, password);
    	log.info("Connected database successfully...");
    	
    	stmtRecAndAct = conn.createStatement();


    	ResultSet resultSrc=null;
    	ResultSet resultRecAndAct=null;

    //	DataViews dv=dataViewsRepository.findOne(dvId);
    	log.info("fDate :"+fDate);
    	log.info("tDate :"+tDate.plusDays(1));
    	
    	
    	
    	List<BigInteger> recRulesSrcDv=new ArrayList<BigInteger>();
    	
    	//Reconciliation group
    	List<Long> recRuleIds= ruleGroupDetailsRepository.fetchByRuleGroupIdAndTenantId(Long.valueOf(recRuleGroupId),tenantId);
    	log.info("recRuleIds :"+recRuleIds);
    	if(recRuleIds.size()!=0)
    	{
    	recRulesSrcDv=rulesRepository.fetchDistictSrcViewIdsByRuleId( recRuleIds);
    	log.info("rulesSrcDv :"+recRulesSrcDv);
    	List<BigInteger> recRulesTrgDv=rulesRepository.fetchDistictTargetViewIdsByRuleId( recRuleIds);
    	log.info("rulesTrgDv :"+recRulesTrgDv);
    	//adding trgDav to srcDvList

    	for(int i=0;i<recRulesTrgDv.size();i++)
    	{
    		if(!recRulesSrcDv.contains(recRulesTrgDv.get(i)))
    		{
    			recRulesSrcDv.add(recRulesTrgDv.get(i));
    		}

    	}
    	}
    	
    	//accounting group
    	
    	
    	List<Long> acctRuleIds= ruleGroupDetailsRepository.fetchByRuleGroupIdAndTenantId(Long.valueOf(actRuleGroupId),tenantId);
    	if(acctRuleIds.size()!=0)
    	{
    		List<BigInteger> actRulesSrcDv=rulesRepository.fetchDistictSrcViewIdsByRuleId( recRuleIds);
    	log.info("actRulesSrcDv :"+actRulesSrcDv);
    	List<BigInteger> actRulesTrgDv=rulesRepository.fetchDistictTargetViewIdsByRuleId( recRuleIds);
    	log.info("actRulesTrgDv :"+actRulesTrgDv);
    	//adding trgDav to srcDvList

    	for(int i=0;i<actRulesTrgDv.size();i++)
    	{
    		if(!recRulesSrcDv.contains(actRulesTrgDv.get(i)))
    		{
    			recRulesSrcDv.add(actRulesTrgDv.get(i));
    		}

    	}
    	}
    	
    	
       log.info("finalDvList :"+recRulesSrcDv);
       while(fDate.isBefore(tDate.plusDays(1)))
       {
    	   log.info("fDate in while:"+fDate);
    	   int totalCount=0;
    	   int reconciledCount=0;
    	   int accountedCount=0;
    	   int journalCount=0;
    	   LinkedHashMap map=new LinkedHashMap();
    	   map.put("date", fDate);
    	   for (int i=0;i<recRulesSrcDv.size();i++)
    	   {
    		   log.info("recRulesSrcDv :"+recRulesSrcDv);
    		   if(recRulesSrcDv.get(i)!=null)
    		   {
    			   LinkedHashMap dvMap=new LinkedHashMap();
    			   DataViews dv=dataViewsRepository.findOne(recRulesSrcDv.get(i).longValue());
    			  
    			   String query="select scrIds from "+schemaName+"."+dv.getDataViewName().toLowerCase()+" where fileDate Like '"+fDate+"%'";
    			   log.info("query :"+query);
    				List<BigInteger> srcIds=new ArrayList<BigInteger>();
    	    		resultSrc=stmtRecAndAct.executeQuery(query);
    	    		while(resultSrc.next())
    	    		{
    	    			srcIds.add(new BigInteger(resultSrc.getString("scrIds")));
    	    		}
    			   totalCount=totalCount+srcIds.size();

    			   if(srcIds.size()!=0)
    			   {
    				   List<BigInteger> reconciliationCount=reconciliationResultRepository.fetchOrginalRowIds(tenantId,Long.valueOf(recRuleGroupId), dv.getId(), srcIds);
    				   log.info("srcIds :"+srcIds.size());
    				   log.info("reconciliationCount :"+reconciliationCount.size());

    				   reconciledCount=reconciledCount+reconciliationCount.size();

    				   List<BigInteger> acctRowIds=accountedSummaryRepository.fetchAllAccountedRowIdsBySrcIds(srcIds, Long.valueOf(actRuleGroupId), dv.getId());
    				   accountedCount=accountedCount+acctRowIds.size();
    				   
    				   //for journal
    				   List<BigInteger> journalRowIds=accountedSummaryRepository.fetchAllAccountedRowIdsBySrcIdsAndJournalStatus(srcIds, Long.valueOf(actRuleGroupId), dv.getId());
    				   journalCount=journalCount+journalRowIds.size();
    			   }

    			   
    		   }
    	   }

    	   map.put("acctCount", accountedCount);
    	   map.put("reconCount", reconciledCount);
    	   map.put("journalCount", journalCount);
    	   
    	   if(accountedCount!=0)
    	   {
    	   float Actpercentage=((float) accountedCount/(float)totalCount)*100;
    	   log.info("Actpercentage :"+Actpercentage);
    	   map.put("acctountedPercent",Actpercentage);
    	   }

    	   if(reconciledCount!=0)
    	   {
    	   float percentage=((float)reconciledCount/(float)totalCount)*100;
    	   log.info("percentage :"+percentage);
    	   map.put("reconciledPercent",percentage);
    	   }
    	   if(journalCount!=0)
    	   {
    	   float percentage=((float)journalCount/(float)totalCount)*100;
    	   log.info("percentage :"+percentage);
    	   map.put("journalPercent",percentage);
    	   }
    	   finalMap.add(map);
    	   fDate=fDate.plusDays(1);
		   log.info("fDate in end while:"+fDate);
       }
       if(resultSrc!=null)
    	   resultSrc.close();
       if(stmtRecAndAct!=null)
    	   stmtRecAndAct.close();
       if(conn!=null)
    	   conn.close();
    	return finalMap;


    }

    /**
     * author:ravali
     * @param agingAnalysisParameters
     * @return
     * @throws SQLException
     * #Revamped queries
     * #currently working
     */
  
   @PostMapping("/reconciliationAndAccoutingAgingWiseAnalysisForDashBoard")
    @Timed 
    public LinkedHashMap agingWiseAnalysisForDashBoardWithRevampedQuery(@RequestBody HashMap agingAnalysisParameters) throws SQLException 
   {
	   log.info("Rest Request to get aging analysis :"+agingAnalysisParameters);
	   LinkedHashMap finalMap=new LinkedHashMap();
	   LinkedHashMap agingWithAnalysis=new LinkedHashMap();


	   Properties props =  propertiesUtilService.getPropertiesFromClasspath("File.properties");
	   Long processId=Long.valueOf(agingAnalysisParameters.get("processId").toString());
	   String status=agingAnalysisParameters.get("status").toString();
	   String dataviewName=agingAnalysisParameters.get("dataviewName").toString();
	   Long tenantId =Long.valueOf(agingAnalysisParameters.get("tenantId").toString());
	   Long dvId =Long.valueOf(agingAnalysisParameters.get("viewId").toString());
	   String dvType=agingAnalysisParameters.get("dvType").toString();

	   /* String recRuleGroupId=props.getProperty("reconciliationRuleGroup");
	   log.info("recRuleGroup :"+recRuleGroupId);

	   String actRuleGroupId=props.getProperty("accountingRuleGroup");
	   log.info("actRuleGroupId :"+actRuleGroupId);
	    */
	   Long recGrpId=0l;
	   Long actGrpId=0l;
	   ProcessDetails procesRecDet=processDetailsRepository.findByProcessIdAndTagType(processId, "reconciliationRuleGroup");
	   if(procesRecDet!=null)
		   recGrpId=procesRecDet.getTypeId();

	   ProcessDetails procesActDet=processDetailsRepository.findByProcessIdAndTagType(processId, "accountingRuleGroup");
	   if(procesActDet!=null)
		   actGrpId=procesActDet.getTypeId();



	   ZonedDateTime fmDate=ZonedDateTime.parse(agingAnalysisParameters.get("startDate").toString());
	   ZonedDateTime toDate=ZonedDateTime.parse(agingAnalysisParameters.get("endDate").toString());
	   log.info("fmDate :"+fmDate);
	   log.info("toDate :"+toDate);

	   String dbUrl=env.getProperty("spring.datasource.url");
	   String[] parts=dbUrl.split("[\\s@&?$+-]+");
	   String host = parts[0].split("/")[2].split(":")[0];
	   log.info("host :"+host);
	   String schemaName=parts[0].split("/")[3];
	   log.info("schemaName :"+schemaName);
	   String userName = env.getProperty("spring.datasource.username");
	   String password = env.getProperty("spring.datasource.password");


	   Connection conn = null;
	 
	   Statement stmtRecAndAct = null;



	   conn = DriverManager.getConnection(dbUrl, userName, password);
	   log.info("Connected database successfully...");
	 
	   stmtRecAndAct = conn.createStatement();


	   ResultSet resultSrc=null;
	   ResultSet resultRecAndAct=null;

	   DataViews dv=dataViewsRepository.findOne(dvId);

	   //fetching srcIds from data master table

	   java.time.LocalDate fDate=fmDate.toLocalDate();
	   java.time.LocalDate tDate=toDate.toLocalDate();
	   log.info("fDate :"+fDate);
	   log.info("tDate :"+tDate);

	   /*List<BigInteger> srcIdsDM=dataMasterRepository.fetchIdsByFileDates(tenantId, fDate+"%",tDate+"%");
    	log.info("srcIdsDM :"+srcIdsDM.size());*/

	   agingWithAnalysis.put("viewType", dataviewName);


	   List<LinkedHashMap> dataMapList=new ArrayList<LinkedHashMap>();
	   if(status.equalsIgnoreCase("reconciled"))
	   {
		   agingWithAnalysis.put("type","reconcilied");
		   log.info("tenantId***********"+tenantId);
		   log.info("recRuleGroupId***********"+recGrpId);
		   log.info("dv.getId()***********"+ dv.getId());
		   List<LinkedHashMap> lhpList=new ArrayList<LinkedHashMap>();
		   List<Object[]> reconcAnalysis=new ArrayList<Object[]>();
		   if(dvType.equalsIgnoreCase("source"))
		   {
			   reconcAnalysis =reconciliationResultRepository.fetchAgeAnalysisForSrcReconciliation(tenantId,recGrpId, dv.getId(), fDate+"%",tDate+"%");
		   }
		   else
		   {
			   reconcAnalysis =reconciliationResultRepository.fetchAgeAnalysisForTrgReconciliation(tenantId,recGrpId, dv.getId(), fDate+"%",tDate+"%");
		   }
		   log.info("reconcAnalysis.size()***********"+ reconcAnalysis.size());
		   for(int i=0;i<reconcAnalysis.size();i++)
		   {
			   LinkedHashMap lhp=new LinkedHashMap();
			   Long ruleAge=Long.valueOf(reconcAnalysis.get(i)[0].toString());
			   if(ruleAge.equals(0l))
				   lhp.put("age",0);
			   else
				   lhp.put("age",reconcAnalysis.get(i)[2]);
			   lhp.put("rule_age", reconcAnalysis.get(i)[0]);
			   lhp.put("count", reconcAnalysis.get(i)[1]);
			   lhp.put("approvalCount", reconcAnalysis.get(i)[3]);
			   dataMapList.add(lhp);

		   }
	   }
	   else if(status.equalsIgnoreCase("Not Reconciled"))
	   {
		   agingWithAnalysis.put("type","Not Reconciled");

		   /*List<BigInteger> srcIdsDM=dataMasterRepository.fetchIdsByFileDates(tenantId, fDate+"%",tDate+"%");
        	log.info("srcIdsDM :"+srcIdsDM.size());*/

		   String query="select scrIds from "+schemaName+"."+dv.getDataViewName().toLowerCase()+" where fileDate between '"+fmDate+"' and '"+toDate+"'";
		   log.info("query :"+query);
		   //  resultSrc=stmtsrc.executeQuery(query);

		   List<BigInteger> srcIds=new ArrayList<BigInteger>();
		   resultSrc=stmtRecAndAct.executeQuery(query);
		   while(resultSrc.next())
		   {
			   srcIds.add(new BigInteger(resultSrc.getString("scrIds")));
		   }
		   log.info("srcIds.size :"+srcIds.size());

		   if(srcIds.size()!=0)
		   {
			   List<BigInteger> orginalRowIds=reconciliationResultRepository.fetchOrginalRowIds(tenantId,recGrpId, dv.getId(), srcIds);
			   log.info("if Not Reconciled :"+orginalRowIds.size());

			   String orginalRows=orginalRowIds.toString().replaceAll("\\[", "").replaceAll("\\]", "");
			   log.info("orginalRows :"+orginalRows);

			   if(orginalRowIds.size()!=0)
			   {
				   String queryUnRec="select DATEDIFF('"+toDate+"',`fileDate`) as `rule_age`, count(`scrIds`), concat((DATEDIFF('"+toDate+"',`fileDate`)-1),'-' ,DATEDIFF('"+toDate+"',`fileDate`)) as period from "+schemaName+"."+dataviewName.toLowerCase()+" where fileDate between '"+fmDate+"' and '"+toDate+"' and scrIds not in ("+orginalRows+") group by rule_age, period";
				   log.info("queryUnRec :"+queryUnRec);

				   resultRecAndAct=stmtRecAndAct.executeQuery(queryUnRec);

				   while(resultRecAndAct.next())
				   {
					   LinkedHashMap data=new LinkedHashMap();
					   Long ruleAge=Long.valueOf(resultRecAndAct.getString("rule_age"));
					   if(ruleAge.equals(0l))
						   data.put("age",0);
					   else
						   data.put("age",resultRecAndAct.getString("period"));
					   data.put("Count", resultRecAndAct.getString("count(`scrIds`)"));
					   dataMapList.add(data);
				   }
			   }
		   }
	   }
	   else if(status.equalsIgnoreCase("Accounted"))
	   {
		   agingWithAnalysis.put("type","Accounted");


		   List<Object[]> actAnalysis=accountedSummaryRepository.fetchAgeAnalysisForAccounting(actGrpId, dv.getId(),tenantId, fDate+"%",tDate+"%");
		   log.info("actAnalysis :"+actAnalysis.size());
		   for(int i=0;i<actAnalysis.size();i++)
		   {
			   LinkedHashMap lhp=new LinkedHashMap();
			   Long ruleAge=Long.valueOf(actAnalysis.get(i)[0].toString());
			   if(ruleAge.equals(0l))
				   lhp.put("age",0);
			   else
				   lhp.put("age", actAnalysis.get(i)[2]);
			   lhp.put("count", actAnalysis.get(i)[1]);
			   lhp.put("approvedCount", actAnalysis.get(i)[3]);
			   lhp.put("journalCount", actAnalysis.get(i)[4]);
			   dataMapList.add(lhp);
		   }

	   }
	   else if(status.equalsIgnoreCase("Not Accounted"))
	   {

		   agingWithAnalysis.put("type","Not Accounted");

		   String query="select scrIds from "+schemaName+"."+dv.getDataViewName().toLowerCase()+" where fileDate between '"+fmDate+"' and '"+toDate+"'";
		   log.info("query :"+query);
		   // resultSrc=stmtsrc.executeQuery(query);

		   List<BigInteger> srcIds=new ArrayList<BigInteger>();
		   resultSrc=stmtRecAndAct.executeQuery(query);
		   while(resultSrc.next())
		   {
			   srcIds.add(new BigInteger(resultSrc.getString("scrIds")));
		   }
		   log.info("srcIds.size :"+srcIds.size());

		   if(srcIds.size()!=0)
		   {
			   List<BigInteger> acctRowIds=accountedSummaryRepository.fetchAllAccountedRowIdsBySrcIds(srcIds, actGrpId, dv.getId());
			   log.info("acctRowIds :"+acctRowIds.size());

			   String acctRows=acctRowIds.toString().replaceAll("\\[", "").replaceAll("\\]", "");
			   log.info("acctRows :"+acctRows);

			   if(acctRowIds.size()!=0)
			   {

				   String queryact=" SELECT "+
						   "DATEDIFF('"+toDate+"',`fileDate`) as `rule_age`, count(`scrIds`), concat((DATEDIFF('"+toDate+"',`fileDate`)-1),'-' ,DATEDIFF('"+toDate+"',`fileDate`)) as"+
						   " period FROM "+
						   schemaName+"."+dataviewName.toLowerCase()+" where fileDate between '"+fmDate+"' and '"+toDate+"' and scrIds not in ("+acctRows+" ) "
						   +"group by rule_age, period";
				   log.info("queryact :"+queryact);

				   resultRecAndAct=stmtRecAndAct.executeQuery(queryact);

				   while(resultRecAndAct.next())
				   {
					   LinkedHashMap data=new LinkedHashMap();
					   log.info("resultRecAndAct.getString('rule_age') :"+resultRecAndAct.getString("rule_age"));
					   Long ruleAge=Long.valueOf(resultRecAndAct.getString("rule_age"));
					   if(ruleAge.equals(0l))
						   data.put("age",0);
					   else
						   data.put("age",resultRecAndAct.getString("period"));
					   data.put("Count", resultRecAndAct.getString("count(`scrIds`)"));
					   dataMapList.add(data);
				   }
			   }
		   }

	   }

	   agingWithAnalysis.put("data", dataMapList);
	   finalMap.put("agingWithAnalysis", agingWithAnalysis);
	   if(resultSrc!=null)
		   resultSrc.close();
	   if(resultRecAndAct!=null)
		   resultRecAndAct.close();
	   if(stmtRecAndAct!=null)
		   stmtRecAndAct.close();
	   if(conn!=null)
		   conn.close();
	   log.info("*****end*****");
	   return finalMap;
   }
    
    
    
    
    @PostMapping("/getExtractionAndTransformation")
    @Timed
    public List<LinkedHashMap> getExtractionAndTransformation(@RequestBody HashMap agingAnalysisParameters,@RequestParam Long tenantId) 
    {
    	List<LinkedHashMap> finalMap=new ArrayList<LinkedHashMap>();
    	ZonedDateTime fmDate=ZonedDateTime.parse(agingAnalysisParameters.get("startDate").toString());
    	ZonedDateTime toDate=ZonedDateTime.parse(agingAnalysisParameters.get("endDate").toString());
    	log.info("fmDate :"+fmDate);
    	log.info("toDate :"+toDate);
    	java.time.LocalDate fDate=fmDate.toLocalDate();
    	java.time.LocalDate tDate=toDate.toLocalDate();
    	log.info("fDate :"+fDate);
    	log.info("tDate :"+tDate);

    	Properties props =  propertiesUtilService.getPropertiesFromClasspath("File.properties");

    	String profiles=props.getProperty("profiles");
    	log.info("profiles :"+profiles);
    	Pattern pattern = Pattern.compile("\\d+");
    	Matcher matcher = pattern.matcher(profiles);

    	List<Long> profileId = new ArrayList<Long>();

    	 while (matcher.find()) {
    	System.out.println(matcher.group());
    	profileId.add(Long.parseLong(matcher.group())); // Add the value to the list
    	}
    	log.info("profileId Long:"+profileId);
    	
    	while(fDate.isBefore(tDate.plusDays(1)))
    	{
    		log.info("in while fDate :"+fDate);
    		LinkedHashMap map=new LinkedHashMap();
    		map.put("date",fDate );
    		log.info("fDate%" +fDate+"%");;
    		List<SourceFileInbHistory> totalFilesExtracted=sourceFileInbHistoryRepository.findByProfileIdsAndFileReceivedDate(profileId,tenantId,fDate+"%");
    		log.info("totalFilesExtracted :"+totalFilesExtracted.size());
    		
    		List<SourceFileInbHistory> totalFilesTransformed=sourceFileInbHistoryRepository.findByProfileIdsAndFileReceivedDateAndStatusLoaded(profileId,tenantId,fDate+"%");
    		log.info("totalFilesTransformed :"+totalFilesTransformed);
    		map.put("totalExtractedCount", totalFilesExtracted.size());
    		map.put("totalTransFormedCount", totalFilesTransformed.size());
    		
    		finalMap.add(map);
    		fDate=fDate.plusDays(1);
    		log.info("in end of while fDate :"+fDate);
    		
    	}
		return finalMap;
    	
    }
 
    		
    
    
    /**
     * date:27th Jan 2017
     * @param dates
     * @param tenantId
     * @return
     */
    
    @PostMapping("/getExtractionPerProfile")
    @Timed
    public LinkedHashMap getExtractionPerProfile(@RequestBody HashMap dates,@RequestParam Long tenantId,@RequestParam Long profileId,@RequestParam Long tempId ) 
    {
    	List<LinkedHashMap> finalMap=new ArrayList<LinkedHashMap>();
    	ZonedDateTime fmDate=ZonedDateTime.parse(dates.get("startDate").toString());
    	ZonedDateTime toDate=ZonedDateTime.parse(dates.get("endDate").toString());
    	log.info("fmDate :"+fmDate);
    	log.info("toDate :"+toDate);
    	java.time.LocalDate fDate=fmDate.toLocalDate();
    	java.time.LocalDate tDate=toDate.toLocalDate();
    	log.info("fDate :"+fDate);
    	log.info("tDate :"+tDate);

    	Properties props =  propertiesUtilService.getPropertiesFromClasspath("File.properties");

    	String profiles=props.getProperty("profiles");
    	log.info("profiles :"+profiles);
    	Pattern pattern = Pattern.compile("\\d+");
    	Matcher matcher = pattern.matcher(profiles);

    //	List<Long> profileId = new ArrayList<Long>();

    	/*while (matcher.find()) {
    		System.out.println(matcher.group());
    		profileId.add(Long.parseLong(matcher.group())); // Add the value to the list
    	}*/
    	log.info("profileId Long:"+profileId);
    	ApplicationPrograms app=applicationProgramsRepository.findByPrgmNameAndTenantId("DataExtraction", tenantId);
    	//for(Long profId:profileId)
    	//{


    		log.info("fDate1 :"+fDate);	
    		log.info("tDate1 :"+tDate);	

    		List<BigInteger> jobDetails=jobDetailsRepository.findByTenantIdAndProgrammIdAndParameterArgument1(app.getId(),profileId,"%"+tempId+"%",fDate,tDate);
    		log.info("jobDetails :"+jobDetails);
    		int totalCount=0;
    		for(BigInteger job:jobDetails)
    		{
    			

    			log.info("in while");
    			List<SchedulerDetails> sch=schedulerDetailsRepository.findByJobIdAndStartDateAndEndDate(job,fDate+"%",tDate+"%");
    			log.info("sch :"+sch);
    			for(int i=0;i<sch.size();i++)
    			{
    				if(sch.get(i).getFrequency().equalsIgnoreCase("ondemand") || sch.get(i).getFrequency().equalsIgnoreCase("onetime") )
    					totalCount=totalCount+1;
    				if(sch.get(i).getFrequency().equalsIgnoreCase("hourly"))
    				{
    					log.info("sch.get(i).getHours() :"+sch.get(i).getHours());
    					/*Long subCount=24/sch.get(i).getHours();
    					log.info("subCount1 :"+subCount);*/
    					//	int numDays=sch.get(i).getStartDate().minusDays(sch.get(i).getEndDate().getDayOfMonth())

    					if(toDate.isBefore(sch.get(i).getEndDate()))
    					{
    						Duration duration = Duration.between( sch.get(i).getStartDate() , toDate );
    						log.info("duration1hr :"+duration.toHours());
    						Long totalRuns=duration.toHours()/sch.get(i).getHours();
    						log.info("totalRuns1 :"+totalRuns);
    						totalCount=totalCount+totalRuns.intValue();
    						log.info("duration1days :"+duration.toDays());
    					}
    					else if(toDate.isAfter(sch.get(i).getEndDate()))
    					{
    						Duration duration = Duration.between( sch.get(i).getStartDate() , sch.get(i).getEndDate() );
    						log.info("duration2hr :"+duration.toHours());
    						Long totalRuns=duration.toHours()/24;
    						log.info("totalRuns2 :"+totalRuns);
    						totalCount=totalCount+totalCount;
    						log.info("duration2days :"+duration.toDays());
    					}
    				}
    				if(sch.get(i).getFrequency().equalsIgnoreCase("minutes"))
    				{
    					log.info("sch.get(i).getMinutes :"+sch.get(i).getMinutes());
    					/*Long subCount=24/sch.get(i).getHours();
    					log.info("subCount1 :"+subCount);*/
    					//	int numDays=sch.get(i).getStartDate().minusDays(sch.get(i).getEndDate().getDayOfMonth())

    					if(toDate.isBefore(sch.get(i).getEndDate()))
    					{
    						Duration duration = Duration.between( sch.get(i).getStartDate() , toDate );
    						log.info("duration1min :"+duration.toMinutes());
    						Long totalRuns=duration.toMinutes()/sch.get(i).getMinutes();
    						log.info("totalRuns1 :"+totalRuns);
    						totalCount=totalCount+totalCount;
    						log.info("duration1days :"+duration.toDays());
    					}
    					else if(toDate.isAfter(sch.get(i).getEndDate()))
    					{
    						Duration duration = Duration.between( sch.get(i).getStartDate() , sch.get(i).getEndDate() );
    						log.info("duration2min :"+duration.toMinutes());
    						Long totalRuns=duration.toMinutes()/sch.get(i).getMinutes();
    						log.info("totalRuns2 :"+totalRuns);
    						totalCount=totalCount+totalCount;
    						log.info("duration2days :"+duration.toDays());
    					}
    				}
    				if(sch.get(i).getFrequency().equalsIgnoreCase("weekly"))
    				{

    					log.info("sch.get(i).getWeekDay() :"+sch.get(i).getWeekDay());
    					if(toDate.isBefore(sch.get(i).getEndDate()))
    					{
    						Long weeks=ChronoUnit.WEEKS.between( sch.get(i).getStartDate() , toDate );
    					     log.info("weeks :"+weeks);
    						totalCount=totalCount+weeks.intValue();
    						//log.info("duration1days :"+duration.toDays());
    					}
    					else if(toDate.isAfter(sch.get(i).getEndDate()))
    					{
    						Long weeks=ChronoUnit.WEEKS.between( sch.get(i).getStartDate() , toDate );
   					     log.info("weeks :"+weeks);
    						
    						totalCount=totalCount+weeks.intValue();
    						
    					}
    				
    				}
    				if(sch.get(i).getFrequency().equalsIgnoreCase("MONTHLY"))
    				{

    					log.info("sch.get(i).getWeekDay() :"+sch.get(i).getWeekDay());
    					if(toDate.isBefore(sch.get(i).getEndDate()))
    					{

    						Long months=ChronoUnit.MONTHS.between( sch.get(i).getStartDate() , toDate );
    						log.info("months :"+months);
    						if(months==0)
    							months=1l;
    						totalCount=totalCount+months.intValue();
    						//log.info("duration1days :"+duration.toDays());
    					}
    					else if(toDate.isAfter(sch.get(i).getEndDate()))
    					{
    						Long months=ChronoUnit.MONTHS.between( sch.get(i).getStartDate() , toDate );
    						log.info("months :"+months);
    						if(months==0)
    							months=1l;
    						totalCount=totalCount+months.intValue();

    					}

    				}
    				
    				
    				
    				log.info("count :"+totalCount);
    				

    			}
    		}
    		
    		LinkedHashMap map=new LinkedHashMap();
			map.put("totalCount",totalCount );
    
    	return map;

    }
    
    
    @PostMapping("/getTotalCountForTransformation")
    @Timed
    public List<LinkedHashMap> getTotalCountForTransformation(@RequestBody HashMap agingAnalysisParameters,@RequestParam Long tenantId) 
    {
    	List<LinkedHashMap> finalMap=new ArrayList<LinkedHashMap>();
    	ZonedDateTime fmDate=ZonedDateTime.parse(agingAnalysisParameters.get("startDate").toString());
    	ZonedDateTime toDate=ZonedDateTime.parse(agingAnalysisParameters.get("endDate").toString());
    	log.info("fmDate :"+fmDate);
    	log.info("toDate :"+toDate);
    	java.time.LocalDate fDate=fmDate.toLocalDate();
    	java.time.LocalDate tDate=toDate.toLocalDate();
    	log.info("fDate :"+fDate);
    	log.info("tDate :"+tDate);

    	Properties props =  propertiesUtilService.getPropertiesFromClasspath("File.properties");

    	String profiles=props.getProperty("profiles");
    	log.info("profiles :"+profiles);
    	Pattern pattern = Pattern.compile("\\d+");
    	Matcher matcher = pattern.matcher(profiles);

    	List<Long> profileId = new ArrayList<Long>();

    	 while (matcher.find()) {
    	System.out.println(matcher.group());
    	profileId.add(Long.parseLong(matcher.group())); // Add the value to the list
    	}
    	log.info("profileId Long:"+profileId);
    	
    	while(fDate.isBefore(tDate.plusDays(1)))
    	{
    		log.info("in while fDate :"+fDate);
    		LinkedHashMap map=new LinkedHashMap();
    		map.put("date",fDate );
    		log.info("fDate%" +fDate+"%");;
    		List<SourceFileInbHistory> totalFilesExtracted=sourceFileInbHistoryRepository.findByProfileIdsAndFileReceivedDate(profileId,tenantId,fDate+"%");
    		log.info("totalFilesExtracted :"+totalFilesExtracted.size());
    		
    		List<SourceFileInbHistory> totalFilesTransformed=sourceFileInbHistoryRepository.findByProfileIdsAndFileReceivedDateAndStatusLoaded(profileId,tenantId,fDate+"%");
    		log.info("totalFilesTransformed :"+totalFilesTransformed);
    		map.put("totalExtractedCount", totalFilesExtracted.size());
    		map.put("totalTransFormedCount", totalFilesTransformed.size());
    		
    		finalMap.add(map);
    		fDate=fDate.plusDays(1);
    		log.info("in end of while fDate :"+fDate);
    		
    	}
		return finalMap;
    	
    }   
    
    

    
    
    /**
     * 
     * date:14th Feb 2017
     * @param dates
     * @param tenantId
     * @return
     */
    
    @PostMapping("/getTotalExtractionCountPerProfile")
    @Timed
    public List<LinkedHashMap> getTotalExtractionCountPerProfile(@RequestBody HashMap parameters) 
    {
    	List<LinkedHashMap> finalMap=new ArrayList<LinkedHashMap>();
    	ZonedDateTime fmDate=ZonedDateTime.parse(parameters.get("startDate").toString());
    	ZonedDateTime toDate=ZonedDateTime.parse(parameters.get("endDate").toString());
    	Long tenantId=Long.valueOf(parameters.get("tenantId").toString());
    	Long processId=Long.valueOf(parameters.get("processId").toString());
    	log.info("fmDate :"+fmDate);
    	log.info("toDate :"+toDate);
    	java.time.LocalDate fDate=fmDate.toLocalDate();
    	java.time.LocalDate tDate=toDate.toLocalDate();
    	log.info("fDate :"+fDate);
    	log.info("tDate :"+tDate);


    	ApplicationPrograms app=applicationProgramsRepository.findByPrgmNameAndTenantId("DataExtraction", tenantId);
    	List<BigInteger> procesDet=processDetailsRepository.findTypeIdByProcessIdAndTagType(processId, "sourceProfile");
    	log.info("procesDet profileIds :"+procesDet);
    	for(BigInteger profileId:procesDet)
    	{
    		SourceProfiles sp=sourceProfilesRepository.findOne(profileId.longValue());
    		LinkedHashMap spMap=new LinkedHashMap();
    		spMap.put("spurceProfileName",sp.getSourceProfileName());
    		log.info("profileId Long:"+profileId);
    		List<LinkedHashMap> templateMapList=new ArrayList<LinkedHashMap>();
    		List<BigInteger> tempIds=sourceProfileFileAssignmentsRepository.fetchTempIdsBySrcProfile(profileId.longValue());
    		for(BigInteger tempId:tempIds)
    		{
    			LinkedHashMap tempMap=new LinkedHashMap();
    			log.info("tempId :"+tempId);
    			FileTemplates ft=fileTemplatesRepository.findOne(tempId.longValue());
    			tempMap.put("fileTemplateName", ft.getTemplateName());
    			log.info("fDate1 :"+fDate);	
    			log.info("tDate1 :"+tDate);	

    			List<BigInteger> jobDetails=jobDetailsRepository.findByTenantIdAndProgrammIdAndParameterArgument1(app.getId(),profileId.longValue(),"%"+tempId.longValue()+"%",fDate,tDate);
    			log.info("jobDetails :"+jobDetails);
    			int totalCount=0;
    			for(BigInteger job:jobDetails)
    			{


    				log.info("in while");
    				List<SchedulerDetails> sch=schedulerDetailsRepository.findByJobIdAndStartDateAndEndDate(job,fDate+"%",tDate+"%");
    				log.info("sch :"+sch);
    				for(int i=0;i<sch.size();i++)
    				{
    					if(sch.get(i).getFrequency().equalsIgnoreCase("ondemand") || sch.get(i).getFrequency().equalsIgnoreCase("onetime") )
    						totalCount=totalCount+1;
    					if(sch.get(i).getFrequency().equalsIgnoreCase("hourly"))
    					{
    						log.info("sch.get(i).getHours() :"+sch.get(i).getHours());
    						/*Long subCount=24/sch.get(i).getHours();
    					log.info("subCount1 :"+subCount);*/
    						//	int numDays=sch.get(i).getStartDate().minusDays(sch.get(i).getEndDate().getDayOfMonth())

    						if(toDate.isBefore(sch.get(i).getEndDate()))
    						{
    							Duration duration = Duration.between( sch.get(i).getStartDate() , toDate );
    							log.info("duration days :"+duration.toDays());
    							log.info("duration1hr :"+duration.toHours());
    							Long totalRuns=duration.toHours()/sch.get(i).getHours();
    							log.info("totalRuns1 :"+totalRuns.intValue());
    							totalCount=totalCount+totalRuns.intValue();
    							log.info("duration1days :"+duration.toDays());
    						}
    						else if(toDate.isAfter(sch.get(i).getEndDate()))
    						{
    							Duration duration = Duration.between( sch.get(i).getStartDate() , sch.get(i).getEndDate() );
    							log.info("duration2hr :"+duration.toHours());
    							Long totalRuns=duration.toHours()/24;
    							log.info("totalRuns2 :"+totalRuns);
    							totalCount=totalCount+totalRuns.intValue();
    							log.info("duration2days :"+duration.toDays());
    						}
    					}
    					if(sch.get(i).getFrequency().equalsIgnoreCase("minutes"))
    					{
    						log.info("sch.get(i).getMinutes :"+sch.get(i).getMinutes());
    						/*Long subCount=24/sch.get(i).getHours();
    					log.info("subCount1 :"+subCount);*/
    						//	int numDays=sch.get(i).getStartDate().minusDays(sch.get(i).getEndDate().getDayOfMonth())

    						if(toDate.isBefore(sch.get(i).getEndDate()))
    						{
    							Duration duration = Duration.between( sch.get(i).getStartDate() , toDate );
    							log.info("duration1min :"+duration.toMinutes());
    							Long totalRuns=duration.toMinutes()/sch.get(i).getMinutes();
    							log.info("totalRuns1 :"+totalRuns);
    							totalCount=totalCount+totalRuns.intValue();
    							log.info("duration1days :"+duration.toDays());
    						}
    						else if(toDate.isAfter(sch.get(i).getEndDate()))
    						{
    							Duration duration = Duration.between( sch.get(i).getStartDate() , sch.get(i).getEndDate() );
    							log.info("duration2min :"+duration.toMinutes());
    							Long totalRuns=duration.toMinutes()/sch.get(i).getMinutes();
    							log.info("totalRuns2 :"+totalRuns);
    							totalCount=totalCount+totalRuns.intValue();
    							log.info("duration2days :"+duration.toDays());
    						}
    					}
    					if(sch.get(i).getFrequency().equalsIgnoreCase("weekly"))
    					{

    						log.info("sch.get(i).getWeekDay() :"+sch.get(i).getWeekDay());
    						if(toDate.isBefore(sch.get(i).getEndDate()))
    						{
    							Long weeks=ChronoUnit.WEEKS.between( sch.get(i).getStartDate() , toDate );
    							log.info("weeks :"+weeks);
    							totalCount=totalCount+weeks.intValue();
    							//log.info("duration1days :"+duration.toDays());
    						}
    						else if(toDate.isAfter(sch.get(i).getEndDate()))
    						{
    							Long weeks=ChronoUnit.WEEKS.between( sch.get(i).getStartDate() , toDate );
    							log.info("weeks :"+weeks);

    							totalCount=totalCount+weeks.intValue();

    						}

    					}
    					if(sch.get(i).getFrequency().equalsIgnoreCase("MONTHLY"))
    					{

    						log.info("sch.get(i).getWeekDay() :"+sch.get(i).getWeekDay());
    						if(toDate.isBefore(sch.get(i).getEndDate()))
    						{

    							Long months=ChronoUnit.MONTHS.between( sch.get(i).getStartDate() , toDate );
    							log.info("months :"+months);
    							if(months==0)
    								months=1l;
    							totalCount=totalCount+months.intValue();
    							//log.info("duration1days :"+duration.toDays());
    						}
    						else if(toDate.isAfter(sch.get(i).getEndDate()))
    						{
    							Long months=ChronoUnit.MONTHS.between( sch.get(i).getStartDate() , toDate );
    							log.info("months :"+months);
    							if(months==0)
    								months=1l;
    							totalCount=totalCount+months.intValue();

    						}

    					}



    					log.info("count :"+totalCount);


    				}
    			}

    			tempMap.put("templateRunsCount", totalCount);
    			templateMapList.add(tempMap);
    		}
    		spMap.put("templateList", templateMapList);
    		finalMap.add(spMap);
    	}

    	return finalMap;

    }
    
    
    @PostMapping("/EachDayAnalysisForTotalExtractionCountForProfile")
    @Timed
    public List<LinkedHashMap> EachDayAnalysisForTotalExtractionCountForProfile(@RequestBody HashMap parameters) throws SQLException 
    {
    	List<LinkedHashMap> finalMap=new ArrayList<LinkedHashMap>();
       	Long tenantId=Long.valueOf(parameters.get("tenantId").toString());
    	Long processId=Long.valueOf(parameters.get("processId").toString());
    	ZonedDateTime fmDate=ZonedDateTime.parse(parameters.get("startDate").toString());
    	ZonedDateTime toDate=ZonedDateTime.parse(parameters.get("endDate").toString());
    	log.info("fmDate :"+fmDate);
    	log.info("toDate :"+toDate);
    	java.time.LocalDate fDate=fmDate.toLocalDate();
    	java.time.LocalDate tDate=toDate.toLocalDate();
    	log.info("fDate :"+fDate);
    	log.info("tDate :"+tDate);


    	ApplicationPrograms app=applicationProgramsRepository.findByPrgmNameAndTenantId("DataExtraction", tenantId);
    	List<BigInteger> procesDet=processDetailsRepository.findTypeIdByProcessIdAndTagType(processId, "sourceProfile");
    	log.info("procesDet profileIds :"+procesDet);
    	
    	while(tDate.plusDays(1).isAfter(fDate))
    	{
    		log.info("in while tDate :"+tDate.toString());
    		LinkedHashMap map=new LinkedHashMap();
    		map.put("date", tDate);
    		int finalCount=0;
    		int extractedCount=0;
    		log.info("finalCount :"+finalCount);

    		for(BigInteger profileId:procesDet)
    		{

    			SourceProfiles sp=sourceProfilesRepository.findOne(profileId.longValue());
    			LinkedHashMap spMap=new LinkedHashMap();
    			spMap.put("spurceProfileName",sp.getSourceProfileName());
    			log.info("profileId Long:"+profileId);
    			List<LinkedHashMap> templateMapList=new ArrayList<LinkedHashMap>();
    			List<BigInteger> tempIds=sourceProfileFileAssignmentsRepository.fetchTempIdsBySrcProfile(profileId.longValue());
    			for(BigInteger tempId:tempIds)
    			{
    				LinkedHashMap tempMap=new LinkedHashMap();
    				log.info("tempId :"+tempId);
    				FileTemplates ft=fileTemplatesRepository.findOne(tempId.longValue());
    				tempMap.put("fileTemplateName", ft.getTemplateName());
    				log.info("fDate1 :"+fDate);	
    				log.info("tDate1 :"+tDate);	

    				List<BigInteger> jobDetails=jobDetailsRepository.findByTenantIdAndProgrammIdAndParameterArgument1In(app.getId(),profileId.longValue(),"%"+tempId.longValue()+"%",tDate+"%");
    				log.info("jobDetails :"+jobDetails);

    				for(BigInteger job:jobDetails)
    				{


    					log.info("in while");
    					List<SchedulerDetails> sch=schedulerDetailsRepository.findByJobIdIn(job);
    					log.info("sch :"+sch);
    					for(int i=0;i<sch.size();i++)
    					{

    						if(sch.get(i).getFrequency().equalsIgnoreCase("hourly"))
    						{
    							log.info("sch.get(i).getHours() :"+sch.get(i).getHours());

    							log.info("tDate.getDayOfWeek()1 :"+tDate.getDayOfWeek());
    							if(toDate.isBefore(sch.get(i).getEndDate()))
    							{
    								Long totalRuns=24/sch.get(i).getHours();
    								log.info("totalRuns1 :"+totalRuns.intValue());
    								finalCount=finalCount+totalRuns.intValue();
    								int extractedFileCount=getOutOfCountForProfileExtraction(tDate,sch.get(i).getOozieJobId());
    								extractedCount=extractedCount+extractedFileCount;

    							}

    						}
    						if(sch.get(i).getFrequency().equalsIgnoreCase("minutes"))
    						{
    							log.info("sch.get(i).getMinutes :"+sch.get(i).getMinutes());

    							if(toDate.isBefore(sch.get(i).getEndDate()))
    							{
    								Long totalRuns=24*60/sch.get(i).getMinutes();
    								log.info("totalRuns1 :"+totalRuns);
    								finalCount=finalCount+totalRuns.intValue();
    								int extractedFileCount=getOutOfCountForProfileExtraction(tDate,sch.get(i).getOozieJobId());
    								extractedCount=extractedCount+extractedFileCount;

    							}

    						}

    						if(sch.get(i).getFrequency().equalsIgnoreCase("Daily"))
    						{
    							log.info("sch.get(i).getMinutes :"+sch.get(i).getMinutes());

    							if(toDate.isBefore(sch.get(i).getEndDate()))
    							{
    								Long totalRuns=1l;
    								log.info("totalRuns1 :"+totalRuns);
    								finalCount=finalCount+totalRuns.intValue();
    								int extractedFileCount=getOutOfCountForProfileExtraction(tDate,sch.get(i).getOozieJobId());
    								extractedCount=extractedCount+extractedFileCount;

    							}

    						}
    						if(sch.get(i).getFrequency().equalsIgnoreCase("weekly"))
    						{

    							log.info("sch.get(i).getWeekDay() :"+sch.get(i).getWeekDay());
    							String weekDay=tDate.getDayOfWeek().toString();
    							String day=weekDay.subSequence(0, 3).toString();
    							log.info("tDate.getDayOfWeek() :"+weekDay.subSequence(0, 3));
    							if(sch.get(i).getWeekDay().equalsIgnoreCase(day))
    							{
    								if(toDate.isBefore(sch.get(i).getEndDate()))
    								{
    									Long weeks=1l;

    									finalCount=finalCount+weeks.intValue();
    									int extractedFileCount=getOutOfCountForProfileExtraction(tDate,sch.get(i).getOozieJobId());
    									extractedCount=extractedCount+extractedFileCount;

    								}
    							}

    						}
    						if(sch.get(i).getFrequency().equalsIgnoreCase("MONTHLY"))
    						{

    							log.info("sch.get(i).getMonth() :"+sch.get(i).getMonth());
    							String month=tDate.getMonth().toString();
    							String mon=month.subSequence(0, 3).toString();
    							log.info("tDate.getMonth() :"+mon.subSequence(0, 3));
    							if(sch.get(i).getMonth().equalsIgnoreCase(mon))
    							{
    								if(toDate.isBefore(sch.get(i).getEndDate()))
    								{

    									Long months=1l;
    									finalCount=finalCount+months.intValue();
    									int extractedFileCount=getOutOfCountForProfileExtraction(tDate,sch.get(i).getOozieJobId());
    									extractedCount=extractedCount+extractedFileCount;

    								}
    							}


    						}


    						if(sch.get(i).getFrequency().equalsIgnoreCase("ONETIME"))
    						{

    							log.info("sch.get(i).getDate() :"+sch.get(i).getDate());

    							if(sch.get(i).getDate().equals(tDate))
    							{


    								Long oneTime=1l;
    								finalCount=finalCount+oneTime.intValue();
    								int extractedFileCount=getOutOfCountForProfileExtraction(tDate,sch.get(i).getOozieJobId());
    								extractedCount=extractedCount+extractedFileCount;


    							}


    						}


    					}


    				}

    			}

    		}

    		map.put("count", finalCount);
    		map.put("extractedCount", extractedCount);

    		finalMap.add(map);
    		tDate=tDate.minusDays(1);
    		log.info("end of while fDate :"+fDate);
    	}

    	return finalMap;

    }
    
    /**
     * author:ravali
     * Feb16-Ping Oozie jobId
     * @param dates
     * @param tenantId
     * @param processId
     * @return
     * @throws SQLException 
     */
    @PostMapping("/getOutOfCountForProfileExtraction")
    @Timed
    public int getOutOfCountForProfileExtraction(@RequestBody java.time.LocalDate tDate,@RequestParam String oozieJobId) throws SQLException 
    {
    	
     	log.info("Request Rest to fetch schedulers list with oozieJobId"+oozieJobId);
    	log.info("Request Rest to local date"+tDate);
    	List<SchedulerDetails> schList = new ArrayList<SchedulerDetails>();
    	//to get the total count and adding attribute to response header
    	
    	List<HashMap> finalSchList=new ArrayList<HashMap>();

    	String oozieUrl=env.getProperty("oozie.OozieClient");
    	//	Properties props = propertiesUtilService.getPropertiesFromClasspath("File.properties");
    	String DB_URL = env.getProperty("oozie.ozieUrl");
    	String USER = env.getProperty("oozie.ozieUser");
    	String PASS = env.getProperty("oozie.oziePswd");
    	String schema = env.getProperty("oozie.ozieSchema");

    	Connection conn =null;
    	conn = DriverManager.getConnection(DB_URL, USER, PASS);
    	Statement stmt = null;
    	
    	stmt = conn.createStatement();
    	
    	ResultSet result2=null;
    	int totalCount =0;
	
    	
    	String query="select count(job_id) from "+schema+".recon_oozie_jobs_v where parent_id ='"+oozieJobId+"' and job_id !='"+oozieJobId+"' and '"+tDate+"%'"+" = Date(start_time)";
    	log.info("query :"+query);
			result2=stmt.executeQuery(query);
			while(result2.next())
	    	{
				totalCount=Integer.parseInt(result2.getString("count(job_id)").toString());
	    	}
			
			
	    	if(conn!=null)
	    		conn.close();
	    	if(stmt!=null)
	    		stmt.close();
	    	if(result2!=null)
	    		result2.close();
	  
			
			log.info("totalCount :"+totalCount);
			return totalCount;
		
    }
    
   
    
    
}
