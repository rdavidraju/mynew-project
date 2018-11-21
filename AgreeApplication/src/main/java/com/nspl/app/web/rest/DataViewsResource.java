package com.nspl.app.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import io.github.jhipster.web.util.ResponseUtil;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
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
import javax.sql.DataSource;

import org.json.JSONException;
import org.json.JSONString;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
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
import com.nspl.app.domain.DataViewFilters;
import com.nspl.app.domain.DataViewUnion;
import com.nspl.app.domain.DataViews;
import com.nspl.app.domain.DataViewsColumns;
import com.nspl.app.domain.DataViewsSrcMappings;
import com.nspl.app.domain.FileTemplateLines;
import com.nspl.app.domain.FileTemplates;
import com.nspl.app.domain.LookUpCode;
import com.nspl.app.repository.DataViewConditionsRepository;
import com.nspl.app.repository.DataViewFiltersRepository;
import com.nspl.app.repository.DataViewUnionRepository;
import com.nspl.app.repository.DataViewsColumnsRepository;
import com.nspl.app.repository.DataViewsRepository;
import com.nspl.app.repository.DataViewsSrcMappingsRepository;
import com.nspl.app.repository.FileTemplateLinesRepository;
import com.nspl.app.repository.FileTemplatesRepository;
import com.nspl.app.repository.LookUpCodeRepository;
import com.nspl.app.repository.search.DataViewsColumnsSearchRepository;
import com.nspl.app.repository.search.DataViewsSearchRepository;
import com.nspl.app.security.IDORUtils;
import com.nspl.app.service.ActiveStatusService;
import com.nspl.app.service.DataViewsService;
import com.nspl.app.service.ElasticSearchColumnNamesService;
import com.nspl.app.service.ExcelFunctionsService;
import com.nspl.app.service.PropertiesUtilService;
import com.nspl.app.service.UserJdbcService;
import com.nspl.app.web.rest.dto.DataViewsToViewColumnsDTO;
import com.nspl.app.web.rest.util.HeaderUtil;
import com.nspl.app.web.rest.util.LocalDateTypeConverter;
import com.nspl.app.web.rest.util.PaginationUtil;
import com.google.gson.ExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * REST controller for managing DataViews.
 */
@RestController
@RequestMapping("/api")
public class DataViewsResource {

	private final Logger log = LoggerFactory.getLogger(DataViewsResource.class);

	private static final String ENTITY_NAME = "dataViews";

	private final DataViewsRepository dataViewsRepository;

	private final DataViewsSearchRepository dataViewsSearchRepository;

	@Inject
	DataViewsColumnsRepository dataViewsColumnsRepository;

	@Inject
	DataViewFiltersRepository dataViewFiltersRepository;

	@Inject
	FileTemplatesRepository fileTemplatesRepository;

	@Inject
	FileTemplateLinesRepository fileTemplateLinesRepository;

	@Inject
	DataViewConditionsRepository dataViewConditionsRepository;

	@Inject
	ExcelFunctionsService excelFunctionsService;

	@Inject
	DataViewsService dataViewsService;

	@Inject
	PropertiesUtilService propertiesUtilService;

	@Inject
	DataViewsSrcMappingsRepository dataViewsSrcMappingsRepository;

	@Inject
	DataViewUnionRepository dataViewUnionRepository;

	@Inject
	ElasticSearchColumnNamesService elasticSearchColumnNamesService;

	@Inject
	DataViewsColumnsSearchRepository dataViewsColumnsSearchRepository;

	@Inject
	LookUpCodeRepository lookUpCodeRepository;

	@Inject
	UserJdbcService userJdbcService;

	@Inject
	ActiveStatusService activeStatusService;

	public DataViewsResource(DataViewsRepository dataViewsRepository, DataViewsSearchRepository dataViewsSearchRepository) {
		this.dataViewsRepository = dataViewsRepository;
		this.dataViewsSearchRepository = dataViewsSearchRepository;
	}

