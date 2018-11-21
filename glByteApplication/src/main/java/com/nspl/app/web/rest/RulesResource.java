package com.nspl.app.web.rest;



import com.codahale.metrics.annotation.Timed;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;

import com.nspl.app.domain.AccountingLineTypes;
import com.nspl.app.domain.AcctRuleConditions;
import com.nspl.app.domain.AcctRuleDerivations;
import com.nspl.app.domain.AppRuleConditions;
import com.nspl.app.domain.ApprovalGroups;
import com.nspl.app.domain.ApprovalRuleAssignment;
import com.nspl.app.domain.ChartOfAccount;
import com.nspl.app.domain.DataViews;
import com.nspl.app.domain.DataViewsColumns;
import com.nspl.app.domain.LedgerDefinition;
import com.nspl.app.domain.LookUpCode;
import com.nspl.app.domain.MappingSet;
import com.nspl.app.domain.RuleConditions;
import com.nspl.app.domain.RuleGroup;
import com.nspl.app.domain.RuleGroupDetails;
import com.nspl.app.domain.Rules;
import com.nspl.app.repository.AccountingLineTypesRepository;
import com.nspl.app.repository.AcctRuleConditionsRepository;
import com.nspl.app.repository.AcctRuleDerivationsRepository;
import com.nspl.app.repository.AppRuleConditionsRepository;
import com.nspl.app.repository.ApprovalGroupsRepository;
import com.nspl.app.repository.ApprovalRuleAssignmentRepository;
import com.nspl.app.repository.ChartOfAccountRepository;
import com.nspl.app.repository.DataViewsColumnsRepository;
import com.nspl.app.repository.DataViewsRepository;
import com.nspl.app.repository.LedgerDefinitionRepository;
import com.nspl.app.repository.LookUpCodeRepository;
import com.nspl.app.repository.MappingSetRepository;
import com.nspl.app.repository.RuleConditionsRepository;
import com.nspl.app.repository.RuleGroupDetailsRepository;
import com.nspl.app.repository.RuleGroupRepository;
import com.nspl.app.repository.RulesRepository;
import com.nspl.app.repository.search.RulesSearchRepository;
import com.nspl.app.service.UserJdbcService;
import com.nspl.app.web.rest.dto.AcctRuleCondDTO;
import com.nspl.app.web.rest.dto.AcctRuleDerivationDTO;
import com.nspl.app.web.rest.dto.AppRuleCondAndActDto;
import com.nspl.app.web.rest.dto.ApprRuleAssgnDto;
import com.nspl.app.web.rest.dto.ApprovalActionDto;
import com.nspl.app.web.rest.dto.LineItems;
import com.nspl.app.web.rest.dto.RuleConditionsDTO;
import com.nspl.app.web.rest.dto.RuleDTO;
import com.nspl.app.web.rest.dto.RulesAndLineItems;
import com.nspl.app.web.rest.dto.RulesDTO;
import com.nspl.app.web.rest.util.HeaderUtil;

import io.github.jhipster.web.util.ResponseUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Rules.
 */
@RestController
@RequestMapping("/api")
public class RulesResource {

	private final Logger log = LoggerFactory.getLogger(RulesResource.class);

	private static final String ENTITY_NAME = "rules";

	private final RulesRepository rulesRepository;

	//private final RulesSearchRepository rulesSearchRepository;

    @Inject 
    LedgerDefinitionRepository ledgerDefinitionRepository;
    
	@Inject
    ChartOfAccountRepository chartOfAccountRepository;
	
	@Inject
	RuleConditionsRepository ruleConditionsRepository;

	@Inject
	DataViewsColumnsRepository dataViewsColumnsRepository;

	@Inject
	DataViewsRepository dataViewsRepository;


	@Inject
	RuleGroupRepository ruleGroupRepository;

	@Inject
	RuleGroupDetailsRepository ruleGroupDetailsRepository;


	@Inject
	AccountingLineTypesRepository accountingLineTypesRepository;

	@Inject
	AcctRuleConditionsRepository acctRuleConditionsRepository;

	@Inject
	AcctRuleDerivationsRepository acctRuleDerivationsRepository;

	@Inject
	LookUpCodeRepository lookUpCodeRepository;

	@Inject
	MappingSetRepository mappingSetRepository;

	@Inject
	AppRuleConditionsRepository appRuleConditionsRepository;

	@Inject
	ApprovalRuleAssignmentRepository approvalRuleAssignmentRepository;

	@Inject
	ApprovalGroupsRepository approvalGroupsRepository;


	@Inject
	UserJdbcService userJdbcService;



	public RulesResource(RulesRepository rulesRepository, RulesSearchRepository rulesSearchRepository) {
		this.rulesRepository = rulesRepository;
		//this.rulesSearchRepository = rulesSearchRepository;
	}

	/**
	 * POST  /rules : Create a new rules.
	 *
	 * @param rules the rules to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new rules, or with status 400 (Bad Request) if the rules has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/rules")
	@Timed
	public ResponseEntity<Rules> createRules(@RequestBody Rules rules) throws URISyntaxException {
		log.debug("REST request to save Rules : {}", rules);
		if (rules.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new rules cannot already have an ID")).body(null);
		}
		Rules result = rulesRepository.save(rules);
		//rulesSearchRepository.save(result);
		return ResponseEntity.created(new URI("/api/rules/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /rules : Updates an existing rules.
	 *
	 * @param rules the rules to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated rules,
	 * or with status 400 (Bad Request) if the rules is not valid,
	 * or with status 500 (Internal Server Error) if the rules couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/rules")
	@Timed
	public ResponseEntity<Rules> updateRules(@RequestBody Rules rules) throws URISyntaxException {
		log.debug("REST request to update Rules : {}", rules);
		if (rules.getId() == null) {
			return createRules(rules);
		}
		Rules result = rulesRepository.save(rules);
		//rulesSearchRepository.save(result);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, rules.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /rules : get all the rules.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of rules in body
	 */
	@GetMapping("/rules")
	@Timed
	public List<Rules> getAllRules() {
		log.debug("REST request to get all Rules");
		List<Rules> rules = rulesRepository.findAll();
		return rules;
	}

