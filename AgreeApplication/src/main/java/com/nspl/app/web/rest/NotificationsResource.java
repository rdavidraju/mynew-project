package com.nspl.app.web.rest;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
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
import com.nspl.app.domain.Notifications;
import com.nspl.app.repository.NotificationsRepository;
import com.nspl.app.service.UserJdbcService;
import com.nspl.app.web.rest.util.HeaderUtil;
import com.nspl.app.web.rest.util.PaginationUtil;

/**
 * REST controller for managing Notifications.
 */
@RestController
@RequestMapping("/api")
public class NotificationsResource {

    private final Logger log = LoggerFactory.getLogger(NotificationsResource.class);

    private static final String ENTITY_NAME = "notifications";
        
    private final NotificationsRepository notificationsRepository;
    
    @Inject
    UserJdbcService userJdbcService;

    public NotificationsResource(NotificationsRepository notificationsRepository) {
        this.notificationsRepository = notificationsRepository;
    }

    /**
     * POST  /notifications : Create a new notifications.
     *
     * @param notifications the notifications to create
     * @return the ResponseEntity with status 201 (Created) and with body the new notifications, or with status 400 (Bad Request) if the notifications has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/notifications")
    @Timed
    public ResponseEntity<Notifications> createNotifications(@RequestBody Notifications notifications) throws URISyntaxException {
        log.debug("REST request to save Notifications : {}", notifications);
        if (notifications.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new notifications cannot already have an ID")).body(null);
        }
        Notifications result = notificationsRepository.save(notifications);
        return ResponseEntity.created(new URI("/api/notifications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /notifications : Updates an existing notifications.
     *
     * @param notifications the notifications to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated notifications,
     * or with status 400 (Bad Request) if the notifications is not valid,
     * or with status 500 (Internal Server Error) if the notifications couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/notifications")
    @Timed
    public ResponseEntity<Notifications> updateNotifications(@RequestBody Notifications notifications) throws URISyntaxException {
        log.debug("REST request to update Notifications : {}", notifications);
        if (notifications.getId() == null) {
            return createNotifications(notifications);
        }
        Notifications result = notificationsRepository.save(notifications);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, notifications.getId().toString()))
            .body(result);
    }

    /**
     * GET  /notifications : get all the notifications.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of notifications in body
     */
    @GetMapping("/notifications")
    @Timed
    public ResponseEntity<List<Notifications>> getAllNotifications(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Notifications");
        Page<Notifications> page = notificationsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/notifications");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /notifications/:id : get the "id" notifications.
     *
     * @param id the id of the notifications to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the notifications, or with status 404 (Not Found)
     */
    @GetMapping("/notifications/{id}")
    @Timed
    public ResponseEntity<Notifications> getNotifications(@PathVariable Long id) {
        log.debug("REST request to get Notifications : {}", id);
        Notifications notifications = notificationsRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(notifications));
    }

    /**
     * DELETE  /notifications/:id : delete the "id" notifications.
     *
     * @param id the id of the notifications to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/notifications/{id}")
    @Timed
    public ResponseEntity<Void> deleteNotifications(@PathVariable Long id) {
        log.debug("REST request to delete Notifications : {}", id);
        notificationsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * Author: Shiva
     * @param tenantId, userId
     * Description: Fetching Notifications data by userId and tenantId
     * @return HashMap with notifications data
     */
    @GetMapping("/getNotificationsDataByUserId")
    @Timed
    public HashMap getNotificationsDataByUserId(HttpServletRequest request){
    HashMap map=userJdbcService.getuserInfoFromToken(request);
  	Long tenantId=Long.parseLong(map.get("tenantId").toString());
	Long userId=Long.parseLong(map.get("userId").toString());
    	log.info("Rest api for getting notifications by user id: "+ userId +", tenant id: "+ tenantId);
    	HashMap finalMap = new HashMap();
    	List<Notifications> notifications = notificationsRepository.findByTenantIdAndUserIdOrderByIdDesc(tenantId, userId);
    	log.info("No of records fetched: "+ notifications.size());
    	List<HashMap> data = new ArrayList<HashMap>();
    	if(notifications.size()>0)
    	{
    		for(Notifications not : notifications)
    		{
    			HashMap hm = new HashMap();
    			hm.put("id", not.getId());
    			hm.put("message", not.getMessage());
    			hm.put("module", not.getModule());
    			hm.put("actionType", not.getActionType());
    			hm.put("actionValue", not.getActionValue());
    			hm.put("creationDate", not.getCreationDate());
    			hm.put("isViewed", not.isIsViewed());
    			//Getting time difference in Seconds, Minuites, Hours
    			String time = "";
    			ZonedDateTime start = not.getCreationDate();
    			ZonedDateTime stop = ZonedDateTime.now();
    			Duration dur = Duration.between(start, stop);
    			long hours = dur.toHours();
    			long minutes = dur.toMinutes();
    			long days = dur.toDays();
    			long seconds = dur.getSeconds();
    			if(days>0)
    			{
    				time = time + days+" days ago";
    			}
    			else if(hours>0)
    			{
    				time = time +hours + " hrs ago";
    			}
    			else if(minutes>0)
    			{
    				time = time+minutes + " mins ago";
    			}
    			else if(seconds>0)
    			{
    				time = time + seconds + " secs ago";
    			}
    			hm.put("time", time);		
    			data.add(hm);
    		}
    	}
    	finalMap.put("data", data);
    	finalMap.put("count", notifications.size());
    	List<Notifications> unRead = notificationsRepository.findByTenantIdAndUserIdAndIsViewed(tenantId, userId, false);
    	log.info("Un Read: "+ unRead.size());
    	finalMap.put("unRead", unRead.size());
    	return finalMap;
    }
    
