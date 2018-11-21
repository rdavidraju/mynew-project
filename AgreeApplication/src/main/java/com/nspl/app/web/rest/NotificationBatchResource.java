package com.nspl.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.config.ApplicationContextProvider;
import com.nspl.app.domain.AccountingData;
import com.nspl.app.domain.ApprovalRuleAssignment;
import com.nspl.app.domain.BatchHeader;
import com.nspl.app.domain.DataViews;
import com.nspl.app.domain.DataViewsColumns;
import com.nspl.app.domain.FileTemplateLines;
import com.nspl.app.domain.JobDetails;
import com.nspl.app.domain.LookUpCode;
import com.nspl.app.domain.NotificationBatch;
import com.nspl.app.domain.ReconciliationResult;
import com.nspl.app.domain.RuleGroup;
import com.nspl.app.domain.Rules;
import com.nspl.app.jbpm.service.ApprovalProcessService;
import com.nspl.app.jbpm.service.ApprovalTaskService;
import com.nspl.app.repository.ApprovalRuleAssignmentRepository;
import com.nspl.app.repository.DataViewsColumnsRepository;
import com.nspl.app.repository.DataViewsRepository;
import com.nspl.app.repository.LookUpCodeRepository;
import com.nspl.app.repository.NotificationBatchRepository;
import com.nspl.app.repository.ReconciliationResultRepository;
import com.nspl.app.repository.RuleConditionsRepository;
import com.nspl.app.repository.RuleGroupDetailsRepository;
import com.nspl.app.repository.RuleGroupRepository;
import com.nspl.app.repository.RulesRepository;
import com.nspl.app.service.AccountingDataService;
import com.nspl.app.service.DashBoardV4Service;
import com.nspl.app.service.DataViewsService;
import com.nspl.app.service.HierarchyService;
import com.nspl.app.service.ReconciliationResultService;
import com.nspl.app.service.UserJdbcService;
import com.nspl.app.web.rest.util.HeaderUtil;
import com.nspl.app.web.rest.util.PaginationUtil;

import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;

import org.jbpm.services.task.commands.GetOrgEntityCommand;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * REST controller for managing NotificationBatch.
 */
@RestController
@RequestMapping("/api")
public class NotificationBatchResource {

    private final Logger log = LoggerFactory.getLogger(NotificationBatchResource.class);

    private static final String ENTITY_NAME = "notificationBatch";
        
    private final NotificationBatchRepository notificationBatchRepository;
    
    @Inject
    RuleGroupRepository ruleGroupRepository;
    
    @Inject
    RulesRepository rulesRepository;
    
    @Inject
    DataViewsRepository dataViewsRepository;
    
    @Inject
    ReconciliationResultRepository reconciliationResultRepository;
    
    @Autowired
   	Environment env;
    
    @Inject
    ApprovalRuleAssignmentRepository approvalRuleAssignmentRepository;
    
    @Inject
    UserJdbcService userJdbcService;
    
    @Inject
    LookUpCodeRepository lookUpCodeRepository;
    
    @Inject
    ReconciliationResultService reconciliationResultService;
    
    @Inject
    RuleGroupDetailsRepository ruleGroupDetailsRepository;
    
    @Inject
    DataViewsColumnsRepository dataViewsColumnsRepository;
    
    @Inject
    RuleConditionsRepository ruleConditionsRepository;
    
   @Inject
   ApprovalTaskService approvalTaskService;
   
   @Inject
   HierarchyService hierarchyService;
   
   @Inject
   ApprovalProcessService approvalProcessService;
   
   @Inject
   AccountingDataService accountingDataService;
   
   @Inject
   DashBoardV4Service dashBoardV4Service;
   
   
   @Inject
   DataViewsService dataViewsService;
   
   @PersistenceContext(unitName="default")
  	private EntityManager em;
   
   	@Autowired
	RuntimeManager manager;
   	
   	
     
   
    public NotificationBatchResource(NotificationBatchRepository notificationBatchRepository) {
        this.notificationBatchRepository = notificationBatchRepository;
    }

    /**
     * POST  /notification-batches : Create a new notificationBatch.
     *
     * @param notificationBatch the notificationBatch to create
     * @return the ResponseEntity with status 201 (Created) and with body the new notificationBatch, or with status 400 (Bad Request) if the notificationBatch has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/notification-batches")
    @Timed
    public ResponseEntity<NotificationBatch> createNotificationBatch(@RequestBody NotificationBatch notificationBatch) throws URISyntaxException {
        log.debug("REST request to save NotificationBatch : {}", notificationBatch);
        if (notificationBatch.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new notificationBatch cannot already have an ID")).body(null);
        }
        NotificationBatch result = notificationBatchRepository.save(notificationBatch);
        return ResponseEntity.created(new URI("/api/notification-batches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /notification-batches : Updates an existing notificationBatch.
     *
     * @param notificationBatch the notificationBatch to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated notificationBatch,
     * or with status 400 (Bad Request) if the notificationBatch is not valid,
     * or with status 500 (Internal Server Error) if the notificationBatch couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/notification-batches")
    @Timed
    public ResponseEntity<NotificationBatch> updateNotificationBatch(@RequestBody NotificationBatch notificationBatch) throws URISyntaxException {
        log.debug("REST request to update NotificationBatch : {}", notificationBatch);
        if (notificationBatch.getId() == null) {
            return createNotificationBatch(notificationBatch);
        }
        NotificationBatch result = notificationBatchRepository.save(notificationBatch);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, notificationBatch.getId().toString()))
            .body(result);
    }

    /**
     * GET  /notification-batches : get all the notificationBatches.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of notificationBatches in body
     */
    @GetMapping("/notification-batches")
    @Timed
    public ResponseEntity<List<NotificationBatch>> getAllNotificationBatches(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of NotificationBatches");
        Page<NotificationBatch> page = notificationBatchRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/notification-batches");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /notification-batches/:id : get the "id" notificationBatch.
     *
     * @param id the id of the notificationBatch to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the notificationBatch, or with status 404 (Not Found)
     */
    @GetMapping("/notification-batches/{id}")
    @Timed
    public ResponseEntity<NotificationBatch> getNotificationBatch(@PathVariable Long id) {
        log.debug("REST request to get NotificationBatch : {}", id);
        NotificationBatch notificationBatch = notificationBatchRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(notificationBatch));
    }

    /**
     * DELETE  /notification-batches/:id : delete the "id" notificationBatch.
     *
     * @param id the id of the notificationBatch to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/notification-batches/{id}")
    @Timed
    public ResponseEntity<Void> deleteNotificationBatch(@PathVariable Long id) {
        log.debug("REST request to delete NotificationBatch : {}", id);
        notificationBatchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    
    @GetMapping("/_search/notification-batch")
    @Timed
    public List<NotificationBatch> searchNotificationBatch(@RequestParam Long tenantId, @RequestParam(value="filterValue",required=false) String filterValue,@RequestParam(value = "pageNumber", required=false) Long pageNumber, @RequestParam(value = "pageSize", required=false) Long pageSize) {
    	log.info("Rest api for fetching notification batch usgin full text search for the tenant_id: "+tenantId);
		Long limit = 0L;
		limit = (pageNumber * pageSize + 1)-1;
		log.info("Limit Starting Values : "+ limit);
		log.info("Page Number : "+ pageNumber);
    	
    	List<NotificationBatch> batchHeaders = notificationBatchRepository.fetchRecordsWithKeyWord(tenantId, filterValue, pageNumber, limit);
    	return batchHeaders;
    }
    
    /**
     * author: ravali
     * Desc :API to fetch notification list based on tenantId and userId(currentApprover)
     * @param tenantId
     * @param userId
     * @param offset
     * @param limit
     * @return List<HashMap>
     * @throws ParseException 
     */
    
    @GetMapping("/getNotificationBatchList2")
    @Timed
    public  List<HashMap> getNotificationBatch(@RequestParam Long tenantId,@RequestParam Long userId,@RequestParam(value = "page" , required = false) Integer offset,
			@RequestParam(value = "per_page", required = false) Integer limit,HttpServletResponse response,@RequestParam(value = "status", required = false) String status) throws ParseException
			{
    	log.info("Request Rest to get notification Batch by tenantId:"+tenantId +" and "+userId);

    	
    	List<NotificationBatch> notBatchList=new ArrayList<NotificationBatch>();
    	PaginationUtil paginationUtil=new PaginationUtil();

    	int maxlmt=paginationUtil.MAX_LIMIT;
    	int minlmt=paginationUtil.MIN_OFFSET;
    	log.info("maxlmt: "+maxlmt);
    	Page<NotificationBatch> page = null;
    	HttpHeaders headers = null;
    	List<NotificationBatch> notificationCount;
    	if(status!=null)
    	notificationCount =notBatchList = notificationBatchRepository.findByTenantIdAndStatusAndCurrentApproverOrderById(tenantId,status,userId);
    	else
    		notificationCount =notBatchList = notificationBatchRepository.findByTenantIdAndCurrentApproverOrderById(tenantId,userId);
    	log.info("notificationCount.size :"+notificationCount.size());
		response.addIntHeader("X-COUNT", notificationCount.size());
    	
    	if(limit==null || limit<minlmt){
    		
    		if(status!=null)
    		notBatchList = notificationBatchRepository.findByTenantIdAndStatusAndCurrentApproverOrderById(tenantId,status,userId);
    		else
    			notBatchList=notificationBatchRepository.findByTenantIdAndCurrentApproverOrderById(tenantId,userId);

    		limit = notBatchList.size();
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
    		if(status!=null)
    		page = notificationBatchRepository.findByTenantIdAndStatusAndCurrentApproverOrderById(tenantId,status,userId,PaginationUtil.generatePageRequest2(offset, limit));
    		else
    			page = notificationBatchRepository.findByTenantIdAndCurrentApproverOrderById(tenantId,userId,PaginationUtil.generatePageRequest2(offset, limit));	


    	}
    	else{
    		log.info("input limit is within maxlimit");
    		if(status!=null)
    		page = notificationBatchRepository.findByTenantIdAndStatusAndCurrentApproverOrderById(tenantId,status,userId,PaginationUtil.generatePageRequest(offset, limit));
    		else
    			page = notificationBatchRepository.findByTenantIdAndCurrentApproverOrderById(tenantId,userId,PaginationUtil.generatePageRequest(offset, limit));

    	}


    	if(notBatchList.size()==0)
    	{

    		notBatchList=page.getContent();
    	}


    	List<HashMap> finalMap=new ArrayList<HashMap>();
    	for (NotificationBatch notBatch:notBatchList)
    	{
    		HashMap batchMap=new HashMap();
    		batchMap.put("id", notBatch.getId());
    		if(notBatch.getNotificationName()!=null)
    			batchMap.put("notificationName", notBatch.getNotificationName());
    		else
    			batchMap.put("notificationName","");
    		if(notBatch.getCreatedDate()!=null)
    			batchMap.put("notificationDate", notBatch.getCreatedDate());
    		else
    			batchMap.put("notificationDate", "");
    		if(notBatch.getRuleGroupId()!=null)
    		{
    			RuleGroup ruleGrp=ruleGroupRepository.findOne(notBatch.getRuleGroupId());
    			batchMap.put("ruleGroupName", ruleGrp.getName());
    		}
    		else
    			batchMap.put("ruleGroupName", "");
    		if(notBatch.getRuleId()!=null)
    		{
    			Rules rule=rulesRepository.findOne(notBatch.getRuleId());
    			batchMap.put("ruleName", rule.getRuleCode());
    			DataViews dvName=dataViewsRepository.findOne(rule.getSourceDataViewId());
    			batchMap.put("dataViewName", dvName.getDataViewName());
    		}
    		else
    		{
    			batchMap.put("ruleName", "");	
    			batchMap.put("dataViewName", "");
    		}
    		if(notBatch.getStatus()!=null)
    		{
    			batchMap.put("statusCode", notBatch.getStatus());
    			LookUpCode lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("APPROVAL_STATUS", notBatch.getStatus(), tenantId);
    			batchMap.put("status", lookUpCode.getMeaning());
    		}
    		else
    			batchMap.put("status", "");
    		batchMap.put("dueDate", null);
    		
    		
    		/* Added by Swetha Start */
    		/*if(notBatch.getStatus().equalsIgnoreCase("IN_PROCESS")){
    		if(notBatch.getProcessInstanceId()!=null && notBatch.getProcessInstanceId()!=0){
    			Long taskId=approvalTaskService.getTasksByProcessId(notBatch.getProcessInstanceId(), userId);
    			if(taskId!=null)
    				batchMap.put("taskId", taskId);
    		}
    		}*/
    		/* Added by Swetha end */
    		
    		finalMap.add(batchMap);

    	}
    	return finalMap;


			}
    
