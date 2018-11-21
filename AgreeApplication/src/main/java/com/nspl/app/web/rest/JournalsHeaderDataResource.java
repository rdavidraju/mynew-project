package com.nspl.app.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.codahale.metrics.annotation.Timed;
import com.jcraft.jsch.SftpException;
import com.nspl.app.config.ApplicationContextProvider;
import com.nspl.app.domain.AccountedSummary;
import com.nspl.app.domain.ApplicationPrograms;
import com.nspl.app.domain.Calendar;
import com.nspl.app.domain.DataViews;
import com.nspl.app.domain.DataViewsColumns;
import com.nspl.app.domain.FxRates;
import com.nspl.app.domain.JeLdrDetails;
import com.nspl.app.domain.JeLines;
import com.nspl.app.domain.JournalsHeaderData;
import com.nspl.app.domain.LookUpCode;
import com.nspl.app.domain.NotificationBatch;
import com.nspl.app.domain.Periods;
import com.nspl.app.domain.TemplateDetails;
import com.nspl.app.domain.TenantConfig;
import com.nspl.app.repository.AccountedSummaryRepository;
import com.nspl.app.repository.ApplicationProgramsRepository;
import com.nspl.app.repository.CalendarRepository;
import com.nspl.app.repository.DataViewsColumnsRepository;
import com.nspl.app.repository.FxRatesRepository;
import com.nspl.app.repository.JeLdrDetailsRepository;
import com.nspl.app.repository.JeLinesRepository;
import com.nspl.app.repository.JournalsHeaderDataRepository;
import com.nspl.app.repository.LookUpCodeRepository;
import com.nspl.app.repository.PeriodsRepository;
import com.nspl.app.repository.TemplateDetailsRepository;
import com.nspl.app.repository.TenantConfigRepository;
import com.nspl.app.service.FileExportService;
import com.nspl.app.service.FileService;
import com.nspl.app.service.JeWQService;
import com.nspl.app.service.PropertiesUtilService;
import com.nspl.app.service.ReconciliationResultService;
import com.nspl.app.service.UserJdbcService;
import com.nspl.app.web.rest.dto.ErrorReport;
import com.nspl.app.web.rest.util.HeaderUtil;
import com.nspl.app.web.rest.util.PaginationUtil;

import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;

import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateRepository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

















import javax.sql.DataSource;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;


/**
 * REST controller for managing JournalsHeaderData.
 */
@RestController
@RequestMapping("/api")
public class JournalsHeaderDataResource {

    private final Logger log = LoggerFactory.getLogger(JournalsHeaderDataResource.class);

    private static final String ENTITY_NAME = "journalsHeaderData";
        
    private final JournalsHeaderDataRepository journalsHeaderDataRepository;

    
    @Inject
    TemplateDetailsRepository templateDetailsRepository;
    
    @Inject
    LookUpCodeRepository lookUpCodeRepository;
    
    @Inject
    JeLinesRepository jeLinesRepository;
    
    @Inject
    JeLdrDetailsRepository jeLdrDetailsRepository;
    
    @Inject
    DataViewsColumnsRepository dataViewsColumnsRepository;
    
    
    @Inject
    FileService fileService;
    
    @Inject
    CalendarRepository calendarRepository;
    
    @Inject
    TenantConfigRepository tenantConfigRepository;
    
    @PersistenceContext(unitName="default")
	private EntityManager em;
    
    @Autowired
   	Environment env;
    
    @Inject
    JeWQService jeWQService;
    
    @Inject
	PropertiesUtilService propertiesUtilService;
    
    
    @Inject
    PeriodsRepository periodsRepository;
    
    
    @Inject
    ApplicationProgramsRepository applicationProgramsRepository;
    
    @Inject
    UserJdbcService userJdbcService;
    
    
    @Inject
    AccountedSummaryRepository accountedSummaryRepository;
    
    
    @Inject
    FileExportService fileExportService;
 
   /* @Inject
    JournalsHeaderDataSearchRepository journalsHeaderDataSearchRepository;*/
    
    @Inject
    ReconciliationResultService reconciliationResultService;
    
    
    @Inject
    FxRatesRepository fxRatesRepository;
	
    
    
    public JournalsHeaderDataResource(JournalsHeaderDataRepository journalsHeaderDataRepository) {
        this.journalsHeaderDataRepository = journalsHeaderDataRepository;
    }

    /**
     * POST  /journals-header-data : Create a new journalsHeaderData.
     *
     * @param journalsHeaderData the journalsHeaderData to create
     * @return the ResponseEntity with status 201 (Created) and with body the new journalsHeaderData, or with status 400 (Bad Request) if the journalsHeaderData has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/journals-header-data")
    @Timed
    public ResponseEntity<JournalsHeaderData> createJournalsHeaderData(@RequestBody JournalsHeaderData journalsHeaderData) throws URISyntaxException {
        log.debug("REST request to save JournalsHeaderData : {}", journalsHeaderData);
        if (journalsHeaderData.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new journalsHeaderData cannot already have an ID")).body(null);
        }
        JournalsHeaderData result = journalsHeaderDataRepository.save(journalsHeaderData);
        return ResponseEntity.created(new URI("/api/journals-header-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    

    /**
     * PUT  /journals-header-data : Updates an existing journalsHeaderData.
     *
     * @param journalsHeaderData the journalsHeaderData to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated journalsHeaderData,
     * or with status 400 (Bad Request) if the journalsHeaderData is not valid,
     * or with status 500 (Internal Server Error) if the journalsHeaderData couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/journals-header-data")
    @Timed
    public ResponseEntity<JournalsHeaderData> updateJournalsHeaderData(@RequestBody JournalsHeaderData journalsHeaderData) throws URISyntaxException {
        log.debug("REST request to update JournalsHeaderData : {}", journalsHeaderData);
        if (journalsHeaderData.getId() == null) {
            return createJournalsHeaderData(journalsHeaderData);
        }
        JournalsHeaderData result = journalsHeaderDataRepository.save(journalsHeaderData);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, journalsHeaderData.getId().toString()))
            .body(result);
    }

    /**
     * GET  /journals-header-data : get all the journalsHeaderData.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of journalsHeaderData in body
     */
    @GetMapping("/journals-header-data")
    @Timed
    public ResponseEntity<List<JournalsHeaderData>> getAllJournalsHeaderData(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of JournalsHeaderData");
        Page<JournalsHeaderData> page = journalsHeaderDataRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/journals-header-data");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /journals-header-data/:id : get the "id" journalsHeaderData.
     *
     * @param id the id of the journalsHeaderData to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the journalsHeaderData, or with status 404 (Not Found)
     */
    @GetMapping("/journals-header-data/{id}")
    @Timed
    public ResponseEntity<JournalsHeaderData> getJournalsHeaderData(@PathVariable Long id) {
        log.debug("REST request to get JournalsHeaderData : {}", id);
        JournalsHeaderData journalsHeaderData = journalsHeaderDataRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(journalsHeaderData));
    }

    /**
     * DELETE  /journals-header-data/:id : delete the "id" journalsHeaderData.
     *
     * @param id the id of the journalsHeaderData to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/journals-header-data/{id}")
    @Timed
    public ResponseEntity<Void> deleteJournalsHeaderData(@PathVariable Long id) {
        log.debug("REST request to delete JournalsHeaderData : {}", id);
        journalsHeaderDataRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
/*   @GetMapping("/_search/journals-header-data")
    @Timed
    public List<JournalsHeaderData> searchJournalHeaderData(@RequestParam Long tenantId, @RequestParam(value="filterValue",required=false) String filterValue,@RequestParam(value = "pageNumber", required=false) Long pageNumber, @RequestParam(value = "pageSize", required=false) Long pageSize) {
    	log.info("Rest api for fetching notification batch usgin full text search for the tenant_id: "+tenantId);
		Long limit = 0L;
		limit = (pageNumber-1 * pageSize + 1)-1;
		log.info("Limit Starting Values : "+ limit);
		log.info("Page Number : "+ pageNumber);
    	
    	List<JournalsHeaderData> batchHeaders = journalsHeaderDataRepository.fetchRecordsByKeyWord(tenantId, filterValue, pageNumber, limit);
    	return batchHeaders;
    }*/
    
    
    
    /**
     * author:ravali
     * @param tableName
     * @param columnName
     * @param tenantId
     * @param parentTableName
     * @param parentColumnName
     * @return
     */
    @GetMapping("/sideBarAPIForJournalHeader")
	@Timed
	public List<HashMap> getFileTempList(HttpServletRequest request,@RequestParam String tableName,@RequestParam String columnName,@RequestParam(value = "parentTableName", required = false) String parentTableName,@RequestParam(value = "parentColumnName", required = false) String parentColumnName)
	{
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
      	Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	log.info("Rest Request to get distinct source details");
    	log.info("columnName :"+columnName);

    	Query distinctList=em.createQuery("select distinct"+"("+columnName+")"+ " FROM "+tableName+" where tenant_id="+tenantId);
    	log.info("distinctList : "+distinctList);
    	List distinct = new ArrayList<String>();
    	distinct =distinctList.getResultList();

    	log.info("distinctList :"+distinctList);
    	log.info("distinct :"+distinct);
    	
    	List<HashMap> FinalMap=new ArrayList<HashMap>();


    	int k=0;
    	int allCount = 0;
    	for(int i=0;i<distinct.size();i++)
    	{

    		if(distinct.get(i) != null && !distinct.get(i).toString().isEmpty() && !distinct.get(i).toString().equals(""))
    		{
    			HashMap map1=new HashMap();
    			k=k+1;
    			String str=columnName+"='"+distinct.get(i)+"' and tenantId="+tenantId;
    			//List entity=em.createQuery("select u FROM "+tableName+" u"+ " where "+str).getResultList();
    			List entity=em.createQuery("FROM "+tableName+ " where "+str).getResultList();

    			log.info("entity.size() :"+entity.size());
    			map1.put("S.no",k);
    			map1.put(columnName, distinct.get(i));
    			String displayName=distinct.get(i).toString().replaceAll("\\W", " ");
    			map1.put("dispname", displayName);

    			map1.put("count",entity.size());
    			if(parentTableName!=null && parentColumnName!=null)
    			{
    				List name=em.createQuery("select "+parentColumnName+" FROM "+parentTableName+ " where id="+distinct.get(i)).getResultList();
    				for(int n=0;n<name.size();n++)
    				{
    					if(name.get(n)!=null)
    						map1.put("sourceName", name.get(n));
    					else
    						map1.put("sourceName", "");	
    				}
    			}
    			map1.put("Lists", entity);
    			allCount = allCount + entity.size();
    			FinalMap.add(map1);
    		}


    	}



    	return FinalMap;
	}


/**
 * author :ravali
 * Desc:LOV for ledger name and periods
 * @param jeTempId
 * @param tenantId
 * @return
 */
    @GetMapping("/LedgerAndPeriodList")
   	@Timed 
   	
   	public HashMap journalHeaderDetails(HttpServletRequest request,@RequestParam(value = "jeTempId", required = false) String jeTempId)//,@RequestParam Long tenantId)
	{
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
      	Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	log.info("request rest to fetch ledgerNames and periods");
    	TemplateDetails tempDet=templateDetailsRepository.findByTenantIdAndIdForDisplay(tenantId, jeTempId);
    	HashMap finalMap=new  HashMap();
    	List<BigInteger> tempIds = null;
    	if(jeTempId==null)
    		tempIds=templateDetailsRepository.fetchByTenantIdAndActive(tenantId);
    	log.info("tempIds :"+tempIds);
    	
    	List<String> ledgerName=new ArrayList<String>();
    	List<String> periods=new ArrayList<String>();
    	if(tempIds!=null && !tempIds.isEmpty())
    	{
    		ledgerName=journalsHeaderDataRepository.fetchLedgerNameByJeTempIdIn(tempIds);
    		periods=journalsHeaderDataRepository.fetchPeriodByJeTempIdIn(tempIds);
    	}
    	else if(jeTempId!=null)
    	{
    		ledgerName=journalsHeaderDataRepository.fetchLedgerNameByJeTempId(tempDet.getId());
    		periods=journalsHeaderDataRepository.fetchPeriodByJeTempId(tempDet.getId());
    	}
    	finalMap.put("ledgerNames", ledgerName)	;
    	finalMap.put("period",periods);
		return finalMap;
    	
	}
    
    /**
     * author:ravali
     * Desc:List of batch level information
     * @param journalHeaderDetails
     * @param tenantId
     * @return
     */
   /* @PostMapping("/journalHeaderGroupedInfo")
   	@Timed 
 	public LinkedHashMap journalHeaderDetails(@RequestBody HashMap journalHeaderDetails,@RequestParam Long tenantId)
    {
    	log.info("request rest to fetch ledgerNames and periods");
    	List<JournalsHeaderData> jHDList=new ArrayList<JournalsHeaderData>();
    	LinkedHashMap finalLhmap=new LinkedHashMap();
    	List<LinkedHashMap> finalMap=new ArrayList<LinkedHashMap>();
    	if((journalHeaderDetails.get("jeTempId")!=null && Long.valueOf(journalHeaderDetails.get("jeTempId").toString())!= 0) && (journalHeaderDetails.get("jeLedger")!=null && journalHeaderDetails.get("jeLedger")!="" && !journalHeaderDetails.get("jeLedger").toString().equalsIgnoreCase("All"))&& 
    			(journalHeaderDetails.get("jePeriod")!="" && journalHeaderDetails.get("jePeriod")!=null && !journalHeaderDetails.get("jePeriod").toString().equalsIgnoreCase("all")))

    	{

    		if(journalHeaderDetails.get("startDate")!=null && journalHeaderDetails.get("endDate")!=null)
    		{
    			log.info("1.if 5 paremeters are given");

    			ZonedDateTime fmDate=ZonedDateTime.parse(journalHeaderDetails.get("startDate").toString());
    			ZonedDateTime toDate=ZonedDateTime.parse(journalHeaderDetails.get("endDate").toString());

    			jHDList=journalsHeaderDataRepository.findByjeTempIdAndLedgerNameAndPeriodAndTenantIdAndJeBatchDateBetween(Long.valueOf(journalHeaderDetails.get("jeTempId").toString()),
    					journalHeaderDetails.get("jeLedger").toString(),journalHeaderDetails.get("jePeriod").toString(),tenantId,fmDate.toLocalDate().plusDays(1),toDate.toLocalDate().plusDays(1));
    		}
    		else
    		{
    			log.info("2.if 3 parameters are given "+journalHeaderDetails);
    			jHDList=journalsHeaderDataRepository.findByjeTempIdAndLedgerNameAndPeriodAndTenantId(Long.valueOf(journalHeaderDetails.get("jeTempId").toString()),
    					journalHeaderDetails.get("jeLedger").toString(),journalHeaderDetails.get("jePeriod").toString(),tenantId);
    		}
    	}
    	else if ((Long.valueOf(journalHeaderDetails.get("jeTempId").toString())== 0) && (journalHeaderDetails.get("jeLedger")!="" && journalHeaderDetails.get("jeLedger")!=null && !journalHeaderDetails.get("jeLedger").toString().equalsIgnoreCase("All")) && 
    			(journalHeaderDetails.get("jePeriod")!=null && journalHeaderDetails.get("jePeriod")!="" && !journalHeaderDetails.get("jePeriod").toString().equalsIgnoreCase("all")))
    	{

    		List<BigInteger> tempIds=templateDetailsRepository.fetchByTenantIdAndActive(tenantId);
    		log.info("tempIds :"+tempIds);
    		if(journalHeaderDetails.get("startDate")!=null && journalHeaderDetails.get("endDate")!=null)
    		{
    			log.info("3.journalHeaderDetails :"+journalHeaderDetails);
    			ZonedDateTime fmDate=ZonedDateTime.parse(journalHeaderDetails.get("startDate").toString());
    			ZonedDateTime toDate=ZonedDateTime.parse(journalHeaderDetails.get("endDate").toString());


    			jHDList=journalsHeaderDataRepository.findByjeTempIdInAndLedgerNameAndPeriodAndTenantIdAndJeBatchDateBetween(tempIds,
    					journalHeaderDetails.get("jeLedger").toString(),journalHeaderDetails.get("jePeriod").toString(),tenantId,fmDate.toLocalDate().plusDays(1),toDate.toLocalDate().plusDays(1));
    		}	
    		else
    		{
    			log.info("3.journalHeaderDetails :"+journalHeaderDetails);

    			jHDList=journalsHeaderDataRepository.findByjeTempIdInAndLedgerNameAndPeriodAndTenantId(tempIds,
    					journalHeaderDetails.get("jeLedger").toString(),journalHeaderDetails.get("jePeriod").toString(),tenantId);
    		}

    	}
    	else if ((Long.valueOf(journalHeaderDetails.get("jeTempId").toString())!= 0) && (journalHeaderDetails.get("jeLedger")=="" || journalHeaderDetails.get("jeLedger")==null || journalHeaderDetails.get("jeLedger").toString().equalsIgnoreCase("All")) && 
    			(journalHeaderDetails.get("jePeriod")!=null && journalHeaderDetails.get("jePeriod")!="" && !journalHeaderDetails.get("jePeriod").toString().equalsIgnoreCase("all")))
    	{


    		if(journalHeaderDetails.get("startDate")!=null && journalHeaderDetails.get("endDate")!=null)
    		{
    			log.info("4.journalHeaderDetails :"+journalHeaderDetails);
    			ZonedDateTime fmDate=ZonedDateTime.parse(journalHeaderDetails.get("startDate").toString());
    			ZonedDateTime toDate=ZonedDateTime.parse(journalHeaderDetails.get("endDate").toString());


    			jHDList=journalsHeaderDataRepository.findByjeTempIdAndPeriodAndTenantIdAndJeBatchDateBetween(Long.valueOf(journalHeaderDetails.get("jeTempId").toString()),
    					journalHeaderDetails.get("jePeriod").toString(),tenantId,fmDate.toLocalDate().plusDays(1),toDate.toLocalDate().plusDays(1));
    		}	
    		else
    		{
    			log.info("4.journalHeaderDetails :"+journalHeaderDetails);

    			jHDList=journalsHeaderDataRepository.findByjeTempIdInAndPeriodAndTenantId(Long.valueOf(journalHeaderDetails.get("jeTempId").toString())
    					,journalHeaderDetails.get("jePeriod").toString(),tenantId);
    		}

    	}
    	else if ((Long.valueOf(journalHeaderDetails.get("jeTempId").toString())== 0) && (journalHeaderDetails.get("jeLedger")!="" && journalHeaderDetails.get("jeLedger")!=null && !journalHeaderDetails.get("jeLedger").toString().equalsIgnoreCase("All")) && 
    			(journalHeaderDetails.get("jePeriod")==null || journalHeaderDetails.get("jePeriod")=="" || journalHeaderDetails.get("jePeriod").toString().equalsIgnoreCase("all")))
    	{

    		List<BigInteger> tempIds=templateDetailsRepository.fetchByTenantIdAndActive(tenantId);
    		log.info("tempIds :"+tempIds);
    		if(journalHeaderDetails.get("startDate")!=null && journalHeaderDetails.get("endDate")!=null)
    		{
    			log.info("5.journalHeaderDetails :"+journalHeaderDetails);
    			ZonedDateTime fmDate=ZonedDateTime.parse(journalHeaderDetails.get("startDate").toString());
    			ZonedDateTime toDate=ZonedDateTime.parse(journalHeaderDetails.get("endDate").toString());


    			jHDList=journalsHeaderDataRepository.findByjeTempIdInAndLedgerNameAndJeBatchDateBetween(tempIds,
    					journalHeaderDetails.get("jeLedger").toString(),tenantId,fmDate.toLocalDate().plusDays(1),toDate.toLocalDate().plusDays(1));
    		}	
    		else
    		{
    			log.info("5.journalHeaderDetails :"+journalHeaderDetails);

    			jHDList=journalsHeaderDataRepository.findByjeTempIdInAndLedgerName(tempIds,
    					journalHeaderDetails.get("jeLedger").toString(),tenantId);
    		}

    	}
    	else if ((Long.valueOf(journalHeaderDetails.get("jeTempId").toString())!= 0) && (journalHeaderDetails.get("jeLedger")=="" || journalHeaderDetails.get("jeLedger")==null || journalHeaderDetails.get("jeLedger").toString().equalsIgnoreCase("All")) && 
    			(journalHeaderDetails.get("jePeriod")==null || journalHeaderDetails.get("jePeriod")=="" || journalHeaderDetails.get("jePeriod").toString().equalsIgnoreCase("all")))
    	{


    		if(journalHeaderDetails.get("startDate")!=null && journalHeaderDetails.get("endDate")!=null)
    		{
    			log.info("6.journalHeaderDetails :"+journalHeaderDetails);
    			ZonedDateTime fmDate=ZonedDateTime.parse(journalHeaderDetails.get("startDate").toString());
    			ZonedDateTime toDate=ZonedDateTime.parse(journalHeaderDetails.get("endDate").toString());


    			jHDList=journalsHeaderDataRepository.findByjeTempIdAndTenantIdAndJeBatchDateBetween(Long.valueOf(journalHeaderDetails.get("jeTempId").toString()),
    					tenantId,fmDate.toLocalDate().plusDays(1),toDate.toLocalDate().plusDays(1));
    		}	
    		else
    		{
    			log.info("6.journalHeaderDetails :"+journalHeaderDetails);

    			jHDList=journalsHeaderDataRepository.findByjeTempIdAndTenantId(Long.valueOf(journalHeaderDetails.get("jeTempId").toString()),tenantId);

    		}

    	}
    	else if ((Long.valueOf(journalHeaderDetails.get("jeTempId").toString())== 0) && (journalHeaderDetails.get("jeLedger")=="" || journalHeaderDetails.get("jeLedger")==null || journalHeaderDetails.get("jeLedger").toString().equalsIgnoreCase("All")) && 
    			(journalHeaderDetails.get("jePeriod")!=null && journalHeaderDetails.get("jePeriod")!="" && !journalHeaderDetails.get("jePeriod").toString().equalsIgnoreCase("all")))
    	{

    		List<BigInteger> tempIds=templateDetailsRepository.fetchByTenantIdAndActive(tenantId);
    		log.info("tempIds :"+tempIds);
    		if(journalHeaderDetails.get("startDate")!=null && journalHeaderDetails.get("endDate")!=null)
    		{
    			log.info("7.journalHeaderDetails :"+journalHeaderDetails);
    			ZonedDateTime fmDate=ZonedDateTime.parse(journalHeaderDetails.get("startDate").toString());
    			ZonedDateTime toDate=ZonedDateTime.parse(journalHeaderDetails.get("endDate").toString());


    			jHDList=journalsHeaderDataRepository.findByjeTempIdInAndPeriodAndTenantIdAndJeBatchDateBetween(tempIds,
    					journalHeaderDetails.get("jePeriod").toString(),tenantId,fmDate.toLocalDate().plusDays(1),toDate.toLocalDate().plusDays(1));
    		}	
    		else
    		{
    			log.info("7.journalHeaderDetails :"+journalHeaderDetails);

    			jHDList=journalsHeaderDataRepository.findByjeTempIdInAndPeriod(tempIds,
    					journalHeaderDetails.get("jePeriod").toString(),tenantId);
    		}

    	}
    	else if ((Long.valueOf(journalHeaderDetails.get("jeTempId").toString())!= 0) && (journalHeaderDetails.get("jeLedger")!="" && journalHeaderDetails.get("jeLedger")!=null && !journalHeaderDetails.get("jeLedger").toString().equalsIgnoreCase("All")) && 
    			(journalHeaderDetails.get("jePeriod")==null || journalHeaderDetails.get("jePeriod")=="" || journalHeaderDetails.get("jePeriod").toString().equalsIgnoreCase("all")))
    	{


    		if(journalHeaderDetails.get("startDate")!=null && journalHeaderDetails.get("endDate")!=null)
    		{
    			log.info("8.journalHeaderDetails :"+journalHeaderDetails);
    			ZonedDateTime fmDate=ZonedDateTime.parse(journalHeaderDetails.get("startDate").toString());
    			ZonedDateTime toDate=ZonedDateTime.parse(journalHeaderDetails.get("endDate").toString());


    			jHDList=journalsHeaderDataRepository.findByjeTempIdAndLedgerNameAndTenantIdAndJeBatchDateBetween(Long.valueOf(journalHeaderDetails.get("jeTempId").toString()),
    					journalHeaderDetails.get("jeLedger").toString(),tenantId,fmDate.toLocalDate().plusDays(1),toDate.toLocalDate().plusDays(1));
    		}	
    		else
    		{
    			log.info("8.journalHeaderDetails :"+journalHeaderDetails);

    			jHDList=journalsHeaderDataRepository.findByjeTempIdAndLedgerNameAndTenantId(Long.valueOf(journalHeaderDetails.get("jeTempId").toString())
    					,journalHeaderDetails.get("jeLedger").toString(),tenantId);
    		}

    	}
    	else 
    	{
    		log.info("9 :in else");
    		List<BigInteger> tempIds=templateDetailsRepository.fetchByTenantIdAndActive(tenantId);
    		log.info("tempIds :"+tempIds);

    		if(journalHeaderDetails.get("startDate")!=null && journalHeaderDetails.get("endDate")!=null)
    		{
    			log.info("9.journalHeaderDetails :"+journalHeaderDetails);
    			ZonedDateTime fmDate=ZonedDateTime.parse(journalHeaderDetails.get("startDate").toString());
    			ZonedDateTime toDate=ZonedDateTime.parse(journalHeaderDetails.get("endDate").toString());


    			jHDList=journalsHeaderDataRepository.findByByJeTempIdInAndTenantIdAndJeBatchDateBetween(tempIds,
    					tenantId,fmDate.toLocalDate().plusDays(1),toDate.toLocalDate().plusDays(1));
    		}

    		else
    		{
    			log.info("9.journalHeaderDetails :"+journalHeaderDetails);
    			jHDList=journalsHeaderDataRepository.findByJeTempIdInAndTenantId(tempIds,tenantId);
    		}
    		log.info("jHDList :"+jHDList);
    	}

    	for(JournalsHeaderData jHD:jHDList)
    	{
    		LinkedHashMap map=new LinkedHashMap();
    		map.put("id", jHD.getId());
    		if(jHD.getJeTempId()!=null)
    		{
    			map.put("jeTempId", jHD.getJeTempId());
    			TemplateDetails temp=templateDetailsRepository.findOne(jHD.getJeTempId());
    			if(temp!=null && temp.getTemplateName()!=null)
    				map.put("templateName", temp.getTemplateName());
    		}
    		map.put("batchName", jHD.getJeBatchName());
    		map.put("batchDate", jHD.getJeBatchDate());
    		map.put("ledger", jHD.getLedgerName());
    		map.put("source", jHD.getSource());
    		map.put("category", jHD.getCategory());
    		map.put("period", jHD.getPeriod());
    		map.put("revDetails", jHD.getRevDetails());
    		BigDecimal drAmount=jeLinesRepository.fetchDebitAmountJeBatchId(jHD.getId());
    		map.put("drAmount", drAmount);
    		BigDecimal crAmount=jeLinesRepository.fetchDebitAmountJeBatchId(jHD.getId());
    		map.put("crAmount", crAmount);
    		map.put("reference", jHD.getJobReference());

    		finalMap.add(map);

    	}
    	finalLhmap.put("map", finalMap);
    	return finalLhmap;

    }*/
    
    
    //after pagination
    
