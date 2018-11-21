package com.nspl.app.web.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.domain.Authority;
import com.nspl.app.domain.Functionality;
import com.nspl.app.domain.RoleFunctionAssignment;
import com.nspl.app.domain.TenantDetails;
import com.nspl.app.domain.User;
import com.nspl.app.domain.UserRoleAssignment;
import com.nspl.app.repository.FunctionalityRepository;
import com.nspl.app.repository.RoleFunctionAssignmentRepository;
import com.nspl.app.repository.TenantDetailsRepository;
import com.nspl.app.repository.UserRepository;
import com.nspl.app.repository.UserRoleAssignmentRepository;
import com.nspl.app.security.SecurityUtils;
import com.nspl.app.service.MailService;
import com.nspl.app.service.UserService;
import com.nspl.app.service.dto.UserDTO;
import com.nspl.app.service.dto.UserFunctionalitiesDTO;
import com.nspl.app.web.rest.util.HeaderUtil;
import com.nspl.app.web.rest.vm.KeyAndPasswordVM;
import com.nspl.app.web.rest.vm.ManagedUserVM;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AccountResource {

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    private final UserRepository userRepository;

    private final UserService userService;

    private final MailService mailService;

    private static final String CHECK_ERROR_MESSAGE = "Incorrect password";
    
    private final FunctionalityRepository functionalityRepository;
    
    private final RoleFunctionAssignmentRepository roleFunctionAssignmentRepository;
    
    private final UserRoleAssignmentRepository userRoleAssignmentRepository;
    
    
    private final TenantDetailsRepository tenantDetailsRepository;

    public AccountResource(UserRepository userRepository, UserService userService,
            MailService mailService,FunctionalityRepository functionalityRepository,
            RoleFunctionAssignmentRepository roleFunctionAssignmentRepository,UserRoleAssignmentRepository userRoleAssignmentRepository,TenantDetailsRepository tenantDetailsRepository) {

        this.userRepository = userRepository;
        this.userService = userService;
        this.mailService = mailService;
        this.functionalityRepository = functionalityRepository;
        this.roleFunctionAssignmentRepository = roleFunctionAssignmentRepository;
        this.userRoleAssignmentRepository = userRoleAssignmentRepository;
        this.tenantDetailsRepository = tenantDetailsRepository;
        
    }

    /**
     * POST  /register : register the user.
     *
     * @param managedUserVM the managed user View Model
     * @return the ResponseEntity with status 201 (Created) if the user is registered or 400 (Bad Request) if the login or email is already in use
     */
    @PostMapping(path = "/register",
        produces={MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    @Timed
    public ResponseEntity registerAccount(@Valid @RequestBody ManagedUserVM managedUserVM) {

    	log.info("rest request to register the user");
        HttpHeaders textPlainHeaders = new HttpHeaders();
        textPlainHeaders.setContentType(MediaType.TEXT_PLAIN);
        if (!checkPasswordLength(managedUserVM.getPassword())) {
            return new ResponseEntity<>(CHECK_ERROR_MESSAGE, HttpStatus.BAD_REQUEST);
        }
        return userRepository.findOneByLogin(managedUserVM.getLogin().toLowerCase())
            .map(user -> new ResponseEntity<>("login already in use", textPlainHeaders, HttpStatus.BAD_REQUEST))
            .orElseGet(() -> userRepository.findOneByEmail(managedUserVM.getEmail())
                .map(user -> new ResponseEntity<>("email address already in use", textPlainHeaders, HttpStatus.BAD_REQUEST))
                .orElseGet(() -> {
                    User user = userService
                        .createUser(managedUserVM.getLogin(), managedUserVM.getPassword(),
                            managedUserVM.getFirstName(), managedUserVM.getLastName(),
                            managedUserVM.getEmail().toLowerCase(), managedUserVM.getImageUrl(),
                            managedUserVM.getLangKey());

                    mailService.sendActivationEmail(user);
                    return new ResponseEntity<>(HttpStatus.CREATED);
                })
        );
    }

    /**
     * GET  /activate : activate the registered user.
     *
     * @param key the activation key
     * @return the ResponseEntity with status 200 (OK) and the activated user in body, or status 500 (Internal Server Error) if the user couldn't be activated
     */
  /*  @GetMapping("/activate")
    @Timed
    public ResponseEntity<String> activateAccount(@RequestParam(value = "key") String key) {
    	log.info("key :"+key);
        return userService.activateRegistration(key)
            .map(user -> new ResponseEntity<String>(HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }*/
    
    @GetMapping("/activate")
	@Timed
	public ModelAndView activateAccount(@RequestParam(value = "key") String key) {
    	 Optional<User> user= userService.activateRegistration(key);
    	 if(user.get().getActivated())
    	 {
    		 return new ModelAndView("redirect:/#/activate");
    	 }
    	 else
    	 {
		return new ModelAndView("redirect:/#/activate-error");
    	 }
		  
	}

    /**
     * GET  /authenticate : check if the user is authenticated, and return its login.
     *
     * @param request the HTTP request
     * @return the login if the user is authenticated
     */
    @GetMapping("/authenticate")
    @Timed
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    /**
     * GET  /account : get the current user.
     *
     * @return the ResponseEntity with status 200 (OK) and the current user in body, or status 500 (Internal Server Error) if the user couldn't be returned
     */
    @GetMapping("/account")
    @Timed
    public ResponseEntity<UserDTO> getAccount() {
    	
    	
    	User user=userService.getUserWithAuthorities();
    	log.info("user :"+user);
    	UserDTO userDTO=new UserDTO();
    	userDTO.setId(user.getId()); 
    	userDTO.setLogin(user.getLogin());
    	userDTO.setFirstName(user.getFirstName());
    	userDTO.setLastName(user.getLastName());
    	userDTO.setEmail(user.getEmail());
    	userDTO.setImageUrl(user.getImageUrl());
    	userDTO.setActivated(user.getActivated());
    	userDTO.setLangKey(user.getLangKey());
    	userDTO.setCreatedBy(user.getCreatedBy());
    	userDTO.setCreatedDate(user.getCreatedDate());
    	userDTO.setLastModifiedBy(user.getLastModifiedBy());
    	userDTO.setLastModifiedDate(user.getLastModifiedDate());
    	if(user.getTenantId()!=null)
    	{
    	userDTO.setTenantId(user.getTenantId());
    	TenantDetails tenant=tenantDetailsRepository.findOne(user.getTenantId());
    	if(tenant!=null && tenant.getTenantName()!=null)
    	userDTO.setTenantName(tenant.getTenantName());    	
    	}
    	userDTO.setImage(user.getImage());
    	userDTO.setAuthorities(user.getAuthorities().stream().map(Authority::getName)
                .collect(Collectors.toSet()));
    	return Optional.ofNullable(userDTO)
            .map(user1 -> new ResponseEntity<>(userDTO, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * POST  /account : update the current user information.
     *
     * @param userDTO the current user information
     * @return the ResponseEntity with status 200 (OK), or status 400 (Bad Request) or 500 (Internal Server Error) if the user couldn't be updated
     */
    @PostMapping("/account")
    @Timed
    public ResponseEntity saveAccount(@Valid @RequestBody UserDTO userDTO) {
        final String userLogin = SecurityUtils.getCurrentUserLogin();
        Optional<User> existingUser = userRepository.findOneByEmail(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getLogin().equalsIgnoreCase(userLogin))) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("user-management", "emailexists", "Email already in use")).body(null);
        }
        return userRepository
            .findOneByLogin(userLogin)
            .map(u -> {
                userService.updateUser(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail(),
                    userDTO.getLangKey(), userDTO.getImageUrl());
                return new ResponseEntity(HttpStatus.OK);
            })
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * POST  /account/change_password : changes the current user's password
     *
     * @param password the new password
     * @return the ResponseEntity with status 200 (OK), or status 400 (Bad Request) if the new password is not strong enough
     */
    @PostMapping(path = "/account/change_password",
        produces = MediaType.TEXT_PLAIN_VALUE)
    @Timed
    public ResponseEntity changePassword(@RequestBody String password) {
        if (!checkPasswordLength(password)) {
            return new ResponseEntity<>(CHECK_ERROR_MESSAGE, HttpStatus.BAD_REQUEST);
        }
        userService.changePassword(password);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST   /account/reset_password/init : Send an email to reset the password of the user
     *
     * @param mail the mail of the user
     * @return the ResponseEntity with status 200 (OK) if the email was sent, or status 400 (Bad Request) if the email address is not registered
     */
    @PostMapping(path = "/account/reset_password/init",
        produces = MediaType.TEXT_PLAIN_VALUE)
    @Timed
    public ResponseEntity requestPasswordReset(@RequestBody String mail) {
    	log.info("reset password :"+mail);
        return userService.requestPasswordReset(mail)
            .map(user -> {
            	log.info("user after request password reset");
                mailService.sendPasswordResetMail(user);
                return new ResponseEntity<>("email was sent", HttpStatus.OK);
            }).orElse(new ResponseEntity<>("email address not registered", HttpStatus.BAD_REQUEST));
    }

    /**
     * POST   /account/reset_password/finish : Finish to reset the password of the user
     *
     * @param keyAndPassword the generated key and the new password
     * @return the ResponseEntity with status 200 (OK) if the password has been reset,
     * or status 400 (Bad Request) or 500 (Internal Server Error) if the password could not be reset
     */
    @PostMapping(path = "/account/reset_password/finish",
        produces = MediaType.TEXT_PLAIN_VALUE)
    @Timed
    public ResponseEntity<String> finishPasswordReset(@RequestBody KeyAndPasswordVM keyAndPassword) {
        if (!checkPasswordLength(keyAndPassword.getNewPassword())) {
            return new ResponseEntity<>(CHECK_ERROR_MESSAGE, HttpStatus.BAD_REQUEST);
        }
        return userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey())
              .map(user -> new ResponseEntity<String>(HttpStatus.OK))
              .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    private boolean checkPasswordLength(String password) {
        return !StringUtils.isEmpty(password) &&
            password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH &&
            password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH;
    }
    
    
    
	@RequestMapping(value = "/getLoggedInUserFunctionalities",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public  UserFunctionalitiesDTO getLoggedInUserFunctionalities() {
		List<String> funs=new ArrayList<String>();
		List<String> finalFuns=new ArrayList<String>();
		UserFunctionalitiesDTO userFunctionalityDTO=new UserFunctionalitiesDTO();
		User user=userService.getCurrentUser();
		log.info("user: "+user);
		Long tenantId=user.getTenantId();
		userFunctionalityDTO.setId(user.getId());
		userFunctionalityDTO.setLogin(user.getLogin());
		userFunctionalityDTO.setPassword(user.getPassword());
		userFunctionalityDTO.setFirstName(user.getFirstName());
		userFunctionalityDTO.setLastName(user.getLastName());
		userFunctionalityDTO.setEmail(user.getEmail());
		userFunctionalityDTO.setLangKey(user.getLangKey());
		userFunctionalityDTO.setTenantId(tenantId);
		userFunctionalityDTO.setImageUrl(user.getImageUrl());
		userFunctionalityDTO.setCreatedBy(user.getCreatedBy());
		userFunctionalityDTO.setCreatedDate(user.getCreatedDate());
		userFunctionalityDTO.setLastModifiedBy(user.getLastModifiedBy());
		userFunctionalityDTO.setLastModifiedDate(user.getLastModifiedDate());
		List<UserRoleAssignment> userRoleAssignmentList=userRoleAssignmentRepository.findByuserId(user.getId());
		log.info("userRoleAssignmentList: "+userRoleAssignmentList);
		for(int i=0;i<userRoleAssignmentList.size();i++){
			UserRoleAssignment userRoleAssignment=userRoleAssignmentList.get(i);
			Long rid=userRoleAssignment.getRoleId();
			List<RoleFunctionAssignment> roleFunctionsAssignmentList=roleFunctionAssignmentRepository.findByRoleId(rid);
			log.info("roleFunctionsAssignmentList: "+roleFunctionsAssignmentList);
			for(int j=0;j<roleFunctionsAssignmentList.size();j++){
				RoleFunctionAssignment roleFunctionsAssignment=roleFunctionsAssignmentList.get(j);
				Long funId=roleFunctionsAssignment.getFunctionId();
				Functionality functionality=functionalityRepository.findOne(funId);
				String funName = functionality.getFuncName();
				if(roleFunctionsAssignment.isCanView()!=null && roleFunctionsAssignment.isCanView()==true){
					funName=functionality.getFuncName();
					funName=funName+"_VIEW";
					funs.add(funName);
				}
				if(roleFunctionsAssignment.isCanExecute()!=null && roleFunctionsAssignment.isCanExecute()==true){
					funName=functionality.getFuncName();
					funName=funName+"_EXECUTE";
					funs.add(funName);
				}
				if(roleFunctionsAssignment.isCanInsert()!=null && roleFunctionsAssignment.isCanInsert()==true){
					funName=functionality.getFuncName();
					funName=funName+"_INSERT";
					funs.add(funName);
				}
				if(roleFunctionsAssignment.isCanUpdate()!=null && roleFunctionsAssignment.isCanUpdate()==true){
					funName=functionality.getFuncName();
					funName=funName+"_UPDATE";
					funs.add(funName);
				}
				if(roleFunctionsAssignment.isCanDelete()!=null && roleFunctionsAssignment.isCanDelete()==true){
					funName=functionality.getFuncName();
					funName=funName+"_DELETE";
					funs.add(funName);
					//log.info("funs[d]: "+funs);
				}
				//log.info("final funs: "+funs);
			}
			//log.info("final funs2: "+finalFuns);
		}
		for(int i1=0;i1<funs.size();i1++){
			boolean isDistinct = false;
			for(int j1=0;j1<i1;j1++){
				if(funs.get(i1).equals(funs.get(j1))){
					isDistinct = true;
					break;
				}
			}
			if(!isDistinct){
				//log.info(funs.get(i1)+" ");
				finalFuns.add(funs.get(i1));
			}
		}
		userFunctionalityDTO.setAuthorities(finalFuns);
		return userFunctionalityDTO;
	}
}