    /**
     * Author: Swetha
     * Api to retrieve approval notifications based on hierarchy type tagged to loggedInUser and his subordinates or a dedicated user's
     * @param tenantId
     * @param userId
     * @param offset
     * @param limit
     * @param response
     * @param status
     * @param hierarchyType [Hierarchy - Retrieves list tagged to loggedInUser and his subordinates, User - dedicated user's list  
     * @return
     * @throws ParseException
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    @GetMapping("/getNotificationBatchList")
    @Timed
    public  List<HashMap> getNotificationBatchListBasedOnHierarchyType(HttpServletRequest request,@RequestParam(value = "page" , required = false) Integer offset,
			@RequestParam(value = "per_page", required = false) Integer limit,HttpServletResponse response,@RequestParam(value = "status", required = false) String status, @RequestParam(value = "hierarchyType") String hierarchyType, @RequestParam(value = "module", required = false) String module) throws ParseException, ClassNotFoundException, SQLException
			{
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	Long userId=Long.parseLong(map.get("userId").toString());
    	log.info("Request Rest to get notification Batch by tenantId:"+tenantId +" and "+userId);

    	
    	List<NotificationBatch> notBatchList=new ArrayList<NotificationBatch>();
    	PaginationUtil paginationUtil=new PaginationUtil();

    	int maxlmt=paginationUtil.MAX_LIMIT;
    	int minlmt=paginationUtil.MIN_OFFSET;
    	log.info("maxlmt: "+maxlmt);
    	Page<NotificationBatch> page = null;
    	HttpHeaders headers = null;
    	List<String> resources=hierarchyService.getSubordinatesList(userId, tenantId);
    	List<Long> resourceList=new ArrayList<Long>();
    	
    	if(hierarchyType!=null && !(hierarchyType.isEmpty()) && hierarchyType.equalsIgnoreCase("Hierarchy")){
    	for(int i=0;i<resources.size();i++){
    		String res=resources.get(i);
    		Long resId=Long.parseLong(res);
    		resourceList.add(resId);
    	}
    	}
    	else resourceList.add(userId);
    	
    	List<String> moduleList=new ArrayList<String>();
    	if(module.equalsIgnoreCase("ALL")){
    		moduleList.add("RECON_APPROVALS");
    		moduleList.add("ACCOUNTING_APPROVALS");
    		moduleList.add("JOURNAL_APPROVALS");
    	}
    	else moduleList.add(module);
    		
    	
    	if(status!=null)
    	notBatchList = notificationBatchRepository.fetchByTenantIdAndStatusAndModuleAndCurrentApproverInOrderByIdDesc(tenantId,status,resourceList, moduleList);
    	else
    		notBatchList = notificationBatchRepository.fetchByTenantIdAndModuleAndCurrentApproverInOrderByIdDesc(tenantId,resourceList, moduleList);
    	log.info("notificationCount.size :"+notBatchList.size());
		response.addIntHeader("X-COUNT", notBatchList.size());
    	
    	if(limit==null || limit<minlmt){
    		
    		if(status!=null)
    		notBatchList = notificationBatchRepository.fetchByTenantIdAndStatusAndModuleAndCurrentApproverInOrderByIdDesc(tenantId,status,resourceList, moduleList);
    		else
    			notBatchList=notificationBatchRepository.fetchByTenantIdAndModuleAndCurrentApproverInOrderByIdDesc(tenantId,resourceList, moduleList);

    		limit = notBatchList.size();
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
    		if(status!=null)
    		page = notificationBatchRepository.findByTenantIdAndStatusAndModuleInAndCurrentApproverInOrderByIdDesc(tenantId,status,moduleList,resourceList,PaginationUtil.generatePageRequestWithSortColumn(offset, limit, "desc", "id"));
    		else
    			page = notificationBatchRepository.findByTenantIdAndModuleInAndCurrentApproverInOrderByIdDesc(tenantId,moduleList,resourceList,PaginationUtil.generatePageRequestWithSortColumn(offset, limit, "desc", "id"));	


    	}
    	else{
    		log.info("input limit is within maxlimit offset :"+offset+"limit :"+limit);
    		if(status!=null)
    		page = notificationBatchRepository.findByTenantIdAndStatusAndModuleInAndCurrentApproverInOrderByIdDesc(tenantId,status,moduleList,resourceList,PaginationUtil.generatePageRequestWithSortColumn(offset, limit, "desc", "id"));
    		else
    			page = notificationBatchRepository.findByTenantIdAndModuleInAndCurrentApproverInOrderByIdDesc(tenantId,moduleList,resourceList,PaginationUtil.generatePageRequestWithSortColumn(offset, limit, "desc", "id"));

    	}

	
    		notBatchList=page.getContent();
    	

    	List<HashMap> finalMap=new ArrayList<HashMap>();
    	for (NotificationBatch notBatch:notBatchList)
    	{
    		if(notBatch.getProcessInstanceId()!=0)
    		{
    		HashMap batchMap=new HashMap();
    		batchMap.put("id", notBatch.getId());
    		if(notBatch.getNotificationName()!=null)
    			batchMap.put("notificationName", notBatch.getNotificationName());
    		else
    			batchMap.put("notificationName","");
    		if(notBatch.getCreatedDate()!=null)
    			batchMap.put("notificationDate", notBatch.getCreatedDate());
    		else
    			batchMap.put("notificationDate", "");
    		if(notBatch.getRuleGroupId()!=null)
    		{
    			RuleGroup ruleGrp=ruleGroupRepository.findOne(notBatch.getRuleGroupId());
    			batchMap.put("ruleGroupName", ruleGrp.getName());
    			List<RuleGroup> recOrActRG=ruleGroupRepository.findByApprRuleGrpId(notBatch.getRuleGroupId());
    			//Accounting or reconciliation groups
    			List<LinkedHashMap> actOrRecGrpsList=new ArrayList<LinkedHashMap>();
    			for(RuleGroup grp:recOrActRG)
    			{
    				LinkedHashMap grpMap =new LinkedHashMap();
    			//	grpMap.put("groupId", grp.getId());
    				grpMap.put("groupId", grp.getIdForDisplay());
    				grpMap.put("groupName", grp.getName());
    				actOrRecGrpsList.add(grpMap);
    			}
    			batchMap.put("ruleGroupsList", actOrRecGrpsList);
    		}
    		else
    			batchMap.put("ruleGroupName", "");
    		if(notBatch.getRuleId()!=null)
    		{
    			Rules rule=rulesRepository.findOne(notBatch.getRuleId());
    			batchMap.put("ruleName", rule.getRuleCode());
    			DataViews dvName=dataViewsRepository.findOne(rule.getSourceDataViewId());
    		//	batchMap.put("dataViewId", dvName.getId());
    			batchMap.put("dataViewId", dvName.getIdForDisplay());
    			batchMap.put("dataViewName", dvName.getDataViewDispName());
    			//batchMap.put("qualfierDate", dataViewsService.getDateQualifiers(dvName.getId()));
    		}
    		else
    		{
    			batchMap.put("ruleName", "");	
    			batchMap.put("dataViewName", "");
    		}
    		if(notBatch.getModule()!=null)
    			batchMap.put("module", notBatch.getModule());
    		if(notBatch.getStatus()!=null)
    		{
    			batchMap.put("statusCode", notBatch.getStatus());
    			LookUpCode lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("APPROVAL_STATUS", notBatch.getStatus(), tenantId);
    			batchMap.put("status", lookUpCode.getMeaning());
    		}
    		else
    			batchMap.put("status", "");
    		batchMap.put("dueDate", null);
    		
    		/* Approver Information */
    		Long curApprId=notBatch.getCurrentApprover();
    		HashMap currAppInfo=userJdbcService.jdbcConnc(curApprId,tenantId);
    		//HashMap currAppInfo=(HashMap) usersData.get(curApprId.toString());
    	//	log.info("currAppInfo: "+currAppInfo);
    	/*	if(currAppInfo!=null){
    		firstName=currAppInfo.get("first_name").toString();
    		lastName=currAppInfo.get("last_name").toString();
    		fullName=firstName+" "+lastName;
    		}*/
    		
    		if(currAppInfo.get("assigneeName")!=null)
    		batchMap.put("approver",currAppInfo.get("assigneeName"));
    		else batchMap.put("approver", "");
    		
    		/**previous approver information**/
    		
    		NotificationBatch prevBatch=notificationBatchRepository.findOne(notBatch.getParentBatch());
    		
