package com.nspl.app.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.domain.ApplicationPrograms;
import com.nspl.app.domain.ProcessDetails;
import com.nspl.app.domain.SchedulerDetails;
import com.nspl.app.repository.AccountedSummaryRepository;
import com.nspl.app.repository.AppModuleSummaryRepository;
import com.nspl.app.repository.ApplicationProgramsRepository;
import com.nspl.app.repository.JobDetailsRepository;
import com.nspl.app.repository.ProcessDetailsRepository;
import com.nspl.app.repository.ReconciliationResultRepository;
import com.nspl.app.repository.SchedulerDetailsRepository;
import com.nspl.app.repository.SourceFileInbHistoryRepository;
import com.nspl.app.web.rest.JournalsHeaderDataResource;


@Service
public class DashBoardV2Service {

	 private final Logger log = LoggerFactory.getLogger(DashBoardV2Service.class);
	 
	 @Inject
	 ReconciliationResultRepository reconciliationResultRepository;
	 
	 @Autowired
	   	Environment env;
	 
	 @Inject
	 AccountedSummaryRepository accountedSummaryRepository;
	 
	 @Inject
	 UserJdbcService userJdbcService;
	 
	 @Inject
	 ProcessDetailsRepository processDetailsRepository;
	 
	 @Inject
	 SourceFileInbHistoryRepository sourceFileInbHistoryRepository;
	 
	 @Inject
	 AppModuleSummaryRepository appModuleSummaryRepository;
	 
	 
	 @Inject
	 ApplicationProgramsRepository applicationProgramsRepository;
	 
	 @Inject
	 JobDetailsRepository jobDetailsRepository;
	 
	 @Inject
	 SchedulerDetailsRepository schedulerDetailsRepository;
	
	public String dateFormat(String date) throws ParseException
	{
	 DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	  Date fdate = df.parse(date);
	  df = new SimpleDateFormat("dd-MMM-yy");
	  log.info("df :"+df.format(fdate));
	  String[] dt=df.format(fdate).toString().split("-");
	  log.info("df[1] :"+dt[1]);
	return dt[1];
	}
	
	
	public String[] dateSpecifiedFormat(String date) throws ParseException
	{
	 DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	  Date fdate = df.parse(date);
	  df = new SimpleDateFormat("dd-MMM-yyyy");
	  log.info("df :"+df.format(fdate));
	  String[] dt=df.format(fdate).toString().split("-");
	  log.info("df[1] :"+dt[1]);
	return dt;
	}
	
	public Long violationCount(Long tenantId,Long grpId,Long dvId,int violation,LocalDate fDate,LocalDate tDate,String dvName,String module) throws ParseException, SQLException
	{

		log.info("service to fetch violation count for a group ");


		List<BigInteger> finalSrcIdList=new ArrayList<BigInteger>();
		if(module.equalsIgnoreCase("reconciliation"))
		{
			List<BigInteger> reconciliedSrcIds=reconciliationResultRepository.fetchReconciledSourceIds(tenantId, grpId,dvId);	 
			finalSrcIdList.addAll(reconciliedSrcIds);
			List<BigInteger> reconciliedTrgIds=reconciliationResultRepository.fetchReconciledTargetIds(tenantId, grpId,dvId);	 
			finalSrcIdList.addAll(reconciliedTrgIds);
		}
		else
		{
			List<BigInteger> accountedViewIds=accountedSummaryRepository.fetchAccountedRowIdsByRuleGrpIdAndViewId(grpId, dvId) ;
			finalSrcIdList.addAll(accountedViewIds);
		}


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



		conn = DriverManager.getConnection(dbUrl, userName, password);
		log.info("Connected database successfully...");
		stmtDv = conn.createStatement();

		ResultSet resultDv=null;



		String finalSrcIds=finalSrcIdList.toString().replaceAll("\\[", "").replaceAll("\\]", "");

		String query="select DATEDIFF( SYSDATE(), `v`.`fileDate`) as `rule_age`,count(scrIds) from "+schemaName+"."+dvName.toLowerCase().toLowerCase()+" v where fileDate between '"+fDate+"' and '"+tDate+"' "
				+ " and scrIds not in ("+finalSrcIds+")group by rule_age";


		//log.info("query :"+query);

		resultDv=stmtDv.executeQuery(query);
		ResultSetMetaData rsmd2 = resultDv.getMetaData();


		Long violationCount=0l;
		while(resultDv.next())
		{
			//log.info("resultDv.getString(count(scrIds)).toString() :"+resultDv.getString("count(scrIds)").toString());
			if(Long.valueOf(resultDv.getString("rule_age").toString())>violation);
			violationCount=violationCount+Long.valueOf(resultDv.getString("count(scrIds)").toString());
		}
		log.info("violationCount :"+violationCount);
		if(resultDv!=null)
			resultDv.close();
		if(stmtDv!=null)
			stmtDv.close();
		if(conn!=null)
			conn.close();
		return violationCount;

	}
	
	
	
	
	
