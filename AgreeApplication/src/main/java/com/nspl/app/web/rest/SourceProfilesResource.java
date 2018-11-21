package com.nspl.app.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
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
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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
import com.nspl.app.config.ApplicationContextProvider;
import com.nspl.app.domain.FileTemplates;
import com.nspl.app.domain.SourceConnectionDetails;
import com.nspl.app.domain.SourceProfileFileAssignments;
import com.nspl.app.domain.SourceProfiles;
import com.nspl.app.domain.TenantConfig;
import com.nspl.app.repository.FileTemplatesRepository;
import com.nspl.app.repository.SourceConnectionDetailsRepository;
import com.nspl.app.repository.SourceProfileFileAssignmentsRepository;
import com.nspl.app.repository.SourceProfilesRepository;
import com.nspl.app.repository.TenantConfigRepository;
import com.nspl.app.repository.search.SourceProfileFileAssignmentsSearchRepository;
import com.nspl.app.repository.search.SourceProfilesSearchRepository;
import com.nspl.app.security.IDORUtils;
import com.nspl.app.service.ElasticSearchColumnNamesService;
import com.nspl.app.service.OozieService;
import com.nspl.app.service.UserJdbcService;
import com.nspl.app.web.rest.dto.ConnectionDetails;
import com.nspl.app.web.rest.dto.ErrorReport;
import com.nspl.app.web.rest.dto.FileTemplatesPostingDTO;
import com.nspl.app.web.rest.dto.SourceProfileDTO;
import com.nspl.app.web.rest.dto.SourceProfilesAndTemplateDTO;
import com.nspl.app.web.rest.dto.SrcProfileAndSrcProfileAssignDTO;
import com.nspl.app.web.rest.dto.SrcProfileConnectionDetailsDTO;
import com.nspl.app.web.rest.util.HeaderUtil;
import com.nspl.app.web.rest.util.PaginationUtil;

/**
 * REST controller for managing SourceProfiles.
 */
@RestController
@RequestMapping("/api")
public class SourceProfilesResource {

	private final Logger log = LoggerFactory
			.getLogger(SourceProfilesResource.class);

	private static final String ENTITY_NAME = "sourceProfiles";

	private final SourceProfilesRepository sourceProfilesRepository;

	private final SourceProfilesSearchRepository sourceProfilesSearchRepository;

	private final SourceConnectionDetailsRepository sourceConnectionDetailsRepository;

	private final SourceProfileFileAssignmentsSearchRepository sourceProfileFileAssignmentsSearchRepository;
	
	private final FileTemplatesRepository fileTemplatesRepository;

	@Inject
	UserJdbcService userJdbcService;

	@Inject
	SourceProfileFileAssignmentsRepository sourceProfileFileAssignmentsRepository;

	@Inject
	ElasticSearchColumnNamesService elasticSearchColumnNamesService;

	@Inject
	private Environment env;
	
	@Inject
	TenantConfigRepository tenantConfigRepository;
	
    @Inject
	OozieService oozieService;


	@PersistenceContext(unitName="default")
	private EntityManager em;

	public SourceProfilesResource(
			SourceProfilesRepository sourceProfilesRepository,
			SourceProfilesSearchRepository sourceProfilesSearchRepository,
			SourceConnectionDetailsRepository sourceConnectionDetailsRepository,
			SourceProfileFileAssignmentsRepository sourceProfileFileAssignmentsRepository,
			SourceProfileFileAssignmentsSearchRepository sourceProfileFileAssignmentsSearchRepository,
			FileTemplatesRepository fileTemplatesRepository) {
		this.sourceProfilesRepository = sourceProfilesRepository;
		this.sourceProfilesSearchRepository = sourceProfilesSearchRepository;
		this.sourceConnectionDetailsRepository = sourceConnectionDetailsRepository;
		this.sourceProfileFileAssignmentsSearchRepository = sourceProfileFileAssignmentsSearchRepository;
		this.fileTemplatesRepository = fileTemplatesRepository;
	}

	/**
	 * POST /source-profiles : Create a new sourceProfiles.
	 *
	 * @param sourceProfiles
	 *            the sourceProfiles to create
	 * @return the ResponseEntity with status 201 (Created) and with body the
	 *         new sourceProfiles, or with status 400 (Bad Request) if the
	 *         sourceProfiles has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/source-profiles/userId}")
	@Timed
	public ResponseEntity<SourceProfiles> createSourceProfiles(
			@RequestBody SourceProfiles sourceProfiles,@RequestParam Long userId)
					throws URISyntaxException {
		log.debug("REST request to save SourceProfiles : {}", sourceProfiles);
		if (sourceProfiles.getId() != null) {
			return ResponseEntity
					.badRequest()
					.headers(
							HeaderUtil
							.createFailureAlert(ENTITY_NAME,
									"idexists",
									"A new sourceProfiles cannot already have an ID"))
									.body(null);
		}
		sourceProfiles.setCreatedBy(userId);
		sourceProfiles.setLastUpdatedBy(userId);
		sourceProfiles.setCreatedDate(ZonedDateTime.now());
		sourceProfiles.setLastUpdatedDate(ZonedDateTime.now());
		SourceProfiles result = sourceProfilesRepository.save(sourceProfiles);

		String idForDisplay = IDORUtils.computeFrontEndIdentifier(result.getId().toString());
		result.setIdForDisplay(idForDisplay);
		result = sourceProfilesRepository.save(result);

		//result.getId()
		//sourceProfilesSearchRepository.save(result);
		return ResponseEntity
				.created(new URI("/api/source-profiles/" + result.getId()))
				.headers(
						HeaderUtil.createEntityCreationAlert(ENTITY_NAME,
								result.getId().toString())).body(result);
	}

	/**
	 * PUT /source-profiles : Updates an existing sourceProfiles.
	 *
	 * @param sourceProfiles
	 *            the sourceProfiles to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         sourceProfiles, or with status 400 (Bad Request) if the
	 *         sourceProfiles is not valid, or with status 500 (Internal Server
	 *         Error) if the sourceProfiles couldnt be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/source-profiles/{userId}")
	@Timed
	public ResponseEntity<SourceProfiles> updateSourceProfiles(
			@RequestBody SourceProfiles sourceProfiles,@RequestParam Long userId)
					throws URISyntaxException {
		log.debug("REST request to update SourceProfiles : {}", sourceProfiles);
		if (sourceProfiles.getId() == null) {
			return createSourceProfiles(sourceProfiles,userId);
		}
		sourceProfiles.setCreatedBy(userId);
		sourceProfiles.setLastUpdatedBy(userId);
		sourceProfiles.setLastUpdatedDate(ZonedDateTime.now());
		SourceProfiles result = sourceProfilesRepository.save(sourceProfiles);
		//sourceProfilesSearchRepository.save(result);
		return ResponseEntity
				.ok()
				.headers(
						HeaderUtil.createEntityUpdateAlert(ENTITY_NAME,
								sourceProfiles.getId().toString()))
								.body(result);
	}

	/**
	 * GET /source-profiles : get all the sourceProfiles.
	 *
	 * @param pageable
	 *            the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         sourceProfiles in body
	 */
	@GetMapping("/sourceProfilesByTenantId/{tenantId}")
	@Timed
	public ResponseEntity<List<SourceProfiles>> getAllSourceProfiles(
			@ApiParam Pageable pageable,@RequestParam Long tenantId) {
		log.debug("REST request to get a page of SourceProfiles");
		Page<SourceProfiles> page = sourceProfilesRepository.findByTenantIdOrderByIdDesc(tenantId,pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
				page, "/api/sourceProfilesByTenantId");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}






