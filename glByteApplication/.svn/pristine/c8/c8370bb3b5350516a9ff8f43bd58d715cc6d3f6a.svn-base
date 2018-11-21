package com.nspl.app.jbpm.web.rest;

import java.math.BigInteger;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.task.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.domain.AccountedSummary;
import com.nspl.app.domain.AccountingData;
import com.nspl.app.domain.AppModuleSummary;
import com.nspl.app.domain.DataViews;
import com.nspl.app.domain.NotificationBatch;
import com.nspl.app.domain.RuleGroup;
import com.nspl.app.domain.TenantConfig;
import com.nspl.app.jbpm.service.ApprovalTaskService;
import com.nspl.app.repository.AccountedSummaryRepository;
import com.nspl.app.repository.AccountingDataRepository;
import com.nspl.app.repository.AppModuleSummaryRepository;
import com.nspl.app.repository.ApprovalRuleAssignmentRepository;
import com.nspl.app.repository.DataViewsRepository;
import com.nspl.app.repository.NotificationBatchRepository;
import com.nspl.app.repository.NotificationsRepository;
import com.nspl.app.repository.ReconciliationResultRepository;
import com.nspl.app.repository.RuleGroupRepository;
import com.nspl.app.repository.RulesRepository;
import com.nspl.app.repository.TenantConfigRepository;
import com.nspl.app.security.jwt.TokenProvider;
import com.nspl.app.service.ApproveService;
import com.nspl.app.service.DashBoardV4Service;
import com.nspl.app.service.UserJdbcService;
import com.nspl.app.web.rest.dto.ErrorReport;

@RestController
@RequestMapping("/api")
public class ApprovalTaskResource {

	@Inject
	TaskService taskService;

	@Autowired
	RuntimeManager manager;

	@Inject
	NotificationBatchRepository notificationBatchRepository;
	
	@Inject
	ReconciliationResultRepository reconciliationResultRepository;
	
	@Inject
	ApprovalRuleAssignmentRepository approvalRuleAssignmentRepository;
	
	@Inject
	NotificationsRepository notificationsRepository;
	
	@Inject
	RulesRepository rulesRepository;
	
	@Inject
	DataViewsRepository dataViewsRepository;
	
	@Inject
	ApprovalTaskService approvalTaskService;

	@Inject
	ApproveService approveService;
	
	
	@Inject
	UserJdbcService userJdbcService;
	
	@Inject
	TokenProvider tokenProvider;

	@Inject
	AuthenticationManager authenticationManager;
	
	
	@Inject
	TenantConfigRepository tenantConfigRepository;
	
	
	@Inject
	RuleGroupRepository ruleGroupRepository;
	
	@PersistenceContext(unitName="default")
	private EntityManager em;
	
	
	@Inject
	private Environment env;
	
	
	@Inject
	AccountingDataRepository accountingDataRepository;
	
	
	@Inject
	AccountedSummaryRepository accountedSummaryRepository;
	
	@Inject
	AppModuleSummaryRepository appModuleSummaryRepository;
	
	@Inject
	DashBoardV4Service dashBoardV4Service;

	
	
	
	private final Logger log = LoggerFactory
			.getLogger(ApprovalTaskResource.class);

	
	
	/**
	 * Author: ravali
	 * Api to Multiple Approve Task
	 * @param batchId
	 * @param userId
	 * @param tenantId
	 * @param recRefList/Notification Batch Ids
	 * @throws SQLException
	 * @throws ClassNotFoundException 
	 */
	