	@PostMapping("/data-views")
	@Timed
	public ResponseEntity<DataViews> createDataViews(@RequestBody DataViews dataViews, @RequestParam Long tenanatId, @RequestParam Long userId) throws URISyntaxException {
		log.debug("REST request to save DataViews : {}", dataViews);
		if (dataViews.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new dataViews cannot already have an ID")).body(null);
		}
		dataViews.setCreatedBy(userId);
		dataViews.setCreationDate(ZonedDateTime.now());
		dataViews.setTenantId(tenanatId);
		DataViews result = dataViewsRepository.save(dataViews);
		dataViewsSearchRepository.save(result);
		return ResponseEntity.created(new URI("/api/data-views/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /data-views : Updates an existing dataViews.
	 *
	 * @param dataViews the dataViews to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated dataViews,
	 * or with status 400 (Bad Request) if the dataViews is not valid,
	 * or with status 500 (Internal Server Error) if the dataViews couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/data-views")
	@Timed
	public ResponseEntity<DataViews> updateDataViews(@RequestBody DataViews dataViews, @RequestParam Long tenanatId, @RequestParam Long userId) throws URISyntaxException {
		log.debug("REST request to update DataViews : {}", dataViews);
		dataViews.setLastUpdatedBy(userId);
		dataViews.setLastUpdatedDate(ZonedDateTime.now());
		dataViews.setTenantId(tenanatId);
		DataViews result = dataViewsRepository.save(dataViews);
		dataViewsSearchRepository.save(result);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dataViews.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /data-views : get all the dataViews.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of dataViews in body
	 */
	@GetMapping("/data-views")
	@Timed
	public List<DataViews> getAllDataViews() {
		log.debug("REST request to get all DataViews");
		List<DataViews> dataViews = dataViewsRepository.findAll();
		return dataViews;
	}

	/**
	 * GET  /data-views/:id : get the "id" dataViews.
	 *
	 * @param id the id of the dataViews to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the dataViews, or with status 404 (Not Found)
	 */
	@GetMapping("/data-views/{id}")
	@Timed
	public ResponseEntity<DataViews> getDataViews(@PathVariable Long id) {

		String idForDisplay = IDORUtils.computeFrontEndIdentifier(id.toString());
		log.info("encrypted :"+idForDisplay);
		log.debug("REST request to get DataViews : {}", id);
		DataViews dataViews = dataViewsRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(dataViews));
	}

	/**
	 * DELETE  /data-views/:id : delete the "id" dataViews.
	 *
	 * @param id the id of the dataViews to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/data-views/{id}")
	@Timed
	public ResponseEntity<Void> deleteDataViews(@PathVariable Long id) {
		log.debug("REST request to delete DataViews : {}", id);
		dataViewsRepository.delete(id);
		dataViewsSearchRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH  /_search/data-views?query=:query : search for the dataViews corresponding
	 * to the query.
	 *
	 * @param query the query of the dataViews search 
	 * @return the result of the search
	 */
	@GetMapping("/_search/data-views")
	@Timed
	public ResponseEntity<List<DataViews>> searchDataViews(HttpServletRequest request,@RequestParam(value="filterValue",required=false) String filterValue, @RequestParam(value = "page" , required = false) Integer offset, 
			@RequestParam(value = "per_page", required = false) Integer limit) {
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		log.info("Rest api for fetching data views data from elasticsearch repository for the tenant_id: "+tenantId);
		String query = "";
		query = query + " tenantId: \""+tenantId+"\" ";		// Filtering with tenant id	
		/*    	if(columnName != null && columnValue != null)
    	{
    		 String colName = elasticSearchColumnNamesService.getDataViewColName(columnName);
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
		log.info("query"+query);
		log.info("offset:"+offset+"limit"+limit);
		Page<DataViews> page = dataViewsSearchRepository.search(queryStringQuery(query), PaginationUtil.generatePageRequest(offset, limit));
		HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/data-views");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);

		/*        return StreamSupport
            .stream(dataViewsSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());*/
	}

	/**
	 * Author: Swetha
	 * Description: Retrieve data views based on tenanatId
	 * @param tenantId
	 * @return
	 * @throws URISyntaxException 
	 */
	@GetMapping("/dataViewsByTenanat")
	@Timed
	public List<DataViewsToViewColumnsDTO> getByTenantId( HttpServletRequest request,@RequestParam(value = "page" , required = false) Integer offset,
			@RequestParam(value = "per_page", required = false) Integer limit, HttpServletResponse response) throws URISyntaxException{
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		log.info("REST request to fetch dataViews based on tenantId: "+tenantId);
		List<DataViewsToViewColumnsDTO> dtoList=new ArrayList<DataViewsToViewColumnsDTO>();
		List<DataViews> dvList=new ArrayList<DataViews>();
		int xCount=0;
		dvList=dataViewsRepository.fetchDataViewByTenantIdOrderByDataViewDispNameAsc(tenantId);
		if(dvList!=null && !(dvList.isEmpty())){
		log.info("dvList :"+dvList.size());
		xCount=dvList.size();
		}
		Integer pagelimit=0;
		if(offset == null || offset == 0)
		{
			offset = 0;
		}
		if(limit == null || limit == 0)
		{
			limit = xCount;
		}
		pagelimit = ((offset+1) * limit + 1)-1;
		int startIndex=offset*limit; 

		if(pagelimit>xCount){
			pagelimit=xCount;
		}
		log.info("startIndex: "+startIndex+" limit: "+limit+"pagelimit: "+pagelimit);
		List<DataViews> finalList=dvList.subList(startIndex,pagelimit);
		for(int i=0;i<finalList.size();i++){
			DataViewsToViewColumnsDTO dvDto=new DataViewsToViewColumnsDTO();
			DataViews dv=finalList.get(i);
			dvDto.setId(dv.getIdForDisplay());
			if(dv.getDataViewName()!=null && !dv.getDataViewName().isEmpty())
				dvDto.setDataViewName(dv.getDataViewName());
			if(dv.getDataViewDispName()!=null && !dv.getDataViewDispName().isEmpty())
				dvDto.setDataViewDispName(dv.getDataViewDispName());
			if(dv.getCreatedBy()!=null)
				dvDto.setCreatedBy(dv.getCreatedBy());
			if(dv.getCreationDate()!=null)
				dvDto.setCreationDate(dv.getCreationDate());
			if(dv.getDataViewObject()!=null)
				dvDto.setDataViewObject(dv.getDataViewObject());
			if(dv.getDescription()!=null)
				dvDto.setDescription(dv.getDescription());
			if(dv.isEnabledFlag()!=null)
				dvDto.setEnabledFlag(dv.isEnabledFlag());
			if(dv.getEndDate()!=null)
				dvDto.setEndDate(dv.getEndDate());
			if(dvDto.getEndDate()!=null && dvDto.getEndDate().isBefore(ZonedDateTime.now()))
			{
				dvDto.setEndDated(true);
			}
			else
			{
				dvDto.setEndDated(false);
			}
			if(dv.getLastUpdatedBy()!=null)
				dvDto.setLastUpdatedBy(dv.getLastUpdatedBy());
			if(dv.getLastUpdatedDate()!=null)
				dvDto.setLastUpdatedDate(dv.getLastUpdatedDate());
			if(dv.getStartDate()!=null)
				dvDto.setStartDate(dv.getStartDate());
			if(dv.getTenantId()!=null)
				dvDto.setTenantId(dv.getTenantId());
			List<DataViewsSrcMappings> dvSrcMapList=dataViewsSrcMappingsRepository.findByDataViewIdOrderByTemplateIdAsc(dv.getId());
			if(dvSrcMapList!=null && !(dvSrcMapList.isEmpty())){
				if(dvSrcMapList.size()==1)
					dvDto.setType("Single Template");
				else if(dvSrcMapList.size()>1){
					DataViewsSrcMappings dvSrcMap1=dvSrcMapList.get(0);
					DataViewsSrcMappings dvSrcMap2=dvSrcMapList.get(1);
					if(dvSrcMap1.getRelation()!=null )
					{
						LookUpCode relCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("DV_RELATION", dvSrcMap1.getRelation(), tenantId);
						if(relCode!=null)
							dvDto.setType(relCode.getMeaning());
					}
					else if(dvSrcMap2.getRelation()!=null )
					{
						LookUpCode relCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("DV_RELATION", dvSrcMap2.getRelation(), tenantId);
						if(relCode!=null)
							dvDto.setType(relCode.getMeaning());
					}
				}

			}
			dtoList.add(dvDto);
		}
		response.addIntHeader("X-COUNT", dtoList.size());
		return dtoList;

	}

	/**
	 * Author: Swetha
	 * Description: Posting data view along with List of data view columns 
	 * @param dataViewsToViewColumnsDTO
	 * @param tenantId
	 * @param userId
	 * @throws URISyntaxException
	 */
	@PostMapping("/dataViewsNColumns")
	@Timed
	public void createDataViews(@RequestBody List<DataViewsToViewColumnsDTO> dataViewsToViewColumnsDTOList, HttpServletRequest request) throws URISyntaxException {
		log.info("Rest REquest to post data views and columns");

		HashMap userTenantMap=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(userTenantMap.get("tenantId").toString());
		Long userId=Long.parseLong(userTenantMap.get("userId").toString());

		DataViewsToViewColumnsDTO dataViewsToViewColumnsDTO=dataViewsToViewColumnsDTOList.get(0);
		DataViews dv=new DataViews();
		dv.setDataViewName(dataViewsToViewColumnsDTO.getDataViewName());
		dv.setDataViewDispName(dataViewsToViewColumnsDTO.getDataViewDispName());
		dv.setCreatedBy(userId);
		dv.setCreationDate(ZonedDateTime.now());
		dv.setEnabledFlag(true);
		dv.setLastUpdatedBy(userId);
		dv.setLastUpdatedDate(ZonedDateTime.now());
		dv.setTenantId(tenantId);
		dv.setStartDate(dataViewsToViewColumnsDTO.getStartDate());
		dv.setEndDate(dataViewsToViewColumnsDTO.getEndDate());
		dv.setDescription(dataViewsToViewColumnsDTO.getDescription());
		DataViews dvNew=dataViewsRepository.save(dv);
		dataViewsSearchRepository.save(dvNew);
		Long dvId=dvNew.getId();
		log.info("dvId after saving: "+dvId);

		List<DataViewsColumns> dvcList=new ArrayList<DataViewsColumns>();
		log.info("dataViewsToViewColumnsDTO.getDataViewsColumnsList().size(): "+dataViewsToViewColumnsDTO.getDataViewsColumnsList().size());
		for(int i=0;i<dataViewsToViewColumnsDTO.getDataViewsColumnsList().size();i++){
			DataViewsColumns dvcdto=dataViewsToViewColumnsDTO.getDataViewsColumnsList().get(i);
			DataViewsColumns dvc=new DataViewsColumns();
			dvc.setColDataType(dvcdto.getColDataType());
			dvc.setColumnName(dvcdto.getColumnName());
			dvc.setCreatedBy(userId);
			dvc.setCreationDate(ZonedDateTime.now());
			dvc.setDataViewId(dvId);
			dvc.setLastUpdatedBy(userId);
			dvc.setLastUpdatedDate(ZonedDateTime.now());
			dvc.setRefDvColumn(dvcdto.getRefDvColumn());
			dvc.setRefDvName(dvcdto.getRefDvName());
			dvc.setRefDvName(dvcdto.getRefDvName());
			dvc.setRefDvType(dvcdto.getRefDvType());
			DataViewsColumns dvcNew=dataViewsColumnsRepository.save(dvc);
			dataViewsColumnsSearchRepository.save(dvcNew);
			Long dvcNewId=dvcNew.getId();
			log.info("dvcNewId after data view column save: "+dvcNewId);
		}
	}


	/**
	 * Author: Swetha, Ravali
	 * Description: Retrieve data views along with data view columns based on tenanatId
	 * @param tenantId
	 * @return
	 */
	@GetMapping("/getDataViewsNColsByTenant")
	@Timed
	public List<HashMap> getDataViewsNColsByTenant(@RequestParam(value = "dataViewId", required=false) String dataViewId,HttpServletRequest request) {
		log.info("Rest request to getDataViewsNColsByTenant by dataViewId :"+dataViewId);

		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		List<HashMap> dvTodvcDtoLIst=new ArrayList<HashMap>();

		DataViews dview=dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId,dataViewId);
		if(dataViewId!=null)
		{
			dvTodvcDtoLIst = dataViewsService.fetchDataViewAndColumnsByDvId(dview.getId());

		}
		else
		{
			List<DataViews> dataViewsList=dataViewsRepository.fetchActiveDataViews(tenantId);
			List<DataViewsColumns> dViewcolList=dataViewsColumnsRepository.fetchDataViewColumnsByDataViewIds(tenantId);
			HashMap<Long,List<DataViewsColumns>> dataViewColMap=new HashMap<Long,List<DataViewsColumns>>();    	
			DataViewFilters dviewFilter=new DataViewFilters();
			for(int i =0;i<dViewcolList.size();i++)
			{

				if(dataViewColMap.containsKey(dViewcolList.get(i).getDataViewId()))
				{
					List<DataViewsColumns> dvcs = new ArrayList<DataViewsColumns>();
					dvcs = dataViewColMap.get(dViewcolList.get(i).getDataViewId());
					dvcs.add(dViewcolList.get(i));
					dataViewColMap.put(dViewcolList.get(i).getDataViewId(),dvcs);
				}
				else
				{
					List<DataViewsColumns> dvcs = new ArrayList<DataViewsColumns>();
					dvcs.add(dViewcolList.get(i));
					dataViewColMap.put(dViewcolList.get(i).getDataViewId(),dvcs);
				}

			}
			HashMap<Long,FileTemplates> fTempMap=new HashMap<Long,FileTemplates>();

			List<FileTemplates> fTempList=fileTemplatesRepository.findByTenantId(tenantId);
			for(FileTemplates fTemp:fTempList)
			{
				fTempMap.put(fTemp.getId(), fTemp);
			}
			HashMap<Long,FileTemplateLines> fTempLinesMap=new HashMap<Long, FileTemplateLines>();
			List<FileTemplateLines> ftempLinesList=fileTemplateLinesRepository.fetchFileTemplatesByFileTempId(tenantId);
			for(FileTemplateLines fTempLine:ftempLinesList)
			{
				fTempLinesMap.put(fTempLine.getId(), fTempLine);
			}
			log.info("dataViewsList sz: "+dataViewsList.size());
			for(int i=0;i<dataViewsList.size();i++){
				DataViews dv=dataViewsList.get(i);
				HashMap dvToDvcDto=new HashMap();
				dvToDvcDto.put("id",dv.getIdForDisplay());
				if(dv.getDataViewName()!=null)
					dvToDvcDto.put("dataViewName",dv.getDataViewName());
				if(dv.getDataViewDispName()!=null)
					dvToDvcDto.put("dataViewDispName",dv.getDataViewDispName());
				dvToDvcDto.put("createdBy",dv.getCreatedBy());
				dvToDvcDto.put("creationDate",dv.getCreationDate());
				if(dv.isEnabledFlag()!=null)
					dvToDvcDto.put("enabledFlag",dv.isEnabledFlag());
				dvToDvcDto.put("lastUpdatedBy",dv.getLastUpdatedBy());
				dvToDvcDto.put("lastUpdatedDate",dv.getLastUpdatedDate());
				dvToDvcDto.put("tenantId",dv.getTenantId());
				dvToDvcDto.put("startDate",dv.getStartDate());
				dvToDvcDto.put("endDate",dv.getEndDate());
				if(dv.getDescription()!=null)
					dvToDvcDto.put("description",dv.getDescription());

				List<DataViewsSrcMappings> dataViewSrcMapList=dataViewsSrcMappingsRepository.findByDataViewId(dv.getId());
				DataViewsSrcMappings dvSrcMapng=new DataViewsSrcMappings();
				if(dataViewSrcMapList!=null && dataViewSrcMapList.size()==1){
					dvSrcMapng=dataViewSrcMapList.get(0);
					log.info("Single Template Data View");
				}
				else{
					for(int k=0;k<dataViewSrcMapList.size();k++){
						dvSrcMapng=dataViewSrcMapList.get(k);
						if(dvSrcMapng.getRelation()!=null)
							dvToDvcDto.put("viewRelation", dvSrcMapng.getRelation());
					}
				}



				List<DataViewsColumns> dvcList=new ArrayList<DataViewsColumns>();
				if(dataViewColMap.get(dv.getId())!=null)
					dvcList = dataViewColMap.get(dv.getId());
				List<HashMap> dvcdtoLIst=new ArrayList<HashMap>();

				for(int j=0;j<dvcList.size();j++){
					DataViewsColumns dvcdto=dvcList.get(j);
					HashMap dvc=new HashMap();
					dvc.put("id",dvcdto.getId());
					if(dvcdto.getColDataType()!=null){
						String dataTypeCode=dvcdto.getColDataType();
						LookUpCode dTypeCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("DATA_TYPE",dataTypeCode, tenantId);
						if(dTypeCode!=null){
							dvc.put("colDataType",dataTypeCode);
							dvc.put("colDataTypeMeaning",dTypeCode.getMeaning());
						}
					}
					if(dvcdto.getColumnName()!=null){
						dvc.put("columnName",dvcdto.getColumnName());

					}

					if(dvcdto.getRefDvColumn()!=null){
						//FileTemplateLines ftl=fileTemplateLinesRepository.findByTemplateIdAndMasterTableReferenceColumn(Long.parseLong(dvcdto.getRefDvName()),dvcdto.getColumnName());
						FileTemplateLines ftl=fileTemplateLinesRepository.findOne(Long.parseLong(dvcdto.getRefDvColumn()));
						log.info("ftl for dvc.getRefDvColumn(): "+dvcdto.getRefDvColumn()+" is: "+ftl);
						if(ftl!=null){
							dvc.put("colName",ftl.getColumnHeader());
							dvc.put("columnHeader",ftl.getColumnHeader());
							dvc.put("refDvColumn",dvcdto.getRefDvColumn());
						}
					}
					if(dvcdto.getQualifier()!=null){
						//dvc.put("qualifier", dvcdto.getQualifier());
						String qualifierCode=dvcdto.getQualifier();
						LookUpCode qualCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("RECON_QUALIFIERS",qualifierCode, tenantId);
						if(qualCode!=null){
							dvc.put("qualifier",qualifierCode);
							dvc.put("qualifierMeaning",qualCode.getMeaning());
						}
					}
					dvc.put("createdBy",dvcdto.getCreatedBy());
					dvc.put("creationDate",dvcdto.getCreationDate());
					if(dvcdto.getDataViewId()!=null)
						dvc.put("dataViewId",dview.getIdForDisplay());
					if(dvcdto.getRefDvName()!=null){
						String refDvName=dvcdto.getRefDvName();
						FileTemplates ftemp=fileTemplatesRepository.findOne(Long.parseLong(refDvName));
						if(ftemp!=null && ftemp.getTemplateName()!=null)
							dvc.put("sourceName",ftemp.getTemplateName());
						dvc.put("refDvName",refDvName);
					}
					if(dvcdto.getFormula()!=null)
						dvc.put("formula",dvcdto.getFormula());	
					dvc.put("lastUpdatedBy",dvcdto.getLastUpdatedBy());
					dvc.put("lastUpdatedDate",dvcdto.getLastUpdatedDate());
					if(dvcdto.getRefDvName()!=null)
						dvc.put("refDvName",dvcdto.getRefDvName());
					if(dvcdto.getRefDvType()!=null)
						dvc.put("refDvType",dvcdto.getRefDvType());


					log.info("dvcdto.getRefDvName(): "+dvcdto.getRefDvName()+" dvcdto.getRefDvColumn(): "+dvcdto.getRefDvColumn());

					if(dvSrcMapng.getRelation()!=null && dvSrcMapng.getRelation().equalsIgnoreCase("UNION"))
					{


						List<HashMap> dvUList=new ArrayList<HashMap>();
						List<DataViewUnion> dvunionList=dataViewUnionRepository.findByDataViewLineId(dvcdto.getId());
						for(DataViewUnion dvunion:dvunionList)
						{
							HashMap dvU=new HashMap();
							dvU.put("id", dvunion.getId());
							if(dvunion.getDataViewLineId()!=null)
								dvU.put("dataViewLineId",dvunion.getDataViewLineId());
							if(dvunion.getRefDvType()!=null)
								dvU.put("refDvType",dvunion.getRefDvType());
							if(dvunion.getRefDvName()!=null)
								dvU.put("refDvName",dvunion.getRefDvName());
							if(dvunion.getRefDvColumn()!=null)
								dvU.put("refDvColumn",dvunion.getRefDvColumn());
							dvU.put("createdBy",dvunion.getCreatedBy());
							dvU.put("creationDate", dvunion.getCreationDate());
							dvU.put("lastUpdatedBy", dvunion.getLastUpdatedBy());
							dvU.put("lastUpdatedDate", dvunion.getLastUpdatedDate());
							if(dvunion.getFormula()!=null)
								dvU.put("excelexpressioninputUnion", dvunion.getFormula());
							dvUList.add(dvU);
						}
						dvc.put("src", dvUList);



					}



					/* Filters */
					else
					{
						if(dvcdto.getRefDvName()!=null && dvcdto.getRefDvColumn()!=null){
							DataViewFilters dvf=dataViewFiltersRepository.findByDataViewIdAndRefSrcTypeAndRefSrcIdAndRefSrcColId(dview.getId(), dvcdto.getRefDvType(), Long.parseLong(dvcdto.getRefDvName().toString()), Long.parseLong(dvcdto.getRefDvColumn()));
							if(dvf!=null){
								if(dvf.getFilterOperator()!=null)
									dvc.put("operator", dvf.getFilterOperator());
								if(dvf.getFilterValue()!=null)
									dvc.put("colValue", dvf.getFilterValue());
							}
						}
						else{
							log.info("new column filter are not handled");
						}
					}
					dvcdtoLIst.add(dvc);
				}

				if(dvSrcMapng.getRelation()!=null && dvSrcMapng.getRelation().equalsIgnoreCase("UNION"))
					dvToDvcDto.put("dataViewsUnionColumnsList", dvcdtoLIst);
				else
					dvToDvcDto.put("dataViewsColumnsList",dvcdtoLIst);


				dvToDvcDto.put("dvColumnsList", dvcdtoLIst);

				dvTodvcDtoLIst.add(dvToDvcDto);
			}
		}
		return dvTodvcDtoLIst;

	}   

	/**
	 * Author: Swetha
	 * Description: Api to retrieve result set of a data view
	 * @param viewId
	 * @return 
	 * @throws ClassNotFoundException
	 * @throws SQLException 
	 */    
	@GetMapping("/getViewInfo")
	@Timed
	public List<HashMap> getViewInfo(@RequestParam Long viewId, HttpServletRequest request) throws ClassNotFoundException, SQLException {	

		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());

		log.info("Rest Request to retrieve dataViewInfo for viewId: "+viewId+" and tenantId: "+tenantId);

		Connection conn = null;
		Statement stmt = null;
		Statement stmt2 = null;
		ResultSet result = null;
		ResultSet rs=null;
		List<HashMap> mapList2=new ArrayList<HashMap>();
		try{
			DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
			conn=ds.getConnection();
			log.info("Connected database successfully...");
			stmt = conn.createStatement();

			String viewQuery=dataViewsService.frameQuery(viewId, tenantId);

			stmt2 = conn.createStatement();
			String count = null;
			result=stmt2.executeQuery(viewQuery);

			rs=stmt2.getResultSet();

			ResultSetMetaData rsmd2 = result.getMetaData();
			log.info("col count: "+rsmd2.getColumnCount());
			int columnsNumber = rsmd2.getColumnCount();
			log.info("columnsNumber: "+columnsNumber);
			int columnCount = rsmd2.getColumnCount();
			while(rs.next()){
				HashMap<String,String> map2=new HashMap<String,String>();
				for (int i = 1; i <= columnCount; i++ ) {
					String name = rsmd2.getColumnName(i); 
					for(int t=0,num=1;t<columnsNumber;t++, num++){ 
						String Val=rs.getString(num);
					}
					map2.put(name, rs.getString(i));
				}
				mapList2.add(map2);
			}
		}catch(SQLException se){
			log.info("se: "+se);
		}
		finally{
			result.close();
			rs.close();
			stmt.close();
			stmt2.close();
			conn.close();
		}
		return mapList2;
	}

	/**
	 * Author: Swetha
	 * api to retrieve view and column names and id list by tenantId
	 * @param tenantId
	 * @return
	 */
	@GetMapping("/getDVNColList")
	@Timed
	public List<HashMap> getDataViewNColList(HttpServletRequest request){

		HashMap userTenantInfo=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(userTenantInfo.get("tenantId").toString());

		log.info("Rest Request to getDataViewNColList for Tenanat: "+tenantId);
		List<HashMap> mapList=new ArrayList<HashMap>();

		List<DataViews> dataViewsList=dataViewsRepository.fetchActiveDataViews(tenantId);
		for(int i=0;i<dataViewsList.size();i++){
			DataViews dv=dataViewsList.get(i);
			HashMap map=new HashMap();
			map.put("dataViewId", dv.getId());
			map.put("dataViewName", dv.getDataViewDispName());

			List<DataViewsColumns> dataViewColList=dataViewsColumnsRepository.findByDataViewId(dv.getId());
			if(dataViewColList!=null && dataViewColList.size()!=0){
				List<HashMap> colMapList=new ArrayList<HashMap>();
				for(int j=0;j<dataViewColList.size();j++){
					DataViewsColumns dvc=dataViewColList.get(j);

					HashMap colMap=new HashMap();
					colMap.put("ColumnId", dvc.getRefDvColumn());
					colMap.put("columnDisplayName", dvc.getColumnName());
					colMap.put("userDisplayColName", "");
					colMap.put("columnType", "");
					colMapList.add(colMap);
				}
				map.put("dataViewCols", colMapList);
				mapList.add(map);
			}

		}
		return mapList;

	}

	/**
	 * Author: Swetha
	 * Api to fetch relation wise grouped data
	 * @param tenantId
	 * @return
	 */
	@GetMapping("/dataViewsSideBarData")
	@Timed
	public List<HashMap> dataViewsSideBarData(HttpServletRequest request){

		log.info("Rest Request to get dataViewsSideBarData");
		HashMap userTenantInfo=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(userTenantInfo.get("tenantId").toString());

		List<HashMap> dataViewGrpList=new ArrayList<HashMap>();

		List<String> relationList=dataViewsSrcMappingsRepository.fetchDistinctRelations(tenantId);
		String relation="";
		int count;
		int allCount = 0;
		List dvListForAll= new ArrayList<>();
		HashMap allMap=new HashMap();
		allMap.put("relation", "All");
		dataViewGrpList.add(allMap);
		for(int i=0;i<relationList.size();i++){
			log.info("relationList at i:"+i+" is: "+relationList.get(i));
			if(relationList.get(i)!=null && !(relationList.get(i).isEmpty())){
				HashMap map=new HashMap();
				String relationCode=relationList.get(i);
				log.info("relationCode :"+relationCode);
				LookUpCode lCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("DV_RELATION", relationCode, tenantId);
				relation=lCode.getMeaning();
				List<Object[]> objList=dataViewsRepository.fetchDataViewsByRelation(relation,tenantId);
				List dvList=new ArrayList<>();
				for(int j=0;j<objList.size();j++){
					Object[] obj1=objList.get(j);
					HashMap map1=new HashMap();
					map1.put("idForDisplay", obj1[0]);
					map1.put("dataViewName", obj1[1]);
					map1.put("dataViewDispName", obj1[2]);
					dvList.add(map1);
				}
				count=dvList.size();
				log.info("count: "+count);
				map.put("relation", relation);
				map.put("count", count);
				allCount = allCount + count;
				dvListForAll.addAll(dvList);
				map.put("viewList", dvList);
				dataViewGrpList.add(map);
			}
			else {

				relation="Single Template";
				HashMap map=new HashMap();
				List<DataViews> dvList=dataViewsRepository.fetchSingleTemplateDataViews(tenantId);
				log.info("dvList sz: "+dvList.size());
				List finList=new ArrayList<>();
				map.put("relation", relation);
				Long dvId=0l;
				for(int j=0;j<dvList.size();j++){
					dvId=dvList.get(j).getId();
					List<DataViewsSrcMappings> dvSrcMapList=dataViewsSrcMappingsRepository.findByDataViewIdOrderByTemplateIdAsc(dvId);
					if(dvSrcMapList.size()==1)
					{
						HashMap dvMap=new HashMap();
						dvMap.put("idForDisplay", dvList.get(j).getIdForDisplay());
						dvMap.put("dataViewDispName", dvList.get(j).getDataViewDispName());
						dvMap.put("dataViewName", dvList.get(j).getDataViewName());
						finList.add(dvMap);
					}


				}
				map.put("viewList", finList);
				allCount = allCount + finList.size();
				map.put("count", finList.size());
				dvListForAll.addAll(finList);
				log.info("finList sz: "+finList.size());
				dataViewGrpList.add(map);
			}

		}
		Object[] dvs;
		for(HashMap map : dataViewGrpList)
		{
			if(map.containsValue("All"))
			{
				map.put("count", allCount);
				map.put("viewList", dvListForAll);

			}
		}

		return dataViewGrpList;

	}



	/**
	 * Author: Shiva
	 * @param List<Long> viewIds
	 * Description: Processing Manual Unreconciliation Data
	 * @return List<LinkedHashMap> with data view columns information
	 */
	@PostMapping("/getDataViewsColumnsInfo")
	@Timed
	public List<List<LinkedHashMap>> getDataViewColumnsInfo(@RequestBody List<String> viewIds,HttpServletRequest request){
		log.info("Rest api for getting columns information for the data view id(s): "+ viewIds);
		HashMap userTenantInfo=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(userTenantInfo.get("tenantId").toString());

		List<List<LinkedHashMap>> finalMap = new ArrayList<List<LinkedHashMap>>();
		if(viewIds.size()>0)
		{
			for(String viewId : viewIds)
			{
				log.info("Fetching column information for view id: "+ viewId);
				List<LinkedHashMap> viewInfo = new ArrayList<LinkedHashMap>();
				DataViews dview=dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, viewId);
				List<DataViewsColumns> dvc = dataViewsColumnsRepository.findByDataViewId(dview.getId());
				if(dvc.size()>0)
				{
					for(DataViewsColumns dv : dvc)
					{
						LinkedHashMap columnInfo = new LinkedHashMap();
						columnInfo.put("id", dv.getId());
						columnInfo.put("dataViewId", dv.getDataViewId());
						columnInfo.put("refDvType", dv.getRefDvType());
						columnInfo.put("refDvName", dv.getRefDvName());
						columnInfo.put("refDvColumn", dv.getRefDvColumn());
						columnInfo.put("columnName", dv.getColumnName());
						columnInfo.put("colDataType", dv.getColDataType());
						columnInfo.put("createdBy", dv.getCreatedBy());
						columnInfo.put("lastUpdatedBy", dv.getLastUpdatedBy());
						columnInfo.put("creationDate", dv.getCreationDate());
						columnInfo.put("lastUpdatedDate", dv.getLastUpdatedDate());
						columnInfo.put("formula", dv.getFormula());
						columnInfo.put("qualilfier", dv.getQualifier());
						columnInfo.put("formulaAlias", dv.getFormulaAlias());
						if(dv.getGroupBy()!=null)
							columnInfo.put("groupBy", dv.getGroupBy());
						viewInfo.add(columnInfo);
					}
				}
				finalMap.add(viewInfo);
			}
		}
		return finalMap;
	}
	/**
	 * Author : Shobha
	 * Swetha: Integrated Search functionality
	 * 		   Modified setting with DTO to JSONObject 	
	 * @param tenantId
	 * @param offset
	 * @param limit
	 * @param relation
	 * @return
	 * @throws URISyntaxException
	 * @throws ParseException 
	 * @throws java.text.ParseException 
	 * @throws JSONException 
	 * @throws IOException 
	 */
	@GetMapping("/fetchDataViewsByType")
	@Timed
	public List<JSONObject> fetchDataViewsByType(@RequestParam(value = "page" , required = false) Integer pageNumber,
			@RequestParam(value = "per_page", required = false) Integer pageSize,@RequestParam(required = false) String relation, 
			@RequestParam(required = false) String sortDirection,@RequestParam(required = false) String sortCol,@RequestParam(required = false) String searchVal,HttpServletRequest request, HttpServletResponse response) 
					throws URISyntaxException, ParseException, IOException, JSONException, java.text.ParseException{

		log.info("Rest Request to fetchDataViewsByType with relation: "+relation+" sortCol: "+sortCol+"searchVal: "+searchVal);

		HashMap map0=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map0.get("tenantId").toString());

