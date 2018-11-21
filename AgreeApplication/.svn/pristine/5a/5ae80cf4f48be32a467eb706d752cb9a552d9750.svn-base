package com.nspl.app.service;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nspl.app.config.ApplicationContextProvider;
import com.nspl.app.domain.AccountingEvents;
import com.nspl.app.domain.AppModuleSummary;
import com.nspl.app.domain.DataViews;
import com.nspl.app.domain.DataViewsColumns;
import com.nspl.app.domain.FileTemplateLines;
import com.nspl.app.domain.LookUpCode;
import com.nspl.app.domain.ReconciliationResult;
import com.nspl.app.domain.RuleGroup;
import com.nspl.app.repository.AccountingEventsRepository;
import com.nspl.app.repository.AppModuleSummaryRepository;
import com.nspl.app.repository.AppRuleConditionsRepository;
import com.nspl.app.repository.BalanceTypeRepository;
import com.nspl.app.repository.DataChildRepository;
import com.nspl.app.repository.DataViewsColumnsRepository;
import com.nspl.app.repository.DataViewsRepository;
import com.nspl.app.repository.FileTemplateLinesRepository;
import com.nspl.app.repository.LookUpCodeRepository;
import com.nspl.app.repository.ReconciliationDuplicateResultRepository;
import com.nspl.app.repository.ReconciliationResultRepository;
import com.nspl.app.repository.RuleConditionsRepository;
import com.nspl.app.repository.RuleGroupDetailsRepository;
import com.nspl.app.repository.RuleGroupRepository;
import com.nspl.app.repository.RulesRepository;
import com.nspl.app.repository.TenantConfigRepository;
import com.nspl.app.repository.search.ReconciliationResultSearchRepository;
import com.nspl.app.web.rest.DataViewsResource;
/**
 * Service Implementation for managing ReconciliationResult.
 */
@Service
@Transactional
public class ReconciliationResultService {

	private final Logger log = LoggerFactory.getLogger(ReconciliationResultService.class);

	private final ReconciliationResultRepository reconciliationResultRepository;

	private final ReconciliationResultSearchRepository reconciliationResultSearchRepository;
	
	@Inject
	DataSource dataSource;
	
/*	@Inject
	JdbcTemplate masterJdbcTemplate;*/

	@Inject
	RuleGroupDetailsRepository ruleGroupDetailsRepository;

	@Inject
	RuleGroupRepository ruleGroupRepository;

	@Inject
	RulesRepository rulesRepository;

	@Inject
	DataViewsResource dataViewsResource;

	@Inject
	FileTemplateLinesRepository fileTemplateLinesRepository;

	@Inject
	DataViewsColumnsRepository dataViewsColumnsRepository;

	@Inject
	PropertiesUtilService propertiesUtilService;

	@Inject
	DataViewsRepository dataViewsRepository;

	@Inject
	RuleConditionsRepository ruleConditionsRepository;

	@Inject
	ReconciliationResultService reconciliationResultService;

	@Inject
	AccountingDataService accountingDataService;

	@Inject
	BalanceTypeService balanceTypeService;

	@Inject
	AppRuleConditionsRepository appRuleConditionsRepository;

	@Inject
	AccountingEventsRepository accountingEventsRepository;

	@Inject
	AppModuleSummaryRepository appModuleSummaryRepository;

	@Inject
	BalanceTypeRepository balanceTypeRepository;

	@Inject
	ReconciliationDuplicateResultRepository reconciliationDuplicateResultRepository;

	@Inject
	LookUpCodeRepository lookUpCodeRepository;

	@Inject
	TenantConfigRepository tenantConfigRepository;

	@Inject
	DataChildRepository dataChildRepository;

	@Inject
	FileService fileService;

	@Inject
	UserJdbcService userJdbcService;
	
/*	
	@Autowired
	private SessionFactory sessionFactory;*/

	@Inject
	private Environment env;

	@Inject
	DashBoardV4Service dashBoardV4Service;

	@PersistenceContext(unitName = "default")
	private EntityManager em;

	public ReconciliationResultService(
			ReconciliationResultRepository reconciliationResultRepository,
			ReconciliationResultSearchRepository reconciliationResultSearchRepository) {
		this.reconciliationResultRepository = reconciliationResultRepository;
		this.reconciliationResultSearchRepository = reconciliationResultSearchRepository;
	}

	/**
	 * Save a reconciliationResult.
	 *
	 * @param reconciliationResult
	 *            the entity to save
	 * @return the persisted entity
	 */
	public ReconciliationResult save(ReconciliationResult reconciliationResult) {
		log.debug("Request to save ReconciliationResult : {}",
				reconciliationResult);
		ReconciliationResult result = reconciliationResultRepository
				.save(reconciliationResult);
		reconciliationResultSearchRepository.save(result);
		return result;
	}

	/**
	 * Get all the reconciliationResults.
	 * 
	 * @param pageable
	 *            the pagination information
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<ReconciliationResult> findAll(Pageable pageable) {
		log.debug("Request to get all ReconciliationResults");
		Page<ReconciliationResult> result = reconciliationResultRepository
				.findAll(pageable);
		return result;
	}

	/**
	 * Get one reconciliationResult by id.
	 *
	 * @param id
	 *            the id of the entity
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public ReconciliationResult findOne(Long id) {
		log.debug("Request to get ReconciliationResult : {}", id);
		ReconciliationResult reconciliationResult = reconciliationResultRepository
				.findOne(id);
		return reconciliationResult;
	}

	/**
	 * Delete the reconciliationResult by id.
	 *
	 * @param id
	 *            the id of the entity
	 */
	public void delete(Long id) {
		log.debug("Request to delete ReconciliationResult : {}", id);
		reconciliationResultRepository.delete(id);
		reconciliationResultSearchRepository.delete(id);
	}

	/**
	 * Search for the reconciliationResult corresponding to the query.
	 *
	 * @param query
	 *            the query of the search
	 * @param pageable
	 *            the pagination information
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<ReconciliationResult> search(String query, Pageable pageable) {
		log.debug(
				"Request to search for a page of ReconciliationResults for query {}",
				query);
		Page<ReconciliationResult> result = reconciliationResultSearchRepository
				.search(queryStringQuery(query), pageable);
		return result;
	}

	/* Get Distinct DvIds based on RuleGroup */
	public HashMap<String, List<BigInteger>> getDistinctDVIdsforRuleGrp(Long ruleGrpId, Long tenantId) 
	{
		log.info("In Service for getting distinct source and target ids for grup id: "+ ruleGrpId + ", tenantId: " + tenantId);
		HashMap<String, List<BigInteger>> finalMap = new HashMap<String, List<BigInteger>>();
		List<BigInteger> distSrcIds = new ArrayList<BigInteger>();
		List<BigInteger> distTargetIds = new ArrayList<BigInteger>();
		List<Long> ruleIds = ruleGroupDetailsRepository.fetchByRuleGroupIdAndTenantId(ruleGrpId, tenantId);
		log.info("Rule Ids Size for Group Id " + ruleGrpId + ": "+ ruleIds.size() + ", " + ruleIds);
		if (ruleIds.size() > 0) 
		{
			distSrcIds = rulesRepository.fetchDistictSrcViewIdsByRuleId(ruleIds);
			distTargetIds = rulesRepository.fetchDistictTargetViewIdsByRuleId(ruleIds);
		}
		log.info("Distinct Source View Ids for GroupId " + ruleGrpId + " : "+ distSrcIds);
		log.info("Distinct Target View Ids for GroupId " + ruleGrpId + " : "+ distTargetIds);
		finalMap.put("sourceViewIds", distSrcIds);
		finalMap.put("targeViewIds", distTargetIds);
		return finalMap;
	}
	
	public HashMap getViewColumnDataType(Long dataViewId)
	{
		HashMap finalMap = new HashMap();
		List<DataViewsColumns> dvcs = dataViewsColumnsRepository.findByDataViewId(dataViewId);
		if(dvcs.size()>0)
		{
			for(DataViewsColumns dv : dvcs)
			{
				if("File Template".equalsIgnoreCase(dv.getRefDvType())) 
				{
					FileTemplateLines ftl = fileTemplateLinesRepository.findOne(Long.parseLong(dv.getRefDvColumn()));
					if(ftl != null)
					{
						finalMap.put(ftl.getColumnAlias(), dv.getColDataType());
					}
				}
				else if("Data View".equalsIgnoreCase(dv.getRefDvType()) || dv.getRefDvType() == null)
				{
					finalMap.put(dv.getColumnName(), dv.getColDataType());
				}
			}
		}
		return finalMap;
	}

	public String getQualifierColumnName(BigInteger dataViewId)
			throws ClassNotFoundException {
		log.info("In Service for getting qualifier column name for the view id: "
				+ dataViewId);
		DataViewsColumns dvc = dataViewsColumnsRepository
				.findByDataViewIdAndQualifier(dataViewId.longValue(), "AMOUNT");
		String qualifier = "";
		if (dvc != null) {
			qualifier = qualifier + dvc.getColumnName();
			log.info("Qualifier column name for data view id " + dataViewId
					+ " is: " + qualifier);
		} else {
			log.info("There is no Qualifier for the view id : " + dataViewId);
		}
		return qualifier;
	}

	public List<BigInteger> convertLongToBig(List<Long> longIds) {
		List<BigInteger> finalList = new ArrayList<BigInteger>();
		if (longIds.size() > 0) {
			for (Long id : longIds) {
				finalList.add(BigInteger.valueOf(id));
			}
		}
		return finalList;
	}




	public HashMap getColumnInfobyId(Long colId) 
	{
		HashMap finalMap = new HashMap();
		DataViewsColumns dc = dataViewsColumnsRepository.findOne(colId);
		if (dc != null) 
		{
			if("File Template".equalsIgnoreCase(dc.getRefDvType()))
			{
				FileTemplateLines ftl = fileTemplateLinesRepository.findOne(Long.parseLong(dc.getRefDvColumn().toString()));
				if (ftl != null) 
				{
					finalMap.put("columnAlias", ftl.getColumnAlias());
				}
			} 
			else if("Data View".equalsIgnoreCase(dc.getRefDvType()) || dc.getRefDvType() == null) 
			{
				finalMap.put("columnAlias", dc.getColumnName());
			}
		}
		log.info("finalMap: " + finalMap);
		return finalMap;
	}

	public HashMap getColumnInfobyIds(List<Long> colIds) 
	{
		HashMap finalMap = new HashMap();
		for(Long id : colIds) 
		{
			DataViewsColumns dc = dataViewsColumnsRepository.findOne(id);
			if (dc != null) 
			{
				if("File Template".equalsIgnoreCase(dc.getRefDvType())) 
				{
					FileTemplateLines ftl = fileTemplateLinesRepository.findOne(Long.parseLong(dc.getRefDvColumn().toString()));
					if(ftl != null) 
					{
						finalMap.put(ftl.getColumnAlias(), dc.getColumnName());
					}
				} 
				else if("Data View".equalsIgnoreCase(dc.getRefDvType()) || dc.getRefDvType() == null) 
				{
					finalMap.put(dc.getColumnName(), dc.getColumnName());
				}
			}
		}
		log.info("finalMap: " + finalMap);
		return finalMap;
	}


	public String getColumnNamesAsString(Long dataViewId) 
	{
		String columnString = "";
		List<DataViewsColumns> dvcs = dataViewsColumnsRepository.findByDataViewId(dataViewId);
		if(dvcs.size() > 0) 
		{
			for(DataViewsColumns dvc : dvcs) 
			{
				if("File Template".equalsIgnoreCase(dvc.getRefDvType())) 
				{
					FileTemplateLines ftl = fileTemplateLinesRepository.findOne(Long.parseLong(dvc.getRefDvColumn().toString()));
					if (ftl != null) 
					{
						columnString = columnString + ftl.getColumnAlias()+ ", ";
					}
				} 
				else if("Data View".equalsIgnoreCase(dvc.getRefDvType()) || dvc.getRefDvType() == null) 
				{
					columnString = columnString + dvc.getColumnName() + ", ";
				}
			}
			columnString = columnString.substring(0, columnString.length() - 2);
		}
		return columnString;
	}
	
	public LinkedHashMap getViewColumns(Long viewId, String sourceOrTarget)
	{
		LinkedHashMap finalMap = new LinkedHashMap();
		List<DataViewsColumns> dvcs = dataViewsColumnsRepository.findByDataViewId(viewId);
		if (dvcs.size() > 0) {
			if("source".equalsIgnoreCase(sourceOrTarget))
			{
				for(DataViewsColumns dvc : dvcs)
				{
					if("File Template".equalsIgnoreCase(dvc.getRefDvType())) {
						FileTemplateLines ftl = fileTemplateLinesRepository.findOne(Long.parseLong(dvc.getRefDvColumn().toString()));
						if (ftl != null) {
							finalMap.put(ftl.getColumnAlias(), "S_"+dvc.getColumnName());
						}
					}
					else if ("Data View".equalsIgnoreCase(dvc.getRefDvType()) || dvc.getRefDvType() == null) {
						finalMap.put(dvc.getColumnName(), "S_"+dvc.getColumnName());
					}
				}				
			}
			else if("target".equalsIgnoreCase(sourceOrTarget))
			{
				for(DataViewsColumns dvc : dvcs)
				{
					if("File Template".equalsIgnoreCase(dvc.getRefDvType())) {
						FileTemplateLines ftl = fileTemplateLinesRepository.findOne(Long.parseLong(dvc.getRefDvColumn().toString()));
						if (ftl != null) {
							finalMap.put(ftl.getColumnAlias(), "T_"+dvc.getColumnName());
						}
					}
					else if ("Data View".equalsIgnoreCase(dvc.getRefDvType()) || dvc.getRefDvType() == null) {
						finalMap.put(dvc.getColumnName(), "T_"+dvc.getColumnName());
					}
				}
			}
		}
		return finalMap;
	}
	
	public LinkedHashMap getViewColumnsForAcc(Long viewId)
	{
		LinkedHashMap finalMap = new LinkedHashMap();
		List<DataViewsColumns> dvcs = dataViewsColumnsRepository.findByDataViewId(viewId);
		if(dvcs.size()>0)
		{
			for(DataViewsColumns dvc : dvcs)
			{
				if("File Template".equalsIgnoreCase(dvc.getRefDvType()))
				{
					FileTemplateLines ftl = fileTemplateLinesRepository.findOne(Long.parseLong(dvc.getRefDvColumn().toString()));
					if (ftl != null) {
						finalMap.put(ftl.getColumnAlias(), dvc.getColumnName());
					}
				}
				else if ("Data View".equalsIgnoreCase(dvc.getRefDvType()) || dvc.getRefDvType() == null)
				{
					finalMap.put(dvc.getColumnName(), dvc.getColumnName());
				}
			}
		}
		return finalMap;
	}

	/* Getting data view column alias and names map in sequence order */
	public LinkedHashMap getColHeadersMapInSequence(Long viewId, Long groupId, Long tenantId, String sourceOrTarget) {
		log.info("In service for getting column headers in sequence");
		LinkedHashMap finalMap = new LinkedHashMap();
		// List<String> colNames = new ArrayList<String>();
		List<BigInteger> ruleIds = ruleGroupDetailsRepository.fetchRuleIdsByGroupAndTenantId(groupId, tenantId);
		log.info("Rule Ids " + ruleIds + " for the group id " + groupId);
		List<BigInteger> allViewColIds = dataViewsColumnsRepository.fetchIdsByDataViewId(viewId);
		log.info("All View Column Ids: " + allViewColIds);
		List<BigInteger> remainingSequence = new ArrayList<BigInteger>();
		if (ruleIds.size() > 0) {
			if ("source".equalsIgnoreCase(sourceOrTarget)) {
				log.info("In Source : " + viewId);
				List<BigInteger> ids = rulesRepository.fetchIdsBySourceViewIdAndIds(tenantId, viewId, ruleIds);
				log.info("Rule Ids " + ids + " tagged to source view id "+ viewId);
				if (ids.size() > 0) {
					List<BigInteger> sColumnIds = ruleConditionsRepository.fetchSourceColumnIdsByRuleIds(ids);
					sColumnIds.remove(null);
					log.info("Source Column Ids: " + sColumnIds+ " for Source View Id: " + viewId);
					finalMap.put("Recon Ref Id", "Recon Ref Id");
					if (sColumnIds.size() > 0) {
						for (BigInteger colId : sColumnIds) {
							DataViewsColumns dvc = dataViewsColumnsRepository.findOne(colId.longValue());
							if (dvc != null) {
								if ("File Template".equalsIgnoreCase(dvc.getRefDvType())) {
									FileTemplateLines ftl = fileTemplateLinesRepository.findOne(Long.parseLong(dvc.getRefDvColumn().toString()));
									if (ftl != null) {
										finalMap.put(ftl.getColumnAlias(),dvc.getColumnName());
									}
								} else if ("Data View".equalsIgnoreCase(dvc.getRefDvType()) || dvc.getRefDvType() == null) {
									finalMap.put(dvc.getColumnName(), dvc.getColumnName());
								}
							}
						}
					}
					finalMap.put("Rule_Name", "Rule_Name");
					finalMap.put("rowDescription", "rowDescription");
					finalMap.put("adjustmentType", "adjustmentType");
					finalMap.put("Status", "Status");
					if (allViewColIds.size() > 0) {
						for (BigInteger id : allViewColIds) {
							if (!sColumnIds.contains(id)) {
								remainingSequence.add(id);
							}
						}
					}
					log.info("Sourece View id: " + viewId + ", Remaining Column Ids: " + remainingSequence);
					if (remainingSequence.size() > 0) {
						for (BigInteger colId : remainingSequence) {
							DataViewsColumns dvc = dataViewsColumnsRepository.findOne(colId.longValue());
							if (dvc != null) {
								if ("File Template".equalsIgnoreCase(dvc.getRefDvType())) {
									FileTemplateLines ftl = fileTemplateLinesRepository.findOne(Long.parseLong(dvc.getRefDvColumn().toString()));
									if (ftl != null) {
										finalMap.put(ftl.getColumnAlias(),dvc.getColumnName());
									}
								} else if ("Data View".equalsIgnoreCase(dvc.getRefDvType()) || dvc.getRefDvType() == null) {
									finalMap.put(dvc.getColumnName(), dvc.getColumnName());
								}
							}
						}
					}
				}
			} else if ("target".equalsIgnoreCase(sourceOrTarget)) {
				log.info("In Target : " + viewId);
				List<BigInteger> ids = rulesRepository.fetchIdsByTargetViewIdAndIds(tenantId, viewId, ruleIds);
				log.info("Rule Ids " + ids + " which are tagged to target view id : " + ids);
				if (ids.size() > 0) {
					List<BigInteger> tColumnIds = ruleConditionsRepository.fetchTargetColumnIdsByRuleIds(ids);
					tColumnIds.remove(null);
					log.info("Target view id " + viewId + "Target Column Ids: " + tColumnIds);
					finalMap.put("Recon Ref Id", "Recon Ref Id");
					if (tColumnIds.size() > 0) {
						for (BigInteger colId : tColumnIds) {
							DataViewsColumns dvc = dataViewsColumnsRepository.findOne(colId.longValue());
							if (dvc != null) {
								if ("File Template".equalsIgnoreCase(dvc.getRefDvType())) {
									FileTemplateLines ftl = fileTemplateLinesRepository.findOne(Long.parseLong(dvc.getRefDvColumn().toString()));
									if (ftl != null) {
										finalMap.put(ftl.getColumnAlias(),dvc.getColumnName());
									}
								} else if ("Data View".equalsIgnoreCase(dvc.getRefDvType()) || dvc.getRefDvType() == null) {
									finalMap.put(dvc.getColumnName(),dvc.getColumnName());
								}
							}
						}
					}
					finalMap.put("Rule_Name", "Rule_Name");
					finalMap.put("rowDescription", "rowDescription");
					finalMap.put("adjustmentType", "adjustmentType");
					finalMap.put("Status", "Status");
					if (allViewColIds.size() > 0) {
						for (BigInteger id : allViewColIds) {
							if (!tColumnIds.contains(id)) {
								remainingSequence.add(id);
							}
						}
					}
					log.info("View Id : " + viewId + ", Remaining Column Ids " + remainingSequence);
					if (remainingSequence.size() > 0) {
						for (BigInteger colId : remainingSequence) {
							DataViewsColumns dvc = dataViewsColumnsRepository.findOne(colId.longValue());
							if (dvc != null) {
								if ("File Template".equalsIgnoreCase(dvc
										.getRefDvType())) {
									FileTemplateLines ftl = fileTemplateLinesRepository
											.findOne(Long.parseLong(dvc
													.getRefDvColumn()
													.toString()));
									if (ftl != null) {
										finalMap.put(ftl.getColumnAlias(),
												dvc.getColumnName());
									}
								} else if ("Data View".equalsIgnoreCase(dvc
										.getRefDvType())
										|| dvc.getRefDvType() == null) {
									finalMap.put(dvc.getColumnName(),
											dvc.getColumnName());
								}
							}
						}
					}
				}
			}
		} else {
			log.info("No Rule Ids found for the group id: " + groupId);
		}
		return finalMap;
	}

	public List<HashMap> getRecColsAlignInfo(Long viewId, Long groupId, Long tenantId, String sourceOrTarget, String status, String type, Long ruleId, String groupBy) {
		log.info("In service for getting column headers in sequence");
		List<HashMap> finalMap = new ArrayList<HashMap>();
		List<BigInteger> ruleIds = ruleGroupDetailsRepository.fetchRuleIdsByGroupAndTenantId(groupId, tenantId);
		log.info("Rule Ids " + ruleIds + " for the group id " + groupId);
		List<BigInteger> allViewColIds = dataViewsColumnsRepository.fetchIdsByDataViewId(viewId);
		log.info("All View Column Ids: " + allViewColIds);
		List<BigInteger> remainingSequence = new ArrayList<BigInteger>();
		if (ruleIds.size() > 0) 
		{
			if (type.toLowerCase().contains("summary")) 
			{
				if ("reconciled".equalsIgnoreCase(status)) {
					HashMap reconRefMp = new HashMap();
					reconRefMp.put("field", "reconReference");
					reconRefMp.put("header", "Recon Reference");
					reconRefMp.put("columnName", "recon_reference");
					reconRefMp.put("align", "left");
					reconRefMp.put("width", "150px");
					reconRefMp.put("colId", "recon_reference");
					reconRefMp.put("dataType", "STRING");
					finalMap.add(reconRefMp);
					if ("rules".equalsIgnoreCase(groupBy)) {
						HashMap ruleMp = new HashMap();
						ruleMp.put("field", "groupByColumn");
						ruleMp.put("header", "Rule Name");
						ruleMp.put("columnName", "rule_code");
						ruleMp.put("align", "left");
						ruleMp.put("width", "150px");
						ruleMp.put("colId", "rule_code");
						ruleMp.put("dataType", "STRING");
						finalMap.add(ruleMp);
					} else if ("batch".equalsIgnoreCase(groupBy)) {
						HashMap batchMp = new HashMap();
						batchMp.put("field", "groupByColumn");
						batchMp.put("header", "Batch");
						batchMp.put("columnName", "recon_job_reference");
						batchMp.put("align", "left");
						batchMp.put("width", "150px");
						batchMp.put("colId", "recon_job_reference");
						batchMp.put("dataTygetColAlignInfope", "STRING");
						finalMap.add(batchMp);
					} else if ("approvalRule".equalsIgnoreCase(groupBy)) {
						HashMap apprRuleMp = new HashMap();
						apprRuleMp.put("field", "groupByColumn");
						apprRuleMp.put("header", "Approval Rule");
						apprRuleMp.put("columnName", "rule_code");
						apprRuleMp.put("align", "left");
						apprRuleMp.put("width", "150px");
						apprRuleMp.put("colId", "rule_code");
						apprRuleMp.put("dataType", "STRING");
						finalMap.add(apprRuleMp);
					} else if ("approvalDate".equalsIgnoreCase(groupBy)) {
						HashMap apprDateMp = new HashMap();
						apprDateMp.put("field", "groupByColumn");
						apprDateMp.put("header", "Approval Date");
						apprDateMp.put("columnName", "final_action_date");
						apprDateMp.put("align", "left");
						apprDateMp.put("width", "150px");
						apprDateMp.put("colId", "final_action_date");
						apprDateMp.put("dataType", "DATE");
						finalMap.add(apprDateMp);
					} else if ("approvalStatus".equalsIgnoreCase(groupBy)) {
						HashMap apprStatusMp = new HashMap();
						apprStatusMp.put("field", "groupByColumn");
						apprStatusMp.put("header", "Approval Status");
						apprStatusMp.put("columnName", "final_status");
						apprStatusMp.put("align", "left");
						apprStatusMp.put("width", "150px");
						apprStatusMp.put("colId", "final_status");
						apprStatusMp.put("dataType", "STRING");
						finalMap.add(apprStatusMp);
					} else {
						DataViewsColumns dvc = dataViewsColumnsRepository.findByDataViewIdAndQualifier(viewId,"CURRENCYCODE");
						if (dvc != null) {
							HashMap mp = getColNameNType(dvc.getId());
							HashMap colMp = new HashMap();
							colMp.put("field", "groupByColumn");
							colMp.put("header", mp.get("displayColName").toString());
							colMp.put("columnName", mp.get("columnName").toString());
							colMp.put("align", "left");
							colMp.put("width", "150px");
							colMp.put("colId", mp.get("columnName").toString());
							colMp.put("dataType", "STRING");
							finalMap.add(colMp);
						}
					}
					HashMap countRefMp = new HashMap();
					countRefMp.put("field", "count");
					countRefMp.put("header", "Transaction Count");
					countRefMp.put("align", "center");
					countRefMp.put("width", "150px");
					countRefMp.put("colId", "");
					countRefMp.put("dataType", "STRING");
					finalMap.add(countRefMp);
					HashMap amountRefMp = new HashMap();
					amountRefMp.put("field", "amount");
					amountRefMp.put("header", "Reconciled Amount");
					amountRefMp.put("align", "right");
					amountRefMp.put("width", "150px");
					amountRefMp.put("colId", "");
					amountRefMp.put("dataType", "DECIMAL");
					finalMap.add(amountRefMp);
					HashMap varianceRefMp = new HashMap();
					varianceRefMp.put("field", "varianceAmount");
					varianceRefMp.put("header", "Variance Amount");
					varianceRefMp.put("align", "right");
					varianceRefMp.put("width", "150px");
					varianceRefMp.put("colId", "");
					varianceRefMp.put("dataType", "DECIMAL");
					finalMap.add(varianceRefMp);
				} else {
					if ("source".equalsIgnoreCase(sourceOrTarget)) {
						HashMap transIdRefMp = new HashMap();
						transIdRefMp.put("field", "dataRowId");
						transIdRefMp.put("header", "Transaction Id");
						transIdRefMp.put("columnName", "original_row_id");
						transIdRefMp.put("align", "left");
						transIdRefMp.put("width", "150px");
						transIdRefMp.put("colId", "original_row_id");
						transIdRefMp.put("dataType", "STRING");
						finalMap.add(transIdRefMp);

					} else {
						HashMap transIdRefMp = new HashMap();
						transIdRefMp.put("field", "dataRowId");
						transIdRefMp.put("header", "Transaction Id");
						transIdRefMp.put("columnName", "target_row_id");
						transIdRefMp.put("align", "left");
						transIdRefMp.put("width", "150px");
						transIdRefMp.put("colId", "target_row_id");
						transIdRefMp.put("dataType", "STRING");
						finalMap.add(transIdRefMp);
					}
					if ("suggestion".equalsIgnoreCase(status)) {
						if (allViewColIds.size() > 0) {
							finalMap.addAll(getColAlignInfo(allViewColIds));
						}

					} else {
						if (allViewColIds.size() > 0) {
							finalMap.addAll(getColAlignInfo(allViewColIds));
						}
					}
					HashMap rowDescRefMp = new HashMap();
					rowDescRefMp.put("field", "rowDescription");
					rowDescRefMp.put("header", "Row Description");
					rowDescRefMp.put("columnName", "rowDescription");
					rowDescRefMp.put("align", "right");
					rowDescRefMp.put("width", "150px");
					rowDescRefMp.put("colId", "rowDescription");
					rowDescRefMp.put("dataType", "STRING");
					finalMap.add(rowDescRefMp);
					HashMap adjTypeRefMp = new HashMap();
					adjTypeRefMp.put("field", "adjustmentType");
					adjTypeRefMp.put("header", "Adjustment Type");
					adjTypeRefMp.put("columnName", "adjustmentType");
					adjTypeRefMp.put("align", "right");
					adjTypeRefMp.put("width", "150px");
					adjTypeRefMp.put("colId", "adjustmentType");
					adjTypeRefMp.put("dataType", "STRING");
					finalMap.add(adjTypeRefMp);
				}
			} 
			else 
			{
				if (ruleId != null) 
				{
					if ("source".equalsIgnoreCase(sourceOrTarget)) {
						if ("reconciled".equalsIgnoreCase(status)) {
							List<BigInteger> sColumnIds = ruleConditionsRepository.fetchSourceColumnIdsByRuleId(ruleId);
							log.info("Source Column Ids: " + sColumnIds+ " for Source View Id: " + viewId);
							if (sColumnIds.size() > 0) {
								finalMap.addAll(getColAlignInfo(sColumnIds));
							}
							if (allViewColIds.size() > 0) {
								for (BigInteger id : allViewColIds) {
									if (!sColumnIds.contains(id)) {
										remainingSequence.add(id);
									}
								}
							}
							if (remainingSequence.size() > 0) {
								finalMap.addAll(getColAlignInfo(remainingSequence));
							}
							HashMap rowDescRefMp = new HashMap();
							rowDescRefMp.put("field", "rowDescription");
							rowDescRefMp.put("header", "Row Description");
							rowDescRefMp.put("columnName", "rowDescription");
							rowDescRefMp.put("align", "left");
							rowDescRefMp.put("width", "150px");
							rowDescRefMp.put("colId", "rowDescription");
							rowDescRefMp.put("dataType", "STRING");
							finalMap.add(rowDescRefMp);
							HashMap adjTypeRefMp = new HashMap();
							adjTypeRefMp.put("field", "adjustmentType");
							adjTypeRefMp.put("header", "Adjustment Type");
							adjTypeRefMp.put("columnName", "adjustmentType");
							adjTypeRefMp.put("align", "left");
							adjTypeRefMp.put("width", "150px");
							adjTypeRefMp.put("colId", "adjustmentType");
							adjTypeRefMp.put("dataType", "STRING");
							finalMap.add(adjTypeRefMp);
						}
					} else {
						if ("reconciled".equalsIgnoreCase(status)) {
							List<BigInteger> tColumnIds = ruleConditionsRepository.fetchTargetColumnIdsByRuleId(ruleId);
							log.info("Source Column Ids: " + tColumnIds+ " for Source View Id: " + viewId);
							if (tColumnIds.size() > 0) {
								finalMap.addAll(getColAlignInfo(tColumnIds));
							}
							if (allViewColIds.size() > 0) {
								for (BigInteger id : allViewColIds) {
									if (!tColumnIds.contains(id)) {
										remainingSequence.add(id);
									}
								}
							}
							if (remainingSequence.size() > 0) {
								finalMap.addAll(getColAlignInfo(remainingSequence));
							}

							HashMap rowDescRefMp = new HashMap();
							rowDescRefMp.put("field", "rowDescription");
							rowDescRefMp.put("header", "Row Description");
							rowDescRefMp.put("columnName", "rowDescription");
							rowDescRefMp.put("align", "left");
							rowDescRefMp.put("width", "150px");
							rowDescRefMp.put("colId", "");
							rowDescRefMp.put("dataType", "STRING");
							finalMap.add(rowDescRefMp);
							HashMap adjTypeRefMp = new HashMap();
							adjTypeRefMp.put("field", "adjustmentType");
							adjTypeRefMp.put("header", "Adjustment Type");
							adjTypeRefMp.put("columnName", "Adjustment Type");
							adjTypeRefMp.put("align", "left");
							adjTypeRefMp.put("width", "150px");
							adjTypeRefMp.put("colId", "");
							adjTypeRefMp.put("dataType", "STRING");
							finalMap.add(adjTypeRefMp);
						}
					}
				}

			}
		} else {
			log.info("No Rule Ids found for the group id: " + groupId);
		}
		return finalMap;
	}

