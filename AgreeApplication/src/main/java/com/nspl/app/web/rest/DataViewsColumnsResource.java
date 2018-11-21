package com.nspl.app.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import io.github.jhipster.web.util.ResponseUtil;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
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
import com.nspl.app.domain.DataViewConditions;
import com.nspl.app.domain.DataViewFilters;
import com.nspl.app.domain.DataViewUnion;
import com.nspl.app.domain.DataViews;
import com.nspl.app.domain.DataViewsColumns;
import com.nspl.app.domain.DataViewsSrcMappings;
import com.nspl.app.domain.FileTemplateLines;
import com.nspl.app.domain.FileTemplates;
import com.nspl.app.repository.AccountingLineTypesRepository;
import com.nspl.app.repository.AcctRuleConditionsRepository;
import com.nspl.app.repository.ApplicationProgramsRepository;
import com.nspl.app.repository.DataViewConditionsRepository;
import com.nspl.app.repository.DataViewFiltersRepository;
import com.nspl.app.repository.DataViewUnionRepository;
import com.nspl.app.repository.DataViewsColumnsRepository;
import com.nspl.app.repository.DataViewsRepository;
import com.nspl.app.repository.DataViewsSrcMappingsRepository;
import com.nspl.app.repository.FileTemplateLinesRepository;
import com.nspl.app.repository.FileTemplatesRepository;
import com.nspl.app.repository.RuleGroupDetailsRepository;
import com.nspl.app.repository.TenantConfigRepository;
import com.nspl.app.repository.search.DataViewsColumnsSearchRepository;
import com.nspl.app.security.IDORUtils;
import com.nspl.app.service.AccountingDataService;
import com.nspl.app.service.DataViewsService;
import com.nspl.app.service.ExcelFunctionsService;
import com.nspl.app.service.FileExportService;
import com.nspl.app.service.FileService;
import com.nspl.app.service.FileTemplatesService;
import com.nspl.app.service.PropertiesUtilService;
import com.nspl.app.service.ReconciliationResultService;
import com.nspl.app.service.UserJdbcService;
import com.nspl.app.web.rest.dto.DataViewColmnDTO;
import com.nspl.app.web.rest.dto.ErrorReport;
import com.nspl.app.web.rest.dto.StatusStringDTO;
import com.nspl.app.web.rest.util.HeaderUtil;

/**
 * REST controller for managing DataViewsColumns.
 */
@RestController
@RequestMapping("/api")
public class DataViewsColumnsResource {

    private final Logger log = LoggerFactory.getLogger(DataViewsColumnsResource.class);

    private static final String ENTITY_NAME = "dataViewsColumns";
        
    private final DataViewsColumnsRepository dataViewsColumnsRepository;

    private final DataViewsColumnsSearchRepository dataViewsColumnsSearchRepository;
    
    @Inject
    FileTemplateLinesRepository fileTemplateLinesRepository;
    
    @Inject
    FileTemplatesRepository fileTemplatesRepository;
    
    @Inject
    DataViewsRepository dataViewsRepository;
    
    @Inject
    DataViewConditionsRepository dataViewConditionsRepository;
    
    @Inject
    DataViewFiltersRepository dataViewFiltersRepository;
    
    @Inject
    ExcelFunctionsService excelFunctionsService;
    
    @Inject
    PropertiesUtilService propertiesUtilService;
    
    @Inject
    DataViewsService dataViewsService;
    
    @Inject
    DataViewsSrcMappingsRepository dataViewsSrcMappingsRepository;
    
    @Inject
    ReconciliationResultService reconciliationResultService;
    
    @Inject
    DataViewUnionRepository dataviewUnionRepositorty;
    
    @Inject
    RuleGroupDetailsRepository ruleGroupDetailsRepository;
    
    @Inject
    AccountingLineTypesRepository accountingLineTypesRepository;
    
    @Inject
    AcctRuleConditionsRepository acctRuleConditionsRepository;
    
    @Inject
    AccountingDataService accountingDataService;
    
    @Inject
    ApplicationProgramsRepository applicationProgramsRepository;
    
    
    @Inject
    FileService fileService;
    
    @Inject
    private Environment env;
    
    @Inject
    FileTemplatesService fileTemplatesService;
    
    @Inject
    UserJdbcService userJdbcService;
    
    
    @Inject
    TenantConfigRepository tenantConfigRepository;
    
    
    @Inject
    FileExportService fileExportService;
    
    public DataViewsColumnsResource(DataViewsColumnsRepository dataViewsColumnsRepository, DataViewsColumnsSearchRepository dataViewsColumnsSearchRepository) {
        this.dataViewsColumnsRepository = dataViewsColumnsRepository;
        this.dataViewsColumnsSearchRepository = dataViewsColumnsSearchRepository;
    }

    /**
     * POST  /data-views-columns : Create a new dataViewsColumns.
     *
     * @param dataViewsColumns the dataViewsColumns to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dataViewsColumns, or with status 400 (Bad Request) if the dataViewsColumns has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/data-views-columns")
    @Timed
    public ResponseEntity<DataViewsColumns> createDataViewsColumns(@RequestBody DataViewsColumns dataViewsColumns) throws URISyntaxException {
        log.debug("REST request to save DataViewsColumns : {}", dataViewsColumns);
        if (dataViewsColumns.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new dataViewsColumns cannot already have an ID")).body(null);
        }
        DataViewsColumns result = dataViewsColumnsRepository.save(dataViewsColumns);
        dataViewsColumnsSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/data-views-columns/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
   
    /**
     * Author: Swetha
     * Description: Posting and Updating DataView Columns and Filters
     * @param dataViewColmnDTO
     * @param userId
     * @return
     * @throws URISyntaxException
     * @throws ClassNotFoundException 
     */
    @PostMapping("/postDataViewsColumns")
    @Timed
    public HashMap createOrUpdateDataViewsColumns(@RequestBody DataViewColmnDTO dataViewColmnDTO,HttpServletRequest request) 
    		throws URISyntaxException, ClassNotFoundException {
    	log.debug("REST request to createOrUpdateDataViewsColumns : {}", dataViewColmnDTO);

    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long userId=Long.parseLong(map.get("userId").toString());
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	LinkedHashMap createOrUpdateMap=new LinkedHashMap();

    	String dvcColName="";
    	String physicalViewColumnName="";
    	String dType="";
    	String relation="";
    	String formula="";
    	Boolean grpByQualifier = null;
    	String amtQualCol="";
    	DataViews dvIdForDisplay=dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, dataViewColmnDTO.getDataViewId());
    	DataViews dv=dataViewsRepository.findOne(dvIdForDisplay.getId()); 
    	log.info("dv: "+dv);
    	
    	DataViewsColumns amtQualData=dataViewsColumnsRepository.findByDataViewIdAndQualifier(dv.getId(), "AMOUNT");
    	if(amtQualData!=null)
    	amtQualCol=amtQualData.getColumnName();
    	log.info("amtQualCol: "+amtQualCol);
    	
    	//DataViewsColumns grpByQualData=dataViewsColumnsRepository
    	
    	

    	if (dataViewColmnDTO.getId()!= null) {
    		log.info("Data View Column Update");
    		DataViewsColumns dvc=dataViewsColumnsRepository.findOne(dataViewColmnDTO.getId());
    		dvcColName=dvc.getColumnName();
    		dType=dvc.getColDataType();
    		formula=dvc.getFormula();
    		grpByQualifier=dvc.getGroupBy();

    		relation=dataViewsSrcMappingsRepository.fetchDVRelationByViewId(dv.getId());
    		log.info("relation: "+relation);
    		if(dvc.getRefDvType()!=null && dvc.getRefDvType().equalsIgnoreCase("File Template")){
    			if(relation==null || (relation!=null && relation.equalsIgnoreCase("JOIN"))){
    				log.info("dvc.getRefDvColumn(): "+dvc.getRefDvColumn());
    				FileTemplateLines ftl=fileTemplateLinesRepository.findOne(Long.parseLong(dvc.getRefDvColumn()));
    				//log.info("ftl: "+ftl);
    				physicalViewColumnName=ftl.getColumnAlias();
    			}
    			else if(relation!=null && relation.equalsIgnoreCase("UNION")){
    				physicalViewColumnName=dvc.getColumnName();
    			}
    		}
    		else physicalViewColumnName=dvc.getColumnName();
    	}

    	if(dvcColName!=null && !(dvcColName.isEmpty()) && !(dvcColName.equalsIgnoreCase(""))){
    		if(!(dvcColName.equalsIgnoreCase(dataViewColmnDTO.getColumnName()))){
    			createOrUpdateMap.put("ColumnRenamed", physicalViewColumnName);
    		}

    	}

    	if(dType!=null && !(dType.isEmpty()) && !(dType.equalsIgnoreCase(""))){
    		if(!(dType.equalsIgnoreCase(dataViewColmnDTO.getColDataType()))){
    			createOrUpdateMap.put("DataTypeUpdated", dvcColName);
    		}

    	}

    	if(formula!=null && !(formula.isEmpty()) && !(formula.equalsIgnoreCase(""))){
    		if(!(formula.equalsIgnoreCase(dataViewColmnDTO.getFormula()))){
    			createOrUpdateMap.put("FormulaUpdated", dvcColName);
    		}

    	}
    	log.info("grpByQualifier b4 check: "+grpByQualifier);
    	
    	if(grpByQualifier!=null){
    		if(grpByQualifier.compareTo(dataViewColmnDTO.getGroupBy())==0){
    			log.info("No change in groupBy Qualifier");
    		}
    		else{
    			log.info("group by qualifier changed");
    			createOrUpdateMap.put("groupByQualifier", dvcColName);
    		}
    			
    	}
    	else if(dataViewColmnDTO.getGroupBy()!=null && dataViewColmnDTO.getGroupBy()){
    		log.info("Previously NO groupBY flag enable, But now its enabled");
    		createOrUpdateMap.put("groupByQualifier", dvcColName);
    	}
    	
    	/*if(amtQualCol!=null){
    		if(dataViewColmnDTO.getQualifier()!=null && dataViewColmnDTO.getQualifier().equalsIgnoreCase("AMOUNT"))
    		if(amtQualCol.equalsIgnoreCase(dataViewColmnDTO.getColName())){
    			log.info("Amount qualifier matched");
    		}
    		else{
    			log.info("Amount qualifier not matched");
    			createOrUpdateMap.put("amountQualifier", dvcColName);
    		}
    	}*/

    	List<String> tempIdList=new ArrayList<String>();
    	tempIdList=dataViewsColumnsRepository.fetchDistinctTemplateId(dv.getId());
    	log.info("tempIdList sz for viewId: "+dataViewColmnDTO.getDataViewId()+" are: "+tempIdList.size());
    	DataViewsColumns dataViewsColumns=new DataViewsColumns();
    	if(dataViewColmnDTO.getDataViewId()!=null);
    	dataViewsColumns.setDataViewId(dv.getId());
    	if(dataViewColmnDTO.getRefDvType()!=null)
    		dataViewsColumns.setRefDvType(dataViewColmnDTO.getRefDvType());
    	if(dataViewColmnDTO.getRefDvName()!=null){
    		FileTemplates ftData=fileTemplatesRepository.findByIdForDisplayAndTenantId(dataViewColmnDTO.getRefDvName(), tenantId);
    		dataViewsColumns.setRefDvName(ftData.getId().toString());
    	}
    	else if(dataViewColmnDTO.getRefDvName()==null)
    		dataViewsColumns.setRefDvType("Data View");
    	if(dataViewColmnDTO.getRefDvColumn()!=null)
    		dataViewsColumns.setRefDvColumn(dataViewColmnDTO.getRefDvColumn());
    	if(dataViewColmnDTO.getColumnName()!=null)
    		dataViewsColumns.setColumnName(dataViewColmnDTO.getColumnName());
    	if(dataViewColmnDTO.getColDataType()!=null)
    		dataViewsColumns.setColDataType(dataViewColmnDTO.getColDataType());
    	dataViewsColumns.setCreatedBy(userId);
    	dataViewsColumns.setLastUpdatedBy(userId);      
    	dataViewsColumns.setCreationDate(ZonedDateTime.now());
    	dataViewsColumns.setLastUpdatedDate(ZonedDateTime.now());
    	if(dataViewColmnDTO.getFormula()!=null)
    	{
    		dataViewsColumns.setFormula(dataViewColmnDTO.getFormula());
    		String Str=dataViewsService.excelFormulasUpd(dataViewColmnDTO.getFormula(), tenantId,dv.getId(),"JOIN");
    		dataViewsColumns.setFormulaAlias(Str);
    	}
    	if(dataViewColmnDTO.getQualifier()!=null)
    		dataViewsColumns.setQualifier(dataViewColmnDTO.getQualifier());
    	if(dataViewColmnDTO.getGroupBy()!=null)
    		dataViewsColumns.setGroupBy(dataViewColmnDTO.getGroupBy());

    	if (dataViewColmnDTO.getId()!= null) {
    		dataViewsColumns.setId(dataViewColmnDTO.getId());
    	}
    	DataViewsColumns result = dataViewsColumnsRepository.save(dataViewsColumns);
    	createOrUpdateMap.put("colId", result.getId());
    	DataViewFilters dataViewFilter=dataViewFiltersRepository.findByDataViewIdAndRefSrcTypeAndRefSrcColId(dv.getId(),dataViewColmnDTO.getRefDvType(),dataViewColmnDTO.getId());

    	String filterOperator="";
    	String filterValue="";		

    	if(dataViewFilter!=null && dataViewFilter.getId()!=null){
    		dataViewFilter.setId( dataViewFilter.getId());
    		filterOperator=dataViewFilter.getFilterOperator();
    		filterValue=dataViewFilter.getFilterValue();
    		log.info("filterOperator: "+filterOperator+" filterValue: "+filterValue);
    	}
    	else{
    		dataViewFilter=new DataViewFilters();
    	}


    	if(filterOperator!=null && !(filterOperator.isEmpty()) && !(filterOperator.equalsIgnoreCase("")) && 
    			!(filterOperator.equalsIgnoreCase(dataViewColmnDTO.getOperator()) && !(filterValue.equalsIgnoreCase(dataViewColmnDTO.getColValue())))){
    		createOrUpdateMap.put("FilterUpdated",dataViewColmnDTO.getId());
    	}