	/* @PostMapping("/transformationAnalysisforGivenPeriod")
     @Timed*/
     public LinkedHashMap transformationAnalysisforGivenPeriod(Long processId,HashMap dates,HttpServletRequest request) throws SQLException, ParseException 
     {
    	 log.info("Rest Request to get aging analysis for transformation:"+dates);



    	 HashMap map=userJdbcService.getuserInfoFromToken(request);
    	 Long tenantId=Long.parseLong(map.get("tenantId").toString());


    	 LinkedHashMap eachMap=new LinkedHashMap();

    	 if(processId!=null)

    	 {


    		 List<BigInteger> profileId=processDetailsRepository.findTypeIdByProcessIdAndTagType(processId, "sourceProfile");


    		 ZonedDateTime fmDate=ZonedDateTime.parse(dates.get("startDate").toString());

    		 ZonedDateTime toDate=ZonedDateTime.parse(dates.get("endDate").toString());

    		 log.info("fmDate :"+fmDate);

    		 log.info("toDate :"+toDate);

    		 LocalDate fDate=fmDate.toLocalDate();

    		 LocalDate tDate=toDate.toLocalDate();

    		 log.info("fDate :"+fDate);

    		 log.info("tDate :"+tDate);
    		 /**Transformation 1w**/


    		 log.info("fDate :"+fDate);

    		 log.info("tDate :"+tDate);
    		 LocalDate till1wDate=tDate;
    		 LocalDate till1wDateFrLoop=tDate;
    		 log.info("tillDate :"+till1wDate);
    		 LocalDate from1wDate=tDate.minusDays(7);
    		 log.info(" in if 1W fromDate :"+from1wDate);
    		 log.info(" in if fDate :"+fDate);

    		 log.info("fromDate befor query :"+from1wDate);

    		 log.info("tillDate befor query :"+till1wDateFrLoop);

    		 Long days=Duration.between(fDate.atStartOfDay(), tDate.atStartOfDay()).toDays();
    		 if(days<=7 || days>=7)
    		 {

    			 log.info("for one week");
    			 //log.info("fDate :"+fDate);

    			// log.info("tDate :"+tDate);


    			 List<LocalDate> datesList=new ArrayList<LocalDate>();


    			 List<String> labelValue=new ArrayList<String>();

    			 List<String> labelDates=new ArrayList<String>();

    			 List<Double> transformed=new ArrayList<Double>();

    			 List<Double> transformationFailed=new ArrayList<Double>();

    			 List<Double> transformedPer=new ArrayList<Double>();

    			 List<Double> transformationFailedPer=new ArrayList<Double>();

    			 if(days<7)
    			 {
    				 while(till1wDateFrLoop.isAfter(fDate.minusDays(1))){
    					 datesList.add(till1wDateFrLoop);
    					 till1wDateFrLoop=till1wDateFrLoop.minusDays(1);
    					 System.out.println("end of while till1wDateFrLoop :"+till1wDateFrLoop);
    				 }
    			 }
    			 else
    			 {
    				 while(till1wDateFrLoop.isAfter(from1wDate)){
    					 datesList.add(till1wDateFrLoop);
    					 till1wDateFrLoop=till1wDateFrLoop.minusDays(1);
    					 System.out.println("end of while till1wDateFrLoop :"+till1wDateFrLoop);
    				 }
    			 }
    			 //log.info("datesList :"+datesList);


    			 /*List<Long> profileIds=new ArrayList<Long>();
    			for(BigInteger profile:profileId)
    			{
    				profileIds.add(profile.longValue());
    			}*/
    			 log.info("fromDate befor query :"+from1wDate);

    			 log.info("tillDate befor query :"+till1wDateFrLoop);
    			 List<Object[]> totalFilesExtracted=new ArrayList<Object[]>();
    			 if(days<7)
    			 {
    				 log.info("in if less than 7days transformation :"+fDate+tDate);
    				 totalFilesExtracted=sourceFileInbHistoryRepository.fetchTransfomedCountBetweenGivenDate(profileId,fDate.minusDays(1)+"%",tDate+"%");
    			 }
    			 else
    				 totalFilesExtracted=sourceFileInbHistoryRepository.fetchTransfomedCountBetweenGivenDate(profileId,from1wDate+"%",till1wDate+"%");
    			 log.info("totalFilesExtracted size :"+totalFilesExtracted.size());

    			 //List<LinkedHashMap> datesMapList=new ArrayList<LinkedHashMap>();

    			 // Double totalExtracted=0d;

    			 Double totalTransformed=0d;

    			 Double totalTransFailedCt=0d;

    			 Double totalTransformedPer=0d;

    			 Double totalTransFailedPer=0d;


    			 /* Double totalUnReconAmt=0d;

    			 Double totalReconAmtPer=0d;

    			 Double totalUnReconPer=0d;*/

    			 LinkedHashMap datesMap=new LinkedHashMap();

    			 for(int i=0;i<totalFilesExtracted.size();i++)

    			 {

    				 if(totalFilesExtracted!=null)

    				 {

    					 /*log.info("reconSummary.get(i)[8].toString() :"+reconSummary.get(i)[8].toString());

    					 log.info("reconSummary.get(i)[2].toString() :"+reconSummary.get(i)[2].toString());

    					 log.info("reconSummary.get(i)[3].toString() :"+reconSummary.get(i)[3].toString());*/

    					 totalTransFailedCt=totalTransFailedCt+Double.valueOf(totalFilesExtracted.get(i)[5].toString());

    					 totalTransformed=totalTransformed+Double.valueOf(totalFilesExtracted.get(i)[1].toString());

    					 totalTransformedPer=totalTransformedPer+Double.valueOf(totalFilesExtracted.get(i)[2].toString());

    					 totalTransFailedPer=totalTransFailedPer+Double.valueOf(totalFilesExtracted.get(i)[3].toString());


    					 datesMap.put(totalFilesExtracted.get(i)[4].toString(), totalFilesExtracted.get(i)[1].toString()+","+totalFilesExtracted.get(i)[5].toString()+","+
    							 totalFilesExtracted.get(i)[2].toString()+","+totalFilesExtracted.get(i)[3].toString());



    				 }

    			 }


    			// log.info("datesMap :"+datesMap);



    			 for(int d=0;d<datesList.size();d++)

    			 {
    				
    					 //log.info("datesList.get(d) transformation:"+datesList.get(d));

    					 LocalDate dt=LocalDate.parse(datesList.get(d).toString());

    					 String month=dateFormat(datesList.get(d).toString());

    					 labelValue.add(dt.getDayOfMonth()+" "+month);

    					 labelDates.add(datesList.get(d).toString());

    					 if(datesMap.get(datesList.get(d).toString())!=null)

    					 {

    						 // DateFormatSymbols().getMonths()[month]

    						 log.info("in if datesList.get(d)key :"+datesMap.get(datesList.get(d).toString()));

    						 String[] split=datesMap.get(datesList.get(d).toString()).toString().split(",");

    						 log.info("split [0]"+split[0]);

    						 log.info("split [1]"+split[1]);

    						 transformed.add(Double.valueOf(split[0]));

    						 transformationFailed.add(Double.valueOf(split[1]));

    						 transformedPer.add(Double.valueOf(split[2]));

    						 transformationFailedPer.add(Double.valueOf(split[3]));

    					 }

    					 else

    					 {

    						 log.info("in else");

    						 Double l=0d;

    						 transformed.add(l);

    						 transformationFailed.add(l);

    						 transformedPer.add(l);

    						 transformationFailedPer.add(l);

    					

    				 }

    			 }

    			// log.info("labelValue :"+labelValue);

    			// log.info("recon :"+transformed);

    			// log.info("unRecon :"+transformationFailed);

    			 LinkedHashMap map1=new LinkedHashMap();

    			 map1.put("labelValue", labelValue);

    			 map1.put("labelDates", labelDates);

    			 map1.put("transformed", transformed);

    			 map1.put("transformationFailed", transformationFailed);

    			 map1.put("transformedPer", transformedPer);

    			 map1.put("transformationFailedPer", transformationFailedPer);



    			 // weekMap

    			 LinkedHashMap wmap=new LinkedHashMap();


    			 /*	 wmap.put("reconAmount", totalReconAmt);

    			 wmap.put("reconPercentage", totalReconAmtPer);*/

    			 wmap.put("totalTransformed", totalTransformed);

    			 wmap.put("totalTransFailedCt", totalTransFailedCt);


    			 wmap.put("totalTransformedPer", totalTransformedPer);

    			 wmap.put("totalTransFailedPer", totalTransFailedPer);

    			 wmap.put("detailedData", map1);

    			 eachMap.put("1W", wmap);

    			 //	 finalMap.add(weekMap);

    		 }

    		 /** transformation 2w**/

    		 log.info("for 2 week");

    		 LocalDate from2WDate=tDate.minusDays(14);

    		 LocalDate till2WDate=tDate;
    		 LocalDate till2WDateFrLoop=tDate;
    		 log.info("from2WDate for 2 week:"+from2WDate);
    		 log.info("till2WDate :"+till2WDate);

    		 if(days>=7)
    		 {

    			 List<LocalDate> datesList=new ArrayList<LocalDate>();

    			 log.info("from2WDate :"+from2WDate);

    			 List<String> labelValue=new ArrayList<String>();

    			 List<String> labelDates=new ArrayList<String>();

    			 List<Double> transformed=new ArrayList<Double>();

    			 List<Double> transformationFailed=new ArrayList<Double>();

    			 List<Double> transformedPer=new ArrayList<Double>();

    			 List<Double> transformationFailedPer=new ArrayList<Double>();


    			 if(days>=7 && days<14)
    			 {
    				 while(till2WDateFrLoop.isAfter(fDate.minusDays(1))){
    					 datesList.add(till2WDateFrLoop);
    					 till2WDateFrLoop=till2WDateFrLoop.minusDays(1);
    					 System.out.println("end of while till1wDateFrLoop :"+till2WDateFrLoop);
    				 }
    			 }
    			 else
    			 {
    				 while(till2WDateFrLoop.isAfter(from2WDate)){
        				 datesList.add(till2WDateFrLoop);
        				 till2WDateFrLoop=till2WDateFrLoop.minusDays(1);
        				 System.out.println("end of while till2WDateFrLoop :"+till2WDateFrLoop);
        			 }
    			 }
    			 
    			 
    			 

    			/* while(till2WDateFrLoop.isAfter(from2WDate)){
    				 datesList.add(till2WDateFrLoop);
    				 till2WDateFrLoop=till2WDateFrLoop.minusDays(1);
    				 System.out.println("end of while till2WDateFrLoop :"+till2WDateFrLoop);
    			 }*/


    			// log.info("datesList :"+datesList);

    			 log.info("from2WDate befor query :"+from2WDate);

    			 log.info("till2WDate befor query :"+till2WDate);
    			 List<Object[]> totalFilesExtracted=new ArrayList<Object[]>();
    			 if(days>=7 && days<14)
    			 {
    				 log.info("in if less than 7days totalFilesExtracted :"+fDate+tDate);
    				 totalFilesExtracted=sourceFileInbHistoryRepository.fetchTransfomedCountBetweenGivenDate(profileId,fDate.minusDays(1)+"%",tDate+"%");
    			 }
    			 else
    			 totalFilesExtracted=sourceFileInbHistoryRepository.fetchTransfomedCountBetweenGivenDate(profileId,from2WDate+"%",till2WDate+"%");

    			 log.info("totalFiles2wExtracted size :"+totalFilesExtracted.size());

    			 //List<LinkedHashMap> datesMapList=new ArrayList<LinkedHashMap>();

    			 Double totalTransFailedCt=0d;

    			 Double totalTransformed=0d;


    			 Double totalTransformedPer=0d;

    			 Double totalTransFailedPer=0d;

    			 /* Double totalUnReconAmt=0d;

    			 Double totalReconAmtPer=0d;

    			 Double totalUnReconPer=0d;*/

    			 LinkedHashMap datesMap=new LinkedHashMap();

    			 for(int i=0;i<totalFilesExtracted.size();i++)

    			 {

    				 if(totalFilesExtracted!=null)

    				 {

    					 /*log.info("reconSummary.get(i)[8].toString() :"+reconSummary.get(i)[8].toString());

    					 log.info("reconSummary.get(i)[2].toString() :"+reconSummary.get(i)[2].toString());

    					 log.info("reconSummary.get(i)[3].toString() :"+reconSummary.get(i)[3].toString());*/

    					 totalTransFailedCt=totalTransFailedCt+Double.valueOf(totalFilesExtracted.get(i)[5].toString());

    					 totalTransformed=totalTransformed+Double.valueOf(totalFilesExtracted.get(i)[1].toString());

    					 totalTransformedPer=totalTransformedPer+Double.valueOf(totalFilesExtracted.get(i)[2].toString());

    					 totalTransFailedPer=totalTransFailedPer+Double.valueOf(totalFilesExtracted.get(i)[3].toString());



    					 datesMap.put(totalFilesExtracted.get(i)[4].toString(), totalFilesExtracted.get(i)[1].toString()+","+totalFilesExtracted.get(i)[5].toString()+","+
    							 totalFilesExtracted.get(i)[2].toString()+","+totalFilesExtracted.get(i)[3].toString());



    				 }

    			 }




    			 /* log.info("totalReconAmt :"+totalReconAmt); 

    			 log.info("totaldvAmt :"+totaldvAmt); 

    			 log.info("totalReconAmtPer :"+totalReconAmtPer); 

    			 log.info("totalUnReconPer :"+totalUnReconPer); */

    			 // totalUnReconAmt=totaldvAmt-totalReconAmt;

    			 //	 log.info("totalUnReconAmt :"+totalUnReconAmt); 



    			// log.info("datesMap :"+datesMap);



    			 for(int d=0;d<datesList.size();d++)

    			 {

    				// log.info("datesList.get(d) :"+datesList.get(d));

    				 LocalDate dt=LocalDate.parse(datesList.get(d).toString());

    				 String month=dateFormat(datesList.get(d).toString());

    				 labelValue.add(dt.getDayOfMonth()+" "+month);

    				 labelDates.add(datesList.get(d).toString());

    				 if(datesMap.get(datesList.get(d).toString())!=null)

    				 {

    					 // DateFormatSymbols().getMonths()[month]

    					// log.info("in if datesList.get(d)key :"+datesMap.get(datesList.get(d).toString()));

    					 String[] split=datesMap.get(datesList.get(d).toString()).toString().split(",");

    					 log.info("split [0]"+split[0]);

    					 log.info("split [1]"+split[1]);

    					 transformed.add(Double.valueOf(split[0]));

    					 transformationFailed.add(Double.valueOf(split[1]));

    					 transformedPer.add(Double.valueOf(split[2]));

    					 transformationFailedPer.add(Double.valueOf(split[3]));

    				 }

    				 else

    				 {

    					 log.info("in else");

    					 Double l=0d;

    					 transformed.add(l);

    					 transformationFailed.add(l);

    					 transformedPer.add(l);

    					 transformationFailedPer.add(l);

    				 }



    			 }

    			// log.info("labelValue :"+labelValue);

    			// log.info("transformed :"+transformed);

    			// log.info("transformationFailed :"+transformationFailed);

    			 LinkedHashMap map2=new LinkedHashMap();

    			 map2.put("labelValue", labelValue);

    			 map2.put("labelDates", labelDates);

    			 map2.put("transformed", transformed);

    			 map2.put("transformationFailed", transformationFailed);

    			 map2.put("transformedPer", transformedPer);

    			 map2.put("transformationFailedPer", transformationFailedPer);



    			 // weekMap

    			 LinkedHashMap twomap=new LinkedHashMap();



    			 /* twomap.put("reconAmount", totalReconAmt);

    			 twomap.put("reconPercentage", totalReconAmtPer);*/

    			 twomap.put("totalTransformed", totalTransformed);

    			 twomap.put("totalTransFailedCt", totalTransFailedCt);

    			 twomap.put("totalTransformedPer", totalTransformedPer);

    			 twomap.put("totalTransFailedPer", totalTransFailedPer);

    			 twomap.put("detailedData", map2);

    			 eachMap.put("2W", twomap);

    			 //	 finalMap.add(weekMap);

    		 }




    		 /**1month**/



    		 LocalDate from6Date = null;
    		 LocalDate till6Date = null;

    		 if(days>=14)
    		 {


    			 //	 from1wDate

    			 //	till1wDate
    			// log.info("for 1month week");

    			 log.info("from1wDate :"+from1wDate);//02-10
    			 log.info("till1wDate :"+till1wDate);//02-17
    			 LocalDate from2wDate=from1wDate.minusDays(7);//02-03
    			 log.info("from2wDate :"+from2wDate);
    			 LocalDate till2wDate=from1wDate;//02-10
    			 log.info("till2wDate :"+till2wDate);
    			 LocalDate from3wDate=from2wDate.minusDays(7);//01-27
    			 log.info("from3wDate :"+from3wDate);
    			 LocalDate till3wDate=from2wDate;//02-03
    			 log.info("till3wDate :"+till3wDate);
    			 LocalDate from4wDate=from3wDate.minusDays(7);//01-20
    			 log.info("from4wDate :"+from4wDate);
    			 LocalDate till4wDate=from3wDate;//01-27
    			 log.info("till4wDate :"+till4wDate);
    			 LocalDate from5wDate=from4wDate.minusDays(2);//01-20
    			 log.info("from5wDate :"+from5wDate);
    			 LocalDate till5wDate=from4wDate;//01-27
    			 log.info("till5wDate :"+till5wDate);
    			 /**to retreive third month dates**/
    			 from6Date=from5wDate.minusDays(7);
    			 till6Date=from5wDate;
    			 log.info("for 1month week");
    			 Double totalTransFailedCt=0d;

    			 Double totalTransformed=0d;

    			 List<String> labelValue=new ArrayList<String>();
    			 List<LinkedHashMap> labelDatesValue=new ArrayList<LinkedHashMap>();

    			 List<Double> trasnformed=new ArrayList<Double>();

    			 List<Double> transformationFailed=new ArrayList<Double>();

    			 List<Double> transformedPer=new ArrayList<Double>();

    			 List<Double> transformationFailedPer=new ArrayList<Double>();
    			 Double totalTransformedPer=0d;

    			 Double totalTransFailedPer=0d;

    			 log.info("profileId :"+profileId);
    			 log.info("from1wDate before query:"+from1wDate);
    			 log.info("till1wDate before query:"+till1wDate);
    			 /**for 1st week**/
    			 List<Object[]> filesExtracted1WSummary=sourceFileInbHistoryRepository.fetchTransfomedCountBetweenGivenDateForWeek(profileId,from1wDate+"%",till1wDate+"%");

    			 if(filesExtracted1WSummary!=null)
    			 {
    				 log.info("filesExtracted1WSummary :"+filesExtracted1WSummary);
    				 log.info("filesExtracted1WSummary0 :"+filesExtracted1WSummary.get(0)[0].toString());
    				 log.info("filesExtracted1WSummary1 :"+filesExtracted1WSummary.get(0)[1].toString());
    				 log.info("filesExtracted1WSummary2 :"+filesExtracted1WSummary.get(0)[2].toString());
    				 labelDatesValue.add(datesMap(from1wDate.toString(),till1wDate.toString()));
    				 // labelDatesValue.add(from1wDate+"-"+till1wDate);
    				 labelValue.add("Week-1");
    				 if(filesExtracted1WSummary.get(0)[1]!=null)
    					 trasnformed.add(Double.valueOf(filesExtracted1WSummary.get(0)[1].toString()));
    				 else
    					 trasnformed.add(0d);
    				 if(filesExtracted1WSummary.get(0)[4]!=null)
    					 transformationFailed.add(Double.valueOf(filesExtracted1WSummary.get(0)[4].toString()));
    				 else
    					 transformationFailed.add(0d);
    				 if(filesExtracted1WSummary.get(0)[1]!=null)
    					 totalTransformed=totalTransformed+Double.valueOf(filesExtracted1WSummary.get(0)[1].toString());
    				 if(filesExtracted1WSummary.get(0)[4]!=null)
    					 totalTransFailedCt=totalTransFailedCt+Double.valueOf(filesExtracted1WSummary.get(0)[4].toString());

    			 }
    			 else
    			 {

    				 labelValue.add("Week-1");
    				 trasnformed.add(0d);
    				 transformationFailed.add(0d);
    			 }
    			 /**for 2st week**/
    			 List<Object[]> filesExtracted2WSummary=sourceFileInbHistoryRepository.fetchTransfomedCountBetweenGivenDateForWeek(profileId,from2wDate+"%",till2wDate+"%");
    			 labelDatesValue.add(datesMap(from2WDate.toString(),from2WDate.toString()));
    			 //	 labelDatesValue.add(from2WDate+"-"+till2WDate);
    			 labelValue.add("Week-2");
    			 if(filesExtracted2WSummary!=null)
    			 {
    				 if(filesExtracted2WSummary.get(0)[1]!=null)
    					 trasnformed.add(Double.valueOf(filesExtracted2WSummary.get(0)[1].toString()));
    				 else
    					 trasnformed.add(0d);
    				 if(filesExtracted2WSummary.get(0)[4]!=null)
    					 transformationFailed.add(Double.valueOf(filesExtracted2WSummary.get(0)[4].toString()));
    				 else
    					 transformationFailed.add(0d);
    				 if(filesExtracted2WSummary.get(0)[1]!=null)
    					 totalTransformed=totalTransformed+Double.valueOf(filesExtracted2WSummary.get(0)[1].toString());
    				 if(filesExtracted2WSummary.get(0)[4]!=null)
    					 totalTransFailedCt=totalTransFailedCt+Double.valueOf(filesExtracted2WSummary.get(0)[4].toString());
    			 }
    			 else
    			 {
    				 trasnformed.add(0d);
    				 transformationFailed.add(0d);
    			 }


    			 if(from3wDate.isAfter(fDate))
    			 {
    				 List<Object[]> filesExtracted3WSummary=sourceFileInbHistoryRepository.fetchTransfomedCountBetweenGivenDateForWeek(profileId,from3wDate+"%",till3wDate+"%");
    				 labelDatesValue.add(datesMap(from3wDate.toString(),till3wDate.toString()));
    				 // labelDatesValue.add(from3wDate+"-"+till3wDate);
    				 labelValue.add("Week-3");
    				 if(filesExtracted3WSummary!=null)
    				 {
    					 if(filesExtracted3WSummary.get(0)[1]!=null)
    						 trasnformed.add(Double.valueOf(filesExtracted3WSummary.get(0)[1].toString()));
    					 else
    						 trasnformed.add(0d);
    					 if(filesExtracted3WSummary.get(0)[4]!=null)
    						 transformationFailed.add(Double.valueOf(filesExtracted3WSummary.get(0)[4].toString()));
    					 else
    						 transformationFailed.add(0d);
    					 if(filesExtracted3WSummary.get(0)[1]!=null)
    						 totalTransformed=totalTransformed+Double.valueOf(filesExtracted3WSummary.get(0)[1].toString());
    					 if(filesExtracted3WSummary.get(0)[4]!=null)
    						 totalTransFailedCt=totalTransFailedCt+Double.valueOf(filesExtracted3WSummary.get(0)[4].toString());
    				 }
    				 else
    				 {
    					 trasnformed.add(0d);
    					 transformationFailed.add(0d);
    				 }
    			 }

    			 if(from4wDate.isAfter(fDate))
    			 {
    				 List<Object[]>filesExtracted4WSummary=sourceFileInbHistoryRepository.fetchTransfomedCountBetweenGivenDateForWeek(profileId,from4wDate+"%",till4wDate+"%");
    				 labelDatesValue.add(datesMap(from4wDate.toString(),till4wDate.toString()));
    				 //labelDatesValue.add(from4wDate+"-"+till4wDate);
    				 labelValue.add("Week-4");
    				 if(filesExtracted4WSummary!=null)
    				 {
    					 if(filesExtracted4WSummary.get(0)[1]!=null)
    						 trasnformed.add(Double.valueOf(filesExtracted4WSummary.get(0)[1].toString()));
    					 else
    						 trasnformed.add(0d);
    					 if(filesExtracted4WSummary.get(0)[4]!=null)
    						 transformationFailed.add(Double.valueOf(filesExtracted4WSummary.get(0)[4].toString()));
    					 else
    						 transformationFailed.add(0d);
    					 if(filesExtracted4WSummary.get(0)[1]!=null)
    						 totalTransformed=totalTransformed+Double.valueOf(filesExtracted4WSummary.get(0)[1].toString());
    					 if(filesExtracted4WSummary.get(0)[4]!=null)
    						 totalTransFailedCt=totalTransFailedCt+Double.valueOf(filesExtracted4WSummary.get(0)[4].toString());
    				 }
    				 else
    				 {
    					 trasnformed.add(0d);
    					 transformationFailed.add(0d);
    				 }
    			 }

    			 if(from5wDate.isAfter(fDate))
    			 {
    				 List<Object[]> filesExtracted5WSummary=sourceFileInbHistoryRepository.fetchTransfomedCountBetweenGivenDateForWeek(profileId,from5wDate+"%",till5wDate+"%");
    				 labelDatesValue.add(datesMap(from5wDate.toString(),till5wDate.toString()));
    				 // labelDatesValue.add(from5wDate+"-"+till5wDate);
    				 labelValue.add("Week-5");
    				 if(filesExtracted5WSummary!=null)
    				 {
    					 if(filesExtracted5WSummary.get(0)[1]!=null)
    						 trasnformed.add(Double.valueOf(filesExtracted5WSummary.get(0)[1].toString()));
    					 else
    						 trasnformed.add(0d);
    					 if(filesExtracted5WSummary.get(0)[4]!=null)
    						 transformationFailed.add(Double.valueOf(filesExtracted5WSummary.get(0)[4].toString()));
    					 else
    						 transformationFailed.add(0d);
    					 if(filesExtracted5WSummary.get(0)[1]!=null)
    						 totalTransformed=totalTransformed+Double.valueOf(filesExtracted5WSummary.get(0)[1].toString());
    					 if(filesExtracted5WSummary.get(0)[4]!=null)
    						 totalTransFailedCt=totalTransFailedCt+Double.valueOf(filesExtracted5WSummary.get(0)[4].toString());
    				 }
    				 else
    				 {
    					 trasnformed.add(0d);
    					 transformationFailed.add(0d);
    				 }

    			 }




    			 log.info("totalTransformed :"+totalTransformed); 

    			 log.info("totalTransFailedCt :"+totalTransFailedCt); 


    			 LinkedHashMap map3=new LinkedHashMap();

    			 map3.put("labelValue", labelValue);
    			 map3.put("labelDates", labelDatesValue);

    			 map3.put("transformed", trasnformed);

    			 map3.put("transformationFailed", transformationFailed);


    			 LinkedHashMap ommap=new LinkedHashMap();

    			 ommap.put("totalTransformed", totalTransformed);

    			 ommap.put("totalTransFailedCt", totalTransFailedCt);

    			 ommap.put("detailedData", map3);

    			 eachMap.put("1M", ommap);

    			 //	 finalMap.add(weekMap);

    		 }



    		 /**3month need to implement for this**/
    		 // from5wDate.isAfter(fDate)



    		 if(days>=30)
    		 {

    			 //	 from1wDate

    			 //	till1wDate
    			 log.info("for 1month week");

    			 log.info("from1wDate :"+from1wDate);//02-10
    			 log.info("till1wDate :"+till1wDate);//02-17
    			 LocalDate from2wDate=from1wDate.minusDays(7);//02-03
    			 log.info("from2wDate :"+from2wDate);
    			 LocalDate till2wDate=from1wDate;//02-10
    			 log.info("till2wDate :"+till2wDate);
    			 LocalDate from3wDate=from2wDate.minusDays(7);//01-27
    			 log.info("from3wDate :"+from3wDate);
    			 LocalDate till3wDate=from2wDate;//02-03
    			 log.info("till3wDate :"+till3wDate);
    			 LocalDate from4wDate=from3wDate.minusDays(7);//01-20
    			 log.info("from4wDate :"+from4wDate);
    			 LocalDate till4wDate=from3wDate;//01-27
    			 log.info("till4wDate :"+till4wDate);
    			 LocalDate from5wDate=from4wDate.minusDays(2);//01-20
    			 log.info("from5wDate :"+from5wDate);
    			 LocalDate till5wDate=from4wDate;//01-27
    			 log.info("till5wDate :"+till5wDate);
    			 /**to retreive third month dates**/
    			 from6Date=from5wDate.minusDays(7);
    			 till6Date=from5wDate;

    			 LocalDate from7Date=from5wDate.minusDays(7);
    			 LocalDate till7Date=from5wDate;

    			 LocalDate from8Date=from7Date.minusDays(7);
    			 LocalDate till8Date=from7Date;

    			 LocalDate from9Date=from8Date.minusDays(7);
    			 LocalDate till9Date=from8Date;
    			 LocalDate from10Date=from9Date.minusDays(7);
    			 LocalDate till10Date=from9Date;
    			 LocalDate from11Date=from10Date.minusDays(7);
    			 LocalDate till11Date=from10Date;
    			 LocalDate from12Date=from11Date.minusDays(7);
    			 LocalDate till12Date=from11Date;
    			 LocalDate from13Date=from12Date.minusDays(7);
    			 LocalDate till13Date=from12Date;
    			 log.info("for 1month week");
    			 Double totalTransFailedCt=0d;

    			 Double totalTransformed=0d;

    			 List<String> labelValue=new ArrayList<String>();
    			 List<LinkedHashMap> labelDatesValue=new ArrayList<LinkedHashMap>();

    			 List<Double> trasnformed=new ArrayList<Double>();

    			 List<Double> transformationFailed=new ArrayList<Double>();

    			 log.info("profileId :"+profileId);
    			 log.info("from1wDate before query:"+from1wDate);
    			 log.info("till1wDate before query:"+till1wDate);
    			 /**for 1st week**/
    			 List<Object[]> filesExtracted1WSummary=sourceFileInbHistoryRepository.fetchTransfomedCountBetweenGivenDateForWeek(profileId,from1wDate+"%",till1wDate+"%");

    			 if(filesExtracted1WSummary!=null)
    			 {
    				// log.info("filesExtracted1WSummary :"+filesExtracted1WSummary);
    				// log.info("filesExtracted1WSummary0 :"+filesExtracted1WSummary.get(0)[0].toString());
    				// log.info("filesExtracted1WSummary1 :"+filesExtracted1WSummary.get(0)[1].toString());
    				// log.info("filesExtracted1WSummary2 :"+filesExtracted1WSummary.get(0)[2].toString());
    				 labelDatesValue.add(datesMap(from1wDate.toString(),till1wDate.toString()));
    				 // labelDatesValue.add(from1wDate+"-"+till1wDate);
    				 labelValue.add("Week-1");
    				 if(filesExtracted1WSummary.get(0)[1]!=null)
    					 trasnformed.add(Double.valueOf(filesExtracted1WSummary.get(0)[1].toString()));
    				 else
    					 trasnformed.add(0d);
    				 if(filesExtracted1WSummary.get(0)[4]!=null)
    					 transformationFailed.add(Double.valueOf(filesExtracted1WSummary.get(0)[4].toString()));
    				 else
    					 transformationFailed.add(0d);
    				 if(filesExtracted1WSummary.get(0)[1]!=null)
    					 totalTransformed=totalTransformed+Double.valueOf(filesExtracted1WSummary.get(0)[1].toString());
    				 if(filesExtracted1WSummary.get(0)[4]!=null)
    					 totalTransFailedCt=totalTransFailedCt+Double.valueOf(filesExtracted1WSummary.get(0)[4].toString());

    			 }
    			 else
    			 {
    				 labelDatesValue.add(datesMap(from1wDate.toString(),till1wDate.toString()));
    				 // labelDatesValue.add(from1wDate+"-"+till1wDate);
    				 labelValue.add("Week-1");
    				 trasnformed.add(0d);
    				 transformationFailed.add(0d);
    			 }
    			 /**for 2st week**/
    			 List<Object[]> filesExtracted2WSummary=sourceFileInbHistoryRepository.fetchTransfomedCountBetweenGivenDateForWeek(profileId,from2wDate+"%",till2wDate+"%");
    			 labelDatesValue.add(datesMap(from2WDate.toString(),till2WDate.toString()));
    			 //	 labelDatesValue.add(from2WDate+"-"+till2WDate);
    			 labelValue.add("Week-2");
    			 if(filesExtracted2WSummary!=null)
    			 {
    				 if(filesExtracted2WSummary.get(0)[1]!=null)
    					 trasnformed.add(Double.valueOf(filesExtracted2WSummary.get(0)[1].toString()));
    				 else
    					 trasnformed.add(0d);
    				 if(filesExtracted2WSummary.get(0)[4]!=null)
    					 transformationFailed.add(Double.valueOf(filesExtracted2WSummary.get(0)[4].toString()));
    				 else
    					 transformationFailed.add(0d);
    				 if(filesExtracted2WSummary.get(0)[1]!=null)
    					 totalTransformed=totalTransformed+Double.valueOf(filesExtracted2WSummary.get(0)[1].toString());
    				 if(filesExtracted2WSummary.get(0)[4]!=null)
    					 totalTransFailedCt=totalTransFailedCt+Double.valueOf(filesExtracted2WSummary.get(0)[4].toString());
    			 }
    			 else
    			 {
    				 trasnformed.add(0d);
    				 transformationFailed.add(0d);
    			 }


    			 if(from3wDate.isAfter(fDate))
    			 {
    				 List<Object[]> filesExtracted3WSummary=sourceFileInbHistoryRepository.fetchTransfomedCountBetweenGivenDateForWeek(profileId,from3wDate+"%",till3wDate+"%");
    				 labelDatesValue.add(datesMap(from3wDate.toString(),till3wDate.toString()));
    				 // labelDatesValue.add(from3wDate+"-"+till3wDate);
    				 labelValue.add("Week-3");
    				 if(filesExtracted3WSummary!=null)
    				 {
    					 if(filesExtracted3WSummary.get(0)[1]!=null)
    						 trasnformed.add(Double.valueOf(filesExtracted3WSummary.get(0)[1].toString()));
    					 else
    						 trasnformed.add(0d);
    					 if(filesExtracted3WSummary.get(0)[4]!=null)
    						 transformationFailed.add(Double.valueOf(filesExtracted3WSummary.get(0)[4].toString()));
    					 else
    						 transformationFailed.add(0d);
    					 if(filesExtracted3WSummary.get(0)[1]!=null)
    						 totalTransformed=totalTransformed+Double.valueOf(filesExtracted3WSummary.get(0)[1].toString());
    					 if(filesExtracted3WSummary.get(0)[4]!=null)
    						 totalTransFailedCt=totalTransFailedCt+Double.valueOf(filesExtracted3WSummary.get(0)[4].toString());
    				 }
    				 else
    				 {
    					 trasnformed.add(0d);
    					 transformationFailed.add(0d);
    				 }
    			 }

    			 if(from4wDate.isAfter(fDate))
    			 {
    				 List<Object[]>filesExtracted4WSummary=sourceFileInbHistoryRepository.fetchTransfomedCountBetweenGivenDateForWeek(profileId,from4wDate+"%",till4wDate+"%");
    				 labelDatesValue.add(datesMap(from4wDate.toString(),till4wDate.toString()));
    				 // labelDatesValue.add(from4wDate+"-"+till4wDate);
    				 labelValue.add("Week-4");
    				 if(filesExtracted4WSummary!=null)
    				 {
    					 if(filesExtracted4WSummary.get(0)[1]!=null)
    						 trasnformed.add(Double.valueOf(filesExtracted4WSummary.get(0)[1].toString()));
    					 else
    						 trasnformed.add(0d);
    					 if(filesExtracted4WSummary.get(0)[4]!=null)
    						 transformationFailed.add(Double.valueOf(filesExtracted4WSummary.get(0)[4].toString()));
    					 else
    						 transformationFailed.add(0d);
    					 if(filesExtracted4WSummary.get(0)[1]!=null)
    						 totalTransformed=totalTransformed+Double.valueOf(filesExtracted4WSummary.get(0)[1].toString());
    					 if(filesExtracted4WSummary.get(0)[4]!=null)
    						 totalTransFailedCt=totalTransFailedCt+Double.valueOf(filesExtracted4WSummary.get(0)[4].toString());
    				 }
    				 else
    				 {
    					 trasnformed.add(0d);
    					 transformationFailed.add(0d);
    				 }
    			 }

    			 if(from5wDate.isAfter(fDate))
    			 {
    				 List<Object[]> filesExtracted5WSummary=sourceFileInbHistoryRepository.fetchTransfomedCountBetweenGivenDateForWeek(profileId,from5wDate+"%",till5wDate+"%");
    				 labelDatesValue.add(datesMap(from5wDate.toString(),till5wDate.toString()));

    				 // labelDatesValue.add(from5wDate+"-"+till5wDate);
    				 labelValue.add("Week-5");
    				 if(filesExtracted5WSummary!=null)
    				 {
    					 if(filesExtracted5WSummary.get(0)[1]!=null)
    						 trasnformed.add(Double.valueOf(filesExtracted5WSummary.get(0)[1].toString()));
    					 else
    						 trasnformed.add(0d);
    					 if(filesExtracted5WSummary.get(0)[4]!=null)
    						 transformationFailed.add(Double.valueOf(filesExtracted5WSummary.get(0)[4].toString()));
    					 else
    						 transformationFailed.add(0d);
    					 if(filesExtracted5WSummary.get(0)[1]!=null)
    						 totalTransformed=totalTransformed+Double.valueOf(filesExtracted5WSummary.get(0)[1].toString());
    					 if(filesExtracted5WSummary.get(0)[4]!=null)
    						 totalTransFailedCt=totalTransFailedCt+Double.valueOf(filesExtracted5WSummary.get(0)[4].toString());
    				 }
    				 else
    				 {
    					 trasnformed.add(0d);
    					 transformationFailed.add(0d);
    				 }

    			 }

    			 if(from6Date.isAfter(fDate))
    			 {
    				 List<Object[]> filesExtracted6WSummary=sourceFileInbHistoryRepository.fetchTransfomedCountBetweenGivenDateForWeek(profileId,from6Date+"%",till6Date+"%");
    				 labelDatesValue.add(datesMap(from6Date.toString(),till6Date.toString()));
    				 // labelDatesValue.add(from6Date+"-"+till6Date);
    				 labelValue.add("Week-6");
    				 if(filesExtracted6WSummary!=null)
    				 {
    					 if(filesExtracted6WSummary.get(0)[1]!=null)
    						 trasnformed.add(Double.valueOf(filesExtracted6WSummary.get(0)[1].toString()));
    					 else
    						 trasnformed.add(0d);
    					 if(filesExtracted6WSummary.get(0)[4]!=null)
    						 transformationFailed.add(Double.valueOf(filesExtracted6WSummary.get(0)[4].toString()));
    					 else
    						 transformationFailed.add(0d);
    					 if(filesExtracted6WSummary.get(0)[1]!=null)
    						 totalTransformed=totalTransformed+Double.valueOf(filesExtracted6WSummary.get(0)[1].toString());
    					 if(filesExtracted6WSummary.get(0)[4]!=null)
    						 totalTransFailedCt=totalTransFailedCt+Double.valueOf(filesExtracted6WSummary.get(0)[4].toString());
    				 }
    				 else
    				 {
    					 trasnformed.add(0d);
    					 transformationFailed.add(0d);
    				 }

    			 }

    			 if(from7Date.isAfter(fDate))
    			 {
    				 List<Object[]> filesExtracted6WSummary=sourceFileInbHistoryRepository.fetchTransfomedCountBetweenGivenDateForWeek(profileId,from7Date+"%",till7Date+"%");
    				 labelDatesValue.add(datesMap(from7Date.toString(),till7Date.toString()));

    				 // labelDatesValue.add(from7Date+"-"+till7Date);
    				 labelValue.add("Week-7");
    				 if(filesExtracted6WSummary!=null)
    				 {
    					 if(filesExtracted6WSummary.get(0)[1]!=null)
    						 trasnformed.add(Double.valueOf(filesExtracted6WSummary.get(0)[1].toString()));
    					 else
    						 trasnformed.add(0d);
    					 if(filesExtracted6WSummary.get(0)[4]!=null)
    						 transformationFailed.add(Double.valueOf(filesExtracted6WSummary.get(0)[4].toString()));
    					 else
    						 transformationFailed.add(0d);
    					 if(filesExtracted6WSummary.get(0)[1]!=null)
    						 totalTransformed=totalTransformed+Double.valueOf(filesExtracted6WSummary.get(0)[1].toString());
    					 if(filesExtracted6WSummary.get(0)[4]!=null)
    						 totalTransFailedCt=totalTransFailedCt+Double.valueOf(filesExtracted6WSummary.get(0)[4].toString());
    				 }
    				 else
    				 {
    					 trasnformed.add(0d);
    					 transformationFailed.add(0d);
    				 }

    			 }

    			 if(from8Date.isAfter(fDate))
    			 {
    				 List<Object[]> filesExtracted6WSummary=sourceFileInbHistoryRepository.fetchTransfomedCountBetweenGivenDateForWeek(profileId,from8Date+"%",till8Date+"%");
    				 labelDatesValue.add(datesMap(from8Date.toString(),till8Date.toString()));

    				 //labelDatesValue.add(from8Date+"-"+till8Date);
    				 labelValue.add("Week-8");
    				 if(filesExtracted6WSummary!=null)
    				 {
    					 if(filesExtracted6WSummary.get(0)[1]!=null)
    						 trasnformed.add(Double.valueOf(filesExtracted6WSummary.get(0)[1].toString()));
    					 else
    						 trasnformed.add(0d);
    					 if(filesExtracted6WSummary.get(0)[4]!=null)
    						 transformationFailed.add(Double.valueOf(filesExtracted6WSummary.get(0)[4].toString()));
    					 else
    						 transformationFailed.add(0d);
    					 if(filesExtracted6WSummary.get(0)[1]!=null)
    						 totalTransformed=totalTransformed+Double.valueOf(filesExtracted6WSummary.get(0)[1].toString());
    					 if(filesExtracted6WSummary.get(0)[4]!=null)
    						 totalTransFailedCt=totalTransFailedCt+Double.valueOf(filesExtracted6WSummary.get(0)[4].toString());
    				 }
    				 else
    				 {
    					 trasnformed.add(0d);
    					 transformationFailed.add(0d);
    				 }

    			 }


    			 if(from9Date.isAfter(fDate))
    			 {

    				 List<Object[]> filesExtracted6WSummary=sourceFileInbHistoryRepository.fetchTransfomedCountBetweenGivenDateForWeek(profileId,from9Date+"%",till9Date+"%");
    				 labelDatesValue.add(datesMap(from9Date.toString(),till9Date.toString()));

    				 //labelDatesValue.add(from9Date+"-"+till9Date);
    				 labelValue.add("Week-9");
    				 if(filesExtracted6WSummary!=null)
    				 {
    					 if(filesExtracted6WSummary.get(0)[1]!=null)
    						 trasnformed.add(Double.valueOf(filesExtracted6WSummary.get(0)[1].toString()));
    					 else
    						 trasnformed.add(0d);
    					 if(filesExtracted6WSummary.get(0)[4]!=null)
    						 transformationFailed.add(Double.valueOf(filesExtracted6WSummary.get(0)[4].toString()));
    					 else
    						 transformationFailed.add(0d);
    					 if(filesExtracted6WSummary.get(0)[1]!=null)
    						 totalTransformed=totalTransformed+Double.valueOf(filesExtracted6WSummary.get(0)[1].toString());
    					 if(filesExtracted6WSummary.get(0)[4]!=null)
    						 totalTransFailedCt=totalTransFailedCt+Double.valueOf(filesExtracted6WSummary.get(0)[4].toString());
    				 }
    				 else
    				 {
    					 trasnformed.add(0d);
    					 transformationFailed.add(0d);
    				 }

    			 }

    			 if(from10Date.isAfter(fDate))
    			 {
    				 List<Object[]> filesExtracted6WSummary=sourceFileInbHistoryRepository.fetchTransfomedCountBetweenGivenDateForWeek(profileId,from10Date+"%",till10Date+"%");
    				 labelDatesValue.add(datesMap(from10Date.toString(),till10Date.toString()));

    				 // labelDatesValue.add(from10Date+"-"+till10Date);
    				 labelValue.add("Week-10");
    				 if(filesExtracted6WSummary!=null)
    				 {
    					 if(filesExtracted6WSummary.get(0)[1]!=null)
    						 trasnformed.add(Double.valueOf(filesExtracted6WSummary.get(0)[1].toString()));
    					 else
    						 trasnformed.add(0d);
    					 if(filesExtracted6WSummary.get(0)[4]!=null)
    						 transformationFailed.add(Double.valueOf(filesExtracted6WSummary.get(0)[4].toString()));
    					 else
    						 transformationFailed.add(0d);
    					 if(filesExtracted6WSummary.get(0)[1]!=null)
    						 totalTransformed=totalTransformed+Double.valueOf(filesExtracted6WSummary.get(0)[1].toString());
    					 if(filesExtracted6WSummary.get(0)[4]!=null)
    						 totalTransFailedCt=totalTransFailedCt+Double.valueOf(filesExtracted6WSummary.get(0)[4].toString());
    				 }
    				 else
    				 {
    					 trasnformed.add(0d);
    					 transformationFailed.add(0d);
    				 }

    			 }


    			 if(from11Date.isAfter(fDate))
    			 {
    				 List<Object[]> filesExtracted6WSummary=sourceFileInbHistoryRepository.fetchTransfomedCountBetweenGivenDateForWeek(profileId,from11Date+"%",till11Date+"%");
    				 labelDatesValue.add(datesMap(from11Date.toString(),till11Date.toString()));

    				 // labelDatesValue.add(from11Date+"-"+till11Date);
    				 labelValue.add("Week-11");
    				 if(filesExtracted6WSummary!=null)
    				 {
    					 if(filesExtracted6WSummary.get(0)[1]!=null)
    						 trasnformed.add(Double.valueOf(filesExtracted6WSummary.get(0)[1].toString()));
    					 else
    						 trasnformed.add(0d);
    					 if(filesExtracted6WSummary.get(0)[4]!=null)
    						 transformationFailed.add(Double.valueOf(filesExtracted6WSummary.get(0)[4].toString()));
    					 else
    						 transformationFailed.add(0d);
    					 if(filesExtracted6WSummary.get(0)[1]!=null)
    						 totalTransformed=totalTransformed+Double.valueOf(filesExtracted6WSummary.get(0)[1].toString());
    					 if(filesExtracted6WSummary.get(0)[4]!=null)
    						 totalTransFailedCt=totalTransFailedCt+Double.valueOf(filesExtracted6WSummary.get(0)[4].toString());
    				 }
    				 else
    				 {
    					 trasnformed.add(0d);
    					 transformationFailed.add(0d);
    				 }

    			 }

    			 if(from12Date.isAfter(fDate))
    			 {
    				 List<Object[]> filesExtracted6WSummary=sourceFileInbHistoryRepository.fetchTransfomedCountBetweenGivenDateForWeek(profileId,from12Date+"%",till12Date+"%");
    				 labelDatesValue.add(datesMap(from12Date.toString(),till12Date.toString()));

    				 // labelDatesValue.add(from12Date+"-"+till12Date);
    				 labelValue.add("Week-12");
    				 if(filesExtracted6WSummary!=null)
    				 {
    					 if(filesExtracted6WSummary.get(0)[1]!=null)
    						 trasnformed.add(Double.valueOf(filesExtracted6WSummary.get(0)[4].toString()));
    					 else
    						 trasnformed.add(0d);
    					 if(filesExtracted6WSummary.get(0)[4]!=null)
    						 transformationFailed.add(Double.valueOf(filesExtracted6WSummary.get(0)[4].toString()));
    					 else
    						 transformationFailed.add(0d);
    					 if(filesExtracted6WSummary.get(0)[1]!=null)
    						 totalTransformed=totalTransformed+Double.valueOf(filesExtracted6WSummary.get(0)[1].toString());
    					 if(filesExtracted6WSummary.get(0)[4]!=null)
    						 totalTransFailedCt=totalTransFailedCt+Double.valueOf(filesExtracted6WSummary.get(0)[4].toString());
    				 }
    				 else
    				 {
    					 trasnformed.add(0d);
    					 transformationFailed.add(0d);
    				 }

    			 }

    			 if(from13Date.isAfter(fDate))
    			 {
    				 List<Object[]> filesExtracted6WSummary=sourceFileInbHistoryRepository.fetchTransfomedCountBetweenGivenDateForWeek(profileId,from13Date+"%",till13Date+"%");
    				 labelDatesValue.add(datesMap(from13Date.toString(),till13Date.toString()));

    				 // labelDatesValue.add(from13Date+"-"+till13Date);
    				 labelValue.add("Week-13");
    				 if(filesExtracted6WSummary!=null)
    				 {
    					 if(filesExtracted6WSummary.get(0)[1]!=null)
    						 trasnformed.add(Double.valueOf(filesExtracted6WSummary.get(0)[1].toString()));
    					 else
    						 trasnformed.add(0d);
    					 if(filesExtracted6WSummary.get(0)[4]!=null)
    						 transformationFailed.add(Double.valueOf(filesExtracted6WSummary.get(0)[4].toString()));
    					 else
    						 transformationFailed.add(0d);
    					 if(filesExtracted6WSummary.get(0)[1]!=null)
    						 totalTransformed=totalTransformed+Double.valueOf(filesExtracted6WSummary.get(0)[1].toString());
    					 if(filesExtracted6WSummary.get(0)[4]!=null)
    						 totalTransFailedCt=totalTransFailedCt+Double.valueOf(filesExtracted6WSummary.get(0)[4].toString());
    				 }
    				 else
    				 {
    					 trasnformed.add(0d);
    					 transformationFailed.add(0d);
    				 }

    			 }

    			 log.info("totalTransformed :"+totalTransformed); 

    			 log.info("totalTransFailedCt :"+totalTransFailedCt); 


    			 LinkedHashMap map3=new LinkedHashMap();

    			 map3.put("labelValue", labelValue);
    			 map3.put("labelDates", labelDatesValue);

    			 map3.put("transformed", trasnformed);

    			 map3.put("transformationFailed", transformationFailed);



    			 LinkedHashMap tmmap=new LinkedHashMap();



    			 tmmap.put("totalTransformed", totalTransformed);

    			 tmmap.put("totalTransFailedCt", totalTransFailedCt);



    			 tmmap.put("detailedData", map3);

    			 eachMap.put("3M", tmmap);

    			 //	 finalMap.add(weekMap);

    		 }


    	 }

    	 return eachMap;

     }
     
     
     
     
     
     
     
