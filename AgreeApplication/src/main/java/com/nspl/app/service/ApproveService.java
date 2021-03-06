package com.nspl.app.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.task.TaskEvent;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.Task;
import org.kie.internal.task.api.InternalTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.nspl.app.domain.AccountedSummary;
import com.nspl.app.domain.AccountingData;
import com.nspl.app.domain.AppModuleSummary;
import com.nspl.app.domain.ApprovalGroupMembers;
import com.nspl.app.domain.ApprovalRuleAssignment;
import com.nspl.app.domain.DataViews;
import com.nspl.app.domain.JournalApprovalInfo;
import com.nspl.app.domain.NotificationBatch;
import com.nspl.app.domain.Notifications;
import com.nspl.app.domain.ReconciliationResult;
import com.nspl.app.jbpm.service.ApprovalProcessService;
import com.nspl.app.jbpm.service.ApprovalTaskService;
import com.nspl.app.jbpm.util.EmailNotification;
import com.nspl.app.jbpm.web.rest.ApprovalProcessResource;
import com.nspl.app.repository.AccountedSummaryRepository;
import com.nspl.app.repository.AccountingDataRepository;
import com.nspl.app.repository.AppModuleSummaryRepository;
import com.nspl.app.repository.ApprovalGroupMembersRepository;
import com.nspl.app.repository.ApprovalRuleAssignmentRepository;
import com.nspl.app.repository.DataViewsRepository;
import com.nspl.app.repository.JournalApprovalInfoRepository;
import com.nspl.app.repository.NotificationBatchRepository;
import com.nspl.app.repository.NotificationsRepository;
import com.nspl.app.repository.ReconciliationResultRepository;
import com.nspl.app.web.rest.dto.ErrorReport;
import com.nspl.app.web.rest.dto.StatusStringDTO;


@Service
public class ApproveService {
	@Inject
	NotificationBatchRepository notificationBatchRepository;
	
	@Inject
	ReconciliationResultRepository reconciliationResultRepository;
	
	@Inject
	ApprovalRuleAssignmentRepository approvalRuleAssignmentRepository;
	
	@Inject
	NotificationsRepository notificationsRepository;
	
	@Inject
	AppModuleSummaryRepository appModuleSummaryRepository;
	
	
	@Inject
	DashBoardV4Service dashBoardV4Service;
	
	@Inject
	DataViewsRepository dataViewsRepository;
	
	@Inject
	JournalApprovalInfoRepository journalApprovalInfoRepository;
	
	@Inject
	ApprovalGroupMembersRepository approvalGroupMembersRepository;
	
	@PersistenceContext(unitName="default")
	private EntityManager em;

	private final Logger log = LoggerFactory
			.getLogger(ApproveService.class);
	
	@Inject
	TaskService taskService;

	@Autowired
	RuntimeManager manager;

	@Inject
    private Environment env;
	
	@Inject
	ApprovalProcessService approvalProcessService;  
	
	@Inject
	ApprovalProcessResource approvalProcessResource;
	
	@Inject
	AccountingDataRepository accountingDataRepository;
	
	@Inject
	AccountedSummaryRepository accountedSummaryRepository;
	
	@Inject
	ApprovalTaskService approvalTaskService;
	
