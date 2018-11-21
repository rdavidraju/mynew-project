package com.nspl.app.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import com.nspl.app.config.ApplicationContextProvider;
import com.nspl.app.domain.BatchHeader;
import com.nspl.app.domain.DataMaster;
import com.nspl.app.domain.DataStaging;
import com.nspl.app.domain.DataViews;
import com.nspl.app.domain.FileTemplates;
import com.nspl.app.domain.LookUpCode;
import com.nspl.app.domain.SchedulerDetails;
import com.nspl.app.domain.SourceFileInbHistory;
import com.nspl.app.domain.SourceProfileFileAssignments;
import com.nspl.app.domain.SourceProfiles;
import com.nspl.app.repository.BatchHeaderRepository;
import com.nspl.app.repository.FileTemplatesRepository;
import com.nspl.app.repository.LookUpCodeRepository;
import com.nspl.app.repository.SchedulerDetailsRepository;
import com.nspl.app.repository.SourceFileInbHistoryRepository;
import com.nspl.app.repository.SourceProfileFileAssignmentsRepository;
import com.nspl.app.repository.SourceProfilesRepository;
import com.nspl.app.repository.search.BatchHeaderSearchRepository;
import com.nspl.app.service.OozieService;
import com.nspl.app.service.UserJdbcService;
import com.nspl.app.web.rest.dto.BatchDetailsDTO;
import com.nspl.app.web.rest.dto.BatchHeaderDTO;
import com.nspl.app.web.rest.dto.ErrorReport;
import com.nspl.app.web.rest.util.HeaderUtil;
import com.nspl.app.web.rest.util.PaginationUtil;

import org.springframework.data.domain.Sort.Direction;
/**
 * REST controller for managing BatchHeader.
 */
@RestController
@RequestMapping("/api")
public class BatchHeaderResource {

	private final Logger log = LoggerFactory.getLogger(BatchHeaderResource.class);

	private static final String ENTITY_NAME = "batchHeader";

	private final BatchHeaderRepository batchHeaderRepository;

	private final SourceFileInbHistoryRepository sourceFileInbHistoryRepository;

	private final SourceProfilesRepository sourceProfilesRepository;

	private final SourceProfileFileAssignmentsRepository sourceProfileFileAssignmentsRepository;

	private final FileTemplatesRepository fileTemplatesRepository;

	private final BatchHeaderSearchRepository batchHeaderSearchRepository; 

	private final SchedulerDetailsRepository schedulerDetailsRepository;

	@Inject
	DataStagingResource dataStagingResource;

	@Inject
	OozieService oozieService;

	@Inject
	UserJdbcService userJdbcService;

	@Inject
	private Environment env;
	
	@Inject
	LookUpCodeRepository lookUpCodeRepository;

	public BatchHeaderResource(BatchHeaderRepository batchHeaderRepository,
			SourceFileInbHistoryRepository sourceFileInbHistoryRepository,
			SourceProfilesRepository sourceProfilesRepository,
			SourceProfileFileAssignmentsRepository sourceProfileFileAssignmentsRepository,
			FileTemplatesRepository fileTemplatesRepository,
			BatchHeaderSearchRepository batchHeaderSearchRepository,
			SchedulerDetailsRepository schedulerDetailsRepository
			) {
		this.batchHeaderRepository = batchHeaderRepository;
		this.sourceFileInbHistoryRepository = sourceFileInbHistoryRepository;
		this.sourceProfilesRepository = sourceProfilesRepository;
		this.sourceProfileFileAssignmentsRepository = sourceProfileFileAssignmentsRepository;
		this.fileTemplatesRepository = fileTemplatesRepository;
		this.batchHeaderSearchRepository = batchHeaderSearchRepository;
		this.schedulerDetailsRepository = schedulerDetailsRepository;
	}

	/**
	 * POST  /batch-headers : Create a new batchHeader.
	 *
	 * @param batchHeader the batchHeader to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new batchHeader, or with status 400 (Bad Request) if the batchHeader has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/batch-headers")
	@Timed
	public ResponseEntity<BatchHeader> createBatchHeader(@RequestBody BatchHeader batchHeader) throws URISyntaxException {
		log.debug("REST request to save BatchHeader : {}", batchHeader);
		if (batchHeader.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new batchHeader cannot already have an ID")).body(null);
		}
		BatchHeader result = batchHeaderRepository.save(batchHeader);
		batchHeaderSearchRepository.save(result);
		return ResponseEntity.created(new URI("/api/batch-headers/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /batch-headers : Updates an existing batchHeader.
	 *
	 * @param batchHeader the batchHeader to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated batchHeader,
	 * or with status 400 (Bad Request) if the batchHeader is not valid,
	 * or with status 500 (Internal Server Error) if the batchHeader couldn't be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/batch-headers")
	@Timed
	public ResponseEntity<BatchHeader> updateBatchHeader(@RequestBody BatchHeader batchHeader) throws URISyntaxException {
		log.debug("REST request to update BatchHeader : {}", batchHeader);
		if (batchHeader.getId() == null) {
			return createBatchHeader(batchHeader);
		}
		BatchHeader result = batchHeaderRepository.save(batchHeader);
		batchHeaderSearchRepository.save(result);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, batchHeader.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /batch-headers : get all the batchHeaders.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of batchHeaders in body
	 */
	@GetMapping("/batch-headers")
	@Timed
	public ResponseEntity<List<BatchHeader>> getAllBatchHeaders(@ApiParam Pageable pageable) {
		log.debug("REST request to get a page of BatchHeaders");
		Page<BatchHeader> page = batchHeaderRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/batch-headers");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET  /batch-headers/:id : get the "id" batchHeader.
	 *
	 * @param id the id of the batchHeader to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the batchHeader, or with status 404 (Not Found)
	 */
	@GetMapping("/batch-headers/{id}")
	@Timed
	public ResponseEntity<BatchHeader> getBatchHeader(@PathVariable Long id) {
		log.debug("REST request to get BatchHeader : {}", id);
		BatchHeader batchHeader = batchHeaderRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(batchHeader));
	}

	/**
	 * DELETE  /batch-headers/:id : delete the "id" batchHeader.
	 *
	 * @param id the id of the batchHeader to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/batch-headers/{id}")
	@Timed
	public ResponseEntity<Void> deleteBatchHeader(@PathVariable Long id) {
		log.debug("REST request to delete BatchHeader : {}", id);
		batchHeaderRepository.delete(id);
		batchHeaderSearchRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * Author: Shiva
	 * @param tenantId
	 * @param filterValue
	 * @param pageable
	 * @return
	 */
	@GetMapping("/_search/batch-header")
	@Timed
	public List<BatchHeader> searchBatchHeader(HttpServletRequest request, @RequestParam(value="filterValue",required=false) String filterValue,@RequestParam(value = "pageNumber", required=false) Long pageNumber, @RequestParam(value = "pageSize", required=false) Long pageSize) {
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		log.info("Rest api for fetching batch header data usgin full text search for the tenant_id: "+tenantId);
		Long limit = 0L;
		limit = (pageNumber * pageSize + 1)-1;
		log.info("Limit Starting Values : "+ limit);
		log.info("Page Number : "+ pageNumber);

		List<BatchHeader> batchHeaders = batchHeaderRepository.fetchRecordsWithKeyWord(tenantId, filterValue, pageNumber, limit);
		return batchHeaders;
	}

