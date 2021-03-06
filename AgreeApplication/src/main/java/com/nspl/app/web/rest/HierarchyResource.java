package com.nspl.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.domain.Hierarchy;
import com.nspl.app.domain.NotificationBatch;
//import com.nspl.app.jbpm.service.ApprovalProcessService;
import com.nspl.app.repository.HierarchyRepository;
import com.nspl.app.repository.NotificationBatchRepository;
import com.nspl.app.service.HierarchyService;
import com.nspl.app.service.UserJdbcService;
import com.nspl.app.web.rest.util.HeaderUtil;
import com.nspl.app.web.rest.util.PaginationUtil;

import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * REST controller for managing Hierarchy.
 */
@RestController
@RequestMapping("/api")
public class HierarchyResource {

    private final Logger log = LoggerFactory.getLogger(HierarchyResource.class);

    private static final String ENTITY_NAME = "hierarchy";

    private final HierarchyRepository hierarchyRepository;
    
   /* @Inject
    ApprovalProcessService approvalProcessService;*/
    
    @Inject
    HierarchyService hierarchyService;
    
    @Inject
    UserJdbcService userJdbcService;
    
    @Inject
    NotificationBatchRepository notificationBatchRepository;

    public HierarchyResource(HierarchyRepository hierarchyRepository) {
        this.hierarchyRepository = hierarchyRepository;
    }

    /**
     * POST  /hierarchies : Create a new hierarchy.
     *
     * @param hierarchy the hierarchy to create
     * @return the ResponseEntity with status 201 (Created) and with body the new hierarchy, or with status 400 (Bad Request) if the hierarchy has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/hierarchies")
    @Timed
    public ResponseEntity<Hierarchy> createHierarchy(@RequestBody Hierarchy hierarchy) throws URISyntaxException {
        log.debug("REST request to save Hierarchy : {}", hierarchy);
        if (hierarchy.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new hierarchy cannot already have an ID")).body(null);
        }
        Hierarchy result = hierarchyRepository.save(hierarchy);
        return ResponseEntity.created(new URI("/api/hierarchies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /hierarchies : Updates an existing hierarchy.
     *
     * @param hierarchy the hierarchy to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated hierarchy,
     * or with status 400 (Bad Request) if the hierarchy is not valid,
     * or with status 500 (Internal Server Error) if the hierarchy couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/hierarchies")
    @Timed
    public ResponseEntity<Hierarchy> updateHierarchy(@RequestBody Hierarchy hierarchy) throws URISyntaxException {
        log.debug("REST request to update Hierarchy : {}", hierarchy);
        if (hierarchy.getId() == null) {
            return createHierarchy(hierarchy);
        }
        Hierarchy result = hierarchyRepository.save(hierarchy);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, hierarchy.getId().toString()))
            .body(result);
    }

    /**
     * GET  /hierarchies : get all the hierarchies.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of hierarchies in body
     */
    @GetMapping("/hierarchies")
    @Timed
    public ResponseEntity<List<Hierarchy>> getAllHierarchies(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Hierarchies");
        Page<Hierarchy> page = hierarchyRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/hierarchies");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /hierarchies/:id : get the "id" hierarchy.
     *
     * @param id the id of the hierarchy to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the hierarchy, or with status 404 (Not Found)
     */
    @GetMapping("/hierarchies/{id}")
    @Timed
    public ResponseEntity<Hierarchy> getHierarchy(@PathVariable Long id) {
        log.debug("REST request to get Hierarchy : {}", id);
        Hierarchy hierarchy = hierarchyRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(hierarchy));
    }

    /**
     * DELETE  /hierarchies/:id : delete the "id" hierarchy.
     *
     * @param id the id of the hierarchy to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/hierarchies/{id}")
    @Timed
    public ResponseEntity<Void> deleteHierarchy(@PathVariable Long id) {
        log.debug("REST request to delete Hierarchy : {}", id);
        hierarchyRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
   /**
    * author :tavali
    * @param userId
    * @param tenantId
    * @param status
    * @param module
    * @return
    * @throws NumberFormatException
    * @throws ClassNotFoundException
    * @throws SQLException
    * Desc :get hierarchy of a user along with notification count
    */
    @GetMapping("/hierarchyListForAParent")
   	@Timed
   	public LinkedHashMap getHierachyList(HttpServletRequest request	,@RequestParam(value = "status", required = false) String status, 
   			@RequestParam(value = "module", required = false) String module) throws NumberFormatException, ClassNotFoundException, SQLException  {
    	HashMap map0=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map0.get("tenantId").toString());
    	Long userId=Long.parseLong(map0.get("userId").toString());
    	