	/**
	 * GET  /rules/:id : get the "id" rules.
	 *
	 * @param id the id of the rules to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the rules, or with status 404 (Not Found)
	 */
	@GetMapping("/rules/{id}")
	@Timed
	public ResponseEntity<Rules> getRules(@PathVariable Long id) {
		log.debug("REST request to get Rules : {}", id);
		Rules rules = rulesRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rules));
	}

	/**
	 * DELETE  /rules/:id : delete the "id" rules.
	 *
	 * @param id the id of the rules to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/rules/{id}")
	@Timed
	public ResponseEntity<Void> deleteRules(@PathVariable Long id) {
		log.debug("REST request to delete Rules : {}", id);
		rulesRepository.delete(id);
		//rulesSearchRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH  /_search/rules?query=:query : search for the rules corresponding
	 * to the query.
	 *
	 * @param query the query of the rules search 
	 * @return the result of the search
	 */
	/*@GetMapping("/_search/rules")
	@Timed
	public List<Rules> searchRules(@RequestParam String query) {
		log.debug("REST request to search Rules for query {}", query);
		return StreamSupport
				.stream(rulesSearchRepository.search(queryStringQuery(query)).spliterator(), false)
				.collect(Collectors.toList());
	}*/




	/**
	 * Ravali
	 * @param tenantId
	 * @param rulePurpose
	 * @param ruleType
	 * @return List<Rules>
	 */
	@GetMapping("/getRulesByTenantIdAndRuleType/{tenantId}/{ruleType}")
	@Timed
	public List<Rules> getRuleBytypeAndPurpose(@RequestParam Long tenantId,@RequestParam String ruleType)
	{
		log.info("Rest Request to get rules by tenantId "+tenantId+",ruleType "+ruleType);

		List<Rules> rulesList=rulesRepository.findByTenantIdAndRuleTypeAndEnabledFlag(tenantId,ruleType,true);

		return rulesList;

	}



	//@MessageMapping("/hello")
	//@SendTo("/topic/greetings")
	@GetMapping("/rulessssss")

	//@Scheduled(fixedRate=5000)
	public List<Rules> getAllRulesList() throws Exception {
		log.debug("REST request to get all Rules");
		List<Rules> rules = rulesRepository.findAll();
		log.info("rules :"+rules.get(0));
		Thread.sleep(30000);
		return rules;
	}

	/**
	 * Ravali
	 * @param rule-Id
	 * @return RulesDTO
	 */

	@GetMapping("/rulesAndConditions")
	@Timed
	public RulesDTO getRulesAndConditions(@RequestParam Long id)
	{
		RulesDTO rulesDto=new RulesDTO();
		RuleDTO ruleDTO=new RuleDTO();
		/** Setting RuleDTO for rule**/
		Rules rule=rulesRepository.findOne(id);
		ruleDTO.setId(rule.getId());
		ruleDTO.setTenantId(rule.getTenantId());
		if(rule.getRuleCode()!=null && !rule.getRuleCode().isEmpty())
			ruleDTO.setRuleCode(rule.getRuleCode());
		if(rule.getStartDate()!=null)
			ruleDTO.setStartDate(rule.getStartDate());
		if(rule.getEndDate()!=null)
			ruleDTO.setEndDate(rule.getEndDate());
		if(rule.isEnabledFlag()!=null)
			ruleDTO.setEnabledFlag(rule.isEnabledFlag());
		if(rule.getRuleType()!=null && !rule.getRuleType().isEmpty())
			ruleDTO.setRuleType(rule.getRuleType());
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
//			ruleDTO.setSourceDataViewId(rule.getSourceDataViewId());


			DataViews tDataView=dataViewsRepository.findOne(rule.getSourceDataViewId());
			ruleDTO.setSourceDataViewId(tDataView.getIdForDisplay());
			if(tDataView!=null && tDataView.getDataViewName()!=null && !tDataView.getDataViewName().isEmpty())
				ruleDTO.setsDataViewName(tDataView.getDataViewName());
			if(tDataView!=null && tDataView.getDataViewDispName()!=null && !tDataView.getDataViewDispName().isEmpty())
				ruleDTO.setsDataViewDisplayName(tDataView.getDataViewDispName());
		}
		if(rule.getTargetDataViewId()!=null)
		{

//			ruleDTO.setTargetDataViewId(rule.getTargetDataViewId());

			DataViews tDataView=dataViewsRepository.findOne(rule.getTargetDataViewId());
			ruleDTO.setTargetDataViewId(tDataView.getIdForDisplay());
			if(tDataView!=null && tDataView.getDataViewName()!=null && !tDataView.getDataViewName().isEmpty())
				ruleDTO.settDataViewName(tDataView.getDataViewName());
			if(tDataView!=null && tDataView.getDataViewDispName()!=null && !tDataView.getDataViewDispName().isEmpty())
				ruleDTO.settDataViewDisplayName(tDataView.getDataViewDispName());
		}



		rulesDto.setRule(ruleDTO);

		List<RuleConditionsDTO> ruleCondDto=new ArrayList<RuleConditionsDTO>();
		List<RuleConditions> ruleConditionsList=ruleConditionsRepository.findByRuleId(id);
		for(RuleConditions cond:ruleConditionsList)
		{
			RuleConditionsDTO ruleCond=new RuleConditionsDTO();
			ruleCond.setId(cond.getId());
			if(cond.getRuleId()!=null)
				ruleCond.setRuleId(cond.getRuleId());
			if(cond.getOpenBracket()!=null && !cond.getOpenBracket().isEmpty())
				ruleCond.setOpenBracket(cond.getOpenBracket());
			/*if(cond.getsDataViewId()!=null)
    		{
    			ruleCond.setsDataViewId(cond.getsDataViewId());
    			DataViews srcdataView=dataViewsRepository.findOne(cond.getsDataViewId());
    			if(srcdataView!=null && srcdataView.getDataViewName()!=null && !srcdataView.getDataViewName().isEmpty())
    				ruleCond.setsDataviewName(srcdataView.getDataViewName());//Data view
    			if(srcdataView!=null && srcdataView.getDataViewDispName()!=null && !srcdataView.getDataViewDispName().isEmpty())
    				ruleCond.setsDataviewDisplayName(srcdataView.getDataViewDispName());//Data view
    		}*/
			if(cond.getsColumnId()!=null)
			{
				ruleCond.setsColumnId(cond.getsColumnId());
				DataViewsColumns srcDtViewColmn=dataViewsColumnsRepository.findOne(cond.getsColumnId());
				if(srcDtViewColmn!=null && srcDtViewColmn.getColDataType()!=null && !srcDtViewColmn.getColDataType().isEmpty())
					ruleCond.setsColumnType(srcDtViewColmn.getColDataType());
				if(srcDtViewColmn!=null && srcDtViewColmn.getColumnName()!=null && !srcDtViewColmn.getColumnName().isEmpty())
					ruleCond.setsColumnName(srcDtViewColmn.getColumnName());
			}
			if(cond.getsFormula()!=null && !cond.getsFormula().isEmpty())
				ruleCond.setsFormula(cond.getsFormula());
			if(cond.getsValueType()!=null && !cond.getsValueType().isEmpty())
				ruleCond.setsValueType(cond.getsValueType());
			if(cond.getsOperator()!=null && !cond.getsOperator().isEmpty())
				ruleCond.setsOperator(cond.getsOperator());
			if(cond.getsToleranceType()!=null && !cond.getsToleranceType().isEmpty())
				ruleCond.setsToleranceType(cond.getsToleranceType());
			if(cond.getsToleranceOperatorFrom()!=null && !cond.getsToleranceOperatorFrom().isEmpty())
				ruleCond.setsToleranceOperatorFrom(cond.getsToleranceOperatorFrom());
			if(cond.getsToleranceOperatorTo()!=null && !cond.getsToleranceOperatorTo().isEmpty())
				ruleCond.setsToleranceOperatorTo(cond.getsToleranceOperatorTo());
			if(cond.getsToleranceValueFrom()!=null && !cond.getsToleranceValueFrom().isEmpty())
				ruleCond.setsToleranceValueFrom(cond.getsToleranceValueFrom());
			if(cond.getsToleranceValueTo()!=null && !cond.getsToleranceValueTo().isEmpty())
				ruleCond.setsToleranceValueTo(cond.getsToleranceValueTo());
			if(cond.issMany()!=null)
				ruleCond.setsMany(cond.issMany());
			/*if(cond.getAggregationMethod()!=null && !cond.getAggregationMethod().isEmpty())
    			ruleCond.setAggregationMethod(cond.getAggregationMethod());
    		if(cond.gettDataViewId()!=null)
    		{
    			ruleCond.settDataViewId(cond.gettDataViewId());
    			DataViews tDataView=dataViewsRepository.findOne(cond.gettDataViewId());
    			if(tDataView!=null && tDataView.getDataViewName()!=null && !tDataView.getDataViewName().isEmpty())
    				ruleCond.settDataViewName(tDataView.getDataViewName());
    			if(tDataView!=null && tDataView.getDataViewDispName()!=null && !tDataView.getDataViewDispName().isEmpty())
    				ruleCond.settDataViewDisplayName(tDataView.getDataViewDispName());
    		}
    		if(cond.gettColumnType()!=null && !cond.gettColumnType().isEmpty())
    			ruleCond.settColumnType(cond.gettColumnType());*/
			if(cond.gettColumnId()!=null)
			{
				ruleCond.settColumnId(cond.gettColumnId());
				DataViewsColumns tDtViewColmn=dataViewsColumnsRepository.findOne(cond.gettColumnId());
				if(tDtViewColmn!=null && tDtViewColmn.getColumnName()!=null && !tDtViewColmn.getColumnName().isEmpty())
					ruleCond.settColumnName(tDtViewColmn.getColumnName());
			}

			if(cond.gettFormula()!=null && !cond.gettFormula().isEmpty())
				ruleCond.settFormula(cond.gettFormula());
			if(cond.istMany()!=null )
				ruleCond.settMany(cond.istMany());
			if(cond.gettToleranceOperatorFrom()!=null && !cond.gettToleranceOperatorFrom().isEmpty())
				ruleCond.settToleranceOperatorFrom(cond.gettToleranceOperatorFrom());
			if(cond.gettToleranceOperatorTo()!=null && !cond.gettToleranceOperatorTo().isEmpty())
				ruleCond.settToleranceOperatorTo(cond.gettToleranceOperatorTo());
			if(cond.gettToleranceType()!=null && !cond.gettToleranceType().isEmpty())
				ruleCond.settToleranceType(cond.gettToleranceType());
			if(cond.gettToleranceOperatorFrom()!=null && !cond.gettToleranceOperatorFrom().isEmpty())
				ruleCond.settToleranceValueFrom(cond.gettToleranceOperatorFrom());
			if(cond.gettToleranceOperatorTo()!=null && !cond.gettToleranceOperatorTo().isEmpty())
				ruleCond.settToleranceValueTo(cond.gettToleranceOperatorTo());
			if(cond.getCloseBracket()!=null && !cond.getCloseBracket().isEmpty())
				ruleCond.setCloseBracket(cond.getCloseBracket());
			if(cond.getLogicalOperator()!=null && !cond.getLogicalOperator().isEmpty())
				ruleCond.setLogicalOperator(cond.getLogicalOperator());
			if(cond.getCreatedBy()!=null)
				ruleCond.setCreatedBy(cond.getCreatedBy());
			if(cond.getLastUpdatedBy()!=null )
				ruleCond.setLastUpdatedBy(cond.getLastUpdatedBy());
			if(cond.getCreationDate()!=null )
				ruleCond.setCreationDate(cond.getCreationDate());
			if(cond.getLastUpdatedDate()!=null )
				ruleCond.setLastUpdatedDate(cond.getLastUpdatedDate());
			if(ruleCond!=null)
				ruleCondDto.add(ruleCond);


		}
		if(ruleCondDto!=null)
			rulesDto.setRuleConditions(ruleCondDto);
		return rulesDto;
	}
	/**
	 * Author : Shobha
	 * @param ruleId
	 * @return
	 */
	@GetMapping("/getExistingRuleDetailsByRuleId")
	@Timed
	public RulesDTO getExistingRuleDetailsByRuleId(@RequestParam Long ruleId)
	{
		log.info("Rest Request to get existing rule details by ruleId "+ruleId);
		RulesDTO rulesDTO=new RulesDTO();
		RuleDTO ruleDTO = new RuleDTO() ;
		Rules rule = new Rules();
		rule = rulesRepository.findOne(ruleId);
		log.info("rule :"+rule);
		ruleDTO.setId(rule.getId());
		ruleDTO.setTenantId(rule.getTenantId());
		if(rule.getRuleCode()!=null && !rule.getRuleCode().isEmpty())
			ruleDTO.setRuleCode(rule.getRuleCode());
		if(rule.getStartDate()!=null)
			ruleDTO.setStartDate(rule.getStartDate());
		if(rule.getEndDate()!=null)
			ruleDTO.setEndDate(rule.getEndDate());
		if(rule.isEnabledFlag()!=null)
			ruleDTO.setEnabledFlag(rule.isEnabledFlag());
		if(rule.getRuleType()!=null && !rule.getRuleType().isEmpty())
		{
			ruleDTO.setRuleType(rule.getRuleType());
			LookUpCode ruleTypeLookUp  = new LookUpCode();
			ruleTypeLookUp = lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("RULE_TYPE",rule.getRuleType(),rule.getTenantId());
			if(ruleTypeLookUp != null && ruleTypeLookUp.getMeaning() != null)
				ruleDTO.setRuleTypeMeaning(ruleTypeLookUp.getMeaning());
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
//			ruleDTO.setSourceDataViewId(rule.getSourceDataViewId());


			DataViews tDataView=dataViewsRepository.findOne(rule.getSourceDataViewId());
			ruleDTO.setSourceDataViewId(tDataView.getIdForDisplay());
			if(tDataView!=null && tDataView.getDataViewName()!=null && !tDataView.getDataViewName().isEmpty())
				ruleDTO.setsDataViewName(tDataView.getDataViewName());
			if(tDataView!=null && tDataView.getDataViewDispName()!=null && !tDataView.getDataViewDispName().isEmpty())
				ruleDTO.setsDataViewDisplayName(tDataView.getDataViewDispName());
		}
		if(rule.getTargetDataViewId()!=null)
		{

//			ruleDTO.setTargetDataViewId(rule.getTargetDataViewId());

			DataViews tDataView=dataViewsRepository.findOne(rule.getTargetDataViewId());
			ruleDTO.setTargetDataViewId(tDataView.getIdForDisplay());
			if(tDataView!=null && tDataView.getDataViewName()!=null && !tDataView.getDataViewName().isEmpty())
				ruleDTO.settDataViewName(tDataView.getDataViewName());
			if(tDataView!=null && tDataView.getDataViewDispName()!=null && !tDataView.getDataViewDispName().isEmpty())
				ruleDTO.settDataViewDisplayName(tDataView.getDataViewDispName());
		}


		rulesDTO.setId(ruleDTO.getId());
		rulesDTO.setItemName(ruleDTO.getRuleCode());
		rulesDTO.setRule(ruleDTO);
		List<RuleConditions> ruleConditionsList=ruleConditionsRepository.findByRuleId(rule.getId());
		List<RuleConditionsDTO> ruleCondDto=new ArrayList<RuleConditionsDTO>();
		for(RuleConditions cond:ruleConditionsList)
		{
			RuleConditionsDTO ruleCond=new RuleConditionsDTO();
			ruleCond.setId(cond.getId());
			if(cond.getRuleId()!=null)
				ruleCond.setRuleId(cond.getRuleId());
			if(cond.getOpenBracket()!=null && !cond.getOpenBracket().isEmpty())
				ruleCond.setOpenBracket(cond.getOpenBracket());

			if(cond.getsColumnId()!=null)
			{
				ruleCond.setsColumnId(cond.getsColumnId());
				DataViewsColumns srcDtViewColmn=dataViewsColumnsRepository.findOne(cond.getsColumnId());
				if(srcDtViewColmn!=null && srcDtViewColmn.getColDataType()!=null && !srcDtViewColmn.getColDataType().isEmpty())
					ruleCond.setsColumnType(srcDtViewColmn.getColDataType());
				if(srcDtViewColmn!=null && srcDtViewColmn.getColumnName()!=null && !srcDtViewColmn.getColumnName().isEmpty())
					ruleCond.setsColumnName(srcDtViewColmn.getColumnName());
				if(srcDtViewColmn!=null &&  srcDtViewColmn.getColDataType() != null)
				{
					ruleCond.setDataType(srcDtViewColmn.getColDataType());
					if(cond.getOperator() !=null)
					{
						ruleCond.setOperator(cond.getOperator());
						LookUpCode code = lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId(srcDtViewColmn.getColDataType(),cond.getOperator(),rule.getTenantId());
						if(code != null && code.getMeaning() != null)
						{
						ruleCond.setOperatorMeaning(code.getMeaning());	
						}
					}
				}
			}
			
			if(cond.getsFormula()!=null && !cond.getsFormula().isEmpty())
				ruleCond.setsFormula(cond.getsFormula());
			if(cond.getsValueType()!=null && !cond.getsValueType().isEmpty())
				ruleCond.setsValueType(cond.getsValueType());
			if(cond.getsOperator()!=null && !cond.getsOperator().isEmpty())
				ruleCond.setsOperator(cond.getsOperator());
			if(cond.getsValue()!=null && !cond.getsValue().isEmpty())
				ruleCond.setsValue(cond.getsValue());
			if(cond.getsToleranceType()!=null && !cond.getsToleranceType().isEmpty())
				ruleCond.setsToleranceType(cond.getsToleranceType());
			if(cond.getsToleranceOperatorFrom()!=null && !cond.getsToleranceOperatorFrom().isEmpty())
				ruleCond.setsToleranceOperatorFrom(cond.getsToleranceOperatorFrom());
			if(cond.getsToleranceOperatorTo()!=null && !cond.getsToleranceOperatorTo().isEmpty())
				ruleCond.setsToleranceOperatorTo(cond.getsToleranceOperatorTo());
			if(cond.getsToleranceValueFrom()!=null && !cond.getsToleranceValueFrom().isEmpty())
				ruleCond.setsToleranceValueFrom(cond.getsToleranceValueFrom());
			if(cond.getsToleranceValueTo()!=null && !cond.getsToleranceValueTo().isEmpty())
				ruleCond.setsToleranceValueTo(cond.getsToleranceValueTo());
			if(cond.issMany()!=null)
				ruleCond.setsMany(cond.issMany());

			if(cond.gettColumnId()!=null)
			{
				ruleCond.settColumnId(cond.gettColumnId());
				DataViewsColumns tDtViewColmn=dataViewsColumnsRepository.findOne(cond.gettColumnId());
				if(tDtViewColmn!=null && tDtViewColmn.getColumnName()!=null && !tDtViewColmn.getColumnName().isEmpty())
					ruleCond.settColumnName(tDtViewColmn.getColumnName());
			}
			if(cond.gettOperator()!=null && !cond.gettOperator().isEmpty())
				ruleCond.settOperator(cond.gettOperator());
			if(cond.gettValue()!=null && !cond.gettValue().isEmpty())
				ruleCond.settValue(cond.gettValue());
			if(cond.gettFormula()!=null && !cond.gettFormula().isEmpty())
				ruleCond.settFormula(cond.gettFormula());
			if(cond.istMany()!=null )
				ruleCond.settMany(cond.istMany());
			if(cond.gettToleranceOperatorFrom()!=null && !cond.gettToleranceOperatorFrom().isEmpty())
				ruleCond.settToleranceOperatorFrom(cond.gettToleranceOperatorFrom());
			if(cond.gettToleranceOperatorTo()!=null && !cond.gettToleranceOperatorTo().isEmpty())
				ruleCond.settToleranceOperatorTo(cond.gettToleranceOperatorTo());
			if(cond.gettToleranceType()!=null && !cond.gettToleranceType().isEmpty())
				ruleCond.settToleranceType(cond.gettToleranceType());
			if(cond.gettToleranceValueFrom()!=null && !cond.gettToleranceValueFrom().isEmpty())
				ruleCond.settToleranceValueFrom(cond.gettToleranceValueFrom());
			if(cond.gettToleranceValueTo()!=null && !cond.gettToleranceValueTo().isEmpty())
				ruleCond.settToleranceValueTo(cond.gettToleranceValueTo());
			if(cond.getCloseBracket()!=null && !cond.getCloseBracket().isEmpty())
				ruleCond.setCloseBracket(cond.getCloseBracket());
			if(cond.getLogicalOperator()!=null && !cond.getLogicalOperator().isEmpty())
			{
				ruleCond.setLogicalOperator(cond.getLogicalOperator());
				LookUpCode logicalOpLookUp  = new LookUpCode();
				logicalOpLookUp = lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("LOGICAL_OPERATOR",cond.getLogicalOperator(),rule.getTenantId());
				if(logicalOpLookUp != null && logicalOpLookUp.getMeaning() != null)
					ruleCond.setLogicalOperatorMeaning( logicalOpLookUp.getMeaning());

			}
			if(cond.getCreatedBy()!=null)
				ruleCond.setCreatedBy(cond.getCreatedBy());
			if(cond.getLastUpdatedBy()!=null )
				ruleCond.setLastUpdatedBy(cond.getLastUpdatedBy());
			if(cond.getCreationDate()!=null )
				ruleCond.setCreationDate(cond.getCreationDate());
			if(cond.getLastUpdatedDate()!=null )
				ruleCond.setLastUpdatedDate(cond.getLastUpdatedDate());
			if(ruleCond!=null)
				ruleCondDto.add(ruleCond);


		}

		rulesDTO.setRuleConditions(ruleCondDto);




		return rulesDTO;
	}

	/**
	 * Ravali
	 * @param tenantId
	 * @param rulePurpose
	 * @param ruleType
	 * @return List<Rules>
	 */
	@GetMapping("/getRulesByTenantIdAndRulePurposeAndRuleType")
	@Timed
	public List<HashMap<String,String>> getRuleBytypeAndPurpose(HttpServletRequest request,@RequestParam String rulePurpose,@RequestParam(value="ruleType",required=false) String ruleType, @RequestParam(required=false) String appProcess)
	{
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		log.info("Rest Request to get rules by tenantId "+tenantId+",rulePurpose "+rulePurpose);
		List<RulesDTO> rulesDTOList = new ArrayList<RulesDTO>();
		//rulepurpose based groupid's=> tenantId and rulepurpose in rulegroup table
		//fetch rule id's for these groups from grp details
		//apply id and rule type filter (In rule table)
		List<Long> ruleGroupIds=ruleGroupRepository.fetchIdsByTenantIdAndRulePurpose(tenantId,rulePurpose);
		List<HashMap<String,String>> ruleList = new ArrayList<HashMap<String,String>>();
		log.info("ruleGroupIds :"+ruleGroupIds);
		if(ruleGroupIds.size() > 0)
		{
			List<Object[]> ruleIds=ruleGroupDetailsRepository.fetchRuleIdsByRuleGroupIds(ruleGroupIds);
			log.info("ruleIds :"+ruleIds);
			HashMap<Long,Long> ruleToAssignmentIdMap = new HashMap<Long,Long>();
			List<Long> ruleIdList = new ArrayList<Long>();
			for(int i=0;i<ruleIds.size();i++)
			{
				Object[] objArr = new Object[2];
				objArr = ruleIds.get(i);
				ruleToAssignmentIdMap.put( Long.valueOf(objArr[1].toString()),Long.valueOf(objArr[0].toString()));
				ruleIdList.add(Long.valueOf(objArr[1].toString()));
			}
			log.info("ruleIdList"+ruleIdList);
			log.info("ruleToAssignmentIdMap:"+ruleToAssignmentIdMap);
			List<Rules> rulesList = new ArrayList<Rules>();
			if(rulePurpose.equalsIgnoreCase("reconciliation"))
			{
				if(ruleIds.size() > 0)
				{
					if(ruleType == null || ruleType.equals("") || ruleType.isEmpty() || ruleType.equals("undefined") && ruleIdList.size() > 0)
						rulesList = rulesRepository.findByIdsAndTenantIdAndEnabledFlag(ruleIdList,tenantId);//rule id's input
					else	if(ruleType != null && !ruleType.equals("") && !ruleType.isEmpty() && ruleIdList.size() > 0)
						rulesList = rulesRepository.findByIdsAndTenantIdAndRuleTypeAndEnabledFlag(ruleIdList,tenantId,ruleType);//rule id's input
				}

			}
			else if(rulePurpose.equalsIgnoreCase("ACCOUNTING")) {
				rulesList = rulesRepository.findByIdsAndTenantIdAndEnabledFlag(ruleIdList,tenantId);//rule id's input
			}
			else if( rulePurpose.equalsIgnoreCase("APPROVALS"))
			{
				log.info("1=>"+appProcess);
				if(appProcess != null && !appProcess.isEmpty() && appProcess != "" )
				{
					log.info("2=>");
					RuleGroup ruleGroup = new RuleGroup();
					ruleGroup = ruleGroupRepository.findByIdForDisplayAndTenantId(appProcess, tenantId);
					log.info("ruleGroup.getId()"+ruleGroup.getId());
					if(ruleGroup != null && ruleGroup.getId() != null){
						log.info("3=>");
						rulesList = rulesRepository.fetchFilteredRulesByReconOrAccRuleGroup(ruleIdList,ruleGroup.getId());
						log.info("rulesList:"+rulesList);
					}
					else
					{
						log.info("4=>");
						rulesList = rulesRepository.findByIdsAndTenantIdAndEnabledFlag(ruleIdList,tenantId);//rule id's input
					}
				
					log.info("5=>");
				}
				else
				{
					log.info("6=>");
					rulesList = rulesRepository.findByIdsAndTenantIdAndEnabledFlag(ruleIdList,tenantId);//rule id's input
				}
				
			}
			log.info("ruleList.size"+ruleList.size());
			for(int i = 0;i<rulesList.size();i++)
			{
				HashMap<String,String> ruleObj = new HashMap<String,String>();
				ruleObj.put("id",rulesList.get(i).getId()+"");
				ruleObj.put("ruleCode", rulesList.get(i).getRuleCode());
				ruleList.add(ruleObj);

			}
		}
		else
		{

		}



		return ruleList;
	}


	/**
	 * author:ravali
	 * @param tenantId
	 * @param rulePurpose
	 * @return getAcctRulesByTenantIdAndRulePurposeAndRuleType
	 */
	@GetMapping("/getAcctRulesByTenantIdAndRulePurposeAndRuleType")
	@Timed
	public List<RulesAndLineItems> getAcctRuleBytypeAndPurpose(HttpServletRequest request,@RequestParam String rulePurpose)
	{
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		
		log.info("Rest Request to get rules by tenantId "+tenantId+",rulePurpose "+rulePurpose);
		List<RulesAndLineItems> rulesDTOList = new ArrayList<RulesAndLineItems>();
		//rulepurpose based groupid's=> tenantId and rulepurpose in rulegroup table
		//fetch rule id's for these groups from grp details
		//apply id and rule type filter (In rule table)
		List<Long> ruleGroupIds=ruleGroupRepository.fetchIdsByTenantIdAndRulePurpose(tenantId,rulePurpose);
		log.info("ruleGroupIds :"+ruleGroupIds);
		if(ruleGroupIds.size() > 0)
		{



			List<Long> ruleIds=ruleGroupDetailsRepository.fetchByRuleGroupIds(ruleGroupIds);
			log.info("ruleIds :"+ruleIds);
			List<Rules> rulesList = new ArrayList<Rules>();
			if(ruleIds.size()>0)
				rulesList = rulesRepository.findByIdsAndTenantIdAndEnabledFlag(ruleIds,tenantId);//rule id's input
			//log.info("rulesList :"+rulesList);

			for(int i = 0;i<rulesList.size();i++)
			{

				RulesAndLineItems rulesAndLineItems=new RulesAndLineItems();
				//RulesDTO rulesDTO=new RulesDTO();
				//RuleDTO ruleDTO = new RuleDTO() ;
				Rules rule = new Rules();
				rule = rulesList.get(i);
				log.info("rule :"+rule);
				rulesAndLineItems.setId(rule.getId());
				if(rule.getRuleCode()!=null && !rule.getRuleCode().isEmpty())
					rulesAndLineItems.setRuleCode(rule.getRuleCode());
				if(rule.getStartDate()!=null)
					rulesAndLineItems.setStartDate(rule.getStartDate());
				if(rule.getEndDate()!=null)
					rulesAndLineItems.setEndDate(rule.getEndDate());
				if(rule.isEnabledFlag()!=null)
					rulesAndLineItems.setEnabledFlag(rule.isEnabledFlag());
				if(rule.getRuleType()!=null && !rule.getRuleType().isEmpty())
					rulesAndLineItems.setRuleType(rule.getRuleType());
				if(rule.getcOA()!=null && !rule.getcOA().isEmpty())
					rulesAndLineItems.setCoa(rule.getcOA());

				List<LineItems> linesItemList=new ArrayList<LineItems>();


				List<AccountingLineTypes> acctngLineTypesList=accountingLineTypesRepository.findByRuleId(rule.getId());
				for(AccountingLineTypes acctngLineTypes:acctngLineTypesList)
				{
					LineItems lineItem=new LineItems();
					lineItem.setId(acctngLineTypes.getId());
					if(acctngLineTypes.getLineType()!=null && !acctngLineTypes.getLineType().isEmpty())
						lineItem.setLineType(acctngLineTypes.getLineType());
					if(acctngLineTypes.getDataViewId()!=null)
					{
						lineItem.setSourceDataviewId(acctngLineTypes.getDataViewId());
						DataViews dataViewName=dataViewsRepository.findOne(acctngLineTypes.getDataViewId());
						if(dataViewName!=null && !dataViewName.getDataViewName().isEmpty())
							lineItem.setSourceDataviewName(dataViewName.getDataViewName());
					}
					List<AcctRuleCondDTO> acctRuleCondDtoList=new ArrayList<AcctRuleCondDTO>();
					List<AcctRuleConditions> acctRuleConditionsList=acctRuleConditionsRepository.findByRuleActionId(acctngLineTypes.getId());
					for(AcctRuleConditions acctRuleCond:acctRuleConditionsList)
					{
						AcctRuleCondDTO acctRuleCondDTO=new AcctRuleCondDTO();
						acctRuleCondDTO.setId(acctRuleCond.getId());
						if(acctRuleCond.getOpenBracket()!=null && !acctRuleCond.getOpenBracket().isEmpty())
							acctRuleCondDTO.setOpenBracket(acctRuleCond.getOpenBracket());
						if(acctRuleCond.getOperator()!=null && !acctRuleCond.getOperator().isEmpty())
							acctRuleCondDTO.setOperator(acctRuleCond.getOperator());
						if(acctRuleCond.getValue()!=null && !acctRuleCond.getValue().isEmpty())
							acctRuleCondDTO.setValue(acctRuleCond.getValue());
						if(acctRuleCond.getCloseBracket()!=null && !acctRuleCond.getCloseBracket().isEmpty())
							acctRuleCondDTO.setCloseBracket(acctRuleCond.getCloseBracket());
						if(acctRuleCond.getLogicalOperator()!=null && !acctRuleCond.getLogicalOperator().isEmpty())
							acctRuleCondDTO.setLogicalOperator(acctRuleCond.getLogicalOperator());
						if(acctRuleCond.getsViewColumnId()!=null)
						{
							acctRuleCondDTO.setsViewColumnId(acctRuleCond.getsViewColumnId());
							DataViewsColumns dViewCol=dataViewsColumnsRepository.findOne(acctRuleCond.getsViewColumnId());
							if(dViewCol!=null)
								acctRuleCondDTO.setsViewColumnName(dViewCol.getColumnName());
						}
						acctRuleCondDtoList.add(acctRuleCondDTO);
					}


					List<AcctRuleDerivationDTO> acctRuleDerDTOList=new ArrayList<AcctRuleDerivationDTO>();
					List<AcctRuleDerivations> acctRuleDerList=acctRuleDerivationsRepository.findByAcctRuleActionId(acctngLineTypes.getId());
					for(AcctRuleDerivations acctRuleDer:acctRuleDerList)
					{
						AcctRuleDerivationDTO acctRuleDerDTO=new AcctRuleDerivationDTO();

						acctRuleDerDTO.setId(acctRuleDer.getId());
						if(acctRuleDer.getDataViewColumn()!=null &&!acctRuleDer.getDataViewColumn().isEmpty())
							acctRuleDerDTO.setDataViewColumn(Long.valueOf(acctRuleDer.getDataViewColumn()));
						if(acctRuleDer.getAccountingReferences()!=null && !acctRuleDer.getAccountingReferences().isEmpty())
						{
							acctRuleDerDTO.setAccountingReferencesCode(acctRuleDer.getAccountingReferences());
							LookUpCode lookUpCode=lookUpCodeRepository.findByTenantIdAndLookUpCodeAndLookUpType(tenantId,acctRuleDer.getAccountingReferences(),rule.getcOA());
							if(lookUpCode != null && lookUpCode.getMeaning()!=null)
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


					lineItem.setAccountingRuleConditions(acctRuleCondDtoList);
					lineItem.setAccountingRuleDerivations(acctRuleDerDTOList);
					linesItemList.add(lineItem);

				}
				rulesAndLineItems.setLineItems(linesItemList);
				rulesDTOList.add(rulesAndLineItems);

			}
		}
		else
		{

		}

		return rulesDTOList;
	}


	/**
	 * author :ravali
	 * @param tenantId
	 * @param rulePurpose
	 * @return
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	@GetMapping("/getAppRulesByTenantIdAndRulePurpose")
	@Timed
	public List<AppRuleCondAndActDto> getAppRuleByRulePurpose(HttpServletRequest request,@RequestParam Long ruleId) throws ClassNotFoundException, SQLException
	{
		HashMap map0=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map0.get("tenantId").toString());
		log.info("Rest Request to get rules by tenantId "+tenantId);
		List<AppRuleCondAndActDto> rulesDTOList = new ArrayList<AppRuleCondAndActDto>();
		//rulepurpose based groupid's=> tenantId and rulepurpose in rulegroup table
		//fetch rule id's for these groups from grp details
		//apply id and rule type filter (In rule table)
		//List<Long> ruleGroupIds=ruleGroupRepository.fetchIdsByTenantIdAndRulePurpose(tenantId,rulePurpose);
		//List<Long> ruleGroupIds= new ArrayList<Long>();
		//ruleGroupIds.add(ruleId);
		//log.info("ruleGroupIds :"+ruleGroupIds);
		//if(ruleGroupIds.size() > 0)
		//{

			List<Long> ruleIds= new ArrayList<Long>();
			ruleIds.add(ruleId);
					//ruleGroupDetailsRepository.fetchByRuleGroupIds(ruleGroupIds);
			log.info("ruleIds :"+ruleIds);
			List<Rules> rulesList = new ArrayList<Rules>();
			rulesList = rulesRepository.findByIdsAndTenantIdAndEnabledFlag(ruleIds,tenantId);//rule id's input
			//log.info("rulesList :"+rulesList);

			for(int i = 0;i<rulesList.size();i++)
			{

				AppRuleCondAndActDto rules=new AppRuleCondAndActDto();
				//RulesDTO rulesDTO=new RulesDTO();
				//RuleDTO ruleDTO = new RuleDTO() ;
				Rules rule = new Rules();
				rule = rulesList.get(i);
				log.info("rule :"+rule);
				rules.setId(rule.getId());
				if(rule.getRuleCode()!=null && !rule.getRuleCode().isEmpty())
					rules.setRuleCode(rule.getRuleCode());
				if(rule.getStartDate()!=null)
					rules.setStartDate(rule.getStartDate());
				if(rule.getEndDate()!=null)
					rules.setEndDate(rule.getEndDate());
				if(rule.isEnabledFlag()!=null)
					rules.setEnabledFlag(rule.isEnabledFlag());
				if(rule.getRuleType() !=null)
				{
					rules.setApprovalNeededType( rule.getRuleType() );
					LookUpCode lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("APPROVAL_TYPE",rule.getRuleType(), rule.getTenantId());
					if(lookUpCode != null &&  lookUpCode.getMeaning() != null)
						rules.setApprovalNeededTypeMeaning( lookUpCode.getMeaning() );
				}

				if(rule.getSourceDataViewId()!=null)
				{
					
					DataViews dv=dataViewsRepository.findOne(rule.getSourceDataViewId());
					rules.setSourceDataViewId(dv.getIdForDisplay());
					if(dv!=null && dv.getDataViewName()!=null)
						rules.setSourceDataViewName(dv.getDataViewName());
				}

				List<AppRuleCondDto> appRuleCondDtoList=new ArrayList<AppRuleCondDto>();


				List<AppRuleConditions> appRuleCondList=appRuleConditionsRepository.findByRuleId(rule.getId());
				log.info("appRuleCondList size:"+appRuleCondList.size());
				for(AppRuleConditions appRuleCond:appRuleCondList)
				{
					log.info("=>"+(appRuleCondDtoList.size()+1));
					AppRuleCondDto appRuleCondDto=new AppRuleCondDto();
					if(appRuleCond.getOpenBracket() != null)
					{
						appRuleCondDto.setOpenBracket(appRuleCond.getOpenBracket());
					}
					if(appRuleCond.getCloseBracket() != null)
					{
						appRuleCondDto.setCloseBracket(appRuleCond.getCloseBracket());
					}
					appRuleCondDto.setId(appRuleCond.getId());
					if(appRuleCond.getColumnId()!=null)
					{
						appRuleCondDto.setColumnId(appRuleCond.getColumnId());
						DataViewsColumns dvcolName=dataViewsColumnsRepository.findOne(appRuleCond.getColumnId());
						if(dvcolName!=null && dvcolName.getColumnName()!=null)
							appRuleCondDto.setColumnName(dvcolName.getColumnName());
						if(dvcolName != null && dvcolName.getColDataType() != null)
							appRuleCondDto.setColDataType(dvcolName.getColDataType());
					}
					if(appRuleCond.getOperator()!=null)
					{
						appRuleCondDto.setOperator(appRuleCond.getOperator());
						LookUpCode lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("OPERATOR", appRuleCond.getOperator(), rule.getTenantId());
						if(lookUpCode != null && lookUpCode.getMeaning()!= null)
							appRuleCondDto.setOperatorMeaning(lookUpCode.getMeaning());
					}
					if(appRuleCond.getLogicalOperator()!=null)
					{
						appRuleCondDto.setLogicalOperator(appRuleCond.getLogicalOperator());
						LookUpCode lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("LOGICAL_OPERATOR", appRuleCond.getLogicalOperator(), rule.getTenantId());
						if(lookUpCode != null && lookUpCode.getMeaning()!= null)
							appRuleCondDto.setLogicalOperatorMeaning(lookUpCode.getMeaning());
					}
					if(appRuleCond.getValue() != null)
						appRuleCondDto.setValue(appRuleCond.getValue());
					appRuleCondDtoList.add(appRuleCondDto);
				}
				rules.setApprovalConditions(appRuleCondDtoList);
				ApprovalActionDto appActDto=new ApprovalActionDto();
				List<ApprovalRuleAssignment> appRuleAssignList=approvalRuleAssignmentRepository.findByRuleId(rule.getId());
				log.info("appRuleAssignList size:"+appRuleAssignList.size());
				List<ApprRuleAssgnDto> apprRuleAssgnDtoList=new ArrayList<ApprRuleAssgnDto>();
				for(ApprovalRuleAssignment  AppRuleAssign:appRuleAssignList)
				{
					log.info("AppRuleAssign =>"+(appRuleAssignList.size()+1));
					ApprRuleAssgnDto apprRuleAssgnDto=new ApprRuleAssgnDto();
					apprRuleAssgnDto.setId(AppRuleAssign.getId());
					appActDto.setAssigneeType(AppRuleAssign.getAssignType());
					apprRuleAssgnDto.setAssigneeId(AppRuleAssign.getAssigneeId());
					if(AppRuleAssign.getAssignType().equalsIgnoreCase("USER"))
					{
						log.info("assignee type is user");
						
						HashMap map=userJdbcService.jdbcConnc(AppRuleAssign.getAssigneeId(),tenantId);

						if(map!=null && map.get("loginName")!=null)
							apprRuleAssgnDto.setLoginName(map.get("loginName").toString());
						if(map!=null && map.get("assigneeName")!=null)
							apprRuleAssgnDto.setAssigneeName(map.get("assigneeName").toString());

					}
					else if(AppRuleAssign.getAssignType().equalsIgnoreCase("GROUP"))
					{
						log.info("assignee type is group");
						
						ApprovalGroups appGroup = new ApprovalGroups();
						log.info("assignee is"+AppRuleAssign.getAssigneeId());
						if(AppRuleAssign.getAssigneeId() != null)
							appGroup=approvalGroupsRepository.findOne(AppRuleAssign.getAssigneeId());
						if(appGroup != null)
							apprRuleAssgnDto.setAssigneeName(appGroup.getGroupName());
					}
					apprRuleAssgnDto.setAutoApproval(AppRuleAssign.isAutoApproval());
					apprRuleAssgnDto.setNotification(true);
					apprRuleAssgnDto.setEmail(AppRuleAssign.isEmail());
					apprRuleAssgnDtoList.add(apprRuleAssgnDto);

				}
				log.info("apprRuleAssgnDtoList size:"+apprRuleAssgnDtoList.size());

				appActDto.setActionDetails(apprRuleAssgnDtoList);
				rules.setApprovalActions(appActDto);
				rulesDTOList.add(rules);

			}
		//}


		return rulesDTOList;
	}

	/**
	 * Author : Shobha,Ravali
	 * @param tenantId
	 * @param rulePurpose
	 * @return getAcctRulesdefinitionByTenantIdAndRulePurpose
	 */
	@GetMapping("/getExistingAccountingRuleDetailsByRuleId")
	@Timed
	public RulesAndLineItems getExistingAccountingRuleDetailsByRuleId(@RequestParam Long ruleId)
	{

		log.info("Rest Request to get existing accounting rule by ruleId "+ruleId);

		Rules rule=rulesRepository.findOne(ruleId);

		RulesAndLineItems rules=new RulesAndLineItems();


		rules.setId(rule.getId());
		if(rule.getRuleCode()!=null && !rule.getRuleCode().isEmpty())
			rules.setRuleCode(rule.getRuleCode());
		if(rule.getStartDate()!=null)
			rules.setStartDate(rule.getStartDate());
		if(rule.getEndDate()!=null)
			rules.setEndDate(rule.getEndDate());
		if(rule.isEnabledFlag()!=null)
			rules.setEnabledFlag(rule.isEnabledFlag());
		if(rule.getTenantId()!=null)
			rules.setTenantId(rule.getTenantId());
		if(rule.getRuleType()!=null && !rule.getRuleType().isEmpty())
		{
			rules.setRuleType(rule.getRuleType());
			LookUpCode lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("RULE_TYPE",rule.getRuleType(), rule.getTenantId());
			if(lookUpCode != null &&  lookUpCode.getMeaning() != null)
				rules.setRuleTypeMeaning( lookUpCode.getMeaning() );
		}

		if(rule.getcOA()!=null && !rule.getcOA().isEmpty())
		{
			rules.setCoa(rule.getcOA());
			LookUpCode lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("CHART_OF_ACCOUNTS",rule.getcOA(), rule.getTenantId());
			if(lookUpCode != null && lookUpCode.getMeaning()!=null )
				rules.setCoaMeaning(lookUpCode.getMeaning());
		}
		if(rule.getSourceDataViewId() != null)
		{
			//List<HashMap> sColList = new ArrayList<HashMap>();
			//	sColList = dataViewsService.fetchDataViewAndColumnsByDvId(rule.getSourceDataViewId());
			DataViews dv = new DataViews();
			dv =dataViewsRepository.findOne(rule.getSourceDataViewId());
//		rules.setSourceDataViewId(rule.getSourceDataViewId());
			rules.setSourceDataViewId(dv.getIdForDisplay());

			rules.setSourceDataViewName(dv.getDataViewDispName());

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


		List<AcctRuleDerivationDTO> headerDerivationRulesList=new ArrayList<AcctRuleDerivationDTO>();
		List<AcctRuleDerivations> acctRuleHeaderDerList=acctRuleDerivationsRepository.findByAcctRuleActionIdIsNullAndRuleId(rule.getId());
		for(AcctRuleDerivations acctRuleDer:acctRuleHeaderDerList)
		{

			AcctRuleDerivationDTO acctRuleDerDTO=new AcctRuleDerivationDTO();

			acctRuleDerDTO.setId(acctRuleDer.getId());
			if(acctRuleDer.getCriteria() != null)
			{
				acctRuleDerDTO.setCriteria(acctRuleDer.getCriteria());
				LookUpCode lookUpCode=lookUpCodeRepository.findByTenantIdAndLookUpCodeAndLookUpType(rule.getTenantId(),acctRuleDer.getCriteria(),"ACCOUNTING_CRITERIA_TYPES");
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
					LookUpCode lookUpCode=lookUpCodeRepository.findByTenantIdAndLookUpCodeAndLookUpType(rule.getTenantId(),acctRuleDer.getOperator(),dvCol.getColDataType());
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

			}

			if(acctRuleDer.getAccountingReferences()!=null && !acctRuleDer.getAccountingReferences().isEmpty())
			{
				acctRuleDerDTO.setAccountingReferencesCode(acctRuleDer.getAccountingReferences());
				LookUpCode lookUpCode=lookUpCodeRepository.findByTenantIdAndLookUpCodeAndLookUpType(rule.getTenantId(),acctRuleDer.getAccountingReferences(),"ACC_HEADER_LINE_TYPES");
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
						LookUpCode lookUpCode=lookUpCodeRepository.findByTenantIdAndLookUpCodeAndLookUpType(rules.getTenantId(),acctRuleDer.getSegValue(),"SOURCE");
						if(lookUpCode != null &&lookUpCode.getMeaning()!=null)
						{
							acctRuleDerDTO.setSegValue(lookUpCode.getLookUpCode());
							acctRuleDerDTO.setSegValueMeaning(lookUpCode.getMeaning());
						}

					}
					else if(acctRuleDer.getAccountingReferences().equalsIgnoreCase("CATEGORY")){
						//from lookups
						LookUpCode lookUpCode=lookUpCodeRepository.findByTenantIdAndLookUpCodeAndLookUpType(rules.getTenantId(),acctRuleDer.getSegValue(),"CATEGORY");
						if(lookUpCode != null &&lookUpCode.getMeaning()!=null)
						{
							acctRuleDerDTO.setSegValueMeaning(lookUpCode.getMeaning());
							acctRuleDerDTO.setSegValue(lookUpCode.getLookUpCode());
						}

					}

				}
			}
			
			//acctRuleDerDTO.setSegValue(acctRuleDer.getSegValue());
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
					if(dViewCol!=null )
					{
						if(dViewCol.getColumnName() != null)
						acctRuleCondDTO.setsViewColumnName(dViewCol.getColumnName());
						if(dViewCol.getColDataType() != null)
							acctRuleCondDTO.setDataType(dViewCol.getColDataType());
					}
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
				if(acctRuleDer.getCriteria() != null)
				{
					acctRuleDerDTO.setCriteria(acctRuleDer.getCriteria());
					LookUpCode lookUpCode=lookUpCodeRepository.findByTenantIdAndLookUpCodeAndLookUpType(rule.getTenantId(),acctRuleDer.getCriteria(),"ACCOUNTING_CRITERIA_TYPES");
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
					LookUpCode lookUpCode=lookUpCodeRepository.findByTenantIdAndLookUpCodeAndLookUpType(rule.getTenantId(),acctRuleDer.getAccountingReferences(),rule.getcOA());
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
				acctRuleDerDTO.setSegValue(acctRuleDer.getSegValue());
				acctRuleDerDTO.setType(acctRuleDer.getType());
				acctRuleDerDTO.setRuleId(acctRuleDer.getRuleId());

				acctRuleDerDTOList.add(acctRuleDerDTO);


			}

			lineType.setAccountingRuleConditions(acctRuleCondDTOList);
			lineType.setAccountingRuleDerivations(acctRuleDerDTOList);
			lineDerivationRulesList.add(lineType);
		}

		rules.setLineDerivationRules(lineDerivationRulesList);


		return rules;
	}
	/**
	 * author:ravali
	 * @param tenantId
	 * @param rulePurpose
	 * @return getAcctRulesdefinitionByTenantIdAndRulePurpose
	 */
	@GetMapping("/getAcctRulesdefinitionByTenantIdAndRulePurpose")
	@Timed
	public List<HashMap<String,String>> getAcctRulesdefinitionByTenantIdAndRulePurpose(HttpServletRequest request,@RequestParam String rulePurpose,@RequestParam String coaId,@RequestParam Boolean activityBased)
	{
		HashMap map1=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map1.get("tenantId").toString());
		log.info("Rest Request to get rules by tenantId "+tenantId+",rulePurpose "+rulePurpose);
		List<RulesAndLineItems> rulesDTOList = new ArrayList<RulesAndLineItems>();
		//rulepurpose based groupid's=> tenantId and rulepurpose in rulegroup table
		//fetch rule id's for these groups from grp details
		//apply id and rule type filter (In rule table)
		 ChartOfAccount coa = new ChartOfAccount();
		 if(coaId != null)
		 {
			 coa = chartOfAccountRepository.findByTenantIdAndIdForDisplay(tenantId, coaId);
		 }
		 
		List<Long> ruleGroupIds= new ArrayList<Long>();
		if(activityBased != null && activityBased==true)
		{
			ruleGroupIds = ruleGroupRepository.fetchIdsByTenantIdAndRulePurposeAndActivityBasedIsTrue(tenantId,rulePurpose); 
		}
		else
			ruleGroupIds = ruleGroupRepository.fetchIdsByTenantIdAndRulePurposeAndActivityBasedIsNullOrFalse(tenantId,rulePurpose); 
		List<HashMap<String,String>> ruleList = new ArrayList<HashMap<String,String>>();
		//log.info("ruleGroupIds :"+ruleGroupIds);
		if(ruleGroupIds.size() > 0)
		{
			List<Long> ruleIds=ruleGroupDetailsRepository.fetchByRuleGroupIds(ruleGroupIds);
			log.info("ruleIds :"+ruleIds);
			List<Rules> rulesList = new ArrayList<Rules>();
			if(ruleIds.size()>0)
			{
				if(coa != null && coa.getId() != null)
				{
					log.info("coa is not null");
					rulesList = rulesRepository.findByIdsAndTenantIdAndCoaAndEnabledFlag(ruleIds,tenantId,coa.getId());//rule id's input	
				}
				else
				{
					rulesList = rulesRepository.findByIdsAndTenantIdAndEnabledFlag(ruleIds,tenantId);//rule id's input
				}
			}
				
			log.info("rulesListsize :"+rulesList.size());

			for(int i = 0;i<rulesList.size();i++)
			{
				HashMap<String,String> ruleObj = new HashMap<String,String>();

				Rules rule=rulesRepository.findOne(rulesList.get(i).getId());

				ruleObj.put("id",rule.getId()+"");
				ruleObj.put("ruleCode", rule.getRuleCode());
				ruleList.add(ruleObj);
				/*RulesAndLineItems rules=new RulesAndLineItems();


						rules.setId(rule.getId());
		    			if(rule.getRuleCode()!=null && !rule.getRuleCode().isEmpty())
		    				rules.setRuleCode(rule.getRuleCode());
		    			if(rule.getStartDate()!=null)
		    				rules.setStartDate(rule.getStartDate());
		    			if(rule.getEndDate()!=null)
		    				rules.setEndDate(rule.getEndDate());
		    			if(rule.isEnabledFlag()!=null)
		    				rules.setEnabledFlag(rule.isEnabledFlag());
		    			if(rule.getTenantId()!=null)
		    			rules.setTenantId(rule.getTenantId());
		    			if(rule.getRuleType()!=null && !rule.getRuleType().isEmpty())
		    			{
		    				rules.setRuleType(rule.getRuleType());
		    				LookUpCode lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("RULE_TYPE",rule.getRuleType(), rule.getTenantId());
	    					if(lookUpCode != null &&  lookUpCode.getMeaning() != null)
	    						rules.setRuleTypeMeaning( lookUpCode.getMeaning() );
		    			}

		    			if(rule.getCOA()!=null && !rule.getCOA().isEmpty())
		    			{
		    				rules.setCoa(rule.getCOA());
		    				LookUpCode lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("CHART_OF_ACCOUNTS",rule.getCOA(), rule.getTenantId());
		    				if(lookUpCode != null && lookUpCode.getMeaning()!=null )
		    					rules.setCoaMeaning(lookUpCode.getMeaning());
		    			}
		    			if(rule.getSourceDataViewId() != null)
						{
							//List<HashMap> sColList = new ArrayList<HashMap>();
						//	sColList = dataViewsService.fetchDataViewAndColumnsByDvId(rule.getSourceDataViewId());
							DataViews dv = new DataViews();
							dv =dataViewsRepository.findOne(rule.getSourceDataViewId());
							rules.setSourceDataViewId(rule.getSourceDataViewId());
							rules.setSourceDataViewName(dv.getDataViewName());

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


		    			List<AcctRuleDerivationDTO> headerDerivationRulesList=new ArrayList<AcctRuleDerivationDTO>();
		    			List<AcctRuleDerivations> acctRuleHeaderDerList=acctRuleDerivationsRepository.findByAcctRuleActionIdIsNullAndRuleId(rule.getId());
		    			for(AcctRuleDerivations acctRuleDer:acctRuleHeaderDerList)
		    			{

		    				AcctRuleDerivationDTO acctRuleDerDTO=new AcctRuleDerivationDTO();

	    					acctRuleDerDTO.setId(acctRuleDer.getId());
	    					if(acctRuleDer.getCriteria() != null)
	    					{
	    						acctRuleDerDTO.setCriteria(acctRuleDer.getCriteria());
	    						LookUpCode lookUpCode=lookUpCodeRepository.findByTenantIdAndLookUpCodeAndLookUpType(rule.getTenantId(),acctRuleDer.getCriteria(),"ACCOUNTING_CRITERIA_TYPES");
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
	    							LookUpCode lookUpCode=lookUpCodeRepository.findByTenantIdAndLookUpCodeAndLookUpType(rule.getTenantId(),acctRuleDer.getOperator(),dvCol.getColDataType());
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

	    					}

	    					if(acctRuleDer.getAccountingReferences()!=null && !acctRuleDer.getAccountingReferences().isEmpty())
	    					{
	    						acctRuleDerDTO.setAccountingReferencesCode(acctRuleDer.getAccountingReferences());
	    						LookUpCode lookUpCode=lookUpCodeRepository.findByTenantIdAndLookUpCodeAndLookUpType(rule.getTenantId(),acctRuleDer.getAccountingReferences(),"ACC_HEADER_LINE_TYPES");
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
	    					acctRuleDerDTO.setSegValue(acctRuleDer.getSegValue());
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
		    					if(acctRuleDer.getCriteria() != null)
		    					{
		    						acctRuleDerDTO.setCriteria(acctRuleDer.getCriteria());
		    						LookUpCode lookUpCode=lookUpCodeRepository.findByTenantIdAndLookUpCodeAndLookUpType(rule.getTenantId(),acctRuleDer.getCriteria(),"ACCOUNTING_CRITERIA_TYPES");
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
		    						LookUpCode lookUpCode=lookUpCodeRepository.findByTenantIdAndLookUpCodeAndLookUpType(rule.getTenantId(),acctRuleDer.getAccountingReferences(),rule.getCOA());
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
		    					acctRuleDerDTO.setSegValue(acctRuleDer.getSegValue());
		    					acctRuleDerDTO.setType(acctRuleDer.getType());
		    					acctRuleDerDTO.setRuleId(acctRuleDer.getRuleId());

		    					acctRuleDerDTOList.add(acctRuleDerDTO);


			    			}

		    				lineType.setAccountingRuleConditions(acctRuleCondDTOList);
		    				lineType.setAccountingRuleDerivations(acctRuleDerDTOList);
		    				lineDerivationRulesList.add(lineType);
		    			}

		    			rules.setLineDerivationRules(lineDerivationRulesList);
		    			rulesDTOList.add(rules);*/
			}
		}
		else
		{

		}

		return ruleList;
	}