	public List<HashMap> getColAlignInfo(List<BigInteger> columnIds) {
		columnIds.remove(null);
		List<HashMap> finalMap = new ArrayList<HashMap>();
		for (BigInteger id : columnIds) {
			HashMap hm = new HashMap();
			DataViewsColumns dvc = dataViewsColumnsRepository.findOne(id.longValue());
			if (dvc != null) {
				String columnName = "";
				if("File Template".equalsIgnoreCase(dvc.getRefDvType())) {
					FileTemplateLines ftl = fileTemplateLinesRepository.findOne(Long.parseLong(dvc.getRefDvColumn()));
					if (ftl != null) {
						columnName = ftl.getColumnAlias();
					}
				} else if ("Data View".equalsIgnoreCase(dvc.getRefDvType())
						|| dvc.getRefDvType() == null) {
					columnName = dvc.getColumnName();
				}
				/* hm.put("field", dvc.getColumnName()); */
				hm.put("field", columnName);
				hm.put("header", dvc.getColumnName());
				if ("DECIMAL".equalsIgnoreCase(dvc.getColDataType())) {
					hm.put("align", "right");
				} else {
					hm.put("align", "left");
				}
				hm.put("columnName", columnName);
				hm.put("width", "150px");
				hm.put("colId", dvc.getId());
				hm.put("dataType", dvc.getColDataType());
				finalMap.add(hm);
			}
		}
		return finalMap;
	}

	public List<HashMap> getColAlignInfoForAcc(List<BigInteger> columnIds) {
		List<HashMap> finalMap = new ArrayList<HashMap>();
		for (BigInteger id : columnIds) {
			HashMap hm = new HashMap();
			DataViewsColumns dvc = dataViewsColumnsRepository.findOne(id
					.longValue());
			if (dvc != null) {
				String columnName = "";
				if ("File Template".equalsIgnoreCase(dvc.getRefDvType())) {
					FileTemplateLines ftl = fileTemplateLinesRepository
							.findOne(Long.parseLong(dvc.getRefDvColumn()));
					if (ftl != null) {
						columnName = ftl.getColumnAlias();
					}
				} else if ("Data View".equalsIgnoreCase(dvc.getRefDvType())
						|| dvc.getRefDvType() == null) {
					columnName = dvc.getColumnName();
				}
				hm.put("field", dvc.getColumnName());
				hm.put("header", dvc.getColumnName());
				if ("DECIMAL".equalsIgnoreCase(dvc.getColDataType())) {
					hm.put("align", "right");
				} else {
					hm.put("align", "left");
				}
				hm.put("columnName", columnName);
				hm.put("width", "150px");
				hm.put("colId", dvc.getId());
				hm.put("dataType", dvc.getColDataType());
				finalMap.add(hm);
			}
		}
		return finalMap;
	}


	public List<String> getViewColumnHeadersInSequenceForApproval(Long viewId,
			Long groupId, Long tenantId, String sourceOrTarget) {
		log.info("In service for getting column headers in sequence");
		List<String> colNames = new ArrayList<String>();
		List<BigInteger> ruleIds = ruleGroupDetailsRepository
				.fetchRuleIdsByGroupAndTenantId(groupId, tenantId);
		log.info("Rule Ids " + ruleIds + " for the group id " + groupId);
		if (ruleIds.size() > 0) {
			if ("source".equalsIgnoreCase(sourceOrTarget)) {
				log.info("In Source : " + viewId);
				List<BigInteger> ids = rulesRepository
						.fetchIdsBySourceViewIdAndIds(tenantId, viewId, ruleIds);
				log.info("Rule Ids " + ids + " tagged to source view id"
						+ viewId);
				if (ids.size() > 0) {
					List<BigInteger> sColumnIds = ruleConditionsRepository
							.fetchSourceColumnIdsByRuleIds(ids);
					sColumnIds.remove(null);
					log.info("Source Column Ids: " + sColumnIds
							+ " for Source View Id: " + viewId);
					if (sColumnIds.size() > 0) {
						List<String> dvc = dataViewsColumnsRepository
								.fetchFileTemplateLineIdsByIds(sColumnIds);
						List<String> seqColumns = new ArrayList<String>();
						if (dvc.size() > 0) {
							List<Long> idsAsLong = new ArrayList<Long>();
							for (String idString : dvc) {
								idsAsLong.add(Long.parseLong(idString));
							}
							seqColumns = fileTemplateLinesRepository
									.fetchColumnAliasByIds(idsAsLong);
							log.info("Sequence Columns " + seqColumns);
							colNames.addAll(seqColumns);
						} else {
							seqColumns = dataViewsColumnsRepository
									.findColumnName(sColumnIds);
							log.info("seqColumns :" + seqColumns);
							colNames.addAll(seqColumns);
						}
					}
				}
			} else if ("target".equalsIgnoreCase(sourceOrTarget)) {
				log.info("In Target : " + viewId);
				List<BigInteger> ids = rulesRepository
						.fetchIdsByTargetViewIdAndIds(tenantId, viewId, ruleIds);
				log.info("Rule Ids " + ids
						+ " which are tagged to target view id : " + ids);
				if (ids.size() > 0) {
					List<BigInteger> tColumnIds = ruleConditionsRepository
							.fetchTargetColumnIdsByRuleIds(ids);
					tColumnIds.remove(null);
					log.info("Target view id " + viewId + "Target Column Ids: "
							+ tColumnIds);
					if (tColumnIds.size() > 0) {
						List<String> dvc = dataViewsColumnsRepository
								.fetchFileTemplateLineIdsByIds(tColumnIds);
						List<String> seqColumns = new ArrayList<String>();
						if (dvc.size() > 0) {
							List<Long> idsAsLong = new ArrayList<Long>();
							for (String idString : dvc) {
								idsAsLong.add(Long.parseLong(idString));
							}
							seqColumns = fileTemplateLinesRepository
									.fetchColumnAliasByIds(idsAsLong);
							log.info("Target View Id" + viewId
									+ "Sequence Columns " + seqColumns);
							colNames.addAll(seqColumns);
						} else {
							seqColumns = dataViewsColumnsRepository
									.findColumnName(tColumnIds);
							log.info("seqColumns :" + seqColumns);
							colNames.addAll(seqColumns);
						}
					}
				}
			}
		} else {
			log.info("No Rule Ids found for the group id: " + groupId);
		}
		return colNames;
	}

	/**
	 * author ravali
	 * 
	 * @param columnIds
	 * @param headerList
	 * @return
	 */
	public List<HashMap> getColAlignInfoForApproval(List<BigInteger> columnIds,
			List<String> headerList) {
		List<HashMap> finalMap = new ArrayList<HashMap>();
		for (BigInteger id : columnIds) {
			HashMap hm = new HashMap();
			DataViewsColumns dvc = dataViewsColumnsRepository.findOne(id
					.longValue());
			if (dvc != null) {
				for (String header : headerList) {
					log.info("header :"
							+ header.replaceAll("[0-9]", "")
									.replaceAll("_", "").split("(?=[A-Z])")[0]);
					log.info("dvc.getColumnName() :"
							+ dvc.getColumnName().split(" ")[0]);
					if (dvc.getColumnName().split(" ")[0]
							.equalsIgnoreCase(header.replaceAll("[0-9]", "")
									.replaceAll("_", "").split("(?=[A-Z])")[0])) {
						log.info("in if");
						hm.put("field", header.replaceAll("[0-9]", "")
								.replaceAll("_", ""));
						hm.put("header", dvc.getColumnName());
						if ("DECIMAL".equalsIgnoreCase(dvc.getColDataType())) {
							hm.put("align", "right");
						} else {
							hm.put("align", "left");
						}
						hm.put("width", "150px");
						finalMap.add(hm);
					}
				}
			}
		}
		return finalMap;
	}

	/**
	 * ravali
	 * 
	 * @param viewId
	 * @param groupId
	 * @param tenantId
	 * @param sourceOrTarget
	 * @param headerList
	 * @return
	 */
	public List<HashMap> getAppActOrRecColsAlignInfo(Long viewId, Long groupId,
			Long tenantId, String sourceOrTarget, List<String> headerList) {
		log.info("In service for getting column headers in sequence");
		List<HashMap> finalMap = new ArrayList<HashMap>();
		List<BigInteger> ruleIds = ruleGroupDetailsRepository
				.fetchRuleIdsByGroupAndTenantId(groupId, tenantId);
		log.info("Rule Ids " + ruleIds + " for the group id " + groupId);
		List<BigInteger> allViewColIds = dataViewsColumnsRepository
				.fetchIdsByDataViewId(viewId);
		log.info("All View Column Ids: " + allViewColIds);
		List<String> sysColumns = new ArrayList<String>();
		sysColumns.add("Status");
		List<BigInteger> remainingSequence = new ArrayList<BigInteger>();
		if (ruleIds.size() > 0) {
			if ("source".equalsIgnoreCase(sourceOrTarget)) {

				log.info("In Source : " + viewId);
				List<BigInteger> ids = rulesRepository
						.fetchIdsBySourceViewIdAndIds(tenantId, viewId, ruleIds);
				log.info("Rule Ids " + ids + " tagged to source view id"
						+ viewId);
				if (ids.size() > 0) {
					List<BigInteger> sColumnIds = ruleConditionsRepository
							.fetchSourceColumnIdsByRuleIds(ids);
					log.info("Source Column Ids: " + sColumnIds
							+ " for Source View Id: " + viewId);
					if (sColumnIds.size() > 0) {
						finalMap.addAll(reconciliationResultService
								.getColAlignInfoApp(sColumnIds));
					}
				}
			} else if ("target".equalsIgnoreCase(sourceOrTarget)) {

				log.info("In Target : " + viewId);
				List<BigInteger> ids = rulesRepository
						.fetchIdsByTargetViewIdAndIds(tenantId, viewId, ruleIds);
				log.info("Rule Ids " + ids
						+ " which are tagged to target view id : " + ids);
				if (ids.size() > 0) {
					List<BigInteger> tColumnIds = ruleConditionsRepository
							.fetchTargetColumnIdsByRuleIds(ids);
					log.info("Target view id " + viewId + "Target Column Ids: "
							+ tColumnIds);
					if (tColumnIds.size() > 0) {
						finalMap.addAll(reconciliationResultService
								.getColAlignInfoApp(tColumnIds));
					}

				}
			}
		}
		return finalMap;
	}

	/**
	 * ravali setting alias name from file templates
	 * 
	 * @param viewId
	 * @param groupId
	 * @param tenantId
	 * @param sourceOrTarget
	 * @return
	 */
	public List<HashMap> getColAlignInfoApp(List<BigInteger> columnIds) {
		List<HashMap> finalMap = new ArrayList<HashMap>();
		for (BigInteger id : columnIds) {
			HashMap hm = new HashMap();
			DataViewsColumns dvc = dataViewsColumnsRepository.findOne(id
					.longValue());
			if (dvc != null) {
				if (dvc.getRefDvColumn() != null) {
					FileTemplateLines ftl = fileTemplateLinesRepository
							.findOne(Long.parseLong(dvc.getRefDvColumn()
									.toString()));
					if (ftl != null)
						hm.put("field", ftl.getColumnAlias());
				} else
					hm.put("field", dvc.getColumnName());
				hm.put("header", dvc.getColumnName());
				if ("DECIMAL".equalsIgnoreCase(dvc.getColDataType())) {
					hm.put("align", "right");
				} else {
					hm.put("align", "left");
				}
				hm.put("width", "150px");
				finalMap.add(hm);
			}
		}
		return finalMap;
	}

	public String getViewColumnQualifier(BigInteger dataViewId, String qualifierCode) 
	{
		DataViewsColumns dvc = dataViewsColumnsRepository.findByDataViewIdAndQualifier(dataViewId.longValue(), qualifierCode);
		String qualifier = "";
		if(dvc != null) 
		{
			if("File Template".equalsIgnoreCase(dvc.getRefDvType())) 
			{
				FileTemplateLines ftl = fileTemplateLinesRepository.findOne(Long.parseLong(dvc.getRefDvColumn()));
				if(ftl != null) 
				{
					qualifier = ftl.getColumnAlias();
				}
			} 
			else if("Data View".equalsIgnoreCase(dvc.getRefDvType()) || dvc.getRefDvType() == null) 
			{
				qualifier = dvc.getColumnName();
			}
		} 
		else 
		{
			log.info("There is no Transdate Qualifier for the view id : " + dataViewId);
		}
		return qualifier;
	}

	public HashMap getCountAndAmountRecordWise(Long viewId, List<Long> rowIds, String amountQualifier) throws SQLException 
	{
		log.info("Fetching count and amounts based on view and rowids...");
		HashMap finalMap = new HashMap();
		String idsString = rowIds.toString();
		idsString = idsString.replace("[", "");
		idsString = idsString.replace("]", "");
		Connection conn = null;
		Statement stmt = null;
		ResultSet result = null;

		Properties props = propertiesUtilService.getPropertiesFromClasspath("File.properties");
		String currencyFormat = props.getProperty("currencyFormat");

		try {
			DataSource ds = (DataSource) ApplicationContextProvider.getApplicationContext().getBean("dataSource");
			conn = ds.getConnection();
			stmt = conn.createStatement();
			DataViews dv = dataViewsRepository.findOne(viewId.longValue());
			String amount = "";
			if(dv != null) 
			{
				String viewName = dv.getDataViewName();
				log.info("Query: select sum(`" + amountQualifier + "`) from `"+ viewName.toLowerCase() + "` where scrIds in ("+ idsString + ")");
				result = stmt.executeQuery("select sum(`" + amountQualifier
						+ "`) from `" + viewName.toLowerCase()
						+ "` where scrIds in (" + idsString + ")");
				while(result.next()) 
				{
					amount = amount + result.getString(1);
				}
			}
			else 
			{
				log.info("View doen't not exist for the view id: " + viewId);
			}
			Double amountDouble = Double.parseDouble(amount);
			finalMap.put("amount", amountDouble);
			finalMap.put("amountQualifier", amountQualifier);
			finalMap.put("count", rowIds.size());
		} catch (SQLException se) {
			log.info("Error while executing query: " + se);
		} catch (Exception e) {
			log.info("Exception while getting databse properties");
		} finally {
			if (result != null)
				result.close();
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();
		}
		return finalMap;
	}

	public String getDataViewColumnName(Long columnId) {
		log.info("Fetching columnName based on column id: " + columnId);
		String columnName = "";
		DataViewsColumns dvc = dataViewsColumnsRepository.findOne(columnId);
		if (dvc != null) {
			if ("File Template".equalsIgnoreCase(dvc.getRefDvType())) {
				FileTemplateLines ftl = fileTemplateLinesRepository
						.findOne(Long
								.parseLong(dvc.getRefDvColumn().toString()));
				if (ftl != null) {
					columnName = columnName + ftl.getColumnAlias();
				}
			} else if ("Data View".equalsIgnoreCase(dvc.getRefDvType())
					|| dvc.getRefDvType() == null) {
				columnName = columnName + dvc.getColumnName();
			}
		}
		return columnName;
	}

	public String getCurrencySymbol(String currencyCode) throws ParseException {
		String symbol = "";
		String jsonString = "[ {" + "   \"symbol\":\"$\","
				+ " \"currency\":\"USD\" " + " }," + " { "
				+ "   \"symbol\":\"$\"," + " \"currency\":\"US\" " + " },"
				+ "{" + " \"symbol\":\"CA$\"," + " \"currency\":\"CAD\" "
				+ " }," + " { " + "   \"symbol\":\"\u20AC\", "
				+ "   \"currency\":\"EUR\" " + "}," + " { "
				+ "   \"symbol\":\"AED\", " + "   \"currency\":\"AED\" "
				+ "  }," + "  { " + "   \"symbol\":\"Af\", "
				+ "    \"currency\":\"AFN\"" + "  }," + "  {"
				+ "    \"symbol\":\"ALL\"," + "   \"currency\":\"ALL\""
				+ "  }," + "  {" + "    \"symbol\":\"AMD\","
				+ "    \"currency\":\"AMD\"" + "  }," + "  {"
				+ "     \"symbol\":\"AR$\"," + " \"currency\":\"ARS\"" + " },"
				+ " {" + "  \"symbol\":\"AU$\"," + "  \"currency\":\"AUD\""
				+ " }," + " { " + "  \"symbol\":\"man.\", "
				+ "   \"currency\":\"AZN\"" + "}," + "{"
				+ "   \"symbol\":\"KM\"," + "   \"currency\":\"BAM\"" + " },"
				+ "{" + " \"symbol\":\"Tk\"," + "\"currency\":\"BDT\"" + " },"
				+ " {" + "\"symbol\":\"BGN\"," + "\"currency\":\"BGN\"" + " },"
				+ "{" + "  \"symbol\":\"BD\"," + "   \"currency\":\"BHD\""
				+ "  }," + "  {" + "    \"symbol\":\"FBu\","
				+ "    \"currency\":\"BIF\"" + " }," + " {"
				+ "    \"symbol\":\"BN$\"," + "   \"currency\":\"BND\"" + " },"
				+ " {" + "     \"symbol\":\"Bs\"," + "  \"currency\":\"BOB\""
				+ " }," + "  {" + "   \"symbol\":\"R$\","
				+ "    \"currency\":\"BRL\"" + "   }," + "  {"
				+ "    \"symbol\":\"BWP\"," + "    \"currency\":\"BWP\""
				+ "  }," + "  {" + "     \"symbol\":\"BYR\","
				+ "    \"currency\":\"BYR\"" + " }," + " {"
				+ "     \"symbol\":\"BZ$\"," + "    \"currency\":\"BZD\""
				+ " }," + "  {" + "     \"symbol\":\"CDF\","
				+ "     \"currency\":\"CDF\"" + " }," + "  {"
				+ "     \"symbol\":\"CHF\"," + "     \"currency\":\"CHF\""
				+ "  }," + "  {" + "    \"symbol\":\"CL$\","
				+ "    \"currency\":\"CLP\"" + "  }," + "  {"
				+ "    \"symbol\":\"CN¥\"," + "    \"currency\":\"CNY\""
				+ "  }," + " {" + "   \"symbol\":\"CO$\","
				+ "   \"currency\":\"COP\"" + " }," + " {"
				+ "    \"symbol\":\"\u20A1\"," + "     \"currency\":\"CRC\""
				+ "  }," + "  {" + "     \"symbol\":\"CV$\","
				+ "    \"currency\":\"CVE\"" + " }," + "  {"
				+ "    \"symbol\":\"Kč\"," + "    \"currency\":\"CZK\""
				+ "  }," + "  {" + "     \"symbol\":\"Fdj\","
				+ "    \"currency\":\"DJF\"" + "  }," + "  {"
				+ "   \"symbol\":\"Dkr\"," + "   \"currency\":\"DKK\"" + "  },"
				+ "  {" + "    \"symbol\":\"RD$\","
				+ "    \"currency\":\"DOP\"" + " }," + "  {"
				+ "    \"symbol\":\"DA\"," + "    \"currency\":\"DZD\"" + " },"
				+ " {" + "    \"symbol\":\"Ekr\"," + "   \"currency\":\"EEK\""
				+ " }," + "  {" + "     \"symbol\":\"EGP\","
				+ "    \"currency\":\"EGP\"" + "  }," + "  {"
				+ "    \"symbol\":\"Nfk\"," + "    \"currency\":\"ERN\""
				+ "  }," + "  {" + "    \"symbol\":\"Br\","
				+ "    \"currency\":\"ETB\"" + "  }," + " {"
				+ "    \"symbol\":\"£\"," + "    \"currency\":\"GBP\"" + " },"
				+ " {" + "    \"symbol\":\"GEL\"," + "    \"currency\":\"GEL\""
				+ " }," + " {" + "   \"symbol\":\"GH\u20B5\","
				+ "   \"currency\":\"GHS\"" + "}," + "  {"
				+ "    \"symbol\":\"FG\"," + "    \"currency\":\"GNF\""
				+ "  }," + "  {" + "     \"symbol\":\"GTQ\","
				+ "     \"currency\":\"GTQ\"" + "   }," + "  {"
				+ "    \"symbol\":\"HK$\"," + "    \"currency\":\"HKD\""
				+ "  }," + "  {" + "    \"symbol\":\"HNL\","
				+ "    \"currency\":\"HNL\"" + "  }," + "   {"
				+ "    \"symbol\":\"kn\"," + "    \"currency\":\"HRK\"" + " },"
				+ "  {" + "    \"symbol\":\"Ft\","
				+ "     \"currency\":\"HUF\"" + "   }," + "  {"
				+ "    \"symbol\":\"Rp\"," + "     \"currency\":\"IDR\""
				+ "  }," + "  {" + "     \"symbol\":\"\u20AA\","
				+ "    \"currency\":\"ILS\"" + "  }," + "  {"
				+ "     \"symbol\":\"₹\"," + "     \"currency\":\"INR\""
				+ "   }," + "  {" + "  \"symbol\":\"IQD\","
				+ "     \"currency\":\"IQD\"" + " }," + "   {"
				+ "    \"symbol\":\"IRR\"," + "    \"currency\":\"IRR\""
				+ "  }," + "  {" + "    \"symbol\":\"Ikr\","
				+ "     \"currency\":\"ISK\"" + "  }," + " {"
				+ "    \"symbol\":\"J$\"," + "    \"currency\":\"JMD\""
				+ "  }," + "  {" + "     \"symbol\":\"JD\","
				+ "     \"currency\":\"JOD\"" + "  }," + "  {"
				+ "     \"symbol\":\"¥\"," + "    \"currency\":\"JPY\""
				+ "   }," + " {" + "     \"symbol\":\"Ksh\","
				+ "     \"currency\":\"KES\"" + "  }," + "  {"
				+ "     \"symbol\":\"KHR\"," + "   \"currency\":\"KHR\""
				+ " }," + "  {" + "    \"symbol\":\"CF\","
				+ "    \"currency\":\"KMF\"" + "  }," + "  {"
				+ "     \"symbol\":\"\u20A9\"," + "     \"currency\":\"KRW\""
				+ "  }," + "  {" + "     \"symbol\":\"KD\","
				+ "     \"currency\":\"KWD\"" + "  }," + "   {"
				+ "    \"symbol\":\"KZT\"," + "    \"currency\":\"KZT\""
				+ "  }," + " {" + "    \"symbol\":\"LB£\","
				+ "    \"currency\":\"LBP\"" + "  }," + " {"
				+ "  \"symbol\":\"SLRs\"," + "    \"currency\":\"LKR\""
				+ "  }," + "   {" + "   \"symbol\":\"Lt\","
				+ "      \"currency\":\"LTL\"" + "  }," + "  {"
				+ "    \"symbol\":\"Ls\"," + "    \"currency\":\"LVL\"" + " },"
				+ " {" + "    \"symbol\":\"LD\"," + "     \"currency\":\"LYD\""
				+ "  }," + "  {" + "     \"symbol\":\"MAD\","
				+ "   \"currency\":\"MAD\"" + "  }," + "  {"
				+ "     \"symbol\":\"MDL\"," + "    \"currency\":\"MDL\""
				+ "  }," + "  {" + "   \"symbol\":\"MGA\","
				+ "    \"currency\":\"MGA\"" + " }," + "  {"
				+ "    \"symbol\":\"MKD\"," + "    \"currency\":\"MKD\""
				+ " }," + " {" + "   \"symbol\":\"MMK\","
				+ "   \"currency\":\"MMK\"" + "   }," + "  {"
				+ "     \"symbol\":\"MOP$\"," + "     \"currency\":\"MOP\""
				+ "  }," + " {" + "    \"symbol\":\"MURs\","
				+ "    \"currency\":\"MUR\"" + " }," + "  {"
				+ "    \"symbol\":\"MX$\"," + "    \"currency\":\"MXN\""
				+ " }," + " {" + "     \"symbol\":\"RM\","
				+ "   \"currency\":\"MYR\"" + "  }," + " {"
				+ "   \"symbol\":\"MTn\"," + "     \"currency\":\"MZN\""
				+ "  }," + " {" + "    \"symbol\":\"N$\","
				+ "    \"currency\":\"NAD\"" + " }," + " {"
				+ "     \"symbol\":\"\u20A6\"," + "     \"currency\":\"NGN\""
				+ "  }," + "   {" + "    \"symbol\":\"C$\","
				+ "     \"currency\":\"NIO\"" + "  }," + "  {"
				+ "     \"symbol\":\"Nkr\"," + "     \"currency\":\"NOK\""
				+ "  }," + "  {" + "     \"symbol\":\"NPRs\","
				+ "     \"currency\":\"NPR\"" + "  }," + " {"
				+ "    \"symbol\":\"NZ$\"," + "   \"currency\":\"NZD\"" + " },"
				+ "  {" + "     \"symbol\":\"OMR\","
				+ "    \"currency\":\"OMR\"" + "  }," + "  {"
				+ " \"symbol\":\"B/.\"," + "     \"currency\":\"PAB\"" + "  },"
				+ "  {" + "     \"symbol\":\"S/.\","
				+ "     \"currency\":\"PEN\"" + "  }," + "  {"
				+ "     \"symbol\":\"\u20B1\"," + "   \"currency\":\"PHP\""
				+ "   }," + "  {" + "     \"symbol\":\"PKRs\","
				+ "     \"currency\":\"PKR\"" + " }," + "  {"
				+ "     \"symbol\":\"zł\"," + "     \"currency\":\"PLN\""
				+ "  }," + "  {" + "    \"symbol\":\"\u20B2\","
				+ "     \"currency\":\"PYG\"" + " }," + "  {"
				+ "     \"symbol\":\"QR\"," + "    \"currency\":\"QAR\"" + "},"
				+ " {" + "    \"symbol\":\"RON\"," + "    \"currency\":\"RON\""
				+ "  }," + "  {" + "     \"symbol\":\"din.\","
				+ "     \"currency\":\"RSD\"" + "  }," + "  {"
				+ "     \"symbol\":\"RUB\"," + "     \"currency\":\"RUB\""
				+ "  }," + " {" + "    \"symbol\":\"RWF\","
				+ "    \"currency\":\"RWF\"" + " }," + "  {"
				+ "   \"symbol\":\"SR\"," + "     \"currency\":\"SAR\""
				+ "  }," + "  {" + "    \"symbol\":\"SDG\","
				+ "    \"currency\":\"SDG\"" + "  }," + "  {"
				+ "    \"symbol\":\"Skr\"," + "     \"currency\":\"SEK\""
				+ "  }," + "   {" + "    \"symbol\":\"S$\","
				+ "    \"currency\":\"SGD\"" + " }," + " {"
				+ "    \"symbol\":\"Ssh\"," + "    \"currency\":\"SOS\""
				+ " }," + " {" + "    \"symbol\":\"SY£\","
				+ "    \"currency\":\"SYP\"" + "  }," + "  {"
				+ "     \"symbol\":\"฿\"," + "     \"currency\":\"THB\""
				+ "  }," + "  {" + "    \"symbol\":\"DT\","
				+ "    \"currency\":\"TND\"" + "  }," + " {"
				+ "    \"symbol\":\"T$\"," + "    \"currency\":\"TOP\""
				+ "  }," + "  {" + "     \"symbol\":\"TL\","
				+ "     \"currency\":\"TRY\"" + "  }," + "   {"
				+ "     \"symbol\":\"TT$\"," + "    \"currency\":\"TTD\""
				+ "  }," + " {" + "    \"symbol\":\"NT$\","
				+ "    \"currency\":\"TWD\"" + "  }," + " {"
				+ "     \"symbol\":\"TSh\"," + "    \"currency\":\"TZS\""
				+ "  }," + " {" + "    \"symbol\":\"\u20B4\","
				+ "    \"currency\":\"UAH\"" + " }," + " {"
				+ "    \"symbol\":\"USh\"," + "     \"currency\":\"UGX\""
				+ "  }," + "  {" + "    \"symbol\":\"$U\","
				+ "     \"currency\":\"UYU\"" + "  }," + "  {"
				+ "    \"symbol\":\"UZS\"," + "    \"currency\":\"UZS\""
				+ "  }," + "  {" + "     \"symbol\":\"Bs.F.\","
				+ "   \"currency\":\"VEF\"" + " }," + " {"
				+ "     \"symbol\":\"\u20AB\"," + "     \"currency\":\"VND\""
				+ "  }," + "   {" + "      \"symbol\":\"FCFA\","
				+ "     \"currency\":\"XAF\"" + "  }," + "   {"
				+ "     \"symbol\":\"CFA\"," + "     \"currency\":\"XOF\""
				+ "  }," + "   {" + "     \"symbol\":\"YR\","
				+ "   \"currency\":\"YER\"" + "   }," + "   {"
				+ "     \"symbol\":\"R\"," + "    \"currency\":\"ZAR\""
				+ "   }," + "   {" + "     \"symbol\":\"ZK\","
				+ "     \"currency\":\"ZMK\"" + "  }" + "]";

		JSONArray c = (JSONArray) new JSONParser().parse(jsonString);
		for (int i = 0; i < c.size(); i++) {
			JSONObject obj = (JSONObject) c.get(i);
			if (currencyCode.equalsIgnoreCase(obj.get("currency").toString())) {
				symbol = obj.get("symbol").toString();
			}
			// System.out.println("["+obj.get("currency")+", "+obj.get("symbol")+"]");
		}

		return symbol;
	}

