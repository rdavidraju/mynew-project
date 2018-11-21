package com.nspl.app.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
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

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nspl.app.config.ApplicationContextProvider;
import com.nspl.app.domain.AccountedSummary;
import com.nspl.app.domain.AccountingData;
import com.nspl.app.domain.AppModuleSummary;
import com.nspl.app.domain.ChartOfAccount;
import com.nspl.app.domain.DataViews;
import com.nspl.app.domain.DataViewsColumns;
import com.nspl.app.domain.FileTemplateLines;
import com.nspl.app.domain.LedgerDefinition;
import com.nspl.app.domain.Segments;
import com.nspl.app.repository.AccountedSummaryRepository;
import com.nspl.app.repository.AccountingDataRepository;
import com.nspl.app.repository.AccountingLineTypesRepository;
import com.nspl.app.repository.AcctRuleConditionsRepository;
import com.nspl.app.repository.AppModuleSummaryRepository;
import com.nspl.app.repository.ChartOfAccountRepository;
import com.nspl.app.repository.DataViewsColumnsRepository;
import com.nspl.app.repository.DataViewsRepository;
import com.nspl.app.repository.FileTemplateLinesRepository;
import com.nspl.app.repository.LedgerDefinitionRepository;
import com.nspl.app.repository.LookUpCodeRepository;
import com.nspl.app.repository.RulesRepository;
import com.nspl.app.repository.SegmentsRepository;
import com.nspl.app.web.rest.AccountingDataResource;
import com.nspl.app.web.rest.dto.CreditLineDTO;
import com.nspl.app.web.rest.dto.DebitLineDTO;
import com.nspl.app.web.rest.dto.ManualAccountingDTO;

@Service
@Transactional
public class AccountingDataService {

    private final Logger log = LoggerFactory.getLogger(AccountingDataResource.class);
    
	@Inject
	ReconciliationResultService reconciliationResultService;
	
	@Inject
	AccountingDataRepository accountingDataRepository;
	
    @Inject
    FileTemplateLinesRepository fileTemplateLinesRepository;
    
    @Inject
    LookUpCodeRepository lookUpCodeRepository;
    
    @Inject
    PropertiesUtilService propertiesUtilService;
    
    @Inject
    RulesRepository rulesRepository;
    
    @Inject
    DataViewsRepository dataViewsRepository;
    
    @Inject
    AccountingLineTypesRepository accountingLineTypesRepository;
    
    @Inject
    AcctRuleConditionsRepository acctRuleConditionsRepository;
    
    @Inject
    DataViewsColumnsRepository dataViewsColumnsRepository;
    
    @Inject
    AccountingDataService accountingDataService;
    
    @Inject
    AccountedSummaryRepository accountedSummaryRepository;
    
    @Inject
    LedgerDefinitionRepository ledgerDefinitionRepository;
    
    @Inject
    SegmentsRepository segmentsRepository;
    
	@PersistenceContext(unitName="default")
	private EntityManager em;
	
	@Inject
	AppModuleSummaryRepository appModuleSummaryRepository;
	
	@Inject
	DashBoardV4Service dashBoardV4Service;
	
	@Inject
	ChartOfAccountRepository chartOfAccountRepository;
	
    @Inject
    private Environment env;
	
    /**
     * Author: Shiva
     * Purpose: Fetching segments length based on coa reference id
     * **/
    public int getSegmentsLengthWithCoaRef(Long coaRefId)
    {
    	int segmentsLength = 0;
    	List<Segments> segments = segmentsRepository.findByCoaId(coaRefId);
    	segmentsLength = segmentsLength + segments.size();
    	return segmentsLength;
    }
    
    /**
     * Author: Shiva
     * Purpose: Fetching Accounting data view columns in sequence order
     * **/
    public LinkedHashMap getViewColumnHeadersMapInSequence(Long dataViewId, Long tenantId, Long groupId, List<String> groupedParams)
    {
    	log.info("In service for getting column headers in sequence order for viewId: "+ dataViewId+", groupId"+ groupId+", tenantId"+ tenantId);
    	List<BigInteger> remainingSequence = new ArrayList<BigInteger>();
    	LinkedHashMap finalMap = new LinkedHashMap();
    	List<BigInteger> ruleIds = rulesRepository.fetchRuleIdsByGroupId(groupId, tenantId, dataViewId);
    	log.info("Rule Ids: "+ ruleIds);
    	if(ruleIds.size()>0 && !ruleIds.contains(null))
    	{
    		List<BigInteger> sViewColumnIds = acctRuleConditionsRepository.fetchRuleIdsByTenantIdAndRuleId(ruleIds);
    		sViewColumnIds.remove(null);
    		log.info("Source View Column Ids: "+sViewColumnIds);		
    		if(sViewColumnIds.size()>0)
    		{
    			for(BigInteger id : sViewColumnIds)
    			{
    				DataViewsColumns dvc = dataViewsColumnsRepository.findOne(id.longValue());
    				if(dvc != null)
    				{
    		    		if("File Template".equalsIgnoreCase(dvc.getRefDvType()))
    		    		{
    		        		FileTemplateLines ftl = fileTemplateLinesRepository.findOne(Long.parseLong(dvc.getRefDvColumn()));
    		        		if(ftl != null)
    		        		{
    		        			finalMap.put(ftl.getColumnAlias(), dvc.getColumnName());
    		        		}
    		    		}
    		    		else if("Data View".equalsIgnoreCase(dvc.getRefDvType()) || dvc.getRefDvType() == null)
    		    		{
    		    			finalMap.put(dvc.getColumnName(), dvc.getColumnName());
    		    		}
    				}
    			}
    		}
/*    		finalMap.put("Debit_Account", "Debit_Account");
    		finalMap.put("Credit_Account", "Credit_Account");
    		finalMap.put("Ledger", "Ledger");
    		finalMap.put("Source", "Source");
    		finalMap.put("Category", "Category");
    		finalMap.put("Status", "Status");
    		finalMap.put("Approval_Status", "Approval_Status");
    		finalMap.put("Acct_Rule", "Acct_Rule");*/
    		finalMap.put("accounting_status", "Status");
    		finalMap.put("rowDescription", "rowDescription");
    		finalMap.put("adjustmentType", "adjustmentType");
/*    		finalMap.put("accounting_recon_status", "Status");*/
    		finalMap.put("line_type", "line_type");
    		finalMap.put("line_type_detail", "Line Type Detail");
    		finalMap.put("source_meaning", "Source");
    		finalMap.put("coa_ref", "coa_ref");
    		finalMap.put("coa_name", "COA Name");
    		finalMap.put("ledger_ref", "ledger_ref");
    		finalMap.put("ledger_name", "Ledger Name");
    		finalMap.put("category_meaning", "Category");
    		finalMap.put("entered_currency", "Entered Currency");
    		finalMap.put("accounted_currency", "Accounted Currency");
    		finalMap.put("rule_code", "Acct Rule");
    		finalMap.put("entered_amount", "Entered Amount");
    		finalMap.put("accounted_amount", "Accounted Amount");
    		finalMap.put("amount_col_id", "amount_col_id");
    		finalMap.put("journal_status", "Journal Status");
    		finalMap.put("status", "Accounted Status");
    		finalMap.put("accounting_ref_1", "accounting_ref_1");
    		finalMap.put("accounting_ref_2", "accounting_ref_2");
    		finalMap.put("accounting_ref_3", "accounting_ref_3");
    		finalMap.put("accounting_ref_4", "accounting_ref_4");
    		finalMap.put("accounting_ref_5", "accounting_ref_5");
    		finalMap.put("accounting_ref_6", "accounting_ref_6");
    		finalMap.put("accounting_ref_7", "accounting_ref_7");
    		finalMap.put("accounting_ref_8", "accounting_ref_8");
    		finalMap.put("accounting_ref_9", "accounting_ref_9");
    		finalMap.put("accounting_ref_10", "accounting_ref_10");
    		List<BigInteger> allViewColIds = dataViewsColumnsRepository.fetchIdsByDataViewId(dataViewId);
    		log.info("All View Ids: "+allViewColIds);
    		if(allViewColIds.size()>0)
    		{
    			for(BigInteger id : allViewColIds)
    			{
    				if(!sViewColumnIds.contains(id))
    				{
    					remainingSequence.add(id);
    				}
    			}
    		}
    		log.info("Remaining Sequnce Ids: "+remainingSequence);
    		if(remainingSequence.size()>0)
    		{
    			for(BigInteger id : remainingSequence)
    			{
    				DataViewsColumns dvc = dataViewsColumnsRepository.findOne(id.longValue());
    				if(dvc != null)
    				{
    		    		if("File Template".equalsIgnoreCase(dvc.getRefDvType()))
    		    		{
    		        		FileTemplateLines ftl = fileTemplateLinesRepository.findOne(Long.parseLong(dvc.getRefDvColumn()));
    		        		if(ftl != null)
    		        		{
    		        			finalMap.put(ftl.getColumnAlias(), dvc.getColumnName());
    		        		}
    		    		}
    		    		else if("Data View".equalsIgnoreCase(dvc.getRefDvType()) || dvc.getRefDvType() == null)
    		    		{
    		    			finalMap.put(dvc.getColumnName(), dvc.getColumnName());
    		    		}
    				}
    			}
    		}
    	}
    	return finalMap;
    }
    
    public List<String> getSegmentsCodes(AccountingData ad, int segmentsSize)
    {
    	List<String> finalList = new ArrayList<String>();
    	int i=1;
    	if(i<=segmentsSize)
    	{
    		if(ad.getAccountingRef1() != null)
    			finalList.add(ad.getAccountingRef1());
    		else 
    			finalList.add("XXXXX");
    	}
    	i++;
    	if(i<=segmentsSize)
    	{
    		if(ad.getAccountingRef2() != null)
    			finalList.add(ad.getAccountingRef2());
    		else 
    			finalList.add("XXXXX");
    	}
    	i++;
    	if(i<=segmentsSize)
    	{
    		if(ad.getAccountingRef3() != null)
    			finalList.add(ad.getAccountingRef3());
    		else 
    			finalList.add("XXXXX");
    	}
    	i++;
    	if(i<=segmentsSize)
    	{
    		if(ad.getAccountingRef4() != null)
    			finalList.add(ad.getAccountingRef4());
    		else 
    			finalList.add("XXXXX");
    	}
    	i++;
    	if(i<=segmentsSize)
    	{
    		if(ad.getAccountingRef5() != null)
    			finalList.add(ad.getAccountingRef5());
    		else 
    			finalList.add("XXXXX");
    	}
    	i++;
    	if(i<=segmentsSize)
    	{
    		if(ad.getAccountingRef6() != null)
    			finalList.add(ad.getAccountingRef6());
    		else 
    			finalList.add("XXXXX");
    	}
    	i++;
    	if(i<=segmentsSize)
    	{
    		if(ad.getAccountingRef7() != null)
    			finalList.add(ad.getAccountingRef7());
    		else 
    			finalList.add("XXXXX");
    	}
    	i++;
    	if(i<=segmentsSize)
    	{
    		if(ad.getAccountingRef8() != null)
    			finalList.add(ad.getAccountingRef8());
    		else 
    			finalList.add("XXXXX");
    	}
    	i++;
    	if(i<=segmentsSize)
    	{
    		if(ad.getAccountingRef9() != null)
    			finalList.add(ad.getAccountingRef9());
    		else 
    			finalList.add("XXXXX");
    	}
    	i++;
    	if(i<=segmentsSize)
    	{
    		if(ad.getAccountingRef10() != null)
    			finalList.add(ad.getAccountingRef10());
    		else 
    			finalList.add("XXXXX");
    	}
    	return finalList;
    }
    
    public List<HashMap> getColAlignInforAccSummary(String status)
    {
    	List<HashMap> finalMap = new ArrayList<HashMap>();
    	if(!status.toLowerCase().contains("un accounted"))
		{
    		HashMap ledgerMp = new HashMap();
    		ledgerMp.put("field", "ledger_name");
    		ledgerMp.put("header", "Ledger");
    		ledgerMp.put("columnName", "ledger_name");
    		ledgerMp.put("align", "left");
    		ledgerMp.put("width", "150px");
    		ledgerMp.put("colId", "");
    		ledgerMp.put("dataType", "STRING");
			finalMap.add(ledgerMp);
			
			HashMap sourceMp = new HashMap();
			sourceMp.put("field", "Source");
			sourceMp.put("header", "Source");
			sourceMp.put("columnName", "source_meaning");
			sourceMp.put("align", "left");
			sourceMp.put("width", "150px");
			sourceMp.put("colId", "");
			sourceMp.put("dataType", "STRING");
			finalMap.add(sourceMp);
			
			HashMap categoryMp = new HashMap();
			categoryMp.put("field", "Category");
			categoryMp.put("header", "Category");
			categoryMp.put("columnName", "category_meaning");
			categoryMp.put("align", "left");
			categoryMp.put("width", "150px");
			categoryMp.put("colId", "");
			categoryMp.put("dataType", "STRING");
			finalMap.add(categoryMp);
			
			HashMap entCurrencyMp = new HashMap();
			entCurrencyMp.put("field", "Entered Currency");
			entCurrencyMp.put("header", "Entered Currency");
			entCurrencyMp.put("columnName", "entered_currency");
			entCurrencyMp.put("align", "left");
			entCurrencyMp.put("width", "150px");
			entCurrencyMp.put("colId", "");
			entCurrencyMp.put("dataType", "STRING");
			finalMap.add(entCurrencyMp);

			HashMap acctdCurrencyMp = new HashMap();
			acctdCurrencyMp.put("field", "Accounted Currency");
			acctdCurrencyMp.put("header", "Accounted Currency");
			acctdCurrencyMp.put("columnName", "accounted_currency");
			acctdCurrencyMp.put("align", "left");
			acctdCurrencyMp.put("width", "150px");
			acctdCurrencyMp.put("colId", "");
			acctdCurrencyMp.put("dataType", "STRING");
			finalMap.add(acctdCurrencyMp);
			
			HashMap acctCountMp = new HashMap();
			acctCountMp.put("field", "count");
			acctCountMp.put("header", "Transaction Count");
			acctCountMp.put("columnName", "count");
			acctCountMp.put("align", "right");
			acctCountMp.put("width", "100px");
			acctCountMp.put("colId", "");
			acctCountMp.put("dataType", "INTEGER");
			finalMap.add(acctCountMp);
			
			HashMap jeCountMp = new HashMap();
			jeCountMp.put("field", "jeCount");
			jeCountMp.put("header", "Journals Prepared");
			jeCountMp.put("columnName", "je_count");
			jeCountMp.put("align", "right");
			jeCountMp.put("width", "100px");
			jeCountMp.put("colId", "");
			jeCountMp.put("dataType", "INTEGER");
			finalMap.add(jeCountMp);
			
			
			HashMap revCountMp = new HashMap();
			revCountMp.put("field", "reverseCount");
			revCountMp.put("header", "Reversed Entries");
			revCountMp.put("columnName", "reverse_count");
			revCountMp.put("align", "right");
			revCountMp.put("width", "100px");
			revCountMp.put("colId", "");
			revCountMp.put("dataType", "INTEGER");
			finalMap.add(revCountMp);
			
			HashMap acctAmtMp = new HashMap();
			acctAmtMp.put("field", "Accounted Amount");
			acctAmtMp.put("header", "Accounted Amount");
			acctAmtMp.put("columnName", "accounted_amount");
			acctAmtMp.put("align", "right");
			acctAmtMp.put("width", "150px");
			acctAmtMp.put("colId", "");
			acctAmtMp.put("dataType", "DECIMAL");
			finalMap.add(acctAmtMp);
		}
    	return finalMap;
    }
    
   public List<HashMap> getAccColsAlignInfo(Long viewId, Long groupId, Long tenantId, String status)
   {
	   log.info("In service for getting accounting data view columns alignment info for the view: "+ viewId+", group id: "+groupId+", tenant id: "+ tenantId);
	   	List<BigInteger> remainingSequence = new ArrayList<BigInteger>();
		List<HashMap> finalMap = new ArrayList<HashMap>();
	
   		List<BigInteger> ruleIds = rulesRepository.fetchRuleIdsByGroupId(groupId, tenantId, viewId);
	   	log.info("Rule Ids: "+ ruleIds);
	   	if(ruleIds.size()>0 && !ruleIds.contains(null))
	   	{
	   		List<BigInteger> sViewColumnIds = acctRuleConditionsRepository.fetchRuleIdsByTenantIdAndRuleId(ruleIds);
	   		sViewColumnIds.remove(null);
	   		log.info("Source View Column Ids: "+sViewColumnIds);
	   		// sequence (rules based source view columns)
	   		if(sViewColumnIds.size()>0)
	   		{
	   			finalMap.addAll(reconciliationResultService.getColAlignInfoForAcc(sViewColumnIds));
	   		}
				if(!status.toLowerCase().contains("un accounted"))
				{
					HashMap ledgerNameMp = new HashMap();
					ledgerNameMp.put("field", "Ledger Name");
					ledgerNameMp.put("header", "Ledger Name");
					ledgerNameMp.put("columnName", "ledger_name");
					ledgerNameMp.put("align", "left");
					ledgerNameMp.put("width", "150px");
					ledgerNameMp.put("colId", "");
					ledgerNameMp.put("dataType", "STRING");
					finalMap.add(ledgerNameMp);
					
					HashMap sourceMp = new HashMap();
					sourceMp.put("field", "Source");
					sourceMp.put("header", "Source");
					sourceMp.put("columnName", "source_meaning");
					sourceMp.put("align", "left");
					sourceMp.put("width", "150px");
					sourceMp.put("colId", "");
					sourceMp.put("dataType", "STRING");
					finalMap.add(sourceMp);
					
					HashMap categoryMp = new HashMap();
					categoryMp.put("field", "Category");
					categoryMp.put("header", "Category");
					categoryMp.put("columnName", "category_meaning");
					categoryMp.put("align", "left");
					categoryMp.put("width", "150px");
					categoryMp.put("colId", "");
					categoryMp.put("dataType", "STRING");
					finalMap.add(categoryMp);
					
					HashMap jrnStatusMp = new HashMap();
					jrnStatusMp.put("field", "Journal Status");
					jrnStatusMp.put("header", "Journal Status");
					jrnStatusMp.put("columnName", "journal_status");
					jrnStatusMp.put("align", "left");
					jrnStatusMp.put("width", "150px");
					jrnStatusMp.put("colId", "");
					jrnStatusMp.put("dataType", "STRING");
					finalMap.add(jrnStatusMp);
					
					HashMap acctMp = new HashMap();
					acctMp.put("field", "Accounted Status");
					acctMp.put("header", "Accounted Status");
					acctMp.put("columnName", "status");
					acctMp.put("align", "left");
					acctMp.put("width", "150px");
					acctMp.put("colId", "");
					acctMp.put("dataType", "STRING");
					finalMap.add(acctMp);
					
				}
				
	   		// Remaining columns
	   		List<BigInteger> allViewColIds = dataViewsColumnsRepository.fetchIdsByDataViewId(viewId);
	   		log.info("All View Ids: "+allViewColIds);
	   		if(allViewColIds.size()>0)
	   		{
	   			for(BigInteger id : allViewColIds)
	   			{
	   				if(!sViewColumnIds.contains(id))
	   				{
	   					remainingSequence.add(id);
	   				}
	   			}
	   		}
	   		log.info("Remaining Sequnce Ids: "+remainingSequence);
	   		if(remainingSequence.size()>0)
	   		{
	   			finalMap.addAll(reconciliationResultService.getColAlignInfoForAcc(remainingSequence));
	   		}
	   		if(!status.toLowerCase().contains("un accounted"))
	   		{
				HashMap accRuleMp = new HashMap();
				accRuleMp.put("field", "Acct Rule");
				accRuleMp.put("header", "Acct Rule");
				accRuleMp.put("columnName", "rule_code");
				accRuleMp.put("align", "left");
				accRuleMp.put("width", "150px");
				accRuleMp.put("colId", "");
				accRuleMp.put("dataType", "STRING");
				finalMap.add(accRuleMp);
	   		}
  		    	HashMap rowDescRefMp = new HashMap();
   	   		  	rowDescRefMp.put("field", "rowDescription");
   	   		  	rowDescRefMp.put("header", "Row Description");
   	   		  	rowDescRefMp.put("columnName", "rowDescription");
   	   		  	rowDescRefMp.put("align", "right");
   	   		  	rowDescRefMp.put("width", "150px");
   	   		  	rowDescRefMp.put("colId", "");
   	   		  	rowDescRefMp.put("dataType", "STRING");
	   	   		finalMap.add(rowDescRefMp);
	   	   		
	   	   		HashMap adjTypeRefMp = new HashMap();
	   	   		adjTypeRefMp.put("field", "adjustmentType");
	   	   		adjTypeRefMp.put("header", "Adjustment Type");
	   	   		adjTypeRefMp.put("columnName", "adjustmentType");
	   	   		adjTypeRefMp.put("align", "right");
	   	   		adjTypeRefMp.put("width", "150px");
	   	   		adjTypeRefMp.put("colId", "");
	   	   		adjTypeRefMp.put("dataType", "STRING");
	   	   		finalMap.add(adjTypeRefMp);
	   	}
	   	return finalMap;
   }
   
   /**
    * Author: Ravali
    * **/
   public List<String> getViewColumnHeadersMapInSequenceForAcctApproval(Long dataViewId, Long tenantId, Long groupId)
   {
	   log.info("In service for getting column headers in sequence order for viewId: "+ dataViewId+", groupId"+ groupId+", tenantId"+ tenantId);
	   List<String> colNames = new ArrayList<String>();


	   List<BigInteger> ruleIds = accountingLineTypesRepository.fetchRuleIdsByTenantIdAndRuleId(tenantId, groupId, dataViewId);
	   log.info("Rule Ids: "+ ruleIds);
	   if(ruleIds.size()>0 && !ruleIds.contains(null))
	   {
		   List<BigInteger> sViewColumnIds = acctRuleConditionsRepository.fetchRuleIdsByTenantIdAndRuleId(ruleIds);
		   log.info("Source View Column Ids: "+sViewColumnIds);
		   if(sViewColumnIds.size()>0)
		   {
			   for(BigInteger id : sViewColumnIds)
			   {
				   DataViewsColumns dvc = dataViewsColumnsRepository.findOne(id.longValue());
				   if(dvc != null)
				   {
					   FileTemplateLines ftl = fileTemplateLinesRepository.findOne(Long.parseLong(dvc.getRefDvColumn().toString()));
					   if(ftl != null)	// Need to handle one more condition
					   {
						   colNames.add(ftl.getColumnAlias());
					   }
				   }
			   }
		   }
	   }
	   return colNames;
   }
   
   /**
    * Author: Ravali
    * **/
   public List<HashMap> getAcctApprovalsColsAlignInfo(Long viewId, Long groupId, Long tenantId)
   {
	   log.info("In service for getting accounting data view columns alignment info for the view: "+ viewId+", group id: "+groupId+", tenant id: "+ tenantId);
	   	List<BigInteger> remainingSequence = new ArrayList<BigInteger>();
		List<HashMap> finalMap = new ArrayList<HashMap>();
		
   		HashMap idMp = new HashMap();
   		idMp.put("field", "Id");
   		idMp.put("header", "Id");
   		idMp.put("align", "left");
   		idMp.put("width", "150px");
   		finalMap.add(idMp);
   		
	   	List<BigInteger> ruleIds = accountingLineTypesRepository.fetchRuleIdsByTenantIdAndRuleId(tenantId, groupId, viewId);
	   	log.info("Rule Ids: "+ ruleIds);
	   	if(ruleIds.size()>0 && !ruleIds.contains(null))
	   	{
	   		List<BigInteger> sViewColumnIds = acctRuleConditionsRepository.fetchRuleIdsByTenantIdAndRuleId(ruleIds);
	   		log.info("Source View Column Ids: "+sViewColumnIds);
	   		// sequence (rules based source view columns)
	   		if(sViewColumnIds.size()>0)
	   		{
	   			finalMap.addAll(reconciliationResultService.getColAlignInfoApp(sViewColumnIds));
	   		}
	   	}
	   	return finalMap;
   }

   public HashMap getDataViewColumnNameByColumnId(Long columnId)
   {
	   HashMap finalMap = new HashMap();
	   String colQualifier = "";
	   DataViewsColumns dvc = dataViewsColumnsRepository.findOne(columnId);
	   if(dvc != null)
	   {
			String dataType = dvc.getColDataType();
			if("File Template".equalsIgnoreCase(dvc.getRefDvType()))
			{
				FileTemplateLines ftl = fileTemplateLinesRepository.findOne(Long.parseLong(dvc.getRefDvColumn().toString()));
				if(ftl != null)
				{
					colQualifier = colQualifier + ftl.getColumnAlias();
				}
			}
			else if("Data View".equalsIgnoreCase(dvc.getRefDvType()) || dvc.getRefDvType() == null)
			{
				colQualifier = colQualifier + dvc.getColumnName();
			}
			finalMap.put("columnName", colQualifier);
			finalMap.put("dataType", dvc.getColDataType());
	   }
	   return finalMap;
   } 
	 	 
	 public void manualUnAccForTotal(List<BigInteger> orginalRowIds, Long tenantId, Long viewId, Long groupId, Long userId)
	 {
		 log.info("In Service for deleting records from accounted summary and accounting data manualUnAccForTotal...");
		 if(orginalRowIds.size()>0)
		 {
			 List<BigInteger> ruleIds = accountedSummaryRepository.fetchDistinctRuleIdsByRowIds(orginalRowIds, groupId, viewId);
			 if(ruleIds.size()>0)
			 {
				 for(BigInteger ruleId : ruleIds)
				 {
					 List<AccountedSummary> accSummary = accountedSummaryRepository.fetchIdsByTotalIdsRuleIdAndGroupId(orginalRowIds, groupId, viewId, ruleId.longValue());
					 if(accSummary.size()>0)
					 {
						 accountedSummaryRepository.delete(accSummary);
						 log.info("Deleted records from summary table: "+ accSummary.size());
					 }
					 List<AccountingData> accIds = accountingDataRepository.fetchRecordsByRuleId(orginalRowIds, tenantId, viewId, groupId, ruleId.longValue());
					 if(accIds.size()>0)
					 {
						 accountingDataRepository.delete(accIds);
						 log.info("Deleted records from accounting data table: "+ accIds.size());
					 }
					// postAppModuleSummaryTableForAcc(groupId, viewId, ruleId.longValue(), userId, Long.valueOf(accSummary.size()), "ACCOUNTING", "ACCOUNTED");
				 }
			 }
		 }
	 }

	 /**
	  * Author: Shiva
	  * Purpose: Posting accounting counts in t_app_module_summary table.
	  * **/
	  public void postAppModuleSummaryTableForAcc(Long groupId, Long viewId, Long ruleId, Long userId, Long typeCount, String type, String status)
	  {
		  log.info("Posting "+type+" count in t_app_module_summary table for the group id: "+groupId+", view id: "+ viewId+", rule id: "+ ruleId+", status: "+ status);
		  AppModuleSummary amsSource = appModuleSummaryRepository.findByModuleAndRuleGroupIdAndRuleIdAndTypeAndViewId(type, groupId, ruleId, status, viewId);
		  if(amsSource != null)
		  {
			  log.info("Updating the app_module_summary with count: " + (amsSource.getTypeCount()-typeCount));
			  amsSource.setLastUpdatedBy(userId);
			  amsSource.setLastUpdatedDate(ZonedDateTime.now());
			  amsSource.setTypeCount(amsSource.getTypeCount()-typeCount);
			  AppModuleSummary amsSrcUpdate = appModuleSummaryRepository.save(amsSource);
		  }
		  else
		  {
			  log.info("Creating the app_module_summary record with count: "+typeCount);
			  AppModuleSummary amsCreate = new AppModuleSummary();
			  amsCreate.setCreatedBy(userId);
			  amsCreate.setCreatedDate(ZonedDateTime.now());
			  amsCreate.setLastUpdatedBy(userId);
			  amsCreate.setLastUpdatedDate(ZonedDateTime.now());
			  amsCreate.setModule(type);
			  amsCreate.setRuleGroupId(groupId);
			  amsCreate.setRuleId(ruleId);
			  amsCreate.setType(status);
			  amsCreate.setTypeCount(typeCount);
			  amsCreate.setViewId(viewId);
			  AppModuleSummary amsSrcCreate = appModuleSummaryRepository.save(amsCreate);
		  }
	  }
	  

