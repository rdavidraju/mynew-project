package com.nspl.app.web.rest;

import io.github.jhipster.web.util.ResponseUtil;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.config.Constants;
import com.nspl.app.domain.Roles;
import com.nspl.app.domain.TenantDetails;
import com.nspl.app.domain.User;
import com.nspl.app.domain.UserRoleAssignment;
import com.nspl.app.repository.AuthorityRepository;
import com.nspl.app.repository.RolesRepository;
import com.nspl.app.repository.TenantDetailsRepository;
import com.nspl.app.repository.UserRepository;
import com.nspl.app.repository.UserRoleAssignmentRepository;
import com.nspl.app.security.AuthoritiesConstants;
import com.nspl.app.security.TenantContext;
import com.nspl.app.service.MailService;
import com.nspl.app.service.UserService;
import com.nspl.app.service.dto.UserAndUserRoleAssignmentDTO;
import com.nspl.app.service.dto.UserDTO;
import com.nspl.app.service.dto.UserRoleAssignmentDTO;
import com.nspl.app.service.dto.UserRolesDTO;
import com.nspl.app.service.util.RandomUtil;
import com.nspl.app.web.rest.dto.ErrorReport;
import com.nspl.app.web.rest.util.HeaderUtil;
import com.nspl.app.web.rest.util.PaginationUtil;
import com.nspl.app.web.rest.vm.ManagedUserVM;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

/**
 * REST controller for managing users.
 * <p>
 * This class accesses the User entity, and needs to fetch its collection of authorities.
 * <p>
 * For a normal use-case, it would be better to have an eager relationship between User and Authority,
 * and send everything to the client side: there would be no View Model and DTO, a lot less code, and an outer-join
 * which would be good for performance.
 * <p>
 * We use a View Model and a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the user and the authorities, because people will
 * quite often do relationships with the user, and we don't want them to get the authorities all
 * the time for nothing (for performance reasons). This is the #1 goal: we should not impact our users'
 * application because of this use-case.</li>
 * <li> Not having an outer join causes n+1 requests to the database. This is not a real issue as
 * we have by default a second-level cache. This means on the first HTTP call we do the n+1 requests,
 * but then all authorities come from the cache, so in fact it's much better than doing an outer join
 * (which will get lots of data from the database, for each HTTP call).</li>
 * <li> As this manages users, for security reasons, we'd rather have a DTO layer.</li>
 * </ul>
 * <p>
 * Another option would be to have a specific JPA entity graph to handle this case.
 */
