package com.nspl.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.domain.BucketList;
import com.nspl.app.domain.DataViews;
import com.nspl.app.domain.ReportDefination;
import com.nspl.app.domain.ReportRequests;
import com.nspl.app.domain.ReportType;
import com.nspl.app.domain.Reports;
import com.nspl.app.repository.BucketListRepository;
import com.nspl.app.repository.DataViewsRepository;
import com.nspl.app.repository.ReportDefinationRepository;
import com.nspl.app.repository.ReportRequestsRepository;
import com.nspl.app.repository.ReportTypeRepository;
import com.nspl.app.repository.ReportsRepository;
import com.nspl.app.service.UserJdbcService;
import com.nspl.app.web.rest.util.HeaderUtil;
import com.nspl.app.web.rest.util.PaginationUtil;

import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * REST controller for managing ReportRequests.
 */
@RestController
@RequestMapping("/api")
public class ReportRequestsResource {

    private final Logger log = LoggerFactory.getLogger(ReportRequestsResource.class);

    private static final String ENTITY_NAME = "reportRequests";

    private final ReportRequestsRepository reportRequestsRepository;
    
    @Inject
    UserJdbcService userJdbcService;
    
    @Inject
    ReportsRepository reportsRepository;
    
    @Inject
    DataViewsRepository dataViewsRepository;
    
    @Inject
    ReportTypeRepository reportTypeRepository;
    
    @Inject
    BucketListRepository bucketListRepository;
    
    @Inject
    ReportDefinationRepository reportDefinationRepository;
    
    public ReportRequestsResource(ReportRequestsRepository reportRequestsRepository) {
        this.reportRequestsRepository = reportRequestsRepository;
    }

    /**
     * POST  /report-requests : Create a new reportRequests.
     *
     * @param reportRequests the reportRequests to create
     * @return the ResponseEntity with status 201 (Created) and with body the new reportRequests, or with status 400 (Bad Request) if the reportRequests has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/report-requests")
    @Timed
    public ResponseEntity<ReportRequests> createReportRequests(@RequestBody ReportRequests reportRequests) throws URISyntaxException {
        log.debug("REST request to save ReportRequests : {}", reportRequests);
        if (reportRequests.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new reportRequests cannot already have an ID")).body(null);
        }
        ReportRequests result = reportRequestsRepository.save(reportRequests);
        return ResponseEntity.created(new URI("/api/report-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /report-requests : Updates an existing reportRequests.
     *
     * @param reportRequests the reportRequests to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated reportRequests,
     * or with status 400 (Bad Request) if the reportRequests is not valid,
     * or with status 500 (Internal Server Error) if the reportRequests couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/report-requests")
    @Timed
    public ResponseEntity<ReportRequests> updateReportRequests(@RequestBody ReportRequests reportRequests) throws URISyntaxException {
        log.debug("REST request to update ReportRequests : {}", reportRequests);
        if (reportRequests.getId() == null) {
            return createReportRequests(reportRequests);
        }
        ReportRequests result = reportRequestsRepository.save(reportRequests);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, reportRequests.getId().toString()))
            .body(result);
    }

    /**
     * GET  /report-requests : get all the reportRequests.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of reportRequests in body
     */
    @GetMapping("/report-requests")
    @Timed
    public ResponseEntity<List<ReportRequests>> getAllReportRequests(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of ReportRequests");
        Page<ReportRequests> page = reportRequestsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/report-requests");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /report-requests/:id : get the "id" reportRequests.
     *
     * @param id the id of the reportRequests to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the reportRequests, or with status 404 (Not Found)
     */
    @GetMapping("/report-requests/{id}")
    @Timed
    public ResponseEntity<ReportRequests> getReportRequests(@PathVariable Long id) {
        log.debug("REST request to get ReportRequests : {}", id);
        ReportRequests reportRequests = reportRequestsRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(reportRequests));
    }
    
    
    /**
     * authore ravali
     * @param id
     * @return
     * @throws ParseException
     * Desc :api to get a parameter object for a request
     */
    @GetMapping("/getRequestparameterObject")
    @Timed
    public LinkedHashMap getRequestparameterObject(@RequestParam String id,HttpServletRequest request) throws ParseException {
        log.debug("REST request to get a parameter object for a requestId", id);
        HashMap map0=userJdbcService.getuserInfoFromToken(request);
      	Long tenantId=Long.parseLong(map0.get("tenantId").toString());
        ReportRequests reportRequests = reportRequestsRepository.findByTenantIdAndIdForDisplay(tenantId, id);
        LinkedHashMap map=new LinkedHashMap();
        if(reportRequests.getFilterMap()!=null)
		{
			JSONParser parser = new JSONParser();
			
			String paramVal=reportRequests.getFilterMap();
			JSONObject jsonValue = (JSONObject) parser.parse(paramVal);
			map.put("filterMap", jsonValue);
		}
		else
			map.put("filterMap", "");
        
        
        return map;
    }

