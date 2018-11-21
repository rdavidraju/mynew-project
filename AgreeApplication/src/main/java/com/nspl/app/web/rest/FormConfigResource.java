package com.nspl.app.web.rest;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
import com.nspl.app.domain.DataViewsColumns;
import com.nspl.app.domain.FormConfig;
import com.nspl.app.repository.DataViewsColumnsRepository;
import com.nspl.app.repository.FormConfigRepository;
import com.nspl.app.service.FormConfigService;
import com.nspl.app.service.ReconciliationResultService;
import com.nspl.app.service.UserJdbcService;
import com.nspl.app.web.rest.dto.RWQFormConfigDTO;
import com.nspl.app.web.rest.util.HeaderUtil;
import com.nspl.app.web.rest.util.PaginationUtil;

/**
 * REST controller for managing FormConfig.
 */
@RestController
@RequestMapping("/api")
public class FormConfigResource {

	private final Logger log = LoggerFactory.getLogger(FormConfigResource.class);

	private static final String ENTITY_NAME = "formConfig";

	private final FormConfigRepository formConfigRepository;

	@Inject
	ReconciliationResultService reconciliationResultService;

	@Inject
	DataViewsColumnsRepository dataViewsColumnsRepository;

	@Inject
	FormConfigService formConfigService;

	@Inject
	UserJdbcService userJdbcService;

	public FormConfigResource(FormConfigRepository formConfigRepository) {
		this.formConfigRepository = formConfigRepository;
	}



	/**
	 * POST  /form-configs : Create a new formConfig.
	 *
	 * @param formConfig the formConfig to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new formConfig, or with status 400 (Bad Request) if the formConfig has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/form-configs")
	@Timed
	public ResponseEntity<FormConfig> createFormConfig(@RequestBody FormConfig formConfig) throws URISyntaxException {
		log.debug("REST request to save FormConfig : {}", formConfig);
		if (formConfig.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new formConfig cannot already have an ID")).body(null);
		}
		FormConfig result = formConfigRepository.save(formConfig);
		return ResponseEntity.created(new URI("/api/form-configs/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /form-configs : Updates an existing formConfig.
	 *
	 * @param formConfig the formConfig to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated formConfig,
	 * or with status 400 (Bad Request) if the formConfig is not valid,
	 * or with status 500 (Internal Server Error) if the formConfig couldn't be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/form-configs")
	@Timed
	public ResponseEntity<FormConfig> updateFormConfig(@RequestBody FormConfig formConfig) throws URISyntaxException {
		log.debug("REST request to update FormConfig : {}", formConfig);
		if (formConfig.getId() == null) {
			return createFormConfig(formConfig);
		}
		FormConfig result = formConfigRepository.save(formConfig);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, formConfig.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /form-configs : get all the formConfigs.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of formConfigs in body
	 */
	@GetMapping("/form-configs")
	@Timed
	public ResponseEntity<List<FormConfig>> getAllFormConfigs(@ApiParam Pageable pageable) {
		log.debug("REST request to get a page of FormConfigs");
		Page<FormConfig> page = formConfigRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/form-configs");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET  /form-configs/:id : get the "id" formConfig.
	 *
	 * @param id the id of the formConfig to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the formConfig, or with status 404 (Not Found)
	 */
	@GetMapping("/form-configs/{id}")
	@Timed
	public ResponseEntity<FormConfig> getFormConfig(@PathVariable Long id) {
		log.debug("REST request to get FormConfig : {}", id);
		FormConfig formConfig = formConfigRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(formConfig));
	}

