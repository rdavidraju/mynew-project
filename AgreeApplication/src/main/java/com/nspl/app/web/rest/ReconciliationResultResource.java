package com.nspl.app.web.rest;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
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
import com.nspl.app.domain.DataViews;
import com.nspl.app.domain.DataViewsColumns;
import com.nspl.app.domain.FileTemplateLines;
import com.nspl.app.domain.LookUpCode;
import com.nspl.app.domain.ReconciliationDuplicateResult;
import com.nspl.app.domain.ReconciliationResult;
import com.nspl.app.domain.RuleGroup;
import com.nspl.app.domain.TenantConfigModules;
import com.nspl.app.repository.AppModuleSummaryRepository;
import com.nspl.app.repository.ApplicationProgramsRepository;
import com.nspl.app.repository.DataMasterRepository;
import com.nspl.app.repository.DataViewsColumnsRepository;
import com.nspl.app.repository.DataViewsRepository;
import com.nspl.app.repository.FileTemplateLinesRepository;
import com.nspl.app.repository.FormConfigRepository;
import com.nspl.app.repository.LookUpCodeRepository;
import com.nspl.app.repository.ReconciliationDuplicateResultRepository;
import com.nspl.app.repository.ReconciliationResultRepository;
import com.nspl.app.repository.RuleConditionsRepository;
import com.nspl.app.repository.RuleGroupDetailsRepository;
import com.nspl.app.repository.RuleGroupRepository;
import com.nspl.app.repository.RulesRepository;
import com.nspl.app.repository.TenantConfigModulesRepository;
import com.nspl.app.service.AccountingDataService;
import com.nspl.app.service.DashBoardV4Service;
import com.nspl.app.service.DataViewsService;
import com.nspl.app.service.FileExportService;
import com.nspl.app.service.FileService;
import com.nspl.app.service.FormConfigService;
import com.nspl.app.service.OozieService;
import com.nspl.app.service.PropertiesUtilService;
import com.nspl.app.service.ReconciliationResultService;
import com.nspl.app.service.UserJdbcService;
import com.nspl.app.web.rest.dto.ErrorReport;
import com.nspl.app.web.rest.dto.ErrorReporting;
import com.nspl.app.web.rest.dto.ManualRecDTO;
import com.nspl.app.web.rest.dto.ManualUnRecByColumnDTO;
import com.nspl.app.web.rest.dto.ManualUnReconDTO;
import com.nspl.app.web.rest.dto.RWQDataFetchDTO;
import com.nspl.app.web.rest.dto.RWQDetailInfoDTO;
import com.nspl.app.web.rest.dto.ReconCountNAmountsDTO;
import com.nspl.app.web.rest.dto.ReconRefTransactionsDTO;
import com.nspl.app.web.rest.dto.ReconSummaryDTO;
import com.nspl.app.web.rest.dto.ReconciledDTO;
import com.nspl.app.web.rest.dto.SuggestedPostDTO;
import com.nspl.app.web.rest.dto.SuggestedPostingDTO;
import com.nspl.app.web.rest.dto.UnReconciledDTO;
import com.nspl.app.web.rest.dto.ViewIdRowIdDTO;
import com.nspl.app.web.rest.util.HeaderUtil;
import com.nspl.app.web.rest.util.PaginationUtil;
/**
 * REST controller for managing ReconciliationResult.
 */
@RestController
@RequestMapping("/api")
public class ReconciliationResultResource {

    private final Logger log = LoggerFactory.getLogger(ReconciliationResultResource.class);

    private static final String ENTITY_NAME = "reconciliationResult";
        
    private final ReconciliationResultService reconciliationResultService;
    
    @Inject
    RuleGroupDetailsRepository ruleGroupDetailsRepository;
    
    @Inject
    RulesRepository rulesRepository;
    
    @Inject
    ReconciliationResultRepository reconciliationResultRepository;
    
    @Inject
    DataMasterRepository dataMasterRepository;
    
    @Inject
    RuleGroupRepository ruleGroupRepository;
    
    @Inject 
    DataViewsColumnsRepository dataViewsColumnsRepository;
    
    @Inject
    DataViewsRepository dataViewsRepository;
    
    @Inject
    DataViewsResource dataViewsResource;
    
    @Inject
    FileTemplateLinesRepository fileTemplateLinesRepository;
    
    @Inject
    OozieService oozieService;
    
    @Inject
    TenantConfigModulesRepository tenantConfigModulesRepository;
    
    @Inject
    FormConfigRepository formConfigRepository;
    
    @Inject
	PropertiesUtilService propertiesUtilService;
    
    @Inject
    FileService fileService;
    
    @Inject
    UserJdbcService userJdbcService;
    
    @Inject
    FormConfigService formConfigService;
    
    @Inject
    LookUpCodeRepository lookUpCodeRepository;
    
    @Inject
    private Environment env;
    
    @Inject
    AppModuleSummaryRepository appModuleSummaryRepository;
    
    @Inject
    ReconciliationDuplicateResultRepository reconciliationDuplicateResultRepository;
    
    @Inject
    AccountingDataService accountingDataService;
    
    @Inject
    ApplicationProgramsRepository applicationProgramsRepository;
    
    @Inject
    RuleConditionsRepository ruleConditionsRepository;
    
    @Inject
    FileExportService fileExportService;

    @Inject
    DashBoardV4Service dashBoardV4Service;
    
    @Inject
    DataViewsService dataViewsService;
    
    @PersistenceContext(unitName="default")
	private EntityManager em;
	
	
	 /**
     * Author: Shiva
     * Description: Api to fetch columns Aligns
     * @param viewName
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
	@GetMapping("/getReconColumnAlignmentInfo")
    @Timed
    public HashMap getReconColumnAlignmentInfo(HttpServletRequest request, @RequestParam String groupId, 
    		@RequestParam String viewId, @RequestParam String sourceOrTarget, 
    		@RequestParam String status,@RequestParam String type, @RequestParam (value = "ruleId", required=false) Long ruleId,
    		@RequestParam (value = "groupBy", required=false) String groupBy) throws ClassNotFoundException, SQLException {
    	log.info("Rest api to fetching Recon view columns alignments details for the view id: "+viewId);
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		RuleGroup rg = ruleGroupRepository.findByIdForDisplayAndTenantId(groupId, tenantId);
		DataViews dv = dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, viewId);
    	HashMap finalMap = new HashMap();
    	finalMap.put("viewId", viewId);
    	List<HashMap> cols = new ArrayList<HashMap>();
    	if("source".equalsIgnoreCase(sourceOrTarget))
    	{
    		if(ruleId != null){
    			cols = reconciliationResultService.getRecColsAlignInfo(dv.getId(), rg.getId(), tenantId, "source", status,type,ruleId, groupBy);
    		} else{
    			cols = reconciliationResultService.getRecColsAlignInfo(dv.getId(), rg.getId(), tenantId, "source", status,type,null, groupBy);
    		}
    	}
    	else if("target".equalsIgnoreCase(sourceOrTarget))
    	{
    		if(ruleId != null){
    			cols = reconciliationResultService.getRecColsAlignInfo(dv.getId(), rg.getId(), tenantId, "target", status,type,ruleId, groupBy);
    		} else{
    			cols = reconciliationResultService.getRecColsAlignInfo(dv.getId(), rg.getId(), tenantId, "target", status,type,null, groupBy);
    		}
    	}
    	finalMap.put("columns", cols);
    	return finalMap;
	}

    public ReconciliationResultResource(ReconciliationResultService reconciliationResultService) {
        this.reconciliationResultService = reconciliationResultService;
    }

    /**
     * POST  /reconciliation-results : Create a new reconciliationResult.
     *
     * @param reconciliationResult the reconciliationResult to create
     * @return the ResponseEntity with status 201 (Created) and with body the new reconciliationResult, or with status 400 (Bad Request) if the reconciliationResult has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/reconciliation-results")
    @Timed
    public ResponseEntity<ReconciliationResult> createReconciliationResult(@RequestBody ReconciliationResult reconciliationResult) throws URISyntaxException {
        log.debug("REST request to save ReconciliationResult : {}", reconciliationResult);
        if (reconciliationResult.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new reconciliationResult cannot already have an ID")).body(null);
        }
        ReconciliationResult result = reconciliationResultService.save(reconciliationResult);
        return ResponseEntity.created(new URI("/api/reconciliation-results/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /reconciliation-results : Updates an existing reconciliationResult.
     *
     * @param reconciliationResult the reconciliationResult to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated reconciliationResult,
     * or with status 400 (Bad Request) if the reconciliationResult is not valid,
     * or with status 500 (Internal Server Error) if the reconciliationResult couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/reconciliation-results")
    @Timed
    public ResponseEntity<ReconciliationResult> updateReconciliationResult(@RequestBody ReconciliationResult reconciliationResult) throws URISyntaxException {
        log.debug("REST request to update ReconciliationResult : {}", reconciliationResult);
        if (reconciliationResult.getId() == null) {
            return createReconciliationResult(reconciliationResult);
        }
        ReconciliationResult result = reconciliationResultService.save(reconciliationResult);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, reconciliationResult.getId().toString()))
            .body(result);
    }

    /**
     * GET  /reconciliation-results : get all the reconciliationResults.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of reconciliationResults in body
     */
    @GetMapping("/reconciliation-results")
    @Timed
    public ResponseEntity<List<ReconciliationResult>> getAllReconciliationResults(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of ReconciliationResults");
        Page<ReconciliationResult> page = reconciliationResultService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/reconciliation-results");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /reconciliation-results/:id : get the "id" reconciliationResult.
     *
     * @param id the id of the reconciliationResult to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the reconciliationResult, or with status 404 (Not Found)
     */
    @GetMapping("/reconciliation-results/{id}")
    @Timed
    public ResponseEntity<ReconciliationResult> getReconciliationResult(@PathVariable Long id) {
        log.debug("REST request to get ReconciliationResult : {}", id);
        ReconciliationResult reconciliationResult = reconciliationResultService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(reconciliationResult));
    }

    /**
     * DELETE  /reconciliation-results/:id : delete the "id" reconciliationResult.
     *
     * @param id the id of the reconciliationResult to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/reconciliation-results/{id}")
    @Timed
    public ResponseEntity<Void> deleteReconciliationResult(@PathVariable Long id) {
        log.debug("REST request to delete ReconciliationResult : {}", id);
        reconciliationResultService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/reconciliation-results?query=:query : search for the reconciliationResult corresponding
     * to the query.
     *
     * @param query the query of the reconciliationResult search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/reconciliation-results")
    @Timed
    public ResponseEntity<List<ReconciliationResult>> searchReconciliationResults(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of ReconciliationResults for query {}", query);
        Page<ReconciliationResult> page = reconciliationResultService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/reconciliation-results");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    
    /** Author: Shiva
     * 	Purpose: Fetching RWQ Header Parameters List
	 *  Params: tenantId, groupId
	 *  Result: HashMap with RWQ header parameters list
	 *  Date: 01-12-2017
     * @throws ParseException 
     * @throws SQLException 
     * @throws ClassNotFoundException 
     */
    @GetMapping("/getReconHeaderParamsList")
    @Timed
    public HashMap getReconHeaderParamsList(HttpServletRequest request,@RequestParam String groupId)
    {
    	log.info("Rest API for fetch RWQ Header Parameters List for "+ "rule group id: "+ groupId);
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	HashMap finalMap = new HashMap();
    	RuleGroup rg = ruleGroupRepository.findByIdForDisplayAndTenantId(groupId, tenantId);
    	Long ruleGroupId = rg.getId();
    	RuleGroup ruleGrp = ruleGroupRepository.findOne(ruleGroupId);
    	if(ruleGrp != null)
    	{
    		List<HashMap> source = new ArrayList<HashMap>();
    		List<HashMap> target = new ArrayList<HashMap>();
    		log.info("Rule Group Name: "+ ruleGrp.getName());
        	finalMap.put("tenantId", tenantId);
        	finalMap.put("ruleGroupId", groupId);
        	finalMap.put("ruleGroupName", ruleGrp.getName());
        	
        	// Fetching Distinct Source and Target View Ids
        	HashMap<String, List<BigInteger>> distinctViewIdMap = reconciliationResultService.getDistinctDVIdsforRuleGrp(ruleGroupId, tenantId);
        	List<BigInteger> distSrcIds = distinctViewIdMap.get("sourceViewIds");
        	List<BigInteger> distTargetIds = distinctViewIdMap.get("targeViewIds");
        	log.info("Tentnt Id: "+tenantId+"Group Id: "+groupId+", Source View Ids: "+distSrcIds+", Target View Ids: "+distTargetIds);
        	List<Long> ruleIds = ruleGroupDetailsRepository.fetchByRuleGroupIdAndTenantId(ruleGroupId, tenantId);
        	if(distSrcIds.size()>0)
        	{
        		for(BigInteger srcViewId : distSrcIds)
        		{
        			HashMap sourceMap = new HashMap();
        			List<HashMap> innerTargetViews = new ArrayList<HashMap>();
        			DataViews dv = dataViewsRepository.findOne(srcViewId.longValue());
        			if(dv != null)
        			{
        				sourceMap.put("viewId", dv.getIdForDisplay());
        				sourceMap.put("viewName", dv.getDataViewDispName());
        				List<HashMap> srcDateQualifiers = dataViewsService.getDateQualifiers(srcViewId.longValue());
        				sourceMap.put("srcDateQualifiers", srcDateQualifiers);
        				// Fetching Inner Target Views
        				List<BigInteger> innerTrgtViews = rulesRepository.fetchDistinctTargetViewIdsBySourceId(srcViewId.longValue(), tenantId, ruleIds);
        				if(innerTrgtViews.size()>0)
        				{
        					for(BigInteger innerTrgtViewId : innerTrgtViews)
        					{
        						HashMap innerTargetView = new HashMap();
        						DataViews innerTargetDv = dataViewsRepository.findOne(innerTrgtViewId.longValue());
        						if(innerTargetDv != null)
        						{
        							innerTargetView.put("viewId", innerTargetDv.getIdForDisplay());
        							innerTargetView.put("viewName", innerTargetDv.getDataViewDispName());
        	        				List<HashMap> trDateQualifiers = dataViewsService.getDateQualifiers(innerTrgtViewId.longValue());
        	        				innerTargetView.put("trDateQualifiers", trDateQualifiers);
        							innerTargetViews.add(innerTargetView);	// Adding target HashMap with respect to source
        							target.add(innerTargetView);	// Adding inner target HashMap to final targets list
        						}
        					}
        				}
        				sourceMap.put("innerTarget", innerTargetViews);
        			}
                	source.add(sourceMap);
        		}
        	}
        	finalMap.put("source", source);
        	finalMap.put("target", target);
    	}
    	log.info("getReconHeaderParamsList: "+finalMap);
    	return finalMap;
    }    
    
    @PostMapping("/getReconGroupingSummaryInfo")
    @Timed
    public HashMap getReconGroupingSummaryInfo(HttpServletRequest request,@RequestBody ReconSummaryDTO params) throws SQLException
    {
    	HashMap finalMap = new HashMap();
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	String periodFactor =  params.getPeriodFactor();
    	
		Long pageNumber = params.getPageNumber();
		Long pageSize = params.getPageSize();
		Long limit =  (pageNumber * pageSize + 1) - 1; // Limit, Page Size
		
		RuleGroup rg = ruleGroupRepository.findByIdForDisplayAndTenantId(params.getGroupId(), tenantId);
		Long groupId = rg.getId();
		DataViews sdv = dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, params.getsViewId());
		Long sViewId = sdv.getId();
		
