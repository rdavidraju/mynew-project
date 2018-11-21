package com.nspl.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nspl.app.domain.BucketList;
import com.nspl.app.domain.DataViews;
import com.nspl.app.domain.DataViewsColumns;
import com.nspl.app.domain.JobActions;
import com.nspl.app.domain.Notifications;
import com.nspl.app.domain.Periods;
import com.nspl.app.domain.Project;
import com.nspl.app.domain.ReportDefination;
import com.nspl.app.domain.ReportRequests;
import com.nspl.app.domain.ReportType;
import com.nspl.app.domain.Reports;
import com.nspl.app.domain.RuleConditions;
import com.nspl.app.domain.SchedulerDetails;
import com.nspl.app.repository.BucketDetailsRepository;
import com.nspl.app.repository.BucketListRepository;
import com.nspl.app.repository.DataViewsColumnsRepository;
import com.nspl.app.repository.DataViewsRepository;
import com.nspl.app.repository.FileTemplateLinesRepository;
import com.nspl.app.repository.JobActionsRepository;
import com.nspl.app.repository.LookUpCodeRepository;
import com.nspl.app.repository.NotificationsRepository;
import com.nspl.app.repository.PeriodsRepository;
import com.nspl.app.repository.ProjectRepository;
import com.nspl.app.repository.ReportDefinationRepository;
import com.nspl.app.repository.ReportParametersRepository;
import com.nspl.app.repository.ReportRequestsRepository;
import com.nspl.app.repository.ReportTypeRepository;
import com.nspl.app.repository.ReportingDashboardRepository;
import com.nspl.app.repository.ReportingDashboardUsecasesRepository;
import com.nspl.app.repository.ReportsRepository;
import com.nspl.app.repository.RuleConditionsRepository;
import com.nspl.app.repository.SchedulerDetailsRepository;
import com.nspl.app.repository.TenantConfigRepository;
import com.nspl.app.repository.search.ProjectSearchRepository;
import com.nspl.app.repository.SegmentsRepository;
import com.nspl.app.service.DataViewsService;
import com.nspl.app.service.FileExportService;
import com.nspl.app.service.OozieService;
import com.nspl.app.service.PropertiesUtilService;
import com.nspl.app.service.ReportsService;
import com.nspl.app.service.UserJdbcService;
import com.nspl.app.web.rest.util.HeaderUtil;

import io.github.jhipster.web.util.ResponseUtil;

import org.apache.oozie.client.OozieClientException;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.RelationalGroupedDataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
//import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import static org.elasticsearch.index.query.QueryBuilders.*;

import org.json.JSONArray;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*import com.nspl.livy.InteractiveJobParameters;
import com.nspl.livy.LivyException;
import com.nspl.livy.LivyInteractiveClient;
import com.nspl.livy.SessionEventListener;
import com.nspl.livy.SessionKind;
import com.nspl.livy.StatementResult;
import com.nspl.livy.StatementResultListener;*/











/*import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.net.MalformedURLException;*/

/**
 * REST controller for managing Project.
 */
@RestController
@RequestMapping("/api")
public class ProjectResource {

    private final Logger log = LoggerFactory.getLogger(ProjectResource.class);

    private static final String ENTITY_NAME = "project";
        
    private final ProjectRepository projectRepository;

    private final ProjectSearchRepository projectSearchRepository;
    
   /* @Value("${samplevalue}")
    private String value;*/
    
    @Inject 
    ReportsRepository reportsRepository;
    
    @Inject
    DataViewsRepository dataViewsRepository;
    
    @Inject
    ReportParametersRepository reportParametersRepository;
    
    @Inject
    LookUpCodeRepository lookUpCodeRepository;
    
    @Inject
    DataViewsService dataViewsService;
    
    @Inject
    ReportDefinationRepository reportDefinationRepository;
    
    @Inject
    private Environment env;
    
    @Inject
    ReportsService reportsService;
    
    @Inject
    BucketListRepository bucketListRepository;
    
    @Inject
    BucketDetailsRepository bucketDetailsRepository;
    
    @Inject
    DataViewsColumnsRepository dataViewsColumnsRepository;
    
    @Inject
    FileTemplateLinesRepository fileTemplateLinesRepository;
    
    @Inject
    PropertiesUtilService propertiesUtilService;
    
    @Inject
    OozieService oozieService;
    
    @Inject
    JobActionsRepository jobActionsRepository;
    
    @Inject
    ReportRequestsRepository reportRequestsRepository;
    
    @Inject
    NotificationsRepository notificationsRepository;
    
    @Inject
    SchedulerDetailsRepository schedulerDetailsRepository;
    
    @Inject
    SegmentsRepository segmentsRepository;
    
    @Inject
    UserJdbcService userJdbcService;
    
    @Inject
    TenantConfigRepository tenantConfigRepository;
    
    @Inject
    RuleConditionsRepository ruleConditionsRepository;
    
    @Inject
    ReportingDashboardRepository reportingDashboardRepository;
    
    @Inject
    ReportingDashboardUsecasesRepository reportingDashboardUsecasesRepository;
    
    @Inject
    FileExportService fileExportService;
    
    @Inject
    ReportTypeRepository reportTypeRepository;
    
    @Inject
    PeriodsRepository periodsRepository;
    
    
   
    @PersistenceContext(unitName="default")
	private EntityManager em;
    
   /* private LivyInteractiveClient client = null;
	private int session_status = com.nspl.livy.Session.STARTING;
	private String resultResponse = null;*/
    
    public ProjectResource(ProjectRepository projectRepository, ProjectSearchRepository projectSearchRepository) {
        this.projectRepository = projectRepository;
        this.projectSearchRepository = projectSearchRepository;
    }

    /**
     * POST  /projects : Create a new project.
     *
     * @param project the project to create
     * @return the ResponseEntity with status 201 (Created) and with body the new project, or with status 400 (Bad Request) if the project has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/projects")
    @Timed
    public ResponseEntity<Project> createProject(@RequestBody Project project) throws URISyntaxException {
        log.debug("REST request to save Project : {}", project);
        if (project.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new project cannot already have an ID")).body(null);
        }
        Project result = projectRepository.save(project);
        projectSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/projects/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /projects : Updates an existing project.
     *
     * @param project the project to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated project,
     * or with status 400 (Bad Request) if the project is not valid,
     * or with status 500 (Internal Server Error) if the project couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/projects")
    @Timed
    public ResponseEntity<Project> updateProject(@RequestBody Project project) throws URISyntaxException {
        log.debug("REST request to update Project : {}", project);
        if (project.getId() == null) {
            return createProject(project);
        }
        Project result = projectRepository.save(project);
        projectSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, project.getId().toString()))
            .body(result);
    }

    /**
     * GET  /projects : get all the projects.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of projects in body
     */
    @GetMapping("/projects")
    @Timed
    public List<Project> getAllProjects() {
        log.debug("REST request to get all Projects");
        List<Project> projects = projectRepository.findAll();
        return projects;
    }

