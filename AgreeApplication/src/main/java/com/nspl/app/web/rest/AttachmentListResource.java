package com.nspl.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.domain.AttachmentFiles;
import com.nspl.app.domain.AttachmentList;
import com.nspl.app.repository.AttachmentFilesRepository;
import com.nspl.app.repository.AttachmentListRepository;
import com.nspl.app.service.UserJdbcService;
import com.nspl.app.web.rest.dto.AttachmentFileDto;
import com.nspl.app.web.rest.util.HeaderUtil;
import com.nspl.app.web.rest.util.PaginationUtil;

import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * REST controller for managing AttachmentList.
 */
@RestController
@RequestMapping("/api")
public class AttachmentListResource {
	
	
	  @Inject
    UserJdbcService userJdbcService;
	  
	  @Inject
	  AttachmentFilesRepository attachmentFilesRepository;

    private final Logger log = LoggerFactory.getLogger(AttachmentListResource.class);
    
    private static final String ENTITY_NAME = "attachmentList";

    private final AttachmentListRepository attachmentListRepository;

    public AttachmentListResource(AttachmentListRepository attachmentListRepository) {
        this.attachmentListRepository = attachmentListRepository;
    }

    /**
     * POST  /attachment-lists : Create a new attachmentList.
     *
     * @param attachmentList the attachmentList to create
     * @return the ResponseEntity with status 201 (Created) and with body the new attachmentList, or with status 400 (Bad Request) if the attachmentList has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/attachment-lists")
    @Timed
    public ResponseEntity<AttachmentList> createAttachmentList(@RequestBody AttachmentList attachmentList) throws URISyntaxException {
        log.debug("REST request to save AttachmentList : {}", attachmentList);
        if (attachmentList.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new attachmentList cannot already have an ID")).body(null);
        }
        AttachmentList result = attachmentListRepository.save(attachmentList);
        return ResponseEntity.created(new URI("/api/attachment-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /attachment-lists : Updates an existing attachmentList.
     *
     * @param attachmentList the attachmentList to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated attachmentList,
     * or with status 400 (Bad Request) if the attachmentList is not valid,
     * or with status 500 (Internal Server Error) if the attachmentList couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/attachment-lists")
    @Timed
    public ResponseEntity<AttachmentList> updateAttachmentList(@RequestBody AttachmentList attachmentList) throws URISyntaxException {
        log.debug("REST request to update AttachmentList : {}", attachmentList);
        if (attachmentList.getId() == null) {
            return createAttachmentList(attachmentList);
        }
        AttachmentList result = attachmentListRepository.save(attachmentList);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, attachmentList.getId().toString()))
            .body(result);
    }

    /**
     * GET  /attachment-lists : get all the attachmentLists.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of attachmentLists in body
     */
    @GetMapping("/attachment-lists")
    @Timed
    public ResponseEntity<List<AttachmentList>> getAllAttachmentLists(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of AttachmentLists");
        Page<AttachmentList> page = attachmentListRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/attachment-lists");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /attachment-lists/:id : get the "id" attachmentList.
     *
     * @param id the id of the attachmentList to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the attachmentList, or with status 404 (Not Found)
     */
    @GetMapping("/attachment-lists/{id}")
    @Timed
    public ResponseEntity<AttachmentList> getAttachmentList(@PathVariable Long id) {
        log.debug("REST request to get AttachmentList : {}", id);
        AttachmentList attachmentList = attachmentListRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(attachmentList));
    }

