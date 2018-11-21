package com.nspl.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.domain.FileTemplateSummaryToDetailMapping;

import com.nspl.app.repository.FileTemplateSummaryToDetailMappingRepository;
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

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing FileTemplateSummaryToDetailMapping.
 */
@RestController
@RequestMapping("/api")
public class FileTemplateSummaryToDetailMappingResource {

    private final Logger log = LoggerFactory.getLogger(FileTemplateSummaryToDetailMappingResource.class);

    private static final String ENTITY_NAME = "fileTemplateSummaryToDetailMapping";

    private final FileTemplateSummaryToDetailMappingRepository fileTemplateSummaryToDetailMappingRepository;

    public FileTemplateSummaryToDetailMappingResource(FileTemplateSummaryToDetailMappingRepository fileTemplateSummaryToDetailMappingRepository) {
        this.fileTemplateSummaryToDetailMappingRepository = fileTemplateSummaryToDetailMappingRepository;
    }

    /**
     * POST  /file-template-summary-to-detail-mappings : Create a new fileTemplateSummaryToDetailMapping.
     *
     * @param fileTemplateSummaryToDetailMapping the fileTemplateSummaryToDetailMapping to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fileTemplateSummaryToDetailMapping, or with status 400 (Bad Request) if the fileTemplateSummaryToDetailMapping has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/file-template-summary-to-detail-mappings")
    @Timed
    public ResponseEntity<FileTemplateSummaryToDetailMapping> createFileTemplateSummaryToDetailMapping(@RequestBody FileTemplateSummaryToDetailMapping fileTemplateSummaryToDetailMapping) throws URISyntaxException {
        log.debug("REST request to save FileTemplateSummaryToDetailMapping : {}", fileTemplateSummaryToDetailMapping);
        if (fileTemplateSummaryToDetailMapping.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new fileTemplateSummaryToDetailMapping cannot already have an ID")).body(null);
        }
        FileTemplateSummaryToDetailMapping result = fileTemplateSummaryToDetailMappingRepository.save(fileTemplateSummaryToDetailMapping);
        return ResponseEntity.created(new URI("/api/file-template-summary-to-detail-mappings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /file-template-summary-to-detail-mappings : Updates an existing fileTemplateSummaryToDetailMapping.
     *
     * @param fileTemplateSummaryToDetailMapping the fileTemplateSummaryToDetailMapping to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated fileTemplateSummaryToDetailMapping,
     * or with status 400 (Bad Request) if the fileTemplateSummaryToDetailMapping is not valid,
     * or with status 500 (Internal Server Error) if the fileTemplateSummaryToDetailMapping couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/file-template-summary-to-detail-mappings")
    @Timed
    public ResponseEntity<FileTemplateSummaryToDetailMapping> updateFileTemplateSummaryToDetailMapping(@RequestBody FileTemplateSummaryToDetailMapping fileTemplateSummaryToDetailMapping) throws URISyntaxException {
        log.debug("REST request to update FileTemplateSummaryToDetailMapping : {}", fileTemplateSummaryToDetailMapping);
        if (fileTemplateSummaryToDetailMapping.getId() == null) {
            return createFileTemplateSummaryToDetailMapping(fileTemplateSummaryToDetailMapping);
        }
        FileTemplateSummaryToDetailMapping result = fileTemplateSummaryToDetailMappingRepository.save(fileTemplateSummaryToDetailMapping);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, fileTemplateSummaryToDetailMapping.getId().toString()))
            .body(result);
    }

    /**
     * GET  /file-template-summary-to-detail-mappings : get all the fileTemplateSummaryToDetailMappings.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of fileTemplateSummaryToDetailMappings in body
     */
    @GetMapping("/file-template-summary-to-detail-mappings")
    @Timed
    public ResponseEntity<List<FileTemplateSummaryToDetailMapping>> getAllFileTemplateSummaryToDetailMappings(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of FileTemplateSummaryToDetailMappings");
        Page<FileTemplateSummaryToDetailMapping> page = fileTemplateSummaryToDetailMappingRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/file-template-summary-to-detail-mappings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /file-template-summary-to-detail-mappings/:id : get the "id" fileTemplateSummaryToDetailMapping.
     *
     * @param id the id of the fileTemplateSummaryToDetailMapping to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fileTemplateSummaryToDetailMapping, or with status 404 (Not Found)
     */
    @GetMapping("/file-template-summary-to-detail-mappings/{id}")
    @Timed
    public ResponseEntity<FileTemplateSummaryToDetailMapping> getFileTemplateSummaryToDetailMapping(@PathVariable Long id) {
        log.debug("REST request to get FileTemplateSummaryToDetailMapping : {}", id);
        FileTemplateSummaryToDetailMapping fileTemplateSummaryToDetailMapping = fileTemplateSummaryToDetailMappingRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(fileTemplateSummaryToDetailMapping));
    }

    /**
     * DELETE  /file-template-summary-to-detail-mappings/:id : delete the "id" fileTemplateSummaryToDetailMapping.
     *
     * @param id the id of the fileTemplateSummaryToDetailMapping to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/file-template-summary-to-detail-mappings/{id}")
    @Timed
    public ResponseEntity<Void> deleteFileTemplateSummaryToDetailMapping(@PathVariable Long id) {
        log.debug("REST request to delete FileTemplateSummaryToDetailMapping : {}", id);
        fileTemplateSummaryToDetailMappingRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
