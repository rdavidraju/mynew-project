package com.nspl.app.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

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
import com.nspl.app.domain.ApprovalGroups;
import com.nspl.app.domain.FxRates;
import com.nspl.app.domain.FxRatesDetails;
import com.nspl.app.domain.LookUpCode;
import com.nspl.app.domain.LookUpType;
import com.nspl.app.repository.LookUpCodeRepository;
import com.nspl.app.repository.LookUpTypeRepository;
import com.nspl.app.repository.search.LookUpCodeSearchRepository;
import com.nspl.app.security.IDORUtils;
import com.nspl.app.service.UserJdbcService;
import com.nspl.app.web.rest.dto.ErrorReporting;
import com.nspl.app.web.rest.dto.SrcConnectionDetailsDTO;
import com.nspl.app.web.rest.util.HeaderUtil;
import com.nspl.app.web.rest.util.PaginationUtil;
import com.nspl.app.config.ApplicationContextProvider;

/**
 * REST controller for managing LookUpCode.
 */
@RestController
@RequestMapping("/api")
public class LookUpCodeResource {

	private final Logger log = LoggerFactory.getLogger(LookUpCodeResource.class);

	private static final String ENTITY_NAME = "lookUpCode";

	private final LookUpCodeRepository lookUpCodeRepository;

	private final LookUpCodeSearchRepository lookUpCodeSearchRepository;

	@Inject
	LookUpTypeRepository lookUpTypeRepository;

	@Inject
	UserJdbcService userJdbcService;

	public LookUpCodeResource(LookUpCodeRepository lookUpCodeRepository, LookUpCodeSearchRepository lookUpCodeSearchRepository) {
		this.lookUpCodeRepository = lookUpCodeRepository;
		this.lookUpCodeSearchRepository = lookUpCodeSearchRepository;
	}

	/**
	 * POST  /look-up-codes : Create a new lookUpCode.
	 *
	 * @param lookUpCode the lookUpCode to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new lookUpCode, or with status 400 (Bad Request) if the lookUpCode has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/look-up-codes")
	@Timed
	/* public ResponseEntity<LookUpCode> createLookUpCode(@RequestBody LookUpCode lookUpCode) throws URISyntaxException {
        log.debug("REST request to save LookUpCode : {}", lookUpCode);
        if (lookUpCode.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new lookUpCode cannot already have an ID")).body(null);
        }
        LookUpCode result = lookUpCodeRepository.save(lookUpCode);
        lookUpCodeSearchRepository.save(result);
        return ResponseEntity.created(new URI("	" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }*/

