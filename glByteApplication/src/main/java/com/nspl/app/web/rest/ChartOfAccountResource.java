package com.nspl.app.web.rest;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

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

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.domain.ChartOfAccount;
import com.nspl.app.domain.LookUpCode;
import com.nspl.app.domain.MappingSet;
import com.nspl.app.domain.MappingSetValues;
import com.nspl.app.domain.Segments;
import com.nspl.app.repository.ChartOfAccountRepository;
import com.nspl.app.repository.LookUpCodeRepository;
import com.nspl.app.repository.MappingSetRepository;
import com.nspl.app.repository.MappingSetValuesRepository;
import com.nspl.app.repository.SegmentsRepository;
import com.nspl.app.security.IDORUtils;
import com.nspl.app.service.ActiveStatusService;
import com.nspl.app.service.UserJdbcService;
import com.nspl.app.web.rest.dto.ChartOfAccountsDTO;
import com.nspl.app.web.rest.util.HeaderUtil;
import com.nspl.app.web.rest.util.PaginationUtil;

/**
 * REST controller for managing ChartOfAccount.
 */
@RestController
@RequestMapping("/api")
public class ChartOfAccountResource {

    private final Logger log = LoggerFactory.getLogger(ChartOfAccountResource.class);

    private static final String ENTITY_NAME = "chartOfAccount";

    private final ChartOfAccountRepository chartOfAccountRepository;
    
    @Inject
    SegmentsRepository segmentsRepository;
    
    @Inject
    MappingSetRepository mappingSetRepository;
    
    @Inject
	UserJdbcService userJdbcService;
    
    @Inject
    LookUpCodeRepository lookUpCodeRepository;
    
    @Inject
    ActiveStatusService activeStatusService;

    @Inject
    MappingSetValuesRepository mappingSetValuesRepository;
    public ChartOfAccountResource(ChartOfAccountRepository chartOfAccountRepository) {
        this.chartOfAccountRepository = chartOfAccountRepository;
    }