		List<String> relationList = new ArrayList<String>();
		List<BigInteger> filteredDvList = new ArrayList<BigInteger>();
		List<JSONObject> outputData=new ArrayList<JSONObject>();
		List<JSONObject> finalObjList=new ArrayList<JSONObject>();
		if(sortDirection==null)
			sortDirection="Descending";
		if(relation != null &&  !relation.isEmpty() && !relation.equals(""))
		{
			//log.info("relation not null");
			if(relation.equalsIgnoreCase("All"))
			{
				//log.info("relation is all");
				relationList =  dataViewsSrcMappingsRepository.fetchDistinctRelations(tenantId);
				//log.info("relationList:"+relationList);
				if(relationList.size() > 0)
				{
					filteredDvList = dataViewsRepository.fetchDataViewIdsByRelationIn(relationList,tenantId);
				}
				List<BigInteger> singleTemplateList = new ArrayList<BigInteger>(); 
				singleTemplateList = dataViewsRepository.fetchDataViewIdsByRelationIsNull(tenantId);
				filteredDvList.addAll(singleTemplateList);
			}
			else
			{
				log.info("relation is not all  but not null");
				filteredDvList = dataViewsRepository.fetchDataViewIdsByRelation(relation, tenantId);
			}

		}
		else
		{
			log.info("relation is  null");
			List<DataViews> views = dataViewsRepository.findByTenantId(tenantId);
			for(DataViews view : views)
			{
				List<DataViewsSrcMappings> srcMappings = dataViewsSrcMappingsRepository.findByDataViewIdAndRelation(view.getId(),null);
				if(srcMappings.size() == 1)
				{
					filteredDvList.add(BigInteger.valueOf(view.getId()));
				}

			}
		}
		log.info("filteredDvList:"+filteredDvList.size());
		List<Long> ids = new ArrayList<Long>();
		for(BigInteger l : filteredDvList)
		{
			Long longNumber= l.longValue();
			ids.add(longNumber);
		}