    /**
     * GET  /projects/:id : get the "id" project.
     *
     * @param id the id of the project to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the project, or with status 404 (Not Found)
     */
    @GetMapping("/projects/{id}")
    @Timed
    public ResponseEntity<Project> getProject(@PathVariable Long id) {
        log.debug("REST request to get Project : {}", id);
        Project project = projectRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(project));
    }

    /**
     * DELETE  /projects/:id : delete the "id" project.
     *
     * @param id the id of the project to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/projects/{id}")
    @Timed
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        log.debug("REST request to delete Project : {}", id);
        projectRepository.delete(id);
        projectSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/projects?query=:query : search for the project corresponding
     * to the query.
     *
     * @param query the query of the project search 
     * @return the result of the search
     */
    @GetMapping("/_search/projects")
    @Timed
    public List<Project> searchProjects(@RequestParam String query) {
        log.debug("REST request to search Projects for query {}", query);
        return StreamSupport
            .stream(projectSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

    /**
     * POC for Accounting Work Queue Pivot
     * @param tenanatId
     * @param reportId
     * @param filtersMap
     * @return
     * @throws AnalysisException
     * @throws IOException
     * @throws ParseException
     */
    @PostMapping("/AWQPivotingData")
    @Timed
    public List<LinkedHashMap> AWQ_poc(@RequestParam Long tenanatId, @RequestBody(required=false) HashMap filtersMap, @RequestParam Long dataViewId, @RequestParam Long ruleGrpId) throws AnalysisException, IOException, ParseException{
    	
    	SparkSession spark = reportsService.getSparkSession();
    	JavaSparkContext sContext = new JavaSparkContext(spark.sparkContext());
    	SQLContext sqlCont = new SQLContext(sContext);
    	
    	
    	Dataset<Row> reports_data=reportsService.pocAWQ(tenanatId, filtersMap,spark, dataViewId,ruleGrpId);
    	
    	log.info("Previewing reports_data b4 Pivoting");
    	reports_data.show();
    	
    	int sz=0;
        String pivotCol="";
        String refPivotCol="";
        List grpColList=new ArrayList<>();
        
        RelationalGroupedDataset grpData = null;
        Dataset<Row> data2 = null;
        
        String dbUrl = env.getProperty("spring.datasource.url");
		String[] parts = dbUrl.split("[\\s@&?$+-]+");
		String host = parts[0].split("/")[2].split(":")[0];
		String schemaName = parts[0].split("/")[3];
		String userName = env.getProperty("spring.datasource.username");
		String password = env.getProperty("spring.datasource.password");
		String jdbcDriver = env.getProperty("spring.datasource.jdbcdriver");
        
        /*String dbUrl = "jdbc:mysql://192.168.0.44:3306/agree_application_2712";
		//String[] parts = dbUrl.split("[\\s@&?$+-]+");
		String host = "192.168.0.44";
		String schemaName = "agree_application_2712";
		String userName = "recon_dev";
		String password = "Welcome321$";
		//String jdbcDriver = "";
*/		
        Dataset<Row> datViewData = sqlCont.read().format("jdbc")
				.option("url", dbUrl).option("user", userName)
				.option("password", password).option("dbtable", "t_data_views")
				.load().where("id=" + dataViewId).select("data_view_name");

		Row dv = datViewData.collectAsList().get(0);

		String dvatViewName = dv.getString(0);
		dvatViewName = dvatViewName.toLowerCase();
		
       /*  Pivot Rows */
        if(filtersMap.containsKey("groupingCols")){
        	grpColList=(List) filtersMap.get("groupingCols");
        log.info("grpColList: "+grpColList);
        sz=grpColList.size();
        log.info("sz: "+grpColList);
        org.apache.spark.sql.Column[] col = new org.apache.spark.sql.Column[sz];
        int h=0;
        String refName="";
        for(h=0;h<sz;h++){
        	HashMap map=(HashMap) grpColList.get(h);
        	log.info("map: "+map);
        	String refTypeId=map.get("refType").toString();
        	if(refTypeId.equalsIgnoreCase("FIN_FUNCTION")){
        		refName=map.get("itemName").toString();
        		
        	}
        	else {
        		refName=map.get("itemName").toString();
        	}
        	
            col[h]= new Column(refName);
            log.info("col["+h+"]: "+col[h]);
        }
        
        log.info("col: "+col.toString());
        
        grpData=reports_data.groupBy(col);

        log.info("grouped data count: "+grpData.count());
        
        
       /*  Pivot Columns */
        if(filtersMap.containsKey("columnCols")){
        	
        	List pivotCols=(List) filtersMap.get("columnCols");
        	log.info("pivotCols: "+pivotCols);
        	HashMap pivotColObj=(HashMap) pivotCols.get(0);
        	pivotCol=pivotColObj.get("itemName").toString();
        	//refPivotCol=fieldRefMap.get(pivotCol).toString();
        	refPivotCol=pivotCol;
        	grpData=grpData.pivot(refPivotCol);
        }
        
        log.info("pivoted data count: "+grpData.count());
        }
        
        String amtCol="";
       /*  Aggregation */
        if(filtersMap.containsKey("valueCols")){
        	List amtColList=(List) filtersMap.get("valueCols");
        	HashMap amtColMap=(HashMap) amtColList.get(0);
        	log.info("amtColMap: "+amtColMap);
        	amtCol=amtColMap.get("itemName").toString();
        	String refAmtCol="";
        	String amtColrefTypeId=amtColMap.get("refType").toString();
        	refAmtCol=amtColMap.get("itemName").toString();
            data2=grpData.agg(
                functions.sum(refAmtCol).as("sum"),
                functions.count("*").as("count")
            );    
            log.info("data2 after addng sum cnt: "+data2.count());
            data2.show();
        }
    
        List<Row> data=data2.collectAsList();
        String[] columnsList=data2.columns();
        log.info("data.size(): "+data.size()+" columnsList.length: "+columnsList.length);
        
        /* Final Result set */
        List<LinkedHashMap> maps=new ArrayList<LinkedHashMap>();
        log.info(">> "+ data.get(0).get(0));
        
        for(int j=0;j<data.size();j++){
        	LinkedHashMap mapCount = new LinkedHashMap();
        	LinkedHashMap mapAmount = new LinkedHashMap();
        	String amount = "";
        	String count = "";
        	for(int s=0; s<columnsList.length; s++)
        	{
        		if(!("sum".equalsIgnoreCase(columnsList[s])) && !("count".equalsIgnoreCase(columnsList[s])))
        		{
        			mapCount.put(columnsList[s], data.get(j).get(s));
        			mapAmount.put(columnsList[s], data.get(j).get(s));
        		}
        		else if("sum".equalsIgnoreCase(columnsList[s]))
        		{
        			amount = amount + data.get(j).get(s).toString();
        		}
        		else if("count".equalsIgnoreCase(columnsList[s]))
        		{
        			count = count + data.get(j).get(s).toString();
        		}
        	}
        	mapCount.put("type", "count");
        	mapCount.put("value", Integer.parseInt(count));
        	
        	mapAmount.put("type", "amount");
        	mapAmount.put("value", Double.parseDouble(amount));
        	
        	maps.add(mapCount);
        	maps.add(mapAmount);
/*            for(int s=0;s<(columnsList.length);s++){
            	
            	if(s+sz==columnsList.length || j+sz==columnsList.length){
            		break;
            	}
            	LinkedHashMap map = new LinkedHashMap();
            	LinkedHashMap map2 = new LinkedHashMap();
             	for(int z=0;z<sz;z++){
             		HashMap grpColMap=(HashMap) grpColList.get(z);
             		//log.info("grpColMap: "+grpColMap);
             		
                	map.put(grpColMap.get("itemName"),data.get(j).get(z));
                	
                }
             	
             	if(pivotCol!=null && !(pivotCol.isEmpty())){
             	map.put(pivotCol,columnsList[s+sz]);
             	}
             	
             	if(data.get(j).get(s+sz)!=null)
             	map.put(amtCol,data.get(j).get(s+sz));
                maps.add(map);
            }*/
        }
        sContext.close();
		return maps;
    }    
    
    /**
     * @param tenantId
     * @param reportId
     * @param filtersMap
     * @return
     * @throws org.json.simple.parser.ParseException
     * @throws IOException
     * @throws OozieClientException
     * @throws URISyntaxException
     */
    
    @Async
    @PostMapping("/PivotViewReportAsync")
    @Timed
    public LinkedHashMap PivotViewReportAsync(HttpServletRequest request, @RequestParam Long reportId, @RequestBody(required = false) HashMap filtersMap) 
    		throws org.json.simple.parser.ParseException, IOException, OozieClientException, URISyntaxException{
    	
    	log.info("PivotViewReportAsync start time:"+ZonedDateTime.now());
    	LinkedHashMap map=new LinkedHashMap();
    	List<LinkedHashMap> reportReturnList=PivotViewReportNew(request, reportId, filtersMap);
    	map.put("status", "your request has been submitted");
    	log.info("PivotViewReportAsync end time:"+ZonedDateTime.now());
		return map;
    	
    }
    
    
    /**
     * Author: swetha
     * @param tenantId
     * @param reportId
     * @param filtersMap
     * @param pageNumber
     * @param pageSize
     * @param response
     * @return
     * @throws org.json.simple.parser.ParseException
     * @throws IOException
     * @throws OozieClientException
     * @throws URISyntaxException
     */
    
    @Async
    @PostMapping("/TabularViewReportGenerationAsync")
    @Timed
    public LinkedHashMap reportingPOCAsync(HttpServletRequest request, @RequestParam String reportId, @RequestParam String reqName, @RequestBody(required = false) HashMap filtersMap, 
    		@RequestParam(required=false) Integer pageNumber, @RequestParam(required=false) Integer pageSize,@RequestParam String sysReqName,HttpServletResponse response) throws org.json.simple.parser.ParseException, IOException, OozieClientException, URISyntaxException
    	{
    	log.info("TabularViewReportGenerationAsync start time:"+ZonedDateTime.now());
    	LinkedHashMap map=new LinkedHashMap();
    	//JSONObject jsonObj=reportsService.reportingPOC(tenantId, userId, reportId, filtersMap, pageNumber, pageSize, response);
    	JSONObject jsonObj=reportingPOC(request, reportId, reqName, filtersMap, pageNumber, pageSize, sysReqName,response);
    	map.put("status", "your request has been submitted");
    	log.info("TabularViewReportGenerationAsync end time:"+ZonedDateTime.now());
		return map;
    	}
    
    

    /**
     * Author: Swetha
     * @param request
     * @param reportId
     * @param filtersMap
     * @param pageNumber
     * @param pageSize
     * @param response
     * @return
     * @throws org.json.simple.parser.ParseException
     * @throws IOException
     * @throws OozieClientException
     * @throws URISyntaxException
     */
    @PostMapping("/TabularViewReportGeneration")
    @Timed
    public JSONObject reportingPOC(HttpServletRequest request, @RequestParam String reportId, @RequestParam String reqName, @RequestBody(required = false) HashMap filtersMap, 
    		@RequestParam(required=false) Integer pageNumber, @RequestParam(required=false) Integer pageSize,@RequestParam String sysReqName,HttpServletResponse response) throws org.json.simple.parser.ParseException, 
    		IOException, OozieClientException, URISyntaxException{
    	log.info("Rest Request to TabularViewReportGeneration for reportId: "+reportId+" reqName: "+reqName+" sysReqName: "+sysReqName);
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
      	Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	Long userId=Long.parseLong(map.get("userId").toString());
    		log.info("TabularViewReportGeneration start time:"+ZonedDateTime.now());
    		reqName=reqName.trim();
    		Reports repData=reportsRepository.findByTenantIdAndIdForDisplay(tenantId, reportId);
    		sysReqName=sysReqName.replaceAll(" ", "");
    		LinkedHashMap jobDataMap=reportsService.reportingPOC(tenantId, userId, repData.getId(), reqName, filtersMap, pageNumber, pageSize,sysReqName, response,request);
        	log.info("TabularViewReportGeneration end time:"+ZonedDateTime.now());
        	//ReportRequests repReq=(ReportRequests) jobDataMap.get("repReq");
        	
        	ReportRequests repReq=reportRequestsRepository.findOneByTenantIdAndSysReqName(tenantId, sysReqName);
        	log.info("repReq for sysReqName: "+sysReqName+" is: "+repReq);
        	
         	JSONObject jsonObj=new JSONObject();
        	JSONObject output=new JSONObject();
        	 JSONObject pivotOutput=new JSONObject();
        	String outputPath="";
        	String pivotOutputPath="";
        	String lastOne="";
        	String val="";
        	String reportName="";
        	LinkedHashMap dataMap=new LinkedHashMap();
        	JSONObject newFileData=new JSONObject();
        	JSONObject newPivotFileData=new JSONObject();
        	String outputType="";
        	
        	List<ReportDefination> reportCondList=new ArrayList<ReportDefination>();
        	reportCondList=reportDefinationRepository.fetchReportConditions(repData.getId());
        	
        	List<HashMap> additionalParamsList=new ArrayList<HashMap>();
			
			if(repData.getSourceViewId()!=null){
				DataViews dvData=dataViewsRepository.findOne(repData.getSourceViewId());
				HashMap map1=new HashMap();
				map1.put("Parameter", "Data View");
				map1.put("Value", dvData.getDataViewDispName());
				additionalParamsList.add(map1);
			}
			if(repData.getReportTypeId()!=null){
				ReportType repType=reportTypeRepository.findOne(repData.getReportTypeId());
				HashMap map2=new HashMap();
				if(repType.getType()!=null && repType.getType().equalsIgnoreCase("AGING_REPORT")){
					Long buckId=Long.parseLong(repData.getReportVal01().toString());
					BucketList buckList=bucketListRepository.findOne(buckId);
					map2.put("Parameter", "Bucket Name");
					map2.put("Value", buckList.getBucketName());
					additionalParamsList.add(map2);
				}
				else if(repType.getType()!=null && repType.getType().equalsIgnoreCase("ROLL_BACK_REPORT")){
					String rollBackVal=repData.getReportVal01();
					HashMap map3=new HashMap();
					map3.put("Parameter", "Roll Back Type");
					map3.put("Value", rollBackVal+" Wise("+repData.getReportVal02()+" "+rollBackVal+")");
					additionalParamsList.add(map3);
				}
			
			if(repType.getType()!=null && (repType.getType().equalsIgnoreCase("AGING_REPORT") || repType.getType().equalsIgnoreCase("ROLL_BACK_REPORT"))){
    			ReportDefination aggregatorData=reportDefinationRepository.findByReportIdAndDataType(repData.getId(), "AGGREGATOR");
    			HashMap map4=new HashMap();
    			map4.put("Parameter", "Agregate Column");
				map4.put("Value", aggregatorData.getDisplayName());
    			additionalParamsList.add(map4);
    			
    			ReportDefination dateQualifierData=reportDefinationRepository.findByReportIdAndDataType(repData.getId(), "DATE_QUALIFIER");
    			HashMap map5=new HashMap();
    			map5.put("Parameter", "Date qualifier");
				map5.put("Value", dateQualifierData.getDisplayName());
    			additionalParamsList.add(map5);
			}
			}
        	
        	if(filtersMap!=null && !(filtersMap.isEmpty())){
        		outputType=filtersMap.get("outputType").toString();
        	}
        	if(jobDataMap!=null && !(jobDataMap.isEmpty())){
        	val=jobDataMap.get("jobId").toString();
        	log.info("tenantId: "+tenantId+" val: "+val+" userId: "+userId+" reportId: "+reportId);
    		 JSONObject taboutput=new JSONObject();
    		 HashMap requestInfo=new HashMap();
    		
    		List<JobActions> jobactList=jobActionsRepository.findByJobId(val);
    		log.info("jobactList sz: "+jobactList.size());
    		
    		 JobActions jobAction=jobActionsRepository.findReportOutputPath(val, tenantId);
    				log.info("jobAction: "+jobAction);
    				if(jobAction!=null){
    				log.info("jobAction: "+jobAction);
    				String actionName=jobAction.getActionName();
    				String[] actionNamesArr=actionName.split("is: ");
    				log.info("actionNamesArr -0: "+actionNamesArr[0]+" actionNamesArr-1: "+actionNamesArr[1]);
    				outputPath=actionNamesArr[1];
    				Long schedulerId=jobAction.getSchedulerId();
    				taboutput=reportsService.testFileReading(outputPath,userId,val,schedulerId,tenantId,repData.getId());
    				newFileData=(JSONObject) taboutput.clone();
    				newFileData.put("outputPath", outputPath);
    				//flag=true;
    				dataMap.put("output", taboutput);
    				dataMap.put("outputPath", outputPath);
    				}
    				else{
    					//output=null;
    				}
    				JobActions pivotPathData=jobActionsRepository.findReportPivoutOutputPath(val, tenantId);
    				String pivotPath="";
    				if(pivotPathData!=null){
    					log.info("pivotPathData: "+pivotPathData);
    					String actionName=pivotPathData.getActionName();
    					String[] actionNamesArr=actionName.split("is: ");
    					log.info("actionNamesArr -0: "+actionNamesArr[0]+" actionNamesArr-1: "+actionNamesArr[1]);
    					pivotPath=actionNamesArr[1];
    					Long schedulerId=pivotPathData.getSchedulerId();
    					pivotOutput=reportsService.testFileReading(pivotPath,userId,val,schedulerId,tenantId,repData.getId());
    					newPivotFileData=(JSONObject) pivotOutput.clone();
    					dataMap.put("pivotOutput", pivotOutput);
    					dataMap.put("pivotOutputPath", pivotPath);
    					}
    					else{
    						//for Account Analysis Report
    						dataMap.put("pivotOutputPath", outputPath);
    					}
    	
        	
			if(dataMap!=null && !(dataMap.isEmpty())){
				if(dataMap.containsKey("output")){
			output=(JSONObject) dataMap.get("output");//output
			if(dataMap.containsKey("outputPath")){
			log.info("dataMap.get(outputPath: "+dataMap.get("outputPath"));
			}
			else{
				log.info("dataMap doesn't contain outputPath");
			}
			outputPath=dataMap.get("outputPath").toString();
			pivotOutputPath=dataMap.get("pivotOutputPath").toString();
			lastOne=jobDataMap.get("lastOne").toString();
			reportName=jobDataMap.get("reportName").toString();
			log.info("repReq before appending to file object: "+repReq);
			requestInfo.put("id", repReq.getId());
			requestInfo.put("idForDisplay", repReq.getIdForDisplay());
			requestInfo.put("request_type", repReq.getRequestType());
			requestInfo.put("req_name", repReq.getReqName());
			requestInfo.put("report_id", repReq.getReportId());
			requestInfo.put("status", repReq.getStatus());
			requestInfo.put("file_name", repReq.getFileName());
			requestInfo.put("output_path", repReq.getOutputPath());
			System.out.println("requestInfo; "+requestInfo);
			newFileData.put("requestInfo", requestInfo);
			newPivotFileData.put("requestInfo", requestInfo);
		}
			}
        	}
			
        	String status="";
        	System.out.println("outputPath.length(): "+outputPath.length());
		if(outputPath.length()>1)
		{	
		log.info("outputPath :"+outputPath);
		String[] bits = outputPath.split("/");
		lastOne = bits[bits.length-1];
		String[] pivotFileNameArr=pivotOutputPath.split("/");
		String pivotFileName=pivotFileNameArr[pivotFileNameArr.length-1];
		log.info("file name :"+lastOne);
		status=oozieService.getStatusOfOozieJobId(val);
		log.info("status after processess is completed :");
		repReq.setStatus(status);
		repReq.setGeneratedTime(ZonedDateTime.now());
		repReq.setOutputPath(outputPath);
		repReq.setPivotPath(pivotOutputPath);
		repReq.setFileName(lastOne);
		repReq.setLastUpdatedDate(ZonedDateTime.now());
		repReq=reportRequestsRepository.save(repReq);
		log.info(" final repReq :"+repReq);
		
		Notifications notification=new Notifications();
		notification.setModule("REPORTING");
		
		notification.setMessage("Requested "+reportName+" has been generated report");
		notification.setUserId(userId);
		notification.setIsViewed(false);
		notification.setActionType("REQUEST,REPORT");
		log.info("reportId: "+reportId+" repReq.getIdForDisplay(): "+repReq.getIdForDisplay());
		String repIdReqId=repReq.getIdForDisplay().toString().concat(","+reportId.toString());
		notification.setActionValue(repIdReqId);
		notification.setTenantId(tenantId);
		notification.setCreatedBy(userId);
		notification.setCreationDate(ZonedDateTime.now());
		notification.setLastUpdatedBy(userId);
		notification.setLastUpdatedDate(ZonedDateTime.now());
		notification=notificationsRepository.save(notification);
		log.info("notification :"+notification);
		int totDataCnt=0;
		if(output.containsKey("X-COUNT")){
			log.info("output contains X-COUNT key");
		totDataCnt=Integer.parseInt(output.get("X-COUNT").toString());
		}
		else{
			log.info("output doesn't contains X-COUNT key");
		}
		log.info("totDataCnt: "+totDataCnt);
		response.addIntHeader("X-COUNT", totDataCnt);
		
		if(output.containsKey("data")){
			log.info("output contains key data");
		}
		else{
			log.info("output doesn't contains key data");
		}
		List<LinkedHashMap> maps=(List<LinkedHashMap>) output.get("data");
		List<LinkedHashMap> subMaps=new ArrayList<LinkedHashMap>();
		if(maps!=null && maps.size()>0){
			if(maps.size()>=25){
			subMaps=maps.subList(0, 25);
			}
			else if(maps.size()<=25)
				subMaps=maps.subList(0, (maps.size()));
			
			log.info("submaps final count: "+subMaps.size());
		}
		
		
		output.put("data", subMaps);
		JSONObject requestInfo=(JSONObject) output.get("requestInfo");
		log.info("requestInfo from output: "+requestInfo);
		output.put("requestInfo", repReq);
		
		String path=dataMap.get("outputPath").toString();//outputPath
		log.info("path: "+path);
		
		output.put("outputPath", path);
		
		output.put("additionalParams", additionalParamsList);
		
		output.put("reportConditionsList", reportCondList);
		}
		
		else{
			log.info("In output path doesnt exists case and Updating Request status");
			status=oozieService.getStatusOfOozieJobId(val);
			log.info("status: "+status);
			repReq.setStatus(status);
			repReq.setLastUpdatedDate(ZonedDateTime.now());
			repReq=reportRequestsRepository.save(repReq);
			log.info(" final repReq :"+repReq);
			
		}
		log.info("**end of tabular API** "+ZonedDateTime.now());
		
		log.info("outputType: "+outputType);
		if(outputType.equalsIgnoreCase("TABLE")){
			return output;
		}
		else 
			return newPivotFileData;
			
    }
    
    /**
     * Author: Swetha
     * Api to present report output pagewise by reading hdfs file
     * @param reportPath
     * @param pageNumber
     * @param pageSize
     * @param response
     * @return
     * @throws IOException
     * @throws URISyntaxException
     * @throws org.json.simple.parser.ParseException
     */
    @GetMapping("/getReportOutputByPage")
    public JSONObject getReportOutputByPage(@RequestParam String reportPath, @RequestParam(required=false) Integer pageNumber, @RequestParam(required=false) Integer pageSize,HttpServletResponse response) throws IOException, URISyntaxException, org.json.simple.parser.ParseException{
    	
    	log.info("Rest Request to getReportOutputByPage with parameters: ");
    	log.info("reportPath: "+reportPath);
    	log.info("pageNumber: "+pageNumber+" & pageSize: "+pageSize);
    	
    	JSONObject finalOutput=new JSONObject();
    	List<JSONObject> limitedOutputList=new ArrayList<JSONObject>();
    	
    	LinkedHashMap cmpltOutput=reportsService.hdfsFileReading(reportPath);
    	int totDataCnt=Integer.parseInt(cmpltOutput.get("X-COUNT").toString());
		log.info("totDataCnt: "+totDataCnt);
		response.addIntHeader("X-COUNT", totDataCnt);
    	
		//LinkedHashMap outputMap=cmpltOutput.get("")
		
		List<JSONObject> outputList=(List<JSONObject>) cmpltOutput.get("data");
		//log.info("outputList: "+outputList);
		List<JSONObject> HeaderList=(List<JSONObject>) cmpltOutput.get("columns");
		log.info("HeaderList: "+HeaderList);
		
		JSONObject hMap=HeaderList.get(0);
		log.info("hMap: "+hMap);
		
		int limit = 0;
		if(pageNumber == null || pageNumber == 0)
		{
			pageNumber = 0;
		}
		if(pageSize == null || pageSize == 0)
		{
			pageSize = totDataCnt;
		}
			limit = ((pageNumber+1) * pageSize + 1)-1;
		int startIndex=pageNumber*pageSize; 
		
		if(limit>totDataCnt){
			limit=totDataCnt;
		}
		
		log.info("startIndex: "+startIndex+" limit: "+limit);
		
		for(int j=startIndex;j<limit;j++){
			
			JSONObject map=outputList.get(j);
			limitedOutputList.add(map);
			
		}
		
		log.info("limitedOutputList: "+limitedOutputList);
		finalOutput.put("data", limitedOutputList);
		finalOutput.put("columns", HeaderList);
		finalOutput.put("X-COUNT", totDataCnt);
		return finalOutput;
    }
    
    
    
    /**
     * Updated: Rk,
     * Author: Ravali,
     * Author: Swetha [Integrated Global & Column level search]
     * @param requestId
     * @param outputType
     * @param pageNumber
     * @param pageSize
     * @param sortColumn
     * @param sortOrder
     * @param searchString
     * @param searchColumn
     * @param response
     * @param request
     * @return
     * @throws IOException
     * @throws URISyntaxException
     * @throws org.json.simple.parser.ParseException
     * @throws JSONException
     * @throws ParseException
     */
    @PostMapping("/getReportOutputByRequestId")
    public JSONObject getReportOutputByRequestId(@RequestParam String requestId,@RequestParam String outputType, @RequestParam(required=false) Integer pageNumber, @RequestParam(required=false) Integer pageSize,@RequestParam(required=false) String sortColumn
    		,@RequestParam(required=false) String sortOrder,@RequestParam(required=false) String searchString, @RequestBody(required=false) List<HashMap> searchObject,
    		HttpServletResponse response, HttpServletRequest request) throws IOException, URISyntaxException, org.json.simple.parser.ParseException, JSONException, ParseException{

    	log.info("Rest Request to getReportOutputByPage with parameters requestId: "+requestId+"sortColumn :"+sortColumn+" sortOrder:"+sortOrder+" searchString :"+searchString+" outputType :"+outputType);
    	log.info("requestId: "+requestId);
    	HashMap map1=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map1.get("tenantId").toString());
    	ReportRequests reqData=reportRequestsRepository.findByTenantIdAndIdForDisplay(tenantId, requestId);
    	ReportRequests req=reportRequestsRepository.findOne(reqData.getId());
    	System.out.println("req: "+req);
    	String outputPath="";
    	if(outputType.equalsIgnoreCase("table"))
    		outputPath=req.getOutputPath();
    	else if(outputType.equalsIgnoreCase("pivot"))
    		outputPath=req.getPivotPath();
    	log.info("outputPath: "+outputPath);
    	log.info("pageNumber: "+pageNumber+" & pageSize: "+pageSize);

    	JSONObject finalOutput=new JSONObject();
    	List<JSONObject> limitedOutputList=new ArrayList<JSONObject>();
    	LinkedHashMap cmpltOutput=reportsService.hdfsFileReading(outputPath);
    	int totDataCnt=0;
    	if(cmpltOutput.get("X-COUNT")!=null)
    	{
    		totDataCnt=Integer.parseInt(cmpltOutput.get("X-COUNT").toString());
    		log.info("totDataCnt: "+totDataCnt);
    	}
    	
    	// **************** START Rk *********************
    	List<JSONObject> outputList=new ArrayList<JSONObject>();
		outputList=(List<JSONObject>) cmpltOutput.get("data");
		List<JSONObject> HeaderList=new ArrayList<JSONObject>();
		HeaderList=(List<JSONObject>) cmpltOutput.get("columns");
		log.info("HeaderList: "+HeaderList);
		List<String> headList=new ArrayList<String>();
		String sortColDataType=null;
		if(HeaderList!=null && !(HeaderList.isEmpty()))
		for(JSONObject eachCol:HeaderList) {
    		headList.add(eachCol.get("field").toString());
    		if(eachCol.get("field")!=null && eachCol.get("field").toString().equals(sortColumn)) {
    			sortColDataType=eachCol.get("dataType").toString();
    		}
    	}
    	if(searchString!=null&&searchString.length()>0) {
    		log.info("********************: In side global search");
    		outputList=reportsService.jsonArrayListGlobalSearch(outputList, HeaderList, searchString);
    	}
    	if(searchObject!=null && !(searchObject.isEmpty())) {
    		log.info("********************: In side column level search");
    		outputList=reportsService.jsonArrayListKeyLevelSearch(outputList, searchObject);
    	}
    	if(sortColumn!=null)
    	{
    		log.info("********************: In side sort");
    		outputList=reportsService.sortJsonArrayList(outputList, sortColumn, sortOrder, sortColDataType);
    	}
    	
    	if(outputList!=null)
    	totDataCnt=outputList.size();
    	response.addIntHeader("X-COUNT", totDataCnt);
    	int limit = 0;
		if(pageNumber == null || pageNumber == 0)
		{
			pageNumber = 0;
		}
		if(pageSize == null || pageSize == 0)
		{
			pageSize = totDataCnt;
		}
		limit = ((pageNumber+1) * pageSize + 1)-1;
		int startIndex=pageNumber*pageSize; 

		if(limit>totDataCnt){
			limit=totDataCnt;
		}
		log.info("startIndex: "+startIndex+" limit: "+limit);
		if(outputList!=null && outputList.size()>0 && limit<=outputList.size()){
		limitedOutputList=outputList.subList(startIndex, limit);
		}
		else limitedOutputList=outputList;
		finalOutput.put("data", limitedOutputList);
		finalOutput.put("columns", HeaderList);
		finalOutput.put("X-COUNT", totDataCnt);
		// ***************** END Rk ******************
    	return finalOutput;
    }
   
    
    /**
     * Author: Swetha
     * @param tenantId
     * @param reportId
     * @param filtersMap
     * @return
     * @throws org.json.simple.parser.ParseException
     * @throws IOException
     * @throws OozieClientException
     * @throws URISyntaxException
     */
    @PostMapping("/PivotViewReport")
    @Timed
    public List<LinkedHashMap> PivotViewReportNew(HttpServletRequest request, @RequestParam Long reportId, @RequestBody(required = false) HashMap filtersMap) 
    		throws org.json.simple.parser.ParseException, IOException, OozieClientException, URISyntaxException{
    	HashMap map0=userJdbcService.getuserInfoFromToken(request);
      	Long tenantId=Long.parseLong(map0.get("tenantId").toString());
    	Long userId=9L;
    	log.info("Rest Request to reportingPOC with reportId: "+reportId+" @"+DateTime.now());
    	log.info("filtersMap: "+filtersMap);
    	JSONObject obj = new JSONObject();
    	obj.putAll(filtersMap);
    	log.info("obj: "+obj);
    	
    	List<LinkedHashMap> maps=new ArrayList<LinkedHashMap>();
    	Reports report=reportsRepository.findOne(reportId);
		String reportName=report.getReportName();
		
		ReportRequests repReq=new ReportRequests();
		ReportRequests repReqUpd=new ReportRequests();
		String requestName=report.getReportName()+ZonedDateTime.now();
		repReq.setReqName(requestName);
		repReq.setReportId(reportId);
		repReq.setTenantId(tenantId);
		repReq.setStatus("RUNNING");
			String filMap=obj.toJSONString();
			log.info("filMap: "+filMap);
		repReq.setFilterMap(filMap);
		repReq.setSubmittedTime(ZonedDateTime.now());
		
		repReq.setCreatedBy(userId);
		repReq.setLastUpdatedBy(userId);
		repReq.setCreatedDate(ZonedDateTime.now());
		repReq.setLastUpdatedDate(ZonedDateTime.now());
		repReq.setRequestType("Run");
		repReq=reportRequestsRepository.save(repReq);
    	log.info("repReq :"+repReq);
    	
    	String cmpltFilePath=reportsService.FileWriteHDFS(reportId, obj,"params",tenantId);
    	log.info("done writing file to hdfs");
    	
    	HashMap parameterSet = new HashMap();
		parameterSet.put("param1", reportId);
		parameterSet.put("param2", cmpltFilePath);
		parameterSet.put("param5", "Pivot");
		

		log.info("Api call to Intiate Job for Data Transformation process: "+parameterSet);
		ResponseEntity jobStatus=oozieService.jobIntiateForAcctAndRec(tenantId, userId, "Reporting_Dev", parameterSet,null,request);
		log.info("jobStatus: "+jobStatus);
		HashMap map=(HashMap) jobStatus.getBody();
		log.info("map: "+map);
		String val=map.get("status").toString();
		log.info("val: "+val);
		JSONObject pivotOutput=new JSONObject();
		LinkedHashMap dataMap=new LinkedHashMap();
		
		String status="";
    	String lastOne="";
		String pivotOutputPath="";
		
		if(val.equalsIgnoreCase("Failed to intiate job")){
			log.info("Reporting Program Failed");
			repReq.setStatus("FAILED");
			repReq.setLastUpdatedDate(ZonedDateTime.now());
			repReq=reportRequestsRepository.save(repReq);
			log.info("updating repReq if it is failed :"+repReq);
		}
		else{
			log.info("Job has been initiated succesfully");
			status=oozieService.getStatusOfOozieJobId(val);
			log.info("status: "+status);
			repReq.setJobId(val);
			repReq.setLastUpdatedDate(ZonedDateTime.now());
			repReq=reportRequestsRepository.save(repReq);
			log.info("updating request with jobId: "+repReq);
			for(int i=0;;i++){
				
				status=oozieService.getStatusOfOozieJobId(val);
				
				if(!(status.equalsIgnoreCase("RUNNING"))){
					log.info("status: "+status);
					
				break;
				//log.info("outputPath at i: "+i+" is: "+outputPath);
					}
				else{
					//log.info("status is still running: "+status);
				}
			}
			log.info("request to get success job status");
			
			JobActions pivotPathData=jobActionsRepository.findReportPivoutOutputPath(val, tenantId);
			System.out.println("pivotPathData: "+pivotPathData);
			String pivotPath="";
			if(pivotPathData!=null){
				log.info("pivotPathData: "+pivotPathData);
				String actionName=pivotPathData.getActionName();
				String[] actionNamesArr=actionName.split("is: ");
				log.info("actionNamesArr -0: "+actionNamesArr[0]+" actionNamesArr-1: "+actionNamesArr[1]);
				pivotPath=actionNamesArr[1];
				Long schedulerId=pivotPathData.getSchedulerId();
				pivotOutput=reportsService.testFileReading(pivotPath,userId,val,schedulerId,tenantId,reportId);
				JSONObject newPivotFileData = (JSONObject) pivotOutput.clone();
				dataMap.put("pivotOutput", pivotOutput);
				dataMap.put("pivotOutputPath", pivotPath);
				}
			
			if(pivotPath!=null && !(pivotPath.isEmpty()) && pivotPath.length()>1){
				String[] bits = pivotPath.split("/");
				lastOne = bits[bits.length-1];
				String[] pivotFileNameArr=pivotOutputPath.split("/");
				String pivotFileName=pivotFileNameArr[pivotFileNameArr.length-1];
				log.info("file name :"+lastOne);
				status=oozieService.getStatusOfOozieJobId(val);
				log.info("status after processess is completed :");
				repReq.setStatus(status);
				repReq.setGeneratedTime(ZonedDateTime.now());
				repReq.setOutputPath("");
				repReq.setPivotPath(pivotPath);
				repReq.setFileName(lastOne);
				repReq.setLastUpdatedDate(ZonedDateTime.now());
				repReqUpd=reportRequestsRepository.save(repReq);
				log.info(" final repReq :"+repReqUpd);
				
				Notifications notification=new Notifications();
				notification.setModule("REPORTING");
				
				notification.setMessage("Requested "+reportName+" has been generated report");
				notification.setUserId(userId);
				notification.setIsViewed(false);
				notification.setActionType("SCHEDULER");
				SchedulerDetails sch=schedulerDetailsRepository.findByOozieJobId(val);
				notification.setActionValue(sch.getId().toString());
				notification.setTenantId(tenantId);
				notification.setCreatedBy(userId);
				notification.setCreationDate(ZonedDateTime.now());
				notification.setLastUpdatedBy(userId);
				notification.setLastUpdatedDate(ZonedDateTime.now());
				notification=notificationsRepository.save(notification);
				log.info("notification :"+notification);
				
				if(pivotOutput.containsKey("data")){
					log.info("output contains key data");
				}
				else{
					log.info("output doesn't contains key data");
				}
				maps=(List<LinkedHashMap>) pivotOutput.get("data");
				JSONObject requestInfo=(JSONObject) pivotOutput.get("requestInfo");
				log.info("requestInfo from output: "+requestInfo);
				pivotOutput.put("requestInfo", repReq);
				pivotOutput.put("outputPath", pivotPath);
			}
			else{
				log.info("In output path doesnt exists case and Updating Request status");
				status=oozieService.getStatusOfOozieJobId(val);
				log.info("status: "+status);
				repReqUpd.setStatus(status);
				repReqUpd.setLastUpdatedDate(ZonedDateTime.now());
				reportRequestsRepository.save(repReqUpd);
				log.info(" final repReqUpd :"+repReqUpd);
			}
		
			log.info("**end of pivot API** "+ZonedDateTime.now());	
		}
		return maps;
		
    }
    
    
    @GetMapping("/aaaamatchingSuggestions")
    @Timed
    public String aaaamatchingSuggestions(@RequestParam String sourcePath,@RequestParam String targetPath) throws IOException, URISyntaxException, org.json.simple.parser.ParseException, InvalidFormatException
    {
    	ArrayList<ArrayList<String>> srcList = new ArrayList<ArrayList<String>>();
    	ArrayList<ArrayList<String>> trgtList = new ArrayList<ArrayList<String>>();
    	ArrayList<ArrayList<String>> exactMatchList=new ArrayList<ArrayList<String>>();
    	srcList=readExcelFile(sourcePath);
    	trgtList=readExcelFile(targetPath);
    	log.info("srcList size :"+srcList.size());
    	log.info("trgtList size :"+trgtList.size());
    	exactMatchList=findExactMatch(srcList,trgtList,14,9);
    	log.info("exactMatchList size :"+exactMatchList.size());
    	return "Success";
    }
    
    private ArrayList<ArrayList<String>> readExcelFile(String filePath) throws InvalidFormatException, IOException {
    	ArrayList<ArrayList<String>> finalList = new ArrayList<ArrayList<String>>();
    	Workbook workbook = WorkbookFactory.create(new File(filePath));		// Reading excel file from path we can update it
		Sheet sheet = workbook.getSheetAt(0);														// creating first sheet object
		DataFormatter dataFormatter = new DataFormatter();											//DataFormatter to format and get each cell's value as String
		for(int i=1, rowsLen=sheet.getPhysicalNumberOfRows(); i<rowsLen;i++){
			org.apache.poi.ss.usermodel.Row row=sheet.getRow(i);
			ArrayList<String> eachRow=new ArrayList<String>();
			for(int j=0, colsLen=row.getLastCellNum();j<colsLen;j++){								// Read all cell values of current row in to one arraylist
				eachRow.add(dataFormatter.formatCellValue(row.getCell(j)));
			}
			finalList.add(eachRow);
		}
    	return finalList;
    }
    
    private ArrayList<String> readExcelFileAsStringList(String filePath) throws InvalidFormatException, IOException {
    	ArrayList<String> finalList = new ArrayList<String>();
    	Workbook workbook = WorkbookFactory.create(new File(filePath));		// Reading excel file from path we can update it
		Sheet sheet = workbook.getSheetAt(0);														// creating first sheet object
		DataFormatter dataFormatter = new DataFormatter();											//DataFormatter to format and get each cell's value as String
		for(int i=1, rowsLen=sheet.getPhysicalNumberOfRows(); i<rowsLen;i++){
			org.apache.poi.ss.usermodel.Row row=sheet.getRow(i);
			ArrayList<String> eachRow=new ArrayList<String>();
			for(int j=0, colsLen=row.getLastCellNum();j<colsLen;j++){								// Read all cell values of current row in to one arraylist
				eachRow.add((dataFormatter.formatCellValue(row.getCell(j))).toUpperCase().replace("|", ""));
			}
			finalList.add((String.join("|", eachRow)));
		}
    	return finalList;
    }
    
    private ArrayList<ArrayList<String>> findExactMatch(ArrayList<ArrayList<String>> srcList,ArrayList<ArrayList<String>> trgtList,int srcAmtInx, int trgtAmtInx) {
    	ArrayList<ArrayList<String>> matchColIndxsList=new ArrayList<ArrayList<String>>();
    	ArrayList<String> srcAndTrgtMatchedRowsIndxs = new ArrayList<String>();
    	HashMap<String, String> dict = new HashMap<String, String>();
		// *********Adding Data to dictionary**********
		dict.put("SIx Continents", "Intercontinental Hotel Group");
		dict.put("WEDDINGTON", "Gap");
		dict.put("IAC", "VIMEO");
		dict.put("Land of Nod", "MEADOWBROOK L.L.C.");
		dict.put("ADS Alliance", "Conversant Media");
		dict.put("FAST RETAILING U DES","UNIQLO USA LLC");
		dict.put("MICROSOFT DES","MICROSOFT CORPORATION");
		dict.put("MLW Squared", "Ahalogy");
		dict.put("LINTV", "Harris & Forstot");
		dict.put("Hearst", "iCrossing");
		dict.put("WesternStone", "The Shane Company");
		dict.put("ADS Alliance", "Conversant Media");
		dict.put("VF Service", "The North Face");
		dict.put("Graham Holdings", "Social Code");
		dict.put("ShapeUp Club AB", "Lifesum AB");
		dict.put("Arcadia", "Dorothy Perkins");
		dict.put("KLM Royal Dutch Airlines", "Koninklijke Luchtvaart Maatschappij NV");
		dict.put("Ted Baker", "No Ordinary Label Limited");
		dict.put("Socialize", "Socialyse");
		dict.put("Glow Digital Media Limited", "AdGlow");
		dict.put("Euromarket Desig DES", "MEADOWBROOK L.L.C.");
		//**********************END********************

    	ArrayList<ArrayList<String>> tmpSrcList=new ArrayList<ArrayList<String>>(), tmpTrgtList=new ArrayList<ArrayList<String>>();
    	int cnt=0, srcColSize=srcList.get(0).size(), trgtColSize=trgtList.get(0).size();
    	Boolean isMatched=false;
    	for(int i=0;i<srcList.size();i++) {	// For each source row
    		if(srcList.get(i).get(srcAmtInx)!=null&&srcList.get(i).get(srcAmtInx).trim().length()>0) {
        		BigDecimal srcAmt = new BigDecimal(srcList.get(i).get(srcAmtInx));
    			for(int j=0;j<trgtList.size();j++) {	// For each target row
//    				isMatched=false;
    				if(trgtList.get(j).get(trgtAmtInx)!=null&&trgtList.get(j).get(trgtAmtInx).trim().length()>0) {
    					BigDecimal trgtAmt = new BigDecimal(trgtList.get(j).get(trgtAmtInx));
    					if(srcAmt.compareTo(trgtAmt)==0) {								// Both amounts are matched exactly
    						log.info("Source Row: "+(i+1)+" and Target Row: "+(j+1)+" are matched with amount with :"+trgtAmt);
    						if(matchingWithDict(srcList.get(i),trgtList.get(j),dict)) {
    							log.info("Source Row"+(i+1)+" Target Row "+(j+1)+" matched with dictionary");
    							srcAndTrgtMatchedRowsIndxs.add((i+tmpSrcList.size())+":"+(j+tmpTrgtList.size()));
    							tmpSrcList.add(srcList.get(i));
    							tmpTrgtList.add(trgtList.get(j));
    							srcList.remove(i);
    							trgtList.remove(j);
    							i--;
    							break;
    						}
    					}
    				}
    	    	}
    		}
    	}
    	
    	log.info("tmpSrcList :"+tmpSrcList.size());
    	log.info("tmpTrgtList :"+tmpTrgtList.size());
    	log.info("srcList :"+srcList.size());
    	log.info("trgtList :"+trgtList.size());
//    	log.info("srcMatchedRowsIndxs :"+srcAndTrgtMatchedRowsIndxs);
    	for(int i=0;i<srcList.size();i++) {	// For each source row
    		if(srcList.get(i).get(srcAmtInx)!=null&&srcList.get(i).get(srcAmtInx).trim().length()>0) {
        		BigDecimal srcAmt = new BigDecimal(srcList.get(i).get(srcAmtInx));
        		BigDecimal tempAmt=new BigDecimal(0);
        		ArrayList<Integer> tmptList=new ArrayList<Integer>();
    			for(int j=0;j<trgtList.size();j++) {	// For each target row
    				if(trgtList.get(j).get(trgtAmtInx)!=null&&trgtList.get(j).get(trgtAmtInx).trim().length()>0) {
    					BigDecimal trgtAmt = new BigDecimal(trgtList.get(j).get(trgtAmtInx));
    					if(srcAmt.compareTo(trgtAmt)>0&&(tempAmt.add(trgtAmt).compareTo(srcAmt)<=0)) {								// Both amounts are matched exactly
    						if(matchingWithDict(srcList.get(i),trgtList.get(j),dict)) {
    							log.info("Inside match with multiple values src amt :"+srcAmt +" And trgt partial amt :"+trgtAmt);
    							log.info("Before adding present amt :"+tempAmt+" trgtAmt :"+trgtAmt);
    							tempAmt=tempAmt.add(trgtAmt);
    							tmptList.add(j);
    							log.info("After adding total amt :"+tempAmt+" trgtAmt"+trgtAmt);
    							if(srcAmt.compareTo(tempAmt)==0) {
        							log.info("Matched src amt :"+srcAmt +" And trgt amt :"+tempAmt);
    								tmpSrcList.add(srcList.get(i));	
        							srcList.remove(i);
    								for(Integer ind:tmptList) {
    									tmpTrgtList.add(trgtList.get(ind));
    								}
    								for(Integer ind:tmptList) {
    									trgtList.remove(ind);
    								}
//									StringBuilder strbul = new StringBuilder();
//									Iterator<Integer> iter = tmptList.iterator();
//									while (iter.hasNext()) {
//										strbul.append(iter.next());
//										if (iter.hasNext()) {
//											strbul.append(",");
//										}
//									}
//    								srcAndTrgtMatchedRowsIndxs.add((i)+":"+strbul.toString());
    								i--;
        							break;
    							}   							
    						}
    					}
    				}
    	    	}
    		}
    	}
    	log.info("tmpSrcList :"+tmpSrcList.size());
    	log.info("tmpTrgtList :"+tmpTrgtList.size());
    	log.info("srcList :"+srcList.size());
    	log.info("trgtList :"+trgtList.size());
//    	log.info("srcMatchedRowsIndxs :"+srcAndTrgtMatchedRowsIndxs);
    	return matchColIndxsList;
    }
    
	private Boolean matchingWithDict(ArrayList<String> srcList, ArrayList<String> trgtList, HashMap<String, String> dict) {
		String srcRow=String.join("|", srcList), trgtRow=String.join("|", trgtList);
		for (HashMap.Entry<String, String> item : dict.entrySet()) {
			String key = item.getKey(), value = item.getValue();
//			^(?=.*Land.*)(?=.*Nod.*)(?=.*of.*).*$
//			^(?=.*\bjack\b)(?=.*\bjames\b)(?=.*\bjason\b)(?=.*\bjules\b).*$
			String regex1="^";
			for(String tmp: key.split("\\s")) {
				regex1=regex1+"(?=.*"+tmp.toUpperCase()+".*)";
			}
			regex1=regex1+".*$";
			String regex2="^";
			for(String tmp: value.split("\\s")) {
				regex2=regex2+"(?=.*"+tmp.toUpperCase()+".*)";
			}
			regex2=regex2+".*$";
			if(srcRow.toUpperCase().matches(regex1+"|"+regex2)&&trgtRow.toUpperCase().matches(regex1+"|"+regex2)) {
				log.info("Matched source :"+key.toUpperCase()+" Target :"+value.toUpperCase());
				return true;
			}
		}
		return false;
	}

	@GetMapping("/_aaaaMatchingWithRulesPOC")
    @Timed
    public String _aaaaMatchingWithRulesPOC(@RequestParam String sourcePath,@RequestParam String targetPath) throws IOException, URISyntaxException, org.json.simple.parser.ParseException, InvalidFormatException, ParseException
    {
    	ArrayList<ArrayList<String>> srcList = new ArrayList<ArrayList<String>>();
    	ArrayList<ArrayList<String>> trgtList = new ArrayList<ArrayList<String>>();
    	ArrayList<ArrayList<String>> matchedList = new ArrayList<ArrayList<String>>();
    	ArrayList<ArrayList<String>> matchedList1 = new ArrayList<ArrayList<String>>();
    	srcList=readExcelFile(sourcePath);
    	trgtList=readExcelFile(targetPath);
    	log.info("srcList size :"+srcList.size());
    	log.info("trgtList size :"+trgtList.size());
    	Long srcDvId=208l, trgtDvId=209l;
    	ArrayList<Long> oneToOneIds=new ArrayList<Long>();
    	oneToOneIds.add(584l);
    	ArrayList<Long> oneToManyIds=new ArrayList<Long>();
    	oneToManyIds.add(585l);
//    	ArrayList<Long> oneToManyIds=(ArrayList<Long>) Arrays.asList(585l,588l,589l);
//    	ArrayList<Long> manyToOneIds=(ArrayList<Long>) Arrays.asList(586l);
//    	ArrayList<Long> manyTomanyIds=(ArrayList<Long>) Arrays.asList(587l);
    	
    	List<Long> srcColsList=new ArrayList<Long>();
    	List<Long> trgtColsList=new ArrayList<Long>();
    	List<DataViewsColumns> srcDataViewsColumns=dataViewsColumnsRepository.findByDataViewId(208l);
    	HashMap<Long,String> srcColDatatypes=new HashMap<Long,String>();
    	HashMap<Long,String> trgtColDatatypes=new HashMap<Long,String>();
    	for(DataViewsColumns col:srcDataViewsColumns) {
    		srcColsList.add(col.getId());
    		srcColDatatypes.put(col.getId(), col.getColDataType());
    	}
    	List<DataViewsColumns> trgtDataViewsColumns=dataViewsColumnsRepository.findByDataViewId(209l);
    	for(DataViewsColumns col:trgtDataViewsColumns) {
    		trgtColsList.add(col.getId());
    		trgtColDatatypes.put(col.getId(), col.getColDataType());
    	}
    	ArrayList<String> cmpltdSrcList=new ArrayList<String>(),cmpltdTrgtList=new ArrayList<String>(),
    			cmpltdSrcList1=new ArrayList<String>(),cmpltdTrgtList1=new ArrayList<String>();
    	SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
		Calendar cal = Calendar.getInstance();
		RuleConditions dtRuleCondition=null,amtRuleCondition=null;
    	for(Long id:oneToOneIds) {
    		List<RuleConditions> ruleConditions=ruleConditionsRepository.findByRuleId(id);
    		int srcAmtIndx=-1,srcDtIndx=-1,trgtAmtIndx=-1,trgtDtIndx=-1;
    		// ******** find index and colId of amount column and date columns ********
    		for(RuleConditions condition:ruleConditions) {
    				if(srcColDatatypes.get(condition.getsColumnId()).equals("DECIMAL")&&trgtColDatatypes.get(condition.gettColumnId()).equals("DECIMAL")) {
    				srcAmtIndx=srcColsList.indexOf(condition.getsColumnId());
    				trgtAmtIndx=trgtColsList.indexOf(condition.gettColumnId());
    				amtRuleCondition=condition;
    			}
    			else if(srcColDatatypes.get(condition.getsColumnId()).equals("DATE")&&trgtColDatatypes.get(condition.gettColumnId()).equals("DATE")) {
    				srcDtIndx=srcColsList.indexOf(condition.getsColumnId());
    				trgtDtIndx=trgtColsList.indexOf(condition.gettColumnId());
    				dtRuleCondition=condition;
    			}
    		}
    		// ******** END of find index of amount column and date columns ********
    		log.info("srcDtIndx :"+srcDtIndx+" trgtDtIndx :"+trgtDtIndx+" srcAmtIndx :"+srcAmtIndx+" trgtAmtIndx:"+trgtAmtIndx);
    		if(srcAmtIndx<0||trgtAmtIndx<0) {
    			log.info("********************* Amount columns indexes not found ******************");
    			break;
    		}
    		log.info("**************************** ONE TO ONE MATCHES *******************************************");
    		if((srcDtIndx<0||trgtDtIndx<0)&&(srcAmtIndx>=0&&trgtAmtIndx>=0)) {
    			
    		}else if((srcDtIndx>=0&&trgtDtIndx>=0)&&(srcAmtIndx>=0&&trgtAmtIndx>=0)) {
    			for(int i=0;i<srcList.size();i++) {	// For each source row
    				int srcLstInd=srcList.get(i).size()-1;
    				if(cmpltdSrcList.contains(srcList.get(i).get(srcLstInd)))
    					continue;
    	    		if(srcList.get(i).get(srcAmtIndx)!=null&&srcList.get(i).get(srcAmtIndx).trim().length()>0) {
    	        		BigDecimal srcAmt = new BigDecimal(srcList.get(i).get(srcAmtIndx));
//						Calculating date ranges by considering tolerance
    	        		Date srcDtRangeFrom=addOrSubtractDays(srcList.get(i).get(srcDtIndx),dtRuleCondition.getsToleranceOperatorFrom(),dtRuleCondition.getsToleranceValueFrom(),sdf,cal);
						Date srcDtRangeTo=addOrSubtractDays(srcList.get(i).get(srcDtIndx),dtRuleCondition.getsToleranceOperatorTo(),dtRuleCondition.getsToleranceValueTo(),sdf,cal);
    	    			for(int j=0;j<trgtList.size();j++) {	// For each target row
    	    				int trgtLstInd=trgtList.get(j).size()-1;
    	    				if(cmpltdTrgtList.contains(trgtList.get(j).get(trgtLstInd)))
    	    					continue;
    	    				if(trgtList.get(j).get(trgtAmtIndx)!=null&&trgtList.get(j).get(trgtAmtIndx).trim().length()>0) {
    	    					BigDecimal trgtAmt = new BigDecimal(trgtList.get(j).get(trgtAmtIndx));
    	    					if(srcAmt.compareTo(trgtAmt)==0) {								// Both amounts are matched exactly
//    	    						log.info(">>>>>>>Source Row: "+(i+1)+" and Target Row: "+(j+1)+" are matched with Source amount :"+srcAmt+" Target amount :"+trgtAmt);
//    	    						Calculating date ranges by considering tolerance
    	    						Date trgtDtRangeFrom=addOrSubtractDays(trgtList.get(j).get(trgtDtIndx),dtRuleCondition.gettToleranceOperatorFrom(),dtRuleCondition.gettToleranceValueFrom(),sdf,cal);
    	    						Date trgtDtRangeTo=addOrSubtractDays(trgtList.get(j).get(trgtDtIndx),dtRuleCondition.gettToleranceOperatorTo(),dtRuleCondition.gettToleranceValueTo(),sdf,cal);
//    	    						Date ranges comparision
    	    						if((!srcDtRangeFrom.after(trgtDtRangeFrom) && !srcDtRangeFrom.before(trgtDtRangeTo))||(!srcDtRangeTo.after(trgtDtRangeFrom) && !srcDtRangeTo.before(trgtDtRangeTo))
    	    								||(!trgtDtRangeFrom.after(srcDtRangeFrom) && !trgtDtRangeFrom.before(srcDtRangeTo))||(!trgtDtRangeTo.after(srcDtRangeFrom) && !trgtDtRangeTo.before(srcDtRangeTo))) {
//    	    							log.info(">>>>>>>>>>>>>>Source Row: "+(i+1)+" and Target Row: "+(j+1)+" are matched with Dates also :"+trgtAmt);
    	    							int succededCnt=2;
    	    							for(RuleConditions eachCondition:ruleConditions) {
    	    								if(eachCondition!=dtRuleCondition&&eachCondition!=amtRuleCondition) {
    	    									int srcInd=srcColsList.indexOf(eachCondition.getsColumnId()), trgtInd=trgtColsList.indexOf(eachCondition.getsColumnId());
    	    									switch(srcColDatatypes.get(eachCondition.getsColumnId())) {
	    	    									case "INTEGER":
	    	    										switch(eachCondition.getOperator()) {
		    	    										case "=":
		    	    											if(Integer.parseInt(srcList.get(i).get(srcInd))==Integer.parseInt(trgtList.get(j).get(trgtInd)))
		    	    												succededCnt++;
		    	    											break;
		    	    										case "<":
		    	    											if(Integer.parseInt(srcList.get(i).get(srcInd))<Integer.parseInt(trgtList.get(j).get(trgtInd)))
		    	    												succededCnt++;
		    	    											break;
		    	    										case ">":
		    	    											if(Integer.parseInt(srcList.get(i).get(srcInd))>Integer.parseInt(trgtList.get(j).get(trgtInd)))
		    	    												succededCnt++;
		    	    											break;
		    	    										case "<=":
		    	    											if(Integer.parseInt(srcList.get(i).get(srcInd))<=Integer.parseInt(trgtList.get(j).get(trgtInd)))
		    	    												succededCnt++;
		    	    											break;
		    	    										case ">=":
		    	    											if(Integer.parseInt(srcList.get(i).get(srcInd))>=Integer.parseInt(trgtList.get(j).get(trgtInd)))
		    	    												succededCnt++;
		    	    											break;
		    	    										case "!=":
		    	    											if(Integer.parseInt(srcList.get(i).get(srcInd))!=Integer.parseInt(trgtList.get(j).get(trgtInd)))
		    	    												succededCnt++;
		    	    											break;
		    	    									}
	    	    										break;
	    	    									case "VARCHAR":
	    	    										if(varcharValidation(eachCondition.getOperator(),srcList.get(i).get(srcColsList.indexOf(eachCondition.getsColumnId())),trgtList.get(i).get(trgtColsList.indexOf(eachCondition.gettColumnId())))) {
	    	    											succededCnt++;
	    	    										}
	    	    										break;
	    	    									case "DATE":
	    	    										break;
	    	    									case "DATETIME":
	    	    										break;
	    	    									case "BOOLEAN":
	    	    										break;
	    	    									case "DECIMAL":
//	    	    										new BigDecimal(trgtList.get(j).get(trgtAmtIndx))
	    	    										if(decimalComparision(eachCondition.getOperator(),new BigDecimal(srcList.get(i).get(srcInd)),new BigDecimal(trgtList.get(j).get(trgtInd)))) {
	    	    											succededCnt++;
	    	    										}
	    	    										break;
    	    									}
    	    								}
    	    							}
    	    							if(succededCnt==ruleConditions.size()) {
//    	    								log.info("********FINALLY********Source Row: "+(i+1)+" and Target Row: "+(j+1)+" are matched with all conditions");
    	    								ArrayList<String> tmpList=srcList.get(i);
    	    								tmpList.add("Source Row");
    	    								tmpList.add(trgtList.get(j).get(trgtLstInd));
    	    								matchedList.add(tmpList);
    	    								log.info("One To One>>>>>>:  "+tmpList);
    	    								ArrayList<String> tmpList1=trgtList.get(j);
    	    								tmpList1.add("Target Row");
    	    								tmpList1.add(srcList.get(i).get(srcLstInd));
    	    								matchedList.add(tmpList1);
    	    								log.info("One To One>>>>>>:  "+tmpList1);
    	    								cmpltdSrcList.add(srcList.get(i).get(srcLstInd));
    	    								srcList.remove(i);
    	    								i--;
    	    								cmpltdTrgtList.add(trgtList.get(j).get(trgtLstInd));
    	    								trgtList.remove(j);
    	    								j--;
    	    								break;
    	    							}
    	    						}else {
//    	    							log.info("XXXXXXXXXXX Source Row: "+(i+1)+" and Target Row: "+(j+1)+" are not matched with Dates");
    	    						}
    	    					}
    	    				}
    	    	    	}
    	    		}
    	    	}
    		}
    	}
    	log.info("One to One Matches Source :"+cmpltdSrcList);
    	log.info("One to One Matches Target :"+cmpltdTrgtList);
    	log.info("One to One Matches Source Count :"+cmpltdSrcList.size());
    	log.info("One to One Matches Target Count :"+cmpltdTrgtList.size());
//    	log.info("**************************** ONE TO ONE MATCHES *******************************************");
//    	for(ArrayList<String> eachList: matchedList) {
//    		log.info("One To One>>>>>>:  "+eachList);
//    	}
    	log.info("**************************** END OF ONE TO ONE MATCHES *******************************************");
    	dtRuleCondition=null;
    	amtRuleCondition=null;
    	for(Long id:oneToManyIds) {
    		List<RuleConditions> ruleConditions=ruleConditionsRepository.findByRuleId(id);
    		int srcAmtIndx=-1,srcDtIndx=-1,trgtAmtIndx=-1,trgtDtIndx=-1;
    		// ******** find index and colId of amount column and date columns ********
    		for(RuleConditions condition:ruleConditions) {
    				if(srcColDatatypes.get(condition.getsColumnId()).equals("DECIMAL")&&trgtColDatatypes.get(condition.gettColumnId()).equals("DECIMAL")) {
    				srcAmtIndx=srcColsList.indexOf(condition.getsColumnId());
    				trgtAmtIndx=trgtColsList.indexOf(condition.gettColumnId());
    				amtRuleCondition=condition;
    			}
    			else if(srcColDatatypes.get(condition.getsColumnId()).equals("DATE")&&trgtColDatatypes.get(condition.gettColumnId()).equals("DATE")) {
    				srcDtIndx=srcColsList.indexOf(condition.getsColumnId());
    				trgtDtIndx=trgtColsList.indexOf(condition.gettColumnId());
    				dtRuleCondition=condition;
    			}
    		}
    		// ******** END of find index of amount column and date columns ********
    		log.info("srcDtIndx :"+srcDtIndx+" trgtDtIndx :"+trgtDtIndx+" srcAmtIndx :"+srcAmtIndx+" trgtAmtIndx:"+trgtAmtIndx);
    		if(srcAmtIndx<0||trgtAmtIndx<0) {
    			log.info("********************* Amount columns indexes not found ******************");
    			break;
    		}
    		log.info("**************************** ONE TO MANY MATCHES *******************************************");
    		if((srcDtIndx<0||trgtDtIndx<0)&&(srcAmtIndx>=0&&trgtAmtIndx>=0)) {
    			
    		}else if((srcDtIndx>=0&&trgtDtIndx>=0)&&(srcAmtIndx>=0&&trgtAmtIndx>=0)) {
    			Boolean matchFlag=false;
    			for(int i=0;i<srcList.size();i++) {	// For each source row
    				int srcLstInd=srcList.get(i).size()-1;
    				matchFlag=false;
//    				if(cmpltdSrcList.contains(srcList.get(i).get(srcLstInd)))
//    					continue;
    	    		if(srcList.get(i).get(srcAmtIndx)!=null&&srcList.get(i).get(srcAmtIndx).trim().length()>0) {
    	        		BigDecimal srcAmt = new BigDecimal(srcList.get(i).get(srcAmtIndx));
    	        		BigDecimal tempAmt = new BigDecimal(0);
    	        		int trgtStrtInd=0;
    	        		ArrayList<Integer> tmptList=new ArrayList<Integer>();
//						Calculating date ranges by considering tolerance
    	        		Date srcDtRangeFrom=addOrSubtractDays(srcList.get(i).get(srcDtIndx),dtRuleCondition.getsToleranceOperatorFrom(),dtRuleCondition.getsToleranceValueFrom(),sdf,cal);
						Date srcDtRangeTo=addOrSubtractDays(srcList.get(i).get(srcDtIndx),dtRuleCondition.getsToleranceOperatorTo(),dtRuleCondition.getsToleranceValueTo(),sdf,cal);
    	    			for(int j=0;j<trgtList.size();j++) {	// For each target row
    	    				int trgtLstInd=trgtList.get(j).size()-1;
//    	    				if(cmpltdTrgtList.contains(trgtList.get(j).get(trgtLstInd)))
//    	    					continue;
    	    				if(trgtList.get(j).get(trgtAmtIndx)!=null&&trgtList.get(j).get(trgtAmtIndx).trim().length()>0) {
    	    					BigDecimal trgtAmt = new BigDecimal(trgtList.get(j).get(trgtAmtIndx));
    	    					if(srcAmt.compareTo(trgtAmt)>0&&(tempAmt.add(trgtAmt).compareTo(srcAmt)<=0)) {								// Both amounts are matched exactly
//    	    						log.info(">>>>>>>Source Row: "+(i+1)+" and Target Row: "+(j+1)+" are matched with Source amount :"+srcAmt+" Target amount :"+trgtAmt);
//    	    						Calculating date ranges by considering tolerance
    	    						Date trgtDtRangeFrom=addOrSubtractDays(trgtList.get(j).get(trgtDtIndx),dtRuleCondition.gettToleranceOperatorFrom(),dtRuleCondition.gettToleranceValueFrom(),sdf,cal);
    	    						Date trgtDtRangeTo=addOrSubtractDays(trgtList.get(j).get(trgtDtIndx),dtRuleCondition.gettToleranceOperatorTo(),dtRuleCondition.gettToleranceValueTo(),sdf,cal);
//    	    						Date ranges comparision
    	    						if((!srcDtRangeFrom.after(trgtDtRangeFrom) && !srcDtRangeFrom.before(trgtDtRangeTo))||(!srcDtRangeTo.after(trgtDtRangeFrom) && !srcDtRangeTo.before(trgtDtRangeTo))
    	    								||(!trgtDtRangeFrom.after(srcDtRangeFrom) && !trgtDtRangeFrom.before(srcDtRangeTo))||(!trgtDtRangeTo.after(srcDtRangeFrom) && !trgtDtRangeTo.before(srcDtRangeTo))) {
//    	    							log.info(">>>>>>>>>>>>>>Source Row: "+(i+1)+" and Target Row: "+(j+1)+" are matched with Dates also :"+trgtAmt);
    	    							int succededCnt=2;
    	    							for(RuleConditions eachCondition:ruleConditions) {
    	    								if(eachCondition!=dtRuleCondition&&eachCondition!=amtRuleCondition) {
    	    									int srcInd=srcColsList.indexOf(eachCondition.getsColumnId()), trgtInd=trgtColsList.indexOf(eachCondition.getsColumnId());
    	    									switch(srcColDatatypes.get(eachCondition.getsColumnId())) {
	    	    									case "INTEGER":
	    	    										switch(eachCondition.getOperator()) {
		    	    										case "=":
		    	    											if(Integer.parseInt(srcList.get(i).get(srcInd))==Integer.parseInt(trgtList.get(j).get(trgtInd)))
		    	    												succededCnt++;
		    	    											break;
		    	    										case "<":
		    	    											if(Integer.parseInt(srcList.get(i).get(srcInd))<Integer.parseInt(trgtList.get(j).get(trgtInd)))
		    	    												succededCnt++;
		    	    											break;
		    	    										case ">":
		    	    											if(Integer.parseInt(srcList.get(i).get(srcInd))>Integer.parseInt(trgtList.get(j).get(trgtInd)))
		    	    												succededCnt++;
		    	    											break;
		    	    										case "<=":
		    	    											if(Integer.parseInt(srcList.get(i).get(srcInd))<=Integer.parseInt(trgtList.get(j).get(trgtInd)))
		    	    												succededCnt++;
		    	    											break;
		    	    										case ">=":
		    	    											if(Integer.parseInt(srcList.get(i).get(srcInd))>=Integer.parseInt(trgtList.get(j).get(trgtInd)))
		    	    												succededCnt++;
		    	    											break;
		    	    										case "!=":
		    	    											if(Integer.parseInt(srcList.get(i).get(srcInd))!=Integer.parseInt(trgtList.get(j).get(trgtInd)))
		    	    												succededCnt++;
		    	    											break;
		    	    									}
	    	    										break;
	    	    									case "VARCHAR":
	    	    										if(varcharValidation(eachCondition.getOperator(),srcList.get(i).get(srcColsList.indexOf(eachCondition.getsColumnId())),trgtList.get(i).get(trgtColsList.indexOf(eachCondition.gettColumnId())))) {
	    	    											succededCnt++;
	    	    										}
	    	    										break;
	    	    									case "DATE":
	    	    										break;
	    	    									case "DATETIME":
	    	    										break;
	    	    									case "BOOLEAN":
	    	    										break;
	    	    									case "DECIMAL":
	    	    										if(decimalComparision(eachCondition.getOperator(),new BigDecimal(srcList.get(i).get(srcInd)),new BigDecimal(trgtList.get(j).get(trgtInd)))) {
	    	    											succededCnt++;
	    	    										}
	    	    										break;
    	    									}
    	    								}
    	    							}
    	    							if(succededCnt==ruleConditions.size()) {
//    	    								log.info("Inside match with multiple values src amt :"+srcAmt +" And trgt partial amt :"+trgtAmt + "Temp Amount :"+tempAmt);
    	    								tempAmt=tempAmt.add(trgtAmt);
    	    								tmptList.add(j);
    	    								if(tempAmt.compareTo(new BigDecimal(0))==0) {
    	    									trgtStrtInd=j;
    	    								}
//    	    								log.info("After adding amounts src amt :"+srcAmt +" And trgt partial amt :"+trgtAmt + "Temp Amount :"+tempAmt);
    	    								if(srcAmt.compareTo(tempAmt)==0) {
//    	    									log.info("*************FINALLY************ Matched src amt :"+srcAmt +" And sum of multiple rows amount :"+tempAmt);
//    	    									log.info("Participated source rows to build matching amount are :"+tmptList);
    	    									ArrayList<String> tmpList=srcList.get(i);
    	    									tmpList.add("Source Row");
    	    									cmpltdSrcList1.add(srcList.get(i).get(srcLstInd));
    	    									cmpltdSrcList.add(srcList.get(i).get(srcLstInd));
    	    									String tmpstr="";
    	    									Collections.sort(tmptList, Collections.reverseOrder());
    	    									for(Integer ind:tmptList) {
    	        									cmpltdTrgtList1.add(trgtList.get(ind).get(trgtLstInd));
    	        									cmpltdTrgtList.add(trgtList.get(ind).get(trgtLstInd));
    	        									tmpstr=tmpstr+","+trgtList.get(ind).get(trgtLstInd);
    	        									ArrayList<String> tmpList1=trgtList.get(ind);
    	        									tmpList1.add("Target Row");
    	        									tmpList1.add(srcList.get(i).get(srcLstInd));
    	        									matchedList1.add(tmpList1);
    	        									log.info("One To Many>>>>>>:  "+tmpList1);
    	        									trgtList.remove(ind);
    	        								}
    	    									tmpList.add(tmpstr.substring(1));
    	    									matchedList1.add(tmpList);
    	    									log.info("One To Many>>>>>>:  "+tmpList);
    	    									srcList.remove(i);
    	    									i--;
    	    									matchFlag=true;
    	    									break;
    	    								}
    	    							}
    	    						}else {
//    	    							log.info("XXXXXXXXXXX Source Row: "+(i+1)+" and Target Row: "+(j+1)+" are not matched with Dates");
    	    						}
    	    					}
    	    				}
    	    				if(!matchFlag && j==trgtList.size()-1) {
    	    	    			trgtStrtInd++;
    							j=trgtStrtInd;
    							tempAmt=new BigDecimal(0);
//    							log.info("XXXXXXXXXXXXXXXXXXXXX---Updated target loop index to :"+j);
    	    	    		}
    	    	    	}
    	    		}
    	    	}
    		}
    	}
    	
//    	log.info("**************************** ONE TO MANY MATCHES *******************************************");
//    	for(ArrayList<String> eachList: matchedList1) {
//    		log.info("One To Many>>>>>>:  "+eachList);
//    	}
    	log.info("**************************** END OF ONE TO MANY MATCHES *******************************************");
    	
    	log.info("One to Many Matches Source :"+cmpltdSrcList1);
    	log.info("One to Many Matches Target :"+cmpltdTrgtList1);
    	log.info("One to Many Matches Source Count :"+cmpltdSrcList1.size());
    	log.info("One to Many Matches Target Count :"+cmpltdTrgtList1.size());
    	return "Success";
    }
	
	
	@GetMapping("/_aPatternMatchingWithRules")
    @Timed
    public String _aPatternMatchingWithRules(@RequestParam String sourcePath,@RequestParam String targetPath) throws IOException, URISyntaxException, org.json.simple.parser.ParseException, InvalidFormatException, ParseException
    {
    	ArrayList<String> srcList = new ArrayList<String>();
    	ArrayList<String> trgtList = new ArrayList<String>();
    	ArrayList<String> matchedList = new ArrayList<String>();
    	ArrayList<String> matchedList1 = new ArrayList<String>();
    	srcList=readExcelFileAsStringList(sourcePath);
    	trgtList=readExcelFileAsStringList(targetPath);
    	log.info("srcList size :"+srcList.size());
    	log.info("trgtList size :"+trgtList.size());
    	Long srcDvId=208l, trgtDvId=209l;
    	ArrayList<Long> oneToOneIds=new ArrayList<Long>();
    	oneToOneIds.add(584l);
    	ArrayList<Long> oneToManyIds=new ArrayList<Long>();
    	oneToManyIds.add(585l);
//    	ArrayList<Long> oneToManyIds=(ArrayList<Long>) Arrays.asList(585l,588l,589l);
//    	ArrayList<Long> manyToOneIds=(ArrayList<Long>) Arrays.asList(586l);
//    	ArrayList<Long> manyTomanyIds=(ArrayList<Long>) Arrays.asList(587l);
    	
    	List<Long> srcColsList=new ArrayList<Long>();
    	List<Long> trgtColsList=new ArrayList<Long>();
    	List<DataViewsColumns> srcDataViewsColumns=dataViewsColumnsRepository.findByDataViewId(208l);
    	HashMap<Long,String> srcColDatatypes=new HashMap<Long,String>();
    	HashMap<Long,String> trgtColDatatypes=new HashMap<Long,String>();
    	for(DataViewsColumns col:srcDataViewsColumns) {
    		srcColsList.add(col.getId());
    		srcColDatatypes.put(col.getId(), col.getColDataType());
    	}
    	List<DataViewsColumns> trgtDataViewsColumns=dataViewsColumnsRepository.findByDataViewId(209l);
    	for(DataViewsColumns col:trgtDataViewsColumns) {
    		trgtColsList.add(col.getId());
    		trgtColDatatypes.put(col.getId(), col.getColDataType());
    	}
    	ArrayList<String> cmpltdSrcList=new ArrayList<String>(),cmpltdTrgtList=new ArrayList<String>(),
    			cmpltdSrcList1=new ArrayList<String>(),cmpltdTrgtList1=new ArrayList<String>();
    	SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
		Calendar cal = Calendar.getInstance();
		RuleConditions dtRuleCondition=null,amtRuleCondition=null;
    	for(Long id:oneToOneIds) {
    		List<RuleConditions> ruleConditions=ruleConditionsRepository.findByRuleId(id);
    		int srcAmtIndx=-1,srcDtIndx=-1,trgtAmtIndx=-1,trgtDtIndx=-1;
    		// ******** find index and colId of amount column and date columns ********
    		for(RuleConditions condition:ruleConditions) {
    				if(srcColDatatypes.get(condition.getsColumnId()).equals("DECIMAL")&&trgtColDatatypes.get(condition.gettColumnId()).equals("DECIMAL")) {
    				srcAmtIndx=srcColsList.indexOf(condition.getsColumnId());
    				trgtAmtIndx=trgtColsList.indexOf(condition.gettColumnId());
    				amtRuleCondition=condition;
    			}
    			else if(srcColDatatypes.get(condition.getsColumnId()).equals("DATE")&&trgtColDatatypes.get(condition.gettColumnId()).equals("DATE")) {
    				srcDtIndx=srcColsList.indexOf(condition.getsColumnId());
    				trgtDtIndx=trgtColsList.indexOf(condition.gettColumnId());
    				dtRuleCondition=condition;
    			}
    		}
    		// ******** END of find index of amount column and date columns ********
    		log.info("srcDtIndx :"+srcDtIndx+" trgtDtIndx :"+trgtDtIndx+" srcAmtIndx :"+srcAmtIndx+" trgtAmtIndx:"+trgtAmtIndx);
    		if(srcAmtIndx<0||trgtAmtIndx<0) {
    			log.info("********************* Amount columns indexes not found ******************");
    			break;
    		}
    		log.info("**************************** ONE TO ONE MATCHES *******************************************");
    		if((srcDtIndx<0||trgtDtIndx<0)&&(srcAmtIndx>=0&&trgtAmtIndx>=0)) {
    			
    		}else if((srcDtIndx>=0&&trgtDtIndx>=0)&&(srcAmtIndx>=0&&trgtAmtIndx>=0)) {
    			for(int i=0;i<srcList.size();i++) {	// For each source row
	    			List<String> srcIndList=new ArrayList<String>();
	    			for(String s:(srcList.get(i)).split("\\|")) {
	    				srcIndList.add(s);
	    			}    				
    				int srcLstInd=srcIndList.size()-1;
    				if(cmpltdSrcList.contains(srcIndList.get(srcLstInd)))
    					continue;
    	    		if(srcIndList.get(srcAmtIndx)!=null&&srcIndList.get(srcAmtIndx).trim().length()>0) {
    	        		BigDecimal srcAmt = new BigDecimal(srcIndList.get(srcAmtIndx));
//						Calculating date ranges by considering tolerance
    	        		Date srcDtRangeFrom=addOrSubtractDays(srcIndList.get(srcDtIndx),dtRuleCondition.getsToleranceOperatorFrom(),dtRuleCondition.getsToleranceValueFrom(),sdf,cal);
						Date srcDtRangeTo=addOrSubtractDays(srcIndList.get(srcDtIndx),dtRuleCondition.getsToleranceOperatorTo(),dtRuleCondition.getsToleranceValueTo(),sdf,cal);
    	    			for(int j=0;j<trgtList.size();j++) {	// For each target row
    	    				List<String> trgtIndList=new ArrayList<String>();
    	    				for(String s:trgtList.get(j).split("\\|")) {
    	    					trgtIndList.add(s);
    		    			} 
    	    				int trgtLstInd=trgtIndList.size()-1;
    	    				if(cmpltdTrgtList.contains(trgtIndList.get(trgtLstInd)))
    	    					continue;
    	    				if(trgtIndList.get(trgtAmtIndx)!=null&&trgtIndList.get(trgtAmtIndx).trim().length()>0) {
    	    					BigDecimal trgtAmt = new BigDecimal(trgtIndList.get(trgtAmtIndx));
    	    					if(srcAmt.compareTo(trgtAmt)==0) {								// Both amounts are matched exactly
//    	    						log.info(">>>>>>>Source Row: "+(i+1)+" and Target Row: "+(j+1)+" are matched with Source amount :"+srcAmt+" Target amount :"+trgtAmt);
//    	    						Calculating date ranges by considering tolerance
    	    						Date trgtDtRangeFrom=addOrSubtractDays(trgtIndList.get(trgtDtIndx),dtRuleCondition.gettToleranceOperatorFrom(),dtRuleCondition.gettToleranceValueFrom(),sdf,cal);
    	    						Date trgtDtRangeTo=addOrSubtractDays(trgtIndList.get(trgtDtIndx),dtRuleCondition.gettToleranceOperatorTo(),dtRuleCondition.gettToleranceValueTo(),sdf,cal);
//    	    						Date ranges comparision
    	    						if((!srcDtRangeFrom.after(trgtDtRangeFrom) && !srcDtRangeFrom.before(trgtDtRangeTo))||(!srcDtRangeTo.after(trgtDtRangeFrom) && !srcDtRangeTo.before(trgtDtRangeTo))
    	    								||(!trgtDtRangeFrom.after(srcDtRangeFrom) && !trgtDtRangeFrom.before(srcDtRangeTo))||(!trgtDtRangeTo.after(srcDtRangeFrom) && !trgtDtRangeTo.before(srcDtRangeTo))) {
//    	    							log.info(">>>>>>>>>>>>>>Source Row: "+(i+1)+" and Target Row: "+(j+1)+" are matched with Dates also :"+trgtAmt);
    	    							int succededCnt=2;
    	    							for(RuleConditions eachCondition:ruleConditions) {
    	    								if(eachCondition!=dtRuleCondition&&eachCondition!=amtRuleCondition) {
    	    									int srcInd=srcColsList.indexOf(eachCondition.getsColumnId()), trgtInd=trgtColsList.indexOf(eachCondition.getsColumnId());
    	    									switch(srcColDatatypes.get(eachCondition.getsColumnId())) {
	    	    									case "INTEGER":
	    	    										switch(eachCondition.getOperator()) {
		    	    										case "=":
		    	    											if(Integer.parseInt(srcIndList.get(srcInd))==Integer.parseInt(trgtIndList.get(trgtInd)))
		    	    												succededCnt++;
		    	    											break;
		    	    										case "<":
		    	    											if(Integer.parseInt(srcIndList.get(srcInd))<Integer.parseInt(trgtIndList.get(trgtInd)))
		    	    												succededCnt++;
		    	    											break;
		    	    										case ">":
		    	    											if(Integer.parseInt(srcIndList.get(srcInd))>Integer.parseInt(trgtIndList.get(trgtInd)))
		    	    												succededCnt++;
		    	    											break;
		    	    										case "<=":
		    	    											if(Integer.parseInt(srcIndList.get(srcInd))<=Integer.parseInt(trgtIndList.get(trgtInd)))
		    	    												succededCnt++;
		    	    											break;
		    	    										case ">=":
		    	    											if(Integer.parseInt(srcIndList.get(srcInd))>=Integer.parseInt(trgtIndList.get(trgtInd)))
		    	    												succededCnt++;
		    	    											break;
		    	    										case "!=":
		    	    											if(Integer.parseInt(srcIndList.get(srcInd))!=Integer.parseInt(trgtIndList.get(trgtInd)))
		    	    												succededCnt++;
		    	    											break;
		    	    									}
	    	    										break;
	    	    									case "VARCHAR":
	    	    										if(varcharValidation(eachCondition.getOperator(),srcIndList.get(srcColsList.indexOf(eachCondition.getsColumnId())),trgtIndList.get(trgtColsList.indexOf(eachCondition.gettColumnId())))) {
	    	    											succededCnt++;
	    	    										}
	    	    										break;
	    	    									case "DATE":
	    	    										break;
	    	    									case "DATETIME":
	    	    										break;
	    	    									case "BOOLEAN":
	    	    										break;
	    	    									case "DECIMAL":
//	    	    										new BigDecimal(trgtList.get(j).get(trgtAmtIndx))
	    	    										if(decimalComparision(eachCondition.getOperator(),new BigDecimal(srcIndList.get(srcInd)),new BigDecimal(trgtIndList.get(trgtInd)))) {
	    	    											succededCnt++;
	    	    										}
	    	    										break;
    	    									}
    	    								}
    	    							}
    	    							if(succededCnt==ruleConditions.size()) {
//    	    								log.info("********FINALLY********Source Row: "+(i+1)+" and Target Row: "+(j+1)+" are matched with all conditions");
    	    								srcIndList.add("Source Row");
    	    								srcIndList.add(trgtIndList.get(trgtLstInd));
    	    								matchedList.add(String.join("|", srcIndList));
//    	    								log.info("One To One>>>>>>:  "+srcIndList);
    	    								trgtIndList.add("Target Row");
    	    								trgtIndList.add(srcIndList.get(srcLstInd));
    	    								matchedList.add(String.join("|", trgtIndList));
//    	    								log.info("One To One>>>>>>:  "+trgtIndList);
    	    								cmpltdSrcList.add(srcIndList.get(srcLstInd));
    	    								srcList.remove(i);
    	    								i--;
    	    								cmpltdTrgtList.add(trgtIndList.get(trgtLstInd));
    	    								trgtList.remove(j);
    	    								j--;
    	    								break;
    	    							}
    	    						}else {
//    	    							log.info("XXXXXXXXXXX Source Row: "+(i+1)+" and Target Row: "+(j+1)+" are not matched with Dates");
    	    						}
    	    					}
    	    				}
    	    	    	}
    	    		}
    	    	}
    		}
    	}
    	log.info("One to One Matches Source :"+cmpltdSrcList);
    	log.info("One to One Matches Target :"+cmpltdTrgtList);
    	log.info("One to One Matches Source Count :"+cmpltdSrcList.size());
    	log.info("One to One Matches Target Count :"+cmpltdTrgtList.size());
//    	log.info("**************************** ONE TO ONE MATCHES *******************************************");
//    	for(ArrayList<String> eachList: matchedList) {
//    		log.info("One To One>>>>>>:  "+eachList);
//    	}
    	log.info("**************************** END OF ONE TO ONE MATCHES *******************************************");
    	
    	dtRuleCondition=null;
    	amtRuleCondition=null;
    	List<RuleConditions> varCharRuleConditions=new ArrayList<RuleConditions>();
    	for(Long id:oneToManyIds) {
    		List<RuleConditions> ruleConditions=ruleConditionsRepository.findByRuleId(id);
    		int srcAmtIndx=-1,srcDtIndx=-1,trgtAmtIndx=-1,trgtDtIndx=-1;
    		// ******** find index and colId of amount column and date columns ********
			for (RuleConditions condition : ruleConditions) {
				if (srcColDatatypes.get(condition.getsColumnId()).equals("DECIMAL")
						&& trgtColDatatypes.get(condition.gettColumnId()).equals("DECIMAL")) {
					srcAmtIndx = srcColsList.indexOf(condition.getsColumnId());
					trgtAmtIndx = trgtColsList.indexOf(condition.gettColumnId());
					amtRuleCondition = condition;
				} else if (srcColDatatypes.get(condition.getsColumnId()).equals("DATE")
						&& trgtColDatatypes.get(condition.gettColumnId()).equals("DATE")) {
					srcDtIndx = srcColsList.indexOf(condition.getsColumnId());
					trgtDtIndx = trgtColsList.indexOf(condition.gettColumnId());
					dtRuleCondition = condition;
				}else if(srcColDatatypes.get(condition.getsColumnId()).equals("VARCHAR")
						&& trgtColDatatypes.get(condition.gettColumnId()).equals("VARCHAR")) {
					varCharRuleConditions.add(condition);
				}
			}
    		// ******** END of find index of amount column and date columns ********
    		log.info("srcDtIndx :"+srcDtIndx+" trgtDtIndx :"+trgtDtIndx+" srcAmtIndx :"+srcAmtIndx+" trgtAmtIndx:"+trgtAmtIndx);
    		if(srcAmtIndx<0||trgtAmtIndx<0) {
    			log.info("********************* Amount columns indexes not found ******************");
    			break;
    		}
    		log.info("**************************** ONE TO MANY MATCHES *******************************************");
    		if((srcDtIndx<0||trgtDtIndx<0)&&(srcAmtIndx>=0&&trgtAmtIndx>=0)) {
    			
    		}else if((srcDtIndx>=0&&trgtDtIndx>=0)&&(srcAmtIndx>=0&&trgtAmtIndx>=0)) {
    			Boolean matchFlag=false;
    			for(int i=0;i<srcList.size();i++) {	// For each source row
    				List<String> tmpList=new ArrayList<String>();
    				for(String s:srcList.get(i).split("\\|")) {
    					tmpList.add(s);
	    			}
    				String srcRegex="^",trgtRegex="^";
    				for (RuleConditions condition : varCharRuleConditions) {
    					for(String tmp: tmpList.get(srcColsList.indexOf(condition.getsColumnId())).toUpperCase().split("[^A-Z^0-9]")) {
    						srcRegex=srcRegex+"(?=.*"+tmp.toUpperCase()+".*)";
    					}
    				}
    				srcRegex=srcRegex+".*$";
    				log.info("srcRegex >>>>>>>>>>:"+srcRegex);
    				Pattern pattern = Pattern.compile(srcRegex);
    				List<String> srcMatchingList = srcList.stream()
    				        .filter(pattern.asPredicate())
    				        .collect(Collectors.toList());
    				log.info("srcMatchingList :"+srcMatchingList.size());
    				List<String> trgtMatchingList = trgtList.stream()
    				        .filter(pattern.asPredicate())
    				        .collect(Collectors.toList());
    				log.info("trgtMatchingList :"+trgtMatchingList.size());
    				int srcLstInd = 0;
    				ArrayList<String> tmptList = new ArrayList<String>(),tmptListSrc=new ArrayList<String>();
					for (int j = 0; j < srcMatchingList.size(); j++) {
						List<String> srcIndList =new ArrayList<String>();
						for(String s:srcMatchingList.get(j).split("\\|")) {
							srcIndList.add(s);
		    			}
						srcLstInd = srcIndList.size() - 1;
						matchFlag = false;
						if (srcIndList.get(srcAmtIndx) != null && srcIndList.get(srcAmtIndx).trim().length() > 0) {
							BigDecimal srcAmt = new BigDecimal(srcIndList.get(srcAmtIndx));
							BigDecimal tempAmt = new BigDecimal(0);
							int trgtStrtInd = 0;
							// Calculating date ranges by considering tolerance
							Date srcDtRangeFrom = addOrSubtractDays(srcIndList.get(srcDtIndx),
									dtRuleCondition.getsToleranceOperatorFrom(),
									dtRuleCondition.getsToleranceValueFrom(), sdf, cal);
							Date srcDtRangeTo = addOrSubtractDays(srcIndList.get(srcDtIndx),
									dtRuleCondition.getsToleranceOperatorTo(), dtRuleCondition.getsToleranceValueTo(),
									sdf, cal);
							
							int trgtLstInd = 0;
							tmptList = new ArrayList<String>();
							for (int k = 0; k < trgtMatchingList.size(); k++) { // For each target row
								ArrayList<Integer> tmptListIndx = new ArrayList<Integer>();
								List<String> trgtIndList =new ArrayList<String>();
								for(String s:trgtMatchingList.get(k).split("\\|")) {
									trgtIndList.add(s);
				    			}
								trgtLstInd=trgtIndList.size() - 1;
								if (trgtIndList.get(trgtAmtIndx) != null
										&& trgtIndList.get(trgtAmtIndx).trim().length() > 0) {
									BigDecimal trgtAmt = new BigDecimal(trgtIndList.get(trgtAmtIndx));
									if (tempAmt.add(trgtAmt).compareTo(srcAmt) <= 0) { // Both amounts are matched exactly
										Date trgtDtRangeFrom = addOrSubtractDays(trgtIndList.get(trgtDtIndx),
												dtRuleCondition.gettToleranceOperatorFrom(),
												dtRuleCondition.gettToleranceValueFrom(), sdf, cal);
										Date trgtDtRangeTo = addOrSubtractDays(trgtIndList.get(trgtDtIndx),
												dtRuleCondition.gettToleranceOperatorTo(),
												dtRuleCondition.gettToleranceValueTo(), sdf, cal);
										// Date ranges comparision
										if ((!srcDtRangeFrom.after(trgtDtRangeFrom)
												&& !srcDtRangeFrom.before(trgtDtRangeTo))
												|| (!srcDtRangeTo.after(trgtDtRangeFrom)
														&& !srcDtRangeTo.before(trgtDtRangeTo))
												|| (!trgtDtRangeFrom.after(srcDtRangeFrom)
														&& !trgtDtRangeFrom.before(srcDtRangeTo))
												|| (!trgtDtRangeTo.after(srcDtRangeFrom)
														&& !trgtDtRangeTo.before(srcDtRangeTo))) {
											log.info("Dates matched Source :"+srcAmt+" Target :"+trgtAmt+" tempAmt :"+tempAmt);
											int succededCnt = 2;
											for (RuleConditions eachCondition : ruleConditions) {
												if (eachCondition != dtRuleCondition
														&& eachCondition != amtRuleCondition) {
													int srcInd = srcColsList.indexOf(eachCondition.getsColumnId()),
															trgtInd = trgtColsList
																	.indexOf(eachCondition.getsColumnId());
													switch (srcColDatatypes.get(eachCondition.getsColumnId())) {
													case "INTEGER":
														switch (eachCondition.getOperator()) {
														case "=":
															if (Integer.parseInt(srcIndList.get(srcInd)) == Integer
																	.parseInt(trgtIndList.get(trgtInd)))
																succededCnt++;
															break;
														case "<":
															if (Integer.parseInt(srcIndList.get(srcInd)) < Integer
																	.parseInt(trgtIndList.get(trgtInd)))
																succededCnt++;
															break;
														case ">":
															if (Integer.parseInt(srcIndList.get(srcInd)) > Integer
																	.parseInt(trgtIndList.get(trgtInd)))
																succededCnt++;
															break;
														case "<=":
															if (Integer.parseInt(srcIndList.get(srcInd)) <= Integer
																	.parseInt(trgtIndList.get(trgtInd)))
																succededCnt++;
															break;
														case ">=":
															if (Integer.parseInt(srcIndList.get(srcInd)) >= Integer
																	.parseInt(trgtIndList.get(trgtInd)))
																succededCnt++;
															break;
														case "!=":
															if (Integer.parseInt(srcIndList.get(srcInd)) != Integer
																	.parseInt(trgtIndList.get(trgtInd)))
																succededCnt++;
															break;
														}
														break;
													case "VARCHAR":
														if (varcharValidation(eachCondition.getOperator(),
																srcIndList.get(srcColsList
																		.indexOf(eachCondition.getsColumnId())),
																trgtIndList.get(trgtColsList
																		.indexOf(eachCondition.gettColumnId())))) {
															succededCnt++;
														}
														break;
													case "DATE":
														break;
													case "DATETIME":
														break;
													case "BOOLEAN":
														break;
													case "DECIMAL":
														if (decimalComparision(eachCondition.getOperator(),
																new BigDecimal(srcIndList.get(srcInd)),
																new BigDecimal(trgtIndList.get(trgtInd)))) {
															succededCnt++;
														}
														break;
													}
												}
											}
											if(succededCnt==ruleConditions.size()) {
	    	    								tempAmt=tempAmt.add(trgtAmt);
												log.info("*****conditions also matched SrcAmt :"+srcAmt+" tempAmt :"+tempAmt);
	    	    								tmptList.add(trgtIndList.get(trgtLstInd));
	    	    								tmptListIndx.add(k);
	    	    								if(tempAmt.compareTo(new BigDecimal(0))==0) {
	    	    									trgtStrtInd=Integer.parseInt(trgtIndList.get(trgtLstInd));
	    	    								}
	    	    								if(srcAmt.compareTo(tempAmt)==0) {
	    	    									log.info("*****FINALLY Amounts are also matched SrcAmt :"+srcAmt+" tempAmt :"+tempAmt);
	    	    									srcIndList.add("Source Row");
	    	    									cmpltdSrcList1.add(srcIndList.get(srcLstInd));
	    	    									cmpltdSrcList.add(srcIndList.get(srcLstInd));
	    	    									String tmpstr="";
	    	    									Collections.sort(tmptListIndx, Collections.reverseOrder());
	    	    									for(Integer ind:tmptListIndx) {
	    	        									cmpltdTrgtList1.add(trgtMatchingList.get(ind).split("\\|")[trgtLstInd]);
	    	        									cmpltdTrgtList.add(trgtMatchingList.get(ind).split("\\|")[trgtLstInd]);
	    	        									tmpstr=tmpstr+","+trgtMatchingList.get(ind).split("\\|")[trgtLstInd];
	    	        									List<String> tmpList1=new ArrayList<String>();
	    	        									for(String s:trgtMatchingList.get(ind).split("\\|")) {
	    	        										tmpList1.add(s);
	    	        									}
	    	        									tmpList1.add("Target Row");
	    	        									tmpList1.add(srcIndList.get(srcLstInd));
	    	        									matchedList1.add(String.join("|", tmpList1));
	    	        									log.info("One To Many>>>>>>:  "+tmpList1);
//	    	        									trgtList.remove(ind);
	    	        								}
	    	    									srcIndList.add(tmpstr.substring(1));
	    	    									matchedList1.add(String.join("|", srcIndList));
	    	    									log.info("One To Many>>>>>>:  "+srcIndList);
	    	    									tmptListSrc.add(srcIndList.get(srcLstInd));
//	    	    									srcList.remove(Integer.parseInt(srcIndList.get(srcLstInd)));
	    	    									matchFlag=true;
	    	    									for(Integer ind:tmptListIndx) {
	    	    										trgtMatchingList.remove(ind);
	    	    									}
	    	    									break;
	    	    								}
	    	    							}
										}
									}
								}
								if(!matchFlag && k==trgtMatchingList.size()-1) {
	    	    	    			trgtStrtInd++;
	    							k=trgtStrtInd;
	    							tempAmt=new BigDecimal(0);
	    							tmptList.clear();
	    							tmptListSrc.clear();
	    	    	    		}
							}
							for(int s=0;s<trgtList.size();s++) {
								if(tmptList.contains(trgtList.get(s).split("\\|")[trgtLstInd])) {
									trgtList.remove(s);
									s--;
								}
							}
						}
					}
					for(int s=0;s<srcList.size();s++) {
						if(tmptListSrc.contains(srcList.get(s).split("\\|")[srcLstInd])) {
							srcList.remove(s);
							s--;
						}
					}
    	    	}
    		}
    	}
    	
    	log.info("**************************** END OF ONE TO MANY MATCHES *******************************************");
    	log.info("One to Many Matches Source :"+cmpltdSrcList1);
    	log.info("One to Many Matches Target :"+cmpltdTrgtList1);
    	log.info("One to Many Matches Source Count :"+cmpltdSrcList1.size());
    	log.info("One to Many Matches Target Count :"+cmpltdTrgtList1.size());
    	
    	return "Success";
    }
	
	private Boolean filterListByCondition(String cmpType, String srcVal, String trgtVal) {
		String regex1="^";
		for(String tmp: srcVal.trim().toUpperCase().split("[^A-Z^0-9]")) {
			regex1=regex1+"(?=.*"+tmp.toUpperCase()+".*)";
		}
		regex1=regex1+".*$";
		String regex2="^";
		for(String tmp: trgtVal.trim().toUpperCase().split("[^A-Z^0-9]")) {
			regex2=regex2+"(?=.*"+tmp.toUpperCase()+".*)";
		}
		regex2=regex2+".*$";
		if(cmpType.equalsIgnoreCase("NOT_EQUALS")) {
			if(!srcVal.toUpperCase().matches(regex1)&&trgtVal.toUpperCase().matches(regex2)) {
				return true;
			}
		}else {
			if(srcVal.toUpperCase().matches(regex1)&&trgtVal.toUpperCase().matches(regex2)) {
				return true;
			}
		}
		return false;
	}
	
	private Boolean varcharValidation(String cmpType, String srcVal, String trgtVal) {
		String regex1="^";
		for(String tmp: srcVal.trim().split("[^A-Z^0-9]")) {
			regex1=regex1+"(?=.*"+tmp+".*)";
		}
		regex1=regex1+".*$";
		String regex2="^";
		for(String tmp: trgtVal.trim().split("[^A-Z^0-9]")) {
			regex2=regex2+"(?=.*"+tmp+".*)";
		}
		regex2=regex2+".*$";
		if(cmpType.equalsIgnoreCase("NOT_EQUALS")) {
			if(!srcVal.matches(regex1)&&trgtVal.matches(regex2)) {
				return true;
			}
		}else {
			if(srcVal.matches(regex1)&&trgtVal.matches(regex2)) {
				return true;
			}
		}
		return false;
	}
	
	private Boolean integerComparision(String cmpType, BigDecimal sAmt, BigDecimal tAmt) {
		switch(cmpType) {
			case "=":
				if(sAmt.compareTo(tAmt)==0)
					return true;
				break;
			case "<":
				if(sAmt.compareTo(tAmt)<0)
					return true;
				break;
			case ">":
				if(sAmt.compareTo(tAmt)>0)
					return true;
				break;
			case "<=":
				if(sAmt.compareTo(tAmt)<=0)
					return true;
				break;
			case ">=":
				if(sAmt.compareTo(tAmt)>=0)
					return true;
				break;
			case "!=":
				if(sAmt.compareTo(tAmt)!=0)
					return true;
				break;
//			case "BETWEEN":
//				break;
		}
		return false;
	}
	
	private Boolean decimalComparision(String cmpType, BigDecimal sAmt, BigDecimal tAmt) {
		switch(cmpType) {
			case "=":
				if(sAmt.compareTo(tAmt)==0)
					return true;
				break;
			case "<":
				if(sAmt.compareTo(tAmt)<0)
					return true;
				break;
			case ">":
				if(sAmt.compareTo(tAmt)>0)
					return true;
				break;
			case "<=":
				if(sAmt.compareTo(tAmt)<=0)
					return true;
				break;
			case ">=":
				if(sAmt.compareTo(tAmt)>=0)
					return true;
				break;
			case "!=":
				if(sAmt.compareTo(tAmt)!=0)
					return true;
				break;
		}
		return false;
	}
	
	private Date addOrSubtractDays(String sDate, String Operator, String days, SimpleDateFormat sdf, Calendar cal) throws ParseException {
		if(days==null || days.length()<1)
			return cal.getTime();
		cal.setTime(sdf.parse(sDate));
		switch(Operator){
			case "-":
				cal.add(Calendar.DATE,-Integer.parseInt(days));
				break;
			case "+":
				cal.add(Calendar.DATE,Integer.parseInt(days));
				break;
		}
        return cal.getTime();
	}
	
	@GetMapping("/downloadAsCSV")
	@Timed
	public ResponseEntity<?> buildExcelFile(HttpServletResponse response) throws InvalidFormatException, IOException {
		String jsonString = "{\"infile\": [{\"field1\": 11,\"field2\": 12,\"field3\": 13},{\"field1\": 21,\"field2\": 22,\"field3\": 23},{\"field1\": 31,\"field2\": 32,\"field3\": 33}]}";
		org.json.JSONObject output;
		XSSFWorkbook workbook=new XSSFWorkbook();
		File file=null;
		try {
			output = new org.json.JSONObject(jsonString);
			JSONArray docs = output.getJSONArray("infile");
			if(docs.length()>0) {
//				File file = new File("/home/nspl/Desktop/Test/fromJSON.csv");
//				String csv = CDL.toString(docs);
//				FileUtils.writeStringToFile(file, csv);
				//file = new File("/home/nspl/Desktop/Test/fromJSON.xlsx");
				workbook.createSheet();
				XSSFSheet sheet=workbook.getSheetAt(0);
				String[] colNames= org.json.JSONObject.getNames(docs.getJSONObject(0));
				XSSFRow colsRow=sheet.createRow(0);
				log.info("Co names: "+colNames);
				for(int i=0;i<colNames.length;i++) {
					XSSFCell newCell=colsRow.createCell(0);
					newCell.setCellValue(colNames[i]);
					log.info("Co name: "+colNames[i]);
				}
				for(int i=0;i<docs.length();i++) {
					XSSFRow newRow=sheet.createRow(i+1);
					for(int j=0;j<colNames.length;j++) {
						if(docs.getJSONObject(i).has(colNames[j])) 
							newRow.createCell(j).setCellValue(docs.getJSONObject(i).getString(colNames[j]));
						else
							newRow.createCell(j).setCellValue(XSSFCell.CELL_TYPE_BLANK);
					}
				}
				
				ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
				//FileOutputStream fos =new FileOutputStream(file);
				workbook.write(outputStream);
				//fos.flush();
				//fos.close();
				
//				Resource resource = new ClassPathResource("/home/nspl/Desktop/Test/fromJSON.xlsx");
//				  byte[] content = null;
				byte[] buffer= new byte[8192]; // use bigger if you want
				  try {
					  
					  FileInputStream in = new FileInputStream(file);
						OutputStream out = response.getOutputStream();
						
						int length = 0;

						while ((length = in.read(buffer)) > 0){
						     out.write(buffer, 0, length);
						}
						in.close();
						out.close();
						response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
					    response.setHeader("Content-Disposition", "attachment; filename=fromJSON.xlsx");
					  
					  
//				   content = FileCopyUtils.copyToByteArray(resource.getInputStream());
				   System.out.println("Converted .csv to bytes successfully.");
				  } catch (IOException e1) {
				   System.err.println("Error while converting file to bytes.");
				   e1.printStackTrace();
				  }
				 
				  HttpHeaders headers = new HttpHeaders();
				  headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
				  System.out.println("Download sample .csv request completed");
				  return new ResponseEntity<byte[]>(buffer, headers, HttpStatus.OK);
				
				
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		File xls = new File("/home/nspl/Desktop/Test/fromJSON.xlsx");
		
		return null;
	}
	
//	 /**
//     * Livy: Create session
//     * @param input
//     * @return
//     * @throws IOException
//     * @throws InvalidKeyException
//     * @throws NoSuchAlgorithmException
//     * @throws NoSuchPaddingException
//     * @throws InvalidAlgorithmParameterException
//     * @throws IllegalBlockSizeException
//     * @throws BadPaddingException
//     */
   /* @RequestMapping(value = "/interactiveSampleClient", method = RequestMethod.POST, produces = "text/csv")
	@Timed
	public String interactiveSampleClient(@RequestBody String input)throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		
		System.out.println("in interactiveSampleClient with input: "+input);
		String baseUri = "http://192.168.0.155:8998";
		try {
			client = new LivyInteractiveClient(baseUri, "username", "password");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		InteractiveJobParameters param = new InteractiveJobParameters(SessionKind.SPARK,"2G",2,1,1);
		
		try {
			System.out.println("SessionKind.SPARK " + SessionKind.SPARK);
			client.createSession(param, 1000, new SessionEventListener() {
				@Override
				public boolean updateStatus(com.nspl.livy.Session event) {
					System.out.println("Session Updated: " + event.getAppId());
					System.out.println("Session Updated: " + event.getAppInfo());
					System.out.println("Session Updated: " + event.getId());
					System.out.println("Session Updated: " + event.getLog());
					System.out.println("Session Updated: " + event.getState());
					boolean ret = true;
					switch(event.getState()) {
					case com.nspl.livy.Session.IDLE:
						session_status = com.nspl.livy.Session.IDLE;
						ret = false;
						break;
					case com.nspl.livy.Session.STARTING:
						try {
							System.out.println("Session is starting. Session ID: " + client.getSession().getId());
						} catch (IOException e) {
							e.printStackTrace();
						}
						session_status = com.nspl.livy.Session.STARTING;
						ret = true;
						break;
					case com.nspl.livy.Session.DEAD:
						session_status = com.nspl.livy.Session.DEAD;
						ret = false;
						break;
					case com.nspl.livy.Session.ERROR:
						session_status = com.nspl.livy.Session.ERROR;
						ret = true;
						break;
					default:
						session_status = com.nspl.livy.Session.ERROR;
						ret = false;
						break;
					}
				return ret;
				}
			});
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (LivyException e1) {
			e1.printStackTrace();
		}

		return resultResponse;
	}
	
//	*//**
//	 * Livy query submission
//	 * @param input
//	 * @return
//	 * @throws IOException
//	 * @throws InvalidKeyException
//	 * @throws NoSuchAlgorithmException
//	 * @throws NoSuchPaddingException
//	 * @throws InvalidAlgorithmParameterException
//	 * @throws IllegalBlockSizeException
//	 * @throws BadPaddingException
//	 *//*
	@RequestMapping(value = "/SubmitStatement", method = RequestMethod.POST, produces = "text/csv")
	@Timed
	public String submitStatement(@RequestBody String input)throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		
		
		String statement = checkStatement(input);
		System.out.println("Your input is [" + statement + "]");
		
		try {
			client.submitStatement(statement, 1000, new StatementResultListener() {
				@Override
				public void update(StatementResult result) {
					resultResponse = result.getOutput();
					System.out.println("Update Received. " + result.getOutput());
					//System.out.println("Ready for your input ....");
					//System.out.print(">");
				}
			});
		} catch (LivyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return resultResponse;
		
	}
	
	private String checkStatement(String statement) {
		StringBuilder sb = new StringBuilder();
		int start = 0, end;
		String buf = statement;
		
		while(buf.length() > 0) {
			end = buf.indexOf("\"");
			if(end < 0) {
				sb.append(buf);
				break;
			}
			sb.append(buf.substring(start, end));
			sb.append("\\");
			sb.append("\"");
			buf = buf.substring(end + 1);
			start = 0;
		}

		return sb.toString();
	}	
	
//	*//**
//	 * Author: Swetha
//	 * POC on LIvy Integration
//	 * @param tenantId
//	 * @param reportId
//	 * @param filtersMap
//	 * @param pageNumber
//	 * @param pageSize
//	 * @param response
//	 * @return
//	 * @throws AnalysisException
//	 * @throws IOException
//	 * @throws ParseException
//	 *//*
	@PostMapping("/tabularLivyPOC")
    @Timed 
    public LinkedHashMap tabularLivyPOC(@RequestParam Long tenantId, @RequestParam Long reportId,
			@RequestBody(required = false) HashMap filtersMap,  @RequestParam(required=false) Integer pageNumber, @RequestParam(required=false) Integer pageSize,HttpServletResponse response) throws AnalysisException,
			IOException, ParseException {
    	
    	log.info("Rest request to tabularLevyPOC with tenantId: "+tenantId+" reportId: "+reportId);

    	LinkedHashMap finalMap=new LinkedHashMap();
		
    	//LinkedHashMap serviceMap=new LinkedHashMap();
		LinkedHashMap serviceMap=reportsService.reportDataSetCreationLivy(tenantId, reportId, filtersMap);
    	
    	Dataset<Row> data=(Dataset<Row>) serviceMap.get("data");
    	log.info("previewing data: ");
    	List<String> layoutColList=(List<String>) serviceMap.get("layoutColList");
    	log.info("layoutColList: "+layoutColList);
    	String buckAgg=(String) serviceMap.get("buckAgg");
    	String reportTypeName=serviceMap.get("reportTypeName").toString();
    	 String reconSatus=serviceMap.get("as_of_recon_status").toString();
         String accSatus=serviceMap.get("as_of_acc_status").toString();
         log.info("reconSatus: "+reconSatus+" accSatus: "+accSatus);
         
		//Modifying layout order
        
        List<String> refTypeList=new ArrayList<String>();
        refTypeList.add("DATA_VIEW");
        refTypeList.add("FIN_FUNCTION");
        
        int finSize=0;
        finSize=layoutColList.size();
        List<String> finList=new ArrayList<String>();
     
        log.info("finSize: "+finSize);
        Column[] finColArr = new Column[finSize];
        String[] finalColList=new String[finSize];
        int p=0;
        for(p=0;p<layoutColList.size();p++){
            
            String col=layoutColList.get(p);
            Column colarryVal=new Column(col);
            finalColList[p]=col;
            finColArr[p]=colarryVal;
        }
        
        log.info("finalColList: "+finalColList);
        
        log.info("p: "+p);
        int index=0;
        
		Dataset<Row> unSortedLayoutData=data.select(finColArr);
		log.info("Previewing unSortedLayoutData dataset with sz: "+unSortedLayoutData.count());
		
		Dataset<Row> updLayoutData=unSortedLayoutData.sort(finColArr);
        
        log.info("previewing updLayoutData aftr sorting");
        //updLayoutData.show(200);
        
		List<Row> tabData = updLayoutData.collectAsList();
		int totDataCnt=0;
		if(tabData!=null && !(tabData.isEmpty())){
			totDataCnt=tabData.size();
		}
		String[] columnsList = updLayoutData.columns();
		
		int limit = 0;
		if(pageNumber == null || pageNumber == 0)
		{
			pageNumber = 0;
		}
		if(pageSize == null || pageSize == 0)
		{
			pageSize = totDataCnt;
		}
			limit = ((pageNumber+1) * pageSize + 1)-1;
		int startIndex=pageNumber*pageSize; 
		
		if(limit>tabData.size()){
			limit=tabData.size();
		}
		
		log.info("Limit Starting Values : "+ limit);
		log.info("Page Number : "+ pageNumber);
		
		List<LinkedHashMap> maps = new ArrayList<LinkedHashMap>();
		maps=reportsService.retuneTabularReportJSON(reportId,reportTypeName,tabData,startIndex,limit,finalColList );
		
		 List<LinkedHashMap> headerMap=new ArrayList<LinkedHashMap>();
		 if(maps!=null && !(maps.isEmpty()) && maps.get(0)!=null && !(maps.get(0).isEmpty())){
		 headerMap=reportsService.tabuleHeaderData(maps.get(0), reportId); 
		 log.info("headerMap: "+headerMap);
		 }

		log.info("end time: " + System.currentTimeMillis() + " curTym: "
				+ ZonedDateTime.now());

		if(headerMap==null){
			headerMap=new ArrayList<LinkedHashMap>();
		}
		finalMap.put("columns", headerMap);
		finalMap.put("data", maps);
		
		response.addIntHeader("X-COUNT", totDataCnt);
		
		return finalMap;
		
    }
	
//	*//**
//	 * Author: Swetha
//	 * POC on LIvy Integration: Dynamic data set query framing using jpa queries
//	 * @param tenantId
//	 * @param reportId
//	 * @param filtersMap
//	 * @param pageNumber
//	 * @param pageSize
//	 * @param response
//	 * @return
//	 * @throws AnalysisException
//	 * @throws IOException
//	 * @throws ParseException
//	 * @throws URISyntaxException 
//	 *//*
	@PostMapping("/tabularQueryLivyPOC")
    @Timed 
    public LinkedHashMap tabularQueryLivyPOC(@RequestParam Long tenantId, @RequestParam Long reportId,
			@RequestBody(required = false) HashMap filtersMap,  @RequestParam(required=false) Integer pageNumber, @RequestParam(required=false) Integer pageSize,HttpServletResponse response) throws AnalysisException,
			IOException, ParseException {
    	
    	log.info("Rest request to tabularLevyPOC with tenantId: "+tenantId+" reportId: "+reportId);

    	LinkedHashMap finalMap=new LinkedHashMap();
		
    	//LinkedHashMap serviceMap=new LinkedHashMap();
		LinkedHashMap serviceMap=reportsService.reportDataSetCreationLivy(tenantId, reportId, filtersMap);
		return serviceMap;
	}

	@GetMapping("/testRollUp")
    @Timed
    public List<LinkedHashMap> testRollUp() throws AnalysisException, IOException, ParseException{
    	
    	SparkSession spark = reportsService.getSparkSession();
    	JavaSparkContext sContext = new JavaSparkContext(spark.sparkContext());
    	SQLContext sqlCont = new SQLContext(sContext);
    	
    	String query="";
    	
    	String dbUrl = env.getProperty("spring.datasource.url");
		String[] parts = dbUrl.split("[\\s@&?$+-]+");
		String host = parts[0].split("/")[2].split(":")[0];
		String schemaName = parts[0].split("/")[3];
		String userName = env.getProperty("spring.datasource.username");
		String password = env.getProperty("spring.datasource.password");
		String jdbcDriver = env.getProperty("spring.datasource.jdbcdriver");
		
		DataFrameReader dv_data = sqlCont.read().format("jdbc")
				.option("url", dbUrl).option("user", userName)
				.option("password", password).option("dbtable", "`alpha6m_source_4`");

		Dataset<Row> dv_data_ds = dv_data.load();
		dv_data_ds.registerTempTable("dv_data_ds_v");
		
		query="SELECT ProviderName_86, CurrencyCode_86, SUM(Amount_86) FROM dv_data_ds_v "
				+" GROUP BY ROLLUP(ProviderName_86, CurrencyCode_86) ORDER BY 1,2, GROUPING_ID()";
		
		query="SELECT ProviderName_86, CurrencyCode_86, Date_86, SUM(Amount_86) FROM dv_data_ds_v "
				+" GROUP BY ROLLUP(ProviderName_86, CurrencyCode_86,Date_86) ORDER BY 1,2, GROUPING_ID()";
		String[] aggArr=new String[2];
		aggArr[0]="Amount_86";
		aggArr[1]="Duplicateamount";
		String colNames;
		//Dataset<Row> ds1=dv_data_ds.groupBy("ProviderName_86","CurrencyCode_86").pivot("CurrencyCode_86").sum(aggArr);
		//Dataset<Row> ds1=dv_data_ds.groupBy("ProviderName_86","CurrencyCode_86").sum(aggArr);
		//Dataset<Row> ds1=dv_data_ds.groupBy("ProviderName_86","CurrencyCode_86").pivot("LedgerName_86").sum("Amount_86","Duplicateamount");
		Dataset<Row> ds1=dv_data_ds.groupBy("ProviderName_86","CurrencyCode_86").pivot("LedgerName_86").sum("Amount_86","Duplicateamount");
		ds1.show((int) ds1.count());
		Dataset<Row> ds2=dv_data_ds.join(ds1,"ProviderName_86");
		ds2.show((int) ds2.count());
		
		Dataset<Row> data=spark.sql(query);
		data.show((int) data.count());
		
		spark.clearActiveSession();
    	
		return null;
	}*/
	
	
	/*@GetMapping("/getDashboardOutput")
    @Timed
    public ArrayList<HashMap> getDashboardOutput(@RequestParam Long dashboardId) throws IOException, org.json.simple.parser.ParseException, InvalidFormatException, ParseException, URISyntaxException
    {
		ArrayList<Object[]> dbRepReqList=reportingDashboardRepository.getReportRequestsByDashboard(dashboardId);
		log.info("Totla reports count in Dashboard :"+dbRepReqList.size());
		ArrayList<HashMap> finalList=new ArrayList<HashMap>();
		
		for(int i=0;i<dbRepReqList.size();i++) {
			String outputPath=(String) dbRepReqList.get(i)[9];
			log.info("Req Name :"+(String) dbRepReqList.get(i)[8]+" output path :"+(String) dbRepReqList.get(i)[9]);
		   	LinkedHashMap cmpltOutput=reportsService.hdfsFileReading(outputPath);
		   	List<JSONObject> outputList=new ArrayList<JSONObject>();
			outputList=(List<JSONObject>) cmpltOutput.get("data");
			List<JSONObject> headerList=new ArrayList<JSONObject>();
			headerList=(List<JSONObject>) cmpltOutput.get("columns");
			log.info("outputList size :"+outputList.size()+" headerList size :"+headerList.size());
			
			HashMap repOutput=new HashMap<>();
			repOutput.put("name", (String) dbRepReqList.get(i)[1]);
			repOutput.put("type", (String) dbRepReqList.get(i)[3]);
			repOutput.put("showFilters", false);
			repOutput.put("x", (int) dbRepReqList.get(i)[4]);
			repOutput.put("y", (int) dbRepReqList.get(i)[5]);
			repOutput.put("w", (int) dbRepReqList.get(i)[6]);
			repOutput.put("h", (int) dbRepReqList.get(i)[7]);
			repOutput.put("usecaseId", dbRepReqList.get(i)[10]);
//			repOutput.put("columns", headerList);
			
			HashMap options=new HashMap<>();
			JSONObject optionsObj=new JSONObject();
			JSONObject dtSourceObj=new JSONObject();
			dtSourceObj.put("dataSourceType", "json");
			dtSourceObj.put("data", outputList);
			optionsObj.put("dataSource", dtSourceObj);
			JSONObject sliceObj=new JSONObject();
			ArrayList<JSONObject> rows=new ArrayList<JSONObject>(), cols=new ArrayList<JSONObject>(), vals=new ArrayList<JSONObject>();
			
			switch((String) dbRepReqList.get(i)[3]){
			case "TABLE":
				JSONObject colObj=new JSONObject();
				colObj.put("uniqueName", "Measures");
				cols.add(colObj);
				ArrayList<String> flatOrder=new ArrayList<String>();
				for(JSONObject eachObj:headerList) {
					JSONObject newObj=new JSONObject();
					flatOrder.add((String)eachObj.get("header"));
					newObj.put("uniqueName", (String)eachObj.get("header"));
					if(((String)eachObj.get("dataType")).equalsIgnoreCase("DECIMAL")) {
						newObj.put("aggregation", (String)eachObj.get("sum"));
						newObj.put("format", (String)eachObj.get("currency"));
						vals.add(newObj);
					}else {
						newObj.put("sort", "unsorted");
						rows.add(newObj);
					}
				}
				sliceObj.put("flatOrder", flatOrder);
				sliceObj.put("expandAll", true);
				break;
			case "PIVOT":
				String grpColIds=(String) dbRepReqList.get(i)[2];
				for(String s:grpColIds.split(",")) {
					JSONObject newVal=new JSONObject();
					newVal.put("uniqueName", reportDefinationRepository.findOne(Long.valueOf(s)).getDisplayName());
					rows.add(newVal);
				}
				
				String colColIds=(String) dbRepReqList.get(i)[11];
				if(colColIds.length()>0) {
					for(String s:colColIds.split(",")) {
						if(s.length()>0) {
							JSONObject newVal=new JSONObject();
							newVal.put("uniqueName", reportDefinationRepository.findOne(Long.valueOf(s)).getDisplayName());
							cols.add(newVal);
						}
					}
				}
				
				String valColIds=(String) dbRepReqList.get(i)[12];
				for(String s:valColIds.split(",")) {
					JSONObject newVal=new JSONObject();
					newVal.put("uniqueName", reportDefinationRepository.findOne(Long.valueOf(s)).getDisplayName());
					newVal.put("aggregation", "sum");
					vals.add(newVal);
				}
				
				
				break;
			}
			sliceObj.put("rows", rows);
			sliceObj.put("columns", cols);
			sliceObj.put("measures", vals);
			optionsObj.put("slice", sliceObj);
			repOutput.put("pivotOptions",optionsObj);
			finalList.add(repOutput);
		}
		log.info("Final output :"+finalList.size());
		return finalList;
    }*/
	
	@GetMapping("/getDashboardOutput")
    @Timed
    public ArrayList<HashMap> getDashboardOutput(@RequestParam Long dashboardId) throws IOException, org.json.simple.parser.ParseException, InvalidFormatException, ParseException, URISyntaxException
    {
		ArrayList<Object[]> dbRepReqList=reportingDashboardRepository.getReportRequestsByDashboard(dashboardId);
		log.info("Totla reports count in Dashboard :"+dbRepReqList.size());
		ArrayList<HashMap> finalList=new ArrayList<HashMap>();
		
		for(int i=0;i<dbRepReqList.size();i++) {
			finalList.add(getOutputForEachUsecase(dbRepReqList.get(i)));
		}
		log.info("Final output :"+finalList.size());
		return finalList;
    }
	
	@GetMapping("/getUsecaseOutput")
    @Timed
    public HashMap getUsecaseOutput(@RequestParam Long usecaseId) throws IOException, org.json.simple.parser.ParseException, InvalidFormatException, ParseException, URISyntaxException
    {
		log.info("Usecase data for Id :"+usecaseId);
		ArrayList<Object[]> dbRepReqList=reportingDashboardRepository.getReportRequestsByUsecase(usecaseId);
		if(dbRepReqList.size()>0)
			return getOutputForEachUsecase(dbRepReqList.get(0));
		else
			return new HashMap();
    }
	
	private HashMap getOutputForEachUsecase(Object[] dbRepReq) throws IOException, URISyntaxException, org.json.simple.parser.ParseException{
		log.info("dbRepReq :"+dbRepReq.length);
		log.info("dbRepReq :"+dbRepReq.toString());
		String outputPath=(String) dbRepReq[9];
		log.info("Req Name :"+(String) dbRepReq[8]+" output path :"+(String) dbRepReq[9]);
	   	LinkedHashMap cmpltOutput=reportsService.hdfsFileReading(outputPath);
	   	List<JSONObject> outputList=new ArrayList<JSONObject>();
		outputList=(List<JSONObject>) cmpltOutput.get("data");
		List<JSONObject> headerList=new ArrayList<JSONObject>();
		headerList=(List<JSONObject>) cmpltOutput.get("columns");
		log.info("outputList size :"+outputList.size()+" headerList size :"+headerList.size());
		HashMap repOutput=new HashMap<>();
		repOutput.put("name", (String) dbRepReq[1]);
		repOutput.put("type", (String) dbRepReq[3]);
		repOutput.put("showFilters", false);
		repOutput.put("x", (int) dbRepReq[4]);
		repOutput.put("y", (int) dbRepReq[5]);
		repOutput.put("w", (int) dbRepReq[6]);
		repOutput.put("h", (int) dbRepReq[7]);
		repOutput.put("usecaseId", dbRepReq[10]);
		repOutput.put("reportId", dbRepReq[14]);
		repOutput.put("requestId", dbRepReq[15]);
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		repOutput.put("updateTime", dateFormat.format(dbRepReq[13]));
		HashMap options=new HashMap<>();
		JSONObject optionsObj=new JSONObject();
		JSONObject dtSourceObj=new JSONObject();
		dtSourceObj.put("dataSourceType", "json");
		dtSourceObj.put("data", outputList);
		optionsObj.put("dataSource", dtSourceObj);
		JSONObject sliceObj=new JSONObject();
		ArrayList<JSONObject> rows=new ArrayList<JSONObject>(), cols=new ArrayList<JSONObject>(), vals=new ArrayList<JSONObject>();
		String colColIds=(String) dbRepReq[11];
		String valColIds=(String) dbRepReq[12];
		switch((String) dbRepReq[3]){
			case "TABLE":
				log.info("In table type");
				JSONObject colObj=new JSONObject();
				colObj.put("uniqueName", "Measures");
				cols.add(colObj);
				if(colColIds.length()>0) {
					for(String s:colColIds.split(",")) {
						if(s.length()>0) {
							JSONObject newVal=new JSONObject();
							newVal.put("uniqueName", reportDefinationRepository.findOne(Long.valueOf(s)).getDisplayName());
							rows.add(newVal);
						}
					}
				}
				
				for(String s:valColIds.split(",")) {
					JSONObject newVal=new JSONObject();
					newVal.put("uniqueName", reportDefinationRepository.findOne(Long.valueOf(s)).getDisplayName());
					newVal.put("aggregation", "sum");
					vals.add(newVal);
				}
				
				log.info("Table type completed");
				sliceObj.put("expandAll", true);
				break;
			case "PIVOT":
				log.info("Pivot type");
				String grpColIds=(String) dbRepReq[2];
				for(String s:grpColIds.split(",")) {
					JSONObject newVal=new JSONObject();
					newVal.put("uniqueName", reportDefinationRepository.findOne(Long.valueOf(s)).getDisplayName());
					rows.add(newVal);
				}
				
				if(colColIds.length()>0) {
					for(String s:colColIds.split(",")) {
						if(s.length()>0) {
							JSONObject newVal=new JSONObject();
							newVal.put("uniqueName", reportDefinationRepository.findOne(Long.valueOf(s)).getDisplayName());
							cols.add(newVal);
						}
					}
				}
				
				
				for(String s:valColIds.split(",")) {
					JSONObject newVal=new JSONObject();
					newVal.put("uniqueName", reportDefinationRepository.findOne(Long.valueOf(s)).getDisplayName());
					newVal.put("aggregation", "sum");
					vals.add(newVal);
				}
				log.info("Pivot type completed");
				
				break;
		}
		sliceObj.put("rows", rows);
		sliceObj.put("columns", cols);
		sliceObj.put("measures", vals);
		optionsObj.put("slice", sliceObj);
		repOutput.put("pivotOptions",optionsObj);
		log.info("Returning....");
		return repOutput;
	}
	
	
	
	
	/*@GetMapping("/downloadFile")
    @Timed
    public void downloadFile(HttpServletRequest request, 
            HttpServletResponse response) throws ServletException, IOException
    {
        response.setHeader("Content-Disposition", "attachment; filename=MyExcel.xls");
		String jsonString = "{\"infile\": [{\"field1\": 11,\"field2\": 12,\"field3\": 13},{\"field1\": 21,\"field2\": 22,\"field3\": 23},{\"field1\": 31,\"field2\": 32,\"field3\": 33}]}";
		org.json.JSONObject output;
		Workbook workbook=new HSSFWorkbook();
		try {
			output = new org.json.JSONObject(jsonString);
			JSONArray docs = output.getJSONArray("infile");
			if(docs.length()>0) {
				workbook.createSheet();
				Sheet sheet=workbook.createSheet("MySheet");
				String[] colNames= org.json.JSONObject.getNames(docs.getJSONObject(0));
				org.apache.poi.ss.usermodel.Row colsRow=sheet.createRow(0);
				log.info("Co names: "+colNames);
				for(int i=0;i<colNames.length;i++) {
					Cell newCell=colsRow.createCell(i);
					newCell.setCellValue(colNames[i]);
					log.info("Co name: "+colNames[i]);
				}
				for(int i=0;i<docs.length();i++) {
					org.apache.poi.ss.usermodel.Row newRow=sheet.createRow(i+1);
					for(int j=0;j<colNames.length;j++) {
						if(docs.getJSONObject(i).has(colNames[j])) {
							newRow.createCell(j).setCellValue(docs.getJSONObject(i).getString(colNames[j]));
						}
						else
							newRow.createCell(j).setCellValue(XSSFCell.CELL_TYPE_BLANK);
					}
				}
				response.setContentType("application/vnd.ms-excel; charset=UTF-8");
				ServletOutputStream out = response.getOutputStream();
				workbook.write(out);
				if (workbook instanceof Closeable) {
					((Closeable) workbook).close();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }*/
	
	/**
	 * Author: Swetha
	 * @param sysReqName
	 * @param request
	 * @return
	 * @throws OozieClientException
	 */
	@Async
	@GetMapping("/cancelJob")
    @Timed
    public LinkedHashMap cancelJob(@RequestParam String sysReqName, HttpServletRequest request) throws OozieClientException{
		log.info("Rest Request to cal job  with requestName: "+sysReqName);
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		ReportRequests repRequestData1=reportRequestsRepository.findOneByTenantIdAndSysReqName(tenantId, sysReqName);
		log.info("repRequestData1: "+repRequestData1);
		
		LinkedHashMap result=new LinkedHashMap();
		result.put("status", "your request has been cancelled");
		
		for(int i=0;;i++){
			ReportRequests repRequestData=reportRequestsRepository.findOneByTenantIdAndSysReqName(tenantId, sysReqName);
			if(repRequestData!=null && repRequestData.getJobId()!=null)
			{
				String jobId=repRequestData.getJobId();
				log.info("jobId: "+jobId);
				oozieService.KillOzieParentJob(jobId);
				log.info("Killed job with jobId: "+jobId);
				break;
			}
		}
		
		return result;
	}
	
	/**
	 * Author: Swetha
	 * @param request
	 * @param reportId
	 * @param requestId
	 * @param response
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws org.json.simple.parser.ParseException
	 * @throws OozieClientException
	 * @throws URISyntaxException
	 */
	@Async
	@GetMapping("/refeshRecentRequest")
    @Timed
    public void refeshRecentRequest(HttpServletRequest request, @RequestParam String reportId, @RequestParam String requestId,
    		HttpServletResponse response) throws JsonParseException, JsonMappingException, IOException, org.json.simple.parser.ParseException, OozieClientException, URISyntaxException{
		log.info("Rest request to refeshRecentRequest with reportId: "+reportId);
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		ReportRequests reqData=reportRequestsRepository.findByTenantIdAndIdForDisplay(tenantId, requestId);

		String paramVal=reqData.getFilterMap();
		
		ObjectMapper mapper = new ObjectMapper();
		HashMap filtersMap = new HashMap();
		filtersMap = mapper.readValue(paramVal, new TypeReference<HashMap>() {});
		System.out.println("filtersMap b4: "+filtersMap);
		
		String dateTymVal=filtersMap.get("dateTimeVal").toString();//2018-08-23T07:23:58.198Z
		System.out.println("dateTymVal: "+dateTymVal);
		dateTymVal=LocalDate.now().toString();
		filtersMap.put("dateTimeVal", dateTymVal);
		if((filtersMap.get("reportTypeCode").toString()).equalsIgnoreCase("TRAIL_BALANCE_REPORT")) {
			HashMap period=(HashMap) filtersMap.get("selPeriod");
			System.out.println("period: "+period);
			System.out.println("period.get calId: "+period.get("calId"));
			String calId=period.get("calId").toString();
			log.info("Selected calender Id : "+calId);
			ArrayList<Periods> eligiblePeriods=periodsRepository.getPeriodsByCalIdOrderByDesc(Long.parseLong(calId));
			if(eligiblePeriods.size()>0) {
				String curPeriod=eligiblePeriods.get(0).getPeriodName();
				log.info("Current period name : "+curPeriod);
				period.put("periodName", curPeriod);
				filtersMap.put("selPeriod", period);
			}
		}
		System.out.println("filtersMap aftr: "+filtersMap);
		
		reportingPOC(request, reportId, reqData.getReqName(), filtersMap, 0, 25, reqData.getSysReqName(), response);
	}
	
     /*@RequestMapping
     public String sayValue() {
         return value;
     }*/
}
    