    /**
     * DELETE  /report-requests/:id : delete the "id" reportRequests.
     *
     * @param id the id of the reportRequests to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/report-requests/{id}")
    @Timed
    public ResponseEntity<Void> deleteReportRequests(@PathVariable Long id) {
        log.debug("REST request to delete ReportRequests : {}", id);
        reportRequestsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    /**
     * Author: Swetha
     * @param request
     * @param reqId
     * @return
     * @throws ParseException
     */
    @GetMapping("/requestInfoByReqId")
    @Timed
    public LinkedHashMap requestInfoByReqId(HttpServletRequest request, @RequestParam(value="reqId", required=true) String reqId) throws ParseException {
    	HashMap map0=userJdbcService.getuserInfoFromToken(request);
      	Long tenantId=Long.parseLong(map0.get("tenantId").toString());
      	log.info("rest request to get requestInfoByReqId :"+tenantId);
      	ReportRequests reqInfo = reportRequestsRepository.findByTenantIdAndIdForDisplay(tenantId,reqId);
      	Reports repData=reportsRepository.findByTenantIdAndId(tenantId, reqInfo.getReportId());
      	LinkedHashMap map=new LinkedHashMap();
		if(reqInfo!=null)
		{
			if(reqInfo.getId()!=null){
				ReportRequests reqDataInp=reportRequestsRepository.findOne(reqInfo.getId());
				map.put("reqId",reqDataInp.getIdForDisplay() );
			}
			if(reqInfo.getReqName()!=null)
				map.put("reqName", reqInfo.getReqName());
			if(reqInfo.getReportId()!=null){
				Reports reportsData=reportsRepository.findOne(reqInfo.getReportId());
				map.put("reportId", reportsData.getIdForDisplay());
			}
			if(reqInfo.getOutputPath()!=null)
				map.put("outputPath", reqInfo.getOutputPath());
			if(reqInfo.getFileName()!=null)
				map.put("fileName", reqInfo.getFileName());
			if(reqInfo.getTenantId()!=null)
				map.put("tenantId", reqInfo.getTenantId());
			if( reqInfo.getStatus()!=null)
				map.put("status", reqInfo.getStatus());
			if(reqInfo.getCreatedDate()!=null)
				map.put("createdDate", reqInfo.getCreatedDate());
			if(reqInfo.getCreatedBy()!=null)
				map.put("createdBy", reqInfo.getCreatedBy());
			if(reqInfo.getLastUpdatedBy()!=null)
				map.put("lastUpdatedBy", reqInfo.getLastUpdatedBy());
			if(reqInfo.getLastUpdatedDate()!=null)
				map.put("lastUpdatedDate", reqInfo.getLastUpdatedDate());
			if(reqInfo.getSubmittedTime()!=null)
				map.put("submittedTime", reqInfo.getSubmittedTime());
			if(reqInfo.getGeneratedTime()!=null)
				map.put("generatedTime", reqInfo.getGeneratedTime());
			if(reqInfo.getJobId()!=null)
				map.put("jobId", reqInfo.getJobId());
			if(reqInfo.getRequestType()!=null)
				map.put("requestType", reqInfo.getRequestType());
			
			List<ReportDefination> reportCondList=new ArrayList<ReportDefination>();
        	reportCondList=reportDefinationRepository.fetchReportConditions(repData.getId());
			
			List<HashMap> additionalParamsList=new ArrayList<HashMap>();
			
			if(repData.getSourceViewId()!=null){
				DataViews dvData=dataViewsRepository.findOne(repData.getSourceViewId());
				HashMap map1=new HashMap();
				map1.put("Parameter", "Data View");
				map1.put("Value", dvData.getDataViewDispName());
				additionalParamsList.add(map1);
			}
			if(repData.getReportTypeId()!=null){
				ReportType repType=reportTypeRepository.findOne(repData.getReportTypeId());
				//log.info("repType: "+repType);
				HashMap map2=new HashMap();
				if(repType.getType()!=null && repType.getType().equalsIgnoreCase("AGING_REPORT")){
					Long buckId=Long.parseLong(repData.getReportVal01().toString());
					//log.info("buckId: "+buckId);
					BucketList buckList=bucketListRepository.findOne(buckId);
					map2.put("Parameter", "Bucket Name");
					map2.put("Value", buckList.getBucketName());
					additionalParamsList.add(map2);
				}
				else if(repType.getType()!=null && repType.getType().equalsIgnoreCase("ROLL_BACK_REPORT")){
					String rollBackVal=repData.getReportVal01();
					HashMap map3=new HashMap();
					map3.put("Parameter", "Roll Back Type");
					map3.put("Value", rollBackVal+" Wise("+repData.getReportVal02()+" "+rollBackVal+")");
					additionalParamsList.add(map3);
				}
			
			if(repType.getType()!=null && (repType.getType().equalsIgnoreCase("AGING_REPORT") || repType.getType().equalsIgnoreCase("ROLL_BACK_REPORT"))){
    			ReportDefination aggregatorData=reportDefinationRepository.findByReportIdAndDataType(repData.getId(), "AGGREGATOR");
    			HashMap map4=new HashMap();
    			map4.put("Parameter", "Agregate Column");
				map4.put("Value", aggregatorData.getDisplayName());
    			additionalParamsList.add(map4);
    			
    			ReportDefination dateQualifierData=reportDefinationRepository.findByReportIdAndDataType(repData.getId(), "DATE_QUALIFIER");
    			HashMap map5=new HashMap();
    			map5.put("Parameter", "Date qualifier");
				map5.put("Value", dateQualifierData.getDisplayName());
    			additionalParamsList.add(map5);
			}
			}
			
			//map.put("additionalParams", additionalParamsList);
			
			if(reqInfo.getFilterMap()!=null)
			{
				JSONParser parser = new JSONParser();
				String paramVal=reqInfo.getFilterMap();
				JSONObject jsonValue = (JSONObject) parser.parse(paramVal);
				jsonValue.put("additionalParams", additionalParamsList);
				jsonValue.put("reportConditionsList", reportCondList);
				map.put("filterMap", jsonValue);
			}
			else
				map.put("filterMap", "");			
		}
    	
    	return map;
    }
    

