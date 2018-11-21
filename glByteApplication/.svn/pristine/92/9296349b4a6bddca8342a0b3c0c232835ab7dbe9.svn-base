package com.nspl.app.web.rest;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.domain.Calendar;
import com.nspl.app.domain.ProcessDetails;
import com.nspl.app.domain.Processes;
import com.nspl.app.domain.RuleGroup;
import com.nspl.app.domain.SourceProfiles;
import com.nspl.app.repository.CalendarRepository;
import com.nspl.app.repository.ProcessDetailsRepository;
import com.nspl.app.repository.ProcessesRepository;
import com.nspl.app.repository.RuleGroupRepository;
import com.nspl.app.repository.SourceProfilesRepository;
import com.nspl.app.security.IDORUtils;
import com.nspl.app.service.ActiveStatusService;
import com.nspl.app.service.UserJdbcService;
import com.nspl.app.web.rest.dto.ProcessesDTO;
import com.nspl.app.web.rest.util.HeaderUtil;
import com.nspl.app.web.rest.util.PaginationUtil;

/**
 * REST controller for managing Processes.
 */
@RestController
@RequestMapping("/api")
public class ProcessesResource {

    private final Logger log = LoggerFactory.getLogger(ProcessesResource.class);

    private static final String ENTITY_NAME = "processes";

    private final ProcessesRepository processesRepository;
    
    
    @Inject
    ProcessDetailsRepository processDetailsRepository;
    
    @Inject
    SourceProfilesRepository sourceProfilesRepository;
    
    @Inject
    RuleGroupRepository ruleGroupRepository;
    
    @Inject
    CalendarRepository calendarRepository;
    
    @Inject
    UserJdbcService userJdbcService;
    
    @Inject
    ActiveStatusService activeStatusService;

    public ProcessesResource(ProcessesRepository processesRepository) {
        this.processesRepository = processesRepository;
    }

    /**
     * POST  /processes : Create a new processes.
     *
     * @param processes the processes to create
     * @return the ResponseEntity with status 201 (Created) and with body the new processes, or with status 400 (Bad Request) if the processes has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/processeses")
    @Timed
    public ResponseEntity<Processes> createProcesses(@RequestBody Processes processes) throws URISyntaxException {
        log.debug("REST request to save Processes : {}", processes);
        if (processes.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new processes cannot already have an ID")).body(null);
        }
        Processes result = processesRepository.save(processes);
        return ResponseEntity.created(new URI("/api/processeses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    
   

    /**
     * PUT  /processes : Updates an existing processes.
     *
     * @param processes the processes to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated processes,
     * or with status 400 (Bad Request) if the processes is not valid,
     * or with status 500 (Internal Server Error) if the processes couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/processeses")
    @Timed
    public ResponseEntity<Processes> updateProcesses(@RequestBody Processes processes) throws URISyntaxException {
        log.debug("REST request to update Processes : {}", processes);
        if (processes.getId() == null) {
            return createProcesses(processes);
        }
        Processes result = processesRepository.save(processes);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, processes.getId().toString()))
            .body(result);
    }

    /**
     * GET  /processes : get all the processes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of processes in body
     */
    @GetMapping("/processeses")
    @Timed
    public ResponseEntity<List<Processes>> getAllProcesses(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Processes");
        Page<Processes> page = processesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/processeses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /processes/:id : get the "id" processes.
     *
     * @param id the id of the processes to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the processes, or with status 404 (Not Found)
     */
    @GetMapping("/processeses/{id}")
    @Timed
    public ResponseEntity<Processes> getProcesses(@PathVariable Long id) {
        log.debug("REST request to get Processes : {}", id);
        Processes processes = processesRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(processes));
    }

