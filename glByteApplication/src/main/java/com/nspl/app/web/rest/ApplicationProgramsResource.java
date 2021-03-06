package com.nspl.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.config.ApplicationContextProvider;
import com.nspl.app.domain.ApplicationPrograms;
import com.nspl.app.domain.JobDetails;
import com.nspl.app.domain.ProgParametersSets;
import com.nspl.app.domain.SchedulerDetails;
import com.nspl.app.repository.ApplicationProgramsRepository;
import com.nspl.app.repository.JobDetailsRepository;
import com.nspl.app.repository.SchedulerDetailsRepository;
import com.nspl.app.repository.search.ApplicationProgramsSearchRepository;
import com.nspl.app.web.rest.util.HeaderUtil;
import com.nspl.app.repository.ProgParametersSetsRepository;
import com.nspl.app.service.ApplicationProgramsService;
import com.nspl.app.service.PropertiesUtilService;
//import com.nspl.app.service.SourceConnectionDetailsService;




import com.nspl.app.service.UserJdbcService;

import io.github.jhipster.web.util.ResponseUtil;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import static org.elasticsearch.index.query.QueryBuilders.*;

import org.springframework.web.multipart.MultipartFile;

import au.com.bytecode.opencsv.CSVReader;

import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.io.IOException;

/**
 * REST controller for managing ApplicationPrograms.
 */
@RestController
@RequestMapping("/api")
public class ApplicationProgramsResource {

	private final Logger log = LoggerFactory.getLogger(ApplicationProgramsResource.class);

	private static final String ENTITY_NAME = "applicationPrograms";

	private final ApplicationProgramsRepository applicationProgramsRepository;

	@Inject
	ProgParametersSetsRepository progParametersSetsRepository;

	@PersistenceContext(unitName="default")
	private EntityManager em;

	@Inject
	PropertiesUtilService propertiesUtilService;


	@Inject
	JobDetailsRepository jobDetailsRepository;
	
	
	@Inject
	SchedulerDetailsRepository schedulerDetailsRepository;

	@Autowired
	Environment env;
	
	@Inject
	UserJdbcService userJdbcService;
	
	@Inject
	ApplicationProgramsService applicationProgramsService;
	
	private final ApplicationProgramsSearchRepository applicationProgramsSearchRepository;

	public ApplicationProgramsResource(ApplicationProgramsRepository applicationProgramsRepository, ApplicationProgramsSearchRepository applicationProgramsSearchRepository) {
		this.applicationProgramsRepository = applicationProgramsRepository;
		this.applicationProgramsSearchRepository = applicationProgramsSearchRepository;
	}

	/**
	 * POST  /application-programs : Create a new applicationPrograms.
	 *
	 * @param applicationPrograms the applicationPrograms to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new applicationPrograms, or with status 400 (Bad Request) if the applicationPrograms has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/application-programs")
	@Timed
	public ResponseEntity<ApplicationPrograms> createApplicationPrograms(@RequestBody ApplicationPrograms applicationPrograms) throws URISyntaxException {
		log.debug("REST request to save ApplicationPrograms : {}", applicationPrograms);
		if (applicationPrograms.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new applicationPrograms cannot already have an ID")).body(null);
		}
		ApplicationPrograms result = applicationProgramsRepository.save(applicationPrograms);
		applicationProgramsSearchRepository.save(result);
		return ResponseEntity.created(new URI("/api/application-programs/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /application-programs : Updates an existing applicationPrograms.
	 *
	 * @param applicationPrograms the applicationPrograms to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated applicationPrograms,
	 * or with status 400 (Bad Request) if the applicationPrograms is not valid,
	 * or with status 500 (Internal Server Error) if the applicationPrograms couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/application-programs")
	@Timed
	public ResponseEntity<ApplicationPrograms> updateApplicationPrograms(@RequestBody ApplicationPrograms applicationPrograms) throws URISyntaxException {
		log.debug("REST request to update ApplicationPrograms : {}", applicationPrograms);
		if (applicationPrograms.getId() == null) {
			return createApplicationPrograms(applicationPrograms);
		}
		ApplicationPrograms result = applicationProgramsRepository.save(applicationPrograms);
		applicationProgramsSearchRepository.save(result);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, applicationPrograms.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /application-programs : get all the applicationPrograms.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of applicationPrograms in body
	 */
	@GetMapping("/application-programs")
	@Timed
	public List<ApplicationPrograms> getAllApplicationPrograms() {
		log.debug("REST request to get all ApplicationPrograms");
		List<ApplicationPrograms> applicationPrograms = applicationProgramsRepository.findAll();
		return applicationPrograms;
	}

