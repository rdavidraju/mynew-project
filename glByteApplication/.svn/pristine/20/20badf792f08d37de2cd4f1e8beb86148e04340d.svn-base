package com.nspl.app.service;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.nspl.app.config.ApplicationContextProvider;
import com.nspl.app.domain.AppRuleConditions;
import com.nspl.app.domain.ApprovalRuleAssignment;
import com.nspl.app.domain.DataViews;
import com.nspl.app.domain.DataViewsColumns;
import com.nspl.app.domain.FileTemplates;
import com.nspl.app.domain.LookUpCode;
import com.nspl.app.domain.RuleGroup;
import com.nspl.app.domain.RuleGroupDetails;
import com.nspl.app.domain.Rules;
import com.nspl.app.repository.AppRuleConditionsRepository;
import com.nspl.app.repository.ApprovalRuleAssignmentRepository;
import com.nspl.app.repository.DataStagingRepository;
import com.nspl.app.repository.DataViewsColumnsRepository;
import com.nspl.app.repository.DataViewsRepository;
import com.nspl.app.repository.DataViewsSrcMappingsRepository;
import com.nspl.app.repository.LookUpCodeRepository;
import com.nspl.app.repository.RuleGroupDetailsRepository;
import com.nspl.app.repository.RuleGroupRepository;
import com.nspl.app.repository.RulesRepository;
import com.nspl.app.repository.search.RuleGroupSearchRepository;
import com.nspl.app.security.IDORUtils;
import com.nspl.app.web.rest.AppModuleSummaryResource;
import com.nspl.app.web.rest.AppRuleCondDto;
import com.nspl.app.web.rest.dto.AppRuleCondAndActDto;
import com.nspl.app.web.rest.dto.ApprRuleAssgnDto;
import com.nspl.app.web.rest.dto.ApprovalActionDto;
import com.nspl.app.web.rest.dto.ApprovalRuleDto;
import com.nspl.app.web.rest.dto.ErrorReport;
import com.nspl.app.web.rest.dto.RuleGroupDTO;
import com.nspl.app.web.rest.dto.RulesDTO;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.URI;
import java.security.PrivilegedAction;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.security.UserGroupInformation;

import scala.util.control.Exception;


@Service
public class RuleGroupService {
	private final Logger log = LoggerFactory.getLogger(RuleGroupService.class);
	
	
	@Inject
	RuleGroupRepository ruleGroupRepository;
	
	@Inject 
	RuleGroupDetailsRepository ruleGroupDetailsRepository;
	
	@Inject
	RuleService ruleService;
	
	 @Inject
	OozieService oozieService;
	 
	 @Inject
	 private Environment env;
	 
	 @Inject
	 RulesRepository rulesRepository;
	 
	 @Inject
	 DataStagingRepository dataStagingRepository;
	 
	 @Inject
	 DataViewsSrcMappingsRepository dataViewsSrcMappingsRepository;
	 
	 @Inject
	 RuleGroupSearchRepository ruleGroupSearchRepository;
	 
	 @Inject
	 AppModuleSummaryResource appModuleSummaryResource;
	 
	 @Inject
	 UserJdbcService userJdbcService;
	 
	 @Inject
	 ApprovalRuleAssignmentRepository approvalRuleAssignmentRepository;
	 
	 @Inject
	 DataViewsRepository dataViewsRepository;


	 @Inject
	 DataViewsColumnsRepository dataViewsColumnsRepository;


	@Inject
	LookUpCodeRepository lookUpCodeRepository;


	@Inject
	AppRuleConditionsRepository appRuleConditionsRepository;
	 
