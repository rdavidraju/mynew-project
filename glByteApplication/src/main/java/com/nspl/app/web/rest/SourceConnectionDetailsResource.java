package com.nspl.app.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
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
import com.dropbox.core.DbxException;
import com.jcraft.jsch.SftpException;
import com.nspl.app.domain.LookUpCode;
import com.nspl.app.domain.SourceConnectionDetails;
import com.nspl.app.domain.SourceProfiles;
import com.nspl.app.repository.LookUpCodeRepository;
import com.nspl.app.repository.SourceConnectionDetailsRepository;
import com.nspl.app.repository.SourceProfileFileAssignmentsRepository;
import com.nspl.app.repository.SourceProfilesRepository;
import com.nspl.app.repository.search.SourceConnectionDetailsSearchRepository;
import com.nspl.app.repository.search.SourceProfilesSearchRepository;
import com.nspl.app.security.IDORUtils;
import com.nspl.app.service.ActiveStatusService;
import com.nspl.app.service.DriveService;
import com.nspl.app.service.ElasticSearchColumnNamesService;
import com.nspl.app.service.SFTPUtilService;
import com.nspl.app.service.SourceConnectionDetailsService;
import com.nspl.app.service.SourceConnectionStatusService;
import com.nspl.app.service.UserJdbcService;
import com.nspl.app.web.rest.dto.SourceConcAndSrcProfileDto;
import com.nspl.app.web.rest.dto.SourceConnectionsDTO;
import com.nspl.app.web.rest.dto.SrcConnectionDetailsDTO;
import com.nspl.app.web.rest.util.HeaderUtil;
import com.nspl.app.web.rest.util.PaginationUtil;

/**
 * REST controller for managing SourceConnectionDetails.
 */
@RestController
@RequestMapping("/api")
public class SourceConnectionDetailsResource {

	private final Logger log = LoggerFactory.getLogger(SourceConnectionDetailsResource.class);

	private static final String ENTITY_NAME = "sourceConnectionDetails";

	private final SourceConnectionDetailsRepository sourceConnectionDetailsRepository;

	private final SourceConnectionDetailsSearchRepository sourceConnectionDetailsSearchRepository;

	private final SourceConnectionDetailsService sourceConnectionDetailsService;

	@Inject
	UserJdbcService userJdbcService;

	@Inject
	SourceProfilesRepository sourceProfilesRepository;

	@Inject
	SourceProfileFileAssignmentsRepository sourceProfileFileAssignmentsRepository;

	//	@Inject
	//	private final SourceConnectionDetailsService sourceConnectionDetailsService;

	@Inject
	LookUpCodeRepository lookUpCodeRepository;

	@Inject
	ElasticSearchColumnNamesService elasticSearchColumnNamesService;

	@Inject
	DriveService driveService;

	@Inject
	SFTPUtilService sFTPUtilService;

	@Inject
	SourceProfilesSearchRepository sourceProfilesSearchRepository;
	
	@Inject
	ActiveStatusService activeStatusService;


	public SourceConnectionDetailsResource(SourceConnectionDetailsRepository sourceConnectionDetailsRepository, SourceConnectionDetailsSearchRepository sourceConnectionDetailsSearchRepository, SourceProfilesSearchRepository sourceProfilesSearchRepository, SourceConnectionDetailsService sourceConnectionDetailsService){
		this.sourceConnectionDetailsRepository = sourceConnectionDetailsRepository;
		this.sourceConnectionDetailsSearchRepository = sourceConnectionDetailsSearchRepository;
		this.sourceProfilesSearchRepository = sourceProfilesSearchRepository;
		this.sourceConnectionDetailsService = sourceConnectionDetailsService;
	}

	/**
	 * POST  /source-connection-details : Create a new sourceConnectionDetails.
	 *
	 * @param sourceConnectionDetails the sourceConnectionDetails to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new sourceConnectionDetails, or with status 400 (Bad Request) if the sourceConnectionDetails has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	/*@PostMapping("/source-connection-details/{userId}/{tenantId}")
	@Timed
	public ResponseEntity<SourceConnectionDetails> createSourceConnectionDetails(@RequestBody SourceConnectionDetails sourceConnectionDetails,@RequestParam Long userId,@RequestParam Long tenantId) throws URISyntaxException {
		log.debug("REST request to save SourceConnectionDetails : {}", sourceConnectionDetails);
		if (sourceConnectionDetails.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new sourceConnectionDetails cannot already have an ID")).body(null);
		}
		sourceConnectionDetails.setCreatedDate(ZonedDateTime.now());
		sourceConnectionDetails.setLastUpdatedDate(ZonedDateTime.now());
		sourceConnectionDetails.setCreatedBy(userId);
		sourceConnectionDetails.setLastUpdatedBy(userId);
		sourceConnectionDetails.setTenantId(tenantId);
		sourceConnectionDetails.setStartDate(sourceConnectionDetails.getStartDate().plusDays(1));
		sourceConnectionDetails.setEndDate(sourceConnectionDetails.getEndDate().plusDays(1));
		SourceConnectionDetails result = sourceConnectionDetailsRepository.save(sourceConnectionDetails);
		sourceConnectionDetailsSearchRepository.save(result);
		return ResponseEntity.created(new URI("/api/source-connection-details/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}*/