	public HashMap getColNameNType(Long columnId) {
		HashMap finalMap = new HashMap();
		DataViewsColumns dvc = dataViewsColumnsRepository.findOne(columnId);
		if (dvc != null) 
		{
			if("File Template".equalsIgnoreCase(dvc.getRefDvType())) 
			{
				FileTemplateLines ftl = fileTemplateLinesRepository.findOne(Long.parseLong(dvc.getRefDvColumn()));
				if (ftl != null)
				{
					finalMap.put("columnName", ftl.getColumnAlias());
					finalMap.put("dataType", dvc.getColDataType());
				}
			} 
			else if("Data View".equalsIgnoreCase(dvc.getRefDvType()) || dvc.getRefDvType() == null) 
			{
				finalMap.put("columnName", dvc.getColumnName());
				finalMap.put("dataType", dvc.getColDataType());
			}
			finalMap.put("displayColName", dvc.getColumnName());
		}
		return finalMap;
	}
	
	public HashMap getColumnNameNDatatypeForView(Long viewId)
	{
		HashMap finalMap = new HashMap();
		List<DataViewsColumns> dvcs = dataViewsColumnsRepository.findByDataViewId(viewId);
		if(dvcs.size()>0)
		{
			for(DataViewsColumns dvc : dvcs)
			{
				if("File Template".equalsIgnoreCase(dvc.getRefDvType())) 
				{
					FileTemplateLines ftl = fileTemplateLinesRepository.findOne(Long.parseLong(dvc.getRefDvColumn()));
					if (ftl != null)
					{
						finalMap.put(ftl.getColumnAlias(), dvc.getColDataType());
					}
				} 
				else if("Data View".equalsIgnoreCase(dvc.getRefDvType()) || dvc.getRefDvType() == null) 
				{
					finalMap.put(dvc.getColumnName(), dvc.getColDataType());
				}
			}
		}
		return finalMap;
	}

	public HashMap unReconcileBasedOnReconRefs(List<String> reconRefs,
			List<RuleGroup> accountingGroupIds, 
			Long tenantId, 
			Long userId, 
			String jobReference) 
	{
		List<ReconciliationResult> reconResult = reconciliationResultRepository.fetchRecordsByReconReferenceIds(reconRefs);
		List<ReconciliationResult> approvalsReconResult = reconciliationResultRepository.fetchRecordsByApprovalReconReferenceIds(reconRefs);		
		HashMap finalMap = new HashMap();
		finalMap.put("approvalsCount", approvalsReconResult.size());
		finalMap.put("unReconcileCount", reconResult.size());
		ZonedDateTime reconciledDate = ZonedDateTime.now();
		log.info("ReconciliationResult with recon ref ids size: "+ reconResult.size());
		if(accountingGroupIds.size() > 0)
		{
			log.info("Un Reconciling activity based");
			for(RuleGroup accountingGroupIdTagged : accountingGroupIds) 
			{
				List<BigInteger> accountingViewIds = new ArrayList<BigInteger>();
				if(accountingGroupIdTagged != null && reconRefs.size() > 0) 
				{
					List<Long> accountingRuleIds = ruleGroupDetailsRepository.fetchRuleIdsByGroupIdAndTenantId(accountingGroupIdTagged.getId(), tenantId);
					log.info("accountingRuleIds " + accountingRuleIds);
					accountingViewIds = rulesRepository.fetchDistictSrcViewIdsByRuleId(accountingRuleIds);
					log.info("accountingViewIds " + accountingViewIds);
				}
				Map<Long, Long> rowAndViewIds = new HashMap<Long, Long>();
				List<ReconciliationResult> unreconciledRecords = new ArrayList<ReconciliationResult>();
				List<Long> originalIds = new ArrayList<Long>();
				List<Long> targetIds = new ArrayList<Long>();
				List<Long> ruleIds = new ArrayList<Long>();

				if(reconResult.size() > 0) 
				{
					List<ReconciliationResult> reconResultContainer = new ArrayList<ReconciliationResult>();
					List<Long> varianceIds = new ArrayList<Long>();
					int reconResultTemp = 1;
					for(ReconciliationResult row : reconResult) 
					{
						if (ruleIds.isEmpty() || !ruleIds.contains(row.getReconciliationRuleId())) 
						{
							ruleIds.add(row.getReconciliationRuleId());
						}
						row.setReconStatus("UNRECONCILED");
						row.setCurrentRecordFlag(false);
						row.setReconciledDate(reconciledDate);
						row.setReconJobReference(jobReference);
						if(row.getOriginalViewId() != null && accountingViewIds.contains(BigInteger.valueOf(row.getOriginalViewId()))) 
						{
							rowAndViewIds.put(row.getOriginalRowId(), row.getOriginalViewId());
							if(row.getOriginalRowId().compareTo(0L) < 0) 
							{
								varianceIds.add(row.getOriginalRowId() * -1);
							}
						} 
						else if(row.getTargetViewId() != null && accountingViewIds.contains(BigInteger.valueOf(row.getTargetViewId()))) 
						{
							targetIds.add(row.getTargetRowId());
							if(row.getTargetRowId().compareTo(0L) < 0) 
							{
								varianceIds.add(row.getTargetRowId() * -1);
							}
						}

						if (row.getOriginalViewId() != null) 
						{
							originalIds.add(row.getOriginalRowId());
						} 
						else if (row.getTargetViewId() != null) 
						{
							targetIds.add(row.getTargetRowId());
						}
						if (reconResultTemp < 1000) 
						{
							reconResultContainer.add(row);
							reconResultTemp++;
						} 
						else 
						{
							reconciliationResultRepository.save(reconResultContainer);
							reconciliationResultRepository.flush();
							reconResultTemp = 1;
							reconResultContainer.clear();
						}
					}
					if(!reconResultContainer.isEmpty()) 
					{
						reconciliationResultRepository.save(reconResultContainer);
						reconciliationResultRepository.flush();
						reconResultTemp = 1;
						reconResultContainer.clear();
					}
					if(!varianceIds.isEmpty())
					{
						dataChildRepository.deleteByIds(varianceIds);
					}
				}
				Iterator<Long> iter = null;
				try 
				{
					iter = rowAndViewIds.keySet().iterator();
				} 
				catch(Exception exp) 
				{
					exp.printStackTrace();
				}
				List<Long> viewIds = new ArrayList<Long>();
				List<Long> rowIds = new ArrayList<Long>();

				if(iter != null && accountingGroupIdTagged != null) 
				{
					while(iter.hasNext()) 
					{
						Long rowId = iter.next();
						if (rowIds.isEmpty() || !rowIds.contains(rowId)) 
						{
							rowIds.add(rowId);
						}
						Long viewId = rowAndViewIds.get(rowId);
						if(viewIds.isEmpty() || !viewIds.contains(viewId))
						{
							viewIds.add(viewId);
						}
					}
					List<AccountingEvents> acctEventsContainer = new ArrayList<AccountingEvents>();
					int acctEventsTemp = 1;
					List<AccountingEvents> deletedEvents = new ArrayList<AccountingEvents>();
					List<AccountingEvents> existingRecords = accountingEventsRepository.findByAcctRuleGroupIdAndDataViewIdInAndRowIdInAndRuleIdIn(accountingGroupIdTagged.getId(), viewIds,rowIds, ruleIds);
					for(AccountingEvents existingRecord : existingRecords) 
					{
						if(existingRecord.getAcctStatus().equalsIgnoreCase("PENDING")) 
						{
							existingRecord.setEventType("UNRECONCILED");
							existingRecord.setEventTime(ZonedDateTime.now());
						} 
						else if(existingRecord.getAcctStatus().equalsIgnoreCase("ACCOUNTED")) 
						{
							existingRecord.setEventType("UNRECONCILED");
							existingRecord.setAcctStatus("NEED_REACCOUNTING");
							existingRecord.setEventTime(ZonedDateTime.now());
							existingRecord.setLastUpdatedDate(ZonedDateTime.now());
						} 
						else if("NOT_ACCOUNTED".equalsIgnoreCase(existingRecord.getAcctStatus())) 
						{
							deletedEvents.add(existingRecord);
							continue;
						}
						if(acctEventsTemp < 1000) 
						{
							acctEventsContainer.add(existingRecord);
							acctEventsTemp++;
						} 
						else
						{
							accountingEventsRepository.save(acctEventsContainer);
							accountingEventsRepository.flush();
							acctEventsTemp = 1;
							acctEventsContainer.clear();
						}
					}
					if(!acctEventsContainer.isEmpty()) 
					{
						accountingEventsRepository.save(acctEventsContainer);
						accountingEventsRepository.flush();
						acctEventsTemp = 1;
						acctEventsContainer.clear();
					}
					if(!deletedEvents.isEmpty()) 
					{
						accountingEventsRepository.delete(deletedEvents);
					}
				}

				/*
				 * if(unreconciledRecords.size()>0) { List<ReconciliationResult>
				 * unReconciledDataForBalaceTypeSource = new
				 * ArrayList<ReconciliationResult>(); List<ReconciliationResult>
				 * unReconciledDataForBalaceTypeTarget = new
				 * ArrayList<ReconciliationResult>();
				 * 
				 * Set<ReconciliationResult> unReconciledIdsSource = new
				 * HashSet<ReconciliationResult>(); Set<ReconciliationResult>
				 * unReconciledIdsTarget = new HashSet<ReconciliationResult>();
				 * 
				 * for(ReconciliationResult record:unreconciledRecords) {
				 * if(record.getTargetViewId()==null ||
				 * record.getTargetRowId()==null) {
				 * unReconciledDataForBalaceTypeSource.add(record);
				 * 
				 * ReconciliationResult srcIdRec = new ReconciliationResult();
				 * srcIdRec.setOriginalViewId(record.getOriginalViewId());
				 * srcIdRec
				 * .setReconciliationRuleGroupId(record.getReconciliationRuleGroupId
				 * ());
				 * srcIdRec.setReconciliationRuleId(record.getReconciliationRuleId
				 * ());
				 * 
				 * unReconciledIdsSource.add(srcIdRec); }
				 * 
				 * if(record.getOriginalViewId()==null ||
				 * record.getOriginalRowId()==null) {
				 * unReconciledDataForBalaceTypeTarget.add(record);
				 * 
				 * ReconciliationResult tarIdRec = new ReconciliationResult();
				 * tarIdRec.setTargetViewId(record.getTargetViewId());
				 * tarIdRec.setReconciliationRuleGroupId
				 * (record.getReconciliationRuleGroupId());
				 * tarIdRec.setReconciliationRuleId
				 * (record.getReconciliationRuleId());
				 * 
				 * unReconciledIdsTarget.add(tarIdRec); } } log.info("Time6: "+
				 * new Date()); String
				 * dbUrl=env.getProperty("spring.datasource.url"); String[]
				 * parts=dbUrl.split("[\\s@&?$+-]+"); String
				 * schemaName=parts[0].split("/")[3];
				 * 
				 * Map<Long,List<String>> viewColsMap = new HashMap<Long,
				 * List<String>>();
				 * log.info("UnReconcileIds For Source: "+unReconciledIdsSource
				 * .size()+", unReconciledIdsSource: "+unReconciledIdsSource);
				 * log
				 * .info("UnReconcileIds For Target: "+unReconciledIdsTarget.size
				 * ()+", unReconciledIdsTarget: "+unReconciledIdsTarget);
				 * for(ReconciliationResult viewId:unReconciledIdsSource) { Long
				 * id = viewId.getOriginalViewId(); Long reconGroupId =
				 * viewId.getReconciliationRuleGroupId(); Long reconRuleId =
				 * viewId.getReconciliationRuleId();
				 * 
				 * HashMap<String, List<String>> groupBycolsAndQualifier =
				 * balanceTypeService
				 * .findGrpByColNamesAndQualifiersForDataView(id); String
				 * dataViewName =
				 * dataViewsRepository.findById(id).getDataViewName();
				 * List<String> groupBycols =
				 * groupBycolsAndQualifier.get("groupByCol"); String
				 * amtQualifierCol = null;
				 * if(groupBycolsAndQualifier.get("AMOUNT")!=null) {
				 * amtQualifierCol =
				 * groupBycolsAndQualifier.get("AMOUNT").get(0); } else {
				 * continue; } if(groupBycols.size()==0) { continue; }
				 * viewColsMap.put(id, groupBycols );
				 * 
				 * String subQuery = "SELECT original_row_id FROM "+schemaName+
				 * ".t_reconciliation_result WHERE recon_job_reference = '"
				 * +jobReference+"' and " +
				 * "reconciliation_rule_group_id = "+reconGroupId
				 * +" and reconciliation_rule_id = "
				 * +reconRuleId+" and original_view_id = "+id+
				 * " and recon_status = 'UNRECONCILED' and current_record_flag = true and original_row_id is not null"
				 * ; String inIds = ""; for(Long unreconId:originalIds) {
				 * if(inIds.length()==0) { inIds = ""+unreconId; } else { inIds
				 * += ", "+unreconId; } } String groupBySQL =
				 * "SELECT SUM("+amtQualifierCol+") as type_amt"; for(String
				 * col: groupBycols) { groupBySQL += ", "+col.toLowerCase(); }
				 * 
				 * groupBySQL +=
				 * " FROM "+schemaName+".`"+dataViewName.toLowerCase()+
				 * "` WHERE scrIds in ( "+inIds+" ) "; groupBySQL +=
				 * " GROUP BY ";
				 * 
				 * for(String col: groupBycols) { groupBySQL +=
				 * col.toLowerCase()+", "; }
				 * 
				 * groupBySQL = groupBySQL.trim(); if(groupBySQL.endsWith(","))
				 * { groupBySQL = groupBySQL.substring(0,groupBySQL.length()-1);
				 * }
				 * 
				 * List<BalanceType> balanceTypeForProcessedRecords = new
				 * ArrayList<BalanceType>(); if(inIds.length()>0) {
				 * balanceTypeForProcessedRecords =
				 * balanceTypeService.fetchProcessedRecords
				 * (groupBySQL,groupBycols,id, tenantId, userId,
				 * "RECONCILIATION", reconGroupId, reconRuleId,"SOURCE"); }
				 * 
				 * List<BalanceType> existingBalanceTypeRecords =
				 * balanceTypeService
				 * .fetchExistingBalanceTypeRecords("RECONCILIATION",
				 * groupBycols, id, tenantId, reconGroupId, reconRuleId);
				 * List<BalanceType> derivedBalanceTypeRecords =
				 * balanceTypeService
				 * .deriveOpeningAndClosingBalance(existingBalanceTypeRecords
				 * ,balanceTypeForProcessedRecords);
				 * 
				 * balanceTypeRepository.save(derivedBalanceTypeRecords);
				 * 
				 * } log.info("Time7: "+ new Date()); for(ReconciliationResult
				 * viewId:unReconciledIdsTarget) { Long id =
				 * viewId.getTargetViewId(); Long reconGroupId =
				 * viewId.getReconciliationRuleGroupId(); Long reconRuleId =
				 * viewId.getReconciliationRuleId();
				 * 
				 * HashMap<String, List<String>> groupBycolsAndQualifier =
				 * balanceTypeService
				 * .findGrpByColNamesAndQualifiersForDataView(id); String
				 * dataViewName =
				 * dataViewsRepository.findById(id).getDataViewName();
				 * List<String> groupBycols =
				 * groupBycolsAndQualifier.get("groupByCol"); String
				 * amtQualifierCol = null;
				 * if(groupBycolsAndQualifier.get("AMOUNT")!=null) {
				 * amtQualifierCol =
				 * groupBycolsAndQualifier.get("AMOUNT").get(0); } else {
				 * continue; } if(groupBycols.size()==0) { continue; }
				 * viewColsMap.put(id, groupBycols );
				 * 
				 * String subQuery = "SELECT target_row_id FROM "+schemaName+
				 * ".t_reconciliation_result WHERE recon_job_reference = '"
				 * +jobReference+"' and " +
				 * "reconciliation_rule_group_id = "+reconGroupId
				 * +" and reconciliation_rule_id = "
				 * +reconRuleId+" and target_view_id = "+id+
				 * " and recon_status = 'UNRECONCILED' and current_record_flag = true and target_row_id is not null"
				 * ; String inIds = "";
				 * 
				 * for(Long unreconId:targetIds) { if(inIds.length()==0) { inIds
				 * = ""+unreconId; } else { inIds += ", "+unreconId; } }
				 * 
				 * String groupBySQL =
				 * "SELECT SUM("+amtQualifierCol+") as type_amt"; for(String
				 * col: groupBycols) { groupBySQL += ", "+col.toLowerCase(); }
				 * 
				 * groupBySQL +=
				 * " FROM "+schemaName+".`"+dataViewName.toLowerCase()+
				 * "` WHERE scrIds in ( "+inIds+" ) "; groupBySQL +=
				 * " GROUP BY ";
				 * 
				 * for(String col: groupBycols) { groupBySQL +=
				 * col.toLowerCase()+", "; }
				 * 
				 * groupBySQL = groupBySQL.trim(); if(groupBySQL.endsWith(","))
				 * { groupBySQL = groupBySQL.substring(0,groupBySQL.length()-1);
				 * }
				 * 
				 * List<BalanceType> balanceTypeForProcessedRecords = new
				 * ArrayList<BalanceType>(); if(inIds.length()>0) {
				 * balanceTypeForProcessedRecords =
				 * balanceTypeService.fetchProcessedRecords
				 * (groupBySQL,groupBycols,id, tenantId, userId,
				 * "RECONCILIATION", reconGroupId, reconRuleId, "TARGET"); }
				 * 
				 * List<BalanceType> existingBalanceTypeRecords =
				 * balanceTypeService
				 * .fetchExistingBalanceTypeRecords("RECONCILIATION",
				 * groupBycols, id, tenantId, reconGroupId, reconRuleId);
				 * List<BalanceType> derivedBalanceTypeRecords =
				 * balanceTypeService
				 * .deriveOpeningAndClosingBalance(existingBalanceTypeRecords
				 * ,balanceTypeForProcessedRecords);
				 * 
				 * balanceTypeRepository.save(derivedBalanceTypeRecords); }
				 * log.info("Time8: "+ new Date()); }
				 * 
				 * }
				 */
			}
		} 
		else 
		{
			log.info("Un Reconciling non activity based.");
			if(reconResult.size() > 0) 
			{
				List<ReconciliationResult> reconResultContainer = new ArrayList<ReconciliationResult>();
				List<Long> varianceIds = new ArrayList<Long>();
				int reconResultTemp = 1;
				for(ReconciliationResult row : reconResult) 
				{
					row.setReconStatus("UNRECONCILED");
					row.setReconciledDate(reconciledDate);
					row.setReconJobReference(jobReference);
					if(row.getOriginalViewId() != null) 
					{
						if(row.getOriginalRowId().compareTo(0L) < 0) 
						{
							varianceIds.add(row.getOriginalRowId() * -1);
						}
					} 
					else if(row.getTargetViewId() != null) 
					{
						if(row.getTargetRowId().compareTo(0L) < 0) 
						{
							varianceIds.add(row.getTargetRowId() * -1);
						}
					}
					if(reconResultTemp < 1000) 
					{
						reconResultContainer.add(row);
						reconResultTemp++;
					} 
					else 
					{
						reconciliationResultRepository.save(reconResultContainer);
						reconciliationResultRepository.flush();
						reconResultTemp = 1;
						reconResultContainer.clear();
					}
				}
				if(!reconResultContainer.isEmpty())
				{
					reconciliationResultRepository.save(reconResultContainer);
					reconciliationResultRepository.flush();
					reconResultTemp = 1;
					reconResultContainer.clear();
				}
				if(!varianceIds.isEmpty()) 
				{
					dataChildRepository.deleteByIds(varianceIds);
				}
			}
		}
		return finalMap;
	}

	/**
	 * Author: Shiva Purpose: Posting reconciliation counts in
	 * t_app_module_summary table
	 **/
	public void postAppModuleSummaryTable(Long groupId, Long viewId,
			Long ruleId, Long userId, Long typeCount, String type,
			String sourceOrTarget) {
		log.info("Posting " + sourceOrTarget
				+ " count in t_app_module_summary table for the group id: "
				+ groupId + ", view id: " + viewId + ", rule id: " + ruleId
				+ ", type: " + type);
		AppModuleSummary amsSource = appModuleSummaryRepository
				.findByModuleAndRuleGroupIdAndRuleIdAndTypeAndViewId(type,
						groupId, ruleId, sourceOrTarget, viewId);
		if (amsSource != null) {
			amsSource.setLastUpdatedBy(userId);
			amsSource.setLastUpdatedDate(ZonedDateTime.now());
			amsSource.setTypeCount(amsSource.getTypeCount() - typeCount);
			AppModuleSummary amsSrcUpdate = appModuleSummaryRepository
					.save(amsSource);
		} else {
			AppModuleSummary amsCreate = new AppModuleSummary();
			amsCreate.setCreatedBy(userId);
			amsCreate.setCreatedDate(ZonedDateTime.now());
			amsCreate.setLastUpdatedBy(userId);
			amsCreate.setLastUpdatedDate(ZonedDateTime.now());
			amsCreate.setModule(type);
			amsCreate.setRuleGroupId(groupId);
			amsCreate.setRuleId(ruleId);
			amsCreate.setType(sourceOrTarget);
			amsCreate.setTypeCount(typeCount);
			amsCreate.setViewId(viewId);
			AppModuleSummary amsSrcCreate = appModuleSummaryRepository
					.save(amsCreate);
		}
	}

	public String convertDatsToString(List<String> dates) {
		String datesAsString = "";
		for (int i = 0; i < dates.size(); i++) {
			if (i == dates.size() - 1) {
				datesAsString = datesAsString + dates.get(i).trim();
			} else {
				datesAsString = datesAsString + dates.get(i).trim() + "|";
			}
		}
		return datesAsString;
	}



	public String getCurrencySymbol(Set<String> codesList)
			throws ParseException {
		String currencySymbol = "";
		if (codesList.size() == 1) {
			currencySymbol = currencySymbol
					+ getCurrencySymbol(codesList.toArray()[codesList.size() - 1]
							.toString());
		} else if (codesList.size() > 1) {
			currencySymbol = currencySymbol + "*";
		}
		return currencySymbol;
	}

	public HashMap getDataTYpeNColumnName(Long colId) {
		HashMap finalMap = new HashMap();
		finalMap.put("dataType", "");
		finalMap.put("columnName", "");
		DataViewsColumns dvc = dataViewsColumnsRepository.findOne(colId);
		if (dvc != null) {
			if ("VARCHAR".equals(dvc.getColDataType())
					|| "DATE".equalsIgnoreCase(dvc.getColDataType())) {
				finalMap.put("dataType", "string");
			} else if ("INTEGER".equalsIgnoreCase(dvc.getColDataType())
					|| "DECIMAL".equalsIgnoreCase(dvc.getColDataType())) {
				finalMap.put("dataType", "integer");
			}
			if ("File Template".equalsIgnoreCase(dvc.getRefDvType())) {
				FileTemplateLines ftl = fileTemplateLinesRepository
						.findOne(Long
								.parseLong(dvc.getRefDvColumn().toString()));
				if (ftl != null) {
					finalMap.put("columnName", ftl.getColumnAlias());
				}
			} else if ("Data View".equalsIgnoreCase(dvc.getRefDvType())
					|| dvc.getRefDvType() == null) {
				finalMap.put("columnName", dvc.getColumnName());
			}
		}
		return finalMap;
	}

