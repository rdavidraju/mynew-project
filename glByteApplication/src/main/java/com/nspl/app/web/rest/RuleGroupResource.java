package com.nspl.app.web.rest;


import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.swing.text.Segment;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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

import com.codahale.metrics.annotation.Timed;
import com.esotericsoftware.minlog.Log;
import com.google.api.client.util.Data;
import com.nspl.app.config.ApplicationContextProvider;
import com.nspl.app.domain.AccountingLineTypes;
import com.nspl.app.domain.AcctRuleConditions;
import com.nspl.app.domain.AcctRuleDerivations;
import com.nspl.app.domain.AppRuleConditions;
import com.nspl.app.domain.ApprovalGroups;
import com.nspl.app.domain.ApprovalRuleAssignment;
import com.nspl.app.domain.ChartOfAccount;
import com.nspl.app.domain.DataViews;
import com.nspl.app.domain.DataViewsColumns;
import com.nspl.app.domain.FxRates;
import com.nspl.app.domain.LedgerDefinition;
import com.nspl.app.domain.LookUpCode;
import com.nspl.app.domain.MappingSet;
import com.nspl.app.domain.ReconciliationResult;
import com.nspl.app.domain.RuleConditions;
import com.nspl.app.domain.RuleGroup;
import com.nspl.app.domain.RuleGroupDetails;
import com.nspl.app.domain.Rules;
import com.nspl.app.domain.Segments;
import com.nspl.app.domain.TenantConfigModules;
import com.nspl.app.repository.AccountedSummaryRepository;
import com.nspl.app.repository.AccountingLineTypesRepository;
import com.nspl.app.repository.AcctRuleConditionsRepository;
import com.nspl.app.repository.AcctRuleDerivationsRepository;
import com.nspl.app.repository.AppRuleConditionsRepository;
import com.nspl.app.repository.ApprovalGroupsRepository;
import com.nspl.app.repository.ApprovalRuleAssignmentRepository;
import com.nspl.app.repository.ChartOfAccountRepository;
import com.nspl.app.repository.DataViewsColumnsRepository;
import com.nspl.app.repository.DataViewsRepository;
import com.nspl.app.repository.FxRatesRepository;
import com.nspl.app.repository.LedgerDefinitionRepository;
import com.nspl.app.repository.LookUpCodeRepository;
import com.nspl.app.repository.MappingSetRepository;
import com.nspl.app.repository.ReconciliationResultRepository;
import com.nspl.app.repository.RuleConditionsRepository;
import com.nspl.app.repository.RuleGroupDetailsRepository;
import com.nspl.app.repository.RuleGroupRepository;
import com.nspl.app.repository.RulesRepository;
import com.nspl.app.repository.SegmentsRepository;
import com.nspl.app.repository.TenantConfigModulesRepository;
import com.nspl.app.repository.search.RuleGroupSearchRepository;
import com.nspl.app.security.IDORUtils;
import com.nspl.app.service.ActiveStatusService;
import com.nspl.app.service.DataViewsService;
import com.nspl.app.service.ElasticSearchColumnNamesService;
import com.nspl.app.service.OozieService;
import com.nspl.app.service.RuleGroupService;
import com.nspl.app.service.RuleService;
import com.nspl.app.service.UserJdbcService;
import com.nspl.app.web.rest.dto.AccountingRuleDTO;
import com.nspl.app.web.rest.dto.AcctRuleCondDTO;
import com.nspl.app.web.rest.dto.AcctRuleDerivationDTO;
import com.nspl.app.web.rest.dto.AppRuleCondAndActDto;
import com.nspl.app.web.rest.dto.ApprRuleAssgnDto;
import com.nspl.app.web.rest.dto.ApprovalActionDto;
import com.nspl.app.web.rest.dto.ApprovalRuleDto;
import com.nspl.app.web.rest.dto.ErrorReport;
import com.nspl.app.web.rest.dto.FileTemplatesPostingDTO;
import com.nspl.app.web.rest.dto.LineItems;
import com.nspl.app.web.rest.dto.RuleConditionsDTO;
import com.nspl.app.web.rest.dto.RuleDTO;
import com.nspl.app.web.rest.dto.RuleGroupDTO;
import com.nspl.app.web.rest.dto.RuleGroupDetailsDTO;
import com.nspl.app.web.rest.dto.RulesAndLineItems;
import com.nspl.app.web.rest.dto.RulesDTO;
import com.nspl.app.web.rest.util.HeaderUtil;
import com.nspl.app.web.rest.util.PaginationUtil;

/**
 * REST controller for managing RuleGroup.
 */
@RestController
@RequestMapping("/api")
public class RuleGroupResource {

    private final Logger log = LoggerFactory.getLogger(RuleGroupResource.class);

    private static final String ENTITY_NAME = "ruleGroup";
        
    private final RuleGroupRepository ruleGroupRepository;

    //private final RuleGroupSearchRepository ruleGroupSearchRepository;
    
    @Inject
    RuleConditionsRepository ruleConditionsRepository;
    
    @Inject
    RuleGroupDetailsRepository ruleGroupDetailsRepository; 
    
    @Inject
    RulesRepository rulesRepository;
    
    @Inject
    DataViewsRepository dataViewsRepository;
    
    @Inject
    DataViewsColumnsRepository dataViewsColumnsRepository;
    
    @Inject
    AccountingLineTypesRepository accountingLineTypesRepository;
    
    @Inject
    AcctRuleConditionsRepository acctRuleConditionsRepository;
    
    @Inject
    AcctRuleDerivationsRepository acctRuleDerivationsRepository;
    
    @Inject
    MappingSetRepository mappingSetRepository;
    
    @Inject
    LookUpCodeRepository lookUpCodeRepository;
    
    @Inject
    DataViewsService dataViewsService;
    
    @Inject
    RuleService ruleService;
    
    @Inject
    RuleGroupService ruleGroupService;
    
    @Inject
    AppRuleConditionsRepository appRuleConditionsRepository;
    
    @Inject
    ApprovalRuleAssignmentRepository approvalRuleAssignmentRepository;
    
    @Inject
    ApprovalGroupsRepository approvalGroupsRepository;
    
    @Inject
    ElasticSearchColumnNamesService elasticSearchColumnNamesService;
    
    @Inject
    private Environment env;
    
    @Inject
    UserJdbcService userJdbcService;
    
    @Inject
    OozieService oozieService;
    
    @Inject
    SegmentsRepository segmentsRepository;
    
    @Inject
    ChartOfAccountRepository chartOfAccountRepository;
    
    @Inject 
    LedgerDefinitionRepository ledgerDefinitionRepository;
    
    @Inject 
    FxRatesRepository fxRatesRepository;
    
    @Inject
    AccountedSummaryRepository accountedSummaryRepository;
    
    @Inject
    ReconciliationResultRepository reconciliationResultRepository;
    
    @Inject
    AppModuleSummaryResource appModuleSummaryResource;
    
    @Inject
    TenantConfigModulesRepository tenantConfigModulesRepository;
    
    
    @Inject
    ActiveStatusService activeStatusService;
    
    @PersistenceContext(unitName="default")
	private EntityManager em;

    public RuleGroupResource(RuleGroupRepository ruleGroupRepository, RuleGroupSearchRepository ruleGroupSearchRepository,AccountedSummaryRepository accountedSummaryRepository,ReconciliationResultRepository reconciliationResultRepository) {
        this.ruleGroupRepository = ruleGroupRepository;
        //this.ruleGroupSearchRepository = ruleGroupSearchRepository;
        this.accountedSummaryRepository = accountedSummaryRepository;
        this.reconciliationResultRepository = reconciliationResultRepository;
    }

    /**
     * POST  /rule-groups : Create a new ruleGroup.
     *
     * @param ruleGroup the ruleGroup to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ruleGroup, or with status 400 (Bad Request) if the ruleGroup has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/rule-groups")
    @Timed
    public ResponseEntity<RuleGroup> createRuleGroup(@RequestBody RuleGroup ruleGroup) throws URISyntaxException {
        log.debug("REST request to save RuleGroup : {}", ruleGroup);
        if (ruleGroup.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new ruleGroup cannot already have an ID")).body(null);
        }
        RuleGroup result = ruleGroupRepository.save(ruleGroup);
        //ruleGroupSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/rule-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rule-groups : Updates an existing ruleGroup.
     *
     * @param ruleGroup the ruleGroup to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ruleGroup,
     * or with status 400 (Bad Request) if the ruleGroup is not valid,
     * or with status 500 (Internal Server Error) if the ruleGroup couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/rule-groups")
    @Timed
    public ResponseEntity<RuleGroup> updateRuleGroup(@RequestBody RuleGroup ruleGroup) throws URISyntaxException {
        log.debug("REST request to update RuleGroup : {}", ruleGroup);
        if (ruleGroup.getId() == null) {
            return createRuleGroup(ruleGroup);
        }
        RuleGroup result = ruleGroupRepository.save(ruleGroup);
       // ruleGroupSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ruleGroup.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rule-groups : get all the ruleGroups.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of ruleGroups in body
     */
    @GetMapping("/rule-groups")
    @Timed
    public List<RuleGroup> getAllRuleGroups(HttpServletRequest request) {
        HashMap map=userJdbcService.getuserInfoFromToken(request);
        Long tenantId=Long.parseLong(map.get("tenantId").toString());
        log.debug("REST request to get all RuleGroups for tenantId: "+tenantId);
        List<RuleGroup> ruleGroups = ruleGroupRepository.findByTenantIdOrderByIdDesc(tenantId);
        return ruleGroups;
    }
    /**
     * Author : Shobha
     * @param offset
     * @param limit
     * @param tenantId
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/ruleGroupsByTenantId")
	@Timed
	public ResponseEntity<List<RuleGroup>> getRuleGroupsByTenantId(HttpServletRequest request,@RequestParam(value = "page" , required = false) Integer offset,
			@RequestParam(value = "per_page", required = false) Integer limit) throws URISyntaxException {
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		log.debug("REST request to get a page of Rule groups for tenant: "+tenantId);
		List<RuleGroup> ruleGroupList = new ArrayList<RuleGroup>();
		PaginationUtil paginationUtil=new PaginationUtil();
		
		int maxlmt=paginationUtil.MAX_LIMIT;
		int minlmt=paginationUtil.MIN_OFFSET;
		log.info("maxlmt: "+maxlmt);
		Page<RuleGroup> page = null;
		HttpHeaders headers = null;
		
		if(limit==null || limit<minlmt){
			ruleGroupList = ruleGroupRepository.findByTenantIdOrderByIdDesc(tenantId);
			limit = ruleGroupList.size();
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
			 page = ruleGroupRepository.findByTenantIdOrderByIdDesc(tenantId,PaginationUtil.generatePageRequest2(offset, limit));
			headers = PaginationUtil.generatePaginationHttpHeaders2(page, "/api/ruleGroupsByTenantId",offset, limit);
		}
		else{
			log.info("input limit is within maxlimit");
			page = ruleGroupRepository.findByTenantIdOrderByIdDesc(tenantId,PaginationUtil.generatePageRequest(offset, limit));
			headers = PaginationUtil.generatePaginationHttpHeaderss(page, "/api/ruleGroupsByTenantId", offset, limit);
		}
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /rule-groups/:id : get the "id" ruleGroup.
     *
     * @param id the id of the ruleGroup to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ruleGroup, or with status 404 (Not Found)
     */
    @GetMapping("/rule-groups/{id}")
    @Timed
    public ResponseEntity<RuleGroup> getRuleGroup(@PathVariable Long id) {
        log.debug("REST request to get RuleGroup : {}", id);
        RuleGroup ruleGroup = ruleGroupRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(ruleGroup));
    }

    /**
     * DELETE  /rule-groups/:id : delete the "id" ruleGroup.
     *
     * @param id the id of the ruleGroup to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/rule-groups/{id}")
    @Timed
    public ResponseEntity<Void> deleteRuleGroup(@PathVariable Long id) {
        log.debug("REST request to delete RuleGroup : {}", id);
        ruleGroupRepository.delete(id);
       // ruleGroupSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    /**
     * Author : Shiva
     */
    /**
     * SEARCH  /_search/rule-groups?query=:query : search for the ruleGroup corresponding
     * to the query.
     *
     * @param query the query of the ruleGroup search 
     * @return the result of the search
     */
//    @GetMapping("/_search/rule-groups")
//    @Timed
//    public ResponseEntity<List<RuleGroup>> searchRuleGroups( HttpServletRequest request,/*@RequestParam String tenantId,@RequestParam(value="columnName",required=false) String columnName, @RequestParam(value="columnValue",required=false) String columnValue,*/ @RequestParam(value="filterValue",required=false) String filterValue, @ApiParam Pageable pageable) {
//    	HashMap map=userJdbcService.getuserInfoFromToken(request);
//    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
//        log.info("Rest api for applying elastic search on rule_group table for the tenant_id: "+ tenantId);
//    	String query = "";
//    	
//    	query = query + " tenantId: \""+tenantId+"\" ";		// Filtering with tenant id
//    	
//
//	   	     if(filterValue != null)
//	   	     {
//	   	        query = query + " AND \""+filterValue+"\"";
//	   	     } 
//    	/*}*/
//        log.info("ElasticSearch Query: "+ query);
//		Page<RuleGroup> page = ruleGroupSearchRepository.search(queryStringQuery(query), pageable);
//		HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/rule-groups");
//		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
///*        Page<RuleGroup> page = ruleGroupSearchRepository.search(queryStringQuery(query), pageable);
//        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/rule-groups");
//        List<RuleGroup> rulegroups = new ArrayList<>();
//        rulegroups = page.getContent();
//        List<RuleGroupDTO> ruleGroupDtoList = new ArrayList<>();
//        ruleGroupDtoList = fetchRuleGroupDTOByRuleGrpList(rulegroups,Long.valueOf(tenantId));
//        return ruleGroupDtoList;*/
//    }
    