    	if(dataViewColmnDTO.getDataViewId()!=null)
    		dataViewFilter.setDataViewId(dv.getId());
    	if(dataViewColmnDTO.getRefDvType()!=null)
    		dataViewFilter.setRefSrcType(dataViewColmnDTO.getRefDvType());
    	if(result.getId()!=null){
    		dataViewFilter.setRefSrcColId(result.getId());
    	}
    	if(dataViewColmnDTO.getId()== null && result.getId()!=null){
    		createOrUpdateMap.put("Create", result.getId());
    	}
    	if(dataViewColmnDTO.getRefDvName()!=null){
    		FileTemplates ftData=fileTemplatesRepository.findByIdForDisplayAndTenantId(dataViewColmnDTO.getRefDvName(), tenantId);
    		dataViewFilter.setRefSrcId(ftData.getId());
    	}
    	if(dataViewColmnDTO.getOperator()!=null && !(dataViewColmnDTO.getOperator().isEmpty())){
    		dataViewFilter.setFilterOperator(dataViewColmnDTO.getOperator());
    	}
    	else {
    		dataViewFilter.setFilterOperator(null);
    	}
    	if(dataViewColmnDTO.getColValue()!=null && !(dataViewColmnDTO.getColValue().isEmpty())){
    		dataViewFilter.setFilterValue(dataViewColmnDTO.getColValue());
    	}
    	else{
    		dataViewFilter.setFilterValue(null);
    	}
    	dataViewFilter.setCreatedBy(userId);
    	dataViewFilter.setLastUpdatedBy(userId);
    	dataViewFilter.setCreationDate(ZonedDateTime.now());
    	dataViewFilter.setLastUpdatedDate(ZonedDateTime.now());

    	DataViewFilters dvfNew=new DataViewFilters();
    	if(dataViewFilter.getFilterOperator()!=null && !(dataViewFilter.getFilterOperator().isEmpty()) && dataViewFilter.getFilterValue()!=null && !(dataViewFilter.getFilterValue().isEmpty())){
    		dvfNew=dataViewFiltersRepository.save(dataViewFilter);
    	}
    	else{
    		log.info("filter deleted");
    		dataViewFiltersRepository.delete(dataViewFilter);
    	}