	public HashMap updateAppModuleSummaryForSource(String amountQualifier,
			Long viewId, Long groupId, String module, String type,
			String status, Long userId, Long tenantId, String periodFactor)
			throws SQLException, ClassNotFoundException 
	{
		log.info("Updating counts and amounts in app_module_summary table for source...");
		HashMap finalMap = new HashMap();
		Connection conn = null;
		Statement countAmntsStmt = null;
		ResultSet countAmntsRS = null;

		Statement dvStmt = null;
		ResultSet dvRS = null;

		PreparedStatement appModSumIdsStmt = null;
		ResultSet appModSumIdsRS = null;
		
		PreparedStatement preparedStatement = null;

		PreparedStatement checkRecordStmt = null;
		ResultSet checkRecordRS = null;

		Statement updateExistedRecordStmt = null;
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

		Statement multiCurrencyStmt = null;
		ResultSet multiCurrencyRS = null;

		try {
			DataSource ds = (DataSource) ApplicationContextProvider.getApplicationContext().getBean("dataSource");
			conn = ds.getConnection();
			DataViews dv = dataViewsRepository.findOne(viewId.longValue());
			// Fetching app_module_summary ids based on status
			String appModSumIdsQuery = "select id from t_app_module_summary where rule_group_id = "
					+ groupId
					+ " and view_id = "
					+ viewId
					+ " and module = 'RECONCILIATION' and type = 'SOURCE'";
			List<Long> appModSumIds = new ArrayList<Long>();
			appModSumIdsStmt = conn.prepareStatement(appModSumIdsQuery);
			appModSumIdsRS = appModSumIdsStmt.executeQuery();
			while (appModSumIdsRS.next()) 
			{
				appModSumIds.add(appModSumIdsRS.getLong(1));
			}
			if(dv != null) 
			{
				String dvQuery = "select Date(`" + periodFactor
						+ "`), count(*), sum(`" + amountQualifier + "`) from `"
						+ dv.getDataViewName().toLowerCase()
						+ "` group by Date(`" + periodFactor + "`)";
				dvStmt = conn.createStatement();
				dvRS = dvStmt.executeQuery(dvQuery);
				HashMap dvMap = new HashMap();
				while(dvRS.next()) 
				{
					HashMap dateMap = new HashMap();
					dateMap.put("count", dvRS.getString(2));
					dateMap.put("amount", dvRS.getString(3));
					dvMap.put(dvRS.getString(1), dateMap);
				}
				String query = "select Date(dv.`"
						+ periodFactor
						+ "`), count(*), sum(dv.`"
						+ amountQualifier
						+ "`),"
						+ "    recon.reconciliation_rule_id, recon.reconciliation_rule_group_id"
						+ "    from t_reconciliation_result recon,"
						+ "    `"
						+ dv.getDataViewName().toLowerCase()
						+ "` dv"
						+ "	   where reconciliation_rule_group_id = "
						+ groupId
						+ " "
						+ "    and original_view_id = "
						+ viewId
						+ ""
						+ "    and current_record_flag is true"
						+ "    and recon.original_row_id = dv.scrIds"
						+ "    and recon_status = 'RECONCILED'"
						+ "    group by Date(dv.`"
						+ periodFactor
						+ "`),reconciliation_rule_id, reconciliation_rule_group_id";

				countAmntsStmt = conn.createStatement();
				countAmntsRS = countAmntsStmt.executeQuery(query);

				String insertTableSQL = "INSERT INTO t_app_module_summary"
						+ "(file_date, module, rule_group_id, rule_id, view_id, type, type_count, created_date, last_updated_date, type_amt, dv_count, dv_amt) VALUES"
						+ "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				preparedStatement = conn.prepareStatement(insertTableSQL);
				updateExistedRecordStmt = conn.createStatement();
				while (countAmntsRS.next()) 
				{
					String date = countAmntsRS.getString(1);
					Long count = Long.parseLong(countAmntsRS.getString(2));
					BigDecimal amount = new BigDecimal(countAmntsRS.getString(3));
					Long ruleId = Long.parseLong(countAmntsRS.getString(4));
					LocalDate localDate = LocalDate.parse(date);
					HashMap getDVMap = (HashMap) dvMap.get(date);
					Long dvCount = Long.parseLong(getDVMap.get("count").toString());
					BigDecimal dvAmount = new BigDecimal(getDVMap.get("amount").toString());
					// Check to record exist in app_module_summary table or not
					String checkRecordquery = "select * from t_app_module_summary where file_date = '"
							+ date
							+ "' and module = 'RECONCILIATION' and rule_group_id = "
							+ groupId
							+ " and rule_id = "
							+ ruleId
							+ " and view_id = "
							+ viewId
							+ " and type = 'SOURCE'";

					checkRecordStmt = conn.prepareStatement(checkRecordquery);
					checkRecordRS = checkRecordStmt.executeQuery();
					int size = 0;
					Long id = null;
					while (checkRecordRS.next()) 
					{
						id = Long.parseLong(checkRecordRS.getString("id"));
						size = size + 1;
					}
					if(size > 0)
					{
						if(size == 1) 
						{
							// Updating existing record
							String updateQuery = "update t_app_module_summary set type_count = "
									+ count
									+ ", type_amt = "
									+ amount
									+ ", dv_count = "
									+ dvCount
									+ ", dv_amt = "
									+ dvAmount
									+ ", last_updated_date = '"
									+ timeStamp
									+ "', last_updated_by = "
									+ userId + " where id = " + id;
							updateExistedRecordStmt.addBatch(updateQuery);
							// updateExistedRecordStmt.executeUpdate(updateQuery);
							appModSumIds.remove(id); // Removing updated Record
						} 
					} 
					else 
					{
						preparedStatement.setDate(1,java.sql.Date.valueOf(date));
						preparedStatement.setString(2, "RECONCILIATION");
						preparedStatement.setLong(3, groupId);
						if(ruleId != null)
						{
							preparedStatement.setLong(4, ruleId);
						} 
						else 
						{
							preparedStatement.setNull(4, java.sql.Types.INTEGER);
						}
						preparedStatement.setLong(5, viewId);
						preparedStatement.setString(6, "SOURCE");
						preparedStatement.setLong(7, count);
						preparedStatement.setTimestamp(8,new java.sql.Timestamp(System.currentTimeMillis()));
						preparedStatement.setTimestamp(9,new java.sql.Timestamp(System.currentTimeMillis()));
						preparedStatement.setBigDecimal(10, amount);
						preparedStatement.setLong(11, dvCount);
						preparedStatement.setBigDecimal(12, dvAmount);
						preparedStatement.addBatch();
						// execute insert SQL stetement
						//preparedStatement.executeUpdate();
					}
				}
				preparedStatement.executeBatch();
				updateExistedRecordStmt.executeBatch();
				if (appModSumIds.size() > 0) 
				{
					for(Long id : appModSumIds) 
					{
						String dvCountAmtQuery = "select * from t_app_module_summary where id = "
								+ id;
						dvStmt = conn.createStatement();
						dvRS = dvStmt.executeQuery(dvCountAmtQuery);
						Long dvCount = 0L;
						BigDecimal dvAmount = new BigDecimal("0.0");
						String date = "";
						while (dvRS.next()) 
						{
							date = dvRS.getString("file_date");
						}
						HashMap getDVMap = (HashMap) dvMap.get(date);
						if(getDVMap != null)
						{
							dvCount = Long.parseLong(getDVMap.get("count").toString());
							dvAmount = new BigDecimal(getDVMap.get("amount").toString());
						}

						String updateQuery = "update t_app_module_summary set type_count = 0, type_amt = 0.0, dv_count = "
								+ dvCount
								+ ", dv_amt = "
								+ dvAmount
								+ ", last_updated_date = '"
								+ timeStamp
								+ "', last_updated_by = "
								+ userId
								+ " where id = " + id;
						updateExistedRecordStmt.addBatch(updateQuery);
						/*updateExistedRecordStmt = conn.createStatement();*/
						/*updateExistedRecordStmt.executeUpdate(updateQuery);*/
					}
				}
				updateExistedRecordStmt.executeBatch();
			}
		} 
		catch (Exception e)
		{
			log.info("Exception while updating count and amounts in app_module_summary: "+ e);
		} finally {
			// ResultSet
			if (countAmntsRS != null)
				countAmntsRS.close();
			if (appModSumIdsRS != null)
				appModSumIdsRS.close();
			if (checkRecordRS != null)
				checkRecordRS.close();
			if (dvRS != null)
				dvRS.close();
			if (multiCurrencyRS != null)
				multiCurrencyRS.close();
			// Statement
			if (countAmntsStmt != null)
				countAmntsStmt.close();
			if (appModSumIdsStmt != null)
				appModSumIdsStmt.close();
			if (checkRecordStmt != null)
				checkRecordStmt.close();
			if(preparedStatement != null)
				preparedStatement.close();
			if (updateExistedRecordStmt != null)
				updateExistedRecordStmt.close();
			if (dvStmt != null)
				dvStmt.close();
			if (multiCurrencyStmt != null)
				multiCurrencyStmt.close();
			// Connection
			if (conn != null)
				conn.close();
		}
		return finalMap;
	}

	public HashMap updateAppModuleSummaryForTarget(String amountQualifier,
			Long viewId, Long groupId, String module, String type,
			String status, Long userId, Long tenantId, String periodFactor)
			throws SQLException, ClassNotFoundException {
		log.info("Updating counts and amounts in app_module_summary table for target...");
		HashMap finalMap = new HashMap();
		Connection conn = null;
		Statement countAmntsStmt = null;
		ResultSet countAmntsRS = null;

		PreparedStatement appModSumIdsStmt = null;
		ResultSet appModSumIdsRS = null;

		Statement multiCurrencyStmt = null;
		ResultSet multiCurrencyRS = null;

		PreparedStatement checkRecordStmt = null;
		ResultSet checkRecordRS = null;
		PreparedStatement preparedStatement = null;

		Statement dvStmt = null;
		ResultSet dvRS = null;

		Statement updateExistedRecordStmt = null;

		String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

		try {
			DataSource ds = (DataSource) ApplicationContextProvider.getApplicationContext().getBean("dataSource");
			conn = ds.getConnection();
			DataViews dv = dataViewsRepository.findOne(viewId.longValue());

			// Fetching app_module_summary ids based on status
			String appModSumIdsQuery = "select id from t_app_module_summary where rule_group_id = "
					+ groupId
					+ " and view_id = "
					+ viewId
					+ " and module = 'RECONCILIATION' and type = 'TARGET'";
			List<Long> appModSumIds = new ArrayList<Long>();
			appModSumIdsStmt = conn.prepareStatement(appModSumIdsQuery);
			appModSumIdsRS = appModSumIdsStmt.executeQuery();

			while(appModSumIdsRS.next()) 
			{
				appModSumIds.add(appModSumIdsRS.getLong(1));
			}
			if(dv != null) 
			{
				String dvQuery = "select Date(`" + periodFactor
						+ "`), count(*), sum(`" + amountQualifier + "`) from `"
						+ dv.getDataViewName().toLowerCase()
						+ "` group by Date(`" + periodFactor + "`)";
				dvStmt = conn.createStatement();
				dvRS = dvStmt.executeQuery(dvQuery);
				HashMap dvMap = new HashMap();
				while(dvRS.next()) 
				{
					HashMap dateMap = new HashMap();
					dateMap.put("count", dvRS.getString(2));
					dateMap.put("amount", dvRS.getString(3));
					dvMap.put(dvRS.getString(1), dateMap);
				}
				String query = "select Date(dv.`"
						+ periodFactor
						+ "`), count(*), sum(dv.`"
						+ amountQualifier
						+ "`), "
						+ "    recon.reconciliation_rule_id, recon.reconciliation_rule_group_id"
						+ "    from t_reconciliation_result recon,"
						+ "    `"
						+ dv.getDataViewName().toLowerCase()
						+ "` dv"
						+ "	   where reconciliation_rule_group_id = "
						+ groupId
						+ " "
						+ "		and target_view_id = "
						+ viewId
						+ ""
						+ "    and current_record_flag is true"
						+ "		and recon.target_row_id = dv.scrIds  "
						+ "    and recon_status = 'RECONCILED' "
						+ "    group by Date(`"
						+ periodFactor
						+ "`),reconciliation_rule_id, reconciliation_rule_group_id";
				countAmntsStmt = conn.createStatement();
				countAmntsRS = countAmntsStmt.executeQuery(query);
				
				String insertTableSQL = "INSERT INTO t_app_module_summary"
						+ "(file_date, module, rule_group_id, rule_id, view_id, type, type_count, created_date, last_updated_date, type_amt, dv_count, dv_amt) VALUES"
						+ "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				preparedStatement = conn.prepareStatement(insertTableSQL);
				updateExistedRecordStmt = conn.createStatement();
				while(countAmntsRS.next()) 
				{
					String date = countAmntsRS.getString(1);
					Long count = Long.parseLong(countAmntsRS.getString(2));
					BigDecimal amount = new BigDecimal(countAmntsRS.getString(3));
					Long ruleId = Long.parseLong(countAmntsRS.getString(4));
					LocalDate localDate = LocalDate.parse(date);
					HashMap getDVMap = (HashMap) dvMap.get(date);
					Long dvCount = Long.parseLong(getDVMap.get("count").toString());
					BigDecimal dvAmount = new BigDecimal(getDVMap.get("amount").toString());
					// Check to record exist in app_module_summary table or not
					String checkRecordquery = "select * from t_app_module_summary where file_date = '"
							+ date
							+ "' and module = 'RECONCILIATION' and rule_group_id = "
							+ groupId
							+ " and rule_id = "
							+ ruleId
							+ " and view_id = "
							+ viewId
							+ " and type = 'TARGET'";

					checkRecordStmt = conn.prepareStatement(checkRecordquery);
					checkRecordRS = checkRecordStmt.executeQuery();
					int size = 0;
					Long id = null;
					while(checkRecordRS.next())
					{
						id = Long.parseLong(checkRecordRS.getString("id"));
						size = size + 1;
					}
					if(size > 0)
					{
						if (size == 1) 
						{
							// Updating existing record
							String updateQuery = "update t_app_module_summary set type_count = "
									+ count
									+ ", type_amt = "
									+ amount
									+ ", dv_count = "
									+ dvCount
									+ ", dv_amt = "
									+ dvAmount
									+ ", last_updated_date = '"
									+ timeStamp
									+ "', last_updated_by = "
									+ userId + " where id = " + id;
							updateExistedRecordStmt.addBatch(updateQuery);
/*							updateExistedRecordStmt.executeUpdate(updateQuery);*/
							appModSumIds.remove(id); // Removing updated Record
						} 
					} 
					else 
					{
						preparedStatement.setDate(1,java.sql.Date.valueOf(date));
						preparedStatement.setString(2, "RECONCILIATION");
						preparedStatement.setLong(3, groupId);
						if(ruleId != null)
						{
							preparedStatement.setLong(4, ruleId);
						} 
						else 
						{
							preparedStatement.setNull(4, java.sql.Types.INTEGER);
						}
						preparedStatement.setLong(5, viewId);
						preparedStatement.setString(6, "TARGET");
						preparedStatement.setLong(7, count);
						preparedStatement.setTimestamp(8,new java.sql.Timestamp(System.currentTimeMillis()));
						preparedStatement.setTimestamp(9,new java.sql.Timestamp(System.currentTimeMillis()));
						preparedStatement.setBigDecimal(10, amount);
						preparedStatement.setLong(11, dvCount);
						preparedStatement.setBigDecimal(12, dvAmount);
						preparedStatement.addBatch();
						// execute insert SQL stetement
						// preparedStatement.executeUpdate();
					}
				}
				preparedStatement.executeBatch();
				updateExistedRecordStmt.executeBatch();
				if (appModSumIds.size() > 0)
				{
					for(Long id : appModSumIds) 
					{
						String dvCountAmtQuery = "select * from t_app_module_summary where id = "+ id;
						dvStmt = conn.createStatement();
						dvRS = dvStmt.executeQuery(dvCountAmtQuery);
						Long dvCount = 0L;
						BigDecimal dvAmount = new BigDecimal("0.0");
						String date = "";
						while(dvRS.next()) 
						{
							date = dvRS.getString("file_date");
						}
						HashMap getDVMap = (HashMap) dvMap.get(date);
						if (getDVMap != null)
						{
							dvCount = Long.parseLong(getDVMap.get("count").toString());
							dvAmount = new BigDecimal(getDVMap.get("amount").toString());
						}
						String updateQuery = "update t_app_module_summary set type_count = 0, type_amt = 0.0, dv_count = "
								+ dvCount
								+ ", dv_amt = "
								+ dvAmount
								+ ", last_updated_date = '"
								+ timeStamp
								+ "', last_updated_by = "
								+ userId
								+ " where id = " + id;
						updateExistedRecordStmt.addBatch(updateQuery);
/*						updateExistedRecordStmt = conn.createStatement();
						updateExistedRecordStmt.executeUpdate(updateQuery);*/
					}
				}
				updateExistedRecordStmt.executeBatch();
			}
		} 
		catch(Exception e)
		{
			log.info("Exception while updating count and amounts in app_module_summary: "+ e);
		} 
		finally 
		{
			// ResultSet
			if (countAmntsRS != null)
				countAmntsRS.close();
			if (appModSumIdsRS != null)
				appModSumIdsRS.close();
			if (checkRecordRS != null)
				checkRecordRS.close();
			if (dvRS != null)
				dvRS.close();
			if (multiCurrencyRS != null)
				multiCurrencyRS.close();
			// Statement
			if (countAmntsStmt != null)
				countAmntsStmt.close();
			if (appModSumIdsStmt != null)
				appModSumIdsStmt.close();
			if (checkRecordStmt != null)
				checkRecordStmt.close();
			if(preparedStatement != null)
				preparedStatement.close();
			if (updateExistedRecordStmt != null)
				updateExistedRecordStmt.close();
			if (multiCurrencyStmt != null)
				multiCurrencyStmt.close();
			if (dvStmt != null)
				dvStmt.close();
			// Connection
			if (conn != null)
				conn.close();
		}

		return finalMap;
	}

	public HashMap updateAppModuleSummaryInfoSource(String amountQualifier,
			String viewName, Long groupId, Long viewId, String status,
			Long ruleId, Long userId) throws ClassNotFoundException,
			SQLException {
		System.out
				.println("Updating count and amounts for for Source Reconciliation...");
		HashMap finalMap = new HashMap();
		Connection conn = null;
		ResultSet countsAmountsRS = null;
		PreparedStatement fetchCountAmntsStmt = null;
		PreparedStatement checkRecordStmt = null;
		Statement updateStament = null;
		ResultSet checkRecordRS = null;

		PreparedStatement appModSumIdsStmt = null;
		ResultSet appModSumIdsRS = null;
		Statement updateExistedRecordStmt = null;

		try {
			String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(new Date());
			// Fetching app_module_summary ids based on status
			List<Long> appSummaryIds = new ArrayList<Long>();
			String appModSumIdsQuery = "select id from t_app_module_summary where module = 'Reconciliation' and rule_group_id = "
					+ groupId
					+ " and view_id = "
					+ viewId
					+ " and type = 'Source'";
			appModSumIdsStmt = conn.prepareStatement(appModSumIdsQuery);
			appModSumIdsRS = appModSumIdsStmt.executeQuery();

			while (appModSumIdsRS.next()) {
				appSummaryIds.add(appModSumIdsRS.getLong(1));
			}
			System.out.println("AppModuleSummaryIds for " + status
					+ " status: " + appSummaryIds);

			String countsAmntsQuery = "select Date(grp.`fileDate`), count(*), sum(grp.`"
					+ amountQualifier
					+ "`), grp.reconciliation_rule_id from ("
					+ " 	select dv.*, recon.recon_status,"
					+ "     recon.reconciliation_rule_id"
					+ "     from t_reconciliation_result recon,"
					+ "		t_rule_group_details rgd, "
					+ "     t_rules rl, "
					+ "     "
					+ viewName
					+ " dv"
					+ " 	where reconciliation_rule_group_id = "
					+ groupId
					+ " "
					+ " 	and original_view_id = "
					+ viewId
					+ ""
					+ " 	and current_record_flag is true"
					+ "		and reconciliation_rule_group_id = rgd.rule_group_id"
					+ "		and rgd.rule_id = rl.id "
					+ "		and recon.reconciliation_rule_id = rl.id"
					+ "		and recon.original_row_id = dv.scrIds ) grp"
					+ "		where grp.recon_status = '"
					+ status
					+ "' and grp.reconciliation_rule_id = "
					+ ruleId
					+ " group by Date(grp.`fileDate`), grp.reconciliation_rule_id";
			fetchCountAmntsStmt = conn.prepareStatement(countsAmntsQuery);
			countsAmountsRS = fetchCountAmntsStmt.executeQuery();

			while (countsAmountsRS.next()) {
				String date = countsAmountsRS.getString(1).toString();
				Long count = Long.parseLong(countsAmountsRS.getString(2)
						.toString());
				Double amount = Double.parseDouble(countsAmountsRS.getString(3)
						.toString());
				BigDecimal bdAmount = new BigDecimal(amount);
				bdAmount = bdAmount.setScale(2, BigDecimal.ROUND_HALF_UP);

				String query = "select * from t_app_module_summary where file_date = '"
						+ date
						+ "' and module = 'Reconciliation' and rule_group_id = "
						+ groupId
						+ " and rule_id = "
						+ ruleId
						+ " and view_id = " + viewId + " and type = 'Source'";

				checkRecordStmt = conn.prepareStatement(query);
				checkRecordRS = checkRecordStmt.executeQuery();
				int size = 0;
				Long id = null;
				while (checkRecordRS.next()) {
					id = Long.parseLong(checkRecordRS.getString("id"));
					size = size + 1;
				}
				if (size > 0) {
					if (size == 1) {
						String updateQuery = "UPDATE t_app_module_summary SET type_count = "
								+ count
								+ ", type_amt = "
								+ bdAmount
								+ ", last_updated_date = '"
								+ timeStamp
								+ "', last_updated_by = "
								+ userId
								+ " where id = " + id;
						updateStament = conn.createStatement();
						updateStament.executeUpdate(updateQuery);
						appSummaryIds.remove(id);
					} else {
						System.out
								.println("Duplicate records exist while updating count and amounts for view id: "
										+ viewId
										+ ", group id: "
										+ groupId
										+ ", rule id: "
										+ ruleId
										+ ", date: "
										+ date);
					}
				} else {
					log.info("Amount::: " + bdAmount);
					System.out
							.println("Need to insert record once data transmission has been completed for "
									+ groupId
									+ ", viewId: "
									+ viewId
									+ ", date: "
									+ date
									+ ", ruleId: "
									+ ruleId
									+ ", status: "
									+ status
									+ ", module: Reconciliation");
					String insertTableSQL = "INSERT INTO t_app_module_summary"
							+ "(file_date, module, rule_group_id, rule_id, view_id, type, type_count, created_date, last_updated_date, type_amt) VALUES"
							+ "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
					PreparedStatement preparedStatement = conn
							.prepareStatement(insertTableSQL);
					preparedStatement.setDate(1, java.sql.Date.valueOf(date));
					preparedStatement.setString(2, "Reconciliation");
					preparedStatement.setLong(3, groupId);
					if (ruleId != null) {
						preparedStatement.setLong(4, ruleId);
					} else {
						preparedStatement.setNull(4, java.sql.Types.INTEGER);
					}
					preparedStatement.setLong(5, viewId);
					preparedStatement.setString(6, "Source");
					preparedStatement.setLong(7, count);
					preparedStatement.setTimestamp(8, new java.sql.Timestamp(
							System.currentTimeMillis()));
					preparedStatement.setTimestamp(9, new java.sql.Timestamp(
							System.currentTimeMillis()));
					preparedStatement.setBigDecimal(10, bdAmount);
					// execute insert SQL stetement
					preparedStatement.executeUpdate();
				}
			}

			System.out.println("AppModuleSummaryIds After removing ids: "
					+ appSummaryIds);
			if (appSummaryIds.size() > 0) {
				for (Long id : appSummaryIds) {
					String updateQuery = "update t_app_module_summary set type_count = 0, type_amt = 0.0, last_updated_date = '"
							+ timeStamp
							+ "', last_updated_by = "
							+ userId
							+ " where id = " + id;
					updateExistedRecordStmt = conn.createStatement();
					updateExistedRecordStmt.executeUpdate(updateQuery);
				}
				System.out
						.println("Updated Remainig Records: " + appSummaryIds);
			}
		} catch (Exception e) {
			System.out.println("Exception while updating count and amounts: "
					+ e);
		} finally {
			try {

				// ResultSet
				if (countsAmountsRS != null)
					countsAmountsRS.close();
				if (checkRecordRS != null)
					checkRecordRS.close();
				if (appModSumIdsRS != null)
					appModSumIdsRS.close();
				// Statemnt
				if (fetchCountAmntsStmt != null)
					fetchCountAmntsStmt.close();
				if (checkRecordStmt != null)
					checkRecordStmt.close();
				if (updateStament != null)
					updateStament.close();
				if (appModSumIdsStmt != null)
					appModSumIdsStmt.close();
				if (updateExistedRecordStmt != null)
					updateExistedRecordStmt.close();
				// Connection
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				System.out.println("Exception while closing statements");
			}
		}
		return finalMap;
	}

	@Transactional
	public HashMap getOriginalIdsByTransDateApprovals(Long viewId,
			String rangeFrom, String rangeTo, String periodFactor,
			List<BigInteger> srcIds, String amountQualifier)
			throws SQLException, ClassNotFoundException {
		List<BigInteger> viewOriginalIds = new ArrayList<BigInteger>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet result = null;
		HashMap finalMap = new HashMap();
		String srcId = srcIds.toString().replaceAll("\\[", "")
				.replaceAll("\\]", "").replaceAll("\\s", "");

		try {
			DataSource ds = (DataSource) ApplicationContextProvider
					.getApplicationContext().getBean("dataSource");
			conn = ds.getConnection();
			stmt = conn.createStatement();
			DataViews dv = dataViewsRepository.findOne(viewId.longValue());
			if (dv != null) {
				String viewName = dv.getDataViewName();
				String query = "";
				if ("fileDate".equalsIgnoreCase(periodFactor)) {
					query = "SELECT count(scrIds),SUM(`" + amountQualifier
							+ "`) FROM `" + viewName.toLowerCase()
							+ "` where scrIds in (" + srcId
							+ ") and Date(fileDate) between '" + rangeFrom
							+ "' AND '" + rangeTo + "'";
				} else if ("dateQualifier".equalsIgnoreCase(periodFactor)) {
					String dateQualifier = getViewColumnQualifier(
							new BigInteger(viewId.toString()), "TRANSDATE");
					if (dateQualifier.length() > 0) {
						query = "SELECT count(scrIds),SUM(`" + amountQualifier
								+ "`) FROM `" + viewName.toLowerCase()
								+ "` where scrIds in (" + srcId
								+ ") and Date(`" + dateQualifier
								+ "`) between '" + rangeFrom + "' AND '"
								+ rangeTo + "'";
					}
				}
				result = stmt.executeQuery(query);
				while (result.next()) {
					finalMap.put("count", result.getString(1));
					finalMap.put("amount", result.getString(2));
				}
			} else {
				log.info("View doen't not exist for the view id: " + viewId);
			}
		} catch (SQLException se) {
			log.info("Error while executing query: " + se);
		} catch (Exception e) {
			log.info("Exception while getting databse properties");
		} finally {
			if (result != null)
				result.close();
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();
		}
		log.info("Ids Size: " + viewOriginalIds.size());
		return finalMap;
	}
	
	public String jsonToCSV(List<LinkedHashMap> finalList, String newFileName)
			throws IOException {
		LinkedHashMap keysList = finalList.get(0);
		List<String> keyList = new ArrayList<String>();
		Set<String> keyset = keysList.keySet();
		log.info("keyset :" + keyset.remove("Id"));

		String commaSeparated = keyset.stream()
				.collect(Collectors.joining(","));

		HashMap pathMap = new HashMap();

		// String newFileName =
		// prog.getGeneratedPath()+rg.getName().replaceAll("\\s","")+"_"+updRes+".csv";

		try (

		BufferedWriter writer = Files.newBufferedWriter(Paths.get(newFileName));

				CSVPrinter csvPrinter = new CSVPrinter(writer,
						CSVFormat.DEFAULT.withHeader(commaSeparated));) {

			for (int j = 0; j < finalList.size(); j++) {
				List<String> valuesList = new ArrayList<String>();
				for (String hea : keyset) {
					// log.info("hea: "+hea);
					if (finalList.get(j).containsKey(hea)) {
						// log.info("values.get(j).get(hea): "+values.get(j).get(hea));
						if (finalList.get(j).get(hea) != null)
							valuesList
									.add(finalList.get(j).get(hea).toString());
						else
							valuesList.add("");
					} else {
						valuesList.add("");
					}
				}

				commaSeparated = valuesList.stream().collect(
						Collectors.joining(","));

				csvPrinter.printRecord(commaSeparated);
			}
			csvPrinter.flush();
		}

		return newFileName;
	}

	public List<BigInteger> getRowIdsForChildData(String srcOrTrgt,
			Long viewId, Long groupId, Long tenantId, String reconReference) {
		List<BigInteger> rowIds = new ArrayList<BigInteger>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet result = null;
		try {
			DataViews dv = dataViewsRepository.findById(viewId);
			String query = "";
			if ("source".equalsIgnoreCase(srcOrTrgt)) {
				query = "select distinct original_row_id from t_reconciliation_result rs,"
						+ "	`"
						+ dv.getDataViewName().toLowerCase()
						+ "` dv where recon_reference in('"
						+ reconReference
						+ "') and "
						+ " original_view_id = "
						+ viewId
						+ " and target_view_id is null and  "
						+ " upper(IFNULL(adjustmentType,'X'))!='VARIANCE' and rs.original_row_id = dv.scrIds  "
						+ "  and tenant_id = "
						+ tenantId
						+ " and reconciliation_rule_group_id = " + groupId;
			} else {
				query = "select distinct target_row_id from "
						+ " t_reconciliation_result rs,	" + " `"
						+ dv.getDataViewName().toLowerCase() + "` dv where "
						+ " recon_reference in('" + reconReference
						+ "') and target_view_id = " + viewId
						+ " and original_view_id is null "
						+ " and upper(IFNULL(adjustmentType,'X'))!='VARIANCE' "
						+ " and tenant_id = " + tenantId
						+ " and reconciliation_rule_group_id = " + groupId + ""
						+ " and rs.target_row_id = dv.scrIds";
			}
			DataSource ds = (DataSource) ApplicationContextProvider
					.getApplicationContext().getBean("dataSource");
			conn = ds.getConnection();
			stmt = conn.createStatement();
			result = stmt.executeQuery(query);
			while (result.next()) {
				if (result.getString(1) != null) {
					rowIds.add(new BigInteger(result.getString(1).toString()));
				}
			}
		} catch (Exception e) {
			log.info("Exception while fetching row ids for child data in reconciliation"
					+ e);
		} finally {
			try {
				if (result != null)
					result.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				log.info("Exception while closing jdbc statements.(Fetching child row ids)"
						+ e);
			}
		}
		return rowIds;
	}
	
