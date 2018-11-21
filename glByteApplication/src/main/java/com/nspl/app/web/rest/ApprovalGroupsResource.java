package com.nspl.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.config.ApplicationContextProvider;
import com.nspl.app.domain.ApprovalGroupMembers;
import com.nspl.app.domain.ApprovalGroups;
import com.nspl.app.domain.ApprovalRuleAssignment;
import com.nspl.app.domain.LookUpCode;
import com.nspl.app.domain.NotificationBatch;
import com.nspl.app.domain.ReconciliationResult;
import com.nspl.app.domain.RuleGroup;
import com.nspl.app.repository.ApprovalGroupMembersRepository;
import com.nspl.app.repository.ApprovalGroupsRepository;
import com.nspl.app.repository.ApprovalRuleAssignmentRepository;
import com.nspl.app.repository.LookUpCodeRepository;
import com.nspl.app.repository.NotificationBatchRepository;
import com.nspl.app.repository.ReconciliationResultRepository;
import com.nspl.app.service.UserJdbcService;
import com.nspl.app.web.rest.util.HeaderUtil;
import com.nspl.app.web.rest.util.PaginationUtil;

import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;

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

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

/**
 * REST controller for managing ApprovalGroups.
 */
@RestController
@RequestMapping("/api")
public class ApprovalGroupsResource {

    private final Logger log = LoggerFactory.getLogger(ApprovalGroupsResource.class);

    private static final String ENTITY_NAME = "approvalGroups";
        
    private final ApprovalGroupsRepository approvalGroupsRepository;
    
    @Inject
    ApprovalGroupMembersRepository approvalGroupMembersRepository;
    
    @Inject
    UserJdbcService userJdbcService;
    
    @Inject
    NotificationBatchRepository notificationBatchRepository;
    
    @Inject
    ReconciliationResultRepository reconciliationResultRepository;
    
    @Inject
    ApprovalRuleAssignmentRepository approvalRuleAssignmentRepository;
    
    @Autowired
   	Environment env;
    
    @Inject
    LookUpCodeRepository lookUpCodeRepository;

    public ApprovalGroupsResource(ApprovalGroupsRepository approvalGroupsRepository) {
        this.approvalGroupsRepository = approvalGroupsRepository;
    }