    		if(prevBatch!=null && prevBatch.getId()!=null)
    		{
    		Long prevApprId=prevBatch.getCurrentApprover();
    		HashMap prevAppInfo=userJdbcService.jdbcConnc(prevApprId,tenantId);
    		
    		if(prevAppInfo.get("assigneeName")!=null)
        		batchMap.put("prevApprover",prevAppInfo.get("assigneeName"));
        		else batchMap.put("prevApprover", "");
    		}
    		else
    			batchMap.put("prevApprover", "");
    		
    		
    		finalMap.add(batchMap);
    	}

    	}
    	log.info("finalMap size "+finalMap.size());
    	return finalMap;


			}
    
    /**
     * author :ravali
     * get approval history
     * @param notificationId
     * @param status
     * @return
     * @throws SQLException
     */
    @GetMapping("/getApprovalHistory")
    @Timed
    public List<HashMap> getApprovalHistory(@RequestParam Long notificationId,@RequestParam(value = "status", required = false) String status) throws SQLException
    {
    	log.info("Request Rest to get Approval history based on notification id :"+notificationId);
    	NotificationBatch notBatch=notificationBatchRepository.findOne(notificationId);
    	Integer I = null;
    	if(notBatch.getRefLevel()!=null)
    	{
    		I=notBatch.getRefLevel();
    	}
    	String refNum=String.valueOf(I);
    	if(refNum.length()<=1)
    		refNum="0"+refNum;
    	log.info("refNum :"+refNum);
    	List<HashMap> finalMap=new ArrayList<HashMap>();
    	String dbUrl=env.getProperty("spring.datasource.url");
    	String[] parts=dbUrl.split("[\\s@&?$+-]+");
    	String host = parts[0].split("/")[2].split(":")[0];
    	log.info("host :"+host);
    	String schemaName=parts[0].split("/")[3];
    	log.info("schemaName :"+schemaName);
    	String userName = env.getProperty("spring.datasource.username");
    	String password = env.getProperty("spring.datasource.password");
    	String jdbcDriver = env.getProperty("spring.datasource.jdbcdriver");

    	Connection conn = null;
    	Statement stmt = null;
    	conn = DriverManager.getConnection(dbUrl, userName, password);
    	log.info("Connected database successfully...");
    	stmt = conn.createStatement();
    	ResultSet result2=null;
    	//Class.forName("com.mysql.jdbc.Driver");
    	if(status!=null)
    	{
    		String userAndBatchId =notBatch.getCurrentApprover()+"|"+notBatch.getId()+"|"+status;
    		log.info("userAndBatchId :"+userAndBatchId);
    		String query="select * from "+schemaName+".t_reconciliation_result where appr_ref_"+refNum+" LIKE '"+userAndBatchId+"%'";
    		log.info("query :"+query);
    		result2=stmt.executeQuery("select * from "+schemaName+".t_reconciliation_result where appr_ref_"+refNum+" LIKE '"+userAndBatchId+"%'");
    	}
    	else
    	{
    		String userAndBatchId =notBatch.getCurrentApprover()+"|"+notBatch.getId();
    		log.info("userAndBatchId :"+userAndBatchId);
    		String query="select * from "+schemaName+".t_reconciliation_result where appr_ref_"+refNum+" LIKE '"+userAndBatchId+"%'";
    		log.info("query :"+query);
    		result2=stmt.executeQuery(query);
    	}
    	while(result2.next())
    	{
    		HashMap app=new HashMap();
    		if(result2.getString("id")!=null)
    			app.put("id", result2.getString("id"));
    		if(result2.getString("original_view_id")!=null)
    		{
    			app.put("recordType", "Source");
    			DataViews dvName=dataViewsRepository.findOne(Long.valueOf(result2.getString("original_view_id")));
    			app.put("viewId", Long.valueOf(result2.getString("original_view_id")));
    			app.put("viewName", dvName.getDataViewName());
    		}
    		else if(result2.getString("target_view_id")!=null)
    		{
    			app.put("recordType", "Target");
    			DataViews dvName=dataViewsRepository.findOne(Long.valueOf(result2.getString("target_view_id")));
    			app.put("viewId", Long.valueOf(result2.getString("target_view_id")));
    			app.put("viewName", dvName.getDataViewName());
    		}
    		if(result2.getString("reconciled_date")!=null)
    			app.put("reconciledDate", result2.getString("reconciled_date"));
    		else
    			app.put("reconciledDate", "");
    		if(result2.getString("recon_reference")!=null)
    			app.put("recReference", result2.getString("recon_reference"));
    		else
    			app.put("recReference", "");
    		if(result2.getString("recon_job_reference")!=null)
    			app.put("jobReference", result2.getString("recon_job_reference"));
    		else
    			app.put("jobReference", "");
    		if(result2.getString("approval_initiation_date")!=null)
    			app.put("approvalInitiationDate", result2.getString("approval_initiation_date"));
    		else
    			app.put("approvalInitiationDate", "");
    		finalMap.add(app);
    	}

    	if(conn!=null)
    		conn.close();
    	if(stmt!=null)
    		stmt.close();
    	if(result2!=null)
    		result2.close();
    	return finalMap;

    }
    
  /**
   * author ravali
   * Desc :Approval history of returning src and target records of ditinct reon reference
   * @param notificationId
   * @return
   * @throws SQLException
   * @return List<HashMap>
   */
    
    /*@GetMapping("/getApprovalHistorySrcTarget")
    @Timed
    public List<HashMap> getApprovalHistorySrcTarget(@RequestParam Long notificationId) throws SQLException
    {
    	log.info("Request Rest to get Approval history based on notification id :"+notificationId);
    	NotificationBatch notBatch=notificationBatchRepository.findOne(notificationId);
    	Integer I = null;
    	if(notBatch.getRefLevel()!=null)
    	{
    		I=notBatch.getRefLevel();
    	}
    	String refNum=String.valueOf(I);
    	if(refNum.length()<=1)
    		refNum="0"+refNum;
    	log.info("refNum :"+refNum);
    	List<HashMap> finalMap=new ArrayList<HashMap>();
    	String dbUrl=env.getProperty("spring.datasource.url");
    	String[] parts=dbUrl.split("[\\s@&?$+-]+");
    	String host = parts[0].split("/")[2].split(":")[0];
    	log.info("host :"+host);
    	String schemaName=parts[0].split("/")[3];
    	log.info("schemaName :"+schemaName);
    	String userName = env.getProperty("spring.datasource.username");
    	String password = env.getProperty("spring.datasource.password");
    	String jdbcDriver = env.getProperty("spring.datasource.jdbcdriver");

    	Connection conn = null;
    	Statement stmt = null;
    	Statement stmtSrc = null;
    	Statement stmtTrg = null;
    	Statement stmtSrcView= null;
    	Statement stmtTrgView= null;

    	conn = DriverManager.getConnection(dbUrl, userName, password);
    	log.info("Connected database successfully...");
    	stmt = conn.createStatement();
    	stmtSrc = conn.createStatement();
    	stmtTrg = conn.createStatement();
    	stmtSrcView = conn.createStatement();
    	stmtTrgView = conn.createStatement();
    	ResultSet result2=null;
    	ResultSet resultSrc=null;
    	ResultSet resultTrg=null;
    	ResultSet resultSrcView=null;
    	ResultSet resultTrgView=null;
    	//Class.forName("com.mysql.jdbc.Driver");



    	String userAndBatchId =notBatch.getCurrentApprover()+"|"+notBatch.getId();
    	log.info("userAndBatchId :"+userAndBatchId);
    	String query="select distinct(recon_reference) from "+schemaName+".t_reconciliation_result where appr_ref_"+refNum+" LIKE '"+userAndBatchId+"%'";
    	log.info("query"+query);
    	result2=stmt.executeQuery(query);

    	while(result2.next())
    	{
    		List<HashMap> srcRecList=new ArrayList<HashMap>();
    		List<HashMap> tarRecList=new ArrayList<HashMap>();
    		String srcQue="select * from "+schemaName+".t_reconciliation_result where original_view_id is not null and recon_reference in ('"+result2.getShort("recon_reference")+"' and appr_ref_"+refNum+" LIKE '"+userAndBatchId+"%'";
    		log.info("srcQue"+srcQue);
    		resultSrc=stmtSrc.executeQuery(srcQue);

    		String trgQue="select * from "+schemaName+".t_reconciliation_result where target_view_id is not null and recon_reference='"+result2.getShort("recon_reference")+"' and appr_ref_"+refNum+" LIKE '"+userAndBatchId+"%'";
    		log.info("trgQue"+trgQue);
    		resultTrg=stmtTrg.executeQuery(trgQue);


    		//for src record detals

    		ResultSetMetaData rsmdSrc = resultSrc.getMetaData();
    		int columnsNumberSrc = rsmdSrc.getColumnCount();
    		int columnCount = rsmdSrc.getColumnCount();

    		List<String> dvSrcList=new ArrayList<String>();
    		List<String> dvTrgList=new ArrayList<String>();
    		while(resultSrc.next()){
    			HashMap map2=new HashMap();
    			for (int i = 1; i <= columnCount; i++ ) {
    				String name = rsmdSrc.getColumnName(i); 
    				for(int t=0,num=1;t<columnsNumberSrc;t++, num++){ 
    					String Val=resultSrc.getString(num);
    				}
    				if(name.equalsIgnoreCase("id"))
    				{
    					if(resultSrc.getString("id")!=null)
    						map2.put("id", resultSrc.getString("id"));
    					else
    						map2.put("id", "");
    				}
    				if(name.equalsIgnoreCase("recon_reference"))
    				{
    					if(resultSrc.getString("recon_reference")!=null)
    						map2.put("reconReference", resultSrc.getString("recon_reference"));
    					else
    						map2.put("reconReference", "");
    				}
    				if(name.equalsIgnoreCase("reconciled_date"))
    				{
    					if(resultSrc.getString("reconciled_date")!=null)
    						map2.put("reconciledDate", resultSrc.getString("reconciled_date"));
    					else
    						map2.put("reconciledDate", "");
    				}
    				if(name.equalsIgnoreCase("original_row_id"))
    				{
    					if(resultSrc.getString("original_row_id")!=null)
    						map2.put("orginalRowId", resultSrc.getString("original_row_id"));
    					else
    						map2.put("orginalRowId", "");
    				}
    				if(name.equalsIgnoreCase("original_view_id"))
    				{
    					//map2.put("recordType", "Source");
    					DataViews dvName=dataViewsRepository.findOne(Long.valueOf(resultSrc.getString("original_view_id")));
    					//map2.put("viewId", Long.valueOf(resultSrc.getString("original_view_id")));
    					//map2.put("viewName", dvName.getDataViewName());
    					log.info("notBatch.getTenantId() :"+notBatch.getTenantId());
    					List<String> headerList=reconciliationResultService.getViewColumnHeadersInSequenceForApproval(Long.valueOf(resultSrc.getString("original_view_id")),Long.valueOf(resultSrc.getString("reconciliation_rule_group_id")),notBatch.getTenantId(),"Source");

    					log.info("headerList Src:"+headerList);

    					List<HashMap> alignInfo=new ArrayList<HashMap>();

    					//to set empty list when there is no alignInfo for source
    					List<HashMap> alignInfoEmptyList=new ArrayList<HashMap>();
    					//to fetch src view
    					String resSrcView="select * from "+schemaName+"."+dvName.getDataViewName().toLowerCase() +" where scrIds ="+resultSrc.getString("original_row_id");
    					resultTrgView=stmtSrcView.executeQuery(resSrcView);
    					ResultSetMetaData rsmdhdrSrc = resultTrgView.getMetaData();
    					int columnCountViewSrc= rsmdhdrSrc.getColumnCount();
    					while (resultTrgView.next())
    					{
        					alignInfo=reconciliationResultService.getAppActOrRecColsAlignInfo(Long.valueOf(resultSrc.getString("original_view_id")),Long.valueOf(resultSrc.getString("reconciliation_rule_group_id")),notBatch.getTenantId(),"Source",headerList);

    						HashMap map=new HashMap();
    						for (int j = 1; j <= columnCountViewSrc; j++ ) {
    							String viewColumnName = rsmdhdrSrc.getColumnName(j); 
    							map.put(viewColumnName, resultTrgView.getString(j));
    						}
    						
    						Boolean header=false;
    						HashMap finalDvList=new HashMap();
    						for(int head=0;head<headerList.size();head++)
    						{

    							if(map.containsKey(headerList.get(head).toString()))
    							{
    								header=true;
    								map2.put(headerList.get(head), map.get(headerList.get(head)));
    							}
    						}

    					}
    					map2.put("alignInfo", alignInfo);
    					if(dvSrcList.contains(dvName.getDataViewName()))
    					{
    						log.info("don add to list");
    					}
    					else
    						dvSrcList.add(dvName.getDataViewName());

    				}
    			}
    			srcRecList.add(map2);
    		}

    		//for target record details

    		ResultSetMetaData rsmdTrg = resultTrg.getMetaData();
    		log.info("col count Trg: "+rsmdTrg.getColumnCount());
    		int columnsNumberTrg = rsmdTrg.getColumnCount();
    		log.info("columnsNumber Trg: "+columnsNumberTrg);
    		int columnCount1 = rsmdSrc.getColumnCount();


    		while(resultTrg.next()){
    			HashMap map2=new HashMap();
    			for (int i = 1; i <= columnCount1; i++ ) {
    				String name = rsmdTrg.getColumnName(i); 
    				for(int t=0,num=1;t<columnsNumberTrg;t++, num++){ 
    					String Val=resultTrg.getString(num);
    				}
    				if(name.equalsIgnoreCase("id"))
    				{
    					if(resultTrg.getString("id")!=null)
    						map2.put("id", resultTrg.getString("id"));
    					else
    						map2.put("id", "");
    				}
    				if(name.equalsIgnoreCase("reconciled_date"))
    				{
    					if(resultTrg.getString("reconciled_date")!=null)
    						map2.put("reconciledDate", resultTrg.getString("reconciled_date"));
    					else
    						map2.put("reconciledDate", "");
    				}
    				if(name.equalsIgnoreCase("target_row_id"))
    				{
    					if(resultTrg.getString("target_row_id")!=null)
    						map2.put("targetRowId", resultTrg.getString("target_row_id"));
    					else
    						map2.put("targetRowId", "");
    				}
    				if(name.equalsIgnoreCase("target_view_id"))
    				{
    					
    					DataViews dvName=dataViewsRepository.findOne(Long.valueOf(resultTrg.getString("target_view_id")));
    					List<String> headerList=reconciliationResultService.getViewColumnHeadersInSequenceForApproval(Long.valueOf(resultTrg.getString("target_view_id")),Long.valueOf(resultTrg.getString("reconciliation_rule_group_id")),notBatch.getTenantId(),"Target");
    					log.info("headerList Trg:"+ headerList);
    					List<HashMap> alignInfo=new ArrayList<HashMap>();
    					

    					//to fetcg target view
    					String resTrgView="select * from "+schemaName+"."+dvName.getDataViewName().toLowerCase() +" where scrIds ="+resultTrg.getString("target_row_id");
    					resultTrgView=stmtSrcView.executeQuery(resTrgView);
    					ResultSetMetaData rsmdhdrTrg = resultTrgView.getMetaData();
    					int columnCountViewTarg= rsmdhdrTrg.getColumnCount();
    					log.info("resultTrgView :"+resultTrgView);
    					
    					
    					while (resultTrgView.next())
    					{
        					alignInfo=reconciliationResultService.getAppActOrRecColsAlignInfo(Long.valueOf(resultTrg.getString("target_view_id")),Long.valueOf(resultTrg.getString("reconciliation_rule_group_id")),notBatch.getTenantId(),"Target",headerList);

    						
    						HashMap map=new HashMap();
    						for (int j = 1; j <= columnCountViewTarg; j++ ) {
    							String viewColumnName = rsmdhdrTrg.getColumnName(j); 
    							map.put(viewColumnName, resultTrgView.getString(j));
    						}
    					
    						
    						Boolean header=false;
    						HashMap finalDvList=new HashMap();
    						for(int head=0;head<headerList.size();head++)
    						{

    							if(map.containsKey(headerList.get(head).toString()))
    							{
    								log.info("if header name matches :");
    								header=true;
    								map2.put(headerList.get(head), map.get(headerList.get(head)));
    							}


    						}
    					
    					}
    					map2.put("alignInfo", alignInfo);
    					
    					if(dvTrgList.contains(dvName.getDataViewName()))
    					{
    						log.info("don add to list");
    					}
    					else
    						dvTrgList.add(dvName.getDataViewName());
    				}

    			}
    			tarRecList.add(map2);
    		}

    		HashMap map=new HashMap();
    		map.put("source", srcRecList);
    		map.put("target", tarRecList);
    		map.put("reconReference", result2.getString("recon_reference"));
    		map.put("sourceName", dvSrcList.toString().replaceAll("\\]", "").replaceAll("\\[", ""));
    		map.put("targetName", dvTrgList.toString().replaceAll("\\]", "").replaceAll("\\[", ""));
    		finalMap.add(map);

    	}



    	if(conn!=null)
    		conn.close();
    	if(stmt!=null)
    		stmt.close();
    	if(result2!=null)
    		result2.close();
    	if(resultSrc!=null)
    		resultSrc.close();
    	if(resultTrg!=null)
    		resultTrg.close();
    	if(resultTrgView!=null)
    		resultTrgView.close();
    	if(stmtTrgView!=null)
    		stmtTrgView.close();
    	if(stmtSrcView!=null)
    		stmtSrcView.close();
    	if(stmtSrc!=null)
    		stmtSrc.close();
    	if(stmtTrg!=null)
    		stmtTrg.close();
    	return finalMap;

    }*/
    
    
    /**
     * author ravali
     * getApprovalAssmtDetails for a oarticular batch
     * @param notificationId
     * @param tenantId
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    @GetMapping("/getApprovalAssmtDetails")
    @Timed
    public HashMap getApprovalAssmtDetails(@RequestParam Long notificationId) throws ClassNotFoundException, SQLException
    {
    	log.info("Request Rest to get approval Assngmt details with notification id :"+notificationId);
    	NotificationBatch notBtc=notificationBatchRepository.findOne(notificationId);
    	HashMap batchMap=new HashMap();
    	if(notBtc!=null)
    	{

    		batchMap.put("batchId", notBtc.getId());
    		batchMap.put("batchName", notBtc.getNotificationName());
    		batchMap.put("currentApprover",notBtc.getCurrentApprover());
    		batchMap.put("level", notBtc.getRefLevel());

    		List<HashMap> appInfoList=new ArrayList<HashMap>();
    		List<ApprovalRuleAssignment> appRuleAsnmtList=approvalRuleAssignmentRepository.findByRuleId(notBtc.getRuleId());
    		log.info("appRuleAsnmtList.size() :"+appRuleAsnmtList.size());
    		Integer seq=0;
    		List<String> status=new ArrayList<String>();
    		for(int j=0;j<appRuleAsnmtList.size();j++)
    		{
    			HashMap appInfo=new HashMap();
    			
                log.info("appRuleAsnmtList :"+appRuleAsnmtList.get(j));
                log.info("notBtc.getRefLevel() :"+notBtc.getRefLevel());
    			if(j+1<notBtc.getRefLevel())
    			{
    				log.info("notBtc.getRefLevel() in if");
    				//to get approval date accessing parent batch id
    				NotificationBatch parentBatch=notificationBatchRepository.findOne(notBtc.getParentBatch());
    				if(parentBatch!=null)
    		    			appInfo.put("approvalDate", parentBatch.getLastUpdatedDate());
    				appInfo.put("approvalStatus", "Approved");
    				status.add("Approved");
    			}
    			else if(j+1==notBtc.getRefLevel())
    			{
    				log.info("ref and index matches :"+notBtc.getStatus());
    				log.info("ref and index matches :"+notBtc.getLastUpdatedDate());
    			
    				LookUpCode lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("APPROVAL_STATUS", notBtc.getStatus(), notBtc.getTenantId());
    				if(lookUpCode.getMeaning().equalsIgnoreCase("APPROVED"))
    					appInfo.put("approvalDate", notBtc.getLastUpdatedDate());
    			
    				appInfo.put("approvalStatus", lookUpCode.getMeaning());
    				status.add(lookUpCode.getMeaning());
    			}
    			else if(j+1>notBtc.getRefLevel())
    			{
    				log.info("if appAsst is grater tha ref level");
    				List<ReconciliationResult> reconStatus=reconciliationResultRepository.findByApprovalBatchId(notBtc.getId());
    				log.info("reconStatus.size :"+reconStatus.size());
    				if(reconStatus.size()>0)
    				{
    					LookUpCode lookUpCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("APPROVAL_STATUS", reconStatus.get(0).getFinalStatus(), notBtc.getTenantId());
    					appInfo.put("approvalDate", notBtc.getLastUpdatedDate());
    					if(status.contains("Rejected")||status.contains("InProcess"))
    						appInfo.put("approvalStatus", "");
    					else
    						appInfo.put("approvalStatus", lookUpCode.getMeaning());
    					status.add(lookUpCode.getMeaning());
    				}
    				else
    				{
    					appInfo.put("approvalStatus", "");
    				}
    				
    					
    			}
    			seq=seq+1;
    			appInfo.put("seq",seq);
    			if(appRuleAsnmtList.get(j).getAssignType().equalsIgnoreCase("USER"))
    			{
    				HashMap map=userJdbcService.jdbcConnc(appRuleAsnmtList.get(j).getAssigneeId(), notBtc.getTenantId());
    			if(map!=null && map.get("assigneeName")!=null)
    				appInfo.put("name",map.get("assigneeName"));
    			}
    			
    			log.info("appInfo :"+appInfo);
    			appInfoList.add(appInfo);
    		}
    		log.info("status :"+status);
    		if(status.contains("InProcess"))
				batchMap.put("approvalStatus", "InProcess");
			else if(status.contains("Rejected"))
				batchMap.put("approvalStatus", "Rejected");
			else
				batchMap.put("approvalStatus", "Approved");
    		batchMap.put("approversInfo", appInfoList)	;

    		List<HashMap> childList=new ArrayList<HashMap>();
    		List<NotificationBatch> parentBatchList=notificationBatchRepository.findByParentBatch(notificationId);
    		for(NotificationBatch parentBatch:parentBatchList)
    		{
    			HashMap child=new HashMap();
    			child.put("batchId", parentBatch.getId());
    			child.put("batchName", parentBatch.getNotificationName());
    			child.put("currentApprover",parentBatch.getCurrentApprover());
    			child.put("level", parentBatch.getRefLevel());
    			child.put("approvalStatus", parentBatch.getStatus());
    			childList.add(child);

    		}
    		batchMap.put("childInfo",childList);

    		String dbUrl=env.getProperty("spring.datasource.url");
    		String[] parts=dbUrl.split("[\\s@&?$+-]+");
    		String host = parts[0].split("/")[2].split(":")[0];
    		log.info("host :"+host);
    		String schemaName=parts[0].split("/")[3];
    		log.info("schemaName :"+schemaName);
    		String userName = env.getProperty("spring.datasource.username");
    		String password = env.getProperty("spring.datasource.password");
    		String jdbcDriver = env.getProperty("spring.datasource.jdbcdriver");

    		Connection conn = null;
    		Statement stmt = null;
    		conn = DriverManager.getConnection(dbUrl, userName, password);
    		log.info("Connected database successfully...");
    		stmt = conn.createStatement();
    		ResultSet result2=null;
    		//Class.forName("com.mysql.jdbc.Driver");
    		Integer I = null;
    		if(notBtc.getRefLevel()!=null)
    		{
    			I=notBtc.getRefLevel();
    		}
    		String refNum=String.valueOf(I);
    		if(refNum.length()<=1)
    			refNum="0"+refNum;
    		log.info("refNum :"+refNum);
    		List<LookUpCode> lookUpCodes=lookUpCodeRepository.findByTenantIdAndLookUpType( notBtc.getTenantId(), "APPROVAL_STATUS");
    		if(lookUpCodes.size()>0)
    		{
    			HashMap detail=new HashMap();
    			Long total=0l;
    			for(int i=0;i<lookUpCodes.size();i++)
    			{
    				String userAndBatchId =notBtc.getCurrentApprover()+"|"+notBtc.getId()+"|"+lookUpCodes.get(i).getMeaning();
    				log.info("userAndBatchId :"+userAndBatchId);
    				String query="select count(*) from "+schemaName+".t_reconciliation_result where appr_ref_"+refNum+" LIKE '"+userAndBatchId+"%'";
    				log.info("query :"+query);
    				result2=stmt.executeQuery(query);
    				while(result2.next())
    				{
    					if(result2.getString("count(*)")!=null)
    						detail.put(lookUpCodes.get(i).getMeaning(), result2.getString("count(*)"));
    					total=total+Long.valueOf(result2.getString("count(*)"));

    				}
    				log.info("detail :"+detail);
    				detail.put("total", total);
    				batchMap.put("details",detail);
    			}
    		}
    		
    		if(conn!=null)
    			conn.close();
    		if(stmt!=null)
    			stmt.close();
    		if(result2!=null)
    			result2.close();
    	}

    	return batchMap;
    }
/**
 * author :ravali
 */
    @GetMapping("/getDistinctRefNumForBatchId")
    @Timed
    public List<HashMap> getDistinctRefNumForBatchId(@RequestParam Long notificationId,@RequestParam(value = "status", required = false) String status,@RequestParam(value = "pageNumber", required = false) Integer  pageNumber,@RequestParam(value = "pageSize", required = false) Integer pageSize,HttpServletResponse response) throws SQLException
    {
    	log.info("Request Rest to get distinct recon ref on notification id :"+notificationId);
    	NotificationBatch notBatch=notificationBatchRepository.findOne(notificationId);
    	Integer I = null;
    	if(notBatch.getRefLevel()!=null)
    	{
    		I=notBatch.getRefLevel();
    	}
    	String refNum=String.valueOf(I);
    	if(refNum.length()<=1)
    		refNum="0"+refNum;
    	log.info("refNum :"+refNum);
    	List<HashMap> finalMap=new ArrayList<HashMap>();

    	String userAndBatchId ="";
    	if(status!=null)
    		userAndBatchId =notBatch.getCurrentApprover()+"|"+notBatch.getId()+"|"+status;
    	else
    		userAndBatchId =notBatch.getCurrentApprover()+"|"+notBatch.getId();
    	log.info("userAndBatchId :"+userAndBatchId);

    	if(pageNumber==null&&pageSize==null)
    	{
    		pageNumber=0;
    		pageSize=25;
    	}

    	String query="select distinct(reconReference) from ReconciliationResult where apprRef"+refNum+" LIKE '"+userAndBatchId+"%'";
    	
    	
    	String queryCount="select distinct(reconReference) from ReconciliationResult where apprRef"+refNum+" LIKE '"+userAndBatchId+"%'";
    	Query distReconRefQueryCount=em.createQuery(query);
    	List<String> distReconCount=distReconRefQueryCount.getResultList();
    	response.addIntHeader("XCOUNT", distReconCount.size());
    	
    	//response
    	Query distReconRefQuery=em.createQuery(query);
    	
    	distReconRefQuery.setFirstResult(((pageNumber+1)-1) * pageSize);
    	distReconRefQuery.setMaxResults(pageSize);
    	List<String> distReconRef=distReconRefQuery.getResultList();
    	log.info("distReconRef :"+distReconRef.size());
    	for(int i=0;i<distReconRef.size();i++)
    	{
    		HashMap recRef=new HashMap();
    		if(distReconRef.get(i)!=null)
    			recRef.put("reconReference", distReconRef.get(i));
    		if(!recRef.isEmpty())
    			finalMap.add(recRef);	


    	}
    	return finalMap;

}
    
    
 
    
    /**
     * author:ravali
     */

    @GetMapping("/getApprovalHistorySrcTargetTypedQuery")
    @Timed
    public List<HashMap> getApprovalHistorySrcTargetTypedQuery(@RequestParam Long notificationId,@RequestParam String reconRef) throws SQLException
    {
    	log.info("Request Rest to get Approval history based on notification id :"+notificationId);
    	NotificationBatch notBatch=notificationBatchRepository.findOne(notificationId);
    	Integer I = null;
    	if(notBatch.getRefLevel()!=null)
    	{
    		I=notBatch.getRefLevel();
    	}
    	String refNum=String.valueOf(I);
    	if(refNum.length()<=1)
    		refNum="0"+refNum;
    	log.info("refNum :"+refNum);
    	List<HashMap> finalMap=new ArrayList<HashMap>();
    	String dbUrl=env.getProperty("spring.datasource.url");
    	String[] parts=dbUrl.split("[\\s@&?$+-]+");
    	String host = parts[0].split("/")[2].split(":")[0];
    	log.info("host :"+host);
    	String schemaName=parts[0].split("/")[3];
    	log.info("schemaName :"+schemaName);
    	String userName = env.getProperty("spring.datasource.username");
    	String password = env.getProperty("spring.datasource.password");
    	String jdbcDriver = env.getProperty("spring.datasource.jdbcdriver");

    	Connection conn = null;

    	Statement stmtSrcView= null;
    	Statement stmtTrgView= null;

    	conn = DriverManager.getConnection(dbUrl, userName, password);
    	log.info("Connected database successfully...");

    	stmtSrcView = conn.createStatement();
    	stmtTrgView = conn.createStatement();


    	ResultSet resultSrcView=null;
    	ResultSet resultTrgView=null;


    	String userAndBatchId =notBatch.getCurrentApprover()+"|"+notBatch.getId();
    	log.info("userAndBatchId :"+userAndBatchId);



    	try
    	{


    		List<HashMap> alignSrcInfo=new ArrayList<HashMap>();
    		List<HashMap> alignTrgInfo=new ArrayList<HashMap>();
    		List<String> headerSrcList=new ArrayList<String>();
    		List<String> headerTrgList=new ArrayList<String>();
    		List<HashMap> srcRecList=new ArrayList<HashMap>();
    		List<HashMap> tarRecList=new ArrayList<HashMap>();

    		String srcQue="from ReconciliationResult where originalViewId is not null and reconReference='"+reconRef+"' and apprRef"+refNum+" LIKE '"+userAndBatchId+"%'";
    		List<ReconciliationResult> reconSrcResultList=em.createQuery(srcQue).getResultList();
    		//	resultSrc=stmtSrc.executeQuery(srcQue);

    		String trgQue="from ReconciliationResult where targetViewId is not null and reconReference='"+reconRef+"' and apprRef"+refNum+" LIKE '"+userAndBatchId+"%'";
    		List<ReconciliationResult> reconTrgResultList=em.createQuery(trgQue).getResultList();
    		log.info("reconSrcResultList :"+reconSrcResultList.size());
    		log.info("reconTrgResultList :"+reconTrgResultList.size());

    		//for src record detals



    		List<String> dvSrcList=new ArrayList<String>();
    		List<String> dvTrgList=new ArrayList<String>();
    		//while(resultSrc.next())

    		for(ReconciliationResult reconSrcResult:reconSrcResultList){
    			HashMap map2=new HashMap();

    			if(reconSrcResult.getId()!=null)
    				map2.put("id", reconSrcResult.getId());
    			else
    				map2.put("id", "");

    			if(reconSrcResult.getReconciledDate()!=null)
    				map2.put("reconciledDate", reconSrcResult.getReconciledDate());
    			else
    				map2.put("reconciledDate", "");

    			if(reconSrcResult.getOriginalRowId()!=null)
    				map2.put("orginalRowId", reconSrcResult.getOriginalRowId());
    			else
    				map2.put("orginalRowId", "");
    			if(reconSrcResult.getReconReference()!=null)
    				map2.put("reconReference", reconSrcResult.getReconReference());

    			if(reconSrcResult.getOriginalViewId()!=null)
    			{

    				DataViews dvName=dataViewsRepository.findOne(reconSrcResult.getOriginalViewId());

    				headerSrcList=reconciliationResultService.getViewColumnHeadersInSequenceForApproval(reconSrcResult.getOriginalViewId(),reconSrcResult.getReconciliationRuleGroupId(),notBatch.getTenantId(),"Source");



    				//to set empty list when there is no alignInfo for source
    				List<HashMap> alignInfoEmptyList=new ArrayList<HashMap>();
    				//to fetch src view
    				String resSrcView="select * from "+schemaName+".`"+dvName.getDataViewName().toLowerCase() +"` where scrIds ="+reconSrcResult.getOriginalRowId();

    				resultSrcView=stmtSrcView.executeQuery(resSrcView);
    				ResultSetMetaData rsmdhdrSrc = resultSrcView.getMetaData();
    				int columnCountViewSrc= rsmdhdrSrc.getColumnCount();
    				while (resultSrcView.next())
    				{

    					HashMap map=new HashMap();
    					for (int j = 1; j <= columnCountViewSrc; j++ ) {
    						String viewColumnName = rsmdhdrSrc.getColumnName(j); 
    						map.put(viewColumnName, resultSrcView.getString(j));
    					}

    					Boolean header=false;
    					HashMap finalDvList=new HashMap();
    					for(int head=0;head<headerSrcList.size();head++)
    					{

    						if(map.containsKey(headerSrcList.get(head).toString()))
    						{
    							header=true;
    							map2.put(headerSrcList.get(head), map.get(headerSrcList.get(head)));
    						}
    					}

    				}

    				if(dvSrcList.contains(dvName.getDataViewName()))
    				{
    					log.info("don add to list");
    				}
    				else
    					dvSrcList.add(dvName.getDataViewName());

    			}

    			srcRecList.add(map2);
    		}

    		//for target record details


    		for(ReconciliationResult reconTrgResult:reconTrgResultList){
    			HashMap map2=new HashMap();


    			if(reconTrgResult.getId()!=null)

    				map2.put("id", reconTrgResult.getId());
    			else
    				map2.put("id", "");

    			if(reconTrgResult.getReconciledDate()!=null)

    				map2.put("reconciledDate", reconTrgResult.getReconciledDate());
    			else
    				map2.put("reconciledDate", "");

    			if(reconTrgResult.getTargetRowId()!=null)

    				map2.put("targetRowId", reconTrgResult.getTargetRowId());
    			else
    				map2.put("targetRowId", "");

    			if(reconTrgResult.getTargetViewId()!=null)
    			{

    				DataViews dvName=dataViewsRepository.findOne(reconTrgResult.getTargetViewId());
    				headerTrgList=reconciliationResultService.getViewColumnHeadersInSequenceForApproval(reconTrgResult.getTargetViewId(),reconTrgResult.getReconciliationRuleGroupId(),notBatch.getTenantId(),"Target");

    				List<HashMap> alignInfo=new ArrayList<HashMap>();


    				//to fetcg target view
    				String resTrgView="select * from "+schemaName+".`"+dvName.getDataViewName().toLowerCase() +"` where scrIds ="+reconTrgResult.getTargetRowId();
    				resultTrgView=stmtTrgView.executeQuery(resTrgView);
    				ResultSetMetaData rsmdhdrTrg = resultTrgView.getMetaData();
    				int columnCountViewTarg= rsmdhdrTrg.getColumnCount();

    				while (resultTrgView.next())
    				{

    					HashMap map=new HashMap();
    					for (int j = 1; j <= columnCountViewTarg; j++ ) {
    						String viewColumnName = rsmdhdrTrg.getColumnName(j); 
    						map.put(viewColumnName, resultTrgView.getString(j));
    					}


    					Boolean header=false;
    					HashMap finalDvList=new HashMap();
    					for(int head=0;head<headerTrgList.size();head++)
    					{

    						if(map.containsKey(headerTrgList.get(head).toString()))
    						{
    							header=true;
    							map2.put(headerTrgList.get(head), map.get(headerTrgList.get(head)));
    						}


    					}

    				}


    				if(dvTrgList.contains(dvName.getDataViewName()))
    				{
    					log.info("don add to list");
    				}
    				else
    					dvTrgList.add(dvName.getDataViewName());
    			}


    			tarRecList.add(map2);
    		}

    		HashMap map=new HashMap();
    		map.put("source", srcRecList);
    		map.put("target", tarRecList);
    		List<HashMap> srccolumnOptions=new ArrayList<HashMap>();
    		alignSrcInfo=reconciliationResultService.getAppActOrRecColsAlignInfo(reconSrcResultList.get(0).getOriginalViewId(),reconSrcResultList.get(0).getReconciliationRuleGroupId(),notBatch.getTenantId(),"Source",headerSrcList);
    		map.put("srcnotificationBatchTableColumns", alignSrcInfo);
    		for(int x=0;x<alignSrcInfo.size();x++){
    			HashMap srccolumn=new HashMap();
    			srccolumn.put("label", alignSrcInfo.get(x).get("header"));
    			srccolumn.put("value", alignSrcInfo.get(x));
    			srccolumnOptions.add(srccolumn);
    		}
    		List<HashMap> trgcolumnOptions=new ArrayList<HashMap>();
    		alignTrgInfo=reconciliationResultService.getAppActOrRecColsAlignInfo(reconTrgResultList.get(0).getTargetViewId(),reconTrgResultList.get(0).getReconciliationRuleGroupId(),notBatch.getTenantId(),"Target",headerTrgList);
    		for(int x=0;x<alignTrgInfo.size();x++){
    			HashMap trgcolumn=new HashMap();
    			trgcolumn.put("label", alignSrcInfo.get(x).get("header"));
    			trgcolumn.put("value", alignSrcInfo.get(x));
    			trgcolumnOptions.add(trgcolumn);
    		}
    		map.put("trgnotificationBatchTableColumns", alignTrgInfo);
    		map.put("reconReference", reconRef);
    		map.put("sourceName", dvSrcList.toString().replaceAll("\\]", "").replaceAll("\\[", ""));
    		map.put("targetName", dvTrgList.toString().replaceAll("\\]", "").replaceAll("\\[", ""));
    		map.put("srccolumnOptions", srccolumnOptions);
    		map.put("trgcolumnOptions", trgcolumnOptions);
    		finalMap.add(map);

    	}
    	finally
    	{
    		log.info("finally");
    		if(conn!=null)
    			conn.close();
    		if(stmtSrcView!=null)
    			stmtSrcView.close();
    		if(resultSrcView!=null)
    			resultSrcView.close();
    		if(stmtTrgView!=null)
    			stmtTrgView.close();
    		if(resultTrgView!=null)
    			resultTrgView.close();

    	}

    	log.info("/*****end****/");
    	return finalMap;

    }
    
    
    /**
     * author:ravali
     * @param notificationId
     * @param status
     * @param pageNumber
     * @param pageSize
     * @param response
     * @return
     * @throws SQLException
     */
    
    @GetMapping("/getDistinctOrginalRowIdForBatchId")
    @Timed
    public List<HashMap> getDistinctOrginalRowIdForBatchId(@RequestParam Long notificationId,@RequestParam(value = "status", required = false) String status,@RequestParam(value = "pageNumber", required = false) Integer  pageNumber,@RequestParam(value = "pageSize", required = false) Integer pageSize,HttpServletResponse response) throws SQLException
    {
    	log.info("Request Rest to get distinct orginal rowIds of notification id :"+notificationId);
    	NotificationBatch notBatch=notificationBatchRepository.findOne(notificationId);
    	Integer I = null;
    	if(notBatch.getRefLevel()!=null)
    	{
    		I=notBatch.getRefLevel();
    	}
    	String refNum=String.valueOf(I);
    	if(refNum.length()<=1)
    		refNum="0"+refNum;
    	log.info("refNum :"+refNum);
    	List<HashMap> finalMap=new ArrayList<HashMap>();

    	String userAndBatchId ="";
    	if(status!=null)
    		userAndBatchId =notBatch.getCurrentApprover()+"|"+notBatch.getId()+"|"+status;
    	else
    		userAndBatchId =notBatch.getCurrentApprover()+"|"+notBatch.getId();
    	log.info("userAndBatchId :"+userAndBatchId);

    	if(pageNumber==null&&pageSize==null)
    	{
    		pageNumber=0;
    		pageSize=25;
    	}

    	String query="select distinct(originalRowId) from AccountingData where apprRef"+refNum+" LIKE '"+userAndBatchId+"%'";
    	
    	
    	String queryCount="select distinct(originalRowId) from AccountingData where apprRef"+refNum+" LIKE '"+userAndBatchId+"%'";
    	Query distReconRefQueryCount=em.createQuery(query);
    	List<String> distReconCount=distReconRefQueryCount.getResultList();
    	response.addIntHeader("XCOUNT", distReconCount.size());
    	
    	//response
    	Query distReconRefQuery=em.createQuery(query);
    	
    	distReconRefQuery.setFirstResult(((pageNumber+1)-1) * pageSize);
    	distReconRefQuery.setMaxResults(pageSize);
    	List<String> distReconRef=distReconRefQuery.getResultList();
    	log.info("distReconRef :"+distReconRef.size());
    	for(int i=0;i<distReconRef.size();i++)
    	{
    		HashMap recRef=new HashMap();
    		if(distReconRef.get(i)!=null)
    			recRef.put("orginalRowIds", distReconRef.get(i));
    		if(!recRef.isEmpty())
    			finalMap.add(recRef);	


    	}
    	return finalMap;

}
    /**
     * author:ravali
     * @param notificationId
     * @param reconRef
     * @return
     * @throws SQLException
     */
    @GetMapping("/getViewReportForAccounting")
    @Timed
    public List<HashMap> getViewReportForAccounting(@RequestParam Long notificationId,@RequestParam String rowId) throws SQLException
    {
    	log.info("Request Rest to get Approval history based on notification id :"+notificationId);
    	NotificationBatch notBatch=notificationBatchRepository.findOne(notificationId);
    	Integer I = null;
    	if(notBatch.getRefLevel()!=null)
    	{
    		I=notBatch.getRefLevel();
    	}
    	String refNum=String.valueOf(I);
    	if(refNum.length()<=1)
    		refNum="0"+refNum;
    	log.info("refNum :"+refNum);
    	List<HashMap> finalMap=new ArrayList<HashMap>();
    	String dbUrl=env.getProperty("spring.datasource.url");
    	String[] parts=dbUrl.split("[\\s@&?$+-]+");
    	String host = parts[0].split("/")[2].split(":")[0];
    	log.info("host :"+host);
    	String schemaName=parts[0].split("/")[3];
    	log.info("schemaName :"+schemaName);
    	String userName = env.getProperty("spring.datasource.username");
    	String password = env.getProperty("spring.datasource.password");
    	String jdbcDriver = env.getProperty("spring.datasource.jdbcdriver");

    	Connection conn = null;

    	Statement stmtDebitView= null;
    	Statement stmtCreditView= null;

    	conn = DriverManager.getConnection(dbUrl, userName, password);
    	log.info("Connected database successfully...");

    	stmtDebitView = conn.createStatement();
    	stmtCreditView = conn.createStatement();


    	ResultSet resultDebitView=null;
    	ResultSet resultCreditView=null;
    	

    	String userAndBatchId =notBatch.getCurrentApprover()+"|"+notBatch.getId();
    	log.info("userAndBatchId :"+userAndBatchId);



    	try
    	{


    		List<HashMap> alignDebitInfo=new ArrayList<HashMap>();
    		List<HashMap> alignCreditInfo=new ArrayList<HashMap>();

    		List<String> headerDebitList=new ArrayList<String>();
    		List<String> headerCreditList=new ArrayList<String>();

    		List<HashMap> debitAcctList=new ArrayList<HashMap>();
    		List<HashMap> creditAcctList=new ArrayList<HashMap>();
    		
    		


    		String debitViewQue="from AccountingData where originalViewId is not null and originalRowId='"+rowId+"' and lineType ='DEBIT' and apprRef"+refNum+" LIKE '"+userAndBatchId+"%'";
    		List<AccountingData> acctDebitResultList=em.createQuery(debitViewQue).getResultList();
    		log.info("acctDebitResultList :"+acctDebitResultList.size());
    		
    		String creditViewQue="from AccountingData where originalViewId is not null and originalRowId='"+rowId+"' and lineType ='CREDIT' and apprRef"+refNum+" LIKE '"+userAndBatchId+"%'";
    		List<AccountingData> acctCreditResultList=em.createQuery(creditViewQue).getResultList();
    		log.info("acctCreditResultList :"+acctCreditResultList.size());
    		

    		

    		List<String> dvDebitList=new ArrayList<String>();
    		List<String> dvCreditList=new ArrayList<String>();

    		for(AccountingData acctDebResult:acctDebitResultList){
    			HashMap map2=new HashMap();

    			if(acctDebResult.getId()!=null)
    				map2.put("id", acctDebResult.getId());
    			else
    				map2.put("id", "");

    			if(acctDebResult.getOriginalRowId()!=null)
    				map2.put("orginalRowId", acctDebResult.getOriginalRowId());
    			else
    				map2.put("orginalRowId", "");

    			if(acctDebResult.getOriginalViewId()!=null)
    			{
    				
    			

    				DataViews dvName=dataViewsRepository.findOne(acctDebResult.getOriginalViewId());

    				headerDebitList=accountingDataService.getViewColumnHeadersMapInSequenceForAcctApproval(acctDebResult.getOriginalViewId(),notBatch.getTenantId(),acctDebResult.getAcctGroupId());



    				//to set empty list when there is no alignInfo for source
    				List<HashMap> alignInfoEmptyList=new ArrayList<HashMap>();
    				//to fetch src view
    				String actDView="select * from "+schemaName+"."+dvName.getDataViewName().toLowerCase() +" where scrIds ="+acctDebResult.getOriginalRowId();

    				resultDebitView=stmtDebitView.executeQuery(actDView);
    				ResultSetMetaData rsmdhdrSrc = resultDebitView.getMetaData();
    				int columnCountViewSrc= rsmdhdrSrc.getColumnCount();
    				while (resultDebitView.next())
    				{

    					HashMap map=new HashMap();
    					for (int j = 1; j <= columnCountViewSrc; j++ ) {
    						String viewColumnName = rsmdhdrSrc.getColumnName(j); 
    						map.put(viewColumnName, resultDebitView.getString(j));
    					}

    					Boolean header=false;
    					HashMap finalDvList=new HashMap();
    					for(int head=0;head<headerDebitList.size();head++)
    					{

    						if(map.containsKey(headerDebitList.get(head).toString()))
    						{
    							header=true;
    							map2.put(headerDebitList.get(head), map.get(headerDebitList.get(head)));
    						}
    					}

    				}

    				if(dvDebitList.contains(dvName.getDataViewName()))
    				{
    					log.info("don add to list");
    				}
    				else
    					dvDebitList.add(dvName.getDataViewName());

    			}

    			debitAcctList.add(map2);
    		}

    		
    		
    		//for Credit record details


    		for(AccountingData acctCreditResult:acctCreditResultList){
    			HashMap map2=new HashMap();


    			if(acctCreditResult.getId()!=null)

    				map2.put("id", acctCreditResult.getId());
    			else
    				map2.put("id", "");

    			

    			if(acctCreditResult.getOriginalRowId()!=null)

    				map2.put("orginalRowId", acctCreditResult.getOriginalRowId());
    			else
    				map2.put("orginalRowId", "");

    			if(acctCreditResult.getOriginalRowId()!=null)
    			{

    				DataViews dvName=dataViewsRepository.findOne(acctCreditResult.getOriginalRowId());
    				headerCreditList=accountingDataService.getViewColumnHeadersMapInSequenceForAcctApproval(acctCreditResult.getOriginalViewId(),notBatch.getTenantId(),acctCreditResult.getAcctGroupId());

    				List<HashMap> alignInfo=new ArrayList<HashMap>();


    				//to fetcg credit view
    				String resCreditView="select * from "+schemaName+"."+dvName.getDataViewName().toLowerCase() +" where scrIds ="+acctCreditResult.getOriginalRowId();
    				resultCreditView=stmtCreditView.executeQuery(resCreditView);
    				ResultSetMetaData rsmdhdrTrg = resultCreditView.getMetaData();
    				int columnCountViewTarg= rsmdhdrTrg.getColumnCount();

    				while (resultCreditView.next())
    				{

    					HashMap map=new HashMap();
    					for (int j = 1; j <= columnCountViewTarg; j++ ) {
    						String viewColumnName = rsmdhdrTrg.getColumnName(j); 
    						map.put(viewColumnName, resultCreditView.getString(j));
    					}


    					Boolean header=false;
    					HashMap finalDvList=new HashMap();
    					for(int head=0;head<headerCreditList.size();head++)
    					{

    						if(map.containsKey(headerCreditList.get(head).toString()))
    						{
    							header=true;
    							map2.put(headerCreditList.get(head), map.get(headerCreditList.get(head)));
    						}


    					}

    				}


    				if(dvCreditList.contains(dvName.getDataViewName()))
    				{
    					log.info("don add to list");
    				}
    				else
    					dvCreditList.add(dvName.getDataViewName());
    			}


    			creditAcctList.add(map2);
    		}

    	
    		
    		HashMap map=new HashMap();
    		map.put("debit", debitAcctList);
    		map.put("credit", creditAcctList);
    		List<HashMap> debitcolumnOptions=new ArrayList<HashMap>();
    	//	alignDebitInfo=accountingDataService.getAcctApprovalsColsAlignInfo(acctDebitResultList.get(0).getOriginalViewId(),acctDebitResultList.get(0).getAcctGroupId(),notBatch.getTenantId());
    		alignDebitInfo=accountingDataService.getAcctApprovalsColsAlignInfo(acctDebitResultList.get(0).getOriginalViewId(),acctDebitResultList.get(0).getAcctGroupId(),notBatch.getTenantId());
    		map.put("debitNotificationBatchTableColumns", alignDebitInfo);
    		for(int x=0;x<alignDebitInfo.size();x++){
    			HashMap debcolumn=new HashMap();
    			debcolumn.put("label", alignDebitInfo.get(x).get("header"));
    			debcolumn.put("value", alignDebitInfo.get(x));
    			debitcolumnOptions.add(debcolumn);
    		}
    		List<HashMap> creditcolumnOptions=new ArrayList<HashMap>();
    		if(acctCreditResultList.size()>0)
    		{
    		alignCreditInfo=accountingDataService.getAcctApprovalsColsAlignInfo(acctCreditResultList.get(0).getOriginalViewId(),acctCreditResultList.get(0).getAcctGroupId(),notBatch.getTenantId());
    		for(int x=0;x<alignCreditInfo.size();x++){
    			HashMap creditcolumn=new HashMap();
    			creditcolumn.put("label", alignCreditInfo.get(x).get("header"));
    			creditcolumn.put("value", alignCreditInfo.get(x));
    			creditcolumnOptions.add(creditcolumn);
    		}
    		}
    		map.put("CreditnotificationBatchTableColumns", alignCreditInfo);
    		map.put("sourceName", dvDebitList.toString().replaceAll("\\]", "").replaceAll("\\[", ""));
    		map.put("targetName", dvCreditList.toString().replaceAll("\\]", "").replaceAll("\\[", ""));
    		map.put("srccolumnOptions", debitcolumnOptions);
    		map.put("trgcolumnOptions", creditcolumnOptions);
    		finalMap.add(map);

    		
    		
    		
    		
    		

    	}
    	finally
    	{
    		log.info("finally");
    		if(conn!=null)
    			conn.close();
    		if(stmtDebitView!=null)
    			stmtDebitView.close();
    		if(resultDebitView!=null)
    			resultDebitView.close();
    		if(stmtCreditView!=null)
    			stmtCreditView.close();
    		if(resultCreditView!=null)
    			resultCreditView.close();
    	}

    	log.info("/*****end****/");
    	return finalMap;

    } 
    
    

