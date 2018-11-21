package com.nspl.app.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.nspl.app.domain.AccountedSummary;
import com.nspl.app.domain.AccountingData;
import com.nspl.app.domain.DataViews;
import com.nspl.app.domain.DataViewsColumns;
import com.nspl.app.domain.FileTemplateLines;
import com.nspl.app.domain.LookUpCode;
import com.nspl.app.domain.RuleGroup;
import com.nspl.app.repository.AccountedSummaryRepository;
import com.nspl.app.repository.AccountingDataRepository;
import com.nspl.app.repository.AccountingLineTypesRepository;
import com.nspl.app.repository.AcctRuleConditionsRepository;
import com.nspl.app.repository.AppModuleSummaryRepository;
import com.nspl.app.repository.DataMasterRepository;
import com.nspl.app.repository.DataViewsColumnsRepository;
import com.nspl.app.repository.DataViewsRepository;
import com.nspl.app.repository.FileTemplateLinesRepository;
import com.nspl.app.repository.LookUpCodeRepository;
import com.nspl.app.repository.RuleGroupDetailsRepository;
import com.nspl.app.repository.RuleGroupRepository;
import com.nspl.app.repository.RulesRepository;
import com.nspl.app.repository.TenantConfigModulesRepository;
import com.nspl.app.repository.search.AccountingDataSearchRepository;
import com.nspl.app.service.AccountingDataService;
import com.nspl.app.service.DashBoardV4Service;
import com.nspl.app.service.DataViewsService;
import com.nspl.app.service.FileExportService;
import com.nspl.app.service.FileService;
import com.nspl.app.service.PropertiesUtilService;
import com.nspl.app.service.ReconciliationResultService;
import com.nspl.app.service.UserJdbcService;
import com.nspl.app.web.rest.dto.AWQDetailInfoDTO;
import com.nspl.app.web.rest.dto.AWQGroupingDTO;
import com.nspl.app.web.rest.dto.AWQStatusesDTO;
import com.nspl.app.web.rest.dto.ErrorReport;
import com.nspl.app.web.rest.dto.ErrorReporting;
import com.nspl.app.web.rest.dto.ManualAccountingDTO;
import com.nspl.app.web.rest.dto.ManualUnAccAutoAccDTO;
import com.nspl.app.web.rest.dto.UnAccountedGroupingDTO;
import com.nspl.app.web.rest.util.HeaderUtil;
import com.nspl.app.web.rest.util.PaginationUtil;

/**
 * REST controller for managing AccountingData.
 */
@RestController
@RequestMapping("/api")
public class AccountingDataResource {
	
	

	private final Logger log = LoggerFactory
			.getLogger(AccountingDataResource.class);

	private static final String ENTITY_NAME = "accountingData";

	private final AccountingDataRepository accountingDataRepository;

	private final AccountingDataSearchRepository accountingDataSearchRepository;

	@Inject
	AccountingDataService accountingDataService;

	@Inject
	DataMasterRepository dataMasterRepository;

	@Inject
	ReconciliationResultService reconciliationResultService;

	@Inject
	RuleGroupDetailsRepository ruleGroupDetailsRepository;

	@Inject
	LookUpCodeRepository lookUpCodeRepository;

	@Inject
	RulesRepository rulesRepository;

	@Inject
	RuleGroupRepository ruleGroupRepository;

	@Inject
	RuleGroupResource ruleGroupResource;

	@Inject
	AcctRuleConditionsRepository acctRuleConditionsRepository;

	@Inject
	AccountingLineTypesRepository accountingLineTypesRepository;

	@Inject
	DataViewsColumnsRepository dataViewsColumnsRepository;

	@Inject
	DataViewsRepository dataViewsRepository;

	@Inject
	AccountedSummaryRepository accountedSummaryRepository;

	@Inject
	FileTemplateLinesRepository fileTemplateLinesRepository;

	@Inject
	PropertiesUtilService propertiesUtilService;
	
	@Inject
	DataViewsService dataViewsService;
	
	@Inject
	AppModuleSummaryRepository appModuleSummaryRepository;

	@Inject
	FileService fileService;
	
	@Inject
	UserJdbcService userJdbcService;
	
	@Inject
	TenantConfigModulesRepository tenantConfigModulesRepository;

    @Inject
    FileExportService fileExportService;
    
    @Inject
    DashBoardV4Service dashBoardV4Service;

	@PersistenceContext(unitName = "default")
	private EntityManager em;

	public AccountingDataResource(
			AccountingDataRepository accountingDataRepository,
			AccountingDataSearchRepository accountingDataSearchRepository) {
		this.accountingDataRepository = accountingDataRepository;
		this.accountingDataSearchRepository = accountingDataSearchRepository;
	}