    /**
     * POST  /approval-groups : Create a new approvalGroups.
     *
     * @param approvalGroups the approvalGroups to create
     * @return the ResponseEntity with status 201 (Created) and with body the new approvalGroups, or with status 400 (Bad Request) if the approvalGroups has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/approval-groups")
    @Timed
    public ResponseEntity<ApprovalGroups> createApprovalGroups(@RequestBody ApprovalGroups approvalGroups) throws URISyntaxException {
        log.debug("REST request to save ApprovalGroups : {}", approvalGroups);
        if (approvalGroups.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new approvalGroups cannot already have an ID")).body(null);
        }
        ApprovalGroups result = approvalGroupsRepository.save(approvalGroups);
        return ResponseEntity.created(new URI("/api/approval-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /approval-groups : Updates an existing approvalGroups.
     *
     * @param approvalGroups the approvalGroups to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated approvalGroups,
     * or with status 400 (Bad Request) if the approvalGroups is not valid,
     * or with status 500 (Internal Server Error) if the approvalGroups couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/approval-groups")
    @Timed
    public ResponseEntity<ApprovalGroups> updateApprovalGroups(HttpServletRequest request,@RequestBody ApprovalGroups approvalGroups) throws URISyntaxException {
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long userId=Long.parseLong(map.get("userId").toString());
    	
        log.debug("REST request to update ApprovalGroups for user: "+userId+" -> "+ approvalGroups);
        if (approvalGroups.getId() == null) {
            return createApprovalGroups(approvalGroups);
        }
        approvalGroups.setStartDate(approvalGroups.getStartDate().plusDays(1));
        if(approvalGroups.getEndDate()!=null){
        approvalGroups.setEndDate(approvalGroups.getEndDate().plusDays(1));
        }
        approvalGroups.setLastUpdatedBy(userId);
        approvalGroups.setLastUpdatedDate(ZonedDateTime.now());
        ApprovalGroups result = approvalGroupsRepository.save(approvalGroups);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, approvalGroups.getId().toString()))
            .body(result);
    }

    /**
     * GET  /approval-groups : get all the approvalGroups.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of approvalGroups in body
     */
   /* @GetMapping("/approval-groups")
    @Timed
    public ResponseEntity<List<ApprovalGroups>> getAllApprovalGroups(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of ApprovalGroups");
        Page<ApprovalGroups> page = approvalGroupsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/approval-groups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }*/
    
    
    @GetMapping("/approval-groups")
    @Timed
    public ResponseEntity<List<ApprovalGroups>> getAllApprovalGroups(HttpServletRequest request,@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of ApprovalGroups");
        HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
        Page<ApprovalGroups> page = approvalGroupsRepository.findByTenantIdOrderByIdDesc(tenantId,pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/approval-groups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    @GetMapping("/approvalGroupsForTenant")
    @Timed
    public List<ApprovalGroups>  getApprovalGroupsForTenant(HttpServletRequest request) {
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
        log.debug("REST request to get a page of ApprovalGroups for tenant: "+tenantId);
        List<ApprovalGroups> aprovalGroups = approvalGroupsRepository.findByTenantId(tenantId);
        return aprovalGroups;
    }

    /**
     * GET  /approval-groups/:id : get the "id" approvalGroups.
     *
     * @param id the id of the approvalGroups to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the approvalGroups, or with status 404 (Not Found)
     */
    @GetMapping("/approval-groups/{id}")
    @Timed
    public ResponseEntity<ApprovalGroups> getApprovalGroups(@PathVariable Long id) {
        log.debug("REST request to get ApprovalGroups : {}", id);
        ApprovalGroups approvalGroups = approvalGroupsRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(approvalGroups));
    }

    /**
     * DELETE  /approval-groups/:id : delete the "id" approvalGroups.
     *
     * @param id the id of the approvalGroups to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/approval-groups/{id}")
    @Timed
    public ResponseEntity<Void> deleteApprovalGroups(@PathVariable Long id) {
        log.debug("REST request to delete ApprovalGroups : {}", id);
        approvalGroupsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    
    /**
     * author:ravali
     * desc:API to get approval group and group members by approval groupId
     * param id
     */
    @GetMapping("/getApprovalGroupsAndGrpMembers")
    @Timed
    public LinkedHashMap getApprovalGroupsAndGrpMembers(@RequestParam Long id) throws ClassNotFoundException, SQLException
    {
    	log.info("Request Rest to get approval groups and group members");
    	LinkedHashMap finalMap=new LinkedHashMap();
    	 ApprovalGroups approvalgrp= approvalGroupsRepository.findOne(id);
    	 finalMap.put("id", id);
    	 finalMap.put("groupName", approvalgrp.getGroupName());
    	 finalMap.put("tenantId", approvalgrp.getTenantId());
    	 finalMap.put("description", approvalgrp.getDescription());
    	 finalMap.put("startDate", approvalgrp.getStartDate());
    	 finalMap.put("endDate", approvalgrp.getEndDate());
    	 finalMap.put("enabledFlag", approvalgrp.isEnabledFlag());
    	 finalMap.put("createdBy", approvalgrp.getCreatedBy());
    	 finalMap.put("createdDate", approvalgrp.getCreatedDate());
    	 finalMap.put("lastUpdatedBy", approvalgrp.getLastUpdatedBy());
    	 finalMap.put("lastUpdatedDate", approvalgrp.getLastUpdatedDate());
    	 finalMap.put("type", approvalgrp.getType());
    	 
    	 List<LinkedHashMap> lhmList=new ArrayList<LinkedHashMap>();
    	 List<ApprovalGroupMembers> approvalGrpMemList=approvalGroupMembersRepository.findByGroupId(id);
    	 for(ApprovalGroupMembers apprlGrpMem:approvalGrpMemList)
    	 {
    		 LinkedHashMap lmp=new LinkedHashMap();
    		 lmp.put("id", apprlGrpMem.getId());
    		 lmp.put("groupId", apprlGrpMem.getGroupId());
    		 lmp.put("seq", apprlGrpMem.getSeq());
    		 lmp.put("userId",apprlGrpMem.getUserId());
    		 HashMap map= userJdbcService.jdbcConnc(apprlGrpMem.getUserId(),approvalgrp.getTenantId());
    		 lmp.put("userName", map.get("assigneeName"));
    		 lmp.put("startDate", apprlGrpMem.getStartDate());
    		 lmp.put("endDate", apprlGrpMem.getEndDate());
    		 lmp.put("createdBy", apprlGrpMem.getCreatedBy());
    		 lmp.put("createdDate", apprlGrpMem.getCreatedDate());
    		 lhmList.add(lmp);
    	 
    	 }
    			 
    	 finalMap.put("approvalsGroupMemList", lhmList);
		return finalMap;
    	
    }
    /**
     * author :ravali
     * desc :API to post approval group and approval group members
     * params:tenantId,userId
     */
    @PostMapping("/postApprovalGroupsAndGrpMembers")
    @Timed
    public HashMap getApprovalGroupsAndGrpMembers(HttpServletRequest request,@RequestBody HashMap apprMap)
    {
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	Long userId=Long.parseLong(map.get("userId").toString());
    	
    	log.info("Rest Request to post approval group and group members");
    	ApprovalGroups approvalgrp= new ApprovalGroups();
    	HashMap finalMap=new HashMap();

    	if(apprMap.get("id")!=null)
    		approvalgrp=approvalGroupsRepository.findOne(Long.valueOf(apprMap.get("id").toString()));
    	else
    	{
    		if(apprMap.get("groupName")!=null)
    			approvalgrp.setGroupName(apprMap.get("groupName").toString());
    		approvalgrp.setTenantId(tenantId);
    		if(apprMap.get("description")!=null)
    			approvalgrp.setDescription(apprMap.get("description").toString());
    		if(apprMap.get("startDate")!=null)
    		{
    			ZonedDateTime stDate=ZonedDateTime.parse(apprMap.get("startDate").toString());
    			approvalgrp.setStartDate(stDate);
    		}
    		if(apprMap.get("endDate")!=null)
    		{
    			ZonedDateTime edDate=ZonedDateTime.parse(apprMap.get("endDate").toString());
    			approvalgrp.setEndDate(edDate);
    		}
    		if(apprMap.get("type")!=null)
    			approvalgrp.setType(apprMap.get("type").toString());
    		approvalgrp.setCreatedBy(tenantId);
    		approvalgrp.setCreatedDate(ZonedDateTime.now());
    		approvalgrp.setLastUpdatedBy(tenantId);
    		approvalgrp.setLastUpdatedDate(ZonedDateTime.now());
    		approvalgrp.setEnabledFlag(true);
    		approvalGroupsRepository.save(approvalgrp);
    	}
    	if(approvalgrp.getId()!=null)
    	{
    		finalMap.put("id", approvalgrp.getId());
    		finalMap.put("groupName", approvalgrp.getGroupName());
    		List<HashMap> apprGrpsMapList=(List<HashMap>) apprMap.get("approvalsGroupMemList");

    		List<ApprovalGroupMembers> approvalGrpMemList=new ArrayList<ApprovalGroupMembers>();
    		for(int i=0;i<apprGrpsMapList.size();i++)
    		{
    			ApprovalGroupMembers appGrpMem=new ApprovalGroupMembers();

    			if(apprGrpsMapList.get(i).get("id")!=null)
    				appGrpMem.setId(Long.valueOf(apprGrpsMapList.get(i).get("id").toString()));
    			appGrpMem.setGroupId(approvalgrp.getId());
    			appGrpMem.setSeq(Integer.valueOf(apprGrpsMapList.get(i).get("seq").toString()));
    			appGrpMem.setUserId(Long.valueOf(apprGrpsMapList.get(i).get("userId").toString()));
    			if(apprGrpsMapList.get(i).get("startDate")!=null)
    			{
    				ZonedDateTime stDate=ZonedDateTime.parse(apprGrpsMapList.get(i).get("startDate").toString());
    				appGrpMem.setStartDate(stDate);
    			}
    			if(apprGrpsMapList.get(i).get("endDate")!=null)
    			{
    				ZonedDateTime edDate=ZonedDateTime.parse(apprGrpsMapList.get(i).get("endDate").toString());
    				appGrpMem.setEndDate(edDate);
    			}
    			if(apprGrpsMapList.get(i).get("id")==null)
    			{
    				appGrpMem.setCreatedBy(userId);
    				appGrpMem.setCreatedDate(ZonedDateTime.now());
    			}
    			else
    			{
    				appGrpMem.setCreatedBy(Long.valueOf(apprGrpsMapList.get(i).get("createdBy").toString()));
    				ZonedDateTime crtDate=ZonedDateTime.parse(apprGrpsMapList.get(i).get("createdDate").toString());
    				appGrpMem.setCreatedDate(crtDate);
    			}


    			appGrpMem.setLastUpdatedBy(userId);
    			appGrpMem.setLastUpdatedDate(ZonedDateTime.now());
    			approvalGrpMemList.add(appGrpMem);
    		}

    		approvalGroupMembersRepository.save(approvalGrpMemList);
    	}
    	return finalMap;

    }
    
   
    /**
     * author:ravali
     * Approval history for Reconciliation
     * @param notificationId
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    
    @GetMapping("/getApprovalsHistoryDetails")
    @Timed
    public HashMap getApprovalsHistoryDetails(@RequestParam Long notificationId) throws ClassNotFoundException, SQLException
    {
    	log.info("Request Rest to get approval history details with notification id :"+notificationId);
    	NotificationBatch notBtc=notificationBatchRepository.findOne(notificationId);
    	HashMap batchMap=new HashMap();
    	if(notBtc!=null)
    	{

    		batchMap.put("batchId", notBtc.getId());
    		batchMap.put("batchName", notBtc.getNotificationName());
    		batchMap.put("currentApprover",notBtc.getCurrentApprover());
    		batchMap.put("level", notBtc.getRefLevel());
    	/*	String dbUrl=env.getProperty("spring.datasource.url");
    		String[] parts=dbUrl.split("[\\s@&?$+-]+");
    		String host = parts[0].split("/")[2].split(":")[0];
    		log.info("host :"+host);
    		String schemaName=parts[0].split("/")[3];
    		log.info("schemaName :"+schemaName);
    		String userName = env.getProperty("spring.datasource.username");
    		String password = env.getProperty("spring.datasource.password");
    		String jdbcDriver = env.getProperty("spring.datasource.jdbcdriver");*/
    		Connection conn = null;
    		Statement stmt = null;
    		Statement stmtCount = null;
    		DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
    		conn = ds.getConnection();	

    		log.info("Connected database successfully...");
    		stmt = conn.createStatement();
    		stmtCount = conn.createStatement();
    		ResultSet result2=null;
    		ResultSet resultCount=null;
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

    		List<HashMap> appInfoList=new ArrayList<HashMap>();

    		log.info("==>if parent is 0");

    		//HashMap appInfo=new HashMap();
    		//appInfo.put("approvalStatus", notBtc.getStatus());
    		//appInfoList.add(appInfo);



    		String userAndBatchId =notBtc.getCurrentApprover()+"|"+notBtc.getId()+"|";
    		log.info("userAndBatchId :"+userAndBatchId);

    		int refNum1=Integer.parseInt(refNum);
    		refNum1=refNum1-1;
    		String refNum1Str=String.valueOf(refNum1);
    		if(refNum1Str.length()<=1)
    			refNum1Str="0"+refNum1Str;
    		log.info("refNum :"+refNum);
    		String tableName="";
    		if(notBtc.getModule().equalsIgnoreCase("RECON_APPROVALS"))
    			tableName="t_reconciliation_result";
    			else if(notBtc.getModule().equalsIgnoreCase("ACCOUNTING_APPROVALS"))
    				tableName="t_accounting_data";
    			else if(notBtc.getModule().equalsIgnoreCase("JOURNAL_APPROVALS"))
    				tableName="t_journal_approval_info";
    		String query="select * from "+tableName+" where appr_ref_"+refNum+" LIKE '"+userAndBatchId+"%' Limit 1";
    		log.info("query :"+query);
    		result2=stmt.executeQuery(query);
    		int seq=0;
    		List<String> status=new ArrayList<String>();
    		while(result2.next())
    		{
    			String pattern = "yyyy-MM-dd HH:mm:ss";
    			   DateTimeFormatter Parser = DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.systemDefault());
    			   ZonedDateTime znDt;
    					  

    			seq=seq+1;

                 //checking each ref column till 15
    			HashMap appInfo1=new HashMap();
    			if(result2.getString("appr_ref_01")!=null)
    			{
    				String apprRef1Status=result2.getString("appr_ref_01").split("\\|")[2];
    				String apprRef1App=result2.getString("appr_ref_01").split("\\|")[0];
    				String apprRef1AppDate=result2.getString("appr_ref_01").split("\\|")[3];
    				znDt=ZonedDateTime.parse(apprRef1AppDate,Parser);

    				HashMap map=userJdbcService.jdbcConnc(Long.valueOf(apprRef1App),notBtc.getTenantId());
    				appInfo1.put("name", map.get("assigneeName"));
    				appInfo1.put("approvalStatus", apprRef1Status.split("\\(")[0]);
    				appInfo1.put("approvalDate", znDt);
    				appInfo1.put("seq", seq);

    			}
    			HashMap appInfo2=new HashMap();
    			if(result2.getString("appr_ref_02")!=null)
    			{
    				String apprRef2Status=result2.getString("appr_ref_02").split("\\|")[2];
    				String apprRef2App=result2.getString("appr_ref_02").split("\\|")[0];
    				String apprRef2AppDate=result2.getString("appr_ref_02").split("\\|")[3];
    				znDt=ZonedDateTime.parse(apprRef2AppDate,Parser);

    				HashMap map=userJdbcService.jdbcConnc(Long.valueOf(apprRef2App),notBtc.getTenantId());
    				appInfo2.put("name", map.get("assigneeName"));
    				appInfo2.put("approvalStatus", apprRef2Status.split("\\(")[0]);
    				appInfo2.put("approvalDate", znDt);
    				appInfo2.put("seq", seq+1);
    			}


    			HashMap appInfo3=new HashMap();
    			if(result2.getString("appr_ref_03")!=null)
    			{
    				String apprRef3Status=result2.getString("appr_ref_03").split("\\|")[2];
    				String apprRef3App=result2.getString("appr_ref_03").split("\\|")[0];
    				String apprRef3AppDate=result2.getString("appr_ref_03").split("\\|")[3];
    				znDt=ZonedDateTime.parse(apprRef3AppDate,Parser);
    				
    				HashMap map=userJdbcService.jdbcConnc(Long.valueOf(apprRef3App),notBtc.getTenantId());
    				appInfo3.put("name", map.get("assigneeName"));
    				appInfo3.put("approvalStatus", apprRef3Status.split("\\(")[0]);
    				appInfo3.put("approvalDate", znDt);
    				appInfo3.put("seq", seq+2);
    			}
    			HashMap appInfo4=new HashMap();
    			if(result2.getString("appr_ref_04")!=null)
    			{
    				String apprRef4Status=result2.getString("appr_ref_04").split("\\|")[2];
    				String apprRef4App=result2.getString("appr_ref_04").split("\\|")[0];
    				String apprRef4AppDate=result2.getString("appr_ref_04").split("\\|")[3];
    				znDt=ZonedDateTime.parse(apprRef4AppDate,Parser);

    				HashMap map=userJdbcService.jdbcConnc(Long.valueOf(apprRef4App),notBtc.getTenantId());
    				appInfo4.put("name", map.get("assigneeName"));
    				appInfo4.put("approvalStatus", apprRef4Status.split("\\(")[0]);
    				appInfo4.put("approvalDate", znDt);
    				appInfo4.put("seq", seq+3);
    			}

    			HashMap appInfo5=new HashMap();
    			if(result2.getString("appr_ref_05")!=null)
    			{
    				String apprRef5Status=result2.getString("appr_ref_05").split("\\|")[2];
    				String apprRef5App=result2.getString("appr_ref_05").split("\\|")[0];
    				String apprRef5AppDate=result2.getString("appr_ref_05").split("\\|")[3];
    				znDt=ZonedDateTime.parse(apprRef5AppDate,Parser);

    				HashMap map=userJdbcService.jdbcConnc(Long.valueOf(apprRef5App),notBtc.getTenantId());
    				appInfo5.put("name",  map.get("assigneeName"));
    				appInfo5.put("approvalStatus", apprRef5Status.split("\\(")[0]);
    				appInfo5.put("approvalDate", znDt);
    				appInfo5.put("seq", seq+4);

    			}

    			HashMap appInfo6=new HashMap();
    			if(result2.getString("appr_ref_06")!=null)
    			{
    				String apprRef6Status=result2.getString("appr_ref_06").split("\\|")[2];
    				String apprRef6App=result2.getString("appr_ref_06").split("\\|")[0];
    				String apprRef6AppDate=result2.getString("appr_ref_06").split("\\|")[3];
    				znDt=ZonedDateTime.parse(apprRef6AppDate,Parser);

    				HashMap map=userJdbcService.jdbcConnc(Long.valueOf(apprRef6App),notBtc.getTenantId());
    				appInfo6.put("name",  map.get("assigneeName"));
    				appInfo6.put("approvalStatus", apprRef6Status.split("\\(")[0]);
    				appInfo6.put("approvalDate", znDt);
    				appInfo6.put("seq", seq+5);
    			}

    			HashMap appInfo7=new HashMap();
    			if(result2.getString("appr_ref_07")!=null)
    			{
    				String apprRef7Status=result2.getString("appr_ref_07").split("\\|")[2];
    				String apprRef7App=result2.getString("appr_ref_07").split("\\|")[0];
    				String apprRef7AppDate=result2.getString("appr_ref_07").split("\\|")[1];
    				znDt=ZonedDateTime.parse(apprRef7AppDate,Parser);

    				HashMap map=userJdbcService.jdbcConnc(Long.valueOf(apprRef7App),notBtc.getTenantId());
    				appInfo7.put("name",  map.get("assigneeName"));
    				appInfo7.put("approvalStatus", apprRef7Status.split("\\(")[0]);
    				appInfo7.put("approvalDate", znDt);
    				appInfo7.put("seq", seq+6);
    			}

    			HashMap appInfo8=new HashMap();    			
    			if(result2.getString("appr_ref_08")!=null)
    			{
    				String apprRef8Status=result2.getString("appr_ref_08").split("\\|")[2];
    				String apprRef8App=result2.getString("appr_ref_08").split("\\|")[0];
    				String apprRef8AppDate=result2.getString("appr_ref_08").split("\\|")[3];
    				znDt=ZonedDateTime.parse(apprRef8AppDate,Parser);

    				HashMap map=userJdbcService.jdbcConnc(Long.valueOf(apprRef8App),notBtc.getTenantId());
    				appInfo8.put("name",  map.get("assigneeName"));
    				appInfo8.put("approvalStatus", apprRef8Status.split("\\(")[0]);
    				appInfo8.put("approvalDate", znDt);
    				appInfo8.put("seq", seq+7);
    			}

    			HashMap appInfo9=new HashMap();
    			if(result2.getString("appr_ref_09")!=null)
    			{
    				String apprRef9Status=result2.getString("appr_ref_09").split("\\|")[2];
    				String apprRef9App=result2.getString("appr_ref_09").split("\\|")[0];
    				String apprRef9AppDate=result2.getString("appr_ref_09").split("\\|")[3];
    				znDt=ZonedDateTime.parse(apprRef9AppDate,Parser);

    				HashMap map=userJdbcService.jdbcConnc(Long.valueOf(apprRef9App),notBtc.getTenantId());
    				appInfo9.put("name",  map.get("assigneeName"));
    				appInfo9.put("approvalStatus", apprRef9Status.split("\\(")[0]);
    				appInfo9.put("approvalDate", znDt);
    				appInfo9.put("seq", seq+8);
    			}

    			HashMap appInfo10=new HashMap();			
    			if(result2.getString("appr_ref_10")!=null)
    			{
    				String apprRef10Status=result2.getString("appr_ref_10").split("\\|")[2];
    				String apprRef10App=result2.getString("appr_ref_10").split("\\|")[0];
    				String apprRef10AppDate=result2.getString("appr_ref_10").split("\\|")[3];
    				znDt=ZonedDateTime.parse(apprRef10AppDate,Parser);

    				HashMap map=userJdbcService.jdbcConnc(Long.valueOf(apprRef10App),notBtc.getTenantId());
    				appInfo10.put("name",  map.get("assigneeName"));
    				appInfo10.put("approvalStatus", apprRef10Status.split("\\(")[0]);
    				appInfo10.put("approvalDate", znDt);
    				appInfo10.put("seq", seq+9);
    			}
    			HashMap appInfo11=new HashMap();		

    			if(result2.getString("appr_ref_11")!=null)
    			{
    				String apprRef11Status=result2.getString("appr_ref_11").split("\\|")[2];
    				String apprRef11App=result2.getString("appr_ref_11").split("\\|")[0];
    				String apprRef11AppDate=result2.getString("appr_ref_11").split("\\|")[3];
    				znDt=ZonedDateTime.parse(apprRef11AppDate,Parser);

    				HashMap map=userJdbcService.jdbcConnc(Long.valueOf(apprRef11App),notBtc.getTenantId());
    				appInfo11.put("name",  map.get("assigneeName"));
    				appInfo11.put("approvalStatus", apprRef11Status.split("\\(")[0]);
    				appInfo11.put("approvalDate", znDt);
    				appInfo11.put("seq", seq+10);
    			}
    			HashMap appInfo12=new HashMap();		
    			if(result2.getString("appr_ref_12")!=null)
    			{
    				String apprRef12Status=result2.getString("appr_ref_12").split("\\|")[2];
    				String apprRef12App=result2.getString("appr_ref_12").split("\\|")[0];
    				String apprRef12AppDate=result2.getString("appr_ref_12").split("\\|")[3];
    				znDt=ZonedDateTime.parse(apprRef12AppDate,Parser);

    				HashMap map=userJdbcService.jdbcConnc(Long.valueOf(apprRef12App),notBtc.getTenantId());
    				appInfo12.put("name", map.get("assigneeName"));
    				appInfo12.put("approvalStatus", apprRef12Status.split("\\(")[0]);
    				appInfo12.put("approvalDate", znDt);
    				appInfo12.put("seq", seq+11);
    			}
    			HashMap appInfo13=new HashMap();
    			if(result2.getString("appr_ref_13")!=null)
    			{
    				String apprRef13Status=result2.getString("appr_ref_13").split("\\|")[2];
    				String apprRef13App=result2.getString("appr_ref_13").split("\\|")[0];
    				String apprRef13AppDate=result2.getString("appr_ref_13").split("\\|")[3];
    				znDt=ZonedDateTime.parse(apprRef13AppDate,Parser);

    				HashMap map=userJdbcService.jdbcConnc(Long.valueOf(apprRef13App),notBtc.getTenantId());
    				appInfo13.put("name",  map.get("assigneeName"));
    				appInfo13.put("approvalStatus", apprRef13Status.split("\\(")[0]);
    				appInfo13.put("approvalDate", znDt);
    				appInfo13.put("seq", seq+12);
    			}

    			HashMap appInfo14=new HashMap();
    			if(result2.getString("appr_ref_14")!=null)
    			{
    				String apprRef14Status=result2.getString("appr_ref_14").split("\\|")[2];
    				String apprRef14App=result2.getString("appr_ref_14").split("\\|")[0];
    				String apprRef14AppDate=result2.getString("appr_ref_14").split("\\|")[3];
    				znDt=ZonedDateTime.parse(apprRef14AppDate,Parser);

    				HashMap map=userJdbcService.jdbcConnc(Long.valueOf(apprRef14App),notBtc.getTenantId());
    				appInfo14.put("name",  map.get("assigneeName"));
    				appInfo14.put("approvalStatus", apprRef14Status.split("\\(")[0]);
    				appInfo14.put("approvalDate", znDt);
    				appInfo14.put("seq", seq+13);
    			}

    			HashMap appInfo15=new HashMap();
    			if(result2.getString("appr_ref_15")!=null)
    			{
    				String apprRef15Status=result2.getString("appr_ref_15").split("\\|")[2];
    				String apprRef15App=result2.getString("appr_ref_15").split("\\|")[0];
    				String apprRef15AppDate=result2.getString("appr_ref_15").split("\\|")[3];
    				znDt=ZonedDateTime.parse(apprRef15AppDate,Parser);

    				HashMap map=userJdbcService.jdbcConnc(Long.valueOf(apprRef15App),notBtc.getTenantId());
    				appInfo15.put("name",  map.get("assigneeName"));
    				appInfo15.put("approvalStatus", apprRef15Status.split("\\(")[0]);
    				appInfo15.put("approvalDate", znDt);
    				appInfo15.put("seq", seq+14);
    			}
    			List<ApprovalRuleAssignment> appRuleAsnmtList=approvalRuleAssignmentRepository.findByRuleId(notBtc.getRuleId());
    			
    			//based on ref level adding the approval details to final list 
    			if(notBtc.getRefLevel()==1)
    			{

    				appInfoList.add(appInfo1);
    				LookUpCode code=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("APPROVAL_STATUS", notBtc.getStatus(), notBtc.getTenantId());
    				status.add(code.getMeaning());
    				log.info("notBtc.getStatus() :"+notBtc.getStatus());
    				if(notBtc.getStatus().equalsIgnoreCase("approved"))
    				{
    					log.info("in if ");
    					HashMap nxtAppInfo=new HashMap();

    					if(appRuleAsnmtList.size()>notBtc.getApprRef())
    					{
    						log.info("in if appRuleAsnmtList :"+appRuleAsnmtList.size());
    						log.info("in if notBtc :"+notBtc.getApprRef());
    						HashMap map=userJdbcService.jdbcConnc(appRuleAsnmtList.get(notBtc.getApprRef()).getAssigneeId(),notBtc.getTenantId());
    						nxtAppInfo.put("name", map.get("assigneeName"));
    						nxtAppInfo.put("approvalStatus", "InProcess");
    						nxtAppInfo.put("seq", 2);
    						status.add("InProcess");
    						appInfoList.add(nxtAppInfo);

    					}
    				}

    			}
    			if(notBtc.getRefLevel()==2)
    			{


    				appInfoList.add(appInfo1);
    				appInfoList.add(appInfo2);
    				status.add(appInfo2.get("approvalStatus").toString());

    				if(appInfo2.get("approvalStatus").toString().equalsIgnoreCase("approved"))
    				{
    					log.info("condition satistfies");
    					HashMap nxtAppInfo=new HashMap();

    					if(appRuleAsnmtList.size()>notBtc.getApprRef())
    					{
    						log.info("notBtc.getApprRef()+1 :"+notBtc.getApprRef()+1);
    						log.info("appRef1 :"+appRuleAsnmtList.get(notBtc.getApprRef()).getAssigneeId());
    						//log.info("appRef2 :"+appRuleAsnmtList.get(notBtc.getApprRef()+1).getAssigneeId());
    						//log.info("appRef3 :"+appRuleAsnmtList.get(notBtc.getApprRef()-1).getAssigneeId());
    						HashMap map=userJdbcService.jdbcConnc(appRuleAsnmtList.get(notBtc.getApprRef()).getAssigneeId(),notBtc.getTenantId());
    						nxtAppInfo.put("name", map.get("assigneeName"));
    						nxtAppInfo.put("approvalStatus", "InProcess");
    						status.add("InProcess");
    						nxtAppInfo.put("seq", 3);
    						appInfoList.add(nxtAppInfo);
    					}
    				}


    			}
    			if(notBtc.getRefLevel()==3)
    			{

    				appInfoList.add(appInfo1);
    				appInfoList.add(appInfo2);
    				appInfoList.add(appInfo3);
    				status.add(appInfo3.get("approvalStatus").toString());

    				if((appInfo3.get("approvalStatus").toString().startsWith("approved")))
    				{
    					HashMap nxtAppInfo=new HashMap();
    					if(appRuleAsnmtList.size()>notBtc.getApprRef())
    					{
    						HashMap map=userJdbcService.jdbcConnc(appRuleAsnmtList.get(notBtc.getApprRef()+1).getAssigneeId(),notBtc.getTenantId());
    						nxtAppInfo.put("name", map.get("assigneeName"));
    						nxtAppInfo.put("approvalStatus", "InProcess");
    						status.add("InProcess");
    						nxtAppInfo.put("seq", 4);
    						appInfoList.add(nxtAppInfo);
    					}
    				}



    			}
    			if(notBtc.getRefLevel()==4)
    			{

    				appInfoList.add(appInfo1);
    				appInfoList.add(appInfo2);
    				appInfoList.add(appInfo3);
    				appInfoList.add(appInfo4);
    				status.add(appInfo4.get("approvalStatus").toString());

    				if(appInfo4.get("approvalStatus").toString().startsWith("approved"))
    				{
    					HashMap nxtAppInfo=new HashMap();
    					if(appRuleAsnmtList.size()>notBtc.getApprRef())
    					{
    						HashMap map=userJdbcService.jdbcConnc(appRuleAsnmtList.get(notBtc.getApprRef()).getAssigneeId(),notBtc.getTenantId());
    						nxtAppInfo.put("name", map.get("assigneeName"));
    						nxtAppInfo.put("approvalStatus", "InProcess");
    						status.add("InProcess");
    						nxtAppInfo.put("seq", 5);
    						appInfoList.add(appInfo5);
    					}
    				}



    			}
    			if(notBtc.getRefLevel()==5)
    			{
    				appInfoList.add(appInfo1);
    				appInfoList.add(appInfo2);
    				appInfoList.add(appInfo3);
    				appInfoList.add(appInfo4);
    				appInfoList.add(appInfo5);
    				status.add(appInfo5.get("approvalStatus").toString());


    				if(appInfo5.get("approvalStatus").toString().startsWith("approved"))
    				{
    					HashMap nxtAppInfo=new HashMap();

    					if(appRuleAsnmtList.size()>notBtc.getApprRef())
    					{
    						HashMap map=userJdbcService.jdbcConnc(appRuleAsnmtList.get(notBtc.getApprRef()).getAssigneeId(),notBtc.getTenantId());
    						nxtAppInfo.put("name", map.get("assigneeName"));
    						nxtAppInfo.put("approvalStatus", "InProcess");
    						status.add("InProcess");
    						nxtAppInfo.put("seq", 6);
    						appInfoList.add(nxtAppInfo);
    					}
    				}



    			}
    			if(notBtc.getRefLevel()==6)
    			{
    				appInfoList.add(appInfo1);
    				appInfoList.add(appInfo2);
    				appInfoList.add(appInfo3);
    				appInfoList.add(appInfo4);
    				appInfoList.add(appInfo5);
    				appInfoList.add(appInfo6);
    				status.add(appInfo6.get("approvalStatus").toString());



    				if(appInfo6.get("approvalStatus").toString().startsWith("approved"))
    				{
    					HashMap nxtAppInfo=new HashMap();

    					if(appRuleAsnmtList.size()>notBtc.getApprRef())
    					{
    						HashMap map=userJdbcService.jdbcConnc(appRuleAsnmtList.get(notBtc.getApprRef()).getAssigneeId(),notBtc.getTenantId());
    						nxtAppInfo.put("name", map.get("assigneeName"));
    						nxtAppInfo.put("approvalStatus", "InProcess");
    						status.add("InProcess");
    						nxtAppInfo.put("seq", 7);
    						appInfoList.add(nxtAppInfo);
    					}
    				}



    			}
    			if(notBtc.getRefLevel()==7)
    			{
    				appInfoList.add(appInfo1);
    				appInfoList.add(appInfo2);
    				appInfoList.add(appInfo3);
    				appInfoList.add(appInfo4);
    				appInfoList.add(appInfo5);
    				appInfoList.add(appInfo6);
    				appInfoList.add(appInfo7);
    				status.add(appInfo7.get("approvalStatus").toString());

    				if(appInfo7.get("approvalStatus").toString().startsWith("approved"))
    				{
    					HashMap nxtAppInfo=new HashMap();

    					if(appRuleAsnmtList.size()>notBtc.getApprRef())
    					{
    						HashMap map=userJdbcService.jdbcConnc(appRuleAsnmtList.get(notBtc.getApprRef()).getAssigneeId(),notBtc.getTenantId());
    						nxtAppInfo.put("name", map.get("assigneeName"));
    						nxtAppInfo.put("approvalStatus", "InProcess");
    						status.add("InProcess");
    						nxtAppInfo.put("seq", 8);
    						appInfoList.add(nxtAppInfo);
    					}
    				}



    			}

    			if(notBtc.getRefLevel()==8)
    			{
    				appInfoList.add(appInfo1);
    				appInfoList.add(appInfo2);
    				appInfoList.add(appInfo3);
    				appInfoList.add(appInfo4);
    				appInfoList.add(appInfo5);
    				appInfoList.add(appInfo6);
    				appInfoList.add(appInfo7);
    				appInfoList.add(appInfo8);
    				status.add(appInfo8.get("approvalStatus").toString());

    				if(appInfo8.get("approvalStatus").toString().startsWith("approved"))
    				{
    					HashMap nxtAppInfo=new HashMap();
    					if(appRuleAsnmtList.size()>notBtc.getApprRef())
    					{
    						HashMap map=userJdbcService.jdbcConnc(appRuleAsnmtList.get(notBtc.getApprRef()).getAssigneeId(),notBtc.getTenantId());
    						nxtAppInfo.put("name", map.get("assigneeName"));
    						nxtAppInfo.put("approvalStatus", "InProcess");
    						status.add("InProcess");
    						nxtAppInfo.put("seq", 9);
    						appInfoList.add(nxtAppInfo);
    					}
    				}



    			}
    			if(notBtc.getRefLevel()==9)
    			{
    				appInfoList.add(appInfo1);
    				appInfoList.add(appInfo2);
    				appInfoList.add(appInfo3);
    				appInfoList.add(appInfo4);
    				appInfoList.add(appInfo5);
    				appInfoList.add(appInfo6);
    				appInfoList.add(appInfo7);
    				appInfoList.add(appInfo8);
    				appInfoList.add(appInfo9);
    				status.add(appInfo9.get("approvalStatus").toString());

    				if(appInfo9.get("approvalStatus").toString().startsWith("approved"))
    				{
    					HashMap nxtAppInfo=new HashMap();

    					if(appRuleAsnmtList.size()>notBtc.getApprRef())
    					{
    						HashMap map=userJdbcService.jdbcConnc(appRuleAsnmtList.get(notBtc.getApprRef()).getAssigneeId(),notBtc.getTenantId());
    						nxtAppInfo.put("name", map.get("assigneeName"));
    						nxtAppInfo.put("approvalStatus", "InProcess");
    						status.add("InProcess");
    						nxtAppInfo.put("seq", 10);
    						appInfoList.add(nxtAppInfo);
    					}
    				}



    			}

    			if(notBtc.getRefLevel()==10)
    			{
    				appInfoList.add(appInfo1);
    				appInfoList.add(appInfo2);
    				appInfoList.add(appInfo3);
    				appInfoList.add(appInfo4);
    				appInfoList.add(appInfo5);
    				appInfoList.add(appInfo6);
    				appInfoList.add(appInfo7);
    				appInfoList.add(appInfo8);
    				appInfoList.add(appInfo9);
    				appInfoList.add(appInfo10);
    				status.add(appInfo10.get("approvalStatus").toString());

    				if(appInfo10.get("approvalStatus").toString().startsWith("approved"))
    				{
    					HashMap nxtAppInfo=new HashMap();

    					if(appRuleAsnmtList.size()>notBtc.getApprRef())
    					{
    						HashMap map=userJdbcService.jdbcConnc(appRuleAsnmtList.get(notBtc.getApprRef()).getAssigneeId(),notBtc.getTenantId());
    						nxtAppInfo.put("name", map.get("assigneeName"));
    						nxtAppInfo.put("approvalStatus", "InProcess");
    						nxtAppInfo.put("seq", 11);
    						appInfoList.add(nxtAppInfo);
    					}
    				}

    			}
    			if(notBtc.getRefLevel()==11)
    			{
    				appInfoList.add(appInfo1);
    				appInfoList.add(appInfo2);
    				appInfoList.add(appInfo3);
    				appInfoList.add(appInfo4);
    				appInfoList.add(appInfo5);
    				appInfoList.add(appInfo6);
    				appInfoList.add(appInfo7);
    				appInfoList.add(appInfo8);
    				appInfoList.add(appInfo9);
    				appInfoList.add(appInfo10);
    				appInfoList.add(appInfo11);
    				status.add(appInfo11.get("approvalStatus").toString());

    				if(appInfo11.get("approvalStatus").toString().startsWith("approved"))
    				{
    					HashMap nxtAppInfo=new HashMap();

    					if(appRuleAsnmtList.size()>notBtc.getApprRef())
    					{
    						HashMap map=userJdbcService.jdbcConnc(appRuleAsnmtList.get(notBtc.getApprRef()).getAssigneeId(),notBtc.getTenantId());
    						nxtAppInfo.put("name", map.get("assigneeName"));
    						nxtAppInfo.put("approvalStatus", "InProcess");
    						status.add("InProcess");
    						nxtAppInfo.put("seq", 12);
    						appInfoList.add(nxtAppInfo);

    					}
    				}



    			}

    			if(notBtc.getRefLevel()==12)
    			{
    				appInfoList.add(appInfo1);
    				appInfoList.add(appInfo2);
    				appInfoList.add(appInfo3);
    				appInfoList.add(appInfo4);
    				appInfoList.add(appInfo5);
    				appInfoList.add(appInfo6);
    				appInfoList.add(appInfo7);
    				appInfoList.add(appInfo8);
    				appInfoList.add(appInfo9);
    				appInfoList.add(appInfo10);
    				appInfoList.add(appInfo11);
    				appInfoList.add(appInfo12);
    				status.add(appInfo12.get("approvalStatus").toString());


    				if(appInfo12.get("approvalStatus").toString().startsWith("approved"))
    				{
    					HashMap nxtAppInfo=new HashMap();

    					if(appRuleAsnmtList.size()>notBtc.getApprRef())
    					{
    						HashMap map=userJdbcService.jdbcConnc(appRuleAsnmtList.get(notBtc.getApprRef()).getAssigneeId(),notBtc.getTenantId());
    						nxtAppInfo.put("name", map.get("assigneeName"));
    						nxtAppInfo.put("approvalStatus", "InProcess");
    						status.add("InProcess");
    						nxtAppInfo.put("seq", 13);
    						appInfoList.add(nxtAppInfo);
    					}
    				}

    			}
    			if(notBtc.getRefLevel()==13)
    			{
    				appInfoList.add(appInfo1);
    				appInfoList.add(appInfo2);
    				appInfoList.add(appInfo3);
    				appInfoList.add(appInfo4);
    				appInfoList.add(appInfo5);
    				appInfoList.add(appInfo6);
    				appInfoList.add(appInfo7);
    				appInfoList.add(appInfo8);
    				appInfoList.add(appInfo9);
    				appInfoList.add(appInfo10);
    				appInfoList.add(appInfo11);
    				appInfoList.add(appInfo12);
    				appInfoList.add(appInfo13);
    				status.add(appInfo13.get("approvalStatus").toString());

    				if(appInfo13.get("approvalStatus").toString().startsWith("approved"))
    				{
    					HashMap nxtAppInfo=new HashMap();

    					if(appRuleAsnmtList.size()>notBtc.getApprRef())
    					{
    						HashMap map=userJdbcService.jdbcConnc(appRuleAsnmtList.get(notBtc.getApprRef()).getAssigneeId(),notBtc.getTenantId());
    						nxtAppInfo.put("name", map.get("assigneeName"));
    						nxtAppInfo.put("approvalStatus", "InProcess");
    						status.add("InProcess");
    						nxtAppInfo.put("seq", 14);
    						appInfoList.add(nxtAppInfo);
    					}
    				}

    			}
    			if(notBtc.getRefLevel()==14)
    			{
    				appInfoList.add(appInfo1);
    				appInfoList.add(appInfo2);
    				appInfoList.add(appInfo3);
    				appInfoList.add(appInfo4);
    				appInfoList.add(appInfo5);
    				appInfoList.add(appInfo6);
    				appInfoList.add(appInfo7);
    				appInfoList.add(appInfo8);
    				appInfoList.add(appInfo9);
    				appInfoList.add(appInfo10);
    				appInfoList.add(appInfo11);
    				appInfoList.add(appInfo12);
    				appInfoList.add(appInfo13);
    				appInfoList.add(appInfo14);
    				status.add(appInfo14.get("approvalStatus").toString());

    				if(appInfo14.get("approvalStatus").toString().startsWith("approved"))
    				{
    					HashMap nxtAppInfo=new HashMap();

    					if(appRuleAsnmtList.size()>notBtc.getApprRef())
    					{
    						HashMap map=userJdbcService.jdbcConnc(appRuleAsnmtList.get(notBtc.getApprRef()).getAssigneeId(),notBtc.getTenantId());
    						nxtAppInfo.put("name", map.get("assigneeName"));
    						nxtAppInfo.put("approvalStatus", "InProcess");
    						status.add("InProcess");
    						nxtAppInfo.put("seq", 15);
    						appInfoList.add(nxtAppInfo);
    					}
    				}
    			}
    			if(notBtc.getRefLevel()==15)
    			{
    				appInfoList.add(appInfo1);
    				appInfoList.add(appInfo2);
    				appInfoList.add(appInfo3);
    				appInfoList.add(appInfo4);
    				appInfoList.add(appInfo5);
    				appInfoList.add(appInfo6);
    				appInfoList.add(appInfo7);
    				appInfoList.add(appInfo8);
    				appInfoList.add(appInfo9);
    				appInfoList.add(appInfo10);
    				appInfoList.add(appInfo11);
    				appInfoList.add(appInfo12);
    				appInfoList.add(appInfo13);
    				appInfoList.add(appInfo14);
    				appInfoList.add(appInfo15);
    				status.add(appInfo15.get("approvalStatus").toString());

    				if(appInfo15.get("approvalStatus").toString().startsWith("approved"))
    				{
    					HashMap nxtAppInfo=new HashMap();

    					if(appRuleAsnmtList.size()>notBtc.getApprRef())
    					{
    						HashMap map=userJdbcService.jdbcConnc(appRuleAsnmtList.get(notBtc.getApprRef()).getAssigneeId(),notBtc.getTenantId());
    						nxtAppInfo.put("name", map.get("assigneeName"));
    						nxtAppInfo.put("approvalStatus", "InProcess");
    						status.add("InProcess");
    						nxtAppInfo.put("seq", 16);
    						appInfoList.add(nxtAppInfo);
    					}
    				}

    			}


    			if(status.contains("InProcess"))
    				batchMap.put("approvalStatus", "InProcess");
    			else if(status.contains("Rejected"))
    				batchMap.put("approvalStatus", "Rejected");
    			else
    				batchMap.put("approvalStatus", "Approved");



    			batchMap.put("approversList", appInfoList);



    		}
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