    /**
     * POST  /chart-of-accounts : Create a new chartOfAccount.
     *
     * @param chartOfAccount the chartOfAccount to create
     * @return the ResponseEntity with status 201 (Created) and with body the new chartOfAccount, or with status 400 (Bad Request) if the chartOfAccount has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/chart-of-accounts")
    @Timed
    public ResponseEntity<ChartOfAccountsDTO> createChartOfAccount(@RequestBody ChartOfAccount chartOfAccount) throws URISyntaxException {
        log.debug("REST request to save ChartOfAccount : {}", chartOfAccount);
        if (chartOfAccount.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new chartOfAccount cannot already have an ID")).body(null);
        }
        ChartOfAccount result = chartOfAccountRepository.save(chartOfAccount);
   		String idForDisplay = IDORUtils.computeFrontEndIdentifier(result.getId().toString());
		result.setIdForDisplay(idForDisplay);
		result = chartOfAccountRepository.save(result);
		ChartOfAccountsDTO dto = new ChartOfAccountsDTO();
		dto.setId(result.getIdForDisplay());
		dto.setName(result.getName());
		dto.setDescription(result.getDescription());
		dto.setStartDate(result.getStartDate());
		dto.setEndDate(result.getEndDate());
		dto.setSegmentSeparator(result.getSegmentSeperator());
		dto.setEnabledFlag(result.isEnabledFlag());
		dto.setCreatedBy(result.getCreatedBy());
		dto.setCreatedDate(result.getCreatedDate());
		dto.setLastUpdatedBy(result.getLastUpdatedBy());
		dto.setLastUpdatedDate(result.getLastUpdatedDate());
        return ResponseEntity.created(new URI("/api/chart-of-accounts/" + dto.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, dto.getId().toString()))
            .body(dto);
    }

    /**
     * PUT  /chart-of-accounts : Updates an existing chartOfAccount.
     *
     * @param chartOfAccount the chartOfAccount to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated chartOfAccount,
     * or with status 400 (Bad Request) if the chartOfAccount is not valid,
     * or with status 500 (Internal Server Error) if the chartOfAccount couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/chart-of-accounts")
    @Timed
    public ResponseEntity<ChartOfAccountsDTO> updateChartOfAccount(HttpServletRequest request,@RequestBody ChartOfAccountsDTO chartOfAccount) throws URISyntaxException {
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	Long userId=Long.parseLong(map.get("userId").toString());
        log.debug("REST request to update ChartOfAccount of user:"+userId+"-> ", chartOfAccount);

        if (chartOfAccount.getId() == null) {
        	ChartOfAccount coaCreate = new ChartOfAccount();
        	if(chartOfAccount.getName() != null)
        		coaCreate.setName(chartOfAccount.getName());
        	if(chartOfAccount.getDescription() != null)
        		coaCreate.setDescription(chartOfAccount.getDescription());
        	if(chartOfAccount.getStartDate() != null)
        	{
        		coaCreate.setStartDate(chartOfAccount.getStartDate());
        	}
        	if(chartOfAccount.getEndDate() != null)
        	{
        		coaCreate.setEndDate(chartOfAccount.getEndDate());
        	}
        	if(chartOfAccount.getSegmentSeparator() != null)
        		coaCreate.setSegmentSeperator(chartOfAccount.getSegmentSeparator());
        	coaCreate.setEnabledFlag(chartOfAccount.isEnabledFlag());
        	coaCreate.setTenantId(tenantId);
        	coaCreate.setCreatedBy(userId);
        	coaCreate.setLastUpdatedBy(userId);
        	coaCreate.setCreatedDate(ZonedDateTime.now());
        	coaCreate.setLastUpdatedDate(ZonedDateTime.now());
            return createChartOfAccount(coaCreate);
        }
        ChartOfAccount coa = chartOfAccountRepository.findByIdForDisplayAndTenantId(chartOfAccount.getId(), tenantId);
    	if(chartOfAccount.getName() != null)
    		coa.setName(chartOfAccount.getName());
    	if(chartOfAccount.getDescription() != null)
    		coa.setDescription(chartOfAccount.getDescription());
    	if(chartOfAccount.getStartDate() != null)
    	{
    		coa.setStartDate(chartOfAccount.getStartDate());
    	}
    	if(chartOfAccount.getEndDate() != null)
    	{
    		coa.setEndDate(chartOfAccount.getEndDate());
    	}
    	else
    		if(chartOfAccount.getEndDate() == null)
        	{
        		coa.setEndDate(null);
        	}
    	if(chartOfAccount.getSegmentSeparator() != null)
    		coa.setSegmentSeperator(chartOfAccount.getSegmentSeparator());
    	coa.setEnabledFlag(chartOfAccount.isEnabledFlag());
    	coa.setLastUpdatedBy(userId);
    	coa.setLastUpdatedDate(ZonedDateTime.now());
        ChartOfAccount result = chartOfAccountRepository.save(coa);
        
		ChartOfAccountsDTO dto = new ChartOfAccountsDTO();
		dto.setId(result.getIdForDisplay());
		dto.setName(result.getName());
		dto.setDescription(result.getDescription());
		dto.setStartDate(result.getStartDate());
		dto.setEndDate(result.getEndDate());
		dto.setSegmentSeparator(result.getSegmentSeperator());
		dto.setEnabledFlag(result.isEnabledFlag());
		dto.setCreatedBy(result.getCreatedBy());
		dto.setCreatedDate(result.getCreatedDate());
		dto.setLastUpdatedBy(result.getLastUpdatedBy());
		dto.setLastUpdatedDate(result.getLastUpdatedDate());
		
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dto.getId().toString()))
            .body(dto);
    }

    /**
     * GET  /chart-of-accounts : get all the chartOfAccounts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of chartOfAccounts in body
     */
    @GetMapping("/chart-of-accounts")
    @Timed
    public ResponseEntity<List<HashMap>> getAllChartOfAccounts(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of ChartOfAccounts");
        List<HashMap> finalList = new ArrayList<HashMap>();
        Page<ChartOfAccount> page = chartOfAccountRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/chart-of-accounts");
        if(page.getSize()>0)
        {
        	for(ChartOfAccount coa : page)
        	{
        		HashMap hm = new HashMap();
        		hm.put("id", coa.getIdForDisplay());
        		hm.put("name", coa.getName());
        		hm.put("description", coa.getDescription());
        		hm.put("startDate", coa.getStartDate());
        		hm.put("endDate", coa.getEndDate());
        		hm.put("segmentSeparator", coa.getSegmentSeperator());
        		hm.put("enabledFlag", coa.isEnabledFlag());
        		hm.put("createdBy", coa.getCreatedBy());
        		hm.put("createdDate", coa.getCreatedDate());
        		hm.put("lastUpdatedBy", coa.getLastUpdatedBy());
        		hm.put("lastUpdatedDate", coa.getLastUpdatedDate());
        		finalList.add(hm);
        	}
        }
        return new ResponseEntity<>(finalList, headers, HttpStatus.OK);
    }
    
    
    @GetMapping("/getActivechartOfAccounts")
    @Timed
    public ResponseEntity<List<HashMap>> getActivechartOfAccounts(HttpServletRequest request) {
        log.debug("REST request to get a page of ChartOfAccounts");
        List<HashMap> finalList = new ArrayList<HashMap>();
       // Page<ChartOfAccount> page = chartOfAccountRepository.findAll(pageable);
       // HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/chart-of-accounts");
        HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
        List<ChartOfAccount> chartOfAccountList=chartOfAccountRepository.findByTenantIdAndActiveState(tenantId);
        log.info("chartOfAccountList :"+chartOfAccountList.size());
        if(chartOfAccountList.size()>0)
        {
        	for(ChartOfAccount coa : chartOfAccountList)
        	{
        		HashMap hm = new HashMap();
        		hm.put("id", coa.getIdForDisplay());
        		hm.put("name", coa.getName());
        		hm.put("description", coa.getDescription());
        		hm.put("startDate", coa.getStartDate());
        		hm.put("endDate", coa.getEndDate());
        		hm.put("segmentSeparator", coa.getSegmentSeperator());
        		hm.put("enabledFlag", coa.isEnabledFlag());
        		hm.put("createdBy", coa.getCreatedBy());
        		hm.put("createdDate", coa.getCreatedDate());
        		hm.put("lastUpdatedBy", coa.getLastUpdatedBy());
        		hm.put("lastUpdatedDate", coa.getLastUpdatedDate());
        		finalList.add(hm);
        	}
        }
        return new ResponseEntity<>(finalList, HttpStatus.OK);
    }