@RestController
@RequestMapping("/api")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    private static final String ENTITY_NAME = "userManagement";

    private final UserRepository userRepository;

    private final MailService mailService;

    private final UserService userService;
    private final UserRoleAssignmentRepository userRoleAssignmentRepository;

    private final RolesRepository rolesRepository;
    
    private final TenantDetailsRepository tenantDetailsRepository;
    
    @Autowired
	Environment env;
    
    @Autowired
    SessionRegistry sessionRegistry;

    public UserResource(UserRepository userRepository, MailService mailService,
            UserService userService,UserRoleAssignmentRepository userRoleAssignmentRepository,PasswordEncoder passwordEncoder,AuthorityRepository authorityRepository, RolesRepository rolesRepository,TenantDetailsRepository tenantDetailsRepository) {

        this.userRepository = userRepository;
        this.mailService = mailService;
        this.userService = userService;
        this.userRoleAssignmentRepository =userRoleAssignmentRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.rolesRepository = rolesRepository;
        this.tenantDetailsRepository=tenantDetailsRepository;
    }
    
    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    /**
     * POST  /users  : Creates a new user.
     * <p>
     * Creates a new user if the login and email are not already used, and sends an
     * mail with an activation link.
     * The user needs to be activated on creation.
     *
     * @param managedUserVM the user to create
     * @return the ResponseEntity with status 201 (Created) and with body the new user, or with status 400 (Bad Request) if the login or email is already in use
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/users")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity createUser(@Valid @RequestBody ManagedUserVM managedUserVM) throws URISyntaxException {
        log.debug("REST request to save User : {}", managedUserVM);

        if (managedUserVM.getId() != null) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new user cannot already have an ID"))
                .body(null);
        // Lowercase the user login before comparing with database
        } else if (userRepository.findOneByLogin(managedUserVM.getLogin().toLowerCase()).isPresent()) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "userexists", "Login already in use"))
                .body(null);
        } else if (userRepository.findOneByEmail(managedUserVM.getEmail()).isPresent()) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "emailexists", "Email already in use"))
                .body(null);
        } else {
            User newUser = userService.createUser(managedUserVM);
            mailService.sendActivationValidationEmail(newUser,0);
            return ResponseEntity.created(new URI("/api/users/" + newUser.getLogin()))
                .headers(HeaderUtil.createAlert( "userManagement.created", newUser.getLogin()))
                .body(newUser);
        }
    }

    /**
     * PUT  /users : Updates an existing User.
     *
     * @param managedUserVM the user to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated user,
     * or with status 400 (Bad Request) if the login or email is already in use,
     * or with status 500 (Internal Server Error) if the user couldn't be updated
     */
    @PutMapping("/users")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody ManagedUserVM managedUserVM) {
        log.debug("REST request to update User : {}", managedUserVM);
        Optional<User> existingUser = userRepository.findOneByEmail(managedUserVM.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(managedUserVM.getId()))) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "emailexists", "Email already in use")).body(null);
        }
        existingUser = userRepository.findOneByLogin(managedUserVM.getLogin().toLowerCase());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(managedUserVM.getId()))) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "userexists", "Login already in use")).body(null);
        }
        Optional<UserDTO> updatedUser = userService.updateUser(managedUserVM);

        return ResponseUtil.wrapOrNotFound(updatedUser,
            HeaderUtil.createAlert("userManagement.updated", managedUserVM.getLogin()));
    }

    /**
     * GET  /users : get all users.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and with body all users
     */
    /*@GetMapping("/users")
    @Timed
    public ResponseEntity<List<UserDTO>> getAllUsers(@ApiParam Pageable pageable) {
        final Page<UserDTO> page = userService.getAllManagedUsers(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/users");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }*/
  /*  @GetMapping("/users")
    @Timed
    public ResponseEntity<List<UserDTO>> getAllUsers(@ApiParam Pageable pageable) {
       // final Page<UserDTO> page = userService.getAllManagedUsers(pageable);
    	User currentUser = userService.getCurrentUser();
    	Long tenantId = currentUser.getTenantId();
    	log.info("Current User Tenant ID: "+ tenantId);
    	final Page<UserDTO> page = userService.getAllUsersByTenantId(pageable, tenantId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/users");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    */
    
    @GetMapping("/usersListWithRolesAndFunctions")
    @Timed
    public ResponseEntity<List<User>> fetchUsersWithRolesAndFunctions(@RequestParam(value = "page" , required = false) Integer offset,
			@RequestParam(value = "per_page", required = false) Integer limit) {
    	log.info("Rest request to fetch users and their roles");
    	User currentUser = userService.getCurrentUser();
    	List<User> usersList=new ArrayList<User>();
    	Long tenantId = currentUser.getTenantId();
    	log.info("Current User Tenant ID: "+ tenantId);//findAllByTenantIdOrderByCreatedDateDesc
    	PaginationUtil paginationUtil=new PaginationUtil();
		int maxlmt=paginationUtil.MAX_LIMIT;
		int minlmt=paginationUtil.MIN_OFFSET;
		log.info("maxlmt: "+maxlmt);
		Page<User> page = null;
		HttpHeaders headers = null;
		if(limit==null || limit<minlmt){
			usersList=userRepository.findAllByTenantIdOrderByCreatedDateDesc(tenantId);
			log.info("usersList :"+usersList.size());
			log.info("offset :"+offset);
			limit=usersList.size();
		}
		if(limit>maxlmt)
		{
			log.info("input limit exceeds maxlimit");
			page = userRepository.findAllByTenantIdOrderByCreatedDateDesc(tenantId,PaginationUtil.generatePageRequest2(offset, limit));
			try {
				headers = PaginationUtil.generatePaginationHttpHeaders2(page, "/api/fileTemplatesByTenantId",offset, limit);
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			log.info("input limit is within maxlimit");
			page = userRepository.findAllByTenantIdOrderByCreatedDateDesc(tenantId,PaginationUtil.generatePageRequest(offset, limit));
			try {
				headers = PaginationUtil.generatePaginationHttpHeaderss(page, "/api/fileTemplatesByTenantId", offset, limit);
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * @return a string list of the all of the roles
     */
    @GetMapping("/users/authorities")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public List<String> getAuthorities() {
        return userService.getAuthorities();
    }

    /**
     * GET  /users/:login : get the "login" user.
     *
     * @param login the login of the user to find
     * @return the ResponseEntity with status 200 (OK) and with body the "login" user, or with status 404 (Not Found)
     */
    @GetMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
    @Timed
    public ResponseEntity<UserDTO> getUser(@PathVariable String login) {
        log.debug("REST request to get User : {}", login);
        return ResponseUtil.wrapOrNotFound(
            userService.getUserWithAuthoritiesByLogin(login)
                .map(UserDTO::new));
    }

    /**
     * DELETE /users/:login : delete the "login" User.
     *
     * @param login the login of the user to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> deleteUser(@PathVariable String login) {
        log.debug("REST request to delete User: {}", login);
        userService.deleteUser(login);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert( "userManagement.deleted", login)).build();
    }
    
    @PostMapping("/assignRolesToUser")
    @Timed
    public ErrorReport assignRolesToUser(@RequestBody List<UserRoleAssignment> userRoleAssignments ) throws URISyntaxException 
    {
    	ErrorReport errorReport = new ErrorReport();
    	errorReport.setTaskName("Assign Roles");
    	List<UserRoleAssignment> uRAs = userRoleAssignmentRepository.save(userRoleAssignments);
    	errorReport.setTaskStatus("Failure");
    	if(userRoleAssignments.size() == uRAs.size())
    	{
    		errorReport.setTaskStatus("Success");
    	}
    	return errorReport;
    }
    
    @GetMapping("/userById")
    @Timed  
    public User getUserById(@RequestParam Long id)
    {
    	User user=userRepository.findOne(id);
    	return user;
    }
    
    /**
     * Author: Shiva
     * Description: Creating Users and User Role Assignment Creation
     * @param UserAndUserRoleAssignmentDTO
     * @throws ClassNotFoundException 
     */
   /* @PostMapping("/userAndUserRoleAssignmentCreation")
    @Timed*/
    @RequestMapping(value = "/userAndUserRoleAssignmentCreation", method = RequestMethod.POST)
    public ResponseEntity<HashMap> userAndUserRoleAssignmentCreation(@RequestBody UserAndUserRoleAssignmentDTO userNRoleAssignment, @RequestParam Long userId,HttpServletRequest request) throws URISyntaxException, ClassNotFoundException 
    {
    	log.info("Rest API for User and User Role Assignment Creation");
    	User loggedInUser = userRepository.findOne(userId);
    	HashMap finalMap = new HashMap();
    	Integer port = TenantContext.getCurrentPort();
    	// jdbc connection
    	String dbUrl=env.getProperty("spring.datasource.url");
		String schemaName= env.getProperty("spring.datasource.applicationschema");
		String userName = env.getProperty("spring.datasource.username");
		String password = env.getProperty("spring.datasource.password");
		String jdbcDriver = env.getProperty("spring.datasource.jdbcdriver");
	
		Connection conn = null;
		Statement stmt = null;
		Statement stmt1 = null;
		ResultSet result1=null;
		ResultSet result2=null;
    	// Updating User
    	if(userNRoleAssignment.getId() != null)
    	{
    		User user = userRepository.findOne(userNRoleAssignment.getId());
    		if(user != null)
    		{
    			user.setId(user.getId());
    			if(userNRoleAssignment.getLogin() != null)
                user.setLogin(userNRoleAssignment.getLogin());
    			if(userNRoleAssignment.getFirstName() != null)
                user.setFirstName(userNRoleAssignment.getFirstName());
    			if(userNRoleAssignment.getLastName() != null)
                user.setLastName(userNRoleAssignment.getLastName());
    			if(userNRoleAssignment.getEmail() != null)
                user.setEmail(userNRoleAssignment.getEmail());
    			if(userNRoleAssignment.getImageUrl() != null)
                user.setImageUrl(userNRoleAssignment.getImageUrl());
                if (userNRoleAssignment.getLangKey() == null){
                    user.setLangKey("en"); // default language
                } else {
                    user.setLangKey(userNRoleAssignment.getLangKey());
                }
                /*if (userNRoleAssignment.getAuthorities() != null) {
                    user.setAuthorities(userNRoleAssignment.getAuthorities());
                }*/
                if(userNRoleAssignment.getPassword() != null)
                user.setPassword(passwordEncoder.encode(userNRoleAssignment.getPassword()));
                user.setResetKey(RandomUtil.generateResetKey());
                user.setResetDate(Instant.now());
                if(userNRoleAssignment.isActivated() == true || userNRoleAssignment.isActivated() == false)
                user.setActivated(userNRoleAssignment.isActivated());
              //  if(userNRoleAssignment.getTenantId() != null)
                user.setTenantId(loggedInUser.getTenantId());
                user.setLastModifiedDate(Instant.now());
                user.setBusinessTitle(userNRoleAssignment.getBusinessTile());
                if(userNRoleAssignment.getManagerId()!=null)
             	user.setManagerId(userNRoleAssignment.getManagerId());
                user.setWorkLocation(userNRoleAssignment.getWorkLocation());
                if(userNRoleAssignment.getDateOfBirth()!=null)
                user.setDateOfBirth(userNRoleAssignment.getDateOfBirth().plusDays(1));
                user.setAddress(userNRoleAssignment.getAddress());
                user.setContactNum(userNRoleAssignment.getContactNum());
                user.setTimeZone(userNRoleAssignment.getTimeZone());
                if(userNRoleAssignment.getStartDate()!=null)
                user.setStartDate(userNRoleAssignment.getStartDate());
                if(userNRoleAssignment.getEndDate()!=null)
                	user.setEndDate(userNRoleAssignment.getEndDate().plusDays(1));
                if(userNRoleAssignment.getGender()!=null)
                	user.setGender(userNRoleAssignment.getGender());
                if(loggedInUser != null)
                {
                    user.setLastModifiedBy(loggedInUser.getLogin());
                }
                if(userNRoleAssignment.getManagerId()!=null)
                { 


                	if(user.getManagerId()!=null)
                	{
                		if(userNRoleAssignment.getManagerId()!=null)
                			ApiExecutionServiceForHierarchy(user.getId(), user.getTenantId(), user.getStartDate(),user.getEndDate(), user.getManagerId(),true,request);
                		else
                			ApiExecutionServiceForHierarchy(user.getId(), user.getTenantId(), user.getStartDate(),user.getEndDate(), null,true,request);

                	}
                }
                
                User userNew =  userRepository.save(user);
                finalMap.put("id", userNew.getId());
                finalMap.put("firstName", userNew.getFirstName());
                return new ResponseEntity(finalMap, HttpStatus.OK);
    		}
    	}
    	else
    	{
    		//Creating User
            User user = new User();
            if(userNRoleAssignment.getLogin() != null)
            user.setLogin(userNRoleAssignment.getLogin());
            if(userNRoleAssignment.getFirstName() != null)
            user.setFirstName(userNRoleAssignment.getFirstName());
            if(userNRoleAssignment.getLastName() != null)
            user.setLastName(userNRoleAssignment.getLastName());
            if(userNRoleAssignment.getEmail() != null)
            user.setEmail(userNRoleAssignment.getEmail());
            if(userNRoleAssignment.getImageUrl() != null)
            user.setImageUrl(userNRoleAssignment.getImageUrl());
            if (userNRoleAssignment.getLangKey() == null) {
                user.setLangKey("en"); // default language
            } else {
                user.setLangKey(userNRoleAssignment.getLangKey());
            }
            if (userNRoleAssignment.getAuthorities() != null) {
                user.setAuthorities(userNRoleAssignment.getAuthorities());
            }
            if(userNRoleAssignment.getPassword() != null)
            user.setPassword(passwordEncoder.encode(userNRoleAssignment.getPassword()));
            user.setResetKey(RandomUtil.generateResetKey());
            user.setResetDate(Instant.now());
            if(userNRoleAssignment.isActivated() == true || userNRoleAssignment.isActivated() == false)
            user.setActivated(userNRoleAssignment.isActivated());
           // if(userNRoleAssignment.getTenantId() != null)
            user.setTenantId(loggedInUser.getTenantId());
            if(userNRoleAssignment.getBusinessTile()!=null)
            user.setBusinessTitle(userNRoleAssignment.getBusinessTile());
            if(userNRoleAssignment.getManagerId()!=null)
            {
            	user.setManagerId(userNRoleAssignment.getManagerId());
            
            }
           
            if(userNRoleAssignment.getWorkLocation()!=null)
            user.setWorkLocation(userNRoleAssignment.getWorkLocation());
            if(userNRoleAssignment.getDateOfBirth()!=null)
            user.setDateOfBirth(userNRoleAssignment.getDateOfBirth().plusDays(1));
            if(userNRoleAssignment.getAddress()!=null)
            user.setAddress(userNRoleAssignment.getAddress());
            if(userNRoleAssignment.getContactNum()!=null)
            user.setContactNum(userNRoleAssignment.getContactNum());
            if(userNRoleAssignment.getTimeZone()!=null)
            user.setTimeZone(userNRoleAssignment.getTimeZone());
            if(userNRoleAssignment.getStartDate()!=null)
                user.setStartDate(userNRoleAssignment.getStartDate());
            if(userNRoleAssignment.getEndDate()!=null)
            	user.setEndDate(userNRoleAssignment.getEndDate().plusDays(1));
            if(userNRoleAssignment.getGender()!=null)
                	user.setGender(userNRoleAssignment.getGender());
            user.setActivated(false);
            // new user gets registration key
            user.setActivationKey(RandomUtil.generateActivationKey());
            User userNew =  userRepository.save(user); 
     //   User userNew = userService.createUser(user.getLogin(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getImageUrl(), user.getLangKey());
         
			userId=userNew.getId();
			userNew.setTenantId(loggedInUser.getTenantId());
			userNew.setResetKey(RandomUtil.generateResetKey());
			userNew.setStartDate(ZonedDateTime.now());
			userNew.setCreatedDate(Instant.now());
			userNew=userRepository.save(userNew);
		
			
			mailService.sendActivationValidationEmail(userNew,port);
			  
            log.info("userNew.getManagerId() :"+userNew.getManagerId());
            if(userNew.getManagerId()!=null)
            {
            	if(userNRoleAssignment.getManagerId()!=null)
            	ApiExecutionServiceForHierarchy(userNew.getId(), user.getTenantId(), user.getStartDate(),user.getEndDate(), user.getManagerId(),false,request);
            	else
                	ApiExecutionServiceForHierarchy(userNew.getId(), user.getTenantId(), user.getStartDate(),user.getEndDate(), null,false,request);

            	
            	
            }
            
            if(userNew != null)
            {
                List<UserRoleAssignmentDTO> userRoles = userNRoleAssignment.getUserRoleAssignments();
                if(userRoles.size()>0)
                {
                	for(UserRoleAssignmentDTO userRole : userRoles)
                	{
                		UserRoleAssignment usrRlAss = new UserRoleAssignment();
                		usrRlAss.setUserId(userNew.getId());
                		if(userRole.getRoleId() != null)
                		usrRlAss.setRoleId(userRole.getRoleId());
                		if(userRole.getActiveFlag() != null)
                		{
                			usrRlAss.setActiveFlag(userRole.getActiveFlag());
                		}
                		else
                		{
                			usrRlAss.setActiveFlag(null);
                		}
                		if(userRole.getDeleteFlag() != null)
                		{
                			usrRlAss.setDeleteFlag(userRole.getDeleteFlag());
                		}
                		else
                		{
                			usrRlAss.setDeleteFlag(null);
                		}
                		if(userRole.getStartDate() != null)
                		usrRlAss.setStartDate(userRole.getStartDate());
                		if(userRole.getEndDate() != null)
                		usrRlAss.setEndDate(userRole.getEndDate());
                		usrRlAss.setCreationDate(ZonedDateTime.now());
                		usrRlAss.setLastUpdatedDate(ZonedDateTime.now());
                		if(userId != null)
                		{
                			usrRlAss.setAssignedBy(userId);
                			usrRlAss.setCreatedBy(userId);
                			usrRlAss.setLastUpdatedBy(userId);
                		}
                		UserRoleAssignment userRoleAssignment = userRoleAssignmentRepository.save(usrRlAss);
                	}
                }
                finalMap.put("id", userNew.getId());
                finalMap.put("firstName", userNew.getFirstName());
                return new ResponseEntity(finalMap, HttpStatus.OK);
            }
    	}
    	return null;
    }
    
    /**
     * Author: Shiva
     * Description: Fetching User Details, Roles which are assigned to that particular user based on user id.
     * @param userId
     */
    @GetMapping("/getLoggedInUserRolesDetails")
    @Timed
    public UserRolesDTO getReconColumnAlignmentInfo() throws ClassNotFoundException, SQLException {
    	log.info("Rest API for fething currently logged in user roles details");
    	UserRolesDTO result = new UserRolesDTO();
    	User currentUser = userService.getCurrentUser();
    	log.info("Currently logged in user id: "+ currentUser.getId()+", Tenant ID: "+ currentUser.getTenantId());
    	User user = userRepository.findOne(currentUser.getId());
    	List<Roles> roles = new ArrayList<Roles>();
    	if(user != null)
    	{
    		result.setId(user.getId());
    		result.setLogin(user.getLogin());
    		result.setFirstName(user.getFirstName());
    		result.setLastName(user.getLastName());
    		result.setEmail(user.getEmail());
    		result.setImageUrl(user.getImageUrl());
    		result.setActivated(user.getActivated());
    		result.setLangKey(user.getLangKey());
    		result.setActivationKey(user.getActivationKey());
    		result.setResetKey(user.getResetKey());
    		result.setCreatedBy(user.getCreatedBy());
    		result.setCreatedDate(user.getCreatedDate());
    		result.setResetDate(user.getResetDate());
    		result.setLastModifiedBy(user.getLastModifiedBy());
    		result.setLastModifiedDate(user.getLastModifiedDate());
    		result.setTenantId(user.getTenantId());
    		List<BigInteger> roleIds = userRoleAssignmentRepository.fetchRoleIdsByUserId(currentUser.getId());
    		if(roleIds.size()>0)
    		{
    			for(BigInteger roleId : roleIds)
    			{
    				Roles role = rolesRepository.findOne(roleId.longValue());
    				if(role != null)
    				{
    					roles.add(role);
    				}
    			}
    		}
    		result.setUserRoleAssignments(roles);
    	}
    	return result;
    }
    
    
    /**
     * Author: Shiva
     * Description: Fetching User Details, Roles which are assigned to that particular user based on user id.
     * @param userId
     */
    @GetMapping("/getUserRolesDetailsByUserId")
    @Timed
    public UserRolesDTO getReconColumnAlignmentInfo(@RequestParam Long userId) throws ClassNotFoundException, SQLException {
    	log.info("Rest API for fething user roles details for the user id: "+ userId);
    	UserRolesDTO result = new UserRolesDTO();
    	//User currentUser = userService.getCurrentUser();
    	User user = userRepository.findOne(userId);
    	List<Roles> roles = new ArrayList<Roles>();
    	if(user != null)
    	{
    		result.setId(user.getId());
    		if(user.getLogin()!=null && !user.getLogin().isEmpty())
    		result.setLogin(user.getLogin());
    		if(user.getFirstName()!=null && !user.getFirstName().isEmpty())
    		result.setFirstName(user.getFirstName());
    		if(user.getLastName()!=null && !user.getLastName().isEmpty())
    		result.setLastName(user.getLastName());
    		if(user.getEmail()!=null && !user.getEmail().isEmpty())
    		result.setEmail(user.getEmail());
    		if(user.getImageUrl()!=null && !user.getImageUrl().isEmpty())
    		result.setImageUrl(user.getImageUrl());
    		result.setActivated(user.getActivated());
    		if(user.getLangKey()!=null)
    		result.setLangKey(user.getLangKey());
    		if(user.getActivationKey()!=null)
    		result.setActivationKey(user.getActivationKey());
    		if(user.getResetKey()!=null)
    		result.setResetKey(user.getResetKey());
    		if(user.getCreatedBy()!=null)
    		result.setCreatedBy(user.getCreatedBy());
    		if(user.getCreatedDate()!=null)
    		result.setCreatedDate(user.getCreatedDate());
    		if(user.getResetDate()!=null)
    		result.setResetDate(user.getResetDate());
    		if(user.getLastModifiedBy()!=null)
    		result.setLastModifiedBy(user.getLastModifiedBy());
    		if(user.getLastModifiedDate()!=null)
    		result.setLastModifiedDate(user.getLastModifiedDate());
    		result.setTenantId(user.getTenantId());
    		TenantDetails ten=tenantDetailsRepository.findOne(user.getTenantId());
    		if(ten!=null && ten.getTenantName() != null){
    			result.setOrganizationName(ten.getTenantName());
    		}
    		if(user.getImage()!=null)
    		result.setImage(user.getImage());
    		if(user.getBusinessTitle()!=null)
    		result.setBusinessTile(user.getBusinessTitle());
    		if(user.getBusinessTitle()!=null)
    		if(user.getManagerId() != null){
    			result.setManagerId(user.getManagerId());
    			User userMang = userRepository.findOne(user.getManagerId());
        		log.info("userMang :"+userMang);
        		result.setManagerName(userMang.getFirstName() + ' ' + userMang.getLastName());
        		result.setManagerImage(userMang.getImage());
    		}
    		if(user.getWorkLocation()!=null && !user.getWorkLocation().isEmpty())
    		result.setWorkLocation(user.getWorkLocation());
    		if(user.getDateOfBirth()!=null)
    		result.setDateOfBirth(user.getDateOfBirth());
    		if(user.getAddress()!=null)
    		result.setAddress(user.getAddress());
    		if(user.getContactNum()!=null)
    		result.setContactNum(user.getContactNum());
    		if(user.getTimeZone()!=null)
    		result.setTimeZone(user.getTimeZone());
     		if(user.getStartDate()!=null)
        		result.setStartDate(user.getStartDate());
     		if(user.getEndDate()!=null)
        		result.setEndDate(user.getEndDate());
     		if(user.getGender()!=null)
     		result.setGender(user.getGender());
     		
    		List<UserRoleAssignment> rolesAssgmntList = userRoleAssignmentRepository.findByUserId(userId);
    		log.info("rolesAssgmntList :"+rolesAssgmntList.size());
    		List<UserRoleAssignmentDTO> userRoleAssignmentDTOList=new ArrayList<UserRoleAssignmentDTO>();
    		if(rolesAssgmntList.size()>0)
    		{
    			for(UserRoleAssignment rolesAssgmnt : rolesAssgmntList)
    			{
    				if(rolesAssgmnt.getRoleId()!=null)
    				{
    				Roles role = rolesRepository.findOne(rolesAssgmnt.getRoleId());
    				UserRoleAssignmentDTO userRoleAssignment=new UserRoleAssignmentDTO();
    				
    				if(role != null)
    				{
    					if(rolesAssgmnt.getRoleId()!=null)
    					userRoleAssignment.setRoleId(rolesAssgmnt.getRoleId());
    					if(role.getRoleName()!=null && !role.getRoleName().isEmpty())
    					userRoleAssignment.setRoleName(role.getRoleName());
    					userRoleAssignment.setRoleCode(role.getRoleCode());
    					if(rolesAssgmnt.getId()!=null)
    					userRoleAssignment.setId(rolesAssgmnt.getId());
    					if(rolesAssgmnt.getStartDate()!=null)
    					userRoleAssignment.setStartDate(rolesAssgmnt.getStartDate());
    					if(rolesAssgmnt.getEndDate()!=null)
    					userRoleAssignment.setEndDate(rolesAssgmnt.getEndDate());
    					if(rolesAssgmnt.isActiveFlag()!=null)
    					userRoleAssignment.setActiveFlag(rolesAssgmnt.isActiveFlag());
    					if(rolesAssgmnt.isDeleteFlag()!=null)
    					userRoleAssignment.setDeleteFlag(rolesAssgmnt.isDeleteFlag());
    					userRoleAssignmentDTOList.add(userRoleAssignment);
    				}
    				}
    			}
    		}
    		log.info("userRoleAssignmentDTOList :"+userRoleAssignmentDTOList.size());
    		result.setUserRoleAssnmt(userRoleAssignmentDTOList);
    	}
    	return result;
    }

    /**
     * Author: Shiva
     * Description: Creating or Updating User Roles
     * @param List<Roles>
     */
    @PostMapping("/postingUserRoleAssignment")
    @Timed
    public void userAndUserRoleAssignmentCreation(@RequestBody List<Roles> roles) throws URISyntaxException 
    {
    	log.info("Rest API for creating or updating user roles");
    	User currentUser = userService.getCurrentUser();
    	log.info("Currently logged in user id: "+ currentUser.getId()+", tenant id: "+ currentUser.getTenantId());
    	List<BigInteger> roleIds = userRoleAssignmentRepository.fetchRoleIdsByUserId(currentUser.getId());
    	for(Roles role : roles)
    	{
    		if(roleIds.contains(new BigInteger(role.getId().toString())))
   			{ // Updating existing record
    			log.info("Updating record "+role.getId());
   				UserRoleAssignment ura = userRoleAssignmentRepository.findByUserIdAndRoleId(currentUser.getId(), role.getId());
   				if(ura != null)
   				{
    				ura.setId(ura.getId());
    				ura.setAssignedBy(currentUser.getId());
    				ura.setLastUpdatedBy(currentUser.getId());
    				ura.setLastUpdatedDate(ZonedDateTime.now());
    				ura.setActiveFlag(true);	// Need to modify
    				ura.setStartDate(role.getStartDate());
   					ura.setEndDate(role.getEndDate());
   					userRoleAssignmentRepository.save(ura);
   					roleIds.remove(new BigInteger(role.getId().toString()));
   				}
   			}
   			else
   			{	// Creating new record
   				log.info("Creating Record "+role.getId());
   				UserRoleAssignment newURA = new UserRoleAssignment();
   				newURA.setUserId(currentUser.getId());
    			newURA.setRoleId(role.getId());
    			newURA.setAssignedBy(currentUser.getId());
    			newURA.setCreatedBy(currentUser.getId());
    			newURA.setLastUpdatedBy(currentUser.getId());
    			newURA.setCreationDate(ZonedDateTime.now());
   				newURA.setLastUpdatedDate(ZonedDateTime.now());
   				newURA.setActiveFlag(true);	// Need to modify
   				newURA.setStartDate(role.getStartDate());
   				newURA.setEndDate(role.getEndDate());
    			userRoleAssignmentRepository.save(newURA);
    		}
    	}
    	if(roleIds.size()>0)	// Deleting un-assigned records
   		{
   			for(BigInteger roleId : roleIds)
   			{
   				UserRoleAssignment ura = userRoleAssignmentRepository.findByUserIdAndRoleId(currentUser.getId(), roleId.longValue());
    			if(ura != null)
    			{
    				userRoleAssignmentRepository.delete(ura.getId());
    			}
    		}
   		}
   }
    
    
    /**
     * Author: Shiva
     * param : tenantId
     * Description: Fetching list of active users based on tenant id
     * result : List<User>
     */ 
    @GetMapping("/getUsersByTenantId")
    @Timed
    public List<User> getUsersByTenantId() {
    	User currentUser = userService.getCurrentUser();
    	 List<User> users = new ArrayList<User>();
        log.debug("Rest api for fetching active users for the tenant id: ");
        if(currentUser.getTenantId() != null)
        users = userRepository.fetchByTenantId(currentUser.getTenantId());
        log.info("No of records fetched : "+ users.size());
        return users;
    }
    
    /**
     * Author: Shiva
     * param : image file, userId
     * Description: Posting user image as BLOB datatype
     * return: void
     */ 
    @RequestMapping(value = "/postUserImage", method = RequestMethod.POST)
    public HashMap postUserAsBolb(@RequestParam(value="file", required=true) MultipartFile file, @RequestParam(value="userId", required=true) Long userId){
    	log.info("Rest api for posting image for the user id: "+ userId);
    	HashMap finalMap = new HashMap();
    	if(file != null)
    	{
        	try{
        		byte [] byteArr=file.getBytes();
        		Blob image = new javax.sql.rowset.serial.SerialBlob(byteArr);
        		User user = userRepository.findOne(userId);
        		if(user != null)
        		{
        			user.setId(user.getId());
        			user.setImage(byteArr);
        			log.info("Image size: "+ image.length());
            		User updateUser = userRepository.save(user);
            		finalMap.put("id", updateUser.getId());
        		}
        	}
        	catch(IOException | SQLException e)
        	{
        		e.printStackTrace();
        	}
    	}
    	return finalMap;
    }
    
    /**
     * Author: Shiva
     * param : userId
     * Description: Fetching user image
     * return: userImage as encoded string
     */ 
/*    @RequestMapping(value = "/getUserImage",method = RequestMethod.GET) 
    @Produces("image/png")
    public String getImage(@RequestParam(value = "userId") Long userId)
    {
    	log.info("Rest api for getting image for the user id: "+ userId);
    	User user = userRepository.findOne(userId);
    	String encodedImage = "";
    	if(user != null)
    	{
        	byte[] allBytesInBlob = null;
        	byte[] byteImage = null;
        	try{
            	byteImage = user.getImage();
            	Blob image = new javax.sql.rowset.serial.SerialBlob(byteImage);
            	allBytesInBlob = image.getBytes(1, (int)image.length());
            	InputStream in = new ByteArrayInputStream(allBytesInBlob);
            	BufferedImage image2 = ImageIO.read(in);
            	ByteArrayOutputStream baos = new ByteArrayOutputStream(); ImageIO.write(image2, "png", baos); 
            	byte[] res=baos.toByteArray(); 
            	encodedImage = Base64.encode(baos.toByteArray());
        	}
        	catch(Exception e)
        	{
        		log.info("Exception e: "+e);
        	}
    	}
    	return encodedImage;
    }*/
    
    
   
	@GetMapping("/validatingUserEmail")
    @Timed
    public  HashMap validatingUserEmail(@RequestParam String email, @RequestParam(required=false,value="id") Long id){
/*    	Optional<User> user=userRepository.findOneByEmail(email);
    	log.info("user: "+user);
    	HashMap status=new HashMap();
    	if(user!=null && (user.isPresent()==true)){
    		log.info("in if");
    		status.put("result","Email id already in use");
    	}
    	else if(user!=null && !(user.equals("Optional.empty"))){
    		log.info("in else");
    		status.put("result","Success");
    	}
    	
*/		
		HashMap map=new HashMap();
		map.put("result", "No Duplicates Found");
		if(id != null)
		{
			User userWithId = userRepository.findByIdAndEmail(id, email);
			if(userWithId != null)
			{
				map.put("result", "No Duplicates Found");
			}
			else
			{
				List<User> users = userRepository.findByEmail(email);
				if(users.size()>0)
				{
					map.put("result", "Email already in use");
				}
			}
		}
		else 
		{
			List<User> users = userRepository.findByEmail(email);
			if(users.size()>0)
			{
				map.put("result", "Email already in use");
			}
		}
		
		return map;
    	
    }
	 
	 
	 public void ApiExecutionServiceForHierarchy(Long userId,Long tenantId,ZonedDateTime startDate,ZonedDateTime endDate,Long managerId,Boolean update,HttpServletRequest request)
		{
			log.info("ApiExecutionService For adding record in hierarchy resource tenantId :"+tenantId+"managerId :"+managerId+"userId :"+userId+" startDate :"+startDate+"endDate :"+endDate);

			try
			{
//				String url=env.getProperty("spring.datasource.applicationUrl");
//
//				DefaultHttpClient httpClient = new DefaultHttpClient();
//				url=url+"/api/insertRecordInHierarchyTable";
//				HttpPost postRequest = new HttpPost(url);
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

				
				if(managerId!=null)
				{
					log.info("******if managerId is not null*******");
				nameValuePairs.add(new BasicNameValuePair("userId",
						userId.toString()));
				nameValuePairs.add(new BasicNameValuePair("tenantId",
						tenantId.toString()));
				/*nameValuePairs.add(new BasicNameValuePair("startDate",
						startDate.toString()));
				nameValuePairs.add(new BasicNameValuePair("endDate",
						endDate.toString()));*/
				nameValuePairs.add(new BasicNameValuePair("managerId",
						managerId.toString()));
				nameValuePairs.add(new BasicNameValuePair("update",
						update.toString()));
				
				}
				else
				{
					log.info("*********else if managerId is null*********");
					nameValuePairs.add(new BasicNameValuePair("userId",
							userId.toString()));
					nameValuePairs.add(new BasicNameValuePair("tenantId",
							tenantId.toString()));
				/*	nameValuePairs.add(new BasicNameValuePair("startDate",
							startDate.toString()));
					nameValuePairs.add(new BasicNameValuePair("endDate",
							endDate.toString()));*/
					nameValuePairs.add(new BasicNameValuePair("update",
							update.toString()));
				
				}
				
				String authorization=request.getHeader("Authorization");
				String domainName=TenantContext.getCurrentTenant();
				String port=TenantContext.getCurrentPort().toString();
				String sslValue=env.getProperty("server.ssl.enabled");
				String url =null;
				if(sslValue!=null && sslValue.equalsIgnoreCase("true"))
				{
					 url ="https://"+domainName+":"+port+"/agreeapplication";
				}
				else{
					 url ="http://"+domainName+":"+port+"/agreeapplication";
				}
				final String gateWayUrl=url+"/api/insertRecordInHierarchyTable";
				log.info("GateWay Url: "+gateWayUrl);
				
				
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(gateWayUrl);
				post.setHeader("Authorization", authorization);
				post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				 client.execute(post);
				//HttpResponse response =
				client.getConnectionManager().shutdown();
				

			} catch (MalformedURLException e) {

				log.info("Exception in framing api call for hierarchy table");
				e.printStackTrace();

			} catch (IOException e) {

				//DbService.addJobStatus(spark,schedulerId,oozieJobId,userId,tenantId,1L,"Approvals Initiation Failed for batchId: "+batchId,"Failure");
				e.printStackTrace();

			}

		}
	 @RequestMapping(value = "/loggedInUsers", method = RequestMethod.GET)
	 public List<User> getLoggedUsers() {
	 
		 User currentUser = userService.getCurrentUser();
	    List<User> usersList=new ArrayList<User>();
	    Long tenantId = currentUser.getTenantId();
	    List<String> loginList = new ArrayList<String>();
	    String loginNames = "";
		 for(Object principalObj:sessionRegistry.getAllPrincipals())
		 {
			 if(!sessionRegistry.getAllSessions(principalObj, false).isEmpty())
			 {
				 org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) principalObj;
				 loginNames = loginNames+user.getUsername()+",";
			 }
		 }
		 
		 loginNames = loginNames.trim();
		 
		 if(loginNames.endsWith(","))
		 {
			 loginNames = loginNames.substring(0, loginNames.length()-1);
			 for(String loginName:loginNames.split(","))
			 {
				 loginList.add(loginName);
			 }
			 
			 usersList = userRepository.findByTenantIdAndEmailIn(tenantId,loginList);
		 }
		 
		 return usersList;
	 }
	 @RequestMapping(value = "/logout", method = RequestMethod.GET)
	 public void logout() {
	 
		 User currentUser = userService.getCurrentUser();
	    List<User> usersList=new ArrayList<User>();
	    Long tenantId = currentUser.getTenantId();
	    List<String> loginList = new ArrayList<String>();
	    String loginNames = "";
		 for(Object principalObj:sessionRegistry.getAllPrincipals())
		 {
			 if(!sessionRegistry.getAllSessions(principalObj, false).isEmpty())
			 {
				 org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) principalObj;
				 loginNames = loginNames+user.getUsername()+",";
				 if(user.getUsername().equalsIgnoreCase(currentUser.getEmail()))
				 {
					 for(SessionInformation sessInfo:sessionRegistry.getAllSessions(principalObj, true))
					 {
						 sessInfo.expireNow();
					 }
				 }
			 }
		 }
		 
	 }
	 
	 	@GetMapping("/validateUser")
	    @Timed
	    public ResponseEntity<HashMap> validateUser(@RequestParam Long userId){

	 			Integer port = TenantContext.getCurrentPort();
	 			User user = userRepository.findOne(userId);

	 			HashMap finalMap = new HashMap();
	 			
	 			if(user.getActivated())
	 			{
	 			
	 				finalMap.put("status", "User Already activated");
	 				return new ResponseEntity(finalMap, HttpStatus.OK);
	 			}
	 			
	 			user.setActivated(false);
	            user.setActivationKey(RandomUtil.generateActivationKey());
	            User userNew =  userRepository.save(user); 
	         
				userId=userNew.getId();
				userNew.setResetKey(RandomUtil.generateResetKey());
				userNew.setStartDate(ZonedDateTime.now());
				userNew.setCreatedDate(Instant.now());
				userNew=userRepository.save(userNew);
			
				
				mailService.sendActivationValidationEmail(userNew,port);
				  
	        
				finalMap.put("status", "Email Sent");
 				return new ResponseEntity(finalMap, HttpStatus.OK);
	 }
}
