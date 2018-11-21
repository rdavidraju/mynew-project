package com.nspl.app.jbpm.web.rest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;
import org.kie.api.runtime.manager.RuntimeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.config.ApplicationContextProvider;
import com.nspl.app.domain.ApprovalGroupMembers;
import com.nspl.app.domain.ApprovalRuleAssignment;
import com.nspl.app.domain.DataViews;
import com.nspl.app.domain.NotificationBatch;
import com.nspl.app.domain.Notifications;
import com.nspl.app.domain.Rules;
import com.nspl.app.jbpm.service.ApprovalProcessService;
import com.nspl.app.repository.ApprovalGroupMembersRepository;
import com.nspl.app.repository.ApprovalRuleAssignmentRepository;
import com.nspl.app.repository.DataViewsRepository;
import com.nspl.app.repository.NotificationBatchRepository;
import com.nspl.app.repository.NotificationsRepository;
import com.nspl.app.repository.RulesRepository;
import com.nspl.app.repository.TenantConfigRepository;


@RestController
@RequestMapping("/api")
public class ApprovalProcessResource {
	
	
	private final Logger log = LoggerFactory.getLogger(ApprovalProcessResource.class);
	
	@Inject
	ApprovalProcessService approvalProcessService;
	
	@Inject
	NotificationBatchRepository notificationBatchRepository;
	
	@Inject
    private Environment env;
	
	@Inject
	ApprovalRuleAssignmentRepository approvalRuleAssignmentRepository;
	
	@Inject
	NotificationsRepository notificationsRepository;
	
	@Inject
	RulesRepository rulesRepository;
	
	@Inject
	DataViewsRepository dataViewsRepository;
	
	@Inject
	TenantConfigRepository tenantConfigRepository;
	
	@Inject
	ApprovalGroupMembersRepository approvalGroupMembersRepository;
	
	/**
	 * Author: Swetha
	 * Api to Initiate for Approvals
	 * @param batchId
	 * @param ruleId
	 * @param userId
	 * @param tenantId
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	@RequestMapping(value = "/initiateApprovals",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Long initiateApprovals(@RequestParam Long batchId ,@RequestParam Long ruleId,@RequestParam Long userId,@RequestParam Long tenantId, @RequestParam String reconDate,HttpServletRequest request) throws ClassNotFoundException, SQLException {
    	log.debug("REST request to initiateApprovals with approvers for batchId: "+batchId);
    	NotificationBatch batch=notificationBatchRepository.findOne(batchId);
    	log.info("batch: "+batch);
    	Rules ruleData=rulesRepository.findOne(ruleId);
    	Long viewId=ruleData.getSourceDataViewId();
    	DataViews dv=dataViewsRepository.findById(viewId);
    	String viewName=dv.getDataViewName();
    	List finalApproverlist = new ArrayList();
    	List<ApprovalRuleAssignment> approvalRuleAssignmentList=approvalRuleAssignmentRepository.findByRuleId(ruleId);
    	
    	 List userIdList=new ArrayList<>();
    	 log.info("approvalRuleAssignmentList.size() : "+approvalRuleAssignmentList.size());
	    	for(int j=0;j<approvalRuleAssignmentList.size();j++){ //3,4,5
	    		
	    		ApprovalRuleAssignment map=approvalRuleAssignmentList.get(j); //3
	    		log.info("map: "+map);
	    		if(map.getAssignType().equalsIgnoreCase("user"))
	    		{
	    		Long approverId=map.getAssigneeId(); //3
	    		userIdList.add(approverId);
	    		}
	    		else
	    		{
	    			List<ApprovalGroupMembers> apprGrpMem=approvalGroupMembersRepository.findByGroupIdOrderBySeqAsc(map.getAssigneeId());
	    			for(int g=0;g<apprGrpMem.size();g++)
	    			{
	    				Long approverId=apprGrpMem.get(g).getUserId();
	    				userIdList.add(approverId);
	    			}
	    		}
	    		
	    		
	    	}
	    	System.out.println("userIdList: "+userIdList);
	    	String userIdListStr=userIdList.toString();
	    	userIdListStr=userIdListStr.replace("]", "");
	    	userIdListStr=userIdListStr.replace("[", "");
	    	//System.out.println("userIdListStr: "+userIdListStr); //3,4,5
	    	
	/*String gatewayUrl=env.getProperty("spring.datasource.gatewayUrl");
	String[] parts=gatewayUrl.split("[\\s@&?$+-]+");
	String host = parts[0].split("/")[2].split(":")[0];
	String schemaName=parts[0].split("/")[3];
	String userName = env.getProperty("spring.datasource.username");
	String password = env.getProperty("spring.datasource.password");
	String jdbcDriver = env.getProperty("spring.datasource.jdbcdriver");*/
	    
/*	   List<TenantConfig> gatewayPropertiesList= tenantConfigRepository.findByTenantIdAndMeaning(tenantId, "GatewayProperties");
	   HashMap propsMap=new HashMap();
	   for(int i=0;i<gatewayPropertiesList.size();i++){
		   TenantConfig tConfig=gatewayPropertiesList.get(i);
		   propsMap.put(tConfig.getKey(), tConfig.getValue());
	   }*/
	 //  log.info("Gateway propsMap: "+propsMap); 
	    	