	public ResponseEntity<LookUpCode> createLookUpCode(@RequestBody LookUpCode lookUpCode,HttpServletRequest request) throws URISyntaxException {
		log.debug("REST request to save LookUpCode : {}", lookUpCode);
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		Long userId=Long.parseLong(map.get("userId").toString());
		if (lookUpCode.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new lookUpCode cannot already have an ID")).body(null);
		}
		lookUpCode.setIsDefault(false);
		lookUpCode.setActiveStartDate(lookUpCode.getActiveStartDate());
		if(lookUpCode.getActiveEndDate()!=null)
			lookUpCode.setActiveEndDate(lookUpCode.getActiveEndDate());
		
		lookUpCode.setCreatedBy(userId);
		lookUpCode.setLastUpdatedBy(userId);
		lookUpCode.setTenantId(tenantId);
		LookUpCode result = lookUpCodeRepository.save(lookUpCode);
		lookUpCodeSearchRepository.save(result);
		return ResponseEntity.created(new URI("/api/look-up-codes/"+result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}



	/**
	 * PUT  /look-up-codes : Updates an existing lookUpCode.
	 *
	 * @param lookUpCode the lookUpCode to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated lookUpCode,
	 * or with status 400 (Bad Request) if the lookUpCode is not valid,
	 * or with status 500 (Internal Server Error) if the lookUpCode couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/look-up-codes")
	@Timed
	public ResponseEntity<LookUpCode> updateLookUpCode(@RequestBody LookUpCode lookUpCode,HttpServletRequest request) throws URISyntaxException {
		log.debug("REST request to update LookUpCode : {}", lookUpCode);
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		Long userId=Long.parseLong(map.get("userId").toString());
		if (lookUpCode.getId() == null) {
			return createLookUpCode(lookUpCode,request);
		}
		lookUpCode.setActiveStartDate(lookUpCode.getActiveStartDate());
		if(lookUpCode.getActiveEndDate()!=null)
			lookUpCode.setActiveEndDate(lookUpCode.getActiveEndDate());
		lookUpCode.setLastUpdatedBy(userId);
		lookUpCode.setLastUpdatedDate(ZonedDateTime.now());
		LookUpCode result = lookUpCodeRepository.save(lookUpCode);
		lookUpCodeSearchRepository.save(result);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, lookUpCode.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /look-up-codes : get all the lookUpCodes.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of lookUpCodes in body
	 */
	@GetMapping("/look-up-codes")
	@Timed
	public ResponseEntity<List<LookUpCode>> getAllLookUpCodes(@ApiParam Pageable pageable) {
		log.debug("REST request to get a page of LookUpCodes");
		Page<LookUpCode> page = lookUpCodeRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/look-up-codes");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/*  @GetMapping("/look-up-codes")
    @Timed
    public List<LookUpCode> getAllLookUpCodes(@RequestParam Long tenantId) {
        log.debug("REST request to get a page of LookUpCodes");
        List<LookUpCode> lookUpCodeList = lookUpCodeRepository.findByTenantId(tenantId);
		return lookUpCodeList;

    }*/



	/**
	 * GET  /look-up-codes/:id : get the "id" lookUpCode.
	 *
	 * @param id the id of the lookUpCode to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the lookUpCode, or with status 404 (Not Found)
	 */
	@GetMapping("/look-up-codes/{id}")
	@Timed
	public ResponseEntity<LookUpCode> getLookUpCode(@PathVariable Long id) {
		log.debug("REST request to get LookUpCode : {}", id);
		LookUpCode lookUpCode = lookUpCodeRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(lookUpCode));
	}

	/**
	 * DELETE  /look-up-codes/:id : delete the "id" lookUpCode.
	 *
	 * @param id the id of the lookUpCode to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/look-up-codes/{id}")
	@Timed
	public ResponseEntity<Void> deleteLookUpCode(@PathVariable Long id) {
		log.debug("REST request to delete LookUpCode : {}", id);
		lookUpCodeRepository.delete(id);
		lookUpCodeSearchRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH  /_search/look-up-codes?query=:query : search for the lookUpCode corresponding
	 * to the query.
	 *
	 * @param query the query of the lookUpCode search 
	 * @param pageable the pagination information
	 * @return the result of the search
	 */
	@GetMapping("/_search/look-up-codes")
	@Timed
	public ResponseEntity<List<LookUpCode>> searchLookUpCodes(@RequestParam String query, @ApiParam Pageable pageable) {
		log.debug("REST request to search for a page of LookUpCodes for query {}", query);
		Page<LookUpCode> page = lookUpCodeSearchRepository.search(queryStringQuery(query), pageable);
		HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/look-up-codes");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}



	/**
	 * author:ravali
	 * @param lookUpType
	 * @param tenantId
	 * @return look up code
	 */
	@GetMapping("/lookup-codes/{lookuptype}")
	@Timed
	public List<LookUpCode> getByLookUpType(HttpServletRequest request,@PathVariable String lookuptype, @RequestParam(required=false) String module){
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		log.info("lookup-codes for tenant by type : "+lookuptype +" =>"+tenantId);
		List<String> list=new ArrayList<String>();
		List<LookUpCode> reportTypeCodesList=lookUpCodeRepository.findByTenantAndLookUpTypeAndActiveState(tenantId, "REPORT_TYPES");
		for(int i=0;i<reportTypeCodesList.size();i++){
			LookUpCode repType=reportTypeCodesList.get(i);
			list.add(repType.getLookUpCode());
		}
		List<LookUpCode> lookUpCodeList=new ArrayList<LookUpCode>();
		if(module!=null && !(module.isEmpty())){
			lookUpCodeList=lookUpCodeRepository.fetchActiveReportingActiveStaus(tenantId, lookuptype, module);
		}
		else if(list.contains(lookuptype)){
			lookUpCodeList=lookUpCodeRepository.findByTenantAndLookUpTypeOrderBySecAttribute2(tenantId, lookuptype);
		}
		else lookUpCodeList=lookUpCodeRepository.findByTenantAndLookUpTypeAndActiveState(tenantId,lookuptype);
		if(lookuptype.equalsIgnoreCase("all")){
			lookUpCodeList=lookUpCodeRepository.fetchByTenanatIdAndActiveState(tenantId);
		}
		return lookUpCodeList;
	}


	/**
	 * Author Kiran
	 * @param lookUptype
	 * @return
	 */
	@GetMapping("/getLookUpCodesForZeroTenant")
	@Timed
	public List<LookUpCode> getLookUpCodesForZeroTenant(@RequestParam String lookUptype){
		log.info("lookup-codes for tenant: 0");
		List<LookUpCode> lookUpCodeList=lookUpCodeRepository.findByTenantAndLookUpTypeAndActiveStateForZero(lookUptype);
		return lookUpCodeList;
	}

	@GetMapping("/getALlLookUpCodes/{lookuptype}")
	@Timed
	public List<LookUpCode> getALlLookUpCodes(HttpServletRequest request,@PathVariable String lookuptype){
		//	User currentUser = userService.getCurrentUser();
		//	Long tenantId=currentUser.getTenantId();
		//	log.info("/lookup-codes/byLookUpType tenantId: "+tenantId);
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		log.info("getALlLookUpCodes for tenant: "+tenantId);
		List<LookUpCode> lookUpCodeList=lookUpCodeRepository.fetchByTenantIdAndLookUpType(tenantId,lookuptype);
		if(lookuptype.equalsIgnoreCase("all")){
			lookUpCodeList=lookUpCodeRepository.fetchByTenanatId(tenantId);
		}
		return lookUpCodeList;
	}



	/**
	 * author:ravali
	 * @param lookUpType
	 * @param tenantId
	 * @return look up code
	 */
	@GetMapping("/lookupCodesAndMeaning/{lookuptype}")
	@Timed
	public List<HashMap> getByLooKUpCodeAndMeaning(HttpServletRequest request,@PathVariable String lookuptype){
		HashMap map1=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map1.get("tenantId").toString());
		log.info("Rest Request to get lookUpCode and Meaning by lookUpType :"+lookuptype+" and tenantId" +tenantId);
		List<LookUpCode> lookUpCodeList=lookUpCodeRepository.findByTenantAndLookUpTypeAndActiveState(tenantId,lookuptype);
		List<HashMap> finalMap=new ArrayList<HashMap>();
		for(LookUpCode lookUpCode:lookUpCodeList)
		{
			HashMap map=new HashMap();
			if(lookUpCode.getLookUpCode()!=null && !lookUpCode.getLookUpCode().isEmpty())
				map.put("lookUpCode", lookUpCode.getLookUpCode());
			if(lookUpCode.getMeaning()!=null && !lookUpCode.getMeaning().isEmpty())
				map.put("meaning", lookUpCode.getMeaning());
			if(map!=null && !map.isEmpty())
				finalMap.add(map);	
		}
		return finalMap;
	}


	/**
	 * Author : shobha
	 * @param lookuptype
	 * @param tenantId
	 * @return source connections and their display columns from look up code table
	 * 
	 */
	@GetMapping("/connectionsAndDisplayColumns/{lookuptype}")
	@Timed
	public List<SrcConnectionDetailsDTO> fetchConnectionsAndDisplayColumns(HttpServletRequest request,@PathVariable String lookuptype){

		List<SrcConnectionDetailsDTO> srcConDetails = new ArrayList<SrcConnectionDetailsDTO>();
		HashMap map1=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map1.get("tenantId").toString());
		log.info("Rest request to fetch connections and display columns for tenant: "+tenantId);
		List<LookUpCode> connectionsList = lookUpCodeRepository.findByTenantAndLookUpTypeAndActiveState(tenantId,lookuptype);
		log.info("connectionss :"+connectionsList);
		for(LookUpCode con : connectionsList)
		{
			List<LookUpCode> displayCols = lookUpCodeRepository.findByTenantAndLookUpTypeAndActiveState(tenantId,con.getLookUpCode());
			List<HashMap> displayColMap = new ArrayList<HashMap>();
			for(LookUpCode displayCol : displayCols)
			{
				HashMap<String,String> map = new HashMap<String,String>();
				map.put("code", displayCol.getLookUpCode());
				map.put("meaning", displayCol.getMeaning());
				map.put("value","" );
				displayColMap.add(map);
			}
			SrcConnectionDetailsDTO srcconDTO = new SrcConnectionDetailsDTO();
			srcconDTO.setConnectionTypeCode(con.getLookUpCode());
			srcconDTO.setConnectionTypeMeaning(con.getMeaning());
			srcconDTO.setDisplayColumns(displayColMap);
			srcConDetails.add(srcconDTO);
		}
		return srcConDetails;
	}

	
	/**
	 * Author: Shiva
	 * Description: Bulk Upload for LookUp Type, LookUp Codes
	 * @Param: tenantId, userId, multipartfile
	 */
/*	@PostMapping("/lookUpsBulkUpload")
	@Timed
	public ErrorReporting lookUpsBulkUpload(@RequestParam MultipartFile multipart, HttpServletRequest request)
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
    		int lookUpCodeIndx = 0; int lookUpTypeIndx = 1; int meaningIndx = 2; int descriptionIndx = 3; int sDateIndx = 4; int eDateIndx = 5;
			List<String[]> allRows = csvReader.readAll();
			log.info("All Rows Size: "+ allRows.size());
			if(allRows.size()>1)
			{
				log.info("Data Validation Started...");
				List<String> names = new ArrayList<String>();
				List<String> allLookUpTypes = 
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
										fxRates.setStartDate(ZonedDateTime.parse(fxRate.get("startDate").toString()));
										fxRates.setStartDate(localDate.atStartOfDay(ZoneId.systemDefault()));
									}
									if(fxRate.get("endDate") != null)
									{
										LocalDate localDate = LocalDate.parse(fxRate.get("endDate").toString());
										fxRates.setEndDate(localDate.atStartOfDay(ZoneId.systemDefault()));
										fxRates.setEndDate(ZonedDateTime.parse(fxRate.get("endDate").toString()));
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
										LocalDate localDate = LocalDate.parse(fxRate.get("endDate").toString());
										fxRatesNew.setStartDate(localDate.atStartOfDay(ZoneId.systemDefault()));
										fxRatesNew.setStartDate(ZonedDateTime.parse(fxRate.get("startDate").toString()));
									}
										
									if(fxRate.get("endDate") != null)
									{
										LocalDate localDate = LocalDate.parse(fxRate.get("endDate").toString());
										fxRatesNew.setEndDate(localDate.atStartOfDay(ZoneId.systemDefault()));
										fxRatesNew.setEndDate(ZonedDateTime.parse(fxRate.get("endDate").toString()));
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
										fxRatesDetails.setFromDate(ZonedDateTime.parse(data.get("fromDate").toString()));
									}
									if(data.get("toDate") != null)
									{
										LocalDate localDate = LocalDate.parse(data.get("toDate").toString());
										fxRatesDetails.setToDate(localDate.atStartOfDay(ZoneId.systemDefault()));
										fxRatesDetails.setToDate(ZonedDateTime.parse(data.get("toDate").toString()));
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
					catch(Exception e){
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
*/	
	/**
	 * Author: Shiva
	 * Description: Bulk uploading fx rates and fx rates details
	 * @Param: tenantId, userId, multipartfile
	 */
	@PostMapping("/lookUpsBulkUpload")
	@Timed
	public ErrorReporting lookUpsBulkUploadOld(@RequestParam MultipartFile multipart, @RequestParam Long tenantId, @RequestParam Long userId)
	{
		log.info("Rest api for bulk uploading lookup codes and lookup types for the teanant id: "+ tenantId +", File name: "+multipart.getOriginalFilename());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
		ErrorReporting errorReport = new ErrorReporting();
		List<String> reasons = new ArrayList<String>();
		List<Long> lTypeIds = new ArrayList<Long>();
		List<Long> lCodeIds = new ArrayList<Long>();
		try{
			CSVReader csvReader = new CSVReader(new InputStreamReader(multipart.getInputStream()), ',' , '"');
			int lTypeIndx = 0;int lTypeMeanIndx = 0;int lTypeDesIndx = 0;int sDateIndx = 0;	int eDateIndx = 0;int valTypeIndx = 0;int lCodeIndx = 0; int lCodeMeanIndx = 0; 
			int lCodeDesIndx = 0; int sec1Indx = 0; int sec2Indx = 0; int sec3Indx = 0;

			List<String[]> allRows = csvReader.readAll();
			log.info("All Rows Size: "+ allRows.size());
			if(allRows.size()>0)
			{
				log.info("Getting Header Row Indexes...");
				String[] header = allRows.get(0);
				for(int i=0; i<header.length; i++)
				{
					if("look_up_type".equalsIgnoreCase(header[i].toString()))
						lTypeIndx=i;
					else if("look_up_type_meaning".equalsIgnoreCase(header[i].toString()))
						lTypeMeanIndx=i;
					else if("look_up_type_description".equalsIgnoreCase(header[i].toString()))
						lTypeDesIndx=i;
					else if("active_start_date".equalsIgnoreCase(header[i].toString()))
						sDateIndx=i;
					else if("active_end_date".equalsIgnoreCase(header[i].toString()))
						eDateIndx=i;
					else if("vallidation_type".equalsIgnoreCase(header[i].toString()))
						valTypeIndx=i;
					else if("look_up_code".equalsIgnoreCase(header[i].toString()))
						lCodeIndx=i;
					else if("look_up_code_meaning".equalsIgnoreCase(header[i].toString()))
						lCodeMeanIndx=i;
					else if("look_up_code_description".equalsIgnoreCase(header[i].toString()))
						lCodeDesIndx=i;
					else if("secure_attribute_1".equalsIgnoreCase(header[i].toString()))
						sec1Indx=i;
					else if("secure_attribute_2".equalsIgnoreCase(header[i].toString()))
						sec2Indx=i;
					else if("secure_attribute_3".equalsIgnoreCase(header[i].toString()))
						sec3Indx=i;
				}
				log.info("Filed Indexes: look_up_type["+lTypeIndx+"], look_up_type_meaning["+lTypeMeanIndx+"], look_up_type_description["+lTypeDesIndx+"],"
						+ "active_start_date["+sDateIndx+"], active_end_date["+eDateIndx+"], vallidation_type["+valTypeIndx+"], look_up_code["+lCodeIndx+"], look_up_code_meaning["+lCodeMeanIndx+"]"
						+", look_up_code_description["+lCodeDesIndx+"], secure_attribute_1["+sec1Indx+"], secure_attribute_2["+sec2Indx+"], secure_attribute_3["+sec3Indx+"]");

				if(allRows.size()>1)
				{
					log.info("Reading data...");
					List<String> lookUpTypes = new ArrayList<String>();
					for(int j=1; j<allRows.size(); j++)
					{
						try{
							String[] row = allRows.get(j);
							if(!"NULL".equalsIgnoreCase(row[lTypeIndx].toString()) || !(row[lTypeIndx].toString().isEmpty()))
							{
								if(lookUpTypes.contains(row[lTypeIndx].toString().trim()))
								{
									// Saving look up codes
									LookUpCode lookUpCode = new LookUpCode();
									lookUpCode.setTenantId(tenantId);
									if(!"NULL".equalsIgnoreCase(row[lCodeIndx].toString()) || !(row[lCodeIndx].toString().isEmpty()))
										lookUpCode.setLookUpCode(row[lCodeIndx].toString());
									if(!"NULL".equalsIgnoreCase(row[lTypeIndx].toString()) || !(row[lTypeIndx].toString().isEmpty()))
										lookUpCode.setLookUpType(row[lTypeIndx].toString());
									if(!"NULL".equalsIgnoreCase(row[lCodeMeanIndx].toString()) || !(row[lCodeMeanIndx].toString().isEmpty()))
										lookUpCode.setMeaning(row[lCodeMeanIndx].toString());
									if(!"NULL".equalsIgnoreCase(row[lCodeDesIndx].toString()) || !(row[lCodeDesIndx].toString().isEmpty()))
										lookUpCode.setDescription(row[lCodeDesIndx].toString());
									lookUpCode.setEnableFlag(true);
									if(!"NULL".equalsIgnoreCase(row[sDateIndx].toString()) || !(row[sDateIndx].toString().isEmpty()))
										lookUpCode.setActiveStartDate(ZonedDateTime.parse(row[sDateIndx].toString(), formatter));
									if(!"NULL".equalsIgnoreCase(row[eDateIndx].toString()) || !(row[eDateIndx].toString().isEmpty()))
									{
										lookUpCode.setActiveEndDate(ZonedDateTime.parse(row[eDateIndx].toString(), formatter));
									}
									else if(row[eDateIndx].toString().isEmpty() || "NULL".equalsIgnoreCase(row[eDateIndx].toString()))
									{
										lookUpCode.setActiveEndDate(null);
									}
									if(!"NULL".equalsIgnoreCase(row[sec1Indx].toString()) || !(row[sec1Indx].toString().isEmpty()))
										lookUpCode.setSecureAttribute1(row[sec1Indx].toString());
									if(!"NULL".equalsIgnoreCase(row[sec2Indx].toString()) || !(row[sec2Indx].toString().isEmpty()))
										lookUpCode.setSecureAttribute2(row[sec2Indx].toString());
									if(!"NULL".equalsIgnoreCase(row[sec3Indx].toString()) || !(row[sec3Indx].toString().isEmpty()))
										lookUpCode.setSecureAttribute3(row[sec3Indx].toString());
									lookUpCode.setCreatedBy(userId);
									lookUpCode.setCreationDate(ZonedDateTime.now());
									lookUpCode.setLastUpdatedBy(userId);
									lookUpCode.setLastUpdatedDate(ZonedDateTime.now());
									LookUpCode luc = lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId(row[lTypeIndx].toString(),row[lCodeIndx].toString(),tenantId);
									if(luc != null)
									{
										lookUpCode.setId(luc.getId());
										LookUpCode createLookUpCode = lookUpCodeRepository.save(lookUpCode);
										lCodeIds.add(createLookUpCode.getId());
									}
									else
									{
										LookUpCode createLookUpCode = lookUpCodeRepository.save(lookUpCode);
										lCodeIds.add(createLookUpCode.getId());
									}
								}
								else
								{
									log.info("Filed Indexes: look_up_type["+lTypeIndx+"], look_up_type_meaning["+lTypeMeanIndx+"], look_up_type_description["+lTypeDesIndx+"],"
											+ "active_start_date["+sDateIndx+"], active_end_date["+eDateIndx+"], vallidation_type["+valTypeIndx+"], look_up_code["+lCodeIndx+"], look_up_code_meaning["+lCodeMeanIndx+"]"
											+", look_up_code_description["+lCodeDesIndx+"], secure_attribute_1["+sec1Indx+"], secure_attribute_2["+sec2Indx+"], secure_attribute_3["+sec3Indx+"]");
									// Saving Look up types
									LookUpType lookupType = new LookUpType();
									lookupType.setTenantId(tenantId);
									if(!"NULL".equalsIgnoreCase(row[lTypeIndx].toString()) || !(row[lTypeIndx].toString().isEmpty()))
										lookupType.setLookUpType(row[lTypeIndx].toString());
									if(!"NULL".equalsIgnoreCase(row[lTypeMeanIndx].toString()) || !(row[lTypeMeanIndx].toString().isEmpty()))	
										lookupType.setMeaning(row[lTypeMeanIndx].toString());
									if(!"NULL".equalsIgnoreCase(row[lTypeDesIndx].toString()) || !(row[lTypeDesIndx].toString().isEmpty()))
										lookupType.setDescription(row[lTypeDesIndx].toString());
									lookupType.setEnableFlag(true);
									if(!"NULL".equalsIgnoreCase(row[sDateIndx].toString()) || !(row[sDateIndx].toString().isEmpty()))
										lookupType.setActiveStartDate(ZonedDateTime.parse(row[sDateIndx].toString(), formatter));
									if(!"NULL".equalsIgnoreCase(row[eDateIndx].toString()) || !(row[eDateIndx].toString().isEmpty()))
									{
										lookupType.setActiveEndDate(ZonedDateTime.parse(row[eDateIndx].toString(), formatter));
									}
									else if(row[eDateIndx].toString().isEmpty() || "NULL".equalsIgnoreCase(row[eDateIndx].toString()))
									{
										lookupType.setActiveEndDate(null);
									}
									if(!"NULL".equalsIgnoreCase(row[valTypeIndx].toString()) || !(row[valTypeIndx].toString().isEmpty()))
										lookupType.setValidationType(row[valTypeIndx].toString());
									lookupType.setCreatedBy(userId);
									lookupType.setCreationDate(ZonedDateTime.now());
									lookupType.setLastUpdatedBy(userId);
									lookupType.setLastUpdatedDate(ZonedDateTime.now());
									LookUpType lut = lookUpTypeRepository.findByTenantIdAndLookUpType(tenantId, row[lTypeIndx].toString());
									if(lut != null)
									{
										lookupType.setId(lut.getId());
										LookUpType createLookUpType = lookUpTypeRepository.save(lookupType);
										lTypeIds.add(createLookUpType.getId());
									}
									else
									{
										LookUpType createLookUpType = lookUpTypeRepository.save(lookupType);
										lTypeIds.add(createLookUpType.getId());
									}
									// Saving look up codes
									LookUpCode lookUpCode = new LookUpCode();
									lookUpCode.setTenantId(tenantId);
									if(!"NULL".equalsIgnoreCase(row[lCodeIndx].toString()) || !(row[lCodeIndx].toString().isEmpty()))
										lookUpCode.setLookUpCode(row[lCodeIndx].toString());
									if(!"NULL".equalsIgnoreCase(row[lTypeIndx].toString()) || !(row[lTypeIndx].toString().isEmpty()))
										lookUpCode.setLookUpType(row[lTypeIndx].toString());
									if(!"NULL".equalsIgnoreCase(row[lCodeMeanIndx].toString()) || !(row[lCodeMeanIndx].toString().isEmpty()))
										lookUpCode.setMeaning(row[lCodeMeanIndx].toString());
									if(!"NULL".equalsIgnoreCase(row[lCodeDesIndx].toString()) || !(row[lCodeDesIndx].toString().isEmpty()))
										lookUpCode.setDescription(row[lCodeDesIndx].toString());
									lookUpCode.setEnableFlag(true);
									if(!"NULL".equalsIgnoreCase(row[sDateIndx].toString()) || !(row[sDateIndx].toString().isEmpty()))
										lookUpCode.setActiveStartDate(ZonedDateTime.parse(row[sDateIndx].toString(), formatter));
									if(!"NULL".equalsIgnoreCase(row[eDateIndx].toString()) || !(row[eDateIndx].toString().isEmpty()))
									{
										lookUpCode.setActiveEndDate(ZonedDateTime.parse(row[eDateIndx].toString(), formatter));	
									}
									else if(row[eDateIndx].toString().isEmpty() || "NULL".equalsIgnoreCase(row[eDateIndx].toString()))
									{
										lookupType.setActiveEndDate(null);
									}
									if(!"NULL".equalsIgnoreCase(row[sec1Indx].toString()) || !(row[sec1Indx].toString().isEmpty()))
										lookUpCode.setSecureAttribute1(row[sec1Indx].toString());
									if(!"NULL".equalsIgnoreCase(row[sec2Indx].toString()) || !(row[sec2Indx].toString().isEmpty()))
										lookUpCode.setSecureAttribute2(row[sec2Indx].toString());
									if(!"NULL".equalsIgnoreCase(row[sec3Indx].toString()) || !(row[sec3Indx].toString().isEmpty()))
										lookUpCode.setSecureAttribute3(row[sec3Indx].toString());
									lookUpCode.setCreatedBy(userId);
									lookUpCode.setCreationDate(ZonedDateTime.now());
									lookUpCode.setLastUpdatedBy(userId);
									lookUpCode.setLastUpdatedDate(ZonedDateTime.now());
									LookUpCode luc = lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId(row[lTypeIndx].toString(),row[lCodeIndx].toString(),tenantId);
									if(luc != null)
									{
										lookUpCode.setId(luc.getId());
										LookUpCode createLookUpCode = lookUpCodeRepository.save(lookUpCode);
										lCodeIds.add(createLookUpCode.getId());
									}
									else
									{
										LookUpCode createLookUpCode = lookUpCodeRepository.save(lookUpCode);
										lCodeIds.add(createLookUpCode.getId());
									}
									lookUpTypes.add(row[lTypeIndx].toString().trim());
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
				if(lTypeIds.size()>0)
				{
					for(Long id : lTypeIds)
					{
						lookUpTypeRepository.delete(id);
					}
				}
				if(lCodeIds.size()>0)
				{
					for(Long id : lCodeIds)
					{
						lookUpCodeRepository.delete(id);
					}
				}
			}
		}catch(Exception e)
		{
			log.info("Exception: "+e);
		}
		errorReport.setReasons(reasons);
		return errorReport;
	}


	/* Author: shiva, Kiran
	 * Description: Uploading look up codes from csv file
	 * params: csv file, tenantId
	 */
	//	@PostMapping("/uploadLookUpCodes")
	//	@Timed
	public void uploadLookUpCodes(@RequestParam Long tenantId, @RequestParam Long userId)
	{
		log.info("Setting LookUp Codes for Tenant Id:- "+tenantId+" and UserId:-"+userId);

		log.info("lookUpTypeRepository.findByTenantId(tenantId).size():- "+lookUpTypeRepository.findByTenantId(tenantId).size());
		InputStreamReader inputStream = new  InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("jsonFile/LookUpCodes.csv"));
		if(inputStream!=null)
		{
			CSVReader csvReader = new CSVReader(inputStream, ',' , '"');

			//Getting indexes of every field from csv file
			int lookUpCodeIndx = 0;
			int lookUpTypIndx = 0;
			int meaningIndx = 0;
			int descriptionIndx = 0;
			int enableFlagIndx = 0;
			int activeStrtDtIndx = 0;
			int endDtIndx = 0;
			int secAttr1Indx = 0;
			int secAttr2Indx = 0;
			int secAttr3Indx = 0;
			int moduleIndx = 0;
			int isDefaultIndx = 0;
			List<String[]> allRows;
			List<LookUpCode> lookUpsList=null;
			try {
				allRows = csvReader.readAll();
				csvReader.close();
				log.info("All Rows Size in uploadLookUpCodes: "+ allRows.size());
				if(allRows.size()>0)
				{
					//Header Row
					String[] header = allRows.get(0);
					log.info("Header Length: "+ header.length);
					for(int i=0; i<header.length; i++)
					{
						if("look_up_code".equalsIgnoreCase(header[i].toString()))
							lookUpCodeIndx = i;
						else if("look_up_type".equalsIgnoreCase(header[i].toString()))
							lookUpTypIndx = i;
						else if("meaning".equalsIgnoreCase(header[i].toString()))
							meaningIndx = i;
						else if("description".equalsIgnoreCase(header[i].toString()))
							descriptionIndx = i;
						else if("enable_flag".equalsIgnoreCase(header[i].toString()))
							enableFlagIndx = i;
						else if("active_start_date".equalsIgnoreCase(header[i].toString()))
							activeStrtDtIndx = i;
						else if("active_end_date".equalsIgnoreCase(header[i].toString()))
							endDtIndx = i;
						else if("secure_attribute_1".equalsIgnoreCase(header[i].toString()))
							secAttr1Indx = i;
						else if("secure_attribute_2".equalsIgnoreCase(header[i].toString()))
							secAttr2Indx = i;
						else if("secure_attribute_3".equalsIgnoreCase(header[i].toString()))
							secAttr3Indx = i;
						else if("module".equalsIgnoreCase(header[i].toString()))
							moduleIndx = i;
						else if("is_default".equalsIgnoreCase(header[i].toString()))
							isDefaultIndx = i;
							
					}
					log.info("Indexs: look_up_code["+ lookUpCodeIndx+"], look_up_type["+lookUpTypIndx+"], "+"meaning["+meaningIndx+"], "+
							"description["+descriptionIndx+"], "+"enable_flag["+enableFlagIndx+"], "+"active_start_date["+activeStrtDtIndx+"], "+"active_end_date["+endDtIndx+"], "+
							"secure_attribute_1["+secAttr1Indx+"], secure_attribute_2["+secAttr2Indx+"], secure_attribute_3["+secAttr3Indx+"], module["+moduleIndx+"], is_default["+isDefaultIndx+"]");
					if(allRows.size()>0)
					{
						log.info("Setting Lookup codes for tenant: "+tenantId);
						List<LookUpCode> lookUps = new ArrayList<LookUpCode>();
						for(int j=1; j<allRows.size(); j++)
						{
							LookUpCode luc = new LookUpCode();
							String[] row = allRows.get(j);
							int rowLength=row.length;

							if(lookUpCodeIndx<rowLength && lookUpTypIndx<rowLength && meaningIndx<rowLength && descriptionIndx<rowLength && enableFlagIndx<rowLength && 
									activeStrtDtIndx<rowLength && endDtIndx<rowLength && secAttr1Indx<rowLength && secAttr2Indx<rowLength && secAttr3Indx<rowLength && moduleIndx<rowLength )
							{
								luc.setTenantId(tenantId);
								luc.setLookUpCode(row[lookUpCodeIndx].toString());
								luc.setLookUpType(row[lookUpTypIndx].toString());
								//luc.setMeaning(row[meaningIndx].toString());
								//luc.setDescription(row[descriptionIndx].toString());

								if("NULL".equalsIgnoreCase(row[meaningIndx].toString()) || (row[meaningIndx].toString().isEmpty()))
									luc.setMeaning(null);
								else
									luc.setMeaning(row[meaningIndx].toString());
								if("NULL".equalsIgnoreCase(row[descriptionIndx].toString()) || (row[descriptionIndx].toString().isEmpty()))
									luc.setDescription(null);
								else
									luc.setDescription(row[descriptionIndx].toString());


								luc.setEnableFlag(Boolean.valueOf(row[enableFlagIndx].toString()));
								luc.setActiveStartDate(ZonedDateTime.parse(row[activeStrtDtIndx].toString()));
								//luc.setActiveEndDate(LocalDate.parse(row[endDtIndx].toString()));

								if("NULL".equalsIgnoreCase(row[endDtIndx].toString()) || (row[endDtIndx].toString().isEmpty()))
									luc.setActiveEndDate(null);
								else
									luc.setActiveEndDate(ZonedDateTime.parse(row[endDtIndx].toString()));
								if("NULL".equalsIgnoreCase(row[secAttr1Indx].toString()) || (row[secAttr1Indx].toString().isEmpty()))
									luc.setSecureAttribute1(null);
								else
									luc.setSecureAttribute1(row[secAttr1Indx].toString());
								if("NULL".equalsIgnoreCase(row[secAttr2Indx].toString()) || (row[secAttr2Indx].toString().isEmpty()))
									luc.setSecureAttribute2(null);
								else
									luc.setSecureAttribute2(row[secAttr2Indx].toString());
								if("NULL".equalsIgnoreCase(row[secAttr3Indx].toString()) || (row[secAttr3Indx].toString().isEmpty()))
									luc.setSecureAttribute3(null);
								else
									luc.setSecureAttribute3(row[secAttr3Indx].toString());

								if("NULL".equalsIgnoreCase(row[moduleIndx].toString()) || (row[moduleIndx].toString().isEmpty()))
									luc.setModule(null);
								else
									luc.setModule(row[moduleIndx].toString());
								
								if("NULL".equalsIgnoreCase(row[isDefaultIndx].toString()) || (row[isDefaultIndx].toString().isEmpty()))
									luc.setIsDefault(null);
								else
									luc.setIsDefault(Boolean.valueOf(row[isDefaultIndx].toString()));

								luc.setCreatedBy(userId);
								luc.setCreationDate(ZonedDateTime.now());
								luc.setLastUpdatedBy(userId);
								luc.setLastUpdatedDate(ZonedDateTime.now());

								lookUps.add(luc);
							}
						}
						lookUpsList = lookUpCodeRepository.save(lookUps);
						log.info("lookUpsList Size:- "+lookUpsList.size());

						if(inputStream!=null)
						{
							try {
								inputStream.close();
							} catch (IOException e) {
								log.error("Close Input Stream Exception:",e);
								e.printStackTrace();
							}
						}
					}
				}


			} catch (IOException e) {
				e.printStackTrace();
				log.error("Printing IOException while adding look_up_codes: ",e);
			}
		}
	}


	/**
	 * Author Kiran
	 * @param tenantId
	 * @param userId
	 */
	@PostMapping("/uploadLookUpTypesAndCodes")
	@Timed
	public void uploadLookUpTypes(@RequestParam Long tenantId, @RequestParam Long userId)
	{

		log.info("Setting LookUp Types for Tenant Id:- "+tenantId+" and UserId:-"+userId);
		InputStreamReader inputStream = new  InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("jsonFile/LookUpTypes.csv"));
		if(inputStream!=null)
		{
			CSVReader csvReader = new CSVReader(inputStream, ',' , '"');

			//Getting indexes of every field from csv file
			int lookUpTypIndx = 0;
			int meaningIndx = 0;
			int descriptionIndx = 0;
			int activeStrtDtIndx = 0;
			int activeEndDtIndx = 0;
			int validationTypeIndx = 0;

			List<LookUpType> lookupTypes=null;

			List<String[]> allRows;
			try {
				allRows = csvReader.readAll();
				csvReader.close();
				log.info("All Rows Size in uploadLookUpTypes: "+ allRows.size());
				if(allRows.size()>0)
				{
					//Header Row
					String[] header = allRows.get(0);
					log.info("Header Length: "+ header.length);
					for(int i=0; i<header.length; i++)
					{
						if("look_up_type".equalsIgnoreCase(header[i].toString()))
							lookUpTypIndx = i;
						else if("meaning".equalsIgnoreCase(header[i].toString()))
							meaningIndx = i;
						else if("description".equalsIgnoreCase(header[i].toString()))
							descriptionIndx = i;
						else if("active_start_date".equalsIgnoreCase(header[i].toString()))
							activeStrtDtIndx = i;
						else if("active_end_date".equalsIgnoreCase(header[i].toString()))
							activeEndDtIndx = i;
						else if("validation_type".equalsIgnoreCase(header[i].toString()))
							validationTypeIndx = i;

					}
					log.info("Indexs: look_up_type["+lookUpTypIndx+"], "+"meaning["+meaningIndx+"], description["+descriptionIndx+"], active_start_date["+activeStrtDtIndx+"], "+"active_end_date["+activeEndDtIndx+"], "+
							"validation_type["+validationTypeIndx+"]");//, secure_attribute_2["+secAttr2Indx+"], secure_attribute_3["+secAttr3Indx+"]");
					if(allRows.size()>1)
					{
						List<LookUpType> lookUpTypesList = new ArrayList<LookUpType>();
						for(int j=1; j<allRows.size(); j++)
						{
							LookUpType lut = new LookUpType();
							String[] row = allRows.get(j);

							int rowLength=row.length;
							lut.setTenantId(tenantId);

							if(lookUpTypIndx<rowLength && meaningIndx<rowLength && descriptionIndx<rowLength && activeStrtDtIndx<rowLength && validationTypeIndx<rowLength && activeEndDtIndx<rowLength)
							{
								lut.setLookUpType(row[lookUpTypIndx].toString());

								if("NULL".equalsIgnoreCase(row[meaningIndx].toString()) || (row[meaningIndx].toString().isEmpty()))
									lut.setMeaning(null);
								else
									lut.setMeaning(row[meaningIndx].toString());
								if("NULL".equalsIgnoreCase(row[descriptionIndx].toString()) || (row[descriptionIndx].toString().isEmpty()))
									lut.setDescription(null);
								else
									lut.setDescription(row[descriptionIndx].toString());

								lut.setEnableFlag(true);
								lut.setActiveStartDate(ZonedDateTime.parse(row[activeStrtDtIndx].toString()));

								if("NULL".equalsIgnoreCase(row[activeEndDtIndx].toString()) || (row[activeEndDtIndx].toString().isEmpty()))
								{lut.setActiveEndDate(null);}
								else
									lut.setActiveEndDate(ZonedDateTime.parse(row[activeEndDtIndx].toString()));

								if("NULL".equalsIgnoreCase(row[validationTypeIndx].toString()) || (row[validationTypeIndx].toString().isEmpty()))
									lut.setValidationType(null);
								else
									lut.setValidationType(row[validationTypeIndx].toString());

								lut.setCreatedBy(userId);
								lut.setCreationDate(ZonedDateTime.now());
								lut.setLastUpdatedBy(userId);
								lut.setLastUpdatedDate(ZonedDateTime.now());

								lookUpTypesList.add(lut);
							}
						}
						log.info("lookUpTypesList size "+lookUpTypesList.size());



						lookupTypes=lookUpTypeRepository.save(lookUpTypesList);
						log.info("lookupTypes Size:- "+lookupTypes.size());

						/*for(int k=0;k<lookUpTypesList.size();k++)
						{
						System.out.println("--> lookUpTypesList: "+lookUpTypesList.get(k).toString());	
						}
						 */
						if(inputStream!=null)
						{
							try {
								inputStream.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						if(lookupTypes!=null && lookupTypes.size()>0)
						{
							uploadLookUpCodes(tenantId,userId);
						}

					}
				}
			} catch (IOException e) 
			{
				e.printStackTrace();
				log.error("Printing IOException while adding look_up_types: ",e);
			}
		}

	}


	/**
	 * Author: Shiva
	 * Purpose: Check weather approval group exist or not
	 * **/
	@GetMapping("/checkLookUpCodeIsExist")
	@Timed
	public HashMap checkLookUpCodeIsExist(@RequestParam String type,@RequestParam String code, HttpServletRequest request,@RequestParam(required=false,value="id") Long id)
	{
		HashMap map0=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map0.get("tenantId").toString());
		HashMap map=new HashMap();
		map.put("result", "No Duplicates Found");
		if(id != null)
		{
			LookUpCode groupWithId = lookUpCodeRepository.findByIdAndLookUpTypeAndLookUpCodeAndTenantId(id, type, code, tenantId);
			if(groupWithId != null)
			{
				map.put("result", "No Duplicates Found");
			}
			else
			{
				LookUpCode appGroups = lookUpCodeRepository.findByTenantIdAndLookUpTypeAndLookUpCode(tenantId, type, code);
				if(appGroups != null)
				{
					map.put("result", "'"+code+"' lookup code already exists");
				}
			}
		}
		else 
		{
			LookUpCode appGroups = lookUpCodeRepository.findByTenantIdAndLookUpTypeAndLookUpCode(tenantId, type, code);
			if(appGroups != null)
			{
				map.put("result", "'"+code+"' lookup code already exists");
			}
		}
		return map;
	}
	/**
	 * Author : Shobha
	 */
	@GetMapping("/lookUpTypeToLookUpCodeMap")
	@Timed
	public HashMap<String,List<LookUpCode>> fetchLookUpTypeToLookUpCodeMap(HttpServletRequest request){
		log.info("Rest request to fetch lookups for date,decimal,varchar types to use in rules");
		HashMap map1=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map1.get("tenantId").toString());
		HashMap<String,List<LookUpCode>> map = new HashMap<String,List<LookUpCode>>();
		List<LookUpCode> varcharCodes = new ArrayList<LookUpCode>();
		varcharCodes = lookUpCodeRepository.findByTenantIdAndLookUpType(tenantId, "VARCHAR");
		if(varcharCodes.size()>0)
		{
			map.put("VARCHAR",varcharCodes);
		}
		List<LookUpCode> dateCodes = new ArrayList<LookUpCode>();
		dateCodes = lookUpCodeRepository.findByTenantIdAndLookUpType(tenantId, "DATE");
		if(dateCodes.size()>0)
		{
			map.put("DATE",dateCodes);
		}
		List<LookUpCode> operatorCodes = new ArrayList<LookUpCode>();
		operatorCodes = lookUpCodeRepository.findByTenantIdAndLookUpType(tenantId, "OPERATOR");
		if(operatorCodes.size()>0)
		{
			map.put("OPERATOR",operatorCodes);
		}
		List<LookUpCode> ruleGroupTypeCodes = new ArrayList<LookUpCode>();
		ruleGroupTypeCodes = lookUpCodeRepository.findByTenantIdAndLookUpType(tenantId, "RULE_GROUP_TYPE");
		if(ruleGroupTypeCodes.size()>0)
		{
			map.put("RULE_GROUP_TYPE",ruleGroupTypeCodes);
		}
		List<LookUpCode> toleranceFunctions = new ArrayList<LookUpCode>();
		toleranceFunctions = lookUpCodeRepository.findByTenantIdAndLookUpType(tenantId, "TOLERANCE_TYPE");
		if(toleranceFunctions.size()>0)
		{
			map.put("TOLERANCE_TYPE",toleranceFunctions);
		}
		List<LookUpCode> reconFunctions = new ArrayList<LookUpCode>();
		reconFunctions = lookUpCodeRepository.findByTenantIdAndLookUpType(tenantId, "RECONCILIATION_FUNCTIONS");
		if(reconFunctions.size()>0)
		{
			map.put("RECONCILIATION_FUNCTIONS",reconFunctions);
		}
		List<LookUpCode> decimalCodes = new ArrayList<LookUpCode>();
		decimalCodes = lookUpCodeRepository.findByTenantIdAndLookUpType(tenantId, "DECIMAL");
		if(decimalCodes.size()>0)
		{
			map.put("DECIMAL",decimalCodes);
		}
		return map;
		
	}
	@GetMapping("/accLookUpTypeToLookUpCodeMap")
	@Timed
	public HashMap<String,List<LookUpCode>> fetchAccLookUpTypeToLookUpCodeMap(HttpServletRequest request){
		log.info("Rest request to fetch lookups for accounting def");
		HashMap map1=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map1.get("tenantId").toString());
		HashMap<String,List<LookUpCode>> map = new HashMap<String,List<LookUpCode>>();
		List<LookUpCode> accStatusCodes = new ArrayList<LookUpCode>();
		accStatusCodes = lookUpCodeRepository.findByTenantIdAndLookUpType(tenantId, "ACCOUNTING_STATUS");
		if(accStatusCodes.size()>0)
		{
			map.put("ACCOUNTING_STATUS",accStatusCodes);
		}
		List<LookUpCode> accCriteriaTypeCodes = new ArrayList<LookUpCode>();
		accCriteriaTypeCodes = lookUpCodeRepository.findByTenantIdAndLookUpType(tenantId, "ACCOUNTING_CRITERIA_TYPES");
		if(accCriteriaTypeCodes.size()>0)
		{
			map.put("ACCOUNTING_CRITERIA_TYPES",accCriteriaTypeCodes);
		}
		List<LookUpCode> accHeaderLineTypesCodes = new ArrayList<LookUpCode>();
		accHeaderLineTypesCodes = lookUpCodeRepository.findByTenantIdAndLookUpType(tenantId, "ACC_HEADER_LINE_TYPES");
		if(accHeaderLineTypesCodes.size()>0)
		{
			map.put("ACC_HEADER_LINE_TYPES",accHeaderLineTypesCodes);
		}
		List<LookUpCode> ruleGroupTypeCodes = new ArrayList<LookUpCode>();
		ruleGroupTypeCodes = lookUpCodeRepository.findByTenantIdAndLookUpType(tenantId, "RULE_GROUP");
		if(ruleGroupTypeCodes.size()>0)
		{
			map.put("RULE_GROUP",ruleGroupTypeCodes);
		}
		List<LookUpCode> categoryCodes = new ArrayList<LookUpCode>();
		categoryCodes = lookUpCodeRepository.findByTenantIdAndLookUpType(tenantId, "CATEGORY");
		if(categoryCodes.size()>0)
		{
			map.put("SOURCE",categoryCodes);
		}
		List<LookUpCode> sourceCodes = new ArrayList<LookUpCode>();
		sourceCodes = lookUpCodeRepository.findByTenantIdAndLookUpType(tenantId, "SOURCE");
		if(sourceCodes.size()>0)
		{
			map.put("SOURCE",sourceCodes);
		}
		List<LookUpCode> logicalOperatorCodes = new ArrayList<LookUpCode>();
		logicalOperatorCodes = lookUpCodeRepository.findByTenantIdAndLookUpType(tenantId, "LOGICAL_OPERATOR");
		if(logicalOperatorCodes.size()>0)
		{
			map.put("LOGICAL_OPERATOR",logicalOperatorCodes);
		}
		
		List<LookUpCode> accLineTypesCodes = new ArrayList<LookUpCode>();
		accLineTypesCodes = lookUpCodeRepository.findByTenantIdAndLookUpType(tenantId, "ACCOUNTING_LINE_TYPES");
		if(accLineTypesCodes.size()>0)
		{
			map.put("ACCOUNTING_LINE_TYPES",accLineTypesCodes);
		}
		return map;
		
	}
	@PostMapping("/postRecords")
	@Timed
	public HashMap postRecords(HttpServletRequest request,@RequestBody List<String> queries) throws SQLException{
		log.info("post "+queries.size()+" records......");
		HashMap resp = new HashMap<String,String>();
		if(queries.size()>0)
		{
			DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
			Connection conn = ds.getConnection();
			Statement stmt = null;
			int count =0; 
			int failedCount =0; 
			for(int i=0;i<queries.size();i++)
			{
				stmt = conn.createStatement();
				log.info("queries.get(i)"+queries.get(i));
				try{
					stmt.executeUpdate(queries.get(i));
					count = count+1;
				}
				catch(Exception e)
				{
					e.printStackTrace();
					failedCount=failedCount+1;
				}
				
			//	stmt.addBatch(queries.get(i));
			}
			
			resp.put("successCount",count);
			resp.put("failedCount",failedCount);
			//int [] count =stmt.executeBatch();
		//	for(int c : count)
			//{
			//	log.info("count"+c);	
			//}
			
			if(stmt !=null)
				stmt.close();
			if(conn != null)
				conn.close();
			
		}
		return resp;
	}
	@GetMapping("/domainNameToTableNameMap")
	@Timed
	public HashMap<String,String> domainNameToTableNameMap(HttpServletRequest request,@RequestParam String className) throws ClassNotFoundException{


		Class<?> entityClass = Class.forName(className);
		 HashMap<String,String> domainColNameToTableColNameMap = new HashMap<String,String>();

		/*************************************** Get Fields from domain and column names of table *********************************************************/
		Table table = entityClass.getAnnotation(Table.class);
		String tableName = table.name();
		Field[] fields = entityClass.getDeclaredFields();
		for(int f=0;f<fields.length;f++)
		{
			Class type = fields[f].getType();
			String name = fields[f].getName();
			Column column = fields[f].getAnnotation(Column.class);
			if (column != null)
			{
				domainColNameToTableColNameMap.put(name, column.name());
			}
		}
		return domainColNameToTableColNameMap;

		/**********************************************************************************************************************/
	}
}