     @PostMapping("/reconciliationAnalysisforGivenPeriod")
     @Timed
     public LinkedHashMap reconciliationAnalysisforGivenPeriod(@RequestParam Long processId,@RequestBody HashMap dates) throws SQLException, ParseException 
     {
    	 log.info("Rest Request to get aging analysis in service:"+dates);

    	 LinkedHashMap eachMap=new LinkedHashMap();
    	 DecimalFormat dform = new DecimalFormat("####0.00");

    	 if(processId!=null)

    	 {

    		 ProcessDetails procesRecDet=processDetailsRepository.findByProcessIdAndTagType(processId, "reconciliationRuleGroup");

    		 ZonedDateTime fmDate=ZonedDateTime.parse(dates.get("startDate").toString());

    		 ZonedDateTime toDate=ZonedDateTime.parse(dates.get("endDate").toString());

    		 log.info("fmDate :"+fmDate);

    		 log.info("toDate :"+toDate);

    		 LocalDate fDate=fmDate.toLocalDate();

    		 LocalDate tDate=toDate.toLocalDate();

    		 log.info("fDate :"+fDate);

    		 log.info("tDate :"+tDate);



    		 /**Reconciliation 1w**/

    		 log.info("for one week");

    		 LocalDate from1wDate=tDate.minusDays(7);

    		 LocalDate till1wDate=tDate;
    	
    		 log.info("tillDate :"+till1wDate);
    		 
        	 Long days=Duration.between(fDate.atStartOfDay(), tDate.atStartOfDay()).toDays();

    		 if(days<=7 || days>=7)
    		 {

    			 log.info("*****in if 1W*****");
    		
    			 LinkedHashMap oneWeekMap=new LinkedHashMap();
    			 if(days<7)
    				 oneWeekMap= reconciliationAnalysisforGivenWeek(processId, fDate.minusDays(1),tDate);
    			 else
    				 oneWeekMap= reconciliationAnalysisforGivenWeek(processId, from1wDate,till1wDate);
    			 eachMap.put("1W", oneWeekMap);

    			 //	 finalMap.add(weekMap);

    		 }

    		 /** reconciliation 2w**/

    		

    		 LocalDate from2WDate=tDate.minusDays(14);

    		 LocalDate till2WDate=tDate;
   
    		 if(days>=7)
    		 {
    			
    			 log.info("for 2 week");
    			 List<LocalDate> datesList=new ArrayList<LocalDate>();

    			 log.info("from2WDate befor query :"+from2WDate);

    			 log.info("till2WDate befor query :"+till2WDate);
    			 LinkedHashMap twoWeekMap=new LinkedHashMap();
    			 
    			 if(days>=7 && days<14)
    				 twoWeekMap=reconciliationAnalysisforGivenWeek(processId,fDate.minusDays(1),tDate);
    			 else
    			  twoWeekMap=reconciliationAnalysisforGivenWeek(processId, from2WDate,till2WDate);
    			 eachMap.put("2W", twoWeekMap);

    			 //	 finalMap.add(weekMap);

    		 }



    		 /**1month**/


    		 LocalDate from6Date = null;
    		 LocalDate till6Date = null;


    		 if(days>=14)

    		 {

    			 log.info("for 1month week");

    			 log.info("from1wDate :"+from1wDate);//02-10
    			 log.info("till1wDate :"+till1wDate);//02-17
    			 LocalDate from2wDate=from1wDate.minusDays(7);//02-03
    			 log.info("from2wDate :"+from2wDate);
    			 LocalDate till2wDate=from1wDate;//02-10
    			 log.info("till2wDate :"+till2wDate);
    			 LocalDate from3wDate=from2wDate.minusDays(7);//01-27
    			 log.info("from3wDate :"+from3wDate);
    			 LocalDate till3wDate=from2wDate;//02-03
    			 log.info("till3wDate :"+till3wDate);
    			 LocalDate from4wDate=from3wDate.minusDays(7);//01-20
    			 log.info("from4wDate :"+from4wDate);
    			 LocalDate till4wDate=from3wDate;//01-27
    			 log.info("till4wDate :"+till4wDate);
    			 LocalDate from5wDate=from4wDate.minusDays(2);//01-20
    			 log.info("from5wDate :"+from5wDate);
    			 LocalDate till5wDate=from4wDate;//01-27
    			 log.info("till5wDate :"+till5wDate);

    			 /**to retreive third month dates**/
    			 from6Date=from5wDate.minusDays(7);
    			 till6Date=from5wDate;


    			 List<Double> recon=new ArrayList<Double>();

    			 List<Double> unRecon=new ArrayList<Double>();


    			 List<String> labelValue=new ArrayList<String>();
    			 List<LinkedHashMap> labelDatesValue=new ArrayList<LinkedHashMap>();
    			 
    			 List<Double> reconAmtPer=new ArrayList<Double>();
        		 List<Double> unReconAmtPer=new ArrayList<Double>();
        		

    			 Double totalReconAmt=0d;

    			 Double totaldvAmt=0d;

    			 Double totalUnReconAmt=0d;

    			 Double totalReconAmtPer=0d;
    			
    			 Double totalUnReconAmtPer=0d;
    			 
    			 Double totalUnReconCt=0d;
        		 Double totalReconCt=0d;

        		 Double totalReconCtPer=0d;
        		 
        		 Double totaldvCt=0d;

    			 /**for 1st week**/
    			 List<Object[]> recon1WSummary=appModuleSummaryRepository.fetchReconCountAndUnReconciledCountForWeek(procesRecDet.getTypeId(),from1wDate,till1wDate);
    			 if(recon1WSummary!=null)
    			 {
    				 labelDatesValue.add(datesMap(from1wDate.toString(),till1wDate.toString()));
    				
    				 labelValue.add("Week-1");
    				 
    				 totaldvCt=totaldvAmt+Double.valueOf(recon1WSummary.get(0)[0].toString());
    				 totalReconCt=totalReconCt+Double.valueOf(recon1WSummary.get(0)[1].toString());
    				 totaldvAmt=totaldvAmt+Double.valueOf(recon1WSummary.get(0)[4].toString());

    				 totalReconAmt=totalReconAmt+Double.valueOf(recon1WSummary.get(0)[5].toString());

    				 totalReconAmtPer=totalReconAmtPer+Double.valueOf(recon1WSummary.get(0)[6].toString());

    				 totalUnReconAmtPer=totalUnReconAmtPer+Double.valueOf(recon1WSummary.get(0)[7].toString());
    				 


    				 recon.add(Double.valueOf(recon1WSummary.get(0)[2].toString()));

    				 unRecon.add(Double.valueOf(recon1WSummary.get(0)[3].toString()));
    				 reconAmtPer.add(Double.valueOf(recon1WSummary.get(0)[6].toString()));
    				 unReconAmtPer.add(Double.valueOf(recon1WSummary.get(0)[7].toString()));

    			 }
    			 List<Object[]> recon2WSummary=appModuleSummaryRepository.fetchReconCountAndUnReconciledCountForWeek(procesRecDet.getTypeId(),from2wDate,till2wDate);
    			 if(recon2WSummary!=null)
    			 {
    				 labelDatesValue.add(datesMap(from2wDate.toString(),till2wDate.toString()));
    				 
    				 labelValue.add("Week-2");
    				 
    				 totaldvCt=totaldvAmt+Double.valueOf(recon2WSummary.get(0)[0].toString());
    				 totalReconCt=totalReconCt+Double.valueOf(recon2WSummary.get(0)[1].toString());
    				 
    				 totaldvAmt=totaldvAmt+Double.valueOf(recon2WSummary.get(0)[4].toString());

    				 totalReconAmt=totalReconAmt+Double.valueOf(recon2WSummary.get(0)[5].toString());

    				 totalReconAmtPer=totalReconAmtPer+Double.valueOf(recon2WSummary.get(0)[6].toString());

    				 totalUnReconAmtPer=totalUnReconAmtPer+Double.valueOf(recon2WSummary.get(0)[7].toString());

    				 recon.add(Double.valueOf(recon2WSummary.get(0)[2].toString()));

    				 unRecon.add(Double.valueOf(recon2WSummary.get(0)[3].toString()));
    				 reconAmtPer.add(Double.valueOf(recon2WSummary.get(0)[6].toString()));
    				 unReconAmtPer.add(Double.valueOf(recon2WSummary.get(0)[7].toString()));

    			 }
    			 if(from3wDate.isAfter(fDate) || from3wDate.equals(fDate))
    			 {
    				 List<Object[]> recon3WSummary=appModuleSummaryRepository.fetchReconCountAndUnReconciledCountForWeek(procesRecDet.getTypeId(),from3wDate,till3wDate);
    				 if(recon3WSummary!=null)
    				 {
    					 labelDatesValue.add(datesMap(from3wDate.toString(),till3wDate.toString()));
    					
    					 labelValue.add("Week-3");
    					 
    					 totaldvCt=totaldvAmt+Double.valueOf(recon3WSummary.get(0)[0].toString());
        				 totalReconCt=totalReconCt+Double.valueOf(recon3WSummary.get(0)[1].toString());
        				 
    					 totaldvAmt=totaldvAmt+Double.valueOf(recon3WSummary.get(0)[4].toString());

    					 totalReconAmt=totalReconAmt+Double.valueOf(recon3WSummary.get(0)[5].toString());

    					 totalReconAmtPer=totalReconAmtPer+Double.valueOf(recon3WSummary.get(0)[6].toString());

    					 totalUnReconAmtPer=totalUnReconAmtPer+Double.valueOf(recon3WSummary.get(0)[7].toString());

    					 recon.add(Double.valueOf(recon3WSummary.get(0)[2].toString()));

    					 unRecon.add(Double.valueOf(recon3WSummary.get(0)[3].toString()));
    					 
    					 reconAmtPer.add(Double.valueOf(recon3WSummary.get(0)[6].toString()));
        				 unReconAmtPer.add(Double.valueOf(recon3WSummary.get(0)[7].toString()));

    				 }
    			 }
    			 if(from4wDate.isAfter(fDate) || from4wDate.equals(fDate))
    			 {
    				 List<Object[]> recon4WSummary=appModuleSummaryRepository.fetchReconCountAndUnReconciledCountForWeek(procesRecDet.getTypeId(),from4wDate,till4wDate);
    				 if(recon4WSummary!=null)
    				 {
    					 labelDatesValue.add(datesMap(from4wDate.toString(),from4wDate.toString()));
    					
    					 labelValue.add("Week-4");
    					 totaldvCt=totaldvAmt+Double.valueOf(recon4WSummary.get(0)[0].toString());
        				 totalReconCt=totalReconCt+Double.valueOf(recon4WSummary.get(0)[1].toString());
    					 
    					 totaldvAmt=totaldvAmt+Double.valueOf(recon4WSummary.get(0)[4].toString());

    					 totalReconAmt=totalReconAmt+Double.valueOf(recon4WSummary.get(0)[5].toString());

    					 totalReconAmtPer=totalReconAmtPer+Double.valueOf(recon4WSummary.get(0)[6].toString());

    					 totalUnReconAmtPer=totalUnReconAmtPer+Double.valueOf(recon4WSummary.get(0)[7].toString());

    					 recon.add(Double.valueOf(recon4WSummary.get(0)[2].toString()));

    					 unRecon.add(Double.valueOf(recon4WSummary.get(0)[3].toString()));
    					 
    					 reconAmtPer.add(Double.valueOf(recon4WSummary.get(0)[6].toString()));
        				 unReconAmtPer.add(Double.valueOf(recon4WSummary.get(0)[7].toString()));

    				 }
    			 }
    			 if(from5wDate.isAfter(fDate) || from5wDate.equals(fDate))
    			 {
    				 List<Object[]> recon5WSummary=appModuleSummaryRepository.fetchReconCountAndUnReconciledCountForWeek(procesRecDet.getTypeId(),from5wDate,till5wDate);
    				 if(recon5WSummary!=null)
    				 {
    					 labelDatesValue.add(datesMap(from5wDate.toString(),till5wDate.toString()));
    				
    					 labelValue.add("Week-5");
    					 totaldvCt=totaldvAmt+Double.valueOf(recon5WSummary.get(0)[0].toString());
        				 totalReconCt=totalReconCt+Double.valueOf(recon5WSummary.get(0)[1].toString());
    					 
    					 totaldvAmt=totaldvAmt+Double.valueOf(recon5WSummary.get(0)[4].toString());

    					 totalReconAmt=totalReconAmt+Double.valueOf(recon5WSummary.get(0)[5].toString());

    					 totalReconAmtPer=totalReconAmtPer+Double.valueOf(recon5WSummary.get(0)[6].toString());

    					 totalUnReconAmtPer=totalUnReconAmtPer+Double.valueOf(recon5WSummary.get(0)[7].toString());

    					 recon.add(Double.valueOf(recon5WSummary.get(0)[2].toString()));

    					 unRecon.add(Double.valueOf(recon5WSummary.get(0)[3].toString()));
    					 
    					 reconAmtPer.add(Double.valueOf(recon5WSummary.get(0)[6].toString()));
        				 unReconAmtPer.add(Double.valueOf(recon5WSummary.get(0)[7].toString()));


    				 }
    			 }

    			 log.info("totalReconAmt :"+totalReconAmt); 

    			 log.info("totaldvAmt :"+totaldvAmt); 

    			// log.info("totalReconAmtPer :"+totalReconAmtPer); 

    			// log.info("totalUnReconAmtPer :"+totalUnReconAmtPer); 

    			 totalUnReconAmt=totaldvAmt-totalReconAmt;

    			// log.info("totalUnReconAmt :"+totalUnReconAmt); 

    			// log.info("labelValue :"+labelValue);

    			// log.info("recon :"+recon);

    			// log.info("unRecon :"+unRecon);

    			 LinkedHashMap map=new LinkedHashMap();

    			 map.put("labelValue", labelValue);
    			 map.put("labelDateValue", labelDatesValue);
    			 map.put("recon", recon);

    			 map.put("unRecon", unRecon);
    			 map.put("reconAmtPer", reconAmtPer);

        		 map.put("unReconAmtPer", unReconAmtPer);



    			 // weekMap

    			 LinkedHashMap ommap=new LinkedHashMap();



    			 ommap.put("reconAmount", Double.valueOf(totalReconAmt));

    			 ommap.put("reconPercentage",Double.valueOf( dform.format(totalReconAmtPer)));

    			 ommap.put("unReconAmount", Double.valueOf(totalUnReconAmt));

    			 ommap.put("unReconPercentage", Double.valueOf( dform.format(100-totalReconAmtPer)));
    			 
    			 ommap.put("reconCt",Double.valueOf(totalReconCt));
    			 Double reconCtPer=0d;
    		if(totaldvCt>0)
    		{
        		 reconCtPer=(totalReconCt/totaldvCt)*100;
        		 ommap.put("reconCtPer",Double.valueOf(dform.format(reconCtPer)));
    		}
    		else
    			 ommap.put("reconCtPer",0);
        		 Double unReconCt=(totaldvCt-totalReconCt);
        		 ommap.put("unReconCt", Double.valueOf(dform.format(unReconCt)));

        		 if(reconCtPer>0)
        		 ommap.put("unReconCtPer", Double.valueOf(dform.format(100-reconCtPer)));
        		 else
        			 ommap.put("unReconCtPer", 0);

    			 ommap.put("detailedData", map);

    			 eachMap.put("1M", ommap);
    			 
    			

    			 //	 finalMap.add(weekMap);

    		 }



    		 /**3month**/



    		 /* LocalDate till1MDate=fDate.plusDays(30);

     		 LocalDate till3MDate=fDate.plusDays(90);*/

    		 if(days>=30)
    		 {

    			 log.info("for 3month week");

    			 log.info("from1wDate :"+from1wDate);//02-10
    			 log.info("till1wDate :"+till1wDate);//02-17
    			 LocalDate from2wDate=from1wDate.minusDays(7);//02-03
    			 log.info("from2wDate :"+from2wDate);
    			 LocalDate till2wDate=from1wDate;//02-10
    			 log.info("till2wDate :"+till2wDate);
    			 LocalDate from3wDate=from2wDate.minusDays(7);//01-27
    			 log.info("from3wDate :"+from3wDate);
    			 LocalDate till3wDate=from2wDate;//02-03
    			 log.info("till3wDate :"+till3wDate);
    			 LocalDate from4wDate=from3wDate.minusDays(7);//01-20
    			 log.info("from4wDate :"+from4wDate);
    			 LocalDate till4wDate=from3wDate;//01-27
    			 log.info("till4wDate :"+till4wDate);
    			 LocalDate from5wDate=from4wDate.minusDays(2);//01-20
    			 log.info("from5wDate :"+from5wDate);
    			 LocalDate till5wDate=from4wDate;//01-27
    			 log.info("till5wDate :"+till5wDate);
    			 /**to retreive third month dates**/
    			 from6Date=from5wDate.minusDays(7);
    			 till6Date=from5wDate;

    			 LocalDate from7Date=from5wDate.minusDays(7);
    			 LocalDate till7Date=from5wDate;

    			 LocalDate from8Date=from7Date.minusDays(7);
    			 LocalDate till8Date=from7Date;

    			 LocalDate from9Date=from8Date.minusDays(7);
    			 LocalDate till9Date=from8Date;
    			 LocalDate from10Date=from9Date.minusDays(7);
    			 LocalDate till10Date=from9Date;
    			 LocalDate from11Date=from10Date.minusDays(7);
    			 LocalDate till11Date=from10Date;
    			 LocalDate from12Date=from11Date.minusDays(7);
    			 LocalDate till12Date=from11Date;
    			 LocalDate from13Date=from12Date.minusDays(7);
    			 LocalDate till13Date=from12Date;
    			 log.info("for 1month week");
    			 //	 List<LocalDate> datesList=new ArrayList<LocalDate>();



    			 List<String> labelValue=new ArrayList<String>();

    			 List<Double> recon=new ArrayList<Double>();

    			 List<Double> unRecon=new ArrayList<Double>();

    			 List<LinkedHashMap> labelDatesValue=new ArrayList<LinkedHashMap>();
    			 
    			 List<Double> reconAmtPer=new ArrayList<Double>();
        		 List<Double> unReconAmtPer=new ArrayList<Double>();

    			 Double totalReconAmt=0d;

    			 Double totaldvAmt=0d;

    			 Double totalUnReconAmt=0d;

    			 Double totalReconAmtPer=0d;

    			 Double totalUnReconPer=0d;
    			 Double totaldvCt=0d;
    			 Double totalReconCt=0d;

    			 /**for 1st week**/
    			 List<Object[]> recon1WSummary=appModuleSummaryRepository.fetchReconCountAndUnReconciledCountForWeek(procesRecDet.getTypeId(),from1wDate,till1wDate);
    			 if(recon1WSummary!=null)
    			 {
    				 log.info("for 1 week");
    				 labelDatesValue.add(datesMap(from1wDate.toString(),till1wDate.toString()));
    			
    				 labelValue.add("Week-1");
    				 totaldvCt=totaldvAmt+Double.valueOf(recon1WSummary.get(0)[0].toString());
    				 totalReconCt=totalReconCt+Double.valueOf(recon1WSummary.get(0)[1].toString());
    				 reconAmtPer.add(Double.valueOf(recon1WSummary.get(0)[6].toString()));
    				 unReconAmtPer.add(Double.valueOf(recon1WSummary.get(0)[7].toString()));
					 
    				 totaldvAmt=totaldvAmt+Double.valueOf(recon1WSummary.get(0)[4].toString());

    				 totalReconAmt=totalReconAmt+Double.valueOf(recon1WSummary.get(0)[5].toString());

    				 totalReconAmtPer=totalReconAmtPer+Double.valueOf(recon1WSummary.get(0)[6].toString());

    				 totalUnReconPer=totalUnReconPer+Double.valueOf(recon1WSummary.get(0)[7].toString());

    				 recon.add(Double.valueOf(recon1WSummary.get(0)[2].toString()));

    				 unRecon.add(Double.valueOf(recon1WSummary.get(0)[3].toString()));
    				 
    				 


    			 }
    			 List<Object[]> recon2WSummary=appModuleSummaryRepository.fetchReconCountAndUnReconciledCountForWeek(procesRecDet.getTypeId(),from2wDate,till2wDate);
    			 if(recon2WSummary!=null)
    			 {
    				 log.info("for 2 week");
    				 labelDatesValue.add(datesMap(from2wDate.toString(),till2wDate.toString()));
    				
    				 labelValue.add("Week-2");
    				 totaldvCt=totaldvAmt+Double.valueOf(recon2WSummary.get(0)[0].toString());
    				 totalReconCt=totalReconCt+Double.valueOf(recon2WSummary.get(0)[1].toString());
    				 reconAmtPer.add(Double.valueOf(recon2WSummary.get(0)[6].toString()));
    				 unReconAmtPer.add(Double.valueOf(recon2WSummary.get(0)[7].toString()));
    				 
    				 totaldvAmt=totaldvAmt+Double.valueOf(recon2WSummary.get(0)[4].toString());

    				 totalReconAmt=totalReconAmt+Double.valueOf(recon2WSummary.get(0)[5].toString());

    				 totalReconAmtPer=totalReconAmtPer+Double.valueOf(recon2WSummary.get(0)[6].toString());

    				 totalUnReconPer=totalUnReconPer+Double.valueOf(recon2WSummary.get(0)[7].toString());

    				 recon.add(Double.valueOf(recon2WSummary.get(0)[2].toString()));

    				 unRecon.add(Double.valueOf(recon2WSummary.get(0)[3].toString()));

    			 }
    			 List<Object[]> recon3WSummary=appModuleSummaryRepository.fetchReconCountAndUnReconciledCountForWeek(procesRecDet.getTypeId(),from3wDate,till3wDate);
    			 if(recon3WSummary!=null)
    			 {
    				 log.info("for 3 week");
    				 labelDatesValue.add(datesMap(from3wDate.toString(),till3wDate.toString()));
    			
    				 labelValue.add("Week-3");
    				 totaldvCt=totaldvAmt+Double.valueOf(recon3WSummary.get(0)[0].toString());
    				 totalReconCt=totalReconCt+Double.valueOf(recon3WSummary.get(0)[1].toString());
    				 reconAmtPer.add(Double.valueOf(recon3WSummary.get(0)[6].toString()));
    				 unReconAmtPer.add(Double.valueOf(recon3WSummary.get(0)[7].toString()));
    				 
    				 totaldvAmt=totaldvAmt+Double.valueOf(recon3WSummary.get(0)[4].toString());

    				 totalReconAmt=totalReconAmt+Double.valueOf(recon3WSummary.get(0)[5].toString());

    				 totalReconAmtPer=totalReconAmtPer+Double.valueOf(recon3WSummary.get(0)[6].toString());

    				 totalUnReconPer=totalUnReconPer+Double.valueOf(recon3WSummary.get(0)[7].toString());

    				 recon.add(Double.valueOf(recon3WSummary.get(0)[2].toString()));

    				 unRecon.add(Double.valueOf(recon3WSummary.get(0)[3].toString()));

    			 }
    			 List<Object[]> recon4WSummary=appModuleSummaryRepository.fetchReconCountAndUnReconciledCountForWeek(procesRecDet.getTypeId(),from4wDate,till4wDate);
    			 if(recon4WSummary!=null)
    			 {
    				 log.info("for 4 week");
    				 labelDatesValue.add(datesMap(from4wDate.toString(),till4wDate.toString()));

    				 labelValue.add("Week-4");
    				 totaldvCt=totaldvAmt+Double.valueOf(recon4WSummary.get(0)[0].toString());
    				 totalReconCt=totalReconCt+Double.valueOf(recon4WSummary.get(0)[1].toString());
    				 reconAmtPer.add(Double.valueOf(recon4WSummary.get(0)[6].toString()));
    				 unReconAmtPer.add(Double.valueOf(recon4WSummary.get(0)[7].toString()));
    				 
    				 totaldvAmt=totaldvAmt+Double.valueOf(recon4WSummary.get(0)[4].toString());

    				 totalReconAmt=totalReconAmt+Double.valueOf(recon4WSummary.get(0)[5].toString());

    				 totalReconAmtPer=totalReconAmtPer+Double.valueOf(recon4WSummary.get(0)[6].toString());

    				 totalUnReconPer=totalUnReconPer+Double.valueOf(recon4WSummary.get(0)[7].toString());

    				 recon.add(Double.valueOf(recon4WSummary.get(0)[2].toString()));

    				 unRecon.add(Double.valueOf(recon4WSummary.get(0)[3].toString()));

    			 }

    			 List<Object[]> recon5WSummary=appModuleSummaryRepository.fetchReconCountAndUnReconciledCountForWeek(procesRecDet.getTypeId(),from5wDate,till5wDate);
    			 if(recon5WSummary!=null)
    			 {
    				 log.info("for 5 week");
    				 labelDatesValue.add(datesMap(from5wDate.toString(),till5wDate.toString()));

    				 labelValue.add("Week-5");
    				 totaldvCt=totaldvAmt+Double.valueOf(recon5WSummary.get(0)[0].toString());
    				 totalReconCt=totalReconCt+Double.valueOf(recon5WSummary.get(0)[1].toString());
    				 reconAmtPer.add(Double.valueOf(recon5WSummary.get(0)[6].toString()));
    				 unReconAmtPer.add(Double.valueOf(recon5WSummary.get(0)[7].toString()));
    				 
    				 totaldvAmt=totaldvAmt+Double.valueOf(recon5WSummary.get(0)[4].toString());

    				 totalReconAmt=totalReconAmt+Double.valueOf(recon5WSummary.get(0)[5].toString());

    				 totalReconAmtPer=totalReconAmtPer+Double.valueOf(recon5WSummary.get(0)[6].toString());

    				 totalUnReconPer=totalUnReconPer+Double.valueOf(recon5WSummary.get(0)[7].toString());

    				 recon.add(Double.valueOf(recon5WSummary.get(0)[2].toString()));

    				 unRecon.add(Double.valueOf(recon5WSummary.get(0)[3].toString()));

    			 }

    			 List<Object[]> recon6WSummary=appModuleSummaryRepository.fetchReconCountAndUnReconciledCountForWeek(procesRecDet.getTypeId(),from6Date,till6Date);
    			 if(recon6WSummary!=null)
    			 {
    				 labelDatesValue.add(datesMap(from6Date.toString(),till6Date.toString()));
    			
    				 labelValue.add("Week-6");
    				 totaldvCt=totaldvAmt+Double.valueOf(recon6WSummary.get(0)[0].toString());
    				 totalReconCt=totalReconCt+Double.valueOf(recon6WSummary.get(0)[1].toString());
    				 reconAmtPer.add(Double.valueOf(recon6WSummary.get(0)[6].toString()));
    				 unReconAmtPer.add(Double.valueOf(recon6WSummary.get(0)[7].toString()));
    				 
    				 totaldvAmt=totaldvAmt+Double.valueOf(recon6WSummary.get(0)[4].toString());

    				 totalReconAmt=totalReconAmt+Double.valueOf(recon6WSummary.get(0)[5].toString());

    				 totalReconAmtPer=totalReconAmtPer+Double.valueOf(recon6WSummary.get(0)[6].toString());

    				 totalUnReconPer=totalUnReconPer+Double.valueOf(recon6WSummary.get(0)[7].toString());

    				 recon.add(Double.valueOf(recon6WSummary.get(0)[2].toString()));

    				 unRecon.add(Double.valueOf(recon6WSummary.get(0)[3].toString()));

    			 }
    			 if(from7Date.isAfter(fDate) || from7Date.equals(fDate))
    			 {
    				 List<Object[]> recon7WSummary=appModuleSummaryRepository.fetchReconCountAndUnReconciledCountForWeek(procesRecDet.getTypeId(),from7Date,till7Date);
    				 if(recon7WSummary!=null)
    				 {
        				 labelDatesValue.add(datesMap(from7Date.toString(),till7Date.toString()));

    					 labelValue.add("Week-7");
        				 totaldvCt=totaldvAmt+Double.valueOf(recon7WSummary.get(0)[0].toString());
        				 totalReconCt=totalReconCt+Double.valueOf(recon7WSummary.get(0)[1].toString());
        				 reconAmtPer.add(Double.valueOf(recon7WSummary.get(0)[6].toString()));
        				 unReconAmtPer.add(Double.valueOf(recon7WSummary.get(0)[7].toString()));
    					 
    					 totaldvAmt=totaldvAmt+Double.valueOf(recon7WSummary.get(0)[4].toString());

    					 totalReconAmt=totalReconAmt+Double.valueOf(recon7WSummary.get(0)[5].toString());

    					 totalReconAmtPer=totalReconAmtPer+Double.valueOf(recon7WSummary.get(0)[6].toString());

    					 totalUnReconPer=totalUnReconPer+Double.valueOf(recon7WSummary.get(0)[7].toString());

    					 recon.add(Double.valueOf(recon7WSummary.get(0)[2].toString()));

    					 unRecon.add(Double.valueOf(recon7WSummary.get(0)[3].toString()));

    				 }
    			 }
    			 if(from8Date.isAfter(fDate) || from8Date.equals(fDate))
    			 {
    				 List<Object[]> recon8WSummary=appModuleSummaryRepository.fetchReconCountAndUnReconciledCountForWeek(procesRecDet.getTypeId(),from8Date,till8Date);
    				 if(recon8WSummary!=null)
    				 {
        				 labelDatesValue.add(datesMap(from8Date.toString(),till8Date.toString()));

    					 labelValue.add("Week-8");
    					 totaldvCt=totaldvAmt+Double.valueOf(recon8WSummary.get(0)[0].toString());
        				 totalReconCt=totalReconCt+Double.valueOf(recon8WSummary.get(0)[1].toString());
        				 reconAmtPer.add(Double.valueOf(recon8WSummary.get(0)[6].toString()));
        				 unReconAmtPer.add(Double.valueOf(recon8WSummary.get(0)[7].toString()));
    					 
    					 totaldvAmt=totaldvAmt+Double.valueOf(recon8WSummary.get(0)[4].toString());

    					 totalReconAmt=totalReconAmt+Double.valueOf(recon8WSummary.get(0)[5].toString());

    					 totalReconAmtPer=totalReconAmtPer+Double.valueOf(recon8WSummary.get(0)[6].toString());

    					 totalUnReconPer=totalUnReconPer+Double.valueOf(recon8WSummary.get(0)[7].toString());

    					 recon.add(Double.valueOf(recon8WSummary.get(0)[2].toString()));

    					 unRecon.add(Double.valueOf(recon8WSummary.get(0)[3].toString()));

    				 }
    			 }
    			 if(from9Date.isAfter(fDate) || from9Date.equals(fDate))
    			 {
    				 List<Object[]> recon9WSummary=appModuleSummaryRepository.fetchReconCountAndUnReconciledCountForWeek(procesRecDet.getTypeId(),from9Date,till9Date);
    				 if(recon9WSummary!=null)
    				 {
        				 labelDatesValue.add(datesMap(from9Date.toString(),till9Date.toString()));

    					 labelValue.add("Week-9");
    					 totaldvCt=totaldvAmt+Double.valueOf(recon9WSummary.get(0)[0].toString());
        				 totalReconCt=totalReconCt+Double.valueOf(recon9WSummary.get(0)[1].toString());
        				 reconAmtPer.add(Double.valueOf(recon9WSummary.get(0)[6].toString()));
        				 unReconAmtPer.add(Double.valueOf(recon9WSummary.get(0)[7].toString()));
    					 
    					 totaldvAmt=totaldvAmt+Double.valueOf(recon9WSummary.get(0)[4].toString());

    					 totalReconAmt=totalReconAmt+Double.valueOf(recon9WSummary.get(0)[5].toString());

    					 totalReconAmtPer=totalReconAmtPer+Double.valueOf(recon9WSummary.get(0)[6].toString());

    					 totalUnReconPer=totalUnReconPer+Double.valueOf(recon9WSummary.get(0)[7].toString());

    					 recon.add(Double.valueOf(recon9WSummary.get(0)[2].toString()));

    					 unRecon.add(Double.valueOf(recon9WSummary.get(0)[3].toString()));

    				 }
    			 }
    			 if(from10Date.isAfter(fDate) || from10Date.equals(fDate))
    			 {
    				 List<Object[]> recon10WSummary=appModuleSummaryRepository.fetchReconCountAndUnReconciledCountForWeek(procesRecDet.getTypeId(),from10Date,till10Date);
    				 if(recon10WSummary!=null)
    				 {
        				 labelDatesValue.add(datesMap(from10Date.toString(),till10Date.toString()));

    					 labelValue.add("Week-10");
    					 totaldvCt=totaldvAmt+Double.valueOf(recon10WSummary.get(0)[0].toString());
        				 totalReconCt=totalReconCt+Double.valueOf(recon10WSummary.get(0)[1].toString());
        				 reconAmtPer.add(Double.valueOf(recon10WSummary.get(0)[6].toString()));
        				 unReconAmtPer.add(Double.valueOf(recon10WSummary.get(0)[7].toString()));
    					 
    					 
    					 totaldvAmt=totaldvAmt+Double.valueOf(recon10WSummary.get(0)[4].toString());

    					 totalReconAmt=totalReconAmt+Double.valueOf(recon10WSummary.get(0)[5].toString());

    					 totalReconAmtPer=totalReconAmtPer+Double.valueOf(recon10WSummary.get(0)[6].toString());

    					 totalUnReconPer=totalUnReconPer+Double.valueOf(recon10WSummary.get(0)[7].toString());

    					 recon.add(Double.valueOf(recon10WSummary.get(0)[2].toString()));

    					 unRecon.add(Double.valueOf(recon10WSummary.get(0)[3].toString()));

    				 }
    			 }
    			 if(from11Date.isAfter(fDate) || from11Date.equals(fDate))
    			 {
    				 List<Object[]> recon11WSummary=appModuleSummaryRepository.fetchReconCountAndUnReconciledCountForWeek(procesRecDet.getTypeId(),from11Date,till11Date);
    				 if(recon11WSummary!=null)
    				 {
        				 labelDatesValue.add(datesMap(from11Date.toString(),till11Date.toString()));

    					 labelValue.add("Week-11");
    					 totaldvCt=totaldvAmt+Double.valueOf(recon11WSummary.get(0)[0].toString());
        				 totalReconCt=totalReconCt+Double.valueOf(recon11WSummary.get(0)[1].toString());
        				 reconAmtPer.add(Double.valueOf(recon11WSummary.get(0)[6].toString()));
        				 unReconAmtPer.add(Double.valueOf(recon11WSummary.get(0)[7].toString()));
    					 
    					 totaldvAmt=totaldvAmt+Double.valueOf(recon11WSummary.get(0)[4].toString());

    					 totalReconAmt=totalReconAmt+Double.valueOf(recon11WSummary.get(0)[5].toString());

    					 totalReconAmtPer=totalReconAmtPer+Double.valueOf(recon11WSummary.get(0)[6].toString());

    					 totalUnReconPer=totalUnReconPer+Double.valueOf(recon11WSummary.get(0)[7].toString());

    					 recon.add(Double.valueOf(recon11WSummary.get(0)[2].toString()));

    					 unRecon.add(Double.valueOf(recon11WSummary.get(0)[3].toString()));

    				 }
    			 }
    			 if(from12Date.isAfter(fDate) || from12Date.equals(fDate))
    			 {
    				 List<Object[]> recon12WSummary=appModuleSummaryRepository.fetchReconCountAndUnReconciledCountForWeek(procesRecDet.getTypeId(),from12Date,till12Date);
    				 if(recon12WSummary!=null)
    				 {
        				 labelDatesValue.add(datesMap(from12Date.toString(),till12Date.toString()));

    					 labelValue.add("Week-12");
    					 totaldvCt=totaldvAmt+Double.valueOf(recon12WSummary.get(0)[0].toString());
        				 totalReconCt=totalReconCt+Double.valueOf(recon12WSummary.get(0)[1].toString());
        				 reconAmtPer.add(Double.valueOf(recon12WSummary.get(0)[6].toString()));
        				 unReconAmtPer.add(Double.valueOf(recon12WSummary.get(0)[7].toString()));
    					 
    					 totaldvAmt=totaldvAmt+Double.valueOf(recon12WSummary.get(0)[4].toString());

    					 totalReconAmt=totalReconAmt+Double.valueOf(recon12WSummary.get(0)[5].toString());

    					 totalReconAmtPer=totalReconAmtPer+Double.valueOf(recon12WSummary.get(0)[6].toString());

    					 totalUnReconPer=totalUnReconPer+Double.valueOf(recon12WSummary.get(0)[7].toString());

    					 recon.add(Double.valueOf(recon12WSummary.get(0)[2].toString()));

    					 unRecon.add(Double.valueOf(recon12WSummary.get(0)[3].toString()));

    				 }
    			 }
    			 if(from13Date.isAfter(fDate) || from13Date.equals(fDate))
    			 {
    				 List<Object[]> recon13WSummary=appModuleSummaryRepository.fetchReconCountAndUnReconciledCountForWeek(procesRecDet.getTypeId(),from13Date,till13Date);
    				 if(recon13WSummary!=null)
    				 {
        				 labelDatesValue.add(datesMap(from13Date.toString(),till13Date.toString()));

    					 labelValue.add("Week-13");
    					 totaldvCt=totaldvAmt+Double.valueOf(recon13WSummary.get(0)[0].toString());
        				 totalReconCt=totalReconCt+Double.valueOf(recon13WSummary.get(0)[1].toString());
        				 reconAmtPer.add(Double.valueOf(recon13WSummary.get(0)[6].toString()));
        				 unReconAmtPer.add(Double.valueOf(recon13WSummary.get(0)[7].toString()));
    					 
    					 totaldvAmt=totaldvAmt+Double.valueOf(recon13WSummary.get(0)[4].toString());

    					 totalReconAmt=totalReconAmt+Double.valueOf(recon13WSummary.get(0)[5].toString());

    					 totalReconAmtPer=totalReconAmtPer+Double.valueOf(recon13WSummary.get(0)[6].toString());

    					 totalUnReconPer=totalUnReconPer+Double.valueOf(recon13WSummary.get(0)[7].toString());

    					 recon.add(Double.valueOf(recon13WSummary.get(0)[2].toString()));

    					 unRecon.add(Double.valueOf(recon13WSummary.get(0)[3].toString()));

    				 }
    			 }


    			 log.info("totalReconAmt :"+totalReconAmt); 

    			 log.info("totaldvAmt :"+totaldvAmt); 

    			// log.info("totalReconAmtPer :"+totalReconAmtPer); 

    			// log.info("totalUnReconPer :"+totalUnReconPer); 

    			 totalUnReconAmt=totaldvAmt-totalReconAmt;

    			 log.info("totalUnReconAmt :"+totalUnReconAmt); 





    			// log.info("labelValue :"+labelValue);

    			// log.info("recon :"+recon);

    			// log.info("unRecon :"+unRecon);

    			 LinkedHashMap map=new LinkedHashMap();

    			 map.put("labelValue", labelValue);
    			 map.put("labelDateValue", labelDatesValue);

    			 map.put("recon", recon);

    			 map.put("unRecon", unRecon);
    			 
    			 map.put("reconAmtPer", reconAmtPer);

        		 map.put("unReconAmtPer", unReconAmtPer);



    			 // weekMap

    			 LinkedHashMap tmmap=new LinkedHashMap();



    			 tmmap.put("reconAmount",Double.valueOf( totalReconAmt));

    			 tmmap.put("reconPercentage",Double.valueOf( dform.format(totalReconAmtPer)));

    			 tmmap.put("unReconAmount", Double.valueOf(totalUnReconAmt));

    			 tmmap.put("unReconPercentage", Double.valueOf(dform.format(100-totalReconAmtPer)));
    			 
    			 tmmap.put("reconCt",Double.valueOf(totalReconCt));
        		 Double reconCtPer=0d;
        		 if(totaldvCt>0)
        		 {
        			 reconCtPer =(totalReconCt/totaldvCt)*100;
        		 tmmap.put("reconCtPer",Double.valueOf(dform.format(reconCtPer)));
        		 }
        		 else
        			 tmmap.put("reconCtPer",reconCtPer);
        		 Double unReconCt=(totaldvCt-totalReconCt);
        		 tmmap.put("unReconCt",Double.valueOf( dform.format(unReconCt)));

        		 if(unReconCt>0)
        		 tmmap.put("unReconCtPer", Double.valueOf(dform.format(100-reconCtPer)));
        		 else
        			 tmmap.put("unReconCtPer",0);

    			 tmmap.put("detailedData", map);

    			 eachMap.put("3M", tmmap);

    			

    		 }





    	 }
    	 //log.info("eachMap :"+eachMap);
    	 return eachMap;



     }
     
     
     
     
     
