package com.nspl.app.web.rest;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.inject.Inject;

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
import com.nspl.app.domain.User;
import com.nspl.app.domain.UserComments;
import com.nspl.app.repository.UserCommentsRepository;
import com.nspl.app.repository.UserRepository;
import com.nspl.app.service.UserService;
import com.nspl.app.web.rest.util.HeaderUtil;
import com.nspl.app.web.rest.util.PaginationUtil;

/**
 * REST controller for managing UserComments.
 */
@RestController
@RequestMapping("/api")
public class UserCommentsResource {

    private final Logger log = LoggerFactory.getLogger(UserCommentsResource.class);

    private static final String ENTITY_NAME = "userComments";

    private final UserCommentsRepository userCommentsRepository;
    
    @Inject
    UserRepository userRepository;
    
    @Inject
    UserService userService;
    
    public UserCommentsResource(UserCommentsRepository userCommentsRepository) {
        this.userCommentsRepository = userCommentsRepository;
    }

    /**
     * POST  /user-comments : Create a new userComments.
     *
     * @param userComments the userComments to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userComments, or with status 400 (Bad Request) if the userComments has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    /* Author: Shiva
     * Purpose: Posting User Comments
     * */
    @PostMapping("/user-comments")
    @Timed
    public ResponseEntity<UserComments> createUserComments(@RequestBody UserComments userComments) throws URISyntaxException {
        log.debug("REST request to save UserComments : {}", userComments);
        if (userComments.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new userComments cannot already have an ID")).body(null);
        }        
    	User currentUser = userService.getCurrentUser();
    	Long tenantId = currentUser.getTenantId();
        Long userId = currentUser.getId();
    	userComments.setCreatedBy(userId);
        userComments.setLastUpdatedBy(userId);
        userComments.setCreationDate(ZonedDateTime.now());
        userComments.setLastUpdatedDate(ZonedDateTime.now());
        userComments.setTenantId(tenantId);    	
        UserComments result = userCommentsRepository.save(userComments);
        return ResponseEntity.created(new URI("/api/user-comments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    
    /* Author: Shiva
     * Purpose: API for posting user comments 
     * 
     * */
    @PostMapping("/postUserComments")
    @Timed
    public HashMap createUserCommentsRecord(@RequestBody UserComments userComments) throws URISyntaxException {
        log.debug("REST request to save UserComments : {}", userComments);
        HashMap finalMap = new HashMap();
        User currentUser = userService.getCurrentUser();
    	Long tenantId = currentUser.getTenantId();
        Long userId = currentUser.getId();
    	userComments.setCreatedBy(userId);
        userComments.setLastUpdatedBy(userId);
        userComments.setCreationDate(ZonedDateTime.now());
        userComments.setLastUpdatedDate(ZonedDateTime.now());
        userComments.setTenantId(tenantId);    	
        UserComments result = userCommentsRepository.save(userComments);
        finalMap.put("id", result.getId());
		finalMap.put("userId", result.getUserId());
		finalMap.put("subject", result.getSubject());
		finalMap.put("repliedToCommentId", result.getRepliedToCommentId());
		finalMap.put("recipientUserId", result.getRecipientUserId());
		finalMap.put("messageBody", result.getMessageBody());
		finalMap.put("isRead", result.isIsRead());
		finalMap.put("isActive", result.isIsActive());
		User user = userRepository.findOne(result.getUserId());
		if(user != null)
		{
			finalMap.put("userName", user.getFirstName());
			finalMap.put("image", user.getImage());
			finalMap.put("firstLetter", user.getFirstName().charAt(0));
			
		}
		ZonedDateTime date = result.getCreationDate();
		String monthDayPattern = "MMM dd";
		String monthDay = date.format(DateTimeFormatter.ofPattern(monthDayPattern));
		String yearPattern = "yy";
		String year = date.format(DateTimeFormatter.ofPattern(yearPattern));
		String timePattern = "hh:mm";
		String time = date.format(DateTimeFormatter.ofPattern(timePattern));
		String dayNamePattern = "E";
		String dayName = date.format(DateTimeFormatter.ofPattern(dayNamePattern));
		String amOrPmPattern = "a";
		String amOrPm = date.format(DateTimeFormatter.ofPattern(amOrPmPattern));
		String finalTime = "";

		LocalDate localDateIncoming = date.toLocalDate ();
		ZonedDateTime now = ZonedDateTime.now();
		LocalDate localDateToday = now.toLocalDate();
	    LocalDate localDateYesterday = localDateToday.minusDays(1);
	    DateTimeFormatter formatter = null;
	    Locale locale = Locale.getDefault();
	    if (localDateIncoming.isEqual(localDateToday)) 
	    {
	    	finalTime = time+" "+amOrPm+" Today";
	    }
	    else if(localDateIncoming.isEqual(localDateYesterday)) 
	    {
	    	finalTime = time+" "+amOrPm+" Yesterday";
	    }
	    else 
	    {
	    	finalTime = time+" "+amOrPm+" "+monthDay;
	    }
		finalMap.put("creationDate", finalTime);
		finalMap.put("createdBy", result.getCreatedBy());

        return finalMap;
    }

    /**
     * PUT  /user-comments : Updates an existing userComments.
     *
     * @param userComments the userComments to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userComments,
     * or with status 400 (Bad Request) if the userComments is not valid,
     * or with status 500 (Internal Server Error) if the userComments couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-comments")
    @Timed
    public ResponseEntity<UserComments> updateUserComments(@RequestBody UserComments userComments) throws URISyntaxException {
        log.debug("REST request to update UserComments : {}", userComments);
        if (userComments.getId() == null) {
            return createUserComments(userComments);
        }
        UserComments result = userCommentsRepository.save(userComments);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, userComments.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-comments : get all the userComments.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of userComments in body
     */
    @GetMapping("/user-comments")
    @Timed
    public ResponseEntity<List<UserComments>> getAllUserComments(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of UserComments");
        Page<UserComments> page = userCommentsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/user-comments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /user-comments/:id : get the "id" userComments.
     *
     * @param id the id of the userComments to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userComments, or with status 404 (Not Found)
     */
    @GetMapping("/user-comments/{id}")
    @Timed
    public ResponseEntity<UserComments> getUserComments(@PathVariable Long id) {
        log.debug("REST request to get UserComments : {}", id);
        UserComments userComments = userCommentsRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(userComments));
    }

    /**
     * DELETE  /user-comments/:id : delete the "id" userComments.
     *
     * @param id the id of the userComments to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-comments/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserComments(@PathVariable Long id) {
        log.debug("REST request to delete UserComments : {}", id);
        userCommentsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    /* Author: Shiva
     * Purpose: Deleting user comments based on comment id
     * */
    @GetMapping("/deleteUserComments")
    @Timed
    public HashMap deleteUserComments(@RequestParam Long commentId, @RequestParam String commentOrReply) throws ClassNotFoundException, SQLException
    {
    	HashMap finalMap = new HashMap();
    	User currentUser = userService.getCurrentUser();
    	Long tenantId = currentUser.getTenantId();
        Long userId = currentUser.getId();
    	if("comment".equalsIgnoreCase(commentOrReply))
    	{
    		UserComments uc = userCommentsRepository.findOne(commentId);
    		if(uc != null)
    		{
    			List<UserComments> replies = userCommentsRepository.findByTenantIdAndRepliedToCommentId(tenantId, uc.getId());
    			if(replies.size()>0)
    			{
    				userCommentsRepository.deleteInBatch(replies);
    			}
    			userCommentsRepository.delete(uc);
    		}
    		log.info("Comment and its replies has been deleted.");
    	}
    	else if("reply".equalsIgnoreCase(commentOrReply))
    	{
    		UserComments uc = userCommentsRepository.findOne(commentId);
    		if(uc != null)
    		{
    			userCommentsRepository.delete(uc);
    		}
    		log.info("Reply has been deleted");
    	}
    	return finalMap;
    }
    
    /* Author: Shiva
     * Purpose: API for fetching comment record based on comment id
     * */
    @GetMapping("/getUserComment")
    @Timed
    public HashMap getUserComment(@RequestParam Long commentId) throws ClassNotFoundException, SQLException
    {
    	HashMap finalMap = new HashMap();
    	UserComments uc = userCommentsRepository.findOne(commentId);
    	if(uc != null)
    	{
    		finalMap.put("userId", uc.getUserId());
    		finalMap.put("subject", uc.getSubject());
    		finalMap.put("repliedToCommentId", uc.getRepliedToCommentId());
    		finalMap.put("recipientUserId", uc.getRecipientUserId());
    		finalMap.put("messageBody", uc.getMessageBody());
    		finalMap.put("isRead", uc.isIsRead());
    		finalMap.put("isActive", uc.isIsActive());
			User user = userRepository.findOne(uc.getUserId());
			if(user != null)
			{
				finalMap.put("userName", user.getFirstName());
				finalMap.put("image", user.getImage());
				finalMap.put("firstLetter", user.getFirstName().charAt(0));
			}
			ZonedDateTime date = uc.getCreationDate();

			String monthDayPattern = "MMM dd";
			String monthDay = date.format(DateTimeFormatter.ofPattern(monthDayPattern));
			String yearPattern = "yy";
			String year = date.format(DateTimeFormatter.ofPattern(yearPattern));
			String timePattern = "hh:mm";
			String time = date.format(DateTimeFormatter.ofPattern(timePattern));
			String dayNamePattern = "E";
			String dayName = date.format(DateTimeFormatter.ofPattern(dayNamePattern));
			String amOrPmPattern = "a";
			String amOrPm = date.format(DateTimeFormatter.ofPattern(amOrPmPattern));
			String finalTime = "";

			LocalDate localDateIncoming = date.toLocalDate ();
			ZonedDateTime now = ZonedDateTime.now();
			LocalDate localDateToday = now.toLocalDate();
		    LocalDate localDateYesterday = localDateToday.minusDays(1);
		    DateTimeFormatter formatter = null;
		    Locale locale = Locale.getDefault();
		    if (localDateIncoming.isEqual(localDateToday)) 
		    {
		    	finalTime = time+" "+amOrPm+" Today";
		    }
		    else if(localDateIncoming.isEqual(localDateYesterday)) 
		    {
		    	finalTime = time+" "+amOrPm+" Yesterday";
		    }
		    else 
		    {
		    	finalTime = time+" "+amOrPm+" "+monthDay;
		    }

			
			finalMap.put("creationDate", finalTime);
			finalMap.put("createdBy", uc.getCreatedBy());
    	}
    	return finalMap;
    }
    
    
    /**
     * Author: Shiva
     * Purpose: API for fetching All Comments
     **/
    @GetMapping("/getUserComments")
    @Timed
    public List<HashMap> getUserComments(@RequestParam(value = "rangeFrom", required=false) String rangeFrom, @RequestParam(value = "rangeTo", required=false) String rangeTo) throws ClassNotFoundException, SQLException
    {
    	List<HashMap> finalList = new ArrayList<HashMap>();
    	User currentUser = userService.getCurrentUser();
    	Long tenantId = currentUser.getTenantId();
        Long userId = currentUser.getId();
    	List<UserComments> commentsList = new ArrayList<UserComments>();
    	if(rangeFrom != null && rangeTo != null)
    	{
    		commentsList = userCommentsRepository.fetchByTenantIdAndCreationDateRange(tenantId, rangeFrom, rangeTo);
    	}
    	else
    	{
        	commentsList = userCommentsRepository.findByTenantIdAndRepliedToCommentIdIsNull(tenantId);    		
    	}
    	log.info("Comments List Size: "+commentsList.size());
    	if(commentsList.size()>0)
    	{
    		for(UserComments uc : commentsList)
    		{
    			HashMap hm = new HashMap();
    			hm.put("id", uc.getId());
    			hm.put("subject", uc.getSubject());
    			hm.put("userId", uc.getUserId());
    			User user = userRepository.findOne(uc.getUserId());
    			if(user != null)
    			{
    				hm.put("userName", user.getFirstName()+" "+ user.getLastName());
    				hm.put("image", user.getImage());
    				hm.put("firstLetter", user.getFirstName().charAt(0));
    			}
    			hm.put("recipientUserId", uc.getRecipientUserId());
    			hm.put("messageBody", uc.getMessageBody());
    			hm.put("repliedToCommentId", uc.getRepliedToCommentId());
    			hm.put("isActive", uc.isIsActive());
    			hm.put("isRead", uc.isIsRead());
    			ZonedDateTime date = uc.getCreationDate();

    			String monthDayPattern = "MMM dd";
    			String monthDay = date.format(DateTimeFormatter.ofPattern(monthDayPattern));
    			String yearPattern = "yy";
    			String year = date.format(DateTimeFormatter.ofPattern(yearPattern));
    			String timePattern = "hh:mm";
    			String time = date.format(DateTimeFormatter.ofPattern(timePattern));
    			String dayNamePattern = "E";
    			String dayName = date.format(DateTimeFormatter.ofPattern(dayNamePattern));
    			String amOrPmPattern = "a";
    			String amOrPm = date.format(DateTimeFormatter.ofPattern(amOrPmPattern));
    			String finalTime = "";
    			LocalDate localDateIncoming = date.toLocalDate ();
    			ZonedDateTime now = ZonedDateTime.now();
    			LocalDate localDateToday = now.toLocalDate();
    		    LocalDate localDateYesterday = localDateToday.minusDays(1);
    		    DateTimeFormatter formatter = null;
    		    Locale locale = Locale.getDefault();
    		    if (localDateIncoming.isEqual(localDateToday)) 
    		    {
    		    	finalTime = time+" "+amOrPm+" Today";
    		    }
    		    else if(localDateIncoming.isEqual(localDateYesterday)) 
    		    {
    		    	finalTime = time+" "+amOrPm+" Yesterday";
    		    }
    		    else 
    		    {
    		    	finalTime = time+" "+amOrPm+" "+monthDay;
    		    }
    			hm.put("creationDate", finalTime);	
    			hm.put("createdBy", uc.getCreatedBy());
    			List<UserComments> replies = userCommentsRepository.findByTenantIdAndRepliedToCommentId(tenantId, uc.getId());
    			List<HashMap> repliesList = new ArrayList<HashMap>();
    			if(replies.size()>0)
    			{
    				for(UserComments ruc : replies)
    				{
    					HashMap rhm = new HashMap();
    	    			rhm.put("id", ruc.getId());
    	    			rhm.put("subject", ruc.getSubject());
    	    			rhm.put("userId", ruc.getUserId());
    	    			rhm.put("recipientUserId", ruc.getRecipientUserId());
    	    			rhm.put("messageBody", ruc.getMessageBody());
    	    			rhm.put("repliedToCommentId", ruc.getRepliedToCommentId());
    	    			rhm.put("isActive", ruc.isIsActive());
    	    			rhm.put("isRead", ruc.isIsRead());
    	    			User user2 = userRepository.findOne(ruc.getUserId());
    	    			if(user2 != null)
    	    			{
    	    				rhm.put("userName", user2.getFirstName()+" "+ user2.getLastName());
    	    				rhm.put("image", user2.getImage());
    	    				rhm.put("firstLetter", user2.getFirstName().charAt(0));
    	    			}
    	    			ZonedDateTime date2 = ruc.getCreationDate();
    	    			
    	    			String monthDayPattern2 = "MMM dd";
    	    			String monthDay2 = date2.format(DateTimeFormatter.ofPattern(monthDayPattern2));
    	    			String yearPattern2 = "yy";
    	    			String year2 = date2.format(DateTimeFormatter.ofPattern(yearPattern2));
    	    			String timePattern2 = "hh:mm";
    	    			String time2 = date2.format(DateTimeFormatter.ofPattern(timePattern2));
    	    			String dayNamePattern2 = "E";
    	    			String dayName2 = date2.format(DateTimeFormatter.ofPattern(dayNamePattern2));
    	    			String amOrPmPattern2 = "a";
    	    			String amOrPm2 = date2.format(DateTimeFormatter.ofPattern(amOrPmPattern2));
    	    			
    	    			String finalTime2 = "";

    	    			LocalDate localDateIncoming2 = date2.toLocalDate ();
    	    			ZonedDateTime now2 = ZonedDateTime.now();
    	    			LocalDate localDateToday2 = now2.toLocalDate();
    	    		    LocalDate localDateYesterday2 = localDateToday2.minusDays(1);
    	    		    DateTimeFormatter formatter2 = null;
    	    		    Locale locale2 = Locale.getDefault();
    	    		    if (localDateIncoming2.isEqual(localDateToday2)) 
    	    		    {
    	    		    	finalTime2 = time2+" "+amOrPm2+" Today";
    	    		    }
    	    		    else if(localDateIncoming2.isEqual(localDateYesterday2)) 
    	    		    {
    	    		    	finalTime2 = time2+" "+amOrPm2+" Yesterday";
    	    		    }
    	    		    else 
    	    		    {
    	    		    	finalTime2 = time2+" "+amOrPm2+" "+monthDay2;
    	    		    }
    	    			rhm.put("creationDate", finalTime2);
    	    			rhm.put("createdBy", ruc.getCreatedBy());
    	    			repliesList.add(rhm);
    				}
    			}
    			hm.put("replies", repliesList);
    			finalList.add(hm);
    		}
    	}
    	return finalList;
    }
    
    /**
     * Author: Shiva
     * Purpose: Updating un read messages to read messages
     * **/
    @GetMapping("/updateIsReadAsTrue")
    @Timed
    public HashMap updateIsReadAsTrue(){
    	log.info("Rest API for updating is_read column as true");
    	HashMap finalMap = new HashMap();
    	
    	User currentUser = userService.getCurrentUser();
    	Long tenantId = currentUser.getTenantId();
    	
    	List<UserComments> unReads  = userCommentsRepository.findByTenantIdAndIsRead(tenantId, false);
    	if(unReads.size()>0)
    	{
    		for(UserComments unRead : unReads)
    		{
    			unRead.setIsRead(true);
    			userCommentsRepository.save(unRead);
    		}
    	}
    	log.info(unReads.size()+" Records has been updated.");
    	return finalMap;
    }
}
