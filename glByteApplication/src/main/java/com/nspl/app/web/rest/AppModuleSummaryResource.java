package com.nspl.app.web.rest;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.config.ApplicationContextProvider;
import com.nspl.app.domain.AppModuleSummary;
import com.nspl.app.domain.ApplicationPrograms;
import com.nspl.app.domain.DataViews;
import com.nspl.app.domain.DataViewsColumns;
import com.nspl.app.domain.ProcessDetails;
import com.nspl.app.domain.RuleGroup;
import com.nspl.app.domain.RuleGroupDetails;
import com.nspl.app.domain.Rules;
import com.nspl.app.domain.SchedulerDetails;
import com.nspl.app.repository.AppModuleSummaryRepository;
import com.nspl.app.repository.ApplicationProgramsRepository;
import com.nspl.app.repository.DataViewsColumnsRepository;
import com.nspl.app.repository.DataViewsRepository;
import com.nspl.app.repository.JobDetailsRepository;
import com.nspl.app.repository.ProcessDetailsRepository;
import com.nspl.app.repository.RuleGroupDetailsRepository;
import com.nspl.app.repository.RuleGroupRepository;
import com.nspl.app.repository.RulesRepository;
import com.nspl.app.repository.SchedulerDetailsRepository;
import com.nspl.app.repository.SourceFileInbHistoryRepository;
import com.nspl.app.repository.SourceProfileFileAssignmentsRepository;
import com.nspl.app.service.AccountingDataService;
import com.nspl.app.service.DashBoardV4Service;
import com.nspl.app.service.PropertiesUtilService;
import com.nspl.app.service.ReconciliationResultService;
import com.nspl.app.service.UserJdbcService;
import com.nspl.app.web.rest.util.HeaderUtil;
import com.nspl.app.web.rest.util.PaginationUtil;

/**
 * REST controller for managing AppModuleSummary.
 */
@RestController
@RequestMapping("/api")
public class AppModuleSummaryResource {

    private final Logger log = LoggerFactory.getLogger(AppModuleSummaryResource.class);

    private static final String ENTITY_NAME = "appModuleSummary";

    private final AppModuleSummaryRepository appModuleSummaryRepository;
    
    @Inject
    DataViewsColumnsRepository dataViewsColumnsRepository;
    
    @Inject
    RulesRepository rulesRepository;
    
    @Inject
    RuleGroupDetailsRepository ruleGroupDetailsRepository;
    
    @Inject
    RuleGroupRepository ruleGroupRepository;
    
    @Inject
    DataViewsRepository dataViewsRepository;
    
    @Autowired
   	Environment env;
    
    @Inject
	PropertiesUtilService propertiesUtilService;
    
    @Inject
    SourceFileInbHistoryRepository sourceFileInbHistoryRepository;
    
    @Inject
    ProcessDetailsRepository processDetailsRepository;
    
    @Inject
    SourceProfileFileAssignmentsRepository sourceProfileFileAssignmentsRepository;
    
    @Inject
    JobDetailsRepository jobDetailsRepository;
    
    @Inject
    SchedulerDetailsRepository schedulerDetailsRepository;
    
    @Inject
    ReconciliationResultService reconciliationResultService;
    
    @Inject
    ApplicationProgramsRepository applicationProgramsRepository;
    
    @Inject
    UserJdbcService userJdbcService;
    
	@Inject
	AccountingDataService accountingDataService;
	
    @Inject
    DashBoardV4Service dashBoardV4Service;
    
    public AppModuleSummaryResource(AppModuleSummaryRepository appModuleSummaryRepository) {
        this.appModuleSummaryRepository = appModuleSummaryRepository;
    }

    /**
     * POST  /app-module-summaries : Create a new appModuleSummary.
     *
     * @param appModuleSummary the appModuleSummary to create
     * @return the ResponseEntity with status 201 (Created) and with body the new appModuleSummary, or with status 400 (Bad Request) if the appModuleSummary has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/app-module-summaries")
    @Timed
    public ResponseEntity<AppModuleSummary> createAppModuleSummary(@RequestBody AppModuleSummary appModuleSummary) throws URISyntaxException {
        log.debug("REST request to save AppModuleSummary : {}", appModuleSummary);
        if (appModuleSummary.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new appModuleSummary cannot already have an ID")).body(null);
        }
        AppModuleSummary result = appModuleSummaryRepository.save(appModuleSummary);
        return ResponseEntity.created(new URI("/api/app-module-summaries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /app-module-summaries : Updates an existing appModuleSummary.
     *
     * @param appModuleSummary the appModuleSummary to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated appModuleSummary,
     * or with status 400 (Bad Request) if the appModuleSummary is not valid,
     * or with status 500 (Internal Server Error) if the appModuleSummary couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/app-module-summaries")
    @Timed
    public ResponseEntity<AppModuleSummary> updateAppModuleSummary(@RequestBody AppModuleSummary appModuleSummary) throws URISyntaxException {
        log.debug("REST request to update AppModuleSummary : {}", appModuleSummary);
        if (appModuleSummary.getId() == null) {
            return createAppModuleSummary(appModuleSummary);
        }
        AppModuleSummary result = appModuleSummaryRepository.save(appModuleSummary);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, appModuleSummary.getId().toString()))
            .body(result);
    }

    /**
     * GET  /app-module-summaries : get all the appModuleSummaries.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of appModuleSummaries in body
     */
    @GetMapping("/app-module-summaries")
    @Timed
    public ResponseEntity<List<AppModuleSummary>> getAllAppModuleSummaries(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of AppModuleSummaries");
        Page<AppModuleSummary> page = appModuleSummaryRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/app-module-summaries");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /app-module-summaries/:id : get the "id" appModuleSummary.
     *
     * @param id the id of the appModuleSummary to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the appModuleSummary, or with status 404 (Not Found)
     */
    @GetMapping("/app-module-summaries/{id}")
    @Timed
    public ResponseEntity<AppModuleSummary> getAppModuleSummary(@PathVariable Long id) {
        log.debug("REST request to get AppModuleSummary : {}", id);
        AppModuleSummary appModuleSummary = appModuleSummaryRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(appModuleSummary));
    }

    /**
     * DELETE  /app-module-summaries/:id : delete the "id" appModuleSummary.
     *
     * @param id the id of the appModuleSummary to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/app-module-summaries/{id}")
    @Timed
    public ResponseEntity<Void> deleteAppModuleSummary(@PathVariable Long id) {
        log.debug("REST request to delete AppModuleSummary : {}", id);
        appModuleSummaryRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    
   
    @RequestMapping(value = "/postingRecordsInAppModule",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void createAppModuleSummary(@RequestParam Long fileTemp,@RequestParam Long tenantId,@RequestParam Long userId){
    	log.debug("REST request to save AppModuleSummary fileTempId:", fileTemp);
    	List<BigInteger> dvCol=dataViewsColumnsRepository.findDistinctDataViewIdByRefDvTypeAndRefDvColumn("File Template",fileTemp.toString());
    	log.info("dvCol :"+dvCol);
    	//List<AppModuleSummary> appModSumList=new ArrayList<AppModuleSummary>();
    	/*  	String dbUrl=env.getProperty("spring.datasource.url");
    	String[] parts=dbUrl.split("[\\s@&?$+-]+");
    	String host = parts[0].split("/")[2].split(":")[0];
    	log.info("host :"+host);
    	String schemaName=parts[0].split("/")[3];
    	log.info("schemaName :"+schemaName);
    	String userName = env.getProperty("spring.datasource.username");
    	String password = env.getProperty("spring.datasource.password");*/


    	Connection conn = null;        
    	Statement stmtRec = null;
    	ResultSet resultRec=null;

