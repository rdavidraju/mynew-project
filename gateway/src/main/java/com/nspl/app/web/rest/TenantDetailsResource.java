package com.nspl.app.web.rest;

import io.github.jhipster.web.util.ResponseUtil;

import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import com.netflix.zuul.context.RequestContext;
import com.nspl.app.domain.TenantDetails;
import com.nspl.app.domain.User;
import com.nspl.app.domain.UserRoleAssignment;
import com.nspl.app.repository.RolesRepository;
import com.nspl.app.repository.TenantDetailsRepository;
import com.nspl.app.repository.UserRepository;
import com.nspl.app.repository.UserRoleAssignmentRepository;
import com.nspl.app.security.IDORUtils;
import com.nspl.app.security.TenantContext;
import com.nspl.app.service.MailService;
import com.nspl.app.service.UserService;
import com.nspl.app.service.util.RandomUtil;
import com.nspl.app.web.rest.dto.TenantNDefaultUser;
import com.nspl.app.web.rest.util.HeaderUtil;
import com.nspl.app.web.rest.util.PaginationUtil;

/**
 * REST controller for managing TenantDetails.
 */
@RestController
@RequestMapping("/api")
public class TenantDetailsResource {

	private final Logger log = LoggerFactory.getLogger(TenantDetailsResource.class);

	private static final String ENTITY_NAME = "tenantDetails";

	private final TenantDetailsRepository tenantDetailsRepository;

	private final PasswordEncoder passwordEncoder;

	private final Environment env;

	private UserRepository userRepository;

	@Inject
	UserService userService;

	@Inject
	MailService mailService;

	@Inject
	RolesRepository rolesRepository;

	@Inject
	UserRoleAssignmentRepository userRoleAssignmentRepository;


	@Inject
	UserResource userResource;

	@PersistenceContext(unitName="default")
	private EntityManager em;

	public TenantDetailsResource(TenantDetailsRepository tenantDetailsRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, Environment env) {
		this.tenantDetailsRepository = tenantDetailsRepository;
		this.userRepository = userRepository;
		this.passwordEncoder=passwordEncoder;
		this.env=env;
	}

	/**
	 * POST  /tenant-details : Create a new tenantDetails.
	 *
	 * @param tenantDetails the tenantDetails to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new tenantDetails, or with status 400 (Bad Request) if the tenantDetails has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/tenant-details")
	@Timed
	public ResponseEntity<TenantDetails> createTenantDetails(@RequestBody TenantDetails tenantDetails) throws URISyntaxException {
		log.debug("REST request to save TenantDetails : {}", tenantDetails);
		if (tenantDetails.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new tenantDetails cannot already have an ID")).body(null);
		}
		TenantDetails result = tenantDetailsRepository.save(tenantDetails);
		return ResponseEntity.created(new URI("/api/tenant-details/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /tenant-details : Updates an existing tenantDetails.
	 *
	 * @param tenantDetails the tenantDetails to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated tenantDetails,
	 * or with status 400 (Bad Request) if the tenantDetails is not valid,
	 * or with status 500 (Internal Server Error) if the tenantDetails couldn't be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/tenant-details")
	@Timed
	public ResponseEntity<HashMap> updateTenantDetails(@RequestBody TenantNDefaultUser tentDet) throws URISyntaxException {
		log.debug("REST request to update TenantDetails : {}", tentDet);
		HashMap map=new HashMap();
		User currentUser = userService.getCurrentUser();
		TenantDetails tent=new TenantDetails();
		TenantDetails tentDetails=tenantDetailsRepository.findByIdForDisplay(tentDet.getId());
		tent.setId(tentDetails.getId());
		if(tentDet.getTenantName()!=null)
			tent.setTenantName(tentDet.getTenantName());
		if(tentDet.getPrimaryContact()!=null)
			tent.setPrimaryContact(tentDet.getPrimaryContact());
		if(tentDet.getPrimaryContactExt()!=null)
			tent.setPrimaryContactExt(tentDet.getPrimaryContactExt());
		if(tentDet.getSecondaryContact()!=null)
			tent.setSecondaryContact(tentDet.getSecondaryContact());
		if(tentDet.getSecondaryContactExt()!=null)
			tent.setSecondaryContactExt(tentDet.getSecondaryContactExt());
		if(tentDet.getWebsite()!=null)
			tent.setWebsite(tentDet.getWebsite());
		if(tentDet.getCorporateAddress()!=null)
			tent.setCorporateAddress(tentDet.getCorporateAddress());
		if(tentDet.getCorporateAddress2()!=null)
			tent.setCorporateAddress2(tentDet.getCorporateAddress2());
		if(tentDet.getCity()!=null)
			tent.setCity(tentDet.getCity());
		if(tentDet.getState()!=null)
			tent.setState(tentDet.getState());
		if(tentDet.getCountry()!=null)
			tent.setCountry(tentDet.getCountry());
		if(tentDet.getPincode()!=null)
			tent.setPincode(tentDet.getPincode());
		if(tentDet.getDomainName()!=null)
			tent.setDomainName(tentDet.getDomainName());
		if(tentDet.getTenantSubLogo()!=null)
			tent.setTenantSubLogo(tentDet.getTenantSubLogo());
		if(tentDet.getTenantLogo()!=null)
			tent.setTenantLogo(tentDet.getTenantLogo());
		if(tentDet.getCreatedBy()!=null)
			tent.setCreatedBy(tentDet.getCreatedBy());
		tent.setLastUpdatedBy(currentUser.getId());
		if(tentDet.getCreationDate()!=null)
			tent.setCreationDate(tentDet.getCreationDate());
		if(tentDet.getId()!=null)
			tent.setIdForDisplay(tentDet.getId());
		tent.setLastUpdatedDate(ZonedDateTime.now());
		tent=tenantDetailsRepository.save(tent);
		map.put("id", tent.getIdForDisplay());
		return ResponseEntity.ok()
				.body(map);
	}

	/**
	 * GET  /tenant-details : get all the tenantDetails.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of tenantDetails in body
	 */
	/*	@GetMapping("/tenant-details")
	@Timed
	public ResponseEntity<List<TenantDetails>> getAllTenantDetails(@ApiParam Pageable pageable) {
		log.debug("REST request to get a page of TenantDetails");
		Page<TenantDetails> page = tenantDetailsRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tenant-details");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}*/


