package com.nspl.app.web.rest;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.nspl.app.domain.LookUpCode;
import com.nspl.app.domain.Periods;
import com.nspl.app.exception.CustomException;
import com.nspl.app.repository.CalendarRepository;
import com.nspl.app.repository.LookUpCodeRepository;
import com.nspl.app.repository.PeriodsRepository;
import com.nspl.app.security.IDORUtils;
import com.nspl.app.service.ActiveStatusService;
import com.nspl.app.service.FileExportService;
import com.nspl.app.service.UserJdbcService;
import com.nspl.app.web.rest.dto.CalendarDTO;
import com.nspl.app.web.rest.util.HeaderUtil;
import com.nspl.app.web.rest.util.PaginationUtil;

/**
 * REST controller for managing Calendar.
 */
@RestController
@RequestMapping("/api")
public class CalendarResource {

    private final Logger log = LoggerFactory.getLogger(CalendarResource.class);

    private static final String ENTITY_NAME = "calendar";
        
    private final CalendarRepository calendarRepository;
    
    
    @Inject
    PeriodsRepository periodsRepository;
    
    @Inject
    LookUpCodeRepository lookUpCodeRepository;
    
    @Inject
	UserJdbcService userJdbcService;
    
    
    @Inject
    FileExportService fileExportService;
    
    @Inject
    ActiveStatusService activeStatusService;
    
    
  //  @Inject
   // PDFGenerator PDFGenerator;

    public CalendarResource(CalendarRepository calendarRepository) {
        this.calendarRepository = calendarRepository;
    }