    	try{

    		DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
    		conn = ds.getConnection();
    		log.info("Connected database successfully...");
    		stmtRec = conn.createStatement();



    		for(BigInteger dv:dvCol)
    		{
    			String amountQualifier = reconciliationResultService.getViewColumnQualifier(dv, "AMOUNT");
    			DataViews dvName=dataViewsRepository.findOne(dv.longValue());
    			if(dvName!=null)
    			{
    				String query="";

    				/**to get fileDate or qualifier Date**/

    				String date=dashBoardV4Service.getFileDateOrQualifier(dvName.getId(), tenantId);
    				query="select DATE("+date+") as date, count(*), sum(`"+amountQualifier+"`) from `"+dvName.getDataViewName().toLowerCase().toLowerCase()+"` group by date";

    				log.info("query :"+query);
    				resultRec=stmtRec.executeQuery(query);
    				//Long count=0l;
    				while(resultRec.next())
    				{
    					//count=count+Long.valueOf(resultRec.getString("count(scrIds)"));
    					//}
    					//log.info("count :"+count);
    					Double amount = Double.parseDouble(resultRec.getString(3).toString());
    					//log.info("TenantID: "+ tenantId);
    					List<BigInteger> srcRuleIds=rulesRepository.fetchIdsBySrcViewId(dv.longValue(), tenantId);
    					//log.info("ruleIds :"+srcRuleIds);
    					for(int i=0;i<srcRuleIds.size();i++)
    					{
    						//log.info("***srcRuleIds.get ****"+srcRuleIds.get(i));
    						List<RuleGroupDetails> ruleGrpDetList=ruleGroupDetailsRepository.findByRuleId(srcRuleIds.get(i).longValue());
    						//log.info("ruleGrpDet :"+ruleGrpDetList.size());
    						for(RuleGroupDetails ruleGrpDet:ruleGrpDetList)
    						{
    							//log.info("ruleGrpDet.getRuleGroupId() :"+ruleGrpDet.getRuleGroupId());

    							if(ruleGrpDet !=null)
    							{
    								RuleGroup ruleGrp=ruleGroupRepository.findOne(ruleGrpDet.getRuleGroupId());
    								//log.info("ruleGrp :"+ruleGrp);
    								if(ruleGrp!=null && ruleGrp.getRulePurpose().equalsIgnoreCase("RECONCILIATION"))
    								{
    									Long ruleId = srcRuleIds.get(i).longValue();

    									// Creating Manual Rule For Source
    									//log.info("resultRec.getString(date).toString() :"+resultRec.getString("date").toString());
    									//log.info("ruleGrp.getId() :"+ruleGrp.getId());
    									//log.info("dv.longValue() :"+dv.longValue());
    									AppModuleSummary checkManualExisting=appModuleSummaryRepository.findByFileDateAndModuleAndRuleGroupIdAndRuleIdAndViewIdAndType
    											(LocalDate.parse(resultRec.getString("date").toString()),"Reconciliation",ruleGrp.getId(),0L,Long.valueOf(dv.longValue()),"SOURCE");
    									//log.info("Source checkManualExisting: "+checkManualExisting);
    									if(checkManualExisting == null)
    									{
    										AppModuleSummary appModSum=new AppModuleSummary();
    										appModSum.setRuleId(0L);
    										appModSum.setViewId(dv.longValue());
    										appModSum.setRuleGroupId(ruleGrp.getId());
    										appModSum.setType("SOURCE");
    										appModSum.setModule("RECONCILIATION");
    										appModSum.setCreatedBy(userId);
    										appModSum.setLastUpdatedBy(userId);
    										appModSum.setCreatedDate(ZonedDateTime.now());
    										appModSum.setLastUpdatedDate(ZonedDateTime.now());
    										BigDecimal count=new BigDecimal(resultRec.getString("count(*)").toString());
    										appModSum.setDvCount(count);
    										appModSum.setDvAmount(BigDecimal.valueOf(amount));
    										appModSum.setTypeCount(0L);
    										appModSum.setApprovalCount(0L);
    										appModSum.setInitiatedCount(0L);
    										appModSum.setFileDate(LocalDate.parse(resultRec.getString("date").toString()));
    										appModuleSummaryRepository.save(appModSum);
    										//appModSumList.add(appModSum);
    									}

    									// Creating or updating record with sepecific to rule_id
    									AppModuleSummary checkExisting=appModuleSummaryRepository.findByFileDateAndModuleAndRuleGroupIdAndRuleIdAndViewIdAndType
    											(LocalDate.parse(resultRec.getString("date").toString()),"Reconciliation",ruleGrp.getId(),ruleId,Long.valueOf(dv.longValue()),"SOURCE");
    									//log.info("Creating or updating record with sepecific to rule_id checkExisting :"+checkExisting);
    									if(checkExisting!=null)
    									{
    										//log.info("******");
    										BigDecimal count=new BigDecimal(resultRec.getString("count(*)").toString());
    										checkExisting.setDvCount(count);
    										checkExisting.setDvAmount(BigDecimal.valueOf(amount));
    										checkExisting.setLastUpdatedBy(userId);
    										checkExisting.setLastUpdatedDate(ZonedDateTime.now());
    										appModuleSummaryRepository.save(checkExisting);
    									}
    									else
    									{
    										//log.info("######");
    										AppModuleSummary appModSum=new AppModuleSummary();
    										appModSum.setRuleId(ruleId);
    										appModSum.setViewId(dv.longValue());
    										appModSum.setRuleGroupId(ruleGrp.getId());
    										appModSum.setType("SOURCE");
    										appModSum.setModule("RECONCILIATION");
    										appModSum.setCreatedBy(userId);
    										appModSum.setLastUpdatedBy(userId);
    										appModSum.setCreatedDate(ZonedDateTime.now());
    										appModSum.setLastUpdatedDate(ZonedDateTime.now());
    										BigDecimal count=new BigDecimal(resultRec.getString("count(*)").toString());
    										appModSum.setDvCount(count);
    										appModSum.setDvAmount(BigDecimal.valueOf(amount));
    										appModSum.setTypeCount(0L);
    										appModSum.setApprovalCount(0L);
    										appModSum.setInitiatedCount(0L);
    										appModSum.setFileDate(LocalDate.parse(resultRec.getString("date").toString()));
    										appModuleSummaryRepository.save(appModSum);
    										//log.info("appModSum at :"+ruleId+"**"+appModSum);

    										//appModSumList.add(appModSum);
    									}
    								}
    								else if(ruleGrp!=null && ruleGrp.getRulePurpose().equalsIgnoreCase("ACCOUNTING"))
    								{
    									List<String> acctString=new ArrayList<String>();
    									acctString.add("ACCOUNTED");
    									acctString.add("IN_PROCESS");
    									acctString.add("JOURNALS_ENTERED");
    									acctString.add("UN_ACCOUNTED");



    									for(String acct:acctString )
    									{
    										if("Accounted".equalsIgnoreCase(acct))
    										{
    											AppModuleSummary checkManualExisting=appModuleSummaryRepository.findByFileDateAndModuleAndRuleGroupIdAndRuleIdAndViewId
    													(LocalDate.parse(resultRec.getString("date").toString()),"Accounting",ruleGrp.getId(),0L,Long.valueOf(dv.longValue()));
    											//log.info("Accounted checkManualExisting: "+checkManualExisting);
    											if(checkManualExisting == null)
    											{
    												AppModuleSummary appModSum=new AppModuleSummary();
    												appModSum.setRuleId(0L);
    												appModSum.setViewId(dv.longValue());
    												appModSum.setRuleGroupId(ruleGrp.getId());
    												appModSum.setType(acct);
    												appModSum.setModule("ACCOUNTING");
    												appModSum.setCreatedBy(userId);
    												appModSum.setLastUpdatedBy(userId);
    												appModSum.setCreatedDate(ZonedDateTime.now());
    												appModSum.setLastUpdatedDate(ZonedDateTime.now());
    												BigDecimal count=new BigDecimal(resultRec.getString("count(*)").toString());
    												appModSum.setDvCount(count);
    												appModSum.setDvAmount(BigDecimal.valueOf(amount));
    												appModSum.setTypeCount(0L);
    												appModSum.setApprovalCount(0L);
    												appModSum.setInitiatedCount(0L);
    												appModSum.setFileDate(LocalDate.parse(resultRec.getString("date").toString()));
    												AppModuleSummary save = appModuleSummaryRepository.save(appModSum);
    												//appModSumList.add(appModSum);
    											}

    										}
    										if("Not accounted".equalsIgnoreCase(acct))
    										{
    											AppModuleSummary checkManualExisting=appModuleSummaryRepository.findByFileDateAndModuleAndRuleGroupIdAndViewIdAndTypeAndRuleIdIsNull
    													(LocalDate.parse(resultRec.getString("date").toString()),"Accounting",ruleGrp.getId(),Long.valueOf(dv.longValue()),acct);
    											//	log.info("checkManualExisting: "+checkManualExisting);
    											if(checkManualExisting == null)
    											{
    												AppModuleSummary appModSum=new AppModuleSummary();
    												appModSum.setRuleId(null);
    												appModSum.setViewId(dv.longValue());
    												appModSum.setRuleGroupId(ruleGrp.getId());
    												appModSum.setModule("ACCOUNTING");
    												appModSum.setFileDate(LocalDate.parse(resultRec.getString("date").toString()));
    												appModSum.setType(acct);
    												appModSum.setCreatedBy(userId);
    												appModSum.setLastUpdatedBy(userId);
    												appModSum.setCreatedDate(ZonedDateTime.now());
    												appModSum.setLastUpdatedDate(ZonedDateTime.now());
    												BigDecimal count=new BigDecimal(resultRec.getString("count(*)").toString());
    												appModSum.setDvCount(count);
    												appModSum.setDvAmount(BigDecimal.valueOf(amount));
    												appModSum.setTypeCount(0L);
    												appModSum.setApprovalCount(0L);
    												appModSum.setInitiatedCount(0L);
    												AppModuleSummary save = appModuleSummaryRepository.save(appModSum);
    												//appModSumList.add(appModSum);
    											}

    										}
    										else
    										{
    											AppModuleSummary checkExisting=appModuleSummaryRepository.findByFileDateAndModuleAndRuleGroupIdAndRuleIdAndViewIdAndType
    													(LocalDate.parse(resultRec.getString("date").toString()),"Accounting",ruleGrp.getId(),Long.valueOf(srcRuleIds.get(i).longValue()),Long.valueOf(dv.longValue()),acct);

    											if(checkExisting!=null)
    											{
    												BigDecimal count=new BigDecimal(resultRec.getString("count(*)").toString());


    												if(acct.equalsIgnoreCase("UN_ACCOUNTED"))
    												{
    													log.info("checkExisting for unaccounted :"+checkExisting);

    													BigDecimal existCt=checkExisting.getDvCount();
    													BigDecimal existAmt=checkExisting.getDvAmount();
    													//log.info("count in unaccounted1 :"+count);
    													//log.info("existCt in unaccounted2 :"+existCt);
    													BigDecimal updateTypeCount=count.subtract(existCt);
    													BigDecimal updateTypeAmount=BigDecimal.valueOf(amount).subtract(existAmt);

    													Long updateTypeCt=checkExisting.getTypeCount()+updateTypeCount.longValue();
    													//log.info("updateTypeCt3 :"+updateTypeCt);
    													BigDecimal updateTypeAmt=BigDecimal.ZERO;
    													if(checkExisting.getTypeAmount()!=null)
    													{
    														log.info("amount in unaccounted :"+amount);
    														log.info("updateTypeAmount in unaccounted :"+updateTypeAmount);
    														updateTypeAmt=checkExisting.getTypeAmount().add(updateTypeAmount);
    													}
    													else
    														updateTypeAmt=updateTypeAmount;
    													checkExisting.setDvCount(count);
    													checkExisting.setDvAmount(BigDecimal.valueOf(amount));
    													checkExisting.setTypeCount(updateTypeCt);
    													checkExisting.setTypeAmount(updateTypeAmt);
    												}
    												checkExisting.setLastUpdatedBy(userId);
    												checkExisting.setLastUpdatedDate(ZonedDateTime.now());
    												appModuleSummaryRepository.save(checkExisting);
    											}
    											else
    											{
    												//for(int act=0;act<=3;act++)
    												//{
    												AppModuleSummary appModSum=new AppModuleSummary();
    												appModSum.setRuleId(srcRuleIds.get(i).longValue());
    												appModSum.setViewId(dv.longValue());
    												appModSum.setRuleGroupId(ruleGrp.getId());
    												/*if(act==0)
        												appModSum.setType("Accounted");
        											else if(act==1)
        												appModSum.setType("Accounting inprocess");
        											else if(act==2)
        												appModSum.setType("Final accounted");
        											else if(act==3)
        												appModSum.setType("Not accounted");*/
    												appModSum.setModule("ACCOUNTING");
    												appModSum.setFileDate(LocalDate.parse(resultRec.getString("date").toString()));
    												appModSum.setType(acct);
    												appModSum.setCreatedBy(userId);
    												appModSum.setLastUpdatedBy(userId);
    												appModSum.setCreatedDate(ZonedDateTime.now());
    												appModSum.setLastUpdatedDate(ZonedDateTime.now());
    												BigDecimal count=new BigDecimal(resultRec.getString("count(*)").toString());
    												appModSum.setDvCount(count);
    												appModSum.setDvAmount(BigDecimal.valueOf(amount));
    												appModSum.setTypeCount(0L);
    												if(acct.equalsIgnoreCase("UN_ACCOUNTED"))
    												{
    													appModSum.setTypeCount(count.longValue());
    													appModSum.setTypeAmount(BigDecimal.valueOf(amount));
    												}
    												appModSum.setApprovalCount(0L);
    												appModSum.setInitiatedCount(0L);
    												appModuleSummaryRepository.save(appModSum);
    												//appModSumList.add(appModSum);
    												//}
    											}
    										}

    									}
    								}
    							}
    						}
    					}
    					List<BigInteger> trgRuleIds=rulesRepository.fetchIdsByTrgViewId(dv.longValue(), tenantId);
    					//log.info("target ruleIds :"+trgRuleIds);
    					for(int i=0;i<trgRuleIds.size();i++)
    					{
    						//log.info("***trgRuleIds.get ****"+trgRuleIds.get(i));
    						AppModuleSummary appModSum=new AppModuleSummary();
    						appModSum.setRuleId(trgRuleIds.get(i).longValue());
    						List<RuleGroupDetails> ruleGrpDetList=ruleGroupDetailsRepository.findByRuleId(trgRuleIds.get(i).longValue());
    						//log.info("ruleGrpDet :"+ruleGrpDetList.size());
    						for(RuleGroupDetails ruleGrpDet:ruleGrpDetList)
    						{
    							if(ruleGrpDet !=null)
    							{
    								RuleGroup ruleGrp=ruleGroupRepository.findOne(ruleGrpDet.getRuleGroupId());
    								//log.info("ruleGrp :"+ruleGrp);
    								if(ruleGrp!=null)
    								{
    								AppModuleSummary checkExisting=appModuleSummaryRepository.findByFileDateAndModuleAndRuleGroupIdAndRuleIdAndViewIdAndType
    										(LocalDate.parse(resultRec.getString("date").toString()),"Reconciliation",ruleGrp.getId(),Long.valueOf(trgRuleIds.get(i).longValue()),Long.valueOf(dv.longValue()),"TARGET");

    								// Checking maunal record for target
    								AppModuleSummary checkManualExisting=appModuleSummaryRepository.findByFileDateAndModuleAndRuleGroupIdAndRuleIdAndViewIdAndType
    										(LocalDate.parse(resultRec.getString("date").toString()),"Reconciliation",ruleGrp.getId(),0L,Long.valueOf(dv.longValue()),"TARGET");
    								if(checkManualExisting == null)
    								{
    									AppModuleSummary appModSumManualRec=new AppModuleSummary();
    									appModSumManualRec.setRuleId(0L);
    									if(ruleGrp!=null && ruleGrp.getRulePurpose().equalsIgnoreCase("RECONCILIATION"))
    									{
    										appModSumManualRec.setRuleGroupId(ruleGrp.getId());
    										appModSumManualRec.setType("TARGET");
    										appModSumManualRec.setViewId(dv.longValue());
    										appModSumManualRec.setModule("RECONCILIATION");
    										appModSumManualRec.setCreatedBy(userId);
    										appModSumManualRec.setLastUpdatedBy(userId);
    										appModSumManualRec.setCreatedDate(ZonedDateTime.now());
    										appModSumManualRec.setLastUpdatedDate(ZonedDateTime.now());
    										BigDecimal count=new BigDecimal(resultRec.getString("count(*)").toString());
    										appModSumManualRec.setDvCount(count);
    										appModSumManualRec.setTypeCount(0L);
    										appModSumManualRec.setApprovalCount(0L);
    										appModSumManualRec.setInitiatedCount(0L);
    										appModSumManualRec.setDvAmount(BigDecimal.valueOf(amount));
    										appModSumManualRec.setFileDate(LocalDate.parse(resultRec.getString("date").toString()));
    										AppModuleSummary save = appModuleSummaryRepository.save(appModSumManualRec);
    										//appModSumList.add(appModSum);
    									}
    								}

    								//log.info("checkExisting trg :"+checkExisting);
    								if(checkExisting!=null)
    								{
    									BigDecimal count=new BigDecimal(resultRec.getString("count(*)").toString());
    									checkExisting.setDvCount(count);
    									checkExisting.setDvAmount(BigDecimal.valueOf(amount));
    									checkExisting.setLastUpdatedBy(userId);
    									checkExisting.setLastUpdatedDate(ZonedDateTime.now());
    									appModuleSummaryRepository.save(checkExisting);
    								}
    								else
    								{
    									if(ruleGrp!=null && ruleGrp.getRulePurpose().equalsIgnoreCase("RECONCILIATION"))
    									{
    										//log.info("new target record in else:");
    										appModSum.setRuleGroupId(ruleGrp.getId());
    										appModSum.setType("TARGET");
    										appModSum.setViewId(dv.longValue());

    										appModSum.setModule("RECONCILIATION");
    										appModSum.setCreatedBy(userId);
    										appModSum.setLastUpdatedBy(userId);
    										appModSum.setCreatedDate(ZonedDateTime.now());
    										appModSum.setLastUpdatedDate(ZonedDateTime.now());
    										BigDecimal count=new BigDecimal(resultRec.getString("count(*)").toString());
    										appModSum.setDvCount(count);
    										appModSum.setTypeCount(0L);
    										appModSum.setApprovalCount(0L);
    										appModSum.setInitiatedCount(0L);
    										appModSum.setDvAmount(BigDecimal.valueOf(amount));
    										appModSum.setFileDate(LocalDate.parse(resultRec.getString("date").toString()));
    										//log.info("appModSum :"+appModSum);
    										if(!ruleGrp.getRulePurpose().equalsIgnoreCase("APPROVALS"))
    											appModuleSummaryRepository.save(appModSum);
    									}
    								}
    							}
    							}
    						}

    					}
    				}




    				// Creating record for Not Accounted with rule_id null

    			}

    		}
    	}
    	catch(SQLException e)
    	{

    	}
    	finally
    	{
    		if(resultRec!=null)
    			try {
    				resultRec.close();
    			} catch (SQLException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    		if(stmtRec!=null)
    			try {
    				stmtRec.close();
    			} catch (SQLException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    		if(conn!=null)
    			try {
    				conn.close();
    			} catch (SQLException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    	}
    	//appModSumList=appModuleSummaryRepository.save(appModSumList);
    	//log.info("appModSumList :"+appModSumList);
    }
    
    
    @PostMapping("/getSummaryInfoForReconciliation")
    @Timed 
    public LinkedHashMap getSummaryInfoForReconciliation(@RequestParam Long processId ,@RequestBody HashMap dates) throws SQLException
    {
    	LinkedHashMap finalMap=new LinkedHashMap();
    	List<LinkedHashMap> dataMap=new ArrayList<LinkedHashMap>();
    	Properties props =  propertiesUtilService.getPropertiesFromClasspath("File.properties");
    /*	String recRuleGroupId=props.getProperty("reconciliationRuleGroup");
    	log.info("recRuleGroup :"+recRuleGroupId);*/
    	
    	ProcessDetails procesDet=processDetailsRepository.findByProcessIdAndTagType(processId, "reconciliationRuleGroup");
    	if(procesDet!=null)
    	{
    	ZonedDateTime fmDate=ZonedDateTime.parse(dates.get("startDate").toString());
    	ZonedDateTime toDate=ZonedDateTime.parse(dates.get("endDate").toString());
    	log.info("fmDate :"+fmDate);
    	log.info("toDate :"+toDate);
    	java.time.LocalDate fDate=fmDate.toLocalDate();
    	java.time.LocalDate tDate=toDate.toLocalDate();
    	List<String> rulesList=new ArrayList<String>();
    //	List<Object[]> reconSummary=appModuleSummaryRepository.fetchRecCountsByGroupIdAndFileDate(Long.valueOf(recRuleGroupId),fDate,tDate);
    	
    	List<Object[]> reconSummary=appModuleSummaryRepository.fetchRecCountsByGroupIdAndFileDate(procesDet.getTypeId(),fDate,tDate);
    	for(int i=0;i<reconSummary.size();i++)
    	{
    		List<Object>  data=new ArrayList<Object>();
    		LinkedHashMap map=new LinkedHashMap();
    		map.put("DvCount", reconSummary.get(i)[0]);
    		map.put("ReconciledCount", reconSummary.get(i)[1]);
    		map.put("unReconciledCount", reconSummary.get(i)[2]);
    		map.put("reconciledPer", reconSummary.get(i)[3]);
    		data.add(reconSummary.get(i)[3]);
    		map.put("unReconciledPer", reconSummary.get(i)[4]);
    		data.add(reconSummary.get(i)[4]);
    		map.put("dvType", reconSummary.get(i)[5]);
    		map.put("ruleId", reconSummary.get(i)[6]);
    		map.put("viewId", reconSummary.get(i)[7]);
    		
    		
    		
    		Rules rule=rulesRepository.findOne(Long.valueOf(reconSummary.get(i)[6].toString()));
    		map.put("stack", rule.getRuleCode());
    		if(!rulesList.contains(rule.getRuleCode()))
    			rulesList.add(rule.getRuleCode());
    		DataViews dv=dataViewsRepository.findOne(Long.valueOf(reconSummary.get(i)[7].toString()));
    		map.put("label", dv.getDataViewName());
    		
    	
    		
			log.info("data :"+data);
			map.put("data",data);
			dataMap.add(map);
    		
    	}
    	log.info("rulesList :"+rulesList);
    	finalMap.put("rulesList", rulesList);
    }
    	
    	finalMap.put("reconciliationData", dataMap);
    	
		return finalMap;
    }
 
    
    @PostMapping("/getAgingAnalsisForUnReconciliation")
    @Timed 
    public List<LinkedHashMap> getAgingAnalsisForUnReconciliation(@RequestBody HashMap paramMap)
    {
    	List<LinkedHashMap> finalMap=new ArrayList<LinkedHashMap>();
    	Properties props =  propertiesUtilService.getPropertiesFromClasspath("File.properties");
    	/*String recRuleGroupId=props.getProperty("reconciliationRuleGroup");
    	log.info("recRuleGroup :"+recRuleGroupId);*/
    	Long processId=Long.valueOf(paramMap.get("processId").toString());
    	Long ruleId=Long.valueOf(paramMap.get("ruleId").toString());
    	Long viewId=Long.valueOf(paramMap.get("viewId").toString());
    	

    	ProcessDetails procesDet=processDetailsRepository.findByProcessIdAndTagType(processId, "reconciliationRuleGroup");
    	if(procesDet!=null)
    	{    	
    		ZonedDateTime fmDate=ZonedDateTime.parse(paramMap.get("startDate").toString());
    		ZonedDateTime toDate=ZonedDateTime.parse(paramMap.get("endDate").toString());
    		log.info("fmDate :"+fmDate);
    		log.info("toDate :"+toDate);
    		java.time.LocalDate fDate=fmDate.toLocalDate();
    		java.time.LocalDate tDate=toDate.toLocalDate();

    	//	List<Object[]> reconSummary=appModuleSummaryRepository.fetchAgingForUnReconCount(Long.valueOf(recRuleGroupId),fDate,tDate);
    		
    		List<Object[]> reconSummary=appModuleSummaryRepository.fetchAgingForUnReconCount(Long.valueOf(procesDet.getTypeId()),fDate,tDate,ruleId,viewId);

    		for(int i=0;i<reconSummary.size();i++)
    		{
    			LinkedHashMap map=new LinkedHashMap();
    			if(reconSummary.get(i)[0].equals(0))
    				map.put("age", 0);
    			else
    				map.put("age", reconSummary.get(i)[4]);
    			map.put("count", reconSummary.get(i)[2]);
    			map.put("percentage", reconSummary.get(i)[3]);

    			finalMap.add(map);

    		}
    	}
    	return finalMap;
    }
    
    
    
    
    @PostMapping("/getEachDayAnalysisForReconciliation")
    @Timed
    public List<LinkedHashMap> getEachDayAnalysisForReconciliation(@RequestParam Long processId,@RequestBody HashMap dates) throws SQLException 
    {
    	log.info("Rest Request to get aging analysis :"+dates);

    	Properties props =  propertiesUtilService.getPropertiesFromClasspath("File.properties");




    	/*String recRuleGroupId=props.getProperty("reconciliationRuleGroup");
    	log.info("recRuleGroup :"+recRuleGroupId);*/


    	ProcessDetails procesDet=processDetailsRepository.findByProcessIdAndTagType(processId, "reconciliationRuleGroup");

    	ZonedDateTime fmDate=ZonedDateTime.parse(dates.get("startDate").toString());
    	ZonedDateTime toDate=ZonedDateTime.parse(dates.get("endDate").toString());
    	log.info("fmDate :"+fmDate);
    	log.info("toDate :"+toDate);
    	java.time.LocalDate fDate=fmDate.toLocalDate();
    	java.time.LocalDate tDate=toDate.toLocalDate();
    	log.info("fDate :"+fDate);
    	log.info("tDate :"+tDate);



    	//	DataViews dv=dataViewsRepository.findOne(dvId);
    	log.info("fDate :"+fDate);
    	log.info("tDate :"+tDate.plusDays(1));
    	List<LinkedHashMap> finalMap=new ArrayList<LinkedHashMap>();
    	if(procesDet!=null)
    	{
    		while(fDate.isBefore(tDate.plusDays(1)))
    		{
    			log.info("in while fDate :"+fDate);
    			LinkedHashMap map=new LinkedHashMap();
    			List< Object[]> reconSummary=appModuleSummaryRepository.fetchReconCountAndUnReconciledCountPerDay(Long.valueOf(procesDet.getTypeId()),fDate);
    			for(int i=0;i<reconSummary.size();i++)
    			{
    				log.info("reconSummary :"+reconSummary.size());
    				if(reconSummary!=null)
    				{
    					map.put("date", fDate);
    					if(reconSummary.get(i)[2]!=null)
    						map.put("reconciledPer", reconSummary.get(i)[2]);
    					else
    						map.put("reconciledPer", "");
    					if(reconSummary.get(i)[3]!=null)
    						map.put("unReconciledPer", reconSummary.get(i)[3]);
    					else
    						map.put("unReconciledPer", "");
    					finalMap.add(map);
    					fDate=fDate.plusDays(1);
    					log.info("end of while fDate :"+fDate);
    				}
    			}
    		}
    	}

    	return finalMap;


    }
    
    
    
    /*  @PostMapping("/getSummaryInfoForAccounting")
      @Timed 
      public List<LinkedHashMap> getSummaryInfoForAccounting(@RequestParam String process ,@RequestBody HashMap dates) throws SQLException
      {
      	
      	List<LinkedHashMap> finalMap=new ArrayList<LinkedHashMap>();
      	Properties props =  propertiesUtilService.getPropertiesFromClasspath("File.properties");
      	String accountingRuleGroup=props.getProperty("accountingRuleGroup");
      	log.info("accountingRuleGroup :"+accountingRuleGroup);
      	
      	ZonedDateTime fmDate=ZonedDateTime.parse(dates.get("startDate").toString());
      	ZonedDateTime toDate=ZonedDateTime.parse(dates.get("endDate").toString());
      	log.info("fmDate :"+fmDate);
      	log.info("toDate :"+toDate);
      	java.time.LocalDate fDate=fmDate.toLocalDate();
      	java.time.LocalDate tDate=toDate.toLocalDate();
      	
      	List<Object[]> accountingSummary=appModuleSummaryRepository.fetchActCountsByGroupIdAndFileDate(Long.valueOf(accountingRuleGroup),fDate,tDate);
      	for(int i=0;i<accountingSummary.size();i++)
      	{
      		List<Object>  data=new ArrayList<Object>();
      		LinkedHashMap map=new LinkedHashMap();
      		map.put("DvCount", accountingSummary.get(i)[0]);
      		map.put("typeCount", accountingSummary.get(i)[1]);
      		map.put("type", accountingSummary.get(i)[2]);
      		map.put("ruleId", accountingSummary.get(i)[3]);
      		map.put("viewId", accountingSummary.get(i)[4]);
      		
      		
      		
      		Rules rule=rulesRepository.findOne(Long.valueOf(accountingSummary.get(i)[3].toString()));
      		map.put("stack", rule.getRuleCode());
      		DataViews dv=dataViewsRepository.findOne(Long.valueOf(accountingSummary.get(i)[4].toString()));
      		map.put("label", dv.getDataViewName());
      		
      	
      		
  			log.info("data :"+data);
  			//map.put("data",data);
  			finalMap.add(map);
      		
      	}
  		return finalMap;
      }*/
      
      
      /**
       * author:ravali
       * (current working)
       * @param process
       * @param dates
       * @return
       * @throws SQLException
       */
      @PostMapping("/getSummaryInfoForAccounted")
      @Timed 
      public LinkedHashMap getSummaryInfoForAccounted(@RequestParam Long processId ,@RequestBody HashMap dates) throws SQLException
      {

      	LinkedHashMap accountedSummary=new LinkedHashMap();
      	Properties props =  propertiesUtilService.getPropertiesFromClasspath("File.properties");
      	/*String accountingRuleGroup=props.getProperty("accountingRuleGroup");
      	log.info("accountingRuleGroup :"+accountingRuleGroup);*/
      	
    	ProcessDetails procesDet=processDetailsRepository.findByProcessIdAndTagType(processId, "accountingRuleGroup");

    	
      	ZonedDateTime fmDate=ZonedDateTime.parse(dates.get("startDate").toString());
      	ZonedDateTime toDate=ZonedDateTime.parse(dates.get("endDate").toString());
      	log.info("fmDate :"+fmDate);
      	log.info("toDate :"+toDate);
      	java.time.LocalDate fDate=fmDate.toLocalDate();
      	java.time.LocalDate tDate=toDate.toLocalDate();
    	
      	if(procesDet!=null)
    	{
      		List<Object> dvCount=new ArrayList<Object>();
          	List<String> ruleNames=new ArrayList<String>();
          	List<String> dvNames=new ArrayList<String>();
      	List<String> distType=appModuleSummaryRepository.findDistinctTypeByRuleGroupId(procesDet.getTypeId());
      	log.info("distType :"+distType);
      
      	List<LinkedHashMap> finalMap=new ArrayList<LinkedHashMap>();
      	for(String type:distType)
      	{
      		List<Object> percent=new ArrayList<Object>();

      		List<Object> typeCount=new ArrayList<Object>();

      		LinkedHashMap map=new LinkedHashMap();
      		map.put("label", type);
      		List<Object[]> accountingSummary=appModuleSummaryRepository.fetchActCountsByGroupIdAndFileDate(Long.valueOf(procesDet.getTypeId()),type,fDate,tDate);
      		for(int i=0;i<accountingSummary.size();i++)
      		{
      			if(i==0)
      				dvCount.add(accountingSummary.get(i)[0]);
      			percent.add(accountingSummary.get(i)[2]);
      			typeCount.add(accountingSummary.get(i)[1]);

      			Rules rule=rulesRepository.findOne(Long.valueOf(accountingSummary.get(i)[4].toString()));
      			if(!ruleNames.contains(rule.getRuleCode()))
      				ruleNames.add(rule.getRuleCode());
      			DataViews dv=dataViewsRepository.findOne(Long.valueOf(accountingSummary.get(i)[5].toString()));
      			if(!dvNames.contains(dv.getDataViewName()))
      				dvNames.add(dv.getDataViewName());

      		}
      		log.info("percent :"+percent);
      		map.put("data", percent);
      		log.info("typeCount :"+typeCount);
      		map.put("typeCount", typeCount);
      		finalMap.add(map);
      	}
      
      	log.info("ruleNames :"+ruleNames);
      	log.info("dvNames :"+dvNames);
      	log.info("dvCount :"+dvCount);
      	accountedSummary.put("accountingData", finalMap);
      	accountedSummary.put("rulesList", ruleNames);
      	accountedSummary.put("dvList", dvNames);
      	accountedSummary.put("dvCount", dvCount);
    	}

      	return accountedSummary;
      }
      
      
      @PostMapping("/getAgingAnalsisForAccountingSummary")
      @Timed 
      public List<LinkedHashMap> getAgingAnalsisForUnAccounting(@RequestBody HashMap paramMap)
      {
    	  List<LinkedHashMap> finalMap=new ArrayList<LinkedHashMap>();
    	  Properties props =  propertiesUtilService.getPropertiesFromClasspath("File.properties");
    	  /*String actRuleGroupId=props.getProperty("accountingRuleGroup");
    	  log.info("actRuleGroupId :"+actRuleGroupId);*/
    	  
    	  Long processId=Long.valueOf(paramMap.get("processId").toString());
    	  String type=paramMap.get("status").toString();

    	  ZonedDateTime fmDate=ZonedDateTime.parse(paramMap.get("startDate").toString());
    	  ZonedDateTime toDate=ZonedDateTime.parse(paramMap.get("endDate").toString());
    	  log.info("fmDate :"+fmDate);
    	  log.info("toDate :"+toDate);
    	  java.time.LocalDate fDate=fmDate.toLocalDate();
    	  java.time.LocalDate tDate=toDate.toLocalDate();

    	  ProcessDetails procesDet=processDetailsRepository.findByProcessIdAndTagType(processId, "accountingRuleGroup");
    	  if(procesDet!=null)
    	  {

    		  List<Object[]> actSummary=appModuleSummaryRepository.fetchAgingForAccountingSummary(procesDet.getTypeId(),type,fDate,tDate);
    		  for(int i=0;i<actSummary.size();i++)
    		  {
    			  LinkedHashMap map=new LinkedHashMap();
    			  if(actSummary.get(i)[0].equals(0))
    				  map.put("age", 0);
    			  else
    				  map.put("age", actSummary.get(i)[4]);
    			  map.put("count", actSummary.get(i)[2]);
    			  map.put("percentage", actSummary.get(i)[3]);

    			  finalMap.add(map);

    		  }
    	  }
    	  return finalMap;
      }
      
      
    
      
      
      
      @PostMapping("/getEachDayAnalysisForAccounting")
      @Timed
      public List<LinkedHashMap> getEachDayAnalysisForAccounting(@RequestParam Long processId,@RequestParam String type,@RequestBody HashMap dates) throws SQLException 
      {
    	  log.info("Rest Request to get aging analysis :"+dates);

    	  Properties props =  propertiesUtilService.getPropertiesFromClasspath("File.properties");


    	  List<LinkedHashMap> finalMap=new ArrayList<LinkedHashMap>();

    	  String actRuleGroupId=props.getProperty("accountingRuleGroup");
    	  log.info("actRuleGroupId :"+actRuleGroupId);


    	  ProcessDetails procesDet=processDetailsRepository.findByProcessIdAndTagType(processId, "accountingRuleGroup");
    	  if(procesDet!=null)
    	  {
    		  ZonedDateTime fmDate=ZonedDateTime.parse(dates.get("startDate").toString());
    		  ZonedDateTime toDate=ZonedDateTime.parse(dates.get("endDate").toString());
    		  log.info("fmDate :"+fmDate);
    		  log.info("toDate :"+toDate);
    		  java.time.LocalDate fDate=fmDate.toLocalDate();
    		  java.time.LocalDate tDate=toDate.toLocalDate();
    		  log.info("fDate :"+fDate);
    		  log.info("tDate :"+tDate);



    		  //	DataViews dv=dataViewsRepository.findOne(dvId);
    		  log.info("fDate :"+fDate);
    		  log.info("tDate :"+tDate.plusDays(1));
    		

    		  while(fDate.isBefore(tDate.plusDays(1)))
    		  {
    			  log.info("in while fDate :"+fDate);
    			  LinkedHashMap map=new LinkedHashMap();
    			  List< Object[]> actSummary=appModuleSummaryRepository.fetchProcessedAndUnProcessedCountPerDay(Long.valueOf(actRuleGroupId),type,fDate);
    			  for(int i=0;i<actSummary.size();i++)
    			  {
    				  log.info("reconSummary :"+actSummary.size());
    				  if(actSummary!=null)
    				  {
    					  map.put("date", fDate);
    					  if(actSummary.get(i)[2]!=null)
    						  map.put(type.replaceAll("\\s", "")+"Per", actSummary.get(i)[2]);
    					  else
    						  map.put(type.replaceAll("\\s", "")+"Per", "");

    					  finalMap.add(map);
    					  fDate=fDate.plusDays(1);
    					  log.info("end of while fDate :"+fDate);
    				  }
    			  }
    		  }
    	  }
    	  return finalMap;


      }
      
    
      
      @PostMapping("/getEachDayAnalysisForTransformation")
      @Timed
      public List<LinkedHashMap> getEachDayAnalysisForTransformation(@RequestParam Long processId,@RequestBody HashMap dates) throws SQLException 
      {
      	log.info("Rest Request to get aging analysis :"+dates);
      
      	Properties props =  propertiesUtilService.getPropertiesFromClasspath("File.properties");

      	
      	/*Long dvId =Long.valueOf(agingAnalysisParameters.get("dvId").toString());*/

      	/*String profiles=props.getProperty("profiles");
      	log.info("profiles :"+profiles.replaceAll("\\[", "").replaceAll("\\]", ""));*/
      	List<BigInteger> procDetails=processDetailsRepository.findTypeIdByProcessIdAndTagType(processId, "sourceProfile");

      	ZonedDateTime fmDate=ZonedDateTime.parse(dates.get("startDate").toString());
      	ZonedDateTime toDate=ZonedDateTime.parse(dates.get("endDate").toString());
      	log.info("fmDate :"+fmDate);
      	log.info("toDate :"+toDate);
      	java.time.LocalDate fDate=fmDate.toLocalDate();
      	java.time.LocalDate tDate=toDate.toLocalDate();
      	log.info("fDate :"+fDate);
      	log.info("tDate :"+tDate);
      


      //	DataViews dv=dataViewsRepository.findOne(dvId);
      	log.info("fDate :"+fDate);
      	log.info("tDate :"+tDate.plusDays(1));
      	List<LinkedHashMap> finalMap=new ArrayList<LinkedHashMap>();
    
         while(fDate.isBefore(tDate.plusDays(1)))
         {
      	   log.info("in while fDate :"+fDate.toString());
      	   LinkedHashMap map=new LinkedHashMap();
      	 List< Object[]> transformedSummary=sourceFileInbHistoryRepository.fetchTransfomedCount(procDetails,fDate.toString()+"%");
      	 if(transformedSummary!=null)
     	  {
      	 for(int i=0;i<transformedSummary.size();i++)
      	 {
      	  log.info("transformedSummary :"+transformedSummary.size());
      	 
      	  map.put("date", fDate);
      	
      	  if(transformedSummary.get(i)!=null)
      	  map.put("transformedPer", transformedSummary.get(i)[2]);
      	  else
      		  map.put("transformedPer", "");
      	  if(transformedSummary.get(i)!=null)
      	  map.put("ntTransformedPer", transformedSummary.get(i)[3]);
      	  else
      		  map.put("ntTransformedPer", "");
      	  finalMap.add(map);
      	  fDate=fDate.plusDays(1);
      	  log.info("end of while fDate :"+fDate);
      	  }
      	  }
         }
      	
      	return finalMap;


      }
      
      /**
       * author ravali
       * (pie chart)
       * @param process
       * @param dates
       * @return
       * @throws SQLException
       */
      @PostMapping("/getAgingAnalysisForTransformation")
      @Timed
      public List<LinkedHashMap> getAgingAnalysisForTransformation(@RequestBody HashMap paramMap) throws SQLException 
      {
        	List<LinkedHashMap> finalMap=new ArrayList<LinkedHashMap>();
          	
        	Long profileId=Long.valueOf(paramMap.get("profileId").toString());
        	
          	ZonedDateTime fmDate=ZonedDateTime.parse(paramMap.get("startDate").toString());
          	ZonedDateTime toDate=ZonedDateTime.parse(paramMap.get("endDate").toString());
          	log.info("fmDate :"+fmDate);
          	log.info("toDate :"+toDate);
          	java.time.LocalDate fDate=fmDate.toLocalDate();
          	java.time.LocalDate tDate=toDate.toLocalDate();
          
          	
          	
          	List<Object[]> transformedSummary=sourceFileInbHistoryRepository.fetchAgingForTransformedSummary(profileId,fDate+"%",tDate+"%");
          	for(int i=0;i<transformedSummary.size();i++)
          	{
          		LinkedHashMap map=new LinkedHashMap();
          		
          		map.put("date", transformedSummary.get(i)[0]);
          		map.put("count", transformedSummary.get(i)[1]);
          		
      			finalMap.add(map);
          		
          	}
      		return finalMap;
          }
      
      
      
      /**
       * author ravali
       * (pie chart)
       * @param process
       * @param dates
       * @return
       * @throws SQLException
       */
      @PostMapping("/getAgingAnalysisExtraction")
      @Timed
      public List<LinkedHashMap> getAgingAnalysisExtraction(@RequestBody HashMap paramMap) throws SQLException
      {
        	List<LinkedHashMap> finalMap=new ArrayList<LinkedHashMap>();
          
         
          	Long profileId=Long.valueOf(paramMap.get("profileId").toString());
          	ZonedDateTime fmDate=ZonedDateTime.parse(paramMap.get("startDate").toString());
          	ZonedDateTime toDate=ZonedDateTime.parse(paramMap.get("endDate").toString());
          	log.info("fmDate :"+fmDate);
          	log.info("toDate :"+toDate);
          	java.time.LocalDate fDate=fmDate.toLocalDate();
          	java.time.LocalDate tDate=toDate.toLocalDate();
          
          	
          	
          	List<Object[]> extarctedSummary=sourceFileInbHistoryRepository.fetchAgingForTransformedSummary(profileId,fDate+"%",tDate+"%");
          	for(int i=0;i<extarctedSummary.size();i++)
          	{
          		LinkedHashMap map=new LinkedHashMap();
          		
          		map.put("date", extarctedSummary.get(i)[0]);
          		map.put("count", extarctedSummary.get(i)[1]);
          		
      			finalMap.add(map);
          		
          	}
      		return finalMap;
          }
      
      
      
      
      @PostMapping("/getEachDayAnalysisForAProcess")
      @Timed
      public List<LinkedHashMap> getEachDayAnalysisForAProcess(@RequestParam Long processId,HttpServletRequest request,@RequestBody HashMap dates) throws SQLException 
      {
      	log.info("Rest Request to get aging analysis :"+dates);
      	HashMap map0=userJdbcService.getuserInfoFromToken(request);
	  	Long tenantId=Long.parseLong(map0.get("tenantId").toString());

      	Properties props =  propertiesUtilService.getPropertiesFromClasspath("File.properties");
    	List<LinkedHashMap> finalMap=new ArrayList<LinkedHashMap>();
      	
      	/*Long dvId =Long.valueOf(agingAnalysisParameters.get("dvId").toString());*/

      	/*String profiles=props.getProperty("profiles");
      	log.info("profiles :"+profiles.replaceAll("\\[", "").replaceAll("\\]", ""));*/
    	if(processId!=null)
    	{
      	List<BigInteger> procDetails=processDetailsRepository.findTypeIdByProcessIdAndTagType(processId, "sourceProfile");

      	ZonedDateTime fmDate=ZonedDateTime.parse(dates.get("startDate").toString());
      	ZonedDateTime toDate=ZonedDateTime.parse(dates.get("endDate").toString());
      	log.info("fmDate :"+fmDate);
      	log.info("toDate :"+toDate);
      	java.time.LocalDate fDate=fmDate.toLocalDate();
      	java.time.LocalDate tDate=toDate.toLocalDate();
      	log.info("fDate :"+fDate);
      	log.info("tDate :"+tDate);
      
      	ApplicationPrograms app=applicationProgramsRepository.findByPrgmNameAndTenantId("DataExtraction", tenantId);

      //	DataViews dv=dataViewsRepository.findOne(dvId);
      	log.info("fDate :"+fDate);
      	log.info("tDate :"+tDate);
      
    
        // while(fDate.isBefore(tDate.plusDays(1)))
      	 while(tDate.plusDays(1).isAfter(fDate))
         {
        	 Double totalCount = 0d;
      	   log.info("in while tDate :"+tDate.toString());
      	   LinkedHashMap map=new LinkedHashMap();
      	   
      	   /**
      	    * extraction
      	    */
      	 int finalCount=0;
 		int extractedCount=0;
 		log.info("finalCount :"+finalCount);

 		for(BigInteger profileId:procDetails)
 		{

 		
 			log.info("profileId Long:"+profileId);
 			List<LinkedHashMap> templateMapList=new ArrayList<LinkedHashMap>();
 		//	List<BigInteger> tempIds=sourceProfileFileAssignmentsRepository.fetchTempIdsBySrcProfile(profileId.longValue());
 		//	for(BigInteger tempId:tempIds)
 		//	{
 				LinkedHashMap tempMap=new LinkedHashMap();
 			//	log.info("tempId :"+tempId);
 			
 				log.info("fDate1 :"+fDate);	
 				log.info("tDate1 :"+tDate);	

 				List<BigInteger> jobDetails=jobDetailsRepository.findByTenantIdAndProgrammIdAndParameterArgument1AndStartDate(app.getId(),procDetails,tDate+"%");
				//log.info("jobDetails**** :"+jobDetails+" on "+tDate);

				if(jobDetails.size()>0)
				{


 					log.info("in while");
 					List<SchedulerDetails> sch=schedulerDetailsRepository.findSchedulersByJobIds(jobDetails);
 					log.info("sch :"+sch);
 					for(int i=0;i<sch.size();i++)
 					{

 						if(sch.get(i).getOozieJobId()!=null)
 						{

							if(sch.get(i).getFrequency().equalsIgnoreCase("OnDemand"))
							{finalCount=finalCount+1;//how many has to performed
							extractedCount=extractedCount+1;//how many has performed

							}

							if(sch.get(i).getFrequency().equalsIgnoreCase("OneTime"))
							{ finalCount=finalCount+1;
							extractedCount=extractedCount+1;
							}
 							
 						if(sch.get(i).getFrequency().equalsIgnoreCase("hourly"))
 						{
 							log.info("sch.get(i).getHours() :"+sch.get(i).getHours());

 							log.info("tDate.getDayOfWeek()1 :"+tDate.getDayOfWeek());
 							if(toDate.isBefore(sch.get(i).getEndDate()))
 							{
 								Long totalRuns=24/sch.get(i).getHours();
 								log.info("totalRuns1 :"+totalRuns.intValue());
 								finalCount=finalCount+totalRuns.intValue();
 								int extractedFileCount=getOutOfCountForProfileExtractionFromOozie(tDate,sch.get(i).getOozieJobId());
 								extractedCount=extractedCount+extractedFileCount;

 							}

 						}
 						if(sch.get(i).getFrequency().equalsIgnoreCase("minutes"))
 						{
 							log.info("sch.get(i).getMinutes :"+sch.get(i).getMinutes());

 							if(toDate.isBefore(sch.get(i).getEndDate()))
 							{
 								Long totalRuns=24*60/sch.get(i).getMinutes();
 								log.info("totalRuns1 :"+totalRuns);
 								finalCount=finalCount+totalRuns.intValue();
 								int extractedFileCount=getOutOfCountForProfileExtractionFromOozie(tDate,sch.get(i).getOozieJobId());
 								extractedCount=extractedCount+extractedFileCount;

 							}

 						}

 						if(sch.get(i).getFrequency().equalsIgnoreCase("Daily"))
 						{
 							log.info("sch.get(i).getMinutes :"+sch.get(i).getMinutes());

 							if(toDate.isBefore(sch.get(i).getEndDate()))
 							{
 								Long totalRuns=1l;
 								log.info("totalRuns1 :"+totalRuns);
 								finalCount=finalCount+totalRuns.intValue();
 								int extractedFileCount=getOutOfCountForProfileExtractionFromOozie(tDate,sch.get(i).getOozieJobId());
 								extractedCount=extractedCount+extractedFileCount;

 							}

 						}
 						if(sch.get(i).getFrequency().equalsIgnoreCase("weekly"))
 						{

 							log.info("sch.get(i).getWeekDay() :"+sch.get(i).getWeekDay());
 							String weekDay=tDate.getDayOfWeek().toString();
 							String day=weekDay.subSequence(0, 3).toString();
 							log.info("tDate.getDayOfWeek() :"+weekDay.subSequence(0, 3));
 							if(sch.get(i).getWeekDay().equalsIgnoreCase(day))
 							{
 								if(toDate.isBefore(sch.get(i).getEndDate()))
 								{
 									Long weeks=1l;

 									finalCount=finalCount+weeks.intValue();
 									int extractedFileCount=getOutOfCountForProfileExtractionFromOozie(tDate,sch.get(i).getOozieJobId());
 									extractedCount=extractedCount+extractedFileCount;

 								}
 							}

 						}
 						if(sch.get(i).getFrequency().equalsIgnoreCase("MONTHLY"))
 						{

 							log.info("sch.get(i).getMonth() :"+sch.get(i).getMonth());
 							String month=tDate.getMonth().toString();
 							String mon=month.subSequence(0, 3).toString();
 							log.info("tDate.getMonth() :"+mon.subSequence(0, 3));
 							if(sch.get(i).getMonth().equalsIgnoreCase(mon))
 							{
 								if(toDate.isBefore(sch.get(i).getEndDate()))
 								{

 									Long months=1l;
 									finalCount=finalCount+months.intValue();
 									int extractedFileCount=getOutOfCountForProfileExtractionFromOozie(tDate,sch.get(i).getOozieJobId());
 									extractedCount=extractedCount+extractedFileCount;

 								}
 							}


 						}
 					}
 					}


 				}

 		//	}

 		}
 		log.info("extractedCount1 :"+extractedCount);
 		log.info("finalCount1 :"+finalCount);

 		map.put("count", finalCount);
 		double extractedPer=0d;
 		if(finalCount>0)
 		{
 			extractedPer=(extractedCount/finalCount)*100;
 		}
		log.info("extractedPer :"+extractedPer);
 		map.put("extractedCount", extractedCount);
      	   
      	   
      	   
      	   
      	   /**transformation**/
      	 
      	 List< Object[]> transformedSummary=sourceFileInbHistoryRepository.fetchTransfomedCount(procDetails,tDate.toString()+"%");
      	 if(transformedSummary!=null)
     	  {
      	 for(int i=0;i<transformedSummary.size();i++)
      	 {
      	  log.info("transformation :"+transformedSummary.size());
      	 
      	  map.put("date", tDate);
      	
      	  if(transformedSummary.get(i)!=null)
      	  {
      	  map.put("transformation", transformedSummary.get(i)[2]);
      	  if(map.get("transformation")!=null)
      	//totalCount=totalCount+Long.valueOf(map.get("transformation").toString());
      	log.info("totalCount :"+totalCount);
      	  }
      	  else
      		  map.put("transformation", "");
      	  if(transformedSummary.get(i)!=null)
      	  map.put("ntTransformedPer", transformedSummary.get(i)[3]);
      	  else
      		  map.put("ntTransformedPer", "");
      	  //finalMap.add(map);
      	 // fDate=fDate.plusDays(1);
      	 
      	  }
      	  }
      	 
      	 
      	 /**accounting**/
      	 
      	  ProcessDetails procesActDet=processDetailsRepository.findByProcessIdAndTagType(processId, "accountingRuleGroup");
      	 
      	 List< Object[]> actSummary=appModuleSummaryRepository.fetchProcessedAndUnProcessedCountPerDay(procesActDet.getTypeId(),"Final Accounted",tDate);
		  for(int i=0;i<actSummary.size();i++)
		  {
			  log.info("reconSummary :"+actSummary.size());
			  if(actSummary!=null)
			  {
				  map.put("date", tDate);
				  if(actSummary.get(i)[2]!=null)
				  {
					  map.put("accounting", actSummary.get(i)[2]);
					  if(map.get("accounting")!=null)
						//totalCount=totalCount+Long.valueOf(map.get("accounting").toString());
				      	log.info("totalCount :"+totalCount);
				  }
				  else
					  map.put("accounting", 0);

				 // finalMap.add(map);
				 // fDate=fDate.plusDays(1);
				 
			  }
		  }
		  
		  
		  /**Reconciliation**/
			ProcessDetails procesRecDet=processDetailsRepository.findByProcessIdAndTagType(processId, "reconciliationRuleGroup");
			List< Object[]> reconSummary=appModuleSummaryRepository.fetchReconCountAndUnReconciledCountPerDay(procesRecDet.getTypeId(),tDate);
			for(int i=0;i<reconSummary.size();i++)
			{
				log.info("reconSummary :"+reconSummary.size());
				if(reconSummary!=null)
				{
					map.put("date", tDate);
					if(reconSummary.get(i)[2]!=null)
					{
						map.put("reconciliation", reconSummary.get(i)[2]);
						if(map.get("Reconciliation")!=null)
						//totalCount=totalCount+Long.valueOf(map.get("Reconciliation").toString());
				      	log.info("totalCount :"+totalCount);
					}
					else
						map.put("reconciliation", 0);
					if(reconSummary.get(i)[3]!=null)
						map.put("unReconciledPer", reconSummary.get(i)[3]);
					else
						map.put("unReconciledPer", "");
					//finalMap.add(map);
					
					
				}
			}
			map.put("journals", 0);
			map.put("extraction", 0);
		//Double total=totalCount/5d;s
			//map.put("total", total);
			finalMap.add(map);
			tDate=tDate.minusDays(1);
			log.info("end of while fDate :"+fDate);
	  }
      }
      	 
        
      	return finalMap;


      }
      
      
      /**
       * author:ravali
       * Feb16-Ping Oozie jobId
       * @param dates
       * @param tenantId
       * @param processId
       * @return
       * @throws SQLException 
       */
      @PostMapping("/getOutOfCountForProfileExtractionFromOozie")
      @Timed
      public int getOutOfCountForProfileExtractionFromOozie(@RequestBody java.time.LocalDate tDate,@RequestParam String oozieJobId) throws SQLException 
      {
      	
       	log.info("Request Rest to fetch schedulers list with oozieJobId"+oozieJobId);
      	log.info("Request Rest to local date"+tDate);
      	List<SchedulerDetails> schList = new ArrayList<SchedulerDetails>();
      	//to get the total count and adding attribute to response header
      	
      	List<HashMap> finalSchList=new ArrayList<HashMap>();

      	String oozieUrl=env.getProperty("oozie.OozieClient");
      	//	Properties props = propertiesUtilService.getPropertiesFromClasspath("File.properties");
      	String DB_URL = env.getProperty("oozie.ozieUrl");
      	String USER = env.getProperty("oozie.ozieUser");
      	String PASS = env.getProperty("oozie.oziePswd");
      	String schema = env.getProperty("oozie.ozieSchema");

      	Connection conn =null;
      	conn = DriverManager.getConnection(DB_URL, USER, PASS);
      	Statement stmt = null;
      	
      	stmt = conn.createStatement();
      	
      	ResultSet result2=null;
      	int totalCount =0;
  	
      	
      	String query="select count(job_id) from "+schema+".recon_oozie_jobs_v where parent_id ='"+oozieJobId+"' and job_id !='"+oozieJobId+"' and '"+tDate+"%'"+" = Date(start_time)";
      	//log.info("query :"+query);
  			result2=stmt.executeQuery(query);
  			while(result2.next())
  	    	{
  				totalCount=Integer.parseInt(result2.getString("count(job_id)").toString());
  	    	}
  			
  			
  	    	if(conn!=null)
  	    		conn.close();
  	    	if(stmt!=null)
  	    		stmt.close();
  	    	if(result2!=null)
  	    		result2.close();
  	  
  			
  			log.info("totalCount :"+totalCount);
  			return totalCount;
  		
      }
      
      
      
 	 @GetMapping("/updateAppModuleSummaryForRecon")
 	 @Async
 	 @Timed
 	 public HashMap updateAppModuleSummaryForRecon(HttpServletRequest request,@RequestParam(value = "groupId", required=true) String groupId) throws ClassNotFoundException, SQLException
 	 {
 		 log.info("API for updating recon count and amounts in app module summary table");
 		 HashMap finalMap = new HashMap();
 		 HashMap map=userJdbcService.getuserInfoFromToken(request);
 		 Long tenantId = Long.parseLong(map.get("tenantId").toString());
 		 Long userId = Long.parseLong(map.get("userId").toString());
 		 RuleGroup ruleGroup = ruleGroupRepository.findByIdForDisplayAndTenantId(groupId, tenantId);
 		 try
 		 {
 	 		 HashMap<String, List<BigInteger>> srcTrViewIds = reconciliationResultService.getDistinctDVIdsforRuleGrp(ruleGroup.getId(), tenantId);
 	 		 List<BigInteger> srcViewIds =  srcTrViewIds.get("sourceViewIds");
 	 		 List<BigInteger> trViewIds = srcTrViewIds.get("targeViewIds");
 	 		 if(srcViewIds.size()>0)
 	 		 {
 	 			 for(BigInteger sViewId : srcViewIds)
 	 			 {
 	 				 String dateQualifier = dashBoardV4Service.getFileDateOrQualifier(sViewId.longValue(), tenantId);
 	 				 String sAmountQualifier = reconciliationResultService.getViewColumnQualifier(sViewId, "AMOUNT");
 	 				 HashMap updateCountAmntsForSrc = reconciliationResultService.updateAppModuleSummaryForSource(sAmountQualifier, sViewId.longValue(), ruleGroup.getId(), "RECONCILIATION", "SOURCE", "RECONCILED", userId, tenantId, dateQualifier);
 	 			 }
 	 		 }
 	 		 System.out.println("Updated count and amounts for source");
 	 		 if(trViewIds.size()>0)
 	 		 {
 	 			 for(BigInteger tViewId : trViewIds)
 	 			 {
 	 				 String dateQualifier = dashBoardV4Service.getFileDateOrQualifier(tViewId.longValue(), tenantId);
 	 				 String tAmountQualifier = reconciliationResultService.getViewColumnQualifier(tViewId, "AMOUNT");
 	 				 HashMap updateCountAmntsForTrgt = reconciliationResultService.updateAppModuleSummaryForTarget(tAmountQualifier, tViewId.longValue(), ruleGroup.getId(), "RECONCILIATION", "TARGET", "RECONCILED", userId, tenantId, dateQualifier);
 	 			 }
 	 		 }
 	 		 System.out.println("Updated count and amounts for target"); 
 	 		 // Updating multi currency fields
 	 		 LinkedHashMap multiCurrency = dashBoardV4Service.checkMultiCurrency(ruleGroup.getId(), tenantId, srcTrViewIds);
 	 		 if(multiCurrency.size()>0)
 	 		 {
 	 			 HashMap updateMultiCurrency =  dashBoardV4Service.updateMultiCurrency(multiCurrency, ruleGroup.getId());
 	 		 }
 	 		 finalMap.put("status", "Success");
 		 }
 		 catch(Exception e)
 		 {
 			 finalMap.put("status", "Failed");
 		 }
 		 return finalMap;
 	 }

 	 
 	 
 	 
 	 @GetMapping("/updateAppModuleSummaryForAccounting")
 	 @Async
 	 @Timed
 	 public HashMap updateAppModuleSummaryForAccounting(HttpServletRequest request,@RequestParam(value = "groupId", required=true) String groupId) throws ClassNotFoundException, SQLException
 	 {
 		 log.info("API for updating accounting count and amounts in app module summary");

 		 HashMap map=userJdbcService.getuserInfoFromToken(request);
 		 Long tenantId = Long.parseLong(map.get("tenantId").toString());
 		 Long userId = Long.parseLong(map.get("userId").toString());
 		 RuleGroup ruleGroup = ruleGroupRepository.findByIdForDisplayAndTenantId(groupId, tenantId);
 		 HashMap finalMap = new HashMap();
 		 try{
 	 		 HashMap<String, List<BigInteger>> srcTrViewIds = reconciliationResultService.getDistinctDVIdsforRuleGrp(ruleGroup.getId(), tenantId);
 	 		 List<BigInteger> srcViewIds =  srcTrViewIds.get("sourceViewIds");
 	 		 if(srcViewIds.size()>0)
 	 		 {
 	 			 for(BigInteger sViewId : srcViewIds)
 	 			 {
 	 				 String dateQualifier = dashBoardV4Service.getFileDateOrQualifier(sViewId.longValue(), tenantId);
 	 				 String amountQualifier = reconciliationResultService.getViewColumnQualifier(sViewId, "AMOUNT");
 	 				 HashMap updateCountAmountsAcc = accountingDataService.updateAppModuleSummaryInfoACC(ruleGroup.getId(), sViewId.longValue(), amountQualifier, userId,tenantId, dateQualifier);
 	 			 }
 	 			 log.info("Updated count and amounts for accounting in t_app_module_summary table");
 	 			 List<BigInteger> targetViewIds = new ArrayList<BigInteger>();
 	 			 srcTrViewIds.put("targeViewIds", targetViewIds);
 	 	 		 LinkedHashMap multiCurrency = dashBoardV4Service.checkMultiCurrency(ruleGroup.getId(), tenantId, srcTrViewIds);
 	 	 		 if(multiCurrency.size()>0)
 	 	 		 {
 	 	 			 HashMap updateMultiCurrency =  dashBoardV4Service.updateMultiCurrency(multiCurrency, ruleGroup.getId());
 	 	 		 }
 	 	 		 log.info("Updated multi currency fields in t_app_module_summary table");
 	 		 }
 		 }
 		 catch(Exception e)
 		 {
 			 log.info("Exception while updating count and amounts for accounting: "+e);
 		 }

 		 return finalMap;
 	 }

 	 /**
 	  * Author: Shiva
 	  * Purpose: Deleting app_module_summary records based on ruleGroupId
 	  * **/
 	 @GetMapping("/deleteAppModulesByGroupId")
 	 @Timed
 	 public HashMap deleteAppModulesByGroupId(HttpServletRequest request,@RequestParam(value = "groupId", required=true) Long groupId) throws ClassNotFoundException, SQLException
 	 {
 		 HashMap finalMap = new HashMap();
 		 List<AppModuleSummary> appModules = appModuleSummaryRepository.findByRuleGroupId(groupId);
 		 if(appModules.size()>0)
 		 {
 			 appModuleSummaryRepository.deleteInBatch(appModules);
 		 }
 		 finalMap.put("count", appModules.size());
 		 finalMap.put("message", appModules.size()+" records has been deleted for the group id "+groupId);
 		 return finalMap;
 	 }
 	 
 	 
 	 @Async
 	 @PostMapping("/updateAppModuleSummaryAfterRuleGroupIsCreated")
 	 @Timed
 	 public void updateAppModuleSummaryAfterRuleGroupIsCreated(@RequestParam Long ruleGroupId,HttpServletRequest request)
 	 {
 		 HashMap map=userJdbcService.getuserInfoFromToken(request);
 		 Long userId=Long.parseLong(map.get("userId").toString());
 		 Long tenantId= Long.parseLong(map.get("tenantId").toString());


 		 RuleGroup ruleGrp=ruleGroupRepository.findOne(ruleGroupId);
 		 List<Long> ruleIds=ruleGroupDetailsRepository.fetchRuleIdsByGroupIdAndTenantId(ruleGroupId,ruleGrp.getTenantId());
 		 log.info("ruleIds :"+ruleIds.size());
 		 if(ruleIds.size()>0)
 		 {
 			 List<BigInteger> dvIds=new ArrayList<BigInteger>();
 			 List<BigInteger> srcIds=rulesRepository.fetchDistictSrcViewIdsByRuleId(ruleIds);
 			 List<BigInteger> trgIds=rulesRepository.fetchDistictTargetViewIdsByRuleId(ruleIds);
 			 dvIds.addAll(srcIds);
 			 dvIds.addAll(trgIds);
 			 log.info("dvIds :"+dvIds.size());
 			 if(dvIds.size()>0)
 			 {
 				 List<String> tempIds=dataViewsColumnsRepository.fetchDistinctTemplateIdByDataViewIdIn(dvIds);
 				 log.info("tempIds :"+tempIds.size());
 				 for(String tempId:tempIds)
 				 {
 					 try {
 						 createAppModuleSummary(Long.valueOf(tempId), ruleGrp.getTenantId(), userId);
 					 } catch (NumberFormatException e) {
 						 // TODO Auto-generated catch block
 						 e.printStackTrace();
 					 }
 				 }
 			 }
 		 }
 	 }
}
