package com.nspl.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.domain.TenantConfig;
import com.nspl.app.repository.TenantConfigRepository;
import com.nspl.app.security.IDORUtils;
import com.nspl.app.service.UserJdbcService;
import com.nspl.app.web.rest.util.HeaderUtil;

import io.github.jhipster.web.util.ResponseUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

/**
 * REST controller for managing TenantConfig.
 */
@RestController
@RequestMapping("/api")
public class TenantConfigResource {

	private final Logger log = LoggerFactory.getLogger(TenantConfigResource.class);

	private static final String ENTITY_NAME = "tenantConfig";

	private final TenantConfigRepository tenantConfigRepository;

	//    private final 
	@Inject
	UserJdbcService userJdbcService;
	
	@PersistenceContext(unitName="default")
	private EntityManager em;

	public TenantConfigResource(TenantConfigRepository tenantConfigRepository){//, UserJdbcService userJdbcService) {
		this.tenantConfigRepository = tenantConfigRepository;
		//        this.userJdbcService = userJdbcService;
	}

	/**
	 * POST  /tenant-configs : Create a new tenantConfig.
	 *
	 * @param tenantConfig the tenantConfig to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new tenantConfig, or with status 400 (Bad Request) if the tenantConfig has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/tenant-configs")
	@Timed
	public ResponseEntity<TenantConfig> createTenantConfig(@RequestBody TenantConfig tenantConfig) throws URISyntaxException {
		log.debug("REST request to save TenantConfig : {}", tenantConfig);
		if (tenantConfig.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new tenantConfig cannot already have an ID")).body(null);
		}
		TenantConfig result = tenantConfigRepository.save(tenantConfig);
		return ResponseEntity.created(new URI("/api/tenant-configs/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /tenant-configs : Updates an existing tenantConfig.
	 *
	 * @param tenantConfig the tenantConfig to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated tenantConfig,
	 * or with status 400 (Bad Request) if the tenantConfig is not valid,
	 * or with status 500 (Internal Server Error) if the tenantConfig couldn't be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/tenant-configs")
	@Timed
	public ResponseEntity<TenantConfig> updateTenantConfig(@RequestBody TenantConfig tenantConfig) throws URISyntaxException {
		log.debug("REST request to update TenantConfig : {}", tenantConfig);
		if (tenantConfig.getId() == null) {
			return createTenantConfig(tenantConfig);
		}
		TenantConfig result = tenantConfigRepository.save(tenantConfig);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tenantConfig.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /tenant-configs : get all the tenantConfigs.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of tenantConfigs in body
	 */
	@GetMapping("/tenant-configs")
	@Timed
	public List<TenantConfig> getAllTenantConfigs() {
		log.debug("REST request to get all TenantConfigs");
		return tenantConfigRepository.findAll();
	}