	@PostMapping("/source-connection-details")
	@Timed
	public ResponseEntity<SourceConnectionDetails> createSourceConnectionDetails(HttpServletRequest request,@RequestBody SourceConcAndSrcProfileDto sourceConcAndSrcProfileDto) throws URISyntaxException {

		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		Long userId=Long.parseLong(map.get("userId").toString());
		log.debug("REST request to save SourceConnectionDetails for tenant: "+tenantId+" user: "+userId+"->"+ sourceConcAndSrcProfileDto);
		if (sourceConcAndSrcProfileDto.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new sourceConnectionDetails cannot already have an ID")).body(null);
		}
		SourceConnectionDetails result =new SourceConnectionDetails();
		/*SourceConnectionDetails sourceConnectionDetails=new SourceConnectionDetails();
		if(sourceConcAndSrcProfileDto.getName()!=null && !sourceConcAndSrcProfileDto.getName().isEmpty())
		sourceConnectionDetails.setName(sourceConcAndSrcProfileDto.getName());
		if(sourceConcAndSrcProfileDto.getDescription()!=null && !sourceConcAndSrcProfileDto.getDescription().isEmpty())
		sourceConnectionDetails.setDescription(sourceConcAndSrcProfileDto.getDescription());
		if(sourceConcAndSrcProfileDto.getProtocol()!=null && !sourceConcAndSrcProfileDto.getProtocol().isEmpty())
		sourceConnectionDetails.setProtocol(sourceConcAndSrcProfileDto.getProtocol());
		if(sourceConcAndSrcProfileDto.getClientKey()!=null && !sourceConcAndSrcProfileDto.getClientKey().isEmpty())
		sourceConnectionDetails.setClientKey(sourceConcAndSrcProfileDto.getClientKey());
		if(sourceConcAndSrcProfileDto.getClientKey()!=null && !sourceConcAndSrcProfileDto.getClientKey().isEmpty())
		sourceConnectionDetails.setClientSecret(sourceConcAndSrcProfileDto.getClientKey());
		if(sourceConcAndSrcProfileDto.getAuthEndpointUrl()!=null && !sourceConcAndSrcProfileDto.getAuthEndpointUrl().isEmpty())
		sourceConnectionDetails.setAuthEndpointUrl(sourceConcAndSrcProfileDto.getAuthEndpointUrl());
		if(sourceConcAndSrcProfileDto.getTokenEndpointUrl()!=null && !sourceConcAndSrcProfileDto.getTokenEndpointUrl().isEmpty())
		sourceConnectionDetails.setTokenEndpointUrl(sourceConcAndSrcProfileDto.getTokenEndpointUrl());
		if(sourceConcAndSrcProfileDto.getCallBackUrl()!=null && !sourceConcAndSrcProfileDto.getCallBackUrl().isEmpty())
		sourceConnectionDetails.setCallBackUrl(sourceConcAndSrcProfileDto.getCallBackUrl());
		if(sourceConcAndSrcProfileDto.getHost()!=null && !sourceConcAndSrcProfileDto.getHost().isEmpty())
		sourceConnectionDetails.setHost(sourceConcAndSrcProfileDto.getHost());
		if(sourceConcAndSrcProfileDto.getUserName()!=null && !sourceConcAndSrcProfileDto.getUserName().isEmpty())
		sourceConnectionDetails.setUserName(sourceConcAndSrcProfileDto.getUserName());
		if(sourceConcAndSrcProfileDto.getPassword()!=null && !sourceConcAndSrcProfileDto.getPassword().isEmpty())
		sourceConnectionDetails.setPassword(sourceConcAndSrcProfileDto.getPassword());
		if(sourceConcAndSrcProfileDto.getUrl()!=null && !sourceConcAndSrcProfileDto.getUrl().isEmpty())
		sourceConnectionDetails.setUrl(sourceConcAndSrcProfileDto.getUrl());
		if(sourceConcAndSrcProfileDto.getConnectionType()!=null && !sourceConcAndSrcProfileDto.getConnectionType().isEmpty())
			sourceConnectionDetails.setConnectionType(sourceConcAndSrcProfileDto.getConnectionType());
		if(sourceConcAndSrcProfileDto.getAccessToken()!=null && !sourceConcAndSrcProfileDto.getAccessToken().isEmpty())
			sourceConnectionDetails.setAccessToken(sourceConcAndSrcProfileDto.getAccessToken());
		if(sourceConcAndSrcProfileDto.getPort()!=null && !sourceConcAndSrcProfileDto.getPort().isEmpty())
			sourceConnectionDetails.setPort(sourceConcAndSrcProfileDto.getPort());
		if(sourceConcAndSrcProfileDto.getEnabledFlag()!=null)
			sourceConnectionDetails.setEnabledFlag(sourceConcAndSrcProfileDto.getEnabledFlag());
		sourceConnectionDetails.setCreatedDate(ZonedDateTime.now());
		sourceConnectionDetails.setLastUpdatedDate(ZonedDateTime.now());
		sourceConnectionDetails.setCreatedBy(userId);
		sourceConnectionDetails.setLastUpdatedBy(userId);
		sourceConnectionDetails.setTenantId(tenantId);
		if(sourceConcAndSrcProfileDto.getStartDate()!=null)
		sourceConnectionDetails.setStartDate(sourceConcAndSrcProfileDto.getStartDate());
		if(sourceConcAndSrcProfileDto.getEndDate()!=null)
		sourceConnectionDetails.setEndDate(sourceConcAndSrcProfileDto.getEndDate());*/
		 result = SourceConnectionStatusService.saveSourceConnectionDetails(sourceConcAndSrcProfileDto,result, userId, tenantId);
		 result = sourceConnectionDetailsRepository.save(result);

		//SourceConnectionDetails sourceConnectionDetails = SourceConnectionStatusService.saveSourceConnectionDetails(sourceConcAndSrcProfileDto, userId, tenantId);

		String idForDisplay = IDORUtils.computeFrontEndIdentifier(result.getId().toString());
		result.setIdForDisplay(idForDisplay);
		result = sourceConnectionDetailsRepository.save(result);

		sourceConnectionDetailsSearchRepository.save(result);
		List<SourceProfiles> sourceProfileList=sourceConcAndSrcProfileDto.getSourceProfileList();
		for(SourceProfiles sourceProfiles:sourceProfileList)
		{
			log.info("sourceProfiles :"+sourceProfiles);
			SourceProfiles sp = new SourceProfiles();
			if(sourceProfiles != null && sourceProfiles.getId() != null)
			{
				sp =sourceProfilesRepository.findOne(sourceProfiles.getId());
				//sourceProfiles.setId(sourceProfiles.getId());
				sp.setConnectionId(result.getId());
			}
			sp.setLastUpdatedBy(userId);
			sourceProfiles.setLastUpdatedDate(ZonedDateTime.now());
			SourceProfiles spResult = sourceProfilesRepository.save(sp);
			sourceProfilesSearchRepository.save(spResult);
		}
		return ResponseEntity.created(new URI("/api/source-connection-details/" + result.getIdForDisplay()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdForDisplay()))
				.body(result);



	}