    /**
     * POST  /calendars : Create a new calendar.
     *
     * @param calendar the calendar to create
     * @return the ResponseEntity with status 201 (Created) and with body the new calendar, or with status 400 (Bad Request) if the calendar has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/calendars")
    @Timed
    public ResponseEntity<CalendarDTO> createCalendar(HttpServletRequest request, @RequestBody CalendarDTO calendar) throws URISyntaxException {
        log.debug("REST request to save Calendar : {}", calendar);
    	HashMap map = userJdbcService.getuserInfoFromToken(request);
    	Long tenantId = Long.parseLong(map.get("tenantId").toString());
    	Long userId = Long.parseLong(map.get("userId").toString());
        if (calendar.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new calendar cannot already have an ID")).body(null);
        }
        Calendar createCalendar = new Calendar();
        if(calendar.getName() != null)
        	createCalendar.setName(calendar.getName());
        if(calendar.getDescription() != null)
        	createCalendar.setDescription(calendar.getDescription());
        if(calendar.getCalendarType() != null)
        	createCalendar.setCalendarType(calendar.getCalendarType());
        if(calendar.getStartDate() != null)
        	createCalendar.setStartDate(calendar.getStartDate());
        if(calendar.getEndDate() != null)
        	createCalendar.setEndDate(calendar.getEndDate());
        	createCalendar.setEnabledFlag(calendar.isEnabledFlag());
        	createCalendar.setCreatedBy(userId);
        	createCalendar.setLastUpdatedBy(userId);
        	createCalendar.setCreatedDate(ZonedDateTime.now());
        	createCalendar.setLastUpdatedDate(ZonedDateTime.now());        
        Calendar result = calendarRepository.save(createCalendar);
        
		String idForDisplay = IDORUtils.computeFrontEndIdentifier(result.getId().toString());
		result.setIdForDisplay(idForDisplay);
		result = calendarRepository.save(result);
		
        CalendarDTO dto = new CalendarDTO();
        
		dto.setId(result.getIdForDisplay());
		dto.setName(result.getName());
		dto.setDescription(result.getDescription());
		dto.setCalendarType(result.getCalendarType());
		dto.setStartDate(result.getStartDate());
		dto.setEndDate(result.getEndDate());
		dto.setEnabledFlag(result.isEnabledFlag());
		dto.setCreatedBy(result.getCreatedBy());
		dto.setCreatedDate(result.getCreatedDate());
		dto.setLastUpdatedBy(result.getLastUpdatedBy());
		dto.setLastUpdatedDate(result.getLastUpdatedDate());
        
        return ResponseEntity.created(new URI("/api/calendars/" + dto.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, dto.getId().toString()))
            .body(dto);
    }

    /**
     * PUT  /calendars : Updates an existing calendar.
     *
     * @param calendar the calendar to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated calendar,
     * or with status 400 (Bad Request) if the calendar is not valid,
     * or with status 500 (Internal Server Error) if the calendar couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/calendars")
    @Timed
    public ResponseEntity<CalendarDTO> updateCalendar(HttpServletRequest request,@RequestBody CalendarDTO calendar) throws URISyntaxException {
    	
    	HashMap map = userJdbcService.getuserInfoFromToken(request);
    	Long tenantId = Long.parseLong(map.get("tenantId").toString());
    	Long userId = Long.parseLong(map.get("userId").toString());
        log.debug("REST request to update Calendar of user: "+userId+"-> ", calendar);
        if (calendar.getId() == null) {
            return createCalendar(request, calendar);
        }
        Calendar calUpdate = calendarRepository.findByIdForDisplayAndTenantId(calendar.getId(), tenantId);
        if(calendar.getName() != null)
        	calUpdate.setName(calendar.getName());
        if(calendar.getDescription() != null)
        	calUpdate.setDescription(calendar.getDescription());
        if(calendar.getCalendarType() != null)
        	calUpdate.setCalendarType(calendar.getCalendarType());
        if(calendar.getStartDate() != null)
        	calUpdate.setStartDate(calendar.getStartDate());
        if(calendar.getEndDate() != null)
        	calUpdate.setEndDate(calendar.getEndDate());
        	calUpdate.setEnabledFlag(calendar.isEnabledFlag());
        	calUpdate.setLastUpdatedBy(userId);
        	calUpdate.setLastUpdatedDate(ZonedDateTime.now());
        Calendar result = calendarRepository.save(calUpdate);
        CalendarDTO dto = new CalendarDTO();
        
		dto.setId(result.getIdForDisplay());
		dto.setName(result.getName());
		dto.setDescription(result.getDescription());
		dto.setCalendarType(result.getCalendarType());
		dto.setStartDate(result.getStartDate());
		dto.setEndDate(result.getEndDate());
		dto.setEnabledFlag(result.isEnabledFlag());
		dto.setCreatedBy(result.getCreatedBy());
		dto.setCreatedDate(result.getCreatedDate());
		dto.setLastUpdatedBy(result.getLastUpdatedBy());
		dto.setLastUpdatedDate(result.getLastUpdatedDate());
		
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dto.getId().toString()))
            .body(dto);
    }

    /**
     * GET  /calendars : get all the calendars.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of calendars in body
     */
    @GetMapping("/calendars")
    @Timed
    public ResponseEntity<List<Calendar>> getAllCalendars(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Calendars");
        Page<Calendar> page = calendarRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/calendars");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /calendars/:id : get the "id" calendar.
     *
     * @param id the id of the calendar to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the calendar, or with status 404 (Not Found)
     */
    @GetMapping("/calendars/{id}")
    @Timed
    public ResponseEntity<Calendar> getCalendar(@PathVariable Long id) {
        log.debug("REST request to get Calendar : {}", id);
        Calendar calendar = calendarRepository.findOne(id);
        if(calendar == null)
        {
        	throw new CustomException(id,"record with id "+id+" not present");
        }
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(calendar));
    }