	/**
	 * GET  /application-programs/:id : get the "id" applicationPrograms.
	 *
	 * @param id the id of the applicationPrograms to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the applicationPrograms, or with status 404 (Not Found)
	 */
	@GetMapping("/application-programs/{id}")
	@Timed
	public ResponseEntity<ApplicationPrograms> getApplicationPrograms(@PathVariable Long id) {
		log.debug("REST request to get ApplicationPrograms : {}", id);
		ApplicationPrograms applicationPrograms = applicationProgramsRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(applicationPrograms));
	}

	/**
	 * DELETE  /application-programs/:id : delete the "id" applicationPrograms.
	 *
	 * @param id the id of the applicationPrograms to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/application-programs/{id}")
	@Timed
	public ResponseEntity<Void> deleteApplicationPrograms(@PathVariable Long id) {
		log.debug("REST request to delete ApplicationPrograms : {}", id);
		applicationProgramsRepository.delete(id);
		applicationProgramsSearchRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH  /_search/application-programs?query=:query : search for the applicationPrograms corresponding
	 * to the query.
	 *
	 * @param query the query of the applicationPrograms search 
	 * @return the result of the search
	 */
	@GetMapping("/_search/application-programs")
	@Timed
	public List<ApplicationPrograms> searchApplicationPrograms(@RequestParam String query) {
		log.debug("REST request to search ApplicationPrograms for query {}", query);
		return StreamSupport
				.stream(applicationProgramsSearchRepository.search(queryStringQuery(query)).spliterator(), false)
				.collect(Collectors.toList());
	}


	@GetMapping("/programsListByTenantId")
	@Timed
	public List<ApplicationPrograms> getProgramsListByTenantId(HttpServletRequest request) throws SQLException
	{
		HashMap map0=userJdbcService.getuserInfoFromToken(request);
      	Long tenantId=Long.parseLong(map0.get("tenantId").toString());
		log.info("rest request to get application programs list by tenantId:"+tenantId);
		List<ApplicationPrograms> getAllPrograms=applicationProgramsRepository.findByTenantIdAndEnableIsTrue(tenantId);
		return getAllPrograms;
	}
	
	
	
	
	/**
	 * Author Ravali
	 * @param tenantId
	 * @return
	 * @throws SQLException 
	 */
	/* @GetMapping("/programe")
    @Timed
    public List<HashMap> getProgrammes(@RequestParam Long tenantId) throws SQLException
    {*/
	@GetMapping("/programe")
	@Timed
	public List<HashMap> getProgrammes(HttpServletRequest request,@RequestParam Long programId) throws SQLException
	{
		HashMap map0=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map0.get("tenantId").toString());
		log.info("rest request to get application programs for tenant: "+tenantId);
		//List<ApplicationPrograms> getAllPrograms=applicationProgramsRepository.findByTenantIdAndEnableIsTrue(tenantId);
		//List<ApplicationPrograms> getAllPrograms=applicationProgramsRepository.findByTenantId(tenantId);
		HashMap programs=new HashMap();
		List<HashMap> map=new ArrayList<HashMap>();
		//if(getAllPrograms.size()>0)
		//{
		//for(ApplicationPrograms prog:getAllPrograms)
		//{
		ApplicationPrograms prog=applicationProgramsRepository.findOne(programId);
		HashMap program=new HashMap();
		program.put("programId",prog.getId());
		if(prog.getPrgmName()!=null)
			program.put("programName", prog.getPrgmName());
		List<ProgParametersSets> paramsetsList=progParametersSetsRepository.findByProgramIdAndStatusAndRequestFormIsTrue(prog.getId(),"ACTIVE");
		log.info("paramsetsList size:"+paramsetsList.size());
		List<HashMap> paramSetsMapList=new ArrayList<HashMap>();
		Connection conn = null;
		Statement stmt = null;
		DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
		conn = ds.getConnection();
		log.info("Connected database successfully...");
		stmt = conn.createStatement();
		ResultSet result2=null;
		for(ProgParametersSets paramset:paramsetsList)
		{
			HashMap param=new HashMap();
			if(paramset.getId()!=null)
				param.put("paramId", paramset.getId());
			if(paramset.getParameterName()!=null)
				param.put("paramName", paramset.getParameterName());
			param.put("bindValue", paramset.getBindValue());
			param.put("showValue", paramset.getEntityColumn());
			param.put("mandatory", paramset.getMandatory());
			param.put("dependency", paramset.getDependency());
			param.put("entityColumn",paramset.getEntityName());
			if(paramset.getDependency() != null )
			{
				/*ProgParametersSets dependencyPgmSet = new ProgParametersSets();
    					dependencyPgmSet = progParametersSetsRepository.findByPrograamIdAndParameterName(dependencyPgmSet.getProgramId(),dependencyPgmSet.getParameterName());
    					if(dependencyPgmSet != null )
    					{
    						param.put("dependencyName",dependencyPgmSet.getParameterName());//set dependency parameter name
    					}*/
			}
			else
			{
				List<HashMap> valueList=new ArrayList<HashMap>();


				if(paramset.getEntityName()!=null && paramset.getEntityColumn()!=null)
				{

					/*	String dbUrl=env.getProperty("spring.datasource.url");
					String[] parts=dbUrl.split("[\\s@&?$+-]+");
					String host = parts[0].split("/")[2].split(":")[0];
					log.info("host :"+host);
					String schemaName=parts[0].split("/")[3];
					log.info("schemaName :"+schemaName);
					String userName = env.getProperty("spring.datasource.username");
					String password = env.getProperty("spring.datasource.password");
					String jdbcDriver = env.getProperty("spring.datasource.jdbcdriver");*/

				
					//Class.forName("com.mysql.jdbc.Driver");

					log.info("paramset.getEntityName() :"+paramset.getEntityName());
					String query="";
					log.info("paramset.getParameterName() :"+paramset.getParameterName().replaceAll("\\s",""));
					if(paramset.getParameterName().replaceAll("\\s","").startsWith("RuleGroup") && prog.getPrgmName().equalsIgnoreCase("Reconciliation"))
					{

						query="select * from "+paramset.getEntityName()+" where tenant_id="+tenantId+" AND rule_purpose = "+"'Reconciliation'"+" and ((enabled_flag =1 AND (DATEDIFF(CURDATE(),start_date)) >= 0) and if(end_date is null, sysdate()+1, (DATEDIFF(end_date,CURDATE()) >= 0))) order by "+paramset.getEntityColumn()+" asc";
						log.info("query1 :"+query);
					}
					else if(paramset.getParameterName().replaceAll("\\s","").startsWith("RuleGroup") && prog.getPrgmName().equalsIgnoreCase("Accounting"))
					{
						query="select * from "+paramset.getEntityName()+" where tenant_id="+tenantId+" AND rule_purpose = "+"'Accounting'"+" and ((enabled_flag =1 AND (DATEDIFF(CURDATE(),start_date)) >= 0) and if(end_date is null, sysdate()+1, (DATEDIFF(end_date,CURDATE()) >= 0))) order by "+paramset.getEntityColumn()+" asc";
						log.info("query2 :"+query);
					}
					else if("DashboardAppModuleSummary".equalsIgnoreCase(prog.getPrgmName()))
					{
						query = "select * from "+paramset.getEntityName()+" where tenant_id="+tenantId;
					}
					else
					{
						query="select * from "+paramset.getEntityName()+" where tenant_id="+tenantId+" and ((enabled_flag =1 AND (DATEDIFF(CURDATE(),start_date)) >= 0) and if(end_date is null, sysdate()+1, (DATEDIFF(end_date,CURDATE()) >= 0))) order by "+paramset.getEntityColumn()+" asc";
						log.info("query3 :"+query);
					}
					result2=stmt.executeQuery(query);
					log.info("final query :"+query);


					
					if(result2!=null)
					{
						ResultSetMetaData rsmd2 = result2.getMetaData();
						int columnCount=rsmd2.getColumnCount();
						List<HashMap<String,String>> finalValueSet=new ArrayList<HashMap<String,String>>();
						while(result2.next())
						{
							HashMap rec=new HashMap();
							for (int i = 1; i <= columnCount; i++ ) {
								String name = rsmd2.getColumnName(i); 

								rec.put(name, result2.getString(i));
							}

							finalValueSet.add(rec);
						}
						param.put("valuesList", finalValueSet);
					}
					
					
				}

			}
			paramSetsMapList.add(param);

		}
		program.put("parametersSet", paramSetsMapList);
		map.add(program);
		if(result2!=null)
			result2.close();
		if(stmt!=null)
			stmt.close();
		if(conn!=null)
			conn.close();
		return map;
	}

	/**
	 * author :Ravali
	 * @param tenantId
	 * @return fetching jobs lists with program name
	 */

	@GetMapping("/JobsListWithProgramName")
	@Timed
	public List<HashMap> jobsListWithProgramName(HttpServletRequest request,@RequestParam String frequencyType)
	{
		HashMap map0=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map0.get("tenantId").toString());
		Long userId=Long.parseLong(map0.get("userId").toString());
		log.info("Rest request to get the JobsListWithProgramName for tenant: "+tenantId);
		List<String> distPrgName=applicationProgramsRepository.findByTenantIdAndDistinctPrgmName(tenantId);
		log.info("distPrgName :"+distPrgName.size());
		List<HashMap> finalMapList=new ArrayList<HashMap>();
		for(int i=0;i<distPrgName.size();i++)
		{
			if(distPrgName.get(i)!=null)
			{
				log.info("distPrgName.get(i) :"+distPrgName.get(i));
				List<BigInteger> progIds=applicationProgramsRepository.findIdsByTenantIdAndPrgmName(tenantId,distPrgName.get(i));
				log.info("progIds :"+progIds);
				HashMap map=new HashMap();
				map.put("name", distPrgName.get(i));
				List<JobDetails> jobNameList=jobDetailsRepository.findByCreatedByAndProgramIds(progIds,userId);
				if(jobNameList.size()>0)
				{
					//map.put("count", jobNameList.size());
					List<HashMap> jobMapList=new ArrayList<HashMap>();
					for(JobDetails jobs:jobNameList)
					{
						List<SchedulerDetails> sch=new ArrayList<SchedulerDetails>();
						if(frequencyType.equalsIgnoreCase("scheduled"))
								sch=schedulerDetailsRepository.fetchByJobIdAndScheduled(jobs.getId());
						else if(frequencyType.equalsIgnoreCase("ONDEMAND"))
							sch=schedulerDetailsRepository.fetchByJobIdAndFequencyOnDemand(jobs.getId());
						
						
						
						
						if(sch.size()>0)
						{
						HashMap jbMap=new HashMap();
						map.put("programId", jobs.getProgrammId());
						jbMap.put("id", jobs.getIdForDisplay());
						if(jobs.getJobName()!=null)
							jbMap.put("jobName", jobs.getJobName());
						jobMapList.add(jbMap);
						}
					}
					
					map.put("List", jobMapList);
					map.put("count", jobMapList.size());
					if(jobMapList.size()>0)
					finalMapList.add(map);

				}

			}
		}
		return finalMapList;


	}
	/**
	 * Author : shiva
	 * @param multipart
	 * @param tenantId
	 * @param userId
	 */
	@PostMapping("/uploadApplicationPrograms")
	@Timed
	public void uploadApplicationPrograms(@RequestParam MultipartFile multipart, @RequestParam Long tenantId, @RequestParam Long userId)
	{
		log.info("Uploading application programs for the file : "+ multipart.getOriginalFilename()+", tenant id: "+tenantId);
		try {

			CSVReader csvReader = new CSVReader(new InputStreamReader(multipart.getInputStream()), ',' , '"');
			//InputStream is = multipart.getInputStream();
			//BufferedReader br = new BufferedReader(new InputStreamReader(is));
			//Getting indexes of every field from csv file
			int progNameIndx = 0;
			int prgmDesIndx = 0;
			int generatedPathIndx = 0;
			int targetPathIndx = 0;
			int progPathIndx = 0;
			int prgmOrClasNameIndx = 0;
			int prgmTypeIndx = 0;
			int jhiEnableIndx = 0;
			int startDtIndx = 0;
			int endDtIndx = 0;

			List<String[]> allRows = csvReader.readAll();
			log.info("All Rows Size: "+ allRows.size());

			if(allRows.size()>1)
			{
				String[] header = allRows.get(0);

				for(int i=0; i<header.length; i++)
				{
					if("prgm_name".equalsIgnoreCase(header[i].toString()))
						progNameIndx = i;
					else if("prgm_description".equalsIgnoreCase(header[i].toString()))
						prgmDesIndx = i;
					else if("generated_path".equalsIgnoreCase(header[i].toString()))
						generatedPathIndx = i;
					else if("target_path".equalsIgnoreCase(header[i].toString()))
						targetPathIndx = i;
					else if("prgm_path".equalsIgnoreCase(header[i].toString()))
						progPathIndx = i;
					else if("prgm_or_class_name".equalsIgnoreCase(header[i].toString()))
						prgmOrClasNameIndx = i;
					else if("prgm_type".equalsIgnoreCase(header[i].toString()))
						prgmTypeIndx = i;
					else if("jhi_enable".equalsIgnoreCase(header[i].toString()))
						jhiEnableIndx = i;
					else if("start_date".equalsIgnoreCase(header[i].toString()))
						startDtIndx = i;
					else if("end_date".equalsIgnoreCase(header[i].toString()))
						endDtIndx = i;
				}

				log.info("Indexes: prgm_name["+progNameIndx+"], prgm_description["+prgmDesIndx+"], generated_path["+generatedPathIndx+"], target_path["+targetPathIndx+"], prgm_path["+
						progPathIndx+"], prgm_or_class_name["+prgmOrClasNameIndx+"], prgm_type["+prgmTypeIndx+"], jhi_enable["+jhiEnableIndx+"], start_date["
						+startDtIndx+"], endDtIndx["+endDtIndx+"]");
				if(allRows.size()>1)
				{
					// Reading data
					List<ApplicationPrograms> appPrgms = new ArrayList<ApplicationPrograms>();
					for(int j=1; j<allRows.size(); j++)
					{
						String[] row = allRows.get(j);
						ApplicationPrograms app = new ApplicationPrograms();
						app.setEnable(true);
						app.setTenantId(tenantId);
						if("NULL".equalsIgnoreCase(row[progNameIndx].toString()) || (row[progNameIndx].toString().isEmpty()))
							app.setPrgmName(null);
						else
							app.setPrgmName(row[progNameIndx].toString());
						if("NULL".equalsIgnoreCase(row[prgmDesIndx].toString()) || (row[prgmDesIndx].toString().isEmpty()))
							app.setPrgmDescription(null);
						else
							app.setPrgmDescription(row[prgmDesIndx].toString());
						if("NULL".equalsIgnoreCase(row[generatedPathIndx].toString()) || (row[generatedPathIndx].toString().isEmpty()))
							app.setGeneratedPath(null);
						else
							app.setGeneratedPath(row[generatedPathIndx].toString());
						if("NULL".equalsIgnoreCase(row[targetPathIndx].toString()) || (row[targetPathIndx].toString().isEmpty()))
							app.setTargetPath(null);
						else
							app.setTargetPath(row[targetPathIndx].toString());
						if("NULL".equalsIgnoreCase(row[progPathIndx].toString()) || (row[progPathIndx].toString().isEmpty()))
							app.setPrgmPath(null);
						else
							app.setPrgmPath(row[progPathIndx].toString());
						if("NULL".equalsIgnoreCase(row[prgmOrClasNameIndx].toString()) || (row[prgmOrClasNameIndx].toString().isEmpty()))
							app.setPrgmOrClassName(null);
						else
							app.setPrgmOrClassName(row[prgmOrClasNameIndx].toString());
						if("NULL".equalsIgnoreCase(row[prgmTypeIndx].toString()) || (row[prgmTypeIndx].toString().isEmpty()))
							app.setPrgmType(null);
						else
							app.setPrgmType(row[prgmTypeIndx].toString());
						//app.isEnable(Boolean.valueOf(lineSplit[jhiEnableIndx]));
						if("NULL".equalsIgnoreCase(row[startDtIndx].toString()) || (row[startDtIndx].toString().isEmpty()))
							app.setStartDate(null);
						else
							app.setStartDate(ZonedDateTime.parse(row[startDtIndx].toString()));
						if("NULL".equalsIgnoreCase(row[endDtIndx].toString()) || (row[endDtIndx].toString().isEmpty()))
							app.setEndDate(null);
						else
							app.setEndDate(ZonedDateTime.parse(row[endDtIndx].toString()));
						app.setCreatedBy(userId);
						app.setCreationDate(ZonedDateTime.now());
						app.setLastUpdatedBy(userId);
						app.setLastUpdationDate(ZonedDateTime.now());
						appPrgms.add(app);
					}
					applicationProgramsRepository.save(appPrgms);
				}
			}
			csvReader.close();
		} catch (IOException e) {
			log.info("Exception e: "+e);
		}

	}

	/**
	 * Author Kiran, Swetha
	 * Swetha: Added code to create default program paths 
	 */
	@PostMapping("/uploadingApplicationPrograms")
	@Timed
	public void ConfiguringApplicationPrograms(@RequestParam Long tenantId, @RequestParam Long userId)
	{
		log.info("In Configuring Application Programs for tenant:- "+tenantId+" and userId:- "+userId);
		InputStreamReader inputStream = new  InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("jsonFile/ApplicationProgram.csv"));
		if(inputStream!=null)
		{
			CSVReader csvReader = new CSVReader(inputStream, ',' , '"');
			int progNameIndx = 0;
			int prgmDesIndx = 0;
			int generatedPathIndx = 0;
			int targetPathIndx = 0;
			int progPathIndx = 0;
			int prgmOrClasNameIndx = 0;
			int prgmTypeIndx = 0;
			int jhiEnableIndx = 0;
			int startDtIndx = 0;
			int endDtIndx = 0;
			int queueNameIndx = 0;

			List<String[]> allRows;
			try {
				allRows = csvReader.readAll();
				csvReader.close();
				//log.info("All Rows Size in ConfiguringApplicationPrograms: "+ allRows.size());

				if(allRows.size()>1)
				{
					String[] header = allRows.get(0);

					for(int i=0; i<header.length; i++)
					{
						if("prgm_name".equalsIgnoreCase(header[i].toString()))
							progNameIndx = i;
						else if("prgm_description".equalsIgnoreCase(header[i].toString()))
							prgmDesIndx = i;
						else if("generated_path".equalsIgnoreCase(header[i].toString()))
							generatedPathIndx = i;
						else if("target_path".equalsIgnoreCase(header[i].toString()))
							targetPathIndx = i;
						else if("prgm_path".equalsIgnoreCase(header[i].toString()))
							progPathIndx = i;
						else if("prgm_or_class_name".equalsIgnoreCase(header[i].toString()))
							prgmOrClasNameIndx = i;
						else if("prgm_type".equalsIgnoreCase(header[i].toString()))
							prgmTypeIndx = i;
						else if("jhi_enable".equalsIgnoreCase(header[i].toString()))
							jhiEnableIndx = i;
						else if("start_date".equalsIgnoreCase(header[i].toString()))
							startDtIndx = i;
						else if("end_date".equalsIgnoreCase(header[i].toString()))
							endDtIndx = i;
						else if("queue_name".equalsIgnoreCase(header[i].toString()))
							queueNameIndx = i;
					}

					log.info("Indexes: prgm_name["+progNameIndx+"], prgm_description["+prgmDesIndx+"], generated_path["+generatedPathIndx+"], target_path["+targetPathIndx+"], prgm_path["+
							progPathIndx+"], prgm_or_class_name["+prgmOrClasNameIndx+"], prgm_type["+prgmTypeIndx+"], jhi_enable["+jhiEnableIndx+"], start_date["
							+startDtIndx+"], endDtIndx["+endDtIndx+"], queueName["+queueNameIndx+"]");
				}

				if(allRows.size()>1)
				{
					// Reading data
					List<ApplicationPrograms> appPrgms = new ArrayList<ApplicationPrograms>();
					for(int j=1; j<allRows.size(); j++)
					{
						String[] row = allRows.get(j);
						ApplicationPrograms app = new ApplicationPrograms();
						app.setEnable(true);
						String prgmName="";
						String generatedPath="";
						String targetPath="";
						String prgmType="";
						String prgmPath="";
						String queueName="";
						app.setTenantId(tenantId);
						if("NULL".equalsIgnoreCase(row[progNameIndx].toString()) || (row[progNameIndx].toString().isEmpty()))
							app.setPrgmName(null);
						else{
							prgmName=row[progNameIndx].toString();
							app.setPrgmName(prgmName);
						}
						if("NULL".equalsIgnoreCase(row[prgmDesIndx].toString()) || (row[prgmDesIndx].toString().isEmpty()))
							app.setPrgmDescription(null);
						else
							app.setPrgmDescription(row[prgmDesIndx].toString());
						if("NULL".equalsIgnoreCase(row[generatedPathIndx].toString()) || (row[generatedPathIndx].toString().isEmpty()))
							app.setGeneratedPath(null);
						else{
							generatedPath=row[generatedPathIndx].toString();
							app.setGeneratedPath(generatedPath);
						}
						if("NULL".equalsIgnoreCase(row[targetPathIndx].toString()) || (row[targetPathIndx].toString().isEmpty()))
							app.setTargetPath(null);
						else{
							targetPath=row[targetPathIndx].toString();
							app.setTargetPath(targetPath);
						}
						if("NULL".equalsIgnoreCase(row[progPathIndx].toString()) || (row[progPathIndx].toString().isEmpty()))
							app.setPrgmPath(null);
						else{
							prgmPath=row[progPathIndx].toString();
							app.setPrgmPath(prgmPath);
						}
						if("NULL".equalsIgnoreCase(row[prgmOrClasNameIndx].toString()) || (row[prgmOrClasNameIndx].toString().isEmpty()))
							app.setPrgmOrClassName(null);
						else
							app.setPrgmOrClassName(row[prgmOrClasNameIndx].toString());
						if("NULL".equalsIgnoreCase(row[prgmTypeIndx].toString()) || (row[prgmTypeIndx].toString().isEmpty()))
							app.setPrgmType(null);
						else{
							prgmType=row[prgmTypeIndx].toString();
							app.setPrgmType(prgmType);
						}
						//app.isEnable(Boolean.valueOf(lineSplit[jhiEnableIndx]));
						if("NULL".equalsIgnoreCase(row[startDtIndx].toString()) || (row[startDtIndx].toString().isEmpty()))
							app.setStartDate(null);
						else
							app.setStartDate(ZonedDateTime.parse(row[startDtIndx].toString()));
						if("NULL".equalsIgnoreCase(row[endDtIndx].toString()) || (row[endDtIndx].toString().isEmpty()))
							app.setEndDate(null);
						else
							app.setEndDate(ZonedDateTime.parse(row[endDtIndx].toString()));
						
						if("NULL".equalsIgnoreCase(row[queueNameIndx].toString()) || (row[queueNameIndx].toString().isEmpty()))
							app.setQueueName(null);
						else{
							queueName=row[queueNameIndx].toString();
							app.setQueueName(queueName);
						}
						
						
						app.setCreatedBy(userId);
						app.setCreationDate(ZonedDateTime.now());
						app.setLastUpdatedBy(userId);
						app.setLastUpdationDate(ZonedDateTime.now());
						ApplicationPrograms ap=	applicationProgramsRepository.save(app);

						/* Create Directories for Generated, Target, Program Paths */
						/*String hadoopBaseDirectory=env.getProperty("baseDirectories.hadoopBaseDir");
						String linuxBaseDirectory=env.getProperty("baseDirectories.linuxBaseDir");
						//String nodeName=env.getProperty("spring.hadoop.config.fs.defaultFS");
						String hadoopRootUser=env.getProperty("oozie.hadoopRootUser");
						
						if(prgmName!=null && !(prgmName.isEmpty()) && !(prgmName.equalsIgnoreCase(""))){
								if(generatedPath!=null && !(generatedPath.isEmpty()) && !(generatedPath.equalsIgnoreCase("")))
									applicationProgramsService.createPrgmDirectory(linuxBaseDirectory,generatedPath);
						
							if(hadoopRootUser!=null){
								if(targetPath!=null && !(targetPath.isEmpty()) && !(targetPath.equalsIgnoreCase(""))){
									applicationProgramsService.createDirectoryInHdfs(hadoopBaseDirectory, "targetPath",targetPath, hadoopRootUser,null );
							}
								if(prgmPath!=null && !(prgmPath.isEmpty()) && !(prgmPath.equalsIgnoreCase(""))){
									applicationProgramsService.createDirectoryInHdfs(hadoopBaseDirectory,  "progarmPath",prgmPath, hadoopRootUser,prgmType );
								}
							}
						
							if(prgmName.equalsIgnoreCase("DataExtraction")){
								applicationProgramsService.createDirectoryWithSftp(tenantId);
							}
							
							if(prgmName.equalsIgnoreCase("Reporting")){
								applicationProgramsService.createDirectoryInHdfs(hadoopBaseDirectory, "reportsOutputPath",prgmPath, hadoopRootUser,null );
								applicationProgramsService.createDirectoryInHdfs(hadoopBaseDirectory, "reportsLocalPath",targetPath, hadoopRootUser,null );
							}
							
							if(prgmName.equalsIgnoreCase("DataTransformation")){
								applicationProgramsService.createDirectoryInHdfs(hadoopBaseDirectory, "transformationLocalPath",prgmPath, hadoopRootUser,null );
							}
						}*/
						applicationProgramsService.configuringParameterSets(ap.getId(),row[progNameIndx].toString(), userId);


						//	appPrgms.add(app);
						System.out.println("J Value: "+j+"--> row[progNameIndx].toString():- "+row[progNameIndx].toString());
					}
					//applicationProgramsRepository.save(appPrgms);
				}

			} catch (IOException e) {
				log.error("Printing IOException in Application Programs: ",e);
				e.printStackTrace();
			}
		}

	}

	@GetMapping("/testPgmPath2")
	@Timed
	public void testPgmPath2(@RequestParam String pathType,String path,String hadoopRootUser,String prgmType) throws IOException{
		
		String baseDirectory=env.getProperty("baseDirectories.hadoopBaseDir");
		applicationProgramsService.createDirectoryInHdfs(baseDirectory, pathType,path, hadoopRootUser, prgmType,null);
	}
	
	@GetMapping("/testPgmPath")
	@Timed
	public void testPgmPath(@RequestParam String generatedPath) throws IOException{
		
		String baseDirectory=env.getProperty("baseDirectories.linuxBaseDir");
		applicationProgramsService.createPrgmDirectory(baseDirectory,generatedPath );
	}
	
	@GetMapping("/testPgmPath3")
	@Timed
	public void testPgmPath3(Long tenantId) throws IOException{
		
		applicationProgramsService.createDirectoryWithSftp(tenantId);
	}

}