		/**
		 * Author: Shiva
		 * Purpose: Posting Accounting data counts in t_app_module_summary table
		 * @throws SQLException
		 * **/
		public void postAccountingCountsInAppModuleSummary(Long ruleGroupId, String jobReference, HashMap ruleViewMap, Long userId) throws SQLException
		{
			System.out.println("Service for posting accounting counts in t_app_module_summary table for the rule group id: "+ ruleGroupId+", job reference: "+ jobReference+", rule-view map: "+ruleViewMap);
			Iterator it = ruleViewMap.entrySet().iterator();
			
			Connection conn = null;
		 	
			try{
				DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
				conn = ds.getConnection();

				while (it.hasNext() && !conn.isClosed())
				{
					Statement stmt = conn.createStatement();
					Statement stmt1 = conn.createStatement();
					Statement stmt2 = conn.createStatement();
					Statement stmt3 = conn.createStatement();
					Statement accUpdateStmt = null;
					PreparedStatement accCreationStmt = null;
					Statement inpUpdateStmt = null;
					PreparedStatement inpCreationStmt = null;
			        Map.Entry pair = (Map.Entry)it.next();
			        Long ruleId = Long.parseLong(pair.getKey().toString());
			        Long viewId = Long.parseLong(pair.getValue().toString());
			        ResultSet accountedRS = stmt.executeQuery("SELECT count(*) FROM t_accounted_summary where rule_id = "+ruleId+" and view_id = "+viewId+" and rule_group_id = "+ruleGroupId+" and job_reference = '"+jobReference+"' and status = 'ACCOUNTED'");
			        ResultSet inprocessRS = stmt1.executeQuery("SELECT count(*) FROM t_accounted_summary where rule_id = "+ruleId+" and view_id = "+viewId+" and rule_group_id = "+ruleGroupId+" and job_reference = '"+jobReference+"' and status = 'INPROCESS'");
			        double accountedCount = 0.0;
			        double inprocessCount = 0.0;
			        
			        while(accountedRS.next()){
				 		accountedCount = accountedCount + Double.parseDouble(accountedRS.getString(1));
					}
			        while(inprocessRS.next()){
			        	inprocessCount = inprocessCount + Double.parseDouble(inprocessRS.getString(1));
					}
			        System.out.println(">> Rule Id: " + ruleId+", View Id: " + viewId + ", accounted count: "+accountedCount+", inprocess count: " + inprocessCount);
			        
			        String accountedQuery = "SELECT * FROM t_app_module_summary WHERE rule_group_id = "+ruleGroupId+" and rule_id = "+ruleId+" and view_id = "+viewId+" and type = 'ACCOUNTED' and module = 'ACCOUNTING'";
			        String inprocessQuery = "SELECT * FROM t_app_module_summary WHERE rule_group_id = "+ruleGroupId+" and rule_id = "+ruleId+" and view_id = "+viewId+" and type = 'INPROCESS' and module = 'ACCOUNTING'";
			        
			        ResultSet accResult = stmt2.executeQuery(accountedQuery);
			        ResultSet inpResult = stmt3.executeQuery(inprocessQuery);
			        
					List<HashMap> accRecords = new ArrayList<HashMap>();
					List<HashMap> inpRecords = new ArrayList<HashMap>();
			        
					ResultSetMetaData rsmdInfo = accResult.getMetaData();
		 	    	int colCount = rsmdInfo.getColumnCount();
		 	    	
				 	while(accResult.next()){	// Fetching Accounted Records from t_app_module_summary
		 	    		HashMap hm = new HashMap();
		 	    		for(int i=1; i<=colCount; i++)
		 	    		{
		 	    			hm.put(rsmdInfo.getColumnName(i), accResult.getString(i));
		 	    		}
		 	    		accRecords.add(hm);
					}
				 	
				 	while(inpResult.next()){	// Fetching Inprocess Records from t_app_module_summary
		 	    		HashMap hm = new HashMap();
		 	    		for(int i=1; i<=colCount; i++)
		 	    		{
		 	    			hm.put(rsmdInfo.getColumnName(i), inpResult.getString(i));
		 	    		}
		 	    		inpRecords.add(hm);
					}
				 	// Updating t_app_module_Summary for ACCOUNTED status
				 	if(accRecords.size()>1)
				 	{
				 		System.out.println("Duplicate source records exist...");
				 	}
				 	else if(accRecords.size() == 1)
				 	{	// Updating Record for ACCOUNTED
				 		double accCount = Double.parseDouble(accRecords.get(0).get("type_count").toString())+accountedCount;
				 		accUpdateStmt = conn.createStatement();
				 		accUpdateStmt.executeUpdate("UPDATE TABLE t_app_module_summary SET type_count = "+accCount+" and last_updated_date = "+new java.sql.Date(System.currentTimeMillis())+" where id = "+Long.parseLong(accRecords.get(0).get("id").toString()));
				 	}
				 	else
				 	{	// Creating Record for ACCOUNTED
				 		String query = "INSERT INTO t_app_module_summary (module, rule_group_id, rule_id, type, type_count, view_id, created_by, last_updated_by, created_date, last_updated_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				 		accCreationStmt =  conn.prepareStatement(query);
				 		accCreationStmt.setString(1, "ACCOUNTING");
				 		accCreationStmt.setLong(2, ruleGroupId);
				 		accCreationStmt.setLong(3, ruleId);
				 		accCreationStmt.setString(4, "ACCOUNTED");
				 		accCreationStmt.setDouble(5, accountedCount);
				 		accCreationStmt.setLong(6, viewId);
				 		accCreationStmt.setLong(7, userId);
				 		accCreationStmt.setLong(8, userId);
				 		accCreationStmt.setDate(9, new java.sql.Date(System.currentTimeMillis()));
				 		accCreationStmt.setDate(10, new java.sql.Date(System.currentTimeMillis()));
				 		accCreationStmt.execute();
				 	}
				 	// Updating t_app_module_Summary for INPROCESS status
				 	if(inpRecords.size()>1)
				 	{
				 		System.out.println("Duplicate source records exist...");
				 	}
				 	else if(inpRecords.size() == 1)
				 	{	// Updating Record for INPROCESS
				 		double inpCount = Double.parseDouble(inpRecords.get(0).get("type_count").toString())+inprocessCount;
				 		inpUpdateStmt = conn.createStatement();
				 		inpUpdateStmt.executeUpdate("UPDATE TABLE t_app_module_summary SET type_count = "+inpCount+" and last_updated_date = "+new java.sql.Date(System.currentTimeMillis())+" where id = "+Long.parseLong(inpRecords.get(0).get("id").toString()));
				 	}
				 	else
				 	{	// Creating Record for INPROCESS
				 		String query = "INSERT INTO t_app_module_summary (module, rule_group_id, rule_id, type, type_count, view_id, created_by, last_updated_by, created_date, last_updated_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				 		inpCreationStmt = conn.prepareStatement(query);
				 		inpCreationStmt.setString(1, "ACCOUNTING");
				 		inpCreationStmt.setLong(2, ruleGroupId);
				 		inpCreationStmt.setLong(3, ruleId);
				 		inpCreationStmt.setString(4, "INPROCESS");
				 		inpCreationStmt.setDouble(5, inprocessCount);
				 		inpCreationStmt.setLong(6, viewId);
				 		inpCreationStmt.setLong(7, userId);
				 		inpCreationStmt.setLong(8, userId);
				 		inpCreationStmt.setDate(9, new java.sql.Date(System.currentTimeMillis()));
				 		inpCreationStmt.setDate(10, new java.sql.Date(System.currentTimeMillis()));
				 		inpCreationStmt.execute();
				 	}
					if(accResult != null)
						accResult.close();
					if(inpResult != null)
						inpResult.close();
			        if(accountedRS != null)
			        	accountedRS.close();
			        if(inprocessRS != null)
			        	inprocessRS.close();
					if(stmt != null)
						stmt.close();
					if(stmt1 != null)
						stmt1.close();
					if(stmt2 != null)
						stmt2.close();
					if(stmt3 != null)
						stmt3.close();
			        if(accUpdateStmt != null)
			        	accUpdateStmt.close();
			        if(accCreationStmt != null)
			        	accCreationStmt.close();
			        if(inpUpdateStmt != null)
			        	inpUpdateStmt.close();
			        if(inpCreationStmt != null)
			        	inpCreationStmt.close();
			        it.remove(); // avoids a ConcurrentModificationException
			    }
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
			    if(conn!=null)
			    	conn.close();
			}
		}
		
		public void createReveseEntriesold(List<Long> originalRowIds,Long tenantId, Long userId, String jobRef) 
		{
			List<AccountedSummary> acctSummaryDetails = accountedSummaryRepository.findByRowIdInAndCurrentRecordFlag(originalRowIds,true);
			for(AccountedSummary acctSumm:acctSummaryDetails)
			{
				AccountedSummary accSummRevEntry = new AccountedSummary();
				accSummRevEntry.setRowId(acctSumm.getRowId());
				accSummRevEntry.setRuleId(acctSumm.getRuleId());
				accSummRevEntry.setDebitCount(acctSumm.getCreditCount());
				accSummRevEntry.setCreditCount(acctSumm.getDebitCount());
				accSummRevEntry.setStatus(acctSumm.getStatus());
				accSummRevEntry.setCreatedBy(userId);
				accSummRevEntry.setCreatedDate(ZonedDateTime.now());
				accSummRevEntry.setViewId(acctSumm.getViewId());
				accSummRevEntry.setRuleGroupId(acctSumm.getRuleGroupId());
				accSummRevEntry.setJobReference(jobRef);
				accSummRevEntry.setCurrentRecordFlag(true);
				accSummRevEntry = accountedSummaryRepository.save(accSummRevEntry); // Created reverse entries in t_accounted_summary table
				
				List<AccountingData> accData = accountingDataRepository.findByTenantIdAndAccountedSummaryId(tenantId,acctSumm.getId());
				List<AccountingData> accDataReverseEntries = new ArrayList<AccountingData>();
				List<AccountingData> accDataReversed = new ArrayList<AccountingData>();
				for(AccountingData accDataForRev:accData)
				{
					AccountingData revEntry = new AccountingData();
					revEntry = accDataForRev;
					if(accDataForRev.getLineType()!=null)
					{
						if(accDataForRev.getLineType().equalsIgnoreCase("CREDIT"))
						{
							revEntry.setLineType("DEBIT");
						}
						else if(accDataForRev.getLineType().equalsIgnoreCase("DEBIT"))
						{
							revEntry.setLineType("CREDIT");
						}
					}
					revEntry.setCreatedBy(userId);
					revEntry.setCreatedDate(ZonedDateTime.now());
					revEntry.setReverseRefId(accDataForRev.getId());
					revEntry.setJobReference(jobRef);
					revEntry.setAccountedSummaryId(accSummRevEntry.getId());
					accDataReverseEntries.add(revEntry);
					accDataForRev.setStatus("REVERSED");
					accDataForRev.setLastUpdatedBy(userId);
					accDataForRev.setLastUpdatedDate(ZonedDateTime.now());
					accDataForRev.setAccountedSummaryId(accSummRevEntry.getId());
					accDataReversed.add(accDataForRev);
				}
				accountingDataRepository.save(accDataReverseEntries);
				accountingDataRepository.save(accDataReversed);
				List<AccountedSummary> acctSummaryAll = accountedSummaryRepository.findByRowIdInAndJobReferenceNotIn(originalRowIds,jobRef);
				for(AccountedSummary accSummary: acctSummaryAll)
				{
					accSummary.setCurrentRecordFlag(!accSummary.getCurrentRecordFlag());
					accSummary.setLastUpdatedBy(userId);
					accSummary.setLastUpdatedDate(ZonedDateTime.now());
					accountedSummaryRepository.save(accSummary);
				}
			}
		}
		
		public void createReveseEntries(List<Long> originalRowIds,Long tenantId, Long userId, String jobRef) 
		{
			List<AccountedSummary> acctSummaryDetails = accountedSummaryRepository.findByRowIdInAndCurrentRecordFlag(originalRowIds,true);
			for(AccountedSummary acctSumm:acctSummaryDetails)
			{
				AccountedSummary accSummRevEntry = new AccountedSummary();
				accSummRevEntry.setRowId(acctSumm.getRowId());
				accSummRevEntry.setRuleId(acctSumm.getRuleId());
				accSummRevEntry.setDebitCount(acctSumm.getCreditCount());
				accSummRevEntry.setCreditCount(acctSumm.getDebitCount());
				accSummRevEntry.setStatus(acctSumm.getStatus());
				accSummRevEntry.setCreatedBy(userId);
				accSummRevEntry.setCreatedDate(ZonedDateTime.now());
				accSummRevEntry.setViewId(acctSumm.getViewId());
				accSummRevEntry.setRuleGroupId(acctSumm.getRuleGroupId());
				accSummRevEntry.setJobReference(jobRef);
				accSummRevEntry.setCurrentRecordFlag(true);
				accSummRevEntry = accountedSummaryRepository.save(accSummRevEntry);
				List<AccountingData> accData = accountingDataRepository.findByTenantIdAndAccountedSummaryId(tenantId,acctSumm.getId());
				List<AccountingData> accDataReverseEntries = new ArrayList<AccountingData>();
				List<AccountingData> accDataReversed = new ArrayList<AccountingData>();
				for(AccountingData accDataForRev:accData)
				{
					AccountingData revEntry = new AccountingData();
					revEntry.setTenantId(accDataForRev.getTenantId());
					revEntry.setOriginalRowId(accDataForRev.getOriginalRowId());
					if(accDataForRev.getAccountingRef1()!=null)
					{
						revEntry.setAccountingRef1(accDataForRev.getAccountingRef1());
					}
					if(accDataForRev.getAccountingRef2()!=null)
					{
						revEntry.setAccountingRef2(accDataForRev.getAccountingRef2());
					}
					if(accDataForRev.getAccountingRef3()!=null)
					{
						revEntry.setAccountingRef3(accDataForRev.getAccountingRef3());
					}
					if(accDataForRev.getAccountingRef4()!=null)
					{
						revEntry.setAccountingRef4(accDataForRev.getAccountingRef4());
					}
					if(accDataForRev.getAccountingRef5()!=null)
					{
						revEntry.setAccountingRef5(accDataForRev.getAccountingRef5());
					}
					if(accDataForRev.getAccountingRef6()!=null)
					{
						revEntry.setAccountingRef6(accDataForRev.getAccountingRef6());
					}
					if(accDataForRev.getAccountingRef7()!=null)
					{
						revEntry.setAccountingRef7(accDataForRev.getAccountingRef7());
					}
					if(accDataForRev.getAccountingRef8()!=null)
					{
						revEntry.setAccountingRef8(accDataForRev.getAccountingRef8());
					}
					if(accDataForRev.getAccountingRef9()!=null)
					{
						revEntry.setAccountingRef9(accDataForRev.getAccountingRef9());
					}
					if(accDataForRev.getAccountingRef10()!=null)
					{
						revEntry.setAccountingRef10(accDataForRev.getAccountingRef10());
					}
					if(accDataForRev.getLedgerRef()!=null)
					{
						revEntry.setLedgerRef(accDataForRev.getLedgerRef());
					}
					if(accDataForRev.getLineTypeId()!=null)
					{
						revEntry.setLineTypeId(accDataForRev.getLineTypeId());
					}
					if(accDataForRev.getAmountColId()!=null)
					{
						revEntry.setAmountColId(accDataForRev.getAmountColId());
					}
					revEntry.setCategoryRef(accDataForRev.getCategoryRef());
					revEntry.setSourceRef(accDataForRev.getSourceRef());
					revEntry.setCurrencyRef(accDataForRev.getCurrencyRef());
					if(accDataForRev.getLineType()!=null)
					{
						if(accDataForRev.getLineType().equalsIgnoreCase("CREDIT"))
						{
							revEntry.setLineType("DEBIT");
						}
						else if(accDataForRev.getLineType().equalsIgnoreCase("DEBIT"))
						{
							revEntry.setLineType("CREDIT");
						}
					}
					if(accDataForRev.getCoaRef()!=null)
					{
						revEntry.setCoaRef(accDataForRev.getCoaRef());
					}
					revEntry.setCreatedBy(userId);
					revEntry.setCreatedDate(ZonedDateTime.now());
					if(accDataForRev.getStatus()!=null)
					{
						revEntry.setStatus(accDataForRev.getStatus());
					}
					if(accDataForRev.getAcctGroupId()!=null)
					{
						revEntry.setAcctGroupId(accDataForRev.getAcctGroupId());
					}
					if(accDataForRev.getAcctRuleId()!=null)
					{
						revEntry.setAcctRuleId(accDataForRev.getAcctRuleId());
					}
					if(accDataForRev.getOriginalViewId()!=null)
					{
						revEntry.setOriginalViewId(accDataForRev.getOriginalViewId());
					}
					if(accDataForRev.getLineTypeDetail()!=null)
					{
						revEntry.setLineTypeDetail(accDataForRev.getLineTypeDetail());
					}
					if(accDataForRev.getLedgerRefType()!=null)
					{
						revEntry.setLedgerRefType(accDataForRev.getLedgerRefType());
					}
					if(accDataForRev.getAmount() != null)
					{
						revEntry.setAmount(accDataForRev.getAmount());
					}
					if(accDataForRev.getFxRate() != null)
					{
						revEntry.setFxRate(accDataForRev.getFxRate());
					}
					if(accDataForRev.getAccountedAmount() != null)
					{
						revEntry.setAccountedAmount(accDataForRev.getAccountedAmount());
					}
					if(accDataForRev.getLedgerCurrency() != null)
					{
						revEntry.setLedgerCurrency(accDataForRev.getLedgerCurrency());
					}					
					revEntry.setReverseRefId(accDataForRev.getId());
					revEntry.setJobReference(jobRef);
					revEntry.setAccountedSummaryId(accSummRevEntry.getId());
					accDataReverseEntries.add(revEntry);
					accDataForRev.setStatus("REVERSED");
					accDataForRev.setLastUpdatedBy(userId);
					accDataForRev.setLastUpdatedDate(ZonedDateTime.now());
					accDataForRev.setAccountedSummaryId(accSummRevEntry.getId());
					accDataReversed.add(accDataForRev);
				}
				accountingDataRepository.save(accDataReverseEntries);
				accountingDataRepository.save(accDataReversed);
			}
			List<AccountedSummary> acctSummaryAll = accountedSummaryRepository.findByRowIdInAndJobReferenceNotIn(originalRowIds,jobRef);
			for(AccountedSummary accSummary: acctSummaryAll)
			{
				accSummary.setCurrentRecordFlag(!accSummary.getCurrentRecordFlag());
				accSummary.setLastUpdatedBy(userId);
				accSummary.setLastUpdatedDate(ZonedDateTime.now());
				accountedSummaryRepository.save(accSummary);
			}
		}
		
		// New form services
		public List<String> getActityOrNonActityBased(Long tenantId, Long groupId, Long viewId) throws SQLException
		{
			List<String> activityBasedYorN = new ArrayList<String>();
			Connection conn = null;
			Statement stmt = null;
			ResultSet result = null; 
			try{
				DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
				conn = ds.getConnection();
				stmt = conn.createStatement();
				String query = "select distinct source_data_view_id,data_view_name, (case when reconciliation_view_id is null and accounting_status is null"
						+ " and reconciliation_status is null"
						+ " then 'N'"
						+ " else"
						+ " 'Y' "
						+ " END) activity_based from t_rule_group rg, t_rule_group_details rgd, t_rules rl, t_data_views dvt"
						+ " where rg.tenant_id = "+tenantId+" and rg.id = "+groupId+""
						+ " and rg.id = rgd.rule_group_id"
						+ " and rgd.rule_id = rl.id"
						+ " and rl.source_data_view_id = dvt.id";
				
				log.info("Query to fetch activity and non activity based data views: "+ query);
				
	        	result=stmt.executeQuery(query);
	        	while(result.next()){
	        		Long dataViewId = Long.parseLong(result.getString(1));
	        		String viewName = result.getString(2);
	        		String activityBased = result.getString(3);
	        		if(dataViewId.equals(viewId))
	        		{
	        			activityBasedYorN.add(activityBased);
	        		}
	        	}  
			}
			catch(Exception e)
			{
				log.info("Exception while getting databse properties. "+e);
			}
			finally{
				if(result != null)
					result.close();
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
			}
			return activityBasedYorN;
		}
		
		public List<HashMap> getNonActivitySummary(Long viewId, Long groupId, String amountQualifier, String dateQualifier, String rangeFrom, String rangeTo) throws SQLException
		{
			log.info("Fetching Accounting summary info for the view id: "+ viewId+", group id: "+ groupId);
			List<HashMap> finalList = new ArrayList<HashMap>();
			DataViews dv = dataViewsRepository.findById(viewId.longValue());
			NumberFormat numFormat = NumberFormat.getInstance();
/*			Properties props = propertiesUtilService.getPropertiesFromClasspath("File.properties");
			String currencyFormat = props.getProperty("currencyFormat");*/
			if(dv != null)
			{
				Connection conn = null;
				Statement stmt = null;
				ResultSet result = null; 
				try{
					
					DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
					conn = ds.getConnection();
					
					log.info("Connected to JDBC Successfully");
					stmt = conn.createStatement();
					
					String query = "select accounting_status,count(*) count,"
							+ " case when accounting_status = 'UN ACCOUNTED' "
							+ " then sum(IFNULL(`"+amountQualifier+"`, 0)) "
							+ " else sum(IFNULL(det.accounted_amount, 0)) END amount"
							+ " from ("
							+ "		select accounted_amount, dv.*,(case when acd.ACCOUNTING_STATUS is null"
							+ "		THEN"
							+ "			'UN ACCOUNTED'"
							+ "		ELSE"
							+ "			acd.ACCOUNTING_STATUS END) as accounting_status "
							+ "      FROM (SELECT * "
							+ "            FROM `"+dv.getDataViewName().toLowerCase()+"` "
							+ "            WHERE  Date(`"+dateQualifier+"`) BETWEEN '"+rangeFrom+"' AND '"+rangeTo+"') dv "
							+ "            LEFT JOIN (SELECT  su.status ACCOUNTING_STATUS, "
							+ "                           	  de.original_row_id, "
							+ "                               sum(IF(de.status = 'REVERSED', accounted_amount*-1,accounted_amount)) accounted_amount"
							+ "                          FROM   t_accounted_summary su, "
							+ "                                 t_accounting_data de "
							+ "                          WHERE  su.id = de.accounted_summary_id "
							+ "                                 AND su.rule_group_id = "+groupId+" "
							+ "                                 AND su.view_id = "+viewId+" "
							+ "                                 AND su.current_record_flag IS TRUE "
							+ "                                 AND line_type = 'DEBIT'"
							+ "                                 group by  su.status, de.original_row_id"
							+ "                            ) acd "
							+ "                      ON dv.scrids = acd.original_row_id) det "
							+ " GROUP  BY accounting_status";
					
					log.info("Query to fetch non activity based counts and amounts: "+query);
					
		        	result=stmt.executeQuery(query);
		        	List<String> statuses = new ArrayList<String>();	// Need to get statuses from look up codes
		        	statuses.add("INPROCESS");
		        	statuses.add("UN ACCOUNTED");
		        	statuses.add("ACCOUNTED");
		        	
		        	while(result.next()){
		        		HashMap statusMap = new HashMap();
		        		String status = result.getString("accounting_status");
		        		String count = result.getString("count");
		        		String amount = result.getString("amount");

/*		        		Double reverseAmount = Double.parseDouble(getAmountReverse(viewId, amountQualifier, groupId));
		        		Double originalAmount = Double.parseDouble(amount);
		        		Double finalAmount = originalAmount-reverseAmount;*/
		        		Double finalAmount = Double.parseDouble(amount);
			        	statusMap.put("amount", finalAmount);
			        	statusMap.put("amountValue", finalAmount);

		        		statusMap.put("status",  status.toLowerCase());
		        		statusMap.put("count", numFormat.format(Integer.parseInt(count)));
		        		finalList.add(statusMap);
		        		statuses.remove(status);
		        	}
		        	
		        	if(statuses.size()>0)
		        	{
		        		for(String status : statuses)
		        		{
		        			HashMap statusMap = new HashMap();
		        			statusMap.put("status", status.toLowerCase());
		        			statusMap.put("count", "0");
		        			statusMap.put("amount", "0.0");
		        			statusMap.put("amountValue", 0.0);
		        			statusMap.put("ledgerName",  "");
		        			finalList.add(statusMap);
		        		}
		        	}
		        	if(finalList.size() == statuses.size())
		        	{
		        		finalList.clear();
		        	}
				}
				catch(Exception e)
				{
					log.info("Exception while getting databse properties: "+e);
				}
				finally{
					if(result != null)
						result.close();
					if(stmt != null)
						stmt.close();
					if(conn != null)
						conn.close();
				}
			}
			log.info("Statuses Map: "+finalList);
			return finalList;
		}
		
		public String getAmountReverse(Long viewId, String amountQualifier, Long groupId)
		{
			Properties props = propertiesUtilService.getPropertiesFromClasspath("File.properties");
			String currencyFormat = props.getProperty("currencyFormat");
			DataViews dv = dataViewsRepository.findById(viewId.longValue());
			String reverseAmount = "0.0";
			if(dv != null)
			{
				Connection conn = null;
				Statement stmt = null;
				ResultSet result = null; 
				try{			  		
					DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
					conn = ds.getConnection();
					log.info("Connected to JDBC Successfully");
					stmt = conn.createStatement();
					String query = "select sum(dv.`"+amountQualifier+"`) from `"+dv.getDataViewName().toLowerCase()+"` dv, t_accounting_data ad "
							+ "	where ad.original_row_id = "
							+ " dv.scrIds and acct_group_id = "+groupId+" and original_view_id = "+viewId+" and status = 'REVERSED' and line_type='DEBIT'";
					result=stmt.executeQuery(query);while(result.next()){
		        		HashMap statusMap = new HashMap();
		        		String ledgerName = "";
		        		if(result.getString(1) != null)
		        		{
		        			reverseAmount = result.getString(1).toString(); 
		        		}
					}
				}
				catch(Exception e)
				{
					log.info("Exception while getting reverse entries amount: "+e);
				}
				finally{
					try{
						if(result != null)
							result.close();
						if(stmt != null)
							stmt.close();
						if(conn != null)
							conn.close();
						}
					catch(Exception e)
					{
						log.info("Exception while closing statements.."+e);
					}
				}
			}
			return reverseAmount;
		}
		