	@GetMapping("/tenant-details")
	@Timed
	public ResponseEntity<List<LinkedHashMap>> getAllTenantDetails(@RequestParam(value = "page" , required = false) Integer offset,
			@RequestParam(value = "per_page", required = false) Integer limit) throws URISyntaxException {

		List<TenantDetails> tenantDetailsList = new ArrayList<TenantDetails>();
		List<LinkedHashMap> finalTenantList=new ArrayList<LinkedHashMap>();
		PaginationUtil paginationUtil=new PaginationUtil();

		int maxlmt=paginationUtil.MAX_LIMIT;
		int minlmt=paginationUtil.MIN_OFFSET;
		log.info("maxlmt: "+maxlmt);
		Page<TenantDetails> page = null;
		HttpHeaders headers = null;

		if(limit==null || limit<minlmt){
			tenantDetailsList = tenantDetailsRepository.findAllByOrderByIdDesc();
			limit = tenantDetailsList.size();
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
			page = tenantDetailsRepository.findAllByOrderByIdDesc(PaginationUtil.generatePageRequest2(offset, limit));
			headers = PaginationUtil.generatePaginationHttpHeaders2(page, "/api/tenant-details",offset, limit);
		}
		else{
			log.info("input limit is within maxlimit");
			page = tenantDetailsRepository.findAllByOrderByIdDesc(PaginationUtil.generatePageRequest(offset, limit));
			headers = PaginationUtil.generatePaginationHttpHeaderss(page, "/api/tenant-details", offset, limit);
		}


		if(page.getContent().size()>0)
		{
			for(int i=0;i<page.getContent().size();i++)
			{


				LinkedHashMap tenantDet=new LinkedHashMap();
				tenantDet.put("id", page.getContent().get(i).getIdForDisplay());
				tenantDet.put("tenantName", page.getContent().get(i).getTenantName());
				tenantDet.put("primaryContact", page.getContent().get(i).getPrimaryContact());
				tenantDet.put("primaryContactExt", page.getContent().get(i).getPrimaryContactExt());
				tenantDet.put("secondaryContact", page.getContent().get(i).getSecondaryContact());
				tenantDet.put("secondaryContactExt", page.getContent().get(i).getSecondaryContactExt());
				tenantDet.put("website", page.getContent().get(i).getWebsite());
				tenantDet.put("corporateAddress", page.getContent().get(i).getCorporateAddress());
				tenantDet.put("corporateAddress2", page.getContent().get(i).getCorporateAddress2());
				tenantDet.put("city", page.getContent().get(i).getCity());
				tenantDet.put("state", page.getContent().get(i).getState());
				tenantDet.put("country", page.getContent().get(i).getCountry());
				tenantDet.put("pincode", page.getContent().get(i).getPincode());
				tenantDet.put("domainName", page.getContent().get(i).getDomainName());
				tenantDet.put("tenantSubLogo", page.getContent().get(i).getTenantSubLogo());
				tenantDet.put("tenantLogo", page.getContent().get(i).getTenantLogo());
				tenantDet.put("createdBy", page.getContent().get(i).getCreatedBy());
				tenantDet.put("lastUpdatedBy", page.getContent().get(i).getLastUpdatedBy());
				tenantDet.put("creationDate", page.getContent().get(i).getCreationDate());
				tenantDet.put("lastUpdatedDate", page.getContent().get(i).getLastUpdatedDate());
				//log.info("page.getContent().get(i).getId() :"+page.getContent().get(i).getId());
				
				
				
				List<BigInteger> userIds = new ArrayList<BigInteger>();
				userIds = userRepository.fetchUserIdsByTenantId(page.getContent().get(i).getId());
				log.info("User Ids: "+ userIds);			
				List<BigInteger> adminRoleIds = new ArrayList<BigInteger>();
				adminRoleIds = rolesRepository.findByRoleCode("SYSTEM_ADMINISTRATOR");
				if(adminRoleIds.size()>0 && userIds.size()>0)
				{
					List<Long> userIdsLong = new ArrayList<Long>();
					for(BigInteger userId : userIds)
					{
						userIdsLong.add(userId.longValue());
					}				
					Long adminRoleId = adminRoleIds.get(0).longValue();	// Admin Role
					List<BigInteger> finalUserIds = new ArrayList<BigInteger>();	
					finalUserIds = userRoleAssignmentRepository.fetchUserIdsByRoleIdAndUserIds(adminRoleId, userIdsLong);	
					if(finalUserIds.size()>0)
					{
						log.info("final user ids: "+finalUserIds);


						List<User> userDetails = userRepository.fetchByIdsAndTenantId(finalUserIds,page.getContent().get(i).getId());
						log.info("final user ids: "+userDetails);
						tenantDet.put("userDetails", userDetails.get(0));
					}
				
				List<User> userDetList=userRepository.findByTenantIdAndIsActive(page.getContent().get(i).getId());
				//log.info("userDetList :"+userDetList.size());
				tenantDet.put("noOfActiveUsers", userDetList.size());
				finalTenantList.add(tenantDet);

			}
			}
		}


		return new ResponseEntity<>(finalTenantList, headers, HttpStatus.OK);
	}