     /**GETSUMMARYINFOFORRECONCILIATIONV2 BY RULE
      * @THROWS PARSEEXCEPTION */
      
     /* @POSTMAPPING("/GETSUMMARYINFOFORRECONCILIATIONV2")
      @TIMED 
      PUBLIC LINKEDHASHMAP GETSUMMARYINFOFORRECONCILIATIONV2(@REQUESTPARAM LONG PROCESSID ,@REQUESTBODY HASHMAP DATES) THROWS SQLEXCEPTION, PARSEEXCEPTION
      {
     	 LOG.INFO("REST REQUEST TO GETSUMMARYINFOFORRECONCILIATIONV2 FOR A PROCESS:"+PROCESSID);
      	LINKEDHASHMAP FINALMAP=NEW LINKEDHASHMAP();
      	LIST<LINKEDHASHMAP> DATAMAP=NEW ARRAYLIST<LINKEDHASHMAP>();
      PROCESSES PROCESSES=PROCESSESREPOSITORY.FINDONE(PROCESSID);
      	PROCESSDETAILS PROCESDET=PROCESSDETAILSREPOSITORY.FINDBYPROCESSIDANDTAGTYPE(PROCESSID, "RECONCILIATIONRULEGROUP");
      	IF(PROCESDET!=NULL)
      	{
      	ZONEDDATETIME FMDATE=ZONEDDATETIME.PARSE(DATES.GET("STARTDATE").TOSTRING());
      	ZONEDDATETIME TODATE=ZONEDDATETIME.PARSE(DATES.GET("ENDDATE").TOSTRING());
      	LOG.INFO("FMDATE :"+FMDATE);
      	LOG.INFO("TODATE :"+TODATE);
      	JAVA.TIME.LOCALDATE FDATE=FMDATE.TOLOCALDATE();
      	JAVA.TIME.LOCALDATE TDATE=TODATE.TOLOCALDATE();
      	LIST<STRING> RULESLIST=NEW ARRAYLIST<STRING>();
      	LIST<LONG> RULESIDLIST=NEW ARRAYLIST<LONG>();
      	OBJECT[] UNRECONTOTALCOUNT=APPMODULESUMMARYREPOSITORY.FETCHUNRECCOUNTSBYGROUPIDANDFILEDATE(PROCESDET.GETTYPEID(),FDATE,TDATE);
      	IF(UNRECONTOTALCOUNT[0]!=NULL)
      	{
      	LOG.INFO("UNRECONTOTALCOUNT :"+DOUBLE.VALUEOF(UNRECONTOTALCOUNT[0].TOSTRING()));
      	DOUBLE TOTALUNRECONCT=DOUBLE.VALUEOF(UNRECONTOTALCOUNT[0].TOSTRING());
      	LIST<OBJECT[]> RECONSUMMARY=APPMODULESUMMARYREPOSITORY.FETCHRECCOUNTSBYGROUPIDANDFILEDATE(PROCESDET.GETTYPEID(),FDATE,TDATE);
      	DOUBLE TOTALUNRECONAMT=0D;
      	DOUBLE TOTALUNAPPROVEDCT=0D;
      	LONG VIOLATIONCOUNT=0L;
      	FOR(INT I=0;I<RECONSUMMARY.SIZE();I++)
      	{
      		LIST<OBJECT>  DATA=NEW ARRAYLIST<OBJECT>();
      		LINKEDHASHMAP MAP=NEW LINKEDHASHMAP();
      		MAP.PUT("DVCOUNT", RECONSUMMARY.GET(I)[0]);
      		MAP.PUT("RECONCILEDCOUNT", RECONSUMMARY.GET(I)[1]);
      		MAP.PUT("UNRECONCILEDCOUNT", RECONSUMMARY.GET(I)[2]);
      		MAP.PUT("RECONCILEDPER", RECONSUMMARY.GET(I)[3]);
      		DATA.ADD(RECONSUMMARY.GET(I)[3]);
      		LOG.INFO("RECONSUMMARY.GET(I)[4].TOSTRING() :"+RECONSUMMARY.GET(I)[4].TOSTRING());
      		DOUBLE UNRECONCOUNT=DOUBLE.VALUEOF(RECONSUMMARY.GET(I)[2].TOSTRING());
      		 
      		
      		DOUBLE UNRECONPER=(UNRECONCOUNT/TOTALUNRECONCT)*100;
      		
      		LOG.INFO("UNRECONPER :"+UNRECONPER);
      		DOUBLE FINALUNRECONPER= MATH.ROUND( UNRECONPER * 100.0 ) / 100.0;
      		LOG.INFO("FINALUNRECONPER :"+FINALUNRECONPER);
      		
      		MAP.PUT("UNRECONCILEDPER", FINALUNRECONPER);
      		DATA.ADD(RECONSUMMARY.GET(I)[4]);
      		MAP.PUT("DVTYPE", RECONSUMMARY.GET(I)[5]);
      		MAP.PUT("RULEID", RECONSUMMARY.GET(I)[6]);
      		MAP.PUT("VIEWID", RECONSUMMARY.GET(I)[6]);
      		
      		TOTALUNRECONAMT=TOTALUNRECONAMT+DOUBLE.VALUEOF(RECONSUMMARY.GET(I)[7].TOSTRING());
      		TOTALUNAPPROVEDCT=TOTALUNAPPROVEDCT+DOUBLE.VALUEOF(RECONSUMMARY.GET(I)[8].TOSTRING());
      		IF(!RULESIDLIST.CONTAINS(LONG.VALUEOF(RECONSUMMARY.GET(I)[6].TOSTRING())))
      			RULESIDLIST.ADD(LONG.VALUEOF(RECONSUMMARY.GET(I)[6].TOSTRING()));
      		
      		RULES RULE=RULESREPOSITORY.FINDONE(LONG.VALUEOF(RECONSUMMARY.GET(I)[6].TOSTRING()));
      		MAP.PUT("STACK", RULE.GETRULECODE());
      		IF(!RULESLIST.CONTAINS(RULE.GETRULECODE()))
      			RULESLIST.ADD(RULE.GETRULECODE());
      		DATAVIEWS DV=DATAVIEWSREPOSITORY.FINDONE(LONG.VALUEOF(RECONSUMMARY.GET(I)[6].TOSTRING()));
      		MAP.PUT("VIEWNAME", DV.GETDATAVIEWNAME());
          	LIST<BIGINTEGER> FINALSRCIDLIST=NEW ARRAYLIST<BIGINTEGER>();
          	LIST<BIGINTEGER> RECONCILIEDSRCIDS=RECONCILIATIONRESULTREPOSITORY.FETCHRECONCILEDSOURCEIDS(PROCESSES.GETTENANTID(), PROCESDET.GETTYPEID(), DV.GETID());	 
     		 FINALSRCIDLIST.ADDALL(RECONCILIEDSRCIDS);
     		 LIST<BIGINTEGER> RECONCILIEDTRGIDS=RECONCILIATIONRESULTREPOSITORY.FETCHRECONCILEDTARGETIDS(PROCESSES.GETTENANTID(), PROCESDET.GETTYPEID(), DV.GETID());	 
     		 FINALSRCIDLIST.ADDALL(RECONCILIEDTRGIDS);
     		 
     		 
     		 STRING DBURL=ENV.GETPROPERTY("SPRING.DATASOURCE.URL");
         	 STRING[] PARTS=DBURL.SPLIT("[\\S@&?$+-]+");
         	 STRING HOST = PARTS[0].SPLIT("/")[2].SPLIT(":")[0];
         	 LOG.INFO("HOST :"+HOST);
         	 STRING SCHEMANAME=PARTS[0].SPLIT("/")[3];
         	 LOG.INFO("SCHEMANAME :"+SCHEMANAME);
         	 STRING USERNAME = ENV.GETPROPERTY("SPRING.DATASOURCE.USERNAME");
         	 STRING PASSWORD = ENV.GETPROPERTY("SPRING.DATASOURCE.PASSWORD");
         	 STRING JDBCDRIVER = ENV.GETPROPERTY("SPRING.DATASOURCE.JDBCDRIVER");

         	 CONNECTION CONN = NULL;
         	 STATEMENT STMTDV = NULL;



         	 CONN = DRIVERMANAGER.GETCONNECTION(DBURL, USERNAME, PASSWORD);
         	 LOG.INFO("CONNECTED DATABASE SUCCESSFULLY...");
         	 STMTDV = CONN.CREATESTATEMENT();

         	 RESULTSET RESULTDV=NULL;
         	 RESULTSET RESULTACT=NULL;
         

         	IF(FINALSRCIDLIST!=NULL && !FINALSRCIDLIST.ISEMPTY())
         	{
         	 STRING FINALSRCIDS=FINALSRCIDLIST.TOSTRING().REPLACEALL("\\[", "").REPLACEALL("\\]", "");
         	 
         	 STRING QUERY="SELECT DATEDIFF( SYSDATE(), `V`.`FILEDATE`) AS `RULE_AGE`,COUNT(SCRIDS) FROM "+SCHEMANAME+"."+DV.GETDATAVIEWNAME().TOLOWERCASE().TOLOWERCASE()+" V WHERE FILEDATE BETWEEN '"+FDATE+"' AND '"+TDATE+"' "
         				 + " AND SCRIDS NOT IN ("+FINALSRCIDS+")GROUP BY RULE_AGE";
      	
      		
  			LOG.INFO("QUERY :"+QUERY);

         	 RESULTDV=STMTDV.EXECUTEQUERY(QUERY);
         	 RESULTSETMETADATA RSMD2 = RESULTDV.GETMETADATA();
         	 INT COLUMNCOUNT = RSMD2.GETCOLUMNCOUNT();
         	

         	 WHILE(RESULTDV.NEXT())
         	 {
         		 IF(LONG.VALUEOF(RESULTDV.GETSTRING("RULE_AGE").TOSTRING())>VIOLATION);
         		 VIOLATIONCOUNT=VIOLATIONCOUNT+LONG.VALUEOF(RESULTDV.GETSTRING("COUNT(SCRIDS)").TOSTRING());
         	 }
         	 
         	}
         	 
  			DATAMAP.ADD(MAP);
      		
      	}
      	LOG.INFO("TOTALUNRECONAMT :"+TOTALUNRECONAMT);
      	LOG.INFO("RULESLIST :"+RULESLIST);
      	FINALMAP.PUT("RULESLIST", RULESLIST);
      	FINALMAP.PUT("RULESIDLIST", RULESIDLIST);
      	FINALMAP.PUT("UNRECONITEMSVALUE", TOTALUNRECONAMT);
      	FINALMAP.PUT("UNRECONITEMSVIOLATION", VIOLATIONCOUNT);
      	FINALMAP.PUT("AWAITINGAPPCOUNT", TOTALUNAPPROVEDCT);
      	FINALMAP.PUT("RECONCILIATIONDATA", DATAMAP);
  
      	 }
      }
      	LOG.INFO("******END TIME : "+ZONEDDATETIME.NOW()+"*******");
  		RETURN FINALMAP;
      }*/
     
     
     public int getOutOfCountForProfileExtractionFromOozieDV2(@RequestBody java.time.LocalDate tDate,@RequestParam String oozieJobId) throws SQLException 
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
     
     
     
     
     