	/**
	 * PUT  /source-connection-details : Updates an existing sourceConnectionDetails.
	 *
	 * @param sourceConnectionDetails the sourceConnectionDetails to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated sourceConnectionDetails,
	 * or with status 400 (Bad Request) if the sourceConnectionDetails is not valid,
	 * or with status 500 (Internal Server Error) if the sourceConnectionDetails couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/source-connection-details")
	@Timed
	public ResponseEntity<SourceConnectionDetails> updateSourceConnectionDetails(HttpServletRequest request,@RequestBody SourceConcAndSrcProfileDto srcConnectionDetails) throws URISyntaxException {
		//log.debug("REST request to update SourceConnectionDetails : {}", sourceConnectionDetails);
//		SourceConcAndSrcProfileDto sourceConnectionDetails = new SourceConcAndSrcProfileDto();
		List<SourceProfiles> srcProfiles = srcConnectionDetails.getSourceProfileList();
		log.info("Source Profiles Size: "+ srcProfiles.size());
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		Long userId=Long.parseLong(map.get("userId").toString());
		
		SourceConnectionDetails result = new SourceConnectionDetails();
		/*if (sourceConnectionDetails.getId() == null) {
			//return createSourceConnectionDetails(sourceConnectionDetails,userId,tenantId);
		}*/
		/*sourceConnectionDetails.setId(srcConnectionDetails.getId());
		if(srcConnectionDetails.getName()!=null && !srcConnectionDetails.getName().isEmpty())
			sourceConnectionDetails.setName(srcConnectionDetails.getName());
		if(srcConnectionDetails.getDescription()!=null && !srcConnectionDetails.getDescription().isEmpty())
			sourceConnectionDetails.setDescription(srcConnectionDetails.getDescription());
		if(srcConnectionDetails.getProtocol()!=null && !srcConnectionDetails.getProtocol().isEmpty())
			sourceConnectionDetails.setProtocol(srcConnectionDetails.getProtocol());
		if(srcConnectionDetails.getClientKey()!=null && !srcConnectionDetails.getClientKey().isEmpty())
			sourceConnectionDetails.setClientKey(srcConnectionDetails.getClientKey());
		if(srcConnectionDetails.getClientKey()!=null && !srcConnectionDetails.getClientKey().isEmpty())
			sourceConnectionDetails.setClientSecret(srcConnectionDetails.getClientKey());
		if(srcConnectionDetails.getAuthEndpointUrl()!=null && !srcConnectionDetails.getAuthEndpointUrl().isEmpty())
			sourceConnectionDetails.setAuthEndpointUrl(srcConnectionDetails.getAuthEndpointUrl());
		if(srcConnectionDetails.getTokenEndpointUrl()!=null && !srcConnectionDetails.getTokenEndpointUrl().isEmpty())
			sourceConnectionDetails.setTokenEndpointUrl(srcConnectionDetails.getTokenEndpointUrl());
		if(srcConnectionDetails.getCallBackUrl()!=null && !srcConnectionDetails.getCallBackUrl().isEmpty())
			sourceConnectionDetails.setCallBackUrl(srcConnectionDetails.getCallBackUrl());
		if(srcConnectionDetails.getHost()!=null && !srcConnectionDetails.getHost().isEmpty())
			sourceConnectionDetails.setHost(srcConnectionDetails.getHost());
		if(srcConnectionDetails.getUserName()!=null && !srcConnectionDetails.getUserName().isEmpty())
			sourceConnectionDetails.setUserName(srcConnectionDetails.getUserName());
		if(srcConnectionDetails.getPassword()!=null && !srcConnectionDetails.getPassword().isEmpty())
			sourceConnectionDetails.setPassword(srcConnectionDetails.getPassword());
		if(srcConnectionDetails.getUrl()!=null && !srcConnectionDetails.getUrl().isEmpty())
			sourceConnectionDetails.setUrl(srcConnectionDetails.getUrl());
		if(srcConnectionDetails.getConnectionType()!=null && !srcConnectionDetails.getConnectionType().isEmpty())
			sourceConnectionDetails.setConnectionType(srcConnectionDetails.getConnectionType());
		if(srcConnectionDetails.getAccessToken()!=null && !srcConnectionDetails.getAccessToken().isEmpty())
			sourceConnectionDetails.setAccessToken(srcConnectionDetails.getAccessToken());
		if(srcConnectionDetails.getPort()!=null && !srcConnectionDetails.getPort().isEmpty())
			sourceConnectionDetails.setPort(srcConnectionDetails.getPort());
		if(srcConnectionDetails.getEnabledFlag()!=null)
			sourceConnectionDetails.setEnabledFlag(srcConnectionDetails.getEnabledFlag());
		sourceConnectionDetails.setCreatedDate(ZonedDateTime.now());
		sourceConnectionDetails.setLastUpdatedDate(ZonedDateTime.now());
		sourceConnectionDetails.setCreatedBy(userId);
		sourceConnectionDetails.setLastUpdatedBy(userId);
		sourceConnectionDetails.setTenantId(tenantId);
		if(srcConnectionDetails.getStartDate()!=null)
			sourceConnectionDetails.setStartDate(srcConnectionDetails.getStartDate());
		if(srcConnectionDetails.getEndDate()!=null)
			sourceConnectionDetails.setEndDate(srcConnectionDetails.getEndDate());*/
		
