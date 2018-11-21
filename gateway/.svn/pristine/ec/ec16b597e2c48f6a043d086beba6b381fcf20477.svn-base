package com.nspl.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.domain.Functionality;
import com.nspl.app.domain.RoleFunctionAssignment;
import com.nspl.app.domain.Roles;
import com.nspl.app.domain.User;
import com.nspl.app.repository.FunctionalityRepository;
import com.nspl.app.repository.RoleFunctionAssignmentRepository;
import com.nspl.app.repository.RolesRepository;
import com.nspl.app.service.UserService;
import com.nspl.app.web.rest.dto.ErrorReport;
import com.nspl.app.web.rest.dto.FunctionalityAndRolesDTO;
import com.nspl.app.web.rest.dto.FunctionalityDTO;
import com.nspl.app.web.rest.dto.RoleAssignmentDTO;
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
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * REST controller for managing Functionality.
 */
@RestController
@RequestMapping("/api")
public class FunctionalityResource {

    private final Logger log = LoggerFactory.getLogger(FunctionalityResource.class);

    private static final String ENTITY_NAME = "functionality";

    private final FunctionalityRepository functionalityRepository;
    
    private final RoleFunctionAssignmentRepository roleFunctionAssignmentRepository;
    
    private final RolesRepository rolesRepository;
    
    private final UserService userService;

    public FunctionalityResource(FunctionalityRepository functionalityRepository,RoleFunctionAssignmentRepository roleFunctionAssignmentRepository,
    		RolesRepository rolesRepository,UserService userService) {
        this.functionalityRepository = functionalityRepository;
        this.roleFunctionAssignmentRepository=roleFunctionAssignmentRepository;
        this.rolesRepository=rolesRepository;
        this.userService=userService;
    }

    /**
     * POST  /functionalities : Create a new functionality.
     *
     * @param functionality the functionality to create
     * @return the ResponseEntity with status 201 (Created) and with body the new functionality, or with status 400 (Bad Request) if the functionality has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/functionalities")
    @Timed
    public ResponseEntity<Functionality> createFunctionality(@RequestBody Functionality functionality) throws URISyntaxException {
        log.debug("REST request to save Functionality : {}", functionality);
        if (functionality.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new functionality cannot already have an ID")).body(null);
        }
        Functionality result = functionalityRepository.save(functionality);
        return ResponseEntity.created(new URI("/api/functionalities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /functionalities : Updates an existing functionality.
     *
     * @param functionality the functionality to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated functionality,
     * or with status 400 (Bad Request) if the functionality is not valid,
     * or with status 500 (Internal Server Error) if the functionality couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/functionalities")
    @Timed
    public ResponseEntity<Functionality> updateFunctionality(@RequestBody Functionality functionality) throws URISyntaxException {
        log.debug("REST request to update Functionality : {}", functionality);
    	User currentUser = userService.getCurrentUser();
        log.info("currentUser" + currentUser.getId());
        if (functionality.getId() == null) {
            return createFunctionality(functionality);
        }
        functionality.setStartDate(functionality.getStartDate().plusDays(1));
        if(functionality.getEndDate()!=null){
        functionality.setEndDate(functionality.getEndDate().plusDays(1));
        }
        functionality.setLastUpdatedBy(currentUser.getId());
        functionality.setLastUpdatedDate(ZonedDateTime.now());
        Functionality result = functionalityRepository.save(functionality);
      
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, functionality.getId().toString()))
            .body(result);
    }


    /**
     * GET  /functionalities : get all the functionalities.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of functionalities in body
     */
   /* @GetMapping("/functionalities")
    @Timed
    public ResponseEntity<List<Functionality>> getAllFunctionalities(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Functionalities");
        Page<Functionality> page = functionalityRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/functionalities");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }*/