		log.info("tenantId: "+tenantId+"pageNumber: "+pageNumber+"pageSize: "+pageSize);
		List<JSONObject> dtoList=new ArrayList<JSONObject>();
		//log.info("idsList :"+ids.size());

		List<DataViews> dvFinalList=dataViewsRepository.findByTenantIdAndIdInOrderByIdDesc(tenantId, ids);

		for(int i=0;i<dvFinalList.size();i++){
			JSONObject dvDto=new JSONObject();
			DataViews dv=dvFinalList.get(i);
			dvDto.put("id", dv.getIdForDisplay());
			if(dv.getDataViewName()!=null && !dv.getDataViewName().isEmpty())
				dvDto.put("dataViewName", dv.getDataViewName());
			if(dv.getDataViewDispName()!=null && !dv.getDataViewDispName().isEmpty())
				dvDto.put("dataViewDispName", dv.getDataViewDispName());
			if(dv.getCreatedBy()!=null)
				dvDto.put("createdBy", dv.getCreatedBy());
			if(dv.getCreationDate()!=null)
				dvDto.put("creationDate", dv.getCreationDate());
			if(dv.getDataViewObject()!=null)
				dvDto.put("dataViewObject", dv.getDataViewObject());
			if(dv.getDescription()!=null)
				dvDto.put("description", dv.getDescription());
			Boolean activeStatus=activeStatusService.activeStatus(dv.getStartDate(), dv.getEndDate(), dv.isEnabledFlag());
			dvDto.put("enabledFlag", activeStatus);
			if(dv.getEndDate()!=null)
				dvDto.put("endDate",dv.getEndDate());
			if(dv.getLastUpdatedBy()!=null)
					dvDto.put("lastUpdatedBy", dv.getLastUpdatedBy());
			if(dv.getLastUpdatedDate()!=null)
				dvDto.put("lastUpdatedDate", dv.getLastUpdatedDate());
			if(dv.getStartDate()!=null)
				dvDto.put("startDate", dv.getStartDate());
			if(dv.getTenantId()!=null)
				dvDto.put("tenantId", dv.getTenantId());
			List<DataViewsSrcMappings> dvSrcMapList=dataViewsSrcMappingsRepository.findByDataViewIdOrderByTemplateIdAsc(dv.getId());
			if(dvSrcMapList.size()>0){
				if(dvSrcMapList.size()==1)
				{
					dvDto.put("type", "Single Template");
				}

				else if(dvSrcMapList.size()>1){
					String dvType =dataViewsSrcMappingsRepository.fetchDVRelationByViewId(dv.getId());
					if(dvType!=null && !(dvType.isEmpty()) && !(dvType.equalsIgnoreCase(""))){
						LookUpCode relCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("DV_RELATION", dvType, tenantId);
						dvDto.put("type", relCode.getMeaning());
					}
					else {
						dvDto.put("type", "");
					}
				}
				else
				{
					log.info("nothing");
				}
				dtoList.add(dvDto);
			}

		}

