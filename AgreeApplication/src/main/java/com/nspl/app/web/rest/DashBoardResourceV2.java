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
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;



import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.WordUtils;
import org.joda.time.Days;
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
import com.nspl.app.domain.AccountingData;
import com.nspl.app.domain.ApplicationPrograms;
import com.nspl.app.domain.DataStaging;
import com.nspl.app.domain.DataViews;
import com.nspl.app.domain.DataViewsColumns;
import com.nspl.app.domain.FileTemplateLines;
import com.nspl.app.domain.FileTemplates;
import com.nspl.app.domain.MappingSet;
import com.nspl.app.domain.MappingSetValues;
import com.nspl.app.domain.NotificationBatch;
import com.nspl.app.domain.ProcessDetails;
import com.nspl.app.domain.Processes;
import com.nspl.app.domain.ReconciliationResult;
import com.nspl.app.domain.RuleGroup;
import com.nspl.app.domain.Rules;
import com.nspl.app.domain.SchedulerDetails;
import com.nspl.app.domain.Segments;
import com.nspl.app.domain.SourceFileInbHistory;
import com.nspl.app.domain.SourceProfileFileAssignments;
import com.nspl.app.domain.SourceProfiles;
import com.nspl.app.repository.AccountedSummaryRepository;
import com.nspl.app.repository.AccountingDataRepository;
import com.nspl.app.repository.AppModuleSummaryRepository;
import com.nspl.app.repository.ApplicationProgramsRepository;
import com.nspl.app.repository.DataStagingRepository;
import com.nspl.app.repository.DataViewsColumnsRepository;
import com.nspl.app.repository.DataViewsRepository;
import com.nspl.app.repository.FileTemplateLinesRepository;
import com.nspl.app.repository.FileTemplatesRepository;
import com.nspl.app.repository.JobDetailsRepository;
import com.nspl.app.repository.MappingSetRepository;
import com.nspl.app.repository.MappingSetValuesRepository;
import com.nspl.app.repository.NotificationBatchRepository;
import com.nspl.app.repository.ProcessDetailsRepository;
import com.nspl.app.repository.ProcessesRepository;
import com.nspl.app.repository.ReconciliationResultRepository;
import com.nspl.app.repository.RuleGroupDetailsRepository;
import com.nspl.app.repository.RuleGroupRepository;
import com.nspl.app.repository.RulesRepository;
import com.nspl.app.repository.SchedulerDetailsRepository;
import com.nspl.app.repository.SegmentsRepository;
import com.nspl.app.repository.SourceFileInbHistoryRepository;
import com.nspl.app.repository.SourceProfileFileAssignmentsRepository;
import com.nspl.app.repository.SourceProfilesRepository;
import com.nspl.app.service.DashBoardV2Service;
import com.nspl.app.service.DashBoardV4Service;
import com.nspl.app.service.UserJdbcService;


@RestController
@RequestMapping("/api")
public class DashBoardResourceV2 {



	private final Logger log = LoggerFactory.getLogger(AppModuleSummaryResource.class);


	@Inject
	DataViewsColumnsRepository dataViewsColumnsRepository;

	@Inject
	RulesRepository rulesRepository;

	@Inject
	RuleGroupDetailsRepository ruleGroupDetailsRepository;

	@Inject
	RuleGroupRepository ruleGroupRepository;

	@Inject
	DataViewsRepository dataViewsRepository;

	@Autowired
	Environment env;

	@Inject
	SourceFileInbHistoryRepository sourceFileInbHistoryRepository;

	@Inject
	ProcessDetailsRepository processDetailsRepository;

	@Inject
	SourceProfileFileAssignmentsRepository sourceProfileFileAssignmentsRepository;

	@Inject
	JobDetailsRepository jobDetailsRepository;

	@Inject
	SchedulerDetailsRepository schedulerDetailsRepository;

	@Inject
	ApplicationProgramsRepository applicationProgramsRepository;


	@Inject
	AppModuleSummaryRepository appModuleSummaryRepository;

	@Inject
	DashBoardV2Service dashBoardV2Service;


	@Inject
	ReconciliationResultRepository reconciliationResultRepository;

	@Inject
	UserJdbcService userJdbcService;

	@Inject
	AccountedSummaryRepository accountedSummaryRepository;

	@Inject
	FileTemplateLinesRepository fileTemplateLinesRepository;

	@Inject
	ProcessesRepository processesRepository;


	@Inject
	SourceProfilesRepository sourceProfilesRepository;

	@Inject
	FileTemplatesRepository fileTemplatesRepository;

	@Inject
	AccountingDataRepository accountingDataRepository;

	@Inject
	NotificationBatchRepository notificationBatchRepository;

	@Inject
	SegmentsRepository segmentsRepository;

	@Inject
	MappingSetValuesRepository mappingSetValuesRepository;

	@Inject
	MappingSetRepository mappingSetRepository;
	
	
	@Inject
	DataStagingRepository dataStagingRepository;
	
	
	@Inject
	DashBoardV4Service dashBoardV4Service;

	@PersistenceContext(unitName="default")
	private EntityManager em;

	//   private int violation=1;

	//declaration for round off

	private int round=2;

			private DecimalFormat dform = new DecimalFormat("####0.00");





