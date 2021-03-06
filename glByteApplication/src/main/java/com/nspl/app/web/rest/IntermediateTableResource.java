package com.nspl.app.web.rest;

import com.codahale.metrics.annotation.Timed;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.core.JsonParser;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.databind.type.TypeFactory;

import com.nspl.app.domain.FileTemplates;
import com.nspl.app.domain.IntermediateTable;
import com.nspl.app.domain.LookUpCode;
import com.nspl.app.repository.DataMasterRepository;
import com.nspl.app.repository.DataStagingRepository;
import com.nspl.app.repository.FileTemplatesRepository;
import com.nspl.app.repository.IntermediateTableRepository;
import com.nspl.app.repository.LookUpCodeRepository;
import com.nspl.app.service.UserJdbcService;
import com.nspl.app.web.rest.dto.IntermediateRecordsDTO;
import com.nspl.app.web.rest.util.HeaderUtil;

import io.github.jhipster.web.util.ResponseUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.json.simple.parser.ParseException;

/**
 * REST controller for managing IntermediateTable.
 */
@RestController
@RequestMapping("/api")
public class IntermediateTableResource {

    private final Logger log = LoggerFactory.getLogger(IntermediateTableResource.class);

    private static final String ENTITY_NAME = "intermediateTable";

    private final IntermediateTableRepository intermediateTableRepository;
    
    private final LookUpCodeRepository lookUpCodeRepository;
    
    private final FileTemplatesRepository fileTemplatesRepository;
    
    private final DataStagingRepository dataStagingRepository;
    
    private final DataMasterRepository dataMasterRepository;
    
	@Inject
	UserJdbcService userJdbcService;

    public IntermediateTableResource(IntermediateTableRepository intermediateTableRepository,LookUpCodeRepository lookUpCodeRepository,FileTemplatesRepository fileTemplatesRepository
    		,DataStagingRepository dataStagingRepository,DataMasterRepository dataMasterRepository) {
        this.intermediateTableRepository = intermediateTableRepository;
        this.lookUpCodeRepository=lookUpCodeRepository;
        this.fileTemplatesRepository = fileTemplatesRepository;
        this.dataStagingRepository = dataStagingRepository;
        this.dataMasterRepository =dataMasterRepository;
    }

    /**
     * POST  /intermediate-tables : Create a new intermediateTable.
     *
     * @param intermediateTable the intermediateTable to create
     * @return the ResponseEntity with status 201 (Created) and with body the new intermediateTable, or with status 400 (Bad Request) if the intermediateTable has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/intermediate-tables")
    @Timed
    public ResponseEntity<IntermediateTable> createIntermediateTable(@RequestBody IntermediateTable intermediateTable) throws URISyntaxException {
        log.debug("REST request to save IntermediateTable : {}", intermediateTable);
        if (intermediateTable.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new intermediateTable cannot already have an ID")).body(null);
        }
        IntermediateTable result = intermediateTableRepository.save(intermediateTable);
        return ResponseEntity.created(new URI("/api/intermediate-tables/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /intermediate-tables : Updates an existing intermediateTable.
     *
     * @param intermediateTable the intermediateTable to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated intermediateTable,
     * or with status 400 (Bad Request) if the intermediateTable is not valid,
     * or with status 500 (Internal Server Error) if the intermediateTable couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/intermediate-tables")
    @Timed
    public ResponseEntity<IntermediateTable> updateIntermediateTable(@RequestBody IntermediateTable intermediateTable) throws URISyntaxException {
        log.debug("REST request to update IntermediateTable : {}", intermediateTable);
        if (intermediateTable.getId() == null) {
            return createIntermediateTable(intermediateTable);
        }
        IntermediateTable result = intermediateTableRepository.save(intermediateTable);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, intermediateTable.getId().toString()))
            .body(result);
    }

    /**
     * GET  /intermediate-tables : get all the intermediateTables.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of intermediateTables in body
     */
    @GetMapping("/intermediate-tables")
    @Timed
    public List<IntermediateTable> getAllIntermediateTables() {
        log.debug("REST request to get all IntermediateTables");
        return intermediateTableRepository.findAll();
    }

