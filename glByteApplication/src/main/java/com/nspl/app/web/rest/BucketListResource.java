package com.nspl.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.domain.BucketDetails;
import com.nspl.app.domain.BucketList;
import com.nspl.app.repository.BucketDetailsRepository;
import com.nspl.app.repository.BucketListRepository;
import com.nspl.app.service.ActiveStatusService;
import com.nspl.app.service.UserJdbcService;
import com.nspl.app.web.rest.util.HeaderUtil;
import com.nspl.app.web.rest.util.PaginationUtil;

import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;

import org.json.simple.JSONObject;
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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * REST controller for managing BucketList.
 */
@RestController
@RequestMapping("/api")
public class BucketListResource {

	private final Logger log = LoggerFactory.getLogger(BucketListResource.class);

	private static final String ENTITY_NAME = "bucketList";

	private final BucketListRepository bucketListRepository;

	@Inject
	BucketDetailsRepository bucketDetailsRepository;
	
	@Inject
	UserJdbcService userJdbcService;
	
	@Inject
	ActiveStatusService activeStatusService;

	public BucketListResource(BucketListRepository bucketListRepository) {
		this.bucketListRepository = bucketListRepository;
	}

	/**
	 * POST  /bucket-lists : Create a new bucketList.
	 *
	 * @param bucketList the bucketList to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new bucketList, or with status 400 (Bad Request) if the bucketList has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/bucket-lists")
	@Timed
	public ResponseEntity<BucketList> createBucketList(@RequestBody BucketList bucketList) throws URISyntaxException {
		log.debug("REST request to save BucketList : {}", bucketList);
		if (bucketList.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new bucketList cannot already have an ID")).body(null);
		}
		BucketList result = bucketListRepository.save(bucketList);
		return ResponseEntity.created(new URI("/api/bucket-lists/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /bucket-lists : Updates an existing bucketList.
	 *
	 * @param bucketList the bucketList to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated bucketList,
	 * or with status 400 (Bad Request) if the bucketList is not valid,
	 * or with status 500 (Internal Server Error) if the bucketList couldn't be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/bucket-lists")
	@Timed
	public ResponseEntity<BucketList> updateBucketList(@RequestBody BucketList bucketList) throws URISyntaxException {
		log.debug("REST request to update BucketList : {}", bucketList);
		if (bucketList.getId() == null) {
			return createBucketList(bucketList);
		}
		BucketList result = bucketListRepository.save(bucketList);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, bucketList.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /bucket-lists : get all the bucketLists.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of bucketLists in body
	 */
	@GetMapping("/bucket-lists")
	@Timed
	public ResponseEntity<List<BucketList>> getAllBucketLists(HttpServletRequest request, @ApiParam Pageable pageable, @RequestParam(required=false) String bucketType) {
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		log.debug("REST request to get a page of BucketLists");
		Page<BucketList> page;
		if(bucketType!=null){
			page = bucketListRepository.findByTenantIdAndTypeAndEnabledFlagTrueOrderByIdDesc(tenantId, bucketType, pageable);
		}
		else {
			page = bucketListRepository.findByTenantIdAndEnabledFlagTrueOrderByIdDesc(tenantId,pageable);
		}
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bucket-lists");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET  /bucket-lists/:id : get the "id" bucketList.
	 *
	 * @param id the id of the bucketList to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the bucketList, or with status 404 (Not Found)
	 */
	@GetMapping("/bucket-lists/{id}")
	@Timed
	public ResponseEntity<BucketList> getBucketList(@PathVariable Long id) {
		log.debug("REST request to get BucketList : {}", id);
		BucketList bucketList = bucketListRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(bucketList));
	}