	/**
	 * POST /accounting-data : Create a new accountingData.
	 *
	 * @param accountingData
	 *            the accountingData to create
	 * @return the ResponseEntity with status 201 (Created) and with body the
	 *         new accountingData, or with status 400 (Bad Request) if the
	 *         accountingData has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/accounting-data")
	@Timed
	public ResponseEntity<AccountingData> createAccountingData(
			@RequestBody AccountingData accountingData)
			throws URISyntaxException {
		log.debug("REST request to save AccountingData : {}", accountingData);
		if (accountingData.getId() != null) {
			return ResponseEntity
					.badRequest()
					.headers(
							HeaderUtil
									.createFailureAlert(ENTITY_NAME,
											"idexists",
											"A new accountingData cannot already have an ID"))
					.body(null);
		}
		AccountingData result = accountingDataRepository.save(accountingData);
		accountingDataSearchRepository.save(result);
		return ResponseEntity
				.created(new URI("/api/accounting-data/" + result.getId()))
				.headers(
						HeaderUtil.createEntityCreationAlert(ENTITY_NAME,
								result.getId().toString())).body(result);
	}

	/**
	 * PUT /accounting-data : Updates an existing accountingData.
	 *
	 * @param accountingData
	 *            the accountingData to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         accountingData, or with status 400 (Bad Request) if the
	 *         accountingData is not valid, or with status 500 (Internal Server
	 *         Error) if the accountingData couldnt be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/accounting-data")
	@Timed
	public ResponseEntity<AccountingData> updateAccountingData(
			@RequestBody AccountingData accountingData)
			throws URISyntaxException {
		log.debug("REST request to update AccountingData : {}", accountingData);
		if (accountingData.getId() == null) {
			return createAccountingData(accountingData);
		}
		AccountingData result = accountingDataRepository.save(accountingData);
		accountingDataSearchRepository.save(result);
		return ResponseEntity
				.ok()
				.headers(
						HeaderUtil.createEntityUpdateAlert(ENTITY_NAME,
								accountingData.getId().toString()))
				.body(result);
	} 

	/**
	 * GET /accounting-data : get all the accountingData.
	 *
	 * @param pageable
	 *            the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         accountingData in body
	 */
	@GetMapping("/accounting-data")
	@Timed
	public ResponseEntity<List<AccountingData>> getAllAccountingData(
			@ApiParam Pageable pageable) {
		log.debug("REST request to get a page of AccountingData");
		Page<AccountingData> page = accountingDataRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
				page, "/api/accounting-data");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /accounting-data/:id : get the "id" accountingData.
	 *
	 * @param id
	 *            the id of the accountingData to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         accountingData, or with status 404 (Not Found)
	 */
	@GetMapping("/accounting-data/{id}")
	@Timed
	public ResponseEntity<AccountingData> getAccountingData(
			@PathVariable Long id) {
		log.debug("REST request to get AccountingData : {}", id);
		AccountingData accountingData = accountingDataRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(accountingData));
	}

	/**
	 * DELETE /accounting-data/:id : delete the "id" accountingData.
	 *
	 * @param id
	 *            the id of the accountingData to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/accounting-data/{id}")
	@Timed
	public ResponseEntity<Void> deleteAccountingData(@PathVariable Long id) {
		log.debug("REST request to delete AccountingData : {}", id);
		accountingDataRepository.delete(id);
		accountingDataSearchRepository.delete(id);
		return ResponseEntity
				.ok()
				.headers(
						HeaderUtil.createEntityDeletionAlert(ENTITY_NAME,
								id.toString())).build();
	}

	/**
	 * SEARCH /_search/accounting-data?query=:query : search for the
	 * accountingData corresponding to the query.
	 *
	 * @param query
	 *            the query of the accountingData search
	 * @param pageable
	 *            the pagination information
	 * @return the result of the search
	 */
	@GetMapping("/_search/accounting-data")
	@Timed
	public ResponseEntity<List<AccountingData>> searchAccountingData(
			@RequestParam String query, @ApiParam Pageable pageable) {
		log.debug(
				"REST request to search for a page of AccountingData for query {}",
				query);
		Page<AccountingData> page = accountingDataSearchRepository.search(
				queryStringQuery(query), pageable);
		HttpHeaders headers = PaginationUtil
				.generateSearchPaginationHttpHeaders(query, page,
						"/api/_search/accounting-data");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * Author: Shiva Description: Api to fetch columns Aligns
	 * 
	 * @param viewName
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	@GetMapping("/getAccountingColumnAlignmentInfo")
	@Timed
	public HashMap getAccountingColumnAlignmentInfo(HttpServletRequest request,@RequestParam String groupId,
			@RequestParam String viewId, @RequestParam String status) throws ClassNotFoundException,
			SQLException {
		log.info("Rest api to fetching Recon view columns alignments details for the view id: "+ viewId + ", group id: " + groupId );
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		RuleGroup rg = ruleGroupRepository.findByIdForDisplayAndTenantId(groupId, tenantId);
    	Long ruleGroupId = rg.getId();
    	
    	DataViews dvs = dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, viewId);
    	Long dataViewId = dvs.getId();
		HashMap finalMap = new HashMap();
		finalMap.put("viewId", viewId);
		List<HashMap> cols = accountingDataService.getAccColsAlignInfo(dataViewId,ruleGroupId, tenantId,status);
		finalMap.put("columns", cols);
		return finalMap;
	}
	
	@GetMapping("/getColumnAllignForAccountedSummary")
	@Timed
	public HashMap getColumnInfoForAccountedSummary(/*
			@RequestParam Long tenantId, @RequestParam Long groupId,
			@RequestParam Long viewId,*/ @RequestParam String status) throws ClassNotFoundException,
			SQLException {
/*		log.info("Rest api to fetching Recon view columns alignments details for the view id: "
				+ viewId + ", group id: " + groupId + ", tenantId: " + tenantId);
		HashMap finalMap = new HashMap();
		finalMap.put("viewId", viewId);*/
		HashMap finalMap = new HashMap();
		List<HashMap> cols = accountingDataService.getColAlignInforAccSummary(status);
		finalMap.put("columns", cols);
		return finalMap;
	}

	// Accounting Work Queue New Form API's
	/**
	 * Author: Shiva Purpose: Fetching AWQ Header Parameters List Params:
	 * tenantId, groupId Result: HashMap with AWQ header parameters list Date:
	 * 11-12-2017
	 */
	@GetMapping("/getAccHeaderParamsList")
	@Timed
	public HashMap getReconHeaderParamsList(HttpServletRequest request, @RequestParam String groupId) {
		log.info("Rest API for fetching AWQ Header Parameters List rule group id: " + groupId);
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		HashMap finalMap = new HashMap();
    	RuleGroup rg = ruleGroupRepository.findByIdForDisplayAndTenantId(groupId, tenantId);
    	Long ruleGroupId = rg.getId();
		
		RuleGroup ruleGrp = ruleGroupRepository.findOne(ruleGroupId);
		if (ruleGrp != null) {
			finalMap.put("tenantId", tenantId);
			finalMap.put("ruleGroupId", groupId);
			finalMap.put("ruleGroupName", ruleGrp.getName());
			// Fetching Batch Names
			List<String> batchNames = new ArrayList<String>();
			batchNames = accountingDataRepository.fetchDistinctBatches(tenantId, ruleGroupId);
			finalMap.put("batchNames", batchNames);
			// Fetching data views
			List<HashMap> dataViews = new ArrayList<HashMap>();
			HashMap<String, List<BigInteger>> distinctViewIdMap = reconciliationResultService.getDistinctDVIdsforRuleGrp(ruleGroupId, tenantId);
			List<BigInteger> viewIds = distinctViewIdMap.get("sourceViewIds");
			if (viewIds.size() > 0) {
				for (BigInteger id : viewIds) {
					HashMap mp = new HashMap();
					DataViews dv = dataViewsRepository.findOne(id.longValue());
					if (dv != null) {
						mp.put("id", dv.getIdForDisplay());
						mp.put("viewName", dv.getDataViewDispName());
        				List<HashMap> trDateQualifiers = dataViewsService.getDateQualifiers(dv.getId());
        				mp.put("dateQualifiers", trDateQualifiers);
					}
					dataViews.add(mp);
				}
			} else {
				log.info("No View Ids found for the tenant id: " + tenantId + ", rule group id: " + groupId);
			}
			finalMap.put("dataViews", dataViews);
		}
		return finalMap;
	}

	/*
	 * Author: Shiva
	 * 
	 * @param ManualAccountingData, tenantId Description: Posting Manual
	 * Accounting Data
	 * 
	 * @return ErrorReport
	 */
	@PostMapping("/postManualAccData")
	@Timed
	public ErrorReport postManualAccData(
			@RequestBody ManualAccountingDTO manualAccDTO,HttpServletRequest request) throws ClassNotFoundException, SQLException {
		log.info("Rest api for posting manual accounted data ");
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		Long userId =Long.parseLong(map.get("userId").toString());
		ErrorReport errorReport = new ErrorReport();
		
    	RuleGroup rg = ruleGroupRepository.findByIdForDisplayAndTenantId(manualAccDTO.getGroupId(), tenantId);
    	Long groupId = rg.getId();
    	
    	DataViews dvs = dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, manualAccDTO.getViewId());
    	Long viewId = dvs.getId();

		String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US).format(new Date());
		timeStamp=timeStamp.replaceAll(" ", "_");
		String jobReference = "MANUAL_" + timeStamp;
		log.info("Manual Job Reference: "+jobReference);
		List<AccountedSummary> accSummary = new ArrayList<AccountedSummary>();
		HashMap singleCurPost = accountingDataService.manualAccForSingleCurrency(viewId, groupId, userId, jobReference, tenantId, manualAccDTO);
		 try{
 	 		 HashMap<String, List<BigInteger>> srcTrViewIds = reconciliationResultService.getDistinctDVIdsforRuleGrp(groupId, tenantId);
 	 		 List<BigInteger> srcViewIds =  srcTrViewIds.get("sourceViewIds");
 	 		 if(srcViewIds.size()>0)
 	 		 {
 	 			 for(BigInteger sViewId : srcViewIds)
 	 			 {
 	 				 String dateQualifier = dashBoardV4Service.getFileDateOrQualifier(sViewId.longValue(), tenantId);
 	 				 String amountQualifier = reconciliationResultService.getViewColumnQualifier(sViewId, "AMOUNT");
 	 				 HashMap updateCountAmountsAcc = accountingDataService.updateAppModuleSummaryInfoACC(groupId, sViewId.longValue(), amountQualifier, userId,tenantId, dateQualifier);
 	 			 }
 	 			 log.info("Updated count and amounts for accounting in t_app_module_summary table");
 	 			 List<BigInteger> targetViewIds = new ArrayList<BigInteger>();
 	 			 srcTrViewIds.put("targeViewIds", targetViewIds);
 	 	 		 LinkedHashMap multiCurrency = dashBoardV4Service.checkMultiCurrency(groupId, tenantId, srcTrViewIds);
 	 	 		 if(multiCurrency.size()>0)
 	 	 		 {
 	 	 			 HashMap updateMultiCurrency =  dashBoardV4Service.updateMultiCurrency(multiCurrency, groupId);
 	 	 		 }
 	 	 		 log.info("Updated multi currency fields in t_app_module_summary table");
 	 		 }
 		 }
 		 catch(Exception e)
 		 {
 			 log.info("Exception while updating count and amounts for accounting: "+e);
 		 }
		return errorReport;
	}

	@PostMapping("/getHeaderColumnsInSequence")
	@Timed
	public LinkedHashMap getColumnsSequence(@RequestParam Long viewId,@RequestParam Long tenantId, @RequestParam Long groupId, @RequestBody List<String> groupedByParams) {
	LinkedHashMap headerColumns = accountingDataService.getViewColumnHeadersMapInSequence(viewId, tenantId, groupId,groupedByParams);
		log.info("Header Columns: " + headerColumns);
		return headerColumns;
	}
	
	/**
	 * Author: Jagan, Shiva
	 * @param orginalRowIds, tenantId Description: Processing Manual Un-Accounting Data
	 * @return ErrorReport
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	@PostMapping("/manualUnAccDataAutoAcc")
	@Timed
	public ErrorReporting manualUnAccDataAutoAccounting(@RequestBody ManualUnAccAutoAccDTO params, HttpServletRequest request) throws SQLException, ClassNotFoundException{
		log.info("Rest api for processing manual un accounting data and auto accounting");
		/*Long tenantId = params.getTenantId();*/
		HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId = Long.parseLong(map.get("tenantId").toString());
    	Long userId = Long.parseLong(map.get("userId").toString());
		ErrorReporting errorReport = new ErrorReporting();
		
    	RuleGroup rg = ruleGroupRepository.findByIdForDisplayAndTenantId(params.getGroupId(), tenantId);
    	Long groupId = rg.getId();
    	
    	DataViews dvs = dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, params.getViewId());
    	Long viewId = dvs.getId();
		String type = params.getType();
		
    	params.setTenantId(tenantId);
    	params.setUserId(userId);
		/*Long userId = params.getUserId();*/
		if("recordWise".equalsIgnoreCase(type))
		{
			log.info("Mnaual un accounting for record wise...");
			List<BigInteger> originalRowIds = params.getOriginalRowIds();
			List<BigInteger> rowIds = new ArrayList<BigInteger>();
			if(originalRowIds.size()>0)
			{
				rowIds = accountedSummaryRepository.fetchEligibleRowIdsForUnAccounting(groupId, viewId, originalRowIds); 
			}

			if(rowIds.size()>0)
			{
				if("undo".equalsIgnoreCase(params.getAccountingType()))
				{
					accountingDataService.manualUnAccForTotal(rowIds,tenantId, viewId, groupId, userId);
				}
				else if("reverse".equalsIgnoreCase(params.getAccountingType()))
				{
					String jobRef = ""; 
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					java.util.Date date = new java.util.Date();
					jobRef = "MANUAL_"+dateFormat.format(date)+"RE";
					jobRef=jobRef.replaceAll(" ", "_");
					List<Long> rowIdReverse = new ArrayList<Long>();
					for(BigInteger id : rowIds)
					{
						rowIdReverse.add(id.longValue());
					}
					accountingDataService.createReveseEntries(rowIdReverse,tenantId,userId,jobRef);
				}
			}
		}
		else if("batchWise".equalsIgnoreCase(type))
		{
			log.info("Manual un accounting for batch wise start...");
			DataViews dv = dataViewsRepository.findById(viewId);
			if(dv != null)
			{
				String status = params.getStatus();
				String rangeFrom = params.getRangeFrom();
				String rangeTo = params.getRangeTo();
				// Global Search
				String globalSearch = " AND 1=1 ";
				if(params.getSearchWord() != null && params.getSearchWord().length()>0)
				{
					globalSearch = accountingDataService.getColumnNamesAsString(viewId, params.getSearchWord());
				}
				String dateQualifier = params.getPeriodFactor();
				// Building where condition for filtering grouping summary
				String whereString = "";
				List<HashMap> filters = params.getFilters();
				if(filters.size()>0)
				{
					for(HashMap filter : filters)	// Looping filter column names and values
					{
						String columnName = filter.get("key").toString();
						List<String> values = (List<String>) filter.get("values");
						String dataType = "";
						dataType = filter.get("dataType").toString();
						String innerWhereString = "";
						if(values.size()>0)
						{
							if("job_refernce".equalsIgnoreCase(columnName))
							{
								for(int i=0; i<values.size(); i++)	// job reference
								{
									if(i == values.size()-1)
									{
										innerWhereString = innerWhereString +  " LIKE '%"+values.get(i)+"%' ";
									}
									else
									{
										innerWhereString = innerWhereString + " LIKE '%"+values.get(i) +"%', ";
									}
								}
							}
							else
							{
								for(int i=0; i<values.size(); i++)	// looing column values
								{
									if(i == values.size()-1)
									{
										innerWhereString = innerWhereString +  "'"+values.get(i)+"'";
									}
									else
									{
										innerWhereString = innerWhereString + "'"+values.get(i) +"', ";
									}
								}
							}

							if("DATE".equalsIgnoreCase(dataType))
							{
								whereString = whereString + " AND Date(`"+columnName +"`) IN("+innerWhereString+")";
							}
							else
							{
								whereString = whereString + " AND `"+columnName +"` IN("+innerWhereString+")";
							}
						}
					}
				}
				List<String> activityYorN = new ArrayList<String>();
				activityYorN = accountingDataService.getActityOrNonActityBased(tenantId, groupId, viewId);
				log.info("Filter Query: "+ whereString);
				// Building where condition for column search
				List<HashMap> columnSearchMps = params.getColumnSearch();
				String columnSearchQuery = " AND 1=1 ";
				if(columnSearchMps.size()>0)
				{
					for(HashMap columnSearchMp : columnSearchMps)
					{
						String columnName = columnSearchMp.get("columnName").toString();
						String searchWord = columnSearchMp.get("searchWord").toString();
						columnSearchQuery = columnSearchQuery + " AND `" + columnName +"` LIKE '%"+searchWord+"%'";
					}
				}
				
				String srcOrTrgt = accountingDataService.identifySourceOrTarget(viewId, groupId, tenantId);
				
				List<BigInteger> totalIds = new ArrayList<BigInteger>();
				if(activityYorN.contains("Y"))
				{
					totalIds = accountingDataService.getActivityTotalIdsForBatchWise(status, dv.getDataViewName().toLowerCase(), dateQualifier, rangeFrom, rangeTo, tenantId, groupId, viewId, whereString, globalSearch,columnSearchQuery, params.getSortByColumnName(), params.getSortOrderBy(), srcOrTrgt);
				}
				else
				{
					totalIds = accountingDataService.getNonActivityTotalIdsForBatchWise(status, dv.getDataViewName().toLowerCase(), dateQualifier, rangeFrom, rangeTo, tenantId, groupId, viewId, whereString, globalSearch,columnSearchQuery, params.getSortByColumnName(), params.getSortOrderBy());
				}
				log.info("Total Ids Size: "+ totalIds.size());
				// Manual un accounting and auto accounting
				if(totalIds.size()>0)
				{
					if("undo".equalsIgnoreCase(params.getAccountingType()))
					{
						accountingDataService.manualUnAccForTotal(totalIds,tenantId, viewId, groupId, userId);
					}
					else if("reverse".equalsIgnoreCase(params.getAccountingType()))
					{
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						java.util.Date date = new java.util.Date();
						String jobRef = "MANUAL_"+dateFormat.format(date)+"RE";
						jobRef=jobRef.replaceAll(" ", "_");
						List<Long> totalIdsLong = new ArrayList<Long>();
						for(BigInteger id: totalIds)
						{
							totalIdsLong.add(id.longValue());
						}
						accountingDataService.createReveseEntries(totalIdsLong,tenantId,userId,jobRef);
					}
				}
				log.info("Manual un accounting for batch wise end...");
			}
			else
			{
				log.info("Data view doesn't exist for view id: "+ viewId);
			}
		}		
		
		 try{
 	 		 HashMap<String, List<BigInteger>> srcTrViewIds = reconciliationResultService.getDistinctDVIdsforRuleGrp(groupId, tenantId);
 	 		 List<BigInteger> srcViewIds =  srcTrViewIds.get("sourceViewIds");
 	 		 if(srcViewIds.size()>0)
 	 		 {
 	 			 for(BigInteger sViewId : srcViewIds)
 	 			 {
 	 				 String dateQualifier = dashBoardV4Service.getFileDateOrQualifier(sViewId.longValue(), tenantId);
 	 				 String amountQualifier = reconciliationResultService.getViewColumnQualifier(sViewId, "AMOUNT");
 	 				 HashMap updateCountAmountsAcc = accountingDataService.updateAppModuleSummaryInfoACC(groupId, sViewId.longValue(), amountQualifier, userId,tenantId, dateQualifier);
 	 			 }
 	 			 log.info("Updated count and amounts for accounting in t_app_module_summary table");
 	 			 List<BigInteger> targetViewIds = new ArrayList<BigInteger>();
 	 			 srcTrViewIds.put("targeViewIds", targetViewIds);
 	 	 		 LinkedHashMap multiCurrency = dashBoardV4Service.checkMultiCurrency(groupId, tenantId, srcTrViewIds);
 	 	 		 if(multiCurrency.size()>0)
 	 	 		 {
 	 	 			 HashMap updateMultiCurrency =  dashBoardV4Service.updateMultiCurrency(multiCurrency, groupId);
 	 	 		 }
 	 	 		 log.info("Updated multi currency fields in t_app_module_summary table");
 	 		 }
 		 }
 		 catch(Exception e)
 		 {
 			 log.info("Exception while updating count and amounts for accounting: "+e);
 		 }

		return errorReport;
	}	
	
	/**
	 * Author: Shiva
	 * **/
	@PostMapping("/getAWQStatusesCountsNAmounts")
	@Timed
	public HashMap getAWQStatusesCountsNAmounts(@RequestBody AWQStatusesDTO params, HttpServletRequest request)
	{
		HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	params.setTenantId(tenantId);
    	RuleGroup rg = ruleGroupRepository.findByIdForDisplayAndTenantId(params.getGroupId(), tenantId);
    	Long groupId = rg.getId();
    	
    	DataViews dvs = dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, params.getViewId());
    	Long viewId = dvs.getId();
		/*Long tenantId = params.getTenantId();*/
		String rangeFrom = params.getRangeFrom();
		String rangeTo = params.getRangeTo();
		String periodFactor = params.getPeriodFactor();
		log.info("Rest api for fetching distinct statuses counts and amounts for activity and non activity based accounting for the rule group: "+groupId+", view id: "+viewId);	
		HashMap finalMap = new HashMap();
		List<HashMap> summary = new ArrayList<HashMap>();
		String status = "";
		List<String> reasons = new ArrayList<String>();
		List<String> activityYorN = new ArrayList<String>();
		String amountQualifier = accountingDataService.getQualifierViewColName(viewId, "AMOUNT");
		String transDateQualifier = accountingDataService.getQualifierViewColName(viewId, "TRANSDATE");
		String dateQualifier = periodFactor;
		log.info("Amount qualifier: "+ amountQualifier+", Date Qualifier: "+ dateQualifier+", view id: "+ viewId);
		try {
			activityYorN = accountingDataService.getActityOrNonActityBased(tenantId, groupId, viewId);
			log.info("View Id: "+ viewId + ", activity or non activity list: "+activityYorN);
			if(dateQualifier.length()>0 && amountQualifier.length()>0)
			{
				if(activityYorN.contains("Y"))
				{
					List<HashMap> activityMps = new ArrayList<HashMap>();
					String srcOrTrgt = accountingDataService.identifySourceOrTarget(viewId, groupId, tenantId);
					activityMps = accountingDataService.getActivitySummary(viewId, groupId, amountQualifier, dateQualifier, rangeFrom, rangeTo, srcOrTrgt);
					summary.addAll(activityMps);
					log.info("Activity Based Status Summary info: "+ activityMps);
				}
				else
				{
					List<HashMap> nonActivityMps = new ArrayList<HashMap>();
					nonActivityMps = accountingDataService.getNonActivitySummary(viewId, groupId, amountQualifier, dateQualifier, rangeFrom, rangeTo);
					summary.addAll(nonActivityMps);
					log.info("Non Activity Based Status Summary info: "+ nonActivityMps);
				}
			}
			else
			{
				log.info("No amount or date qualifier found for the view id: "+ viewId);
			}

		} catch (SQLException e) {
			status = "Failed";
		}
		finalMap.put("summary", summary);
		HashMap info = new HashMap();
		info.put("status", status);
		info.put("reasons", reasons);
		finalMap.put("summary", summary);
		finalMap.put("info", info);
		return finalMap;
	}

	/**
	 * Author: Shiva
	 * @throws SQLException 
	 * **/
	@PostMapping("/getAWQGroupingSummaryInfo")
	@Timed
	public List<HashMap> getAWQGroupingSummaryInfo(@RequestBody AWQGroupingDTO params,HttpServletRequest request) throws SQLException
	{
		List<HashMap> finalMapList = new ArrayList<HashMap>();
		/*Long tenantId = params.getTenantId();*/
		HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	RuleGroup rg = ruleGroupRepository.findByIdForDisplayAndTenantId(params.getGroupId(), tenantId);
    	Long groupId = rg.getId();
    	
    	DataViews dvs = dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, params.getViewId());
    	Long viewId = dvs.getId();
		String status = params.getStatus();
		String periodFactor = params.getPeriodFactor();
		String rangeFrom = params.getRangeFrom();
		String rangeTo = params.getRangeTo();
		params.setTenantId(tenantId);
		List<String> filterList = new ArrayList<String>();
		log.info("Rest api for fetching all grouping by summary info for the status "+status);
		String amountQualifier = reconciliationResultService.getViewColumnQualifier(BigInteger.valueOf(viewId), "AMOUNT");
		DataViews dv = dataViewsRepository.findById(viewId);
		// Building where string
		String whereStringQuery = "";
		List<HashMap> filters = params.getFilters();
		if(filters.size()>0)
		{
			for(HashMap filter : filters)	// Looping filter column names and values
			{
				String columnName = filter.get("key").toString();
				List<String> values = (List<String>) filter.get("values");
				String dataType = filter.get("dataType").toString();
				String innerWhereString = "";
				if(values.size()>0)
				{
					if("final_status".equalsIgnoreCase(columnName))
					{
						for(int i=0; i<values.size(); i++)	// Need to get look up codes for look up meanings
						{
							LookUpCode luc = lookUpCodeRepository.findByMeaningAndLookUpTypeAndTenantId(values.get(i).toString(), "APPROVAL_STATUS", tenantId);
							String lookUpCode = "";
							if(luc != null)
							{
								lookUpCode = luc.getLookUpCode();
							}
							else 
							{
								lookUpCode = values.get(i).toString();
							}
							if(i == values.size()-1)
							{
								innerWhereString = innerWhereString +  "'"+lookUpCode+"'";
							}
							else
							{
								innerWhereString = innerWhereString + "'"+lookUpCode +"', ";
							}								
						}
						if(values.contains("Not Required"))
						{
							whereStringQuery = whereStringQuery + " AND (final_status IN("+innerWhereString+") OR final_status is null) ";
						}
						else if(values.contains("Awaiting for Approvals"))
						{
							whereStringQuery = whereStringQuery + " AND (final_status IN("+innerWhereString+") OR final_status = 'IN_PROCESS') ";
						}
						else
						{
							whereStringQuery = whereStringQuery + " AND final_status IN("+innerWhereString+") ";
						}
					}
					else
					{
						for(int i=0; i<values.size(); i++)	// looping column values
						{
							if(i == values.size()-1)
							{
								innerWhereString = innerWhereString +  "'"+values.get(i)+"'";
							}
							else
							{
								innerWhereString = innerWhereString + "'"+values.get(i) +"', ";
							}
						}
						if("DATE".equalsIgnoreCase(dataType))
						{
							whereStringQuery = whereStringQuery + " AND Date(`"+columnName +"`) IN("+innerWhereString+")";
						}
						else
						{
							whereStringQuery = whereStringQuery + " AND `"+columnName +"` IN("+innerWhereString+")";
						}						
					}
				}
			}
		}
		
		log.info("Filter Query: "+ whereStringQuery);
		final String whereString = whereStringQuery;
		
		// Getting dateQualifier or fileDate
		String dateGroupByValue = periodFactor;

		log.info("Date Qualifier: "+dateGroupByValue);
		final String dateGroupBy = dateGroupByValue;
		
		List<String> inputFilterList = new ArrayList<String>();
		if(filters.size()>0)
		{
			for(HashMap filter : filters)
			{
				String columnName = filter.get("key").toString();
				inputFilterList.add(columnName);
			}
		}
		log.info("Filtering columns: "+ inputFilterList);
		
		final List<String> activityYorN = accountingDataService.getActityOrNonActityBased(tenantId, groupId, viewId);
		
		log.info("ViewID: "+viewId+", ActivityORNonActivityBased: "+activityYorN);
		String srcOrTrgt = accountingDataService.identifySourceOrTarget(viewId, groupId, tenantId);
		if(!status.toLowerCase().contains("un accounted"))
		{
			// By Sub Process
			if(!inputFilterList.contains("rule_code"))
			{
				log.info("Group by Rule Code");
				Thread rulesThread = new Thread()
				{
					public void run()
					{
						List<HashMap> rulesSummary = new ArrayList<HashMap>();
						if(activityYorN.contains("Y"))
						{
							try {
								rulesSummary = accountingDataService.getGroupByActivitySummaryInfo(status, "rule_code", dv.getDataViewName().toLowerCase(), dateGroupBy, rangeFrom, rangeTo, tenantId, groupId, viewId,whereString,amountQualifier, "asc", "STRING", srcOrTrgt);
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						else
						{
							try {
								rulesSummary = accountingDataService.getGroupByNonActivitySummaryInfo(status, "rule_code", dv.getDataViewName().toLowerCase(), dateGroupBy, rangeFrom, rangeTo, tenantId, groupId, viewId,whereString,amountQualifier, "asc", "STRING");
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						log.info("Grouping Rules for the view id "+ viewId+" is: "+rulesSummary.size());
						if(rulesSummary.size()>0)
						{
							HashMap rulesMap = new HashMap();
							rulesMap.put("filterName", "rule_code");
							rulesMap.put("filterDisplyName", "Sub Process");
							rulesMap.put("summary", rulesSummary);
							finalMapList.add(rulesMap);
						}
					}
				};
				rulesThread.start();
			}
			
			// By Batch
			if(!inputFilterList.contains("job_reference"))
			{
				log.info("Group by Job Reference");
				Thread batchThread = new Thread()
				{
					public void run()
					{
						List<HashMap> batchSummary = new ArrayList<HashMap>();
						if(activityYorN.contains("Y"))
						{
							try {
								batchSummary = accountingDataService.getGroupByActivitySummaryInfo(status, "job_reference", dv.getDataViewName().toLowerCase(), dateGroupBy, rangeFrom, rangeTo, tenantId, groupId, viewId,whereString,amountQualifier, "asc", "STRING", srcOrTrgt);
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						else
						{
							try {
								batchSummary = accountingDataService.getGroupByNonActivitySummaryInfo(status, "job_reference", dv.getDataViewName().toLowerCase(), dateGroupBy, rangeFrom, rangeTo, tenantId, groupId, viewId,whereString,amountQualifier, "asc", "STRING");
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
							log.info("Grouping batches for the view id "+ viewId+" is: "+batchSummary.size());
						if(batchSummary.size()>0)
						{
							HashMap rulesMap = new HashMap();
							rulesMap.put("filterName", "job_reference");
							rulesMap.put("filterDisplyName", "Batch");
							rulesMap.put("summary", batchSummary);
							finalMapList.add(rulesMap);
						}
					}
				};
				batchThread.start();
			}		
			
			// By final status
			if(!inputFilterList.contains("final_status"))
			{
				log.info("Group by Final Status");
				Thread finalStatusThread = new Thread()
				{
					public void run()
					{
						List<HashMap> batchSummary = new ArrayList<HashMap>();
						if(activityYorN.contains("Y"))
						{
							try {
								batchSummary = accountingDataService.getGroupByActivitySummaryInfo(status, "final_status", dv.getDataViewName().toLowerCase(), dateGroupBy, rangeFrom, rangeTo, tenantId, groupId, viewId,whereString,amountQualifier, "asc", "STRING", srcOrTrgt);
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						else
						{
							try {
								batchSummary = accountingDataService.getGroupByNonActivitySummaryInfo(status, "final_status", dv.getDataViewName().toLowerCase(), dateGroupBy, rangeFrom, rangeTo, tenantId, groupId, viewId,whereString,amountQualifier, "asc", "STRING");
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						log.info("Grouping Final Status for the view id "+ viewId+" is: "+batchSummary.size());
						if(batchSummary.size()>0)
						{
							HashMap rulesMap = new HashMap();
							rulesMap.put("filterName", "final_status");
							rulesMap.put("filterDisplyName", "Approval Status");
							rulesMap.put("summary", batchSummary);
							finalMapList.add(rulesMap);
						}
					}
				};
				finalStatusThread.start();
			}
			
			// By Final Action Date
			if(!inputFilterList.contains("final_action_date"))
			{
				log.info("Group by Final Action Date");
				Thread finalActinThread = new Thread()
				{
					public void run()
					{
						List<HashMap> batchSummary = new ArrayList<HashMap>();
						if(activityYorN.contains("Y"))
						{
							try {
								batchSummary = accountingDataService.getGroupByActivitySummaryInfo(status, "final_action_date", dv.getDataViewName().toLowerCase(), dateGroupBy, rangeFrom, rangeTo, tenantId, groupId, viewId,whereString,amountQualifier, "asc", "DATE", srcOrTrgt);
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						else
						{
							try {
								batchSummary = accountingDataService.getGroupByNonActivitySummaryInfo(status, "final_action_date", dv.getDataViewName().toLowerCase(), dateGroupBy, rangeFrom, rangeTo, tenantId, groupId, viewId,whereString,amountQualifier, "asc", "DATE");
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						log.info("Grouping Approval Action Date for the view id "+ viewId+" is: "+batchSummary.size());
						if(batchSummary.size()>0)
						{
							List<HashMap> dateFormatSummary = new ArrayList<HashMap>();
							dateFormatSummary = accountingDataService.getDatesSummaryWithFormat(batchSummary);
							HashMap apprDtMap = new HashMap();
							apprDtMap.put("filterName", "final_action_date");
							apprDtMap.put("filterDisplyName", "Approved Date");
							apprDtMap.put("summary", dateFormatSummary);
							finalMapList.add(apprDtMap);
						}
					}
				};
				finalActinThread.start();
			}
			
			// By Approval Rule Id
			if(!inputFilterList.contains("approval_rule_code"))
			{
				log.info("Group by Approval Rule Code");
				Thread apprvRuleCodeThread = new Thread()
				{
					public void run()
					{
						List<HashMap> batchSummary = new ArrayList<HashMap>();
						if(activityYorN.contains("Y"))
						{
							try {
								batchSummary = accountingDataService.getGroupByActivitySummaryInfo(status, "approval_rule_code", dv.getDataViewName().toLowerCase(), dateGroupBy, rangeFrom, rangeTo, tenantId, groupId, viewId,whereString,amountQualifier, "asc", "STRING", srcOrTrgt);
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						else
						{
							try {
								batchSummary = accountingDataService.getGroupByNonActivitySummaryInfo(status, "approval_rule_code", dv.getDataViewName().toLowerCase(), dateGroupBy, rangeFrom, rangeTo, tenantId, groupId, viewId,whereString,amountQualifier, "asc", "STRING");
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
							log.info("Grouping Approval Rule Id for the view id "+ viewId+" is: "+batchSummary.size());
						if(batchSummary.size()>0)
						{
							HashMap rulesMap = new HashMap();
							rulesMap.put("filterName", "approval_rule_id");
							rulesMap.put("filterDisplyName", "Approval Rule");
							rulesMap.put("summary", batchSummary);
							finalMapList.add(rulesMap);
						}
					}
				};
				apprvRuleCodeThread.start();
			}
		}

		// Fetching group by true columns		
		List<HashMap> trueCols = accountingDataService.getGroupByColsTrueMap(viewId);
		log.info("True Cols Map: "+trueCols);
		if(trueCols.size()>0)
		{
			for(HashMap colMap : trueCols)
			{
				Thread truColThread = new Thread()
				{
					public void run()
					{
						if(!inputFilterList.contains(colMap.get("colName").toString()))
						{
							List<HashMap> columnSummary = new ArrayList<HashMap>();
							if("DATE".equalsIgnoreCase(colMap.get("dataType").toString()))
							{
								if(activityYorN.contains("Y"))
								{
									try {
										columnSummary = accountingDataService.getGroupByActivitySummaryInfo(status, colMap.get("colName").toString(), dv.getDataViewName().toLowerCase(), dateGroupBy, rangeFrom, rangeTo, tenantId, groupId, viewId,whereString,amountQualifier, "desc", colMap.get("dataType").toString(), srcOrTrgt);
									} catch (SQLException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
								else
								{
									try {
										columnSummary = accountingDataService.getGroupByNonActivitySummaryInfo(status, colMap.get("colName").toString(), dv.getDataViewName().toLowerCase(), dateGroupBy, rangeFrom, rangeTo, tenantId, groupId, viewId,whereString,amountQualifier, "desc", colMap.get("dataType").toString());
									} catch (SQLException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
								
								log.info("Grouping "+colMap.get("colDisplayName").toString()+" for the view id "+ viewId+" is: "+columnSummary.size());
								if(columnSummary.size()>0)
								{
									List<HashMap> dateFormatSummary = new ArrayList<HashMap>();
									dateFormatSummary = accountingDataService.getDatesSummaryWithFormat(columnSummary);
									HashMap rulesMap = new HashMap();
									rulesMap.put("filterName", colMap.get("colName").toString());
									rulesMap.put("filterDisplyName", colMap.get("colDisplayName").toString());
									rulesMap.put("summary", dateFormatSummary);
									finalMapList.add(rulesMap);
								}
							}
							else
							{
								if(activityYorN.contains("Y"))
								{
									try {
										columnSummary = accountingDataService.getGroupByActivitySummaryInfo(status, colMap.get("colName").toString(), dv.getDataViewName().toLowerCase(), dateGroupBy, rangeFrom, rangeTo, tenantId, groupId, viewId,whereString,amountQualifier, "asc", colMap.get("dataType").toString(), srcOrTrgt);
									} catch (SQLException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}	
								}
								else
								{
									try {
										columnSummary = accountingDataService.getGroupByNonActivitySummaryInfo(status, colMap.get("colName").toString(), dv.getDataViewName().toLowerCase(), dateGroupBy, rangeFrom, rangeTo, tenantId, groupId, viewId,whereString,amountQualifier, "asc", colMap.get("dataType").toString());
									} catch (SQLException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
								
								log.info("Grouping "+colMap.get("colDisplayName").toString()+" for the view id "+ viewId+" is: "+columnSummary.size());
								if(columnSummary.size()>0)
								{
									HashMap rulesMap = new HashMap();
									rulesMap.put("filterName", colMap.get("colName").toString());
									rulesMap.put("filterDisplyName", colMap.get("colDisplayName").toString());
									rulesMap.put("summary", columnSummary);
									finalMapList.add(rulesMap);
								}
							}
						}
					}

				};
				truColThread.start();
			}
		}
		
		// Fetching group by period
		if(!inputFilterList.contains("fileDate"))
		{
			List<HashMap> columnSummary = new ArrayList<HashMap>();
			if(activityYorN.contains("Y"))
			{
				columnSummary = accountingDataService.getGroupByActivitySummaryInfo(status, dateGroupBy, dv.getDataViewName().toLowerCase(), dateGroupBy, rangeFrom, rangeTo, tenantId, groupId, viewId,whereString,amountQualifier, "desc", "DATE", srcOrTrgt);	
			}
			else
			{
				columnSummary = accountingDataService.getGroupByNonActivitySummaryInfo(status, dateGroupBy, dv.getDataViewName().toLowerCase(), dateGroupBy, rangeFrom, rangeTo, tenantId, groupId, viewId,whereString,amountQualifier, "desc", "DATE");
			}
			
			log.info("Grouping "+dateGroupBy+" for the view id "+ viewId+" is: "+columnSummary.size());
			if(columnSummary.size()>0)
			{
				List<HashMap> dateFormatSummary = new ArrayList<HashMap>();
				dateFormatSummary = accountingDataService.getDatesSummaryWithFormat(columnSummary);
				HashMap rulesMap = new HashMap();
				rulesMap.put("filterName", dateGroupBy);
				rulesMap.put("filterDisplyName", "Period");
				rulesMap.put("summary", dateFormatSummary);
				finalMapList.add(rulesMap);
			}
		}
		return finalMapList;
	}
	
	
	/**
	 * Author: Shiva
	 * @throws SQLException 
	 * **/
	@PostMapping("/getAccountedSummaryInfo")
	@Timed
	public HashMap getAccountedSummaryInfo(@RequestBody AWQGroupingDTO params,HttpServletRequest request) throws SQLException
	{
		HashMap finalMap = new HashMap();
		/*Long tenantId = params.getTenantId();*/
		HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	RuleGroup rg = ruleGroupRepository.findByIdForDisplayAndTenantId(params.getGroupId(), tenantId);
    	Long groupId = rg.getId();
    	
		Long pageNumber = params.getPageNumber();
		Long pageSize = params.getPageSize();
		
		Long limit =  (pageNumber * pageSize + 1) - 1;
		log.info("Page Limit: " + limit + ", Page Number : " + pageNumber);
    	
    	DataViews dvs = dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, params.getViewId());
    	Long viewId = dvs.getId();
		String status = params.getStatus();
		String periodFactor = params.getPeriodFactor();
		String rangeFrom = params.getRangeFrom();
		String rangeTo = params.getRangeTo();
		List<String> filterList = new ArrayList<String>();
		log.info("Rest api for fetching all grouping by summary info for the status "+status);
		String amountQualifier = reconciliationResultService.getViewColumnQualifier(BigInteger.valueOf(viewId), "AMOUNT");
		DataViews dv = dataViewsRepository.findById(viewId);
		// Building where string
		String whereString = "";
		List<HashMap> filters = params.getFilters();
		
		if(filters.size()>0)
		{
			for(HashMap filter : filters)	// Looping filter column names and values
			{
				String columnName = filter.get("key").toString();
				List<String> values = (List<String>) filter.get("values");
				String dataType = filter.get("dataType").toString();
				String innerWhereString = "";
				if(values.size()>0)
				{
					if("final_status".equalsIgnoreCase(columnName))
					{
						for(int i=0; i<values.size(); i++)	// Need to get look up codes for look up meanings
						{
							LookUpCode luc = lookUpCodeRepository.findByMeaningAndLookUpTypeAndTenantId(values.get(i).toString(), "APPROVAL_STATUS", tenantId);
							String lookUpCode = "";
							if(luc != null)
							{
								lookUpCode = luc.getLookUpCode();
							}
							else 
							{
								lookUpCode = values.get(i).toString();
							}
							if(i == values.size()-1)
							{
								innerWhereString = innerWhereString +  "'"+lookUpCode+"'";
							}
							else
							{
								innerWhereString = innerWhereString + "'"+lookUpCode +"', ";
							}								
						}
						if(values.contains("Not Required"))
						{
							whereString = whereString + " AND (final_status IN("+innerWhereString+") OR final_status is null) ";
						}
						else if(values.contains("Awaiting for Approvals"))
						{
							whereString = whereString + " AND (final_status IN("+innerWhereString+") OR final_status = 'IN_PROCESS') ";
						}
						else
						{
							whereString = whereString + " AND final_status IN("+innerWhereString+") ";
						}
					}
					else
					{
						List<String> batchNames = new ArrayList<String>();
						for(int i=0; i<values.size(); i++)	// looping column values
						{
							if(i == values.size()-1)
							{
								innerWhereString = innerWhereString +  "'"+values.get(i)+"'";
							}
							else
							{
								innerWhereString = innerWhereString + "'"+values.get(i) +"', ";
							}
							batchNames.add(values.get(i));
						}
						if("DATE".equalsIgnoreCase(dataType))
						{
							whereString = whereString + " AND Date(`"+columnName +"`) IN("+innerWhereString+")";
						}
						else
						{
							if("job_reference".equalsIgnoreCase(columnName) && batchNames.contains("Manual"))
							{
								whereString = whereString + " AND (`"+columnName +"` IN("+innerWhereString+") OR rule_code = 'Manual')";
							}
							else
							{
								whereString = whereString + " AND `"+columnName +"` IN("+innerWhereString+")";								
							}
						}						
					}
				}
			}
		}

		log.info("Filter Query: "+ whereString);
		String srcOrTrgt = accountingDataService.identifySourceOrTarget(viewId, groupId, tenantId);
		// Getting dateQualifier or fileDate
		String dateGroupBy = periodFactor;
		log.info("Date Qualifier: "+dateGroupBy);
		
		List<String> activityYorN = new ArrayList<String>();
		activityYorN = accountingDataService.getActityOrNonActityBased(tenantId, groupId, viewId);
		log.info("ViewID: "+ viewId+", Activity Or Non Activity: "+activityYorN);
		
		if(activityYorN.contains("Y"))
		{
			log.info("Fetching Activity Based Accounted Summary Information...");
			finalMap = accountingDataService.accountedSummaryForActivity(dv.getDataViewName().toLowerCase(), dateGroupBy, rangeFrom, rangeTo, groupId, viewId, whereString, status, srcOrTrgt, params.getPeriodFactor(), limit, pageSize);
		}
		else
		{
			log.info("Fetching Non Activity Based Accounted Summary Information...");
			finalMap = accountingDataService.accountedSummaryForNonActivity(dv.getDataViewName().toLowerCase(), dateGroupBy, rangeFrom, rangeTo, groupId, viewId, whereString, status, params.getPeriodFactor(), limit, pageSize);
		}	
		log.info("Fetching Summary Info API completed");
		return finalMap;
	}
	
	/**
	 * Author: Shiva
	 * Purpose: Fetching Un Accounted Data 
	 * @throws SQLException 
	 *  **/
	@PostMapping("/getUnAccountingDetailInfo")
	@Timed
	public List<HashMap> getUnAccountedData(@RequestBody AWQDetailInfoDTO params, HttpServletRequest request) throws SQLException
	{
		log.info("REST API for fetching Un Accounted Grouping Detail Data");
		List<HashMap> finalList = new ArrayList<HashMap>();

		HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	
		String rangeFrom = params.getRangeFrom();
		String rangeTo = params.getRangeTo();
		
    	RuleGroup rg = ruleGroupRepository.findByIdForDisplayAndTenantId(params.getGroupId(), tenantId);
    	Long groupId = rg.getId();

    	DataViews dvs = dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, params.getViewId());
    	Long viewId = dvs.getId();
    	log.info("Group Id: "+ groupId+", View Id: "+ viewId);
    	
		Long pageNumber = params.getPageNumber();
		Long pageSize = params.getPageSize();
		
		Long limit =  (pageNumber * pageSize + 1) - 1;
		log.info("Page Limit: " + limit + ", Page Number : " + pageNumber);
		
		List<HashMap> filters = params.getFilters();
		String innerWhereString = "";
		if(filters.size()>0)
		{
			innerWhereString = " AND ";
			for(HashMap filter : filters)	// Looping filter column names and values
			{
				String columnName = filter.get("key").toString();
				List<String> values = (List<String>) filter.get("values");
				for(int i=0; i<values.size(); i++)	// looing column values
				{
					if(i == values.size()-1)
					{
						innerWhereString = innerWhereString +  "'"+values.get(i)+"'";
					}
					else
					{
						innerWhereString = innerWhereString + "'"+values.get(i) +"', ";
					}
				}
			}
		}
		log.info("Where String: "+ innerWhereString);
		String srcOrTrgt = accountingDataService.identifySourceOrTarget(viewId, groupId, tenantId);
		List<String> activityYorN = new ArrayList<String>();
		activityYorN = accountingDataService.getActityOrNonActityBased(tenantId, groupId, viewId);
		log.info("Activity Or Non-Activity based : "+ activityYorN);
		List<BigInteger> paginationIds = new ArrayList<BigInteger>();
		Long totalCount = 0L;
		if(activityYorN.contains("Y"))
		{
			paginationIds = accountingDataService.getPaginationIdsForUnAccActivity(groupId, viewId, params.getPeriodFactor(), rangeFrom, rangeTo, params.getStatus(), pageNumber, pageSize, "Y", innerWhereString, srcOrTrgt);
			totalCount = accountingDataService.getTotalCountForUnAccActivity(groupId, viewId, params.getPeriodFactor(), rangeFrom, rangeTo, params.getStatus(), pageNumber, pageSize, "Y", innerWhereString, srcOrTrgt);			
		}
		else
		{
			paginationIds = accountingDataService.getPaginationIdsForUnAccActivity(groupId, viewId, params.getPeriodFactor(), rangeFrom, rangeTo, params.getStatus(), pageNumber, pageSize, "N", innerWhereString, srcOrTrgt);		
			totalCount = accountingDataService.getTotalCountForUnAccActivity(groupId, viewId, params.getPeriodFactor(), rangeFrom, rangeTo, params.getStatus(), pageNumber, pageSize, "N", innerWhereString, srcOrTrgt);
		}
		log.info("Pagination Ids Size: "+paginationIds.size());
		String idsString = paginationIds.toString();
		idsString = idsString.replace("[", "");
		idsString = idsString.replace("]", "");
		String idsWhereString = "";
		HashMap headerColumns = reconciliationResultService.getViewColumnsForAcc(viewId);
		if(paginationIds.size()>0)
		{
			idsWhereString = " AND scrIds in ("+idsString+")";
			try 
			{
				if(activityYorN.contains("Y"))
				{
					finalList = accountingDataService.getUnAccountedGroupingDetailInfo(groupId, viewId, params.getPeriodFactor(), rangeFrom, rangeTo, "Y", params.getStatus(), pageNumber, pageSize, idsWhereString, headerColumns, srcOrTrgt);
				}
				else
				{
					finalList = accountingDataService.getUnAccountedGroupingDetailInfo(groupId, viewId, params.getPeriodFactor(), rangeFrom, rangeTo, "N", params.getStatus(), pageNumber, pageSize, idsWhereString, headerColumns, srcOrTrgt);					
				}
				HashMap infoObj = new HashMap();
				HashMap info = new HashMap();
				info.put("totalCount", totalCount);
				infoObj.put("info", info);
				finalList.add(infoObj);
			} 
			catch (Exception e) 
			{
				log.info("Exception while fetching un accounted grouping data: "+e);
			}
		}
		return finalList;
	}
	
	/**
	 * Author: Shiva 
	 * @throws SQLException 
	 * @throws ClassNotFoundException **/
	@PostMapping("/getAccountingDetailInfo")
	@Timed
	public List<HashMap> getAccountingDetailInfo(@RequestBody AWQDetailInfoDTO params, HttpServletRequest request,@RequestParam(value = "approvalStatus", required=false) String approvalStatus) throws SQLException, ClassNotFoundException{
		log.info("Rest API for fetching Accounting  Detail infomation");
		String status = params.getStatus();
		HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
		String rangeFrom = params.getRangeFrom();
		String rangeTo = params.getRangeTo();
		String apprStatusQuery = "";
    	RuleGroup rg = ruleGroupRepository.findByIdForDisplayAndTenantId(params.getGroupId(), tenantId);
    	Long groupId = rg.getId();
    	HashMap segSeparator = ruleGroupResource.getCoaNameAndId(params.getGroupId(), request);
    	String segmentSeparator = "";
    	if(segSeparator.size()>0 && segSeparator.get("segmentSeparator") != null)
    	{
    		segmentSeparator = segSeparator.get("segmentSeparator").toString();
    	}
    	log.info("SegmentSeparator:>> "+segmentSeparator);
    	DataViews dvs = dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, params.getViewId());
    	Long viewId = dvs.getId();
		if(approvalStatus != null)
		{
			apprStatusQuery = " and de.final_status = '"+approvalStatus+"' ";
		}
		/*Long tenantId = params.getTenantId();*/
		params.setTenantId(tenantId);
		List<HashMap> finalList = new ArrayList<HashMap>();
		Long pageNumber = params.getPageNumber();
		Long pageSize = params.getPageSize();
		HashMap headerColumns = accountingDataService.getViewColumnHeadersMapInSequence(viewId, tenantId, groupId, new ArrayList<String>());
		log.info("Header Columns: "+headerColumns);
		// Global Search
		String globalSearch = " AND 1=1 ";
		if(params.getSearchWord() != null && params.getSearchWord().length()>0)
		{
			globalSearch = accountingDataService.getColumnNamesAsString(viewId, params.getSearchWord());
		}
		
		String dateQualifier = params.getPeriodFactor();
		
		// Building where condition for filtering grouping summary
		String whereString = "";
		List<HashMap> filters = params.getFilters();
		if(filters.size()>0)
		{
			for(HashMap filter : filters)	// Looping filter column names and values
			{
				String columnName = filter.get("key").toString();
				List<String> values = (List<String>) filter.get("values");
				String dataType = "";
				dataType = filter.get("dataType").toString();
				String innerWhereString = "";
				if(values.size()>0)
				{
					if("final_status".equalsIgnoreCase(columnName))
					{
						for(int i=0; i<values.size(); i++)	// Need to get look up codes for look up meanings
						{
							//LookUpCode luc = lookUpCodeRepository.findByMeaningAndLookUpTypeAndTenantId(values.get(i).toString(), "APPROVAL_STATUS", tenantId);
							String lookUpCode = values.get(i).toString();
							/*if(luc != null)
							{
								lookUpCode = luc.getLookUpCode();
							}
							else 
							{
								lookUpCode = values.get(i).toString();
							}*/
							if(i == values.size()-1)
							{
								innerWhereString = innerWhereString +  "'"+lookUpCode+"'";
							}
							else
							{
								innerWhereString = innerWhereString + "'"+lookUpCode +"', ";
							}								
						}
						if(values.contains("Not Required"))
						{
							whereString = whereString + " AND (final_status IN("+innerWhereString+") OR final_status is null) ";
						}
						else if(values.contains("Awaiting for Approvals"))
						{
							whereString = whereString + " AND (final_status IN("+innerWhereString+") OR final_status = 'InProcess') ";
						}
						else
						{
							whereString = whereString + " AND final_status IN("+innerWhereString+") ";
						}
					}
					else
					{
						if("job_refernce".equalsIgnoreCase(columnName))
						{
							for(int i=0; i<values.size(); i++)	// looing column values
							{
								if(i == values.size()-1)
								{
									innerWhereString = innerWhereString +  " LIKE '%"+values.get(i)+"%' ";
								}
								else
								{
									innerWhereString = innerWhereString + " LIKE '%"+values.get(i) +"%', ";
								}
							}
						}
						else
						{
							for(int i=0; i<values.size(); i++)
							{
								if(i == values.size()-1)
								{
									innerWhereString = innerWhereString +  "'"+values.get(i)+"'";
								}
								else
								{
									innerWhereString = innerWhereString + "'"+values.get(i) +"', ";
								}
							}
						}
						if("DATE".equalsIgnoreCase(dataType))
						{
							whereString = whereString + " AND Date(`"+columnName +"`) IN("+innerWhereString+")";
						}
						else
						{
							whereString = whereString + " AND `"+columnName +"` IN("+innerWhereString+")";
						}											
					}
				}
			}
		}
		
		List<String> activityYorN = new ArrayList<String>();
		activityYorN = accountingDataService.getActityOrNonActityBased(tenantId, groupId, viewId);
		
		log.info("Filter Query: "+ whereString);
		
		// Building where condition for column search
		List<HashMap> columnSearchMps = params.getColumnSearch();
		String columnSearchQuery = " AND 1=1 ";
		if(columnSearchMps.size()>0)
		{
			for(HashMap columnSearchMp : columnSearchMps)
			{
				String columnName = columnSearchMp.get("columnName").toString();
				String searchWord = columnSearchMp.get("searchWord").toString();
				columnSearchQuery = columnSearchQuery + " AND `" + columnName +"` LIKE '%"+searchWord+"%'";
			}
		}
	
		Long limit =  (pageNumber * pageSize + 1) - 1;
		log.info("Page Limit: " + limit + ", Page Number : " + pageNumber);
		DataViews dv = dataViewsRepository.findById(viewId);
		List<BigInteger> paginationIds = new ArrayList<BigInteger>();
		Long totalCount = 0L;
		
		String srcOrTrgt = accountingDataService.identifySourceOrTarget(viewId, groupId, tenantId);
		
		try {
			if(activityYorN.contains("Y"))
			{
				paginationIds = accountingDataService.getPaginationIdsForActivity(status, dv.getDataViewName().toLowerCase(), dateQualifier, rangeFrom, rangeTo, tenantId, groupId, viewId, whereString, limit, pageSize, globalSearch,columnSearchQuery, params.getSortByColumnName(), params.getSortOrderBy(), apprStatusQuery, srcOrTrgt);
				totalCount = accountingDataService.getTotalCountForActivity(status, dv.getDataViewName().toLowerCase(), dateQualifier, rangeFrom, rangeTo, tenantId, groupId, viewId, whereString, limit, pageSize, globalSearch, columnSearchQuery, params.getSortByColumnName(), params.getSortOrderBy(), apprStatusQuery, srcOrTrgt);				
			}
			else
			{
				paginationIds = accountingDataService.getPaginationIdsForNonActivity(status, dv.getDataViewName().toLowerCase(), dateQualifier, rangeFrom, rangeTo, tenantId, groupId, viewId, whereString, limit, pageSize, globalSearch,columnSearchQuery, params.getSortByColumnName(), params.getSortOrderBy(), apprStatusQuery);
				totalCount = accountingDataService.getTotalCountForNonActivity(status, dv.getDataViewName().toLowerCase(), dateQualifier, rangeFrom, rangeTo, tenantId, groupId, viewId, whereString, limit, pageSize, globalSearch, columnSearchQuery, params.getSortByColumnName(), params.getSortOrderBy(), apprStatusQuery);
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		log.info("Pagination Ids Size: "+paginationIds.size());
		String idsString = paginationIds.toString();
		idsString = idsString.replace("[", "");
		idsString = idsString.replace("]", "");
		String idsWhereString = "";
		
		if(paginationIds.size()>0)
		{
			idsWhereString = " AND det.scrIds in ("+idsString+")";
			try {
				if(activityYorN.contains("Y"))
				{
					finalList = accountingDataService.getAccountingDetailInfo(dv.getDataViewName().toLowerCase(), dateQualifier, rangeFrom, rangeTo, viewId, groupId, status, idsWhereString, headerColumns, "Y", apprStatusQuery,srcOrTrgt, segmentSeparator, tenantId);
				}
				else
				{
					finalList = accountingDataService.getAccountingDetailInfo(dv.getDataViewName().toLowerCase(), dateQualifier, rangeFrom, rangeTo, viewId, groupId, status, idsWhereString, headerColumns, "N", apprStatusQuery,srcOrTrgt, segmentSeparator, tenantId);					
				}
				HashMap infoObj = new HashMap();
				HashMap info = new HashMap();
				info.put("totalCount", totalCount);
				infoObj.put("info", info);
				//info.put("totalCount", totalCount);
				finalList.add(infoObj);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		log.info("Complted API Execution");
		return finalList;
	}
	
	/**
	 * Author: Shiva
	 * Purpose: Fetching Un Accounted Grouping Summary Info
	 * ClassNotFoundException 
	 * @throws SQLException **/
	@PostMapping("/getUnAccountedGroupInfo")
	@Timed
	public HashMap getUnAccountingGrouping(@RequestBody UnAccountedGroupingDTO params, HttpServletRequest request) throws SQLException
	{
		log.info("REST API for fetching un accounted grouping summary info");
		String status = params.getStatus();
		List<HashMap> columnAlignInfo = new ArrayList<HashMap>();
		
		List<HashMap> finalList = new ArrayList<HashMap>();
		HashMap finalMap = new HashMap();
		HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	
		String rangeFrom = params.getRangeFrom();
		String rangeTo = params.getRangeTo();
		String periodFactor = params.getPeriodFactor();
		
		Long pageNumber = params.getPageNumber();
		Long pageSize = params.getPageSize();
		
		Long limit =  (pageNumber * pageSize + 1) - 1;
		log.info("Page Limit: " + limit + ", Page Number : " + pageNumber);
		
    	RuleGroup rg = ruleGroupRepository.findByIdForDisplayAndTenantId(params.getGroupId(), tenantId);
    	Long groupId = rg.getId();
    	
    	DataViews dvs = dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, params.getViewId());
    	Long viewId = dvs.getId();
    	log.info("GroupId: "+ groupId+", View Id: "+ viewId);
    	List<String> activityYorN = accountingDataService.getActityOrNonActityBased(tenantId, groupId, viewId);
    	
    	log.info("Activity or Non activity: "+activityYorN);
		List<DataViewsColumns> dvCols = new ArrayList<DataViewsColumns>();
		
		dvCols = dataViewsColumnsRepository.findByDataViewIdAndGroupByIsTrue(viewId);
		if(dvCols.size() == 0)
		{
			DataViewsColumns dvc = dataViewsColumnsRepository.findByDataViewIdAndQualifier(viewId, "CURRENCYCODE");
			dvCols.add(dvc);
		}
		
		log.info(viewId+" ViewId True columns Size: "+dvCols.size());
		// Fetching Amount columns
		List<BigInteger> amountColIds = new ArrayList<BigInteger>();
		List<BigInteger> currencyColIds = new ArrayList<BigInteger>();
		List<Long> ruleIds = ruleGroupDetailsRepository.fetchByRuleGroupIdAndTenantId(groupId, tenantId);
		log.info("Rule Ids with groupid: "+ ruleIds);
		if(ruleIds.size()>0)
		{
			List<Long> ruleIdsTaggedSrcView = rulesRepository.fetchIdsBySourceViewIdAndRuleIds(ruleIds, viewId);
			log.info("Rule Ids tagged to source view id: "+ruleIdsTaggedSrcView);
			if(ruleIdsTaggedSrcView.size()>0)
			{
				amountColIds = accountingLineTypesRepository.fetchAmoutColIdsByRuleIds(ruleIdsTaggedSrcView);
				log.info("Amount Column Ids: "+amountColIds);
				currencyColIds = rulesRepository.fetchIdsCurrencyColIdsByRuleIds(ruleIdsTaggedSrcView);
				log.info("Currency Col Ids: "+currencyColIds);
			}
			else
			{
				log.info("No rule ids");
			}
		}
		
		// Validating rule based currency column exist in group by true columns
//		for(DataViewsColumns dv : dvCols)
//		{
//			if(currencyColIds.contains(BigInteger.valueOf(dv.getId())))
//			{
//				currencyColIds.remove(BigInteger.valueOf(dv.getId()));
//			}
//		}
		
		HashMap amountsMap = new HashMap();
		HashMap currenciesMap = new HashMap();
		String amountsQuery = "";
		String currenciesQuesy = "";
		String groupBy = "";
		
		if(amountColIds.size()>0)
		{
			List<Long> amtIds = new ArrayList<Long>();
			for(BigInteger id : amountColIds)
			{
				amtIds.add(id.longValue());
			}
			amountsMap = reconciliationResultService.getColumnInfobyIds(amtIds);
			log.info("AmountsMap: "+amountsMap);
			Iterator it = amountsMap.entrySet().iterator();
		    while(it.hasNext()) 
		    {
		        Map.Entry pair = (Map.Entry)it.next();
		        amountsQuery = amountsQuery +" ifnull(SUM(`"+pair.getKey().toString()+"`), 0) `"+pair.getKey().toString()+"`, ";
		    }
		}
		if(currencyColIds.size()>0)
		{
			List<Long> curIds = new ArrayList<Long>();
			for(BigInteger id : currencyColIds)
			{
				curIds.add(id.longValue());
			}
			currenciesMap = reconciliationResultService.getColumnInfobyIds(curIds);	
			log.info("CurrenciesMap: "+currenciesMap);
			Iterator it = currenciesMap.entrySet().iterator();
		    while(it.hasNext()) 
		    {
		        Map.Entry pair = (Map.Entry)it.next();
		        currenciesQuesy = currenciesQuesy +"`"+pair.getKey().toString()+"`, ";
		        groupBy = groupBy +"`"+pair.getKey().toString()+"`,";
		    }
		    groupBy = groupBy.substring(0, groupBy.length()-1);
		    groupBy = ", "+groupBy;
		}
		log.info("Amounts Query: "+amountsQuery);
	    log.info("Currencies Query: "+currenciesQuesy);
	    log.info("Group By: "+ groupBy);
	    String srcOrTrgt = accountingDataService.identifySourceOrTarget(viewId, groupId, tenantId);
		
		if(activityYorN.contains("Y"))
		{
			String groupingColumn = "";
			if(params.getGroupByColumnName() != null && params.getGroupByColumnName().length()>0)
			{
				groupingColumn = params.getGroupByColumnName();
				HashMap summaryMap = new HashMap();
    			summaryMap = accountingDataService.getUnAcctredGroupingInfo(groupingColumn, viewId, periodFactor, rangeFrom, rangeTo, groupId, status, "Y", currenciesQuesy, amountsQuery, groupBy, currenciesMap, amountsMap, limit, pageSize, srcOrTrgt);
    			summaryMap.put("displayName", params.getGroupByColumnName());
    			summaryMap.put("groupBy", groupingColumn);
    			finalList.add(summaryMap);
			}
			else
			{
	    		for(DataViewsColumns dv : dvCols)
	    		{
	    			HashMap summaryMap = new HashMap();
					if("File Template".equalsIgnoreCase(dv.getRefDvType())) 
					{
						FileTemplateLines ftl = fileTemplateLinesRepository.findOne(Long.parseLong(dv.getRefDvColumn()));
						if(ftl != null)
						{
							groupingColumn = ftl.getColumnAlias();
			    			summaryMap = accountingDataService.getUnAcctredGroupingInfo(groupingColumn, viewId, periodFactor, rangeFrom, rangeTo, groupId, status, "Y", currenciesQuesy, amountsQuery, groupBy, currenciesMap, amountsMap, limit, pageSize, srcOrTrgt);
			    			summaryMap.put("displayName", dv.getColumnName());
			    			summaryMap.put("groupBy", groupingColumn);
						}
					}
	    			else if("Data View".equalsIgnoreCase(dv.getRefDvType()) || dv.getRefDvType() == null)
	    			{
	    				groupingColumn = dv.getColumnName();
	        			summaryMap = accountingDataService.getUnAcctredGroupingInfo(groupingColumn, viewId, periodFactor, rangeFrom, rangeTo, groupId, status, "Y", currenciesQuesy, amountsQuery, groupBy, currenciesMap, amountsMap, limit, pageSize, srcOrTrgt);
	        			summaryMap.put("displayName", dv.getColumnName());
	        			summaryMap.put("groupBy", groupingColumn);
	    			}
	    			finalList.add(summaryMap);
	    		}
			}
		}
    	else
    	{
			String groupingColumn = "";
			if(params.getGroupByColumnName() != null && params.getGroupByColumnName().length()>0)
			{
    			HashMap summaryMap = new HashMap();
				groupingColumn = params.getGroupByColumnName();
    			summaryMap = accountingDataService.getUnAcctredGroupingInfo(groupingColumn, viewId, periodFactor, rangeFrom, rangeTo, groupId, status, "N", currenciesQuesy, amountsQuery, groupBy, currenciesMap, amountsMap, limit, pageSize, srcOrTrgt);
    			summaryMap.put("displayName", params.getGroupByColumnName());
    			summaryMap.put("groupBy", groupingColumn);
    			finalList.add(summaryMap);
			}
			else
			{
	    		for(DataViewsColumns dv : dvCols)
	    		{
	    			HashMap summaryMap = new HashMap();
					if("File Template".equalsIgnoreCase(dv.getRefDvType())) 
					{
						FileTemplateLines ftl = fileTemplateLinesRepository.findOne(Long.parseLong(dv.getRefDvColumn()));
						if(ftl != null)
						{
							groupingColumn = ftl.getColumnAlias();
			    			summaryMap = accountingDataService.getUnAcctredGroupingInfo(groupingColumn, viewId, periodFactor, rangeFrom, rangeTo, groupId, status, "N", currenciesQuesy, amountsQuery, groupBy, currenciesMap, amountsMap, limit, pageSize, srcOrTrgt);
			    			summaryMap.put("displayName", dv.getColumnName());
			    			summaryMap.put("groupBy", groupingColumn);
						}
					}
	    			else if("Data View".equalsIgnoreCase(dv.getRefDvType()) || dv.getRefDvType() == null)
	    			{
	    				groupingColumn = dv.getColumnName();
	        			summaryMap = accountingDataService.getUnAcctredGroupingInfo(groupingColumn, viewId, periodFactor, rangeFrom, rangeTo, groupId, status, "N", currenciesQuesy, amountsQuery, groupBy, currenciesMap, amountsMap, limit, pageSize, srcOrTrgt);
	        			summaryMap.put("displayName", dv.getColumnName());
	        			summaryMap.put("groupBy", groupingColumn);
	    			}
	    			finalList.add(summaryMap);
	    		}				
			}
    	}
		
		Iterator it = currenciesMap.entrySet().iterator();
		
	    while(it.hasNext()) 
	    {
	    	Map.Entry pair = (Map.Entry)it.next();
//	    	HashMap curMap = new HashMap();
//	    	curMap.put("field", pair.getKey().toString());
//	    	curMap.put("header", pair.getValue().toString());
//	    	curMap.put("align", "CENTER");
//	    	curMap.put("width", "150px");
//	    	curMap.put("colId", pair.getKey().toString());
//	    	curMap.put("dataType", "STRING");
//	    	columnAlignInfo.add(curMap);
	    	finalMap.put("currencyColumn",pair.getKey().toString());
	    }
	    
		HashMap countMap = new HashMap();
		countMap.put("field", "count");
		countMap.put("header", "Transactional Count");
		countMap.put("align", "right");
		countMap.put("width", "150px");
    	countMap.put("colId", "count");
    	countMap.put("dataType", "INTEGER");
    	columnAlignInfo.add(countMap);
		
		// Amounts
		Iterator it2 = amountsMap.entrySet().iterator();
	    while(it2.hasNext()) 
	    {
	    	Map.Entry pair = (Map.Entry)it2.next();
	    	HashMap amtMap = new HashMap();
	    	
	    	amtMap.put("field", pair.getKey().toString());
	    	amtMap.put("header", pair.getValue().toString());
	    	amtMap.put("align", "right");
	    	amtMap.put("width", "150px");
	    	amtMap.put("colId", pair.getKey().toString());
	    	amtMap.put("dataType", "DECIMAL");
	    	columnAlignInfo.add(amtMap);
	    }
	    finalMap.put("summaryInfo", finalList);
	    finalMap.put("columnAlignInfo", columnAlignInfo);
	    return finalMap;
	}
	
	
	/** Author: Shiva
	 * Purpose: Fetching All Reconciliation
	 * **/
	@GetMapping("/getAcctSummaryByRuleGroup")
    @Timed
    public HashMap getAcctSummaryByRuleGroup(HttpServletRequest request) throws ClassNotFoundException, SQLException
    {
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId = Long.parseLong(map.get("tenantId").toString());
    	log.info("Rest API for fetching all accounting groups summary information for the tenant id: "+tenantId);
    	HashMap finalMap = accountingDataService.getAcctSummaryByRuleGroup(tenantId);
    	
    	return finalMap;
    }
	
	/* AUTHOR: Bhagath */
	@GetMapping("/getAcctSummaryByRuleGroupOld")
    @Timed
    public List<HashMap> getAcctSummaryByRuleGroupOld(HttpServletRequest request) throws ClassNotFoundException, SQLException
    {
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId = Long.parseLong(map.get("tenantId").toString());
    	log.info("Rest API for fetching acct count and amounts for source and target views by accounting rule group for the tenant id: "+tenantId);
    	List<HashMap> finalList = new ArrayList<HashMap>();
    	HashMap resultMap = new HashMap();
    	resultMap = accountingDataService.getAcctSummaryByRuleGroup(tenantId);
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
    			group.put("isActivityBased", groupInfo.get("isActivityBased"));
    			group.put("isMultiCurrency", groupInfo.get("isMultiCurrency"));
    			List<HashMap> summary = new ArrayList<HashMap>();
    			HashMap views = (HashMap) groupInfo.get("views");
    			Iterator viewsItr = views.entrySet().iterator();
    			while(viewsItr.hasNext())
    			{
    				Map.Entry viewPair = (Map.Entry)viewsItr.next();
    				HashMap viewInfo = (HashMap) viewPair.getValue();
    				summary.add(viewInfo);
    			}
    			group.put("summary", summary);
    			finalList.add(group);
    		}
    	}
    	return finalList;
    }
	
	
    @GetMapping("/cloneObjectTesting")
    @Timed
    public void getAccountingTransactions(@RequestParam(value = "ruleGroupId", required=true) Long ruleGroupId)
    {
    	log.info("Clone Object Testing");
    	AccountingData ad = accountingDataRepository.findOne(ruleGroupId);
    	log.info("Before Cloning: "+ad);
    	if(ad != null)
    	{
    		AccountingData adClone = new AccountingData();
    		adClone = ad;
    		adClone.setId(null);
    		log.info("After Cloning: "+ adClone);
    	}
    }
	
    @PostMapping("/getAccountingTransactions")
    @Timed
    public List<LinkedHashMap> getAccountingTransactions(HttpServletRequest request,HttpServletResponse response,
    		@RequestParam(value = "ruleGroupName", required=true) String ruleGroupName, 
    		@RequestParam(value = "viewName", required=true) String viewName,
    		@RequestParam(value = "status", required=true) String status, 
    		@RequestParam(value = "pageNumber", required=false) Long pageNumber,
    		@RequestParam(value = "pageSize", required=false) Long pageSize,
    		@RequestParam(value = "fileExport", required=false) String fileExport,
    		@RequestParam(value = "fileType", required=false) String fileType,
    		@RequestBody HashMap<String, List<String>> filterColumns) throws IOException, SQLException{
    	
    	List<LinkedHashMap> finalList = new ArrayList<LinkedHashMap>();
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId = Long.parseLong(map.get("tenantId").toString());
    	Long userId = Long.parseLong(map.get("userId").toString());
    	log.info("Rest API for fetching accounting data for outbound api for the tenant id: "+tenantId);
    	// Identifying group id and view id
    	HashMap inputsInfo = accountingDataService.getGroupIdAndViewId(ruleGroupName, viewName, tenantId);
		log.info("Inputs Info: "+inputsInfo);

    	String paginationCondition = "";
    	if(pageNumber != null && pageSize != null)
    	{
			Long limit = 0L;
			limit = (pageNumber * pageSize + 1)-1;
			paginationCondition = "limit "+limit+", "+pageSize;
    	}
		
    	if(inputsInfo.size()>0)
    	{
    		Long groupId = Long.parseLong(inputsInfo.get("groupId").toString());
    		Long viewId = Long.parseLong(inputsInfo.get("viewId").toString());
    		String dataViewName = inputsInfo.get("viewName").toString();
    		log.info("Group Id: "+ groupId+", View Id: "+ viewId);
    		LinkedHashMap finalHeaderColumns = new LinkedHashMap();
    		finalHeaderColumns.put("accounting_rule_group", "Accounting Rule Group");
    		finalHeaderColumns.put("rule_code", "Accounting Rule");
    		finalHeaderColumns.put("ledger_name", "Ledger");
    		finalHeaderColumns.put("source_meaning", "Source");
    		finalHeaderColumns.put("category_meaning", "Category");
    		finalHeaderColumns.put("coa_name", "COA");
    		finalHeaderColumns.put("accounted_currency", "Accounted Currency");
    		finalHeaderColumns.put("entered_currency", "Entered Currency");
    		finalHeaderColumns.put("line_type", "Line Type");
    		finalHeaderColumns.put("line_type_detail", "Line Type Detail");
    		finalHeaderColumns.put("accounted_amount", "Accounted Amount");
    		finalHeaderColumns.put("entered_amount", "Entered Amount");
    		finalHeaderColumns.put("entered_amount", "Entered Amount");
    		finalHeaderColumns.put("accounting_status", "Accounting Status");
    		finalHeaderColumns.put("journal_status", "Journal Status");
    		finalHeaderColumns.put("job_reference", "Job Reference");
    		finalHeaderColumns.putAll(reconciliationResultService.getViewColumnsForAcc(viewId));
    		finalHeaderColumns.put("final_status", "Approval Status");
    		finalHeaderColumns.put("approval_rule_code", "Approval Rule");
    		finalHeaderColumns.put("final_action_date", "Approved Date");
    		
    		log.info("GroupId: "+ groupId+", ViewId: "+ viewId);
    		HashMap headerColumns = accountingDataService.getViewColumnHeadersMapInSequence(viewId, tenantId, groupId, new ArrayList<String>());
    		List<String> activityYorN = new ArrayList<String>();
    		activityYorN = accountingDataService.getActityOrNonActityBased(tenantId, groupId, viewId);
    		log.info("Activity based or non activity based accounting: "+ activityYorN);
    		String srcOrTrgt = accountingDataService.identifySourceOrTarget(viewId, groupId, tenantId);
    		log.info(viewName+ " is "+srcOrTrgt+" data view");
    		String whereString = "";
    		if(activityYorN.contains("Y"))
    		{
    			log.info("Fetching Activity based accounting data...");
    			finalList = accountingDataService.getAccountingTransactionsData(srcOrTrgt, viewId, "Y", viewName+"_"+tenantId, groupId, status, whereString, paginationCondition, finalHeaderColumns);
    		}
    		else
    		{
    			log.info("Fetching Non Activity based accounting...");
    			finalList = accountingDataService.getAccountingTransactionsData(srcOrTrgt, viewId, "N", viewName+"_"+tenantId, groupId, status, whereString, paginationCondition, finalHeaderColumns);
    		}
    		// Exporting data into the files
	    	if(fileExport != null && fileExport.length()>0 && fileType != null && fileType.length()>0)
	    	{
	    		/*List<String> columnNames = reconciliationResultService.getColumnsAsList(headerColumns);*/
	    		List<String> columnNames = new ArrayList<String>();

	    		
	    		Iterator itr = finalHeaderColumns.entrySet().iterator();
    			while(itr.hasNext())
    			{
    				Map.Entry pair = (Map.Entry)itr.next();
    				columnNames.add(pair.getValue().toString());
    			}
    			
		    	log.info("Exporting data into the file . . .");
	        	if(fileType.equalsIgnoreCase("csv"))
	        	{
	        		log.info("Exporting data into CSV File...");
	        		response.setContentType ("application/csv");
	        		response.setHeader ("Content-Disposition", "attachment; filename=\"Accounting.csv\"");
	        		fileExportService.jsonToCSV(finalList,columnNames,response.getWriter());
	        	}
	        	if(fileType.equalsIgnoreCase("pdf"))
	        	{
	        		log.info("Exporting data into PDF File...");
	        		response.setContentType ("application/pdf");
	        		response.setHeader ("Content-Disposition", "attachment; filename=\"Accounting.pdf\"");
	        		fileExportService.jsonToCSV(finalList, columnNames,response.getWriter());
	        	}
	        	else if(fileType.equalsIgnoreCase("excel"))
	        	{
/*	        		response.setContentType("application/vnd.ms-excel");
	        		response.setHeader("Content-Disposition","attachment; filename=\"Accounting.xlsx\"");
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
    		log.info("No input parameters found for the group name: "+ruleGroupName+", view name: "+ viewName);
    		return finalList;
    	}
    }
}