		public List<HashMap> getActivitySummary(Long viewId, Long groupId, String amountQualifier, String dateQualifier, String rangeFrom, String rangeTo, String srcOrTrgt) throws SQLException
		{
			log.info("Fetching Activity based Statuses Accounting summary info for the view id: "+ viewId+", group id: "+ groupId);
			List<HashMap> finalList = new ArrayList<HashMap>();
			DataViews dv = dataViewsRepository.findById(viewId.longValue());
			NumberFormat numFormat = NumberFormat.getInstance();
/*			Properties props = propertiesUtilService.getPropertiesFromClasspath("File.properties");
			String currencyFormat = props.getProperty("currencyFormat");*/
			if(dv != null)
			{
				Connection conn = null;
				Statement stmt = null;
				ResultSet result = null; 
				try{
			  		
					String srcOrTrgtQuery = "";
					if("source".equalsIgnoreCase(srcOrTrgt))
					{
						srcOrTrgtQuery = " LEFT JOIN (select recon_reference, original_row_id from t_reconciliation_result where original_view_id = "+viewId+" and recon_status = 'RECONCILED'"
								+ " and current_record_flag is true) recon"
								+ " ON dv.scrIds = recon.original_row_id ";
					}
					else if("target".equalsIgnoreCase(srcOrTrgt))
					{
						srcOrTrgtQuery = " LEFT JOIN (select recon_reference, target_row_id from t_reconciliation_result where target_view_id = "+viewId+" and recon_status = 'RECONCILED'"
								+ " and current_record_flag is true) recon"
								+ " ON dv.scrIds = recon.target_row_id ";
					}
					
					DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
					conn = ds.getConnection();
					
					log.info("Connected to JDBC Successfully");
					stmt = conn.createStatement();
					String query  = "SELECT "
							+ " accounting_recon_status, "
							+ " Count(*) count, "
							+ " CASE "
							+ " WHEN accounting_recon_status = 'UN ACCOUNTED, NOT RECONCILED' THEN "
							+ " Sum(Ifnull(`"+amountQualifier+"`, 0)) "
							+ " WHEN accounting_recon_status = 'UN ACCOUNTED, RECONCILED' THEN "
							+ " Sum(Ifnull(`"+amountQualifier+"`, 0)) "
							+ " WHEN accounting_recon_status = 'ACCOUNTED, NOT RECONCILED' THEN Sum( "
							+ " Ifnull(det.accounted_amount, 0)) "
							+ " WHEN accounting_recon_status = 'ACCOUNTED, RECONCILED' THEN Sum( "
							+ " Ifnull(det.accounted_amount, 0)) "
							+ " end amount "
							+ " FROM (SELECT accounted_amount, "
							+ "      dv.*, "
							+ "      (CASE "
							+ "      WHEN acd.accounting_status IS NULL AND recon.recon_reference IS NULL THEN "
							+ "     	'UN ACCOUNTED, NOT RECONCILED' "
							+ "      WHEN acd.accounting_status IS NULL AND recon.recon_reference IS NOT NULL THEN "
							+ "         'UN ACCOUNTED, RECONCILED' "
							+ "		 WHEN acd.accounting_status IS NOT NULL AND recon.recon_reference IS NULL THEN "
							+ "         'ACCOUNTED, NOT RECONCILED' "
							+ "      WHEN acd.accounting_status IS NOT NULL AND recon.recon_reference IS NOT NULL THEN "
							+ "          'ACCOUNTED, RECONCILED' "
							+ "      end)  AS accounting_recon_status "
							+ "      FROM (SELECT * "
							+ "            FROM `"+dv.getDataViewName().toLowerCase()+"` "
							+ "            WHERE  Date(`"+dateQualifier+"`) BETWEEN '"+rangeFrom+"' AND '"+rangeTo+"') dv "
							+ "            LEFT JOIN (SELECT  su.status ACCOUNTING_STATUS, "
							+ "                           	  de.original_row_id, "
							+ "                               sum(IF(de.status = 'REVERSED', accounted_amount*-1,accounted_amount)) accounted_amount"
							+ "                          FROM   t_accounted_summary su, "
							+ "                                 t_accounting_data de "
							+ "                          WHERE  su.id = de.accounted_summary_id "
							+ "                                 AND su.rule_group_id = "+groupId+" "
							+ "                                 AND su.view_id = "+viewId+" "
							+ "                                 AND su.current_record_flag IS TRUE "
							+ "                                 AND line_type = 'DEBIT'"
							+ "                                 group by  su.status, de.original_row_id"
							+ "                            ) acd "
							+ "                      ON dv.scrids = acd.original_row_id "
							+ "				 "+ srcOrTrgtQuery +" ) det "
							+ " GROUP  BY accounting_recon_status";
					
					
					
					log.info("Query: "+ query);
		        	result=stmt.executeQuery(query);
		        	List<String> statuses = new ArrayList<String>();	// Need to get statuses from look up codes
		        	statuses.add("UN ACCOUNTED, NOT RECONCILED");
		        	statuses.add("UN ACCOUNTED, RECONCILED");
		        	statuses.add("ACCOUNTED, NOT RECONCILED");
		        	statuses.add("ACCOUNTED, RECONCILED");
		        	
		        	while(result.next()){
		        		HashMap statusMap = new HashMap();
		        		String status = result.getString("accounting_recon_status");
		        		String count = result.getString("count");
		        		String amount = result.getString("amount");

		        		Double originalAmount = Double.parseDouble(amount);
		        		statusMap.put("amount", originalAmount);
			        	statusMap.put("amountValue", originalAmount);

		        		statusMap.put("status",  status.toLowerCase());
		        		statusMap.put("count", numFormat.format(Integer.parseInt(count)));
		        		finalList.add(statusMap);
		        		statuses.remove(status);
		        	}
		        	if(statuses.size()>0)
		        	{
		        		for(String status : statuses)
		        		{
		        			HashMap statusMap = new HashMap();
		        			statusMap.put("status", status.toLowerCase());
		        			statusMap.put("count", "0");
		        			statusMap.put("amount", "0.0");
		        			statusMap.put("amountValue", 0.0);
		        			finalList.add(statusMap);
		        		}
		        	}
		        	if(finalList.size() == statuses.size())
		        	{
		        		finalList.clear();
		        	}
				}
				catch(Exception e)
				{
					log.info("Exception while getting databse properties: "+e);
				}
				finally{
					if(result != null)
						result.close();
					if(stmt != null)
						stmt.close();
					if(conn != null)
						conn.close();
				}
			}
			log.info("Statuses Map: "+finalList);
			return finalList;
		}

		
		public List<HashMap> getGroupByNonActivitySummaryInfo(String status,String groupBy, String viewName, String dateQualifier, String rangeFrom, String rangeTo, Long tenantId, Long groupId, Long viewId, String whereString, String amountQualifier, String orderBy, String dataType) throws SQLException
		{
			List<HashMap> finalList = new ArrayList<HashMap>();
			NumberFormat numFormat = NumberFormat.getInstance();
			
			Properties props = propertiesUtilService.getPropertiesFromClasspath("File.properties");
			String currencyFormat = props.getProperty("currencyFormat");
			
			Connection conn = null;
			Statement stmt = null;
			ResultSet result = null; 
			try{
				DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
				conn = ds.getConnection();
				stmt = conn.createStatement();

						String groupByString = "";
						if("DATE".equalsIgnoreCase(dataType))
						{
							groupByString = groupByString + "Date(det.`"+groupBy+"`)";
						}
						else
						{
							groupByString = groupByString + "det.`"+groupBy+"`";
						}
				
				String query = "select "+groupByString+", count(*), SUM(`"+amountQualifier+"`) from "
						+ " (select dv.*,  "
						+ " 	(case when acd.ACCOUNTING_STATUS is null  "
						+ "     THEN       "
						+ " 		'UN ACCOUNTED'  "
						+ " 	ELSE      "
						+ "     acd.ACCOUNTING_STATUS  END) as accounting_status,  "
						+ "		appStatusLum.meaning final_status, final_action_date, "
						+ "		rl2.rule_code  approval_rule_code, "
						+ "		source_ref,	"
						+ "     coa_ref,  "
						+ "     ledger_ref,  "
						+ "     category_ref, "
						+ "		currency_ref,	"
						+ "     led.name ledger_name,  "
						+ "     coa.name coa_name,  "
						+ "		srclum.meaning source_meaning,   "
				        + "		catlum.meaning category_meaning,  "
						+ "		case when acct_rule_id = 0  " 
						+ "			then 'Manual' "
						+ "		else   "
						+ "			rl.rule_code end rule_code,  "  
						+ "     rl.id rule_id,  "
						/*+ "     acd.job_reference  "*/
						+ " 	case when SUBSTRING(job_reference, 1, 6) = 'MANUAL' "
						+ " 		THEN "
						+ " 			'Manual' "
						+ " 		ELSE "
						+ "				acd.job_reference  end job_reference, amount_col_id "
						+ "     from (select * from `"+viewName+"` where Date(`"+dateQualifier+"`) between '"+rangeFrom+"' and '"+rangeTo+"') dv "
						+ "     LEFT JOIN ("
						+ "     SELECT distinct su.STATUS ACCOUNTING_STATUS, de.ledger_ref,de.coa_ref,de.acct_rule_id,de.original_row_id,"
						+ "		de.approval_rule_id, DATE(de.final_action_date) final_action_date,de.final_status, "
						+ "     de.source_ref,de.category_ref,de.job_reference, de.tenant_id, de.currency_ref, de.amount_col_id FROM t_accounted_summary su, "
						+ "     t_accounting_data de WHERE su.id = de.accounted_summary_id AND su.rule_group_id = "+groupId+" and su.view_id = "+viewId+" and su.current_record_flag is true"
						+ " 	and su.job_reference = de.job_reference) acd  "
						+ "     ON dv.scrIds = acd.original_row_id "
						+ " 	LEFT JOIN look_up_code srclum  ON acd.source_ref = srclum.look_up_code  and acd.tenant_id = srclum.tenant_id and srclum.look_up_type = 'SOURCE' "
						+ "		LEFT JOIN look_up_code catlum  ON acd.category_ref = catlum.look_up_code  and acd.tenant_id = catlum.tenant_id and catlum.look_up_type = 'CATEGORY' "
						+ "		LEFT JOIN look_up_code appStatusLum ON acd.final_status = appStatusLum.look_up_code and acd.tenant_id = appStatusLum.tenant_id and appStatusLum.look_up_type = 'APPROVAL_STATUS'"
						+ "     LEFT JOIN t_ledger_definition led ON acd.ledger_ref = led.id "
						+ "     LEFT JOIN t_chart_of_account coa ON acd.coa_ref = coa.id "
						+ "     LEFT JOIN t_rules rl ON acd.acct_rule_id = rl.id "
						+ " 	LEFT JOIN t_rules rl2 ON acd.approval_rule_id = rl2.id) det "
						+ "     where accounting_status = '"+status+"' "+whereString+" group by "+groupByString+" order by "+groupByString+" "+orderBy;
				
				log.info("Grouping Query: "+query);
				result=stmt.executeQuery(query);
				if("final_status".equalsIgnoreCase(groupBy))
				{
		        	while(result.next())
		        	{
		        		HashMap summaryMap = new HashMap();
		        		String count = result.getString(2).toString();
		        		String amount = result.getString(3).toString();
			        	String name = "";
			        	if(result.getString(1) == null)
			        	{
			        		name = "Not Required";
			        	}
			        	else if("InProcess".equalsIgnoreCase(result.getString(1)))
			        	{
			        		name = "Awaiting for Approvals";	// 'InProcess'		        		
			        	}
			        	else
			        	{
			        		name = result.getString(1);
			        	}
		        		summaryMap.put("name", name);
			        	summaryMap.put("count", numFormat.format(Integer.parseInt(count)));
			        	summaryMap.put("amount", amount);
			        	summaryMap.put("amountValue", amount);
			        	summaryMap.put("filterColumn", groupBy);
			        	summaryMap.put("dataType", dataType);
			        	finalList.add(summaryMap);	        			
		        	}
				}
				else
				{
		        	while(result.next())
		        	{
		        		HashMap summaryMap = new HashMap();
		        		String name = result.getString(1);
		        		String count = result.getString(2).toString();
		        		String amount = result.getString(3).toString();
		        		if(name != null)
		        		{
			        		summaryMap.put("name", name);
			        		summaryMap.put("count", numFormat.format(Integer.parseInt(count)));
			        		summaryMap.put("amount", amount);
			        		summaryMap.put("amountValue", amount);
			        		summaryMap.put("filterColumn", groupBy);
			        		summaryMap.put("dataType", dataType);
			        		finalList.add(summaryMap);	        			
		        		}
		        	}					
				}
			}
			catch(Exception e)
			{
				log.info("Exception while getting databse properties: "+ e);
			}
			finally{
				if(result != null)
					result.close();
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
			}			
			log.info("Group by: "+groupBy+" Size: "+finalList.size());
			return finalList;
		}
		
		public List<HashMap> getGroupByActivitySummaryInfo(String status,String groupBy, String viewName, String dateQualifier, String rangeFrom, String rangeTo, Long tenantId, Long groupId, Long viewId, String whereString, String amountQualifier, String orderBy, String dataType, String srcOrTrgt) throws SQLException
		{
			List<HashMap> finalList = new ArrayList<HashMap>();
			NumberFormat numFormat = NumberFormat.getInstance();
			
/*			Properties props = propertiesUtilService.getPropertiesFromClasspath("File.properties");
			String currencyFormat = props.getProperty("currencyFormat");*/
			
			Connection conn = null;
			Statement stmt = null;
			ResultSet result = null; 
			try{
				
				DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
				conn = ds.getConnection();
				
				log.info("Connected to JDBC Successfully");
				stmt = conn.createStatement();

						String groupByString = "";
						if("DATE".equalsIgnoreCase(dataType))
						{
							groupByString = groupByString + "Date(det.`"+groupBy+"`)";
						}
						else
						{
							groupByString = groupByString + "det.`"+groupBy+"`";
						}
						
						String srcOrTrgtQuery = "";
						if("source".equalsIgnoreCase(srcOrTrgt))
						{
							srcOrTrgtQuery = " LEFT JOIN (select recon_reference, original_row_id from t_reconciliation_result where original_view_id = "+viewId+" and recon_status = 'RECONCILED'"
									+ " and current_record_flag is true) recon"
									+ " ON dv.scrIds = recon.original_row_id ";
						}
						else if("target".equalsIgnoreCase(srcOrTrgt))
						{
							srcOrTrgtQuery = " LEFT JOIN (select recon_reference, target_row_id from t_reconciliation_result where target_view_id = "+viewId+" and recon_status = 'RECONCILED'"
									+ " and current_record_flag is true) recon"
									+ " ON dv.scrIds = recon.target_row_id ";
						}
						
				String query = "select "+groupByString+", count(*), SUM(`"+amountQualifier+"`) from ("
						+ " select dv.*,"
						+ " 	 (case when acd.ACCOUNTING_STATUS is null and recon.recon_reference is null"
						+ " 		THEN        		"
						+ " 			'UN ACCOUNTED, NOT RECONCILED'"
						+ " 		when acd.ACCOUNTING_STATUS is null and recon.recon_reference is not null"
						+ "         then"
						+ "            'UN ACCOUNTED, RECONCILED'"
						+ "         when acd.ACCOUNTING_STATUS is not null and recon.recon_reference is null"
						+ "         then "
						+ "         	'ACCOUNTED, NOT RECONCILED'"
						+ "         when acd.ACCOUNTING_STATUS is not null and recon.recon_reference is not null"
						+ "         then "
						+ "		         'ACCOUNTED, RECONCILED' END) as accounting_recon_status,"
						+ "			appStatusLum.meaning final_status, final_action_date, "
						+ "			rl2.rule_code  approval_rule_code, " 
						+ "			source_ref,"
						+ "			currency_ref,	"
						+ "         coa_ref,"
						+ "         ledger_ref,"
						+ "         category_ref,  "
						+ "         led.name ledger_name,"
						+ "         coa.name coa_name,      "
						+ "			srclum.meaning source_meaning,   "
				        + "			catlum.meaning category_meaning,  "
				        + "	        case when acct_rule_id = 0   "
				        + "	        then 'Manual'        "
						+ "	        else   "
						+ "	        rl.rule_code end rule_code, " 
						+ "         rl.id rule_id, "
						/*+ "         acd.job_reference"*/
						+ " 		case when SUBSTRING(job_reference, 1, 6) = 'MANUAL' "
						+ " 			THEN "
						+ " 				'Manual' "
						+ " 			ELSE "
						+ "					acd.job_reference  end job_reference,amount_col_id "
						+ "        from (select * from `"+viewName+"` where Date(`"+dateQualifier+"`) between '"+rangeFrom+"' and '"+rangeTo+"') dv "
						+ "        LEFT JOIN (SELECT distinct su.STATUS ACCOUNTING_STATUS, "
						+ "        de.ledger_ref,"
						+ "        de.coa_ref,"
						+ "        de.acct_rule_id,"
						+ "        de.original_row_id, "
						+ "        de.source_ref,"
						+ "        de.category_ref,"
						+ "        de.tenant_id, "
						+ "		   de.currency_ref, "
						+ "		   de.approval_rule_id, DATE(de.final_action_date) final_action_date,de.final_status, de.amount_col_id, "
						+ "        de.job_reference FROM t_accounted_summary su, "
						+ "        t_accounting_data de WHERE su.id = de.accounted_summary_id AND su.rule_group_id = "+groupId+" and su.view_id = "+viewId+" and su.current_record_flag is true"
						+ " 	   and su.job_reference = de.job_reference) acd  "
						+ "        ON dv.scrIds = acd.original_row_id "
						+ "				 "+ srcOrTrgtQuery +" "
						+ "		   LEFT JOIN look_up_code srclum  ON acd.source_ref = srclum.look_up_code  and acd.tenant_id = srclum.tenant_id and srclum.look_up_type = 'SOURCE'"   
						+ "	       LEFT JOIN look_up_code catlum  ON acd.category_ref = catlum.look_up_code  and acd.tenant_id = catlum.tenant_id and catlum.look_up_type = 'CATEGORY'"
						+ "		   LEFT JOIN look_up_code appStatusLum ON acd.final_status = appStatusLum.look_up_code and acd.tenant_id = appStatusLum.tenant_id and appStatusLum.look_up_type = 'APPROVAL_STATUS'"
						+ "        LEFT JOIN t_ledger_definition led ON acd.ledger_ref = led.id "
						+ "        LEFT JOIN t_chart_of_account coa ON acd.coa_ref = coa.id "
						+ "        LEFT JOIN t_rules rl ON acd.acct_rule_id = rl.id"
						+ "		   LEFT JOIN t_rules rl2 ON acd.approval_rule_id = rl2.id"
						+ ") det where accounting_recon_status = '"+status+"' "+whereString+" group by "+groupByString+" order by "+groupByString+" asc";
				
				log.info("Grouping Query: "+query);
				result=stmt.executeQuery(query);
				if("final_status".equalsIgnoreCase(groupBy))
				{
		        	while(result.next()){
		        		HashMap summaryMap = new HashMap();
		        		String name = "";
		        		if(result.getString(1) == null)
		        		{
		        			name = "Not Required";
		        		}
		        		else if("InProcess".equalsIgnoreCase(result.getString(1)))
		        		{
		        			name = "Awaiting for Approvals";
		        		}
		        		else
		        		{
		        			name = result.getString(1);
		        		}
		        		String count = result.getString(2).toString();
		        		String amount = result.getString(3).toString();
			        	summaryMap.put("name", name);
			        	summaryMap.put("count", numFormat.format(Integer.parseInt(count)));
			        	summaryMap.put("amount", Double.parseDouble(amount));
			        	summaryMap.put("amountValue", amount);
			        	summaryMap.put("filterColumn", groupBy);
			        	summaryMap.put("dataType", dataType);
			        	finalList.add(summaryMap);
		        	}	
				}
				else
				{
		        	while(result.next()){
		        		HashMap summaryMap = new HashMap();
		        		String name = result.getString(1);
		        		String count = result.getString(2).toString();
		        		String amount = result.getString(3).toString();
		        		if(name != null)
		        		{
			        		summaryMap.put("name", name);
			        		summaryMap.put("count", numFormat.format(Integer.parseInt(count)));
			        		summaryMap.put("amount", Double.parseDouble(amount));
			        		summaryMap.put("amountValue", amount);
			        		summaryMap.put("filterColumn", groupBy);
			        		summaryMap.put("dataType", dataType);
			        		finalList.add(summaryMap);
		        		}
		        	}					
				}
			}
			catch(Exception e)
			{
				log.info("Exception while getting databse properties: "+ e);
			}
			finally{
				if(result != null)
					result.close();
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
			}		
			log.info("Group by: "+groupBy+" Size: "+finalList.size());
			return finalList;
		}

		public List<HashMap> getGroupByColsTrueMap(Long viewId)
		{
			log.info("Fetching data view columns info with group by is true for the view id: "+ viewId);
			List<HashMap> finalList = new ArrayList<HashMap>();
			List<DataViewsColumns> dvsc = dataViewsColumnsRepository.findByDataViewIdAndGroupByIsTrue(viewId);
			if(dvsc.size()>0)
			{
				for(DataViewsColumns dvc : dvsc)
				{
					HashMap colInfo = new HashMap();
					if("File Template".equalsIgnoreCase(dvc.getRefDvType()))
					{
						FileTemplateLines ftl = fileTemplateLinesRepository.findOne(Long.parseLong(dvc.getRefDvColumn().toString()));
						if(ftl != null)
						{
							colInfo.put("id", dvc.getId());
							colInfo.put("colName", ftl.getColumnAlias());
							colInfo.put("colDisplayName", dvc.getColumnName());
							colInfo.put("dataType", dvc.getColDataType());
						}
					}
					else if("Data View".equalsIgnoreCase(dvc.getRefDvType()) || dvc.getRefDvType() == null)
					{
						colInfo.put("id", dvc.getId());
						colInfo.put("colName", dvc.getColumnName());
						colInfo.put("colDisplayName", dvc.getColumnName());
						colInfo.put("dataType", dvc.getColDataType());			
					}
					finalList.add(colInfo);
				}
			}
			else 
			{
				log.info("No records found for group by is true for the view id: "+ viewId);
			}
			return finalList;
		}
		
		public String getQualifierViewColName(Long viewId, String qualifier)
		{
			log.info("Fetching "+qualifier+" column name for view id: "+viewId);
			DataViewsColumns dvc = dataViewsColumnsRepository.findByDataViewIdAndQualifier(viewId, qualifier);
			String qualifierColName = "";
			if(dvc != null)
			{
				if("File Template".equalsIgnoreCase(dvc.getRefDvType()))
				{
					FileTemplateLines ftl = fileTemplateLinesRepository.findOne(Long.parseLong(dvc.getRefDvColumn().toString()));
					if(ftl != null)
					{
						qualifierColName = ftl.getColumnAlias();
					}
				}
				else if("Data View".equalsIgnoreCase(dvc.getRefDvType()) || dvc.getRefDvType() == null)
				{
					qualifierColName = dvc.getColumnName();
				}
			}
			else
			{
				log.info("No "+qualifier+" found for the view id: "+ viewId);
			}
			return qualifierColName;
		}
		
		public Long getTotalCountForNonActivity(String status, String viewName, String dateQualifier, String rangeFrom, String rangeTo, Long tenantId, Long groupId, Long viewId, String whereString, Long pageNumber, Long pageSize, String globalSearch, String columnSearchQuery, String sortByColumnName, String sortOrderBy, String approvalStatus) throws SQLException, ClassNotFoundException
		{
			Connection conn = null;
			Statement stmt = null;
			ResultSet result = null; 
			Long totalCount = 0L;
			String dateQualifierCondition = "";
			if(dateQualifier != null)
			{
				dateQualifierCondition = " where Date(`"+dateQualifier+"`) between '"+rangeFrom+"' and '"+rangeTo+"'";
			}
			try{
				
				DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
				conn = ds.getConnection();
				
				log.info("Connected to JDBC Successfully");
				stmt = conn.createStatement();

				String query = "select scrIds, `"+sortByColumnName+"`, count(*) from  "
						+ " (select dv.*,   	"
						+ "   (case when acd.ACCOUNTING_STATUS is null "
						+ "		THEN        		"
						+ "			'UN ACCOUNTED'   	"
						+ "		ELSE           "
						+ "        acd.ACCOUNTING_STATUS  END) as accounting_status,"
						+ " appStatusLum.meaning final_status,"
						+ " rl2.rule_code  approval_rule_code, final_action_date, "
						+ " 		(case when acd.journal_status is null "
						+ "			THEN "
						+ "			'Pending for Journal' "
						+ "			 when acd.journal_status ='ENTERED' "
						+ "			 THEN "
						+ "			'Journals Prepared' end) as journal_status, " 
						+ "  	   source_ref,"
						+ "        coa_ref,"
						+ "        ledger_ref,"
						+ "        category_ref,"
						+ "		 	entered_currency,	"
						+ "			accounted_currency,	"
						+ "        led.name ledger_name,   "
						+ "        coa.name coa_name,"
						+ "		srclum.meaning source_meaning,   "
				        + "		catlum.meaning category_meaning,  "
						+ "			case when acct_rule_id = 0 "  
						+ "				then 'Manual' "        
						+ "			else   "
						+ "				rl.rule_code end rule_code, " 
						+ "        rl.id rule_id,  "
						/*+ "        acd.job_reference"*/
						+ " 	case when SUBSTRING(job_reference, 1, 6) = 'MANUAL' "
						+ " 		THEN "
						+ " 			'Manual' "
						+ " 		ELSE "
						+ "				acd.job_reference  end job_reference, amount_col_id "
						+ "        from (select * from `"+viewName+"`"+dateQualifierCondition+") dv "
						+ "        LEFT JOIN ("
						+ "        SELECT distinct su.STATUS ACCOUNTING_STATUS, su.journal_status, de.ledger_ref,de.coa_ref,de.acct_rule_id,de.original_row_id,"
						+ " de.approval_rule_id, DATE(de.final_action_date) final_action_date,de.final_status, "
						+ " de.source_ref,de.category_ref,de.job_reference, de.tenant_id, de.currency_ref entered_currency, de.amount_col_id, de.ledger_currency accounted_currency FROM 	"
						+ "       t_accounted_summary su,"
						+ "       t_accounting_data de WHERE su.id = de.accounted_summary_id AND su.rule_group_id = "+groupId+" and su.view_id = "+viewId+" and su.current_record_flag is true"+approvalStatus
						+ " 	   and su.job_reference = de.job_reference) acd  "
						+ "        ON dv.scrIds = acd.original_row_id  "
						+ " 		LEFT JOIN look_up_code srclum  ON acd.source_ref = srclum.look_up_code  and acd.tenant_id = srclum.tenant_id and srclum.look_up_type = 'SOURCE' "   
						+ "			LEFT JOIN look_up_code catlum  ON acd.category_ref = catlum.look_up_code  and acd.tenant_id = catlum.tenant_id and catlum.look_up_type = 'CATEGORY' "
						+ " 	   LEFT JOIN look_up_code appStatusLum ON acd.final_status = appStatusLum.look_up_code and acd.tenant_id = appStatusLum.tenant_id and appStatusLum.look_up_type = 'APPROVAL_STATUS'"
						+ "        LEFT JOIN t_ledger_definition led ON acd.ledger_ref = led.id "
						+ "        LEFT JOIN t_chart_of_account coa ON acd.coa_ref = coa.id "
						+ "        LEFT JOIN t_rules rl ON acd.acct_rule_id = rl.id "
						+ "		   LEFT JOIN t_rules rl2 ON acd.approval_rule_id = rl2.id) det "
						+ "        where accounting_status = '"+status+"' "+whereString+globalSearch+columnSearchQuery+" group by scrIds, `"+sortByColumnName+"` order by `"+sortByColumnName+"` "+ sortOrderBy;
					
				log.info("Pagination Ids Query: "+query);
				result=stmt.executeQuery(query);
				while(result.next()){
					if(result.getString(1) != null)
					{
						totalCount = totalCount + 1;
					}
				}
			}
			catch(Exception e)
			{
				log.info("Exception while getting databse properties: "+ e);
			}
			finally{
				if(result != null)
					result.close();
					if(stmt != null)
						stmt.close();
					if(conn != null)
						conn.close();
				}
			return totalCount;
		}
		
		public Long getTotalCountForActivity(String status, String viewName, String dateQualifier, String rangeFrom, String rangeTo, Long tenantId, Long groupId, Long viewId, String whereString, Long pageNumber, Long pageSize, String globalSearch, String columnSearchQuery, String sortByColumnName, String sortOrderBy, String approvalStatus, String srcOrTrgt) throws SQLException, ClassNotFoundException
		{
			Connection conn = null;
			Statement stmt = null;
			ResultSet result = null; 
			Long totalCount = 0L;
			String dateQualifierCondition = "";
			if(dateQualifier != null)
			{
				dateQualifierCondition = " where Date(`"+dateQualifier+"`) between '"+rangeFrom+"' and '"+rangeTo+"'";
			}
			try{

				DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
				conn = ds.getConnection();
				
				log.info("Connected to JDBC Successfully");
				stmt = conn.createStatement();			
				
				String srcOrTrgtQuery = "";
				if("source".equalsIgnoreCase(srcOrTrgt))
				{
					srcOrTrgtQuery = " LEFT JOIN (select recon_reference, original_row_id from t_reconciliation_result where original_view_id = "+viewId+" and recon_status = 'RECONCILED'"
							+ " and current_record_flag is true) recon"
							+ " ON dv.scrIds = recon.original_row_id ";
				}
				else if("target".equalsIgnoreCase(srcOrTrgt))
				{
					srcOrTrgtQuery = " LEFT JOIN (select recon_reference, target_row_id from t_reconciliation_result where target_view_id = "+viewId+" and recon_status = 'RECONCILED'"
							+ " and current_record_flag is true) recon"
							+ " ON dv.scrIds = recon.target_row_id ";
				}

				String query = "select scrIds, `"+sortByColumnName+"`, count(*) from ("
						+ "	select dv.*,"
						+ "		 (case when acd.ACCOUNTING_STATUS is null and recon.recon_reference is null"
						+ "			THEN        		"
						+ "				'UN ACCOUNTED, NOT RECONCILED'"
						+ "			when acd.ACCOUNTING_STATUS is null and recon.recon_reference is not null"
						+ "	        then"
						+ "	           'UN ACCOUNTED, RECONCILED'"
						+ "	        when acd.ACCOUNTING_STATUS is not null and recon.recon_reference is null"
						+ "	        then "
						+ "		       'ACCOUNTED, NOT RECONCILED'"
						+ "	        when acd.ACCOUNTING_STATUS is not null and recon.recon_reference is not null"
						+ "	        then "
						+ "		       'ACCOUNTED, RECONCILED' END) as accounting_recon_status,"
						+ " 		appStatusLum.meaning final_status, final_action_date, "
						+ " 		rl2.rule_code  approval_rule_code, "
						+ " 		(case when acd.journal_status is null "
						+ "			THEN "
						+ "			'Pending for Journal' "
						+ "			 when acd.journal_status ='ENTERED' "
						+ "			 THEN "
						+ "			'Journals Prepared' end) as journal_status, " 
						+ "  		source_ref,"
						+ "	        coa_ref,"
						+ "	        ledger_ref, "
						+ "	        category_ref,"
						+ "		 	entered_currency,	"
						+ "			accounted_currency,	"
						+ "	        led.name ledger_name, "
						+ "	        coa.name coa_name, "
						+ "			srclum.meaning source_meaning,   "
				        + "			catlum.meaning category_meaning,  "
						+ "			case when acct_rule_id = 0 "  
						+ "				then 'Manual' "        
						+ "			else   "
						+ "				rl.rule_code end rule_code, " 
						+ "	        rl.id rule_id, "
				/*		+ "	        acd.job_reference "*/
						+ " 	case when SUBSTRING(job_reference, 1, 6) = 'MANUAL' "
						+ " 		THEN "
						+ " 			'Manual' "
						+ " 		ELSE "
						+ "				acd.job_reference  end job_reference, amount_col_id "
						+ "	        from (select * from `"+viewName+"` "+dateQualifierCondition+") dv "
						+ "	        LEFT JOIN (SELECT distinct su.STATUS ACCOUNTING_STATUS, su.journal_status, de.ledger_ref,de.coa_ref,de.acct_rule_id,de.original_row_id, "
						+ " 	de.approval_rule_id, DATE(de.final_action_date) final_action_date,de.final_status, "
						+ "	        de.source_ref,"
						+ "	        de.category_ref,"
						+ "         de.tenant_id, "
						+ "		   de.currency_ref entered_currency,	"
						+ "		   de.ledger_currency accounted_currency, de.amount_col_id,	"
						+ "	        de.job_reference FROM t_accounted_summary su, "
						+ "	        t_accounting_data de WHERE su.id = de.accounted_summary_id AND su.rule_group_id = "+groupId+" and su.view_id = "+viewId+" and su.current_record_flag is true "+approvalStatus
						+ " 	   and su.job_reference = de.job_reference) acd  "
						+ "	        ON dv.scrIds = acd.original_row_id "
						+ "				 "+ srcOrTrgtQuery +" "
						+ " 		LEFT JOIN look_up_code srclum  ON acd.source_ref = srclum.look_up_code  and acd.tenant_id = srclum.tenant_id and srclum.look_up_type = 'SOURCE' "   
						+ "			LEFT JOIN look_up_code catlum  ON acd.category_ref = catlum.look_up_code  and acd.tenant_id = catlum.tenant_id and catlum.look_up_type = 'CATEGORY' "
						+ " 		LEFT JOIN look_up_code appStatusLum ON acd.final_status = appStatusLum.look_up_code and acd.tenant_id = appStatusLum.tenant_id and appStatusLum.look_up_type = 'APPROVAL_STATUS'"
						+ "	        LEFT JOIN t_ledger_definition led ON acd.ledger_ref = led.id "
						+ "	        LEFT JOIN t_chart_of_account coa ON acd.coa_ref = coa.id "
						+ "			LEFT JOIN t_rules rl ON acd.acct_rule_id = rl.id"
						+ "			LEFT JOIN t_rules rl2 ON acd.approval_rule_id = rl2.id) det where accounting_recon_status = '"+status+"' "+whereString+globalSearch+columnSearchQuery+" group by scrIds, `"+sortByColumnName+"` order by `"+sortByColumnName+"` "+ sortOrderBy;
					
				log.info("Total Ids Query for total count: "+query);
				result=stmt.executeQuery(query);
				while(result.next()){
					if(result.getString(1) != null)
					{
						totalCount = totalCount + 1;
					}
				}
			}
			catch(Exception e)
			{
				log.info("Exception while getting databse properties: "+ e);
			}
			finally{
				if(result != null)
					result.close();
					if(stmt != null)
						stmt.close();
					if(conn != null)
						conn.close();
				}	
			return totalCount;
		}
		