	public HashMap getReconciledDataWithReconRef(Long tenantId, Long groupId, Long sViewId, Long tViewId, String periodFactor, String rangeFrom, String rangeTo,
			Long pageIndx, Long pageSize, String srcGrpCols, String approvalLeftJoin, String filterQuery, String searchQuery, 
			String sortQuery, String ascOrDesc, HashMap grpCols, String dvGrpCols, String dv2GrpCols) throws NumberFormatException, SQLException
	{
		HashMap finalMap = new HashMap();
		Connection conn = null;
		Statement stmt = null;
		ResultSet result = null;
		
		Statement totalCountStmt = null;
		ResultSet totalCountRS = null;
		Long totalCount = 0L;
		List<HashMap> finalList = new ArrayList<HashMap>();
		
		try{
		
			DataSource ds = (DataSource) ApplicationContextProvider.getApplicationContext().getBean("dataSource");
			conn = ds.getConnection();
		
			DataViews sdv = dataViewsRepository.findById(sViewId);
			DataViews tdv = dataViewsRepository.findById(tViewId);
			
			String withPaginationGrouping = "SELECT recon_src.recon_reference, "
					+ srcGrpCols
					+ "       recon_src.src_count, "
					+ "       recon_src.src_amount_sum, "
					+ "       recon_src.src_tolerance_amount, "
					+ "       recon_tr.tr_count, "
					+ "       recon_tr.tr_amount_sum, "
					+ "       recon_tr.tr_tolerance_amount ";
			String withoutPaginationGrouping = "SELECT COUNT(*) COUNT ";
			String withPagination =  searchQuery+" ORDER BY `recon_reference` "+ascOrDesc+" LIMIT " + pageIndx + ", " + pageSize;
			
			String query = " FROM (SELECT recon_reference, "
					+  dvGrpCols
					+ "               Count(*)                    src_count, "
					+ "               Sum(ifnull(recon.original_amount, 0))  src_amount_sum, "
					+ "               Sum(ifnull(recon.tolerance_amount, 0)) src_tolerance_amount "
					+ "        FROM   t_reconciliation_result recon "
					+ " LEFT JOIN t_rules rl ON recon.reconciliation_rule_id = rl.id "
					+ approvalLeftJoin
					+ ",               `"+sdv.getDataViewName().toLowerCase()+"` dv "
					+ "        WHERE  reconciliation_rule_group_id = "+groupId+" "
					+ "               AND Date(dv.`"+periodFactor+"`) BETWEEN '"+rangeFrom+"' AND '"+rangeTo+"' "
					+ "               AND original_view_id = "+sViewId+" "
					+ "               AND current_record_flag IS TRUE "
					+ "               AND recon.original_row_id = dv.scrids "
					+ "               AND recon_status = 'RECONCILED' "
					+ "               AND recon.tenant_id = "+tenantId+" "
					+ 	filterQuery
					+ "        GROUP BY "
					+ " "+dv2GrpCols+"recon_reference) recon_src "
					+ "       JOIN (SELECT recon_reference, "
					+ "                   reconciliation_rule_id, "
					+ "                    Count(*)                    tr_count, "
					+ "                    Sum(ifnull(recon.target_amount, 0))    tr_amount_sum, "
					+ "                    Sum(ifnull(recon.tolerance_amount, 0)) tr_tolerance_amount "
					+ "             FROM   t_reconciliation_result recon "
					+ "                    LEFT JOIN t_rules rl ON recon.reconciliation_rule_id = rl.id "
					+ approvalLeftJoin
					+ ",                    `"+tdv.getDataViewName().toLowerCase()+"` dv "
					+ "             WHERE  reconciliation_rule_group_id = "+groupId+" "
					+ "                    AND target_view_id = "+tViewId+" "
					+ "                    AND current_record_flag IS TRUE "
					+ "                    AND recon.target_row_id = dv.scrids "
					+ "                    AND recon_status = 'RECONCILED' "
					+ "                    AND recon.tenant_id = "+tenantId+" "
					+ "             GROUP  BY recon_reference, "
					+ "                       reconciliation_rule_id) recon_tr " 
					+ "         ON recon_src.recon_reference = recon_tr.recon_reference ";
			
			String withLimit = withPaginationGrouping+" "+ query +" "+withPagination;
			String withOutLimit = withoutPaginationGrouping+ " "+ query + searchQuery;
			log.info("Query to reconciled reference grouping summary info: "+ withLimit);
			totalCountStmt = conn.createStatement();
			totalCountRS = totalCountStmt.executeQuery(withOutLimit);
			
			while(totalCountRS.next())
			{
				totalCount = totalCount + Long.parseLong(totalCountRS.getString(1).toString());
			}
			stmt = conn.createStatement();
			result = stmt.executeQuery(withLimit);
			
			ResultSetMetaData rsmd = result.getMetaData();
			int colCount = rsmd.getColumnCount();
			
			while(result.next()) 
			{
				HashMap srcMap = new HashMap();
				HashMap trMap = new HashMap();

				HashMap reconRecord = new HashMap();
				reconRecord.put("reconReference", result.getString("recon_reference"));
				
				for(int i = 1; i <= colCount; i++) 
				{
					if(grpCols.get(rsmd.getColumnName(i).toString()) != null)
					{
						reconRecord.put(grpCols.get(rsmd.getColumnName(i).toString()), result.getString(rsmd.getColumnName(i).toString()));
					}
				}				
				/*reconRecord.put("ruleId", Long.parseLong(result.getString("reconciliation_rule_id")));*/
				reconRecord.put("srcCount", Long.parseLong(result.getString("src_count")));
				reconRecord.put("srcAmount", Double.parseDouble(result.getString("src_amount_sum")));
				reconRecord.put("srcTolAmount", Double.parseDouble(result.getString("src_tolerance_amount")));
				
				reconRecord.put("trCount", Long.parseLong(result.getString("tr_count")));
				reconRecord.put("trAmount", Double.parseDouble(result.getString("tr_amount_sum")));
				reconRecord.put("trTolAmount", Double.parseDouble(result.getString("tr_tolerance_amount")));
			
				finalList.add(reconRecord);
			}
		}
		catch(Exception e)
		{
			log.info("Exception while fetching reconciled recon grouping summar info : "+e);
		}
		finally
		{
			try
			{
				if(result != null)
					result.close();
				if(totalCountRS != null)
					totalCountRS.close();
				if (stmt != null)
					stmt.close();	
				if (totalCountStmt != null)
					totalCountStmt.close();
				if (conn != null)
					conn.close();
				} 
			catch(Exception e)
			{
				log.info("Exception while closing jdbc statements.(Grouping ReconRefId Info)"+ e);
			}
		}
		finalMap.put("result", finalList);
		HashMap info = new HashMap();
		info.put("totalCount", totalCount);
		finalMap.put("info", info);
		return finalMap;
	}
	
	public HashMap getReconRefTranscations(Long tenantId, Long groupId, Long sViewId, Long tViewId, String periodFactor, String rangeFrom, String rangeTo,
			Long pageIndx, Long pageSize, String sGroupByValues, String groupBy, String sGroupingColumnName, String ascOrDesc, String searchQuery, 
			/*String currenciesString, */String sortQuery) throws SQLException
	{
		List<LinkedHashMap> finalList = new ArrayList<LinkedHashMap>();
		HashMap finalMap = new HashMap();
		Connection conn = null;
		Statement stmt = null;
		ResultSet result = null;
		
		Statement totalCountStmt = null;
		ResultSet totalCountRS = null;
		Long totalCount = 0L;
		
		HashMap apprStatusLookups = new HashMap();
		List<LookUpCode> lcs = lookUpCodeRepository.findByTenantIdAndLookUpType(tenantId, "APPROVAL_STATUS");
		for(LookUpCode lc : lcs) 
		{
			if("IN_PROCESS".equalsIgnoreCase(lc.getLookUpCode()))
			{
				apprStatusLookups.put(lc.getLookUpCode().toString(), "");
			}
			else
			{
				apprStatusLookups.put(lc.getLookUpCode(), lc.getMeaning());
			}
		}
/*		String currQualifier = reconciliationResultService.getViewColumnQualifier(BigInteger.valueOf(sViewId), "CURRENCYCODE");
		String currenciesQuery = "";
		if(currenciesString.length()>0)
		{
			currenciesQuery = " AND dv.`"+currQualifier +"` IN ("+currenciesString+")";
		}*/
		
		try{
			String groupingField = "";
			String sGroupingColumn = "";
			// String tGroupingColumn = "";
			String approvalLeftJoin = ", ";
			if("rules".equalsIgnoreCase(groupBy))
			{
				groupingField = "(CASE WHEN recon_src.grp_column IS NULL THEN 'Manual' ELSE recon_src.grp_column end) ";
				sGroupingColumn = "rl.rule_code";
			//	tGroupingColumn = "rl.rule_code";
			}
			else if("batch".equalsIgnoreCase(groupBy))
			{
				groupingField = "(CASE WHEN Substring(recon_src.grp_column, 1, 7) = 'MANUAL_' 	THEN 'Manual' ELSE recon_src.grp_column end )";
				sGroupingColumn = "recon_job_reference";
			//	tGroupingColumn = "recon_job_reference";
			}
			else if("columnName".equalsIgnoreCase(groupBy))
			{
				groupingField = "recon_src.grp_column";
				sGroupingColumn = "dv."+sGroupingColumnName;
			//	tGroupingColumn = "dv."+tGroupingColumnName;
			}
			else if("approvalRule".equalsIgnoreCase(groupBy))
			{
				groupingField = "recon_src.grp_column";
				sGroupingColumn = "arl.rule_code";
			//	tGroupingColumn = "arl.rule_code";
				approvalLeftJoin = " LEFT JOIN t_rules arl ON recon.approval_rule_id = arl.id, ";
			}
			else if("approvalStatus".equalsIgnoreCase(groupBy))
			{
				groupingField = "recon_src.grp_column";
				sGroupingColumn = "final_status";
			//	tGroupingColumn = "final_status";
			}
			else if("approvalDate".equalsIgnoreCase(groupBy))
			{
				groupingField = "recon_src.grp_column";
				sGroupingColumn = "date(final_action_date)";
			//	tGroupingColumn = "date(final_action_date)";
			}
			else if("days".equalsIgnoreCase(groupBy))
			{
				groupingField = "recon_src.grp_column";
				sGroupingColumn = sGroupingColumnName;
			//	tGroupingColumn = tGroupingColumnName;
			}
			DataSource ds = (DataSource) ApplicationContextProvider.getApplicationContext().getBean("dataSource");
			conn = ds.getConnection();
			
			String withPaginationGrouping = "SELECT recon_src.recon_reference, "
					+ "       recon_src.reconciliation_rule_id, "
					+ groupingField +" grouping_col, "
					+ "       recon_src.src_count, "
					+ "       recon_src.src_amount_sum, "
					+ "       recon_src.src_tolerance_amount, "
					+ "       recon_tr.tr_count, "
					+ "       recon_tr.tr_amount_sum, "
					+ "       recon_tr.tr_tolerance_amount ";
			String withoutPaginationGrouping = "SELECT COUNT(*) COUNT ";
			
			String withPagination =  searchQuery+" ORDER BY "+sortQuery+" "+ascOrDesc+" LIMIT " + pageIndx + ", " + pageSize;
			
			DataViews sdv = dataViewsRepository.findById(sViewId);
			DataViews tdv = dataViewsRepository.findById(tViewId);
			
			 
			String query = " FROM (SELECT recon_reference, "
					+ "               reconciliation_rule_id, "
					+ sGroupingColumn +" grp_column, "
					+ "               Count(*)                    src_count, "
					+ "               Sum(ifnull(recon.original_amount, 0))  src_amount_sum, "
					+ "               Sum(ifnull(recon.tolerance_amount, 0)) src_tolerance_amount "
					+ "        FROM   t_reconciliation_result recon "
					+ " LEFT JOIN t_rules rl ON recon.reconciliation_rule_id = rl.id "
					+ approvalLeftJoin
					+ "               `"+sdv.getDataViewName().toLowerCase()+"` dv "
					+ "        WHERE  reconciliation_rule_group_id = "+groupId+" "
					+ "               AND Date(dv.`"+periodFactor+"`) BETWEEN '"+rangeFrom+"' AND '"+rangeTo+"' "
					+ "               AND original_view_id = "+sViewId+" "
					+ "               AND current_record_flag IS TRUE "
					+ "               AND recon.original_row_id = dv.scrids "
					+ "               AND recon_status = 'RECONCILED' "
					+ "               AND recon.tenant_id = "+tenantId+" "
					+ 	sGroupByValues/*+currenciesQuery*/
					+ "        GROUP  BY recon_reference, "
					+ "                  reconciliation_rule_id, "+sGroupingColumn+") recon_src "
					+ "       JOIN (SELECT recon_reference, "
					+ "                   reconciliation_rule_id, "
					/*+ tGroupingColumn +" grp_column, "*/
					+ "                    Count(*)                    tr_count, "
					+ "                    Sum(ifnull(recon.target_amount, 0))    tr_amount_sum, "
					+ "                    Sum(ifnull(recon.tolerance_amount, 0)) tr_tolerance_amount "
					+ "             FROM   t_reconciliation_result recon "
					+ "                    LEFT JOIN t_rules rl ON recon.reconciliation_rule_id = rl.id "
					+ approvalLeftJoin
					+ "                    `"+tdv.getDataViewName().toLowerCase()+"` dv "
					+ "             WHERE  reconciliation_rule_group_id = "+groupId+" "
					/*+ "                    AND Date(dv.`"+tPeriodFactor+"`) BETWEEN '"+rangeFrom+"' AND '"+rangeTo+"' "*/
					+ "                    AND target_view_id = "+tViewId+" "
					+ "                    AND current_record_flag IS TRUE "
					+ "                    AND recon.target_row_id = dv.scrids "
					+ "                    AND recon_status = 'RECONCILED' "
					+ "                    AND recon.tenant_id = "+tenantId+" "
					/*+ 	tGroupByValues*/
					+ "             GROUP  BY recon_reference, "
					+ "                       reconciliation_rule_id) recon_tr " 
					/* + " , "+tGroupingColumn+") recon_tr "*/
					+ "         ON recon_src.recon_reference = recon_tr.recon_reference ";
				
			String withLimit = withPaginationGrouping+" "+ query +" "+withPagination;
			String withOutLimit = withoutPaginationGrouping+ " "+ query + searchQuery;
			log.info("Query to reconciled reference grouping summary info: "+ withLimit);
			totalCountStmt = conn.createStatement();
			totalCountRS = totalCountStmt.executeQuery(withOutLimit);
			
			while(totalCountRS.next())
			{
				totalCount = totalCount + Long.parseLong(totalCountRS.getString(1).toString());
			}
			
			stmt = conn.createStatement();
			result = stmt.executeQuery(withLimit);
			while(result.next()) 
			{
				HashMap srcMap = new HashMap();
				HashMap trMap = new HashMap();
				
				LinkedHashMap reconRecord = new LinkedHashMap();
				reconRecord.put("reconReference", result.getString("recon_reference"));
				reconRecord.put("ruleId", Long.parseLong(result.getString("reconciliation_rule_id")));
				
				if("approvalStatus".equalsIgnoreCase(groupBy))
				{
					if(result.getString("grouping_col") != null)
					{
						reconRecord.put("groupingCol", apprStatusLookups.get(result.getString("grouping_col").toString()));						
					}
					else
					{
						reconRecord.put("groupingCol", "");
					}
				}
				else
				{
					reconRecord.put("groupingCol", result.getString("grouping_col"));	
				}

				reconRecord.put("srcCount", Long.parseLong(result.getString("src_count")));
				reconRecord.put("srcAmount", Double.parseDouble(result.getString("src_amount_sum")));
				reconRecord.put("srcTolAmount", Double.parseDouble(result.getString("src_tolerance_amount")));
				
				reconRecord.put("trCount", Long.parseLong(result.getString("tr_count")));
				reconRecord.put("trAmount", Double.parseDouble(result.getString("tr_amount_sum")));
				reconRecord.put("trTolAmount", Double.parseDouble(result.getString("tr_tolerance_amount")));
			
				finalList.add(reconRecord);
			}
		}
		catch(Exception e)
		{
			log.info("Exception while fetching reconciled recon grouping summar info : "+e);
		}
		finally
		{
			try
			{
				if(result != null)
					result.close();
				if (stmt != null)
					stmt.close();				
				if (conn != null)
					conn.close();
				} 
			catch(Exception e)
			{
				log.info("Exception while closing jdbc statements.(Grouping ReconRefId Info)"+ e);
			}
		}
		finalMap.put("result", finalList);
		HashMap info = new HashMap();
		info.put("totalCount", totalCount);
		finalMap.put("info", info);
		return finalMap;
	}
	
	
	public List<LinkedHashMap> getGroupByReconRefDetailInfo(Long tenantId,
			Long groupId, Long viewId, String groupByValues, Long pageIndx,
			Long pageSize, String rangeFrom, String rangeTo,
			String periodFactor, String amountQualifier, String sourceOrTarget,
			String typeOfReconData, String sortBy, String columnSearchQuery,
			String statusNReconRefQuery, String groupByField, String dvOrRecon)
			throws SQLException, ClassNotFoundException {
		log.info("Service for fetching reconciled transaction with group by recon reference id start....");
		List<LinkedHashMap> finalList = new ArrayList<LinkedHashMap>();
		LinkedHashMap info = new LinkedHashMap();
		LinkedHashMap recMain = new LinkedHashMap();
		info.put("amountQualifier", amountQualifier);
		Connection conn = null;
		Statement stmt = null;
		Statement stmt2 = null;
		ResultSet result = null;
		ResultSet result2 = null;
		String reconResultTable = "";
		if ("suggestion".equalsIgnoreCase(typeOfReconData)) 
		{
			reconResultTable = "t_reconciliation_suggestion_result";
		} 
		else {
			reconResultTable = "t_reconciliation_result";
		}
		try {
			HashMap apprStatusLookups = new HashMap();
			List<LookUpCode> lcs = lookUpCodeRepository.findByTenantIdAndLookUpType(tenantId, "APPROVAL_STATUS");
			for(LookUpCode lc : lcs) 
			{
				if("IN_PROCESS".equalsIgnoreCase(lc.getLookUpCode()))
				{
					apprStatusLookups.put(lc.getLookUpCode(), "");
				}
				else
				{
					apprStatusLookups.put(lc.getLookUpCode(), lc.getMeaning());
				}
			}
			DataSource ds = (DataSource) ApplicationContextProvider.getApplicationContext().getBean("dataSource");
			conn = ds.getConnection();
			String withLimit = "";
			String withoutLimit = "";
			DataViews dv = dataViewsRepository.findById(viewId.longValue());
			String groupingField = "";
			String innerGroupingField = "";
			String ruleCodeQuery = " recon.rule_code ";
			String ruleCodeColumn = "reconciliation_rule_id";
			if("final_action_date".equalsIgnoreCase(groupByField)) 
			{
				groupingField = ", Date(recon.final_action_date)";
				innerGroupingField = ", " + groupByField;
			} 
			else if("reconciliation_rule_id".equalsIgnoreCase(groupByField)) 
			{
				groupingField = "";
				innerGroupingField = "";
				ruleCodeQuery = " (case when recon.rule_code is null then 'Manual' else recon.rule_code end) rule_code ";
			} 
			else if("approval_rule_id".equalsIgnoreCase(groupByField)) 
			{
				groupingField = ", recon." + groupByField + "";
				if ("dv".equalsIgnoreCase(dvOrRecon)) {
					innerGroupingField = ", dv." + groupByField + "";
				} else {
					innerGroupingField = ", " + groupByField;
				}
				ruleCodeColumn = "approval_rule_id";
			} 
			else{
				if ("recon_job_reference".equalsIgnoreCase(groupByField)) {
					groupingField = ", (CASE WHEN Substring(recon.recon_job_reference, 1, 7) = 'MANUAL_' 	THEN 'Manual' ELSE recon.recon_job_reference end ) recon_job_reference";
				} else {
					groupingField = ", recon." + groupByField + "";
				}
				if ("dv".equalsIgnoreCase(dvOrRecon)) {
					innerGroupingField = ", dv." + groupByField + "";
				} else {
					innerGroupingField = ", " + groupByField;
				}
			}
			if(dv != null) 
			{
				String query = "";
				if ("source".equalsIgnoreCase(sourceOrTarget)) 
				{
					query = "select recon.recon_reference,recon.reconciliation_rule_id,recon.count,recon.amount_sum, recon.tolerance_amount, "
							+ ruleCodeQuery
							+ " "
							+ groupingField
							+ " from "
							+ " (select recon_reference, reconciliation_rule_id, rl.rule_code "
							+ innerGroupingField
							+ ", count(*) count,  sum(dv.`"
							+ amountQualifier
							+ "`)  amount_sum, sum(tolerance_amount) tolerance_amount "
							+ " from "
							+ reconResultTable
							+ " recon"
							+ " LEFT JOIN t_rules rl ON recon."
							+ ruleCodeColumn
							+ " = rl.id,"
							+ " `"
							+ dv.getDataViewName().toLowerCase()
							+ "` dv "
							+ "	where reconciliation_rule_group_id = "
							+ groupId
							+ " "
							+ " and Date(dv.`"
							+ periodFactor
							+ "`) between '"
							+ rangeFrom
							+ "' and '"
							+ rangeTo
							+ "'"
							+ "	and original_view_id = "
							+ viewId
							+ ""
							+ "	and current_record_flag is true"
							+ "	and recon.original_row_id = dv.scrIds"
							+ "	and recon_status = 'RECONCILED'"
							+ " and recon.tenant_id = "
							+ tenantId
							+ ""
							+ " "
							+ groupByValues
							+ " "
							+ columnSearchQuery
							+ statusNReconRefQuery
							+ "	group by recon_reference, reconciliation_rule_id "
							+ innerGroupingField
							+ ""
							+ " ) recon"
							+ " order by recon.recon_reference " + sortBy + " ";
				}
				else if ("target".equalsIgnoreCase(sourceOrTarget)) 
				{
					query = "select recon.recon_reference,recon.reconciliation_rule_id,recon.count,recon.amount_sum,recon.tolerance_amount, "
							+ ruleCodeQuery
							+ " "
							+ groupingField
							+ ""
							+ " from "
							+ " (select recon_reference, reconciliation_rule_id, rl.rule_code "
							+ innerGroupingField
							+ ", count(*) count, sum(dv.`"
							+ amountQualifier
							+ "`) amount_sum, sum(tolerance_amount) tolerance_amount"
							+ "	from "
							+ reconResultTable
							+ " recon"
							+ " LEFT JOIN t_rules rl ON recon."
							+ ruleCodeColumn
							+ " = rl.id,"
							+ " `"
							+ dv.getDataViewName().toLowerCase()
							+ "` dv "
							+ "	where reconciliation_rule_group_id = "
							+ groupId
							+ " "
							+ " and Date(dv.`"
							+ periodFactor
							+ "`) between '"
							+ rangeFrom
							+ "' and '"
							+ rangeTo
							+ "'"
							+ "	and target_view_id = "
							+ viewId
							+ ""
							+ "	and current_record_flag is true"
							+ "	and recon.target_row_id = dv.scrIds"
							+ "	and recon_status = 'RECONCILED'"
							+ " and recon.tenant_id = "
							+ tenantId
							+ ""
							+ " "
							+ groupByValues
							+ " "
							+ columnSearchQuery
							+ statusNReconRefQuery
							+ "	group by recon_reference, reconciliation_rule_id "
							+ innerGroupingField
							+ ""
							+ " ) recon"
							+ " order by recon.recon_reference " + sortBy + " ";
				}

				withLimit = query + " limit " + pageIndx + ", " + pageSize + "";
				withoutLimit = query;
				log.info(sourceOrTarget.toUpperCase() + " query to fetching recon ref id grouping info: " + withLimit);
				stmt = conn.createStatement();
				result = stmt.executeQuery(withLimit);

				stmt2 = conn.createStatement();
				result2 = stmt2.executeQuery(withoutLimit);
				int totalCount = 0;
				while (result2.next()) {
					totalCount = totalCount + 1;
				}
				info.put("totalCount", totalCount);
				if("source".equalsIgnoreCase(sourceOrTarget)) {
					//HashMap headerColumns = reconciliationResultService.getColHeadersMapInSequence(viewId, groupId,tenantId, "source");
					while(result.next()) 
					{
						LinkedHashMap reconRecord = new LinkedHashMap();
						// Need to add data
						reconRecord.put("reconReference", result.getString(1).toString());
						reconRecord.put("ruleId", result.getString(2));
						reconRecord.put("count", result.getString(3));
						reconRecord.put("amount", result.getString(4));
						reconRecord.put("varianceAmount", result.getString(5));
						reconRecord.put("ruleCode", result.getString(6));
						if ("reconciliation_rule_id".equalsIgnoreCase(groupByField) || "approval_rule_id".equalsIgnoreCase(groupByField)) 
						{
							reconRecord.put("groupByColumn", result.getString(6));
						} 
						else if ("final_status".equalsIgnoreCase(groupByField)) 
						{
							if (result.getString(7) != null) {
								reconRecord.put("groupByColumn",
										apprStatusLookups.get(result
												.getString(7)));
							} else {
								reconRecord.put("groupByColumn", "");
							}
						} else {
							if (result.getString(7) != null) {
								reconRecord.put("groupByColumn",
										result.getString(7));
							} else {
								reconRecord.put("groupByColumn", "");
							}
						}
						finalList.add(reconRecord);
					}
				} else if ("target".equalsIgnoreCase(sourceOrTarget)) {
					HashMap headerColumns = reconciliationResultService
							.getColHeadersMapInSequence(viewId, groupId,
									tenantId, "target");
					while (result.next()) {
						LinkedHashMap reconRecord = new LinkedHashMap();
						// Need to add data
						reconRecord.put("reconReference", result.getString(1)
								.toString());
						reconRecord.put("ruleId", result.getString(2));
						reconRecord.put("count", result.getString(3));
						reconRecord.put("amount", result.getString(4));
						reconRecord.put("varianceAmount", result.getString(5));
						reconRecord.put("ruleCode", result.getString(6));
						if ("reconciliation_rule_id"
								.equalsIgnoreCase(groupByField)
								|| "approval_rule_id"
										.equalsIgnoreCase(groupByField)) {
							reconRecord.put("groupByColumn",
									result.getString(6));
						} else if ("final_status"
								.equalsIgnoreCase(groupByField)) {
							if (result.getString(7) != null) {
								reconRecord.put("groupByColumn",
										apprStatusLookups.get(result
												.getString(7)));
							} else {
								reconRecord.put("groupByColumn", "");
							}
						} else {
							if (result.getString(7) != null) {
								reconRecord.put("groupByColumn",
										result.getString(7));
							} else {
								reconRecord.put("groupByColumn", "");
							}
						}
						finalList.add(reconRecord);
					}
				}
			} else {
				log.info("View Doesn't exist for the view id: " + viewId);
			}
		} catch (Exception e) {
			log.info("Exception while fetching recon grouping by recon reference info: "
					+ e);
		} finally {
			try {
				if (result != null)
					result.close();
				if (result2 != null)
					result2.close();
				if (stmt != null)
					stmt.close();
				if (stmt2 != null)
					stmt2.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				log.info("Exception while closing jdbc statements.(Grouping ReconRefId Info)"
						+ e);
			}
		}
		log.info("Service for fetching reconciled transaction with group by recon reference id end....");
		recMain.put("info", info);
		finalList.add(recMain);
		return finalList;
	}

	public String getStringWithNumbers(List<Long> values) {
		String finalString = "";
		for (int i = 0; i < values.size(); i++) {
			if (i == values.size() - 1) {
				finalString = finalString + values.get(i);
			} else {
				finalString = finalString + values.get(i) + ", ";
			}
		}
		return finalString;
	}

	public String getStringWithStrings(List<String> values) {
		String finalString = "";
		for (int i = 0; i < values.size(); i++) {
			if (i == values.size() - 1) {
				finalString = finalString + "'" + values.get(i) + "'";
			} else {
				finalString = finalString + "'" + values.get(i) + "', ";
			}
		}
		return finalString;
	}

	public String getReconRefsAsString(List<String> values) {
		String finalString = "";
		for (int i = 0; i < values.size(); i++) // looing column values
		{
			if (i == values.size() - 1) {
				finalString = finalString + " LIKE '%" + values.get(i) + "%' ";
			} else {
				finalString = finalString + " LIKE '%" + values.get(i) + "%', ";
			}
		}
		return finalString;
	}

