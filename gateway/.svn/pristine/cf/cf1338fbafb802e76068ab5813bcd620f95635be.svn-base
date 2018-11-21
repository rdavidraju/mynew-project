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
import com.nspl.app.domain.Functionality;
import com.nspl.app.domain.RoleFunctionAssignment;
import com.nspl.app.domain.Roles;
import com.nspl.app.domain.User;
import com.nspl.app.domain.UserRoleAssignment;
import com.nspl.app.repository.FunctionalityRepository;
import com.nspl.app.repository.RoleFunctionAssignmentRepository;
import com.nspl.app.repository.RolesRepository;
import com.nspl.app.repository.UserRepository;
import com.nspl.app.repository.UserRoleAssignmentRepository;
import com.nspl.app.service.UserService;
import com.nspl.app.service.dto.RoleFuncUsersDTO;
import com.nspl.app.service.dto.RoleFunctionsDTO;
import com.nspl.app.service.dto.RoleUsersDTO;
import com.nspl.app.service.dto.UserRolesDTO;
import com.nspl.app.web.rest.dto.FunctionalityDTO;
import com.nspl.app.web.rest.dto.RolesDTO;
import com.nspl.app.web.rest.util.HeaderUtil;
import com.nspl.app.web.rest.util.PaginationUtil;

/**
 * REST controller for managing Roles.
 */
@RestController
@RequestMapping("/api")
public class RolesResource {

    private final Logger log = LoggerFactory.getLogger(RolesResource.class);

    private static final String ENTITY_NAME = "roles";

    private final RolesRepository rolesRepository;
    private final UserService userService;
    private final FunctionalityRepository functionalityRepository;
    private final RoleFunctionAssignmentRepository roleFunctionAssignmentRepository;
    private final UserRoleAssignmentRepository userRoleAssignmentRepository;
    private final UserRepository userRepository;
    
    public RolesResource(RolesRepository rolesRepository,
    		UserService userService,FunctionalityRepository functionalityRepository,
    		RoleFunctionAssignmentRepository roleFunctionsAssignmentRepository, UserRoleAssignmentRepository userRoleAssignmentRepository, UserRepository userRepository) {
        this.rolesRepository = rolesRepository;
        this.userService = userService;
        this.functionalityRepository =functionalityRepository;
        this.roleFunctionAssignmentRepository = roleFunctionsAssignmentRepository;
        this.userRoleAssignmentRepository = userRoleAssignmentRepository;
        this.userRepository = userRepository;
    }

    /**
     * POST  /roles : Create a new roles.
     *
     * @param roles the roles to create
     * @return the ResponseEntity with status 201 (Created) and with body the new roles, or with status 400 (Bad Request) if the roles has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/roles")
    @Timed
    public ResponseEntity<Roles> createRoles(@RequestBody Roles roles) throws URISyntaxException {
        log.debug("REST request to save Roles : {}", roles);
        if (roles.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new roles cannot already have an ID")).body(null);
        }
        User currentUser = userService.getCurrentUser();
        roles.setStartDate(roles.getStartDate());
        if(roles.getEndDate()!=null){
        roles.setEndDate(roles.getEndDate());
        }
        roles.setTenantId(currentUser.getTenantId());
        roles.setCreatedBy(currentUser.getId());
        roles.setCreationDate(ZonedDateTime.now());
        roles.setLastUpdatedBy(currentUser.getId());
        roles.setLastUpdatedDate(ZonedDateTime.now());
        Roles result = rolesRepository.save(roles);
        return ResponseEntity.created(new URI("/api/roles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /roles : Updates an existing roles.
     *
     * @param roles the roles to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated roles,
     * or with status 400 (Bad Request) if the roles is not valid,
     * or with status 500 (Internal Server Error) if the roles couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/roles")
    @Timed
    public ResponseEntity<Roles> updateRoles(@RequestBody Roles roles) throws URISyntaxException {
        log.debug("REST request to update Roles : {}", roles);
        if (roles.getId() == null) {
            return createRoles(roles);
        }
        Roles result = rolesRepository.save(roles);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, roles.getId().toString()))
            .body(result);
    }

    /**
     * GET  /roles : get all the roles.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of roles in body
     */
   /* @GetMapping("/roles")
    @Timed
    public ResponseEntity<List<Roles>> getAllRoles(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Roles");
        Page<Roles> page = rolesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/roles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }*/
    