    /**
     * GET  /intermediate-tables/:id : get the "id" intermediateTable.
     *
     * @param id the id of the intermediateTable to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the intermediateTable, or with status 404 (Not Found)
     */
    @GetMapping("/intermediate-tables/{id}")
    @Timed
    public ResponseEntity<IntermediateTable> getIntermediateTable(@PathVariable Long id) {
        log.debug("REST request to get IntermediateTable : {}", id);
        IntermediateTable intermediateTable = intermediateTableRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(intermediateTable));
    }

    /**
     * DELETE  /intermediate-tables/:id : delete the "id" intermediateTable.
     *
     * @param id the id of the intermediateTable to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/intermediate-tables/{id}")
    @Timed
    public ResponseEntity<Void> deleteIntermediateTable(@PathVariable Long id) {
        log.debug("REST request to delete IntermediateTable : {}", id);
        intermediateTableRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    
    /**
     * Author : Shobha
     * @param templateId
     * @return
     */
    @GetMapping("/fetchIntermediateTablesByTemplateId")
    @Timed
    public List<IntermediateRecordsDTO> fetchIntermediateTablesByTemplateId(HttpServletRequest request, @RequestParam String templateId,
    		@RequestParam (required = false) String referenceTable,@RequestParam (required = false) Long srcFileHistInbId) {
    	
		HashMap map=userJdbcService.getuserInfoFromToken(request);
		Long tenantId=Long.parseLong(map.get("tenantId").toString());
		
        log.debug("REST request to get IntermediateTable : ", templateId);
        FileTemplates ft = new FileTemplates();
        ft = fileTemplatesRepository.findByIdForDisplayAndTenantId(templateId,tenantId);
        List<IntermediateRecordsDTO> interRecords = new ArrayList<IntermediateRecordsDTO>();
        if(ft != null && ft.getMultipleIdentifier() != null && ft.getMultipleIdentifier() == true)
        {
        	
            List<IntermediateTable> intermediateTableRecords = intermediateTableRepository.findByTemplateId(ft.getId());
            for(IntermediateTable  intermediateTableRecord : intermediateTableRecords)
            {
            	IntermediateRecordsDTO interRecord = new IntermediateRecordsDTO();
            	interRecord.setCreatedBy(intermediateTableRecord.getCreatedBy());
            	interRecord.setCreationDate(intermediateTableRecord.getCreationDate());
            	if(intermediateTableRecord.getData() != null)
            	interRecord.setData(intermediateTableRecord.getData());
            	interRecord.setHeaderInfo(intermediateTableRecord.getHeaderInfo());
            	interRecord.setId(intermediateTableRecord.getId());
            	interRecord.setLastUpdatedBy(intermediateTableRecord.getLastUpdatedBy());
            	interRecord.setLastUpdatedDate(intermediateTableRecord.getLastUpdatedDate());
            	interRecord.setPositionEnd(intermediateTableRecord.getPositionEnd());
            	interRecord.setPositionStart(intermediateTableRecord.getPositionStart());
            	if(referenceTable == null)
            	{
            		interRecord.setRowIdentifier(intermediateTableRecord.getRowIdentifier());
            	}
            	else
            	{
            		if(referenceTable != null && !referenceTable.isEmpty() )
                	{
                		log.info("referenceTable"+referenceTable);
                		if(referenceTable.toLowerCase()  .equalsIgnoreCase( "staging"))
                		{
                			int countByRecIdentifier = dataStagingRepository.findBySrcFileInbIdAndIntermediateId(srcFileHistInbId,intermediateTableRecord.getId()).size();
                			log.info("countByRecIdentifier"+countByRecIdentifier);
                			if(countByRecIdentifier == 0)
                				interRecord.setDisplay(false);
                			interRecord.setRowIdentifier(intermediateTableRecord.getRowIdentifier() + "("+countByRecIdentifier+")");
                			log.info("rec id"+interRecord.getRowIdentifier());
                		}
                		else if(referenceTable.toLowerCase() .equalsIgnoreCase( "master"))
                		{
                			int countByRecIdentifier = dataMasterRepository.findBySrcFileInbIdAndIntermediateId(srcFileHistInbId,intermediateTableRecord.getId()).size();
                			log.info("countByRecIdentifier"+countByRecIdentifier);
                			if(countByRecIdentifier == 0)
                				interRecord.setDisplay(false);
                			interRecord.setRowIdentifier(intermediateTableRecord.getRowIdentifier() + "("+countByRecIdentifier+")");
                			log.info("rec id"+interRecord.getRowIdentifier());
                		}
                	}
            	}
            	LookUpCode lookUpCode = new LookUpCode();
            	if(intermediateTableRecord.getRowIdentifierCriteria() != null)
            	{
            		interRecord.setCriteria(intermediateTableRecord.getRowIdentifierCriteria());
            		lookUpCode = lookUpCodeRepository.findByLookUpTypeAndLookUpCodeAndTenantId("ROW_IDENTIFIER", intermediateTableRecord.getRowIdentifierCriteria(), intermediateTableRecord.getTenantId());
                	if(lookUpCode != null && lookUpCode.getMeaning() != null)
                	interRecord.setCriteriaMeaning(lookUpCode.getMeaning());
            	}
            	
            	interRecord.setRowInfo(intermediateTableRecord.getRowInfo());
            	
            	interRecord.setTemplateId(intermediateTableRecord.getTemplateId());
            	interRecord.setTenantId(intermediateTableRecord.getTenantId());
            	JSONParser parser = new JSONParser();
            	JSONArray newJObject = null;
            	try {
            		if(intermediateTableRecord.getData() != null)
            		{
            			newJObject = (JSONArray) parser.parse(intermediateTableRecord.getData());
            			interRecord.setSampleData(newJObject);
            		}
        			
        		} catch (ParseException e) {
        			e.printStackTrace();
        		}
            	interRecords.add(interRecord);

            }
        }
        
        return interRecords;
    }
    
}