	/**
	 * Author: Swetha
	 * Api to Approve Task
	 * @param taskId
	 * @param batchId
	 * @param userId
	 * @param tenantId
	 * @param recRefList
	 * @throws SQLException
	 * @throws ClassNotFoundException 
	 * @throws ParseException 
	 */
	public ErrorReport approveTask(Long taskId,
			Long batchId, Long userId, Long tenantId, List<String> recRefList,String appType) throws SQLException, ClassNotFoundException {
		log.info("in approveTask: approveTaskByRole");
		log.info("taskId: " + taskId);
		log.info("batchId: " + batchId+" userId: "+userId);
		String module="";
		ErrorReport report=new ErrorReport();
		report.setTaskName("Approve Task");
		StatusStringDTO statusDto=new StatusStringDTO();
		NotificationBatch notBatch=notificationBatchRepository.findOne(batchId);
		module=notBatch.getModule();
		log.info("notBatch: "+notBatch);
		Long pidOld=notBatch.getProcessInstanceId();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String currentDateTime = dateFormat.format(date);

		RuntimeEngine engine = manager.getRuntimeEngine(null);
		KieSession ksession = engine.getKieSession();
		ksession.getWorkItemManager().registerWorkItemHandler("Email",
				new EmailNotification());
		TaskService taskService = engine.getTaskService();
		Task task = taskService.getTaskById(taskId);
		String taskOwner=task.getTaskData().getActualOwner().toString();
		String taskCreatedBy=task.getTaskData().getCreatedBy().toString();
		String appStatus=null;
		if(appType!=null && appType.equalsIgnoreCase("ImmediateApprovals")){
			appStatus="ApprovedByAdmin";
		}
		else appStatus="Approved";

		if(!(taskOwner.equalsIgnoreCase(userId.toString()))){
			appStatus=appStatus+"("+userId+")";	
		}
		/* Fetching LoggedIn User role */
		//Long uId=0L;

		//HashMap userInfo=approvalProcessService.getUserInfo(userId, tenantId);
		/*if(userInfo!=null){
		uId=Long.parseLong(userInfo.get("user_id").toString());
		}*/

		log.info("recRefList sz: "+recRefList.size());
		String refRefListStr="";
		if(recRefList!=null && !(recRefList.isEmpty())){
			refRefListStr=recRefList.toString();
			refRefListStr=refRefListStr.replace("]", "");
			refRefListStr=refRefListStr.replace("[", "");
			refRefListStr="'"+refRefListStr.replace(" ","").replace(",","','")+"'";
		}
		log.info("refRefListStr: "+refRefListStr);

		Integer I = null;
		if(notBatch.getRefLevel()!=null)
		{
			I=notBatch.getRefLevel();
		}
		String refNum=String.valueOf(I);
		String reassRefNum=null;
		List<Long> recIdsList=new ArrayList<Long>();
		String status="InProcess";
		/*if(!(taskOwner.equalsIgnoreCase(taskCreatedBy))){
			log.info("As taskOwner "+taskOwner+" and taskCreator: "+taskCreatedBy+" are not equal, TaskId: "+taskId+" is an escalated or reassigned Task");
			I=I+2;
			reassRefNum="0"+String.valueOf(I);
			log.info("reassRefNum: "+reassRefNum);
			recIdsList=getRecIdList(notBatch, reassRefNum, refRefListStr,status,module);
		}
    	else*/ 
		//recIdsList=getRecIdList(notBatch, refNum, refRefListStr,status,module);
		//String refNum=String.valueOf(I);
		if(refNum.length()<=1)
			refNum="0"+refNum;
		log.info("refNum :"+refNum);
		List<HashMap> finalMap=new ArrayList<HashMap>();
		recIdsList=getRecIdList(notBatch, refNum, refRefListStr,status,module);
		log.info("recIdsList sz: "+recIdsList.size());
		//	log.info("recIdsList for updating status Approved are: "+recIdsList);

		if(module.equalsIgnoreCase("ACCOUNTING_APPROVALS"))
		{
			AccountingData acctRes=new AccountingData();
			List<AccountingData> acctResList=new ArrayList<AccountingData>();
			log.info("time before looping :"+ZonedDateTime.now());
			for(int j=0;j<recIdsList.size();j++){


				Long recId=recIdsList.get(j);
				AccountingData recRes=accountingDataRepository.findOne(recId);
				String appRefStatus=notBatch.getCurrentApprover()+"|"+notBatch.getId()+"|"+appStatus+"|"+currentDateTime;
				//log.info("appRefStatus: "+appRefStatus);
				String IStr=String.valueOf(I);
				if(IStr.equalsIgnoreCase("1")){
					recRes.setApprRef01(appRefStatus);
				}
				else if(IStr.equalsIgnoreCase("2")){
					recRes.setApprRef02(appRefStatus);
				}
				else if(IStr.equalsIgnoreCase("3")){
					recRes.setApprRef03(appRefStatus);
				}else if(IStr.equalsIgnoreCase("4")){
					recRes.setApprRef04(appRefStatus);
				}else if(IStr.equalsIgnoreCase("5")){
					recRes.setApprRef05(appRefStatus);
				}else if(IStr.equalsIgnoreCase("6")){
					recRes.setApprRef06(appRefStatus);
				}else if(IStr.equalsIgnoreCase("7")){
					recRes.setApprRef07(appRefStatus);
				}else if(IStr.equalsIgnoreCase("8")){
					recRes.setApprRef08(appRefStatus);
				}else if(IStr.equalsIgnoreCase("9")){
					recRes.setApprRef09(appRefStatus);
				}else if(IStr.equalsIgnoreCase("10")){
					recRes.setApprRef10(appRefStatus);
				}else if(IStr.equalsIgnoreCase("11")){
					recRes.setApprRef11(appRefStatus);
				}else if(IStr.equalsIgnoreCase("12")){
					recRes.setApprRef12(appRefStatus);
				}else if(IStr.equalsIgnoreCase("13")){
					recRes.setApprRef13(appRefStatus);
				}else if(IStr.equalsIgnoreCase("14")){
					recRes.setApprRef14(appRefStatus);
				}else if(IStr.equalsIgnoreCase("15")){
					recRes.setApprRef15(appRefStatus);
				}
				recRes.setFinalActionDate(ZonedDateTime.now());
				if(appType!=null && appType.equalsIgnoreCase("ImmediateApprovals")){
					recRes.setFinalStatus("APPROVED");
				}
				//accountingDataRepository.save(recRes);

				acctResList.add(recRes);

			}
			log.info("time after looping :"+ZonedDateTime.now());
			acctResList=accountingDataRepository.save(acctResList);
			log.info("time after updating :"+ZonedDateTime.now());


			log.info("Update accounting data status to approved");
		}
		else if(module.equalsIgnoreCase("JOURNAL_APPROVALS"))
		{
			//AccountingData acctRes=new AccountingData();
			JournalApprovalInfo jeApprovalInfo=new JournalApprovalInfo();
			//List<JournalApprovalInfo> acctResList=new ArrayList<JournalApprovalInfo>();
			List<JournalApprovalInfo> jeApprovalInfoList=new ArrayList<JournalApprovalInfo>();
			log.info("time before looping :"+ZonedDateTime.now());
			for(int j=0;j<recIdsList.size();j++){


				Long recId=recIdsList.get(j);
				JournalApprovalInfo jeAppRes=journalApprovalInfoRepository.findOne(recId);
				String appRefStatus=notBatch.getCurrentApprover()+"|"+notBatch.getId()+"|"+appStatus+"|"+currentDateTime;
				//log.info("appRefStatus: "+appRefStatus);
				String IStr=String.valueOf(I);
				if(IStr.equalsIgnoreCase("1")){
					jeAppRes.setApprRef01(appRefStatus);
				}
				else if(IStr.equalsIgnoreCase("2")){
					jeAppRes.setApprRef02(appRefStatus);
				}
				else if(IStr.equalsIgnoreCase("3")){
					jeAppRes.setApprRef03(appRefStatus);
				}else if(IStr.equalsIgnoreCase("4")){
					jeAppRes.setApprRef04(appRefStatus);
				}else if(IStr.equalsIgnoreCase("5")){
					jeAppRes.setApprRef05(appRefStatus);
				}else if(IStr.equalsIgnoreCase("6")){
					jeAppRes.setApprRef06(appRefStatus);
				}else if(IStr.equalsIgnoreCase("7")){
					jeAppRes.setApprRef07(appRefStatus);
				}else if(IStr.equalsIgnoreCase("8")){
					jeAppRes.setApprRef08(appRefStatus);
				}else if(IStr.equalsIgnoreCase("9")){
					jeAppRes.setApprRef09(appRefStatus);
				}else if(IStr.equalsIgnoreCase("10")){
					jeAppRes.setApprRef10(appRefStatus);
				}else if(IStr.equalsIgnoreCase("11")){
					jeAppRes.setApprRef11(appRefStatus);
				}else if(IStr.equalsIgnoreCase("12")){
					jeAppRes.setApprRef12(appRefStatus);
				}else if(IStr.equalsIgnoreCase("13")){
					jeAppRes.setApprRef13(appRefStatus);
				}else if(IStr.equalsIgnoreCase("14")){
					jeAppRes.setApprRef14(appRefStatus);
				}else if(IStr.equalsIgnoreCase("15")){
					jeAppRes.setApprRef15(appRefStatus);
				}
				jeAppRes.setFinalActionDate(ZonedDateTime.now());
				if(appType!=null && appType.equalsIgnoreCase("ImmediateApprovals")){
					jeAppRes.setFinalStatus("APPROVED");
				}
				//accountingDataRepository.save(recRes);

				jeApprovalInfoList.add(jeAppRes);

			}
			log.info("time after looping :"+ZonedDateTime.now());
			jeApprovalInfoList=journalApprovalInfoRepository.save(jeApprovalInfoList);
			log.info("time after updating :"+ZonedDateTime.now());


			log.info("Update journal Approval data status to approved");
		}
		else{
			List<ReconciliationResult> recResList=new ArrayList<ReconciliationResult>();
			ReconciliationResult recResult=new ReconciliationResult();
			log.info("time befor looping :"+ZonedDateTime.now());
			for(int j=0;j<recIdsList.size();j++){

				Long recId=recIdsList.get(j);
				recResult=reconciliationResultRepository.findOne(recIdsList.get(0));
				ReconciliationResult recRes=reconciliationResultRepository.findOne(recId);
				String appRefStatus=notBatch.getCurrentApprover()+"|"+notBatch.getId()+"|"+appStatus+"|"+currentDateTime;
				//log.info("appRefStatus: "+appRefStatus);
				String IStr=String.valueOf(I);
				if(IStr.equalsIgnoreCase("1")){
					recRes.setApprRef01(appRefStatus);
				}
				else if(IStr.equalsIgnoreCase("2")){
					recRes.setApprRef02(appRefStatus);
				}
				else if(IStr.equalsIgnoreCase("3")){
					recRes.setApprRef03(appRefStatus);
				}else if(IStr.equalsIgnoreCase("4")){
					recRes.setApprRef04(appRefStatus);
				}else if(IStr.equalsIgnoreCase("5")){
					recRes.setApprRef05(appRefStatus);
				}else if(IStr.equalsIgnoreCase("6")){
					recRes.setApprRef06(appRefStatus);
				}else if(IStr.equalsIgnoreCase("7")){
					recRes.setApprRef07(appRefStatus);
				}else if(IStr.equalsIgnoreCase("8")){
					recRes.setApprRef08(appRefStatus);
				}else if(IStr.equalsIgnoreCase("9")){
					recRes.setApprRef09(appRefStatus);
				}else if(IStr.equalsIgnoreCase("10")){
					recRes.setApprRef10(appRefStatus);
				}else if(IStr.equalsIgnoreCase("11")){
					recRes.setApprRef11(appRefStatus);
				}else if(IStr.equalsIgnoreCase("12")){
					recRes.setApprRef12(appRefStatus);
				}else if(IStr.equalsIgnoreCase("13")){
					recRes.setApprRef13(appRefStatus);
				}else if(IStr.equalsIgnoreCase("14")){
					recRes.setApprRef14(appRefStatus);
				}else if(IStr.equalsIgnoreCase("15")){
					recRes.setApprRef15(appRefStatus);
				}
				recRes.setFinalActionDate(ZonedDateTime.now());
				if(appType!=null && appType.equalsIgnoreCase("ImmediateApprovals")){
					recRes.setFinalStatus("APPROVED");
				}
				recResList.add(recRes);
				//reconciliationResultRepository.save(recRes);
			}
			log.info("time after looping :"+ZonedDateTime.now());
			recResList=reconciliationResultRepository.save(recResList);
			log.info("time after updating :"+ZonedDateTime.now());
			log.info("recResList :"+recResList.size());
			log.info("recResult :"+recResult);
			//to update approval count for reconciliation
			if(recResList.get(0).getFinalStatus()!=null && recResList.get(0).getFinalStatus().equalsIgnoreCase("APPROVED")){}
			log.info("update recon appref status to Approved");
		}

		/*RuntimeEngine engine = manager.getRuntimeEngine(null);
    		KieSession ksession = engine.getKieSession();
    		ksession.getWorkItemManager().registerWorkItemHandler("Email",
    				new EmailNotification());
    		TaskService taskService = engine.getTaskService();
    		Task task = taskService.getTaskById(taskId);
    		String taskOwner=task.getTaskData().getActualOwner().toString();*/

		//if(uId!=null && !(uId.toString().equalsIgnoreCase(userId.toString()))){
		if(!(taskOwner.equalsIgnoreCase(userId.toString()))){
			taskService.delegate(taskId, task.getTaskData()
					.getActualOwner().getId(), userId.toString());
			log.info("task: " + taskId + " has been delegated from: "
					+ task.getTaskData().getActualOwner() + " to: "
					+ userId);
		}

		taskService.start(taskId, userId.toString());
		Map<String, Object> results = new HashMap<String, Object>();
		results.put("out_action", "APPROVED");
		results.put("out_fromuser", userId);

		if(appType!=null && appType.equalsIgnoreCase("ImmediateApprovals")){
			results.put("out_adminapprcnt", 100);
			log.info("out_adminapprcnt set to 100");
		}

		boolean lastApprover=false;
		int approversCount=0;
		List approverList=new ArrayList<>();
		Long newNotifBatchId=0L;
		Long newNotifId=0L;
		if(!(appType!=null && appType.equalsIgnoreCase("ImmediateApprovals"))){


			try {

				Map<String, Object> content = ((InternalTaskService) taskService)
						.getTaskContent(taskId);
				log.info("content: "+content);
				approverList = (ArrayList) content.get("in_list");
				if(content.get("owner")!=null){
					String curApp=content.get("owner").toString();
					log.info("curApp: "+curApp);
				}
				approversCount=(int) content.get("in_approverscount");
				if (Integer.valueOf(content.get("in_approverscount").toString()) > 1) {
					log.info("in_approverscount greater than 1: "+content.get("in_approverscount"));

					NotificationBatch notifBatch=new NotificationBatch();
					int refNo=Integer.parseInt(refNum);
					refNo=refNo+1;
					notifBatch.setNotificationName(notBatch.getNotificationName()+"-"+refNo);
					notifBatch.setStatus("IN_PROCESS");
					notifBatch.setCreatedBy(userId);
					notifBatch.setCreatedDate(ZonedDateTime.now());
					notifBatch.setParentBatch(notBatch.getId());
					notifBatch.setApprRef(notBatch.getApprRef()+1);
					int updRefNum=Integer.parseInt(refNum)+1;
					notifBatch.setRefLevel(updRefNum);
					//	List<ApprovalRuleAssignment> appRulAssList=approvalRuleAssignmentRepository.findByRuleId(notBatch.getRuleId());
					//	ApprovalRuleAssignment nextAppAss=appRulAssList.get(notBatch.getApprRef());




					//	notifBatch.setCurrentApprover(currentApprover);
					notifBatch.setRuleGroupId(notBatch.getRuleGroupId());
					notifBatch.setRuleId(notBatch.getRuleId());
					notifBatch.setModule(notBatch.getModule());
					notifBatch.setTenantId(tenantId);
					log.info("pidOld: "+pidOld);
					notifBatch.setProcessInstanceId(pidOld);
					notifBatch.setLastUpdatedBy(userId);
					notifBatch.setLastUpdatedDate(ZonedDateTime.now());
					NotificationBatch newNotifBatch=notificationBatchRepository.save(notifBatch);
					newNotifBatchId=newNotifBatch.getId();
					log.info("newNotifBatch generated with id: "+newNotifBatchId);
					results.put("out_batchId", newNotifBatchId.toString());
					results.put("out_tenantId", tenantId.toString());
					Notifications notification=new Notifications();
					notification.setModule(module);
					if(module.equalsIgnoreCase("ACCOUNTING_APPROVALS")){
						notification.setMessage("Accounting Requires your approval");
					}
					else if(module.equalsIgnoreCase("JOURNAL_APPROVALS"))
					{
						notification.setMessage("journal approval Requires your approval");
					}
					else notification.setMessage("Reconciliation Requires your approval");


					/**check**/
					//	notification.setUserId(currentApprover);
					notification.isViewed(false);
					notification.setActionType("Notification Batch");
					notification.setActionValue(batchId.toString());
					notification.setTenantId(tenantId);
					notification.setCreatedBy(userId);
					notification.setLastUpdatedBy(userId);
					notification.setCreationDate(ZonedDateTime.now());
					notification.setLastUpdatedDate(ZonedDateTime.now());
					log.info("saving notification with : "+notification);
					Notifications newNotif=notificationsRepository.save(notification);
					log.info("saved notification with id: "+newNotif.getId());
					newNotifId=newNotif.getId();
					List<AccountingData> acctListUpdate=new ArrayList<AccountingData>();
					List<JournalApprovalInfo> journalApprovalInfoListUpdate=new ArrayList<JournalApprovalInfo>();
					List<ReconciliationResult> recListUpdate=new ArrayList<ReconciliationResult>();
					log.info("time befor loop of updating in process :"+ZonedDateTime.now());
					//	List<BigInteger> accountedSummaryIds=new ArrayList<BigInteger>();
					/**completing the task**/
					taskService.complete(taskId, userId.toString(), results);

					List<Long> taskIdList = taskService.getTasksByProcessInstanceId(notBatch.getProcessInstanceId());
					log.info("taskIdList :"+taskIdList);
					Long currentApprover=0l;
					for(Long tasksId:taskIdList)
					{
						Task resTask = taskService.getTaskById(tasksId);
						if(resTask.getTaskData().getStatus() != null && resTask.getTaskData().getStatus().toString().equalsIgnoreCase("Reserved") && task.getTaskData().getActualOwner().getId()!=null)
							currentApprover=Long.valueOf(resTask.getTaskData().getActualOwner().getId());

						log.info("currentApprover :"+currentApprover);
					}
					newNotifBatch=notificationBatchRepository.findOne(newNotifBatchId);
					log.info("newBtc :"+newNotifBatch);
					if(newNotifBatch!=null)
					{
						newNotifBatch.setCurrentApprover(currentApprover);
						newNotifBatch=notificationBatchRepository.save(newNotifBatch);
					}

					Notifications newNotification=notificationsRepository.findOne(newNotifId);
					if(newNotification!=null)
					{
						newNotification.setUserId(currentApprover);
						newNotification=notificationsRepository.save(newNotification);
					}


					for(int j=0;j<recIdsList.size();j++){

						Long recId=recIdsList.get(j);

						if(module.equalsIgnoreCase("ACCOUNTING_APPROVALS")){
							AccountingData recRes=accountingDataRepository.findOne(recId);
							//accountedSummaryIds.add(BigInteger.valueOf(recRes.getAccountedSummaryId()));
							recRes.setFinalStatus("IN_PROCESS");
							String appRefStatus=newNotifBatch.getCurrentApprover()+"|"+newNotifBatch.getId()+"|InProcess|"+currentDateTime;
							String updRefNumStr=null;
							/*if(reassRefNum!=null){
        						updRefNumStr=String.valueOf(Integer.parseInt(reassRefNum)+1);
        					}
        					else */
							updRefNumStr=String.valueOf(updRefNum);
							//log.info("updRefNumStr: "+updRefNumStr);
							if(updRefNumStr.equalsIgnoreCase("1")){
								recRes.setApprRef01(appRefStatus);
							}
							else if(updRefNumStr.equalsIgnoreCase("2")){
								recRes.setApprRef02(appRefStatus);
							}
							else if(updRefNumStr.equalsIgnoreCase("3")){
								recRes.setApprRef03(appRefStatus);
							}else if(updRefNumStr.equalsIgnoreCase("4")){
								recRes.setApprRef04(appRefStatus);
							}else if(updRefNumStr.equalsIgnoreCase("5")){
								recRes.setApprRef05(appRefStatus);
							}else if(updRefNumStr.equalsIgnoreCase("6")){
								recRes.setApprRef06(appRefStatus);
							}else if(updRefNumStr.equalsIgnoreCase("7")){
								recRes.setApprRef07(appRefStatus);
							}else if(updRefNumStr.equalsIgnoreCase("8")){
								recRes.setApprRef08(appRefStatus);
							}else if(updRefNumStr.equalsIgnoreCase("9")){
								recRes.setApprRef09(appRefStatus);
							}else if(updRefNumStr.equalsIgnoreCase("10")){
								recRes.setApprRef10(appRefStatus);
							}else if(updRefNumStr.equalsIgnoreCase("11")){
								recRes.setApprRef11(appRefStatus);
							}else if(updRefNumStr.equalsIgnoreCase("12")){
								recRes.setApprRef12(appRefStatus);
							}else if(updRefNumStr.equalsIgnoreCase("13")){
								recRes.setApprRef13(appRefStatus);
							}else if(updRefNumStr.equalsIgnoreCase("14")){
								recRes.setApprRef14(appRefStatus);
							}else if(updRefNumStr.equalsIgnoreCase("15")){
								recRes.setApprRef15(appRefStatus);
							}
							recRes.setFinalActionDate(ZonedDateTime.now());
							//accountingDataRepository.save(recRes);
							acctListUpdate.add(recRes);
						}
						else if (module.equalsIgnoreCase("JOURNAL_APPROVALS"))
						{

							//AccountingData recRes=accountingDataRepository.findOne(recId);
							JournalApprovalInfo journalApprovalInfo=journalApprovalInfoRepository.findOne(recId);
							//	accountedSummaryIds.add(BigInteger.valueOf(recRes.getAccountedSummaryId()));
							journalApprovalInfo.setFinalStatus("IN_PROCESS");
							String appRefStatus=newNotifBatch.getCurrentApprover()+"|"+newNotifBatch.getId()+"|InProcess|"+currentDateTime;
							String updRefNumStr=null;
							/*if(reassRefNum!=null){
        						updRefNumStr=String.valueOf(Integer.parseInt(reassRefNum)+1);
        					}
        					else */
							updRefNumStr=String.valueOf(updRefNum);
							//log.info("updRefNumStr: "+updRefNumStr);
							if(updRefNumStr.equalsIgnoreCase("1")){
								journalApprovalInfo.setApprRef01(appRefStatus);
							}
							else if(updRefNumStr.equalsIgnoreCase("2")){
								journalApprovalInfo.setApprRef02(appRefStatus);
							}
							else if(updRefNumStr.equalsIgnoreCase("3")){
								journalApprovalInfo.setApprRef03(appRefStatus);
							}else if(updRefNumStr.equalsIgnoreCase("4")){
								journalApprovalInfo.setApprRef04(appRefStatus);
							}else if(updRefNumStr.equalsIgnoreCase("5")){
								journalApprovalInfo.setApprRef05(appRefStatus);
							}else if(updRefNumStr.equalsIgnoreCase("6")){
								journalApprovalInfo.setApprRef06(appRefStatus);
							}else if(updRefNumStr.equalsIgnoreCase("7")){
								journalApprovalInfo.setApprRef07(appRefStatus);
							}else if(updRefNumStr.equalsIgnoreCase("8")){
								journalApprovalInfo.setApprRef08(appRefStatus);
							}else if(updRefNumStr.equalsIgnoreCase("9")){
								journalApprovalInfo.setApprRef09(appRefStatus);
							}else if(updRefNumStr.equalsIgnoreCase("10")){
								journalApprovalInfo.setApprRef10(appRefStatus);
							}else if(updRefNumStr.equalsIgnoreCase("11")){
								journalApprovalInfo.setApprRef11(appRefStatus);
							}else if(updRefNumStr.equalsIgnoreCase("12")){
								journalApprovalInfo.setApprRef12(appRefStatus);
							}else if(updRefNumStr.equalsIgnoreCase("13")){
								journalApprovalInfo.setApprRef13(appRefStatus);
							}else if(updRefNumStr.equalsIgnoreCase("14")){
								journalApprovalInfo.setApprRef14(appRefStatus);
							}else if(updRefNumStr.equalsIgnoreCase("15")){
								journalApprovalInfo.setApprRef15(appRefStatus);
							}
							journalApprovalInfo.setFinalActionDate(ZonedDateTime.now());
							//accountingDataRepository.save(recRes);
							journalApprovalInfoListUpdate.add(journalApprovalInfo);

						}
						else{
							ReconciliationResult recRes=reconciliationResultRepository.findOne(recId);
							recRes.setFinalStatus("IN_PROCESS");
							String appRefStatus=newNotifBatch.getCurrentApprover()+"|"+newNotifBatch.getId()+"|InProcess|"+currentDateTime;
							/*String updRefNumStr=String.valueOf(updRefNum);*/

							String updRefNumStr=null;
							if(reassRefNum!=null){
								updRefNumStr=String.valueOf(Integer.parseInt(reassRefNum)+1);
							}
							else 
								updRefNumStr=String.valueOf(updRefNum);
							//log.info("updRefNumStr: "+updRefNumStr);
							if(updRefNumStr.equalsIgnoreCase("1")){
								recRes.setApprRef01(appRefStatus);
							}
							else if(updRefNumStr.equalsIgnoreCase("2")){
								recRes.setApprRef02(appRefStatus);
							}
							else if(updRefNumStr.equalsIgnoreCase("3")){
								recRes.setApprRef03(appRefStatus);
							}else if(updRefNumStr.equalsIgnoreCase("4")){
								recRes.setApprRef04(appRefStatus);
							}else if(updRefNumStr.equalsIgnoreCase("5")){
								recRes.setApprRef05(appRefStatus);
							}else if(updRefNumStr.equalsIgnoreCase("6")){
								recRes.setApprRef06(appRefStatus);
							}else if(updRefNumStr.equalsIgnoreCase("7")){
								recRes.setApprRef07(appRefStatus);
							}else if(updRefNumStr.equalsIgnoreCase("8")){
								recRes.setApprRef08(appRefStatus);
							}else if(updRefNumStr.equalsIgnoreCase("9")){
								recRes.setApprRef09(appRefStatus);
							}else if(updRefNumStr.equalsIgnoreCase("10")){
								recRes.setApprRef10(appRefStatus);
							}else if(updRefNumStr.equalsIgnoreCase("11")){
								recRes.setApprRef11(appRefStatus);
							}else if(updRefNumStr.equalsIgnoreCase("12")){
								recRes.setApprRef12(appRefStatus);
							}else if(updRefNumStr.equalsIgnoreCase("13")){
								recRes.setApprRef13(appRefStatus);
							}else if(updRefNumStr.equalsIgnoreCase("14")){
								recRes.setApprRef14(appRefStatus);
							}else if(updRefNumStr.equalsIgnoreCase("15")){
								recRes.setApprRef15(appRefStatus);
							}
							//recRes.setFinalActionDate(ZonedDateTime.now());
							//reconciliationResultRepository.save(recRes);

							recListUpdate.add(recRes);
						}
					}

					log.info("time after loop of updating in process :"+ZonedDateTime.now());
					if(recListUpdate.size()>0)
						reconciliationResultRepository.save(recListUpdate);
					if(acctListUpdate.size()>0)
						accountingDataRepository.save(acctListUpdate);
					if(journalApprovalInfoListUpdate.size()>0)
						journalApprovalInfoRepository.save(journalApprovalInfoListUpdate);
					log.info("time after updating to inprocess :"+ZonedDateTime.now());
					log.info("updated with next approver status references");

				}
				log.info("Approve Method: Approver Count "
						+ content.get("in_approverscount"));
				if (content.get("in_approverscount").equals(1)) {
					approversCount=(int) content.get("in_approverscount");
					lastApprover = true;
					List inApproverList = (ArrayList) content.get("in_list");
					log.info("inApproverList: "+inApproverList);
					log.info("inApproverList.get(0).toString(): "+inApproverList.get(0));

					inApproverList.remove(0);
					log.info("removed index 0 ");
					log.info("inApproverList aftr remove: "+inApproverList);
					results.put("finalApprover", null);
					/**completing the task**/
					taskService.complete(taskId, userId.toString(), results);
					notBatch.setStatus("APPROVED");
					notificationBatchRepository.save(notBatch);
					/* Updating Final Status as Approved */
					String jobReference="";
					List<BigInteger> rowIds=new ArrayList<BigInteger>();
					rowIds=accountingDataRepository.fetchByIds(recIdsList);
					log.info("rowIds size found from accounted data: "+rowIds.size());
					List<AccountingData> updatingActFinalStatus=new ArrayList<AccountingData>();
					List<ReconciliationResult> updatingRecFinalStatus=new ArrayList<ReconciliationResult>();
					List<JournalApprovalInfo> updateJournalApprovalinfoList=new ArrayList<JournalApprovalInfo>();
					log.info("recIdsList :"+recIdsList);

					for(int j=0;j<recIdsList.size();j++){

						Long recId=recIdsList.get(j);
						if(module.equalsIgnoreCase("ACCOUNTING_APPROVALS")){
							AccountingData recRes=accountingDataRepository.findOne(recId);
							if(j==0){
								jobReference=recRes.getJobReference();
							}
							recRes.setFinalStatus("APPROVED");
							//accountingDataRepository.save(recRes);
							updatingActFinalStatus.add(recRes);
						}
						else if (module.equalsIgnoreCase("JOURNAL_APPROVALS")){
							JournalApprovalInfo recRes=journalApprovalInfoRepository.findOne(recId);
							recRes.setFinalStatus("APPROVED");
							journalApprovalInfoRepository.save(recRes);
							//reconciliationResultRepository.save(recRes);
							updateJournalApprovalinfoList.add(recRes);
						}
						else{
							ReconciliationResult recRes=reconciliationResultRepository.findOne(recId);
							recRes.setFinalStatus("APPROVED");
							//reconciliationResultRepository.save(recRes);
							updatingRecFinalStatus.add(recRes);
						}
					}


					if(module.equalsIgnoreCase("ACCOUNTING_APPROVALS")){
						log.info("Updating final approval status in Accounted Summary");
						List<AccountedSummary> accSummaryList=accountedSummaryRepository.fetchByRowIdsAndJobReference(rowIds, jobReference);
						List<AccountedSummary> finalaccSummaryList=new ArrayList<AccountedSummary>();
						log.info("accSummaryList found based on rowIds and jobReference: "+jobReference+" size: "+accSummaryList.size());
						if(accSummaryList!=null && accSummaryList.size()>0){
							for(int f=0;f<accSummaryList.size();f++){
								AccountedSummary accountedSummary=accSummaryList.get(f);
								AccountedSummary accSummary=accountedSummaryRepository.findOne(accountedSummary.getId());
								accSummary.setApprovalStatus("APPROVED");
								//accountedSummaryRepository.save(accSummary);
								finalaccSummaryList.add(accSummary);
							}
							accountedSummaryRepository.save(finalaccSummaryList);
						}

						log.info("Updated final approval status in accounted summary");
					}
					try
					{
						if(updatingActFinalStatus.size()>0)
						{
							updatingActFinalStatus=accountingDataRepository.save(updatingActFinalStatus);

							String recIdsListStr=recIdsList.toString().replaceAll("\\[", "").replaceAll("\\]", "");
							//to update approval count for accounting
							log.info("acctResList.get(0).getAcctGroupId() :"+updatingActFinalStatus.get(0).getAcctGroupId());
							List<Object[]> ruleIdAndViewIds=accountingDataRepository.fetchDistinctRuleIdAndViewIdByRuleGroupId(updatingActFinalStatus.get(0).getAcctGroupId(),recIdsList);
							log.info("ruleIdAndViewIds :"+ruleIdAndViewIds.size());
							List<java.sql.Date> fileDatesList=appModuleSummaryRepository.findDistinctFileDateByModuleAndRuleGroupId("Accounting",updatingActFinalStatus.get(0).getAcctGroupId());
							log.info("fileDatesList :"+fileDatesList);

							for(int d=0;d<fileDatesList.size();d++)
							{
								for(int i=0;i<ruleIdAndViewIds.size();i++)
								{
									log.info("ruleIdAndViewIds.get(i)[0] :"+ruleIdAndViewIds.get(i)[0]);
									log.info("ruleIdAndViewIds.get(i)[1] :"+ruleIdAndViewIds.get(i)[1]);
									if(ruleIdAndViewIds.get(i)[0]!=null)
									{

										AppModuleSummary checkExisting=appModuleSummaryRepository.findByFileDateAndModuleAndRuleGroupIdAndRuleIdAndViewIdAndType
												(fileDatesList.get(d).toLocalDate(),"Accounting",updatingActFinalStatus.get(0).getAcctGroupId(),Long.valueOf(ruleIdAndViewIds.get(i)[1].toString()),Long.valueOf(ruleIdAndViewIds.get(i)[0].toString()),"Accounted");
										log.info("checkExisting :"+checkExisting);

										String fileDateOrQualifierDate=dashBoardV4Service.getFileDateOrQualifierAndGetColumnName(Long.valueOf(ruleIdAndViewIds.get(i)[0].toString()), updatingActFinalStatus.get(0).getTenantId());


										String finalApprovallistQuery="SELECT originalRowId FROM AccountingData where acctGroupId ="+updatingActFinalStatus.get(0).getId()+" and originalViewId ="+Long.valueOf(ruleIdAndViewIds.get(i)[0].toString())+" and acctRuleId ="+Long.valueOf(ruleIdAndViewIds.get(i)[1].toString())+" and id in ("+recIdsListStr+") and original_row_id in "
												+ "(select id from DataMaster where tenantId ="+updatingActFinalStatus.get(0).getTenantId()+" and Date("+fileDateOrQualifierDate+")='"+fileDatesList.get(d)+"') group by original_row_id";

										//log.info("finalApprovallistQuery :"+finalApprovallistQuery);
										Query exeFinalApprovallistQuery=em.createQuery(finalApprovallistQuery);
										List finalApprovallist=exeFinalApprovallistQuery.getResultList();


										//List<BigInteger> finalApprovallist=accountingDataRepository.fetchActApprovedCount(recIdsList, updatingActFinalStatus.get(0).getAcctGroupId(), Long.valueOf(ruleIdAndViewIds.get(i)[1].toString()), Long.valueOf(ruleIdAndViewIds.get(i)[0].toString()), fileDatesList.get(d).toLocalDate(),tenantId);
										if(checkExisting!=null)
										{
											checkExisting.setApprovalCount(Long.valueOf(finalApprovallist.size()));;
											checkExisting.setLastUpdatedBy(userId);
											checkExisting.setLastUpdatedDate(ZonedDateTime.now());
											appModuleSummaryRepository.save(checkExisting);
										}
									}
								}
							}




						}





						if(updatingRecFinalStatus.size()>0)
						{
							updatingRecFinalStatus=	reconciliationResultRepository.save(updatingRecFinalStatus);
							String recIdsListStr=recIdsList.toString().replaceAll("\\[", "").replaceAll("\\]", "");
							List<Object[]> ruleIdAndViewIds=reconciliationResultRepository.fetchDistinctRuleIdAndViewIdByRuleGroupId(updatingRecFinalStatus.get(0).getReconciliationRuleGroupId(),recIdsList);
							List<java.sql.Date> fileDatesList=appModuleSummaryRepository.findDistinctFileDateByModuleAndRuleGroupId("Reconciliation",updatingRecFinalStatus.get(0).getReconciliationRuleGroupId());
							//  log.info("fileDatesList :"+fileDatesList);
							for(int d=0;d<fileDatesList.size();d++)
							{
								for(int i=0;i<ruleIdAndViewIds.size();i++)
								{
									if(ruleIdAndViewIds.get(i)[0]!=null)
									{



										AppModuleSummary checkExisting=appModuleSummaryRepository.findByFileDateAndModuleAndRuleGroupIdAndRuleIdAndViewIdAndType
												(fileDatesList.get(d).toLocalDate(),"Reconciliation",updatingRecFinalStatus.get(0).getReconciliationRuleGroupId(),Long.valueOf(ruleIdAndViewIds.get(i)[2].toString()),Long.valueOf(ruleIdAndViewIds.get(i)[0].toString()),"Source");
										log.info("checkExisting :"+checkExisting);

										String fileDateOrQualifierDate=dashBoardV4Service.getFileDateOrQualifierAndGetColumnName(Long.valueOf(ruleIdAndViewIds.get(i)[0].toString()), updatingRecFinalStatus.get(0).getTenantId());


										String finalApprovallistQuery="SELECT originalRowId FROM ReconciliationResult where originalRowId is not null and reconciliationRuleGroupId ="+updatingRecFinalStatus.get(0).getReconciliationRuleGroupId()+" and originalViewId ="+Long.valueOf(ruleIdAndViewIds.get(i)[0].toString())+" and reconciliationRuleId ="+Long.valueOf(ruleIdAndViewIds.get(i)[2].toString())+" and id in ("+recIdsListStr+") and originalRowId in "
												+ "(select id from DataMaster where tenantId ="+updatingRecFinalStatus.get(0).getTenantId()+" and Date("+fileDateOrQualifierDate+")='"+fileDatesList.get(d)+"')";






										//log.info("finalApprovallistQuery :"+finalApprovallistQuery);
										Query exeFinalApprovallistQuery=em.createQuery(finalApprovallistQuery);
										List finalApprovallist=exeFinalApprovallistQuery.getResultList();

										log.info("finalApprovallist.size :"+finalApprovallist.size());



										//List<BigInteger> finalApprovallist=reconciliationResultRepository.fetchApprovedCount(recIdsList, updatingRecFinalStatus.get(0).getReconciliationRuleGroupId(), Long.valueOf(ruleIdAndViewIds.get(i)[2].toString()), Long.valueOf(ruleIdAndViewIds.get(i)[0].toString()), fileDatesList.get(d).toLocalDate(),tenantId);
										if(checkExisting!=null)
										{
											//log.info("checkExisting if not null:"+checkExisting);
											checkExisting.setApprovalCount(Long.valueOf(finalApprovallist.size()));;
											checkExisting.setLastUpdatedBy(userId);
											checkExisting.setLastUpdatedDate(ZonedDateTime.now());
											appModuleSummaryRepository.save(checkExisting);
										}
									}
									if(ruleIdAndViewIds.get(i)[1]!=null)
									{

										AppModuleSummary checkExisting=appModuleSummaryRepository.findByFileDateAndModuleAndRuleGroupIdAndRuleIdAndViewIdAndType
												(fileDatesList.get(d).toLocalDate(),"Reconciliation",updatingRecFinalStatus.get(0).getReconciliationRuleGroupId(),Long.valueOf(ruleIdAndViewIds.get(i)[2].toString()),Long.valueOf(ruleIdAndViewIds.get(i)[1].toString()),"Target");
										log.info("checkExisting in target:"+checkExisting);


										String fileDateOrQualifierDate=dashBoardV4Service.getFileDateOrQualifierAndGetColumnName(Long.valueOf(ruleIdAndViewIds.get(i)[1].toString()), updatingRecFinalStatus.get(0).getTenantId());
										log.info("fileDateOrQualifierDate :"+fileDateOrQualifierDate);



										String finalApprovallistQuery="SELECT targetRowId FROM ReconciliationResult where targetRowId is not null and reconciliationRuleGroupId ="+updatingRecFinalStatus.get(0).getReconciliationRuleGroupId()+" and targetViewId ="+Long.valueOf(ruleIdAndViewIds.get(i)[1].toString())+" and reconciliationRuleId ="+Long.valueOf(ruleIdAndViewIds.get(i)[2].toString())+" and id in ("+recIdsListStr+") and targetRowId in "
												+ "(select id from DataMaster where tenantId ="+updatingRecFinalStatus.get(0).getTenantId()+" and Date("+fileDateOrQualifierDate+")='"+fileDatesList.get(d)+"')";



										//log.info("finalApprovallistQuery :"+finalApprovallistQuery);
										Query exeFinalApprovallistQuery=em.createQuery(finalApprovallistQuery);
										List finalApprovallist=exeFinalApprovallistQuery.getResultList();
										log.info("finalApprovallist.size :"+finalApprovallist.size());

										//List<BigInteger> finalApprovallist=reconciliationResultRepository.fetchApprovedCountForTarget(recIdsList, updatingRecFinalStatus.get(0).getReconciliationRuleGroupId(), Long.valueOf(ruleIdAndViewIds.get(i)[2].toString()), Long.valueOf(ruleIdAndViewIds.get(i)[1].toString()), fileDatesList.get(d).toLocalDate(),tenantId);
										if(checkExisting!=null)
										{
											checkExisting.setApprovalCount(Long.valueOf(finalApprovallist.size()));;
											checkExisting.setLastUpdatedBy(userId);
											checkExisting.setLastUpdatedDate(ZonedDateTime.now());
											appModuleSummaryRepository.save(checkExisting);
										}
									}
								}
							}

						}
					}
					catch(Exception e)
					{
						log.info("exception while updating records in app module summary :"+e);
					}



				}
				/** Commented by Swetha Start - For Recon Ref Level Approvals */
				/*else{
    				log.info("approverList bf4 removing: "+approverList);
    				for(int i=0;i<approverList.size();i++){
    					HashMap map=(HashMap) approverList.get(i);
    					log.info("map: "+map);
    					List appInfo=(List) map.get("User");
    					log.info("appInfo: "+appInfo);
    					String approverId= appInfo.get(0).toString();
    					log.info("approverId: "+approverId);
    					if(approverId.equalsIgnoreCase(userId.toString())){
    						approverList.remove(i);
    						log.info("approverList aftr removing: "+approverList);
    					}
    				}
    			}*/
				/** Commented by Swetha Start - For Recon Ref Level Approvals */
			} catch (IllegalStateException e) {
				log.info("The Task Content Needs to be a Map in order to use this method and it was: class java.lang.String");

			}
		}
		else{
			log.info("in immediate approval");
		}
		try {
			log.info("in complete task try");
			//log.info("results :"+results.toString());
			/**************/

			statusDto.setStatus(batchId.toString());
			log.info("task completedddddddddddd");
			log.info("lastApprover: "+lastApprover);
			log.info("Updating invoice status with APPROVED");
			/** Commented by Swetha Start - For Recon Ref Level Approvals */
			/*if(notBatch.getRefLevel()==1){
    					List<Long> recIdsList1=getRecIdList(notBatch, refNum, null ,status,module);
    					log.info("recIdsList1 sz: "+recIdsList1.size());
    					if(recIdsList1.size()>0){
    						log.info("Not all the records in batch: "+notBatch.getId()+" are approved");
    					}
    					else{
    						log.info("all the records in the batch are approved");
    						notBatch.setStatus("APPROVED");
    					}
    				}*/
			/** Commented by Swetha Start - For Recon Ref Level Approvals */
			notBatch.setStatus("APPROVED");
			report.setTaskStatus("Success");
			report.setDetails(batchId.toString());
			notBatch.setLastUpdatedDate(ZonedDateTime.now());
			/** Commented by Swetha end - For Recon Ref Level Approvals */

			/** UnCommented by Swetha Start - For Recon Ref Level Approvals */
			notificationBatchRepository.save(notBatch);
			log.info("Notification status updated to approved");
			/** UnCommented by Swetha Start - For Recon Ref Level Approvals */

			/** Commented by Swetha Start - For Recon Ref Level Approvals */
			/* Initiate New Process with left over approvers */
			/*log.info("approversCount: "+approversCount);
    				 if(approversCount>1){
    					 log.info("in approversCount: "+approversCount+" b4 approval initiation ");
    					 Long pid=approvalProcessService.initiateApprovals(newNotifBatchId,notBatch.getRuleId(),userId,tenantId,approverList);
    					 log.info("approvals initiated with pid: "+pid);
    					 notBatch.setProcessInstanceId(pid);
    				 }
    				 notificationBatchRepository.save(notBatch);
    				 log.info("Notification status updated to approved");	*/ 
			/** Commented by Swetha end - For Recon Ref Level Approvals */
		} catch (Exception e) {
			e.printStackTrace();
			report.setTaskStatus("Failed");
			report.setDetails(batchId.toString());
			log.info("in exception ");
			log.info(e.getMessage());
		} finally {
			if (engine != null) {
				log.info("disposing engine");
				manager.disposeRuntimeEngine(engine);
			}

		}

		return report;

	}
	
	
	/**
	 * Author: Swetha
	 * Api to Reject Task
	 * @param taskId
	 * @param batchId
	 * @param userId
	 * @param tenantId
	 * @param recRefList
	 * @throws SQLException
	 * @throws ClassNotFoundException 
	 */
	public ErrorReport rejectTask(Long taskId,
			Long batchId,Long userId, Long tenantId, List<String> recRefList,String appType) throws SQLException, ClassNotFoundException {
			log.info("in rejectTask: rejectTask");
			log.info("taskId: " + taskId);
			log.info("batchId: " + batchId);
			String module="";
			StatusStringDTO statusDto=new StatusStringDTO();
			ErrorReport report=new ErrorReport();
			report.setTaskName("Reject Task");
			NotificationBatch notBatch=notificationBatchRepository.findOne(batchId);
			module=notBatch.getModule();
			log.info("notBatch: "+notBatch);
			Long pidOld=notBatch.getProcessInstanceId();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			String currentDateTime = dateFormat.format(date);
			
			RuntimeEngine engine = manager.getRuntimeEngine(null);
    		KieSession ksession = engine.getKieSession();
    		ksession.getWorkItemManager().registerWorkItemHandler("Email",
    				new EmailNotification());
    		TaskService taskService = engine.getTaskService();
    		Task task = taskService.getTaskById(taskId);
    		String taskOwner=task.getTaskData().getActualOwner().toString();
    		String appStatus=null;
    		if(appType!=null && appType.equalsIgnoreCase("ImmediateApprovals")){
    			appStatus="RejectedByAdmin";
    		}
    		else appStatus="Rejected";
    		
    		if(!(taskOwner.equalsIgnoreCase(userId.toString()))){
    			appStatus=appStatus+"("+userId+")";	
    		}
    		
    		//HashMap userInfo=approvalProcessService.getUserInfo(userId, tenantId);
    		/*if(userInfo!=null){
    		uId=Long.parseLong(userInfo.get("user_id").toString());
    		}*/
			
			log.info("recRefList sz: "+recRefList.size());
			String refRefListStr="";
			if(recRefList!=null && !(recRefList.isEmpty())){
				refRefListStr=recRefList.toString();
				refRefListStr=refRefListStr.replace("]", "");
				refRefListStr=refRefListStr.replace("[", "");
				refRefListStr="'"+refRefListStr.replace(" ","").replace(",","','")+"'";
			}
			log.info("refRefListStr: "+refRefListStr);
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
	    	String status="InProcess";
	    	List<Long> recIdsList=getRecIdList(notBatch, refNum, refRefListStr,status,module);
	    	log.info("recIdsList sz: "+recIdsList.size());
			log.info("recIdsList for updating status Rejected are: "+recIdsList);
			
			if(module.equalsIgnoreCase("ACCOUNTING_APPROVALS"))
			{
				
				List<AccountingData> acctRejectedRecList=new ArrayList<AccountingData>();
				for(int j=0;j<recIdsList.size();j++){

					
					Long recId=recIdsList.get(j);
					AccountingData recRes=accountingDataRepository.findOne(recId);
					String appRefStatus=notBatch.getCurrentApprover()+"|"+notBatch.getId()+"|"+appStatus+"|"+currentDateTime;
					//log.info("appRefStatus: "+appRefStatus);
					String IStr=String.valueOf(I);
					if(IStr.equalsIgnoreCase("1")){
						recRes.setApprRef01(appRefStatus);
					}
					else if(IStr.equalsIgnoreCase("2")){
						recRes.setApprRef02(appRefStatus);
					}
					else if(IStr.equalsIgnoreCase("3")){
						recRes.setApprRef03(appRefStatus);
					}else if(IStr.equalsIgnoreCase("4")){
						recRes.setApprRef04(appRefStatus);
					}else if(IStr.equalsIgnoreCase("5")){
						recRes.setApprRef05(appRefStatus);
					}else if(IStr.equalsIgnoreCase("6")){
						recRes.setApprRef06(appRefStatus);
					}else if(IStr.equalsIgnoreCase("7")){
						recRes.setApprRef07(appRefStatus);
					}else if(IStr.equalsIgnoreCase("8")){
						recRes.setApprRef08(appRefStatus);
					}else if(IStr.equalsIgnoreCase("9")){
						recRes.setApprRef09(appRefStatus);
					}else if(IStr.equalsIgnoreCase("10")){
						recRes.setApprRef10(appRefStatus);
					}else if(IStr.equalsIgnoreCase("11")){
						recRes.setApprRef11(appRefStatus);
					}else if(IStr.equalsIgnoreCase("12")){
						recRes.setApprRef12(appRefStatus);
					}else if(IStr.equalsIgnoreCase("13")){
						recRes.setApprRef13(appRefStatus);
					}else if(IStr.equalsIgnoreCase("14")){
						recRes.setApprRef14(appRefStatus);
					}else if(IStr.equalsIgnoreCase("15")){
						recRes.setApprRef15(appRefStatus);
					}
					recRes.setFinalStatus("REJECTED");
					recRes.setFinalActionDate(ZonedDateTime.now());
					
					//accountingDataRepository.save(recRes);
					acctRejectedRecList.add(recRes);
				
				}
				acctRejectedRecList=accountingDataRepository.save(acctRejectedRecList);
				log.info("Update accounting data status to Rejected");
				/**updating in app module summary**/
				
				if(acctRejectedRecList.size()>0)
				{
				log.info("acctResList.get(0).getAcctGroupId() :"+acctRejectedRecList.get(0).getAcctGroupId());
				String recIdsListStr=recIdsList.toString().replaceAll("\\[", "").replaceAll("\\]", "");
				List<Object[]> ruleIdAndViewIds=accountingDataRepository.fetchDistinctRuleIdAndViewIdByRuleGroupId(acctRejectedRecList.get(0).getAcctGroupId(),recIdsList);
				log.info("ruleIdAndViewIds :"+ruleIdAndViewIds.size());
				List<java.sql.Date> fileDatesList=appModuleSummaryRepository.findDistinctFileDateByModuleAndRuleGroupId("Accounting",acctRejectedRecList.get(0).getAcctGroupId());
	          //  log.info("fileDatesList :"+fileDatesList);
	           
		    	for(int d=0;d<fileDatesList.size();d++)
		    	{
		    		for(int i=0;i<ruleIdAndViewIds.size();i++)
		    		{
		    			log.info("ruleIdAndViewIds.get(i)[0] :"+ruleIdAndViewIds.get(i)[0]);
		    			log.info("ruleIdAndViewIds.get(i)[1] :"+ruleIdAndViewIds.get(i)[1]);
		    			if(ruleIdAndViewIds.get(i)[0]!=null)
		    			{

		    				AppModuleSummary checkExisting=appModuleSummaryRepository.findByFileDateAndModuleAndRuleGroupIdAndRuleIdAndViewIdAndType
		    						(fileDatesList.get(d).toLocalDate(),"Accounting",acctRejectedRecList.get(0).getAcctGroupId(),Long.valueOf(ruleIdAndViewIds.get(i)[1].toString()),Long.valueOf(ruleIdAndViewIds.get(i)[0].toString()),"Accounted");
		    				log.info("checkExisting :"+checkExisting);
		    				
		    				
		    				
		    				String fileDateOrQualifierDate=dashBoardV4Service.getFileDateOrQualifierAndGetColumnName(Long.valueOf(ruleIdAndViewIds.get(i)[0].toString()), acctRejectedRecList.get(0).getTenantId());
		    				
		    				
		    				String finalApprovallistQuery="SELECT originalRowId FROM AccountingData where acctGroupId ="+acctRejectedRecList.get(0).getId()+" and originalViewId ="+Long.valueOf(ruleIdAndViewIds.get(i)[0].toString())+" and acctRuleId ="+Long.valueOf(ruleIdAndViewIds.get(i)[1].toString())+" and id in ("+recIdsListStr+") and original_row_id in "
		    						+ "(select id from DataMaster where tenantId ="+acctRejectedRecList.get(0).getTenantId()+" and Date("+fileDateOrQualifierDate+")='"+fileDatesList.get(d)+"') group by original_row_id";
		    				
		    			//	log.info("finalApprovallistQuery :"+finalApprovallistQuery);
		    				Query exeFinalApprovallistQuery=em.createQuery(finalApprovallistQuery);
		    				List finalApprovallist=exeFinalApprovallistQuery.getResultList();
		    				
		    				
		    				
		    				
		    			//	List<BigInteger> finalApprovallist=accountingDataRepository.fetchActApprovedCount(recIdsList, acctRejectedRecList.get(0).getAcctGroupId(), Long.valueOf(ruleIdAndViewIds.get(i)[1].toString()), Long.valueOf(ruleIdAndViewIds.get(i)[0].toString()), fileDatesList.get(d).toLocalDate(),tenantId);
		    				if(checkExisting!=null)
		    				{
		    					checkExisting.setApprovalCount(Long.valueOf(finalApprovallist.size()));;
		    					checkExisting.setLastUpdatedBy(userId);
		    					checkExisting.setLastUpdatedDate(ZonedDateTime.now());
		    					appModuleSummaryRepository.save(checkExisting);
		    				}
		    			}
		    		}
		    	}
			}
				
			}
			
			else if(module.equalsIgnoreCase("JOURNAL_APPROVALS"))
			{
				List<JournalApprovalInfo> jornalstRejectedRecList=new ArrayList<JournalApprovalInfo>();
				
				//List<AccountingData> acctRejectedRecList=new ArrayList<AccountingData>();
				for(int j=0;j<recIdsList.size();j++){

					
					Long recId=recIdsList.get(j);
				//	AccountingData recRes=accountingDataRepository.findOne(recId);
					JournalApprovalInfo recRes=journalApprovalInfoRepository.findOne(recId);
					String appRefStatus=notBatch.getCurrentApprover()+"|"+notBatch.getId()+"|"+appStatus+"|"+currentDateTime;
					//log.info("appRefStatus: "+appRefStatus);
					String IStr=String.valueOf(I);
					if(IStr.equalsIgnoreCase("1")){
						recRes.setApprRef01(appRefStatus);
					}
					else if(IStr.equalsIgnoreCase("2")){
						recRes.setApprRef02(appRefStatus);
					}
					else if(IStr.equalsIgnoreCase("3")){
						recRes.setApprRef03(appRefStatus);
					}else if(IStr.equalsIgnoreCase("4")){
						recRes.setApprRef04(appRefStatus);
					}else if(IStr.equalsIgnoreCase("5")){
						recRes.setApprRef05(appRefStatus);
					}else if(IStr.equalsIgnoreCase("6")){
						recRes.setApprRef06(appRefStatus);
					}else if(IStr.equalsIgnoreCase("7")){
						recRes.setApprRef07(appRefStatus);
					}else if(IStr.equalsIgnoreCase("8")){
						recRes.setApprRef08(appRefStatus);
					}else if(IStr.equalsIgnoreCase("9")){
						recRes.setApprRef09(appRefStatus);
					}else if(IStr.equalsIgnoreCase("10")){
						recRes.setApprRef10(appRefStatus);
					}else if(IStr.equalsIgnoreCase("11")){
						recRes.setApprRef11(appRefStatus);
					}else if(IStr.equalsIgnoreCase("12")){
						recRes.setApprRef12(appRefStatus);
					}else if(IStr.equalsIgnoreCase("13")){
						recRes.setApprRef13(appRefStatus);
					}else if(IStr.equalsIgnoreCase("14")){
						recRes.setApprRef14(appRefStatus);
					}else if(IStr.equalsIgnoreCase("15")){
						recRes.setApprRef15(appRefStatus);
					}
					recRes.setFinalStatus("REJECTED");
					recRes.setFinalActionDate(ZonedDateTime.now());
					
					//accountingDataRepository.save(recRes);
					jornalstRejectedRecList.add(recRes);
				
				}
				jornalstRejectedRecList=journalApprovalInfoRepository.save(jornalstRejectedRecList);
				log.info("Update accounting data status to Rejected");
				/**updating in app module summary**/
		
			
			}
			
			else{
				List<ReconciliationResult> recResRejList=new ArrayList<ReconciliationResult>();
			for(int j=0;j<recIdsList.size();j++){
				
				Long recId=recIdsList.get(j);
				ReconciliationResult recRes=reconciliationResultRepository.findOne(recId);
				String appRefStatus=notBatch.getCurrentApprover()+"|"+notBatch.getId()+"|"+appStatus+"|"+currentDateTime;
				//log.info("appRefStatus: "+appRefStatus);
				String IStr=String.valueOf(I);
				if(IStr.equalsIgnoreCase("1")){
					recRes.setApprRef01(appRefStatus);
				}
				else if(IStr.equalsIgnoreCase("2")){
					recRes.setApprRef02(appRefStatus);
				}
				else if(IStr.equalsIgnoreCase("3")){
					recRes.setApprRef03(appRefStatus);
				}else if(IStr.equalsIgnoreCase("4")){
					recRes.setApprRef04(appRefStatus);
				}else if(IStr.equalsIgnoreCase("5")){
					recRes.setApprRef05(appRefStatus);
				}else if(IStr.equalsIgnoreCase("6")){
					recRes.setApprRef06(appRefStatus);
				}else if(IStr.equalsIgnoreCase("7")){
					recRes.setApprRef07(appRefStatus);
				}else if(IStr.equalsIgnoreCase("8")){
					recRes.setApprRef08(appRefStatus);
				}else if(IStr.equalsIgnoreCase("9")){
					recRes.setApprRef09(appRefStatus);
				}else if(refNum.equalsIgnoreCase("10")){
					recRes.setApprRef10(appRefStatus);
				}else if(refNum.equalsIgnoreCase("11")){
					recRes.setApprRef11(appRefStatus);
				}else if(refNum.equalsIgnoreCase("12")){
					recRes.setApprRef12(appRefStatus);
				}else if(refNum.equalsIgnoreCase("13")){
					recRes.setApprRef13(appRefStatus);
				}else if(refNum.equalsIgnoreCase("14")){
					recRes.setApprRef14(appRefStatus);
				}else if(refNum.equalsIgnoreCase("15")){
					recRes.setApprRef15(appRefStatus);
				}
				recRes.setFinalStatus("REJECTED");
				recRes.setFinalActionDate(ZonedDateTime.now());
				//reconciliationResultRepository.save(recRes);
				recResRejList.add(recRes);
			}
			recResRejList=reconciliationResultRepository.save(recResRejList);
			log.info("update recon appref status to Rejected");
			
			
			
			/**updating in appmodule summary**/
			
			try
			{
			if(recResRejList.size()>0)
			{
				String recIdsListStr=recIdsList.toString().replaceAll("\\[", "").replaceAll("\\]", "");
			List<Object[]> ruleIdAndViewIds=reconciliationResultRepository.fetchDistinctRuleIdAndViewIdByRuleGroupId(recResRejList.get(0).getReconciliationRuleGroupId(),recIdsList);
			List<java.sql.Date> fileDatesList=appModuleSummaryRepository.findDistinctFileDateByModuleAndRuleGroupId("Reconciliation",recResRejList.get(0).getReconciliationRuleGroupId());
	        log.info("fileDatesList :"+fileDatesList);
	    	for(int d=0;d<fileDatesList.size();d++)
	    	{
	    		for(int i=0;i<ruleIdAndViewIds.size();i++)
	    		{
	    			if(ruleIdAndViewIds.get(i)[0]!=null)
	    			{

	    				AppModuleSummary checkExisting=appModuleSummaryRepository.findByFileDateAndModuleAndRuleGroupIdAndRuleIdAndViewIdAndType
	    						(fileDatesList.get(d).toLocalDate(),"Reconciliation",recResRejList.get(0).getReconciliationRuleGroupId(),Long.valueOf(ruleIdAndViewIds.get(i)[2].toString()),Long.valueOf(ruleIdAndViewIds.get(i)[0].toString()),"Source");
	    				log.info("checkExisting :"+checkExisting);
	    				
	    				
	    				String fileDateOrQualifierDate=dashBoardV4Service.getFileDateOrQualifierAndGetColumnName(Long.valueOf(ruleIdAndViewIds.get(i)[0].toString()),  recResRejList.get(0).getTenantId());
	    				
	    				
	    				String finalApprovallistQuery="SELECT originalRowId FROM ReconciliationResult where originalRowId is not null and reconciliationRuleGroupId ="+ recResRejList.get(0).getId()+" and originalViewId ="+Long.valueOf(ruleIdAndViewIds.get(i)[0].toString())+" and reconciliationRuleId ="+Long.valueOf(ruleIdAndViewIds.get(i)[2].toString())+" and id in ("+recIdsListStr+") and originalRowId in "
	    						+ "(select id from DataMaster where tenantId ="+ recResRejList.get(0).getTenantId()+" and Date("+fileDateOrQualifierDate+")='"+fileDatesList.get(d)+"')";
	    				
	    				

	    				
	    				
	    				
	    			//	log.info("finalApprovallistQuery :"+finalApprovallistQuery);
	    				Query exeFinalApprovallistQuery=em.createQuery(finalApprovallistQuery);
	    				List finalApprovallist=exeFinalApprovallistQuery.getResultList();
	    				
	    				log.info("finalApprovallist.size :"+finalApprovallist.size());
	    				
	    				
	    				
	    				
	    				
	    				
	    			//	List<BigInteger> finalApprovallist=reconciliationResultRepository.fetchApprovedCount(recIdsList, recResRejList.get(0).getReconciliationRuleGroupId(), Long.valueOf(ruleIdAndViewIds.get(i)[2].toString()), Long.valueOf(ruleIdAndViewIds.get(i)[0].toString()), fileDatesList.get(d).toLocalDate(),tenantId);
	    				if(checkExisting!=null)
	    				{
	    					checkExisting.setApprovalCount(Long.valueOf(finalApprovallist.size()));;
	    					checkExisting.setLastUpdatedBy(userId);
	    					checkExisting.setLastUpdatedDate(ZonedDateTime.now());
	    					appModuleSummaryRepository.save(checkExisting);
	    				}
	    			}
	    			if(ruleIdAndViewIds.get(i)[1]!=null)
	    			{

	    				AppModuleSummary checkExisting=appModuleSummaryRepository.findByFileDateAndModuleAndRuleGroupIdAndRuleIdAndViewIdAndType
	    						(fileDatesList.get(d).toLocalDate(),"Reconciliation",recResRejList.get(0).getReconciliationRuleGroupId(),Long.valueOf(ruleIdAndViewIds.get(i)[2].toString()),Long.valueOf(ruleIdAndViewIds.get(i)[1].toString()),"Target");
	    				log.info("checkExisting in target:"+checkExisting);
	    				
	    				String fileDateOrQualifierDate=dashBoardV4Service.getFileDateOrQualifierAndGetColumnName(Long.valueOf(ruleIdAndViewIds.get(i)[1].toString()), recResRejList.get(0).getTenantId());
	    				log.info("fileDateOrQualifierDate :"+fileDateOrQualifierDate);
	    				
	    				
	    			
	    				String finalApprovallistQuery="SELECT targetRowId FROM ReconciliationResult where targetRowId is not null and reconciliationRuleGroupId ="+recResRejList.get(0).getId()+" and targetViewId ="+Long.valueOf(ruleIdAndViewIds.get(i)[1].toString())+" and reconciliationRuleId ="+Long.valueOf(ruleIdAndViewIds.get(i)[2].toString())+" and id in ("+recIdsListStr+") and targetRowId in "
	    						+ "(select id from DataMaster where tenantId ="+recResRejList.get(0).getTenantId()+" and Date("+fileDateOrQualifierDate+")='"+fileDatesList.get(d)+"')";
	    				
	    			
	    				
	    			//	log.info("finalApprovallistQuery :"+finalApprovallistQuery);
	    				Query exeFinalApprovallistQuery=em.createQuery(finalApprovallistQuery);
	    				List finalApprovallist=exeFinalApprovallistQuery.getResultList();
	    				log.info("finalApprovallist.size :"+finalApprovallist.size());
	    				
	    				//List<BigInteger> finalApprovallist=reconciliationResultRepository.fetchApprovedCountForTarget(recIdsList, recResRejList.get(0).getReconciliationRuleGroupId(), Long.valueOf(ruleIdAndViewIds.get(i)[2].toString()), Long.valueOf(ruleIdAndViewIds.get(i)[1].toString()), fileDatesList.get(d).toLocalDate(),tenantId);
	    				if(checkExisting!=null)
	    				{
	    					checkExisting.setApprovalCount(Long.valueOf(finalApprovallist.size()));;
	    					checkExisting.setLastUpdatedBy(userId);
	    					checkExisting.setLastUpdatedDate(ZonedDateTime.now());
	    					appModuleSummaryRepository.save(checkExisting);
	    				}
	    			}
	    		}
	    	}
			}
			}
			catch(Exception e)
			{
				log.info("exception while updating records in app module summary :"+e);
			}
			
			
			
			
			}
	    		if(!(taskOwner.equalsIgnoreCase(userId.toString()))){
	    			taskService.delegate(taskId, task.getTaskData()
							.getActualOwner().getId(), userId.toString());
					log.info("task: " + taskId + " has been delegated from: "
							+ task.getTaskData().getActualOwner() + " to: "
							+ userId);
	    		}
	    		
	    		taskService.start(taskId, userId.toString());
	    		Map<String, Object> content = ((InternalTaskService) taskService)
						.getTaskContent(taskId);
	    		Map<String, Object> results = new HashMap<String, Object>();
	    		results.put("out_action", "REJECTED");
	    		results.put("out_fromuser", userId);
	    		try {
	    			log.info("in complete task try");
	    			taskService.complete(taskId, userId.toString(), results);
	    			log.info("task completedddddddddddd");
	    			statusDto.setStatus(batchId.toString());
	    				log.info("Updating invoice status with REJECTED");
	    				 notBatch.setStatus("REJECTED");
	    				 report.setTaskStatus("Success");
	    				 report.setDetails(batchId.toString());
	    				 notBatch.setLastUpdatedDate(ZonedDateTime.now());
	    				 notificationBatchRepository.save(notBatch);
	    				 log.info("Notification status updated to REJECTED");
	    		} catch (Exception e) {
	    			e.printStackTrace();
	    			 report.setTaskStatus("Failed");
					 report.setDetails(batchId.toString());
	    			log.info("in exception ");
	    			log.info(e.getMessage());
	    		} finally {
	    			if (engine != null) {
	    				log.info("disposing engine");
	    				manager.disposeRuntimeEngine(engine);
	    			}
	    		
	    	}
	    		
				return report;
				
	}
	