    /**
     * GET  /functionalities/:id : get the "id" functionality.
     *
     * @param id the id of the functionality to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the functionality, or with status 404 (Not Found)
     */
    @GetMapping("/functionalities/{id}")
    @Timed
    public ResponseEntity<Functionality> getFunctionality(@PathVariable Long id) {
        log.debug("REST request to get Functionality : {}", id);
        Functionality functionality = functionalityRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(functionality));
    }

    /**
     * DELETE  /functionalities/:id : delete the "id" functionality.
     *
     * @param id the id of the functionality to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/functionalities/{id}")
    @Timed
    public ResponseEntity<Void> deleteFunctionality(@PathVariable Long id) {
        log.debug("REST request to delete Functionality : {}", id);
        functionalityRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    /**
     * Author: Shiva
     * param : tenantId
     * Description: Fetching list of functionalities based on tenant id
     * result : List<Functionality>
     */ 
    @GetMapping("/getFunctionalatiesByTenantId")
    @Timed
    public List<Functionality> getFunctionalatiesByTenantId() {
/*    	Long tenantId=0l;
    	User currentUser = userService.getCurrentUser();
    	if(currentUser.getTenantId()!=null)
    		tenantId=currentUser.getTenantId();
    	
        log.debug("Rest api for fetching functionalities for the tenant id: "+ tenantId);
        List<Functionality> functions = functionalityRepository.fetchByTenantId(tenantId); */
    	List<Functionality> functions = functionalityRepository.findAll();
        log.info("No of records fetched : "+ functions.size());
        return functions;
    }
    
    
    
    /**
     * author :ravali
     * @Desc :Get all functionality with pagination
     * @param offset
     * @param limit
     * @param tenantId
     * @param response
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/functionalities")
 	@Timed
 	public ResponseEntity<List<Functionality>> getRuleGroupsByTenantId(@RequestParam(value = "page" , required = false) Integer offset,
 			@RequestParam(value = "per_page", required = false) Integer limit,HttpServletResponse response) throws URISyntaxException {
 		log.info("REST request to get a page of Functionalities");
 		Long tenantId=0l;
    	User currentUser = userService.getCurrentUser();
    	if(currentUser.getTenantId()!=null)
    		tenantId=currentUser.getTenantId();
 		List<Functionality> FunctionalityList = new ArrayList<Functionality>();
 		PaginationUtil paginationUtil=new PaginationUtil();
 		
 		int maxlmt=paginationUtil.MAX_LIMIT;
 		int minlmt=paginationUtil.MIN_OFFSET;
 		log.info("maxlmt: "+maxlmt);
 		Page<Functionality> page = null;
 		HttpHeaders headers = null;
 		
 		
 		List<Functionality> fuctListCnt = functionalityRepository.findByTenantIdOrderByIdDesc(tenantId);
    	response.addIntHeader("X-COUNT", fuctListCnt.size());
 		
 		
 		
 		if(limit==null || limit<minlmt){
 			FunctionalityList = functionalityRepository.findByTenantIdOrderByIdDesc(tenantId);
 			limit = FunctionalityList.size();
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
 			 page = functionalityRepository.findByTenantIdOrderByIdDesc(tenantId,PaginationUtil.generatePageRequest2(offset, limit));
 			headers = PaginationUtil.generatePaginationHttpHeaders2(page, "/api/functionalities",offset, limit);
 		}
 		else{
 			log.info("input limit is within maxlimit");
 			page = functionalityRepository.findByTenantIdOrderByIdDesc(tenantId,PaginationUtil.generatePageRequest(offset, limit));
 			headers = PaginationUtil.generatePaginationHttpHeaderss(page, "/api/functionalities", offset, limit);
 		}
 		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
     }
    
    
    /**
     * author:ravali
     * Get functionalities and their assignment details 
     * @param functId
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/getFunctionalitiesAndRespectiveRoles")
   	@Timed
   	public FunctionalityAndRolesDTO getFunctionalitiesAndRepectiveRoles(@RequestParam Long functId) throws URISyntaxException {
      
    	log.info("REST request to get FunctionalitiesAndRespectiveRoles");
      	 Functionality function = functionalityRepository.findOne(functId);
      	FunctionalityAndRolesDTO functionalityDTO = new FunctionalityAndRolesDTO();
  		 functionalityDTO.setId(functId);
  		 functionalityDTO.setFuncActiveFlag(Boolean.valueOf(function.getActiveInd()));
  		 functionalityDTO.setFuncDesc(function.getFuncDesc());
  		 functionalityDTO.setFuncName(function.getFuncName());
  		 functionalityDTO.setFuncType(function.getFuncType());
  		 functionalityDTO.setFuncStartDate(function.getStartDate());
  		 functionalityDTO.setFuncEndDate(function.getEndDate());
  		 functionalityDTO.setTenantId(function.getTenantId());
  		 List<RoleAssignmentDTO> roleAssignmentDTOList=new ArrayList<RoleAssignmentDTO>();
  		 List<RoleFunctionAssignment> roleFunctAssignmentList=roleFunctionAssignmentRepository.findByFunctionId(functId);
  		 for(RoleFunctionAssignment roleFunctAssignment:roleFunctAssignmentList)
  		 {
  			RoleAssignmentDTO roleAssignment=new RoleAssignmentDTO();
  			
  			Roles role=rolesRepository.findOne(roleFunctAssignment.getRoleId());
  			roleAssignment.setAssigmentId(roleFunctAssignment.getId());
  			roleAssignment.setRoleId(role.getId());
  			roleAssignment.setRoleName(role.getRoleName());
  			roleAssignment.setRoleDescription(role.getRoleDesc());
  			roleAssignment.setStartDate(roleFunctAssignment.getStartDate());
  			roleAssignment.setEndDate(roleFunctAssignment.getEndDate());
  			roleAssignment.setCanView(roleFunctAssignment.isCanView());
  			roleAssignment.setCanInsert(roleFunctAssignment.isCanInsert());
  			roleAssignment.setCanUpdate(roleFunctAssignment.isCanUpdate());
  			roleAssignment.setCanExecute(roleFunctAssignment.isCanExecute());
  			roleAssignment.setCanDelete(roleFunctAssignment.isCanDelete());
  			roleAssignment.setActiveFlag(roleFunctAssignment.isActiveFlag());
  			roleAssignment.setStatus(true);
  			roleAssignmentDTOList.add(roleAssignment);
  			
  		 }
  		functionalityDTO.setFuncRoles(roleAssignmentDTOList);
		return functionalityDTO;
  		 
    }
    
    /**
     * author:ravali
     * post functionalities and assigning a role to functionality
     * @param FunctionalityAndRolesDTO
     * @return
     */
    
    @PostMapping("/postFunctionalitiesAndRespectiveRoles")
   	@Timed
   	public HashMap postFunctionalitiesAndRepectiveRoles(@RequestBody FunctionalityAndRolesDTO FunctionalityAndRolesDTO){

    	log.info("REST request to post FunctionalitiesAndRespectiveRoles");
    	HashMap finalMap = new HashMap();
    	//	FunctionalityAndRolesDTO functionalityDTO = new FunctionalityAndRolesDTO();
    	User currentUser = userService.getCurrentUser();
    	Functionality function =new Functionality();
    	if(FunctionalityAndRolesDTO.getId()!=null)
    	{
    		function=functionalityRepository.findOne(FunctionalityAndRolesDTO.getId());
    	}
    	else
    	{
    		function.setActiveInd(FunctionalityAndRolesDTO.isFuncActiveFlag());
    		function.setFuncDesc(FunctionalityAndRolesDTO.getFuncDesc());
    		function.setFuncName(FunctionalityAndRolesDTO.getFuncName());
    		function.setFuncType(FunctionalityAndRolesDTO.getFuncType());
    		function.setStartDate(FunctionalityAndRolesDTO.getFuncStartDate().plusDays(1));
    		if(FunctionalityAndRolesDTO.getFuncEndDate() != null){
    			function.setEndDate(FunctionalityAndRolesDTO.getFuncEndDate().plusDays(1));
    		}
    		function.setTenantId(currentUser.getTenantId());
    		function.setCreatedBy(currentUser.getId());
    		function.setLastUpdatedBy(currentUser.getId());
    		function.setCreationDate(ZonedDateTime.now());
    		function.setLastUpdatedDate(ZonedDateTime.now());
    		functionalityRepository.save(function);
    	}
    	int count=0;
    	if(function.getId()!=null)
    	{
    		List<RoleAssignmentDTO> roleAssignmentDTOList=FunctionalityAndRolesDTO.getFuncRoles();
    		for(RoleAssignmentDTO roleAssignmentDTO:roleAssignmentDTOList)
    		{
    			RoleFunctionAssignment roleAssignment=new RoleFunctionAssignment();
    			Roles role=new Roles();


    			count=count+1;
    			/*role.setRoleName(roleAssignmentDTO.getRoleName());
    			role.setRoleDesc(roleAssignmentDTO.getRoleDescription());
    			role.setStartDate(roleAssignmentDTO.getStartDate());
    			role.setEndDate(roleAssignmentDTO.getEndDate());
    			role.setActiveInd(roleAssignmentDTO.getActiveFlag());
    			role.setTenantId(currentUser.getTenantId());
    			role.setCreatedBy(currentUser.getId());
    			role.setLastUpdatedBy(currentUser.getId());
    			role.setCreationDate(ZonedDateTime.now());
    			role.setLastUpdatedDate(ZonedDateTime.now());
    			rolesRepository.save(role);*/
    			
    			roleAssignment.setFunctionId(function.getId());
    			roleAssignment.setRoleId(roleAssignmentDTO.getRoleId());
    			roleAssignment.setCanView(roleAssignmentDTO.getCanView());
    			roleAssignment.setCanInsert(roleAssignmentDTO.getCanInsert());
    			roleAssignment.setCanUpdate(roleAssignmentDTO.getCanUpdate());
    			roleAssignment.setCanExecute(roleAssignmentDTO.getCanExecute());
    			roleAssignment.setCanDelete(roleAssignmentDTO.getCanDelete());
    			roleAssignment.setActiveFlag(roleAssignmentDTO.getActiveFlag());
    			roleAssignment.setStartDate(roleAssignmentDTO.getStartDate().plusDays(1));
    			if(roleAssignmentDTO.getEndDate() != null){
    				roleAssignment.setEndDate(roleAssignmentDTO.getEndDate().plusDays(1));
    			}
    			roleAssignment.setCreatedBy(currentUser.getId());
    			roleAssignment.setLastUpdateBy(currentUser.getId());
    			roleAssignment.setCreationDate(ZonedDateTime.now());
    			roleAssignment.setLastUpdatedDate(ZonedDateTime.now());
    			roleAssignment.setActiveFlag(true);
    			roleFunctionAssignmentRepository.save(roleAssignment);

    		}
    	}
    	finalMap.put("id", function.getId());
    	finalMap.put("name", function.getFuncName());
    	finalMap.put("count", count +" roles has been tagged to functionality "+function.getFuncName());
    	return finalMap;

    }
   
    /**
     * Author: Shiva
     * Purpose: Check weather functionality name exist or not
     * **/
    @GetMapping("/checkFunctionalityIsExist")
	@Timed
	public HashMap checkFunctionalityIsExist(@RequestParam String name,@RequestParam(required=false,value="id") Long id)
	{
    	Long tenantId=userService.getCurrentUser().getTenantId();
		HashMap map=new HashMap();
		map.put("result", "No Duplicates Found");
		if(id != null)
		{
			Functionality funcWithId = functionalityRepository.findByIdAndFuncNameAndTenantId(id, name, tenantId);
			if(funcWithId != null)
			{
				map.put("result", "No Duplicates Found");
			}
			else
			{
				List<Functionality> funs = functionalityRepository.findByTenantIdAndFuncName(tenantId, name);
				if(funs.size()>0)
				{
					map.put("result", "'"+name+"' function already exists");
				}
			}
		}
		else 
		{
			List<Functionality> funs = functionalityRepository.findByTenantIdAndFuncName(tenantId, name);
			if(funs.size()>0)
			{
				map.put("result", "'"+name+"' function already exists");
			}
		}
		return map;
	}
}