	/**
	 * GET /source-profiles/:id : get the "id" sourceProfiles.
	 *
	 * @param id
	 *            the id of the sourceProfiles to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         sourceProfiles, or with status 404 (Not Found)
	 */
	@GetMapping("/source-profiles/{id}")
	@Timed
	public ResponseEntity<SourceProfiles> getSourceProfiles(
			@PathVariable Long id) {
		log.debug("REST request to get SourceProfiles : {}", id);
		SourceProfiles sourceProfiles = sourceProfilesRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(sourceProfiles));
	}

	/**
	 * DELETE /source-profiles/:id : delete the "id" sourceProfiles.
	 *
	 * @param id
	 *            the id of the sourceProfiles to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/source-profiles/{id}")
	@Timed
	public ResponseEntity<Void> deleteSourceProfiles(@PathVariable Long id) {
		log.debug("REST request to delete SourceProfiles : {}", id);
		sourceProfilesRepository.delete(id);
		//sourceProfilesSearchRepository.delete(id);
		return ResponseEntity
				.ok()
				.headers(
						HeaderUtil.createEntityDeletionAlert(ENTITY_NAME,
								id.toString())).build();
	}

	/**
	 * SEARCH /_search/source-profiles?query=:query : search for the
	 * sourceProfiles corresponding to the query.
	 *
	 * @param query
	 *            the query of the sourceProfiles search
	 * @param pageable
	 *            the pagination information
	 * @return the result of the search
	 */
		@GetMapping("/_search/source-profiles")
		@Timed
		public ResponseEntity<List<SourceProfiles>> searchSourceProfiles(HttpServletRequest request,@RequestParam(value="filterValue",required=false) String filterValue,@RequestParam String sortColName, @RequestParam String sortOrder,
				@RequestParam(value = "page" , required = false) Integer offset, @RequestParam(value = "per_page", required = false) Integer limit) {
			HashMap map=userJdbcService.getuserInfoFromToken(request);
			Long tenantId=Long.parseLong(map.get("tenantId").toString());
			
			log.info("Rest api for fetching source profiles data with elasticsearch for the tenant_id : "+ tenantId);
			String query = "";
			query = query + " tenantId: \""+tenantId+"\" ";		// Filtering with tenant id	
	
			if(filterValue != null)
			{
				query = query + " AND \""+filterValue+"\"";
			} 
			log.info("query"+query);
			log.info("offset:"+offset+"limit"+limit);
			if(limit == null && offset == null)
			{
				offset = 0;
				Integer lmt = sourceProfilesSearchRepository.findByTenantId(tenantId).size();
				limit = lmt;
				if(limit == 0)
					limit=1;
			}	
	
			Page<SourceProfiles> page = sourceProfilesSearchRepository.search(queryStringQuery(query), PaginationUtil.generatePageRequestWithSort(offset, limit, sortColName, sortOrder));
			HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page,"/api/_search/source-profiles");
			return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
		}

	@GetMapping("/sourceProfilesNames/{tenantId}")
	@Timed
	public LinkedHashMap getDistinctSourceProfileNames(
			@PathVariable Long tenantId) {
		log.debug("REST request to fect distinct source profile names by tenantiId :"
				+ tenantId);
		List<SourceProfiles> sourceProfileList = sourceProfilesRepository
				.findByTenantId(tenantId);
		// List<LinkedHashMap> linkedMapList=new ArrayList<LinkedHashMap>();
		LinkedHashMap lhp = new LinkedHashMap();
		if (sourceProfileList.size() > 0 && sourceProfileList != null) {
			for (int i = 0; i < sourceProfileList.size(); i++) {

				SourceProfiles sourceProfile = sourceProfileList.get(i);
				log.info("sourceProfile :" + sourceProfile);

				if (sourceProfile.getSourceProfileName() != null
						&& !sourceProfile.getSourceProfileName().isEmpty())
					lhp.put(sourceProfile.getSourceProfileName(),
							sourceProfile.getId());
				// linkedMapList.add(lhp);
				log.info("lhp :" + lhp);
				log.info("linkedMapList1 :" + lhp);

			}
		}
		log.info("linkedMapList2 :" + lhp);
		return lhp;
	}

	/**
	 * Author : Shobha
	 */
	/*	@GetMapping("/fetchSourceProfiles/{tenantId}")
	@Timed
	public List<SourceProfiles> getAllSourceProfiles(@PathVariable Long tenantId) {

		List<SourceProfiles> listOfSourceProfiles = new ArrayList<SourceProfiles>();
		listOfSourceProfiles = sourceProfilesRepository.findByTenantIdOrderAndActiveStatus(tenantId);
		log.info("size of source profiles" + listOfSourceProfiles.size());
		return listOfSourceProfiles;
	}*/

	@GetMapping("/fetchSourceProfiles")
	@Timed
	public List<SourceProfiles> getAllSourceProfiles(HttpServletRequest request) {	//source profiles
		List<SourceProfiles> listOfSourceProfiles = new ArrayList<SourceProfiles>();
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		log.info("Rest api for fetching active source profile based on active status for tenant: "+tenantId);


		/*		if(sourceProfiles.getStartDate() != null)
		{
			if(sourceProfiles.getEndDate() != null)
			{
				listOfSourceProfiles = sourceProfilesRepository.fetchSourceProfileByActiveStatusWithStartDate(sourceProfiles.getTenantId(), sourceProfiles.getStartDate(), sourceProfiles.getEndDate());		
			}
			else
			{
				listOfSourceProfiles = sourceProfilesRepository.fetchSourceProfileByActiveStatus(sourceProfiles.getTenantId(), sourceProfiles.getStartDate());
			}
		}
		 */		
		listOfSourceProfiles = sourceProfilesRepository.fetchActiveSourceProfileByTenantId(tenantId);
		return listOfSourceProfiles;
	}

	/**
	 * Author:Ravali
	 * get connectionDetailsOfSourceProfilesByTenantId
	 *
	 * @param tenantId
	 * @return SrcProfileConnectionDetailsDTO
	 */
	@GetMapping("/connectionDetails")
	@Timed
	public List<SrcProfileConnectionDetailsDTO> concDetialsOfSrcPRofile(
			HttpServletRequest request, @RequestParam(required=false) String idForDisplay,@RequestParam(required = false) String sortColName, @RequestParam(required = false) String sortOrder,
			@RequestParam(required = false) Boolean isActive) {

		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());

		log.info("Rest Request to get connection details of source profile By tenantId :"+ tenantId+"idForDisplay"+idForDisplay);//+" and profileId: "+Id);
		SourceProfiles srcProfile = new SourceProfiles();
		List<SrcProfileConnectionDetailsDTO> srcProfileConnectionDetailsDTOList = new ArrayList<SrcProfileConnectionDetailsDTO>();
		List<SourceProfiles> srcProfList = new ArrayList<SourceProfiles>();
		if(idForDisplay!=null){
			srcProfile = sourceProfilesRepository.findByTenantIdAndIdForDisplay(tenantId,idForDisplay);
			srcProfList.add(srcProfile);
		}
		else{
			if(isActive!=null && isActive)
				srcProfList = sourceProfilesRepository.findByTenantIdAndEnabledFlagIsTrueOrderByIdDesc(tenantId);	
			else
				srcProfList = sourceProfilesRepository.findByTenantIdOrderByIdDesc(tenantId);
		}
		//log.info("srcProfList :" + srcProf);
	
		// List<SourceConnectionDetails> srcConcDetailsList=new
		// ArrayList<SourceConnectionDetails>();
		for(int i =0;i<srcProfList.size();i++)
		{
			SourceProfiles srcProf = new SourceProfiles();
			srcProf = srcProfList.get(i);
			SrcProfileConnectionDetailsDTO SrcProfConcDto = new SrcProfileConnectionDetailsDTO();
			SrcProfConcDto.setId(srcProf.getId());
			if (srcProf.getSourceProfileName() != null && !srcProf.getSourceProfileName().isEmpty())
				SrcProfConcDto.setSourceProfileName(srcProf.getSourceProfileName());
			if(srcProf.getIdForDisplay() != null)
				SrcProfConcDto.setIdForDisplay(srcProf.getIdForDisplay());
			if (srcProf.getDescription() != null
					&& !srcProf.getDescription().isEmpty())
				SrcProfConcDto.setProfileDescription(srcProf.getDescription());
			if (srcProf.getStartDate() != null)
				SrcProfConcDto.setStartDate(srcProf.getStartDate());
			if (srcProf.getEndDate() != null)
				SrcProfConcDto.setEndDate(srcProf.getEndDate());
			if(srcProf.getEndDate()!=null && srcProf.getEndDate().isBefore(ZonedDateTime.now()))
				SrcProfConcDto.setEndDated(true);
			if (srcProf.isEnabledFlag() != null)
				SrcProfConcDto.setEnabledFlag(srcProf.isEnabledFlag());
			if (srcProf.getConnectionId() != null)
				SrcProfConcDto.setConnectionId(srcProf.getConnectionId());
			if(srcProf.getCreatedBy() != null)
				SrcProfConcDto.setCreatedBy(srcProf.getCreatedBy());
			if(srcProf.getCreatedDate() != null)
				SrcProfConcDto.setCreatedDate(srcProf.getCreatedDate());
			if (srcProf.getConnectionId() != null) {
				SourceConnectionDetails srcConcDetails = sourceConnectionDetailsRepository
						.findOne(srcProf.getConnectionId());
				//	log.info("srcConcDetails :" + srcConcDetails);
				//ConnectionDetails concDTO = new ConnectionDetails();
				if (srcConcDetails != null) {
					if (srcConcDetails.getName() != null
							&& !srcConcDetails.getName().isEmpty())
						SrcProfConcDto.setName(srcConcDetails.getName());
					else
						SrcProfConcDto.setName("");
					if (srcConcDetails.getDescription() != null
							&& !srcConcDetails.getDescription().isEmpty())
						SrcProfConcDto.setConnectionDescription(srcConcDetails
								.getDescription());
					else
						SrcProfConcDto.setConnectionDescription("");
					if (srcConcDetails.getProtocol() != null)
						SrcProfConcDto.setProtocol(srcConcDetails.getProtocol());
					else
						SrcProfConcDto.setProtocol("");
					if (srcConcDetails.getClientKey() != null
							&& !srcConcDetails.getClientKey().isEmpty())
						SrcProfConcDto.setClientKey(srcConcDetails.getClientKey());
					else
						SrcProfConcDto.setClientKey("");
					if (srcConcDetails.getClientSecret() != null
							&& !srcConcDetails.getClientSecret().isEmpty())
						SrcProfConcDto.setClientSecret(srcConcDetails
								.getClientSecret());
					else
						SrcProfConcDto.setClientSecret("");
					if (srcConcDetails.getAuthEndpointUrl() != null
							&& !srcConcDetails.getAuthEndpointUrl().isEmpty())
						SrcProfConcDto.setAuthEndPointUrl(srcConcDetails
								.getAuthEndpointUrl());
					else
						SrcProfConcDto.setAuthEndPointUrl("");
					if (srcConcDetails.getTokenEndpointUrl() != null
							&& !srcConcDetails.getTokenEndpointUrl().isEmpty())
						SrcProfConcDto.setTokenEndPointUrl(srcConcDetails
								.getTokenEndpointUrl());
					else
						SrcProfConcDto.setTokenEndPointUrl("");
					if (srcConcDetails.getCallBackUrl() != null
							&& !srcConcDetails.getCallBackUrl().isEmpty())
						SrcProfConcDto.setCallBackUrl(srcConcDetails.getCallBackUrl());
					else
						SrcProfConcDto.setCallBackUrl("");
					if (srcConcDetails.getHost() != null
							&& !srcConcDetails.getHost().isEmpty())
						SrcProfConcDto.setHost(srcConcDetails.getHost());
					else
						SrcProfConcDto.setHost("");
					if (srcConcDetails.getPassword() != null
							&& !srcConcDetails.getPassword().isEmpty())
						SrcProfConcDto.setJhiPassword(srcConcDetails.getPassword());
					else
						SrcProfConcDto.setJhiPassword("");
					if (srcConcDetails.getConnectionType() != null
							&& !srcConcDetails.getConnectionType().isEmpty())
						SrcProfConcDto.setConnectionType(srcConcDetails
								.getConnectionType());
					else
						SrcProfConcDto.setConnectionType("");
					if (srcConcDetails.getAccessToken() != null
							&& !srcConcDetails.getAccessToken().isEmpty())
						SrcProfConcDto.setAccessToken(srcConcDetails.getAccessToken());
					else
						SrcProfConcDto.setAccessToken("");
					if (srcConcDetails.getPort() != null
							&& !srcConcDetails.getPort().isEmpty())
						SrcProfConcDto.setPort(srcConcDetails.getPort());
					else
						SrcProfConcDto.setPort("");
					if (srcConcDetails.getUserName() != null
							&& !srcConcDetails.getUserName().isEmpty())
						SrcProfConcDto.setUserName(srcConcDetails.getUserName());
					else
						SrcProfConcDto.setUserName("");
				}
			}
			if (SrcProfConcDto != null)
				srcProfileConnectionDetailsDTOList.add(SrcProfConcDto);
		}
		
		//log.info("srcConcDetailsList :" + srcProfileConnectionDetailsDTOList);
		return srcProfileConnectionDetailsDTOList;

	}

	/**
	 * Author:Ravali
	 * get connectionDetailsOfSourceProfilesByTenantId
	 *
	 * @param tenantId
	 * @return SrcProfileConnectionDetailsDTO
	 */	

	@PostMapping("/sourceProfilesAndProfileAssignments")
	@Timed
	public List<ErrorReport> sourceProfilesAndProfileAssignments(HttpServletRequest request,@RequestBody SrcProfileAndSrcProfileAssignDTO SrcProfileAndSrcProfileAssignDTO)//,@RequestParam Long userId)
	{

		List<ErrorReport> finalErrorReport=new ArrayList<ErrorReport>();
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long userId=Long.parseLong(map.get("userId").toString());
		Long tenantId= Long.parseLong(map.get("tenantId").toString());
		log.info("Rest Request to save source profiles and List of Source profile assignments :"+SrcProfileAndSrcProfileAssignDTO+" of user: "+userId);
		if(SrcProfileAndSrcProfileAssignDTO.getSourceProfiles()!=null)
		{
			SourceProfiles sourceProfiles=SrcProfileAndSrcProfileAssignDTO.getSourceProfiles();
			sourceProfiles.setCreatedBy(userId);
			sourceProfiles.setLastUpdatedBy(userId);
			sourceProfiles.setTenantId(tenantId);
			sourceProfiles.createdDate(ZonedDateTime.now());
			sourceProfiles.setLastUpdatedDate(ZonedDateTime.now());

			sourceProfiles.setStartDate(sourceProfiles.getStartDate());
			if(sourceProfiles.getEndDate()!=null)
				sourceProfiles.setEndDate(sourceProfiles.getEndDate());
			log.info("sourceProfiles :"+sourceProfiles);
			//log.info("SrcProfileAndSrcProfileAssignDTO get id bef"+SrcProfileAndSrcProfileAssignDTO.getSourceProfiles().getId());
			SourceProfiles srcProf = sourceProfilesRepository.save(sourceProfiles);
			//log.info("before Id for display"+srcProf.getId());
			//log.info("sourceProfiles id before Id for display"+sourceProfiles.getId());
			//log.info("SrcProfileAndSrcProfileAssignDTO get id after"+SrcProfileAndSrcProfileAssignDTO.getSourceProfiles().getId());
			if(sourceProfiles.getIdForDisplay() == null)
			{
				String idForDisplay = IDORUtils.computeFrontEndIdentifier(srcProf.getId().toString());
				srcProf.setIdForDisplay(idForDisplay);
				srcProf = sourceProfilesRepository.save(srcProf);
			}
			//sourceProfilesSearchRepository.save(srcProf);
			log.info("sourceProfiles :"+sourceProfiles);
			log.info("sourceProfiles.getId :"+srcProf.getIdForDisplay());
			ErrorReport srcProfileErrorReport=new ErrorReport();
			srcProfileErrorReport.setTaskName("Source Profile Save");
			if(srcProf.getId()!=null)
			{
				sourceProfiles.setEnabledFlag(sourceProfiles.isEnabledFlag());
				srcProfileErrorReport.setDetails(srcProf.getIdForDisplay()+"");
				srcProfileErrorReport.setTaskStatus("Success");
				finalErrorReport.add(srcProfileErrorReport);
				if(SrcProfileAndSrcProfileAssignDTO.getSourceProfileFileAssignments()!=null)
				{
					log.info("srcProf.getId(): "+srcProf.getId());
					List<BigInteger> fileTempIds=sourceProfileFileAssignmentsRepository.fetchTempIdsBySrcProfile(srcProf.getId());
					log.info("templateIds before:"+fileTempIds);
					List<SourceProfileFileAssignments> srcProfileAssignList=SrcProfileAndSrcProfileAssignDTO.getSourceProfileFileAssignments();
					List<SourceProfileFileAssignments> srcProfileAssignFinalList=new ArrayList<SourceProfileFileAssignments>();
					for(SourceProfileFileAssignments srcProfileAssign:srcProfileAssignList)
					{

						//log.info("srcProfileAssign.getLocalDirectoryPath(): "+srcProfileAssign.getLocalDirectoryPath());
						for(SourceProfileFileAssignments srcProfile:srcProfileAssignList)
						{
							if(srcProfileAssign.getId()!=null && srcProfileAssign.getId()!=0)
							{
								if(fileTempIds.size()>0)
								{
									for(int id=0;id<fileTempIds.size();id++)
									{
										log.info("fileTempIds.get(id) :"+fileTempIds.get(id));
										log.info("srcProfileAssign.getTemplateId() :"+srcProfile.getTemplateId());
										if(fileTempIds.get(id).longValue()==srcProfile.getTemplateId())
										{
											log.info("same");
											fileTempIds.remove(fileTempIds.get(id));
										}
									}
								}
							}
						}
						//else
						//{
						log.info("fileTempIds after :"+fileTempIds);

						for(int j=0;j<fileTempIds.size();j++)
						{
							log.info("fileTempIds.get(j).longValue() :"+fileTempIds.get(j).longValue());
							log.info("srcProf.getId() :"+srcProf.getId());
							SourceProfileFileAssignments spfa=sourceProfileFileAssignmentsRepository.findBySourceProfileIdAndTemplateId(srcProf.getId(),fileTempIds.get(j).longValue());
							// RuleGroupDetails ruleGroup=ruleGroupDetailsRepository.findByRuleGroupIdAndRuleId(ruleGrpId.getId(),ruleIdsList.get(j).longValue());
							log.info("spfaId :"+spfa);
							log.info("");
							if(spfa!=null)
								sourceProfileFileAssignmentsRepository.delete(spfa);
						}
						
//						if(srcProfileAssign.getLocalDirectoryPath()==null )
//						{
//							TenantConfig  tenantConfig=tenantConfigRepository.findByTenantIdAndKey(tenantId, "LocalDirPath");
//							if(tenantConfig !=  null)
//							log.info("tenantConfig: "+tenantConfig.toString());
//							if(tenantConfig != null && tenantConfig.getValue() != null)
//							srcProfileAssign.setLocalDirectoryPath(tenantConfig.getValue());
//						}
						srcProfileAssign.setLocalDirectoryPath("HDFS Path");
						srcProfileAssign.setSourceProfileId(srcProf.getId());
						srcProfileAssign.setCreatedBy(userId);
						srcProfileAssign.setLastUpdatedBy(userId);
						srcProfileAssign.setCreatedDate(ZonedDateTime.now());
						srcProfileAssign.setLastUpdatedDate(ZonedDateTime.now());
						srcProfileAssign.setEnabledFlag(true);
						if(srcProfileAssign.getTemplateId() != null && srcProfileAssign.getSourceProfileId() !=null )
						{
							SourceProfileFileAssignments srcProfileFileAssign=sourceProfileFileAssignmentsRepository.save(srcProfileAssign);
							//sourceProfileFileAssignmentsSearchRepository.save(srcProfileFileAssign);
							srcProfileAssignFinalList.add(srcProfileFileAssign);
						}
						else
						{
							log.info("either template id or src prof id are null");
						}


					}
					ErrorReport srcProfileAssignErrorReport=new ErrorReport();
					srcProfileAssignErrorReport.setTaskName("Source Profile File Assignment Save");
					if(srcProfileAssignList.size()==srcProfileAssignFinalList.size())
					{
						srcProfileAssignErrorReport.setTaskStatus("Success");
						finalErrorReport.add(srcProfileAssignErrorReport);
					}
					else
					{
						srcProfileAssignErrorReport.setTaskStatus("Failure");
						finalErrorReport.add(srcProfileAssignErrorReport);
					}
				}
			}
			else
			{
				srcProfileErrorReport.setTaskStatus("Failure");
				finalErrorReport.add(srcProfileErrorReport);
			}
		}
		else
		{
			log.info("source profiles doesnt exist");
		}
		return finalErrorReport;

	}



	/**
	 * Author:Ravali
	 * get Records between start and end date
	 *
	 * @param tenantId
	 * @return List of Records
	 */	
	@GetMapping("/fetchActiveRecords")
	@Timed
	public List getActiveRecords(HttpServletRequest request,@RequestParam String startDate,@RequestParam(value="endDate",required=false) String endDate,@RequestParam String tableName)
	{
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		log.info("Rest Request to get distinct source details for tenant: "+tenantId);
		log.info("startDate :"+startDate);
		log.info("endDate :"+endDate);
		log.info("columnName :"+tableName);LocalDate lStDt=LocalDate.parse(startDate.substring(0, 10));
		List entity;
		LocalDate lEdSt;

		if(endDate!=null)
		{
			lEdSt=LocalDate.parse(endDate.substring(0, 10));

			entity=em.createQuery(" FROM "+tableName+" where tenantId="+tenantId ).getResultList();
		}else{
			entity=em.createQuery(" FROM "+tableName+" where tenantId="+tenantId ).getResultList();
		}
		return entity;
	}



	/**
	 * Author:Ravali
	 * get unassigned source profiles 
	 * @return List of Records
	 */	
	@GetMapping("/UnAssignedSourceProfileList")
	@Timed
	public List<SourceProfiles> getUnassignedFileTemplates(HttpServletRequest request,@RequestBody FileTemplates fileTemplates)
	{
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		log.info("Rest Request to fetch unassigned list for tenantId: "+tenantId +"->"+fileTemplates.getStartDate() +"->"+fileTemplates.getEndDate());
		//LocalDate lEdSt;
		//LocalDate lStDt=fileTemplates.getStartDate();
		ZonedDateTime lEdSt;
		ZonedDateTime lStDt=fileTemplates.getStartDate();
		List<SourceProfiles> activeUnassignedList=new ArrayList<SourceProfiles>();
		if(fileTemplates.getEndDate()!=null)
		{
			lEdSt=fileTemplates.getEndDate();
			log.info("lStDt "+lStDt);
			log.info("lEdSt "+lEdSt);
			activeUnassignedList=sourceProfilesRepository.findByConnectionIdIsNullAndActive(tenantId);
		}
		else
			activeUnassignedList=sourceProfilesRepository.findByConnectionIdIsNull(tenantId);
		return activeUnassignedList;

	}


	/**
	 * Author:Ravali,Shiva
	 * get unassigned source profiles 
	 * @param tenantId
	 * @return List of Records
	 */	
	@GetMapping("/UnAssignedSourceProfiles")
	@Timed
	public List<SourceProfiles> getUnassignedFileTemps(HttpServletRequest request,@RequestParam(required=false) String conId)
	{

		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		log.info("Rest Request to fetch Un-Assigned Source Profiles for tenant: "+tenantId+" conId:"+conId);
		List<SourceProfiles> activeUnassignedList=new ArrayList<SourceProfiles>();
		if(conId == null || conId.isEmpty() || conId == ""  )
		{
			log.info("conId is null");
			activeUnassignedList=sourceProfilesRepository.findByConnectionIdIsNull(tenantId);
			log.info("activeUnassignedList size when null"+activeUnassignedList.size());

		}
		else
		{
			log.info("sourceProfileDTO.getConnectionId(): "+conId);
			SourceConnectionDetails scd = sourceConnectionDetailsRepository.findByIdForDisplayAndTenantId(conId, tenantId);
			activeUnassignedList=sourceProfilesRepository.findByConnection(tenantId,scd.getId());
			log.info("activeUnassignedList size when not null"+activeUnassignedList.size());


		}

		/*if(sourceProfileDTO.getConnectionId() != 0)
		{
			log.info("Connection Id passing "+ sourceProfileDTO.getConnectionId());
			if(sourceProfileDTO.getStartDate() != null)
			{
				String startDateString = sourceProfileDTO.getStartDate();
				Instant strtDtInstance = Instant.parse(startDateString);
		       	LocalDate startDate = LocalDateTime.ofInstant(strtDtInstance, ZoneId.of(ZoneOffset.UTC.getId())).toLocalDate();
		       	log.info("Start Date: "+startDate);
				if(sourceProfileDTO.getEndDate() != null)
				{
					String endDateString = sourceProfileDTO.getEndDate();
			        Instant endDtInstance = Instant.parse(endDateString);
			        LocalDate endDate = LocalDateTime.ofInstant(endDtInstance, ZoneId.of(ZoneOffset.UTC.getId())).toLocalDate(); 
					log.info("End Date: "+endDate);
			        activeUnassignedList = sourceProfilesRepository.fetchByConnectionIdsAndStartDateAndEndDate(sourceProfileDTO.getTenantId(),sourceProfileDTO.getConnectionId(), startDate, endDate);	
				}
				else
				{
					activeUnassignedList = sourceProfilesRepository.fetchByConnectionIdsAndStartDate(sourceProfileDTO.getTenantId(),sourceProfileDTO.getConnectionId(), startDate);
				}
			}
		}
		else
		{
			log.info("Connection Id is null");
			if(sourceProfileDTO.getStartDate() != null)
			{
				String startDateString = sourceProfileDTO.getStartDate();
				Instant strtDtInstance = Instant.parse(startDateString);
		       	LocalDate startDate = LocalDateTime.ofInstant(strtDtInstance, ZoneId.of(ZoneOffset.UTC.getId())).toLocalDate();
		       	log.info("Start Date: "+startDate);
				if(sourceProfileDTO.getEndDate() != null)
				{
					String endDateString = sourceProfileDTO.getEndDate();
			        Instant endDtInstance = Instant.parse(endDateString);
			        LocalDate endDate = LocalDateTime.ofInstant(endDtInstance, ZoneId.of(ZoneOffset.UTC.getId())).toLocalDate();
					log.info("End Date: "+endDate);
			        activeUnassignedList=sourceProfilesRepository.fetchByConnectionIdIdIsNullAndStartDateAndEndDate(sourceProfileDTO.getTenantId(),startDate, endDate);	
				}
				else
				{
					activeUnassignedList=sourceProfilesRepository.fetchByConnectionIdINullAndStartDate(sourceProfileDTO.getTenantId(), startDate);
				}
			}
		}*/
		log.info("No of records fetched: "+ activeUnassignedList.size());
		return activeUnassignedList;
	}
	@GetMapping("/sourceProfileswithDetailInfo")
	@Timed
	public List<SourceProfileDTO> getAllSourceProfilesWithConDetails(HttpServletRequest request,@RequestParam (required=false) String status) {

		List<SourceProfiles> sourceProfilesList =new ArrayList<SourceProfiles>();
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		log.debug("REST request to getAllSourceProfilesWithConDetails"+status+" for tenant: "+tenantId);
		if(status.equalsIgnoreCase("null")||status==null )
		{
			sourceProfilesList = sourceProfilesRepository.findByTenantIdOrderByCreatedDateDesc(tenantId);
		}
		else
		{
			if(status.equalsIgnoreCase("true"))
				sourceProfilesList = sourceProfilesRepository.findByTenantIdAndEnabledFlagOrderByCreatedDateDesc(tenantId,true);
			else if(status.equalsIgnoreCase("false"))
				sourceProfilesList = sourceProfilesRepository.findByTenantIdAndEnabledFlagOrderByCreatedDateDesc(tenantId,false);
		}

		List<SourceProfileDTO> sourceProfilesListDTO = new ArrayList<SourceProfileDTO>();
		for(SourceProfiles sp : sourceProfilesList)
		{
			SourceProfileDTO spDTO = new SourceProfileDTO();
			if(sp.getId() != null)
				spDTO.setId(sp.getId());
			if(sp.getSourceProfileName() != null)
				spDTO.setSourceProfileName(sp.getSourceProfileName());
			if(sp.getConnectionId() != null)
				spDTO.setConnectionId(sp.getConnectionId());
			SourceConnectionDetails scd = new SourceConnectionDetails();
			if(sp.getConnectionId() != null)
			{
				scd = sourceConnectionDetailsRepository.findOne(sp.getConnectionId());
				if(scd != null)
				{
					spDTO.setConnectionName(scd.getName());
					spDTO.setConnectionType(scd.getConnectionType());
				}

			}
			if(sp.getDescription() != null)
				spDTO.setDescription(sp.getDescription());
			if(sp.isEnabledFlag() != null)
				spDTO.setEnabledFlag(sp.isEnabledFlag());
			if(sp.getEndDate() != null)
				spDTO.setEndDate(sp.getEndDate());
			if(sp.getStartDate() != null)
				spDTO.setStartDate(sp.getStartDate());
			if(sp.getTenantId() != null)
				spDTO.setTenantId(sp.getTenantId());
			sourceProfilesListDTO.add(spDTO);
		}
		return sourceProfilesListDTO;
	}

	/**
	 * Author: Kiran, shobha
	 * @param offset
	 * @param sortColName
	 * @param sortOrder
	 * @param limit
	 * @param tenantId
	 * @return
	 * @throws URISyntaxException
	 * @throws SQLException 
	 */
	@GetMapping("/sourceProfilesByTenantIdwithSort")
	@Timed
	public  List<HashMap> getAllSourceProfilesUsingSort(@RequestParam(value = "page" , required = false) Integer offset,@RequestParam(required = false) String sortColName,HttpServletRequest request,
			@RequestParam(required = false) String sortOrder,@RequestParam(value = "per_page", required = false) Integer limit) throws URISyntaxException, SQLException 
			{
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		log.debug("REST request to get SourceProfiles using sort"+sortColName+"=>"+sortOrder+"=> tenant: "+tenantId);

		//		PaginationUtil paginationUtil=new PaginationUtil();
		//		
		//		int maxlmt=paginationUtil.MAX_LIMIT;
		//		int minlmt=paginationUtil.MIN_OFFSET;
		//		log.info("maxlmt: "+maxlmt);
		//		Page<SourceProfiles> page = null;
		//		HttpHeaders headers = null;
		//		
		//		if(sortOrder==null)
		//			sortOrder="Descending";
		//		if(sortColName==null)
		//			sortColName="id";
		//
		//		HashMap map=userJdbcService.getuserInfoFromToken(request);
		//		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		//		
		//		if(limit==null || limit<minlmt){
		//			List<SourceProfiles> srcPrflList = sourceProfilesRepository.findByTenantId(tenantId);
		//			//fileTemplateList=fileTemplatesRepository.findByTenantIdOrderByIdDesc(tenantId);
		//			log.info("SourceProfiles list size :"+srcPrflList.size());
		//			log.info("offset :"+offset+"limit"+limit);
		//			limit=srcPrflList.size();
		//		}
		//		
		//		if(limit == 0 )
		//		{
		//			limit = paginationUtil.DEFAULT_LIMIT;
		//		}
		//		if(offset == null || offset == 0)
		//		{
		//			offset = paginationUtil.DEFAULT_OFFSET;
		//		}
		//		log.info("offset, limit,sortOrder, sortColName"+offset + limit+sortOrder+ sortColName);
		//		if(!sortColName.equalsIgnoreCase("name"))
		//		{
		//			if(limit>maxlmt)
		//			{
		//				log.info("input limit exceeds maxlimit");
		//				page = sourceProfilesRepository.findByTenantId(tenantId,PaginationUtil.generatePageRequestWithSortColumn(offset, limit,sortOrder, sortColName));
		//				headers = PaginationUtil.generatePaginationHttpHeaders2(page, "/api/sourceProfilesByTenantIdwithSort",offset, limit);
		//			}
		//			else if(limit<maxlmt)
		//			{
		//				log.info("input limit is within maxlimit");
		//				page = sourceProfilesRepository.findByTenantId(tenantId,PaginationUtil.generatePageRequestWithSortColumn(offset, limit, sortOrder, sortColName));
		//				headers = PaginationUtil.generatePaginationHttpHeaderss(page, "/api/sourceProfilesByTenantIdwithSort", offset, limit);
		//			}
		//		}
		//		else{
		//			
		//			List<Long> spIds=sourceProfilesRepository.fetchSourcePrfliesIdsUsingConnections(tenantId);
		//			
		//			if(limit>maxlmt)
		//			{
		//				log.info("when connection name is given input limit exceeds maxlimit");
		//				page = sourceProfilesRepository.findByIdIn(spIds,PaginationUtil.generatePageRequest2(offset, limit));
		//				headers = PaginationUtil.generatePaginationHttpHeaders2(page, "/api/sourceProfilesByTenantIdwithSort",offset, limit);
		//			}
		//			else if(limit<maxlmt)
		//			{
		//				log.info("when connection name is given input limit is within maxlimit");
		//				page = sourceProfilesRepository.findByIdIn(spIds,PaginationUtil.generatePageRequest2(offset, limit));
		//				headers = PaginationUtil.generatePaginationHttpHeaderss(page, "/api/sourceProfilesByTenantIdwithSort", offset, limit);
		//			}
		//		}
		Connection conn = null;
		Statement stmt = null;
		ResultSet result = null; 
		List<HashMap> dataMap = new ArrayList<HashMap>();
//		String dbUrl=env.getProperty("spring.datasource.url");
//		String[] parts=dbUrl.split("[\\s@&?$+-]+");
//		String host = parts[0].split("/")[2].split(":")[0];
//		String schemaName=parts[0].split("/")[3];
//		String userName = env.getProperty("spring.datasource.username");
//		String password = env.getProperty("spring.datasource.password");
//		String jdbcDriver = env.getProperty("spring.datasource.jdbcdriver");
		try{
			DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
	 		conn=ds.getConnection();
			stmt = conn.createStatement();
			log.info("Successfully connected to JDBC with schema ");

			if(limit == null || limit == 0 )
			{
				limit = sourceProfilesRepository.findByTenantId(tenantId).size();
			}
			if(offset == null || offset == 0)
			{
				offset=0;
			}
			int offSt = 0;
			offSt = (offset * limit + 1)-1;
			
			String query = "";
			if(sortOrder==null)
				sortOrder="Descending";
			if(sortColName==null)
				sortColName="id";


			//create view 
			String view = "SELECT `pro`.`id` AS `id`, `pro`.`id_for_display` AS `profId`, `pro`.`source_profile_name` AS `sourceProfileName`, `pro`.`end_date` As `endDate`, `pro`.`end_date` >= NOW() As `endDated` ,"
					+ "`pro`.`description` As`profileDescription`, `con`.`id` AS `conId`, `con`.`name` AS `connectionName` , `con`.`connection_type` AS `connectionType` ,"
					+ " ( select count(*) from `t_source_profiles` where tenant_id= "+tenantId +") AS `count` , `pro`.`enabled_flag` As `enabledFlag` FROM  ((`t_source_profiles` `pro`  JOIN `t_source_connection_details` "
							+ "`con`)  )  WHERE  (`pro`.`connection_id` =  `con`.`id` ) and `pro`.tenant_id =  " +tenantId ;
			query = "SELECT * FROM ("+view+") AS profiles_and_cons" +" order by "+sortColName +" "+sortOrder +" limit "+offSt+", "+limit;
			stmt.executeQuery(query);
			//query ="SELECT * from "+schemaName+".`"+"profiles_and_cons"+"` "+" order by "+sortColName +" "+sortOrder +" limit "+offSt+", "+limit;
			result=stmt.executeQuery(query);
			log.info("query"+query);
			ResultSetMetaData rsmd = result.getMetaData();
			int colCount = rsmd.getColumnCount();
			while(result.next()){
				HashMap hm = new HashMap();
				for(int i=1; i<=colCount; i++)
				{
					hm.put(rsmd.getColumnName(i), result.getString(i));
					log.info("rsmd.getColumnName(i)"+rsmd.getColumnName(i)+"result.getString(i) "+result.getString(i));
				}
				int taggedProfCnt = 0;

				SourceProfiles sp = sourceProfilesRepository.findByTenantIdAndIdForDisplay(tenantId,hm.get("profId").toString());
				taggedProfCnt = sourceProfileFileAssignmentsRepository.findBySourceProfileId(sp.getId()).size();
				hm.put("taggedProfCnt",taggedProfCnt);
				//log.info("hm.get(enddate) "+hm.get("endDate") );
//				if(hm.get("endDate") != null)
//				{
//					log.info("hm.get(enddate) "+hm.get("endDate") );
//					LocalDate localDate = LocalDate.parse(hm.get("endDate").toString());
//					log.info("localDate"+localDate);
//					if(localDate != null )
//					{
//						log.info("localDate isnot null"+localDate);
//						if( localDate.isBefore(LocalDate.now()))
//							hm.put("endDated","true");
//					}
//					else
//					{
//						log.info("localDate is null");
//						hm.put("endDated","false");
//					}
//				}
//				else
//					hm.put("endDated","false");

				dataMap.add(hm);
			}
			log.info("Data Size: "+ dataMap.size());
		}
		catch(Exception e)
		{
			log.info("Exceptin while fetching data: "+ e);
		}
		finally
		{
			//drop view
			//stmt.executeUpdate( "DROP view "+schemaName+".`profiles_and_cons`");

			if(result != null)
				result.close();	
			if(stmt != null)
				stmt.close();
			if(conn != null)
				conn.close();
		}

		//		List<SourceProfileDTO> sourceProfilesListDTO = new ArrayList<SourceProfileDTO>();
		//			List<SrcProfileConnectionDetailsDTO> srcProfileConnectionDetailsDTOList = new ArrayList<SrcProfileConnectionDetailsDTO>();
		//		// List<SourceConnectionDetails> srcConcDetailsList=new
		//		// ArrayList<SourceConnectionDetails>();
		//			List<SourceProfiles> srcProfList = new ArrayList<SourceProfiles>();
		//			srcProfList = page.getContent();
		//			int count = 0 ;
		//			count = sourceProfilesRepository.findByTenantId(tenantId).size();
		//			for (SourceProfiles srcProf : srcProfList) {
		//			SrcProfileConnectionDetailsDTO SrcProfConcDto = new SrcProfileConnectionDetailsDTO();
		//			SrcProfConcDto.setId(srcProf.getId());
		//			SrcProfConcDto.setTotalCount(count);
		//			if (srcProf.getSourceProfileName() != null
		//					&& !srcProf.getSourceProfileName().isEmpty())
		//				SrcProfConcDto.setSourceProfileName(srcProf
		//						.getSourceProfileName());
		//			if (srcProf.getDescription() != null
		//					&& !srcProf.getDescription().isEmpty())
		//				SrcProfConcDto.setProfileDescription(srcProf.getDescription());
		//			if (srcProf.getStartDate() != null)
		//				SrcProfConcDto.setStartDate(srcProf.getStartDate());
		//			if (srcProf.getEndDate() != null)
		//				SrcProfConcDto.setEndDate(srcProf.getEndDate());
		//			if(srcProf.getEndDate()!=null && srcProf.getEndDate().isBefore(LocalDate.now()))
		//				SrcProfConcDto.setEndDated(true);
		//			if (srcProf.isEnabledFlag() != null)
		//				SrcProfConcDto.setEnabledFlag(srcProf.isEnabledFlag());
		//			if (srcProf.getConnectionId() != null)
		//				SrcProfConcDto.setConnectionId(srcProf.getConnectionId());
		//			if(srcProf.getCreatedBy() != null)
		//				SrcProfConcDto.setCreatedBy(srcProf.getCreatedBy());
		//			if(srcProf.getCreatedDate() != null)
		//				SrcProfConcDto.setCreatedDate(srcProf.getCreatedDate());
		//			if (srcProf.getConnectionId() != null) {
		//				SourceConnectionDetails srcConcDetails = sourceConnectionDetailsRepository
		//						.findOne(srcProf.getConnectionId());
		//				log.info("srcConcDetails :" + srcConcDetails);
		//				//ConnectionDetails concDTO = new ConnectionDetails();
		//				if (srcConcDetails != null) {
		//					if (srcConcDetails.getName() != null
		//							&& !srcConcDetails.getName().isEmpty())
		//						SrcProfConcDto.setName(srcConcDetails.getName());
		//					else
		//						SrcProfConcDto.setName("");
		//					if (srcConcDetails.getDescription() != null
		//							&& !srcConcDetails.getDescription().isEmpty())
		//						SrcProfConcDto.setConnectionDescription(srcConcDetails
		//								.getDescription());
		//					else
		//						SrcProfConcDto.setConnectionDescription("");
		//					if (srcConcDetails.getProtocol() != null)
		//						SrcProfConcDto.setProtocol(srcConcDetails.getProtocol());
		//					else
		//						SrcProfConcDto.setProtocol("");
		//					if (srcConcDetails.getClientKey() != null
		//							&& !srcConcDetails.getClientKey().isEmpty())
		//						SrcProfConcDto.setClientKey(srcConcDetails.getClientKey());
		//					else
		//						SrcProfConcDto.setClientKey("");
		//					if (srcConcDetails.getClientSecret() != null
		//							&& !srcConcDetails.getClientSecret().isEmpty())
		//						SrcProfConcDto.setClientSecret(srcConcDetails
		//								.getClientSecret());
		//					else
		//						SrcProfConcDto.setClientSecret("");
		//					if (srcConcDetails.getAuthEndpointUrl() != null
		//							&& !srcConcDetails.getAuthEndpointUrl().isEmpty())
		//						SrcProfConcDto.setAuthEndPointUrl(srcConcDetails
		//								.getAuthEndpointUrl());
		//					else
		//						SrcProfConcDto.setAuthEndPointUrl("");
		//					if (srcConcDetails.getTokenEndpointUrl() != null
		//							&& !srcConcDetails.getTokenEndpointUrl().isEmpty())
		//						SrcProfConcDto.setTokenEndPointUrl(srcConcDetails
		//								.getTokenEndpointUrl());
		//					else
		//						SrcProfConcDto.setTokenEndPointUrl("");
		//					if (srcConcDetails.getCallBackUrl() != null
		//							&& !srcConcDetails.getCallBackUrl().isEmpty())
		//						SrcProfConcDto.setCallBackUrl(srcConcDetails.getCallBackUrl());
		//					else
		//						SrcProfConcDto.setCallBackUrl("");
		//					if (srcConcDetails.getHost() != null
		//							&& !srcConcDetails.getHost().isEmpty())
		//						SrcProfConcDto.setHost(srcConcDetails.getHost());
		//					else
		//						SrcProfConcDto.setHost("");
		//					if (srcConcDetails.getPassword() != null
		//							&& !srcConcDetails.getPassword().isEmpty())
		//						SrcProfConcDto.setJhiPassword(srcConcDetails.getPassword());
		//					else
		//						SrcProfConcDto.setJhiPassword("");
		//					if (srcConcDetails.getConnectionType() != null
		//							&& !srcConcDetails.getConnectionType().isEmpty())
		//						SrcProfConcDto.setConnectionType(srcConcDetails
		//								.getConnectionType());
		//					else
		//						SrcProfConcDto.setConnectionType("");
		//					if (srcConcDetails.getAccessToken() != null
		//							&& !srcConcDetails.getAccessToken().isEmpty())
		//						SrcProfConcDto.setAccessToken(srcConcDetails.getAccessToken());
		//					else
		//						SrcProfConcDto.setAccessToken("");
		//					if (srcConcDetails.getPort() != null
		//							&& !srcConcDetails.getPort().isEmpty())
		//						SrcProfConcDto.setPort(srcConcDetails.getPort());
		//					else
		//						SrcProfConcDto.setPort("");
		//					if (srcConcDetails.getUserName() != null
		//							&& !srcConcDetails.getUserName().isEmpty())
		//						SrcProfConcDto.setUserName(srcConcDetails.getUserName());
		//					else
		//						SrcProfConcDto.setUserName("");
		//				}
		//			}
		//			if (SrcProfConcDto != null)
		//				srcProfileConnectionDetailsDTOList.add(SrcProfConcDto);
		//		}
		//Page<SourceProfiles> page = sourceProfilesRepository.findByTenantIdOrderByIdDesc(tenantId,pageable);
		//HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
		//page, "/api/sourceProfilesByTenantIdwithSort");
		return dataMap;
			}
	/**
	 * Author : Shobha
	 * @param request
	 * @param sourceProfilesAndTemplateDTO
	 * @return
	 * 
	 */
	@PostMapping("/sourceProfileAndTemplate")
	@Timed
	public ErrorReport sourceProfileAndTemplate(HttpServletRequest request,@RequestBody SourceProfilesAndTemplateDTO sourceProfilesAndTemplateDTO) throws Exception
	{
		log.info("Rest request to save profile and extract and transform");
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		Long userId=Long.parseLong(map.get("userId").toString());
	
		ErrorReport errorReport = new ErrorReport();
		try{
			errorReport = saveProfile(sourceProfilesAndTemplateDTO,request);
		}
		catch(Exception e)
		{
			
		}
		
		return errorReport;
	}
	
	@Transactional(propagation= Propagation.REQUIRED)
	public ErrorReport saveProfile(SourceProfilesAndTemplateDTO sourceProfilesAndTemplateDTO,HttpServletRequest request) throws Exception
	{
		String errorMessage = ""; 
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		Long userId=Long.parseLong(map.get("userId").toString());
	
		SourceProfiles srcProfiles = new SourceProfiles();
		srcProfiles.setSourceProfileName(sourceProfilesAndTemplateDTO.getSourceProfileName());
		srcProfiles.setStartDate(ZonedDateTime.now());
		srcProfiles.setEnabledFlag(true);
		SourceConnectionDetails scd =new SourceConnectionDetails();
		scd =sourceConnectionDetailsRepository.findByIdForDisplayAndTenantId(sourceProfilesAndTemplateDTO.getConnectionId(), tenantId);
		if(scd != null && scd.getName() != null){
			srcProfiles.setConnectionId(scd.getId());
		}
		srcProfiles.setTenantId(tenantId);
		srcProfiles.setCreatedBy(userId);
		srcProfiles.setCreatedDate(ZonedDateTime.now());
		srcProfiles.setLastUpdatedBy(userId);
		srcProfiles.setLastUpdatedDate(ZonedDateTime.now());
		srcProfiles = sourceProfilesRepository.save(srcProfiles);
		
		String idForDisplay = IDORUtils.computeFrontEndIdentifier(srcProfiles.getId().toString());
		srcProfiles.setIdForDisplay(idForDisplay);
		srcProfiles = sourceProfilesRepository.save(srcProfiles);
		
		FileTemplates ft = new FileTemplates();
		ft = fileTemplatesRepository.findByIdForDisplayAndTenantId(sourceProfilesAndTemplateDTO.getTemplateId(), tenantId);
		
		SourceProfileFileAssignments sPFA = new SourceProfileFileAssignments();
		sPFA.setSourceProfileId(srcProfiles.getId());
		sPFA.setTemplateId(ft.getId());
		sPFA.setFileNameFormat(sourceProfilesAndTemplateDTO.getFileNameFormat());
		sPFA.setFileDescription(sourceProfilesAndTemplateDTO.getFileDescription());
		sPFA.setFileExtension(sourceProfilesAndTemplateDTO.getFileExtension());
		sPFA.setSourceDirectoryPath(sourceProfilesAndTemplateDTO.getSourceDirectoryPath());
		sPFA.setEnabledFlag(true);
		sPFA.setCreatedBy(userId);
		sPFA.setCreatedDate(ZonedDateTime.now());
		sPFA.setLastUpdatedBy(userId);
		sPFA.setLastUpdatedDate(ZonedDateTime.now());
		sPFA = sourceProfileFileAssignmentsRepository.save(sPFA);

		ErrorReport errorReport = new ErrorReport();
		if(sPFA.getId() != null && sPFA.getTemplateId() != null && sPFA.getSourceProfileId() != null){
			if(sourceProfilesAndTemplateDTO.getExtractAndTransform() != null && sourceProfilesAndTemplateDTO.getExtractAndTransform() == true){
				//initiate transformation job
				 HashMap params = new HashMap();
				 params.put("param1",  sPFA.getSourceProfileId());
				 params.put("param3", "true");
				ResponseEntity response= oozieService.jobIntiateForAcctAndRec(tenantId, userId, "DataExtraction", params,"",request);
				if(response != null){
					errorReport.setTaskName("Profile saved and extraction is initiated");
					errorReport.setTaskStatus("SUCCESS");
				}
				else
				{
					errorReport.setTaskName("Profile saved but failed to initiate extraction");
					errorReport.setTaskStatus("SUCCESS");
				}
			}
			else
			{
				errorReport.setTaskName(sourceProfilesAndTemplateDTO.getSourceProfileName() +" saved successfully");
				errorReport.setTaskStatus("SUCCESS");
			}
			
		}
		else
		{
			errorReport.setTaskName(sourceProfilesAndTemplateDTO.getSourceProfileName() +" save failed");
			errorReport.setTaskStatus("FAILURE");
		}
		return errorReport;
	}

}