	/**
	 * GET  /tenant-configs/:id : get the "id" tenantConfig.
	 *
	 * @param id the id of the tenantConfig to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the tenantConfig, or with status 404 (Not Found)
	 */
	@GetMapping("/tenant-configs/{id}")
	@Timed
	public ResponseEntity<TenantConfig> getTenantConfig(@PathVariable Long id) {
		log.debug("REST request to get TenantConfig : {}", id);
		TenantConfig tenantConfig = tenantConfigRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(tenantConfig));
	}

	/**
	 * DELETE  /tenant-configs/:id : delete the "id" tenantConfig.
	 *
	 * @param id the id of the tenantConfig to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/tenant-configs/{id}")
	@Timed
	public ResponseEntity<Void> deleteTenantConfig(@PathVariable Long id) {
		log.debug("REST request to delete TenantConfig : {}", id);
		tenantConfigRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * author Kiran
	 * @return
	 */
	@GetMapping("/tenantConfigsByTenantId")
	@Timed
	public List<TenantConfig> getAllTenantConfigsByTenantId(HttpServletRequest request, @RequestParam String type) {
		log.debug("REST request to get all TenantConfigs");
		Long tenantId=0l;
		if(type!=null && type.equals("create"))
		{
			return tenantConfigRepository.findByTenantId(tenantId);
		}
		else if(type!=null && type.equals("view"))
		{
			HashMap map=userJdbcService.getuserInfoFromToken(request);
			tenantId=Long.parseLong(map.get("tenantId").toString());
			return tenantConfigRepository.findByTenantId(tenantId);
		}
		// HashMap map =userJdbcService.getuserInfoFromToken(request);
		return null;
	}


	/**
	 * author Kiran
	 * @param request
	 * @param tenantConfigList
	 */
	@PostMapping("/postTenantConfigs")
	@Timed
	public void postTenantConfigs(HttpServletRequest request,@RequestBody List<TenantConfig> tenantConfigList) {
		log.debug("REST request to get all TenantConfigs");

		List<TenantConfig> listOfConfigs= new ArrayList<TenantConfig>();
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());

		Long userId=Long.parseLong(map.get("userId").toString());

		log.info("tenantId"+tenantId);
		for(TenantConfig tenantConfig: tenantConfigList)
		{
			TenantConfig saveConfigs = new TenantConfig();
			if(tenantConfig.getKey()!=null){
				saveConfigs.setKey(tenantConfig.getKey());
			}
			if(tenantConfig.getMeaning()!=null){
				saveConfigs.setMeaning(tenantConfig.getMeaning());
			}
			if(tenantConfig.getValue()!=null){
				saveConfigs.setValue(tenantConfig.getValue());
			}
			if(tenantId!=null){
				saveConfigs.setTenantId(tenantId);
			}
			if(userId!=null){
				saveConfigs.setCreatedBy(userId);
			}
			saveConfigs.setCreationDate(ZonedDateTime.now());
			listOfConfigs.add(saveConfigs);
		}
		tenantConfigRepository.save(listOfConfigs);
		//  return tenantConfigRepository.findByTenantId();
	}

	/**
	 * Author Kiran
	 * @param request
	 */
	@PostMapping("/postConfigsBasedOnZeroTenant")
	@Timed
	public void postConfigsBasedOnZeroTenantId(HttpServletRequest request, @RequestParam(required=false) Long tenantId, @RequestParam(required=false) Long userId)
	{

		HashMap map=new HashMap();
		if(tenantId==null || userId==null)
		{
			map=userJdbcService.getuserInfoFromToken(request);
			tenantId=Long.parseLong(map.get("tenantId").toString());
			userId=Long.parseLong(map.get("userId").toString());
		}

		log.info("Rest request to update/post detaisl for tenant :"+tenantId);

		List<TenantConfig> tenantConfigList=tenantConfigRepository.findByTenantId(0l);
		List<TenantConfig> listOfConfigs= new ArrayList<TenantConfig>();

		if(tenantConfigList!=null && tenantConfigList.size()>0)
		{
			for(TenantConfig tenantConfig: tenantConfigList)
			{
				TenantConfig saveConfigs = new TenantConfig();
				if(tenantConfig.getKey()!=null){
					saveConfigs.setKey(tenantConfig.getKey());
				}
				if(tenantConfig.getMeaning()!=null){
					saveConfigs.setMeaning(tenantConfig.getMeaning());
				}
				if(tenantConfig.getValue()!=null){
					saveConfigs.setValue(tenantConfig.getValue());
				}
				if(tenantId!=null){
					saveConfigs.setTenantId(tenantId);
				}
				if(userId!=null){
					saveConfigs.setCreatedBy(userId);
				}
				saveConfigs.setCreationDate(ZonedDateTime.now());
				listOfConfigs.add(saveConfigs);
			}
			tenantConfigRepository.save(listOfConfigs);
		}
	}
	
	/** dynamic API to update encrypted Id column **/
	@Transactional
	@GetMapping("/UpdateEncryptedId")
	@Timed
	public List<HashMap> UpdateEncryptedId(@RequestParam (required=false) List<Long> idList, @RequestParam String tableName)  {
		log.debug("REST request to get all TenantConfigs");
		List<HashMap> finalUpdatedMapList=new ArrayList<HashMap>();
		log.info("idList :"+idList);
		List<String> booleanList=new ArrayList<String>();
		List<Long> idList1 = new ArrayList<Long>();
		if(idList == null || idList.size() == 0)
		{
			Query distinctList=em.createQuery("select id FROM "+tableName+" where idForDisplay is null");

			log.info("distinctList : "+distinctList);
			List distinct = new ArrayList<String>();
			distinct =distinctList.getResultList();
					
					for(int i=0;i<distinct.size();i++)
					{
						String id = distinct.get(i).toString();
						idList1.add(Long.valueOf(id));
					}
					idList =idList1;
		}
		for(Long id:idList)
		{
			String idForDisplay = IDORUtils.computeFrontEndIdentifier(id.toString());
			//	log.info("idForDisplay :"+idForDisplay);
			/*String updateQuery="UPDATE "+tableName+" t set t.idForDisplay='"+idForDisplay+"' where t.id="+id;
			log.info("updateQuery :"+updateQuery);
			Query exe=em.createQuery(updateQuery);*/
			HashMap updateMap=new HashMap();

			String updateQuery="UPDATE "+tableName+" t set t.idForDisplay=:idForDisplay where t.id=:id";
			//	log.info("updateQuery :"+updateQuery);
			Query exe=em.createQuery(updateQuery);
			exe.setParameter("idForDisplay", idForDisplay);
			exe.setParameter("id", id);
			em.flush();
			//     log.info("resultSet :"+exe.executeUpdate()); 
			if(exe.executeUpdate()!=0)
				booleanList.add("true");
			else
			{
				updateMap.put("id", id);
				updateMap.put("status","failed to update record");
				finalUpdatedMapList.add(updateMap);

			}
		}
		return finalUpdatedMapList;

	}

}