	    	/*String schemaName= env.getProperty("spring.datasource.gatewayschema").toString();
			String userName =env.getProperty("spring.datasource.gatewayUserName").toString();
			String password =env.getProperty("spring.datasource.gatewayPassword").toString();
			String jdbcDriver = env.getProperty("spring.datasource.jdbcdriver").toString();
			String dbUrl=env.getProperty("spring.datasource.gatewayUrl").toString();*/
	    	String schemaName= env.getProperty("spring.secondDatasource.databaseName").toString();
	    	
	   Connection conn = null;
	   Statement stmt2 = null;
	   ResultSet result = null;
	
	   ResultSet rs=null;
	   List<HashMap> mapList2=new ArrayList<HashMap>();
	   List<HashMap> mapList3=new ArrayList<HashMap>();
	   HashMap finalMap=new HashMap();
	try{
	     // Class.forName(jdbcDriver);
	     // conn = DriverManager.getConnection(dbUrl, userName,password);
	      
		    DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("secondaryDataSource");
			conn = ds.getConnection();
	      
	     // log.info("Connected database successfully...");

	    //  log.info("userIdListStr before query:"+userIdListStr);
	      String query="SELECT * FROM "+schemaName+".jhi_user where id in ("+userIdListStr+")";
	      
	//log.info("***query***:"+query);
	      stmt2 = conn.createStatement();
	  
	      result=stmt2.executeQuery(query);
	     
		   rs=stmt2.getResultSet();
		
		  ResultSetMetaData rsmd2 = result.getMetaData();
		  //log.info("col count: "+rsmd2.getColumnCount());
		int columnsNumber = rsmd2.getColumnCount();
		//log.info("columnsNumber: "+columnsNumber);
		int columnCount = rsmd2.getColumnCount();
		while(rs.next()){
			//HashMap finalMap=new HashMap();
			System.out.println("rs.getString(id): "+rs.getString(1));
			Long uId=Long.parseLong(rs.getString("id"));
		 HashMap<String,String> map2=new HashMap<String,String>();
		for (int i = 1; i <= columnCount; i++ ) {
			//System.out.println("i in rs.next():"+i+"rsmd2.getColumnName(i)");
			  String name = rsmd2.getColumnName(i); 
		 if(!name.equalsIgnoreCase("image"))
		 map2.put(name, rs.getString(i));
		}
		
		mapList2.add(map2);
		finalMap.put(uId,map2);
		//log.info("mapList2: "+mapList2);
		log.info("finalMap: "+finalMap);
		}
	}catch(SQLException se){
		   log.info("Exception in initiating approvals for batchId: "+batchId+"is "+se);
	}
		finally{
			result.close();
			rs.close();
			
			stmt2.close();
			conn.close();
		}
	 	   
    	Map<String, Object> approversMap3 = new HashMap<String, Object>();
    	Map<String, Object> approversMap5 = new HashMap<String, Object>();
    	log.info("approvalRuleAssignmentList sz: "+approvalRuleAssignmentList.size());
    	