    /**
     * DELETE  /attachment-lists/:id : delete the "id" attachmentList.
     *
     * @param id the id of the attachmentList to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/attachment-lists/{id}")
    @Timed
    public ResponseEntity<Void> deleteAttachmentList(@PathVariable Long id) {
        log.debug("REST request to delete AttachmentList : {}", id);
        attachmentListRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    
    @GetMapping("/getAttchmentAndAttachmentFilesByAttachmentKey")
   	@Timed
   	public LinkedHashMap getAttchmentAndAttachmentFilesById(HttpServletRequest request, @RequestParam String attachmentKey)
    {
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	LinkedHashMap attachment=new LinkedHashMap();
    	AttachmentList attchMentBatch=attachmentListRepository.findByAttachmentKeyAndTenantId(attachmentKey,tenantId);
    	if(attchMentBatch!=null)
    	{
    		attachment.put("id", attchMentBatch.getId());
    		if(attchMentBatch.getAttachmentKey()!=null)
    			attachment.put("attachmentKey", attchMentBatch.getAttachmentKey());
    		else
    			attachment.put("attachmentKey", "");
    		if(attchMentBatch.getAttachmentCategory()!=null)
    			attachment.put("attachmentCategory", attchMentBatch.getAttachmentCategory());
    		else
    			attachment.put("attachmentCategory", "");
    		attachment.put("createdBy", attchMentBatch.getCreatedBy());
    		attachment.put("creationDate", attchMentBatch.getCreationDate());
    		attachment.put("lastUpdatedBy", attchMentBatch.getLastUpdatedBy());
    		attachment.put("lastUpdatedDate", attchMentBatch.getLastUpdatedDate());

    		List<LinkedHashMap> attachmentFilesListMap=new ArrayList<LinkedHashMap>();

    		List<AttachmentFiles> attachmentFilesList=attachmentFilesRepository.findByAttachmentId(attchMentBatch.getId());
    		attachment.put("attachmentFilesList", attachmentFilesList);
    	}
    	return attachment;

    }
    
    @PostMapping("/postAttchmentAndAttachmentFiles")
   	@Timed
   	public AttachmentList postAttchmentAndAttachmentFiles(HttpServletRequest request, @RequestBody AttachmentFileDto attachmentMap)
    {
    	HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	Long userId=Long.parseLong(map.get("userId").toString());
    
    	AttachmentList attchMentBatch=new AttachmentList();
    	//AttachmentList attchMentBatch=attachmentListRepository.findByAttachmentKeyAndTenantId(attachmentKey,tenantId);
    	if(attachmentMap!=null)
    	{
    		if(attachmentMap.getAttachmentKey()!=null)
    		attchMentBatch.setAttachmentKey(attachmentMap.getAttachmentKey());
    		if(attachmentMap.getAttachmentCategory()!=null)
    		attchMentBatch.setAttachmentCategory(attachmentMap.getAttachmentCategory());
    		attchMentBatch.setTenantId(tenantId);
    		attchMentBatch.setCreatedBy(userId);
    		attchMentBatch.setLastUpdatedBy(userId);
    		attchMentBatch.setCreationDate(ZonedDateTime.now());
    		attchMentBatch.setLastUpdatedDate(ZonedDateTime.now());
    		attchMentBatch=attachmentListRepository.save(attchMentBatch);
    		
    		if( attachmentMap.getAttachmentFilesList()!=null)
    		{
    			List<AttachmentFiles> filesList=attachmentMap.getAttachmentFilesList();
    			List<AttachmentFiles> attachmentFilesList=new ArrayList<AttachmentFiles>();
    			for(int i=0;i<filesList.size();i++)
    			{
    				AttachmentFiles file=new AttachmentFiles();
    				file.setAttachmentId(attchMentBatch.getId());
    				if(filesList.get(i).getFileName()!=null)
    				file.setFileName(filesList.get(i).getFileName().toString());
    				if(filesList.get(i).getFileType()!=null)
    				file.setFileType(filesList.get(i).getFileType().toString());
    				if(filesList.get(i).getFileContent()!=null)
    				{
    				file.setFileContent(filesList.get(i).getFileContent());
    				}
    				if(filesList.get(i).getUrl()!=null)
    				file.setUrl(filesList.get(i).getUrl().toString());
    				file.setCreatedBy(userId);
    				file.setLastUpdatedBy(userId);
    				file.setCreationDate(ZonedDateTime.now());
    				file.setLastUpdatedDate(ZonedDateTime.now());
    				attachmentFilesList.add(file);
    			}
    			attachmentFilesList=attachmentFilesRepository.save(attachmentFilesList);
    		}
    		
    	}

    	
    	return attchMentBatch;

    }
    
}
