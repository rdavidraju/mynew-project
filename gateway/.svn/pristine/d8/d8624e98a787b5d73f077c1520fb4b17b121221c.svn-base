package com.nspl.app.web.rest;

import io.github.jhipster.web.util.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.nspl.app.domain.UserRoleAssignment;
import com.nspl.app.repository.UserRoleAssignmentRepository;
import com.nspl.app.service.UserService;
import com.nspl.app.service.dto.RoleUsersDTO;
import com.nspl.app.service.dto.UserRoleAssignmentDTO;
import com.nspl.app.web.rest.util.HeaderUtil;

/**
 * REST controller for managing UserRoleAssignment.
 */
@RestController
@RequestMapping("/api")
public class UserRoleAssignmentResource {

    private final Logger log = LoggerFactory.getLogger(UserRoleAssignmentResource.class);

    private static final String ENTITY_NAME = "userRoleAssignment";

    private final UserRoleAssignmentRepository userRoleAssignmentRepository;
    
    private final UserService userService;

    public UserRoleAssignmentResource(UserRoleAssignmentRepository userRoleAssignmentRepository,UserService userService) {
        this.userRoleAssignmentRepository = userRoleAssignmentRepository;
        this.userService = userService;
    }

    /**
     * POST  /user-role-assignments : Create a new userRoleAssignment.
     *
     * @param userRoleAssignment the userRoleAssignment to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userRoleAssignment, or with status 400 (Bad Request) if the userRoleAssignment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-role-assignments")
    @Timed
    public ResponseEntity<UserRoleAssignment> createUserRoleAssignment(@RequestBody UserRoleAssignment userRoleAssignment) throws URISyntaxException {
        log.debug("REST request to save UserRoleAssignment : {}", userRoleAssignment);
        if (userRoleAssignment.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new userRoleAssignment cannot already have an ID")).body(null);
        }
        UserRoleAssignment result = userRoleAssignmentRepository.save(userRoleAssignment);
        return ResponseEntity.created(new URI("/api/user-role-assignments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-role-assignments : Updates an existing userRoleAssignment.
     *
     * @param userRoleAssignment the userRoleAssignment to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userRoleAssignment,
     * or with status 400 (Bad Request) if the userRoleAssignment is not valid,
     * or with status 500 (Internal Server Error) if the userRoleAssignment couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-role-assignments")
    @Timed
    public ResponseEntity<UserRoleAssignment> updateUserRoleAssignment(@RequestBody UserRoleAssignment userRoleAssignment,@RequestParam Long userId) throws URISyntaxException {
        log.debug("REST request to update UserRoleAssignment : {}", userRoleAssignment);
        if (userRoleAssignment.getId() == null) {
            return createUserRoleAssignment(userRoleAssignment);
        }
        userRoleAssignment.setStartDate(userRoleAssignment.getStartDate());
        userRoleAssignment.setEndDate(userRoleAssignment.getEndDate());
        userRoleAssignment.setLastUpdatedBy(userId);
        userRoleAssignment.setLastUpdatedDate(ZonedDateTime.now());
        
        UserRoleAssignment result = userRoleAssignmentRepository.save(userRoleAssignment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, userRoleAssignment.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-role-assignments : get all the userRoleAssignments.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of userRoleAssignments in body
     */
    @GetMapping("/user-role-assignments")
    @Timed
    public List<UserRoleAssignment> getAllUserRoleAssignments() {
        log.debug("REST request to get all UserRoleAssignments");
        return userRoleAssignmentRepository.findAll();
    }