    	for(int i=0;i<approvalRuleAssignmentList.size();i++)
    	{
    		ApprovalRuleAssignment txnApprActionHistoryDTO2=approvalRuleAssignmentList.get(i); //3
    		String type=txnApprActionHistoryDTO2.getAssignType();
    		 //3
    		//log.info("approver: "+approver+" with type: "+type);
    		if(type.equalsIgnoreCase("user")){
    			Long approver=txnApprActionHistoryDTO2.getAssigneeId();
    			Map<String, Object> approversMap2 = new HashMap<String, Object>();
    			List groupList2 = new ArrayList();
    			HashMap userData=(HashMap) finalMap.get(approver); 
    			groupList2.add(approver); //3
    			groupList2.add(null); //Escalation Time
    			groupList2.add(null); //Escalation Manager
    			groupList2.add(userData.get("email").toString()); //swetha.kaukuntla@gmail.com
    			groupList2.add("U"+approver); //U3
    			approversMap2.put("User", groupList2); //{"User",{3,"swetha.kaukuntla@gmail.com"},"U3"}
    			finalApproverlist.add(approversMap2); //[{"User",{3,"swetha.kaukuntla@gmail.com"},"U3"}]
    		}
    		if(type.equalsIgnoreCase("group")){
    			List<ApprovalGroupMembers> apprGrpMem=approvalGroupMembersRepository.findByGroupIdOrderBySeqAsc(txnApprActionHistoryDTO2.getAssigneeId());
    			log.info("apprGrpMem :"+apprGrpMem);
    			for(int g=0;g<apprGrpMem.size();g++)
    			{
    			Map<String, Object> approversMap2 = new HashMap<String, Object>();
    			List groupList2 = new ArrayList();
    			log.info("apprGrpMem.get(g).getUserId() :"+apprGrpMem.get(g).getUserId());
    			log.info("finalMap.get(apprGrpMem.get(g).getUserId()) :"+finalMap.get(apprGrpMem.get(g).getUserId()));
    			HashMap userData=(HashMap) finalMap.get(apprGrpMem.get(g).getUserId()); 
    			log.info("userData :"+userData);
    			groupList2.add(apprGrpMem.get(g).getUserId()); //3
    			groupList2.add(null); //Escalation Time
    			groupList2.add(null); //Escalation Manager
    			groupList2.add(userData.get("email").toString()); //swetha.kaukuntla@gmail.com
    			groupList2.add("U"+apprGrpMem.get(g).getUserId()); //U3
    			approversMap2.put("User", groupList2); //{"User",{3,"swetha.kaukuntla@gmail.com"},"U3"}
    			finalApproverlist.add(approversMap2); //[{"User",{3,"swetha.kaukuntla@gmail.com"},"U3"}]
    			}
    		}
    		
    	}
    	 log.info("finalApproverlist: "+finalApproverlist);
    	