/**
 * author :ravali
 * @param tenantId
 * @param module
 * @param offset
 * @param limit
 * @param response
 * @return
 */
   
@GetMapping("/notificationParentBatches")
@Timed
public List<LinkedHashMap> notificationParentBatches(@RequestParam Long tenantId,@RequestParam String module,@RequestParam(value = "page" , required = false) Integer offset,
		@RequestParam(value = "per_page", required = false) Integer limit,HttpServletResponse response)
		{
	log.info("Rest Resquest get batch details with respect to process instance info");
	List<LinkedHashMap> finalMapList=new ArrayList<LinkedHashMap>();

	List<NotificationBatch> notBatchList=new ArrayList<NotificationBatch>();
	PaginationUtil paginationUtil=new PaginationUtil();

	int maxlmt=paginationUtil.MAX_LIMIT;
	int minlmt=paginationUtil.MIN_OFFSET;
	log.info("maxlmt: "+maxlmt);
	Page<NotificationBatch> page = null;
	HttpHeaders headers = null;
	
	List<String> moduleList=new ArrayList<String>();
	if(module.equalsIgnoreCase("ALL")){
		moduleList.add("RECON_APPROVALS");
		moduleList.add("ACCOUNTING_APPROVALS");
	}
	else moduleList.add(module);



	List<NotificationBatch> notificationBatchList=notificationBatchRepository.findByTenantIdAndParentBatchAndModuleIn(tenantId,0l,moduleList);
	response.addIntHeader("X-COUNT", notificationBatchList.size());

	if(limit==null || limit<minlmt){


		notBatchList = notificationBatchRepository.findByTenantIdAndParentBatchAndModuleIn(tenantId,0l,moduleList);
		limit = notBatchList.size();
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

		page = notificationBatchRepository.findByTenantIdAndParentBatchAndModuleIn(tenantId,0l,moduleList,PaginationUtil.generatePageRequest2(offset, limit));


	}
	else{
		log.info("input limit is within maxlimit");

		page = notificationBatchRepository.findByTenantIdAndParentBatchAndModuleIn(tenantId,0l,moduleList,PaginationUtil.generatePageRequest(offset, limit));

	}


	if(notBatchList.size()==0)
	{

		notBatchList=page.getContent();
	}



	for(NotificationBatch notificationBatch: notBatchList)
	{
		LinkedHashMap nBMap=new LinkedHashMap();
		nBMap.put("moduleName", notificationBatch.getModule());
		nBMap.put("instanceId",notificationBatch.getProcessInstanceId());
		nBMap.put("batchName", notificationBatch.getNotificationName());
		nBMap.put("batchId", notificationBatch.getId());
		nBMap.put("createdDate", notificationBatch.getCreatedDate());
		finalMapList.add(nBMap);

	}

	return finalMapList;

}