		int totDataCnt=dtoList.size();

		int limit = 0;
		if(pageNumber == null || pageNumber == 0)
		{
			pageNumber = 0;
		}
		if(pageSize == null || pageSize == 0)
		{
			pageSize = totDataCnt;
		}
		pageNumber=pageNumber-1;
		limit = ((pageNumber+1) * pageSize + 1)-1;
		int startIndex=pageNumber*pageSize; 

		if(limit>totDataCnt){
			limit=totDataCnt;
		}
		List<String> headList=new ArrayList<String>();
		headList.add("dataViewDispName");
		headList.add("description");
		headList.add("type");
		headList.add("startDate");
		headList.add("endDate");

		int sz=0;
		List<JSONObject> finDataList=new ArrayList<JSONObject>();
		if(searchVal!=null && !(searchVal.isEmpty()) && !(searchVal.equalsIgnoreCase(""))){
			finalObjList=dataViewsService.globalSearch(dtoList, headList, searchVal);
			sz=finalObjList.size();
			//log.info("after search sz: "+sz);
			response.addIntHeader("x-total-count", sz);
			if(finalObjList!=null && finalObjList.size()>0 && limit<=finalObjList.size()){
				finDataList=finalObjList.subList(startIndex, limit);
			}
			else{
				log.info("finalObjList is null");
				finDataList=finalObjList;
			}
		}
		else {
			sz=dtoList.size();
			//log.info("without search sz: "+sz);
			response.addIntHeader("x-total-count", sz);
			if(dtoList!=null && dtoList.size()>limit && limit<=dtoList.size()){
				finDataList=dtoList.subList(startIndex, limit);
			}
			else {
				//log.info("finObj is null");
				finDataList=dtoList;
			}
		}