    @PostMapping("/journalHeaderGroupedInfo")
   	@Timed 
 	public LinkedHashMap journalHeaderDetails(HttpServletRequest request,@RequestBody HashMap journalHeaderDetails,@RequestParam(required=false) Integer pageNumber, 
 			@RequestParam(required=false) Integer pageSize,HttpServletResponse response)
    {
    	HashMap map0=userJdbcService.getuserInfoFromToken(request);
      	Long tenantId=Long.parseLong(map0.get("tenantId").toString());
    	log.info("request rest to fetch ledgerNames and periods");


    	List<JournalsHeaderData> jHDList=new ArrayList<JournalsHeaderData>();
    	LinkedHashMap finalLhmap=new LinkedHashMap();
    	List<LinkedHashMap> finalMap=new ArrayList<LinkedHashMap>();
    	if(journalHeaderDetails.get("jeTempId")!=null && !journalHeaderDetails.get("jeTempId").toString().equalsIgnoreCase("0") )
    	{
    		TemplateDetails tempId=templateDetailsRepository.findByTenantIdAndIdForDisplay(tenantId, journalHeaderDetails.get("jeTempId").toString());
    		journalHeaderDetails.put("jeTempId", tempId.getId());
    	}
    	log.info("journalHeaderDetails.get(jeTempId)  :"+journalHeaderDetails.get("jeTempId"));
    	if((journalHeaderDetails.get("jeTempId")!=null && Long.valueOf(journalHeaderDetails.get("jeTempId").toString())!= 0) && (journalHeaderDetails.get("jeLedger")!=null && journalHeaderDetails.get("jeLedger")!="" && !journalHeaderDetails.get("jeLedger").toString().equalsIgnoreCase("All"))&& 
    			(journalHeaderDetails.get("jePeriod")!="" && journalHeaderDetails.get("jePeriod")!=null && !journalHeaderDetails.get("jePeriod").toString().equalsIgnoreCase("all")))

    	{

    		if(journalHeaderDetails.get("startDate")!=null && journalHeaderDetails.get("endDate")!=null)
    		{
    			log.info("1.if 5 paremeters are given");

    			ZonedDateTime fmDate=ZonedDateTime.parse(journalHeaderDetails.get("startDate").toString());
    			ZonedDateTime toDate=ZonedDateTime.parse(journalHeaderDetails.get("endDate").toString());

    			jHDList=journalsHeaderDataRepository.findByjeTempIdAndLedgerNameAndPeriodAndTenantIdAndJeBatchDateBetweenOrderByGlDateDesc(Long.valueOf(journalHeaderDetails.get("jeTempId").toString()),
    					journalHeaderDetails.get("jeLedger").toString(),journalHeaderDetails.get("jePeriod").toString(),tenantId,fmDate.toLocalDate().plusDays(1),toDate.toLocalDate().plusDays(1)/*,columnName*/);
    		}
    		else
    		{
    			log.info("2.if 3 parameters are given "+journalHeaderDetails);
    			jHDList=journalsHeaderDataRepository.findByjeTempIdAndLedgerNameAndPeriodAndTenantIdOrderByGlDateDesc(Long.valueOf(journalHeaderDetails.get("jeTempId").toString()),
    					journalHeaderDetails.get("jeLedger").toString(),journalHeaderDetails.get("jePeriod").toString(),tenantId);
    		}
    	}
    	else if ((Long.valueOf(journalHeaderDetails.get("jeTempId").toString())== 0) && (journalHeaderDetails.get("jeLedger")!="" && journalHeaderDetails.get("jeLedger")!=null && !journalHeaderDetails.get("jeLedger").toString().equalsIgnoreCase("All")) && 
    			(journalHeaderDetails.get("jePeriod")!=null && journalHeaderDetails.get("jePeriod")!="" && !journalHeaderDetails.get("jePeriod").toString().equalsIgnoreCase("all")))
    	{

    		List<BigInteger> tempIds=templateDetailsRepository.fetchByTenantIdAndActive(tenantId);
    		log.info("tempIds :"+tempIds);
    		if(journalHeaderDetails.get("startDate")!=null && journalHeaderDetails.get("endDate")!=null)
    		{
    			log.info("3.journalHeaderDetails :"+journalHeaderDetails);
    			ZonedDateTime fmDate=ZonedDateTime.parse(journalHeaderDetails.get("startDate").toString());
    			ZonedDateTime toDate=ZonedDateTime.parse(journalHeaderDetails.get("endDate").toString());


    			jHDList=journalsHeaderDataRepository.findByjeTempIdInAndLedgerNameAndPeriodAndTenantIdAndJeBatchDateBetween(tempIds,
    					journalHeaderDetails.get("jeLedger").toString(),journalHeaderDetails.get("jePeriod").toString(),tenantId,fmDate.toLocalDate().plusDays(1),toDate.toLocalDate().plusDays(1));
    		}	
    		else
    		{
    			log.info("3.journalHeaderDetails :"+journalHeaderDetails);

    			jHDList=journalsHeaderDataRepository.findByjeTempIdInAndLedgerNameAndPeriodAndTenantId(tempIds,
    					journalHeaderDetails.get("jeLedger").toString(),journalHeaderDetails.get("jePeriod").toString(),tenantId);
    		}

    	}
    	else if ((Long.valueOf(journalHeaderDetails.get("jeTempId").toString())!= 0) && (journalHeaderDetails.get("jeLedger")=="" || journalHeaderDetails.get("jeLedger")==null || journalHeaderDetails.get("jeLedger").toString().equalsIgnoreCase("All")) && 
    			(journalHeaderDetails.get("jePeriod")!=null && journalHeaderDetails.get("jePeriod")!="" && !journalHeaderDetails.get("jePeriod").toString().equalsIgnoreCase("all")))
    	{


    		if(journalHeaderDetails.get("startDate")!=null && journalHeaderDetails.get("endDate")!=null)
    		{
    			log.info("4.journalHeaderDetails :"+journalHeaderDetails);
    			ZonedDateTime fmDate=ZonedDateTime.parse(journalHeaderDetails.get("startDate").toString());
    			ZonedDateTime toDate=ZonedDateTime.parse(journalHeaderDetails.get("endDate").toString());


    			jHDList=journalsHeaderDataRepository.findByjeTempIdAndPeriodAndTenantIdAndJeBatchDateBetweenOrderByGlDateDesc(Long.valueOf(journalHeaderDetails.get("jeTempId").toString()),
    					journalHeaderDetails.get("jePeriod").toString(),tenantId,fmDate.toLocalDate().plusDays(1),toDate.toLocalDate().plusDays(1));
    		}	
    		else
    		{
    			log.info("4.journalHeaderDetails :"+journalHeaderDetails);

    			jHDList=journalsHeaderDataRepository.findByjeTempIdInAndPeriodAndTenantIdOrderByGlDateDesc(Long.valueOf(journalHeaderDetails.get("jeTempId").toString())
    					,journalHeaderDetails.get("jePeriod").toString(),tenantId);
    		}

    	}
    	else if ((Long.valueOf(journalHeaderDetails.get("jeTempId").toString())== 0) && (journalHeaderDetails.get("jeLedger")!="" && journalHeaderDetails.get("jeLedger")!=null && !journalHeaderDetails.get("jeLedger").toString().equalsIgnoreCase("All")) && 
    			(journalHeaderDetails.get("jePeriod")==null || journalHeaderDetails.get("jePeriod")=="" || journalHeaderDetails.get("jePeriod").toString().equalsIgnoreCase("all")))
    	{

    		List<BigInteger> tempIds=templateDetailsRepository.fetchByTenantIdAndActive(tenantId);
    		log.info("tempIds :"+tempIds);
    		if(journalHeaderDetails.get("startDate")!=null && journalHeaderDetails.get("endDate")!=null)
    		{
    			log.info("5.journalHeaderDetails :"+journalHeaderDetails);
    			ZonedDateTime fmDate=ZonedDateTime.parse(journalHeaderDetails.get("startDate").toString());
    			ZonedDateTime toDate=ZonedDateTime.parse(journalHeaderDetails.get("endDate").toString());


    			jHDList=journalsHeaderDataRepository.findByjeTempIdInAndLedgerNameAndJeBatchDateBetween(tempIds,
    					journalHeaderDetails.get("jeLedger").toString(),tenantId,fmDate.toLocalDate().plusDays(1),toDate.toLocalDate().plusDays(1));
    		}	
    		else
    		{
    			log.info("5.journalHeaderDetails :"+journalHeaderDetails);

    			jHDList=journalsHeaderDataRepository.findByjeTempIdInAndLedgerName(tempIds,
    					journalHeaderDetails.get("jeLedger").toString(),tenantId);
    		}

    	}
    	else if ((Long.valueOf(journalHeaderDetails.get("jeTempId").toString())!= 0) && (journalHeaderDetails.get("jeLedger")=="" || journalHeaderDetails.get("jeLedger")==null || journalHeaderDetails.get("jeLedger").toString().equalsIgnoreCase("All")) && 
    			(journalHeaderDetails.get("jePeriod")==null || journalHeaderDetails.get("jePeriod")=="" || journalHeaderDetails.get("jePeriod").toString().equalsIgnoreCase("all")))
    	{


    		if(journalHeaderDetails.get("startDate")!=null && journalHeaderDetails.get("endDate")!=null)
    		{
    			log.info("6.journalHeaderDetails :"+journalHeaderDetails);
    			ZonedDateTime fmDate=ZonedDateTime.parse(journalHeaderDetails.get("startDate").toString());
    			ZonedDateTime toDate=ZonedDateTime.parse(journalHeaderDetails.get("endDate").toString());


    			jHDList=journalsHeaderDataRepository.findByjeTempIdAndTenantIdAndJeBatchDateBetweenOrderByGlDateDesc(Long.valueOf(journalHeaderDetails.get("jeTempId").toString()),
    					tenantId,fmDate.toLocalDate().plusDays(1),toDate.toLocalDate().plusDays(1));
    		}	
    		else
    		{
    			log.info("6.journalHeaderDetails :"+journalHeaderDetails);

    			jHDList=journalsHeaderDataRepository.findByjeTempIdAndTenantIdOrderByGlDateDesc(Long.valueOf(journalHeaderDetails.get("jeTempId").toString()),tenantId);

    		}

    	}
    	else if ((Long.valueOf(journalHeaderDetails.get("jeTempId").toString())== 0) && (journalHeaderDetails.get("jeLedger")=="" || journalHeaderDetails.get("jeLedger")==null || journalHeaderDetails.get("jeLedger").toString().equalsIgnoreCase("All")) && 
    			(journalHeaderDetails.get("jePeriod")!=null && journalHeaderDetails.get("jePeriod")!="" && !journalHeaderDetails.get("jePeriod").toString().equalsIgnoreCase("all")))
    	{

    		List<BigInteger> tempIds=templateDetailsRepository.fetchByTenantIdAndActive(tenantId);
    		log.info("tempIds :"+tempIds);
    		if(journalHeaderDetails.get("startDate")!=null && journalHeaderDetails.get("endDate")!=null)
    		{
    			log.info("7.journalHeaderDetails :"+journalHeaderDetails);
    			ZonedDateTime fmDate=ZonedDateTime.parse(journalHeaderDetails.get("startDate").toString());
    			ZonedDateTime toDate=ZonedDateTime.parse(journalHeaderDetails.get("endDate").toString());


    			jHDList=journalsHeaderDataRepository.findByjeTempIdInAndPeriodAndTenantIdAndJeBatchDateBetween(tempIds,
    					journalHeaderDetails.get("jePeriod").toString(),tenantId,fmDate.toLocalDate().plusDays(1),toDate.toLocalDate().plusDays(1));
    		}	
    		else
    		{
    			log.info("7.journalHeaderDetails :"+journalHeaderDetails);

    			jHDList=journalsHeaderDataRepository.findByjeTempIdInAndPeriod(tempIds,
    					journalHeaderDetails.get("jePeriod").toString(),tenantId);
    		}

    	}
    	else if ((Long.valueOf(journalHeaderDetails.get("jeTempId").toString())!= 0) && (journalHeaderDetails.get("jeLedger")!="" && journalHeaderDetails.get("jeLedger")!=null && !journalHeaderDetails.get("jeLedger").toString().equalsIgnoreCase("All")) && 
    			(journalHeaderDetails.get("jePeriod")==null || journalHeaderDetails.get("jePeriod")=="" || journalHeaderDetails.get("jePeriod").toString().equalsIgnoreCase("all")))
    	{


    		if(journalHeaderDetails.get("startDate")!=null && journalHeaderDetails.get("endDate")!=null)
    		{
    			log.info("8.journalHeaderDetails :"+journalHeaderDetails);
    			ZonedDateTime fmDate=ZonedDateTime.parse(journalHeaderDetails.get("startDate").toString());
    			ZonedDateTime toDate=ZonedDateTime.parse(journalHeaderDetails.get("endDate").toString());


    			jHDList=journalsHeaderDataRepository.findByjeTempIdAndLedgerNameAndTenantIdAndJeBatchDateBetweenOrderByGlDateDesc(Long.valueOf(journalHeaderDetails.get("jeTempId").toString()),
    					journalHeaderDetails.get("jeLedger").toString(),tenantId,fmDate.toLocalDate().plusDays(1),toDate.toLocalDate().plusDays(1));
    		}	
    		else
    		{
    			log.info("8.journalHeaderDetails :"+journalHeaderDetails);

    			jHDList=journalsHeaderDataRepository.findByjeTempIdAndLedgerNameAndTenantIdOrderByGlDateDesc(Long.valueOf(journalHeaderDetails.get("jeTempId").toString())
    					,journalHeaderDetails.get("jeLedger").toString(),tenantId);
    		}

    	}
    	else 
    	{
    		log.info("9 :in else");
    		List<BigInteger> tempIds=templateDetailsRepository.fetchByTenantIdAndActive(tenantId);
    		log.info("tempIds :"+tempIds);

    		if(journalHeaderDetails.get("startDate")!=null && journalHeaderDetails.get("endDate")!=null)
    		{
    			log.info("9.journalHeaderDetails :"+journalHeaderDetails);
    			ZonedDateTime fmDate=ZonedDateTime.parse(journalHeaderDetails.get("startDate").toString());
    			ZonedDateTime toDate=ZonedDateTime.parse(journalHeaderDetails.get("endDate").toString());


    			jHDList=journalsHeaderDataRepository.findByByJeTempIdInAndTenantIdAndJeBatchDateBetween(tempIds,
    					tenantId,fmDate.toLocalDate().plusDays(1),toDate.toLocalDate().plusDays(1));
    		}

    		else
    		{
    			log.info("9.journalHeaderDetails :"+journalHeaderDetails);
    			jHDList=journalsHeaderDataRepository.findByJeTempIdInAndTenantId(tempIds,tenantId);
    		}
    		log.info("jHDList :"+jHDList);
    	}

    	response.addIntHeader("X-COUNT", jHDList.size());

    	//For currency format
    	Properties props = propertiesUtilService.getPropertiesFromClasspath("File.properties");
    	String currencyFormat = props.getProperty("currencyFormat");

    	int limit = 0;
    	if(pageNumber == null || pageNumber == 0)
    	{
    		pageNumber = 0;
    	}
    	if(pageSize == null || pageSize == 0)
    	{
    		pageSize = jHDList.size();
    	}
    	limit = ((pageNumber+1) * pageSize + 1)-1;
    	int startIndex=pageNumber*pageSize; 

    	if(limit>jHDList.size()){
    		limit=jHDList.size();
    	}

    	log.info("Limit Starting Values : "+ limit);
    	log.info("Page Number : "+ pageNumber);



    	for(int j=startIndex;j<limit;j++)
    	{
    		LinkedHashMap map=new LinkedHashMap();
    		map.put("id", jHDList.get(j).getId());
    		if(jHDList.get(j).getJeTempId()!=null)
    		{
    			map.put("jeTempId", jHDList.get(j).getJeTempId());
    			TemplateDetails temp=templateDetailsRepository.findOne(jHDList.get(j).getJeTempId());
    			if(temp!=null && temp.getTemplateName()!=null)
    				map.put("templateName", temp.getTemplateName());
    		}
    		map.put("glDate", jHDList.get(j).getGlDate());
    		map.put("batchName", jHDList.get(j).getJeBatchName());
    		map.put("batchDate", jHDList.get(j).getJeBatchDate());
    		map.put("ledger", jHDList.get(j).getLedgerName());
    		map.put("source", jHDList.get(j).getSource());
    		map.put("category", jHDList.get(j).getCategory());
    		map.put("period", jHDList.get(j).getPeriod());
    		map.put("revDetails", jHDList.get(j).getRevDetails());
    		BigDecimal drAmount=jeLinesRepository.fetchDebitAmountJeHeaderId(jHDList.get(j).getId());
    		if(drAmount!=null)
    			map.put("drAmount", drAmount);
    		else
    			map.put("drAmount","");
    		BigDecimal crAmount=jeLinesRepository.fetchDebitAmountJeHeaderId(jHDList.get(j).getId());
    		if(crAmount!=null)
    			map.put("crAmount",crAmount);
    		else
    			map.put("crAmount", "");
    		map.put("reference", jHDList.get(j).getJobReference());

    		finalMap.add(map);

    	}
    	
    	
    	
    	
    	finalLhmap.put("map", finalMap);
    	return finalLhmap;

    }
 
    
    
