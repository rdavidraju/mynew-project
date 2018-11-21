package com.nspl.app.web.rest;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

import au.com.bytecode.opencsv.CSVReader;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.domain.FxRates;
import com.nspl.app.domain.FxRatesDetails;
import com.nspl.app.repository.FxRatesDetailsRepository;
import com.nspl.app.repository.FxRatesRepository;
import com.nspl.app.security.IDORUtils;
import com.nspl.app.service.ActiveStatusService;
import com.nspl.app.service.FileExportService;
import com.nspl.app.service.UserJdbcService;
import com.nspl.app.web.rest.dto.ErrorReporting;
import com.nspl.app.web.rest.dto.FxRatesDTO;
import com.nspl.app.web.rest.util.HeaderUtil;
import com.nspl.app.web.rest.util.PaginationUtil;

/**
 * REST controller for managing FxRates.
 */
@RestController
@RequestMapping("/api")
public class FxRatesResource {

    private final Logger log = LoggerFactory.getLogger(FxRatesResource.class);

    private static final String ENTITY_NAME = "fxRates";
        
    private final FxRatesRepository fxRatesRepository;
    
    @Inject
    FxRatesDetailsRepository fxRatesDetailsRepository;
    
    @Inject
	UserJdbcService userJdbcService;
    
    @Inject
    FileExportService fileExportService;
    
    
    @Inject
    ActiveStatusService activeStatusService;
    

    public FxRatesResource(FxRatesRepository fxRatesRepository) {
        this.fxRatesRepository = fxRatesRepository;
    }

    /**
     * POST  /fx-rates : Create a new fxRates.
     *
     * @param fxRates the fxRates to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fxRates, or with status 400 (Bad Request) if the fxRates has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/fx-rates")
    @Timed
    public ResponseEntity<FxRates> createFxRates(@RequestBody FxRates fxRates) throws URISyntaxException {
        log.debug("REST request to save FxRates : {}", fxRates);
        if (fxRates.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new fxRates cannot already have an ID")).body(null);
        }
        FxRates result = fxRatesRepository.save(fxRates);
		String idForDisplay = IDORUtils.computeFrontEndIdentifier(result.getId().toString());
		result.setIdForDisplay(idForDisplay);
		result = fxRatesRepository.save(result);
        return ResponseEntity.created(new URI("/api/fx-rates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /fx-rates : Updates an existing fxRates.
     *
     * @param fxRates the fxRates to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated fxRates,
     * or with status 400 (Bad Request) if the fxRates is not valid,
     * or with status 500 (Internal Server Error) if the fxRates couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/fx-rates")
    @Timed
    public /*ResponseEntity<FxRates>*/HashMap updateFxRates(@RequestBody FxRatesDTO fxRatesDTO,HttpServletRequest request) throws URISyntaxException {
    	HashMap hm = new HashMap();
        log.debug("REST request to update FxRates : {}", fxRatesDTO);
        HashMap map=userJdbcService.getuserInfoFromToken(request);
      	Long userId=Long.parseLong(map.get("userId").toString());
      	Long tenantId=Long.parseLong(map.get("tenantId").toString());
      	if(fxRatesDTO.getId() == null)
      	{
      		FxRates fxRates = new FxRates();
      		fxRates.setTenantId(tenantId);
      		if(fxRatesDTO.getName() != null && !fxRatesDTO.getName().isEmpty())
      			fxRates.setName(fxRatesDTO.getName());
      		if(fxRatesDTO.getDescription() != null )
      			fxRates.setDescription(fxRatesDTO.getDescription());
      		if(fxRatesDTO.getConversionType() != null)
      			fxRates.setConversionType(fxRatesDTO.getConversionType());
      		if(fxRatesDTO.getStartDate() != null)
      			fxRates.setStartDate(fxRatesDTO.getStartDate());
      		if(fxRatesDTO.getEndDate() != null)
      			fxRates.setEndDate(fxRatesDTO.getEndDate());
      		fxRates.setCreatedBy(userId);
      		fxRates.setLastUpdatedBy(userId);
      		fxRates.setCreatedDate(ZonedDateTime.now());
      		fxRates.setLastUpdatedDate(ZonedDateTime.now());
      		FxRates result = fxRatesRepository.save(fxRates);
    		String idForDisplay = IDORUtils.computeFrontEndIdentifier(result.getId().toString());
    		result.setIdForDisplay(idForDisplay);
    		FxRates resultUpdated = fxRatesRepository.save(result);
      	}
      	else
      	{
      		FxRates fxRates = fxRatesRepository.findByTenantIdAndIdForDisplay(tenantId, fxRatesDTO.getId());
      		if(fxRates != null)
      		{
          		if(fxRatesDTO.getName() != null && !fxRatesDTO.getName().isEmpty())
          			fxRates.setName(fxRatesDTO.getName());
          		if(fxRatesDTO.getDescription() != null )
          			fxRates.setDescription(fxRatesDTO.getDescription());
          		if(fxRatesDTO.getConversionType() != null)
          			fxRates.setConversionType(fxRatesDTO.getConversionType());
          		if(fxRatesDTO.getStartDate() != null)
          			fxRates.setStartDate(fxRatesDTO.getStartDate());
          		if(fxRatesDTO.getEndDate() != null)
          			fxRates.setEndDate(fxRatesDTO.getEndDate());
          		if(fxRatesDTO.getEnabledFlag() != null)
          			fxRates.setEnabledFlag(fxRatesDTO.getEnabledFlag());
          		fxRates.setLastUpdatedBy(userId);
          		fxRates.setLastUpdatedDate(ZonedDateTime.now());
      			fxRates.setIdForDisplay(fxRatesDTO.getId());
      			FxRates fxRatesUpdate = fxRatesRepository.save(fxRates);
        		hm.put("id", fxRatesUpdate.getIdForDisplay());
        		hm.put("name", fxRatesUpdate.getName());
      		}
      	}
      	return hm;
    }

    /**
     * GET  /fx-rates : get all the fxRates.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of fxRates in body
     */
    @GetMapping("/fx-rates")
    @Timed
    public ResponseEntity<List<FxRates>> getAllFxRates(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of FxRates");
        Page<FxRates> page = fxRatesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/fx-rates");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /fx-rates/:id : get the "id" fxRates.
     *
     * @param id the id of the fxRates to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fxRates, or with status 404 (Not Found)
     */
    @GetMapping("/fx-rates/{id}")
    @Timed
    public ResponseEntity<FxRates> getFxRates(@PathVariable Long id) {
        log.debug("REST request to get FxRates : {}", id);
        FxRates fxRates = fxRatesRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(fxRates));
    }