			@PostMapping(value = "/approveTask")
			 @Timed
			public HashMap approve( HttpServletRequest request,@RequestParam(required=false) Long batchId,@RequestParam String type,
					@RequestBody List<String> ids) throws SQLException, ClassNotFoundException {
				HashMap map0=userJdbcService.getuserInfoFromToken(request);
				Long tenantId=Long.parseLong(map0.get("tenantId").toString());
				Long userId=Long.parseLong(map0.get("userId").toString());
				
				log.info("in approveTask with parameters: typr: "+type+" batchId: "+batchId+" tenantId: "+tenantId+" userId: "+userId+" ids: "+ids);
				HashMap map=new HashMap();
				List<ErrorReport> finalErrorReport=new ArrayList<ErrorReport>();
				if(type.equalsIgnoreCase("Record"))
				{

					Long taskId = null;
					NotificationBatch notBatch=notificationBatchRepository.findOne(batchId);
					try {
						taskId=approvalTaskService.getTasksByProcessId(notBatch.getProcessInstanceId(),notBatch.getCurrentApprover());
						log.info("taskId***** :"+taskId);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					log.info("****taskId*******:"+taskId);
					ErrorReport report=new ErrorReport();
					if(taskId!=null)
						report=approveService.approveTask(taskId,batchId,userId,tenantId,ids,null);
					else
					{
						report.setTaskName("Approve Task");
						report.setTaskStatus("Failed");
						report.setDetails(batchId.toString());
						log.info("taskId is null");
					}
					if(report!=null)
						finalErrorReport.add(report);
				}
				else
				{
					for(int i=0;i<ids.size();i++)
					{
						Long taskId = null;
						NotificationBatch notBatch=notificationBatchRepository.findOne(Long.parseLong(ids.get(i)));
						try {
							taskId=approvalTaskService.getTasksByProcessId(notBatch.getProcessInstanceId(),notBatch.getCurrentApprover());
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						List<String> recRefList=new ArrayList<String>();
						log.info("taskId :"+taskId);
						ErrorReport report=new ErrorReport();
						if(taskId!=null)
							report=approveService.approveTask(taskId,notBatch.getId(),userId,tenantId,recRefList,null);
						else
						{
							log.info("taskId is null");
							report.setTaskName("Approve Task");
							report.setTaskStatus("Failed");
							report.setDetails(batchId.toString());

						}
						if(report!=null)
							finalErrorReport.add(report);
					}

				}
				List<String> status=new ArrayList<String>();
				for(int i=0;i<finalErrorReport.size();i++)
				{
					status.add(finalErrorReport.get(i).getTaskStatus());
				}
				int Count=0;
				for(int s=0;s<status.size();s++)
				{
					if(status.equals("Failed"))
						Count++;
				}
				if(Count==0)
					Count=status.size();
				
				if(Count==status.size())
				map.put("status", "Success");
				else
					map.put("status", "Failed");
				map.put("count", Count+" out of "+status.size()+" Approved");
				map.put("errorReport", finalErrorReport);
				
				log.info("End of Approval Task");
				return map;

			}
			
			/**
			 * Author: ravali
			 * Api to Reject Task for multiple batch
			 * @param batchId
			 * @param userId
			 * @param tenantId
			 * @param recRefList
			 * @throws SQLException
			 * @throws ClassNotFoundException 
			 */
			@PostMapping(value = "/rejectTask")
			 @Timed
			public HashMap multipleReject(HttpServletRequest request ,@RequestParam(required=false) Long batchId,@RequestParam String type,
					@RequestBody List<String> ids) throws SQLException, ClassNotFoundException {

				HashMap map0=userJdbcService.getuserInfoFromToken(request);
				Long tenantId=Long.parseLong(map0.get("tenantId").toString());
				Long userId=Long.parseLong(map0.get("userId").toString());
				
				log.info("batchIds :"+ids+" of tenant: "+tenantId);
				log.info("userId :"+userId);
				HashMap map=new HashMap();
				List<ErrorReport> finalErrorReport=new ArrayList<ErrorReport>();
				if(type.equalsIgnoreCase("Record"))
				{

					Long taskId = null;
					NotificationBatch notBatch=notificationBatchRepository.findOne(batchId);
					try {
						taskId=approvalTaskService.getTasksByProcessId(notBatch.getProcessInstanceId(),notBatch.getCurrentApprover());
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					log.info("taskId :"+taskId);
					ErrorReport report=new ErrorReport();
					if(taskId!=null)
						report=approveService.rejectTask(taskId,batchId,userId,tenantId,ids,null);
					else
					{
						report.setTaskName("Reject Task");
						report.setTaskStatus("Failed");
						report.setDetails(batchId.toString());
						log.info("taskId is null");
					}
					if(report!=null)
						finalErrorReport.add(report);
				}
				else
				{
					for(int i=0;i<ids.size();i++)
					{
						Long taskId = null;
						NotificationBatch notBatch=notificationBatchRepository.findOne(Long.parseLong(ids.get(i)));
						try {
							taskId=approvalTaskService.getTasksByProcessId(notBatch.getProcessInstanceId(),notBatch.getCurrentApprover());
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						List<String> recRefList=new ArrayList<String>();
						log.info("taskId :"+taskId);
						ErrorReport report=new ErrorReport();
						if(taskId!=null)
							report=approveService.rejectTask(taskId,notBatch.getId(),userId,tenantId,recRefList,null);
						else
						{
							log.info("taskId is null");
							report.setTaskName("Reject Task");
							report.setTaskStatus("Failed");
							report.setDetails(batchId.toString());

						}
						if(report!=null)
							finalErrorReport.add(report);
					}

				}
				List<String> status=new ArrayList<String>();
				for(int i=0;i<finalErrorReport.size();i++)
				{
					status.add(finalErrorReport.get(i).getTaskStatus());
				}
				int Count=0;
				for(int s=0;s<status.size();s++)
				{
					if(status.equals("Failed"))
						Count++;
				}
				if(Count==0)
					Count=status.size();
				
				if(Count==status.size())
				map.put("status", "Success");
				else
					map.put("status", "Failed");
				map.put("count", Count+" out of "+status.size()+" Rejected");
				map.put("errorReport", finalErrorReport);
				
				return map;

			}
			
			/**
			 * Author: Ravali
			 * Api to perform approval actions approve and reject through email
			 * @param userId
			 * @param tenantId
			 * @param batchId
			 * @param action
			 * @throws SQLException
			 * @throws ClassNotFoundException
			 */
			@GetMapping(value = "/approveRejectTaskByEmail")
			 @Timed
			public void approveRejectByemail( @RequestParam Long userId, @RequestParam Long tenantId,@RequestParam Long batchId,@RequestParam String action) throws SQLException, ClassNotFoundException {
				log.info("Rest Request to approve or reject task through email");
			Long taskId=null;
				NotificationBatch notBatch=notificationBatchRepository.findOne(batchId);
				try {
					taskId=approvalTaskService.getTasksByProcessId(notBatch.getProcessInstanceId(),notBatch.getCurrentApprover());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				List<String> recRefList=new ArrayList<String>();
				log.info("taskId :"+taskId);
				ErrorReport report=new ErrorReport();
				if(taskId!=null && action.equalsIgnoreCase("Approve"))
					report=approveService.approveTask(taskId,notBatch.getId(),userId,tenantId,recRefList,null);
				else if(taskId!=null && action.equalsIgnoreCase("Reject"))
					report=approveService.rejectTask(taskId,notBatch.getId(),userId,tenantId,recRefList,null);
				
				else
				{
					log.info("taskId is null");
					report.setTaskName("Approve Task");
					report.setTaskStatus("Failed");
					report.setDetails(batchId.toString());

				}
				
			}
		
			/**
			 * Author: Ravali
			 * Api to Generate token and perfom approval action through email notifications
			 * @param userId
			 * @param tenantId
			 * @param batchId
			 * @param action
			 * @return
			 * @throws ClassNotFoundException
			 * @throws SQLException
			 */
			@GetMapping(value = "/tokenGenerationForEmailAprovalTask")
			 @Timed
			 public ModelAndView callAPIWithJWTTOken(@RequestParam String userId, @RequestParam String tenantId,@RequestParam String batchId,@RequestParam String action,HttpServletRequest request) throws ClassNotFoundException, SQLException
			{
				log.info("Rest api for calling rest api with JWT Token");

				HashMap map=userJdbcService.jdbcConnc(Long.parseLong(userId),Long.parseLong(tenantId));
				log.info("loginName :"+map.get("loginName"));
				log.info("password :"+map.get("password"));
				
				
			//	TenantConfig tenConfig=tenantConfigRepository.findByTenantIdAndKey(Long.valueOf(tenantId), "Application URL");
				//TenantConfig gateWayProp=tenantConfigRepository.findByTenantIdAndKey(Long.valueOf(tenantId), "GatewayPort");
				
				String gatewayPort=env.getProperty("spring.datasource.gatewayPort").toString();
				String jwt = "";
				try {
					Authentication authentication = new UsernamePasswordAuthenticationToken(map.get("loginName"), null,
							AuthorityUtils.createAuthorityList("ROLE_USER"));
					SecurityContextHolder.getContext().setAuthentication(authentication);
					jwt = tokenProvider.createToken(authentication,true);

					log.info("jwt :"+jwt);
				} catch (AuthenticationException ae) {
					log.info("Authentication exception trace: {}", ae);

				}
				if(jwt!=null && !jwt.isEmpty())
				{
					String applicationUrl=request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
					String theUrl = applicationUrl+"/api/approveRejectTaskByEmail";
					RestTemplate restTemplate = new RestTemplate();
					try {
						HttpHeaders headers = new HttpHeaders();
						headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
						headers.add("Authorization", "Bearer "+jwt);
						log.info("theUrl: "+theUrl);
						log.info("userId: "+userId+" tenantId: "+tenantId+" batchId: "+batchId+" action: "+action);
						UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(theUrl)
								.queryParam("userId", userId)
								.queryParam("tenantId", tenantId)
								.queryParam("batchId", batchId)
								.queryParam("action",action);

						HttpEntity<?> entity = new HttpEntity<>(headers);
						ResponseEntity<String> responses = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);
						System.out.println("Result - status ("+ responses.getStatusCode() + ") has body: " + responses.hasBody());
					}
					catch (Exception eek) {
						System.out.println("** Exception: "+ eek.getMessage());
					}
				}
				
				
				String gateWayhost=env.getProperty("spring.secondDatasource.serverName").toString();
				String url ="https://"+gateWayhost+":"+gatewayPort;
				log.info("gateWarUrl :"+url);
				
				if(action.equalsIgnoreCase("approve"))
					return new ModelAndView("redirect:"+url+"/#/approved");
				else
					return new ModelAndView("redirect:"+url+"/#/rejected");
				
				
			}
				
			/**
			 * Author: Swetha
			 * @param userId
			 * @param tenantId
			 * @param batchId
			 * @param type
			 * @param ids
			 * @param assignTo
			 * @return
			 * @throws SQLException
			 */
			@SuppressWarnings("unused")
			@RequestMapping(value = "/reassignTask", method = RequestMethod.POST)
			public HashMap reassignTaskByRole(HttpServletRequest request,@RequestParam(required=false) Long batchId,@RequestParam String type,
					@RequestBody List<String> ids, @RequestParam Long assignTo) throws SQLException 
			{
				HashMap map0=userJdbcService.getuserInfoFromToken(request);
				Long tenantId=Long.parseLong(map0.get("tenantId").toString());
				Long userId=Long.parseLong(map0.get("userId").toString());
				
				log.info("reassignTask with userId: "+userId+" tenantId: "+tenantId+" batchId: "+batchId+" type: "+type);
				HashMap map=new HashMap();
				List<ErrorReport> finalErrorReport=new ArrayList<ErrorReport>();
				if(type.equalsIgnoreCase("Record"))
				{

					Long taskId = null;
					NotificationBatch notBatch=notificationBatchRepository.findOne(batchId);
					try {
						taskId=approvalTaskService.getTasksByProcessId(notBatch.getProcessInstanceId(),notBatch.getCurrentApprover());
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					log.info("taskId :"+taskId);
					ErrorReport report=new ErrorReport();
					if(taskId!=null)
						report=approveService.reAssignTask(taskId,batchId,userId,tenantId,ids,assignTo);
					else
					{
						report.setTaskName("ReAssign Task");
						report.setTaskStatus("Failed");
						report.setDetails(batchId.toString());
						log.info("taskId is null");
					}
					if(report!=null)
						finalErrorReport.add(report);
				}
				else
				{
					for(int i=0;i<ids.size();i++)
					{
						Long taskId = null;
						NotificationBatch notBatch=notificationBatchRepository.findOne(Long.parseLong(ids.get(i)));
						try {
							taskId=approvalTaskService.getTasksByProcessId(notBatch.getProcessInstanceId(),notBatch.getCurrentApprover());
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						List<String> recRefList=new ArrayList<String>();
						log.info("taskId :"+taskId);
						ErrorReport report=new ErrorReport();
						if(taskId!=null)
							report=approveService.reAssignTask(taskId,notBatch.getId(),userId,tenantId,recRefList,assignTo);
						else
						{
							log.info("taskId is null");
							report.setTaskName("ReAssign Task");
							report.setTaskStatus("Failed");
							report.setDetails(batchId.toString());

						}
						if(report!=null)
							finalErrorReport.add(report);
					}

				}
				List<String> status=new ArrayList<String>();
				for(int i=0;i<finalErrorReport.size();i++)
				{
					status.add(finalErrorReport.get(i).getTaskStatus());
				}
				int Count=0;
				for(int s=0;s<status.size();s++)
				{
					if(status.equals("Failed"))
						Count++;
				}
				if(Count==0)
					Count=status.size();
				
				if(Count==status.size())
				map.put("status", "Success");
				else
					map.put("status", "Failed");
				map.put("count", Count+" out of "+status.size()+" ReAssigned");
				map.put("errorReport", finalErrorReport);
				
				log.info("End of ReAssign Task");
				return map;

			}
			
			
			/**
			 * Author: Swetha
			 * Api for Immediate approval action approve
			 * @param userId
			 * @param tenantId
			 * @param batchId
			 * @param type
			 * @param ids
			 * @return
			 * @throws SQLException
			 * @throws ClassNotFoundException
			 */
			@PostMapping(value = "/immediateApproveTask")
			 @Timed
			public HashMap adminApproveTask( @RequestParam Long userId, @RequestParam Long tenantId,@RequestParam(required=false) Long batchId,@RequestParam String type,
					@RequestBody List<String> ids) throws SQLException, ClassNotFoundException {
				log.info("in approveTask with parameters: typr: "+type+" batchId: "+batchId+" tenantId: "+tenantId+" userId: "+userId+" ids: "+ids);
				HashMap map=new HashMap();
				List<ErrorReport> finalErrorReport=new ArrayList<ErrorReport>();
				if(type.equalsIgnoreCase("Record"))
				{

					Long taskId = null;
					NotificationBatch notBatch=notificationBatchRepository.findOne(batchId);
					try {
						taskId=approvalTaskService.getTasksByProcessId(notBatch.getProcessInstanceId(),notBatch.getCurrentApprover());
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					log.info("taskId :"+taskId);
					ErrorReport report=new ErrorReport();
					if(taskId!=null)
						report=approveService.approveTask(taskId,batchId,userId,tenantId,ids,"ImmediateApprovals");
					else
					{
						report.setTaskName("Approve Task");
						report.setTaskStatus("Failed");
						report.setDetails(batchId.toString());
						log.info("taskId is null");
					}
					if(report!=null)
						finalErrorReport.add(report);
				}
				else
				{
					for(int i=0;i<ids.size();i++)
					{
						Long taskId = null;
						NotificationBatch notBatch=notificationBatchRepository.findOne(Long.parseLong(ids.get(i)));
						try {
							taskId=approvalTaskService.getTasksByProcessId(notBatch.getProcessInstanceId(),notBatch.getCurrentApprover());
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						List<String> recRefList=new ArrayList<String>();
						log.info("taskId :"+taskId);
						ErrorReport report=new ErrorReport();
						if(taskId!=null)
							report=approveService.approveTask(taskId,notBatch.getId(),userId,tenantId,recRefList,null);
						else
						{
							log.info("taskId is null");
							report.setTaskName("Approve Task");
							report.setTaskStatus("Failed");
							report.setDetails(batchId.toString());

						}
						if(report!=null)
							finalErrorReport.add(report);
					}

				}
				List<String> status=new ArrayList<String>();
				for(int i=0;i<finalErrorReport.size();i++)
				{
					status.add(finalErrorReport.get(i).getTaskStatus());
				}
				int Count=0;
				for(int s=0;s<status.size();s++)
				{
					if(status.equals("Failed"))
						Count++;
				}
				if(Count==0)
					Count=status.size();
				
				if(Count==status.size())
				map.put("status", "Success");
				else
					map.put("status", "Failed");
				map.put("count", Count+" out of "+status.size()+" Approved");
				map.put("errorReport", finalErrorReport);
				
				log.info("End of Approval Task");
				return map;

			}
			
			
			
			@Transactional
			 @RequestMapping(value = "/updateApprovalWhileInitiating",
	            method = RequestMethod.POST,
	            produces = MediaType.APPLICATION_JSON_VALUE)
	    @Timed
			public void updateApprovalWhileInitiating( @RequestParam String recIdFinDataListSz,
					@RequestParam String rulGrpId)  
			{
				log.info("in API updateApprovalWhileInitiating of dashBoard for ids of recIdFinDataListSz ");

				List<Long> idList = Arrays.asList(recIdFinDataListSz.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
				log.info("idList size :"+idList.size());
				/*	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = new Date();
				String currentDateTime = dateFormat.format(date);

			    //  SimpleDateFormat sdFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			      String apprRefOne=curApp+"|"+lastRecId+"|InProcess|"+currentDateTime;
			      String updateQuery ="";
			      if(tableName!=null && tableName.equalsIgnoreCase("ReconciliationResult"))
				       updateQuery = "UPDATE ReconciliationResult r SET r.approvalBatchId=:approvalBatchId,r.approvalGroupId=:approvalGroupId, r.approvalRuleId=:approvalRuleId,r.approvalInitiationDate=:approvalInitiationDate,r.apprRef01=:apprRef01,r.finalStatus='IN_PROCESS' WHERE r.id in (:id)";

			  	Query exe=em.createQuery(updateQuery);
				exe.setParameter("approvalBatchId", Long.valueOf(lastRecId));
				exe.setParameter("approvalGroupId", Long.valueOf(rulGrpId));
				exe.setParameter("approvalRuleId", Long.valueOf(ruleId));
				exe.setParameter("approvalInitiationDate", ZonedDateTime.now());
				exe.setParameter("apprRef01", apprRefOne);
				//exe.setParameter("finalActionDate", ZonedDateTime.now());
				exe.setParameter("id", idList);
				em.flush();
				if(exe.executeUpdate()!=0)
				{
					log.info("records updated");

				}*/


				//log.info("************* end of updating reconciliation result :"+ZonedDateTime.now());

				List<RuleGroup> recRuleGroup=ruleGroupRepository.findByApprRuleGrpId(Long.valueOf(rulGrpId));
				for(RuleGroup recRG:recRuleGroup)
				{
					List<Object[]> ruleIdAndViewIds=reconciliationResultRepository.fetchDistinctRuleIdAndViewIdByRuleGroupId(recRG.getId(),idList);
					List<java.sql.Date> fileDatesList=appModuleSummaryRepository.findDistinctFileDateByModuleAndRuleGroupId("Reconciliation",recRG.getId());
					//log.info("fileDatesList :"+fileDatesList);
					for(int d=0;d<fileDatesList.size();d++)
					{
						for(int i=0;i<ruleIdAndViewIds.size();i++)
						{
							if(ruleIdAndViewIds.get(i)[0]!=null)
							{

								AppModuleSummary checkExisting=appModuleSummaryRepository.findByFileDateAndModuleAndRuleGroupIdAndRuleIdAndViewIdAndType
			    						(fileDatesList.get(d).toLocalDate(),"Reconciliation",recRG.getId(),Long.valueOf(ruleIdAndViewIds.get(i)[2].toString()),Long.valueOf(ruleIdAndViewIds.get(i)[0].toString()),"Source");
			    				log.info("checkExisting :"+checkExisting);
			    				
			    				String fileDateOrQualifierDate=dashBoardV4Service.getFileDateOrQualifierAndGetColumnName(Long.valueOf(ruleIdAndViewIds.get(i)[0].toString()), recRG.getTenantId());
			    				
			    				
			    				String finalApprovallistQuery="SELECT originalRowId FROM ReconciliationResult where originalRowId is not null and reconciliationRuleGroupId ="+recRG.getId()+" and originalViewId ="+Long.valueOf(ruleIdAndViewIds.get(i)[0].toString())+" and reconciliationRuleId ="+Long.valueOf(ruleIdAndViewIds.get(i)[2].toString())+" and id in ("+recIdFinDataListSz+") and originalRowId in "
			    						+ "(select id from DataMaster where tenantId ="+recRG.getTenantId()+" and Date("+fileDateOrQualifierDate+")='"+fileDatesList.get(d)+"')";
			    				
			    				

			    				
			    				
			    				
			    				//log.info("finalApprovallistQuery :"+finalApprovallistQuery);
			    				Query exeFinalApprovallistQuery=em.createQuery(finalApprovallistQuery);
								List finalApprovallist=exeFinalApprovallistQuery.getResultList();

								log.info("finalApprovallist.size :"+finalApprovallist.size());


								//	List<BigInteger> finalApprovallist=reconciliationResultRepository.fetchApprovedCount(idList, recRG.getId(), Long.valueOf(ruleIdAndViewIds.get(i)[2].toString()), Long.valueOf(ruleIdAndViewIds.get(i)[0].toString()), fileDatesList.get(d).toLocalDate(),recRG.getTenantId());
								if(checkExisting!=null)
								{
									checkExisting.setInitiatedCount(Long.valueOf(finalApprovallist.size()));
									//checkExisting.setLastUpdatedBy(userId);
									checkExisting.setLastUpdatedDate(ZonedDateTime.now());
									appModuleSummaryRepository.save(checkExisting);
								}
							}
							if(ruleIdAndViewIds.get(i)[1]!=null)
							{

								AppModuleSummary checkExisting=appModuleSummaryRepository.findByFileDateAndModuleAndRuleGroupIdAndRuleIdAndViewIdAndType
			    						(fileDatesList.get(d).toLocalDate(),"Reconciliation",recRG.getId(),Long.valueOf(ruleIdAndViewIds.get(i)[2].toString()),Long.valueOf(ruleIdAndViewIds.get(i)[1].toString()),"Target");
			    				log.info("checkExisting in target:"+checkExisting);
			    			//	log.info("Long.valueOf(ruleIdAndViewIds.get(i)[1].toString()) :"+Long.valueOf(ruleIdAndViewIds.get(i)[1].toString()));
			    				String fileDateOrQualifierDate=dashBoardV4Service.getFileDateOrQualifierAndGetColumnName(Long.valueOf(ruleIdAndViewIds.get(i)[1].toString()), recRG.getTenantId());
			    				log.info("fileDateOrQualifierDate :"+fileDateOrQualifierDate);
			    				
			    				
			    			
			    				String finalApprovallistQuery="SELECT targetRowId FROM ReconciliationResult where targetRowId is not null and reconciliationRuleGroupId ="+recRG.getId()+" and targetViewId ="+Long.valueOf(ruleIdAndViewIds.get(i)[1].toString())+" and reconciliationRuleId ="+Long.valueOf(ruleIdAndViewIds.get(i)[2].toString())+" and id in ("+recIdFinDataListSz+") and targetRowId in "
			    						+ "(select id from DataMaster where tenantId ="+recRG.getTenantId()+" and Date("+fileDateOrQualifierDate+")='"+fileDatesList.get(d)+"')";
			    				
			    			
			    				
			    				//log.info("finalApprovallistQuery :"+finalApprovallistQuery);
			    				Query exeFinalApprovallistQuery=em.createQuery(finalApprovallistQuery);
								List finalApprovallist=exeFinalApprovallistQuery.getResultList();
								log.info("finalApprovallist.size :"+finalApprovallist.size());

								//	List<BigInteger> finalApprovallist=reconciliationResultRepository.fetchApprovedCountForTarget(idList, recRG.getId(), Long.valueOf(ruleIdAndViewIds.get(i)[2].toString()), Long.valueOf(ruleIdAndViewIds.get(i)[1].toString()), fileDatesList.get(d).toLocalDate(),recRG.getTenantId());
								if(checkExisting!=null)
								{
									checkExisting.setInitiatedCount(Long.valueOf(finalApprovallist.size()));
									//checkExisting.setLastUpdatedBy(userId);
									checkExisting.setLastUpdatedDate(ZonedDateTime.now());
									appModuleSummaryRepository.save(checkExisting);
								}
							}
						}
					}
				}





}
			
			
			@Transactional
			 @RequestMapping(value = "/updateActApprovalWhileInitiating",
	            method = RequestMethod.POST,
	            produces = MediaType.APPLICATION_JSON_VALUE)
	    @Timed
			public void updateActApprovalWhileInitiating( @RequestParam String recIdFinDataListSz,
					@RequestParam String rulGrpId)
			{
				log.info("in API updateActApprovalWhileInitiating for recIdFinDataList for dashboard" );

				List<Long> idList = Arrays.asList(recIdFinDataListSz.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
				log.info("idList :"+idList.size());
				/*SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = new Date();
				String currentDateTime = dateFormat.format(date);

				//  SimpleDateFormat sdFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String apprRefOne=curApp+"|"+lastRecId+"|InProcess|"+currentDateTime;
				String updateQuery ="";
				updateQuery="UPDATE AccountingData a SET a.approvalBatchId=:approvalBatchId,a.approvalGroupId=:approvalGroupId, a.approvalRuleId=:approvalRuleId,a.approvalInitiationDate=:approvalInitiationDate,a.apprRef01=:apprRef01,a.finalStatus='IN_PROCESS' WHERE a.id in (:id)";
				Query exe=em.createQuery(updateQuery);
				exe.setParameter("approvalBatchId", Long.valueOf(lastRecId));
				exe.setParameter("approvalGroupId", Long.valueOf(rulGrpId));
				exe.setParameter("approvalRuleId", Long.valueOf(ruleId));
				exe.setParameter("approvalInitiationDate", ZonedDateTime.now());
				exe.setParameter("apprRef01", apprRefOne);
				//	exe.setParameter("finalActionDate", ZonedDateTime.now());
				exe.setParameter("id", idList);
				em.flush();*/
				//if(exe.executeUpdate()!=0)
				//{
				//	log.info("records updated");

				List<BigInteger> acctDataIds=accountingDataRepository.findAccountedSummaryIdByIdIn(idList);
				log.info("acctDataIds :"+acctDataIds.size());

				List<AccountedSummary> acctSummaryList=accountedSummaryRepository.findByIdIn(acctDataIds);
				log.info("acctSummaryList :"+acctSummaryList.size());

				List<AccountedSummary> accountedSummaryList=new ArrayList<AccountedSummary>();

				for(int i=0;i<acctSummaryList.size();i++)
				{
					AccountedSummary acctSummary=acctSummaryList.get(i);
					acctSummary.setApprovalStatus("IN_PROCESS");
					accountedSummaryList.add(acctSummary);

				}
				accountedSummaryList=accountedSummaryRepository.save(accountedSummaryList);
				log.info("accountedSummaryList :"+accountedSummaryList.size());


				//}

				log.info("************* end of updating accounting data and accounting summary :"+ZonedDateTime.now());

				List<RuleGroup> actRuleGroup=ruleGroupRepository.findByApprRuleGrpId(Long.valueOf(rulGrpId));
				for(RuleGroup actRG:actRuleGroup)
				{
					List<Object[]> ruleIdAndViewIds=accountingDataRepository.fetchDistinctRuleIdAndViewIdByRuleGroupId(actRG.getId(),idList);
					log.info("ruleIdAndViewIds :"+ruleIdAndViewIds.size());
					List<java.sql.Date> fileDatesList=appModuleSummaryRepository.findDistinctFileDateByModuleAndRuleGroupId("Accounting",actRG.getId());
					//log.info("fileDatesList :"+fileDatesList);
					for(int d=0;d<fileDatesList.size();d++)
					{
						for(int i=0;i<ruleIdAndViewIds.size();i++)
						{
							//log.info("ruleIdAndViewIds.get(i)[0] :"+ruleIdAndViewIds.get(i)[0]);
							//log.info("ruleIdAndViewIds.get(i)[1] :"+ruleIdAndViewIds.get(i)[1]);
							if(ruleIdAndViewIds.get(i)[0]!=null)
							{

								AppModuleSummary checkExisting=appModuleSummaryRepository.findByFileDateAndModuleAndRuleGroupIdAndRuleIdAndViewIdAndType
										(fileDatesList.get(d).toLocalDate(),"Accounting",actRG.getId(),Long.valueOf(ruleIdAndViewIds.get(i)[1].toString()),Long.valueOf(ruleIdAndViewIds.get(i)[0].toString()),"Accounted");
								log.info("checkExisting :"+checkExisting);

								String fileDateOrQualifierDate=dashBoardV4Service.getFileDateOrQualifierAndGetColumnName(Long.valueOf(ruleIdAndViewIds.get(i)[0].toString()), actRG.getTenantId());


								String finalApprovallistQuery="SELECT originalRowId FROM AccountingData where acctGroupId ="+actRG.getId()+" and originalViewId ="+Long.valueOf(ruleIdAndViewIds.get(i)[0].toString())+" and acctRuleId ="+Long.valueOf(ruleIdAndViewIds.get(i)[1].toString())+" and id in ("+recIdFinDataListSz+") and original_row_id in "
										+ "(select id from DataMaster where tenantId ="+actRG.getTenantId()+" and Date("+fileDateOrQualifierDate+")='"+fileDatesList.get(d)+"') group by original_row_id";

								//log.info("finalApprovallistQuery :"+finalApprovallistQuery);
								Query exeFinalApprovallistQuery=em.createQuery(finalApprovallistQuery);
								List finalApprovallist=exeFinalApprovallistQuery.getResultList();

								//List<BigInteger> finalApprovallist=accountingDataRepository.fetchActApprovedCount(idList, actRG.getId(), Long.valueOf(ruleIdAndViewIds.get(i)[1].toString()), Long.valueOf(ruleIdAndViewIds.get(i)[0].toString()), fileDatesList.get(d).toLocalDate(),actRG.getTenantId());
								log.info("finalApprovallist size :"+finalApprovallist.size());
								if(checkExisting!=null)
								{
									checkExisting.setInitiatedCount(Long.valueOf(finalApprovallist.size()));
									//checkExisting.setLastUpdatedBy(userId);
									checkExisting.setLastUpdatedDate(ZonedDateTime.now());
									appModuleSummaryRepository.save(checkExisting);
								}
							}
						}
					}
				}
}
			
			
			
	
}
			
