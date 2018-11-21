package com.nspl.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.domain.BatchHeader;
import com.nspl.app.domain.DataMaster;
import com.nspl.app.domain.DataStaging;
import com.nspl.app.domain.RuleGroup;
import com.nspl.app.domain.SourceFileInbHistory;
import com.nspl.app.repository.BatchHeaderRepository;
import com.nspl.app.repository.DataStagingRepository;
import com.nspl.app.repository.SourceFileInbHistoryRepository;
import com.nspl.app.repository.search.DataStagingSearchRepository;
import com.nspl.app.service.FindDelimiterAndFileExtensionService;
import com.nspl.app.web.rest.dto.ErrorReport;
import com.nspl.app.web.rest.util.HeaderUtil;
import com.nspl.app.web.rest.util.PaginationUtil;

import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.persistence.EntityManager;
import javax.inject.Inject;
import javax.persistence.Entity;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.nspl.app.web.rest.util.PaginationUtil;
/**
 * REST controller for managing DataStaging.
 */


@RestController
@RequestMapping("/api")
public class DataStagingResource {
	
	

    private final Logger log = LoggerFactory.getLogger(DataStagingResource.class);

    private static final String ENTITY_NAME = "dataStaging";
        
    private final DataStagingRepository dataStagingRepository;

    private final DataStagingSearchRepository dataStagingSearchRepository;
    
    private final SourceFileInbHistoryRepository sourceFileInbHistoryRepository;
    
    @Inject
    FindDelimiterAndFileExtensionService findDelimiterAndFileExtensionService;
    
    private final BatchHeaderRepository batchHeaderRepository;
    
    @PersistenceContext(unitName="default")
	private EntityManager em;
    
    @Inject
	private Environment env;

    public DataStagingResource(DataStagingRepository dataStagingRepository, DataStagingSearchRepository dataStagingSearchRepository,SourceFileInbHistoryRepository sourceFileInbHistoryRepository,BatchHeaderRepository batchHeaderRepository) {
        this.dataStagingRepository = dataStagingRepository;
        this.dataStagingSearchRepository = dataStagingSearchRepository;
        this.sourceFileInbHistoryRepository =sourceFileInbHistoryRepository;
    	this.batchHeaderRepository=batchHeaderRepository;
    }

    /**
     * POST  /data-stagings : Create a new dataStaging.
     *
     * @param dataStaging the dataStaging to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dataStaging, or with status 400 (Bad Request) if the dataStaging has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/data-stagings")
    @Timed
    public ResponseEntity<DataStaging> createDataStaging(@RequestBody DataStaging dataStaging) throws URISyntaxException {
        log.debug("REST request to save DataStaging : {}", dataStaging);
        if (dataStaging.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new dataStaging cannot already have an ID")).body(null);
        }
        DataStaging result = dataStagingRepository.save(dataStaging);
        dataStagingSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/data-stagings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /data-stagings : Updates an existing dataStaging.
     *
     * @param dataStaging the dataStaging to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated dataStaging,
     * or with status 400 (Bad Request) if the dataStaging is not valid,
     * or with status 500 (Internal Server Error) if the dataStaging couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/data-stagings")
    @Timed
    public ResponseEntity<DataStaging> updateDataStaging(@RequestBody DataStaging dataStaging) throws URISyntaxException {
        log.debug("REST request to update DataStaging : {}", dataStaging);
        if (dataStaging.getId() == null) {
            return createDataStaging(dataStaging);
        }
        DataStaging result = dataStagingRepository.save(dataStaging);
        dataStagingSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dataStaging.getId().toString()))
            .body(result);
    }

    /**
     * GET  /data-stagings : get all the dataStagings.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of dataStagings in body
     */
    @GetMapping("/data-stagings")
    @Timed
    public ResponseEntity<List<DataStaging>> getAllDataStagings(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of DataStagings");
        Page<DataStaging> page = dataStagingRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/data-stagings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /data-stagings/:id : get the "id" dataStaging.
     *
     * @param id the id of the dataStaging to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dataStaging, or with status 404 (Not Found)
     */
    @GetMapping("/data-stagings/{id}")
    @Timed
    public ResponseEntity<DataStaging> getDataStaging(@PathVariable Long id) {
        log.debug("REST request to get DataStaging : {}", id);
        DataStaging dataStaging = dataStagingRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(dataStaging));
    }