    	LinkedHashMap finalMap=new LinkedHashMap();
    	List<LinkedHashMap> finalMapList=new ArrayList<LinkedHashMap>();
    	LinkedHashMap mp=new LinkedHashMap();
    	mp.put("userId", userId);
    	HashMap map=userJdbcService.jdbcConnc(userId,tenantId);
    	mp.put("label", map.get("assigneeName"));
    	mp.put("expandedIcon", "fa-user");
    	mp.put("collapsedIcon", "fa-user");
    	mp.put("data", " ");
      	int count=hierarchyService.getNotificationCountForUser(userId, status, tenantId, module);
    	mp.put("count", count);
    	List<LinkedHashMap> childList=hierarchyService.getHierfunc(userId,status,tenantId, module);
    	mp.put("children", childList);
    	
    	finalMapList.add(mp);
    	
    	finalMap.put("data", finalMapList);
    	
		return finalMap;
    	
    }
   
    
    /**
     * author :ravali
     * @param tenantId
     * @return
     * @throws NumberFormatException
     * @throws ClassNotFoundException
     * @throws SQLException
     * Desc : hierarchy level from parent level by tenantId 
     */
    @GetMapping("/hierarchyListFromParentHead")
   	@Timed
   	public LinkedHashMap getHierachyFromParentHead(HttpServletRequest request) throws NumberFormatException, ClassNotFoundException, SQLException  {
    	LinkedHashMap finalMap=new LinkedHashMap();
    	HashMap map0=userJdbcService.getuserInfoFromToken(request);
      	Long tenantId=Long.parseLong(map0.get("tenantId").toString());
    	Long parentId=0l;
    	Hierarchy hierarchy=hierarchyRepository.findByTenantIdAndParentId(tenantId,parentId);
    	//log.info("hierarchy :"+hierarchy);
    	if(hierarchy!=null)
    	{
    	finalMap.put("userId", hierarchy.getObjectName());
    	HashMap map=userJdbcService.jdbcConnc(Long.valueOf(hierarchy.getObjectName()),tenantId);
    	finalMap.put("userName", map.get("assigneeName"));
    	finalMap.put("label", map.get("businessTitle"));
    	finalMap.put("type", "person");
    	finalMap.put("styleClass", "ui-person");
    	finalMap.put("expanded", true);
    	LinkedHashMap user=new LinkedHashMap();
    	user.put("userName", map.get("assigneeName"));
    	user.put("imgUrl", map.get("imgUrl"));
    	finalMap.put("data", user);
    	
    	List<LinkedHashMap> childList=hierarchyService.getHierfuncFromHead(Long.valueOf(hierarchy.getObjectName()),tenantId);
    	finalMap.put("children", childList);
    	}
    	
		return finalMap;
    	
    }
    
    @GetMapping("/reverseHierarchyList")
   	@Timed
   	public LinkedHashMap getReverseHierarchyList(@RequestParam Long userId,HttpServletRequest request) throws NumberFormatException, ClassNotFoundException, SQLException  {
    	

    	LinkedHashMap finalMap=new LinkedHashMap();
    
    	HashMap map0=userJdbcService.getuserInfoFromToken(request);
      	Long tenantId=Long.parseLong(map0.get("tenantId").toString());
    	finalMap.put("userId", userId);
    	HashMap map=userJdbcService.jdbcConnc(userId,tenantId);
    	finalMap.put("userName", map.get("assigneeName"));
    	List<LinkedHashMap> childList=hierarchyService.getHierfuncReverse(userId);
    	finalMap.put("parentList", childList);
    	
		return finalMap;
    	
    
    }
    
    @GetMapping("/getManagerialHierarchyList")
   	@Timed
   	public List<HashMap> getHierachyList(@RequestParam String tenantId,@RequestParam Long userId) throws NumberFormatException, ClassNotFoundException, SQLException{
    	log.info("Rest API for fetching managerial hierarchy list for the tenant id: "+ tenantId+", userId");
    	List<HashMap> finalList = new ArrayList<HashMap>();
    	HashMap tenantIdByIdForDisplay=userJdbcService.getTenantIdForDisplay(tenantId);
    	List<Hierarchy> totalList = hierarchyRepository.findByTenantId(Long.valueOf(tenantIdByIdForDisplay.get("tenantId").toString()));
    	if(totalList.size()>0)
    	{
    		HashMap parentChildMap = new HashMap();
    		for(Hierarchy hierarchy : totalList)
    		{
    			parentChildMap.put(Long.parseLong(hierarchy.getObjectName()), hierarchy.getParentId());
    		}
    		log.info("Parent Child Map: "+parentChildMap);
    		Long baseId = userId;
    		for(Hierarchy hierarchy : totalList)
    		{
    			if(parentChildMap.get(baseId) != null)
    			{
        			if(Long.parseLong(parentChildMap.get(baseId).toString()) == 0L)
        			{
        				HashMap map=userJdbcService.jdbcConnc(baseId,hierarchy.getTenantId());
        				HashMap hm = new HashMap();
        				hm.put("managerName", map.get("assigneeName"));
        				hm.put("designation", map.get("businessTitle"));
        				finalList.add(hm);
        				break;
        			}
        			else
        			{
        				HashMap map=userJdbcService.jdbcConnc(baseId,hierarchy.getTenantId());
        				HashMap hm = new HashMap();
        				hm.put("managerName", map.get("assigneeName"));
        				hm.put("designation", map.get("businessTitle"));
        				finalList.add(hm);
        				baseId = Long.parseLong(parentChildMap.get(baseId).toString());
        			}
    			}
    		}
    		log.info("Final List: "+ finalList);
    	}
    	return finalList;
	}

    
    
    @PostMapping("/insertRecordInHierarchyTable")
   	@Timed
   	public void insertRecordInHierarchyTable(@RequestParam Long userId,@RequestParam Long tenantId,@RequestParam(value="managerId",required=false) Long managerId,@RequestParam Boolean update) {
    	log.info("Request rest to insert records into hierarchy table userId :"+userId +"tenantId :"+tenantId+" managerId :"+managerId );
    	log.info("insterting record when manager id is null or 0");
    	Hierarchy hie=new Hierarchy();
    	
    	if(update)
		{
		Hierarchy hierarIfExist=hierarchyRepository.findByObjectNameAndParentId(userId.toString(),managerId);
	//	Hierarchy hierar=hierarchyRepository.findByObjectName(managerId.toString());
		if(hierarIfExist!=null)
		{
			
			hierarIfExist.setObjectName(userId.toString());
			hierarIfExist.setParentId(managerId);
			hierarIfExist.setObjectType("RESOURCE");
			hierarIfExist.setEnableFlag(true);
			hierarIfExist.setTenantId(tenantId);
			hierarIfExist.setLastUpdatedBy(userId);
			hierarIfExist.setLastUpdatedDate(ZonedDateTime.now());
			hierarIfExist =hierarchyRepository.save(hierarIfExist);
        	log.info("hie :"+hie);
		}
		}
    	
    	
    	
    	else
    	{
    	if(managerId!=null)
    	{
    		//Hierarchy hierar=hierarchyRepository.findByObjectName(managerId.toString());
    		log.info("if managerId is not null");
        	hie.setObjectName(userId.toString());
        	hie.setParentId(managerId);
        	hie.setObjectType("RESOURCE");
        	hie.setEnableFlag(true);
        	hie.setTenantId(tenantId);
        	/*ZonedDateTime stDate=ZonedDateTime.parse(startDate);
        	hie.setStartDate(stDate.toLocalDate().plusDays(1));
        	ZonedDateTime edDate=ZonedDateTime.parse(endDate);
        	hie.setStartDate(edDate.toLocalDate().plusDays(1));*/
        	hie.setCreatedDate(ZonedDateTime.now());
        	hie.setCreatedBy(userId);
        	hie.setLastUpdatedBy(userId);
        	hie.setLastUpdatedDate(ZonedDateTime.now());
        	hie =hierarchyRepository.save(hie);
        	log.info("hie :"+hie);
    	
    	}
    	else
    	{
    		
        	log.info("else if managerId is null");
        	hie.setObjectName(userId.toString());
        	hie.setParentId(0l);
        	hie.setObjectType("RESOURCE");
        	hie.setEnableFlag(true);
        	hie.setTenantId(tenantId);
        	/*ZonedDateTime stDate=ZonedDateTime.parse(startDate);
        	hie.setStartDate(stDate.toLocalDate());
        	ZonedDateTime edDate=ZonedDateTime.parse(endDate);
        	hie.setStartDate(edDate.toLocalDate());*/
        	hie.setCreatedDate(ZonedDateTime.now());
        	hie.setCreatedBy(userId);
        	hie.setLastUpdatedBy(userId);
        	hie.setLastUpdatedDate(ZonedDateTime.now());
        	hie =hierarchyRepository.save(hie);
        	log.info("hie :"+hie);
    	}
    }

    }
    
}
