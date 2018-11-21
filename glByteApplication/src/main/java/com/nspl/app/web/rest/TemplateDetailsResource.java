package com.nspl.app.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

import java.math.BigDecimal;
import java.math.BigInteger;
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
import com.nspl.app.domain.DataViews;
import com.nspl.app.domain.DataViewsColumns;
import com.nspl.app.domain.JeLdrDetails;
import com.nspl.app.domain.JeLines;
import com.nspl.app.domain.JournalsHeaderData;
import com.nspl.app.domain.LedgerDefinition;
import com.nspl.app.domain.LineCriteria;
import com.nspl.app.domain.LookUpCode;
import com.nspl.app.domain.MappingSet;
import com.nspl.app.domain.MappingSetValues;
import com.nspl.app.domain.RuleGroup;
import com.nspl.app.domain.Segments;
import com.nspl.app.domain.TemplAttributeMapping;
import com.nspl.app.domain.TemplateDetails;
import com.nspl.app.repository.CalendarRepository;
import com.nspl.app.repository.ChartOfAccountRepository;
import com.nspl.app.repository.DataViewsColumnsRepository;
import com.nspl.app.repository.DataViewsRepository;
import com.nspl.app.repository.JeLdrDetailsRepository;
import com.nspl.app.repository.JeLinesRepository;
import com.nspl.app.repository.JournalsHeaderDataRepository;
import com.nspl.app.repository.LedgerDefinitionRepository;
import com.nspl.app.repository.LineCriteriaRepository;
import com.nspl.app.repository.LookUpCodeRepository;
import com.nspl.app.repository.MappingSetRepository;
import com.nspl.app.repository.MappingSetValuesRepository;
import com.nspl.app.repository.RuleGroupRepository;
import com.nspl.app.repository.SegmentsRepository;
import com.nspl.app.repository.TemplAttributeMappingRepository;
import com.nspl.app.repository.TemplateDetailsRepository;
import com.nspl.app.repository.search.TemplateDetailsSearchRepository;
import com.nspl.app.security.IDORUtils;
import com.nspl.app.service.ActiveStatusService;
import com.nspl.app.service.UserJdbcService;
import com.nspl.app.web.rest.dto.JeLinesDTO;
import com.nspl.app.web.rest.dto.JournalHeaderDTO;
import com.nspl.app.web.rest.dto.JournalsResultDTO;
import com.nspl.app.web.rest.dto.LineCriteriaDTO;
import com.nspl.app.web.rest.dto.TemplAttributeMappingDTO;
import com.nspl.app.web.rest.dto.TemplateDetailsDTO;
import com.nspl.app.web.rest.util.HeaderUtil;
import com.nspl.app.web.rest.util.PaginationUtil;

/**
 * REST controller for managing TemplateDetails.
 */
@RestController
@RequestMapping("/api")
public class TemplateDetailsResource {

	private final Logger log = LoggerFactory.getLogger(TemplateDetailsResource.class);

	private static final String ENTITY_NAME = "templateDetails";

	private final TemplateDetailsRepository templateDetailsRepository;

	@Inject
	private TemplAttributeMappingRepository templAttributeMappingRepository;

	@Inject
	private LineCriteriaRepository lineCriteriaRepository;

	@Inject
	private LookUpCodeRepository lookUpCodeRepository;

	@Inject
	DataViewsColumnsRepository dataViewsColumnsRepository;

	@Inject
	DataViewsRepository dataViewsRepository; 

	@Inject
	JournalsHeaderDataRepository journalsHeaderDataRepository;

	@Inject
	JeLinesRepository jeLinesRepository;


	@Inject
	JeLdrDetailsRepository jeLdrDetailsRepository;

	@Inject
	LedgerDefinitionRepository ledgerDefinitionRepository;

	@Inject
	CalendarRepository calendarRepository;

	@Inject
	TemplateDetailsSearchRepository templateDetailsSearchRepository;

	@Inject
	ChartOfAccountRepository chartOfAccountRepository;

	@Inject
	RuleGroupRepository ruleGroupRepository;

	@Inject
	UserJdbcService userJdbcService;

	@Inject
	SegmentsRepository segmentsRepository;

	@Inject
	MappingSetRepository mappingSetRepository;


	@Inject
	MappingSetValuesRepository mappingSetValuesRepository;
	
	@Inject
	ActiveStatusService activeStatusService;

	public TemplateDetailsResource(TemplateDetailsRepository templateDetailsRepository) {
		this.templateDetailsRepository = templateDetailsRepository;
	}

	/**
	 * POST  /template-details : Create a new templateDetails.
	 *
	 * @param templateDetails the templateDetails to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new templateDetails, or with status 400 (Bad Request) if the templateDetails has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/template-details")
	@Timed
	public ResponseEntity<TemplateDetails> createTemplateDetails(@RequestBody TemplateDetails templateDetails) throws URISyntaxException {
		log.debug("REST request to save TemplateDetails : {}", templateDetails);
		if (templateDetails.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new templateDetails cannot already have an ID")).body(null);
		}
		TemplateDetails result = templateDetailsRepository.save(templateDetails);
		templateDetailsSearchRepository.save(result);
		return ResponseEntity.created(new URI("/api/template-details/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /template-details : Updates an existing templateDetails.
	 *
	 * @param templateDetails the templateDetails to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated templateDetails,
	 * or with status 400 (Bad Request) if the templateDetails is not valid,
	 * or with status 500 (Internal Server Error) if the templateDetails couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/template-details")
	@Timed
	public ResponseEntity<TemplateDetails> updateTemplateDetails(@RequestBody TemplateDetails templateDetails) throws URISyntaxException {
		log.debug("REST request to update TemplateDetails : {}", templateDetails);
		if (templateDetails.getId() == null) {
			return createTemplateDetails(templateDetails);
		}
		TemplateDetails result = templateDetailsRepository.save(templateDetails);
		templateDetailsSearchRepository.save(result);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, templateDetails.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /template-details : get all the templateDetails.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of templateDetails in body
	 */
	@GetMapping("/template-details")
	@Timed
	public ResponseEntity<List<TemplateDetails>> getAllTemplateDetails(@ApiParam Pageable pageable) {
		log.debug("REST request to get a page of TemplateDetails");
		Page<TemplateDetails> page = templateDetailsRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/template-details");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET  /template-details/:id : get the "id" templateDetails.
	 *
	 * @param id the id of the templateDetails to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the templateDetails, or with status 404 (Not Found)
	 */
	@GetMapping("/template-details/{id}")
	@Timed
	public ResponseEntity<TemplateDetails> getTemplateDetails(@PathVariable Long id) {
		log.debug("REST request to get TemplateDetails : {}", id);
		TemplateDetails templateDetails = templateDetailsRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(templateDetails));
	}