    /**
     * DELETE  /data-stagings/:id : delete the "id" dataStaging.
     *
     * @param id the id of the dataStaging to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/data-stagings/{id}")
    @Timed
    public String deleteDataStaging(@PathVariable Long id) {
        log.debug("REST request to delete DataStaging : {}", id);
        DataStaging stagingRecord = new DataStaging();
        stagingRecord = dataStagingRepository.findOne(id);
        int count = -1;
        String msg = "Failure";
       Long srcFileInb = 0L;
        if(stagingRecord != null)
        {
        	srcFileInb = stagingRecord.getSrcFileInbId();
        }
        dataStagingRepository.delete(id);
        dataStagingSearchRepository.delete(id);
    	
        count = dataStagingRepository.findBySrcFileInbIdAndRecordStatus(srcFileInb,"Fail").size();
    	log.info("count"+count);
        
    	if(count == 0)
    	{
    		findDelimiterAndFileExtensionService.moveLinesFromStagingToMaster(srcFileInb);
    		SourceFileInbHistory srcFIleInbHist = new SourceFileInbHistory();
    		srcFIleInbHist = sourceFileInbHistoryRepository.findOne(srcFileInb);
    		srcFIleInbHist.setStatus("LOADED");
    		srcFIleInbHist.setFileTransformedDate(ZonedDateTime.now());
    		sourceFileInbHistoryRepository.save(srcFIleInbHist);
    		
    		Long batchId=srcFIleInbHist.getBatchId();
    		log.info("Batch Id: "+batchId);
    		Long filesCount=sourceFileInbHistoryRepository.fetchByBatchIdAndLoadedStatus(batchId);
    		if(filesCount!=0l)
    		{
    			BatchHeader batch=batchHeaderRepository.findById(batchId);
    			batch.setTransformationStatus(filesCount +" file(s) - LOADED");
    			batch.setTransformedDatetime(ZonedDateTime.now());
    			batchHeaderRepository.save(batch);
    			msg = "success";
    		}
    		//deleteStagingRecords(dataStagings.get(0).getSrcFileInbId());
    	}
    	return msg;
        //return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    /**
     * Author : Shobha
     * @param srcFileInb
     * @return
     */
    @PutMapping("/force-transform")
    @Timed
    public String forceTransform(@RequestParam Long srcFileInb)
    {
    	String msg = "Failure";
    	findDelimiterAndFileExtensionService.moveLinesFromStagingToMaster(srcFileInb);
    	SourceFileInbHistory srcFIleInbHist = new SourceFileInbHistory();
    	srcFIleInbHist = sourceFileInbHistoryRepository.findOne(srcFileInb);
    	srcFIleInbHist.setStatus("LOADED");
    	srcFIleInbHist.setFileTransformedDate(ZonedDateTime.now());
    	sourceFileInbHistoryRepository.save(srcFIleInbHist);

    	Long batchId=srcFIleInbHist.getBatchId();
    	log.info("Batch Id: "+batchId);
    	Long filesCount=sourceFileInbHistoryRepository.fetchByBatchIdAndLoadedStatus(batchId);
    	if(filesCount!=0l)
    	{
    		BatchHeader batch=batchHeaderRepository.findById(batchId);
    		batch.setTransformationStatus(filesCount +" file(s) - LOADED");
    		batch.setTransformedDatetime(ZonedDateTime.now());
    		batchHeaderRepository.save(batch);
    		msg = "success";
    	}
    	//deleteStagingRecords(dataStagings.get(0).getSrcFileInbId());
    	return msg;

    }