		/* Integrating Relationship wise counts */
		List<String> relationList1=dataViewsSrcMappingsRepository.fetchDistinctRelations(tenantId);
		int count;
		int allCount = 0;
		HashMap allMap=new HashMap();
		allMap.put("relation", "All");
		List<HashMap> dataViewGrpList=new ArrayList<HashMap>();
		for(int i=0;i<relationList1.size();i++){
			//log.info("relationList at i:"+i+" is: "+relationList1.get(i));
			if(relationList1.get(i)!=null && !(relationList1.get(i).isEmpty())){
				HashMap map=new HashMap();
				String relationCode=relationList1.get(i);
				//log.info("relationCode :"+relationCode);
				LookUpCode lCode=lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("DV_RELATION", relationCode, tenantId);
				relation=lCode.getMeaning();
				List<Object[]> objList=dataViewsRepository.fetchDataViewsByRelation(relation,tenantId);
				count=objList.size();
				//log.info("count: "+count);
				map.put("relation", relation);
				map.put("count", count);
				allCount = allCount + count;
				dataViewGrpList.add(map);
			}
			else {

				relation="Single Template";
				HashMap map=new HashMap();
				List<DataViews> dvList=dataViewsRepository.fetchSingleTemplateDataViews(tenantId);
				//log.info("dvList sz: "+dvList.size());
				map.put("relation", relation);
				Long dvId=0l;
				int singleTempCount=0;
				for(int j=0;j<dvList.size();j++){
					dvId=dvList.get(j).getId();
					List<DataViewsSrcMappings> dvSrcMapList=dataViewsSrcMappingsRepository.findByDataViewIdOrderByTemplateIdAsc(dvId);
					if(dvSrcMapList.size()==1)
					{
						singleTempCount++;
					}
				}
				allCount = allCount + singleTempCount;
				//log.info("allCount: "+allCount);
				map.put("count", singleTempCount);
				//log.info("finList sz: "+singleTempCount);
				dataViewGrpList.add(map);

			}

		}
		allMap.put("count", allCount);
		//log.info("allMap: "+allMap);
		dataViewGrpList.add(allMap);
		//log.info("dataViewGrpList: "+dataViewGrpList);