	/**
	 * DELETE  /template-details/:id : delete the "id" templateDetails.
	 *
	 * @param id the id of the templateDetails to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/template-details/{id}")
	@Timed
	public ResponseEntity<Void> deleteTemplateDetails(@PathVariable Long id) {
		log.debug("REST request to delete TemplateDetails : {}", id);
		templateDetailsRepository.delete(id);
		templateDetailsSearchRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}


	@GetMapping("/_search/template-details")
	@Timed
	public ResponseEntity<List<TemplateDetails>> searchDataViews(@RequestParam String tenantId, @RequestParam(value="columnName",required=false) String columnName, @RequestParam(value="columnValue",required=false) String columnValue, @RequestParam(value="filterValue",required=false) String filterValue, @ApiParam Pageable pageable) {
		log.info("Rest api for fetching template data from elasticsearch repository for the tenant_id: "+tenantId);
		String query = "";
		query = query + " tenantId: \""+tenantId+"\" ";		// Filtering with tenant id	

		if(filterValue != null)
		{
			query = query + " AND \""+filterValue+"\"";
		} 
		log.info("query :"+query);
		Page<TemplateDetails> page = templateDetailsSearchRepository.search(queryStringQuery(query), pageable);
		HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/template-details");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}



	@GetMapping("/getJournalsTemplateDetails")
	@Timed
	//public ResponseEntity<List<TemplateDetails>> getAllJournalsTemplateDetails(@RequestParam(value = "page" , required = false) Integer offset,
	//	@RequestParam(value = "per_page", required = false) Integer limit,@RequestParam Long tenantId) throws URISyntaxException {
	public ResponseEntity<List<LinkedHashMap>> getAllJournalsTemplateDetails(@RequestParam(value = "page" , required = false) Integer offset,
			@RequestParam(value = "per_page", required = false) Integer limit,HttpServletRequest request) throws URISyntaxException {

		log.info("REST request to get journals page details");
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		List<LinkedHashMap> finalTemplList=new ArrayList<LinkedHashMap>();

		List<TemplateDetails> journalTempDetails = new ArrayList<TemplateDetails>();
		PaginationUtil paginationUtil=new PaginationUtil();

		int maxlmt=paginationUtil.MAX_LIMIT;
		int minlmt=paginationUtil.MIN_OFFSET;
		log.info("maxlmt: "+maxlmt);
		Page<TemplateDetails> page = null;
		HttpHeaders headers = null;

		if(limit==null || limit<minlmt){
			journalTempDetails = templateDetailsRepository.findByTenantIdOrderByIdDesc(tenantId);
			limit = journalTempDetails.size();
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
			page = templateDetailsRepository.findByTenantIdOrderByIdDesc(tenantId,PaginationUtil.generatePageRequest2(offset, limit));
			headers = PaginationUtil.generatePaginationHttpHeaders2(page, "/api/getJournalsTemplateDetails",offset, limit);
		}
		else{
			log.info("input limit is within maxlimit");
			page = templateDetailsRepository.findByTenantIdOrderByIdDesc(tenantId,PaginationUtil.generatePageRequest(offset, limit));
			headers = PaginationUtil.generatePaginationHttpHeaderss(page, "/api/getJournalsTemplateDetails", offset, limit);
		}
		for(int i=0;i<page.getContent().size();i++)
		{
			LinkedHashMap lhm=new LinkedHashMap();
			lhm.put("id", page.getContent().get(i).getIdForDisplay());
			lhm.put("tenantId", page.getContent().get(i).getTenantId());
			lhm.put("targetAppSource", page.getContent().get(i).getTargetAppSource());
			lhm.put("templateName", page.getContent().get(i).getTemplateName());
			lhm.put("description", page.getContent().get(i).getDescription());
			lhm.put("viewId", page.getContent().get(i).getViewId());
			lhm.put("viewName", page.getContent().get(i).getViewName());
			if(page.getContent().get(i).getViewId()!=null)
			{
				DataViews dv=dataViewsRepository.findOne(page.getContent().get(i).getViewId());
						lhm.put("viewDisplayName", dv.getDataViewDispName());
			}
			lhm.put("startDate", page.getContent().get(i).getStartDate());
			lhm.put("endDate", page.getContent().get(i).getEndDate());
			
			/** active check**/

			Boolean activeStatus=activeStatusService.activeStatus(page.getContent().get(i).getStartDate(), page.getContent().get(i).getEndDate(), page.getContent().get(i).isEnabledFlag());
			
			lhm.put("enabledFlag", activeStatus);
			/** active check end**/
			lhm.put("createdBy", page.getContent().get(i).getCreatedBy());
			lhm.put("lastUpdatedBy", page.getContent().get(i).getLastUpdatedBy());
			lhm.put("createdDate", page.getContent().get(i).getCreatedDate());
			lhm.put("lastUpdatedDate", page.getContent().get(i).getLastUpdatedDate());
			
			if(page.getContent().get(i).getCoaId() != null)
			{
				lhm.put("coaId", page.getContent().get(i).getCoaId());
				ChartOfAccount coa=chartOfAccountRepository.findOne(page.getContent().get(i).getCoaId());
				lhm.put("coaName",coa.getName());
			}
			
			if(page.getContent().get(i).getRuleGrpId() != null)
			{
				lhm.put("ruleGrpId", page.getContent().get(i).getRuleGrpId());
			//	log.info("template Id :"+page.getContent().get(i).getId());
			//	log.info("page.getContent().get(i).getRuleGrpId() :"+page.getContent().get(i).getRuleGrpId());
				RuleGroup ruleGrpName=ruleGroupRepository.findOne(page.getContent().get(i).getRuleGrpId());
				if(ruleGrpName!=null && ruleGrpName.getName()!=null)
				lhm.put("ruleGroupName",ruleGrpName.getName());
				else
					lhm.put("ruleGroupName","");
			}
			