	/**
	 * DELETE  /form-configs/:id : delete the "id" formConfig.
	 *
	 * @param id the id of the formConfig to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/form-configs/{id}")
	@Timed
	public ResponseEntity<Void> deleteFormConfig(@PathVariable Long id) {
		log.debug("REST request to delete FormConfig : {}", id);
		formConfigRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * Author: Shiva
	 * Input Params: tenantId, userId, formConfig, formLevel, valueDTO
	 * @throws ClassNotFoundException 
	 **/
	@PostMapping("/postFormConfigParams")
	@Timed
	public FormConfig postFormConfigParams(HttpServletRequest request, @RequestParam String formConf, @RequestParam String formLevel, @RequestBody List<RWQFormConfigDTO> formConfig) throws URISyntaxException, JsonGenerationException, JsonMappingException, IOException, ClassNotFoundException {
		log.info("Rest api for posting form config parameter");
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		Long userId =Long.parseLong(map.get("userId").toString());
		String finalString = "";
		ObjectMapper mapperObj = new ObjectMapper();
		if(formConfig.size()>0)
		{
			for(RWQFormConfigDTO conf : formConfig)
			{
				if("AMOUNT".equalsIgnoreCase(conf.getParameter()))
				{
					/*String sAmountQualifier = reconciliationResultService.getViewColumnQualifier(BigInteger.valueOf(sViewId), "AMOUNT");
    				String tAmountQualifier = reconciliationResultService.getViewColumnQualifier(BigInteger.valueOf(tViewId), "AMOUNT");
    				if(sAmountQualifier != null && tAmountQualifier != null)
    				{*/
					log.info("In Amount");
					String jsonStr = mapperObj.writeValueAsString(conf);
					finalString = finalString + jsonStr+", ";
					/*}*/
				}
				else if("TRANSDATE".equalsIgnoreCase(conf.getParameter()))
				{
					/*String sDateQualifier = reconciliationResultService.getViewColumnQualifier(BigInteger.valueOf(sViewId), "TRANSDATE");
    				String tDateQualifier = reconciliationResultService.getViewColumnQualifier(BigInteger.valueOf(tViewId), "TRANSDATE");
    				if(sDateQualifier != null && tDateQualifier != null)
    				{*/
					log.info("In Date");
					String jsonStr = mapperObj.writeValueAsString(conf);
					finalString = finalString + jsonStr+", ";
					/*}*/
				}
				else if("CURRENCYCODE".equalsIgnoreCase(conf.getParameter()))
				{
					/*String sCurrencyQualifier = reconciliationResultService.getViewColumnQualifier(BigInteger.valueOf(sViewId), "CURRENCYCODE");
    				String tCurrencyQualifier = reconciliationResultService.getViewColumnQualifier(BigInteger.valueOf(tViewId), "CURRENCYCODE");
    				if(sCurrencyQualifier != null && tCurrencyQualifier != null)
    				{*/
					log.info("In currency code");
					String jsonStr = mapperObj.writeValueAsString(conf);
					finalString = finalString + jsonStr+", ";
					/*}*/
				}
				else if("KEY COMPONENT".equalsIgnoreCase(conf.getParameter()))
				{
					log.info("In key component");
					String jsonStr = mapperObj.writeValueAsString(conf);
					finalString = finalString + jsonStr+", ";
				}
			}
			if(finalString.length()>0)
			{
				finalString = finalString.substring(0, finalString.length()-2);
			}
		}
		finalString = "["+finalString+"]";
		log.info("Final String: "+ finalString);
		FormConfig newFormConfig = new FormConfig();
		newFormConfig.setTenantId(tenantId);
		newFormConfig.setFormConfig(formConf);
		newFormConfig.setFormLevel(formLevel);
		newFormConfig.setValue(finalString);
		newFormConfig.setCreatedBy(userId);
		newFormConfig.setLastUpdatedBy(userId);
		newFormConfig.setCreatedDate(ZonedDateTime.now());
		newFormConfig.setLastUpdatedDate(ZonedDateTime.now());
		FormConfig fc = formConfigRepository.findByTenantIdAndFormConfigAndFormLevel(tenantId, "reconcilewq", "customfilters");
		FormConfig createFormConfig = null;
		if(fc != null)
		{
			newFormConfig.setId(fc.getId());
			createFormConfig = formConfigRepository.save(newFormConfig);
		}
		else
		{
			createFormConfig = formConfigRepository.save(newFormConfig);
		}
		return createFormConfig;
	}

