package com.nspl.app.web.rest;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

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
import com.nspl.app.domain.Periods;
import com.nspl.app.repository.CalendarRepository;
import com.nspl.app.repository.PeriodsRepository;
import com.nspl.app.service.UserJdbcService;
import com.nspl.app.web.rest.dto.PeriodsDTO;
import com.nspl.app.web.rest.util.HeaderUtil;
import com.nspl.app.web.rest.util.PaginationUtil;

/**
 * REST controller for managing Periods.
 */
@RestController
@RequestMapping("/api")
public class PeriodsResource {

    private final Logger log = LoggerFactory.getLogger(PeriodsResource.class);

    private static final String ENTITY_NAME = "periods";
        
    private final PeriodsRepository periodsRepository;
    
    @Inject
    CalendarRepository calendarRepository;
    
    @Inject
	UserJdbcService userJdbcService;

    public PeriodsResource(PeriodsRepository periodsRepository) {
        this.periodsRepository = periodsRepository;
    }

    /**
     * POST  /periods : Create a new periods.
     *
     * @param periods the periods to create
     * @return the ResponseEntity with status 201 (Created) and with body the new periods, or with status 400 (Bad Request) if the periods has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/periods")
    @Timed
    public ResponseEntity<Periods> createPeriods(@RequestBody Periods periods) throws URISyntaxException {
        log.debug("REST request to save Periods : {}", periods);
        if (periods.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new periods cannot already have an ID")).body(null);
        }
        Periods result = periodsRepository.save(periods);
        return ResponseEntity.created(new URI("/api/periods/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /periods : Updates an existing periods.
     *
     * @param periods the periods to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated periods,
     * or with status 400 (Bad Request) if the periods is not valid,
     * or with status 500 (Internal Server Error) if the periods couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/periods")
    @Timed
    public PeriodsDTO updatePeriods(HttpServletRequest request,@RequestBody PeriodsDTO periods) throws URISyntaxException {
    	
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long userId=Long.parseLong(map.get("userId").toString());
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	PeriodsDTO finalResult = new PeriodsDTO();
    	Long id = 0L;
        log.debug("REST request to update Periods of user: "+userId+"-> "+ periods);
        if (periods.getId() == null) {
        	Periods period = new Periods();
        	Calendar cal = calendarRepository.findByIdForDisplayAndTenantId(periods.getCalId(), tenantId);
        	period.setCalId(cal.getId());
        	period.setPeriodName(periods.getPeriodName());
        	period.setFromDate(periods.getFromDate());
        	period.setToDate(periods.getToDate());
        	period.setPeriodNum(periods.getPeriodNum());
        	period.setQuarter(periods.getQuarter());
        	period.setYear(periods.getYear());
        	period.setAdjustment(periods.getAdjustment());
        	period.setCreatedBy(periods.getCreatedBy());
        	period.setCreatedDate(periods.getCreatedDate());
        	period.setLastUpdatedBy(userId);
        	period.setLastUpdatedDate(ZonedDateTime.now());
        	Periods savePeriods = periodsRepository.save(period);
        	id = savePeriods.getId();
        }
        else
        {
        	Periods periodsUpdated = periodsRepository.findOne(periods.getId()); 
        	periodsUpdated.setPeriodName(periods.getPeriodName());
        	periodsUpdated.setFromDate(periods.getFromDate());
        	periodsUpdated.setToDate(periods.getToDate());
        	periodsUpdated.setPeriodNum(periods.getPeriodNum());
        	periodsUpdated.setQuarter(periods.getQuarter());
        	periodsUpdated.setYear(periods.getYear());
        	periodsUpdated.setAdjustment(periods.getAdjustment());
        	periodsUpdated.setEnabledFlag(periods.getEnabledFlag());
        	periodsUpdated.setStatus(periods.getStatus());
        	periodsUpdated.setLastUpdatedBy(userId);
        	periodsUpdated.setLastUpdatedDate(ZonedDateTime.now());
            Periods result = periodsRepository.save(periodsUpdated);
            id = result.getId();
        }
        Periods p = periodsRepository.findOne(id);
        if(p != null)
        {
        	finalResult.setId(p.getId());
        	Calendar cal = calendarRepository.findOne(p.getCalId());
        	if(cal != null)
        	{
        		finalResult.setCalId(cal.getIdForDisplay());
        	}
        	finalResult.setPeriodName(p.getPeriodName());
        	finalResult.setFromDate(p.getFromDate());
        	finalResult.setToDate(p.getToDate());
        	finalResult.setPeriodNum(p.getPeriodNum());
        	finalResult.setQuarter(p.getQuarter());
        	finalResult.setYear(p.getYear());
        	finalResult.setAdjustment(p.isAdjustment());
        	finalResult.setCreatedBy(p.getCreatedBy());
        	finalResult.setLastUpdatedBy(p.getLastUpdatedBy());
        	finalResult.setCreatedDate(p.getCreatedDate());
        	finalResult.setLastUpdatedDate(p.getLastUpdatedDate());
        	finalResult.setEnabledFlag(p.isEnabledFlag());
        	finalResult.setStatus(p.getStatus());
        }
        return finalResult;
    }
    
    /**
     * GET  /ledgerPeriods : get all the periods for selected ledgers.
     *
     * @return the status 200 (OK) and the list of periods in body
     */
    @GetMapping("/getPeriodsByLedgerName")
    @Timed
    public ArrayList<HashMap> getPeriodsByLedgerName(HttpServletRequest request,@RequestParam String ldgrName){
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long userId=Long.parseLong(map.get("userId").toString());
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	ArrayList<HashMap> finalList=new ArrayList<HashMap>();
    	ArrayList<Object[]> periodsList=periodsRepository.getPeriodsByLedgerName(tenantId, ldgrName);
    	for(Object[] eachRow:periodsList) {
    		HashMap newMap=new HashMap();
    		newMap.put("periodName",(String) eachRow[1]);
    		newMap.put("calId", eachRow[0]);
    		finalList.add(newMap);
    	}
    	
    	return finalList;
    }

    /**
     * GET  /periods : get all the periods.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of periods in body
     */
    @GetMapping("/periods")
    @Timed
    public ResponseEntity<List<Periods>> getAllPeriods(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Periods");
        Page<Periods> page = periodsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/periods");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /periods/:id : get the "id" periods.
     *
     * @param id the id of the periods to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the periods, or with status 404 (Not Found)
     */
    @GetMapping("/periods/{id}")
    @Timed
    public ResponseEntity<Periods> getPeriods(@PathVariable Long id) {
        log.debug("REST request to get Periods : {}", id);
        Periods periods = periodsRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(periods));
    }

    /**
     * DELETE  /periods/:id : delete the "id" periods.
     *
     * @param id the id of the periods to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/periods/{id}")
    @Timed
    public ResponseEntity<Void> deletePeriods(@PathVariable Long id) {
        log.debug("REST request to delete Periods : {}", id);
        periodsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
