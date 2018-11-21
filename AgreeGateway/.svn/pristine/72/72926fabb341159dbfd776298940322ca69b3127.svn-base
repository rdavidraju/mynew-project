package com.nspl.app.web.rest;

import io.github.jhipster.web.util.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.nspl.app.domain.RoleFunctionAssignment;
import com.nspl.app.domain.User;
import com.nspl.app.repository.RoleFunctionAssignmentRepository;
import com.nspl.app.service.UserService;
import com.nspl.app.service.dto.RoleFunctionsDTO;
import com.nspl.app.web.rest.util.HeaderUtil;

/**
 * REST controller for managing RoleFunctionAssignment.
 */
@RestController
@RequestMapping("/api")
public class RoleFunctionAssignmentResource {

    private final Logger log = LoggerFactory.getLogger(RoleFunctionAssignmentResource.class);

    private static final String ENTITY_NAME = "roleFunctionAssignment";

    private final RoleFunctionAssignmentRepository roleFunctionAssignmentRepository;
    
    private final UserService userService;

    public RoleFunctionAssignmentResource(RoleFunctionAssignmentRepository roleFunctionAssignmentRepository, UserService userService) {
        this.roleFunctionAssignmentRepository = roleFunctionAssignmentRepository;
        this.userService = userService;
    }

    /**
     * POST  /role-function-assignments : Create a new roleFunctionAssignment.
     *
     * @param roleFunctionAssignment the roleFunctionAssignment to create
     * @return the ResponseEntity with status 201 (Created) and with body the new roleFunctionAssignment, or with status 400 (Bad Request) if the roleFunctionAssignment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/role-function-assignments")
    @Timed
    public ResponseEntity<RoleFunctionAssignment> createRoleFunctionAssignment(@RequestBody RoleFunctionAssignment roleFunctionAssignment) throws URISyntaxException {
        log.debug("REST request to save RoleFunctionAssignment : {}", roleFunctionAssignment);
        if (roleFunctionAssignment.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new roleFunctionAssignment cannot already have an ID")).body(null);
        }
        RoleFunctionAssignment result = roleFunctionAssignmentRepository.save(roleFunctionAssignment);
        return ResponseEntity.created(new URI("/api/role-function-assignments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /role-function-assignments : Updates an existing roleFunctionAssignment.
     *
     * @param roleFunctionAssignment the roleFunctionAssignment to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated roleFunctionAssignment,
     * or with status 400 (Bad Request) if the roleFunctionAssignment is not valid,
     * or with status 500 (Internal Server Error) if the roleFunctionAssignment couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/role-function-assignments")
    @Timed
    public ResponseEntity<RoleFunctionAssignment> updateRoleFunctionAssignment(@RequestBody RoleFunctionAssignment roleFunctionAssignment) throws URISyntaxException {
        log.debug("REST request to update RoleFunctionAssignment : {}", roleFunctionAssignment);
        User currentUser = userService.getCurrentUser();
        if (roleFunctionAssignment.getId() == null) {
            return createRoleFunctionAssignment(roleFunctionAssignment);
        }
        //roleFunctionAssignment.setCreatedBy(currentUser.getId());
        roleFunctionAssignment.setStartDate(roleFunctionAssignment.getStartDate().plusDays(1));
        if(roleFunctionAssignment.getEndDate()!=null)
        roleFunctionAssignment.setEndDate(roleFunctionAssignment.getEndDate().plusDays(1));
        roleFunctionAssignment.setLastUpdateBy(currentUser.getId());
        //roleFunctionAssignment.setCreationDate(ZonedDateTime.now());
        roleFunctionAssignment.setLastUpdatedDate(ZonedDateTime.now());
        RoleFunctionAssignment result = roleFunctionAssignmentRepository.save(roleFunctionAssignment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, roleFunctionAssignment.getId().toString()))
            .body(result);
    }

    /**
     * GET  /role-function-assignments : get all the roleFunctionAssignments.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of roleFunctionAssignments in body
     */
    @GetMapping("/role-function-assignments")
    @Timed
    public List<RoleFunctionAssignment> getAllRoleFunctionAssignments() {
        log.debug("REST request to get all RoleFunctionAssignments");
        return roleFunctionAssignmentRepository.findAll();
    }

