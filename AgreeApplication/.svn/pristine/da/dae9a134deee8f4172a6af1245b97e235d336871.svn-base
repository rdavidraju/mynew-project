package com.nspl.app.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import io.github.jhipster.web.util.ResponseUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.oozie.client.OozieClientException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.codahale.metrics.annotation.Timed;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;
import com.nspl.app.config.ApplicationContextProvider;
import com.nspl.app.domain.ApplicationPrograms;
import com.nspl.app.domain.BatchHeader;
import com.nspl.app.domain.DataViews;
import com.nspl.app.domain.FormConfig;
import com.nspl.app.domain.JobDetails;
import com.nspl.app.domain.ProgParametersSets;
import com.nspl.app.domain.Reports;
import com.nspl.app.domain.RuleGroup;
import com.nspl.app.domain.SchedulerDetails;
import com.nspl.app.domain.SourceFileInbHistory;
import com.nspl.app.domain.SourceProfileFileAssignments;
import com.nspl.app.domain.SourceProfiles;
import com.nspl.app.repository.ApplicationProgramsRepository;
import com.nspl.app.repository.BatchHeaderRepository;
import com.nspl.app.repository.DataViewsRepository;
import com.nspl.app.repository.FormConfigRepository;
import com.nspl.app.repository.JobDetailsRepository;
import com.nspl.app.repository.LookUpCodeRepository;
import com.nspl.app.repository.ProgParametersSetsRepository;
import com.nspl.app.repository.ReportsRepository;
import com.nspl.app.repository.RuleGroupDetailsRepository;
import com.nspl.app.repository.RuleGroupRepository;
import com.nspl.app.repository.SchedulerDetailsRepository;
import com.nspl.app.repository.SourceFileInbHistoryRepository;
import com.nspl.app.repository.SourceProfileFileAssignmentsRepository;
import com.nspl.app.repository.SourceProfilesRepository;
import com.nspl.app.repository.search.JobDetailsSearchRepository;
import com.nspl.app.security.IDORUtils;
import com.nspl.app.service.OozieService;
import com.nspl.app.service.ReportsService;
import com.nspl.app.service.UserJdbcService;
import com.nspl.app.service.WebSocketService;
import com.nspl.app.web.rest.dto.ErrorReport;
import com.nspl.app.web.rest.dto.Frequencies;
import com.nspl.app.web.rest.dto.JobDetialsDTO;
import com.nspl.app.web.rest.dto.OozieJobDTO;
import com.nspl.app.web.rest.dto.Parameters;
import com.nspl.app.web.rest.util.HeaderUtil;

/**
 * REST controller for managing JobDetails.
 */
@RestController
@RequestMapping("/api")
public class JobDetailsResource {

    private final Logger log = LoggerFactory.getLogger(JobDetailsResource.class);

    private static final String ENTITY_NAME = "jobDetails";
        
    private final JobDetailsRepository jobDetailsRepository;

    private final JobDetailsSearchRepository jobDetailsSearchRepository;
    
    @Inject
    SchedulerDetailsRepository schedularDetailsRepository;
    
    @Inject
    ApplicationProgramsRepository applicationprogramsRepository;
    
    @Inject
	OozieService oozieService;
    
    @Inject
    ProgParametersSetsRepository progParametersSetsRepository;
    
    @Inject
    RuleGroupDetailsRepository ruleGroupDetailsRepository;
    
    @Inject
    RuleGroupRepository ruleGroupRepository;
    
    @Inject
    LookUpCodeRepository lookUpCodeRepository;
    
    @Inject
    ApplicationProgramsRepository applicationProgramsRepository;
    
    @Inject
	FormConfigRepository formConfigRepository;

	@Inject
	SourceProfilesRepository sourceProfilesRepository;

	@Inject
	SourceProfileFileAssignmentsRepository sourceProfileFileAssignmentsRepository;

	@Inject
	BatchHeaderRepository batchHeaderRepository;

	@Inject
	SourceFileInbHistoryRepository sourceFileInbHistoryRepository;
	
	@Inject
	DataViewsRepository dataViewsRepository;

    
    @Inject
    private Environment env;
    
    @Inject
	UserJdbcService userJdbcService;
    
    
    @Inject
    ReportsService reportsService;
    
    @Inject
    ReportsRepository reportsRepository;
    
    @Inject
    SchedulerDetailsResource schedulerDetailsResource;
    
    @Inject
    WebSocketService webSocketService;

    public JobDetailsResource(JobDetailsRepository jobDetailsRepository, JobDetailsSearchRepository jobDetailsSearchRepository) {
        this.jobDetailsRepository = jobDetailsRepository;
        this.jobDetailsSearchRepository = jobDetailsSearchRepository;
    }

    /**
     * POST  /job-details : Create a new jobDetails.
     *
     * @param jobDetails the jobDetails to create
     * @return the ResponseEntity with status 201 (Created) and with body the new jobDetails, or with status 400 (Bad Request) if the jobDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/job-details")
    @Timed
    public ResponseEntity<JobDetails> createJobDetails(@RequestBody JobDetails jobDetails) throws URISyntaxException {
        log.debug("REST request to save JobDetails : {}", jobDetails);
        if (jobDetails.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new jobDetails cannot already have an ID")).body(null);
        }
        JobDetails result = jobDetailsRepository.save(jobDetails);
        jobDetailsSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/job-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /job-details : Updates an existing jobDetails.
     *
     * @param jobDetails the jobDetails to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated jobDetails,
     * or with status 400 (Bad Request) if the jobDetails is not valid,
     * or with status 500 (Internal Server Error) if the jobDetails couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/job-details")
    @Timed
    public ResponseEntity<JobDetails> updateJobDetails(@RequestBody JobDetails jobDetails) throws URISyntaxException {
        log.debug("REST request to update JobDetails : {}", jobDetails);
        if (jobDetails.getId() == null) {
            return createJobDetails(jobDetails);
        }
        JobDetails result = jobDetailsRepository.save(jobDetails);
        jobDetailsSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, jobDetails.getId().toString()))
            .body(result);
    }

    /**
     * GET  /job-details : get all the jobDetails.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of jobDetails in body
     */
    @GetMapping("/job-details")
    @Timed
    public List<JobDetails> getAllJobDetails() {
        log.debug("REST request to get all JobDetails");
        List<JobDetails> jobDetails = jobDetailsRepository.findAll();
        return jobDetails;
    }