		public List<BigInteger> getNonActivityTotalIdsForBatchWise(String status, String viewName, String dateQualifier, String rangeFrom, String rangeTo, Long tenantId, Long groupId, Long viewId, String whereString, String globalSearch, String columnSearchQuery, String sortByColumnName, String sortOrderBy) throws SQLException, ClassNotFoundException
		{
			List<BigInteger> paginationIds = new ArrayList<BigInteger>();
			Connection conn = null;
			Statement stmt = null;
			ResultSet result = null; 
			try{
				
				DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
				conn = ds.getConnection();
				
				log.info("Connected to JDBC Successfully");
				stmt = conn.createStatement();				
				String query = "select scrIds, count(*) from  "
						+ " (select dv.*,   	"
						+ "   (case when acd.ACCOUNTING_STATUS is null "
						+ "		THEN        		"
						+ "			'UN ACCOUNTED'   	"
						+ "		ELSE           "
						+ "        acd.ACCOUNTING_STATUS  END) as accounting_status,"
						+ " appStatusLum.meaning final_status, rl2.rule_code  approval_rule_code, final_action_date, "
						+ "  	   journal_status,"
						+ "  	   source_ref,"
						+ "        coa_ref,"
						+ "        ledger_ref,"
						+ "        category_ref,"
						+ "		 	entered_currency,	"
						+ "			accounted_currency,	"
						+ "        led.name ledger_name,   "
						+ "        coa.name coa_name,"
						+ "        rl.rule_code,"
						+ "        rl.id rule_id,  "
						+ "		   srclum.meaning source_meaning,   "
				        + "		   catlum.meaning category_meaning,  "
						/*+ "        acd.job_reference"*/
						+ " 		case when SUBSTRING(job_reference, 1, 6) = 'MANUAL' "
						+ " 		THEN "
						+ " 			'Manual' "
						+ " 		ELSE "
						+ "				acd.job_reference  end job_reference, amount_col_id "
						+ "        from (select * from `"+viewName+"` where Date(`"+dateQualifier+"`) between '"+rangeFrom+"' and '"+rangeTo+"') dv "
						+ "        LEFT JOIN ("
						+ "        SELECT distinct su.STATUS ACCOUNTING_STATUS, su.journal_status, de.ledger_ref,de.coa_ref,de.acct_rule_id,de.original_row_id,"
						+ " de.approval_rule_id, DATE(de.final_action_date) final_action_date,de.final_status, "
						+ "	de.source_ref,de.category_ref,de.job_reference,de.tenant_id, de.currency_ref entered_currency, de.amount_col_id, de.ledger_currency accounted_currency FROM 	"
						+ "        t_accounted_summary su,"
						+ "        t_accounting_data de WHERE su.id = de.accounted_summary_id AND su.rule_group_id = "+groupId+" and su.view_id = "+viewId+" and su.current_record_flag is true "
						+ " 	   and su.job_reference = de.job_reference AND (su.journal_status is null AND (approval_status is null OR approval_status = 'REJECTED'))) acd  "
						+ "        ON dv.scrIds = acd.original_row_id  "
						+ " 		LEFT JOIN look_up_code srclum  ON acd.source_ref = srclum.look_up_code  and acd.tenant_id = srclum.tenant_id and srclum.look_up_type = 'SOURCE' "   
						+ "			LEFT JOIN look_up_code catlum  ON acd.category_ref = catlum.look_up_code  and acd.tenant_id = catlum.tenant_id and catlum.look_up_type = 'CATEGORY' "
						+ " LEFT JOIN look_up_code appStatusLum ON acd.final_status = appStatusLum.look_up_code and acd.tenant_id = appStatusLum.tenant_id and appStatusLum.look_up_type = 'APPROVAL_STATUS'"
						+ "        LEFT JOIN t_ledger_definition led ON acd.ledger_ref = led.id "
						+ "        LEFT JOIN t_chart_of_account coa ON acd.coa_ref = coa.id "
						+ "        LEFT JOIN t_rules rl ON acd.acct_rule_id = rl.id "
						+ "		   LEFT JOIN t_rules rl2 ON acd.approval_rule_id = rl2.id) det "
						+ "         where accounting_status = '"+status+"' "+whereString+globalSearch+columnSearchQuery+" group by scrIds"; // order by `"+sortByColumnName+"` "+ sortOrderBy;
				log.info("Pagination Ids Query: "+query);
				result=stmt.executeQuery(query);
				while(result.next()){
		        		if(result.getString(1) != null)
		        		{
			        		paginationIds.add(new BigInteger(result.getString(1)));
		        		}
		        	}
				}
				catch(Exception e)
				{
					log.info("Exception while getting databse properties: "+ e);
				}
				finally{
					if(result != null)
						result.close();
					if(stmt != null)
						stmt.close();
					if(conn != null)
						conn.close();
				}	
			return paginationIds;
		}
		
		public List<BigInteger> getPaginationIdsForNonActivity(String status, String viewName, String dateQualifier, String rangeFrom, String rangeTo, Long tenantId, Long groupId, Long viewId, String whereString, Long pageNumber, Long pageSize, String globalSearch, String columnSearchQuery, String sortByColumnName, String sortOrderBy, String approvalStatus) throws SQLException, ClassNotFoundException
		{
			List<BigInteger> paginationIds = new ArrayList<BigInteger>();
			Connection conn = null;
			Statement stmt = null;
			ResultSet result = null; 
			String dateQualifierCondition = "";
			if(dateQualifier != null)
			{
				dateQualifierCondition = " where Date(`"+dateQualifier+"`) between '"+rangeFrom+"' and '"+rangeTo+"'";
			}
			try{
				
				DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
				conn = ds.getConnection();
				
				log.info("Connected to JDBC Successfully");
				stmt = conn.createStatement();				
				String query = "select scrIds, `"+sortByColumnName+"`, count(*) from  "
						+ " (select dv.*,   	"
						+ "   (case when acd.ACCOUNTING_STATUS is null "
						+ "		THEN        		"
						+ "			'UN ACCOUNTED'   	"
						+ "		ELSE           "
						+ "        acd.ACCOUNTING_STATUS  END) as accounting_status,"
						+ " appStatusLum.meaning final_status, final_action_date, "
						+ "	rl2.rule_code  approval_rule_code, "
						+ " 		(case when acd.journal_status is null "
						+ "			THEN "
						+ "			'Pending for Journal' "
						+ "			 when acd.journal_status ='ENTERED' "
						+ "			 THEN "
						+ "			'Journals Prepared' end) as journal_status, " 
						+ "  	   source_ref,"
						+ "        coa_ref,"
						+ "        ledger_ref,"
						+ "        category_ref,"
						+ "		 	entered_currency,	"
						+ "			accounted_currency,	"
						+ "        led.name ledger_name,   "
						+ "        coa.name coa_name,"
						+ "		srclum.meaning source_meaning,   "
				        + "		catlum.meaning category_meaning,  "
						+ "			case when acct_rule_id = 0 "   
						+ "			then 'Manual'        "
						+ "			else   "
						+ "			rl.rule_code end rule_code, "
						+ "        rl.id rule_id,  "
						/*+ "        acd.job_reference"*/
						+ " 	case when SUBSTRING(job_reference, 1, 6) = 'MANUAL' "
						+ " 		THEN "
						+ " 			'Manual' "
						+ " 		ELSE "
						+ "				acd.job_reference  end job_reference, amount_col_id "
						+ "        from (select * from `"+viewName+"` "+dateQualifierCondition+") dv "
						+ "        LEFT JOIN ("
						+ "        SELECT distinct su.STATUS ACCOUNTING_STATUS, su.journal_status, de.ledger_ref,de.coa_ref,de.acct_rule_id,de.original_row_id,"
						+ " de.approval_rule_id, DATE(de.final_action_date) final_action_date,de.final_status, "
						+ "	de.source_ref,de.category_ref,de.job_reference, de.tenant_id, de.currency_ref entered_currency, de.amount_col_id, de.ledger_currency accounted_currency FROM 	"
						+ "        t_accounted_summary su,"
						+ "        t_accounting_data de WHERE su.id = de.accounted_summary_id AND su.rule_group_id = "+groupId+" and su.view_id = "+viewId+" and su.current_record_flag is true "+approvalStatus
						+ " 	   and su.job_reference = de.job_reference) acd  "
						+ "        ON dv.scrIds = acd.original_row_id  "
						+ "		   LEFT JOIN look_up_code srclum  ON acd.source_ref = srclum.look_up_code  and acd.tenant_id = srclum.tenant_id and srclum.look_up_type = 'SOURCE'  "
						+ "		   LEFT JOIN look_up_code catlum  ON acd.category_ref = catlum.look_up_code  and acd.tenant_id = catlum.tenant_id and catlum.look_up_type = 'CATEGORY' "
						+ " 	   LEFT JOIN look_up_code appStatusLum ON acd.final_status = appStatusLum.look_up_code and acd.tenant_id = appStatusLum.tenant_id and appStatusLum.look_up_type = 'APPROVAL_STATUS'"
						+ "        LEFT JOIN t_ledger_definition led ON acd.ledger_ref = led.id "
						+ "        LEFT JOIN t_chart_of_account coa ON acd.coa_ref = coa.id "
						+ "        LEFT JOIN t_rules rl ON acd.acct_rule_id = rl.id "
						+ "		   LEFT JOIN t_rules rl2 ON acd.approval_rule_id = rl2.id) det "
						+ "        where accounting_status = '"+status+"' "+whereString+globalSearch+columnSearchQuery+" group by scrIds, `"+sortByColumnName+"` order by `"+sortByColumnName+"` "+ sortOrderBy+" limit "+pageNumber+", "+pageSize+"";
				log.info("Pagination Ids Query: "+query);
				result=stmt.executeQuery(query);
				while(result.next()){
		        		if(result.getString(1) != null)
		        		{
			        		paginationIds.add(new BigInteger(result.getString(1)));
		        		}
		        	}
				}
				catch(Exception e)
				{
					log.info("Exception while getting databse properties: "+ e);
				}
				finally{
					if(result != null)
						result.close();
					if(stmt != null)
						stmt.close();
					if(conn != null)
						conn.close();
				}	
			return paginationIds;
		}

		public List<BigInteger> getActivityTotalIdsForBatchWise(String status, String viewName, String dateQualifier, String rangeFrom, String rangeTo, Long tenantId, Long groupId, Long viewId, String whereString, String globalSearch, String columnSearchQuery, String sortByColumnName, String sortOrderBy, String srcOrTrgt) throws SQLException, ClassNotFoundException
		{
			List<BigInteger> paginationIds = new ArrayList<BigInteger>();
			Connection conn = null;
			Statement stmt = null;
			ResultSet result = null; 
			try{
				
				DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
				conn = ds.getConnection();
				
				stmt = conn.createStatement();						
				
				String srcOrTrgtQuery = "";
				if("source".equalsIgnoreCase(srcOrTrgt))
				{
					srcOrTrgtQuery = " LEFT JOIN (select recon_reference, original_row_id from t_reconciliation_result where original_view_id = "+viewId+" and recon_status = 'RECONCILED'"
							+ " and current_record_flag is true) recon"
							+ " ON dv.scrIds = recon.original_row_id ";
				}
				else if("target".equalsIgnoreCase(srcOrTrgt))
				{
					srcOrTrgtQuery = " LEFT JOIN (select recon_reference, target_row_id from t_reconciliation_result where target_view_id = "+viewId+" and recon_status = 'RECONCILED'"
							+ " and current_record_flag is true) recon"
							+ " ON dv.scrIds = recon.target_row_id ";
				}
				
				String query = "select scrIds, count(*) from ("
						+ "	select dv.*,"
						+ "		 (case when acd.ACCOUNTING_STATUS is null and recon.recon_reference is null"
						+ "			THEN        		"
						+ "				'UN ACCOUNTED, NOT RECONCILED'"
						+ "			when acd.ACCOUNTING_STATUS is null and recon.recon_reference is not null"
						+ "	        then"
						+ "	           'UN ACCOUNTED, RECONCILED'"
						+ "	        when acd.ACCOUNTING_STATUS is not null and recon.recon_reference is null"
						+ "	        then "
						+ "		        'ACCOUNTED, NOT RECONCILED'"
						+ "         when acd.ACCOUNTING_STATUS is not null and recon.recon_reference is not null"
						+ "	        then "
						+ "         'ACCOUNTED, RECONCILED' END) as accounting_recon_status,"
						+ " appStatusLum.meaning final_status, rl2.rule_code  approval_rule_code, final_action_date, "
						+ "  		journal_status,"
						+ "  		source_ref,"
						+ "	        coa_ref,"
						+ "	        ledger_ref, "
						+ "	        category_ref,"
						+ "		 	entered_currency,	"
						+ "			accounted_currency,	"
						+ "	        led.name ledger_name, "
						+ "	        coa.name coa_name,"
						+ "			srclum.meaning source_meaning,   "
				        + "			catlum.meaning category_meaning,  "
						+ "			case when acct_rule_id = 0 "  
						+ "				then 'Manual' "        
						+ "			else   "
						+ "				rl.rule_code end rule_code, " 
						+ "	        rl.id rule_id, "
						+ " 		case when SUBSTRING(job_reference, 1, 6) = 'MANUAL' "
						+ " 		THEN "
						+ " 			'Manual' "
						+ " 		ELSE "
						+ "				acd.job_reference  end job_reference,amount_col_id "
						+ "	        from (select * from `"+viewName+"` where Date(`"+dateQualifier+"`) between '"+rangeFrom+"' and '"+rangeTo+"') dv "
						+ "	        LEFT JOIN (SELECT distinct su.STATUS ACCOUNTING_STATUS, su.journal_status, de.ledger_ref,de.coa_ref,de.acct_rule_id,de.original_row_id, "
						+ " de.approval_rule_id, DATE(de.final_action_date) final_action_date,de.final_status, "
						+ "	        de.source_ref,"
						+ "	        de.category_ref,"
						+ "			de.tenant_id, "	
						+ "		   de.currency_ref entered_currency,	"
						+ "		   de.ledger_currency accounted_currency, de.amount_col_id,	"
						+ "	        de.job_reference FROM t_accounted_summary su, "
						+ "	        t_accounting_data de WHERE su.id = de.accounted_summary_id AND su.rule_group_id = "+groupId+" and su.view_id = "+viewId+" and su.current_record_flag is true "
						+ " 	    and su.job_reference = de.job_reference AND (su.journal_status is null AND (approval_status is null OR approval_status = 'REJECTED'))) acd  "
						+ "	        ON dv.scrIds = acd.original_row_id "
						+ "				 "+ srcOrTrgtQuery +" "
						+ " 		LEFT JOIN look_up_code srclum  ON acd.source_ref = srclum.look_up_code  and acd.tenant_id = srclum.tenant_id and srclum.look_up_type = 'SOURCE' "   
						+ "			LEFT JOIN look_up_code catlum  ON acd.category_ref = catlum.look_up_code  and acd.tenant_id = catlum.tenant_id and catlum.look_up_type = 'CATEGORY' "
						+ " LEFT JOIN look_up_code appStatusLum ON acd.final_status = appStatusLum.look_up_code and acd.tenant_id = appStatusLum.tenant_id and appStatusLum.look_up_type = 'APPROVAL_STATUS'"
						+ "	        LEFT JOIN t_ledger_definition led ON acd.ledger_ref = led.id "
						+ "	        LEFT JOIN t_chart_of_account coa ON acd.coa_ref = coa.id "
						+ "			LEFT JOIN t_rules rl ON acd.acct_rule_id = rl.id"
						+ "			LEFT JOIN t_rules rl2 ON acd.approval_rule_id = rl2.id) det where accounting_recon_status = '"+status+"' "+whereString+globalSearch+columnSearchQuery+" group by scrIds"; // order by `"+sortByColumnName+"` "+ sortOrderBy;
				
				log.info("Pagination Ids Query: "+query);
				result=stmt.executeQuery(query);
				while(result.next()){
		        		if(result.getString(1) != null)
		        		{
			        		paginationIds.add(new BigInteger(result.getString(1)));
		        		}
		        	}
				}
				catch(Exception e)
				{
					log.info("Exception while getting databse properties: "+ e);
				}
				finally{
					if(result != null)
						result.close();
					if(stmt != null)
						stmt.close();
					if(conn != null)
						conn.close();
				}	
			return paginationIds;
		}
		