    /**
     * DELETE  /fx-rates/:id : delete the "id" fxRates.
     *
     * @param id the id of the fxRates to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/fx-rates/{id}")
    @Timed
    public ResponseEntity<Void> deleteFxRates(@PathVariable Long id) {
        log.debug("REST request to delete FxRates : {}", id);
        fxRatesRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    
    /**
     * author:ravali
     * Get all FxRates details with pagination
     * @param offset
     * @param limit
     * @param tenantId
     * @param response
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/getAllFxRates")
 	@Timed
 	public ResponseEntity<List<FxRatesDTO>> getRuleGroupsByTenantId(@RequestParam(value = "page" , required = false) Integer offset,
 			@RequestParam(value = "per_page", required = false) Integer limit,HttpServletResponse response,HttpServletRequest request) throws URISyntaxException {
 		log.debug("REST request to get a page of FxRates");
 		HashMap map=userJdbcService.getuserInfoFromToken(request);
 	  	Long tenantId=Long.parseLong(map.get("tenantId").toString());
 		List<FxRates> fxRateList = new ArrayList<FxRates>();
 		PaginationUtil paginationUtil=new PaginationUtil();
 		
 		int maxlmt=paginationUtil.MAX_LIMIT;
 		int minlmt=paginationUtil.MIN_OFFSET;
 		log.info("maxlmt: "+maxlmt);
 		Page<FxRates> page = null;
 		HttpHeaders headers = null;
 		
 		List<FxRates> fxRateListCnt = fxRatesRepository.findByTenantIdOrderByIdDesc(tenantId);
    	response.addIntHeader("X-COUNT", fxRateListCnt.size());
 		
 		if(limit==null || limit<minlmt){
 			fxRateList = fxRatesRepository.findByTenantIdOrderByIdDesc(tenantId);
 			limit = fxRateList.size();
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
 			 page = fxRatesRepository.findByTenantIdOrderByIdDesc(tenantId,PaginationUtil.generatePageRequest2(offset, limit));
 			headers = PaginationUtil.generatePaginationHttpHeaders2(page, "/api/functionalities",offset, limit);
 		}
 		else{
 			log.info("input limit is within maxlimit");
 			page = fxRatesRepository.findByTenantIdOrderByIdDesc(tenantId,PaginationUtil.generatePageRequest(offset, limit));
 			headers = PaginationUtil.generatePaginationHttpHeaderss(page, "/api/functionalities", offset, limit);
 		}
     	List<FxRatesDTO> fxRatesList = new ArrayList<FxRatesDTO>();
    	Iterator itr=page.iterator();
    	while(itr.hasNext()){
    		FxRatesDTO fxRates = new FxRatesDTO();
    		FxRates fr=(FxRates) itr.next();
    		if(fr.getIdForDisplay()!=null && !fr.getIdForDisplay().isEmpty())
    		fxRates.setId(fr.getIdForDisplay());
    		if(fr.getTenantId() != null)
    		fxRates.setTenantId(fr.getTenantId());
    		if(fr.getName() != null && !fr.getName().isEmpty())
    		fxRates.setName(fr.getName());
    		if(fr.getDescription() != null && !fr.getDescription().isEmpty())
    		fxRates.setDescription(fr.getDescription());
    		if(fr.getConversionType() != null && !fr.getConversionType().isEmpty())
    		fxRates.setConversionType(fr.getConversionType());
    		if(fr.getStartDate() != null)
    		fxRates.setStartDate(fr.getStartDate());
    		if(fr.getEndDate() != null)
    		fxRates.setEndDate(fr.getEndDate());
    		if(fr.isEnabledFlag() != null)
    		{
    			/** active check**/