    /**
     * DELETE  /processes/:id : delete the "id" processes.
     *
     * @param id the id of the processes to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/processeses/{id}")
    @Timed
    public ResponseEntity<Void> deleteProcesses(@PathVariable Long id) {
        log.debug("REST request to delete Processes : {}", id);
        processesRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    
    /**
     * author ravali
     * @param offset
     * @param limit
     * @param tenantId
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/processessByTenantId")
  	@Timed
  	public ResponseEntity<List<ProcessesDTO>> getProcessessByTenantId(HttpServletRequest request,@RequestParam(value = "page" , required = false) Integer offset,
  			@RequestParam(value = "per_page", required = false) Integer limit) throws URISyntaxException {
    	
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
      	Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	List<ProcessesDTO> finalList = new ArrayList<ProcessesDTO>();
  		log.debug("REST request to get a page of Rule groups for tenant: "+tenantId);
  		List<Processes> processesList = new ArrayList<Processes>();
  		PaginationUtil paginationUtil=new PaginationUtil();
  		
  		int maxlmt=paginationUtil.MAX_LIMIT;
  		int minlmt=paginationUtil.MIN_OFFSET;
  		log.info("maxlmt: "+maxlmt);
  		Page<Processes> page = null;
  		HttpHeaders headers = null;
  		
  		if(limit==null || limit<minlmt){
  			processesList = processesRepository.findByTenantIdOrderByIdDesc(tenantId);
  			limit = processesList.size();
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
  			page = processesRepository.findByTenantIdOrderByIdDesc(tenantId,PaginationUtil.generatePageRequest2(offset, limit));
  			headers = PaginationUtil.generatePaginationHttpHeaders2(page, "/api/processessByTenantId",offset, limit);
  		}
  		else{
  			log.info("input limit is within maxlimit");
  			page = processesRepository.findByTenantIdOrderByIdDesc(tenantId,PaginationUtil.generatePageRequest(offset, limit));
  			headers = PaginationUtil.generatePaginationHttpHeaderss(page, "/api/ruleGroupsByTenantId", offset, limit);
  		}
      	if(page.getSize()>0)
      	{
      		for(Processes pr : page)
      		{
      			ProcessesDTO dto = new ProcessesDTO();
      			dto.setId(pr.getIdForDisplay());
      			dto.setProcessName(pr.getProcessName());
      			dto.setJhiDesc(pr.getDesc());
      			dto.setStartDate(pr.getStartDate());
      			dto.setEndDate(pr.getEndDate());
      			/** active check **/
            	
    			Boolean activeStatus=activeStatusService.activeStatus(pr.getStartDate(), pr.getEndDate(), pr.isEnable());
      			