    		List<LookUpCode> lookUpCodes=lookUpCodeRepository.findByTenantIdAndLookUpType(notBtc.getTenantId(), "APPROVAL_STATUS");
    		if(lookUpCodes.size()>0)
    		{
    			HashMap detail=new HashMap();
    			Long total=0l;
    			for(int i=0;i<lookUpCodes.size();i++)
    			{
    				String userAndBatchIdlook =notBtc.getCurrentApprover()+"|"+notBtc.getId()+"|"+lookUpCodes.get(i).getMeaning();
    				log.info("userAndBatchId :"+userAndBatchIdlook);
    				
    				if(notBtc.getModule().equalsIgnoreCase("RECON_APPROVALS"))
    	    			tableName="t_reconciliation_result";
    	    			else if(notBtc.getModule().equalsIgnoreCase("ACCOUNTING_APPROVALS"))
    	    				tableName="t_accounting_data";
    				
    				String queryForCount="select count(*) from "+tableName+" where appr_ref_"+refNum+" LIKE '"+userAndBatchIdlook+"%'";
    				log.info("queryForCount :"+queryForCount);
    				resultCount=stmtCount.executeQuery(queryForCount);
    				while(resultCount.next())
    				{
    					if(resultCount.getString("count(*)")!=null)
    						detail.put(lookUpCodes.get(i).getMeaning(), resultCount.getString("count(*)"));
    					total=total+Long.valueOf(resultCount.getString("count(*)"));

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
    		if(stmtCount!=null)
    			stmtCount.close();
    		if(resultCount!=null)
    			resultCount.close();
    	}
    	return batchMap;
    }
    
    
    
    
    
    
    
    /**
     * author:ravali
     * Approval history for Reconciliation
     * @param notificationId
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    
    @GetMapping("/getApprovalsHistoryDetailsForAct")
    @Timed
    public HashMap getApprovalsHistoryDetailsForAct(@RequestParam Long notificationId) throws ClassNotFoundException, SQLException
    {
    	log.info("Request Rest to get approval history details with notification id :"+notificationId);
    	NotificationBatch notBtc=notificationBatchRepository.findOne(notificationId);
    	HashMap batchMap=new HashMap();
    	if(notBtc!=null)
    	{

    		batchMap.put("batchId", notBtc.getId());
    		batchMap.put("batchName", notBtc.getNotificationName());
    		batchMap.put("currentApprover",notBtc.getCurrentApprover());
    		batchMap.put("level", notBtc.getRefLevel());
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
    		Statement stmtCount = null;
    		conn = DriverManager.getConnection(dbUrl, userName, password);
    		log.info("Connected database successfully...");
    		stmt = conn.createStatement();
    		stmtCount = conn.createStatement();
    		ResultSet result2=null;
    		ResultSet resultCount=null;
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

    		List<HashMap> appInfoList=new ArrayList<HashMap>();

    		log.info("==>if parent is 0");

    		//HashMap appInfo=new HashMap();
    		//appInfo.put("approvalStatus", notBtc.getStatus());
    		//appInfoList.add(appInfo);



    		String userAndBatchId =notBtc.getCurrentApprover()+"|"+notBtc.getId()+"|";
    		log.info("userAndBatchId :"+userAndBatchId);

    		int refNum1=Integer.parseInt(refNum);
    		refNum1=refNum1-1;
    		String refNum1Str=String.valueOf(refNum1);
    		if(refNum1Str.length()<=1)
    			refNum1Str="0"+refNum1Str;
    		log.info("refNum :"+refNum);
    		String query="select * from "+schemaName+".t_accounting_data where appr_ref_"+refNum+" LIKE '"+userAndBatchId+"%' Limit 1";
    		log.info("query :"+query);
    		result2=stmt.executeQuery(query);
    		int seq=0;
    		List<String> status=new ArrayList<String>();
    		while(result2.next())
    		{
    			String pattern = "yyyy-MM-dd HH:mm:ss";
    			   DateTimeFormatter Parser = DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.systemDefault());
    			   ZonedDateTime znDt;
    					  

    			seq=seq+1;

                 //checking each ref column till 15
    			HashMap appInfo1=new HashMap();
    			if(result2.getString("appr_ref_01")!=null)
    			{
    				String apprRef1Status=result2.getString("appr_ref_01").split("\\|")[2];
    				String apprRef1App=result2.getString("appr_ref_01").split("\\|")[0];
    				String apprRef1AppDate=result2.getString("appr_ref_01").split("\\|")[3];
    				znDt=ZonedDateTime.parse(apprRef1AppDate,Parser);

    				HashMap map=userJdbcService.jdbcConnc(Long.valueOf(apprRef1App),notBtc.getTenantId());
    				appInfo1.put("name", map.get("assigneeName"));
    				appInfo1.put("approvalStatus", apprRef1Status.split("\\(")[0]);
    				appInfo1.put("approvalDate", znDt);
    				appInfo1.put("seq", seq);

    			}
    			HashMap appInfo2=new HashMap();
    			if(result2.getString("appr_ref_02")!=null)
    			{
    				String apprRef2Status=result2.getString("appr_ref_02").split("\\|")[2];
    				String apprRef2App=result2.getString("appr_ref_02").split("\\|")[0];
    				String apprRef2AppDate=result2.getString("appr_ref_02").split("\\|")[3];
    				znDt=ZonedDateTime.parse(apprRef2AppDate,Parser);

    				HashMap map=userJdbcService.jdbcConnc(Long.valueOf(apprRef2App),notBtc.getTenantId());
    				appInfo2.put("name", map.get("assigneeName"));
    				appInfo2.put("approvalStatus", apprRef2Status.split("\\(")[0]);
    				appInfo2.put("approvalDate", znDt);
    				appInfo2.put("seq", seq+1);
    			}


    			HashMap appInfo3=new HashMap();
    			if(result2.getString("appr_ref_03")!=null)
    			{
    				String apprRef3Status=result2.getString("appr_ref_03").split("\\|")[2];
    				String apprRef3App=result2.getString("appr_ref_03").split("\\|")[0];
    				String apprRef3AppDate=result2.getString("appr_ref_03").split("\\|")[3];
    				znDt=ZonedDateTime.parse(apprRef3AppDate,Parser);
    				
    				HashMap map=userJdbcService.jdbcConnc(Long.valueOf(apprRef3App),notBtc.getTenantId());
    				appInfo3.put("name", map.get("assigneeName"));
    				appInfo3.put("approvalStatus", apprRef3Status.split("\\(")[0]);
    				appInfo3.put("approvalDate", znDt);
    				appInfo3.put("seq", seq+2);
    			}
    			HashMap appInfo4=new HashMap();
    			if(result2.getString("appr_ref_04")!=null)
    			{
    				String apprRef4Status=result2.getString("appr_ref_04").split("\\|")[2];
    				String apprRef4App=result2.getString("appr_ref_04").split("\\|")[0];
    				String apprRef4AppDate=result2.getString("appr_ref_04").split("\\|")[3];
    				znDt=ZonedDateTime.parse(apprRef4AppDate,Parser);

    				HashMap map=userJdbcService.jdbcConnc(Long.valueOf(apprRef4App),notBtc.getTenantId());
    				appInfo4.put("name", map.get("assigneeName"));
    				appInfo4.put("approvalStatus", apprRef4Status.split("\\(")[0]);
    				appInfo4.put("approvalDate", znDt);
    				appInfo4.put("seq", seq+3);
    			}

    			HashMap appInfo5=new HashMap();
    			if(result2.getString("appr_ref_05")!=null)
    			{
    				String apprRef5Status=result2.getString("appr_ref_05").split("\\|")[2];
    				String apprRef5App=result2.getString("appr_ref_05").split("\\|")[0];
    				String apprRef5AppDate=result2.getString("appr_ref_05").split("\\|")[3];
    				znDt=ZonedDateTime.parse(apprRef5AppDate,Parser);

    				HashMap map=userJdbcService.jdbcConnc(Long.valueOf(apprRef5App),notBtc.getTenantId());
    				appInfo5.put("name",  map.get("assigneeName"));
    				appInfo5.put("approvalStatus", apprRef5Status.split("\\(")[0]);
    				appInfo5.put("approvalDate", znDt);
    				appInfo5.put("seq", seq+4);

    			}

    			HashMap appInfo6=new HashMap();
    			if(result2.getString("appr_ref_06")!=null)
    			{
    				String apprRef6Status=result2.getString("appr_ref_06").split("\\|")[2];
    				String apprRef6App=result2.getString("appr_ref_06").split("\\|")[0];
    				String apprRef6AppDate=result2.getString("appr_ref_06").split("\\|")[3];
    				znDt=ZonedDateTime.parse(apprRef6AppDate,Parser);

    				HashMap map=userJdbcService.jdbcConnc(Long.valueOf(apprRef6App),notBtc.getTenantId());
    				appInfo6.put("name",  map.get("assigneeName"));
    				appInfo6.put("approvalStatus", apprRef6Status.split("\\(")[0]);
    				appInfo6.put("approvalDate", znDt);
    				appInfo6.put("seq", seq+5);
    			}

    			HashMap appInfo7=new HashMap();
    			if(result2.getString("appr_ref_07")!=null)
    			{
    				String apprRef7Status=result2.getString("appr_ref_07").split("\\|")[2];
    				String apprRef7App=result2.getString("appr_ref_07").split("\\|")[0];
    				String apprRef7AppDate=result2.getString("appr_ref_07").split("\\|")[1];
    				znDt=ZonedDateTime.parse(apprRef7AppDate,Parser);

    				HashMap map=userJdbcService.jdbcConnc(Long.valueOf(apprRef7App),notBtc.getTenantId());
    				appInfo7.put("name",  map.get("assigneeName"));
    				appInfo7.put("approvalStatus", apprRef7Status.split("\\(")[0]);
    				appInfo7.put("approvalDate", znDt);
    				appInfo7.put("seq", seq+6);
    			}

    			HashMap appInfo8=new HashMap();    			
    			if(result2.getString("appr_ref_08")!=null)
    			{
    				String apprRef8Status=result2.getString("appr_ref_08").split("\\|")[2];
    				String apprRef8App=result2.getString("appr_ref_08").split("\\|")[0];
    				String apprRef8AppDate=result2.getString("appr_ref_08").split("\\|")[3];
    				znDt=ZonedDateTime.parse(apprRef8AppDate,Parser);

    				HashMap map=userJdbcService.jdbcConnc(Long.valueOf(apprRef8App),notBtc.getTenantId());
    				appInfo8.put("name",  map.get("assigneeName"));
    				appInfo8.put("approvalStatus", apprRef8Status.split("\\(")[0]);
    				appInfo8.put("approvalDate", znDt);
    				appInfo8.put("seq", seq+7);
    			}

    			HashMap appInfo9=new HashMap();
    			if(result2.getString("appr_ref_09")!=null)
    			{
    				String apprRef9Status=result2.getString("appr_ref_09").split("\\|")[2];
    				String apprRef9App=result2.getString("appr_ref_09").split("\\|")[0];
    				String apprRef9AppDate=result2.getString("appr_ref_09").split("\\|")[3];
    				znDt=ZonedDateTime.parse(apprRef9AppDate,Parser);

    				HashMap map=userJdbcService.jdbcConnc(Long.valueOf(apprRef9App),notBtc.getTenantId());
    				appInfo9.put("name",  map.get("assigneeName"));
    				appInfo9.put("approvalStatus", apprRef9Status.split("\\(")[0]);
    				appInfo9.put("approvalDate", znDt);
    				appInfo9.put("seq", seq+8);
    			}

    			HashMap appInfo10=new HashMap();			
    			if(result2.getString("appr_ref_10")!=null)
    			{
    				String apprRef10Status=result2.getString("appr_ref_10").split("\\|")[2];
    				String apprRef10App=result2.getString("appr_ref_10").split("\\|")[0];
    				String apprRef10AppDate=result2.getString("appr_ref_10").split("\\|")[3];
    				znDt=ZonedDateTime.parse(apprRef10AppDate,Parser);

    				HashMap map=userJdbcService.jdbcConnc(Long.valueOf(apprRef10App),notBtc.getTenantId());
    				appInfo10.put("name",  map.get("assigneeName"));
    				appInfo10.put("approvalStatus", apprRef10Status.split("\\(")[0]);
    				appInfo10.put("approvalDate", znDt);
    				appInfo10.put("seq", seq+9);
    			}
    			HashMap appInfo11=new HashMap();		

    			if(result2.getString("appr_ref_11")!=null)
    			{
    				String apprRef11Status=result2.getString("appr_ref_11").split("\\|")[2];
    				String apprRef11App=result2.getString("appr_ref_11").split("\\|")[0];
    				String apprRef11AppDate=result2.getString("appr_ref_11").split("\\|")[3];
    				znDt=ZonedDateTime.parse(apprRef11AppDate,Parser);

    				HashMap map=userJdbcService.jdbcConnc(Long.valueOf(apprRef11App),notBtc.getTenantId());
    				appInfo11.put("name",  map.get("assigneeName"));
    				appInfo11.put("approvalStatus", apprRef11Status.split("\\(")[0]);
    				appInfo11.put("approvalDate", znDt);
    				appInfo11.put("seq", seq+10);
    			}
    			HashMap appInfo12=new HashMap();		
    			if(result2.getString("appr_ref_12")!=null)
    			{
    				String apprRef12Status=result2.getString("appr_ref_12").split("\\|")[2];
    				String apprRef12App=result2.getString("appr_ref_12").split("\\|")[0];
    				String apprRef12AppDate=result2.getString("appr_ref_12").split("\\|")[3];
    				znDt=ZonedDateTime.parse(apprRef12AppDate,Parser);

    				HashMap map=userJdbcService.jdbcConnc(Long.valueOf(apprRef12App),notBtc.getTenantId());
    				appInfo12.put("name", map.get("assigneeName"));
    				appInfo12.put("approvalStatus", apprRef12Status.split("\\(")[0]);
    				appInfo12.put("approvalDate", znDt);
    				appInfo12.put("seq", seq+11);
    			}
    			HashMap appInfo13=new HashMap();
    			if(result2.getString("appr_ref_13")!=null)
    			{
    				String apprRef13Status=result2.getString("appr_ref_13").split("\\|")[2];
    				String apprRef13App=result2.getString("appr_ref_13").split("\\|")[0];
    				String apprRef13AppDate=result2.getString("appr_ref_13").split("\\|")[3];
    				znDt=ZonedDateTime.parse(apprRef13AppDate,Parser);

    				HashMap map=userJdbcService.jdbcConnc(Long.valueOf(apprRef13App),notBtc.getTenantId());
    				appInfo13.put("name",  map.get("assigneeName"));
    				appInfo13.put("approvalStatus", apprRef13Status.split("\\(")[0]);
    				appInfo13.put("approvalDate", znDt);
    				appInfo13.put("seq", seq+12);
    			}

    			HashMap appInfo14=new HashMap();
    			if(result2.getString("appr_ref_14")!=null)
    			{
    				String apprRef14Status=result2.getString("appr_ref_14").split("\\|")[2];
    				String apprRef14App=result2.getString("appr_ref_14").split("\\|")[0];
    				String apprRef14AppDate=result2.getString("appr_ref_14").split("\\|")[3];
    				znDt=ZonedDateTime.parse(apprRef14AppDate,Parser);

    				HashMap map=userJdbcService.jdbcConnc(Long.valueOf(apprRef14App),notBtc.getTenantId());
    				appInfo14.put("name",  map.get("assigneeName"));
    				appInfo14.put("approvalStatus", apprRef14Status.split("\\(")[0]);
    				appInfo14.put("approvalDate", znDt);
    				appInfo14.put("seq", seq+13);
    			}

    			HashMap appInfo15=new HashMap();
    			if(result2.getString("appr_ref_15")!=null)
    			{
    				String apprRef15Status=result2.getString("appr_ref_15").split("\\|")[2];
    				String apprRef15App=result2.getString("appr_ref_15").split("\\|")[0];
    				String apprRef15AppDate=result2.getString("appr_ref_15").split("\\|")[3];
    				znDt=ZonedDateTime.parse(apprRef15AppDate,Parser);

    				HashMap map=userJdbcService.jdbcConnc(Long.valueOf(apprRef15App),notBtc.getTenantId());
    				appInfo15.put("name",  map.get("assigneeName"));
    				appInfo15.put("approvalStatus", apprRef15Status.split("\\(")[0]);
    				appInfo15.put("approvalDate", znDt);
    				appInfo15.put("seq", seq+14);
    			}
    			List<ApprovalRuleAssignment> appRuleAsnmtList=approvalRuleAssignmentRepository.findByRuleId(notBtc.getRuleId());
    			
    			//based on ref level adding the approval details to final list 
    			if(notBtc.getRefLevel()==1)
    			{

    				appInfoList.add(appInfo1);
    				LookUpCode code=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("APPROVAL_STATUS", notBtc.getStatus(), notBtc.getTenantId());
    				status.add(code.getMeaning());
    				log.info("notBtc.getStatus()1 :"+notBtc.getStatus());
    				if(notBtc.getStatus().equalsIgnoreCase("approved"))
    				{
    					log.info("in if ");
    					HashMap nxtAppInfo=new HashMap();

    					if(appRuleAsnmtList.size()>notBtc.getApprRef())
    					{
    						log.info("in if appRuleAsnmtList :"+appRuleAsnmtList.size());
    						log.info("in if notBtc :"+notBtc.getApprRef());
    						HashMap map=userJdbcService.jdbcConnc(appRuleAsnmtList.get(notBtc.getApprRef()).getAssigneeId(),notBtc.getTenantId());
    						nxtAppInfo.put("name", map.get("assigneeName"));
    						nxtAppInfo.put("approvalStatus", "InProcess");
    						nxtAppInfo.put("seq", 2);
    						status.add("InProcess");
    						appInfoList.add(nxtAppInfo);

    					}
    				}
    				else
    				{
    					log.info("in else with no approved status");
    				}
    					

    			}
    			if(notBtc.getRefLevel()==2)
    			{


    				appInfoList.add(appInfo1);
    				appInfoList.add(appInfo2);
    				status.add(appInfo2.get("approvalStatus").toString());

    				if(appInfo2.get("approvalStatus").toString().equalsIgnoreCase("approved"))
    				{
    					log.info("condition satistfies");
    					HashMap nxtAppInfo=new HashMap();

    					if(appRuleAsnmtList.size()>notBtc.getApprRef())
    					{
    						log.info("notBtc.getApprRef()+1 :"+notBtc.getApprRef()+1);
    						log.info("appRef1 :"+appRuleAsnmtList.get(notBtc.getApprRef()).getAssigneeId());
    						log.info("appRef2 :"+appRuleAsnmtList.get(notBtc.getApprRef()+1).getAssigneeId());
    						log.info("appRef3 :"+appRuleAsnmtList.get(notBtc.getApprRef()-1).getAssigneeId());
    						HashMap map=userJdbcService.jdbcConnc(appRuleAsnmtList.get(notBtc.getApprRef()).getAssigneeId(),notBtc.getTenantId());
    						nxtAppInfo.put("name", map.get("assigneeName"));
    						nxtAppInfo.put("approvalStatus", "InProcess");
    						status.add("InProcess");
    						nxtAppInfo.put("seq", 3);
    						appInfoList.add(nxtAppInfo);
    					}
    				}


    			}
    			if(notBtc.getRefLevel()==3)
    			{

    				appInfoList.add(appInfo1);
    				appInfoList.add(appInfo2);
    				appInfoList.add(appInfo3);
    				status.add(appInfo3.get("approvalStatus").toString());

    				if((appInfo3.get("approvalStatus").toString().startsWith("approved")))
    				{
    					HashMap nxtAppInfo=new HashMap();
    					if(appRuleAsnmtList.size()>notBtc.getApprRef())
    					{
    						HashMap map=userJdbcService.jdbcConnc(appRuleAsnmtList.get(notBtc.getApprRef()+1).getAssigneeId(),notBtc.getTenantId());
    						nxtAppInfo.put("name", map.get("assigneeName"));
    						nxtAppInfo.put("approvalStatus", "InProcess");
    						status.add("InProcess");
    						nxtAppInfo.put("seq", 4);
    						appInfoList.add(nxtAppInfo);
    					}
    				}



    			}
    			if(notBtc.getRefLevel()==4)
    			{

    				appInfoList.add(appInfo1);
    				appInfoList.add(appInfo2);
    				appInfoList.add(appInfo3);
    				appInfoList.add(appInfo4);
    				status.add(appInfo4.get("approvalStatus").toString());

    				if(appInfo4.get("approvalStatus").toString().startsWith("approved"))
    				{
    					HashMap nxtAppInfo=new HashMap();
    					if(appRuleAsnmtList.size()>notBtc.getApprRef())
    					{
    						HashMap map=userJdbcService.jdbcConnc(appRuleAsnmtList.get(notBtc.getApprRef()).getAssigneeId(),notBtc.getTenantId());
    						nxtAppInfo.put("name", map.get("assigneeName"));
    						nxtAppInfo.put("approvalStatus", "InProcess");
    						status.add("InProcess");
    						nxtAppInfo.put("seq", 5);
    						appInfoList.add(appInfo5);
    					}
    				}



    			}
    			if(notBtc.getRefLevel()==5)
    			{
    				appInfoList.add(appInfo1);
    				appInfoList.add(appInfo2);
    				appInfoList.add(appInfo3);
    				appInfoList.add(appInfo4);
    				appInfoList.add(appInfo5);
    				status.add(appInfo5.get("approvalStatus").toString());


    				if(appInfo5.get("approvalStatus").toString().startsWith("approved"))
    				{
    					HashMap nxtAppInfo=new HashMap();

    					if(appRuleAsnmtList.size()>notBtc.getApprRef())
    					{
    						HashMap map=userJdbcService.jdbcConnc(appRuleAsnmtList.get(notBtc.getApprRef()).getAssigneeId(),notBtc.getTenantId());
    						nxtAppInfo.put("name", map.get("assigneeName"));
    						nxtAppInfo.put("approvalStatus", "InProcess");
    						status.add("InProcess");
    						nxtAppInfo.put("seq", 6);
    						appInfoList.add(nxtAppInfo);
    					}
    				}



    			}
    			if(notBtc.getRefLevel()==6)
    			{
    				appInfoList.add(appInfo1);
    				appInfoList.add(appInfo2);
    				appInfoList.add(appInfo3);
    				appInfoList.add(appInfo4);
    				appInfoList.add(appInfo5);
    				appInfoList.add(appInfo6);
    				status.add(appInfo6.get("approvalStatus").toString());



    				if(appInfo6.get("approvalStatus").toString().startsWith("approved"))
    				{
    					HashMap nxtAppInfo=new HashMap();

    					if(appRuleAsnmtList.size()>notBtc.getApprRef())
    					{
    						HashMap map=userJdbcService.jdbcConnc(appRuleAsnmtList.get(notBtc.getApprRef()).getAssigneeId(),notBtc.getTenantId());
    						nxtAppInfo.put("name", map.get("assigneeName"));
    						nxtAppInfo.put("approvalStatus", "InProcess");
    						status.add("InProcess");
    						nxtAppInfo.put("seq", 7);
    						appInfoList.add(nxtAppInfo);
    					}
    				}



    			}
    			if(notBtc.getRefLevel()==7)
    			{
    				appInfoList.add(appInfo1);
    				appInfoList.add(appInfo2);
    				appInfoList.add(appInfo3);
    				appInfoList.add(appInfo4);
    				appInfoList.add(appInfo5);
    				appInfoList.add(appInfo6);
    				appInfoList.add(appInfo7);
    				status.add(appInfo7.get("approvalStatus").toString());

    				if(appInfo7.get("approvalStatus").toString().startsWith("approved"))
    				{
    					HashMap nxtAppInfo=new HashMap();

    					if(appRuleAsnmtList.size()>notBtc.getApprRef())
    					{
    						HashMap map=userJdbcService.jdbcConnc(appRuleAsnmtList.get(notBtc.getApprRef()).getAssigneeId(),notBtc.getTenantId());
    						nxtAppInfo.put("name", map.get("assigneeName"));
    						nxtAppInfo.put("approvalStatus", "InProcess");
    						status.add("InProcess");
    						nxtAppInfo.put("seq", 8);
    						appInfoList.add(nxtAppInfo);
    					}
    				}



    			}

    			if(notBtc.getRefLevel()==8)
    			{
    				appInfoList.add(appInfo1);
    				appInfoList.add(appInfo2);
    				appInfoList.add(appInfo3);
    				appInfoList.add(appInfo4);
    				appInfoList.add(appInfo5);
    				appInfoList.add(appInfo6);
    				appInfoList.add(appInfo7);
    				appInfoList.add(appInfo8);
    				status.add(appInfo8.get("approvalStatus").toString());

    				if(appInfo8.get("approvalStatus").toString().startsWith("approved"))
    				{
    					HashMap nxtAppInfo=new HashMap();
    					if(appRuleAsnmtList.size()>notBtc.getApprRef())
    					{
    						HashMap map=userJdbcService.jdbcConnc(appRuleAsnmtList.get(notBtc.getApprRef()).getAssigneeId(),notBtc.getTenantId());
    						nxtAppInfo.put("name", map.get("assigneeName"));
    						nxtAppInfo.put("approvalStatus", "InProcess");
    						status.add("InProcess");
    						nxtAppInfo.put("seq", 9);
    						appInfoList.add(nxtAppInfo);
    					}
    				}



    			}
    			if(notBtc.getRefLevel()==9)
    			{
    				appInfoList.add(appInfo1);
    				appInfoList.add(appInfo2);
    				appInfoList.add(appInfo3);
    				appInfoList.add(appInfo4);
    				appInfoList.add(appInfo5);
    				appInfoList.add(appInfo6);
    				appInfoList.add(appInfo7);
    				appInfoList.add(appInfo8);
    				appInfoList.add(appInfo9);
    				status.add(appInfo9.get("approvalStatus").toString());

    				if(appInfo9.get("approvalStatus").toString().startsWith("approved"))
    				{
    					HashMap nxtAppInfo=new HashMap();

    					if(appRuleAsnmtList.size()>notBtc.getApprRef())
    					{
    						HashMap map=userJdbcService.jdbcConnc(appRuleAsnmtList.get(notBtc.getApprRef()).getAssigneeId(),notBtc.getTenantId());
    						nxtAppInfo.put("name", map.get("assigneeName"));
    						nxtAppInfo.put("approvalStatus", "InProcess");
    						status.add("InProcess");
    						nxtAppInfo.put("seq", 10);
    						appInfoList.add(nxtAppInfo);
    					}
    				}



    			}

    			if(notBtc.getRefLevel()==10)
    			{
    				appInfoList.add(appInfo1);
    				appInfoList.add(appInfo2);
    				appInfoList.add(appInfo3);
    				appInfoList.add(appInfo4);
    				appInfoList.add(appInfo5);
    				appInfoList.add(appInfo6);
    				appInfoList.add(appInfo7);
    				appInfoList.add(appInfo8);
    				appInfoList.add(appInfo9);
    				appInfoList.add(appInfo10);
    				status.add(appInfo10.get("approvalStatus").toString());

    				if(appInfo10.get("approvalStatus").toString().startsWith("approved"))
    				{
    					HashMap nxtAppInfo=new HashMap();

    					if(appRuleAsnmtList.size()>notBtc.getApprRef())
    					{
    						HashMap map=userJdbcService.jdbcConnc(appRuleAsnmtList.get(notBtc.getApprRef()).getAssigneeId(),notBtc.getTenantId());
    						nxtAppInfo.put("name", map.get("assigneeName"));
    						nxtAppInfo.put("approvalStatus", "InProcess");
    						nxtAppInfo.put("seq", 11);
    						appInfoList.add(nxtAppInfo);
    					}
    				}

    			}
    			if(notBtc.getRefLevel()==11)
    			{
    				appInfoList.add(appInfo1);
    				appInfoList.add(appInfo2);
    				appInfoList.add(appInfo3);
    				appInfoList.add(appInfo4);
    				appInfoList.add(appInfo5);
    				appInfoList.add(appInfo6);
    				appInfoList.add(appInfo7);
    				appInfoList.add(appInfo8);
    				appInfoList.add(appInfo9);
    				appInfoList.add(appInfo10);
    				appInfoList.add(appInfo11);
    				status.add(appInfo11.get("approvalStatus").toString());

    				if(appInfo11.get("approvalStatus").toString().startsWith("approved"))
    				{
    					HashMap nxtAppInfo=new HashMap();

    					if(appRuleAsnmtList.size()>notBtc.getApprRef())
    					{
    						HashMap map=userJdbcService.jdbcConnc(appRuleAsnmtList.get(notBtc.getApprRef()).getAssigneeId(),notBtc.getTenantId());
    						nxtAppInfo.put("name", map.get("assigneeName"));
    						nxtAppInfo.put("approvalStatus", "InProcess");
    						status.add("InProcess");
    						nxtAppInfo.put("seq", 12);
    						appInfoList.add(nxtAppInfo);

    					}
    				}



    			}

    			if(notBtc.getRefLevel()==12)
    			{
    				appInfoList.add(appInfo1);
    				appInfoList.add(appInfo2);
    				appInfoList.add(appInfo3);
    				appInfoList.add(appInfo4);
    				appInfoList.add(appInfo5);
    				appInfoList.add(appInfo6);
    				appInfoList.add(appInfo7);
    				appInfoList.add(appInfo8);
    				appInfoList.add(appInfo9);
    				appInfoList.add(appInfo10);
    				appInfoList.add(appInfo11);
    				appInfoList.add(appInfo12);
    				status.add(appInfo12.get("approvalStatus").toString());


    				if(appInfo12.get("approvalStatus").toString().startsWith("approved"))
    				{
    					HashMap nxtAppInfo=new HashMap();

    					if(appRuleAsnmtList.size()>notBtc.getApprRef())
    					{
    						HashMap map=userJdbcService.jdbcConnc(appRuleAsnmtList.get(notBtc.getApprRef()).getAssigneeId(),notBtc.getTenantId());
    						nxtAppInfo.put("name", map.get("assigneeName"));
    						nxtAppInfo.put("approvalStatus", "InProcess");
    						status.add("InProcess");
    						nxtAppInfo.put("seq", 13);
    						appInfoList.add(nxtAppInfo);
    					}
    				}

    			}
    			if(notBtc.getRefLevel()==13)
    			{
    				appInfoList.add(appInfo1);
    				appInfoList.add(appInfo2);
    				appInfoList.add(appInfo3);
    				appInfoList.add(appInfo4);
    				appInfoList.add(appInfo5);
    				appInfoList.add(appInfo6);
    				appInfoList.add(appInfo7);
    				appInfoList.add(appInfo8);
    				appInfoList.add(appInfo9);
    				appInfoList.add(appInfo10);
    				appInfoList.add(appInfo11);
    				appInfoList.add(appInfo12);
    				appInfoList.add(appInfo13);
    				status.add(appInfo13.get("approvalStatus").toString());

    				if(appInfo13.get("approvalStatus").toString().startsWith("approved"))
    				{
    					HashMap nxtAppInfo=new HashMap();

    					if(appRuleAsnmtList.size()>notBtc.getApprRef())
    					{
    						HashMap map=userJdbcService.jdbcConnc(appRuleAsnmtList.get(notBtc.getApprRef()).getAssigneeId(),notBtc.getTenantId());
    						nxtAppInfo.put("name", map.get("assigneeName"));
    						nxtAppInfo.put("approvalStatus", "InProcess");
    						status.add("InProcess");
    						nxtAppInfo.put("seq", 14);
    						appInfoList.add(nxtAppInfo);
    					}
    				}

    			}
    			if(notBtc.getRefLevel()==14)
    			{
    				appInfoList.add(appInfo1);
    				appInfoList.add(appInfo2);
    				appInfoList.add(appInfo3);
    				appInfoList.add(appInfo4);
    				appInfoList.add(appInfo5);
    				appInfoList.add(appInfo6);
    				appInfoList.add(appInfo7);
    				appInfoList.add(appInfo8);
    				appInfoList.add(appInfo9);
    				appInfoList.add(appInfo10);
    				appInfoList.add(appInfo11);
    				appInfoList.add(appInfo12);
    				appInfoList.add(appInfo13);
    				appInfoList.add(appInfo14);
    				status.add(appInfo14.get("approvalStatus").toString());

    				if(appInfo14.get("approvalStatus").toString().startsWith("approved"))
    				{
    					HashMap nxtAppInfo=new HashMap();

    					if(appRuleAsnmtList.size()>notBtc.getApprRef())
    					{
    						HashMap map=userJdbcService.jdbcConnc(appRuleAsnmtList.get(notBtc.getApprRef()).getAssigneeId(),notBtc.getTenantId());
    						nxtAppInfo.put("name", map.get("assigneeName"));
    						nxtAppInfo.put("approvalStatus", "InProcess");
    						status.add("InProcess");
    						nxtAppInfo.put("seq", 15);
    						appInfoList.add(nxtAppInfo);
    					}
    				}
    			}
    			if(notBtc.getRefLevel()==15)
    			{
    				appInfoList.add(appInfo1);
    				appInfoList.add(appInfo2);
    				appInfoList.add(appInfo3);
    				appInfoList.add(appInfo4);
    				appInfoList.add(appInfo5);
    				appInfoList.add(appInfo6);
    				appInfoList.add(appInfo7);
    				appInfoList.add(appInfo8);
    				appInfoList.add(appInfo9);
    				appInfoList.add(appInfo10);
    				appInfoList.add(appInfo11);
    				appInfoList.add(appInfo12);
    				appInfoList.add(appInfo13);
    				appInfoList.add(appInfo14);
    				appInfoList.add(appInfo15);
    				status.add(appInfo15.get("approvalStatus").toString());

    				if(appInfo15.get("approvalStatus").toString().startsWith("approved"))
    				{
    					HashMap nxtAppInfo=new HashMap();

    					if(appRuleAsnmtList.size()>notBtc.getApprRef())
    					{
    						HashMap map=userJdbcService.jdbcConnc(appRuleAsnmtList.get(notBtc.getApprRef()).getAssigneeId(),notBtc.getTenantId());
    						nxtAppInfo.put("name", map.get("assigneeName"));
    						nxtAppInfo.put("approvalStatus", "InProcess");
    						status.add("InProcess");
    						nxtAppInfo.put("seq", 16);
    						appInfoList.add(nxtAppInfo);
    					}
    				}

    			}

log.info("status :"+status);
    			if(status.contains("InProcess"))
    				batchMap.put("approvalStatus", "InProcess");
    			else if(status.contains("Rejected"))
    				batchMap.put("approvalStatus", "Rejected");
    			else
    				batchMap.put("approvalStatus", "Approved");



    			batchMap.put("approversList", appInfoList);



    		}
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


    		List<LookUpCode> lookUpCodes=lookUpCodeRepository.findByTenantIdAndLookUpType(notBtc.getTenantId(), "APPROVAL_STATUS");
    		if(lookUpCodes.size()>0)
    		{
    			HashMap detail=new HashMap();
    			Long total=0l;
    			for(int i=0;i<lookUpCodes.size();i++)
    			{
    				String userAndBatchIdlook =notBtc.getCurrentApprover()+"|"+notBtc.getId()+"|"+lookUpCodes.get(i).getMeaning();
    				log.info("userAndBatchId :"+userAndBatchIdlook);
    				String queryForCount="select count(*) from "+schemaName+".t_reconciliation_result where appr_ref_"+refNum+" LIKE '"+userAndBatchIdlook+"%'";
    				log.info("queryForCount :"+queryForCount);
    				resultCount=stmtCount.executeQuery(queryForCount);
    				while(resultCount.next())
    				{
    					if(resultCount.getString("count(*)")!=null)
    						detail.put(lookUpCodes.get(i).getMeaning(), resultCount.getString("count(*)"));
    					total=total+Long.valueOf(resultCount.getString("count(*)"));

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
    		if(stmtCount!=null)
    			stmtCount.close();
    		if(resultCount!=null)
    			resultCount.close();
    	}
    	return batchMap;
    }
    
    
    /**
     * Author: Shiva
     * Purpose: Check weather approval group exist or not
     * **/
    @GetMapping("/checkApprovalGroupIsExist")
	@Timed
	public HashMap checkApprovalGroupIsExist(@RequestParam String name,HttpServletRequest request,@RequestParam(required=false,value="id") Long id)
	{
    	HashMap map0=userJdbcService.getuserInfoFromToken(request);
	  	Long tenantId=Long.parseLong(map0.get("tenantId").toString());
		HashMap map=new HashMap();
		map.put("result", "No Duplicates Found");
		if(id != null)
		{
			ApprovalGroups groupWithId = approvalGroupsRepository.findByIdAndGroupNameAndTenantId(id, name, tenantId);
			if(groupWithId != null)
			{
				map.put("result", "No Duplicates Found");
			}
			else
			{
				List<ApprovalGroups> appGroups = approvalGroupsRepository.findByTenantIdAndGroupName(tenantId, name);
				if(appGroups.size()>0)
				{
					map.put("result", "'"+name+"' approval group is already exists");
				}
			}
		}
		else 
		{
			List<ApprovalGroups> appGroups = approvalGroupsRepository.findByTenantIdAndGroupName(tenantId, name);
			if(appGroups.size()>0)
			{
				map.put("result", "'"+name+"' approval group already is exists");
			}
		}
		return map;
	}
}