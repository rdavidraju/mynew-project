package com.nspl.app.web.rest;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.multipart.MultipartFile;

import au.com.bytecode.opencsv.CSVReader;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.domain.MappingSet;
import com.nspl.app.domain.MappingSetValues;
import com.nspl.app.repository.LookUpCodeRepository;
import com.nspl.app.repository.MappingSetRepository;
import com.nspl.app.repository.MappingSetValuesRepository;
import com.nspl.app.security.IDORUtils;
import com.nspl.app.service.ActiveStatusService;
import com.nspl.app.service.FileExportService;
import com.nspl.app.service.UserJdbcService;
import com.nspl.app.web.rest.dto.ErrorReporting;
import com.nspl.app.web.rest.dto.MappingSetAndValuesDTO;
import com.nspl.app.web.rest.util.HeaderUtil;

/**
 * REST controller for managing MappingSet.
 */
@RestController
@RequestMapping("/api")
public class MappingSetResource {

    private final Logger log = LoggerFactory.getLogger(MappingSetResource.class);

    private static final String ENTITY_NAME = "mappingSet";
        
    private final MappingSetRepository mappingSetRepository;
    
    @Inject
    LookUpCodeRepository lookUpCodeRepository;

    @Inject
    MappingSetValuesRepository mappingSetValuesRepository;
    
    @Inject
	UserJdbcService userJdbcService;
    
    @Inject
    ActiveStatusService activeStatusService;
    
    
    @Inject
	FileExportService fileExportService;
    
    public MappingSetResource(MappingSetRepository mappingSetRepository) {
        this.mappingSetRepository = mappingSetRepository;
    }

    /**
     * POST  /mapping-sets : Create a new mappingSet.
     *
     * @param mappingSet the mappingSet to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mappingSet, or with status 400 (Bad Request) if the mappingSet has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/mapping-sets")
    @Timed
    public ResponseEntity<MappingSet> createMappingSet(@RequestBody MappingSet mappingSet) throws URISyntaxException {
        log.debug("REST request to save MappingSet : {}", mappingSet);
        if (mappingSet.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new mappingSet cannot already have an ID")).body(null);
        }
        MappingSet result = mappingSetRepository.save(mappingSet);
        return ResponseEntity.created(new URI("/api/mapping-sets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /mapping-sets : Updates an existing mappingSet.
     *
     * @param mappingSet the mappingSet to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated mappingSet,
     * or with status 400 (Bad Request) if the mappingSet is not valid,
     * or with status 500 (Internal Server Error) if the mappingSet couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/mapping-sets")
    @Timed
    public ResponseEntity<MappingSet> updateMappingSet(@RequestBody MappingSet mappingSet) throws URISyntaxException {
        log.debug("REST request to update MappingSet : {}", mappingSet);
        if (mappingSet.getId() == null) {
        	
            return createMappingSet(mappingSet);
        }
        MappingSet result = mappingSetRepository.save(mappingSet);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, mappingSet.getId().toString()))
            .body(result);
    }

    /**
     * GET  /mapping-sets : get all the mappingSets.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of mappingSets in body
     */
    @GetMapping("/mapping-sets")
    @Timed
    public List<MappingSet> getAllMappingSets() {
        log.debug("REST request to get all MappingSets");
        List<MappingSet> mappingSets = mappingSetRepository.findAll();
        return mappingSets;
    }