	/**
	 * GET  /tenant-details/:id : get the "id" tenantDetails.
	 *
	 * @param id the id of the tenantDetails to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the tenantDetails, or with status 404 (Not Found)
	 */
	/*	@GetMapping("/tenant-details/{id}")
	@Timed
	public ResponseEntity<TenantDetails> getTenantDetails(@PathVariable Long id) {
		log.debug("REST request to get TenantDetails : {}", id);
	LinkedHashMap tenatConfig=new LinkedHashMap();
		TenantDetails tenantDetails = tenantDetailsRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(tenantDetails));
	}*/


	@GetMapping("/tenant-details/{id}")
	@Timed
	public ResponseEntity<LinkedHashMap> getTenantDetails(@PathVariable String id) {
		log.debug("REST request to get TenantDetails : {}", id);
		TenantDetails tenantDetails = tenantDetailsRepository.findByIdForDisplay(id);
		LinkedHashMap tenantDet=new LinkedHashMap();
		if(tenantDetails != null)
		{
			log.info("Tenant Id: "+ tenantDetails.getId());
			// Tenant Details
			tenantDet.put("id", tenantDetails.getIdForDisplay());
			tenantDet.put("tenantName", tenantDetails.getTenantName());
			tenantDet.put("primaryContact", tenantDetails.getPrimaryContact());
			tenantDet.put("primaryContactExt", tenantDetails.getPrimaryContactExt());
			tenantDet.put("secondaryContact", tenantDetails.getSecondaryContact());
			tenantDet.put("secondaryContactExt", tenantDetails.getSecondaryContactExt());
			tenantDet.put("website", tenantDetails.getWebsite());
			tenantDet.put("corporateAddress", tenantDetails.getCorporateAddress());
			tenantDet.put("corporateAddress2", tenantDetails.getCorporateAddress2());
			tenantDet.put("city", tenantDetails.getCity());
			tenantDet.put("state", tenantDetails.getState());
			tenantDet.put("country", tenantDetails.getCountry());
			tenantDet.put("pincode", tenantDetails.getPincode());
			tenantDet.put("domainName", tenantDetails.getDomainName());
			tenantDet.put("tenantSubLogo", tenantDetails.getTenantSubLogo());
			tenantDet.put("tenantLogo", tenantDetails.getTenantLogo());
			tenantDet.put("createdBy", tenantDetails.getCreatedBy());
			tenantDet.put("lastUpdatedBy", tenantDetails.getLastUpdatedBy());
			tenantDet.put("creationDate", tenantDetails.getCreationDate());
			tenantDet.put("lastUpdatedDate", tenantDetails.getLastUpdatedDate());			
			// User Ids which are tagged to tenant id
			List<BigInteger> userIds = new ArrayList<BigInteger>();
			userIds = userRepository.fetchUserIdsByTenantId(tenantDetails.getId());
			log.info("User Ids: "+ userIds);			
			List<BigInteger> adminRoleIds = new ArrayList<BigInteger>();
			adminRoleIds = rolesRepository.findByRoleCode("SYSTEM_ADMINISTRATOR");
			if(adminRoleIds.size()>0 && userIds.size()>0)
			{
				List<Long> userIdsLong = new ArrayList<Long>();
				for(BigInteger userId : userIds)
				{
					userIdsLong.add(userId.longValue());
				}				
				Long adminRoleId = adminRoleIds.get(0).longValue();	// Admin Role
				List<BigInteger> finalUserIds = new ArrayList<BigInteger>();				
				finalUserIds = userRoleAssignmentRepository.fetchUserIdsByRoleIdAndUserIds(adminRoleId, userIdsLong);	
				List<UserRoleAssignment> userRoleAssignIdList	 = userRoleAssignmentRepository.fetchIdByRoleIdAndUserIds(adminRoleIds.get(0).longValue(), userIdsLong);	
				List<LinkedHashMap> userRoleassMapList=new ArrayList<LinkedHashMap>();
				for(UserRoleAssignment userRoleAssignId:userRoleAssignIdList)
				{

					LinkedHashMap userRoleAssMap = new LinkedHashMap();
					if(userRoleAssignId.getId() != null)
						userRoleAssMap.put("id", userRoleAssignId.getId());
					if(userRoleAssignId.getId() != null)
						userRoleAssMap.put("userId", userRoleAssignId.getUserId());
					if(userRoleAssignId.getRoleId() != null)
						userRoleAssMap.put("roleId", userRoleAssignId.getRoleId());
					if(userRoleAssignId.getAssignedBy() != null)
						userRoleAssMap.put("assignedBy", userRoleAssignId.getAssignedBy());
					if(userRoleAssignId.getCreatedBy() != null)
						userRoleAssMap.put("createdBy", userRoleAssignId.getCreatedBy());
					if(userRoleAssignId.getLastUpdatedBy() != null)
						userRoleAssMap.put("lastUpdatedBy", userRoleAssignId.getLastUpdatedBy());
					if(userRoleAssignId.getLastUpdatedDate() != null)
						userRoleAssMap.put("lastUpdatedDate", userRoleAssignId.getLastUpdatedDate());
					if(userRoleAssignId.isDeleteFlag() != null)
						userRoleAssMap.put("deleteFlag", userRoleAssignId.isDeleteFlag());
					if(userRoleAssignId.isActiveFlag() != null)
						userRoleAssMap.put("activeFlag", userRoleAssignId.isActiveFlag());
					if(userRoleAssignId.getStartDate() != null)
						userRoleAssMap.put("startDate", userRoleAssignId.getStartDate());
					if(userRoleAssignId.getEndDate() != null)
						userRoleAssMap.put("endDate", userRoleAssignId.getEndDate());
					if(userRoleAssignId.getId() != null)
						userRoleAssMap.put("userId", userRoleAssignId.getUserId());
					userRoleassMapList.add(userRoleAssMap);

				}
				tenantDet.put("adminDetails", userRoleassMapList);
				if(finalUserIds.size()>0)
				{
					log.info("final user ids: "+finalUserIds);


					List<User> userDetails = userRepository.fetchByIdsAndTenantId(finalUserIds,tenantDetails.getId());
					log.info("final user ids: "+userDetails);
					tenantDet.put("userDetails", userDetails);
				}
			}
			else
			{
				tenantDet.put("userDetails", "");
			}
			/*		List<Roles> role=rolesRepository.findByTenantIdAndRoleName(tenantDetails.getId(), "SYSTEM_ADMINISTRATOR");
			if(role.size()>0)
			{
				List<UserRoleAssignment> userRoleAssign=userRoleAssignmentRepository.findByRoleId(role.get(0).getId());
				User userDetList=userRepository.findOne(userRoleAssign.get(0).getUserId());
				tenantDet.put("userDetails", userDetList);
			}*/
		}
		else
		{
			log.info("Tenant Id doesn't exist");
		}

		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(tenantDet));
	}

	/**
	 * DELETE  /tenant-details/:id : delete the "id" tenantDetails.
	 *
	 * @param id the id of the tenantDetails to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/tenant-details/{id}")
	@Timed
	public ResponseEntity<Void> deleteTenantDetails(@PathVariable Long id) {
		log.debug("REST request to delete TenantDetails : {}", id);
		tenantDetailsRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}




	/**
	 * author:ravali
	 * @param tenantId
	 * @param name
	 * Desc: checking whether template name already exists
	 * @return hashMap stating the status
	 */
	@GetMapping("/tenantIsExists")
	@Timed
	public HashMap tenantName(@RequestParam String name)
	{
		HashMap map=new HashMap();
		map.put("result", "No Duplicates Found");
		TenantDetails temName=tenantDetailsRepository.findByTenantName(name);
		if(temName != null)
		{
			map.put("result", "'"+name+"' tenantName already exists");
		}
		return map;
	}

	/**
	 * Author: Shiva, Kiran
	 * Description: Creating tenant and default user
	 * @param UserAndUserRoleAssignmentDTO
	 */
	@PostMapping("/createTenantAndDefaultUser")
	@Timed
	//	@Async
	public HashMap userAndUserRoleAssignmentCreation(@RequestBody TenantNDefaultUser tenantNDefaultUser,HttpServletRequest request) throws URISyntaxException 
	{
		log.info("Rest Request to create user and tenant");
		//Creating tenant
		Integer port = TenantContext.getCurrentPort();
		TenantDetails tenant = tenantDetailsRepository.findByTenantName(tenantNDefaultUser.getTenantName());
		HashMap result= new HashMap();
		Long tenantId=0l;
		if(tenant == null)
		{
			Long userId=null;
			TenantDetails newTenant = new TenantDetails();
			newTenant.setTenantName(tenantNDefaultUser.getTenantName());
			newTenant.setPrimaryContact(tenantNDefaultUser.getPrimaryContact());
			newTenant.setPrimaryContactExt(tenantNDefaultUser.getPrimaryContactExt());
			newTenant.setSecondaryContact(tenantNDefaultUser.getSecondaryContact());
			newTenant.setSecondaryContactExt(tenantNDefaultUser.getSecondaryContactExt());
			newTenant.setWebsite(tenantNDefaultUser.getWebsite());
			newTenant.setCorporateAddress(tenantNDefaultUser.getCorporateAddress());
			newTenant.setCorporateAddress2(tenantNDefaultUser.getCorporateAddress2());
			newTenant.setCity(tenantNDefaultUser.getCity());
			newTenant.setState(tenantNDefaultUser.getState());
			newTenant.setCountry(tenantNDefaultUser.getCountry());
			newTenant.setPincode(tenantNDefaultUser.getPincode());
			newTenant.setDomainName(tenantNDefaultUser.getDomainName());
			newTenant.setTenantSubLogo(tenantNDefaultUser.getTenantSubLogo());
			newTenant.setTenantLogo(tenantNDefaultUser.getTenantLogo());
			TenantDetails createTenant = tenantDetailsRepository.save(newTenant);
			String idForDisplay = IDORUtils.computeFrontEndIdentifier(createTenant.getId().toString());
			createTenant.setIdForDisplay(idForDisplay);
			createTenant= tenantDetailsRepository.save(createTenant);
			// Creating default user
			if(userRepository.findOneByEmail(tenantNDefaultUser.getConDetails().getEmail()).isPresent())
			{
				log.info("Email already exist. "+tenantNDefaultUser.getConDetails().getEmail());
			}
			else
			{
				User user = new User();
				user.setLogin(tenantNDefaultUser.getConDetails().getFirstName()); 
				user.setFirstName(tenantNDefaultUser.getConDetails().getFirstName());
				user.setLastName(tenantNDefaultUser.getConDetails().getLastName());
				user.setEmail(tenantNDefaultUser.getConDetails().getEmail());
				user.setImageUrl(tenantNDefaultUser.getConDetails().getImageUrl());
				user.setTenantId(newTenant.getId());
				if (tenantNDefaultUser.getConDetails().getLangKey() == null){
					user.setLangKey("en"); // default language
				} else {
					user.setLangKey(tenantNDefaultUser.getConDetails().getLangKey());
				}
				user.setAuthorities(tenantNDefaultUser.getConDetails().getAuthorities());
				String encryptedPassword = passwordEncoder.encode(tenantNDefaultUser.getConDetails().getPassword());

				user.setPassword(encryptedPassword);
				user.setActivated(false);
				user.setCreatedBy(createTenant.getId().toString());
				user.setLastModifiedDate(Instant.now());
				//   System.out.println("Instant.now(): "+Instant.now());
				//				User userNew =  userRepository.save(user);
				User userNew = userService.createUser(user.getLogin(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getImageUrl(), user.getLangKey());
				userId=userNew.getId();
				tenantId=createTenant.getId();
				userNew.setTenantId(tenantId);
				userNew.setResetKey(RandomUtil.generateResetKey());
				userNew.setStartDate(ZonedDateTime.now());
				userNew.setCreatedDate(Instant.now());
				userRepository.save(userNew);

				mailService.sendActivationValidationEmail(userNew,port);

				//To insert record in hierarchy table



				userResource.ApiExecutionServiceForHierarchy(userNew.getId(), user.getTenantId(), user.getStartDate(),user.getEndDate(), null,false, request);


				/*Roles role=new Roles();
				role.setTenantId(tenantId);
				role.setRoleName("TENANT_ADMIN");
				role.setRoleDesc("TENANT_ADMIN");
				role.setStartDate(LocalDate.now());
				role.setActiveInd(true);
				role.setCreatedBy(createTenant.getId());
				role.setLastUpdatedBy(createTenant.getId());
				role.setCreationDate(ZonedDateTime.now());
				role.setLastUpdatedDate(ZonedDateTime.now());
				role=rolesRepository.save(role);*/

				Long roleSysAdmin=rolesRepository.findByRoleCodeWith("SYSTEM_ADMINISTRATOR");
				Long roleDefUser=rolesRepository.findByRoleCodeWith("DEFAULT_USER");

				UserRoleAssignment userRoleAssignmentSysAdmin=new UserRoleAssignment();
				userRoleAssignmentSysAdmin.setUserId(userId);
				userRoleAssignmentSysAdmin.setRoleId(roleSysAdmin);
				userRoleAssignmentSysAdmin.setAssignedBy(userId);
				userRoleAssignmentSysAdmin.setActiveFlag(true);
				userRoleAssignmentSysAdmin.setStartDate(ZonedDateTime.now());
				userRoleAssignmentSysAdmin.setCreatedBy(createTenant.getId());
				userRoleAssignmentSysAdmin.setLastUpdatedBy(createTenant.getId());
				userRoleAssignmentSysAdmin.setCreationDate(ZonedDateTime.now());
				userRoleAssignmentSysAdmin.setLastUpdatedDate(ZonedDateTime.now());
				userRoleAssignmentSysAdmin=userRoleAssignmentRepository.save(userRoleAssignmentSysAdmin);

				UserRoleAssignment userRoleAssignment=new UserRoleAssignment();
				userRoleAssignment.setUserId(userId);
				userRoleAssignment.setRoleId(roleDefUser);
				userRoleAssignment.setAssignedBy(userId);
				userRoleAssignment.setActiveFlag(true);
				userRoleAssignment.setStartDate(ZonedDateTime.now());
				userRoleAssignment.setCreatedBy(createTenant.getId());
				userRoleAssignment.setLastUpdatedBy(createTenant.getId());
				userRoleAssignment.setCreationDate(ZonedDateTime.now());
				userRoleAssignment.setLastUpdatedDate(ZonedDateTime.now());
				userRoleAssignment=userRoleAssignmentRepository.save(userRoleAssignment);


			}
			//log.info("tenantId: "+tenantId+" userId: "+userId);
			LoadingCsvFiles(userId,tenantId,request);
			log.info("tenantId: "+tenantId+" userId: "+userId);
			result.put("id", createTenant.getId());
			result.put("tenantId", createTenant.getIdForDisplay());
			//	log.info("result-2:" +result);
			return result;
		}
		else
		{
			result.put("tenantId", tenantId);
			return result;
		}
	}


	@GetMapping("/test")
	@Timed
	public void test(){
		String requestUri = RequestContext.getCurrentContext().getRequest().getRequestURI();
		System.out.println("requestUri: "+requestUri);
	}


	//Kiran
	public void LoadingCsvFiles(Long userId,Long tenantId, HttpServletRequest request)
	{
		log.info("Service Call to Load the Csv files when new tenant is created: "+tenantId);
		String authorization=request.getHeader("Authorization");
		try
		{
			String domainName=TenantContext.getCurrentTenant();
			//log.info("Domain name: "+domainName);
			//			String port=env.getProperty("server.port");
			/*String port1=TenantContext.getCurrentPort().toString();
			log.info("Port1: "+port1);*/

			String port=env.getProperty("server.port");
			//log.info("port: "+port);

			String sslValue=env.getProperty("server.ssl.enabled");
			//log.info("sslValue: "+sslValue);
			String url =null;
			if(sslValue!=null && sslValue.equalsIgnoreCase("true"))
			{
				url ="https://"+domainName+":"+port+"/agreeapplication";
			}
			else{
				url ="http://"+domainName+":"+port+"/agreeapplication";
			}
			final String gateWayUrl=url;
			log.info("GateWay Url: "+gateWayUrl);

			List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			urlParameters.add(new BasicNameValuePair("tenantId", tenantId.toString()));
			urlParameters.add(new BasicNameValuePair("userId", userId.toString()));

			// Below Url to post details in Lookup codes and types
			url=url+"/api/uploadLookUpTypesAndCodes";
			log.info("Loading LookUp Codes...LoopUp codes Api Execution Url: "+url);
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);
			post.setHeader("Authorization", authorization);
			post.setEntity(new UrlEncodedFormEntity(urlParameters));
			HttpResponse response = client.execute(post);
			log.info("Executed lookupcodes posting api");
			client.getConnectionManager().shutdown();
			log.info("connection shutdown");

			// Below Url to post details in Application programs and Program Parameters sets
			url="";
			url=gateWayUrl+"/api/uploadingApplicationPrograms";
			log.info("Loading Application Programs ...Application Programs Api Execution Url: "+url);
			HttpClient client1 = new DefaultHttpClient();
			HttpPost post1 = new HttpPost(url);
			post1.setHeader("Authorization", authorization);
			post1.setEntity(new UrlEncodedFormEntity(urlParameters));
			HttpResponse response1 = client1.execute(post1);
			client1.getConnectionManager().shutdown();

			// Below Url to post details in Tenant Config table
			url="";
			url=gateWayUrl+"/api/postConfigsBasedOnZeroTenant";
			log.info("Loading Tenant config ...Tenant config Api Execution Url: "+url);
			HttpClient client2 = new DefaultHttpClient();
			HttpPost post2 = new HttpPost(url);
			post2.setHeader("Authorization", authorization);
			post2.setEntity(new UrlEncodedFormEntity(urlParameters));
			HttpResponse response2 = client2.execute(post2);
			client2.getConnectionManager().shutdown();

			// Below Url to post details in Report Types table
			url="";
			url=gateWayUrl+"/api/saveReportTypes";
			log.info("Loading Report Types ...Report Types Api Execution Url: "+url);
			HttpClient client3 = new DefaultHttpClient();
			HttpPost post3 = new HttpPost(url);
			post3.setHeader("Authorization", authorization);
			post3.setEntity(new UrlEncodedFormEntity(urlParameters));
			HttpResponse response3 = client3.execute(post3);
			client3.getConnectionManager().shutdown();


			
			// Below Url to post details in Default Templates
			url="";
			url=gateWayUrl+"/api/DefaultFileTemplatesPostingDTO";
			log.info("Loading Default Templates ... Url: "+url);
			HttpClient client4 = new DefaultHttpClient();
			HttpPost post4 = new HttpPost(url);
			post4.setHeader("Authorization", authorization);

			post4.setEntity(new UrlEncodedFormEntity(urlParameters));
			HttpResponse response4 = client4.execute(post4);
			client4.getConnectionManager().shutdown();
			







		} 
		catch (MalformedURLException e) 
		{
			log.info("Exception while calling Application Api");
			e.printStackTrace();
			log.error("Printing exception MalformedURLException: ",e);

		}
		catch (IOException e) 
		{
			log.info("Exception while loading csv files");
			e.printStackTrace();
			log.error("Printing exception IOException: ",e);

		}

	}


	@Transactional
	@GetMapping("/UpdateEncryptedId")
	@Timed
	public List<HashMap> UpdateEncryptedId(@RequestParam (required=false) List<Long> idList, @RequestParam String tableName)  {
		log.debug("REST request to get all TenantConfigs");
		List<HashMap> finalUpdatedMapList=new ArrayList<HashMap>();
		log.info("idList :"+idList);
		List<String> booleanList=new ArrayList<String>();
		List<Long> idList1 = new ArrayList<Long>();
		if(idList == null || idList.size() == 0)
		{
			Query distinctList=em.createQuery("select id FROM "+tableName+" where idForDisplay is null");

			log.info("distinctList : "+distinctList);
			List distinct = new ArrayList<String>();
			distinct =distinctList.getResultList();

			for(int i=0;i<distinct.size();i++)
			{
				String id = distinct.get(i).toString();
				idList1.add(Long.valueOf(id));
			}
			idList =idList1;
		}
		for(Long id:idList)
		{
			String idForDisplay = IDORUtils.computeFrontEndIdentifier(id.toString());
			//	log.info("idForDisplay :"+idForDisplay);
			/*String updateQuery="UPDATE "+tableName+" t set t.idForDisplay='"+idForDisplay+"' where t.id="+id;
			log.info("updateQuery :"+updateQuery);
			Query exe=em.createQuery(updateQuery);*/
			HashMap updateMap=new HashMap();

			String updateQuery="UPDATE "+tableName+" t set t.idForDisplay=:idForDisplay where t.id=:id";
			//	log.info("updateQuery :"+updateQuery);
			Query exe=em.createQuery(updateQuery);
			exe.setParameter("idForDisplay", idForDisplay);
			exe.setParameter("id", id);
			em.flush();
			//     log.info("resultSet :"+exe.executeUpdate()); 
			if(exe.executeUpdate()!=0)
				booleanList.add("true");
			else
			{
				updateMap.put("id", id);
				updateMap.put("status","failed to update record");
				finalUpdatedMapList.add(updateMap);

			}
		}
		return finalUpdatedMapList;

	}



	@GetMapping("/DomainIsExists")
	@Timed
	public HashMap DomainIsExists(HttpServletRequest request,@RequestParam String name,@RequestParam(required=false,value="id") String id)
	{

		HashMap map=new HashMap();
		map.put("result", "No Duplicates Found");

		if(id!=null)
		{
			List<TenantDetails> domainName=tenantDetailsRepository.findByIdForDisplayAndDomainName(id,name);
			List<TenantDetails> domainDupName=tenantDetailsRepository.findByDomainName(name);
			if(domainName.size()==1)
				map.put("result", "No Duplicates Found");
			if(domainDupName.size()!=0 && (!id.equalsIgnoreCase(domainDupName.get(0).getIdForDisplay())))
			{
				if(domainDupName.size()>0)
					map.put("result", "'"+name+"' domainName already exists");
			}
		}
		else
		{
			List<TenantDetails> domainName=tenantDetailsRepository.findByDomainName(name);
			//List<Processes> processName=processesRepository.findByProcessNameAndTenantId(name,tenantId);
			if(domainName.size()>0)
			{
				map.put("result", "'"+name+"' domainName already exists");
			}
		}
		return map;
	}


}