	/**
	 * Author : Shobha
	 * @param sourceProfileId
	 * @return
	 */
	/*@GetMapping("/batchHeader")
	@Timed
	public List<BatchHeaderDTO> getBatchHeaders(@RequestParam(value = "page" , required = false) Integer offset,@RequestParam(value = "per_page", required = false) Integer limit,
			HttpServletResponse response,@RequestParam(required = false) List<Long> sourceProfileId,@RequestParam(required=false) Long srcProfAssgnId, @RequestParam String status, @RequestParam(required = false) String sortColName, @RequestParam(required = false) String sortOrder) throws ClassNotFoundException, SQLException{
		log.debug("Rest request to fetch batch list by pagination with: "+sortColName+" sortingOrder: "+sortOrder);
		List<BatchHeaderDTO> batchHeaderList = new ArrayList<BatchHeaderDTO>();
		List<Object> batchIds = new ArrayList<Object>();
		if(srcProfAssgnId == null )
		{
			batchIds = sourceFileInbHistoryRepository.findDistinctBatchIdsByProfileIdIn(sourceProfileId);

		}
		else
			batchIds = sourceFileInbHistoryRepository.findDistinctBatchIdsBySrcPrfFileAsmtId(srcProfAssgnId);
		//get distinct batch id's of these profile
		log.info("batchIds:"+batchIds);

		if(sortOrder==null)
			sortOrder="Descending";
		if(sortColName==null)
			sortColName="id";


		PaginationUtil paginationUtil=new PaginationUtil();
		int maxlmt=paginationUtil.MAX_LIMIT;
		int minlmt=paginationUtil.MIN_OFFSET;
		int totalCount = 0;
		if(limit==null || limit<minlmt){
			limit =batchIds.size();
		}
		//totalCount = batchIds.size();
		if(limit == 0 )
		{
			limit = paginationUtil.DEFAULT_LIMIT;
		}
		if(offset == null || offset == 0)
		{
			offset = paginationUtil.DEFAULT_OFFSET;
		}

		Page<BatchHeader> page = null;
		if(batchIds .size()>0) {
			List<Long> batchesIds = new ArrayList<Long>();
			for(int i=0;i<batchIds.size();i++)
			{
				if(batchIds.get(i) != null && !batchIds.get(i).toString().isEmpty() && batchIds.get(i) != "")
					batchesIds.add(Long.valueOf(batchIds.get(i).toString()));
			}
			log.info("status: "+status+" limit"+limit+" offset"+offset);
			log.info("batchesIds"+batchesIds);
			Pageable pageable = null;
			//holded 
			List<BatchHeader> filteredBatchHistoryList = new ArrayList<BatchHeader>();
			if(limit>maxlmt)
			{
				//extraction_status//transformation_status
				if( status.equals("001") || status == null || status.equals("")  || status.isEmpty() )//|| status.equals("null"))
				{
					log.info("status is null so fetch this way");
					pageable =	PaginationUtil.generatePageRequest2WithSort(offset, limit,"transformedDatetime","extractedDatetime");
					//pageable.getSort().and( new Sort(new Sort.Order(Direction.DESC, "transformed_datetime"),new Sort.Order(Direction.DESC, "extracted_datetime")));
					page = batchHeaderRepository.findByIdIn(batchesIds,pageable);
					filteredBatchHistoryList = page.getContent();
					List<BatchHeader> unFilteredBatches = new ArrayList<BatchHeader>();
					if(batchesIds.size()>0)
					{
						unFilteredBatches = batchHeaderRepository.findByIdIn(batchesIds);
						totalCount = unFilteredBatches.size();
					}
				}

				else
				{	
					log.info("status is not null so fetch this way");
					pageable =	PaginationUtil.generatePageRequestWithSortColumn(offset, limit,sortOrder,sortColName);
					//pageable.getSort().and( new Sort(new Sort.Order(Direction.DESC, "transformed_datetime"),new Sort.Order(Direction.DESC, "extracted_datetime")));
					if(status.equalsIgnoreCase("hold"))
					{
						page = batchHeaderRepository.findByIdInAndHoldIsTrue(batchesIds,pageable);
						filteredBatchHistoryList = page.getContent();

						List<BatchHeader> unFilteredBatches = new ArrayList<BatchHeader>();
						if(batchesIds.size()>0)
						{
							unFilteredBatches = batchHeaderRepository.findByIdInAndHoldIsTrue(batchesIds);
							totalCount = unFilteredBatches.size();
						}
					}
					else if(status.equalsIgnoreCase("scheduled"))
					{
						List<String> scheduled = new ArrayList<String>();
						scheduled.add("Manual");
						page = batchHeaderRepository.findByIdInAndTypeNotIn(batchesIds,pageable,scheduled);
						filteredBatchHistoryList = page.getContent();

						List<BatchHeader> unFilteredBatches = new ArrayList<BatchHeader>();
						if(batchesIds.size()>0)
						{
							unFilteredBatches = batchHeaderRepository.findByIdInAndTypeNotIn(batchesIds,scheduled);
							totalCount = unFilteredBatches.size();
						}
					}
					else if(status.equalsIgnoreCase("manual"))
					{
						page = batchHeaderRepository.findByIdInAndTypeContains(batchesIds,pageable,"Manual");
						filteredBatchHistoryList = page.getContent();

						List<BatchHeader> unFilteredBatches = new ArrayList<BatchHeader>();
						if(batchesIds.size()>0)
						{
							unFilteredBatches = batchHeaderRepository.findByIdInAndTypeContains(batchesIds,"Manual");
							totalCount = unFilteredBatches.size();
						}
					}
					else {

						List<BatchHeader> unFilteredBatches = new ArrayList<BatchHeader>();
						if(batchesIds.size()>0)
						{
							unFilteredBatches = batchHeaderRepository.fetchCountByStatus(batchesIds,status,status);
							totalCount = unFilteredBatches.size();
						}
						page = batchHeaderRepository.findByIdInAndExtractionStatusContains(batchesIds,pageable,status);
						if(page.getContent().size() > 0)
						{
							filteredBatchHistoryList = page.getContent();
						}
						page = batchHeaderRepository.findByIdInAndTransformationStatusContains(batchesIds,pageable,status);
						if(page.getContent().size() > 0)
						{
							filteredBatchHistoryList.addAll(page.getContent());
						}
					}

				}
			}
			else
			{
				log.info("limit is okay, its not greater");
				if(  status.equals("001") || status == null || status.equals("") || status.isEmpty())
				{
					log.info("status is null so fetch this way1");
					pageable =	PaginationUtil.generatePageRequest2WithSort(offset, limit,"transformedDatetime","extractedDatetime");
					//pageable.getSort().and( new Sort(new Sort.Order(Direction.DESC, "transformed_datetime"),new Sort.Order(Direction.DESC, "extracted_datetime")));
					page = batchHeaderRepository.findByIdIn(batchesIds,pageable);
					filteredBatchHistoryList = page.getContent();

					List<BatchHeader> unFilteredBatches = new ArrayList<BatchHeader>();
					if(batchesIds.size()>0)
					{
						unFilteredBatches = batchHeaderRepository.findByIdIn(batchesIds);
						totalCount = unFilteredBatches.size();
					}
				}
				else
				{
					log.info("status is not null so fetch this way");
					pageable =	PaginationUtil.generatePageRequestWithSortColumn(offset, limit, sortOrder, sortColName);
					//pageable.getSort().and( new Sort(new Sort.Order(Direction.DESC, "transformed_datetime"),new Sort.Order(Direction.DESC, "extracted_datetime")));
					page = batchHeaderRepository.findByIdInAndExtractionStatusContainsOrTransformationStatusContains(batchesIds,pageable,status,status);
					log.info("pageable: "+pageable);

					if(status.equalsIgnoreCase("hold"))
					{
						log.info("fetch by hold");
						page = batchHeaderRepository.findByIdInAndHoldIsTrue(batchesIds,pageable);
						filteredBatchHistoryList = page.getContent();

						List<BatchHeader> unFilteredBatches = new ArrayList<BatchHeader>();
						if(batchesIds.size()>0)
						{
							unFilteredBatches = batchHeaderRepository.findByIdInAndHoldIsTrue(batchesIds);
							totalCount = unFilteredBatches.size();
						}
					}
					else if(status.equalsIgnoreCase("scheduled"))
					{
						log.info("fetch by scheduled");
						List<String> scheduled = new ArrayList<String>();
						scheduled.add("Manual");
						page = batchHeaderRepository.findByIdInAndTypeNotIn(batchesIds,pageable,scheduled);
						filteredBatchHistoryList = page.getContent();

						List<BatchHeader> unFilteredBatches = new ArrayList<BatchHeader>();
						if(batchesIds.size()>0)
						{
							unFilteredBatches = batchHeaderRepository.findByIdInAndTypeNotIn(batchesIds,scheduled);
							totalCount = unFilteredBatches.size();
						}
					}
					else if(status.equalsIgnoreCase("manual"))
					{
						log.info("fetch by manual");
						page = batchHeaderRepository.findByIdInAndTypeContains(batchesIds,pageable,"Manual");
						filteredBatchHistoryList = page.getContent();

						List<BatchHeader> unFilteredBatches = new ArrayList<BatchHeader>();
						if(batchesIds.size()>0)
						{
							unFilteredBatches = batchHeaderRepository.findByIdInAndTypeContains(batchesIds,"Manual");
							totalCount = unFilteredBatches.size();
						}
					}
					else {
						log.info("fetch by ..."+status);

						List<BatchHeader> unFilteredBatches = new ArrayList<BatchHeader>();
						if(batchesIds.size()>0)
						{
							unFilteredBatches = batchHeaderRepository.fetchCountByStatus(batchesIds,status,status);
							totalCount = unFilteredBatches.size();

						}
						log.info("batchesIds: "+batchesIds+" pageable "+pageable+" status: "+status);
						page = batchHeaderRepository.findByIdInAndTransformationStatusContains(batchesIds,pageable,status);
						if(page.getContent().size() > 0)
						{
							filteredBatchHistoryList = page.getContent();
						}
						page = batchHeaderRepository.findByIdInAndExtractionStatusContains(batchesIds,pageable,status);
						if(page.getContent().size() > 0)
						{
							filteredBatchHistoryList.addAll(page.getContent());
						}
					}
				}
			}
			log.info("totalCount:"+totalCount);
			Sort sort = Sort.by(
    		  Sort.Order.("numberOfHands"));
    		return batchHeaderRepository.findByIdInAndExtractionStatusContainsOrTransformationStatusContainsOrderByExtractedDatetimeTransformedDatetimeDesc(sort,batchesIds,PaginationUtil.generatePageRequest(offset, limit),status,status);
			//List<BatchHeader> pageContentData =new ArrayList<BatchHeader>();
			//log.info("filteredBatchHistoryList"+filteredBatchHistoryList);
			if(filteredBatchHistoryList != null && filteredBatchHistoryList.size()>0)
			{
				pageContentData =page.getContent();
    		List<Long> batchIdsFiltered = new ArrayList<Long>();
    		for(int i = 0;i<pageContentData.size();i++)
        	{
    			batchIdsFiltered.add(pageContentData.get(i).getId());
        	}
				//pageContentData = batchHeaderRepository.orderBatchesByExtractedAndTransformedTimeStamp(batchIdsFiltered);
				for(int i = 0;i<filteredBatchHistoryList.size();i++)
				{
					//	BigInteger batchId = BigInteger.valueOf(Long.valueOf(batchIds.get(i).toString()));
					BatchHeader batchHeader = new BatchHeader();
					batchHeader =filteredBatchHistoryList.get(i);
					if(batchHeader != null)
					{
						BatchHeaderDTO batchDTO = new BatchHeaderDTO();


						if(batchHeader != null)
						{
							batchDTO.setTotalCount(totalCount);
							if(batchHeader.getId() != null)
								batchDTO.setId(batchHeader.getId());
							if(batchHeader.getExtRef() != null)
								batchDTO.setReference(batchHeader.getExtRef());
							if(batchHeader.getJobRef() != null)
								batchDTO.setJobRef(batchHeader.getJobRef());
							if(batchHeader.getBatchName() != null)
								batchDTO.setBatchName(batchHeader.getBatchName());
							if(batchHeader.getType() == null || batchHeader.getType() == "" || batchHeader.getType().isEmpty())
								batchDTO.setBatchType("Scheduled");        			
							else
								batchDTO.setBatchType(batchHeader.getType());
							if(batchHeader.getExtractionStatus() != null)
								batchDTO.setExtractionStatus(batchHeader.getExtractionStatus());
							if(batchHeader.getExtractedDatetime() != null)
								batchDTO.setExtractedDateTime(batchHeader.getExtractedDatetime());
							if(batchHeader.getTransformationStatus() != null)
								batchDTO.setTransformationStatus(batchHeader.getTransformationStatus());
							if(batchHeader.getTransformedDatetime() != null)
								batchDTO.setTransformedDateTime(batchHeader.getTransformedDatetime());

							if(batchHeader.getJobRef() != null)
							{
								SchedulerDetails schedulesByJobRef = new SchedulerDetails();
								schedulesByJobRef = schedulerDetailsRepository.findByOozieJobIdAndTenantID(batchHeader.getTenantId(),batchHeader.getJobRef());
								if(schedulesByJobRef != null && schedulesByJobRef.getFrequency() != null && !schedulesByJobRef.getFrequency().equalsIgnoreCase("ONDEMAND"))
								{
									HashMap lastRunMap=new HashMap();
									lastRunMap = oozieService.getNextRunByOozieJobId(schedulesByJobRef.getOozieJobId());
									if(lastRunMap.size()>0 )
									{
										if( lastRunMap.get("nextMatdTime") != null)
										{
											batchDTO.setNextSchedule( lastRunMap.get("nextMatdTime").toString());
										}
										else
										{
											batchDTO.setNextSchedule("No Schedule");
										}
									}

								}
							}
								if(batchHeader.getNextSchedule() != null)
        				batchDTO.setNextSchedule(batchHeader.getNextSchedule());
							batchDTO.setSelected(false);
							if(batchHeader.getHold() != null)
								batchDTO.setHold(batchHeader.getHold());
							if(batchHeader.getHolReason() != null)
								batchDTO.setHoldReason(batchHeader.getHolReason());
							batchHeaderList.add(batchDTO);
						}
					}
				}
			}
			else
			{
				log.info("filteredBatchHistoryList is null and content doesnot exist");
			}
		}

		return batchHeaderList;
	}*/
	/**
	 * Author : Shobha
	 * @param batchId
	 * @return
	 */
	/*@GetMapping("/batchDetailsByBatchId")
	@Timed
	public List<BatchDetailsDTO> getBatchDetailsByBatchId(@RequestParam(value = "page" , required = false) Integer offset,@RequestParam(required = false) String sortColName, @RequestParam(required = false) String sortOrder,
			@RequestParam(value = "per_page", required = false) Integer limit,HttpServletResponse response,@RequestParam Long batchId) {
		log.info("Rest request to get batch details by batch id"+batchId +"and sortColName "+sortColName+" and sortOrder: "+sortOrder);
		List<ErrorReport> errorReportsList = new ArrayList<ErrorReport>();
		List<BatchDetailsDTO> batchDetailsList  = new ArrayList<BatchDetailsDTO>();
		List<SourceFileInbHistory> srcFileInbHistList = new ArrayList<SourceFileInbHistory>();
		ErrorReport errorReport = new ErrorReport();
		errorReport.setTaskName("Batch List by batch id");

		if(sortOrder==null)
			sortOrder="Descending";
		if(sortColName==null)
			sortColName="id";

		if(batchId != null)
		{
			BatchHeader batchHeader = new BatchHeader();
			batchHeader = batchHeaderRepository.findOne(batchId);
			srcFileInbHistList = sourceFileInbHistoryRepository.findByBatchId(batchId);
			PaginationUtil paginationUtil=new PaginationUtil();
			int maxlmt=paginationUtil.MAX_LIMIT;
			int minlmt=paginationUtil.MIN_OFFSET;
			int totalCount = 0;

			if(limit==null || limit<minlmt){
				limit =srcFileInbHistList.size();
			}
			totalCount = srcFileInbHistList.size();
			if(limit == 0 )
			{
				limit = paginationUtil.DEFAULT_LIMIT;
			}
			if(offset == null || offset == 0)
			{
				offset = paginationUtil.DEFAULT_OFFSET;
			}
			Page<SourceFileInbHistory> page = null;

			log.info("limit"+limit+"=>offset"+offset);
			if(srcFileInbHistList.size()>0)
			{
				if(limit>maxlmt)
				{
					page = sourceFileInbHistoryRepository.findByBatchId(batchId,PaginationUtil.generatePageRequestWithSortColumn(offset, limit, sortOrder, sortColName));
				}
				else
				{
					page = sourceFileInbHistoryRepository.findByBatchId(batchId,PaginationUtil.generatePageRequestWithSortColumn(offset, limit, sortOrder, sortColName));
				}
				if(page != null && page.getContent() != null && page.getContent().size()>0)
				{
					srcFileInbHistList = page.getContent();
					for(SourceFileInbHistory srcInbHistory : srcFileInbHistList)
					{
						BatchDetailsDTO batchDetailsDto = new BatchDetailsDTO();
						batchDetailsDto.setTotalCount(totalCount);
						if(srcInbHistory.getId() != null)
							batchDetailsDto.setSrcInbHistId(srcInbHistory.getId());
						if(batchHeader.getBatchName() != null)
							batchDetailsDto.setBatchHeaderName(batchHeader.getBatchName());
						if(srcInbHistory.getFileSize() != null)
							batchDetailsDto.setFileSize(srcInbHistory.getFileSize());
						if(srcInbHistory.getHold() != null)
							batchDetailsDto.setHold(srcInbHistory.getHold());
						if(srcInbHistory.getFileTransformedDate() != null)
							batchDetailsDto.setFileTransformedDate(srcInbHistory.getFileTransformedDate());
						if(srcInbHistory.getFileName() != null)
							batchDetailsDto.setFileName(srcInbHistory.getFileName());
						SourceProfiles srcProf = new SourceProfiles();
						if(srcInbHistory.getProfileId() != null)
							srcProf = sourceProfilesRepository.findOne(srcInbHistory.getProfileId());

						if(srcProf != null && srcProf.getSourceProfileName() != null)
							batchDetailsDto.setProfileName(srcProf.getSourceProfileName());
						if(srcInbHistory.getStatus() != null)
							batchDetailsDto.setStatus(srcInbHistory.getStatus());

						SourceProfileFileAssignments spa = new SourceProfileFileAssignments();
						if(srcInbHistory.getSrcPrfFileAsmtId() != null)
						{
							log.info("Long.valueOf(srcInbHistory.getSrcPrfFileAsmtId()):"+srcInbHistory.getSrcPrfFileAsmtId());
							spa = sourceProfileFileAssignmentsRepository.findOne(Long.valueOf(srcInbHistory.getSrcPrfFileAsmtId()));
							log.info("spa is null");
							if(spa != null && spa.getTemplateId() != null)	
							{
								batchDetailsDto.setSrcProfAssId(srcInbHistory.getSrcPrfFileAsmtId());
								log.info("spa.getTemplateId():"+spa.getTemplateId());
								FileTemplates filetemplate = new FileTemplates();
								filetemplate = fileTemplatesRepository.findOne(Long.valueOf(spa.getTemplateId()));
								log.info("filetemplate.getTemplateName()"+filetemplate.getTemplateName());
								batchDetailsDto.setTemplateName(filetemplate.getTemplateName());
								if( spa.getTemplateId() != null)
									batchDetailsDto.setTemplateId( spa.getTemplateId() );

							}
							else

								log.info("spa is null");
						}


						batchDetailsList.add(batchDetailsDto);
					}
				}
			}
		}


		return batchDetailsList;
	}*/
	/**
	 * Author : shobha
	 * @param srcFileInbHistId
	 * @return
	 */
	@PutMapping("/holdBatchDetail")
	@Timed
	public String holdBatchDetail(@RequestParam Long srcFileInbHistId,@RequestParam String reason) {
		SourceFileInbHistory srcFileInbHist = new SourceFileInbHistory();
		srcFileInbHist = sourceFileInbHistoryRepository.findOne(srcFileInbHistId);
		if(srcFileInbHist != null )
		{
			srcFileInbHist.setHold(true);
			srcFileInbHist.setHoldReason(reason);
			sourceFileInbHistoryRepository.save(srcFileInbHist);
			return "success";
		}
		return "failed";
	}
	/**
	 *  Author : shobha
	 * @param srcFileInbHistId
	 * @return
	 */
	@PutMapping("/releaseHoldForBatchDetail")
	@Timed
	public String releaseHoldForBatchDetail(@RequestParam Long srcFileInbHistId,@RequestParam String reason) {
		SourceFileInbHistory srcFileInbHist = new SourceFileInbHistory();
		srcFileInbHist = sourceFileInbHistoryRepository.findOne(srcFileInbHistId);
		if(srcFileInbHist != null )
		{
			srcFileInbHist.setHold(false);
			srcFileInbHist.setHoldReason(reason);
			sourceFileInbHistoryRepository.save(srcFileInbHist);
			return "success";
		}
		return "failed";
	}
	/**
	 *  Author : shobha
	 * @param batchId
	 * @return
	 */
	@PutMapping("/holdBatch")
	@Timed
	public String holdBatch(@RequestParam Long batchId,@RequestParam String reason) {
		BatchHeader batchHeader  = new BatchHeader();
		log.info("batchId to hold:"+batchId);
		batchHeader = batchHeaderRepository.findOne(batchId);
		if(batchHeader != null )
		{
			batchHeader.setHold(true);
			batchHeader.setHolReason(reason);
			BatchHeader bh = batchHeaderRepository.save(batchHeader);
			//batchHeaderSearchRepository.save(bh);
			return "success";
		}
		return "failed";
	}
	/**
	 *  Author : shobha
	 * @param batchId
	 * @return
	 */
	@PutMapping("/releaseHoldForBatch")
	@Timed
	public String releaseHoldForBatch(@RequestParam Long batchId,@RequestParam String reason) {
		BatchHeader batchHeader  = new BatchHeader();
		log.info("batchId to release hold:"+batchId);
		batchHeader = batchHeaderRepository.findOne(batchId);
		if(batchHeader != null )
		{
			batchHeader.setHold(false);
			batchHeader.setHolReason(reason);
			BatchHeader bh = batchHeaderRepository.save(batchHeader);
			//batchHeaderSearchRepository.save(bh);
			return "success";
		}
		return "failed";
	}
	@GetMapping("/fetchDistinctBatchStatus")
	@Timed
	public List<HashMap<String,String>> fetchDistinctBatchStatus() {
		List<HashMap<String,String> > statusList = new ArrayList<HashMap<String,String>>();
		return statusList;
	}