/**
 * author :ravali
 * @param processId
 * @return
 * @throws NumberFormatException
 * @throws ClassNotFoundException
 * @throws SQLException
 */

@GetMapping("/listTasksByProcessId")
@Timed
public List<LinkedHashMap> getTasksByProcessId(@RequestParam Long processId) throws NumberFormatException, ClassNotFoundException, SQLException
{
	RuntimeEngine engine = manager.getRuntimeEngine(null);
	KieSession ksession = engine.getKieSession();
	TaskService taskService = engine.getTaskService();


	List<Long> taskIdList = taskService.getTasksByProcessInstanceId(processId);
	int TasksSize = taskIdList.size();
	log.info("TasksSize: " + TasksSize);

	List<LinkedHashMap> finalMap=new ArrayList<LinkedHashMap>();
	for(int i=0;i<taskIdList.size();i++) {
		LinkedHashMap taskMap=new LinkedHashMap();
		Long taskId = taskIdList.get(i);
		log.info("taskId: " + taskId);
		Task task = taskService.getTaskById(taskId);
		HashMap userMap=new HashMap();
		if (task != null) {
			log.info("actualOwner :"+task.getTaskData().getActualOwner().getId());
			NotificationBatch notification=notificationBatchRepository.findByCurrentApproverAndProcessInstanceId(Long.valueOf(task.getTaskData().getActualOwner().getId()),processId);
			taskMap.put("taskId", task.getId());
			taskMap.put("batchId", notification.getId());
			taskMap.put("batchName", notification.getNotificationName());
			log.info("task.getTaskData().getCreatedBy() :"+task.getTaskData().getCreatedBy().getId());
			userMap=userJdbcService.jdbcConnc(Long.valueOf(task.getTaskData().getCreatedBy().getId()),notification.getTenantId());
			taskMap.put("taskOwner", userMap.get("assigneeName"));
			userMap=userJdbcService.jdbcConnc(notification.getCurrentApprover(),notification.getTenantId());
			log.info("userMap.get(assigneeName) :"+userMap.get("assigneeName"));
			taskMap.put("currentApprover",userMap.get("assigneeName"));
			log.info("task.getTaskData().getPreviousStatus() :"+task.getTaskData().getPreviousStatus());
			taskMap.put("previousStatus",task.getTaskData().getPreviousStatus());
			taskMap.put("currentStatus", notification.getStatus());
			taskMap.put("actionDate", notification.getLastUpdatedDate());
			finalMap.add(taskMap);
		}
	}
	return finalMap;


}