		ObjectMapper objectMapper = new ObjectMapper();

		String jsonDataViewGrpList = objectMapper.writeValueAsString(dataViewGrpList);
		response.addHeader("relationCounts", jsonDataViewGrpList);

		return finDataList;
	}


	/**
	 * Author: Shiva
	 * Purpose: Check weather data view exist or not
	 * **/
	@GetMapping("/checkDataViewIsExist")
	@Timed
	public HashMap checkDataViewIsExist(@RequestParam String name,@RequestParam(required=false,value="id") String id, HttpServletRequest request)
	{
		log.info("Rest Request to checkDataViewIsExist with name: "+name);

		HashMap userTenantInfo=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(userTenantInfo.get("tenantId").toString());


		HashMap map=new HashMap();
		map.put("result", "No Duplicates Found");
		if(id != null)
		{
			DataViews dv=dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, id);
			DataViews viewWithId = dataViewsRepository.findByIdAndDataViewDispNameAndTenantId(dv.getId(), name, tenantId);
			if(viewWithId != null)
			{
				map.put("result", "No Duplicates Found");
			}
			else
			{
				List<DataViews> views = dataViewsRepository.findByTenantIdAndDataViewDispName(tenantId, name);
				if(views.size()>0)
				{
					map.put("result", " Data Source: '"+name+"' already exists");
				}
			}
		}
		else 
		{
			List<DataViews> views = dataViewsRepository.findByTenantIdAndDataViewDispName(tenantId, name);
			if(views.size()>0)
			{
				map.put("result", " Data Source: '"+name+"' already exists");
			}
		}
		return map;
	}
	@GetMapping("/dataViewsListByTenantId")
	@Timed
	public List<DataViews> dataViewsListByTenantId( HttpServletRequest request)
	{
		log.info("Rest request to fetch Data views list by tenant id");
		List<DataViews> dvs =new ArrayList<DataViews>();
		HashMap userTenantInfo=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(userTenantInfo.get("tenantId").toString());
		dvs = dataViewsRepository.findByTenantId(tenantId);
		return dvs;
	}

}