    @PostMapping("/journalHeaderDetailOrSummaryInfoBKP")
   	@Timed 
 	public LinkedHashMap journalHeaderAndLineDetailInformationBKP(@RequestParam Long batchId,@RequestParam(value = "desc", required = false) String desc,HttpServletResponse response,@RequestParam(value = "page" , required = false) Integer offset,
			@RequestParam(value = "size", required = false) Integer limit) throws URISyntaxException 
    {
    	log.info("Rest Request to get line level information");
    	Properties props = propertiesUtilService.getPropertiesFromClasspath("File.properties");
    	String currencyFormat = props.getProperty("currencyFormat");
    	LinkedHashMap finalMap=new LinkedHashMap();


    	JournalsHeaderData jHd=journalsHeaderDataRepository.findOne(batchId);
    	finalMap.put("batchName", jHd.getJeBatchName());
    	finalMap.put("glDate", jHd.getGlDate());
    	finalMap.put("source", jHd.getSource());
    	if(jHd.getConversionType()!=null && !jHd.getConversionType().isEmpty())
    	{
    		FxRates fxRates=fxRatesRepository.findOne(Long.valueOf(jHd.getConversionType()));
    		if(fxRates!=null)
    	finalMap.put("conversionType", fxRates.getConversionType());
    	}
    	else
    		finalMap.put("conversionType", "");
    	finalMap.put("ledger",jHd.getLedgerName());
    	finalMap.put("period", jHd.getPeriod());
    	finalMap.put("category", jHd.getCategory());
    	List<LinkedHashMap> attributeNames=new ArrayList<LinkedHashMap>();
    	List<LinkedHashMap> jelineDetailsList=new ArrayList<LinkedHashMap>();
    	
    	
		List<JeLdrDetails> jeLdrDetailsColumns=jeLdrDetailsRepository.findByJeTempId(jHd.getJeTempId());
		for(JeLdrDetails jeLdrDetails:jeLdrDetailsColumns)
		{
			if(jeLdrDetails.getColType().equalsIgnoreCase("System")){
				LookUpCode lookUpCode=lookUpCodeRepository.findOne(jeLdrDetails.getColValue());

				LinkedHashMap attributeName=new LinkedHashMap();
				attributeName.put("field", lookUpCode.getMeaning().substring(0, 1).toLowerCase() + lookUpCode.getMeaning().substring(1).replaceAll(" ", ""));
				attributeName.put( "header", lookUpCode.getMeaning());
				if(lookUpCode.getMeaning().equalsIgnoreCase("Line"))
					attributeName.put("align", "center");
				if(lookUpCode.getMeaning().equalsIgnoreCase("Accounted Credit") || lookUpCode.getMeaning().equalsIgnoreCase("Accounted Debit") || 
						lookUpCode.getMeaning().equalsIgnoreCase("Entered Debit") || lookUpCode.getMeaning().equalsIgnoreCase("Entered Credit"))
					attributeName.put("align", "right");
				else
					attributeName.put("align", "left");
				
				attributeNames.add(attributeName);
				
			}
			else if(jeLdrDetails.getColType().equalsIgnoreCase("Line"))
			{
				log.info("in else");
				DataViewsColumns dvc=dataViewsColumnsRepository.findOne(jeLdrDetails.getColValue());

				LinkedHashMap attributeName=new LinkedHashMap();
				attributeName.put("field", dvc.getColumnName().substring(0, 1).toLowerCase() + dvc.getColumnName().substring(1).replaceAll(" ", ""));
				attributeName.put("header", dvc.getColumnName());
				if(dvc.getColDataType().equalsIgnoreCase("DATE"))
					attributeName.put("align", "center");
				else if(dvc.getColDataType().equalsIgnoreCase("DECIMAL"))
					attributeName.put("align", "right");
				else
					attributeName.put("align", "left");
				attributeNames.add(attributeName);
			}
		}
    	
    	
    	
    	if(desc!=null && desc.equalsIgnoreCase("Details"))
    	{
    		log.info("if api to fetch Details of records");
    		Page<JeLines> jeLinesList=null;

    		List<JeLines> jeLinesListCount=jeLinesRepository.findByJeHeaderId(batchId);
    		response.addIntHeader("X-COUNT", jeLinesListCount.size());
    		int Count =0;
    		
    		
    		PaginationUtil paginationUtil=new PaginationUtil();
    		
    		int maxlmt=paginationUtil.MAX_LIMIT;
    		int minlmt=paginationUtil.MIN_OFFSET;
    		log.info("maxlmt: "+maxlmt);
    	
    		HttpHeaders headers = null;
    		
    		if(limit==null || limit<minlmt){
    			jeLinesListCount=jeLinesRepository.findByJeHeaderId(batchId);
    			limit = jeLinesListCount.size();
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
    			jeLinesList = jeLinesRepository.findByJeHeaderId(batchId,PaginationUtil.generatePageRequest2(offset, limit));
    			headers = PaginationUtil.generatePaginationHttpHeaders2(jeLinesList, "/api/journalHeaderDetailOrSummaryInfoBKP",offset, limit);
    		}
    		else{
    			log.info("input limit is within maxlimit");
    			jeLinesList = jeLinesRepository.findByJeHeaderId(batchId,PaginationUtil.generatePageRequest(offset, limit));
    			headers = PaginationUtil.generatePaginationHttpHeaderss(jeLinesList, "/api/journalHeaderDetailOrSummaryInfoBKP", offset, limit);
    		}
    		
    		


    		for(int i=0;i<jeLinesList.getContent().size();i++)
    		{
    			LinkedHashMap jelineDetails=new LinkedHashMap();
    			Count =Count+1;
    			jelineDetails.put("line", Count);
    			jelineDetails.put("codeCombination", jeLinesList.getContent().get(i).getCodeCombination());
    			if(jeLinesList.getContent().get(i).getAccountedDebit()!=null)
    			{
    			//String actDeb = reconciliationResultService.getAmountInFormat(jeLinesList.getContent().get(i).getAccountedDebit().toString(),currencyFormat);
    			jelineDetails.put("accountedDebit", jeLinesList.getContent().get(i).getAccountedDebit());
    			}
    			else
    				jelineDetails.put("accountedDebit", 0d);
    			if(jeLinesList.getContent().get(i).getAccountedCredit()!=null)
    			{
		    	//String actCred = reconciliationResultService.getAmountInFormat(jeLinesList.getContent().get(i).getAccountedCredit().toString(),currencyFormat);
    			jelineDetails.put("accountedCredit", jeLinesList.getContent().get(i).getAccountedCredit());
    			}
    			else
        			jelineDetails.put("accountedCredit", 0d);

    		  // jelineDetails.put("currency", jeLinesList.getContent().get(i).getCurrency());
    			List<JeLdrDetails> jeLdrDetailsList=jeLdrDetailsRepository.findByJeTempIdAndRefColumnNotNull(jHd.getJeTempId());
    		
    			
    			for(JeLdrDetails jeLdrDetails:jeLdrDetailsList)
    			{

    				if(jeLdrDetails.getColType().equalsIgnoreCase("System")){
    					LookUpCode lookUpCode=lookUpCodeRepository.findOne(jeLdrDetails.getColValue());

    					if(jeLdrDetails.getRefColumn()==1)
    						jelineDetails.put(lookUpCode.getMeaning().substring(0, 1).toLowerCase() + lookUpCode.getMeaning().substring(1).replaceAll(" ", ""), jeLinesList.getContent().get(i).getAttributeRef1());
    					if(jeLdrDetails.getRefColumn()==2)
    						jelineDetails.put(lookUpCode.getMeaning().substring(0, 1).toLowerCase() + lookUpCode.getMeaning().substring(1).replaceAll(" ", ""), jeLinesList.getContent().get(i).getAttributeRef2());
    					if(jeLdrDetails.getRefColumn()==3)
    						jelineDetails.put(lookUpCode.getMeaning().substring(0, 1).toLowerCase() + lookUpCode.getMeaning().substring(1).replaceAll(" ", ""), jeLinesList.getContent().get(i).getAttributeRef3());
    					if(jeLdrDetails.getRefColumn()==4)
    						jelineDetails.put(lookUpCode.getMeaning().substring(0, 1).toLowerCase() + lookUpCode.getMeaning().substring(1).replaceAll(" ", ""), jeLinesList.getContent().get(i).getAttributeRef4());
    					if(jeLdrDetails.getRefColumn()==5)
    						jelineDetails.put(lookUpCode.getMeaning().substring(0, 1).toLowerCase() + lookUpCode.getMeaning().substring(1).replaceAll(" ", ""), jeLinesList.getContent().get(i).getAttributeRef5());
    					if(jeLdrDetails.getRefColumn()==6)
    						jelineDetails.put(lookUpCode.getMeaning().substring(0, 1).toLowerCase() + lookUpCode.getMeaning().substring(1).replaceAll(" ", ""), jeLinesList.getContent().get(i).getAttributeRef6());

    				}
    				else if(jeLdrDetails.getColType().equalsIgnoreCase("Line"))
    				{
    					DataViewsColumns dvc=dataViewsColumnsRepository.findOne(jeLdrDetails.getColValue());

    					if(jeLdrDetails.getRefColumn()==1)
    						jelineDetails.put(dvc.getColumnName().substring(0, 1).toLowerCase() + dvc.getColumnName().substring(1).replaceAll(" ", ""), jeLinesList.getContent().get(i).getAttributeRef1());
    					if(jeLdrDetails.getRefColumn()==2)
    						jelineDetails.put(dvc.getColumnName().substring(0, 1).toLowerCase() + dvc.getColumnName().substring(1).replaceAll(" ", ""), jeLinesList.getContent().get(i).getAttributeRef2());
    					if(jeLdrDetails.getRefColumn()==3)
    						jelineDetails.put(dvc.getColumnName().substring(0, 1).toLowerCase() + dvc.getColumnName().substring(1).replaceAll(" ", ""), jeLinesList.getContent().get(i).getAttributeRef3());
    					if(jeLdrDetails.getRefColumn()==4)
    						jelineDetails.put(dvc.getColumnName().substring(0, 1).toLowerCase() + dvc.getColumnName().substring(1).replaceAll(" ", ""), jeLinesList.getContent().get(i).getAttributeRef4());
    					if(jeLdrDetails.getRefColumn()==5)
    						jelineDetails.put(dvc.getColumnName().substring(0, 1).toLowerCase() + dvc.getColumnName().substring(1).replaceAll(" ", ""), jeLinesList.getContent().get(i).getAttributeRef5());
    					if(jeLdrDetails.getRefColumn()==6)
    						jelineDetails.put(dvc.getColumnName().substring(0, 1).toLowerCase() + dvc.getColumnName().substring(1).replaceAll(" ", ""), jeLinesList.getContent().get(i).getAttributeRef6());
    				}
    			}
    			
    			if(jeLinesList.getContent().get(i).getDebitAmount()!=null)
    			{
    			//String deb = reconciliationResultService.getAmountInFormat(jeLinesList.getContent().get(i).getDebitAmount().toString(),currencyFormat);
    			jelineDetails.put("enteredDebit", jeLinesList.getContent().get(i).getDebitAmount());
    			}
    			else
    				jelineDetails.put("enteredDebit", 0d);
    			if(jeLinesList.getContent().get(i).getCreditAmount()!=null)
    			{
		    	//String cred = reconciliationResultService.getAmountInFormat(jeLinesList.getContent().get(i).getCreditAmount().toString(),currencyFormat);
    			jelineDetails.put("enteredCredit", jeLinesList.getContent().get(i).getCreditAmount());
    			}
    			else
        			jelineDetails.put("enteredCredit", 0d);
    			
    			/*jelineDetails.put("enteredDebit", jeLinesList.getContent().get(i).getDebitAmount());
    			jelineDetails.put("enteredCredit", jeLinesList.getContent().get(i).getCreditAmount());*/
    			jelineDetailsList.add(jelineDetails);
    		}
    		finalMap.put("jelineDetailsList", jelineDetailsList);
    	}
    	else
    	{
   /* 		String dbUrl=env.getProperty("spring.datasource.url");
    		String[] parts=dbUrl.split("[\\s@&?$+-]+");
    		String host = parts[0].split("/")[2].split(":")[0];
    		log.info("host :"+host);
    		String schemaName=parts[0].split("/")[3];
    		log.info("schemaName :"+schemaName);
    		String userName = env.getProperty("spring.datasource.username");
    		String password = env.getProperty("spring.datasource.password");*/
    		
    		Connection conn = null;
    		Statement stmt = null;
    		Statement stmtCt = null;
    		//conn = DriverManager.getConnection(dbUrl, userName, password);
    		DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
			
    		ResultSet result2=null;
    		ResultSet resultCount=null;
    		log.info("if api to fetch Summary of records");
    		List<JeLdrDetails> jeLdrDetailsList=jeLdrDetailsRepository.findByJeTempIdAndRefColumnNotNull(jHd.getJeTempId());
    		int Count=0;

    		String subQry1="select code_combination,round(sum(accounted_debit),2) as actDeb,round(sum(accounted_credit),2) as actCred,round(sum(debit_amount),2) as deb,round(sum(credit_amount),2) as cred,";
    		String subQry2=" from t_je_lines  where je_header_id="+batchId+" group by code_combination,";
    		String subQry3="";
    		HashMap attrbuteMap=new HashMap();
    		for(JeLdrDetails jeLdrDetails:jeLdrDetailsList)
    		{
    			subQry3=subQry3+"attribute_Ref_"+jeLdrDetails.getRefColumn()+",";

    			if(jeLdrDetails.getColType().equalsIgnoreCase("System")){
    				LookUpCode lookUpCode=lookUpCodeRepository.findOne(jeLdrDetails.getColValue());
    				

    				if(jeLdrDetails.getRefColumn()==1)
    					attrbuteMap.put("attribute_Ref_"+jeLdrDetails.getRefColumn(),lookUpCode.getMeaning());
    				if(jeLdrDetails.getRefColumn()==2)
    					attrbuteMap.put("attribute_Ref_"+jeLdrDetails.getRefColumn(),lookUpCode.getMeaning());
    				if(jeLdrDetails.getRefColumn()==3)
    					attrbuteMap.put("attribute_Ref_"+jeLdrDetails.getRefColumn(),lookUpCode.getMeaning());
    				if(jeLdrDetails.getRefColumn()==4)
    					attrbuteMap.put("attribute_Ref_"+jeLdrDetails.getRefColumn(),lookUpCode.getMeaning());
    				if(jeLdrDetails.getRefColumn()==5)
    					attrbuteMap.put("attribute_Ref_"+jeLdrDetails.getRefColumn(),lookUpCode.getMeaning());
    				if(jeLdrDetails.getRefColumn()==6)
    					attrbuteMap.put("attribute_Ref_"+jeLdrDetails.getRefColumn(),lookUpCode.getMeaning());

    			}
    			else if(jeLdrDetails.getColType().equalsIgnoreCase("Line"))
    			{
    				DataViewsColumns dvc=dataViewsColumnsRepository.findOne(jeLdrDetails.getColValue());
    				
    				/*if(dvc.getColumnName().equalsIgnoreCase("currency"))
    				{
    					subQry3=subQry3+"currency,";
    				}*/
    				if(jeLdrDetails.getRefColumn()==1)
    					attrbuteMap.put("attribute_Ref_"+jeLdrDetails.getRefColumn(),dvc.getColumnName());
    				if(jeLdrDetails.getRefColumn()==2)
    					attrbuteMap.put("attribute_Ref_"+jeLdrDetails.getRefColumn(),dvc.getColumnName());
    				if(jeLdrDetails.getRefColumn()==3)
    					attrbuteMap.put("attribute_Ref_"+jeLdrDetails.getRefColumn(),dvc.getColumnName());
    				if(jeLdrDetails.getRefColumn()==4)
    					attrbuteMap.put("attribute_Ref_"+jeLdrDetails.getRefColumn(),dvc.getColumnName());
    				if(jeLdrDetails.getRefColumn()==5)
    					attrbuteMap.put("attribute_Ref_"+jeLdrDetails.getRefColumn(),dvc.getColumnName());
    				if(jeLdrDetails.getRefColumn()==6)
    					attrbuteMap.put("attribute_Ref_"+jeLdrDetails.getRefColumn(),dvc.getColumnName());
    				
    			}

    		}
    		log.info("attrbuteMap :"+attrbuteMap);
    		log.info("subQry3 :"+subQry3);
    		
    		String finalQuery="";
    		if(subQry3!=null && !subQry3.isEmpty())
    		{
    		String subque=subQry3.substring(0, subQry3.length() - 1);
    		log.info("subQry3 :"+subque);
    		finalQuery=subQry1+subque+subQry2+subque;
    		log.info("QUERY:"+subQry1+subque+subQry2+subque);
    		}
    		else
    		{

        		String subQry4="select code_combination,round(sum(accounted_debit),2) as actDeb,round(sum(accounted_credit),2) as actCred,round(sum(debit_amount),2) as deb,round(sum(credit_amount),2) as cred";
        		String subQry5=" from t_je_lines  where je_header_id="+batchId+" group by code_combination";
        		finalQuery=subQry4+subQry5;
        		log.info("QUERY:"+finalQuery);
    		}

    		
    		log.info("offset :"+offset +"and limit :"+limit);
    		Integer pageNumber=offset;
    		Integer pageSize=limit;
    		log.info("pageNumber :"+pageNumber +"and pageSize :"+pageSize);
    		String finalPagQuery=finalQuery+" Limit "+(pageNumber-1) * pageSize+", "+pageSize;
    		log.info("Final pagination QUERY:"+finalPagQuery);
    		try
    		{
    			conn = ds.getConnection();
        		stmt = conn.createStatement();
        		stmtCt = conn.createStatement();
    		result2=stmt.executeQuery(finalPagQuery);
    		log.info("****query executed*****");
    		ResultSetMetaData rsmd2 = result2.getMetaData();
    		int columnCount = rsmd2.getColumnCount();
    		Boolean currency=false;
    		
    		resultCount=stmtCt.executeQuery(finalQuery);
    		int totalCount=0;
    		while(resultCount.next())
    		{
    			totalCount =totalCount+1;
    		}
    		  
    		/*   
    		    for (int i = 1; i <= columnCount; i++) {
    		        if(rsmd2.getColumnName(i).equalsIgnoreCase("currency")) {
    		        	currency=true;
    		 
    		        }*/
    		 
    		   
    		
    		int count=0;
    		while(result2.next())
    		{
    			LinkedHashMap map=new LinkedHashMap();
    			Count =Count+1;
    			map.put("line", Count);
    			for (int i = 1; i <= columnCount; i++ ) {
    				String name = rsmd2.getColumnName(i); 

    				//log.info("result2.getString(code_combination) :"+result2.getString("code_combination"));
    				//log.info("result2.getString(currency) :"+result2.getString("currency"));
    				map.put("tempId", jHd.getJeTempId());
    				map.put("codeCombination", result2.getString("code_combination"));

    				/*if(name.equalsIgnoreCase("currency"))
    					map.put("currency", result2.getString("currency"));*/

    				if(result2.getString("actDeb")!=null)
    				{
    					//String actDeb = reconciliationResultService.getAmountInFormat(result2.getString("actDeb"),currencyFormat);
    					map.put("accountedDebit", Double.valueOf(result2.getString("actDeb")));
    				}
    				else
    					map.put("accountedDebit", 0d);
    				if(result2.getString("actCred")!=null)
    				{
    					//String actCred = reconciliationResultService.getAmountInFormat(result2.getString("actCred"),currencyFormat);
    					map.put("accountedCredit", Double.valueOf(result2.getString("actCred")));
    				}
    				else
    					map.put("accountedCredit", 0d);

    				if(name.equalsIgnoreCase("attribute_Ref_1"))
    				{
    					//if(attrbuteMap.get("attribute_Ref_1").toString().equalsIgnoreCase("currency"))
    					{
    						map.put("refColumn1", attrbuteMap.get("attribute_Ref_1").toString().substring(0, 1).toLowerCase()+ attrbuteMap.get("attribute_Ref_1").toString().substring(1).replaceAll(" ", ""));
    						map.put(attrbuteMap.get("attribute_Ref_1").toString().substring(0, 1).toLowerCase() + attrbuteMap.get("attribute_Ref_1").toString().substring(1).replaceAll(" ", ""), result2.getString("attribute_Ref_1"));
    					}
    				}
    				if(name.equalsIgnoreCase("attribute_Ref_2"))
    				{
    					//if(attrbuteMap.get("attribute_Ref_2").toString().equalsIgnoreCase("currency"))
    					{
    						map.put("refColumn2", attrbuteMap.get("attribute_Ref_2").toString().substring(0, 1).toLowerCase()+ attrbuteMap.get("attribute_Ref_2").toString().substring(1).replaceAll(" ", ""));
    						map.put(attrbuteMap.get("attribute_Ref_2").toString().substring(0, 1).toLowerCase() + attrbuteMap.get("attribute_Ref_2").toString().substring(1).replaceAll(" ", ""), result2.getString("attribute_Ref_2"));
    					}
    				}
    				if(name.equalsIgnoreCase("attribute_Ref_3"))
    				{
    					//if(!attrbuteMap.get("attribute_Ref_3").toString().equalsIgnoreCase("currency"))
    					{
    						map.put("refColumn3", attrbuteMap.get("attribute_Ref_3").toString().substring(0, 1).toLowerCase()+ attrbuteMap.get("attribute_Ref_3").toString().substring(1).replaceAll(" ", ""));
    						map.put(attrbuteMap.get("attribute_Ref_3").toString().substring(0, 1).toLowerCase() + attrbuteMap.get("attribute_Ref_3").toString().substring(1).replaceAll(" ", ""), result2.getString("attribute_Ref_3"));
    					}
    				}
    				if(name.equalsIgnoreCase("attribute_Ref_4"))
    				{
    					//if(!attrbuteMap.get("attribute_Ref_4").toString().equalsIgnoreCase("currency"))
    					{
    						map.put("refColumn4", attrbuteMap.get("attribute_Ref_4").toString().substring(0, 1).toLowerCase()+ attrbuteMap.get("attribute_Ref_4").toString().substring(1).replaceAll(" ", ""));
    						map.put(attrbuteMap.get("attribute_Ref_4").toString().substring(0, 1).toLowerCase() + attrbuteMap.get("attribute_Ref_4").toString().substring(1).replaceAll(" ", ""), result2.getString("attribute_Ref_4"));
    					}
    				}
    				if(name.equalsIgnoreCase("attribute_Ref_5"))
    				{
    					//if(!attrbuteMap.get("attribute_Ref_5").toString().equalsIgnoreCase("currency"))
    					{
    						map.put("refColumn5", attrbuteMap.get("attribute_Ref_5").toString().substring(0, 1).toLowerCase()+ attrbuteMap.get("attribute_Ref_5").toString().substring(1).replaceAll(" ", ""));
    						map.put(attrbuteMap.get("attribute_Ref_5").toString().substring(0, 1).toLowerCase() + attrbuteMap.get("attribute_Ref_5").toString().substring(1).replaceAll(" ", ""), result2.getString("attribute_Ref_5"));
    					}
    				}
    				if(name.equalsIgnoreCase("attribute_Ref_6"))
    				{
    					//if(!attrbuteMap.get("attribute_Ref_6").toString().equalsIgnoreCase("currency"))
    					{
    						map.put("refColumn6", attrbuteMap.get("attribute_Ref_6").toString().substring(0, 1).toLowerCase()+ attrbuteMap.get("attribute_Ref_6").toString().substring(1).replaceAll(" ", ""));
    						map.put(attrbuteMap.get("attribute_Ref_6").toString().substring(0, 1).toLowerCase() + attrbuteMap.get("attribute_Ref_6").toString().substring(1).replaceAll(" ", ""), result2.getString("attribute_Ref_6"));
    					}
    				}

    				if(result2.getString("deb")!=null)
    				{
    					//String deb = reconciliationResultService.getAmountInFormat(result2.getString("deb"),currencyFormat);
    					map.put("enteredDebit", Double.valueOf(result2.getString("deb")));
    				}
    				else
    					map.put("enteredDebit", 0d);
    				if(result2.getString("cred")!=null)
    				{
    					//String cred = reconciliationResultService.getAmountInFormat(result2.getString("cred"),currencyFormat);
    					map.put("enteredCredit",Double.valueOf(result2.getString("cred")));
    				}
    				else
    					map.put("enteredCredit",0d);

    			}
    			jelineDetailsList.add(map);
    		}
    		response.addIntHeader("X-COUNT", totalCount);
    		}
    		catch(Exception e)
    		{
    			
    		}

    		finally
    		{
    		if(result2!=null)
				try {
					result2.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		if(resultCount!=null)
				try {
					resultCount.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		if(stmt!=null)
				try {
					stmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		if(stmtCt!=null)
				try {
					stmtCt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		if(conn!=null)
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}

    	

    	}
    	log.info("jelineDetailsListsize :"+jelineDetailsList.size());
    	finalMap.put("columnsWithAttributeNames",attributeNames);
    	finalMap.put("jelineDetailsList", jelineDetailsList);

    	
    	return finalMap;


    }
    
    
    /**
     * author:ravali
     * Desc:Summary or a detail information of a batch
     * @param batchId
     * @param desc
     * @return
     * @throws SQLException
     * @throws URISyntaxException 
     */
    /**current working**/
    
    @PostMapping("/journalHeaderDetailOrSummaryInfo")
   	@Timed 
 	public LinkedHashMap journalHeaderAndLineDetailInformation(@RequestParam Long batchId,@RequestParam(value = "desc", required = false) String desc,HttpServletResponse response,@RequestParam(value = "page" , required = false) Integer offset,
			@RequestParam(value = "size", required = false) Integer limit) throws URISyntaxException 
    {
    	log.info("Rest Request to get line level information");

    	LinkedHashMap finalMap=new LinkedHashMap();


    	JournalsHeaderData jHd=journalsHeaderDataRepository.findOne(batchId);
    	finalMap.put("batchName", jHd.getJeBatchName());
    	finalMap.put("glDate", jHd.getGlDate());
    	finalMap.put("source", jHd.getSource());
    	if(jHd.getConversionType()!=null && !jHd.getConversionType().isEmpty())
    	{
    		FxRates fxRates=fxRatesRepository.findOne(Long.valueOf(jHd.getConversionType()));
    		if(fxRates!=null)
    			finalMap.put("conversionType", fxRates.getConversionType());
    	}
    	else
    		finalMap.put("conversionType", "");
    	finalMap.put("ledger",jHd.getLedgerName());
    	finalMap.put("period", jHd.getPeriod());
    	finalMap.put("category", jHd.getCategory());
    	List<LinkedHashMap> attributeNames=new ArrayList<LinkedHashMap>();
    	List<LinkedHashMap> jelineDetailsList=new ArrayList<LinkedHashMap>();


    	//List<JeLdrDetails> jeLdrDetailsColumns=jeLdrDetailsRepository.findByJeTempId(jHd.getJeTempId());
    //	for(JeLdrDetails jeLdrDetails:jeLdrDetailsColumns)
    //	{
    	List<JeLines> jeLinesListForColumns=jeLinesRepository.findByJeHeaderId(batchId);
    	
    	List<String> fixedLines=new ArrayList<String>();
    	fixedLines.add("Line");
    	fixedLines.add("Code Combination");
    	fixedLines.add("Accounted Debit");
    	fixedLines.add("Accounted Credit");
    	fixedLines.add("Entered Debit");
    	fixedLines.add("Entered Credit");
    	for(int i=0;i<fixedLines.size();i++)
    	{

			//LookUpCode lookUpCode=lookUpCodeRepository.findOne(jeLdrDetails.getColValue());

			LinkedHashMap attributeName=new LinkedHashMap();
			attributeName.put("field", fixedLines.get(i).substring(0, 1).toLowerCase() + fixedLines.get(i).substring(1).replaceAll(" ", ""));
			attributeName.put( "header", fixedLines.get(i));
			if(fixedLines.get(i).equalsIgnoreCase("Line"))
				attributeName.put("align", "center");
			if(fixedLines.get(i).equalsIgnoreCase("Accounted Credit") || fixedLines.get(i).equalsIgnoreCase("Accounted Debit") || 
					fixedLines.get(i).equalsIgnoreCase("Entered Debit") || fixedLines.get(i).equalsIgnoreCase("Entered Credit"))
				attributeName.put("align", "right");
			else
				attributeName.put("align", "left");

			attributeNames.add(attributeName);

		
    	}
    	if(jeLinesListForColumns.get(0).getAttrRef1Type()!=null)
    	{
    		LinkedHashMap attributeName=new LinkedHashMap();
			if(jeLinesListForColumns.get(0).getAttrRef1Type().split("_")[0].equalsIgnoreCase("LookUp")){
				LookUpCode lookUpCode=lookUpCodeRepository.findOne(Long.valueOf(jeLinesListForColumns.get(0).getAttrRef1Type().split("_")[1]));
				attributeName.put("field", lookUpCode.getMeaning().substring(0, 1).toLowerCase() + lookUpCode.getMeaning().substring(1).replaceAll(" ", ""));
				attributeName.put( "header", lookUpCode.getMeaning());
					attributeName.put("align", "left");

				attributeNames.add(attributeName);
			}
			else if(jeLinesListForColumns.get(0).getAttrRef1Type().split("_")[0].equalsIgnoreCase("column"))
			{
				DataViewsColumns dvc=dataViewsColumnsRepository.findOne(Long.valueOf(jeLinesListForColumns.get(0).getAttrRef1Type().split("_")[1]));
				attributeName.put("field", dvc.getColumnName().substring(0, 1).toLowerCase() + dvc.getColumnName().substring(1).replaceAll(" ", ""));
				attributeName.put( "header", dvc.getColumnName());
				if(dvc.getColDataType().equalsIgnoreCase("DATE"))
    				attributeName.put("align", "center");
    			else if(dvc.getColDataType().equalsIgnoreCase("DECIMAL"))
    				attributeName.put("align", "right");
    			else
    				attributeName.put("align", "left");
    			attributeNames.add(attributeName);
			}
		}
     	if(jeLinesListForColumns.get(0).getAttrRef2Type()!=null)
    	{
    		LinkedHashMap attributeName=new LinkedHashMap();
			if(jeLinesListForColumns.get(0).getAttrRef2Type().split("_")[0].equalsIgnoreCase("LookUp")){
				LookUpCode lookUpCode=lookUpCodeRepository.findOne(Long.valueOf(jeLinesListForColumns.get(0).getAttrRef2Type().split("_")[1]));
				attributeName.put("field", lookUpCode.getMeaning().substring(0, 1).toLowerCase() + lookUpCode.getMeaning().substring(1).replaceAll(" ", ""));
				attributeName.put( "header", lookUpCode.getMeaning());
					attributeName.put("align", "left");

				attributeNames.add(attributeName);
			}
			else if(jeLinesListForColumns.get(0).getAttrRef2Type().split("_")[0].equalsIgnoreCase("column"))
			{
				DataViewsColumns dvc=dataViewsColumnsRepository.findOne(Long.valueOf(jeLinesListForColumns.get(0).getAttrRef2Type().split("_")[1]));
				attributeName.put("field", dvc.getColumnName().substring(0, 1).toLowerCase() + dvc.getColumnName().substring(1).replaceAll(" ", ""));
				attributeName.put( "header", dvc.getColumnName());
				if(dvc.getColDataType().equalsIgnoreCase("DATE"))
    				attributeName.put("align", "center");
    			else if(dvc.getColDataType().equalsIgnoreCase("DECIMAL"))
    				attributeName.put("align", "right");
    			else
    				attributeName.put("align", "left");
    			attributeNames.add(attributeName);
			}
		}
     	if(jeLinesListForColumns.get(0).getAttrRef3Type()!=null)
    	{
    		LinkedHashMap attributeName=new LinkedHashMap();
			if(jeLinesListForColumns.get(0).getAttrRef3Type().split("_")[0].equalsIgnoreCase("LookUp")){
				LookUpCode lookUpCode=lookUpCodeRepository.findOne(Long.valueOf(jeLinesListForColumns.get(0).getAttrRef3Type().split("_")[1]));
				attributeName.put("field", lookUpCode.getMeaning().substring(0, 1).toLowerCase() + lookUpCode.getMeaning().substring(1).replaceAll(" ", ""));
				attributeName.put( "header", lookUpCode.getMeaning());
					attributeName.put("align", "left");

				attributeNames.add(attributeName);
			}
			else if(jeLinesListForColumns.get(0).getAttrRef3Type().split("_")[0].equalsIgnoreCase("column"))
			{
				DataViewsColumns dvc=dataViewsColumnsRepository.findOne(Long.valueOf(jeLinesListForColumns.get(0).getAttrRef3Type().split("_")[1]));
				attributeName.put("field", dvc.getColumnName().substring(0, 1).toLowerCase() + dvc.getColumnName().substring(1).replaceAll(" ", ""));
				attributeName.put( "header", dvc.getColumnName());
				if(dvc.getColDataType().equalsIgnoreCase("DATE"))
    				attributeName.put("align", "center");
    			else if(dvc.getColDataType().equalsIgnoreCase("DECIMAL"))
    				attributeName.put("align", "right");
    			else
    				attributeName.put("align", "left");
    			attributeNames.add(attributeName);
			}
		}
     	if(jeLinesListForColumns.get(0).getAttrRef4Type()!=null)
    	{
    		LinkedHashMap attributeName=new LinkedHashMap();
			if(jeLinesListForColumns.get(0).getAttrRef4Type().split("_")[0].equalsIgnoreCase("LookUp")){
				LookUpCode lookUpCode=lookUpCodeRepository.findOne(Long.valueOf(jeLinesListForColumns.get(0).getAttrRef4Type().split("_")[1]));
				attributeName.put("field", lookUpCode.getMeaning().substring(0, 1).toLowerCase() + lookUpCode.getMeaning().substring(1).replaceAll(" ", ""));
				attributeName.put( "header", lookUpCode.getMeaning());
					attributeName.put("align", "left");

				attributeNames.add(attributeName);
			}
			else if(jeLinesListForColumns.get(0).getAttrRef4Type().split("_")[0].equalsIgnoreCase("column"))
			{
				DataViewsColumns dvc=dataViewsColumnsRepository.findOne(Long.valueOf(jeLinesListForColumns.get(0).getAttrRef4Type().split("_")[1]));
				attributeName.put("field", dvc.getColumnName().substring(0, 1).toLowerCase() + dvc.getColumnName().substring(1).replaceAll(" ", ""));
				attributeName.put( "header", dvc.getColumnName());
				if(dvc.getColDataType().equalsIgnoreCase("DATE"))
    				attributeName.put("align", "center");
    			else if(dvc.getColDataType().equalsIgnoreCase("DECIMAL"))
    				attributeName.put("align", "right");
    			else
    				attributeName.put("align", "left");
    			attributeNames.add(attributeName);
			}
		}
     	if(jeLinesListForColumns.get(0).getAttrRef5Type()!=null)
    	{
    		LinkedHashMap attributeName=new LinkedHashMap();
			if(jeLinesListForColumns.get(0).getAttrRef5Type().split("_")[0].equalsIgnoreCase("LookUp")){
				LookUpCode lookUpCode=lookUpCodeRepository.findOne(Long.valueOf(jeLinesListForColumns.get(0).getAttrRef5Type().split("_")[1]));
				attributeName.put("field", lookUpCode.getMeaning().substring(0, 1).toLowerCase() + lookUpCode.getMeaning().substring(1).replaceAll(" ", ""));
				attributeName.put( "header", lookUpCode.getMeaning());
					attributeName.put("align", "left");

				attributeNames.add(attributeName);
			}
			else if(jeLinesListForColumns.get(0).getAttrRef5Type().split("_")[0].equalsIgnoreCase("column"))
			{
				DataViewsColumns dvc=dataViewsColumnsRepository.findOne(Long.valueOf(jeLinesListForColumns.get(0).getAttrRef5Type().split("_")[1]));
				attributeName.put("field", dvc.getColumnName().substring(0, 1).toLowerCase() + dvc.getColumnName().substring(1).replaceAll(" ", ""));
				attributeName.put( "header", dvc.getColumnName());
				if(dvc.getColDataType().equalsIgnoreCase("DATE"))
    				attributeName.put("align", "center");
    			else if(dvc.getColDataType().equalsIgnoreCase("DECIMAL"))
    				attributeName.put("align", "right");
    			else
    				attributeName.put("align", "left");
    			attributeNames.add(attributeName);
			}
		}
     	if(jeLinesListForColumns.get(0).getAttrRef6Type()!=null)
    	{
    		LinkedHashMap attributeName=new LinkedHashMap();
			if(jeLinesListForColumns.get(0).getAttrRef6Type().split("_")[0].equalsIgnoreCase("LookUp")){
				LookUpCode lookUpCode=lookUpCodeRepository.findOne(Long.valueOf(jeLinesListForColumns.get(0).getAttrRef6Type().split("_")[1]));
				attributeName.put("field", lookUpCode.getMeaning().substring(0, 1).toLowerCase() + lookUpCode.getMeaning().substring(1).replaceAll(" ", ""));
				attributeName.put( "header", lookUpCode.getMeaning());
					attributeName.put("align", "left");

				attributeNames.add(attributeName);
			}
			else if(jeLinesListForColumns.get(0).getAttrRef6Type().split("_")[0].equalsIgnoreCase("column"))
			{
				DataViewsColumns dvc=dataViewsColumnsRepository.findOne(Long.valueOf(jeLinesListForColumns.get(0).getAttrRef6Type().split("_")[1]));
				attributeName.put("field", dvc.getColumnName().substring(0, 1).toLowerCase() + dvc.getColumnName().substring(1).replaceAll(" ", ""));
				attributeName.put( "header", dvc.getColumnName());
				if(dvc.getColDataType().equalsIgnoreCase("DATE"))
    				attributeName.put("align", "center");
    			else if(dvc.getColDataType().equalsIgnoreCase("DECIMAL"))
    				attributeName.put("align", "right");
    			else
    				attributeName.put("align", "left");
    			attributeNames.add(attributeName);
			}
		}
    
    //	}



    	if(desc!=null && desc.equalsIgnoreCase("Details"))
    	{
    		log.info("if api to fetch Details of records");
    		Page<JeLines> jeLinesList=null;

    		List<JeLines> jeLinesListCount=jeLinesRepository.findByJeHeaderId(batchId);
    		response.addIntHeader("X-COUNT", jeLinesListCount.size());
    		int Count =0;


    		PaginationUtil paginationUtil=new PaginationUtil();

    		int maxlmt=paginationUtil.MAX_LIMIT;
    		int minlmt=paginationUtil.MIN_OFFSET;
    		log.info("maxlmt: "+maxlmt);

    		HttpHeaders headers = null;

    		if(limit==null || limit<minlmt){
    			jeLinesListCount=jeLinesRepository.findByJeHeaderId(batchId);
    			limit = jeLinesListCount.size();
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
    			jeLinesList = jeLinesRepository.findByJeHeaderId(batchId,PaginationUtil.generatePageRequest2(offset, limit));
    			headers = PaginationUtil.generatePaginationHttpHeaders2(jeLinesList, "/api/journalHeaderDetailOrSummaryInfo",offset, limit);
    		}
    		else{
    			log.info("input limit is within maxlimit");
    			jeLinesList = jeLinesRepository.findByJeHeaderId(batchId,PaginationUtil.generatePageRequest(offset, limit));
    			headers = PaginationUtil.generatePaginationHttpHeaderss(jeLinesList, "/api/journalHeaderDetailOrSummaryInfo", offset, limit);
    		}




    		for(int i=0;i<jeLinesList.getContent().size();i++)
    		{
    			LinkedHashMap jelineDetails=new LinkedHashMap();
    			Count =Count+1;
    			jelineDetails.put("line", Count);
    			jelineDetails.put("codeCombination", jeLinesList.getContent().get(i).getCodeCombination());
    			if(jeLinesList.getContent().get(i).getAccountedDebit()!=null)
    			{
    				jelineDetails.put("accountedDebit", jeLinesList.getContent().get(i).getAccountedDebit());
    			}
    			else
    				jelineDetails.put("accountedDebit", 0d);
    			if(jeLinesList.getContent().get(i).getAccountedCredit()!=null)
    			{
    				jelineDetails.put("accountedCredit", jeLinesList.getContent().get(i).getAccountedCredit());
    			}
    			else
    				jelineDetails.put("accountedCredit", 0d);


    			if(jeLinesList.getContent().get(i).getAttrRef1Type()!=null)
    			{
    				if(jeLinesList.getContent().get(i).getAttrRef1Type().split("_")[0].equalsIgnoreCase("LookUp")){
    					LookUpCode lookUpCode=lookUpCodeRepository.findOne(Long.valueOf(jeLinesList.getContent().get(i).getAttrRef1Type().split("_")[1]));
    					jelineDetails.put(lookUpCode.getMeaning().substring(0, 1).toLowerCase() + lookUpCode.getMeaning().substring(1).replaceAll(" ", ""), jeLinesList.getContent().get(i).getAttributeRef1());
    				}
    				else if(jeLinesList.getContent().get(i).getAttrRef1Type().split("_")[0].equalsIgnoreCase("column"))
    				{
    					DataViewsColumns dvc=dataViewsColumnsRepository.findOne(Long.valueOf(jeLinesList.getContent().get(i).getAttrRef1Type().split("_")[1]));
    					jelineDetails.put(dvc.getColumnName().substring(0, 1).toLowerCase() + dvc.getColumnName().substring(1).replaceAll(" ", ""), jeLinesList.getContent().get(i).getAttributeRef1());

    				}
    			}
    			if(jeLinesList.getContent().get(i).getAttrRef2Type()!=null)
    			{
    				if(jeLinesList.getContent().get(i).getAttrRef2Type().split("_")[0].equalsIgnoreCase("LookUp")){
    					LookUpCode lookUpCode=lookUpCodeRepository.findOne(Long.valueOf(jeLinesList.getContent().get(i).getAttrRef2Type().split("_")[1]));
    					jelineDetails.put(lookUpCode.getMeaning().substring(0, 1).toLowerCase() + lookUpCode.getMeaning().substring(1).replaceAll(" ", ""), jeLinesList.getContent().get(i).getAttributeRef2());
    				}
    				else if(jeLinesList.getContent().get(i).getAttrRef2Type().split("_")[0].equalsIgnoreCase("column"))
    				{
    					DataViewsColumns dvc=dataViewsColumnsRepository.findOne(Long.valueOf(jeLinesList.getContent().get(i).getAttrRef2Type().split("_")[1]));
    					jelineDetails.put(dvc.getColumnName().substring(0, 1).toLowerCase() + dvc.getColumnName().substring(1).replaceAll(" ", ""), jeLinesList.getContent().get(i).getAttributeRef2());

    				}
    			}

    			if(jeLinesList.getContent().get(i).getAttrRef3Type()!=null)
    			{
    				if(jeLinesList.getContent().get(i).getAttrRef3Type().split("_")[0].equalsIgnoreCase("LookUp")){
    					LookUpCode lookUpCode=lookUpCodeRepository.findOne(Long.valueOf(jeLinesList.getContent().get(i).getAttrRef3Type().split("_")[1]));
    					jelineDetails.put(lookUpCode.getMeaning().substring(0, 1).toLowerCase() + lookUpCode.getMeaning().substring(1).replaceAll(" ", ""), jeLinesList.getContent().get(i).getAttributeRef3());
    				}
    				else if(jeLinesList.getContent().get(i).getAttrRef3Type().split("_")[0].equalsIgnoreCase("column"))
    				{
    					DataViewsColumns dvc=dataViewsColumnsRepository.findOne(Long.valueOf(jeLinesList.getContent().get(i).getAttrRef3Type().split("_")[1]));
    					jelineDetails.put(dvc.getColumnName().substring(0, 1).toLowerCase() + dvc.getColumnName().substring(1).replaceAll(" ", ""), jeLinesList.getContent().get(i).getAttributeRef3());

    				}
    			}

    			if(jeLinesList.getContent().get(i).getAttrRef4Type()!=null)
    			{
    				if(jeLinesList.getContent().get(i).getAttrRef4Type().split("_")[0].equalsIgnoreCase("LookUp")){
    					LookUpCode lookUpCode=lookUpCodeRepository.findOne(Long.valueOf(jeLinesList.getContent().get(i).getAttrRef4Type().split("_")[1]));
    					jelineDetails.put(lookUpCode.getMeaning().substring(0, 1).toLowerCase() + lookUpCode.getMeaning().substring(1).replaceAll(" ", ""), jeLinesList.getContent().get(i).getAttributeRef4());
    				}
    				else if(jeLinesList.getContent().get(i).getAttrRef4Type().split("_")[0].equalsIgnoreCase("column"))
    				{
    					DataViewsColumns dvc=dataViewsColumnsRepository.findOne(Long.valueOf(jeLinesList.getContent().get(i).getAttrRef4Type().split("_")[1]));
    					jelineDetails.put(dvc.getColumnName().substring(0, 1).toLowerCase() + dvc.getColumnName().substring(1).replaceAll(" ", ""), jeLinesList.getContent().get(i).getAttributeRef4());

    				}
    			}
    			if(jeLinesList.getContent().get(i).getAttrRef5Type()!=null)
    			{
    				if(jeLinesList.getContent().get(i).getAttrRef5Type().split("_")[0].equalsIgnoreCase("LookUp")){
    					LookUpCode lookUpCode=lookUpCodeRepository.findOne(Long.valueOf(jeLinesList.getContent().get(i).getAttrRef5Type().split("_")[1]));
    					jelineDetails.put(lookUpCode.getMeaning().substring(0, 1).toLowerCase() + lookUpCode.getMeaning().substring(1).replaceAll(" ", ""), jeLinesList.getContent().get(i).getAttributeRef5());
    				}
    				else if(jeLinesList.getContent().get(i).getAttrRef5Type().split("_")[0].equalsIgnoreCase("column"))
    				{
    					DataViewsColumns dvc=dataViewsColumnsRepository.findOne(Long.valueOf(jeLinesList.getContent().get(i).getAttrRef5Type().split("_")[1]));
    					jelineDetails.put(dvc.getColumnName().substring(0, 1).toLowerCase() + dvc.getColumnName().substring(1).replaceAll(" ", ""), jeLinesList.getContent().get(i).getAttributeRef5());

    				}
    			}
    			if(jeLinesList.getContent().get(i).getAttrRef6Type()!=null)
    			{
    				if(jeLinesList.getContent().get(i).getAttrRef6Type().split("_")[0].equalsIgnoreCase("LookUp")){
    					LookUpCode lookUpCode=lookUpCodeRepository.findOne(Long.valueOf(jeLinesList.getContent().get(i).getAttrRef6Type().split("_")[1]));
    					jelineDetails.put(lookUpCode.getMeaning().substring(0, 1).toLowerCase() + lookUpCode.getMeaning().substring(1).replaceAll(" ", ""), jeLinesList.getContent().get(i).getAttributeRef6());
    				}
    				else if(jeLinesList.getContent().get(i).getAttrRef6Type().split("_")[0].equalsIgnoreCase("column"))
    				{
    					DataViewsColumns dvc=dataViewsColumnsRepository.findOne(Long.valueOf(jeLinesList.getContent().get(i).getAttrRef6Type().split("_")[1]));
    					jelineDetails.put(dvc.getColumnName().substring(0, 1).toLowerCase() + dvc.getColumnName().substring(1).replaceAll(" ", ""), jeLinesList.getContent().get(i).getAttributeRef6());

    				}
    			}

    			if(jeLinesList.getContent().get(i).getDebitAmount()!=null)
    			{
    				//String deb = reconciliationResultService.getAmountInFormat(jeLinesList.getContent().get(i).getDebitAmount().toString(),currencyFormat);
    				jelineDetails.put("enteredDebit", jeLinesList.getContent().get(i).getDebitAmount());
    			}
    			else
    				jelineDetails.put("enteredDebit", 0d);
    			if(jeLinesList.getContent().get(i).getCreditAmount()!=null)
    			{
    				//String cred = reconciliationResultService.getAmountInFormat(jeLinesList.getContent().get(i).getCreditAmount().toString(),currencyFormat);
    				jelineDetails.put("enteredCredit", jeLinesList.getContent().get(i).getCreditAmount());
    			}
    			else
    				jelineDetails.put("enteredCredit", 0d);


    			jelineDetailsList.add(jelineDetails);
    		}
    		finalMap.put("columnsWithAttributeNames",attributeNames);
    		finalMap.put("jelineDetailsList", jelineDetailsList);
    	}
    	else
    	{


    		Connection conn = null;
    		Statement stmt = null;
    		Statement stmtCt = null;
    		//conn = DriverManager.getConnection(dbUrl, userName, password);
    		DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");

    		ResultSet result2=null;
    		ResultSet resultCount=null;
    		log.info("if api to fetch Summary of records");
    		int Count=0;

    		String subQry1="select code_combination,round(sum(accounted_debit),2) as actDeb,round(sum(accounted_credit),2) as actCred,round(sum(debit_amount),2) as deb,round(sum(credit_amount),2) as cred,";
    		String subQry2=" from t_je_lines  where je_header_id="+batchId+" group by code_combination,";
    		String subQry3="attribute_ref_1,attr_ref_1_type,attribute_ref_2,attr_ref_2_type,attribute_ref_3,attr_ref_3_type,attribute_ref_4,attr_ref_4_type,attribute_ref_5,attr_ref_5_type,attribute_ref_6,attr_ref_6_type";

    		log.info("subQry3 :"+subQry3);

    		String finalQuery=subQry1+subQry3+subQry2+subQry3;


    		log.info("offset :"+offset +"and limit :"+limit);
    		Integer pageNumber=offset;
    		Integer pageSize=limit;
    		log.info("pageNumber :"+pageNumber +"and pageSize :"+pageSize);
    		String finalPagQuery=finalQuery+" Limit "+(pageNumber-1) * pageSize+", "+pageSize;
    		log.info("Final pagination QUERY:"+finalPagQuery);
    		try
    		{
    			conn = ds.getConnection();
    			stmt = conn.createStatement();
    			stmtCt = conn.createStatement();
    			result2=stmt.executeQuery(finalPagQuery);
    			log.info("****query executed*****");
    			ResultSetMetaData rsmd2 = result2.getMetaData();
    			int columnCount = rsmd2.getColumnCount();
    			Boolean currency=false;

    			resultCount=stmtCt.executeQuery(finalQuery);
    			int totalCount=0;
    			while(resultCount.next())
    			{
    				totalCount =totalCount+1;
    			}


    			int count=0;
    			while(result2.next())
    			{
    				//log.info("result2 ");
    				LinkedHashMap map=new LinkedHashMap();
    				Count =Count+1;
    				map.put("line", Count);
    				for (int i = 1; i <= columnCount; i++ ) {
    					//log.info("columnCount :"+columnCount);
    					String name = rsmd2.getColumnName(i); 
    					//log.info("name :"+name);

    					map.put("tempId", jHd.getJeTempId());
    					map.put("codeCombination", result2.getString("code_combination"));
    					//log.info("map 1:"+map);

    					if(result2.getString("actDeb")!=null)
    					{
    						map.put("accountedDebit", Double.valueOf(result2.getString("actDeb")));
    					}
    					else
    						map.put("accountedDebit", 0d);
    					if(result2.getString("actCred")!=null)
    					{
    						map.put("accountedCredit", Double.valueOf(result2.getString("actCred")));
    					}
    					else
    						map.put("accountedCredit", 0d);

    					if(result2.getString("attr_ref_1_type")!=null)
    					{
    						String column=result2.getString("attr_ref_1_type");
    						map.put("refColumn1", result2.getString("attribute_Ref_1"));
    						map.put("refType1", column);
    						if(column.split("_")[0].equalsIgnoreCase("LookUp")){
    							LookUpCode lookUpCode=lookUpCodeRepository.findOne(Long.valueOf(column.split("_")[1]));
    							map.put(lookUpCode.getMeaning().substring(0, 1).toLowerCase() + lookUpCode.getMeaning().substring(1).replaceAll(" ", ""), result2.getString("attribute_Ref_1"));
    						}
    						else if(column.split("_")[0].equalsIgnoreCase("column"))
    						{
    							DataViewsColumns dvc=dataViewsColumnsRepository.findOne(Long.valueOf(column.split("_")[1]));
    							map.put(dvc.getColumnName().substring(0, 1).toLowerCase() + dvc.getColumnName().substring(1).replaceAll(" ", ""), result2.getString("attribute_Ref_1"));

    						}
    					}
    					if(result2.getString("attr_ref_2_type")!=null)
    					{
    						String column=result2.getString("attr_ref_2_type");
    						map.put("refColumn2", result2.getString("attribute_Ref_2"));
    						map.put("refType2", column);
    						if(column.split("_")[0].equalsIgnoreCase("LookUp")){
    							LookUpCode lookUpCode=lookUpCodeRepository.findOne(Long.valueOf(column.split("_")[1]));
    							map.put(lookUpCode.getMeaning().substring(0, 1).toLowerCase() + lookUpCode.getMeaning().substring(1).replaceAll(" ", ""), result2.getString("attribute_Ref_2"));
    						}
    						else if(column.split("_")[0].equalsIgnoreCase("column"))
    						{
    							DataViewsColumns dvc=dataViewsColumnsRepository.findOne(Long.valueOf(column.split("_")[1]));
    							map.put(dvc.getColumnName().substring(0, 1).toLowerCase() + dvc.getColumnName().substring(1).replaceAll(" ", ""), result2.getString("attribute_Ref_2"));

    						}
    					}
    					if(result2.getString("attr_ref_3_type")!=null)
    					{
    						String column=result2.getString("attr_ref_3_type");
    						map.put("refColumn3", result2.getString("attribute_Ref_3"));
    						map.put("refType3", column);
    						if(column.split("_")[0].equalsIgnoreCase("LookUp")){
    							LookUpCode lookUpCode=lookUpCodeRepository.findOne(Long.valueOf(column.split("_")[1]));
    							map.put(lookUpCode.getMeaning().substring(0, 1).toLowerCase() + lookUpCode.getMeaning().substring(1).replaceAll(" ", ""), result2.getString("attribute_Ref_3"));
    						}
    						else if(column.split("_")[0].equalsIgnoreCase("column"))
    						{
    							DataViewsColumns dvc=dataViewsColumnsRepository.findOne(Long.valueOf(column.split("_")[1]));
    							map.put(dvc.getColumnName().substring(0, 1).toLowerCase() + dvc.getColumnName().substring(1).replaceAll(" ", ""), result2.getString("attribute_Ref_3"));

    						}
    					}
    					if(result2.getString("attr_ref_4_type")!=null)
    					{
    						String column=result2.getString("attr_ref_4_type");
    						map.put("refColumn4", result2.getString("attribute_Ref_4"));
    						map.put("refType4", column);
    						if(column.split("_")[0].equalsIgnoreCase("LookUp")){
    							LookUpCode lookUpCode=lookUpCodeRepository.findOne(Long.valueOf(column.split("_")[1]));
    							map.put(lookUpCode.getMeaning().substring(0, 1).toLowerCase() + lookUpCode.getMeaning().substring(1).replaceAll(" ", ""), result2.getString("attribute_Ref_4"));
    						}
    						else if(column.split("_")[0].equalsIgnoreCase("column"))
    						{
    							DataViewsColumns dvc=dataViewsColumnsRepository.findOne(Long.valueOf(column.split("_")[1]));
    							map.put(dvc.getColumnName().substring(0, 1).toLowerCase() + dvc.getColumnName().substring(1).replaceAll(" ", ""), result2.getString("attribute_Ref_4"));

    						}
    					}
    					if(result2.getString("attr_ref_5_type")!=null)
    					{
    						String column=result2.getString("attr_ref_5_type");
    						map.put("refColumn5", result2.getString("attribute_Ref_5"));
    						map.put("refType5", column);
    						if(column.split("_")[0].equalsIgnoreCase("LookUp")){
    							LookUpCode lookUpCode=lookUpCodeRepository.findOne(Long.valueOf(column.split("_")[1]));
    							map.put(lookUpCode.getMeaning().substring(0, 1).toLowerCase() + lookUpCode.getMeaning().substring(1).replaceAll(" ", ""), result2.getString("attribute_Ref_5"));
    						}
    						else if(column.split("_")[0].equalsIgnoreCase("column"))
    						{
    							DataViewsColumns dvc=dataViewsColumnsRepository.findOne(Long.valueOf(column.split("_")[1]));
    							map.put(dvc.getColumnName().substring(0, 1).toLowerCase() + dvc.getColumnName().substring(1).replaceAll(" ", ""), result2.getString("attribute_Ref_5"));

    						}
    					}
    					if(result2.getString("attr_ref_6_type")!=null)
    					{
    						String column=result2.getString("attr_ref_6_type");
    						map.put("refColumn6", result2.getString("attribute_Ref_6"));
    						map.put("refType6", column);
    						if(column.split("_")[0].equalsIgnoreCase("LookUp")){
    							LookUpCode lookUpCode=lookUpCodeRepository.findOne(Long.valueOf(column.split("_")[1]));
    							map.put(lookUpCode.getMeaning().substring(0, 1).toLowerCase() + lookUpCode.getMeaning().substring(1).replaceAll(" ", ""), result2.getString("attribute_Ref_6"));
    						}
    						else if(column.split("_")[0].equalsIgnoreCase("column"))
    						{
    							DataViewsColumns dvc=dataViewsColumnsRepository.findOne(Long.valueOf(column.split("_")[1]));
    							map.put(dvc.getColumnName().substring(0, 1).toLowerCase() + dvc.getColumnName().substring(1).replaceAll(" ", ""), result2.getString("attribute_Ref_6"));

    						}
    					}

    					if(result2.getString("deb")!=null)
    					{
    						map.put("enteredDebit", Double.valueOf(result2.getString("deb")));
    					}
    					else
    						map.put("enteredDebit", 0d);
    					if(result2.getString("cred")!=null)
    					{
    						map.put("enteredCredit",Double.valueOf(result2.getString("cred")));
    					}
    					else
    						map.put("enteredCredit",0d);

    				}
    				//log.info("last before :"+map);
    				jelineDetailsList.add(map);
    			}
    			response.addIntHeader("X-COUNT", totalCount);
    		}
    		catch(Exception e)
    		{
    			e.printStackTrace();
    		}

    		finally
    		{
    			if(result2!=null)
    				try {
    					result2.close();
    				} catch (SQLException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    			if(resultCount!=null)
    				try {
    					resultCount.close();
    				} catch (SQLException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    			if(stmt!=null)
    				try {
    					stmt.close();
    				} catch (SQLException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    			if(stmtCt!=null)
    				try {
    					stmtCt.close();
    				} catch (SQLException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    			if(conn!=null)
    				try {
    					conn.close();
    				} catch (SQLException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    			//}



    		}
    		log.info("jelineDetailsListsize :"+jelineDetailsList.size());
    		finalMap.put("columnsWithAttributeNames",attributeNames);
    		finalMap.put("jelineDetailsList", jelineDetailsList);

    	}
    	return finalMap;



    }
    
    
    @PostMapping("/journalHeaderGroupedInfoExcel")
   	@Timed 
 	public void journalHeaderGroupedInfoExcelGeneration(@RequestParam String batchName,@RequestBody LinkedHashMap jHGroupedInfo,HttpServletRequest request,@RequestParam String fileType,HttpServletResponse response) throws IOException
    {
    	HashMap map0=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map0.get("tenantId").toString());
    	ApplicationPrograms prog=applicationProgramsRepository.findByPrgmNameAndTenantIdAndEnableIsTrue("Journals",tenantId);
    	/*Properties props =  propertiesUtilService.getPropertiesFromClasspath("File.properties");
    	log.info("tempLocation :"+props.getProperty("localTempLocation"));*/
    	log.info("tempLocation :"+prog.getGeneratedPath());
    	LinkedHashMap finalLhmap=jeWQService.journalHeaderDetailsForExcel(jHGroupedInfo, tenantId);
    	//log.info("finalLhmap :"+finalLhmap);
    	List<LinkedHashMap> journalsHeaderDetailsList=(List<LinkedHashMap>) finalLhmap.get("map");


    	LinkedHashMap keysList=journalsHeaderDetailsList.get(0);

  		Set<String> keyset=keysList.keySet();
		log.info("keyset :"+keyset.remove("jeTempId"));
	
		List<String> keyList = new ArrayList<String>(keyset);
    	log.info("keyList :"+keyList);
   

    	if(fileType.equalsIgnoreCase("csv"))
    	{
    		response.setContentType ("application/csv");
    		response.setHeader ("Content-Disposition", "attachment; filename=\"journalHeader.csv\"");

    		fileExportService.jsonToCSV(journalsHeaderDetailsList,keyList,response.getWriter());



    	}
    	if(fileType.equalsIgnoreCase("pdf"))
    	{
    		response.setContentType ("application/pdf");
    		response.setHeader ("Content-Disposition", "attachment; filename=\"journalHeader.pdf\"");

    		fileExportService.jsonToCSV(journalsHeaderDetailsList, keyList,response.getWriter());



    	}
    	else if(fileType.equalsIgnoreCase("excel"))
    	{
    		/*response.setContentType("application/vnd.ms-excel");
    		response.setHeader(
    				"Content-Disposition",
    				"attachment; filename=\"journalHeader.xlsx\""
    				);*/
    		//fileExportService.jsonToCSV(journalsHeaderDetailsList, keyList,response.getWriter());
    		
    		response=fileExportService.exportToExcel(response, keyList, journalsHeaderDetailsList);
    	}


    }
    
    
    /**
     * author:ravali
     * @param batchId
     * @param desc
     * @throws URISyntaxException
     * @throws SftpException
     * @throws IOException
     * @throws SQLException
     */
   /* @PostMapping("/generateJQETemp")
	@Timed
	public HashMap generateConsolidationTemplate(@RequestParam Long batchId,@RequestParam(value = "desc", required = false) String desc)
			throws URISyntaxException, SftpException, IOException, SQLException{
    	ErrorReport downloadReport = new ErrorReport();
    	//String fileName="journalTemplate.xlsx";
    	//String fileCsv="je.csv";
    	//downloadReport = fileService.downloadCostStatementTemplate(fileCsv);
    	//log.info("downloadReport.getDetails() :"+downloadReport.getDetails());
    	JournalsHeaderData jHd=journalsHeaderDataRepository.findOne(batchId);
    	ApplicationPrograms prog=applicationProgramsRepository.findByPrgmNameAndTenantIdAndEnableIsTrue("Journals",jHd.getTenantId());
    	
    	//Properties props =  propertiesUtilService.getPropertiesFromClasspath("File.properties");
    	log.info("tempLocation :"+prog.getGeneratedPath());
    	LinkedHashMap finalMap=jeWQService.journalHeaderAndLineDetails(batchId, desc);
    //	log.info("finalMap :"+finalMap);
    	String batchName=jHd.getJeBatchName();
    	String tenantId=jHd.getTenantId().toString();
    	String fileName=batchName.replaceAll("[^a-zA-Z0-9]","")+tenantId+LocalDate.now().toString().replaceAll("\\s+","").replaceAll("[^a-zA-Z0-9]","")+"LineLevel";
    	JSONObject output;
    	try {
    		output = new JSONObject(finalMap.toString());



    		JSONArray docs = output.getJSONArray("jelineDetailsList");
              //home/nspl/Desktop/fromJSON.csv
    		File file=new File(prog.getGeneratedPath()+fileName+".csv");
    		String csv = CDL.toString(docs);
    		FileUtils.writeStringToFile(file, csv);
    	} catch (JSONException e) {
    		e.printStackTrace();
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}      


    	try {
    		String csvFileAddress = prog.getGeneratedPath()+fileName+".csv"; //csv file address
    		String xlsxFileAddress = prog.getGeneratedPath()+fileName+".xlsx"; //xlsx file address
    		XSSFWorkbook workBook = new XSSFWorkbook();
    		XSSFSheet sheet1 = workBook.createSheet("sheet1");
    		String currentLine=null;
    		int RowNum=2;


    		BufferedReader br = new BufferedReader(new FileReader(csvFileAddress));
    		while ((currentLine = br.readLine()) != null) {
    			String str[] = currentLine.split(",");
    			RowNum++;
    			XSSFRow currentRow=sheet1.createRow(RowNum);
    			for(int i=0;i<str.length;i++){
    				currentRow.createCell(i).setCellValue(str[i]);
    			}
    		}

    		FileOutputStream fileOutputStream =  new FileOutputStream(xlsxFileAddress);
    		workBook.write(fileOutputStream);
    		fileOutputStream.close();
    		System.out.println("Done");
    	} catch (Exception ex) {
    		System.out.println(ex.getMessage()+"Exception in try");
    	}


    	FileInputStream fsIP = new FileInputStream(new File(prog.getGeneratedPath()+fileName+".xlsx")); 

    	XSSFWorkbook wb = new XSSFWorkbook(fsIP); 
    	XSSFSheet sheet = wb.getSheetAt(0);

    	//JournalsHeaderData jHd=journalsHeaderDataRepository.findOne(batchId);
    	
    	 //Create a new font and alter it.
    	 XSSFFont font = wb.createFont();
         font.setBold(true);
         font.setFontHeightInPoints((short)10);
         
       //Set font into style
         XSSFCellStyle style = wb.createCellStyle();
         style.setFont(font);
        
         
        
        
    	XSSFRow row0 =  sheet.createRow(0);
    	XSSFCell cell0 = row0.createCell(0);
    	cell0.setCellValue("Batch Name:");
    	cell0.setCellStyle(style);


    	XSSFCell cell1 = row0.createCell(1);
    	cell1.setCellValue(jHd.getJeBatchName());


    	XSSFCell cell3 = row0.createCell(3);
    	cell3.setCellValue("GL Date:");
    	cell3.setCellStyle(style);

    	XSSFCell cell4 = row0.createCell(4);
    	cell4.setCellValue(jHd.getGlDate().toString());


    	XSSFCell cell6 = row0.createCell(6);
    	cell6.setCellValue("Source:");
    	cell6.setCellStyle(style);

    	XSSFCell cell7 = row0.createCell(7);
    	cell7.setCellValue(jHd.getSource());

    	XSSFCell cell9 = row0.createCell(9);
    	cell9.setCellValue("Conversion Type:");
    	cell9.setCellStyle(style);

    	XSSFCell cell10 = row0.createCell(10);
    	cell10.setCellValue(jHd.getConversionType());


    	XSSFRow row1 =  sheet.createRow(1);

    	XSSFCell cellR0 = row1.createCell(0);
    	cellR0.setCellValue("Ledger:");
    	cellR0.setCellStyle(style);

    	XSSFCell cellR1 = row1.createCell(1);
    	cellR1.setCellValue(jHd.getLedgerName());


    	XSSFCell cellR3 = row1.createCell(3);
    	cellR3.setCellValue("Period:");
    	cellR3.setCellStyle(style);

    	XSSFCell cellR4 = row1.createCell(4);
    	cellR4.setCellValue(jHd.getPeriod());

    	XSSFCell cellR6 = row1.createCell(6);
    	cellR6.setCellValue("Category:");
    	cellR6.setCellStyle(style);

    	XSSFCell cellR7 = row1.createCell(7);
    	cellR7.setCellValue(jHd.getCategory());

    
    	FileOutputStream output_file = new FileOutputStream(new File(prog.getGeneratedPath()+fileName+".xlsx"));
    	log.info("output_file :"+output_file);
    	log.info("wb :"+wb.getNumberOfSheets());
    	wb.write(output_file);
    	output_file.close();

    	File file = new File(prog.getGeneratedPath()+fileName+".xlsx");
    	InputStream inputStream=new FileInputStream(file);
    	HashMap finalPath=new HashMap();
    	String[] destStatus=fileService.fileUpload(inputStream, fileName+".xlsx",Long.valueOf(tenantId));
    	if(destStatus[0].equalsIgnoreCase("success"))
    	{
    		finalPath.put("status", destStatus[0]);
    		String finalFSPath="";
    		TenantConfig fileServerUrl=tenantConfigRepository.findByTenantIdAndKey(Long.valueOf(tenantId), "File Server Path");
    		
    		if(fileServerUrl!=null)
    			finalFSPath=fileServerUrl.getValue()+destStatus[1];
    		else
    		{
    			 fileServerUrl=tenantConfigRepository.findByTenantIdAndKey(0l, "File Server Path");
    			 finalFSPath=fileServerUrl.getValue()+destStatus[1];
    		}
    		finalPath.put("destPath", finalFSPath);
    	}
    	else
    	{
    		finalPath.put("status", "failure");
    	}
		return finalPath;

    }*/
    
    
    
    
    
    
    @PostMapping("/generateJQETemp")
   	@Timed
   	public void generateConsolidationTemplate(@RequestParam Long batchId,@RequestParam(value = "desc", required = false) String desc,@RequestParam String fileType,HttpServletResponse response)
   			throws URISyntaxException, SftpException, IOException, SQLException{

    	JournalsHeaderData jHd=journalsHeaderDataRepository.findOne(batchId);
    	ApplicationPrograms prog=applicationProgramsRepository.findByPrgmNameAndTenantIdAndEnableIsTrue("Journals",jHd.getTenantId());

    	//Properties props =  propertiesUtilService.getPropertiesFromClasspath("File.properties");
    	log.info("tempLocation :"+prog.getGeneratedPath());
    	LinkedHashMap finalMap=jeWQService.journalHeaderAndLineDetails(batchId, desc);
    	List<LinkedHashMap> jelineDetailsList=(List<LinkedHashMap>) finalMap.get("jelineDetailsList");
    	LinkedHashMap keysList=jelineDetailsList.get(0);

    	Set<String> keyset=keysList.keySet();
    	log.info("keyset :"+keyset.remove("id"));
    	log.info("keyset :"+keyset.remove("createdBy"));
    	log.info("keyset :"+keyset.remove("createdDate"));
    	log.info("keyset :"+keyset.remove("fxRateId"));
    	log.info("keyset :"+keyset.remove("lastUpdatedBy"));
    	log.info("keyset :"+keyset.remove("lastUpdatedDate"));
    	List<String> keyList = new ArrayList<String>(keyset);
    	log.info("keyList :"+keyList);

    	if(fileType.equalsIgnoreCase("csv"))
    	{
    		response.setContentType ("application/csv");
    		response.setHeader ("Content-Disposition", "attachment; filename=\"journalHeaderDetails.csv\"");

    		fileExportService.jsonToCSV(jelineDetailsList,keyList,response.getWriter());



    	}
    	if(fileType.equalsIgnoreCase("pdf"))
    	{
    		response.setContentType ("application/pdf");
    		response.setHeader ("Content-Disposition", "attachment; filename=\"journalHeaderDetails.pdf\"");

    		fileExportService.jsonToCSV(jelineDetailsList, keyList,response.getWriter());



    	}
    	else if(fileType.equalsIgnoreCase("excel"))
    	{
    		/*response.setContentType("application/vnd.ms-excel");
    		response.setHeader(
    				"Content-Disposition",
    				"attachment; filename=\"journalHeaderDetails.xlsx\""
    				);
    		fileExportService.jsonToCSV(jelineDetailsList, keyList,response.getWriter());
    		*/
    		response=fileExportService.exportToExcel(response, keyList, jelineDetailsList);
    	}




    }

    
    
    
    /**
     * author:ravali
     * desc:reverse functionality
     * @param jeBatchId
     * @param userId
     * @param functionality
     */
   @GetMapping("/reverseFunctionality")
	@Timed
    public HashMap reverseFunc(HttpServletRequest request,@RequestParam Long jeBatchId,@RequestParam String functionality) 
   {

	   HashMap map0=userJdbcService.getuserInfoFromToken(request);
	  	Long userId=Long.parseLong(map0.get("userId").toString());
	   HashMap map=new HashMap();
	   JournalsHeaderData jHDById=journalsHeaderDataRepository.findOne(jeBatchId);
	   jHDById.setLastUpdatedBy(userId);
	   jHDById.setLastUpdatedDate(ZonedDateTime.now());
	   jHDById.setRevDetails("Reversed");
	   JournalsHeaderData newJHD=new JournalsHeaderData();
	   newJHD.setJeBatchName("REV-"+jHDById.getJeBatchName());
	   newJHD.setTenantId(jHDById.getTenantId());
	   newJHD.setJeTempId(jHDById.getJeTempId());
	   newJHD.setJeBatchDate(jHDById.getJeBatchDate());
	   newJHD.setGlDate(jHDById.getGlDate());
	   newJHD.setCurrency(jHDById.getCurrency());
	   newJHD.setConversionType(jHDById.getConversionType());
	   newJHD.setCategory(jHDById.getCategory());
	   newJHD.setSource(jHDById.getSource());
	   newJHD.setLedgerName(jHDById.getLedgerName());
	   newJHD.setBatchTotal(jHDById.getBatchTotal());
	   newJHD.setRunDate(jHDById.getRunDate());
	   newJHD.setSubmittedBy(jHDById.getSubmittedBy());
	   newJHD.setCreatedBy(jHDById.getCreatedBy());
	   newJHD.setLastUpdatedBy(userId);
	   newJHD.setCreatedDate(jHDById.getCreatedDate());
	   newJHD.setLastUpdatedDate(ZonedDateTime.now());
	   newJHD.setPeriod(jHDById.getPeriod());
	   newJHD.setJobReference(jHDById.getJobReference());
	   newJHD.setRevDetails(jeBatchId.toString());
	   newJHD= journalsHeaderDataRepository.save(newJHD);


	   jHDById=journalsHeaderDataRepository.save(jHDById);
	   List<JeLines> jeLinesList=jeLinesRepository.findByJeHeaderId(jeBatchId);
	
	   List<JeLines>  newJeLinesList=new ArrayList<JeLines>();
	   for(JeLines jeLines:jeLinesList)
	   {
		   JeLines jL=new JeLines();
		   jL.setJeHeaderId(newJHD.getId());
		   jL.setJeBatchId(newJHD.getJeBatchId());
		   jL.setRowId(jeLines.getRowId());
		   jL.setLineNum(jeLines.getLineNum());
		   jL.setDescriptionAttribute(jeLines.getDescriptionAttribute());
		   jL.setCurrency(jeLines.getCurrency());
		   if(functionality.equalsIgnoreCase("switch"))
		   {
			   jL.setDebitAmount(jeLines.getCreditAmount());
			   jL.setCreditAmount(jeLines.getDebitAmount());
			   jL.setAccountedCredit(jeLines.getAccountedDebit());
			   jL.setAccountedDebit(jeLines.getAccountedCredit());
		   }
		   else
		   {
			   if(jeLines.getDebitAmount()!=null )
			   {
				   if(jeLines.getDebitAmount().signum()==-1)
					   jL.setDebitAmount(jeLines.getDebitAmount().abs());
				   else
					   jL.setDebitAmount(jeLines.getDebitAmount().negate());
			   }
			   if(jeLines.getCreditAmount()!=null)
			   {
				   if(jeLines.getCreditAmount().signum()==-1)
					   jL.setCreditAmount(jeLines.getCreditAmount().abs());
				   else
					   jL.setCreditAmount(jeLines.getCreditAmount().negate());
			   }
			   if(jeLines.getAccountedCredit()!=null)
			   {
				   if(jeLines.getAccountedCredit().signum()==-1)
					   jL.setAccountedCredit(jeLines.getAccountedCredit().abs());
				   else
					   jL.setAccountedCredit(jeLines.getAccountedCredit().negate());
			   }
			   if(jeLines.getAccountedDebit()!=null)
			   {
				   if(jeLines.getAccountedDebit().signum()==-1)
					   jL.setAccountedDebit(jeLines.getAccountedDebit().abs());
				   else
					   jL.setAccountedDebit(jeLines.getAccountedDebit().negate());
			   }
		   }
		   jL.setComments(jeLines.getComments());
		   jL.setCreatedDate(ZonedDateTime.now());
		   jL.setCreatedBy(userId);
		   jL.setLastUpdatedDate(ZonedDateTime.now());
		   jL.setLastUpdatedBy(userId);
		   jL.setCodeCombination(jeLines.getCodeCombination());
		   jL.setAttributeRef1(jeLines.getAttributeRef1());
		   jL.setAttributeRef2(jeLines.getAttributeRef2());
		   jL.setAttributeRef3(jeLines.getAttributeRef3());
		   jL.setAttributeRef4(jeLines.getAttributeRef4());
		   jL.setAttributeRef5(jeLines.getAttributeRef5());
		   jL.setAttributeRef6(jeLines.getAttributeRef6());
		   jL.setAttrRef1Type(jeLines.getAttrRef1Type());
		   jL.setAttrRef2Type(jeLines.getAttrRef2Type());
		   jL.setAttrRef3Type(jeLines.getAttrRef3Type());
		   jL.setAttrRef4Type(jeLines.getAttrRef4Type());
		   jL.setAttrRef5Type(jeLines.getAttrRef5Type());
		   jL.setAttrRef6Type(jeLines.getAttrRef6Type());
		   newJeLinesList.add(jL);
	   }

	   if(jeLinesList.size()==newJeLinesList.size())
	   {
		   jeLinesRepository.save(newJeLinesList);
		   //reversed RowIds
		   List<BigInteger> rowIds=jeLinesRepository.findRowIdsByJeHeaderId(newJHD.getId());
		   log.info("reversed rowIds :"+rowIds);
		   List<AccountedSummary> acctJournalsList=accountedSummaryRepository.findByRowIdIn(rowIds);
		   log.info("acctJournalsList.size :"+acctJournalsList.size());
		   for(AccountedSummary actJour:acctJournalsList)
		   {
			   actJour.setJournalStatus("REVERSED");
			   actJour.setLastUpdatedBy(userId);
			   actJour.setLastUpdatedDate(ZonedDateTime.now());
			   accountedSummaryRepository.save(actJour);
		   }
		   
		   if(newJHD.getId()!=null)
			   map.put("status", "Reversed Successfully With Batch Name '"+newJHD.getJeBatchName()+"'");
		   else
			   map.put("status", "Failed To Create Reverse Batch For '"+jHDById.getJeBatchName()+"'");  
	   }
	   return map;

   }
 
   
   
   /**
    *  author :ravali
    * @param tenantId
    * @return
    */
    @GetMapping("/distincttemplateNames")
 	@Timed
 	public List<LinkedHashMap> templateName(HttpServletRequest request)
 	{
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
      	Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	log.info("Rest request to fetch distinct template names ");
    	List<BigInteger> tempIdList=journalsHeaderDataRepository.fetchDistinctTempIdByTenantId(tenantId);
    	log.info("tempIdList :"+tempIdList);
    	List<LinkedHashMap> finalMap=new ArrayList<LinkedHashMap>();
    	for(int i=0;i<tempIdList.size();i++)
    	{
    		LinkedHashMap tempName=new LinkedHashMap();
    		TemplateDetails temp=templateDetailsRepository.findOne(tempIdList.get(i).longValue());
    		tempName.put("jeTempId", temp.getId());
    		tempName.put("templateName", temp.getTemplateName());
    		finalMap.add(tempName);
    	}

    	return finalMap;
 	}
    
    @PostMapping("/acctDrillDownBKP")
    @Timed
 	public List<LinkedHashMap> acctDrillDownBKP(@RequestBody HashMap map,@RequestParam Long batchId,@RequestParam(value = "page" , required = true) Integer page,
			@RequestParam(value = "per_page", required = true) Integer size)
			{
    	log.info("if api to fetch Details of records :"+batchId);
    	//	Page<JeLines> jeLinesList=jeLinesRepository.findByJeBatchId(batchId,pageable);
    	List<JeLdrDetails> jeLdr=jeLdrDetailsRepository.findByJeTempIdAndRefColumnNotNull(Long.valueOf(map.get("tempId").toString()));

    	List<LinkedHashMap> jelineDetailsList=new ArrayList<LinkedHashMap>();

    	Properties props = propertiesUtilService.getPropertiesFromClasspath("File.properties");
    	String currencyFormat = props.getProperty("currencyFormat");

    	/*  	String dbUrl=env.getProperty("spring.datasource.url");
    	String[] parts=dbUrl.split("[\\s@&?$+-]+");
    	String host = parts[0].split("/")[2].split(":")[0];
    	log.info("host :"+host);
    	String schemaName=parts[0].split("/")[3];
    	log.info("schemaName :"+schemaName);
    	String userName = env.getProperty("spring.datasource.username");
    	String password = env.getProperty("spring.datasource.password");
    	String jdbcDriver = env.getProperty("spring.datasource.jdbcdriver");*/
    	Connection conn = null;
    	Statement stmt = null;

    	ResultSet result2=null;
    	log.info("if api to fetch Summary of records");







    	int Count=0;
    	String subQry3;
    	String subQry1="select * from t_je_lines  where je_header_id="+batchId+" and ";
    	//if(map.get("currency")!=null)
    	//	subQry3=" code_combination='"+map.get("codeCombination")+"'";
    	//else
    	subQry3=" code_combination='"+map.get("codeCombination")+"'";


    	String query ="";
    	HashMap attrbuteMap=new HashMap();
    	for(int i=0;i<jeLdr.size();i++)
    	{
    		if(jeLdr.get(i).getRefColumn()==1)
    		{
    			if(map.get("refColumn1")!=null)
    			{

    				if(jeLdr.get(i).getColType().equalsIgnoreCase("System")){
    					LookUpCode lookUpCode=lookUpCodeRepository.findOne(jeLdr.get(i).getColValue());
    					String lok=lookUpCode.getMeaning().substring(0, 1).toLowerCase()+lookUpCode.getMeaning().substring(1).replaceAll(" ", "");
    					attrbuteMap.put("attribute_Ref_1",lok);
    					log.info("lok 1**:"+lok);
    					if( !(lok.equalsIgnoreCase("enteredDebit") ||lok.equalsIgnoreCase("enteredCredit")) && (map.get("refColumn1").toString().equalsIgnoreCase(lok) && map.get(lok)!=null))
    						query=query+"attribute_Ref_1='"+map.get(lok)+"' and ";

    				}
    				else if(jeLdr.get(i).getColType().equalsIgnoreCase("Line")){
    					DataViewsColumns dvc=dataViewsColumnsRepository.findOne(jeLdr.get(i).getColValue());
    					String dvname=dvc.getColumnName().substring(0, 1).toLowerCase()+dvc.getColumnName().substring(1).replaceAll(" ", "");
    					attrbuteMap.put("attribute_Ref_1",dvname);
    					if(map.get("refColumn1").toString().equalsIgnoreCase(dvname) && !dvname.equalsIgnoreCase("currency") && map.get(dvname)!=null)
    						query=query+"attribute_Ref_1='"+map.get(dvname)+"' and ";
    				}

    			}

    		}
    		if(jeLdr.get(i).getRefColumn()==2)
    		{
    			if(map.get("refColumn2")!=null)
    			{

    				if(jeLdr.get(i).getColType().equalsIgnoreCase("System")){
    					LookUpCode lookUpCode=lookUpCodeRepository.findOne(jeLdr.get(i).getColValue());
    					String lok=lookUpCode.getMeaning().substring(0, 1).toLowerCase()+lookUpCode.getMeaning().substring(1).replaceAll(" ", "");
    					attrbuteMap.put("attribute_Ref_2",lok);
    					if(!(lok.equalsIgnoreCase("enteredDebit") ||lok.equalsIgnoreCase("enteredCredit")) && map.get("refColumn2").toString().equalsIgnoreCase(lok) && map.get(lok)!=null)
    						query=query+"attribute_Ref_2='"+map.get(lok)+"' and ";

    				}
    				else if(jeLdr.get(i).getColType().equalsIgnoreCase("Line")){
    					DataViewsColumns dvc=dataViewsColumnsRepository.findOne(jeLdr.get(i).getColValue());
    					String dvname=dvc.getColumnName().substring(0, 1).toLowerCase()+dvc.getColumnName().substring(1).replaceAll(" ", "");
    					attrbuteMap.put("attribute_Ref_2",dvname);
    					if(map.get("refColumn2").toString().equalsIgnoreCase(dvname) && !dvname.equalsIgnoreCase("currency") && map.get(dvname)!=null)
    						query=query+"attribute_Ref_2='"+map.get(dvname)+"' and ";
    				}

    			}

    		}
    		if(jeLdr.get(i).getRefColumn()==3)
    		{
    			if(map.get("refColumn3")!=null)
    			{

    				if(jeLdr.get(i).getColType().equalsIgnoreCase("System")){
    					LookUpCode lookUpCode=lookUpCodeRepository.findOne(jeLdr.get(i).getColValue());
    					String lok=lookUpCode.getMeaning().substring(0, 1).toLowerCase()+lookUpCode.getMeaning().substring(1).replaceAll(" ", "");
    					attrbuteMap.put("attribute_Ref_3",lok);
    					if(!(lok.equalsIgnoreCase("enteredDebit") ||lok.equalsIgnoreCase("enteredCredit")) && map.get("refColumn3").toString().equalsIgnoreCase(lok) && map.get(lok)!=null)
    						query=query+"attribute_Ref_3='"+map.get(lok)+"' and ";

    				}
    				else if(jeLdr.get(i).getColType().equalsIgnoreCase("Line")){
    					DataViewsColumns dvc=dataViewsColumnsRepository.findOne(jeLdr.get(i).getColValue());
    					String dvname=dvc.getColumnName().substring(0, 1).toLowerCase()+dvc.getColumnName().substring(1).replaceAll(" ", "");
    					attrbuteMap.put("attribute_Ref_3",dvname);
    					if(map.get("refColumn3").toString().equalsIgnoreCase(dvname) && !dvname.equalsIgnoreCase("currency") && map.get(dvname)!=null)
    						query=query+"attribute_Ref_3='"+map.get(dvname)+"' and ";
    				}

    			}

    		}
    		if(jeLdr.get(i).getRefColumn()==4)
    		{
    			if(map.get("refColumn4")!=null)
    			{

    				if(jeLdr.get(i).getColType().equalsIgnoreCase("System")){
    					LookUpCode lookUpCode=lookUpCodeRepository.findOne(jeLdr.get(i).getColValue());
    					String lok=lookUpCode.getMeaning().substring(0, 1).toLowerCase()+lookUpCode.getMeaning().substring(1).replaceAll(" ", "");
    					attrbuteMap.put("attribute_Ref_4",lok);
    					if(!(lok.equalsIgnoreCase("enteredDebit") ||lok.equalsIgnoreCase("enteredCredit")) && map.get("refColumn4").toString().equalsIgnoreCase(lok) && map.get(lok)!=null)
    						query=query+"attribute_Ref_4='"+map.get(lok)+"' and ";

    				}
    				else if(jeLdr.get(i).getColType().equalsIgnoreCase("Line")){
    					DataViewsColumns dvc=dataViewsColumnsRepository.findOne(jeLdr.get(i).getColValue());
    					String dvname=dvc.getColumnName().substring(0, 1).toLowerCase()+dvc.getColumnName().substring(1).replaceAll(" ", "");
    					attrbuteMap.put("attribute_Ref_4",dvname);
    					if(map.get("refColumn4").toString().equalsIgnoreCase(dvname) && !dvname.equalsIgnoreCase("currency") && map.get(dvname)!=null)
    						query=query+"attribute_Ref_4='"+map.get(dvname)+"' and ";
    				}

    			}

    		}
    		if(jeLdr.get(i).getRefColumn()==5)
    		{
    			if(map.get("refColumn5")!=null)
    			{

    				if(jeLdr.get(i).getColType().equalsIgnoreCase("System")){
    					LookUpCode lookUpCode=lookUpCodeRepository.findOne(jeLdr.get(i).getColValue());
    					String lok=lookUpCode.getMeaning().substring(0, 1).toLowerCase()+lookUpCode.getMeaning().substring(1).replaceAll(" ", "");
    					attrbuteMap.put("attribute_Ref_5",lok);
    					if(!(lok.equalsIgnoreCase("enteredDebit") ||lok.equalsIgnoreCase("enteredCredit")) && map.get("refColumn5").toString().equalsIgnoreCase(lok) && map.get(lok)!=null)
    						query=query+"attribute_Ref_5='"+map.get(lok)+"' and ";

    				}
    				else if(jeLdr.get(i).getColType().equalsIgnoreCase("Line")){
    					DataViewsColumns dvc=dataViewsColumnsRepository.findOne(jeLdr.get(i).getColValue());
    					String dvname=dvc.getColumnName().substring(0, 1).toLowerCase()+dvc.getColumnName().substring(1).replaceAll(" ", "");
    					attrbuteMap.put("attribute_Ref_5",dvname);
    					if(map.get("refColumn5").toString().equalsIgnoreCase(dvname) && !dvname.equalsIgnoreCase("currency") && map.get(dvname)!=null)
    						query=query+"attribute_Ref_5='"+map.get(dvname)+"' and ";
    				}

    			}

    		}
    		if(jeLdr.get(i).getRefColumn()==6)
    		{
    			if(map.get("refColumn6")!=null)
    			{

    				if(jeLdr.get(i).getColType().equalsIgnoreCase("System")){
    					LookUpCode lookUpCode=lookUpCodeRepository.findOne(jeLdr.get(i).getColValue());
    					String lok=lookUpCode.getMeaning().substring(0, 1).toLowerCase()+lookUpCode.getMeaning().substring(1).replaceAll(" ", "");
    					attrbuteMap.put("attribute_Ref_6",lok);
    					if(!(lok.equalsIgnoreCase("enteredDebit") ||lok.equalsIgnoreCase("enteredCredit")) && map.get("refColumn2").toString().equalsIgnoreCase(lok) && map.get(lok)!=null)
    						query=query+"attribute_Ref_6='"+map.get(lok)+"' and ";

    				}
    				else if(jeLdr.get(i).getColType().equalsIgnoreCase("Line")){
    					DataViewsColumns dvc=dataViewsColumnsRepository.findOne(jeLdr.get(i).getColValue());
    					String dvname=dvc.getColumnName().substring(0, 1).toLowerCase()+dvc.getColumnName().substring(1).replaceAll(" ", "");
    					attrbuteMap.put("attribute_Ref_6",dvname);
    					if(dvname.equalsIgnoreCase("currency"))
    						query=query+"currency"+"' and ";
    					else if(map.get("refColumn6").toString().equalsIgnoreCase(dvname) && !dvname.equalsIgnoreCase("currency") && map.get(dvname)!=null)
    						query=query+"attribute_Ref_6='"+map.get(dvname)+"' and ";
    				}

    			}

    		}

    	}

    	String finalQuery=subQry1+query+subQry3+"  Limit "+(page-1) * size+", "+size;
    	log.info("finalQuery :"+finalQuery);
    	log.info("attrbuteMap :"+attrbuteMap);
    	try
    	{
    		DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
    		conn = ds.getConnection();
    		stmt = conn.createStatement();
    		result2=stmt.executeQuery(finalQuery);
    		ResultSetMetaData rsmd2 = result2.getMetaData();
    		int columnCount = rsmd2.getColumnCount();
    		int count=0;
    		while(result2.next())
    		{
    			LinkedHashMap det=new LinkedHashMap();
    			Count =Count+1;
    			det.put("line", Count);
    			for (int i = 1; i <= columnCount; i++ ) {
    				String name = rsmd2.getColumnName(i); 

    				det.put("codeCombination", result2.getString("code_combination"));

    				if(name.equalsIgnoreCase("currency"))
    					det.put("currency", result2.getString("currency"));
    				/*	det.put("accountedDebit", result2.getString("accounted_debit"));
    			det.put("accountedCredit", result2.getString("accounted_credit"));*/




    				if(result2.getString("accounted_debit")!=null)
    				{
    					//String deb = reconciliationResultService.getAmountInFormat(result2.getString("accounted_debit"),currencyFormat);
    					det.put("accountedDebit", Double.valueOf(result2.getString("accounted_debit")));
    				}
    				else
    					det.put("accountedDebit", 0d);
    				if(result2.getString("accounted_credit")!=null)
    				{
    					//String cred = reconciliationResultService.getAmountInFormat(result2.getString("accounted_credit"),currencyFormat);
    					det.put("accountedCredit",Double.valueOf(result2.getString("accounted_credit")));
    				}
    				else
    					det.put("accountedCredit",0d);


    				if(name.equalsIgnoreCase("attribute_Ref_1"))
    				{
    					if(attrbuteMap.get("attribute_Ref_1")!=null && !attrbuteMap.get("attribute_Ref_1").toString().equalsIgnoreCase("currency"))
    					{
    						det.put("refColumn1", attrbuteMap.get("attribute_Ref_1").toString().substring(0, 1).toLowerCase()+ attrbuteMap.get("attribute_Ref_1").toString().substring(1).replaceAll(" ", ""));
    						det.put(attrbuteMap.get("attribute_Ref_1").toString().substring(0, 1).toLowerCase() + attrbuteMap.get("attribute_Ref_1").toString().substring(1).replaceAll(" ", ""), result2.getString("attribute_Ref_1"));
    					}
    				}
    				if(name.equalsIgnoreCase("attribute_Ref_2"))
    				{
    					if(attrbuteMap.get("attribute_Ref_2")!=null && !attrbuteMap.get("attribute_Ref_2").toString().equalsIgnoreCase("currency"))
    					{
    						log.info("result2.getString(attribute_Ref_2): "+result2.getString("attribute_Ref_2"));
    						det.put("refColumn2", attrbuteMap.get("attribute_Ref_2").toString().substring(0, 1).toLowerCase()+ attrbuteMap.get("attribute_Ref_2").toString().substring(1).replaceAll(" ", ""));
    						map.put(attrbuteMap.get("attribute_Ref_2").toString().substring(0, 1).toLowerCase() + attrbuteMap.get("attribute_Ref_2").toString().substring(1).replaceAll(" ", ""), result2.getString("attribute_Ref_2"));
    					}
    				}
    				if(name.equalsIgnoreCase("attribute_Ref_3"))
    				{
    					if(attrbuteMap.get("attribute_Ref_3")!=null && !attrbuteMap.get("attribute_Ref_3").toString().equalsIgnoreCase("currency"))
    					{
    						det.put("refColumn3", attrbuteMap.get("attribute_Ref_3").toString().substring(0, 1).toLowerCase()+ attrbuteMap.get("attribute_Ref_3").toString().substring(1).replaceAll(" ", ""));
    						det.put(attrbuteMap.get("attribute_Ref_3").toString().substring(0, 1).toLowerCase() + attrbuteMap.get("attribute_Ref_3").toString().substring(1).replaceAll(" ", ""), result2.getString("attribute_Ref_3"));
    					}
    				}
    				if(name.equalsIgnoreCase("attribute_Ref_4"))
    				{
    					if(attrbuteMap.get("attribute_Ref_4")!=null && !attrbuteMap.get("attribute_Ref_4").toString().equalsIgnoreCase("currency"))
    					{
    						det.put("refColumn4", attrbuteMap.get("attribute_Ref_4").toString().substring(0, 1).toLowerCase()+ attrbuteMap.get("attribute_Ref_4").toString().substring(1).replaceAll(" ", ""));
    						det.put(attrbuteMap.get("attribute_Ref_4").toString().substring(0, 1).toLowerCase() + attrbuteMap.get("attribute_Ref_4").toString().substring(1).replaceAll(" ", ""), result2.getString("attribute_Ref_4"));
    					}
    				}
    				if(name.equalsIgnoreCase("attribute_Ref_5"))
    				{
    					if(attrbuteMap.get("attribute_Ref_5")!=null && !attrbuteMap.get("attribute_Ref_5").toString().equalsIgnoreCase("currency"))
    					{
    						det.put("refColumn5", attrbuteMap.get("attribute_Ref_5").toString().substring(0, 1).toLowerCase()+ attrbuteMap.get("attribute_Ref_5").toString().substring(1).replaceAll(" ", ""));
    						det.put(attrbuteMap.get("attribute_Ref_5").toString().substring(0, 1).toLowerCase() + attrbuteMap.get("attribute_Ref_5").toString().substring(1).replaceAll(" ", ""), result2.getString("attribute_Ref_5"));
    					}
    				}
    				if(name.equalsIgnoreCase("attribute_Ref_6"))
    				{
    					if(attrbuteMap.get("attribute_Ref_6")!=null && !attrbuteMap.get("attribute_Ref_6").toString().equalsIgnoreCase("currency"))
    					{
    						det.put("refColumn6", attrbuteMap.get("attribute_Ref_6").toString().substring(0, 1).toLowerCase()+ attrbuteMap.get("attribute_Ref_6").toString().substring(1).replaceAll(" ", ""));
    						det.put(attrbuteMap.get("attribute_Ref_6").toString().substring(0, 1).toLowerCase() + attrbuteMap.get("attribute_Ref_6").toString().substring(1).replaceAll(" ", ""), result2.getString("attribute_Ref_6"));
    					}
    				}

    				if(result2.getString("debit_amount")!=null)
    				{
    					//String deb = reconciliationResultService.getAmountInFormat(result2.getString("debit_amount"),currencyFormat);
    					det.put("enteredDebit",Double.valueOf( result2.getString("debit_amount")));
    				}
    				else
    					det.put("enteredDebit", 0d);
    				if(result2.getString("credit_amount")!=null)
    				{
    					//String cred = reconciliationResultService.getAmountInFormat(result2.getString("credit_amount"),currencyFormat);
    					det.put("enteredCredit",Double.valueOf( result2.getString("credit_amount")));
    				}
    				else
    					det.put("enteredCredit",0d);


    				/*det.put("enteredDebit", result2.getString("debit_amount"));
    			det.put("enteredCredit", result2.getString("credit_amount"));*/
    			}
    			jelineDetailsList.add(det);
    		}
    	}
    	catch(Exception e)
    	{
    		log.info("Exception while fetching data in acctDrillDown :"+e);
    	}
    	finally
    	{
    		if(result2!=null)
    			try {
    				result2.close();
    			} catch (SQLException e) {
    				// TODO Auto-generated catch block
    				log.info("Exception while closing result2 in acctDrillDown "+e);
    				e.printStackTrace();
    			}
    		if(stmt!=null)
    			try {
    				stmt.close();
    			} catch (SQLException e) {
    				// TODO Auto-generated catch block
    				log.info("Exception while closing stmt in acctDrillDown "+e);
    				e.printStackTrace();
    			}
    		if(conn!=null)
    			try {
    				conn.close();
    			} catch (SQLException e) {
    				// TODO Auto-generated catch block
    				log.info("Exception while closing conn in acctDrillDown "+e);
    				e.printStackTrace();

    			}
    	}
    	return jelineDetailsList;
			}
    /**
     * author :ravali
     * @param map
     * @param batchId
     * @param page
     * @param size
     * @return
     * @throws SQLException
     */
    /** current working**/
    @PostMapping("/accountingDrillDown")
    @Timed
 	public List<LinkedHashMap> acctDrillDown(@RequestBody HashMap map,@RequestParam Long batchId,@RequestParam(value = "page" , required = true) Integer page,
			@RequestParam(value = "per_page", required = true) Integer size)
			{
    	log.info("if api to fetch Details of records :"+batchId);
    	//	Page<JeLines> jeLinesList=jeLinesRepository.findByJeBatchId(batchId,pageable);
    	List<JeLdrDetails> jeLdr=jeLdrDetailsRepository.findByJeTempIdAndRefColumnNotNull(Long.valueOf(map.get("tempId").toString()));

    	List<LinkedHashMap> jelineDetailsList=new ArrayList<LinkedHashMap>();

    	Properties props = propertiesUtilService.getPropertiesFromClasspath("File.properties");
    	String currencyFormat = props.getProperty("currencyFormat");

    	/*  	String dbUrl=env.getProperty("spring.datasource.url");
    	String[] parts=dbUrl.split("[\\s@&?$+-]+");
    	String host = parts[0].split("/")[2].split(":")[0];
    	log.info("host :"+host);
    	String schemaName=parts[0].split("/")[3];
    	log.info("schemaName :"+schemaName);
    	String userName = env.getProperty("spring.datasource.username");
    	String password = env.getProperty("spring.datasource.password");
    	String jdbcDriver = env.getProperty("spring.datasource.jdbcdriver");*/
    	Connection conn = null;
    	Statement stmt = null;

    	ResultSet result2=null;
    	log.info("if api to fetch Summary of records");







    	int Count=0;
    	String subQry3;
    	String subQry1="select * from t_je_lines  where je_header_id="+batchId+" and ";
    	//if(map.get("currency")!=null)
    	//	subQry3=" code_combination='"+map.get("codeCombination")+"'";
    	//else
    	subQry3="code_combination='"+map.get("codeCombination")+"'";


    	String query ="";
    	
    	if(map.get("refColumn1")!=null)
    		query=query+"attribute_Ref_1='"+map.get("refColumn1")+"' and";
    	if(map.get("refColumn2")!=null)
    		query=query+"attribute_Ref_2='"+map.get("refColumn2")+"' and ";
    	if(map.get("refColumn3")!=null)
    		query=query+"attribute_Ref_3='"+map.get("refColumn3")+"' and ";
    	if(map.get("refColumn4")!=null)
    		query=query+"attribute_Ref_4='"+map.get("refColumn4")+"' and ";
    	if(map.get("refColumn1")!=null)
    		query=query+"attribute_Ref_5='"+map.get("refColumn5")+"' and ";
    	if(map.get("refColumn1")!=null)
    		query=query+"attribute_Ref_6='"+map.get("refColumn6")+"' and ";
    	
    	
    	if(map.get("refType1")!=null)
    		query=query+"attr_ref_1_type='"+map.get("refType1")+"' and ";
    	if(map.get("refColumn2")!=null)
    		query=query+"attr_ref_2_type='"+map.get("refType2")+"' and ";
    	if(map.get("refColumn3")!=null)
    		query=query+"attr_ref_3_type='"+map.get("refType3")+"' and ";
    	if(map.get("refColumn4")!=null)
    		query=query+"attr_ref_4_type='"+map.get("refType4")+"' and ";
    	if(map.get("refColumn1")!=null)
    		query=query+"attr_ref_5_type='"+map.get("refType5")+"' and ";
    	if(map.get("refColumn1")!=null)
    		query=query+"attr_ref_6_type='"+map.get("refType6")+"' and ";
    	
    	/*if(map.get("accountedCredit")!=null)
    		query="accounted_credit="+map.get("accountedCredit");
    	if(map.get("accountedDebit")!=null)
    		query="accounted_debit="+map.get("accountedDebit");
    	
    	if(map.get("enteredCredit")!=null)
    		query="credit_amount="+map.get("enteredCredit");
    	if(map.get("enteredDebit")!=null)
    		query="debit_amount="+map.get("enteredDebit");*/
    	
    	//HashMap attrbuteMap=new HashMap();
    	

    	String finalQuery=subQry1+query+subQry3+"  Limit "+(page-1) * size+", "+size;
    	log.info("finalQuery :"+finalQuery);
    	//log.info("attrbuteMap :"+attrbuteMap);
    	try
    	{
    		DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
    		conn = ds.getConnection();
    		stmt = conn.createStatement();
    		result2=stmt.executeQuery(finalQuery);
    		ResultSetMetaData rsmd2 = result2.getMetaData();
    		int columnCount = rsmd2.getColumnCount();
    		int count=0;
    		while(result2.next())
    		{
    			LinkedHashMap det=new LinkedHashMap();
    			Count =Count+1;
    			det.put("line", Count);
    			for (int i = 1; i <= columnCount; i++ ) {
    				String name = rsmd2.getColumnName(i); 

    				
    				
    				det.put("codeCombination", result2.getString("code_combination"));

    			
    				if(result2.getString("accounted_debit")!=null)
    				{
    					det.put("accountedDebit", Double.valueOf(result2.getString("accounted_debit")));
    				}
    				else
    					det.put("accountedDebit", 0d);
    				if(result2.getString("accounted_credit")!=null)
    				{
    					det.put("accountedCredit", Double.valueOf(result2.getString("accounted_credit")));
    				}
    				else
    					det.put("accountedCredit", 0d);

    				if(result2.getString("attr_ref_1_type")!=null)
    				{
    					String column=result2.getString("attr_ref_1_type");
    					det.put("refColumn1", result2.getString("attribute_Ref_1"));
    					if(column.split("_")[0].equalsIgnoreCase("LookUp")){
    						LookUpCode lookUpCode=lookUpCodeRepository.findOne(Long.valueOf(column.split("_")[1]));
    						map.put(lookUpCode.getMeaning().substring(0, 1).toLowerCase() + lookUpCode.getMeaning().substring(1).replaceAll(" ", ""), result2.getString("attribute_Ref_1"));
    					}
    					else if(column.split("_")[0].equalsIgnoreCase("column"))
    					{
    						DataViewsColumns dvc=dataViewsColumnsRepository.findOne(Long.valueOf(column.split("_")[1]));
    						map.put(dvc.getColumnName().substring(0, 1).toLowerCase() + dvc.getColumnName().substring(1).replaceAll(" ", ""), result2.getString("attribute_Ref_1"));

    					}
    				}
    				if(result2.getString("attr_ref_2_type")!=null)
    				{
    					String column=result2.getString("attr_ref_2_type");
    					det.put("refColumn2", result2.getString("attribute_Ref_2"));
    					if(column.split("_")[0].equalsIgnoreCase("LookUp")){
    						LookUpCode lookUpCode=lookUpCodeRepository.findOne(Long.valueOf(column.split("_")[1]));
    						det.put(lookUpCode.getMeaning().substring(0, 1).toLowerCase() + lookUpCode.getMeaning().substring(1).replaceAll(" ", ""), result2.getString("attribute_Ref_2"));
    					}
    					else if(column.split("_")[0].equalsIgnoreCase("column"))
    					{
    						DataViewsColumns dvc=dataViewsColumnsRepository.findOne(Long.valueOf(column.split("_")[1]));
    						det.put(dvc.getColumnName().substring(0, 1).toLowerCase() + dvc.getColumnName().substring(1).replaceAll(" ", ""), result2.getString("attribute_Ref_2"));

    					}
    				}
    				if(result2.getString("attr_ref_3_type")!=null)
    				{
    					String column=result2.getString("attr_ref_3_type");
    					det.put("refColumn3", result2.getString("attribute_Ref_3"));
    					if(column.split("_")[0].equalsIgnoreCase("LookUp")){
    						LookUpCode lookUpCode=lookUpCodeRepository.findOne(Long.valueOf(column.split("_")[1]));
    						det.put(lookUpCode.getMeaning().substring(0, 1).toLowerCase() + lookUpCode.getMeaning().substring(1).replaceAll(" ", ""), result2.getString("attribute_Ref_3"));
    					}
    					else if(column.split("_")[0].equalsIgnoreCase("column"))
    					{
    						DataViewsColumns dvc=dataViewsColumnsRepository.findOne(Long.valueOf(column.split("_")[1]));
    						det.put(dvc.getColumnName().substring(0, 1).toLowerCase() + dvc.getColumnName().substring(1).replaceAll(" ", ""), result2.getString("attribute_Ref_3"));

    					}
    				}
    				if(result2.getString("attr_ref_4_type")!=null)
    				{
    					String column=result2.getString("attr_ref_4_type");
    					det.put("refColumn4", result2.getString("attribute_Ref_4"));

    					if(column.split("_")[0].equalsIgnoreCase("LookUp")){
    						LookUpCode lookUpCode=lookUpCodeRepository.findOne(Long.valueOf(column.split("_")[1]));
    						det.put(lookUpCode.getMeaning().substring(0, 1).toLowerCase() + lookUpCode.getMeaning().substring(1).replaceAll(" ", ""), result2.getString("attribute_Ref_4"));
    					}
    					else if(column.split("_")[0].equalsIgnoreCase("column"))
    					{
    						DataViewsColumns dvc=dataViewsColumnsRepository.findOne(Long.valueOf(column.split("_")[1]));
    						det.put(dvc.getColumnName().substring(0, 1).toLowerCase() + dvc.getColumnName().substring(1).replaceAll(" ", ""), result2.getString("attribute_Ref_4"));

    					}
    				}
    				if(result2.getString("attr_ref_5_type")!=null)
    				{
    					String column=result2.getString("attr_ref_5_type");
    					det.put("refColumn5", result2.getString("attribute_Ref_5"));

    					if(column.split("_")[0].equalsIgnoreCase("LookUp")){
    						LookUpCode lookUpCode=lookUpCodeRepository.findOne(Long.valueOf(column.split("_")[1]));
    						det.put(lookUpCode.getMeaning().substring(0, 1).toLowerCase() + lookUpCode.getMeaning().substring(1).replaceAll(" ", ""), result2.getString("attribute_Ref_5"));
    					}
    					else if(column.split("_")[0].equalsIgnoreCase("column"))
    					{
    						DataViewsColumns dvc=dataViewsColumnsRepository.findOne(Long.valueOf(column.split("_")[1]));
    						det.put(dvc.getColumnName().substring(0, 1).toLowerCase() + dvc.getColumnName().substring(1).replaceAll(" ", ""), result2.getString("attribute_Ref_5"));

    					}
    				}
    				if(result2.getString("attr_ref_6_type")!=null)
    				{
    					String column=result2.getString("attr_ref_6_type");
    					det.put("refColumn6", result2.getString("attribute_Ref_6"));

    					if(column.split("_")[0].equalsIgnoreCase("LookUp")){
    						LookUpCode lookUpCode=lookUpCodeRepository.findOne(Long.valueOf(column.split("_")[1]));
    						det.put(lookUpCode.getMeaning().substring(0, 1).toLowerCase() + lookUpCode.getMeaning().substring(1).replaceAll(" ", ""), result2.getString("attribute_Ref_6"));
    					}
    					else if(column.split("_")[0].equalsIgnoreCase("column"))
    					{
    						DataViewsColumns dvc=dataViewsColumnsRepository.findOne(Long.valueOf(column.split("_")[1]));
    						det.put(dvc.getColumnName().substring(0, 1).toLowerCase() + dvc.getColumnName().substring(1).replaceAll(" ", ""), result2.getString("attribute_Ref_6"));

    					}
    				}

    				if(result2.getString("debit_amount")!=null)
    				{
    					det.put("enteredDebit", Double.valueOf(result2.getString("debit_amount")));
    				}
    				else
    					det.put("enteredDebit", 0d);
    				if(result2.getString("credit_amount")!=null)
    				{
    					det.put("enteredCredit",Double.valueOf(result2.getString("credit_amount")));
    				}
    				else
    					det.put("enteredCredit",0d);

    			}
    			jelineDetailsList.add(det);
    		}
    	}
    	catch(Exception e)
    	{
    		log.info("Exception while fetching data in acctDrillDown :"+e);
    	}
    	finally
    	{
    		if(result2!=null)
    			try {
    				result2.close();
    			} catch (SQLException e) {
    				// TODO Auto-generated catch block
    				log.info("Exception while closing result2 in acctDrillDown "+e);
    				e.printStackTrace();
    			}
    		if(stmt!=null)
    			try {
    				stmt.close();
    			} catch (SQLException e) {
    				// TODO Auto-generated catch block
    				log.info("Exception while closing stmt in acctDrillDown "+e);
    				e.printStackTrace();
    			}
    		if(conn!=null)
    			try {
    				conn.close();
    			} catch (SQLException e) {
    				// TODO Auto-generated catch block
    				log.info("Exception while closing conn in acctDrillDown "+e);
    				e.printStackTrace();

    			}
    	}
    	return jelineDetailsList;
			}
    	
    
    
    
    
    
    @GetMapping("/getJeHeaderAndLines")
    @Timed
    public List<LinkedHashMap> getJeHeaderAndLines(@RequestParam(value = "glPeriod" , required = false) List<String> glPeriod,@RequestParam(value = "glDate" , required = false) List<String> glDate, @RequestParam Long tenantId)
    {
    	log.info("rest request to get journal Header and lines");
    	List<JournalsHeaderData> jHDList=new ArrayList<JournalsHeaderData>();
    	if(glPeriod!=null && glDate==null)
    	{
    		log.info("glPeriod :"+glPeriod);
    		jHDList=journalsHeaderDataRepository.findByPeriodInAndTenantId(glPeriod,tenantId);

    	}
    	else if(glPeriod==null && glDate!=null)
    	{
    		if(glDate.size()==2)
    		{
    			log.info("glDate :"+glDate);
    			//ZonedDateTime fmDate=ZonedDateTime.parse(glDate.get(0).toString());
    			//ZonedDateTime toDate=ZonedDateTime.parse(glDate.get(1).toString());
    			LocalDate fmDate = LocalDate.parse(glDate.get(0).toString());
    			LocalDate toDate = LocalDate.parse(glDate.get(1).toString());
    			log.info("fmDate :"+fmDate);
    			log.info("toDate :"+toDate);
    			jHDList=journalsHeaderDataRepository.findByGlDateBetweenAndTenantId(fmDate,toDate,tenantId);
    		}
    		else
    		{
    			log.info("give from and to dates");
    		}
    	}
    	else if(glPeriod!=null && glDate!=null)
    	{
    		
    		if(glDate.size()==2)
    		{
    			ZonedDateTime glFmDate = ZonedDateTime.parse(glDate.get(0).toString());
    			ZonedDateTime glToDate = ZonedDateTime.parse(glDate.get(1).toString());
    			
    			List<Calendar> calenderList=calendarRepository.findByTenantAndActiveState(tenantId);
    			List<Periods> periodsList=new ArrayList<Periods>();
    			for(Calendar cal:calenderList)
    			{
    				periodsList=periodsRepository.findByCalId(cal.getId());
    				
    			}
    			List<String> periods=new ArrayList<String>();
    			for(Periods per:periodsList)
    			{
    				//looping through give input and then checking the condition
    				for(String glPer:glPeriod)
    				{
    				if((glPer.equalsIgnoreCase(per.getPeriodName())) && per.getFromDate().isAfter(glFmDate) && per.getToDate().isBefore(glToDate))
    				{
    					periods.add(glPer);
    					
    					
    				}
    				}
    			}
    			
    			log.info("periods :"+periods);
    			jHDList=journalsHeaderDataRepository.findByGlDateBetweenAndTenantIdAndPeriodIn(glFmDate,glToDate,tenantId,periods);
    			
    		}
    	}
    	log.info("jHDList :"+jHDList.size());
    	List<LinkedHashMap> jhdMapList=new ArrayList<LinkedHashMap>();
    	for(JournalsHeaderData jHD:jHDList)
    	{
    		LinkedHashMap jHDMap=new LinkedHashMap();
    		if(jHD.getJeBatchName()!=null)
    			jHDMap.put("jeBatchName", jHD.getJeBatchName());
    		else
    			jHDMap.put("jeBatchName", "");
    		if(jHD.getLedgerName()!=null)
    			jHDMap.put("ledgerName", jHD.getLedgerName());
    		else
    			jHDMap.put("ledgerName", "");
    		if(jHD.getSource()!=null)
    			jHDMap.put("source", jHD.getSource());
    		else
    			jHDMap.put("source", "");
    		if(jHD.getCategory()!=null)
    			jHDMap.put("categoryName", jHD.getCategory());
    		else
    			jHDMap.put("categoryName", "");
    		if(jHD.getGlDate()!=null)
    			jHDMap.put("glDate", jHD.getGlDate());
    		else
    			jHDMap.put("glDate","");
    		if(jHD.getCurrency()!=null)
    			jHDMap.put("currency", jHD.getCurrency());
    		else
    			jHDMap.put("currency", "");
    		if(jHD.getPeriod()!=null)
    			jHDMap.put("glPeriod", jHD.getPeriod());
    		else
    			jHDMap.put("glPeriod", "");
    		if(jHD.getConversionType()!=null)
    			jHDMap.put("conversionType", jHD.getConversionType());
    		else
    			jHDMap.put("conversionType", "");

    		List<LinkedHashMap> jeLineMapList=new ArrayList<LinkedHashMap>();
    		List<JeLines> jeLinesList=jeLinesRepository.findByJeHeaderId(jHD.getId());
    		for(JeLines jeLines:jeLinesList)
    		{
    			LinkedHashMap jeLineMap=new LinkedHashMap();
    			if(jeLines.getCodeCombination()!=null)
    				jeLineMap.put("codeCombination", jeLines.getCodeCombination());
    			else
    				jeLineMap.put("codeCombination", "");
    			if(jeLines.getDebitAmount()!=null)
    				jeLineMap.put("enteredDebit", jeLines.getDebitAmount());
    			else
    				jeLineMap.put("enteredDebit", "");
    			if(jeLines.getCreditAmount()!=null)
    				jeLineMap.put("enteredCredit", jeLines.getCreditAmount());
    			else
    				jeLineMap.put("enteredCredit", "");
    			if(jeLines.getAccountedDebit()!=null)
    				jeLineMap.put("acctDebit", jeLines.getAccountedDebit());
    			else
    				jeLineMap.put("acctDebit", "");
    			if(jeLines.getAccountedCredit()!=null)
    				jeLineMap.put("acctCredit", jeLines.getAccountedCredit());
    			else
    				jeLineMap.put("acctCredit","");
    			if(jeLines.getAttributeRef1()!=null)
    				jeLineMap.put("attributeRef1", jeLines.getAttributeRef1());
    			else
    				jeLineMap.put("attributeRef1", "");
    			if(jeLines.getAttributeRef2()!=null)
    				jeLineMap.put("attributeRef2", jeLines.getAttributeRef2());
    			else
    				jeLineMap.put("attributeRef2", "");
    			if(jeLines.getAttributeRef3()!=null)
    				jeLineMap.put("attributeRef3", jeLines.getAttributeRef3());
    			else
    				jeLineMap.put("attributeRef3", "");
    			if(jeLines.getAttributeRef5()!=null)
    				jeLineMap.put("attributeRef5", jeLines.getAttributeRef5());
    			else
    				jeLineMap.put("attributeRef5", "");
    			if(jeLines.getAttributeRef6()!=null)
    				jeLineMap.put("attributeRef6", jeLines.getAttributeRef6());
    			else
    				jeLineMap.put("attributeRef6","");

    			jeLineMapList.add(jeLineMap);

    		}
    		jHDMap.put("lineDetails",jeLineMapList);
    		jhdMapList.add(jHDMap);

    	}
    	return jhdMapList;

    }
    
    
  /*  @PostMapping("/testReportJsonToCSVTest")
    @Timed
    public void testReportJsonToCSVTest() throws IOException
	   { try (
	            BufferedWriter writer = Files.newBufferedWriter(Paths.get("/home/nspl/temp/sample.csv"));

	            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
	                    .withHeader("ID", "Name", "Designation", "Company"));
	        ) {
	            csvPrinter.printRecord("1", "Sundar Pichai ♥", "CEO", "123,145");
	            csvPrinter.printRecord("2", "Satya Nadella", "CEO", "Microsoft");
	            csvPrinter.printRecord("3", "Tim cook", "CEO", "Apple");

	            csvPrinter.printRecord(Arrays.asList("4", "Mark Zuckerberg", "CEO", "Facebook"));

	            csvPrinter.flush();            
	        }}
    
    
    @PostMapping("/testReportJsonToCSV")
    @Timed
    public void testReportJsonToCSV(@RequestBody LinkedHashMap map) throws IOException
    { 


    	List<HashMap> header=(List<HashMap>) map.get("columns");
    	List<HashMap> values=(List<HashMap>) map.get("data");
    	List<String> head=new ArrayList<String>();
    	for(int i=0;i<header.size();i++)
    	{
    		head.add(header.get(i).get("field").toString());
    	}

    	log.info("head :"+head);



    	String commaSeparated = head.stream()
    			.collect(Collectors.joining(","));
    	log.info("commaSeparated :"+commaSeparated);
    	try (
    			BufferedWriter writer = Files.newBufferedWriter(Paths.get("/home/nspl/temp/sample.csv"));

    			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
    					.withHeader(commaSeparated));
    			) {

    		for(int j=0;j<values.size();j++)
    		{
    			List<String> valuesList=new ArrayList<String>();
    			for(String hea:head)
    			{
    				valuesList.add(values.get(j).get(hea).toString());
    			}
    			log.info("valuesList :"+valuesList);
    			commaSeparated=valuesList.stream()
    					.collect(Collectors.joining("\",\""));
    			log.info("commaSeparated :"+commaSeparated);
    			csvPrinter.printRecord("5,039.25","DEBIT","PAYMENTECH       DES:TRANSFER   ID:Pinte0002723100 INDN:Pinterest Fees          CO ID:1020401225 CCD","Accounted","Un-Reconciled","","","","","5,039.25");
    		}
    		 csvPrinter.printRecord("2", "Satya Nadella", "CEO", "Microsoft");
	            csvPrinter.printRecord("3", "Tim cook", "CEO", "Apple");

	            csvPrinter.printRecord(Arrays.asList("4", "Mark Zuckerberg", "CEO", "Facebook"));

    		csvPrinter.flush();            
    	}}
    
    @PostMapping("/testReportJsonToCSVTestFinal")
    @Timed
    public void testReportJsonToCSVTestFinal(@RequestBody LinkedHashMap map) throws IOException
    { 


    	List<HashMap> header=(List<HashMap>) map.get("columns");
    	List<HashMap> values=(List<HashMap>) map.get("data");
    	List<String> head=new ArrayList<String>();
    	for(int i=0;i<header.size();i++)
    	{
    		head.add(header.get(i).get("field").toString());
    	}

    	log.info("head :"+head);



    	String commaSeparated = head.stream()
    			.collect(Collectors.joining("\",\""));
    	log.info("commaSeparated :"+commaSeparated);
    	try (
    			BufferedWriter writer = Files.newBufferedWriter(Paths.get("/home/nspl/temp/sample.csv"));

    			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
    					.withHeader("\""+commaSeparated+"\""));
    			) {

    		for(int j=0;j<values.size();j++)
    		{
    			List<String> valuesList=new ArrayList<String>();
    			for(String hea:head)
    			{
    				valuesList.add(values.get(j).get(hea).toString());
    			}
    			log.info("valuesList :"+valuesList);
    			commaSeparated=valuesList.stream()
    					.collect(Collectors.joining("\",\""));
    			log.info("commaSeparated :"+commaSeparated);
    			csvPrinter.printRecord("\""+commaSeparated+"\"");
    		}

    		csvPrinter.flush();            
    	}}
    */
    
}
    

