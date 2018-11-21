package com.nspl.app.web.rest;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.formula.functions.Now;
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
import com.nspl.app.domain.Periods;
import com.nspl.app.domain.ProcessDetails;
import com.nspl.app.domain.Processes;
import com.nspl.app.repository.PeriodsRepository;
import com.nspl.app.repository.ProcessDetailsRepository;
import com.nspl.app.repository.ProcessesRepository;
import com.nspl.app.service.UserJdbcService;
import com.nspl.app.web.rest.util.HeaderUtil;
import com.nspl.app.web.rest.util.PaginationUtil;

/**
 * REST controller for managing ProcessDetails.
 */
@RestController
@RequestMapping("/api")
public class ProcessDetailsResource {

    private final Logger log = LoggerFactory.getLogger(ProcessDetailsResource.class);

    private static final String ENTITY_NAME = "processDetails";

    private final ProcessDetailsRepository processDetailsRepository;
    
    @Inject
    PeriodsRepository periodsRepository;
    
    @Inject
    UserJdbcService userJdbcService;
    
    @Inject
    ProcessesRepository processesRepository;

    public ProcessDetailsResource(ProcessDetailsRepository processDetailsRepository) {
        this.processDetailsRepository = processDetailsRepository;
    }

    /**
     * POST  /process-details : Create a new processDetails.
     *
     * @param processDetails the processDetails to create
     * @return the ResponseEntity with status 201 (Created) and with body the new processDetails, or with status 400 (Bad Request) if the processDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/process-details")
    @Timed
    public ResponseEntity<ProcessDetails> createProcessDetails(@RequestBody ProcessDetails processDetails) throws URISyntaxException {
        log.debug("REST request to save ProcessDetails : {}", processDetails);
        if (processDetails.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new processDetails cannot already have an ID")).body(null);
        }
        ProcessDetails result = processDetailsRepository.save(processDetails);
        return ResponseEntity.created(new URI("/api/process-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    
    /**
     * author:ravali
     * @param processDetailsList
     * @return
     * @throws URISyntaxException
     */
    
    @PostMapping("/processDetailsList")
    @Timed
    public List<ProcessDetails> createProcessDetailsList(@RequestBody List<ProcessDetails> processDetailsList,@RequestParam Long userId) throws URISyntaxException {
        log.debug("REST request to save ProcessDetails :", processDetailsList);
        List<ProcessDetails> result=new ArrayList<ProcessDetails>();
        for(ProcessDetails processDetails:processDetailsList)
        {
        	processDetails.setCreatedBy(userId);
        	processDetails.setCreatedDate(ZonedDateTime.now());
        	processDetails.setLastUpdatedBy(userId);
        	processDetails.setLastUpdatedDate(ZonedDateTime.now());
        	result.add(processDetails);
        
        }
        result= processDetailsRepository.save(processDetailsList);
		return result;
      
    }

    /**
     * PUT  /process-details : Updates an existing processDetails.
     *
     * @param processDetails the processDetails to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated processDetails,
     * or with status 400 (Bad Request) if the processDetails is not valid,
     * or with status 500 (Internal Server Error) if the processDetails couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/process-details")
    @Timed
    public ResponseEntity<ProcessDetails> updateProcessDetails(@RequestBody ProcessDetails processDetails) throws URISyntaxException {
        log.debug("REST request to update ProcessDetails : {}", processDetails);
        if (processDetails.getId() == null) {
            return createProcessDetails(processDetails);
        }
        ProcessDetails result = processDetailsRepository.save(processDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, processDetails.getId().toString()))
            .body(result);
    }

    /**
     * GET  /process-details : get all the processDetails.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of processDetails in body
     */
    @GetMapping("/process-details")
    @Timed
    public ResponseEntity<List<ProcessDetails>> getAllProcessDetails(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of ProcessDetails");
        Page<ProcessDetails> page = processDetailsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/process-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /process-details/:id : get the "id" processDetails.
     *
     * @param id the id of the processDetails to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the processDetails, or with status 404 (Not Found)
     */
    @GetMapping("/process-details/{id}")
    @Timed
    public ResponseEntity<ProcessDetails> getProcessDetails(@PathVariable Long id) {
        log.debug("REST request to get ProcessDetails : {}", id);
        ProcessDetails processDetails = processDetailsRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(processDetails));
    }

    /**
     * DELETE  /process-details/:id : delete the "id" processDetails.
     *
     * @param id the id of the processDetails to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/process-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteProcessDetails(@PathVariable Long id) {
        log.debug("REST request to delete ProcessDetails : {}", id);
        processDetailsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    @GetMapping("/processDetailsForCalender")
    @Timed
    public LinkedHashMap getCalenderDetailsForAProcess(HttpServletRequest request,@RequestParam String processId) {
        log.debug("REST request to get ProcessDetails : ", processId);
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
      	Long tenantId=Long.parseLong(map.get("tenantId").toString());

        LinkedHashMap finalMap=new LinkedHashMap();
        
        Processes processes = processesRepository.findByTenantIdAndIdForDisplay(tenantId, processId);
        
        ProcessDetails processDetails = processDetailsRepository.findByProcessIdAndTagType(processes.getId(), "calendar");
        if(processDetails!=null && processDetails.getTypeId()!=null)
        {
        processDetails.getTypeId();
        log.info("LocalDate.now() :"+LocalDate.now());
        ZonedDateTime currentDate=ZonedDateTime.now();
        LocalDate currentDateL=LocalDate.now();
        finalMap.put("currentdate", currentDateL.toString());
        String month=String.valueOf(LocalDate.now().getMonthValue());
        log.info("month :"+month);
        if(month.length()<2)
        	month="0"+month;
        log.info("month1 :"+month);
        finalMap.put("startDate", LocalDate.now().getYear()+"-"+month+"-01");
        List<LinkedHashMap> calenderLov=new ArrayList<LinkedHashMap>();
        List<Periods> periodsOfCalender=periodsRepository.findByCalIdAndYearOrderByFromDateDesc(processDetails.getTypeId(),LocalDate.now().getYear());
        for(Periods calPeriod:periodsOfCalender)
        {
        	LinkedHashMap calender=new LinkedHashMap();

        	
        	if(calPeriod.getFromDate().isAfter(currentDate.minusMonths(1)))
        	{
        		//log.info("fmDate "+calPeriod.getFromDate()+" is after "+currentDate.minusMonths(1));
        	}
        	else
        	{
        		//log.info("fmDate "+calPeriod.getFromDate()+" is before "+currentDate.minusMonths(1));
        		calender.put("periodName", calPeriod.getPeriodName());
            	calender.put("fromDate", calPeriod.getFromDate());
            	calenderLov.add(calender);
        	}
        
        }
   
        finalMap.put("calendar", calenderLov);
        }
		return finalMap;
        
    }
    
    
}