@GetMapping("/getViewInfoForEachRefNumOfBatchId")
@Timed
public List<LinkedHashMap> getViewInfoForEachRefNumOfBatchId(@RequestParam Long notificationId,@RequestParam(value = "status", required = false) String status,@RequestParam(value = "pageNumber", required = false) Integer  pageNumber,@RequestParam(value = "pageSize", required = false) Integer pageSize,HttpServletResponse response) throws SQLException
{
	log.info("Request Rest to get distinct recon ref on notification id :"+notificationId);
	NotificationBatch notBatch=notificationBatchRepository.findOne(notificationId);
	Integer I = null;
	if(notBatch.getRefLevel()!=null)
	{
		I=notBatch.getRefLevel();
	}
	String refNum=String.valueOf(I);
	if(refNum.length()<=1)
		refNum="0"+refNum;
	log.info("refNum :"+refNum);
	List<LinkedHashMap> finalMap=new ArrayList<LinkedHashMap>();

	String userAndBatchId ="";
	if(status!=null)
		userAndBatchId =notBatch.getCurrentApprover()+"|"+notBatch.getId()+"|"+status;
	else
		userAndBatchId =notBatch.getCurrentApprover()+"|"+notBatch.getId();
	log.info("userAndBatchId :"+userAndBatchId);

	if(pageNumber==null&&pageSize==null)
	{
		pageNumber=0;
		pageSize=25;
	}

	String query="select distinct(reconReference) from ReconciliationResult where apprRef"+refNum+" LIKE '"+userAndBatchId+"%'";


	String queryCount="select distinct(reconReference) from ReconciliationResult where apprRef"+refNum+" LIKE '"+userAndBatchId+"%'";
	Query distReconRefQueryCount=em.createQuery(query);
	List<String> distReconCount=distReconRefQueryCount.getResultList();
	response.addIntHeader("XCOUNT", distReconCount.size());

	//response
	Query distReconRefQuery=em.createQuery(query);

	distReconRefQuery.setFirstResult(((pageNumber+1)-1) * pageSize);
	distReconRefQuery.setMaxResults(pageSize);
	List<String> distReconRef=distReconRefQuery.getResultList();
	log.info("distReconRef :"+distReconRef.size());

	Connection conn = null;
	Statement stmt = null;
	Statement stmtTrg = null;
	ResultSet resultSrcView =null;
	ResultSet resultTrgView =null;


	try
	{
		DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
		conn=ds.getConnection();
		log.info("Connected database successfully...");
		stmt = conn.createStatement();
		stmtTrg =conn.createStatement();
		List<BigInteger> allDvIds=new ArrayList<BigInteger>();

		LinkedHashMap dvInfo=new LinkedHashMap(); 
		List<BigInteger> orginalViewIds=reconciliationResultRepository.findOrginalViewIdByReconReference(distReconRef);
		allDvIds.addAll(orginalViewIds);
		List<LinkedHashMap> srcTrgCombination=new ArrayList<LinkedHashMap>(); 
		for(BigInteger orginalView:orginalViewIds)
		{

			log.info("distReconRef :"+distReconRef.toString());
			String distReconRefJoin =	distReconRef.stream().collect(Collectors.joining("','", "'", "'"));
			String queryToGetTrgReconRef="select distinct(reconReference) from ReconciliationResult where reconReference in ("+distReconRefJoin+") and originalViewId="+orginalView+" and apprRef"+refNum+" LIKE '"+userAndBatchId+"%'" ;
			Query distReconRefQueryCountForTrg=em.createQuery(queryToGetTrgReconRef);

			List<String> distTrgReconRef=distReconRefQueryCountForTrg.getResultList();
			log.info("distTrgReconRef :"+distTrgReconRef.size());
			//log.info("distTrgReconRef1 :"+distTrgReconRef.toString());

			if(distTrgReconRef.size()>0)
			{
				List<BigInteger> trgViewIds=reconciliationResultRepository.findTargetViewIdByReconReference(distTrgReconRef);
				for(int i=0;i<trgViewIds.size();i++)
				{
					LinkedHashMap srcTrgMap=new LinkedHashMap();
					srcTrgMap.put("source", orginalView);
					srcTrgMap.put("target", trgViewIds.get(i));
					srcTrgMap.put("referenceList", distTrgReconRef);
					srcTrgCombination.add(srcTrgMap);
				}
			}



			DataViews dvName=dataViewsRepository.findOne(orginalView.longValue());


			List<BigInteger> orginalSrcIds=reconciliationResultRepository.findOrginalRowIdsByOrginalViewIdAndReconReference(distReconRef, orginalView);
			String orgSrcId=orginalSrcIds.toString().replaceAll("\\[", "").replaceAll("\\]", "");
			String resSrcView="select * from `"+dvName.getDataViewName().toLowerCase() +"` where scrIds in ("+orgSrcId+")";

			resultSrcView=stmt.executeQuery(resSrcView);

			ResultSetMetaData rsmdhdrSrc = resultSrcView.getMetaData();
			int columnCountViewSrc= rsmdhdrSrc.getColumnCount();
			while (resultSrcView.next())
			{
				LinkedHashMap map=new LinkedHashMap();
				for (int j = 1; j <= columnCountViewSrc; j++ ) {
					String viewColumnName = rsmdhdrSrc.getColumnName(j); 
					map.put(viewColumnName, resultSrcView.getString(j));
				}

				dvInfo.put(resultSrcView.getString("scrIds"), map);
			}
		}

		List<BigInteger> targetViewIds=reconciliationResultRepository.findTargetViewIdByReconReference(distReconRef);
		allDvIds.addAll(targetViewIds);
		for(BigInteger targetView:targetViewIds)
		{
			DataViews dvName=dataViewsRepository.findOne(targetView.longValue());
			List<BigInteger> targetSrcIds=reconciliationResultRepository.findTargetRowIdsByOrginalViewIdAndReconReference(distReconRef, targetView);
			String trgSrcId=targetSrcIds.toString().replaceAll("\\[", "").replaceAll("\\]", "");
			String resTrgView="select * from `"+dvName.getDataViewName().toLowerCase() +"` where scrIds in ("+trgSrcId+")";

			resultTrgView=stmt.executeQuery(resTrgView);

			ResultSetMetaData rsmdhdrTrg = resultTrgView.getMetaData();
			int columnCountViewTrg= rsmdhdrTrg.getColumnCount();
			while (resultTrgView.next())
			{
				LinkedHashMap map=new LinkedHashMap();
				for (int j = 1; j <= columnCountViewTrg; j++ ) {
					String viewColumnName = rsmdhdrTrg.getColumnName(j); 
					map.put(viewColumnName, resultTrgView.getString(j));
				}

				dvInfo.put(resultTrgView.getString("scrIds"), map);
			}


		}
		//log.info("dvInfo :"+dvInfo);
		log.info("dvInfo :"+dvInfo.size());

		log.info("srcTrgCombination :"+srcTrgCombination);


		for(int i=0;i<srcTrgCombination.size();i++)
		{


			LinkedHashMap eachSrcTrg=new LinkedHashMap();

			DataViews srcDv=dataViewsRepository.findOne(Long.valueOf((srcTrgCombination.get(i).get("source")).toString()));
			eachSrcTrg.put("sourceName", srcDv.getDataViewDispName());

			DataViews trgDv=dataViewsRepository.findOne(Long.valueOf(srcTrgCombination.get(i).get("target").toString()));
			eachSrcTrg.put("targetName", trgDv.getDataViewDispName());


			List<LinkedHashMap> targetColumnList=new ArrayList<LinkedHashMap>();
			List<LinkedHashMap> sourceColumnList=new ArrayList<LinkedHashMap>();



			LinkedHashMap groupByColumnsSrc=new LinkedHashMap();
			groupByColumnsSrc=dashBoardV4Service.getDVGroupByColumns(srcDv.getId(),true);

			List<LinkedHashMap> lmpSrc=(List<LinkedHashMap>) groupByColumnsSrc.get("columnsList");
			for(int c=0;c<lmpSrc.size();c++)
			{
				String columnName=(lmpSrc.get(c).get("columnAliasName")).toString();
				LinkedHashMap column=new LinkedHashMap();
				column.put("field", lmpSrc.get(c).get("columnName"));
				column.put("header", lmpSrc.get(c).get("columnAliasName"));
				column.put("dataType", lmpSrc.get(c).get("dataType"));
				if(!lmpSrc.get(c).get("dataType").toString().equalsIgnoreCase("Decimal"))
					column.put("align", "left");
				else
					column.put("align", "right");
				column.put("width", "150px");

				sourceColumnList.add(column);

			}


			sourceColumnList.add((LinkedHashMap) groupByColumnsSrc.get("amtQualifier"));
			sourceColumnList.add((LinkedHashMap) groupByColumnsSrc.get("transDateQualifier"));

			eachSrcTrg.put("source", sourceColumnList);


			LinkedHashMap groupByColumnsTrg=new LinkedHashMap();
			groupByColumnsTrg=dashBoardV4Service.getDVGroupByColumns(trgDv.getId(),true);

			List<LinkedHashMap> lmpTrg=(List<LinkedHashMap>) groupByColumnsTrg.get("columnsList");
			for(int c=0;c<lmpTrg.size();c++)
			{
				String columnName=(lmpTrg.get(c).get("columnAliasName")).toString();
				LinkedHashMap column=new LinkedHashMap();
				column.put("field", lmpTrg.get(c).get("columnName"));
				column.put("header", lmpTrg.get(c).get("columnAliasName"));
				column.put("dataType", lmpTrg.get(c).get("dataType"));
				if(!lmpTrg.get(c).get("dataType").toString().equalsIgnoreCase("Decimal"))
					column.put("align", "left");
				else
					column.put("align", "right");
				column.put("width", "150px");

				targetColumnList.add(column);



			}
			targetColumnList.add((LinkedHashMap) groupByColumnsTrg.get("amtQualifier"));

			targetColumnList.add((LinkedHashMap) groupByColumnsTrg.get("transDateQualifier"));



			eachSrcTrg.put("target", targetColumnList);








			List<String> distSrcTrgReconRefList=(List<String>) srcTrgCombination.get(i).get("referenceList");
			//log.info("distSrcTrgReconRefList :"+distSrcTrgReconRefList);
			log.info("distSrcTrgReconRefList size :"+distSrcTrgReconRefList.size());
			List<LinkedHashMap> reconRefernceListMap=new ArrayList<LinkedHashMap>();
			for(String distSrcTrgReconRef:distSrcTrgReconRefList)
			{
				//log.info("distSrcTrgReconRef :"+distSrcTrgReconRef);
				LinkedHashMap reconRefMap=new LinkedHashMap();
				reconRefMap.put("reconReference", distSrcTrgReconRef);

				String srcQue="from ReconciliationResult where originalViewId is not null and originalViewId="+srcTrgCombination.get(i).get("source")+" and reconReference='"+distSrcTrgReconRef+"' and apprRef"+refNum+" LIKE '"+userAndBatchId+"%'";
				List<ReconciliationResult> reconSrcResultList=em.createQuery(srcQue).getResultList();
				//	resultSrc=stmtSrc.executeQuery(srcQue);

				String trgQue="from ReconciliationResult where targetViewId is not null and targetViewId ="+srcTrgCombination.get(i).get("target")+" and reconReference='"+distSrcTrgReconRef+"' and apprRef"+refNum+" LIKE '"+userAndBatchId+"%'";
				List<ReconciliationResult> reconTrgResultList=em.createQuery(trgQue).getResultList();

				List<LinkedHashMap> srcRecList=new ArrayList<LinkedHashMap>();
				List<LinkedHashMap> tarRecList=new ArrayList<LinkedHashMap>();
				for(ReconciliationResult reconSrcResult:reconSrcResultList){

					LinkedHashMap map2=new LinkedHashMap();

					if(reconSrcResult.getOriginalRowId()!=null)
					{
						map2=(LinkedHashMap) dvInfo.get(reconSrcResult.getOriginalRowId().toString());
					}
					//log.info("map2 :"+map2);
					if(reconSrcResult.getId()!=null)
						map2.put("id", reconSrcResult.getId());
					else
						map2.put("id", "");

					if(reconSrcResult.getReconciledDate()!=null)
						map2.put("reconciledDate", reconSrcResult.getReconciledDate());
					else
						map2.put("reconciledDate", "");

					if(reconSrcResult.getOriginalRowId()!=null)
						map2.put("orginalRowId", reconSrcResult.getOriginalRowId());
					else
						map2.put("orginalRowId", "");
					if(reconSrcResult.getReconReference()!=null)
						map2.put("reconReference", reconSrcResult.getReconReference());


					srcRecList.add(map2);
				}


				for(ReconciliationResult reconTrgResult:reconTrgResultList){

					LinkedHashMap map2=new LinkedHashMap();

					if(reconTrgResult.getTargetRowId()!=null)
					{
						map2=(LinkedHashMap) dvInfo.get(reconTrgResult.getTargetRowId().toString());
					}
					if(reconTrgResult.getId()!=null)

						map2.put("id", reconTrgResult.getId());
					else
						map2.put("id", "");

					if(reconTrgResult.getReconciledDate()!=null)

						map2.put("reconciledDate", reconTrgResult.getReconciledDate());
					else
						map2.put("reconciledDate", "");

					if(reconTrgResult.getTargetRowId()!=null)

						map2.put("targetRowId", reconTrgResult.getTargetRowId());
					else
						map2.put("targetRowId", "");

					tarRecList.add(map2);
				}



				//reconRefMap.put("srcColumnsList",srcColumnsList);
				//reconRefMap.put("trgColumnsList",trgColumnsList);
				reconRefMap.put("source", srcRecList);
				reconRefMap.put("target", tarRecList);
				reconRefernceListMap.add(reconRefMap);

			}



			eachSrcTrg.put("reconRef", reconRefernceListMap);
			finalMap.add(eachSrcTrg);


		}
	}
	catch(Exception e)
	{
		log.info("Exception while fetching getViewInfoForEachRefNumOfBatchId :"+e);
	}
	finally
	{
		if(resultSrcView!=null)
			resultSrcView.close();
		if(resultTrgView!=null)
			resultTrgView.close();
		if(stmt!=null)
			stmt.close();
		if(stmtTrg!=null)
			stmtTrg.close();
		if(conn!=null)
			conn.close();
	}
	log.info("*********end time ************"+ZonedDateTime.now());
	return finalMap;

}

}