    /**
     * GET  /chart-of-accounts/:id : get the "id" chartOfAccount.
     *
     * @param id the id of the chartOfAccount to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the chartOfAccount, or with status 404 (Not Found)
     */
    @GetMapping("/chart-of-accounts/{id}")
    @Timed
    public ResponseEntity<ChartOfAccount> getChartOfAccount(HttpServletRequest request, @PathVariable String id) {
        log.debug("REST request to get ChartOfAccount : {}", id);
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
        ChartOfAccount chartOfAccount = chartOfAccountRepository.findByIdForDisplayAndTenantId(id, tenantId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(chartOfAccount));
    }

    /**
     * DELETE  /chart-of-accounts/:id : delete the "id" chartOfAccount.
     *
     * @param id the id of the chartOfAccount to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/chart-of-accounts/{id}")
    @Timed
    public ResponseEntity<Void> deleteChartOfAccount(@PathVariable Long id) {
        log.debug("REST request to delete ChartOfAccount : {}", id);
        chartOfAccountRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    /**
     * author :ravali
     * @param offset
     * @param limit
     * @param tenantId
     * @param response
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/getAllChartOfAccounts")
 	@Timed
 	public ResponseEntity<List<HashMap>> getRuleGroupsByTenantId(HttpServletRequest request,@RequestParam(value = "page" , required = false) Integer offset,
 			@RequestParam(value = "per_page", required = false) Integer limit,HttpServletResponse response,@RequestParam(value = "activeFlag" , required = false) Boolean activeFlag) throws URISyntaxException {
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());

    	log.debug("REST request to get a page of ChartOfAccount :"+activeFlag);
    	List<ChartOfAccount> COAList = new ArrayList<ChartOfAccount>();
    	PaginationUtil paginationUtil=new PaginationUtil();

    	List<HashMap> finalList = new ArrayList<HashMap>();
    	int maxlmt=paginationUtil.MAX_LIMIT;
    	int minlmt=paginationUtil.MIN_OFFSET;
    	log.info("maxlmt: "+maxlmt);
    	Page<ChartOfAccount> page = null;


    	HttpHeaders headers = null;
    	List<ChartOfAccount> COAListCnt = chartOfAccountRepository.findByTenantIdOrderByIdDesc(tenantId);
    	response.addIntHeader("X-COUNT", COAListCnt.size());

    	if(limit==null || limit<minlmt){
    		log.info("in if :");
    		if(activeFlag!=null)
    		{
    			COAList = chartOfAccountRepository.findByTenantIdAndEnabledFlagIsTrue(tenantId);
    			log.info("coalist1 :"+COAList.size());
    		}
    		else
    		{
    			COAList = chartOfAccountRepository.findByTenantIdOrderByIdDesc(tenantId);
    			log.info("coalist2 :"+COAList.size());
    		}
    		limit = COAList.size();
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
    		page = chartOfAccountRepository.findByTenantIdOrderByIdDesc(tenantId,PaginationUtil.generatePageRequest2(offset, limit));
    		headers = PaginationUtil.generatePaginationHttpHeaders2(page, "/api/functionalities",offset, limit);
    	}
    	else{
    		log.info("input limit is within maxlimit");

    		if(activeFlag!=null)
    		{
    			page = chartOfAccountRepository.findByTenantIdAndEnabledFlagIsTrue(tenantId,PaginationUtil.generatePageRequest(offset, limit));
    			headers = PaginationUtil.generatePaginationHttpHeaderss(page, "/api/functionalities", offset, limit);
    		}
    		else
    		{
    			page = chartOfAccountRepository.findByTenantIdOrderByIdDesc(tenantId,PaginationUtil.generatePageRequest(offset, limit));
    			headers = PaginationUtil.generatePaginationHttpHeaderss(page, "/api/functionalities", offset, limit);
    		}
    	}
    	if(page.getSize()>0)
    	{
    		for(ChartOfAccount coa : page)
    		{
    			HashMap hm = new HashMap();
    			hm.put("id", coa.getIdForDisplay());
    			hm.put("name", coa.getName());
    			hm.put("description", coa.getDescription());
    			hm.put("startDate", coa.getStartDate());
    			hm.put("endDate", coa.getEndDate());
    			hm.put("segmentSeperator", coa.getSegmentSeperator());

    			/** active check**/