    /**
     * author :ravali
     * @param offset
     * @param limit
     * @param tenantId
     * @param response
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/roles")
    @Timed
	public ResponseEntity<List<RolesDTO>> getRuleGroupsByTenantId(@RequestParam(value = "page" , required = false) Integer offset,
 			@RequestParam(value = "per_page", required = false) Integer limit,HttpServletResponse response) throws URISyntaxException {
 		log.debug("REST request to get a page of Rule groups");
 		List<Roles> rolesList = new ArrayList<Roles>();
 		List<RolesDTO> finalList = new ArrayList<RolesDTO>();
 		PaginationUtil paginationUtil=new PaginationUtil();
 		User currentUser = userService.getCurrentUser();
 		int maxlmt=paginationUtil.MAX_LIMIT;
 		int minlmt=paginationUtil.MIN_OFFSET;
 		log.info("maxlmt: "+maxlmt);
 		Page<Roles> page = null;
 		HttpHeaders headers = null;

 		List<Long> tenantIds = new ArrayList<Long>();
 		tenantIds.add(0L);
 		tenantIds.add(currentUser.getTenantId());
 		
 		List<Roles> rolesListCnt = rolesRepository.findByTenantIdInOrderByIdDesc(tenantIds);
    	response.addIntHeader("X-COUNT", rolesListCnt.size());
 		
 		if(limit==null || limit<minlmt){
 			rolesList = rolesRepository.findByTenantIdInOrderByIdDesc(tenantIds);
 			limit = rolesList.size();
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
 			page = rolesRepository.findByTenantIdInOrderByIdDesc(tenantIds,PaginationUtil.generatePageRequest2(offset, limit));
 			headers = PaginationUtil.generatePaginationHttpHeaders2(page, "/api/functionalities",offset, limit); 
 		}
 		else{
 			log.info("input limit is within maxlimit");
 			page = rolesRepository.findByTenantIdInOrderByIdDesc(tenantIds,PaginationUtil.generatePageRequest(offset, limit));
 			headers = PaginationUtil.generatePaginationHttpHeaderss(page, "/api/functionalities", offset, limit);
 		}
     	if(page.getSize()>0)
     	{
     		for(Roles role : page)
     		{
     			RolesDTO roleDTO = new RolesDTO();
     			roleDTO.setId(role.getId());
     			roleDTO.setRoleCode(role.getRoleCode());
     			roleDTO.setRoleDesc(role.getRoleDesc());
     			roleDTO.setRoleName(role.getRoleName());
     			roleDTO.setStartDate(role.getStartDate());
     			roleDTO.setEndDate(role.getEndDate());
     			roleDTO.setActiveInd(role.getActiveInd());
     			roleDTO.setTenantId(role.getTenantId());
     			List<RoleFunctionAssignment> rfa = roleFunctionAssignmentRepository.findByRoleId(role.getId());
     			roleDTO.setFunctionalitiesCount(rfa.size());
     			List<UserRoleAssignment> ura = userRoleAssignmentRepository.findByRoleId(role.getId());
     			roleDTO.setUsersCount(ura.size());
     			finalList.add(roleDTO);
     		}
     	}
 		return new ResponseEntity<>(finalList, headers, HttpStatus.OK);
     }
    
    
    

    /**
     * GET  /roles/:id : get the "id" roles.
     *
     * @param id the id of the roles to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the roles, or with status 404 (Not Found)
     */
    @GetMapping("/roles/{id}")
    @Timed
    public ResponseEntity<Roles> getRoles(@PathVariable Long id) {
        log.debug("REST request to get Roles : {}", id);
        Roles roles = rolesRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(roles));
    }

    /**
     * DELETE  /roles/:id : delete the "id" roles.
     *
     * @param id the id of the roles to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/roles/{id}")
    @Timed
    public ResponseEntity<Void> deleteRoles(@PathVariable Long id) {
        log.debug("REST request to delete Roles : {}", id);
        rolesRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    /**
     * GET  /rolesAndFunctionsByUserId : get roles for input user
     * 
     */
    @GetMapping("/rolesAndFunctionsByUserId")
    @Timed
    public List<RolesDTO> fetchRolesAndFunctionsByUserId(@RequestParam Long userId) {
        log.debug("REST request to get a page of Roles");
        User currentUser = userService.getCurrentUser();
        List<RolesDTO> rolesAndFunctions = new ArrayList<RolesDTO>();
        
        List<Roles> roles = rolesRepository.fetchAssignedRolesByUserId(userId);
        for(Roles role : roles)
        {
        	RolesDTO roleDTO = new RolesDTO();
        	roleDTO.setRoleName(role.getRoleName());
        	roleDTO.setRoleDesc(role.getRoleDesc());
        	roleDTO.setStartDate(role.getStartDate());
        	roleDTO.setEndDate(role.getEndDate());
        	roleDTO.setActiveInd(role.getActiveInd());
        	roleDTO.setId(role.getId());
        	
        	List<RoleFunctionAssignment> roleFunctionAssignments = new ArrayList<RoleFunctionAssignment>();
        	roleFunctionAssignments = roleFunctionAssignmentRepository.findByRoleId(role.getId());
        	
        	List<FunctionalityDTO> functionsListDTO = new ArrayList<FunctionalityDTO>();
        	
        	 for(RoleFunctionAssignment rfa : roleFunctionAssignments)
        	 {
        		 Functionality function = new Functionality();
        		 function = functionalityRepository.findOne(rfa.getFunctionId());
        		 FunctionalityDTO functionalityDTO = new FunctionalityDTO();
        		 functionalityDTO.setActiveFlag(Boolean.valueOf(function.getActiveInd()));
        		 functionalityDTO.setCanDelete(rfa.isCanDelete());
        		 functionalityDTO.setCanExecute(rfa.isCanExecute());
        		 functionalityDTO.setCanInsert(rfa.isCanInsert());
        		 functionalityDTO.setCanUpdate(rfa.isCanUpdate());
        		 functionalityDTO.setCanView(rfa.isCanView());
        		 functionalityDTO.setFuncDesc(function.getFuncDesc());
        		 functionalityDTO.setFuncName(function.getFuncName());
        		 functionalityDTO.setId(function.getId());
        		 functionalityDTO.setFuncAssignEndDate(rfa.getEndDate());
        		 functionalityDTO.setFuncAssignStartDate(rfa.getStartDate());
        		 functionalityDTO.setFuncStartDate(function.getStartDate());
        		 functionalityDTO.setFuncEndDate(function.getEndDate());
        		 
        		 functionsListDTO.add(functionalityDTO);
        	 }
        	roleDTO.setFunctions(functionsListDTO);
        	rolesAndFunctions.add(roleDTO);
        }
        return rolesAndFunctions;
    }
    @GetMapping("/rolesForTenant")
    @Timed
    public List<Roles> fetchRolesForTenant() {
        log.debug("REST request to get Roles");
        List<Roles> roles = new ArrayList<Roles>();
        List<Roles> defaultRoles = new ArrayList<Roles>();
        User currentUser = userService.getCurrentUser();
        List<Long> tenantIds = new ArrayList<Long>();
        tenantIds.add(0L);
        tenantIds.add(currentUser.getTenantId());
        roles = rolesRepository.findByTenantIdIn(tenantIds);
        
        return  roles;
        }
    
    /**
     * Author: Shiva
     * Param : RoleFuncUsersDTO with Roles, Functions, User Details
     * Description: Posting Role Function Assignment, Role User Assignment
     * result : HashMap with role id and role name
     */
    @PostMapping("/postRoleFunctionsUsersAssignment")
    @Timed
    public HashMap postRoleFunctionsUsersAssignment(@RequestBody RoleFuncUsersDTO dto) {
    	log.info("Rest api for posting role function and role user assignment");
    	User currentUser = userService.getCurrentUser();
    	log.info("Currently logged in user: "+currentUser.getId());
    	HashMap finalMap = new HashMap();
    	List<RoleFunctionsDTO> roleFncs = new ArrayList<RoleFunctionsDTO>();
    	if(dto.getRoleFncs() != null)	// Role Function Assignment
    		roleFncs = dto.getRoleFncs();
    	List<RoleUsersDTO> usrRol = new ArrayList<RoleUsersDTO>();
    	if(dto.getUsrRol() != null)	// User Role Assignment
    		usrRol = dto.getUsrRol();
    	// Getting Roles Details
    	Roles role = new Roles();
    	//if(dto.getTenantId() != null)
    		role.setTenantId(currentUser.getTenantId());
    	if(dto.getRoleName() != null)
    		role.setRoleName(dto.getRoleName());
    	if(dto.getRoleDescription() != null)
    		role.setRoleDesc(dto.getRoleDescription());
    	if(dto.getStartDate() != null)
    		role.setStartDate(dto.getStartDate());
    	if(dto.getEndDate() != null){
    		role.setEndDate(dto.getEndDate());
    	}
    	if(dto.getEndDate() == null){
    		role.setEndDate(null);
    	}
    	if(dto.getActiveInd() != null)
    		role.setActiveInd(dto.getActiveInd());
    	Roles newRole = rolesRepository.save(role);		// Posting Role
    	if(newRole != null)
    	{
    		if(roleFncs.size()>0) // posting role function assignment
    		{
    			for(RoleFunctionsDTO rfa : roleFncs)
    			{
    				RoleFunctionAssignment newRfa = new RoleFunctionAssignment();
    				newRfa.setRoleId(newRole.getId());
    				newRfa.setFunctionId(rfa.getFunctionId());
    				newRfa.setAssignedBy(rfa.getAssignedBy());
    				newRfa.setActiveFlag(rfa.getActiveFlag());
    				newRfa.setDeleteFlag(rfa.getDeleteFlag());
    				newRfa.setStartDate(rfa.getStartDate());
    				if(rfa.getEndDate() != null){
    					newRfa.setEndDate(rfa.getEndDate());
    				}
    				if(rfa.getEndDate() == null){
        				newRfa.setEndDate(null);
        			}
    				newRfa.setCanView(rfa.getCanView());
    				newRfa.setCanInsert(rfa.getCanInsert());
    				newRfa.setCanUpdate(rfa.getCanUpdate());
    				newRfa.setCanExecute(rfa.getCanExecute());
    				newRfa.setCanDelete(rfa.getCanDelete());
    				newRfa.setCreatedBy(currentUser.getId());
    				newRfa.setLastUpdateBy(currentUser.getId());
    				newRfa.setCreationDate(ZonedDateTime.now());
    				newRfa.setLastUpdatedDate(ZonedDateTime.now());
    				RoleFunctionAssignment roleFncAssgnmt = roleFunctionAssignmentRepository.save(newRfa);
    			}
    		}
    		if(usrRol.size()>0)	// posting User Role Assignment
    		{
    			for(RoleUsersDTO ura : usrRol)
    			{
    				UserRoleAssignment newUra = new UserRoleAssignment();
    				newUra.setUserId(ura.getUserId());
    				newUra.setRoleId(newRole.getId());
    				newUra.setAssignedBy(ura.getAssignedBy());
    				newUra.setDeleteFlag(ura.getDeleteFlag());
    				newUra.setActiveFlag(ura.getActiveFlag());
    				newUra.setStartDate(ura.getStartDate());
    				if(ura.getEndDate() != null){
    					newUra.setEndDate(ura.getEndDate());
    				}
    				if(ura.getEndDate() == null){
    					newUra.setEndDate(null);
    				}
    				newUra.setCreatedBy(currentUser.getId());
    				newUra.setLastUpdatedBy(currentUser.getId());
    				newUra.setCreationDate(ZonedDateTime.now());
    				newUra.setLastUpdatedDate(ZonedDateTime.now());
    				UserRoleAssignment usrRoleAssgmnt = userRoleAssignmentRepository.save(newUra);
    			}
    		}
    	}
    	finalMap.put("id", newRole.getId());
    	finalMap.put("name", newRole.getRoleName());
    	return finalMap;
    }
    
    /**
     * Author: Shiva
     * Param : roleId
     * Description: Fetching role functions, user role assignments based on role id
     * result : HashMap with role id and role name
     */
    @GetMapping("/getRoleFunctionsNUserRoleAssignmentsByRoleId")
    @Timed
    public RoleFuncUsersDTO postRoleFunctionsUsersAssignment(@RequestParam Long roleId){
    	log.info("Rest api for fetching role functions and user role assignments for the role id: "+ roleId);
    //	Long tenantId=userService.getCurrentUser().getTenantId();
    	RoleFuncUsersDTO finalData = new RoleFuncUsersDTO();
    	Roles role = rolesRepository.findOne(roleId);
    	List<RoleFunctionsDTO> roleFncs = new ArrayList<RoleFunctionsDTO>();
    	List<RoleUsersDTO> usrRol = new ArrayList<RoleUsersDTO>();
    	if(role != null)
    	{		// Getting Role Details
    		finalData.setId(role.getId());
    		if(role.getTenantId() == 0L)
    		{
    			finalData.setIsDefaultRole(true);
    		}
    		else
    		{
    			finalData.setIsDefaultRole(false);
    		}
    		finalData.setTenantId(role.getTenantId());
    		finalData.setRoleCode(role.getRoleCode());
    		finalData.setRoleName(role.getRoleName());
    		finalData.setRoleDescription(role.getRoleDesc());
    		finalData.setStartDate(role.getStartDate());
    		finalData.setEndDate(role.getEndDate());
    		finalData.setActiveInd(role.getActiveInd());
    		List<RoleFunctionAssignment> rfas = roleFunctionAssignmentRepository.findByRoleId(roleId); // Getting Role Functions
    		if(rfas.size()>0)	
    		{
    			for(RoleFunctionAssignment rfa : rfas)
    			{
    				RoleFunctionsDTO rf = new RoleFunctionsDTO();
    				rf.setId(rfa.getId());
    				rf.setRoleId(rfa.getRoleId());
    				rf.setFunctionId(rfa.getFunctionId());
    				rf.setAssignedBy(rfa.getAssignedBy());
    				rf.setActiveFlag(rfa.isActiveFlag());
    				rf.setDeleteFlag(rfa.isDeleteFlag());
    				rf.setStartDate(rfa.getStartDate());
    				rf.setEndDate(rfa.getEndDate());
    				rf.setCanView(rfa.isCanView());
    				rf.setCanInsert(rfa.isCanInsert());
    				rf.setCanUpdate(rfa.isCanUpdate());
    				rf.setCanExecute(rfa.isCanExecute());
    				rf.setCanDelete(rfa.isCanDelete());
    				rf.setCreatedBy(rfa.getCreatedBy());
    				rf.setCreationDate(rfa.getCreationDate());
    				rf.setLastUpdateBy(rfa.getLastUpdateBy());
    				rf.setLastUpdatedDate(rfa.getLastUpdatedDate());
    				Functionality fn = functionalityRepository.findOne(rfa.getFunctionId());
    				if(fn != null)
    				{
    					rf.setFuncName(fn.getFuncName());
    					rf.setFuncDesc(fn.getFuncDesc());
    					rf.setFuncType(fn.getFuncType());
    				}
    				roleFncs.add(rf);
    			}
    		}
    		finalData.setRoleFncs(roleFncs);
    		List<UserRoleAssignment> uras = userRoleAssignmentRepository.findByRoleId(roleId); // Getting Role Users
    		if(uras.size()>0)
    		{
    			for(UserRoleAssignment ura : uras)
    			{
    				RoleUsersDTO ru = new RoleUsersDTO();
    				ru.setId(ura.getId());
    				ru.setUserId(ura.getUserId());
    				ru.setRoleId(ura.getRoleId());
    				ru.setAssignedBy(ura.getAssignedBy());
    				ru.setDeleteFlag(ura.isDeleteFlag());
    				ru.setCreatedBy(ura.getCreatedBy());
    				ru.setLastUpdatedBy(ura.getLastUpdatedBy());
    				ru.setCreationDate(ura.getCreationDate());
    				ru.setLastUpdatedDate(ura.getLastUpdatedDate());
    				ru.setActiveFlag(ura.isActiveFlag());
    				ru.setStartDate(ura.getStartDate());
    				ru.setEndDate(ura.getEndDate());
    				User user = userRepository.findOne(ura.getUserId());
    				if(user != null)
    				{
    					ru.setUserName(user.getLogin());
    				}
    				usrRol.add(ru);
    			}
    		}
    		finalData.setUsrRol(usrRol);
    	}
    	return finalData;
    }
    
    /**
     * Author: Shiva
     * Param : roleId
     * Description: Updating Role Record 
     * result : void
     */
    @PostMapping("/updateRoleBasedonId")
    @Timed
    public void updateRoleBasedonId(@RequestBody RoleFuncUsersDTO dto){
    	log.info("Rest api for updating role id : "+ dto.getId());
    	Long tenantId=userService.getCurrentUser().getTenantId();
    	if(dto.getId() != null)
    	{
    		Roles role = rolesRepository.findOne(dto.getId());
    		if(role != null)
    		{
    			role.setRoleCode(dto.getRoleCode());
    			role.setRoleName(dto.getRoleName());
    			role.setRoleDesc(dto.getRoleDescription());
    			role.setTenantId(tenantId);
    			role.setStartDate(dto.getStartDate());
    			if(dto.getEndDate() != null){
    				role.setEndDate(dto.getEndDate());
    			}
    			else if(dto.getEndDate() == null)
    			{
    				role.setEndDate(null);
    			}
    			role.setActiveInd(dto.getActiveInd());
    			Roles roleUpdate = rolesRepository.save(role);
    		}
    		log.info("Updated role with id: "+ dto.getId());
    	}
    }
    
   @GetMapping("/getUsersBasedOnRoleName")
    @Timed
    public List<User> getUsersBasedOnRoleName(@RequestParam String roleName){
	   log.info("Rest API to get users of a roleCode :"+roleName);
	   User currentUser = userService.getCurrentUser();
	   List<User> userList=new ArrayList<User>();
	   if(currentUser.getTenantId()!=null)
	   {
		   List<Long> tenantIds = new ArrayList<Long>();
		   tenantIds.add(0L);
		   tenantIds.add(currentUser.getTenantId());
	   Roles role=rolesRepository.findByRoleCodeAndTenantIdIn(roleName,tenantIds);
	   log.info("rolesIds :"+role);
	   if(role!=null)
	   {
		   List<BigInteger> userIds=userRoleAssignmentRepository.fetchUserIdsByRoleIds(role.getId());
		   log.info("userIds :"+userIds);
		   if(userIds.size()!=0)
		   {
			   userList=userRepository.fetchByIdsAndTenantId(userIds,currentUser.getTenantId());
		   }
	   }
   }
	   return userList;


   }
    
    /**
     * author :ravali
     * @param roleId
     * @return users List
     */
    /*@GetMapping("/getUsersBasedOnRoleId")
    @Timed
    public List<User> getUsersBasedOnRoleName(@RequestParam Long roleId){
    	log.info("Rest API to get users of a role :"+roleId);
    	Roles role=rolesRepository.findOne(roleId);
    	log.info("role :"+role);
    	List<BigInteger> userIds=userRoleAssignmentRepository.fetchUserIdsByRoleIds(role.getId());
    	log.info("userIds :"+userIds);
    	List<User> userList=userRepository.fetchByIds(userIds);
    	return userList;
    	
    	
    }*/

   /**
    * Author: Shiva
    * Purpose: Check weather Role exist or not
    * **/
   @GetMapping("/checkRoleIsExist")
	@Timed
	public HashMap checkRoleIsExist(@RequestParam String name,@RequestParam(required=false,value="id") Long id, @RequestParam String type)
	{
	   Long tenantId=userService.getCurrentUser().getTenantId();
	   List<Long> tenantIds = new ArrayList<Long>();
	   tenantIds.add(0L);
	   tenantIds.add(tenantId);
	   
		HashMap map=new HashMap();
		map.put("result", "No Duplicates Found");
		if(id != null)
		{
			Roles roleWithId;
			if(type.equalsIgnoreCase("name"))
			 roleWithId = rolesRepository.findByIdAndRoleNameAndTenantIdIn(id, name, tenantIds);
			else 
				roleWithId = rolesRepository.findByIdAndRoleCodeAndTenantIdIn(id, name, tenantIds);
			if(roleWithId != null)
			{
				map.put("result", "No Duplicates Found");
			}
			else
			{
				List<Roles> roles;
				//findByTenantIdInAndRoleCode
				if(type.equalsIgnoreCase("name"))
				 roles = rolesRepository.findByTenantIdInAndRoleName(tenantIds, name);
				else
					roles = rolesRepository.findByTenantIdInAndRoleCode(tenantIds, name);
				if(roles.size()>0)
				{
					map.put("result", "'"+name+"' role already exists");
				}
			}
		}
		else 
		{
			List<Roles> roles ;
			if(type.equalsIgnoreCase("name"))
			 roles = rolesRepository.findByTenantIdInAndRoleName(tenantIds, name);
			else roles = rolesRepository.findByTenantIdInAndRoleCode(tenantIds, name);
			if(roles.size()>0)
			{
				map.put("result", "'"+name+"' role already exists");
			}
		}
		return map;
	}
   
}