	/**
	 * author kiran
	 * @param sourceProfileId
	 * @param srcProfAssgnId
	 * @param status
	 * @param searchWord
	 * @return
	 */
	@GetMapping("/textSearchBatchHeader")
	@Timed
	public List<BatchHeader> textSearchBatchHeader(@RequestParam(required = false) List<Long> sourceProfileId,@RequestParam(required=false) Long srcProfAssgnId, @RequestParam(required=false) String status, @RequestParam String searchWord)
	{
		log.info("Api to search in BatchHeader, sourceProfileId: "+sourceProfileId+" and searchKeyword: "+searchWord);

		String columnString = "";

		columnString="COALESCE(batch_name, ''), COALESCE(extraction_status, ''), COALESCE(extracted_datetime, ''), COALESCE(transformation_status, ''), COALESCE(transformed_datetime, ''), COALESCE(jhi_type, ''), COALESCE(ext_ref, '') ";

		columnString = " AND CONCAT("+columnString +") LIKE '%"+searchWord+"%'";

		List<Long> batchIdsList = new ArrayList<Long>();
		if(srcProfAssgnId == null )
		{
			batchIdsList = sourceFileInbHistoryRepository.fetchDistinctBatchIdsByProfileIdIn(sourceProfileId);
		}
		else
			batchIdsList = sourceFileInbHistoryRepository.fetchDistinctBatchIdsBySrcPrfFileAsmtId(srcProfAssgnId);

		String batchIds = batchIdsList.toString();

		batchIds = batchIds.substring(1, batchIds.length() - 1);


		if(batchIdsList.size()>0)
		{
			if(status==null || status.equals(""))
			{
				columnString = "Select * from t_batch_header where id in ("+batchIds+")"+ columnString;
			}
			else if(status.equalsIgnoreCase("hold"))
			{
				columnString = "Select * from t_batch_header where id in ("+batchIds+") and hold is true "+ columnString;
			}
			else if(status.equalsIgnoreCase("scheduled"))
			{
				String scheduled="Manual";
				columnString = "Select * from t_batch_header where id in ("+batchIds+") and jhi_type not like '"+scheduled+"'  "+ columnString;
			}
			else if(status.equalsIgnoreCase("manual"))
			{
				columnString = "Select * from t_batch_header where id in ("+batchIds+") and jhi_type like '"+status+"'"+ columnString;
			}
			else {

				columnString = "Select * from t_batch_header where id in ("+batchIds+") and extraction_status like '%"+status+"%' or transformation_status like '%"+status+"%'"+ columnString;
			}
		}
		else{
			log.info("No Batch Headers found for the given profile or assignment");
		}

		log.info("columnString: "+columnString);
		Connection dbconn = dataStagingResource.getDbConnection();
		List<BatchHeader> searchResults=getBatchHeaderRecords(dbconn, columnString);
		//log.info("searchResults:- "+searchResults);
		
		try {
			dbconn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return searchResults;
	}


	public List<BatchHeader> getBatchHeaderRecords(Connection conn,String query)
	{
		List<BatchHeader> bHeaderRecords = new ArrayList<BatchHeader>();
		Statement stmt = null;
		ResultSet result = null;
		ResultSet rs = null;

		try{
			stmt = conn.createStatement();
			result=stmt.executeQuery(query);
			rs=stmt.getResultSet();

			while(rs.next())
			{
				BatchHeader bHeader = new BatchHeader();
				bHeader.setBatchName(rs.getString("batch_name"));
				bHeader.setExtractionStatus(rs.getString("extraction_status"));
				//				bHeader.setExtractedDatetime(rs.getTimestamp("extracted_datetime"));
				bHeader.setTransformationStatus(rs.getString("transformation_status"));
				//				bHeader.setTransformedDatetime(rs.getTimestamp("transformed_datetime"));
				bHeader.setType(rs.getString("jhi_type"));
				bHeader.setExtRef(rs.getString("ext_ref"));
				bHeaderRecords.add(bHeader);
			}
		}
		catch(SQLException s)
		{
			log.info("Connection Failed while gettig BatchHeader Records details "+s);
		}
		finally{
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				result.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return bHeaderRecords;
	}


	/**
	 * author kiran
	 * @param batchId
	 * @param searchWord
	 * @return
	 */
	@GetMapping("/textSearchBatchHeaderDetails")
	@Timed
	public List<BatchDetailsDTO> textSearchBatchHeaderDetails(@RequestParam(required=false) Long batchId,  @RequestParam String searchWord)
	{
		log.info("Api to search in BatchHeader Details, batchId: "+batchId);

		String columnString = "";
		columnString="COALESCE(file_name, ''), COALESCE(file_transformed_date, ''), COALESCE(sfi.status, ''), COALESCE(template_name, '') ";
		columnString = " AND CONCAT("+columnString +") LIKE '%"+searchWord+"%'";
		//columnString = "Select * from t_source_file_inb_history where batch_id = "+batchId+""+ columnString;

		/*" Select  sfi.file_name as fileName, sfi.file_transformed_date, ft.template_name,   sfi.status from  "
		+ " ((t_source_file_inb_history as sfi join  t_source_profile_file_assignments as sp) join  "
		+ " t_file_templates as ft ) WHERE ((sfi.src_prf_file_asmt_id = sp.id) AND (sp.template_id = ft.id) AND (sfi.batch_id=4))  ;"
		 */

		columnString="Select sfi.file_name as file_name, sfi.file_transformed_date as file_transformed_date, ft.template_name as template_name, sfi.status as status from "
				+ "t_source_file_inb_history as sfi, t_source_profile_file_assignments as sp, t_file_templates as ft "
				+ "where sfi.src_prf_file_asmt_id = sp.id and sp.template_id = ft.id and sfi.batch_id="+batchId+""+ columnString;


		log.info("columnString in Batch Details:- "+columnString);

		Connection dbConn = dataStagingResource.getDbConnection();

		List<BatchDetailsDTO> searchResult=getBatchHeaderDetailRecords(dbConn, columnString);
		
		try {
			dbConn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return searchResult;
	}



	public List<BatchDetailsDTO> getBatchHeaderDetailRecords(Connection conn,String query)
	{
		List<BatchDetailsDTO> bHDetailsRecords = new ArrayList<BatchDetailsDTO>();
		Statement stmt = null;
		ResultSet result = null;
		ResultSet rs = null;

		try{
			stmt = conn.createStatement();
			result=stmt.executeQuery(query);
			rs=stmt.getResultSet();

			while(rs.next())
			{
				BatchDetailsDTO bHDetail = new BatchDetailsDTO();
				bHDetail.setFileName(rs.getString("file_name"));
				//				bHDetail.setFileTransformedDate(rs.getTimestamp("file_transformed_date"));
				bHDetail.setTemplateName(rs.getString("template_name"));
				bHDetail.setStatus(rs.getString("status"));

				bHDetailsRecords.add(bHDetail);
			}
		}
		catch(SQLException s)
		{
			log.info("Connection Failed while gettig BatchHeader Records details "+s);
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				result.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return bHDetailsRecords;
	}



	public List<SourceFileInbHistory> getBatchHeaderDetailRecords1(Connection conn,String query)
	{
		List<SourceFileInbHistory> bHDetailsRecords = new ArrayList<SourceFileInbHistory>();
		Statement stmt = null;
		ResultSet result = null;
		ResultSet rs =null;

		try{
			stmt = conn.createStatement();
			result=stmt.executeQuery(query);
			rs=stmt.getResultSet();

			while(rs.next())
			{
				SourceFileInbHistory bHDetail = new SourceFileInbHistory();
				bHDetail.setFileName(rs.getString("file_name"));
				//				bHDetail.setFileTransformedDate(rs.getTimestamp("file_transformed_date"));
				bHDetail.setStatus(rs.getString("status"));

				bHDetailsRecords.add(bHDetail);
			}
		}
		catch(SQLException s)
		{
			log.info("Connection Failed while gettig BatchHeader Records details "+s);
		}
		finally{
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				result.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				stmt.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return bHDetailsRecords;
	}


	/**
	 * Author Kiran
	 * @param offset
	 * @param sortColName
	 * @param sortOrder
	 * @param limit
	 * @param response
	 * @param batchId
	 * @return
	 * @throws SQLException
	 */
	@GetMapping("/batchDetailsByBatchId")
	@Timed
	public List<HashMap> getBatchDetailsByBatchId(HttpServletRequest request,@RequestParam(value = "page" , required = false) Integer offset,@RequestParam(required = false) String sortColName, 
			@RequestParam(required = false) String sortOrder,@RequestParam(value = "per_page", required = false) Integer limit,@RequestParam Long batchId) throws SQLException 
			{
		log.info("Rest request to get batch details by batch id"+batchId +"and sortColName "+sortColName+" and sortOrder: "+sortOrder);

		Connection conn = null;
		Statement stmt = null;
		ResultSet result = null; 
		String schemaName=null;
		List<HashMap> dataMap = new ArrayList<HashMap>();
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long userId=Long.parseLong(map.get("userId").toString());
		Long tenantId=Long.parseLong(map.get("tenantId").toString()); 
		try
		{
//			String dbUrl=env.getProperty("spring.datasource.url");
//			String[] parts=dbUrl.split("[\\s@&?$+-]+");
//			String host = parts[0].split("/")[2].split(":")[0];
//			schemaName=parts[0].split("/")[3];
//			String userName = env.getProperty("spring.datasource.username");
//			String password = env.getProperty("spring.datasource.password");
//			String jdbcDriver = env.getProperty("spring.datasource.jdbcdriver");
//			Class.forName(jdbcDriver);
//			conn = DriverManager.getConnection(dbUrl, userName, password);
			DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
	 		conn=ds.getConnection();
			stmt = conn.createStatement();
			log.info("Successfully connected to JDBC with schema "+schemaName);

			//HashMap map=userJdbcService.getuserInfoFromToken(request);
			//Long tenantId=Long.parseLong(map.get("tenantId").toString());
			if(limit == null || limit == 0 )
			{
				limit = sourceFileInbHistoryRepository.findByBatchId(batchId).size();
			}
			if(offset == null )
			{
				offset=0;
			}
			if(sortOrder==null)
				sortOrder="desc";
			if(sortColName==null)
				sortColName="srcInbHistId";
			int offSt = 0;
			offSt = (offset * limit + 1)-1;
			String query = "";


		/*	String view="CREATE OR REPLACE VIEW `batch_details_by_batch_id` As Select "
					+ "`sfih`.`id` As `srcInbHistId`, "
					+ "`bh`.`batch_name` AS `batchHeaderName`, "
					+ "`sp`.`source_profile_name` AS `profileName`, "
					+ "`ft`.`id` AS `templateId`, "
					+ "`ft`.`id_for_display` AS `idForDisplay`, "
					+ "`ft`.`template_name` AS `templateName`, "
					+ "`sfih`.`file_size` AS `fileSize`, "
					+ "`sfih`.`status` AS `status`, "
					+ "`sfih`.`hold` AS `hold`, "
					+ "`sfih`.`file_name` AS `fileName`, "
					+ "`sfih`.`src_prf_file_asmt_id` AS `srcProfAssId`, "
					+ "`sfih`.`file_transformed_date` AS `fileTransformedDate`, "
					+ "(select count(*) from `t_source_file_inb_history` where batch_id= "+batchId+") AS `totalCount` "
					//+ ", (select count(*) from `t_data_staging` where src_file_inb_id = `sfih`.`id`) AS `stagingCount`,"
					//+ "(select count(*) from `t_data_master` where src_file_inb_id = `sfih`.`id`) AS `masterCount`"
					+ "FROM ("
					+ "(`t_source_file_inb_history` `sfih`  JOIN "
					//+ "`t_data_staging` `DS` JOIN"
					//+ "`t_data_master` `DM` JOIN"
					+ "`t_file_templates` `ft` JOIN "
					+ "`t_source_profile_file_assignments` `spfa` JOIN "
					+ "`t_batch_header` `bh` JOIN "
					+ "`t_source_profiles` `sp`))"
					+ "WHERE `bh`.`id`=`sfih`.`batch_id` and `sp`.`id`=`sfih`.`profile_id` and `sfih`.`src_prf_file_asmt_id` = `spfa`.`id` and `spfa`.`template_id` = `ft`.`id` and `sfih`.`batch_id`="+batchId
					+ "";
*/
			String view ="SELECT `sfih`.`id`                                 AS `srcInbHistId`,"
					     +"  `bh`.`batch_name`                           AS `batchHeaderName`, "
					     +"  `sp`.`source_profile_name`                  AS `profileName`, "
					      +" `ft`.`id`                                   AS `templateId`," 
					      +" `ft`.`id_for_display`                       AS `idForDisplay`," 
					      +" `ft`.`template_name`                        AS `templateName`, "
					      +"`sfih`.`file_size`                          AS `fileSize`, "
					      +" `sfih`.`status`                             AS `status`, "
					      +" `sfih`.`hold`                               AS `hold`, "
					      +" `sfih`.`hold_reason`                               AS `holdReason`, "
					      +" `sfih`.`file_name`                          AS `fileName`, "
					      +" `sfih`.`src_prf_file_asmt_id`               AS `srcProfAssId`, " 
					       +" `sfih`.`file_transformed_date`              AS `fileTransformedDate`, "
						  +" (select count(*) master_count from `t_data_master` where src_file_inb_id= `sfih`.`id`) master_count, "
					      +" (select count(*) staging_count from `t_data_staging` where src_file_inb_id= `sfih`.`id`) staging_count , "
					      +" (select count(*) fail_lines_count from `t_data_staging` where src_file_inb_id= `sfih`.`id` and record_status like '%fail%' ) fail_lines_count , "
					      +" (select count(*) success_lines_count from `t_data_staging` where src_file_inb_id= `sfih`.`id` and record_status like '%success%' ) success_lines_count "
					+"FROM `t_source_file_inb_history` `sfih` "
					+"JOIN   `t_file_templates` `ft` "
					+"JOIN   `t_source_profile_file_assignments` `spfa` "
					+"JOIN   `t_batch_header` `bh` "
					+"JOIN   `t_source_profiles` `sp` "
					+"WHERE  `bh`.`id`=`sfih`.`batch_id` "
					+"AND    `sp`.`id`=`sfih`.`profile_id` "
					+"AND    `sfih`.`src_prf_file_asmt_id` = `spfa`.`id` "
					+"AND    `spfa`.`template_id` = `ft`.`id` "

					+"AND    `sfih`.`batch_id`="+batchId;
			
			log.info("view definition is"+view);
			
		//	stmt.executeUpdate(view);
			log.info("sortColName: "+sortColName+" sortOrder: "+sortOrder+" offSt: "+offSt+" limit: "+limit);
			// query ="SELECT * from "+schemaName+".`"+"batch_details_by_batch_id"+"` "+" order by "+sortColName +" "+sortOrder +" limit "+offSt+", "+limit;
			query ="SELECT * from "+"("+view+") AS batchDetailsByBatch"+" order by "+sortColName +" "+sortOrder +" limit "+offSt+", "+limit;
			
			int totalCountQuery = sourceFileInbHistoryRepository.findByBatchId(batchId).size();
			log.info("query in Batch details: "+query);
			result=stmt.executeQuery(query);
			ResultSetMetaData rsmd = result.getMetaData();
			int colCount = rsmd.getColumnCount();
			while(result.next()){
				HashMap hm = new HashMap();
				Long srcInbHistId = 0L;
				String status = "";
				for(int i=1; i<=colCount; i++)
				{
					if(rsmd.getColumnName(i).equalsIgnoreCase("status"))
					{
						
						status = result.getString(i);
						log.info("status"+status);
						hm.put(rsmd.getColumnName(i), status);
						LookUpCode code = new LookUpCode();
						code = lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("TRANSFORAMTION_STATUS", status, tenantId);
					
						if(code != null && code.getMeaning() != null)
							hm.put("statusMeaning", code.getMeaning());
						else
						{
							code = lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("EXTRACTION_STATUS", status, tenantId);
							if(code != null && code.getMeaning() != null)
								hm.put("statusMeaning", code.getMeaning());
						}
					}
					else if (rsmd.getColumnName(i).equalsIgnoreCase("master_count") )
					{
						if(status.toLowerCase().contains("fail"))
						{
							
						}
						else
						{
							hm.put("lineCount",result.getString(i));
						}
					}
					else if (rsmd.getColumnName(i).equalsIgnoreCase("staging_count") )
					{
						if(status.toLowerCase().contains("fail"))
						{
							hm.put("lineCount",result.getString(i));
						}
						else
						{
							
						}
					}
					else
					{
						hm.put(rsmd.getColumnName(i), result.getString(i));	
						if(rsmd.getColumnName(i).equalsIgnoreCase("srcInbHistId"))
						{
							srcInbHistId=Long.valueOf(result.getString(i));
							log.info("srcInbHistId is:"+srcInbHistId);
						}
					}
					
				}
				hm.put("totalCount", totalCountQuery);
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
			//stmt.executeUpdate( "ALTER view `"+schemaName+"`.`batch_details_by_batch_id` RENAME `.`batch_details_by_batch_id_toDrop`");
			//stmt.executeUpdate( "DROP view `"+schemaName+"`.`batch_details_by_batch_id_toDrop`");

			if(result != null)
				result.close();	
			if(stmt != null)
				stmt.close();
			if(conn != null)
				conn.close();
		}
		return dataMap;

			}

	/**
	 * Author: Kiran
	 * @param offset
	 * @param limit
	 * @param request
	 * @param sourceProfileId
	 * @param srcProfAssgnId
	 * @param status
	 * @param sortColName
	 * @param sortOrder
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	@GetMapping("/batchHeader")
	@Timed
	public List<HashMap> getBatchHeaders(@RequestParam(value = "page" , required = false) Integer offset,@RequestParam(value = "per_page", required = false) Integer limit,
			HttpServletRequest request,@RequestParam(required = false) List<Long> sourceProfileId,@RequestParam(required=false) Long srcProfAssgnId, @RequestParam(required=false) String status, @RequestParam(required = false) String sortColName, @RequestParam(required = false) String sortOrder) throws ClassNotFoundException, SQLException
			{
		log.debug("Rest request to fetch batch list by pagination with sortColName: "+sortColName+" sortingOrder: "+sortOrder);

		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		Long userId=Long.parseLong(map.get("userId").toString());

		Connection conn = null;
		Statement stmt = null;
		Statement stmt1 = null;
		ResultSet result = null; 
		ResultSet result1 = null;
		String schemaName=null;
		List<HashMap> dataMap = new ArrayList<HashMap>();
		//		List<BatchHeaderDTO> batchHeaderList = new ArrayList<BatchHeaderDTO>();
		List<Long> batchIds = new ArrayList<Long>();
		log.info("sourceProfileId"+sourceProfileId+"srcProfAssgnId"+srcProfAssgnId);
		
		if(srcProfAssgnId == null && sourceProfileId ==null)
		{
			log.info("both are null");
			//batchIds = sourceFileInbHistoryRepository.fetchDistinctBatchIdsUsingTenant(tenantId);
			List<Object> batchIdsByTenantId = new ArrayList<Object>();
			batchIdsByTenantId = batchHeaderRepository.findBatchIdsByTenantId(tenantId);
			if(batchIdsByTenantId.size()>0)
			batchIds=(List<Long>)(Object)batchIdsByTenantId;
			//log.info("batchIds with both null"+batchIds);
		}
		else if(srcProfAssgnId!=null)
		{
			log.info("srcProfAssId is not null");
			//batchIds = sourceFileInbHistoryRepository.fetchDistinctBatchIdsBySrcPrfFileAsmtId(srcProfAssgnId);
			List<Object> batchIdsBysrcPrfAsmtId =batchHeaderRepository.findBySrcPrfAsmtId(srcProfAssgnId);
			if(batchIdsBysrcPrfAsmtId.size()>0)
			batchIds=(List<Long>)(Object)batchIdsBysrcPrfAsmtId;
			//log.info("batchIds with srcProfAssgnId"+batchIds);
		}
		else if(sourceProfileId != null && sourceProfileId.size() >0 )
		{
			log.info("sourceProfile id is not null and has "+ sourceProfileId.size());
			//batchIds = sourceFileInbHistoryRepository.fetchDistinctBatchIdsByProfileIdIn(sourceProfileId);
			List<Object> batchIdsByProfileId =batchHeaderRepository.findByProfileIdIn(sourceProfileId);
			if(batchIdsByProfileId.size()>0)
			batchIds=(List<Long>)(Object)batchIdsByProfileId;
			//log.info("batchIds with sourceProfileId"+batchIds);
		}
		
		//get distinct batch id's of these profile
		log.info("batchIds:"+batchIds);

		if(batchIds.size()>0)
		{
			if(limit == null || limit == 0 )
			{
				limit = batchIds.size();
			}
			if(offset == null )
			{
				offset=0;
			}

			if(sortOrder==null)
				sortOrder="desc";

			try
			{
				DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
		 		conn=ds.getConnection();
				stmt = conn.createStatement();
				log.info("Successfully connected to JDBC with schema "+schemaName);

				int offSt = 0;
				offSt = (offset * limit + 1)-1;
				String query = "";


				String batchIdsList = batchIds.toString();
				batchIdsList = batchIdsList.substring(1, batchIdsList.length() - 1);



				String view="Select "
						+ "`bh`.`id` AS `id`, "
						+ "`bh`.`batch_name` AS `batchName`, "
						+ "`bh`.`ext_ref` AS `reference`, "
						+ "`bh`.`jhi_type` AS `batchType`, "
						+ "`bh`.`job_ref` AS `jobRef`, "
						+ "`bh`.`extraction_status` AS `extractionStatus`, "
						+ "`bh`.`extracted_datetime` AS `extractedDateTime`, "
						+ "`bh`.`transformation_status` AS `transformationStatus`, "
						+ "`bh`.`transformed_datetime` AS `transformedDateTime`, "
						+ "`bh`.`hold` AS `hold`,"
						+ "`bh`.`hold_reason` AS `holdReason`, "
						+ "GREATEST(IFNULL(`bh`.`transformed_datetime`,'2001-01-01 00:00:00'),IFNULL(`bh`.`extracted_datetime`,'2001-01-01 00:00:00')) as `lastActionTime` "
						+ "FROM  ("
						+ "(`t_batch_header` `bh`)) "
						+ "where `bh`.`id` in ("+batchIdsList+")";
				
				if(status!=null)
				{
					if(status.toLowerCase().contains("hold"))
					{
						view = view+" and `bh`.`hold` is true ";
					}
					else if(status.equalsIgnoreCase("scheduled"))
					{
						view = view+" and `bh`.`jhi_type` != 'Manual' ";
					}
					else if(status.equalsIgnoreCase("manual"))
					{
						view = view+" and `bh`.`jhi_type` = 'Manual' ";
					}
					else if(status.equalsIgnoreCase("Extracted"))
					{
						view = view+" and `bh`.`extraction_status` like '%"+status+"%' ";
					}
					else if(status.equalsIgnoreCase("transformed"))
					{
						view = view+" and `bh`.`transformation_status` like '%LOADED%' ";
					}
					else if(status.equalsIgnoreCase("fail"))
					{
						view = view+" and ( `bh`.`extraction_status` like '%Failed%' or `bh`.`transformation_status` like '%Failed%' ) ";
					}
				}


				log.info("B4 Execution view: "+view);
				query = "SELECT * from "+"("+view+") AS batch_header_by_prf_or_asmt_id";

				stmt.executeQuery(query);
				stmt1 = conn.createStatement();

				log.info("sortColName: "+sortColName+" sortOrder: "+sortOrder+" offSt: "+offSt+" limit: "+limit);

				if(sortColName==null)
				{
//					sortColName="transformedDatetime desc, extractedDatetime desc";
					sortColName="lastActionTime desc";
					query ="SELECT * from "+"("+view+") AS batch_header_by_prf_or_asmt_id"+" order by "+sortColName +" limit "+offSt+", "+limit;
					result1=stmt1.executeQuery("SELECT COUNT(*) AS rowcount from "+"("+view+") AS batch_header_by_prf_or_asmt_id");
				}
				else
				{
					query ="SELECT * from "+schemaName+".`"+"batch_header_by_prf_or_asmt_id"+"` "+" order by "+sortColName +" "+sortOrder +" limit "+offSt+", "+limit;
					result1=stmt1.executeQuery("SELECT COUNT(*) AS rowcount from "+"("+view+") AS batch_header_by_prf_or_asmt_id");
				}

				log.info("query in Batch get details: "+query);


				result1.next();
				int count = result1.getInt("rowcount");
				log.info("count: "+count);
				result1.close();

				result=stmt.executeQuery(query);
				ResultSetMetaData rsmd = result.getMetaData();
				int colCount = rsmd.getColumnCount();
				while(result.next())
				{
					HashMap hm = new HashMap();
					String extractionStatusMeaning="";
					String transformationStatusMeaning="";
					
					for(int i=1; i<=colCount; i++)
					{
						String value="";
						value=result.getString(i);
						hm.put(rsmd.getColumnName(i),value );
//						log.info("rsmd.getColumnName(i): "+rsmd.getColumnName(i)+" result.getString(i): "+result.getString(i));
						if(rsmd.getColumnName(i).equalsIgnoreCase("extractionStatus") && (result.getString(i)!=null && result.getString(i).contains("EXTRACT")))
						{
							//log.info("rsmd.getColumnName(i)->1: "+rsmd.getColumnName(i)+" result.getString(i): "+result.getString(i));
							if(result.getString(i)!=null)
							{
								String extStatus=result.getString(i);
								
								if(extStatus.contains(","))
								{
									String[] ext=extStatus.split(",");
									String extStatus1=ext[0];
									String extStatus2=ext[1];
									String extractStatusMeaning1=extractStatusMeaning(extStatus1, tenantId, userId);
									String extractStatusMeaning2=extractStatusMeaning(extStatus2, tenantId, userId);
									extractionStatusMeaning=extractStatusMeaning1+", "+extractStatusMeaning2;
								}
								else
								{
									//log.info("extStatus->: "+extStatus);
									 extractionStatusMeaning=extractStatusMeaning(extStatus, tenantId, userId);
								}
							}
						}
						
						else if(rsmd.getColumnName(i).equalsIgnoreCase("transformationStatus") && (result.getString(i)!=null && result.getString(i).contains("LOAD")))
						{
							//log.info("rsmd.getColumnName(i)->2: "+rsmd.getColumnName(i)+" result.getString(i): "+result.getString(i));
							if(result.getString(i)!=null)
							{
								String transStatus=result.getString(i);
								
								if(transStatus.contains(","))
								{
									String[] ext=transStatus.split(",");
									String extStatus1=ext[0];
									String extStatus2=ext[1];
									log.info("extStatus1 :"+extStatus1);
									log.info("extStatus2 :"+extStatus2);
									String extractStatusMeaning1=transformStatusMeaning(extStatus1, tenantId, userId);
									String extractStatusMeaning2=transformStatusMeaning(extStatus2, tenantId, userId);
									transformationStatusMeaning=extractStatusMeaning1+", "+extractStatusMeaning2;
								}
								else
								{
									transformationStatusMeaning=transformStatusMeaning(transStatus, tenantId, userId);
								}
							}
						}
						
						if(hm.get("jobRef") != null)
						{
							String val=(String) hm.get("jobRef");
							SchedulerDetails schedulesByJobRef = new SchedulerDetails();
							if(val != null && !val.isEmpty() && !val.equals("")&& !val.equals(" "))
							{
								schedulesByJobRef = schedulerDetailsRepository.findByOozieJobIdAndTenantID(tenantId,val);
								if(schedulesByJobRef != null && schedulesByJobRef.getFrequency() != null && !schedulesByJobRef.getFrequency().equalsIgnoreCase("ONDEMAND"))
								{
									HashMap lastRunMap=new HashMap();
									lastRunMap = oozieService.getNextRunByOozieJobId(schedulesByJobRef.getOozieJobId());
									if(lastRunMap.size()>0 )
									{
										if( lastRunMap.get("nextMatdTime") != null)
										{
											hm.put("nextSchedule", lastRunMap.get("nextMatdTime").toString());
										}
										else
										{
											hm.put("nextSchedule", "No Schedule");
										}
									}
								}
							}
							
						}
					}
					
					//log.info("extractionStatusMeaning: "+extractionStatusMeaning);
					//log.info("transformationStatusMeaning: "+transformationStatusMeaning);
					
					hm.put("extractionStatusMeaning", extractionStatusMeaning);
					hm.put("transformationStatusMeaning", transformationStatusMeaning);

					hm.put("totalCount", count);

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
				//stmt.executeUpdate( "DROP view `"+schemaName+"`.`batch_header_by_prf_or_asmt_id`");

				if(result != null)
					result.close();	
				if(result1!=null)
					result1.close();
				if(stmt != null)
					stmt.close();
				if(stmt1!=null)
					stmt1.close();
				if(conn != null)
					conn.close();
			}
		}
		return dataMap;

			}
	
	
	/**
	 * Author : Kiran
	 * @param extStatus
	 * @param tenantId
	 * @param userId
	 * @return
	 */
	public String extractStatusMeaning(String extStatus, Long tenantId, Long userId)
	{
		String[] ext=extStatus.split("-");
		String ext1=ext[0];
		String ext2=ext[1];
		String extractionStatusMeaning="";
		if(ext2!=null)
		{
			//log.info("ext2.trim(),tenantId,userId"+ext2.trim()+tenantId+userId);
			LookUpCode extCode = lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("EXTRACTION_STATUS", ext2.trim(), tenantId);
			if(extCode != null && extCode.getMeaning() != null)
			{
				//log.info("extCode: "+extCode.getMeaning());
				ext2=extCode.getMeaning();
				extractionStatusMeaning=ext1+"- "+ext2;
			}
		}
		return extractionStatusMeaning;
	}
	
	/**
	 * Author : Kiran
	 * @param extStatus
	 * @param tenantId
	 * @param userId
	 * @return
	 */
	public String transformStatusMeaning(String extStatus, Long tenantId, Long userId)
	{
		String[] ext=extStatus.split("-");
		String ext1=ext[0];
		String ext2=ext[1];
		String transformationStatusMeaning="";
		if(ext2!=null)
		{
			LookUpCode transCode = lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("TRANSFORAMTION_STATUS", ext2.trim(), tenantId);
			if(transCode != null && transCode.getMeaning() != null)
			{
				//log.info("transCode: "+transCode);
				transformationStatusMeaning=ext1+"- "+transCode.getMeaning();
			}
		}
		return transformationStatusMeaning;
	}

}