    /**
     * GET  /job-details/:id : get the "id" jobDetails.
     *
     * @param id the id of the jobDetails to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the jobDetails, or with status 404 (Not Found)
     */
    @GetMapping("/job-details/{id}")
    @Timed
    public ResponseEntity<JobDetails> getJobDetails(@PathVariable Long id) {
        log.debug("REST request to get JobDetails : {}", id);
        JobDetails jobDetails = jobDetailsRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(jobDetails));
    }

    /**
     * DELETE  /job-details/:id : delete the "id" jobDetails.
     *
     * @param id the id of the jobDetails to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/job-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteJobDetails(@PathVariable Long id) {
        log.debug("REST request to delete JobDetails : {}", id);
        jobDetailsRepository.delete(id);
        jobDetailsSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/job-details?query=:query : search for the jobDetails corresponding
     * to the query.
     *
     * @param query the query of the jobDetails search 
     * @return the result of the search
     */
    @GetMapping("/_search/job-details")
    @Timed
    public List<JobDetails> searchJobDetails(@RequestParam String query) {
        log.debug("REST request to search JobDetails for query {}", query);
        return StreamSupport
            .stream(jobDetailsSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
    
    
    
    /**
     * @author ravali
     * @param jobDetialsDTO(posting job details and frequency details(schedulers)
     * @param userId
     * @param tenantId
     * @throws org.json.simple.parser.ParseException 
     * @throws IOException 
     * @throws NumberFormatException 
     */


    @PostMapping("/postingJobDetails")
     @Timed
     public List<ErrorReport> postDto(HttpServletRequest request,@RequestBody JobDetialsDTO jobDetialsDTO) throws org.json.simple.parser.ParseException//,@RequestParam Long userId,@RequestParam Long tenantId)
, NumberFormatException, IOException
     {
    	HashMap map0=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map0.get("tenantId").toString());
    	Long userId=Long.parseLong(map0.get("userId").toString());
    	log.info("Rest Request to post job details and Scheduler details for userId :"+userId+ " and tenantId :"+tenantId);
    	List<JobDetails> jobDet=new ArrayList<JobDetails>();
    	JobDetails jobDetails=new JobDetails();
    	Boolean nameExists=false;
    	List<ErrorReport> finalErrReport=new ArrayList<ErrorReport>();
    	OozieJobDTO oozieJobDTO=new OozieJobDTO();
    	oozieJobDTO.setUserId(userId.toString());
    	List<String> Ids=new ArrayList<String>();
    	String hadoopBaseDirectory=env.getProperty("baseDirectories.hadoopBaseDir");
		String linuxBaseDirectory=env.getProperty("baseDirectories.linuxBaseDir");
    	if(jobDetialsDTO.getId()!=null)
    	{
    		JobDetails jobExist=jobDetailsRepository.findByIdForDisplayAndTenantId(jobDetialsDTO.getId(),tenantId);
    		jobDetails.setId(jobExist.getId());
    		jobDet=jobDetailsRepository.findByJobNameAndTenantId(jobDetialsDTO.getJobName(),tenantId);
    		if(jobDet.size()==1 || jobDet.size()==0)
    			nameExists=true;

    	}
    	else
    	{
    		jobDet=jobDetailsRepository.findByJobNameAndTenantId(jobDetialsDTO.getJobName(),tenantId);
    		if(jobDet.size()==0)
    			nameExists=true;
    	}
    	log.info("verifying duplicate jobName :"+nameExists);
    	if(nameExists)
    	{

    		if(jobDetialsDTO.getProgramId()!=null)
    			jobDetails.setProgrammId(jobDetialsDTO.getProgramId());
    		if(jobDetialsDTO.getJobName()!=null)
    			jobDetails.setJobName(jobDetialsDTO.getJobName());
    		if(jobDetialsDTO.getJobDesc()!=null)
    			jobDetails.setJobDescription(jobDetialsDTO.getJobDesc());
    		if(jobDetialsDTO.getJobStatus()!=null)
    			jobDetails.setJobStatus(jobDetialsDTO.getJobStatus());
    		HashMap map=new HashMap();
    		if(jobDetialsDTO.getProgramId()!=null)
    		{
    			ApplicationPrograms app=applicationProgramsRepository.findOne(jobDetialsDTO.getProgramId());
    			if(app!=null && app.getQueueName()!=null)
        			oozieJobDTO.setQueueName(app.getQueueName());
        			else
        				oozieJobDTO.setQueueName("default");
    			if(app!=null && app.getId()!=null)
    			{

    				List<ProgParametersSets> pPSList=progParametersSetsRepository.findByProgramId(app.getId());
    				for(int i=0;i<pPSList.size();i++)
    				{
    					map.put("param"+i, pPSList.get(i).getParameterName());

    				}

    			}
    		}
    		else if(jobDetialsDTO.getProgramId()==null || jobDetialsDTO.getProgramId()==0)
    		{
    			log.info(" jobDetialsDTO.getProgramName(),tenantId"+ jobDetialsDTO.getProgramName()+tenantId);
    			ApplicationPrograms app=applicationProgramsRepository.findByPrgmNameAndTenantId( jobDetialsDTO.getProgramName(),tenantId);
    			if(app!=null && app.getQueueName()!=null)
    			oozieJobDTO.setQueueName(app.getQueueName());
    			else
    				oozieJobDTO.setQueueName("default");
    		if(app != null && app.getId()!=null )
    			jobDetails.setProgrammId(app.getId());
    			log.info("app"+app);
    			if(app!=null && app.getId()!=null)
    			{
    				
    				List<ProgParametersSets> pPSList=progParametersSetsRepository.findByProgramId(app.getId());
    				log.info("pPSList"+pPSList);
    				for(int i=0;i<pPSList.size();i++)
    				{
    					map.put("param"+i, pPSList.get(i).getParameterName());

    				}

    			}

    		}
    		log.info("map obj:"+map);
    		if(jobDetialsDTO.getProgramId()!=null)
    		{
    			ApplicationPrograms aprograms =  applicationProgramsRepository.findOne(jobDetialsDTO.getProgramId());
    			jobDetialsDTO.setProgramName(aprograms.getPrgmName());
    		}
    		if(jobDetialsDTO.getParameters() != null ) {
    			List<Parameters> parameter=jobDetialsDTO.getParameters();
    			for(int i=0;i<parameter.size();i++)
    			{	if(map.get("param0")!=null)
    				if(parameter.get(i).getParamName().equalsIgnoreCase(map.get("param0").toString()))
    					jobDetails.setParameterArguments1(parameter.get(i).getSelectedValue());
    				if(map.get("param1")!=null)
    				if(parameter.get(i).getParamName().equalsIgnoreCase(map.get("param1").toString()))
    				{
    					if(jobDetialsDTO.getProgramName()!=null &&jobDetialsDTO.getProgramName().equalsIgnoreCase("Reporting")){
    						String val=parameter.get(i).getSelectedValue();
	    					JSONParser jsonParser = new JSONParser();
	    					JSONObject jsonObject = (JSONObject)jsonParser.parse(val);
	    					Reports repData=reportsRepository.findByTenantIdAndIdForDisplay(tenantId, parameter.get(0).getSelectedValue().toString());
    						String filterParamPath=reportsService.FileWriteHDFS(repData.getId(), jsonObject, "params", tenantId);
    						jobDetails.setParameterArguments2(filterParamPath);
    					}
    					else if(parameter.get(i).getSelectedValue() == null && 
    							jobDetialsDTO.getProgramName()!=null &&
    							jobDetialsDTO.getProgramName().equalsIgnoreCase("Reconciliation"))
        				{
        					String ruleIds = "";
        					RuleGroup ruleGroup = ruleGroupRepository.findByIdForDisplayAndTenantId(jobDetails.getParameterArguments1(),tenantId);
        					log.info("ruleGroup "+ruleGroup.toString());
        					List<BigInteger> ruleIdsBigInt = ruleGroupDetailsRepository.fetchByRuleGroupIdAndTenantIds(ruleGroup.getId(),tenantId);
        					
        					for(BigInteger id: ruleIdsBigInt)
        					{
        						ruleIds = ruleIds+id+",";
        					}
        					
        					ruleIds = ruleIds.trim();
        					if(ruleIds.endsWith(","))
        					{
        						ruleIds = ruleIds.substring(0,ruleIds.length()-1);
        					}
        					
    						jobDetails.setParameterArguments2(ruleIds );
        				}
    					else{
    						jobDetails.setParameterArguments2(parameter.get(i).getSelectedValue());
    					}
    					/*if(jobDetialsDTO.getProgramId()!=null)
     				{
     					log.info("parameter.get(i).getSelectedValue() :"+parameter.get(i).getSelectedValue());
     					ApplicationPrograms prgName=applicationprogramsRepository.findOne(jobDetialsDTO.getProgramId());
     					if(((prgName.getPrgmName().equalsIgnoreCase("Reconciliation") && prgName.getPrgmType().equalsIgnoreCase("shell")) ||(prgName.getPrgmType().equalsIgnoreCase("shell")&& prgName.getPrgmName().equalsIgnoreCase("Accounting")))&& jobDetails.getParameterArguments1()!=null)
     					{

     						if(parameter.get(i).getSelectedValue()==null)
     						{
     							log.info("jobDetails.getParameterArguments1() :"+jobDetails.getParameterArguments1());
     						//	RuleGroup rg=ruleGroupRepository.findByTenantIdAndName(tenantId, jobDetails.getParameterArguments1());
     						//	log.info("rg :"+rg);
     							List<BigInteger> ruleIds=ruleGroupDetailsRepository.fetchRuleIdsByGroupAndTenantId(Long.valueOf(jobDetails.getParameterArguments1()),tenantId);
     							log.info("ruleIds :"+ruleIds);
     							if(ruleIds.size()>0)
     							{
     								log.info("ruleIds.toString() :"+ruleIds.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", ""));
     								jobDetails.setParameterArguments2(ruleIds.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", ""));
     							}
     						}
     						else
     						{
     							jobDetails.setParameterArguments2(parameter.get(i).getSelectedValue());
     						}

     					}
     					else
     					{
     						log.info("get arg2: "+parameter.get(i).getSelectedValue());
         					jobDetails.setParameterArguments2(parameter.get(i).getSelectedValue());
         				}
     				}*/


    				}
    				if(map.get("param2")!=null)
    				if(parameter.get(i).getParamName().equalsIgnoreCase(map.get("param2").toString()))
    					jobDetails.setParameterArguments3(parameter.get(i).getSelectedValue());
    				if(map.get("param3")!=null)
    				if(parameter.get(i).getParamName().equalsIgnoreCase(map.get("param3").toString()))
    					jobDetails.setParameterArguments4(parameter.get(i).getSelectedValue());
    				if(map.get("param4")!=null)
    				if(parameter.get(i).getParamName().equalsIgnoreCase(map.get("param4").toString()))
    					jobDetails.setParameterArguments5(parameter.get(i).getSelectedValue());
    				if(map.get("param5")!=null)
    				if(parameter.get(i).getParamName().equalsIgnoreCase(map.get("param5").toString()))
    					jobDetails.setParameterArguments6(parameter.get(i).getSelectedValue());
    				if(map.get("param6")!=null)
    				if(parameter.get(i).getParamName().equalsIgnoreCase(map.get("param6").toString()))
    					jobDetails.setParameterArguments7(parameter.get(i).getSelectedValue());
    				if(map.get("param7")!=null)
    				if(parameter.get(i).getParamName().equalsIgnoreCase(map.get("param7").toString()))
    					jobDetails.setParameterArguments8(parameter.get(i).getSelectedValue());
    				if(map.get("param8")!=null)
    				if(parameter.get(i).getParamName().equalsIgnoreCase(map.get("param8").toString()))
    					jobDetails.setParameterArguments9(parameter.get(i).getSelectedValue());
    				if(map.get("param9")!=null)
    				if(parameter.get(i).getParamName().equalsIgnoreCase(map.get("param9").toString()))
    					jobDetails.setParameterArguments10(parameter.get(i).getSelectedValue());
    				}
    		}
    		if(jobDetialsDTO.getMaxConsecutiveFails()!=null)
    			jobDetails.setMaxConsecutiveFails(jobDetialsDTO.getMaxConsecutiveFails());
    		if(jobDetialsDTO.getSendFailAlerts()!=null)
    			jobDetails.setSendFailAlerts(jobDetialsDTO.getSendFailAlerts());
    		jobDetails.setCreatedBy(userId);
    		jobDetails.setLastUpdatedBy(userId);
    		jobDetails.setCreationDate(ZonedDateTime.now());
    		jobDetails.setLastUpdatedDate(ZonedDateTime.now());
    		jobDetails.setTenantId(tenantId);
    		jobDetails.setEnable(true);
    		if(jobDetialsDTO.getScheStartDate()!=null)
    			jobDetails.setStartDate(jobDetialsDTO.getScheStartDate());
    		if(jobDetialsDTO.getScheEndDate()!=null && !jobDetialsDTO.getScheEndDate().toString().isEmpty() && jobDetialsDTO.getScheEndDate().toString() != "")
    			jobDetails.setEndDate(jobDetialsDTO.getScheEndDate());
    		JobDetails job=jobDetailsRepository.save(jobDetails);
            /** to update id for display column**/    	
    		String idForDisplay = IDORUtils.computeFrontEndIdentifier(job.getId().toString());
    		job.setIdForDisplay(idForDisplay);
    		job = jobDetailsRepository.save(job);
    		log.info("job :"+job);
    		if(job.getId()!=null)
    		{
    			/*List<SchedulerDetails> schedularDetList=schedularDetailsRepository.findByJobId(job.getId());
     			if(schedularDetList.size()>0)
     			{
     				schedularDetailsRepository.delete(schedularDetList);
     			}*/
    			//Error Report after saving job 

    			ErrorReport jobErrorReport=new ErrorReport();
    			jobErrorReport.setTaskName("Job save");
    			jobErrorReport.setTaskStatus("Success");
    			jobErrorReport.setDetails(job.getIdForDisplay());
    			finalErrReport.add(jobErrorReport);



    			List<SchedulerDetails> schedularList=new ArrayList<SchedulerDetails>();
    			List<Frequencies> frequencies=jobDetialsDTO.getFrequencies();
    			List<Long> freqIdList=new ArrayList<Long>();
    			//getting frequencies
    			for(int i=0;i<frequencies.size();i++)
    			{
    				if(frequencies.get(i).getId()!=null)
    				{
    					freqIdList.add(frequencies.get(i).getId());
    				}
    			}
    			log.info("freqIdList :"+freqIdList);
    			List<Long> delList=new ArrayList<Long>();
    			List<SchedulerDetails> schedularDetailList=schedularDetailsRepository.findByJobId(job.getId());
    			List<Long> schIds=new ArrayList<Long>();
    			for(SchedulerDetails scheduler:schedularDetailList)
    			{

    				schIds.add(scheduler.getId());
    			}
    			log.info("schIds :"+schIds);
    			for(int l=0;l<schIds.size();l++)
    			{
    				if(freqIdList.contains(schIds.get(l)))
    				{

    				}
    				else
    				{
    					log.info("in else");
    					delList.add(schIds.get(l));
    					SchedulerDetails schDetail=schedularDetailsRepository.findOne(schIds.get(l));
    					schDetail.setDeleteFlag(true);
    					schDetail.setLastUpdatedBy(userId);
    					schDetail.setLastUpdatedDate(ZonedDateTime.now());
    					schDetail.setIsViewed(false);
    					schedularDetailsRepository.save(schDetail);
    					if(schDetail.getOozieJobId()!=null)
    						try {
    							String status=oozieService.KillOzieParentJob(schDetail.getOozieJobId());
    							log.info("JobStatus :"+status);
    						} catch (OozieClientException e) {
    							// TODO Auto-generated catch block
    							log.info("Failed to kill job");
    							e.printStackTrace();
    						}
    					//schedularList.add(schDetail);

    				}
    			}
    			log.info("delList :"+delList);

    			//getting id of existing schedulers for particular job and updating
    			List<SchedulerDetails> schedularDetails=schedularDetailsRepository.findByJobId(job.getId());
    			if(schedularDetails.size()>0)
    			{
    				/*for(SchedulerDetails scheduler:schedularDetails)
     				{*/


    				//update

    				for(int j=0;j<frequencies.size();j++)
    				{
    					if(frequencies.get(j).getId()!=null)
    					{
    						//already exists
    						log.info("frequencies.get(j).getId() :"+frequencies.get(j).getId());
    						SchedulerDetails schDetail=schedularDetailsRepository.findOne(frequencies.get(j).getId());
    						schDetail.setDate(frequencies.get(j).getDate());
    						schDetail.setLastUpdatedBy(userId);
    						schDetail.setLastUpdatedDate(ZonedDateTime.now());
    						schedularList.add(schDetail);

    					}
    					else
    					{
    						log.info("new creation for existing job Id");
    						SchedulerDetails schedularr=new SchedulerDetails();
    						schedularr.setJobId(job.getId());
    						int count=schedularDetails.size();

    						if(jobDetialsDTO.getScheStartDate()!=null)
    						{
    							schedularr.setStartDate(jobDetialsDTO.getScheStartDate());
    						}
    						if(jobDetialsDTO.getScheEndDate()!=null)
    						{
    							schedularr.setEndDate(jobDetialsDTO.getScheEndDate());
    						}
    						if(frequencies.get(j).getType()!=null)
    							schedularr.setFrequency(frequencies.get(j).getType());
    						if(frequencies.get(j).getOccurance()!=null)
    							schedularr.setFrequencyValue(frequencies.get(j).getOccurance());
    						if(frequencies.get(j).getTime()!=null)
    						{
    							log.info("frequencies.get(j).getTime() :"+frequencies.get(j).getTime());
    							//log.info("frequencies.get(j).getTime().substring(10, 18) :"+frequencies.get(j).getTime().substring(11, 19));
    							schedularr.setTime(frequencies.get(j).getTime());
    						}
    						if(frequencies.get(j).getDay()!=null)
    							schedularr.setDayOf(frequencies.get(j).getDay().toString().replace("[", "").replace("]", "").replaceAll("\\s",""));
    						if(frequencies.get(j).getMinutes()!=null)
    							schedularr.setMinutes(frequencies.get(j).getMinutes());
    						if(frequencies.get(j).getHours()!=null)
    							schedularr.setHours(frequencies.get(j).getHours());
    						if(frequencies.get(j).getMonth()!=null)
    							schedularr.setMonth(frequencies.get(j).getMonth().toString().replace("[", "").replace("]", "").replaceAll("\\s",""));
    						if(frequencies.get(j).getWeekDay()!=null)
    							schedularr.setWeekDay(frequencies.get(j).getWeekDay().toString().replace("[", "").replace("]", "").replaceAll("\\s",""));
    						if(frequencies.get(j).getDate()!=null)
    						{
    							schedularr.setDate(frequencies.get(j).getDate());
    						}
    						schedularr.setTenantId(tenantId);
    						schedularr.setCreatedBy(userId);
    						schedularr.setLastUpdatedBy(userId);
    						schedularr.setCreationDate(ZonedDateTime.now());
    						schedularr.setLastUpdatedDate(ZonedDateTime.now());
    						schedularr.setSchedulerName(job.getJobName()+"_"+count);
    						schedularList.add(schedularr);
    						count++;
    					}
    				}






    			}
    			else

    			{

    				for(int j=0;j<frequencies.size();j++)
    				{
    					log.info("Setting new scheduler information");

    					SchedulerDetails schedular=new SchedulerDetails();
    					schedular.setJobId(job.getId());
    					int count=1;

    					if(jobDetialsDTO.getScheStartDate()!=null)
    					{
    						schedular.setStartDate(jobDetialsDTO.getScheStartDate());
    					}
    					if(jobDetialsDTO.getScheEndDate()!=null)
    					{
    						schedular.setEndDate(jobDetialsDTO.getScheEndDate());
    					}
    					if(frequencies.get(j).getType()!=null)
    						schedular.setFrequency(frequencies.get(j).getType());
    					if(frequencies.get(j).getOccurance()!=null)
    						schedular.setFrequencyValue(frequencies.get(j).getOccurance());
    					if(frequencies.get(j).getTime()!=null)
    					{
    						log.info("frequencies.get(j).getTime() :"+frequencies.get(j).getTime());
    						//log.info("frequencies.get(j).getTime().substring(10, 18) :"+frequencies.get(j).getTime().substring(11, 19));
    						schedular.setTime(frequencies.get(j).getTime());
    					}
    					if(frequencies.get(j).getDay()!=null)
    						schedular.setDayOf(frequencies.get(j).getDay().toString().replace("[", "").replace("]", "").replaceAll("\\s",""));
    					if(frequencies.get(j).getMinutes()!=null)
    						schedular.setMinutes(frequencies.get(j).getMinutes());
    					if(frequencies.get(j).getHours()!=null)
    						schedular.setHours(frequencies.get(j).getHours());
    					if(frequencies.get(j).getMonth()!=null)
    						schedular.setMonth(frequencies.get(j).getMonth().toString().replace("[", "").replace("]", "").replaceAll("\\s",""));
    					if(frequencies.get(j).getWeekDay()!=null)
    						schedular.setWeekDay(frequencies.get(j).getWeekDay().toString().replace("[", "").replace("]", "").replaceAll("\\s",""));
    					if(frequencies.get(j).getDate()!=null)
    						schedular.setDate(frequencies.get(j).getDate());
    					schedular.setTenantId(tenantId);
    					schedular.setCreatedBy(userId);
    					schedular.setLastUpdatedBy(userId);
    					schedular.setCreationDate(ZonedDateTime.now());
    					schedular.setLastUpdatedDate(ZonedDateTime.now());
    					schedular.setSchedulerName(job.getJobName()+"_"+count);
    					schedular.setIsViewed(false);
    					schedularList.add(schedular);
    					count++;

    				}
    			}
    			List<SchedulerDetails> sch=schedularDetailsRepository.save(schedularList);
    			log.info("Saved scheduler information :"+sch);
    			
    			
    			

    			if(frequencies.size()==schedularList.size())
    			{
    				ErrorReport schErrorReport=new ErrorReport();
    				schErrorReport.setTaskName("Schedulers Save");
    				schErrorReport.setTaskStatus("Success");
    				schErrorReport.setDetails(sch.get(0).getId().toString());
    				finalErrReport.add(schErrorReport);

    			}
    			else
    			{
    				ErrorReport schErrorReport=new ErrorReport();
    				schErrorReport.setTaskName("Schedulers Save");
    				schErrorReport.setTaskStatus("Failure");
    				finalErrReport.add(schErrorReport);

    			}

    			for(int i=0;i<sch.size();i++)
    			{
    				
    				
    				String Freq="";
    				if(sch.get(i).getMinutes()!=null)
    					Freq=sch.get(i).getMinutes().toString()+" ";
    				else
    					Freq="* ";
    				if(sch.get(i).getHours()!=null && sch.get(i).getFrequency().equalsIgnoreCase("hourly"))
    				{
    					Freq=Freq+"0/"+sch.get(i).getHours().toString()+" ";
    				}
    				else if(sch.get(i).getHours()!=null)
    				{
    					Freq=Freq+sch.get(i).getHours().toString()+" ";
    				}
    				else
    					Freq=Freq+"* ";
    				if(sch.get(i).getDayOf()!=null)
    					Freq=Freq+sch.get(i).getDayOf().toString()+" ";
    				else
    					Freq=Freq+"* ";
    				if(sch.get(i).getMonth()!=null)
    					Freq=Freq+sch.get(i).getMonth()+" ";
    				else
    					Freq=Freq+"* ";
    				if(sch.get(i).getWeekDay()!=null)
    					Freq=Freq+sch.get(i).getWeekDay()+" ";
    				else
    					Freq=Freq+"*";

    				log.info("Freq :"+Freq);
    				sch.get(i).setFrequencyFormat(Freq);
					SchedulerDetails frequencySetting=schedularDetailsRepository.save(sch.get(i));


    				if(sch.get(i).getFrequency()!=null && sch.get(i).getFrequency().equalsIgnoreCase("minutes"))
    				{
    					Freq="0/"+Freq;
    					log.info("Freq if min:"+Freq);
    				}
    				
    				oozieJobDTO.setFrequency(Freq);
    				if(job.getProgrammId()!=null)
    				{
    					ApplicationPrograms Prg=applicationprogramsRepository.findOne(job.getProgrammId());
    					oozieJobDTO.setHdfsPath(hadoopBaseDirectory+Prg.getTargetPath());
    					if(Prg.getPrgmPath()!=null)
    						oozieJobDTO.setJarFilePath(hadoopBaseDirectory+Prg.getPrgmPath());
    					if(Prg.getPrgmOrClassName()!=null)
    						oozieJobDTO.setClassName(Prg.getPrgmOrClassName());
    					if(Prg.getPrgmType()!=null)
    						oozieJobDTO.setJobType(Prg.getPrgmType());
    					oozieJobDTO.setProgramName(Prg.getPrgmName());
    					if(Prg.getPrgmName().equalsIgnoreCase("Accounting") || Prg.getPrgmName().equalsIgnoreCase("Reconciliation") || Prg.getPrgmName().equalsIgnoreCase("Reconciliation and Approvals") || Prg.getPrgmName().equalsIgnoreCase("Accounting and Approvals")
    							|| Prg.getPrgmName().toLowerCase().startsWith("reconciliation"))
    						oozieJobDTO.setRuleType("NORMAL");
    					oozieJobDTO.setPath(linuxBaseDirectory+Prg.getGeneratedPath());
    				}
    				String startDate=jobDetialsDTO.getScheStartDate().toString();
    				log.info("Orginal startDate :"+startDate);
    				DateFormat utcFormat=new SimpleDateFormat();
    				DateFormat utcFormatEd=new SimpleDateFormat();
    				
    				String endDate=jobDetialsDTO.getScheEndDate().toString();
					log.info("Orginal endDate :"+endDate);
    				
                    String dtFrmat="yyyy-MM-ddTHH:mm:ssZ[UTC]";
                    String dtFrmatMS="yyyy-MM-ddTHH:mm:ss.SSSZ[UTC]";
    				if(startDate.length()==dtFrmat.length())
    				{
    				utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z[UTC]'");
    				utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    				}
    				
    				else if(startDate.length()==dtFrmatMS.length())
    				{
    				utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z[UTC]'");
    				utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    				}
    				
    				if(endDate.length()==dtFrmat.length())
    				{
    					utcFormatEd = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z[UTC]'");
    					utcFormatEd.setTimeZone(TimeZone.getTimeZone("UTC"));
    				}
    				
    				else if(endDate.length()==dtFrmatMS.length())
    				{
    					utcFormatEd = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z[UTC]'");
    					utcFormatEd.setTimeZone(TimeZone.getTimeZone("UTC"));
    				}

    				
    				
    				
    				Date date = null ;
    				try {
    					
    					date = utcFormat.parse(startDate);
    				} catch (ParseException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}

    				String timeFormat=env.getProperty("oozie.oozieServerTimeFormat");
    				log.info("timeFormat :"+timeFormat);
    				String timeZone=env.getProperty("oozie.oozieServerTimeZone");
    				log.info("timeZone :"+timeZone);
    				DateFormat tFormat = new SimpleDateFormat(timeFormat);
    				
    				TimeZone tZoneFormat = TimeZone.getTimeZone(timeZone);
    				tFormat.setTimeZone(tZoneFormat);
    				/*if(timeZone.equalsIgnoreCase("IST"))
    				    pstFormat = new SimpleDateFormat("yyyy-MM-dd'T0'HH:mm'+0530'");
    				else if(timeZone.equalsIgnoreCase("PDT"))
    					pstFormat = new SimpleDateFormat("yyyy-MM-dd'T0'HH:mm");*/
    				//pstFormat.setTimeZone(TimeZone.getTimeZone("IST"));

    				if(date != null) {
    					String finalStartDate=tFormat.format(date);
        				log.info("finalStartDate after parsing:"+finalStartDate);
        				oozieJobDTO.setStartDate(finalStartDate);
    				}
    			

    				if(sch.get(i).getEndDate()!=null)
    				{
    					//String endDate=sch.get(i).getEndDate().toString();
    					log.info("Orginal endDate :"+endDate);
    					Date dateEnd = null ;
    					try {
    						dateEnd = utcFormatEd.parse(endDate);
    					} catch (ParseException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					}
    					
    				
    					String finalEndDate=tFormat.format(dateEnd);
    					log.info("finalEndDate ater parsing:"+finalEndDate);

    					oozieJobDTO.setEndDate(finalEndDate);
    				}
    				
    				
    				
    				/** job frequency **/
    				if(timeZone.equalsIgnoreCase("PST"))
    				{
    					if(sch.get(i).getFrequency().equalsIgnoreCase("WEEKLY"))
    					{
    						List<String> weekDays=frequencies.get(i).getWeekDay();
    						String finalWD="";
    						//String finalDateTime="";
    						for(int d=0;d<weekDays.size();d++)
    						{

    							String freqTime=frequencies.get(i).getFrequencyTime().toString();
    							String time=freqTime.substring(11, 19);
    							log.info("time :"+time);
    							String dateTime=oozieService.weekDayDate(weekDays.get(d), time, timeFormat, timeZone);
    							log.info("dateTime :"+dateTime);
    							//if(d==0)
    							//	finalDateTime=dateTime;
    							LocalDate Ld=LocalDate.parse(dateTime.substring(0, 10));
    							finalWD=finalWD+Ld.getDayOfWeek().toString().substring(0, 3).toLowerCase()+",";
    							// LocalDate nextWed1 = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.));

    						}

    						ZonedDateTime frequencyDateTime=frequencies.get(i).getFrequencyTime();
    						Date freqDTtime=null;
    						try {
    							freqDTtime = utcFormatEd.parse(frequencyDateTime.toString());
    						} catch (ParseException e) {
    							// TODO Auto-generated catch block
    							e.printStackTrace();
    						}
    						String formattedFreqDTtime=tFormat.format(freqDTtime);
    						log.info("formattedFreqDTtime :"+formattedFreqDTtime);
    						
    						HashMap frequencyMap=oozieService.frequencyFormat(formattedFreqDTtime);
    						
    						
    						log.info("finalWD :"+finalWD);
    						finalWD=finalWD.substring(0,finalWD.length()-1);
    						log.info("after removing coma :"+finalWD);

    						//HashMap frequencyMap=oozieService.frequencyFormat(finalDateTime);
    						String minOfFreq=frequencyMap.get("min").toString();
    						String hoursOfFreq=frequencyMap.get("hours").toString();
    						String dateOfFreq=frequencyMap.get("date").toString();
    						String monthOfFreq=frequencyMap.get("month").toString();

    						String finalFreq=minOfFreq+" "+hoursOfFreq+" * * "+finalWD;

    						sch.get(i).setFrequencyFormat(finalFreq);
    						//log.info("sch.get(i)  in weekly :"+sch.get(i));
    						SchedulerDetails frequencySet=schedularDetailsRepository.save(sch.get(i));
    						//log.info("frequencySet :"+frequencySet);
    						oozieJobDTO.setFrequency(finalFreq);
    						log.info("oozieJobDTO.getFreq :"+oozieJobDTO.getFrequency());

    					}


    					if(frequencies.get(i).getFrequencyTime()!=null && !sch.get(i).getFrequency().equalsIgnoreCase("WEEKLY"))
    					{
    						ZonedDateTime frequencyDateTime=frequencies.get(i).getFrequencyTime();
    						Date freqDTtime=null;
    						try {
    							freqDTtime = utcFormatEd.parse(frequencyDateTime.toString());
    						} catch (ParseException e) {
    							// TODO Auto-generated catch block
    							e.printStackTrace();
    						}
    						String formattedFreqDTtime=tFormat.format(freqDTtime);
    						log.info("formattedFreqDTtime :"+formattedFreqDTtime);
    						HashMap frequencyMap=oozieService.frequencyFormat(formattedFreqDTtime);
    						log.info("frequencyMap :"+frequencyMap);
    						String finalFreq="";
    						String minOfFreq=frequencyMap.get("min").toString();
    						String hoursOfFreq=frequencyMap.get("hours").toString();
    						String dateOfFreq=frequencyMap.get("date").toString();
    						String monthOfFreq=frequencyMap.get("month").toString();
    						if(sch.get(i).getFrequency().equalsIgnoreCase("ONETIME"))
    						{
    							finalFreq=minOfFreq+" "+hoursOfFreq+" "+dateOfFreq+" "+monthOfFreq+" *";
    						}
    						if(sch.get(i).getFrequency().equalsIgnoreCase("DAILY"))
    						{
    							finalFreq=minOfFreq+" "+hoursOfFreq+" * * *";
    						}
    						if(sch.get(i).getFrequency().equalsIgnoreCase("MONTHLY"))
    						{
    							finalFreq=minOfFreq+" "+hoursOfFreq+" "+dateOfFreq+" "+sch.get(i).getMonth()+" *";
    						}
    						sch.get(i).setFrequencyFormat(finalFreq);
    						SchedulerDetails frequencySet=schedularDetailsRepository.save(sch.get(i));
    						oozieJobDTO.setFrequency(finalFreq);
    						log.info("oozieJobDTO.getFreq :"+oozieJobDTO.getFrequency());
    					}
    				}
    				
    				

    				if(job.getJobName()!=null)
    				{
    					String jobNm=job.getJobName().replaceAll("\\s+","").replaceAll("[^a-zA-Z0-9]","");
    					log.info("jobName after replacing spaces :"+jobNm);
    					oozieJobDTO.setJobName(jobNm);
    					oozieJobDTO.setJobId(job.getId());
    				}
    				//Build Job
    				if(sch.get(i).getFrequency()!=null)
    					oozieJobDTO.setFrequencyType(sch.get(i).getFrequency());
    				oozieJobDTO .setProgramId(job.getProgrammId());
    				oozieService.BuildOozieJob(oozieJobDTO,tenantId,userId,request);
    				//Iniatiate Job
    				log.info("queueName:"+oozieJobDTO.getQueueName());
    				String JobId="";
    				JobId =	oozieService.InitiateOozieJob(oozieJobDTO);
    				String Id="";
    				if(JobId!=null && !JobId.isEmpty())
    				{
    					Id=JobId;
    					sch.get(i).setStatus("Submitted");
    					ErrorReport iniErrorReport=new ErrorReport();
    					iniErrorReport.setTaskName("Initiate Job");
    					iniErrorReport.setTaskStatus("Success");
    					iniErrorReport.setDetails("Scheduler Initiated SuccessFully with Reference "+JobId);
    					finalErrReport.add(iniErrorReport);
    				}
    				else
    				{
    					sch.get(i).setStatus("Error");
    					ErrorReport iniErrorReport=new ErrorReport();
    					iniErrorReport.setTaskName("Initiate Job");
    					iniErrorReport.setTaskStatus("Failure");
    					finalErrReport.add(iniErrorReport);
    				}
    				Ids.add(Id);
    				sch.get(i).setOozieJobId(JobId);
    				schedularDetailsRepository.save(sch);

    			}
    			try {
					List<LinkedHashMap> getAllSchedulersList=schedulerDetailsResource.getAllSchedulersList(request, null, null, null, job.getIdForDisplay(), null, null);
					log.info("getAllSchedulersList :"+getAllSchedulersList.get(0));
					HashMap tenantIdForDisp=userJdbcService.getTenantIdForDisplayByTenantId(tenantId);
					webSocketService.webSocketConnectionService(getAllSchedulersList.get(0),"jobs/"+tenantIdForDisp.get("tenantIdForDisplay"));
					
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OozieClientException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    		else
    		{
    			ErrorReport jobErrorReport=new ErrorReport();
    			jobErrorReport.setTaskName("Job save");
    			jobErrorReport.setTaskStatus("Failure");
    			finalErrReport.add(jobErrorReport);

    		}
    		
    	}
    	
    	
    	return finalErrReport;

     }
         
/**
 * @author ravali
 * @param Id
 * @param tenantId
 * @return job details and respective schedulers
 * @throws OozieClientException 
 */
    @GetMapping("/jobDetailsAndSchedularByJobId")
    @Timed
    
    public JobDetialsDTO getJobDetailsAndSchedular(HttpServletRequest request,@RequestParam String Id) throws OozieClientException
    {

    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	log.info("Request to get jobDetailsAndSchedularByJobId for tenant: "+tenantId);
    	JobDetialsDTO jobDetailsDTO=new JobDetialsDTO();
    	JobDetails job=jobDetailsRepository.findByIdForDisplayAndTenantId(Id,tenantId);
    	JobDetails jobDetails=jobDetailsRepository.findByTenantIdAndId(tenantId,job.getId());
    	
    	if(jobDetails.getId()!=null)
    		jobDetailsDTO.setId(jobDetails.getIdForDisplay());
    	if(jobDetails.getProgrammId()!=null)
    		jobDetailsDTO.setProgramId(jobDetails.getProgrammId());
    	if(jobDetails.getJobName()!=null)
    		jobDetailsDTO.setJobName(jobDetails.getJobName());
    	if(jobDetails.getJobDescription()!=null)
    		jobDetailsDTO.setJobDesc(jobDetails.getJobDescription());

    	if(jobDetails.getJobStatus()!=null)
    		jobDetailsDTO.setJobStatus(jobDetailsDTO.getJobStatus());
    	List<String> paramList=new ArrayList<String>();
    	
    	List<ProgParametersSets> paramsetsList=progParametersSetsRepository.findByProgramIdAndStatusAndRequestFormIsTrue(jobDetails.getProgrammId(),"ACTIVE");
log.info("paramsetsList :"+paramsetsList.size());
    	if(jobDetails.getParameterArguments1()!=null && !jobDetails.getParameterArguments1().isEmpty())
    		paramList.add(jobDetails.getParameterArguments1());
    	else if(1<=paramsetsList.size())
    		paramList.add("null");
    	if(jobDetails.getParameterArguments2()!=null && !jobDetails.getParameterArguments2().isEmpty())
    		paramList.add(jobDetails.getParameterArguments2());
    	else if(2<=paramsetsList.size())
    		paramList.add("null");
    	if(jobDetails.getParameterArguments3()!=null && !jobDetails.getParameterArguments3().isEmpty())
    		paramList.add(jobDetails.getParameterArguments3());
    	else if(3<=paramsetsList.size())
    		paramList.add("null");
    	if(jobDetails.getParameterArguments4()!=null && !jobDetails.getParameterArguments4().isEmpty())
    		paramList.add(jobDetails.getParameterArguments4());
    	else if(4<=paramsetsList.size())
    		paramList.add("null");
    	if(jobDetails.getParameterArguments5()!=null && !jobDetails.getParameterArguments5().isEmpty())
    		paramList.add(jobDetails.getParameterArguments5());
    	else if(5<=paramsetsList.size())
    		paramList.add("null");
    	if(jobDetails.getParameterArguments6()!=null && !jobDetails.getParameterArguments6().isEmpty())
    		paramList.add(jobDetails.getParameterArguments6());
    	else if(6<=paramsetsList.size())
    		paramList.add("null");
    	if(jobDetails.getParameterArguments7()!=null && !jobDetails.getParameterArguments7().isEmpty())
    		paramList.add(jobDetails.getParameterArguments7());
    	else if(7<=paramsetsList.size())
    		paramList.add("null");
    	if(jobDetails.getParameterArguments8()!=null && !jobDetails.getParameterArguments8().isEmpty())
    		paramList.add(jobDetails.getParameterArguments8());
    	else if(8<=paramsetsList.size())
    		paramList.add("null");
    	if(jobDetails.getParameterArguments9()!=null && !jobDetails.getParameterArguments9().isEmpty())
    		paramList.add(jobDetails.getParameterArguments9());
    	else if(9<=paramsetsList.size())
    		paramList.add("null");
    	if(jobDetails.getParameterArguments10()!=null && !jobDetails.getParameterArguments10().isEmpty())
    		paramList.add(jobDetails.getParameterArguments10());
    	else if(10<=paramsetsList.size())
    		paramList.add("null");

    	List<Parameters> parameterList=new ArrayList<Parameters>();
    	log.info("paramList :"+paramList);
    	for(int i=0;i<paramList.size();i++)
    	{
    		Parameters parameter=new Parameters();
    		
    		if(paramsetsList.size()>0)
    		{
    			try
    			{
    			ProgParametersSets pps = paramsetsList.get(i);
    			parameter.setParamId(pps.getId());
    			parameter.setParamName(pps.getParameterName());
        		parameter.setSelectedValue(paramList.get(i));
        		
        		String[] parList=paramList.get(i).split(",");
        		List<String> paramValues=new ArrayList<String>();
        		
        		for(String par:parList)
        		{
        			log.info("parList.par"+par);
        		log.info("Rest api for fetching rule names, rule ids for the prog parameter id "+ jobDetails.getProgrammId()+" and tenant id "+tenantId);
            
            	
            	if(pps != null)
            	{
            		/*String dbUrl=env.getProperty("spring.datasource.url");
            		String[] parts=dbUrl.split("[\\s@&?$+-]+");
            		String host = parts[0].split("/")[2].split(":")[0];
            		String schemaName=parts[0].split("/")[3];
            		String userName = env.getProperty("spring.datasource.username");
            		String password = env.getProperty("spring.datasource.password");
            		String jdbcDriver = env.getProperty("spring.datasource.jdbcdriver");*/
            		
            		
            		
            		Connection conn = null;
            		Statement stmt = null;
            		ResultSet rs=null;
            		
            		DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
            		conn = ds.getConnection();	
            		
            		try{
            		//	Class.forName(jdbcDriver);
            		//	conn = DriverManager.getConnection(dbUrl, userName, password);
            			log.info("Connected database successfully...");
            			stmt = conn.createStatement();
            			log.info("Entity Name: "+ pps.getEntityName()+", Entity Column: "+ pps.getEntityColumn()+", Bind Value: "+ pps.getBindValue()+", Dependency Id: "+ pps.getDependency());
            			String query = "SELECT * FROM ";
            			if(pps.getDependency() != null)
            			{
            				//get previous selected value
            				String prevVal = "";
            				if(paramList.get(i-1) != null)
            				prevVal = paramList.get(i-1);
            				if(prevVal != null)
            				{
            					if(prevVal.contains(','+""))
            					{
            						String [] strArr = prevVal.split(",");
            						String framedStr = "";
            						for(int s=0;s<strArr.length;s++)
            						{
            							 strArr[s] = "'" + strArr[s] + "'";
            						}
            						log.info("strArr :"+strArr);
            						for(int s=0;s<strArr.length;s++)
            						{
            							if(s == (strArr.length-1))
            								framedStr = framedStr + strArr[s];
            							else
            								
            							framedStr = framedStr + strArr[s] + ",";
            						}
            						log.info("framedStr"+framedStr);
            						query = query+pps.getEntityName()+" WHERE "+pps.getBindValue()+"='"+par+"' and tenant_id = "+ tenantId + " and "+pps.getDependency()+" in ( "+framedStr+")";
            					}
            					else
            						query = query+pps.getEntityName()+" WHERE "+pps.getBindValue()+"='"+par+"' and tenant_id = "+ tenantId + " and "+pps.getDependency()+" = '"+prevVal+"'";
            					
            				}
            					
            				else
            					query = query+pps.getEntityName()+" WHERE "+pps.getBindValue()+"='"+par+"' and tenant_id = "+ tenantId ; 
            			}
            			else
            			{
            				query = query+pps.getEntityName()+" WHERE "+pps.getBindValue()+"='"+par+"' and tenant_id = "+ tenantId;
            			}

            			log.info("SQL: "+ query);
            			rs = stmt.executeQuery(query);
            			ResultSetMetaData rsmd = rs.getMetaData();
            			int colCount = rsmd.getColumnCount();

            			while(rs.next()){
            				HashMap hm = new HashMap();
            				/*	      	   		hm.put("id", rs.getLong(3));
        	      	   		hm.put("ruleName", rs.getString(12));*/
            				for(int j=1; j<=colCount; j++)
            				{



            					if(pps.getEntityColumn().equalsIgnoreCase(rsmd.getColumnName(j)))
            					{
            						//hm.put(rsmd.getColumnName(j), rs.getString(j));
            						log.info("rs.getString(j) :"+rs.getString(j));
            						paramValues.add(rs.getString(j));
            					}
            					if(pps.getBindValue().equalsIgnoreCase(rsmd.getColumnName(j)))
            					{
            						//hm.put(rsmd.getColumnName(j), rs.getString(j));
            					}
            				}





            			}

            		}
            		catch(SQLException e)
            		{
            			log.info("Oops.. It seems like view "+pps.getEntityName()+" doesn't exist.");
            			log.info("Exception: "+ e);
            		}
            		
            		
            		if(rs!=null)
            			rs.close();
            		if(stmt!=null)
            			stmt.close();
            		if(conn!=null)
            			conn.close();
            	}
            	
            	log.info("paramValues.toString() :"+paramValues.toString());
            	parameter.setSelectedValueName(paramValues.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
    			}
        		
            	parameterList.add(parameter);
            	
    			}
            		catch(Exception e)
            		{
            			log.info("Exception: "+e);
            		}
            	}
    	}
    
    	jobDetailsDTO.setParameters(parameterList);

    	if(jobDetails.getMaxConsecutiveFails()!=null)
    		jobDetailsDTO.setMaxConsecutiveFails(jobDetails.getMaxConsecutiveFails());
    	if(jobDetails.isSendFailAlerts()!=null)
    		jobDetailsDTO.setSendFailAlerts(jobDetails.isSendFailAlerts());



    	List<SchedulerDetails> schedularList=schedularDetailsRepository.findByJobIdAndDeleteFlagIsNull(jobDetails.getId());

    	List<Frequencies> frequencies=new ArrayList<Frequencies>();
    	for(int j=0;j<schedularList.size();j++)
    	{
    		Frequencies freq=new Frequencies();
    		if(schedularList.get(j).getStartDate()!=null)
    			jobDetailsDTO.setScheStartDate(schedularList.get(j).getStartDate());
    		if(schedularList.get(j).getEndDate()!=null)
    			jobDetailsDTO.setScheEndDate(schedularList.get(j).getEndDate());
    		if(schedularList.get(j).getFrequency()!=null)
    			freq.setType(schedularList.get(j).getFrequency());
    		if(schedularList.get(j).getFrequencyValue()!=null)
    			freq.setOccurance(schedularList.get(j).getFrequencyValue());
    		if(schedularList.get(j).getTime()!=null)
    			freq.setTime(schedularList.get(j).getTime());
    		if(schedularList.get(j).getOozieJobId()!=null && !schedularList.get(j).getOozieJobId().isEmpty())
    		freq.setOozieJobId(schedularList.get(j).getOozieJobId());
    		if(schedularList.get(j).getOozieJobId()!=null)
    		{
    			String status=oozieService.getStatusOfOozieJobId(schedularList.get(j).getOozieJobId());
    			freq.setStatus(status);
    		}
    		
    		
    		freq.setId(schedularList.get(j).getId());
    		if(schedularList.get(j).getDayOf()!=null)
    		{
    			List<String> myList = new ArrayList<String>(Arrays.asList(schedularList.get(j).getDayOf().split(",")));
    			freq.setDay(myList);
    		}
    		if(schedularList.get(j).getTime()!=null)
    			freq.setTime(schedularList.get(j).getTime());

    		if(schedularList.get(j).getMinutes()!=null)
    			freq.setMinutes(schedularList.get(j).getMinutes());
    		if(schedularList.get(j).getHours()!=null)
    			freq.setHours(schedularList.get(j).getHours());
    		if(schedularList.get(j).getMonth()!=null)
    		{
    			List<String> myList = new ArrayList<String>(Arrays.asList(schedularList.get(j).getMonth().split(",")));
    			freq.setMonth(myList);
    		}
    		if(schedularList.get(j).getWeekDay()!=null)
    		{
    			List<String> myList = new ArrayList<String>(Arrays.asList(schedularList.get(j).getWeekDay().split(",")));
    			freq.setWeekDay(myList);
    		}
    		if(schedularList.get(j).getDate()!=null)
    			freq.setDate(schedularList.get(j).getDate());
    		frequencies.add(freq);
    	}
    	
    	jobDetailsDTO.setFrequencies(frequencies);
    
    	return jobDetailsDTO;
    			

    }
    
    /**
     * @author ravali, kiran
     * @param tenantId
     * @return List of job details
     */
    /*@GetMapping("/getAllJobDetailsByTenantId")
	@Timed
	public List<HashMap> getAllJobDetailsByTenantId(@RequestParam Long tenantId,@RequestParam(value = "page" , required = false) Integer offset, 
			@RequestParam(required = false) String sortColName, @RequestParam(required = false) String sortOrder,
			@RequestParam(value = "per_page", required = false) Integer limit,HttpServletResponse response)
			{
		log.info("Request Rest to get all job details By tenantId");
		List<HashMap> finalMap=new ArrayList<HashMap>();
		List<JobDetails> jobDetailsList=new ArrayList<JobDetails>();
		PaginationUtil paginationUtil=new PaginationUtil();

		int maxlmt=paginationUtil.MAX_LIMIT;
		int minlmt=paginationUtil.MIN_OFFSET;
		log.info("maxlmt: "+maxlmt);
		Page<JobDetails> page = null;
		//HttpHeaders headers = null;
		
		if(sortOrder==null)
			sortOrder="Descending";
		if(sortColName==null)
			sortColName="id";

		List<JobDetails> jobDetailsListCnt = jobDetailsRepository.findByTenantIdAndOrderById(tenantId);
		response.addIntHeader("X-COUNT", jobDetailsListCnt.size());

		if(limit==null || limit<minlmt){
			jobDetailsList = jobDetailsRepository.findByTenantIdAndOrderById(tenantId);

			limit = jobDetailsList.size();
		}
		if(limit == 0 )
		{
			limit = paginationUtil.DEFAULT_LIMIT;
		}
		if(offset == null || offset == 0)
		{
			offset = paginationUtil.DEFAULT_OFFSET;
		}
		if(limit>maxlmt)
		{
			log.info("input limit exceeds maxlimit");
			page = jobDetailsRepository.findByTenantId(tenantId,PaginationUtil.generatePageRequestWithSortColumn(offset, limit, sortOrder, sortColName));
		}
		else{
			log.info("input limit is within maxlimit");
			page = jobDetailsRepository.findByTenantId(tenantId,PaginationUtil.generatePageRequestWithSortColumn(offset, limit, sortOrder, sortColName));

		}

		if(jobDetailsList.size()==0)
		{
			//jobDetailsList = jobDetailsRepository.findByTenantIdAndOrderById(tenantId);
			jobDetailsList=page.getContent();
		}

		for(JobDetails jobDetails:jobDetailsList)
		{
			HashMap map=new HashMap();
			if(jobDetails.getId()!=null)
				map.put("jobId", jobDetails.getId());
			else
				map.put("jobId", "");
			if(jobDetails.getJobName()!=null)
				map.put("jobName", jobDetails.getJobName());
			else
				map.put("jobName", "");
			if(jobDetails.getJobDescription()!=null)
				map.put("jobDescription",jobDetails.getJobDescription());
			else
				map.put("jobDescription", "");
			if(jobDetails.getJobStatus()!=null)
				map.put("jobStatus", jobDetails.getJobStatus());
			else
				map.put("jobStatus", "");
			if(jobDetails.getProgrammId()!=null){
				ApplicationPrograms program=applicationprogramsRepository.findOne(jobDetails.getProgrammId());
				if(program!=null)
					map.put("programName",program.getPrgmName());
				else
					map.put("programName","");
			}
			if(jobDetails.getStartDate()!=null)
				map.put("startDate",jobDetails.getStartDate());
			else
				map.put("startDate","");
			if(jobDetails.getEndDate()!=null)
				map.put("startDate",jobDetails.getEndDate());
			else
				map.put("startDate","");
			map.put("tenantId", jobDetails.getTenantId());
			if(jobDetails.getMaxConsecutiveFails()!=null)
				map.put("maxConsecutiveFails", jobDetails.getMaxConsecutiveFails());
			else
				map.put("maxConsecutiveFails", "");
			if(jobDetails.isSendFailAlerts()!=null)
				map.put("sendFailAlerts",jobDetails.isSendFailAlerts());
			else
				map.put("sendFailAlerts","");
			if(jobDetails.getParameterArguments1()!=null)
				map.put("parameterArguments1", jobDetails.getParameterArguments1());
			else
				map.put("parameterArguments1", "");
			if(jobDetails.getParameterArguments2()!=null)
				map.put("parameterArguments2", jobDetails.getParameterArguments2());
			else
				map.put("parameterArguments2", "");
			if(jobDetails.getParameterArguments3()!=null)
				map.put("parameterArguments3", jobDetails.getParameterArguments3());
			else
				map.put("parameterArguments3", "");
			if(jobDetails.getParameterArguments4()!=null)
				map.put("parameterArguments4", jobDetails.getParameterArguments4());
			else
				map.put("parameterArguments4", "");
			if(jobDetails.getParameterArguments5()!=null)
				map.put("parameterArguments5", jobDetails.getParameterArguments5());
			else
				map.put("parameterArguments5", "");
			if(jobDetails.getParameterArguments6()!=null)
				map.put("parameterArguments6", jobDetails.getParameterArguments6());
			else
				map.put("parameterArguments6", "");	
			if(jobDetails.getParameterArguments7()!=null)
				map.put("parameterArguments7", jobDetails.getParameterArguments7());
			else
				map.put("parameterArguments7", "");
			if(jobDetails.getParameterArguments8()!=null)
				map.put("parameterArguments8", jobDetails.getParameterArguments8());
			else
				map.put("parameterArguments8", "");
			if(jobDetails.getParameterArguments9()!=null)
				map.put("parameterArguments9", jobDetails.getParameterArguments9());
			else
				map.put("parameterArguments9", "");
			if(jobDetails.getParameterArguments10()!=null)
				map.put("parameterArguments10", jobDetails.getParameterArguments10());
			else
				map.put("parameterArguments10","");
			finalMap.add(map);
		}
		return finalMap;


			}*/
    
    /**
     * author ravali
     * @param name
     * @return count of job names
     */
    @GetMapping("/isJobNameAvailable")
    @Timed 
    public Long getJobNameCount(HttpServletRequest request,@RequestParam String jobName)//,@RequestParam Long tenantId)
    {
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	log.info("Request Rest to check jon name for tenant: "+tenantId);
    	Long i=0L;
    	List<JobDetails> jobDetList=jobDetailsRepository.findByJobNameAndTenantId(jobName,tenantId);
    	log.info("jobDetList :"+jobDetList);
    	if(jobDetList.size()>0)
    	{
    		for(JobDetails jobDet:jobDetList)
    		{
    			i=jobDet.getId();
    		}
    	}
    	return i;
    }
    
    
    
    @PostMapping("/jobIntiateForAcctAndRec")
    @Timed
    public ResponseEntity jobIntiateForAcctAndRec(HttpServletRequest request,@RequestParam String progType,@RequestBody HashMap parameterSet)
    {
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	Long userId=Long.parseLong(map.get("userId").toString());
    	log.info("Rest Request to post job And scheduler details by parameter set :"+progType +"Map :"+parameterSet +" for tenant: "+tenantId+" user: "+userId);

    	/*LookUpCode mean=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("RULE_GROUP_TYPE", progType, tenantId);
    	if(parameterSet.size()==1)
    	{
    		List<BigInteger> ruleIds=ruleGroupDetailsRepository.fetchRuleIdsByGroupAndTenantId(Long.valueOf(parameterSet.get("param1").toString()),tenantId);
			log.info("ruleIds :"+ruleIds);
			if(ruleIds.size()>0)
			{
				log.info("ruleIds.toString() :"+ruleIds.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", ""));
				parameterSet.put("param2",ruleIds.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", ""));
			}
    }*/
    	String ruleType="";
    	if(progType.equalsIgnoreCase("Accounting") || progType.equalsIgnoreCase("Reconciliation") || progType.equalsIgnoreCase("Reconciliation and Approvals") || progType.equalsIgnoreCase("Accounting and Approvals")
				|| progType.toLowerCase().startsWith("reconciliation"))
    		ruleType="NORMAL";
    	ResponseEntity response= oozieService.jobIntiateForAcctAndRec(tenantId, userId, progType, parameterSet,ruleType,request);
    	return response;
    }
    
  /**
   * Author Kiran  
   * @param file
   * @param tenantId
   * @param userId
   * @param srcPrfAsmtlId
   * @return
   * @throws IOException
   * @throws SftpException
   */

@PostMapping("/DropZoneTransformation")
	@Timed
	public String transformationOfFileUsingDropZone(HttpServletRequest request,@RequestParam("file") MultipartFile file, @RequestParam Long srcPrfAsmtlId) throws IOException, SftpException
	{
		log.info("Rest Post Api to Transform the file after file Drop");
		HashMap map=userJdbcService.getuserInfoFromToken(request);
	  	Long tenantId=Long.parseLong(map.get("tenantId").toString());
		Long userId=Long.parseLong(map.get("userId").toString());
		String formConfig="Local_Connection";
		SourceProfileFileAssignments srcFileAsmtDetails=sourceProfileFileAssignmentsRepository.findOne(srcPrfAsmtlId);

		if(srcFileAsmtDetails!=null)
		{
			FormConfig formDetails=formConfigRepository.findByFormConfig(formConfig);
			String targetFolderPath=srcFileAsmtDetails.getLocalDirectoryPath();
			String fileName=file.getOriginalFilename();
			log.info("fileName: "+fileName);
			java.text.DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
			String filename1 = "";
			String filename2 = "";
			if ((fileName.lastIndexOf(".") != -1) && (fileName.lastIndexOf(".") != 0))
			{
				int val = fileName.lastIndexOf(".");
				filename1 = fileName.substring(0, val);
				filename2 = fileName.substring(val, fileName.length());
			}
			String fileWithTimeStamp = filename1 + "_" + df.format(new Date()) + filename2;
			System.out.println("TemplateName with timestamp:- " + fileWithTimeStamp);

			if(formDetails!=null)
			{
				String str=formDetails.getValue();
				String[] strVals=str.split(",");
				String host=strVals[0];
				String port=strVals[1];
				String userName=strVals[2];
				String password=strVals[3];

				Channel channel =sftpConnection(host, port, userName,password);
				ChannelSftp targetchannelSftp = (ChannelSftp)channel;


				InputStream inputStream = new BufferedInputStream(file.getInputStream());
				putFile(targetchannelSftp, targetFolderPath, fileWithTimeStamp, inputStream);

				Vector<ChannelSftp.LsEntry> filesMoved = targetchannelSftp.ls(targetFolderPath);
				BatchHeader batchHdrDetails=null;
				if (filesMoved.size() != 0)
				{
					log.info("File Moved to local Dir Path: "+targetFolderPath);

					Long profileId=srcFileAsmtDetails.getSourceProfileId();
					SourceProfiles srcPrfLDetails=sourceProfilesRepository.findOne(profileId);

					batchHdrDetails=new BatchHeader();
					batchHdrDetails.setBatchName("Batch_"+srcPrfLDetails.getSourceProfileName());
					batchHdrDetails.setCreatedBy(userId);
					batchHdrDetails.setCreatedDate(ZonedDateTime.now());
					batchHdrDetails.setExtractedDatetime(ZonedDateTime.now());
					batchHdrDetails.setExtractionStatus("1 file(s) - EXTRACTED");
					batchHdrDetails.setTenantId(tenantId);
					batchHdrDetails.setType("Manual");
					BatchHeader batchHdr=batchHeaderRepository.save(batchHdrDetails);  

					batchHdrDetails=batchHeaderRepository.findOne(batchHdr.getId());
					batchHdrDetails.setBatchName("Batch_"+srcPrfLDetails.getSourceProfileName()+"_"+batchHdr.getId());
					batchHeaderRepository.save(batchHdrDetails);


					SourceFileInbHistory srcFileHstry = new SourceFileInbHistory();
					srcFileHstry.setProfileId(profileId);
					srcFileHstry.setFileName(fileWithTimeStamp);
					srcFileHstry.setFileReceivedDate(ZonedDateTime.now());
					srcFileHstry.setLocalDirPath(targetFolderPath);
					srcFileHstry.setFileExt(filename2);
					srcFileHstry.setCreatedBy(userId);
					srcFileHstry.setCreationDate(ZonedDateTime.now());
					srcFileHstry.setSrcPrfFileAsmtId(srcPrfAsmtlId);
					srcFileHstry.setTenantId(tenantId);
					srcFileHstry.setStatus("Extracted");
					srcFileHstry.setBatchId(batchHdr.getId());
					srcFileHstry.setHold(false);
					sourceFileInbHistoryRepository.save(srcFileHstry);


					/**Code to start Transformation Process**/

					HashMap parameterSet = new HashMap();
					parameterSet.put("param1", profileId);
					parameterSet.put("param2", srcFileAsmtDetails.getTemplateId());
//					parameterSet.put("param3", "null");
//					parameterSet.put("param4", "null");

					log.info("Api call to Intiate Job for Data Transformation process: "+parameterSet);
					jobIntiateForAcctAndRec(request, "DataTransformation", parameterSet);

				}
			}
		}
		else{
			log.info("Invalid Source Profile Asmt Id");
		}


		return null;
	}



	public static Channel sftpConnection(String host, String prt, String userName, String password)
	{
		Channel channel = null ;
		//System.out.println("port:"+prt);
		int port=0;
		try{
			port = Integer.parseInt(prt);}
		catch(NumberFormatException ex)
		{
			System.out.println("NumberFormat Exception for port: "+prt);
			System.exit(0);
		}
		System.out.println("Host: "+host+" Port: "+port+" UserName "+userName+" Password: "+password);
		Session session = null;
		try 
		{
			JSch jsch = new JSch();
			session = jsch.getSession(userName, host, port);
			session.setPassword(password);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			//System.out.println("==========config========="+config);
			session.connect();
			channel = session.openChannel("sftp");
			//System.out.println("==========channel=========="+channel);
			channel.connect();
			System.out.println("channel success connection :"+channel.isConnected());
		} 
		catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Failed connection");
		}

		return channel ;
	}



	public static void putFile(Channel channel, String targetFolder, String fileToTransfer, InputStream sourceStream)
	{
		System.out.println("targetFolder: "+targetFolder+" --> file name: "+fileToTransfer);
		ChannelSftp channelSftp = (ChannelSftp) channel;

		//		String tTargetFolder = targetFolder;
		//System.out.println("Target Folder: "+targetFolder);
		/***/
		try {
			channelSftp.cd("/");
		} catch (SftpException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		String[] folders = targetFolder.split( "/" );
		for ( String folder : folders ) {
			//System.out.println("folder: "+folder);
			if ( folder.length() > 0 ) {
				try {
					channelSftp.cd( folder );
					//	System.out.println("Change Folder: "+folder);
				}
				catch ( SftpException e ) {
					try {
						channelSftp.mkdir( folder );
						//System.out.println("Creating Folder: "+folder);
						channelSftp.chmod(0777, folder);
						channelSftp.cd( folder );
						//System.out.println("Change Folder: "+folder);
					} catch (SftpException e1) {
						e1.printStackTrace();
					}

				}
			}
		}
		/***/
		//	channelSftp.mkdir(targetFolder);

		try {
			channelSftp.cd(targetFolder);
		}
		catch(Exception e)
		{

		}
		try {	
			FileInputStream fis = null;
			if(sourceStream == null)
			{
				File f = new File(fileToTransfer);
				fis = new FileInputStream(f);
				channelSftp.put(fis, f.getName());
			}
			else
			{
				System.out.println("Copying file to server ");
				//	System.out.println("sourceStream: "+sourceStream+" fileToTransfer: "+fileToTransfer);
				channelSftp.put(sourceStream, fileToTransfer);
			}
			if(fis != null)
			{
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		catch (SftpException e) {
			// TODO Auto-generated catch block
			System.out.println("SFTP Exception "+e.getMessage());
			e.printStackTrace();
			disconnect(channel);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("File Not Found Exception "+e.getMessage());
			e.printStackTrace();
		}
	}

	public static void disconnect(Channel channel)
	{
		try {
			Session session = channel.getSession();
			channel.disconnect();
			session.disconnect();
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Author Kiran
	 * @param tenantId
	 * @param offset
	 * @param sortColName
	 * @param sortOrder
	 * @param limit
	 * @return
	 * @throws SQLException
	 */
	@GetMapping("/getAllJobDetailsByTenantId")
	@Timed
	public List<HashMap> getAllJobDetailsByTenantId(HttpServletRequest request,@RequestParam(value = "page" , required = false) Integer offset, 
			@RequestParam(required = false) String sortColName, @RequestParam(required = false) String sortOrder,
			@RequestParam(value = "per_page", required = false) Integer limit) throws SQLException
			{
		log.info("Request Rest to get all job details By tenantId");
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long userId=Long.parseLong(map.get("userId").toString());
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		Connection conn = null;
		Statement stmt = null;
		ResultSet result = null; 
		String schemaName=null;
		List<HashMap> dataMap = new ArrayList<HashMap>();
		try
		{
//			String dbUrl=env.getProperty("spring.datasource.url");
//			String[] parts=dbUrl.split("[\\s@&?$+-]+");
//			String host = parts[0].split("/")[2].split(":")[0];
//			schemaName=parts[0].split("/")[3];
//			String userName = env.getProperty("spring.datasource.username");
//			String password = env.getProperty("spring.datasource.password");
//			String jdbcDriver = env.getProperty("spring.datasource.jdbcdriver");
//			Class.forName(jdbcDriver);
//			conn = DriverManager.getConnection(dbUrl, userName, password);
			DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
	 		conn=ds.getConnection();
			stmt = conn.createStatement();
			log.info("Successfully connected to JDBC with schema "+schemaName);

			//HashMap map=userJdbcService.getuserInfoFromToken(request);
			//Long tenantId=Long.parseLong(map.get("tenantId").toString());
			if(limit == null || limit == 0 )
			{
				limit = jobDetailsRepository.findByTenantIdAndOrderById(tenantId).size();
			}
			if(offset == null )
			{
				offset=0;
			}
			if(sortOrder==null)
				sortOrder="desc";
			if(sortColName==null)
				sortColName="jobId";
			int offSt = 0;
			offSt = (offset * limit + 1)-1;
			String query = "";
			
			
			String view="Select "
					+ "`jd`.`id` AS `jobId`, "
					+ "`jd`.`job_name` AS `jobName`, "
					+ "`jd`.`job_description` AS `jobDescription`, "
					+ "`jd`.`job_status` AS `jobStatus`, "
					+ "`jd`.`start_date` AS `startDate`, "
					+ "`jd`.`end_date` AS `endDate`, "
					+ "`jd`.`tenant_id` AS `tenantId`, "
					+ "`jd`.`max_consecutive_fails` AS `maxConsecutiveFails`, "
					+ "`jd`.`send_fail_alerts` AS `sendFailAlerts`, "
					+ "`jd`.`parameter_arguments_1` AS `parameterArguments1`, "
					+ "`jd`.`parameter_arguments_2` AS `parameterArguments2`, "
					+ "`jd`.`parameter_arguments_3` AS `parameterArguments3`, "
					+ "`jd`.`parameter_arguments_4` AS `parameterArguments4`, "
					+ "`jd`.`parameter_arguments_5` AS `parameterArguments5`, "
					+ "`jd`.`parameter_arguments_6` AS `parameterArguments6`, "
					+ "`jd`.`parameter_arguments_7` AS `parameterArguments7`, "
					+ "`jd`.`parameter_arguments_8` AS `parameterArguments8`, "
					+ "`jd`.`parameter_arguments_9` AS `parameterArguments9`, "
					+ "`jd`.`parameter_arguments_10` AS `parameterArguments10`, "
					+ "`ap`.`prgm_name` AS `programName` "
					+ "FROM((`t_job_details` `jd` JOIN `t_application_programs` `ap`))"
					+ "Where `jd`.`tenant_id`="+tenantId+" and `ap`.`id` = `jd`.`programm_id`";


			//stmt.executeUpdate(view);
			log.info("sortColName: "+sortColName+" sortOrder: "+sortOrder+" offSt: "+offSt+" limit: "+limit);
			//query ="SELECT * from "+schemaName+".`"+"get_all_job_details_by_tenant_id"+"` "+" order by "+sortColName +" "+sortOrder +" limit "+offSt+", "+limit;
			query="SELECT * FROM ("+view+") AS get_all_job_details_by_tenant_id "+" order by "+sortColName +" "+sortOrder +" limit "+offSt+", "+limit;
			log.info("query in Job Details: "+query);
			result=stmt.executeQuery(query);
			ResultSetMetaData rsmd = result.getMetaData();
			int colCount = rsmd.getColumnCount();
			while(result.next()){
				HashMap hm = new HashMap();
				for(int i=1; i<=colCount; i++)
				{
					hm.put(rsmd.getColumnName(i), result.getString(i));
				}
				dataMap.add(hm);
			}
			log.info("Data Size: "+ dataMap.size());
		}
		catch(Exception e)
		{
			log.info("Exceptin while fetching data: "+ e);
		}
		finally
		{
			//drop view
			//stmt.executeUpdate( "DROP view `"+schemaName+"`.`get_all_job_details_by_tenant_id`");

			if(result != null)
				result.close();	
			if(stmt != null)
				stmt.close();
			if(conn != null)
				conn.close();
		}
		return dataMap;


			}

}