     @PostMapping("/extractionAnalysisforGivenPeriod")
     @Timed
     public LinkedHashMap extractionAnalysisforGivenPeriod(List<BigInteger> jobDetails,LocalDate fDate,LocalDate tDate,ZonedDateTime userToDate) throws SQLException, ParseException
     {


    	 Double finalCount=0d;
    	 Double extractedCount=0d;
    	 log.info("fDate in service ext:"+fDate);
    	 log.info("tDate in service ext:"+tDate);
    	 log.info("userToDate :"+userToDate);
    	 LocalDate tillFrLoop=tDate;
    	 log.info("tillFrLoop :"+tillFrLoop);

    	 List<String> labelValue=new ArrayList<String>();

    	 List<String> labelDates=new ArrayList<String>();

    	 List<Double> extracted=new ArrayList<Double>();

    	 List<Double> extractedFailed=new ArrayList<Double>();

    	 List<LocalDate> datesList=new ArrayList<LocalDate>();


    	 while(tillFrLoop.isAfter(fDate) || tillFrLoop.equals(fDate)){
    		 log.info("tillFrLoop in while in ext:"+tillFrLoop);
    		 datesList.add(tillFrLoop);
    		 LocalDate dt=tillFrLoop;

    		 String monthOf=dateFormat(tillFrLoop.toString());

    		 labelValue.add(dt.getDayOfMonth()+" "+monthOf);

    		 labelDates.add(tillFrLoop.toString());
    		// log.info("labelDates :"+labelDates);
    		 if(jobDetails.size()>0)
    		 {
    		 List<SchedulerDetails> schList=schedulerDetailsRepository.findSchedulersByJobIdIn(jobDetails,tillFrLoop+"%");
    		 if(schList.size()>0)
    		 {
    			 Double dateSpecificExtracted=0d;
    			 Double dateSpecificExtractedFailed=0d;
    			 for(SchedulerDetails sch:schList)
    			 {


    				 if(sch.getOozieJobId()!=null)
    				 {
    					 if(sch.getFrequency().equalsIgnoreCase("OnDemand"))
    					 {
    						 finalCount=finalCount+1;//how many has to performed
    						 extractedCount=extractedCount+1;//how many has performed
    						 dateSpecificExtracted=dateSpecificExtracted+1d;
    						 dateSpecificExtractedFailed=dateSpecificExtractedFailed+0d;
    						 
    					 }
    					 if(sch.getFrequency().equalsIgnoreCase("OneTime"))
    					 {
    						 /** condition to check date**/
    						 finalCount=finalCount+1;
    						 extractedCount=extractedCount+1;
    						 dateSpecificExtracted=dateSpecificExtracted+1d;
    						 dateSpecificExtractedFailed=dateSpecificExtractedFailed+0d;
    					 }


    					 if(sch.getFrequency().equalsIgnoreCase("hourly"))
    					 {
    						 log.info("sch.get(i).getHours() :"+sch.getHours());

    						 log.info("tDate.getDayOfWeek()1 :"+tDate.getDayOfWeek());
    						 if(userToDate.isBefore(sch.getEndDate()))
    						 {
    							 Long totalRuns=24/sch.getHours();
    							 log.info("totalRuns1 :"+totalRuns.intValue());
    							 finalCount=finalCount+totalRuns.intValue();
    							 int extractedFileCount=getOutOfCountForProfileExtractionFromOozieDV2(tDate,sch.getOozieJobId());
    							 extractedCount=extractedCount+extractedFileCount;
    							 double ActextFileCt=extractedFileCount;
    							
    							 Double extFailCt=totalRuns.intValue()-ActextFileCt;
    							
    							 
    							 dateSpecificExtracted=dateSpecificExtracted+ActextFileCt;
        						 dateSpecificExtractedFailed=dateSpecificExtractedFailed+extFailCt;

    						 }

    					 }
    					 if(sch.getFrequency().equalsIgnoreCase("minutes"))
    					 {
    						 log.info("sch.get(i).getMinutes :"+sch.getMinutes());

    						 if(userToDate.isBefore(sch.getEndDate()))
    						 {
    							 Long totalRuns=24*60/sch.getMinutes();
    							 log.info("totalRuns1 :"+totalRuns);
    							 finalCount=finalCount+totalRuns.intValue();
    							 int extractedFileCount=getOutOfCountForProfileExtractionFromOozieDV2(tDate,sch.getOozieJobId());
    							 extractedCount=extractedCount+extractedFileCount;
    							 double ActextFileCt=extractedFileCount;
    							 
    							 Double extFailCt=totalRuns.intValue()-ActextFileCt;
    							 dateSpecificExtracted=dateSpecificExtracted+ActextFileCt;
        						 dateSpecificExtractedFailed=dateSpecificExtractedFailed+extFailCt;


    						 }

    					 }

    					 if(sch.getFrequency().equalsIgnoreCase("Daily"))
    					 {
    						 log.info("sch.get(i).getMinutes :"+sch.getMinutes());

    						 if(userToDate.isBefore(sch.getEndDate()))
    						 {
    							 Long totalRuns=1l;
    							 log.info("totalRuns1 :"+totalRuns);
    							 finalCount=finalCount+totalRuns.intValue();
    							 int extractedFileCount=getOutOfCountForProfileExtractionFromOozieDV2(tDate,sch.getOozieJobId());
    							 extractedCount=extractedCount+extractedFileCount;
    							 double ActextFileCt=extractedFileCount;
    							
    							 Double extFailCt=totalRuns.intValue()-ActextFileCt;
    							 dateSpecificExtracted=dateSpecificExtracted+ActextFileCt;
        						 dateSpecificExtractedFailed=dateSpecificExtractedFailed+extFailCt;
    						 }

    					 }
    					 if(sch.getFrequency().equalsIgnoreCase("weekly"))
    					 {

    						 log.info("sch.get(i).getWeekDay() :"+sch.getWeekDay());
    						 String weekDay=tDate.getDayOfWeek().toString();
    						 String day=weekDay.subSequence(0, 3).toString();
    						 log.info("tDate.getDayOfWeek() :"+weekDay.subSequence(0, 3));
    						 if(sch.getWeekDay().equalsIgnoreCase(day))
    						 {
    							 if(userToDate.isBefore(sch.getEndDate()))
    							 {
    								 Long weeks=1l;

    								 finalCount=finalCount+weeks.intValue();
    								 int extractedFileCount=getOutOfCountForProfileExtractionFromOozieDV2(tDate,sch.getOozieJobId());
    								 extractedCount=extractedCount+extractedFileCount;
    								 double ActextFileCt=extractedFileCount;
    								
    								 Double extFailCt=weeks.intValue()-ActextFileCt;
    								 dateSpecificExtracted=dateSpecificExtracted+ActextFileCt;
            						 dateSpecificExtractedFailed=dateSpecificExtractedFailed+extFailCt;
    							 }
    						 }

    					 }
    					 if(sch.getFrequency().equalsIgnoreCase("MONTHLY"))
    					 {

    						 log.info("sch.get(i).getMonth() :"+sch.getMonth());
    						 String month=tDate.getMonth().toString();
    						 String mon=month.subSequence(0, 3).toString();
    						 log.info("tDate.getMonth() :"+mon.subSequence(0, 3));
    						 if(sch.getMonth().equalsIgnoreCase(mon))
    						 {
    							 if(userToDate.isBefore(sch.getEndDate()))
    							 {

    								 Long months=1l;
    								 finalCount=finalCount+months.intValue();
    								 int extractedFileCount=getOutOfCountForProfileExtractionFromOozieDV2(tDate,sch.getOozieJobId());
    								 extractedCount=extractedCount+extractedFileCount;
    								 double ActextFileCt=extractedFileCount;
    								
    								 Double extFailCt=months.intValue()-ActextFileCt;
    								 dateSpecificExtracted=dateSpecificExtracted+ActextFileCt;
            						 dateSpecificExtractedFailed=dateSpecificExtractedFailed+extFailCt;

    							 }
    						 }


    					 }
    				 }

    			 }
    			 extracted.add(dateSpecificExtracted);
    			 
				 extractedFailed.add(dateSpecificExtractedFailed);

    		 }
    		 else
    		 {
    			 extracted.add(0d);
    			 extractedFailed.add(0d);
    		 }
    	 }
    		 else
    		 {
    			 extracted.add(0d);
    			 extractedFailed.add(0d);
    		 }
    		 tillFrLoop=tillFrLoop.minusDays(1);
    		 System.out.println("end of while tillFrLoop :"+tillFrLoop);
    	
    	 }

    	 log.info("datesList :"+datesList);

    	 LinkedHashMap valuesMap=new LinkedHashMap();

    	 valuesMap.put("labelValue", labelValue);

    	 valuesMap.put("labelDates", labelDates);

    	 valuesMap.put("extracted", extracted);

    	 valuesMap.put("extractionFailed", extractedFailed);


    	 DecimalFormat dform = new DecimalFormat("####0.00");
    	 // weekMap

    	 LinkedHashMap weekMap=new LinkedHashMap();

    	 Double totalExtractionFailed=finalCount-extractedCount;

    	 /* twomap.put("reconAmount", totalReconAmt);

     			 twomap.put("reconPercentage", totalReconAmtPer);*/

    	 weekMap.put("totalExtracted", extractedCount);
    	 
    	 Double totalExtractedPer=0d;
    	 if(finalCount>0)
    		 totalExtractedPer= (extractedCount/finalCount)*100;
    	 
    	 weekMap.put("totalExtractedPer",Double.valueOf(dform.format(totalExtractedPer)));

    	 weekMap.put("totalExtractionFailedCt", totalExtractionFailed);
    	 
    	 Double totalExtractionFailedCtPer=0d;
    	 if(finalCount>0)
    		 totalExtractionFailedCtPer =(totalExtractionFailed/finalCount)*100;
    	 
    	 
    	 weekMap.put("totalExtractionFailedCtPer",Double.valueOf(dform.format(totalExtractionFailedCtPer)));


    	 weekMap.put("detailedData", valuesMap);

    	 return weekMap;

     }
     
     
     /** reconciliation details for given week**/
     