			if(page.getContent().get(i).getApprRuleGrpId() != null)
			{
				lhm.put("appRuleGrpId", page.getContent().get(i).getRuleGrpId());
				RuleGroup ruleGrpName=ruleGroupRepository.findOne(page.getContent().get(i).getApprRuleGrpId());
				lhm.put("appRuleGroupName",ruleGrpName.getName());
			}
			finalTemplList.add(lhm);
		}
		return new ResponseEntity<>(finalTemplList, headers, HttpStatus.OK);
		
	}


	/**
	 * Auhtor:Shiva
	 *@param FileTemplatesPostingDTO
	 * @return ErrorReport
	 * Description: Posting template details header, template attribute mapping and line criteria
	 */
	@PostMapping("/postingTemplateDetails")
	@Timed
	public TemplateDetails postingFileTempAndFileTempLinesAndSPA(HttpServletRequest request,@RequestBody TemplateDetailsDTO templateDetailsDTO)//, @RequestParam Long tenantId, @RequestParam Long userId)
	{
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		Long userId=Long.parseLong(map.get("userId").toString());
		log.info("Rest api for posting template details, template attribute mapping and line criteria");
		List<TemplAttributeMappingDTO> tempAttMapping = templateDetailsDTO.getJeHeaderDerivations();
		List<LineCriteriaDTO> jeLineCriteria = templateDetailsDTO.getJeLineDerivations();

		//Posting Template Details
		TemplateDetails tempDetails = new TemplateDetails();
		if(templateDetailsDTO.getId() != null)
		{
			tempDetails=templateDetailsRepository.findByTenantIdAndIdForDisplay(tenantId,templateDetailsDTO.getId());
			tempDetails.setId(tempDetails.getId());
		}
		tempDetails.setTenantId(tenantId);
		if(templateDetailsDTO.getTargetAppSource() != null)
			tempDetails.setTargetAppSource(templateDetailsDTO.getTargetAppSource());
		if(templateDetailsDTO.getTemplateName() != null)
			tempDetails.setTemplateName(templateDetailsDTO.getTemplateName());
		if(templateDetailsDTO.getDescription() != null)
			tempDetails.setDescription(templateDetailsDTO.getDescription());
		if(templateDetailsDTO.getViewId() != null)
		{
			DataViews dv=dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, templateDetailsDTO.getViewId());
			tempDetails.setViewId(dv.getId());
		}
		if(templateDetailsDTO.getViewName() != null)
			tempDetails.setViewName(templateDetailsDTO.getViewName());
		if(templateDetailsDTO.getStartDate() != null)
			tempDetails.setStartDate(templateDetailsDTO.getStartDate());
		if(templateDetailsDTO.getEndDate() != null)
			tempDetails.setEndDate(templateDetailsDTO.getEndDate());
		if(templateDetailsDTO.getEnable() != null)
			tempDetails.setEnabledFlag(templateDetailsDTO.getEnable());
		if(templateDetailsDTO.getCoaId() != null)
		{
			ChartOfAccount coa=chartOfAccountRepository.findByIdForDisplayAndTenantId(templateDetailsDTO.getCoaId(), tenantId);
			tempDetails.setCoaId(coa.getId());
		}
		if(templateDetailsDTO.getRuleGrpId() != null)
		{
			RuleGroup rg=ruleGroupRepository.findByIdForDisplayAndTenantId(templateDetailsDTO.getRuleGrpId(), tenantId);
			tempDetails.setRuleGrpId(rg.getId());
			
		}
		tempDetails.setCreatedBy(userId);
		tempDetails.setLastUpdatedBy(userId);
		tempDetails.setCreatedDate(ZonedDateTime.now());
		tempDetails.setLastUpdatedDate(ZonedDateTime.now());
		TemplateDetails td = templateDetailsRepository.save(tempDetails);
		templateDetailsSearchRepository.save(td);
		List<TemplAttributeMapping> tempAttrMapping = new ArrayList<TemplAttributeMapping>();
		if(td != null)
		{
			log.info("Template Details Id: " + td.getId());
			//Posting Template Attributes Mapping
			if(tempAttMapping.size()>0)
			{
				for(TemplAttributeMappingDTO attrMap : tempAttMapping)
				{
					TemplAttributeMapping tmpl = new TemplAttributeMapping();
					if(attrMap.getId() != null)
						tmpl.setId(attrMap.getId());
					tmpl.setJeTempId(td.getId());
					if(attrMap.getMappingType() != null)
						tmpl.setMappingType(attrMap.getMappingType());
					if(attrMap.getValue() != null)
						tmpl.setValue(attrMap.getValue());
					if(attrMap.getSourceViewColumnId() != null)
						tmpl.setSourceViewColumnId(attrMap.getSourceViewColumnId());		
					if(attrMap.getViewId() != null)
						tmpl.setViewId(attrMap.getViewId());
					tmpl.setCretaedBy(userId);
					tmpl.setLastUpdatedBy(userId);
					tmpl.setCreatedDate(ZonedDateTime.now());
					tmpl.setLastUpdatedDate(ZonedDateTime.now());
					if(attrMap.getAttributeName() != null)
						if(!"RATE".equalsIgnoreCase(attrMap.getAttributeName()))
						{
							tmpl.setAttributeName(attrMap.getAttributeName());
							tempAttrMapping.add(tmpl);
						}

				}
				List<TemplAttributeMapping> tempAttrMpng = templAttributeMappingRepository.save(tempAttrMapping);
				log.info("Template Attributes Mapping Size: " + tempAttrMpng.size());
			}
			// Posting Line Criterial
			List<LineCriteria> lineCriteria = new ArrayList<LineCriteria>();
			if(jeLineCriteria.size()>0)
			{
				if(jeLineCriteria.get(0).getId() == null)
				{
					List<BigInteger> jeTempIds = lineCriteriaRepository.fetchIdsByJeTempId(td.getId());
					if(jeTempIds.size()>0)
					{
						for(BigInteger id : jeTempIds)
						{
							lineCriteriaRepository.delete(id.longValue());
						}
					}
				}
				for(LineCriteriaDTO jlc : jeLineCriteria)
				{
					if(jlc.getsViewColumn() != null)
					{
						LineCriteria lc = new LineCriteria();
						if(jlc.getId() != null) //updating record based on id
							lc.setId(jlc.getId());
						lc.setJeTempId(td.getId());
						lc.setSeq(jlc.getSeq());
						if(jlc.getsViewColumn() != null)
							lc.setsViewColumn(jlc.getsViewColumn());
						if(jlc.getOperator() != null)
							lc.setOperator(jlc.getOperator());
						if(jlc.getValue() != null)
							lc.setValue(jlc.getValue().toString());
						lc.setCreatedBy(userId);
						lc.setLastUpdatedBy(userId);
						lc.setCreatedDate(ZonedDateTime.now());
						lc.setLastUpdatedDate(ZonedDateTime.now());
						if(jlc.getsViews() != null)
							lc.setViewId(jlc.getsViews());
						lineCriteria.add(lc);
					}
				}
				List<LineCriteria> lineCriterias = lineCriteriaRepository.save(lineCriteria);
				log.info("Line Criteria Size: "+lineCriteria.size());
			}
		}
		return tempDetails;
	}

	/**Auhtor:Shiva
	 * @param templateId
	 * @param tenantId
	 * @return template details in HashMap
	 */
	@GetMapping("/getTemplateDetails")
	@Timed
	public HashMap getFileTempList(HttpServletRequest request,@RequestParam Long templateId)
	{
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		log.info("Rest api for fetching template details: "+ templateId);
		HashMap finalMap = new HashMap();
		TemplateDetails tmpDetails = templateDetailsRepository.findOne(templateId);
		if(tmpDetails != null)
		{	
			// Template Details
			finalMap.put("id", tmpDetails.getId());
			finalMap.put("description", tmpDetails.getDescription());
			finalMap.put("targetAppSource", tmpDetails.getTargetAppSource());
			finalMap.put("templateName", tmpDetails.getTemplateName());
			String[] viewIdString = tmpDetails.getViewId().toString().split("\\,");	// View Ids
			List<Long> viewIds = new ArrayList<Long>();
			if(viewIdString.length>0)
			{
				for(String viewId : viewIdString)
				{
					viewIds.add(Long.parseLong(viewId));
				}
			}
			String[] viewNamesString = tmpDetails.getViewName().toString().split("\\,");
			List<String> viewNames = new ArrayList<String>();
			if(viewNamesString.length>0)
			{
				for(String viewName : viewNamesString)
				{
					viewNames.add(viewName);
				}
			}
			List<HashMap> selectedView = new ArrayList<HashMap>();
			if(viewIds.size()>0)
			{
				for(Long viewId : viewIds)
				{
					HashMap hm = new HashMap();
					DataViews dv = dataViewsRepository.findById(viewId);
					if(dv != null)
					{
						hm.put("id", viewId);
						hm.put("itemName", dv.getDataViewDispName());
					}
					selectedView.add(hm);
				}
			}
			if(tmpDetails.getCoaId()!=null)
			{
				finalMap.put("coaId", tmpDetails.getCoaId());
				ChartOfAccount coa=chartOfAccountRepository.findOne(tmpDetails.getCoaId());
				finalMap.put("coaName", coa.getName());
			}
			else
			{
				finalMap.put("coaId", null);
				finalMap.put("coaName","");
			}
			if(tmpDetails.getRuleGrpId()!=null)
			{
				finalMap.put("ruleGrpId", tmpDetails.getRuleGrpId());
				RuleGroup ruleGrpName=ruleGroupRepository.findOne(tmpDetails.getRuleGrpId());
				finalMap.put("ruleGrpName",ruleGrpName.getName());
			}
			else
			{
				finalMap.put("ruleGrpId",null);
				finalMap.put("ruleGrpName","");
			}
			finalMap.put("ruleGrpId", tmpDetails.getRuleGrpId());
			finalMap.put("viewId", viewIds);
			finalMap.put("viewName", viewNames);
			finalMap.put("startDate", tmpDetails.getStartDate());
			finalMap.put("endDate", tmpDetails.getEndDate());
			finalMap.put("enable", tmpDetails.isEnabledFlag());
			finalMap.put("selectedView", selectedView);
			finalMap.put("selectedView9", selectedView);
			List<HashMap> jeHeaderDerivations = new ArrayList<HashMap>();
			// Template Attributes Mapping
			List<TemplAttributeMapping> tam = templAttributeMappingRepository.findByJeTempId(templateId);
			if(tam.size()>0)
			{
				List<HashMap> tmplHMs = new ArrayList<HashMap>();
				for(TemplAttributeMapping tmpMp : tam)
				{
					HashMap hm = new HashMap();
					LookUpCode luc = lookUpCodeRepository.findByTenantIdAndLookUpCodeAndLookUpType(tenantId, tmpMp.getAttributeName(), "JE_DERIVATIONS");
					if(luc != null)
					{
						hm.put("meaning", luc.getMeaning());
					}
					hm.put("id", tmpMp.getId());
					hm.put("attributeName", tmpMp.getAttributeName());
					hm.put("mappingType", tmpMp.getMappingType());
					hm.put("value", tmpMp.getValue());
					hm.put("showRow", true);
					if(tmpMp.getSourceViewColumnId() != null)
					{
						hm.put("sourceViewColumnId", tmpMp.getSourceViewColumnId());
					}
					hm.put("viewId", tmpMp.getViewId());
					if(tmpMp.getMappingType().trim().length()>0)
					{
						tmplHMs.add(hm);
					}
				}
				finalMap.put("jeHeaderDerivations", tmplHMs);
			}
			//Line Criteria
			List<LineCriteria> lcs = lineCriteriaRepository.findByJeTempId(templateId);
			if(lcs.size()>0)
			{
				List<HashMap> lineCriteria = new ArrayList<HashMap>();
				for(LineCriteria lc : lcs)
				{
					HashMap hm = new HashMap();
					hm.put("id", lc.getId());
					hm.put("seq", lc.getSeq());
					hm.put("value", lc.getValue());
					// Getting View Name based on view id
					DataViewsColumns dvc = dataViewsColumnsRepository.findOne(lc.getsViewColumn());
					if(dvc != null)
					{
						hm.put("sViewColumn", lc.getsViewColumn());
						DataViews dv = dataViewsRepository.findOne(dvc.getDataViewId());
						if(dv != null)
						{
							hm.put("viewName", dv.getDataViewName());		
						}
					}
					hm.put("operator", lc.getOperator());
					hm.put("sViews", lc.getViewId());
					lineCriteria.add(hm);
				}
				finalMap.put("jeLineDerivations", lineCriteria);
			}
		}
		return finalMap;
	}



	/**
	 * author:ravali
	 * @param tenantId
	 * @param name
	 * Desc: checking whether template name already exists
	 * @return hashMap stating the status
	 */
	@GetMapping("/templateNameIsExists")
	@Timed
	//	public HashMap templateName(@RequestParam Long tenantId,@RequestParam String name)
	public HashMap templateName(HttpServletRequest request,@RequestParam String name)
	{
		HashMap user=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(user.get("tenantId").toString());

		HashMap map=new HashMap();
		map.put("result", "No Duplicates Found");
		List<TemplateDetails> temName=templateDetailsRepository.findByTenantIdAndTemplateName(tenantId,name);
		if(temName.size()>0)
		{
			map.put("result", "'"+name+"' templateName already exists");
		}
		return map;
	}

	/**
	 * author: Shiva
	 * @param tenantId
	 * Desc: Fetching batch details based on tenant id.
	 * @return List<LinkedHashMap> with batch details
	 */
	@GetMapping("/getBatchDetailsByTenantId")
	@Timed
	public List<LinkedHashMap> getBatchDetailsByTenantId(HttpServletRequest request)
	{
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		log.info("Rest api for fetching list of batch names for the tenant id: "+ tenantId);
		List<LinkedHashMap> finalMap = new ArrayList<LinkedHashMap>();
		List<JournalsHeaderData> journals = journalsHeaderDataRepository.fetchByTenantId(tenantId);
		if(journals.size()>0)
		{
			for(JournalsHeaderData jrnl : journals)
			{
				LinkedHashMap hm = new LinkedHashMap();
				hm.put("id", jrnl.getId());
				TemplateDetails temp = templateDetailsRepository.findOne(jrnl.getJeTempId());
				if(temp != null)
				{
					hm.put("templateName", temp.getTemplateName());
				}
				hm.put("jeBatchName", jrnl.getJeBatchName());
				hm.put("period", jrnl.getPeriod());
				hm.put("jeTempId", jrnl.getJeTempId());
				finalMap.add(hm);
			}
		}
		log.info("No of records fetched: "+ finalMap.size());
		return finalMap;
	}

	/**
	 * author: Shiva
	 * @param journalHeaderId
	 * Desc: Fetching batch details based on tenant id.
	 * @return List<LinkedHashMap> with batch details
	 */
	@GetMapping("/getJournalsResult")
	@Timed
	public JournalsResultDTO getJournalsResult(@RequestParam Long journalHeaderId)
	{
		log.info("Rest api for fetching journals result for the journal header id: "+journalHeaderId);
		JournalsResultDTO finalMap = new JournalsResultDTO();
		JournalsHeaderData jrnlHeader = journalsHeaderDataRepository.findOne(journalHeaderId);
		if(jrnlHeader != null)
		{
			finalMap.setId(jrnlHeader.getId()); // Template Info Start
			TemplateDetails temp = templateDetailsRepository.findOne(jrnlHeader.getJeTempId());
			if(temp != null)
				finalMap.setTemplateName(temp.getTemplateName());
			finalMap.setJeBatchName(jrnlHeader.getJeBatchName());
			finalMap.setPeriod(jrnlHeader.getPeriod());	// Template Info End
			// Je Header Info Start
			JournalHeaderDTO journalHeader =  new JournalHeaderDTO();
			journalHeader.setTenantId(jrnlHeader.getTenantId());
			journalHeader.setJeTempId(jrnlHeader.getJeTempId());
			if(temp != null)
				journalHeader.setTemplateName(temp.getTemplateName());
			journalHeader.setJeBatchName(jrnlHeader.getJeBatchName());
			journalHeader.setJeBatchDate(jrnlHeader.getJeBatchDate());
			journalHeader.setGlDate(jrnlHeader.getGlDate());
			journalHeader.setCurrency(jrnlHeader.getCurrency());
			journalHeader.setConversionType(jrnlHeader.getConversionType());
			journalHeader.setCategory(jrnlHeader.getCategory());
			journalHeader.setSource(jrnlHeader.getSource());
			journalHeader.setLedgerName(jrnlHeader.getLedgerName());
			journalHeader.setBatchTotal(jrnlHeader.getBatchTotal());
			journalHeader.setRunDate(jrnlHeader.getRunDate());
			journalHeader.setSubmittedBy(jrnlHeader.getSubmittedBy());
			journalHeader.setPeriod(jrnlHeader.getPeriod());
			List<JeLinesDTO> jeLinesDTO = new ArrayList<JeLinesDTO>();
			log.info("Je Temp Id: "+ jrnlHeader.getJeTempId());
			TemplAttributeMapping tmplAttr = templAttributeMappingRepository.findByJeTempIdAndAttributeName(jrnlHeader.getJeTempId(), "JOURNAL_GENERATION_LEVEL");
			log.info("tmplAttr.getValue(): "+ tmplAttr.getValue());
			if("SUMMARY".equalsIgnoreCase(tmplAttr.getValue()))
			{
				log.info("In Summary");
				List<String> codeCombinations = jeLinesRepository.fetchDistinctCodeCombinations(jrnlHeader.getId());
				log.info("Distinct code combination size: "+ codeCombinations);
				if(codeCombinations.size()>0)
				{
					for(String codeComb : codeCombinations)
					{
						JeLinesDTO je = new JeLinesDTO();
						BigDecimal acctdDebit = jeLinesRepository.fetchAccountedDebitByCodeCombinationAndJeHeaderId(jrnlHeader.getId(), codeComb);
						BigDecimal acctdCredit = jeLinesRepository.fetchAccountedCreditByCodeCombinationAndJeHeaderId(jrnlHeader.getId(), codeComb);
						je.setAccountedCredit(acctdCredit);
						je.setAccountedDebit(acctdDebit);
						je.setCodeCombination(codeComb);
						jeLinesDTO.add(je);
					}
				}
			}
			else if("DETAIL".equalsIgnoreCase(tmplAttr.getValue()))
			{
				log.info("In Details");
				List<JeLines> jeLines = jeLinesRepository.findByJeHeaderId(jrnlHeader.getId());
				if(jeLines.size()>0)
				{
					for(JeLines jeLine : jeLines)
					{
						JeLinesDTO je = new JeLinesDTO();
						je.setDescriptionAttribute(jeLine.getDescriptionAttribute());
						/*						je.setId(jeLine.getId());
						je.setJeBatchId(jeLine.getJeBatchId());
						je.setRowId(jeLine.getRowId());
						je.setLineNum(jeLine.getLineNum());
						je.setDescriptionAttribute(jeLine.getDescriptionAttribute());
						 */						je.setCodeCombination(jeLine.getCodeCombination());
						 /*je.setCurrency(jeLine.getCurrency());
						je.setDebitAmount(jeLine.getDebitAmount());
						je.setCreditAmount(jeLine.getCreditAmount());*/
						 je.setAccountedDebit(jeLine.getAccountedDebit());
						 je.setAccountedCredit(jeLine.getAccountedCredit());
						 je.setDebitAmount(jeLine.getDebitAmount());
						 je.setCreditAmount(jeLine.getCreditAmount());
						 /*			je.setComments(jeLine.getComments());*/
						 jeLinesDTO.add(je);
					}
				}
			}
			journalHeader.setJeLinesDTO(jeLinesDTO);
			finalMap.setJournalHeader(journalHeader);
		}

		return finalMap;
	}


	/**
	 * author :ravali	
	 * @param templateDetailsDTO
	 * @param tenantId
	 * @param userId
	 * @return
	 */
	@PostMapping("/postingTemplateDetailsAndDerivation")
	@Timed
	//	public TemplateDetails tempDetailsAndDerviations(@RequestBody TemplateDetailsDTO templateDetailsDTO, @RequestParam Long tenantId, @RequestParam Long userId)

	public TemplateDetails tempDetailsAndDerviations(@RequestBody TemplateDetailsDTO templateDetailsDTO, HttpServletRequest request)

	{
		HashMap User=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(User.get("tenantId").toString());
		Long userId=Long.parseLong(User.get("userId").toString());
		log.info("Rest api for posting template details, template attribute mapping and je LDR Details");
		List<TemplAttributeMappingDTO> tempAttMapping = templateDetailsDTO.getJeHeaderDerivations();
		List<LineCriteriaDTO> jeLdrDetailsListDTO = templateDetailsDTO.getJeLineDerivations();

		//Posting Template Details
		TemplateDetails tempDetails = new TemplateDetails();
		if(templateDetailsDTO.getId() != null)
		{
			tempDetails=templateDetailsRepository.findByTenantIdAndIdForDisplay(tenantId,templateDetailsDTO.getId());
			tempDetails.setId(tempDetails.getId());
		}
		tempDetails.setTenantId(tenantId);
		if(templateDetailsDTO.getTargetAppSource() != null)
			tempDetails.setTargetAppSource(templateDetailsDTO.getTargetAppSource());
		if(templateDetailsDTO.getTemplateName() != null)
			tempDetails.setTemplateName(templateDetailsDTO.getTemplateName());
		if(templateDetailsDTO.getDescription() != null)
			tempDetails.setDescription(templateDetailsDTO.getDescription());
		if(templateDetailsDTO.getViewId() != null)
		{
			DataViews dv=dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, templateDetailsDTO.getViewId());
			tempDetails.setViewId(dv.getId());
		if(dv!=null)
		templateDetailsDTO.setViewName(dv.getDataViewName());
		}
		tempDetails.setViewName(templateDetailsDTO.getViewName());
		if(templateDetailsDTO.getStartDate() != null)
			tempDetails.setStartDate(templateDetailsDTO.getStartDate());
		if(templateDetailsDTO.getEndDate() != null)
			tempDetails.setEndDate(templateDetailsDTO.getEndDate());

		if(templateDetailsDTO.getEnable() != null)
			tempDetails.setEnabledFlag(templateDetailsDTO.getEnable());
		else
			tempDetails.setEnabledFlag(true);
		if(templateDetailsDTO.getId()==null || templateDetailsDTO.getId().isEmpty())
		{
			tempDetails.setCreatedDate(ZonedDateTime.now());
			tempDetails.setCreatedBy(userId);
		}
		tempDetails.setLastUpdatedBy(userId);
		
		if(templateDetailsDTO.getId() == null)
		tempDetails.setCreatedDate(ZonedDateTime.now());
		else
			tempDetails.setCreatedDate(ZonedDateTime.now());
		tempDetails.setLastUpdatedDate(ZonedDateTime.now());
		if(templateDetailsDTO.getCoaId() != null)
		{
			ChartOfAccount coa=chartOfAccountRepository.findByIdForDisplayAndTenantId(templateDetailsDTO.getCoaId(), tenantId);
			tempDetails.setCoaId(coa.getId());
		}
		if(templateDetailsDTO.getRuleGrpId() != null)
		{
			
			RuleGroup rg=ruleGroupRepository.findByIdForDisplayAndTenantId(templateDetailsDTO.getRuleGrpId(), tenantId);
			tempDetails.setRuleGrpId(rg.getId());
		}
		
		if(templateDetailsDTO.getApprRuleGrpId() != null)
		{
			
			RuleGroup rg=ruleGroupRepository.findByIdForDisplayAndTenantId(templateDetailsDTO.getApprRuleGrpId(), tenantId);
			tempDetails.setApprRuleGrpId(rg.getId());
		}
		
		TemplateDetails td = templateDetailsRepository.save(tempDetails);
		if(templateDetailsDTO.getId()==null)
		{
		String idForDisplay = IDORUtils.computeFrontEndIdentifier(td.getId().toString()); 
		 td.setIdForDisplay(idForDisplay); 
		 td=templateDetailsRepository.save(td);
		}
		List<TemplAttributeMapping> tempAttrMapping = new ArrayList<TemplAttributeMapping>();
		
		List<TemplAttributeMapping> tempAttrMappingExisting = templAttributeMappingRepository.findByJeTempId(td.getId());
		
		if(tempAttrMappingExisting.size()>0)
		{
			templAttributeMappingRepository.delete(tempAttrMappingExisting);
		}
		if(td != null)
		{
			log.info("Template Details Id: " + td.getId());
			//Posting Template Attributes Mapping
			if(tempAttMapping.size()>0)
			{
				for(TemplAttributeMappingDTO attrMap : tempAttMapping)
				{
					TemplAttributeMapping tmpl = new TemplAttributeMapping();
					if(attrMap.getId() != null)
						tmpl.setId(attrMap.getId());
					tmpl.setJeTempId(td.getId());
					if(attrMap.getMappingType() != null)
						tmpl.setMappingType(attrMap.getMappingType());
					if(attrMap.getValue() != null)
						tmpl.setValue(attrMap.getValue());
					if(attrMap.getSourceViewColumnId() != null)
						tmpl.setSourceViewColumnId(attrMap.getSourceViewColumnId());		
					if(attrMap.getViewId() != null)
						tmpl.setViewId(attrMap.getViewId());
					if(attrMap.getId()==null || attrMap.getId()==0)
						tmpl.setCretaedBy(userId);
					tmpl.setLastUpdatedBy(userId);
					tmpl.setCreatedDate(ZonedDateTime.now());
					tmpl.setLastUpdatedDate(ZonedDateTime.now());
					if(attrMap.getAttributeName() != null)
					{
						tmpl.setAttributeName(attrMap.getAttributeName());
						if(attrMap.getAttributeName().equalsIgnoreCase("LEDGER") && attrMap.getMappingType().equalsIgnoreCase("LEDGER_DEFINITION"))
						{
							LedgerDefinition ledger=ledgerDefinitionRepository.findByIdForDisplayAndTenantId(attrMap.getValue(), tenantId);
							tmpl.setValue(ledger.getId().toString());
							
						}
						else if(attrMap.getAttributeName().equalsIgnoreCase("PERIOD") && attrMap.getMappingType().equalsIgnoreCase("CALENDAR") )
						{
							Calendar cal=calendarRepository.findByIdForDisplayAndTenantId(attrMap.getValue(), tenantId);
							tmpl.setValue(cal.getId().toString());
						}
						
						else if(attrMap.getAttributeName().equalsIgnoreCase("JOURNAL_BATCH_NAME"))
						{
							tmpl.setValue(templateDetailsDTO.getJeBatchName().toString().replaceAll("\\[", "").replaceAll("\\]", ""));
						}
						else if(attrMap.getAttributeName().equalsIgnoreCase("BALANCE_TYPE"))
							tmpl.setValue(templateDetailsDTO.getBalanceType().toString().replaceAll("\\[", "").replaceAll("\\]", ""));
						else if(attrMap.getValue() != null)
							tmpl.setValue(attrMap.getValue());

					}

					if(attrMap.getAliasName()!=null)
						tmpl.setAliasName(attrMap.getAliasName());
					if(attrMap.getRuleLevel()!=null)
						tmpl.setRuleLevel(attrMap.getRuleLevel());
					tempAttrMapping.add(tmpl);

				}
				List<TemplAttributeMapping> tempAttrMpng = templAttributeMappingRepository.save(tempAttrMapping);
				log.info("Template Attributes Mapping Size: " + tempAttrMpng.size());
			}
			// Posting je_LDR_Details 

			List<JeLdrDetails> jeLdrDetails = new ArrayList<JeLdrDetails>();
			
			
			List<JeLdrDetails> jeLdrDetailsExistingList = jeLdrDetailsRepository.findByJeTempId(td.getId());
			if(jeLdrDetailsExistingList.size()>0)
			{
				jeLdrDetailsRepository.delete(jeLdrDetailsExistingList);
			}
			
			if(jeLdrDetailsListDTO.size()>0)
			{

				for(LineCriteriaDTO jldDto : jeLdrDetailsListDTO)
				{
					JeLdrDetails jld=new JeLdrDetails();
					jld.setJeTempId(td.getId());
					jld.setColType(jldDto.getColType());
					jld.setColValue(jldDto.getColValue());
					jld.setCreatedBy(userId);
					jld.setCreatedDate(ZonedDateTime.now());
					jld.setLastUpdatedBy(userId);
					jld.setLastUpdatedDate(ZonedDateTime.now());
					if(jldDto.getRefColumn()!=null)
						jld.setRefColumn(jldDto.getRefColumn());
					jeLdrDetails.add(jld);

				}
				jeLdrDetailsRepository.save(jeLdrDetails);
				log.info("jeLDRDetails Size: "+jeLdrDetails.size());
			}
		}
		return tempDetails;
	}


	/**
	 * author :ravali
	 * @param id
	 * @return
	 */
	@GetMapping("/getTemplateDetailsAndDerivation")
	@Timed
	public TemplateDetailsDTO getTempDetailsAndDerviations(@RequestParam String id,HttpServletRequest request)
	{
		log.info("Rest Request to Get TemplateDetails And Derivation");
		HashMap User=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(User.get("tenantId").toString());
    	Long userId=Long.parseLong(User.get("userId").toString());
		TemplateDetailsDTO tempDto=new TemplateDetailsDTO();
		TemplateDetails temp=templateDetailsRepository.findByTenantIdAndIdForDisplay(tenantId, id);
		TemplateDetails tempDetails=templateDetailsRepository.findOne(temp.getId());
		tempDto.setId(tempDetails.getIdForDisplay());
		if(tempDetails.getTemplateName()!=null)
		tempDto.setTemplateName(tempDetails.getTemplateName());
		if(tempDetails.getViewId()!=null)
		{
		DataViews dv=dataViewsRepository.findOne(tempDetails.getViewId());
		tempDto.setViewId(dv.getIdForDisplay());
		}
		if(tempDetails.getViewName()!=null)
		tempDto.setViewName(tempDetails.getViewName());
		if(tempDetails.getDescription()!=null)
		tempDto.setDescription(tempDetails.getDescription());
		if(tempDetails.isEnabledFlag()!=null)
		tempDto.setEnable(tempDetails.isEnabledFlag());
		if(tempDetails.getStartDate()!=null)
		tempDto.setStartDate(tempDetails.getStartDate());
		if(tempDetails.getEndDate()!=null)
		tempDto.setEndDate(tempDetails.getEndDate());
		
		if(tempDetails.getCoaId() != null)
		{
			ChartOfAccount coa=chartOfAccountRepository.findOne(tempDetails.getCoaId());
			tempDto.setCoaId(coa.getIdForDisplay());
			tempDto.setCoaName(coa.getName());
		}
		if(tempDetails.getRuleGrpId() != null)
		{
			
			RuleGroup ruleGrpName=ruleGroupRepository.findOne(tempDetails.getRuleGrpId());
			tempDto.setRuleGrpId(ruleGrpName.getIdForDisplay());
			tempDto.setRuleGrpName(ruleGrpName.getName());
		}
		
		if(tempDetails.getApprRuleGrpId() != null)
		{
			
			RuleGroup ruleGrpName=ruleGroupRepository.findOne(tempDetails.getApprRuleGrpId());
			tempDto.setApprRuleGrpId(ruleGrpName.getIdForDisplay());
			tempDto.setAppRuleGrpName(ruleGrpName.getName());
			
		}
		if(tempDetails.getCreatedDate()!=null)
		tempDto.setCreatedDate(tempDetails.getCreatedDate());
		
	List<TemplAttributeMappingDTO> jeHeaderDerivationsList=new ArrayList<TemplAttributeMappingDTO>();//template attribute mapping
		List<LineCriteriaDTO> jeLineDerivationsList=new ArrayList<LineCriteriaDTO>();
		
		List<TemplAttributeMapping> tempAttrMapingList=templAttributeMappingRepository.findByJeTempId(temp.getId());
		for(TemplAttributeMapping tempAttrMap:tempAttrMapingList)
		{
			TemplAttributeMappingDTO jeHedDerDto=new TemplAttributeMappingDTO();
			jeHedDerDto.setId(tempAttrMap.getId());
			if(tempAttrMap.getAttributeName()!=null)
			jeHedDerDto.setAttributeName(tempAttrMap.getAttributeName());
			if(tempAttrMap.getValue()!=null)
			jeHedDerDto.setValue(tempAttrMap.getValue());
			if(tempAttrMap.getAttributeName().equalsIgnoreCase("ROUNDING_SETOFF_ACCOUNT") || tempAttrMap.getAttributeName().equalsIgnoreCase("MULTI_COMPANY_ACCOUNTS"))
			{
				Segments segments=segmentsRepository.findByCoaIdAndQualifier(tempDetails.getCoaId(),"NATURAL_ACCOUNT");
				List<MappingSetValues> mappingSetValues =  mappingSetValuesRepository.findByMappingSetId(segments.getValueId());
				if(mappingSetValues.size()>0)
				{
					for(MappingSetValues mappingsetValue : mappingSetValues)
					{
						
						if(mappingsetValue.getSourceValue() != null && mappingsetValue.getSourceValue().equalsIgnoreCase(tempAttrMap.getValue()))
						{
						String valueName = mappingsetValue.getSourceValue() ;
						if(mappingsetValue.getTargetValue()  != null)
							valueName = valueName+" - " + mappingsetValue.getTargetValue() ;
						jeHedDerDto.setValueName(valueName);
						}
					}
					
				}
				
			}
			
			if(tempAttrMap.getMappingType()!=null)
			{
				
				if(tempAttrMap.getMappingType().equalsIgnoreCase("LOOKUP_CODE"))
				{
					
					if(tempAttrMap.getValue().equalsIgnoreCase("SUMMARY")  || tempAttrMap.getValue().equalsIgnoreCase("DETAIL"))
					{
						LookUpCode	lookUpCode=lookUpCodeRepository.findByTenantIdAndLookUpCodeAndLookUpType(tempDetails.getTenantId(), tempAttrMap.getValue(),"JOURNAL_GENERATION_LEVEL");
						jeHedDerDto.setValueName(lookUpCode.getMeaning());
					}
					else
					{
						log.info("tempAttrMap.getValue(): " + tempAttrMap.getValue());
						LookUpCode	lookUpCode=new LookUpCode();
						if(tempAttrMap.getAttributeName().equalsIgnoreCase("Date"))
							lookUpCode=lookUpCodeRepository.findByTenantIdAndLookUpCodeAndLookUpType(tempDetails.getTenantId(), tempAttrMap.getValue(),"GL_DATES_DERIVATION");
						else
						lookUpCode=lookUpCodeRepository.findByTenantIdAndLookUpCodeAndLookUpType(tempDetails.getTenantId(), tempAttrMap.getValue(),tempAttrMap.getAttributeName());
						if(lookUpCode!=null)
						jeHedDerDto.setValueName(lookUpCode.getMeaning());
						else
							lookUpCode=lookUpCodeRepository.findByTenantIdAndLookUpCode(tempDetails.getTenantId(), tempAttrMap.getValue());
						jeHedDerDto.setValueName(lookUpCode.getMeaning());

					}
					
					
					
				}
				else if(tempAttrMap.getMappingType().equalsIgnoreCase("LEDGER_DEFINITION"))
				{
					LedgerDefinition ledger=ledgerDefinitionRepository.findOne(Long.valueOf(tempAttrMap.getValue()));
					jeHedDerDto.setValueName(ledger.getName());
					jeHedDerDto.setValue(ledger.getIdForDisplay());
				}
				else if(tempAttrMap.getMappingType().equalsIgnoreCase("CALENDAR"))
				{
					Calendar cal=calendarRepository.findOne(Long.valueOf(tempAttrMap.getValue()));
					jeHedDerDto.setValueName(cal.getName());
					jeHedDerDto.setValue(cal.getIdForDisplay());
				}
//				******  For GL Date custom function @Rk  *********
				else if(tempAttrMap.getMappingType().equalsIgnoreCase("CUSTOM_FUNCTION")) {
					jeHedDerDto.setValueName(tempAttrMap.getValue());
				}
				jeHedDerDto.setMappingType(tempAttrMap.getMappingType());
//				********** END **************
			}
			if(tempAttrMap.getSourceViewColumnId()!=null)
			{
			jeHedDerDto.setSourceViewColumnId(tempAttrMap.getSourceViewColumnId());
			DataViewsColumns dvc=dataViewsColumnsRepository.findOne(tempAttrMap.getSourceViewColumnId());
			jeHedDerDto.setSourceViewColumnName(dvc.getColumnName());
			}
			if(tempAttrMap.getViewId()!=null)
			jeHedDerDto.setViewId(tempAttrMap.getViewId());
			if(tempAttrMap.getAliasName()!=null)
			jeHedDerDto.setAliasName(tempAttrMap.getAliasName());
			if(tempAttrMap.getRuleLevel()!=null)
			jeHedDerDto.setRuleLevel(tempAttrMap.getRuleLevel());
			jeHeaderDerivationsList.add(jeHedDerDto);
			
		}
		
		List<JeLdrDetails> jeLdrDetailsList=jeLdrDetailsRepository.findByJeTempId(temp.getId());
		for(JeLdrDetails JeLdrDet:jeLdrDetailsList)
		{
			log.info("JeLdrDet :"+ JeLdrDet);
			LineCriteriaDTO JeLdrDetDto=new LineCriteriaDTO();
			JeLdrDetDto.setId(JeLdrDet.getId());
			if(JeLdrDet.getColType()!=null)
			JeLdrDetDto.setColType(JeLdrDet.getColType());
			if(JeLdrDet.getColValue()!=null)
			JeLdrDetDto.setColValue(JeLdrDet.getColValue());
			if(JeLdrDet.getColType().equalsIgnoreCase("system"))
			{
				log.info("JeLdrDet.getColValue() :"+JeLdrDet.getColValue());
				
				LookUpCode lookUpCode=lookUpCodeRepository.findOne(JeLdrDet.getColValue());
				JeLdrDetDto.setColValueName(lookUpCode.getMeaning());
			}
			else if(JeLdrDet.getColType().equalsIgnoreCase("Line"))
			{
				DataViewsColumns dvc=dataViewsColumnsRepository.findOne(JeLdrDet.getColValue());
				JeLdrDetDto.setColValueName(dvc.getColumnName());
			}
			jeLineDerivationsList.add(JeLdrDetDto);
		}
		
		tempDto.setJeHeaderDerivations(jeHeaderDerivationsList);
		tempDto.setJeLineDerivations(jeLineDerivationsList);
		return tempDto;
		
	}


	/**
	 * Author Kiran
	 * @param request
	 * @param dataViewId
	 * @return
	 */
	@GetMapping("/getTemplateIdAndNameByDataViewId")
	@Timed
	public List<TemplateDetails> getTemplateIdAndName(HttpServletRequest request,@RequestParam String dataViewId)
	{
		log.info("Rest request to get the template Id(s) and name(s) of dataviewId "+dataViewId);
		HashMap User=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(User.get("tenantId").toString());
		List<TemplateDetails> listOftemplateDetails=new ArrayList<TemplateDetails>();

		DataViews dataView = dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId,dataViewId);
		Long dataViewIdLong = 0L;
		if(dataView!=null)
		{
			dataViewIdLong = dataView.getId();
		}
		List<TemplateDetails> templateDetails=templateDetailsRepository.findByTenantIdAndViewId(tenantId,dataViewIdLong);
		if(templateDetails!=null)
		{
			for(int k=0;k<templateDetails.size();k++)
			{
				TemplateDetails tempDetails = new TemplateDetails();
				//tempDetails.setId(templateDetails.get(k).getId());
				tempDetails.setIdForDisplay(templateDetails.get(k).getIdForDisplay());
				tempDetails.setTenantId(tenantId);
				if(templateDetails.get(k).getTemplateName()!=null)
					tempDetails.setTemplateName(templateDetails.get(k).getTemplateName());
				if(templateDetails.get(k).getTargetAppSource()!=null)
					tempDetails.setTargetAppSource(templateDetails.get(k).getTargetAppSource());
				if(templateDetails.get(k)!=null)
					tempDetails.setDescription(templateDetails.get(k).getDescription());
				if(templateDetails.get(k).getViewName()!=null)
					tempDetails.setViewName(templateDetails.get(k).getViewName());
				if(templateDetails.get(k).getEnabledFlag()!=null)
					tempDetails.setEnabledFlag(templateDetails.get(k).getEnabledFlag());
				if(templateDetails.get(k).getCoaId()!=null)
					tempDetails.setCoaId(templateDetails.get(k).getCoaId());
				if(templateDetails.get(k).getRuleGrpId()!=null)
					tempDetails.setRuleGrpId(templateDetails.get(k).getRuleGrpId());

				listOftemplateDetails.add(tempDetails);
			}
		}
		else{
			log.info("Records not found for given DataViewId");
		}
		return listOftemplateDetails;
	}
}