	public List<BigInteger> getScrIdsByReconReference(String sourceOrTarget,
			Long viewId, String recRefsAsString, Long tenantId, Long groupId,
			String typeOfReconData, String varianceType) {
		List<BigInteger> scrIds = new ArrayList<BigInteger>();
		String varianceCondition = "";
		Connection conn = null;
		Statement stmt = null;
		ResultSet result = null;
		String reconResultTable = "";

		String srcVarType = "";
		String trgVarType = "";

		if ("variance".equalsIgnoreCase(varianceType)) {
			srcVarType = " sign(original_row_id) = -1";
			trgVarType = " sign(target_row_id) = -1";
			varianceCondition = " dv.adjustmentType = 'VARIANCE' ";
		} else {
			srcVarType = " upper(IFNULL(adjustmentType,'X'))!='VARIANCE' ";
			trgVarType = " upper(IFNULL(adjustmentType,'X'))!='VARIANCE' ";
			varianceCondition = " 1=1 ";
		}

		if ("suggestion".equalsIgnoreCase(typeOfReconData)) {
			reconResultTable = "t_reconciliation_suggestion_result";
		} else {
			reconResultTable = "t_reconciliation_result";
		}
		try {
			String query = "";
			DataViews dv = dataViewsRepository.findById(viewId);
			if (dv != null) {
				if ("source".equalsIgnoreCase(sourceOrTarget)) {
					query = "select original_row_id from " + reconResultTable
							+ " rs," + "	`"
							+ dv.getDataViewName().toLowerCase() + "` dv"
							+ " where recon_reference in(" + recRefsAsString
							+ ") and original_view_id = " + viewId
							+ " and target_view_id is null and " + srcVarType
							+ "" + "	and " + varianceCondition
							+ " and tenant_id = " + tenantId + ""
							+ " and rs.original_row_id = dv.scrIds "
							+ " and reconciliation_rule_group_id = " + groupId
							+ "" + " group by original_row_id";
				} else if ("target".equalsIgnoreCase(sourceOrTarget)) {
					query = "select target_row_id from " + reconResultTable
							+ " rs," + "	`"
							+ dv.getDataViewName().toLowerCase() + "` dv"
							+ " where recon_reference in(" + recRefsAsString
							+ ") and target_view_id = " + viewId
							+ " and original_view_id is null and " + trgVarType
							+ "" + "	and " + varianceCondition
							+ " and tenant_id = " + tenantId + ""
							+ " and rs.target_row_id = dv.scrIds "
							+ " and reconciliation_rule_group_id = " + groupId
							+ "" + " group by target_row_id";
				}
				log.info("Query to fetch variance/non-variance ids based on recon reference: "
						+ query);
				DataSource ds = (DataSource) ApplicationContextProvider
						.getApplicationContext().getBean("dataSource");
				conn = ds.getConnection();
				stmt = conn.createStatement();
				result = stmt.executeQuery(query);

				while (result.next()) {
					scrIds.add(new BigInteger(result.getString(1).toString()));
				}
			}
		} catch (Exception e) {
			log.info("Exception while fetching row ids: " + e);
		} finally {
			try {
				if (result != null)
					result.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				log.info("Exception while closing jdbc statements.(Grouping ReconRefId Info)"
						+ e);
			}
		}
		return scrIds;
	}

	public HashMap getAllReconGroupsSummaryInfo(Long tenantId)
	{
		HashMap finalMap = new HashMap();
		List<HashMap> summary = new ArrayList<HashMap>();
		HashMap info = new HashMap();
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet result = null;
		
		int count = 0;
		try
		{
			DataSource ds = (DataSource) ApplicationContextProvider.getApplicationContext().getBean("dataSource");
			conn = ds.getConnection();
			
			String query = "SELECT DISTINCT det.rule_group_id, det.name,  det.s_view_name, det.t_view_name, det.source_view_id, "
					+ " det.target_view_id, det.source_view_display_id,det.target_view_display_id,"
					+ " ifnull(recon_src.src_recon_amount, 0) src_recon_amount, ifnull(recon_tr.tr_recon_amount, 0) tr_recon_amount, det.rgd_for_display, "
					+ " ifnull(recon_src.src_recon_count, 0) src_recon_count, ifnull(recon_tr.tr_recon_count, 0) tr_recon_count, "
					+ " Ifnull(det.sr_view_count, 0) sr_view_count, Ifnull(det.tr_view_count, 0) tr_view_count, " 
					+ " det.sr_last_file_date,det.tr_last_file_date,det.sr_last_trans_date, det.tr_last_trans_date"
					+ " FROM "
					+ " (SELECT rg.id rule_group_id, rg.name, ru.source_data_view_id  source_view_id, ru.target_data_view_id  target_view_id, "
					+ " sdv.data_view_disp_name s_view_name, tdv.data_view_disp_name t_view_name, "
					+ " sdv.id_for_display source_view_display_id,tdv.id_for_display target_view_display_id,rg.id_for_display rgd_for_display,"
					+ " sdv.record_count sr_view_count, tdv.record_count tr_view_count, "
					+ " sdv.last_file_date sr_last_file_date, "
					+ " tdv.last_file_date tr_last_file_date, sdv.last_trans_date sr_last_trans_date, tdv.last_trans_date tr_last_trans_date "
					+ " FROM t_rule_group rg, t_rule_group_details rgd, t_rules ru, t_data_views sdv, t_data_views tdv "
					+ " WHERE rg.id = rgd.rule_group_id "
					+ " AND rgd.rule_id = ru.id AND ru.tenant_id = "
					+ tenantId
					+ " AND ru.source_data_view_id = sdv.id AND ru.target_data_view_id = tdv.id "
					+ " AND rg.rule_purpose = 'RECONCILIATION') det "
					+ " LEFT JOIN (SELECT reconciliation_rule_group_id, original_view_id, Sum(original_amount) AS src_recon_amount, count(*) AS src_recon_count "
					+ " FROM t_reconciliation_result WHERE original_view_id IS NOT NULL AND current_record_flag IS TRUE "
					+ " AND tenant_id = "
					+ tenantId
					+ " AND recon_status = 'RECONCILED' "
					+ " GROUP  BY reconciliation_rule_group_id, original_view_id) recon_src "
					+ " ON det.rule_group_id = recon_src.reconciliation_rule_group_id AND recon_src.original_view_id = det.source_view_id "
					+ " LEFT JOIN (SELECT reconciliation_rule_group_id, "
					+ " target_view_id, Sum(target_amount) AS tr_recon_amount , count(*) AS tr_recon_count FROM t_reconciliation_result "
					+ " WHERE target_view_id IS NOT NULL AND current_record_flag IS TRUE AND tenant_id = "
					+ tenantId
					+ " "
					+ " AND recon_status = 'RECONCILED' GROUP  BY reconciliation_rule_group_id, target_view_id) recon_tr "
					+ " ON det.rule_group_id = recon_tr.reconciliation_rule_group_id  AND recon_tr.target_view_id = det.target_view_id order by det.name asc";
			log.info("Query to fetch all recon groups summary info: "+query);
			stmt = conn.createStatement();
			result = stmt.executeQuery(query);

			while(result.next()) 
			{
				HashMap hm = new HashMap();
				hm.put("groupName", result.getString("name"));
				hm.put("groupId", result.getString("rgd_for_display"));
				hm.put("sViewName", result.getString("s_view_name"));
				hm.put("tViewName", result.getString("t_view_name"));
				hm.put("sViewId", result.getString("source_view_display_id"));
				hm.put("tViewId", result.getString("target_view_display_id"));
				hm.put("sRecCount", Long.parseLong(result.getString("src_recon_count")));
				hm.put("tRecCount", Long.parseLong(result.getString("tr_recon_count")));
				hm.put("sRecAmount", Double.parseDouble(result.getString("src_recon_amount")));
				hm.put("tRecAmount", Double.parseDouble(result.getString("tr_recon_amount")));
				hm.put("sViewCount", Long.parseLong(result.getString("sr_view_count")));
				hm.put("tViewCount", Long.parseLong(result.getString("tr_view_count")));
				hm.put("sLastFileDate", result.getString("sr_last_file_date"));
				hm.put("tLastFileDate", result.getString("tr_last_file_date"));
				hm.put("sLastTransDate", result.getString("sr_last_trans_date"));
				hm.put("tLastTransDate", result.getString("tr_last_trans_date"));
				summary.add(hm);
				count = count + 1;
			}
		}
		catch(Exception e)
		{
			log.info("Exception while fetching summary info for all groups. "+ e);
		}
		finally
		{
			try 
			{
				if(result != null)
					result.close();
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
			} 
			catch(Exception e) 
			{
				log.info("Exception while closing jdbc statements.(Grouping ReconRefId Info) "+ e);
			}
		}
		info.put("count",count);
		finalMap.put("summary", summary);
		finalMap.put("info", info);
		return finalMap;
	}
	
	public HashMap getReconSummaryByRuleGroup(Long tenantId)
			throws SQLException, ClassNotFoundException {
		HashMap grpsMap = new HashMap();

		Connection conn = null;
		Statement stmt = null;
		ResultSet result = null;
		try {
			String query = "SELECT DISTINCT det.rule_group_id, det.name,  det.s_view_name, det.t_view_name, det.source_view_id, "
					+ " det.target_view_id, det.source_view_display_id,det.target_view_display_id,recon_src.src_recon_amount, recon_tr.tr_recon_amount,det.rgd_for_display, "
					+ " recon_src.src_recon_count, recon_tr.tr_recon_count FROM "
					+ " (SELECT rg.id rule_group_id, rg.name, ru.source_data_view_id  source_view_id, ru.target_data_view_id  target_view_id, "
					+ " sdv.data_view_disp_name s_view_name, tdv.data_view_disp_name t_view_name, "
					+ " sdv.id_for_display source_view_display_id,tdv.id_for_display target_view_display_id,rg.id_for_display rgd_for_display "
					+ " FROM t_rule_group rg, t_rule_group_details rgd, t_rules ru, t_data_views sdv, t_data_views tdv "
					+ " WHERE rg.id = rgd.rule_group_id "
					+ " AND rgd.rule_id = ru.id AND ru.tenant_id = "
					+ tenantId
					+ " AND ru.source_data_view_id = sdv.id AND ru.target_data_view_id = tdv.id "
					+ " AND rg.rule_purpose = 'RECONCILIATION') det "
					+ " LEFT JOIN (SELECT reconciliation_rule_group_id, original_view_id, Sum(original_amount) AS src_recon_amount, count(*) AS src_recon_count "
					+ " FROM t_reconciliation_result WHERE original_view_id IS NOT NULL AND current_record_flag IS TRUE "
					+ " AND tenant_id = "
					+ tenantId
					+ " AND recon_status = 'RECONCILED' "
					+ " GROUP  BY reconciliation_rule_group_id, original_view_id) recon_src "
					+ " ON det.rule_group_id = recon_src.reconciliation_rule_group_id AND recon_src.original_view_id = det.source_view_id "
					+ " LEFT JOIN (SELECT reconciliation_rule_group_id, "
					+ " target_view_id, Sum(target_amount) AS tr_recon_amount , count(*) AS tr_recon_count FROM t_reconciliation_result "
					+ " WHERE target_view_id IS NOT NULL AND current_record_flag IS TRUE AND tenant_id = "
					+ tenantId
					+ " "
					+ " AND recon_status = 'RECONCILED' GROUP  BY reconciliation_rule_group_id, target_view_id) recon_tr "
					+ " ON det.rule_group_id = recon_tr.reconciliation_rule_group_id  AND recon_tr.target_view_id = det.target_view_id order by det.name asc";

			log.info("Query to fetch summary info for all rule groups: "
					+ query);

			DataSource ds = (DataSource) ApplicationContextProvider
					.getApplicationContext().getBean("dataSource");
			conn = ds.getConnection();

			stmt = conn.createStatement();
			result = stmt.executeQuery(query);
			while (result.next()) {
				String grpId = result.getString(1).toString();
				String grpName = result.getString(2).toString();
				if (grpsMap.containsKey(grpId)) {
					HashMap grpInfo = (HashMap) grpsMap.get(grpId);
					List<HashMap> summary = (List<HashMap>) grpInfo
							.get("summary");
					HashMap recInfo = new HashMap();
					recInfo.put("sViewId", result.getString(7).toString());
					recInfo.put("tViewId", result.getString(8).toString());
					recInfo.put("sViewName", result.getString(3).toString());
					recInfo.put("tViewName", result.getString(4).toString());
					if (result.getString(9) != null
							&& result.getString(10) != null) {
						recInfo.put("sRecAmount", Double.parseDouble(result
								.getString(9).toString()));
						recInfo.put("tRecAmount", Double.parseDouble(result
								.getString(10).toString()));
					} else {
						recInfo.put("sRecAmount", 0.0);
						recInfo.put("tRecAmount", 0.0);
					}
					if (result.getString(12) != null
							&& result.getString(13) != null) {
						recInfo.put("sRecCount", Long.parseLong((result
								.getString(12).toString())));
						recInfo.put("tRecCount", Long.parseLong(result
								.getString(13).toString()));
					} else {
						recInfo.put("sRecCount", 0);
						recInfo.put("tRecCount", 0);
					}
					summary.add(recInfo);
				} else {
					HashMap groupMap = new HashMap();
					groupMap.put("groupId", result.getString(11).toString());
					groupMap.put("groupName", grpName);
					List<HashMap> summary = new ArrayList<HashMap>();
					HashMap rec = new HashMap();
					rec.put("sViewId", result.getString(7).toString());
					rec.put("tViewId", result.getString(8).toString());
					rec.put("sViewName", result.getString(3).toString());
					rec.put("tViewName", result.getString(4).toString());

					if (result.getString(9) != null
							&& result.getString(10) != null) {
						rec.put("sRecAmount", Double.parseDouble(result
								.getString(9).toString()));
						rec.put("tRecAmount", Double.parseDouble(result
								.getString(10).toString()));
					} else {
						rec.put("sRecAmount", 0.0);
						rec.put("tRecAmount", 0.0);
					}
					if (result.getString(12) != null
							&& result.getString(13) != null) {
						rec.put("sRecCount", Long.parseLong((result
								.getString(12).toString())));
						rec.put("tRecCount", Long.parseLong(result
								.getString(13).toString()));
					} else {
						rec.put("sRecCount", 0);
						rec.put("tRecCount", 0);
					}
					summary.add(rec);
					groupMap.put("summary", summary);
					grpsMap.put(grpId, groupMap);
				}
			}
		} catch (Exception e) {
			log.info("Exception while fetching summary info for all groups. "
					+ e);
		} finally {
			try {
				if (result != null)
					result.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				log.info("Exception while closing jdbc statements.(Grouping ReconRefId Info)"
						+ e);
			}
		}
		return grpsMap;
	}

	// Outbound API's
	public HashMap getReconSrcTrgtRulesInfo(String ruleGroupName,
			String sViewName, String tViewName, String rulesCondition) {
		HashMap finalMap = new HashMap();
		Connection conn = null;
		Statement stmt = null;
		ResultSet result = null;
		try {
			String query = "select distinct rg.id rule_group_id, rg.name rule_group_name, ru.id rule_id, ru.rule_code,"
					+ " ru.source_data_view_id, ru.target_data_view_id, s_dv.data_view_name source_data_view, t_dv.data_view_name target_data_view"
					+ " from t_rule_group_details rgd, t_rule_group rg, "
					+ " t_rules ru, t_data_views s_dv, t_data_views t_dv"
					+ " where rg.id = rgd.rule_group_id"
					+ " and rgd.rule_id = ru.id"
					+ " and rg.rule_purpose = 'Reconciliation'"
					+ " and ru.source_data_view_id = s_dv.id"
					+ " and ru.target_data_view_id = t_dv.id"
					+ " and rg.name = '"
					+ ruleGroupName
					+ "' "
					+ " and s_dv.data_view_name = '"
					+ sViewName
					+ "' "
					+ " and t_dv.data_view_name = '"
					+ tViewName
					+ "' "
					+ rulesCondition;
			log.info("Query for fetching recon view ids, rule ids: " + query);
			DataSource ds = (DataSource) ApplicationContextProvider
					.getApplicationContext().getBean("dataSource");
			conn = ds.getConnection();
			stmt = conn.createStatement();
			result = stmt.executeQuery(query);
			List<Long> ruleIds = new ArrayList<Long>();
			while (result.next()) {
				ruleIds.add(Long.parseLong(result.getString("rule_id")
						.toString()));
				finalMap.put("groupId", Long.parseLong(result.getString(
						"rule_group_id").toString()));
				finalMap.put("sViewId", Long.parseLong(result.getString(
						"source_data_view_id").toString()));
				finalMap.put("tViewId", Long.parseLong(result.getString(
						"target_data_view_id").toString()));
				finalMap.put("sViewName", result.getString("source_data_view")
						.toString());
				finalMap.put("tViewName", result.getString("target_data_view")
						.toString());
			}
			finalMap.put("ruleIds", ruleIds);
		} catch (Exception e) {
			log.info("Exception while getting recon view ids, rule ids: " + e);
		} finally {
			try {
				if (result != null)
					result.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				log.info("Exception while closing jdbc statements.(Fetching recon source, target, rule ids)"
						+ e);
			}
		}
		return finalMap;
	}

	public List<LinkedHashMap> getReconciledTransactionData(String sViewName,String tViewName, Long groupId, Long sViewId, Long tViewId,String rulesCondition, String paginationCondition, String srcQuery,String trgtQuery, HashMap headerColumns) {
		List<LinkedHashMap> finalList = new ArrayList<LinkedHashMap>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet result = null;
		try {
			DataSource ds = (DataSource) ApplicationContextProvider.getApplicationContext().getBean("dataSource");
			conn = ds.getConnection();
			
			String query = "SELECT src.*, "
					+ "       target.* "
					+ " FROM (SELECT src_inner.*, "
					+ "               rl.rule_code      recon_rule_name, "
					+ "               appr_group.name   approval_group_name, "
					+ "               appr_rl.rule_code approval_rule_name "
					+ "        FROM   ((SELECT recon_group.name      rule_group_name, "
					+ "                       recon.recon_reference src_rec_ref, "
					+ "                       sdv.*, "
					+ "                       recon.recon_status, "
					+ "                       recon.reconciliation_rule_id, "
					+ "                       recon.recon_job_reference, "
					+ "                       recon.approval_group_id, "
					+ "                       recon.approval_rule_id, "
					+ "                       recon.final_status    approval_status "
					+ "                FROM   t_reconciliation_result recon, "
					+ "                       `"+sViewName.toLowerCase()+"` sdv, "
					+ "                       t_rule_group recon_group "
					+ "                WHERE  recon.reconciliation_rule_group_id = "+groupId+" "
					+ "                       AND recon_group.id = "+groupId+" "
					+ "                       AND original_view_id = "+sViewId+" "
					+ "                       AND recon_status = 'RECONCILED' "
					+ "                       AND current_record_flag IS TRUE "
					+ " 					  "+srcQuery+" "
					+ "                       AND recon.original_row_id = sdv.scrids) src_inner "
					+ "                LEFT JOIN t_rules rl ON src_inner.reconciliation_rule_id = rl.id "
					+ "                LEFT JOIN t_rule_group appr_group ON src_inner.approval_group_id = appr_group.id "
					+ "                LEFT JOIN t_rules appr_rl ON src_inner.approval_rule_id = appr_rl.id)) src, "
					+ "       (SELECT recon.recon_reference target_rec_ref, "
					+ "               tdv.* "
					+ "        FROM   t_reconciliation_result recon, "
					+ "               `"+tViewName.toLowerCase()+"` tdv "
					+ "        WHERE  recon.reconciliation_rule_group_id = "+groupId+" "
					+ "               AND target_view_id = "+tViewId+" "
					+ "               AND recon_status = 'RECONCILED' "
					+ "               AND current_record_flag IS TRUE "
					+ " 					  "+trgtQuery+" "
					+ "               AND recon.target_row_id = tdv.scrids) target "
					+ " WHERE src.src_rec_ref = target.target_rec_ref "
					+ " "+ paginationCondition +" ";

			log.info("Query to fetching reconciled transactions data (Outbound data. Reconciled) "+ query);
			stmt = conn.createStatement();
			result = stmt.executeQuery(query);
			ResultSetMetaData rsmd = result.getMetaData();
			int colCount = rsmd.getColumnCount();
			log.info("Column Count: " + colCount);
			while (result.next()) {
				LinkedHashMap mp = new LinkedHashMap();
/*				for (int i = 1; i <= colCount; i++) {
					mp.put(rsmd.getColumnName(i).toString(),result.getString(i));
				}*/
				Iterator it = headerColumns.entrySet().iterator();
				while(it.hasNext())
				{
					Map.Entry pair = (Map.Entry)it.next();
			        mp.put(pair.getValue().toString(), result.getString(pair.getKey().toString()));
				}
				finalList.add(mp);
			}
		} catch (Exception e) {
			log.info("Exception while fetching reconciled transactional data. "+ e);
		} finally {
			try {
				if (result != null)
					result.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				log.info("Exception while closing jdbc statements.(Fetching recon source, target, rule ids) "+ e);
			}
		}
		return finalList;
	}

	public HashMap getReconInputsInfo(String sourceOrTarget, String groupName,
			String viewName) {
		HashMap finalMap = new HashMap();
		Connection conn = null;
		Statement stmt = null;
		ResultSet result = null;
		try {
			DataSource ds = (DataSource) ApplicationContextProvider
					.getApplicationContext().getBean("dataSource");
			conn = ds.getConnection();
			String query = "";
			if ("source".equalsIgnoreCase(sourceOrTarget)) {
				query = "select distinct rg.id rule_group_id, rg.name rule_group_name, ru.id rule_id, ru.rule_code,"
						+ " ru.source_data_view_id view_id, s_dv.data_view_name view_name"
						+ " from t_rule_group_details rgd, t_rule_group rg, t_rules ru, t_data_views s_dv"
						+ " where rg.id = rgd.rule_group_id"
						+ " and rgd.rule_id = ru.id"
						+ " and rg.rule_purpose = 'Reconciliation'"
						+ " and ru.source_data_view_id = s_dv.id"
						+ " and rg.name = '"
						+ groupName
						+ "' "
						+ " and s_dv.data_view_name = '" + viewName + "'";
			} else {
				query = "select distinct rg.id rule_group_id, rg.name rule_group_name, ru.id rule_id, ru.rule_code,"
						+ " ru.target_data_view_id view_id, t_dv.data_view_name view_name"
						+ " from t_rule_group_details rgd, t_rule_group rg, t_rules ru, t_data_views t_dv"
						+ " where rg.id = rgd.rule_group_id"
						+ " and rgd.rule_id = ru.id"
						+ " and rg.rule_purpose = 'Reconciliation'"
						+ " and ru.target_data_view_id = t_dv.id"
						+ " and rg.name = '"
						+ groupName
						+ "' "
						+ " and t_dv.data_view_name = '" + viewName + "'";

			}
			log.info("Query to fetch recon inputs: " + query);

			stmt = conn.createStatement();
			result = stmt.executeQuery(query);
			while (result.next()) {
				finalMap.put("groupId", result.getString("rule_group_id"));
				finalMap.put("groupName", result.getString("rule_group_name"));
				finalMap.put("viewName", result.getString("view_name"));
				finalMap.put("viewId", result.getString("view_id"));
			}
		} catch (Exception e) {
			log.info("Exception while fetching recon inputs info. " + e);
		} finally {
			try {
				if (result != null)
					result.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				log.info("Exception while closing jdbc statements.(Fetching recon inputs info.) "
						+ e);
			}
		}
		return finalMap;
	}

	public List<LinkedHashMap> getUnReconciledTransactions(String sourceOrTarget, Long groupId, Long viewId, String viewName,
			String paginationCondition, String filterQuery, HashMap headerColumns) {
		List<LinkedHashMap> finalList = new ArrayList<LinkedHashMap>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet result = null;
		try {
			DataSource ds = (DataSource) ApplicationContextProvider
					.getApplicationContext().getBean("dataSource");
			conn = ds.getConnection();
			String query = "";
			if ("source".equalsIgnoreCase(sourceOrTarget)) {
				query = "select det.* from (select dv.*,"
						+ " (case when rs.recon_status is null"
						+ " then 'Not Reconciled'"
						+ " else 'Reconciled' end) as status"
						+ " from "
						+ " (select * from `"
						+ viewName.toLowerCase()
						+ "` "
						+ filterQuery
						+ ") dv"
						+ " left outer join"
						+ " (select * from t_reconciliation_result where reconciliation_rule_group_id = "
						+ groupId
						+ " and original_view_id = "
						+ viewId
						+ ""
						+ " and recon_status = 'RECONCILED' and current_record_flag is true) rs"
						+ " on dv.scrIds = rs.original_row_id) det where status = 'Not Reconciled' "
						+ paginationCondition;
			} else if ("target".equalsIgnoreCase(sourceOrTarget)) {
				query = "select det.* from (select dv.*,"
						+ " (case when rs.recon_status is null"
						+ " then 'Not Reconciled'"
						+ " else 'Reconciled' end) as status from "
						+ " (select * from `"
						+ viewName.toLowerCase()
						+ "` "
						+ filterQuery
						+ ") dv"
						+ " left outer join"
						+ " (select * from t_reconciliation_result where reconciliation_rule_group_id = "
						+ groupId
						+ " and target_view_id = "
						+ viewId
						+ ""
						+ " and recon_status = 'RECONCILED' and current_record_flag is true) rs"
						+ " on dv.scrIds = rs.target_row_id) det where status = 'Not Reconciled' "
						+ paginationCondition;
			}
			log.info("Query to fetch un reconciled transactions data: " + query);
			stmt = conn.createStatement();
			result = stmt.executeQuery(query);

			ResultSetMetaData rsmd = result.getMetaData();
			int colCount = rsmd.getColumnCount();
			log.info("Column Count: " + colCount);

			while (result.next()) {
				LinkedHashMap mp = new LinkedHashMap();
				Iterator it = headerColumns.entrySet().iterator();
				while(it.hasNext())
				{
					Map.Entry pair = (Map.Entry)it.next();
			        mp.put(pair.getValue().toString(), result.getString(pair.getKey().toString()));
				}
				finalList.add(mp);
			}
		} catch (Exception e) {
			log.info("Exception while fetching un reconciled transactions " + e);
		} finally {
			try {
				if (result != null)
					result.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				log.info("Exception while closing jdbc statements.(Fetching un reconciled transactions.) "
						+ e);
			}
		}
		return finalList;
	}