	/**
	 * Author: Shiva
	 * Input Params: tenantId, userId, formConfig, formLevel, valueDTO
	 * @throws ParseException 
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonGenerationException 
	 * @throws ClassNotFoundException 
	 **/
	@GetMapping("/getFormConfigParams")
	@Timed
	public HashMap getFormConfigParams( HttpServletRequest request,@RequestParam Long sViewId,@RequestParam Long tViewId) throws ParseException, JsonGenerationException, JsonMappingException, IOException, ClassNotFoundException{
		log.info("Rest api for fetching form config parameters");
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		Long userId =Long.parseLong(map.get("userId").toString());
		HashMap finalMap = new HashMap();
		FormConfig formConfig = formConfigRepository.findByTenantId(tenantId);    	

		if(formConfig == null)
		{
			FormConfig saveFormConfig = formConfigService.postDefaultFormConf(tenantId, userId, sViewId);
			JSONParser parser = new JSONParser();
			JSONArray jsonValue = (JSONArray) parser.parse(saveFormConfig.getValue());
			JSONArray finalValue = formConfigService.getFormConfigValue(sViewId, tViewId, jsonValue);

			finalMap.put("id", saveFormConfig.getId());
			finalMap.put("tenantId", saveFormConfig.getTenantId());
			finalMap.put("formConfig", saveFormConfig.getFormConfig());
			finalMap.put("formLevel", saveFormConfig.getFormLevel());
			finalMap.put("filterValues", finalValue);

		}
		else
		{
			log.info("Fetching Form Config Params for the tenant id: "+ tenantId);
			JSONParser parser = new JSONParser();
			JSONArray jsonValue = (JSONArray) parser.parse(formConfig.getValue());
			JSONArray finalValue = formConfigService.getFormConfigValue(sViewId, tViewId, jsonValue);

			finalMap.put("id", formConfig.getId());
			finalMap.put("tenantId", formConfig.getTenantId());
			finalMap.put("formConfig", formConfig.getFormConfig());
			finalMap.put("formLevel", formConfig.getFormLevel());
			finalMap.put("filterValues", finalValue);		
		}
		return finalMap;
	}

	/**
	 * Author Kiran
	 * @param request
	 * @param formConfig
	 * @param formLevel
	 * @param type
	 * @return
	 */
	@GetMapping("/getFormConfigDetails")//need to modify string
	@Timed
	public FormConfig getFormConfigBasedOnType(HttpServletRequest request,@RequestParam String formConfig, @RequestParam String formLevel, @RequestParam String type)
	{
		log.info("Rest request to get the details based on the type: "+type);

		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());

		log.info("tenantId: "+tenantId);