//    @GetMapping("/_search/testSearch")
//    @Timed
//    public ResponseEntity<List<RuleGroup>> testSearch(@RequestParam String query,@ApiParam Pageable pageable) {
//    	log.info("Query: "+query);
//        Page<RuleGroup> page = ruleGroupSearchRepository.search(queryStringQuery(query), pageable);
//        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/rule-groups");
//        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
//    }

    
    /*@PostMapping("/postingRuleGrpAndRulesAndRuleConditions")
    @Timed
    public List<ErrorReport> postRulesHierarchy(@RequestBody RuleGroupDTO ruleGroupDTO)
    {
    	log.info("Rest Request to post rule group, rules and ruleConditions");
    	*//** not integrated delete function for conditions
    	 * @throws SQLException 
    	 * @throws ClassNotFoundException 
    	 * @throws IOException **//*
    	Long tenantId = 0L;
    	Long userId = 0L;
    	List<ErrorReport> errorReport=new ArrayList<ErrorReport>();
    	RuleGroup ruleGroup=new RuleGroup();
    	if(ruleGroupDTO.getId() != null)
    	{
    		log.info("group already exists so edit");
    		ruleGroup.setId(ruleGroupDTO.getId());
    	}
    	else
    	{

    	}
    	if(ruleGroupDTO.getName()!=null && !ruleGroupDTO.getName().isEmpty())
    		ruleGroup.setName(ruleGroupDTO.getName());
    	if(ruleGroupDTO.getRulePurpose() != null && !ruleGroupDTO.getRulePurpose().isEmpty())
    		ruleGroup.setRulePurpose(ruleGroupDTO.getRulePurpose());
    	if(ruleGroupDTO.getStartDate()!=null)
    		ruleGroup.setStartDate(ruleGroupDTO.getStartDate());
    	if(ruleGroupDTO.getEndDate()!=null)
    		ruleGroup.setEndDate(ruleGroupDTO.getEndDate());
    	ruleGroup.setEnabledFlag(true);
    	if(ruleGroupDTO != null && ruleGroupDTO.getTenantId() != null)
    	{
    		tenantId = ruleGroupDTO.getTenantId();
    	}
    	if(ruleGroupDTO != null && ruleGroupDTO.getCreatedBy() != null)
    	{
    		userId = ruleGroupDTO.getCreatedBy();
    	}
    	if(ruleGroupDTO.getId() == null || ruleGroupDTO.getId().equals(""))
    	{
    		ruleGroup.setCreatedBy(ruleGroupDTO.getCreatedBy());
    		ruleGroup.setCreationDate(ZonedDateTime.now());
    	}
    	ruleGroup.setLastUpdatedBy(ruleGroupDTO.getCreatedBy());
    	ruleGroup.setLastUpdatedDate(ZonedDateTime.now());
    	ruleGroup.setTenantId(tenantId);

    	RuleGroup newGrp=new RuleGroup();
    	if(ruleGroupDTO.getName()!=null)
    	{
    		newGrp=ruleGroupRepository.save(ruleGroup);
    	}

    	if(newGrp!=null && newGrp.getId()!=null)
    	{

    		ErrorReport ruleGroupSave=new ErrorReport();
    		ruleGroupSave.setTaskName("Rule Group Save");
    		ruleGroupSave.setTaskStatus("Success");
    		ruleGroupSave.setDetails(newGrp.getId()+"");
    		errorReport.add(ruleGroupSave);

    		List<BigInteger> ruleIdsList=ruleGroupDetailsRepository.fetchRuleIdsByGroupAndTenantId(newGrp.getId(),newGrp.getTenantId());
			log.info("ruleIdsList before:"+ruleIdsList);
    		
    		List<RulesDTO> ruleDTO = new ArrayList<RulesDTO>();
    		log.info("ruleGroupDTO.getRules()"+ruleGroupDTO.getRules().size());
    		ruleDTO = ruleGroupDTO.getRules();
    		for(int i=0;i<ruleDTO.size();i++)
    		{
    			Long ruleId = 0L;
    			Rules newRule=new Rules();
    			Rules rule=new Rules();
    			
    			if(ruleDTO.get(i).getRule().getId() != null && ruleDTO.get(i).getRule().getId()!=0)
    			{
    				//rule.setId(ruleDTO.get(i).getRule().getId());
    			
    				if(ruleIdsList.size()>0)
    				{
    					for(int id=0;id<ruleIdsList.size();id++)
    					{
    						log.info("ruleIdsList.get(id) :"+ruleIdsList.get(id));
    						log.info("ruleDTO.get(i).getRule().getId() :"+ruleDTO.get(i).getRule().getId());
    						if(ruleIdsList.get(id).longValue()==ruleDTO.get(i).getRule().getId())
    						{
    							log.info("same");
    							ruleIdsList.remove(ruleIdsList.get(id));
    						}
    					}
    				}
    				log.info("ruleIdsList after:"+ruleIdsList);
    			
    				
    			}
    			else
    			{
    				
    			//}
    				for(int j=0;j<ruleIdsList.size();j++)
    				{
    					log.info("ruleIdsList.get(j).longValue() :"+ruleIdsList.get(j).longValue());
    					log.info("newGrp.getId() :"+newGrp.getId());
    					RuleGroupDetails ruleGrpId=ruleGroupDetailsRepository.findByRuleGroupIdAndRuleId(newGrp.getId(),ruleIdsList.get(j).longValue());
    					log.info("ruleGrpId :"+ruleGrpId);
    					log.info("");
    					if(ruleGrpId!=null)
    					ruleGroupDetailsRepository.delete(ruleGrpId);
    				}
    			log.info("ruleDTO.get(i).getRule() :"+ruleDTO.get(i).getRule());
    				log.info("rule id is null please save");
    				
    				//	rule.setId(ruleDTO.get(i).getId());
    				if(ruleDTO.get(i).getRule().getRuleCode()!=null && !ruleDTO.get(i).getRule().getRuleCode().isEmpty())
    					rule.setRuleCode(ruleDTO.get(i).getRule().getRuleCode());
    				//if(ruleDTO.get(i).getRule().getRuleName()!=null && !ruleDTO.get(i).getRule().getRuleName().isEmpty())
    				//	rule.setRuleName(ruleDTO.get(i).getRule().getRuleName());
    				if(ruleDTO.get(i).getRule().getStartDate()!=null)
    					rule.setStartDate(ruleDTO.get(i).getRule().getStartDate());
    				if(ruleDTO.get(i).getRule().getEndDate()!=null)
    					rule.setEndDate(ruleDTO.get(i).getRule().getEndDate());
    				rule.setEnabledFlag(true);;
    				if(ruleDTO.get(i).getRule().getRuleType()!=null && !ruleDTO.get(i).getRule().getRuleType().isEmpty())
    					rule.setRuleType(ruleDTO.get(i).getRule().getRuleType());
    				//if(ruleDTO.get(i).getRule().getRulePurpose()!=null && !ruleDTO.get(i).getRule().getRulePurpose().isEmpty())
    				//	rule.setRulePurpose(ruleDTO.get(i).getRule().getRulePurpose());
    				rule.setSourceDataViewId(ruleDTO.get(i).getRule().getSourceDataViewId());
    				rule.setTargetDataViewId(ruleDTO.get(i).getRule().getTargetDataViewId());
    				rule.setCreatedBy(userId);
    				rule.setCreationDate(ZonedDateTime.now());
    				rule.setLastUpdatedBy(userId);
    				rule.setLastUpdatedDate(ZonedDateTime.now());

    				//if(ruleDTO.get(i).getRule().getRuleName()!=null && ruleDTO.get(i).getRule().getRuleType()!=null && ruleDTO.get(i).getRule().getRulePurpose()!=null)
    				rule.setCreatedBy(userId);
    				rule.setCreationDate(ZonedDateTime.now());
    				rule.setLastUpdatedBy(userId);
    				rule.setLastUpdatedDate(ZonedDateTime.now());
    				rule.setTenantId(newGrp.getTenantId());
    				newRule=rulesRepository.save(rule);
    		
    			
    			//else
    			//{
    				//ruleId = ruleDTO.get(i).getRule().getId();

    			//}

    			if( newRule.getId()!=null)
    			{
    				ruleId = newRule.getId();
    			}
    			if(ruleId !=null && ruleId !=0 )
    			{
    				log.info("newGrp.getId() :"+newGrp.getId());
    				log.info("ruleId :"+ruleId);
    				//RuleGroupDetails ruleGrpDet=ruleGroupDetailsRepository.findByRuleGroupIdAndRuleId(newGrp.getId(),ruleId);
    				
    				
    				//log.info("ruleGrpDet :"+ruleGrpDet);
    				RuleGroupDetails ruleGrpDetails=new RuleGroupDetails();
    				RuleGroupDetails rulegroupDet=new RuleGroupDetails();
    				rulegroupDet.setRuleGroupId(newGrp.getId());
					rulegroupDet.setRuleId(ruleId);
					rulegroupDet.setPriority(ruleDTO.get(i).getRule().getPriority());
					rulegroupDet.setCreatedBy(userId);
					rulegroupDet.setLastUpdatedBy(userId);
					rulegroupDet.setCreationDate(ZonedDateTime.now());
					rulegroupDet.setLastUpdatedDate(ZonedDateTime.now());
					rulegroupDet.setTenantId(newGrp.getTenantId());
					ruleGrpDetails= ruleGroupDetailsRepository.save(rulegroupDet);
    				//
    				List<RuleConditionsDTO> ruleCondList=ruleDTO.get(i).getRuleConditions();
    						log.info("ruleDTO.get(i).getRuleConditions()"+ruleDTO.get(i).getRuleConditions().size());
    				for(RuleConditionsDTO ruleCond : ruleCondList)
    				{
    					RuleConditions ruleCondition = new RuleConditions();
    					//ruleCondition.setAggregationMethod(ruleCond.getAggregationMethod());
    					if(ruleCond.getId()!= null && ruleCond.getId() != 0)
    					{
    						//ruleCondition.setId(ruleCond.getId());
    					}
    					else if( ruleCond.getId() == 0)
    					{
    						
    					//}
    					if(ruleCond.getCloseBracket()!=null && !ruleCond.getCloseBracket().isEmpty())
    						ruleCondition.setCloseBracket(ruleCond.getCloseBracket());
    					if(ruleCond.getLogicalOperator()!=null && !ruleCond.getLogicalOperator().isEmpty())
    						ruleCondition.setLogicalOperator(ruleCond.getLogicalOperator());
    					if(ruleCond.getOpenBracket()!=null && !ruleCond.getOpenBracket().isEmpty())
    						ruleCondition.setOpenBracket(ruleCond.getOpenBracket());
    					if(ruleCond.getsOperator()!=null && !ruleCond.getsOperator().isEmpty())
    						ruleCondition.setsOperator(ruleCond.getsOperator());
    					if(ruleCond.gettOperator()!=null && !ruleCond.gettOperator().isEmpty())
    						ruleCondition.settOperator(ruleCond.gettOperator());
    					if(ruleCond.getsColumnId()!=null )
    						ruleCondition.setsColumnId(ruleCond.getsColumnId());
    					//ruleCondition.setsColumnType(ruleCond.getsColumnType());
    					//ruleCondition.setsDataViewId(ruleCond.getsDataViewId());
    					if(ruleCond.getsFormula()!=null && !ruleCond.getsFormula().isEmpty())
    						ruleCondition.setsFormula(ruleCond.getsFormula());
    					if(ruleCond.getsMany()!=null)
    						ruleCondition.setsMany(ruleCond.getsMany());
    					if(ruleCond.gettToleranceOperatorFrom()!=null && !ruleCond.gettToleranceOperatorFrom().isEmpty())
    						ruleCondition.setsToleranceOperatorFrom(ruleCond.gettToleranceOperatorFrom());
    					if(ruleCond.gettToleranceOperatorTo()!=null && !ruleCond.gettToleranceOperatorTo().isEmpty())
    						ruleCondition.setsToleranceOperatorTo(ruleCond.gettToleranceOperatorTo());
    					if(ruleCond.getsToleranceType()!=null && !ruleCond.getsToleranceType().isEmpty())
    						ruleCondition.setsToleranceType(ruleCond.getsToleranceType());
    					if(ruleCond.getsToleranceValueFrom()!=null && !ruleCond.getsToleranceValueFrom().isEmpty())
    						ruleCondition.setsToleranceValueFrom(ruleCond.getsToleranceValueFrom());
    					if(ruleCond.getsToleranceValueTo()!=null && !ruleCond.getsToleranceValueTo().isEmpty())
    						ruleCondition.setsToleranceValueTo(ruleCond.getsToleranceValueTo());
    					if(ruleCond.gettColumnId()!=null)
    						ruleCondition.settColumnId(ruleCond.gettColumnId());
    					//ruleCondition.settColumnType(ruleCond.gettColumnType());
    					//ruleCondition.settDataViewId(ruleCond.gettDataViewId());
    					if(ruleCond.gettFormula()!=null && !ruleCond.gettFormula().isEmpty())
    						ruleCondition.settFormula(ruleCond.gettFormula());
    					if(ruleCond.gettMany()!=null)
    						ruleCondition.settMany(ruleCond.gettMany());
    					if(ruleCond.gettToleranceOperatorFrom()!=null && !ruleCond.gettToleranceOperatorFrom().isEmpty())
    						ruleCondition.settToleranceOperatorFrom(ruleCond.gettToleranceOperatorFrom());
    					if(ruleCond.gettToleranceOperatorTo()!=null && !ruleCond.gettToleranceOperatorTo().isEmpty())
    						ruleCondition.settToleranceOperatorTo(ruleCond.gettToleranceOperatorTo());
    					if(ruleCond.gettToleranceType()!=null && !ruleCond.gettToleranceType().isEmpty())
    						ruleCondition.settToleranceType(ruleCond.gettToleranceType());
    					if(ruleCond.gettToleranceValueFrom()!=null && !ruleCond.gettToleranceValueFrom().isEmpty())
    						ruleCondition.settToleranceValueFrom(ruleCond.gettToleranceValueFrom());
    					if(ruleCond.gettToleranceValueTo()!=null && !ruleCond.gettToleranceValueTo().isEmpty())
    						ruleCondition.settToleranceValueTo(ruleCond.gettToleranceValueTo());
    					if(ruleCond.getsValue()!=null && !ruleCond.getsValue().isEmpty())
    						ruleCondition.setsValue(ruleCond.getsValue());
    					if(ruleCond.getsValueType()!=null && !ruleCond.getsValueType().isEmpty())
    						ruleCondition.setsValueType(ruleCond.getsValueType());
    					if(ruleCond.gettValue()!=null && !ruleCond.gettValue().isEmpty())
    						ruleCondition.settValue(ruleCond.gettValue());
    					if(ruleCond.gettValueType()!=null && !ruleCond.gettValueType().isEmpty())
    						ruleCondition.settValueType(ruleCond.gettValueType());
    					if(ruleId!=null)
    						ruleCondition.setRuleId(ruleId);
    					if(ruleCond.getValue()!=null && !ruleCond.getValue().isEmpty())
    					ruleCondition.setValue(ruleCond.getValue());
    					if(ruleCond.getOperator()!=null && !ruleCond.getOperator().isEmpty())
    					ruleCondition.setOperator(ruleCond.getOperator());
    					ruleCondition.setCreatedBy(userId);
    					ruleCondition.setCreationDate(ZonedDateTime.now());
    					ruleCondition.setLastUpdatedBy(userId);
    					ruleCondition.setLastUpdatedDate(ZonedDateTime.now());
    					log.info("save condition"+ruleCondition);
    					RuleConditions newRuleCond=ruleConditionsRepository.save(ruleCondition);

    					if(newRuleCond!=null && newRuleCond.getId()!=null)
    					{

    					}
    					else
    					{
    						log.info("failed saving condition");
    						ErrorReport ruleConditionSave=new ErrorReport();
    						ruleConditionSave.setTaskName("Rule Condition Save");
    						ruleConditionSave.setTaskStatus("failure");
    						errorReport.add(ruleConditionSave);
    					}
    				}
    			}
    				//RuleGroupDetails ruleGrpDetails= ruleGroupDetailsRepository.save(rulegroupDet);
    				if(ruleGrpDetails!=null && ruleGrpDetails.getId()!=null)
    				{

    				}
    				else
    				{
    					ErrorReport ruleGrpDetailsSave=new ErrorReport();
    					ruleGrpDetailsSave.setTaskName("Rule Group Details Save");
    					ruleGrpDetailsSave.setTaskStatus("failure");
    					errorReport.add(ruleGrpDetailsSave);
    				}
    			}
    			else
    			{
    				log.info("ruleId issss"+ruleId);
    			}
    		}
    	}
    	}
    	else
    	{
    		ErrorReport ruleGroupSave=new ErrorReport();
    		ruleGroupSave.setTaskName("Rule Group Save");
    		ruleGroupSave.setTaskStatus("failure");
    		errorReport.add(ruleGroupSave);
    	}
    	return errorReport;
    }*/
    @Transactional(propagation= Propagation.REQUIRED)
    @PostMapping("/testRollBack")
    @Timed
    public void testRollBack(HttpServletRequest request,@RequestBody RuleGroupDTO ruleGroupDTO) throws Exception
    {
    	log.info("Test roll back");
    	RuleGroup rg = new RuleGroup();
    	rg.setName("xxx");
    	rg.setRulePurpose("test");
    	ruleGroupRepository.save(rg);
    	try{
    		ruleService.saveRule();
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
    @SuppressWarnings("finally")
	@PostMapping("/bulkUploadReconcileRules")
    @Timed
    //@Transactional(propagation = Propagation.REQUIRED,noRollbackFor = Exception.class)
    public List<ErrorReport> bulkUploadReconcileRules(@RequestParam("file") MultipartFile file,HttpServletRequest request,HttpServletResponse response,@RequestParam String reconProcesses) throws Exception
    {
    	List<ErrorReport>  errorReports=new ArrayList<ErrorReport>();
    	ObjectMapper objMapper = new ObjectMapper();
		TypeFactory typeFact = objMapper.getTypeFactory();
		List<RuleGroupDTO> reconcileProcessesList = objMapper.readValue(reconProcesses, typeFact.constructCollectionType(List.class,RuleGroupDTO.class));
		for(int i=0;i<reconcileProcessesList.size();i++)
		{
			RuleGroupDTO ruleGroupDTO = new RuleGroupDTO();
			ruleGroupDTO = reconcileProcessesList.get(i);
			ErrorReport errorReport =new ErrorReport();

			try {

				errorReport = ruleGroupService.postRulegroup(ruleGroupDTO,request,"true");

			} catch (Exception e) {
				ErrorReport errorRep =new ErrorReport();
				if(ruleGroupDTO.getName() != null)
				errorRep.setTaskName("Saving recon process : "+ruleGroupDTO.getName());
				else
					errorRep.setTaskName("Saving recon process number "+(i+1));	
				errorRep.setTaskStatus("Failed");
				errorRep.setDetails(e.getMessage());
				errorReport = (errorRep);
			}
			finally{
				errorReports.add(errorReport);
			}
		}
    	return errorReports;
    }
    
    
    
    
    @SuppressWarnings("finally")
	@PostMapping("/postingRuleGrpAndRulesAndRuleConditions")
    @Timed
    //@Transactional(propagation = Propagation.REQUIRED,noRollbackFor = Exception.class)
    public ErrorReport postRulesHierarchy(HttpServletRequest request,HttpServletResponse response,@RequestBody RuleGroupDTO ruleGroupDTO,@RequestParam (required =false) String bulkUpload) throws Exception
    {
    	log.info("Rest Request to post rule group, rules and ruleConditions");
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long userId=Long.parseLong(map.get("userId").toString());
    	Long tenantId= Long.parseLong(map.get("tenantId").toString());
    	ErrorReport errorReport=new ErrorReport();
    	if(ruleGroupDTO.isAdhocRuleCreation())
    	{
    		log.info("inside if with orphan");
    		RuleGroup ruleGrp=ruleGroupRepository.findByIdForDisplayAndTenantId(ruleGroupDTO.getId(), tenantId);
    		if(ruleGrp!=null && ruleGrp.getId()!=null)
    		{
    			//Kiran
    			try {
    				List<ErrorReport> rulesSaveReports = new ArrayList<ErrorReport> ();
    				rulesSaveReports = ruleService.postRules(ruleGroupDTO.getRules(), ruleGrp,bulkUpload);
    				errorReport.setTaskName("Adhoc rule creation");
    				errorReport.setSubTasksList(rulesSaveReports);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			//As adhoc rule will be only one 
    			//check if the got rule list has adhoc rule
    			//if yes=> call next process after creation
    			
    		}
    		else
    		{
    			try {
    				errorReport = ruleGroupService.postRulegroup(ruleGroupDTO,request,bulkUpload);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					ErrorReport failedReport = new ErrorReport();
					failedReport.setTaskName("Process save failed");
					failedReport.setTaskStatus("Failed");
					failedReport.setDetails(e.getMessage());
					errorReport = failedReport;
					e.printStackTrace();
				}
    		}
    	}
    	else
    	{
    		log.info("in else bcz orphan removed");
    			try {
    				errorReport = ruleGroupService.postRulegroup(ruleGroupDTO,request,bulkUpload);
				} catch (Exception e) {
	    			ErrorReport errorRep =new ErrorReport();
	    			errorRep.setTaskName("Saving rule group");
	    			errorRep.setTaskStatus("Failed");
	    			errorRep.setDetails(e.getMessage());
	    			errorReport = (errorRep);
	    			return errorReport;
    		}
    	}
    	return errorReport;

    }
    
    @GetMapping("/getRuleGroupDetailsObject")
	@Timed
   public RuleGroupDTO getRuleGroupDetails(@RequestParam String groupId,HttpServletRequest request)
    {
    	RuleGroupDTO ruleGroupDto=new RuleGroupDTO();
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long userId=Long.parseLong(map.get("userId").toString());
    	Long tenantId= Long.parseLong(map.get("tenantId").toString());
    	log.info("tenantId :"+tenantId);
    	RuleGroup ruleGroup = ruleGroupRepository.findByIdForDisplayAndTenantId(groupId, tenantId);
    	if(ruleGroup!=null)
    	{

    		if(ruleGroup.getId()!=null )
    			ruleGroupDto.setId(ruleGroup.getIdForDisplay());
    		ruleGroupDto.setTenantId(ruleGroup.getTenantId());
    		if(ruleGroup.getName()!=null && !ruleGroup.getName().isEmpty())
    			ruleGroupDto.setName(ruleGroup.getName());
    			//ruleGroupDto.setApprRuleGrpId(ruleGroup.getApprRuleGrpId());
    			RuleGroup rg = new RuleGroup();
    			rg = ruleGroupRepository.findTaggedGroup(ruleGroup.getId());
    			if(rg != null && rg.getIdForDisplay() != null)
    			{
    				ruleGroupDto.setApprRuleGrpId(rg.getIdForDisplay());
    			}
    		if(ruleGroup.getRulePurpose() != null && !ruleGroup.getRulePurpose().isEmpty())
    		{
    			ruleGroupDto.setRulePurpose(ruleGroup.getRulePurpose());
    			LookUpCode lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("RULE_GROUP_TYPE",ruleGroup.getRulePurpose(), ruleGroup.getTenantId());
    			if(lookUpCode != null &&  lookUpCode.getMeaning() != null)
    				ruleGroupDto.setMeaning(lookUpCode.getMeaning());
    			if(ruleGroup.getRulePurpose().equalsIgnoreCase("ACCOUNTING"))
    			{
    				//set coa from any rule
    				List<RuleGroupDetails> taggedRules = ruleGroupDetailsRepository.findByRuleGroupId(ruleGroup.getId());
    				if(taggedRules.size()>0)
    				{
    					Rules rule = rulesRepository.findById(taggedRules.get(0).getRuleId());
    					ChartOfAccount coa = chartOfAccountRepository.findOne(Long.valueOf(rule.getcOA()));
    					if(coa != null && coa.getName() != null)
    					{
    						ruleGroupDto.setCoa(coa.getIdForDisplay());
    					}
    				}
    				
    			}
    		}
    		if(ruleGroup.getReconciliationGroupId() != null)
    		{
    			
    			ruleGroupDto.setActivityBased(true);
    			RuleGroup reconRuleGroup = new RuleGroup();
    			reconRuleGroup = ruleGroupRepository.findOne(ruleGroup.getReconciliationGroupId());
    			if(reconRuleGroup != null && reconRuleGroup.getName() != null)
    			{
    				ruleGroupDto.setReconciliationGroupName(reconRuleGroup.getName() );
    				ruleGroupDto.setReconciliationGroupId(reconRuleGroup.getIdForDisplay());
    			}
    		}
    		if(ruleGroup.getActivityBased() != null)
    			ruleGroupDto.setActivityBased(ruleGroup.getActivityBased());
    		if(ruleGroup.getCrossCurrency() != null)
    			ruleGroupDto.setCrossCurrency(ruleGroup.getCrossCurrency());
    		if(ruleGroup.getMultiCurrency() != null)
    			ruleGroupDto.setMultiCurrency(ruleGroup.getMultiCurrency());

    		if(ruleGroup.getControlAccount() != null)
    			ruleGroupDto.setControlAccount(ruleGroup.getControlAccount());
    		if(ruleGroup.getRealizedGainLossAccount() != null)
    			ruleGroupDto.setRealizedGainLossAccount(ruleGroup.getRealizedGainLossAccount());
    		if(ruleGroup.getFxGainAccount() != null)
    			ruleGroupDto.setFxGainAccount(ruleGroup.getFxGainAccount());
    		if(ruleGroup.getFxLossAccount() != null)
    			ruleGroupDto.setFxLossAccount(ruleGroup.getFxLossAccount());

    		if(ruleGroup.getConversionDate() != null)
    		{
    			ruleGroupDto.setConversionDate(ruleGroup.getConversionDate());
    			LookUpCode conversionDateCode = new LookUpCode();
    			conversionDateCode = lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("CONV_DATE_RS_LOV", ruleGroup.getConversionDate(), ruleGroup.getTenantId());
    			if(conversionDateCode != null && conversionDateCode.getMeaning() != null)
    				ruleGroupDto.setConversionDateMeaning( conversionDateCode.getMeaning() );
    		}

    		if(ruleGroup.getFxRateId() != null)
    		{
    			ruleGroupDto.setFxRateId(ruleGroup.getFxRateId());
    			FxRates fxRate = new FxRates();
    			fxRate = fxRatesRepository.findOne(ruleGroup.getFxRateId());
    			if(fxRate != null && fxRate.getName() != null)
    				ruleGroupDto.setFxRateName( fxRate.getName());
    		}

    		RuleGroup taggedRG = new RuleGroup();
    		taggedRG = ruleGroupRepository.findTaggedGroup(ruleGroup.getId());
    		
    		if(taggedRG != null)
    		{
    			log.info("tagged group  found"+taggedRG.getId());
    			ruleGroupDto.setApprRuleGrpName(taggedRG.getName());
    			if(taggedRG.getRulePurpose().toLowerCase().contains("recon"))
				{
    				ruleGroupDto.setConfiguredModuleName("Reconciliation");
    				ruleGroupDto.setConfiguredModuleId("RECONCILIATION");
				}else if(taggedRG.getRulePurpose().toLowerCase().contains("accounting"))
				{
					ruleGroupDto.setConfiguredModuleName("Accounting");
					ruleGroupDto.setConfiguredModuleId("ACCOUNTING");
				}
    			//ruleGroupDto.setReconciliationGroupId(taggedRG.getIdForDisplay());
				//ruleGroupDto.setReconciliationGroupName(taggedRG.getName());
    		}
    		
    		else
    		{
    			log.info("tagged group not found");
    		}
    		if(ruleGroup.getStartDate()!=null)
    			ruleGroupDto.setStartDate(ruleGroup.getStartDate());
    		if(ruleGroup.getAccountingType()!=null)
    			ruleGroupDto.setAccountingTypeCode(ruleGroup.getAccountingType());
    		if(ruleGroup.getEndDate()!=null)
    			ruleGroupDto.setEndDate(ruleGroup.getEndDate());
    		if(ruleGroup.isEnabledFlag()!=null)
    			ruleGroupDto.setEnableFlag(ruleGroup.isEnabledFlag());
    		if(ruleGroup.getCreatedBy()!=null)
    			ruleGroupDto.setCreatedBy(ruleGroup.getCreatedBy());
    		if(ruleGroup.getCreationDate()!=null)
    			ruleGroupDto.setCreationDate(ruleGroup.getCreationDate());


    	}
    	return ruleGroupDto;
    }

	@GetMapping("/getRuleGrpAndRuleConditionsAndRuleGrpDetails")
    @Timed
//    public RuleGroupDTO getRuleDetails(@RequestParam Long groupId)
      public RuleGroupDTO getRuleDetails(HttpServletRequest request,@RequestParam String groupId)
	{
		log.info("Rest request to get rule details"+groupId);
		List<DataViews> dataViewsForTenant = new ArrayList<DataViews>();
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		
		RuleGroup ruleGroup = ruleGroupRepository.findByIdForDisplayAndTenantId(groupId,tenantId);
		
		Long ruleGroupId =ruleGroup.getId();
		RuleGroupDTO ruleGroupDto=new RuleGroupDTO();
		ruleGroupDto.setId(groupId);
		ruleGroupDto.setName(ruleGroup.getName());
		ruleGroupDto.setStartDate(ruleGroup.getStartDate());
		ruleGroupDto.setEndDate(ruleGroup.getEndDate());
		if(ruleGroup.getRulePurpose() != null && !ruleGroup.getRulePurpose().isEmpty())
    	{
    		ruleGroupDto.setRulePurpose(ruleGroup.getRulePurpose());
    		LookUpCode lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("RULE_GROUP_TYPE",ruleGroup.getRulePurpose(), ruleGroup.getTenantId());
    		if(lookUpCode != null &&  lookUpCode.getMeaning() != null)
    			ruleGroupDto.setMeaning(lookUpCode.getMeaning());;
    	}
		/**
		 * Data views map
		 */
		//dataViewsForTenant = dataViewsRepository.fetchActiveDataViews(ruleGroup.getTenantId());
		
		//log.info(""+dataViewsForTenant.size());
		
		//log.info("dataViewsIdtoNameMap size"+dataViewsIdtoNameMap.size());
		//log.info("dataViewsIdtoNameMap key set"+dataViewsIdtoNameMap.keySet());

		/**       
		 * Column names map
		 */
		//List<DataViewsColumns> dataViewColumns = new ArrayList<DataViewsColumns>();
		//HashMap<Long, DataViewsColumns> dataViewColumnsMap = new HashMap<Long, DataViewsColumns>();
		//dataViewColumns = dataViewsColumnsRepository.findByDataViewIdIn(dataViewIds);
		//log.info("dataViewIds"+dataViewIds+"dataViewColumns.size() size"+dataViewColumns.size());
		//for(int k=0; k<dataViewColumns.size();k++)
		//{
		//	dataViewColumnsMap.put(dataViewColumns.get(k).getId(),dataViewColumns.get(k) );
		//	log.info("dataViewColumns.get(k).getId()"+dataViewColumns.get(k).getId());
		//	log.info("dataViewColumns.get(k).getColumnName()"+dataViewColumns.get(k).getColumnName());

		//}

		List<RuleGroupDetails> ruleGrpDetailsList=ruleGroupDetailsRepository.findByRuleGroupIdOrderByPriorityAsc(ruleGroupId);
		Set<Long> dataViewIdFromRule = new HashSet<Long>();
		//fetch data views list of of which only rules are tagged to
		HashMap<Long, List<DataViewsColumns>> dataViewToColsMap = new HashMap<Long, List<DataViewsColumns>>();
		//HashMap<Long, DataViewsColumns> dataViewColumnsMap = new HashMap<Long, DataViewsColumns>();
		for(int i=0;i<ruleGrpDetailsList.size();i++)
		{
			Rules rule = new Rules();
			rule = rulesRepository.findOne(ruleGrpDetailsList.get(i).getRuleId());
			dataViewIdFromRule.add(rule.getSourceDataViewId());
			dataViewIdFromRule.add(rule.getTargetDataViewId());
		}
		log.info("dataViewIdFromRule"+dataViewIdFromRule);
		/*for(Long l : dataViewIdFromRule)
		{
			//get columns
			List<DataViewsColumns> cols = new ArrayList<DataViewsColumns>();
			cols = dataViewsColumnsRepository.findByDataViewId(l);
			dataViewToColsMap.put(l,cols);
			for(DataViewsColumns dvCol : cols)
			{
				dataViewColumnsMap.put(dvCol.getId(),dvCol);
			}
		}*/
		dataViewsForTenant = dataViewsRepository.findByIdIn(dataViewIdFromRule);
		HashMap<Long, DataViews> dataViewsIdtoNameMap = new HashMap<Long, DataViews>();
		for(int k = 0; k<dataViewsForTenant.size();k++)
		{
			//String[] dataViewNameAndDisplayName = new String[2];
			//dataViewNameAndDisplayName[0]  = dataViewsForTenant.get(k).getDataViewName();
			//dataViewNameAndDisplayName[1] =  dataViewsForTenant.get(k).getDataViewDispName();
			dataViewsIdtoNameMap.put(dataViewsForTenant.get(k).getId(),dataViewsForTenant.get(k));
		}
		
		//dataViewColumnsMap = dataViewsColumnsRepository.findByDataViewId(dataViewId)
		List<RulesDTO> ruleDtoList=new ArrayList<RulesDTO>();
		if(ruleGrpDetailsList!=null && ruleGrpDetailsList.size()>0 && (!ruleGroup.getRulePurpose().equals("APPROVALS")) )
		{
			for(int i=0;i<ruleGrpDetailsList.size();i++)
			{
				Rules rule=rulesRepository.findOne(ruleGrpDetailsList.get(i).getRuleId());

				if(rule!=null)
				{
					RulesDTO rulesDto=new RulesDTO();

					RuleDTO ruleDTO = new RuleDTO() ;
					log.info("rule :"+rule);
					
					// setting rule group Details Id
					RuleGroupDetails ruleGrpDetails=ruleGroupDetailsRepository.findByRuleGroupIdAndRuleId(ruleGroupId, rule.getId());
							if(ruleGrpDetails!=null)
								ruleDTO.setRuleGroupAssignId(ruleGrpDetails.getId());
					
					ruleDTO.setPriority(ruleGrpDetailsList.get(i).getPriority());
					ruleDTO.setId(rule.getId());
					ruleDTO.setTenantId(rule.getTenantId());
					if(rule.getRuleCode()!=null && !rule.getRuleCode().isEmpty())
						ruleDTO.setRuleCode(rule.getRuleCode());
					if(rule.getStartDate()!=null)
						ruleDTO.setStartDate(rule.getStartDate());
					if(rule.getEndDate()!=null)
						ruleDTO.setEndDate(rule.getEndDate());
					if(rule.isEnabledFlag()!=null)
					{
						/** active check**/

		    			Boolean activeStatus=activeStatusService.activeStatus(rule.getStartDate(), rule.getEndDate(), rule.isEnabledFlag());
		    			ruleDTO.setEnabledFlag(activeStatus);
		    			/** active check end**/
						
						
					}
					ruleDTO.setAssignmentFlag(ruleGrpDetailsList.get(i).isEnabledFlag());
					if(rule.getCategory()!=null)
					ruleDTO.setCategory(rule.getCategory());
					
					ruleDTO.setSuggestionFlag(ruleGrpDetails.getSuggestionRule());
										
					if(rule.getRuleType()!=null && !rule.getRuleType().isEmpty())
		    			{
						ruleDTO.setRuleType(rule.getRuleType());
		    				LookUpCode lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("RULE_TYPE",rule.getRuleType(), rule.getTenantId());
	    					if(lookUpCode != null &&  lookUpCode.getMeaning() != null)
	    						ruleDTO.setRuleTypeMeaning( lookUpCode.getDescription());
		    			}
						
					if(rule.getCreatedBy()!=null)
						ruleDTO.setCreatedBy(rule.getCreatedBy());
					if(rule.getLastUpdatedBy()!=null)
						ruleDTO.setLastUpdatedBy(rule.getLastUpdatedBy());
					if(rule.getCreationDate()!=null)
						ruleDTO.setCreationDate(rule.getCreationDate());
					if(rule.getLastUpdatedDate()!=null)
						ruleDTO.setLastUpdatedDate(rule.getLastUpdatedDate());
					if(rule.getSourceDataViewId()!=null)
					{
						//Kiran
						//DataViews tDataView=dataViewsRepository.findOne(rule.getSourceDataViewId());
						if(dataViewsIdtoNameMap.size() >0 && dataViewsIdtoNameMap.get(rule.getSourceDataViewId()) != null)
						{
							
							DataViews view = dataViewsIdtoNameMap.get(rule.getSourceDataViewId());
							view = dataViewsIdtoNameMap.get(rule.getSourceDataViewId());
							if(view != null)
							{
								if(view.getIdForDisplay() != null)
								ruleDTO.setSourceDataViewId(view.getIdForDisplay());
								
								if(view.getDataViewDispName() != null)
								ruleDTO.setsDataViewDisplayName(view.getDataViewDispName());
								
								if(view.getDataViewName() != null)
									ruleDTO.setsDataViewName(view.getDataViewName());
							}
						}
					}
					if(rule.getTargetDataViewId()!=null)
					{

//						ruleDTO.setTargetDataViewId(rule.getTargetDataViewId());
						if(dataViewsIdtoNameMap.size() >0 && dataViewsIdtoNameMap.get(rule.getSourceDataViewId()) != null)
						{
							//String[] view = new String[2];
							DataViews view = dataViewsIdtoNameMap.get(rule.getTargetDataViewId());
							if(view != null && view.getDataViewDispName() != null)
							{
								ruleDTO.settDataViewDisplayName(view.getDataViewDispName());
							}
							if(view != null && view.getDataViewName() != null )
							{
								ruleDTO.settDataViewName(view.getDataViewName());
							}
							if(view != null && view.getIdForDisplay() != null )
							{
								ruleDTO.setTargetDataViewId(view.getIdForDisplay());
							}
						}
						/*DataViews tDataView=dataViewsRepository.findOne(rule.getTargetDataViewId());
						ruleDTO.setTargetDataViewId(tDataView.getIdForDisplay());
						if(tDataView!=null && tDataView.getDataViewName()!=null && !tDataView.getDataViewName().isEmpty())
							ruleDTO.settDataViewName(tDataView.getDataViewName());
						if(tDataView!=null && tDataView.getDataViewDispName()!=null && !tDataView.getDataViewDispName().isEmpty())
							ruleDTO.settDataViewDisplayName(tDataView.getDataViewDispName());*/
					}
					/**************************************************************/
	    			//Rule Edit based on accounting process initiation status
	    			Object count = reconciliationResultRepository.fetchRecordCountByGroupIdAndRuleId(ruleGroupId,rule.getId());
	    			log.info("count has value:"+count);
	    			if(count != null && !count.equals(0))
	    			{
	    				Long reconCount = 0L;
	    				reconCount = Long.valueOf(count.toString());
	    				if(reconCount >0)
	    				{
	    					ruleDTO.setEditRule(false);
	    				}
	    				else
	    					ruleDTO.setEditRule(true);
	    			}
	    			/**************************************************************/

					rulesDto.setRule(ruleDTO);

					//ruleDto.setRuleDTO(rule);
					//ruleGroupDto.setRuleGroupType(rule.getRulePurpose());
					//.setId(rule.getId());
					/*if(rule.getRuleCode()!=null && !rule.getRuleCode().isEmpty())
    					ruleDto.setRuleCode(rule.getRuleCode());
    				if(rule.getRuleName()!=null && !rule.getRuleName().isEmpty())
    					ruleDto.setRuleName(rule.getRuleName());
    				if(rule.getStartDate()!=null)
    					ruleDto.setStartDate(rule.getStartDate());
    				if(rule.getEndDate()!=null)
    					ruleDto.setEndDate(rule.getEndDate());
    				if(rule.isEnabledFlag()!=null)
    					ruleDto.setEnableFlag(rule.isEnabledFlag());
    				if(rule.getRuleType()!=null && !rule.getRuleType().isEmpty())
    					ruleDto.setRuleType(rule.getRuleType());
    				if(rule.getRulePurpose()!=null && !rule.getRulePurpose().isEmpty())
    					ruleDto.setRulePurpose(rule.getRulePurpose());
					 */
					
					
	    			
					List<RuleConditions> ruleCondList = ruleConditionsRepository.findByRuleId(rule.getId());
					if(ruleCondList.size()>0)
					{
						List<RuleConditionsDTO> ruleConditionsdtoList = new ArrayList<RuleConditionsDTO>();
						for(int j = 0;j<ruleCondList.size() ;j++)
						{
							RuleConditions ruleCondition = new RuleConditions();
							ruleCondition = ruleCondList.get(j);
							RuleConditionsDTO ruleCondDTO = new RuleConditionsDTO();
							//ruleCondDTO.setAggregationMethod(ruleCondition.getAggregationMethod());
							ruleCondDTO.setSequenceNo(j+1);
							if(ruleCondition.getCloseBracket()!=null && !ruleCondition.getCloseBracket().isEmpty())
								ruleCondDTO.setCloseBracket(ruleCondition.getCloseBracket());
							if(ruleCondition.getCreatedBy()!=null)
								ruleCondDTO.setCreatedBy(ruleCondition.getCreatedBy());
							if(ruleCondition.getCreationDate()!=null)
								ruleCondDTO.setCreationDate(ruleCondition.getCreationDate());
							if(ruleCondition.getId()!=null)
								ruleCondDTO.setId(ruleCondition.getId());
							if(ruleCondition.getLastUpdatedBy()!=null)
								ruleCondDTO.setLastUpdatedBy(ruleCondition.getLastUpdatedBy());
							if(ruleCondition.getLastUpdatedDate()!=null)
								ruleCondDTO.setLastUpdatedDate(ruleCondition.getLastUpdatedDate());
							if(ruleCondition.getLogicalOperator()!=null && !ruleCondition.getLogicalOperator().isEmpty())
							{
								ruleCondDTO.setLogicalOperator(ruleCondition.getLogicalOperator());
								LookUpCode codeForLogicalOperator = new LookUpCode();
								codeForLogicalOperator = lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("LOGICAL_OPERATOR",ruleCondition.getLogicalOperator(), rule.getTenantId());
								if(codeForLogicalOperator != null && codeForLogicalOperator.getMeaning() != null)
								ruleCondDTO.setLogicalOperatorMeaning(codeForLogicalOperator.getMeaning());
							}
							if(ruleCondition.getOpenBracket()!=null && !ruleCondition.getOpenBracket().isEmpty())
								ruleCondDTO.setOpenBracket(ruleCondition.getOpenBracket());
							
							if(ruleCondition.getsOperator()!=null && !ruleCondition.getsOperator().isEmpty())
								ruleCondDTO.setsOperator(ruleCondition.getsOperator());
							
							if(ruleCondition.gettOperator()!=null && !ruleCondition.gettOperator().isEmpty())
								ruleCondDTO.settOperator(ruleCondition.gettOperator());
							
							if(ruleCondition.issReconAmountMatch() != null && ruleCondition.issReconAmountMatch() == true)
							{
								log.info("sreconmatch for "+(j+1)+" is "+ruleCondition.issReconAmountMatch());
								ruleCondDTO.setsReconAmountMatch(ruleCondition.issReconAmountMatch());
								ruleCondDTO.setAmountQualifier(true);
								rulesDto.setHasAmountQualifier(true);
							}
							else
							{
								log.info("s reconmatch is null");
								ruleCondDTO.setAmountQualifier(false);
								//rulesDto.setHasAmountQualifier(false);
							}
								
							
							if(ruleCondition.istReconAmountMatch() != null && ruleCondition.istReconAmountMatch() == true)
							{
								log.info("treconmatch for "+(j+1)+" is "+ruleCondition.istReconAmountMatch());
								ruleCondDTO.settReconAmountMatch(ruleCondition.istReconAmountMatch());
								ruleCondDTO.setAmountQualifier(true);
								rulesDto.setHasAmountQualifier(true);
							}
							
							if(ruleCondition.getRuleId()!=null)
								ruleCondDTO.setRuleId(ruleCondition.getRuleId());
							
							if(ruleCondition.getsColumnId()!=null)
							{
								ruleCondDTO.setsColumnId(ruleCondition.getsColumnId());
								DataViewsColumns dvColumns = new DataViewsColumns();
								dvColumns = dataViewsColumnsRepository.findOne(ruleCondition.getsColumnId());
								if(dvColumns!=null)
								{
									//dvColumns =dataViewColumnsMap.get(ruleCondition.getsColumnId());
									if(dvColumns != null && dvColumns.getColDataType() != null)
									{
										ruleCondDTO.setDataType(dvColumns.getColDataType());
									}
								}
							}
							if(ruleDTO.getSourceDataViewId() != null)
							{
								//List<HashMap> sColList = new ArrayList<HashMap>();
								
								//DataViews DV=dataViewsRepository.findByTenantIdAndIdForDisplay(ruleGroup.getTenantId(), ruleDTO.getSourceDataViewId());
								
								//sColList = dataViewsService.fetchDataViewAndColumnsByDvId(DV.getId());
								if(dataViewToColsMap.size()>0 && dataViewToColsMap.get(ruleDTO.getSourceDataViewId() ) != null)
								{
									ruleCondDTO.setsColumnList(dataViewToColsMap.get(ruleDTO.getSourceDataViewId() ));
								}
							
								
							}
							//ruleCondDTO.setsDataViewId(ruleCondition.getsDataViewId());
							/*String[] sdataViewDetails = new String[2];
    						sdataViewDetails = dataViewsIdtoNameMap.get(ruleCondDTO.getsDataViewId());

    						if(sdataViewDetails != null)
    						{
    							log.info("sdataViewDetails:"+sdataViewDetails[0]+sdataViewDetails[1]);
    							ruleCondDTO.setsDataviewName(sdataViewDetails[0]);
        						ruleCondDTO.setsDataviewDisplayName(sdataViewDetails[1]);
    						}*/
							//log.info("ruleCondition id:"+ruleCondition.getId());
							//log.info("ruleCondDTO.getsColumnId()"+ruleCondition.getsColumnId());
							//log.info("dataViewColumnsMap.get(ruleCondDTO.getsColumnId())"+dataViewColumnsMap.get(ruleCondition.getsColumnId()));
							//log.info("ruleCondDTO.gettColumnId()"+ruleCondition.gettColumnId());
							//log.info("dataViewColumnsMap.get(ruleCondDTO.gettColumnId())"+dataViewColumnsMap.get(ruleCondition.gettColumnId()));
							//if(dataViewColumnsMap.get(ruleCondition.getsColumnId())!=null && !dataViewColumnsMap.get(ruleCondition.getsColumnId()).getColumnName().isEmpty())
								//ruleCondDTO.setsColumnName(dataViewColumnsMap.get(ruleCondition.getsColumnId()).getColumnName());
							if(ruleCondition.getsColumnId() != null)
							{
								DataViewsColumns dvCol = new DataViewsColumns();
								dvCol = dataViewsColumnsRepository.findOne(ruleCondition.getsColumnId());
								if(dvCol != null)
								ruleCondDTO.setsColumnName(dvCol.getColumnName());
							}
							
							//ruleCondDTO.setsColumnType(ruleCondition.getsColumnType());
							if(ruleCondition.gettColumnId()!=null)
							{
								ruleCondDTO.settColumnId(ruleCondition.gettColumnId());
								{
									DataViewsColumns dvCol = new DataViewsColumns();
									dvCol = dataViewsColumnsRepository.findOne(ruleCondition.gettColumnId());
									if(dvCol != null)
									{
										ruleCondDTO.settColumnName(dvCol.getColumnName());
										if(dvCol.getColDataType() != null)
										{
											ruleCondDTO.settDataType(dvCol.getColDataType() );
										}
									}
										
								}
								//if(dataViewColumnsMap.size()>0 && dataViewColumnsMap.get(ruleCondition.gettColumnId())!=null && !dataViewColumnsMap.get(ruleCondition.gettColumnId()).getColumnName().isEmpty())
									
								
							}
							//ruleCondDTO.settColumnType(ruleCondition.gettColumnType());
							//ruleCondDTO.settDataViewId(ruleCondition.gettDataViewId());
							
							if(ruleDTO.getTargetDataViewId() != null)
							{
								//List<HashMap> tColList = new ArrayList<HashMap>();
								//DataViews DV=dataViewsRepository.findByTenantIdAndIdForDisplay(ruleGroup.getTenantId(), ruleDTO.getTargetDataViewId());
							//	tColList = dataViewsService.fetchDataViewAndColumnsByDvId(DV.getId());
								
								//ruleCondDTO.settColumnList(tColList);
								if(dataViewToColsMap.size()>0 && dataViewToColsMap.get(ruleDTO.getTargetDataViewId() ) != null)
								{
									ruleCondDTO.settColumnList(dataViewToColsMap.get(ruleDTO.getTargetDataViewId() ));
								}
							}
							
							String[] tdataViewDetails = new String[2];
							//tdataViewDetails = dataViewsIdtoNameMap.get(ruleCondDTO.gettDataViewId());

							/*if(tdataViewDetails  != null)
    						{
    							log.info("tdataViewDetails:"+tdataViewDetails[0]+tdataViewDetails[1]);
    							ruleCondDTO.settDataViewName(tdataViewDetails[0]);
        						ruleCondDTO.settDataViewDisplayName(tdataViewDetails[1]);
    						}*/
							if(ruleCondition.getsFormula()!=null)
								ruleCondDTO.setsFormula(ruleCondition.getsFormula());
							if(ruleCondition.gettFormula()!=null)
								ruleCondDTO.settFormula(ruleCondition.gettFormula());
							if(ruleCondition.issMany()!=null)
								ruleCondDTO.setsMany(ruleCondition.issMany());
							if(ruleCondition.istMany()!=null)
								ruleCondDTO.settMany(ruleCondition.istMany());
							if(ruleCondition.gettToleranceOperatorFrom()!=null && !ruleCondition.gettToleranceOperatorFrom().isEmpty())
								ruleCondDTO.settToleranceOperatorFrom(ruleCondition.gettToleranceOperatorFrom());
							
							if(ruleCondition.gettToleranceOperatorTo()!=null && !ruleCondition.gettToleranceOperatorTo().isEmpty())
								ruleCondDTO.settToleranceOperatorTo(ruleCondition.gettToleranceOperatorTo());
							if(ruleCondition.gettToleranceType()!=null && !ruleCondition.gettToleranceType().isEmpty())
							{
								ruleCondDTO.settToleranceType(ruleCondition.gettToleranceType());
								LookUpCode toleranceLookUp = new LookUpCode();
								toleranceLookUp = lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("TOLERANCE_TYPE", ruleCondition.gettToleranceType(), rule.getTenantId());
								
								if(toleranceLookUp != null && toleranceLookUp.getMeaning() != null)
									ruleCondDTO.settToleranceTypeMeaning(toleranceLookUp.getMeaning());
							}
								
							if(ruleCondition.gettToleranceValueFrom()!=null && !ruleCondition.gettToleranceValueFrom().isEmpty())
							{
								if(ruleCondition.gettToleranceValueFrom().endsWith("%"))
								{
									String[] output = new String[2];
									output = ruleCondition.gettToleranceValueFrom().split("%");
									if(output[0] != null)
									ruleCondDTO.settToleranceValueFrom(output[0]);
									ruleCondDTO.settPercentile(true);
								}
							else
								ruleCondDTO.settToleranceValueFrom(ruleCondition.gettToleranceValueFrom());
							}
							if(ruleCondition.gettToleranceValueTo()!=null && !ruleCondition.gettToleranceValueTo().isEmpty())
							{
								if(ruleCondition.gettToleranceValueTo().endsWith("%"))
								{
									String[] output = new String[2];
									output = ruleCondition.gettToleranceValueTo().split("%");
									if(output[0] != null)
									ruleCondDTO.settToleranceValueTo(output[0]);
									ruleCondDTO.settPercentile(true);
								}
							else
								ruleCondDTO.settToleranceValueTo(ruleCondition.gettToleranceValueTo());
							}
							if(ruleCondition.getsToleranceOperatorFrom()!=null && !ruleCondition.getsToleranceOperatorFrom().isEmpty())
								ruleCondDTO.setsToleranceOperatorFrom(ruleCondition.getsToleranceOperatorFrom());
							
							if(ruleCondition.getsToleranceOperatorTo()!=null && !ruleCondition.getsToleranceOperatorTo().isEmpty())
								ruleCondDTO.setsToleranceOperatorTo(ruleCondition.getsToleranceOperatorTo());
							
							if(ruleCondition.getsToleranceType()!=null && !ruleCondition.getsToleranceType().isEmpty())
							{
								ruleCondDTO.setsToleranceType(ruleCondition.getsToleranceType());
								LookUpCode toleranceLookUp = new LookUpCode();
								toleranceLookUp = lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("TOLERANCE_TYPE", ruleCondition.getsToleranceType(), rule.getTenantId());
								
								if(toleranceLookUp != null && toleranceLookUp.getMeaning() != null)
									ruleCondDTO.setsToleranceTypeMeaning(toleranceLookUp.getMeaning());
							}
								
							if(ruleCondition.getsToleranceValueFrom()!=null && !ruleCondition.getsToleranceValueFrom().isEmpty())
							{
								if(ruleCondition.getsToleranceValueFrom().endsWith("%"))
								{
									String[] output = new String[2];
									output = ruleCondition.getsToleranceValueFrom().split("%");
									if(output[0] != null)
									ruleCondDTO.setsToleranceValueFrom(output[0]);
									ruleCondDTO.setsPercentile(true);
								}
								else
								ruleCondDTO.setsToleranceValueFrom(ruleCondition.getsToleranceValueFrom());
							}
							if(ruleCondition.getsToleranceValueTo()!=null && !ruleCondition.getsToleranceValueTo().isEmpty())
							{
								if(ruleCondition.getsToleranceValueTo().endsWith("%"))
								{
									String[] output = new String[2];
									output = ruleCondition.getsToleranceValueTo().split("%");
									if(output[0] != null)
									ruleCondDTO.setsToleranceValueTo(output[0]);
									ruleCondDTO.setsPercentile(true);
								}
							else
								ruleCondDTO.setsToleranceValueTo(ruleCondition.getsToleranceValueTo());
							}
								
							if(ruleCondition.getsValue()!=null && !ruleCondition.getsValue().isEmpty())
								ruleCondDTO.setsValue(ruleCondition.getsValue());
							if(ruleCondition.gettValue()!=null && !ruleCondition.gettValue().isEmpty())
								ruleCondDTO.settValue(ruleCondition.gettValue());
							if(ruleCondition.getValue()!=null)
								ruleCondDTO.setValue(ruleCondition.getValue());
							if(ruleCondition.getOperator()!=null)
							{
								ruleCondDTO.setOperator(ruleCondition.getOperator());
								DataViewsColumns dvCol = new DataViewsColumns();
								if(ruleCondition.getsColumnId() != null)
								{
									dvCol = dataViewsColumnsRepository.findOne(ruleCondition.getsColumnId());
									if(dvCol != null)
									{
										//dvCol = dataViewColumnsMap.get(ruleCondition.getsColumnId());
										LookUpCode codeForOperator= new LookUpCode();
										if(codeForOperator != null)
										{
											if(dvCol!=null)
											{
												if(ruleCondition.getOperator()!=null && dvCol.getColDataType()!=null)
												{
													codeForOperator = lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId(dvCol.getColDataType(),ruleCondition.getOperator(),rule.getTenantId());
													if(codeForOperator != null && codeForOperator.getMeaning() != null)
														ruleCondDTO.setOperatorMeaning(codeForOperator.getMeaning());
												}
											}
										}
									}
								
									
								}
							}
							if(ruleCondition.getsColumnFieldName()!=null && !ruleCondition.getsColumnFieldName().isEmpty() )
								ruleCondDTO.setOperator(ruleCondition.getsColumnFieldName());
							if(ruleCondition.getsValueType()!=null && !ruleCondition.getsValueType().isEmpty() )
								ruleCondDTO.setOperator(ruleCondition.getsValueType());
							if(ruleCondition.gettValueType()!=null && !ruleCondition.gettValueType().isEmpty())
								ruleCondDTO.settValueType(ruleCondition.gettValueType());

							ruleConditionsdtoList.add(ruleCondDTO);


						}
						rulesDto.setRuleConditions(ruleConditionsdtoList);
					}

					if(rulesDto!=null)
						ruleDtoList.add(rulesDto);
				}

			}
		}
		if(ruleDtoList!=null)
			ruleGroupDto.setRules(ruleDtoList);
		return ruleGroupDto;
	}
    
    
	/* @PostMapping("/postAccountingRules")
	 @Timed
	 public List<ErrorReport> postAccountingRules(HttpServletRequest request,@RequestBody AccountingRuleDTO accountingRuleDTO)
	 {
		 log.info("Just call API");
		 AccountingRuleDTO acctRuleDTO=accountingRuleDTO;
		 HashMap map=userJdbcService.getuserInfoFromToken(request);
		 Long userId=Long.parseLong(map.get("userId").toString());
		 Long tenantId= Long.parseLong(map.get("tenantId").toString());
		 RuleGroup ruleGrp=new RuleGroup();
		 List<ErrorReport> errorReport=new ArrayList<ErrorReport>();
		 if(acctRuleDTO.getId()!=null && !acctRuleDTO.getId().equals("") )
		 {
			 if(acctRuleDTO.getId() !=null)
				{
					RuleGroup rg = ruleGroupRepository.findByIdForDisplayAndTenantId(acctRuleDTO.getId(), tenantId);
					if(rg != null && rg.getId() != null)
					{
						ruleGrp.setId(rg.getId());
						ruleGrp.setIdForDisplay(acctRuleDTO.getId());
					}
					
				}
		 }
		 else
		 {

		 }

		 if(acctRuleDTO.getName()!=null && !acctRuleDTO.getName().isEmpty())
			 ruleGrp.setName(acctRuleDTO.getName());
		 if(acctRuleDTO.getRulePurpose()!=null && !acctRuleDTO.getRulePurpose().isEmpty())
			 ruleGrp.setRulePurpose(acctRuleDTO.getRulePurpose());
		 if(acctRuleDTO.getStartDate()!=null)
			 ruleGrp.setStartDate(acctRuleDTO.getStartDate());
		 if(acctRuleDTO.getEndDate()!=null)
			 ruleGrp.setEndDate(acctRuleDTO.getEndDate());
		 if(acctRuleDTO.getEnableFlag()!=null)
			 ruleGrp.enabledFlag(acctRuleDTO.getEnableFlag());
		 ruleGrp.setTenantId(tenantId);
		 ruleGrp.setCreatedBy(accountingRuleDTO.getCreatedBy());
		 ruleGrp.setLastUpdatedBy(accountingRuleDTO.getCreatedBy());
		 ruleGrp.setCreationDate(ZonedDateTime.now());
		 ruleGrp.setLastUpdatedDate(ZonedDateTime.now());
		 RuleGroup ruleGrpId=ruleGroupRepository.save(ruleGrp);
		 if(acctRuleDTO.getId() == null)
		 {
			 String idForDisplay = IDORUtils.computeFrontEndIdentifier(ruleGrpId.getId().toString());
			 ruleGrpId.setIdForDisplay(idForDisplay);
			 ruleGrpId = ruleGroupRepository.save(ruleGrpId);
		 }
		
		 
		// ruleGroupSearchRepository.save(ruleGrpId);

		 if(ruleGrpId!=null && ruleGrpId.getId()!=null)
		 {
			 ErrorReport ruleGroupSave=new ErrorReport();
			 ruleGroupSave.setTaskName("Rule Group Save");
			 ruleGroupSave.setTaskStatus("Success");
			 ruleGroupSave.setDetails(ruleGrpId.getIdForDisplay()+"");
			 errorReport.add(ruleGroupSave);
			 //ruleId's By ruleGrpId
			 List<BigInteger> ruleIdsList=ruleGroupDetailsRepository.fetchRuleIdsByGroupAndTenantId(ruleGrpId.getId(),tenantId);
			 log.info("ruleIdsList before:"+ruleIdsList);

			 List<RulesAndLineItems> rulesAndItems=acctRuleDTO.getRules();
			 for(int i=0;i<rulesAndItems.size();i++)
			 {
				 RulesAndLineItems ruleItem=rulesAndItems.get(i);
				 Rules rule=new Rules();

				 if(ruleItem.getId()!=null && ruleItem.getId()!=0)
				 {

					 rule.setId(ruleItem.getId());
					 if(ruleIdsList.size()>0)
					 { 
						 for(int j=0;j<rulesAndItems.size();j++)
						 {
							 if(rulesAndItems.get(j).getId()!=null && rulesAndItems.get(j).getId()!=0)
							 {
							 for(int id=0;id<ruleIdsList.size();id++)
							 {
								 log.info("ruleIdsList.get(id) :"+ruleIdsList.get(id));
								 log.info("ruleDTO.get(i).getRule().getId() :"+rulesAndItems.get(j).getId());
								 if(ruleIdsList.get(id).longValue()==rulesAndItems.get(j).getId())
								 {
									 log.info("same");
									 ruleIdsList.remove(ruleIdsList.get(id));
								 }
							 }
						 }
						 }
					 }
					 log.info("ruleIdsList after:"+ruleIdsList);


				 }
				 // else

				 // {

				 for(int j=0;j<ruleIdsList.size();j++)
				 {
					 log.info("ruleIdsList.get(j).longValue() :"+ruleIdsList.get(j).longValue());
					 log.info("newGrp.getId() :"+ruleGrpId.getId());
					 RuleGroupDetails ruleGroup=ruleGroupDetailsRepository.findByRuleGroupIdAndRuleId(ruleGrpId.getId(),ruleIdsList.get(j).longValue());
					 log.info("ruleGrpId :"+ruleGrp);
					 log.info("");
					 if(ruleGroup!=null)
						 ruleGroupDetailsRepository.delete(ruleGroup);
				 }
				 if(ruleItem.getRuleCode()!=null && !ruleItem.getRuleCode().isEmpty())
					 rule.setRuleCode(ruleItem.getRuleCode());
				 if(ruleItem.getStartDate()!=null)
					 rule.setStartDate(ruleItem.getStartDate());
				 if(ruleItem.getEndDate()!=null)
					 rule.setEndDate(ruleItem.getEndDate());
				 if(ruleItem.getEnabledFlag()!=null)
					 rule.setEnabledFlag(ruleItem.getEnabledFlag());
				 if(ruleItem.getRuleType()!=null && !ruleItem.getRuleType().isEmpty())
					 rule.setRuleType(ruleItem.getRuleType());
				 if(ruleItem.getCoa()!=null && !ruleItem.getCoa().isEmpty())
					 rule.setcOA(ruleItem.getCoa());
				 rule.setTenantId(tenantId);
				 rule.setCreatedBy(accountingRuleDTO.getCreatedBy());
				 rule.setLastUpdatedBy(accountingRuleDTO.getCreatedBy());
				 rule.setCreationDate(ZonedDateTime.now());
				 rule.setLastUpdatedDate(ZonedDateTime.now());

				 Rules ruleID=rulesRepository.save(rule);



				 if(ruleID.getId()!=null && ruleGrpId.getId()!=null)
				 {
					 log.info("ruleItem.getId() :"+ruleItem.getId());
					 if( ruleItem.getId()==null || ruleItem.getId()==0)
					 {
						 log.info("ruleItem.getId() for adhoc in if:"+ruleItem.getId());
						 log.info("accountingRuleDTO.getAdhocRuleCreation()"+accountingRuleDTO.getAdhocRuleCreation());
						 if(accountingRuleDTO.getAdhocRuleCreation()==true)
						 {
							 log.info("ruleID.getRuleType() :"+ruleID.getRuleType());
							 HashMap parameterSet = new HashMap<>();
							 parameterSet.put("param1",ruleGrpId.getId());
							 log.info("ruleGrpId.getId() :"+ruleGrpId.getId());
							 parameterSet.put("param2",ruleID.getId());
							 log.info("ruleID :"+ruleID.getId());
							 log.info("ruleGroupDTO.getLastUpdatedby() :"+accountingRuleDTO.getLastUpdatedBy());
							 ResponseEntity response= oozieService.jobIntiateForAcctAndRec(accountingRuleDTO.getTenantId(), accountingRuleDTO.getLastUpdatedBy(), "Accounting", parameterSet,"ADHOC",request);
							 ErrorReport jobInitiation=new ErrorReport();
							 jobInitiation.setTaskName("Job Initiation");

							 log.info("response.getBody() :"+response.getBody());
							 String status=response.getBody().toString().substring(8).replaceAll("\\}", "");
							 log.info("status :"+status);
							 log.info("response.getstatusValue :"+response.getStatusCodeValue());


							 jobInitiation.setTaskStatus(status);
							 jobInitiation.setDetails(ruleID.getId()+"");
							 errorReport.add(jobInitiation);
						 }
					 }
					 RuleGroupDetails ruleGrpDetails=new RuleGroupDetails();
					 RuleGroupDetails ruleGrpDet=ruleGroupDetailsRepository.findByRuleGroupIdAndRuleId(ruleGrpId.getId(),ruleID.getId());
					 if(ruleGrpDet!=null)
					 {
						 ruleGrpDet.setPriority(ruleItem.getPriority());
						 ruleGrpDet.setEnabledFlag(ruleItem.isAssignmentFlag());
						 ruleGroupDetailsRepository.save(ruleGrpDet);
						 //ruleGroupDetailsRepository.save(ruleGrpDet);
					 }
					 else
					 {

						 ruleGrpDetails.setRuleGroupId(ruleGrpId.getId());
						 ruleGrpDetails.setRuleId(ruleID.getId());
						 ruleGrpDetails.setTenantId(tenantId);
						 ruleGrpDetails.setPriority(ruleItem.getPriority());
						 if(accountingRuleDTO.getCreatedBy()!=null)
							 ruleGrpDetails.setCreatedBy(accountingRuleDTO.getCreatedBy());
						 if(accountingRuleDTO.getLastUpdatedBy()!=null)
							 ruleGrpDetails.setLastUpdatedBy(accountingRuleDTO.getLastUpdatedBy());
						 ruleGrpDetails.setEnabledFlag(ruleItem.isAssignmentFlag());
						 ruleGrpDetails.setCreationDate(ZonedDateTime.now());
						 ruleGrpDetails.setLastUpdatedDate(ZonedDateTime.now());
						 RuleGroupDetails ruleGrpDets=ruleGroupDetailsRepository.save(ruleGrpDetails);
					 }
				 }
				 List<LineItems> lineItemsList=rulesAndItems.get(i).getLineItems();
				 for(LineItems lineItems:lineItemsList)
				 {

					 AccountingLineTypes acctLineType=new AccountingLineTypes();
					 AccountingLineTypes actLinetypeId=new AccountingLineTypes();
					 if(lineItems.getId()!=null && lineItems.getId()!=0)
					 {
						 // acctLineType.setId(lineItems.getId());
					 }
					 else
					 {
						 acctLineType.setTenantId(tenantId);
						 acctLineType.setRuleId(ruleID.getId());
						 acctLineType.setLineType(lineItems.getLineType());
						 acctLineType.setDataViewId(lineItems.getSourceDataviewId());
						 acctLineType.setCreatedBy(accountingRuleDTO.getCreatedBy());
						 acctLineType.setLastUpdatedBy(accountingRuleDTO.getCreatedBy());
						 acctLineType.setCreationDate(ZonedDateTime.now());
						 acctLineType.setLastUpdatedDate(ZonedDateTime.now());
						 actLinetypeId=accountingLineTypesRepository.save(acctLineType);
					 }
					 log.info("actLinetypeId :"+actLinetypeId);
					 List<AcctRuleCondDTO> acctRuleCondDTOList=lineItems.getAccountingRuleConditions();
					 for(AcctRuleCondDTO acctRuleCondDTO:acctRuleCondDTOList)
					 {
						 AcctRuleConditions acctRuleCond=new AcctRuleConditions();

						 if(acctRuleCondDTO.getId()!=null && acctRuleCondDTO.getId()!=0)
						 {

						 }
						 else
						 {
							 acctRuleCond.setRuleId(ruleID.getId());
							 acctRuleCond.setRuleActionId(actLinetypeId.getId());
							 if(acctRuleCondDTO.getOpenBracket()!=null && !acctRuleCondDTO.getOpenBracket().isEmpty())
								 acctRuleCond.setOpenBracket(acctRuleCondDTO.getOpenBracket());
							 if(acctRuleCondDTO.getOperator()!=null && !acctRuleCondDTO.getOperator().isEmpty())
								 acctRuleCond.setOperator(acctRuleCondDTO.getOperator());
							 if(acctRuleCondDTO.getValue()!=null && !acctRuleCondDTO.getValue().isEmpty())
								 acctRuleCond.setValue(acctRuleCondDTO.getValue());
							 if(acctRuleCondDTO.getCloseBracket()!=null && !acctRuleCondDTO.getCloseBracket().isEmpty())
								 acctRuleCond.setCloseBracket(acctRuleCondDTO.getCloseBracket());
							 if(acctRuleCondDTO.getLogicalOperator()!=null && !acctRuleCondDTO.getLogicalOperator().isEmpty())
								 acctRuleCond.setLogicalOperator(acctRuleCondDTO.getLogicalOperator());
							 if(acctRuleCondDTO.getsViewColumnId()!=null)
								 acctRuleCond.setsViewColumnId(acctRuleCondDTO.getsViewColumnId());
							 if(acctRuleCondDTO.getFunc()!=null && !acctRuleCondDTO.getFunc().isEmpty())
								 acctRuleCond.setFunction(acctRuleCondDTO.getFunc());

							 acctRuleCond.setCreatedBy(accountingRuleDTO.getCreatedBy());
							 acctRuleCond.setLastUpdatedBy(accountingRuleDTO.getCreatedBy());
							 acctRuleCond.setCreatedDate(ZonedDateTime.now());
							 acctRuleCond.setLastUpdatedDate(ZonedDateTime.now());
							 AcctRuleConditions actCond= acctRuleConditionsRepository.save(acctRuleCond);
							 if(actCond!=null && actCond.getId()!=null)
							 {

							 }
							 else
							 {
								 ErrorReport ruleConditionSave=new ErrorReport();
								 ruleConditionSave.setTaskName("Acct Rule Condition Save");
								 ruleConditionSave.setTaskStatus("failure");
								 errorReport.add(ruleConditionSave);
							 }
						 }
					 }
					 List<AcctRuleDerivationDTO> acctRuleDerDTOList=lineItems.getAccountingRuleDerivations();
					 for(AcctRuleDerivationDTO acctRuleDerDTO:acctRuleDerDTOList)
					 {
						 AcctRuleDerivations acctRuleDer=new AcctRuleDerivations();

						 if(acctRuleDerDTO.getId()!=null && acctRuleDerDTO.getId()!=0)
						 {

						 }
						 else
						 {
							 acctRuleDer.setAcctRuleActionId(actLinetypeId.getId());
							 if(acctRuleDerDTO.getDataViewColumn()!=null)
								 acctRuleDer.setDataViewColumn(acctRuleDerDTO.getDataViewColumn().toString());
							 if(acctRuleDerDTO.getAccountingReferencesCode()!=null && !acctRuleDerDTO.getAccountingReferencesCode().isEmpty())
								 acctRuleDer.setAccountingReferences(acctRuleDerDTO.getAccountingReferencesCode());
							 if(acctRuleDerDTO.getConstantValue()!=null && !acctRuleDerDTO.getConstantValue().isEmpty())
								 acctRuleDer.setConstantValue(acctRuleDerDTO.getConstantValue());
							 if(acctRuleDerDTO.getMappingSetId()!=null)
								 acctRuleDer.setMappingSetId(acctRuleDerDTO.getMappingSetId());
							 acctRuleDer.setCreatedBy(accountingRuleDTO.getCreatedBy());
							 acctRuleDer.setLastUpdatedBy(accountingRuleDTO.getCreatedBy());
							 acctRuleDer.setCreatedDate(ZonedDateTime.now());
							 acctRuleDer.setLastUpdatedDate(ZonedDateTime.now());
							 AcctRuleDerivations acctDer= acctRuleDerivationsRepository.save(acctRuleDer);
							 if(acctDer!=null && acctDer.getId()!=null)
							 {

							 }
							 else
							 {
								 ErrorReport ruleDerSave=new ErrorReport();
								 ruleDerSave.setTaskName("Acct Rule Derivation Save");
								 ruleDerSave.setTaskStatus("failure");
								 errorReport.add(ruleDerSave);
							 }
						 }

					 }

				 }
				 // }
			 }
		 }
		 else
		 {
			 ErrorReport ruleGroupSave=new ErrorReport();
			 ruleGroupSave.setTaskName("Rule Group Save");
			 ruleGroupSave.setTaskStatus("failure");
			 errorReport.add(ruleGroupSave);
		 }
		 return errorReport;


	 }
	*/
/**
 * Author:ravali
 * @param accountingRuleDTO
 * @param tenantId
 * @param userId
 * Desc:rules,ruleGrp,postAcctRule and AcctRuleDerivations
 */
	
/*
	 @PostMapping("/postAcctRuleConditionsAndAcctRuleDerivations")
	 @Timed
	 public List<ErrorReport> post(@RequestBody AccountingRuleDTO accountingRuleDTO)
	 {

		 log.info("Request Rest to post accounting rule conditions and derivations");
		 AccountingRuleDTO acctRuleDTO=accountingRuleDTO;
		 RuleGroup ruleGrp=new RuleGroup();
		 List<ErrorReport> errorReport=new ArrayList<ErrorReport>();
		 if(acctRuleDTO.getId()!=null && acctRuleDTO.getId()!=0)
		 {
			 ruleGrp.setId(acctRuleDTO.getId());
		 }
		 else
		 {

		 }

		 if(acctRuleDTO.getName()!=null && !acctRuleDTO.getName().isEmpty())
			 ruleGrp.setName(acctRuleDTO.getName());
		 if(acctRuleDTO.getRulePurpose()!=null && !acctRuleDTO.getRulePurpose().isEmpty())
			 ruleGrp.setRulePurpose(acctRuleDTO.getRulePurpose());
		 if(acctRuleDTO.getStartDate()!=null)
			 ruleGrp.setStartDate(acctRuleDTO.getStartDate().plusDays(1));
		 if(acctRuleDTO.getEndDate()!=null)
			 ruleGrp.setEndDate(acctRuleDTO.getEndDate().plusDays(1));
		 if(acctRuleDTO.getEnabledFlag()!=null)
			 ruleGrp.enabledFlag(acctRuleDTO.getEnabledFlag());
		 ruleGrp.setTenantId(accountingRuleDTO.getTenantId());
		 ruleGrp.setCreatedBy(accountingRuleDTO.getCreatedBy());
		 ruleGrp.setLastUpdatedBy(accountingRuleDTO.getCreatedBy());
		 ruleGrp.setCreationDate(ZonedDateTime.now());
		 ruleGrp.setLastUpdatedDate(ZonedDateTime.now());
		 RuleGroup ruleGrpId=ruleGroupRepository.save(ruleGrp);


		 if(ruleGrpId!=null && ruleGrpId.getId()!=null)
		 {
			 ErrorReport ruleGroupSave=new ErrorReport();
			 ruleGroupSave.setTaskName("Rule Group Save");
			 ruleGroupSave.setTaskStatus("Success");
			 ruleGroupSave.setDetails(ruleGrpId.getId()+"");
			 errorReport.add(ruleGroupSave);
			 //ruleId's By ruleGrpId
			 List<BigInteger> ruleIdsList=ruleGroupDetailsRepository.fetchRuleIdsByGroupAndTenantId(ruleGrpId.getId(),ruleGrpId.getTenantId());
			 log.info("ruleIdsList before:"+ruleIdsList);

			 List<RulesAndLineItems> rulesAndItems=acctRuleDTO.getRules();
			 for(int i=0;i<rulesAndItems.size();i++)
			 {
				 RulesAndLineItems ruleItem=rulesAndItems.get(i);
				 Rules rule=new Rules();

				 if(ruleItem.getId()!=null && ruleItem.getId()!=0)
				 {

					 rule.setId(ruleItem.getId());
					 if(ruleIdsList.size()>0)
					 {
						 for(int id=0;id<ruleIdsList.size();id++)
						 {
							 log.info("ruleIdsList.get(id) :"+ruleIdsList.get(id));
							 log.info("ruleDTO.get(i).getRule().getId() :"+ruleItem.getId());
							 if(ruleIdsList.get(id).longValue()==ruleItem.getId())
							 {
								 log.info("same");
								 ruleIdsList.remove(ruleIdsList.get(id));
							 }
						 }
					 }
					 log.info("ruleIdsList after:"+ruleIdsList);
					 

				 }
				// else

				// {
					 
					 for(int j=0;j<ruleIdsList.size();j++)
					 {
						 log.info("ruleIdsList.get(j).longValue() :"+ruleIdsList.get(j).longValue());
						 log.info("newGrp.getId() :"+ruleGrpId.getId());
						 RuleGroupDetails ruleGroup=ruleGroupDetailsRepository.findByRuleGroupIdAndRuleId(ruleGrpId.getId(),ruleIdsList.get(j).longValue());
						 log.info("ruleGrpId :"+ruleGrp);
						 log.info("");
						 if(ruleGroup!=null)
							 ruleGroupDetailsRepository.delete(ruleGroup);
					 }
					 if(ruleItem.getRuleCode()!=null && !ruleItem.getRuleCode().isEmpty())
						 rule.setRuleCode(ruleItem.getRuleCode());
					 if(ruleItem.getStartDate()!=null)
						 rule.setStartDate(ruleItem.getStartDate().plusDays(1));
					 if(ruleItem.getEndDate()!=null)
						 rule.setEndDate(ruleItem.getEndDate().plusDays(1));
					 if(ruleItem.getEnabledFlag()!=null)
						 rule.setEnabledFlag(ruleItem.getEnabledFlag());
					 if(ruleItem.getRuleType()!=null && !ruleItem.getRuleType().isEmpty())
						 rule.setRuleType(ruleItem.getRuleType());
					 if(ruleItem.getCoa()!=null && !ruleItem.getCoa().isEmpty())
						 rule.setCOA(ruleItem.getCoa());
					 rule.setTenantId(accountingRuleDTO.getTenantId());
					 rule.setCreatedBy(accountingRuleDTO.getCreatedBy());
					 rule.setLastUpdatedBy(accountingRuleDTO.getCreatedBy());
					 rule.setCreationDate(ZonedDateTime.now());
					 rule.setLastUpdatedDate(ZonedDateTime.now());

					 Rules ruleID=rulesRepository.save(rule);
					 
					
					 
					 if(ruleID.getId()!=null && ruleGrpId.getId()!=null)
					 {
						 if(ruleID.getRuleType()!=null && ruleID.getRuleType().equalsIgnoreCase("ADHOC"))
						 {
							 log.info("ruleID.getRuleType() :"+ruleID.getRuleType());
							 HashMap parameterSet = new HashMap<>();
								parameterSet.put("param1",ruleGrpId.getId());
								log.info("ruleGrpId.getId() :"+ruleGrpId.getId());
								parameterSet.put("param2",ruleID.getId());
								log.info("ruleID :"+ruleID.getId());
								log.info("ruleGroupDTO.getLastUpdatedby() :"+accountingRuleDTO.getLastUpdatedBy());
								//ResponseEntity response= oozieService.jobIntiateForAcctAndRec(accountingRuleDTO.getTenantId(), accountingRuleDTO.getLastUpdatedBy(), "Accounting", parameterSet);
								ErrorReport jobInitiation=new ErrorReport();
								jobInitiation.setTaskName("Job Initiation");
												
								//log.info("response.getBody() :"+response.getBody());
								//String status=response.getBody().toString().substring(8).replaceAll("\\}", "");
								//log.info("status :"+status);
								//log.info("response.getstatusValue :"+response.getStatusCodeValue());
								
								
								//jobInitiation.setTaskStatus(status);
								jobInitiation.setDetails(ruleID.getId()+"");
								errorReport.add(jobInitiation);
						 }
						
						 RuleGroupDetails ruleGrpDetails=new RuleGroupDetails();
						 RuleGroupDetails ruleGrpDet=ruleGroupDetailsRepository.findByRuleGroupIdAndRuleId(ruleGrpId.getId(),ruleID.getId());
							if(ruleGrpDet!=null)
							{
								ruleGrpDet.setEnabledFlag(ruleItem.isAssignmentFlag());
								ruleGroupDetailsRepository.save(ruleGrpDet);
								//ruleGroupDetailsRepository.save(ruleGrpDet);
							}
							else
							{
						
						 ruleGrpDetails.setRuleGroupId(ruleGrpId.getId());
						 ruleGrpDetails.setRuleId(ruleID.getId());
						 ruleGrpDetails.setTenantId(accountingRuleDTO.getTenantId());
						 ruleGrpDetails.setPriority(ruleItem.getPriority());
						 if(accountingRuleDTO.getCreatedBy()!=null)
						 ruleGrpDetails.setCreatedBy(accountingRuleDTO.getCreatedBy());
						 if(accountingRuleDTO.getLastUpdatedBy()!=null)
						 ruleGrpDetails.setLastUpdatedBy(accountingRuleDTO.getLastUpdatedBy());
						 ruleGrpDetails.setEnabledFlag(ruleItem.isAssignmentFlag());
						 ruleGrpDetails.setCreationDate(ZonedDateTime.now());
						 ruleGrpDetails.setLastUpdatedDate(ZonedDateTime.now());
						 RuleGroupDetails ruleGrpDets=ruleGroupDetailsRepository.save(ruleGrpDetails);
							}
					 }
					 List<LineItems> lineItemsList=rulesAndItems.get(i).getLineItems();
					 for(LineItems lineItems:lineItemsList)
					 {
						
						 AccountingLineTypes acctLineType=new AccountingLineTypes();
						 AccountingLineTypes actLinetypeId=new AccountingLineTypes();
						 if(lineItems.getId()!=null && lineItems.getId()!=0)
						 {
							// acctLineType.setId(lineItems.getId());
						 }
						 else
						 {
						 acctLineType.setTenantId(accountingRuleDTO.getTenantId());
						 acctLineType.setRuleId(ruleID.getId());
						 acctLineType.setLineType(lineItems.getLineType());
						 acctLineType.setDataViewId(lineItems.getSourceDataviewId());
						 acctLineType.setCreatedBy(accountingRuleDTO.getCreatedBy());
						 acctLineType.setLastUpdatedBy(accountingRuleDTO.getCreatedBy());
						 acctLineType.setCreationDate(ZonedDateTime.now());
						 acctLineType.setLastUpdatedDate(ZonedDateTime.now());
						 actLinetypeId=accountingLineTypesRepository.save(acctLineType);
						 }
						log.info("actLinetypeId :"+actLinetypeId);
						 List<AcctRuleCondDTO> acctRuleCondDTOList=lineItems.getAccountingRuleConditions();
						 for(AcctRuleCondDTO acctRuleCondDTO:acctRuleCondDTOList)
						 {
							 AcctRuleConditions acctRuleCond=new AcctRuleConditions();

							 if(acctRuleCondDTO.getId()!=null && acctRuleCondDTO.getId()!=0)
							 {

							 }
							 else
							 {
								 acctRuleCond.setRuleId(ruleID.getId());
								 acctRuleCond.setRuleActionId(actLinetypeId.getId());
								 if(acctRuleCondDTO.getOpenBracket()!=null && !acctRuleCondDTO.getOpenBracket().isEmpty())
									 acctRuleCond.setOpenBracket(acctRuleCondDTO.getOpenBracket());
								 if(acctRuleCondDTO.getOperator()!=null && !acctRuleCondDTO.getOperator().isEmpty())
									 acctRuleCond.setOperator(acctRuleCondDTO.getOperator());
								 if(acctRuleCondDTO.getValue()!=null && !acctRuleCondDTO.getValue().isEmpty())
									 acctRuleCond.setValue(acctRuleCondDTO.getValue());
								 if(acctRuleCondDTO.getCloseBracket()!=null && !acctRuleCondDTO.getCloseBracket().isEmpty())
									 acctRuleCond.setCloseBracket(acctRuleCondDTO.getCloseBracket());
								 if(acctRuleCondDTO.getLogicalOperator()!=null && !acctRuleCondDTO.getLogicalOperator().isEmpty())
									 acctRuleCond.setLogicalOperator(acctRuleCondDTO.getLogicalOperator());
								 if(acctRuleCondDTO.getsViewColumnId()!=null)
									 acctRuleCond.setsViewColumnId(acctRuleCondDTO.getsViewColumnId());
								 if(acctRuleCondDTO.getFunc()!=null && !acctRuleCondDTO.getFunc().isEmpty())
									 acctRuleCond.setFunction(acctRuleCondDTO.getFunc());

								 acctRuleCond.setCreatedBy(accountingRuleDTO.getCreatedBy());
								 acctRuleCond.setLastUpdatedBy(accountingRuleDTO.getCreatedBy());
								 acctRuleCond.setCreatedDate(ZonedDateTime.now());
								 acctRuleCond.setLastUpdatedDate(ZonedDateTime.now());
								 AcctRuleConditions actCond= acctRuleConditionsRepository.save(acctRuleCond);
								 if(actCond!=null && actCond.getId()!=null)
								 {

								 }
								 else
								 {
									 ErrorReport ruleConditionSave=new ErrorReport();
									 ruleConditionSave.setTaskName("Acct Rule Condition Save");
									 ruleConditionSave.setTaskStatus("failure");
									 errorReport.add(ruleConditionSave);
								 }
							 }
						 }
						 List<AcctRuleDerivationDTO> acctRuleDerDTOList=lineItems.getAccountingRuleDerivations();
						 for(AcctRuleDerivationDTO acctRuleDerDTO:acctRuleDerDTOList)
						 {
							 AcctRuleDerivations acctRuleDer=new AcctRuleDerivations();

							 if(acctRuleDerDTO.getId()!=null && acctRuleDerDTO.getId()!=0)
							 {

							 }
							 else
							 {
								 acctRuleDer.setAcctRuleActionId(actLinetypeId.getId());
								 if(acctRuleDerDTO.getDataViewColumn()!=null)
									 acctRuleDer.setDataViewColumn(acctRuleDerDTO.getDataViewColumn().toString());
								 if(acctRuleDerDTO.getAccountingReferencesCode()!=null && !acctRuleDerDTO.getAccountingReferencesCode().isEmpty())
									 acctRuleDer.setAccountingReferences(acctRuleDerDTO.getAccountingReferencesCode());
								 if(acctRuleDerDTO.getConstantValue()!=null && !acctRuleDerDTO.getConstantValue().isEmpty())
									 acctRuleDer.setConstantValue(acctRuleDerDTO.getConstantValue());
								 if(acctRuleDerDTO.getMappingSetId()!=null)
									 acctRuleDer.setMappingSetId(acctRuleDerDTO.getMappingSetId());
								 acctRuleDer.setCreatedBy(accountingRuleDTO.getCreatedBy());
								 acctRuleDer.setLastUpdatedBy(accountingRuleDTO.getCreatedBy());
								 acctRuleDer.setCreatedDate(ZonedDateTime.now());
								 acctRuleDer.setLastUpdatedDate(ZonedDateTime.now());
								 AcctRuleDerivations acctDer= acctRuleDerivationsRepository.save(acctRuleDer);
								 if(acctDer!=null && acctDer.getId()!=null)
								 {

								 }
								 else
								 {
									 ErrorReport ruleDerSave=new ErrorReport();
									 ruleDerSave.setTaskName("Acct Rule Derivation Save");
									 ruleDerSave.setTaskStatus("failure");
									 errorReport.add(ruleDerSave);
								 }
							 }

						 }

					 }
				// }
			 }
		 }
		 else
		 {
			 ErrorReport ruleGroupSave=new ErrorReport();
			 ruleGroupSave.setTaskName("Rule Group Save");
			 ruleGroupSave.setTaskStatus("failure");
			 errorReport.add(ruleGroupSave);
		 }
		 return errorReport;

	 }*/
	 
	 
	 
	 /**
	  * Author:Ravali
	  * @param groupId
	  * @return fetchAcctRuleConditionsAndAcctRuleDerivations
	  */
	
	    @GetMapping("/fetchAcctRuleConditionsAndAcctRuleDerivations")
		@Timed
	    public AccountingRuleDTO fetchAcctRulesCondAndDervtn(HttpServletRequest request, @RequestParam("groupId") String groupId)
	    {
	    	log.info("Request Rest to post accounting rule conditions and derivations");
	    	AccountingRuleDTO acctRuleDTO=new AccountingRuleDTO();
	    	HashMap map=userJdbcService.getuserInfoFromToken(request);
	    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
	    	RuleGroup ruleGrp=ruleGroupRepository.findByIdForDisplayAndTenantId(groupId, tenantId);
	    	if(ruleGrp.getId() != null )
	    		acctRuleDTO.setId(ruleGrp.getIdForDisplay());
	    	if(ruleGrp.getName()!=null && !ruleGrp.getName().isEmpty())
	    		acctRuleDTO.setName(ruleGrp.getName());
	    	if(ruleGrp.getRulePurpose()!=null && !ruleGrp.getRulePurpose().isEmpty())
			{
	    		acctRuleDTO.setRulePurpose(ruleGrp.getRulePurpose());
				LookUpCode lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("RULE_GROUP_TYPE",ruleGrp.getRulePurpose(), ruleGrp.getTenantId());
				if(lookUpCode != null &&  lookUpCode.getMeaning() != null)
					acctRuleDTO.setMeaning(lookUpCode.getMeaning());
			}
	    	if(ruleGrp.getStartDate()!=null)
	    		acctRuleDTO.setStartDate(ruleGrp.getStartDate());
	    	if(ruleGrp.getEndDate()!=null)
	    		acctRuleDTO.setEndDate(ruleGrp.getEndDate());
	    	if(ruleGrp.isEnabledFlag()!=null)
	    		acctRuleDTO.setEnableFlag(ruleGrp.isEnabledFlag());
	    	if(ruleGrp.getApprRuleGrpId() != null)
	    	{
	    		RuleGroup rg = new RuleGroup();
    			rg = ruleGroupRepository.findOne(ruleGrp.getApprRuleGrpId());
    			if(rg != null && rg.getIdForDisplay() != null)
    			{
    				acctRuleDTO.setApprRuleGrpId(rg.getIdForDisplay() );
    			}
	    	}
	    		
	    	List<RulesAndLineItems> rulesAndItems=new ArrayList<RulesAndLineItems>();
	    	List<RuleGroupDetails> ruleGrpDetailsList=ruleGroupDetailsRepository.findByRuleGroupId(ruleGrp.getId());
	    	if(ruleGrpDetailsList.size()>0)
	    	{
	    		for(int i=0;i<ruleGrpDetailsList.size();i++)
	    		{
	    			Rules rule=rulesRepository.findOne(ruleGrpDetailsList.get(i).getRuleId());

	    			RulesAndLineItems ruleItem=new RulesAndLineItems();
	    			
	    			//setting rule group detail id
	    			RuleGroupDetails ruleGrpDetails=ruleGroupDetailsRepository.findByRuleGroupIdAndRuleId(ruleGrp.getId(), rule.getId());
					if(ruleGrpDetails!=null)
						ruleItem.setRuleGroupAssignId(ruleGrpDetails.getId());
	    			ruleItem.setId(rule.getId());
	    			if(rule.getRuleCode()!=null && !rule.getRuleCode().isEmpty())
	    				ruleItem.setRuleCode(rule.getRuleCode());
	    			if(rule.getStartDate()!=null)
	    				ruleItem.setStartDate(rule.getStartDate());
	    			if(rule.getEndDate()!=null)
	    				ruleItem.setEndDate(rule.getEndDate());
	    			if(rule.isEnabledFlag()!=null)
	    				ruleItem.setEnabledFlag(rule.isEnabledFlag());
	    			if(rule.getRuleType()!=null && !rule.getRuleType().isEmpty())
	    			{
	    				ruleItem.setRuleType(rule.getRuleType());
	    				LookUpCode lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("RULE_TYPE",rule.getRuleType(), rule.getTenantId());
    					if(lookUpCode != null &&  lookUpCode.getMeaning() != null)
    						ruleItem.setRuleTypeMeaning( lookUpCode.getMeaning() );
	    			}
	    			
	    			if(rule.getcOA()!=null && !rule.getcOA().isEmpty())
	    			{
	    				ruleItem.setCoa(rule.getcOA());
	    				LookUpCode lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("CHART_OF_ACCOUNTS",rule.getcOA(), rule.getTenantId());
	    				if(lookUpCode != null && lookUpCode.getMeaning()!=null )
	    				ruleItem.setCoaMeaning(lookUpCode.getMeaning());
	    			}
	    				
	    			if(ruleGrpDetailsList.get(i).isEnabledFlag() !=null)
	    			ruleItem.setAssignmentFlag(ruleGrpDetailsList.get(i).isEnabledFlag());
	    			List<LineItems> lineItemsList=new ArrayList<LineItems>();
	    			List<AccountingLineTypes> acctLineTypesList=accountingLineTypesRepository.findByRuleId(rule.getId());
	    			for(AccountingLineTypes acctLineTypes:acctLineTypesList)
	    			{
	    				LineItems lineItems=new LineItems();

	    				lineItems.setId(acctLineTypes.getId());
	    				if(acctLineTypes.getLineType() != null)
	    				{
	    					lineItems.setLineType(acctLineTypes.getLineType());
	    					LookUpCode lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("LINE_TYPE",acctLineTypes.getLineType(), rule.getTenantId());
	    					if(lookUpCode != null && lookUpCode.getMeaning() != null)
	    					{
	    						lineItems.setLineTypeMeaning(lookUpCode.getMeaning());
	    					}
	    				}
	    				lineItems.setSourceDataviewId(acctLineTypes.getDataViewId());
	    				if(acctLineTypes.getDataViewId() != null)
						{
							List<HashMap> sColList = new ArrayList<HashMap>();
							sColList = dataViewsService.fetchDataViewAndColumnsByDvId(acctLineTypes.getDataViewId());
							DataViews dv = new DataViews();
							dv =dataViewsRepository.findOne(acctLineTypes.getDataViewId());
							lineItems.setSourceDataviewDisplayName(dv.getDataViewDispName());
							lineItems.setColumnsForDV(sColList);
						}

	    				List<AcctRuleCondDTO> acctRuleCondDTOList=new ArrayList<AcctRuleCondDTO>();
	    				List<AcctRuleConditions> acctRuleCondList=acctRuleConditionsRepository.findByRuleActionId(acctLineTypes.getId());
	    				Integer seq = 0 ;
	    				for(AcctRuleConditions acctRuleCond:acctRuleCondList)
	    				{
	    					// AcctRuleConditions acctRuleCond=new AcctRuleConditions();
	    					AcctRuleCondDTO acctRuleCondDTO=new AcctRuleCondDTO();
	    					seq = seq+1;
	    					acctRuleCondDTO.setSequence(seq);
	    					acctRuleCondDTO.setId(acctRuleCond.getId());
	    					if(acctRuleCond.getOpenBracket()!=null && !acctRuleCond.getOpenBracket().isEmpty())
	    						acctRuleCondDTO.setOpenBracket(acctRuleCond.getOpenBracket());
	    					if(acctRuleCond.getsViewColumnId()!=null)
	    					{
	    						acctRuleCondDTO.setsViewColumnId(acctRuleCond.getsViewColumnId());
	    						DataViewsColumns dViewCol=dataViewsColumnsRepository.findOne(acctRuleCond.getsViewColumnId());
	    						if(dViewCol!=null)
	    							acctRuleCondDTO.setsViewColumnName(dViewCol.getColumnName());
	    						
	    						if(acctRuleCond.getOperator()!=null && !acctRuleCond.getOperator().isEmpty())
		    					{
		    						acctRuleCondDTO.setOperator(acctRuleCond.getOperator());
		    						DataViewsColumns dvCol = new DataViewsColumns();
		    						dvCol = dataViewsColumnsRepository.findOne(acctRuleCond.getsViewColumnId());
		    						if(dvCol != null)
		    						{
		    							LookUpCode lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId(dvCol.getColDataType(),acctRuleCond.getOperator(), rule.getTenantId());
			    						if(lookUpCode != null && lookUpCode.getMeaning() != null)
			    						{
			    							acctRuleCondDTO.setOperatorMeaning(lookUpCode.getMeaning());
			    						}
		    						}
		    					}
	    					}
	    					if(acctRuleCond.getValue()!=null && !acctRuleCond.getValue().isEmpty())
	    						acctRuleCondDTO.setValue(acctRuleCond.getValue());
	    					if(acctRuleCond.getCloseBracket()!=null && !acctRuleCond.getCloseBracket().isEmpty())
	    						acctRuleCondDTO.setCloseBracket(acctRuleCond.getCloseBracket());
	    					if(acctRuleCond.getLogicalOperator()!=null && !acctRuleCond.getLogicalOperator().isEmpty())
	    					{
	    						acctRuleCondDTO.setLogicalOperator(acctRuleCond.getLogicalOperator());
	    						LookUpCode lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("LOGICAL_OPERATOR",acctRuleCond.getLogicalOperator(), rule.getTenantId());
	    						if(lookUpCode != null && lookUpCode.getMeaning() != null)
	    						{
	    							acctRuleCondDTO.setLogicalOperatorMeaning(lookUpCode.getMeaning() );
	    						}
	    					}
	    					
	    					
	    					
	    					if(acctRuleCond.getFunction()!=null && !acctRuleCond.getFunction().isEmpty())
	    						acctRuleCondDTO.setFunc(acctRuleCond.getFunction());


	    					acctRuleCondDTOList.add(acctRuleCondDTO);

	    				}

	    				List<AcctRuleDerivationDTO> acctRuleDerDTOList=new ArrayList<AcctRuleDerivationDTO>();
	    				List<AcctRuleDerivations> acctRuleDerList=acctRuleDerivationsRepository.findByAcctRuleActionId(acctLineTypes.getId());
	    				for(AcctRuleDerivations acctRuleDer:acctRuleDerList)
	    				{
	    					AcctRuleDerivationDTO acctRuleDerDTO=new AcctRuleDerivationDTO();

	    					acctRuleDerDTO.setId(acctRuleDer.getId());
	    					if(acctRuleDer.getDataViewColumn()!=null &&!acctRuleDer.getDataViewColumn().isEmpty())
	    					{
	    						acctRuleDerDTO.setDataViewColumn(Long.valueOf(acctRuleDer.getDataViewColumn()));//dataViewColumnName
	    						DataViewsColumns dvCol = new DataViewsColumns();
	    						dvCol = dataViewsColumnsRepository.findOne(Long.valueOf(acctRuleDer.getDataViewColumn()));
	    						if(dvCol != null && dvCol.getColumnName() != null)
	    						{
	    							acctRuleDerDTO.setDataViewColumnName(dvCol.getColumnName());
	    						}
	    						
	    					}
	    						
	    					if(acctRuleDer.getAccountingReferences()!=null && !acctRuleDer.getAccountingReferences().isEmpty())
	    					{
	    						acctRuleDerDTO.setAccountingReferencesCode(acctRuleDer.getAccountingReferences());
	    						LookUpCode lookUpCode=lookUpCodeRepository.findByTenantIdAndLookUpCodeAndLookUpType(ruleGrp.getTenantId(),acctRuleDer.getAccountingReferences(),rule.getcOA());
	    						if(lookUpCode != null &&lookUpCode.getMeaning()!=null)
	    							acctRuleDerDTO.setAccountingReferencesMeaning(lookUpCode.getMeaning());
	    					}
	    					if(acctRuleDer.getConstantValue()!=null && !acctRuleDer.getConstantValue().isEmpty())
	    						acctRuleDerDTO.setConstantValue(acctRuleDer.getConstantValue());
	    					if(acctRuleDer.getMappingSetId()!=null)
	    					{

	    						acctRuleDerDTO.setMappingSetId(acctRuleDer.getMappingSetId());
	    						MappingSet mappinSet=mappingSetRepository.findOne(acctRuleDer.getMappingSetId());
	    						if(mappinSet!=null)
	    							acctRuleDerDTO.setMappingSetName(mappinSet.getName());
	    					}


	    					acctRuleDerDTOList.add(acctRuleDerDTO);

	    				}

	    				lineItems.setAccountingRuleConditions(acctRuleCondDTOList);
	    				lineItems.setAccountingRuleDerivations(acctRuleDerDTOList);
	    				lineItemsList.add(lineItems);
	    			}

	    			ruleItem.setLineItems(lineItemsList);
	    			
	    			rulesAndItems.add(ruleItem);
	    		}

	    		acctRuleDTO.setRules(rulesAndItems);
	    	}

	    	return acctRuleDTO;
	    }
	    
	  
	   /**
	    * Author: ravali
	    * @param tenantId
	    * @param purpose
	    * @return rule group Id's
	    */
	    @GetMapping("/fetchRuleGroupIdsByTenantIdAndPurpose")
		@Timed
		public List<RuleGroupDTO> getRuleGrpIds(HttpServletRequest request,@RequestParam String purpose)
		{
	    	HashMap map=userJdbcService.getuserInfoFromToken(request);
	    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
	    	log.info("Request Rest to fetch rule Group Id By tenantId :"+tenantId+" and purpose :"+purpose);
	    	List<RuleGroupDTO> finalList = new ArrayList<RuleGroupDTO>();
	    	List<RuleGroup> ruleGrp=ruleGroupRepository.fetchByTenantIdAndRulePurpose(tenantId,purpose);
	    	if(ruleGrp.size()>0)
	    	{
	    		for(RuleGroup rg : ruleGrp)
	    		{
	    			RuleGroupDTO dto = new RuleGroupDTO();
	    			dto.setId(rg.getIdForDisplay());
	    			dto.setTenantId(rg.getTenantId());
	    			dto.setName(rg.getName());
	    			dto.setStartDate(rg.getStartDate());
	    			dto.setEndDate(rg.getEndDate());
	    			dto.setEnableFlag(rg.getEnabledFlag());
	    			dto.setCreatedBy(rg.getCreatedBy());
	    			dto.setLastUpdatedBy(rg.getLastUpdatedBy());
	    			dto.setCreationDate(rg.getCreationDate());
	    			dto.setLastUpdatedDate(rg.getLastUpdatedDate());
	    			dto.setRulePurpose(rg.getRulePurpose());
	    			if(rg.getApprRuleGrpId() != null){
	    				
	    				RuleGroup ruleGrpTagged = new RuleGroup();
	    				ruleGrpTagged = ruleGroupRepository.findOne(rg.getApprRuleGrpId());
	        			if(ruleGrpTagged != null && ruleGrpTagged.getIdForDisplay() != null)
	        			{
	        				dto.setApprRuleGrpId(rg.getIdForDisplay() );
	        			}
	    			} else {
	    				dto.setApprRuleGrpId(null);
	    				dto.setApprRuleGrpIdForDisplay("");
	    			}
	    			dto.setAccountingTypeCode(rg.getAccountingType());
	    			RuleGroup ruleGrpp = new RuleGroup();
	    			{
	    				if(rg.getReconciliationGroupId() != null)
	    					ruleGrpp =ruleGroupRepository.findOne(rg.getReconciliationGroupId());
	    			}
	    			if(ruleGrpp != null)
	    			{
		    			dto.setReconciliationGroupId(ruleGrpp.getIdForDisplay());
	    			}
	    			else
	    			{
		    			dto.setReconciliationGroupId(null);
	    			}
	    			dto.setControlAccount(rg.getControlAccount());
	    			dto.setConversionDate(rg.getConversionDate());
	    			dto.setFxGainAccount(rg.getFxGainAccount());
	    			dto.setFxLossAccount(rg.getFxLossAccount());
	    			dto.setFxRateId(rg.getFxRateId());
	    			dto.setRealizedGainLossAccount(rg.getRealizedGainLossAccount());
	    			dto.setActivityBased(rg.getActivityBased());
	    			dto.setCrossCurrency(rg.getCrossCurrency());
	    			dto.setMultiCurrency(rg.getMultiCurrency());
	    			finalList.add(dto);
	    		}
	    	}
	    	return finalList;
		}
	    
	    /**
	     * author:ravali
	     * @param tenantId
	     * @param ruleGroupId
	     * @return dataviewIds
	     */
	    @GetMapping("/fetchDataViewIds")
		@Timed
		public List<HashMap> getDataViewIds(@RequestParam Long tenantId,@RequestParam Long ruleGroupId)
		{
	    	log.info("Request Rest to fetch dataView Ids By tenantId :"+tenantId+" and ruleGrpId :"+ruleGroupId);
	    	List<HashMap> mapList=new ArrayList<HashMap>();
	    	RuleGroup ruleGrp=ruleGroupRepository.findOne(ruleGroupId);

	    	List<RuleGroupDetails> ruleGroupDetailsList=ruleGroupDetailsRepository.findByRuleGroupId(ruleGroupId);

	    	List<Long> dataViewIdList=new ArrayList<Long>();
	    	if(ruleGrp!=null && ruleGrp.getRulePurpose().equalsIgnoreCase("ACCOUNTING"))
	    	{
	    		HashMap map=new HashMap();
	    		List<BigInteger> dataViewIds=accountingLineTypesRepository.fetchDataViewIdsByTenantIdAndRuleId(tenantId,ruleGroupId);
	    		log.info("dataViewIds :"+dataViewIds);
	    		map.put("accountingDataViewIds", dataViewIds);
	    		if(!map.isEmpty())
	    			mapList.add(map);
	    	}
	    	else if(ruleGrp!=null && ruleGrp.getRulePurpose().equalsIgnoreCase("RECONCILIATION"))
	    	{
	    		for(RuleGroupDetails ruleGroupDetails:ruleGroupDetailsList)
	    		{

	    			HashMap map=new HashMap();
	    			Rules rule=rulesRepository.findOne(ruleGroupDetails.getRuleId());
	    			map.put("ruleId", rule.getId());
	    			HashMap dv=new HashMap();
	    			if(rule.getSourceDataViewId()!=null)
	    				dv.put("source", rule.getSourceDataViewId());
	    			if(rule.getTargetDataViewId()!=null)
	    				dv.put("target", rule.getTargetDataViewId());
	    			if(dv!=null && !dv.isEmpty())
	    				map.put("dataViews", dv);
	    			if(map!=null && !map.isEmpty())
	    				mapList.add(map);

	    		}
	    	}
	    	return mapList;




		}
	    /**
	     * Author : shobha
	     * @param tenantId
	     * @param rulePurpose
	     * @return 
	     */
	    @GetMapping("/fetchUnTaggedRuleGroups")
		@Timed
		public List<RuleGroupDTO> getUnTaggedRuleGroups(HttpServletRequest request,@RequestParam(value = "rulePurpose" , required = false) String rulePurpose,@RequestParam(value = "isActive" , required = false) Boolean isActive)
		{
	    	HashMap map=userJdbcService.getuserInfoFromToken(request);
	    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
	    	log.info("Rest request to fetchUnTaggedRuleGroups for tenant: "+tenantId);
	    	List<RuleGroupDTO> ruleGroupDTOList=new ArrayList<RuleGroupDTO>();
	    	List<RuleGroup> ruleGroupList = new ArrayList<RuleGroup>();
	    	ruleGroupList = ruleGroupRepository.findUnTaggedRuleGroups(tenantId, rulePurpose); 
	    	ruleGroupDTOList = fetchRuleGroupDTOByRuleGrpList(ruleGroupList,tenantId,isActive);
	    	return ruleGroupDTOList;
		}
		
	    /**
	     * author:Ravali
	     * @param tenantId
	     * @return
	     */
	    @GetMapping("/RuleGroupsWithMeaning")
		@Timed
		public List<RuleGroupDTO> getruleGrpDetails(@RequestParam(value = "page" , required = false) Integer offset,
				@RequestParam(value = "per_page", required = false) Integer limit,HttpServletResponse response, HttpServletRequest request,  @RequestParam(required = false) String sortColName, @RequestParam(required = false) String sortOrder,
				@RequestParam(value = "rulePurpose" , required = false) String rulePurpose,@RequestParam(value = "isActive" , required = false) Boolean isActive) throws URISyntaxException {
	    	HashMap map=userJdbcService.getuserInfoFromToken(request);
			Long tenantId=Long.parseLong(map.get("tenantId").toString());
	    	log.debug("REST request to get a page of Rule groups for tenant: "+tenantId+"=>"+offset+"=>"+limit+"=>"+limit+"sortColName"+sortColName+"sortOrder"+sortOrder);
	    	List<RuleGroup> ruleGroupList = new ArrayList<RuleGroup>();
	    	Page<RuleGroup> page = null;
	    	HttpHeaders headers = null;
	    	
	    	if(sortOrder==null)
				sortOrder="Descending";
			if(sortColName==null)
				sortColName="id";

			
			

	    	int count = 0;
	    	List<RuleGroup> ruleGrps = new ArrayList<RuleGroup>();
	    	if(rulePurpose == null || rulePurpose.isEmpty() || rulePurpose.equals("") || rulePurpose.toLowerCase().equalsIgnoreCase("all"))
	    	{
	    		ruleGrps = ruleGroupRepository.findByTenantIdOrderByIdDesc(tenantId);
	    	}
	    	else
	    	{
	    		ruleGrps = ruleGroupRepository.findByTenantIdAndRulePurposeOrderByIdDesc(tenantId,rulePurpose);	
	    	}
	    	count = ruleGrps.size();
	    	
	    	if(limit==null || limit == 0 ){
	    		limit = count;
	    	}
	    	if(offset == null || offset == 0)
	    	{
	    		offset = 0;
	    	}
	    	log.info("offset, limit"+offset+"=>"+ limit);
	    		if(rulePurpose == null || rulePurpose.isEmpty() || rulePurpose.equals("") || rulePurpose.equalsIgnoreCase("all") )
	    		{
	    			page = ruleGroupRepository.findByTenantId(tenantId,PaginationUtil.generatePageRequestWithSortColumn(offset, limit, sortOrder, sortColName));
	    		}
	    		else
	    		{
	    			page = ruleGroupRepository.findByTenantIdAndRulePurpose(tenantId,rulePurpose,PaginationUtil.generatePageRequestWithSortColumn(offset, limit, sortOrder, sortColName));
	    			
	    		}
	    		
	    	headers = PaginationUtil.generatePaginationHttpHeaders2(page, "/api/ruleGroupsByTenantId",offset, limit);
	    
	    	ruleGroupList = page.getContent();
	    	
	    			
	    	response.addIntHeader("X-COUNT",count);
	    	
	    	List<RuleGroupDTO> ruleGroupDTOList=new ArrayList<RuleGroupDTO>();
	    	ruleGroupDTOList = fetchRuleGroupDTOByRuleGrpList(ruleGroupList,tenantId,isActive);
	    	return ruleGroupDTOList;
	    }

	    public List<RuleGroupDTO> fetchRuleGroupDTOByRuleGrpList(List<RuleGroup> ruleGroupList,Long tenantId,Boolean isActive)
	    {

	    	List<RuleGroupDTO> ruleGroupDTOList=new ArrayList<RuleGroupDTO>();
	    	for(RuleGroup  ruleGroup:ruleGroupList)
	    	{
	    		if(isActive != null && !ruleGroup.isEnabledFlag())
	    		{
	    			
	    		}
	    		else
	    		{
	    			RuleGroupDTO ruleGroupDto=new RuleGroupDTO();
	    			if(ruleGroup.getId()!=null )
	    				ruleGroupDto.setId(ruleGroup.getIdForDisplay());
	    			ruleGroupDto.setTenantId(ruleGroup.getTenantId());
	    			if(ruleGroup.getName()!=null && !ruleGroup.getName().isEmpty())
	    				ruleGroupDto.setName(ruleGroup.getName());
	    			if(ruleGroup.getRulePurpose() != null && !ruleGroup.getRulePurpose().isEmpty())
	    			{
	    				//ruleGroupDto.setRulePurpose(ruleGroup.getRulePurpose());
	    				LookUpCode lookUp=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("RULE_GROUP_TYPE", ruleGroup.getRulePurpose(), tenantId);
	    				if(lookUp!=null)
	    					ruleGroupDto.setRulePurpose(lookUp.getMeaning());
	    			}
	    			if(ruleGroup.getStartDate()!=null)
	    				ruleGroupDto.setStartDate(ruleGroup.getStartDate());
	    			if(ruleGroup.getEndDate()!=null)
	    				ruleGroupDto.setEndDate(ruleGroup.getEndDate());
	    			if(ruleGroup.getEndDate() != null && ruleGroup.getEndDate().isBefore(ZonedDateTime.now()))
	    			{
	    				ruleGroupDto.setEndDated(true);
	    			}
	    			else
	    			{
	    				ruleGroupDto.setEndDated(false);
	    			}

	    			if(ruleGroup.isEnabledFlag()!=null)
	    			{
	    				/** active check**/

	        			Boolean activeStatus=activeStatusService.activeStatus(ruleGroup.getStartDate(), ruleGroup.getEndDate(), ruleGroup.isEnabledFlag());
	        			ruleGroupDto.setEnableFlag(activeStatus);
	        			/** active check end**/
	    				
	    			}
	    			if(ruleGroup.getCreatedBy()!=null)
	    				ruleGroupDto.setCreatedBy(ruleGroup.getCreatedBy());
	    			int ruleCnt =0;
	    			ruleCnt = ruleGroupDetailsRepository.findByRuleGroupId(ruleGroup.getId()).size();
	    			ruleGroupDto.setRuleCount(ruleCnt);
	    			if(ruleGroupDTOList.size()==0)
	    			{
	    				int reconCount = 0;
	    				reconCount = ruleGroupRepository.findByTenantIdAndRulePurpose(tenantId, "RECONCILIATION").size();
	    				int accCount= 0;
	    				accCount= ruleGroupRepository.findByTenantIdAndRulePurpose(tenantId, "ACCOUNTING").size();
	    				int appCount = 0;
	    				appCount= ruleGroupRepository.findByTenantIdAndRulePurpose(tenantId, "APPROVALS").size();
	    				int allCount= 0;
	    				allCount = reconCount+accCount+appCount;
	    				Map <String,Integer> map = new LinkedHashMap<String,Integer>();
	    				map.put("All",allCount);
	    				map.put("Reconciliation",reconCount);
	    				map.put("Accounting",accCount);
	    				map.put("Approvals",appCount);
	    				ruleGroupDto.setRuleGrpTypeAndCount(map);
	    			}
	    			//if(isActive != null)
	    			//{
	    			//if(isActive)
	    			//{

	    			//}
	    			//}else{
	    			ruleGroupDTOList.add(ruleGroupDto);
	    		}
	    		//}
	    	}
	    	return ruleGroupDTOList;
	    }
	    
	    
	    /**
	     * author:ravali
	     * @param approvalRuleDto
	     * @return errorReport
	     * Desc: 
	     */
	    @PostMapping("/postAppRuleConditionsAndAppRuleActions")
		@Timed
	
		public ErrorReport postAppRuleConditionsAndRuleActions(HttpServletRequest request,@RequestBody ApprovalRuleDto approvalRuleDto,@RequestParam (required=false) Boolean bulkUpload) throws RuntimeException
	    {

	    	log.info("Request Rest to post approval rule conditions and actions");
	    	String errorMessage = "";
	    	ErrorReport errorReport=new ErrorReport();
	    	try
	    	{
	    		errorReport = ruleGroupService.postApprovalRuleGroup(request,approvalRuleDto,bulkUpload);
	    	}
	    	catch(Exception e)
	    	{
	    		ErrorReport failedReport = new ErrorReport();
	    		failedReport.setTaskName("Approval process save report");
	    		failedReport.setTaskStatus("Failed");
	    		failedReport.setDetails(e.getMessage());
	    		errorReport = failedReport;
	    	}
	    	return errorReport;

	    }
		
	    
	    /**
	     * author:ravali
	     * @param groupId
	     * @return ApprovalRuleDto
	     * @throws SQLException 
	     */
	    @GetMapping("/fetchAppRuleConditionsAndAppActions")
		@Timed
	    public ApprovalRuleDto fetchAppRulesCondAndAct(HttpServletRequest request, @RequestParam("groupId") String groupId) throws SQLException
	    {
	    	log.info("Request Rest to get approval rule conditions and actions :" +groupId);
	    	HashMap map1=userJdbcService.getuserInfoFromToken(request);
			Long tenantId=Long.parseLong(map1.get("tenantId").toString());
	    	ApprovalRuleDto appRuleDTO=new ApprovalRuleDto();
	    	RuleGroup ruleGrp=ruleGroupRepository.findByIdForDisplayAndTenantId(groupId, tenantId);
	    	if(ruleGrp.getId() != null )
	    		appRuleDTO.setId(ruleGrp.getIdForDisplay());
	    	if(ruleGrp.getName()!=null && !ruleGrp.getName().isEmpty())
	    		appRuleDTO.setName(ruleGrp.getName());
	    	if(ruleGrp.getRulePurpose()!=null && !ruleGrp.getRulePurpose().isEmpty())
	    		appRuleDTO.setRulePurpose(ruleGrp.getRulePurpose());
	    	if(ruleGrp.getStartDate()!=null)
	    		appRuleDTO.setStartDate(ruleGrp.getStartDate());
	    	if(ruleGrp.getEndDate()!=null)
	    		appRuleDTO.setEndDate(ruleGrp.getEndDate());
	    	if(ruleGrp.isEnabledFlag()!=null)
	    		appRuleDTO.setEnableFlag(ruleGrp.isEnabledFlag());
	    	if(ruleGrp.getCreatedBy()!=null)
	    		appRuleDTO.setCreatedBy(ruleGrp.getCreatedBy());
	    	if(ruleGrp.getCreationDate()!=null)
	    		appRuleDTO.setCreatedDate(ruleGrp.getCreationDate());
	    	RuleGroup taggedRG = new RuleGroup();
	    	taggedRG = ruleGroupRepository.findTaggedGroup(ruleGrp.getId());
				if(taggedRG != null)
				{
					appRuleDTO.setApprRuleGrpName(taggedRG.getName());
					if(taggedRG.getRulePurpose().toLowerCase().contains("recon"))
					{
						appRuleDTO.setConfiguredModuleName("Reconciliation");
						appRuleDTO.setConfiguredModuleId("RECONCILIATION");
					}else if(taggedRG.getRulePurpose().toLowerCase().contains("accounting"))
					{
						appRuleDTO.setConfiguredModuleName("Accounting");
						appRuleDTO.setConfiguredModuleId("ACCOUNTING");
					}
					appRuleDTO.setApprRuleGrpId(taggedRG.getIdForDisplay());
					appRuleDTO.setApprRuleGrpName(taggedRG.getName());
				}
				
	    	
	    	List<AppRuleCondAndActDto> appRuleCondAndActDtoList=new ArrayList<AppRuleCondAndActDto>();
	    	List<RuleGroupDetails> ruleGrpDetailsList=ruleGroupDetailsRepository.findByRuleGroupIdOrderByPriorityAsc(ruleGrp.getId());
	    	if(ruleGrpDetailsList.size()>0)
	    	{
	    		for(int i=0;i<ruleGrpDetailsList.size();i++)
	    		{
	    			Rules rule=rulesRepository.findOne(ruleGrpDetailsList.get(i).getRuleId());
	    			log.info("rule :"+rule);
	    			
	    			AppRuleCondAndActDto appRuleCondAndAct=new AppRuleCondAndActDto();
	    			
	    			
	    			// setting rule group Details Id
					RuleGroupDetails ruleGrpDetails=ruleGroupDetailsRepository.findByRuleGroupIdAndRuleId(ruleGrp.getId(), rule.getId());
							if(ruleGrpDetails!=null)
								appRuleCondAndAct.setRuleGroupAssignId(ruleGrpDetails.getId());
					
					appRuleCondAndAct.setEditRule(true);
	    			appRuleCondAndAct.setId(rule.getId());
	    			if(rule.getRuleCode()!=null && !rule.getRuleCode().isEmpty())
	    				appRuleCondAndAct.setRuleCode(rule.getRuleCode());
	    			if(rule.getStartDate()!=null)
	    				appRuleCondAndAct.setStartDate(rule.getStartDate());
	    			if(rule.getEndDate()!=null)
	    				appRuleCondAndAct.setEndDate(rule.getEndDate());
	    			if(rule.isEnabledFlag()!=null)
	    				appRuleCondAndAct.setEnabledFlag(rule.isEnabledFlag());
	    			if(ruleGrpDetailsList.get(i).isEnabledFlag()!=null)
	    				appRuleCondAndAct.setAssignmentFlag(ruleGrpDetailsList.get(i).isEnabledFlag());
	    			if(rule.getRuleType() !=null)
	    			{
	    				appRuleCondAndAct.setApprovalNeededType( rule.getRuleType() );
	    				LookUpCode lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("APPROVAL_TYPE",rule.getRuleType(), ruleGrp.getTenantId());
    					if(lookUpCode != null &&  lookUpCode.getMeaning() != null)
    						appRuleCondAndAct.setApprovalNeededTypeMeaning( lookUpCode.getMeaning() );
	    			}
	    				
	    			if(rule.getSourceDataViewId()!=null)
	    			{
	    				
	    				
	    				DataViews dv=dataViewsRepository.findOne(rule.getSourceDataViewId());
	    				appRuleCondAndAct.setSourceDataViewId(dv.getIdForDisplay());
	    				if(dv!=null && dv.getDataViewDispName()!=null)
	    					appRuleCondAndAct.setSourceDataViewName(dv.getDataViewDispName());
	    			}
	    			
	    			if(rule.getCreatedBy()!=null)
	    				appRuleCondAndAct.setCreatedBy(rule.getCreatedBy());
	    			if(rule.getCreationDate()!=null)
	    				appRuleCondAndAct.setCreatedDate(rule.getCreationDate());
	    			if(rule.getLastUpdatedBy()!=null)
	    				appRuleCondAndAct.setLastUpdatedBy(rule.getLastUpdatedBy());
	    			if(rule.getLastUpdatedDate()!=null)
	    				appRuleCondAndAct.setCreatedDate(rule.getLastUpdatedDate());
	    			log.info("appRuleCondAndAct.get :"+appRuleCondAndAct.getEnabledFlag());
	    			List<AppRuleCondDto> appRuleCondDtoList=new ArrayList<AppRuleCondDto>();
	    			List<AppRuleConditions> appRuleCondList=appRuleConditionsRepository.findByRuleId(rule.getId());
	    			Integer seq=0;
	    			for(AppRuleConditions appRuleCond:appRuleCondList)
	    			{
	    				AppRuleCondDto appRuleCondDto=new AppRuleCondDto();

	    				seq=seq+1;
	    				appRuleCondDto.setSequence(seq);
	    				appRuleCondDto.setValue(appRuleCond.getValue());
	    				appRuleCondDto.setId(appRuleCond.getId());
	    				if(appRuleCond.getOpenBracket() != null)
	    					appRuleCondDto.setOpenBracket(appRuleCond.getOpenBracket());
	    				if(appRuleCond.getCloseBracket() != null)
	    					appRuleCondDto.setCloseBracket(appRuleCond.getCloseBracket());
	    				if(appRuleCond.getCreatedBy()!=null)
	    				appRuleCondDto.setCreatedBy(appRuleCond.getCreatedBy());
	    				if(appRuleCond.getCreatedDate()!=null)
	    					appRuleCondDto.setCreatedDate(appRuleCond.getCreatedDate());
	    				if(appRuleCond.getLastUpdatedBy()!=null)
		    				appRuleCondDto.setLastUpdatedBy(appRuleCond.getLastUpdatedBy());
		    				if(appRuleCond.getLastUpdatedDate()!=null)
		    					appRuleCondDto.setCreatedDate(appRuleCond.getLastUpdatedDate());
	    				
	    				if(appRuleCond.getColumnId()!=null)
	    				{
	    					appRuleCondDto.setColumnId(appRuleCond.getColumnId());
	    					DataViewsColumns dvcolName=dataViewsColumnsRepository.findOne(appRuleCond.getColumnId());
	    					if(dvcolName!=null && dvcolName.getColumnName()!=null)
	    						appRuleCondDto.setColumnName(dvcolName.getColumnName());
	    					if(dvcolName != null && dvcolName.getColDataType() != null)
	    					{
	    						appRuleCondDto.setColDataType(dvcolName.getColDataType());
	    					}
	    				}
	    				if(appRuleCond.getOperator()!=null)
						{
	    					appRuleCondDto.setOperator(appRuleCond.getOperator());
							DataViewsColumns dvCol = new DataViewsColumns();
							if(appRuleCond.getColumnId() != null)
							{
								dvCol = dataViewsColumnsRepository.findOne(appRuleCond.getColumnId());
								LookUpCode codeForOperator= new LookUpCode();
								if(codeForOperator != null)
								{
									if(dvCol!=null)
									{
										if(appRuleCond.getOperator()!=null && dvCol.getColDataType()!=null)
										{
											codeForOperator = lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId(dvCol.getColDataType(),appRuleCond.getOperator(), ruleGrp.getTenantId());
											if(codeForOperator != null && codeForOperator.getMeaning() != null)
												appRuleCondDto.setOperatorMeaning(codeForOperator.getMeaning());
										}
									}
								}
							}
						}
	    				if(appRuleCond.getOperator()!=null)
	    				{
	    					appRuleCondDto.setOperator(appRuleCond.getOperator());
	    					LookUpCode lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("OPERATOR", appRuleCond.getOperator(), ruleGrp.getTenantId());
	    					if(lookUpCode != null &&  lookUpCode.getMeaning() != null)
	    						appRuleCondDto.setOperatorMeaning(lookUpCode.getMeaning());
	    				}
	    				if(appRuleCond.getLogicalOperator()!=null)
	    				{
	    					appRuleCondDto.setLogicalOperator(appRuleCond.getLogicalOperator());
	    					LookUpCode lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("LOGICAL_OPERATOR", appRuleCond.getLogicalOperator(), ruleGrp.getTenantId());
	    					if(lookUpCode != null &&  lookUpCode.getMeaning() != null)
	    						appRuleCondDto.setLogicalOperatorMeaning(lookUpCode.getMeaning());
	    				}
	    				appRuleCondDtoList.add(appRuleCondDto);
	    			}
	    			appRuleCondAndAct.setApprovalConditions(appRuleCondDtoList);

	    			ApprovalActionDto appActDto=new ApprovalActionDto();
	    			List<ApprovalRuleAssignment> appRuleAssignList=approvalRuleAssignmentRepository.findByRuleId(rule.getId());
	    			List<ApprRuleAssgnDto> apprRuleAssgnDtoList=new ArrayList<ApprRuleAssgnDto>();
	    			for(ApprovalRuleAssignment  AppRuleAssign:appRuleAssignList)
	    			{
	    				ApprRuleAssgnDto apprRuleAssgnDto=new ApprRuleAssgnDto();
	    				apprRuleAssgnDto.setId(AppRuleAssign.getId());
	    				appActDto.setAssigneeType(AppRuleAssign.getAssignType());
	    				apprRuleAssgnDto.setAssigneeId(AppRuleAssign.getAssigneeId());
	    				if(AppRuleAssign.getAssignType().equalsIgnoreCase("USER"))
	    				{

	    					log.info("assignee type is user");
	    					try{ 
	    						String loginName="";
	    						String assigneeName="";
	    						HashMap map=userJdbcService.jdbcConnc(AppRuleAssign.getAssigneeId(),tenantId);
	    						if(map!=null && map.get("loginName")!=null)
	    							apprRuleAssgnDto.setLoginName(map.get("loginName").toString());
	    						if(map!=null && map.get("assigneeName")!=null)
	    							apprRuleAssgnDto.setAssigneeName(map.get("assigneeName").toString());



	    					}catch(SQLException se){
	    						log.info("se: "+se);
	    					} catch (ClassNotFoundException e) {
	    						// TODO Auto-generated catch block
	    						e.printStackTrace();
	    					}


	    					//apprRuleAssgnDto.setAssigneeName(assigneeName);
	    				}
	    				else if(AppRuleAssign.getAssignType().equalsIgnoreCase("GROUP"))
	    				{
	    					log.info("assignee type is group");
	    					ApprovalGroups appGroup= new ApprovalGroups(); 
	    					if(AppRuleAssign.getAssigneeId() != null)
	    					appGroup = approvalGroupsRepository.findOne(AppRuleAssign.getAssigneeId());
	    					apprRuleAssgnDto.setAssigneeId(AppRuleAssign.getAssigneeId());
	    					if(appGroup != null)
	    					{
	    						apprRuleAssgnDto.setAssigneeName(appGroup.getGroupName());
	    					}
	    					
	    				}
	    				apprRuleAssgnDto.setAutoApproval(AppRuleAssign.isAutoApproval());
	    				apprRuleAssgnDto.setNotification(true);
	    				apprRuleAssgnDto.setEmail(AppRuleAssign.isEmail());
	    				if(AppRuleAssign.getCreatedBy()!=null)
	    				apprRuleAssgnDto.setCreatedBy(AppRuleAssign.getCreatedBy());
	    				if(AppRuleAssign.getLastUpdatedBy()!=null)
	    				apprRuleAssgnDto.setLastUpdatedBy(AppRuleAssign.getLastUpdatedBy());
	    				if(AppRuleAssign.getCreationDate()!=null)
	    				apprRuleAssgnDto.setCreationDate(AppRuleAssign.getCreationDate());
	    				apprRuleAssgnDtoList.add(apprRuleAssgnDto);

	    			}

	    			appActDto.setActionDetails(apprRuleAssgnDtoList);
	    			appRuleCondAndAct.setApprovalActions(appActDto);
	    			appRuleCondAndActDtoList.add(appRuleCondAndAct);

	    		}
	    		appRuleDTO.setRules(appRuleCondAndActDtoList);
	    	}

	    	return appRuleDTO;

	    }
	    
	    /**
	     * poc for getting current user
	     * @param useId
	     * @return
	     * @throws ClassNotFoundException
	     * @throws SQLException
	     * @throws NoSuchFieldException
	     * @throws SecurityException
	     */
	    @GetMapping("/map")
		@Timed
	    public Map handleRequest(@RequestParam Long useId) throws NoSuchFieldException, SecurityException {
	        User user = (User) SecurityContextHolder.getContext()
	            .getAuthentication().getPrincipal();
	      //  Query query = em.createNativeQuery("SELECT * FROM User WHERE id="+useId, User.class);
	     //   log.info("query.getResultList() :"+query.getResultList());
	        Map<String, Object> userModel = new HashMap<String, Object>();
	        userModel.put("username", user.getUsername());
	       // log.info("class :"+user.getClass().getField("tenantId")); 
	       log.info("class :"+user.getClass().getField("tenant_id")); 
	        return userModel;
	        
	        
	        
	        
	        
	      }
	    
	    /**
	     * Author : Shobha, Ravali
	     * @param request
	     * @param accountingRuleDTO
	     * @return
	     */
	     @PostMapping("/postAccountingRuleDefination")
		 @Timed
		 public List<ErrorReport> postAccountingRuleDefination(HttpServletRequest request,@RequestBody AccountingRuleDTO accountingRuleDTO)
		 {
			 log.info("Rest Request to post accounting rule definition");
			 AccountingRuleDTO acctRuleDTO=accountingRuleDTO;
			 HashMap map=userJdbcService.getuserInfoFromToken(request);
			 Long userId=Long.parseLong(map.get("userId").toString());
			 Long tenantId= Long.parseLong(map.get("tenantId").toString());
			 
			 /**************************************************************************/
			 RuleGroup ruleGrp=new RuleGroup();
			 RuleGroup ruleGroupToProceed=new RuleGroup();
			 
			 List<ErrorReport> errorReport=new ArrayList<ErrorReport>();
			 if(accountingRuleDTO.getAdhocRuleCreation() != null && !accountingRuleDTO.getAdhocRuleCreation())
			 {
				 if(acctRuleDTO.getId()!=null && !acctRuleDTO.getId().equals(""))
				 {
					 RuleGroup accRuleGroup = ruleGroupRepository.findByIdForDisplayAndTenantId(acctRuleDTO.getId(), tenantId);
					 if(accRuleGroup != null && accRuleGroup.getId() != null)
					 {
						 ruleGroupToProceed=accRuleGroup;
						 ruleGrp.setId(accRuleGroup.getId());
						 ruleGrp.setIdForDisplay(acctRuleDTO.getId());
					 }
				 }
				 else
				 {

				 }
				 if(acctRuleDTO.getName()!=null && !acctRuleDTO.getName().isEmpty())
					 ruleGrp.setName(acctRuleDTO.getName());
				 if(acctRuleDTO.getRulePurpose()!=null && !acctRuleDTO.getRulePurpose().isEmpty())
					 ruleGrp.setRulePurpose(acctRuleDTO.getRulePurpose());
				 if(acctRuleDTO.getStartDate()!=null)
					 ruleGrp.setStartDate(acctRuleDTO.getStartDate());
				 if(acctRuleDTO.getEndDate()!=null)
					 ruleGrp.setEndDate(acctRuleDTO.getEndDate());
				 if(acctRuleDTO.getEnableFlag()!=null)
					 ruleGrp.enabledFlag(acctRuleDTO.getEnableFlag());
				 if(acctRuleDTO.getAccountingTypeCode()!=null)
					 ruleGrp.setAccountingType(acctRuleDTO.getAccountingTypeCode());
				 
				 if(acctRuleDTO.getActivityBased() != null )
					 ruleGrp.setActivityBased(acctRuleDTO.getActivityBased());
				 if(acctRuleDTO.getReconciliationGroupId()!=null)
				 {
					 RuleGroup rg = new RuleGroup ();
					 rg = ruleGroupRepository.findByIdForDisplayAndTenantId(acctRuleDTO.getReconciliationGroupId(), tenantId);
					 ruleGrp.setReconciliationGroupId(rg.getId());
				 }
					
				 if(acctRuleDTO.getApprRuleGrpId() != null)
				 {
					 RuleGroup taggedRuleGrp = new RuleGroup();
					 taggedRuleGrp = ruleGroupRepository.findByIdForDisplayAndTenantId(acctRuleDTO.getApprRuleGrpId(), tenantId);
					 if(taggedRuleGrp != null && taggedRuleGrp.getId() != null)
					 {
						 ruleGrp.setApprRuleGrpId(taggedRuleGrp.getId());
					 }
				 }
				 if(acctRuleDTO.getCrossCurrency() != null)
					 ruleGrp.setCrossCurrency(acctRuleDTO.getCrossCurrency());
				 if(acctRuleDTO.getControlAccount() != null)
					 ruleGrp.setControlAccount(acctRuleDTO.getControlAccount());
				 if(acctRuleDTO.getRealizedGainLossAccount() != null)
					 ruleGrp.setRealizedGainLossAccount(acctRuleDTO.getRealizedGainLossAccount());
				 if(acctRuleDTO.getFxGainAccount() != null)
					 ruleGrp.setFxGainAccount(acctRuleDTO.getFxGainAccount());
				 if(acctRuleDTO.getFxLossAccount() != null)
					 ruleGrp.setFxLossAccount(acctRuleDTO.getFxLossAccount());
				 
				 
				 if(acctRuleDTO.getMultiCurrency() != null)
					 ruleGrp.setMultiCurrency(acctRuleDTO.getMultiCurrency());
				 if(acctRuleDTO.getConversionDate() != null)
					 ruleGrp.setConversionDate(acctRuleDTO.getConversionDate());
				 if(acctRuleDTO.getFxRateId() != null)
					 ruleGrp.setFxRateId(acctRuleDTO.getFxRateId() );
				 
				 ruleGrp.setTenantId(tenantId);
				 ruleGrp.setCreatedBy(accountingRuleDTO.getCreatedBy());
				
				 if(acctRuleDTO.getId()==null )
				 {
					 ruleGrp.setCreationDate(ZonedDateTime.now());
				 }
				 else
					 ruleGrp.setCreationDate(accountingRuleDTO.getCreationDate()); 
				 ruleGrp.setLastUpdatedBy(userId);
				 ruleGrp.setLastUpdatedDate(ZonedDateTime.now());
				 
				 RuleGroup ruleGrpId=ruleGroupRepository.save(ruleGrp);
				 if( acctRuleDTO.getId() == null)
				 {
					 String idForDisplay = IDORUtils.computeFrontEndIdentifier(ruleGrpId.getId().toString());
					 ruleGrpId.setIdForDisplay(idForDisplay);
					 ruleGroupToProceed = ruleGroupRepository.save(ruleGrpId);	 
				 }
			 }
			 else
			 {
				 ruleGroupToProceed= ruleGroupRepository.findByIdForDisplayAndTenantId(accountingRuleDTO.getId(), tenantId);
			 }
			 
			 
			 //Group save
			/**************************************************************************/

			 if(ruleGroupToProceed!=null && ruleGroupToProceed.getId()!=null)
			 {
				 ErrorReport ruleGroupSave=new ErrorReport();
				 ruleGroupSave.setTaskName("Rule Group Save");
				 ruleGroupSave.setTaskStatus("Success");
				 ruleGroupSave.setDetails(ruleGroupToProceed.getIdForDisplay()+"");
				 errorReport.add(ruleGroupSave);
				 //ruleId's By ruleGrpId
				 
				 List<BigInteger> ruleIdsList=ruleGroupDetailsRepository.fetchRuleIdsByGroupAndTenantId(ruleGroupToProceed.getId(),ruleGroupToProceed.getTenantId());
				 log.info("ruleIdsList before:"+ruleIdsList);

				 List<RulesAndLineItems> rulesList=acctRuleDTO.getRules();
				 for(int i=0;i<rulesList.size();i++)
				 {
					 RulesAndLineItems rules=rulesList.get(i);
					 Rules rule=new Rules();
					 if(accountingRuleDTO.getAdhocRuleCreation() != null && !accountingRuleDTO.getAdhocRuleCreation())
					 {
						 if(rules.getId()!=null && rules.getId()!=0)
						 {

							 rule.setId(rules.getId());
							 if(ruleIdsList.size()>0)
							 { 
								 for(int j=0;j<rulesList.size();j++)
								 {
									 if(rulesList.get(j).getId()!=null && rulesList.get(j).getId()!=0)
									 {
										 for(int id=0;id<ruleIdsList.size();id++)
										 {
											 log.info("ruleIdsList.get(id) :"+ruleIdsList.get(id));
											 log.info("ruleDTO.get(i).getRule().getId() :"+rulesList.get(j).getId());
											 if(ruleIdsList.get(id).longValue()==rulesList.get(j).getId())
											 {
												 log.info("same");
												 ruleIdsList.remove(ruleIdsList.get(id));
											 }
										 }
									 }
								 }
							 }
							 log.info("ruleIdsList after:"+ruleIdsList);


						 }
						 // else

						 // {

						 for(int j=0;j<ruleIdsList.size();j++)
						 {
							 log.info("ruleIdsList.get(j).longValue() :"+ruleIdsList.get(j).longValue());
							 log.info("newGrp.getId() :"+ruleGroupToProceed.getId());
							 RuleGroupDetails ruleGroup=ruleGroupDetailsRepository.findByRuleGroupIdAndRuleId(ruleGroupToProceed.getId(),ruleIdsList.get(j).longValue());
							 log.info("ruleGrpId :"+ruleGrp);
							 log.info("");
							 if(ruleGroup!=null)
								 ruleGroupDetailsRepository.delete(ruleGroup);
						 }
					 }
					 if(rules.getId() != null)
						 rule.setId(rules.getId());
						 if(rules.getRuleCode()!=null && !rules.getRuleCode().isEmpty())
							 rule.setRuleCode(rules.getRuleCode());
						 if(rules.getCategory()!=null && !rules.getCategory().isEmpty())
							 rule.setCategory(rules.getCategory());
						 if(rules.getStartDate()!=null)
							 rule.setStartDate(rules.getStartDate());
						 if(rules.getEndDate()!=null)
							 rule.setEndDate(rules.getEndDate());
						 if(rules.getEnabledFlag()!=null)
							 rule.setEnabledFlag(rules.getEnabledFlag());
						 if(rules.getRuleType()!=null && !rules.getRuleType().isEmpty())
							 rule.setRuleType(rules.getRuleType());
						 if(rules.getCoa()!=null && !rules.getCoa().isEmpty())
						 {
							 ChartOfAccount coa = new ChartOfAccount();
							 coa = chartOfAccountRepository.findByTenantIdAndIdForDisplay(tenantId, rules.getCoa());
							 if(coa != null && coa.getId() != null)
							 {
								 rule.setcOA(coa.getId().toString());
							 }
						 }
						 if(rules.getSourceDataViewId()!=null)
						 {
							 //Kiran
							 String srcDVId=rules.getSourceDataViewId();
							 DataViews srcDV=dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, srcDVId);
							 rule.setSourceDataViewId(srcDV.getId());
						 }
						 //					 rule.setSourceDataViewId(rules.getSourceDataViewId());
						 if(rules.getReconDataSourceId() != null)
						 {
							 //String rcnDV=rules.getReconDataSourceId();
							 DataViews reconDV=dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, rules.getReconDataSourceId());
							 rule.setReconciliationViewId(reconDV.getId());
							// rule.setReconciliationViewId(rules.getReconDataSourceId() );
						 }
						 // rule.setTargetDataViewId(rules.getReconDataSourceId() );
						 if(rules.getAccountingStatus()!=null)
							 rule.setAccountingStatus(rules.getAccountingStatus());
						 if(rules.getReconciliationStatus()!=null)
							 rule.setReconciliationStatus(rules.getReconciliationStatus());
						 if(rules.getEnterCurrencyColId()!=null)
							 rule.setCurrencyColumnId(rules.getEnterCurrencyColId());
						 rule.setTenantId(tenantId);
						 rule.setCreatedBy(accountingRuleDTO.getCreatedBy());
						 if(rules.getId()==null)
						 {
							 rule.setCreationDate(ZonedDateTime.now());
						 }
						 else
							 rule.setCreationDate(accountingRuleDTO.getCreationDate()); 
						 rule.setLastUpdatedBy(userId);
						 rule.setLastUpdatedDate(ZonedDateTime.now());

						 Rules ruleID=rulesRepository.save(rule);



						 if(ruleID.getId()!=null && ruleGroupToProceed.getId()!=null)
						 {
							 log.info("rules.getId() :"+rules.getId());
							 if( rules.getId()==null)
							 {
								 log.info("ruleItem.getId() for adhoc in if:"+rules.getId());
								 log.info("accountingRuleDTO.getAdhocRuleCreation()"+accountingRuleDTO.getAdhocRuleCreation());
								 if(accountingRuleDTO.getAdhocRuleCreation()==true)
								 {
									 log.info("ruleID.getRuleType() :"+ruleID.getRuleType());
									 HashMap parameterSet = new HashMap<>();
									 parameterSet.put("param1",ruleGroupToProceed.getId());
									 log.info("ruleGrpId.getId() :"+ruleGroupToProceed.getId());
									 parameterSet.put("param2",ruleID.getId());
									 log.info("ruleID :"+ruleID.getId());
									 log.info("ruleGroupDTO.getLastUpdatedby() :"+accountingRuleDTO.getLastUpdatedBy());
									 ResponseEntity response= oozieService.jobIntiateForAcctAndRec(tenantId, userId, "Accounting", parameterSet,"ADHOC",request);
									 ErrorReport jobInitiation=new ErrorReport();
									 jobInitiation.setTaskName("Job Initiation");

									 log.info("response.getBody() :"+response.getBody());
									 String status=response.getBody().toString().substring(8).replaceAll("\\}", "");
									 log.info("status :"+status);
									 log.info("response.getstatusValue :"+response.getStatusCodeValue());


									 jobInitiation.setTaskStatus(status);
									 jobInitiation.setDetails(ruleID.getId()+"");
									 errorReport.add(jobInitiation);
								 }
							 }
							 RuleGroupDetails ruleGrpDetails=new RuleGroupDetails();
							 RuleGroupDetails ruleGrpDet=ruleGroupDetailsRepository.findByRuleGroupIdAndRuleId(ruleGroupToProceed.getId(),ruleID.getId());

							 if(ruleGrpDet!=null)
							 {
								 ruleGrpDet.setPriority(rules.getPriority());
								 ruleGrpDet.setEnabledFlag(rules.isAssignmentFlag());
								 ruleGroupDetailsRepository.save(ruleGrpDet);
								 //ruleGroupDetailsRepository.save(ruleGrpDet);
							 }
							 else
							 {
								 ruleGrpDetails.setRuleGroupId(ruleGroupToProceed.getId());
								 ruleGrpDetails.setRuleId(ruleID.getId());
								 ruleGrpDetails.setTenantId(tenantId);
								 if(rules.getPriority()>0)
								 {
									 ruleGrpDetails.setPriority(rules.getPriority());
								 }
								 else
								 {
									 int lastPriority = ruleGroupDetailsRepository.getLatestPriority(ruleGroupToProceed.getId());
									 ruleGrpDetails.setPriority(lastPriority+1);
								 }
								 
								 ruleGrpDetails.setCreatedBy(ruleID.getCreatedBy());
								 ruleGrpDetails.setLastUpdatedBy(ruleID.getLastUpdatedBy());
								 ruleGrpDetails.setEnabledFlag(rules.isAssignmentFlag());
								 ruleGrpDetails.setCreationDate(ZonedDateTime.now());
								 ruleGrpDetails.setLastUpdatedDate(ZonedDateTime.now());
								 RuleGroupDetails ruleGrpDets=ruleGroupDetailsRepository.save(ruleGrpDetails);
							 }
						 }

						 if(rulesList.get(i).getHeaderDerivationRules()!=null)
						 {
							 List<AcctRuleDerivationDTO> headerDerivationRulesList=rulesList.get(i).getHeaderDerivationRules();
							 log.info("rulesList.get(i).getHeaderDerivationRules()"+rulesList.get(i).getHeaderDerivationRules().size());
							 for(AcctRuleDerivationDTO headerDerivationRules:headerDerivationRulesList)
							 {
								 AcctRuleDerivations acctRuleDer=new AcctRuleDerivations();
								 if(headerDerivationRules.getId()!=null )
								 {
									 acctRuleDer.setId(headerDerivationRules.getId());
								 }
									 log.info("create acc header line");
									 //	 acctRuleDer.setAcctRuleActionId(actLinetypeId.getId()); not setting at rule level
									 if(headerDerivationRules.getCriteria()!=null && !headerDerivationRules.getCriteria().isEmpty())
										 acctRuleDer.setCriteria(headerDerivationRules.getCriteria());
									 if(headerDerivationRules.getOperator()!=null && !headerDerivationRules.getOperator().isEmpty())
										 acctRuleDer.setOperator(headerDerivationRules.getOperator());
									 if(headerDerivationRules.getValue()!=null && !headerDerivationRules.getValue().isEmpty())
										 acctRuleDer.setValue(headerDerivationRules.getValue());
									 if(headerDerivationRules.getDataViewColumn()!=null)
										 acctRuleDer.setDataViewColumn(headerDerivationRules.getDataViewColumn().toString());
									 if(headerDerivationRules.getAccountingReferencesCode()!=null && !headerDerivationRules.getAccountingReferencesCode().isEmpty())
									 {
										 acctRuleDer.setAccountingReferences(headerDerivationRules.getAccountingReferencesCode());
										 if(headerDerivationRules.getAccountingReferencesCode().toLowerCase().contains("ledger") && headerDerivationRules.getCriteria()!= null &&
												!headerDerivationRules.getCriteria().toLowerCase().contains("mapping"))
										 {
											 LedgerDefinition ledgerDef = new LedgerDefinition();
											 ledgerDef = ledgerDefinitionRepository.findByTenantIdAndIdForDisplay(tenantId,headerDerivationRules.getSegValue());
											 if(ledgerDef != null && ledgerDef.getId() != null)
											 {
												 acctRuleDer.setSegValue(ledgerDef.getId().toString());
											 }
										 }
										 else if(headerDerivationRules.getAccountingReferencesCode().toLowerCase().contains("ledger") && headerDerivationRules.getCriteria()!= null &&
													headerDerivationRules.getCriteria().toLowerCase().contains("mapping")){
											 acctRuleDer.setSegValue(headerDerivationRules.getSegValue());
										 }
										 else
										 {
											 if(headerDerivationRules.getSegValue()!=null && !headerDerivationRules.getSegValue().isEmpty())
												 acctRuleDer.setSegValue(headerDerivationRules.getSegValue()); 
										 }
									 }
										
									 if(headerDerivationRules.getConstantValue()!=null && !headerDerivationRules.getConstantValue().isEmpty())
										 acctRuleDer.setConstantValue(headerDerivationRules.getConstantValue());
									 if(headerDerivationRules.getMappingSetId()!=null)
										 acctRuleDer.setMappingSetId(headerDerivationRules.getMappingSetId());
									 if(headerDerivationRules.getType()!=null && !headerDerivationRules.getType().isEmpty())
										 acctRuleDer.setType(headerDerivationRules.getType());
									 acctRuleDer.setRuleId(ruleID.getId());
									 acctRuleDer.setCreatedBy(accountingRuleDTO.getCreatedBy());
									 acctRuleDer.setLastUpdatedBy(accountingRuleDTO.getCreatedBy());
									 acctRuleDer.setCreatedDate(ZonedDateTime.now());
									 acctRuleDer.setLastUpdatedDate(ZonedDateTime.now());
									 AcctRuleDerivations acctDer= acctRuleDerivationsRepository.save(acctRuleDer);
							 }
						 }
						 if(rulesList.get(i).getLineDerivationRules()!=null)
						 {
							 log.info("rulesList.get(i).getLineDerivationRules()"+rulesList.get(i).getLineDerivationRules().size());
							 List<LineItems> lineDerivationRulesList=rulesList.get(i).getLineDerivationRules();
							 List<AccountingLineTypes> existingLines = accountingLineTypesRepository.findByRuleId(ruleID.getId());
							 log.info("existingDerivations"+existingLines.size());
							 for(AccountingLineTypes acct : existingLines)
							 {
								 accountingLineTypesRepository.delete(acct.getId());
							 }
							 for(LineItems lineDerivationRules:lineDerivationRulesList)
							 {

								 AccountingLineTypes acctLineType=new AccountingLineTypes();
								 AccountingLineTypes actLinetypeId=new AccountingLineTypes();
								 if(lineDerivationRules.getId()!=null )
								 {
									  acctLineType.setId(lineDerivationRules.getId());
								 }
									 acctLineType.setTenantId(tenantId);
									 acctLineType.setRuleId(ruleID.getId());
									 acctLineType.setLineType(lineDerivationRules.getLineType());
									 acctLineType.setLineTypeDetail(lineDerivationRules.getLineTypeDetail());
									 acctLineType.setAmountColumnId(lineDerivationRules.getEnteredAmtColId());
									 //	 acctLineType.setDataViewId(lineDerivationRules.getSourceDataviewId());
									 acctLineType.setCreatedBy(accountingRuleDTO.getCreatedBy());
									 acctLineType.setLastUpdatedBy(accountingRuleDTO.getCreatedBy());
									 acctLineType.setCreationDate(ZonedDateTime.now());
									 acctLineType.setLastUpdatedDate(ZonedDateTime.now());
									 actLinetypeId=accountingLineTypesRepository.save(acctLineType);
								 log.info("actLinetypeId :"+actLinetypeId);
								 List<AcctRuleCondDTO> acctRuleCondDTOList=lineDerivationRules.getAccountingRuleConditions();
								 for(AcctRuleCondDTO acctRuleCondDTO:acctRuleCondDTOList)
								 {
									 AcctRuleConditions acctRuleCond=new AcctRuleConditions();

									 if(acctRuleCondDTO.getId()!=null )
									 {
										 acctRuleCond.setId(acctRuleCondDTO.getId());
									 }
									 acctRuleCond.setRuleId(ruleID.getId());
									 acctRuleCond.setRuleActionId(actLinetypeId.getId());
									 if(acctRuleCondDTO.getOpenBracket()!=null && !acctRuleCondDTO.getOpenBracket().isEmpty())
										 acctRuleCond.setOpenBracket(acctRuleCondDTO.getOpenBracket());
									 if(acctRuleCondDTO.getOperator()!=null && !acctRuleCondDTO.getOperator().isEmpty())
										 acctRuleCond.setOperator(acctRuleCondDTO.getOperator());
									 if(acctRuleCondDTO.getValue()!=null && !acctRuleCondDTO.getValue().isEmpty())
										 acctRuleCond.setValue(acctRuleCondDTO.getValue());
									 if(acctRuleCondDTO.getCloseBracket()!=null && !acctRuleCondDTO.getCloseBracket().isEmpty())
										 acctRuleCond.setCloseBracket(acctRuleCondDTO.getCloseBracket());
									 if(acctRuleCondDTO.getLogicalOperator()!=null && !acctRuleCondDTO.getLogicalOperator().isEmpty())
										 acctRuleCond.setLogicalOperator(acctRuleCondDTO.getLogicalOperator());
									 if(acctRuleCondDTO.getsViewColumnId()!=null)
										 acctRuleCond.setsViewColumnId(acctRuleCondDTO.getsViewColumnId());
									 if(acctRuleCondDTO.getFunc()!=null && !acctRuleCondDTO.getFunc().isEmpty())
										 acctRuleCond.setFunction(acctRuleCondDTO.getFunc());
									 acctRuleCond.setCreatedBy(accountingRuleDTO.getCreatedBy());
									 acctRuleCond.setLastUpdatedBy(accountingRuleDTO.getCreatedBy());
									 acctRuleCond.setCreatedDate(ZonedDateTime.now());
									 acctRuleCond.setLastUpdatedDate(ZonedDateTime.now());
									 AcctRuleConditions actCond= acctRuleConditionsRepository.save(acctRuleCond);
									 if(actCond!=null && actCond.getId()!=null)
									 {

									 }
									 else
									 {
										 ErrorReport ruleConditionSave=new ErrorReport();
										 ruleConditionSave.setTaskName("Acct Rule Condition Save");
										 ruleConditionSave.setTaskStatus("failure");
										 errorReport.add(ruleConditionSave);
									 }
								 }
								 if(lineDerivationRules.getAccountingRuleDerivations()!=null)
								 {
									 List<AcctRuleDerivationDTO> acctRuleDerDTOList=lineDerivationRules.getAccountingRuleDerivations();
								
									
									 for(AcctRuleDerivationDTO acctRuleDerDTO:acctRuleDerDTOList)
									 {
										 AcctRuleDerivations acctRuleDer=new AcctRuleDerivations();

										 if(acctRuleDerDTO.getId()!=null && acctRuleDerDTO.getId()!=0)
										 {
											 acctRuleDer.setId(acctRuleDerDTO.getId());
										 }
											 if(acctRuleDerDTO.getCriteria()!=null && !acctRuleDerDTO.getCriteria().isEmpty())
												 acctRuleDer.setCriteria(acctRuleDerDTO.getCriteria());
											 if(acctRuleDerDTO.getOperator()!=null && !acctRuleDerDTO.getOperator().isEmpty())
												 acctRuleDer.setOperator(acctRuleDerDTO.getOperator());
											 if(acctRuleDerDTO.getValue()!=null && !acctRuleDerDTO.getValue().isEmpty())
												 acctRuleDer.setValue(acctRuleDerDTO.getValue());
											 if(acctRuleDerDTO.getDataViewColumn()!=null)
												 acctRuleDer.setDataViewColumn(acctRuleDerDTO.getDataViewColumn().toString());
											 if(acctRuleDerDTO.getAccountingReferencesCode()!=null && !acctRuleDerDTO.getAccountingReferencesCode().isEmpty())
												 acctRuleDer.setAccountingReferences(acctRuleDerDTO.getAccountingReferencesCode());
											 if(acctRuleDerDTO.getConstantValue()!=null && !acctRuleDerDTO.getConstantValue().isEmpty())
												 acctRuleDer.setConstantValue(acctRuleDerDTO.getConstantValue());
											 if(acctRuleDerDTO.getMappingSetId()!=null)
												 acctRuleDer.setMappingSetId(acctRuleDerDTO.getMappingSetId());
											 if(acctRuleDerDTO.getSegValue()!=null && !acctRuleDerDTO.getSegValue().isEmpty())
												 acctRuleDer.setSegValue(acctRuleDerDTO.getSegValue()); 
											 if(acctRuleDerDTO.getType()!=null && !acctRuleDerDTO.getType().isEmpty())
												 acctRuleDer.setType(acctRuleDerDTO.getType());
											 acctRuleDer.setAcctRuleActionId(actLinetypeId.getId());
											 acctRuleDer.setRuleId(ruleID.getId());
											 acctRuleDer.setCreatedBy(accountingRuleDTO.getCreatedBy());
											 acctRuleDer.setLastUpdatedBy(accountingRuleDTO.getCreatedBy());
											 acctRuleDer.setCreatedDate(ZonedDateTime.now());
											 acctRuleDer.setLastUpdatedDate(ZonedDateTime.now());
											 AcctRuleDerivations acctDer= acctRuleDerivationsRepository.save(acctRuleDer);
											 if(acctDer!=null && acctDer.getId()!=null)
											 {

											 }
											 else
											 {
												 ErrorReport ruleDerSave=new ErrorReport();
												 ruleDerSave.setTaskName("Acct Rule Derivation Save");
												 ruleDerSave.setTaskStatus("failure");
												 errorReport.add(ruleDerSave);
											 }

									 }

								 }
							 }
						 }
				 }
				 
				 appModuleSummaryResource.updateAppModuleSummaryAfterRuleGroupIsCreated(ruleGroupToProceed.getId(), request);

			 }
			 else
			 {
				 ErrorReport ruleGroupSave=new ErrorReport();
				 ruleGroupSave.setTaskName("Rule Group Save");
				 ruleGroupSave.setTaskStatus("failure");
				 errorReport.add(ruleGroupSave);
			 }
			 return errorReport;


		 }  
		    

	    
	    @GetMapping("/getAccountingRuleDerivation")
		@Timed
	    public AccountingRuleDTO getAccountingRuleDerivation(@RequestParam("groupId") String groupId,HttpServletRequest request)
	    {
	    	log.info("Request Rest to get accounting rule conditions and derivations"+groupId);
	    	HashMap map=userJdbcService.getuserInfoFromToken(request);
	    	Long userId=Long.parseLong(map.get("userId").toString());
	    	Long tenantId= Long.parseLong(map.get("tenantId").toString());
	    	AccountingRuleDTO acctRuleDTO=new AccountingRuleDTO();
	    	RuleGroup ruleGrp=ruleGroupRepository.findByIdForDisplayAndTenantId(groupId,tenantId);
	    	Long ruleGrp_Id=ruleGrp.getId();

	    	if(ruleGrp!=null)
	    	{
	    		if(ruleGrp.getId() != null )
	    			acctRuleDTO.setId(ruleGrp.getIdForDisplay());
	    		if(ruleGrp.getName()!=null && !ruleGrp.getName().isEmpty())
	    			acctRuleDTO.setName(ruleGrp.getName());
	    		if(ruleGrp.getRulePurpose()!=null && !ruleGrp.getRulePurpose().isEmpty())
	    		{
	    			acctRuleDTO.setRulePurpose(ruleGrp.getRulePurpose());
	    			LookUpCode lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("RULE_GROUP_TYPE",ruleGrp.getRulePurpose(), ruleGrp.getTenantId());
	    			if(lookUpCode != null &&  lookUpCode.getMeaning() != null)
	    				acctRuleDTO.setMeaning(lookUpCode.getMeaning());
	    		}
	    		if(ruleGrp.getStartDate()!=null)
	    			acctRuleDTO.setStartDate(ruleGrp.getStartDate());
	    		if(ruleGrp.getEndDate()!=null)
	    			acctRuleDTO.setEndDate(ruleGrp.getEndDate());
	    		if(ruleGrp.isEnabledFlag()!=null)
	    			acctRuleDTO.setEnableFlag(ruleGrp.isEnabledFlag());
	    		if(ruleGrp.getReconciliationGroupId() != null)
	    		{
	    			RuleGroup rg = new RuleGroup ();
	    			rg = ruleGroupRepository.findOne(ruleGrp.getReconciliationGroupId());
	    			acctRuleDTO.setReconciliationGroupId(rg.getIdForDisplay());
	    			RuleGroup reconRuleGroup = new RuleGroup();
	    			reconRuleGroup = ruleGroupRepository.findOne(ruleGrp.getReconciliationGroupId());
	    			if(reconRuleGroup != null && reconRuleGroup.getName() != null)
	    			{
	    				acctRuleDTO.setReconciliationGroupName(reconRuleGroup.getName() );
	    			}
	    		}
	    		if(ruleGrp.getApprRuleGrpId()!=null)
	    		{
	    			
	    			RuleGroup appRuleGrp=ruleGroupRepository.findOne(ruleGrp.getApprRuleGrpId());
	    			if(appRuleGrp!=null && appRuleGrp.getName()!=null)
	    			{
	    				acctRuleDTO.setApprRuleGrpId(ruleGrp.getIdForDisplay());
	    				acctRuleDTO.setAppGroupName(appRuleGrp.getName());
	    			}
	    		}

	    		if(ruleGrp.getAccountingType()!=null)
	    		{
	    			acctRuleDTO.setAccountingTypeCode(ruleGrp.getAccountingType());
	    			LookUpCode lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("ACCOUNTING_TYPE",ruleGrp.getAccountingType(), ruleGrp.getTenantId());
	    			if(lookUpCode != null &&  lookUpCode.getMeaning() != null)
	    			{
	    				acctRuleDTO.setAccountingTypeMeaning( lookUpCode.getMeaning() );
	    			}
	    		}
	    		if(ruleGrp.getCreatedBy() != null)
	    			acctRuleDTO.setCreatedBy(ruleGrp.getCreatedBy());
	    		if(ruleGrp.getLastUpdatedBy() != null)
	    			acctRuleDTO.setLastUpdatedBy(ruleGrp.getLastUpdatedBy());
	    		if(ruleGrp.getCreationDate() != null)
	    			acctRuleDTO.setCreationDate(ruleGrp.getCreationDate() );
	    		if(ruleGrp.getLastUpdatedDate() != null)
	    			acctRuleDTO.setLastUpdatedDate(ruleGrp.getLastUpdatedDate() );


	    		List<RulesAndLineItems> rulesList=new ArrayList<RulesAndLineItems>();
	    		//List<RulesAndLineItems> rulesAndItems=new ArrayList<RulesAndLineItems>();
	    		List<RuleGroupDetails> ruleGrpDetailsList=ruleGroupDetailsRepository.findByRuleGroupIdOrderByPriorityAsc(ruleGrp_Id);
	    		if(ruleGrpDetailsList.size()>0)
	    		{
	    			HashMap<Long,DataViews> dataViewsMap = new HashMap<Long,DataViews>();
	    			
	    			for(int i=0;i<ruleGrpDetailsList.size();i++)
	    			{
	    				Rules rule=rulesRepository.findOne(ruleGrpDetailsList.get(i).getRuleId());

	    				RulesAndLineItems rules=new RulesAndLineItems();

	    				//setting rule group detail id
	    				RuleGroupDetails ruleGrpDetails=ruleGroupDetailsRepository.findByRuleGroupIdAndRuleId(ruleGrp_Id, rule.getId());
	    				if(ruleGrpDetails!=null)
	    					rules.setRuleGroupAssignId(ruleGrpDetails.getId());
	    				rules.setId(rule.getId());
	    				if(rule.getRuleCode()!=null && !rule.getRuleCode().isEmpty())
	    					rules.setRuleCode(rule.getRuleCode());
	    				if(rule.getCategory()!=null && !rule.getCategory().isEmpty())
	    					rules.setCategory(rule.getCategory());
	    				if(rule.getStartDate()!=null)
	    					rules.setStartDate(rule.getStartDate());
	    				if(rule.getEndDate()!=null)
	    					rules.setEndDate(rule.getEndDate());
	    				if(rule.isEnabledFlag()!=null)
	    					rules.setEnabledFlag(rule.isEnabledFlag());
	    				if(rule.getAccountingStatus()!=null)
	    					rules.setAccountingStatus(rule.getAccountingStatus());
	    				if(rule.getReconciliationStatus()!=null)
	    					rules.setReconciliationStatus(rule.getReconciliationStatus());

	    				/**************************************************************/
	    				//Rule Edit based on accounting process initiation status
	    				Object count = accountedSummaryRepository.fetchRecordCountByGroupIdAndRuleIdAndAccountingViewId(ruleGrp.getId(),rule.getId(),rule.getSourceDataViewId());
	    				log.info("count has value:"+count);
	    				if(count != null )
	    				{
	    					Long accountingCount = 0L;
	    					accountingCount = Long.valueOf(count.toString());
	    					if(accountingCount >0)
	    					{
	    						rules.setEditRule(false);
	    					}
	    					else
	    						rules.setEditRule(true);
	    				}
	    				/**************************************************************/


	    				if(rule.getRuleType()!=null && !rule.getRuleType().isEmpty())
	    				{
	    					rules.setRuleType(rule.getRuleType());
	    					LookUpCode lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("RULE_TYPE",rule.getRuleType(), rule.getTenantId());
	    					if(lookUpCode != null &&  lookUpCode.getMeaning() != null)
	    						rules.setRuleTypeMeaning( lookUpCode.getMeaning() );
	    				}

	    				if(rule.getcOA()!=null && !rule.getcOA().isEmpty())
	    				{
	    					
	    					ChartOfAccount coa = chartOfAccountRepository.findOne(Long.valueOf(rule.getcOA()));
	    					if(coa != null && coa.getName() != null)
	    					{
	    						rules.setCoa(coa.getIdForDisplay());
	    						rules.setCoaMeaning(coa.getName());
	    					}
	    					/*LookUpCode lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("CHART_OF_ACCOUNTS",rule.getCOA(), rule.getTenantId());
	    				if(lookUpCode != null && lookUpCode.getMeaning()!=null )
	    					rules.setCoaMeaning(lookUpCode.getMeaning());*/
	    				}
	    				if(rule.getSourceDataViewId() != null)
	    				{
	    					//List<HashMap> sColList = new ArrayList<HashMap>();
	    					//	sColList = dataViewsService.fetchDataViewAndColumnsByDvId(rule.getSourceDataViewId());
	    					DataViews dv = new DataViews();
	    					dv =dataViewsRepository.findOne(rule.getSourceDataViewId());
	    					rules.setSourceDataViewId(dv.getIdForDisplay());
	    					rules.setSourceDataViewName(dv.getDataViewDispName());

	    				}
	    				if(rule.getReconciliationViewId() != null)
	    				{
	    					//Kiran
	    					DataViews dv = new DataViews();
	    					dv =dataViewsRepository.findOne(rule.getReconciliationViewId());
	    					rules.setReconDataSourceId(dv.getIdForDisplay());
	    					rules.setReconDataSourceName(dv.getDataViewDispName());
	    				}
	    				if(rule.getCurrencyColumnId()!=null)
	    				{
	    					rules.setEnterCurrencyColId(rule.getCurrencyColumnId());
	    					DataViewsColumns dvCol = new DataViewsColumns();
	    					dvCol = dataViewsColumnsRepository.findOne(rule.getCurrencyColumnId());
	    					if(dvCol != null && dvCol.getColumnName() != null)
	    					{
	    						rules.setEnterCurrencyColName(dvCol.getColumnName());
	    					}

	    				}
	    				if(rule.getTenantId()!=null)
	    					rules.setTenantId(rule.getTenantId());
	    				if(ruleGrpDetailsList.get(i).isEnabledFlag() !=null)
	    					rules.setAssignmentFlag(ruleGrpDetailsList.get(i).isEnabledFlag());

	    				if(rule.getCreatedBy() != null)
	    					rules.setCreatedBy(rule.getCreatedBy());
	    				if(rule.getLastUpdatedBy() != null)
	    					rules.setLastUpdatedBy(rule.getLastUpdatedBy());
	    				if(rule.getCreationDate() != null)
	    					rules.setCreationDate(rule.getCreationDate() );
	    				if(rule.getLastUpdatedDate() != null)
	    					rules.setLastUpdatedDate(rule.getLastUpdatedDate() );



	    				List<AcctRuleDerivationDTO> headerDerivationRulesList=new ArrayList<AcctRuleDerivationDTO>();
	    				List<AcctRuleDerivations> acctRuleHeaderDerList=acctRuleDerivationsRepository.findByAcctRuleActionIdIsNullAndRuleId(ruleGrpDetailsList.get(i).getRuleId());
	    				for(AcctRuleDerivations acctRuleDer:acctRuleHeaderDerList)
	    				{

	    					AcctRuleDerivationDTO acctRuleDerDTO=new AcctRuleDerivationDTO();

	    					acctRuleDerDTO.setId(acctRuleDer.getId());
	    					if(acctRuleDer.getCriteria() != null)
	    					{
	    						acctRuleDerDTO.setCriteria(acctRuleDer.getCriteria());
	    						LookUpCode lookUpCode=lookUpCodeRepository.findByTenantIdAndLookUpCodeAndLookUpType(ruleGrp.getTenantId(),acctRuleDer.getCriteria(),"ACCOUNTING_CRITERIA_TYPES");
	    						if(lookUpCode != null &&lookUpCode.getMeaning()!=null)
	    							acctRuleDerDTO.setCriteriaMeaning(lookUpCode.getMeaning());
	    					}

	    					if(acctRuleDer.getOperator() != null)
	    					{
	    						acctRuleDerDTO.setOperator(acctRuleDer.getOperator());
	    						DataViewsColumns dvCol = new DataViewsColumns();
	    						dvCol = dataViewsColumnsRepository.findOne(Long.valueOf(acctRuleDer.getDataViewColumn()));
	    						if(dvCol != null && dvCol.getColDataType() != null)
	    						{
	    							LookUpCode lookUpCode=lookUpCodeRepository.findByTenantIdAndLookUpCodeAndLookUpType(ruleGrp.getTenantId(),acctRuleDer.getOperator(),dvCol.getColDataType());
	    							if(lookUpCode != null &&lookUpCode.getMeaning()!=null)
	    								acctRuleDerDTO.setOperatorMeaning(lookUpCode.getMeaning());
	    						}
	    					}

	    					acctRuleDerDTO.setValue(acctRuleDer.getValue());
	    					if(acctRuleDer.getDataViewColumn()!=null &&!acctRuleDer.getDataViewColumn().isEmpty())
	    					{
	    						acctRuleDerDTO.setDataViewColumn(Long.valueOf(acctRuleDer.getDataViewColumn()));//dataViewColumnName
	    						DataViewsColumns dvCol = new DataViewsColumns();
	    						dvCol = dataViewsColumnsRepository.findOne(Long.valueOf(acctRuleDer.getDataViewColumn()));
	    						if(dvCol != null && dvCol.getColumnName() != null)
	    						{
	    							acctRuleDerDTO.setDataViewColumnName(dvCol.getColumnName());
	    						}
	    						if(dvCol != null && dvCol.getColDataType() != null)
	    						{
	    							acctRuleDerDTO.setDataType(dvCol.getColDataType());
	    						}
	    					}

	    					if(acctRuleDer.getAccountingReferences()!=null && !acctRuleDer.getAccountingReferences().isEmpty())
	    					{
	    						acctRuleDerDTO.setAccountingReferencesCode(acctRuleDer.getAccountingReferences());
	    						LookUpCode lookUpCode=lookUpCodeRepository.findByTenantIdAndLookUpCodeAndLookUpType(ruleGrp.getTenantId(),acctRuleDer.getAccountingReferences(),"ACC_HEADER_LINE_TYPES");
	    						if(lookUpCode != null &&lookUpCode.getMeaning()!=null)
	    							acctRuleDerDTO.setAccountingReferencesMeaning(lookUpCode.getMeaning());
	    					}
	    					/*if(acctRuleDer.getConstantValue()!=null && !acctRuleDer.getConstantValue().isEmpty())
    						acctRuleDerDTO.setConstantValue(acctRuleDer.getConstantValue());
    					if(acctRuleDer.getMappingSetId()!=null)
    					{

    						acctRuleDerDTO.setMappingSetId(acctRuleDer.getMappingSetId());
    						MappingSet mappinSet=mappingSet.findOne(acctRuleDer.getMappingSetId());
    						if(mappinSet!=null)
    							acctRuleDerDTO.setMappingSetName(mappinSet.getName());
    					}*/
	    					/*if(acctRuleDer.getAccountingReferences() != null)
    					{
    						if(acctRuleDer.getAccountingReferences().equalsIgnoreCase("LEDGER_NAME")){
    							//from ledger table
    							LedgerDefinition ledgerDef = new LedgerDefinition();
    							ledgerDef = ledgerDefinitionRepository.findOne(Long.valueOf(acctRuleDer.getSegValue()));
    						if(ledgerDef != null && ledgerDef.getName() != null)
    							acctRuleDerDTO.setSegValue(ledgerDef.getName());
    						}
    						else if(acctRuleDer.getAccountingReferences().equalsIgnoreCase("SOURCE")){
    							//from lookups
    							LookUpCode lookUpCode=lookUpCodeRepository.findByTenantIdAndLookUpCodeAndLookUpType(ruleGrp.getTenantId(),acctRuleDer.getSegValue(),"SOURCE");
        						if(lookUpCode != null &&lookUpCode.getMeaning()!=null)
        							acctRuleDerDTO.setSegValue(lookUpCode.getMeaning());
    						}
    						else if(acctRuleDer.getAccountingReferences().equalsIgnoreCase("CATEGORY")){
    							//from lookups
    							LookUpCode lookUpCode=lookUpCodeRepository.findByTenantIdAndLookUpCodeAndLookUpType(ruleGrp.getTenantId(),acctRuleDer.getSegValue(),"CATEGORY");
        						if(lookUpCode != null &&lookUpCode.getMeaning()!=null)
        							acctRuleDerDTO.setSegValue(lookUpCode.getMeaning());
    						}

    					}*/
	    					if(acctRuleDer.getCriteria().equalsIgnoreCase("MAPPING_SET") )
	    					{
	    						if(acctRuleDer.getSegValue()!=null)
	    						{
	    							MappingSet mappinSet=mappingSetRepository.findOne(Long.valueOf(acctRuleDer.getSegValue()));
	    							if(mappinSet!=null)
	    							{
	    								acctRuleDerDTO.setSegValueMeaning(mappinSet.getName());
	    								acctRuleDerDTO.setSegValue(mappinSet.getId().toString());
	    							}
	    						}
	    					}
	    					else if( acctRuleDer.getCriteria().equalsIgnoreCase("CONSTANT") || acctRuleDer.getCriteria().equalsIgnoreCase("VIEW_COLUMN"))
	    					{
	    						if(acctRuleDer.getAccountingReferences() != null)
	    						{
	    							if(acctRuleDer.getAccountingReferences().equalsIgnoreCase("LEDGER_NAME")){
	    								//from ledger table
	    								LedgerDefinition ledgerDef = new LedgerDefinition();
	    								if(acctRuleDer.getSegValue() != null)
	    								ledgerDef = ledgerDefinitionRepository.findOne(Long.valueOf(acctRuleDer.getSegValue()));

	    								if(ledgerDef != null && ledgerDef.getName() != null)	
	    								{
	    									acctRuleDerDTO.setSegValueMeaning(ledgerDef.getName());
	    									
	    									acctRuleDerDTO.setSegValue(ledgerDef.getIdForDisplay());
	    								}

	    							}
	    							else if(acctRuleDer.getAccountingReferences().equalsIgnoreCase("SOURCE")){
	    								//from lookups
	    								LookUpCode lookUpCode=lookUpCodeRepository.findByTenantIdAndLookUpCodeAndLookUpType(ruleGrp.getTenantId(),acctRuleDer.getSegValue(),"SOURCE");
	    								if(lookUpCode != null &&lookUpCode.getMeaning()!=null)
	    								{
	    									acctRuleDerDTO.setSegValue(lookUpCode.getLookUpCode());
	    									acctRuleDerDTO.setSegValueMeaning(lookUpCode.getMeaning());
	    								}

	    							}
	    							else if(acctRuleDer.getAccountingReferences().equalsIgnoreCase("CATEGORY")){
	    								//from lookups
	    								LookUpCode lookUpCode=lookUpCodeRepository.findByTenantIdAndLookUpCodeAndLookUpType(ruleGrp.getTenantId(),acctRuleDer.getSegValue(),"CATEGORY");
	    								if(lookUpCode != null &&lookUpCode.getMeaning()!=null)
	    								{
	    									acctRuleDerDTO.setSegValueMeaning(lookUpCode.getMeaning());
	    									acctRuleDerDTO.setSegValue(lookUpCode.getLookUpCode());
	    								}

	    							}

	    						}
	    					}
	    					acctRuleDerDTO.setType(acctRuleDer.getType());
	    					acctRuleDerDTO.setRuleId(acctRuleDer.getRuleId());
	    					acctRuleDerDTO.setType(acctRuleDer.getType());
	    					headerDerivationRulesList.add(acctRuleDerDTO);


	    				}
	    				rules.setHeaderDerivationRules(headerDerivationRulesList);
	    				List<LineItems> lineDerivationRulesList=new ArrayList<LineItems>();
	    				List<AccountingLineTypes> acctLineTypesList=accountingLineTypesRepository.findByRuleId(rule.getId());
	    				for(AccountingLineTypes acctLineTypes:acctLineTypesList)
	    				{
	    					LineItems lineType=new LineItems();

	    					lineType.setId(acctLineTypes.getId());
	    					if(acctLineTypes.getLineType() != null)
	    					{
	    						lineType.setLineType(acctLineTypes.getLineType());
	    						LookUpCode lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("LINE_TYPE",acctLineTypes.getLineType(), rule.getTenantId());
	    						if(lookUpCode != null && lookUpCode.getMeaning() != null)
	    						{
	    							lineType.setLineTypeMeaning(lookUpCode.getMeaning());
	    						}
	    					}
	    					if(acctLineTypes.getLineTypeDetail() != null)
	    					{
	    						lineType.setLineTypeDetail(acctLineTypes.getLineTypeDetail());
	    						LookUpCode lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("ACCOUNTING_LINE_TYPES",acctLineTypes.getLineTypeDetail(), rule.getTenantId());
	    						if(lookUpCode != null && lookUpCode.getMeaning() != null)
	    						{
	    							lineType.setLineTypeDetailMeaning(lookUpCode.getMeaning());
	    						}
	    					}

	    					lineType.setRuleId(acctLineTypes.getRuleId());
	    					if(acctLineTypes.getAmountColumnId() != null)
	    					{
	    						lineType.setEnteredAmtColId(acctLineTypes.getAmountColumnId());
	    						DataViewsColumns dvCol = new DataViewsColumns();
	    						dvCol =dataViewsColumnsRepository.findOne(Long.valueOf(acctLineTypes.getAmountColumnId()));
	    						if(dvCol != null && dvCol.getColumnName() != null)
	    							lineType.setEnteredAmtColName( dvCol.getColumnName());
	    					}


	    					List<AcctRuleCondDTO> acctRuleCondDTOList=new ArrayList<AcctRuleCondDTO>();
	    					List<AcctRuleConditions> acctRuleCondList=acctRuleConditionsRepository.findByRuleActionId(acctLineTypes.getId());
	    					Integer seq = 0 ;
	    					for(AcctRuleConditions acctRuleCond:acctRuleCondList)
	    					{
	    						// AcctRuleConditions acctRuleCond=new AcctRuleConditions();
	    						AcctRuleCondDTO acctRuleCondDTO=new AcctRuleCondDTO();
	    						seq = seq+1;
	    						acctRuleCondDTO.setSequence(seq);
	    						acctRuleCondDTO.setId(acctRuleCond.getId());
	    						if(acctRuleCond.getOpenBracket()!=null && !acctRuleCond.getOpenBracket().isEmpty())
	    							acctRuleCondDTO.setOpenBracket(acctRuleCond.getOpenBracket());
	    						if(acctRuleCond.getsViewColumnId()!=null)
	    						{
	    							acctRuleCondDTO.setsViewColumnId(acctRuleCond.getsViewColumnId());
	    							DataViewsColumns dViewCol=dataViewsColumnsRepository.findOne(acctRuleCond.getsViewColumnId());
	    							if(dViewCol!=null)
	    								acctRuleCondDTO.setsViewColumnName(dViewCol.getColumnName());

	    							if(acctRuleCond.getOperator()!=null && !acctRuleCond.getOperator().isEmpty())
	    							{
	    								acctRuleCondDTO.setOperator(acctRuleCond.getOperator());
	    								DataViewsColumns dvCol = new DataViewsColumns();
	    								dvCol = dataViewsColumnsRepository.findOne(acctRuleCond.getsViewColumnId());
	    								if(dvCol != null)
	    								{
	    									if(dvCol.getColDataType() != null)
	    										acctRuleCondDTO.setDataType(dvCol.getColDataType());
	    									LookUpCode lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId(dvCol.getColDataType(),acctRuleCond.getOperator(), rule.getTenantId());
	    									if(lookUpCode != null && lookUpCode.getMeaning() != null)
	    									{
	    										acctRuleCondDTO.setOperatorMeaning(lookUpCode.getMeaning());
	    									}
	    								}
	    							}
	    						}
	    						if(acctRuleCond.getValue()!=null && !acctRuleCond.getValue().isEmpty())
	    							acctRuleCondDTO.setValue(acctRuleCond.getValue());
	    						if(acctRuleCond.getCloseBracket()!=null && !acctRuleCond.getCloseBracket().isEmpty())
	    							acctRuleCondDTO.setCloseBracket(acctRuleCond.getCloseBracket());
	    						if(acctRuleCond.getLogicalOperator()!=null && !acctRuleCond.getLogicalOperator().isEmpty())
	    						{
	    							acctRuleCondDTO.setLogicalOperator(acctRuleCond.getLogicalOperator());
	    							LookUpCode lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("LOGICAL_OPERATOR",acctRuleCond.getLogicalOperator(), rule.getTenantId());
	    							if(lookUpCode != null && lookUpCode.getMeaning() != null)
	    							{
	    								acctRuleCondDTO.setLogicalOperatorMeaning(lookUpCode.getMeaning() );
	    							}
	    						}



	    						if(acctRuleCond.getFunction()!=null && !acctRuleCond.getFunction().isEmpty())
	    							acctRuleCondDTO.setFunc(acctRuleCond.getFunction());


	    						acctRuleCondDTOList.add(acctRuleCondDTO);

	    					}

	    					List<AcctRuleDerivationDTO> acctRuleDerDTOList=new ArrayList<AcctRuleDerivationDTO>();
	    					List<AcctRuleDerivations> acctRuleDerList=acctRuleDerivationsRepository.findByAcctRuleActionId(acctLineTypes.getId());
	    					for(AcctRuleDerivations acctRuleDer:acctRuleDerList)
	    					{
	    						AcctRuleDerivationDTO acctRuleDerDTO=new AcctRuleDerivationDTO();

	    						acctRuleDerDTO.setId(acctRuleDer.getId());

	    						if(acctRuleDer.getAccountingReferences() != null)
	    						{
	    							acctRuleDerDTO.setAccountingReferencesCode(acctRuleDer.getAccountingReferences());
	    							Segments segment = segmentsRepository.findOne(Long.valueOf(acctRuleDer.getAccountingReferences()));
	    							if(segment != null && segment.getSegmentName() != null)
	    								acctRuleDerDTO.setAccountingReferencesMeaning(segment.getSegmentName());
	    						}


	    						if(acctRuleDer.getCriteria() != null)
	    						{
	    							acctRuleDerDTO.setCriteria(acctRuleDer.getCriteria());
	    							LookUpCode lookUpCode=lookUpCodeRepository.findByTenantIdAndLookUpCodeAndLookUpType(ruleGrp.getTenantId(),acctRuleDer.getCriteria(),"ACCOUNTING_CRITERIA_TYPES");
	    							if(lookUpCode != null &&lookUpCode.getMeaning()!=null)
	    								acctRuleDerDTO.setCriteriaMeaning(lookUpCode.getMeaning());
	    						}


	    						acctRuleDerDTO.setValue(acctRuleDer.getValue());
	    						if(acctRuleDer.getDataViewColumn()!=null &&!acctRuleDer.getDataViewColumn().isEmpty())
	    						{
	    							acctRuleDerDTO.setDataViewColumn(Long.valueOf(acctRuleDer.getDataViewColumn()));//dataViewColumnName
	    							DataViewsColumns dvCol = new DataViewsColumns();
	    							dvCol = dataViewsColumnsRepository.findOne(Long.valueOf(acctRuleDer.getDataViewColumn()));
	    							if(dvCol != null && dvCol.getColumnName() != null)
	    							{
	    								acctRuleDerDTO.setDataViewColumnName(dvCol.getColumnName());
	    							}
	    							if(dvCol != null && dvCol.getColDataType() != null)
	    							{
	    								acctRuleDerDTO.setDataType(dvCol.getColDataType());
	    							}
	    							if(acctRuleDer.getOperator()!= null)
	    							{
	    								acctRuleDerDTO.setOperator(acctRuleDer.getOperator());
	    								LookUpCode lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId(dvCol.getColDataType(),acctRuleDer.getOperator(), rule.getTenantId());
	    								if(lookUpCode != null && lookUpCode.getMeaning() != null)
	    								{
	    									acctRuleDerDTO.setOperatorMeaning(lookUpCode.getMeaning());
	    								}
	    							}



	    						}

	    						if(acctRuleDer.getAccountingReferences()!=null && !acctRuleDer.getAccountingReferences().isEmpty())
	    						{
	    							acctRuleDerDTO.setAccountingReferencesCode(acctRuleDer.getAccountingReferences());
	    							LookUpCode lookUpCode=lookUpCodeRepository.findByTenantIdAndLookUpCodeAndLookUpType(ruleGrp.getTenantId(),acctRuleDer.getAccountingReferences(),rule.getcOA());
	    							if(lookUpCode != null &&lookUpCode.getMeaning()!=null)
	    								acctRuleDerDTO.setAccountingReferencesMeaning(lookUpCode.getMeaning());
	    						}
	    						/*if(acctRuleDer.getConstantValue()!=null && !acctRuleDer.getConstantValue().isEmpty())
	    						acctRuleDerDTO.setConstantValue(acctRuleDer.getConstantValue());
	    					if(acctRuleDer.getMappingSetId()!=null)
	    					{

	    						acctRuleDerDTO.setMappingSetId(acctRuleDer.getMappingSetId());
	    						MappingSet mappinSet=mappingSetRepository.findOne(acctRuleDer.getMappingSetId());
	    						if(mappinSet!=null)
	    							acctRuleDerDTO.setMappingSetName(mappinSet.getName());
	    					}
	    					acctRuleDerDTO.setSegValue(acctRuleDer.getSegValue());*/
	    						if(acctRuleDer.getCriteria().equalsIgnoreCase("MAPPING_SET") )
	    						{
	    							if(acctRuleDer.getSegValue()!=null)
	    							{
	    								MappingSet mappinSet=mappingSetRepository.findOne(Long.valueOf(acctRuleDer.getSegValue()));
	    								if(mappinSet!=null)
	    								{
	    									acctRuleDerDTO.setSegValue(acctRuleDer.getSegValue());
	    									acctRuleDerDTO.setSegValueMeaning(mappinSet.getName());
	    								}

	    							}
	    						}
	    						else 
	    						{
	    							acctRuleDerDTO.setSegValue(acctRuleDer.getSegValue());

	    						}
	    						acctRuleDerDTO.setType(acctRuleDer.getType());
	    						acctRuleDerDTO.setRuleId(acctRuleDer.getRuleId());

	    						acctRuleDerDTOList.add(acctRuleDerDTO);


	    					}

	    					lineType.setAccountingRuleConditions(acctRuleCondDTOList);
	    					lineType.setAccountingRuleDerivations(acctRuleDerDTOList);
	    					lineDerivationRulesList.add(lineType);
	    				}

	    				rules.setLineDerivationRules(lineDerivationRulesList);
	    				rulesList.add(rules);
	    			}

	    			acctRuleDTO.setRules(rulesList);
	    		}
	    	}
	    	return acctRuleDTO;
	    }
	    
	    
	    /**
	     * author:ravali
	     * API to get ruleGroupName by data view id and rule purpose
	     * @param dvId
	     * @param purpose
	     * @return
	     */
	    @GetMapping("/getRuleGroupNameByDvIdAndPurpose")
		@Timed
	    public List<LinkedHashMap> getRuleGroupNameByDvIdAndPurpose(HttpServletRequest request,@RequestParam String dvId,@RequestParam String purpose)
	    {
	    	HashMap map=userJdbcService.getuserInfoFromToken(request);
	    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
	    	DataViews sdv = dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, dvId);
	    	Long viewId = sdv.getId();
	    	log.info("Rest Request to get ruleGrp names by dvId :"+viewId +"and purpose :"+purpose);
	    	List<Object[]> ruleGroupNameList=ruleGroupRepository.findRuleGrpNamesByDataViewIdAndPurPose(viewId,purpose);
	    	List<LinkedHashMap> finalMap=new ArrayList<LinkedHashMap>();
	    	for(int i=0;i<ruleGroupNameList.size();i++)
	    	{
	    		LinkedHashMap result=new LinkedHashMap();
	    		result.put("groupId", ruleGroupNameList.get(i)[0]);
	    		result.put("groupName", ruleGroupNameList.get(i)[1]);
	    		finalMap.add(result);
	    		
	    	}
			return finalMap;
	    }
	    
	    /**
	     * author :ravali
	     * fetch data display view name and ids
	     * @param ruleGroupId
	     * @param tenantId
	     * @return
	     */
	    @GetMapping("/getDVNameByRuleGroupId")
	  		@Timed
	  	    public List<LinkedHashMap> getDVNameByRuleGroupId(HttpServletRequest request,@RequestParam String ruleGroupId)//,@RequestParam Long tenantId)
	  	    {
	    	HashMap map=userJdbcService.getuserInfoFromToken(request);
  	    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
	  	    	log.info("Rest Request to get data view names by ruleGroupId :"+ruleGroupId +" for tenant: "+tenantId);
	  	    	
	  	    	List<LinkedHashMap> finalMap=new ArrayList<LinkedHashMap>();
	  	    	RuleGroup rg =new RuleGroup();
	  	    	rg = ruleGroupRepository.findByIdForDisplayAndTenantId(ruleGroupId, tenantId);
	  	    	List<Long> ruleIds=ruleGroupDetailsRepository.fetchByRuleGroupIdAndTenantId(rg.getId(),tenantId);
	  	    	log.info("ruleIds :"+ruleIds);
	  	  
	  	    	List<BigInteger> RulesSrcDv=rulesRepository.fetchDistictSrcViewIdsByRuleId( ruleIds);
	  	    	log.info("RulesSrcDv :"+RulesSrcDv);
	  	    	List<BigInteger> RulesTrgDv=rulesRepository.fetchDistictTargetViewIdsByRuleId( ruleIds);
	  	    	log.info("RulesTrgDv :"+RulesTrgDv);
	  	    	//adding trgDav to srcDvList

	  	    	for(int i=0;i<RulesTrgDv.size();i++)
	  	    	{
	  	    		if(!RulesSrcDv.contains(RulesTrgDv.get(i)))
	  	    		{
	  	    			RulesSrcDv.add(RulesTrgDv.get(i));
	  	    		}

	  	    	}
	  	    	log.info("finalDvList :"+RulesSrcDv);
	  	  
	  	    	for(int i=0;i<RulesSrcDv.size();i++)
	  	    	{
	  	    		LinkedHashMap dvMap=new LinkedHashMap();
	  	    		if(RulesSrcDv.get(i) != null)
	  	    		{
	  	    			DataViews dv=dataViewsRepository.findOne(RulesSrcDv.get(i).longValue());
		  	    		dvMap.put("id", dv.getIdForDisplay());
		    			dvMap.put("dataViewDispName", dv.getDataViewDispName());
		    			finalMap.add(dvMap);	
	  	    		}
	  	    		
	  	    	}
	  	    	
	  	    	
	  	    	
	  			return finalMap;
	  	    }
	    
	    
	    /**
	     * Author Kiran
	     * @param ruleGrpId
	     * @return
	     */
	    @GetMapping("/getCoaDetailsByGrpId")
  		@Timed
	    public HashMap getCoaNameAndId(@RequestParam String ruleGrpId,HttpServletRequest request)
	    {
	    	log.info("Rest request to get the Coa Details based on Group Id: "+ruleGrpId);
	    	HashMap map=userJdbcService.getuserInfoFromToken(request);
  	    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
	    	RuleGroup rg=ruleGroupRepository.findByIdForDisplayAndTenantId(ruleGrpId, tenantId);
	    	List<RuleGroupDetails> ruleGrpDetails = ruleGroupDetailsRepository.findByRuleGroupId(rg.getId());
	    	if(ruleGrpDetails!=null && ruleGrpDetails.size()>0)
	    	{
	    		Long ruleId=ruleGrpDetails.get(0).getRuleId();
	    		if(ruleId != null)
	    		{
	    			Rules rules= rulesRepository.findById(ruleId);
		    		if(rules!=null && rules.getcOA() != null)
		    		{
		    			String coaId=rules.getcOA();
		    			ChartOfAccount coa=chartOfAccountRepository.findById(Long.valueOf(coaId));
		    			HashMap resultCoa= new HashMap();
		    			if(coa != null )
		    			{
		    				resultCoa.put("coaId", coa.getIdForDisplay());
			    			if( coa.getName() != null)
			    			resultCoa.put("coaName", coa.getName());
			    			resultCoa.put("segmentSeparator", coa.getSegmentSeperator());
		    			}
		    			
		    			return resultCoa;
		    		}
	    		}
	    		
	    	}
			return null;
	    }
	    /**
	     * Authpr : shobha
	     * @param ruleGrpDetailsDTO
	     * @return
	     * @throws ParseException
	     */
	    @PutMapping("/updatePriority")
  		@Timed
	    public HashMap<String, String> updatePriority(@RequestBody List<RuleGroupDetailsDTO> ruleGrpDetailsDTO) throws ParseException
	    {
	    	log.info("Rest request to update the priority from detail"+ruleGrpDetailsDTO);
	    	HashMap<String, String> result = new HashMap<String, String>();
	    	int cnt = 0;
	    	for(int i =0;i<ruleGrpDetailsDTO.size();i++)
	    	{
				RuleGroupDetails rgd = new RuleGroupDetails();
	    		rgd = ruleGroupDetailsRepository.findOne(ruleGrpDetailsDTO.get(i).getAssignmentId());
	    		if(rgd != null && rgd.getId() != null)
	    			rgd.setPriority(ruleGrpDetailsDTO.get(i).getPriority());
	    		ruleGroupDetailsRepository.save(rgd);
	    		cnt = cnt+1;
	    	}
	    	if(cnt == ruleGrpDetailsDTO.size())
	    	result.put("updated","success" );
	    	else
	    		result.put("updated","Failed" );
	    	return result;
	    }

}
	    

