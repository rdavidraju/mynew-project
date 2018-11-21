package com.nspl.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.domain.TenantDetails;
import com.nspl.app.domain.User;
import com.nspl.app.repository.TenantDetailsRepository;
import com.nspl.app.repository.UserRepository;
import com.nspl.app.web.rest.dto.TenantNDefaultUser;
import com.nspl.app.web.rest.util.HeaderUtil;
import com.nspl.app.web.rest.util.PaginationUtil;

import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.nspl.app.service.util.RandomUtil;

import java.time.Instant;

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
	public ResponseEntity<TenantDetails> updateTenantDetails(@RequestBody TenantDetails tenantDetails) throws URISyntaxException {
		log.debug("REST request to update TenantDetails : {}", tenantDetails);
		if (tenantDetails.getId() == null) {
			return createTenantDetails(tenantDetails);
		}
		TenantDetails result = tenantDetailsRepository.save(tenantDetails);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tenantDetails.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /tenant-details : get all the tenantDetails.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of tenantDetails in body
	 */
	@GetMapping("/tenant-details")
	@Timed
	public ResponseEntity<List<TenantDetails>> getAllTenantDetails(@ApiParam Pageable pageable) {
		log.debug("REST request to get a page of TenantDetails");
		Page<TenantDetails> page = tenantDetailsRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tenant-details");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET  /tenant-details/:id : get the "id" tenantDetails.
	 *
	 * @param id the id of the tenantDetails to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the tenantDetails, or with status 404 (Not Found)
	 */
	@GetMapping("/tenant-details/{id}")
	@Timed
	public ResponseEntity<TenantDetails> getTenantDetails(@PathVariable Long id) {
		log.debug("REST request to get TenantDetails : {}", id);
		TenantDetails tenantDetails = tenantDetailsRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(tenantDetails));
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
	 * Author: Shiva
	 * Description: Creating tenant and default user
	 * @param UserAndUserRoleAssignmentDTO
	 */
	@PostMapping("/createTenantAndDefaultUser")
	@Timed
//	@Async
	public HashMap<String, Long>  userAndUserRoleAssignmentCreation(@RequestBody TenantNDefaultUser tenantNDefaultUser) throws URISyntaxException 
	{
		log.info("Rest Request to create user and tenant");
		//Creating tenant
		TenantDetails tenant = tenantDetailsRepository.findByTenantName(tenantNDefaultUser.getTenantName());
		HashMap<String, Long> result= new HashMap<String, Long>();
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
			TenantDetails createTenant = tenantDetailsRepository.save(newTenant);
			// Creating default user
			if(userRepository.findOneByEmail(tenantNDefaultUser.getConDetails().getEmail()).isPresent())
			{
				log.info("Email already exist. "+tenantNDefaultUser.getConDetails().getEmail());
			}
			else
			{
				User user = new User();
				user.setLogin(tenantNDefaultUser.getConDetails().getLogin());
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
				user.setResetKey(RandomUtil.generateResetKey());
				user.setCreatedDate(Instant.now());
				String encryptedPassword = passwordEncoder.encode(tenantNDefaultUser.getConDetails().getPassword());

				user.setPassword(encryptedPassword);
				user.setActivated(true);
				user.setCreatedBy(createTenant.getId().toString());
				user.setLastModifiedDate(Instant.now());
				//   System.out.println("Instant.now(): "+Instant.now());
				User userNew =  userRepository.save(user);
				userId=userNew.getId();
				tenantId=createTenant.getId();

			}
			//log.info("tenantId: "+tenantId+" userId: "+userId);
			ApiExecutionServiceForSummary(userId,tenantId);
			log.info("tenantId: "+tenantId+" userId: "+userId);
			result.put("tenantId", tenantId);
		//	log.info("result-2:" +result);
			return result;
		}
		else
		{
			result.put("tenantId", tenantId);
			return result;
		}
	}


	public void ApiExecutionServiceForSummary(Long userId,Long tenantId)
	{
		log.info("ApiExecutionService For adding Application Program and Parameters for tenantId :"+tenantId);

		try
		{
			String url=env.getProperty("spring.datasource.applicationUrl");

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			if(tenantId!=null)
				nameValuePairs.add(new BasicNameValuePair("tenantId",
						tenantId.toString()));
			if(userId!=null)
				nameValuePairs.add(new BasicNameValuePair("userId",
						userId.toString()));

			// Below Url to post details in Lookup codes and types
			DefaultHttpClient httpClient = new DefaultHttpClient();
			url=url+"/api/uploadLookUpTypesAndCodes";
			HttpPost postRequest = new HttpPost(url);
			postRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpClient.execute(postRequest);
			httpClient.getConnectionManager().shutdown();

			
			// Below Url to post details in Application programs and Program Parameters sets
			url="";
			url=env.getProperty("spring.datasource.applicationUrl");

			log.info("url after adding LookUps:- "+url);

			DefaultHttpClient httpClient1= new DefaultHttpClient();
			url=url+"/api/uploadingApplicationPrograms";
			HttpPost postRequest1 = new HttpPost(url);
			postRequest1.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response1 = httpClient1.execute(postRequest1);
			httpClient1.getConnectionManager().shutdown();

			// Below Url to post details in Tenant Config table
			url="";
			url=env.getProperty("spring.datasource.applicationUrl");

			log.info("url after adding Application Programs:- "+url);

			DefaultHttpClient httpClient2= new DefaultHttpClient();
			url=url+"/api/postConfigsBasedOnZeroTenant";
			HttpPost postRequest2 = new HttpPost(url);
			postRequest2.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response2 = httpClient2.execute(postRequest2);
			httpClient2.getConnectionManager().shutdown();


			// Below Url to post details in Report Types table
			url="";
			url=env.getProperty("spring.datasource.applicationUrl");

			log.info("url after adding Application Programs:- "+url);

			DefaultHttpClient httpClient3= new DefaultHttpClient();
			url=url+"/api/saveReportTypes";
			HttpPost postRequest3 = new HttpPost(url);
			postRequest3.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response3 = httpClient3.execute(postRequest3);
			httpClient3.getConnectionManager().shutdown();
			
		} catch (MalformedURLException e) {

			log.info("Exception in framing api call for Tenant Details");
			e.printStackTrace();

		} catch (IOException e) {

			//DbService.addJobStatus(spark,schedulerId,oozieJobId,userId,tenantId,1L,"Approvals Initiation Failed for batchId: "+batchId,"Failure");
			e.printStackTrace();

		}

	}

}
