package com.nspl.app.web.rest;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import com.nspl.app.domain.Calendar;
import com.nspl.app.domain.ChartOfAccount;
import com.nspl.app.domain.LedgerDefinition;
import com.nspl.app.repository.CalendarRepository;
import com.nspl.app.repository.ChartOfAccountRepository;
import com.nspl.app.repository.LedgerDefinitionRepository;
import com.nspl.app.security.IDORUtils;
import com.nspl.app.service.ActiveStatusService;
import com.nspl.app.service.UserJdbcService;
import com.nspl.app.web.rest.dto.LedgerDefinitionDTO;
import com.nspl.app.web.rest.util.HeaderUtil;
import com.nspl.app.web.rest.util.PaginationUtil;

/**
 * REST controller for managing LedgerDefinition.
 */
@RestController
@RequestMapping("/api")
public class LedgerDefinitionResource {

    private final Logger log = LoggerFactory.getLogger(LedgerDefinitionResource.class);

    private static final String ENTITY_NAME = "ledgerDefinition";
        
    private final LedgerDefinitionRepository ledgerDefinitionRepository;
    
    
    @Inject
    CalendarRepository calendarRepository;
    
    @Inject
    ChartOfAccountRepository chartOfAccountRepository;
    
    @Inject
	UserJdbcService userJdbcService;
    
    @Inject
    ActiveStatusService activeStatusService;

    public LedgerDefinitionResource(LedgerDefinitionRepository ledgerDefinitionRepository) {
        this.ledgerDefinitionRepository = ledgerDefinitionRepository;
    }