    /**
     * GET  /role-function-assignments/:id : get the "id" roleFunctionAssignment.
     *
     * @param id the id of the roleFunctionAssignment to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the roleFunctionAssignment, or with status 404 (Not Found)
     */
    @GetMapping("/role-function-assignments/{id}")
    @Timed
    public ResponseEntity<RoleFunctionAssignment> getRoleFunctionAssignment(@PathVariable Long id) {
        log.debug("REST request to get RoleFunctionAssignment : {}", id);
        RoleFunctionAssignment roleFunctionAssignment = roleFunctionAssignmentRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(roleFunctionAssignment));
    }

    /**
     * DELETE  /role-function-assignments/:id : delete the "id" roleFunctionAssignment.
     *
     * @param id the id of the roleFunctionAssignment to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/role-function-assignments/{id}")
    @Timed
    public ResponseEntity<Void> deleteRoleFunctionAssignment(@PathVariable Long id) {
        log.debug("REST request to delete RoleFunctionAssignment : {}", id);
        roleFunctionAssignmentRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    /**
     * Author: Shiva
     * Param : RoleFunctionsDTO
     * Description: Updating Role Function Assignment
     * result : void
     */
    @PostMapping("/updateRoleFunctionAssignmentById")
    @Timed
    public void updateRoleFunctionAssignmentById(@RequestBody RoleFunctionsDTO dto){
    	log.info("Rest api for updating role functions assignment of id : "+ dto.getId());
    	RoleFunctionAssignment rfa = roleFunctionAssignmentRepository.findOne(dto.getId());
    	User currentUser = userService.getCurrentUser();
    	log.info("Currently logged in user: "+currentUser.getId());
    	if(rfa != null)
    	{
    		rfa.setRoleId(dto.getRoleId());
    		rfa.setFunctionId(dto.getFunctionId());
    		rfa.setAssignedBy(dto.getAssignedBy());
    		rfa.setActiveFlag(dto.getActiveFlag());
    		rfa.setDeleteFlag(dto.getDeleteFlag());
    		rfa.setStartDate(dto.getStartDate().plusDays(1));
    		if(dto.getEndDate() != null){
    			rfa.setEndDate(dto.getEndDate().plusDays(1));
    		}
    		else if(dto.getEndDate() == null)
    		{
    			rfa.setEndDate(null);
    		}
    		rfa.setCanView(dto.getCanView());
    		rfa.setCanInsert(dto.getCanInsert());
    		rfa.setCanUpdate(dto.getCanUpdate());
    		rfa.setCanExecute(dto.getCanExecute());
    		rfa.setCanDelete(dto.getCanDelete());
    		rfa.setLastUpdateBy(currentUser.getId());
    		rfa.setLastUpdatedDate(dto.getLastUpdatedDate());
    		RoleFunctionAssignment updateRfa = roleFunctionAssignmentRepository.save(rfa);
    		log.info("Updated role function assignment record: "+ rfa.getId());
    	}
    }
    
    /**
     * Author: Shiva
     * Param : functionId, roleId
     * Description: Deleting role function assignment based on functionId, roleId
     * result : void
     */
    @DeleteMapping("/deleteRoleFunctionByFunctionIdnRoleId")
    @Timed
    public void deleteRoleFunctionByFunctionIdnRoleId(@RequestParam Long functionId, @RequestParam Long roleId){
    	log.info("Rest api for deleting role function assignment record based on function id: "+functionId+", roleId: "+ roleId);
    	RoleFunctionAssignment rfa = roleFunctionAssignmentRepository.findByFunctionIdAndRoleId(functionId, roleId);
    	if(rfa != null)
    	{
    		roleFunctionAssignmentRepository.delete(rfa.getId());
    		 log.info("Role Function Assignment Record with id : "+ rfa.getId()+" has been deleted.");
    	}
    }
    
    /**
     * Author: Shiva
     * param : tenantId
     * Description: Fetching list of functionalities based on tenant id
     * result : List<Functionality>
     */ 
    @PostMapping("/postRoleFunctionAssignment")
    @Timed
    public void postRoleFunctionAssignment(@RequestParam Long roleId, @RequestBody List<RoleFunctionsDTO> roleFunctionsDTO) {
    	log.info("Rest api for posting role function assignment for the role id : "+ roleId);
    	if(roleFunctionsDTO.size()>0)
    	{
			User currentUser = userService.getCurrentUser();
    		List<RoleFunctionAssignment> rfaList = new ArrayList<RoleFunctionAssignment>();
    		for(RoleFunctionsDTO rolFnc : roleFunctionsDTO)
    		{
    			RoleFunctionAssignment rfa = new RoleFunctionAssignment();
    			rfa.setRoleId(roleId);
    			if(rolFnc.getId() != null)
    				rfa.setFunctionId(rolFnc.getId());	//Setting id as function id
    				rfa.setAssignedBy(currentUser.getId());
    				rfa.setActiveFlag(true);
    			if(rolFnc.getDeleteFlag() != null)
    				rfa.setDeleteFlag(rolFnc.getDeleteFlag());
    			if(rolFnc.getStartDate() != null)
    				rfa.setStartDate(rolFnc.getStartDate().plusDays(1));
    			if(rolFnc.getEndDate() != null)
    				rfa.setEndDate(rolFnc.getEndDate().plusDays(1));
    			if(rolFnc.getEndDate() == null)
    				rfa.setEndDate(null);
    			if(rolFnc.getCanView() != null)
    				rfa.setCanView(rolFnc.getCanView());
    			if(rolFnc.getCanInsert() != null)
    				rfa.setCanInsert(rolFnc.getCanInsert());
    			if(rolFnc.getCanUpdate() != null)
    				rfa.setCanUpdate(rolFnc.getCanUpdate());
    			if(rolFnc.getCanExecute() !=null)
    				rfa.setCanExecute(rolFnc.getCanExecute());
    			if(rolFnc.getCanDelete() != null)
    				rfa.setCanDelete(rolFnc.getCanDelete());
    			rfa.setCreatedBy(currentUser.getId());
    			rfa.setLastUpdateBy(currentUser.getId());
    			rfa.setCreationDate(ZonedDateTime.now());
    			rfa.setLastUpdatedDate(ZonedDateTime.now());
    			rfaList.add(rfa);
    		}
    		List<RoleFunctionAssignment> insertrfaList = roleFunctionAssignmentRepository.save(rfaList);
    		log.info(insertrfaList + " records inserted. ");
    	}
    }
}