			@PostMapping("/getEachModuleAnalysisForAProcess")
			@Timed
			public List<LinkedHashMap> getEachModuleAnalysisForAProcess(HttpServletRequest request,@RequestParam String processId,@RequestBody HashMap dates) throws SQLException 
			{
				log.info("Rest Request to get aging analysis :"+dates);
				HashMap map0=userJdbcService.getuserInfoFromToken(request);
				Long tenantId=Long.parseLong(map0.get("tenantId").toString());

				Processes processes = processesRepository.findByTenantIdAndIdForDisplay(tenantId, processId);
				
				List<LinkedHashMap> finalMap=new ArrayList<LinkedHashMap>();


				if(processId!=null)
				{
					List<BigInteger> procDetails=processDetailsRepository.findTypeIdByProcessIdAndTagType(processes.getId(), "sourceProfile");

					ZonedDateTime fmDate=ZonedDateTime.parse(dates.get("startDate").toString());
					ZonedDateTime toDate=ZonedDateTime.parse(dates.get("endDate").toString());
					log.info("fmDate :"+fmDate);
					log.info("toDate :"+toDate);
					java.time.LocalDate fDate=fmDate.toLocalDate();
					java.time.LocalDate tDate=toDate.toLocalDate();
					log.info("fDate :"+fDate);
					log.info("tDate :"+tDate);

					ApplicationPrograms app=applicationProgramsRepository.findByPrgmNameAndTenantId("DataExtraction", tenantId);

					//	DataViews dv=dataViewsRepository.findOne(dvId);
					log.info("fDate :"+fDate);
					log.info("tDate :"+tDate);


					// while(fDate.isBefore(tDate.plusDays(1)))
					//	 while(tDate.plusDays(1).isAfter(fDate))
					//   {
					Double totalCount = 0d;
					log.info("in while tDate :"+tDate.toString());
					LinkedHashMap map=new LinkedHashMap();

					/**
					 * extraction
					 */
					int finalCount=0;
					int extractedCount=0;
					log.info("finalCount :"+finalCount);


					List<LinkedHashMap> templateMapList=new ArrayList<LinkedHashMap>();

					LinkedHashMap tempMap=new LinkedHashMap();


					log.info("fDate1 :"+fDate);	
					log.info("tDate1 :"+tDate);	

					/*List<BigInteger> jobDetails=jobDetailsRepository.findByTenantIdAndProgrammIdAndParameterArgument1(app.getId(),procDetails,fDate+"%",tDate+"%");
					log.info("jobDetails :"+jobDetails);

					if(jobDetails.size()>0)
					{

						log.info("in while");
						List<SchedulerDetails> sch=schedulerDetailsRepository.findSchedulersByJobIds(jobDetails);
						log.info("sch :"+sch);
						for(int i=0;i<sch.size();i++)
						{

							if(sch.get(i).getOozieJobId()!=null)
							{

								if(sch.get(i).getFrequency().equalsIgnoreCase("OnDemand"))
								{finalCount=finalCount+1;//how many has to performed
								extractedCount=extractedCount+1;//how many has performed

								}

								if(sch.get(i).getFrequency().equalsIgnoreCase("OneTime"))
								{ finalCount=finalCount+1;
								extractedCount=extractedCount+1;
								}
								if(sch.get(i).getFrequency().equalsIgnoreCase("hourly"))
								{
									log.info("sch.get(i).getHours() :"+sch.get(i).getHours());

									log.info("tDate.getDayOfWeek()1 :"+tDate.getDayOfWeek());
									if(toDate.isBefore(sch.get(i).getEndDate()))
									{
										Long totalRuns=24/sch.get(i).getHours();
										log.info("totalRuns1 :"+totalRuns.intValue());
										finalCount=finalCount+totalRuns.intValue();
										int extractedFileCount=getOutOfCountForProfileExtractionFromOozieDV2(tDate,sch.get(i).getOozieJobId());
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
										int extractedFileCount=getOutOfCountForProfileExtractionFromOozieDV2(tDate,sch.get(i).getOozieJobId());
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
										int extractedFileCount=getOutOfCountForProfileExtractionFromOozieDV2(tDate,sch.get(i).getOozieJobId());
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
											int extractedFileCount=getOutOfCountForProfileExtractionFromOozieDV2(tDate,sch.get(i).getOozieJobId());
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
											int extractedFileCount=getOutOfCountForProfileExtractionFromOozieDV2(tDate,sch.get(i).getOozieJobId());
											extractedCount=extractedCount+extractedFileCount;

										}
									}


								}
							}
						}
					}*/

					if(procDetails.size()>0)
					{
					List<SourceFileInbHistory> extractionPerProfile=sourceFileInbHistoryRepository.findByProfileIdInAndFileReceivedDateBetween
							(procDetails,fDate+"%",tDate+"%");
					
					map.put("extractedCount", extractionPerProfile.size());
					/*double extractedPer=0d;
					if(finalCount>0)
					{
						extractedPer=(extractedCount/finalCount)*100;
					}
					log.info("extractedPer :"+extractedPer);
					map.put("extracted", Double.valueOf(dform.format(extractedPer)));*/
					if(extractionPerProfile.size()>0)
					map.put("extracted",100l);
					else
						map.put("extracted",0l);	




					/**transformation**/

					List< Object[]> transformedSummary=sourceFileInbHistoryRepository.fetchTransfomedPercentagesBetweenGivenDates(procDetails,fDate+"%",tDate.toString()+"%");
					if(transformedSummary!=null)
					{
						if(transformedSummary.size()>0)
						{
							for(int i=0;i<transformedSummary.size();i++)
							{
								log.info("transformation :"+transformedSummary.size());



								if(transformedSummary.get(i)!=null)
								{
									map.put("transformation", transformedSummary.get(i)[2]);
									if(map.get("transformation")!=null)
										//totalCount=totalCount+Long.valueOf(map.get("transformation").toString());
										log.info("totalCount :"+totalCount);
								}
								else
									map.put("transformation", 0);
								if(transformedSummary.get(i)!=null)
									map.put("ntTransformedPer", transformedSummary.get(i)[3]);
								else
									map.put("ntTransformedPer", 0);
								//finalMap.add(map);
								// fDate=fDate.plusDays(1);

							}
						}
						else
							map.put("transformation", 0);
					}

					}
					/**accounting**/

					ProcessDetails procesActDet=processDetailsRepository.findByProcessIdAndTagType(processes.getId(), "accountingRuleGroup");
					if(procesActDet!=null)
					{
					List< Object[]> actSummary=appModuleSummaryRepository.fetchAccNUnAccInfoBetweenGivenDates(procesActDet.getTypeId(),"JOURNALS_ENTERED",fDate,tDate);
					if(actSummary.size()>0)
					{
						for(int i=0;i<actSummary.size();i++)
						{
							log.info("reconSummary :"+actSummary.size());
							if(actSummary!=null)
							{

								if(actSummary.get(i)[4]!=null)
								{
									map.put("accounting", actSummary.get(i)[4]);
									if(map.get("accounting")!=null)
										//totalCount=totalCount+Long.valueOf(map.get("accounting").toString());
										log.info("totalCount :"+totalCount);
								}
								else
									map.put("accounting", 0);

								
								map.put("accountingAmt", actSummary.get(i)[3]);
								map.put("accountingAmtPer", actSummary.get(i)[5]);
								// finalMap.add(map);
								// fDate=fDate.plusDays(1);

							}
						}
					}
					
					else
					{
						map.put("accounting", 0);
						map.put("accountingAmtPer", 0);
					}
				}

					/**Reconciliation**/
					ProcessDetails procesRecDet=processDetailsRepository.findByProcessIdAndTagType(processes.getId(), "reconciliationRuleGroup");
					
					if(procesRecDet!=null)
					{
					log.info("procesRecDet.getTypeId(): "+procesRecDet.getTypeId()+" fDate: "+fDate+" tDate: "+tDate);
					
					BigDecimal totalDvCount=appModuleSummaryRepository.fetchTotalDvcountForReconGroup(procesRecDet.getTypeId(), fDate, tDate);
					BigDecimal totalDvAmt=appModuleSummaryRepository.fetchTotalDvAmtForReconGroup(procesRecDet.getTypeId(), fDate, tDate);
					
			

					List<Object[]> reconSummary=appModuleSummaryRepository.fetchReconciledAndUnReconciledAmountsAndCounts(procesRecDet.getTypeId(), fDate, tDate,totalDvCount,totalDvAmt);
				
					if(reconSummary!=null && !(reconSummary.isEmpty()))
					{
					map.put("reconCountPer", reconSummary.get(0)[2]);
					map.put("unReconCountPer", reconSummary.get(0)[4]);
					map.put("reconAmtPer", reconSummary.get(0)[6]);
					map.put("unReconAmtPer", reconSummary.get(0)[8]);
					}else
						map.put("reconciliation", 0);
				}
					
				/*	if(reconSummary!=null && !(reconSummary.isEmpty()))
					{
						for(int i=0;i<reconSummary.size();i++)
						{
							log.info("reconSummary :"+reconSummary.size());
							if(reconSummary!=null)
							{

								if(reconSummary.get(i)[2]!=null)
								{
									map.put("reconCountPer", reconSummary.get(i)[1]);
									if(map.get("reconCountPer")!=null)
										//totalCount=totalCount+Long.valueOf(map.get("Reconciliation").toString());
										log.info("totalCount :"+totalCount);
								}
								else
									map.put("reconCountPer", 0);
								if(reconSummary.get(i)[3]!=null)
									map.put("unReconCountPer", reconSummary.get(i)[2]);
								else
									map.put("unReconCountPer", 0);
								
								
								map.put("reconAmtPer", reconSummary.get(i)[3]);
								map.put("unReconAmtPer", reconSummary.get(i)[4]);
								
								
								//finalMap.add(map);


							}
						}
					}*/
					/*else
						map.put("reconciliation", 0);*/

					/**jounals**/


		/*			List< Object[]> journalsSummary=appModuleSummaryRepository.fetchProcessedAndUnProcessedCountBetweenGivenDates(procesActDet.getTypeId(),"Final Accounted",fDate,tDate);
					if(journalsSummary.size()>0)
					{
						for(int i=0;i<journalsSummary.size();i++)
						{
							log.info("journalsSummary :"+journalsSummary.size());
							if(journalsSummary!=null)
							{

								if(journalsSummary.get(i)[2]!=null)
								{
									map.put("journals", journalsSummary.get(i)[2]);
								}
								else
									map.put("journals", 0);

								// finalMap.add(map);
								// fDate=fDate.plusDays(1);

							}
						}
					}
					else
						map.put("journals", 0);*/

					finalMap.add(map);

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
			@PostMapping("/getOutOfCountForProfileExtractionFromOozieDV2")
			@Timed
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

			/**
			 * author:ravali
			 * @param processId
			 * @param tenantId
			 * @param dates
			 * @return
			 * @throws SQLException
			 * @throws ParseException
			 * Desc :APi to fetch grouped reconciliation data for 1W,2W,1M,3M
			 */

			@PostMapping("/reconciliationAnalysisforGivenPeriod")
			@Timed
			public LinkedHashMap reconciliationAnalysisforGivenPeriod(HttpServletRequest request,@RequestParam String processId,@RequestBody HashMap dates) throws SQLException, ParseException 
			{ 
				log.info("Rest Request to get aging analysis :"+dates);
				LinkedHashMap eachMap=new LinkedHashMap();
		    	HashMap map=userJdbcService.getuserInfoFromToken(request);
		      	Long tenantId=Long.parseLong(map.get("tenantId").toString());
		        LinkedHashMap finalMap=new LinkedHashMap();
		        Processes processes = processesRepository.findByTenantIdAndIdForDisplay(tenantId, processId);

				if(processId!=null)

				{
					eachMap=dashBoardV2Service.reconciliationAnalysisforGivenPeriod(processes.getId(), dates);

				}

				return eachMap;
			}






			/**
			 * author :ravali
			 * @param processId
			 * @param tenantId
			 * @param dates
			 * @return
			 * @throws SQLException
			 * @throws ParseException
			 * Desc :APi to fetch grouped accounted data for 1W,2W,1M,3M
			 */

			@PostMapping("/AccountingAnalysisforGivenPeriod")
			@Timed
			public LinkedHashMap accountingAnalysisforGivenPeriod(HttpServletRequest request,@RequestParam String processId,@RequestBody HashMap dates) throws SQLException, ParseException 
			{
				log.info("Rest Request to get aging analysis :"+dates);

				LinkedHashMap finalMap=new LinkedHashMap();
				LinkedHashMap eachMap=new LinkedHashMap();
				
		    	HashMap map2=userJdbcService.getuserInfoFromToken(request);
		      	Long tenantId=Long.parseLong(map2.get("tenantId").toString());
		      	Processes processes = processesRepository.findByTenantIdAndIdForDisplay(tenantId, processId);
				if(processId!=null)
				{
					ProcessDetails procesRecDet=processDetailsRepository.findByProcessIdAndTagType(processes.getId(), "accountingRuleGroup");
					ZonedDateTime fmDate=ZonedDateTime.parse(dates.get("startDate").toString());
					ZonedDateTime toDate=ZonedDateTime.parse(dates.get("endDate").toString());
					log.info("fmDate :"+fmDate);
					log.info("toDate :"+toDate);
					LocalDate fDate=fmDate.toLocalDate();
					LocalDate tDate=toDate.toLocalDate();
					log.info("fDate :"+fDate);
					log.info("tDate :"+tDate);

					/* Duration dur = Duration.between(fmDate, toDate);
    		 log.info("dur.toDays(): "+dur.toDays());*/

					/**Acounting For a week**/
					

					LocalDate from1wDate=tDate.minusDays(7);

					LocalDate till1wDate=tDate;
					LocalDate till1wDateFrLoop=tDate;
					log.info("tillDate :"+till1wDate);
					Long days=Duration.between(fDate.atStartOfDay(), tDate.atStartOfDay()).toDays();
					if(days<=7 || days>=7)
					{


						log.info(" in if 1W fromDate :"+from1wDate);
						log.info(" in if fDate :"+fDate);

						LinkedHashMap WeekMap=new LinkedHashMap();
						if(days<7)
							WeekMap=dashBoardV2Service.accountingAndJournalAnalysisforGivenWeek(processes.getId(), fDate.minusDays(1),tDate);
						else
							WeekMap=dashBoardV2Service.accountingAndJournalAnalysisforGivenWeek(processes.getId(), from1wDate,till1wDate);

						eachMap.put("1W", WeekMap);

						log.info("weekMap :"+eachMap);
						//	 finalMap.add(weekMap);
					}
					/** Accounting 2w**/

					log.info("for 2 week");

					LocalDate from2WDate=tDate.minusDays(14);

					LocalDate till2WDate=tDate;
					LocalDate till2WDateFrLoop=tDate;

					log.info("till2WDate :"+till2WDate);
					if(days>=7)
					{
						LinkedHashMap WeekMap=new LinkedHashMap();
						
						if(days>=7 && days<14)
							WeekMap=dashBoardV2Service.accountingAndJournalAnalysisforGivenWeek(processes.getId(), fDate.minusDays(1),tDate);
						else
						 WeekMap=dashBoardV2Service.accountingAndJournalAnalysisforGivenWeek(processes.getId(), from2WDate,till2WDate);
						eachMap.put("2W", WeekMap);

						

					}

					/**Accounting for a month**/

					LocalDate from6Date = null;
					LocalDate till6Date = null;


					if(days>=14)
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

						List<String> labelValue=new ArrayList<String>();
						List<LinkedHashMap> labelDatesValue=new ArrayList<LinkedHashMap>();
						List<Double> accounted=new ArrayList<Double>();
						List<Double> accountingInProcess=new ArrayList<Double>();
						List<Double> finalAccounted=new ArrayList<Double>();
						List<Double> finalNotPosted=new ArrayList<Double>();
						List<Double> notAccounted=new ArrayList<Double>();

						List<Double> accountedAmtPer=new ArrayList<Double>();
						List<Double> accountingInProcessAmtPer=new ArrayList<Double>();
						List<Double> finalAccountedAmtPer=new ArrayList<Double>();
						List<Double> notAccountedAmtPer=new ArrayList<Double>();

						// log.info("datesList in Acounting For a week:"+datesList);




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



						List< Object[]> acct1WSummary=appModuleSummaryRepository.fetchAccountingAnalysisWithWeeks(procesRecDet.getTypeId(),from1wDate,till1wDate );
						log.info("acct1WSummary size :"+acct1WSummary.size());
						labelDatesValue.add(dashBoardV2Service.datesMap(from1wDate.toString(),till1wDate.toString()));
						labelValue.add("Week-1");
						Double l=0d;
						if(acct1WSummary.size()>0)
						{
							for(int i=0;i<acct1WSummary.size();i++)
							{
								if(acct1WSummary!=null)
								{
									log.info("acct1WSummary.get(i)[0] :"+acct1WSummary.get(i)[0]+"at "+i);
									
									// Double acted=0d;
									if(acct1WSummary.get(i)[0].toString().equalsIgnoreCase("Accounted"))
									{
										accounted.add(Double.valueOf(acct1WSummary.get(i)[5].toString()));
										accountedAmtPer.add(Double.valueOf(acct1WSummary.get(i)[6].toString()));
										if(acct1WSummary.get(i)[4]!=null)
											totalActAmt=totalActAmt+Double.valueOf(acct1WSummary.get(i)[4].toString());
										if(acct1WSummary.get(i)[6]!=null)
											totalActAmtPer=totalActAmtPer+Double.valueOf(acct1WSummary.get(i)[6].toString());

										if(acct1WSummary.get(i)[2]!=null)
											totalActCt=totalActCt+Double.valueOf(acct1WSummary.get(i)[2].toString());
										if(acct1WSummary.get(i)[5]!=null)
											totalActCtPer=totalActCtPer+Double.valueOf(acct1WSummary.get(i)[5].toString());
									}
									if(acct1WSummary.get(i)[0].toString().equalsIgnoreCase("Accounting inprocess") || acct1WSummary.get(i)[0].toString().equalsIgnoreCase("inprocess"))
									{

										accountingInProcess.add(Double.valueOf(acct1WSummary.get(i)[5].toString()));
										accountingInProcessAmtPer.add(Double.valueOf(acct1WSummary.get(i)[6].toString()));

										if(acct1WSummary.get(i)[4]!=null)
											totalActInProcAmt=totalActInProcAmt+Double.valueOf(acct1WSummary.get(i)[4].toString());
										if(acct1WSummary.get(i)[6]!=null)
											totalActInProcPer=totalActInProcPer+Double.valueOf(acct1WSummary.get(i)[6].toString());

										if(acct1WSummary.get(i)[2]!=null)
											totalActInProcCt=totalActInProcCt+Double.valueOf(acct1WSummary.get(i)[2].toString());
										if(acct1WSummary.get(i)[5]!=null)
											totalActInProcCtPer=totalActInProcCtPer+Double.valueOf(acct1WSummary.get(i)[5].toString());
									}
									if(acct1WSummary.get(i)[0].toString().equalsIgnoreCase("Final accounted"))
									{
										log.info("acct1WSummary.get(i)[5].toString() :"+acct1WSummary.get(i)[5].toString());
										finalAccounted.add(Double.valueOf(acct1WSummary.get(i)[5].toString()));
										finalAccountedAmtPer.add(Double.valueOf(acct1WSummary.get(i)[6].toString()));

										// Double finalJournalsNotPosted=acted-Double.valueOf(acct1WSummary.get(i)[4].toString());
										// finalNotPosted.add(Double.valueOf(finalJournalsNotPosted));

										if(acct1WSummary.get(i)[4]!=null)
											totalFinalActAmt=totalFinalActAmt+Double.valueOf(acct1WSummary.get(i)[4].toString());
										if(acct1WSummary.get(i)[6]!=null)
											totalFinalActPer=totalFinalActPer+Double.valueOf(acct1WSummary.get(i)[6].toString());

										if(acct1WSummary.get(i)[2]!=null)
											totalFinalActCt=totalFinalActCt+Double.valueOf(acct1WSummary.get(i)[2].toString());
										if(acct1WSummary.get(i)[5]!=null)
											totalFinalActCtPer=totalFinalActCtPer+Double.valueOf(acct1WSummary.get(i)[5].toString());
									}
									if(acct1WSummary.get(i)[0].toString().equalsIgnoreCase("Not accounted"))
									{

										notAccounted.add(Double.valueOf(acct1WSummary.get(i)[5].toString()));
										notAccountedAmtPer.add(Double.valueOf(acct1WSummary.get(i)[6].toString()));

										if(acct1WSummary.get(i)[4]!=null)
											totalNotActAmt=totalNotActAmt+Double.valueOf(acct1WSummary.get(i)[4].toString());
										if(acct1WSummary.get(i)[6]!=null)
											totalNotActPer=totalNotActPer+Double.valueOf(acct1WSummary.get(i)[6].toString());

										if(acct1WSummary.get(i)[2]!=null)
											totalNotActCt=totalNotActCt+Double.valueOf(acct1WSummary.get(i)[2].toString());
										if(acct1WSummary.get(i)[5]!=null)
											totalNotActCtPer=totalNotActCtPer+Double.valueOf(acct1WSummary.get(i)[5].toString());
									}

								}

							}
						}
						else
						{
							accounted.add(l);
							accountingInProcess.add(l);
							finalAccounted.add(l);
							finalNotPosted.add(l);
							notAccounted.add(l);


							accountedAmtPer.add(l);
							accountingInProcessAmtPer.add(l);
							finalAccountedAmtPer.add(l);
							notAccountedAmtPer.add(l);


						}


						List< Object[]> acct2WSummary=appModuleSummaryRepository.fetchAccountingAnalysisWithWeeks(procesRecDet.getTypeId(),from2wDate,till2wDate);
						log.info("acct1WSummary size :"+acct2WSummary.size());

						labelDatesValue.add(dashBoardV2Service.datesMap(from2wDate.toString(),till2wDate.toString()));

						labelValue.add("Week-2");
						if(acct2WSummary.size()>0)
						{
							for(int i=0;i<acct2WSummary.size();i++)
							{
								if(acct2WSummary!=null)
								{

									// Double acted=0d;
									if(acct2WSummary.get(i)[0].toString().equalsIgnoreCase("Accounted"))
									{

										accounted.add(Double.valueOf(acct2WSummary.get(i)[5].toString()));
										accountedAmtPer.add(Double.valueOf(acct2WSummary.get(i)[6].toString()));
										if(acct2WSummary.get(i)[4]!=null)
											totalActAmt=totalActAmt+Double.valueOf(acct2WSummary.get(i)[4].toString());
										if(acct2WSummary.get(i)[6]!=null)
											totalActAmtPer=totalActAmtPer+Double.valueOf(acct2WSummary.get(i)[6].toString());

										if(acct2WSummary.get(i)[2]!=null)
											totalActCt=totalActCt+Double.valueOf(acct2WSummary.get(i)[2].toString());
										if(acct2WSummary.get(i)[5]!=null)
											totalActCtPer=totalActCtPer+Double.valueOf(acct2WSummary.get(i)[5].toString());
									}
									if(acct2WSummary.get(i)[0].toString().equalsIgnoreCase("Accounting inprocess") || acct2WSummary.get(i)[0].toString().equalsIgnoreCase("inprocess"))
									{

										accountingInProcess.add(Double.valueOf(acct2WSummary.get(i)[5].toString()));
										accountingInProcessAmtPer.add(Double.valueOf(acct2WSummary.get(i)[6].toString()));
										if(acct2WSummary.get(i)[4]!=null)
											totalActInProcAmt=totalActInProcAmt+Double.valueOf(acct2WSummary.get(i)[4].toString());
										if(acct2WSummary.get(i)[6]!=null)
											totalActInProcPer=totalActInProcPer+Double.valueOf(acct2WSummary.get(i)[6].toString());

										if(acct2WSummary.get(i)[2]!=null)
											totalActInProcCt=totalActInProcCt+Double.valueOf(acct2WSummary.get(i)[2].toString());
										if(acct2WSummary.get(i)[5]!=null)
											totalActInProcCtPer=totalActInProcCtPer+Double.valueOf(acct2WSummary.get(i)[5].toString());
									}
									if(acct2WSummary.get(i)[0].toString().equalsIgnoreCase("Final accounted"))
									{

										finalAccounted.add(Double.valueOf(acct2WSummary.get(i)[5].toString()));
										finalAccountedAmtPer.add(Double.valueOf(acct2WSummary.get(i)[6].toString()));
										// Double finalJournalsNotPosted=acted-Double.valueOf(acct2WSummary.get(i)[4].toString());
										// finalNotPosted.add(Double.valueOf(finalJournalsNotPosted));

										// finalNotPosted.add(acted);

										if(acct2WSummary.get(i)[4]!=null)
											totalFinalActAmt=totalFinalActAmt+Double.valueOf(acct2WSummary.get(i)[4].toString());
										if(acct2WSummary.get(i)[6]!=null)
											totalFinalActPer=totalFinalActPer+Double.valueOf(acct2WSummary.get(i)[6].toString());

										if(acct2WSummary.get(i)[2]!=null)
											totalFinalActCt=totalFinalActCt+Double.valueOf(acct2WSummary.get(i)[2].toString());
										if(acct2WSummary.get(i)[5]!=null)
											totalFinalActCtPer=totalFinalActCtPer+Double.valueOf(acct2WSummary.get(i)[5].toString());
									}
									if(acct2WSummary.get(i)[0].toString().equalsIgnoreCase("Not accounted"))
									{

										notAccounted.add(Double.valueOf(acct2WSummary.get(i)[5].toString()));
										notAccountedAmtPer.add(Double.valueOf(acct2WSummary.get(i)[6].toString()));
										if(acct2WSummary.get(i)[4]!=null)
											totalNotActAmt=totalNotActAmt+Double.valueOf(acct2WSummary.get(i)[4].toString());
										if(acct2WSummary.get(i)[6]!=null)
											totalNotActPer=totalNotActPer+Double.valueOf(acct2WSummary.get(i)[6].toString());

										if(acct2WSummary.get(i)[2]!=null)
											totalNotActCt=totalNotActCt+Double.valueOf(acct2WSummary.get(i)[2].toString());
										if(acct2WSummary.get(i)[5]!=null)
											totalNotActCtPer=totalNotActCtPer+Double.valueOf(acct2WSummary.get(i)[5].toString());
									}

								}

							}
						}
						else
						{
							accounted.add(l);
							accountingInProcess.add(l);
							finalAccounted.add(l);
							finalNotPosted.add(l);
							notAccounted.add(l);

							accountedAmtPer.add(l);
							accountingInProcessAmtPer.add(l);
							finalAccountedAmtPer.add(l);
							notAccountedAmtPer.add(l);


						}

						if(from3wDate.isAfter(fDate) || from3wDate.equals(fDate))
						{
							List< Object[]> acct3WSummary=appModuleSummaryRepository.fetchAccountingAnalysisWithWeeks(procesRecDet.getTypeId(),from3wDate,till3wDate);
							log.info("acct3WSummary size :"+acct3WSummary.size());

							labelDatesValue.add(dashBoardV2Service.datesMap(from3wDate.toString(),till3wDate.toString()));
							labelValue.add("Week-3");
							if(acct3WSummary.size()>0)
							{
								for(int i=0;i<acct3WSummary.size();i++)
								{
									if(acct3WSummary!=null)
									{

										//	 Double acted=0d;
										if(acct3WSummary.get(i)[0].toString().equalsIgnoreCase("Accounted"))
										{
											accountedAmtPer.add(Double.valueOf(acct3WSummary.get(i)[6].toString()));
											accounted.add(Double.valueOf(acct3WSummary.get(i)[5].toString()));

											if(acct3WSummary.get(i)[4]!=null)
												totalActAmt=totalActAmt+Double.valueOf(acct3WSummary.get(i)[4].toString());
											if(acct3WSummary.get(i)[6]!=null)
												totalActAmtPer=totalActAmtPer+Double.valueOf(acct3WSummary.get(i)[6].toString());
											if(acct3WSummary.get(i)[2]!=null)
												totalActCt=totalActCt+Double.valueOf(acct3WSummary.get(i)[2].toString());
											if(acct3WSummary.get(i)[5]!=null)
												totalActCtPer=totalActCtPer+Double.valueOf(acct3WSummary.get(i)[5].toString());

										}
										if(acct3WSummary.get(i)[0].toString().equalsIgnoreCase("Accounting inprocess") || acct3WSummary.get(i)[0].toString().equalsIgnoreCase("inprocess"))
										{

											accountingInProcess.add(Double.valueOf(acct3WSummary.get(i)[5].toString()));
											accountingInProcessAmtPer.add(Double.valueOf(acct3WSummary.get(i)[6].toString()));

											if(acct3WSummary.get(i)[4]!=null)
												totalActInProcAmt=totalActInProcAmt+Double.valueOf(acct3WSummary.get(i)[4].toString());
											if(acct3WSummary.get(i)[6]!=null)
												totalActInProcPer=totalActInProcPer+Double.valueOf(acct3WSummary.get(i)[6].toString());

											if(acct3WSummary.get(i)[2]!=null)
												totalActInProcCt=totalActInProcCt+Double.valueOf(acct3WSummary.get(i)[2].toString());
											if(acct3WSummary.get(i)[5]!=null)
												totalActInProcCtPer=totalActInProcCtPer+Double.valueOf(acct3WSummary.get(i)[5].toString());
										}
										if(acct3WSummary.get(i)[0].toString().equalsIgnoreCase("Final accounted"))
										{

											finalAccounted.add(Double.valueOf(acct3WSummary.get(i)[5].toString()));
											finalAccountedAmtPer.add(Double.valueOf(acct3WSummary.get(i)[6].toString()));

											// Double finalJournalsNotPosted=acted-Double.valueOf(acct3WSummary.get(i)[4].toString());
											// finalNotPosted.add(Double.valueOf(finalJournalsNotPosted));

											// finalNotPosted.add(acted);

											if(acct3WSummary.get(i)[4]!=null)
												totalFinalActAmt=totalFinalActAmt+Double.valueOf(acct3WSummary.get(i)[4].toString());
											if(acct3WSummary.get(i)[6]!=null)
												totalFinalActPer=totalFinalActPer+Double.valueOf(acct3WSummary.get(i)[6].toString());

											if(acct3WSummary.get(i)[2]!=null)
												totalFinalActCt=totalFinalActCt+Double.valueOf(acct3WSummary.get(i)[2].toString());
											if(acct3WSummary.get(i)[5]!=null)
												totalFinalActCtPer=totalFinalActCtPer+Double.valueOf(acct3WSummary.get(i)[5].toString());
										}
										if(acct3WSummary.get(i)[0].toString().equalsIgnoreCase("Not accounted"))
										{
											notAccounted.add(Double.valueOf(acct3WSummary.get(i)[5].toString()));
											notAccountedAmtPer.add(Double.valueOf(acct3WSummary.get(i)[6].toString()));

											if(acct3WSummary.get(i)[4]!=null)
												totalNotActAmt=totalNotActAmt+Double.valueOf(acct3WSummary.get(i)[4].toString());
											if(acct3WSummary.get(i)[6]!=null)
												totalNotActPer=totalNotActPer+Double.valueOf(acct3WSummary.get(i)[6].toString());

											if(acct3WSummary.get(i)[2]!=null)
												totalNotActCt=totalNotActCt+Double.valueOf(acct3WSummary.get(i)[2].toString());
											if(acct3WSummary.get(i)[5]!=null)
												totalNotActCtPer=totalNotActCtPer+Double.valueOf(acct3WSummary.get(i)[5].toString());
										}

									}

								}
							}
							else
							{
								accounted.add(l);
								accountingInProcess.add(l);
								finalAccounted.add(l);
								finalNotPosted.add(l);
								notAccounted.add(l);

								accountedAmtPer.add(l);
								accountingInProcessAmtPer.add(l);
								finalAccountedAmtPer.add(l);
								notAccountedAmtPer.add(l);


							}
						}

						if(from4wDate.isAfter(fDate) ||from4wDate.equals(fDate))
						{
							List< Object[]> acct4WSummary=appModuleSummaryRepository.fetchAccountingAnalysisWithWeeks(procesRecDet.getTypeId(),from4wDate,till4wDate);
							log.info("acct4WSummary size :"+acct4WSummary.size());

							labelDatesValue.add(dashBoardV2Service.datesMap(from4wDate.toString(),till4wDate.toString()));
							labelValue.add("Week-4");
							if(acct4WSummary.size()>0)
							{
								for(int i=0;i<acct4WSummary.size();i++)
								{
									if(acct4WSummary!=null)
									{

										// Double acted=0d;
										if(acct4WSummary.get(i)[0].toString().equalsIgnoreCase("Accounted"))
										{

											// acted=acted+Double.valueOf(acct4WSummary.get(i)[5].toString());
											accounted.add(Double.valueOf(acct4WSummary.get(i)[5].toString()));
											accountedAmtPer.add(Double.valueOf(acct4WSummary.get(i)[6].toString()));

											if(acct4WSummary.get(i)[4]!=null)
												totalActAmt=totalActAmt+Double.valueOf(acct4WSummary.get(i)[4].toString());
											if(acct4WSummary.get(i)[6]!=null)
												totalActAmtPer=totalActAmtPer+Double.valueOf(acct4WSummary.get(i)[6].toString());
											if(acct4WSummary.get(i)[2]!=null)
												totalActCt=totalActCt+Double.valueOf(acct4WSummary.get(i)[2].toString());
											if(acct4WSummary.get(i)[5]!=null)
												totalActCtPer=totalActCtPer+Double.valueOf(acct4WSummary.get(i)[5].toString());

										}
										if(acct4WSummary.get(i)[0].toString().equalsIgnoreCase("Accounting inprocess") || acct4WSummary.get(i)[0].toString().equalsIgnoreCase("inprocess"))
										{

											accountingInProcess.add(Double.valueOf(acct4WSummary.get(i)[5].toString()));
											accountingInProcessAmtPer.add(Double.valueOf(acct4WSummary.get(i)[6].toString()));

											if(acct4WSummary.get(i)[4]!=null)
												totalActInProcAmt=totalActInProcAmt+Double.valueOf(acct4WSummary.get(i)[4].toString());
											if(acct4WSummary.get(i)[6]!=null)
												totalActInProcPer=totalActInProcPer+Double.valueOf(acct4WSummary.get(i)[6].toString());

											if(acct4WSummary.get(i)[2]!=null)
												totalActInProcCt=totalActInProcCt+Double.valueOf(acct4WSummary.get(i)[2].toString());
											if(acct4WSummary.get(i)[5]!=null)
												totalActInProcCtPer=totalActInProcCtPer+Double.valueOf(acct4WSummary.get(i)[5].toString());
										}
										if(acct4WSummary.get(i)[0].toString().equalsIgnoreCase("Final accounted"))
										{

											finalAccounted.add(Double.valueOf(acct4WSummary.get(i)[5].toString()));
											//	 Double finalJournalsNotPosted=acted-Double.valueOf(acct4WSummary.get(i)[4].toString());
											//	 finalNotPosted.add(Double.valueOf(finalJournalsNotPosted));
											finalAccountedAmtPer.add(Double.valueOf(acct4WSummary.get(i)[6].toString()));

											if(acct4WSummary.get(i)[4]!=null)
												totalFinalActAmt=totalFinalActAmt+Double.valueOf(acct4WSummary.get(i)[4].toString());
											if(acct4WSummary.get(i)[6]!=null)
												totalFinalActPer=totalFinalActPer+Double.valueOf(acct4WSummary.get(i)[6].toString());

											if(acct4WSummary.get(i)[2]!=null)
												totalFinalActCt=totalFinalActCt+Double.valueOf(acct4WSummary.get(i)[2].toString());
											if(acct4WSummary.get(i)[5]!=null)
												totalFinalActCtPer=totalFinalActCtPer+Double.valueOf(acct4WSummary.get(i)[5].toString());
										}
										if(acct4WSummary.get(i)[0].toString().equalsIgnoreCase("Not accounted"))
										{

											notAccounted.add(Double.valueOf(acct4WSummary.get(i)[5].toString()));
											notAccountedAmtPer.add(Double.valueOf(acct4WSummary.get(i)[6].toString()));

											if(acct4WSummary.get(i)[4]!=null)
												totalNotActAmt=totalNotActAmt+Double.valueOf(acct4WSummary.get(i)[4].toString());
											if(acct4WSummary.get(i)[6]!=null)
												totalNotActPer=totalNotActPer+Double.valueOf(acct4WSummary.get(i)[6].toString());

											if(acct4WSummary.get(i)[2]!=null)
												totalNotActCt=totalNotActCt+Double.valueOf(acct4WSummary.get(i)[2].toString());
											if(acct4WSummary.get(i)[5]!=null)
												totalNotActCtPer=totalNotActCtPer+Double.valueOf(acct4WSummary.get(i)[5].toString());
										}

									}

								}
							}
							else
							{
								accounted.add(l);
								accountingInProcess.add(l);
								finalAccounted.add(l);
								finalNotPosted.add(l);
								notAccounted.add(l);

								accountedAmtPer.add(l);
								accountingInProcessAmtPer.add(l);
								finalAccountedAmtPer.add(l);
								notAccountedAmtPer.add(l);


							}
						}
						if(from5wDate.isAfter(fDate) || from5wDate.equals(fDate))
						{

							List< Object[]> acct5WSummary=appModuleSummaryRepository.fetchAccountingAnalysisWithWeeks(procesRecDet.getTypeId(),from5wDate,till5wDate);
							log.info("acct5WSummary size :"+acct5WSummary.size());

							labelDatesValue.add(dashBoardV2Service.datesMap(from5wDate.toString(),till5wDate.toString()));

							labelValue.add("Week-5");
							if(acct5WSummary.size()>0)
							{
								for(int i=0;i<acct5WSummary.size();i++)
								{
									if(acct5WSummary!=null)
									{

										// Double acted=0d;
										if(acct5WSummary.get(i)[0].toString().equalsIgnoreCase("Accounted"))
										{

											// acted=acted+Double.valueOf(acct5WSummary.get(i)[5].toString());
											accounted.add(Double.valueOf(acct5WSummary.get(i)[5].toString()));
											accountedAmtPer.add(Double.valueOf(acct5WSummary.get(i)[6].toString()));

											if(acct5WSummary.get(i)[4]!=null)
												totalActAmt=totalActAmt+Double.valueOf(acct5WSummary.get(i)[4].toString());
											if(acct5WSummary.get(i)[6]!=null)
												totalActAmtPer=totalActAmtPer+Double.valueOf(acct5WSummary.get(i)[6].toString());
											if(acct5WSummary.get(i)[2]!=null)
												totalActCt=totalActCt+Double.valueOf(acct5WSummary.get(i)[2].toString());
											if(acct5WSummary.get(i)[5]!=null)
												totalActCtPer=totalActCtPer+Double.valueOf(acct5WSummary.get(i)[5].toString());

										}
										if(acct5WSummary.get(i)[0].toString().equalsIgnoreCase("Accounting inprocess") || acct5WSummary.get(i)[0].toString().equalsIgnoreCase("inprocess"))
										{

											accountingInProcess.add(Double.valueOf(acct5WSummary.get(i)[5].toString()));
											accountingInProcessAmtPer.add(Double.valueOf(acct5WSummary.get(i)[6].toString()));

											if(acct5WSummary.get(i)[4]!=null)
												totalActInProcAmt=totalActInProcAmt+Double.valueOf(acct5WSummary.get(i)[4].toString());
											if(acct5WSummary.get(i)[6]!=null)
												totalActInProcPer=totalActInProcPer+Double.valueOf(acct5WSummary.get(i)[6].toString());

											if(acct5WSummary.get(i)[2]!=null)
												totalActInProcCt=totalActInProcCt+Double.valueOf(acct5WSummary.get(i)[2].toString());
											if(acct5WSummary.get(i)[5]!=null)
												totalActInProcCtPer=totalActInProcCtPer+Double.valueOf(acct5WSummary.get(i)[5].toString());
										}
										if(acct5WSummary.get(i)[0].toString().equalsIgnoreCase("Final accounted"))
										{

											finalAccounted.add(Double.valueOf(acct5WSummary.get(i)[5].toString()));
											finalAccountedAmtPer.add(Double.valueOf(acct5WSummary.get(i)[6].toString()));

											if(acct5WSummary.get(i)[4]!=null)
												totalFinalActAmt=totalFinalActAmt+Double.valueOf(acct5WSummary.get(i)[4].toString());
											if(acct5WSummary.get(i)[6]!=null)
												totalFinalActPer=totalFinalActPer+Double.valueOf(acct5WSummary.get(i)[6].toString());

											if(acct5WSummary.get(i)[2]!=null)
												totalFinalActCt=totalFinalActCt+Double.valueOf(acct5WSummary.get(i)[2].toString());
											if(acct5WSummary.get(i)[5]!=null)
												totalFinalActCtPer=totalFinalActCtPer+Double.valueOf(acct5WSummary.get(i)[5].toString());
										}
										if(acct5WSummary.get(i)[0].toString().equalsIgnoreCase("Not accounted"))
										{

											notAccounted.add(Double.valueOf(acct5WSummary.get(i)[5].toString()));
											notAccountedAmtPer.add(Double.valueOf(acct5WSummary.get(i)[6].toString()));

											if(acct5WSummary.get(i)[4]!=null)
												totalNotActAmt=totalNotActAmt+Double.valueOf(acct5WSummary.get(i)[4].toString());
											if(acct5WSummary.get(i)[6]!=null)
												totalNotActPer=totalNotActPer+Double.valueOf(acct5WSummary.get(i)[6].toString());

											if(acct5WSummary.get(i)[2]!=null)
												totalNotActCt=totalNotActCt+Double.valueOf(acct5WSummary.get(i)[2].toString());
											if(acct5WSummary.get(i)[5]!=null)
												totalNotActCtPer=totalNotActCtPer+Double.valueOf(acct5WSummary.get(i)[5].toString());
										}

									}

								}
							}
							else
							{
								accounted.add(l);
								accountingInProcess.add(l);
								finalAccounted.add(l);
								finalNotPosted.add(l);
								notAccounted.add(l);

								accountedAmtPer.add(l);
								accountingInProcessAmtPer.add(l);
								finalAccountedAmtPer.add(l);
								notAccountedAmtPer.add(l);


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





						log.info("labelValue :"+labelValue);
						log.info("accounted :"+accounted);
						log.info("accountingInProcess :"+accountingInProcess);
						log.info("finalAccounted :"+finalAccounted);
						log.info("notAccounted :"+notAccounted);


						List<Double> result = IntStream.range(0, accounted.size())
								.mapToObj(i -> Double.valueOf((dform.format(notAccounted.get(i) + accountingInProcess.get(i)).toString())))
								.collect(Collectors.toList());


						LinkedHashMap map=new LinkedHashMap();
						map.put("labelValue", labelValue);
						map.put("labelDatesValue", labelDatesValue);
						map.put("accounted", accounted);
						map.put("accountingInProcess", accountingInProcess);
						map.put("finalAccounted", finalAccounted);
						map.put("finalNotAccounted", finalNotPosted);
						map.put("notAccounted", notAccounted);
						map.put("notActAndAcctInProc", result);


						List<Double> resultAmtPer = IntStream.range(0, accountingInProcessAmtPer.size())
								.mapToObj(i -> Double.valueOf((dform.format(notAccountedAmtPer.get(i) + accountingInProcessAmtPer.get(i)).toString())))
								.collect(Collectors.toList());
						map.put("accountedAmtPer", accountedAmtPer);
						map.put("accountingInProcessAmtPer", accountingInProcessAmtPer);
						map.put("finalAccountedAmtPer", finalAccountedAmtPer);

						map.put("notAccountedAmtPer", notAccountedAmtPer);
						map.put("notActAndAcctInProcAmtPer", resultAmtPer);


						// OneMonthMap
						LinkedHashMap ommap=new LinkedHashMap();

						ommap.put("actamount", Double.valueOf(dform.format(totalActAmt)));
						ommap.put("actPercentage", Double.valueOf(dform.format(totalActAmtPer)));
						ommap.put("actInProcessamount",Double.valueOf(dform.format( totalActInProcAmt)));
						ommap.put("actInProcessPercentage", Double.valueOf(dform.format(totalActInProcPer)));
						ommap.put("finalActamount", Double.valueOf(dform.format(totalFinalActAmt)));
						ommap.put("finalActPercentage", Double.valueOf(dform.format(totalFinalActPer)));
						//to get final un posted amount and percentage 
						Double finalNotPostedAmt=totalActAmt-totalFinalActAmt;
						ommap.put("finalUnpostedAmt", Double.valueOf(dform.format(finalNotPostedAmt)));
						Double finalNotPostedPer=totalActAmtPer-totalFinalActPer;
						ommap.put("finalUnpostedPer", Double.valueOf(dform.format(finalNotPostedPer)));

						ommap.put("notActamount", Double.valueOf(totalNotActAmt));
						ommap.put("notActPercentage", Double.valueOf(dform.format(totalNotActPer)));


						ommap.put("actCt", Double.valueOf(dform.format(totalActCt)));
						ommap.put("actCtPer", Double.valueOf(dform.format(totalActCtPer)));
						ommap.put("actInProcessCt", Double.valueOf(dform.format(totalActInProcCt)));
						ommap.put("actInProcessCtPer", Double.valueOf(dform.format(totalActInProcCtPer)));
						ommap.put("finalActCt", Double.valueOf(dform.format(totalFinalActCt)));
						ommap.put("finalActCtPer", Double.valueOf(totalFinalActCtPer));
						ommap.put("notActCt", Double.valueOf(dform.format(totalNotActCt)));
						ommap.put("notActCtPer", Double.valueOf(dform.format(totalNotActCtPer)));



						ommap.put("notActAndAcctInProcCt", Double.valueOf(dform.format(Double.valueOf(totalNotActCt)+Double.valueOf(totalActInProcCt))));
						ommap.put("notActAndAcctInProcCtPer", Double.valueOf(dform.format(Double.valueOf(totalNotActCtPer)+Double.valueOf(totalActInProcCtPer))));

						ommap.put("notActAndAcctInProcAmt", Double.valueOf(dform.format(Double.valueOf(totalNotActAmt)+Double.valueOf(totalActInProcAmt))));
						ommap.put("notActAndAcctInProcAmtPer",Double.valueOf( dform.format(Double.valueOf(totalNotActPer)+Double.valueOf(totalActInProcPer))));


						ommap.put("detailedData", map);
						eachMap.put("1M", ommap);

						log.info("1MonthMap :"+eachMap);

					}



					if(days>=30)
					{
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


						List<String> labelValue=new ArrayList<String>();
						// List<String> labelDatesValue=new ArrayList<String>();
						List<Double> accounted=new ArrayList<Double>();
						List<Double> accountingInProcess=new ArrayList<Double>();
						List<Double> finalAccounted=new ArrayList<Double>();
						List<Double> finalNotPosted=new ArrayList<Double>();
						List<Double> notAccounted=new ArrayList<Double>();

						List<LinkedHashMap> labelDatesValue=new ArrayList<LinkedHashMap>();

						// log.info("datesList in Acounting For a week:"+datesList);

						List<Double> accountedAmtPer=new ArrayList<Double>();
						List<Double> accountingInProcessAmtPer=new ArrayList<Double>();
						List<Double> finalAccountedAmtPer=new ArrayList<Double>();
						List<Double> notAccountedAmtPer=new ArrayList<Double>();



						Double totalActAmt=0d;
						Double totalActInProcAmt=0d;
						Double totalFinalActAmt=0d;
						Double totalNotActAmt=0d;
						Double totalActAmtPer=0d;
						Double totalActInProcPer=0d;
						Double totalFinalActPer=0d;
						Double totalNotActPer=0d;
						Double l=0d;


						/**counts**/
						Double totalActCt=0d;
						Double totalActInProcCt=0d;
						Double totalFinalActCt=0d;
						Double totalNotActCt=0d;
						Double totalActCtPer=0d;
						Double totalActInProcCtPer=0d;
						Double totalFinalActCtPer=0d;
						Double totalNotActCtPer=0d;
						// log.info("datesList in Acounting For a week:"+datesList);


						List< Object[]> acct1WSummary=appModuleSummaryRepository.fetchAccountingAnalysisWithWeeks(procesRecDet.getTypeId(),from1wDate,till1wDate );

						log.info("acct1WSummary size :"+acct1WSummary.size());

						labelDatesValue.add(dashBoardV2Service.datesMap(from1wDate.toString(),till1wDate.toString()));
						// labelDatesValue.add(from1wDate+"-"+till1wDate);
						labelValue.add("Week-1");
						if(acct1WSummary.size()>0)
						{
							for(int i=0;i<acct1WSummary.size();i++)
							{
								if(acct1WSummary!=null)
								{

									// Double acted=0d;
									if(acct1WSummary.get(i)[0].toString().equalsIgnoreCase("Accounted"))
									{

										// acted=acted+Double.valueOf(acct1WSummary.get(i)[5].toString());
										accounted.add(Double.valueOf(acct1WSummary.get(i)[5].toString()));
										accountedAmtPer.add(Double.valueOf(acct1WSummary.get(i)[6].toString()));

										if(acct1WSummary.get(i)[4]!=null)
											totalActAmt=totalActAmt+Double.valueOf(acct1WSummary.get(i)[4].toString());
										if(acct1WSummary.get(i)[6]!=null)
											totalActAmtPer=totalActAmtPer+Double.valueOf(acct1WSummary.get(i)[6].toString());
										if(acct1WSummary.get(i)[2]!=null)
											totalActCt=totalActCt+Double.valueOf(acct1WSummary.get(i)[2].toString());
										if(acct1WSummary.get(i)[5]!=null)
											totalActCtPer=totalActCtPer+Double.valueOf(acct1WSummary.get(i)[5].toString());

									}
									if(acct1WSummary.get(i)[0].toString().equalsIgnoreCase("Accounting inprocess") || acct1WSummary.get(i)[0].toString().equalsIgnoreCase("inprocess"))
									{

										accountingInProcess.add(Double.valueOf(acct1WSummary.get(i)[5].toString()));
										accountingInProcessAmtPer.add(Double.valueOf(acct1WSummary.get(i)[6].toString()));

										if(acct1WSummary.get(i)[4]!=null)
											totalActInProcAmt=totalActInProcAmt+Double.valueOf(acct1WSummary.get(i)[4].toString());
										if(acct1WSummary.get(i)[6]!=null)
											totalActInProcPer=totalActInProcPer+Double.valueOf(acct1WSummary.get(i)[6].toString());

										if(acct1WSummary.get(i)[2]!=null)
											totalActInProcCt=totalActInProcCt+Double.valueOf(acct1WSummary.get(i)[2].toString());
										if(acct1WSummary.get(i)[5]!=null)
											totalActInProcCtPer=totalActInProcCtPer+Double.valueOf(acct1WSummary.get(i)[5].toString());
									}
									if(acct1WSummary.get(i)[0].toString().equalsIgnoreCase("Final accounted"))
									{

										finalAccounted.add(Double.valueOf(acct1WSummary.get(i)[5].toString()));
										//	 Double finalJournalsNotPosted=acted-Double.valueOf(acct1WSummary.get(i)[4].toString());
										//	 finalNotPosted.add(Double.valueOf(finalJournalsNotPosted));
										finalAccountedAmtPer.add(Double.valueOf(acct1WSummary.get(i)[6].toString()));

										if(acct1WSummary.get(i)[4]!=null)
											totalFinalActAmt=totalFinalActAmt+Double.valueOf(acct1WSummary.get(i)[4].toString());
										if(acct1WSummary.get(i)[6]!=null)
											totalFinalActPer=totalFinalActPer+Double.valueOf(acct1WSummary.get(i)[6].toString());

										if(acct1WSummary.get(i)[2]!=null)
											totalFinalActCt=totalFinalActCt+Double.valueOf(acct1WSummary.get(i)[2].toString());
										if(acct1WSummary.get(i)[5]!=null)
											totalFinalActCtPer=totalFinalActCtPer+Double.valueOf(acct1WSummary.get(i)[5].toString());
									}
									if(acct1WSummary.get(i)[0].toString().equalsIgnoreCase("Not accounted"))
									{

										notAccounted.add(Double.valueOf(acct1WSummary.get(i)[5].toString()));
										notAccountedAmtPer.add(Double.valueOf(acct1WSummary.get(i)[6].toString()));

										if(acct1WSummary.get(i)[4]!=null)
											totalNotActAmt=totalNotActAmt+Double.valueOf(acct1WSummary.get(i)[4].toString());
										if(acct1WSummary.get(i)[6]!=null)
											totalNotActPer=totalNotActPer+Double.valueOf(acct1WSummary.get(i)[6].toString());

										if(acct1WSummary.get(i)[2]!=null)
											totalNotActCt=totalNotActCt+Double.valueOf(acct1WSummary.get(i)[2].toString());
										if(acct1WSummary.get(i)[5]!=null)
											totalNotActCtPer=totalNotActCtPer+Double.valueOf(acct1WSummary.get(i)[5].toString());
									}

								}

							}
						}
						else
						{
							accounted.add(l);
							accountingInProcess.add(l);
							finalAccounted.add(l);
							finalNotPosted.add(l);
							notAccounted.add(l);

							accountedAmtPer.add(l);
							accountingInProcessAmtPer.add(l);
							finalAccountedAmtPer.add(l);
							notAccountedAmtPer.add(l);

						}


						List< Object[]> acct2WSummary=appModuleSummaryRepository.fetchAccountingAnalysisWithWeeks(procesRecDet.getTypeId(),from2wDate,till2wDate);
						log.info("acct1WSummary size :"+acct2WSummary.size());
						labelDatesValue.add(dashBoardV2Service.datesMap(from2wDate.toString(),till2wDate.toString()));

						labelValue.add("Week-2");
						if(acct2WSummary.size()>0)
						{
							for(int i=0;i<acct2WSummary.size();i++)
							{
								if(acct2WSummary!=null)
								{

									// Double acted=0d;
									if(acct2WSummary.get(i)[0].toString().equalsIgnoreCase("Accounted"))
									{

										accounted.add(Double.valueOf(acct2WSummary.get(i)[5].toString()));
										accountedAmtPer.add(Double.valueOf(acct2WSummary.get(i)[6].toString()));

										if(acct2WSummary.get(i)[4]!=null)
											totalActAmt=totalActAmt+Double.valueOf(acct2WSummary.get(i)[4].toString());
										if(acct2WSummary.get(i)[6]!=null)
											totalActAmtPer=totalActAmtPer+Double.valueOf(acct2WSummary.get(i)[6].toString());
										if(acct2WSummary.get(i)[2]!=null)
											totalActCt=totalActCt+Double.valueOf(acct2WSummary.get(i)[2].toString());
										if(acct2WSummary.get(i)[5]!=null)
											totalActCtPer=totalActCtPer+Double.valueOf(acct2WSummary.get(i)[5].toString());

									}
									if(acct2WSummary.get(i)[0].toString().equalsIgnoreCase("Accounting inprocess") || acct2WSummary.get(i)[0].toString().equalsIgnoreCase("inprocess"))
									{

										accountingInProcess.add(Double.valueOf(acct2WSummary.get(i)[5].toString()));
										accountingInProcessAmtPer.add(Double.valueOf(acct2WSummary.get(i)[6].toString()));

										if(acct2WSummary.get(i)[4]!=null)
											totalActInProcAmt=totalActInProcAmt+Double.valueOf(acct2WSummary.get(i)[4].toString());
										if(acct2WSummary.get(i)[6]!=null)
											totalActInProcPer=totalActInProcPer+Double.valueOf(acct2WSummary.get(i)[6].toString());

										if(acct2WSummary.get(i)[2]!=null)
											totalActInProcCt=totalActInProcCt+Double.valueOf(acct2WSummary.get(i)[2].toString());
										if(acct2WSummary.get(i)[5]!=null)
											totalActInProcCtPer=totalActInProcCtPer+Double.valueOf(acct2WSummary.get(i)[5].toString());
									}
									if(acct2WSummary.get(i)[0].toString().equalsIgnoreCase("Final accounted"))
									{

										finalAccounted.add(Double.valueOf(acct2WSummary.get(i)[5].toString()));
										finalAccountedAmtPer.add(Double.valueOf(acct2WSummary.get(i)[6].toString()));
										if(acct2WSummary.get(i)[4]!=null)
											totalFinalActAmt=totalFinalActAmt+Double.valueOf(acct2WSummary.get(i)[4].toString());
										if(acct2WSummary.get(i)[6]!=null)
											totalFinalActPer=totalFinalActPer+Double.valueOf(acct2WSummary.get(i)[6].toString());

										if(acct2WSummary.get(i)[2]!=null)
											totalFinalActCt=totalFinalActCt+Double.valueOf(acct2WSummary.get(i)[2].toString());
										if(acct2WSummary.get(i)[5]!=null)
											totalFinalActCtPer=totalFinalActCtPer+Double.valueOf(acct2WSummary.get(i)[5].toString());
									}
									if(acct2WSummary.get(i)[0].toString().equalsIgnoreCase("Not accounted"))
									{

										notAccounted.add(Double.valueOf(acct2WSummary.get(i)[5].toString()));
										notAccountedAmtPer.add(Double.valueOf(acct2WSummary.get(i)[6].toString()));

										if(acct2WSummary.get(i)[4]!=null)
											totalNotActAmt=totalNotActAmt+Double.valueOf(acct2WSummary.get(i)[4].toString());
										if(acct2WSummary.get(i)[6]!=null)
											totalNotActPer=totalNotActPer+Double.valueOf(acct2WSummary.get(i)[6].toString());

										if(acct2WSummary.get(i)[2]!=null)
											totalNotActCt=totalNotActCt+Double.valueOf(acct2WSummary.get(i)[2].toString());
										if(acct2WSummary.get(i)[5]!=null)
											totalNotActCtPer=totalNotActCtPer+Double.valueOf(acct2WSummary.get(i)[5].toString());
									}

								}

							}
						}
						else
						{
							accounted.add(l);
							accountingInProcess.add(l);
							finalAccounted.add(l);
							finalNotPosted.add(l);
							notAccounted.add(l);

							accountedAmtPer.add(l);
							accountingInProcessAmtPer.add(l);
							finalAccountedAmtPer.add(l);
							notAccountedAmtPer.add(l);

						}




						List< Object[]> acct3WSummary=appModuleSummaryRepository.fetchAccountingAnalysisWithWeeks(procesRecDet.getTypeId(),from3wDate,till3wDate);
						log.info("acct3WSummary size :"+acct3WSummary.size());
						labelDatesValue.add(dashBoardV2Service.datesMap(from3wDate.toString(),till3wDate.toString()));

						labelValue.add("Week-3");
						if(acct3WSummary.size()>0)
						{
							for(int i=0;i<acct3WSummary.size();i++)
							{
								if(acct3WSummary!=null)
								{

									// Double acted=0d;
									if(acct3WSummary.get(i)[0].toString().equalsIgnoreCase("Accounted"))
									{

										accounted.add(Double.valueOf(acct3WSummary.get(i)[5].toString()));
										accountedAmtPer.add(Double.valueOf(acct3WSummary.get(i)[6].toString()));

										if(acct3WSummary.get(i)[4]!=null)
											totalActAmt=totalActAmt+Double.valueOf(acct3WSummary.get(i)[4].toString());
										if(acct3WSummary.get(i)[6]!=null)
											totalActAmtPer=totalActAmtPer+Double.valueOf(acct3WSummary.get(i)[6].toString());
										if(acct3WSummary.get(i)[2]!=null)
											totalActCt=totalActCt+Double.valueOf(acct3WSummary.get(i)[2].toString());
										if(acct3WSummary.get(i)[5]!=null)
											totalActCtPer=totalActCtPer+Double.valueOf(acct3WSummary.get(i)[5].toString());

									}
									if(acct3WSummary.get(i)[0].toString().equalsIgnoreCase("Accounting inprocess") || acct3WSummary.get(i)[0].toString().equalsIgnoreCase("inprocess"))
									{

										accountingInProcess.add(Double.valueOf(acct3WSummary.get(i)[5].toString()));
										accountingInProcessAmtPer.add(Double.valueOf(acct3WSummary.get(i)[6].toString()));

										if(acct3WSummary.get(i)[4]!=null)
											totalActInProcAmt=totalActInProcAmt+Double.valueOf(acct3WSummary.get(i)[4].toString());
										if(acct3WSummary.get(i)[6]!=null)
											totalActInProcPer=totalActInProcPer+Double.valueOf(acct3WSummary.get(i)[6].toString());

										if(acct3WSummary.get(i)[2]!=null)
											totalActInProcCt=totalActInProcCt+Double.valueOf(acct3WSummary.get(i)[2].toString());
										if(acct3WSummary.get(i)[5]!=null)
											totalActInProcCtPer=totalActInProcCtPer+Double.valueOf(acct3WSummary.get(i)[5].toString());
									}
									if(acct3WSummary.get(i)[0].toString().equalsIgnoreCase("Final accounted"))
									{
										finalAccounted.add(Double.valueOf(acct3WSummary.get(i)[5].toString()));
										finalAccountedAmtPer.add(Double.valueOf(acct3WSummary.get(i)[6].toString()));

										if(acct3WSummary.get(i)[4]!=null)
											totalFinalActAmt=totalFinalActAmt+Double.valueOf(acct3WSummary.get(i)[4].toString());
										if(acct3WSummary.get(i)[6]!=null)
											totalFinalActPer=totalFinalActPer+Double.valueOf(acct3WSummary.get(i)[6].toString());

										if(acct3WSummary.get(i)[2]!=null)
											totalFinalActCt=totalFinalActCt+Double.valueOf(acct3WSummary.get(i)[2].toString());
										if(acct3WSummary.get(i)[5]!=null)
											totalFinalActCtPer=totalFinalActCtPer+Double.valueOf(acct3WSummary.get(i)[5].toString());
									}
									if(acct3WSummary.get(i)[0].toString().equalsIgnoreCase("Not accounted"))
									{

										notAccounted.add(Double.valueOf(acct3WSummary.get(i)[5].toString()));
										notAccountedAmtPer.add(Double.valueOf(acct3WSummary.get(i)[6].toString()));

										if(acct3WSummary.get(i)[4]!=null)
											totalNotActAmt=totalNotActAmt+Double.valueOf(acct3WSummary.get(i)[4].toString());
										if(acct3WSummary.get(i)[6]!=null)
											totalNotActPer=totalNotActPer+Double.valueOf(acct3WSummary.get(i)[6].toString());

										if(acct3WSummary.get(i)[2]!=null)
											totalNotActCt=totalNotActCt+Double.valueOf(acct3WSummary.get(i)[2].toString());
										if(acct3WSummary.get(i)[5]!=null)
											totalNotActCtPer=totalNotActCtPer+Double.valueOf(acct3WSummary.get(i)[5].toString());
									}

								}

							}
						}
						else
						{
							accounted.add(l);
							accountingInProcess.add(l);
							finalAccounted.add(l);
							finalNotPosted.add(l);
							notAccounted.add(l);


							accountedAmtPer.add(l);
							accountingInProcessAmtPer.add(l);
							finalAccountedAmtPer.add(l);
							notAccountedAmtPer.add(l);

						}


						List< Object[]> acct4WSummary=appModuleSummaryRepository.fetchAccountingAnalysisWithWeeks(procesRecDet.getTypeId(),from4wDate,till4wDate);
						log.info("acct4WSummary size :"+acct4WSummary.size());
						labelDatesValue.add(dashBoardV2Service.datesMap(from4wDate.toString(),till4wDate.toString()));

						labelValue.add("Week-4");
						if(acct4WSummary.size()>0)
						{
							for(int i=0;i<acct4WSummary.size();i++)
							{
								if(acct4WSummary!=null)
								{


									if(acct4WSummary.get(i)[0].toString().equalsIgnoreCase("Accounted"))
									{

										accounted.add(Double.valueOf(acct4WSummary.get(i)[5].toString()));
										accountedAmtPer.add(Double.valueOf(acct4WSummary.get(i)[6].toString()));

										if(acct4WSummary.get(i)[4]!=null)
											totalActAmt=totalActAmt+Double.valueOf(acct4WSummary.get(i)[4].toString());
										if(acct4WSummary.get(i)[6]!=null)
											totalActAmtPer=totalActAmtPer+Double.valueOf(acct4WSummary.get(i)[6].toString());
										if(acct4WSummary.get(i)[2]!=null)
											totalActCt=totalActCt+Double.valueOf(acct4WSummary.get(i)[2].toString());
										if(acct4WSummary.get(i)[5]!=null)
											totalActCtPer=totalActCtPer+Double.valueOf(acct4WSummary.get(i)[5].toString());

									}
									if(acct4WSummary.get(i)[0].toString().equalsIgnoreCase("Accounting inprocess") || acct4WSummary.get(i)[0].toString().equalsIgnoreCase("inprocess"))
									{

										accountingInProcess.add(Double.valueOf(acct4WSummary.get(i)[5].toString()));
										accountingInProcessAmtPer.add(Double.valueOf(acct4WSummary.get(i)[6].toString()));

										if(acct4WSummary.get(i)[4]!=null)
											totalActInProcAmt=totalActInProcAmt+Double.valueOf(acct4WSummary.get(i)[4].toString());
										if(acct4WSummary.get(i)[6]!=null)
											totalActInProcPer=totalActInProcPer+Double.valueOf(acct4WSummary.get(i)[6].toString());

										if(acct4WSummary.get(i)[2]!=null)
											totalActInProcCt=totalActInProcCt+Double.valueOf(acct4WSummary.get(i)[2].toString());
										if(acct4WSummary.get(i)[5]!=null)
											totalActInProcCtPer=totalActInProcCtPer+Double.valueOf(acct4WSummary.get(i)[5].toString());
									}
									if(acct4WSummary.get(i)[0].toString().equalsIgnoreCase("Final accounted"))
									{

										finalAccounted.add(Double.valueOf(acct4WSummary.get(i)[5].toString()));
										finalAccountedAmtPer.add(Double.valueOf(acct4WSummary.get(i)[6].toString()));

										if(acct4WSummary.get(i)[4]!=null)
											totalFinalActAmt=totalFinalActAmt+Double.valueOf(acct4WSummary.get(i)[4].toString());
										if(acct4WSummary.get(i)[6]!=null)
											totalFinalActPer=totalFinalActPer+Double.valueOf(acct4WSummary.get(i)[6].toString());

										if(acct4WSummary.get(i)[2]!=null)
											totalFinalActCt=totalFinalActCt+Double.valueOf(acct4WSummary.get(i)[2].toString());
										if(acct4WSummary.get(i)[5]!=null)
											totalFinalActCtPer=totalFinalActCtPer+Double.valueOf(acct4WSummary.get(i)[5].toString());
									}
									if(acct4WSummary.get(i)[0].toString().equalsIgnoreCase("Not accounted"))
									{

										notAccounted.add(Double.valueOf(acct4WSummary.get(i)[5].toString()));
										notAccountedAmtPer.add(Double.valueOf(acct4WSummary.get(i)[6].toString()));

										if(acct4WSummary.get(i)[4]!=null)
											totalNotActAmt=totalNotActAmt+Double.valueOf(acct4WSummary.get(i)[4].toString());
										if(acct4WSummary.get(i)[6]!=null)
											totalNotActPer=totalNotActPer+Double.valueOf(acct4WSummary.get(i)[6].toString());

										if(acct4WSummary.get(i)[2]!=null)
											totalNotActCt=totalNotActCt+Double.valueOf(acct4WSummary.get(i)[2].toString());
										if(acct4WSummary.get(i)[5]!=null)
											totalNotActCtPer=totalNotActCtPer+Double.valueOf(acct4WSummary.get(i)[5].toString());
									}

								}

							}
						}
						else
						{
							accounted.add(l);
							accountingInProcess.add(l);
							finalAccounted.add(l);
							finalNotPosted.add(l);
							notAccounted.add(l);

							accountedAmtPer.add(l);
							accountingInProcessAmtPer.add(l);
							finalAccountedAmtPer.add(l);
							notAccountedAmtPer.add(l);

						}



						List< Object[]> acct5WSummary=appModuleSummaryRepository.fetchAccountingAnalysisWithWeeks(procesRecDet.getTypeId(),from5wDate,till5wDate);
						log.info("acct5WSummary size :"+acct5WSummary.size());
						labelDatesValue.add(dashBoardV2Service.datesMap(from5wDate.toString(),till5wDate.toString()));

						labelValue.add("Week-5");
						if(acct5WSummary.size()>0)
						{
							for(int i=0;i<acct5WSummary.size();i++)
							{
								if(acct5WSummary!=null)
								{


									if(acct5WSummary.get(i)[0].toString().equalsIgnoreCase("Accounted"))
									{

										accounted.add(Double.valueOf(acct5WSummary.get(i)[5].toString()));
										accountedAmtPer.add(Double.valueOf(acct5WSummary.get(i)[6].toString()));

										if(acct5WSummary.get(i)[4]!=null)
											totalActAmt=totalActAmt+Double.valueOf(acct5WSummary.get(i)[4].toString());
										if(acct5WSummary.get(i)[6]!=null)
											totalActAmtPer=totalActAmtPer+Double.valueOf(acct5WSummary.get(i)[6].toString());
										if(acct5WSummary.get(i)[2]!=null)
											totalActCt=totalActCt+Double.valueOf(acct5WSummary.get(i)[2].toString());
										if(acct5WSummary.get(i)[5]!=null)
											totalActCtPer=totalActCtPer+Double.valueOf(acct5WSummary.get(i)[5].toString());

									}
									if(acct5WSummary.get(i)[0].toString().equalsIgnoreCase("Accounting inprocess") || acct5WSummary.get(i)[0].toString().equalsIgnoreCase("inprocess"))
									{

										accountingInProcess.add(Double.valueOf(acct5WSummary.get(i)[5].toString()));
										accountingInProcessAmtPer.add(Double.valueOf(acct5WSummary.get(i)[6].toString()));

										if(acct5WSummary.get(i)[4]!=null)
											totalActInProcAmt=totalActInProcAmt+Double.valueOf(acct5WSummary.get(i)[4].toString());
										if(acct5WSummary.get(i)[6]!=null)
											totalActInProcPer=totalActInProcPer+Double.valueOf(acct5WSummary.get(i)[6].toString());

										if(acct5WSummary.get(i)[2]!=null)
											totalActInProcCt=totalActInProcCt+Double.valueOf(acct5WSummary.get(i)[2].toString());
										if(acct5WSummary.get(i)[5]!=null)
											totalActInProcCtPer=totalActInProcCtPer+Double.valueOf(acct5WSummary.get(i)[5].toString());
									}
									if(acct5WSummary.get(i)[0].toString().equalsIgnoreCase("Final accounted"))
									{
										finalAccounted.add(Double.valueOf(acct5WSummary.get(i)[5].toString()));
										finalAccountedAmtPer.add(Double.valueOf(acct5WSummary.get(i)[6].toString()));

										if(acct5WSummary.get(i)[4]!=null)
											totalFinalActAmt=totalFinalActAmt+Double.valueOf(acct5WSummary.get(i)[4].toString());
										if(acct5WSummary.get(i)[6]!=null)
											totalFinalActPer=totalFinalActPer+Double.valueOf(acct5WSummary.get(i)[6].toString());

										if(acct5WSummary.get(i)[2]!=null)
											totalFinalActCt=totalFinalActCt+Double.valueOf(acct5WSummary.get(i)[2].toString());
										if(acct5WSummary.get(i)[5]!=null)
											totalFinalActCtPer=totalFinalActCtPer+Double.valueOf(acct5WSummary.get(i)[5].toString());
									}
									if(acct5WSummary.get(i)[0].toString().equalsIgnoreCase("Not accounted"))
									{

										notAccounted.add(Double.valueOf(acct5WSummary.get(i)[5].toString()));
										notAccountedAmtPer.add(Double.valueOf(acct5WSummary.get(i)[6].toString()));
										if(acct5WSummary.get(i)[4]!=null)
											totalNotActAmt=totalNotActAmt+Double.valueOf(acct5WSummary.get(i)[4].toString());
										if(acct5WSummary.get(i)[6]!=null)
											totalNotActPer=totalNotActPer+Double.valueOf(acct5WSummary.get(i)[6].toString());

										if(acct5WSummary.get(i)[2]!=null)
											totalNotActCt=totalNotActCt+Double.valueOf(acct5WSummary.get(i)[2].toString());
										if(acct5WSummary.get(i)[5]!=null)
											totalNotActCtPer=totalNotActCtPer+Double.valueOf(acct5WSummary.get(i)[5].toString());
									}

								}

							}
						}
						else
						{
							accounted.add(l);
							accountingInProcess.add(l);
							finalAccounted.add(l);
							finalNotPosted.add(l);
							notAccounted.add(l);

							accountedAmtPer.add(l);
							accountingInProcessAmtPer.add(l);
							finalAccountedAmtPer.add(l);
							notAccountedAmtPer.add(l);


						}


						List< Object[]> acct6WSummary=appModuleSummaryRepository.fetchAccountingAnalysisWithWeeks(procesRecDet.getTypeId(),from6Date,till6Date);
						log.info("acct5WSummary size :"+acct6WSummary.size());
						labelDatesValue.add(dashBoardV2Service.datesMap(from6Date.toString(),till6Date.toString()));

						labelValue.add("Week-6");
						if(acct6WSummary.size()>0)
						{
							for(int i=0;i<acct6WSummary.size();i++)
							{
								if(acct6WSummary!=null)
								{


									if(acct6WSummary.get(i)[0].toString().equalsIgnoreCase("Accounted"))
									{

										accounted.add(Double.valueOf(acct6WSummary.get(i)[5].toString()));
										accountedAmtPer.add(Double.valueOf(acct6WSummary.get(i)[6].toString()));

										if(acct6WSummary.get(i)[4]!=null)
											totalActAmt=totalActAmt+Double.valueOf(acct6WSummary.get(i)[4].toString());
										if(acct6WSummary.get(i)[6]!=null)
											totalActAmtPer=totalActAmtPer+Double.valueOf(acct6WSummary.get(i)[6].toString());
										if(acct6WSummary.get(i)[2]!=null)
											totalActCt=totalActCt+Double.valueOf(acct6WSummary.get(i)[2].toString());
										if(acct6WSummary.get(i)[5]!=null)
											totalActCtPer=totalActCtPer+Double.valueOf(acct6WSummary.get(i)[5].toString());

									}
									if(acct6WSummary.get(i)[0].toString().equalsIgnoreCase("Accounting inprocess") || acct6WSummary.get(i)[0].toString().equalsIgnoreCase("inprocess"))
									{

										accountingInProcess.add(Double.valueOf(acct6WSummary.get(i)[5].toString()));
										accountingInProcessAmtPer.add(Double.valueOf(acct6WSummary.get(i)[6].toString()));

										if(acct6WSummary.get(i)[4]!=null)
											totalActInProcAmt=totalActInProcAmt+Double.valueOf(acct6WSummary.get(i)[4].toString());
										if(acct6WSummary.get(i)[6]!=null)
											totalActInProcPer=totalActInProcPer+Double.valueOf(acct6WSummary.get(i)[6].toString());

										if(acct6WSummary.get(i)[2]!=null)
											totalActInProcCt=totalActInProcCt+Double.valueOf(acct6WSummary.get(i)[2].toString());
										if(acct6WSummary.get(i)[5]!=null)
											totalActInProcCtPer=totalActInProcCtPer+Double.valueOf(acct6WSummary.get(i)[5].toString());
									}
									if(acct6WSummary.get(i)[0].toString().equalsIgnoreCase("Final accounted"))
									{
										finalAccounted.add(Double.valueOf(acct6WSummary.get(i)[5].toString()));
										finalAccountedAmtPer.add(Double.valueOf(acct6WSummary.get(i)[6].toString()));

										if(acct6WSummary.get(i)[4]!=null)
											totalFinalActAmt=totalFinalActAmt+Double.valueOf(acct6WSummary.get(i)[4].toString());
										if(acct6WSummary.get(i)[6]!=null)
											totalFinalActPer=totalFinalActPer+Double.valueOf(acct6WSummary.get(i)[6].toString());

										if(acct6WSummary.get(i)[2]!=null)
											totalFinalActCt=totalFinalActCt+Double.valueOf(acct6WSummary.get(i)[2].toString());
										if(acct6WSummary.get(i)[5]!=null)
											totalFinalActCtPer=totalFinalActCtPer+Double.valueOf(acct6WSummary.get(i)[5].toString());
									}
									if(acct6WSummary.get(i)[0].toString().equalsIgnoreCase("Not accounted"))
									{

										notAccounted.add(Double.valueOf(acct6WSummary.get(i)[5].toString()));
										finalAccountedAmtPer.add(Double.valueOf(acct6WSummary.get(i)[6].toString()));

										if(acct6WSummary.get(i)[4]!=null)
											totalNotActAmt=totalNotActAmt+Double.valueOf(acct6WSummary.get(i)[4].toString());
										if(acct6WSummary.get(i)[6]!=null)
											totalNotActPer=totalNotActPer+Double.valueOf(acct6WSummary.get(i)[6].toString());

										if(acct6WSummary.get(i)[2]!=null)
											totalNotActCt=totalNotActCt+Double.valueOf(acct6WSummary.get(i)[2].toString());
										if(acct6WSummary.get(i)[5]!=null)
											totalNotActCtPer=totalNotActCtPer+Double.valueOf(acct6WSummary.get(i)[5].toString());
									}

								}

							}
						}
						else
						{
							accounted.add(l);
							accountingInProcess.add(l);
							finalAccounted.add(l);
							finalNotPosted.add(l);
							notAccounted.add(l);

							accountedAmtPer.add(l);
							accountingInProcessAmtPer.add(l);
							finalAccountedAmtPer.add(l);
							notAccountedAmtPer.add(l);


						}
						if(from7Date.isAfter(fDate) || from7Date.equals(fDate))
						{
							List< Object[]> acct7WSummary=appModuleSummaryRepository.fetchAccountingAnalysisWithWeeks(procesRecDet.getTypeId(),from7Date,till7Date);
							log.info("acct5WSummary size :"+acct7WSummary.size());
							labelDatesValue.add(dashBoardV2Service.datesMap(from7Date.toString(),till7Date.toString()));

							labelValue.add("Week-7");
							if(acct7WSummary.size()>0)
							{
								for(int i=0;i<acct7WSummary.size();i++)
								{
									if(acct7WSummary!=null)
									{


										if(acct7WSummary.get(i)[0].toString().equalsIgnoreCase("Accounted"))
										{

											accounted.add(Double.valueOf(acct7WSummary.get(i)[5].toString()));
											accountedAmtPer.add(Double.valueOf(acct7WSummary.get(i)[6].toString()));

											if(acct7WSummary.get(i)[4]!=null)
												totalActAmt=totalActAmt+Double.valueOf(acct7WSummary.get(i)[4].toString());
											if(acct7WSummary.get(i)[6]!=null)
												totalActAmtPer=totalActAmtPer+Double.valueOf(acct7WSummary.get(i)[6].toString());
											if(acct7WSummary.get(i)[2]!=null)
												totalActCt=totalActCt+Double.valueOf(acct7WSummary.get(i)[2].toString());
											if(acct7WSummary.get(i)[5]!=null)
												totalActCtPer=totalActCtPer+Double.valueOf(acct7WSummary.get(i)[5].toString());

										}
										if(acct7WSummary.get(i)[0].toString().equalsIgnoreCase("Accounting inprocess") || acct7WSummary.get(i)[0].toString().equalsIgnoreCase("inprocess"))
										{

											accountingInProcess.add(Double.valueOf(acct7WSummary.get(i)[5].toString()));
											accountingInProcessAmtPer.add(Double.valueOf(acct7WSummary.get(i)[6].toString()));

											if(acct7WSummary.get(i)[4]!=null)
												totalActInProcAmt=totalActInProcAmt+Double.valueOf(acct7WSummary.get(i)[4].toString());
											if(acct7WSummary.get(i)[6]!=null)
												totalActInProcPer=totalActInProcPer+Double.valueOf(acct7WSummary.get(i)[6].toString());

											if(acct7WSummary.get(i)[2]!=null)
												totalActInProcCt=totalActInProcCt+Double.valueOf(acct7WSummary.get(i)[2].toString());
											if(acct7WSummary.get(i)[5]!=null)
												totalActInProcCtPer=totalActInProcCtPer+Double.valueOf(acct7WSummary.get(i)[5].toString());
										}
										if(acct7WSummary.get(i)[0].toString().equalsIgnoreCase("Final accounted"))
										{
											finalAccounted.add(Double.valueOf(acct7WSummary.get(i)[5].toString()));
											finalAccountedAmtPer.add(Double.valueOf(acct7WSummary.get(i)[6].toString()));

											if(acct7WSummary.get(i)[4]!=null)
												totalFinalActAmt=totalFinalActAmt+Double.valueOf(acct7WSummary.get(i)[4].toString());
											if(acct7WSummary.get(i)[6]!=null)
												totalFinalActPer=totalFinalActPer+Double.valueOf(acct7WSummary.get(i)[6].toString());

											if(acct7WSummary.get(i)[2]!=null)
												totalFinalActCt=totalFinalActCt+Double.valueOf(acct7WSummary.get(i)[2].toString());
											if(acct7WSummary.get(i)[5]!=null)
												totalFinalActCtPer=totalFinalActCtPer+Double.valueOf(acct7WSummary.get(i)[5].toString());
										}
										if(acct7WSummary.get(i)[0].toString().equalsIgnoreCase("Not accounted"))
										{

											notAccounted.add(Double.valueOf(acct7WSummary.get(i)[5].toString()));
											notAccountedAmtPer.add(Double.valueOf(acct7WSummary.get(i)[6].toString()));

											if(acct7WSummary.get(i)[4]!=null)
												totalNotActAmt=totalNotActAmt+Double.valueOf(acct7WSummary.get(i)[4].toString());
											if(acct7WSummary.get(i)[6]!=null)
												totalNotActPer=totalNotActPer+Double.valueOf(acct7WSummary.get(i)[6].toString());

											if(acct7WSummary.get(i)[2]!=null)
												totalNotActCt=totalNotActCt+Double.valueOf(acct7WSummary.get(i)[2].toString());
											if(acct7WSummary.get(i)[5]!=null)
												totalNotActCtPer=totalNotActCtPer+Double.valueOf(acct7WSummary.get(i)[5].toString());
										}

									}

								}
							}
							else
							{
								accounted.add(l);
								accountingInProcess.add(l);
								finalAccounted.add(l);
								finalNotPosted.add(l);
								notAccounted.add(l);

								accountedAmtPer.add(l);
								accountingInProcessAmtPer.add(l);
								finalAccountedAmtPer.add(l);
								notAccountedAmtPer.add(l);


							}
						}

						if(from8Date.isAfter(fDate) || from8Date.equals(fDate))
						{

							List< Object[]> acct8WSummary=appModuleSummaryRepository.fetchAccountingAnalysisWithWeeks(procesRecDet.getTypeId(),from8Date,till8Date);
							log.info("acct5WSummary size :"+acct8WSummary.size());
							labelDatesValue.add(dashBoardV2Service.datesMap(from8Date.toString(),till8Date.toString()));

							labelValue.add("Week-8");
							if(acct8WSummary.size()>0)
							{
								for(int i=0;i<acct8WSummary.size();i++)
								{
									if(acct8WSummary!=null)
									{


										if(acct8WSummary.get(i)[0].toString().equalsIgnoreCase("Accounted"))
										{

											accounted.add(Double.valueOf(acct8WSummary.get(i)[5].toString()));
											accountedAmtPer.add(Double.valueOf(acct8WSummary.get(i)[6].toString()));

											if(acct8WSummary.get(i)[4]!=null)
												totalActAmt=totalActAmt+Double.valueOf(acct8WSummary.get(i)[4].toString());
											if(acct8WSummary.get(i)[6]!=null)
												totalActAmtPer=totalActAmtPer+Double.valueOf(acct8WSummary.get(i)[6].toString());
											if(acct8WSummary.get(i)[2]!=null)
												totalActCt=totalActCt+Double.valueOf(acct8WSummary.get(i)[2].toString());
											if(acct8WSummary.get(i)[5]!=null)
												totalActCtPer=totalActCtPer+Double.valueOf(acct8WSummary.get(i)[5].toString());

										}
										if(acct8WSummary.get(i)[0].toString().equalsIgnoreCase("Accounting inprocess") || acct8WSummary.get(i)[0].toString().equalsIgnoreCase("inprocess"))
										{

											accountingInProcess.add(Double.valueOf(acct8WSummary.get(i)[5].toString()));
											accountingInProcessAmtPer.add(Double.valueOf(acct8WSummary.get(i)[6].toString()));

											if(acct8WSummary.get(i)[4]!=null)
												totalActInProcAmt=totalActInProcAmt+Double.valueOf(acct8WSummary.get(i)[4].toString());
											if(acct8WSummary.get(i)[6]!=null)
												totalActInProcPer=totalActInProcPer+Double.valueOf(acct8WSummary.get(i)[6].toString());

											if(acct8WSummary.get(i)[2]!=null)
												totalActInProcCt=totalActInProcCt+Double.valueOf(acct8WSummary.get(i)[2].toString());
											if(acct8WSummary.get(i)[5]!=null)
												totalActInProcCtPer=totalActInProcCtPer+Double.valueOf(acct8WSummary.get(i)[5].toString());
										}
										if(acct8WSummary.get(i)[0].toString().equalsIgnoreCase("Final accounted"))
										{

											finalAccounted.add(Double.valueOf(acct8WSummary.get(i)[5].toString()));
											finalAccountedAmtPer.add(Double.valueOf(acct8WSummary.get(i)[6].toString()));

											if(acct8WSummary.get(i)[4]!=null)
												totalFinalActAmt=totalFinalActAmt+Double.valueOf(acct8WSummary.get(i)[4].toString());
											if(acct8WSummary.get(i)[6]!=null)
												totalFinalActPer=totalFinalActPer+Double.valueOf(acct8WSummary.get(i)[6].toString());

											if(acct8WSummary.get(i)[2]!=null)
												totalFinalActCt=totalFinalActCt+Double.valueOf(acct8WSummary.get(i)[2].toString());
											if(acct8WSummary.get(i)[5]!=null)
												totalFinalActCtPer=totalFinalActCtPer+Double.valueOf(acct8WSummary.get(i)[5].toString());
										}
										if(acct8WSummary.get(i)[0].toString().equalsIgnoreCase("Not accounted"))
										{

											notAccounted.add(Double.valueOf(acct8WSummary.get(i)[5].toString()));
											notAccountedAmtPer.add(Double.valueOf(acct8WSummary.get(i)[6].toString()));

											if(acct8WSummary.get(i)[4]!=null)
												totalNotActAmt=totalNotActAmt+Double.valueOf(acct8WSummary.get(i)[4].toString());
											if(acct8WSummary.get(i)[6]!=null)
												totalNotActPer=totalNotActPer+Double.valueOf(acct8WSummary.get(i)[6].toString());

											if(acct8WSummary.get(i)[2]!=null)
												totalNotActCt=totalNotActCt+Double.valueOf(acct8WSummary.get(i)[2].toString());
											if(acct8WSummary.get(i)[5]!=null)
												totalNotActCtPer=totalNotActCtPer+Double.valueOf(acct8WSummary.get(i)[5].toString());
										}

									}

								}
							}
							else
							{
								accounted.add(l);
								accountingInProcess.add(l);
								finalAccounted.add(l);
								finalNotPosted.add(l);
								notAccounted.add(l);

								accountedAmtPer.add(l);
								accountingInProcessAmtPer.add(l);
								finalAccountedAmtPer.add(l);
								notAccountedAmtPer.add(l);


							}

						}
						if(from9Date.isAfter(fDate) || from9Date.equals(fDate))
						{


							List< Object[]> acct9WSummary=appModuleSummaryRepository.fetchAccountingAnalysisWithWeeks(procesRecDet.getTypeId(),from9Date,till9Date);
							log.info("acct5WSummary size :"+acct9WSummary.size());
							labelDatesValue.add(dashBoardV2Service.datesMap(from9Date.toString(),till9Date.toString()));

							labelValue.add("Week-9");
							if(acct9WSummary.size()>0)
							{
								for(int i=0;i<acct9WSummary.size();i++)
								{
									if(acct9WSummary!=null)
									{


										if(acct9WSummary.get(i)[0].toString().equalsIgnoreCase("Accounted"))
										{

											accounted.add(Double.valueOf(acct9WSummary.get(i)[5].toString()));
											accountedAmtPer.add(Double.valueOf(acct9WSummary.get(i)[6].toString()));

											if(acct9WSummary.get(i)[4]!=null)
												totalActAmt=totalActAmt+Double.valueOf(acct9WSummary.get(i)[4].toString());
											if(acct9WSummary.get(i)[6]!=null)
												totalActAmtPer=totalActAmtPer+Double.valueOf(acct9WSummary.get(i)[6].toString());
											if(acct9WSummary.get(i)[2]!=null)
												totalActCt=totalActCt+Double.valueOf(acct9WSummary.get(i)[2].toString());
											if(acct9WSummary.get(i)[5]!=null)
												totalActCtPer=totalActCtPer+Double.valueOf(acct9WSummary.get(i)[5].toString());

										}
										if(acct9WSummary.get(i)[0].toString().equalsIgnoreCase("Accounting inprocess") || acct9WSummary.get(i)[0].toString().equalsIgnoreCase("inprocess"))
										{

											accountingInProcess.add(Double.valueOf(acct9WSummary.get(i)[5].toString()));
											accountingInProcessAmtPer.add(Double.valueOf(acct9WSummary.get(i)[6].toString()));

											if(acct9WSummary.get(i)[4]!=null)
												totalActInProcAmt=totalActInProcAmt+Double.valueOf(acct9WSummary.get(i)[4].toString());
											if(acct9WSummary.get(i)[6]!=null)
												totalActInProcPer=totalActInProcPer+Double.valueOf(acct9WSummary.get(i)[6].toString());

											if(acct9WSummary.get(i)[2]!=null)
												totalActInProcCt=totalActInProcCt+Double.valueOf(acct9WSummary.get(i)[2].toString());
											if(acct9WSummary.get(i)[5]!=null)
												totalActInProcCtPer=totalActInProcCtPer+Double.valueOf(acct9WSummary.get(i)[5].toString());
										}
										if(acct9WSummary.get(i)[0].toString().equalsIgnoreCase("Final accounted"))
										{
											finalAccounted.add(Double.valueOf(acct9WSummary.get(i)[5].toString()));
											finalAccountedAmtPer.add(Double.valueOf(acct9WSummary.get(i)[6].toString()));

											if(acct9WSummary.get(i)[4]!=null)
												totalFinalActAmt=totalFinalActAmt+Double.valueOf(acct9WSummary.get(i)[4].toString());
											if(acct9WSummary.get(i)[6]!=null)
												totalFinalActPer=totalFinalActPer+Double.valueOf(acct9WSummary.get(i)[6].toString());

											if(acct9WSummary.get(i)[2]!=null)
												totalFinalActCt=totalFinalActCt+Double.valueOf(acct9WSummary.get(i)[2].toString());
											if(acct9WSummary.get(i)[5]!=null)
												totalFinalActCtPer=totalFinalActCtPer+Double.valueOf(acct9WSummary.get(i)[5].toString());
										}
										if(acct9WSummary.get(i)[0].toString().equalsIgnoreCase("Not accounted"))
										{

											notAccounted.add(Double.valueOf(acct9WSummary.get(i)[5].toString()));
											notAccountedAmtPer.add(Double.valueOf(acct9WSummary.get(i)[6].toString()));

											if(acct9WSummary.get(i)[4]!=null)
												totalNotActAmt=totalNotActAmt+Double.valueOf(acct9WSummary.get(i)[4].toString());
											if(acct9WSummary.get(i)[6]!=null)
												totalNotActPer=totalNotActPer+Double.valueOf(acct9WSummary.get(i)[6].toString());

											if(acct9WSummary.get(i)[2]!=null)
												totalNotActCt=totalNotActCt+Double.valueOf(acct9WSummary.get(i)[2].toString());
											if(acct9WSummary.get(i)[5]!=null)
												totalNotActCtPer=totalNotActCtPer+Double.valueOf(acct9WSummary.get(i)[5].toString());
										}

									}

								}
							}
							else
							{
								accounted.add(l);
								accountingInProcess.add(l);
								finalAccounted.add(l);
								finalNotPosted.add(l);
								notAccounted.add(l);


								accountedAmtPer.add(l);
								accountingInProcessAmtPer.add(l);
								finalAccountedAmtPer.add(l);
								notAccountedAmtPer.add(l);


							}


						}
						if(from10Date.isAfter(fDate) || from10Date.equals(fDate))
						{



							List< Object[]> acct10WSummary=appModuleSummaryRepository.fetchAccountingAnalysisWithWeeks(procesRecDet.getTypeId(),from10Date,till10Date);
							log.info("acct5WSummary size :"+acct10WSummary.size());
							labelDatesValue.add(dashBoardV2Service.datesMap(from10Date.toString(),till10Date.toString()));

							labelValue.add("Week-10");
							if(acct10WSummary.size()>0)
							{
								for(int i=0;i<acct10WSummary.size();i++)
								{
									if(acct10WSummary!=null)
									{


										if(acct10WSummary.get(i)[0].toString().equalsIgnoreCase("Accounted"))
										{

											accounted.add(Double.valueOf(acct10WSummary.get(i)[5].toString()));
											accountedAmtPer.add(Double.valueOf(acct10WSummary.get(i)[6].toString()));

											if(acct10WSummary.get(i)[4]!=null)
												totalActAmt=totalActAmt+Double.valueOf(acct10WSummary.get(i)[4].toString());
											if(acct10WSummary.get(i)[6]!=null)
												totalActAmtPer=totalActAmtPer+Double.valueOf(acct10WSummary.get(i)[6].toString());
											if(acct10WSummary.get(i)[2]!=null)
												totalActCt=totalActCt+Double.valueOf(acct10WSummary.get(i)[2].toString());
											if(acct10WSummary.get(i)[5]!=null)
												totalActCtPer=totalActCtPer+Double.valueOf(acct10WSummary.get(i)[5].toString());

										}
										if(acct10WSummary.get(i)[0].toString().equalsIgnoreCase("Accounting inprocess") || acct10WSummary.get(i)[0].toString().equalsIgnoreCase("inprocess"))
										{

											accountingInProcess.add(Double.valueOf(acct10WSummary.get(i)[5].toString()));
											accountingInProcessAmtPer.add(Double.valueOf(acct10WSummary.get(i)[6].toString()));

											if(acct10WSummary.get(i)[4]!=null)
												totalActInProcAmt=totalActInProcAmt+Double.valueOf(acct10WSummary.get(i)[4].toString());
											if(acct10WSummary.get(i)[6]!=null)
												totalActInProcPer=totalActInProcPer+Double.valueOf(acct10WSummary.get(i)[6].toString());

											if(acct10WSummary.get(i)[2]!=null)
												totalActInProcCt=totalActInProcCt+Double.valueOf(acct10WSummary.get(i)[2].toString());
											if(acct10WSummary.get(i)[5]!=null)
												totalActInProcCtPer=totalActInProcCtPer+Double.valueOf(acct10WSummary.get(i)[5].toString());
										}
										if(acct10WSummary.get(i)[0].toString().equalsIgnoreCase("Final accounted"))
										{
											finalAccounted.add(Double.valueOf(acct10WSummary.get(i)[5].toString()));
											finalAccountedAmtPer.add(Double.valueOf(acct10WSummary.get(i)[6].toString()));

											if(acct10WSummary.get(i)[4]!=null)
												totalFinalActAmt=totalFinalActAmt+Double.valueOf(acct10WSummary.get(i)[4].toString());
											if(acct10WSummary.get(i)[6]!=null)
												totalFinalActPer=totalFinalActPer+Double.valueOf(acct10WSummary.get(i)[6].toString());

											if(acct10WSummary.get(i)[2]!=null)
												totalFinalActCt=totalFinalActCt+Double.valueOf(acct10WSummary.get(i)[2].toString());
											if(acct10WSummary.get(i)[5]!=null)
												totalFinalActCtPer=totalFinalActCtPer+Double.valueOf(acct10WSummary.get(i)[5].toString());
										}
										if(acct10WSummary.get(i)[0].toString().equalsIgnoreCase("Not accounted"))
										{

											notAccounted.add(Double.valueOf(acct10WSummary.get(i)[5].toString()));
											notAccountedAmtPer.add(Double.valueOf(acct10WSummary.get(i)[6].toString()));

											if(acct10WSummary.get(i)[4]!=null)
												totalNotActAmt=totalNotActAmt+Double.valueOf(acct10WSummary.get(i)[4].toString());
											if(acct10WSummary.get(i)[6]!=null)
												totalNotActPer=totalNotActPer+Double.valueOf(acct10WSummary.get(i)[6].toString());

											if(acct10WSummary.get(i)[2]!=null)
												totalNotActCt=totalNotActCt+Double.valueOf(acct10WSummary.get(i)[2].toString());
											if(acct10WSummary.get(i)[5]!=null)
												totalNotActCtPer=totalNotActCtPer+Double.valueOf(acct10WSummary.get(i)[5].toString());
										}

									}

								}
							}
							else
							{
								accounted.add(l);
								accountingInProcess.add(l);
								finalAccounted.add(l);
								finalNotPosted.add(l);
								notAccounted.add(l);

								accountedAmtPer.add(l);
								accountingInProcessAmtPer.add(l);
								finalAccountedAmtPer.add(l);
								notAccountedAmtPer.add(l);


							}

						}
						if(from11Date.isAfter(fDate) || from11Date.equals(fDate))
						{

							List< Object[]> acct11WSummary=appModuleSummaryRepository.fetchAccountingAnalysisWithWeeks(procesRecDet.getTypeId(),from11Date,till11Date);
							log.info("acct5WSummary size :"+acct11WSummary.size());
							labelDatesValue.add(dashBoardV2Service.datesMap(from11Date.toString(),till11Date.toString()));

							labelValue.add("Week-11");
							if(acct11WSummary.size()>0)
							{
								for(int i=0;i<acct11WSummary.size();i++)
								{
									if(acct11WSummary!=null)
									{


										if(acct11WSummary.get(i)[0].toString().equalsIgnoreCase("Accounted"))
										{

											accounted.add(Double.valueOf(acct11WSummary.get(i)[5].toString()));
											accountedAmtPer.add(Double.valueOf(acct11WSummary.get(i)[6].toString()));

											if(acct11WSummary.get(i)[4]!=null)
												totalActAmt=totalActAmt+Double.valueOf(acct11WSummary.get(i)[4].toString());
											if(acct11WSummary.get(i)[6]!=null)
												totalActAmtPer=totalActAmtPer+Double.valueOf(acct11WSummary.get(i)[6].toString());
											if(acct11WSummary.get(i)[2]!=null)
												totalActCt=totalActCt+Double.valueOf(acct11WSummary.get(i)[2].toString());
											if(acct11WSummary.get(i)[5]!=null)
												totalActCtPer=totalActCtPer+Double.valueOf(acct11WSummary.get(i)[5].toString());

										}
										if(acct11WSummary.get(i)[0].toString().equalsIgnoreCase("Accounting inprocess") || acct11WSummary.get(i)[0].toString().equalsIgnoreCase("inprocess"))
										{

											accountingInProcess.add(Double.valueOf(acct11WSummary.get(i)[5].toString()));
											accountingInProcessAmtPer.add(Double.valueOf(acct11WSummary.get(i)[6].toString()));

											if(acct11WSummary.get(i)[4]!=null)
												totalActInProcAmt=totalActInProcAmt+Double.valueOf(acct11WSummary.get(i)[4].toString());
											if(acct11WSummary.get(i)[6]!=null)
												totalActInProcPer=totalActInProcPer+Double.valueOf(acct11WSummary.get(i)[6].toString());

											if(acct11WSummary.get(i)[2]!=null)
												totalActInProcCt=totalActInProcCt+Double.valueOf(acct11WSummary.get(i)[2].toString());
											if(acct11WSummary.get(i)[5]!=null)
												totalActInProcCtPer=totalActInProcCtPer+Double.valueOf(acct11WSummary.get(i)[5].toString());
										}
										if(acct11WSummary.get(i)[0].toString().equalsIgnoreCase("Final accounted"))
										{

											finalAccounted.add(Double.valueOf(acct11WSummary.get(i)[5].toString()));
											finalAccountedAmtPer.add(Double.valueOf(acct11WSummary.get(i)[6].toString()));

											if(acct11WSummary.get(i)[4]!=null)
												totalFinalActAmt=totalFinalActAmt+Double.valueOf(acct11WSummary.get(i)[4].toString());
											if(acct11WSummary.get(i)[6]!=null)
												totalFinalActPer=totalFinalActPer+Double.valueOf(acct11WSummary.get(i)[6].toString());

											if(acct11WSummary.get(i)[2]!=null)
												totalFinalActCt=totalFinalActCt+Double.valueOf(acct11WSummary.get(i)[2].toString());
											if(acct11WSummary.get(i)[5]!=null)
												totalFinalActCtPer=totalFinalActCtPer+Double.valueOf(acct11WSummary.get(i)[5].toString());
										}
										if(acct11WSummary.get(i)[0].toString().equalsIgnoreCase("Not accounted"))
										{

											notAccounted.add(Double.valueOf(acct11WSummary.get(i)[5].toString()));
											notAccountedAmtPer.add(Double.valueOf(acct11WSummary.get(i)[6].toString()));

											if(acct11WSummary.get(i)[4]!=null)
												totalNotActAmt=totalNotActAmt+Double.valueOf(acct11WSummary.get(i)[4].toString());
											if(acct11WSummary.get(i)[6]!=null)
												totalNotActPer=totalNotActPer+Double.valueOf(acct11WSummary.get(i)[6].toString());

											if(acct11WSummary.get(i)[2]!=null)
												totalNotActCt=totalNotActCt+Double.valueOf(acct11WSummary.get(i)[2].toString());
											if(acct11WSummary.get(i)[5]!=null)
												totalNotActCtPer=totalNotActCtPer+Double.valueOf(acct11WSummary.get(i)[5].toString());
										}

									}

								}
							}
							else
							{
								accounted.add(l);
								accountingInProcess.add(l);
								finalAccounted.add(l);
								finalNotPosted.add(l);
								notAccounted.add(l);

								accountedAmtPer.add(l);
								accountingInProcessAmtPer.add(l);
								finalAccountedAmtPer.add(l);
								notAccountedAmtPer.add(l);


							}

						}
						if(from12Date.isAfter(fDate) || from12Date.equals(fDate))
						{

							List< Object[]> acct12WSummary=appModuleSummaryRepository.fetchAccountingAnalysisWithWeeks(procesRecDet.getTypeId(),from12Date,till12Date);
							log.info("acct5WSummary size :"+acct12WSummary.size());
							labelDatesValue.add(dashBoardV2Service.datesMap(from12Date.toString(),till12Date.toString()));

							labelValue.add("Week-12");
							if(acct12WSummary.size()>0)
							{
								for(int i=0;i<acct12WSummary.size();i++)
								{
									if(acct12WSummary!=null)
									{


										if(acct12WSummary.get(i)[0].toString().equalsIgnoreCase("Accounted"))
										{

											accounted.add(Double.valueOf(acct12WSummary.get(i)[5].toString()));
											accountedAmtPer.add(Double.valueOf(acct12WSummary.get(i)[6].toString()));

											if(acct12WSummary.get(i)[4]!=null)
												totalActAmt=totalActAmt+Double.valueOf(acct12WSummary.get(i)[4].toString());
											if(acct12WSummary.get(i)[6]!=null)
												totalActAmtPer=totalActAmtPer+Double.valueOf(acct12WSummary.get(i)[6].toString());
											if(acct12WSummary.get(i)[2]!=null)
												totalActCt=totalActCt+Double.valueOf(acct12WSummary.get(i)[2].toString());
											if(acct12WSummary.get(i)[5]!=null)
												totalActCtPer=totalActCtPer+Double.valueOf(acct12WSummary.get(i)[5].toString());

										}
										if(acct12WSummary.get(i)[0].toString().equalsIgnoreCase("Accounting inprocess") || acct12WSummary.get(i)[0].toString().equalsIgnoreCase("inprocess"))
										{

											accountingInProcess.add(Double.valueOf(acct12WSummary.get(i)[5].toString()));
											accountingInProcessAmtPer.add(Double.valueOf(acct12WSummary.get(i)[6].toString()));

											if(acct12WSummary.get(i)[4]!=null)
												totalActInProcAmt=totalActInProcAmt+Double.valueOf(acct12WSummary.get(i)[4].toString());
											if(acct12WSummary.get(i)[6]!=null)
												totalActInProcPer=totalActInProcPer+Double.valueOf(acct12WSummary.get(i)[6].toString());

											if(acct12WSummary.get(i)[2]!=null)
												totalActInProcCt=totalActInProcCt+Double.valueOf(acct12WSummary.get(i)[2].toString());
											if(acct12WSummary.get(i)[5]!=null)
												totalActInProcCtPer=totalActInProcCtPer+Double.valueOf(acct12WSummary.get(i)[5].toString());
										}
										if(acct12WSummary.get(i)[0].toString().equalsIgnoreCase("Final accounted"))
										{

											finalAccounted.add(Double.valueOf(acct12WSummary.get(i)[5].toString()));
											finalAccountedAmtPer.add(Double.valueOf(acct12WSummary.get(i)[6].toString()));

											if(acct12WSummary.get(i)[4]!=null)
												totalFinalActAmt=totalFinalActAmt+Double.valueOf(acct12WSummary.get(i)[4].toString());
											if(acct12WSummary.get(i)[6]!=null)
												totalFinalActPer=totalFinalActPer+Double.valueOf(acct12WSummary.get(i)[6].toString());

											if(acct12WSummary.get(i)[2]!=null)
												totalFinalActCt=totalFinalActCt+Double.valueOf(acct12WSummary.get(i)[2].toString());
											if(acct12WSummary.get(i)[5]!=null)
												totalFinalActCtPer=totalFinalActCtPer+Double.valueOf(acct12WSummary.get(i)[5].toString());
										}
										if(acct12WSummary.get(i)[0].toString().equalsIgnoreCase("Not accounted"))
										{

											notAccounted.add(Double.valueOf(acct12WSummary.get(i)[5].toString()));
											notAccountedAmtPer.add(Double.valueOf(acct12WSummary.get(i)[6].toString()));

											if(acct12WSummary.get(i)[4]!=null)
												totalNotActAmt=totalNotActAmt+Double.valueOf(acct12WSummary.get(i)[4].toString());
											if(acct12WSummary.get(i)[6]!=null)
												totalNotActPer=totalNotActPer+Double.valueOf(acct12WSummary.get(i)[6].toString());

											if(acct12WSummary.get(i)[2]!=null)
												totalNotActCt=totalNotActCt+Double.valueOf(acct12WSummary.get(i)[2].toString());
											if(acct12WSummary.get(i)[5]!=null)
												totalNotActCtPer=totalNotActCtPer+Double.valueOf(acct12WSummary.get(i)[5].toString());
										}

									}

								}
							}
							else
							{
								accounted.add(l);
								accountingInProcess.add(l);
								finalAccounted.add(l);
								finalNotPosted.add(l);
								notAccounted.add(l);

								accountedAmtPer.add(l);
								accountingInProcessAmtPer.add(l);
								finalAccountedAmtPer.add(l);
								notAccountedAmtPer.add(l);


							}

						}
						if(from13Date.isAfter(fDate) || from13Date.equals(fDate))
						{


							List< Object[]> acct13WSummary=appModuleSummaryRepository.fetchAccountingAnalysisWithWeeks(procesRecDet.getTypeId(),from13Date,till13Date);
							log.info("acct5WSummary size :"+acct13WSummary.size());
							labelDatesValue.add(dashBoardV2Service.datesMap(from13Date.toString(),till13Date.toString()));

							labelValue.add("Week-13");
							if(acct13WSummary.size()>0)
							{
								for(int i=0;i<acct13WSummary.size();i++)
								{
									if(acct13WSummary!=null)
									{


										if(acct13WSummary.get(i)[0].toString().equalsIgnoreCase("Accounted"))
										{

											accounted.add(Double.valueOf(acct13WSummary.get(i)[5].toString()));
											accountedAmtPer.add(Double.valueOf(acct13WSummary.get(i)[6].toString()));

											if(acct13WSummary.get(i)[4]!=null)
												totalActAmt=totalActAmt+Double.valueOf(acct13WSummary.get(i)[4].toString());
											if(acct13WSummary.get(i)[6]!=null)
												totalActAmtPer=totalActAmtPer+Double.valueOf(acct13WSummary.get(i)[6].toString());
											if(acct13WSummary.get(i)[2]!=null)
												totalActCt=totalActCt+Double.valueOf(acct13WSummary.get(i)[2].toString());
											if(acct13WSummary.get(i)[5]!=null)
												totalActCtPer=totalActCtPer+Double.valueOf(acct13WSummary.get(i)[5].toString());

										}
										if(acct13WSummary.get(i)[0].toString().equalsIgnoreCase("Accounting inprocess") || acct13WSummary.get(i)[0].toString().equalsIgnoreCase("inprocess"))
										{

											accountingInProcess.add(Double.valueOf(acct13WSummary.get(i)[5].toString()));
											accountingInProcessAmtPer.add(Double.valueOf(acct13WSummary.get(i)[6].toString()));

											if(acct13WSummary.get(i)[4]!=null)
												totalActInProcAmt=totalActInProcAmt+Double.valueOf(acct13WSummary.get(i)[4].toString());
											if(acct13WSummary.get(i)[6]!=null)
												totalActInProcPer=totalActInProcPer+Double.valueOf(acct13WSummary.get(i)[6].toString());

											if(acct13WSummary.get(i)[2]!=null)
												totalActInProcCt=totalActInProcCt+Double.valueOf(acct13WSummary.get(i)[2].toString());
											if(acct13WSummary.get(i)[5]!=null)
												totalActInProcCtPer=totalActInProcCtPer+Double.valueOf(acct13WSummary.get(i)[5].toString());
										}
										if(acct13WSummary.get(i)[0].toString().equalsIgnoreCase("Final accounted"))
										{
											finalAccounted.add(Double.valueOf(acct13WSummary.get(i)[5].toString()));
											finalAccountedAmtPer.add(Double.valueOf(acct13WSummary.get(i)[6].toString()));

											if(acct13WSummary.get(i)[4]!=null)
												totalFinalActAmt=totalFinalActAmt+Double.valueOf(acct13WSummary.get(i)[4].toString());
											if(acct13WSummary.get(i)[6]!=null)
												totalFinalActPer=totalFinalActPer+Double.valueOf(acct13WSummary.get(i)[6].toString());

											if(acct13WSummary.get(i)[2]!=null)
												totalFinalActCt=totalFinalActCt+Double.valueOf(acct13WSummary.get(i)[2].toString());
											if(acct13WSummary.get(i)[5]!=null)
												totalFinalActCtPer=totalFinalActCtPer+Double.valueOf(acct13WSummary.get(i)[5].toString());
										}
										if(acct13WSummary.get(i)[0].toString().equalsIgnoreCase("Not accounted"))
										{

											notAccounted.add(Double.valueOf(acct13WSummary.get(i)[5].toString()));
											notAccountedAmtPer.add(Double.valueOf(acct13WSummary.get(i)[6].toString()));

											if(acct13WSummary.get(i)[4]!=null)
												totalNotActAmt=totalNotActAmt+Double.valueOf(acct13WSummary.get(i)[4].toString());
											if(acct13WSummary.get(i)[6]!=null)
												totalNotActPer=totalNotActPer+Double.valueOf(acct13WSummary.get(i)[6].toString());

											if(acct13WSummary.get(i)[2]!=null)
												totalNotActCt=totalNotActCt+Double.valueOf(acct13WSummary.get(i)[2].toString());
											if(acct13WSummary.get(i)[5]!=null)
												totalNotActCtPer=totalNotActCtPer+Double.valueOf(acct13WSummary.get(i)[5].toString());
										}

									}

								}
							}
							else
							{
								accounted.add(l);
								accountingInProcess.add(l);
								finalAccounted.add(l);
								finalNotPosted.add(l);
								notAccounted.add(l);

								accountedAmtPer.add(l);
								accountingInProcessAmtPer.add(l);
								finalAccountedAmtPer.add(l);
								notAccountedAmtPer.add(l);


							}

						}


						log.info("labelValue :"+labelValue);
						log.info("accounted :"+accounted);
						log.info("accountingInProcess :"+accountingInProcess);
						log.info("finalAccounted :"+finalAccounted);
						log.info("notAccounted :"+notAccounted);
						List<Double> result = IntStream.range(0, accounted.size())
								.mapToObj(i -> notAccounted.get(i) + accountingInProcess.get(i))
								.collect(Collectors.toList());

						LinkedHashMap map=new LinkedHashMap();
						map.put("labelValue", labelValue);
						map.put("labelDatesValue", labelDatesValue);
						map.put("accounted", accounted);
						map.put("accountingInProcess", accountingInProcess);
						map.put("finalAccounted", finalAccounted);
						map.put("finalNotAccounted", finalNotPosted);
						map.put("notAccounted", notAccounted);
						map.put("notActAndAcctInProc", result);



						List<Double> resultAmtPer = IntStream.range(0, accountingInProcessAmtPer.size())
								.mapToObj(i -> Double.valueOf((dform.format(notAccountedAmtPer.get(i) + accountingInProcessAmtPer.get(i)).toString())))
								.collect(Collectors.toList());
						/**amt per**/

						map.put("accountedAmtPer", accountedAmtPer);
						map.put("accountingInProcessAmtPer", accountingInProcessAmtPer);
						map.put("finalAccountedAmtPer", finalAccountedAmtPer);
						map.put("notAccountedAmtPer", notAccountedAmtPer);
						map.put("notActAndAcctInProcAmtPer", resultAmtPer);

						// twoMonthMap
						LinkedHashMap tmmap=new LinkedHashMap();

						tmmap.put("actamount", Double.valueOf(dform.format(totalActAmt)));
						tmmap.put("actPercentage", Double.valueOf(dform.format(totalActAmtPer)));
						tmmap.put("actInProcessamount", Double.valueOf(dform.format(totalActInProcAmt)));
						tmmap.put("actInProcessPercentage", Double.valueOf(dform.format(totalActInProcPer)));
						tmmap.put("finalActamount", Double.valueOf(dform.format(totalFinalActAmt)));
						tmmap.put("finalActPercentage", Double.valueOf(dform.format(totalFinalActPer)));

						//to get final un posted amount and percentage 
						Double finalNotPostedAmt=totalActAmt-totalFinalActAmt;
						tmmap.put("finalUnpostedAmt", Double.valueOf(dform.format(finalNotPostedAmt)));
						Double finalNotPostedPer=totalActAmtPer-totalFinalActPer;
						tmmap.put("finalUnpostedPer", Double.valueOf(dform.format(finalNotPostedPer)));

						tmmap.put("notActamount", Double.valueOf(dform.format(totalNotActAmt)));
						tmmap.put("notActPercentage", Double.valueOf(dform.format(totalNotActPer)));



						tmmap.put("actCt", Double.valueOf(dform.format(totalActCt)));
						tmmap.put("actCtPer",Double.valueOf( dform.format(totalActCtPer)));
						tmmap.put("actInProcessCt", Double.valueOf(dform.format(totalActInProcCt)));
						tmmap.put("actInProcessCtPer", Double.valueOf(dform.format(totalActInProcCtPer)));
						tmmap.put("finalActCt", Double.valueOf(dform.format(totalFinalActCt)));
						tmmap.put("finalActCtPer", Double.valueOf(dform.format(totalFinalActCtPer)));
						tmmap.put("notActCt", Double.valueOf(dform.format(totalNotActCt)));
						tmmap.put("notActCtPer", Double.valueOf(dform.format(totalNotActCtPer)));

						tmmap.put("notActAndAcctInProcCt", Double.valueOf(dform.format(Double.valueOf(totalNotActCt)+Double.valueOf(totalActInProcCt))));
						tmmap.put("notActAndAcctInProcCtPer", Double.valueOf(dform.format(Double.valueOf(totalNotActCtPer)+Double.valueOf(totalActInProcCtPer))));

						tmmap.put("notActAndAcctInProcAmt", Double.valueOf(dform.format(Double.valueOf(totalNotActAmt)+Double.valueOf(totalActInProcAmt))));
						tmmap.put("notActAndAcctInProcAmtPer",Double.valueOf( dform.format(Double.valueOf(totalNotActPer)+Double.valueOf(totalActInProcPer))));


						tmmap.put("detailedData", map);
						eachMap.put("3M", tmmap);

					}

				}
				return eachMap;

			}



			/**
			 * bar graph for reconciliation
			 * @param processId
			 * @param dates
			 * @return
			 * @throws SQLException
			 * @throws ParseException 
			 */
			@PostMapping("/getSummaryInfoForReconciliationV2")
			@Timed 
			public LinkedHashMap getSummaryInfoForReconciliationV2(HttpServletRequest request,@RequestParam String processId ,@RequestBody HashMap dates,@RequestParam int violation) throws SQLException, ParseException
			{
				log.info("Rest request to getSummaryInfoForReconciliationV2 for a process:"+processId);
				LinkedHashMap finalMap=new LinkedHashMap();
				List<LinkedHashMap> dataMap=new ArrayList<LinkedHashMap>();
		    	HashMap map1=userJdbcService.getuserInfoFromToken(request);
		      	Long tenantId=Long.parseLong(map1.get("tenantId").toString());
				Processes processes1 = processesRepository.findByTenantIdAndIdForDisplay(tenantId, processId);
				Processes processes=processesRepository.findOne(processes1.getId());
				ProcessDetails procesDet=processDetailsRepository.findByProcessIdAndTagType(processes.getId(), "reconciliationRuleGroup");
				if(procesDet!=null)
				{
					ZonedDateTime fmDate=ZonedDateTime.parse(dates.get("startDate").toString());
					ZonedDateTime toDate=ZonedDateTime.parse(dates.get("endDate").toString());
					log.info("fmDate :"+fmDate);
					log.info("toDate :"+toDate);
					java.time.LocalDate fDate=fmDate.toLocalDate();
					java.time.LocalDate tDate=toDate.toLocalDate();
					
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
					
					ResultSet resultDv=null;
					ResultSet resultApprovedAmt=null;
					
					
					conn = DriverManager.getConnection(dbUrl, userName, password);
					
					
					
					List<String> rulesList=new ArrayList<String>();
					List<Long> rulesIdList=new ArrayList<Long>();

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

						LinkedHashMap map=new LinkedHashMap();
						map.put("DvCount", reconSummary.get(i)[0]);
						totalDVCount=totalDVCount+Double.valueOf(reconSummary.get(i)[0].toString());
						map.put("ReconciledCount", reconSummary.get(i)[1]);
						map.put("unReconciledCount", reconSummary.get(i)[2]);
						map.put("reconciledPer", reconSummary.get(i)[3]);
						map.put("unReconciledPer", reconSummary.get(i)[4].toString());
						map.put("reconciledAmt", reconSummary.get(i)[8].toString());
						map.put("unReconciledAmt", reconSummary.get(i)[6].toString());
						totalDVAmt=totalDVAmt+Double.valueOf(reconSummary.get(i)[10].toString());
						Double reconciledAmtPer=(Double.valueOf(reconSummary.get(i)[8].toString())/Double.valueOf(reconSummary.get(i)[10].toString()))*100;
					//	Double unReconciledAmtPer=(Double.valueOf(reconSummary.get(i)[6].toString())/Double.valueOf(reconSummary.get(i)[10].toString()))*100;
						
						Double unReconciledAmtPer=100-reconciledAmtPer;


						map.put("reconciledAmtPer", Double.valueOf(dform.format(reconciledAmtPer)));
						map.put("unReconciledAmtPer",Double.valueOf(dform.format(unReconciledAmtPer)));
						map.put("viewId", reconSummary.get(i)[5]);

						totalReconAmt=totalReconAmt+Double.valueOf(reconSummary.get(i)[8].toString());
						totalUnReconAmt=totalUnReconAmt+Double.valueOf(reconSummary.get(i)[6].toString());

						approvedCount=approvedCount+Double.valueOf(reconSummary.get(i)[9].toString());
						map.put("unApprovedCount", Double.valueOf(reconSummary.get(i)[7].toString()));
						totalUnApprovedCt=totalUnApprovedCt+Double.valueOf(reconSummary.get(i)[7].toString());

						DataViews dv=dataViewsRepository.findOne(Long.valueOf(reconSummary.get(i)[5].toString()));
						map.put("viewName", dv.getDataViewDispName());
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
							if(Long.valueOf(ruleDetailsForView.get(r)[0].toString())==0)
								dvRuleDetail.put("ruleName", "Manual");
							else
							{
								Rules rule=rulesRepository.findOne(Long.valueOf(ruleDetailsForView.get(r)[0].toString()));
								dvRuleDetail.put("ruleName",rule.getRuleCode());
							}
							dvRuleDetail.put("type", ruleDetailsForView.get(r)[1]);
							dvRuleDetailList.add(dvRuleDetail);
						}
						map.put("dvRuleDetails", dvRuleDetailList);
		

						DataViewsColumns dvColumn=dataViewsColumnsRepository.findByDataViewIdAndQualifier(dv.getId(), "AMOUNT");
						String ammountQualifier="";
						if(dvColumn.getRefDvType()!=null &&dvColumn.getRefDvType().equalsIgnoreCase("File Template"))
						{
							FileTemplateLines ftl=fileTemplateLinesRepository.findOne(Long.valueOf(dvColumn.getRefDvColumn()));
							ammountQualifier=ftl.getColumnAlias();
						}
						else
							ammountQualifier=dvColumn.getColumnName();

						
						log.info("Connected database successfully...");
						stmtDv = conn.createStatement();
						stmtApprovedAmt = conn.createStatement();

					
						String query="";

						if(finalSrcIdList!=null && !finalSrcIdList.isEmpty())
						{
							String finalSrcIds=finalSrcIdList.toString().replaceAll("\\[", "").replaceAll("\\]", "");

							query="select DATEDIFF( SYSDATE(), `v`.`fileDate`) as `rule_age`,count(scrIds),sum(`"+ammountQualifier+"`) as `"+ammountQualifier+"` from "+schemaName+".`"+dv.getDataViewName().toLowerCase().toLowerCase()+"` v where fileDate between '"+fDate+"' and '"+tDate+"' "
									+ " and scrIds not in ("+finalSrcIds+")group by rule_age";

						}
						else
						{
							query="select DATEDIFF( SYSDATE(), `v`.`fileDate`) as `rule_age`,count(scrIds),sum(`"+ammountQualifier+"`) as `"+ammountQualifier+"` from "+schemaName+".`"+dv.getDataViewName().toLowerCase().toLowerCase()+"` v where fileDate between '"+fDate+"' and '"+tDate+"' "
									+ "group by rule_age";

						}
						log.info("query :"+query);

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
							totalViolationCount=totalViolationCount+Long.valueOf(resultDv.getString("count(scrIds)").toString());
							log.info("ruleAge"+ruleAge);
							if(ruleAge>violation)
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

							query="select sum(`"+ammountQualifier+"`) as `"+ammountQualifier+"` from "+schemaName+".`"+dv.getDataViewName().toLowerCase().toLowerCase()+"` v where fileDate between '"+fDate+"' and '"+tDate+"' "
									+ " and scrIds not in ("+finalSrcIds+")";

						}
						else
						{
							query="select sum(`"+ammountQualifier+"`) as `"+ammountQualifier+"` from "+schemaName+".`"+dv.getDataViewName().toLowerCase().toLowerCase()+"` v where fileDate between '"+fDate+"' and '"+tDate+"'";


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
					log.info("totalUnReconAmt :"+totalUnReconAmt);
					log.info("totalDVAmt :"+totalDVAmt);
					Double unReconItemsValuePer=0d;
					Double unApprovedCtPer=0d;
					double unReconItemsViolationPer=0l;
					if(totalReconAmt>0)
						unReconItemsValuePer=(totalUnReconAmt/totalDVAmt)*100;
					if(totalDVCount>0)
						unApprovedCtPer=(totalUnApprovedCt/totalDVCount)*100;
					if(totalViolationCount>0)
						unReconItemsViolationPer=((double)violationCount/totalViolationCount)*100;
					log.info("rulesList :"+rulesList);
					finalMap.put("rulesList", rulesList);
					finalMap.put("rulesIdList", rulesIdList);
					finalMap.put("unReconItemsValue", totalUnReconAmt);
					finalMap.put("unReconItemsValuePer", dform.format(unReconItemsValuePer));
					finalMap.put("unReconItemsViolation", violationCount);
					finalMap.put("unReconItemsViolationPer", unReconItemsViolationPer);
					finalMap.put("awaitingAppCount", totalUnApprovedCt);
					finalMap.put("awaitingAppCountPer", dform.format(unApprovedCtPer));
					finalMap.put("reconciliationData", dataMap);
					
					if(resultDv!=null)
						resultDv.close();
					if(resultApprovedAmt!=null)
						resultApprovedAmt.close();
					if(stmtDv!=null)
						stmtDv.close();
					if(stmtApprovedAmt!=null)
						stmtApprovedAmt.close();
					if(conn!=null)
						conn.close();

	
				}

				log.info("******end Time : "+ZonedDateTime.now()+"*******");
				return finalMap;
			}

			/**api to fetch violated records for unreconciliation or unaccounted**/

			@PostMapping("/getUnReconciledRecordsForViolation")
			@Timed 
			public List<LinkedHashMap> getUnReconciledRecordsForViolation(HttpServletRequest request,@RequestParam String processId,@RequestParam Long viewId,@RequestParam String amtQuailifier
					,@RequestParam List<String> groupByColmns,@RequestBody HashMap dates,@RequestParam String module,@RequestParam int violation) throws SQLException, ParseException
					{
				HashMap map=userJdbcService.getuserInfoFromToken(request);
				Long tenantId=Long.parseLong(map.get("tenantId").toString());
				Processes processes = processesRepository.findByTenantIdAndIdForDisplay(tenantId, processId);
				ProcessDetails procesRecDet=processDetailsRepository.findByProcessIdAndTagType(processes.getId(), "reconciliationRuleGroup");
				ProcessDetails procesActDet=processDetailsRepository.findByProcessIdAndTagType(processes.getId(), "accountingRuleGroup");
				ZonedDateTime fmDate=ZonedDateTime.parse(dates.get("startDate").toString());
				ZonedDateTime toDate=ZonedDateTime.parse(dates.get("endDate").toString());
				log.info("fmDate :"+fmDate);
				log.info("toDate :"+toDate);
				java.time.LocalDate fDate=fmDate.toLocalDate();
				java.time.LocalDate tDate=toDate.toLocalDate();

				List<BigInteger> finalSrcIdList=new ArrayList<BigInteger>();


				if(module.equalsIgnoreCase("un-reconciled"))
				{
					log.info(" in if un-reconciled");
					List<BigInteger> reconciliedSrcIds=reconciliationResultRepository.fetchReconciledSourceIds(tenantId, procesRecDet.getTypeId(), viewId);	
					log.info("reconciliedSrcIds.size :"+reconciliedSrcIds.size());
					finalSrcIdList.addAll(reconciliedSrcIds);
					List<BigInteger> reconciliedTrgIds=reconciliationResultRepository.fetchReconciledTargetIds(tenantId, procesRecDet.getTypeId(), viewId);	
					log.info("reconciliedTrgIds.size :"+reconciliedTrgIds.size());
					finalSrcIdList.addAll(reconciliedTrgIds);
				}
				else if(module.equalsIgnoreCase("not accounted"))
				{
					log.info(" in if not accounted");
					//only equal to accounted
					List<BigInteger> accountedViewIds=accountedSummaryRepository.fetchAccountedRowIdsByRuleGrpIdAndViewId(procesActDet.getTypeId(), viewId) ;
					finalSrcIdList.addAll(accountedViewIds);
				}
				log.info("finalSrcIdList :"+finalSrcIdList);
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
				ResultSet resultAct=null;
				DataViews dvName=dataViewsRepository.findOne(viewId);

				String groupBy=groupByColmns.toString().replaceAll("\\[", "").replaceAll("\\]", "");
				String finalSrcIds=finalSrcIdList.toString().replaceAll("\\[", "").replaceAll("\\]", "");
				log.info("groupBy :"+groupBy);
				String query="";
				List<LinkedHashMap> finalMap=new ArrayList<LinkedHashMap>();

				if(finalSrcIds!=null && !finalSrcIds.isEmpty())
				{
					query="select * from (select DATEDIFF( SYSDATE(), `v`.`fileDate`) as `rule_age`,`"+groupBy+"`,round(sum(`"+amtQuailifier+"`),"+round+") as `"+amtQuailifier+"` from "+schemaName+".`"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` v where fileDate between '"+fDate+"' and '"+tDate+"' "
							+ " and scrIds not in ("+finalSrcIds+")group by rule_age,`"+groupBy+"`) a where rule_age>"+violation+" order by `"+amtQuailifier+"` desc limit 10";
					//log.info("query :"+query);
				}
				else
				{
					query="select * from (select DATEDIFF( SYSDATE(), `v`.`fileDate`) as `rule_age`,`"+groupBy+"`,round(sum(`"+amtQuailifier+"`),"+round+") as `"+amtQuailifier+"` from "+schemaName+".`"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` v where fileDate between '"+fDate+"' and '"+tDate+"'"
							+ " group by rule_age,`"+groupBy+"`) a where rule_age>"+violation+" order by `"+amtQuailifier+"` desc limit 10";
					//log.info("query :"+query);
				}

				resultDv=stmtDv.executeQuery(query);
				ResultSetMetaData rsmd2 = resultDv.getMetaData();
				int columnCount = rsmd2.getColumnCount();

				while(resultDv.next())
				{
					LinkedHashMap map2=new LinkedHashMap();
					for (int i = 1; i <= columnCount; i++ ) {
						String name = rsmd2.getColumnName(i); 
						if(i==2)
							map2.put("name", resultDv.getString(i));
						if(i==3)
							map2.put("value", resultDv.getString(i));

					}
					finalMap.add(map2);
				}
				if(conn!=null)
					conn.close();
				if(stmtDv!=null)
					stmtDv.close();
				if(resultDv!=null)
					resultDv.close();
				return finalMap;

					}




			/**api to fetch unapproved records for reconciliation  or accounted**/

			@PostMapping("/getUnApprovedRecords")
			@Timed 
			public List<LinkedHashMap> getUnApprovedRecords(@RequestParam String processId,@RequestParam Long viewId,@RequestParam String amtQuailifier
					,@RequestParam List<String> groupByColmns,@RequestBody HashMap dates,HttpServletRequest request,@RequestParam String module) throws SQLException, ParseException
					{
				HashMap map=userJdbcService.getuserInfoFromToken(request);
				Long tenantId=Long.parseLong(map.get("tenantId").toString());
				Processes processes = processesRepository.findByTenantIdAndIdForDisplay(tenantId, processId);
				ProcessDetails procesRecDet=processDetailsRepository.findByProcessIdAndTagType(processes.getId(), "reconciliationRuleGroup");
				ProcessDetails procesActDet=processDetailsRepository.findByProcessIdAndTagType(processes.getId(), "accountingRuleGroup");
				ZonedDateTime fmDate=ZonedDateTime.parse(dates.get("startDate").toString());
				ZonedDateTime toDate=ZonedDateTime.parse(dates.get("endDate").toString());
				log.info("fmDate :"+fmDate);
				log.info("toDate :"+toDate);
				java.time.LocalDate fDate=fmDate.toLocalDate();
				java.time.LocalDate tDate=toDate.toLocalDate();

				List<BigInteger> finalSrcIdList=new ArrayList<BigInteger>();


				if(module.equalsIgnoreCase("reconciled"))
				{
					log.info(" in if reconciled");
					List<BigInteger> approvedSrcIds=reconciliationResultRepository.fetchApprovedSourceIds(tenantId, procesRecDet.getTypeId(), viewId);	
					log.info("reconciliedSrcIds.size :"+approvedSrcIds.size());
					finalSrcIdList.addAll(approvedSrcIds);
					List<BigInteger> approvedTrgIds=reconciliationResultRepository.fetchApprovedTargetIds(tenantId, procesRecDet.getTypeId(), viewId);	
					log.info("reconciliedTrgIds.size :"+approvedTrgIds.size());
					finalSrcIdList.addAll(approvedTrgIds);
				}
				else if(module.equalsIgnoreCase("accounted"))
				{
					log.info(" in if not accounted");
					List<BigInteger> approvedViewIds=accountedSummaryRepository.fetchDistinctApprovedRowIds(procesActDet.getTypeId(), viewId) ;
					finalSrcIdList.addAll(approvedViewIds);
				}
				log.info("finalSrcIdList :"+finalSrcIdList);
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
				// ResultSet resultAct=null;
				DataViews dvName=dataViewsRepository.findOne(viewId);

				String groupBy=groupByColmns.toString().replaceAll("\\[", "").replaceAll("\\]", "");
				String finalSrcIds=finalSrcIdList.toString().replaceAll("\\[", "").replaceAll("\\]", "");
				log.info("groupBy :"+groupBy);
				String query="";
				List<LinkedHashMap> finalMap=new ArrayList<LinkedHashMap>();

				if(finalSrcIds!=null && !finalSrcIds.isEmpty())
				{
					query="select * from (select "+groupBy+",round(sum(`"+amtQuailifier+"`),"+round+") as `"+amtQuailifier+"` from "+schemaName+".`"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` v where fileDate between '"+fDate+"' and '"+tDate+"' "
							+ " and scrIds not in ("+finalSrcIds+")group by "+groupBy+") a order by `"+amtQuailifier+"` desc limit 10";
					//log.info("query :"+query);
				}
				else
				{
					query="select * from (select "+groupBy+",round(sum(`"+amtQuailifier+"`),"+round+") as `"+amtQuailifier+"` from "+schemaName+".`"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` v where fileDate between '"+fDate+"' and '"+tDate+"'"
							+ " group by "+groupBy+") a order by `"+amtQuailifier+"` desc limit 10";
					//log.info("query :"+query);
				}

				resultDv=stmtDv.executeQuery(query);
				ResultSetMetaData rsmd2 = resultDv.getMetaData();
				int columnCount = rsmd2.getColumnCount();

				while(resultDv.next())
				{
					LinkedHashMap map2=new LinkedHashMap();
					for (int i = 1; i <= columnCount; i++ ) {
						String name = rsmd2.getColumnName(i); 
						if(i==1)
							map2.put("name", resultDv.getString(i));
						if(i==2)
							map2.put("value", resultDv.getString(i));

					}
					finalMap.add(map2);
				}
				if(conn!=null)
					conn.close();
				if(stmtDv!=null)
					stmtDv.close();
				if(resultDv!=null)
					resultDv.close();
				return finalMap;

					}






			@PostMapping("/getSummaryInfoForAccountingV2")
			@Timed 
			public LinkedHashMap getSummaryInfoForAccountingV2(HttpServletRequest request,@RequestParam String processId ,@RequestBody HashMap dates,@RequestParam int violation) throws SQLException, ParseException
			{
				log.info("getSummaryInfoForAccountingV2 of processId:"+processId);
				LinkedHashMap finalMap=new LinkedHashMap();
				List<LinkedHashMap> dataMap=new ArrayList<LinkedHashMap>();
		    	HashMap map1=userJdbcService.getuserInfoFromToken(request);
		      	Long tenantId=Long.parseLong(map1.get("tenantId").toString());
		      	Processes processes = processesRepository.findByTenantIdAndIdForDisplay(tenantId, processId);
				ProcessDetails procesDet=processDetailsRepository.findByProcessIdAndTagType(processes.getId(), "accountingRuleGroup");
				log.info("procesDet :"+procesDet);
				if(procesDet!=null)
				{
					ZonedDateTime fmDate=ZonedDateTime.parse(dates.get("startDate").toString());
					ZonedDateTime toDate=ZonedDateTime.parse(dates.get("endDate").toString());
					log.info("fmDate :"+fmDate);
					log.info("toDate :"+toDate);
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
					Statement stmtApp=null;


					conn = DriverManager.getConnection(dbUrl, userName, password);
					log.info("Connected database successfully...");
					stmtDv = conn.createStatement();
					stmtApp = conn.createStatement();

					ResultSet resultDv=null;
					ResultSet resultApp=null;










					List<BigInteger> distViewsIds=appModuleSummaryRepository.findDistinctViewIdByRuleGroupId(procesDet.getTypeId(),fDate,tDate);
					log.info("distViewsIds :"+distViewsIds);
					Long violationCount=0l;
					Long totalViolationCount=0l;
					Long totalUnapprovedCount=0l;
					Long totalapprovedCount=0l;
					for(BigInteger viewId:distViewsIds)
					{

						LinkedHashMap map=new LinkedHashMap();
						map.put("viewId", viewId);
						List<Object[]> accountingSummary=appModuleSummaryRepository.fetchActCountsByGroupIdAndViewIdAndFileDate(Long.valueOf(procesDet.getTypeId()),viewId.longValue(),fDate,tDate);
						DataViews dv=dataViewsRepository.findOne(viewId.longValue());
						map.put("viewName", dv.getDataViewName());
						List<LinkedHashMap>  dvRuleDetailList=new ArrayList<LinkedHashMap>();

						List<BigInteger> ruleDetailsForView=appModuleSummaryRepository.findRuleIdByRuleGroupIdAndViewId(procesDet.getTypeId(),viewId.longValue(),fDate,tDate);
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
							log.info("dvCount :"+accountingSummary.get(0)[0].toString());
							log.info("dvAmt :"+accountingSummary.get(0)[8].toString());
							log.info("dvAmt at "+i+" is :"+dvAmt);
							log.info("dvCount at "+i+" is :"+dvCount);
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

							map.put(process.replaceAll("\\s", "")+"per", Double.valueOf(accountingSummary.get(i)[2].toString()));
							map.put(process.replaceAll("\\s", "")+"amt", Double.valueOf(accountingSummary.get(i)[9].toString()));
							log.info("total dv amount :"+accountingSummary.get(i)[8].toString());
							Double amtPer=(Double.valueOf(accountingSummary.get(i)[9].toString())/Double.valueOf(accountingSummary.get(i)[8].toString()))*100;


							map.put(process.replaceAll("\\s", "")+"amtPer", Double.valueOf(dform.format(amtPer)));

							log.info("map.get(notAccountedCount) :"+map.get("notAccountedCount"));
							log.info("map.get(accountingInprocessCount) :"+map.get("accountingInprocessCount"));

							if(process.equalsIgnoreCase("Not Accounted"))
							{

								Double notAcctAndAcctInProcCt=0d;
								Double notAcctAndAcctInProcAmt=0d;

								Double notAcctAndAcctInProcCtPer=0d;
								Double notAcctAndAcctInProcAmtPer=0d;

								if(map.get("notAccountedCount")!=null)
								{
									if(map.get("accountingInprocessCount")!=null)
										notAcctAndAcctInProcCt=Double.valueOf(dform.format( Double.valueOf(map.get("notAccountedCount").toString())+Double.valueOf(map.get("accountingInprocessCount").toString())));
									else
										notAcctAndAcctInProcCt= Double.valueOf(dform.format(Double.valueOf(map.get("notAccountedCount").toString())));
								}

								if(map.get("notAccountedamt")!=null)
								{
									if(map.get("accountingInprocessamt")!=null)
										notAcctAndAcctInProcAmt=Double.valueOf(dform.format( Double.valueOf(map.get("notAccountedamt").toString())+Double.valueOf(map.get("accountingInprocessamt").toString())));
									else
										notAcctAndAcctInProcAmt= Double.valueOf(dform.format(Double.valueOf(map.get("notAccountedamt").toString())));
								}


								if(map.get("notAccountedper")!=null)
								{
									if(map.get("accountingInprocessper")!=null)
										notAcctAndAcctInProcCtPer=Double.valueOf(dform.format( Double.valueOf(map.get("notAccountedper").toString())+Double.valueOf(map.get("accountingInprocessper").toString())));
									else
										notAcctAndAcctInProcCtPer= Double.valueOf(dform.format(Double.valueOf(map.get("notAccountedper").toString())));
								}

								if(map.get("notAccountedamtPer")!=null)
								{
									if(map.get("accountingInprocessamtPer")!=null)
										notAcctAndAcctInProcAmtPer=Double.valueOf(dform.format( Double.valueOf(map.get("notAccountedamtPer").toString())+Double.valueOf(map.get("accountingInprocessamtPer").toString())));
									else
										notAcctAndAcctInProcAmtPer= Double.valueOf(dform.format(Double.valueOf(map.get("notAccountedamtPer").toString())));
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
							}

							String query="";
							if(process .equalsIgnoreCase("Not accounted"))
							{
								totalNotActAmt=totalNotActAmt+Double.valueOf(accountingSummary.get(i)[9].toString());
								totalNotActCt=totalNotActCt+Double.valueOf(accountingSummary.get(i)[1].toString());
								List<BigInteger> finalSrcIdList=new ArrayList<BigInteger>();
								//only accounted
								List<BigInteger> accountedViewIds=accountedSummaryRepository.fetchAccountedRowIdsByRuleGrpIdAndViewId(procesDet.getTypeId(), viewId.longValue()) ;
								finalSrcIdList.addAll(accountedViewIds);

								if(finalSrcIdList!=null && !finalSrcIdList.isEmpty())
								{
									String finalSrcIds=finalSrcIdList.toString().replaceAll("\\[", "").replaceAll("\\]", "");

									query="select DATEDIFF( SYSDATE(), `v`.`fileDate`) as `rule_age`,count(scrIds),sum(`"+ammountQualifier+"`) as `"+ammountQualifier+"` from "+schemaName+".`"+dv.getDataViewName().toLowerCase().toLowerCase()+"` v where fileDate between '"+fDate+"' and '"+tDate+"' "
											+ " and scrIds not in ("+finalSrcIds+")group by rule_age";

								}
								else
								{
									query="select DATEDIFF( SYSDATE(), `v`.`fileDate`) as `rule_age`,count(scrIds),sum(`"+ammountQualifier+"`) as `"+ammountQualifier+"` from "+schemaName+".`"+dv.getDataViewName().toLowerCase().toLowerCase()+"` v where fileDate between '"+fDate+"' and '"+tDate+"' "
											+ "group by rule_age";
								}

								//log.info("query :"+query);
								if(finalSrcIdList.size()>0)
								{
									resultDv=stmtDv.executeQuery(query);
									ResultSetMetaData rsmd2 = resultDv.getMetaData();
									int columnCount = rsmd2.getColumnCount();
									map.put("violationAmount",0);
									map.put("violationCount", 0);
									log.info("violation :"+violation);

									while(resultDv.next())
									{
										//log.info("resultDv.getString(rule_age) :"+resultDv.getString("rule_age"));
										int ruleAge=Integer.valueOf(resultDv.getString("rule_age").toString());

										// int ruleAge=1;
										totalViolationCount=totalViolationCount+Long.valueOf(resultDv.getString("count(scrIds)").toString());
										log.info("ruleAge"+ruleAge);
										if(ruleAge>violation)
										{
											log.info("if rule age is greater than violation");
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
							if(process .equalsIgnoreCase("accounted"))
							{
								log.info("accountingSummary.get(i)[9] :"+accountingSummary.get(i)[9]);
								totalActAmt=totalActAmt+Double.valueOf(accountingSummary.get(i)[9].toString());
								totalActct=totalActct+Double.valueOf(accountingSummary.get(i)[1].toString());
								map.put("unApprovedCount", Double.valueOf(dform.format(Double.valueOf(accountingSummary.get(i)[6].toString()))));
								totalUnapprovedCount=totalUnapprovedCount+Long.valueOf(accountingSummary.get(i)[6].toString());
								List<BigInteger> finalApprovedSrcIds=accountedSummaryRepository.fetchDistinctApprovedRowIds(procesDet.getTypeId(), viewId.longValue()) ;
								finalApprovedSrcIds.addAll(finalApprovedSrcIds);
								totalapprovedCount=totalapprovedCount+Long.valueOf(accountingSummary.get(i)[7].toString());

								if(finalApprovedSrcIds!=null && !finalApprovedSrcIds.isEmpty())
								{
									String finalSrcIds=finalApprovedSrcIds.toString().replaceAll("\\[", "").replaceAll("\\]", "");

									query="select case when sum(`"+ammountQualifier+"`) is null then 0 else sum(`"+ammountQualifier+"`) end as `"+ammountQualifier+"` from "+schemaName+".`"+dv.getDataViewName().toLowerCase().toLowerCase()+"` v where fileDate between '"+fDate+"' and '"+tDate+"' "
											+ " and scrIds not in ("+finalSrcIds+")";

								}
								else
								{
									query="select case when sum(`"+ammountQualifier+"`) is null then 0 else sum(`"+ammountQualifier+"`) end as `"+ammountQualifier+"` from "+schemaName+".`"+dv.getDataViewName().toLowerCase().toLowerCase()+"` v where fileDate between '"+fDate+"' and '"+tDate+"'";


								}
								//log.info("query for unapproved amt :"+query);
								resultApp=stmtApp.executeQuery(query);



								while(resultApp.next())
								{
									map.put("unApprovedAmount", Double.valueOf(dform.format(Double.valueOf(resultApp.getString(ammountQualifier).toString()))));
								}
							}
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

						dataMap.add(map);

					}
					log.info("totalapprovedCount :"+totalapprovedCount);
					log.info("totalUnapprovedCount :"+totalUnapprovedCount);
					double violationCountPer=0;
					if(totalViolationCount>0)
					{
						violationCountPer =((double)violationCount/totalViolationCount)*100;
					}
					double totalUnapprovedCountPer=0;
					if(totalapprovedCount>0)
					{
						totalUnapprovedCountPer=((double)totalUnapprovedCount/totalapprovedCount)*100;
					}
					log.info("totalUnapprovedCountPer :"+totalUnapprovedCountPer);
					Double totalNotActAmtPer=0d;
					if(totalActAmt>0)
					{
						log.info("dvAmt in if : "+dvAmt);
						totalNotActAmtPer=Double.valueOf(dform.format(((totalNotActAmt+totalActInProcAmt)/dvAmt)*100));
					}
					log.info("rulesList :"+rulesList);
					finalMap.put("rulesList", rulesList);
					finalMap.put("rulesIdList", rulesIdList);
					finalMap.put("unAccountedItemsValue", totalNotActAmt+totalActInProcAmt);
					finalMap.put("unAccountedItemsValuePer", totalNotActAmtPer);
					finalMap.put("unAccountedItemsViolation",violationCount);
					finalMap.put("unAccountedItemsViolationPer",Double.valueOf(dform.format(violationCountPer)));
					finalMap.put("awaitingAppCount", totalUnapprovedCount);
					finalMap.put("awaitingAppCountPer", totalUnapprovedCountPer);
					finalMap.put("finalUnpostedCt", dvCount-totalFinalAccountedCt);
					finalMap.put("accountedCt", totalActct);
					if(totalActct>0)
					{
						Double totalActCtPer=Double.valueOf(dform.format((totalActct/dvCount)*100));
						finalMap.put("accountedCtPer", totalActCtPer);
					}
					else
						finalMap.put("accountedCtPer", 0);
					finalMap.put("accountedAmt", Double.valueOf(dform.format(totalActAmt)));
					if(totalActAmt>0)
					{
						Double totalActAmtPer=Double.valueOf(dform.format((totalActAmt/dvAmt)*100));
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

					if(resultDv!=null)
						resultDv.close();
					if(resultApp!=null)
						resultApp.close();
					if(stmtDv!=null)
						stmtDv.close();
					if(stmtApp!=null)
						stmtApp.close();
					if(conn!=null)
						conn.close();
				}
				

				return finalMap;
			}



			@GetMapping("/getLovGroupBy")
			@Timed 
			public LinkedHashMap getLovGroupBy(@RequestParam Long viewId)
			{

				LinkedHashMap finalMap=new LinkedHashMap();
				finalMap=dashBoardV4Service.getDVGroupByColumns(viewId,false);
				
				return finalMap;


			}





			@PostMapping("/getUnProcessedOrProcessedData")
			@Timed 
			public List<LinkedHashMap> getUnProcessedData(@RequestParam String processId,@RequestParam Long viewId,@RequestParam String module,@RequestParam String amtQuailifier
					,@RequestParam List<String> groupByColmns,@RequestBody HashMap dates,HttpServletRequest request) throws SQLException, ParseException
					{
				HashMap map=userJdbcService.getuserInfoFromToken(request);
				Long tenantId=Long.parseLong(map.get("tenantId").toString());
				Processes processes = processesRepository.findByTenantIdAndIdForDisplay(tenantId, processId);
				ProcessDetails procesRecDet=processDetailsRepository.findByProcessIdAndTagType(processes.getId(), "reconciliationRuleGroup");
				ProcessDetails procesActDet=processDetailsRepository.findByProcessIdAndTagType(processes.getId(), "accountingRuleGroup");
				ZonedDateTime fmDate=ZonedDateTime.parse(dates.get("startDate").toString());
				ZonedDateTime toDate=ZonedDateTime.parse(dates.get("endDate").toString());
				log.info("fmDate :"+fmDate);
				log.info("toDate :"+toDate);
				java.time.LocalDate fDate=fmDate.toLocalDate();
				java.time.LocalDate tDate=toDate.toLocalDate();

				List<BigInteger> finalSrcIdList=new ArrayList<BigInteger>();
				if(module.equalsIgnoreCase("reconciled") || module.equalsIgnoreCase("un-reconciled"))
				{
					log.info(" in if");
					List<BigInteger> reconciliedSrcIds=reconciliationResultRepository.fetchReconciledSourceIds(tenantId, procesRecDet.getTypeId(), viewId);	 
					finalSrcIdList.addAll(reconciliedSrcIds);
					List<BigInteger> reconciliedTrgIds=reconciliationResultRepository.fetchReconciledTargetIds(tenantId, procesRecDet.getTypeId(), viewId);	 
					finalSrcIdList.addAll(reconciliedTrgIds);
				}
				else if(module.equalsIgnoreCase("accounted") )
				{
					log.info(" in else accounted");
					//where status = accounted and journal not entered
					List<BigInteger> accountedViewIds=accountedSummaryRepository.fetchUnPostedRowIdsByRuleGrpIdAndViewId(procesActDet.getTypeId(), viewId) ;
					finalSrcIdList.addAll(accountedViewIds);
				}
				else if(module.equalsIgnoreCase("accounting in process")||  module.equalsIgnoreCase("not accounted"))
				{
					log.info(" in else not accounted");
					List<BigInteger> accountedViewIds=accountedSummaryRepository.fetchUnPostedRowIdsByRuleGrpIdAndViewId(procesActDet.getTypeId(), viewId) ;
					finalSrcIdList.addAll(accountedViewIds);
				}
				else if(module.equalsIgnoreCase("JE Creation"))
				{
					log.info(" in else JE Creation");
					List<BigInteger> accountedViewIds=accountedSummaryRepository.fetchPostedRowIdsByRuleGrpIdAndViewId(procesActDet.getTypeId(), viewId) ;
					finalSrcIdList.addAll(accountedViewIds);
				}
				log.info("finalSrcIdList :"+finalSrcIdList.size());
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
				ResultSet resultAct=null;
				DataViews dvName=dataViewsRepository.findOne(viewId);

				String groupBy=groupByColmns.toString().replaceAll("\\[", "").replaceAll("\\]", "");
				String finalSrcIds=finalSrcIdList.toString().replaceAll("\\[", "").replaceAll("\\]", "");
				log.info("groupBy :"+groupBy);
				String query="";
				List<LinkedHashMap> finalMap=new ArrayList<LinkedHashMap>();

				if(module.equalsIgnoreCase("un-reconciled") || module.equalsIgnoreCase("not accounted"))
				{
					if(finalSrcIds!=null && !finalSrcIds.isEmpty())
					{
						query="select `"+groupBy+"`,round(sum(`"+amtQuailifier+"`),"+round+") as `"+amtQuailifier+"` from "+schemaName+".`"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where Date(fileDate) >= '"+fDate+"' and Date(fileDate) <='"+tDate+"' "
								+ " and scrIds not in ("+finalSrcIds+")group by `"+groupBy+"` order by `"+amtQuailifier+"` desc limit 10";
						//log.info("query :"+query);
					}
					
					else if(finalSrcIds.isEmpty() && module.equalsIgnoreCase("un-reconciled"))
					{
						query="select `"+groupBy+"`,round(sum(`"+amtQuailifier+"`),"+round+") as `"+amtQuailifier+"` from "+schemaName+".`"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where Date(fileDate) >= '"+fDate+"' and Date(fileDate) <='"+tDate+"' "
								+ " group by `"+groupBy+"` order by `"+amtQuailifier+"` desc limit 10";
						//log.info("query :"+query);
					}

				}
				if(module.equalsIgnoreCase("reconciled") || module.equalsIgnoreCase("accounted"))
				{
					query="select `"+groupBy+"`,round(sum(`"+amtQuailifier+"`),"+round+") as `"+amtQuailifier+"` from "+schemaName+".`"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where Date(fileDate) >= '"+fDate+"' and Date(fileDate) <='"+tDate+"' "
							+ " and scrIds in ("+finalSrcIds+")group by `"+groupBy+"` order by `"+amtQuailifier+"` desc limit 10";
					//log.info("query :"+query);
				}
				if(module.equalsIgnoreCase("JE Creation"))
				{
					query="select `"+groupBy+"`,round(sum(`"+amtQuailifier+"`),"+round+") as `"+amtQuailifier+"` from "+schemaName+".`"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where Date(fileDate) >= '"+fDate+"' and Date(fileDate) <='"+tDate+"' "
							+ " and scrIds in ("+finalSrcIds+")group by `"+groupBy+"` order by `"+amtQuailifier+"` desc limit 10";
					//log.info("query :"+query);
				}


				if(!query.isEmpty())
				{
					resultDv=stmtDv.executeQuery(query);
					ResultSetMetaData rsmd2 = resultDv.getMetaData();
					int columnCount = rsmd2.getColumnCount();

					while(resultDv.next())
					{
						LinkedHashMap map2=new LinkedHashMap();
						for (int i = 1; i <= columnCount; i++ ) {
							String name = rsmd2.getColumnName(i); 
							if(i==1)
								map2.put("name", resultDv.getString(i));
							if(i==2)
								map2.put("value", Double.valueOf(resultDv.getString(i).toString()));
						}
						finalMap.add(map2);
					}
				}
				if(conn!=null)
					conn.close();
				if(stmtDv!=null)
					stmtDv.close();
				if(resultDv!=null)
					resultDv.close();

				return finalMap;

					}


			@PostMapping("/transformationAnalysisforGivenPeriod")
			@Timed
			public LinkedHashMap transformationAnalysisforGivenPeriod(@RequestParam String processId,@RequestBody HashMap dates,HttpServletRequest request) throws SQLException, ParseException 
			{
				log.info("Rest Request to get aging analysis :"+dates);

				HashMap map=userJdbcService.getuserInfoFromToken(request);
				Long tenantId=Long.parseLong(map.get("tenantId").toString());

				Processes processes = processesRepository.findByTenantIdAndIdForDisplay(tenantId, processId);
				LinkedHashMap eachMap=new LinkedHashMap();

				if(processId!=null)

				{
					eachMap=dashBoardV2Service.transformationAnalysisforGivenPeriod(processes.getId(), dates, request);
				}

				return eachMap;



			}


			@PostMapping("/extractionAnalysisforGivenPeriod")
			@Timed
			public LinkedHashMap extractionAnalysisforGivenPeriod(@RequestParam String processId,@RequestBody HashMap dates,HttpServletRequest request) throws SQLException, ParseException
			{
				HashMap map=userJdbcService.getuserInfoFromToken(request);
				Long tenantId=Long.parseLong(map.get("tenantId").toString());
				Processes processes = processesRepository.findByTenantIdAndIdForDisplay(tenantId, processId);
				LinkedHashMap eachMap=new LinkedHashMap();
				List<BigInteger>  profileId=processDetailsRepository.findTypeIdByProcessIdAndTagType(processes.getId(), "sourceProfile");

				ZonedDateTime fmDate=ZonedDateTime.parse(dates.get("startDate").toString());
				ZonedDateTime toDate=ZonedDateTime.parse(dates.get("endDate").toString());
				log.info("fmDate :"+fmDate);
				log.info("toDate :"+toDate);
				java.time.LocalDate fDate=fmDate.toLocalDate();
				java.time.LocalDate tDate=toDate.toLocalDate();

				ApplicationPrograms app=applicationProgramsRepository.findByPrgmNameAndTenantId("DataExtraction", tenantId);

				/* Double finalCount=0d;
         	 Double extractedCount=0d;*/

				LocalDate from1wDate=tDate.minusDays(7);

				LocalDate till1wDate=tDate;
				LocalDate till1wDateFrLoop=tDate;
				log.info("tillDate :"+till1wDate);

				List<BigInteger> jobDetails=jobDetailsRepository.findByTenantIdAndProgrammIdAndParameterArgument1(app.getId(),profileId,fDate+"%",tDate+"%");
				log.info("jobDetails :"+jobDetails);
				log.info("from1wDate :"+from1wDate);
				log.info("fDate :"+fDate);

				List<LocalDate> datesList=new ArrayList<LocalDate>();

				/* Boolean oneWeek=false;
			 Boolean twoWeek=false;
			 Boolean oneMonth=false;
			 Boolean threeMonth=false;*/

				Long days=Duration.between(fDate.atStartOfDay(), tDate.atStartOfDay()).toDays();
				if(days<=7 || days>=7)
				{
					log.info("from1wDate :"+from1wDate);
					log.info("till1wDate :"+till1wDate);
					LinkedHashMap oneWeekMap=new LinkedHashMap();
					if(days<7)
						oneWeekMap=dashBoardV2Service.extractionAnalysisforGivenPeriod(jobDetails, fDate.minusDays(1), tDate, toDate);
					else
						oneWeekMap=dashBoardV2Service.extractionAnalysisforGivenPeriod(jobDetails, from1wDate, till1wDate, toDate);
					 
					eachMap.put("1W", oneWeekMap);
				}

				log.info("eachMap after oneWeekMap :"+eachMap);


				/** for 2nd week**/

				if(days>=7)
				{
					LocalDate from2WDate=tDate.minusDays(14);
					log.info("from2WDate :"+from2WDate);
					LocalDate till2WDate=tDate;
					log.info("till2WDate :"+till2WDate);
					LinkedHashMap twoWeekMap=new LinkedHashMap();
					if(days>=7 && days<14)
						twoWeekMap=dashBoardV2Service.extractionAnalysisforGivenPeriod(jobDetails, fDate.minusDays(1), tDate, toDate);
					else
					   twoWeekMap=dashBoardV2Service.extractionAnalysisforGivenPeriod(jobDetails, from2WDate, till2WDate, toDate);
					eachMap.put("2W", twoWeekMap);
				}
				/** for 1month**/
				LocalDate from6Date = null;
				LocalDate till6Date = null;

				if(days>=14)
				{
					List<String> labelValue=new ArrayList<String>();

					List<String> labelDates=new ArrayList<String>();
					List<LinkedHashMap> labelDatesValue=new ArrayList<LinkedHashMap>();

					List<Double> extracted=new ArrayList<Double>();

					List<Double> extractedFailed=new ArrayList<Double>();
					Double totalExtractedCount=0d;
					Double totalExtractionFailedCt=0d;
					
					Double totalExtractedCountPer=0d;
					Double totalExtractionFailedCtPer=0d;


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


					LinkedHashMap oneWeekMap=dashBoardV2Service.extractionAnalysisforGivenPeriod(jobDetails, from1wDate, till1wDate, toDate);
					labelDatesValue.add(dashBoardV2Service.datesMap(from1wDate.toString(),till1wDate.toString()));

					// labelDatesValue.add(from1wDate+"-"+till1wDate);
					labelValue.add("Week-1");
					extracted.add(Double.valueOf(oneWeekMap.get("totalExtracted").toString()));
					extractedFailed.add(Double.valueOf(oneWeekMap.get("totalExtractionFailedCt").toString()));
					totalExtractedCount=totalExtractedCount+Double.valueOf(oneWeekMap.get("totalExtracted").toString());
					totalExtractionFailedCt=totalExtractionFailedCt+Double.valueOf(oneWeekMap.get("totalExtractionFailedCt").toString());
					totalExtractedCountPer=totalExtractedCountPer+Double.valueOf(oneWeekMap.get("totalExtractedPer").toString());
					totalExtractionFailedCtPer=totalExtractionFailedCtPer+Double.valueOf(oneWeekMap.get("totalExtractionFailedCtPer").toString());

					LinkedHashMap twoWeekMap=dashBoardV2Service.extractionAnalysisforGivenPeriod(jobDetails, from2wDate, till2wDate, toDate);
					labelDatesValue.add(dashBoardV2Service.datesMap(from2wDate.toString(),till2wDate.toString()));

					// labelDatesValue.add(from2wDate+"-"+till2wDate);
					labelValue.add("Week-2");
					extracted.add(Double.valueOf(twoWeekMap.get("totalExtracted").toString()));
					extractedFailed.add(Double.valueOf(twoWeekMap.get("totalExtractionFailedCt").toString()));
					totalExtractedCount=totalExtractedCount+Double.valueOf(twoWeekMap.get("totalExtracted").toString());
					totalExtractionFailedCt=totalExtractionFailedCt+Double.valueOf(twoWeekMap.get("totalExtractionFailedCt").toString());
					totalExtractedCountPer=totalExtractedCountPer+Double.valueOf(twoWeekMap.get("totalExtractedPer").toString());
					totalExtractionFailedCtPer=totalExtractionFailedCtPer+Double.valueOf(twoWeekMap.get("totalExtractionFailedCtPer").toString());


					if(from3wDate.isAfter(fDate) || from3wDate.equals(fDate))
					{
						LinkedHashMap threeWeek=dashBoardV2Service.extractionAnalysisforGivenPeriod(jobDetails, from3wDate, till3wDate, toDate);
						labelDatesValue.add(dashBoardV2Service.datesMap(from3wDate.toString(),till3wDate.toString()));

						// labelDatesValue.add(from3wDate+"-"+till3wDate);
						labelValue.add("Week-3");
						extracted.add(Double.valueOf(threeWeek.get("totalExtracted").toString()));
						extractedFailed.add(Double.valueOf(threeWeek.get("totalExtractionFailedCt").toString()));
						totalExtractedCount=totalExtractedCount+Double.valueOf(threeWeek.get("totalExtracted").toString());
						totalExtractionFailedCt=totalExtractionFailedCt+Double.valueOf(threeWeek.get("totalExtractionFailedCt").toString());
						totalExtractedCountPer=totalExtractedCountPer+Double.valueOf(threeWeek.get("totalExtractedPer").toString());
						totalExtractionFailedCtPer=totalExtractionFailedCtPer+Double.valueOf(threeWeek.get("totalExtractionFailedCtPer").toString());
					}

					if(from4wDate.isAfter(fDate) || from4wDate.equals(fDate))
					{
						LinkedHashMap fourWeekMap=dashBoardV2Service.extractionAnalysisforGivenPeriod(jobDetails, from4wDate,till4wDate, toDate);
						labelDatesValue.add(dashBoardV2Service.datesMap(from4wDate.toString(),till4wDate.toString()));

						// labelDatesValue.add(from4wDate+"-"+till4wDate);
						labelValue.add("Week-4");
						extracted.add(Double.valueOf(fourWeekMap.get("totalExtracted").toString()));
						extractedFailed.add(Double.valueOf(fourWeekMap.get("totalExtractionFailedCt").toString()));
						totalExtractedCount=totalExtractedCount+Double.valueOf(fourWeekMap.get("totalExtracted").toString());
						totalExtractionFailedCt=totalExtractionFailedCt+Double.valueOf(fourWeekMap.get("totalExtractionFailedCt").toString());
						totalExtractedCountPer=totalExtractedCountPer+Double.valueOf(fourWeekMap.get("totalExtractedPer").toString());
						totalExtractionFailedCtPer=totalExtractionFailedCtPer+Double.valueOf(fourWeekMap.get("totalExtractionFailedCtPer").toString());
					}

					if(from5wDate.isAfter(fDate) || from5wDate.equals(fDate))
					{
						LinkedHashMap fiveWeekMap=dashBoardV2Service.extractionAnalysisforGivenPeriod(jobDetails, from5wDate,till5wDate, toDate);
						labelDatesValue.add(dashBoardV2Service.datesMap(from5wDate.toString(),till5wDate.toString()));

						// labelDatesValue.add(from5wDate+"-"+till5wDate);
						labelValue.add("Week-5");
						extracted.add(Double.valueOf(fiveWeekMap.get("totalExtracted").toString()));
						extractedFailed.add(Double.valueOf(fiveWeekMap.get("totalExtractionFailedCt").toString()));
						totalExtractedCount=totalExtractedCount+Double.valueOf(fiveWeekMap.get("totalExtracted").toString());
						totalExtractionFailedCt=totalExtractionFailedCt+Double.valueOf(fiveWeekMap.get("totalExtractionFailedCt").toString());
						totalExtractedCountPer=totalExtractedCountPer+Double.valueOf(fiveWeekMap.get("totalExtractedPer").toString());
						totalExtractionFailedCtPer=totalExtractionFailedCtPer+Double.valueOf(fiveWeekMap.get("totalExtractionFailedCtPer").toString());
					}
					LinkedHashMap OMDet=new LinkedHashMap();

					OMDet.put("labelValue", labelValue);
					OMDet.put("labelDateValue", labelDatesValue);
					OMDet.put("extracted", extracted);

					OMDet.put("extractionFailed", extractedFailed);

					LinkedHashMap oneMonthMap=new LinkedHashMap();
					
				
			    	 
					
					oneMonthMap.put("totalExtracted", totalExtractedCount);

					oneMonthMap.put("totalExtractionFailedCt", totalExtractionFailedCt);
					 oneMonthMap.put("totalExtractedPer",Double.valueOf(dform.format(totalExtractedCountPer)));
					 oneMonthMap.put("totalExtractionFailedCtPer",Double.valueOf(dform.format(totalExtractionFailedCtPer)));

					oneMonthMap.put("detailedData", OMDet);
					eachMap.put("1M", oneMonthMap);
				}
				if(days>=30)
				{

					List<String> labelValue=new ArrayList<String>();

					List<String> labelDates=new ArrayList<String>();
					List<LinkedHashMap> labelDatesValue=new ArrayList<LinkedHashMap>();

					List<Double> extracted=new ArrayList<Double>();

					List<Double> extractedFailed=new ArrayList<Double>();
					Double totalExtractedCount=0d;
					Double totalExtractionFailedCt=0d;
					
					
					Double totalExtractedCountPer=0d;
					Double totalExtractionFailedCtPer=0d;

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


					LinkedHashMap oneWeekMap=dashBoardV2Service.extractionAnalysisforGivenPeriod(jobDetails, from1wDate, till1wDate, toDate);
					labelDatesValue.add(dashBoardV2Service.datesMap(from1wDate.toString(),till1wDate.toString()));

					//	 labelDatesValue.add(from1wDate+"-"+till1wDate);
					labelValue.add("Week-1");
					extracted.add(Double.valueOf(oneWeekMap.get("totalExtracted").toString()));
					extractedFailed.add(Double.valueOf(oneWeekMap.get("totalExtractionFailedCt").toString()));
					totalExtractedCount=totalExtractedCount+Double.valueOf(oneWeekMap.get("totalExtracted").toString());
					totalExtractionFailedCt=totalExtractionFailedCt+Double.valueOf(oneWeekMap.get("totalExtractionFailedCt").toString());
					totalExtractedCountPer=totalExtractedCountPer+Double.valueOf(oneWeekMap.get("totalExtractedPer").toString());
					totalExtractionFailedCtPer=totalExtractionFailedCtPer+Double.valueOf(oneWeekMap.get("totalExtractionFailedCtPer").toString());

					LinkedHashMap twoWeekMap=dashBoardV2Service.extractionAnalysisforGivenPeriod(jobDetails, from2wDate, till2wDate, toDate);
					labelDatesValue.add(dashBoardV2Service.datesMap(from2wDate.toString(),till2wDate.toString()));

					//	 labelDatesValue.add(from2wDate+"-"+till2wDate);
					labelValue.add("Week-2");
					extracted.add(Double.valueOf(twoWeekMap.get("totalExtracted").toString()));
					extractedFailed.add(Double.valueOf(twoWeekMap.get("totalExtractionFailedCt").toString()));
					totalExtractedCount=totalExtractedCount+Double.valueOf(twoWeekMap.get("totalExtracted").toString());
					totalExtractionFailedCt=totalExtractionFailedCt+Double.valueOf(twoWeekMap.get("totalExtractionFailedCt").toString());
					totalExtractedCountPer=totalExtractedCountPer+Double.valueOf(twoWeekMap.get("totalExtractedPer").toString());
					totalExtractionFailedCtPer=totalExtractionFailedCtPer+Double.valueOf(twoWeekMap.get("totalExtractionFailedCtPer").toString());




					LinkedHashMap threeWeek=dashBoardV2Service.extractionAnalysisforGivenPeriod(jobDetails, from3wDate, till3wDate, toDate);
					labelDatesValue.add(dashBoardV2Service.datesMap(from3wDate.toString(),till3wDate.toString()));

					// labelDatesValue.add(from3wDate+"-"+till3wDate);
					labelValue.add("Week-3");
					extracted.add(Double.valueOf(threeWeek.get("totalExtracted").toString()));
					extractedFailed.add(Double.valueOf(threeWeek.get("totalExtractionFailedCt").toString()));
					totalExtractedCount=totalExtractedCount+Double.valueOf(threeWeek.get("totalExtracted").toString());
					totalExtractionFailedCt=totalExtractionFailedCt+Double.valueOf(threeWeek.get("totalExtractionFailedCt").toString());
					totalExtractedCountPer=totalExtractedCountPer+Double.valueOf(threeWeek.get("totalExtractedPer").toString());
					totalExtractionFailedCtPer=totalExtractionFailedCtPer+Double.valueOf(threeWeek.get("totalExtractionFailedCtPer").toString());

					LinkedHashMap fourWeekMap=dashBoardV2Service.extractionAnalysisforGivenPeriod(jobDetails, from4wDate,till4wDate, toDate);
					labelDatesValue.add(dashBoardV2Service.datesMap(from4wDate.toString(),till4wDate.toString()));

					// labelDatesValue.add(from4wDate+"-"+till4wDate);
					labelValue.add("Week-4");
					extracted.add(Double.valueOf(fourWeekMap.get("totalExtracted").toString()));
					extractedFailed.add(Double.valueOf(fourWeekMap.get("totalExtractionFailedCt").toString()));
					totalExtractedCount=totalExtractedCount+Double.valueOf(fourWeekMap.get("totalExtracted").toString());
					totalExtractionFailedCt=totalExtractionFailedCt+Double.valueOf(fourWeekMap.get("totalExtractionFailedCt").toString());
					totalExtractedCountPer=totalExtractedCountPer+Double.valueOf(fourWeekMap.get("totalExtractedPer").toString());
					totalExtractionFailedCtPer=totalExtractionFailedCtPer+Double.valueOf(fourWeekMap.get("totalExtractionFailedCtPer").toString());

					LinkedHashMap fiveWeekMap=dashBoardV2Service.extractionAnalysisforGivenPeriod(jobDetails, from5wDate,till5wDate, toDate);
					labelDatesValue.add(dashBoardV2Service.datesMap(from5wDate.toString(),till5wDate.toString()));

					//	 labelDatesValue.add(from5wDate+"-"+till5wDate);
					labelValue.add("Week-5");
					extracted.add(Double.valueOf(fiveWeekMap.get("totalExtracted").toString()));
					extractedFailed.add(Double.valueOf(fiveWeekMap.get("totalExtractionFailedCt").toString()));
					totalExtractedCount=totalExtractedCount+Double.valueOf(fiveWeekMap.get("totalExtracted").toString());
					totalExtractionFailedCt=totalExtractionFailedCt+Double.valueOf(fiveWeekMap.get("totalExtractionFailedCt").toString());
					totalExtractedCountPer=totalExtractedCountPer+Double.valueOf(fiveWeekMap.get("totalExtractedPer").toString());
					totalExtractionFailedCtPer=totalExtractionFailedCtPer+Double.valueOf(fiveWeekMap.get("totalExtractionFailedCtPer").toString());

					if(from6Date.isAfter(fDate) || from6Date.equals(fDate))
					{
						LinkedHashMap sixWeekMap=dashBoardV2Service.extractionAnalysisforGivenPeriod(jobDetails, from6Date,till6Date, toDate);
						labelDatesValue.add(dashBoardV2Service.datesMap(from6Date.toString(),till6Date.toString()));

						//	 labelDatesValue.add(from6Date+"-"+till6Date);
						labelValue.add("Week-6");
						extracted.add(Double.valueOf(sixWeekMap.get("totalExtracted").toString()));
						extractedFailed.add(Double.valueOf(sixWeekMap.get("totalExtractionFailedCt").toString()));
						totalExtractedCount=totalExtractedCount+Double.valueOf(sixWeekMap.get("totalExtracted").toString());
						totalExtractionFailedCt=totalExtractionFailedCt+Double.valueOf(sixWeekMap.get("totalExtractionFailedCt").toString());
						totalExtractedCountPer=totalExtractedCountPer+Double.valueOf(sixWeekMap.get("totalExtractedPer").toString());
						totalExtractionFailedCtPer=totalExtractionFailedCtPer+Double.valueOf(sixWeekMap.get("totalExtractionFailedCtPer").toString());

					}

					if(from7Date.isAfter(fDate) || from7Date.equals(fDate))
					{
						LinkedHashMap sixWeekMap=dashBoardV2Service.extractionAnalysisforGivenPeriod(jobDetails, from7Date,till7Date, toDate);
						labelDatesValue.add(dashBoardV2Service.datesMap(from7Date.toString(),till7Date.toString()));

						// labelDatesValue.add(from7Date+"-"+till7Date);
						labelValue.add("Week-7");
						extracted.add(Double.valueOf(sixWeekMap.get("totalExtracted").toString()));
						extractedFailed.add(Double.valueOf(sixWeekMap.get("totalExtractionFailedCt").toString()));
						totalExtractedCount=totalExtractedCount+Double.valueOf(sixWeekMap.get("totalExtracted").toString());
						totalExtractionFailedCt=totalExtractionFailedCt+Double.valueOf(sixWeekMap.get("totalExtractionFailedCt").toString());
						totalExtractedCountPer=totalExtractedCountPer+Double.valueOf(sixWeekMap.get("totalExtractedPer").toString());
						totalExtractionFailedCtPer=totalExtractionFailedCtPer+Double.valueOf(sixWeekMap.get("totalExtractionFailedCtPer").toString());
					}


					if(from8Date.isAfter(fDate) || from8Date.equals(fDate))
					{
						LinkedHashMap sixWeekMap=dashBoardV2Service.extractionAnalysisforGivenPeriod(jobDetails, from8Date,till8Date, toDate);
						labelDatesValue.add(dashBoardV2Service.datesMap(from8Date.toString(),till8Date.toString()));

						// labelDatesValue.add(from8Date+"-"+till8Date);
						labelValue.add("Week-8");
						extracted.add(Double.valueOf(sixWeekMap.get("totalExtracted").toString()));
						extractedFailed.add(Double.valueOf(sixWeekMap.get("totalExtractionFailedCt").toString()));
						totalExtractedCount=totalExtractedCount+Double.valueOf(sixWeekMap.get("totalExtracted").toString());
						totalExtractionFailedCt=totalExtractionFailedCt+Double.valueOf(sixWeekMap.get("totalExtractionFailedCt").toString());
						totalExtractedCountPer=totalExtractedCountPer+Double.valueOf(sixWeekMap.get("totalExtractedPer").toString());
						totalExtractionFailedCtPer=totalExtractionFailedCtPer+Double.valueOf(sixWeekMap.get("totalExtractionFailedCtPer").toString());
					}

					if(from9Date.isAfter(fDate) || from9Date.equals(fDate))
					{
						LinkedHashMap sixWeekMap=dashBoardV2Service.extractionAnalysisforGivenPeriod(jobDetails, from9Date,till9Date, toDate);
						labelDatesValue.add(dashBoardV2Service.datesMap(from9Date.toString(),till9Date.toString()));

						// labelDatesValue.add(from9Date+"-"+till9Date);
						labelValue.add("Week-9");
						extracted.add(Double.valueOf(sixWeekMap.get("totalExtracted").toString()));
						extractedFailed.add(Double.valueOf(sixWeekMap.get("totalExtractionFailedCt").toString()));
						totalExtractedCount=totalExtractedCount+Double.valueOf(sixWeekMap.get("totalExtracted").toString());
						totalExtractionFailedCt=totalExtractionFailedCt+Double.valueOf(sixWeekMap.get("totalExtractionFailedCt").toString());
						totalExtractedCountPer=totalExtractedCountPer+Double.valueOf(sixWeekMap.get("totalExtractedPer").toString());
						totalExtractionFailedCtPer=totalExtractionFailedCtPer+Double.valueOf(sixWeekMap.get("totalExtractionFailedCtPer").toString());
					}

					if(from10Date.isAfter(fDate) || from10Date.equals(fDate))
					{
						LinkedHashMap sixWeekMap=dashBoardV2Service.extractionAnalysisforGivenPeriod(jobDetails, from10Date,till10Date, toDate);
						labelDatesValue.add(dashBoardV2Service.datesMap(from10Date.toString(),till10Date.toString()));

						// labelDatesValue.add(from10Date+"-"+till10Date);
						labelValue.add("Week-10");
						extracted.add(Double.valueOf(sixWeekMap.get("totalExtracted").toString()));
						extractedFailed.add(Double.valueOf(sixWeekMap.get("totalExtractionFailedCt").toString()));
						totalExtractedCount=totalExtractedCount+Double.valueOf(sixWeekMap.get("totalExtracted").toString());
						totalExtractionFailedCt=totalExtractionFailedCt+Double.valueOf(sixWeekMap.get("totalExtractionFailedCt").toString());
						totalExtractedCountPer=totalExtractedCountPer+Double.valueOf(sixWeekMap.get("totalExtractedPer").toString());
						totalExtractionFailedCtPer=totalExtractionFailedCtPer+Double.valueOf(sixWeekMap.get("totalExtractionFailedCtPer").toString());
					}


					if(from11Date.isAfter(fDate) || from11Date.equals(fDate))
					{
						LinkedHashMap sixWeekMap=dashBoardV2Service.extractionAnalysisforGivenPeriod(jobDetails, from11Date,till11Date, toDate);
						labelDatesValue.add(dashBoardV2Service.datesMap(from11Date.toString(),till11Date.toString()));

						// labelDatesValue.add(from10Date+"-"+till10Date);
						labelValue.add("Week-11");
						extracted.add(Double.valueOf(sixWeekMap.get("totalExtracted").toString()));
						extractedFailed.add(Double.valueOf(sixWeekMap.get("totalExtractionFailedCt").toString()));
						totalExtractedCount=totalExtractedCount+Double.valueOf(sixWeekMap.get("totalExtracted").toString());
						totalExtractionFailedCt=totalExtractionFailedCt+Double.valueOf(sixWeekMap.get("totalExtractionFailedCt").toString());
						totalExtractedCountPer=totalExtractedCountPer+Double.valueOf(sixWeekMap.get("totalExtractedPer").toString());
						totalExtractionFailedCtPer=totalExtractionFailedCtPer+Double.valueOf(sixWeekMap.get("totalExtractionFailedCtPer").toString());
					}

					if(from12Date.isAfter(fDate) || from12Date.equals(fDate))
					{
						LinkedHashMap sixWeekMap=dashBoardV2Service.extractionAnalysisforGivenPeriod(jobDetails, from12Date,till12Date, toDate);
						labelDatesValue.add(dashBoardV2Service.datesMap(from12Date.toString(),till12Date.toString()));

						// labelDatesValue.add(from12Date+"-"+till12Date);
						labelValue.add("Week-12");
						extracted.add(Double.valueOf(sixWeekMap.get("totalExtracted").toString()));
						extractedFailed.add(Double.valueOf(sixWeekMap.get("totalExtractionFailedCt").toString()));
						totalExtractedCount=totalExtractedCount+Double.valueOf(sixWeekMap.get("totalExtracted").toString());
						totalExtractionFailedCt=totalExtractionFailedCt+Double.valueOf(sixWeekMap.get("totalExtractionFailedCt").toString());
						totalExtractedCountPer=totalExtractedCountPer+Double.valueOf(sixWeekMap.get("totalExtractedPer").toString());
						totalExtractionFailedCtPer=totalExtractionFailedCtPer+Double.valueOf(sixWeekMap.get("totalExtractionFailedCtPer").toString());
					}
					if(from13Date.isAfter(fDate) || from13Date.equals(fDate))
					{
						LinkedHashMap sixWeekMap=dashBoardV2Service.extractionAnalysisforGivenPeriod(jobDetails, from13Date,till13Date, toDate);
						labelDatesValue.add(dashBoardV2Service.datesMap(from13Date.toString(),till13Date.toString()));

						// labelDatesValue.add(from13Date+"-"+till13Date);
						labelValue.add("Week-13");
						extracted.add(Double.valueOf(sixWeekMap.get("totalExtracted").toString()));
						extractedFailed.add(Double.valueOf(sixWeekMap.get("totalExtractionFailedCt").toString()));
						totalExtractedCount=totalExtractedCount+Double.valueOf(sixWeekMap.get("totalExtracted").toString());
						totalExtractionFailedCt=totalExtractionFailedCt+Double.valueOf(sixWeekMap.get("totalExtractionFailedCt").toString());
						totalExtractedCountPer=totalExtractedCountPer+Double.valueOf(sixWeekMap.get("totalExtractedPer").toString());
						totalExtractionFailedCtPer=totalExtractionFailedCtPer+Double.valueOf(sixWeekMap.get("totalExtractionFailedCtPer").toString());
					}



					LinkedHashMap THDet=new LinkedHashMap();

					THDet.put("labelValue", labelValue);
					THDet.put("labelDateValue", labelDatesValue);
					THDet.put("extracted", extracted);

					THDet.put("extractionFailed", extractedFailed);

					LinkedHashMap threeMonthMap=new LinkedHashMap();
					threeMonthMap.put("totalExtracted", totalExtractedCount);

					threeMonthMap.put("totalExtractionFailedCt", totalExtractionFailedCt);
					threeMonthMap.put("totalExtractedPer",Double.valueOf(dform.format(totalExtractedCountPer)));
					threeMonthMap.put("totalExtractionFailedCtPer",Double.valueOf(dform.format(totalExtractionFailedCtPer)));

					threeMonthMap.put("detailedData", THDet);
					eachMap.put("3M", threeMonthMap);
				}
				return eachMap;

			}

			@PostMapping("/extractionAndTransformationUnprocessedCounts")
			@Timed
			public LinkedHashMap extractionAndTransformationUnprocessedCounts(@RequestParam String processId,@RequestBody HashMap dates,HttpServletRequest request) throws SQLException, ParseException
			{

				LinkedHashMap finalMap=new LinkedHashMap();

				HashMap map=userJdbcService.getuserInfoFromToken(request);
				Long tenantId=Long.parseLong(map.get("tenantId").toString());

				Processes processes = processesRepository.findByTenantIdAndIdForDisplay(tenantId, processId);

				List<BigInteger>  profileId=processDetailsRepository.findTypeIdByProcessIdAndTagType(processes.getId(), "sourceProfile");

				ZonedDateTime fmDate=ZonedDateTime.parse(dates.get("startDate").toString());
				ZonedDateTime toDate=ZonedDateTime.parse(dates.get("endDate").toString());
				log.info("fmDate :"+fmDate);
				log.info("toDate :"+toDate);
				java.time.LocalDate fDate=fmDate.toLocalDate();
				java.time.LocalDate tDate=toDate.toLocalDate();

				ApplicationPrograms app=applicationProgramsRepository.findByPrgmNameAndTenantId("DataExtraction", tenantId);
				log.info("app :"+app);
				log.info("profileId :"+profileId);
				if(profileId.size()>0)
				{
					List<BigInteger> jobDetails=jobDetailsRepository.findByTenantIdAndProgrammIdAndParameterArgument1(app.getId(),profileId,fDate+"%",tDate+"%");

					LinkedHashMap LHM=dashBoardV2Service.extractionAnalysisforGivenPeriod(jobDetails, fDate,tDate, toDate);
					log.info("totalExtractionFailedCt :"+Double.valueOf(LHM.get("totalExtractionFailedCt").toString()));
					log.info("totalExtracted :"+Double.valueOf(LHM.get("totalExtracted").toString()));
					Double totalExtractionFailedCtPer=0d;
					if(Double.valueOf(LHM.get("totalExtracted").toString())>0)
						totalExtractionFailedCtPer=(Double.valueOf(LHM.get("totalExtractionFailedCt").toString())/Double.valueOf(LHM.get("totalExtracted").toString()))*100;
					finalMap.put("totalExtractionFailedCtPer", Double.valueOf(dform.format(totalExtractionFailedCtPer)));
					finalMap.put("totalExtractionFailedCt", Double.valueOf(LHM.get("totalExtractionFailedCt").toString()));


					List<Object[]> filesTransformedSummary=sourceFileInbHistoryRepository.fetchTransfomedCountBetweenGivenDateForWeek(profileId,fDate+"%",tDate+"%");
					Double totalTransformationFailedCtPer=0d;
					//if(Double.valueOf(filesTransformedSummary.get(0)[1].toString())>0)
					//	totalTransformationFailedCtPer=(Double.valueOf(filesTransformedSummary.get(0)[4].toString())/Double.valueOf(filesTransformedSummary.get(0)[1].toString()))*100;
					finalMap.put("totalTransformationFailedCtPer", Double.valueOf(dform.format(filesTransformedSummary.get(0)[3])));
					finalMap.put("totalTransformationFailedCt", filesTransformedSummary.get(0)[4]);
					log.info("totalTransformation :"+ filesTransformedSummary.get(0)[1].toString());



					/**records in staging**/;

					List<DataStaging> dataStatingTotal=dataStagingRepository.findByProfileIdIn(profileId,fDate+"%",tDate+"%");
					log.info("dataStatingTotal size :"+dataStatingTotal.size());
					finalMap.put("failedRecordsCount", dataStatingTotal.size());
					List<DataStaging> dataStatingFailedRecords=dataStagingRepository.findByProfileIdInAndRecordStatus(profileId,"FAIL",fDate+"%",tDate+"%");
					log.info("dataStatingFailedRecords size :"+dataStatingFailedRecords.size());
					Double fRecPer=0d;
					if(dataStatingTotal.size()>0)
						fRecPer=(Double.valueOf(dataStatingFailedRecords.size())/Double.valueOf(dataStatingTotal.size()))*100;
					finalMap.put("failedRecordsCountPer",Double.valueOf(dform.format(fRecPer)));
				}

				return finalMap;
			}




			@PostMapping("/journalsUnprocessedCounts")
			@Timed
			public LinkedHashMap journalsUnprocessedCounts(@RequestParam String processId,@RequestBody HashMap dates,HttpServletRequest request,@RequestParam int violation) throws SQLException, ParseException
			{
				log.info("getSummaryInfoForAccountingV2 of processId:"+processId);
				LinkedHashMap finalMap=new LinkedHashMap();
				List<LinkedHashMap> dataMap=new ArrayList<LinkedHashMap>();
		    	HashMap map1=userJdbcService.getuserInfoFromToken(request);
		      	Long tenantId=Long.parseLong(map1.get("tenantId").toString());
				Processes processes = processesRepository.findByTenantIdAndIdForDisplay(tenantId, processId);
				ProcessDetails procesDet=processDetailsRepository.findByProcessIdAndTagType(processes.getId(), "accountingRuleGroup");
				if(procesDet!=null)
				{
					ZonedDateTime fmDate=ZonedDateTime.parse(dates.get("startDate").toString());
					ZonedDateTime toDate=ZonedDateTime.parse(dates.get("endDate").toString());
					log.info("fmDate :"+fmDate);
					log.info("toDate :"+toDate);
					java.time.LocalDate fDate=fmDate.toLocalDate();
					java.time.LocalDate tDate=toDate.toLocalDate();

					Double totalAccountedAmt=0d;
					Double totalPostedAmt=0d;
					
					
					
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
					Statement stmtApp=null;


					conn = DriverManager.getConnection(dbUrl, userName, password);
					log.info("Connected database successfully...");
					stmtDv = conn.createStatement();
					stmtApp = conn.createStatement();
					
					
					
					

					/* Object[] unActCount=appModuleSummaryRepository.fetchUnActCountsByGroupIdAndFileDate(procesDet.getTypeId(),fDate,tDate);
    		 if(unActCount[0]!=null)
    		 {
    			 log.info("unrecontotalCount :"+Double.valueOf(unActCount[0].toString()));
    			 totalActct=Double.valueOf(unActCount[0].toString());
    		 }*/

					List<BigInteger> distViewsIds=appModuleSummaryRepository.findDistinctViewIdByRuleGroupId(procesDet.getTypeId(),fDate,tDate);
					log.info("distViewsIds :"+distViewsIds.size());
					Long violationCount=0l;
					Long totalUnapprovedCount=0l;
					Long totalViolationCount=0l;
					for(BigInteger viewId:distViewsIds)
					{

						LinkedHashMap map=new LinkedHashMap();
						map.put("viewId", viewId);
						List<Object[]> accountingSummary=appModuleSummaryRepository.fetchActCountsByGroupIdAndViewIdAndFileDate(Long.valueOf(procesDet.getTypeId()),viewId.longValue(),fDate,tDate);
						DataViews dv=dataViewsRepository.findOne(viewId.longValue());
						map.put("viewName", dv.getDataViewName());
						for(int i=0;i<accountingSummary.size();i++)
						{

							String capitalized = WordUtils.capitalizeFully(accountingSummary.get(i)[3].toString());
							map.put("dvCount", accountingSummary.get(i)[0].toString());
							String process=capitalized.substring(0, 1).toLowerCase() + capitalized.substring(1);
							map.put(process.replaceAll("\\s", "")+"Count", accountingSummary.get(i)[1].toString());

							DataViewsColumns dvColumn=dataViewsColumnsRepository.findByDataViewIdAndQualifier(dv.getId(), "AMOUNT");
							String ammountQualifier="";
							if(dvColumn.getRefDvType().equalsIgnoreCase("File Template"))
							{
								FileTemplateLines ftl=fileTemplateLinesRepository.findOne(Long.valueOf(dvColumn.getRefDvColumn()));
								ammountQualifier=ftl.getColumnAlias();
							}
							else
								ammountQualifier=dvColumn.getColumnName();

							map.put(process.replaceAll("\\s", "")+"per", accountingSummary.get(i)[2].toString());
							map.put(process.replaceAll("\\s", "")+"amt", accountingSummary.get(i)[9].toString());


				

							ResultSet resultDv=null;
							// ResultSet resultApp=null;
							String query="";
							if(process .equalsIgnoreCase("accounted"))
							{
								totalAccountedAmt=totalAccountedAmt+Double.valueOf(accountingSummary.get(i)[9].toString());
							}
							if(process .equalsIgnoreCase("final accounted"))
							{

								BigDecimal FinalaccountSummary=appModuleSummaryRepository.fetchFinalActAmtByGroupIdAndViewIdAndFileDate(Long.valueOf(procesDet.getTypeId()),viewId.longValue(),fDate,tDate);
								totalPostedAmt=totalPostedAmt+FinalaccountSummary.doubleValue();
								List<BigInteger> finalSrcIdList=new ArrayList<BigInteger>();
								List<BigInteger> PostedViewIds=accountedSummaryRepository.fetchUnPostedRowIdsByRuleGrpIdAndViewId(procesDet.getTypeId(), viewId.longValue()) ;
								finalSrcIdList.addAll(PostedViewIds);

								if(finalSrcIdList!=null && !finalSrcIdList.isEmpty())
								{
									String finalSrcIds=finalSrcIdList.toString().replaceAll("\\[", "").replaceAll("\\]", "");

									query="select DATEDIFF( SYSDATE(), `v`.`fileDate`) as `rule_age`,count(scrIds),sum(`"+ammountQualifier+"`) as `"+ammountQualifier+"` from "+schemaName+".`"+dv.getDataViewName().toLowerCase().toLowerCase()+"` v where fileDate between '"+fDate+"' and '"+tDate+"' "
											+ " and scrIds not in ("+finalSrcIds+")group by rule_age";


									/*else
    					 {
    						 query="select DATEDIFF( SYSDATE(), `v`.`fileDate`) as `rule_age`,count(scrIds),sum(`"+ammountQualifier+"`) as `"+ammountQualifier+"` from "+schemaName+"."+dv.getDataViewName().toLowerCase().toLowerCase()+" v where fileDate between '"+fDate+"' and '"+tDate+"' "
    								 + "group by rule_age";

    					 }*/
									// log.info("query :"+query);

									resultDv=stmtDv.executeQuery(query);
									ResultSetMetaData rsmd2 = resultDv.getMetaData();
									int columnCount = rsmd2.getColumnCount();
									map.put("violationAmount",0);
									map.put("violationCount", 0);


									while(resultDv.next())
									{
										int ruleAge=Integer.valueOf(resultDv.getString("rule_age").toString());


										totalViolationCount=totalViolationCount+Long.valueOf(resultDv.getString("count(scrIds)").toString());
										if(ruleAge>violation)
										{
											// log.info("if rule age is greater than violation");
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
									if(resultDv!=null)
										resultDv.close();

								}
								else
								{
									map.put("violationAmount",0);
									map.put("violationCount", 0);
								}


							}

						}

						dataMap.add(map);
					}
					Double totalNotPostedAmt=totalAccountedAmt-totalPostedAmt;
					double unPostedItemsValuePer=0d;
					double unPostedItemsViolationPer=0d;
					if(totalAccountedAmt>0)
						unPostedItemsValuePer=((double)totalNotPostedAmt/totalAccountedAmt)*100;
					if(totalViolationCount>0)
						unPostedItemsViolationPer=((double)violationCount/totalViolationCount)*100;
					finalMap.put("unPostedItemsValue", totalNotPostedAmt);
					finalMap.put("unPostedItemsValuePer", Double.valueOf(dform.format(unPostedItemsValuePer)));
					finalMap.put("unPostedItemsViolation",violationCount);
					if(totalViolationCount!=0)
						finalMap.put("unPostedItemsViolationPer",Double.valueOf(dform.format(unPostedItemsViolationPer)));
					else
						finalMap.put("unPostedItemsViolationPer",0); 
					// finalMap.put("awaitingAppCount", totalUnapprovedCount);
					// finalMap.put("accountingData", dataMap);
					if(stmtApp!=null)
						stmtApp.close();
					if(stmtDv!=null)
						stmtDv.close();
					if(conn!=null)
						conn.close();

	

				}

				return finalMap;
			}



			/**week specific details**/
			@PostMapping("/weekAnalysisForModule")
			@Timed
			public LinkedHashMap weekAnalysisForModule(@RequestParam String processId,@RequestBody HashMap dates,@RequestParam String module,HttpServletRequest request) throws SQLException, ParseException
			{ 
				log.info("Rest Request to get aging analysis :"+dates);
		    	HashMap map=userJdbcService.getuserInfoFromToken(request);
		      	Long tenantId=Long.parseLong(map.get("tenantId").toString());
				LinkedHashMap eachMap=new LinkedHashMap();
				ZonedDateTime fmDate=ZonedDateTime.parse(dates.get("startDate").toString());

				ZonedDateTime toDate=ZonedDateTime.parse(dates.get("endDate").toString());

				log.info("fmDate :"+fmDate);

				log.info("toDate :"+toDate);

				LocalDate fDate=fmDate.toLocalDate();

				LocalDate tDate=toDate.toLocalDate();
				/* LinkedHashMap datesMap=new LinkedHashMap();
		 datesMap.put("startDate",fDate);
		 datesMap.put("endDate", tDate);*/
				Processes processes = processesRepository.findByTenantIdAndIdForDisplay(tenantId, processId);
				if(processId!=null)

				{
					if(module.equalsIgnoreCase("reconciliation"))
					{
						LinkedHashMap Map=dashBoardV2Service.reconciliationAnalysisforGivenWeek(processes.getId(), fDate,tDate);
						eachMap.put("oneWeek", Map);
					}
					else if(module.equalsIgnoreCase("transformation"))
					{
						eachMap=dashBoardV2Service.transformationAnalysisforGivenWeek(processes.getId(), dates);
						// eachMap.put("oneWeek", Map);
					}
					else if (module.equalsIgnoreCase("extraction"))
					{
						List<BigInteger> profileId=processDetailsRepository.findTypeIdByProcessIdAndTagType(processes.getId(), "sourceProfile");
						ApplicationPrograms app=applicationProgramsRepository.findByPrgmNameAndTenantId("DataExtraction", tenantId);

						List<BigInteger> jobDetails=jobDetailsRepository.findByTenantIdAndProgrammIdAndParameterArgument1(app.getId(),profileId,fDate+"%",tDate+"%");
						log.info("jobDetails :"+jobDetails.size());
						LinkedHashMap WeekMap=dashBoardV2Service.extractionAnalysisforGivenPeriod(jobDetails, fDate, tDate, toDate);
						eachMap.put("oneWeek", WeekMap);
					}
					else if(module.equalsIgnoreCase("accounting"))
					{
						log.info("in accounting");
						LinkedHashMap WeekMap=dashBoardV2Service.accountingAndJournalAnalysisforGivenWeek(processes.getId(), fDate,tDate);
						eachMap.put("oneWeek", WeekMap);
					}

				}
				return eachMap;
			}



			@PostMapping("/detailInformationForApprovals")
			@Timed
			public List<LinkedHashMap> detailInformationForApprovals(HttpServletRequest request,@RequestParam String processId,@RequestParam Long viewId,@RequestBody HashMap dates,@RequestParam String module) throws ClassNotFoundException, SQLException
			{ 
				log.info("Rest Request to get aging analysis :"+dates);
				List<LinkedHashMap> finalMap=new ArrayList<LinkedHashMap>();

				HashMap map1=userJdbcService.getuserInfoFromToken(request);
				Long tenantId=Long.parseLong(map1.get("tenantId").toString());

				ZonedDateTime fmDate=ZonedDateTime.parse(dates.get("startDate").toString());

				ZonedDateTime toDate=ZonedDateTime.parse(dates.get("endDate").toString());

				log.info("fmDate :"+fmDate);

				log.info("toDate :"+toDate);

				LocalDate fDate=fmDate.toLocalDate();

				LocalDate tDate=toDate.toLocalDate();
				Long ruleGroupId=0l;

				Processes processes = processesRepository.findByTenantIdAndIdForDisplay(tenantId, processId);
				ProcessDetails processDetails=new ProcessDetails();
				if(module.equalsIgnoreCase("reconciliation"))
					processDetails = processDetailsRepository.findByProcessIdAndTagType(processes.getId(),"reconciliationRuleGroup");
				else if(module.equalsIgnoreCase("accounting"))
					processDetails = processDetailsRepository.findByProcessIdAndTagType(processes.getId(),"accountingRuleGroup");

				if(processDetails!=null && processDetails.getId()!=null)
				{
					ruleGroupId=processDetails.getTypeId();
					RuleGroup appRuleGrp=ruleGroupRepository.findOne(ruleGroupId);

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
								if(module.equalsIgnoreCase("reconciliation"))
									queryCount="select id from ReconciliationResult where reconciliation_rule_group_id="+ruleGroupId+" and (original_view_id="+viewId+" or target_view_id="+viewId+") and apprRef"+refNum+" LIKE '"+userAndBatchId+"%'";
								else
									queryCount="select id from AccountingData where acct_group_id="+ruleGroupId+" and (original_view_id="+viewId+") and apprRef"+refNum+" LIKE '"+userAndBatchId+"%'";

							//	log.info("queryCount :"+queryCount);
								Query distReconRefQueryCount=em.createQuery(queryCount);
								List count=distReconRefQueryCount.getResultList();
								log.info("count.get(0) :"+count.size());
								LinkedHashMap appMap=new LinkedHashMap();
								HashMap map=userJdbcService.jdbcConnc(notBatch.getCurrentApprover(),notBatch.getTenantId());
								if(map!=null && map.get("assigneeName")!=null)
								{
									appMap.put("name",map.get("assigneeName"));
									appMap.put("value",count.size());
									finalMap.add(appMap);
								}

							}
						}
					}


				}

				return finalMap;
			}

			@PostMapping("/detailInformationForJournals")
			@Timed
			public List<LinkedHashMap> detailInformationForJournals(HttpServletRequest request, @RequestParam String processId,@RequestBody HashMap dates,@RequestParam(value="status",required=false) String status,@RequestParam Long viewId) throws SQLException 
			{ 
		    	HashMap map=userJdbcService.getuserInfoFromToken(request);
		      	Long tenantId=Long.parseLong(map.get("tenantId").toString());		        
		        Processes processes = processesRepository.findByTenantIdAndIdForDisplay(tenantId, processId);
		      
				ProcessDetails processDetails = processDetailsRepository.findByProcessIdAndTagType(processes.getId(),"accountingRuleGroup");

				ZonedDateTime fmDate=ZonedDateTime.parse(dates.get("startDate").toString());

				ZonedDateTime toDate=ZonedDateTime.parse(dates.get("endDate").toString());

				log.info("fmDate :"+fmDate);

				log.info("toDate :"+toDate);

				LocalDate fDate=fmDate.toLocalDate();

				LocalDate tDate=toDate.toLocalDate();
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
				ResultSet resultAct=null;
				List<Long> srcIdsList=new ArrayList<Long>();
				//List<BigInteger> distictViewsList=accountingDataRepository.findDistinctOrginalViewIdByAcctGroupId(processDetails.getTypeId());

				//for(BigInteger distictViews:distictViewsList)
				//{
				String fileDateOrQualifierDate=dashBoardV4Service.getFileDateOrQualifier(viewId,tenantId);
					DataViews dvName=dataViewsRepository.findOne(viewId);
					String query="select scrIds from "+schemaName+".`"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where (Date(`"+fileDateOrQualifierDate+"`)>= '"+fmDate+"' and Date(`"+fileDateOrQualifierDate+"`)<='"+toDate+"')";

					//log.info("query :"+query);
					resultDv=stmtDv.executeQuery(query);

					while(resultDv.next())
					{
						srcIdsList.add(Long.valueOf(resultDv.getString("scrIds")));
					}
				//}
				List<LinkedHashMap> finalMap=new ArrayList<LinkedHashMap>();
				if(srcIdsList.size()>0)
				{
					List<Long> journalEnteredRowIds=new ArrayList<Long>();
					if(status!=null && status.equalsIgnoreCase("JE creation"))
					{
						journalEnteredRowIds =accountedSummaryRepository.findRowIdsByRuleGroupIAndStatus(srcIdsList, processDetails.getTypeId());
					}
					else if(status!=null && status.equalsIgnoreCase("accounted"))
					{
						journalEnteredRowIds=accountedSummaryRepository.findRowIdsByRuleGroupIAndStatusNull(srcIdsList, processDetails.getTypeId());
					}
					else if (status==null)
					{
						journalEnteredRowIds=accountedSummaryRepository.findRowIdsByRuleGroupIAndStatusAccounted(srcIdsList, processDetails.getTypeId());
					}
					log.info("journalEnteredRowIds.size :"+journalEnteredRowIds.size());
					if(journalEnteredRowIds.size()>0)
					{
						List<Long> coaList=accountingDataRepository.findDistinctCoaRefByTenantIdAndOriginalRowIdInAndAcctGroupId(tenantId, journalEnteredRowIds, processDetails.getTypeId());

						log.info("acctDataList :"+coaList);
						// 

						List<Segments> segmentList=segmentsRepository.findByCoaIdInAndQualifier(coaList,"NATURAL_ACCOUNT");
						log.info("segmentList :"+segmentList);
						for(Segments segment:segmentList)
						{

							String distinctRefQuery="select distinct a.accountingRef"+segment.getSequence()
									+ " from AccountingData a where a.acctGroupId="+processDetails.getTypeId()+" and a.originalRowId in ("+journalEnteredRowIds.toString().replaceAll("\\[", "").replaceAll("\\]", "")+")";

						//	log.info("distinctRefQuery :"+distinctRefQuery);
							Query distReconRefQueryCount=em.createQuery(distinctRefQuery);
							List<String> distinctRefLisr=distReconRefQueryCount.getResultList();
							log.info("distinctRefLisr :"+distinctRefLisr);
							if(distinctRefLisr.size()>0)
							{
								for(String distinctRef:distinctRefLisr)
								{
									String queryCount="select sum(a.amount),sum(a.accountedAmount),a.lineType"
											+ " from AccountingData a where a.accountingRef"+segment.getSequence()+"="+distinctRef+" and a.acctGroupId="+processDetails.getTypeId()+" and a.originalRowId in ("+journalEnteredRowIds.toString().replaceAll("\\[", "").replaceAll("\\]", "")+") group by a.lineType";


								//	log.info("queryCount :"+queryCount);
									Query queryResult=em.createQuery(queryCount);
									List<Object[]> count=queryResult.getResultList();

									for(int i=0;i<count.size();i++)
									{
										Double debit=0d;
										Double credit=0d;
										LinkedHashMap actDatamap=new LinkedHashMap();
										actDatamap.put("account", distinctRef);
										MappingSetValues mapSetValue=mappingSetValuesRepository.findByMappingSetIdAndSourceValue(segment.getValueId(),distinctRef);
										log.info("mapSetValue :"+mapSetValue);
										actDatamap.put("description", mapSetValue.getTargetValue());
										if(count.get(i)[2].toString().equalsIgnoreCase("debit"))
										{
											// actDatamap.put("enteredAmount", count.get(i)[0]);
											//accountedAmount
											if(count.get(i)[1]!=null)
											{
												actDatamap.put("debit", count.get(i)[1]);
												debit=Double.valueOf(count.get(i)[1].toString());
											}
											else
												actDatamap.put("debit",0);

										}
										else
											actDatamap.put("debit",0);
										if(count.get(i)[2].toString().equalsIgnoreCase("credit"))
										{
											if(count.get(i)[1]!=null)
											{
												actDatamap.put("credit", count.get(i)[1]);
												credit=Double.valueOf(count.get(i)[1].toString());
											}
											else
												actDatamap.put("credit",0);

										}
										else
											actDatamap.put("credit",0); 
										double balance=credit-debit;
										actDatamap.put("balance", balance);
										finalMap.add(actDatamap);
									}
								}
							}
						}
					}
				}
				if(resultDv!=null)
					resultDv.close();
				if(stmtDv!=null)
					stmtDv.close();
				if(conn!=null)
					conn.close();
				return finalMap;

			}





			@PostMapping("/detailInformationForJournalsByView")
			@Timed
			public List<LinkedHashMap> detailInformationForJournalsByView(@RequestParam String processId,@RequestParam Long viewId,@RequestParam String groupByCol,@RequestBody HashMap dates,HttpServletRequest request,@RequestParam(value="status",required=false) String status) throws SQLException 
			{ 
		    	HashMap map=userJdbcService.getuserInfoFromToken(request);
		      	Long tenantId=Long.parseLong(map.get("tenantId").toString());
		      	Processes processes = processesRepository.findByTenantIdAndIdForDisplay(tenantId, processId);
				ProcessDetails processDetails = processDetailsRepository.findByProcessIdAndTagType(processes.getId(),"accountingRuleGroup");

				ZonedDateTime fmDate=ZonedDateTime.parse(dates.get("startDate").toString());

				ZonedDateTime toDate=ZonedDateTime.parse(dates.get("endDate").toString());

				log.info("fmDate :"+fmDate);

				log.info("toDate :"+toDate);

				LocalDate fDate=fmDate.toLocalDate();

				LocalDate tDate=toDate.toLocalDate();
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
				Statement stmtJe = null;



				conn = DriverManager.getConnection(dbUrl, userName, password);
				log.info("Connected database successfully...");
				stmtDv = conn.createStatement();
				stmtJe= conn.createStatement();
				ResultSet resultDv=null;

				ResultSet finalQuery=null;
				List<Long> srcIdsList=new ArrayList<Long>();
				DataViews dvName=dataViewsRepository.findOne(viewId);
				String query="select scrIds from "+schemaName+".`"+dvName.getDataViewName().toLowerCase()+"` where (Date(fileDate)>= '"+fmDate+"' and Date(fileDate)<='"+toDate+"')";

			//	log.info("query :"+query);
				resultDv=stmtDv.executeQuery(query);

				while(resultDv.next())
				{
					srcIdsList.add(Long.valueOf(resultDv.getString("scrIds")));
				}

				List<LinkedHashMap> finalMap=new ArrayList<LinkedHashMap>();
				if(srcIdsList.size()>0)
				{
					List<BigInteger> journalEnteredRowIds=new ArrayList<BigInteger>();
					if(status!=null && status.equalsIgnoreCase("JE creation"))
					{
						log.info("if je posted");
						journalEnteredRowIds =accountedSummaryRepository.findRowIdsByRuleGroupIAndViewIdAndStatus(srcIdsList, processDetails.getTypeId(),viewId);
						log.info("journalEnteredRowIds.size1 :"+journalEnteredRowIds.size());
					}
					else if(status!=null && status.equalsIgnoreCase("accounted"))
					{
						log.info("else accounted");
						journalEnteredRowIds=accountedSummaryRepository.findUnPostedRowIdsByRuleGroupIAndViewIdAndStatus(srcIdsList, processDetails.getTypeId(),viewId);
					}
					else if (status==null)
					{
						journalEnteredRowIds=accountedSummaryRepository.findRowIdsByRuleGroupIAndViewIdAndStatusAccounted(srcIdsList, processDetails.getTypeId(),viewId);
					}
					log.info("journalEnteredRowIds.size :"+journalEnteredRowIds.size());
					List<Long> coaList=accountingDataRepository.findDistinctCoaRefByTenantIdAndOriginalRowIdInAndAcctGroupIdAndViewId(tenantId, journalEnteredRowIds, processDetails.getTypeId(),viewId);

					log.info("acctDataList :"+coaList);


					List<Segments> segmentList=segmentsRepository.findByCoaIdInAndQualifier(coaList,"NATURAL_ACCOUNT");
					//log.info("segmentList :"+segmentList);
					for(Segments segment:segmentList)
					{

						String distinctRefQuery="select distinct a.accountingRef"+segment.getSequence()
								+ " from AccountingData a where a.acctGroupId="+processDetails.getTypeId()+" and a.originalRowId in ("+journalEnteredRowIds.toString().replaceAll("\\[", "").replaceAll("\\]", "")+")";

						//log.info("distinctRefQuery :"+distinctRefQuery);
						Query distReconRefQueryCount=em.createQuery(distinctRefQuery);
						List<String> distinctRefLisr=distReconRefQueryCount.getResultList();
						log.info("distinctRefLisr :"+distinctRefLisr);
						if(distinctRefLisr.size()>0)
						{
							for(String distinctRef:distinctRefLisr)
							{
								String queryGrpByDv="select  sum(a.amount),sum(a.accounted_amount),a.line_type, a.accounting_ref_"+segment.getSequence()+", v.`"+groupByCol+"`"

    						 + " from "+schemaName+".t_accounting_data a,"+schemaName+".`"+dvName.getDataViewName().toLowerCase()+"` v where a.accounting_ref_"+segment.getSequence()+"="+distinctRef+" and a.acct_group_id="+processDetails.getTypeId()+" and a.original_row_id in ("+journalEnteredRowIds.toString().replaceAll("\\[", "").replaceAll("\\]", "")+") "
    						 + " and a.original_view_id ="+viewId+" and a.original_row_id=v.scrIds group by a.line_type,v.`"+groupByCol+"`";


								//log.info("queryGrpByDv :"+queryGrpByDv);
								finalQuery=stmtJe.executeQuery(queryGrpByDv);


								while(finalQuery.next())
								{
									LinkedHashMap actDatamap=new LinkedHashMap();
									Double debit=0d;
									Double credit=0d;
									actDatamap.put("account", distinctRef);
									MappingSetValues mapSetValue=mappingSetValuesRepository.findByMappingSetIdAndSourceValue(segment.getValueId(),distinctRef);
									log.info("mapSetValue :"+mapSetValue);
									actDatamap.put("description", mapSetValue.getTargetValue());
									if(finalQuery.getString("line_type").equalsIgnoreCase("debit"))
									{
										// actDatamap.put("enteredAmount", count.get(i)[0]);
										//accountedAmount
										if(finalQuery.getString("line_type")!=null)
										{
											actDatamap.put("debit",Double.valueOf(  finalQuery.getString("sum(a.accounted_amount)")));
											debit=Double.valueOf(finalQuery.getString("sum(a.accounted_amount)").toString());
										}
										else
											actDatamap.put("debit",0);

									}
									else
										actDatamap.put("debit",0);
									if(finalQuery.getString("line_type").equalsIgnoreCase("credit"))
									{
										if(finalQuery.getString("line_type")!=null)
										{
											actDatamap.put("credit",Double.valueOf( finalQuery.getString("sum(a.accounted_amount)")));
											credit=Double.valueOf(finalQuery.getString("sum(a.accounted_amount)").toString());
										}
										else
											actDatamap.put("credit",0);

									}
									else
										actDatamap.put("credit",0); 
									double balance=credit-debit;
									actDatamap.put("balance", balance);
									actDatamap.put(groupByCol, finalQuery.getString(groupByCol).toString());
									finalMap.add(actDatamap);
								}
							}
						}
					}
				}
				if(conn!=null)
					conn.close();
				if(stmtDv!=null)
					stmtDv.close();
				if(stmtJe!=null)
					stmtJe.close();
				if(resultDv!=null)
					resultDv.close();
				if(finalQuery!=null)
					finalQuery.close();
				return finalMap;

			}
			
		
			@PostMapping("/getUnProcessedOrProcessedDataForAllGroupBy")
			@Timed 
			public LinkedHashMap getUnProcessedOrProcessedDataForAllGroupBy(@RequestParam String processId,@RequestParam Long viewId,@RequestParam String module,@RequestParam String amtQuailifier
					,@RequestParam List<String> groupByColmns,@RequestBody HashMap dates,HttpServletRequest request) throws SQLException, ParseException
					{
		    	HashMap map=userJdbcService.getuserInfoFromToken(request);
		      	Long tenantId=Long.parseLong(map.get("tenantId").toString());
				Processes processes = processesRepository.findByTenantIdAndIdForDisplay(tenantId, processId);
				ProcessDetails procesRecDet=processDetailsRepository.findByProcessIdAndTagType(processes.getId(), "reconciliationRuleGroup");
				ProcessDetails procesActDet=processDetailsRepository.findByProcessIdAndTagType(processes.getId(), "accountingRuleGroup");
				ZonedDateTime fmDate=ZonedDateTime.parse(dates.get("startDate").toString());
				ZonedDateTime toDate=ZonedDateTime.parse(dates.get("endDate").toString());
				log.info("fmDate :"+fmDate);
				log.info("toDate :"+toDate);
				java.time.LocalDate fDate=fmDate.toLocalDate();
				java.time.LocalDate tDate=toDate.toLocalDate();
				List<BigInteger> finalSrcIdList=new ArrayList<BigInteger>();
				if(module.equalsIgnoreCase("reconciled") || module.equalsIgnoreCase("un-reconciled"))
				{
					log.info(" in if");
					List<BigInteger> reconciliedSrcIds=reconciliationResultRepository.fetchReconciledSourceIds(tenantId, procesRecDet.getTypeId(), viewId);	 
					finalSrcIdList.addAll(reconciliedSrcIds);
					List<BigInteger> reconciliedTrgIds=reconciliationResultRepository.fetchReconciledTargetIds(tenantId, procesRecDet.getTypeId(), viewId);	 
					finalSrcIdList.addAll(reconciliedTrgIds);
				}
				else if(module.equalsIgnoreCase("accounted") )
				{
					log.info(" in else accounted");
					//where status = accounted and journal not entered
					List<BigInteger> accountedViewIds=accountedSummaryRepository.fetchUnPostedRowIdsByRuleGrpIdAndViewId(procesActDet.getTypeId(), viewId) ;
					finalSrcIdList.addAll(accountedViewIds);
				}
			/*	else if(module.equalsIgnoreCase("accounting in process")||  module.equalsIgnoreCase("not accounted"))
				{
					log.info(" in else not accounted");
					List<BigInteger> accountedViewIds=accountedSummaryRepository.fetchUnPostedRowIdsByRuleGrpIdAndViewId(procesActDet.getTypeId(), viewId) ;
					finalSrcIdList.addAll(accountedViewIds);
				}*/
				else if(module.equalsIgnoreCase("JE Creation"))
				{
					log.info(" in else JE Creation");
					List<BigInteger> accountedViewIds=accountedSummaryRepository.fetchPostedRowIdsByRuleGrpIdAndViewId(procesActDet.getTypeId(), viewId) ;
					finalSrcIdList.addAll(accountedViewIds);
				}
				log.info("finalSrcIdList :"+finalSrcIdList.size());
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
				ResultSet resultAct=null;
				DataViews dvName=dataViewsRepository.findOne(viewId);
				LinkedHashMap lhm=new LinkedHashMap();
				for(int g=0;g<groupByColmns.size();g++)
				{
				String groupBy=groupByColmns.get(g);
			//	String groupBy=groupByColmns.toString().replaceAll("\\[", "").replaceAll("\\]", "");
				String finalSrcIds=finalSrcIdList.toString().replaceAll("\\[", "").replaceAll("\\]", "");
				log.info("groupBy :"+groupBy);
				String query="";
				List<LinkedHashMap> finalMap=new ArrayList<LinkedHashMap>();

				if(module.equalsIgnoreCase("un-reconciled") || module.equalsIgnoreCase("not accounted"))
				{
					if(finalSrcIds!=null && !finalSrcIds.isEmpty())
					{
						query="select `"+groupBy+"`,round(sum(`"+amtQuailifier+"`),"+round+") as `"+amtQuailifier+"` from "+schemaName+".`"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where Date(fileDate) >= '"+fDate+"' and Date(fileDate) <='"+tDate+"' "
								+ " and scrIds not in ("+finalSrcIds+")group by `"+groupBy+"` order by `"+amtQuailifier+"` desc limit 10";
						//log.info("query :"+query);
					}
					
					else if(finalSrcIds.isEmpty() && module.equalsIgnoreCase("un-reconciled") || module.equalsIgnoreCase("not accounted"))
					{
						query="select `"+groupBy+"`,round(sum(`"+amtQuailifier+"`),"+round+") as `"+amtQuailifier+"` from "+schemaName+".`"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where Date(fileDate) >= '"+fDate+"' and Date(fileDate) <='"+tDate+"' "
								+ " group by `"+groupBy+"` order by `"+amtQuailifier+"` desc limit 10";
						//log.info("query :"+query);
					}

				}
				if(module.equalsIgnoreCase("reconciled") || module.equalsIgnoreCase("accounted"))
				{
					query="select `"+groupBy+"`,round(sum(`"+amtQuailifier+"`),"+round+") as `"+amtQuailifier+"` from "+schemaName+".`"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where Date(fileDate) >= '"+fDate+"' and Date(fileDate) <='"+tDate+"' "
							+ " and scrIds in ("+finalSrcIds+")group by `"+groupBy+"` order by `"+amtQuailifier+"` desc limit 10";
					//log.info("query :"+query);
				}
				if(module.equalsIgnoreCase("JE Creation"))
				{
					query="select `"+groupBy+"`,round(sum(`"+amtQuailifier+"`),"+round+") as `"+amtQuailifier+"` from "+schemaName+".`"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` where Date(fileDate) >= '"+fDate+"' and Date(fileDate) <='"+tDate+"' "
							+ " and scrIds in ("+finalSrcIds+")group by `"+groupBy+"` order by `"+amtQuailifier+"` desc limit 10";
					//log.info("query :"+query);
				}


				if(!query.isEmpty())
				{
					resultDv=stmtDv.executeQuery(query);
					ResultSetMetaData rsmd2 = resultDv.getMetaData();
					int columnCount = rsmd2.getColumnCount();

					while(resultDv.next())
					{
						LinkedHashMap map2=new LinkedHashMap();
						for (int i = 1; i <= columnCount; i++ ) {
							String name = rsmd2.getColumnName(i); 
							if(i==1)
								map2.put("name", resultDv.getString(i));
							if(i==2)
								map2.put("value", Double.valueOf(resultDv.getString(i).toString()));
						}
						finalMap.add(map2);
					}
				}
				lhm.put(groupBy, finalMap);
					}
				if(conn!=null)
					conn.close();
				if(stmtDv!=null)
					stmtDv.close();
				if(resultDv!=null)
					resultDv.close();

				return lhm;

					}


}