    /**
     * POST  /ledger-definitions : Create a new ledgerDefinition.
     *
     * @param ledgerDefinition the ledgerDefinition to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ledgerDefinition, or with status 400 (Bad Request) if the ledgerDefinition has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/ledger-definitions")
    @Timed
    public ResponseEntity<LedgerDefinitionDTO> createLedgerDefinition(HttpServletRequest request, @RequestBody LedgerDefinitionDTO ledgerDefinition) throws URISyntaxException {
        log.debug("REST request to save LedgerDefinition : {}", ledgerDefinition);
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	Long userId=Long.parseLong(map.get("userId").toString());
    	
        if (ledgerDefinition.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new ledgerDefinition cannot already have an ID")).body(null);
        }
        
        LedgerDefinition createLD = new LedgerDefinition();
        createLD.setTenantId(tenantId);
        if(ledgerDefinition.getName() != null)
        	createLD.setName(ledgerDefinition.getName());
        if(ledgerDefinition.getDescription() != null)        
        	createLD.setDescription(ledgerDefinition.getDescription());
        if(ledgerDefinition.getLedgerType() != null)
        	createLD.setLedgerType(ledgerDefinition.getLedgerType());
        if(ledgerDefinition.getStartDate() != null)
        	createLD.setStartDate(ledgerDefinition.getStartDate());
        if(ledgerDefinition.getEndDate() != null)
        	createLD.setEndDate(ledgerDefinition.getEndDate());
        if(ledgerDefinition.getCurrency() != null)
        	createLD.setCurrency(ledgerDefinition.getCurrency());
        if(ledgerDefinition.getCalendarId() != null)
        {
        	Calendar calendar = calendarRepository.findByIdForDisplayAndTenantId(ledgerDefinition.getCalendarId(), tenantId);
        	if(calendar != null)
        		createLD.setCalendarId(calendar.getId());
        }	
        if(ledgerDefinition.getStatus() != null)
        	createLD.setStatus(ledgerDefinition.getStatus());
        	createLD.setEnabledFlag(ledgerDefinition.isEnabledFlag());
        if(ledgerDefinition.getCoaId() != null)
        {
        	ChartOfAccount coa = chartOfAccountRepository.findByIdForDisplayAndTenantId(ledgerDefinition.getCoaId(), tenantId);
        	if(coa != null)
        		createLD.setCoaId(coa.getId());
        }
        createLD.setCreatedBy(userId);
        createLD.setLastUpdatedBy(userId);
        createLD.setCreatedDate(ZonedDateTime.now());
        createLD.setLastUpdatedDate(ZonedDateTime.now());
        LedgerDefinition result = ledgerDefinitionRepository.save(createLD);
		String idForDisplay = IDORUtils.computeFrontEndIdentifier(result.getId().toString());
		result.setIdForDisplay(idForDisplay);
		result = ledgerDefinitionRepository.save(result);
		
		ledgerDefinition.setId(result.getIdForDisplay());
		
        return ResponseEntity.created(new URI("/api/ledger-definitions/" + ledgerDefinition.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, ledgerDefinition.getId().toString()))
            .body(ledgerDefinition);
    }

    /**
     * PUT  /ledger-definitions : Updates an existing ledgerDefinition.
     *
     * @param ledgerDefinition the ledgerDefinition to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ledgerDefinition,
     * or with status 400 (Bad Request) if the ledgerDefinition is not valid,
     * or with status 500 (Internal Server Error) if the ledgerDefinition couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/ledger-definitions")
    @Timed
    public ResponseEntity<LedgerDefinitionDTO> updateLedgerDefinition(HttpServletRequest request,@RequestBody LedgerDefinitionDTO ledgerDefinition) throws URISyntaxException {
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId = Long.parseLong(map.get("tenantId").toString());
    	Long userId = Long.parseLong(map.get("userId").toString());
        log.debug("REST request to update LedgerDefinition of user: "+userId+"-> ", ledgerDefinition);
        if (ledgerDefinition.getId() == null) {
        	return createLedgerDefinition(request, ledgerDefinition);
        }
        LedgerDefinition ld = ledgerDefinitionRepository.findByIdForDisplayAndTenantId(ledgerDefinition.getId(), tenantId);

        ld.setLastUpdatedBy(userId);
        ld.setLastUpdatedDate(ZonedDateTime.now());
 
        if(ledgerDefinition.getName() != null)
        	ld.setName(ledgerDefinition.getName());
        if(ledgerDefinition.getDescription() != null)        
        	ld.setDescription(ledgerDefinition.getDescription());
        if(ledgerDefinition.getLedgerType() != null)
        	ld.setLedgerType(ledgerDefinition.getLedgerType());
        if(ledgerDefinition.getStartDate() != null)
        	ld.setStartDate(ledgerDefinition.getStartDate());
        if(ledgerDefinition.getEndDate() != null)
        	ld.setEndDate(ledgerDefinition.getEndDate());
        if(ledgerDefinition.getCurrency() != null)
        	ld.setCurrency(ledgerDefinition.getCurrency());
        if(ledgerDefinition.getCalendarId() != null)
        {
        	Calendar calendar = calendarRepository.findByIdForDisplayAndTenantId(ledgerDefinition.getCalendarId(), tenantId);
        	ld.setCalendarId(calendar.getId());
        }	
        if(ledgerDefinition.getStatus() != null)
        	ld.setStatus(ledgerDefinition.getStatus());
        	ld.setEnabledFlag(ledgerDefinition.isEnabledFlag());
        if(ledgerDefinition.getCoaId() != null)
        {
        	ChartOfAccount coa = chartOfAccountRepository.findByIdForDisplayAndTenantId(ledgerDefinition.getCoaId(), tenantId);
        	ld.setCoaId(coa.getId());
        }
        LedgerDefinition result = ledgerDefinitionRepository.save(ld);
        
        LedgerDefinitionDTO finalResult = new LedgerDefinitionDTO();
        finalResult.setId(result.getIdForDisplay());
        finalResult.setName(result.getName());
        finalResult.setDescription(result.getDescription());
        finalResult.setLedgerType(result.getLedgerType());
        finalResult.setStartDate(result.getStartDate());
        finalResult.setEndDate(result.getEndDate());
        finalResult.setCurrency(result.getCurrency());
        
        if(result.getCalendarId() != null)
        {
        	Calendar cal = calendarRepository.findOne(result.getCalendarId());
            finalResult.setCalendarId(cal.getIdForDisplay());        	
        }
        finalResult.setStatus(result.getStatus());
        finalResult.setEnabledFlag(result.isEnabledFlag());
        
        if(result.getCoaId() != null)
        {
        	ChartOfAccount coa = chartOfAccountRepository.findOne(result.getCoaId());
        	finalResult.setCoaId(coa.getIdForDisplay());
        }
        finalResult.setCreatedBy(result.getCreatedBy());
        finalResult.setLastUpdatedBy(result.getLastUpdatedBy());
        finalResult.setCreatedDate(result.getCreatedDate());
        finalResult.setLastUpdatedDate(result.getLastUpdatedDate());
        
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, finalResult.getId().toString()))
            .body(finalResult);
    }

    /**
     * GET  /ledger-definitions : get all the ledgerDefinitions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of ledgerDefinitions in body
     */
    @GetMapping("/ledger-definitions")
    @Timed
    public ResponseEntity<List<LedgerDefinition>> getAllLedgerDefinitions(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of LedgerDefinitions");
        Page<LedgerDefinition> page = ledgerDefinitionRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ledger-definitions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /ledger-definitions/:id : get the "id" ledgerDefinition.
     *
     * @param id the id of the ledgerDefinition to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ledgerDefinition, or with status 404 (Not Found)
     */
    @GetMapping("/ledger-definitions/{id}")
    @Timed
    public ResponseEntity<LedgerDefinitionDTO> getLedgerDefinition(HttpServletRequest request, @PathVariable String id) {
        log.debug("REST request to get LedgerDefinition : {}", id);
        
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
        LedgerDefinition ld = ledgerDefinitionRepository.findByIdForDisplayAndTenantId(id, tenantId);
        LedgerDefinitionDTO dto = new LedgerDefinitionDTO();
        dto.setId(ld.getIdForDisplay());
		dto.setName(ld.getName());
		dto.setDescription(ld.getDescription());
		dto.setLedgerType(ld.getLedgerType());
		dto.setStartDate(ld.getStartDate());
		dto.setEndDate(ld.getEndDate());
		dto.setCurrency(ld.getCurrency());
		if(ld.getCalendarId() != null)
		{
 			Calendar calendar = calendarRepository.findOne(ld.getCalendarId());
 			dto.setCalendarId(calendar.getIdForDisplay());     				
		}
		dto.setStatus(ld.getStatus());
		dto.setEnabledFlag(ld.isEnabledFlag());
		if(ld.getCoaId() != null)
		{
			ChartOfAccount coa = chartOfAccountRepository.findOne(ld.getCoaId());
			dto.setCoaId(coa.getIdForDisplay());
		}
		dto.setCreatedBy(ld.getCreatedBy());
		dto.setCreatedDate(ld.getCreatedDate());
		dto.setLastUpdatedBy(ld.getLastUpdatedBy());
		dto.setLastUpdatedDate(ld.getLastUpdatedDate());
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(dto));
    }

