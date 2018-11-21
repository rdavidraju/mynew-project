package com.nspl.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.esotericsoftware.minlog.Log;
import com.hazelcast.hibernate.local.Timestamp;
import com.nspl.app.config.ApplicationContextProvider;
import com.nspl.app.domain.ApplicationPrograms;
import com.nspl.app.domain.JobDetails;
import com.nspl.app.domain.LookUpCode;
import com.nspl.app.domain.Notifications;
import com.nspl.app.domain.ProgParametersSets;
import com.nspl.app.domain.RuleGroup;
import com.nspl.app.domain.SchedulerDetails;
import com.nspl.app.domain.SourceProfileFileAssignments;
import com.nspl.app.repository.ApplicationProgramsRepository;
import com.nspl.app.repository.JobDetailsRepository;
import com.nspl.app.repository.LookUpCodeRepository;
import com.nspl.app.repository.ProgParametersSetsRepository;
import com.nspl.app.repository.SchedulerDetailsRepository;
import com.nspl.app.repository.SourceProfileFileAssignmentsRepository;
import com.nspl.app.repository.search.SchedulerDetailsSearchRepository;
import com.nspl.app.service.OozieService;
import com.nspl.app.service.PropertiesUtilService;
import com.nspl.app.service.UserJdbcService;
import com.nspl.app.web.rest.dto.Frequencies;
import com.nspl.app.web.rest.dto.OozieJobDTO;
import com.nspl.app.web.rest.util.HeaderUtil;
import com.nspl.app.web.rest.util.PaginationUtil;
import com.oozie.wfmanger.WfManager;
import com.oozie.wfmanger.WfManagerImpl;

import io.github.jhipster.web.util.ResponseUtil;

import org.apache.oozie.client.OozieClient;
import org.apache.oozie.client.OozieClientException;
import org.apache.oozie.client.WorkflowJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing SchedulerDetails.
 */
@RestController
@RequestMapping("/api")
public class SchedulerDetailsResource {

    private final Logger log = LoggerFactory.getLogger(SchedulerDetailsResource.class);

    private static final String ENTITY_NAME = "schedulerDetails";
        
    private final SchedulerDetailsRepository schedulerDetailsRepository;

    private final SchedulerDetailsSearchRepository schedulerDetailsSearchRepository;

    @Autowired
	Environment env;
    
    @PersistenceContext(unitName="default")
	private EntityManager em;
    
    @Inject
    JobDetailsRepository jobDetailsRepository;
    
    @Inject
    SourceProfileFileAssignmentsRepository sourceProfileFileAssignmentsRepository;
    
    @Inject
    ApplicationProgramsRepository applicationProgramsRepository;
    
    @Inject
    ProgParametersSetsRepository progParametersSetsRepository;
    @Inject
    PropertiesUtilService propertiesUtilService;
    
    @Inject
    LookUpCodeRepository lookUpCodeRepository;
   
	@Inject
	OozieService oozieService;
	
	@Inject
	UserJdbcService userJdbcService;
	
    
    public SchedulerDetailsResource(SchedulerDetailsRepository schedulerDetailsRepository, SchedulerDetailsSearchRepository schedulerDetailsSearchRepository) {
        this.schedulerDetailsRepository = schedulerDetailsRepository;
        this.schedulerDetailsSearchRepository = schedulerDetailsSearchRepository;
    }

    /**
     * POST  /scheduler-details : Create a new schedulerDetails.
     *
     * @param schedulerDetails the schedulerDetails to create
     * @return the ResponseEntity with status 201 (Created) and with body the new schedulerDetails, or with status 400 (Bad Request) if the schedulerDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/scheduler-details")
    @Timed
    public ResponseEntity<SchedulerDetails> createSchedulerDetails(@RequestBody SchedulerDetails schedulerDetails) throws URISyntaxException {
        log.debug("REST request to save SchedulerDetails : {}", schedulerDetails);
        if (schedulerDetails.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new schedulerDetails cannot already have an ID")).body(null);
        }
        SchedulerDetails result = schedulerDetailsRepository.save(schedulerDetails);
        schedulerDetailsSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/scheduler-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /scheduler-details : Updates an existing schedulerDetails.
     *
     * @param schedulerDetails the schedulerDetails to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated schedulerDetails,
     * or with status 400 (Bad Request) if the schedulerDetails is not valid,
     * or with status 500 (Internal Server Error) if the schedulerDetails couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/scheduler-details")
    @Timed
    public ResponseEntity<SchedulerDetails> updateSchedulerDetails(@RequestBody SchedulerDetails schedulerDetails) throws URISyntaxException {
        log.debug("REST request to update SchedulerDetails : {}", schedulerDetails);
        if (schedulerDetails.getId() == null) {
            return createSchedulerDetails(schedulerDetails);
        }
        SchedulerDetails result = schedulerDetailsRepository.save(schedulerDetails);
        schedulerDetailsSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, schedulerDetails.getId().toString()))
            .body(result);
    }

    /**
     * GET  /scheduler-details : get all the schedulerDetails.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of schedulerDetails in body
     */
    @GetMapping("/scheduler-details")
    @Timed
    public List<SchedulerDetails> getAllSchedulerDetails() {
        log.debug("REST request to get all SchedulerDetails");
        List<SchedulerDetails> schedulerDetails = schedulerDetailsRepository.findAll();
        return schedulerDetails;
    }