    /**
     * DELETE  /calendars/:id : delete the "id" calendar.
     *
     * @param id the id of the calendar to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/calendars/{id}")
    @Timed
    public ResponseEntity<Void> deleteCalendar(@PathVariable Long id) {
        log.debug("REST request to delete Calendar : {}", id);
        calendarRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * author:ravali
     * @param offset
     * @param limit
     * @param tenantId
     * @param response
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/getAllcalenderList")
 	@Timed
 	public ResponseEntity<List<CalendarDTO>> getRuleGroupsByTenantId(HttpServletRequest request,@RequestParam(value = "page" , required = false) Integer offset,
 			@RequestParam(value = "per_page", required = false) Integer limit,HttpServletResponse response,@RequestParam(value = "activeFlag" , required = false) Boolean activeFlag) throws URISyntaxException {
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
 		log.debug("REST request to get a page of Calendar "+activeFlag+" for tenant: "+tenantId);
 		
 		List<CalendarDTO> finalList = new ArrayList<CalendarDTO>();
 		
 		List<Calendar> calendarList = new ArrayList<Calendar>();
 		PaginationUtil paginationUtil=new PaginationUtil();
 		
 		int maxlmt=paginationUtil.MAX_LIMIT;
 		int minlmt=paginationUtil.MIN_OFFSET;
 		log.info("maxlmt: "+maxlmt);
 		Page<Calendar> page = null;
 		HttpHeaders headers = null;
 		
 		List<Calendar> calendarListCnt = calendarRepository.findByTenantIdOrderByIdDesc(tenantId);
    	response.addIntHeader("X-COUNT", calendarListCnt.size());
 		
 		if(limit==null || limit<minlmt){
 			if(activeFlag!=null)
 			{
 				calendarList = calendarRepository.findByTenantIdAndEnabledFlagIsTrue(tenantId);
 			}
 			else
 			{
 			calendarList = calendarRepository.findByTenantIdOrderByIdDesc(tenantId);
 			}
 			limit = calendarList.size();
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
 			 page = calendarRepository.findByTenantIdOrderByIdDesc(tenantId,PaginationUtil.generatePageRequest2(offset, limit));
 			headers = PaginationUtil.generatePaginationHttpHeaders2(page, "/api/getAllcalenderList",offset, limit);
 		}
 		else{
 			log.info("input limit is within maxlimit");
 			if(activeFlag!=null)
 			{
 				page = calendarRepository.findByTenantIdAndEnabledFlagIsTrue(tenantId,PaginationUtil.generatePageRequest(offset, limit));
 	 			headers = PaginationUtil.generatePaginationHttpHeaderss(page, "/api/getAllcalenderList", offset, limit);
 			}
 			else
 			{
 			page = calendarRepository.findByTenantIdOrderByIdDesc(tenantId,PaginationUtil.generatePageRequest(offset, limit));
 			headers = PaginationUtil.generatePaginationHttpHeaderss(page, "/api/getAllcalenderList", offset, limit);
 			}
 		}
     	if(page.getSize()>0)
     	{
     		for(Calendar cal : page)
     		{
     			CalendarDTO dto = new CalendarDTO();
     			dto.setId(cal.getIdForDisplay());
     			dto.setName(cal.getName());
     			dto.setDescription(cal.getDescription());
     			dto.setCalendarType(cal.getCalendarType());
     			dto.setStartDate(cal.getStartDate());
     			dto.setEndDate(cal.getEndDate());
     			/** active check**/
    			Boolean activeStatus=activeStatusService.activeStatus(cal.getStartDate(), cal.getEndDate(), cal.isEnabledFlag());
    			dto.setEnabledFlag(activeStatus);
     		