		FormConfig formConfigDetails = new FormConfig();
		if(type!=null)
		{
			if(type.equalsIgnoreCase("user"))
			{
				Long userId=Long.parseLong(map.get("userId").toString());
				log.info("UserId: "+userId);
				formConfigDetails=formConfigRepository.findByUserIdAndFormConfigAndFormLevel(userId,formConfig,formLevel);
			}
			else if(type.equalsIgnoreCase("tenant"))
			{
				formConfigDetails=formConfigRepository.findByTenantIdAndFormConfigAndFormLevel(tenantId,formConfig,formLevel);
				if(formConfigDetails==null)
				{
					formConfigDetails=formConfigRepository.findByTenantIdAndFormConfigAndFormLevel(0L,formConfig,formLevel);
				}
			}
		}
		return formConfigDetails;
	}

	/**
	 * Author Kiran
	 * @param request
	 * @param formConfigDetails
	 * @param formConfig
	 * @param formLevel
	 * @param type
	 */
	@PostMapping("/postFormConfigDetails")
	@Timed
	public HashMap<String, String> postFormConfigDetails(@RequestBody FormConfig formConfigDetails,HttpServletRequest request,@RequestParam String formConfig, @RequestParam String formLevel, @RequestParam String type)
	{
		log.info("Rest request to post the FormConfig details based on the type: "+type);
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		Long userId=Long.parseLong(map.get("userId").toString());
		log.info("tenantId: "+tenantId+" userId: "+userId);
		
		
		HashMap<String, String> result = new HashMap();
		result.put("status", "fail");

//		String result="fail";
		FormConfig formDetails = new FormConfig();
		FormConfig formConfigDetails1=null;
		if(type!=null)
		{
			if(type.equalsIgnoreCase("user"))
			{
				formConfigDetails1=formConfigRepository.findByUserIdAndTenantIdAndFormConfigAndFormLevel(userId,tenantId,formConfig,formLevel);
				if(formConfigDetails1!=null)
				{
					if(formConfigDetails1.getValue()!=null && !formConfigDetails1.getValue().isEmpty()){
						formConfigDetails1.setValue(formConfigDetails.getValue());}
					else{
						formConfigDetails1.setValue(formConfigDetails.getValue());
					}
					if(formConfigDetails.getFormConfig()!=null && !formConfigDetails.getFormConfig().isEmpty()){
						formConfigDetails1.setFormConfig(formConfigDetails.getFormConfig());}
					else{
						formConfigDetails1.setFormConfig(formConfig);}
					if(formConfigDetails.getFormLevel()!=null && !formConfigDetails.getFormLevel().isEmpty()){
						formConfigDetails1.setFormConfig(formConfigDetails.getFormLevel());}
					else{
						formConfigDetails1.setFormLevel(formLevel);}
					formConfigDetails1.setLastUpdatedBy(userId);
					formConfigDetails1.setLastUpdatedDate(ZonedDateTime.now());
				}
				else
				{
					//log.info("formConfigDetails: "+formConfigDetails.toString());
					formDetails.setValue(null);
					if(formConfigDetails.getValue()!=null)
						formDetails.setValue(formConfigDetails.getValue());
					formDetails.setFormConfig(formConfig);
					formDetails.setFormLevel(formLevel);
					//log.info("tenantId1: "+tenantId+" userId1: "+userId);
					formDetails.setUserId(userId);
					formDetails.setCreatedBy(userId);
					formDetails.setCreatedDate(ZonedDateTime.now());
					formDetails.setTenantId(tenantId);
				}
			}
			else if(type.equalsIgnoreCase("tenant"))
			{
				formConfigDetails1=formConfigRepository.findByTenantIdAndFormConfigAndFormLevel(tenantId,formConfig,formLevel);
				if(formConfigDetails1!=null)
				{
					if(formConfigDetails1.getValue()!=null && !formConfigDetails1.getValue().isEmpty()){
						formConfigDetails1.setValue(formConfigDetails.getValue());}
					else{
						formConfigDetails1.setValue(formConfigDetails.getValue());
					}
					if(formConfigDetails.getFormConfig()!=null && !formConfigDetails.getFormConfig().isEmpty()){
						formConfigDetails1.setFormConfig(formConfigDetails.getFormConfig());}
					else{
						formConfigDetails1.setFormConfig(formConfig);}
					if(formConfigDetails.getFormLevel()!=null && !formConfigDetails.getFormLevel().isEmpty()){
						formConfigDetails1.setFormConfig(formConfigDetails.getFormLevel());}
					else{
						formConfigDetails1.setFormLevel(formLevel);}
					formConfigDetails1.setLastUpdatedDate(ZonedDateTime.now());
					formConfigDetails1.setLastUpdatedBy(userId);
				}
				else{
					formDetails.setValue(null);
					if(formConfigDetails.getValue()!=null)
						formDetails.setValue(formConfigDetails.getValue());
					formDetails.setFormConfig(formConfig);
					formDetails.setFormLevel(formLevel);
					//log.info("tenantId2: "+tenantId+" userId2: "+userId);
					formDetails.setUserId(userId);
					formDetails.setTenantId(tenantId);
					formDetails.setCreatedBy(userId);
					formDetails.setCreatedDate(ZonedDateTime.now());
				}
			}
			if(formConfigDetails1!=null){
				formConfigRepository.save(formConfigDetails1);
				System.out.println("Saving....");
				result.put("status", "success");}
			else{
				formConfigRepository.save(formDetails);
				System.out.println("Saving.");
				result.put("status", "success");}
		}
		return result;
	}





}