	/**
	 * Author: Swetha
	 * Function to retrieve list or reconciliationId's to update their status
	 * @param notBatch
	 * @param refNum
	 * @param refRefListStr
	 * @param status
	 * @return
	 * @throws SQLException
	 */
	public List getRecIdList(NotificationBatch notBatch, String refNum, String refRefListStr,String status,String module) throws SQLException{
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
    	ResultSet result2=null;
    	List<Long> recIdsList=new ArrayList<Long>();
    	try{
    	conn = DriverManager.getConnection(dbUrl, userName, password);
    	log.info("Connected database successfully...");
    	stmt = conn.createStatement();
    	status="InProcess";
    	String batchQuery="";
    		String userAndBatchId =notBatch.getCurrentApprover()+"|"+notBatch.getId()+"|"+status;
    		log.info("userAndBatchId :"+userAndBatchId);
    		if(refRefListStr!=null && !(refRefListStr.isEmpty())){
    			if(module.equalsIgnoreCase("ACCOUNTING_APPROVALS")){
    				batchQuery="select id from "+schemaName+".t_accounting_data where appr_ref_"+refNum+" LIKE '"+userAndBatchId+"%' and job_reference in ("+refRefListStr+")";
    			}
    			else{
    				batchQuery="select id from "+schemaName+".t_reconciliation_result where appr_ref_"+refNum+" LIKE '"+userAndBatchId+"%' and recon_reference in ("+refRefListStr+")";
    			}
    		}
    		else{
    			if(module.equalsIgnoreCase("ACCOUNTING_APPROVALS")){
    				batchQuery="select id from "+schemaName+".t_accounting_data where appr_ref_"+refNum+" LIKE '"+userAndBatchId+"%'";
    			}
    			else if(module.equalsIgnoreCase("JOURNAL_APPROVALS")){
    				batchQuery="select id from "+schemaName+".t_journal_approval_info where appr_ref_"+refNum+" LIKE '"+userAndBatchId+"%'";
    			}
    			else{
    			batchQuery="select id from "+schemaName+".t_reconciliation_result where appr_ref_"+refNum+" LIKE '"+userAndBatchId+"%'";
    			}
    		}
    		log.info("batchQuery: "+batchQuery);
    		result2=stmt.executeQuery(batchQuery);
    	}catch(SQLException se){
    		   log.info("se: "+se);
    	  }
    	while(result2!=null && result2.next()){
    	   	 HashMap<String,String> map2=new HashMap<String,String>();
    	   		 Long recId=result2.getLong(1);
    	   		recIdsList.add(recId);
    	   	 }
    	result2.close();
		stmt.close();
		conn.close();
		
		return recIdsList;
	}
	