	public List<String> getColumnsAsList(HashMap headerColumns) {
		List<String> columns = new ArrayList<String>();
		Iterator it = headerColumns.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			columns.add(pair.getKey().toString());
		}
		return columns;
	}
	
	
	public HashMap getReconciledSummaryInfo(String periodFactor, Long groupId,  String rangeFrom, String rangeTo, Long tenantId, Long sViewId, Long tViewId,
			Long pageNumber, Long pageSize, String grpByColsQuery, String reconGrpQuery, HashMap grpCols, String grpByCols, String groupingColsQuery) throws NumberFormatException, SQLException
	{
		HashMap finalMap = new HashMap();
		Connection conn = null;
		Statement stmt = null;
		
		Statement totalCountStmt = null;
		ResultSet result = null;
		ResultSet totalCountRS = null;
		Statement totalCountStatement = null;
		ResultSet totalCountResultSet = null;
		List<HashMap> finalList = new ArrayList<HashMap>();
		HashMap info = new HashMap();
		try
		{
			DataSource ds = (DataSource) ApplicationContextProvider.getApplicationContext().getBean("dataSource");
			conn = ds.getConnection();
			stmt = conn.createStatement();
			totalCountStmt = conn.createStatement();			
			totalCountStatement = conn.createStatement();
			
			Long totalCount = 0L;
			
			DataViews sdv = dataViewsRepository.findOne(sViewId.longValue());
			DataViews tdv = dataViewsRepository.findOne(tViewId.longValue());
		
			String query = "SELECT " + reconGrpQuery+ ", "
					+ "       sum(recon_src.src_count) src_count, "
					+ "       sum(recon_src.src_amount_sum) src_amount, "
					+ "       sum(recon_src.src_tolerance_amount) src_var_amount, "
					+ "       sum(recon_tr.tr_count) tr_count, "
					+ "       sum(recon_tr.tr_amount_sum) tr_amount, "
					+ "       sum(recon_tr.tr_tolerance_amount) tr_var_amount"
					+ " FROM   (SELECT recon_reference, "
					+ grpByColsQuery
					+ ",               Count(*)                               src_count, "
					+ "               Sum(Ifnull(recon.original_amount, 0))  src_amount_sum, "
					+ "               Sum(Ifnull(recon.tolerance_amount, 0)) src_tolerance_amount "
					+ "        FROM   t_reconciliation_result recon LEFT JOIN t_rules rl ON recon.reconciliation_rule_id = rl.id LEFT JOIN look_up_code lc on recon.final_status = lc.look_up_code"
					/*+ reconRulesLeftJoin + ", "*/
					+ ",               `"+sdv.getDataViewName().toLowerCase()+"` dv "
					+ "        WHERE  reconciliation_rule_group_id = "+groupId+" "
					+ "               AND Date(dv.`"+periodFactor+"`) BETWEEN '"+rangeFrom+"' AND '"+rangeTo+"' "
					+ "               AND original_view_id = "+sViewId+" "
					+ "               AND current_record_flag IS TRUE "
					+ "               AND recon.original_row_id = dv.scrids "
					+ "               AND recon_status = 'RECONCILED' "
					+ "               AND recon.tenant_id = "+tenantId+" "
					/*+ approvalNullCondition*/
					+ "        GROUP  BY recon_reference , "
					+ grpByCols+") recon_src "
					+ "       JOIN (SELECT recon_reference, "
					+ "                    Count(*)                               tr_count, "
					+ "                    Sum(Ifnull(recon.target_amount, 0))    tr_amount_sum, "
					+ "                    Sum(Ifnull(recon.tolerance_amount, 0)) tr_tolerance_amount "
					+ "             FROM   t_reconciliation_result recon LEFT JOIN t_rules rl ON recon.reconciliation_rule_id = rl.id "
					/*+ reconRulesLeftJoin+ ","*/
					+ ", `"+tdv.getDataViewName().toLowerCase()+"` dv "
					+ "             WHERE  reconciliation_rule_group_id = "+groupId+" "
					+ "                    AND target_view_id = "+tViewId+" "
					+ "                    AND current_record_flag IS TRUE "
					+ "                    AND recon.target_row_id = dv.scrids "
					+ "                    AND recon_status = 'RECONCILED' "
					+ "                    AND recon.tenant_id = "+tenantId+" "
					/*+ approvalNullCondition*/
					+ "             GROUP  BY recon_reference "
					+") recon_tr "
					+ "         ON recon_src.recon_reference = recon_tr.recon_reference "
					+ "	   group by "
					+ groupingColsQuery;
			
			log.info("Query for fetching reconciliation grouping summary info: " + query);
			String paginationQuery = query + " LIMIT "+pageNumber +", "+ pageSize;
			String totalCountQuery = "SELECT IFNULL(SUM(recon_count.src_count),0) source_count, IFNULL(SUM(recon_count.tr_count), 0) target_count, IFNULL(SUM(src_amount), 0) source_amount, IFNULL(SUM(tr_amount), 0) target_amount FROM ("
					+query+") recon_count";
			String totalPaginationCountQuery = "SELECT COUNT(*) count FROM ("+query+") count";
			NumberFormat numFormat = NumberFormat.getInstance();
			result = stmt.executeQuery(paginationQuery);
			totalCountResultSet = totalCountStatement.executeQuery(totalPaginationCountQuery);
			
			totalCountRS = totalCountStmt.executeQuery(totalCountQuery);
			while(totalCountResultSet.next())
			{
				totalCount = totalCount + Long.parseLong(totalCountResultSet.getString("count"));
			}
			ResultSetMetaData rsmd = result.getMetaData();
			int colCount = rsmd.getColumnCount();
			while(result.next()) 
			{
				HashMap map = new HashMap();
				String sAmount = "0.0";
				String tAmount = "0.0";
				String sCount = "0";
				String tCount = "0";
				
				for(int i = 1; i <= colCount; i++) 
				{
					if(grpCols.get(rsmd.getColumnName(i).toString()) != null)
					{
						map.put(grpCols.get(rsmd.getColumnName(i).toString()), result.getString(rsmd.getColumnName(i).toString()));
					}
				}
				
				if(result.getString("src_amount") != null) 
				{
					sAmount = result.getString("src_amount").toString();
				}
				if(result.getString("tr_amount") != null) 
				{
					tAmount = result.getString("tr_amount").toString();
				}
				if(result.getString("src_count") != null) 
				{
					sCount = result.getString("src_count").toString();
				}
				if(result.getString("tr_count") != null) 
				{
					tCount = result.getString("tr_count").toString();
				}

				map.put("sAmount", Double.parseDouble(sAmount));
				map.put("dsCount", Long.parseLong(sCount));
				
				map.put("dtCount", Long.parseLong(tCount));
				map.put("sCount", numFormat.format(Long.parseLong(sCount)));
				map.put("tCount", numFormat.format(Long.parseLong(tCount)));
				map.put("tAmount", Double.parseDouble(tAmount));
				
				finalList.add(map);
			}
			info.put("totalCount", totalCount);
		}
		catch(Exception e)
		{
			log.info("Exception: "+e);
		}
		finally
		{
			try{
				
				if (result != null)
					result.close();
				if(totalCountRS != null)
					totalCountRS.close();
				if(totalCountResultSet != null)
					totalCountResultSet.close();
				if (stmt != null)
					stmt.close();
				if(totalCountStmt != null)
					totalCountStmt.close();
				if(totalCountStatement != null)
					totalCountStatement.close();
				if (conn != null)
					conn.close();
			}
			catch(Exception e) 
			{
				log.info("Exception while closing jdbc statements.(Fetching un reconciled transactions.) "+ e);
			}	
		}
		finalMap.put("summary", finalList);
		finalMap.put("info", info);
		return finalMap;
	}
	
	public HashMap getReconGroupingSummaryInfo(String groupBy, Long groupId, String periodFactor, String rangeFrom, String rangeTo,
			Long tenantId, Long sViewId, Long tViewId, String sColumnName, String dateQualifierType, Long pageNumber, Long pageSize) throws NumberFormatException, SQLException
	{
		HashMap finalMap = new HashMap();
		HashMap grpMap = new HashMap();
		log.info("In Service of fetching reconciled grouping summary info");
		List<HashMap> finalList = new ArrayList<HashMap>();
		String currQualifier = reconciliationResultService.getViewColumnQualifier(BigInteger.valueOf(sViewId), "CURRENCYCODE");

		Connection conn = null;
		Statement stmt = null;
		Statement totalCountStmt = null;
		ResultSet result = null;
		ResultSet totalCountRS = null;
		Statement totalCountStatement = null;
		ResultSet totalCountResultSet = null;
		
		Double totalSrcAmount = 0.0;
		Long totalSrcCount = 0L;

		Double totalTrAmount = 0.0;
		Long totalTrCount = 0L;
		HashMap info = new HashMap();
		
		String groupingCol = "";
		String sGroupByField = "";

		String reconRulesLeftJoin = "";
		String approvalNullCondition = "";
		
		String groupingBy = "";
		Long totalCount = 0L;
		HashMap approvalLookUps = new HashMap();
		if("rules".equalsIgnoreCase(groupBy))
		{
			groupingCol = " recon_src.reconciliation_rule_id, (CASE WHEN recon_src.grp_column IS NULL THEN 'Manual' ELSE recon_src.grp_column end)";
			sGroupByField = " reconciliation_rule_id, rl.rule_code ";
			reconRulesLeftJoin = " LEFT JOIN t_rules rl ON recon.reconciliation_rule_id = rl.id ";
			groupingBy = "Sub Process";
			finalMap.put("groupBy", "rule_code");
			finalMap.put("displayName", groupingBy);
			
			grpMap.put("groupBy", "rule_code");
		}
		else if("batch".equalsIgnoreCase(groupBy))
		{
			groupingCol = " (CASE WHEN Substring(recon_src.grp_column, 1, 7) = 'MANUAL_' THEN 'Manual' ELSE recon_src.grp_column end) ";
			sGroupByField = " recon_job_reference ";
			groupingBy = "Batch";
			finalMap.put("groupBy", sGroupByField);
			finalMap.put("displayName", groupingBy);
			
			grpMap.put("groupBy", sGroupByField.trim());
		}
		else if("columnName".equalsIgnoreCase(groupBy))
		{
			groupingCol = " recon_src.grp_column ";
			sGroupByField = " dv."+sColumnName;
			grpMap.put("groupBy", sColumnName.replaceAll("`", ""));
		}
		else if("days".equalsIgnoreCase(groupBy))
		{
			String sDateQulifier = "";
			if("TRANSDATE".equalsIgnoreCase(dateQualifierType))
			{
				sDateQulifier = reconciliationResultService.getViewColumnQualifier(BigInteger.valueOf(sViewId), "TRANSDATE");
				//String tDateQulifier = reconciliationResultService.getViewColumnQualifier(BigInteger.valueOf(tViewId), "TRANSDATE");
				sGroupByField = " Date(`"+sDateQulifier+"`) ";		
			}
			else if("FILEDATE".equalsIgnoreCase(dateQualifierType))
			{
				sDateQulifier = reconciliationResultService.getViewColumnQualifier(BigInteger.valueOf(sViewId), "FILEDATE");
				//String tDateQulifier = reconciliationResultService.getViewColumnQualifier(BigInteger.valueOf(tViewId), "FILEDATE");
				sGroupByField = " Date(`"+sDateQulifier+"`) ";
			}
			groupingCol = " recon_src.grp_column ";
			groupingBy = "Period";
			grpMap.put("groupBy", sDateQulifier.trim());
/*			finalMap.put("groupBy", sDateQualifier);
			finalMap.put("displayName", groupingBy);*/
		}
		else if("approvalStatus".equalsIgnoreCase(groupBy))
		{
			groupingCol = " (case when recon_src.grp_column IS NULL THEN 'Not Required' ELSE recon_src.grp_column END) ";
			sGroupByField = " final_status ";
			groupingBy = "Approval Status";
			finalMap.put("groupBy", sGroupByField);
			finalMap.put("displayName", groupingBy);
			grpMap.put("groupBy", sGroupByField.trim());
			List<LookUpCode> lookUps = lookUpCodeRepository.findByTenantIdAndLookUpType(tenantId, "APPROVAL_STATUS");
			if(lookUps.size()>0)
			{
				for(LookUpCode lc : lookUps)
				{
					approvalLookUps.put(lc.getLookUpCode(), lc.getMeaning());
				}
			}
		}
		else if("approvalDate".equalsIgnoreCase(groupBy))
		{
			groupingCol = " recon_src.grp_column ";
			sGroupByField = " Date(final_action_date) ";
			approvalNullCondition = " and recon.final_action_date is not null";
			groupingBy = "Approval Date";
			finalMap.put("groupBy", "final_action_date");
			finalMap.put("displayName", groupingBy);
			grpMap.put("groupBy", "final_action_date");
		}
		else if("approvalRule".equalsIgnoreCase(groupBy))
		{
			reconRulesLeftJoin = " LEFT JOIN t_rules rl ON recon.approval_rule_id = rl.id ";
			sGroupByField = " approval_rule_id, rl.rule_code ";
			groupingCol = "recon_src.approval_rule_id, recon_src.grp_column ";
			approvalNullCondition = " and recon.approval_rule_id is not null";
			groupingBy = "Approval Rule";
			grpMap.put("groupBy", "rule_code");
		}
		
		try{
			DataSource ds = (DataSource) ApplicationContextProvider.getApplicationContext().getBean("dataSource");
			conn = ds.getConnection();
			stmt = conn.createStatement();
			totalCountStmt = conn.createStatement();
			
			totalCountStatement = conn.createStatement();

			DataViews sdv = dataViewsRepository.findOne(sViewId.longValue());
			DataViews tdv = dataViewsRepository.findOne(tViewId.longValue());

			String query = "SELECT  "+ groupingCol+" grouping_col, "
					+ "		  recon_src.currency_column           currency_column, "
					+ "       sum(recon_src.src_count) src_count, "
					+ "       sum(recon_src.src_amount_sum) src_amount, "
					+ "       sum(recon_src.src_tolerance_amount) src_var_amount, "
					+ "       sum(recon_tr.tr_count) tr_count, "
					+ "       sum(recon_tr.tr_amount_sum) tr_amount, "
					+ "       sum(recon_tr.tr_tolerance_amount) tr_var_amount"
					+ " FROM   (SELECT recon_reference, "
					+ "               "+sGroupByField+"                       grp_column, "
					+ "				  dv.`"+currQualifier+"`				  currency_column,  "
					+ "               Count(*)                               src_count, "
					+ "               Sum(Ifnull(recon.original_amount, 0))  src_amount_sum, "
					+ "               Sum(Ifnull(recon.tolerance_amount, 0)) src_tolerance_amount "
					+ "        FROM   t_reconciliation_result recon "
					+ reconRulesLeftJoin + ", "
					+ "               `"+sdv.getDataViewName().toLowerCase()+"` dv "
					+ "        WHERE  reconciliation_rule_group_id = "+groupId+" "
					+ "               AND Date(dv.`"+periodFactor+"`) BETWEEN '"+rangeFrom+"' AND '"+rangeTo+"' "
					+ "               AND original_view_id = "+sViewId+" "
					+ "               AND current_record_flag IS TRUE "
					+ "               AND recon.original_row_id = dv.scrids "
					+ "               AND recon_status = 'RECONCILED' "
					+ "               AND recon.tenant_id = "+tenantId+" "
					+ approvalNullCondition
					+ "        GROUP  BY recon_reference , "
					+ "                  "+sGroupByField+", dv.`"+currQualifier+"`) recon_src "
					+ "       JOIN (SELECT recon_reference, "
					+ "                    Count(*)                               tr_count, "
					+ "                    Sum(Ifnull(recon.target_amount, 0))    tr_amount_sum, "
					+ "                    Sum(Ifnull(recon.tolerance_amount, 0)) tr_tolerance_amount "
					+ "             FROM   t_reconciliation_result recon "
					+ reconRulesLeftJoin+ ","
					+ " `"+tdv.getDataViewName().toLowerCase()+"` dv "
					+ "             WHERE  reconciliation_rule_group_id = "+groupId+" "
					+ "                    AND target_view_id = "+tViewId+" "
					+ "                    AND current_record_flag IS TRUE "
					+ "                    AND recon.target_row_id = dv.scrids "
					+ "                    AND recon_status = 'RECONCILED' "
					+ "                    AND recon.tenant_id = "+tenantId+" "
					+ approvalNullCondition
					+ "             GROUP  BY recon_reference "
					+") recon_tr "
					+ "         ON recon_src.recon_reference = recon_tr.recon_reference "
					+ "	   group by "
					+ groupingCol+", recon_src.currency_column";

			log.info("Query for fetching reconciliation grouping summary info: " + query);
			String paginationQuery = query + " LIMIT "+pageNumber +", "+ pageSize;
			String totalCountQuery = "SELECT IFNULL(SUM(recon_count.src_count),0) source_count, IFNULL(SUM(recon_count.tr_count), 0) target_count, IFNULL(SUM(src_amount), 0) source_amount, IFNULL(SUM(tr_amount), 0) target_amount FROM ("
					+query+") recon_count";
			String totalPaginationCountQuery = "SELECT COUNT(*) count FROM ("+query+") count";
			NumberFormat numFormat = NumberFormat.getInstance();
			result = stmt.executeQuery(paginationQuery);
			totalCountResultSet = totalCountStatement.executeQuery(totalPaginationCountQuery);
			
			totalCountRS = totalCountStmt.executeQuery(totalCountQuery);
			while(totalCountResultSet.next())
			{
				totalCount = totalCount + Long.parseLong(totalCountResultSet.getString("count"));
			}
			
			while(result.next()) 
			{
				HashMap map = new HashMap();
				String sAmount = "0.0";
				String tAmount = "0.0";
				String sCount = "0";
				String tCount = "0";
				if (result.getString("src_amount") != null) {
					sAmount = result.getString("src_amount").toString();
				}
				if (result.getString("tr_amount") != null) {
					tAmount = result.getString("tr_amount").toString();
				}
				if (result.getString("src_count") != null) {
					sCount = result.getString("src_count").toString();
				}
				if (result.getString("tr_count") != null) {
					tCount = result.getString("tr_count").toString();
				}

				map.put("sAmount", Double.parseDouble(sAmount));
				map.put("dsCount", Long.parseLong(sCount));
				
				if("rules".equalsIgnoreCase(groupBy))
				{
					map.put("id", Long.parseLong(result.getString("reconciliation_rule_id").toString()));
				}
				else if("approvalRule".equalsIgnoreCase(groupBy))
				{
					map.put("id", Long.parseLong(result.getString("approval_rule_id").toString()));
				}
				if("approvalStatus".equalsIgnoreCase(groupBy))
				{
					if("IN_PROCESS".equalsIgnoreCase(result.getString("grouping_col").toString()))
					{
						map.put("name", "Awaiting for Approvals");
					}
					else
					{
						if(approvalLookUps.containsKey(result.getString("grouping_col").toString()))
						{
							map.put("name", approvalLookUps.get(result.getString("grouping_col").toString()));
						}
						else
						{
							map.put("name", result.getString("grouping_col").toString());							
						}
					}					
				}
				else
				{
					map.put("name", result.getString("grouping_col").toString());
				}
				map.put("currency", result.getString("currency_column"));
				map.put("dtCount", Long.parseLong(tCount));
				map.put("sCount", numFormat.format(Long.parseLong(sCount)));
				map.put("tCount", numFormat.format(Long.parseLong(tCount)));
				map.put("tAmount", Double.parseDouble(tAmount));
				finalList.add(map);
			}
			finalList.add(grpMap);
			while(totalCountRS.next()) 
			{
				totalSrcCount = totalSrcCount+Long.parseLong(totalCountRS.getString("source_count"));
				totalTrCount = totalTrCount+Long.parseLong(totalCountRS.getString("target_count"));
				totalSrcAmount = totalSrcAmount + Double.parseDouble(totalCountRS.getString("source_amount"));
				totalTrAmount = totalTrAmount + Double.parseDouble(totalCountRS.getString("target_amount"));
			}
			info.put("totalSrcAmount", totalSrcAmount);
			info.put("totalTrAmount", totalTrAmount);
/*			info.put("totalSrcCount", numFormat.format(totalSrcCount));
			info.put("totalTrCount", numFormat.format(totalTrCount));*/
			info.put("totalSrcCount",totalSrcCount);
			info.put("totalTrCount", totalTrCount);
			info.put("totalCount", totalCount);
		} 
		catch(Exception e) 
		{
			log.info("Exception while fetching reconciliation grouping summary info. "+ e);
		} 
		finally 
		{
			try{
				
				if (result != null)
					result.close();
				if(totalCountRS != null)
					totalCountRS.close();
				if(totalCountResultSet != null)
					totalCountResultSet.close();
				if (stmt != null)
					stmt.close();
				if(totalCountStmt != null)
					totalCountStmt.close();
				if(totalCountStatement != null)
					totalCountStatement.close();
				if (conn != null)
					conn.close();
			}
			catch(Exception e) 
			{
				log.info("Exception while closing jdbc statements.(Fetching un reconciled transactions.) "+ e);
			}
		}
		finalMap.put("summary", finalList);
		finalMap.put("info", info);
		//finalMap.put("groupBy", groupingBy);
		return finalMap;
	}
	
	
	public void createTempTable(String viewName)
	{
		String stempQuery = "create temporary table if not exists `"+viewName+"_tmp` as select * from `"+viewName+"`";
		String stempQuery1 = "create temporary table if not exists `"+viewName+"_tmp_1` as select * from `"+viewName+"`";
		
/*		Session session = sessionFactory.getCurrentSession();*/
		
		log.info("Temp Query: "+stempQuery);
/*		Connection conn = null;
		Statement stmt = null;*/
/*		ResultSet result = null;*/
		try
		{
/*			JdbcTemplate insert = new JdbcTemplate(dataSource);*/
			
			// DataSource ds = (DataSource) ApplicationContextProvider.getApplicationContext().getBean("dataSource");
		//	conn = ds.getConnection();
		//	stmt = conn.createStatement();
			/*result = stmt.executeQuery(stempQuery);*/
			//int x = stmt.executeUpdate(stempQuery);
/*			session.beginTransaction();
			Query query = session.createQuery(stempQuery);
			query.executeUpdate();
			
			Query query1 = session.createQuery(stempQuery1);
			query1.executeUpdate();*/
			
/*			int x = masterJdbcTemplate.update(stempQuery);
			
			int y = masterJdbcTemplate.update(stempQuery1);*/
			
/*			String query = "";
			insert.query(query);*/
			
			log.info("Created "+viewName+"_tmp Table");
			log.info("Created "+viewName+"_tmp_1 Table");	
	/*		log.info("[x="+x+", y = "+y+"]");*/
		}
		catch(Exception e)
		{
			log.info("Exception while creating temporary table "+e);
		}
		finally
		{
			try{
/*				if(result != null)
					result.close();*/
		/*		if (stmt != null)
					stmt.close();*/
/*				if (conn != null)
					conn.close();*/
			}
			catch(Exception e) {
				log.info("Exception while closing jdbc statements.(Fetching un reconciled transactions.) "+ e);
			}
		}
	}
	
	public HashMap getUnReconGroupingInfo(String groupingColumns, Long sViewId, String periodFactor, String rangeFrom, String rangeTo, Long groupId,
			String ascOrDesc, Long pageNumber, Long pageSize, HashMap grpCols, String grpByCols/*List<String> columNames*/
			/* String groupBy, String ascOrDesc,*/) throws SQLException
	{
		HashMap finalMap = new HashMap();
		List<HashMap> finalList = new ArrayList<>();
		HashMap info = new HashMap();
	
		Connection conn = null;
		Statement stmt = null;
		ResultSet result = null;
		
		Statement totalCountStatement = null;
		ResultSet totalCountResultSet = null;

		try {
			NumberFormat numFormat = NumberFormat.getInstance();
			DataSource ds = (DataSource) ApplicationContextProvider.getApplicationContext().getBean("dataSource");
			conn = ds.getConnection();
			stmt = conn.createStatement();
			//totalCountStmt = conn.createStatement();
			totalCountStatement = conn.createStatement();
			String sAmountCol = getViewColumnQualifier(BigInteger.valueOf(sViewId), "AMOUNT");
			DataViews sdv = dataViewsRepository.findOne(sViewId.longValue());
			String query = "SELECT "+groupingColumns + ", "
					+ "               Sum(`"+sAmountCol+"`)  source_amount, "
					+ "               Sum(1)             src_count"
					+ "        FROM   `"+sdv.getDataViewName().toLowerCase()+"` dv "
					+ "        WHERE  Date(`"+periodFactor+"`) BETWEEN '"+rangeFrom+"' AND '"+rangeTo+"' "
					+ "               AND scrids NOT IN (SELECT original_row_id "
					+ "                                  FROM   t_reconciliation_result "
					+ "                                  WHERE  reconciliation_rule_group_id = "+groupId+" "
					+ "                                         AND original_view_id = "+sViewId+" "
					+ "                                         AND recon_status = 'RECONCILED' "
					+ "                                         AND current_record_flag IS TRUE) "
					+ "        GROUP BY "+grpByCols+" ORDER BY "+grpByCols+" "+ascOrDesc;
			String paginationQuery = query+" LIMIT "+pageNumber+", "+ pageSize;
			log.info("Query to fetching un reconciled grouping summary info: "+ paginationQuery);
			
			String totalCountQuery = "SELECT IFNULL(SUM(unrecon_count.src_count), 0) source_count, IFNULL(SUM(unrecon_count.source_amount), 0) source_amount FROM ("+query+") unrecon_count";
			String totalPaginationCountQuery = "SELECT COUNT(*) count from ("+ query +") count";
			result = stmt.executeQuery(paginationQuery);
			totalCountResultSet = totalCountStatement.executeQuery(totalPaginationCountQuery);
			Long totalPaginationCount = 0L;
			while(totalCountResultSet.next()) 
			{
				totalPaginationCount = totalPaginationCount + Long.parseLong(totalCountResultSet.getString("count"));
			}
			ResultSetMetaData rsmd = result.getMetaData();
			int colCount = rsmd.getColumnCount();
			while(result.next()) 
			{
				String sAmount = "0.0";
				String sCount = "0";
				
				HashMap map = new HashMap();
				for(int i = 1; i <= colCount; i++) 
				{
					if(grpCols.get(rsmd.getColumnName(i).toString()) != null)
					{
						map.put(grpCols.get(rsmd.getColumnName(i).toString()), result.getString(rsmd.getColumnName(i).toString()));
					}
				}
				
				if(result.getString("source_amount") != null) 
				{
					sAmount = result.getString("source_amount").toString();
				}
				if(result.getString("src_count") != null) 
				{
					sCount = result.getString("src_count").toString();
				}

				map.put("sAmount",Double.parseDouble(sAmount));
				map.put("dsCount", Long.parseLong(sCount));
				map.put("sCount",numFormat.format(Long.parseLong(sCount)));
				
				finalList.add(map);
			}
			info.put("totalCount", totalPaginationCount);
		}
		catch(Exception e)
		{
			log.info("Exception while fetching reconciled count and amounts. "+e);
		}
		finally{
			try{
				if(result != null)
					result.close();
				if(stmt != null)
					stmt.close();
				if(totalCountStatement != null)
					totalCountStatement.close();
				if(totalCountResultSet != null)
					totalCountResultSet.close();
				if (conn != null)
					conn.close();
			}
			catch(Exception e) 
			{
				log.info("Exception while closing jdbc statements.(Fetching un reconciled transactions.) "+ e);
			}
			
			finalMap.put("summary", finalList);
			finalMap.put("info", info);
		}
/*		
		log.info("Service for fetching reconciled grouping summary info");
		HashMap finalMap = new HashMap();
		
		List<HashMap> finalList = new ArrayList<>();
		HashMap info = new HashMap();

		String sAmountCol = getViewColumnQualifier(BigInteger.valueOf(sViewId), "AMOUNT");
		String currencyCol = getViewColumnQualifier(BigInteger.valueOf(sViewId), "CURRENCYCODE");
		
		Connection conn = null;
		Statement stmt = null;
		Statement totalCountStmt = null;
		ResultSet totalCountRS = null;
		ResultSet result = null;
		
		Statement totalCountStatement = null;
		ResultSet totalCountResultSet = null;
		
		Double totalSrcAmount = 0.0;
		Long totalSrcCount = 0L;
		Long totalCount = 0L;
		Long totalPaginationCount = 0L;
		
		try {
			NumberFormat numFormat = NumberFormat.getInstance();
			DataSource ds = (DataSource) ApplicationContextProvider.getApplicationContext().getBean("dataSource");
			conn = ds.getConnection();
			stmt = conn.createStatement();
			totalCountStmt = conn.createStatement();
			totalCountStatement = conn.createStatement();
			
			DataViews sdv = dataViewsRepository.findOne(sViewId.longValue());
			String query = "SELECT "+srcGrpColumn+" grouping_col, "
					+ "               Sum(`"+sAmountCol+"`)  source_amount, "
					+ "				  dv.`"+currencyCol+"` currency_column, "
					+ "               Sum(1)             src_count"
					+ "        FROM   `"+sdv.getDataViewName().toLowerCase()+"` dv "
					+ "        WHERE  Date(`"+periodFactor+"`) BETWEEN '"+rangeFrom+"' AND '"+rangeTo+"' "
					+ "               AND scrids NOT IN (SELECT original_row_id "
					+ "                                  FROM   t_reconciliation_result "
					+ "                                  WHERE  reconciliation_rule_group_id = "+groupId+" "
					+ "                                         AND original_view_id = "+sViewId+" "
					+ "                                         AND recon_status = 'RECONCILED' "
					+ "                                         AND current_record_flag IS TRUE) "
					+ "        GROUP  BY "+srcGrpColumn +", dv.`"+currencyCol+"` ORDER  BY grouping_col, currency_column "+ascOrDesc+"";
			log.info("Query to fetching un reconciled grouping summary info: "+ query);
			String paginationQuery = query+" LIMIT "+pageNumber+", "+ pageSize;
			String totalCountQuery = "SELECT IFNULL(SUM(unrecon_count.src_count), 0) source_count, IFNULL(SUM(unrecon_count.source_amount), 0) source_amount FROM ("+query+") unrecon_count";
			String totalPaginationCountQuery = "SELECT COUNT(*) count from ("+ query +") count";
			result = stmt.executeQuery(paginationQuery);
			totalCountResultSet = totalCountStatement.executeQuery(totalPaginationCountQuery);
			while(totalCountResultSet.next()) 
			{
				totalPaginationCount = totalPaginationCount + Long.parseLong(totalCountResultSet.getString("count"));
			}
				while(result.next()) 
				{
					String sAmount = "0.0";
					String sCount = "0";
					HashMap map = new HashMap();
					if(result.getString("source_amount") != null) 
					{
						sAmount = result.getString("source_amount").toString();
					}
					if(result.getString("src_count") != null) 
					{
						sCount = result.getString("src_count").toString();
					}
					map.put("sAmount",Double.parseDouble(sAmount));
					map.put("dsCount", Long.parseLong(sCount));
					map.put("name", result.getString("grouping_col").toString());
					map.put("currency", result.getString("currency_column"));
					map.put("sCount",numFormat.format(Long.parseLong(sCount)));
					totalSrcAmount = totalSrcAmount + Double.parseDouble(sAmount);
					totalSrcCount = totalSrcCount + Long.parseLong(sCount);
					finalList.add(map);
				}
				totalCountRS = totalCountStmt.executeQuery(totalCountQuery);
				while(totalCountRS.next())
				{
					totalCount = totalCount + Long.parseLong(totalCountRS.getString("source_count"));
					totalSrcAmount = totalSrcAmount + Double.parseDouble(totalCountRS.getString("source_amount"));
				}
			info.put("totalSrcAmount", totalSrcAmount);
			info.put("totalSrcCount",totalCount);
			info.put("totalCount", totalPaginationCount);
		}
		catch(Exception e)
		{
			log.info("Exception while fetching reconciled count and amounts. "+e);
		}
		finally{
			try{
				if(result != null)
					result.close();
				if(totalCountRS != null)
					totalCountRS.close();
				if(stmt != null)
					stmt.close();
				if(totalCountStmt != null)
					totalCountStmt.close();
				if(totalCountStatement != null)
					totalCountStatement.close();
				if(totalCountResultSet != null)
					totalCountResultSet.close();
				if (conn != null)
					conn.close();
			}
			catch(Exception e) 
			{
				log.info("Exception while closing jdbc statements.(Fetching un reconciled transactions.) "+ e);
			}
		}
		finalMap.put("summary", finalList);
		finalMap.put("info", info);*/
		return finalMap;
	}
	
	
	public HashMap getUnReconGroupingSummaryInfo(Long groupId, Long sViewId, Long tViewId, String periodFactor, String srcGrpColumn, String rangeFrom, String rangeTo,
			String groupBy, String ascOrDesc, Long pageNumber, Long pageSize) throws SQLException, java.text.ParseException
	{
		
		log.info("Service for fetching reconciled grouping summary info");
		HashMap finalMap = new HashMap();
		
		List<HashMap> finalList = new ArrayList<>();
		HashMap info = new HashMap();

		String sAmountCol = getViewColumnQualifier(BigInteger.valueOf(sViewId), "AMOUNT");
		String currencyCol = getViewColumnQualifier(BigInteger.valueOf(sViewId), "CURRENCYCODE");
		
		Connection conn = null;
		Statement stmt = null;
		Statement totalCountStmt = null;
		ResultSet totalCountRS = null;
		ResultSet result = null;
		
		Statement totalCountStatement = null;
		ResultSet totalCountResultSet = null;
		
		Double totalSrcAmount = 0.0;
		Long totalSrcCount = 0L;
		Long totalCount = 0L;
		Long totalPaginationCount = 0L;
		
		try {
			NumberFormat numFormat = NumberFormat.getInstance();
			DataSource ds = (DataSource) ApplicationContextProvider.getApplicationContext().getBean("dataSource");
			conn = ds.getConnection();
			stmt = conn.createStatement();
			totalCountStmt = conn.createStatement();
			totalCountStatement = conn.createStatement();
			
			DataViews sdv = dataViewsRepository.findOne(sViewId.longValue());
			String query = "SELECT "+srcGrpColumn+" grouping_col, "
					+ "               Sum(`"+sAmountCol+"`)  source_amount, "
					+ "				  dv.`"+currencyCol+"` currency_column, "
					+ "               Sum(1)             src_count"
					+ "        FROM   `"+sdv.getDataViewName().toLowerCase()+"` dv "
					+ "        WHERE  Date(`"+periodFactor+"`) BETWEEN '"+rangeFrom+"' AND '"+rangeTo+"' "
					+ "               AND scrids NOT IN (SELECT original_row_id "
					+ "                                  FROM   t_reconciliation_result "
					+ "                                  WHERE  reconciliation_rule_group_id = "+groupId+" "
					+ "                                         AND original_view_id = "+sViewId+" "
					+ "                                         AND recon_status = 'RECONCILED' "
					+ "                                         AND current_record_flag IS TRUE) "
					+ "        GROUP  BY "+srcGrpColumn +", dv.`"+currencyCol+"` ORDER  BY grouping_col, currency_column "+ascOrDesc+"";
			log.info("Query to fetching un reconciled grouping summary info: "+ query);
			String paginationQuery = query+" LIMIT "+pageNumber+", "+ pageSize;
			String totalCountQuery = "SELECT IFNULL(SUM(unrecon_count.src_count), 0) source_count, IFNULL(SUM(unrecon_count.source_amount), 0) source_amount FROM ("+query+") unrecon_count";
			String totalPaginationCountQuery = "SELECT COUNT(*) count from ("+ query +") count";
			result = stmt.executeQuery(paginationQuery);
			totalCountResultSet = totalCountStatement.executeQuery(totalPaginationCountQuery);
			while(totalCountResultSet.next()) 
			{
				totalPaginationCount = totalPaginationCount + Long.parseLong(totalCountResultSet.getString("count"));
			}
				while(result.next()) 
				{
					String sAmount = "0.0";
					String sCount = "0";
					HashMap map = new HashMap();
					if(result.getString("source_amount") != null) 
					{
						sAmount = result.getString("source_amount").toString();
					}
					if(result.getString("src_count") != null) 
					{
						sCount = result.getString("src_count").toString();
					}
					map.put("sAmount",Double.parseDouble(sAmount));
					map.put("dsCount", Long.parseLong(sCount));
					map.put("name", result.getString("grouping_col").toString());
					map.put("currency", result.getString("currency_column"));
					map.put("sCount",numFormat.format(Long.parseLong(sCount)));
					totalSrcAmount = totalSrcAmount + Double.parseDouble(sAmount);
					totalSrcCount = totalSrcCount + Long.parseLong(sCount);
					finalList.add(map);
				}
				totalCountRS = totalCountStmt.executeQuery(totalCountQuery);
				while(totalCountRS.next())
				{
					totalCount = totalCount + Long.parseLong(totalCountRS.getString("source_count"));
					totalSrcAmount = totalSrcAmount + Double.parseDouble(totalCountRS.getString("source_amount"));
				}
			info.put("totalSrcAmount", totalSrcAmount);
			info.put("totalSrcCount",totalCount);
			info.put("totalCount", totalPaginationCount);
		}
		catch(Exception e)
		{
			log.info("Exception while fetching reconciled count and amounts. "+e);
		}
		finally{
			try{
				if(result != null)
					result.close();
				if(totalCountRS != null)
					totalCountRS.close();
				if(stmt != null)
					stmt.close();
				if(totalCountStmt != null)
					totalCountStmt.close();
				if(totalCountStatement != null)
					totalCountStatement.close();
				if(totalCountResultSet != null)
					totalCountResultSet.close();
				if (conn != null)
					conn.close();
			}
			catch(Exception e) 
			{
				log.info("Exception while closing jdbc statements.(Fetching un reconciled transactions.) "+ e);
			}
		}
		finalMap.put("summary", finalList);
		finalMap.put("info", info);
		return finalMap;
	}


	public List<LinkedHashMap> getUnReconTransactionsData(String srcOrTrgt, Long groupId, Long viewId, String periodFactor, String rangeFrom, String rangeTo, String filterQuery, Long pageStartIndx,
			Long pageSize, String columnSearchQuery, String sortingQuery, HashMap colDataTypeMap, /*String currenciesString, */String globalSearchQuery) throws NumberFormatException, SQLException 
	{
		log.info("Fetching Un Reconciliation Data...");
		List<LinkedHashMap> finalList = new ArrayList<LinkedHashMap>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet result = null;
		Statement stmt2 = null;
		ResultSet result2 = null;
		String query = "";
		String totalCountQuery = "";
/*		String currencyQuery = "";
		String currencyColumn = reconciliationResultService.getViewColumnQualifier(BigInteger.valueOf(viewId), "CURRENCYCODE");
		if(currenciesString.length()>0)
		{
			currencyQuery = " AND `"+currencyColumn+"` in ("+currenciesString+")"; 
		}*/
		try {

			DataSource ds = (DataSource) ApplicationContextProvider.getApplicationContext().getBean("dataSource");
			conn = ds.getConnection();
			stmt = conn.createStatement();
			String amountQualifier = reconciliationResultService.getViewColumnQualifier(BigInteger.valueOf(viewId), "AMOUNT");
			DataViews dv = dataViewsRepository.findOne(viewId);

			if("source".equalsIgnoreCase(srcOrTrgt)) 
			{
				query = "SELECT * FROM `"
						+ dv.getDataViewName().toLowerCase()
						+ "` dv "
						+ " WHERE Date(`"
						+ periodFactor
						+ "`) BETWEEN '"
						+ rangeFrom
						+ "' AND '"
						+ rangeTo
						+ "' "
						/*+ currencyQuery*/
						+ filterQuery
						+ columnSearchQuery
						+ globalSearchQuery
						+ " AND scrids NOT IN (SELECT original_row_id FROM t_reconciliation_result "
						+ " WHERE reconciliation_rule_group_id = " + groupId
						+ " " + " AND original_view_id = " + viewId + " "
						+ " AND recon_status = 'RECONCILED' "
						+ " AND current_record_flag IS TRUE) "+sortingQuery+" limit "
						+ pageStartIndx + ", " + pageSize;

				totalCountQuery = "SELECT count(*) FROM `"
						+ dv.getDataViewName().toLowerCase()
						+ "` dv "
						+ " WHERE Date(`"
						+ periodFactor
						+ "`) BETWEEN '"
						+ rangeFrom
						+ "' AND '"
						+ rangeTo
						+ "' "
						/*+ currencyQuery*/
						+ filterQuery
						+ columnSearchQuery
						+ globalSearchQuery
						+ " AND scrids NOT IN (SELECT original_row_id FROM t_reconciliation_result "
						+ " WHERE reconciliation_rule_group_id = " + groupId
						+ " " + " AND original_view_id = " + viewId + " "
						+ " AND recon_status = 'RECONCILED' "
						+ " AND current_record_flag IS TRUE) ";
			} 
			else 
			{
				query = "SELECT * FROM `"
						+ dv.getDataViewName().toLowerCase()
						+ "` dv where 1=1 "
/*						+ " WHERE  Date(`"
						+ periodFactor
						+ "`) BETWEEN '"
						+ rangeFrom
						+ "' AND '"
						+ rangeTo
						+ "' "
*/						+ columnSearchQuery
						+ globalSearchQuery
						+ " AND scrids NOT IN (SELECT target_row_id FROM t_reconciliation_result "
						+ " WHERE reconciliation_rule_group_id = " + groupId
						+ " " + " AND target_view_id = " + viewId + " "
						+ " AND recon_status = 'RECONCILED' "
						+ " AND current_record_flag IS TRUE) "+sortingQuery+" limit "
						+ pageStartIndx + ", " + pageSize;
				
				totalCountQuery = "SELECT count(*) FROM `"
						+ dv.getDataViewName().toLowerCase()
						+ "` dv where 1=1 "
/*						+ " WHERE  Date(`"
						+ periodFactor
						+ "`) BETWEEN '"
						+ rangeFrom
						+ "' AND '"
						+ rangeTo
						+ "' "*/
						+ columnSearchQuery
						+ globalSearchQuery
						+ " AND scrids NOT IN (SELECT target_row_id FROM t_reconciliation_result "
						+ " WHERE reconciliation_rule_group_id = " + groupId
						+ " " + " AND target_view_id = " + viewId + " "
						+ " AND recon_status = 'RECONCILED' "
						+ " AND current_record_flag IS TRUE)";
			}
			log.info("Query for fetching Un-Reconciled Transactions Data: "+ query);
			result = stmt.executeQuery(query);
			stmt2 = conn.createStatement();
			result2 = stmt2.executeQuery(totalCountQuery);
			ResultSetMetaData rsmd = result.getMetaData();
			colDataTypeMap.put("rowDescription", "STRING");
			colDataTypeMap.put("adjustmentType", "STRING");
			int colCount = rsmd.getColumnCount();
			while (result.next()) {
				LinkedHashMap hm = new LinkedHashMap();
				for (int i = 1; i <= colCount; i++) {
					if(colDataTypeMap.get(rsmd.getColumnName(i)) != null)
					{
						if("DECIMAL".equalsIgnoreCase(colDataTypeMap.get(rsmd.getColumnName(i).toString()).toString()))
						{
							/*hm.put(rsmd.getColumnName(i), getAmountInFormat(result.getString(i).toString(), "US"));*/
							hm.put(rsmd.getColumnName(i), Double.parseDouble(result.getString(i).toString()));
						}
						else
						{
							hm.put(rsmd.getColumnName(i), result.getString(i));
						}
					}
				}
				hm.put("dataRowId", result.getString("scrIds"));
				hm.remove("scrIds");
				hm.remove("srcFileInbId");
				hm.remove("fileDate");
				finalList.add(hm);
			}
			Long totalCount = 0L;
			while (result2.next()) {
				if (result2.getString(1) != null) {
					totalCount = Long.parseLong(result2.getString(1).toString());
				}
			}
			HashMap info = new HashMap();
			info.put("totalCount", totalCount);
			info.put("amountQualifier", amountQualifier);
			LinkedHashMap infoObj = new LinkedHashMap();
			infoObj.put("info", info);
			finalList.add(infoObj);
		} catch (Exception e) {
			log.info("Exception while fetching un-reconciliation transactions: "
					+ e);
		} finally {
			try {
				if (result != null)
					result.close();
				if (result2 != null)
					result2.close();
				if (stmt != null)
					stmt.close();
				if (stmt2 != null)
					stmt2.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				log.info("Exception while closing jdbc statements.(Fetching un reconciled transactions.) "+ e);
			}
		}
		return finalList;
	}

	
	public HashMap getUnReconciledData(String sourceOrTarget, Long groupId, Long viewId, String filterQuery, String columnSearchQuery, 
			String globalSearchQuery, String sortingQuery, Long pageStartIndx, Long pageSize, HashMap colDataTypeMap,
			String periodFactor, String rangeFrom, String rangeTo)
	{
		HashMap finalMap = new HashMap();
		String query = "";
		String totalCountQuery = "";
		
		Connection conn = null;
		Statement stmt = null;
		Statement totalCountStmt = null;
		ResultSet result = null;
		ResultSet totalCountRS = null;
		List<HashMap> finalList = new ArrayList<HashMap>();
		Long totalCount = 0L;
		HashMap info = new HashMap();
		DataViews dv = dataViewsRepository.findOne(viewId);

		try
		{
			DataSource ds = (DataSource) ApplicationContextProvider.getApplicationContext().getBean("dataSource");
			conn = ds.getConnection();
			stmt = conn.createStatement();
			totalCountStmt = conn.createStatement();
			if("source".equalsIgnoreCase(sourceOrTarget))
			{
				query = "SELECT * FROM `"
						+ dv.getDataViewName().toLowerCase()
						+ "` dv "
						+ " WHERE Date(`"
						+ periodFactor
						+ "`) BETWEEN '"
						+ rangeFrom
						+ "' AND '"
						+ rangeTo
						+ "' "
						/*+ currencyQuery*/
						+ filterQuery
						+ columnSearchQuery
						+ globalSearchQuery
						+ " AND scrids NOT IN (SELECT original_row_id FROM t_reconciliation_result "
						+ " WHERE reconciliation_rule_group_id = " + groupId
						+ " " + " AND original_view_id = " + viewId + " "
						+ " AND recon_status = 'RECONCILED' "
						+ " AND current_record_flag IS TRUE) "+sortingQuery+" limit "
						+ pageStartIndx + ", " + pageSize;

				totalCountQuery = "SELECT count(*) FROM `"
						+ dv.getDataViewName().toLowerCase()
						+ "` dv "
						+ " WHERE Date(`"
						+ periodFactor
						+ "`) BETWEEN '"
						+ rangeFrom
						+ "' AND '"
						+ rangeTo
						+ "' "
						/*+ currencyQuery*/
						+ filterQuery
						+ columnSearchQuery
						+ globalSearchQuery
						+ " AND scrids NOT IN (SELECT original_row_id FROM t_reconciliation_result "
						+ " WHERE reconciliation_rule_group_id = " + groupId
						+ " " + " AND original_view_id = " + viewId + " "
						+ " AND recon_status = 'RECONCILED' "
						+ " AND current_record_flag IS TRUE) ";

			}
			else if("target".equalsIgnoreCase(sourceOrTarget))
			{
				query = "SELECT * FROM `"
						+ dv.getDataViewName().toLowerCase()
						+ "` dv where 1=1 "
						+ columnSearchQuery
						+ globalSearchQuery
						+ " AND scrids NOT IN (SELECT target_row_id FROM t_reconciliation_result "
						+ " WHERE reconciliation_rule_group_id = " + groupId
						+ " " + " AND target_view_id = " + viewId + " "
						+ " AND recon_status = 'RECONCILED' "
						+ " AND current_record_flag IS TRUE) "+sortingQuery+" limit "
						+ pageStartIndx + ", " + pageSize;
				
				totalCountQuery = "SELECT count(*) FROM `"
						+ dv.getDataViewName().toLowerCase()
						+ "` dv where 1=1 "
						+ columnSearchQuery
						+ globalSearchQuery
						+ " AND scrids NOT IN (SELECT target_row_id FROM t_reconciliation_result "
						+ " WHERE reconciliation_rule_group_id = " + groupId
						+ " " + " AND target_view_id = " + viewId + " "
						+ " AND recon_status = 'RECONCILED' "
						+ " AND current_record_flag IS TRUE)";
			}
			log.info("Query for fetching un-reconciled data: "+query);
			result = stmt.executeQuery(query);
			totalCountRS = totalCountStmt.executeQuery(totalCountQuery);

			// Total Count
			while(totalCountRS.next()) 
			{
				if(totalCountRS.getString(1) != null) 
				{
					totalCount = Long.parseLong(totalCountRS.getString(1));
				}
			}
			// Fetching Transactions
			ResultSetMetaData rsmd = result.getMetaData();
			colDataTypeMap.put("rowDescription", "STRING");
			colDataTypeMap.put("adjustmentType", "STRING");
			int colCount = rsmd.getColumnCount();
			while (result.next()) {
				HashMap hm = new HashMap();
				for (int i = 1; i <= colCount; i++) {
					if(colDataTypeMap.get(rsmd.getColumnName(i)) != null)
					{
						if("DECIMAL".equalsIgnoreCase(colDataTypeMap.get(rsmd.getColumnName(i).toString()).toString()))
						{
							hm.put(rsmd.getColumnName(i), Double.parseDouble(result.getString(i).toString()));
						}
						else
						{
							hm.put(rsmd.getColumnName(i), result.getString(i));
						}
					}
				}
				hm.put("dataRowId", result.getString("scrIds"));
				hm.remove("scrIds");
				hm.remove("srcFileInbId");
				hm.remove("fileDate");
				finalList.add(hm);
			}
			
		}
		catch(Exception e)
		{
			log.info("Exception while fetching Un-Reconciled data: "+e);
		}
		finally
		{
			try 
			{
				if(result != null)
					result.close();
				if(totalCountRS != null)
					totalCountRS.close();
				if(stmt != null)
					stmt.close();
				if(totalCountStmt != null)
					totalCountStmt.close();
				if(conn != null)
					conn.close();
			} 
			catch(Exception e) 
			{
				log.info("Exception while closing jdbc statements.(Fetching un reconciled transactions.) "+ e);
			}
		}
		info.put("totalCount", totalCount);
		finalMap.put("data", finalList);
		finalMap.put("info", info);
		return finalMap;
	}
	
	
	public HashMap getReconRefChildData(String sourceOrTarget, Long viewId, Long groupId, String reconReference, Long tenantId, String varOrNonVariance,
			Long limit, Long pageSize) throws SQLException 
	{
		HashMap finalMap = new HashMap();
		List<LinkedHashMap> finalList = new ArrayList<LinkedHashMap>();
		Connection conn = null;
		
		Statement stmt = null;
		ResultSet result = null;
		
		try 
		{
			DataViews dv = dataViewsRepository.findById(viewId);
			String query = "";
			if("source".equalsIgnoreCase(sourceOrTarget)) 
			{
				query = "select recon_reference, dv.*, ifnull(tolerance_amount, 0) tolerance_amount from t_reconciliation_result recon, `"
						+ dv.getDataViewName().toLowerCase()
						+ "` dv"
						+ " where "
						+ " reconciliation_rule_group_id = "
						+ groupId
						+ " and original_view_id = "
						+ viewId
						+ " and current_record_flag is true and original_row_id = dv.scrIds"
						+ " and recon_reference = '"
						+ reconReference
						+ "' and tenant_id = "
						+ tenantId
						+ " and upper(IFNULL(adjustmentType,'X')) "
						+ varOrNonVariance + " 'VARIANCE' limit "+limit+", "+pageSize;

			} 
			else if ("target".equalsIgnoreCase(sourceOrTarget)) {
				query = "select recon_reference, dv.*, ifnull(tolerance_amount, 0) tolerance_amount from t_reconciliation_result recon, `"
						+ dv.getDataViewName().toLowerCase()
						+ "` dv"
						+ " where "
						+ " reconciliation_rule_group_id = "
						+ groupId
						+ " and target_view_id = "
						+ viewId
						+ " and current_record_flag is true and target_row_id = dv.scrIds"
						+ " and recon_reference = '"
						+ reconReference
						+ "' and tenant_id = "
						+ tenantId
						+ " and upper(IFNULL(adjustmentType,'X')) "
						+ varOrNonVariance + " 'VARIANCE' limit "+limit+", "+pageSize;
			}
			log.info("Query to fetch ReconRefData: " + query);
			DataSource ds = (DataSource) ApplicationContextProvider.getApplicationContext().getBean("dataSource");
			conn = ds.getConnection();
			
			stmt = conn.createStatement();
			result = stmt.executeQuery(query);
			
			ResultSetMetaData rsmd = result.getMetaData();
			int colCount = rsmd.getColumnCount();
			Double toleranceAmount = 0.0;
			while(result.next()) 
			{
				LinkedHashMap hm = new LinkedHashMap();
				for(int i = 1; i <= colCount; i++)
				{
					hm.put(rsmd.getColumnName(i), result.getString(i));
				}
				// totalAmount = totalAmount + Double.parseDouble(hm.get(amountQualifier).toString());
				toleranceAmount = toleranceAmount + Double.parseDouble(hm.get("tolerance_amount").toString());
				hm.remove("scrIds");
				hm.remove("srcFileInbId");
				hm.remove("fileDate");
				finalList.add(hm);
			}
		}
		catch(Exception e)
		{
			log.info("Exception while fetching reconciliation child data. " + e);
		}
		finally {
			try {
				if(result != null)
					result.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			}catch (Exception e)
			{
				log.info("Exception while closing jdbc statements.(Recon Reference Data) "+ e);
			}
		}
		finalMap.put("data", finalList);
		return finalMap;
	}
	
	public HashMap getReconCustomFilterInfo(Long tenantId, Long groupId, Long sViewId, Long tViewId, String reconReference)
	{
		HashMap finalMap = new HashMap();
		DataSource ds = (DataSource) ApplicationContextProvider.getApplicationContext().getBean("dataSource");
		Connection conn = null;
		Statement stmt = null;
		ResultSet result = null;
		String query = "		select * from ("
				+ "		SELECT 			 "
				+ "						 recon_reference,"
				+ "						 Count(*)             AS src_recon_count ,"
                +"		 	Sum(ifnull(original_amount,0.0)) AS src_recon_amount, "
                +"			Sum(ifnull(tolerance_amount,0.0)) AS src_tolerance_amount "
				+ "                  FROM   t_reconciliation_result "
				+ "                  WHERE  original_view_id = "+sViewId+""
				+ "                         AND current_record_flag IS TRUE "
				+ "                         AND tenant_id = "+tenantId+" "
				+ "                         AND recon_status = 'RECONCILED'"
				+ "                         AND reconciliation_rule_group_id = "+groupId+" "
				+ "                         AND recon_reference = '"+reconReference+"') recon_src  "
				+ "       LEFT JOIN (SELECT recon_reference,"
				+ "						 Count(*)             AS tr_recon_count ,"
                +"		 	Sum(ifnull(target_amount,0.0)) AS tr_recon_amount, "
                +"			Sum(ifnull(tolerance_amount,0.0)) AS tr_tolerance_amount "
				+ "                  FROM   t_reconciliation_result "
				+ "                  WHERE  target_view_id = "+tViewId+""
				+ "                         AND current_record_flag IS TRUE "
				+ "                         AND tenant_id = "+tenantId+" "
				+ "                         AND recon_status = 'RECONCILED'"
				+ "                         AND reconciliation_rule_group_id = "+groupId+""
				+ "                         AND recon_reference = '"+reconReference+"') recon_tr "
				+ "              ON  recon_src.recon_reference = recon_tr.recon_reference";
		log.info("Query to fetching recon custom filter info: "+query);
		try 
		{
			conn = ds.getConnection();
			stmt = conn.createStatement();
			result = stmt.executeQuery(query);
			while(result.next()) 
			{
				finalMap.put("reconReference", result.getString("recon_reference"));
				finalMap.put("srcCount", Long.parseLong(result.getString("src_recon_count")));
				finalMap.put("trCount", Long.parseLong(result.getString("tr_recon_count")));
				finalMap.put("srcAmount", Double.parseDouble(result.getString("src_recon_amount")));
				finalMap.put("trAmount", Double.parseDouble(result.getString("tr_recon_amount")));
				finalMap.put("srcTolAmount", Double.parseDouble(result.getString("src_tolerance_amount")));
				finalMap.put("trTolAmount", Double.parseDouble(result.getString("tr_tolerance_amount")));
			}
		}
		catch(Exception e)
		{
			log.info("Exception while fetching reconciliation child data. " + e);
		}
		finally
		{
			try {
				if(result != null)
					result.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			}catch (Exception e)
			{
				log.info("Exception while closing jdbc statements.(Recon Reference Data) "+ e);
			}
		}
		return finalMap;
	}

	public List<String> getColumnAlilasByViewId(Long viewId) 
	{
		List<String> columnNames = new ArrayList<String>();
		List<DataViewsColumns> dvc = dataViewsColumnsRepository.findByDataViewId(viewId);
		if (dvc.size() > 0) 
		{
			for(DataViewsColumns dv : dvc) 
			{
				if("File Template".equalsIgnoreCase(dv.getRefDvType())) 
				{
					FileTemplateLines ftl = fileTemplateLinesRepository.findOne(Long.parseLong(dv.getRefDvColumn().toString()));
					if (ftl != null)
					{
						columnNames.add(ftl.getColumnAlias());
					}
				} 
				else if("Data View".equalsIgnoreCase(dv.getRefDvType()) || dv.getRefDvType() == null)
				{
					columnNames.add(dv.getColumnName());
				}
			}
		}
		return columnNames;
	}
	
	public HashMap getAmountsGroupByScrIds(Long viewId, String amountQualifier, String rowIds)
	{
		HashMap finalMap = new HashMap();
		DataSource ds = (DataSource) ApplicationContextProvider.getApplicationContext().getBean("dataSource");
		Connection conn = null;
		Statement stmt = null;
		ResultSet result = null;
		DataViews dv = dataViewsRepository.findOne(viewId);
		String query = "select scrIds, `"+amountQualifier+"` from `"+dv.getDataViewName().toLowerCase()+"` where scrIds in("+rowIds+")";
		log.info("Query to fetching recon custom filter info: "+query);
		try 
		{
			conn = ds.getConnection();
			stmt = conn.createStatement();
			result = stmt.executeQuery(query);
			while(result.next()) 
			{
				finalMap.put(result.getString(1), result.getString(2));
			}
		}
		catch(Exception e)
		{
			log.info("Exception while fetching amounts group by scrids" + e);
		}
		finally
		{
			try {
				if(result != null)
					result.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			}catch (Exception e)
			{
				log.info("Exception while closing jdbc statements."+ e);
			}
		}
		return finalMap;
	}
	
	public List<String> getReconReferences(String filterQuery, Long sViewId, Long tViewId, Long groupId, String periodFactor, String rangeFrom, String rangeTo, Long tenantId)
	{
		List<String> finalList = new ArrayList<String>();
		DataSource ds = (DataSource) ApplicationContextProvider.getApplicationContext().getBean("dataSource");
		Connection conn = null;
		Statement stmt = null;
		ResultSet result = null;
		DataViews sdv = dataViewsRepository.findOne(sViewId);
		DataViews tdv = dataViewsRepository.findOne(tViewId);
		String query = "SELECT recon_src.recon_reference"
				+ " FROM (SELECT recon_reference, reconciliation_rule_id, rl.rule_code rule_code"
				+ " FROM t_reconciliation_result recon "
				+ " LEFT JOIN t_rules rl ON recon.reconciliation_rule_id = rl.id, `"+sdv.getDataViewName().toLowerCase()+"` dv "
				+ " WHERE reconciliation_rule_group_id = "+groupId+" "
				+ " AND Date(dv.`"+periodFactor+"`) BETWEEN '"+rangeFrom+"' AND '"+rangeTo+"' "
				+ " AND original_view_id = "+sViewId+" AND current_record_flag IS TRUE AND recon.original_row_id = dv.scrids "
				+ " AND recon_status = 'RECONCILED' AND recon.tenant_id = "+tenantId+" "
				+ filterQuery 
				+ " GROUP BY recon_reference, reconciliation_rule_id, rule_code) recon_src "
				+ " JOIN (SELECT recon_reference, reconciliation_rule_id FROM t_reconciliation_result recon "
				+ " LEFT JOIN t_rules rl ON recon.reconciliation_rule_id = rl.id, `"+tdv.getDataViewName().toLowerCase()+"` dv "
				+ " WHERE reconciliation_rule_group_id = "+groupId+" "
				+ " AND target_view_id = "+tViewId+" AND current_record_flag IS TRUE AND recon.target_row_id = dv.scrids "
				+ " AND recon_status = 'RECONCILED' AND recon.tenant_id = "+tenantId+" "
				+ " GROUP BY recon_reference, reconciliation_rule_id) recon_tr "
				+ " ON recon_src.recon_reference = recon_tr.recon_reference";
		try
		{		
			log.info("Query for fetching recon references with filter query: "+ query);
			conn = ds.getConnection();
			stmt = conn.createStatement();
			result = stmt.executeQuery(query);
			while(result.next()) 
			{
				finalList.add(result.getString("recon_reference"));
			}
		}
		catch(Exception e)
		{
			log.info("Exception while fetching recon references basd on custom filter: " + e);
		}
		finally
		{
			try {
				if(result != null)
					result.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			}catch (Exception e)
			{
				log.info("Exception while closing jdbc statements."+ e);
			}
		}
		return finalList;
	}
	
	// New Changes Dynamically Fetching Summary Information
	public HashMap getColumnInfo(Long columnId)
	{
		DataViewsColumns dvc = dataViewsColumnsRepository.findOne(columnId);
		HashMap finalMap = new HashMap();
		if(dvc != null)
		{
			finalMap.put("columnId", dvc.getId());
			if("File Template".equalsIgnoreCase(dvc.getRefDvType())) 
			{
				FileTemplateLines ftl = fileTemplateLinesRepository.findOne(Long.parseLong(dvc.getRefDvColumn()));
				if(ftl != null)
				{
					finalMap.put("columnName", ftl.getColumnAlias());
				}
			}
			else if("Data View".equalsIgnoreCase(dvc.getRefDvType()) || dvc.getRefDvType() == null)
			{
				finalMap.put("columnName", dvc.getColumnName());
			}
			finalMap.put("displayName", dvc.getColumnName());
			finalMap.put("dataType", dvc.getColDataType());
		}
    	return finalMap;
	}
}