    /**
     * Author: Ravali
     * 		   Swetha: Revamped custom pagination
     * @param request
     * @param repId
     * @param response
     * @param pageNumber
     * @param pageSize
     * @param requestType
     * @param sortDirection
     * @param sortCol
     * @return
     * @throws URISyntaxException
     * @throws ParseException
     */
    @GetMapping("/reportRequestByTenantIdOrReqId")
    @Timed
    public List<LinkedHashMap> reportRequestByTenantIdOrReqIdNew(HttpServletRequest request,@RequestParam(value="repId") String repId,HttpServletResponse response,
    		@RequestParam(value = "page" , required = false) Integer pageNumber,@RequestParam(value = "per_page", required = false) Integer pageSize,
    		@RequestParam(value = "requestType", required = false) String requestType,@RequestParam(required = false) String sortDirection,
    		@RequestParam(required = false) String sortCol) throws URISyntaxException, ParseException {

    	HashMap map0=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map0.get("tenantId").toString());

    	log.info("rest request to get reportRequestByTenantIdOrReqId :"+tenantId);
    	List<ReportRequests> requests=new ArrayList<ReportRequests>();
    	Reports repData=reportsRepository.findByTenantIdAndIdForDisplay(tenantId, repId);
    	Long reportId=repData.getId();
    	List<ReportRequests> totalRequestListForReport=reportRequestsRepository.findByReportId(reportId);
    	int xCount=0;
    	if(totalRequestListForReport!=null && !(totalRequestListForReport.isEmpty()))
    		xCount=totalRequestListForReport.size();