	/**
	 * DELETE  /bucket-lists/:id : delete the "id" bucketList.
	 *
	 * @param id the id of the bucketList to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/bucket-lists/{id}")
	@Timed
	public ResponseEntity<Void> deleteBucketList(@PathVariable Long id) {
		log.debug("REST request to delete BucketList : {}", id);
		bucketListRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * Author: Swetha
	 * Api to Post Bucket and its detail info
	 * @param bucketInfo
	 * @return
	 */
	@PostMapping("/postBucketInfo")
	@Timed
	public Long postBucketInfo(HttpServletRequest request,@RequestBody HashMap bucketInfo){

		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		Long userId=Long.parseLong(map.get("userId").toString());
		log.info("Rest request to post complete bucket and detail info with data: "+bucketInfo);
		Long newBucketListDataId=0L;
		if(bucketInfo!=null && !(bucketInfo.isEmpty())){

			BucketList bucketListData=new BucketList();
			if(bucketInfo.get("bucketName")!=null && !(bucketInfo.get("bucketName").toString().isEmpty()))
				bucketListData.setBucketName(bucketInfo.get("bucketName").toString());
			if(bucketInfo.get("type")!=null && !(bucketInfo.get("type").toString().isEmpty()))
				bucketListData.setType(bucketInfo.get("type").toString());
			if(bucketInfo.get("count")!=null && !(bucketInfo.get("count").toString().isEmpty()))
				bucketListData.setCount(Integer.parseInt(bucketInfo.get("count").toString()));

			// Updated to handle Zoned Date with milli secs conflict @Rk
			if(bucketInfo.get("startDate")!=null && !(bucketInfo.get("startDate").toString().isEmpty()))
			{
				ZonedDateTime stDate=ZonedDateTime.parse(bucketInfo.get("startDate").toString());
				bucketListData.setStartDate(stDate);
			}
			if(bucketInfo.get("endDate")!=null && !(bucketInfo.get("endDate").toString().isEmpty()))
			{
				ZonedDateTime edDate=ZonedDateTime.parse(bucketInfo.get("endDate").toString());
				bucketListData.setEndDate(edDate);
			}

			if(bucketInfo.get("enabledFlag")!=null && !(bucketInfo.get("enabledFlag").toString().isEmpty()))
				bucketListData.setEnabledFlag((Boolean) bucketInfo.get("enabledFlag"));
			bucketListData.setTenantId(tenantId);

			if(bucketInfo.get("id")!=null){
				bucketListData.setId(Long.parseLong(bucketInfo.get("id").toString()));
				bucketListData.setLastUpdatedBy(userId);
				bucketListData.setLastUpdatedDate(ZonedDateTime.now());
			}
			else{
				bucketListData.setCreatedBy(userId);
				bucketListData.setCreatedDate(ZonedDateTime.now());
			}
			
			BucketList newBucketListData=bucketListRepository.save(bucketListData);
			newBucketListDataId=newBucketListData.getId();
			log.info("newBucketListData has been save with id: "+newBucketListDataId);

			if(bucketInfo.get("bucketDetDataList")!=null){
				List<HashMap> bucketDetailsDataList= (List<HashMap>) bucketInfo.get("bucketDetDataList");
				if(!(bucketDetailsDataList.isEmpty()) && bucketDetailsDataList.size()>0){

					for(int i=0;i<bucketDetailsDataList.size();i++){

						HashMap bucketDetailsData=bucketDetailsDataList.get(i);
						BucketDetails bucketDetData=new BucketDetails();
						if(bucketDetailsData.get("fromValue")!=null && !(bucketDetailsData.get("fromValue").toString().isEmpty()))
							bucketDetData.setFromValue(Integer.parseInt(bucketDetailsData.get("fromValue").toString()));
						if(bucketDetailsData.get("toValue")!=null && !(bucketDetailsData.get("toValue").toString().isEmpty()))
							bucketDetData.setToValue(Integer.parseInt(bucketDetailsData.get("toValue").toString()));
						if(bucketDetailsData.get("seqNum")!=null && !(bucketDetailsData.get("seqNum").toString().isEmpty()))
							bucketDetData.setSeqNum(Integer.parseInt(bucketDetailsData.get("seqNum").toString()));
						bucketDetData.setBucketId(newBucketListDataId);	
						bucketDetData.setCreatedBy(userId);
						bucketDetData.setCreatedDate(ZonedDateTime.now());
						if(bucketDetailsData.get("id")!=null){
							bucketDetData.setId(Long.parseLong(bucketDetailsData.get("id").toString()));
						}
						BucketDetails newBucketDet=bucketDetailsRepository.save(bucketDetData);
						log.info("newBucketDet has been saved with id: "+newBucketDet.getId());

					}
				}

			}


		}
		return newBucketListDataId;

	}

	/**
	 * Author: Swetha
	 * Api to retrieve bucket and its detail info
	 * @param bucketId
	 * @param tenantId
	 * @return
	 */
	@GetMapping("/getBucketInfo")
	@Timed
	public HashMap getBucketInfo(HttpServletRequest request,@RequestParam(value="bucketId") Long bucketId){

		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());

