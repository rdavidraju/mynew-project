package com.nspl.app.web.rest;

import au.com.bytecode.opencsv.CSVReader;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.domain.LookUpCode;
import com.nspl.app.domain.LookUpType;
import com.nspl.app.domain.ReportDefination;
import com.nspl.app.domain.ReportType;
import com.nspl.app.domain.Reports;
import com.nspl.app.repository.DataViewsRepository;
import com.nspl.app.repository.LookUpCodeRepository;
import com.nspl.app.repository.ReportDefinationRepository;
import com.nspl.app.repository.ReportTypeRepository;
import com.nspl.app.repository.ReportsRepository;
import com.nspl.app.service.DataViewsService;
import com.nspl.app.service.PropertiesUtilService;
import com.nspl.app.service.UserJdbcService;
import com.nspl.app.web.rest.util.HeaderUtil;
import com.nspl.app.web.rest.util.PaginationUtil;

import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.DataFrameReader;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.RelationalGroupedDataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.functions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * REST controller for managing ReportType.
 */
@RestController
@RequestMapping("/api")
public class ReportTypeResource {

    private final Logger log = LoggerFactory.getLogger(ReportTypeResource.class);

    private static final String ENTITY_NAME = "reportType";

    private final ReportTypeRepository reportTypeRepository;
    
    @Inject
    private ReportsRepository reportsRepository;
    
    @Inject
    ReportDefinationRepository reportDefinationRepository;
    
    @Inject
    DataViewsRepository dataViewsRepository;
    
    @Inject
    PropertiesUtilService propertiesUtilService;
    
    @Inject
    DataViewsService dataViewsService;
    
    @Inject
    LookUpCodeRepository lookUpCodeRepository;
    
    @Inject
    private Environment env;
    
    @Inject
    UserJdbcService userJdbcService;

    public ReportTypeResource(ReportTypeRepository reportTypeRepository) {
        this.reportTypeRepository = reportTypeRepository;
    }

    /**
     * POST  /report-types : Create a new reportType.
     *
     * @param reportType the reportType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new reportType, or with status 400 (Bad Request) if the reportType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/report-types")
    @Timed
    public ResponseEntity<ReportType> createReportType(@RequestBody ReportType reportType) throws URISyntaxException {
        log.debug("REST request to save ReportType : {}", reportType);
        if (reportType.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new reportType cannot already have an ID")).body(null);
        }
        ReportType result = reportTypeRepository.save(reportType);
        return ResponseEntity.created(new URI("/api/report-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /report-types : Updates an existing reportType.
     *
     * @param reportType the reportType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated reportType,
     * or with status 400 (Bad Request) if the reportType is not valid,
     * or with status 500 (Internal Server Error) if the reportType couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/report-types")
    @Timed
    public ResponseEntity<ReportType> updateReportType(@RequestBody ReportType reportType) throws URISyntaxException {
        log.debug("REST request to update ReportType : {}", reportType);
        if (reportType.getId() == null) {
            return createReportType(reportType);
        }
        ReportType result = reportTypeRepository.save(reportType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, reportType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /report-types : get all the reportTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of reportTypes in body
     */
    @GetMapping("/report-types")
    @Timed
    public ResponseEntity<List<ReportType>> getAllReportTypes(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of ReportTypes");
        Page<ReportType> page = reportTypeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/report-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /report-types/:id : get the "id" reportType.
     *
     * @param id the id of the reportType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the reportType, or with status 404 (Not Found)
     */
    @GetMapping("/report-types/{id}")
    @Timed
    public ResponseEntity<ReportType> getReportType(@PathVariable Long id) {
        log.debug("REST request to get ReportType : {}", id);
        ReportType reportType = reportTypeRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(reportType));
    }