	@Transactional(propagation= Propagation.REQUIRED)
	public ErrorReport postRulegroup(RuleGroupDTO ruleGroupDTO,HttpServletRequest request,String bulkUpload)
	{
		log.info("Rest request to post rulegroup");
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		log.info("rg save step-1");
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		Long userId=Long.parseLong(map.get("userId").toString());
		String errorMessage = "Recon process save failed while accessing process details";
    	ErrorReport errorReports=new ErrorReport();
		RuleGroup ruleGroup=new RuleGroup();
		log.info("rg save step-2");
    	if(ruleGroupDTO.getId() != null)
    	{
    		log.info("group already exists so edit");
    		
    		if(ruleGroupDTO.getId() !=null)
			{
    			log.info("ruleGroupDTO.getId()"+ruleGroupDTO.getId());
				RuleGroup rg = ruleGroupRepository.findByIdForDisplayAndTenantId(ruleGroupDTO.getId(), tenantId);
				if(rg != null && rg.getId() != null)
				{
					log.info("rg is not null and display value is"+rg.getIdForDisplay());
					ruleGroup.setId(rg.getId());
					ruleGroup.setIdForDisplay(ruleGroupDTO.getId());
					if(rg.getApprRuleGrpId() != null){
						ruleGroup.setApprRuleGrpId(rg.getApprRuleGrpId() );
					}
				}
				else
				{
					log.info("rg is null ooops");
				}
				
			}
    	}
    	else
    	{
    		log.info("rg save step-3");
    	}
    	ErrorReport emptyRuleGroup = new ErrorReport();
    	emptyRuleGroup.setTaskName("Recon process failed to save");
    	
    	if(ruleGroupDTO.getName() != null )
    	{
    		log.info("rg save step-4");
    		ErrorReport duplicateRuleGroupName = new ErrorReport();
    		errorMessage = "Check if recon process with name "+ruleGroupDTO.getName()+" already exists";
    		duplicateRuleGroupName.setTaskName("Check if recon process with name "+ruleGroupDTO.getName()+" already exists");
    		RuleGroup dupRuleGroup = new RuleGroup();
    		dupRuleGroup = ruleGroupRepository.findByTenantIdAndName(tenantId, ruleGroupDTO.getName());
    		log.info("rg save step-5");
    		if(ruleGroupDTO.getId() == null)
    		{
    			log.info("rg save step-6");
    			if(dupRuleGroup != null && dupRuleGroup.getId() != null)
    			{
    				log.info("rg save step-7");
    				duplicateRuleGroupName.setTaskStatus("Failed");
    				errorReports= (duplicateRuleGroupName);
    				throw new RuntimeException(errorMessage);
    			}
    			log.info("rg save step-8");
    		}
    		else
    		{
    			log.info("rg save step-9");
    			RuleGroup existingRG = new RuleGroup();
    			existingRG = ruleGroupRepository.findByIdForDisplayAndTenantId(ruleGroupDTO.getId(), tenantId);
    			if(existingRG != null && existingRG.getId() != null)
    			{
    				log.info("rg save step-10");
    				ruleGroup.setId(existingRG.getId());
    			}
    		}

    		if(ruleGroupDTO.getName()!=null && !ruleGroupDTO.getName().isEmpty())
    			ruleGroup.setName(ruleGroupDTO.getName());
    		if(ruleGroupDTO.getRulePurpose() != null && !ruleGroupDTO.getRulePurpose().isEmpty())
    			ruleGroup.setRulePurpose(ruleGroupDTO.getRulePurpose());
    		else
    		{
    			ruleGroup.setRulePurpose("RECONCILIATION");
    		}
    		if(bulkUpload != null && bulkUpload.equalsIgnoreCase("true"))
    		{
    			ruleGroup.setStartDate(ZonedDateTime.now());
    		}
    		else
    		{
    			if(ruleGroupDTO.getStartDate()!=null)
    				ruleGroup.setStartDate(ruleGroupDTO.getStartDate());
    			if(ruleGroupDTO.getEndDate()!=null)
    				ruleGroup.setEndDate(ruleGroupDTO.getEndDate());
    		}

    		if(bulkUpload != null && bulkUpload.equalsIgnoreCase("true"))
    		{
    			ruleGroup.setEnabledFlag(true);
    		}
    		else
    			ruleGroup.setEnabledFlag(ruleGroupDTO.getEnableFlag());

    		if(ruleGroupDTO != null && ruleGroupDTO.getTenantId() != null)
    		{
    			tenantId = ruleGroupDTO.getTenantId();
    		}
    		if(ruleGroupDTO != null && ruleGroupDTO.getCreatedBy() != null)
    		{
    			userId = ruleGroupDTO.getCreatedBy();
    		}
    		if(ruleGroupDTO.getId() == null || ruleGroupDTO.getId().equals(""))
    		{

    			ruleGroup.setCreationDate(ZonedDateTime.now());
    		}
    		else
    			ruleGroup.setCreationDate(ruleGroupDTO.getCreationDate());
    		if(ruleGroupDTO.getId() != null)
    			ruleGroup.setCreatedBy(ruleGroupDTO.getCreatedBy());
    		else
    			ruleGroup.setCreatedBy(userId);
    		ruleGroup.setLastUpdatedBy(ruleGroupDTO.getCreatedBy());
    		ruleGroup.setLastUpdatedDate(ZonedDateTime.now());
    		ruleGroup.setTenantId(tenantId);

    		/**************************save rule group***************************/
    		RuleGroup newGrp=new RuleGroup();
    		newGrp=ruleGroupRepository.save(ruleGroup);

    		log.info("newGrp has id for disp as"+newGrp.getIdForDisplay());
    		String idForDisplay ="";
    		if(ruleGroupDTO.getId() == null)
    		{
    			idForDisplay = IDORUtils.computeFrontEndIdentifier(newGrp.getId().toString());
    			log.info("setting id for display"+idForDisplay);
    			newGrp.setIdForDisplay(idForDisplay);
    			newGrp = ruleGroupRepository.save(ruleGroup);

    		}
    		/**************************save rule group***************************/

    		//ruleGroupSearchRepository.save(newGrp);

    		if(newGrp!=null && newGrp.getId()!=null)
    		{
    			List<ErrorReport> rulesReports = new ArrayList<ErrorReport>();
    			try {
    				errorMessage="Recon process save failed as atleast a single rule is not tagged";    	    				
    				if(ruleGroupDTO.getRules()== null || ruleGroupDTO.getRules().size()<1)
    				{
    					throw new RuntimeException(errorMessage);
    				}
    				rulesReports=ruleService.postRules(ruleGroupDTO.getRules(), newGrp,bulkUpload);
    				log.info("rulesReports has size"+rulesReports.size());
    				int count = 0;
    				errorMessage="Recon process save failed while fetching the tagged rules";  
    				count = ruleGroupDetailsRepository.findRuleCountByGroupId(newGrp.getId());
    				ErrorReport saveReport =new ErrorReport();
    				if(count>0)
    				{
    					saveReport.setTaskName("Recon process saved : "+ruleGroupDTO.getName());
    					saveReport.setTaskStatus("Success");
    					if(bulkUpload != null && bulkUpload.equalsIgnoreCase("true"))
    					{
    						saveReport.setDetails(rulesReports.size() +" Rules saved for recon process : "+ruleGroupDTO.getName());
    					}
    					else
    						saveReport.setDetails(newGrp.getIdForDisplay());
    				
    					
    					/*calling method for app module summary*/
    					 appModuleSummaryResource.updateAppModuleSummaryAfterRuleGroupIsCreated(newGrp.getId(), request);
    				}
    				else
    				{
    					saveReport.setTaskName("Recon process save Failed");
    					saveReport.setTaskStatus("Failed");
    				}
    				saveReport.setSubTasksList(rulesReports);
    				errorReports =(saveReport);
    				log.info("rule returned "+errorReports.toString()+" reports");
    				//As adhoc rule will be only one 
    				//check if the got rule list has adhoc rule
    				//if yes=> call next process after creation
    				/**
    				 * find out for adhoc rule
    				 */
    				errorMessage="Recon process save failed while checking if rule is adhoc type";
    				if(ruleGroupDTO.isAdhocRuleCreation())
    				{
    					Long id = 0L;
    					id = rulesRepository.findIdOfAdhocRule(newGrp.getId());
    					if(id>0)
    					{
    						HashMap parameterSet = new HashMap<>();
    						parameterSet.put("param1",ruleGroupDTO.getId());
    						parameterSet.put("param2",id);
    						log.info("ruleGroupDTO.getLastUpdatedby() :"+ruleGroupDTO.getLastUpdatedBy());
    						errorMessage="Recon process save failed because initiating the job is failed";
    						ResponseEntity response= oozieService.jobIntiateForAcctAndRec(tenantId, userId, "Reconciliation", parameterSet,"ADHOC",request);
    						ErrorReport jobInitiation=new ErrorReport();
    						jobInitiation.setTaskName("Job Initiation");

    						log.info("response.getBody() :"+response.getBody());
    						String status=response.getBody().toString().substring(8).replaceAll("\\}", "");
    						log.info("status :"+status);
    						log.info("response.getstatusValue :"+response.getStatusCodeValue());
    						jobInitiation.setTaskStatus(status);
    						//if(!errorReports.contains(jobInitiation));
    						List<ErrorReport> subTasks = new ArrayList<ErrorReport>();
    						subTasks.add(jobInitiation);
    						errorReports.setSubTasksList(subTasks);
    					}
    				}
    				else
    				{
    					log.info("doesnot contain adhoc :");
    					log.info("errorReports size"+errorReports.toString());
    					return errorReports;

    				}
    			} catch (RuntimeException e) {
    				log.info("in catch");
    				log.info("Exception  is "+e.getMessage());
    				ErrorReport errorRep =new ErrorReport();
    				errorRep.setTaskName("Saving rules");
    				errorRep.setTaskStatus("Failed");
    				errorRep.setDetails(e.getMessage());
    				errorReports = (errorRep);
    				log.info("added report"+errorReports.toString());
    				throw new RuntimeException(e.getMessage());
    				//return errorReports;
    			} catch (java.lang.Exception e) {
    				// TODO Auto-generated catch block
    				ErrorReport errorRep =new ErrorReport();
    				errorRep.setTaskName("Saving rules");
    				errorRep.setTaskStatus("Failed");
    				errorRep.setDetails(e.getMessage());
    				errorReports = (errorRep);
    				log.info("exception is in second catch");
    				throw new RuntimeException(e.getMessage());
    			}

    			/*if(ruleGroupDTO.getId() == null && !ruleGroupDTO.isAdhocRuleCreation())
    	    		{
    	    			log.info("no adhoc rule");
    			 *//**
    			 * call file write to HDFS 
    			 *//*
    					ErrorReport reportForFilewriteToHDFS = new ErrorReport();
    					reportForFilewriteToHDFS.setTaskName("File write to HDFS");
    					if(ruleGroupDTO.getId() == null)
    					{
    						String builtPath = "";
    						builtPath = buildPath(newGrp.getId());
    						reportForFilewriteToHDFS.setDetails(builtPath);
    						errorReport.add(reportForFilewriteToHDFS);
    					}
    			  *//**
    			  * end file write to HDFS 
    			  *//*
    	    		}*/

    			/*
    				for(int i =0;i<ruleGroupDTO.getRules().size();i++)
    				{
    					RulesDTO ruleDTO = new RulesDTO();
    					ruleDTO = ruleGroupDTO.getRules().get(i);
    					log.info("ruleDTO getId :"+ruleDTO.getRule().getId());
    					if(ruleDTO.getRule().getId()==null || ruleDTO.getRule().getId()==0 )
    					{
    						log.info("ruleDTO getId in if:"+ruleDTO.getRule().getId());

    						if(ruleDTO.getRule() != null && ruleDTO.getRule().getRuleType() != null && ruleDTO.getRule().getRuleType().contains("ADHOC"))//should modify to get latest record from rules
    						{
    							//fetch error report of adhoc rule for id
    							Long ruleId = 0L; 
    							if(errorReport.get(0).getTaskName().contains("Rule Group Save"))
    							{
    								log.info("errorReport.get(0).getSubTasksList().size() :"+errorReport.get(0).getSubTasksList().size());
    								if(errorReport.get(0).getSubTasksList().size() > 0)
    								{
    									if(errorReport.get(0).getSubTasksList().get(0).getTaskName().contains("Save Rule"))
    									{
    										ruleId =Long.valueOf( errorReport.get(0).getSubTasksList().get(errorReport.get(0).getSubTasksList().size()-1).getDetails());
    										log.info("got rule id with"+ruleId);
    										//call next process
    										HashMap parameterSet = new HashMap<>();
    										parameterSet.put("param1",ruleGroupDTO.getId());
    										parameterSet.put("param2",ruleId);
    										log.info("ruleGroupDTO.getLastUpdatedby() :"+ruleGroupDTO.getLastUpdatedBy());
    										ResponseEntity response= oozieService.jobIntiateForAcctAndRec(ruleGroupDTO.getTenantId(), ruleGroupDTO.getLastUpdatedBy(), "Reconciliation", parameterSet,"ADHOC");
    										ErrorReport jobInitiation=new ErrorReport();
    										jobInitiation.setTaskName("Job Initiation");

    										log.info("response.getBody() :"+response.getBody());
    										String status=response.getBody().toString().substring(8).replaceAll("\\}", "");
    										log.info("status :"+status);
    										log.info("response.getstatusValue :"+response.getStatusCodeValue());


    										jobInitiation.setTaskStatus(status);
    										if(!errorReport.contains(jobInitiation));
    										errorReport.add(jobInitiation);

    									}
    									else
    									{
    										log.info("Save Rule doesnt exist");
    									}
    								}
    								else
    								{
    									log.info("getSubTasksList is empty");
    								}
    							}
    							else
    							{
    								log.info("Rule Group Save doesnt exist");
    							}

    						}
    						else
    						{
    							log.info("no adhoc rule");
    			 *//**
    			 * call file write to HDFS
    			 *//*
    							ErrorReport reportForFilewriteToHDFS = new ErrorReport();
    							reportForFilewriteToHDFS.setTaskName("File write to HDFS");
    							if(ruleGroupDTO.getId() == null)
    							{
    								String builtPath = "";
    								builtPath = buildPath(newGrp.getId());
    								reportForFilewriteToHDFS.setDetails(builtPath);
    								errorReport.add(reportForFilewriteToHDFS);
    							}
    			  *//**
    			  * End file write to HDFS
    			  *//*
    						}
    					}
    					else
    					{
    						//log.info("no adhoc rule");
    					}
    				}*/
    		}
    	}
    	else
    	{
    		emptyRuleGroup.setTaskStatus("Failed");
    		emptyRuleGroup.setDetails("Process cannot be saved as process name is empty");
    		errorReports = (emptyRuleGroup);
    	}
    	
		return errorReports;
    	
    	

	}
	public String buildPath(Long ruleGroupId) throws ClassNotFoundException, SQLException, IOException
	{
		String pathBuilt = "";
		String fileNames = "";
		String ruleIds ="";
		String dataViewIds = "";
		/**
		 * fetch file names
		 */
		
		
		/**
		 * DB cred's
		 */
	    String dbUrl=env.getProperty("spring.datasource.url");
	    String schemaName= env.getProperty("spring.datasource.databaseName");
		String userName = env.getProperty("spring.datasource.username");
		String password = env.getProperty("spring.datasource.password");
		String jdbcDriver = env.getProperty("spring.datasource.jdbcdriver");
		String host = env.getProperty("spring.datasource.serverName");
		Connection conn = null;
		RuleGroup ruleGrp= ruleGroupRepository.findOne(ruleGroupId);
		
		
		List<BigInteger> ruleIdsList= ruleGroupDetailsRepository.fetchRuleIdsByGroupAndTenantId(ruleGroupId,ruleGrp.getTenantId());
		List<Long> dataViewIdsList = new ArrayList<Long>();
		
		if(ruleIdsList.size() > 0)
		{
			for(int i = 0;i<ruleIdsList.size();i++)
			{
				Rules rule = new Rules();
				rule =  rulesRepository.findById(Long.valueOf(ruleIdsList.get(i)+""));
				dataViewIdsList.add(rule.getSourceDataViewId());
				dataViewIdsList.add(rule.getTargetDataViewId());
				if(i==0)
					dataViewIds =  rule.getSourceDataViewId() +","+rule.getTargetDataViewId();
				else
				dataViewIds = ","+rule.getSourceDataViewId() +","+rule.getTargetDataViewId();
				
				if(i == 0)
				{
					ruleIds = ruleIdsList.get(i)+"";
				}
				else {
					ruleIds = ruleIds  +","+ ruleIdsList.get(i);
				}
				
			}
			
		}
		log.info("ruleIds built is:"+ruleIds);
		
		/**
		 * fetch file names based on dataview ids
		 */
		List<Object[]> fileNamesList = 	dataStagingRepository.findFileNamesByDataviewIds(dataViewIdsList);
		for(int i =0; i<fileNamesList.size() ; i++)
		{
			log.info("file name print:"+fileNamesList.get(i));
			if(i==0)
				fileNames = fileNamesList.get(i)+"";
			else
				fileNames = fileNames +","+fileNamesList.get(i)+"";
		}
		pathBuilt = fileNames +"|" +ruleGrp.getTenantId()+"|"+host+"|"+schemaName+"|"+userName+"|"+password+"|"+ruleGroupId+"|"+ruleIds+"|"+ruleGrp.getCreatedBy()+"|"+""
						+dataViewIds;
		fileWriteToHDFS(pathBuilt,ruleGrp.getName());
		return pathBuilt;
	}
	public void fileWriteToHDFS(String path, String ruleGroupName) throws IOException
	{
		log.info("entered to HDFS");
		BufferedWriter bw = null;
		FileWriter fw = null;
		String dir="//home//nspl//HDFS test//";
		ruleGroupName = ruleGroupName.trim();
		final String FILENAME = dir+ruleGroupName+".txt";//trim spaces
		//String s = "File_name1|Pinterest|192.168.0.44|agree_application_2909|root|Welcome|\"\"|1|11|\"\"|DataView1";
		/**
		 * param1 - empty
		 * param2- tenant id
		 * 3 - host
		 * 4-db name
		 * 5-db username
		 * 6-db pwd
		 * 7-RG id
		 * 8-rules(comma separated)
		 * 9-crnt user id
		 * 10-empty
		 * 11-dv id's (comma separated)
		 * 
		 * 
		 */
		fw = new FileWriter(FILENAME);
		bw = new BufferedWriter(fw);
		bw.write(path); 
		 UserGroupInformation ugi
         = UserGroupInformation.createRemoteUser("hdsingle");
		 
		 ugi.doAs(new PrivilegedAction<Void>() {

			@Override
			public Void run(){
				// TODO Auto-generated method stub
				Configuration conf = new Configuration();
				conf.set("fs.default.name", "hdfs://qat.nspl.com:9000/recon/");
				conf.set("hadoop.job.ugi", "hdsingle");
			
				FileSystem file = null;
				try {
					file = FileSystem.get(conf);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				File[] sourceFiles = new File(dir).listFiles();
				if(sourceFiles != null) {
					for(File f: sourceFiles) {
						//we can filter files if needed here
						 try {
							 log.info("copying to HDFS");
							file.copyFromLocalFile(true, true, new Path(f.getPath()), new Path("hdfs://qat.nspl.com:9000/recon"));
						} catch (IllegalArgumentException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				log.info("copied to HDFS");
				return null;
			}
		});
		 
		 
		 
		// ugi.doAs(new PrivilegedAction<Void>() {
			/*Configuration conf = new Configuration();
			conf.set("fs.default.name", "hdfs://qat.nspl.com:9000/recon/");
			conf.set("hadoop.job.ugi", "hdsingle");
		
			FileSystem file = FileSystem.get(conf);
	
					File[] sourceFiles = new File(dir).listFiles();
					            if(sourceFiles != null) {
					                for(File f: sourceFiles) {
					                    //we can filter files if needed here
					                    file.copyFromLocalFile(true, true, new Path(f.getPath()), new Path("hdfs://qat.nspl.com:9000/recon"));
					                }
					            }
			System.out.println("copied to HDFS");*/
	//		return null;
	//	 });
		if (bw != null)
			bw.close();

		if (fw != null)
			fw.close();
	}
	/**
	 * Author : Shobha
	 * @param request
	 * @param approvalRuleDto
	 * @param bulkUpload
	 * @return
	 * @throws java.lang.Exception 
	 */
	@Transactional(propagation= Propagation.REQUIRED)
	public ErrorReport postApprovalRuleGroup(HttpServletRequest request,ApprovalRuleDto approvalRuleDto,Boolean bulkUpload) throws java.lang.Exception
	{
		log.info("Serive request to post approval rule group");
		ApprovalRuleDto apptRuleDTO=approvalRuleDto;
		HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long userId=Long.parseLong(map.get("userId").toString());
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
		String errorMessage = "";
		RuleGroup ruleGrp=new RuleGroup();
    	ErrorReport errorReport=new ErrorReport();

    		if(apptRuleDTO.getId()!=null && !apptRuleDTO.getId().equals(""))
    		{
    			if(apptRuleDTO.getId() !=null)
    			{
    				errorMessage = "Approval process save failed while fetching the details";
    				RuleGroup rg = ruleGroupRepository.findByIdForDisplayAndTenantId(apptRuleDTO.getId(), tenantId);
    				if(rg != null && rg.getId() != null)
    				{
    					ruleGrp.setId(rg.getId());
    					ruleGrp.setIdForDisplay(rg.getIdForDisplay());
    				}
    			}
    		}
    		errorMessage = "Approval process save failed, because approval process name is empty";
    		
    		if(apptRuleDTO.getName() == null || apptRuleDTO.getName().isEmpty() || apptRuleDTO.getName().equals(""))
    			throw new RuntimeException(errorMessage);
    		else
    		ruleGrp.setName(apptRuleDTO.getName());

    		RuleGroup duplicateRuleGroup = new RuleGroup();
    		if(apptRuleDTO.getId() == null)
    		{
    			duplicateRuleGroup = ruleGroupRepository.findByTenantIdAndName(tenantId, apptRuleDTO.getName());	
    			if(duplicateRuleGroup != null && duplicateRuleGroup.getId() != null)
        		{
        			errorMessage = "Approval process save failed because process name already exists";
        			throw new RuntimeException(errorMessage);
        		}
    		}
    		errorMessage = "Approval process save failed while saving the rule purpose/category";

    		if(apptRuleDTO.getRulePurpose()!=null && !apptRuleDTO.getRulePurpose().isEmpty())
    			ruleGrp.setRulePurpose(apptRuleDTO.getRulePurpose());
    		else
    			ruleGrp.setRulePurpose("APPROVALS");

    		errorMessage = "Approval process save failed while saving the start date for the process";

    		if(apptRuleDTO.getStartDate()!=null)
    			ruleGrp.setStartDate(apptRuleDTO.getStartDate());
    		else
    			ruleGrp.setStartDate(ZonedDateTime.now());

    		errorMessage = "Approval process save failed while saving the end date for the process";

    		if(apptRuleDTO.getEndDate()!=null)
    			ruleGrp.setEndDate(apptRuleDTO.getEndDate());

    		errorMessage = "Approval process save failed while saving the status for the process";

    		if(apptRuleDTO.getEnableFlag()!=null)
    			ruleGrp.enabledFlag(apptRuleDTO.getEnableFlag());

    		ruleGrp.setTenantId(tenantId);

    		ruleGrp.setCreatedBy(apptRuleDTO.getCreatedBy());

    		ruleGrp.setLastUpdatedBy(apptRuleDTO.getCreatedBy());

    		if(apptRuleDTO.getId()==null || (bulkUpload != null && bulkUpload == true))
    		{
    			ruleGrp.setCreationDate(ZonedDateTime.now());
    		}
    		else
    			ruleGrp.setCreationDate(apptRuleDTO.getCreatedDate());

    		ruleGrp.setLastUpdatedDate(ZonedDateTime.now());

    		errorMessage = "Approval process save failed";

    		RuleGroup savedRuleGroup = ruleGroupRepository.save(ruleGrp);

    		if(apptRuleDTO.getId() ==null)
    		{
    			String idForDisplay = IDORUtils.computeFrontEndIdentifier(savedRuleGroup.getId().toString());
    			savedRuleGroup.setIdForDisplay(idForDisplay);
    			savedRuleGroup = ruleGroupRepository.save(savedRuleGroup);
    		}
    		if(savedRuleGroup!=null && savedRuleGroup.getId()!=null)
    		{

    			RuleGroup taggedGroup = new RuleGroup();
    			
    			//get if already tagged to other group, delete that
    			List<RuleGroup> alreadyTaggedGroup = new ArrayList<RuleGroup>();
    			alreadyTaggedGroup = ruleGroupRepository.findByApprRuleGrpId(ruleGrp.getId());
    			log.info("alreadyTaggedGroup size:"+alreadyTaggedGroup.size());
    			if(alreadyTaggedGroup.size() == 1)
    			{
    				alreadyTaggedGroup.get(0).setApprRuleGrpId(null);
    				ruleGroupRepository.save(alreadyTaggedGroup.get(0));
    			}
    			else
    			{
    				
    			}
    			if(bulkUpload == null)
    			{
    				errorMessage = "Approval process save failed while checking if any recon/accounting process tagged";
    				if(apptRuleDTO.getApprRuleGrpId() == null)
    					throw new RuntimeException(errorMessage);
    			}
    			else if(bulkUpload == true)
    			{
    				//get approval module id from name
    				RuleGroup processGrp = new RuleGroup();
    				errorMessage = "Approval process save failed while validating if recon/accounting process is tagged";
    				if( apptRuleDTO.getApprRuleGrpName() == null)
    				{
    					throw new RuntimeException(errorMessage);
    				}
    				else
    				{
    					errorMessage = "Approval process save failed while validating if recon/accounting process is tagged";
    					processGrp = ruleGroupRepository.findByTenantIdAndName(tenantId, apptRuleDTO.getApprRuleGrpName());
    					errorMessage = "Approval process save failed while validating existence of recon/accounting process tagged";
    					apptRuleDTO.setApprRuleGrpId(processGrp.getIdForDisplay());
    				}
    			}
    			taggedGroup = ruleGroupRepository.findByIdForDisplayAndTenantId(apptRuleDTO.getApprRuleGrpId(), tenantId);
    			errorMessage = "Approval process save failed because, recon/accounting process tagged to it doesnot exist";

    			ruleGrp.setApprRuleGrpId(taggedGroup.getId());

    			if(taggedGroup.getRulePurpose().toLowerCase().contains("recon")){
    				errorMessage = "Sorry! Updating the recon process with this approval process failed.";	
    			}
    			else if(taggedGroup.getRulePurpose().toLowerCase().contains("accounting")){
    				errorMessage = "Sorry! Updating the accounting process with this approval process failed.";	
    			}
    			taggedGroup.setApprRuleGrpId(savedRuleGroup.getId());
    			ruleGroupRepository.save(taggedGroup);
    			errorMessage = "Approval process save failed, while fetching the already tagged rules";
    			List<BigInteger> ruleIdsList = ruleGroupDetailsRepository.fetchRuleIdsByGroupAndTenantId(savedRuleGroup.getId(),tenantId);

    			log.info("ruleIdsList before:"+ruleIdsList);

    			if(apptRuleDTO.getRules() == null ||apptRuleDTO.getRules().size()<=0)
    			{
    				errorMessage = "Approval process save failed, because atleast one rule should be tagged";
    			}
    			else
    			{
    				List<AppRuleCondAndActDto> rulesAndCond=apptRuleDTO.getRules();
    				for(int i=0;i<rulesAndCond.size();i++)
    				{
    					AppRuleCondAndActDto ruleCond=rulesAndCond.get(i);
    					Rules rule=new Rules();

    					if(ruleCond.getId()!=null && ruleCond.getId()!=0)
    					{

    						rule.setId(ruleCond.getId());

    						if(ruleIdsList.size()>0)
    						{ 
    							for(int j=0;j<rulesAndCond.size();j++)
    							{
    								if(rulesAndCond.get(j).getId()!=null && rulesAndCond.get(j).getId()!=0)
    								{ 
    									for(int id=0;id<ruleIdsList.size();id++)
    									{
    										log.info("ruleIdsList.get(id) :"+ruleIdsList.get(id));
    										log.info("ruleDTO.get(i).getRule().getId() :"+rulesAndCond.get(j).getId());
    										if(ruleIdsList.get(id).longValue()==rulesAndCond.get(j).getId())
    										{
    											log.info("same");
    											ruleIdsList.remove(ruleIdsList.get(id));
    										}
    									}
    								}
    							}}
    						log.info("ruleIdsList after:"+ruleIdsList);


    					}
    					for(int j=0;j<ruleIdsList.size();j++)
    					{
    						log.info("ruleIdsList.get(j).longValue() :"+ruleIdsList.get(j).longValue());
    						log.info("newGrp.getId() :"+savedRuleGroup.getId());
    						RuleGroupDetails ruleGroup=ruleGroupDetailsRepository.findByRuleGroupIdAndRuleId(savedRuleGroup.getId(),ruleIdsList.get(j).longValue());
    						log.info("ruleGrpId :"+ruleGrp);
    						log.info("");
    						if(ruleGroup!=null)
    							ruleGroupDetailsRepository.delete(ruleGroup);
    					}

    					errorMessage="Approval process save failed, because rule name in rule-"+(i+1)+" is null";
    					rule.setRuleCode(ruleCond.getRuleCode());

    					errorMessage="Approval process save failed, while saving start date in rule-"+(i+1);
    					if(ruleCond.getStartDate()!=null)
    						rule.setStartDate(ruleCond.getStartDate());
    					else
    						rule.setStartDate(ZonedDateTime.now());

    					if(ruleCond.getEndDate()!=null)
    						rule.setEndDate(ruleCond.getEndDate());

    					if(ruleCond.getEnabledFlag()!=null)
    						rule.setEnabledFlag(ruleCond.getEnabledFlag());

    					if(bulkUpload != null  && bulkUpload == true)
    						rule.setEnabledFlag(true);

    					errorMessage="Approval process save failed, as source data view in rule-"+(i+1)+" is null";
    					if(bulkUpload != null  && bulkUpload == true)
    					{
    						errorMessage = "Approval process save failed, while validating the source data view name in rule-"+(i+1);
    						List<DataViews> dvs = dataViewsRepository.findByTenantIdAndDataViewDispName(tenantId, ruleCond.getSourceDataViewName());
    						if(dvs.size()>1)
    						{
    							errorMessage="Approval process save failed, as multiple data sources found with name "+ruleCond.getSourceDataViewName()+" while validating rule-"+(i+1);
    							throw new RuntimeException(errorMessage);
    						}
    						else if(dvs.size() == 0)
    						{
    							errorMessage = "Approval process save failed, as data source in rule-"+(i+1)+" not found for this tenant";
    							throw new RuntimeException(errorMessage);
    						}
    						else if(dvs.size() == 1)
    						{
    							errorMessage = "Approval process save failed, while saving the data source in rule-"+(i+1);
    							rule.setSourceDataViewId(dvs.get(0).getId());
    						}
    					}
    					else
    					{
    						if(ruleCond.getSourceDataViewId()!=null)
    						{
    							DataViews dv=dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, ruleCond.getSourceDataViewId());
    							rule.setSourceDataViewId(dv.getId());
    						}	
    					}
    					//rule.setRuleType(ruleCond.getApprovalNeededType());
    					rule.setTenantId(tenantId);
    					if(savedRuleGroup.getCreatedBy()!=null)
    						rule.setCreatedBy(savedRuleGroup.getCreatedBy());
    					if(savedRuleGroup.getLastUpdatedBy()!=null)
    						rule.setLastUpdatedBy(savedRuleGroup.getLastUpdatedBy());

    					if(ruleCond.getId()==null || ruleCond.getId()==0)
    						rule.setCreationDate(ZonedDateTime.now());
    					else
    						rule.setCreationDate(ruleCond.getCreatedDate());
    					rule.setLastUpdatedDate(ZonedDateTime.now());

    					errorMessage = "Approval process save failed, while saving the rule-"+(i+1);
    					Rules savedRule =rulesRepository.save(rule);
    					if(savedRule.getId()!=null && savedRuleGroup.getId()!=null)
    					{
    						RuleGroupDetails ruleGrpDetails=new RuleGroupDetails();
    						RuleGroupDetails ruleGrpDet=ruleGroupDetailsRepository.findByRuleGroupIdAndRuleId(savedRuleGroup.getId(),savedRule.getId());
    						errorMessage = "Approval process save failed, while fetching the tagged details of rule-"+(i+1);
    						if(ruleGrpDet!=null)
    						{
    							log.info("ruleGrpDet is not null.");
    							errorMessage = "Approval process save failed, while updating the priority of rule-"+(i+1);

    							if(bulkUpload == null )
    							{
    								ruleGrpDet.setPriority(ruleCond.getPriority());
    							}
    							else if(bulkUpload ==  true)
    							{
    								ruleGrpDet.setPriority(i+1);
    							}
    							errorMessage = "Approval process save failed, while updating the assignment status of rule-"+(i+1);
    							ruleGrpDet.setEnabledFlag(ruleCond.getAssignmentFlag());
    							ruleGroupDetailsRepository.save(ruleGrpDet);
    						}
    						else
    						{
    							log.info("ruleGrpDet is null.");
    							errorMessage = "Approval process save failed, while updating the priority of rule-"+(i+1);
    							ruleGrpDetails.setPriority(ruleCond.getPriority());
    							ruleGrpDetails.setRuleGroupId(savedRuleGroup.getId());
    							ruleGrpDetails.setRuleId(savedRule.getId());
    							ruleGrpDetails.setTenantId(tenantId);
    							ruleGrpDetails.setCreatedBy(savedRule.getCreatedBy());
    							ruleGrpDetails.setLastUpdatedBy(savedRule.getCreatedBy());
    							ruleGrpDetails.setEnabledFlag(ruleCond.getAssignmentFlag());
    							ruleGrpDetails.setCreationDate(ZonedDateTime.now());
    							ruleGrpDetails.setLastUpdatedDate(ZonedDateTime.now());
    							errorMessage = "Approval process save failed, while tagging rule-"+(i+1)+" to "+savedRuleGroup.getName();
    							RuleGroupDetails ruleGrptag=ruleGroupDetailsRepository.save(ruleGrpDetails);
    						}
    						errorMessage = "Approval process save failed, because rule-"+(i+1)+" doesnot contain conditions. A rule should atleast have one condition.";
        					if(rulesAndCond.get(i).getApprovalConditions() == null || rulesAndCond.get(i).getApprovalConditions().size()<=0)
        					{
        						throw new RuntimeException(errorMessage);
        					}
        					else
        					{
        						//balance parenthesis and build expression and save to a column
        						
        						
        						List<AppRuleCondDto> appCondtionsList=rulesAndCond.get(i).getApprovalConditions();
        						errorMessage = "Approval process save failed while building condition expression at rule-"+(i+1);
        						String exp = ruleService.buildWhereClause(appCondtionsList);
        						if(exp != null && !exp.equals("") && exp != "")
        						{
        							//balance parenthesis
        							Boolean validateParenthesis = ruleService.balanceParanthesis(exp);
        							if(validateParenthesis)
        							{
        								
        							}
        							else
        							{
        								errorMessage = "Approval process save failed while validating parenthesis at rule-"+(i+1);
        								throw new RuntimeException(errorMessage);
        							}
        							savedRule.setConditionExpression(exp);
        							Rules updatedRule = new Rules();
        							updatedRule=rulesRepository.save(savedRule);
        						}
        						for(int k=0;k<appCondtionsList.size();k++)
        						{
        							AppRuleCondDto appCondtionsdto= new AppRuleCondDto();
        							appCondtionsdto = appCondtionsList.get(k);

        							AppRuleConditions appRuleCond=new AppRuleConditions();

        							if(appCondtionsdto.getId() != null)
        								appRuleCond.setId(appCondtionsdto.getId());

        							appRuleCond.setRuleId(savedRule.getId());

        							if(appCondtionsdto.getOpenBracket()!=null)
        								appRuleCond.setOpenBracket(appCondtionsdto.getOpenBracket());
        							if(bulkUpload == null)
        							{
        								if(appCondtionsdto.getColumnId() == null)
        								{
        									errorMessage = "Approval process save failed, because source column in rule condition -"+(k+1)+" in rule -"+(i+1)+" is missing";
        									throw new RuntimeException(errorMessage);
        								}
        								else
        								{
        									appRuleCond.setColumnId(appCondtionsdto.getColumnId());
        									if(appCondtionsdto.getOperator() == null || appCondtionsdto.getOperator().equals(""))
        									{
        										errorMessage = "Approval process save failed, because operator in rule condition -"+(k+1)+" in rule -"+(i+1)+" is missing";
        										throw new RuntimeException(errorMessage);
        									}
        									else
        									{
        										appRuleCond.setOperator(appCondtionsdto.getOperator());	
        										if(appCondtionsdto.getValue() == null || appCondtionsdto.getValue().equals(""))
        										{
        											errorMessage = "Approval process save failed, because value in rule condition -"+(k+1)+" in rule -"+(i+1)+" is missing";
        											throw new RuntimeException(errorMessage);
        										}
        										else
        										{
        											appRuleCond.setValue(appCondtionsdto.getValue());
        										}
        									}
        								}
        							}
        							else if(bulkUpload == true)
        							{
        								if(appCondtionsdto.getColumnName() == null || appCondtionsdto.getColumnName().equals("") || appCondtionsdto.getColumnName().isEmpty())
        								{
        									errorMessage = "Approval process save failed, because source column in rule condition -"+(k+1)+" in rule -"+(i+1)+" is missing";
        									throw new RuntimeException(errorMessage);
        								}
        								else
        								{
        									DataViewsColumns dataViewColumn = new DataViewsColumns();
        									errorMessage = "Approval process save failed, while validating the source column in rule condition -"+(k+1)+" in rule -"+(i+1);
        									dataViewColumn =  dataViewsColumnsRepository.findByDataViewIdAndColumnName(savedRule.getSourceDataViewId(), appCondtionsdto.getColumnName());
        									errorMessage = "Approval process save failed, source column in rule condition -"+(k+1)+" in rule -"+(i+1)+" doesnot exist for that data view";
        									appRuleCond.setColumnId(dataViewColumn.getId());
        									LookUpCode operatorCode = new LookUpCode();
        									errorMessage = "Approval process save failed, operator in rule condition -"+(k+1)+" in rule -"+(i+1)+" doesnot exist for that tenant";
        									operatorCode = lookUpCodeRepository.findByMeaningAndLookUpTypeAndTenantId(appCondtionsdto.getOperatorMeaning(),dataViewColumn.getColDataType(), savedRuleGroup.getTenantId());
        									appRuleCond.setOperator(operatorCode.getLookUpCode());
        									errorMessage = "Approval process save failed, because value in rule condition -"+(k+1)+" in rule -"+(i+1)+" is missing";
        									appRuleCond.setValue(appCondtionsdto.getValue());
        								}
        							}


        							if(appCondtionsdto.getCloseBracket()!=null)
        								appRuleCond.setCloseBracket(appCondtionsdto.getCloseBracket());

        							if(appCondtionsdto.getLogicalOperator()!=null)
        								appRuleCond.setLogicalOperator(appCondtionsdto.getLogicalOperator());



        							if(savedRuleGroup.getCreatedBy()!=null)
        								appRuleCond.setCreatedBy(savedRuleGroup.getCreatedBy());

        							if(savedRuleGroup.getCreatedBy()!=null)
        								appRuleCond.setLastUpdatedBy(savedRuleGroup.getLastUpdatedBy());

        							appRuleCond.setCreatedDate(ZonedDateTime.now());

        							appRuleCond.setLastUpdatedDate(ZonedDateTime.now());

        							AppRuleConditions appRuleCondId=appRuleConditionsRepository.save(appRuleCond);
        							log.info("appRuleCondId :"+appRuleCondId);
        						} 
        						errorMessage ="Approval process save failed, because atleast one approver is not tagged to rule -"+(i+1);
        						if(rulesAndCond.get(i).getApprovalActions() ==null)
        						{
        							throw new RuntimeException(errorMessage);
        						}
        						else
        						{
        							if(rulesAndCond.get(i).getApprovalActions().getActionDetails() == null || rulesAndCond.get(i).getApprovalActions() .getActionDetails().size()<=0)
        							{
        								throw new RuntimeException(errorMessage);
        							}
        							else
        							{
        								ApprovalActionDto appActDto=rulesAndCond.get(i).getApprovalActions();
        								List<ApprRuleAssgnDto> apprRulAssgDtoList=appActDto.getActionDetails();
        								if(appActDto.getAssigneeType() == null || appActDto.getAssigneeType().equals("") || appActDto.getAssigneeType().isEmpty())
        								{
        									errorMessage ="Approval process save failed, because assignee type is missing in rule -"+(i+1);
        									throw new RuntimeException(errorMessage);
        								}
        								if(appActDto.getAssigneeType().toLowerCase().contains("user"))
        								{

        								}
        								else if(appActDto.getAssigneeType().toLowerCase().contains("group"))
        								{
        									if(apprRulAssgDtoList.size() == 1)
        									{

        									}
        									else
        									{
        										errorMessage ="Approval process save failed, invalid approver tagging found in rule -"+(i+1)+". A rule should be tagged with only one group.";
        										throw new RuntimeException(errorMessage);
        									}
        								}
        								String schemaName= env.getProperty("spring.secondDatasource.databaseName").toString();
        								Connection conn = null;
        								Statement stmt = null;
        								ResultSet result2=null;
        								HashMap<String,String> userMap = new HashMap<String,String>();
        								try{ 
        									DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("secondaryDataSource");
        									conn = ds.getConnection();
        									conn.setSchema(schemaName);
        									stmt = conn.createStatement();
        									result2=stmt.executeQuery("select * from "+schemaName+".jhi_user where tenant_id ="+tenantId);
        									String userName=null;
        									while(result2.next())
        									{
        										if(result2.getString("first_name")!=null)
        										{
        											userName = result2.getString("first_name");
        										}
        										if(result2.getString("last_name")!=null)
        										{
        											userName = userName +" " +result2.getString("last_name");
        										}
        										userMap.put(userName, result2.getString("id"));
        									}
        								}
        								catch(SQLException se){
        								} 
        								finally{
        									if(conn!=null)
        										conn.close();
        									if(stmt!=null)
        										stmt.close();
        									if(result2!=null)
        										result2.close();
        								}
        								for(int j =0;j<apprRulAssgDtoList.size();j++)
        								{

        									//validate if assignee type is group, should have only group info and vice versa for user
        									ApprRuleAssgnDto appRuleAssiDto = apprRulAssgDtoList.get(j);
        									ApprovalRuleAssignment appRuleAssgn=new ApprovalRuleAssignment();

        									if(bulkUpload == null)
        									{
        										if(appRuleAssiDto.getAssigneeId()!=null)
        											appRuleAssgn.setAssigneeId(appRuleAssiDto.getAssigneeId());
        									}
        									else if(bulkUpload == true)
        									{
        										//fetch  group id from group name
        										//fetch user id from user name
        										errorMessage ="Approval process save failed, because assignee name in rule action - "+(j+1)+" is missing in rule - "+(i+1);
        										String assignee = appRuleAssiDto.getAssigneeName();
        										errorMessage ="Approval process save failed, while validating the user name in rule action - "+(j+1)+" in rule - "+(i+1);
        										String asgnId= userMap.get(assignee);
        										appRuleAssgn.setAssigneeId(Long.valueOf(asgnId));
        									}

        									appRuleAssgn.setId(appRuleAssiDto.getId());
        									appRuleAssgn.setAssignType(appActDto.getAssigneeType());
        									appRuleAssgn.setRuleId(savedRule.getId());




        									if(appRuleAssiDto.getEmail()!=null)
        										appRuleAssgn.setEmail(appRuleAssiDto.getEmail());
        									if(appRuleAssiDto.getAutoApproval()!=null)
        										appRuleAssgn.setAutoApproval(appRuleAssiDto.getAutoApproval());




        									if(savedRuleGroup.getCreatedBy()!=null)
        										appRuleAssgn.setCreatedBy(savedRuleGroup.getCreatedBy());
        									if(savedRuleGroup.getLastUpdatedBy()!=null)
        										appRuleAssgn.setLastUpdatedBy(savedRuleGroup.getLastUpdatedBy());
        									appRuleAssgn.setCreationDate(ZonedDateTime.now());
        									appRuleAssgn.setLastUpdatedDate(ZonedDateTime.now());


        									errorMessage ="Approval process save failed, while saving the rule action - "+(j+1)+" in rule - "+(i+1);

        									ApprovalRuleAssignment appAssgn=approvalRuleAssignmentRepository.save(appRuleAssgn);
        								}
        							}
        						}

        					}
    					}
    					

    				}
    			}
    			errorReport.setTaskName("Approval process "+savedRuleGroup.getName()+" saved successfully");
    			errorReport.setTaskStatus("success");
    			errorReport.setDetails(savedRuleGroup.getIdForDisplay());
    		}
    		else
    		{
    			ErrorReport ruleGroupSave=new ErrorReport();
    			ruleGroupSave.setTaskName("Rule Group Save");
    			ruleGroupSave.setTaskStatus("failure");
    			errorReport=(ruleGroupSave);
    		}
    	return errorReport;
	}
	
	

}