     public LinkedHashMap reconciliationAnalysisforGivenWeek(Long processId,LocalDate from1wDate,LocalDate till1wDate) throws SQLException, ParseException 
     { 
    	 log.info("Rest Request to get aging analysis in service:start date :"+from1wDate +" and end date :"+till1wDate);
    	 LinkedHashMap wmap=new LinkedHashMap();


    	/* ZonedDateTime fmDate=ZonedDateTime.parse(dates.get("startDate").toString());
    	 ZonedDateTime toDate=ZonedDateTime.parse(dates.get("endDate").toString());
    	 log.info("fmDate :"+fmDate);
    	 log.info("toDate :"+toDate);
    	 java.time.LocalDate fDate=fmDate.toLocalDate();
    	 java.time.LocalDate tDate=toDate.toLocalDate();*/

    	 DecimalFormat dform = new DecimalFormat("####0.00");

    	 if(processId!=null)

    	 {

    		 ProcessDetails procesRecDet=processDetailsRepository.findByProcessIdAndTagType(processId, "reconciliationRuleGroup");

    		// if(week.equalsIgnoreCase("OneWeek"))
    		// {


    			
    	
    			 LocalDate till1wDateFrLoop=till1wDate;
    	
    			 List<LocalDate> datesList=new ArrayList<LocalDate>();



    			 List<String> labelValue=new ArrayList<String>();

    			 List<String> labelDates=new ArrayList<String>();

    			 List<Double> recon=new ArrayList<Double>();

    			 List<Double> unRecon=new ArrayList<Double>();
    			 List<Double> reconAmtPer=new ArrayList<Double>();
    			 List<Double> unReconAmtPer=new ArrayList<Double>();
    			 log.info("for one week");

    			 while(till1wDateFrLoop.isAfter(from1wDate)){
    				 datesList.add(till1wDateFrLoop);
    				 till1wDateFrLoop=till1wDateFrLoop.minusDays(1);
    				 System.out.println("end of while till1wDateFrLoop :"+till1wDateFrLoop);
    			 }

    			 log.info("datesList :"+datesList);

    			 log.info("fromDate befor query :"+from1wDate);

    			 log.info("tillDate befor query :"+till1wDate);
    			
    			 List< Object[]> reconSummary=new ArrayList<Object[]>();
    			
    				 reconSummary=appModuleSummaryRepository.fetchReconCountAndUnReconciledCountWithDates(procesRecDet.getTypeId(),from1wDate,till1wDate);
    			 log.info("reconSummary size :"+reconSummary.size());

    			 //List<LinkedHashMap> datesMapList=new ArrayList<LinkedHashMap>();

    			 Double totalReconAmt=0d;

    			 Double totaldvAmt=0d;

    			 Double totalUnReconAmt=0d;

    			 Double totalUnReconAmtPer=0d;
    			 Double totalUnReconCtPer=0d;

    			 Double totalReconAmtPer=0d;

    			 Double totalUnReconCt=0d;
    			 Double totalReconCt=0d;

    			 Double totalReconCtPer=0d;

    			 Double totaldvCt=0d;

    			 // Double totalUnReconPer=0d;


    			 LinkedHashMap datesMap=new LinkedHashMap();

    			 for(int i=0;i<reconSummary.size();i++)

    			 {

    				 if(reconSummary!=null)

    				 {

    					// log.info("reconSummary.get(i)[8].toString() :"+reconSummary.get(i)[8].toString());

    					// log.info("reconSummary.get(i)[2].toString() :"+reconSummary.get(i)[2].toString());

    					// log.info("reconSummary.get(i)[3].toString() :"+reconSummary.get(i)[3].toString());

    					// log.info("reconSummary.get(i)[7].toString() :"+reconSummary.get(i)[7].toString());
    					 
    					// log.info("reconSummary.get(i)[1].toString() :"+reconSummary.get(i)[1].toString());

    					 totaldvCt=totaldvAmt+Double.valueOf(reconSummary.get(i)[0].toString());

    					 totaldvAmt=totaldvAmt+Double.valueOf(reconSummary.get(i)[4].toString());

    					 totalReconAmt=totalReconAmt+Double.valueOf(reconSummary.get(i)[5].toString());

    					 totalReconAmtPer=totalReconAmtPer+Double.valueOf(reconSummary.get(i)[6].toString());

    					 totalReconCt=totalReconCt+Double.valueOf(reconSummary.get(i)[1].toString());
    					 
    					 totalReconCtPer=totalReconCtPer+Double.valueOf(reconSummary.get(i)[2].toString());

    					 totalUnReconAmtPer=totalUnReconAmtPer+Double.valueOf(reconSummary.get(i)[7].toString());

    					 totalUnReconCtPer=totalUnReconCtPer+Double.valueOf(reconSummary.get(i)[3].toString());
    					 
    					// log.info("totalUnReconCtPer**** :"+totalUnReconCtPer );

    					 datesMap.put(reconSummary.get(i)[8].toString()+"countPer", reconSummary.get(i)[2].toString()+","+reconSummary.get(i)[3].toString());
    					 datesMap.put(reconSummary.get(i)[8].toString()+"amtPer", reconSummary.get(i)[6].toString()+","+reconSummary.get(i)[7].toString());




    				 }



    			 }



    			 log.info("totalReconAmt :"+totalReconAmt); 

    			 log.info("totaldvAmt :"+totaldvAmt); 

    			// log.info("totalReconAmtPer :"+totalReconAmtPer); 
    			 
    			// log.info("totalReconCt :"+totalReconCt); 


    			 totalUnReconAmt=totaldvAmt-totalReconAmt;

    			// log.info("totalUnReconAmt :"+totalUnReconAmt); 



    			// log.info("datesMap :"+datesMap);



    			 for(int d=0;d<datesList.size();d++)

    			 {
    				//	 log.info("datesList.get(d) reconciliation:"+datesList.get(d));

    					 LocalDate dt=LocalDate.parse(datesList.get(d).toString());

    					 String month=dateFormat(datesList.get(d).toString());

    					 labelValue.add(dt.getDayOfMonth()+" "+month);

    					 labelDates.add(datesList.get(d).toString());

    					 if(datesMap.get(datesList.get(d).toString()+"countPer")!=null)

    					 {

    						 // DateFormatSymbols().getMonths()[month]

    						// log.info("in if datesList.get(d)key :"+datesMap.get(datesList.get(d).toString()));

    						 String[] split=datesMap.get(datesList.get(d).toString()+"countPer").toString().split(",");

    						 log.info("split [0]"+split[0]);

    						 log.info("split [1]"+split[1]);

    						 recon.add(Double.valueOf(split[0]));

    						 unRecon.add(Double.valueOf(split[1]));

    					 }

    					 else

    					 {

    						 log.info("in else ct per ");

    						 Double l=0d;

    						 recon.add(l);

    						 unRecon.add(l);

    					 }
    					 if(datesMap.get(datesList.get(d).toString()+"amtPer")!=null)

    					 {

    						 // DateFormatSymbols().getMonths()[month]

    						// log.info("in if datesList.get(d)key :"+datesMap.get(datesList.get(d).toString()));

    						 String[] split=datesMap.get(datesList.get(d).toString()+"amtPer").toString().split(",");

    						 log.info("split [0]"+split[0]);

    						 log.info("split [1]"+split[1]);

    						 reconAmtPer.add(Double.valueOf(split[0]));

    						 unReconAmtPer.add(Double.valueOf(split[1]));

    					 }
    					 else

    					 {
    						 log.info("in else amt Per");

    						 Double l=0d;

    						 reconAmtPer.add(l);

    						 unReconAmtPer.add(l);

    					 }

    			 }

    			 LinkedHashMap map=new LinkedHashMap();

    			 map.put("labelValue", labelValue);

    			 map.put("labelDates", labelDates);

    			 map.put("recon", recon);

    			 map.put("unRecon", unRecon);

    			 map.put("reconAmtPer", reconAmtPer);

    			 map.put("unReconAmtPer", unReconAmtPer);


    			 wmap.put("reconAmount", Double.valueOf(dform.format(totalReconAmt)));
    			 
    			Double totalReconAmtPerr=0d;
    			if(totaldvCt>0)
    				totalReconAmtPerr=(totalReconAmt/totaldvCt)*100;
    			 
    			 log.info("totalReconAmtPer :"+totalReconAmtPerr);

    			 wmap.put("reconPercentage", Double.valueOf(dform.format(totalReconAmtPerr)));

    			 wmap.put("unReconAmount", Double.valueOf(dform.format(totalUnReconAmt)));

    			 /*if(totalReconAmtPer>0)
    				 wmap.put("unReconPercentage",Double.valueOf(dform.format(100-totalReconAmtPer)));
    			 else
    				 wmap.put("unReconPercentage",0);*/
    			 log.info("totalUnReconAmtPer :"+totalUnReconAmtPer);
    			 if(totaldvCt>0)
    			 wmap.put("unReconPercentage",Double.valueOf(dform.format(100-totalReconAmtPer))); 
    			 else
    			 wmap.put("unReconPercentage",0); 

    			 wmap.put("reconCt",totalReconCt);
    			// log.info("totaldvCt :"+totaldvCt);
    			 if(totaldvCt>0)
    			 {
    				// log.info("in if reconciliation count :"+totaldvCt);
    				// log.info("in if reconciliation totalReconCt :"+totalReconCt);
    				// Double reconCtPer=(totalReconCt/totaldvCt)*100;
    				 BigDecimal reconCtBD=BigDecimal.valueOf(totalReconCt);
    				 log.info("in if reconciliation reconCtBD :"+reconCtBD);
    				 BigDecimal totaldvCtBD=BigDecimal.valueOf(totaldvCt);
    				 log.info("totaldvCtBD bigDecimal:"+totaldvCtBD);
    				 BigDecimal totalPer=new BigDecimal(100);
    				 BigDecimal reconCtPer=reconCtBD.divide(totaldvCtBD,2,BigDecimal.ROUND_FLOOR);
    				 System.out.println("reconCtPer :"+reconCtPer);
    				 reconCtPer=reconCtPer.multiply(totalPer);
    				 System.out.println("final per :"+reconCtPer);
    				 wmap.put("reconCtPer",Double.valueOf(dform.format(reconCtPer)));
    				 
    				 
    					BigDecimal unRecctPer=totalPer.subtract(reconCtPer);
    					 wmap.put("unReconCtPer",Double.valueOf(dform.format(unRecctPer)));
    				// BigDecimal unReconCtPer=totalPer.subtract(reconCtPerBD);
    				 // wmap.put("unReconCtPer",(unReconCtPer).setScale(2, BigDecimal.ROUND_UP));
    			 }
    			 else
    			 {
    				 log.info("in else reconciliation");
    				 wmap.put("reconCtPer",0); 
    				 // wmap.put("unReconCtPer",0);
    			 }
    			 Double unReconCt=(totaldvCt-totalReconCt);
    			 wmap.put("unReconCt", Double.valueOf(dform.format(unReconCt)));
    		/*	Double unRecctPer=0d;
    				unRecctPer=	100-reconCtPer;
    			 log.info("unRecctPer *******:"+unRecctPer);
    			 wmap.put("unReconCtPer",Double.valueOf(dform.format(unRecctPer)));*/

    			 wmap.put("detailedData", map);

    			 // eachMap.put("OneWeek", wmap);
    		// }
    		/* else
    		 {


    			 log.info("fDate :"+fDate);

    			 log.info("tDate :"+tDate);
    			 LocalDate till1wDate=tDate;
    			 LocalDate till1wDateFrLoop=tDate;
    			 log.info("tillDate :"+till1wDate);
    			 LocalDate from1wDate=tDate.minusDays(14);
    			 log.info(" in if 1W fromDate :"+from1wDate);
    			 log.info(" in if fDate :"+fDate);
    			 List<LocalDate> datesList=new ArrayList<LocalDate>();



    			 List<String> labelValue=new ArrayList<String>();

    			 List<String> labelDates=new ArrayList<String>();

    			 List<Double> recon=new ArrayList<Double>();

    			 List<Double> unRecon=new ArrayList<Double>();
    			 List<Double> reconAmtPer=new ArrayList<Double>();
    			 List<Double> unReconAmtPer=new ArrayList<Double>();
    			 log.info("for one week");

    			 while(till1wDateFrLoop.isAfter(from1wDate)){
    				 datesList.add(till1wDateFrLoop);
    				 till1wDateFrLoop=till1wDateFrLoop.minusDays(1);
    				 System.out.println("end of while till1wDateFrLoop :"+till1wDateFrLoop);
    			 }

    			 log.info("datesList :"+datesList);


    			 log.info("procesRecDet :"+procesRecDet);

    			 log.info("fromDate befor query :"+from1wDate);

    			 log.info("tillDate befor query :"+till1wDateFrLoop);

    			 List< Object[]> reconSummary=appModuleSummaryRepository.fetchReconCountAndUnReconciledCountWithDates(procesRecDet.getTypeId(),from1wDate,till1wDate);

    			 log.info("reconSummary size :"+reconSummary.size());

    			 //List<LinkedHashMap> datesMapList=new ArrayList<LinkedHashMap>();

    			 Double totalReconAmt=0d;

    			 Double totaldvAmt=0d;

    			 Double totalUnReconAmt=0d;

    			 Double totalReconAmtPer=0d;

    			 Double totalUnReconCt=0d;
    			 Double totalReconCt=0d;

    			 Double totalReconCtPer=0d;

    			 Double totaldvCt=0d;

    			 Double totalUnReconAmtPer=0d;
    			 Double totalUnReconCtPer=0d;

    			 // Double totalUnReconPer=0d;


    			 LinkedHashMap datesMap=new LinkedHashMap();

    			 for(int i=0;i<reconSummary.size();i++)

    			 {

    				 if(reconSummary!=null)

    				 {

    					 log.info("reconSummary.get(i)[8].toString() :"+reconSummary.get(i)[8].toString());

    					 log.info("reconSummary.get(i)[2].toString() :"+reconSummary.get(i)[2].toString());

    					 log.info("reconSummary.get(i)[3].toString() :"+reconSummary.get(i)[3].toString());

    					 totaldvCt=totaldvAmt+Double.valueOf(reconSummary.get(i)[0].toString());

    					 totaldvAmt=totaldvAmt+Double.valueOf(reconSummary.get(i)[4].toString());

    					 totalReconAmt=totalReconAmt+Double.valueOf(reconSummary.get(i)[5].toString());

    					 totalReconAmtPer=totalReconAmtPer+Double.valueOf(reconSummary.get(i)[6].toString());

    					 totalReconCt=totalReconCt+Double.valueOf(reconSummary.get(i)[1].toString());

    					 totalUnReconAmtPer=totalUnReconAmtPer+Double.valueOf(reconSummary.get(i)[7].toString());

    					 totalUnReconCtPer=totalUnReconCtPer+Double.valueOf(reconSummary.get(i)[3].toString());


    					 datesMap.put(reconSummary.get(i)[8].toString()+"countPer", reconSummary.get(i)[2].toString()+","+reconSummary.get(i)[3].toString());
    					 datesMap.put(reconSummary.get(i)[8].toString()+"amtPer", reconSummary.get(i)[6].toString()+","+reconSummary.get(i)[7].toString());




    				 }



    			 }



    			 log.info("totalReconAmt :"+totalReconAmt); 

    			 log.info("totaldvAmt :"+totaldvAmt); 

    			 log.info("totalReconAmtPer :"+totalReconAmtPer); 


    			 totalUnReconAmt=totaldvAmt-totalReconAmt;

    			 log.info("totalUnReconAmt :"+totalUnReconAmt); 



    			 log.info("datesMap :"+datesMap);



    			 for(int d=0;d<datesList.size();d++)

    			 {

    				 log.info("datesList.get(d) :"+datesList.get(d));

    				 LocalDate dt=LocalDate.parse(datesList.get(d).toString());

    				 String month=dateFormat(datesList.get(d).toString());

    				 labelValue.add(dt.getDayOfMonth()+" "+month);

    				 labelDates.add(datesList.get(d).toString());

    				 if(datesMap.get(datesList.get(d).toString()+"countPer")!=null)

    				 {

    					 // DateFormatSymbols().getMonths()[month]

    					 log.info("in if datesList.get(d)key :"+datesMap.get(datesList.get(d).toString()));

    					 String[] split=datesMap.get(datesList.get(d).toString()+"countPer").toString().split(",");

    					 log.info("split [0]"+split[0]);

    					 log.info("split [1]"+split[1]);

    					 recon.add(Double.valueOf(split[0]));

    					 unRecon.add(Double.valueOf(split[1]));

    				 }

    				 else

    				 {

    					 log.info("in else ct per ");

    					 Double l=0d;

    					 recon.add(l);

    					 unRecon.add(l);

    				 }
    				 if(datesMap.get(datesList.get(d).toString()+"amtPer")!=null)

    				 {

    					 // DateFormatSymbols().getMonths()[month]

    					 log.info("in if datesList.get(d)key :"+datesMap.get(datesList.get(d).toString()));

    					 String[] split=datesMap.get(datesList.get(d).toString()+"amtPer").toString().split(",");

    					 log.info("split [0]"+split[0]);

    					 log.info("split [1]"+split[1]);

    					 reconAmtPer.add(Double.valueOf(split[0]));

    					 unReconAmtPer.add(Double.valueOf(split[1]));

    				 }
    				 else

    				 {
    					 log.info("in else amt Per");

    					 Double l=0d;

    					 reconAmtPer.add(l);

    					 unReconAmtPer.add(l);

    				 }


    			 }

    			 log.info("labelValue :"+labelValue);

    			 log.info("recon :"+recon);

    			 log.info("unRecon :"+unRecon);

    			 LinkedHashMap map=new LinkedHashMap();

    			 map.put("labelValue", labelValue);

    			 map.put("labelDates", labelDates);

    			 map.put("recon", recon);

    			 map.put("unRecon", unRecon);

    			 map.put("reconAmtPer", reconAmtPer);

    			 map.put("unReconAmtPer", unReconAmtPer);


    			 wmap.put("reconAmount", Double.valueOf(dform.format(totalReconAmt)));

    			 wmap.put("reconPercentage", Double.valueOf(dform.format(totalReconAmtPer)));

    			 wmap.put("unReconAmount", Double.valueOf(totalUnReconAmt));

    			  if(totalReconAmtPer>0)
    				 wmap.put("unReconPercentage",Double.valueOf(dform.format(100-totalReconAmtPer)));
    			 else
    				 wmap.put("unReconPercentage",0);

    			 log.info("totalUnReconAmtPer :"+totalUnReconAmtPer);
    			 wmap.put("unReconPercentage",Double.valueOf(dform.format(totalUnReconAmtPer))); 


    			 wmap.put("reconCt",totalReconCt);
    			 if(totaldvCt>0)
    			 {

    				 Double reconCtPer=(totalReconCt/totaldvCt)*100;
    				 BigDecimal reconCtPerBD=BigDecimal.valueOf(reconCtPer);


    				 wmap.put("reconCtPer",reconCtPerBD.setScale(2, BigDecimal.ROUND_UP));
    				 BigDecimal totalPer=new BigDecimal(100);
    				 BigDecimal unReconCtPer=totalPer.subtract(reconCtPerBD);
    				 // wmap.put("unReconCtPer",(unReconCtPer).setScale(2, BigDecimal.ROUND_UP));

    			 }
    			 else
    			 {
    				 wmap.put("reconCtPer",0); 
    				 // wmap.put("unReconCtPer",0);
    			 }
    			 Double unReconCt=(totaldvCt-totalReconCt);
    			 wmap.put("unReconCt", Double.valueOf(dform.format(unReconCt)));

    			 log.info("totalUnReconCtPer :"+totalUnReconCtPer);
    			 wmap.put("unReconCtPer",Double.valueOf(dform.format(totalUnReconCtPer)));

    			 wmap.put("detailedData", map);

    			 // eachMap.put("OneWeek", wmap);

    		 }*/
    	 }
    	 return wmap;

     }
     
    
     public LinkedHashMap accountingAndJournalAnalysisforGivenWeek(Long processId,LocalDate from1wDate,LocalDate till1wDate) throws SQLException, ParseException 
     { 
    	 log.info("Rest Request to get aging analysis in service startDate:"+from1wDate+" and end date :"+till1wDate);
    	 LinkedHashMap wmap=new LinkedHashMap();
    	

    	 DecimalFormat dform = new DecimalFormat("####0.00");

    	 if(processId!=null)

    	 {
    		 ProcessDetails procesRecDet=processDetailsRepository.findByProcessIdAndTagType(processId, "accountingRuleGroup");
    		// if(week.equalsIgnoreCase("oneWeek"))
    		// {

    			
    			 LocalDate till1wDateFrLoop=till1wDate;
    	
    		
    			 List<LocalDate> datesList=new ArrayList<LocalDate>();

    			 log.info(" in if 1W fromDate :"+from1wDate);
    		
    			 List<String> labelValue=new ArrayList<String>();
    			 List<Double> finalNotAccounted=new ArrayList<Double>();

    			 List<String> labelDates=new ArrayList<String>();

    			 while(till1wDateFrLoop.isAfter(from1wDate)){
    				 datesList.add(till1wDateFrLoop);
    				 till1wDateFrLoop=till1wDateFrLoop.minusDays(1);
    				 System.out.println("end of while till1wDateFrLoop :"+till1wDateFrLoop);
    			 }

    		//	 log.info("datesList :"+datesList);



    			 List<Double> accounted=new ArrayList<Double>();
    			 List<Double> accountingInProcess=new ArrayList<Double>();
    			 List<Double> finalAccounted=new ArrayList<Double>();
    			 List<Double> notAccounted=new ArrayList<Double>();

    			 List<Double> accountedAmtPer=new ArrayList<Double>();
    			 List<Double> accountingInProcessAmtPer=new ArrayList<Double>();
    			 List<Double> finalAccountedAmtPer=new ArrayList<Double>();
    			 List<Double> notAccountedAmtPer=new ArrayList<Double>();


    		//	 log.info("datesList in Acounting For a week:"+datesList);


    			 log.info("fromDate befor query :"+from1wDate);

    			 log.info("tillDate befor query :"+till1wDate);
    			 List< Object[]> acctSummary=new ArrayList<Object[]>();
    				 acctSummary=appModuleSummaryRepository.fetchAccountingAnalysisWithDates(procesRecDet.getTypeId(),from1wDate,till1wDate); 

    			 log.info("reconSummary size :"+acctSummary.size());


    			 Double totalActAmt=0d;
    			 Double totalActInProcAmt=0d;
    			 Double totalFinalActAmt=0d;
    			 Double totalNotActAmt=0d;
    			 Double totalActAmtPer=0d;
    			 Double totalActInProcPer=0d;
    			 Double totalFinalActPer=0d;
    			 Double totalNotActPer=0d;

    			 /**counts**/
    			 Double totalActCt=0d;
    			 Double totalActInProcCt=0d;
    			 Double totalFinalActCt=0d;
    			 Double totalNotActCt=0d;
    			 Double totalActCtPer=0d;
    			 Double totalActInProcCtPer=0d;
    			 Double totalFinalActCtPer=0d;
    			 Double totalNotActCtPer=0d;


    			 LinkedHashMap datesMap=new LinkedHashMap();
    			 for(int i=0;i<acctSummary.size();i++)
    			 {
    				 if(acctSummary!=null)
    				 {
    					// log.info("acctSummary.get(i)[5] :"+acctSummary.get(i)[5]);
    					// log.info("acctSummary.get(i)[7] :"+acctSummary.get(i)[7]);
    					// log.info("acctSummary.get(i)[3] :"+acctSummary.get(i)[3]);
    					// log.info("acctSummary.get(i)[6] :"+acctSummary.get(i)[6]);
    					 datesMap.put(acctSummary.get(i)[0].toString()+acctSummary.get(i)[1].toString(), acctSummary.get(i)[6]);
    					 datesMap.put(acctSummary.get(i)[0].toString()+"AmtPer"+acctSummary.get(i)[1].toString(), acctSummary.get(i)[7]);
    					 if(acctSummary.get(i)[0].toString().equalsIgnoreCase("Accounted"))
    					 {
    						// log.info("totalActCtPer :"+totalActCtPer);
    						// log.info("acctSummary.get(i)[6].toString() :"+acctSummary.get(i)[6].toString());
    						 if(acctSummary.get(i)[5]!=null)
    							 totalActAmt=totalActAmt+Double.valueOf(acctSummary.get(i)[5].toString());
    						 if(acctSummary.get(i)[7]!=null)
    							 totalActAmtPer=totalActAmtPer+Double.valueOf(acctSummary.get(i)[7].toString());
    						 if(acctSummary.get(i)[3]!=null)
    							 totalActCt=totalActCt+Double.valueOf(acctSummary.get(i)[3].toString());
    						 if(acctSummary.get(i)[6]!=null)
    							 totalActCtPer=totalActCtPer+Double.valueOf(acctSummary.get(i)[6].toString());
    					 }
    					 if(acctSummary.get(i)[0].toString().equalsIgnoreCase("Accounting inprocess") || acctSummary.get(i)[0].toString().equalsIgnoreCase("inprocess"))
    					 {
    						 if(acctSummary.get(i)[5]!=null)
    							 totalActInProcAmt=totalActInProcAmt+Double.valueOf(acctSummary.get(i)[5].toString());
    						 if(acctSummary.get(i)[7]!=null)
    							 totalActInProcPer=totalActInProcPer+Double.valueOf(acctSummary.get(i)[7].toString());
    						 if(acctSummary.get(i)[3]!=null)
    							 totalActInProcCt=totalActInProcCt+Double.valueOf(acctSummary.get(i)[3].toString());
    						 if(acctSummary.get(i)[6]!=null)
    							 totalActInProcCtPer=totalActInProcCtPer+Double.valueOf(acctSummary.get(i)[6].toString());
    					 }
    					 if(acctSummary.get(i)[0].toString().equalsIgnoreCase("Final accounted"))
    					 {
    						 if(acctSummary.get(i)[5]!=null)
    							 totalFinalActAmt=totalFinalActAmt+Double.valueOf(acctSummary.get(i)[5].toString());
    						 if(acctSummary.get(i)[7]!=null)
    							 totalFinalActPer=totalFinalActPer+Double.valueOf(acctSummary.get(i)[7].toString());
    						 if(acctSummary.get(i)[3]!=null)
    							 totalFinalActCt=totalFinalActCt+Double.valueOf(acctSummary.get(i)[3].toString());
    						 if(acctSummary.get(i)[6]!=null)
    							 totalFinalActCtPer=totalFinalActCtPer+Double.valueOf(acctSummary.get(i)[6].toString());
    					 }
    					 if(acctSummary.get(i)[0].toString().equalsIgnoreCase("Not accounted"))
    					 {
    						 if(acctSummary.get(i)[5]!=null)
    							 totalNotActAmt=totalNotActAmt+Double.valueOf(acctSummary.get(i)[5].toString());
    						 if(acctSummary.get(i)[7]!=null)
    							 totalNotActPer=totalNotActPer+Double.valueOf(acctSummary.get(i)[7].toString());
    						 if(acctSummary.get(i)[3]!=null)
    							 totalNotActCt=totalNotActCt+Double.valueOf(acctSummary.get(i)[3].toString());
    						 if(acctSummary.get(i)[7]!=null)
    							 totalNotActCtPer=totalNotActCtPer+Double.valueOf(acctSummary.get(i)[6].toString());
    					 }

    				 }

    			 }


    			// log.info("totalActAmt :"+totalActAmt); 
    			// log.info("totalActInProcAmt :"+totalActInProcAmt); 
    			// log.info("totalFinalActAmt :"+totalFinalActAmt); 
    			// log.info("totalNotActAmt :"+totalNotActAmt); 
    			// log.info("totalActAmtPer :"+totalActAmtPer); 
    			// log.info("totalActInProcPer :"+totalActInProcPer); 
    			// log.info("totalFinalActPer :"+totalFinalActPer); 
    			// log.info("totalNotActPer :"+totalNotActPer); 


    			// log.info("datesMap :"+datesMap);
    			 Double l=0d;
    			 for(int d=0;d<datesList.size();d++)
    			 {
    				
    				// log.info("datesList.get(d) accounting:"+datesList.get(d));
    				 LocalDate dt=LocalDate.parse(datesList.get(d).toString());
    				 String month=dateFormat(datesList.get(d).toString());
    				 labelValue.add(dt.getDayOfMonth()+" "+month);
    				 labelDates.add(datesList.get(d).toString());
    				 // Double acted=0d;

    				 if(datesMap.get("Accounted"+datesList.get(d).toString())!=null)
    				 {
    					 // DateFormatSymbols().getMonths()[month]
    					// log.info("in if datesList.get(d)key :"+datesMap.get("Accounted"+datesList.get(d).toString()));
    					 // String[] split=datesMap.get(datesList.get(d).toString()).toString().split(",");
    					 //	 acted=Double.valueOf(datesMap.get("Accounted"+datesList.get(d).toString()).toString());
    					 accounted.add(Double.valueOf(datesMap.get("Accounted"+datesList.get(d).toString()).toString()));
    					 accountedAmtPer.add(Double.valueOf(datesMap.get("AccountedAmtPer"+datesList.get(d).toString()).toString()));

    					 // unRecon.add(split[1]);
    				 }
    				 else
    				 {
    					 accounted.add(l);
    					 accountedAmtPer.add(l);
    				 }
    				 if (datesMap.get("Accounting inprocess"+datesList.get(d).toString())!=null ||datesMap.get("inprocess".toLowerCase()+datesList.get(d).toString())!=null )
    				 {
    					 log.info("in Accounting inprocess : "+datesMap.get("Accounting inprocess"+datesList.get(d).toString()));

    					 accountingInProcess.add(Double.valueOf(datesMap.get("Accounting inprocess"+datesList.get(d).toString()).toString()));
    					 accountingInProcessAmtPer.add(Double.valueOf(datesMap.get("Accounting inprocessAmtPer"+datesList.get(d).toString()).toString()));

    				 }
    				 else
    				 {
    					 accountingInProcess.add(l);
    					 accountingInProcessAmtPer.add(l);
    				 }
    				 if (datesMap.get("Final accounted"+datesList.get(d).toString())!=null)
    				 {
    					 // Double finalNtAct=acted-Double.valueOf(datesMap.get("Final Accounted"+datesList.get(d).toString()).toString());
    					 // finalNotAccounted.add(finalNtAct);
    					 log.info("in Final accounted : "+datesMap.get("Final accounted"+datesList.get(d).toString()));
    					 finalAccounted.add(Double.valueOf(datesMap.get("Final accounted"+datesList.get(d).toString()).toString()));
    					 finalAccountedAmtPer.add(Double.valueOf(datesMap.get("Final accountedAmtPer"+datesList.get(d).toString()).toString()));

    				 }
    				 else
    				 {
    					 finalAccounted.add(l);
    					 finalAccountedAmtPer.add(l);
    				 }
    				 if (datesMap.get("Not accounted"+datesList.get(d).toString())!=null)
    				 {

    					 log.info("in Not accounted : "+datesMap.get("Not accounted"+datesList.get(d).toString()));
    					 notAccounted.add(Double.valueOf(datesMap.get("Not accounted"+datesList.get(d).toString()).toString()));
    					 notAccountedAmtPer.add(Double.valueOf(datesMap.get("Not accountedAmtPer"+datesList.get(d).toString()).toString()));

    				 }
    				 else
    				 {
    					 notAccounted.add(l);
    					 notAccountedAmtPer.add(l);
    				 }



    			

    			 }
    			// log.info("labelValue :"+labelValue);
    			// log.info("accounted :"+accounted);
    			// log.info("accountingInProcess :"+accountingInProcess);
    			// log.info("finalAccounted :"+finalAccounted);
    			// log.info("notAccounted :"+notAccounted);

    			 /**adding accounted and accounting inprocess**/
    			 List<Double> result = IntStream.range(0, accountingInProcess.size())
    					 .mapToObj(i -> Double.valueOf((dform.format(notAccounted.get(i) + accountingInProcess.get(i)).toString())))
    					 .collect(Collectors.toList());



    			 List<Double> resultAmtPer = IntStream.range(0, accountingInProcessAmtPer.size())
    					 .mapToObj(i -> Double.valueOf((dform.format(notAccountedAmtPer.get(i) + accountingInProcessAmtPer.get(i)).toString())))
    					 .collect(Collectors.toList());

    			 LinkedHashMap map=new LinkedHashMap();
    			 map.put("labelValue", labelValue);
    			 map.put("labelDates", labelDates);
    			 map.put("accounted", accounted);
    			 map.put("accountingInProcess", accountingInProcess);
    			 map.put("finalAccounted", finalAccounted);
    			 map.put("finalNotAccounted", finalNotAccounted);
    			 map.put("notAccounted", notAccounted);
    			 map.put("notActAndAcctInProc", result);



    			 map.put("accountedAmtPer", accountedAmtPer);
    			 map.put("accountingInProcessAmtPer", accountingInProcessAmtPer);
    			 map.put("finalAccountedAmtPer", finalAccountedAmtPer);

    			 map.put("notAccountedAmtPer", notAccountedAmtPer);
    			 map.put("notActAndAcctInProcAmtPer", resultAmtPer);


    			 // weekMap



    			 wmap.put("actamount", Double.valueOf(dform.format(totalActAmt)));
    			 wmap.put("actPercentage", Double.valueOf(dform.format(totalActAmtPer)));
    			 wmap.put("actInProcessamount", Double.valueOf(dform.format(totalActInProcAmt)));
    			 wmap.put("actInProcessPercentage", Double.valueOf(dform.format(totalActInProcPer)));
    			 wmap.put("finalActamount", Double.valueOf(dform.format(totalFinalActAmt)));
    			 wmap.put("finalActPercentage", Double.valueOf(dform.format(totalFinalActPer)));
    			 //to get final un posted amount and percentage 
    			 // Double finalNotPostedAmt=totalActAmt-totalFinalActAmt;
    			 // wmap.put("finalUnpostedAmt", Double.valueOf(finalNotPostedAmt));
    			 // Double finalNotPostedPer=totalActAmtPer-totalFinalActPer;
    			 // wmap.put("finalUnpostedPer", Double.valueOf(finalNotPostedPer));

    			 wmap.put("notActamount", totalNotActAmt);
    			 wmap.put("notActPercentage", Double.valueOf( dform.format(totalNotActPer)));

    			 wmap.put("actCt", Double.valueOf(totalActCt));
    			 wmap.put("actCtPer", Double.valueOf(dform.format(totalActCtPer)));
    			 wmap.put("actInProcessCt", Double.valueOf(totalActInProcCt));
    			 wmap.put("actInProcessCtPer", Double.valueOf(dform.format(totalActInProcCtPer)));
    			 wmap.put("finalActCt", Double.valueOf(totalFinalActCt));
    			 wmap.put("finalActCtPer", Double.valueOf(dform.format(totalFinalActCtPer)));
    			 wmap.put("notActCt", Double.valueOf(totalNotActCt));
    			 wmap.put("notActCtPer", Double.valueOf(dform.format(totalNotActCtPer)));

    			 wmap.put("notActAndAcctInProcCt",Double.valueOf( dform.format(Double.valueOf(totalNotActCt)+Double.valueOf(totalActInProcCt))));
    			 wmap.put("notActAndAcctInProcCtPer",Double.valueOf( dform.format(Double.valueOf(totalNotActCtPer)+Double.valueOf(totalActInProcCtPer))));

    			 wmap.put("notActAndAcctInProcAmt",Double.valueOf( dform.format(Double.valueOf(totalNotActAmt)+Double.valueOf(totalActInProcAmt))));
    			 wmap.put("notActAndAcctInProcAmtPer",Double.valueOf( dform.format(Double.valueOf(totalNotActPer)+Double.valueOf(totalActInProcPer))));

    			 wmap.put("detailedData", map);

    	//	 }
    		/* else
    		 {

    			 log.info("fDate :"+fDate);

    			 log.info("tDate :"+tDate);
    			 LocalDate till1wDate=tDate;
    			 LocalDate till1wDateFrLoop=tDate;
    			 log.info("tillDate :"+till1wDate);
    			 LocalDate from1wDate=tDate.minusDays(14);
    			 log.info(" in if 2W fromDate :"+from1wDate);
    			 log.info(" in if fDate :"+fDate);
    			 List<LocalDate> datesList=new ArrayList<LocalDate>();

    			 log.info(" in if 1W fromDate :"+from1wDate);
    			 log.info(" in if fDate :"+fDate);
    			 List<String> labelValue=new ArrayList<String>();
    			 List<Double> finalNotAccounted=new ArrayList<Double>();

    			 List<String> labelDates=new ArrayList<String>();

    			 while(till1wDateFrLoop.isAfter(from1wDate)){
    				 datesList.add(till1wDateFrLoop);
    				 till1wDateFrLoop=till1wDateFrLoop.minusDays(1);
    				 System.out.println("end of while till1wDateFrLoop :"+till1wDateFrLoop);
    			 }

    			 log.info("datesList :"+datesList);



    			 List<Double> accounted=new ArrayList<Double>();
    			 List<Double> accountingInProcess=new ArrayList<Double>();
    			 List<Double> finalAccounted=new ArrayList<Double>();
    			 List<Double> notAccounted=new ArrayList<Double>();

    			 List<Double> accountedAmtPer=new ArrayList<Double>();
    			 List<Double> accountingInProcessAmtPer=new ArrayList<Double>();
    			 List<Double> finalAccountedAmtPer=new ArrayList<Double>();
    			 List<Double> notAccountedAmtPer=new ArrayList<Double>();


    			 log.info("datesList in Acounting For a week:"+datesList);


    			 log.info("fromDate befor query :"+from1wDate);

    			 log.info("tillDate befor query :"+till1wDateFrLoop);
    			 List< Object[]> acctSummary=appModuleSummaryRepository.fetchAccountingAnalysisWithDates(procesRecDet.getTypeId(),from1wDate,till1wDate);
    			 log.info("reconSummary size :"+acctSummary.size());


    			 Double totalActAmt=0d;
    			 Double totalActInProcAmt=0d;
    			 Double totalFinalActAmt=0d;
    			 Double totalNotActAmt=0d;
    			 Double totalActAmtPer=0d;
    			 Double totalActInProcPer=0d;
    			 Double totalFinalActPer=0d;
    			 Double totalNotActPer=0d;

    			 *//**counts**//*
    			 Double totalActCt=0d;
    			 Double totalActInProcCt=0d;
    			 Double totalFinalActCt=0d;
    			 Double totalNotActCt=0d;
    			 Double totalActCtPer=0d;
    			 Double totalActInProcCtPer=0d;
    			 Double totalFinalActCtPer=0d;
    			 Double totalNotActCtPer=0d;


    			 LinkedHashMap datesMap=new LinkedHashMap();
    			 for(int i=0;i<acctSummary.size();i++)
    			 {
    				 if(acctSummary!=null)
    				 {
    					 datesMap.put(acctSummary.get(i)[0].toString()+acctSummary.get(i)[1].toString(), acctSummary.get(i)[6]);
    					 datesMap.put(acctSummary.get(i)[0].toString()+"AmtPer"+acctSummary.get(i)[1].toString(), acctSummary.get(i)[7]);
    					 if(acctSummary.get(i)[0].toString().equalsIgnoreCase("Accounted"))
    					 {
    						 log.info("totalActCtPer :"+totalActCtPer);
    						 log.info("acctSummary.get(i)[6].toString() :"+acctSummary.get(i)[6].toString());
    						 if(acctSummary.get(i)[5]!=null)
    							 totalActAmt=totalActAmt+Double.valueOf(acctSummary.get(i)[5].toString());
    						 if(acctSummary.get(i)[7]!=null)
    							 totalActAmtPer=totalActAmtPer+Double.valueOf(acctSummary.get(i)[7].toString());
    						 if(acctSummary.get(i)[3]!=null)
    							 totalActCt=totalActCt+Double.valueOf(acctSummary.get(i)[3].toString());
    						 if(acctSummary.get(i)[6]!=null)
    							 totalActCtPer=totalActCtPer+Double.valueOf(acctSummary.get(i)[6].toString());
    					 }
    					 if(acctSummary.get(i)[0].toString().equalsIgnoreCase("Accounting inprocess") || acctSummary.get(i)[0].toString().equalsIgnoreCase("inprocess"))
    					 {
    						 if(acctSummary.get(i)[5]!=null)
    							 totalActInProcAmt=totalActInProcAmt+Double.valueOf(acctSummary.get(i)[5].toString());
    						 if(acctSummary.get(i)[7]!=null)
    							 totalActInProcPer=totalActInProcPer+Double.valueOf(acctSummary.get(i)[7].toString());
    						 if(acctSummary.get(i)[3]!=null)
    							 totalActInProcCt=totalActInProcCt+Double.valueOf(acctSummary.get(i)[3].toString());
    						 if(acctSummary.get(i)[6]!=null)
    							 totalActInProcCtPer=totalActInProcCtPer+Double.valueOf(acctSummary.get(i)[6].toString());
    					 }
    					 if(acctSummary.get(i)[0].toString().equalsIgnoreCase("Final accounted"))
    					 {
    						 if(acctSummary.get(i)[5]!=null)
    							 totalFinalActAmt=totalFinalActAmt+Double.valueOf(acctSummary.get(i)[5].toString());
    						 if(acctSummary.get(i)[7]!=null)
    							 totalFinalActPer=totalFinalActPer+Double.valueOf(acctSummary.get(i)[7].toString());
    						 if(acctSummary.get(i)[3]!=null)
    							 totalFinalActCt=totalFinalActCt+Double.valueOf(acctSummary.get(i)[3].toString());
    						 if(acctSummary.get(i)[6]!=null)
    							 totalFinalActCtPer=totalFinalActCtPer+Double.valueOf(acctSummary.get(i)[6].toString());
    					 }
    					 if(acctSummary.get(i)[0].toString().equalsIgnoreCase("Not accounted"))
    					 {
    						 if(acctSummary.get(i)[5]!=null)
    							 totalNotActAmt=totalNotActAmt+Double.valueOf(acctSummary.get(i)[5].toString());
    						 if(acctSummary.get(i)[7]!=null)
    							 totalNotActPer=totalNotActPer+Double.valueOf(acctSummary.get(i)[7].toString());
    						 if(acctSummary.get(i)[3]!=null)
    							 totalNotActCt=totalNotActCt+Double.valueOf(acctSummary.get(i)[3].toString());
    						 if(acctSummary.get(i)[7]!=null)
    							 totalNotActCtPer=totalNotActCtPer+Double.valueOf(acctSummary.get(i)[6].toString());
    					 }

    				 }

    			 }


    			 log.info("totalActAmt :"+totalActAmt); 
    			 log.info("totalActInProcAmt :"+totalActInProcAmt); 
    			 log.info("totalFinalActAmt :"+totalFinalActAmt); 
    			 log.info("totalNotActAmt :"+totalNotActAmt); 
    			 log.info("totalActAmtPer :"+totalActAmtPer); 
    			 log.info("totalActInProcPer :"+totalActInProcPer); 
    			 log.info("totalFinalActPer :"+totalFinalActPer); 
    			 log.info("totalNotActPer :"+totalNotActPer); 


    			 log.info("datesMap :"+datesMap);
    			 Double l=0d;
    			 for(int d=0;d<datesList.size();d++)
    			 {
    				 log.info("datesList.get(d) :"+datesList.get(d));
    				 LocalDate dt=LocalDate.parse(datesList.get(d).toString());
    				 String month=dateFormat(datesList.get(d).toString());
    				 labelValue.add(dt.getDayOfMonth()+" "+month);
    				 labelDates.add(datesList.get(d).toString());
    				 // Double acted=0d;

    				 if(datesMap.get("Accounted"+datesList.get(d).toString())!=null)
    				 {
    					 // DateFormatSymbols().getMonths()[month]
    					 log.info("in if datesList.get(d)key :"+datesMap.get("Accounted"+datesList.get(d).toString()));
    					 // String[] split=datesMap.get(datesList.get(d).toString()).toString().split(",");
    					 //	 acted=Double.valueOf(datesMap.get("Accounted"+datesList.get(d).toString()).toString());
    					 accounted.add(Double.valueOf(datesMap.get("Accounted"+datesList.get(d).toString()).toString()));
    					 accountedAmtPer.add(Double.valueOf(datesMap.get("AccountedAmtPer"+datesList.get(d).toString()).toString()));

    					 // unRecon.add(split[1]);
    				 }
    				 else
    				 {
    					 accounted.add(l);
    					 accountedAmtPer.add(l);
    				 }
    				 if (datesMap.get("Accounting Inprocess"+datesList.get(d).toString())!=null ||datesMap.get("INPROCESS".toLowerCase()+datesList.get(d).toString())!=null )
    				 {
    					 log.info("in Accounting inprocess : "+datesMap.get("Accounting Inprocess"+datesList.get(d).toString()));

    					 accountingInProcess.add(Double.valueOf(datesMap.get("Accounting Inprocess"+datesList.get(d).toString()).toString()));
    					 accountingInProcessAmtPer.add(Double.valueOf(datesMap.get("Accounting InprocessAmtPer"+datesList.get(d).toString()).toString()));

    				 }
    				 else
    				 {
    					 accountingInProcess.add(l);
    					 accountingInProcessAmtPer.add(l);
    				 }
    				 if (datesMap.get("Final Accounted"+datesList.get(d).toString())!=null)
    				 {
    					 // Double finalNtAct=acted-Double.valueOf(datesMap.get("Final Accounted"+datesList.get(d).toString()).toString());
    					 // finalNotAccounted.add(finalNtAct);
    					 log.info("in Final accounted : "+datesMap.get("Final Accounted"+datesList.get(d).toString()));
    					 finalAccounted.add(Double.valueOf(datesMap.get("Final Accounted"+datesList.get(d).toString()).toString()));
    					 finalAccountedAmtPer.add(Double.valueOf(datesMap.get("Final AccountedAmtPer"+datesList.get(d).toString()).toString()));

    				 }
    				 else
    				 {
    					 finalAccounted.add(l);
    					 finalAccountedAmtPer.add(l);
    				 }
    				 if (datesMap.get("Not Accounted"+datesList.get(d).toString())!=null)
    				 {

    					 log.info("in Not accounted : "+datesMap.get("Not Accounted"+datesList.get(d).toString()));
    					 notAccounted.add(Double.valueOf(datesMap.get("Not Accounted"+datesList.get(d).toString()).toString()));
    					 notAccountedAmtPer.add(Double.valueOf(datesMap.get("Not AccountedAmtPer"+datesList.get(d).toString()).toString()));

    				 }
    				 if (datesMap.get("Not accounted"+datesList.get(d).toString())!=null)
    				 {

    					 log.info("in Not accounted : "+datesMap.get("Not accounted"+datesList.get(d).toString()));
    					 notAccounted.add(Double.valueOf(datesMap.get("Not accounted"+datesList.get(d).toString()).toString()));
    					 notAccountedAmtPer.add(Double.valueOf(datesMap.get("Not accountedAmtPer"+datesList.get(d).toString()).toString()));

    				 }
    				 else
    				 {
    					 notAccounted.add(l);
    					 notAccountedAmtPer.add(l);
    				 }





    			 }
    			 log.info("labelValue :"+labelValue);
    			 log.info("accounted :"+accounted);
    			 log.info("accountingInProcess :"+accountingInProcess);
    			 log.info("finalAccounted :"+finalAccounted);
    			 log.info("notAccounted :"+notAccounted);

    			 *//**adding accounted and accounting inprocess**//*
    			 List<Double> result = IntStream.range(0, accountingInProcess.size())
    					 .mapToObj(i -> Double.valueOf((dform.format(notAccounted.get(i) + accountingInProcess.get(i)).toString())))
    					 .collect(Collectors.toList());



    			 List<Double> resultAmtPer = IntStream.range(0, accountingInProcessAmtPer.size())
    					 .mapToObj(i -> Double.valueOf((dform.format(notAccountedAmtPer.get(i) + accountingInProcessAmtPer.get(i)).toString())))
    					 .collect(Collectors.toList());

    			 LinkedHashMap map=new LinkedHashMap();
    			 map.put("labelValue", labelValue);
    			 map.put("labelDates", labelDates);
    			 map.put("accounted", accounted);
    			 map.put("accountingInProcess", accountingInProcess);
    			 map.put("finalAccounted", finalAccounted);
    			 map.put("finalNotAccounted", finalNotAccounted);
    			 map.put("notAccounted", notAccounted);
    			 map.put("notActAndAcctInProc", result);


    			 map.put("accountedAmtPer", accountedAmtPer);
    			 map.put("accountingInProcessAmtPer", accountingInProcessAmtPer);
    			 map.put("finalAccountedAmtPer", finalAccountedAmtPer);

    			 map.put("notAccountedAmtPer", notAccountedAmtPer);
    			 map.put("notActAndAcctInProcAmtPer", resultAmtPer);


    			 // weekMap



    			 wmap.put("actamount", Double.valueOf(dform.format(totalActAmt)));
    			 wmap.put("actPercentage", Double.valueOf(dform.format(totalActAmtPer)));
    			 wmap.put("actInProcessamount", Double.valueOf(dform.format(totalActInProcAmt)));
    			 wmap.put("actInProcessPercentage", Double.valueOf(dform.format(totalActInProcPer)));
    			 wmap.put("finalActamount", Double.valueOf(dform.format(totalFinalActAmt)));
    			 wmap.put("finalActPercentage", Double.valueOf(dform.format(totalFinalActPer)));
    			 //to get final un posted amount and percentage 
    			 // Double finalNotPostedAmt=totalActAmt-totalFinalActAmt;
    			 // wmap.put("finalUnpostedAmt", Double.valueOf(finalNotPostedAmt));
    			 // Double finalNotPostedPer=totalActAmtPer-totalFinalActPer;
    			 // wmap.put("finalUnpostedPer", Double.valueOf(finalNotPostedPer));

    			 wmap.put("notActamount", Double.valueOf(dform.format(totalNotActAmt)));
    			 wmap.put("notActPercentage",Double.valueOf( dform.format(totalNotActPer)));

    			 wmap.put("actCt", Double.valueOf(totalActCt));
    			 wmap.put("actCtPer", Double.valueOf(dform.format(totalActCtPer)));
    			 wmap.put("actInProcessCt", Double.valueOf(dform.format(totalActInProcCt)));
    			 wmap.put("actInProcessCtPer", Double.valueOf(dform.format(totalActInProcCtPer)));
    			 wmap.put("finalActCt", Double.valueOf(dform.format(totalFinalActCt)));
    			 wmap.put("finalActCtPer", Double.valueOf(dform.format(totalFinalActCtPer)));
    			 wmap.put("notActCt", Double.valueOf(dform.format(totalNotActCt)));
    			 wmap.put("notActCtPer", Double.valueOf(dform.format(totalNotActCtPer)));

    			 wmap.put("notActAndAcctInProcCt",Double.valueOf( dform.format(Double.valueOf(totalNotActCt)+Double.valueOf(totalActInProcCt))));
    			 wmap.put("notActAndAcctInProcCtPer",Double.valueOf( dform.format(Double.valueOf(totalNotActCtPer)+Double.valueOf(totalActInProcCtPer))));

    			 wmap.put("notActAndAcctInProcAmt",Double.valueOf( dform.format(Double.valueOf(totalNotActAmt)+Double.valueOf(totalActInProcAmt))));
    			 wmap.put("notActAndAcctInProcAmtPer",Double.valueOf( dform.format(Double.valueOf(totalNotActPer)+Double.valueOf(totalActInProcPer))));

    			 wmap.put("detailedData", map);

    		 }*/
    	 }
    	 return wmap;

     }
     
     
     