    /**
     * GET  /mapping-sets/:id : get the "id" mappingSet.
     *
     * @param id the id of the mappingSet to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mappingSet, or with status 404 (Not Found)
     */
    @GetMapping("/mapping-sets/{id}")
    @Timed
    public ResponseEntity<MappingSet> getMappingSet(@PathVariable Long id) {
        log.debug("REST request to get MappingSet : {}", id);
        MappingSet mappingSet = mappingSetRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(mappingSet));
    }

    /**
     * DELETE  /mapping-sets/:id : delete the "id" mappingSet.
     *
     * @param id the id of the mappingSet to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/mapping-sets/{id}")
    @Timed
    public ResponseEntity<Void> deleteMappingSet(@PathVariable Long id) {
        log.debug("REST request to delete MappingSet : {}", id);
        mappingSetRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    /**
     * GET  /mapping-sets : get all the mappingSets by tenantid
     *
     * @return the ResponseEntity with status 200 (OK) and the list of mappingSets in body
     */
    @GetMapping("/mappingSetsByTenantId")
    @Timed
    public List<MappingSet> getMappingSetsByTenantId(HttpServletRequest request) {
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	
        log.debug("REST request to get all MappingSets by tenant id");
        List<MappingSet> mappingSets = mappingSetRepository.findByTenantId(tenantId);
        log.info("mappingSets size :"+mappingSets.size());
        return mappingSets;
    }
    
    
    /** get active mapping set for a tenant**/
    @GetMapping("/getActiveMappingSetsByTenantId")
    @Timed
    public List<MappingSet> getActiveMappingSetsByTenantId(HttpServletRequest request,@RequestParam(value="purpose",required=false) String purpose) {
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	
        log.debug("REST request to get all MappingSets by tenant id");
        List<MappingSet> mappingSets=new ArrayList<MappingSet>();
        if(purpose!=null)
        	mappingSets= mappingSetRepository.fetchActiveMappingSetsByTenantIdAndPurpose(tenantId,purpose);
        else
        	mappingSets = mappingSetRepository.fetchActiveMappingSetsByTenantId(tenantId);
        return mappingSets;
    }
    
    /**
     * Author: Shiva
     * Description:Posting mapping set and mapping set values
     * @param MappingSetAndValuesDTO
     */
    @PostMapping("/postingMappingSetAndValues")
    @Timed
    public ResponseEntity<HashMap> postingMappingSetAndValues(HttpServletRequest request, @RequestBody MappingSetAndValuesDTO mappingSetDTO) 
    {
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	Long userId=Long.parseLong(map.get("userId").toString());
    	log.info("Rest api for posting mapping set and mapping set values for the tenant: "+tenantId +", user id: "+userId);
    	HashMap finalMap = new HashMap();
    	try{
        	List<MappingSetValues> mpSetValues = mappingSetDTO.getMappingSetValues();
        	// Creating Mapping Set Record
        	MappingSet newMappinSet = new MappingSet();
        	newMappinSet.setTenantId(tenantId);
        	if(mappingSetDTO.getName() != null)
        		newMappinSet.setName(mappingSetDTO.getName());
        	if(mappingSetDTO.getDescription() != null)
        		newMappinSet.setDescription(mappingSetDTO.getDescription());
        	if(mappingSetDTO.getLookUppurpose() != null)
        		newMappinSet.setPurpose(mappingSetDTO.getLookUppurpose());
        	newMappinSet.setCreatedBy(userId);
        	newMappinSet.setLastUpdatedBy(userId);
        	newMappinSet.setCreatedDate(ZonedDateTime.now());
        	newMappinSet.setLastUpdatedDate(ZonedDateTime.now());
        	if(mappingSetDTO.getStartDate() != null)
        		newMappinSet.setStartDate(mappingSetDTO.getStartDate());
        	if(mappingSetDTO.getEndDate() != null)
        		newMappinSet.setEndDate(mappingSetDTO.getEndDate());
        	if(mappingSetDTO.getEnabledFlag() != null)
        		newMappinSet.setEnabledFlag(mappingSetDTO.getEnabledFlag());
        	MappingSet createMpSet = mappingSetRepository.save(newMappinSet);
        	// Creating Mapping Set Values
        	if(mpSetValues.size()>0)
        	{
        		if(createMpSet != null)
        		{
        			for(MappingSetValues mpValue : mpSetValues)
        			{
        				MappingSetValues newMpValue = new MappingSetValues();
        				newMpValue.setMappingSetId(createMpSet.getId());
        				if(mpValue.getSourceValue() != null)
        					newMpValue.setSourceValue(mpValue.getSourceValue());
        				if(mpValue.getTargetValue() != null)
        					newMpValue.setTargetValue(mpValue.getTargetValue());
        				newMpValue.setCreatedBy(userId);
        				newMpValue.setLastUpdatedBy(userId);
        				newMpValue.setCreatedDate(ZonedDateTime.now());
        				newMpValue.setLastUpdatedDate(ZonedDateTime.now());
        				if(mpValue.getStatus() != null)
        					newMpValue.setStatus(mpValue.getStatus());
        				if(mpValue.getStartDate() != null)
        					newMpValue.setStartDate(mpValue.getStartDate());
        				if(mpValue.getEndDate() != null)
        					newMpValue.setEndDate(mpValue.getEndDate());
        				mappingSetValuesRepository.save(newMpValue);
        			}
        		}
        	}
        	finalMap.put("id", createMpSet.getId());
        	finalMap.put("name", createMpSet.getName());
        	return new ResponseEntity(finalMap, HttpStatus.OK);
    	}
    	catch(Exception e)
    	{
    		return new ResponseEntity(finalMap, HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    }
    
    
    /**
     * Author: Shiva
     * Description:Create or Updating Mapping Set Header Level Information
     * @param MappingSet, tenantId, userId
     */
    @PutMapping("/createOrUpdateMappingSet")
    @Timed
    public ResponseEntity<MappingSet> createOrUpdateMappingSet(HttpServletRequest request, @RequestBody MappingSet mappingSet) throws URISyntaxException {
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	Long userId=Long.parseLong(map.get("userId").toString());
    	
        log.debug("REST request to update MappingSet for tenantId: "+tenantId, mappingSet);
        if (mappingSet.getId() == null) { // Creating new Record
        	mappingSet.setTenantId(tenantId);
        	mappingSet.setCreatedBy(userId);
        	mappingSet.setLastUpdatedBy(userId);
        	mappingSet.setCreatedDate(ZonedDateTime.now());
        	mappingSet.setLastUpdatedDate(ZonedDateTime.now());
        	mappingSet.setStartDate(mappingSet.getStartDate());
        	if(mappingSet.getEndDate()!=null)
        	mappingSet.setEndDate(mappingSet.getEndDate());
            return createMappingSet(mappingSet);
        }
        else
        {	// Updating Existing Record
        	mappingSet.setTenantId(tenantId);
        	mappingSet.setLastUpdatedBy(userId);
        	mappingSet.setLastUpdatedDate(ZonedDateTime.now());
        	mappingSet.setStartDate(mappingSet.getStartDate());
        	if(mappingSet.getEndDate()!=null)
        	mappingSet.setEndDate(mappingSet.getEndDate());
            MappingSet result = mappingSetRepository.save(mappingSet);
            return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, mappingSet.getId().toString()))
                .body(result);
        }
    }
    
    /**
     * Author: Shiva
     * Description: Fetching Mapping sets by tenant id order by id desc
     * @Param: tenantId
     * returns : List of Mapping sets
     */
    @GetMapping("/getMappingSetsByTenantId")
    @Timed
    public List<MappingSet> getMappingSetByTenantId(HttpServletRequest request,@RequestParam Long pageNumber, @RequestParam Long pageSize, HttpServletResponse response,@RequestParam(value="purpose",required=false) String purpose) {
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
        log.debug("REST request to get all MappingSets for the tenant: "+ tenantId);
        
		Long limit =  (pageNumber * pageSize + 1) - 1;
		log.info("Page Limit: " + limit + ", Page Number : " + pageNumber);
		List<MappingSet> mappingSets=new ArrayList<MappingSet>();
		List<MappingSet> mappingSetFinalList=new ArrayList<MappingSet>();
		 List<MappingSet> count = new ArrayList<MappingSet>();
        if(purpose!=null)
        {
        	 mappingSets = mappingSetRepository.fetchByTenantIdAndPurposeOrderByIdDesc(tenantId,purpose, limit, pageSize);
        	 count= mappingSetRepository.fetchOnlycoa(tenantId,purpose);
        }
        else
        {
        	mappingSets = mappingSetRepository.fetchByTenantIdOrderByIdDesc(tenantId, limit, pageSize);
        	count= mappingSetRepository.fetchOtherThancoa(tenantId);
        }
        response.addIntHeader("X-COUNT", count.size());
        for(MappingSet  ms:mappingSets)
        {
        	MappingSet msf=new MappingSet();
        	msf.setId(ms.getId());
        	msf.setTenantId(ms.getTenantId());
        	msf.setName(ms.getName());
        	msf.description(ms.getDescription());
        	msf.setCreatedBy(ms.getCreatedBy());
        	msf.setLastUpdatedBy(ms.getLastUpdatedBy());
        	msf.setCreatedDate(ms.getCreatedDate());
        	msf.setLastUpdatedDate(ms.getLastUpdatedDate());
        	msf.setPurpose(ms.getPurpose());
        	msf.setStartDate(ms.getStartDate());
        	msf.setEndDate(ms.getEndDate());
        	/** active check **/
        	//log.info("befor active status check msf :"+msf);
			Boolean activeStatus=activeStatusService.activeStatus(msf.getStartDate(), msf.getEndDate(), ms.getEnabledFlag());
			//log.info("active status "+activeStatus+" at :"+msf.getId());

        	msf.setEnabledFlag(activeStatus);
        	mappingSetFinalList.add(msf);
        	
        }
        return mappingSetFinalList;
    }
    
    
    /**
     * Author: Shiva
     * Description: Fetching Mapping Set And Mapping Set Values Based on MappinSetId
     * @throws IOException 
     * @Param: tenantId
     * returns : MappingSetAndValuesDTO
     */
    @GetMapping("/fetchMappingSetAndValuesById")
    @Timed
    public MappingSetAndValuesDTO fetchMappingSetAndValuesById(@RequestParam(value = "id", required=true) Long id,@RequestParam(value="fileExport",required=false) boolean fileExport,@RequestParam(value="fileType",required=false) String fileType,HttpServletResponse response) throws IOException {
    	log.info("Rest API for fetching mapping set and mapping set values based on mapping set id: "+id);
    	MappingSetAndValuesDTO finalMap = new MappingSetAndValuesDTO();
    	MappingSet mpSet = mappingSetRepository.findOne(id);
    	if(mpSet != null)
    	{				
    		// Fetching Mapping Set(Header Columns)
    		List<HashMap> lookUps = new ArrayList<HashMap>();
    		finalMap.setId(mpSet.getId());
    		finalMap.setTenantId(mpSet.getTenantId());
    		finalMap.setName(mpSet.getName());
    		finalMap.setDescription(mpSet.getDescription());
    		finalMap.setLastUpdatedBy(mpSet.getLastUpdatedBy());
    		finalMap.setCreatedBy(mpSet.getCreatedBy());
    		finalMap.setCreatedDate(mpSet.getCreatedDate());
    		finalMap.setLastUpdatedDate(mpSet.getLastUpdatedDate());
    		finalMap.setStartDate(mpSet.getStartDate());
    		finalMap.setEndDate(mpSet.getEndDate());
    		finalMap.setEnabledFlag(mpSet.getEnabledFlag());
    		//finalMap.setPurpose(mpSet.getPurpose());
    		String[] purpose = mpSet.getPurpose().split("\\,");
    		log.info("Purpose Length: "+ purpose.length);
    		if(purpose.length>0)
    		{
        		for(int i=0; i<purpose.length; i++)
        		{
        			HashMap hm = new HashMap();
        			String lookUpMeaning = lookUpCodeRepository.fetchLookUpMeaningByLookUpCodeAndLookUpTypeAndTenantId(mpSet.getTenantId(), purpose[i], "MAPPING_TYPES");
        			if(lookUpMeaning != null)
        			{
        				hm.put("lookupCode", purpose[i]);
        				hm.put("lookupMeaning", lookUpMeaning);
        			}
        			lookUps.add(hm);
        		}
    		}
    		finalMap.setPurpose(lookUps);
    		/*List<MappingSetValues> mpValues = mappingSetValuesRepository.fetchActiveMappingSetValuesByMappingId(mpSet.getId());*/	// Fetching mapping set values
    		List<MappingSetValues> mpValues = mappingSetValuesRepository.findByMappingSetId(mpSet.getId());
    		log.info("Mapping Rule Values Active Records Sizes: "+ mpValues.size());
    		if(fileExport)
    		{
    			List<LinkedHashMap> mpValuesListMap=new ArrayList<LinkedHashMap>();
    			for(MappingSetValues mpVal:mpValues)
    			{
    				LinkedHashMap mpValMap=new LinkedHashMap();
    				
    				if(fileExport)
    				{

    					mpValMap.put("Name", mpSet.getName());
        				mpValMap.put("Description", mpSet.getDescription());
        				if( mpVal.getSourceValue()!=null)
        					mpValMap.put("Code", mpVal.getSourceValue());
        				if(mpVal.getTargetValue()!=null)
        					mpValMap.put("Value", mpVal.getTargetValue());
        				if(mpVal.getStartDate()!=null)
        					mpValMap.put("startDate", mpVal.getStartDate());
        				if( mpVal.getEndDate()!=null)
        					mpValMap.put("End Date", mpVal.getEndDate());
        				else
        					mpValMap.put("End Date", "");
    				
    			
    				}
    				else
    				{
    					mpValMap.put("name", mpSet.getName());
        				mpValMap.put("description", mpSet.getDescription());

        				if( mpVal.getSourceValue()!=null)
        					mpValMap.put("code", mpVal.getSourceValue());
        				if(mpVal.getTargetValue()!=null)
        					mpValMap.put("value", mpVal.getTargetValue());
        				if(mpVal.getStartDate()!=null)
        					mpValMap.put("startDate", mpVal.getStartDate());
        				if( mpVal.getEndDate()!=null)
        					mpValMap.put("endDate", mpVal.getEndDate());
        				else
        					mpValMap.put("endDate", "");
    					
    					
    				}
    				mpValuesListMap.add(mpValMap);
    			}

    			LinkedHashMap keysList=mpValuesListMap.get(0);

    			Set<String> keyset=keysList.keySet();

    			List<String> keyList = new ArrayList<String>(keyset);
    			log.info("keyList :"+keyList);

    			if(fileType.equalsIgnoreCase("csv"))
    			{
    				response.setContentType ("application/csv");
    				response.setHeader ("Content-Disposition", "attachment; filename=\"valueSet.csv\"");

    				fileExportService.jsonToCSV(mpValuesListMap,keyList,response.getWriter());



    			}
    			if(fileType.equalsIgnoreCase("pdf"))
    			{
    				response.setContentType ("application/pdf");
    				response.setHeader ("Content-Disposition", "attachment; filename=\"valueSet.pdf\"");

    				fileExportService.jsonToCSV(mpValuesListMap, keyList,response.getWriter());



    			}
    			else if(fileType.equalsIgnoreCase("excel"))
    			{
    				/*response.setContentType("application/vnd.ms-excel");
    				response.setHeader(
    						"Content-Disposition",
    						"attachment; filename=\"valueSet.xlsx\""
    						);
    				fileExportService.jsonToCSV(mpValuesListMap, keyList,response.getWriter());*/
    				
    				response=fileExportService.exportToExcel(response, keyList, mpValuesListMap);
    			}

    		}
    		finalMap.setMappingSetValues(mpValues);
    	}
    	return finalMap;
    }
    
    /**
     * Author: Shiva
     * Description: Fetching Mapping Set And Mapping Set Values Based on Purpose
     * @Param: tenantId
     * returns : List<MappingSetAndValuesDTO>
     */
    @GetMapping("/fetchMappingSetAndValuesByPurpose")
    @Timed
    public List<MappingSetAndValuesDTO> fetchMappingSetAndValuesByPurpose(HttpServletRequest request, @RequestParam(value = "purpose", required=true) String purpose) {
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	log.info("Rest api for fetching Mapping Set and Mapping Set Values based on TenantId: "+tenantId+" and Purpose: "+purpose);

    	List<MappingSet> mpSets = mappingSetRepository.findByTenantIdAndPurposeIgnoreCaseContaining(tenantId, purpose);
    	log.info("MappingSets: "+ mpSets.size());
    	List<MappingSetAndValuesDTO> finalMapList = new ArrayList<MappingSetAndValuesDTO>();
    	if(mpSets.size()>0)
    	{
    		for(MappingSet mpSet : mpSets)
    		{
    	    	MappingSetAndValuesDTO finalMap = new MappingSetAndValuesDTO();
        		// Fetching Mapping Set(Header Columns)
        		List<HashMap> lookUps = new ArrayList<HashMap>();
        		finalMap.setId(mpSet.getId());
        		finalMap.setTenantId(mpSet.getTenantId());
        		finalMap.setName(mpSet.getName());
        		finalMap.setDescription(mpSet.getDescription());
        		finalMap.setLastUpdatedBy(mpSet.getLastUpdatedBy());
        		finalMap.setCreatedBy(mpSet.getCreatedBy());
        		finalMap.setCreatedDate(mpSet.getCreatedDate());
        		finalMap.setLastUpdatedDate(mpSet.getLastUpdatedDate());
        		finalMap.setStartDate(mpSet.getStartDate());
        		finalMap.setEndDate(mpSet.getEndDate());
        		finalMap.setEnabledFlag(mpSet.getEnabledFlag());
        		//finalMap.setPurpose(mpSet.getPurpose());
        		String[] purposes = mpSet.getPurpose().split("\\,");
        		log.info("Purpose Length: "+ purposes.length);
        		if(purposes.length>0)
        		{
            		for(int i=0; i<purposes.length; i++)
            		{
            			HashMap hm = new HashMap();
            			String lookUpMeaning = lookUpCodeRepository.fetchLookUpMeaningByLookUpCodeAndLookUpTypeAndTenantId(mpSet.getTenantId(), purposes[i], "MAPPING_TYPES");
            			if(lookUpMeaning != null)
            			{
            				hm.put("lookupCode", purposes[i]);
            				hm.put("lookupMeaning", lookUpMeaning);
            			}
            			lookUps.add(hm);
            		}
        		}
        		finalMap.setPurpose(lookUps);
        		/*List<MappingSetValues> mpValues = mappingSetValuesRepository.fetchActiveMappingSetValuesByMappingId(mpSet.getId());*/	// Fetching mapping set values
        		List<MappingSetValues> mpValues = mappingSetValuesRepository.findByMappingSetId(mpSet.getId());
        		log.info("Mapping Rule Values Active Records Sizes: "+ mpValues.size());
        		finalMap.setMappingSetValues(mpValues);
        		finalMapList.add(finalMap);
    		}
    	}
    	return finalMapList;
    }
    
    /**
     * Author: Shiva
     * Description: Bulk uploading mapping sets and mapping set values
     * @throws IOException 
     * @Param: tenantId, userId
     */
    @PostMapping("/bulkUploadForMappingSet")
    @Timed
    public ErrorReporting bulkUploadForMappingSet(@RequestParam MultipartFile multipart, HttpServletRequest request) throws IOException
    {
    	HashMap map = userJdbcService.getuserInfoFromToken(request);
    	Long tenantId = Long.parseLong(map.get("tenantId").toString());
    	Long userId = Long.parseLong(map.get("tenantId").toString());
    	log.info("Bulk upload API for mapping set for the tenant id: "+tenantId);
    	ErrorReporting errorReport  = new ErrorReporting();
    	List<String> reasons = new ArrayList<String>();
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
    	HashMap<String, List<HashMap>> mappingData = new HashMap<String, List<HashMap>>();
    	errorReport.setStatus("Success");
    	try{
    		CSVReader csvReader = new CSVReader(new InputStreamReader(multipart.getInputStream()), ',' , '"');
    		int nameIndx = 0; int desIndx = 1; int purposeIndx = 4; int sDateIndx = 2; int eDateIndx = 3; int sValueIndx = 5; int tValueIndx = 6;
    		int strtDtIndx = 7; int endDtIndx = 8;
			List<String[]> allRows = csvReader.readAll();
			log.info("All Rows Size: "+ allRows.size());
			if(allRows.size()>1)
			{
				log.info("Data Validation Started...");
				List<String> names = new ArrayList<String>();
				for(int j=1; j<allRows.size(); j++)
				{
					try
					{
						String[] row = allRows.get(j);
						if(row[nameIndx] != null && !"NULL".equalsIgnoreCase(row[nameIndx].toString()) && row[nameIndx].toString().length()>0)
						{
							if(names.contains(row[nameIndx].toString().trim()))
							{
								List<HashMap> existedData = mappingData.get(row[nameIndx].toString().trim());
								HashMap mpdData = new HashMap();
								mpdData.put("name", row[nameIndx].toString().trim());
								if(row[desIndx] != null && !"NULL".equalsIgnoreCase(row[desIndx].toString()) && row[desIndx].toString().length()>0)
									mpdData.put("description", row[desIndx].toString().trim());
								if(row[purposeIndx] != null && !"NULL".equalsIgnoreCase(row[purposeIndx].toString()) && row[purposeIndx].toString().length()>0)
								{	mpdData.put("purpose", row[purposeIndx].toString().trim());}
								else
								{								
									errorReport.setStatus("Failed");
									reasons.add("Invalid purpose exist in row "+(j+1));
								}
								if(row[sValueIndx] != null && !"NULL".equalsIgnoreCase(row[sValueIndx].toString()) && row[sValueIndx].toString().length()>0)
								{ mpdData.put("sourceValue", row[sValueIndx].toString().trim()); }
								else
								{
									errorReport.setStatus("Failed");
									reasons.add("Invalid source value exist in row "+(j+1));
								}
								if(row[tValueIndx] != null && !"NULL".equalsIgnoreCase(row[tValueIndx].toString()) && row[tValueIndx].toString().length()>0)
								{ mpdData.put("targetValue", row[tValueIndx].toString().trim()); }
								else
								{	
									errorReport.setStatus("Failed");
									reasons.add("Invalid target value exist in row "+(j+1));
								}
								mpdData.put("startDate", LocalDate.parse(row[sDateIndx], formatter));
								if(row[eDateIndx].toString().length() == 0 || "null".equalsIgnoreCase(row[eDateIndx]))
								{	mpdData.put("endDate", null);	}
								else if(row[eDateIndx].toString().length()>0)
								{	
									mpdData.put("endDate", LocalDate.parse(row[eDateIndx].toString(), formatter));
								}
								mpdData.put("startDateChild", LocalDate.parse(row[strtDtIndx], formatter));
								mpdData.put("endDateChild", LocalDate.parse(row[endDtIndx].toString(), formatter));
								
								existedData.add(mpdData);
								mappingData.put(row[nameIndx].toString().trim(), existedData);
							}
							else
							{
								names.add(row[nameIndx].toString().trim());
								List<HashMap> mpDataList = new ArrayList<HashMap>();
								HashMap mpData = new HashMap();
								mpData.put("name", row[nameIndx].toString().trim());
								if(row[desIndx] != null && !"NULL".equalsIgnoreCase(row[desIndx].toString()) && row[desIndx].toString().length()>0)
									mpData.put("description", row[desIndx].toString().trim());
								if(row[purposeIndx] != null && !"NULL".equalsIgnoreCase(row[purposeIndx].toString()) && row[purposeIndx].toString().length()>0)
								{	mpData.put("purpose", row[purposeIndx].toString().trim());}
								else
								{								
									errorReport.setStatus("Failed");
									reasons.add("Invalid purpose exist in row "+(j+1));
								}
								if(row[sValueIndx] != null && !"NULL".equalsIgnoreCase(row[sValueIndx].toString()) && row[sValueIndx].toString().length()>0)
								{ mpData.put("sourceValue", row[sValueIndx].toString().trim()); }
								else
								{
									errorReport.setStatus("Failed");
									reasons.add("Invalid source value exist in row "+(j+1));
								}
								if(row[tValueIndx] != null && !"NULL".equalsIgnoreCase(row[tValueIndx].toString()) && row[tValueIndx].toString().length()>0)
								{ mpData.put("targetValue", row[tValueIndx].toString().trim()); }
								else
								{	
									errorReport.setStatus("Failed");
									reasons.add("Invalid target value exist in row "+(j+1));
								}
								mpData.put("startDate", LocalDate.parse(row[sDateIndx], formatter));
								if(row[eDateIndx].toString().length() == 0 || "null".equalsIgnoreCase(row[eDateIndx]))
								{	mpData.put("endDate", null);	}
								else if(row[eDateIndx].toString().length()>0)
								{	
									mpData.put("endDate", LocalDate.parse(row[eDateIndx].toString(), formatter));
								}
								mpData.put("startDateChild", LocalDate.parse(row[strtDtIndx], formatter));
								mpData.put("endDateChild", LocalDate.parse(row[endDtIndx].toString(), formatter));
								
								mpDataList.add(mpData);
								mappingData.put(row[nameIndx].toString().trim(), mpDataList);	// Key is MappingSet name, Values is MappingSet Data
							}
						}
						else
						{
							errorReport.setStatus("Failed");
							reasons.add("Invalid name exist in row "+(j+1));
						}
					}
					catch(DateTimeParseException e)
					{
						errorReport.setStatus("Failed");
						reasons.add("Invalid date formate in row "+(j+1)+", Plase Use 'dd/mm/yyyy' format");
					}
					catch(Exception e)
					{
						errorReport.setStatus("Failed");
						reasons.add("Un able to read the row "+(j+1));
					}
				}
				log.info("Data Validation End.");
				log.info("Validation Status: "+ errorReport.getStatus());
				if("Success".equalsIgnoreCase(errorReport.getStatus()))
				{
					log.info("Moving data into the database...");
					try{
						if(mappingData.size()>0)
						{
							for(String name : mappingData.keySet())
							{
								List<HashMap> values = mappingData.get(name);
								HashMap mpSet = values.get(0);
								Long mappingSetId = null;
								MappingSet mappingSet = mappingSetRepository.findByTenantIdAndName(tenantId, name);
								// Updating existing records
								if(mappingSet != null)
								{
									if(mpSet.get("description") != null)
										mappingSet.setDescription(mpSet.get("description").toString());
									mappingSet.setLastUpdatedBy(userId);
									mappingSet.setLastUpdatedDate(ZonedDateTime.now());
									if(mpSet.get("purpose") != null)
										mappingSet.setPurpose(mpSet.get("purpose").toString());
									if(mpSet.get("startDate") != null)
									{
										LocalDate localDate = LocalDate.parse(mpSet.get("startDate").toString());
										mappingSet.setStartDate(localDate.atStartOfDay(ZoneId.systemDefault()));
										/*mappingSet.setStartDate(ZonedDateTime.parse(mpSet.get("startDate").toString()));*/										
									}
									if(mpSet.get("endDate") != null)
									{
										LocalDate localDate = LocalDate.parse(mpSet.get("endDate").toString());
										mappingSet.setEndDate(localDate.atStartOfDay(ZoneId.systemDefault()));
										/*mappingSet.setEndDate(ZonedDateTime.parse(mpSet.get("endDate").toString()));*/
									}
									MappingSet mpSetSave = mappingSetRepository.save(mappingSet);
									mappingSetId = mpSetSave.getId();
								}
								else
								{
									MappingSet mappingSetNew = new MappingSet();
									mappingSetNew.setTenantId(tenantId);
									if(mpSet.get("name") != null)
										mappingSetNew.setName(mpSet.get("name").toString());
									if(mpSet.get("description") != null)
										mappingSetNew.setDescription(mpSet.get("description").toString());
									mappingSetNew.setCreatedBy(userId);
									mappingSetNew.setLastUpdatedBy(userId);
									mappingSetNew.setCreatedDate(ZonedDateTime.now());
									mappingSetNew.setLastUpdatedDate(ZonedDateTime.now());
									if(mpSet.get("purpose") != null)
										mappingSetNew.setPurpose(mpSet.get("purpose").toString());
									if(mpSet.get("startDate") != null)
									{
										LocalDate localDate = LocalDate.parse(mpSet.get("startDate").toString());
										mappingSetNew.setStartDate(localDate.atStartOfDay(ZoneId.systemDefault()));
										/*mappingSetNew.setStartDate(ZonedDateTime.parse(mpSet.get("startDate").toString()));*/
									}
										mappingSetNew.setEnabledFlag(true);
									if(mpSet.get("endDate") != null)
									{
										LocalDate localDate = LocalDate.parse(mpSet.get("endDate").toString());
										mappingSetNew.setEndDate(localDate.atStartOfDay(ZoneId.systemDefault()));
										/*mappingSetNew.setEndDate(ZonedDateTime.parse(mpSet.get("endDate").toString()));*/
									}
									MappingSet mpSetSave = mappingSetRepository.save(mappingSetNew);
									mappingSetId = mpSetSave.getId();
								}
								for(HashMap data : values)
								{
									MappingSetValues mpValues = new MappingSetValues();
									mpValues.setMappingSetId(mappingSetId);
									if(data.get("sourceValue") != null)
										mpValues.setSourceValue(data.get("sourceValue").toString());
									if(data.get("targetValue") != null)
										mpValues.setTargetValue(data.get("targetValue").toString());
									mpValues.setCreatedBy(userId);
									mpValues.setLastUpdatedBy(userId);
									mpValues.setCreatedDate(ZonedDateTime.now());
									mpValues.setLastUpdatedDate(ZonedDateTime.now());
									mpValues.setStatus(true);
									if(data.get("startDateChild") != null)
									{
										/*mpValues.setStartDate(ZonedDateTime.parse(data.get("startDateChild").toString()));*/
										LocalDate localDate = LocalDate.parse(data.get("startDateChild").toString());
										mpValues.setStartDate(localDate.atStartOfDay(ZoneId.systemDefault()));
									}
									if(data.get("endDateChild") != null)
									{
										/*mpValues.setEndDate(ZonedDateTime.parse(data.get("endDateChild").toString()));*/
										LocalDate localDate = LocalDate.parse(data.get("endDateChild").toString());
										mpValues.setEndDate(localDate.atStartOfDay(ZoneId.systemDefault()));
									}
									MappingSetValues mpSetValuesSave = mappingSetValuesRepository.save(mpValues);
								}
							}
						}
					}
					catch(Exception e)
					{
						log.info("Error occured while inserting data: "+e);
						errorReport.setStatus("Failed");
						reasons.add("Error occured while inserting data");
					}
				}
			}
    	}
    	catch(Exception e)
    	{
    		errorReport.setStatus("Failed");
    		reasons.add("Error while reading data");
    	}
    	errorReport.setReasons(reasons);
    	return errorReport;    	
    }
    
    /**
     * Author: Shiva
     * Description: Bulk uploading mapping sets and mapping set values
     * @Param: tenantId, userId
     */
    @PostMapping("/mappingSetBulkUpload")
    @Timed
    public ErrorReporting mappingSetBulkUpload(@RequestParam MultipartFile multipart, @RequestParam Long tenantId, @RequestParam Long userId)
    {
    	log.info("Rest api for bulk uploading mapping sets and mapping sets values for the teanant id: "+ tenantId +", File name: "+multipart.getOriginalFilename());
    	ErrorReporting errorReport = new ErrorReporting();
    	List<String> reasons = new ArrayList<String>();
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
    	List<Long> msIds = new ArrayList<Long>();
    	List<Long> msValueIds = new ArrayList<Long>();
    	try{
    		CSVReader csvReader = new CSVReader(new InputStreamReader(multipart.getInputStream()), ',' , '"');
    		int nameIndx = 0; int desIndx = 0; int purposeIndx = 0; int sValueIndx = 0; int tValueIndx = 0; int sDateIndx = 0; int eDateIndx = 0;
			List<String[]> allRows = csvReader.readAll();
			log.info("All Rows Size: "+ allRows.size());
			if(allRows.size()>0)
			{
				log.info("Getting Header Row Indexes...");
				String[] header = allRows.get(0);
				for(int i=0; i<header.length; i++)
				{
					if("name".equalsIgnoreCase(header[i].toString()))
						nameIndx=i;
					else if("description".equalsIgnoreCase(header[i].toString()))
						desIndx=i;
					else if("purpose".equalsIgnoreCase(header[i].toString()))
						purposeIndx=i;
					else if("source_value".equalsIgnoreCase(header[i].toString()))
						sValueIndx=i;
					else if("target_value".equalsIgnoreCase(header[i].toString()))
						tValueIndx=i;
					else if("start_date".equalsIgnoreCase(header[i].toString()))
						sDateIndx=i;
					else if("end_date".equalsIgnoreCase(header[i].toString()))
						eDateIndx=i;
				}
				log.info("Filed Indexes: name["+nameIndx+"], description["+desIndx+"], purpose["+purposeIndx+"], source_value["+sValueIndx+"], target_value["+tValueIndx+"], "
						+ "start_date["+sDateIndx+"], end_date["+eDateIndx+"]");

				if(allRows.size()>1)
				{
					log.info("Reading data...");
					List<String> names = new ArrayList<String>();
					for(int j=1; j<allRows.size(); j++)
					{
						try{
							String[] row = allRows.get(j);
							if(!"NULL".equalsIgnoreCase(row[nameIndx].toString()) || !(row[nameIndx].toString().isEmpty()))
							{
								if(names.contains(row[nameIndx].toString().trim()))
								{
									//log.info("name: "+row[nameIndx].toString()+" existed");
									MappingSet ms = mappingSetRepository.findByTenantIdAndName(tenantId, row[nameIndx].toString());
									if(ms != null)
									{
										MappingSetValues msValues = new MappingSetValues();
										msValues.setMappingSetId(ms.getId());
										if(!"NULL".equalsIgnoreCase(row[sValueIndx].toString()) || !(row[sValueIndx].toString().isEmpty()))
											msValues.setSourceValue(row[sValueIndx].toString());
										if(!"NULL".equalsIgnoreCase(row[tValueIndx].toString()) || !(row[tValueIndx].toString().isEmpty()))
											msValues.setTargetValue(row[tValueIndx].toString());
										msValues.setCreatedBy(userId);
										msValues.setCreatedDate(ZonedDateTime.now());
										msValues.setLastUpdatedBy(userId);
										msValues.setLastUpdatedDate(ZonedDateTime.now());
										if(row[sDateIndx] != null || row[sDateIndx] == null)
											msValues.setStartDate(ZonedDateTime.parse(row[sDateIndx].toString(), formatter));
										if(row[eDateIndx] != null)
										{
											msValues.setEndDate(ZonedDateTime.parse(row[eDateIndx].toString(), formatter));
										}
										else if("NULL".equalsIgnoreCase(row[eDateIndx].toString()) || (row[eDateIndx].toString().isEmpty()))
										{
											msValues.setEndDate(null);
										}
										MappingSetValues createMSValues = mappingSetValuesRepository.save(msValues);
										msValueIds.add(createMSValues.getId());
									}
								}
								else
								{
									//log.info("Saving "+row[nameIndx].toString()+" in mapping set table...");
									MappingSet mappingSet = new MappingSet();
									mappingSet.setTenantId(tenantId);
									mappingSet.setName(row[nameIndx].toString());
									mappingSet.setCreatedBy(userId);
									mappingSet.setCreatedDate(ZonedDateTime.now());
									mappingSet.setLastUpdatedBy(userId);
									mappingSet.setLastUpdatedDate(ZonedDateTime.now());
									mappingSet.setEnabledFlag(true);
									if(!"NULL".equalsIgnoreCase(row[desIndx].toString()) || !(row[desIndx].toString().isEmpty()))
										mappingSet.setDescription(row[desIndx].toString());
									if(!"NULL".equalsIgnoreCase(row[purposeIndx].toString()) || !(row[purposeIndx].toString().isEmpty()))
										mappingSet.setPurpose(row[purposeIndx].toString());
									if(!"NULL".equalsIgnoreCase(row[sDateIndx].toString()) || !(row[sDateIndx].toString().isEmpty()))
										mappingSet.setStartDate(ZonedDateTime.parse(row[sDateIndx].toString(), formatter));
									if(!"NULL".equalsIgnoreCase(row[eDateIndx].toString()) || !(row[eDateIndx].toString().isEmpty()))
									{
										mappingSet.setEndDate(ZonedDateTime.parse(row[eDateIndx].toString(), formatter));
									}
									else if("NULL".equalsIgnoreCase(row[eDateIndx].toString()) || (row[eDateIndx].toString().isEmpty()))
									{
										mappingSet.setEndDate(null);
									}
									MappingSet ms = mappingSetRepository.findByTenantIdAndName(tenantId, row[nameIndx].toString());
									MappingSet createMappingSet = new MappingSet();
									if(ms != null)
									{
										mappingSet.setId(ms.getId());
										createMappingSet = mappingSetRepository.save(mappingSet);
										msIds.add(createMappingSet.getId());
									}
									else
									{
										createMappingSet = mappingSetRepository.save(mappingSet);
										msIds.add(createMappingSet.getId());
									}
									
									MappingSetValues msValues = new MappingSetValues();
									msValues.setMappingSetId(createMappingSet.getId());
									if(!"NULL".equalsIgnoreCase(row[sValueIndx].toString()) || !(row[sValueIndx].toString().isEmpty()))
										msValues.setSourceValue(row[sValueIndx].toString());
									if(!"NULL".equalsIgnoreCase(row[tValueIndx].toString()) || !(row[tValueIndx].toString().isEmpty()))
										msValues.setTargetValue(row[tValueIndx].toString());
									msValues.setCreatedBy(userId);
									msValues.setCreatedDate(ZonedDateTime.now());
									msValues.setLastUpdatedBy(userId);
									msValues.setLastUpdatedDate(ZonedDateTime.now());
									if(!"NULL".equalsIgnoreCase(row[sDateIndx].toString()) || !(row[sDateIndx].toString().isEmpty()))
										msValues.setStartDate(ZonedDateTime.parse(row[sDateIndx].toString(), formatter));
									if(!"NULL".equalsIgnoreCase(row[eDateIndx].toString()) || !(row[eDateIndx].toString().isEmpty()))
									{
										msValues.setEndDate(ZonedDateTime.parse(row[eDateIndx].toString(), formatter));
									}
									else if("NULL".equalsIgnoreCase(row[eDateIndx].toString()) || (row[eDateIndx].toString().isEmpty()))
									{
										msValues.setEndDate(null);
									}
									MappingSetValues createMSValues = mappingSetValuesRepository.save(msValues);
									msValueIds.add(createMSValues.getId());
									names.add(row[nameIndx].toString());
								}
							}
							errorReport.setStatus("Success");
						}
						catch(DateTimeParseException e)
						{
							errorReport.setStatus("Failed");
							reasons.add("Invalid date format found in row : "+ j);
							break;
						}
						catch(Exception e)
						{
							errorReport.setStatus("Failed");
							reasons.add("Un able to read the row: "+ j);
							break;
						}
					}
				}
				else 
				{
					errorReport.setStatus("Failed");
					reasons.add("There is no data present in the file.");
				}
			}
			else
			{
				errorReport.setStatus("Failed");
				reasons.add("You have been uploaded empty file.");
			}
			if("Failed".equalsIgnoreCase(errorReport.getStatus()))
			{
				if(msIds.size()>0)
	    		{
	    			for(Long id : msIds)
	    			{
	    				mappingSetRepository.delete(id);
	    			}
	    		}
	    		if(msValueIds.size()>0)
	    		{
	    			for(Long id : msValueIds)
	    			{
	    				mappingSetValuesRepository.delete(id);
	    			}
	    		}
			}
    	}
    	catch(Exception e)
    	{
    		log.info("Exception: "+e);
    	}
    	errorReport.setReasons(reasons);
    	return errorReport;
    }
}