		DataViews tdv = dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, params.gettViewId());
		Long tViewId = tdv.getId();
		
		String ascOrDesc = "asc";
		
    	List<HashMap> filters = params.getFilters();
    	List<String> groupingColumns = new ArrayList<String>();
    	HashMap grpCols = new HashMap();
		String currQuailfier = reconciliationResultService.getViewColumnQualifier(BigInteger.valueOf(sViewId), "CURRENCYCODE");
		String groupingQuery = "";
		List<String> filterCols = new ArrayList<String>();
    	if(filters!= null && filters.size()>0)
    	{
    		for(HashMap hm : filters)
    		{
    			if("rule_code".equalsIgnoreCase(hm.get("columnName").toString()) || "recon_job_reference".equalsIgnoreCase(hm.get("columnName").toString()) || "final_status".equalsIgnoreCase(hm.get("columnName").toString()))
    			{
    				groupingColumns.add(hm.get("columnName").toString());
    				grpCols.put(hm.get("columnName").toString(), hm.get("columnName").toString());
    				filterCols.add(hm.get("columnName").toString());
    			}
    			else if("final_action_date".equalsIgnoreCase(hm.get("columnName").toString()))
    			{
    				groupingColumns.add("Date(final_action_date)");
    				grpCols.put("Date(final_action_date)", "final_action_date");
    				filterCols.add("final_action_date");
    			}
    			else if("Period".equalsIgnoreCase(hm.get("displayName").toString()))
    			{
    				groupingColumns.add("Date(dv.`"+hm.get("columnName").toString()+"`)");
    				grpCols.put("Date(dv.`"+hm.get("columnName").toString()+"`)", hm.get("columnName").toString());
    				grpCols.put(hm.get("columnName").toString(), hm.get("columnName").toString());
    				filterCols.add(hm.get("columnName").toString());
    			}
    			else
    			{
    				if("DATE".equalsIgnoreCase(hm.get("dataType").toString()))
    				{
        				groupingColumns.add("DATE(dv.`"+hm.get("columnName").toString()+"`)");    				
        				grpCols.put("DATE(dv.`"+hm.get("columnName").toString()+"`)", hm.get("columnName").toString());
        				grpCols.put(hm.get("columnName").toString(), hm.get("columnName").toString());
        				filterCols.add(hm.get("columnName").toString());
    				}
    				else
    				{
        				groupingColumns.add("dv.`"+hm.get("columnName").toString()+"`");
        				grpCols.put("dv.`"+hm.get("columnName").toString()+"`", hm.get("columnName").toString());
        				grpCols.put(hm.get("columnName").toString(), hm.get("columnName").toString());
        				filterCols.add(hm.get("columnName").toString());
    				}
    			}
    		}
    	}
    	
		groupingColumns.add("dv.`"+currQuailfier+"`");
		grpCols.put("dv.`"+currQuailfier+"`", currQuailfier);
		grpCols.put(currQuailfier, currQuailfier);
		filterCols.add(currQuailfier);
		String reconGrpQuery = "";
		String grpByCols = "";
		for(int i=0; i<groupingColumns.size(); i++)
		{
			if("rule_code".equalsIgnoreCase(groupingColumns.get(i)))
			{
				if(i == groupingColumns.size()-1)
				{
					groupingQuery = groupingQuery + " rl.rule_code rule_code ";
					grpByCols = grpByCols + "`"+ grpCols.get(groupingColumns.get(i).toString())+"`";
				}
				else
				{
					groupingQuery = groupingQuery + " rl.rule_code rule_code, ";
					grpByCols = grpByCols + "`"+ grpCols.get(groupingColumns.get(i).toString())+"`, ";
				}
			}
			else if("recon_job_reference".equalsIgnoreCase(groupingColumns.get(i)))
			{
				if(i == groupingColumns.size()-1)
				{
					groupingQuery = groupingQuery + " recon.recon_job_reference recon_job_reference ";
					grpByCols = grpByCols + "`"+ grpCols.get(groupingColumns.get(i).toString())+"`";
				}
				else
				{
					groupingQuery = groupingQuery + "recon.recon_job_reference recon_job_reference, ";
					grpByCols = grpByCols + "`"+ grpCols.get(groupingColumns.get(i).toString())+"`, ";
				}
			}
			else if("final_status".equalsIgnoreCase(groupingColumns.get(i)))
			{
				if(i == groupingColumns.size()-1)
				{
					groupingQuery = groupingQuery + " lc.meaning final_status ";
					grpByCols = grpByCols + " lc.meaning ";
				}
				else
				{
					groupingQuery = groupingQuery + "lc.meaning final_status, ";
					grpByCols = grpByCols + " lc.meaning, ";
				}	
			}
			else
			{
				if(i == groupingColumns.size()-1)
				{
					groupingQuery = groupingQuery + groupingColumns.get(i).toString() + " `"+ grpCols.get(groupingColumns.get(i).toString())+"`";
					grpByCols = grpByCols + "`"+ grpCols.get(groupingColumns.get(i).toString())+"`";
				}
				else
				{
					groupingQuery = groupingQuery + groupingColumns.get(i).toString()+ " `"+ grpCols.get(groupingColumns.get(i).toString()) + "`, ";
					grpByCols = grpByCols + "`"+ grpCols.get(groupingColumns.get(i).toString())+"`, ";
				}
			}
		}
		String groupingColumnsQuery = "";
		for(int i=0; i<filterCols.size(); i++)
		{
			if("rule_code".equalsIgnoreCase(filterCols.get(i).toString()))
			{
				if(i == filterCols.size()-1)
				{
					reconGrpQuery = reconGrpQuery + " (CASE WHEN recon_src.rule_code IS NULL THEN 'Manual' ELSE recon_src.rule_code end) rule_code ";
					groupingColumnsQuery = groupingColumnsQuery + " recon_src.rule_code ";
				}
				else
				{
					reconGrpQuery = reconGrpQuery + " (CASE WHEN recon_src.rule_code IS NULL THEN 'Manual' ELSE recon_src.rule_code end) rule_code, ";
					groupingColumnsQuery = groupingColumnsQuery + " recon_src.rule_code, ";
				}
			}
			else if("recon_job_reference".equalsIgnoreCase(filterCols.get(i).toString()))
			{
				if(i == filterCols.size()-1)
				{
					reconGrpQuery = reconGrpQuery + " (CASE WHEN Substring(recon_src.recon_job_reference, 1, 7) = 'MANUAL_' THEN 'Manual' ELSE recon_src.recon_job_reference end) recon_job_reference ";
					groupingColumnsQuery = groupingColumnsQuery + " recon_src.recon_job_reference, ";
				}
				else
				{
					reconGrpQuery = reconGrpQuery + " (CASE WHEN Substring(recon_src.recon_job_reference, 1, 7) = 'MANUAL_' THEN 'Manual' ELSE recon_src.recon_job_reference end) recon_job_reference, ";
					groupingColumnsQuery = groupingColumnsQuery + " recon_src.recon_job_reference, ";
				}
			}
			else if("final_status".equalsIgnoreCase(filterCols.get(i).toString()))
			{
				if(i == filterCols.size()-1)
				{
					reconGrpQuery = reconGrpQuery + " (CASE WHEN recon_src.final_status IS NULL THEN 'Not Required' ELSE recon_src.final_status end) final_status ";
					groupingColumnsQuery = groupingColumnsQuery + " recon_src.final_status, ";
				}
				else
				{
					reconGrpQuery = reconGrpQuery + " (CASE WHEN recon_src.final_status IS NULL THEN 'Not Required' ELSE recon_src.final_status end) final_status, ";
					groupingColumnsQuery = groupingColumnsQuery + " recon_src.final_status, ";
				}
			}
			else
			{
				if(i == filterCols.size()-1)
				{
					reconGrpQuery = reconGrpQuery + "recon_src.`"+filterCols.get(i).toString()+"` ";
					groupingColumnsQuery = groupingColumnsQuery + "recon_src.`"+filterCols.get(i).toString()+"` ";
				}
				else
				{
					reconGrpQuery = reconGrpQuery + "recon_src.`"+filterCols.get(i).toString()+"`, ";
					groupingColumnsQuery = groupingColumnsQuery + "recon_src.`"+filterCols.get(i).toString()+"`, ";
				}
			}
		}
		
    	if("reconciled".equalsIgnoreCase(params.getStatus()))
    	{
    		log.info("Fetching reconciled grouping summary info.");
    		finalMap = reconciliationResultService.getReconciledSummaryInfo(periodFactor, groupId, params.getRangeFrom(), params.getRangeTo(), tenantId, sViewId, tViewId, pageNumber, pageSize, groupingQuery, reconGrpQuery, grpCols, grpByCols, groupingColumnsQuery);
    	}
    	else if("unReconciled".equalsIgnoreCase(params.getStatus()))
    	{
    		log.info("Fetching un-reconciled grouping summary info.");
    		finalMap = reconciliationResultService.getUnReconGroupingInfo(groupingQuery, sViewId, periodFactor, params.getRangeFrom(), params.getRangeTo(), groupId, ascOrDesc, pageNumber, pageSize, grpCols, grpByCols);
    	}
    	return finalMap;
    }
    
    
    @PostMapping("/getReconCountAndAmounts")
    @Timed
    public List<HashMap> getReconCountAndAmounts(HttpServletRequest request,@RequestBody ReconCountNAmountsDTO params) throws ClassNotFoundException, SQLException, ParseException, java.text.ParseException{
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	params.setTenantId(tenantId);
    	DataViews sdv = dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, params.getsViewId());
    	DataViews tdv = dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, params.gettViewId());
    	RuleGroup rg = ruleGroupRepository.findByIdForDisplayAndTenantId(params.getRuleGroupId(), tenantId);
    	Long groupId = rg.getId();
    	Long sViewId = sdv.getId();
    	Long tViewId = tdv.getId();
    	List<HashMap> finalList = new ArrayList<HashMap>();
    	String dateQualifierType = params.getDateQualifierType();
    	log.info("Rest api for fetching reconciliation summary info for the tenant id: "+ tenantId+", group id: "+ groupId+", source view id: "+ sViewId+", target view id: "+ tViewId);
    	String ascOrDesc = "asc";
    	String sPeriodFactor =  params.getPeriodFactor();
    	String sColumnName = "";
    	
    	Boolean currencyQualifier = false;
    	
		Long pageNumber = params.getPageNumber();
		Long pageSize = params.getPageSize();
		
		Long limit =  (pageNumber * pageSize + 1) - 1;
		log.info("Page Limit: " + limit + ", Page Number : " + pageNumber);
    	
    	// Fetching group by TRUE columns information
    	if("columnName".equalsIgnoreCase(params.getGroupBy()))
    	{
    		List<DataViewsColumns> dvCols = dataViewsColumnsRepository.findByDataViewIdAndGroupByIsTrue(sViewId);
    		if(dvCols.size()>0)
    		{
    			if("reconciled".equalsIgnoreCase(params.getStatus()))
    			{
    				if(params.getGroupByColumnName() != null)
    				{
        				HashMap finalMap = new HashMap();
        				sColumnName = "`"+params.getGroupByColumnName()+"`";
    					finalMap = reconciliationResultService.getReconGroupingSummaryInfo(params.getGroupBy(), groupId, sPeriodFactor, params.getRangeFrom(), params.getRangeTo(), tenantId, sViewId, tViewId, sColumnName, dateQualifierType, limit, pageSize);
        				finalMap.put("groupBy", params.getGroupByColumnName());
        				finalMap.put("displayName", params.getGroupByColumnName());
        				finalList.add(finalMap);
    				}
    				else
    				{
            			for(DataViewsColumns dvColumn : dvCols)
            			{
            				// log.info("Time Stamp 1: "+System.currentTimeMillis());
            				if("CURRENCYCODE".equalsIgnoreCase(dvColumn.getQualifier()))
            				{
            					currencyQualifier = true;
            				}
            				HashMap finalMap = new HashMap();
            				if("File Template".equalsIgnoreCase(dvColumn.getRefDvType())) 
            				{
            					FileTemplateLines ftl = fileTemplateLinesRepository.findOne(Long.parseLong(dvColumn.getRefDvColumn()));
            					if(ftl != null)
            					{
            						sColumnName = "`"+ftl.getColumnAlias()+"`";
            						finalMap = reconciliationResultService.getReconGroupingSummaryInfo(params.getGroupBy(), groupId, sPeriodFactor, params.getRangeFrom(), params.getRangeTo(), tenantId, sViewId, tViewId, sColumnName, dateQualifierType, limit, pageSize);
            						finalMap.put("groupBy", ftl.getColumnAlias());
            					}
            				}
            				else if("Data View".equalsIgnoreCase(dvColumn.getRefDvType()) || dvColumn.getRefDvType() == null)
            				{
            					sColumnName = "`"+dvColumn.getColumnName()+"`";
            					finalMap = reconciliationResultService.getReconGroupingSummaryInfo(params.getGroupBy(), groupId, sPeriodFactor, params.getRangeFrom(), params.getRangeTo(), tenantId, sViewId, tViewId, sColumnName, dateQualifierType, limit, pageSize);
            					finalMap.put("groupBy", dvColumn.getColumnName());
            				}
            				finalMap.put("displayName", dvColumn.getColumnName());
            				finalList.add(finalMap);
            				// log.info("Time Stamp 2: "+System.currentTimeMillis());
            			}
                		if(currencyQualifier == false)
                		{
                			HashMap finalMap2 = new HashMap();
                			String currQualifier = reconciliationResultService.getViewColumnQualifier(BigInteger.valueOf(sViewId), "CURRENCYCODE");
                			sColumnName = "`"+currQualifier+"`";
                			finalMap2 = reconciliationResultService.getReconGroupingSummaryInfo(params.getGroupBy(), groupId, sPeriodFactor, params.getRangeFrom(), params.getRangeTo(), tenantId, sViewId, tViewId, sColumnName,dateQualifierType, limit, pageSize);
                			finalMap2.put("groupBy", currQualifier);
                			finalMap2.put("displayName", "Currency");
                			finalList.add(finalMap2);
                		}		
    				}
    			}
    			else if("unReconciled".equalsIgnoreCase(params.getStatus()))
    			{
    				if(params.getGroupByColumnName() != null)
    				{
    					sColumnName = "`"+params.getGroupByColumnName()+"`";
    					HashMap finalMap = new HashMap();
    					finalMap = reconciliationResultService.getUnReconGroupingSummaryInfo(groupId, sViewId, tViewId, params.getPeriodFactor(), sColumnName, params.getRangeFrom(), params.getRangeTo(), params.getGroupBy(), ascOrDesc, limit, pageSize);			
        				finalMap.put("groupBy", params.getGroupByColumnName());
        				finalMap.put("displayName", params.getGroupByColumnName());
        				finalList.add(finalMap);
    				}
    				else
    				{
            			for(DataViewsColumns dvColumn : dvCols)
            			{
            				if("CURRENCYCODE".equalsIgnoreCase(dvColumn.getQualifier()))
            				{
            					currencyQualifier = true;
            				}
            				HashMap finalMap = new HashMap();
            				if("File Template".equalsIgnoreCase(dvColumn.getRefDvType())) 
            				{
            					FileTemplateLines ftl = fileTemplateLinesRepository.findOne(Long.parseLong(dvColumn.getRefDvColumn()));
            					if(ftl != null)
            					{
            						sColumnName = "`"+ftl.getColumnAlias()+"`";
            						finalMap = reconciliationResultService.getUnReconGroupingSummaryInfo(groupId, sViewId, tViewId, params.getPeriodFactor(), sColumnName, params.getRangeFrom(), params.getRangeTo(), params.getGroupBy(), ascOrDesc, limit, pageSize);
            						finalMap.put("groupBy", ftl.getColumnAlias());
            					}
            				}
            				else if("Data View".equalsIgnoreCase(dvColumn.getRefDvType()) || dvColumn.getRefDvType() == null)
            				{
            					sColumnName = "`"+dvColumn.getColumnName()+"`";
            					finalMap = reconciliationResultService.getUnReconGroupingSummaryInfo(groupId, sViewId, tViewId, params.getPeriodFactor(), sColumnName, params.getRangeFrom(), params.getRangeTo(), params.getGroupBy(), ascOrDesc, limit, pageSize);
            					finalMap.put("groupBy", dvColumn.getColumnName());
            				}
            				finalMap.put("displayName", dvColumn.getColumnName());
            				finalList.add(finalMap);
            			}
                		if(currencyQualifier == false)
                		{
                			HashMap finalMap2 = new HashMap();
                			String currQualifier = reconciliationResultService.getViewColumnQualifier(BigInteger.valueOf(sViewId), "CURRENCYCODE");
                			sColumnName = "`"+currQualifier+"`";
                			finalMap2 = reconciliationResultService.getReconGroupingSummaryInfo(params.getGroupBy(), groupId, sPeriodFactor, params.getRangeFrom(), params.getRangeTo(), tenantId, sViewId, tViewId, sColumnName, dateQualifierType, limit, pageSize);
                        	finalMap2.put("displayName", "Currency");
                        	finalMap2.put("groupBy", currQualifier);
                			finalList.add(finalMap2);
                		}
    				}
    			}
    		}
    	}
    	
    	HashMap finalMap = new HashMap();
    	if("reconciled".equalsIgnoreCase(params.getStatus()))
    	{
    		log.info("Fetching Reconciled Grouping Summary Info");
    		if(!"columnName".equalsIgnoreCase(params.getGroupBy()))
    		{
            	finalMap = reconciliationResultService.getReconGroupingSummaryInfo(params.getGroupBy(), groupId, sPeriodFactor, params.getRangeFrom(), params.getRangeTo(), tenantId, sViewId, tViewId, sColumnName, dateQualifierType, limit, pageSize);
            	finalList.add(finalMap);    			
    		}
    	}
    	else if("suggestion".equalsIgnoreCase(params.getStatus()))
    	{
    		log.info("Fetching Suggestion data Grouping Summary Info");
    		if(!"columnName".equalsIgnoreCase(params.getGroupBy()))
    		{
            	finalMap = reconciliationResultService.getReconGroupingSummaryInfo(params.getGroupBy(), groupId, params.getPeriodFactor(), params.getRangeFrom(), params.getRangeTo(), tenantId, sViewId, tViewId, sColumnName, dateQualifierType, limit, pageSize);
            	finalList.add(finalMap);	
    		}
    	}
    	else if("unReconciled".equalsIgnoreCase(params.getStatus()))
    	{
    		if("days".equalsIgnoreCase(params.getGroupBy()))
    		{
    			sColumnName = "Date(`"+params.getsColumnName()+"`)";
    			log.info("Fetching Un-Reconciled grouping information for group days");
    			ascOrDesc = "desc";
       			log.info("Fetching Un Reconciled Grouping Summary Info");
        		finalMap = reconciliationResultService.getUnReconGroupingSummaryInfo(groupId, sViewId, tViewId, params.getPeriodFactor(), sColumnName, params.getRangeFrom(), params.getRangeTo(), params.getGroupBy(), ascOrDesc, limit, pageSize);
        		finalMap.put("displayName", "Period");
        		finalMap.put("groupBy", params.getsColumnName());
        		finalList.add(finalMap);
    		}
    	}
    	return finalList;
    }
    
    
    @GetMapping("/createTempTables")
    public HashMap createTempTables(HttpServletRequest request,@RequestParam String sViewId, @RequestParam String tViewId)
    {
    	log.info("REST API for creating temporary tables.");
    	HashMap finalMap = new HashMap();
/*    	
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	DataViews sdv = dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, sViewId);
    	DataViews tdv = dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, tViewId);
		DataViews sdvEntity = dataViewsRepository.findOne(sdv.getId());
		DataViews tdvsEntity = dataViewsRepository.findOne(tdv.getId());
		reconciliationResultService.createTempTable(sdv.getDataViewName().toLowerCase());
		reconciliationResultService.createTempTable(tdv.getDataViewName().toLowerCase());*/
    	return finalMap;
    }
    
    @PostMapping("/getReconciledData")
    @Timed
    public HashMap getReconciledData(HttpServletRequest request, @RequestBody ReconciledDTO params) throws NumberFormatException, SQLException
    {
    	HashMap finalMap = new HashMap();
    	
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	
    	RuleGroup rg = ruleGroupRepository.findByIdForDisplayAndTenantId(params.getGroupId(), tenantId);
    	Long groupId = rg.getId();

    	DataViews sdv = dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, params.getsViewId());
    	Long sViewId = sdv.getId();
    	
    	DataViews tdv = dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, params.gettViewId());
    	Long tViewId = tdv.getId();
    	String filterQueryFinal = "";
    	
    	Long pageNumber = params.getPageNumber();
    	Long pageSize = params.getPageSize();
		Long limit = 0L;
		limit = (pageNumber * pageSize + 1)-1;
		
		String searchQuery = "";
		String sortQuery = "";
		String approvalLeftJoin = "";
		String srcGrpCols = "";
		String dvGrpCols = "";
		String dv2GrpCols = "";
		HashMap grpCols = new HashMap();
		if(params.getSelectAll() == false)
		{
    		if(params.getFilters() != null && params.getFilters().size()>0)
    		{
    			List<HashMap> inputs = (List<HashMap>) params.getFilters().get("inputs");
    			HashMap info = (HashMap) params.getFilters().get("info");
    			// int k=0;
    			for(Object key : info.keySet())
    			{
    				String colName = key.toString();
    				String dataType = info.get(colName).toString();
    				if("rule_code".equalsIgnoreCase(colName))
    				{
        				srcGrpCols = srcGrpCols + " (CASE WHEN recon_src.rule_code IS NULL THEN 'Manual' ELSE recon_src.rule_code end) rule_code, ";
        				dvGrpCols = dvGrpCols + "rl.rule_code rule_code, ";
        				dv2GrpCols = dv2GrpCols + " rule_code, ";
    				}
    				else if("recon_job_reference".equalsIgnoreCase(colName))
    				{
    					srcGrpCols = srcGrpCols + " (CASE WHEN Substring(recon_src.recon_job_reference, 1, 7) = 'MANUAL_' THEN 'Manual' ELSE recon_src.recon_job_reference end) recon_job_reference, ";
        				dvGrpCols = dvGrpCols + " recon.recon_job_reference recon_job_reference, ";
        				dv2GrpCols = dv2GrpCols + " recon_job_reference, ";
    				}
    				else
    				{
        				srcGrpCols = srcGrpCols + "recon_src.`"+colName+"`, ";
        				dvGrpCols = dvGrpCols + "dv.`"+colName+"` "+colName+", ";
        				dv2GrpCols = dv2GrpCols + "dv.`"+colName+"`, ";
    				}
    			}
    			
    			for(int j=0; j<inputs.size(); j++)
    			{
    		    	String filterQuery = "";
    				HashMap hm = inputs.get(j);
    				int i=0;
    				for(Object key : hm.keySet())
    				{
    					i = i+1;
    					String columnName = key.toString();
    					String value = hm.get(columnName).toString();
    					String dataType = info.get(columnName).toString();
    					if(i == hm.size())
    					{
    						if("rule_code".equalsIgnoreCase(columnName))
    						{
        						filterQuery = filterQuery + "rl.`"+columnName+"` = '"+value+"'";
        						grpCols.put(columnName, columnName);    							
    						}
    						else if("recon_job_reference".equalsIgnoreCase(columnName))
    						{
        						filterQuery = filterQuery + "recon.`"+columnName+"` = '"+value+"'";
        						grpCols.put(columnName, columnName);
    						}
    						else if("DATE".equalsIgnoreCase(dataType))
    						{
        						filterQuery = filterQuery + "DATE(dv.`"+columnName+"`) = '"+value+"'";
        						grpCols.put("DATE(dv.`"+columnName+"`)", columnName);
    						}
    						else
    						{
        						filterQuery = filterQuery + "dv.`"+columnName+"` = '"+value+"'";
        						grpCols.put(columnName, columnName);
    						}
    					}
    					else
    					{
    						if("rule_code".equalsIgnoreCase(columnName))
    						{
        						filterQuery = filterQuery + "rl.`"+columnName+"` = '"+value+"' AND ";
        						grpCols.put(columnName, columnName);
    						}
    						else if("recon_job_reference".equalsIgnoreCase(columnName))
    						{
        						filterQuery = filterQuery + "recon.`"+columnName+"` = '"+value+"' AND ";
        						grpCols.put(columnName, columnName);
    						}
    						else if("DATE".equalsIgnoreCase(dataType))
    						{
        						filterQuery = filterQuery + "DATE(dv.`"+columnName+"`) = '"+value+"' AND ";
        						grpCols.put("DATE(dv.`"+columnName+"`)", columnName);
    						}
    						else
    						{
        						filterQuery = filterQuery + "dv.`"+columnName+"` = '"+value+"' AND ";
        						grpCols.put(columnName, columnName);
    						}
    					}
    				}
    				filterQuery = "("+filterQuery+")";
    				if(j == inputs.size()-1)
    				{
    					filterQueryFinal = filterQueryFinal + filterQuery;
    				}
    				else
    				{
    					filterQueryFinal = filterQueryFinal + filterQuery+ " OR ";
    				}
    			}
    			filterQueryFinal = " AND ("+filterQueryFinal+") ";
    		}
		}
		finalMap = reconciliationResultService.getReconciledDataWithReconRef(tenantId, groupId, sViewId, tViewId, params.getPeriodFactor(), 
				params.getRangeFrom(), params.getRangeTo(), limit, pageSize, srcGrpCols, approvalLeftJoin, filterQueryFinal, searchQuery, sortQuery, params.getSortBy(), grpCols, dvGrpCols, dv2GrpCols);
    
		List<HashMap> source = new ArrayList<HashMap>();
    	HashMap  s1 = new HashMap();
		s1.put("field", "srcCount");
		s1.put("header", "Transaction Count");
		s1.put("align", "center");
		s1.put("width", "150px");
		s1.put("colId", "src_count");
		s1.put("dataType", "INTEGER");
		source.add(s1);
		HashMap s2 = new HashMap();
		s2.put("field", "srcAmount");
		s2.put("header", "Reconciled Amount");
		s2.put("align", "right");
		s2.put("width", "150px");
		s2.put("colId", "src_amount_sum");
		s2.put("dataType", "DECIMAL");
		source.add(s2);
		HashMap s3 = new HashMap();
		s3.put("field", "srcTolAmount");
		s3.put("header", "Variance Amount");
		s3.put("align", "right");
		s3.put("width", "150px");
		s3.put("colId", "src_tolerance_amount");
		s3.put("dataType", "DECIMAL");
		source.add(s3);
		
    	List<HashMap> target = new ArrayList<HashMap>();
    	HashMap  t1 = new HashMap();
    	t1.put("field", "trCount");
    	t1.put("header", "Transaction Count");
    	t1.put("align", "center");
    	t1.put("width", "150px");
    	t1.put("colId", "tr_count");
    	t1.put("dataType", "INTEGER");
		target.add(t1);
		HashMap t2 = new HashMap();
		t2.put("field", "trAmount");
		t2.put("header", "Reconciled Amount");
		t2.put("align", "right");
		t2.put("width", "150px");
		t2.put("colId", "tr_amount_sum");
		t2.put("dataType", "DECIMAL");
		target.add(t2);
		HashMap t3 = new HashMap();
		t3.put("field", "trTolAmount");
		t3.put("header", "Variance Amount");
		t3.put("align", "right");
		t3.put("width", "150px");
		t3.put("colId", "tr_tolerance_amount");
		t3.put("dataType", "DECIMAL");
		target.add(t3);
		
    	finalMap.put("source", source);
    	finalMap.put("target", target);
    	
		return finalMap;
    }
    
    
    @PostMapping("/getReconciledTransactionsRWQ")
    @Timed
    public HashMap getReconData(HttpServletRequest request,@RequestParam(value = "pageNumber", required=false) Long pageNumber, @RequestParam(value = "pageSize", required=false) Long pageSize, 
    @RequestBody RWQDetailInfoDTO params) throws SQLException
    {
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	HashMap finalMap = new HashMap();
    	List<LinkedHashMap> finalList = new ArrayList<LinkedHashMap>();

    	RuleGroup rg = ruleGroupRepository.findByIdForDisplayAndTenantId(params.getGroupId(), tenantId);
    	Long groupId = rg.getId();

    	DataViews sdv = dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, params.getsViewId());
    	Long sViewId = sdv.getId();
    	
    	DataViews tdv = dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, params.gettViewId());
    	Long tViewId = tdv.getId();
    	List<HashMap> srcHeader = new ArrayList<HashMap>();
    	HashMap groupingColumn = new HashMap();
    	
    	// Building Search Query
    	String searchQuery = "";
    	if(params.getSearchWord() != null && params.getSearchWord().length()>0)
    	{
    		String searchWord = params.getSearchWord();
    		searchQuery = " AND (recon_src.grp_column like '%"+searchWord+"%' OR recon_src.recon_reference like '%"+searchWord+"%' OR recon_src.src_count like '%"+searchWord+"%'"
    				+" OR recon_src.src_amount_sum like '%"+searchWord+"%' OR recon_tr.tr_count like '%"+searchWord+"%' OR recon_tr.tr_amount_sum like '%"+searchWord+"%'"
    				+" OR recon_src.src_tolerance_amount like '%"+searchWord+"%' OR recon_tr.tr_tolerance_amount like '%"+searchWord+"%')";
    	}
    	// Sorting Query
    	String sortingQuery = "";
    	if(params.getSortByColumnId() != null && params.getSortByColumnId().length()>0)
    	{
    		sortingQuery = " `"+params.getSortByColumnId()+"` ";
    	}
    	
    	RWQDataFetchDTO keyValues = params.getKeyValues();
    	List<HashMap> filters = keyValues.getFilters();
    	ManualUnRecByColumnDTO columnvalues = keyValues.getColumnValues();
    	 // columnvalues.getFilters();
		Long limit = 0L;
		limit = (pageNumber * pageSize + 1)-1;
		String periodFactor = params.getPeriodFactor();

    	groupingColumn.put("dataType", "VARCHAR");
    	groupingColumn.put("align", "left");
    	groupingColumn.put("width", "150px");
    	groupingColumn.put("field", "groupingCol");
    	groupingColumn.put("colId", "grouping_col");
    	String currQualifier = reconciliationResultService.getViewColumnQualifier(BigInteger.valueOf(sViewId), "CURRENCYCODE");
    	String sGroupByValues = "";
    	if("rules".equalsIgnoreCase(params.getGroupBy()))
		{
    		groupingColumn.put("header", "Rule Name");
    		if(filters.size()>0)
    		{
    			for(int i=0; i<filters.size(); i++)
    			{
    				if(i == filters.size()-1)
    				{
    					sGroupByValues = sGroupByValues+" (reconciliation_rule_id = "+filters.get(i).get("groupingValue")+" AND dv.`"+currQualifier +"` = '"+filters.get(i).get("currencyValue")+"') ";
    				}
    				else
    				{
    					sGroupByValues = sGroupByValues+" (reconciliation_rule_id = "+filters.get(i).get("groupingValue")+" AND dv.`"+currQualifier +"` = '"+filters.get(i).get("currencyValue")+"') OR ";    					
    				}
    			}
    			sGroupByValues = " AND ("+sGroupByValues+")";
    		}
			HashMap resultMap = reconciliationResultService.getReconRefTranscations(tenantId, groupId, sViewId, tViewId, periodFactor, params.getRangeFrom(), params.getRangeTo(), limit, pageSize, sGroupByValues, params.getGroupBy(), "",  params.getSortOrderBy(), searchQuery, sortingQuery);
			finalList = (List<LinkedHashMap>) resultMap.get("result");
			finalMap.put("info", resultMap.get("info"));
		}
    	else if("batch".equalsIgnoreCase(params.getGroupBy()))
		{
    		groupingColumn.put("header", "Batch");
    		if(filters.size()>0)
    		{
    			for(int i=0; i<filters.size(); i++)
    			{
    				if(i == filters.size()-1)
    				{
    					if("Manual".equalsIgnoreCase(filters.get(i).get("groupingValue").toString()))
    					{
    						sGroupByValues = sGroupByValues + " (reconciliation_rule_id = 0 AND dv.`"+currQualifier+"` = '"+filters.get(i).get("currencyValue").toString()+"') "; 
    					}
    					else
    					{
    						sGroupByValues = sGroupByValues + " (recon_job_reference = '"+filters.get(i).get("groupingValue").toString()+"' AND dv.`"+currQualifier+"` = '"+filters.get(i).get("currencyValue").toString()+"') "; 
    					}
    				}
    				else
    				{
    					if("Manual".equalsIgnoreCase(filters.get(i).get("groupingValue").toString()))
    					{
    						sGroupByValues = sGroupByValues + " (reconciliation_rule_id = 0 AND dv.`"+currQualifier+"` = '"+filters.get(i).get("currencyValue").toString()+"') OR "; 
    					}
    					else
    					{
    						sGroupByValues = sGroupByValues + " (recon_job_reference = '"+filters.get(i).get("groupingValue").toString()+"' AND dv.`"+currQualifier+"` = '"+filters.get(i).get("currencyValue").toString()+"') OR "; 
    					} 
    				}
    			}
    			sGroupByValues = " AND ("+sGroupByValues+")";
    		}
    		HashMap resultMap = reconciliationResultService.getReconRefTranscations(tenantId, groupId, sViewId, tViewId, periodFactor, params.getRangeFrom(), params.getRangeTo(), limit, pageSize, sGroupByValues, params.getGroupBy(), "", params.getSortOrderBy(), searchQuery, sortingQuery);
			finalList = (List<LinkedHashMap>) resultMap.get("result");
			finalMap.put("info", resultMap.get("info"));
		}
    	else if("columnName".equalsIgnoreCase(params.getGroupBy()))
		{
    		groupingColumn.put("header", "Currency");
			HashMap sColInfo = accountingDataService.getColumnInfo(columnvalues.getsColumnId());
			String sColumnName = "";
			if("DATE".equalsIgnoreCase(sColInfo.get("dataType").toString()) || "DATETIME".equalsIgnoreCase(sColInfo.get("dataType").toString()))
			{
				sColumnName = "DATE(dv.`"+sColInfo.get("colName").toString()+"`)";
			}
			else
			{
				sColumnName = "`"+sColInfo.get("colName").toString()+"`";
			}
			if(filters.size()>0)
			{
				for(int i=0; i<filters.size(); i++)
				{
					if(i == filters.size()-1)
					{
						sGroupByValues = sGroupByValues+" ("+sColumnName+" = '"+filters.get(i).get("groupingValue")+"' AND dv.`"+currQualifier+"` = '"+filters.get(i).get("currencyValue").toString()+"') "; 
					}
					else
					{
						sGroupByValues = sGroupByValues+" ("+sColumnName+" = '"+filters.get(i).get("groupingValue")+"' AND dv.`"+currQualifier+"` = '"+filters.get(i).get("currencyValue").toString()+"') OR "; 
					}
				}
				sGroupByValues = " AND ("+sGroupByValues+") ";
			}
			HashMap resultMap = reconciliationResultService.getReconRefTranscations(tenantId, groupId, sViewId, tViewId, periodFactor, params.getRangeFrom(), params.getRangeTo(), limit, pageSize, sGroupByValues, params.getGroupBy(), sColumnName, params.getSortOrderBy(), searchQuery, sortingQuery);
			finalList = (List<LinkedHashMap>) resultMap.get("result");
			finalMap.put("info", resultMap.get("info"));
		}
    	else if("approvalRule".equalsIgnoreCase(params.getGroupBy()))
		{
    		groupingColumn.put("header", "Approval Rules");
    		if(filters.size()>0)
    		{
    			for(int i=0; i<filters.size(); i++)
    			{
    				if(i == filters.size()-1)
    				{
    					sGroupByValues = sGroupByValues + " (approval_rule_id = "+filters.get(i).get("groupingValue")+" AND dv.`"+currQualifier+"` = '"+filters.get(i).get("currencyValue").toString()+"') ";
    				}
    				else
    				{
    					sGroupByValues = sGroupByValues + " (approval_rule_id = "+filters.get(i).get("groupingValue")+" AND dv.`"+currQualifier+"` = '"+filters.get(i).get("currencyValue").toString()+"') OR ";
    				}
    			}
    			sGroupByValues = " AND ("+sGroupByValues+")";
    		}
			HashMap resultMap = reconciliationResultService.getReconRefTranscations(tenantId, groupId, sViewId, tViewId, periodFactor, params.getRangeFrom(), params.getRangeTo(), limit, pageSize, sGroupByValues, params.getGroupBy(), "", params.getSortOrderBy(), searchQuery, sortingQuery);
			finalList = (List<LinkedHashMap>) resultMap.get("result");
			finalMap.put("info", resultMap.get("info"));
		}
    	else if("approvalStatus".equalsIgnoreCase(params.getGroupBy()))
		{
    		groupingColumn.put("header", "Approval Status");
    		if(filters.size()>0)
    		{
    			List<LookUpCode> lookUps = lookUpCodeRepository.findByTenantIdAndLookUpType(tenantId, "APPROVAL_STATUS");
    			HashMap lookUpsMap = new HashMap();
    			if(lookUps.size()>0)
    			{
    				for(LookUpCode luc : lookUps)
    				{
    					lookUpsMap.put(luc.getMeaning(), luc.getLookUpCode());
    				}
    			}
    			log.info("LookUpMeaning:LookUpCode--> "+lookUpsMap);
    			for(int i=0; i<filters.size(); i++)
    			{
    				if(i == filters.size()-1)
    				{
    					if("Not Required".equalsIgnoreCase(filters.get(i).get("groupingValue").toString()))
    					{
    						sGroupByValues = sGroupByValues + " (final_status is null AND dv.`"+currQualifier+"` = '"+filters.get(i).get("currencyValue").toString() +"') ";
    					}
    					else if("Awaiting for Approvals".equalsIgnoreCase(filters.get(i).get("groupingValue").toString()))
    					{
    						sGroupByValues = sGroupByValues + " (final_status  = 'IN_PROCESS' AND dv.`"+currQualifier+"` = '"+filters.get(i).get("currencyValue").toString() +"') ";
    					}
    					else
    					{
    						sGroupByValues = sGroupByValues + " (final_status  = '"+lookUpsMap.get(filters.get(i).get("groupingValue").toString())+"' AND dv.`"+currQualifier+"` = '"+filters.get(i).get("currencyValue").toString() +"') ";
    					}
    				}
    				else
    				{
    					if("Not Required".equalsIgnoreCase(filters.get(i).get("groupingValue").toString()))
    					{
    						sGroupByValues = sGroupByValues + " (final_status is null AND dv.`"+currQualifier+"` = '"+filters.get(i).get("currencyValue").toString() +"') OR ";
    					}
    					else if("Awaiting for Approvals".equalsIgnoreCase(filters.get(i).get("groupingValue").toString()))
    					{
    						sGroupByValues = sGroupByValues + " (final_status  = 'IN_PROCESS' AND dv.`"+currQualifier+"` = '"+filters.get(i).get("currencyValue").toString() +"') OR ";
    					}
    					else
    					{
    						sGroupByValues = sGroupByValues + " (final_status = '"+lookUpsMap.get(filters.get(i).get("groupingValue").toString())+"' AND dv.`"+currQualifier+"` = '"+filters.get(i).get("currencyValue").toString() +"') OR ";
    					}
    				}
    			}
    			sGroupByValues = " AND ("+ sGroupByValues+") ";
    		}
			HashMap resultMap = reconciliationResultService.getReconRefTranscations(tenantId, groupId, sViewId, tViewId, periodFactor, params.getRangeFrom(), params.getRangeTo(), limit, pageSize, sGroupByValues, params.getGroupBy(), "", params.getSortOrderBy(), searchQuery, sortingQuery);
			finalList = (List<LinkedHashMap>) resultMap.get("result");
			finalMap.put("info", resultMap.get("info"));
		}
    	else if("approvalDate".equalsIgnoreCase(params.getGroupBy()))
		{
    		groupingColumn.put("header", "Approved Date");
    		if(filters.size()>0)
    		{
    			for(int i=0; i<filters.size(); i++)
    			{
    				if(i == filters.size()-1)
    				{
    					sGroupByValues = sGroupByValues + " (Date(final_action_date) = '"+filters.get(i).get("groupingValue")+"' AND dv.`"+currQualifier+"` = '"+filters.get(i).get("currencyValue").toString()+"') ";
    				}
    				else
    				{
    					sGroupByValues = sGroupByValues + " (Date(final_action_date) = '"+filters.get(i).get("groupingValue")+"' AND dv.`"+currQualifier+"` = '"+filters.get(i).get("currencyValue").toString()+"') OR ";
    				}
    			}
        		sGroupByValues = " AND ("+sGroupByValues+")";
    		}
			HashMap resultMap = reconciliationResultService.getReconRefTranscations(tenantId, groupId, sViewId, tViewId, periodFactor, params.getRangeFrom(), params.getRangeTo(), limit, pageSize, sGroupByValues, params.getGroupBy(), "", params.getSortOrderBy(), searchQuery, sortingQuery);
			finalList = (List<LinkedHashMap>) resultMap.get("result");
			finalMap.put("info", resultMap.get("info"));
		}
    	else if("days".equalsIgnoreCase(params.getGroupBy()))
    	{
			String sDateQualifier = "";
			String tDateQualifier = "";
    		groupingColumn.put("header", "Period");
    		log.info("Fetching reconciled summary info by period");
    		if(filters.size()>0)
    		{
				if("TRANSDATE".equalsIgnoreCase(params.getDateQualifierType()))
				{
					sDateQualifier = reconciliationResultService.getViewColumnQualifier(BigInteger.valueOf(sViewId), "TRANSDATE");
					sDateQualifier = "Date(dv.`"+sDateQualifier+"`)";
				}
				else if("FILEDATE".equalsIgnoreCase(params.getDateQualifierType()))
				{
					sDateQualifier = reconciliationResultService.getViewColumnQualifier(BigInteger.valueOf(sViewId), "FILEDATE");
					sDateQualifier = "Date(dv.`"+sDateQualifier+"`)";
				}
				for(int i=0; i<filters.size(); i++)
				{
					if(i == filters.size()-1)
					{
						sGroupByValues = sGroupByValues + " ("+sDateQualifier+" = '"+filters.get(i).get("groupingValue")+"' AND `"+currQualifier+"` = '"+filters.get(i).get("currencyValue").toString()+"') ";
					}
					else
					{
						sGroupByValues = sGroupByValues + " ("+sDateQualifier+" = '"+filters.get(i).get("groupingValue")+"' AND `"+currQualifier+"` = '"+filters.get(i).get("currencyValue").toString()+"') OR ";						
					}
				}
				sGroupByValues = " AND ("+sGroupByValues+")";
    		}

			HashMap resultMap = reconciliationResultService.getReconRefTranscations(tenantId, groupId, sViewId, tViewId, periodFactor, params.getRangeFrom(), params.getRangeTo(), limit, pageSize, sGroupByValues, params.getGroupBy(), sDateQualifier, params.getSortOrderBy(), searchQuery, sortingQuery);
			finalList = (List<LinkedHashMap>) resultMap.get("result");
			finalMap.put("info", resultMap.get("info"));
    	}
    	List<HashMap> source = new ArrayList<HashMap>();
    	HashMap  s1 = new HashMap();
		s1.put("field", "srcCount");
		s1.put("header", "Transaction Count");
		s1.put("align", "center");
		s1.put("width", "150px");
		s1.put("colId", "src_count");
		s1.put("dataType", "INTEGER");
		source.add(s1);
		HashMap s2 = new HashMap();
		s2.put("field", "srcAmount");
		s2.put("header", "Reconciled Amount");
		s2.put("align", "right");
		s2.put("width", "150px");
		s2.put("colId", "src_amount_sum");
		s2.put("dataType", "DECIMAL");
		source.add(s2);
		HashMap s3 = new HashMap();
		s3.put("field", "srcTolAmount");
		s3.put("header", "Variance Amount");
		s3.put("align", "right");
		s3.put("width", "150px");
		s3.put("colId", "src_tolerance_amount");
		s3.put("dataType", "DECIMAL");
		source.add(s3);
		
    	List<HashMap> target = new ArrayList<HashMap>();
    	HashMap  t1 = new HashMap();
    	t1.put("field", "trCount");
    	t1.put("header", "Transaction Count");
    	t1.put("align", "center");
    	t1.put("width", "150px");
    	t1.put("colId", "tr_count");
    	t1.put("dataType", "INTEGER");
		target.add(t1);
		HashMap t2 = new HashMap();
		t2.put("field", "trAmount");
		t2.put("header", "Reconciled Amount");
		t2.put("align", "right");
		t2.put("width", "150px");
		t2.put("colId", "tr_amount_sum");
		t2.put("dataType", "DECIMAL");
		target.add(t2);
		HashMap t3 = new HashMap();
		t3.put("field", "trTolAmount");
		t3.put("header", "Variance Amount");
		t3.put("align", "right");
		t3.put("width", "150px");
		t3.put("colId", "tr_tolerance_amount");
		t3.put("dataType", "DECIMAL");
		target.add(t3);
    	
    	finalMap.put("groupingColumn", groupingColumn);
    	finalMap.put("source", source);
    	finalMap.put("target", target);
    	finalMap.put("reconRef", finalList);
    	return finalMap;
    }


    @PostMapping("/getUnReconciledData")
    @Timed
    public HashMap getUnReconciledData(HttpServletRequest request,@RequestBody UnReconciledDTO params)
    {
    	HashMap finalMap = new HashMap();

    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());

    	RuleGroup rg = ruleGroupRepository.findByIdForDisplayAndTenantId(params.getGroupId(), tenantId);
    	Long groupId = rg.getId();
    	
    	DataViews dv = dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, params.getDataViewId());
    	Long viewId = dv.getId();
    	
    	String globalSearchQuery = "";
    	String columnSearchQuery = "";
    	String sortingQuery = "";
    	
    	// Column Level Search
    	
    	// Global Level Search
    	
    	// Filters Query Building
    	Long pageNumber = params.getPageNumber();
    	Long pageSize = params.getPageSize();
		Long limit = 0L;
		limit = (pageNumber * pageSize + 1)-1;
		log.info("Limit Starting Values : "+ limit);
		log.info("Page Number : "+ pageNumber);
    	
    	HashMap colNameNType = reconciliationResultService.getColumnNameNDatatypeForView(viewId);
    	if("source".equalsIgnoreCase(params.getSourceOrTarget()))
    	{
    		String filterQueryFinal = " ";
    		if(params.getSelectAll() == false)
    		{
        		if(params.getFilters() != null && params.getFilters().size()>0)
        		{
        			List<HashMap> inputs = (List<HashMap>) params.getFilters().get("inputs");
        			HashMap info = (HashMap) params.getFilters().get("info");
        			for(int j=0; j<inputs.size(); j++)
        			{
        		    	String filterQuery = "";
        				HashMap hm = inputs.get(j);
        				int i=0;
        				for(Object key : hm.keySet())
        				{
        					i = i+1;
        					String columnName = key.toString();
        					String value = hm.get(columnName).toString();
        					
        					String dataType = info.get(columnName).toString();
        					if(i == hm.size())
        					{
        						if("DATE".equalsIgnoreCase(dataType))
        						{
            						filterQuery = filterQuery + "DATE(`"+columnName+"`) = '"+value+"'";    							
        						}
        						else
        						{
            						filterQuery = filterQuery + "`"+columnName+"` = '"+value+"'";
        						}
        					}
        					else
        					{
        						if("DATE".equalsIgnoreCase(dataType))
        						{
            						filterQuery = filterQuery + "DATE(`"+columnName+")` = '"+value+"' AND ";  						
        						}
        						else
        						{
            						filterQuery = filterQuery + "`"+columnName+"` = '"+value+"' AND ";    						    							
        						}
        					}
        				}
        				filterQuery = "("+filterQuery+")";
        				if(j == inputs.size()-1)
        				{
        					filterQueryFinal = filterQueryFinal + filterQuery;
        				}
        				else
        				{
        					filterQueryFinal = filterQueryFinal + filterQuery+ " OR ";
        				}
        			}
        			filterQueryFinal = " AND ("+filterQueryFinal+") ";
        		}
    		}
    		finalMap = reconciliationResultService.getUnReconciledData(params.getSourceOrTarget(), groupId, viewId, filterQueryFinal, columnSearchQuery, globalSearchQuery, sortingQuery, limit, pageSize, colNameNType, params.getPeriodFactor(), params.getRangeFrom(), params.getRangeTo());
    	}
    	else if("target".equalsIgnoreCase(params.getSourceOrTarget()))
    	{
    		 finalMap = reconciliationResultService.getUnReconciledData(params.getSourceOrTarget(), groupId, viewId, "", columnSearchQuery, globalSearchQuery, sortingQuery, limit, pageSize, colNameNType, params.getPeriodFactor(), params.getRangeFrom(), params.getRangeTo());
    	}
    	return finalMap;
    }
    
    
    @PostMapping("/getReconDataByViewIds")
    @Timed
    public List<LinkedHashMap> getReconData2(HttpServletRequest request,@RequestParam(value = "pageNumber", required=false) Long pageNumber, @RequestParam(value = "pageSize", required=false) Long pageSize, 
    		@RequestParam(value = "exportFile", required=false) String exportFile,@RequestBody RWQDetailInfoDTO params) throws ClassNotFoundException, SQLException, java.text.ParseException{
    	{
    		
        	HashMap map=userJdbcService.getuserInfoFromToken(request);
        	Long tenantId=Long.parseLong(map.get("tenantId").toString());
        	
        	List<LinkedHashMap> finalList = new ArrayList<LinkedHashMap>();
        	RuleGroup rg = ruleGroupRepository.findByIdForDisplayAndTenantId(params.getGroupId(), tenantId);
        	Long groupId = rg.getId();
        	
        	DataViews dv = dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, params.getDataViewId());
        	Long viewId = dv.getId();
        	
    		Long limit = 0L;
    		limit = (pageNumber * pageSize + 1)-1;
    		log.info("Limit Starting Values : "+ limit);
    		log.info("Page Number : "+ pageNumber);
    		HashMap colNameNType = reconciliationResultService.getColumnNameNDatatypeForView(viewId);

        	String columnsString = reconciliationResultService.getColumnNamesAsString(viewId);
        	RWQDataFetchDTO keyValues = params.getKeyValues();
        	ManualUnRecByColumnDTO columnvalues = keyValues.getColumnValues();
        	List<HashMap> filters = keyValues.getFilters();
        	List<HashMap> searchColumnsMps = params.getColumnSearch();
        	log.info("searchColumnsMps: "+searchColumnsMps);
        	
        	//Building Column Search functionality Query
        	String columnSearchQuery = "";
        	String statusNReconRefQuery = "";
    		HashMap recNStatusMp = new HashMap();
    		String globalSearchQuery = "";
    		List<String> columnNames = reconciliationResultService.getColumnAlilasByViewId(viewId);
    		columnNames.add("scrIds");	
    		columnNames.add("rowDescription");
    		columnNames.add("adjustmentType");
    		
    		if(params.getSearchWord() != null && params.getSearchWord().length()>0)
    		{
    			for(int i=0; i< columnNames.size(); i++)
    			{
    				if(i == columnNames.size()-1)
    				{
    					globalSearchQuery = globalSearchQuery + " `"+columnNames.get(i)+"` LIKE '%"+params.getSearchWord()+"%' ";
    				}
    				else
    				{
    					globalSearchQuery = globalSearchQuery + " `"+columnNames.get(i)+"` LIKE '%"+params.getSearchWord()+"%' OR ";
    				}
    			}
    			globalSearchQuery = " AND ("+ globalSearchQuery+") ";
    		}
    		
        	if(params.getColumnSearch() != null)
        	{
        		if(searchColumnsMps.size()>0)
        		{
        			for(int i=0; i<searchColumnsMps.size(); i++)
        			{
        				HashMap mp = searchColumnsMps.get(i);
        				String searchWord = mp.get("searchWord").toString();
        				if("recon_reference".equalsIgnoreCase(mp.get("columnId").toString()) || "recon_status".equalsIgnoreCase(mp.get("columnId").toString()))
        				{
            				if("recon_status".equalsIgnoreCase(mp.get("columnId").toString()))
            				{
            					statusNReconRefQuery = statusNReconRefQuery + " AND recon_status LIKE '%" + searchWord + "%'";
            				}
            				if("recon_reference".equalsIgnoreCase(mp.get("columnId").toString()))
            				{
            					statusNReconRefQuery = statusNReconRefQuery + " AND recon_reference LIKE '%"+searchWord + "%'";
            				}
        				}
        				else
        				{
            				String columnName = mp.get("columnId").toString();
            				if("original_row_id".equalsIgnoreCase(columnName) || "target_row_id".equalsIgnoreCase(columnName))
            				{
            					columnName = "scrIds";
            				}
                			if(i == searchColumnsMps.size()-1)
                			{
                				columnSearchQuery = columnSearchQuery + " AND `" + columnName +"` LIKE '%"+searchWord+"%'";
                			}
                			else
                			{
                				columnSearchQuery = columnSearchQuery + " AND `" + columnName +"` LIKE '%"+searchWord+"%'";
                			}
        				}
        			}
        		}
        	}
        	// Sorting Query
        	String sortingQuery = "";
        	sortingQuery = " ORDER BY `"+params.getSortByColumnId()+"` "+ params.getSortOrderBy();
        	log.info("Column Search Query: " + columnSearchQuery);
        	log.info("Recon Reference or Recon Status Search Query: "+statusNReconRefQuery);
        	String amountQualifier = reconciliationResultService.getViewColumnQualifier(BigInteger.valueOf(viewId), "AMOUNT");

        	String periodFactor = params.getPeriodFactor();
    		String groupByValues = "";
        	if("reconciled".equalsIgnoreCase(params.getStatus()))
        	{}
        	else if("unReconciled".equalsIgnoreCase(params.getStatus()))
        	{
        		String filterQuery = "";
        		String currencyColumn = reconciliationResultService.getViewColumnQualifier(BigInteger.valueOf(viewId), "CURRENCYCODE");
        		if("days".equalsIgnoreCase(params.getGroupBy()))
        		{   			
        			if("source".equalsIgnoreCase(params.getSourceOrTarget()))
        			{
        				if(filters.size()>0)
        				{
        					for(int i=0; i<filters.size(); i++)
        					{
        						if(i == filters.size()-1)
        						{
        							filterQuery = filterQuery + " (DATE(`"+periodFactor+"`) = '"+filters.get(i).get("groupingValue")+"' AND `"+currencyColumn+"` = '"+filters.get(i).get("currencyValue")+"') ";
        						}
        						else
        						{
        							filterQuery = filterQuery + " (DATE(`"+periodFactor+"`) = '"+filters.get(i).get("groupingValue")+"' AND `"+currencyColumn+"` = '"+filters.get(i).get("currencyValue")+"') OR ";
        						}
        					}
        					filterQuery = " AND ("+ filterQuery+")";
        				}
        				finalList = reconciliationResultService.getUnReconTransactionsData("source", groupId, viewId, periodFactor, params.getRangeFrom(), params.getRangeTo(), filterQuery, limit, pageSize,columnSearchQuery, sortingQuery, colNameNType, globalSearchQuery);
        			}
        			else if("target".equalsIgnoreCase(params.getSourceOrTarget()))
        			{
        					finalList = reconciliationResultService.getUnReconTransactionsData("target", groupId, viewId, periodFactor, params.getRangeFrom(), params.getRangeTo(), filterQuery, limit, pageSize, columnSearchQuery, sortingQuery, colNameNType, globalSearchQuery);	
        			}
        		}
        		else if("columnName".equalsIgnoreCase(params.getGroupBy()))
        		{
    				HashMap colInfo = accountingDataService.getColumnInfo(columnvalues.getColumnId());
    				String columnName = "";
    				if("DATE".equalsIgnoreCase(colInfo.get("dataType").toString()) || "DATETIME".equalsIgnoreCase(colInfo.get("dataType").toString()))
    				{
    					columnName = "DATE(`"+colInfo.get("colName").toString()+"`)";
    				}
    				else
    				{
    					columnName = "`"+colInfo.get("colName").toString()+"`";
    				}
        			if("source".equalsIgnoreCase(params.getSourceOrTarget()))
        			{
        				if(filters.size()>0)
        				{
        					for(int i=0; i<filters.size(); i++)
        					{
        						if(i == filters.size()-1)
        						{
        							filterQuery = filterQuery + " ("+columnName+" = '"+filters.get(i).get("groupingValue")+"' AND `"+currencyColumn+"` = '"+filters.get(i).get("currencyValue")+"') ";
        						}
        						else
        						{
        							filterQuery = filterQuery + " ("+columnName+" = '"+filters.get(i).get("groupingValue")+"' AND `"+currencyColumn+"` = '"+filters.get(i).get("currencyValue")+"') OR ";
        						}
        					}
        					filterQuery = " AND ("+ filterQuery+")";
        				}
        				finalList = reconciliationResultService.getUnReconTransactionsData("source", groupId, viewId, periodFactor, params.getRangeFrom(), params.getRangeTo(), filterQuery, limit, pageSize, columnSearchQuery, sortingQuery, colNameNType, globalSearchQuery);
        			}
        			else if("target".equalsIgnoreCase(params.getSourceOrTarget()))
        			{
        				finalList = reconciliationResultService.getUnReconTransactionsData("target", groupId, viewId, periodFactor, params.getRangeFrom(), params.getRangeTo(), filterQuery, limit, pageSize,columnSearchQuery, sortingQuery, colNameNType, globalSearchQuery);
        			}
        		}
        	}
        	return finalList;
    	}
    }
 /**
     * @throws SQLException 
     * **/
    @PostMapping("/getReconRefTransactions")
    @Timed    
    public HashMap getReconDetailTransaction(HttpServletRequest request, @RequestBody ReconRefTransactionsDTO params,
    		@RequestParam(value = "pageNumber", required=false) Long pageNumber, @RequestParam(value = "pageSize", required=false) Long pageSize) throws SQLException
    {
    	HashMap finalMap = new HashMap();
    	String sourceOrTarget = params.getSourceOrTarget();
    	String reconRef = params.getReconReference();
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());

		Long limit = 0L;
		limit = (pageNumber * pageSize + 1)-1;
		log.info("Limit Starting Values : "+ limit);
		log.info("Page Number : "+ pageNumber);
    	
    	RuleGroup rg = ruleGroupRepository.findByIdForDisplayAndTenantId(params.getGroupId(), tenantId);
    	Long groupId = rg.getId();
    	
    	DataViews dv = dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, params.getViewId());
    	Long viewId = dv.getId();
    	
    	log.info("Rest API for fetching recon detail transactions for recon reference: "+reconRef+", group id: "+ groupId+", view id: "+ viewId+", source or target: "+sourceOrTarget);
    	List<LinkedHashMap> finalData = new ArrayList<LinkedHashMap>();

    	HashMap result = reconciliationResultService.getReconRefChildData(sourceOrTarget, viewId, groupId, reconRef, tenantId, "!=", limit, pageSize);
    	finalData = (List<LinkedHashMap>) result.get("data");
    	finalMap.put("reconRefData", finalData);
    	return finalMap;
    }
    
    /** Author: Shiva
     * 	Purpose: Recon Custom Filter For Source
     * @throws SQLException 
     */
    @GetMapping("/reconCustomFilterForSource")
    @Timed
    public HashMap reconCustomFilterForSource(HttpServletRequest request, @RequestParam String groupId, @RequestParam String sViewId, @RequestParam String recReferences,
    		@RequestParam(value = "pageNumber", required=false) Long pageNumber, @RequestParam(value = "pageSize", required=false) Long pageSize) throws SQLException 
    {
    	log.info("Recon Custom Filter API for Source");
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	Long userId = Long.parseLong(map.get("userId").toString());
    	HashMap finalMap = new HashMap();
    	
		Long limit = 0L;
		limit = (pageNumber * pageSize + 1)-1;
		log.info("Limit Starting Values : "+ limit);
		log.info("Page Number : "+ pageNumber);

    	RuleGroup rg = ruleGroupRepository.findByIdForDisplayAndTenantId(groupId, tenantId);
    	Long ruleGroupId = rg.getId();
    	
    	DataViews sdv = dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, sViewId);
    	Long sDataViewId = sdv.getId();
		// Source
    	List<LinkedHashMap> source = new ArrayList<LinkedHashMap>(); 
    	HashMap sourceInfo = reconciliationResultService.getReconRefChildData("source", sDataViewId, ruleGroupId, recReferences, tenantId, "!=", limit, pageSize);
    	source.addAll((List<LinkedHashMap>) sourceInfo.get("data"));
    	finalMap.put("source", source);
		return finalMap;
    }

    
    /** Author: Shiva
     * 	Purpose: Recon Custom Filter For Target
     * @throws SQLException 
     */
    @GetMapping("/reconCustomFilterForTarget")
    @Timed
    public HashMap reconCustomFilterForTarget(HttpServletRequest request, @RequestParam String groupId, @RequestParam String tViewId, @RequestParam String recReferences,
    		@RequestParam(value = "pageNumber", required=false) Long pageNumber, @RequestParam(value = "pageSize", required=false) Long pageSize) throws SQLException 
    {
    	log.info("Rest api for rwq custom filter");
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	Long userId = Long.parseLong(map.get("userId").toString());
    	HashMap finalMap = new HashMap();
    	
		Long limit = 0L;
		limit = (pageNumber * pageSize + 1)-1;
		log.info("Limit Starting Values : "+ limit);
		log.info("Page Number : "+ pageNumber);

    	RuleGroup rg = ruleGroupRepository.findByIdForDisplayAndTenantId(groupId, tenantId);
    	Long ruleGroupId = rg.getId();
    	
    	DataViews tdv = dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, tViewId);
    	Long tDataViewId = tdv.getId();

		// Target
    	List<LinkedHashMap> target = new ArrayList<LinkedHashMap>(); 
    	HashMap targetInfo = reconciliationResultService.getReconRefChildData("target", tDataViewId, ruleGroupId, recReferences, tenantId, "!=", limit, pageSize);
    	target.addAll((List<LinkedHashMap>) targetInfo.get("data"));
    	finalMap.put("target", target);
		return finalMap;
    }
    
    @GetMapping("/getReconCustomFilterInfo")
    @Timed
    public HashMap reconCustomFilterForTarget(HttpServletRequest request, @RequestParam String groupId, @RequestParam String sViewId, @RequestParam String tViewId, @RequestParam String recReferences) throws SQLException 
    {
    		HashMap finalMap = new HashMap();
    		HashMap map=userJdbcService.getuserInfoFromToken(request);
    		Long tenantId=Long.parseLong(map.get("tenantId").toString());
    		
        	RuleGroup rg = ruleGroupRepository.findByIdForDisplayAndTenantId(groupId, tenantId);
        	Long ruleGroupId = rg.getId();
        	
        	DataViews sdv = dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, sViewId);
        	Long sDataViewId = sdv.getId();
        	
        	DataViews tdv = dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, tViewId);
        	Long tDataViewId = tdv.getId();

    		finalMap = reconciliationResultService.getReconCustomFilterInfo(tenantId, ruleGroupId, sDataViewId, tDataViewId, recReferences);
    	
    	return finalMap;
    }

	 /*
	  * Author: Shiva
	  * Description: Posting suggested data
	  * @Param: SuggestedPostingDTO
	  * */
	 @PostMapping("postSuggestedData")
	 public ErrorReporting postSuggestedData(HttpServletRequest request, @RequestBody SuggestedPostingDTO params)
	 {
		 ErrorReporting errorReport = new ErrorReporting();
		 List<String> reasons = new ArrayList<String>();
		 
		 HashMap map=userJdbcService.getuserInfoFromToken(request);
		 Long tenantId=Long.parseLong(map.get("tenantId").toString());
		 Long userId=Long.parseLong(map.get("userId").toString());

		 Long groupId = params.getGroupId();
		 Long sViewId = params.getsViewId();
		 Long tViewId = params.gettViewId();
		 List<SuggestedPostDTO> source = params.getSource();
		 List<SuggestedPostDTO> target = params.getTarget();
		 
		 List<String> reconRefs = new ArrayList<String>();
		 log.info("REST API for posting suggested data for the group id: "+ groupId+", source view id: "+sViewId+", target view id: "+tViewId);
		 
		 if(source.size()>0)
		 {
			 List<ReconciliationResult> sourceRS = new ArrayList<ReconciliationResult>();
			 for(SuggestedPostDTO src : source)
			 {
				 ReconciliationResult rsNew = new ReconciliationResult();
				 rsNew.setOriginalRowId(src.getRowId());
				 rsNew.setOriginalViewId(sViewId);
				 rsNew.setTargetRowId(null);
				 rsNew.setTargetViewId(null);
				 rsNew.setTargetView(null);
				 rsNew.setReconReference(src.getReconReference());
				 rsNew.setReconciliationRuleGroupId(groupId);
				 rsNew.setReconciliationRuleId(src.getRuleId());
				 rsNew.setReconciliationUserId(userId);
				 rsNew.setReconJobReference(src.getJobReference());
				 rsNew.setReconciledDate(ZonedDateTime.now());
				 rsNew.setReconStatus("RECONCILED");
				 rsNew.setCurrentRecordFlag(true);
				 rsNew.setTenantId(tenantId);
				 sourceRS.add(rsNew);
				 reconRefs.add(src.getReconReference());
			 }
			 try{
				 List<ReconciliationResult> batchRSSave = reconciliationResultRepository.save(sourceRS);
				 errorReport.setStatus("Success");
			 }
			 catch(Exception e)
			 {
				 errorReport.setStatus("Failed");
				 reasons.add("Error while posting source suggested data");
			 }
		 }
		 if(target.size()>0)
		 {
			 List<ReconciliationResult> targetRS = new ArrayList<ReconciliationResult>();
			 for(SuggestedPostDTO trgt : target)
			 {
				 ReconciliationResult rsNew = new ReconciliationResult();
				 rsNew.setOriginalRowId(null);
				 rsNew.setOriginalViewId(null);
				 rsNew.setOriginalView(null);
				 rsNew.setTargetRowId(trgt.getRowId());
				 rsNew.setTargetViewId(tViewId);
				 rsNew.setReconReference(trgt.getReconReference());
				 rsNew.setReconciliationRuleGroupId(groupId);
				 rsNew.setReconciliationRuleId(trgt.getRuleId());
				 rsNew.setReconciliationUserId(userId);
				 rsNew.setReconJobReference(trgt.getJobReference());
				 rsNew.setReconciledDate(ZonedDateTime.now());
				 rsNew.setReconStatus("RECONCILED");
				 rsNew.setCurrentRecordFlag(true);
				 rsNew.setTenantId(tenantId);
				 targetRS.add(rsNew);
				 reconRefs.add(trgt.getReconReference());
			 }
			 try{
				 List<ReconciliationResult> batchRSSave = reconciliationResultRepository.save(targetRS);
				 errorReport.setStatus("Success");
			 }
			 catch(Exception e)
			 {
				 errorReport.setStatus("Failed");				
				 reasons.add("Error while posting target suggested data");
			 }
		 }
		 // Deleting suggested data from t_reconciliation_suggestion_data
		 if(reconRefs.size()>0)
		 {
			 List<ReconciliationDuplicateResult> suggestedData = reconciliationDuplicateResultRepository.findByReconReferenceIn(reconRefs);
			 try{
				 reconciliationDuplicateResultRepository.delete(suggestedData);
				 errorReport.setStatus("Success");
				 reasons.add(suggestedData.size() + " Recods has been removed from suggesed data");
			 }
			 catch(Exception e)
			 {
				errorReport.setStatus("Failed");
				reasons.add("Error while removing suggesed data");
			 }
		 }
		 errorReport.setReasons(reasons);
		 return errorReport;
	 }
	 
 /*
  * Author: Shiva
  * @param manualReconciledData, tenantId, userId
  * Description: Posting Manual Reconciliation Data
  * @return void
  */
 @PostMapping("/postManualReconData")
 @Timed
 public ErrorReport postManualReconData(HttpServletRequest request,@RequestBody List<ManualRecDTO> manualRecDTOs) throws NumberFormatException, SQLException, ClassNotFoundException{
 	log.info("Rest request to posting manual reconciled data");
 	HashMap map=userJdbcService.getuserInfoFromToken(request);
 	Long tenantId=Long.parseLong(map.get("tenantId").toString());
 	Long userId=Long.parseLong(map.get("userId").toString());
 	ErrorReport errorReport = new ErrorReport();
 	List<Long> leftRecords = new ArrayList<Long>();
 	Long recRuleGrpId=0L;
 	Long sViewId = 0L;
 	Long tViewId = 0L;
 	Long apprRuleGrp=0L;
 	
 	if(manualRecDTOs.size()>0)
 	{
 		//getting maximum recon reference id
	    Long maxReconRef = reconciliationResultRepository.fetchMaxReconReference();
	    if(maxReconRef == null)
	    {
	  		maxReconRef = 0L;
	    }
	    log.info("Max ReconReference Id: "+maxReconRef);
	    Long reconReferenceId = maxReconRef+1;
	    log.info("Max ReconReference Id: "+maxReconRef+", Max+1 ReferenceId: "+reconReferenceId);
	    
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(new Date());
		timeStamp=timeStamp.replaceAll(" ", "_");
		String jobReference = "MANUAL_"+timeStamp;
		
		List<Long> sourceIds = new ArrayList<Long>();
		List<Long> targetIds = new ArrayList<Long>();
		
		List<ViewIdRowIdDTO> source = new ArrayList<ViewIdRowIdDTO>();
		List<ViewIdRowIdDTO> target = new ArrayList<ViewIdRowIdDTO>();
		List<Long> srcIds = new ArrayList<Long>();
		List<Long> trgtIds = new ArrayList<Long>();
		
		// Removing duplicate objects from input
		for(ManualRecDTO manualRecDTO: manualRecDTOs)
 		{
 	     	List<ViewIdRowIdDTO> sourceDTO = manualRecDTO.getSource();
 	     	List<ViewIdRowIdDTO> targetDTO = manualRecDTO.getTarget();
 	     	if(sourceDTO.size()>0)
 	     	{
 	     		DataViews dv = dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, sourceDTO.get(0).getViewId());
 	     		sViewId = dv.getId();
 	     		for(ViewIdRowIdDTO dr : sourceDTO)
 	     		{
 	     			srcIds.add(dr.getRowId());
 	     			if(!sourceIds.contains(dr.getRowId()))
 	     			{
 	     				source.add(dr);
 	     			}
 	     		}
 	     	}
 	     	if(targetDTO.size()>0)
 	     	{
 	     		DataViews dv = dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, targetDTO.get(0).getViewId());
 	     		tViewId = dv.getId();
	     		for(ViewIdRowIdDTO dr : targetDTO)
 	     		{
	     			trgtIds.add(dr.getRowId());
 	     			if(!targetIds.contains(dr.getRowId()))
 	     			{
 	     				target.add(dr);
 	     			}
 	     		}
 	     	}
 		}
		String srcAmountQualifier = reconciliationResultService.getViewColumnQualifier(BigInteger.valueOf(sViewId), "AMOUNT");
		String trAmountQualifier = reconciliationResultService.getViewColumnQualifier(BigInteger.valueOf(tViewId), "AMOUNT");
		HashMap srcMap = new HashMap();
		HashMap trMap = new HashMap();
		log.info("Amount Qualifier for source: "+ srcAmountQualifier+", target: "+trAmountQualifier);
		if(srcAmountQualifier.length()>0 && trAmountQualifier.length()>0)
		{
			String sourceIdString = reconciliationResultService.getStringWithNumbers(srcIds);
			String targetIdString = reconciliationResultService.getStringWithNumbers(trgtIds);
			srcMap = reconciliationResultService.getAmountsGroupByScrIds(sViewId, srcAmountQualifier, sourceIdString);
			trMap = reconciliationResultService.getAmountsGroupByScrIds(tViewId, trAmountQualifier, targetIdString);
		}
		// Posting manual unreconciliation
	    List<ReconciliationResult> allRecords = new ArrayList<ReconciliationResult>();
	    Long ruleGroupId = 0L;
		if(source.size()>0)
		{
	    	RuleGroup rg = ruleGroupRepository.findByIdForDisplayAndTenantId(source.get(0).getGroupId(), tenantId);
	    	Long groupId = rg.getId();
	    	ruleGroupId = groupId;
	    	
	    	DataViews dv = dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, source.get(0).getViewId());
	    	Long viewId = dv.getId();
	    	sViewId = viewId;
			for(ViewIdRowIdDTO dr : source)
      		{
      			recRuleGrpId=groupId;
      			DataViews dataView = dataViewsRepository.findOne(viewId);
      			if(dataView != null)
      			{
      				ReconciliationResult recon = new ReconciliationResult();
      				recon.setOriginalRowId(dr.getRowId());
      				recon.setOriginalViewId(viewId);
      				recon.setOriginalView(dataView.getDataViewName());
      				recon.setTargetRowId(null);
      				recon.setTargetViewId(null);
      				recon.setTargetView(null);
      				recon.setReconReference(reconReferenceId.toString());
          			recon.setReconciliationRuleName("MANUAL");
          			recon.setReconciliationRuleId(0L);
          			recon.setReconJobReference(jobReference);
          			recon.setReconciledDate(ZonedDateTime.now());
          			recon.setTenantId(tenantId);
          			recon.setReconciliationUserId(userId);
          			recon.setReconciliationRuleGroupId(groupId);
          			recon.setReconStatus("RECONCILED");
          			recon.setCurrentRecordFlag(true);
          			recon.setOriginalAmount(new BigDecimal(srcMap.get(dr.getRowId().toString()).toString()));
          			ReconciliationResult rs = reconciliationResultRepository.fetchSourceUniqueRecord(dr.getRowId(), tenantId, viewId, groupId);
              		if(rs == null)
              			allRecords.add(recon);
              		else
              			leftRecords.add(dr.getRowId());
      			}
      		}
		}
		/*sourceCount = sourceCount + allRecords.size();*/
		if(target.size()>0)
		{
	    	RuleGroup rg = ruleGroupRepository.findByIdForDisplayAndTenantId(target.get(0).getGroupId(), tenantId);
	    	Long groupId = rg.getId();
	    	ruleGroupId = groupId;
	    	
	    	DataViews dv = dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, target.get(0).getViewId());
	    	Long viewId = dv.getId();
	    	tViewId = viewId;
      		for(ViewIdRowIdDTO dr : target)
      		{
      			DataViews dataView = dataViewsRepository.findOne(viewId);
      			if(dataView != null)
      			{
      				ReconciliationResult recon = new ReconciliationResult();
      				recon.setOriginalRowId(null);
      				recon.setOriginalViewId(null);
      				recon.setOriginalView(null);
      				recon.setTargetRowId(dr.getRowId());
      				recon.setTargetViewId(viewId);
      				recon.setTargetView(dataView.getDataViewName());
      				recon.setReconReference(reconReferenceId.toString());
          			recon.setReconciliationRuleName("MANUAL");
          			recon.setReconciliationRuleId(0L);
          			recon.setReconJobReference(jobReference);
          			recon.setReconciledDate(ZonedDateTime.now());
          			recon.setTenantId(tenantId);
          			recon.setReconciliationUserId(userId);
          			recon.setReconciliationRuleGroupId(groupId);
          			recon.setReconStatus("RECONCILED");
          			recon.setCurrentRecordFlag(true);
          			recon.setTargetAmount(new BigDecimal(trMap.get(dr.getRowId().toString()).toString()));
          			ReconciliationResult rs = reconciliationResultRepository.fetchTargetUniqueRecord(dr.getRowId(), tenantId, viewId, groupId);
          			if(rs == null)
          				allRecords.add(recon);
          			else
          				leftRecords.add(dr.getRowId());
      			}
      		}
		}
      	reconciliationResultRepository.save(allRecords);	// Posting all records
      	try
      	{
          	//Updating App Module Summary Table
          	String sAmountQualifier = reconciliationResultService.getViewColumnQualifier(BigInteger.valueOf(sViewId), "AMOUNT");
    		String sDateQualifier = dashBoardV4Service.getFileDateOrQualifier(sViewId, tenantId);
          	HashMap updateCountAmntsForSrc = reconciliationResultService.updateAppModuleSummaryForSource(sAmountQualifier, sViewId, ruleGroupId, "RECONCILIATION", "SOURCE", "RECONCILED", userId, tenantId, sDateQualifier);
          	
          	String tAmountQualifier = reconciliationResultService.getViewColumnQualifier(BigInteger.valueOf(tViewId), "AMOUNT");
    		String tDateQualifier = dashBoardV4Service.getFileDateOrQualifier(tViewId, tenantId);
          	HashMap updateCountAmntsForTrgt = reconciliationResultService.updateAppModuleSummaryForTarget(tAmountQualifier, tViewId, ruleGroupId, "RECONCILIATION", "TARGET", "RECONCILED", userId, tenantId, tDateQualifier);
          	
          	HashMap<String, List<BigInteger>> viewIdsMap = new HashMap<String, List<BigInteger>>();
          	List<BigInteger> srcViewIds = new ArrayList<BigInteger>();
          	srcViewIds.add(BigInteger.valueOf(sViewId));

          	List<BigInteger> trViewIds = new ArrayList<BigInteger>();
          	trViewIds.add(BigInteger.valueOf(tViewId));
          	
          	viewIdsMap.put("sourceViewIds", srcViewIds);
          	viewIdsMap.put("targeViewIds", trViewIds);
          	
          	// Updating Multi Currency Field
    		LinkedHashMap multiCurrency = dashBoardV4Service.checkMultiCurrency(ruleGroupId, tenantId, viewIdsMap);
    		if(multiCurrency.size()>0)
    		{
    			HashMap updateMultiCurrency =  dashBoardV4Service.updateMultiCurrency(multiCurrency, ruleGroupId);
    			log.info("multiCurrencyValue: "+ updateMultiCurrency);
    		}
    		log.info("multiCurrency: "+ multiCurrency);
      	}
      	catch(Exception e)
      	{
      		log.info("Exception while updating count and amounts in app module summary table.");
      	}
      	/* Logic to check if tenant has configured for Approvals */
      	TenantConfigModules tenantConfigModulesList = tenantConfigModulesRepository.findByTenantIdAndModulesAndEnabledFlagTrue(tenantId, "RECON_APPROVALS");
      	if(tenantConfigModulesList!=null)
      	{
	      	/* Logic to initiate approvals program after manual reconciliation */
	      	String prgmName="Recon Approvals";
	      	HashMap parameterSet=new HashMap();
	      	
	      	/* Fetching ApprRuleGrp from reconRuleGrp */
	      	RuleGroup ruleGrpData=ruleGroupRepository.findOne(recRuleGrpId);
	      	if(ruleGrpData!=null && ruleGrpData.getApprRuleGrpId()!=null)
	      		apprRuleGrp=ruleGrpData.getApprRuleGrpId();
	      	parameterSet.put("param1",apprRuleGrp );
	      	parameterSet.put("param3",jobReference );
	      	oozieService.jobIntiateForAcctAndRec(tenantId,userId,prgmName,parameterSet,null,request);
      	}
      	else
      	{
      		log.info("Tenant has not configured for Approvals");
      	}
 	}
	return errorReport;
 }
	
 		/**
 		 * Author: Shiva
 		 * Description: Fetching count and amounts based on view id and original row ids
 		 * params: viewId, rowIds
 		 * **/
	    @PostMapping("/getCountAndAmountRecordWise")
	    @Timed
	    public HashMap getReconciliationDataByViewId(HttpServletRequest request, @RequestParam String viewId, @RequestBody List<Long> rowIds) throws ClassNotFoundException, SQLException
	    {
	    	log.info("Rest api for fetching count and amount based on view id "+viewId+"and row ids");
	    	HashMap finalMap = new HashMap();
	    	
	    	HashMap map=userJdbcService.getuserInfoFromToken(request);
	    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
	    	
	    	DataViews dv = dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, viewId);
	    	Long dataViewId = dv.getId();
	    	
	    	String amtQualifier = reconciliationResultService.getViewColumnQualifier(BigInteger.valueOf(dataViewId), "AMOUNT");
	    	finalMap = reconciliationResultService.getCountAndAmountRecordWise(dataViewId, rowIds, amtQualifier);
	    	
	    	return finalMap;
	    }
	    
 		/**
 		 * Author: Shiva
 		 * Description: Fetching count and amounts based on view id and original row ids
 		 * params: viewId, rowIds
 		 * **/
	    @GetMapping("/getColumnNamesWithGroupByTrue")
	    @Timed
	    public List<HashMap> getColumnNamesWithGroupByTrue(@RequestParam String viewId, HttpServletRequest request) throws ClassNotFoundException, SQLException
	    {
	    	log.info("Rest api for fetching column names with group by true for the view id: "+ viewId);
	    	
			HashMap map=userJdbcService.getuserInfoFromToken(request);
	    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
	    	
	    	DataViews dvs = dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, viewId);
	    	Long dataViewId = dvs.getId();
	    	
	    	List<HashMap> finalMap = new ArrayList<HashMap>();
	    	List<DataViewsColumns> dvcs = dataViewsColumnsRepository.findByDataViewIdAndGroupByIsTrue(dataViewId);
	    	log.info("No of records fetched: "+ dvcs.size());
	    	if(dvcs.size()>0)
	    	{
	    		for(DataViewsColumns dvc : dvcs)
	    		{
	    			HashMap hm = new HashMap();
	    			hm.put("columnId", dvc.getId());
					if("File Template".equalsIgnoreCase(dvc.getRefDvType())) 
					{
						FileTemplateLines ftl = fileTemplateLinesRepository.findOne(Long.parseLong(dvc.getRefDvColumn()));
						if(ftl != null)
						{
							hm.put("columnName", ftl.getColumnAlias());
						}
					}
					else if("Data View".equalsIgnoreCase(dvc.getRefDvType()) || dvc.getRefDvType() == null)
					{
						hm.put("columnName", dvc.getColumnName());
					}
	    			hm.put("displayName", dvc.getColumnName());
	    			finalMap.add(hm);
	    		}
	    	}
	    	log.info("Final Size: "+ finalMap.size());
	    	return finalMap;
	    }
	    
	    @GetMapping("/getColumnQualifierInfo")
	    @Timed
	    public List<HashMap> getColumnQualifierInfo(HttpServletRequest request, @RequestParam String viewId){
	    {
	    	List<HashMap> finalMap = new ArrayList<HashMap>();
	    	HashMap map=userJdbcService.getuserInfoFromToken(request);
	    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
	    	
	    	DataViews dv = dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, viewId);
	    	if(dv != null)
	    	{
	    		Long dataViewId = dv.getId();
		    	log.info("Rest api for fetching qualifiers information based on view id: "+viewId+", tenant id: "+ tenantId);
		    	List<String> lookupCodesByType = lookUpCodeRepository.fetchLookupsByTenantIdAndLookUpType(tenantId, "RECON_QUALIFIERS");
		    	if(lookupCodesByType.size() > 0)
		    	{
		    		log.info("Look up types: "+lookupCodesByType);
		    		for(String qualifier : lookupCodesByType)
		    		{
		    			log.info("ViewID:>> "+viewId+", Qualifier: "+qualifier);
		    			DataViewsColumns dvc = dataViewsColumnsRepository.findByDataViewIdAndQualifier(dataViewId, qualifier);
		    	    	if(dvc != null)
		    	    	{
		    	    		HashMap qualifierMap = new HashMap();
		    				if("File Template".equalsIgnoreCase(dvc.getRefDvType()))
		    				{
		    					FileTemplateLines ftl = fileTemplateLinesRepository.findOne(Long.parseLong(dvc.getRefDvColumn().toString()));
		    					if(ftl != null)
		    					{
		    						qualifierMap.put("qualifier", qualifier);
		    						qualifierMap.put("columnName", ftl.getColumnAlias());
		    						qualifierMap.put("columnId", dvc.getId());
		    						qualifierMap.put("columnDisplayName", dvc.getColumnName());
		    					}
		    				}
		    				else if("Data View".equalsIgnoreCase(dvc.getRefDvType()) || dvc.getRefDvType() == null)
		    				{
	    						qualifierMap.put("qualifier", qualifier);
		    					qualifierMap.put("columnName", dvc.getColumnName());
		    					qualifierMap.put("columnId", dvc.getId());
		    					qualifierMap.put("columnDisplayName", dvc.getColumnName());
		    				}
		    				finalMap.add(qualifierMap);
		    	    	}
		    		}
		    	}
		    	else
		    	{
		    		log.info("There are no look up codes found for the tenant id "+ tenantId+", look up type: RECON_QUALIFIERS");
		    	}
	    	}
	    	else
	    	{
	    		log.info("No data view id found with tenant id and id for display");
	    	}

	    	log.info("API execution completed. Final Size: "+finalMap.size());
	    	return finalMap;
	    }
	}

	 /**
	  * Author: Jagan
	  * @param reconcileRefIds, tenantId
	  * Description: Processing Manual Unreconciliation Data
	  * @return void
	 * @throws SQLException 	
	 * @throws ClassNotFoundException 
	  */
	 @PostMapping("/processManualUnReconDataAutoAcct")
	 @Timed
	 public HashMap processManualUnReconDataAutoAcct(HttpServletRequest request, @RequestBody ManualUnReconDTO params) throws SQLException
	 {
		HashMap map=userJdbcService.getuserInfoFromToken(request);
	    Long tenantId=Long.parseLong(map.get("tenantId").toString());
	    Long userId=Long.parseLong(map.get("userId").toString());
	    log.info("REST API for manual un-reconciliation for the tenan id: "+ tenantId+", user id: "+userId);
	    String groupId = params.getGroupId();
	    String type = params.getType();
	    String unReconcileType = params.getUnReconcileType();
		HashMap finalMap = new HashMap();
	    
    	RuleGroup rg = ruleGroupRepository.findByIdForDisplayAndTenantId(groupId, tenantId);
    	Long ruleGroupId = rg.getId();
    	
    	DataViews sdv = dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, params.getsViewId());
    	Long sViewId = sdv.getId();
    	
    	DataViews tdv = dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, params.gettViewId());
    	Long tViewId = tdv.getId();
    	
    	String periodFactor = params.getPeriodFactor();
    	String rangeFrom = params.getRangeFrom();
    	String rangeTo = params.getRangeTo();
    	
    	String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(new Date());
		String jobReference = "MANUAL_"+timeStamp;
		timeStamp=timeStamp.replaceAll(" ", "_");
    	
	 	int approvalCount = 0;
	 	int unReconcileCount = 0;
	 	List<RuleGroup> accountingGroupIdTagged = ruleGroupRepository.findByTenantIdAndRulePurposeAndReconciliationGroupId(tenantId, "ACCOUNTING", ruleGroupId);
	    if("recordwise".equalsIgnoreCase(type))
	 	{
	    	log.info("Record wise manual un-reconcilation started");
	    	List<String> reconRefs = params.getReconReferences();
	    	if(reconRefs.size()>0)
	 		{
	    		if("unReconcile".equalsIgnoreCase(unReconcileType))
	 			{
	    			log.info("Un Reconciliation Started...");
	 				HashMap result = reconciliationResultService.unReconcileBasedOnReconRefs(reconRefs,accountingGroupIdTagged,tenantId,userId,jobReference);
	 				approvalCount = Integer.parseInt(result.get("approvalsCount").toString());
	 				unReconcileCount = Integer.parseInt(result.get("unReconcileCount").toString());
	 			}
	    		else if("clearReconcile".equalsIgnoreCase(unReconcileType))
	 			{
	    			log.info("Clear Reconciliation Started...");
	 				List<ReconciliationResult> recResults = reconciliationResultRepository.fetchRecordsByReconReferenceIds(reconRefs);
	 				reconciliationResultRepository.delete(recResults);
	 			}
	 		}
	    	log.info("Record wise manual un-reconciliation completed");
	 	}
	    else if("batchwise".equalsIgnoreCase(type))
	    {
	    	log.info("Batchwise manual un-reconciliation...");
	    	String filterQuery = "";
	    	List<String> reconRefs = new ArrayList<String>();
	    	List<HashMap> filters = params.getFilters();
	    	if(filters.size()>0)
	    	{
	    		if("rules".equalsIgnoreCase(params.getGroupBy()) || "batch".equalsIgnoreCase(params.getGroupBy()))
	    		{
		    		for(int i=0; i<filters.size(); i++)
		    		{
	    				String grpColname = filters.get(i).get("groupingColumn").toString();
	    				String grpValue = filters.get(i).get("groupingValue").toString();
	    				String currencyColName = filters.get(i).get("currencyColumn").toString();
	    				String currencyValue = filters.get(i).get("currencyValue").toString();
		    			if(i == filters.size()-1)
		    			{
		    				if("Manual".equalsIgnoreCase(grpValue))
		    				{
			    				filterQuery = filterQuery + " (reconciliation_rule_id = 0 AND `"+currencyColName+"` = '"+currencyValue+"') ";
		    				}
		    				else
		    				{
			    				filterQuery = filterQuery + " (`"+grpColname+"` = '"+grpValue+"' AND `"+currencyColName+"` = '"+currencyValue+"') ";		    					
		    				}
		    			}
		    			else
		    			{
		    				if("Manual".equalsIgnoreCase(grpValue))
		    				{
			    				filterQuery = filterQuery + " (reconciliation_rule_id = 0 AND `"+currencyColName+"` = '"+currencyValue+"') OR ";
		    				}
		    				else
		    				{
			    				filterQuery = filterQuery + " (`"+grpColname+"` = '"+grpValue+"' AND `"+currencyColName+"` = '"+currencyValue+"') OR ";		    					
		    				}
		    			}
		    		}
	    		}
	    		else if("days".equalsIgnoreCase(params.getGroupBy()) || "approvalDate".equalsIgnoreCase(params.getGroupBy()))
	    		{
		    		for(int i=0; i<filters.size(); i++)
		    		{
	    				String grpColname = filters.get(i).get("groupingColumn").toString();
	    				String grpValue = filters.get(i).get("groupingValue").toString();
	    				String currencyColName = filters.get(i).get("currencyColumn").toString();
	    				String currencyValue = filters.get(i).get("currencyValue").toString();
		    			if(i == filters.size()-1)
		    			{
		    				filterQuery = filterQuery + " (Date(`"+grpColname+"`) = '"+grpValue+"' AND `"+currencyColName+"` = '"+currencyValue+"') ";
		    			}
		    			else
		    			{
		    				filterQuery = filterQuery + " (Date(`"+grpColname+"`) = '"+grpValue+"' AND `"+currencyColName+"` = '"+currencyValue+"') OR ";
		    			}
		    		}
	    		}
	    		else
	    		{
		    		for(int i=0; i<filters.size(); i++)
		    		{
	    				String grpColname = filters.get(i).get("groupingColumn").toString();
	    				String grpValue = filters.get(i).get("groupingValue").toString();
	    				String currencyColName = filters.get(i).get("currencyColumn").toString();
	    				String currencyValue = filters.get(i).get("currencyValue").toString();
		    			if(i == filters.size()-1)
		    			{
		    				filterQuery = filterQuery + " (`"+grpColname+"` = '"+grpValue+"' AND `"+currencyColName+"` = '"+currencyValue+"') ";
		    			}
		    			else
		    			{
		    				filterQuery = filterQuery + " (`"+grpColname+"` = '"+grpValue+"' AND `"+currencyColName+"` = '"+currencyValue+"') OR ";
		    			}
		    		}
	    		}
	    		filterQuery = " AND ("+filterQuery+")";
	    		log.info("Filter Query: "+ filterQuery);
	    	}
	    	else
	    	{
	    		filterQuery = " AND 1=1 ";
	    	}
	    	reconRefs = reconciliationResultService.getReconReferences(filterQuery, sViewId, tViewId, ruleGroupId, periodFactor, rangeFrom, rangeTo, tenantId);
	    	log.info("Recon References: "+ reconRefs);
	    	if(reconRefs.size()>0)
	    	{
	    		if("unReconcile".equalsIgnoreCase(unReconcileType))
 		 		{
	    			log.info("Batch wise un-reconciliation started");
	    			HashMap result = reconciliationResultService.unReconcileBasedOnReconRefs(reconRefs,accountingGroupIdTagged,tenantId, userId,jobReference);
	    			approvalCount = Integer.parseInt(result.get("approvalsCount").toString());
	    			unReconcileCount = Integer.parseInt(result.get("unReconcileCount").toString());
	    			log.info("Approval Count: "+ approvalCount+", UnReconciliation Count: "+ unReconcileCount);
 		 		}
	    		else if("clearReconcile".equalsIgnoreCase(unReconcileType))
	    		{
	    			List<ReconciliationResult> recResults = reconciliationResultRepository.fetchRecordsByReconReferenceIds(reconRefs);
	    			reconciliationResultRepository.delete(recResults);
	    		}
	    	}
	    }
	    
		 try
		 {
	 		 HashMap<String, List<BigInteger>> srcTrViewIds = reconciliationResultService.getDistinctDVIdsforRuleGrp(ruleGroupId, tenantId);
	 		 List<BigInteger> srcViewIds =  srcTrViewIds.get("sourceViewIds");
	 		 List<BigInteger> trViewIds = srcTrViewIds.get("targeViewIds");
	 		 if(srcViewIds.size()>0)
	 		 {
	 			 for(BigInteger srcViewId : srcViewIds)
	 			 {
	 				 String dateQualifier = dashBoardV4Service.getFileDateOrQualifier(srcViewId.longValue(), tenantId);
	 				 String sAmountQualifier = reconciliationResultService.getViewColumnQualifier(srcViewId, "AMOUNT");
	 				 HashMap updateCountAmntsForSrc = reconciliationResultService.updateAppModuleSummaryForSource(sAmountQualifier, srcViewId.longValue(), ruleGroupId, "RECONCILIATION", "SOURCE", "RECONCILED", userId, tenantId, dateQualifier);
	 			 }
	 		 }
	 		 System.out.println("Updated count and amounts for source");
	 		 if(trViewIds.size()>0)
	 		 {
	 			 for(BigInteger trViewId : trViewIds)
	 			 {
	 				 String dateQualifier = dashBoardV4Service.getFileDateOrQualifier(trViewId.longValue(), tenantId);
	 				 String tAmountQualifier = reconciliationResultService.getViewColumnQualifier(trViewId, "AMOUNT");
	 				 HashMap updateCountAmntsForTrgt = reconciliationResultService.updateAppModuleSummaryForTarget(tAmountQualifier, trViewId.longValue(), ruleGroupId, "RECONCILIATION", "TARGET", "RECONCILED", userId, tenantId, dateQualifier);
	 			 }
	 		 }
	 		 System.out.println("Updated count and amounts for target"); 
	 		 // Updating multi currency fields
	 		 LinkedHashMap multiCurrency = dashBoardV4Service.checkMultiCurrency(ruleGroupId, tenantId, srcTrViewIds);
	 		 if(multiCurrency.size()>0)
	 		 {
	 			 HashMap updateMultiCurrency =  dashBoardV4Service.updateMultiCurrency(multiCurrency, ruleGroupId);
	 		 }
		 }
		 catch(Exception e)
		 {
			 log.info("Exception while updating t_app_module_summary: "+e);
		 }
	    
	    finalMap.put("approvalsCount", approvalCount);
		finalMap.put("unReconcileCount", unReconcileCount);
		return finalMap;
	 }
	 
	 
	    @GetMapping("/getReconSummaryByRuleGroupOld")
	    @Timed
	    public List<HashMap> getReconSummaryByRuleGroupOld(HttpServletRequest request) throws ClassNotFoundException, SQLException
	    {
	    	HashMap map=userJdbcService.getuserInfoFromToken(request);
	    	Long tenantId = Long.parseLong(map.get("tenantId").toString());
	    	log.info("Rest API for fetching recon count and amounts for source and target views by reconciliation rule group for the tenant id: "+tenantId);
	    	List<HashMap> finalMap = new ArrayList<HashMap>();
	    	HashMap resultMap = new HashMap();
	    	resultMap = reconciliationResultService.getReconSummaryByRuleGroup(tenantId);
	    	if(resultMap.size()>0)
	    	{
	    		Iterator it = resultMap.entrySet().iterator();
	    		while (it.hasNext()) {
	    			HashMap group = new HashMap();
	    			Map.Entry pair = (Map.Entry)it.next();
	    			Long groupId = Long.parseLong(pair.getKey().toString());
	    			HashMap groupInfo = (HashMap) pair.getValue();
	    				
	    			group.put("groupId", groupInfo.get("groupId"));
	    			group.put("groupName", groupInfo.get("groupName"));
	    			List<HashMap> summary = (List<HashMap>) groupInfo.get("summary");
	    			group.put("summary", summary);
	    			finalMap.add(group);
	    		}
	    	}
	    	return finalMap;
	    }
	    
	    /**
	     * Author: Shiva
	     * Purpose: Fetching All Reconciliation Rule Groups Summary Information
	     * **/
	    @GetMapping("/getReconSummaryByRuleGroup")
	    @Timed
	    public HashMap getReconSummaryByRuleGroup(HttpServletRequest request)
	    {
	    	HashMap map=userJdbcService.getuserInfoFromToken(request);
	    	Long tenantId = Long.parseLong(map.get("tenantId").toString());
	    	log.info("REST API to fetch summary information for all reconciliation rule groups for the tenant id: "+tenantId);
	    	HashMap finalMap = reconciliationResultService.getAllReconGroupsSummaryInfo(tenantId);
	    	return finalMap;
	    }
	    
	    /**
	     * Author: Shiva
	     * @throws IOException 
	     *  
	     * **/
	    @PostMapping("/getReconciledTransactions")
	    @Timed
	    public List<LinkedHashMap> reconOutBoundAPI(HttpServletRequest request,HttpServletResponse response,
	    		@RequestParam(value = "ruleGroupName", required=true) String ruleGroupName, 
	    		@RequestParam(value = "sourceViewName", required=true) String sourceViewName,
	    		@RequestParam(value = "targetViewName", required=true) String targetViewName,
	    		@RequestParam(value = "pageNumber", required=false) Long pageNumber,
	    		@RequestParam(value = "pageSize", required=false) Long pageSize,
	    		@RequestParam(value = "fileExport", required=false) String fileExport,
	    		@RequestParam(value = "fileType", required=false) String fileType,
	    		@RequestBody HashMap<String, List<String>> filterColumns) throws IOException{
	    	log.info("HashMap "+ filterColumns);
	    	// Status Reconciled, Not Reconciled
	    	HashMap map=userJdbcService.getuserInfoFromToken(request);
	    	Long tenantId = Long.parseLong(map.get("tenantId").toString());
	    	Long userId = Long.parseLong(map.get("userId").toString());
	    	List<LinkedHashMap> finalList = new ArrayList<LinkedHashMap>();
	    	String ruleIdString = "";
	    	String paginationCondition = "";
	    	if(pageNumber != null && pageSize != null)
	    	{
				Long limit = 0L;
				limit = (pageNumber * pageSize + 1)-1;
				paginationCondition = "limit "+limit+", "+pageSize;
	    	}

	    	sourceViewName = sourceViewName +"_"+tenantId;
	    	targetViewName = targetViewName +"_"+tenantId;
	    	log.info("Rule Ids String: "+ruleIdString);
	    	HashMap reconInputs = reconciliationResultService.getReconSrcTrgtRulesInfo(ruleGroupName, sourceViewName, targetViewName, ruleIdString);	    	
	    	log.info("Recon Inputs: "+reconInputs);
	    	List<Long> inputRuleIds = (List<Long>) reconInputs.get("ruleIds");
	    	List<String> columnNames = new ArrayList<String>();

	    	if(inputRuleIds.size()>0)
	    	{
	    		if(reconInputs.get("sViewId") != null && reconInputs.get("tViewId") != null)
	    		{
	    			Long sViewId = Long.parseLong(reconInputs.get("sViewId").toString());
	    			Long tViewId = Long.parseLong(reconInputs.get("tViewId").toString());
	    			Long groupId = Long.parseLong(reconInputs.get("groupId").toString());
	    			
	    			LinkedHashMap finalHeaderCols = new LinkedHashMap();
	    			LinkedHashMap srcHeaderCols = reconciliationResultService.getViewColumns(sViewId, "source");
	    			LinkedHashMap trHeaderCols = reconciliationResultService.getViewColumns(tViewId, "target");
	    			finalHeaderCols.put("rule_group_name", "Rule Group Name");
	    			finalHeaderCols.put("src_rec_ref", "Recon Reference");
	    			finalHeaderCols.putAll(srcHeaderCols);
	    			finalHeaderCols.putAll(trHeaderCols);
	    			finalHeaderCols.put("recon_rule_name", "Rule Name");
	    			finalHeaderCols.put("recon_status", "Recon Status");
	    			finalHeaderCols.put("recon_job_reference", "Recon Job Reference");
	    			finalHeaderCols.put("approval_group_name", "Approval Group Name");
	    			finalHeaderCols.put("approval_rule_name", "Approval Rule Name");
	    			
	    			Iterator itr = finalHeaderCols.entrySet().iterator();
	    			while(itr.hasNext())
	    			{
	    				Map.Entry pair = (Map.Entry)itr.next();
	    				columnNames.add(pair.getValue().toString());
	    			}

	    			String srcQuery = "";
	    			String trgtQuery = "";
			    	if(filterColumns != null && filterColumns.size()>0)
			    	{
			    		HashMap sHeaderCols = reconciliationResultService.getColHeadersMapInSequence(sViewId, groupId, tenantId, "source");
			    		HashMap tHeaderCols = reconciliationResultService.getColHeadersMapInSequence(tViewId, groupId, tenantId, "target");
			    		HashMap srcTrgtCols =  new HashMap();
			    		srcTrgtCols.putAll(sHeaderCols);
			    		srcTrgtCols.putAll(tHeaderCols);
			    		
			    		Iterator it = filterColumns.entrySet().iterator();
			    		while(it.hasNext())
			    		{
			    			Map.Entry<String, List<String>> pair = (Map.Entry<String, List<String>>)it.next();
			    			String columnName = pair.getKey();
			    			List<String> values = pair.getValue();
			    			if(sHeaderCols.containsKey(columnName))
			    			{
			    				String valuesAsString = reconciliationResultService.getStringWithStrings(values);
			    				srcQuery = srcQuery + " AND "+columnName + " in ("+valuesAsString+") ";
			    			}
			    			if(tHeaderCols.containsKey(columnName))
			    			{
			    				String valuesAsString = reconciliationResultService.getStringWithStrings(values);
			    				trgtQuery = trgtQuery + " AND "+columnName + " in ("+valuesAsString+") ";			    				
			    			}
			    		}
			    	}
	    			String inputRuleIdString = reconciliationResultService.getStringWithNumbers(inputRuleIds);
	    			finalList = reconciliationResultService.getReconciledTransactionData(sourceViewName, targetViewName, groupId, sViewId, tViewId, inputRuleIdString, paginationCondition, srcQuery, trgtQuery, finalHeaderCols);
	    			log.info("Reconciled OutBound Data Size: "+ finalList.size());
	    		}
	    		else
	    		{
	    			log.info("Source Or Target Views Ids Doesn't exist.");
	    		}
	    	}

	    	// Exporting to file
	    	if(fileExport != null && fileExport.length()>0 && fileType != null && fileType.length()>0)
	    	{
/*	    		fileService.exportToExcel(response, columnNames, finalList);*/
		    	log.info("Exporting data into the file . . .");
	        	if(fileType.equalsIgnoreCase("csv"))
	        	{
	        		log.info("Exporting data into CSV File...");
	        		response.setContentType ("application/csv");
	        		response.setHeader ("Content-Disposition", "attachment; filename=\"ReconciledTransactions.csv\"");
	        		fileExportService.jsonToCSV(finalList,columnNames,response.getWriter()); // fileExportService
		    		/*response = fileService.exportToExcel(response, columnNames, finalList);
	        		/*response = fileExportService.exportToExcel(response, columnNames, finalList);*/
	        	}
	        	if(fileType.equalsIgnoreCase("pdf"))
	        	{
	        		log.info("Exporting data into PDF File...");
/*	        		response.setContentType ("application/pdf");
	        		response.setHeader ("Content-Disposition", "attachment; filename=\"ReconciledTransactions.pdf\"");
	        		fileExportService.jsonToCSV(finalList, columnNames,response.getWriter()); */
	        		/*response = fileService.exportToExcel(response, columnNames, finalList);*/
	        		response = fileExportService.exportToExcel(response, columnNames, finalList);
	        	}
	        	else if(fileType.equalsIgnoreCase("excel"))
	        	{
/*	        		response.setContentType("application/vnd.ms-excel");
	        		response.setHeader("Content-Disposition","attachment; filename=\"ReconciledTransactions.xlsx\"");
	        		fileExportService.jsonToCSV(finalList, columnNames,response.getWriter());*/
	        		/*response = fileService.exportToExcel(response, columnNames, finalList);*/
	        		response = fileExportService.exportToExcel(response, columnNames, finalList);
	        	}	   
	        	return null;
	    	}
	    	else
	    	{
		    	return finalList;
	    	}
	    }
	    
	    /**
	     * Author: Shiva
	     * @throws IOException 
	     *  
	     * **/
	    @PostMapping("/getUnReconciledTransactions")
	    @Timed
	    public List<LinkedHashMap> unReconOutBoundAPI(HttpServletRequest request,HttpServletResponse response,
	    		@RequestParam(value = "ruleGroupName", required=true) String ruleGroupName, 
	    		@RequestParam(value = "sourceViewName", required=false) String sourceViewName,
	    		@RequestParam(value = "targetViewName", required=false) String targetViewName,
	    		@RequestParam(value = "pageNumber", required=false) Long pageNumber,
	    		@RequestParam(value = "pageSize", required=false) Long pageSize,
	    		@RequestParam(value = "fileExport", required=false) String fileExport,
	    		@RequestParam(value = "fileType", required=false) String fileType,
	    		@RequestBody HashMap<String, List<String>> filterColumns) throws IOException{
	    	
	    	log.info("Rest API for fetching un reconciled transactions data");
	    	String paginationCondition = "";
	    	if(pageNumber != null && pageSize != null)
	    	{
				Long limit = 0L;
				limit = (pageNumber * pageSize + 1)-1;
				paginationCondition = "limit "+limit+", "+pageSize;
	    	}

	    	HashMap map=userJdbcService.getuserInfoFromToken(request);
	    	Long tenantId = Long.parseLong(map.get("tenantId").toString());
	    	Long userId = Long.parseLong(map.get("userId").toString());

	    	List<LinkedHashMap> finalList = new ArrayList<LinkedHashMap>();
	    	HashMap inputsInfo = null;
	    	String sourceOrTarget = "";
	    	if(sourceViewName != null)
	    	{
	    		sourceViewName = sourceViewName+"_"+tenantId;
	    		inputsInfo = reconciliationResultService.getReconInputsInfo("source", ruleGroupName, sourceViewName);
	    		sourceOrTarget = "source";
	    	}
	    	else if(targetViewName != null)
	    	{
	    		targetViewName = targetViewName+"_"+tenantId;
	    		inputsInfo = reconciliationResultService.getReconInputsInfo("target", ruleGroupName, targetViewName);
	    		sourceOrTarget = "target";
	    	}
	    	log.info("Recon Input Parameter Obj: "+ inputsInfo);
	    	LinkedHashMap headerColumns = null;
	    	if(inputsInfo != null && inputsInfo.size()>0)
	    	{
	    		Long groupId = Long.parseLong(inputsInfo.get("groupId").toString());
	    		Long viewId = Long.parseLong(inputsInfo.get("viewId").toString());
	    		String viewName = inputsInfo.get("viewName").toString();
	    		String filterQuery = "";
	    		headerColumns = reconciliationResultService.getViewColumns(viewId, sourceOrTarget);
/*    			LinkedHashMap srcHeaderCols = reconciliationResultService.getViewColumns(sViewId, "source");
    			LinkedHashMap trHeaderCols = reconciliationResultService.getViewColumns(tViewId, "target");*/
	    		List<String> columnNames = new ArrayList<String>();
    			Iterator itr = headerColumns.entrySet().iterator();
    			while(itr.hasNext())
    			{
    				Map.Entry pair = (Map.Entry)itr.next();
    				columnNames.add(pair.getValue().toString());
    			}
	    		
/*	    		colString = reconciliationResultService.getColumnsAsList(headerColumns);*/
	    		
	    		if(filterColumns != null && filterColumns.size()>0)
		    	{
		    		Iterator it = filterColumns.entrySet().iterator();
		    		while(it.hasNext())
		    		{
		    			Map.Entry<String, List<String>> pair = (Map.Entry<String, List<String>>)it.next();
		    			String columnName = pair.getKey();
		    			List<String> values = pair.getValue();
		    			if(headerColumns.containsKey(columnName))
		    			{
		    				String valuesAsString = reconciliationResultService.getStringWithStrings(values);
		    				filterQuery = filterQuery + " AND "+columnName + " in ("+valuesAsString+") ";
		    			}
		    		}
		    	}
	    		finalList = reconciliationResultService.getUnReconciledTransactions(sourceOrTarget, groupId, viewId, viewName, paginationCondition, filterQuery, headerColumns);
	    		log.info("Final Data Size: "+ finalList.size());
	    		
		    	// Exporting to file
		    	if(fileExport != null && fileExport.length()>0 && fileType != null && fileType.length()>0)
		    	{
			    	log.info("Exporting data into the file . . .");
		        	if(fileType.equalsIgnoreCase("csv"))
		        	{
		        		log.info("Exporting data into CSV File...");
		        		response.setContentType ("application/csv");
		        		response.setHeader ("Content-Disposition", "attachment; filename=\"UnRecTransactions.csv\"");
		        		fileExportService.jsonToCSV(finalList,columnNames,response.getWriter());
		        	}
		        	if(fileType.equalsIgnoreCase("pdf"))
		        	{
		        		log.info("Exporting data into PDF File...");
		        		response.setContentType ("application/pdf");
		        		response.setHeader ("Content-Disposition", "attachment; filename=\"UnRecTransactions.pdf\"");
		        		fileExportService.jsonToCSV(finalList, columnNames,response.getWriter());
		        	}
		        	else if(fileType.equalsIgnoreCase("excel"))
		        	{
/*		        		response.setContentType("application/vnd.ms-excel");
		        		response.setHeader("Content-Disposition","attachment; filename=\"UnRecTransactions.xlsx\""
		        				);
		        		fileExportService.jsonToCSV(finalList, columnNames,response.getWriter());*/
		        		response = fileExportService.exportToExcel(response, columnNames, finalList);
		        	}	
		        	return null;
		    	}	
		    	else
		    	{
			    	return finalList;
		    	}
	    	}
	    	else 
	    	{
	    		return finalList;
	    	}
	    }
	    
	    // New Changes Dynamically Fetching Summary Info
	    @GetMapping("/getReconGroupingColumnsInfo")
	    @Timed
	    public HashMap getReconGroupingColumnsInfo(@RequestParam String groupId, @RequestParam String viewId, String dateQualifierType, HttpServletRequest request) throws ClassNotFoundException, SQLException
	    {
	    	HashMap finalMap = new HashMap();
	    	log.info("REST API for fetching reconciliation grouping columns for the view id "+ viewId);
	    	List<HashMap> recon = new ArrayList<HashMap>();
	    	List<HashMap> unRecon = new ArrayList<HashMap>();
	    	List<String> errorReport = new ArrayList<String>();
	    	
	    	HashMap map=userJdbcService.getuserInfoFromToken(request);
	    	Long tenantId=Long.parseLong(map.get("tenantId").toString());

	    	RuleGroup rg = ruleGroupRepository.findByIdForDisplayAndTenantId(groupId, tenantId);
	    	Long ruleGroupId = rg.getId();
	    	
	    	DataViews dvs = dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, viewId);
	    	Long dataViewId = dvs.getId();
	    	
	    	List<Long> qualifierIds = new ArrayList<Long>();
	    	
	    	// Reconciled
	    	HashMap ruleMap = new HashMap();
	    	ruleMap.put("columnId", "");
	    	ruleMap.put("columnName", "rule_code");
	    	ruleMap.put("displayName", "Sub Process");
	    	ruleMap.put("dataType", "STRING");
	    	
	    	HashMap jobMap = new HashMap();
	    	jobMap.put("columnId", "");
	    	jobMap.put("columnName", "recon_job_reference");
	    	jobMap.put("displayName", "Batch");
	    	jobMap.put("dataType", "STRING");	    	
	    	
	    	recon.add(ruleMap);
	    	recon.add(jobMap);
	    	
	    	// Period
	    	if("FILEDATE".equalsIgnoreCase(dateQualifierType))
	    	{
	    		DataViewsColumns dateDVC = dataViewsColumnsRepository.findByDataViewIdAndQualifier(dataViewId, "FILEDATE");
	    		if(dateDVC != null)
	    		{
	    			HashMap dateMap = reconciliationResultService.getColumnInfo(dateDVC.getId());
	    			dateMap.put("displayName", "Period");
	    			recon.add(dateMap);
	    			unRecon.add(dateMap);
	    			qualifierIds.add(dateDVC.getId());
	    		}
	    	}
	    	else if("TRANSDATE".equalsIgnoreCase(dateQualifierType))
	    	{
	    		DataViewsColumns dateDVC = dataViewsColumnsRepository.findByDataViewIdAndQualifier(dataViewId, "TRANSDATE");
	    		if(dateDVC != null)
	    		{
	    			HashMap dateMap = reconciliationResultService.getColumnInfo(dateDVC.getId());
	    			dateMap.put("displayName", "Period");
	    			recon.add(dateMap);
	    			unRecon.add(dateMap);
	    			qualifierIds.add(dateDVC.getId());
	    		}
	    	}
	    	
	    	// Currency
	    	DataViewsColumns currencyDVC = dataViewsColumnsRepository.findByDataViewIdAndQualifier(dataViewId, "CURRENCYCODE");
	    	if(currencyDVC != null)
	    	{
	    		HashMap currMap = reconciliationResultService.getColumnInfo(currencyDVC.getId());
    			// recon.add(currMap);
    			// unRecon.add(currMap);
    			qualifierIds.add(currencyDVC.getId());
	    	}
	    	
	    	// True Columns
	    	List<DataViewsColumns> dvcs = dataViewsColumnsRepository.findByDataViewIdAndGroupByIsTrue(dataViewId);
	    	for(DataViewsColumns dvc : dvcs)
	    	{
	    		if(!qualifierIds.contains(dvc.getId()))
	    		{
		    		HashMap currMap = reconciliationResultService.getColumnInfo(dvc.getId());
	    			recon.add(currMap);
	    			unRecon.add(currMap);
	    		}
	    	}
	    	
	    	RuleGroup ruleGroup = ruleGroupRepository.findOne(ruleGroupId);
	    	if(ruleGroup.getApprRuleGrpId() != null)
	    	{
		    	HashMap apprStatusMap = new HashMap();
		    	apprStatusMap.put("columnId", "");
		    	apprStatusMap.put("columnName", "final_status");
		    	apprStatusMap.put("displayName", "Approval Status");
		    	apprStatusMap.put("dataType", "STRING");
		    	
		    	HashMap apprRuleMap = new HashMap();
		    	apprRuleMap.put("columnId", "");
		    	apprRuleMap.put("columnName", "rule_code");
		    	apprRuleMap.put("displayName", "Approval Rules");
		    	apprRuleMap.put("dataType", "STRING");
		    	
		    	HashMap apprDateMap = new HashMap();
		    	apprDateMap.put("columnId", "");
		    	apprDateMap.put("columnName", "final_action_date");
		    	apprDateMap.put("displayName", "Approved By Date");
		    	apprDateMap.put("dataType", "DATE");

		    	recon.add(apprStatusMap);
		    	recon.add(apprRuleMap);
		    	recon.add(apprDateMap);
	    	}
	    	
	    	finalMap.put("recon", recon);
	    	finalMap.put("unRecon", unRecon);

	    	return finalMap;
	    }
}