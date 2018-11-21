package com.nspl.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.domain.AttachmentFiles;
import com.nspl.app.repository.AttachmentFilesRepository;
import com.nspl.app.service.UserJdbcService;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * REST controller for managing AttachmentFiles.
 */
@RestController
@RequestMapping("/api")
public class AttachmentFilesResource {

    private final Logger log = LoggerFactory.getLogger(AttachmentFilesResource.class);

    private static final String ENTITY_NAME = "attachmentFiles";

    private final AttachmentFilesRepository attachmentFilesRepository;

    @Inject
    UserJdbcService userJdbcService;
    
    
    public AttachmentFilesResource(AttachmentFilesRepository attachmentFilesRepository) {
        this.attachmentFilesRepository = attachmentFilesRepository;
    }

    /**
     * POST  /attachment-files : Create a new attachmentFiles.
     *
     * @param attachmentFiles the attachmentFiles to create
     * @return the ResponseEntity with status 201 (Created) and with body the new attachmentFiles, or with status 400 (Bad Request) if the attachmentFiles has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/attachment-files")
    @Timed
    public ResponseEntity<AttachmentFiles> createAttachmentFiles(@RequestBody AttachmentFiles attachmentFiles) throws URISyntaxException {
        log.debug("REST request to save AttachmentFiles : {}", attachmentFiles);
        if (attachmentFiles.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new attachmentFiles cannot already have an ID")).body(null);
        }
        AttachmentFiles result = attachmentFilesRepository.save(attachmentFiles);
        return ResponseEntity.created(new URI("/api/attachment-files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /attachment-files : Updates an existing attachmentFiles.
     *
     * @param attachmentFiles the attachmentFiles to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated attachmentFiles,
     * or with status 400 (Bad Request) if the attachmentFiles is not valid,
     * or with status 500 (Internal Server Error) if the attachmentFiles couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/attachment-files")
    @Timed
    public ResponseEntity<AttachmentFiles> updateAttachmentFiles(@RequestBody AttachmentFiles attachmentFiles) throws URISyntaxException {
        log.debug("REST request to update AttachmentFiles : {}", attachmentFiles);
        if (attachmentFiles.getId() == null) {
            return createAttachmentFiles(attachmentFiles);
        }
        AttachmentFiles result = attachmentFilesRepository.save(attachmentFiles);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, attachmentFiles.getId().toString()))
            .body(result);
    }

    /**
     * GET  /attachment-files : get all the attachmentFiles.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of attachmentFiles in body
     */
    @GetMapping("/attachment-files")
    @Timed
    public ResponseEntity<List<AttachmentFiles>> getAllAttachmentFiles(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of AttachmentFiles");
        Page<AttachmentFiles> page = attachmentFilesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/attachment-files");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /attachment-files/:id : get the "id" attachmentFiles.
     *
     * @param id the id of the attachmentFiles to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the attachmentFiles, or with status 404 (Not Found)
     */
    @GetMapping("/attachment-files/{id}")
    @Timed
    public ResponseEntity<AttachmentFiles> getAttachmentFiles(@PathVariable Long id) {
        log.debug("REST request to get AttachmentFiles : {}", id);
        AttachmentFiles attachmentFiles = attachmentFilesRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(attachmentFiles));
    }

    /**
     * DELETE  /attachment-files/:id : delete the "id" attachmentFiles.
     *
     * @param id the id of the attachmentFiles to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/attachment-files/{id}")
    @Timed
    public ResponseEntity<Void> deleteAttachmentFiles(@PathVariable Long id) {
        log.debug("REST request to delete AttachmentFiles : {}", id);
        attachmentFilesRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    
    
    @PostMapping("/createAttachmentFiles")
    @Timed
    public List<AttachmentFiles> createAttachmentFilesList(@RequestBody List<AttachmentFiles> attachmentFiles,HttpServletRequest request) throws URISyntaxException {
        log.debug("REST request to save AttachmentFiles : {}", attachmentFiles);
       /* if (attachmentFiles.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new attachmentFiles cannot already have an ID")).body(null);
        }*/
        HashMap map=userJdbcService.getuserInfoFromToken(request);
    	Long tenantId=Long.parseLong(map.get("tenantId").toString());
    	Long userId=Long.parseLong(map.get("userId").toString());
    	 List<AttachmentFiles> result=new ArrayList<AttachmentFiles>();
       for(int i=0;i<attachmentFiles.size();i++)
       {
    	   AttachmentFiles attchfile=attachmentFiles.get(i);
    	   attchfile.setCreatedBy(userId);
    	   attchfile.setLastUpdatedBy(userId);
    	   attchfile.setCreationDate(ZonedDateTime.now());
    	   attchfile.setLastUpdatedDate(ZonedDateTime.now());
    	   result.add(attchfile);
       }
       result = attachmentFilesRepository.save(result);
        return result;
            
    }

}