    /**
     * Author: Bhagath
     * @param tenantId, userId
     * Description: Fetching Notifications data by userId and tenantId
     * @return HashMap with notifications data
     */
    @GetMapping("/getRecentActivitiesByModule")
    @Timed
    public HashMap getRecentActivitiesByModule(HttpServletRequest request,@RequestParam String module,
    		@RequestParam String fromDate,@RequestParam String toDate){
    HashMap map=userJdbcService.getuserInfoFromToken(request);
  	Long tenantId=Long.parseLong(map.get("tenantId").toString());
	Long userId=Long.parseLong(map.get("userId").toString());
    	log.info("Rest api for getting notifications by user id and module: "+ userId +", tenant id: "+ tenantId);
    	HashMap finalMap = new HashMap();
    	List<Notifications> notifications = notificationsRepository.findByTenantIdAndUserIdAndModuleAndCreationDateBetweenOrderByCreationDate(tenantId, userId, module, fromDate,toDate);
    	log.info("No of records fetched: "+ notifications.size());
    	List<HashMap> data = new ArrayList<HashMap>();
    	if(notifications.size()>0)
    	{
    		for(Notifications not : notifications)
    		{
    			HashMap hm = new HashMap();
    			hm.put("id", not.getId());
    			hm.put("message", not.getMessage());
    			hm.put("module", not.getModule());
    			hm.put("actionType", not.getActionType());
    			hm.put("actionValue", not.getActionValue());
    			hm.put("creationDate", not.getCreationDate());
    			hm.put("isViewed", not.isIsViewed());
    			//Getting time difference in Seconds, Minuites, Hours
    			String time = "";
    			ZonedDateTime start = not.getCreationDate();
    			ZonedDateTime stop = ZonedDateTime.now();
    			Duration dur = Duration.between(start, stop);
    			long hours = dur.toHours();
    			long minutes = dur.toMinutes();
    			long days = dur.toDays();
    			long seconds = dur.getSeconds();
    			if(days>0)
    			{
    				time = time + days+" days ago";
    			}
    			else if(hours>0)
    			{
    				time = time +hours + " hrs ago";
    			}
    			else if(minutes>0)
    			{
    				time = time+minutes + " mins ago";
    			}
    			else if(seconds>0)
    			{
    				time = time + seconds + " secs ago";
    			}
    			hm.put("time", time);		
    			data.add(hm);
    		}
    	}
    	finalMap.put("data", data);
    	finalMap.put("count", notifications.size());
    	return finalMap;
    }
    
    /**
     * Author: Shiva
     * @param notificationsId
     * Description: Updating notifications records by setting is_viewed column as true based on notifications id
     * @return HashMap with notifications id
     */
    @PostMapping("/updateIsViewedAsTrue")
    @Timed
    public HashMap updateIsViewedAsTrue(@RequestBody List<Long> notificationsIds){
    	log.info("Updating notifications record by setting is_viewed column as true based on notifications id: "+ notificationsIds);
    	HashMap finalMap = new HashMap();
    	List<Long> updatedNotIds = new ArrayList<Long>();
    	if(notificationsIds.size()>0)
    	{
    		for(Long id : notificationsIds)
    		{
    			Notifications not = notificationsRepository.findOne(id);
    			if(not != null)
    			{
    				not.setIsViewed(true);
    	    		notificationsRepository.save(not);
    	    		updatedNotIds.add(not.getId());
    			}
    		}
    	}
    	finalMap.put("ids", updatedNotIds);
    	log.info("Updated Ids: "+ updatedNotIds.size());
    	return finalMap;
    }
}