    /**
     * SEARCH  /_search/data-stagings?query=:query : search for the dataStaging corresponding
     * to the query.
     *
     * @param query the query of the dataStaging search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/data-stagings")
    @Timed
    public ResponseEntity<List<DataStaging>> searchDataStagings(@RequestParam Long tenantId, @RequestParam(value="filterValue",required=false) String filterValue, @ApiParam Pageable pageable) {
    	String query = "";
    	query = query + " tenantId: \""+tenantId+"\" ";		// Filtering with tenant id	
	   	if(filterValue != null)
	   	{
	   		query = query + " AND \""+filterValue+"\"";
	   	}
        Page<DataStaging> page = dataStagingSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/data-stagings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    
    /**
     * Author: Swetha
     * Api to retrieve distinct column values
     * @param tableName
     * @param colName
     * @param tenantId
     * @param templateId
     * @return
     */
    @GetMapping("/getDistinctColValues")
    @Timed
    public List<String> getDistinctColValues(@RequestParam String tableName, @RequestParam String colName,@RequestParam Long tenantId,@RequestParam Long templateId){
    	
    	log.info("REST request to getDistinctColValues for TableName: "+tableName +" and colName: "+colName+"tenantId: "+tenantId+" template_id= "+templateId);
    	List<String> colLst=new ArrayList<String>();
    	
    	Query distinctList=em.createQuery("select distinct"+"("+colName+")"+ " FROM "+tableName+" where tenantId="+tenantId+" and templateId= "+templateId);
    			//" and template_id: "+templateId);
		log.info("distinctList : "+distinctList);
    	
		List list=distinctList.getResultList();
		log.info("size: "+list.size());
		
		for(int i=0;i<list.size();i++){
			
			log.info("list.get(i): "+list.get(i));
			String obj=list.get(i).toString();
			colLst.add(obj);
		}
		
		return colLst;
    	
    }
    /**
     * Author Kiran
     * @param srcFileInbId
     * @return
     */
   /* @Transactional
    @GetMapping("/moveLinesFromStagingToMaster")
    @Timed
   */
    