	/**
	 * Author: Swetha
	 * Api to ReAssign Task
	 * @param taskId
	 * @param batchId
	 * @param userId
	 * @param tenantId
	 * @param recRefList
	 * @throws SQLException
	 */
	public ErrorReport reAssignTask(Long taskId,
			Long batchId, Long userId, Long tenantId, List<String> recRefList,Long assignTo) throws SQLException {
		log.info("in reAssignTask");
		log.info("taskId: " + taskId);
		log.info("batchId: " + batchId+" assignTo: "+assignTo);
		String module="";
		ErrorReport report=new ErrorReport();
		report.setTaskName("Reassign Task");
		StatusStringDTO statusDto=new StatusStringDTO();
		NotificationBatch notBatch=notificationBatchRepository.findOne(batchId);
		module=notBatch.getModule();
		log.info("notBatch: "+notBatch);
		Long pidOld=notBatch.getProcessInstanceId();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String currentDateTime = dateFormat.format(date);
		
		log.info("recRefList sz: "+recRefList.size());
		String refRefListStr="";
		if(recRefList!=null && !(recRefList.isEmpty())){
			refRefListStr=recRefList.toString();
			refRefListStr=refRefListStr.replace("]", "");
			refRefListStr=refRefListStr.replace("[", "");
			refRefListStr="'"+refRefListStr.replace(" ","").replace(",","','")+"'";
		}
		log.info("refRefListStr: "+refRefListStr);
		
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
    	String status="InProcess";
    	List<Long> recIdsList=getRecIdList(notBatch, refNum, refRefListStr,status,module);
    	log.info("recIdsList sz: "+recIdsList.size());
		log.info("recIdsList for updating status ReAssigned are: "+recIdsList);
		
		int updRefNum=Integer.parseInt(refNum);
		String apprStatus="ReAssigned";
		
    		RuntimeEngine engine = manager.getRuntimeEngine(null);
    		KieSession ksession = engine.getKieSession();
    		TaskService taskService = engine.getTaskService();
    		Map<String, Object> trxFieldMap = new HashMap<String, Object>();
    		trxFieldMap.put("reassignee", assignTo);
    		Task task = taskService.getTaskById(taskId);
    		if (task.getTaskData().getActualOwner() != null && !(task.getTaskData().getActualOwner().toString().equalsIgnoreCase(userId.toString()))) {
				taskService.delegate(taskId, task.getTaskData()
						.getActualOwner().getId(),userId.toString());
				log.info("task: " + taskId + " has been delegated from: "
						+ task.getTaskData().getActualOwner() + " to: "
						+userId );
				taskService.delegate(taskId, userId.toString(), assignTo.toString());
				log.info("task: " + taskId + " has been delegated from: "
						+ userId.toString() + " to: " + assignTo);
				apprStatus="ReAssigned("+userId+")";
    		}
    		else{
				taskService.delegate(taskId, userId.toString(), assignTo.toString());
				log.info("task: " + taskId + " has been delegated from: "
						+ userId.toString() + " to: " + assignTo);

			}
    		notBatch.setRefLevel(I+1);
    		notBatch.setCurrentApprover(Long.parseLong(assignTo.toString()));
    		notBatch.setStatus("IN_PROCESS");
    		notBatch.setLastUpdatedDate(ZonedDateTime.now());
    		notificationBatchRepository.save(notBatch);
    		
    			//if (Integer.valueOf(content.get("in_approverscount").toString()) > 1) {
    				Notifications notification=new Notifications();
    				notification.setModule(module);
    				if(module.equalsIgnoreCase("ACCOUNTING_APPROVALS")){
    					notification.setMessage("Accounting Requires your approval");
    				}
    				else notification.setMessage("Reconciliation Requires your approval");
    				notification.setUserId(Long.parseLong(assignTo.toString()));
    				notification.isViewed(false);
    				notification.setActionType("Notification Batch");
    				notification.setActionValue(batchId.toString());
    				notification.setTenantId(tenantId);
    				notification.setCreatedBy(userId);
    				notification.setLastUpdatedBy(userId);
    				notification.setCreationDate(ZonedDateTime.now());
    				notification.setLastUpdatedDate(ZonedDateTime.now());
    				log.info("saving notification with : "+notification);
    				Notifications newNotif=notificationsRepository.save(notification);
    				log.info("saved notification with id: "+newNotif.getId());
    				
    				for(int j=0;j<recIdsList.size();j++){
    					
    					Long recId=recIdsList.get(j);
    					if(module.equalsIgnoreCase("ACCOUNTING_APPROVALS")){
    						AccountingData recRes=accountingDataRepository.findOne(recId);
    						recRes.setFinalStatus("IN_PROCESS");
    						String reAssApprRefStatus=userId+"|"+notBatch.getId()+"|"+apprStatus+"|"+currentDateTime;
    						String appRefStatus=assignTo+"|"+notBatch.getId()+"|InProcess|"+currentDateTime;
    						String updRefNumStr=String.valueOf(updRefNum);
    						//log.info("updRefNumStr: "+updRefNumStr);
    						if(updRefNumStr.equalsIgnoreCase("1")){
    							recRes.setApprRef01(reAssApprRefStatus);
    							recRes.setApprRef02(appRefStatus);
    						}
    						else if(updRefNumStr.equalsIgnoreCase("2")){
    							recRes.setApprRef03(appRefStatus);
    							recRes.setApprRef02(reAssApprRefStatus);
    						}
    						else if(updRefNumStr.equalsIgnoreCase("3")){
    							recRes.setApprRef04(appRefStatus);
    							recRes.setApprRef03(reAssApprRefStatus);
    						}else if(updRefNumStr.equalsIgnoreCase("4")){
    							recRes.setApprRef05(appRefStatus);
    							recRes.setApprRef04(reAssApprRefStatus);
    						}else if(updRefNumStr.equalsIgnoreCase("5")){
    							recRes.setApprRef06(appRefStatus);
    							recRes.setApprRef05(reAssApprRefStatus);
    						}else if(updRefNumStr.equalsIgnoreCase("6")){
    							recRes.setApprRef07(appRefStatus);
    							recRes.setApprRef06(reAssApprRefStatus);
    						}else if(updRefNumStr.equalsIgnoreCase("7")){
    							recRes.setApprRef08(appRefStatus);
    							recRes.setApprRef07(reAssApprRefStatus);
    						}else if(updRefNumStr.equalsIgnoreCase("8")){
    							recRes.setApprRef09(appRefStatus);
    							recRes.setApprRef08(reAssApprRefStatus);
    						}else if(updRefNumStr.equalsIgnoreCase("9")){
    							recRes.setApprRef10(appRefStatus);
    							recRes.setApprRef09(reAssApprRefStatus);
    						}else if(updRefNumStr.equalsIgnoreCase("10")){
    							recRes.setApprRef11(appRefStatus);
    							recRes.setApprRef10(reAssApprRefStatus);
    						}else if(updRefNumStr.equalsIgnoreCase("11")){
    							recRes.setApprRef12(appRefStatus);
    							recRes.setApprRef11(reAssApprRefStatus);
    						}else if(updRefNumStr.equalsIgnoreCase("12")){
    							recRes.setApprRef13(appRefStatus);
    							recRes.setApprRef12(reAssApprRefStatus);
    						}else if(updRefNumStr.equalsIgnoreCase("13")){
    							recRes.setApprRef14(appRefStatus);
    							recRes.setApprRef13(reAssApprRefStatus);
    						}else if(updRefNumStr.equalsIgnoreCase("14")){
    							recRes.setApprRef15(appRefStatus);
    							recRes.setApprRef14(reAssApprRefStatus);
    						}else if(updRefNumStr.equalsIgnoreCase("15")){
    							recRes.setApprRef15(appRefStatus);
    						}
    						//recRes.setFinalActionDate(ZonedDateTime.now());
    						accountingDataRepository.save(recRes);
    					}
    					else if (module.equalsIgnoreCase("JOURNAL_APPROVALS"))
    					{

    						JournalApprovalInfo recRes=journalApprovalInfoRepository.findOne(recId);
    						recRes.setFinalStatus("IN_PROCESS");
    						String reAssApprRefStatus=userId+"|"+notBatch.getId()+"|"+apprStatus+"|"+currentDateTime;
    						String appRefStatus=assignTo+"|"+notBatch.getId()+"|InProcess|"+currentDateTime;
    						String updRefNumStr=String.valueOf(updRefNum);
    						//log.info("updRefNumStr: "+updRefNumStr);
    						if(updRefNumStr.equalsIgnoreCase("1")){
    							recRes.setApprRef01(reAssApprRefStatus);
    							recRes.setApprRef02(appRefStatus);
    						}
    						else if(updRefNumStr.equalsIgnoreCase("2")){
    							recRes.setApprRef03(appRefStatus);
    							recRes.setApprRef02(reAssApprRefStatus);
    						}
    						else if(updRefNumStr.equalsIgnoreCase("3")){
    							recRes.setApprRef04(appRefStatus);
    							recRes.setApprRef03(reAssApprRefStatus);
    						}else if(updRefNumStr.equalsIgnoreCase("4")){
    							recRes.setApprRef05(appRefStatus);
    							recRes.setApprRef04(reAssApprRefStatus);
    						}else if(updRefNumStr.equalsIgnoreCase("5")){
    							recRes.setApprRef06(appRefStatus);
    							recRes.setApprRef05(reAssApprRefStatus);
    						}else if(updRefNumStr.equalsIgnoreCase("6")){
    							recRes.setApprRef07(appRefStatus);
    							recRes.setApprRef06(reAssApprRefStatus);
    						}else if(updRefNumStr.equalsIgnoreCase("7")){
    							recRes.setApprRef08(appRefStatus);
    							recRes.setApprRef07(reAssApprRefStatus);
    						}else if(updRefNumStr.equalsIgnoreCase("8")){
    							recRes.setApprRef09(appRefStatus);
    							recRes.setApprRef08(reAssApprRefStatus);
    						}else if(updRefNumStr.equalsIgnoreCase("9")){
    							recRes.setApprRef10(appRefStatus);
    							recRes.setApprRef09(reAssApprRefStatus);
    						}else if(updRefNumStr.equalsIgnoreCase("10")){
    							recRes.setApprRef11(appRefStatus);
    							recRes.setApprRef10(reAssApprRefStatus);
    						}else if(updRefNumStr.equalsIgnoreCase("11")){
    							recRes.setApprRef12(appRefStatus);
    							recRes.setApprRef11(reAssApprRefStatus);
    						}else if(updRefNumStr.equalsIgnoreCase("12")){
    							recRes.setApprRef13(appRefStatus);
    							recRes.setApprRef12(reAssApprRefStatus);
    						}else if(updRefNumStr.equalsIgnoreCase("13")){
    							recRes.setApprRef14(appRefStatus);
    							recRes.setApprRef13(reAssApprRefStatus);
    						}else if(updRefNumStr.equalsIgnoreCase("14")){
    							recRes.setApprRef15(appRefStatus);
    							recRes.setApprRef14(reAssApprRefStatus);
    						}else if(updRefNumStr.equalsIgnoreCase("15")){
    							recRes.setApprRef15(appRefStatus);
    						}
    						recRes.setFinalActionDate(ZonedDateTime.now());
    						journalApprovalInfoRepository.save(recRes);
    					
    					}
    					else{
    						ReconciliationResult recRes=reconciliationResultRepository.findOne(recId);
    					recRes.setFinalStatus("IN_PROCESS");
    					String reAssApprRefStatus=userId+"|"+notBatch.getId()+"|"+apprStatus+"|"+currentDateTime;
    					String appRefStatus=notBatch.getCurrentApprover()+"|"+notBatch.getId()+"|InProcess|"+currentDateTime;
    					String updRefNumStr=String.valueOf(updRefNum);
    					//log.info("updRefNumStr: "+updRefNumStr);
    					if(updRefNumStr.equalsIgnoreCase("1")){
    						recRes.setApprRef01(reAssApprRefStatus);
    						recRes.setApprRef02(appRefStatus);
    					}
    					else if(updRefNumStr.equalsIgnoreCase("2")){
    						recRes.setApprRef03(appRefStatus);
    						recRes.setApprRef02(reAssApprRefStatus);
    					}
    					else if(updRefNumStr.equalsIgnoreCase("3")){
    						recRes.setApprRef04(appRefStatus);
    						recRes.setApprRef03(reAssApprRefStatus);
    					}else if(updRefNumStr.equalsIgnoreCase("4")){
    						recRes.setApprRef05(appRefStatus);
    						recRes.setApprRef04(reAssApprRefStatus);
    					}else if(updRefNumStr.equalsIgnoreCase("5")){
    						recRes.setApprRef06(appRefStatus);
    						recRes.setApprRef05(reAssApprRefStatus);
    					}else if(updRefNumStr.equalsIgnoreCase("6")){
    						recRes.setApprRef07(appRefStatus);
    						recRes.setApprRef06(reAssApprRefStatus);
    					}else if(updRefNumStr.equalsIgnoreCase("7")){
    						recRes.setApprRef08(appRefStatus);
    						recRes.setApprRef07(reAssApprRefStatus);
    					}else if(updRefNumStr.equalsIgnoreCase("8")){
    						recRes.setApprRef09(appRefStatus);
    						recRes.setApprRef08(reAssApprRefStatus);
    					}else if(updRefNumStr.equalsIgnoreCase("9")){
    						recRes.setApprRef10(appRefStatus);
    						recRes.setApprRef09(reAssApprRefStatus);
    					}else if(updRefNumStr.equalsIgnoreCase("10")){
    						recRes.setApprRef11(appRefStatus);
    						recRes.setApprRef10(reAssApprRefStatus);
    					}else if(updRefNumStr.equalsIgnoreCase("11")){
    						recRes.setApprRef12(appRefStatus);
    						recRes.setApprRef11(reAssApprRefStatus);
    					}else if(updRefNumStr.equalsIgnoreCase("12")){
    						recRes.setApprRef13(appRefStatus);
    						recRes.setApprRef12(reAssApprRefStatus);
    					}else if(updRefNumStr.equalsIgnoreCase("13")){
    						recRes.setApprRef14(appRefStatus);
    						recRes.setApprRef13(reAssApprRefStatus);
    					}else if(updRefNumStr.equalsIgnoreCase("14")){
    						recRes.setApprRef15(appRefStatus);
    						recRes.setApprRef14(reAssApprRefStatus);
    					}else if(updRefNumStr.equalsIgnoreCase("15")){
    						recRes.setApprRef15(reAssApprRefStatus);
    					}
    				//	recRes.setFinalActionDate(ZonedDateTime.now());
    					reconciliationResultRepository.save(recRes);
    					}
    				}
    				log.info("updated with next approver status references");
    		
    			if (engine != null) {
    				log.info("disposing engine");
    				manager.disposeRuntimeEngine(engine);
    			}
    		
			return report;

	}

}