      			dto.setJhiEnable(activeStatus);
      			dto.setCreatedBy(pr.getCreatedBy());
      			dto.setCreatedDate(pr.getCreatedDate());
      			dto.setLastUpdatedBy(pr.getLastUpdatedBy());
      			dto.setLastUpdatedDate(pr.getLastUpdatedDate());
      			finalList.add(dto);
      		}
      	}
  		return new ResponseEntity<>(finalList, headers, HttpStatus.OK);
      }
    
    /**
     * author ravali
     * @param procAndDetailMap
     * @param tenantId
     * @param userId
     * @return
     */
    @PostMapping("/processesesAndProcessDetails")
    @Timed
    public HashMap createProcessesAndProcessDetails(HttpServletRequest request,@RequestBody HashMap procAndDetailMap){//,@RequestParam Long tenantId,@RequestParam Long userId){
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
      	Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	Long userId=Long.parseLong(map.get("userId").toString());
    	
        log.debug("REST request to save ProcessesAndProcessDetails for tenant:"+tenantId+"->"+ procAndDetailMap);
        Processes proc=new Processes();
        HashMap finalMap=new HashMap();
        if(procAndDetailMap.get("id")!=null)
        {
        	Processes processes = processesRepository.findByTenantIdAndIdForDisplay(tenantId, procAndDetailMap.get("id").toString());
        	proc.setId(processes.getId());
        }
        if(procAndDetailMap.get("processName")!=null)
        	proc.setProcessName(procAndDetailMap.get("processName").toString());
        if(procAndDetailMap.get("startDate")!=null)
        {
        	ZonedDateTime stDate=ZonedDateTime.parse(procAndDetailMap.get("startDate").toString());
        	proc.setStartDate(stDate);
        }
        if(procAndDetailMap.get("endDate")!=null)
        {
        	ZonedDateTime toDate=ZonedDateTime.parse(procAndDetailMap.get("endDate").toString());
        	proc.setEndDate(toDate);
        }
        if(procAndDetailMap.get("desc")!=null)
        	proc.setDesc(procAndDetailMap.get("desc").toString());
        proc.setEnable(true);
        proc.setTenantId(tenantId);
        proc.setCreatedBy(userId);
        proc.setCreatedDate(ZonedDateTime.now());
        proc.setLastUpdatedBy(userId);
        proc.setLastUpdatedDate(ZonedDateTime.now());
        Processes result = processesRepository.save(proc);
		String idForDisplay = IDORUtils.computeFrontEndIdentifier(result.getId().toString());
		result.setIdForDisplay(idForDisplay);
		result = processesRepository.save(result);
        if(result!=null && result.getId()!=null)
        {
        	List<ProcessDetails> procDetList=new ArrayList<ProcessDetails>();
        	HashMap processDetailListMap= (HashMap) procAndDetailMap.get("processDetailList");
        	if(processDetailListMap.get("spList") !=null )
        	{
	        	List<HashMap> spList=(List<HashMap>) processDetailListMap.get("spList");
	        	/**
	        	 * for uppdating SP
	        	 */
	        	List<BigInteger> processdetIds=processDetailsRepository.findIdByProcessIdAndTagType(result.getId(),"sourceProfile");
	        	log.info("processdetIds before:"+processdetIds);
	        	if(processdetIds.size()>0)
	        	{
	        		for(int j=0;j<spList.size();j++)
					{
						if(spList.get(j).get("id")!= null )
						{
							for(int id=0;id<processdetIds.size();id++)
							{
								
								if(processdetIds.get(id).longValue()==Long.valueOf(spList.get(j).get("id").toString()))
								{
									processdetIds.remove(processdetIds.get(id));
								}
							}
						}
					}
	        	}
	        	log.info("processdetIds after:"+processdetIds);
	        	
	    		for(int j=0;j<processdetIds.size();j++)
				{
					log.info("ruleIdsList.get(j).longValue() :"+processdetIds.get(j).longValue());
					
					ProcessDetails processDet=processDetailsRepository.findOne(processdetIds.get(j).longValue());
					if(processDet!=null)
						processDetailsRepository.delete(processDet);
				}
	        	for(HashMap sp:spList)
	        	{
	        		ProcessDetails procDet=new ProcessDetails();
	
	        		if(sp.get("id")!=null)
	        		{
	        			procDet.setId(Long.valueOf((sp.get("id").toString())));
	        			procDet.setCreatedBy(Long.valueOf((sp.get("createdBy").toString())));
	        			procDet.setCreatedDate(ZonedDateTime.parse(sp.get("createdDate").toString()));
	        		}
	        		procDet.setProcessId(result.getId());
	        		if(sp.get("tagType")!=null)
	        			procDet.setTagType(sp.get("tagType").toString());
	        		if(sp.get("typeId")!=null)
	        		{
	        			SourceProfiles srcProf=sourceProfilesRepository.findByTenantIdAndIdForDisplay(tenantId, sp.get("typeId").toString());
	        			procDet.setTypeId(srcProf.getId());
	        		}
	        		if(sp.get("id")==null)
	        			procDet.setCreatedBy(userId);
	        		if(sp.get("id")==null)
	        			procDet.setCreatedDate(ZonedDateTime.now());
	        		procDet.setLastUpdatedBy(userId);
	        		procDet.setLastUpdatedDate(ZonedDateTime.now());
	        		procDetList.add(procDet);
	        	}
        	}
        	if(processDetailListMap.get("actGrpList") !=null )
        	{
        		List<HashMap> actGrpListMap=(List<HashMap>) processDetailListMap.get("actGrpList");
        		log.info("actGrpListMap ::" + actGrpListMap);
        		if(actGrpListMap!=null && !actGrpListMap.isEmpty())
        		{
        			/**
        			 * for updating actGrp
        			 */
        			List<BigInteger> processdetIds=processDetailsRepository.findIdByProcessIdAndTagType(result.getId(),"accountingRuleGroup");
                	log.info("processdetIds before:"+processdetIds);
                	if(processdetIds.size()>0)
                	{
                		for(int j=0;j<actGrpListMap.size();j++)
        				{
        					if(actGrpListMap.get(j).get("id")!= null )
        					{
		        				for(int id=0;id<processdetIds.size();id++)
		        				{
		        					
		        					if(processdetIds.get(id).longValue()==Long.valueOf(actGrpListMap.get(j).get("id").toString()))
		        					{
		        						processdetIds.remove(processdetIds.get(id));
		        					}
		        				}
        					}
        				}
                	}
                	log.info("processdetIds after:"+processdetIds);
            		for(int j=0;j<processdetIds.size();j++)
        			{
        				ProcessDetails processDet=processDetailsRepository.findOne(processdetIds.get(j).longValue());
        				if(processDet!=null)
        					processDetailsRepository.delete(processDet);
        			}
        			for(HashMap actGrp:actGrpListMap)
        			{
        				ProcessDetails procDet=new ProcessDetails();

        				if(actGrp.get("id")!=null)
        				{
        					procDet.setId(Long.valueOf(actGrp.get("id").toString()));
        					procDet.setCreatedBy(Long.valueOf(actGrp.get("createdBy").toString()));
        					procDet.setCreatedDate(ZonedDateTime.parse(actGrp.get("createdDate").toString()));

        				}
        				procDet.setProcessId(result.getId());
        				if(actGrp.get("tagType")!=null)
        					procDet.setTagType(actGrp.get("tagType").toString());
        				if(actGrp.get("typeId")!=null){
        					//actGrp.get("typeId").toString()
        					RuleGroup ruleGrpData=ruleGroupRepository.findByIdForDisplayAndTenantId(actGrp.get("typeId").toString(), tenantId);
        					procDet.setTypeId(ruleGrpData.getId());
        				}
        				if(actGrp.get("id")==null)
        					procDet.setCreatedBy(userId);
        				if(actGrp.get("id")==null)
        					procDet.setCreatedDate(ZonedDateTime.now());
        				procDet.setLastUpdatedBy(userId);
        				procDet.setLastUpdatedDate(ZonedDateTime.now());
        				procDetList.add(procDet);
        			}
        		}
        	}
        	if(processDetailListMap.get("recGrpList")!=null)
        	{
        		List<HashMap> recGrpListMap=(List<HashMap>) processDetailListMap.get("recGrpList");
        		log.info("recGrpListMap ::" + recGrpListMap);
        		if(recGrpListMap!=null && !recGrpListMap.isEmpty())
        		{
        			/**
        			 * for update recGrp
        			 */
        			List<BigInteger> processdetIds=processDetailsRepository.findIdByProcessIdAndTagType(result.getId(),"reconciliationRuleGroup");
                	log.info("processdetIds before:"+processdetIds);
                	if(processdetIds.size()>0)
                	{
                		for(int j=0;j<recGrpListMap.size();j++)
        				{
        					if(recGrpListMap.get(j).get("id")!= null )
        					{
		        				for(int id=0;id<processdetIds.size();id++)
		        				{
		        					
		        					if(processdetIds.get(id).longValue()==Long.valueOf(recGrpListMap.get(j).get("id").toString()))
		        					{
		        						processdetIds.remove(processdetIds.get(id));
		        					}
		        				}
        					}
        				}
                	}
                	log.info("processdetIds after:"+processdetIds);
            		for(int j=0;j<processdetIds.size();j++)
        			{
        				ProcessDetails processDet=processDetailsRepository.findOne(processdetIds.get(j).longValue());
        				if(processDet!=null)
        					processDetailsRepository.delete(processDet);
        			}
        			for(HashMap recGrp:recGrpListMap)
        			{
        				ProcessDetails procDet=new ProcessDetails();
        				if(recGrp.get("id")!=null)
        				{
        					procDet.setId(Long.valueOf(recGrp.get("id").toString()));
        					procDet.setCreatedBy(Long.valueOf(recGrp.get("createdBy").toString()));
        					procDet.setCreatedDate(ZonedDateTime.parse(recGrp.get("createdDate").toString()));

        				}
        				procDet.setProcessId(result.getId());
        				if(recGrp.get("tagType")!=null)
        					procDet.setTagType(recGrp.get("tagType").toString());
        				if(recGrp.get("typeId")!=null){
        					RuleGroup ruleGroupData=ruleGroupRepository.findByIdForDisplayAndTenantId(recGrp.get("typeId").toString(), tenantId);
        					procDet.setTypeId(ruleGroupData.getId());
        				}
        				if(recGrp.get("id")==null)
        					procDet.setCreatedBy(userId);
        				if(recGrp.get("id")==null)
        					procDet.setCreatedDate(ZonedDateTime.now());
        				procDet.setLastUpdatedBy(userId);
        				procDet.setLastUpdatedDate(ZonedDateTime.now());
        				procDetList.add(procDet);
        			}
        		}
        	}
        	
        	if(processDetailListMap.get("calendarList")!=null)
        	{
        		List<HashMap> calendarListMap=(List<HashMap>) processDetailListMap.get("calendarList");
        		log.info("calendarListMap ::" + calendarListMap);
        		if(calendarListMap!=null && !calendarListMap.isEmpty())
        		{
        			/**
        			 * for update calendar
        			 */
        			List<BigInteger> processdetIds=processDetailsRepository.findIdByProcessIdAndTagType(result.getId(),"calendar");
                	log.info("processdetIds before:"+processdetIds);
                	if(processdetIds.size()>0)
                	{
                		for(int j=0;j<calendarListMap.size();j++)
        				{
        					if(calendarListMap.get(j).get("id")!= null )
        					{
		        				for(int id=0;id<processdetIds.size();id++)
		        				{
		        					
		        					if(processdetIds.get(id).longValue()==Long.valueOf(calendarListMap.get(j).get("id").toString()))
		        					{
		        						processdetIds.remove(processdetIds.get(id));
		        					}
		        				}
        					}
        				}
                	}
                	log.info("processdetIds after:"+processdetIds);
                	
            		for(int j=0;j<processdetIds.size();j++)
        			{
        				ProcessDetails processDet=processDetailsRepository.findOne(processdetIds.get(j).longValue());
        				if(processDet!=null)
        					processDetailsRepository.delete(processDet);
        			}
        			for(HashMap calendar:calendarListMap)
        			{
        				ProcessDetails procDet=new ProcessDetails();
        				if(calendar.get("id")!=null)
        				{
        					procDet.setId(Long.valueOf(calendar.get("id").toString()));
        					procDet.setCreatedBy(Long.valueOf(calendar.get("createdBy").toString()));
        					procDet.setCreatedDate(ZonedDateTime.parse(calendar.get("createdDate").toString()));

        				}
        				procDet.setProcessId(result.getId());
        				if(calendar.get("tagType")!=null)
        					procDet.setTagType(calendar.get("tagType").toString());
        				if(calendar.get("typeId")!=null)
        				{
        					Calendar cal=calendarRepository.findByIdForDisplayAndTenantId(calendar.get("typeId").toString(), tenantId);
        					procDet.setTypeId(cal.getId());
        				}
        				if(calendar.get("id")==null)
        					procDet.setCreatedBy(userId);
        				if(calendar.get("id")==null)
        					procDet.setCreatedDate(ZonedDateTime.now());
        				procDet.setLastUpdatedBy(userId);
        				procDet.setLastUpdatedDate(ZonedDateTime.now());
        				procDetList.add(procDet);
        			}
        		}
        	}
        	if(processDetailListMap.get("violationList")!=null)
        	{
        		List<HashMap> violationListMap=(List<HashMap>) processDetailListMap.get("violationList");
        		log.info("violationListMap ::" + violationListMap);
        		if(violationListMap!=null && !violationListMap.isEmpty())
        		{
        			/**
        			 * for update calendar
        			 */
        			List<BigInteger> processdetIds=processDetailsRepository.findIdByProcessIdAndTagType(result.getId(),"violation");
                	log.info("processdetIds before:"+processdetIds);
                	if(processdetIds.size()>0)
                	{
                		for(int j=0;j<violationListMap.size();j++)
        				{
        					if(violationListMap.get(j).get("id")!= null )
        					{
		        				for(int id=0;id<processdetIds.size();id++)
		        				{
		        					if(processdetIds.get(id).longValue()==Long.valueOf(violationListMap.get(j).get("id").toString()))
		        					{
		        						processdetIds.remove(processdetIds.get(id));
		        					}
		        				}
        					}
        				}
                	}
                	log.info("processdetIds after:"+processdetIds);
            		for(int j=0;j<processdetIds.size();j++)
        			{
        				ProcessDetails processDet=processDetailsRepository.findOne(processdetIds.get(j).longValue());
        				if(processDet!=null)
        					processDetailsRepository.delete(processDet);
        			}
        			for(HashMap calendar:violationListMap)
        			{
        				ProcessDetails procDet=new ProcessDetails();
        				if(calendar.get("id")!=null)
        				{
        					procDet.setId(Long.valueOf(calendar.get("id").toString()));
        					procDet.setCreatedBy(Long.valueOf(calendar.get("createdBy").toString()));
        					procDet.setCreatedDate(ZonedDateTime.parse(calendar.get("createdDate").toString()));

        				}
        				procDet.setProcessId(result.getId());
        				if(calendar.get("tagType")!=null)
        					procDet.setTagType(calendar.get("tagType").toString());
        				if(calendar.get("typeId")!=null)
        					procDet.setTypeId(Long.valueOf(calendar.get("typeId").toString()));
        				if(calendar.get("id")==null)
        					procDet.setCreatedBy(userId);
        				if(calendar.get("id")==null)
        					procDet.setCreatedDate(ZonedDateTime.now());
        				procDet.setLastUpdatedBy(userId);
        				procDet.setLastUpdatedDate(ZonedDateTime.now());
        				procDetList.add(procDet);
        			}
        		}
        	}
        	processDetailsRepository.save(procDetList);
        	finalMap.put("id", result.getIdForDisplay());
        	finalMap.put("processName", result.getProcessName());

        }
		return finalMap;
    
       
    }
    
    /**
     * author:ravali
     * @param processId
     * @return
     */
    @GetMapping("/getprocessesesAndProcessDetails")
    @Timed
    public List<HashMap> getprocessesesAndProcessDetails(HttpServletRequest request, @RequestParam List<String> processIds){
    	log.debug("REST request to get ProcessesAndProcessDetails : {}", processIds);
    	
    	HashMap map0=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map0.get("tenantId").toString());
    	Long userId=Long.parseLong(map0.get("userId").toString());
    	
    	List<HashMap> processessList=new ArrayList<HashMap>();
    	for(String processId:processIds)
    	{
    		Processes prc = processesRepository.findByTenantIdAndIdForDisplay(tenantId, processId);
    		if(prc != null)
    		{
    	    	Processes proc=processesRepository.findOne(prc.getId());
    	    	HashMap finalMap=new HashMap();
    	    	finalMap.put("id", prc.getIdForDisplay());
    	    	if(proc.getProcessName()!=null)
    	    		finalMap.put("processName", proc.getProcessName());
    	    	if(proc.getStartDate()!=null)
    	    		finalMap.put("startDate", proc.getStartDate());
    	    	if(proc.getEndDate()!=null)
    	    		finalMap.put("endDate", proc.getEndDate());
    	    	if(proc.getDesc()!=null)
    	    		finalMap.put("desc", proc.getDesc());
    	    	if( proc.isEnable())
    	    		finalMap.put("enable", proc.isEnable());
    	    	if(proc.getTenantId()!=null)
    	    		finalMap.put("tenantId", proc.getTenantId());
    	    	if(proc.getCreatedBy()!=null)
    	    		finalMap.put("createdBy", proc.getCreatedBy());
    	    	if(proc.getCreatedDate()!=null)
    	    		finalMap.put("createdDate", proc.getCreatedDate());
    	    	if(proc.getLastUpdatedBy()!=null)
    	    		finalMap.put("lastUpdatedBy", proc.getLastUpdatedBy());
    	    	if(proc.getLastUpdatedDate()!=null)
    	    		finalMap.put("lastUpdatedDate", proc.getLastUpdatedDate());

    	    	if(proc!=null && proc.getId()!=null)
    	    	{
    	    		List<HashMap> processDetailListMap=new ArrayList<HashMap>();
    	    		List<ProcessDetails> procDetList=processDetailsRepository.findByProcessId(prc.getId());
    	    		List<HashMap> processDetailSourceProfileMap=new ArrayList<HashMap>();
    	    		List<HashMap> processDetailRecGrpMap=new ArrayList<HashMap>();
    	    		List<HashMap> processDetailActGrpMap=new ArrayList<HashMap>();
    	    		List<HashMap> processDetailCalMap=new ArrayList<HashMap>();
    	    		List<HashMap> processDetailViolationMap=new ArrayList<HashMap>();
    	
    	    		HashMap map=new HashMap();
    	    		for(ProcessDetails procDet:procDetList)
    	    		{
    	    			HashMap procDetMap=new HashMap();
    	    			
    	    			if(procDet.getId()!=null)
    	    				procDetMap.put("id", procDet.getId());
    	    			if(procDet.getProcessId()!=null)
    	    				procDetMap.put("processId", prc.getIdForDisplay());
    	    			if(procDet.getTagType()!=null)
    	    			{
    	    				procDetMap.put("tagType", procDet.getTagType());
    	    					procDetMap.put("typeId", procDet.getTypeId());
    	    				if(procDet.getTagType().equalsIgnoreCase("sourceProfile"))
    	    				{
    	
    	    					{
    	    						SourceProfiles sp=sourceProfilesRepository.findOne(procDet.getTypeId());
    	    						procDetMap.put("typeId", sp.getIdForDisplay());
    	    						procDetMap.put("sourceProfileName", sp.getSourceProfileName());
    	    					}
    	    				}
    	    				else if(procDet.getTagType().equalsIgnoreCase("calendar"))
    	    				{
    	    					Calendar cal=calendarRepository.findOne(procDet.getTypeId());
    	    					procDetMap.put("calendarName", cal.getName());
    	    					procDetMap.put("typeId", cal.getIdForDisplay());
    	    				}
    	    				else if(procDet.getTagType().equalsIgnoreCase("reconciliationRuleGroup") || procDet.getTagType().equalsIgnoreCase("accountingRuleGroup"))
    	    				{
    	    					RuleGroup ruleGrp=ruleGroupRepository.findOne(procDet.getTypeId());
    	    					procDetMap.put("typeId", ruleGrp.getIdForDisplay());
    	    					if(procDet.getTagType().equalsIgnoreCase("reconciliationRuleGroup"))
    	    						procDetMap.put("reconcRuleGroupName", ruleGrp.getName());
    	    					else
    	    						procDetMap.put("actRuleGroupName", ruleGrp.getName());
    	    				}
    	    			}
    	
    	    			if(procDet.getCreatedBy()!=null)
    	    				procDetMap.put("createdBy", procDet.getCreatedBy());
    	    			if(proc.getCreatedDate()!=null)
    	    				procDetMap.put("createdDate", procDet.getCreatedDate());
    	    			if(proc.getLastUpdatedBy()!=null)
    	    				procDetMap.put("lastUpdatedBy", procDet.getLastUpdatedBy());
    	    			if(proc.getLastUpdatedDate()!=null)
    	    				procDetMap.put("lastUpdatedDate", procDet.getLastUpdatedDate());
    	
    	    			if(procDet.getTagType().equalsIgnoreCase("sourceProfile"))
    	    				processDetailSourceProfileMap.add(procDetMap);
    	    			else if(procDet.getTagType().equalsIgnoreCase("reconciliationRuleGroup"))
    	    				processDetailRecGrpMap.add(procDetMap);
    	    			else if(procDet.getTagType().equalsIgnoreCase("calendar"))
    	    				processDetailCalMap.add(procDetMap);
    	    			else if(procDet.getTagType().equalsIgnoreCase("violation"))
    	    				processDetailViolationMap.add(procDetMap);
    	    			else 
    	    				processDetailActGrpMap.add(procDetMap);
    	    			
    	    		}
    	
    	    		map.put("violationList", processDetailViolationMap);
    	    		map.put("calenderList", processDetailCalMap);
    	    		map.put("spList", processDetailSourceProfileMap);
    	
    	    		map.put("recGrpList", processDetailRecGrpMap);
    	
    	    		map.put("actGrpList", processDetailActGrpMap);
    	    		finalMap.put("processDetailList", map);
    	    	}
    	    	processessList.add(finalMap);
    		}
    	}
    	return processessList;
    }
    
    
    
    /**
     * author:ravali
     * Bhagath
     * @param procAndDetailList
     * @param tenantId
     * @param userId
     */    
    @PostMapping("/insertProcessesAndProcessDetails")
    @Timed
    public void insertProcessesAndProcessDetails(HttpServletRequest request,@RequestBody List<HashMap> procAndDetailList/*, @RequestParam Long tenantId,@RequestParam Long userId*/){
        log.debug("REST request to save ProcessesAndProcessDetails : {}", procAndDetailList);
    	HashMap map = userJdbcService.getuserInfoFromToken(request);
    	Long tenantId = Long.parseLong(map.get("tenantId").toString());
    	Long userId = Long.parseLong(map.get("userId").toString());
        Processes proc=new Processes();
        for(HashMap procAndDetailMap:procAndDetailList)
	    {
	        HashMap finalMap=new HashMap();
	        if(procAndDetailMap.get("id")!=null)
	        {
	        	Processes prc = processesRepository.findByTenantIdAndIdForDisplay(tenantId, procAndDetailMap.get("id").toString());
	        	if(prc != null)
	        	{
		        	proc.setId(prc.getId());	        		
	        	}
	        }
	        if(procAndDetailMap.get("processName")!=null)
	        	proc.setProcessName(procAndDetailMap.get("processName").toString());
	        if(procAndDetailMap.get("startDate")!=null)
	        {
	        	ZonedDateTime stDate=ZonedDateTime.parse(procAndDetailMap.get("startDate").toString());
	        	proc.setStartDate(stDate);
	        }
	        if(procAndDetailMap.get("endDate")!=null)
	        {
	        	ZonedDateTime toDate=ZonedDateTime.parse(procAndDetailMap.get("endDate").toString());
	        	proc.setEndDate(toDate);
	        }
	        if(procAndDetailMap.get("desc")!=null)
	        	proc.setDesc(procAndDetailMap.get("desc").toString());
	        proc.setEnable(true);
	        proc.setTenantId(tenantId);
	        proc.setCreatedBy(userId);
	        proc.setCreatedDate(ZonedDateTime.now());
	        proc.setLastUpdatedBy(userId);
	        proc.setLastUpdatedDate(ZonedDateTime.now());
	        Processes result = processesRepository.save(proc);
	        
			String idForDisplay = IDORUtils.computeFrontEndIdentifier(result.getId().toString());
			result.setIdForDisplay(idForDisplay);
			result = processesRepository.save(result);
			
	        if(result!=null && result.getId()!=null)
	        {
	        	HashMap ProDet=  (HashMap) procAndDetailMap.get("processDetailList");
	        	List<ProcessDetails> procDetList=new ArrayList<ProcessDetails>();
	        	//for(HashMap ProDet:processDetailListMap)
	        	//{
	        		ProcessDetails procDet=new ProcessDetails();
	                procDet.setProcessId(result.getId());
	                if(ProDet.get("id") != null)
	                {
	                	Processes process = processesRepository.findByTenantIdAndIdForDisplay(tenantId, ProDet.get("id").toString());
	                	if(process != null)
	                	{
	                		procDet.setProcessId(process.getId());
	                	}
	                }
	        		if(ProDet.get("tagType")!=null)
	        			procDet.setTagType(ProDet.get("tagType").toString());
	        		if(ProDet.get("typeId")!=null)
	        			procDet.setTypeId(Long.valueOf(ProDet.get("typeId").toString()));
	        		procDet.setCreatedBy(userId);
	        		procDet.setCreatedDate(ZonedDateTime.now());
	        		procDet.setLastUpdatedBy(userId);
	        		procDet.setLastUpdatedDate(ZonedDateTime.now());
	        		procDetList.add(procDet);
	        	//}
	        	processDetailsRepository.save(procDetList);
	        	finalMap.put("id", result.getIdForDisplay());
	        	finalMap.put("processName", result.getProcessName());
	        }
	    }
    }
    
    
    /**
     * author:ravali
     * @param name
     * @return
     */
    
    @GetMapping("/processIsExists")
	@Timed
	public HashMap processName(HttpServletRequest request, @RequestParam String name, @RequestParam(required=false,value="id") String id)
	{
    	HashMap map0=userJdbcService.getuserInfoFromToken(request);
      	Long tenantId=Long.parseLong(map0.get("tenantId").toString());
    	
		HashMap map=new HashMap();
		map.put("result", "No Duplicates Found");
		
		if(id!=null)
		{
			List<Processes> processName=processesRepository.findByIdForDisplayAndProcessNameAndTenantId(id,name,tenantId);
			List<Processes> process=processesRepository.findByProcessNameAndTenantId(name,tenantId);
			if(processName.size()==1)
				map.put("result", "No Duplicates Found");
			if(process.size()!=0 && !(id.equalsIgnoreCase(process.get(0).getIdForDisplay())))
			{
			if(process.size()>0)
				map.put("result", "'"+name+"' processName already exists");
			}
		}
		else
		{
		List<Processes> processName=processesRepository.findByProcessNameAndTenantId(name,tenantId);
		if(processName.size()>0)
		{
			map.put("result", "'"+name+"' processName already exists");
		}
		}
		return map;
	}
    
}