    	/*for(int u=0;u<finalApproverlist.size();u++){
    		log.info("app["+u+"]: "+finalApproverlist.get(u));
    	}*/
		Long pid = approvalProcessService.intiateApprovalProcess(batchId.toString(), finalApproverlist,tenantId,userId,request);
		log.info("Approval Process initiated with pid: "+pid);
		batch.setProcessInstanceId(pid);
		notificationBatchRepository.save(batch);
		log.info("updated batch with processInstanceId");
		Notifications notification=new Notifications();
		notification.setModule("APPROVALS");
		notification.setMessage("DataView "+viewName+" Requires your approval");
		notification.setUserId(userId);
		notification.isViewed(false);
		notification.setActionType("Notification Batch");
		notification.setActionValue(batchId.toString());
		notification.setTenantId(tenantId);
		notification.setCreatedBy(userId);
		notification.setLastUpdatedBy(userId);
		notification.setCreationDate(ZonedDateTime.now());
		notification.setLastUpdatedDate(ZonedDateTime.now());
		//log.info("saving notification with : "+notification);
		Notifications newNotif=notificationsRepository.save(notification);
		log.info("new notification created with id: "+newNotif.getId());
		return pid;
    }
	
	/**
	 * Accounting and Reconciliation Approvals
	 * @param batchId
	 * @param ruleId
	 * @param userId
	 * @param tenantId
	 * @param reconDate
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	@RequestMapping(value = "/initiateAccApprovals",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Long initiateAccApprovals(@RequestParam Long batchId ,@RequestParam Long ruleId,@RequestParam Long userId,@RequestParam Long tenantId,HttpServletRequest request) throws ClassNotFoundException, SQLException {
    	log.debug("REST request to initiateApprovals22 with approvers for batchId: "+batchId);
    	NotificationBatch batch=notificationBatchRepository.findOne(batchId);
    	log.info("batch: "+batch);
    	Rules ruleData=rulesRepository.findOne(ruleId);
    	Long viewId=ruleData.getSourceDataViewId();
    	DataViews dv=dataViewsRepository.findById(viewId);
    	String viewName=dv.getDataViewName();
    	List finalApproverlist = new ArrayList();
    	List<ApprovalRuleAssignment> approvalRuleAssignmentList=approvalRuleAssignmentRepository.findByRuleId(ruleId);
    	
    	 List userIdList=new ArrayList<>();
    	 log.info("approvalRuleAssignmentList.size() : "+approvalRuleAssignmentList.size());
	    	for(int j=0;j<approvalRuleAssignmentList.size();j++){ //3,4,5
	    		
	    		ApprovalRuleAssignment map=approvalRuleAssignmentList.get(j); //3
	    		log.info("map: "+map);
	    		if(map.getAssignType().equalsIgnoreCase("user"))
	    		{
	    		Long approverId=map.getAssigneeId(); //3
	    		userIdList.add(approverId);
	    		}
	    		else
	    		{
	    			List<ApprovalGroupMembers> apprGrpMem=approvalGroupMembersRepository.findByGroupIdOrderBySeqAsc(map.getAssigneeId());
	    			for(int g=0;g<apprGrpMem.size();g++)
	    			{
	    				Long approverId=apprGrpMem.get(g).getUserId();
	    				userIdList.add(approverId);
	    			}
	    		}
	    		
	    		
	    	}
	    	System.out.println("userIdList: "+userIdList);
	    	String userIdListStr=userIdList.toString();
	    	userIdListStr=userIdListStr.replace("]", "");
	    	userIdListStr=userIdListStr.replace("[", "");
	    	//System.out.println("userIdListStr: "+userIdListStr); //3,4,5
	    	

	    	/*List<TenantConfig> tntConfig=tenantConfigRepository.findByTenantIdAndMeaning(tenantId, "GatewayProperties");
			log.info("tntConfig :"+tntConfig.size());
			LinkedHashMap lhm=new LinkedHashMap();
			for(TenantConfig tntCfg:tntConfig)
			{
				lhm.put(tntCfg.getKey(), tntCfg.getValue());
				
			}*/
	    	/*String schemaName= env.getProperty("spring.datasource.gatewayschema").toString();
			String userName =env.getProperty("spring.datasource.gatewayUserName").toString();
			String password =env.getProperty("spring.datasource.gatewayPassword").toString();
			String jdbcDriver = env.getProperty("spring.datasource.jdbcdriver").toString();
			String dbUrl=env.getProperty("spring.datasource.gatewayUrl").toString();*/
	    	
	    	String schemaName= env.getProperty("spring.secondDatasource.databaseName").toString();
	    	
	   Connection conn = null;
	   Statement stmt = null;
	   Statement stmt2 = null;
	   ResultSet result = null;
	   ResultSet rs=null;
	   List<HashMap> mapList2=new ArrayList<HashMap>();
	   List<HashMap> mapList3=new ArrayList<HashMap>();
	   HashMap finalMap=new HashMap();
	try{
		
	    DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("secondaryDataSource");
		conn = ds.getConnection();
	    //  Class.forName(jdbcDriver);
	    //  conn = DriverManager.getConnection(dbUrl, userName, password);
	      log.info("Connected database successfully...");
	      stmt = conn.createStatement();
	      
	      String query="SELECT * FROM "+schemaName+".jhi_user where id in ("+userIdListStr+")";
	
	      stmt2 = conn.createStatement();
	      String count = null;
	      result=stmt2.executeQuery(query);
	     
		   rs=stmt2.getResultSet();
		
		  ResultSetMetaData rsmd2 = result.getMetaData();
		  //log.info("col count: "+rsmd2.getColumnCount());
		int columnsNumber = rsmd2.getColumnCount();
		//log.info("columnsNumber: "+columnsNumber);
		int columnCount = rsmd2.getColumnCount();
		while(rs.next()){
			//HashMap finalMap=new HashMap();
			//System.out.println("rs.getString(1): "+rs.getString(1));
			Long uId=Long.parseLong(rs.getString(1));
		 HashMap<String,String> map2=new HashMap<String,String>();
		for (int i = 1; i <= columnCount; i++ ) {
			  String name = rsmd2.getColumnName(i); 
		 for(int t=0,num=1;t<columnsNumber;t++, num++){ 
			 String Val=rs.getString(num);
		 }
		 map2.put(name, rs.getString(i));
		}
		mapList2.add(map2);
		finalMap.put(uId,map2);
		//log.info("mapList2: "+mapList2);
		log.info("finalMap: "+finalMap);
		}
	}catch(SQLException se){
		   log.info("Exception in initiating approvals for batchId: "+batchId+"is "+se);
	}
		finally{
			result.close();
			rs.close();
			stmt.close();
			stmt2.close();
			conn.close();
		}
	 	   
    	Map<String, Object> approversMap3 = new HashMap<String, Object>();
    	Map<String, Object> approversMap5 = new HashMap<String, Object>();
    	log.info("approvalRuleAssignmentList sz: "+approvalRuleAssignmentList.size());
    	
    	for(int i=0;i<approvalRuleAssignmentList.size();i++)
    	{
    		ApprovalRuleAssignment txnApprActionHistoryDTO2=approvalRuleAssignmentList.get(i); //3
    		String type=txnApprActionHistoryDTO2.getAssignType();
    		 //3
    		//log.info("approver: "+approver+" with type: "+type);
    		if(type.equalsIgnoreCase("user")){
    			Long approver=txnApprActionHistoryDTO2.getAssigneeId();
    			Map<String, Object> approversMap2 = new HashMap<String, Object>();
    			List groupList2 = new ArrayList();
    			HashMap userData=(HashMap) finalMap.get(approver); 
    			groupList2.add(approver); //3
    			groupList2.add(null); //Escalation Time
    			groupList2.add(null); //Escalation Manager
    			groupList2.add(userData.get("email").toString()); //swetha.kaukuntla@gmail.com
    			groupList2.add("U"+approver); //U3
    			approversMap2.put("User", groupList2); //{"User",{3,"swetha.kaukuntla@gmail.com"},"U3"}
    			finalApproverlist.add(approversMap2); //[{"User",{3,"swetha.kaukuntla@gmail.com"},"U3"}]
    		}
    		if(type.equalsIgnoreCase("group")){
    			List<ApprovalGroupMembers> apprGrpMem=approvalGroupMembersRepository.findByGroupIdOrderBySeqAsc(txnApprActionHistoryDTO2.getAssigneeId());
    			log.info("apprGrpMem :"+apprGrpMem);
    			for(int g=0;g<apprGrpMem.size();g++)
    			{
    			Map<String, Object> approversMap2 = new HashMap<String, Object>();
    			List groupList2 = new ArrayList();
    			log.info("apprGrpMem.get(g).getUserId() :"+apprGrpMem.get(g).getUserId());
    			log.info("finalMap.get(apprGrpMem.get(g).getUserId()) :"+finalMap.get(apprGrpMem.get(g).getUserId()));
    			HashMap userData=(HashMap) finalMap.get(apprGrpMem.get(g).getUserId()); 
    			log.info("userData :"+userData);
    			groupList2.add(apprGrpMem.get(g).getUserId()); //3
    			groupList2.add(null); //Escalation Time
    			groupList2.add(null); //Escalation Manager
    			groupList2.add(userData.get("email").toString()); //swetha.kaukuntla@gmail.com
    			groupList2.add("U"+apprGrpMem.get(g).getUserId()); //U3
    			approversMap2.put("User", groupList2); //{"User",{3,"swetha.kaukuntla@gmail.com"},"U3"}
    			finalApproverlist.add(approversMap2); //[{"User",{3,"swetha.kaukuntla@gmail.com"},"U3"}]
    			}
    		}
    		
    	}
    	 log.info("finalApproverlist: "+finalApproverlist);
    	
    	/*for(int u=0;u<finalApproverlist.size();u++){
    		log.info("app["+u+"]: "+finalApproverlist.get(u));
    	}*/
		Long pid = approvalProcessService.intiateAccountingApprovalProcess(batchId.toString(), finalApproverlist,tenantId,userId,request);
		log.info("Approval Process initiated with pid: "+pid);
		batch.setProcessInstanceId(pid);
		notificationBatchRepository.save(batch);
		log.info("updated batch with processInstanceId");
		Notifications notification=new Notifications();
		notification.setModule("ACCOUNTING_APPROVALS");
		notification.setMessage("DataView "+viewName+" Requires your approval");
		notification.setUserId(userId);
		notification.isViewed(false);
		notification.setActionType("Notification Batch");
		notification.setActionValue(batchId.toString());
		notification.setTenantId(tenantId);
		notification.setCreatedBy(userId);
		notification.setLastUpdatedBy(userId);
		notification.setCreationDate(ZonedDateTime.now());
		notification.setLastUpdatedDate(ZonedDateTime.now());
		//log.info("saving notification with : "+notification);
		Notifications newNotif=notificationsRepository.save(notification);
		log.info("new notification created with id: "+newNotif.getId());
		return pid;
    }
	
	
	@RequestMapping(value = "/initiateJournalApprovals",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Long initiateJournalApprovals(@RequestParam Long batchId ,@RequestParam Long ruleId,@RequestParam Long userId,@RequestParam Long tenantId,HttpServletRequest request) throws ClassNotFoundException, SQLException {
    	log.debug("REST request to initiateApprovals22 with approvers for batchId: "+batchId);
    	NotificationBatch batch=notificationBatchRepository.findOne(batchId);
    	log.info("batch: "+batch);
    	
    	
    	Rules ruleData=rulesRepository.findOne(ruleId);
    
    	List finalApproverlist = new ArrayList();
    	List<ApprovalRuleAssignment> approvalRuleAssignmentList=approvalRuleAssignmentRepository.findByRuleId(ruleId);
    	
    	 List userIdList=new ArrayList<>();
    	 log.info("approvalRuleAssignmentList.size() : "+approvalRuleAssignmentList.size());
	    	for(int j=0;j<approvalRuleAssignmentList.size();j++){ //3,4,5
	    		
	    		ApprovalRuleAssignment map=approvalRuleAssignmentList.get(j); //3
	    		log.info("map: "+map);
	    		Long approverId=map.getAssigneeId(); //3
	    		userIdList.add(approverId);
	    	}
	    	System.out.println("userIdList: "+userIdList);
	    	String userIdListStr=userIdList.toString();
	    	userIdListStr=userIdListStr.replace("]", "");
	    	userIdListStr=userIdListStr.replace("[", "");
	    	//System.out.println("userIdListStr: "+userIdListStr); //3,4,5
	    	

	    	/*List<TenantConfig> tntConfig=tenantConfigRepository.findByTenantIdAndMeaning(tenantId, "GatewayProperties");
			log.info("tntConfig :"+tntConfig.size());
			LinkedHashMap lhm=new LinkedHashMap();
			for(TenantConfig tntCfg:tntConfig)
			{
				lhm.put(tntCfg.getKey(), tntCfg.getValue());
				
			}*/
	    	/*String schemaName= env.getProperty("spring.datasource.gatewayschema").toString();
			String userName =env.getProperty("spring.datasource.gatewayUserName").toString();
			String password =env.getProperty("spring.datasource.gatewayPassword").toString();
			String jdbcDriver = env.getProperty("spring.datasource.jdbcdriver").toString();
			String dbUrl=env.getProperty("spring.datasource.gatewayUrl").toString();*/
	    	
	    	String schemaName= env.getProperty("spring.secondDatasource.databaseName").toString();
	    	
	   Connection conn = null;
	   Statement stmt = null;
	   Statement stmt2 = null;
	   ResultSet result = null;
	   ResultSet rs=null;
	   List<HashMap> mapList2=new ArrayList<HashMap>();
	   List<HashMap> mapList3=new ArrayList<HashMap>();
	   HashMap finalMap=new HashMap();
	try{
		
	    DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("secondaryDataSource");
		conn = ds.getConnection();
	    //  Class.forName(jdbcDriver);
	    //  conn = DriverManager.getConnection(dbUrl, userName, password);
	      log.info("Connected database successfully...");
	      stmt = conn.createStatement();
	      
	      String query="SELECT * FROM "+schemaName+".jhi_user where id in ("+userIdListStr+")";
	
	      stmt2 = conn.createStatement();
	      String count = null;
	      result=stmt2.executeQuery(query);
	     
		   rs=stmt2.getResultSet();
		
		  ResultSetMetaData rsmd2 = result.getMetaData();
		  //log.info("col count: "+rsmd2.getColumnCount());
		int columnsNumber = rsmd2.getColumnCount();
		//log.info("columnsNumber: "+columnsNumber);
		int columnCount = rsmd2.getColumnCount();
		while(rs.next()){
			//HashMap finalMap=new HashMap();
			//System.out.println("rs.getString(1): "+rs.getString(1));
			Long uId=Long.parseLong(rs.getString(1));
		 HashMap<String,String> map2=new HashMap<String,String>();
		for (int i = 1; i <= columnCount; i++ ) {
			  String name = rsmd2.getColumnName(i); 
		 for(int t=0,num=1;t<columnsNumber;t++, num++){ 
			 String Val=rs.getString(num);
		 }
		 map2.put(name, rs.getString(i));
		}
		mapList2.add(map2);
		finalMap.put(uId,map2);
		//log.info("mapList2: "+mapList2);
		log.info("finalMap: "+finalMap);
		}
	}catch(SQLException se){
		   log.info("Exception in initiating approvals for batchId: "+batchId+"is "+se);
	}
		finally{
			result.close();
			rs.close();
			stmt.close();
			stmt2.close();
			conn.close();
		}
	 	   
    	Map<String, Object> approversMap3 = new HashMap<String, Object>();
    	Map<String, Object> approversMap5 = new HashMap<String, Object>();
    	log.info("approvalRuleAssignmentList sz: "+approvalRuleAssignmentList.size());
    	
    	for(int i=0;i<approvalRuleAssignmentList.size();i++)
    	{
    		ApprovalRuleAssignment txnApprActionHistoryDTO2=approvalRuleAssignmentList.get(i); //3
    		String type=txnApprActionHistoryDTO2.getAssignType();
    		Long approver=txnApprActionHistoryDTO2.getAssigneeId(); //3
    		//log.info("approver: "+approver+" with type: "+type);
    		//if(type.equalsIgnoreCase("user")){
    			Map<String, Object> approversMap2 = new HashMap<String, Object>(); //{User=[Connor, 365d, Susan, Connor@reliance.us]}
    			List groupList2 = new ArrayList();
    			HashMap userData=(HashMap) finalMap.get(approver); 
    			groupList2.add(approver); //3
    			groupList2.add(null); //Escalation Time
    			groupList2.add(null); //Escalation Manager
    			groupList2.add(userData.get("email").toString()); //swetha.kaukuntla@gmail.com
    			groupList2.add("U"+approver); //U3
    			approversMap2.put("User", groupList2); //{"User",{3,"swetha.kaukuntla@gmail.com"},"U3"}
    			finalApproverlist.add(approversMap2); //[{"User",{3,"swetha.kaukuntla@gmail.com"},"U3"}]
    		//}
    		
    	}
    	 log.info("finalApproverlist: "+finalApproverlist);
    	
    	/*for(int u=0;u<finalApproverlist.size();u++){
    		log.info("app["+u+"]: "+finalApproverlist.get(u));
    	}*/
		Long pid = approvalProcessService.intiateJournalApprovalProcess(batchId.toString(), finalApproverlist,tenantId,userId,request);
		log.info("Approval Process initiated with pid: "+pid);
		batch.setProcessInstanceId(pid);
		notificationBatchRepository.save(batch);
		log.info("updated batch with processInstanceId");
		Notifications notification=new Notifications();
		notification.setModule("JOURNAL_APPROVALS");
		notification.setMessage("Template Requires your approval");
		notification.setUserId(userId);
		notification.isViewed(false);
		notification.setActionType("Notification Batch");
		notification.setActionValue(batchId.toString());
		notification.setTenantId(tenantId);
		notification.setCreatedBy(userId);
		notification.setLastUpdatedBy(userId);
		notification.setCreationDate(ZonedDateTime.now());
		notification.setLastUpdatedDate(ZonedDateTime.now());
		//log.info("saving notification with : "+notification);
		Notifications newNotif=notificationsRepository.save(notification);
		log.info("new notification created with id: "+newNotif.getId());
		return pid;
    }
}