    /**
     * Author : Shobha
     * @param offset
     * @param limit
     * @param response
     * @param sourceProfileId
     * @param templateId
     * @param srcFileHistInbId
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/dataStagingLinesBySrcFileInb")
    @Timed
    public ResponseEntity<List<DataStaging>>  fetchDataStagingLinesBySrcFileInb(
    		@RequestParam(value = "page" , required = false) Integer offset,
    		@RequestParam (value="recordStatus") String recordStatus,
    		@RequestParam(value = "per_page", required = false) Integer limit,HttpServletResponse response,
    		@RequestParam(required=false) String sortColName, @RequestParam (required=false)String sortOrder,
    		@RequestParam Long sourceProfileId,@RequestParam ( required=false)Long intermediateId,  @RequestParam Long srcFileHistInbId) throws URISyntaxException {
    	log.debug("Rest request to fetch DataStaging lines by srcInb with:"+srcFileHistInbId);	
    	List<DataStaging> dataStagingLines = new ArrayList<DataStaging>();
    	PaginationUtil paginationUtil=new PaginationUtil();
    	int maxlmt=paginationUtil.MAX_LIMIT;
		int minlmt=paginationUtil.MIN_OFFSET;
		Page<DataStaging> page = null;
		HttpHeaders headers = null;
    	if(limit==null || limit<minlmt){
    		if(intermediateId == null)
    		{
    			dataStagingLines = dataStagingRepository.findBySrcFileInbIdAndRecordStatus(srcFileHistInbId,recordStatus);
    		}
    		else
    		{
    			dataStagingLines = dataStagingRepository.findBySrcFileInbIdAndIntermediateIdAndRecordStatus(srcFileHistInbId,intermediateId,recordStatus);
    		}
    		
    		//log.info("dataStagingLines:"+dataStagingLines.size());
			limit = dataStagingLines.size();
		}
		if(limit == 0 )
    	{
    		limit = paginationUtil.DEFAULT_LIMIT;
    	}
    	if(offset == null || offset == 0)
    	{
    		offset = paginationUtil.DEFAULT_OFFSET;
    	}
    	log.info("limit=>"+limit+"offset=>"+offset);
    	if(limit>maxlmt)
		{
			log.info("input limit exceeds maxlimit");
			if(recordStatus == null || recordStatus.isEmpty() || recordStatus.equals("") || recordStatus.equalsIgnoreCase("All"))
			{
				if(intermediateId == null)
				{
					page =  dataStagingRepository.findBySrcFileInbId(srcFileHistInbId,PaginationUtil.generatePageRequestWithSortColumn(offset, limit,  sortOrder,sortColName));
				}
				else
				{
					page =  dataStagingRepository.findBySrcFileInbIdAndIntermediateId(srcFileHistInbId,intermediateId,PaginationUtil.generatePageRequestWithSortColumn(offset, limit,  sortOrder,sortColName));
				}
				 headers = PaginationUtil.generatePaginationHttpHeaders2(page, "/api/dataStagingLinesBySrcFileInb",offset, limit);
			}
			else
			{
				if(intermediateId == null)
				{
					 page =  dataStagingRepository.findBySrcFileInbIdAndRecordStatus(srcFileHistInbId,recordStatus,PaginationUtil.generatePageRequestWithSortColumn(offset, limit,  sortOrder,sortColName));
				}
				else
				{
					 page =  dataStagingRepository.findBySrcFileInbIdAndIntermediateIdAndRecordStatus(srcFileHistInbId,intermediateId,recordStatus,PaginationUtil.generatePageRequestWithSortColumn(offset, limit,  sortOrder,sortColName));
				}
				
				 headers = PaginationUtil.generatePaginationHttpHeaders2(page, "/api/dataStagingLinesBySrcFileInb",offset, limit);
			}
		}
		else{
			log.info("input limit is within maxlimit"+offset+"limit"+limit+"recordStatus"+recordStatus+"sortOrder"+sortOrder+",sortColName"+sortColName);
			if(recordStatus == null || recordStatus.isEmpty() || recordStatus.equals("") || recordStatus.equalsIgnoreCase("All"))
			{
				if(intermediateId == null)
				{
					page =  dataStagingRepository.findBySrcFileInbId(srcFileHistInbId,PaginationUtil.generatePageRequestWithSortColumn(offset, limit,  sortOrder,sortColName));
				}
				else
				{
					page =  dataStagingRepository.findBySrcFileInbIdAndIntermediateId(srcFileHistInbId,intermediateId,PaginationUtil.generatePageRequestWithSortColumn(offset, limit,  sortOrder,sortColName));
				}
				 
				// headers = PaginationUtil.generatePaginationHttpHeaders2(page, "/api/dataStagingLinesBySrcFileInb",offset, limit);
			}
			else
			{
				if(intermediateId == null)
				{
					log.info("intermediate id is null with srcFileHistInbId"+srcFileHistInbId +"recordStatus"+recordStatus);
					page =  dataStagingRepository.findBySrcFileInbIdAndRecordStatus(srcFileHistInbId,recordStatus,
							PaginationUtil.generatePageRequestWithSortColumn(offset, limit,  sortOrder,sortColName));
				}
				else
				{
					log.info("intermediate id is not null");
					 page =  dataStagingRepository.findBySrcFileInbIdAndIntermediateIdAndRecordStatus(srcFileHistInbId,intermediateId,recordStatus,PaginationUtil.generatePageRequestWithSortColumn(offset, limit,  sortOrder,sortColName));
				}
				
				//headers = PaginationUtil.generatePaginationHttpHeaderss(page, "/api/dataStagingLinesBySrcFileInb", offset, limit);
			}
			int count = 0;
			if(recordStatus == null || recordStatus.isEmpty() || recordStatus.equals("") || recordStatus.equalsIgnoreCase("All"))
			{
				if(intermediateId == null)
				{
					count = dataStagingRepository.findBySrcFileInbId(srcFileHistInbId).size();
				}
				else
				{
					count = dataStagingRepository.findBySrcFileInbIdAndIntermediateId(srcFileHistInbId,intermediateId).size();
				}
				
			}
			else
			{
				if(intermediateId == null)
				{
					count = dataStagingRepository.findBySrcFileInbIdAndRecordStatus(srcFileHistInbId,recordStatus).size();
				}
				else
				{
					count = dataStagingRepository.findBySrcFileInbIdAndIntermediateIdAndRecordStatus(srcFileHistInbId,intermediateId,recordStatus).size();
				}
				
			}
			
			response.addIntHeader("X-COUNT",count);
		}
    	//log.info("content of staging is:"+page.getContent());
    	return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    /**
     * Author : Shobha
     * @param dataStagings
     * @return
     * @throws URISyntaxException
     */
    @PutMapping("/updateDataStagingRecords")
    @Timed
    public ErrorReport updateDataStagingrecords(@RequestBody List<DataStaging> dataStagings) throws URISyntaxException {
    	log.info("updateDataStagingRecords with "+dataStagings.size());
    	ErrorReport errorReportToUpdateRecords = new ErrorReport();
    	List<DataStaging> dss = dataStagingRepository.save(dataStagings);
    	//dataStagingSearchRepository.save(dss);
    	String taskName = "";
    	taskName = "Update DataStaging Lines";
    	errorReportToUpdateRecords.setTaskStatus("success");
    	//move to staging
    	int count = -1;
    	count = dataStagingRepository.findBySrcFileInbIdAndRecordStatus(dataStagings.get(0).getSrcFileInbId(),"Fail").size();
    	log.info("count"+count);
    	if(count == 0)
    	{
    		findDelimiterAndFileExtensionService.moveLinesFromStagingToMaster(dataStagings.get(0).getSrcFileInbId());
    		SourceFileInbHistory srcFIleInbHist = new SourceFileInbHistory();
    		srcFIleInbHist = sourceFileInbHistoryRepository.findOne(dataStagings.get(0).getSrcFileInbId());
    		srcFIleInbHist.setStatus("LOADED");
    		srcFIleInbHist.setFileTransformedDate(ZonedDateTime.now());
    		sourceFileInbHistoryRepository.save(srcFIleInbHist);
    		
    		Long batchId=srcFIleInbHist.getBatchId();
    		log.info("Batch Id: "+batchId);
    		
    		Long filesCount=sourceFileInbHistoryRepository.fetchByBatchIdAndLoadedStatus(batchId);
    		if(filesCount!=0l)
    		{
    			BatchHeader batch=batchHeaderRepository.findById(batchId);
    			batch.setTransformationStatus(filesCount +" file(s) - LOADED");
    			batch.setTransformedDatetime(ZonedDateTime.now());
    			batchHeaderRepository.save(batch);
    		}
    		//deleteStagingRecords(dataStagings.get(0).getSrcFileInbId());
    		
			
    		taskName = taskName + " and Transformation is ";
    		log.info("taskName"+taskName);
    		errorReportToUpdateRecords.setTaskName(taskName);
    	}
    	return errorReportToUpdateRecords;
    }
    /**
     * Author : shobha
     * @param srcFileInbHistId
     */
    @DeleteMapping("/deleteStagingRecords/{srcFileInbHistId}")
    @Timed
    public void deleteStagingRecords(@PathVariable Long srcFileInbHistId) {
        log.debug("REST request to delete staging records by srcFileInbHistId ", srcFileInbHistId);
        List<DataStaging> stagingRecordsToDelete = new ArrayList<DataStaging>();
        stagingRecordsToDelete = dataStagingRepository.findBySrcFileInbId(srcFileInbHistId);
        if(stagingRecordsToDelete.size()>0)
        dataStagingRepository.deleteInBatch(stagingRecordsToDelete);
      //  dataStagingSearchRepository.delete(stagingRecordsToDelete);
    }
    @GetMapping("/moveFromStagingToMasterTable")
    @Timed
    public ErrorReport moveFromStagingToMasterTable(@RequestParam Long srcFileInbHistId) throws URISyntaxException {
    	ErrorReport reportToMoveFromStagingToMaster = new ErrorReport();
  //  findDelimiterAndFileExtensionService.moveLinesFromStagingToMaster(srcFileInbHistId);
    SourceFileInbHistory srcFIleInbHist = new SourceFileInbHistory();
	srcFIleInbHist = sourceFileInbHistoryRepository.findOne(srcFileInbHistId);
	srcFIleInbHist.setStatus("Loaded");
	sourceFileInbHistoryRepository.save(srcFIleInbHist);
	
	Long batchId=srcFIleInbHist.getBatchId();
	log.info("Batch Id: "+batchId);
	
	Long filesCount=sourceFileInbHistoryRepository.fetchByBatchIdAndLoadedStatus(batchId);
	if(filesCount!=0l)
	{
		BatchHeader batch=batchHeaderRepository.findById(batchId);
		batch.setTransformationStatus(filesCount +" file(s) - TRANSFORMED");
		batch.setTransformedDatetime(ZonedDateTime.now());
		batchHeaderRepository.save(batch);
	}
    	return reportToMoveFromStagingToMaster;
    }
    
    
    