    	return createOrUpdateMap;

    }
    /**
     * PUT  /data-views-columns : Updates an existing dataViewsColumns.
     *
     * @param dataViewsColumns the dataViewsColumns to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated dataViewsColumns,
     * or with status 400 (Bad Request) if the dataViewsColumns is not valid,
     * or with status 500 (Internal Server Error) if the dataViewsColumns couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/updateDataViewsColumns")
    @Timed
    public void updateDataViewsColumns(@RequestBody DataViewsColumns dataViewsColumns,@RequestParam Long userId) throws URISyntaxException {
        log.debug("REST request to update DataViewsColumns : {}", dataViewsColumns);
       
        dataViewsColumns.setLastUpdatedBy(userId);
        dataViewsColumns.setLastUpdatedDate(ZonedDateTime.now());
        DataViewsColumns result = dataViewsColumnsRepository.save(dataViewsColumns);
       
    }

    /**
     * GET  /data-views-columns : get all the dataViewsColumns.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of dataViewsColumns in body
     */
    @GetMapping("/data-views-columns")
    @Timed
    public List<DataViewsColumns> getAllDataViewsColumns() {
        log.debug("REST request to get all DataViewsColumns");
        List<DataViewsColumns> dataViewsColumns = dataViewsColumnsRepository.findAll();
        return dataViewsColumns;
    }

    /**
     * GET  /data-views-columns/:id : get the "id" dataViewsColumns.
     *
     * @param id the id of the dataViewsColumns to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dataViewsColumns, or with status 404 (Not Found)
     */
    @GetMapping("/data-views-columns/{id}")
    @Timed
    public ResponseEntity<DataViewsColumns> getDataViewsColumns(@PathVariable Long id) {
        log.debug("REST request to get DataViewsColumns : {}", id);
        DataViewsColumns dataViewsColumns = dataViewsColumnsRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(dataViewsColumns));
    }

    /**
     * DELETE  /data-views-columns/:id : delete the "id" dataViewsColumns.
     *
     * @param id the id of the dataViewsColumns to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/data-views-columns/{id}")
    @Timed
    public LinkedHashMap deleteDataViewsColumns(@PathVariable Long id) {
        log.debug("REST request to delete DataViewsColumns : {}", id);
        
        LinkedHashMap delMap=new LinkedHashMap();
        DataViewsColumns dvc=dataViewsColumnsRepository.findOne(id);
        /* Deleting Data View Filters Tagged to Data View Columns */
        log.info("dvc.getDataViewId(): "+dvc.getDataViewId()+"dvc.getRefDvType():: "+dvc.getRefDvType()+ "dvc.getId(): "+dvc.getId());
	    DataViewFilters dataViewFilter=dataViewFiltersRepository.findByDataViewIdAndRefSrcTypeAndRefSrcColId(dvc.getDataViewId(),dvc.getRefDvType(),dvc.getId());
   	 	log.info("dataViewFilter: "+dataViewFilter);
   	 	if(dataViewFilter!=null){
   	 	dataViewFiltersRepository.delete(dataViewFilter);
   	 	log.info("dataViewFilter deleted with id: "+dataViewFilter.getId());
   	 	}
   	 	else{
   	 		log.info("dataViewFilter doesn't exist");
   	 	}
   	 	
        dataViewsColumnsRepository.delete(id);
        dataViewsColumnsSearchRepository.delete(id);
        log.info("deleteDataViewsColumns with dataViewColumnId: "+id);
        
        delMap.put("Delete", id);
        delMap.put("colId", id);
        
        return delMap;
    }

    /**
     * SEARCH  /_search/data-views-columns?query=:query : search for the dataViewsColumns corresponding
     * to the query.
     *
     * @param query the query of the dataViewsColumns search 
     * @return the result of the search
     */
    @GetMapping("/_search/data-views-columns")
    @Timed
    public List<DataViewsColumns> searchDataViewsColumns(@RequestParam String query) {
        log.debug("REST request to search DataViewsColumns for query {}", query);
        return StreamSupport
            .stream(dataViewsColumnsSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

    /**
     * Author: Swetha
     * Description: Retrieve DataViewColumns / FileTemplateLines based on Id and Type
     * @param viewOrTemplateList
     * @return
     */
    @PostMapping("/getViewColumnsOrTemplateLines")
    @Timed
    public List getViewColumnsOrTemplateLines(@RequestBody List<JSONObject> viewOrTemplateList,HttpServletRequest request){
    	log.info("Rest Request to get dataViewColumns or Template Lines");
    	log.info("viewOrTemplateList :"+viewOrTemplateList);

    	HashMap map0=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map0.get("tenantId").toString());
    	Long userId=Long.parseLong(map0.get("userId").toString());

    	int sz=viewOrTemplateList.size();
    	List finalList=new ArrayList<>();
    	List<Long> dvIdList=new ArrayList<Long>();
    	List<String> tempIdList=new ArrayList<String>();
    	List<Long> intermIdList=new ArrayList<Long>();
    	for(int i=0;i<sz;i++){
    		JSONObject obj=viewOrTemplateList.get(i);
    		if(obj.get("type").toString().equalsIgnoreCase("Data View")){
    			dvIdList.add(Long.parseLong(obj.get("typeId").toString()));
    		}
    		else if(obj.get("type").toString().equalsIgnoreCase("File Template")){
    			if(obj.get("typeId")!=null)
    				tempIdList.add(obj.get("typeId").toString());
    			if(obj.containsKey("intermediateId")){
    				if(obj.get("intermediateId")!=null && obj.get("intermediateId").toString()!=null){
    					intermIdList.add(Long.parseLong(obj.get("intermediateId").toString()));
    				}
    			}
    		}
    	}
    	log.info("dvIdList: "+dvIdList);
    	log.info("tempIdList: "+tempIdList);
    	List<DataViewsColumns> dvcList=dataViewsColumnsRepository.findByDataViewIdIn(dvIdList);
    	log.info("dvcList sz: "+dvcList.size());
    	for(int j=0;j<dvcList.size();j++){
    		DataViewsColumns dvc=dvcList.get(j);
    		HashMap map=new HashMap();
    		map.put("id", dvc.getId());
    		if(dvc.getDataViewId()!=null)
    		{
    			DataViews dataView=dataViewsRepository.findOne(dvc.getDataViewId());
    			map.put("dataViewId", dataView.getIdForDisplay());
    			if(dataView.getDataViewName()!=null)
    				map.put("dataViewName", dataView.getDataViewName());
    			if(dataView.getDataViewDispName()!=null)
    				map.put("dataViewDisplayName", dataView.getDataViewDispName());

    		}
    		if(dvc.getRefDvType()!=null)
    			map.put("refDvType", dvc.getRefDvType());
    		if(dvc.getRefDvName()!=null)
    			map.put("refDvName", dvc.getRefDvName());
    		if(dvc.getRefDvColumn()!=null)
    			map.put("refDvColumn", dvc.getRefDvColumn());
    		if(dvc.getColumnName()!=null){
    			//Presently mapped to master table reference
    			/* Get reference field from fileTemplateLines */
    			if(dvc.getRefDvName()!=null){
    				FileTemplateLines ftl=fileTemplateLinesRepository.findByTemplateIdAndMasterTableReferenceColumn(Long.parseLong(dvc.getRefDvName()),dvc.getColumnName());
    				log.info("ftl for dvc.getRefDvName(): "+dvc.getRefDvName()+" and dvc.getColumnName(): "+dvc.getColumnName()+" is: "+ftl);
    				if(ftl!=null){
    					map.put("columnName",ftl.getColumnHeader());
    				}
    			}
    		}
    		if(dvc.getColDataType()!=null)
    			map.put("colDataType", dvc.getColDataType());
    		if(dvc.getGroupBy()!=null)
    			map.put("groupBy", dvc.getGroupBy());
    		if(dvc.getCreatedBy()!=null)
    			map.put("createdBy", dvc.getCreatedBy());
    		if(dvc.getLastUpdatedBy()!=null)
    			map.put("lastUpdatedBy", dvc.getLastUpdatedBy());
    		if(dvc.getCreationDate()!=null)
    			map.put("creationDate", dvc.getCreationDate());
    		if(dvc.getLastUpdatedDate()!=null)
    			map.put("lastUpdatedDate", dvc.getLastUpdatedDate());
    		if(map!=null)
    			finalList.add(map);

    	}

    	List<FileTemplateLines> ftempList=new ArrayList<FileTemplateLines>();

    	//Separating Multi and Single Identifier Template Lines
    	HashMap tempMap=new HashMap();
    	List<Long> nonMultitempIdsList=new ArrayList<Long>();
    	List<Long> multiTempIdsList=new ArrayList<Long>();
    	//log.info("tempIdList: "+tempIdList);
    	for(int k=0;k<tempIdList.size();k++){
    		String tempIdForDisplay=tempIdList.get(k);
    		FileTemplates ft=fileTemplatesRepository.findByIdForDisplayAndTenantId(tempIdForDisplay, tenantId);
    		Long tempId=ft.getId();
    		Boolean multiId=ft.getMultipleIdentifier();
    		if(multiId==true){
    			multiTempIdsList.add(tempId);
    		}
    		else {
    			nonMultitempIdsList.add(tempId);
    		}
    	}
    	log.info("nonMultitempIdsList: "+nonMultitempIdsList);
    	log.info("multiTempIdsList: "+multiTempIdsList);
    	if(nonMultitempIdsList!=null && !(nonMultitempIdsList.isEmpty()) && nonMultitempIdsList.size()>0){
    		List nonMultiftlMapList=fileTemplatesService.getFileTempleLines(nonMultitempIdsList,"nonMultiId",null);
    		finalList.addAll(nonMultiftlMapList);
    	}
    	if(multiTempIdsList!=null && !(multiTempIdsList.isEmpty()) && multiTempIdsList.size()>0){
    		List multiftlMapList=fileTemplatesService.getFileTempleLines(multiTempIdsList,"multiId",intermIdList);
    		finalList.addAll(multiftlMapList);
    	}

    	return finalList;

    }
    
    /**
     * Author: Swetha
     * Description: Api to Post Data View along with the Tagged DataView Columns and File Template Lines
     * @param viewOrTemplateList
     * @param tenantId
     * @param userId
     */
    @PostMapping("/postViewColumnsOrTemplateLines")
    @Timed
    public HashMap postViewColumnsOrTemplateLines(@RequestBody List<HashMap> viewOrTemplateList, HttpServletRequest request){
    	log.info("Rest Request to post dataViewColumns or Template Lines");

    	HashMap userTenantInfo=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(userTenantInfo.get("tenantId").toString());
    	Long userId=Long.parseLong(userTenantInfo.get("userId").toString());

    	HashMap map=new HashMap();
    	DataViews dv=new DataViews();
    	HashMap obj=(HashMap) viewOrTemplateList.get(0);
    	log.info("obj: "+obj);
    	String disName="";
    	if(obj.get("id")==null){
    		log.info("New DataView Creation");
    		dv.setCreatedBy(userId);
    		map.put("createdBy",userId);
    		dv.setCreationDate(ZonedDateTime.now());
    		map.put("creationDate", ZonedDateTime.now());
    		if(obj.get("dataViewDispName")!=null){
    			disName=obj.get("dataViewDispName").toString();
    			log.info("disName: "+disName);
    			dv.setDataViewDispName(disName.trim());
    			disName=disName+"_"+tenantId;
    			log.info("disName after replacing: "+disName);
    			dv.setDataViewName(disName.toLowerCase().trim());
    			map.put("dataViewDispName", obj.get("dataViewDispName").toString());
    			map.put("dataViewName", disName);
    		}
    	}
    	else{
    		if(obj.get("id")!=null)
    		{
    			DataViews dvExist=dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, obj.get("id").toString());
    			dv=dataViewsRepository.findOne(dvExist.getId());
    			dv.setDataViewName(dv.getDataViewName().toLowerCase().trim());
    			if(obj.get("dataViewDispName")!=null){
    				disName=obj.get("dataViewDispName").toString();
    				dv.setDataViewDispName(disName.trim());
    				map.put("dataViewDispName", obj.get("dataViewDispName").toString());
    				map.put("dataViewName", dv.getDataViewName());
    			}
    		}
    	}
    	dv.setLastUpdatedBy(userId);
    	map.put("lastUpdatedBy",userId);
    	dv.setLastUpdatedDate(ZonedDateTime.now());
    	map.put("lastUpdatedDate", ZonedDateTime.now());
    	if(obj.get("enabledFlag")!=null){
    		dv.setEnabledFlag((Boolean)obj.get("enabledFlag"));
    		map.put("enabledFlag", obj.get("enabledFlag"));
    	}
    	dv.setTenantId(tenantId);
    	map.put("tenantId", tenantId);
    	if(obj.get("startDate")!=null)
    	{	
    		ZonedDateTime stDate=ZonedDateTime.parse(obj.get("startDate").toString());
    		dv.setStartDate(stDate);
    		map.put("startDate", stDate);
    	}
    	if(obj.get("endDate")!=null)
    	{
    		ZonedDateTime edDate=ZonedDateTime.parse(obj.get("endDate").toString());
    		dv.setEndDate(edDate);
    		map.put("endDate", edDate);
    	}
    	if(obj.get("description")!=null){
    		dv.setDescription(obj.get("description").toString());
    		map.put("description", obj.get("description").toString());
    	}
    	DataViews dvNew=dataViewsRepository.save(dv);

    	if(obj.get("id")==null)
    	{
    		String idForDisplay = IDORUtils.computeFrontEndIdentifier(dvNew.getId().toString());
    		dvNew.setIdForDisplay(idForDisplay);
    		dvNew = dataViewsRepository.save(dvNew);
    	}


    	Long dvId=dvNew.getId();
    	map.put("id", dvNew.getIdForDisplay());
    	log.info("davaViews created with DvId: "+dvId);
    	log.info("dvId: "+dvId+" and obj.getid: "+obj.get("id"));

    	List<String> temp=new ArrayList<String>();
    	if(obj.get("dataViewsColumnsList")!=null){
    		List colOrTempList=(List) obj.get("dataViewsColumnsList");
    		for(int i=0;i<colOrTempList.size();i++){
    			HashMap colOrTempObj=(HashMap) colOrTempList.get(i);
    			if(colOrTempObj.get("refDvName")!=null){
    				temp.add(colOrTempObj.get("refDvName").toString());
    			}
    		}
    	}
    	log.info("temp :"+temp);

    	/* Posting Data View Src Mappings */
    	List<HashMap> srcMappingsList=new ArrayList<HashMap>();
    	if(obj.get("srcMapping")!=null){

    		srcMappingsList=(List<HashMap>) obj.get("srcMapping");
    		for(int i=0;i<srcMappingsList.size();i++){

    			HashMap srcmap=srcMappingsList.get(i);
    			DataViewsSrcMappings dvSrcMap=new DataViewsSrcMappings();
    			dvSrcMap.setDataViewId(dvId);
    			if(srcmap.get("typeId")!=null){
    				FileTemplates ft=fileTemplatesRepository.findByIdForDisplayAndTenantId(srcmap.get("typeId").toString(), tenantId);
    				dvSrcMap.setTemplateId(ft.getId());
    			}
    			if(srcmap.get("intermediateId")!=null){
    				dvSrcMap.setIntermediateId(Long.parseLong(srcmap.get("intermediateId").toString()));
    			}
    			dvSrcMap.setCreationDate(ZonedDateTime.now());
    			dvSrcMap.setCreatedBy(userId);
    			if(obj.get("viewRelation")!=null && obj.get("viewRelation").toString().equalsIgnoreCase("union"))
    				dvSrcMap.setRelation(obj.get("viewRelation").toString());
    			dataViewsSrcMappingsRepository.save(dvSrcMap);
    			log.info("dvSrcMap saved at i: "+i);
    		}


    	}

    	if(obj.containsKey("basedTemplate")){
    		if(obj.get("basedTemplate")!=null){
    			HashMap baseMap=(HashMap) obj.get("basedTemplate");
    			DataViewsSrcMappings dvs=new DataViewsSrcMappings();
    			Long tempId=null;
    			Long intId=null;
    			if(baseMap.get("typeId")!=null){
    				FileTemplates ftData=fileTemplatesRepository.findByIdForDisplayAndTenantId(baseMap.get("typeId").toString(), tenantId);
    				tempId=ftData.getId();
    			}
    			if(baseMap.get("intermediateId")!=null){
    				intId=Long.parseLong(baseMap.get("intermediateId").toString());
    			}
    			if(tempId!=null && intId!=null){
    				dvs=dataViewsSrcMappingsRepository.findByDataViewIdAndTemplateIdAndIntermediateId(dvId, tempId, intId);
    			}
    			else{
    				dvs=dataViewsSrcMappingsRepository.findByDataViewIdAndTemplateId(dvId, tempId);
    			}

    			if(dvs!=null){
    				dvs.setBase("Primary");
    				if(obj.get("viewRelation")!=null)
    					dvs.setRelation(obj.get("viewRelation").toString());
    				log.info("dvs: "+dvs);
    				dataViewsSrcMappingsRepository.save(dvs);
    				log.info("data View Src mapping has been saved with dvs id is: "+dvs.getId());
    			}
    		}
    	}

    	/* Posting Data View Columns */
    	if(obj.get("dataViewsColumnsList")!=null){
    		List colOrTempList=(List) obj.get("dataViewsColumnsList");
    		log.info("colOrTempList sz: "+colOrTempList.size());
    		List<HashMap> colOrTempMapList=new ArrayList<HashMap>();
    		for(int i=0;i<colOrTempList.size();i++){

    			HashMap colOrTempMap=new HashMap();
    			HashMap colOrTempObj=(HashMap) colOrTempList.get(i);
    			log.info("colOrTempObj["+i+"] is: "+colOrTempObj);
    			DataViewsColumns dvc=new DataViewsColumns();
    			if(colOrTempObj.get("refDvType")!=null){
    				if(colOrTempObj.get("refDvType").toString().equalsIgnoreCase("Data View")){
    					log.info("in type: "+colOrTempObj.get("refDvType").toString());
    				}
    				else if(colOrTempObj.get("refDvType").toString().equalsIgnoreCase("File Template")){
    					log.info("in type: "+colOrTempObj.get("refDvType").toString());
    				}
    			}
    			else{
    				log.info("colOrTempObj.getrefDvType: "+colOrTempObj.get("refDvType")+" is null");

    			}
    			if(colOrTempObj.get("qualifier")!=null){
    				dvc.setQualifier(colOrTempObj.get("qualifier").toString());
    				colOrTempMap.put("qualifier", colOrTempObj.get("qualifier").toString());
    			}
    			if(colOrTempObj.get("colDataType")!=null){
    				dvc.setColDataType(colOrTempObj.get("colDataType").toString());
    				colOrTempMap.put("colDataType", colOrTempObj.get("colDataType").toString());
    			}
    			if(colOrTempObj.get("formula")!=null)
    				dvc.setFormula(colOrTempObj.get("formula").toString());	
    			dvc.setCreatedBy(userId);
    			colOrTempMap.put("createdBy", userId);
    			dvc.setCreationDate(ZonedDateTime.now());
    			colOrTempMap.put("creationDate", ZonedDateTime.now());
    			if(colOrTempObj.get("refDvType")!=null){
    				dvc.setRefDvType(colOrTempObj.get("refDvType").toString());
    				colOrTempMap.put("refDvType", colOrTempObj.get("refDvType").toString());
    			}
    			else{
    				log.info("setRefDvType is null");
    				dvc.setRefDvType("Data View");
    			}
    			if(colOrTempObj.get("refDvName")!=null){
    				FileTemplates ftData=fileTemplatesRepository.findByIdForDisplayAndTenantId(colOrTempObj.get("refDvName").toString(), tenantId);
    				dvc.setRefDvName(ftData.getId().toString());
    			}
    			else{
    				log.info("new data view column found");

    			}
    			if(colOrTempObj.get("groupBy")!=null){
    				dvc.setGroupBy((Boolean) colOrTempObj.get("groupBy"));
    				colOrTempMap.put("groupBy", colOrTempObj.get("groupBy"));
    			}
    			dvc.setDataViewId(dvId);
    			colOrTempMap.put("dataViewId", dvId);
    			if(colOrTempObj.get("refDvColumn")!=null){
    				dvc.refDvColumn(colOrTempObj.get("refDvColumn").toString());
    				colOrTempMap.put("refDvColumn",colOrTempObj.get("refDvColumn").toString());
    			}
    			if(colOrTempObj.containsKey("intermediateId")){
    				if(colOrTempObj.get("intermediateId")!=null && colOrTempObj.get("intermediateId").toString()!=null){
    					dvc.setIntermediateId(Long.parseLong(colOrTempObj.get("intermediateId").toString()));
    					colOrTempMap.put("intermediateId", Long.parseLong(colOrTempObj.get("intermediateId").toString()));
    				}
    			}
    			if(colOrTempObj.get("columnName")!=null){
    				dvc.setColumnName(colOrTempObj.get("columnName").toString().trim());
    				colOrTempMap.put("columnName", colOrTempObj.get("columnName").toString());
    			}
    			if(colOrTempObj.get("formula")!=null)
    			{
    				log.info("dvc.getform :"+dvc.getFormula());
    				String Str=dataViewsService.excelFormulasUpd(dvc.getFormula(), tenantId,dvId,"JOIN");
    				dvc.setFormulaAlias(Str);
    			}
    			if(colOrTempObj.get("id")!=null){
    			}
    			DataViewsColumns dvcNew=dataViewsColumnsRepository.save(dvc);
    			Long dvcNewId=dvcNew.getId();
    			colOrTempMap.put("id", dvcNewId);

    			/* Posting data view filters */
    			if(colOrTempObj.get("operator")!=null){
    				DataViewFilters dvf=new DataViewFilters();
    				dvf.setCreatedBy(userId);
    				dvf.setCreationDate(ZonedDateTime.now());
    				if(dvId!=null)
    					dvf.setDataViewId(dvId);
    				if(colOrTempObj.get("operator")!=null)
    					dvf.setFilterOperator(colOrTempObj.get("operator").toString());
    				if(dvcNewId!=null)
    					dvf.setRefSrcColId(dvcNewId);
    				if(colOrTempObj.get("refDvName")!=null){
    					FileTemplates ftData=fileTemplatesRepository.findByIdForDisplayAndTenantId(colOrTempObj.get("refDvName").toString(), tenantId);
    					dvf.setRefSrcId(ftData.getId());
    				}
    				if(colOrTempObj.get("refDvType")!=null)
    					dvf.setRefSrcType(colOrTempObj.get("refDvType").toString());
    				else dvf.setRefSrcType(dvcNew.getRefDvType());
    				if(colOrTempObj.get("colValue")!=null)
    					dvf.setFilterValue(colOrTempObj.get("colValue").toString());
    				DataViewFilters dvfNew=dataViewFiltersRepository.save(dvf);
    				log.info("dvfNew created with id: "+dvfNew.getId());

    				if(colOrTempObj.get("operator")!=null)
    					colOrTempMap.put("operator", colOrTempObj.get("operator").toString());
    				if(colOrTempObj.get("colValue")!=null)
    					colOrTempMap.put("colValue",colOrTempObj.get("colValue").toString());
    			}

    			colOrTempMapList.add(colOrTempMap);
    			map.put("dataViewsColumnsList", colOrTempMapList);
    			log.info("DataViewsColumns has been created with id: "+dvcNewId);

    		}

    		/* Posting Data View Conditions */
    		List<HashMap> condMapList=new ArrayList<HashMap>();
    		if(obj.get("conditions")!=null){
    			List colCondList=(List) obj.get("conditions");
    			log.info("colCondList sz: "+colCondList.size());
    			List<HashMap> colCondMapList=new ArrayList<HashMap>();

    			HashMap conMap=new HashMap();
    			HashMap dvCon=(HashMap) colCondList.get(0);
    			DataViewConditions dvConditions=new DataViewConditions();
    			dvConditions.setDataViewId(dvId);
    			if(dvCon.get("srcType1")!=null){
    				dvConditions.setRefSrcType(dvCon.get("srcType1").toString());
    				conMap.put("srcType1", dvCon.get("srcType1"));
    			}
    			if(dvCon.get("srcType2")!=null){
    				dvConditions.setRefSrcType2(dvCon.get("srcType2").toString());
    				conMap.put("srcType2", dvCon.get("srcType2"));
    			}
    			if(dvCon.get("scr1")!=null){
    				FileTemplates ftData=fileTemplatesRepository.findByIdForDisplayAndTenantId(dvCon.get("scr1").toString(), tenantId);
    				dvConditions.setRefSrcId(ftData.getId());
    				conMap.put("scr1", dvCon.get("scr1"));
    			}
    			if(dvCon.get("scr2")!=null){
    				FileTemplates ftData=fileTemplatesRepository.findByIdForDisplayAndTenantId(dvCon.get("scr2").toString(), tenantId);
    				dvConditions.setRefSrcId2(ftData.getId());
    				conMap.put("scr2", dvCon.get("scr2"));
    			}
    			if(dvCon.get("srcCol1")!=null){
    				dvConditions.setRefSrcColId(Long.parseLong(dvCon.get("srcCol1").toString()));
    				conMap.put("srcCol1", dvCon.get("srcCol1"));
    			}
    			if(dvCon.get("srcCol2")!=null){
    				dvConditions.setRefSrcColId2(Long.parseLong(dvCon.get("srcCol2").toString()));
    				conMap.put("srcCol2", dvCon.get("srcCol2"));
    			}
    			if(dvCon.get("conditionOperator")!=null){
    				dvConditions.setFilterOperator(dvCon.get("conditionOperator").toString());
    				conMap.put("conditionOperator", dvCon.get("conditionOperator"));
    			}

    			DataViewConditions dvcNew=dataViewConditionsRepository.save(dvConditions);
    			condMapList.add(conMap);
    			map.put("conditions", condMapList);
    		}

    	}
    	/* Posting Data View Columns and unions */
    	else if(obj.get("dataViewsUnionColumnsList")!=null)
    	{
    		List<String> tempIdList=new ArrayList<String>();
    		List colOrTempList=(List) obj.get("dataViewsUnionColumnsList");
    		log.info("colOrTempList sz: "+colOrTempList.size());
    		List<HashMap> colOrTempMapList=new ArrayList<HashMap>();
    		for(int i=0;i<colOrTempList.size();i++){

    			HashMap colOrTempMap=new HashMap();
    			HashMap colOrTempObj=(HashMap) colOrTempList.get(i);
    			log.info("colOrTempObj["+i+"] is: "+colOrTempObj);
    			DataViewsColumns dvc=new DataViewsColumns();

    			if(colOrTempObj.get("qualifier")!=null){
    				dvc.setQualifier(colOrTempObj.get("qualifier").toString());
    				colOrTempMap.put("qualifier", colOrTempObj.get("qualifier").toString());
    			}
    			if(colOrTempObj.get("colDataType")!=null){
    				dvc.setColDataType(colOrTempObj.get("colDataType").toString());
    				colOrTempMap.put("colDataType", colOrTempObj.get("colDataType").toString());
    			}
    			if(colOrTempObj.get("formula")!=null)
    				dvc.setFormula(colOrTempObj.get("formula").toString());	
    			dvc.setCreatedBy(userId);
    			colOrTempMap.put("createdBy", userId);
    			dvc.setCreationDate(ZonedDateTime.now());
    			colOrTempMap.put("creationDate", ZonedDateTime.now());

    			if(colOrTempObj.get("refDvName")!=null){
    				dvc.setRefDvName(colOrTempObj.get("refDvName").toString());
    			}
    			else{
    				log.info("new data view column found");

    			}
    			dvc.setDataViewId(dvId);
    			colOrTempMap.put("dataViewId", dvId);

    			if(colOrTempObj.get("columnName")!=null){
    				dvc.setColumnName(colOrTempObj.get("columnName").toString().trim());
    				colOrTempMap.put("columnName", colOrTempObj.get("columnName").toString());
    			}
    			if(colOrTempObj.get("colDataType")!=null){
    				dvc.setColDataType(colOrTempObj.get("colDataType").toString());
    				colOrTempMap.put("colDataType", colOrTempObj.get("colDataType").toString());
    			}
    			if(colOrTempObj.get("groupBy")!=null){
    				dvc.setGroupBy((Boolean) colOrTempObj.get("groupBy"));
    				colOrTempMap.put("groupBy", colOrTempObj.get("groupBy"));
    			}
    			if(colOrTempObj.get("id")!=null){
    			}
    			log.info("dvc: "+dvc);
    			DataViewsColumns dvcNew=dataViewsColumnsRepository.save(dvc);
    			Long dvcNewId=dvcNew.getId();
    			colOrTempMap.put("id", dvcNewId);

    			map.put("dataViewsColumnsList", colOrTempMapList);
    			log.info("DataViewsColumns has been created with id: "+dvcNewId);

    			List<DataViewUnion> dvuList=new ArrayList<DataViewUnion>();
    			if(colOrTempObj.get("src")!=null){
    				HashMap setSrc=new HashMap();
    				List<DataViewUnion> dataViewUnionList=new ArrayList<DataViewUnion>();
    				List<HashMap> src=(List<HashMap>) colOrTempObj.get("src");
    				for(int s=0;s<src.size();s++)
    				{
    					DataViewUnion dvu=new DataViewUnion();
    					dvu.setDataViewLineId(dvcNewId);
    					log.info("src.get(s): "+src.get(s));
    					HashMap srcMap=src.get(s);
    					if(srcMap.containsKey("refDvColumn")){
    						if(srcMap.get("refDvColumn")!=null)
    						{
    							if(srcMap.get("refDvColumn").toString().equalsIgnoreCase("customFunction")){
    								dvu.setRefDvColumn(null);
    								setSrc.put("refDvColumn", "customFunction");
    							}
    							else if(srcMap.get("refDvColumn").toString().equalsIgnoreCase("none")){
    								dvu.setFormula(null);
    								dvu.setFormulaAlias(null);
    							}
    							else{
    								dvu.setRefDvColumn(Long.parseLong(srcMap.get("refDvColumn").toString()));
    								setSrc.put("refDvColumn", Long.parseLong(srcMap.get("refDvColumn").toString()));
    							}

    						}
    					}
    					if(srcMap.containsKey("refDvName"))
    						if(srcMap.get("refDvName")!=null)
    						{
    							log.info("srcMap.get(refDvName).toString(): "+srcMap.get("refDvName").toString());
    							FileTemplates ftData=fileTemplatesRepository.findByIdForDisplayAndTenantId(srcMap.get("refDvName").toString(), tenantId);
    							dvu.setRefDvName(ftData.getId());
    							tempIdList.add((srcMap.get("refDvName").toString()));
    							setSrc.put("refDvName",srcMap.get("refDvName").toString());
    						}
    					log.info("refDvType: "+src.get(s).get("refDvType"));
    					if(srcMap.containsKey("refDvType"))
    						if(srcMap.get("refDvType")!=null)
    						{
    							dvu.setRefDvType(srcMap.get("refDvType").toString());
    							setSrc.put("refDvType", src.get(s).get("refDvType").toString());
    						}
    					if(srcMap.containsKey("intermediateId"))
    						if(srcMap.get("intermediateId")!=null)
    						{  
    							dvu.setIntermediateId(Long.parseLong((srcMap.get("intermediateId").toString())));
    							setSrc.put("intermediateId", srcMap.get("intermediateId").toString());
    						}
    					if(srcMap.containsKey("excelexpressioninputUnion"))
    						if(srcMap.get("excelexpressioninputUnion")!=null ){
    							dvu.setFormula(srcMap.get("excelexpressioninputUnion").toString());
    							String formula=srcMap.get("excelexpressioninputUnion").toString();
    							setSrc.put("formula", formula);
    							String formulaAlias=dataViewsService.excelFormulasUpd(formula, tenantId, dvId, "UNION");
    							log.info("final formulaAlias: "+formulaAlias);
    							dvu.setFormulaAlias(formulaAlias);
    							setSrc.put("formulaAlias", formulaAlias);
    						}

    					dvu.setCreatedBy(userId);
    					dvu.setCreationDate(ZonedDateTime.now());
    					dvu.setLastUpdatedBy(userId);
    					dvu.setLastUpdatedDate(ZonedDateTime.now());
    					log.info("dvu b4 adding to list: "+dvu);
    					dataViewUnionList.add(dvu);

    				}
    				dataviewUnionRepositorty.save(dataViewUnionList);
    				colOrTempMap.put("src", setSrc);
    			}

    			log.info("tempIdList: "+tempIdList);

    			colOrTempMapList.add(colOrTempMap);
    		}

    	}
    	return map;
    }
    
    
    /**
     * Author: Swetha
     * Description: Api to fetch the dataView data
     * @param viewName
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    @GetMapping("/getDataViewsData")
    @Timed
    public HashMap getDataViewsData(@RequestParam String viewId,HttpServletResponse response, @RequestParam(value = "pageNumber", required=false) 
    Long pageNumber, @RequestParam(value = "pageSize", required=false) Long pageSize,HttpServletRequest request) throws ClassNotFoundException, SQLException {
    	HashMap userTenantMap=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(userTenantMap.get("tenantId").toString());
    	Long userId=Long.parseLong(userTenantMap.get("userId").toString());
    	Long limit = 0L;
    	if(pageNumber == null || pageNumber == 0)
    	{
    		pageNumber = 0L;
    	}
    	if(pageSize == null || pageSize == 0)
    	{
    		pageSize = 25L;
    	}
    	limit = (pageNumber * pageSize + 1)-1;
    	log.info("Limit Starting Values : "+ limit);
    	log.info("Page Number : "+ pageNumber);

    	HashMap finalMap=new HashMap();


    	HashMap finMap=new HashMap();
    	int count=0;
    	DataViews dView=dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, viewId);
    	finMap=dataViewsService.getDataViewsData(dView.getId(),limit,pageSize);

    	List<LinkedHashMap> attributeNames=new ArrayList<LinkedHashMap>();

    	List<DataViewsColumns> dvColumnsList=dataViewsColumnsRepository.findByDataViewId(dView.getId());
    	for(DataViewsColumns dvc:dvColumnsList)
    	{
    		LinkedHashMap dvcMap=new LinkedHashMap();
    		dvcMap.put("field", dvc.getColumnName());
    		dvcMap.put("header",  dvc.getColumnName());
    		if(dvc.getColDataType().equalsIgnoreCase("DATE"))
    			dvcMap.put("align", "center");
    		else if(dvc.getColDataType().equalsIgnoreCase("DECIMAL"))
    			dvcMap.put("align", "right");
    		else
    			dvcMap.put("align", "left");
    		attributeNames.add(dvcMap);
    	}
    	List<HashMap> mapList=new ArrayList<HashMap>();
    	if(finMap!=null && !(finMap.isEmpty())){
    		if(finMap.containsKey("mapList")){
    			mapList =(List<HashMap>) finMap.get("mapList");
    		}
    		else{
    			log.info("View data doesnt exists");
    		}
    		if(finMap.containsKey("count")){
    			if(finMap.get("count")!=null && finMap.get("count").toString()!=null && !(finMap.get("count").toString().isEmpty())){
    				String val=finMap.get("count").toString();
    				count=Integer.parseInt(val);
    			}
    		}
    	}
    	finalMap.put("dataView", finMap);
    	finalMap.put("columnsWithAttributeNames",attributeNames);
    	response.addIntHeader("x-count", count);
    	return finalMap;
    }
 	   
    
    /**
     * Author: Swetha
     * Description: Api to Create a Data View
     * @param viewId
     * @throws ClassNotFoundException
     * @throws SQLException 
     */    
    @GetMapping("/createDataView")
    @Timed
    public ErrorReport createDataView(@RequestParam String viewId,HttpServletRequest request ) 
    		throws ClassNotFoundException, SQLException {

    	ErrorReport errorReport=new ErrorReport();
    	errorReport.setTaskName("physical view creation");
    	log.info("Rest Request to createDataView viewId :"+viewId);
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	Connection conn = null;
    	Statement stmt = null;
    	Statement stmt2=null;
    	Statement stmt3=null;
    	Statement indxStmt=null;
    	Statement indexStmt=null;
    	Statement insertColStmt=null;
    	ResultSet existrs=null;
    	Statement getDvCountStmt=null;
    	ResultSet getDvCountResult=null;
    	Statement helpStringFunStmt=null;
    	Statement alterTableStmt=null;
    	Statement dropViewStmt=null;
    	Statement checkViewExist=null;
    	ResultSet viewExtRs=null;

    	try{
    		DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
    		conn=ds.getConnection();
    		log.info("Connected database successfully...");
    		stmt = conn.createStatement();
    		stmt2=conn.createStatement();
    		stmt3=conn.createStatement();
    		indxStmt=conn.createStatement();
    		indexStmt=conn.createStatement();
    		insertColStmt=conn.createStatement();
    		getDvCountStmt=conn.createStatement();
    		helpStringFunStmt=conn.createStatement();
    		alterTableStmt=conn.createStatement();
    		dropViewStmt=conn.createStatement();
    		checkViewExist=conn.createStatement();

    		String dbUrl=env.getProperty("spring.datasource.url");
    		String[] parts=dbUrl.split("[\\s@&?$+-]+");
    		String schemaName=parts[0].split("/")[3];

    		DataViews dv=dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, viewId);
    		String physicalViewName=dv.getDataViewName()+"_v";

    		String currDataTableName=dv.getDataViewName();
    		log.info("Current Data View Table Name: "+currDataTableName);

    		physicalViewName=physicalViewName.toLowerCase();

    		String newTableName=dv.getDataViewDispName();
    		newTableName=newTableName.trim();
    		newTableName=newTableName+"_"+tenantId;
    		newTableName=newTableName.toLowerCase().trim();
    		log.info("newTableName: "+newTableName);

    		if(currDataTableName.equalsIgnoreCase(newTableName)){
    			log.info("No change found in Current and Previous Data Views Table Name");
    		}
    		else{
    			log.info("Mismatch found in Current and Previous Data Views Table Name");
    			physicalViewName=newTableName+"_v";
    		}
    		log.info("final physicalViewName: "+physicalViewName );
    		String createViewQuery="create or replace view `"+physicalViewName+"` as  " ;
    		List<DataViewsSrcMappings> dvSrcMapList=dataViewsSrcMappingsRepository.findByDataViewIdAndRelation(dv.getId(), "UNION");
    		String viewQuery="";
    		if(dvSrcMapList!=null && dvSrcMapList.size()!=0){
    			viewQuery=dataViewsService.frameUnionsQuery(dv.getId(), tenantId);
    		}
    		else viewQuery=dataViewsService.frameQuery(dv.getId(), tenantId);
    		log.info("viewQuery in createDataView: "+viewQuery);
    		viewQuery=createViewQuery+viewQuery;
    		log.info("final view creation query viewQuery: "+viewQuery);
    		int out=stmt.executeUpdate(viewQuery);
    		log.info("view creation query executed");

    		String tableName=dv.getDataViewName().toLowerCase();
    		//String tableName="";


    		String checkTableExistence=" SELECT count(*) FROM information_schema.tables WHERE table_schema = '"+schemaName+"' AND table_name = '"+tableName+"' LIMIT 1";
    		existrs=stmt2.executeQuery(checkTableExistence);

    		String existCnt=null;
    		while(existrs.next()){
    			existCnt=existrs.getString(1);
    		}
    		log.info("table existence query executed with existCnt: "+existCnt);

    		String relation=dataViewsSrcMappingsRepository.fetchDVRelationByViewId(dv.getId());
    		log.info("relation: "+relation);

    		if(existCnt.equalsIgnoreCase("1")){

    			if(currDataTableName.equalsIgnoreCase(newTableName)){
    				log.info("No change found in Current and Previous Data Views Table Name");
    				tableName=currDataTableName;
    			}
    			else{
    				log.info("Mismatch found in Current and Previous Data Views Table Name");

    				String checkViewExisQuery=" SELECT count(*) FROM information_schema.tables WHERE table_schema = '"+schemaName+"' AND TABLE_TYPE='VIEW' AND table_name = '"+currDataTableName+"_v' LIMIT 1";
    				viewExtRs=checkViewExist.executeQuery(checkViewExisQuery);
    				String viewExtRsCnt=null;
    				while(viewExtRs.next()){
    					viewExtRsCnt=viewExtRs.getString(1);
    				}
    				log.info("View Existence check query executed with viewExtRsCnt: "+viewExtRsCnt);
    				if(viewExtRsCnt.equalsIgnoreCase("1")){
    					log.info("View existence pass");
    					String dropViewQuery="DROP VIEW `"+currDataTableName+"_v`";
    					log.info("dropViewQuery: "+dropViewQuery);
    					int dropViewCnt=dropViewStmt.executeUpdate(dropViewQuery);
    					log.info("Drop View Query executed with dropViewCnt: "+dropViewCnt);
    				}
    				else{
    					log.info("View already doesnt exist");
    				}

    				String alterTableQuery="ALTER TABLE `"+currDataTableName+"` RENAME `"+newTableName+"`";
    				log.info("alterTableQuery: "+alterTableQuery);
    				int alterTabIndx=alterTableStmt.executeUpdate(alterTableQuery);
    				log.info("Table alteration query executed with alterTabIndx: "+alterTabIndx);

    				tableName=newTableName;
    			}
    		}
    		else{
    			log.info("tableName in else aftr: "+tableName);
    			String createTableQuery="create table `"+tableName+"` as select * from `"+physicalViewName+"`";
    			log.info("table creation query: "+createTableQuery);
    			int createTabCnt=stmt3.executeUpdate(createTableQuery);
    			log.info("table creation query executed with createTabCnt: "+createTabCnt);

    			String indxName=tableName+"_scrIds_Indx";
    			String indxQuery="CREATE INDEX `"+indxName+"` ON `"+tableName+"`(scrIds);";
    			log.info("Create Index on srcIds: "+indxQuery);
    			int indx = indxStmt.executeUpdate(indxQuery);
    			log.info("Index creation stament executed with indx: "+indx);

    			List<DataViewsColumns> groupByDVCList=dataViewsColumnsRepository.findByDataViewIdAndGroupByIsTrue(dv.getId());
    			if(groupByDVCList!=null && groupByDVCList.size()>0)
    				log.info("groupByDVCList.size(): "+groupByDVCList.size());
    			for(int i=0;i<groupByDVCList.size();i++){
    				DataViewsColumns groupBydvc=groupByDVCList.get(i);
    				log.info("groupBydvc: "+groupBydvc);
    				String physicalViewColumnName="";
    				if(groupBydvc.getRefDvType()!=null && groupBydvc.getRefDvType().equalsIgnoreCase("File Template")){
    					if(relation==null || (relation!=null && relation.equalsIgnoreCase("JOIN"))){
    						log.info("groupBydvc column name: "+groupBydvc.getColumnName());
    						FileTemplateLines ftl=fileTemplateLinesRepository.findOne(Long.parseLong(groupBydvc.getRefDvColumn()));
    						//log.info("ftl: "+ftl);
    						physicalViewColumnName=ftl.getColumnAlias();
    					}
    					else if(relation!=null && relation.equalsIgnoreCase("UNION")){
    						physicalViewColumnName=groupBydvc.getColumnName();
    					}
    				}
    				else physicalViewColumnName=groupBydvc.getColumnName();
    				String indexName=tableName+"_"+physicalViewColumnName+"_Indx";
    				String indexQuery="CREATE INDEX `"+indexName+"` ON `"+tableName+"`(`"+physicalViewColumnName+"`);";
    				log.info("Create Index on groupbycol: "+indexQuery);
    				int index = indexStmt.executeUpdate(indexQuery);
    				log.info("Index creation stament executed with indx: "+index);
    			}
    		}

    		String getDvCountQry="";
    		DataViewsColumns dvc=dataViewsColumnsRepository.findByDataViewIdAndQualifier(dv.getId(), "TRANSDATE");
    		String dateQualifier="";
    		if(dvc.getRefDvType()!=null && !(dvc.getRefDvType().isEmpty()) && dvc.getRefDvType().equalsIgnoreCase("File Template")){
    			FileTemplateLines ftl = fileTemplateLinesRepository.findOne(Long.parseLong(dvc.getRefDvColumn().toString()));
    			dateQualifier=ftl.getColumnAlias();
    		}else if(dvc.getRefDvType()==null || (dvc.getRefDvType()!=null && !(dvc.getRefDvType().isEmpty()) && dvc.getRefDvType().equalsIgnoreCase("Data View"))){
    			dateQualifier=dvc.getColumnName();
    		}
    		log.info("dateQualifier column: "+dateQualifier);
    		getDvCountQry= "select max(fileDate), max(`"+dateQualifier+"`), count(*) from `"+tableName+"`";
    		log.info("getDvCountQry: "+getDvCountQry);
    		getDvCountResult=getDvCountStmt.executeQuery(getDvCountQry);
    		int dvCount=0;
    		Date lastFileDate=null;
    		Date lastTransDate=null;
    		while(getDvCountResult.next()){
    			String filDateStr=null;
    			String transDateStr=null;
    			dvCount=getDvCountResult.getInt(3);
    			log.info("dvCount: "+dvCount);
    			if(dvCount>0){
    				log.info("getDvCountResult.getString(1): "+getDvCountResult.getString(1));
    				log.info("getDvCountResult.getDate(1): "+getDvCountResult.getDate(1));
    				log.info("getDvCountResult.getTimestamp(1): "+getDvCountResult.getTimestamp(1).toInstant());
    				log.info("getDvCountResult.getTimestamp(2): "+getDvCountResult.getTimestamp(2));
    				/*filDateStr=getDvCountResult.getString(1);
    				System.out.println("filDateStr b4: "+filDateStr);
    				if(filDateStr.length()==21)
    				filDateStr=filDateStr.substring(0, filDateStr.length()-2);
    				System.out.println("filDateStr aftr: "+filDateStr);
    				
    				transDateStr=getDvCountResult.getString(2);
    				System.out.println("transDateStr b4: "+transDateStr);
    				if(transDateStr.length()==21)
    				transDateStr=transDateStr.substring(0, transDateStr.length()-2);
    				System.out.println("transDateStr: "+transDateStr);
    				SimpleDateFormat sdfSource = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");*/
    				
    				
    			}
    			DataViews dataViewFinalRecord=dataViewsRepository.findOne(dv.getId());
    			dataViewFinalRecord.setDataViewName(tableName);
    			dataViewFinalRecord.setRecordCount(dvCount);
    			if(getDvCountResult.getTimestamp(1)!=null){
    				log.info("getDvCountResult.getTimestamp(1): "+getDvCountResult.getTimestamp(1));
    				log.info("getDvCountResult.getTimestamp(1).toInstant(): "+getDvCountResult.getTimestamp(1).toInstant());
    			dataViewFinalRecord.setLastFileDate(ZonedDateTime.parse(getDvCountResult.getTimestamp(1).toInstant().toString()));
    			}
    			if(getDvCountResult.getTimestamp(2)!=null){
    				log.info("getDvCountResult.getTimestamp(2): "+getDvCountResult.getTimestamp(2));
    				log.info("getDvCountResult.getTimestamp(2).toInstant(): "+getDvCountResult.getTimestamp(2).toInstant());
    			dataViewFinalRecord.setLastTransDate(ZonedDateTime.parse(getDvCountResult.getTimestamp(2).toInstant().toString()));
    			}
    			dataViewsRepository.save(dataViewFinalRecord);
    			log.info("Updated data View count for dataview: "+tableName);
    		}

    	}catch(SQLException se){
    		errorReport.setTaskStatus("Failed to create view ");
    		errorReport.setDetails("Please correct your function");
    		log.info("se: "+se);
    	}
    	finally{
    		if(existrs!=null)
    			existrs.close();
    		if(getDvCountResult!=null)
    			getDvCountResult.close();
    		if(viewExtRs!=null)
    			viewExtRs.close();
    		if(stmt!=null)
    			stmt.close();
    		if(insertColStmt!=null)
    			insertColStmt.close();
    		if(stmt2!=null)
    			stmt2.close();
    		if(stmt3!=null)
    			stmt3.close();
    		if(indxStmt!=null)
    			indxStmt.close();
    		if(indexStmt!=null)
    			indexStmt.close();
    		if(getDvCountStmt!=null)
    			getDvCountStmt.close();
    		if(helpStringFunStmt!=null)
    			helpStringFunStmt.close();
    		if(alterTableStmt!=null)
    			alterTableStmt.close();
    		if(dropViewStmt!=null)
    			dropViewStmt.close();
    		if(checkViewExist!=null)
    			checkViewExist.close();
    		
    		if(conn!=null)
    			conn.close();
    	}
    	return errorReport;
    }
    
    /**
     * Author:Ravali
     * API to update DVColumns and Unions
     * @param dVColsNUnions
     * @param tenantId
     * @param userId
     * @return
     */
    @PostMapping("/updateDataViewColsNUnions")
    @Timed
    public LinkedHashMap updateDataViewColumnsNUnion(@RequestBody HashMap dVColsNUnions, HttpServletRequest request){
    	log.info("Rest Request to post dataViewColumns or Template Lines");
    	HashMap userTenantMap=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(userTenantMap.get("tenantId").toString());
    	Long userId=Long.parseLong(userTenantMap.get("userId").toString());

    	LinkedHashMap createOrUpdateMap=new LinkedHashMap();

    	DataViews dv=new DataViews();
    	HashMap map=new HashMap();
    	List<String> tempIdList=new ArrayList<String>();
    	log.info("dVColsNUnions :"+dVColsNUnions);
    	String dvcColName="";
    	String dType="";
    	String formula="";
    	Boolean grpByQualifier=null;
    	DataViewsColumns dvc=new DataViewsColumns();

    	log.info("obj :"+dVColsNUnions);
    	if(dVColsNUnions.get("id")==null){
    		log.info("New DataView Column Creation");
    		dvc.setCreatedBy(userId);
    		map.put("createdBy",userId);
    		dvc.setCreationDate(ZonedDateTime.now());
    		map.put("creationDate", ZonedDateTime.now());
    	}
    	else{
    		if(dVColsNUnions.get("id")!=null)
    			dvc=dataViewsColumnsRepository.findOne(Long.parseLong(dVColsNUnions.get("id").toString()));
    		dvcColName=dvc.getColumnName();
    		dType=dvc.getColDataType();
    		grpByQualifier=dvc.getGroupBy();
    		//createOrUpdateMap.put("Update", dVColsNUnions.get("id"));
    		log.info("Updating Data View union column");
    	}

    	if(dVColsNUnions.get("colDataType")!=null)
    	{
    		dvc.setColDataType(dVColsNUnions.get("colDataType").toString());
    		map.put("colDataType", dVColsNUnions.get("colDataType").toString());
    	}

    	if(dType!=null && !(dType.isEmpty()) && !(dType.equalsIgnoreCase(""))){
    		if(!(dType.equalsIgnoreCase(dVColsNUnions.get("colDataType").toString()))){
    			createOrUpdateMap.put("DataTypeUpdated", dvcColName);
    		}

    	}

    	if(dVColsNUnions.get("dataViewId")!=null)
    	{
    		dv=dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, dVColsNUnions.get("dataViewId").toString());
    		dvc.setDataViewId(dv.getId());
    		map.put("dataViewId", dv.getIdForDisplay());

    	}
    	if(dVColsNUnions.get("qualifier")!=null)
    	{
    		dvc.setQualifier(dVColsNUnions.get("qualifier").toString());
    		map.put("qualifier", dVColsNUnions.get("qualifier").toString());

    	}
    	if(dVColsNUnions.get("columnName")!=null)
    	{
    		dvc.setColumnName(dVColsNUnions.get("columnName").toString().trim());
    		map.put("columnName", dVColsNUnions.get("columnName").toString());

    	}
    	if(dvcColName!=null && !(dvcColName.isEmpty()) && !(dvcColName.equalsIgnoreCase(""))){
    		if(!(dvcColName.equalsIgnoreCase(dVColsNUnions.get("columnName").toString()))){
    			createOrUpdateMap.put("ColumnRenamed", dvcColName);
    		}

    	}
    	if(dVColsNUnions.get("formula")!=null)
    	{
    		log.info(" formula: "+dVColsNUnions.get("formula"));
    		dvc.setFormula(dVColsNUnions.get("formula").toString());
    		map.put("formula", dVColsNUnions.get("formula").toString());
    		//createOrUpdateMap.put(, dvcColName);
    	}

    	if(dVColsNUnions.get("groupBy")!=null)
    	{
    		log.info(" groupBy: "+dVColsNUnions.get("groupBy"));
    		dvc.setGroupBy((Boolean) dVColsNUnions.get("groupBy"));
    		map.put("groupBy", dVColsNUnions.get("groupBy").toString());
    	}
    	
    	log.info("grpByQualifier b4 check: "+grpByQualifier);
    	
    	if(grpByQualifier!=null && dVColsNUnions.get("groupBy")!=null && !(dVColsNUnions.get("groupBy").toString().isEmpty())){
    		if(grpByQualifier.compareTo((Boolean)dVColsNUnions.get("groupBy"))==0){
    			log.info("No change in groupBy Qualifier");
    		}
    		else{
    			log.info("group by qualifier changed");
    			createOrUpdateMap.put("groupByQualifier", dvcColName);
    		}
    			
    	}
    	else if(dVColsNUnions.get("groupBy")!=null && (Boolean)dVColsNUnions.get("groupBy").equals(true)){
    		log.info("Previously NO groupBY flag enable, But now its enabled");
    		createOrUpdateMap.put("groupByQualifier", dvcColName);
    	}
    	
    	DataViewsColumns newDvc=dataViewsColumnsRepository.save(dvc);
    	log.info("newDvc: "+newDvc);
    	log.info("dvc.getId() :"+newDvc.getId());
    	if(dVColsNUnions.get("id")==null && newDvc.getId()!=null){
    		createOrUpdateMap.put("Create", newDvc.getId());
    	}
    	createOrUpdateMap.put("colId", newDvc.getId());
    	int dvUnionUpdates=0;
    	if(dVColsNUnions.get("src")!=null)
    	{
    		List unionsList=(List) dVColsNUnions.get("src");
    		log.info("unionsList.size :"+unionsList.size());
    		List<HashMap> dvuListMap=new ArrayList<HashMap>();
    		for(int i=0;i<unionsList.size();i++)
    		{
    			DataViewUnion dvu=new DataViewUnion();
    			HashMap dvuMap=new HashMap();
    			DataViewUnion dvuId=new DataViewUnion();
    			HashMap uni=(HashMap) unionsList.get(i);
    			log.info("uni: "+uni);
    			if(uni.get("id")!=null)
    			{
    				log.info("Data View Union Id already Exists with id: "+uni.get("id"));
    				dvu.setId(Long.parseLong(uni.get("id").toString()));
    				dvuMap.put("id", Long.parseLong(uni.get("id").toString()));
    				if(uni.get("refDvName")!=null)
    				{
    					FileTemplates ft=fileTemplatesRepository.findByIdForDisplayAndTenantId(uni.get("refDvName").toString(), tenantId);
    					dvu.setRefDvName(ft.getId());
    					dvuMap.put("refDvName", uni.get("refDvName").toString());
    					tempIdList.add((uni.get("refDvName").toString()));
    				}
    				if(uni.get("refDvType")!=null)
    				{
    					dvu.setRefDvType(uni.get("refDvType").toString());
    					dvuMap.put("refDvType", uni.get("refDvType").toString());

    				}
    				if(uni.get("refDvColumn")!=null){
    					if(uni.get("refDvColumn").toString().equalsIgnoreCase("customFunction"))
    					{
    						dvu.setRefDvColumn(null);
    						dvuMap.put("refDvColumn",null);
    					}
    					else if(uni.get("refDvColumn").toString().equalsIgnoreCase("none"))
    					{
    						dvu.setRefDvColumn(null);
    						dvuMap.put("refDvColumn",null);
    					}
    					else{
    						dvu.setRefDvColumn(Long.parseLong(uni.get("refDvColumn").toString()));
    						dvuMap.put("refDvColumn",uni.get("refDvColumn").toString());
    					}
    				}
    				if(uni.get("intermediateId") !=null)
    				{
    					dvu.setIntermediateId(Long.parseLong(uni.get("intermediateId").toString()));
    				}
    				else{
    					dvu.setIntermediateId(null);
    				}
    				log.info("uni.get(excelexpressioninputUnion): "+uni.get("excelexpressioninputUnion"));
    				if(uni.get("excelexpressioninputUnion")!=null){
    					dvu.setFormula(uni.get("excelexpressioninputUnion").toString());
    					String Str=dataViewsService.excelFormulasUpd(uni.get("excelexpressioninputUnion").toString(), tenantId,dv.getId(),"UNION");
    					log.info("formula alias: "+Str);
    					dvu.setFormulaAlias(Str);

    				}
    				dvu.setDataViewLineId(newDvc.getId());
    				dvuMap.put("dataViewLineId",newDvc.getId());
    				dvu.setLastUpdatedDate(ZonedDateTime.now());
    				dvu.setLastUpdatedBy(userId);
    				log.info("dvu b4 saving: "+dvu);
    				dvuId=dataviewUnionRepositorty.save(dvu);
    				dvuListMap.add(dvuMap);
    			}
    			else
    			{
    				log.info("Data View Union Id doesn't Exists with id: "+uni.get("id"));

    				if(uni.get("refDvName")!=null)
    				{
    					log.info("uni.get(refDvName).toString(): "+uni.get("refDvName").toString());
    					FileTemplates ftData=fileTemplatesRepository.findByIdForDisplayAndTenantId(uni.get("refDvName").toString(), tenantId);
    					dvu.setRefDvName(ftData.getId());
    					dvuMap.put("refDvName", uni.get("refDvName").toString());
    				}
    				if(uni.get("refDvType")!=null)
    				{
    					dvu.setRefDvType(uni.get("refDvType").toString());
    					dvuMap.put("refDvType", uni.get("refDvType").toString());

    				}
    				if(uni.get("refDvColumn")!=null){
    					if(uni.get("refDvColumn").toString().equalsIgnoreCase("customFunction"))
    					{
    						dvu.setRefDvColumn(null);
    						dvuMap.put("refDvColumn",null);
    					}
    					else if(uni.get("refDvColumn").toString().equalsIgnoreCase("None")){
    						dvu.setRefDvColumn(null);
    						dvuMap.put("refDvColumn",null);
    					}
    					else{
    						dvu.setRefDvColumn(Long.parseLong(uni.get("refDvColumn").toString()));
    						dvuMap.put("refDvColumn",uni.get("refDvColumn").toString());
    					}
    				}
    				if(uni.get("intermediateId") !=null)
    				{
    					dvu.setIntermediateId(Long.parseLong(uni.get("intermediateId").toString()));
    				}
    				else{
    					dvu.setIntermediateId(null);
    				}
    				if(uni.get("excelexpressioninputUnion")!=null){
    					dvu.setFormula(uni.get("excelexpressioninputUnion").toString());
    					String Str=dataViewsService.excelFormulasUpd(uni.get("excelexpressioninputUnion").toString(), tenantId,dv.getId(),"UNION");
    					dvu.setFormulaAlias(Str);

    				}
    				dvu.setDataViewLineId(newDvc.getId());
    				dvuMap.put("dataViewLineId",newDvc.getId());
    				dvu.setLastUpdatedDate(ZonedDateTime.now());
    				dvu.setLastUpdatedBy(userId);
    				dvuId=dataviewUnionRepositorty.save(dvu);
    				log.info("");
    				dvuMap.put("id", dvuId.getId());
    				dvuListMap.add(dvuMap);

    			}

    			log.info("dvuId :"+dvuId);
    		}
    		map.put("src", dvuListMap);
    	}
    	log.info("tempIdList: "+tempIdList);
    	log.info("newDvc.getId() :"+newDvc.getId());
    	List<BigInteger> dvunionList=dataviewUnionRepositorty.fetchDistinctTemplateIdsByViewId(dv.getId());
    	log.info("dvunionList :"+dvunionList);
    	for(int y=0;y<dvunionList.size();y++){
    		List<DataViewsSrcMappings> dvsm=dataViewsSrcMappingsRepository.findByDataViewIdAndRelationAndTemplateId(dv.getId(),"UNION",dvunionList.get(y).longValue());
    		if(dvsm.size()==0)
    		{
    			if(dvunionList.get(y)!=null){
    				Long temId=dvunionList.get(y).longValue();
    				DataViewsSrcMappings dvSrcMap=new DataViewsSrcMappings();

    				dvSrcMap.setDataViewId(newDvc.getDataViewId());
    				dvSrcMap.setTemplateId(temId);
    				dvSrcMap.setRelation("UNION");
    				dvSrcMap.setCreationDate(ZonedDateTime.now());
    				dvSrcMap.setCreatedBy(userId);
    				dataViewsSrcMappingsRepository.save(dvSrcMap);
    			}
    		}
    	}

    	return createOrUpdateMap;

    }
    
    /**
     * author: Ravali
     * @param id
     * @return data view column name
     * API to delete data view column and unions 
     */
    @DeleteMapping("/dataViewsColumnAndUnionDelete")
    @Timed
    public LinkedHashMap dvcAndUnionDel(@RequestParam Long id)
    {
    	log.info("Request rest to delete data view columns and data view unions");
    	DataViewsColumns dvc=dataViewsColumnsRepository.findOne(id);
    	LinkedHashMap map=new LinkedHashMap();
    	List<DataViewUnion> dvu=dataviewUnionRepositorty.findByDataViewLineId(id);
    	dataViewsColumnsRepository.delete(dvc);
    	dataviewUnionRepositorty.delete(dvu);
    	map.put("Delete", dvc.getId());
    	map.put("colId", dvc.getId());
    	return map;
    }

    /**
     * Author: Shiva
     * Description: API for getting decimal columns based on view id and datatype
     * @param viewId
     * @throws ClassNotFoundException
     * @throws SQLException 
     */    
    @GetMapping("/getDecimalColumnsByViewIdNType")
    @Timed
    public List<HashMap> getDecimalColumnsByViewId(HttpServletRequest request, @RequestParam String viewId, @RequestParam String dataType)
    {
    	log.info("Rest api for fetching decimal columns for the view id: "+ viewId);
    	List<HashMap> finalList = new ArrayList<HashMap>();
    	
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	Long userId = Long.parseLong(map.get("userId").toString());
    	
    	DataViews sdv = dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, viewId);
    	Long dataViewId = sdv.getId();
    	
    	List<DataViewsColumns> dvcs = dataViewsColumnsRepository.findByDataViewIdAndColDataType(dataViewId, dataType);
    	if(dvcs.size()>0)
    	{
    		for(DataViewsColumns dvc : dvcs)
    		{
    			HashMap hm = new HashMap();
    			hm.put("id", dvc.getId());
    			hm.put("columnName", dvc.getColumnName());
   				finalList.add(hm);
   			}
   		}
    	return finalList;
    }
    
    /**
     * Author: Swetha
     * @param viewOrTemplateList
     * @return
     */
    @PostMapping("/getTemplateLines")
    @Timed
    public List getTemplateLines(@RequestBody List<JSONObject> viewOrTemplateList, HttpServletRequest request){
    	log.info("Rest Request to get dataViewColumns or Template Lines");
    	log.info("viewOrTemplateList :"+viewOrTemplateList);

    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());

    	int sz=viewOrTemplateList.size();
    	List finalList=new ArrayList<>();
    	List<Long> dvIdList=new ArrayList<Long>();
    	List<String> tempIdList=new ArrayList<String>();
    	List<Long> intermIdList=new ArrayList<Long>();
    	List<HashMap> ftIdTiIntIdMapList=new ArrayList<HashMap>();
    	for(int i=0;i<sz;i++){
    		JSONObject obj=viewOrTemplateList.get(i);
    		if(obj.get("type")!=null && obj.get("type").toString().equalsIgnoreCase("File Template")){
    			HashMap ftIdTiIntIdMap=new HashMap();
    			Long intermediateId=null;
    			if(obj.containsKey("typeId"))
    				if(obj.get("typeId")!=null)
    					tempIdList.add(obj.get("typeId").toString());
    			if(obj.containsKey("intermediateId")){
    				if(obj.get("intermediateId")!=null && obj.get("intermediateId").toString()!=null){
    					intermIdList.add(Long.parseLong(obj.get("intermediateId").toString()));
    					intermediateId= Long.parseLong(obj.get("intermediateId").toString());
    				}
    			}
    			ftIdTiIntIdMap.put(obj.get("typeId").toString(),intermediateId);
    			ftIdTiIntIdMapList.add(ftIdTiIntIdMap);
    		}
    	}
    	tempIdList = tempIdList.stream().distinct().collect(Collectors.toList());
    	List<FileTemplateLines> ftempList=new ArrayList<FileTemplateLines>();

    	//Separating Multi and Single Identifier Template Lines
    	HashMap tempMap=new HashMap();
    	List<Long> nonMultitempIdsList=new ArrayList<Long>();
    	List<Long> multiTempIdsList=new ArrayList<Long>();
    	for(int k=0;k<tempIdList.size();k++){
    		String tempIdForDisplay=tempIdList.get(k);
    		FileTemplates ft=fileTemplatesRepository.findByIdForDisplayAndTenantId(tempIdForDisplay, tenantId);
    		Long tempId=ft.getId();
    		Boolean multiId=ft.getMultipleIdentifier();
    		if(multiId==true){
    			multiTempIdsList.add(tempId);
    		}
    		else {
    			nonMultitempIdsList.add(tempId);
    		}
    	}

    	if((nonMultitempIdsList!=null && !(nonMultitempIdsList.isEmpty()) && nonMultitempIdsList.size()>0) && (multiTempIdsList!=null && !(multiTempIdsList.isEmpty()) && multiTempIdsList.size()>0)) {

    		List nonMultiftlMapList=fileTemplatesService.getFileTempleLinesForTemplateTest(ftIdTiIntIdMapList,tenantId);
    		finalList.addAll(nonMultiftlMapList);
    	}
    	else
    	{
    		if(nonMultitempIdsList!=null && !(nonMultitempIdsList.isEmpty()) && nonMultitempIdsList.size()>0){
    			List nonMultiftlMapList=fileTemplatesService.getFileTempleLinesForTemplate(nonMultitempIdsList,"nonMultiId",ftIdTiIntIdMapList,tenantId);
    			finalList.addAll(nonMultiftlMapList);
    		}
    		if(multiTempIdsList!=null && !(multiTempIdsList.isEmpty()) && multiTempIdsList.size()>0){
    			List multiftlMapList=fileTemplatesService.getFileTempleLinesForTemplate(multiTempIdsList,"multiId",ftIdTiIntIdMapList,tenantId);
    			finalList.addAll(multiftlMapList);
    		}
    	}

    	return finalList;

    }
    
    /**
     * Author: Swetha
     * @param formula
     * @param currentTemplate
     * @param request
     * @return
     */
    @PostMapping("/validateCurrentTemplate")
    @Timed
    public ErrorReport validateCurrentTemplate(@RequestParam String formula, @RequestBody List<String> currentTemplateList, HttpServletRequest request){
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	ErrorReport errorReport=new ErrorReport();
    	Long tenantId=0L;
    	StatusStringDTO dto=new StatusStringDTO();
    	if(map!=null && map.containsKey("tenantId")){
    		tenantId=Long.parseLong(map.get("tenantId").toString());
    		errorReport=dataViewsService.distinctTemplates(formula, tenantId, currentTemplateList);
    	}
    	else{
    		log.info("TenantId doesnt exist");
    	}
		return errorReport;
    }
    
    
    
    /** 
     * author:ravali
     * @param viewId
     * @param response
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws IOException
     * @Desc: file export 
     */
    /** returning file in response body instead of storing it in file server**/
  
    @GetMapping("/getDataViewsDataCSV")
    @Timed
    public HashMap getDataViewsDataCSV(@RequestParam String viewId,HttpServletResponse response, HttpServletRequest request,@RequestParam String fileType) throws ClassNotFoundException, SQLException, IOException {

    	// DataViews dv=dataViewsRepository.findOne(viewId);
    	HashMap userTenantMap=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(userTenantMap.get("tenantId").toString());
    	DataViews dview=dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, viewId);
    	DataViews dv=dataViewsRepository.findOne(dview.getId());
    	HashMap finalMap=new HashMap();

    	/*ApplicationPrograms prog=applicationProgramsRepository.findByPrgmNameAndTenantIdAndEnableIsTrue("DataExtraction",dv.getTenantId());
    	String localPath=prog.getGeneratedPath();*/
    	HashMap finMap=new HashMap();
    	int count=0;
    	finMap=dataViewsService.getDataViewsDataWithoutPagination(dview.getId());
    	List<LinkedHashMap> values=(List<LinkedHashMap>) finMap.get("mapList");
    	List<String> head=new ArrayList<String>();
    	List<LinkedHashMap> attributeNames=new ArrayList<LinkedHashMap>();

    	List<DataViewsColumns> dvColumnsList=dataViewsColumnsRepository.findByDataViewId(dview.getId());

    	for(DataViewsColumns dvc:dvColumnsList)
    	{

    		LinkedHashMap dvcMap=new LinkedHashMap();

    		head.add( dvc.getColumnName());
    		dvcMap.put("field", dvc.getColumnName());
    		dvcMap.put("header",  dvc.getColumnName());
    		if(dvc.getColDataType().equalsIgnoreCase("DATE"))
    			dvcMap.put("align", "center");
    		else if(dvc.getColDataType().equalsIgnoreCase("DECIMAL"))
    			dvcMap.put("align", "right");
    		else
    			dvcMap.put("align", "left");
    		attributeNames.add(dvcMap);


    	}

    	log.info("head :"+head);
    	if(fileType.equalsIgnoreCase("csv"))
    	{
    		response.setContentType ("application/csv");
    		response.setHeader ("Content-Disposition", "attachment; filename=\"dataview.csv\"");

    		fileExportService.jsonToCSV(values,head,response.getWriter());



    	}
    	if(fileType.equalsIgnoreCase("pdf"))
    	{
    		response.setContentType ("application/pdf");
    		response.setHeader ("Content-Disposition", "attachment; filename=\"dataview.pdf\"");

    		fileExportService.jsonToCSV(values, head,response.getWriter());



    	}
    	else if(fileType.equalsIgnoreCase("excel"))
    	{
    		/*response.setContentType("application/vnd.ms-excel");
    		response.setHeader(
    				"Content-Disposition",
    				"attachment; filename=\"excel-export-dataview.xlsx\""
    				);
    		fileExportService.jsonToCSVForReports(values, head,response.getWriter());*/
    		response=fileExportService.exportToExcel(response, head, values);
    		
    	}

    	return finalMap;
    }
    
    @GetMapping("/testFormula")
    @Timed
    public void testFormula(@RequestParam String formula,@RequestParam Long tenanatId,@RequestParam Long dataViewId, @RequestParam String relation){
    	
    	String finformula=dataViewsService.excelFormulasUpd(formula, tenanatId, dataViewId, relation);
    	log.info("finformula: "+finformula);
    	
    }
    @GetMapping("/fetchDataViewColumnsbyDvId")
    @Timed
    public List<DataViewsColumns> fetchDataViewColumnsbyDvId(HttpServletRequest request,@RequestParam() String dataViewId)
    {
    	log.info("Rest request to fetch data view columns by data view id"+dataViewId);
    	HashMap userTenantMap=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(userTenantMap.get("tenantId").toString());
    	List<DataViewsColumns> dvCols = new ArrayList<DataViewsColumns>();
    	DataViews dv = new DataViews();
    	dv =dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, dataViewId);
    	dvCols = dataViewsColumnsRepository.findByDataViewId(dv.getId());
    	return dvCols;
    }
    @GetMapping("/fetchDataViewsToDataViewColumnsMap")
    @Timed
    public HashMap<String,List<DataViewsColumns>> fetchDataViewsToDataViewColumnsMap(HttpServletRequest request)
    {
    	log.info("Rest request to fetch map of dataview id to columns");
    	 HashMap<String,List<DataViewsColumns>> map = new HashMap<String, List<DataViewsColumns>>();
    	HashMap userTenantMap=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(userTenantMap.get("tenantId").toString());
    	List<DataViews> dvs = new ArrayList<DataViews>();
    	dvs = dataViewsRepository.findByTenantId(tenantId);
    	for(DataViews dv : dvs)
    	{
    		List<DataViewsColumns> dvCols = new ArrayList<DataViewsColumns>();
        	dvCols = dataViewsColumnsRepository.findByDataViewId(dv.getId());
        	map.put(dv.getIdForDisplay(),dvCols);
    	}
    	
    	return map;
    }
    
    
    /**
     * Author: Swetha
     * @param viewId
     * @param updateMap
     * @param request
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    @PostMapping("/updatePhysicalView")
    @Timed
    public ErrorReport updatePhysicalView(@RequestParam String viewId, @RequestBody LinkedHashMap updateMap,HttpServletRequest request ) 
    		throws ClassNotFoundException, SQLException {

    	log.info("Rest Request to updatePhysicalView with viewId :"+viewId);
    	log.info("updateMap: "+updateMap);
    	String createOrUpdateKey=null;
    	
    	/*ErrorReport errorReport=new ErrorReport();
    	errorReport.setTaskName("physical view creation");*/
    	
    	Long colId=Long.parseLong(updateMap.get("colId").toString());
    	String val="";
    	String colType="";
    	Boolean grpByColVal=null;
    	
    	String insertQuery="";
    	String updColDataQuery="";
    	String deleteQuery="";
    	String truncateQuery="";
    	String dataLoadQuery="";
    	String columnRenameQry="";
    	String colDataTypeQry="";
    	String dropIndexQuery="";
    	String checkIndexNameExistsQuery="";
    	String createColIndxQuery="";
    	

    	String colName="";
    	String relation="";
    	int keysSz=0;
    	Set keyset=null;
    	List<String> keyList=new ArrayList<String>(); 

    	if(updateMap!=null && !(updateMap.isEmpty())){
    		keyset=updateMap.keySet();
    		keysSz=keyset.size();
    		log.info("keysSz: "+keysSz);

    		Iterator itr=keyset.iterator();
    		int count=0;
    		String key="";
    		String value="";
    		while(itr.hasNext()){
    			key=itr.next().toString();
    			value=updateMap.get(key).toString();
    			log.info("key: "+key+" value: "+value);

    			if(count==0){
    				createOrUpdateKey=key;
    				val=value;
    				log.info("First key: "+key+" and value: "+value);
    			}

    			keyList.add(key);
    			count++;
    		}

    		log.info("keyList: "+keyList);
    	}
    	log.info("createOrUpdateKey: "+createOrUpdateKey+", colId: "+colId);
    	
    	/*if(createOrUpdateKey!=null && !(createOrUpdateKey.isEmpty()) && (keyList!=null && keyList.size()>0 && !(keyList.contains("Delete")) && !(keyList.contains("dvConditionUpdated")))){
    	DataViewsColumns dvc1=dataViewsColumnsRepository.findOne(colId);
    	log.info("dvc1: "+dvc1);
    	Long tempLineId=0L;
    	String fileDateMasterRefCol="";
    	String masterCol="";
    	Long tempIdForFileDate=0L;
    		tempLineId=Long.parseLong(dvc1.getRefDvColumn());
    		tempIdForFileDate=Long.parseLong(dvc1.getRefDvName());
    		FileTemplateLines ftlForFileDate=fileTemplateLinesRepository.findByTemplateIdAndMasterTableReferenceColumn(tempIdForFileDate, "file_date");
    		log.info("ftlForFileDate: "+ftlForFileDate);
    		FileTemplateLines ftl1=fileTemplateLinesRepository.findOne(tempLineId);
    		log.info("ftl1: "+ftl1);
    		masterCol=ftl1.getMasterTableReferenceColumn();
    		log.info("masterCol: "+masterCol);
    		if(ftlForFileDate!=null){
    			fileDateMasterRefCol=ftlForFileDate.getMasterTableReferenceColumn();
    			log.info("fileDateMasterRefCol: "+fileDateMasterRefCol);
    		}
    		if(fileDateMasterRefCol.equalsIgnoreCase(masterCol)){
    			log.info("FileDate Column matched ");
    			errorReport.setTaskStatus("FileDate Column Added");
    			errorReport.setDetails("FileDate Column Added, No changes in Physical View");
    			return errorReport;
    		}
    	}*/

    	if(createOrUpdateKey!=null && !(createOrUpdateKey.isEmpty()) && (keyList!=null && keyList.size()>0 && !(keyList.contains("Delete")) && !(keyList.contains("dvConditionUpdated")))){
    		DataViewsColumns dvc=dataViewsColumnsRepository.findOne(colId);
    		Long dvId=dvc.getDataViewId();
    		grpByColVal=dvc.getGroupBy();

    		relation=dataViewsSrcMappingsRepository.fetchDVRelationByViewId(dvId);
    		log.info("relation: "+relation);
    		if(dvc.getRefDvType()!=null && dvc.getRefDvType().equalsIgnoreCase("File Template")){
    			if(relation==null || (relation!=null && relation.equalsIgnoreCase("JOIN"))){
    				log.info("dvc.getRefDvColumn(): "+dvc.getRefDvColumn());
    				FileTemplateLines ftl=fileTemplateLinesRepository.findOne(Long.parseLong(dvc.getRefDvColumn()));
    				//log.info("ftl: "+ftl);
    				colName=ftl.getColumnAlias();
    			}
    			else if(relation!=null && relation.equalsIgnoreCase("UNION")){
    				colName=dvc.getColumnName();
    			}
    		}
    		else colName=dvc.getColumnName();

    	}

    	ErrorReport errorReport=new ErrorReport();
    	errorReport.setTaskName("physical view creation");
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	Connection conn = null;
    	Statement stmt = null;
    	Statement stmt2=null;
    	Statement stmt3=null;
    	Statement indxStmt=null;
    	Statement insertColStmt=null;
    	Statement updateColDataStmt=null;
    	Statement safeUpdStmt=null;
    	Statement delColStmt=null;
    	Statement truncateStmt=null;
    	Statement dataLoadStmt=null;
    	Statement colRenameStmt=null;
    	Statement colDTypeStmt=null;
    	//Statement dropIndexStmt=null;
    	ResultSet existrs=null;
    	Statement checkIndexNameExistsStmt=null;
    	ResultSet indxExistRs=null;
    	Statement createColIndxStmt=null;
    	ResultSet getDvCountResult=null;
    	Statement getDvCountStmt=null;


    	try{
    		DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
    		conn=ds.getConnection();
    		log.info("Connected database successfully...");
    		stmt = conn.createStatement();
    		stmt2=conn.createStatement();
    		stmt3=conn.createStatement();
    		indxStmt=conn.createStatement();
    		insertColStmt=conn.createStatement();
    		updateColDataStmt=conn.createStatement();
    		safeUpdStmt=conn.createStatement();
    		delColStmt=conn.createStatement();
    		truncateStmt=conn.createStatement();
    		dataLoadStmt=conn.createStatement();
    		colRenameStmt=conn.createStatement();
    		colDTypeStmt=conn.createStatement();
    		//dropIndexStmt=conn.createStatement();
    		checkIndexNameExistsStmt=conn.createStatement();
    		createColIndxStmt=conn.createStatement();
    		getDvCountStmt=conn.createStatement();

    		String dbUrl=env.getProperty("spring.datasource.url");
    		String[] parts=dbUrl.split("[\\s@&?$+-]+");
    		String schemaName=parts[0].split("/")[3];

    		DataViews dv=dataViewsRepository.findByTenantIdAndIdForDisplay(tenantId, viewId);
    		String physicalViewName=dv.getDataViewName()+"_v";
    		physicalViewName=physicalViewName.toLowerCase();
    		String createViewQuery="create or replace view `"+physicalViewName+"` as  " ;
    		List<DataViewsSrcMappings> dvSrcMapList=dataViewsSrcMappingsRepository.findByDataViewIdAndRelation(dv.getId(), "UNION");
    		String viewQuery="";
    		if(dvSrcMapList!=null && dvSrcMapList.size()!=0){
    			viewQuery=dataViewsService.frameUnionsQuery(dv.getId(), tenantId);
    		}
    		else viewQuery=dataViewsService.frameQuery(dv.getId(), tenantId);
    		log.info("viewQuery in createDataView: "+viewQuery);
    		viewQuery=createViewQuery+viewQuery;
    		log.info("final view creation query viewQuery: "+viewQuery);
    		int out=stmt.executeUpdate(viewQuery);
    		log.info("view creation query executed with out: "+out);

    		String tableName=dv.getDataViewName().toLowerCase();

    		String checkTableExistence=" SELECT count(*) FROM information_schema.tables WHERE table_schema = '"+schemaName+"' AND table_name = '"+tableName+"' LIMIT 1";
    		existrs=stmt2.executeQuery(checkTableExistence);

    		String existCnt=null;
    		while(existrs.next()){
    			existCnt=existrs.getString(1);
    		}
    		log.info("table existence query executed with existCnt: "+existCnt);

    		if(existCnt.equalsIgnoreCase("1")){
    			log.info("tableName: "+tableName+" already exists");

    			String safeUpdQuery="set sql_safe_updates=0";

    			if(keyList!=null && keyList.size()>0 && keyList.contains("Create") ){

    				log.info("physicalViewName: "+physicalViewName+" colName: "+colName);
    				List<Object[]> colInfoList=dataViewsRepository.fetchColumnInfo(physicalViewName, colName);
    				Object[] colInfo=colInfoList.get(0);
    				if(colInfo!=null && colInfo.length>0){
    					log.info("colInfo[0]: "+colInfo[0]);
    					log.info(" colInfo[3]: "+colInfo[3]);
    					colType=colInfo[4].toString();
    				}
    				log.info("colType: "+colType);
    				insertQuery="ALTER TABLE `"+tableName+"` ADD `"+colName+"` "+colType;
    				log.info("insertQuery: "+insertQuery);
    				int colInsertIdx=insertColStmt.executeUpdate(insertQuery);
    				log.info("Column "+colName+" inserted with colInsertIdx"+colInsertIdx);

    				updColDataQuery=" UPDATE `"+tableName+"` SET `"+colName+"` = ( "
    						+" SELECT `"+colName+"` FROM `"+physicalViewName+"` WHERE `"+tableName+"`.scrIds = `"+physicalViewName+"`.scrIds )";
    				int updColDataIndx=updateColDataStmt.executeUpdate(updColDataQuery);
    				log.info("updateColDataStmt executed with updColDataIndx: "+updColDataIndx);
    			}

    			int safeUpdIndx=safeUpdStmt.executeUpdate(safeUpdQuery);
    			log.info("safeUpdStmt executed with safeUpdIndx: "+safeUpdIndx);

    			if(keyList!=null && keyList.size()>0 && keyList.contains("ColumnRenamed")){
    				List<Object[]> colInfoList=dataViewsRepository.fetchColumnInfo(physicalViewName, colName);
    				Object[] colInfo=colInfoList.get(0);
    				if(colInfo!=null && colInfo.length>0){
    					log.info("colInfo[0]: "+colInfo[0]);
    					log.info(" colInfo[3]: "+colInfo[3]);
    					colType=colInfo[4].toString();
    				}
    				log.info("colType: "+colType);
    				String originalColName=updateMap.get("ColumnRenamed").toString();
    				log.info("originalColName: "+originalColName);
    				columnRenameQry=" ALTER TABLE `"+tableName+"` CHANGE COLUMN `"+originalColName+"` `"+colName+"`"+colType+" NULL DEFAULT NULL ";
    				int colRenameIndx=colRenameStmt.executeUpdate(columnRenameQry);
    				log.info("columnRenameQry executed with colRenameIndx: "+colRenameIndx);
    			}

    			//DataTypeUpdated
    			if(keyList!=null && keyList.size()>0 && (keyList.contains("DataTypeUpdated") || keyList.contains("FormulaUpdated"))){

    				if(!(keyList.contains("FormulaUpdated"))){
    					List<Object[]> colInfoList=dataViewsRepository.fetchColumnInfo(physicalViewName, colName);
    					Object[] colInfo=colInfoList.get(0);
    					if(colInfo!=null && colInfo.length>0){
    						log.info("colInfo[0]: "+colInfo[0]);
    						log.info(" colInfo[3]: "+colInfo[3]);
    						colType=colInfo[4].toString();
    					}
    					log.info("colType: "+colType);
    					String originalColName=updateMap.get("DataTypeUpdated").toString();
    					colDataTypeQry=" ALTER TABLE `"+tableName+"` CHANGE COLUMN `"+colName+"` `"+colName+"`"+colType+" NULL DEFAULT NULL ";
    					int colDtypeIndx=colDTypeStmt.executeUpdate(colDataTypeQry);
    					log.info("column datatype modified Qry executed with colDtypeIndx: "+colDtypeIndx);
    				}

    				updColDataQuery=" UPDATE `"+tableName+"` SET `"+colName+"` = ( "
    						+" SELECT `"+colName+"` FROM `"+physicalViewName+"` WHERE `"+tableName+"`.scrIds = `"+physicalViewName+"`.scrIds )";
    				int updColDataIndx=updateColDataStmt.executeUpdate(updColDataQuery);
    				log.info("updateColDataStmt /formula updated executed with updColDataIndx: "+updColDataIndx);
    			}

    			//FilterUpdated
    			if(keyList!=null && keyList.size()>0 && ( keyList.contains("FilterUpdated") || keyList.contains("dvConditionUpdated"))){
    				truncateQuery="truncate `"+tableName+"`";
    				int trnctIndx=truncateStmt.executeUpdate(truncateQuery);
    				log.info("truncateQuery execute with trnctIndx: "+trnctIndx);
    				dataLoadQuery="INSERT INTO `"+tableName+"` (SELECT * FROM `"+physicalViewName+"`)";
    				log.info("dataLoadQuery: "+dataLoadQuery);
    				int dataLoadIndx=dataLoadStmt.executeUpdate(dataLoadQuery);
    				log.info("dataLoadQuery executed with dataLoadIndx: "+dataLoadIndx);
    			}

    			if(createOrUpdateKey!=null && !(createOrUpdateKey.isEmpty()) && createOrUpdateKey.equalsIgnoreCase("Delete")){

    				List<Object[]> missingViewColList=dataViewsRepository.fetchMissingViewColumns(tableName, physicalViewName);

    				log.info("missingViewColList sz: "+missingViewColList.size());
    				for(int h=0;h<missingViewColList.size();h++){
    					colName=missingViewColList.get(h)[1].toString();
    					log.info("colName: "+colName);
    					deleteQuery="ALTER TABLE `"+tableName+"` "
    							+" DROP COLUMN `"+colName+"`";
    					int delCountIndx=delColStmt.executeUpdate(deleteQuery);
    					log.info("deleteQuery executed for colName: "+colName+" with delCountIndx: "+delCountIndx);
    				}

    			}
    			
    			if(createOrUpdateKey!=null && !(createOrUpdateKey.isEmpty()) && createOrUpdateKey.equalsIgnoreCase("groupByQualifier")){
    				String indexName=tableName+"_"+colName+"_Indx";
    				checkIndexNameExistsQuery="select * from sys.schema_index_statistics where table_schema='"+schemaName+"' and table_name='"+tableName+"' and index_name='"+indexName+"'";
    				log.info("checkIndexNameExistsQuery: "+checkIndexNameExistsQuery);
    				indxExistRs=checkIndexNameExistsStmt.executeQuery(checkIndexNameExistsQuery);
    				
    				String indxexistCnt=null;
    	    		while(indxExistRs.next()){
    	    			indxexistCnt=indxExistRs.getString(2);
    	    		}
    	    		log.info("index existence query executed with indxexisName: "+indxexistCnt);

    	    		if(indxexistCnt!=null){
    	    			log.info("indexName: "+indexName+" already exists for tableName: "+tableName);
    				
    				/*dropIndexQuery="drop index "+indexName+" on "+tableName;
    				int dropIndxCnt=dropIndexStmt.executeUpdate(dropIndexQuery);
    				log.info("Dropped Index for colName: "+colName+" with dropIndxCnt: "+dropIndxCnt);*/
    	    		}
    	    		else{
    	    			log.info("IN else grpByColVal: "+grpByColVal);
    	    			if(grpByColVal){
    	    				
    	    				colDataTypeQry=" ALTER TABLE `"+tableName+"` CHANGE COLUMN `"+colName+"` `"+colName+"`"+"VARCHAR(255) NULL DEFAULT NULL ";
        					int colDtypeIndx=colDTypeStmt.executeUpdate(colDataTypeQry);
        					log.info("column datatype modified Qry executed with colDtypeIndx: "+colDtypeIndx);
    	    				
    	    				createColIndxQuery="CREATE INDEX `"+indexName+"` ON `"+tableName+"`(`"+colName+"`);";
    	    				log.info("Create Index query: "+createColIndxQuery);
    	        			int index = createColIndxStmt.executeUpdate(createColIndxQuery);
    	        			log.info("Index creation stament executed with indx: "+index);
        				}
    	    			
    	    		}

    		}
    		
    		if(createOrUpdateKey!=null && !(createOrUpdateKey.isEmpty()) && createOrUpdateKey.equalsIgnoreCase("amountQualifier")){
    			
    			
    		}
    		}
    		else{
    			String createTableQuery="create table `"+tableName+"` as select * from `"+physicalViewName+"`";
    			log.info("table creation query: "+createTableQuery);
    			int createTabCnt=stmt3.executeUpdate(createTableQuery);
    			log.info("table creation query executed with createTabCnt: "+createTabCnt);

    			String indxName=tableName+"_scrIds_Indx";
    			String indxQuery="CREATE INDEX `"+indxName+"` ON `"+tableName+"`(scrIds);";
    			log.info("Create Index on srcIds: "+indxQuery);
    			int indx = indxStmt.executeUpdate(indxQuery);
    			log.info("Index creation stament executed with indx: "+indx);
    		}
    		/*
    		String getDvCountQry="select count(*) from `"+tableName+"`";
    		log.info("getDvCountQry: "+getDvCountQry);
    		getDvCountResult=getDvCountStmt.executeQuery(getDvCountQry);
    		int dvCount=0;
    		while(getDvCountResult.next()){
    			dvCount=getDvCountResult.getInt(1);
    			log.info("dvCount: "+dvCount);
    			
    			DataViews dataViewFinalRecord=dataViewsRepository.findOne(dv.getId());
    			dataViewFinalRecord.setRecordCount(dvCount);
    			dataViewsRepository.save(dataViewFinalRecord);
    			log.info("Updated data View count for dataview: "+tableName);
    		}*/
    		
    		String getDvCountQry="";
    		DataViewsColumns dvc=dataViewsColumnsRepository.findByDataViewIdAndQualifier(dv.getId(), "TRANSDATE");
    		String dateQualifier="";
    		if(dvc.getRefDvType()!=null && !(dvc.getRefDvType().isEmpty()) && dvc.getRefDvType().equalsIgnoreCase("File Template")){
    			FileTemplateLines ftl = fileTemplateLinesRepository.findOne(Long.parseLong(dvc.getRefDvColumn().toString()));
    			dateQualifier=ftl.getColumnAlias();
    		}else if(dvc.getRefDvType()==null || (dvc.getRefDvType()!=null && !(dvc.getRefDvType().isEmpty()) && dvc.getRefDvType().equalsIgnoreCase("Data View"))){
    			dateQualifier=dvc.getColumnName();
    		}
    		log.info("dateQualifier column: "+dateQualifier);
    		getDvCountQry= "select max(fileDate), max(`"+dateQualifier+"`), count(*) from `"+tableName+"`";
    		log.info("getDvCountQry: "+getDvCountQry);
    		getDvCountResult=getDvCountStmt.executeQuery(getDvCountQry);
    		int dvCount=0;
    		ZonedDateTime lastFileDate=null;
    		ZonedDateTime lastTransDate=null;
    		while(getDvCountResult.next()){
    			dvCount=getDvCountResult.getInt(3);
    			log.info("dvCount: "+dvCount);
    			if(dvCount>0){
    				
    				log.info("getDvCountResult.getString(1): "+getDvCountResult.getString(1));
    				log.info("getDvCountResult.getDate(1): "+getDvCountResult.getDate(1));
    				log.info("getDvCountResult.getTimestamp(1): "+getDvCountResult.getTimestamp(1).toInstant());
    				log.info("getDvCountResult.getTimestamp(2): "+getDvCountResult.getTimestamp(2));
    				/*lastFileDate=ZonedDateTime.parse(getDvCountResult.getString(1));
        			lastTransDate=ZonedDateTime.parse(getDvCountResult.getString(2));*/
    			}
    			log.info("lastFileDate: "+lastFileDate+" lastTransDate: "+lastTransDate );

    			DataViews dataViewFinalRecord=dataViewsRepository.findOne(dv.getId());
    			dataViewFinalRecord.setDataViewName(tableName);
    			dataViewFinalRecord.setRecordCount(dvCount);
    			if(getDvCountResult.getTimestamp(1)!=null){
    				log.info("getDvCountResult.getTimestamp(1): "+getDvCountResult.getTimestamp(1));
    				log.info("getDvCountResult.getTimestamp(1).toInstant(): "+getDvCountResult.getTimestamp(1).toInstant());
    			dataViewFinalRecord.setLastFileDate(ZonedDateTime.parse(getDvCountResult.getTimestamp(1).toInstant().toString()));
    			}
    			if(getDvCountResult.getTimestamp(2)!=null){
    				log.info("getDvCountResult.getTimestamp(2): "+getDvCountResult.getTimestamp(2));
    				log.info("getDvCountResult.getTimestamp(2).toInstant(): "+getDvCountResult.getTimestamp(2).toInstant());
    			dataViewFinalRecord.setLastTransDate(ZonedDateTime.parse(getDvCountResult.getTimestamp(2).toInstant().toString()));
    			}
    			dataViewsRepository.save(dataViewFinalRecord);
    			log.info("Updated data View count for dataview: "+tableName);
    		}

    	}catch(SQLException se){
    		errorReport.setTaskStatus("Failed to create view ");
    		errorReport.setDetails("Please correct your function");
    		log.info("se: "+se);
    	}
    	finally{
    		if(existrs!=null)
    			existrs.close();
    		if(getDvCountResult!=null)
    			getDvCountResult.close();
    		if(indxExistRs!=null)
    			indxExistRs.close();
    		if(stmt!=null)
    			stmt.close();
    		if(colDTypeStmt!=null)
    			colDTypeStmt.close();
    		if(delColStmt!=null)
    			delColStmt.close();
    		if(stmt2!=null)
    			stmt2.close();
    		if(stmt3!=null)
    			stmt3.close();
    		if(dataLoadStmt!=null)
    			dataLoadStmt.close();
    		if(indxStmt!=null)
    			indxStmt.close();
    		if(colRenameStmt!=null)
    			colRenameStmt.close();
    		if(insertColStmt!=null)
    			insertColStmt.close();
    		if(updateColDataStmt!=null)
    			updateColDataStmt.close();
    		if(safeUpdStmt!=null)
    			safeUpdStmt.close();
    		if(truncateStmt!=null)
    			truncateStmt.close();
    		if(getDvCountStmt!=null)
    			getDvCountStmt.close();
    		if(checkIndexNameExistsStmt!=null)
    			checkIndexNameExistsStmt.close();
    		if(createColIndxStmt!=null)
    			createColIndxStmt.close();
    		if(conn!=null)
    			conn.close();
    	}
    	return errorReport;
    }
    
    /**
     * Author: Swetha
     * Api to validate data views column name with Mysql buzzwords
     * @param columnName
     * @return
     */
    @GetMapping("/checkColumnName")
    @Timed
    public ErrorReport checkColumnName(@RequestParam String columnName)
    {
    	log.info("Rest request to checkColumnName: "+columnName);
    	
    	ErrorReport errorReport=new ErrorReport();
    	errorReport.setTaskName("Column Name Validation");
    	
    	List<Object[]> functList=dataViewsRepository.fetchStringFunctions();
		List<String> funList=new ArrayList<>();
		for(int i=0;i<functList.size();i++){
			Object[] function=functList.get(i);
			funList.add(function[1].toString());
		}
		
		log.info("funList: "+funList);
		
		if(funList.contains(columnName.toUpperCase())){
			log.info("columnName should not be standard Mysql Function ");
			errorReport.setTaskStatus("Failed to accept provided column name ");
    		errorReport.setDetails("columnName should not be standard Mysql Function");
		}
		else{
			errorReport.setTaskStatus("Accepted provided column name ");
    		errorReport.setDetails(null);
		}
		
		return errorReport;
    	
    }
    
}