    			Boolean activeStatus=activeStatusService.activeStatus(coa.getStartDate(), coa.getEndDate(), coa.isEnabledFlag());
    			hm.put("enabledFlag", activeStatus);
    			/** active check end**/
    			hm.put("createdBy", coa.getCreatedBy());
    			hm.put("createdDate", coa.getCreatedDate());
    			hm.put("lastUpdatedBy", coa.getLastUpdatedBy());
    			hm.put("lastUpdatedDate", coa.getLastUpdatedDate());
    			finalList.add(hm);
    		}
    	}
    	return new ResponseEntity<>(finalList, headers, HttpStatus.OK);
    }
    
    /**
     * author :ravali
     * @param COAandSegments
     * @param userId
     * @param tenantId
     */
    @PostMapping("/postCOAandSegments")
 	@Timed
 	public  HashMap postCOAandSegments(HttpServletRequest request,@RequestBody HashMap COAandSegments)//,@RequestParam Long userId,@RequestParam Long tenantId)
 	{
    	HashMap map0=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map0.get("tenantId").toString());
    	Long userId=Long.parseLong(map0.get("userId").toString());
    	
    	log.info("postCOAandSegments for tenant: "+tenantId+" user: "+userId);
    	
    	HashMap map=new HashMap();
    	ChartOfAccount coa=new ChartOfAccount();
    	coa.setCreatedBy(userId);
    	coa.setCreatedDate(ZonedDateTime.now());
    	if(COAandSegments.get("description")!=null)
    	coa.setDescription(COAandSegments.get("description").toString());
    	coa.setEnabledFlag(true);
    	if(COAandSegments.get("endDate")!=null)
    	{
    	ZonedDateTime edDate=ZonedDateTime.parse(COAandSegments.get("endDate").toString());
    	coa.setEndDate(edDate);
    	}
    	coa.setLastUpdatedBy(userId);
    	coa.setLastUpdatedDate(ZonedDateTime.now());
    	if(COAandSegments.get("name")!=null)
    	coa.setName(COAandSegments.get("name").toString());
    	if(COAandSegments.get("segmentSeperator")!=null)
    	coa.setSegmentSeperator(COAandSegments.get("segmentSeperator").toString());
    	if(COAandSegments.get("startDate")!=null)
    	{
    	ZonedDateTime stDate=ZonedDateTime.parse(COAandSegments.get("startDate").toString());
        System.out.println("stDate :"+stDate);
    	coa.setStartDate(stDate);
    	}
    	coa.setTenantId(tenantId);
    	ChartOfAccount result = chartOfAccountRepository.save(coa);
    	String idForDisplay = IDORUtils.computeFrontEndIdentifier(result.getId().toString());
    	result.setIdForDisplay(idForDisplay);
    	result = chartOfAccountRepository.save(result);
    	
    	if(result.getId()!=null)
    	{
    	List<HashMap> segmentsList=(List<HashMap>) COAandSegments.get("segments");
    	int segSeq=1;
    	for(HashMap segment:segmentsList)
    	{
    		Segments seg=new Segments();
    		seg.setCoaId(result.getId());
    		seg.setCreatedBy(userId);
    		seg.setCreatedDate(ZonedDateTime.now());
    		seg.setLastUpdatedBy(userId);
    		seg.setLastUpdatedDate(ZonedDateTime.now());
    		if(segment.get("segmentName")!=null)
    		seg.setSegmentName(segment.get("segmentName").toString());
    		if(segment.get("segmentLength")!=null)
    		seg.setSegmentLength(Integer.parseInt(segment.get("segmentLength").toString()));
    		if(segment.get("valueId")!=null)
    		seg.setValueId(Long.valueOf(segment.get("valueId").toString()));  
    		if(segment.get("qualifier")!=null)
    			seg.setQualifier(segment.get("qualifier").toString());
    		seg.setSequence(segSeq);
    		segmentsRepository.save(seg);
    		segSeq++;
    	}
    	}
    	map.put("coaId", result.getIdForDisplay());
    	map.put("coaName", result.getName());
		return map;
 	}
    
    
    
    
    /**
     * author :ravali
     * desc :getCOAandSegments
     * @param id
     * @return
     */
    
    @GetMapping("/getCOAandSegments")
   	@Timed
   	public  HashMap getCOAandSegments(HttpServletRequest request, @RequestParam String id)
    {
    	HashMap map0 = userJdbcService.getuserInfoFromToken(request);
	  	Long tenantId=Long.parseLong(map0.get("tenantId").toString());
	  	
    	HashMap COAandSegments=new HashMap();
    	ChartOfAccount coa=chartOfAccountRepository.findByIdForDisplayAndTenantId(id, tenantId);
    	COAandSegments.put("id", coa.getIdForDisplay());
    	if(coa.getDescription()!=null)
    		COAandSegments.put("description", coa.getDescription());
    	if(coa.isEnabledFlag()!=null)
    		COAandSegments.put("enabledFlag", coa.isEnabledFlag());
    	if(coa.getEndDate()!=null)
    		COAandSegments.put("endDate", coa.getEndDate());
    	if(coa.getName()!=null)
    		COAandSegments.put("name", coa.getName());
    	if(coa.getSegmentSeperator()!=null)
    		COAandSegments.put("segmentSeperator", coa.getSegmentSeperator());
    	if(coa.getStartDate()!=null)
    		COAandSegments.put("startDate", coa.getStartDate());
    	if(coa.getCreatedBy()!=null)
    	COAandSegments.put("createdBy", coa.getCreatedBy());
    	if(coa.getCreatedDate()!=null)
    	COAandSegments.put("createdDate", coa.getCreatedDate());


    	List<HashMap> segmentsMapList=new ArrayList<HashMap>();
    	List<Segments> segmentsList=segmentsRepository.findByCoaId(coa.getId());

    	for(Segments seg:segmentsList)
    	{
    		HashMap segment=new HashMap();
    		segment.put("id", seg.getId());
    		segment.put("coaId", coa.getIdForDisplay());
    		segment.put("segmentName", seg.getSegmentName());
    		segment.put("segmentLength", seg.getSegmentLength());
    		if(seg.getValueId()!=null)
    		{
    			segment.put("valueId", seg.getValueId());
    			MappingSet mappingSet=mappingSetRepository.findOne(seg.getValueId());
    			if(mappingSet!=null &&  mappingSet.getName()!=null)
    			{
    				segment.put("valueName", mappingSet.getName());
    				List<MappingSetValues> mappingSetValues = new ArrayList<MappingSetValues>();
    				mappingSetValues = mappingSetValuesRepository.findByMappingSetId(mappingSet.getId());
    				List<String> sourceValuesList = new ArrayList<String>();
    				if(mappingSetValues.size()>0)
    				{
    					for(MappingSetValues mappingsetValue : mappingSetValues)
    					{
    						sourceValuesList.add(mappingsetValue.getSourceValue());
    					}
    					segment.put("valueSet", sourceValuesList);
    				}
    			}
    			
    		}
    		if(seg.getQualifier()!=null)
    		{
    			segment.put("qualifier", seg.getQualifier());
    			LookUpCode lkCode=lookUpCodeRepository.findByTenantIdAndLookUpCodeAndLookUpType(coa.getTenantId(), seg.getQualifier(), "coa_qualifier");
    			segment.put("qualifierLKMeaning", lkCode.getMeaning());
    		}
    		segment.put("createdBy", seg.getCreatedBy());
    		segment.put("createdDate", seg.getCreatedDate());
    		segmentsMapList.add(segment);

    	}

    	COAandSegments.put("segments", segmentsMapList);
    	return COAandSegments;
    }
    /**
     * author :Shobha
     * purpose : copied above api just to fetch segments in sequence order
     * desc :getCOAandSegments
     * @param id
     * @return
     */
    
    @GetMapping("/getCOAandSegmentsOrderBySequence")
   	@Timed
   	public  HashMap getCOAandSegmentsOderBySequence(HttpServletRequest request, @RequestParam String id, @RequestParam(required = false) String qualifier)
    {
    	log.info("get coas "+id+" qualifier"+qualifier);
    	
    	HashMap map0 = userJdbcService.getuserInfoFromToken(request);
	  	Long tenantId=Long.parseLong(map0.get("tenantId").toString());

    	HashMap COAandSegments=new HashMap();
    	ChartOfAccount coa=chartOfAccountRepository.findByIdForDisplayAndTenantId(id, tenantId);
    	log.info("coa :"+coa);
    	COAandSegments.put("id", coa.getIdForDisplay());
    	if(coa.getDescription()!=null)
    		COAandSegments.put("description", coa.getDescription());
    	if(coa.isEnabledFlag()!=null)
    		COAandSegments.put("enabledFlag", coa.isEnabledFlag());
    	if(coa.getEndDate()!=null)
    		COAandSegments.put("endDate", coa.getEndDate());
    	if(coa.getName()!=null)
    		COAandSegments.put("name", coa.getName());
    	if(coa.getSegmentSeperator()!=null)
    		COAandSegments.put("segmentSeperator", coa.getSegmentSeperator());
    	if(coa.getStartDate()!=null)
    		COAandSegments.put("startDate", coa.getStartDate());
    	if(coa.getCreatedBy()!=null)
    	COAandSegments.put("createdBy", coa.getCreatedBy());
    	if(coa.getCreatedDate()!=null)
    	COAandSegments.put("createdDate", coa.getCreatedDate());


    	List<HashMap> segmentsMapList=new ArrayList<HashMap>();
    	List<Segments> segmentsList=null;
    	
    	if(qualifier!=null && !qualifier.equals("")){
    		segmentsList=segmentsRepository.findByCoaIdAndQualifierOrderBySequenceAsc(coa.getId(),qualifier);
    	}
    	else{
   		 segmentsList=segmentsRepository.findByCoaIdOrderBySequenceAsc(coa.getId());
    	}
    	for(Segments seg:segmentsList)
    	{
    		HashMap segment=new HashMap();
    		segment.put("id", seg.getId());
    		segment.put("coaId", coa.getIdForDisplay());
    		segment.put("segmentName", seg.getSegmentName());
    		segment.put("segmentLength", seg.getSegmentLength());
    		if(seg.getValueId()!=null)
    		{
    			segment.put("valueId", seg.getValueId());
    			MappingSet mappingSet=mappingSetRepository.findOne(seg.getValueId());
    			if(mappingSet!=null &&  mappingSet.getName()!=null)
    			{
    				segment.put("valueName", mappingSet.getName());
    				List<MappingSetValues> mappingSetValues = new ArrayList<MappingSetValues>();
    				mappingSetValues = mappingSetValuesRepository.fetchActiveMappingSetValuesByMappingId(mappingSet.getId());
    				List<String[]> sourceValuesList = new ArrayList<String[]>();
    				if(mappingSetValues.size()>0)
    				{
    					for(MappingSetValues mappingsetValue : mappingSetValues)
    					{
    						String[] str = new String[2];
    						if(mappingsetValue.getSourceValue() != null)
    						{
    						str[0] = mappingsetValue.getSourceValue();
    						str[1] = mappingsetValue.getSourceValue() ;
    						if(mappingsetValue.getTargetValue()  != null)
    							str[1] = str[1]+" - " + mappingsetValue.getTargetValue() ;
    						sourceValuesList.add(str);
    						}
    					}
    					segment.put("valueSet", sourceValuesList);
    				}
    			}
    			
    		}
    		segment.put("createdBy", seg.getCreatedBy());
    		segment.put("createdDate", seg.getCreatedDate());
    		segmentsMapList.add(segment);

    	}

    	COAandSegments.put("segments", segmentsMapList);
    	return COAandSegments;
    	}
    
    /**
     * Author: Shiva
     * Purpose: Check weather chart of account name exist or not
     * **/
    @GetMapping("/checkCOAIsExist")
	@Timed
	public HashMap checkCOAIsExist(@RequestParam String name,HttpServletRequest request,@RequestParam(required=false,value="id") String id)
	{
    	HashMap map0 = userJdbcService.getuserInfoFromToken(request);
	  	Long tenantId=Long.parseLong(map0.get("tenantId").toString());
		HashMap map=new HashMap();
		map.put("result", "No Duplicates Found");
		if(id != null)
		{
			ChartOfAccount coaWithId = chartOfAccountRepository.findByIdForDisplayAndNameAndTenantId(id, name, tenantId);
			if(coaWithId != null)
			{
				map.put("result", "No Duplicates Found");
			}
			else
			{
				List<ChartOfAccount> coas = chartOfAccountRepository.findByTenantIdAndName(tenantId, name);
				if(coas.size()>0)
				{
					map.put("result", "'"+name+"' coa already exists");
				}
			}
		}
		else 
		{
			List<ChartOfAccount> coas = chartOfAccountRepository.findByTenantIdAndName(tenantId, name);
			if(coas.size()>0)
			{
				map.put("result", "'"+name+"' coa already exists");
			}
		}
		return map;
	}
}