    /**
     * Author Kiran
     * @param srcFileInbId
     * @param searchWord
     * @return
     */
    	@GetMapping("/textSearchDataStagingLinesBySrcFileInb")
    	@Timed
    	public List<DataStaging>  textSearchDataStagingLinesBySrcFileInb(@RequestParam Long srcFileInbId, @RequestParam String searchWord)
    	{
    		log.info("Api to search in dataStaging, srcFileInbId: "+srcFileInbId+" and searchKeyword: "+searchWord);
    		String dataStagingQuery=getColumnNamesAsString(searchWord,"t_data_staging");

    		dataStagingQuery="Select * from t_data_staging where src_file_inb_id ="+srcFileInbId+" "+dataStagingQuery;
    		log.info("dataStagingQuery: "+dataStagingQuery);
    		Connection conn = getDbConnection();
    		List<DataStaging> searchResults=getDataStagingRecords(conn, dataStagingQuery);
    		if(conn!=null)
    		{
    			try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
    		}
    		//log.info("searchResults:- "+searchResults);
    		return searchResults;

    	}


    	public String getColumnNamesAsString(String searchWord, String tableName)
    	{
    		String columnString = "";

    		columnString="COALESCE(file_name, ''), COALESCE(line_content, ''), ";
    		for(int i=1;i<=100;i++)
    		{
    			if(i<10){
    				columnString=columnString+" COALESCE(field_0"+i+", ''), ";}
    			else{
    				columnString=columnString+" COALESCE(field_"+i+", ''), ";
    				if(i==100)
    					columnString=columnString+" COALESCE(field_"+i+", '') ";
    			}

    		}
    		if(tableName.equals("t_data_staging"))
    			columnString = columnString +" COALESCE(record_status, '') ";
    		//		else if(tableName.equals("t_data_master"))
    		//			columnString = columnString
    		columnString = " AND CONCAT("+columnString +") LIKE '%"+searchWord+"%'";
    		return columnString;
    	}