		log.info("Rest request to get complete bucket and detail for bucketId: "+bucketId);
		HashMap BucketData=new HashMap();
		BucketList bucketInfo=bucketListRepository.findOne(bucketId);
		if(bucketInfo!=null){

			if(bucketInfo.getBucketName()!=null)
				BucketData.put("bucketName",bucketInfo.getBucketName());
			if(bucketInfo.getId()!=null)
				BucketData.put("id",bucketInfo.getId());
			if(bucketInfo.getStartDate()!=null)
				BucketData.put("startDate",bucketInfo.getStartDate());
			if(bucketInfo.getEndDate()!=null)
				BucketData.put("endDate",bucketInfo.getEndDate());
			if(bucketInfo.getEndDate()!=null)
				BucketData.put("enabledFalg",bucketInfo.getEndDate());
			if(bucketInfo.getType()!=null)
				BucketData.put("type",bucketInfo.getType());
			if(bucketInfo.getCount()!=null)
				BucketData.put("count",bucketInfo.getCount());
			if(bucketInfo.getCreatedBy()!=null)
				BucketData.put("createdBy",bucketInfo.getCreatedBy());
			if(bucketInfo.getCreatedDate()!=null)
				BucketData.put("createdDate",bucketInfo.getCreatedDate());

			List<HashMap> bucketDetDataList= new ArrayList<HashMap>();
			List<BucketDetails> bucketDetailsDataList=bucketDetailsRepository.findByBucketId(bucketId);
			for(int i=0;i<bucketDetailsDataList.size();i++){

				BucketDetails bucketDetailsData=bucketDetailsDataList.get(i);
				HashMap bucketDetData=new HashMap();
				if(bucketDetailsData.getFromValue()!=null)
					bucketDetData.put("fromValue",bucketDetailsData.getFromValue());
				if(bucketDetailsData.getToValue()!=null)
					bucketDetData.put("toValue",bucketDetailsData.getToValue());
				if(bucketDetailsData.getSeqNum()!=null)
					bucketDetData.put("seqNum",bucketDetailsData.getSeqNum());
				if(bucketDetailsData.getBucketId()!=null)
					bucketDetData.put("bucketId", bucketDetailsData.getBucketId());
				if(bucketDetailsData.getCreatedBy()!=null)
					bucketDetData.put("createdBy", bucketDetailsData.getCreatedBy());
				if(bucketDetailsData.getCreatedDate()!=null)
					bucketDetData.put("createdDate", bucketDetailsData.getCreatedDate());
				if(bucketDetailsData.getId()!=null)
					bucketDetData.put("id", bucketDetailsData.getId());
				bucketDetDataList.add(bucketDetData);
			}

			BucketData.put("bucketDetDataList", bucketDetDataList);


		}
		return BucketData;

	}

	/**
	 * Author: Swetha
	 * Api to check BucketName Duplication
	 * @param tenantId
	 * @param bucketName
	 * @return
	 */
	@GetMapping("/checkBucketNameDuplication")
	@Timed
	public Long checkBucketNameDuplication(HttpServletRequest request, @RequestParam String bucketName){

		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		log.info("Rest Request to checkBucketNameDuplication with tenantId: "+tenantId+" and bucketName: "+bucketName);
		Long result=0L;
		Long count=bucketListRepository.fetchBucketNameCount(tenantId, bucketName);
		log.info("count of bucketName: "+bucketName+" is: "+count);
		if(count>1){
			Long bucketNameCnt=bucketListRepository.fetchBucketNameCount(tenantId, bucketName);
			result=bucketNameCnt;
		}
		else if(count==1){
			BucketList bucket=bucketListRepository.findByTenantIdAndBucketName(tenantId, bucketName);
			result=bucket.getId();
		}
		return result;

	}

	/**
	 * Author: Swetha
	 * Api to Fetch Buckets by TenantId with Search Functionality
	 * @param request
	 * @param response
	 * @param pageNumber
	 * @param pageSize
	 * @param searchKeyword
	 * @return
	 */
	@GetMapping("/getBucketsByTenantId")
	@Timed
	public List<JSONObject>  getBucketsByTenantId(HttpServletRequest request,HttpServletResponse response,@RequestParam(value = "page" , required = false) Integer pageNumber,
			@RequestParam(value = "per_page", required = false) Integer pageSize, @RequestParam(required = false) String searchKeyword){
		log.info("Rest request to getBucketsByTenantId ");
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		log.info("tenantId: "+tenantId+" pageNumber: "+pageNumber+" pageSize: "+pageSize);
		List<BucketList> bucketList=bucketListRepository.findByTenantIdOrderByIdDesc(tenantId);
		int totBucketListCnt=0;
		List<JSONObject> bucketDtoList=new ArrayList<JSONObject>();
		List<JSONObject> limitedOutputList=new ArrayList<JSONObject>();
		if(bucketList!=null && bucketList.size()>0){
			log.info("bucketList.size() for tenantId: "+tenantId+" is: "+bucketList.size());
			totBucketListCnt=bucketList.size();
			Iterator itr=bucketList.iterator();
			while(itr.hasNext()){
				BucketList bucketInfo= (BucketList) itr.next();
				LinkedHashMap bucketDto=new LinkedHashMap();

				bucketDto.put("id", bucketInfo.getId());
				if( bucketInfo.getBucketName()!=null)
					bucketDto.put("bucketName", bucketInfo.getBucketName());
				if(bucketInfo.getType()!=null)
					bucketDto.put("type", bucketInfo.getType());
				if(bucketInfo.getCount()!=null)
					bucketDto.put("count", bucketInfo.getCount());
				if(bucketInfo.getStartDate()!=null)
					bucketDto.put("startDate", bucketInfo.getStartDate());
				if(bucketInfo.getEndDate()!=null)
					bucketDto.put("endDate", bucketInfo.getEndDate());
				/*if(bucketInfo.isEnabledFlag()!=null)
					bucketDto.put("enabledFlag", bucketInfo.isEnabledFlag());*/
				Boolean activeStatus=activeStatusService.activeStatus(bucketInfo.getStartDate(), bucketInfo.getEndDate(),bucketInfo.isEnabledFlag());
				bucketDto.put("enabledFlag", activeStatus);
				//bucketDto.put("tenantId", bucketInfo.getTenantId());
				/*bucketDto.put("createdDate", bucketInfo.getCreatedDate());
    			bucketDto.put("createdBy", bucketInfo.getCreatedBy());
    			bucketDto.put("lastUpdatedDate", bucketInfo.getLastUpdatedDate());
    			bucketDto.put("lastUpdatedBy", bucketInfo.getLastUpdatedBy());*/

				JSONObject jsonValue =new JSONObject();
				JSONObject obj=new JSONObject();
				obj.putAll(bucketDto);

				bucketDtoList.add(obj);
			}

			int xCount=0;
			//Search Functionality
			List<JSONObject> searchFilteredObjList=new ArrayList<JSONObject>();
			if(searchKeyword!=null){
				List<String> keyList=new ArrayList<String>();
				keyList.add("bucketName");
				keyList.add("type");
				keyList.add("count");
				keyList.add("startDate");
				keyList.add("endDate");

				for (int i = 0; i < bucketDtoList.size(); i++) {

					for (String  keys:keyList)
					{
						if(bucketDtoList.get(i).get(keys)!=null)
							if(bucketDtoList.get(i).get(keys).toString().toLowerCase().contains(searchKeyword.toLowerCase())){
								searchFilteredObjList.add(bucketDtoList.get(i));
								break;
							}
					}

				}
				xCount=searchFilteredObjList.size();
				log.info("xCount after searching : "+xCount);
			}
			else{
				searchFilteredObjList=bucketDtoList;
				xCount=totBucketListCnt;
			}
			log.info("xCount: "+xCount);

			response.addIntHeader("X-COUNT", xCount);
			int limit = 0;
			if(pageNumber == null || pageNumber == 0)
			{
				pageNumber = 0;
			}
			if(pageSize == null || pageSize == 0)
			{
				pageSize = xCount;
			}
			limit = ((pageNumber+1) * pageSize + 1)-1;
			int startIndex=pageNumber*pageSize; 

			if(limit>xCount){
				limit=xCount;
			}
			log.info("startIndex: "+startIndex+" limit: "+limit);
			if(searchFilteredObjList!=null && searchFilteredObjList.size()>0 && limit<=searchFilteredObjList.size()){
				limitedOutputList=searchFilteredObjList.subList(startIndex, limit);
			}
			else limitedOutputList=searchFilteredObjList;
		}
		log.info("***end of API**** "+ZonedDateTime.now());
		return limitedOutputList;
	}
}