		public Long getTotalCountForUnAccActivity(Long groupId, Long viewId, String periodFactor, String rangeFrom, String rangeTo, String status,
				Long pageNumber, Long pageSize, String activityYorN, String innerWhereString, String srcOrTrgt)
		{
			Long totalCount = 0L;
			Connection conn = null;
			Statement stmt = null;
			ResultSet result = null; 
			try{

				DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
				conn = ds.getConnection();
				DataViews dv = dataViewsRepository.findOne(viewId);
				log.info("Connected to JDBC Successfully");
				stmt = conn.createStatement();
				String rowIdString = "";
				String viewIdString = "";
				if("source".equalsIgnoreCase(srcOrTrgt))
				{
					rowIdString = "original_row_id";
					viewIdString = "original_view_id";
				}
				else if("target".equalsIgnoreCase(srcOrTrgt))
				{
					rowIdString = "target_row_id";
					viewIdString = "target_view_id";				
				}
				String query = "";
				if("Y".equalsIgnoreCase(activityYorN))
				{
					query = "SELECT count(*) "
							+ " FROM (SELECT dv.*, 	"
							+ "               (CASE "
							+ "                   WHEN acd.accounting_status IS NULL "
							+ "                        AND recon.recon_reference IS NULL THEN "
							+ "                   'UN ACCOUNTED, NOT RECONCILED' "
							+ "                   WHEN acd.accounting_status IS NULL "
							+ "                        AND recon.recon_reference IS NOT NULL THEN "
							+ "                   'UN ACCOUNTED, RECONCILED' "
							+ "                   WHEN acd.accounting_status IS NOT NULL "
							+ "                        AND recon.recon_reference IS NULL THEN "
							+ "                   'ACCOUNTED, NOT RECONCILED' "
							+ "                   WHEN acd.accounting_status IS NOT NULL "
							+ "                        AND recon.recon_reference IS NOT NULL THEN "
							+ "                   'ACCOUNTED, RECONCILED' "
							+ "                 end)              AS accounting_status,"
							+ "               original_row_id"
							+ "        FROM   (SELECT * "
							+ "                FROM   `"+dv.getDataViewName().toLowerCase()+"` "
							+ "                WHERE  Date(`"+periodFactor+"`) BETWEEN '"+rangeFrom+"' AND '"+rangeTo+"') dv "
							+ "               LEFT JOIN (SELECT su.status ACCOUNTING_STATUS, "
							+ "                                 de.* "
							+ "                          FROM   t_accounted_summary su, "
							+ "                                 t_accounting_data de "
							+ "                          WHERE  su.id = de.accounted_summary_id "
							+ "                                 AND su.rule_group_id = "+groupId+" "
							+ "                                 AND su.view_id = "+viewId+" "
							+ "                                 AND su.current_record_flag IS TRUE) acd "
							+ "                      ON dv.scrids = acd.original_row_id "
							+ "               LEFT JOIN (SELECT recon_reference, "
							+ "                                 "+rowIdString+" AS source_row_id "
							+ "                          FROM   t_reconciliation_result "
							+ "                          WHERE  "+viewIdString+" = "+viewId+" "
							+ "                                 AND recon_status = 'RECONCILED' "
							+ "                                 AND current_record_flag IS TRUE) recon "
							+ "                      ON dv.scrids = recon.source_row_id) det "
							+ " WHERE  accounting_status = '"+status+"'"+innerWhereString;
				}
				else if("N".equalsIgnoreCase(activityYorN))
				{
					query = "SELECT count(*) "
							+ " FROM   (SELECT dv.*, "
							+ " (CASE "
							+ " WHEN acd.accounting_status IS NULL THEN 'UN ACCOUNTED' "
							+ " ELSE acd.accounting_status end ) AS accounting_status, "
							+ "               original_row_id"
							+ "        FROM   (SELECT * "
							+ "                FROM   `"+dv.getDataViewName().toLowerCase()+"` "
							+ "                WHERE  Date(`"+periodFactor+"`) BETWEEN '"+rangeFrom+"' AND '"+rangeTo+"') dv "
							+ "               LEFT JOIN (SELECT su.status ACCOUNTING_STATUS,  de.* "
							+ " FROM   t_accounted_summary su, "
							+ "	t_accounting_data de "
							+ " WHERE  su.id = de.accounted_summary_id "
							+ " AND su.rule_group_id = "+groupId+" "
							+ " AND su.view_id = "+viewId+" "
							+ " AND su.current_record_flag IS TRUE) acd "
							+ " ON dv.scrids = acd.original_row_id ) det "
							+ " WHERE  accounting_status = '"+status+"'"+innerWhereString;
				}

				log.info("Activity Based Un Accounted Pagination Ids Query: "+query);
				result=stmt.executeQuery(query);
					while(result.next())
					{
			        	if(result.getString(1) != null)
			        	{
			       			totalCount = totalCount + Long.parseLong(result.getString(1));
			       		}
			       	}
				}
				catch(Exception e)
				{
					log.info("Exception while getting databse properties: "+ e);
				}
				finally{
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
						log.info("Exception while closing statements: "+ e);
					}
				}	
			return totalCount;

		}
		
		
		public List<BigInteger> getPaginationIdsForUnAccActivity(Long groupId, Long viewId, String periodFactor, String rangeFrom, String rangeTo, String status, 
				Long pageNumber, Long pageSize, String activityYorN, String innerWhereString, String srcOrTrgt)
		{
			List<BigInteger> paginationIds = new ArrayList<BigInteger>();
			Connection conn = null;
			Statement stmt = null;
			ResultSet result = null; 
			try{

				DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
				conn = ds.getConnection();
				DataViews dv = dataViewsRepository.findOne(viewId);
				log.info("Connected to JDBC Successfully");
				stmt = conn.createStatement();
				String rowIdString = "";
				String viewIdString = "";
				if("source".equalsIgnoreCase(srcOrTrgt))
				{
					rowIdString = "original_row_id";
					viewIdString = "original_view_id";
				}
				else if("target".equalsIgnoreCase(srcOrTrgt))
				{
					rowIdString = "target_row_id";
					viewIdString = "target_view_id";				
				}
				String query = "";
				if("Y".equalsIgnoreCase(activityYorN))
				{
					query = "SELECT scrIds "
							+ " FROM (SELECT dv.*, 	"
							+ "               (CASE "
							+ "                   WHEN acd.accounting_status IS NULL "
							+ "                        AND recon.recon_reference IS NULL THEN "
							+ "                   'UN ACCOUNTED, NOT RECONCILED' "
							+ "                   WHEN acd.accounting_status IS NULL "
							+ "                        AND recon.recon_reference IS NOT NULL THEN "
							+ "                   'UN ACCOUNTED, RECONCILED' "
							+ "                   WHEN acd.accounting_status IS NOT NULL "
							+ "                        AND recon.recon_reference IS NULL THEN "
							+ "                   'ACCOUNTED, NOT RECONCILED' "
							+ "                   WHEN acd.accounting_status IS NOT NULL "
							+ "                        AND recon.recon_reference IS NOT NULL THEN "
							+ "                   'ACCOUNTED, RECONCILED' "
							+ "                 end)              AS accounting_status,"
							+ "               original_row_id"
							+ "        FROM   (SELECT * "
							+ "                FROM   `"+dv.getDataViewName().toLowerCase()+"` "
							+ "                WHERE  Date(`"+periodFactor+"`) BETWEEN '"+rangeFrom+"' AND '"+rangeTo+"') dv "
							+ "               LEFT JOIN (SELECT su.status ACCOUNTING_STATUS, "
							+ "                                 de.* "
							+ "                          FROM   t_accounted_summary su, "
							+ "                                 t_accounting_data de "
							+ "                          WHERE  su.id = de.accounted_summary_id "
							+ "                                 AND su.rule_group_id = "+groupId+" "
							+ "                                 AND su.view_id = "+viewId+" "
							+ "                                 AND su.current_record_flag IS TRUE) acd "
							+ "                      ON dv.scrids = acd.original_row_id "
							+ "               LEFT JOIN (SELECT recon_reference, "
							+ "                                 "+rowIdString+" AS source_row_id "
							+ "                          FROM   t_reconciliation_result "
							+ "                          WHERE  "+viewIdString+" = "+viewId+" "
							+ "                                 AND recon_status = 'RECONCILED' "
							+ "                                 AND current_record_flag IS TRUE) recon "
							+ "                      ON dv.scrids = recon.source_row_id) det "
							+ " WHERE  accounting_status = '"+status+"'"+innerWhereString+" limit "+pageNumber+", "+pageSize+"";
				}
				else if("N".equalsIgnoreCase(activityYorN))
				{
					query = "SELECT scrIds "
							+ " FROM   (SELECT dv.*, "
							+ " (CASE "
							+ " WHEN acd.accounting_status IS NULL THEN 'UN ACCOUNTED' "
							+ " ELSE acd.accounting_status end ) AS accounting_status, "
							+ "               original_row_id"
							+ "        FROM   (SELECT * "
							+ "                FROM   `"+dv.getDataViewName().toLowerCase()+"` "
							+ "                WHERE  Date(`"+periodFactor+"`) BETWEEN '"+rangeFrom+"' AND '"+rangeTo+"') dv "
							+ "               LEFT JOIN (SELECT su.status ACCOUNTING_STATUS,  de.* "
							+ " FROM   t_accounted_summary su, "
							+ "	t_accounting_data de "
							+ " WHERE  su.id = de.accounted_summary_id "
							+ " AND su.rule_group_id = "+groupId+" "
							+ " AND su.view_id = "+viewId+" "
							+ " AND su.current_record_flag IS TRUE) acd "
							+ " ON dv.scrids = acd.original_row_id ) det "
							+ " WHERE  accounting_status = '"+status+"'"+innerWhereString+" limit "+pageNumber+", "+pageSize+"";
				}

				log.info("Activity Based Un Accounted Pagination Ids Query: "+query);
				result=stmt.executeQuery(query);
				while(result.next()){
		        		if(result.getString(1) != null)
		        		{
			        		paginationIds.add(new BigInteger(result.getString(1)));
		        		}
		        	}
				}
				catch(Exception e)
				{
					log.info("Exception while getting databse properties: "+ e);
				}
				finally{
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
						log.info("Exception while closing statements: "+ e);
					}
				}	
			return paginationIds;

		}
		
		public List<BigInteger> getPaginationIdsForActivity(String status, String viewName, String dateQualifier, String rangeFrom, String rangeTo, Long tenantId, Long groupId, Long viewId, String whereString, Long pageNumber, Long pageSize, String globalSearch, String columnSearchQuery, String sortByColumnName, String sortOrderBy, String approvalStatus, String srcOrTrgt) throws SQLException, ClassNotFoundException
		{
			List<BigInteger> paginationIds = new ArrayList<BigInteger>();
			Connection conn = null;
			Statement stmt = null;
			ResultSet result = null; 
			String dateQualifierCondition = "";
			if(dateQualifier != null)
			{
				dateQualifierCondition = " where Date(`"+dateQualifier+"`) between '"+rangeFrom+"' and '"+rangeTo+"'";
			}
			try{
				
				String srcOrTrgtQuery = "";
				if("source".equalsIgnoreCase(srcOrTrgt))
				{
					srcOrTrgtQuery = " LEFT JOIN (select recon_reference, original_row_id from t_reconciliation_result where original_view_id = "+viewId+" and recon_status = 'RECONCILED'"
							+ " and current_record_flag is true) recon"
							+ " ON dv.scrIds = recon.original_row_id ";
				}
				else if("target".equalsIgnoreCase(srcOrTrgt))
				{
					srcOrTrgtQuery = " LEFT JOIN (select recon_reference, target_row_id from t_reconciliation_result where target_view_id = "+viewId+" and recon_status = 'RECONCILED'"
							+ " and current_record_flag is true) recon"
							+ " ON dv.scrIds = recon.target_row_id ";
				}
				
				DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
				conn = ds.getConnection();
				
				log.info("Connected to JDBC Successfully");
				stmt = conn.createStatement();							
				String query = "select scrIds,`"+sortByColumnName+"`, count(*) from ("
						+ "	select dv.*,"
						+ "		 (case when acd.ACCOUNTING_STATUS is null and recon.recon_reference is null"
						+ "			THEN        		"
						+ "				'UN ACCOUNTED, NOT RECONCILED'"
						+ "			when acd.ACCOUNTING_STATUS is null and recon.recon_reference is not null"
						+ "	        then"
						+ "	           'UN ACCOUNTED, RECONCILED'"
						+ "	        when acd.ACCOUNTING_STATUS is not null and recon.recon_reference is null"
						+ "	        then "
						+ "		        'ACCOUNTED, NOT RECONCILED'"
						+ "         when acd.ACCOUNTING_STATUS is not null and recon.recon_reference is not null"
						+ "	        then "
						+ "         'ACCOUNTED, RECONCILED' END) as accounting_recon_status,"
						+ " 	appStatusLum.meaning final_status, "
						+ " 	rl2.rule_code approval_rule_code, final_action_date, "
						+ " 		(case when acd.journal_status is null "
						+ "			THEN "
						+ "			'Pending for Journal' "
						+ "			 when acd.journal_status ='ENTERED' "
						+ "			 THEN "
						+ "			'Journals Prepared' end) as journal_status, " 
						+ "  		source_ref,"
						+ "	        coa_ref,"
						+ "	        ledger_ref, "
						+ "	        category_ref,"
						+ "		 	entered_currency,	"
						+ "			accounted_currency,	"
						+ "	        led.name ledger_name, "
						+ "	        coa.name coa_name,"
						+ "			srclum.meaning source_meaning,   "
				        + "			catlum.meaning category_meaning,  "
						+ "			case when acct_rule_id = 0 "  
						+ "				then 'Manual'        "
						+ "			else   "
						+ "				rl.rule_code end rule_code, "
						+ "	        rl.id rule_id, "
					/*	+ "	        acd.job_reference "*/
						+ " 	case when SUBSTRING(job_reference, 1, 6) = 'MANUAL' "
						+ " 		THEN "
						+ " 			'Manual' "
						+ " 		ELSE "
						+ "				acd.job_reference  end job_reference, amount_col_id "
						+ "	        from (select * from `"+viewName+"` "+dateQualifierCondition+") dv "
						+ "	        LEFT JOIN (SELECT distinct su.STATUS ACCOUNTING_STATUS,su.journal_status, de.ledger_ref,de.coa_ref,de.acct_rule_id,de.original_row_id, "
						+ " 	de.approval_rule_id, DATE(de.final_action_date) final_action_date,de.final_status, "
						+ "	        de.source_ref,"
						+ "	        de.category_ref,"
						+ "         de.tenant_id, "
						+ "		   de.currency_ref entered_currency,	"
						+ "		   de.ledger_currency accounted_currency, de.amount_col_id,	"
						+ "	        de.job_reference FROM t_accounted_summary su, "
						+ "	        t_accounting_data de WHERE su.id = de.accounted_summary_id AND su.rule_group_id = "+groupId+" and su.view_id = "+viewId+" and su.current_record_flag is true "+approvalStatus
						+ " 	    and su.job_reference = de.job_reference) acd  "
						+ "	        ON dv.scrIds = acd.original_row_id "
						+ "				 "+ srcOrTrgtQuery +" "
						+ "			LEFT JOIN look_up_code srclum  ON acd.source_ref = srclum.look_up_code  and acd.tenant_id = srclum.tenant_id and srclum.look_up_type = 'SOURCE' "   
						+ "			LEFT JOIN look_up_code catlum  ON acd.category_ref = catlum.look_up_code  and acd.tenant_id = catlum.tenant_id and catlum.look_up_type = 'CATEGORY' "
						+ " 		LEFT JOIN look_up_code appStatusLum ON acd.final_status = appStatusLum.look_up_code and acd.tenant_id = appStatusLum.tenant_id and appStatusLum.look_up_type = 'APPROVAL_STATUS'"
						+ "	        LEFT JOIN t_ledger_definition led ON acd.ledger_ref = led.id "
						+ "	        LEFT JOIN t_chart_of_account coa ON acd.coa_ref = coa.id "
						+ "			LEFT JOIN t_rules rl ON acd.acct_rule_id = rl.id"
						+ "			LEFT JOIN t_rules rl2 ON acd.approval_rule_id = rl2.id) det where accounting_recon_status = '"+status+"' "+whereString+globalSearch+columnSearchQuery+" group by scrIds, `"+sortByColumnName+"` order by `"+sortByColumnName+"` "+ sortOrderBy+" limit "+pageNumber+", "+pageSize+"";
				
				log.info("Pagination Ids Query: "+query);
				result=stmt.executeQuery(query);
				while(result.next()){
		        		if(result.getString(1) != null)
		        		{
			        		paginationIds.add(new BigInteger(result.getString(1)));
		        		}
		        	}
				}
				catch(Exception e)
				{
					log.info("Exception while getting databse properties: "+ e);
				}
				finally{
					if(result != null)
						result.close();
					if(stmt != null)
						stmt.close();
					if(conn != null)
						conn.close();
				}	
			return paginationIds;
		}

		public List<HashMap> getUnAccountedGroupingDetailInfo(Long groupId, Long viewId, String periodFactor, String rangeFrom, String rangeTo, String activityYorN,
				String status, Long pageNumber, Long pageSize, String idsWhereString, HashMap headerColumns, String srcOrTrgt)
		{
			List<HashMap> finalList = new ArrayList<HashMap>();
			
			Connection conn = null;
			Statement stmt = null;
			ResultSet result = null;
			
			try{
				DataViews dv = dataViewsRepository.findOne(viewId);
				DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
				conn = ds.getConnection();
				stmt = conn.createStatement();
				String query = "";
				
				String rowIdString = "";
				String viewIdString = "";
				if("source".equalsIgnoreCase(srcOrTrgt))
				{
					rowIdString = "original_row_id";
					viewIdString = "original_view_id";
				}
				else if("target".equalsIgnoreCase(srcOrTrgt))
				{
					rowIdString = "target_row_id";
					viewIdString = "target_view_id";				
				}
				
				if("Y".equalsIgnoreCase(activityYorN))
				{
					query = "SELECT * "
							+ " FROM (SELECT dv.*, 	"
							+ "               (CASE "
							+ "                   WHEN acd.accounting_status IS NULL "
							+ "                        AND recon.recon_reference IS NULL THEN "
							+ "                   'UN ACCOUNTED, NOT RECONCILED' "
							+ "                   WHEN acd.accounting_status IS NULL "
							+ "                        AND recon.recon_reference IS NOT NULL THEN "
							+ "                   'UN ACCOUNTED, RECONCILED' "
							+ "                   WHEN acd.accounting_status IS NOT NULL "
							+ "                        AND recon.recon_reference IS NULL THEN "
							+ "                   'ACCOUNTED, NOT RECONCILED' "
							+ "                   WHEN acd.accounting_status IS NOT NULL "
							+ "                        AND recon.recon_reference IS NOT NULL THEN "
							+ "                   'ACCOUNTED, RECONCILED' "
							+ "                 end)              AS accounting_status,"
							+ "               original_row_id"
							+ "        FROM   (SELECT * "
							+ "                FROM   `"+dv.getDataViewName().toLowerCase()+"` "
							+ "                WHERE  Date(`"+periodFactor+"`) BETWEEN '"+rangeFrom+"' AND '"+rangeTo+"') dv "
							+ "               LEFT JOIN (SELECT su.status ACCOUNTING_STATUS, "
							+ "                                 de.* "
							+ "                          FROM   t_accounted_summary su, "
							+ "                                 t_accounting_data de "
							+ "                          WHERE  su.id = de.accounted_summary_id "
							+ "                                 AND su.rule_group_id = "+groupId+" "
							+ "                                 AND su.view_id = "+viewId+" "
							+ "                                 AND su.current_record_flag IS TRUE) acd "
							+ "                      ON dv.scrids = acd.original_row_id "
							+ "               LEFT JOIN (SELECT recon_reference, "
							+ "                                 "+rowIdString+" AS source_row_id "
							+ "                          FROM   t_reconciliation_result "
							+ "                          WHERE  "+viewIdString+" = "+viewId+" "
							+ "                                 AND recon_status = 'RECONCILED' "
							+ "                                 AND current_record_flag IS TRUE) recon "
							+ "                      ON dv.scrids = recon.source_row_id) det "
							+ " WHERE  accounting_status = '"+status+"' "+idsWhereString;
				}
				else if("N".equalsIgnoreCase(activityYorN))
				{
					query = "SELECT * "
							+ " FROM   (SELECT dv.*, "
							+ " (CASE "
							+ " WHEN acd.accounting_status IS NULL THEN 'UN ACCOUNTED' "
							+ " ELSE acd.accounting_status end ) AS accounting_status, "
							+ "               original_row_id"
							+ "        FROM   (SELECT * "
							+ "                FROM   `"+dv.getDataViewName().toLowerCase()+"` "
							+ "                WHERE  Date(`"+periodFactor+"`) BETWEEN '"+rangeFrom+"' AND '"+rangeTo+"') dv "
							+ "               LEFT JOIN (SELECT su.status ACCOUNTING_STATUS,  de.* "
							+ " FROM   t_accounted_summary su, "
							+ "	t_accounting_data de "
							+ " WHERE  su.id = de.accounted_summary_id "
							+ " AND su.rule_group_id = "+groupId+" "
							+ " AND su.view_id = "+viewId+" "
							+ " AND su.current_record_flag IS TRUE) acd "
							+ " ON dv.scrids = acd.original_row_id ) det "
							+ " WHERE  accounting_status = '"+status+"'"+idsWhereString;
				}
				log.info("Query to fetch un-accounted detail info: "+ query);
				result=stmt.executeQuery(query);
				while(result.next()){
					HashMap dataMap = new HashMap();
	 	    	    Iterator it = headerColumns.entrySet().iterator();
	 	    	    dataMap.put("dataRowId", Long.parseLong(result.getString("scrIds")));
	 	    	    while(it.hasNext())	// header columns
	 	    	    {
	 	    	    	Map.Entry pair = (Map.Entry)it.next();
	 	    	    	dataMap.put(pair.getValue().toString(), result.getString(pair.getKey().toString()));   
	 	    	    	finalList.add(dataMap);
	 	    	    }
				}
			}
			catch(Exception e)
			{
				log.info("Exception while fetching un accounted detail info: "+ e);
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
					log.info("Exception while closing statements: "+e);
				}
			}
			return finalList;
		}
		public List<HashMap> getAccountingDetailInfo(String viewName, String dateQualifier, String rangeFrom, String rangeTo, Long viewId, Long groupId, String status, String whereString, HashMap headerColumns, String activityOrNonActivity, String approvalStatus, String srcOrTrgt,
				String segmentSeparator, Long tenantId) throws SQLException, ClassNotFoundException
		{
			List<HashMap> finalList = new ArrayList<HashMap>();
			Connection conn = null;
			Statement stmt = null;
			ResultSet result = null;
			String dateQualifierCondition = "";
			if(dateQualifier != null)
			{
				dateQualifierCondition = " where Date(`"+dateQualifier+"`) between '"+rangeFrom+"' and '"+rangeTo+"'";
			}
			try{
				String srcOrTrgtQuery = "";
				if("source".equalsIgnoreCase(srcOrTrgt))
				{
					srcOrTrgtQuery = " LEFT JOIN (select recon_reference, original_row_id as source_row_id from t_reconciliation_result where original_view_id = "+viewId+" and recon_status = 'RECONCILED'"
							+ " and current_record_flag is true) recon"
							+ " ON dv.scrIds = recon.source_row_id ";
				}
				else if("target".equalsIgnoreCase(srcOrTrgt))
				{
					srcOrTrgtQuery = " LEFT JOIN (select recon_reference, target_row_id from t_reconciliation_result where target_view_id = "+viewId+" and recon_status = 'RECONCILED'"
							+ " and current_record_flag is true) recon"
							+ " ON dv.scrIds = recon.target_row_id ";
				}
				
				DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
				conn = ds.getConnection();
				
				log.info("Connected to JDBC Successfully");
				stmt = conn.createStatement();
				String query = "";
				if("Y".equalsIgnoreCase(activityOrNonActivity))
				{
					query = " select * from ("
							+ "	select dv.*,"
							+ "		(case when acd.ACCOUNTING_STATUS is null and recon.recon_reference is null"
							+ "		THEN        		"
							+ "			'UN ACCOUNTED, NOT RECONCILED'"
							+ "		when acd.ACCOUNTING_STATUS is null and recon.recon_reference is not null"
							+ "     then"
							+ "         'UN ACCOUNTED, RECONCILED'"
							+ "     when acd.ACCOUNTING_STATUS is not null and recon.recon_reference is null"
							+ "     then "
							+ "         'ACCOUNTED, NOT RECONCILED'"
							+ "     when acd.ACCOUNTING_STATUS is not null and recon.recon_reference is not null"
							+ "     then "
							+ "         'ACCOUNTED, RECONCILED' END) as accounting_status,"
							+ " appStatusLum.meaning final_status, final_action_date, "
							+ "	rl2.rule_code  approval_rule_code, "
							+ " 		(case when acd.journal_status is null "
							+ "			THEN "
							+ "			'Pending for Journal' "
							+ "			 when acd.journal_status ='ENTERED' "
							+ "			 THEN "
							+ "			'Journals Prepared' end) as journal_status, (case WHEN acd.status IS NULL THEN 'Un Accounted' ELSE acd.status end) status, "
							+ "     original_row_id,  "
							+ "     line_type,  "
							+ "     line_type_detail,  "
							+ "		source_ref,	"
							+ "     coa_ref,  "
							+ "     ledger_ref,  "
							+ "     category_ref,"
							+ "		currency_ref entered_currency,	"
							+ "		ledger_currency accounted_currency,	"
							+ "     led.name ledger_name,  "
							+ "     acd.amount  entered_amount,"
							+ "		srclum.meaning source_meaning,   "
					        + "		catlum.meaning category_meaning,  "
							+ "     accounted_amount,  "
							+ "     coa.name coa_name,  "
							+ "     accounting_ref_1,accounting_ref_2,accounting_ref_3,accounting_ref_4,accounting_ref_5,accounting_ref_6,accounting_ref_7,accounting_ref_8,accounting_ref_9, accounting_ref_10,  "
							+ "			case when acct_rule_id = 0 "  
							+ "				then 'Manual' "        
							+ "			else   "
							+ "				rl.rule_code end rule_code, " 
							+ "     rl.id rule_id,  "
							/*+ "     acd.job_reference  "*/
							+ " 	case when SUBSTRING(job_reference, 1, 6) = 'MANUAL' "
							+ " 		THEN "
							+ " 			'Manual' "
							+ " 		ELSE "
							+ "				acd.job_reference  end job_reference, amount_col_id "
							+ "     from (select * from `"+viewName+"` "+dateQualifierCondition+") dv "
							+ "        LEFT JOIN (SELECT su.STATUS ACCOUNTING_STATUS,su.journal_status, de.* FROM t_accounted_summary su, "
							+ "        t_accounting_data de WHERE su.id = de.accounted_summary_id AND su.rule_group_id = "+groupId+" and su.view_id = "+viewId+" and su.current_record_flag is true "+approvalStatus+") acd  "
							/* + " 	   and su.job_reference = de.job_reference) acd  "*/
							+ "        ON dv.scrIds = acd.original_row_id "
							+ "				 "+ srcOrTrgtQuery +" "
							+ " 		LEFT JOIN look_up_code srclum  ON acd.source_ref = srclum.look_up_code  and acd.tenant_id = srclum.tenant_id and srclum.look_up_type = 'SOURCE' "   
							+ "			LEFT JOIN look_up_code catlum  ON acd.category_ref = catlum.look_up_code  and acd.tenant_id = catlum.tenant_id and catlum.look_up_type = 'CATEGORY' "
							+ " 	   LEFT JOIN look_up_code appStatusLum ON acd.final_status = appStatusLum.look_up_code and acd.tenant_id = appStatusLum.tenant_id and appStatusLum.look_up_type = 'APPROVAL_STATUS'"
							+ "        LEFT JOIN t_ledger_definition led ON acd.ledger_ref = led.id "
							+ "        LEFT JOIN t_chart_of_account coa ON acd.coa_ref = coa.id "
							+ "        LEFT JOIN t_rules rl ON acd.acct_rule_id = rl.id"
							+ "		   LEFT JOIN t_rules rl2 ON acd.approval_rule_id = rl2.id) det  where accounting_status = '"+status+"' "+whereString;
				}
				else if("N".equalsIgnoreCase(activityOrNonActivity))
				{
					query = "select * from "
							+ " (select dv.*,  "
							+ " 	(case when acd.ACCOUNTING_STATUS is null  "	
							+ "     THEN       "
							+ " 		'UN ACCOUNTED'  "
							+ " 	ELSE      "
							+ "     acd.ACCOUNTING_STATUS  END) as accounting_status,  "
							+ " appStatusLum.meaning final_status, rl2.rule_code  approval_rule_code, final_action_date, "
							+ " 		(case when acd.journal_status is null "
							+ "			THEN "
							+ "			'Pending for Journal' "
							+ "			 when acd.journal_status ='ENTERED' "
							+ "			 THEN "
							+ "			'Journals Prepared' end) as journal_status, (case WHEN acd.status IS NULL THEN 'Un Accounted' ELSE acd.status end) status, "
							+ "     original_row_id,  "
							+ "     line_type,  "
							+ "     line_type_detail,  "
							+ "		source_ref,	"
							+ "     coa_ref,  "
							+ "     ledger_ref,  "
							+ "     category_ref,"
							+ "		currency_ref entered_currency,	"
							+ "		ledger_currency accounted_currency,	"
							+ "     led.name ledger_name,  "
							+ "		srclum.meaning source_meaning,   "
					        + "		catlum.meaning category_meaning,  "
							+ "     acd.amount entered_amount, "
							+ "     accounted_amount,  "
							+ "     coa.name coa_name,  "
							+ "     accounting_ref_1,accounting_ref_2,accounting_ref_3,accounting_ref_4,accounting_ref_5,accounting_ref_6,accounting_ref_7,accounting_ref_8,accounting_ref_9, accounting_ref_10,  "
							+ "			case when acct_rule_id = 0 "  
							+ "				then 'Manual' "        
							+ "			else   "
							+ "				rl.rule_code end rule_code, " 
							+ "     rl.id rule_id,  "	
							+ "     acd.job_reference, amount_col_id"
							+ "     from (select * from `"+viewName+"` "+dateQualifierCondition+") dv "
							+ "     LEFT JOIN ("
							+ "     SELECT su.STATUS ACCOUNTING_STATUS, su.journal_status, de.* FROM t_accounted_summary su, "
							+ "     t_accounting_data de WHERE su.id = de.accounted_summary_id AND su.rule_group_id = "+groupId+" and su.view_id = "+viewId+" and su.current_record_flag is true "+approvalStatus+") acd"
						/*	+ " 	and su.job_reference = de.job_reference) acd  "*/
							+ "     ON dv.scrIds = acd.original_row_id "
							+ " 	LEFT JOIN look_up_code srclum  ON acd.source_ref = srclum.look_up_code  and acd.tenant_id = srclum.tenant_id and srclum.look_up_type = 'SOURCE' "   
							+ "		LEFT JOIN look_up_code catlum  ON acd.category_ref = catlum.look_up_code  and acd.tenant_id = catlum.tenant_id and catlum.look_up_type = 'CATEGORY' "
							+ " 	LEFT JOIN look_up_code appStatusLum ON acd.final_status = appStatusLum.look_up_code and acd.tenant_id = appStatusLum.tenant_id and appStatusLum.look_up_type = 'APPROVAL_STATUS'"
							+ "     LEFT JOIN t_ledger_definition led ON acd.ledger_ref = led.id "
							+ "     LEFT JOIN t_chart_of_account coa ON acd.coa_ref = coa.id "
							+ "     LEFT JOIN t_rules rl ON acd.acct_rule_id = rl.id "
							+ "		LEFT JOIN t_rules rl2 ON acd.approval_rule_id = rl2.id) det "
							+ "     where accounting_status = '"+status+"' "+whereString; 
				}
				log.info("Detail Info Query: "+ query);
				result=stmt.executeQuery(query);
				ResultSetMetaData rsmd = result.getMetaData();
 	    	    List<String> columns = new ArrayList<String>(rsmd.getColumnCount());
 	    	    for(int i = 1; i <= rsmd.getColumnCount(); i++){
 	    	        columns.add(rsmd.getColumnName(i));
 	    	    }
 	    	    log.info("Columns Size: "+columns.size());
 	    	    Map<BigInteger, List<HashMap>> parentChildList = new HashMap<BigInteger, List<HashMap>>();
 	    	    if(columns.size()>0)
 	    	    {
 	 	    	    while(result.next()){	// JDBC result set
 	 	    	    	HashMap dataMap = new HashMap();
 	 	    	    	Iterator it = headerColumns.entrySet().iterator();
 	 	    	    	dataMap.put("dataRowId", Long.parseLong(result.getString("scrIds")));
 	 	    	    	while(it.hasNext())	// header columns
 	 	    	    	{
 	 	    	    		Map.Entry pair = (Map.Entry)it.next();
 	 	    	    		dataMap.put(pair.getValue().toString(), result.getString(pair.getKey().toString()));
 	 	    	    	}
 	 	    	    	if(dataMap.get("ledger_ref") != null)
 	 	    	    	{
 	 	    	    		LedgerDefinition ld = ledgerDefinitionRepository.findOne(Long.parseLong(dataMap.get("ledger_ref").toString()));
 	 	    	    		dataMap.put("ledger_ref", ld.getIdForDisplay());
 	 	    	    	}
 	 	    	    	if(dataMap.get("coa_ref") != null)
 	 	    	    	{
 	 	 	 	    	    ChartOfAccount chartOfAccount = chartOfAccountRepository.findOne(Long.parseLong(dataMap.get("coa_ref").toString()));
 	 	 	 	    	    dataMap.put("coa_ref", chartOfAccount.getIdForDisplay());
 	 	    	    	}
 	 	 	    	    
 	 	 	    	    if(parentChildList.containsKey(new BigInteger(result.getString("scrIds").toString())))
 	 	 	    	    {
 	 	 	    	    	List<HashMap> existedList = parentChildList.get(new BigInteger(result.getString("scrIds").toString()));
 	 	 	    	    	dataMap.put("Debit Account", "");
 	 	 	    	    	dataMap.put("Credit Account", "");
 	 	 	    	    	existedList.add(dataMap);
 	 	 	    	    	parentChildList.put(new BigInteger(result.getString("scrIds").toString()), existedList);
 	 	 	    	    }
 	 	 	    	    else
 	 	 	    	    {
 	 	 	    	    	List<HashMap> dataList = new ArrayList<HashMap>();
 	 	 	    	    	dataMap.put("Debit Account", "");
 	 	 	    	    	dataMap.put("Credit Account", "");
 	 	 	    	    	dataList.add(dataMap);
 	 	 	    	    	parentChildList.put(new BigInteger(result.getString("scrIds").toString()), dataList);
 	 	 	    	    }
 	 	    	    }
 	 	    	    log.info("Parent Child List Size: "+parentChildList.size());
 	 	    	   // log.info("DataMap List>> : "+ parentChildList);
 	 	    	    if(parentChildList.size()>0)
 	 	    	    {
 	 	    	    	for(Map.Entry<BigInteger, List<HashMap>> entry : parentChildList.entrySet())
 	 	    	    	{
 	 	    	    		BigInteger rowId = entry.getKey();
 	 	    	    		List<HashMap> records = entry.getValue();
 	 	    	    		if(records.size()>0)
 	 	    	    		{
 	 	    	    			List<HashMap> children = new ArrayList<HashMap>();
 	 	    	    			List<HashMap> childrenList = new ArrayList<HashMap>();
 	 	    	    			List<String> acctdStatus = new ArrayList<String>();
 	 	    	    			for(HashMap mp : records)
 	 	    	    			{
 	 	    	    				acctdStatus.add(mp.get("Accounted Status").toString());
 	 	    	    				HashMap childMap = new HashMap();
 	 	    	    				if(mp.get("coa_ref") != null)
 	 	    	    				{
 	 	    	    					ChartOfAccount chartOfAccount = chartOfAccountRepository.findByIdForDisplayAndTenantId(mp.get("coa_ref").toString(), tenantId);
 	 	    	    					int segmentLength = accountingDataService.getSegmentsLengthWithCoaRef(chartOfAccount.getId());
 	 	    	    					String codeCombination = "";
 	 	    	    					if(segmentLength>0)
 	 	    	    					{
 	 	    	    						for(int i=0; i<segmentLength; i++)
 	 	    	    						{
 	 	    	    							if(mp.get("accounting_ref_"+(i+1)) != null)
 	 	    	    							{
 	 	    	    								codeCombination = codeCombination +mp.get("accounting_ref_"+(i+1)).toString()+segmentSeparator;
 	 	    	    							}
 	 	    	    							else
 	 	    	    							{
 	 	    	    								codeCombination = codeCombination +"XXXXX"+segmentSeparator;
 	 	    	    							}
 	 	    	    						}
 	 	    	    					}
 	 	    	    					if(codeCombination.length()>0)
 	 	    	    					{
 	 	    	    						codeCombination = codeCombination.substring(0, codeCombination.length() - 1);
 	 	    	    					}	
 	 		    	    				if("CREDIT".equalsIgnoreCase(mp.get("line_type").toString()))
 	 	 	    	    				{
 	 	 	    	    					mp.put("Credit Account", codeCombination);
 	 	 	    	    					childMap.put("Credit Account", codeCombination);
 	 	 	    	    					childMap.put("Debit Account", "");
 	 	 	    	    					if(mp.get("Entered Amount") != null)
 	 	 	 	    	    				{
 	 	 	    	    						String enteredAmount = mp.get("Entered Amount").toString();	
 	 	 	    	    						childMap.put("Credit Entered Amount", Double.parseDouble(enteredAmount));
 	 	 	 	    	    				}
 	 	 	    	    					if(mp.get("Accounted Amount") != null)
 	 	 	 	    	    				{
 	 	 	    	    						String accountedAmount = mp.get("Accounted Amount").toString();
 	 	 	    	    						childMap.put("Credit Accounted Amount", Double.parseDouble(accountedAmount));
 	 	 	 	    	    				}
 	 	 	    	    					childMap.put("Debit Entered Amount", "");
 	 	 	    	    					childMap.put("Debit Accounted Amount", "");
 	 	 	    	    				}
 	 	 	    	    				else if("DEBIT".equalsIgnoreCase(mp.get("line_type").toString()))
 	 	 	    	    				{
 	 	 	    	    					mp.put("Debit Account", codeCombination);
 	 	 	    	    					childMap.put("Debit Account", codeCombination);
 	 	 	    	    					childMap.put("Credit Account", "");
 	 	 	    	    					if(mp.get("Entered Amount") != null)
 	 	 	 	    	    				{
 	 	 	    	    						String enteredAmount = mp.get("Entered Amount").toString();	
 	 	 	    	    						childMap.put("Debit Entered Amount", Double.parseDouble(enteredAmount));
 	 	 	 	    	    				}
 	 	 	    	    					if(mp.get("Accounted Amount") != null)
 	 	 	 	    	    				{
 	 	 	    	    						String accountedAmount = mp.get("Accounted Amount").toString();
 	 	 	    	    						childMap.put("Debit Accounted Amount", accountedAmount);
 	 	 	 	    	    				}
 	 	 	    	    					childMap.put("Credit Entered Amount", "");
 	 	 	    	    					childMap.put("Credit Accounted Amount", "");
 	 	 	    	    				} 	 	    	    					
 	 	    	    				}
 	 	    	    				if(mp.get("amount_col_id") != null)
 	 	    	    				{
 	 	    	    					childMap.put("amount_col_id", mp.get("amount_col_id").toString());
 	 	    	    				}
 	 	    	    				if(mp.get("Line Type Detail") != null)
 	 	    	    				{
 	 	    	    					childMap.put("Line Type Detail", mp.get("Line Type Detail").toString());
 	 	    	    				}
 	 	    	    				if(mp.get("Entered Currency") != null)
 	 	    	    				{
 	 	    	    					childMap.put("Entered Currency", mp.get("Entered Currency").toString());
 	 	    	    				}
 	 	    	    				if(mp.get("Accounted Currency") != null)
 	 	    	    				{
 	 	    	    					childMap.put("Accounted Currency", mp.get("Accounted Currency").toString());
 	 	    	    				}
 	 	    	    				else
 	 	    	    				{
 	 	    	    					childMap.put("Accounted Currency", "");
 	 	    	    				}
 	 	    	    				children.add(mp);
 	 	    	    				childrenList.add(childMap);
 	 	    	    			}

 	 	    	    			HashMap mainMap = (HashMap) children.get(0).clone();
 	 	    	    			mainMap.remove("accounting_ref_1");
 	 	    	    			mainMap.remove("accounting_ref_2");
 	 	    	    			mainMap.remove("accounting_ref_3");
 	 	    	    			mainMap.remove("accounting_ref_4");
 	 	    	    			mainMap.remove("accounting_ref_5");
 	 	    	    			mainMap.remove("accounting_ref_6");
 	 	    	    			mainMap.remove("accounting_ref_7");
 	 	    	    			mainMap.remove("accounting_ref_8");
 	 	    	    			mainMap.remove("accounting_ref_9");
 	 	    	    			mainMap.remove("accounting_ref_10");
 	 	    	    			mainMap.put("Debit Account", "");
 	 	    	    			mainMap.put("Credit Account", "");
 	 	    	    			mainMap.put("Entered Amount", "");
 	 	    	    			mainMap.put("Accounted Amount", "");
 	 	    	    			if(acctdStatus.contains("REVERSED"))
 	 	    	    			{
 	 	    	    				mainMap.put("Accounted Status", "Reversed");
 	 	    	    			}
 	 	    	    			else
 	 	    	    			{
 	 	    	    				mainMap.put("Accounted Status", "Accounted");
 	 	    	    			}
 	 	    	    			mainMap.put("children", childrenList);
 	 	    	    			finalList.add(mainMap);
 	 	    	    		}
 	 	    	    	}
 	 	    	    }
 	    	    }
			}
			catch(Exception e)
			{
				log.info("Exception while getting databse properties: "+ e);
			}
			finally{
				if(result != null)
					result.close();
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
			}
			return finalList;
		}
		
		public String getSegmentsString(int segmentsLength)
		{
			String segmentsString = "";
			if(segmentsLength>0)
			{
				for(int i=0; i<segmentsLength; i++)
				{
					if(i == segmentsLength-1)
					{
						segmentsString = segmentsString + "accounting_ref_"+i+"";
					}
					else
					{
						segmentsString = segmentsString + " accounting_ref_"+i+", ";						
					}
				}
			}
			System.out.println("Segments: "+segmentsString);
			return segmentsString;
		}
		
	    public String getColumnNamesAsString(Long dataViewId, String searchWord)
	    {
	    	String columnString = "";
	    	List<String> colNames = new ArrayList<String>();
	    	List<DataViewsColumns> dvcs = dataViewsColumnsRepository.findByDataViewId(dataViewId);
	    	if(dvcs.size()>0)
	    	{
	    		for(DataViewsColumns dvc : dvcs)
	    		{
					if("File Template".equalsIgnoreCase(dvc.getRefDvType()))
					{
						FileTemplateLines ftl = fileTemplateLinesRepository.findOne(Long.parseLong(dvc.getRefDvColumn().toString()));
						if(ftl != null)
						{
							colNames.add(ftl.getColumnAlias());
						}
					}
					else if("Data View".equalsIgnoreCase(dvc.getRefDvType()) || dvc.getRefDvType() == null)
					{
						colNames.add(dvc.getColumnName());
					}
	    		}
	    		for(int i=0; i< colNames.size(); i++)
	    		{
	    		/*	if(i == colNames.size()-1)
	    			{
	    				columnString = columnString + " COALESCE(`"+colNames.get(i)+"`, '')";
	    			}
	    			else
	    			{*/
	    				columnString = columnString + " COALESCE(`"+colNames.get(i)+"`, ''), ";
	    			/*}*/
	    		}
	    		columnString = columnString +" COALESCE(`accounting_status`, ''), COALESCE(`source_ref`, ''), COALESCE(`category_ref`, ''), COALESCE(`ledger_name`, ''), COALESCE(`coa_name`, ''), COALESCE(`rule_code`, '') ";
	    		
	    		columnString = " AND CONCAT("+columnString +") LIKE '%"+searchWord+"%'";
	    	}
	    	return columnString;
	    }
	    
	    public List<HashMap> getDatesSummaryWithFormat(List<HashMap> dateMaps)
	    {
	    	List<HashMap> finalList = new ArrayList<HashMap>();
	    	try{
		    	for(HashMap map : dateMaps)
		    	{
		    		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					Date date = df.parse(map.get("name").toString());
					df = new SimpleDateFormat("dd-MMM-yy");
					map.put("name", df.format(date));
					finalList.add(map);
		    	}
	    	}
	    	catch(Exception e)
	    	{
	    		log.info("Date format Exception: "+e);
	    	}

	    	return finalList;
	    }
	    
	    // Updating counts and amounts in t_app_module_summary table
	    public HashMap updateCountsNAmountsInAppModuleSummary(Long viewId, String amountQualifier, Long groupId, String status) throws SQLException
	    {
	    	HashMap finalMap = new HashMap();
	    	Connection conn = null;
	    	Statement stmt = null;
	    	ResultSet result = null; 
	    	try{
				DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
				conn = ds.getConnection();
	    		stmt = conn.createStatement();
	    		DataViews dv=dataViewsRepository.findOne(viewId.longValue());
	    		String countAmntsQuery = "select Date(det.`fileDate`), count(*), det.rule_id, SUM(`"+amountQualifier+"`) from  "
	    			+ " (select dv.*,   "
	    			+ "	(case when acd.ACCOUNTING_STATUS is null      "
	    			+ "		THEN        "
	    			+ "			'UN ACCOUNTED'   "
	    			+ "		ELSE   "
	    			+ "        acd.ACCOUNTING_STATUS  END) as accounting_status,  "
	    			+ "		rule_code,  "
	    			+ "     rl.id rule_id,  "
	    			+ "     job_reference  "
	    			+ "     from (select * from `"+dv.getDataViewName().toLowerCase()+"`) dv "
	    			+ "     LEFT JOIN (SELECT distinct su.STATUS ACCOUNTING_STATUS, "
	    			+ "                    de.acct_rule_id,"
	    			+ "                    de.original_row_id,  "
	    			+ "                    de.job_reference"
	    			+ "                    FROM t_accounted_summary su, "
	    			+ "		t_accounting_data de WHERE su.id = de.accounted_summary_id AND su.rule_group_id = "+groupId+" and su.view_id = "+viewId+" and su.current_record_flag is true) acd "
	    			+ "        ON dv.scrIds = acd.original_row_id  	   "
	    			+ "        LEFT JOIN t_rules rl ON acd.acct_rule_id = rl.id ) det "
	    			+ "        where accounting_status = '"+status+"'  group by Date(det.`fileDate`), det.rule_id order by Date(det.`fileDate`) desc";
	    		
	    		   result=stmt.executeQuery(countAmntsQuery);
	        	   while(result.next()){
	        		  String date = result.getString(1);
	        		  Long count = Long.parseLong(result.getString(2));
	        		  Long ruleId = Long.parseLong(result.getString(3));
	        		  BigDecimal amount = new BigDecimal(result.getString(4));
	        		  LocalDate localDate = LocalDate.parse(date);
	        		  AppModuleSummary checkRecord = appModuleSummaryRepository.findByFileDateAndModuleAndRuleGroupIdAndRuleIdAndViewIdAndType(localDate, "Accounting", groupId, ruleId, viewId, status);
	        		  if(checkRecord != null)
	        		  {	// Updating Record
	        			  checkRecord.setTypeCount(count);
	        			  checkRecord.setTypeAmount(amount);
	        			  checkRecord.setLastUpdatedDate(ZonedDateTime.now());
	        			  AppModuleSummary updateRecord = appModuleSummaryRepository.save(checkRecord);
	        			  log.info("Updated count and amounts for the rule group id: "+ groupId+", viweId: "+viewId+", ruleId: "+ ruleId+", module: Accounting, Type: Accounted");
	        		  }
	        		  else
	        		  {
	        			  // Insert New Record
	        		  }
	        	   }
	    	}catch(Exception e)
	    	{
	    		log.info("Exception while updating count and amounts in app_module_summary table.");
	    	}
	  		finally
	  		{
		   		if(result != null)
		   			result.close();	
		   		if(stmt != null)
		   			stmt.close();
				if(conn != null)
					conn.close();
	  		}
	    	return finalMap;
	    }
	    
	    public HashMap updateAppModuleSummaryInfoACC(Long groupId, Long viewId, String amountQualifier, Long userId, Long tenantId, String periodFactor) throws SQLException, ClassNotFoundException
	    {
	    	log.info("Updating accounting count and amounts in app_module_summary for group id: "+ groupId+", view id: "+ viewId);
	    	String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	    	HashMap finalMap = new HashMap();
	    	Connection conn = null;
	    	
	    	PreparedStatement appModSumIdsStmt = null;
	    	ResultSet appModSumIdsRS = null;
	    	
	    	PreparedStatement fetchCountAmntsStmt = null;
	    	ResultSet countsAmountsRS = null;
	    	
		  	Statement dvStmt = null;
		  	ResultSet dvRS = null;	  	
	    	
	    	PreparedStatement checkRecordStmt = null;
	    	ResultSet checkRecordRS = null;
	    	PreparedStatement preparedStatement = null;
	    	
	    	Statement updateExistedRecordStmt = null;
	    	
	    	Statement multiCurrencyStmt = null;
	    	ResultSet multiCurrencyRS = null;
	    	
	    	List<Long> appSummaryIds = new ArrayList<Long>();
	    	try
	    	{
				DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
				conn = ds.getConnection();
		    	// Fetching app_module_summary ids based on status
		    	String appModSumIdsQuery = "select id from t_app_module_summary where rule_group_id = "+groupId+" and view_id = "+viewId+" and module = 'ACCOUNTING'";
	    		appModSumIdsStmt = conn.prepareStatement(appModSumIdsQuery);
	    		appModSumIdsRS = appModSumIdsStmt.executeQuery();
	    		
		    	DataViews dv=dataViewsRepository.findOne(viewId.longValue());
		    	
    		    while(appModSumIdsRS.next())
    		    {
    		    	appSummaryIds.add(appModSumIdsRS.getLong(1));
    		    }
	    		
	  			String dvQuery = "select Date(`"+periodFactor+"`), count(*), sum(`"+amountQualifier+"`) from `"+dv.getDataViewName().toLowerCase()+"` group by Date(`"+periodFactor+"`)";
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
	    		// Fetching count and amounts Query
	    		String countAmntsQuery = "select Date(det.`"+periodFactor+"`), accounting_status, count(*), det.rule_id, SUM(`"+amountQualifier+"`) from ("
	    				+ " select dv.*, "
	    				+ " 	(case when acd.ACCOUNTING_STATUS is null THEN 'UN_ACCOUNTED'"
	    				+ " 	when acd.ACCOUNTING_STATUS is not null and acd.journal_status is not null THEN 'JOURNALS_ENTERED'"
	    				+ "     ELSE acd.ACCOUNTING_STATUS END) as accounting_status,"
	    				+ " 	rule_code, "
	    				+ " 	(case when acd.rule_id = 0"
	    				+ "     then 0 else rl.id end) as rule_id,job_reference from"
	    				+ "     (select * from `"+dv.getDataViewName().toLowerCase()+"`) dv"
	    				+ "     LEFT JOIN (SELECT"
	    				+ " 	distinct su.STATUS ACCOUNTING_STATUS,"
	    				+ "     su.rule_id, su.journal_status, de.acct_rule_id, de.original_row_id, de.job_reference"
	    				+ " 	FROM t_accounted_summary su, t_accounting_data de"
	    				+ "     WHERE su.id = de.accounted_summary_id AND su.rule_group_id = "+groupId+" and su.view_id = "+viewId+" and su.current_record_flag is true) acd "
	    				+ "     ON dv.scrIds = acd.original_row_id"
	    				+ "     LEFT JOIN t_rules rl ON acd.acct_rule_id = rl.id) det"
	    				+ "     group by Date(det.`"+periodFactor+"`), accounting_status, det.rule_id";
	    		
	    		log.info("Count Amouns Fetching Query: "+countAmntsQuery);
    			fetchCountAmntsStmt = conn.prepareStatement(countAmntsQuery);
    			countsAmountsRS = fetchCountAmntsStmt.executeQuery();
    			// Iterating counts and amounts
	    		String insertTableSQL = "INSERT INTO t_app_module_summary"
	    				+ "(file_date, module, rule_group_id, rule_id, view_id, type, type_count, created_date, last_updated_date, type_amt, dv_count, dv_amt) VALUES"
	    				+ "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	    		preparedStatement = conn.prepareStatement(insertTableSQL);
	    		
	    		updateExistedRecordStmt = conn.createStatement();
	    		
    		    while(countsAmountsRS.next())
    		    {
    		    	String date = countsAmountsRS.getString(1).toString();
    		    	String status = "";
    		    	status = countsAmountsRS.getString(2).toString();
    		    	
		    		HashMap getDVMap = (HashMap) dvMap.get(date);
		    		Long dvCount = Long.parseLong(getDVMap.get("count").toString());
		    		BigDecimal dvAmount = new BigDecimal(getDVMap.get("amount").toString());
    		    	Long count = Long.parseLong(countsAmountsRS.getString(3).toString());
    		    	Long ruleId = null;
    		    	if(countsAmountsRS.getString(4) != null)
    		    	{
    		    		ruleId = Long.parseLong(countsAmountsRS.getString(4).toString());
    		    	}
    		    	Double amount = Double.parseDouble(countsAmountsRS.getString(5).toString());
    		    	String query = "";
    		    	if(ruleId != null)
    		    	{
    		    		query = "select * from t_app_module_summary where file_date = '"+date+"' and module = 'ACCOUNTING' and rule_group_id = "+groupId+" and rule_id = "+ruleId+" and view_id = "+viewId+" and type = '"+status+"'";
    		    	}
    		    	else
    		    	{
    		    		query = "select * from t_app_module_summary where file_date = '"+date+"' and module = 'ACCOUNTING' and rule_group_id = "+groupId+" and rule_id is null and view_id = "+viewId+" and type = '"+status+"'";
    		    	}
    		    	
    		    	checkRecordStmt = conn.prepareStatement(query);
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
    		    		if(size == 1)
    		    		{
    		    			// Updating existing record
    		    			String updateQuery =  "update t_app_module_summary set type_count = "+count+", type_amt = "+amount+", dv_count = "+dvCount+", dv_amt = "+dvAmount+", last_updated_date = '"+timeStamp+"', last_updated_by = "+userId+" where id = "+id;
    		    			// updateExistedRecordStmt.executeUpdate(updateQuery);
    		    			updateExistedRecordStmt.addBatch(updateQuery);
    		    			appSummaryIds.remove(id);	// Removing updated Record
    		    		}
    		    	}
    		    	else
    		    	{
    		    		// log.info("Need to insert a record once data transmission has been completed for group id: "+ groupId+", viewId: "+ viewId+", date: "+ date+", ruleId: "+ ruleId+", status: "+ status+", module: ACCOUNTING");
    				    preparedStatement.setDate(1, java.sql.Date.valueOf(date));
    				    preparedStatement.setString(2, "ACCOUNTING");
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
    				    preparedStatement.setString(6, status);
    				    preparedStatement.setLong(7, count);
    				    preparedStatement.setTimestamp(8, new java.sql.Timestamp(System.currentTimeMillis()));
    				   	preparedStatement.setTimestamp(9, new java.sql.Timestamp(System.currentTimeMillis()));
    				  	preparedStatement.setBigDecimal(10, BigDecimal.valueOf(amount));
    				  	preparedStatement.setLong(11, dvCount);
    				  	preparedStatement.setBigDecimal(12, dvAmount);
    				  	preparedStatement.addBatch();
    			 		// execute insert SQL stetement
    		    		// preparedStatement .executeUpdate();
    		    	}
    		    }
    		    preparedStatement.executeBatch();
    		    updateExistedRecordStmt.executeBatch();
    		    /*updateExistedRecordStmt = conn.createStatement();*/
    		    if(appSummaryIds.size()>0)
    		    {
    		    	for(Long id : appSummaryIds)
    		    	{
    		    		String dvCountAmtQuery = "select * from t_app_module_summary where id = "+id;
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
    		  			if(getDVMap != null)
    		  			{
        		  			dvCount = Long.parseLong(getDVMap.get("count").toString());
        		  			dvAmount = new BigDecimal(getDVMap.get("amount").toString());
    		  			}
    		    		String updateQuery = "update t_app_module_summary set type_count = 0, type_amt = 0.0, dv_count = "+dvCount+", dv_amt = "+dvAmount+", last_updated_date = '"+timeStamp+"', last_updated_by = "+userId+" where id = "+id;
    		    		// updateExistedRecordStmt = conn.createStatement();
    		    		// updateExistedRecordStmt.executeUpdate(updateQuery);
    		    		updateExistedRecordStmt.addBatch(updateQuery);
    		    	}
    		    }
    		    updateExistedRecordStmt.executeBatch();
	    	}
	    	catch(Exception e)
	    	{
	    		log.info("Exception while updating count and amounts app module summary table: "+e);
	    	}
	    	finally
	    	{
	    		// ResultSets
	    		if(appModSumIdsRS != null)
	    			appModSumIdsRS.close();
	    		if(countsAmountsRS != null)
	    			countsAmountsRS.close();
	    		if(checkRecordRS != null)
	    			checkRecordRS.close();
	    		if(dvRS != null)
	    			dvRS.close();
	    		if(multiCurrencyRS != null)
	    			multiCurrencyRS.close();
	    		// Statements
	   	        if(appModSumIdsStmt != null)
	   	        	appModSumIdsStmt.close();
	   	        if(fetchCountAmntsStmt != null)
	   	        	fetchCountAmntsStmt.close();
	   	        if(checkRecordStmt != null)
	   	        	checkRecordStmt.close();
	   	        if(updateExistedRecordStmt != null)
	   	        	updateExistedRecordStmt.close();
	   	        if(preparedStatement != null)
	   	        	preparedStatement.close();
	   	        if(dvStmt != null)
	   	        	dvStmt.close();
	   	        if(multiCurrencyStmt != null)
	   	        	multiCurrencyStmt.close();
	   	        // Connection
	   	        if(conn != null)
	   				conn.close();
	    	}
	    	return finalMap;
	    }
	    
		
		public HashMap updateJournalsCountNAmounts(Long viewId, String amountQualifier, String idsString, String status, Long userId)
		{
			HashMap finalMap = new HashMap();
    		Connection conn = null;
    		Statement stmt = null;
    		ResultSet countsAmountsRS = null;
    		Statement updateStament = null;
    		PreparedStatement checkRecord = null;
		    ResultSet checkRecordRS = null;
		    PreparedStatement fetchCountAmntsStmt = null;
    		DataViews dv=dataViewsRepository.findOne(viewId.longValue());
    		try{
				DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
				conn = ds.getConnection();
	    		stmt = conn.createStatement();
	    		String countAmntsQuery = " select Date(det.`fileDate`), count(*), det.rule_id, det.journal_status, det.rule_group_id, det.view_id, SUM(`"+amountQualifier+"`) from "
	    				+ "	(select dv.*,	"
	    				+ "	(case when acd.ACCOUNTING_STATUS is null "
	    				+ "	THEN "
	    				+ "		'Not Accounted' "
	    				+ "	ELSE "
	    				+ "	acd.ACCOUNTING_STATUS  END) as accounting_status, "
	    				+ "	(case when acd.rule_id = 0 "
	    				+ "	then "
	    				+ "		0 "
	    				+ "	else "
	    				+ "     rl.id end ) as rule_id, "
	    				+ "	job_reference,"
	    				+ " journal_status,"
	    				+ " rule_group_id,"
	    				+ " view_id"
	    				+ "	from (select * from `"+dv.getDataViewName().toLowerCase()+"`) dv   "
	    				+ "	LEFT JOIN (SELECT distinct su.STATUS ACCOUNTING_STATUS, su.rule_id, su.journal_status,su.rule_group_id,su.view_id,"
	    				+ "	de.acct_rule_id,	"
	    				+ "	de.original_row_id, "
	    				+ "	de.job_reference FROM t_accounted_summary su, t_accounting_data de "
	    				+ "	WHERE su.id = de.accounted_summary_id AND su.row_id in("+idsString+")) acd "
	    				+ "	ON dv.scrIds = acd.original_row_id  	"
	    				+ "	LEFT JOIN t_rules rl ON acd.acct_rule_id = rl.id) det  group by Date(det.`fileDate`), det.rule_id, det.journal_status, det.rule_group_id,det.view_id";
	    		
	    		log.info("Count Amounts Fetching Query: "+countAmntsQuery);
	    		String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	    		fetchCountAmntsStmt = conn.prepareStatement(countAmntsQuery);
	    		countsAmountsRS = fetchCountAmntsStmt.executeQuery();
	    		while(countsAmountsRS.next())
	    		{
	    	    	String date = countsAmountsRS.getString(1).toString();
	    	    	Long count = Long.parseLong(countsAmountsRS.getString(2).toString());
	   		    	Long ruleId = null;
	   		    	if(countsAmountsRS.getString(3) != null  && countsAmountsRS.getString(4) != null)
	   		    	{
	   		    		ruleId = Long.parseLong(countsAmountsRS.getString(3).toString());
		    		    Double amount = Double.parseDouble(countsAmountsRS.getString(7).toString());
		    		    Long groupId = Long.parseLong(countsAmountsRS.getString(5));
		    		    
		    		    log.info("date: "+ date+", count: "+ count+", ruleId: "+ ruleId+", Amount: "+ amount);
		    		    String query = "";
	    		    	log.info("Not null Query: select * from t_app_module_summary where file_date = '"+date+"' and module = 'Accounting' and rule_group_id = "+groupId+" and rule_id = "+ruleId+" and view_id = "+viewId+" and type = '"+status+"'");
	    		    	query = "select * from t_app_module_summary where file_date = '"+date+"' and module = 'Accounting' and rule_group_id = "+groupId+" and rule_id = "+ruleId+" and view_id = "+viewId+" and type = '"+status+"'";

	    		    	checkRecord = conn.prepareStatement(query);
		    		    checkRecordRS = checkRecord.executeQuery();
		    		    int size = 0;
		    		    while(checkRecordRS.next())
		    		    {
		    		    	size = size + 1;
		    		    }
		    		    log.info("size: "+size);
		    		    if(size > 0)
		    		    {
		    		    	if(size == 1)
		    		    	{
		    		    		// Updating record
		    		    		log.info("Updating count and amounts in app_module_summary table");
		    		    		String updateQuery =  "";
		    		    		updateQuery = "update t_app_module_summary set type_count = "+count+", type_amt = "+amount+", last_updated_date = '"+timeStamp+"', last_updated_by = "+userId+" where file_date = '"+date+"' and module = 'Accounting' and type = '"+status+"' and rule_group_id = "+groupId+" and rule_id = "+ruleId+" and view_id = "+viewId;	
		    		    		updateStament = conn.createStatement();
		    		    		updateStament.executeUpdate(updateQuery);
		    		    		log.info("Updated counts and amounts for date "+date+", group id: "+ groupId+ ", view id: "+ viewId+", rule id: "+ ruleId);
		    		    	}
		    		    	else
		    		    	{
		    		    		log.info("Duplicate records exist while updating journal count and amounts for the group id: "+ groupId+", viewId: "+ viewId+", date: "+ date+", ruleId: "+ ruleId+", status: "+ status+", module: Accounting");
		    		    	}
		    		    }
		    		    else
		    		    {
		    		    	log.info("Need to insert a record, when data tranformation is completed.");
		    		    }
	   		    	}	
	    		}
    		}
	    	catch(Exception e)
	    	{
	    		System.out.println("Exception while updating counts and amounts in app_module_summary table: "+e);
	    	}
	    	finally
	    	{
	    		try
	    		{
	    			if(checkRecordRS != null)
	    				checkRecordRS.close();
	    			if(countsAmountsRS != null)
	    				countsAmountsRS.close();
	    			if(fetchCountAmntsStmt != null)
	    				fetchCountAmntsStmt.close();
	    			if(stmt!=null)
	    					stmt.close();
	    			if(updateStament != null)
	    					updateStament.close();
	    			if(checkRecord != null)
	    				checkRecord.close();
	    	        if(conn!=null)
	    					conn.close();
	    		}
	    		catch(Exception e)
	    		{
	    			log.info("Exception while closing jdbc statements: "+e);
	    		}
    		}
			return finalMap;
		}
		
		public HashMap accountedSummaryForActivity(String viewName, String dateQualifier, String rangeFrom, String rangeTo, Long groupId, Long viewId, String whereString, String status, String srcOrTrgt, String periodFactor,
				Long pageNumber, Long pageSize) throws SQLException
		{
			HashMap finalMap = new HashMap();
			List<HashMap> finalList = new ArrayList<HashMap>();
			NumberFormat numFormat = NumberFormat.getInstance();
			
			Properties props = propertiesUtilService.getPropertiesFromClasspath("File.properties");
			String currencyFormat = props.getProperty("currencyFormat");
			
			Connection conn = null;
			Statement stmt = null;
			ResultSet result = null;
			
			Statement totalCountStmt = null;
			ResultSet totalCountRS = null;
			
			Long totalCount = 0L;
			try{
				
				DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
				conn = ds.getConnection();
				
				log.info("Connected to JDBC Successfully");
				stmt = conn.createStatement();
				totalCountStmt = conn.createStatement();
				
				String srcOrTrgtQuery = "";
				if("source".equalsIgnoreCase(srcOrTrgt))
				{
					srcOrTrgtQuery = " LEFT JOIN (select recon_reference, original_row_id from t_reconciliation_result where original_view_id = "+viewId+" and recon_status = 'RECONCILED'"
							+ " and current_record_flag is true) recon"
							+ " ON dv.scrIds = recon.original_row_id ";
				}
				else if("target".equalsIgnoreCase(srcOrTrgt))
				{
					srcOrTrgtQuery = " LEFT JOIN (select recon_reference, target_row_id from t_reconciliation_result where target_view_id = "+viewId+" and recon_status = 'RECONCILED'"
							+ " and current_record_flag is true) recon"
							+ " ON dv.scrIds = recon.target_row_id ";
				}
				String dateRangeQuery = "";
				if(periodFactor != null)
				{
					dateRangeQuery = " WHERE  Date(`"+dateQualifier+"`) BETWEEN '"+rangeFrom+"' AND '"+rangeTo+"'";
				}
				
				String query = "SELECT ledger_name, "
						+ "       entered_currency, "
						+ "       source_meaning, "
						+ "       category_meaning, "
						+ "       accounted_currency, "
						+ "       Sum(accounted_amount) accounted_amount, "
						+ "       Sum(entered_amount)   entered_amount, "
						+ "       Count(distinct det.scrIds) count,        sum(JE_COUNT) je_count, "
						+ "       sum(reveresed_count) reverse_count"
						+ " FROM   (SELECT dv.*, "
						+ "			( CASE "
						+ "			WHEN acd.accounting_status IS NULL "
						+ "			AND recon.recon_reference IS NULL THEN "
						+ "			'UN ACCOUNTED, NOT RECONCILED' "
						+ "			WHEN acd.accounting_status IS NULL "
						+ "			AND recon.recon_reference IS NOT NULL THEN "
						+ "			'UN ACCOUNTED, RECONCILED' "
						+ "			WHEN acd.accounting_status IS NOT NULL "
						+ "			AND recon.recon_reference IS NULL THEN "
						+ "			'ACCOUNTED, NOT RECONCILED' "
						+ "			WHEN acd.accounting_status IS NOT NULL "
						+ "			AND recon.recon_reference IS NOT NULL THEN "
						+ "			'ACCOUNTED, RECONCILED' "
						+ "			end) AS accounting_recon_status,  "
						+ "         (case when journal_status = 'ENTERED' THEN 1 ELSE 0 END ) AS JE_COUNT,"
						+ "         	reveresed_count, "
						+ "				appStatusLum.meaning, final_status, "
						+ "     		final_action_date, "
						+ "             rl2.rule_code        approval_rule_code, "
						+ "             source_ref, "
						+ "             currency_ref, "
						+ "             coa_ref, "
						+ "             ledger_ref, "
						+ "             category_ref, "
						+ "             led.name             ledger_name, "
						+ "             coa.name             coa_name, "
						+ "             srclum.meaning       source_meaning, "
						+ "             catlum.meaning       category_meaning, "
						+ "             entered_currency, "
						+ "             accounted_currency, "
						+ "             accounted_amount, "
						+ "             acd.amount           entered_amount, job_reference, "
						+ "             CASE "
						+ "                 WHEN acct_rule_id = 0 THEN 'Manual' "
						+ "                 ELSE rl.rule_code "
						+ "               end                  rule_code, "
						+ "               rl.id                rule_id, "
						+ "               amount_col_id  "
						+ "	FROM   (SELECT * "
						+ "                FROM   `"+viewName+"` "
						+ "             "+dateRangeQuery+"   ) dv "
						+ "                LEFT JOIN (SELECT su.status "
						+ "                ACCOUNTING_STATUS, su.journal_status, "
						+ "               de.ledger_ref, "
						+ "               de.coa_ref, "
						+ "               de.acct_rule_id, "
						+ "               de.original_row_id, de.job_reference,"
						+ "               de.source_ref, IF(de.status = 'REVERSED',1,0) reveresed_count, "
						+ "               sum(IF(de.status = 'REVERSED', amount*-1,amount)) amount,"
						+ "               de.approval_rule_id, "
						+ "               Date(de.final_action_date) final_action_date, "
						+ "               de.final_status, "
						+ "               sum(IF(de.status = 'REVERSED', accounted_amount*-1,accounted_amount)) accounted_amount,"
						+ "               de.ledger_currency "
						+ "			      accounted_currency,"
						+ "				  de.currency_ref entered_currency, "
						+ "               de.category_ref, "
						+ "               de.tenant_id, "
						+ "               de.currency_ref, "
						+ "               de.amount_col_id"
						+ "  FROM   t_accounted_summary su, t_accounting_data de "
						+ "             WHERE  su.id = de.accounted_summary_id "
						+ "					AND su.rule_group_id = "+groupId+" "
						+ "					AND su.view_id = "+viewId+" "
						+ "					AND line_type = 'DEBIT' "
						+ "					AND su.current_record_flag IS TRUE "
						+ "					group by su.status, ACCOUNTING_STATUS, su.journal_status, de.ledger_ref, de.coa_ref, de.acct_rule_id, de.original_row_id, de.source_ref, "
						+ " IF(de.status = 'REVERSED',1,0), "
						+ "						de.approval_rule_id, Date(de.final_action_date), de.final_status, de.ledger_currency, de.currency_ref, de.category_ref, "
						+ "						de.tenant_id, de.currency_ref, de.amount_col_id, de.job_reference"
						+ "					) acd ON dv.scrids = acd.original_row_id and acd.acct_rule_id is not null "
						+ " " + srcOrTrgtQuery + " "				
						+ "        LEFT JOIN look_up_code srclum "
						+ "        ON acd.source_ref = srclum.look_up_code  and acd.tenant_id = srclum.tenant_id and srclum.look_up_type = 'SOURCE'	   "
						+ "        LEFT JOIN look_up_code catlum "
						+ "        ON acd.category_ref = catlum.look_up_code  and acd.tenant_id = catlum.tenant_id and catlum.look_up_type = 'CATEGORY'   "
						+ " 	   LEFT JOIN look_up_code appStatusLum ON acd.final_status = appStatusLum.look_up_code and acd.tenant_id = appStatusLum.tenant_id and appStatusLum.look_up_type = 'APPROVAL_STATUS'"
						+ "        LEFT JOIN t_ledger_definition led ON acd.ledger_ref = led.id    "
						+ "        LEFT JOIN t_chart_of_account coa ON acd.coa_ref = coa.id   "
						+ "        LEFT JOIN t_rules rl ON acd.acct_rule_id = rl.id"
						+ " 	   LEFT JOIN t_rules rl2 ON acd.approval_rule_id = rl2.id) det "
						+ "        where accounting_recon_status = '"+status+"' "+whereString+""
						+ "        group by ledger_name, entered_currency, source_meaning, category_meaning, accounted_currency";
				
				log.info("Grouping Query: "+query);
				String paginationQuery = query + " LIMIT "+pageNumber+", "+ pageSize;
				String totalCountQuery = "SELECT COUNT(*) COUT FROM ("+query+") count";
				result=stmt.executeQuery(paginationQuery);
				totalCountRS = totalCountStmt.executeQuery(totalCountQuery);
				while(totalCountRS.next())
				{
					totalCount = totalCount + Long.parseLong(totalCountRS.getString(1));
				}
	        	while(result.next()){
	        		HashMap summaryMap = new HashMap();
	        		if(result.getString(1) != null)
	        		{
		        		summaryMap.put("ledger_name", result.getString("ledger_name"));
	        		}
	        		else
	        		{
		        		summaryMap.put("Ledger Name", "");
	        		}
	        		summaryMap.put("entered_currency", result.getString("entered_currency"));
	        		summaryMap.put("source_meaning", result.getString("source_meaning"));
	        		summaryMap.put("category_meaning", result.getString("category_meaning"));
	        		if(result.getString(5) != null)
	        		{
	        			summaryMap.put("accounted_currency", result.getString("accounted_currency"));
	        		}
	        		else
	        		{
	        			summaryMap.put("accounted_currency", "");
	        		}
	        		summaryMap.put("accounted_amount", result.getString("accounted_amount"));
	        		summaryMap.put("entered_amount", result.getString("entered_amount"));
	        		summaryMap.put("count", Long.parseLong(result.getString("count")));
	        		summaryMap.put("je_count", Long.parseLong(result.getString("je_count")));
	        		summaryMap.put("reverse_count", Long.parseLong(result.getString("reverse_count")));
	        	
	        		finalList.add(summaryMap);
	        	}
			}
			catch(Exception e)
			{
				log.info("Exception while getting databse properties: "+ e);
			}
			finally{
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
			finalMap.put("summary", finalList);
			finalMap.put("totalCount", totalCount);
			log.info("Accounted Summary Info Size: "+finalList.size());
			return finalMap;
		}
		
		public HashMap accountedSummaryForNonActivity(String viewName, String dateQualifier, String rangeFrom, String rangeTo, Long groupId, Long viewId, String whereString, String status, String periodFacotr, 
				Long pageNumber, Long pageSize) throws SQLException
		{
			HashMap finalMap = new HashMap();
			List<HashMap> finalList = new ArrayList<HashMap>();
			NumberFormat numFormat = NumberFormat.getInstance();
			
			Properties props = propertiesUtilService.getPropertiesFromClasspath("File.properties");
			String currencyFormat = props.getProperty("currencyFormat");
			
			Connection conn = null;
			Statement stmt = null;
			ResultSet result = null; 
			
			Statement totalCountStmt = null;
			ResultSet totalCountRS = null; 
			
			String dateRangeQuery = "";
			Long totalCount = 0L;
			if(periodFacotr != null)
			{
				dateRangeQuery = " WHERE  Date(`"+dateQualifier+"`) BETWEEN '"+rangeFrom+"' AND '"+rangeTo+"'";
			}
			try{
				
				DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
				conn = ds.getConnection();
				
				log.info("Connected to JDBC Successfully");
				stmt = conn.createStatement();
				totalCountStmt = conn.createStatement();
				String query = "SELECT ledger_name, "
						+ "       entered_currency, "
						+ "       source_meaning, "
						+ "       category_meaning, "
						+ "       accounted_currency, "
						+ "       Sum(accounted_amount) accounted_amount, "
						+ "       Sum(entered_amount)   entered_amount, "
						+ "       Count(distinct det.scrIds) count,        sum(JE_COUNT) je_count, "
						+ "		  sum(reveresed_count) reverse_count "
						+ " FROM   (SELECT dv.*, "
						+ "			(case when acd.ACCOUNTING_STATUS is null  "
						+ " THEN        	"
						+ "		'UN ACCOUNTED'   	"
						+ "	ELSE "
						+ "		acd.ACCOUNTING_STATUS  END) as accounting_status, "
						+ "		(case when journal_status = 'ENTERED' THEN 1 ELSE 0 END ) AS JE_COUNT, reveresed_count, "
						+ "				appStatusLum.meaning, final_status, "
						+ "     		final_action_date, "
						+ "             rl2.rule_code        approval_rule_code, "
						+ "             source_ref, "
						+ "             currency_ref, "
						+ "             coa_ref, "
						+ "             ledger_ref, "
						+ "             category_ref, "
						+ "             led.name             ledger_name, "
						+ "             coa.name             coa_name, "
						+ "             srclum.meaning       source_meaning, "
						+ "             catlum.meaning       category_meaning, "
						+ "             entered_currency, "
						+ "             accounted_currency, "
						+ "             accounted_amount, "
						+ "             acd.amount           entered_amount, job_reference, "
						+ "             CASE "
						+ "                 WHEN acct_rule_id = 0 THEN 'Manual' "
						+ "                 ELSE rl.rule_code "
						+ "               end                  rule_code, "
						+ "               rl.id                rule_id, "
						+ "               amount_col_id  "
						+ "	FROM   (SELECT * "
						+ "                FROM   `"+viewName+"` "
						+ "                "+dateRangeQuery+") dv "
						+ "               LEFT JOIN (SELECT su.status "
						+ "                ACCOUNTING_STATUS, su.journal_status, "
						+ "               de.ledger_ref, "
						+ "               de.coa_ref, "
						+ "               de.acct_rule_id, "
						+ "               de.original_row_id, "
						+ "               de.source_ref, IF(de.status = 'REVERSED',1,0) reveresed_count, "
						+ "               sum(IF(de.status = 'REVERSED', amount*-1,amount)) amount,"
						+ "               de.approval_rule_id, "
						+ "               Date(de.final_action_date) final_action_date, de.job_reference,"
						+ "               de.final_status, "
						+ "               sum(IF(de.status = 'REVERSED', accounted_amount*-1,accounted_amount)) accounted_amount,"
						+ "               de.ledger_currency "
						+ "			      accounted_currency,"
						+ "				  de.currency_ref entered_currency, "
						+ "               de.category_ref, "
						+ "               de.tenant_id, "
						+ "               de.currency_ref, "
						+ "               de.amount_col_id"
						+ "  FROM   t_accounted_summary su, t_accounting_data de "
						+ "             WHERE  su.id = de.accounted_summary_id "
						+ "					AND su.rule_group_id = "+groupId+" "
						+ "					AND su.view_id = "+viewId+" "
						+ "					AND line_type = 'DEBIT' "
						+ "					AND su.current_record_flag IS TRUE "
						+ "					group by su.status, ACCOUNTING_STATUS, su.journal_status, de.ledger_ref, de.coa_ref, de.acct_rule_id, de.original_row_id, de.source_ref, "
						+ "		IF(de.status = 'REVERSED',1,0), "
						+ "						de.approval_rule_id, Date(de.final_action_date), de.final_status, de.ledger_currency, de.currency_ref, de.category_ref, "
						+ "						de.tenant_id, de.currency_ref, de.amount_col_id, de.job_reference"
						+ "					) acd ON dv.scrids = acd.original_row_id and acd.acct_rule_id is not null "		
						+ "        LEFT JOIN look_up_code srclum "
						+ "        ON acd.source_ref = srclum.look_up_code  and acd.tenant_id = srclum.tenant_id and srclum.look_up_type = 'SOURCE'	   "
						+ "        LEFT JOIN look_up_code catlum "
						+ "        ON acd.category_ref = catlum.look_up_code  and acd.tenant_id = catlum.tenant_id and catlum.look_up_type = 'CATEGORY'   "
						+ " 	   LEFT JOIN look_up_code appStatusLum ON acd.final_status = appStatusLum.look_up_code and acd.tenant_id = appStatusLum.tenant_id and appStatusLum.look_up_type = 'APPROVAL_STATUS'"
						+ "        LEFT JOIN t_ledger_definition led ON acd.ledger_ref = led.id    "
						+ "        LEFT JOIN t_chart_of_account coa ON acd.coa_ref = coa.id   "
						+ "        LEFT JOIN t_rules rl ON acd.acct_rule_id = rl.id"
						+ " 	   LEFT JOIN t_rules rl2 ON acd.approval_rule_id = rl2.id) det "
						+ "        where accounting_status = '"+status+"' "+whereString+""
						+ "        group by ledger_name, entered_currency, source_meaning, category_meaning, accounted_currency";
				
				log.info("Grouping Query: "+query);
				String paginationQuery = query + " LIMIT "+pageNumber+", "+pageSize;
				String totalCountQuery = "SELECT COUNT(*) COUNT FROM ("+query+") count";
				result=stmt.executeQuery(paginationQuery);
				totalCountRS = totalCountStmt.executeQuery(totalCountQuery);
				while(totalCountRS.next())
				{
					totalCount = totalCount + Long.parseLong(totalCountRS.getString(1));
				}
	        	while(result.next()){
	        		HashMap summaryMap = new HashMap();	        	
	        		if(result.getString("ledger_name") != null)
	        			summaryMap.put("ledger_name", result.getString("ledger_name").toString());
	        		else
	        			summaryMap.put("ledger_name", "");
	        		if(result.getString("entered_currency") != null)
	        			summaryMap.put("entered_currency", result.getString("entered_currency").toString());
	        		else
	        			summaryMap.put("entered_currency", "");
	        		summaryMap.put("source_meaning", result.getString("source_meaning").toString());
	        		summaryMap.put("category_meaning", result.getString("category_meaning").toString());
	        		if(result.getString("accounted_currency") != null)
	        			summaryMap.put("accounted_currency", result.getString("accounted_currency").toString());
	        		else
	        			summaryMap.put("accounted_currency", "");
	        		if(result.getString("accounted_amount") != null)
	        			summaryMap.put("accounted_amount", result.getString("accounted_amount").toString());
	        		else
	        			summaryMap.put("accounted_amount", "");
	        		if(result.getString("entered_amount") != null)
	        			summaryMap.put("entered_amount", result.getString("entered_amount").toString());
	        		else
	        			summaryMap.put("entered_amount", "");
	        		if(result.getString("count") != null)
	        			summaryMap.put("count", Long.parseLong(result.getString("count").toString()));
	        		else
	        			summaryMap.put("count", "");
	        		if(result.getString("je_count") != null)
	        			summaryMap.put("je_count", Long.parseLong(result.getString("je_count")));
	        		else
	        			summaryMap.put("je_count", "");
	        		if(result.getString("reverse_count") != null)
	        			summaryMap.put("reverse_count", Long.parseLong(result.getString("reverse_count")));
	        		else
	        			summaryMap.put("reverse_count", "");
	        		finalList.add(summaryMap);
	        	}
			}
			catch(Exception e)
			{
				log.info("Exception while getting databse properties: "+ e);
			}
			finally{
				if(result != null)
					result.close();
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
			}			
			log.info("Accounted Summary Info Size: "+finalList.size());
			finalMap.put("summary", finalList);
			finalMap.put("totalCount", totalCount);
			return finalMap;
		}
		public HashMap getViewData(Long viewId, List<Long> rowIds, String singleOrMulti, String amountQualifier, String currencyCode, Long fxRateId, String conversionDate, String dateQualifier) throws SQLException, ClassNotFoundException
		{
			HashMap finalData = new HashMap();
			String idsString = "";
			for(int i=0; i<rowIds.size(); i++)
			{
				if(i == rowIds.size()-1)
				{
					idsString = idsString + rowIds.get(i);
				}
				else
				{
					idsString = idsString + rowIds.get(i)+", ";
				}
			}
			Connection conn = null;
			Statement stmt = null;
			ResultSet result = null; 
			DataViews dv = dataViewsRepository.findById(viewId.longValue());
			try{
				DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
				conn = ds.getConnection();
				log.info("Connected to JDBC Successfully");
				stmt = conn.createStatement();
				String query = "";
				if("single".equalsIgnoreCase(singleOrMulti))
				{
					query = "select * from `"+dv.getDataViewName().toLowerCase()+"` where scrIds in ("+idsString+")";
				}
				else if("multi".equalsIgnoreCase(singleOrMulti))
				{
					if("TRANSACTION_DATE".equalsIgnoreCase(conversionDate))
					{
						query = "select view.`"+amountQualifier+"`*view.rate as Accounted_amt, view.*  from (select "
								+ " (SELECT (CASE WHEN from_currency = '"+currencyCode+"'"
								+ "		then conversion_rate"
								+ "        WHEN to_currency = '"+currencyCode+"'"
								+ "        then inverse_rate END) as rate "
								+ "        from t_fx_rates_details where fx_rate_id = "+fxRateId+" and `"+dateQualifier+"` between from_date and to_date "
								+ "        and (from_currency = '"+currencyCode+"' or to_currency='"+currencyCode+"')) as rate, "
								+ " dv.* from  `"+dv.getDataViewName().toLowerCase()+"` dv where scrIds in ("+idsString+")"
								+ " ) view";
					}
					else
					{
						String pattern = "yyyy-MM-dd";
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
						String date = simpleDateFormat.format(new Date());
						query = "select view.`"+amountQualifier+"`*view.rate as Accounted_amt, view.*  from (select "
								+ " (SELECT (CASE WHEN from_currency = '"+currencyCode+"'"
								+ "		then conversion_rate"
								+ "        WHEN to_currency = '"+currencyCode+"'"
								+ "        then inverse_rate END) as rate "
								+ "        from t_fx_rates_details where fx_rate_id = "+fxRateId+" and '"+date+"' between from_date and to_date "
								+ "        and (from_currency = '"+currencyCode+"' or to_currency='"+currencyCode+"')) as rate, "
								+ " dv.* from  `"+dv.getDataViewName().toLowerCase()+"` dv where scrIds in ("+idsString+")"
								+ " ) view";
					}
				}
				
				log.info("Query:>>> "+query);
				result=stmt.executeQuery(query);
				
				ResultSetMetaData rsmd = result.getMetaData();
				int columnCount = rsmd.getColumnCount();
				
				while(result.next()){
	        		HashMap dataMap = new HashMap();
	        		for(int i = 1; i <= columnCount; i++ ) {
	        			  String name = rsmd.getColumnName(i);
	        			  dataMap.put(name, result.getString(name));
	        		}
	        		Long rowId = Long.parseLong(result.getString("scrIds").toString());
	        		finalData.put(rowId, dataMap);
	        	}
			}
			catch(Exception e)
			{
				log.info("Exception while fetching view data...");
			}
			finally
			{
				try{
				if(result != null)
					result.close();
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
				}
				catch(Exception e)
				{
					log.info("Exception while closing statements...");
				}
			}
			return finalData;
		}
		
		public HashMap<Long, String> getRowCurrencyValues(Long viewId, List<Long> rowIds, String columnName)
		{
			HashMap<Long, String> finalMap = new HashMap<Long, String>();
			
			Connection conn = null;
			Statement stmt = null;
			ResultSet result = null; 
			DataViews dv = dataViewsRepository.findById(viewId.longValue());
			try{
				DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
				conn = ds.getConnection();
				log.info("Connected to JDBC Successfully");
				stmt = conn.createStatement();
				String query = "";
				if(rowIds.size()>0)
				{
					String rowIdsAsString = reconciliationResultService.getStringWithNumbers(rowIds);
					query = "SELECT scrIds, `"+columnName+"` FROM `"+dv.getDataViewName().toLowerCase()+"` where scrIds in("+rowIdsAsString+")";
					result=stmt.executeQuery(query);
					while(result.next()){
						finalMap.put(Long.parseLong(result.getString(1)), result.getString(2));
					}
				}
			}
			catch(Exception e)
			{
				log.info("Exception while fetching row ids currency values"+e);
			}
			finally
			{
				try{
				if(result != null)
					result.close();
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
				}
				catch(Exception e)
				{
					log.info("Exception while closing statements..."+e);
				}
			}
			return finalMap;
		}
		public HashMap manualAccForSingleCurrency(Long viewId, Long groupId, Long userId, String jobReference, Long tenantId, ManualAccountingDTO manualAccDTO) throws ClassNotFoundException, SQLException
		{
			/*List<Long> rowIds = (List<Long>) singleHM.keySet();*/
			HashMap finalMap = new HashMap();
			List<Long> rowIds = manualAccDTO.getRows();
			log.info("RowIDs: "+ rowIds);
			String transDateQualifier = accountingDataService.getQualifierViewColName(viewId, "TRANSDATE");
			
			if(rowIds.size()>0)
			{
				HashMap colInfo = reconciliationResultService.getColumnInfobyId(manualAccDTO.getEnteredCurrency());
				String currencyColName = colInfo.get("columnAlias").toString();
				HashMap currencyValues = getRowCurrencyValues(viewId, rowIds, currencyColName); 
				// Accounted Summary
				for(Long rowId : rowIds)
				{
					AccountedSummary ac = new AccountedSummary();
					ac.setRowId(rowId);
					ac.setRuleId(0L);
					ac.setCreditCount(manualAccDTO.getCredits().size());
					ac.setDebitCount(manualAccDTO.getDebits().size());
					ac.setStatus("ACCOUNTED");
					ac.setCreatedBy(userId);
					ac.setCreatedDate(ZonedDateTime.now());
					ac.setLastUpdatedBy(userId);
					ac.setLastUpdatedDate(ZonedDateTime.now());
					ac.setJobReference(jobReference);
					ac.setViewId(viewId);
					ac.setRuleGroupId(groupId);
					ac.setCurrentRecordFlag(true);
					AccountedSummary editAccounting = accountedSummaryRepository.findByRowIdAndRuleGroupIdAndViewId(rowId, groupId, viewId);
					if(editAccounting != null)
					{
						ac.setId(editAccounting.getId());
					}
					AccountedSummary asSave = accountedSummaryRepository.save(ac);
					// Accounting Data
					// Credits
					if(manualAccDTO.getCredits().size()>0)
					{
						for(CreditLineDTO credit : manualAccDTO.getCredits())
						{
							HashMap amountColInfo = reconciliationResultService.getColumnInfobyId(credit.getAmountColId());
							String amntColName = amountColInfo.get("columnAlias").toString();
							HashMap amntValues = getRowCurrencyValues(viewId, rowIds, amntColName); 
							
							AccountingData ad = new AccountingData();
							ad.setTenantId(tenantId);
							ad.setAccountedDate(ZonedDateTime.now());
							ad.setAccountedSummaryId(asSave.getId());
							ad.setOriginalRowId(rowId);
							
							LedgerDefinition ld = ledgerDefinitionRepository.findByIdForDisplayAndTenantId(manualAccDTO.getLedgerRef(), tenantId);
							if(ld != null)
							{
								ad.setLedgerRef(ld.getId().toString());
							}
							ad.setLedgerCurrency(manualAccDTO.getAccountedCurrency());
							ad.setLedgerRefType("CONSTANT");
							ad.setLineTypeDetail(credit.getLineTypeDetial());
							ad.setAmountColId(credit.getAmountColId());
							ad.setCategoryRef(manualAccDTO.getCategoryRef());
							ad.setSourceRef(manualAccDTO.getSourceRef());
							ad.setLineType("CREDIT");
							ad.setJobReference(jobReference);
							ad.setCreatedBy(userId);
							ad.setCreatedDate(ZonedDateTime.now());
							ad.setLastUpdatedBy(userId);
							ad.setLastUpdatedDate(ZonedDateTime.now());
							ad.setStatus("SUCCESS");
							ad.setAcctGroupId(groupId);
							ad.setAcctRuleId(0L);
							ad.setOriginalViewId(viewId);
							ChartOfAccount chartOfAccount = chartOfAccountRepository.findByIdForDisplayAndTenantId(manualAccDTO.getCoaRef(), tenantId);
							if(chartOfAccount != null)
							{
								ad.setCoaRef(chartOfAccount.getId().toString());
							}							
							if(currencyValues.size()>0 && currencyValues.get(rowId) != null)
							{
								ad.setCurrencyRef(currencyValues.get(rowId).toString());
							}
							if(amntValues.size()>0 && amntValues.get(rowId) != null)
							{
								ad.setAmount(new BigDecimal(amntValues.get(rowId).toString()));
							}
							
							if(manualAccDTO.getFxRateId() != null && manualAccDTO.getConversionDate() != null && manualAccDTO.getAccountedCurrency() != null)
							{
								HashMap viewData = getViewData(viewId, rowIds, "multi", amntColName,currencyValues.get(rowId).toString(), manualAccDTO.getFxRateId(), manualAccDTO.getConversionDate(),transDateQualifier);
								HashMap dataMap = (HashMap) viewData.get(rowId);
//								if("TRANSACTION_DATE".equalsIgnoreCase(manualAccDTO.getConversionDate()))
//								{
//									LocalDate localDate = LocalDate.parse(dataMap.get(transDateQualifier).toString());
//									ZonedDateTime zonedDateTime = 
//								    		  localDate.atStartOfDay(ZoneId.systemDefault());
//									ad.setAccountedDate(zonedDateTime);
//								}
//								else
//								{
//									ad.setAccountedDate(ZonedDateTime.now());								
//								}
								if(dataMap.get("Accounted_amt") != null && dataMap.get("rate") != null)
								{
									ad.setAccountedAmount(new BigDecimal(dataMap.get("Accounted_amt").toString()));
									ad.setFxRate(new BigDecimal(dataMap.get("rate").toString()));
								}
							}
							else
							{
								ad.setAccountedAmount(new BigDecimal(amntValues.get(rowId).toString()));
								ad.setFxRate(new BigDecimal("1"));
							}
							
							List<HashMap> creditSegs = credit.getCreditSegs();
							int i =0;
							for (HashMap hm : creditSegs) {
								if (i == 0) {
									ad.setAccountingRef1(hm.get("sourceValue").toString());
								} else if (i == 1) {
									ad.setAccountingRef2(hm.get("sourceValue").toString());
								} else if (i == 2) {
									ad.setAccountingRef3(hm.get("sourceValue").toString());
								} else if (i == 3) {
									ad.setAccountingRef4(hm.get("sourceValue").toString());
								} else if (i == 4) {
									ad.setAccountingRef5(hm.get("sourceValue").toString());
								} else if (i == 5) {
									ad.setAccountingRef6(hm.get("sourceValue").toString());
								} else if (i == 6) {
									ad.setAccountingRef7(hm.get("sourceValue").toString());
								} else if (i == 7) {
									ad.setAccountingRef8(hm.get("sourceValue").toString());
								} else if (i == 8) {
									ad.setAccountingRef9(hm.get("sourceValue").toString());
								} else if (i == 9) {
									ad.setAccountingRef10(hm.get("sourceValue").toString());
								}
								i++;
							}
							log.info("SHIVA: tenantId, rowId, CREDIT, viewId, groupId: "+tenantId, rowId, "CREDIT", viewId, groupId);
							AccountingData editAccntngData = accountingDataRepository.findByTenantIdAndOriginalRowIdAndLineTypeAndOriginalViewIdAndAcctGroupIdAndReverseRefIdIsNull(tenantId, rowId, "CREDIT", viewId, groupId);
							if(editAccntngData !=null)
							{
								ad.setId(editAccntngData.getId());
							}
							accountingDataRepository.save(ad);
						}
					}
					//Debits
					if(manualAccDTO.getDebits().size()>0)
					{
						for(DebitLineDTO debit : manualAccDTO.getDebits())
						{
							
							HashMap amountColInfo = reconciliationResultService.getColumnInfobyId(debit.getAmountColId());
							String amntColName = amountColInfo.get("columnAlias").toString();
							HashMap amntValues = getRowCurrencyValues(viewId, rowIds, amntColName); 
							
							AccountingData ad = new AccountingData();
							ad.setTenantId(tenantId);
							ad.setAccountedDate(ZonedDateTime.now());
							ad.setAccountedSummaryId(asSave.getId());
							ad.setOriginalRowId(rowId);
							LedgerDefinition ld = ledgerDefinitionRepository.findByIdForDisplayAndTenantId(manualAccDTO.getLedgerRef(), tenantId);
							if(ld != null)
							{
								ad.setLedgerRef(ld.getId().toString());
							}
							ad.setLedgerCurrency(manualAccDTO.getAccountedCurrency());
							ad.setLedgerRefType("CONSTANT");
							ad.setLineTypeDetail(debit.getLineTypeDetial());
							ad.setAmountColId(debit.getAmountColId());
							ad.setCategoryRef(manualAccDTO.getCategoryRef());
							ad.setSourceRef(manualAccDTO.getSourceRef());
							ad.setLineType("DEBIT");
							ad.setJobReference(jobReference);
							ad.setCreatedBy(userId);
							ad.setCreatedDate(ZonedDateTime.now());
							ad.setLastUpdatedBy(userId);
							ad.setLastUpdatedDate(ZonedDateTime.now());
							ad.setStatus("SUCCESS");
							ad.setAcctGroupId(groupId);
							ad.setAcctRuleId(0L);
							ad.setOriginalViewId(viewId);
							ChartOfAccount chartOfAccount = chartOfAccountRepository.findByIdForDisplayAndTenantId(manualAccDTO.getCoaRef(), tenantId);
							if(chartOfAccount != null)
							{
								ad.setCoaRef(chartOfAccount.getId().toString());
							}
							
							if(currencyValues.size()>0 && currencyValues.get(rowId) != null)
							{
								ad.setCurrencyRef(currencyValues.get(rowId).toString());
							}
							if(amntValues.size()>0 && amntValues.get(rowId) != null)
							{
								ad.setAmount(new BigDecimal(amntValues.get(rowId).toString()));
							}
							
							if(manualAccDTO.getFxRateId() != null && manualAccDTO.getConversionDate() != null && manualAccDTO.getAccountedCurrency() != null)
							{
								HashMap viewData = getViewData(viewId, rowIds, "multi", amntColName, currencyValues.get(rowId).toString(), manualAccDTO.getFxRateId(), manualAccDTO.getConversionDate(),transDateQualifier);
								HashMap dataMap = (HashMap) viewData.get(rowId);
//								if("TRANSACTION_DATE".equalsIgnoreCase(manualAccDTO.getConversionDate()))
//								{
//									LocalDate localDate = LocalDate.parse(dataMap.get(transDateQualifier).toString());
//									ZonedDateTime zonedDateTime = 
//								    		  localDate.atStartOfDay(ZoneId.systemDefault());
//									ad.setAccountedDate(zonedDateTime);
//								}
//								else
//								{
//									ad.setAccountedDate(ZonedDateTime.now());								
//								}
								if(dataMap.get("Accounted_amt") != null && dataMap.get("rate") != null)
								{
									ad.setAccountedAmount(new BigDecimal(dataMap.get("Accounted_amt").toString()));
									ad.setFxRate(new BigDecimal(dataMap.get("rate").toString()));
								}
							}
							else
							{
								ad.setAccountedAmount(new BigDecimal(amntValues.get(rowId).toString()));
								ad.setFxRate(new BigDecimal("1"));
							}
							
							
							List<HashMap> debitSegs = debit.getDebitSegs();
							int i =0;
							for (HashMap hm : debitSegs) {
								if (i == 0) {
									ad.setAccountingRef1(hm.get("sourceValue").toString());
								} else if (i == 1) {
									ad.setAccountingRef2(hm.get("sourceValue").toString());
								} else if (i == 2) {
									ad.setAccountingRef3(hm.get("sourceValue").toString());
								} else if (i == 3) {
									ad.setAccountingRef4(hm.get("sourceValue").toString());
								} else if (i == 4) {
									ad.setAccountingRef5(hm.get("sourceValue").toString());
								} else if (i == 5) {
									ad.setAccountingRef6(hm.get("sourceValue").toString());
								} else if (i == 6) {
									ad.setAccountingRef7(hm.get("sourceValue").toString());
								} else if (i == 7) {
									ad.setAccountingRef8(hm.get("sourceValue").toString());
								} else if (i == 8) {
									ad.setAccountingRef9(hm.get("sourceValue").toString());
								} else if (i == 9) {
									ad.setAccountingRef10(hm.get("sourceValue").toString());
								}
								i++;
							}
							AccountingData editAccntngData = accountingDataRepository.findByTenantIdAndOriginalRowIdAndLineTypeAndOriginalViewIdAndAcctGroupIdAndReverseRefIdIsNull(tenantId, rowId, "DEBIT", viewId, groupId);
							if(editAccntngData !=null)
							{
								log.info("");
								ad.setId(editAccntngData.getId());
							}
							accountingDataRepository.save(ad);
						}
					}
				}
			}
			return finalMap;
		}
		
		public HashMap getColumnInfo(Long columnId)
		{
			HashMap colInfo = new HashMap();
			DataViewsColumns dvc = dataViewsColumnsRepository.findOne(columnId);
			if(dvc != null)
			{
				if("File Template".equalsIgnoreCase(dvc.getRefDvType()))
				{
					FileTemplateLines ftl = fileTemplateLinesRepository.findOne(Long.parseLong(dvc.getRefDvColumn().toString()));
					if(ftl != null)
					{
						colInfo.put("id", dvc.getId());
						colInfo.put("colName", ftl.getColumnAlias());
						colInfo.put("colDisplayName", dvc.getColumnName());
						colInfo.put("dataType", dvc.getColDataType());
					}
				}
				else if("Data View".equalsIgnoreCase(dvc.getRefDvType()) || dvc.getRefDvType() == null)
				{
					colInfo.put("id", dvc.getId());
					colInfo.put("colName", dvc.getColumnName());
					colInfo.put("colDisplayName", dvc.getColumnName());
					colInfo.put("dataType", dvc.getColDataType());			
				}
			}
			return colInfo;
		}
		
		public HashMap getGroupIdAndViewId(String groupName, String viewName, Long tenantId)
		{
			HashMap finalMap = new HashMap();
    		Connection conn = null;
    		Statement stmt = null;
    		ResultSet result = null;
    		String query = "select distinct rg.id rule_group_id,"
    				+ " rg.name rule_group_name,"
    				+ " dv.id data_view_id,"
    				+ " dv.data_view_name"
    				+ " from t_rule_group rg, t_data_views dv"
    				+ " where rg.rule_purpose = 'ACCOUNTING'"
    				+ " and rg.name = '"+groupName+"'"
    				+ " and dv.data_view_name = '"+viewName+"_"+tenantId+"'";
    		try{
    			log.info("Accounting File Export group by group id query: "+query);
    			DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
    			conn = ds.getConnection();
	     	    stmt = conn.createStatement();
	     	    result=stmt.executeQuery(query);
	     	    result.next();
	     	    finalMap.put("groupId", result.getString("rule_group_id"));
	     	    finalMap.put("groupName", result.getString("rule_group_name"));
	     	    finalMap.put("viewId", result.getString("data_view_id"));
	     	    finalMap.put("viewName", result.getString("data_view_name"));
    		}
    		catch(Exception e)
    		{
    			log.info("Exception while fetching group id and view id(Accounting Outbound API) "+e);
    		}
    		finally
    		{
	    		try{
					if(result != null)
						result.close();
					if(stmt != null)
						stmt.close();
					if(conn != null)
						conn.close();			
	    		}
	    		catch(Exception e)
	    		{
	    			log.info("Exception while closing jdbc statements.(Identify source or target) "+e);
	    		}
    		}
			return finalMap;
		}
		
		public String identifySourceOrTarget(Long viewId, Long groupId, Long tenantId)
		{

    		Connection conn = null;
    		Statement stmt = null;
    		Statement stmt2 = null;
    		ResultSet result = null;
    		ResultSet result2 = null;
    		
    		String sourceOrTarget = "";
    		
    		String srcQuery = "select count(*) from t_rule_group_details rgd_a, t_rule_group rg_a, t_rule_group_details rgd_r,"
    				+ " t_rules ru, t_rules ru_r where rg_a.id = "+groupId+""
    				+ " and rgd_a.rule_id = ru.id"
    				+ " and rgd_a.rule_group_id = rg_a.id"
    				+ " and ru.source_data_view_id = "+viewId+""
    				+ " and rg_a.reconciliation_group_id = rgd_r.rule_group_id"
    				+ " and rgd_r.rule_id = ru_r.id"
    				+ " and ru.source_data_view_id = ru_r.source_data_view_id"
    				+ " and ru.reconciliation_view_id = ru_r.target_data_view_id";
    		
    		String trgtQuery = "select count(*) from t_rule_group_details rgd_a, t_rule_group rg_a, t_rule_group_details rgd_r,"
    				+ " t_rules ru, t_rules ru_r where rg_a.id = "+groupId+""
    				+ " and rgd_a.rule_id = ru.id"
    				+ " and rgd_a.rule_group_id = rg_a.id"
    				+ " and ru.source_data_view_id = "+viewId+""
    				+ " and rg_a.reconciliation_group_id = rgd_r.rule_group_id"
    				+ " and rgd_r.rule_id = ru_r.id"
    				+ " and ru.source_data_view_id = ru_r.target_data_view_id"
    				+ " and ru.reconciliation_view_id = ru_r.source_data_view_id";
    		
    		try{
    			
    			DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
    			conn = ds.getConnection();
    			
	     	    // Executing Source Query
	     	    stmt = conn.createStatement();
	     	    result=stmt.executeQuery(srcQuery);
		     	while(result.next()){
		     		int count = Integer.parseInt(result.getString(1).toString());
		     		log.info("Source Count: "+count);
		     		if(count > 0)
		     		{
		     			sourceOrTarget = "Source";
		     		}
		     	}
		     	// Executing Target Query 
		     	stmt2 = conn.createStatement();
		     	result2 = stmt2.executeQuery(trgtQuery);
		     	while(result2.next())
		     	{
		     		int count = Integer.parseInt(result2.getString(1).toString());
		     		log.info("Target Count: "+ count);
		     		if(count > 0)
		     		{
		     			sourceOrTarget = "Target";
		     		}
		     	}
		     	log.info("Final String Source OR Target: "+ sourceOrTarget);
    		}
    		catch(Exception e)
    		{
    			log.info("Exception while fetching recon source data "+e);
    		}
    		finally
    		{
	    		try{
					if(result != null)
						result.close();
					if(result2 != null)
						result2.close();
					if(stmt != null)
						stmt.close();
					if(stmt2 != null)
						stmt2.close();
					if(conn != null)
						conn.close();			
	    		}
	    		catch(Exception e)
	    		{
	    			log.info("Exception while closing jdbc statements.(Identify source or target) "+e);
	    		}
    		}
			return sourceOrTarget;
		}
		
		public HashMap getAcctSummaryByRuleGroup(Long tenantId)
		{
			HashMap finalMap = new HashMap();
			List<HashMap> summary = new ArrayList<HashMap>();
			int count = 0;
			HashMap info = new HashMap();
			
    		Connection conn = null;
    		Statement stmt = null;
    		ResultSet result = null;
    		try
    		{
    			DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
    			conn = ds.getConnection();
	     	    
				String query = "SELECT DISTINCT "
						+ "det.rule_group_id,det.name,det.view_name,det.view_id,act.ledger_ref, "
						+ "act.ledger,act.ledger_currency,det.activity_based,det.multi_currency, ifnull(act.act_amount, 0) act_amount, ifnull(act.trx_count, 0) trx_count, det.reconciliation_group_id, "
						+ "det.rg_id_display,det.dv_id_display, Ifnull(det.view_count, 0) view_count "
						+ "FROM (SELECT rg.id rule_group_id,rg.name,ru.source_data_view_id view_id,dv.data_view_disp_name view_name,"
						+ "rg.activity_based,rg.multi_currency,rg.reconciliation_group_id,rg.id_for_display rg_id_display,dv.id_for_display dv_id_display, dv.record_count	view_count "
						+ "FROM t_rule_group rg, t_rule_group_details rgd, t_rules ru, t_data_views dv "
						+ "WHERE rg.id = rgd.rule_group_id AND rgd.rule_id = ru.id AND ru.tenant_id = "+tenantId+" "
						+ "AND ru.source_data_view_id = dv.id AND rg.rule_purpose = 'ACCOUNTING') det "
						+ "LEFT JOIN ( SELECT acs.rule_group_id acct_group_id, acs.view_id original_view_id,ld.id ledger_ref, "
						+ "ld.name as ledger,ld.currency ledger_currency,sum(acd_c.accounted_amount) act_amount,count(acs.row_id) trx_count "
						+ "FROM t_accounting_data acd_c,t_accounting_data acd_d,t_accounted_summary acs,t_ledger_definition ld "
						+ "WHERE  acd_d.line_type = 'DEBIT' "
						+ "AND acd_c.line_type = 'CREDIT' "
						+ "AND acs.id = acd_d.accounted_summary_id "
						+ "AND acs.id = acd_c.accounted_summary_id "
						+ "AND acd_c.accounted_summary_id = acd_d.accounted_summary_id "
						+ "AND acd_c.original_view_id = acd_d.original_view_id "
						+ "AND acd_c.original_row_id = acd_d.original_row_id "
						+ "AND ifnull(acd_d.amount, 0) - ifnull(acd_c.amount, 0) = 0 "
						+ "AND acs.current_record_flag IS TRUE "
						+ "AND acs.status = 'ACCOUNTED' "
						+ "AND acd_c.status = 'SUCCESS' AND acd_d.status = 'SUCCESS' "
						+ "AND acd_c.tenant_id = "+tenantId+" "
						+ "AND acd_c.ledger_ref = ld.id "
						+ "AND acd_c.acct_rule_id is not null "
						+ "AND acd_d.acct_rule_id is not null "
						+ "GROUP BY  acs.rule_group_id, acs.view_id, ld.id, ld.name, ld.currency) act "
						+ "ON det.rule_group_id = act.acct_group_id AND act.original_view_id = det.view_id order by det.name asc";
				
				log.info("Query to fetch all accounting groups summary info: "+query);
	     	    stmt = conn.createStatement();
	     	    result=stmt.executeQuery(query);
	     	    
	     	    while(result.next())
	     	    {
	     	    	count = count + 1;
	     	    	HashMap hm = new HashMap();
	     	    	hm.put("groupId", result.getString("rg_id_display"));
	     	    	hm.put("groupName", result.getString("name"));
	     	    	hm.put("viewId", result.getString("dv_id_display"));
	     	    	hm.put("viewName", result.getString("view_name"));
	     	    	hm.put("trxCount", Long.parseLong(result.getString("trx_count")));
	     	    	hm.put("acctAmount", Double.parseDouble(result.getString("act_amount")));
	     	    	hm.put("ledgerName", result.getString("ledger"));
	     	    	hm.put("ledgerRef", result.getString("ledger_ref"));
	     	    	hm.put("ledgerCurrency", result.getString("ledger_currency"));
	     	    	if("1".equalsIgnoreCase(result.getString("activity_based")))
	     	    	{
		     	    	hm.put("isActivityBased", true);	     	    		
	     	    	}
	     	    	else
	     	    	{
	     	    		hm.put("isActivityBased", false);
	     	    	}
	     	    	if("1".equalsIgnoreCase(result.getString("multi_currency")))
	     	    	{
	     	    		hm.put("isMultiCurrency", true);
	     	    	}
	     	    	else
	     	    	{
	     	    		hm.put("isMultiCurrency", false);
	     	    	}
	     	    	hm.put("viewCount", Long.parseLong(result.getString("view_count")));
	     	    	summary.add(hm);
	     	    }
    		}
    		catch(Exception e)
    		{
    			log.info("Exception while fetching all accounting summary information: "+e);
    		}
    		finally
    		{
	    		try{
					if(result != null)
						result.close();
					if(stmt != null)
						stmt.close();
					if(conn != null)
						conn.close();	    			
	    		}
	    		catch(Exception e)
	    		{
	    			log.info("Exception while closing jdbc statements.(Accounting All Groups Info.) "+e);
	    		}
    		}
			
			info.put("count", count);
			finalMap.put("summary", summary);
			finalMap.put("info", info);
			return finalMap;
		}
		
		/* Author Bhagath */
		public HashMap getAcctSummaryByRuleGroupOld(Long tenantId) throws SQLException, ClassNotFoundException
	    {
     	    HashMap grpsMap = new HashMap();
    		Connection conn = null;
    		Statement stmt = null;
    		ResultSet result = null;
    		try
    		{	
			String query = "SELECT DISTINCT "
					+ "det.rule_group_id,det.name,det.view_name,det.view_id,act.ledger_ref, "
					+ "act.ledger,act.ledger_currency,det.activity_based,det.multi_currency,act.act_amount,act.trx_count,det.reconciliation_group_id, "
					+ "det.rg_id_display,det.dv_id_display "
					+ "FROM (SELECT rg.id rule_group_id,rg.name,ru.source_data_view_id view_id,dv.data_view_disp_name view_name,"
					+ "rg.activity_based,rg.multi_currency,rg.reconciliation_group_id,rg.id_for_display rg_id_display,dv.id_for_display dv_id_display "
					+ "FROM t_rule_group rg, t_rule_group_details rgd, t_rules ru, t_data_views dv "
					+ "WHERE rg.id = rgd.rule_group_id AND rgd.rule_id = ru.id AND ru.tenant_id = "+tenantId+" "
					+ "AND ru.source_data_view_id = dv.id AND rg.rule_purpose = 'ACCOUNTING') det "
					+ "LEFT JOIN ( SELECT acs.rule_group_id acct_group_id, acs.view_id original_view_id,ld.id ledger_ref, "
					+ "ld.name as ledger,ld.currency ledger_currency,sum(acd_c.accounted_amount) act_amount,count(acs.row_id) trx_count "
					+ "FROM t_accounting_data acd_c,t_accounting_data acd_d,t_accounted_summary acs,t_ledger_definition ld "
					+ "WHERE  acd_d.line_type = 'DEBIT' "
					+ "AND acd_c.line_type = 'CREDIT' "
					+ "AND acs.id = acd_d.accounted_summary_id "
					+ "AND acs.id = acd_c.accounted_summary_id "
					+ "AND acd_c.accounted_summary_id = acd_d.accounted_summary_id "
					+ "AND acd_c.original_view_id = acd_d.original_view_id "
					+ "AND acd_c.original_row_id = acd_d.original_row_id "
					+ "AND acd_d.amount-acd_c.amount =0 "
					+ "AND acs.current_record_flag IS TRUE "
					+ "AND acs.status = 'ACCOUNTED' "
					+ "AND acd_c.tenant_id = "+tenantId+" "
					+ "AND acd_c.ledger_ref = ld.id "
					+ "GROUP BY  acs.rule_group_id, acs.view_id, ld.id, ld.name, ld.currency) act "
					+ "ON det.rule_group_id = act.acct_group_id AND act.original_view_id = det.view_id order by det.name asc";
    			
    			log.info("Query to fetch summary info for all rule groups: "+ query);
    			
    			DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
    			conn = ds.getConnection();
    			
	     	    stmt = conn.createStatement();
	     	    result=stmt.executeQuery(query);
	     	    
		     	while(result.next()){
		     		Long groupId = Long.parseLong(result.getString("rule_group_id"));
		     		String grpName = result.getString("name");
		     		if(grpsMap.containsKey(groupId))
		     		{	// Updating existing record
		     			HashMap grpMap = (HashMap) grpsMap.get(groupId);
		     			HashMap viewMap = (HashMap) grpMap.get("views");
		     			Long viewId = Long.parseLong(result.getString("view_id"));
		     			if(viewMap.containsKey(viewId))
		     			{	// Updating existing data view
		     				HashMap viewDetails = (HashMap) viewMap.get(viewId);
		     				List<HashMap> ledgers = (List<HashMap>) viewDetails.get("ledgers");
			     			HashMap ledgerInfo = new HashMap();
			     			ledgerInfo.put("ledgerRef", result.getString("ledger_ref"));
			     			ledgerInfo.put("ledgerName", result.getString("ledger"));
			     			ledgerInfo.put("ledgerCurrency", result.getString("ledger_currency"));
			     			if(result.getString("act_amount") != null && result.getString("trx_count") != null)
			     			{
			     				ledgerInfo.put("acctAmount", Double.parseDouble(result.getString("act_amount")));
				     			ledgerInfo.put("trxCount", Long.parseLong(result.getString("trx_count")));
			     			}
			     			else
			     			{
			     				ledgerInfo.put("acctAmount", 0.0);
				     			ledgerInfo.put("trxCount", 0);
			     			}
			     			ledgers.add(ledgerInfo);
		     			}
		     			else
		     			{	// Creating new data view
		     				HashMap viewDetails = new HashMap();
		     				viewDetails.put("viewId", result.getString("dv_id_display"));
		     				viewDetails.put("viewName", result.getString("view_name"));
			     			List<HashMap> ledgers = new ArrayList<HashMap>();
			     			HashMap ledgerInfo = new HashMap();
			     			ledgerInfo.put("ledgerRef", result.getString("ledger_ref"));
			     			ledgerInfo.put("ledgerName", result.getString("ledger"));
			     			ledgerInfo.put("ledgerCurrency", result.getString("ledger_currency"));
			     			if(result.getString("act_amount") != null && result.getString("trx_count") != null)
			     			{
			     				ledgerInfo.put("acctAmount", Double.parseDouble(result.getString("act_amount")));
				     			ledgerInfo.put("trxCount", Long.parseLong(result.getString("trx_count")));
			     			}
			     			else
			     			{
			     				ledgerInfo.put("acctAmount", 0.0);
				     			ledgerInfo.put("trxCount", 0);
			     			}
			     			ledgers.add(ledgerInfo);
			     			viewDetails.put("ledgers", ledgers);
		     				viewMap.put(viewId, viewDetails);
		     			}
		     		}
		     		else
		     		{	// Creating new record
		     			HashMap grpMap = new HashMap();
		     			grpMap.put("groupId", result.getString("rg_id_display"));
		     			grpMap.put("groupName", result.getString("name"));
		     			if("1".equalsIgnoreCase(result.getString("activity_based")))
		     			{
		     				grpMap.put("isActivityBased", true);
		     			}
		     			else if("0".equalsIgnoreCase(result.getString("activity_based")))
		     			{
		     				grpMap.put("isActivityBased", false);				
		     			}
		     			if("1".equalsIgnoreCase(result.getString("multi_currency")))
		     			{
		     				grpMap.put("isMultiCurrency", true);
		     			}
		     			else if("0".equalsIgnoreCase(result.getString("multi_currency")))
		     			{
		     				grpMap.put("isMultiCurrency", false);				
		     			}
		     			HashMap viewMap = new HashMap();
		     			Long viewId = Long.parseLong(result.getString("view_id"));
		     			HashMap viewDetails = new HashMap();
		     			viewDetails.put("viewId", result.getString("dv_id_display"));
		     			viewDetails.put("viewName", result.getString("view_name"));
		     			List<HashMap> ledgers = new ArrayList<HashMap>();
		     			HashMap ledgerInfo = new HashMap();
		     			ledgerInfo.put("ledgerRef", result.getString("ledger_ref"));
		     			ledgerInfo.put("ledgerName", result.getString("ledger"));
		     			ledgerInfo.put("ledgerCurrency", result.getString("ledger_currency"));
		     			if(result.getString("act_amount") != null && result.getString("trx_count") != null)
		     			{
		     				ledgerInfo.put("acctAmount", Double.parseDouble(result.getString("act_amount")));
			     			ledgerInfo.put("trxCount", Long.parseLong(result.getString("trx_count")));
		     			}
		     			else
		     			{
		     				ledgerInfo.put("acctAmount", 0.0);
			     			ledgerInfo.put("trxCount", 0);
		     			}
		     			ledgers.add(ledgerInfo);
		     			viewDetails.put("ledgers", ledgers);
		     			viewMap.put(viewId, viewDetails);
		     			grpMap.put("views", viewMap);
		     			grpsMap.put(groupId, grpMap);
		     		}
		     	}
    		}
    		catch(Exception e)
    		{
    			log.info("Exception while fetching summary info for all groups. "+e);
    		}
    		finally
    		{
	    		try{
					if(result != null)
						result.close();
					if(stmt != null)
						stmt.close();
					if(conn != null)
						conn.close();	    			
	    		}
	    		catch(Exception e)
	    		{
	    			log.info("Exception while closing jdbc statements.(Accounting All Groups Info.) "+e);
	    		}
    		}
	    	return grpsMap;
	    }
		public List<LinkedHashMap> getAccountingTransactionsData(String srcOrTrgt, Long viewId, String activityOrNonActivity, String viewName, Long groupId, String status, String whereString, String paginationCondition,
				HashMap finalHeaderColumns)
		{
			List<LinkedHashMap> finalData = new ArrayList<LinkedHashMap>();
			Connection conn = null;
			Statement stmt = null;
			ResultSet result = null;
			try{
				String srcOrTrgtQuery = "";
				if("source".equalsIgnoreCase(srcOrTrgt))
				{
					srcOrTrgtQuery = " LEFT JOIN (select recon_reference, original_row_id as source_row_id from t_reconciliation_result where original_view_id = "+viewId+" and recon_status = 'RECONCILED'"
							+ " and current_record_flag is true) recon"
							+ " ON dv.scrIds = recon.source_row_id ";
				}
				else if("target".equalsIgnoreCase(srcOrTrgt))
				{
					srcOrTrgtQuery = " LEFT JOIN (select recon_reference, target_row_id from t_reconciliation_result where target_view_id = "+viewId+" and recon_status = 'RECONCILED'"
							+ " and current_record_flag is true) recon"
							+ " ON dv.scrIds = recon.target_row_id ";
				}
				DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
				conn = ds.getConnection();
				stmt = conn.createStatement();
				String query = "";
				if("Y".equalsIgnoreCase(activityOrNonActivity))
				{
					query = " select * from ("
							+ "	select dv.*, rg.name accounting_rule_group, "
							+ "	(case when acd.ACCOUNTING_STATUS is null and recon.recon_reference is null"
							+ "	THEN "
							+ "		'UN ACCOUNTED, NOT RECONCILED'"
							+ "	when acd.ACCOUNTING_STATUS is null and recon.recon_reference is not null"
							+ " then"
							+ " 	'UN ACCOUNTED, RECONCILED'"
							+ " when acd.ACCOUNTING_STATUS is not null and recon.recon_reference is null"
							+ " then "
							+ " 	'ACCOUNTED, NOT RECONCILED'"
							+ " when acd.ACCOUNTING_STATUS is not null and recon.recon_reference is not null"
							+ " then "
							+ " 	'ACCOUNTED, RECONCILED' END) as accounting_status,"
							+ " appStatusLum.meaning final_status, final_action_date, rl2.rule_code  approval_rule_code, "
							+ " (case when acd.journal_status is null "
							+ "	THEN "
							+ "		'Pending for Journal' "
							+ " when acd.journal_status ='ENTERED' "
							+ "	THEN "
							+ "		'Journals Prepared' end) as journal_status, " 
							+ " original_row_id, line_type, line_type_detail, source_ref, coa_ref, ledger_ref, category_ref, currency_ref entered_currency,	"
							+ "	ledger_currency accounted_currency, led.name ledger_name, acd.amount entered_amount, srclum.meaning source_meaning, "
					        + "	catlum.meaning category_meaning, accounted_amount, coa.name coa_name, "
							+ " accounting_ref_1,accounting_ref_2,accounting_ref_3,accounting_ref_4,accounting_ref_5,"
							+ " accounting_ref_6,accounting_ref_7,accounting_ref_8,accounting_ref_9, accounting_ref_10,  "
							+ "	case when acct_rule_id = 0 "  
							+ "	then 'Manual' else 	rl.rule_code end rule_code, rl.id rule_id, "
							+ " case when SUBSTRING(job_reference, 1, 6) = 'MANUAL'  THEN 'Manual'  ELSE acd.job_reference  end job_reference, amount_col_id "
							+ " from (select * from `"+viewName.toLowerCase()+"`) dv "	// Need to Add Date Range
							+ " LEFT JOIN (SELECT su.STATUS ACCOUNTING_STATUS,su.journal_status, de.* FROM t_accounted_summary su, "
							+ " t_accounting_data de WHERE su.id = de.accounted_summary_id AND su.rule_group_id = "+groupId+" and su.view_id = "+viewId+" and su.current_record_flag is true) acd  "	// Need to Add Approval Status Condition
							+ " ON dv.scrIds = acd.original_row_id "
							+ "	"+ srcOrTrgtQuery +" "
							+ " LEFT JOIN look_up_code srclum  ON acd.source_ref = srclum.look_up_code  and acd.tenant_id = srclum.tenant_id and srclum.look_up_type = 'SOURCE' "   
							+ "	LEFT JOIN look_up_code catlum  ON acd.category_ref = catlum.look_up_code  and acd.tenant_id = catlum.tenant_id and catlum.look_up_type = 'CATEGORY' "
							+ " LEFT JOIN look_up_code appStatusLum ON acd.final_status = appStatusLum.look_up_code and acd.tenant_id = appStatusLum.tenant_id and appStatusLum.look_up_type = 'APPROVAL_STATUS'"
							+ " LEFT JOIN t_ledger_definition led ON acd.ledger_ref = led.id "
							+ " LEFT JOIN t_chart_of_account coa ON acd.coa_ref = coa.id "
							+ " LEFT JOIN t_rules rl ON acd.acct_rule_id = rl.id"
							+ "	LEFT JOIN t_rules rl2 ON acd.approval_rule_id = rl2.id"
							+ " LEFT JOIN t_rule_group rg ON acd.acct_group_id = rg.id) det where accounting_status = '"+status+"' "+whereString+" "+paginationCondition;
				}
				else if("N".equalsIgnoreCase(activityOrNonActivity))
				{
					query = "select * from "
							+ " (select dv.*,  rg.name accounting_rule_group, "
							+ " (case when acd.ACCOUNTING_STATUS is null  "	
							+ " THEN "
							+ " 	'UN ACCOUNTED'  "
							+ " ELSE"
							+ " acd.ACCOUNTING_STATUS  END) as accounting_status,  "
							+ " appStatusLum.meaning final_status, rl2.rule_code  approval_rule_code, final_action_date, "
							+ " (case when acd.journal_status is null THEN 'Pending for Journal' when acd.journal_status ='ENTERED' THEN "
							+ "	'Journals Prepared' end) as journal_status, original_row_id, line_type, line_type_detail, source_ref, coa_ref, ledger_ref, category_ref,"
							+ "	currency_ref entered_currency, ledger_currency accounted_currency, led.name ledger_name, srclum.meaning source_meaning, "
					        + "	catlum.meaning category_meaning, acd.amount entered_amount, accounted_amount, coa.name coa_name, "
							+ " accounting_ref_1,accounting_ref_2,accounting_ref_3,accounting_ref_4,accounting_ref_5,"
							+ " accounting_ref_6,accounting_ref_7,accounting_ref_8,accounting_ref_9, accounting_ref_10, "
							+ "	case when acct_rule_id = 0 then 'Manual' else rl.rule_code end rule_code, rl.id rule_id, acd.job_reference, amount_col_id"
							+ " from (select * from `"+viewName.toLowerCase()+"`) dv "	// Need to add date range condition
							+ " LEFT JOIN ("
							+ " SELECT su.STATUS ACCOUNTING_STATUS, su.journal_status, de.* FROM t_accounted_summary su, "
							+ " t_accounting_data de WHERE su.id = de.accounted_summary_id AND su.rule_group_id = "+groupId+" and su.view_id = "+viewId+" and su.current_record_flag is true) acd"
							+ " ON dv.scrIds = acd.original_row_id "
							+ " LEFT JOIN look_up_code srclum  ON acd.source_ref = srclum.look_up_code  and acd.tenant_id = srclum.tenant_id and srclum.look_up_type = 'SOURCE' "   
							+ "	LEFT JOIN look_up_code catlum  ON acd.category_ref = catlum.look_up_code  and acd.tenant_id = catlum.tenant_id and catlum.look_up_type = 'CATEGORY' "
							+ " LEFT JOIN look_up_code appStatusLum ON acd.final_status = appStatusLum.look_up_code and acd.tenant_id = appStatusLum.tenant_id and appStatusLum.look_up_type = 'APPROVAL_STATUS'"
							+ " LEFT JOIN t_ledger_definition led ON acd.ledger_ref = led.id "
							+ " LEFT JOIN t_chart_of_account coa ON acd.coa_ref = coa.id "
							+ " LEFT JOIN t_rules rl ON acd.acct_rule_id = rl.id "
							+ "	LEFT JOIN t_rules rl2 ON acd.approval_rule_id = rl2.id "
							+ " LEFT JOIN t_rule_group rg ON acd.acct_group_id = rg.id) det "
							+ " where accounting_status = '"+status+"' "+whereString+" "+paginationCondition;
				}
				log.info("Query to fetch accounting data for outbound api: "+query);
	     	    result=stmt.executeQuery(query);
				ResultSetMetaData rsmd = result.getMetaData();
     	    	int colCount = rsmd.getColumnCount();
	     	    while(result.next()){
					LinkedHashMap mp = new LinkedHashMap();
/*					for(int i=1; i<=colCount; i++)
     	    		{
						mp.put(rsmd.getColumnName(i).toString(), result.getString(i));
     	    		}*/
					Iterator it = finalHeaderColumns.entrySet().iterator();
					while(it.hasNext())
					{
						Map.Entry pair = (Map.Entry)it.next();
				        mp.put(pair.getValue().toString(), result.getString(pair.getKey().toString()));
					}
					mp.remove("accounting_ref_1");
					mp.remove("accounting_ref_2");
					mp.remove("accounting_ref_3");
					mp.remove("accounting_ref_4");
					mp.remove("accounting_ref_5");
					mp.remove("accounting_ref_6");
					mp.remove("accounting_ref_7");
					mp.remove("accounting_ref_8");
					mp.remove("accounting_ref_9");
					mp.remove("accounting_ref_10");
					finalData.add(mp);
	     	    }
			}
			catch(Exception e)
			{
				log.info("Exception while fetching accounting transactions data for(Accounting outbound api) "+e);
			}
			finally
			{
				try{
					if(result != null)
						result.close();
					if(stmt != null)
						stmt.close();
					if(conn != null)
						conn.close();					
				}
				catch(Exception e)
				{
					log.info("Exception while closing statements. "+e);
				}
			}
			return finalData;
		}
		
		public HashMap getUnAcctredGroupingInfo(String groupingColumn, Long viewId, String periodFactor,
				String rangeFrom, String rangeTo, Long groupId, String status, String activityOrNonActivity, String currenciesQuesy, String amountsQuery, String groupBy,
				HashMap currencyMap, HashMap amountMap, Long pageNumber, Long pageSize, String srcOrTrgt)
		{
			HashMap finalMap = new HashMap();
			Connection conn = null;
			Statement stmt = null;
			Statement totalCountStmt = null;
			ResultSet result = null;
			ResultSet totalCountRS = null;
			String rowIdString = "";
			String viewIdString = "";
			if("source".equalsIgnoreCase(srcOrTrgt))
			{
				rowIdString = "original_row_id";
				viewIdString = "original_view_id";
			}
			else if("target".equalsIgnoreCase(srcOrTrgt))
			{
				rowIdString = "target_row_id";
				viewIdString = "target_view_id";				
			}
			DataViews dv = dataViewsRepository.findOne(viewId);
     	    List<HashMap> finalList = new ArrayList<HashMap>();
     	    Long totalCount = 0L;
			try
			{
				String query = "";
    			DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
				conn = ds.getConnection();
				stmt = conn.createStatement();
				totalCountStmt = conn.createStatement();
				if("Y".equalsIgnoreCase(activityOrNonActivity))
				{
					query = "SELECT `"+groupingColumn +"` grouping_column, "+currenciesQuesy+" "+amountsQuery+" count(*) count"
							+ " FROM (SELECT dv.*, 	"
							+ "               (CASE "
							+ "                   WHEN acd.accounting_status IS NULL "
							+ "                        AND recon.recon_reference IS NULL THEN "
							+ "                   'UN ACCOUNTED, NOT RECONCILED' "
							+ "                   WHEN acd.accounting_status IS NULL "
							+ "                        AND recon.recon_reference IS NOT NULL THEN "
							+ "                   'UN ACCOUNTED, RECONCILED' "
							+ "                   WHEN acd.accounting_status IS NOT NULL "
							+ "                        AND recon.recon_reference IS NULL THEN "
							+ "                   'ACCOUNTED, NOT RECONCILED' "
							+ "                   WHEN acd.accounting_status IS NOT NULL "
							+ "                        AND recon.recon_reference IS NOT NULL THEN "
							+ "                   'ACCOUNTED, RECONCILED' "
							+ "                 end)              AS accounting_status,"
							+ "               original_row_id"
							+ "        FROM   (SELECT * "
							+ "                FROM   `"+dv.getDataViewName().toLowerCase()+"` "
							+ "                WHERE  Date(`"+periodFactor+"`) BETWEEN '"+rangeFrom+"' AND '"+rangeTo+"') dv "
							+ "               LEFT JOIN (SELECT su.status ACCOUNTING_STATUS, "
							+ "                                 de.* "
							+ "                          FROM   t_accounted_summary su, "
							+ "                                 t_accounting_data de "
							+ "                          WHERE  su.id = de.accounted_summary_id "
							+ "                                 AND su.rule_group_id = "+groupId+" "
							+ "                                 AND su.view_id = "+viewId+" "
							+ "                                 AND su.current_record_flag IS TRUE) acd "
							+ "                      ON dv.scrids = acd.original_row_id "
							+ "               LEFT JOIN (SELECT recon_reference, "
							+ "                                 "+rowIdString+" AS source_row_id "
							+ "                          FROM   t_reconciliation_result "
							+ "                          WHERE  "+viewIdString+" = "+viewId+" "
							+ "                                 AND recon_status = 'RECONCILED' "
							+ "                                 AND current_record_flag IS TRUE) recon "
							+ "                      ON dv.scrids = recon.source_row_id) det "
							+ " WHERE  accounting_status = '"+status+"' "
							+ " group by grouping_column"+groupBy
							+ " order by grouping_column"+groupBy;
				}
				else if("N".equalsIgnoreCase(activityOrNonActivity))
				{
					query = "SELECT `"+groupingColumn+"` grouping_column, "+currenciesQuesy+" "+amountsQuery+" count(*) count"
							+ " FROM   (SELECT dv.*, "
							+ " (CASE "
							+ " WHEN acd.accounting_status IS NULL THEN 'UN ACCOUNTED' "
							+ " ELSE acd.accounting_status end ) AS accounting_status, "
							+ "               original_row_id"
							+ "        FROM   (SELECT * "
							+ "                FROM   `"+dv.getDataViewName().toLowerCase()+"` "
							+ "                WHERE  Date(`"+periodFactor+"`) BETWEEN '"+rangeFrom+"' AND '"+rangeTo+"') dv "
							+ "               LEFT JOIN (SELECT su.status ACCOUNTING_STATUS,  de.* "
							+ " FROM   t_accounted_summary su, "
							+ "	t_accounting_data de "
							+ " WHERE  su.id = de.accounted_summary_id "
							+ " AND su.rule_group_id = "+groupId+" "
							+ " AND su.view_id = "+viewId+" "
							+ " AND su.current_record_flag IS TRUE) acd "
							+ " ON dv.scrids = acd.original_row_id ) det "
							+ " WHERE  accounting_status = '"+status+"' "
							+ " group by grouping_column "+groupBy
							+ " order by grouping_column "+groupBy;
				}
				String paginationQuery = query+" LIMIT "+pageNumber+", "+pageSize;
				String totalCountQuery = "SELECT COUNT(*) COUNT FROM ("+query+") count";
				log.info("Query For Fetching Un Accounted Grouping Info: "+ query);
	     	    result=stmt.executeQuery(paginationQuery);
	     	    totalCountRS = totalCountStmt.executeQuery(totalCountQuery);
	     	    while(totalCountRS.next())
	     	    {
	     	    	totalCount = totalCount + Long.parseLong(totalCountRS.getString(1));
	     	    }
		 	    while(result.next()){
		 	    	HashMap resultMap = new HashMap();
		 	    	resultMap.put("groupingColumn", result.getString("grouping_column"));
		 	    	resultMap.put("count", Long.parseLong(result.getString("count")));
		 	    	
		 	    	Iterator it = currencyMap.entrySet().iterator();
				    while(it.hasNext()) 
				    {
				        Map.Entry pair = (Map.Entry)it.next();
				        resultMap.put(pair.getKey().toString(), result.getString(pair.getKey().toString()));
				        resultMap.put("currencyColumn", pair.getKey().toString());
				    }
				    Iterator it2 = amountMap.entrySet().iterator();
				    while(it2.hasNext()) 
				    {
				        Map.Entry pair = (Map.Entry)it2.next();
				        resultMap.put(pair.getKey().toString(), result.getString(pair.getKey().toString()));
				    }
		 	    	finalList.add(resultMap);
		 	    }
			}
			catch(Exception e)
			{
				log.info("Exception while fetching un accounted grouping info. "+e);
			}
			finally
			{
				try{
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
					log.info("Exception while closing statements. "+e);
				}
			}
			finalMap.put("summary", finalList);
			finalMap.put("totalCount", totalCount);
			return finalMap;
		}
		
		
}