    	public Connection getDbConnection()
    	{
    		Connection conn = null;
    		String dbUrl=env.getProperty("spring.datasource.url");
    		String userName = env.getProperty("spring.datasource.username");
    		String password = env.getProperty("spring.datasource.password");
    		String jdbcDriver = env.getProperty("spring.datasource.jdbcdriver");  
    		try
    		{
    			try 
    			{
    				Class.forName(jdbcDriver);
    			}
    			catch (ClassNotFoundException e) 
    			{
    				e.printStackTrace();
    			}
    			conn = DriverManager.getConnection(dbUrl, userName, password);
    			log.info("Connected database successfully...");
    		}
    		catch(Exception se){
    			System.out.println("Check -> Database url: "+dbUrl+" and Username: "+userName+" and pasword: "+password);
    			System.out.println("Jdbc Connection failed: "+dbUrl+" and "+se);
    			System.exit(0);
    		}
    		return conn;
    	}

    	public static List<DataStaging> getDataStagingRecords(Connection conn,String query)
    	{
    		List<DataStaging> dataStagingRecords = new ArrayList<DataStaging>();
    		Statement stmt = null;
    		ResultSet rs = null;

    		try{
    			stmt = conn.createStatement();
    			stmt.executeQuery(query);
    			rs=stmt.getResultSet();

    			while(rs.next())
    			{
    				DataStaging dSRecords = new DataStaging();
    				//dSRecords.setId(rs.getLong("id"));
    				//dSRecords.setTenantId(rs.getLong("tenant_id"));
    				//dSRecords.setProfileId(rs.getLong("profile_id"));
    				//dSRecords.setTemplateId(rs.getLong("template_id"));
    				dSRecords.setFileName(rs.getString("file_name"));
    				dSRecords.setLineContent(rs.getString("line_content"));
    				dSRecords.setField01(rs.getString("field_01"));
    				dSRecords.setField02(rs.getString("field_02"));
    				dSRecords.setField03(rs.getString("field_03"));
    				dSRecords.setField04(rs.getString("field_04"));
    				dSRecords.setField05(rs.getString("field_05"));
    				dSRecords.setField06(rs.getString("field_06"));
    				dSRecords.setField07(rs.getString("field_07"));
    				dSRecords.setField08(rs.getString("field_08"));
    				dSRecords.setField09(rs.getString("field_09"));
    				dSRecords.setField10(rs.getString("field_10"));
    				dSRecords.setField11(rs.getString("field_11"));
    				dSRecords.setField12(rs.getString("field_12"));
    				dSRecords.setField13(rs.getString("field_13"));
    				dSRecords.setField14(rs.getString("field_14"));
    				dSRecords.setField15(rs.getString("field_15"));
    				dSRecords.setField16(rs.getString("field_16"));
    				dSRecords.setField17(rs.getString("field_17"));
    				dSRecords.setField18(rs.getString("field_18"));
    				dSRecords.setField19(rs.getString("field_19"));
    				dSRecords.setField20(rs.getString("field_20"));
    				dSRecords.setField21(rs.getString("field_21"));
    				dSRecords.setField22(rs.getString("field_22"));
    				dSRecords.setField23(rs.getString("field_23"));
    				dSRecords.setField24(rs.getString("field_24"));
    				dSRecords.setField25(rs.getString("field_25"));
    				dSRecords.setField26(rs.getString("field_26"));
    				dSRecords.setField27(rs.getString("field_27"));
    				dSRecords.setField28(rs.getString("field_28"));
    				dSRecords.setField29(rs.getString("field_29"));
    				dSRecords.setField30(rs.getString("field_30"));
    				dSRecords.setField31(rs.getString("field_31"));
    				dSRecords.setField32(rs.getString("field_32"));
    				dSRecords.setField33(rs.getString("field_33"));
    				dSRecords.setField34(rs.getString("field_34"));
    				dSRecords.setField35(rs.getString("field_35"));
    				dSRecords.setField36(rs.getString("field_36"));
    				dSRecords.setField37(rs.getString("field_37"));
    				dSRecords.setField38(rs.getString("field_38"));
    				dSRecords.setField39(rs.getString("field_39"));
    				dSRecords.setField40(rs.getString("field_40"));
    				dSRecords.setField41(rs.getString("field_41"));
    				dSRecords.setField42(rs.getString("field_42"));
    				dSRecords.setField43(rs.getString("field_43"));
    				dSRecords.setField44(rs.getString("field_44"));
    				dSRecords.setField45(rs.getString("field_45"));
    				dSRecords.setField46(rs.getString("field_46"));
    				dSRecords.setField47(rs.getString("field_47"));
    				dSRecords.setField48(rs.getString("field_48"));
    				dSRecords.setField49(rs.getString("field_49"));
    				dSRecords.setField50(rs.getString("field_50"));
    				dSRecords.setField51(rs.getString("field_51"));
    				dSRecords.setField52(rs.getString("field_52"));
    				dSRecords.setField53(rs.getString("field_53"));
    				dSRecords.setField54(rs.getString("field_54"));
    				dSRecords.setField55(rs.getString("field_55"));
    				dSRecords.setField56(rs.getString("field_56"));
    				dSRecords.setField57(rs.getString("field_57"));
    				dSRecords.setField58(rs.getString("field_58"));
    				dSRecords.setField59(rs.getString("field_59"));
    				dSRecords.setField60(rs.getString("field_60"));
    				dSRecords.setField61(rs.getString("field_61"));
    				dSRecords.setField62(rs.getString("field_62"));
    				dSRecords.setField63(rs.getString("field_63"));
    				dSRecords.setField64(rs.getString("field_64"));
    				dSRecords.setField65(rs.getString("field_65"));
    				dSRecords.setField66(rs.getString("field_66"));
    				dSRecords.setField67(rs.getString("field_67"));
    				dSRecords.setField68(rs.getString("field_68"));
    				dSRecords.setField69(rs.getString("field_69"));
    				dSRecords.setField70(rs.getString("field_70"));
    				dSRecords.setField71(rs.getString("field_71"));
    				dSRecords.setField72(rs.getString("field_72"));
    				dSRecords.setField73(rs.getString("field_73"));
    				dSRecords.setField74(rs.getString("field_74"));
    				dSRecords.setField75(rs.getString("field_75"));
    				dSRecords.setField76(rs.getString("field_76"));
    				dSRecords.setField77(rs.getString("field_77"));
    				dSRecords.setField78(rs.getString("field_78"));
    				dSRecords.setField79(rs.getString("field_79"));
    				dSRecords.setField80(rs.getString("field_80"));
    				dSRecords.setField81(rs.getString("field_81"));
    				dSRecords.setField82(rs.getString("field_82"));
    				dSRecords.setField83(rs.getString("field_83"));
    				dSRecords.setField84(rs.getString("field_84"));
    				dSRecords.setField85(rs.getString("field_85"));
    				dSRecords.setField86(rs.getString("field_86"));
    				dSRecords.setField87(rs.getString("field_87"));
    				dSRecords.setField88(rs.getString("field_88"));
    				dSRecords.setField89(rs.getString("field_89"));
    				dSRecords.setField90(rs.getString("field_90"));
    				dSRecords.setField91(rs.getString("field_91"));
    				dSRecords.setField92(rs.getString("field_92"));
    				dSRecords.setField93(rs.getString("field_93"));
    				dSRecords.setField94(rs.getString("field_94"));
    				dSRecords.setField95(rs.getString("field_95"));
    				dSRecords.setField96(rs.getString("field_96"));
    				dSRecords.setField97(rs.getString("field_97"));
    				dSRecords.setField98(rs.getString("field_98"));
    				dSRecords.setField99(rs.getString("field_99"));
    				dSRecords.setField100(rs.getString("field_100"));
    				dSRecords.setRecordStatus(rs.getString("record_status"));

    				dataStagingRecords.add(dSRecords);
    			}
    		}
    		catch(SQLException se)
    		{
    			System.out.println("Connection Failed while gettig data Staging Records details "+se);
    		}
    		finally
    		{
    			try { if (rs != null) rs.close(); } catch (Exception e) {};
    		    try { if (stmt != null) stmt.close(); } catch (Exception e) {};
    		}

    		System.out.println("data Staging Records size: "+dataStagingRecords.size());
    		return dataStagingRecords;
    	}
}