    /**
     * GET  /user-role-assignments/:id : get the "id" userRoleAssignment.
     *
     * @param id the id of the userRoleAssignment to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userRoleAssignment, or with status 404 (Not Found)
     */
    @GetMapping("/user-role-assignments/{id}")
    @Timed
    public ResponseEntity<UserRoleAssignment> getUserRoleAssignment(@PathVariable Long id) {
        log.debug("REST request to get UserRoleAssignment : {}", id);
        UserRoleAssignment userRoleAssignment = userRoleAssignmentRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(userRoleAssignment));
    }

    /**
     * DELETE  /user-role-assignments/:id : delete the "id" userRoleAssignment.
     *
     * @param id the id of the userRoleAssignment to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-role-assignments/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserRoleAssignment(@PathVariable Long id) {
        log.debug("REST request to delete UserRoleAssignment : {}", id);
        userRoleAssignmentRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    /**
     * Author: Shiva
     * Param : roleId
     * Description: Updating User Role Assignment Record
     * result : void
     */
    @PostMapping("/updateUserRoleAssignmentById")
    @Timed
    public void updateUserRoleAssignmentById(@RequestBody RoleUsersDTO dto){
    	log.info("Rest api for updating user role assignment record of id: "+ dto.getId());
    	User currentUser = userService.getCurrentUser();
    	log.info("Currently logged in user: "+currentUser.getId());
    	if(dto.getId() != null)
    	{
    		UserRoleAssignment ura = userRoleAssignmentRepository.findOne(dto.getId());
    		if(ura != null)
    		{
    			ura.setUserId(dto.getUserId());
    			ura.setRoleId(dto.getRoleId());
    			ura.setAssignedBy(dto.getAssignedBy());
    			ura.setDeleteFlag(dto.getDeleteFlag());
    			ura.setActiveFlag(dto.getActiveFlag());
    			ura.setStartDate(dto.getStartDate());
    			if(dto.getEndDate() != null){
    				ura.setEndDate(dto.getEndDate());
    			}
    			else if(dto.getEndDate() == null){
    				ura.setEndDate(null);
    			}
    			ura.setLastUpdatedBy(currentUser.getId());
    			ura.setLastUpdatedDate(dto.getLastUpdatedDate());
    			UserRoleAssignment uraUpdate = userRoleAssignmentRepository.save(ura);
    			log.info("Update user role assignment record of id: "+ dto.getId());
    		}
    	}
    }
    
    /**
     * Author: Shiva
     * Param : userId, roleId
     * Description: Deleting user role assignment based on userId, roleId
     * result : void
     */
    @DeleteMapping("/deleteUserRoleByUserIdnRoleId")
    @Timed
    public void deleteUserRoleByUserIdnRoleId(@RequestParam Long userId, @RequestParam Long roleId){
    	log.info("Rest api for deleting user role assignment record based on user id: "+userId+", roleId: "+ roleId);
    	UserRoleAssignment ura = userRoleAssignmentRepository.findByUserIdAndRoleId(userId, roleId);
    	if(ura != null)
    	{
    		 userRoleAssignmentRepository.delete(ura.getId());
    		 log.info("User Role Assignment Record with id : "+ ura.getId()+" has been deleted.");
    	}
    }
    
    @PostMapping("/postUserRoleAssignment")
    @Timed
    public void postUserRoleAssignment(@RequestParam Long roleId, @RequestBody List<UserRoleAssignmentDTO> uraDto){
    	log.info("Rest api for posting user role assignment for the role_id: "+ roleId);
    	if(uraDto.size()>0)
    	{
			User currentUser = userService.getCurrentUser();
    		List<UserRoleAssignment> uraList = new ArrayList<UserRoleAssignment>();
    		for(UserRoleAssignmentDTO dto : uraDto)
    		{
    			UserRoleAssignment ura = new UserRoleAssignment();
    			if(dto.getId() != null)
    				ura.setUserId(dto.getId());	//Setting id as user_id
    			ura.setRoleId(roleId);
    				ura.setAssignedBy(currentUser.getId());
    			if(dto.getDeleteFlag() != null)
    				ura.setDeleteFlag(dto.getDeleteFlag());
    			if(dto.getActiveFlag() != null)
    				ura.setActiveFlag(dto.getActiveFlag());
    			if(dto.getStartDate() != null)
    				ura.setStartDate(dto.getStartDate());
    			if(dto.getEndDate() != null)
    				ura.setEndDate(dto.getEndDate());
    			if(dto.getEndDate() == null)
    				ura.setEndDate(null);
    			ura.setCreatedBy(currentUser.getId());
    			ura.setCreationDate(ZonedDateTime.now());
    			ura.setLastUpdatedBy(currentUser.getId());
    			ura.setLastUpdatedDate(ZonedDateTime.now());
    			uraList.add(ura);
    		}
    		List<UserRoleAssignment> insertURA = userRoleAssignmentRepository.save(uraList);
    	}
    }
    
}