/**
 * author Kiran
 * @param request
 * @param ruleGroupId
 * @param viewId
 * @return
 * @throws URISyntaxException
 */
	@GetMapping("/dataViewsBasedOnTenantId")
	@Timed
	public HashMap<String,List<HashMap<String,String>>> getDistinctDataViews(HttpServletRequest request,@RequestParam(required=false) String ruleGroupId,
			@RequestParam(required=false) String viewId,@RequestParam(required=false) String accRuleGroupId) throws URISyntaxException
	{
		log.info("To get all the Data views ruleGroupId:"+ruleGroupId+"viewId"+viewId);
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		
		List<HashMap<String,String>> hMapResultList=new ArrayList<HashMap<String,String>>();
		List<Long> dataViewIds = new ArrayList<Long>();
		HashMap<String,List<HashMap<String,String>>> returnMap= new HashMap<String,List<HashMap<String,String>>>();
		
		if(accRuleGroupId!= null && !accRuleGroupId.isEmpty() && accRuleGroupId != "")
		{
			log.info("accRuleGroupId is not null with "+ accRuleGroupId);
			List<BigInteger> dvs = new ArrayList<BigInteger>();
			dvs = ruleGroupRepository.fetchAccViewIdsByRuleGrpId(accRuleGroupId);
			log.info("dvs size is "+dvs.size());
			RuleGroup rg = new RuleGroup();
			rg = ruleGroupRepository.findByIdForDisplayAndTenantId(ruleGroupId, tenantId);
			for(BigInteger dv : dvs)
			{
				dataViewIds=rulesRepository.fetchDataViewIdsByRuleGrpAndViewIds(rg.getId(),Long.valueOf(dv.toString()));
				log.info("dataViewIds size is "+dataViewIds.size());
				DataViews accDv= new DataViews();
				accDv = dataViewsRepository.findOne(Long.valueOf(dv.toString()));
				List<HashMap<String,String>> returnList = buildMap(dataViewIds);
				if(returnList.size()>0)
				{
					returnMap.put(accDv.getIdForDisplay(), returnList);
				}
				else
				{
					log.info("return list is empty");
				}
			}

			return returnMap;

		}
		else
		{
			if((viewId==null || viewId == "" || viewId.isEmpty()) && ruleGroupId!=null)
			{
				log.info("view id is null and rulegroup id is not null");
				RuleGroup rg = new RuleGroup();
				rg = ruleGroupRepository.findByIdForDisplayAndTenantId(ruleGroupId, tenantId);
				dataViewIds=rulesRepository.fetchDataViewIdsByRuleGrpIds(rg.getId());
				log.info("dataViewIds size =="+dataViewIds.size());
				List<HashMap<String,String>> returnList = buildMap(dataViewIds);
				log.info("returnList size has== "+returnList.size());
				returnMap.put("All", returnList);
			}
			else if(viewId != null && !viewId.isEmpty()  && viewId != "" && ruleGroupId!=null)
			{
				log.info("viewId is not null and rulegroup id is also not null");
				DataViews dv= new DataViews();
				dv = dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId,viewId);
				RuleGroup rg = new RuleGroup();
				rg = ruleGroupRepository.findByIdForDisplayAndTenantId(ruleGroupId, tenantId);
				dataViewIds=rulesRepository.fetchDataViewIdsByRuleGrpAndViewIds(rg.getId(),dv.getId());
				log.info("dataViewIds size is "+dataViewIds.size());
				List<HashMap<String,String>> returnList = buildMap(dataViewIds);
				log.info("returnList size has "+returnList.size());
				returnMap.put(dv.getIdForDisplay(), returnList);
			}
			else
			{
				List<DataViews> dvList=new ArrayList<DataViews>();
				dvList=dataViewsRepository.findByTenantIdOrderByIdDesc(tenantId);
				log.info("dvList :"+dvList.size());
				if(dvList.size()>0)
				{
					for(DataViews dataView:dvList)
					{
						HashMap<String,String> hMapValue=new HashMap<String,String>();
						hMapValue.put("id", dataView.getIdForDisplay().toString());
						hMapValue.put("DataViewDispName", dataView.getDataViewDispName());
						hMapValue.put("DataViewName", dataView.getDataViewName());
						hMapResultList.add(hMapValue);
					}
					returnMap.put("All", hMapResultList);
				}
				return returnMap;
			}
		}
		//buildMap(dataViewIds,viewId);
		/*for(int k=0;k<dataViewIds.size();k++)
		{
			log.info("IDs :"+dataViewIds.get(k));
		}*/
		log.info("dataViewIds:"+dataViewIds);


		return returnMap;
	}
	
	List<HashMap<String,String>> buildMap(List<Long> dataViewIds)
	{
		log.info("build map with dataviews ids list"+dataViewIds.size());
		log.info("dataviews ids list is"+dataViewIds);
		List<HashMap<String,String>> hMapResultList=new ArrayList<HashMap<String,String>>();
		if(dataViewIds.size()>0)
		{
			List<DataViews> dvList=new ArrayList<DataViews>();
			dvList=dataViewsRepository.fetchByIds(dataViewIds);
			log.info("dvList :"+dvList.size());
			if(dvList.size()>0)
			{
				for(DataViews dataView:dvList)
				{
					HashMap<String,String> hMapValue=new HashMap<String,String>();
					hMapValue.put("id", dataView.getIdForDisplay().toString());
					hMapValue.put("DataViewDispName", dataView.getDataViewDispName());
					hMapValue.put("DataViewName", dataView.getDataViewName());
					hMapValue.put("itemName", dataView.getDataViewDispName());
					hMapResultList.add(hMapValue);
				}
				log.info("hMapResultList size here"+hMapResultList.size());
			}
			else
			{
				log.info("dvList size is 0");
			}
		}
		else{
			log.info("No data Views present");
			HashMap<String,String> hMapValue=new HashMap<String,String>();
			hMapValue.put("failed", "No Views Matched");
			hMapResultList.add(hMapValue);
		}
		log.info("hMapResultList size"+hMapResultList.size());
		return hMapResultList;
	}
	
	/**
	 * Author : Shobha
	 * @param ruleName
	 * @param ruleType
	 * @return
	 */
	@GetMapping("/ruleDuplicationCheck")
	@Timed
	public Boolean ruleDuplicationCheck(HttpServletRequest request,@RequestParam String ruleName,@RequestParam String rulePurpose)
	{
		log.info("Rest request to check duplicate rule -"+ruleName+" for type"+rulePurpose);
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		
		Object  ruleGroup = rulesRepository.findRuleWithNameAndTenantId(ruleName.trim(),tenantId,rulePurpose);
		log.info("ruleGroup is :"+ruleGroup);
		if(ruleGroup != null)
		{
			return true;
		}
		else
		{
			return false;
		}
		
	}
	
	
	
	/**
	 * author :ravali
	 * @param request
	 * @param coaId
	 * Desc : API to check whether the coa is tagged to any rule for a tenant
	 * @return
	 */
	@GetMapping("/checkWhetherCoaTaggedToGroup")
	@Timed
	public Boolean checkWhetherCoaTaggedToGroup(HttpServletRequest request,@RequestParam String coaId)
	{
		Boolean check = null;
		log.info("Rest request to check whether a coa is tagged to any rule(group) for coaId"+coaId);
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		ChartOfAccount coa=chartOfAccountRepository.findByIdForDisplayAndTenantId(coaId, tenantId);
		if(coa!=null && coa.getId()!=null)
		{
			List<Rules> rulesWithCoa=rulesRepository.findByTenantIdAndCOA(tenantId,coa.getId().toString());
			log.info("rulesWithCoa size :"+rulesWithCoa.size());
			if(rulesWithCoa.size() > 0)
			{
				check= true;
			}
			else
			{
				check= false;
			}
		}
		return check;
	}
	
	
}