    			Boolean activeStatus=activeStatusService.activeStatus(fr.getStartDate(), fr.getEndDate(), fr.isEnabledFlag());
    			fxRates.setEnabledFlag(activeStatus);
    			/** active check end**/
    		
    		}
    		if(fr.getCreatedBy() != null)
    		fxRates.setCreatedBy(fr.getCreatedBy());
    		if(fr.getCreatedDate() != null)
    		fxRates.setCreatedDate(fr.getCreatedDate());
    		if(fr.getLastUpdatedBy() != null)
    		fxRates.setLastUpdatedBy(fr.getLastUpdatedBy());
    		if(fr.getLastUpdatedDate() != null)
    		fxRates.setLastUpdatedDate(fr.getLastUpdatedDate());
    		List<FxRatesDetails> child = fxRatesDetailsRepository.findByFxRateId(fr.getId());
    		fxRates.setDetailsCount(child.size());
    		fxRatesList.add(fxRates);
    	}
			     	
     	return new ResponseEntity<>(fxRatesList, headers, HttpStatus.OK);
 		/*return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);*/
     }
    
    /**
     * Author : Shobha
     * @param tenantId
     * @param response
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/fetchfxRatesByTenant")
 	@Timed
 	public List<FxRates> fxRatesByTenant(HttpServletRequest request) throws URISyntaxException {
 		log.debug("REST request to get FxRates");
 		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
 		List<FxRates> fxRateListCnt = fxRatesRepository.findByTenantIdOrderByIdDesc(tenantId);
 		return fxRateListCnt;
    }
    
    
    @GetMapping("/fxRatesActiveByTenant")
 	@Timed
 	public List<FxRates> fxRatesActiveByTenant(HttpServletRequest request) throws URISyntaxException {
 		log.debug("REST request to get FxRates");
 		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
 		List<FxRates> fxRateListCnt = fxRatesRepository.findByTenantIdAndActiveOrderByIdDesc(tenantId);
 		return fxRateListCnt;
    }
    
    
    /**
     * author :ravali
     * Desc : API to get Fx rates and their details by fxRates Id
     * @param id
     * @return
     * @throws IOException 
     */
    @GetMapping("/getFxRatesAndFxRatesDetailsById")
   	@Timed
   	public HashMap getFxRatesAndFxRatesDetailsById(HttpServletRequest request, @RequestParam String id,@RequestParam boolean fileExport,@RequestParam(value="fileType",required=false) String fileType,HttpServletResponse response) throws IOException
    {
    	log.info("Rest Request to get FxRates And FxRatesDetails By Id :"+id);
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());


    	FxRates fxRates=fxRatesRepository.findByTenantIdAndIdForDisplay(tenantId, id);

    	HashMap fxRatesAndDetails=new HashMap();

    	

    		fxRatesAndDetails.put("id", fxRates.getIdForDisplay());
    		fxRatesAndDetails.put("conversionType", fxRates.getConversionType());
    		fxRatesAndDetails.put("createdBy",fxRates.getCreatedBy());
    		fxRatesAndDetails.put("createdDate", fxRates.getCreatedDate());
    		fxRatesAndDetails.put("description", fxRates.getDescription());
    		fxRatesAndDetails.put("enabledFlag", fxRates.isEnabledFlag());
    		fxRatesAndDetails.put("endDate", fxRates.getEndDate());
    		fxRatesAndDetails.put("lastUpdatedBy", fxRates.getLastUpdatedBy());
    		fxRatesAndDetails.put("lastUpdatedDate", fxRates.getLastUpdatedDate());
    		fxRatesAndDetails.put("name", fxRates.getName());
    		fxRatesAndDetails.put("startDate", fxRates.getStartDate());
    		fxRatesAndDetails.put("tenantId", fxRates.getTenantId());
    	
    	/*fxRatesAndDetails.put("idForDisplay", fxRatesAndDetails);*/

    	List<FxRatesDetails> fxRatesDetailList=fxRatesDetailsRepository.findByFxRateId(fxRates.getId());
    	List<LinkedHashMap> fxRatesDetailMapList=new ArrayList<LinkedHashMap>();
    	log.info("fxRatesDetailList :"+fxRatesDetailList.size());
    	for(FxRatesDetails fxRatesDetail:fxRatesDetailList)
    	{


    		if(fileExport)
    		{
    			LinkedHashMap fxRatesDetailMap=new LinkedHashMap();
    			fxRatesDetailMap.put("Name", fxRates.getName());
    			fxRatesDetailMap.put("Description", fxRates.getDescription());
    			fxRatesDetailMap.put("Conversion Type", fxRates.getConversionType());
    			fxRatesDetailMap.put("Start Date", fxRates.getStartDate());
    			fxRatesDetailMap.put("Status", fxRatesDetail.isEnabledFlag());
    			fxRatesDetailMap.put("From Currency", fxRatesDetail.getFromCurrency());
    			fxRatesDetailMap.put("To Currency", fxRatesDetail.getToCurrency());
    			fxRatesDetailMap.put("Conversion Rate", fxRatesDetail.getConversionRate());
    			fxRatesDetailMap.put("Inverse Rate", fxRatesDetail.getInverseRate());
    			fxRatesDetailMap.put("From Date", fxRatesDetail.getFromDate());
    			fxRatesDetailMap.put("To Date", fxRatesDetail.getToDate());
    			fxRatesDetailMap.put("id", fxRatesDetail.getId());
    		
    			fxRatesDetailMap.put("createdBy", fxRatesDetail.getCreatedBy());
    			fxRatesDetailMap.put("createdDate", fxRatesDetail.getCreatedDate());
    		
    			fxRatesDetailMap.put("From Date", fxRatesDetail.getFromDate());
    			fxRatesDetailMap.put("fxRateId", fxRatesDetail.getFxRateId());
    			
    			fxRatesDetailMap.put("lastUpdatedBy", fxRatesDetail.getLastUpdatedBy());
    			fxRatesDetailMap.put("lastUpdatedDate", fxRatesDetail.getLastUpdatedDate());
    			fxRatesDetailMap.put("Source", fxRatesDetail.getSource());
    			//fxRatesDetailMap.put("statusCode", fxRatesDetail.getStatusCode());
    			
    			
    			
    			fxRatesDetailMapList.add(fxRatesDetailMap);
    		}

    		else
    		{
    			LinkedHashMap fxRatesDetailMap=new LinkedHashMap();
    			fxRatesDetailMap.put("name", fxRates.getName());
    			fxRatesDetailMap.put("description", fxRates.getDescription());
    			fxRatesDetailMap.put("conversionType", fxRates.getConversionType());
    			fxRatesDetailMap.put("id", fxRatesDetail.getId());
    			fxRatesDetailMap.put("conversionRate", fxRatesDetail.getConversionRate());
    			fxRatesDetailMap.put("createdBy", fxRatesDetail.getCreatedBy());
    			fxRatesDetailMap.put("createdDate", fxRatesDetail.getCreatedDate());
    			fxRatesDetailMap.put("fromCurrency", fxRatesDetail.getFromCurrency());
    			fxRatesDetailMap.put("fromDate", fxRatesDetail.getFromDate());
    			
    			FxRates fr = fxRatesRepository.findOne(fxRatesDetail.getFxRateId());
    			fxRatesDetailMap.put("fxRateId", fr.getIdForDisplay());
    			
    			fxRatesDetailMap.put("inverseRate", fxRatesDetail.getInverseRate());
    			fxRatesDetailMap.put("lastUpdatedBy", fxRatesDetail.getLastUpdatedBy());
    			fxRatesDetailMap.put("lastUpdatedDate", fxRatesDetail.getLastUpdatedDate());
    			fxRatesDetailMap.put("source", fxRatesDetail.getSource());
    			//fxRatesDetailMap.put("statusCode", fxRatesDetail.getStatusCode());
    			fxRatesDetailMap.put("enabledFlag", fxRatesDetail.isEnabledFlag());
    			fxRatesDetailMap.put("toCurrency", fxRatesDetail.getToCurrency());
    			fxRatesDetailMap.put("toDate", fxRatesDetail.getToDate());
    			fxRatesDetailMapList.add(fxRatesDetailMap);
    		}
    	}
    	fxRatesAndDetails.put("fxRatesDetails", fxRatesDetailMapList);


    	if(fileExport)
    	{
    		LinkedHashMap keysList=fxRatesDetailMapList.get(0);

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
    			response.setHeader ("Content-Disposition", "attachment; filename=\"fxrates.csv\"");

    			fileExportService.jsonToCSV(fxRatesDetailMapList,keyList,response.getWriter());



    		}
    		if(fileType.equalsIgnoreCase("pdf"))
    		{
    			response.setContentType ("application/pdf");
    			response.setHeader ("Content-Disposition", "attachment; filename=\"fxrates.pdf\"");

    			fileExportService.jsonToCSV(fxRatesDetailMapList, keyList,response.getWriter());



    		}
    		else if(fileType.equalsIgnoreCase("excel"))
    		{
    			/*response.setContentType("application/vnd.ms-excel");
        		response.setHeader(
        				"Content-Disposition",
        				"attachment; filename=\"excel-export-fxrates.xlsx\""
        				);
        		fileExportService.jsonToCSV(fxRatesDetailMapList, keyList,response.getWriter());*/
    			response=fileExportService.exportToExcel(response, keyList, fxRatesDetailMapList);
    		}
    	}

    	return fxRatesAndDetails;
    }

    
    /**
     * author :ravali
     * @param fxRatesAndDetails
     * @param userId
     * @param tenantId
     * @return
     */
    
    @PostMapping("/postFxRatesAndFxRatesDetails")
   	@Timed
   	public HashMap postFxRatesAndFxRatesDetails(@RequestBody HashMap fxRatesAndDetails,HttpServletRequest request)
    {
    	log.info("Rest Request to post FxRates And FxRatesDetails :");
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
      	Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	Long userId=Long.parseLong(map.get("userId").toString());
    	HashMap finalMap=new HashMap();
    	FxRates fxRates=new FxRates();
    	if(fxRatesAndDetails.get("conversionType")!=null)
    		fxRates.setConversionType(fxRatesAndDetails.get("conversionType").toString());
    	fxRates.setCreatedBy(userId);
    	fxRates.setCreatedDate(ZonedDateTime.now());
    	if(fxRatesAndDetails.get("description")!=null)
    		fxRates.setDescription(fxRatesAndDetails.get("description").toString());
    	fxRates.setEnabledFlag(true);
    	if(fxRatesAndDetails.get("endDate")!=null)
    	{
    		ZonedDateTime stDate=ZonedDateTime.parse(fxRatesAndDetails.get("endDate").toString());
    		fxRates.setEndDate(stDate);
    	}
    	fxRates.setLastUpdatedBy(userId);
    	fxRates.setLastUpdatedDate(ZonedDateTime.now());
    	if(fxRatesAndDetails.get("name")!=null)
    		fxRates.setName(fxRatesAndDetails.get("name").toString());
    	if(fxRatesAndDetails.get("startDate")!=null)
    	{
    		ZonedDateTime edDate=ZonedDateTime.parse(fxRatesAndDetails.get("startDate").toString());
    		fxRates.setStartDate(edDate);
    	}
    	fxRates.setTenantId(tenantId);
    	FxRates result = fxRatesRepository.save(fxRates);
    	
		String idForDisplay = IDORUtils.computeFrontEndIdentifier(result.getId().toString());
		result.setIdForDisplay(idForDisplay);
		result = fxRatesRepository.save(result);
		
    	finalMap.put("id", fxRates.getIdForDisplay());
    	finalMap.put("name", fxRates.getName());
    	/*finalMap.put("idForDisplay", idForDisplay);*/


    	int count=0;
    	List<HashMap> fxRatesDetailMapList=(List<HashMap>) fxRatesAndDetails.get("fxRatesDetails");
    	if(fxRates.getId()!=null)
    	{
    		for(int i=0;i<fxRatesDetailMapList.size();i++)
    		{
    			count=count+1;
    			HashMap fxRatesDetailMap=fxRatesDetailMapList.get(i);
    			FxRatesDetails fxRatesDetail=new FxRatesDetails();

    			fxRatesDetail.setConversionRate(new BigDecimal(fxRatesDetailMap.get("conversionRate").toString()));
    			fxRatesDetail.setCreatedBy(userId);
    			fxRatesDetail.setCreatedDate(ZonedDateTime.now());
    			if(fxRatesDetailMap.get("fromCurrency")!=null)
    				fxRatesDetail.setFromCurrency(fxRatesDetailMap.get("fromCurrency").toString());
    			if(fxRatesDetailMap.get("fromDate")!=null)
    			{
    				ZonedDateTime fmDate=ZonedDateTime.parse(fxRatesDetailMap.get("fromDate").toString());
    				fxRatesDetail.setFromDate(fmDate);
    			}
    			fxRatesDetail.setFxRateId(fxRates.getId());
    			if(fxRatesDetailMap.get("inverseRate")!=null)
    				fxRatesDetail.setInverseRate(new BigDecimal(fxRatesDetailMap.get("inverseRate").toString()));
    			fxRatesDetail.setLastUpdatedBy(userId);
    			fxRatesDetail.setLastUpdatedDate(ZonedDateTime.now());
    			if(fxRatesDetailMap.get("source")!=null)
    				fxRatesDetail.setSource(fxRatesDetailMap.get("source").toString());
    			//if(fxRatesDetailMap.get("statusCode")!=null)
    			//fxRatesDetail.setStatusCode(fxRatesDetailMap.get("statusCode").toString());
    			fxRatesDetail.setEnabledFlag(true);
    			if(fxRatesDetailMap.get("toCurrency")!=null)
    				fxRatesDetail.setToCurrency(fxRatesDetailMap.get("toCurrency").toString());
    			if(fxRatesDetailMap.get("toDate")!=null)
    			{
    				ZonedDateTime toDate=ZonedDateTime.parse(fxRatesDetailMap.get("toDate").toString());
    				fxRatesDetail.setToDate(toDate);
    			}
    			fxRatesDetailsRepository.save(fxRatesDetail);
    		}
    	}
    	return finalMap;
    }
    

    /**
     * Author: Shiva
     * Description: Bulk uploading fx rates and fx rates details
     * @throws IOException 
     * @Param: tenantId, userId, multipartfile
     */
    @PostMapping("/bulkUploadForFxRates")
    @Timed
    public ErrorReporting bulkUploadForFxRates(@RequestParam MultipartFile multipart, HttpServletRequest request) throws IOException
    {
    	HashMap map = userJdbcService.getuserInfoFromToken(request);
    	Long tenantId = Long.parseLong(map.get("tenantId").toString());
    	Long userId = Long.parseLong(map.get("tenantId").toString());
    	log.info("Bulk upload API for fx rates for the tenant id: "+tenantId);
    	ErrorReporting errorReport  = new ErrorReporting();
    	List<String> reasons = new ArrayList<String>();
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
    	HashMap<String, List<HashMap>> fxRatesData = new HashMap<String, List<HashMap>>();
    	errorReport.setStatus("Success");
    	try{
    		CSVReader csvReader = new CSVReader(new InputStreamReader(multipart.getInputStream()), ',' , '"');
    		int nameIndx = 0; int desIndx = 0; int conTypeIndx = 0; int sDateIndx = 0; int eDateIndx = 0; int frmCurrencyIndx = 0; int toCurrencyIndx = 0;
    		int frmDtIndx = 0; int toDtIndx = 0; int conRateIndx = 0; int inverseRateIndx = 0;
			List<String[]> allRows = csvReader.readAll();
			log.info("All Rows Size: "+ allRows.size());
			
			if(allRows.size()>0)
			{
				log.info("Getting Header Row Indexes...");
				String[] header = allRows.get(0);
				for(int i=0; i<header.length; i++)
				{
					if("name".equalsIgnoreCase(header[i].toString()))
						nameIndx=i;
					else if("description".equalsIgnoreCase(header[i].toString()))
						desIndx=i;
					else if("conversion_type".equalsIgnoreCase(header[i].toString()))
						conTypeIndx=i;
					else if("start_date(dd/mm/yyyy)".equalsIgnoreCase(header[i].toString()))
						sDateIndx=i;
					else if("end_date(dd/mm/yyyy)".equalsIgnoreCase(header[i].toString()))
						eDateIndx=i;
					else if("from_currency".equalsIgnoreCase(header[i].toString()))
						frmCurrencyIndx=i;
					else if("to_currency".equalsIgnoreCase(header[i].toString()))
						toCurrencyIndx=i;
					else if("from_date(dd/mm/yyyy)".equalsIgnoreCase(header[i].toString()))
						frmDtIndx=i;
					else if("to_date(dd/mm/yyyy)".equalsIgnoreCase(header[i].toString()))
						toDtIndx=i;
					else if("conversion_rate".equalsIgnoreCase(header[i].toString()))
						conRateIndx=i;
					else if("inverse_rate".equalsIgnoreCase(header[i].toString()))
						inverseRateIndx=i;
				}
			}
			
			if(allRows.size()>1)
			{
				log.info("Data Validation Started...");
				List<String> names = new ArrayList<String>();
				for(int j=1; j<allRows.size(); j++)
				{
					try
					{
						String[] row = allRows.get(j);
						if(row[nameIndx] != null && !"NULL".equalsIgnoreCase(row[nameIndx].toString()) && row[nameIndx].toString().length()>0)
						{
							if(names.contains(row[nameIndx].toString().trim()))
							{
								List<HashMap> existedData = fxRatesData.get(row[nameIndx].toString().trim());
								HashMap fxrData = new HashMap();
								fxrData.put("name", row[nameIndx].toString().trim());
								if(row[desIndx] != null && !"NULL".equalsIgnoreCase(row[desIndx].toString()) && row[desIndx].toString().length()>0)
									fxrData.put("description", row[desIndx].toString().trim());
								if(row[conTypeIndx] != null && !"NULL".equalsIgnoreCase(row[conTypeIndx].toString()) && row[conTypeIndx].toString().length()>0)
								{	fxrData.put("conversionType", row[conTypeIndx].toString().trim());}
								else
								{								
									errorReport.setStatus("Failed");
									reasons.add("Invalid Conversion exist in row "+(j+1));
								}
								if(row[frmCurrencyIndx] != null && !"NULL".equalsIgnoreCase(row[frmCurrencyIndx].toString()) && row[frmCurrencyIndx].toString().length()>0)
								{ fxrData.put("fromCurrency", row[frmCurrencyIndx].toString().trim()); }
								else
								{
									errorReport.setStatus("Failed");
									reasons.add("Invalid From Currency exist in row "+(j+1));
								}
								if(row[toCurrencyIndx] != null && !"NULL".equalsIgnoreCase(row[toCurrencyIndx].toString()) && row[toCurrencyIndx].toString().length()>0)
								{ fxrData.put("toCurrency", row[toCurrencyIndx].toString().trim()); }
								else
								{	
									errorReport.setStatus("Failed");
									reasons.add("Invalid To Currency exist in row "+(j+1));
								}
								fxrData.put("startDate", LocalDate.parse(row[sDateIndx], formatter));
								if(row[eDateIndx].toString().length() == 0 || "null".equalsIgnoreCase(row[eDateIndx]))
								{	fxrData.put("endDate", null);	}
								else if(row[eDateIndx].toString().length()>0)
								{	
									fxrData.put("endDate", LocalDate.parse(row[eDateIndx].toString(), formatter));
								}
								fxrData.put("fromDate", LocalDate.parse(row[frmDtIndx], formatter));
								fxrData.put("toDate", LocalDate.parse(row[toDtIndx].toString(), formatter));
								fxrData.put("conversionRate", new BigDecimal(row[conRateIndx].toString().trim())); 
								fxrData.put("inverseRate", new BigDecimal(row[inverseRateIndx].toString().trim()));
								existedData.add(fxrData);
								fxRatesData.put(row[nameIndx].toString().trim(), existedData);
							}
							else
							{
								names.add(row[nameIndx].toString().trim());
								List<HashMap> mpDataList = new ArrayList<HashMap>();
								
								HashMap fxrData = new HashMap();
								fxrData.put("name", row[nameIndx].toString().trim());
								if(row[desIndx] != null && !"NULL".equalsIgnoreCase(row[desIndx].toString()) && row[desIndx].toString().length()>0)
									fxrData.put("description", row[desIndx].toString().trim());
								if(row[conTypeIndx] != null && !"NULL".equalsIgnoreCase(row[conTypeIndx].toString()) && row[conTypeIndx].toString().length()>0)
								{	fxrData.put("conversionType", row[conTypeIndx].toString().trim());}
								else
								{								
									errorReport.setStatus("Failed");
									reasons.add("Invalid Conversion exist in row "+(j+1));
								}
								if(row[frmCurrencyIndx] != null && !"NULL".equalsIgnoreCase(row[frmCurrencyIndx].toString()) && row[frmCurrencyIndx].toString().length()>0)
								{ fxrData.put("fromCurrency", row[frmCurrencyIndx].toString().trim()); }
								else
								{
									errorReport.setStatus("Failed");
									reasons.add("Invalid From Currency exist in row "+(j+1));
								}
								if(row[toCurrencyIndx] != null && !"NULL".equalsIgnoreCase(row[toCurrencyIndx].toString()) && row[toCurrencyIndx].toString().length()>0)
								{ fxrData.put("toCurrency", row[toCurrencyIndx].toString().trim()); }
								else
								{	
									errorReport.setStatus("Failed");
									reasons.add("Invalid To Currency exist in row "+(j+1));
								}
								fxrData.put("startDate", LocalDate.parse(row[sDateIndx], formatter));
								if(row[eDateIndx].toString().length() == 0 || "null".equalsIgnoreCase(row[eDateIndx]))
								{	fxrData.put("endDate", null);	}
								else if(row[eDateIndx].toString().length()>0)
								{	
									fxrData.put("endDate", LocalDate.parse(row[eDateIndx].toString(), formatter));
								}
								fxrData.put("fromDate", LocalDate.parse(row[frmDtIndx], formatter));
								fxrData.put("toDate", LocalDate.parse(row[toDtIndx].toString(), formatter));
								fxrData.put("conversionRate", new BigDecimal(row[conRateIndx].toString().trim())); 
								fxrData.put("inverseRate", new BigDecimal(row[inverseRateIndx].toString().trim()));
								
								mpDataList.add(fxrData);
								fxRatesData.put(row[nameIndx].toString().trim(), mpDataList);	// Key is MappingSet name, Values is MappingSet Data
							}
						}
						else
						{
							errorReport.setStatus("Failed");
							reasons.add("Invalid name exist in row "+(j+1));
						}
					}
					catch(DateTimeParseException e){
						errorReport.setStatus("Failed");
						reasons.add("Invalid date formate in row "+(j+1)+", Plase Use 'dd/mm/yyyy' format");
					}
					catch(NumberFormatException e1)
					{
						errorReport.setStatus("Failed");
						reasons.add("Invalid inversion/conversion rate exist in row "+(j+1));
					}
					catch(Exception e)
					{
						errorReport.setStatus("Failed");
						reasons.add("Un able to read the row "+(j+1));
					}
				}
				log.info("Data Validation End.");
				log.info("Data Validation Status: "+errorReport.getStatus());
				// log.info("Fx Rates Data: "+ fxRatesData);
				if("Success".equalsIgnoreCase(errorReport.getStatus()))
				{
					log.info("Moving data into the database...");
					try{
					log.info("FxRates Size: "+fxRatesData.size());
						if(fxRatesData.size()>0)
						{
							for(String name : fxRatesData.keySet())
							{
								List<HashMap> values = fxRatesData.get(name);
								HashMap fxRate = values.get(0);
								Long fxRatesId = null;
								FxRates fxRates = fxRatesRepository.findByTenantIdAndName(tenantId, name);
								if(fxRates != null)
								{
									if(fxRate.get("description") != null)
										fxRates.setDescription(fxRate.get("description").toString());
									if(fxRate.get("conversionType") != null)
										fxRates.setConversionType(fxRate.get("conversionType").toString());
									if(fxRate.get("startDate") != null)
									{
										LocalDate localDate = LocalDate.parse(fxRate.get("startDate").toString());
										/*fxRates.setStartDate(ZonedDateTime.parse(fxRate.get("startDate").toString()));*/
										fxRates.setStartDate(localDate.atStartOfDay(ZoneId.systemDefault()));
									}
									if(fxRate.get("endDate") != null)
									{
										LocalDate localDate = LocalDate.parse(fxRate.get("endDate").toString());
										fxRates.setEndDate(localDate.atStartOfDay(ZoneId.systemDefault()));
										/*fxRates.setEndDate(ZonedDateTime.parse(fxRate.get("endDate").toString()));*/
									}
									fxRates.setEnabledFlag(true);
									fxRates.setLastUpdatedBy(userId);
									fxRates.setLastUpdatedDate(ZonedDateTime.now());
									FxRates fxRateSave = fxRatesRepository.save(fxRates);

									String idForDisplay = IDORUtils.computeFrontEndIdentifier(fxRateSave.getId().toString());
									fxRateSave.setIdForDisplay(idForDisplay);
									fxRateSave = fxRatesRepository.save(fxRateSave);
									fxRatesId = fxRateSave.getId();
								}
								else
								{
									FxRates fxRatesNew = new FxRates();
									fxRatesNew.setTenantId(tenantId);
									if(fxRate.get("name") != null)
										fxRatesNew.setName(fxRate.get("name").toString());
									if(fxRate.get("description") != null)
										fxRatesNew.setDescription(fxRate.get("description").toString());
									if(fxRate.get("conversionType") != null)
										fxRatesNew.setConversionType(fxRate.get("conversionType").toString());
									if(fxRate.get("startDate") != null)
									{
										LocalDate localDate = LocalDate.parse(fxRate.get("startDate").toString());
										fxRatesNew.setStartDate(localDate.atStartOfDay(ZoneId.systemDefault()));
										/*fxRatesNew.setStartDate(ZonedDateTime.parse(fxRate.get("startDate").toString()));*/
									}
										
									if(fxRate.get("endDate") != null)
									{
										LocalDate localDate = LocalDate.parse(fxRate.get("endDate").toString());
										fxRatesNew.setEndDate(localDate.atStartOfDay(ZoneId.systemDefault()));
										/*fxRatesNew.setEndDate(ZonedDateTime.parse(fxRate.get("endDate").toString()));*/
									}
									fxRatesNew.setEnabledFlag(true);
									fxRatesNew.setCreatedBy(userId);
									fxRatesNew.setLastUpdatedBy(userId);
									fxRatesNew.setCreatedDate(ZonedDateTime.now());
									fxRatesNew.setLastUpdatedDate(ZonedDateTime.now());	
									FxRates fxRateSave = fxRatesRepository.save(fxRatesNew);

									String idForDisplay = IDORUtils.computeFrontEndIdentifier(fxRateSave.getId().toString());
									fxRateSave.setIdForDisplay(idForDisplay);
									fxRateSave = fxRatesRepository.save(fxRateSave);
									fxRatesId = fxRateSave.getId();
								}
								// FxRates fxRates = new FxRates();
								for(HashMap data : values)
								{
									FxRatesDetails fxRatesDetails = new FxRatesDetails();
									fxRatesDetails.setFxRateId(fxRatesId);
									if(data.get("fromCurrency") != null)
										fxRatesDetails.setFromCurrency(data.get("fromCurrency").toString());
									if(data.get("toCurrency") != null)
										fxRatesDetails.setToCurrency(data.get("toCurrency").toString());
									if(data.get("fromDate") != null)
									{
										LocalDate localDate = LocalDate.parse(data.get("fromDate").toString());
										fxRatesDetails.setFromDate(localDate.atStartOfDay(ZoneId.systemDefault()));
										/*fxRatesDetails.setFromDate(ZonedDateTime.parse(data.get("fromDate").toString()));*/
									}
									if(data.get("toDate") != null)
									{
										LocalDate localDate = LocalDate.parse(data.get("toDate").toString());
										fxRatesDetails.setToDate(localDate.atStartOfDay(ZoneId.systemDefault()));
										/*fxRatesDetails.setToDate(ZonedDateTime.parse(data.get("toDate").toString()));*/
									}
									if(data.get("conversionRate") != null)
										fxRatesDetails.setConversionRate(new BigDecimal(data.get("conversionRate").toString()));
									if(data.get("inverseRate") != null)
										fxRatesDetails.setInverseRate(new BigDecimal(data.get("inverseRate").toString()));
									fxRatesDetails.setEnabledFlag(true);
									fxRatesDetails.setCreatedBy(userId);
									fxRatesDetails.setLastUpdatedBy(userId);
									fxRatesDetails.setCreatedDate(ZonedDateTime.now());
									fxRatesDetails.setLastUpdatedDate(ZonedDateTime.now());
									FxRatesDetails fxRatesDetailsSave = fxRatesDetailsRepository.save(fxRatesDetails);
								}
							}
						}
						log.info("Data moved into the database...");
					}
					catch(Exception e)
					{
						log.info("Error occured while inserting data ");
						errorReport.setStatus("Failed");
						reasons.add("Error occured while inserting data");
					}
					
				}
			}
    	}
    	catch(Exception e)
    	{
    		log.info("Exception while reading data... "+e);
    		errorReport.setStatus("Failed");
    		reasons.add("Error while reading data");
    	}
    	errorReport.setReasons(reasons);
    	return errorReport;  
    }

    /**
     * Author: Shiva
     * Description: Bulk uploading fx rates and fx rates details
     * @Param: tenantId, userId, multipartfile
     */
    @PostMapping("/fxRatesBulkUpload")
    @Timed
    public ErrorReporting fxRatesBulkUpload(@RequestParam MultipartFile multipart, HttpServletRequest request)
    {
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
      	Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	Long userId=Long.parseLong(map.get("userId").toString());
    	log.info("Rest api for bulk uploading fx rates and fx rates details for the teanant id: "+ tenantId +", File name: "+multipart.getOriginalFilename());
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
    	ErrorReporting errorReport = new ErrorReporting();
    	List<String> reasons = new ArrayList<String>();
    	
    	List<Long> fxRatesIds = new ArrayList<Long>();
    	List<Long> fxRatesDetailsIds = new ArrayList<Long>();
    	
    	try{
    		CSVReader csvReader = new CSVReader(new InputStreamReader(multipart.getInputStream()), ',' , '"');
    		int nameIndx = 0;
    		int desIndx = 0;
    		int conTypeIndx = 0;
    		int sDateIndx = 0;
    		int eDateIndx = 0;
    		int frmCurrencyIndx = 0;
    		int toCurrencyIndx = 0;
    		int conRateIndx = 0;
    		int inverseRateIndx = 0;
    		int sourceIndx = 0;

			List<String[]> allRows = csvReader.readAll();
			log.info("All Rows Size: "+ allRows.size());
			if(allRows.size()>0)
			{
				log.info("Getting Header Row Indexes...");
				String[] header = allRows.get(0);
				for(int i=0; i<header.length; i++)
				{
					if("name".equalsIgnoreCase(header[i].toString()))
						nameIndx=i;
					else if("description".equalsIgnoreCase(header[i].toString()))
						desIndx=i;
					else if("conversion_type".equalsIgnoreCase(header[i].toString()))
						conTypeIndx=i;
					else if("start_date".equalsIgnoreCase(header[i].toString()))
						sDateIndx=i;
					else if("end_date".equalsIgnoreCase(header[i].toString()))
						eDateIndx=i;
					else if("from_currency".equalsIgnoreCase(header[i].toString()))
						frmCurrencyIndx=i;
					else if("to_currency".equalsIgnoreCase(header[i].toString()))
						toCurrencyIndx=i;
					else if("conversion_rate".equalsIgnoreCase(header[i].toString()))
						conRateIndx=i;
					else if("inverse_rate".equalsIgnoreCase(header[i].toString()))
						inverseRateIndx=i;
					else if("source".equalsIgnoreCase(header[i].toString()))
						sourceIndx=i;
				}
				log.info("Filed Indexes: name["+nameIndx+"], description["+desIndx+"], conversion_type["+conTypeIndx+"],"
						+ "start_date["+sDateIndx+"], end_date["+eDateIndx+"], from_currency["+frmCurrencyIndx+"], to_currency["+toCurrencyIndx+"], conversion_rate["+conRateIndx+"]"
						+", inverse_rate["+inverseRateIndx+"], source["+sourceIndx+"]");
			
				if(allRows.size()>1)
				{
					log.info("Reading data...");
					List<String> names = new ArrayList<String>();
					for(int j=1; j<allRows.size(); j++)
					{
						try{
							String[] row = allRows.get(j);
							if(!"NULL".equalsIgnoreCase(row[nameIndx].toString()) || !(row[nameIndx].toString().isEmpty()))
							{
								if(names.contains(row[nameIndx].toString().trim()))
								{
									FxRates fr = fxRatesRepository.findByTenantIdAndName(tenantId, row[nameIndx].toString());
									if(fr != null)
									{
										FxRatesDetails fxrDetails = new FxRatesDetails();
										fxrDetails.setFxRateId(fr.getId());
										if(!"NULL".equalsIgnoreCase(row[frmCurrencyIndx].toString()) || !(row[frmCurrencyIndx].toString().isEmpty()))
											fxrDetails.setFromCurrency(row[frmCurrencyIndx].toString());
										if(!"NULL".equalsIgnoreCase(row[toCurrencyIndx].toString()) || !(row[toCurrencyIndx].toString().isEmpty()))
											fxrDetails.setToCurrency(row[toCurrencyIndx].toString());
										if(!"NULL".equalsIgnoreCase(row[sDateIndx].toString()) || !(row[sDateIndx].toString().isEmpty()))
											fxrDetails.setFromDate(ZonedDateTime.parse(row[sDateIndx].toString(), formatter));
										if(!"NULL".equalsIgnoreCase(row[eDateIndx].toString()) || !(row[eDateIndx].toString().isEmpty()))
											fxrDetails.setToDate(ZonedDateTime.parse(row[eDateIndx].toString(), formatter));
										if(!"NULL".equalsIgnoreCase(row[conRateIndx].toString()) || !(row[conRateIndx].toString().isEmpty()))
											fxrDetails.setConversionRate(new BigDecimal(row[conRateIndx].toString()));
										if(!"NULL".equalsIgnoreCase(row[inverseRateIndx].toString()) || !(row[inverseRateIndx].toString().isEmpty()))
											fxrDetails.setInverseRate(new BigDecimal(row[inverseRateIndx].toString()));
										if(!"NULL".equalsIgnoreCase(row[sourceIndx].toString()) || !(row[sourceIndx].toString().isEmpty()))
											fxrDetails.setSource(row[sourceIndx].toString());
										fxrDetails.setCreatedBy(userId);
										fxrDetails.setCreatedDate(ZonedDateTime.now());
										fxrDetails.setLastUpdatedBy(userId);
										fxrDetails.setLastUpdatedDate(ZonedDateTime.now());
										fxrDetails.setEnabledFlag(true);
										FxRatesDetails createMSValues = fxRatesDetailsRepository.save(fxrDetails);
										fxRatesDetailsIds.add(createMSValues.getId());
									}
								}
								else
								{
									//log.info("Saving "+row[nameIndx].toString()+" in mapping set table...");
									FxRates fxRates = new FxRates();
									fxRates.setTenantId(tenantId);
									fxRates.setName(row[nameIndx].toString());
									if(!"NULL".equalsIgnoreCase(row[desIndx].toString()) || !(row[desIndx].toString().isEmpty()))
										fxRates.setDescription(row[desIndx].toString());
									if(!"NULL".equalsIgnoreCase(row[conTypeIndx].toString()) || !(row[conTypeIndx].toString().isEmpty()))
										fxRates.setConversionType(row[conTypeIndx].toString());
									if(!"NULL".equalsIgnoreCase(row[sDateIndx].toString()) || !(row[sDateIndx].toString().isEmpty()))
										fxRates.setStartDate(ZonedDateTime.parse(row[sDateIndx].toString(), formatter));
									if(!"NULL".equalsIgnoreCase(row[eDateIndx].toString()) || !(row[eDateIndx].toString().isEmpty()))
									{
										fxRates.setEndDate(ZonedDateTime.parse(row[eDateIndx].toString(), formatter));
									}
									else if("NULL".equalsIgnoreCase(row[eDateIndx].toString()) || (row[eDateIndx].toString().isEmpty()))
									{
										fxRates.setEndDate(null);
									}
									fxRates.setCreatedBy(userId);
									fxRates.setCreatedDate(ZonedDateTime.now());
									fxRates.setLastUpdatedBy(userId);
									fxRates.setLastUpdatedDate(ZonedDateTime.now());
									fxRates.setEnabledFlag(true);
									FxRates fr = fxRatesRepository.findByTenantIdAndName(tenantId, row[nameIndx].toString());
									FxRates createFxRates = new FxRates();
									if(fr != null)
									{
										fxRates.setId(fr.getId());
										createFxRates = fxRatesRepository.save(fxRates);
										fxRatesIds.add(createFxRates.getId());
									}
									else
									{
										createFxRates = fxRatesRepository.save(fxRates);
										fxRatesIds.add(createFxRates.getId());
									}

									FxRatesDetails fxrDetails = new FxRatesDetails();
									fxrDetails.setFxRateId(createFxRates.getId());
									if(!"NULL".equalsIgnoreCase(row[frmCurrencyIndx].toString()) || !(row[frmCurrencyIndx].toString().isEmpty()))
										fxrDetails.setFromCurrency(row[frmCurrencyIndx].toString());
									if(!"NULL".equalsIgnoreCase(row[toCurrencyIndx].toString()) || !(row[toCurrencyIndx].toString().isEmpty()))
										fxrDetails.setToCurrency(row[toCurrencyIndx].toString());
									if(!"NULL".equalsIgnoreCase(row[sDateIndx].toString()) || !(row[sDateIndx].toString().isEmpty()))
										fxrDetails.setFromDate(ZonedDateTime.parse(row[sDateIndx].toString(), formatter));
									if(!"NULL".equalsIgnoreCase(row[eDateIndx].toString()) || !(row[eDateIndx].toString().isEmpty()))
									{
										fxrDetails.setToDate(ZonedDateTime.parse(row[eDateIndx].toString(), formatter));
									}
									else if("NULL".equalsIgnoreCase(row[eDateIndx].toString()) || (row[eDateIndx].toString().isEmpty()))
									{
										fxrDetails.setToDate(null);
									}
									if(!"NULL".equalsIgnoreCase(row[conRateIndx].toString()) || !(row[conRateIndx].toString().isEmpty()))
										fxrDetails.setConversionRate(new BigDecimal(row[conRateIndx].toString()));
									if(!"NULL".equalsIgnoreCase(row[inverseRateIndx].toString()) || !(row[inverseRateIndx].toString().isEmpty()))
										fxrDetails.setInverseRate(new BigDecimal(row[inverseRateIndx].toString()));
									if(!"NULL".equalsIgnoreCase(row[sourceIndx].toString()) || !(row[sourceIndx].toString().isEmpty()))
										fxrDetails.setSource(row[sourceIndx].toString());
									fxrDetails.setCreatedBy(userId);
									fxrDetails.setCreatedDate(ZonedDateTime.now());
									fxrDetails.setLastUpdatedBy(userId);
									fxrDetails.setLastUpdatedDate(ZonedDateTime.now());
									fxrDetails.setEnabledFlag(true);
									FxRatesDetails createMSValues = fxRatesDetailsRepository.save(fxrDetails);
									fxRatesDetailsIds.add(createMSValues.getId());
									names.add(row[nameIndx].toString());
								}
							}
							errorReport.setStatus("Success");
						}
						catch(DateTimeParseException e)
						{
							errorReport.setStatus("Failed");
							reasons.add("Invalid date format found in row : "+ j);
							break;
						}
						catch(Exception e)
						{
							errorReport.setStatus("Failed");
							reasons.add("Un able to read the row : "+ j);
							break;
						}
					}
				}
				else
				{
					errorReport.setStatus("Failed");
					reasons.add("There is no data present in the file.");
				}
			}
			else
			{
				errorReport.setStatus("Failed");
				reasons.add("You have been uploaded empty file.");
			}
			if("Failed".equalsIgnoreCase(errorReport.getStatus()))
			{
				if(fxRatesIds.size()>0)
	    		{
	    			for(Long id : fxRatesIds)
	    			{
	    				fxRatesRepository.delete(id);
	    			}
	    		}
	    		if(fxRatesDetailsIds.size()>0)
	    		{
	    			for(Long id : fxRatesDetailsIds)
	    			{
	    				fxRatesDetailsRepository.delete(id);
	    			}
	    		}
			}
    	}
    	catch(Exception e)
    	{
    		log.info("Exception: "+e);
    	}
    	errorReport.setReasons(reasons);
    	return errorReport;
    }
   	
    /**
     * Author: Shiva
     * Purpose: Check weather fx rates exist or not
     * **/
    @GetMapping("/checkFxRatesIsExist")
	@Timed
	public HashMap checkApprovalGroupIsExist(HttpServletRequest request,@RequestParam String name,@RequestParam(required=false,value="id") String id)
	{
		HashMap map=new HashMap();
		HashMap map0=userJdbcService.getuserInfoFromToken(request);
	  	Long tenantId=Long.parseLong(map0.get("tenantId").toString());
		map.put("result", "No Duplicates Found");
		if(id != null)
		{
			FxRates fxRatesWithId = fxRatesRepository.findByIdForDisplayAndNameAndTenantId(id, name, tenantId);
			if(fxRatesWithId != null)
			{
				map.put("result", "No Duplicates Found");
			}
			else
			{
				List<FxRates> fxRates = fxRatesRepository.fetchByTenantIdAndName(tenantId, name);
				if(fxRates.size()>0)
				{
					map.put("result", "'"+name+"' fx rates already exists");
				}
			}
		}
		else 
		{
			List<FxRates> fxRates = fxRatesRepository.fetchByTenantIdAndName(tenantId, name);
			if(fxRates.size()>0)
			{
				map.put("result", "'"+name+"' fx rates already exists");
			}
		}
		return map;
	}
}