    /**
     * DELETE  /report-types/:id : delete the "id" reportType.
     *
     * @param id the id of the reportType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/report-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteReportType(@PathVariable Long id) {
        log.debug("REST request to delete ReportType : {}", id);
        reportTypeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    /**
     * Author: Swetha
     * Api to retrieve reportTypes by TenantId
     * @param tenantId
     * @return
     */
    @GetMapping("/reportTypesByTenantId")
    @Timed
    public List<HashMap> getReportTypesByTenantId(HttpServletRequest request) {
    	HashMap map0=userJdbcService.getuserInfoFromToken(request);
      	Long tenantId=Long.parseLong(map0.get("tenantId").toString());
        log.debug("REST request to get getReportTypesByTenantId : {}", tenantId);
        List<HashMap> reportTypesMapList=new ArrayList<HashMap>();
        List<ReportType> reportTypesList = reportTypeRepository.fetchActiveReportTypesByTenant(tenantId);
        for(int i=0;i<reportTypesList.size();i++){
        	ReportType reportType=reportTypesList.get(i);
        	List<String> coaList=new ArrayList<String>();
        	HashMap map=new HashMap();
        	map.put("id",reportType.getId());
        	map.put("allowDrillDown",reportType.getAllowDrillDown());
        	map.put("coa",reportType.getCoa());
        	if(reportType.getCoa()!=null && reportType.getCoa()==true){
        		List<String> lookupCode=lookUpCodeRepository.fetchLookupsByTenantIdAndLookUpType(tenantId,"CHART_OF_ACCOUNTS");
        		map.put("coaList",lookupCode);
        	}
        	map.put("createdBy",reportType.getCreatedBy());
        	map.put("creationDate",reportType.getCreationDate());
        	map.put("enableFlag",reportType.isEnableFlag());
        	map.put("endDate",reportType.getEndDate());
        	map.put("lastUpdatedBy",reportType.getLastUpdatedBy());
        	map.put("lasteUpdatedDate",reportType.getLasteUpdatedDate());
        	map.put("mode",reportType.getMode());
        	map.put("show",reportType.getShow());
        	map.put("startDate",reportType.getStartDate());
        	map.put("tenantId",reportType.getTenantId());
        	map.put("type",reportType.getType());
        	map.put("reconcile",reportType.getReconcile());
        	map.put("typeDisplayName",reportType.getDisplayName());
        	reportTypesMapList.add(map);
        }
        return reportTypesMapList;
    }
    
    
    /**
     * Author Kiran
     */
    @PostMapping("/saveReportTypes")
    @Timed
    public void saveReportTypes(@RequestParam Long tenantId, @RequestParam Long userId)
    {
    	log.info("Rest request to post Report type for tenantId: "+tenantId);
    	
    	InputStreamReader inputStream = new  InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("jsonFile/reportTypes.csv"));
		if(inputStream!=null)
		{
			CSVReader csvReader = new CSVReader(inputStream, ',' , '"');

			//Getting indexes of every field from csv file
			int TypeIndx = 0;
			int enableFlgIndx = 0;
			int StrtDtIndx = 0;
			int EndDtIndx = 0;
			int coaIndx = 0;
			int alwDrlDwnIndx = 0;
			int modeIndx = 0;
			int showIndx = 0;
			int reconIndx = 0;
			int dsplyNameIndx = 0;
			

			List<String[]> allRows;
			try {
				allRows = csvReader.readAll();
				csvReader.close();
				log.info("All Rows Size in Report types csv: "+ allRows.size());
				if(allRows.size()>0)
				{
					//Header Row
					String[] header = allRows.get(0);
					log.info("Header Length: "+ header.length);
					for(int i=0; i<header.length; i++)
					{
						if("type".equalsIgnoreCase(header[i].toString()))
							TypeIndx = i;
						else if("enable_flag".equalsIgnoreCase(header[i].toString()))
							enableFlgIndx = i;
						else if("start_date".equalsIgnoreCase(header[i].toString()))
							StrtDtIndx = i;
						else if("end_date".equalsIgnoreCase(header[i].toString()))
							EndDtIndx = i;
						else if("coa".equalsIgnoreCase(header[i].toString()))
							coaIndx = i;
						else if("allow_drill_down".equalsIgnoreCase(header[i].toString()))
							alwDrlDwnIndx = i;
						else if("mode".equalsIgnoreCase(header[i].toString()))
							modeIndx = i;
						else if("show".equalsIgnoreCase(header[i].toString()))
							showIndx = i;
						else if("reconcile".equalsIgnoreCase(header[i].toString()))
							reconIndx = i;
						else if("display_name".equalsIgnoreCase(header[i].toString()))
							dsplyNameIndx = i;
					}
					log.info("Indexs: type["+TypeIndx+"], "+"enable_flag["+enableFlgIndx+"], start_date["+StrtDtIndx+"], end_date["+EndDtIndx+"], "+"coa["+coaIndx+"], "+
							"allow_drill_down["+alwDrlDwnIndx+"], mode["+modeIndx+"], show["+showIndx+"],reconcile["+reconIndx+"],display_name["+dsplyNameIndx+"]" );
					if(allRows.size()>1)
					{
						List<ReportType> reportTypesList = new ArrayList<ReportType>();
						for(int j=1; j<allRows.size(); j++)
						{
							ReportType rptTyp = new ReportType();
							String[] row = allRows.get(j);
							
							if("NULL".equalsIgnoreCase(row[TypeIndx].toString()) || (row[TypeIndx].toString().isEmpty()))
								rptTyp.setType(null);
							else
								rptTyp.setType(row[TypeIndx].toString());
							
							if("NULL".equalsIgnoreCase(row[enableFlgIndx].toString()) || (row[enableFlgIndx].toString().isEmpty()))
								rptTyp.setEnableFlag(null);
							else
								rptTyp.setEnableFlag(Boolean.valueOf(row[enableFlgIndx].toString()));
							
							if("NULL".equalsIgnoreCase(row[StrtDtIndx].toString()) || (row[StrtDtIndx].toString().isEmpty()))
								rptTyp.setStartDate(null);
							else
								rptTyp.setStartDate(ZonedDateTime.parse(row[StrtDtIndx].toString()));

							if("NULL".equalsIgnoreCase(row[EndDtIndx].toString()) || (row[EndDtIndx].toString().isEmpty()))
								rptTyp.setEndDate(null);
							else
								rptTyp.setEndDate(ZonedDateTime.parse(row[EndDtIndx].toString()));
							
							rptTyp.setTenantId(tenantId);
							rptTyp.setCreationDate(ZonedDateTime.now());
							rptTyp.setCreatedBy(userId);
							rptTyp.setLastUpdatedBy(userId);
							rptTyp.setLasteUpdatedDate(ZonedDateTime.now());
							
							if("NULL".equalsIgnoreCase(row[coaIndx].toString()) || (row[coaIndx].toString().isEmpty()))
								rptTyp.setCoa(null);
							else
								rptTyp.setCoa(Boolean.valueOf(row[coaIndx].toString()));
							
							if("NULL".equalsIgnoreCase(row[alwDrlDwnIndx].toString()) || (row[alwDrlDwnIndx].toString().isEmpty()))
								rptTyp.setAllowDrillDown(null);
							else
								rptTyp.setAllowDrillDown(Boolean.valueOf(row[alwDrlDwnIndx].toString()));
							
							if("NULL".equalsIgnoreCase(row[dsplyNameIndx].toString()) || (row[dsplyNameIndx].toString().isEmpty()))
								rptTyp.setDisplayName(null);
							else
								rptTyp.setDisplayName(row[dsplyNameIndx].toString());
							
							if("NULL".equalsIgnoreCase(row[modeIndx].toString()) || (row[modeIndx].toString().isEmpty()))
								rptTyp.setMode(null);
							else
								rptTyp.setMode(Boolean.valueOf(row[modeIndx].toString()));
							
							if("NULL".equalsIgnoreCase(row[reconIndx].toString()) || (row[reconIndx].toString().isEmpty()))
								rptTyp.setReconcile(null);
							else
								rptTyp.setReconcile(Boolean.valueOf(row[reconIndx].toString()));
							
							if("NULL".equalsIgnoreCase(row[showIndx].toString()) || (row[showIndx].toString().isEmpty()))
								rptTyp.setShow(null);
							else
								rptTyp.setShow(Boolean.valueOf(row[showIndx].toString()));
							
							reportTypesList.add(rptTyp);
						}
						log.info("reportTypesList size "+reportTypesList.size());
						reportTypeRepository.save(reportTypesList);
						
						if(inputStream!=null)
						{
							try {
								inputStream.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			}catch(IOException e)
			{
				log.error("Printing IOException in ReportTypes: ",e);
				e.printStackTrace();
			}
		}
    }
    
}