    	log.info("Total No of Requests for ReportId: "+reportId+" is: "+xCount);
    	if(sortDirection==null)
    		sortDirection="Descending";
    	if(sortCol==null)
    		sortCol="Id";

    	List<String> relationList = new ArrayList<String>();
    	List<BigInteger> filteredDvList = new ArrayList<BigInteger>();

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


    	if(sortDirection==null)
    		sortDirection="Descending";

    	List<DataViews> dvList=new ArrayList<DataViews>();
    	List<ReportRequests> reportRequestList=new ArrayList<ReportRequests>();
    	reportRequestList=totalRequestListForReport.subList(startIndex, limit);

    	List<LinkedHashMap> requestList=new ArrayList<LinkedHashMap>();
    	for(int i=0;i<reportRequestList.size();i++)
    	{
    		LinkedHashMap map=new LinkedHashMap();
    		if(reportRequestList.get(i)!=null)
    		{
    			if(reportRequestList.get(i).getId()!=null){
    				ReportRequests reqDataInp=reportRequestsRepository.findOne(reportRequestList.get(i).getId());
    				map.put("reqId",reqDataInp.getIdForDisplay() );
    			}
    			if(reportRequestList.get(i).getReqName()!=null)
    				map.put("reqName", reportRequestList.get(i).getReqName());
    			if(reportRequestList.get(i).getReportId()!=null){
    				Reports reportsData=reportsRepository.findOne(reportRequestList.get(i).getReportId());
    				map.put("reportId", reportsData.getIdForDisplay());
    			}
    			if(reportRequestList.get(i).getOutputPath()!=null)
    				map.put("outputPath", reportRequestList.get(i).getOutputPath());
    			if(reportRequestList.get(i).getFileName()!=null)
    				map.put("fileName", reportRequestList.get(i).getFileName());
    			if(reportRequestList.get(i).getTenantId()!=null)
    				map.put("tenantId", reportRequestList.get(i).getTenantId());
    			if( reportRequestList.get(i).getStatus()!=null)
    				map.put("status", reportRequestList.get(i).getStatus());
    			if(reportRequestList.get(i).getCreatedDate()!=null)
    				map.put("createdDate", reportRequestList.get(i).getCreatedDate());
    			if(reportRequestList.get(i).getCreatedBy()!=null)
    				map.put("createdBy", reportRequestList.get(i).getCreatedBy());
    			if(reportRequestList.get(i).getLastUpdatedBy()!=null)
    				map.put("lastUpdatedBy", reportRequestList.get(i).getLastUpdatedBy());
    			if(reportRequestList.get(i).getLastUpdatedDate()!=null)
    				map.put("lastUpdatedDate", reportRequestList.get(i).getLastUpdatedDate());
    			if(reportRequestList.get(i).getSubmittedTime()!=null)
    				map.put("submittedTime", reportRequestList.get(i).getSubmittedTime());
    			if(reportRequestList.get(i).getGeneratedTime()!=null)
    				map.put("generatedTime", reportRequestList.get(i).getGeneratedTime());
    			if(reportRequestList.get(i).getJobId()!=null)
    				map.put("jobId", reportRequestList.get(i).getJobId());
    			if(reportRequestList.get(i).getRequestType()!=null)
    				map.put("requestType", reportRequestList.get(i).getRequestType());

    			List<ReportDefination> reportCondList=new ArrayList<ReportDefination>();
    			reportCondList=reportDefinationRepository.fetchReportConditions(repData.getId());

    			List<HashMap> additionalParamsList=new ArrayList<HashMap>();

    			if(repData.getSourceViewId()!=null){
    				DataViews dvData=dataViewsRepository.findOne(repData.getSourceViewId());
    				HashMap map1=new HashMap();
    				map1.put("Parameter", "Data View");
    				map1.put("Value", dvData.getDataViewDispName());
    				additionalParamsList.add(map1);
    			}
    			if(repData.getReportTypeId()!=null){
    				ReportType repType=reportTypeRepository.findOne(repData.getReportTypeId());
    				//log.info("repType: "+repType);
    				HashMap map2=new HashMap();
    				if(repType.getType()!=null && repType.getType().equalsIgnoreCase("AGING_REPORT")){
    					Long buckId=Long.parseLong(repData.getReportVal01().toString());
    					//log.info("buckId: "+buckId);
    					BucketList buckList=bucketListRepository.findOne(buckId);
    					map2.put("Parameter", "Bucket Name");
    					map2.put("Value", buckList.getBucketName());
    					additionalParamsList.add(map2);
    				}
    				else if(repType.getType()!=null && repType.getType().equalsIgnoreCase("ROLL_BACK_REPORT")){
    					String rollBackVal=repData.getReportVal01();
    					HashMap map3=new HashMap();
    					map3.put("Parameter", "Roll Back Type");
    					map3.put("Value", rollBackVal+" Wise("+repData.getReportVal02()+" "+rollBackVal+")");
    					additionalParamsList.add(map3);
    				}

    				if(repType.getType()!=null && (repType.getType().equalsIgnoreCase("AGING_REPORT") || repType.getType().equalsIgnoreCase("ROLL_BACK_REPORT"))){
    					ReportDefination aggregatorData=reportDefinationRepository.findByReportIdAndDataType(repData.getId(), "AGGREGATOR");
    					HashMap map4=new HashMap();
    					map4.put("Parameter", "Agregate Column");
    					map4.put("Value", aggregatorData.getDisplayName());
    					additionalParamsList.add(map4);

    					ReportDefination dateQualifierData=reportDefinationRepository.findByReportIdAndDataType(repData.getId(), "DATE_QUALIFIER");
    					HashMap map5=new HashMap();
    					map5.put("Parameter", "Date qualifier");
    					map5.put("Value", dateQualifierData.getDisplayName());
    					additionalParamsList.add(map5);
    				}
    			}

    			if(reportRequestList.get(i).getFilterMap()!=null)
    			{
    				JSONParser parser = new JSONParser();
    				String paramVal=reportRequestList.get(i).getFilterMap();
    				JSONObject jsonValue = (JSONObject) parser.parse(paramVal);
    				jsonValue.put("additionalParams", additionalParamsList);
    				jsonValue.put("reportConditionsList", reportCondList);
    				map.put("filterMap", jsonValue);
    			}
    			else
    				map.put("filterMap", "");

    			requestList.add(map);
    		}
    	}
    	response.addIntHeader("X-COUNT",xCount);
    	log.info("***endTime**");
    	return requestList;

    }   
    
    /**
     * Author: Swetha
     * Api to validate duplication of ReportName
     * @param tenantId
     * @param reportName
     * @return
     */
    @GetMapping("/validateRequestName")
    @Timed
    public Long validateRequestName(HttpServletRequest request,@RequestParam String requestName){
    	
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
      	Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	log.info("Rest Request to validateRequestName with tenantId: "+tenantId+" and requestName: "+requestName);
    	Long result=0l;
    	Long count=reportRequestsRepository.fetchRequestNameCount(tenantId, requestName);
    	log.info("count of requestName: "+requestName+" is: "+count);
    	if(count>1){
    		
    	}
    	else if(count==1){
    		ReportRequests reportRequests=reportRequestsRepository.findOneByTenantIdAndReqName(tenantId, requestName);
    		result=reportRequests.getId();
    	}
		return result;
    }
    
}