     public LinkedHashMap transformationAnalysisforGivenWeek(Long processId,HashMap dates) throws SQLException, ParseException 
     { 
    	 log.info("Rest Request to get aging analysis :"+dates);
    	 LinkedHashMap eachMap=new LinkedHashMap();


    	 DecimalFormat dform = new DecimalFormat("####0.00");

    	 if(processId!=null)

    	 {

    		 ProcessDetails procesRecDet=processDetailsRepository.findByProcessIdAndTagType(processId, "reconciliationRuleGroup");

    		 ZonedDateTime fmDate=ZonedDateTime.parse(dates.get("startDate").toString());

    		 ZonedDateTime toDate=ZonedDateTime.parse(dates.get("endDate").toString());

    		 log.info("fmDate :"+fmDate);

    		 log.info("toDate :"+toDate);

    		 LocalDate fDate=fmDate.toLocalDate();

    		 LocalDate tDate=toDate.toLocalDate();

    		 log.info("fDate :"+fDate);

    		 log.info("tDate :"+tDate);
    		 LocalDate till1wDate=tDate;
    		 LocalDate till1wDateFrLoop=tDate;
    		 log.info("tillDate :"+till1wDate);
    		 LocalDate from1wDate=tDate.minusDays(7);
    		 log.info(" in if 1W fromDate :"+from1wDate);
    		 log.info(" in if fDate :"+fDate);
    		 List<LocalDate> datesList=new ArrayList<LocalDate>();

    		 log.info(" in if 1W fromDate :"+from1wDate);
    		 log.info(" in if fDate :"+fDate);
    	


			 List<String> labelValue=new ArrayList<String>();

			 List<String> labelDates=new ArrayList<String>();

			 List<Double> transformed=new ArrayList<Double>();

			 List<Double> transformationFailed=new ArrayList<Double>();


			 while(till1wDateFrLoop.isAfter(from1wDate)){
				 datesList.add(till1wDateFrLoop);
				 till1wDateFrLoop=till1wDateFrLoop.minusDays(1);
				 System.out.println("end of while till1wDateFrLoop :"+till1wDateFrLoop);
			 }

			// log.info("datesList :"+datesList);


			 /*List<Long> profileIds=new ArrayList<Long>();
			for(BigInteger profile:profileId)
			{
				profileIds.add(profile.longValue());
			}*/
			 log.info("fromDate befor query :"+from1wDate);

			 log.info("tillDate befor query :"+till1wDate);
    		 List<BigInteger> profileId=processDetailsRepository.findTypeIdByProcessIdAndTagType(processId, "sourceProfile");


			 List<Object[]> totalFilesExtracted=sourceFileInbHistoryRepository.fetchTransfomedCountBetweenGivenDate(profileId,from1wDate+"%",till1wDate+"%");



			


			 log.info("totalFilesExtracted size :"+totalFilesExtracted.size());

			 //List<LinkedHashMap> datesMapList=new ArrayList<LinkedHashMap>();

			 // Double totalExtracted=0d;

			 Double totalTransformed=0d;

			 Double totalTransFailedCt=0d;

			 /* Double totalUnReconAmt=0d;

			 Double totalReconAmtPer=0d;

			 Double totalUnReconPer=0d;*/

			 LinkedHashMap datesMap=new LinkedHashMap();

			 for(int i=0;i<totalFilesExtracted.size();i++)

			 {

				 if(totalFilesExtracted!=null)

				 {

					 /*log.info("reconSummary.get(i)[8].toString() :"+reconSummary.get(i)[8].toString());

					 log.info("reconSummary.get(i)[2].toString() :"+reconSummary.get(i)[2].toString());

					 log.info("reconSummary.get(i)[3].toString() :"+reconSummary.get(i)[3].toString());*/

					 totalTransFailedCt=totalTransFailedCt+Double.valueOf(totalFilesExtracted.get(i)[5].toString());

					 totalTransformed=totalTransformed+Double.valueOf(totalFilesExtracted.get(i)[1].toString());


					 datesMap.put(totalFilesExtracted.get(i)[4].toString(), totalFilesExtracted.get(i)[1].toString()+","+totalFilesExtracted.get(i)[5].toString());



				 }

			 }


			 //log.info("datesMap :"+datesMap);



			 for(int d=0;d<datesList.size();d++)

			 {

				// log.info("datesList.get(d) :"+datesList.get(d));

				 LocalDate dt=LocalDate.parse(datesList.get(d).toString());

				 String month=dateFormat(datesList.get(d).toString());

				 labelValue.add(dt.getDayOfMonth()+" "+month);

				 labelDates.add(datesList.get(d).toString());

				 if(datesMap.get(datesList.get(d).toString())!=null)

				 {

					 // DateFormatSymbols().getMonths()[month]

					// log.info("in if datesList.get(d)key :"+datesMap.get(datesList.get(d).toString()));

					 String[] split=datesMap.get(datesList.get(d).toString()).toString().split(",");

					 log.info("split [0]"+split[0]);

					 log.info("split [1]"+split[1]);

					 transformed.add(Double.valueOf(split[0]));

					 transformationFailed.add(Double.valueOf(split[1]));

				 }

				 else

				 {

					 log.info("in else");

					 Double l=0d;

					 transformed.add(l);

					 transformationFailed.add(l);

				 }



			 }

			// log.info("labelValue :"+labelValue);

			// log.info("recon :"+transformed);

			// log.info("unRecon :"+transformationFailed);

			 LinkedHashMap map1=new LinkedHashMap();

			 map1.put("labelValue", labelValue);

			 map1.put("labelDates", labelDates);

			 map1.put("transformed", transformed);

			 map1.put("transformationFailed", transformationFailed);



			 // weekMap

			 LinkedHashMap wmap=new LinkedHashMap();


			 /*	 wmap.put("reconAmount", totalReconAmt);

			 wmap.put("reconPercentage", totalReconAmtPer);*/

			 wmap.put("totalTransformed", totalTransformed);

			 wmap.put("totalTransFailedCt", totalTransFailedCt);

			 wmap.put("detailedData", map1);

			 eachMap.put("oneWeek", wmap);
    	//	 log.info("weekMap :"+eachMap);
    		 //	 finalMap.add(weekMap);

    	 }
    	 return eachMap;

     }
     
     public LinkedHashMap datesMap(String startDate,String endDate)
     {
    
		 LinkedHashMap dataMap=new LinkedHashMap();
		 dataMap.put("startDate", startDate);
		 dataMap.put("endDate", endDate);
		 return dataMap;
     }
     
     
     
	
}