		 result = SourceConnectionStatusService.saveSourceConnectionDetails(srcConnectionDetails,result, userId, tenantId);
		 result = sourceConnectionDetailsRepository.save(result);
		
//		SourceConnectionDetails sourceConnectionDetails = SourceConnectionStatusService.saveSourceConnectionDetails(srcConnectionDetails, userId, tenantId);
//		SourceConnectionDetails result = sourceConnectionDetailsRepository.save(sourceConnectionDetails);
//		SourceConnectionDetails result = SourceConnectionStatusService.saveSourceConnectionDetails(srcConnectionDetails, userId, tenantId);

		sourceConnectionDetailsSearchRepository.save(result);
		List<BigInteger> srcProfileIds = sourceProfilesRepository.fetchSrcProfileIdsByConnectionId(result.getId());
		log.info("Connection Id: "+ srcConnectionDetails.getId());
		log.info("srcProfileIds: "+ srcProfileIds);
		if(srcProfiles.size()>0)
		{
			for(SourceProfiles srcProfile : srcProfiles)
			{
				log.info("Source Profile Id: "+ srcProfile.getId());
				if(srcProfileIds.contains(new BigInteger(srcProfile.getId().toString())))
				{
					log.info("Id: "+ srcProfile.getId()+" exist");
					srcProfile.setId(srcProfile.getId());

					SourceProfiles spResult = sourceProfilesRepository.save(srcProfile);
					sourceProfilesSearchRepository.save(spResult);
					srcProfileIds.remove(new BigInteger(srcProfile.getId().toString()));
				}
				else
				{
					srcProfile.setId(srcProfile.getId());
					srcProfile.setConnectionId(result.getId());
					SourceProfiles spResult = sourceProfilesRepository.save(srcProfile);
					sourceProfilesSearchRepository.save(spResult);
				}
				if(srcProfileIds.size()>0)
				{
					for(BigInteger id : srcProfileIds)
					{
						SourceProfiles sp = sourceProfilesRepository.findOne(id.longValue());
						sp.setId(sp.getId());
						sp.setConnectionId(null);
						SourceProfiles spResult = sourceProfilesRepository.save(sp);
						sourceProfilesSearchRepository.save(spResult);
					}
				}
			}
		}
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getIdForDisplay()))
				.body(result);
	}

	/**
	 * GET  /source-connection-details : get all the sourceConnectionDetails.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of sourceConnectionDetails in body
	 */
	@GetMapping("/sourceConnectionDetailsByTenantId")
	@Timed
	public List<HashMap> getAllSourceConnectionDetails(@RequestParam(value = "page" , required = false) Integer offset,@RequestParam(required = false) String sortColName, HttpServletRequest request,
			@RequestParam(required = false) String sortOrder, @RequestParam(value = "per_page", required = false) Integer limit) throws URISyntaxException {

		Page<SourceConnectionDetails> page = null;
		HttpHeaders headers = null;

		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		log.debug("REST request to get a page of SourceConnectionDetails:"+offset+"=>"+sortColName+"=>"+limit+"=>sortOrder"+sortOrder+"=>tenant: "+tenantId);

		if(sortOrder==null)
			sortOrder="Descending";
		if(sortColName==null)
			sortColName="id";

		List<HashMap> finalList=new ArrayList<HashMap>();

		if(limit==null || limit == 0){
			limit=sourceConnectionDetailsRepository.findByTenantIdOrderByIdDesc(tenantId).size();
			log.info("limit == "+limit);
		}
		if(offset == null || offset == 0)
		{
			offset = 0;
		}
		page  = sourceConnectionDetailsRepository.findByTenantId(tenantId,PaginationUtil.generatePageRequestWithSortColumn(offset, limit, sortOrder, sortColName));

		List<SourceConnectionDetails> srcCons = new ArrayList<SourceConnectionDetails>();
		srcCons = page.getContent();
		int count =  0;
		count = sourceConnectionDetailsRepository.findByTenantId(tenantId).size();
		for(int i=0;i<srcCons.size();i++)
		{
			HashMap mp = new HashMap();
			mp.put("count",count);
			mp.put("id",srcCons.get(i).getIdForDisplay());
			mp.put("name",srcCons.get(i).getName());
			mp.put("description",srcCons.get(i).getDescription());
			mp.put("userName",srcCons.get(i).getUserName());
			LookUpCode lookUp= new LookUpCode();
			lookUp=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId( "CONNECTION_TYPE",srcCons.get(i).getConnectionType(), tenantId);
			if(lookUp != null && lookUp.getMeaning() != null)
			{
				mp.put("connectionTypeCode",lookUp.getLookUpCode());
				mp.put("connectionType",lookUp.getMeaning());
			}
				
			/*if(srcCons.get(i).getEndDate()!=null && srcCons.get(i).getEndDate().isBefore(ZonedDateTime.now()) )
			{
				log.info("entered 1");
				mp.put("endDated",true);
			}

			else
			{
				log.info("entered 2");
				mp.put("endDated",false);
			}*/

			/** active check**/
			Boolean activeStatus=activeStatusService.activeStatus(srcCons.get(i).getStartDate(), srcCons.get(i).getEndDate(),srcCons.get(i).isEnabledFlag());
			mp.put("enabledFlag",activeStatus.toString());
			/** active check end**/
			
			

			/*if(srcCons.get(i).isEnabledFlag() != null )
			{
				log.info("entered not nll");
				mp.put("enabledFlag",srcCons.get(i).isEnabledFlag());
			}
			else
			{
				log.info("entered nll");
				mp.put("enabledFlag","");
			}*/
			int profCnt = 0; 
			log.info("srcCons.get(i).getId().toString()"+srcCons.get(i).getId().toString());
			profCnt = sourceProfilesRepository.findByConnectionId(Long.valueOf(srcCons.get(i).getId().toString())).size();
			log.info("profCnt:"+profCnt);
			mp.put("profCnt",profCnt);

			finalList.add(mp);
		}

		return finalList;

	}


	/**
	 * GET  /source-connection-details/:id : get the "id" sourceConnectionDetails.
	 *
	 * @param id the id of the sourceConnectionDetails to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the sourceConnectionDetails, or with status 404 (Not Found)
	 */
	@GetMapping("/source-connection-details/{id}")
	@Timed
	public ResponseEntity<SourceConcAndSrcProfileDto> getSourceConnectionDetails(HttpServletRequest request,@PathVariable String id) {
		log.debug("REST request to get SourceConnectionDetails : {}", id);
		SourceConcAndSrcProfileDto sourceConcAndSrcProfileDto=new SourceConcAndSrcProfileDto();

		//		SourceConnectionDetails sConnectionDetails = sourceConnectionDetailsRepository.findByIdForDisplay(id);
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		List<SourceProfiles> sourceProfileList= new ArrayList<SourceProfiles>();
		SourceConnectionDetails sourceConnectionDetails = sourceConnectionDetailsRepository.findByIdForDisplayAndTenantId(id,tenantId); 
		if(sourceConnectionDetails != null)
		{
			
		
		log.info("sourceConnectionDetails :"+sourceConnectionDetails);
		if (sourceConnectionDetails.getId() != null) 
		{
			sourceConcAndSrcProfileDto.setId(sourceConnectionDetails.getId());
			int profCnt = 0;
			profCnt = sourceProfilesRepository.findByConnectionId(sourceConnectionDetails.getId()).size();
			sourceConcAndSrcProfileDto.setActiveProfileCnt(profCnt);
		}
			
		if(sourceConnectionDetails.getIdForDisplay() != null)
			sourceConcAndSrcProfileDto.setIdForDisplay(sourceConnectionDetails.getIdForDisplay());
		if(sourceConnectionDetails.getName()!=null && !sourceConnectionDetails.getName().isEmpty())
			sourceConcAndSrcProfileDto.setName(sourceConnectionDetails.getName());
		if(sourceConnectionDetails.getDescription()!=null && !sourceConnectionDetails.getDescription().isEmpty())
			sourceConcAndSrcProfileDto.setDescription(sourceConnectionDetails.getDescription());
		if(sourceConnectionDetails.getProtocol()!=null && !sourceConnectionDetails.getProtocol().isEmpty())
			sourceConcAndSrcProfileDto.setProtocol(sourceConnectionDetails.getProtocol());
		if(sourceConnectionDetails.getClientKey()!=null && !sourceConnectionDetails.getClientKey().isEmpty())
			sourceConcAndSrcProfileDto.setClientKey(sourceConnectionDetails.getClientKey());
		if(sourceConnectionDetails.getClientKey()!=null && !sourceConnectionDetails.getClientKey().isEmpty())
			sourceConcAndSrcProfileDto.setClientSecret(sourceConnectionDetails.getClientKey());
		if(sourceConnectionDetails.getAuthEndpointUrl()!=null && !sourceConnectionDetails.getAuthEndpointUrl().isEmpty())
			sourceConcAndSrcProfileDto.setAuthEndpointUrl(sourceConnectionDetails.getAuthEndpointUrl());
		if(sourceConnectionDetails.getTokenEndpointUrl()!=null && !sourceConnectionDetails.getTokenEndpointUrl().isEmpty())
			sourceConcAndSrcProfileDto.setTokenEndpointUrl(sourceConcAndSrcProfileDto.getTokenEndpointUrl());
		if(sourceConnectionDetails.getCallBackUrl()!=null && !sourceConnectionDetails.getCallBackUrl().isEmpty())
			sourceConcAndSrcProfileDto.setCallBackUrl(sourceConnectionDetails.getCallBackUrl());
		if(sourceConnectionDetails.getHost()!=null && !sourceConnectionDetails.getHost().isEmpty())
			sourceConcAndSrcProfileDto.setHost(sourceConnectionDetails.getHost());
		if(sourceConnectionDetails.getUserName()!=null && !sourceConnectionDetails.getUserName().isEmpty())
			sourceConcAndSrcProfileDto.setUserName(sourceConnectionDetails.getUserName());
		if(sourceConnectionDetails.getPassword()!=null && !sourceConnectionDetails.getPassword().isEmpty())
			sourceConcAndSrcProfileDto.setPassword(sourceConnectionDetails.getPassword());
		if(sourceConnectionDetails.getUrl()!=null && !sourceConnectionDetails.getUrl().isEmpty())
			sourceConcAndSrcProfileDto.setUrl(sourceConnectionDetails.getUrl());
		sourceConcAndSrcProfileDto.setLastUpdatedDate(sourceConnectionDetails.getLastUpdatedDate());
		sourceConcAndSrcProfileDto.setCreatedDate(sourceConnectionDetails.getCreatedDate());
		sourceConcAndSrcProfileDto.setCreatedBy(sourceConnectionDetails.getCreatedBy());
		sourceConcAndSrcProfileDto.setLastUpdatedBy(sourceConnectionDetails.getLastUpdatedBy());
		sourceConcAndSrcProfileDto.setTenantId(sourceConnectionDetails.getTenantId());
		if(sourceConnectionDetails.getAccessToken()!=null && !sourceConnectionDetails.getAccessToken().isEmpty())
			sourceConcAndSrcProfileDto.setAccessToken(sourceConnectionDetails.getAccessToken());
		if(sourceConnectionDetails.getPort()!=null && !sourceConnectionDetails.getPort().isEmpty())
			sourceConcAndSrcProfileDto.setPort(sourceConnectionDetails.getPort());
		if(sourceConnectionDetails.getConnectionType()!=null && !sourceConnectionDetails.getConnectionType().isEmpty())
			sourceConcAndSrcProfileDto.setConnectionType(sourceConnectionDetails.getConnectionType());
		if(sourceConnectionDetails.getStartDate()!=null)
			sourceConcAndSrcProfileDto.setStartDate(sourceConnectionDetails.getStartDate());
		if(sourceConnectionDetails.getEndDate()!=null)
			sourceConcAndSrcProfileDto.setEndDate(sourceConnectionDetails.getEndDate());
		if(sourceConnectionDetails.isEnabledFlag()!=null)
			sourceConcAndSrcProfileDto.setEnabledFlag(sourceConnectionDetails.isEnabledFlag());

		//	sourceConcAndSrcProfileDto.setIdForDisplay(id);
		System.out.println("sourceConnectionDetails.getId(): "+sourceConnectionDetails.getId());
		
		sourceProfileList = sourceProfilesRepository.findByConnectionId(sourceConnectionDetails.getId());
		sourceConcAndSrcProfileDto.setSourceProfileList(sourceProfileList);
		}
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(sourceConcAndSrcProfileDto));

	}

	/**
	 * DELETE  /source-connection-details/:id : delete the "id" sourceConnectionDetails.
	 *
	 * @param id the id of the sourceConnectionDetails to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/source-connection-details/{id}")
	@Timed
	public ResponseEntity<Void> deleteSourceConnectionDetails(@PathVariable Long id) {
		log.debug("REST request to delete SourceConnectionDetails : {}", id);
		sourceConnectionDetailsRepository.delete(id);
		sourceConnectionDetailsSearchRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH  /_search/source-connection-details?query=:query : search for the sourceConnectionDetails corresponding
	 * to the query.
	 *
	 * @param query the query of the sourceConnectionDetails search 
	 * @param pageable the pagination information
	 * @return the result of the search
	 */
	@GetMapping("/_search/source-connection-details")
	@Timed
	public ResponseEntity<List<SourceConnectionDetails>> searchSourceConnectionDetails(@RequestParam String tenantId, /*@RequestParam(value="columnName",required=false) String columnName, @RequestParam(value="columnValue",required=false) String columnValue,*/ @RequestParam(value="filterValue",required=false) String filterValue, @ApiParam Pageable pageable) {
		String query = "";
		query = query + " tenantId: \""+tenantId+"\" ";		// Filtering with tenant id

		/*    	if(columnName != null && columnValue != null)
    	{
    		 String colName = elasticSearchColumnNamesService.getSourceConnectionColName(columnName);
    		 if(colName.length()>0)
    	     {
    	        query = query + " AND "+colName+": \""+ columnValue+"\"";
    	     }
    	     if(filterValue != null)
    	     {
    	        query = query + " AND \""+filterValue+"\"";
    	     } 
    	}
    	else
    	{*/
		if(filterValue != null)
		{
			query = query + " AND \""+filterValue+"\"";
		} 
		/*}*/
		log.info("ElasticSearch Query: "+ query);

		Page<SourceConnectionDetails> page = sourceConnectionDetailsSearchRepository.search(queryStringQuery(query), pageable);
		HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/source-connection-details");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}


	/**
	 * Author Shiva, Kiran(moved code to use service call)
	 * @param tenantId
	 * @throws SftpException
	 * @throws IOException
	 * @throws DbxException
	 */
	/*@GetMapping("/fetchFilesFromCloudToLocalDirectory")
	@Timed
	//@Scheduled(fixedDelay=10000)
	public void  fetchFilesFromCloudToLocalDirectory(@RequestParam(value="tenantId") Long tenantId) throws SftpException, IOException, DbxException
	{
		log.info("* * * * * REST API TO FETCHING FILES FROM CLOUD TO LOCAL DIRECTORY * * * * * ");
		List<SourceConnectionDetails> connectionDetails = sourceConnectionDetailsRepository.findByTenantId(tenantId);
		if(connectionDetails.size() > 0)
		{
			for(SourceConnectionDetails scd : connectionDetails)
			{
				sourceConnectionDetailsService.savingFilesToLocalDir(scd,tenantId);
			}
		}
		else
		{
			log.info("No Source Connection Details Found The Tenant: " + tenantId);
		}


	}*/


	/**
	 * Author ravali
	 * @param tenantId
	 * @return (SourceConnectionDetails of Distinct connection type)
	 */

	@GetMapping("/fetchSrcConnectionDetails")
	@Timed
	public List<SrcConnectionDetailsDTO> fetchSrcConnectionDetailsByTenantId(HttpServletRequest request)
	{
		HashMap map1=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map1.get("tenantId").toString());
		log.info("Rest Request to get Source connection details of distinct connectionType :"+tenantId);
		List<SrcConnectionDetailsDTO> SrcConnectionDetailsDTOList=new ArrayList<SrcConnectionDetailsDTO>();
		List<String> distinctConnectionType = sourceConnectionDetailsRepository.findByDistinctConnectionTypeAndTenantId(tenantId);
		log.info("distinctConnectionType :"+distinctConnectionType);
		for(int i=0;i<distinctConnectionType.size();i++)
		{
			if(distinctConnectionType.get(i)!=null)
			{
				String lookUpType="CONNECTION_TYPE";
				String type=distinctConnectionType.get(i);
				LookUpCode lookUpMeaning=lookUpCodeRepository.findByTenantIdAndLookUpCodeAndLookUpType(tenantId,distinctConnectionType.get(i),lookUpType);
				List<LookUpCode> lookUpCodeAndMeaning=lookUpCodeRepository.findByTenantIdAndLookUpType(tenantId,type);
				List<HashMap> finalList=new ArrayList<HashMap>();
				for(int j=0;j<lookUpCodeAndMeaning.size();j++)
				{
					HashMap map = new HashMap();
					if(lookUpCodeAndMeaning.get(j).getLookUpCode()!=null)
						map.put("code", lookUpCodeAndMeaning.get(j).getLookUpCode());
					if(lookUpCodeAndMeaning.get(j).getMeaning()!=null)
						map.put("meaning",lookUpCodeAndMeaning.get(j).getMeaning());
					if(map!=null&&!map.isEmpty())
						finalList.add(map);
				}
				log.info("finalList :"+finalList);
				List<SourceConnectionDetails>  srcConncDetails=sourceConnectionDetailsRepository.findByConnectionTypeAndTenantIdAndActivestatus(distinctConnectionType.get(i),tenantId);
				SrcConnectionDetailsDTO SrcConnectionDetailsDTO=new SrcConnectionDetailsDTO();
				SrcConnectionDetailsDTO.setConnectionTypeCode(distinctConnectionType.get(i));
				if(lookUpMeaning!=null&&lookUpMeaning.getMeaning()!=null)
					SrcConnectionDetailsDTO.setConnectionTypeMeaning(lookUpMeaning.getMeaning());
				SrcConnectionDetailsDTO.setSourceConnectionDetails(srcConncDetails);
				SrcConnectionDetailsDTO.setDisplayColumns(finalList);
				SrcConnectionDetailsDTOList.add(SrcConnectionDetailsDTO);
			}
		}
		return SrcConnectionDetailsDTOList;

	}

	//another API with dto  as input which contains, tenantid, start , end date,connection type
	/**
	 * Author Shiva
	 * @param SourceConnectionsDTO with input values connectionType, tenantId, startDate, endDate 
	 * @return (SourceConnectionDetails of Distinct connection type)
	 */
	@PostMapping("/getSrcConnectionsByActiveStatusAndConType")
	@Timed
	public List<SourceConnectionDetails> getSrcConnectionsByActiveStatusAndConType(@RequestBody SourceConnectionsDTO sourceConn){
		log.info("Rest api for fetching source connection details based on active status and connection type");
		List<SrcConnectionDetailsDTO> finalData = new ArrayList<SrcConnectionDetailsDTO>();
		LocalDate startDate = sourceConn.getStartDate();
		LocalDate endDate = sourceConn.getEndDate();
		log.info("DTO Start Date: "+ startDate +", End Date: "+ endDate);
		List<SourceConnectionDetails> scd = new ArrayList<SourceConnectionDetails>();

		if(startDate != null)
		{
			if(endDate != null)
			{
				scd = sourceConnectionDetailsRepository.fetchScrConnctionsByActiveStatusAndConnectionType(sourceConn.getTenantId(), startDate, endDate, sourceConn.getConnectionType());
			}
			else
			{
				scd = sourceConnectionDetailsRepository.fetchScrConnctionsByActiveStatusAndConnectionTypeWithStartDate(sourceConn.getTenantId(), sourceConn.getStartDate(),sourceConn.getConnectionType());
			}
		}
		log.info("No of records fetched : "+ scd.size());
		return scd;
	}

	/**
	 * Author ravali
	 * @param tenantId
	 * @return (sourceconnectiontype details with lookUpmeaning by tenantId)
	 */

	@GetMapping("/SrcConnectionDetails")
	@Timed
	public List<HashMap> getConnectionDetails(HttpServletRequest request)
	{
		HashMap map1=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map1.get("tenantId").toString());

		log.info("Rest Request to ge source connection details with LookUpMeaning by tenantId :"+tenantId);
		List<HashMap> finalList=new ArrayList<HashMap>();
		List<SourceConnectionDetails> srcConcDetailsList=sourceConnectionDetailsRepository.findByTenantIdOrderByIdDesc(tenantId);
		for(SourceConnectionDetails scd : srcConcDetailsList)
		{
			HashMap map=new HashMap();
			map.put("id", scd.getId());
			if(scd.getName()!=null&&!scd.getName().isEmpty())
				map.put("name",scd.getName());
			else
				map.put("name","");
			if(scd.getDescription()!=null && !scd.getDescription().isEmpty())
				map.put("description", scd.getDescription());
			else
				map.put("description", "");
			if(scd.getProtocol()!=null && !scd.getProtocol().isEmpty())
				map.put("protocol", scd.getProtocol());
			else
				map.put("protocol","");
			if(scd.getClientKey()!=null && !scd.getClientKey().isEmpty())
				map.put("clientKey", scd.getClientKey());
			else
				map.put("clientKey", "");
			if(scd.getClientSecret()!=null && !scd.getClientSecret().isEmpty())
				map.put("clientSecret", scd.getClientSecret());
			else
				map.put("clientSecret","");
			if(scd.getAuthEndpointUrl()!=null && !scd.getAuthEndpointUrl().isEmpty())
				map.put("authEndpointUrl", scd.getAuthEndpointUrl());
			else
				map.put("authEndpointUrl","");
			if(scd.getTokenEndpointUrl()!=null && !scd.getTokenEndpointUrl().isEmpty())
				map.put("tokenEndPointUrl", scd.getTokenEndpointUrl());
			else
				map.put("tokenEndPointUrl", "");
			if(scd.getCallBackUrl()!=null && !scd.getCallBackUrl().isEmpty())
				map.put("callBackUrl",scd.getCallBackUrl());
			else
				map.put("callBackUrl", "");
			if(scd.getHost()!=null && scd.getHost().isEmpty())
				map.put("host", scd.getHost());
			else
				map.put("host","");
			if(scd.getUserName()!=null && !scd.getUserName().isEmpty())
				map.put("userName", scd.getUserName());
			else
				map.put("userName","");
			if(scd.getPassword()!=null && !scd.getPassword().isEmpty())
				map.put("password",scd.getPassword());
			else
				map.put("password","");
			if(scd.getUrl()!=null && !scd.getUrl().isEmpty())
				map.put("url", scd.getUrl());
			else
				map.put("url","");
			map.put("tenantId", scd.getTenantId());
			if(scd.getConnectionType()!=null && !scd.getConnectionType().isEmpty())
			{
				map.put("connectionType", scd.getConnectionType());
				LookUpCode meaning=lookUpCodeRepository.findByTenantIdAndLookUpCode(tenantId,scd.getConnectionType());
				if(meaning.getMeaning()!=null && !meaning.getMeaning().isEmpty())
					map.put("connectionTypeMeaning", meaning.getMeaning());
				else
					map.put("connectionTypeMeaning","");
			}
			else
				map.put("connectionType","");
			if(scd.getAccessToken()!=null && scd.getAccessToken().isEmpty())
				map.put("accessToken", scd.getAccessToken());
			else
				map.put("accessToken","")	;
			if(scd.getPort()!=null && !scd.getPort().isEmpty())
				map.put("port", scd.getPort());
			else
				map.put("port","");
			if(scd.getStartDate()!=null)
				map.put("startDate",scd.getStartDate());
			else
				map.put("startDate","");
			if(scd.getEndDate()!=null)
				map.put("endDate", scd.getEndDate());
			else
				map.put("endDate","");
			if(scd.getEndDate()!=null &&scd.getEndDate().isBefore(ZonedDateTime.now()) )
				map.put("endDated",true);
			else
				map.put("endDated",false);
			if(scd.getCreatedDate()!=null)
				map.put("createdDate", scd.getCreatedDate());
			else
				map.put("createdDate","");
			if(scd.getCreatedBy()!=null)
				map.put("createdBy", scd.getCreatedBy());
			else
				map.put("createdBy","");
			if(scd.getLastUpdatedDate()!=null)
				map.put("lastUpdatedDate", scd.getLastUpdatedDate());
			else
				map.put("lastUpdatedDate","");
			if(scd.getLastUpdatedBy()!=null)
				map.put("lastUpdatedBy", scd.getLastUpdatedBy());
			else
				map.put("lastUpdatedBy","");
			if(scd.isEnabledFlag()!=null)
				map.put("enabledFlag", scd.isEnabledFlag());
			else
				map.put("enabledFlag","");


			if(map!=null&&!map.isEmpty())
				finalList.add(map);
		}
		return finalList;
	}


	/**
	 * Author Kiran
	 * @param sourceConnDetails
	 * @return
	 */

	@PostMapping("/testSourceConnections")
	@Timed
	public HashMap<String, String> checkingConnection(@RequestBody SourceConnectionDetails sourceConnDetails, HttpServletRequest request) //throws IOException
	{
		//SourceConnectionDetails sConnDetails = sourceConnectionDetailsRepository.findOne(id);
		HashMap map1=userJdbcService.getuserInfoFromToken(request);
		Long userId=Long.parseLong(map1.get("userId").toString());
		log.info("====== Rest Api to check the Conection Status ======");
		log.info("Test Connection Start time: "+new Date());
		HashMap<String, String> conc=new HashMap<String, String>();
		if(sourceConnDetails!=null && sourceConnDetails.getConnectionType()!=null)
		{
			if(sourceConnDetails.getConnectionType().equalsIgnoreCase("SFTP"))
			{
				conc =SourceConnectionStatusService.checkSFTConnection(sourceConnDetails);
			}
			else if(sourceConnDetails.getConnectionType().equalsIgnoreCase("DROP_BOX"))
			{
				conc =SourceConnectionStatusService.checkDropBoxConnection(sourceConnDetails);
			}
			else if(sourceConnDetails.getConnectionType().equalsIgnoreCase("GOOGLE_DRIVE"))
			{
				conc=SourceConnectionStatusService.checkGoogleConnection(sourceConnDetails,userId);
			}
			else if(sourceConnDetails.getConnectionType().equalsIgnoreCase("BOX"))
			{
				conc=SourceConnectionStatusService.checkBoxConnection(sourceConnDetails);
			}
			else if(sourceConnDetails.getConnectionType().equalsIgnoreCase("EMAIL"))
			{
				conc=SourceConnectionStatusService.checkEmailConnection(sourceConnDetails);
			}
		}
		log.info("Test Connection End time: "+new Date());
		return conc;
	}
	/**
	 * Author : Shobha	
	 * @param request
	 * @return
	 * @throws URISyntaxException
	 */
	@GetMapping("/connectionsForTenant")
	@Timed
	public List<SourceConnectionDetails> connectionsForTenant(HttpServletRequest request) throws URISyntaxException {
		log.info("Rest request to fetch src connections");
		HashMap map1=userJdbcService.getuserInfoFromToken(request);
		Long userId=Long.parseLong(map1.get("userId").toString());
		Long tenantId=Long.parseLong(map1.get("tenantId").toString());
		List<SourceConnectionDetails> sourceCons = new ArrayList<SourceConnectionDetails>();
		sourceCons = sourceConnectionDetailsRepository.findByTenantId(tenantId);
		return sourceCons;
		
	}

}