     			dto.setCreatedBy(cal.getCreatedBy());
     			dto.setCreatedDate(cal.getCreatedDate());
     			dto.setLastUpdatedBy(cal.getLastUpdatedBy());
     			dto.setLastUpdatedDate(cal.getLastUpdatedDate());
     			finalList.add(dto);
     		}
     	}
 		return new ResponseEntity<>(finalList, headers, HttpStatus.OK);
     }
    
    /**
     * author:ravali
     * @param id
     * @return
     * @throws IOException 
     */
    @GetMapping("/getCalenderAndItsPeriods")
 	@Timed
 	public HashMap getCalenderAndItsPeriods(HttpServletRequest request, @RequestParam String id,@RequestParam(value="fileExport",required=false) boolean fileExport,@RequestParam(value="fileType",required=false) String fileType,HttpServletResponse response) throws IOException
 	{
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());

    	HashMap calMap=new HashMap();
    	Calendar calendar = calendarRepository.findByIdForDisplayAndTenantId(id, tenantId);
    	Calendar cal=calendarRepository.findOne(calendar.getId());
    	calMap.put("id", cal.getIdForDisplay());
    	calMap.put("calendarType", cal.getCalendarType());
    	calMap.put("createdBy", cal.getCreatedBy());
    	calMap.put("createdDate", cal.getCreatedDate());
    	calMap.put("description", cal.getDescription());
    	calMap.put("enabledFlag", cal.isEnabledFlag());
    	calMap.put("endDate", cal.getEndDate());
    	calMap.put("lastUpdatedBy", cal.getLastUpdatedBy());
    	calMap.put("name", cal.getName());
    	calMap.put("startDate", cal.getStartDate());
    	calMap.put("tenantId", cal.getTenantId());
    	
    	List<LinkedHashMap> periodMapList=new ArrayList<LinkedHashMap>();
    	List<Periods> periodsList=periodsRepository.findByCalId(calendar.getId());
    	
    	for(Periods periods:periodsList)
    	{
    		//log.info("periods :"+periods);
    		LinkedHashMap periodMap=new LinkedHashMap();
    		
    		if(fileExport)
    		{
    		periodMap.put("Name", cal.getName());
    		periodMap.put("Description", cal.getDescription());
    		periodMap.put("Start Date", cal.getStartDate());
    		periodMap.put("Calendar Type", cal.getCalendarType());
    		
    		periodMap.put("id", periods.getId());
    		periodMap.put("Period Name", periods.getPeriodName());
    		periodMap.put("status", periods.getStatus());
    		periodMap.put("From Date", periods.getFromDate());
      		periodMap.put("To Date", periods.getToDate());
      		periodMap.put("Period Number", periods.getPeriodNum());
    		periodMap.put("Quarter", periods.getQuarter());
    		periodMap.put("Year", periods.getYear());
    		periodMap.put("Adjustment", periods.isAdjustment());
    		
    		periodMap.put("calId", calendar.getIdForDisplay());
    		
    		periodMap.put("createdBy", periods.getCreatedBy());
    		periodMap.put("createdDate", periods.getCreatedDate());
    		periodMap.put("lastUpdatedBy", periods.getLastUpdatedBy());
    		periodMap.put("lastUpdatedDate", periods.getLastUpdatedDate());
    		
    		
    		}
    		else
    		{
    			periodMap.put("calendarName", cal.getName());
        		periodMap.put("description", cal.getDescription());
        		periodMap.put("startDate", cal.getStartDate());
        		periodMap.put("calendarType", cal.getCalendarType());
        		periodMap.put("id", periods.getId());
        		periodMap.put("periodName", periods.getPeriodName());
        		periodMap.put("fromDate", periods.getFromDate());
          		periodMap.put("toDate", periods.getToDate());
          		periodMap.put("periodNum", periods.getPeriodNum());
        		periodMap.put("quarter", periods.getQuarter());
        		periodMap.put("year", periods.getYear());
        		periodMap.put("adjustment", periods.isAdjustment());
        		
        		periodMap.put("calId", calendar.getIdForDisplay());
        		
        		periodMap.put("createdBy", periods.getCreatedBy());
        		periodMap.put("createdDate", periods.getCreatedDate());
        		periodMap.put("lastUpdatedBy", periods.getLastUpdatedBy());
        		periodMap.put("lastUpdatedDate", periods.getLastUpdatedDate());
        		periodMap.put("status", periods.getStatus());
        		periodMap.put("enabledFlag", periods.isEnabledFlag());
    		}
  
    
    		periodMapList.add(periodMap);
    	}
    	calMap.put("periodList", periodMapList);
    	
    	if(fileExport)
    	{
    		LinkedHashMap keysList=periodMapList.get(0);
    			
    			Set<String> keyset=keysList.keySet();
    			log.info("keyset :"+keyset.remove("id"));
    			log.info("keyset :"+keyset.remove("calId"));
    			log.info("keyset :"+keyset.remove("createdBy"));
    			log.info("keyset :"+keyset.remove("createdDate"));
    			log.info("keyset :"+keyset.remove("lastUpdatedBy"));
    			log.info("keyset :"+keyset.remove("lastUpdatedDate"));
    		
    			List<String> keyList = new ArrayList<String>(keyset);
    	    	log.info("keyList :"+keyList);
    	    	
        	if(fileType.equalsIgnoreCase("csv"))
        	{
        		response.setContentType ("application/csv");
        		response.setHeader ("Content-Disposition", "attachment; filename=\"calendar.csv\"");
        		fileExportService.jsonToCSV(periodMapList,keyList,response.getWriter());
        	}
        	if(fileType.equalsIgnoreCase("pdf"))
        	{
        		/*response.setContentType ("application/pdf");
        		response.setHeader ("Content-Disposition", "attachment; filename=\"calendar.pdf\"");*/
        		/*try {
					response=PDFGenerator.pdfGeneration(periodMapList,keyList,response);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
        	}
        	else if(fileType.equalsIgnoreCase("excel"))
        	{
        		/*response.setContentType("application/vnd.ms-excel");
        		response.setHeader(
        				"Content-Disposition",
        				"attachment; filename=\"excel-export-calendar.xlsx\""
        				);
        		fileExportService.jsonToCSV(periodMapList, keyList,response.getWriter());*/
        		response=fileExportService.exportToExcel(response, keyList, periodMapList);
        	}
    	}
    	return calMap;
 	}
    
    /**
     * author:ravali
     * @param calMap
     * @param userId
     * @param tenantId
     * @return
     */
    @PostMapping("/PostCalenderAndItsPeriods")
 	@Timed
 	public HashMap getCalenderAndItsPeriods(HttpServletRequest request,@RequestBody HashMap calMap)//,@RequestParam Long userId,@RequestParam Long tenantId)
    {
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	Long userId=Long.parseLong(map.get("userId").toString());
    	
    	log.info("Request Rest to post calender and its periods for tenant: "+tenantId+" user: "+userId);
    	HashMap finaMap=new HashMap();
    	Calendar cal=new Calendar();
    	if(calMap.get("id")!=null)
    	{
    		Calendar calendar=calendarRepository.findByIdForDisplayAndTenantId(calMap.get("id").toString(), tenantId);
    		cal.setId(calendar.getId());
    		cal.setIdForDisplay(calendar.getIdForDisplay());
    	}
    	if(calMap.get("calendarType")!=null)
    		cal.setCalendarType(calMap.get("calendarType").toString());
    	cal.setCreatedBy(userId);
    	cal.setCreatedDate(ZonedDateTime.now());
    	if(calMap.get("description")!=null)
    		cal.setDescription(calMap.get("description").toString());
    	cal.setEnabledFlag(true);
    	if(calMap.get("endDate")!=null)
    	{
    		ZonedDateTime edDate=ZonedDateTime.parse(calMap.get("endDate").toString());
    		cal.setEndDate(edDate);
    	}
    	cal.setLastUpdatedBy(userId);
    	if(calMap.get("name")!=null)
    		cal.setName(calMap.get("name").toString());
    	if(calMap.get("startDate")!=null)
    	{
    		ZonedDateTime stDate=ZonedDateTime.parse(calMap.get("startDate").toString());
    		cal.setStartDate(stDate);
    	}
    	cal.setTenantId(tenantId);
    	Calendar result = calendarRepository.save(cal);

    	if(calMap.get("id")!=null)
    	{
    	}
    	else
    	{
		String idForDisplay = IDORUtils.computeFrontEndIdentifier(result.getId().toString());
		result.setIdForDisplay(idForDisplay);
		result = calendarRepository.save(result);
    	}
		
    	
    	List<HashMap> periodsMapList=(List<HashMap>) calMap.get("periodList");
    	log.info("calMap.get(periodList) :"+periodsMapList.size());
    	//log.info("result :"+result);
    	if(result.getId()!=null)
    	{
    		for(int i=0;i<periodsMapList.size();i++)
    		{
    			Periods periods=new Periods();
    			if((Boolean)periodsMapList.get(i).get("adjustment")!=null)
    				periods.setAdjustment((Boolean)periodsMapList.get(i).get("adjustment"));
    			periods.setCalId(result.getId());
    			periods.setCreatedBy(userId);
    			periods.setCreatedDate(ZonedDateTime.now());
    			if(periodsMapList.get(i).get("fromDate")!=null)
    			{
    				ZonedDateTime fmDate=ZonedDateTime.parse(periodsMapList.get(i).get("fromDate").toString());
    				periods.setFromDate(fmDate);
    			}
    			periods.setLastUpdatedBy(userId);
    			periods.setLastUpdatedDate(ZonedDateTime.now());
    			if(periodsMapList.get(i).get("periodName")!=null)
    				periods.setPeriodName(periodsMapList.get(i).get("periodName").toString());
    			if(periodsMapList.get(i).get("periodNum")!=null)
    				periods.setPeriodNum(Integer.parseInt(periodsMapList.get(i).get("periodNum").toString()));
    			if(periodsMapList.get(i).get("quarter")!=null)
    				periods.setQuarter(Integer.parseInt(periodsMapList.get(i).get("quarter").toString()));
    			//periods.status(periodsMapList.get(i).get("status").toString());
    			periods.setEnabledFlag(true);
    			if(periodsMapList.get(i).get("toDate")!=null)
    			{
    				ZonedDateTime toDate=ZonedDateTime.parse(periodsMapList.get(i).get("toDate").toString());
    				periods.setToDate(toDate);
    			}
    			if(periodsMapList.get(i).get("year")!=null)
    				periods.setYear(Integer.parseInt(periodsMapList.get(i).get("year").toString()));
    			if(periodsMapList.get(i).containsKey("status")&&periodsMapList.get(i).get("status")!=null)
    				periods.setStatus(periodsMapList.get(i).get("status").toString());
    			periodsRepository.save(periods);

    		}
    	}
    	finaMap.put("id", result.getIdForDisplay());
    	finaMap.put("name", result.getName());
    	return finaMap;
    }
    
    @GetMapping("/getActiveCalenderType")
 	@Timed
 	public List<LookUpCode> getActiveCalenderType(HttpServletRequest request)
 	{
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	log.info("rest request to get active calender type");
    	List<String> calenderList=calendarRepository.findDistinctCalendarTypeByTenantAndActiveState(tenantId);
    	List<LookUpCode> lookUpCodeList=new  ArrayList<LookUpCode>();
    	for(String cal:calenderList)
    	{
    		LookUpCode lookUpCode=lookUpCodeRepository.findByTenantIdAndLookUpCodeAndLookUpType(tenantId, cal,"CALENDAR_TYPE");
    		lookUpCodeList.add(lookUpCode);
    	}
		return lookUpCodeList;
 	}
    /**
     * Author: Shiva
     * Purpose: Check weather calendar name exist or not
     * **/
    @GetMapping("/checkCalendarNameIsExist")
	@Timed
	public HashMap checkCalendarNameIsExist(@RequestParam String name,HttpServletRequest request,@RequestParam(required=false,value="id") String id)
	{
    	HashMap map0=userJdbcService.getuserInfoFromToken(request);
	  	Long tenantId=Long.parseLong(map0.get("tenantId").toString());
		HashMap map=new HashMap();
		map.put("result", "No Duplicates Found");
		if(id != null)
		{
			Calendar calenWithId = calendarRepository.findByIdForDisplayAndNameAndTenantId(id, name, tenantId);
			if(calenWithId != null)
			{
				map.put("result", "No Duplicates Found");
			}
			else
			{
				List<Calendar> cals = calendarRepository.findByTenantIdAndName(tenantId, name);
				if(cals.size()>0)
				{
					map.put("result", "'"+name+"' calendar already exists");
				}
			}
		}
		else 
		{
			List<Calendar> cals = calendarRepository.findByTenantIdAndName(tenantId, name);
			if(cals.size()>0)
			{
				map.put("result", "'"+name+"' calendar already exists");
			}
		}
		return map;
	}
}