    /**
     * GET  /scheduler-details/:id : get the "id" schedulerDetails.
     *
     * @param id the id of the schedulerDetails to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the schedulerDetails, or with status 404 (Not Found)
     */
    @GetMapping("/scheduler-details/{id}")
    @Timed
    public ResponseEntity<SchedulerDetails> getSchedulerDetails(@PathVariable Long id) {
        log.debug("REST request to get SchedulerDetails : {}", id);
        SchedulerDetails schedulerDetails = schedulerDetailsRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(schedulerDetails));
    }

    /**
     * DELETE  /scheduler-details/:id : delete the "id" schedulerDetails.
     *
     * @param id the id of the schedulerDetails to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/scheduler-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteSchedulerDetails(@PathVariable Long id) {
        log.debug("REST request to delete SchedulerDetails : {}", id);
        SchedulerDetails schedulerDetails = schedulerDetailsRepository.findOne(id);
        schedulerDetails.setDeleteFlag(true);
       // schedulerDetails.setLastUpdatedBy(lastUpdatedBy);
        schedulerDetails.setLastUpdatedDate(ZonedDateTime.now());
       // schedulerDetailsRepository.delete(id);
       // schedulerDetailsSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/scheduler-details?query=:query : search for the schedulerDetails corresponding
     * to the query.
     *
     * @param query the query of the schedulerDetails search 
     * @return the result of the search
     */
    @GetMapping("/_search/scheduler-details")
    @Timed
    public List<SchedulerDetails> searchSchedulerDetails(@RequestParam String query) {
        log.debug("REST request to search SchedulerDetails for query {}", query);
        return StreamSupport
            .stream(schedulerDetailsSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
    
    
    /**
     * author ravali
     * @param page
     * @param size
     * @param tenantId
     * @param response
     * @return List of schedulers
     * @throws OozieClientException
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    
    /**old working before applying filters**/
    
   /* @GetMapping("/getAllSchedulersList")
    @Timed 
    public List<HashMap> getSchedulersList(@RequestParam(value = "page" , required = true) Integer page,
			@RequestParam(value = "per_page", required = true) Integer size,@RequestParam Long tenantId,HttpServletResponse response,@RequestParam(value = "jobId", required = false) Long jobId,@RequestParam Long userId) throws OozieClientException, SQLException, ClassNotFoundException
			{
    	log.info("Request Rest to fetch schedulers list with jobId"+jobId);
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

    	Statement stmt = null;
    	Statement stmt2=null;

    	//Class.forName("com.mysql.jdbc.Driver");
    	log.info("env.getProperty(spring.datasource.jdbcdriver) :"+env.getProperty("spring.datasource.jdbcdriver"));
    	Class.forName(env.getProperty("spring.datasource.jdbcdriver"));
    	if(DB_URL != null)
    	{
    		if(USER != null)
    		{
    			if(PASS != null)
        		{

    		    	conn = DriverManager.getConnection(DB_URL, USER, PASS);
    		    	if(conn != null)
    		    	{
    		    		log.info("Connected database successfully...");
        		    	stmt = conn.createStatement();
        		    	stmt2=conn.createStatement();
        		    	ResultSet result2=null;
        		    	
        		    	//to get the total count and adding attribute to response header
        		    	ResultSet resultCount=stmt2.executeQuery("select count(*) from "+schema+".recon_oozie_jobs_v where tenant_id="+userId);
        		    	while(resultCount.next())
        		    	{
        		    		log.info("resultCount.getString(count(*)) :"+resultCount.getString("count(*)"));
        		    		response.addIntHeader("X-COUNT", Integer.parseInt(resultCount.getString("count(*)")));
        		    	}

        		    	if(jobId != null && jobId>1)
        		    	{
        		    		List<String> schDetList=schedulerDetailsRepository.fetchOzieJobIdsByJobId(jobId);
        		    		if(schDetList.size()>0)
        		    		{
        		    			
        		    			String str="'"+schDetList.toString().replace("[","").replace("]", "").replace(" ","").replace(",","','")+"'";
        		    			log.info("str :"+str);
        		    			String schOzieIds=schDetList.toString().replaceAll("\\[","").replaceAll("\\]", "");
        		    			log.info("schOzieIds :"+schOzieIds);

        		    			result2=stmt.executeQuery("select * from "+schema+".recon_oozie_jobs_v where parent_id in ("+str+")");
        		    		}
        		    	}
        		    	else if(jobId == null || jobId<1)
        		    	{
        		    		result2=stmt.executeQuery("select * from "+schema+".recon_oozie_jobs_v where tenant_id="+userId+" order by start_time desc Limit "+(page-1) * size+", "+size);

        		    	}



        		    	//ResultSetMetaData rsmd = result2.getMetaData();

        		    	//int columnsNumber = rsmd.getColumnCount();
        		    	List<HashMap> oozieList=new ArrayList<HashMap>();

        		    	while(result2.next())
        		    	{
        		    		//log.info("result2.getString(parent_id) :"+result2.getString("parent_id"));

        		    		SchedulerDetails sch=schedulerDetailsRepository.findByOozieJobId(result2.getString("parent_id"));
        		    		if(sch!=null)
        		    		{
        		    			if(sch.getOozieJobId()!=null)
        		    			{
        		    				HashMap map=new HashMap();
        		    				if(result2.getString("job_id")!=null)
        		    					map.put("oozieJobId", result2.getString("job_id"));
        		    				else
        		    					map.put("oozieJobId", "");
        		    				if(result2.getString("start_time")!=null)
        		    					map.put("startTime", result2.getString("start_time"));
        		    				else
        		    					map.put("startTime","");
        		    				if(result2.getString("child_id")!=null)
        		    					map.put("childId", result2.getString("child_id"));
        		    				else
        		    					map.put("childId", "");
        		    				if(result2.getString("end_time")!=null)
        		    					map.put("endTime", result2.getString("end_time"));
        		    				else
        		    					map.put("endTime","");
        		    				if(result2.getString("status")!=null)
        		    					map.put("oozieStatus", result2.getString("status"));
        		    				else
        		    					map.put("oozieStatus","");


        		    				map.put("jobId", sch.getJobId());
        		    				JobDetails jobDetails=jobDetailsRepository.findOne(sch.getJobId());
        		    				if(jobDetails!=null && jobDetails.getJobName()!=null)
        		    					map.put("jobName", jobDetails.getJobName());
        		    				else
        		    					map.put("jobName", "");
        		    				if(jobDetails!=null && jobDetails.getJobDescription()!=null)
        		    					map.put("jobDescription", jobDetails.getJobDescription());
        		    				else
        		    					map.put("jobDescription", "");
        		    				if(sch.getStatus()!=null)
        		    					map.put("status", sch.getStatus());
        		    				else
        		    					map.put("status", "");
        		    				map.put("localStatDate", sch.getStartDate());
        		    				map.put("localEndDate", sch.getEndDate());

        		    				if(sch.getFrequency()!=null)
        		    				{
        		    					LookUpCode lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("SCHEDULE_TYPES", sch.getFrequency(), tenantId);
        		    					map.put("frequencyType", lookUpCode.getMeaning());
        		    				}
        		    				else
        		    					map.put("frequencyType", "");

        		    				if(sch.getSchedulerName()!=null)
        		    					map.put("schedulerName", sch.getSchedulerName());
        		    				else
        		    					map.put("schedulerName", "");

        		    				if(jobDetails!=null && jobDetails.getProgrammId()!=null)
        		    				{
        		    					ApplicationPrograms progName=applicationProgramsRepository.findOne(jobDetails.getProgrammId());
        		    					if(progName.getPrgmName()!=null)
        		    						map.put("programName", progName.getPrgmName());
        		    					//}


        		    					List<ProgParametersSets> prog=progParametersSetsRepository.findByProgramId(jobDetails.getProgrammId());
        		    					String paramList="";

        		    					int k=0;


        		    					if (k<prog.size()){
        		    						if(jobDetails.getParameterArguments1()==null)
        		    							paramList=paramList+" ,";
        		    						else
        		    							paramList=	paramList+jobDetails.getParameterArguments1()+",";
        		    					}
        		    					k++;
        		    					if (k<prog.size()){
        		    						if(jobDetails.getParameterArguments2()==null)
        		    							paramList=paramList+" ,";
        		    						else
        		    							paramList=	paramList+jobDetails.getParameterArguments2()+",";
        		    					}
        		    					k++;
        		    					if (k<prog.size()){
        		    						if(jobDetails.getParameterArguments3()==null)
        		    							paramList=paramList+" ,";
        		    						else
        		    							paramList=	paramList+jobDetails.getParameterArguments3()+",";
        		    					}
        		    					k++;
        		    					if (k<prog.size()){
        		    						if(jobDetails.getParameterArguments4()==null)
        		    							paramList=paramList+" ,";
        		    						else
        		    							paramList=	paramList+jobDetails.getParameterArguments4()+",";
        		    					}
        		    					k++;
        		    					if (k<prog.size()){
        		    						if(jobDetails.getParameterArguments5()==null)
        		    							paramList=paramList+" ,";
        		    						else
        		    							paramList=	paramList+jobDetails.getParameterArguments5()+",";
        		    					}
        		    					k++;
        		    					if (k<prog.size()){
        		    						if(jobDetails.getParameterArguments6()==null)
        		    							paramList=paramList+" ,";
        		    						else
        		    							paramList=	paramList+jobDetails.getParameterArguments6()+",";
        		    					}
        		    					k++;
        		    					if (k<prog.size()){
        		    						if(jobDetails.getParameterArguments7()==null)
        		    							paramList=paramList+" ,";
        		    						else
        		    							paramList=	paramList+jobDetails.getParameterArguments7()+",";
        		    					}
        		    					k++;
        		    					if (k<prog.size()){
        		    						if(jobDetails.getParameterArguments8()==null)
        		    							paramList=paramList+" ,";
        		    						else
        		    							paramList=	paramList+jobDetails.getParameterArguments8()+",";
        		    					}
        		    					k++;
        		    					if (k<prog.size()){
        		    						if(jobDetails.getParameterArguments9()==null)
        		    							paramList=paramList+" ,";
        		    						else
        		    							paramList=	paramList+jobDetails.getParameterArguments9()+",";
        		    					}
        		    					k++;
        		    					if (k<prog.size()){
        		    						if(jobDetails.getParameterArguments10()==null)
        		    							paramList=paramList+" ,";
        		    						else
        		    							paramList=	paramList+jobDetails.getParameterArguments10()+",";
        		    					}


        		    					//log.info("paramList 1:"+paramList);
        		    					if(paramList!=null && paramList.length()!=0)
        		    					{
        		    						String str=paramList.substring(0, paramList.length() - 1);
        		    						//log.info("paramList 2:"+str);
        		    						map.put("parametes", str);
        		    					}
        		    				}
        		    				else
        		    					map.put("programName", "");
        		    				map.put("parametes", "");


        		    				if(map!=null && !map.isEmpty())
        		    					finalSchList.add(map);
        		    			}
        		    		}

        		    	}
        		    	if(conn!=null)
        		    		conn.close();
        		    	if(stmt!=null)
        		    		stmt.close();
        		    	if(stmt2!=null)
        		    		stmt2.close();
        		    	if(result2!=null)
        		    		result2.close();
        		    	if(resultCount!=null)
        		    		resultCount.close();

    		    	}
    		    	else
    		    	{
    		    		log.info("connection is null");
    		    	}
    		    
        		}
        		else
        		{
        			log.info("PASS is null");
        		}
    		}
    		else
    		{
    			log.info("USER is null");
    		}
    	}
    	else
    	{
    		log.info("DB_URL is null");
    	}
    	
    	return finalSchList;

			}*/
        
    
    /** after applying program id and frequency type filters**/
    
    @GetMapping("/getAllSchedulersList")
    @Timed 
    public List<LinkedHashMap> getAllSchedulersList(HttpServletRequest request,@RequestParam(value = "page" , required = true) Integer page,
    		@RequestParam(value = "per_page", required = true) Integer size,
    		HttpServletResponse response,@RequestParam(value = "jobId", required = false) String jobId,
    		@RequestParam(value = "programId", required = false) Long programId,
    		@RequestParam(value = "freType", required = false) String frequencyType) 
    				throws OozieClientException, SQLException, ClassNotFoundException
    				{
    
    	log.info("Request Rest to fetch schedulers list with parameters programId"+programId+" freType :"+frequencyType);
    	HashMap map0=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map0.get("tenantId").toString());
    	Long userId=Long.parseLong(map0.get("userId").toString());
    	
    	

    	log.info("Request Rest to fetch schedulers list with jobId"+jobId+ " of tenant: "+tenantId+" userid: "+userId);

    	List<BigInteger> jobIds=new ArrayList<BigInteger>();
    	List<String> schDetails=new ArrayList<String>();
    	if(programId!=null && frequencyType==null)
    	{

    		log.info("in 1 condition");
    		jobIds=jobDetailsRepository.findByCreatedByAndProgrammId(userId, programId);
    		schDetails= schedulerDetailsRepository.findOozieJobIdByJobIdIn(jobIds);
    	}
    	else if(programId==null && frequencyType!=null)
    	{
    		log.info("in 2 condition");

    		jobIds=jobDetailsRepository.fetchByCreatedBy(userId);
    		
    		if(frequencyType.equalsIgnoreCase("Scheduled"))
    			schDetails= schedulerDetailsRepository.findOozieJobIdByJobIdInAndFrequesncyTypeScheduled(jobIds);
    		else
        		schDetails= schedulerDetailsRepository.findOozieJobIdByJobIdInAndFrequesncyType(jobIds,frequencyType);
    		
    			
    	}
    	else if(programId!=null && frequencyType!=null)
    	{

    		log.info("in 3 condition");
    		jobIds=jobDetailsRepository.findByCreatedByAndProgrammId(userId, programId);
    		if(frequencyType.equalsIgnoreCase("Scheduled"))
    			schDetails= schedulerDetailsRepository.findOozieJobIdByJobIdInAndFrequesncyTypeScheduled(jobIds);
    		else
        		schDetails= schedulerDetailsRepository.findOozieJobIdByJobIdInAndFrequesncyType(jobIds,frequencyType);
    	}
    	else
    	{
    		log.info("in 4 condition **when proh=gram id and frequency type is null");

    		jobIds=jobDetailsRepository.fetchByCreatedBy(userId);
    		schDetails= schedulerDetailsRepository.findOozieJobIdByJobIdIn(jobIds);
    	}
    	String schIds=schDetails.stream().collect(Collectors.joining("','", "'", "'"));
    	/*log.info("jobIds :"+jobIds);
        log.info("schIds :"+schIds);*/
    	log.info("jobIds Size:"+jobIds.size());
    	log.info("schDetails Size:"+schDetails.size());
    //	response.addIntHeader("X-COUNT",schDetails.size() );
    	List<SchedulerDetails> schList = new ArrayList<SchedulerDetails>();
    	//to get the total count and adding attribute to response header

    	List<LinkedHashMap> finalSchList=new ArrayList<LinkedHashMap>();

    	String oozieUrl=env.getProperty("oozie.OozieClient");
    	//	Properties props = propertiesUtilService.getPropertiesFromClasspath("File.properties");
    /*	String DB_URL = env.getProperty("oozie.ozieUrl");
    	String USER = env.getProperty("oozie.ozieUser");
    	String PASS = env.getProperty("oozie.oziePswd");
    	String schema = env.getProperty("oozie.ozieSchema");*/
    	String schema = env.getProperty("oozie.ozieSchema");

    	Connection conn =null;

    	Statement stmt = null;
    	Statement stmt2=null;

    	//Class.forName("com.mysql.jdbc.Driver");
    //	log.info("env.getProperty(spring.datasource.jdbcdriver) :"+env.getProperty("spring.datasource.jdbcdriver"));
    //	Class.forName(env.getProperty("spring.datasource.jdbcdriver"));
    	
    	DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("oozieDataSource");
		conn = ds.getConnection();
    	
    	/*if(DB_URL != null)
    	{
    		if(USER != null)
    		{
    			if(PASS != null)
    			{*/

    				//conn = DriverManager.getConnection(DB_URL, USER, PASS);
    				if(conn != null)
    				{
    					log.info("Connected database successfully...");
    					stmt = conn.createStatement();
    					stmt2=conn.createStatement();
    					ResultSet result2=null;
    					ResultSet resultCount=null;

    					//to get the total count and adding attribute to response header
    					/*ResultSet resultCount=stmt2.executeQuery("select count(*) from "+schema+".recon_oozie_jobs_v where tenant_id="+userId);
    					while(resultCount.next())
    					{
    						log.info("resultCount.getString(count(*)) :"+resultCount.getString("count(*)"));
    						response.addIntHeader("X-COUNT", Integer.parseInt(resultCount.getString("count(*)")));
    					}
*/
    					if(jobId != null && !(jobId.isEmpty()))
    					{
    						JobDetails job=jobDetailsRepository.findByIdForDisplayAndTenantId(jobId,tenantId);
    						List<String> schDetList=schedulerDetailsRepository.fetchOzieJobIdsByJobId(job.getId());
    						if(schDetList.size()>0)
    						{

    							String str="'"+schDetList.toString().replace("[","").replace("]", "").replace(" ","").replace(",","','")+"'";
    							//log.info("str :"+str);
    							/*String schOzieIds=schDetList.toString().replaceAll("\\[","").replaceAll("\\]", "");
        		    			log.info("schOzieIds :"+schOzieIds);
    							 */
    							String query="select * from "+schema+".recon_oozie_jobs_v where parent_id in ("+str+") order by start_time desc";
    							result2=stmt.executeQuery(query);
    							//log.info("query :"+query);
    						}
    					}
    					//else if(jobId == null || jobId<1)
    					else if(jobId == null || jobId.isEmpty())
    					{
    						//log.info("schIds :"+schIds);
    						String Countquery="select count(*) from "+schema+".recon_oozie_jobs_v where tenant_id="+userId+" and parent_id in ("+schIds+")";
    						//log.info("Countquery :"+Countquery);
    						resultCount=stmt2.executeQuery(Countquery);
    						while(resultCount.next())
        					{
        						response.addIntHeader("X-COUNT", Integer.valueOf(resultCount.getString("count(*)")));
        					
        					}
    						
    						String query="select * from "+schema+".recon_oozie_jobs_v where tenant_id="+userId+" and parent_id in ("+schIds+") order by start_time desc Limit "+(page-1) * size+", "+size;
    						result2=stmt.executeQuery(query);
    						
    						if(resultCount!=null)
    						resultCount.close();

    					}

    					//ResultSetMetaData rsmd = result2.getMetaData();

    					//int columnsNumber = rsmd.getColumnCount();
    					List<HashMap> oozieList=new ArrayList<HashMap>();
    					
    					

    					while(result2.next())
    					{
    						int count=0;
    						//log.info("result2.getString(parent_id) :"+result2.getString("parent_id"));
    						SchedulerDetails sch=schedulerDetailsRepository.findByOozieJobId(result2.getString("parent_id"));
    						if(sch!=null)
    						{
    							if(sch.getOozieJobId()!=null)
    							{
    								log.info("when oozie job id is not null");
    								LinkedHashMap map=new LinkedHashMap();
    								count=count+1;
    								map.put("seqNo", count);
    								if(result2.getString("job_id")!=null)
    									map.put("oozieJobId", result2.getString("job_id"));
    								else
    									map.put("oozieJobId", "");
    								if(result2.getString("start_time")!=null)
    									map.put("startTime", result2.getString("start_time"));
    								else
    									map.put("startTime","");
    								
    								
    								if(result2.getString("child_id")!=null && result2.getString("child_id").contains("@")) {
    									log.info("child Id :"+result2.getString("child_id"));
    									map.put("childId", result2.getString("child_id").split("@")[1]);
    								}
    								//else
    								//	continue;
    								if(result2.getString("end_time")!=null)
    									map.put("endTime", result2.getString("end_time"));
    								else
    									map.put("endTime","");
    								
    								
    								
    								if(jobId!=null)
    								{
    									log.info("if job if not null :"+jobId);
    									/*if(result2.getString("status")!=null && result2.getString("status").equalsIgnoreCase("RUNNING"))
        									map.put("oozieStatus", "SCHEDULED");*/
        								 if(result2.getString("status")!=null)
        									map.put("oozieStatus", result2.getString("status").toString());
    								}
    								else if(jobId==null && result2.getString("status")!=null)
    								    map.put("oozieStatus", result2.getString("status").toString());
    								else
    									map.put("oozieStatus","");


    								
    								JobDetails jobDetails=jobDetailsRepository.findOne(sch.getJobId());
    								map.put("jobId", jobDetails.getIdForDisplay());
    								if(jobDetails!=null && jobDetails.getJobName()!=null)
    									map.put("jobName", jobDetails.getJobName());
    								else
    									map.put("jobName", "");
    								if(jobDetails!=null && jobDetails.getJobDescription()!=null)
    									map.put("jobDescription", jobDetails.getJobDescription());
    								else
    									map.put("jobDescription", "");
    								if(sch.getStatus()!=null)
    									map.put("status", sch.getStatus());
    								else
    									map.put("status", "");
    								map.put("localStatDate", sch.getStartDate().toString());
    								map.put("localEndDate", sch.getEndDate().toString());

    								if(sch.getFrequency()!=null)
    								{
    									LookUpCode lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("SCHEDULE_TYPES", sch.getFrequency(), tenantId);
    									map.put("frequencyType", lookUpCode.getMeaning());
    								}
    								else
    									map.put("frequencyType", "");

    								if(sch.getSchedulerName()!=null)
    									map.put("schedulerName", sch.getSchedulerName());
    								else
    									map.put("schedulerName", "");

    								if(jobDetails!=null && jobDetails.getProgrammId()!=null)
    								{
    									ApplicationPrograms progName=applicationProgramsRepository.findOne(jobDetails.getProgrammId());
    									if(progName.getPrgmName()!=null)
    										map.put("programName", progName.getPrgmName());
    									map.put("programId", jobDetails.getProgrammId());
    									//}


    									List<ProgParametersSets> prog=progParametersSetsRepository.findByProgramId(jobDetails.getProgrammId());
    									String paramList="";

    									int k=0;


    									if (k<prog.size()){
    										if(jobDetails.getParameterArguments1()==null)
    											paramList=paramList+" ,";
    										else
    											paramList=	paramList+jobDetails.getParameterArguments1()+",";
    									}
    									k++;
    									if (k<prog.size()){
    										if(jobDetails.getParameterArguments2()==null)
    											paramList=paramList+" ,";
    										else
    											paramList=	paramList+jobDetails.getParameterArguments2()+",";
    									}
    									k++;
    									if (k<prog.size()){
    										if(jobDetails.getParameterArguments3()==null)
    											paramList=paramList+" ,";
    										else
    											paramList=	paramList+jobDetails.getParameterArguments3()+",";
    									}
    									k++;
    									if (k<prog.size()){
    										if(jobDetails.getParameterArguments4()==null)
    											paramList=paramList+" ,";
    										else
    											paramList=	paramList+jobDetails.getParameterArguments4()+",";
    									}
    									k++;
    									if (k<prog.size()){
    										if(jobDetails.getParameterArguments5()==null)
    											paramList=paramList+" ,";
    										else
    											paramList=	paramList+jobDetails.getParameterArguments5()+",";
    									}
    									k++;
    									if (k<prog.size()){
    										if(jobDetails.getParameterArguments6()==null)
    											paramList=paramList+" ,";
    										else
    											paramList=	paramList+jobDetails.getParameterArguments6()+",";
    									}
    									k++;
    									if (k<prog.size()){
    										if(jobDetails.getParameterArguments7()==null)
    											paramList=paramList+" ,";
    										else
    											paramList=	paramList+jobDetails.getParameterArguments7()+",";
    									}
    									k++;
    									if (k<prog.size()){
    										if(jobDetails.getParameterArguments8()==null)
    											paramList=paramList+" ,";
    										else
    											paramList=	paramList+jobDetails.getParameterArguments8()+",";
    									}
    									k++;
    									if (k<prog.size()){
    										if(jobDetails.getParameterArguments9()==null)
    											paramList=paramList+" ,";
    										else
    											paramList=	paramList+jobDetails.getParameterArguments9()+",";
    									}
    									k++;
    									if (k<prog.size()){
    										if(jobDetails.getParameterArguments10()==null)
    											paramList=paramList+" ,";
    										else
    											paramList=	paramList+jobDetails.getParameterArguments10()+",";
    									}


    									//log.info("paramList 1:"+paramList);
    									if(paramList!=null && paramList.length()!=0)
    									{
    										String str=paramList.substring(0, paramList.length() - 1);
    										//log.info("paramList 2:"+str);
    										map.put("parametes", str);
    									}
    								}
    								else
    									map.put("programName", "");
    								map.put("parametes", "");


    								if(map!=null && !map.isEmpty())
    									finalSchList.add(map);
    							}
    						}

    					}
    					if(conn!=null)
    						conn.close();
    					if(stmt!=null)
    						stmt.close();
    					if(stmt2!=null)
    						stmt2.close();
    					if(result2!=null)
    						result2.close();
    					/*if(resultCount!=null)
    						resultCount.close();*/

    				}
    				else
    				{
    					log.info("connection is null");
    				}

    			/*}
    			else
    			{
    				log.info("PASS is null");
    			}
    		}
    		else
    		{
    			log.info("USER is null");
    		}
    	}
    	else
    	{
    		log.info("DB_URL is null");
    	}
*/
    	return finalSchList;

    				}
    
    /**
     * author:ravali
     * @param oozieJobDTO
     * @param tenantId
     * @param userId
     * Desc: API to build oozie job
     */
	@PostMapping("/getAllSchedulersListByFilters")
	@Timed
	public List<HashMap> getAllSchedulersListByFilters(HttpServletRequest request,
			@RequestParam(value = "page", required = true) Integer page,
			@RequestParam(value = "per_page", required = true) Integer size, HttpServletResponse response,@RequestBody(required=false) HashMap filtersMap)
			throws OozieClientException, SQLException, ClassNotFoundException {

		log.info("Request Rest to fetch schedulers list with filters " + filtersMap);
		HashMap map0 = userJdbcService.getuserInfoFromToken(request);
		Long tenantId = Long.parseLong(map0.get("tenantId").toString());
		Long userId = Long.parseLong(map0.get("userId").toString());
		List<HashMap> finalSchedularsList = new ArrayList<HashMap>();
		List<String> schedIds=new ArrayList<String>();
		if(filtersMap.get("scheduledProgram").toString().equals("0")&&filtersMap.get("scheduledType").toString().equalsIgnoreCase("ALL")) { //All programs and all schedule type
			schedIds=jobDetailsRepository.getSchedListByUser(userId);
		}else if(filtersMap.get("scheduledProgram").toString().equals("0")&&!filtersMap.get("scheduledType").toString().equalsIgnoreCase("ALL")) { //All programs and selected schedule type
			schedIds=jobDetailsRepository.getSchedListByUserBySched(userId,filtersMap.get("scheduledType").toString());
		}else if(!filtersMap.get("scheduledProgram").toString().equals("0")&&filtersMap.get("scheduledType").toString().equalsIgnoreCase("ALL")) { //Selected programs and all schedule type
			schedIds=jobDetailsRepository.getSchedListByUserByProgram(userId,Long.parseLong(filtersMap.get("scheduledProgram").toString()));
		}else if(!filtersMap.get("scheduledProgram").toString().equals("0")&&!filtersMap.get("scheduledType").toString().equalsIgnoreCase("ALL")){ //Selected program and selected schedule type
			schedIds=jobDetailsRepository.getSchedListByUserByProgramBySched(userId,Long.parseLong(filtersMap.get("scheduledProgram").toString()),filtersMap.get("scheduledType").toString());
		}
		if(schedIds.size()<1) {
			response.addIntHeader("X-COUNT", 0);
			log.info("No jobs are qualified >>>>>>>>>>>");
			return finalSchedularsList;
		}
		String schIds=schedIds.stream().collect(Collectors.joining("','", "'", "'"));
//		Create connection with properties
		String oozieUrl=env.getProperty("oozie.OozieClient");
    	String schema = env.getProperty("oozie.ozieSchema");
    	Connection conn =null;
    	Statement stmt = null;
    	Statement stmt2=null;
    	DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("oozieDataSource");
		conn = ds.getConnection();
		if (conn != null) {
			log.info("Connected database successfully...");
			stmt = conn.createStatement();
			stmt2 = conn.createStatement();
			ResultSet result2 = null;
			ResultSet resultCount = null;
			String baseQuery = "";
			List<String> schedStatusList=(List<String>) filtersMap.get("scheduledStatusList");
			if(schedStatusList.size()>0) {	//Status list selected
				baseQuery=baseQuery+" AND status IN "+schedStatusList.toString().replaceAll(", ", "','").replace("[", "('").replace("]", "')");
			}
			log.info("Start Date >>>: "+filtersMap.get("stDateFrom")+"  End Date >>>>>>>: "+filtersMap.get("stDateTo"));
			if(filtersMap.get("stDateFrom")!=null && filtersMap.get("stDateTo")!=null) {	// Start and End dates selected
				baseQuery=baseQuery+" AND date(start_time) BETWEEN '"+filtersMap.get("stDateFrom").toString().split("T")[0]+"' AND '"+filtersMap.get("stDateTo").toString().split("T")[0]+"'";
			}else if(filtersMap.get("stDateFrom")!=null) {	// Start date selected
				baseQuery=baseQuery+" AND date(start_time) >= '"+filtersMap.get("stDateFrom").toString().split("T")[0]+"'";
			}else if(filtersMap.get("stDateTo")!=null) {	// End date selected
				baseQuery=baseQuery+" AND date(start_time) <= '"+filtersMap.get("stDateTo").toString().split("T")[0]+"'";
			}
//			Read total count
			resultCount = stmt2.executeQuery("select count(*) from " + schema + ".recon_oozie_jobs_v where parent_id in (" + schIds + ") "+baseQuery);
			while (resultCount.next()) {
				response.addIntHeader("X-COUNT", Integer.valueOf(resultCount.getString("count(*)")));
			}
			baseQuery=baseQuery+" order by start_time desc Limit " + (page) * size + ", " + size;
			log.info("base query for oozie table : "+baseQuery);
			result2 = stmt.executeQuery("select * from " + schema + ".recon_oozie_jobs_v where parent_id in (" + schIds + ") "+baseQuery);
			if (resultCount != null)
				resultCount.close();
			List<HashMap> oozieList = new ArrayList<HashMap>();
//			Build final list
		
			while (result2.next()) {
				int count = 0;
				SchedulerDetails sch = schedulerDetailsRepository.findByOozieJobId(result2.getString("parent_id"));
				if (sch != null) {
					if (sch.getOozieJobId() != null) {
						HashMap map = new HashMap();
						count = count + 1;
						map.put("seqNo", count);
						if( sch.isIsViewed()!=null)
						map.put("isViewed", sch.isIsViewed());
						else
							map.put("isViewed", "");
						if (result2.getString("job_id") != null)
							map.put("oozieJobId", result2.getString("job_id"));
						else
							map.put("oozieJobId", "");
						if (result2.getString("start_time") != null)
							map.put("startTime", result2.getTimestamp("start_time").toInstant());
						else
							map.put("startTime", "");
						if (result2.getString("child_id") != null)
							map.put("childId", result2.getString("child_id"));
						else
							map.put("childId", "");
						if (result2.getString("end_time") != null)
							map.put("endTime",result2.getTimestamp("end_time").toInstant());
						else
							map.put("endTime", "");

						if (result2.getString("status") != null)
							map.put("oozieStatus", result2.getString("status").toString());
						else
							map.put("oozieStatus", "");

						JobDetails jobDetails = jobDetailsRepository.findOne(sch.getJobId());
						map.put("jobId", jobDetails.getIdForDisplay());
						if (jobDetails != null && jobDetails.getJobName() != null)
							map.put("jobName", jobDetails.getJobName());
						else
							map.put("jobName", "");
						if (jobDetails != null && jobDetails.getJobDescription() != null)
							map.put("jobDescription", jobDetails.getJobDescription());
						else
							map.put("jobDescription", "");
						if (sch.getStatus() != null)
							map.put("status", sch.getStatus());
						else
							map.put("status", "");
						map.put("localStatDate", sch.getStartDate());
						map.put("localEndDate", sch.getEndDate());

						if (sch.getFrequency() != null) {
							LookUpCode lookUpCode = lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("SCHEDULE_TYPES", sch.getFrequency(), tenantId);
							map.put("frequencyType", lookUpCode.getMeaning());
						} else
							map.put("frequencyType", "");

						if (sch.getSchedulerName() != null)
							map.put("schedulerName", sch.getSchedulerName());
						else
							map.put("schedulerName", "");

						if (jobDetails != null && jobDetails.getProgrammId() != null) {
							ApplicationPrograms progName = applicationProgramsRepository.findOne(jobDetails.getProgrammId());
							if (progName.getPrgmName() != null)
								map.put("programName", progName.getPrgmName());
							map.put("programId", jobDetails.getProgrammId());
							List<ProgParametersSets> prog = progParametersSetsRepository.findByProgramId(jobDetails.getProgrammId());
							String paramList = "";
							int k = 0;
							if (k < prog.size()) {
								if (jobDetails.getParameterArguments1() == null)
									paramList = paramList + " ,";
								else
									paramList = paramList + jobDetails.getParameterArguments1() + ",";
							}
							k++;
							if (k < prog.size()) {
								if (jobDetails.getParameterArguments2() == null)
									paramList = paramList + " ,";
								else
									paramList = paramList + jobDetails.getParameterArguments2() + ",";
							}
							k++;
							if (k < prog.size()) {
								if (jobDetails.getParameterArguments3() == null)
									paramList = paramList + " ,";
								else
									paramList = paramList + jobDetails.getParameterArguments3() + ",";
							}
							k++;
							if (k < prog.size()) {
								if (jobDetails.getParameterArguments4() == null)
									paramList = paramList + " ,";
								else
									paramList = paramList + jobDetails.getParameterArguments4() + ",";
							}
							k++;
							if (k < prog.size()) {
								if (jobDetails.getParameterArguments5() == null)
									paramList = paramList + " ,";
								else
									paramList = paramList + jobDetails.getParameterArguments5() + ",";
							}
							k++;
							if (k < prog.size()) {
								if (jobDetails.getParameterArguments6() == null)
									paramList = paramList + " ,";
								else
									paramList = paramList + jobDetails.getParameterArguments6() + ",";
							}
							k++;
							if (k < prog.size()) {
								if (jobDetails.getParameterArguments7() == null)
									paramList = paramList + " ,";
								else
									paramList = paramList + jobDetails.getParameterArguments7() + ",";
							}
							k++;
							if (k < prog.size()) {
								if (jobDetails.getParameterArguments8() == null)
									paramList = paramList + " ,";
								else
									paramList = paramList + jobDetails.getParameterArguments8() + ",";
							}
							k++;
							if (k < prog.size()) {
								if (jobDetails.getParameterArguments9() == null)
									paramList = paramList + " ,";
								else
									paramList = paramList + jobDetails.getParameterArguments9() + ",";
							}
							k++;
							if (k < prog.size()) {
								if (jobDetails.getParameterArguments10() == null)
									paramList = paramList + " ,";
								else
									paramList = paramList + jobDetails.getParameterArguments10() + ",";
							}
							if (paramList != null && paramList.length() != 0) {
								String str = paramList.substring(0, paramList.length() - 1);
								map.put("parametes", str);
							}
						} else
							map.put("programName", "");
						map.put("parametes", "");
						if(sch.getCreatedBy()!=null)
						{
							HashMap currAppInfo=userJdbcService.jdbcConnc(sch.getCreatedBy(),tenantId);
				  
				    		if(currAppInfo.get("assigneeName")!=null)
				    			map.put("userName", currAppInfo.get("assigneeName"));
				    		else
				    			map.put("userName", "");
						}
							
						if (map != null && !map.isEmpty())
							finalSchedularsList.add(map);
					}
				}
			}
			if (conn != null)
				conn.close();
			if (stmt != null)
				stmt.close();
			if (stmt2 != null)
				stmt2.close();
			if (result2 != null)
				result2.close();
		} else {
			log.info("connection is null");
		}
		log.info("finalSchedularsList :"+finalSchedularsList.size());
		return finalSchedularsList;
	}

    
    
    
    
    
    /**
     * author:ravali
     * @param oozieJobDTO
     * @param tenantId
     * @param userId
     * Desc: API to build oozie job
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/buildOozieJob")
    @Timed
    public void BuildOozieJob(@RequestBody OozieJobDTO oozieJobDTO,@RequestParam Long tenantId,@RequestParam Long userId,HttpServletRequest request)
   
    {

    	System.out.println("Started");
    	log.info("Sample Log");
    	oozieService.BuildOozieJob(oozieJobDTO,tenantId,userId,request);

    }

    
    
    /**
     * author:ravali
     * @param oozieJobDTO
     * Desc:API to initiate oozie job
     * @return
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/initiateOozieJob")
    @Timed
    public String InitiateOozieJob(@RequestBody OozieJobDTO oozieJobDTO)
   
    {

    	System.out.println("Started");
    	String jobId=oozieService.InitiateOozieJob(oozieJobDTO);
    	return jobId;

    }
    
   
    /**
     * author:ravali
     * @param jobId
     * @param rerunType
     * @param scope
     * @throws IOException
     * Desc:API to rerun coordinate job
     */
    @SuppressWarnings("unchecked")
    @GetMapping("/rerunOozieCoordJob")
    @Timed
    public void rerunOozieCoordJob(@RequestParam String jobId, @RequestParam String rerunType, @RequestParam String scope) throws IOException
    {	  
        
    	System.out.println("REST API FOR RE-RUNNING OOZIE JOB");
    	oozieService.rerunOozieCoordJob(jobId, rerunType, scope);
    	
    }
    
    
    /*
     * Rest API "killOozieCoordJob" is used to kill the Coordinator jobs
     * @param jobId is Coordinator JobId
     * @param rangeType type date if -date is used , action-num if -action is used
     * @param scope kill scope for date or action nums
     */
    @SuppressWarnings("unchecked")
    @GetMapping("/killOozieCoordJob")
    @Timed
    public void killOozieCoordJob(@RequestParam String jobId, @RequestParam String rangeType, @RequestParam String scope)
    {	  
        
    	System.out.println("REST API FOR Killing OOZIE JOB");
    	oozieService.killOozieCoordJob(jobId, rangeType, scope);
    	
    	
    
    	    	
    }

    /*
     * Rest API "SuspendOozieCoordJob" is used to kill the Coordinator jobs
     * @param jobId is Coordinator JobId
     * @param rangeType type date if -date is used , action-num if -action is used
     * @param scope kill scope for date or action nums
     */
    @SuppressWarnings("unchecked")
    @GetMapping("/suspendOozieCoordJob")
    @Timed
    public void suspendOozieCoordJob(@RequestParam String jobId)
    {	  
        
    	System.out.println("REST API FOR Suspend OOZIE JOB");
    	
    	oozieService.suspendOozieCoordJob(jobId);
     	
    }
   

    
   /*  * Rest API "resumeOozieCoordJob" is used to resume the Coordinator jobs
     * @param jobId is Coordinator JobId
     
    */
    @SuppressWarnings("unchecked")
    @GetMapping("/resumeOozieCoordJob")
    @Timed
    public void resumeOozieCoordJob(@RequestParam String jobId)
    {	  
        
    	System.out.println("REST API FOR Resume OOZIE JOB");
    	oozieService.resumeOozieCoordJob(jobId);
    	
    	
   	
    }
   
    
    
  /*  
     * Rest API "OozieCoordJobInfo" is used to print Information of Coordinator jobs
     * @param jobId is Coordinator JobId
     */
    @SuppressWarnings("unchecked")
    @GetMapping("/OozieCoordJobInfo")
    @Timed
    public void OozieCoordJobInfo(@RequestParam String jobId)
    {	  
        
    	System.out.println("REST API FOR OOZIE JOB Info");
    	
    	oozieService.OozieCoordJobInfo(jobId);
    	
    	
    
    }
    
    
   /** Rest API "OozieWFJobInfo" is used to print Information of Coordinator jobs
     * @param jobId is Workflow JobId
     */
    @SuppressWarnings("unchecked")
    @GetMapping("/OozieWFJobInfo")
    @Timed
    public void OozieWFJobInfo(@RequestParam String jobId)
    {	  
        
    	System.out.println("REST API FOR OOZIE JOB Info");
    	
    	oozieService.OozieWFJobInfo(jobId);
   
    }
    
   /* 
     * Rest API "OozieJobLog" is used to print Information of Coordinator jobs and WF Log
     * @param jobId is Workflow JobId or Coordinator ID
     */
    @SuppressWarnings("unchecked")
    @GetMapping("/OozieJobLog")
    @Timed
    public String OozieJobLog(@RequestParam String jobId)
    {	  
        
    	System.out.println("REST API FOR OOZIE JOB Log");
    	String jobInfo=oozieService.OozieJobLog(jobId);
		return jobInfo;
		
    	
    }
    
 
    
   /* 
     * Rest API "OozieJobDef" is used to print Information of Coordinator jobs
     * @param jobId is Coordinator JobId
     */
    @SuppressWarnings("unchecked")
    @GetMapping("/OozieJobDef")
    @Timed
    public void OozieJobDef(@RequestParam String jobId)
    {	  
        
    	System.out.println("REST API FOR OOZIE JOB Info");
    	
    	oozieService.OozieJobDef(jobId);
    	
    	
    }


@GetMapping("/oozieKillJob")
@Timed 

public String kill(@RequestParam String jobId) throws OozieClientException
{
	log.info("Rest Request to kill job :"+jobId);
	String status=oozieService.KillOzieParentJob(jobId);
	return status;
}

/**
 * author:ravali
 * @param jobId
 * @return
 * @throws OozieClientException
 */
@GetMapping("/oozieRerunWFJob")
@Timed 

public String rerunWF(@RequestParam String jobId) throws OozieClientException
{
	log.info("Rest Request to rerun WF :"+jobId);
	String status=oozieService.rerunOozieWFJob(jobId);
	return status;
}


/**
 * author:ravali
 * @param jobId
 * @return
 * @throws OozieClientException
 */

@GetMapping("/getStatusOfJob")
@Timed 

	  public String getStatusOfOozieJobId(@RequestParam String jobId) throws OozieClientException
	  {	  

		  String status=oozieService.getStatusOfOozieJobId(jobId);
		  return status;

	  }




//hardcoded pgm namesss
@GetMapping("/getjobAndSchedulerDetailsForDataMangmt")
@Timed 
public List<HashMap> getjobSchedulersList(HttpServletRequest request,
		@RequestParam Long profileId,@RequestParam(value = "templateId" , required = false) Long templateId) throws  SQLException, ClassNotFoundException
		{
	HashMap map=userJdbcService.getuserInfoFromToken(request);
	Long tenantId=Long.parseLong(map.get("tenantId").toString());
	log.info("Request Rest to fetch schedulers list with jobId for tenant: "+tenantId);

	List<HashMap> finalSchList=new ArrayList<HashMap>();


	List<String> applPro=new ArrayList<String>();
	applPro.add("DataTransformation");
	applPro.add("DataExtraction");
	//LinkedHashMap<String,Integer> dataTranfMap=new LinkedHashMap<String,Integer>();
	//LinkedHashMap<String,Integer> dataExtMap=new LinkedHashMap<String,Integer>();
	
	
	List<BigInteger> appProgIds=applicationProgramsRepository.findByPrgmNameInAndTenantId( tenantId,applPro);
	String progIds=appProgIds.toString().replace("[", "").replace("]", "");
	//List<ProgParametersSets> pPSList=progParametersSetsRepository.findByProgramIdIn(app);
	
	
	
	for(String prog:applPro)
	{
		LinkedHashMap<String,Integer> dtTrOrExtMAp=new LinkedHashMap<String,Integer>();
		ApplicationPrograms app=applicationProgramsRepository.findByPrgmNameAndTenantId(prog,tenantId);
		if(app!=null && app.getId()!=null)
		{
			
			List<ProgParametersSets> pPSList=progParametersSetsRepository.findByProgramId(app.getId());
			for(int i=0;i<pPSList.size();i++)
			{
				if(pPSList.get(i).getParameterName().equalsIgnoreCase("SourceProfileName"))
					dtTrOrExtMAp.put("SourceProfileName", i+1);
				if(pPSList.get(i).getParameterName().equalsIgnoreCase("TemplateName"))
					dtTrOrExtMAp.put("TemplateName", i+1);
				
			}

		}
	//}

	log.info("dtTrOrExtMAp Positions :"+dtTrOrExtMAp);


/*	String dbUrl=env.getProperty("spring.datasource.url");
	String[] parts=dbUrl.split("[\\s@&?$+-]+");
	String host = parts[0].split("/")[2].split(":")[0];
	log.info("host :"+host);
	String schemaName=parts[0].split("/")[3];
	log.info("schemaName :"+schemaName);
	String userName = env.getProperty("spring.datasource.username");
	String password = env.getProperty("spring.datasource.password");*/



	Connection conn =null;

	Statement stmt = null;
	Statement stmt2=null;

	//Class.forName("com.mysql.jdbc.Driver");
	//log.info("env.getProperty(spring.datasource.jdbcdriver) :"+env.getProperty("spring.datasource.jdbcdriver"));
	//Class.forName(env.getProperty("spring.datasource.jdbcdriver"));
	
	
	DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
	conn = ds.getConnection();
	
	//if(dbUrl != null)
	//{
		//if(userName != null)
		//{
			//if(password != null)
			//{

				//conn = DriverManager.getConnection(dbUrl, userName, password);
				if(conn != null)
				{
					log.info("Connected database successfully...");
					stmt = conn.createStatement();
					stmt2=conn.createStatement();

					String finalQuery="";
					if(templateId!=null)
					{
						String param2 = "parameter_arguments_";
						param2 = param2+dtTrOrExtMAp.get("TemplateName");
						finalQuery="select * from t_job_details where tenant_id="+tenantId +" and programm_id in ("+app.getId()+") and parameter_arguments_"+
								dtTrOrExtMAp.get("SourceProfileName")+" in("+profileId+") and ("+param2 +"= " +templateId+" or "+param2 +" like '"+templateId+",%' or "+ param2 +" like '%,"+templateId+"%' or "+param2+" like '%,"+templateId+",%' or "+param2+" is null)";
					}
					else
					{
						List<BigInteger> spf=sourceProfileFileAssignmentsRepository.fetchTempIdsBySrcProfile(profileId);
						log.info("spf :"+spf);
						String tempIds="";
						String temps = "";
						if(spf.size()>0)
						{
							tempIds = spf.toString().replace("[", "").replace("]", "").replaceAll("\\s","");
							temps = "and (parameter_arguments_"+dtTrOrExtMAp.get("TemplateName")+" in("+tempIds+") or parameter_arguments_"+dtTrOrExtMAp.get("TemplateName")+" is null) ";
						
						}
						log.info(tempIds);
						finalQuery="select * from t_job_details where tenant_id="+tenantId +" and programm_id in ("+app.getId()+") and parameter_arguments_"+dtTrOrExtMAp.get("SourceProfileName")+" in("+profileId+") "+temps;

					}

					log.info("finalQuery :"+finalQuery );
					ResultSet result=stmt2.executeQuery(finalQuery);
					while(result.next())
					{
						HashMap jobsMap=new HashMap(); 
						if(result.getString("id")!=null)
							jobsMap.put("jobId", result.getString("id"));
						if(result.getString("job_name")!=null)
							jobsMap.put("jobName", result.getString("job_name"));
						if(result.getString("programm_id")!=null)
						{
							//ApplicationPrograms prog=applicationProgramsRepository.findOne(Long.valueOf(result.getString("programm_id")));
							jobsMap.put("programName", app.getPrgmName());
						}

						if(result.getString("id")!=null)
						{

							List<SchedulerDetails> schedularList=schedulerDetailsRepository.findByJobId(Long.valueOf(result.getString("id")));
							for(int j=0;j<schedularList.size();j++)
							{
								


								jobsMap.put("schedulerid", schedularList.get(j).getId());
								if(schedularList.get(j).getFrequency()!=null)
									jobsMap.put("type", schedularList.get(j).getFrequency());
								else
									jobsMap.put("type", "");

								if(schedularList.get(j).getFrequencyValue()!=null)
									jobsMap.put("occurance", schedularList.get(j).getFrequencyValue());
								else
									jobsMap.put("occurance", "");
								if(schedularList.get(j).getTime()!=null)
									jobsMap.put("time", schedularList.get(j).getTime());
								else
									jobsMap.put("time","");
								if(schedularList.get(j).getOozieJobId()!=null && !schedularList.get(j).getOozieJobId().isEmpty())
									jobsMap.put("oozieJobId", schedularList.get(j).getOozieJobId());
								else
									jobsMap.put("oozieJobId", "");

								if(schedularList.get(j).getDayOf()!=null)
								{
									List<String> myList = new ArrayList<String>(Arrays.asList(schedularList.get(j).getDayOf().split(",")));
									jobsMap.put("day", myList);
								}
								else
									jobsMap.put("day", "");
								if(schedularList.get(j).getMinutes()!=null)
									jobsMap.put("minutes",schedularList.get(j).getMinutes());
								else
									jobsMap.put("minutes","");
								if(schedularList.get(j).getHours()!=null)
									jobsMap.put("hours", schedularList.get(j).getHours());
								else
									jobsMap.put("hours", "");
								if(schedularList.get(j).getMonth()!=null)
								{
									List<String> myList = new ArrayList<String>(Arrays.asList(schedularList.get(j).getMonth().split(",")));
									jobsMap.put("month", myList);
								}
								else
									jobsMap.put("month", "");

								if(schedularList.get(j).getWeekDay()!=null)
								{
									List<String> myList = new ArrayList<String>(Arrays.asList(schedularList.get(j).getWeekDay().split(",")));
									jobsMap.put("weekDay", myList);
								}
								else
									jobsMap.put("weekDay", "");
								if(schedularList.get(j).getDate()!=null)
									jobsMap.put("date", schedularList.get(j).getDate());
								else
									jobsMap.put("date", "");

							}

						}

						finalSchList.add(jobsMap);
					}



					if(conn!=null)
						conn.close();
					if(stmt!=null)
						stmt.close();
					if(stmt2!=null)
						stmt2.close();
					if(result!=null)
						result.close();


				}
				else
				{
					log.info("connection is null");
				}

			//}
			/*else
			{
				log.info("PASS is null");
			}
		}
		else
		{
			log.info("USER is null");
		}
	}
	
	else
	{
		log.info("DB_URL is null");
	}*/
	}
	return finalSchList;

		}



/**
 *author :ravali
 *Desc :API to fetch details of last run of a job
 * @param oozieJobId
 * @return
 * @throws OozieClientException
 * @throws SQLException
 * @throws ClassNotFoundException
 */
@GetMapping("/getLastRunOfAJob")
@Timed 
public HashMap getLastRunOfAJob(
		@RequestParam String oozieJobId) throws OozieClientException, SQLException, ClassNotFoundException
		{
	log.info("Request Rest to fetch last run of a job"+oozieJobId);

	
	HashMap lastRunMap=new HashMap();
	

	String DB_URL = env.getProperty("oozie.ozieUrl");
	String USER = env.getProperty("oozie.ozieUser");
	String PASS = env.getProperty("oozie.oziePswd");
	String OZIESCHEMA = env.getProperty("oozie.ozieSchema");

	Connection conn =null;

	Statement stmt = null;


	//Class.forName("com.mysql.jdbc.Driver");
	log.info("env.getProperty(spring.datasource.jdbcdriver) :"+env.getProperty("spring.datasource.jdbcdriver"));
	Class.forName(env.getProperty("spring.datasource.jdbcdriver"));
	if(DB_URL != null)
	{
		if(USER != null)
		{
			if(PASS != null)
			{

				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				if(conn != null)
				{
					log.info("Connected database successfully...");
					stmt = conn.createStatement();


					
					String finalQuery="SELECT id,created_time,external_id,last_modified_time,status FROM "+OZIESCHEMA+".COORD_ACTIONS where job_id='"+oozieJobId+
							"' and external_id is not null order by 2 desc  limit 1";

					log.info("finalQuery :"+finalQuery);
					
					

					ResultSet result=stmt.executeQuery(finalQuery);
					String timeZone=env.getProperty("oozie.oozieServerTimeZone");
    				log.info("timeZone :"+timeZone);

					while(result.next())
					{
						
						
						
						//ZonedDateTime dateTime=ZonedDateTime.ofInstant(result.getTimestamp("last_modified_time").toInstant(), ZoneId.of(timeZone));
						//log.info("dateTime :"+dateTime);
						
						if(result.getString("id")!=null)
							lastRunMap.put("id", result.getString("id"));
						else
							lastRunMap.put("id", "");
						if(result.getString("created_time")!=null)
						{
							log.info("createdTime TimeStamp:"+result.getTimestamp("created_time"));
							log.info("createdTime String:"+result.getString("created_time"));
							lastRunMap.put("createdTime", result.getTimestamp("created_time").toInstant());
						}
						else
							lastRunMap.put("createdTime", "");
						if(result.getString("external_id")!=null)
							lastRunMap.put("externalId", result.getString("external_id"));
						else
							lastRunMap.put("externalId", "");
						if(result.getString("last_modified_time")!=null)
						{
							log.info("lastModifiedTime TimeStamp:"+result.getTimestamp("last_modified_time"));
							log.info("lastModifiedTime String:"+result.getString("last_modified_time"));
							lastRunMap.put("lastModifiedTime", result.getTimestamp("last_modified_time").toInstant());
						}
						else
							lastRunMap.put("lastModifiedTime", "");
						if(result.getString("status")!=null)
							lastRunMap.put("status", result.getString("status"));
						else
							lastRunMap.put("status", "");
						
					}


					if(conn!=null)
						conn.close();
					if(stmt!=null)
						stmt.close();

					if(result!=null)
						result.close();


				}
				else
				{
					log.info("connection is null");
				}

			}
			else
			{
				log.info("PASS is null");
			}
		}
		else
		{
			log.info("USER is null");
		}
	}
	else
	{
		log.info("DB_URL is null");
	}

	return lastRunMap;

		}

/**
 * author :ravali
 * Desc :API to know the nxt run of the job
 * @param oozieJobId
 * @return
 * @throws OozieClientException
 * @throws SQLException
 * @throws ClassNotFoundException
 */

@GetMapping("/getNxtRunOfAJob")
@Timed 
public HashMap getNxtRunOfAJob(
		@RequestParam String oozieJobId) throws OozieClientException, SQLException, ClassNotFoundException
		{
	log.info("Request Rest to fetch next run of a job"+oozieJobId);

	HashMap lastRunMap=new HashMap();
	
	lastRunMap = oozieService.getNextRunByOozieJobId(oozieJobId);
	return lastRunMap;

		}


@GetMapping("/testOozieStatus")
@Timed
public HashMap testOozieProcessWithUrl(@RequestParam(value="domain",required=false) String  domain) throws IOException
{
	HashMap map=new HashMap();
	if(domain!=null && domain.equalsIgnoreCase("db"))
		map=oozieService.checkingOozieHost();
	else
	{
		HashMap dbMap=new HashMap();
		dbMap=oozieService.checkingOozieHost();
		if(dbMap.get("dbStatus").toString().equalsIgnoreCase("true"))
		{
			map.put("dbStatus", dbMap.get("dbStatus"));
			boolean status=oozieService.testOozieProcessWithUrl();
			map.put("ooziestatus", status);
		}
		else
		{
			map.put("dbStatus", dbMap.get("dbStatus"));
		}
	}
	    return map;
}


@PostMapping("/updateSchedulersIsViewedAsTrue")
@Timed
public void updateSchedulersIsViewedAsTrue(HttpServletRequest request){
	log.info("Updating schedulers record by setting is_viewed column as true based on is viewed false");
	HashMap finalMap = new HashMap();
	HashMap map0=userJdbcService.getuserInfoFromToken(request);
	Long tenantId=Long.parseLong(map0.get("tenantId").toString());
	Long userId=Long.parseLong(map0.get("userId").toString());

	List<Long> updatedNotIds = new ArrayList<Long>();

	List<BigInteger> jobIds=jobDetailsRepository.fetchByCreatedBy(userId);
	log.info("jobIds :"+jobIds);
	List<SchedulerDetails> schDetails=schedulerDetailsRepository.findByJobIdInAndIsViewedFalse(jobIds);
	List<SchedulerDetails> schUpdatingList=new ArrayList<SchedulerDetails>();
	if(schDetails.size()>0)
	{
		for(SchedulerDetails sch : schDetails)
		{


			sch.setIsViewed(true);
			sch.setLastUpdatedBy(userId);
			sch.setLastUpdatedDate(ZonedDateTime.now());
			schUpdatingList.add(sch);

		}
	}
	schUpdatingList=schedulerDetailsRepository.save(schUpdatingList);
	}




}