    /**
     * DELETE  /ledger-definitions/:id : delete the "id" ledgerDefinition.
     *
     * @param id the id of the ledgerDefinition to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/ledger-definitions/{id}")
    @Timed
    public ResponseEntity<Void> deleteLedgerDefinition(@PathVariable Long id) {
        log.debug("REST request to delete LedgerDefinition : {}", id);
        ledgerDefinitionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    
    
    
    @GetMapping("/getAllLedgers")
 	@Timed
 	public ResponseEntity<List<LedgerDefinitionDTO>> getRuleGroupsByTenantId(HttpServletRequest request,@RequestParam(value = "page" , required = false) Integer offset,
 			@RequestParam(value = "per_page", required = false) Integer limit,HttpServletResponse response) throws URISyntaxException {
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	List<LedgerDefinitionDTO> finalList = new ArrayList<LedgerDefinitionDTO>();
 		log.debug("REST request to get a page of FxRates of tenantId: "+tenantId);
 		List<LedgerDefinition> ledgerDefList = new ArrayList<LedgerDefinition>();
 		PaginationUtil paginationUtil=new PaginationUtil();
 		
 		int maxlmt=paginationUtil.MAX_LIMIT;
 		int minlmt=paginationUtil.MIN_OFFSET;
 		log.info("maxlmt: "+maxlmt);
 		Page<LedgerDefinition> page = null;
 		HttpHeaders headers = null;
 		
 		List<LedgerDefinition> ledgerDefListCnt = ledgerDefinitionRepository.findByTenantIdOrderByIdDesc(tenantId);
    	response.addIntHeader("X-COUNT", ledgerDefListCnt.size());
 		
 		if(limit==null || limit<minlmt){
 			ledgerDefList = ledgerDefinitionRepository.findByTenantIdOrderByIdDesc(tenantId);
 			limit = ledgerDefList.size();
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
 			 page = ledgerDefinitionRepository.findByTenantIdOrderByIdDesc(tenantId,PaginationUtil.generatePageRequest2(offset, limit));
 			headers = PaginationUtil.generatePaginationHttpHeaders2(page, "/api/getAllLedgers",offset, limit);
 		}
 		else{
 			log.info("input limit is within maxlimit");
 			page = ledgerDefinitionRepository.findByTenantIdOrderByIdDesc(tenantId,PaginationUtil.generatePageRequest(offset, limit));
 			headers = PaginationUtil.generatePaginationHttpHeaderss(page, "/api/getAllLedgers", offset, limit);
 		}
     	if(page.getSize()>0)
     	{
     		for(LedgerDefinition ld : page)
     		{
     			LedgerDefinitionDTO dto = new LedgerDefinitionDTO();
     			dto.setId(ld.getIdForDisplay());
     			dto.setName(ld.getName());
     			dto.setDescription(ld.getDescription());
     			dto.setLedgerType(ld.getLedgerType());
     			dto.setStartDate(ld.getStartDate());
     			dto.setEndDate(ld.getEndDate());
     			dto.setCurrency(ld.getCurrency());
     			if(ld.getCalendarId() != null)
     			{
         			Calendar calendar = calendarRepository.findOne(ld.getCalendarId());
         			dto.setCalendarId(calendar.getIdForDisplay());     				
     			}
     			dto.setStatus(ld.getStatus());
     			/**check active status**/
    			Boolean activeStatus=activeStatusService.activeStatus(ld.getStartDate(), ld.getEndDate(), ld.isEnabledFlag());
     			dto.setEnabledFlag(activeStatus);
     			if(ld.getCoaId() != null)
     			{
     				ChartOfAccount coa = chartOfAccountRepository.findOne(ld.getCoaId());
     				dto.setCoaId(coa.getIdForDisplay());
     			}
     			dto.setCreatedBy(ld.getCreatedBy());
     			dto.setCreatedDate(ld.getCreatedDate());
     			dto.setLastUpdatedBy(ld.getLastUpdatedBy());
     			dto.setLastUpdatedDate(ld.getLastUpdatedDate());
     			finalList.add(dto);
     		}
     	}
 		return new ResponseEntity<>(finalList, headers, HttpStatus.OK);
     }
    
    
    
    /**Author: Shiva
     * Description: API for fetching Ledger Details based on tenant id
     * @param tenantId
     * return List<LinkedHashmap> with ledger information
     */
    @GetMapping("/getLedgerDataByTenantId")
    @Timed
    public List<LinkedHashMap> getLedgerDataByTenantId(HttpServletRequest request){
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	log.info("Rest api for fetching ledger data for the tenant id: "+ tenantId);
    	List<LinkedHashMap> finalData = new ArrayList<LinkedHashMap>();
    	
    	List<LedgerDefinition> ledgers = ledgerDefinitionRepository.findByTenantId(tenantId);
    	log.info("No of records fetched: "+ ledgers.size());
    	if(ledgers.size()>0)
    	{
    		for(LedgerDefinition ledger : ledgers)
    		{
    			LinkedHashMap hm = new LinkedHashMap();
    			hm.put("id", ledger.getIdForDisplay());
    			hm.put("name", ledger.getName());
    			hm.put("description", ledger.getDescription());
    			hm.put("ledgerType", ledger.getLedgerType());
    			hm.put("startDate", ledger.getStartDate());
    			hm.put("endDate", ledger.getEndDate());
    			hm.put("currency", ledger.getCurrency());
    			hm.put("status",ledger.getStatus());
    			hm.put("enabledFlag", ledger.isEnabledFlag());
    			hm.put("createdBy", ledger.getCreatedBy());
    			hm.put("createdDate", ledger.getCreatedDate());
    			hm.put("lastUpdatedBy", "lastUpdatedDate");
    			hm.put("lastUpdatedDate", ledger.getLastUpdatedDate());
    			if(ledger.getCalendarId() != null)
    			{
    				Calendar calendar=calendarRepository.findOne(ledger.getCalendarId());
        			hm.put("calendarName",calendar.getName());
        			hm.put("calendarId", calendar.getIdForDisplay());
    			}
    			if(ledger.getCoaId() != null)
    			{
        			ChartOfAccount coa=chartOfAccountRepository.findOne(ledger.getCoaId());
    				hm.put("coaId",coa.getIdForDisplay());
        			hm.put("coaName", coa.getName());
    			}
    			finalData.add(hm);
    		}
    	}
    	log.info("Final Size: "+ finalData.size());
    	return finalData;
    }
    
    /**
     * Author : Shobha
     * @param tenantId
     * @param coa
     * @return
     */
    @GetMapping("/getLedgerDataByTenantIdAndCoa")
    @Timed
    public List<LedgerDefinitionDTO> getLedgerDataByTenantIdandCOA(HttpServletRequest request,@RequestParam(required = false) String coa){
    	log.info("REST API to fetch ledgers by tenant id and coa id: "+ coa);
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	List<LedgerDefinitionDTO> finalList = new ArrayList<LedgerDefinitionDTO>();
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	List<LedgerDefinition> ledgersbyTenantAndCoa = new ArrayList<LedgerDefinition>();
    	/*ChartOfAccount coaRecord = chartOfAccountRepository.findByIdForDisplayAndTenantId(coa, tenantId);*/
    	ChartOfAccount chartOfAccount  = chartOfAccountRepository.findByTenantIdAndIdForDisplay(tenantId,coa);
    	log.info("coa id: "+chartOfAccount.getId());
    	if(chartOfAccount != null)
    	{
    		ledgersbyTenantAndCoa = ledgerDefinitionRepository.findByTenantIdAndCoaIdAndActive(tenantId,chartOfAccount.getId());
    		log.info("Ledgers Size: "+ledgersbyTenantAndCoa.size());
        	if(ledgersbyTenantAndCoa.size()>0)
        	{
        		for(LedgerDefinition ld : ledgersbyTenantAndCoa)
        		{
        			LedgerDefinitionDTO dto = new LedgerDefinitionDTO();
        			dto.setId(ld.getIdForDisplay());
        			dto.setName(ld.getName());
        			dto.setDescription(ld.getDescription());
        			dto.setLedgerType(ld.getLedgerType());
        			dto.setStartDate(ld.getStartDate());
        			dto.setEndDate(ld.getEndDate());
        			dto.setCurrency(ld.getCurrency());
        			if(ld.getCalendarId() != null)
        			{
        				Calendar cal = calendarRepository.findOne(ld.getCalendarId());
        				dto.setCalendarId(cal.getIdForDisplay());
        			}
        			dto.setStatus(ld.getStatus());
        			dto.setEnabledFlag(ld.isEnabledFlag());
        			if(ld.getCoaId() != null)
        			{
        				ChartOfAccount ca = chartOfAccountRepository.findOne(ld.getCoaId());
        				dto.setCoaId(ca.getIdForDisplay());
        			}
        			dto.setCreatedBy(ld.getCreatedBy());
        			dto.setCreatedDate(ld.getCreatedDate());
        			dto.setLastUpdatedBy(ld.getLastUpdatedBy());
        			dto.setLastUpdatedDate(ld.getLastUpdatedDate());
        			finalList.add(dto);
        		}
        	}
    	}
    	log.info("Ledgers Final Result : "+finalList.size());
    	return finalList;
    }
    
    /**
     * Author: Shiva
     * Purpose: Check weather ledger exist or not
     * **/
    @GetMapping("/checkLedgerIsExist")
	@Timed
	public HashMap checkLedgerIsExist(@RequestParam String name,HttpServletRequest request,@RequestParam(required=false,value="id") String id)
	{
    	HashMap map0=userJdbcService.getuserInfoFromToken(request);
	  	Long tenantId=Long.parseLong(map0.get("tenantId").toString());
		HashMap map=new HashMap();
		map.put("result", "No Duplicates Found");
		if(id != null)
		{
			LedgerDefinition ledgerWithId = ledgerDefinitionRepository.findByIdForDisplayAndNameAndTenantId(id, name, tenantId);
			if(ledgerWithId != null)
			{
				map.put("result", "No Duplicates Found");
			}
			else
			{
				List<LedgerDefinition> ledgers = ledgerDefinitionRepository.findByTenantIdAndName(tenantId, name);
				if(ledgers.size()>0)
				{
					map.put("result", "'"+name+"' ledger name already exists");
				}
			}
		}
		else 
		{
			List<LedgerDefinition> ledgers = ledgerDefinitionRepository.findByTenantIdAndName(tenantId, name);
			if(ledgers.size()>0)
			{
				map.put("result", "'"+name+"' ledger already exists");
			}
		}
		return map;
	}
}
