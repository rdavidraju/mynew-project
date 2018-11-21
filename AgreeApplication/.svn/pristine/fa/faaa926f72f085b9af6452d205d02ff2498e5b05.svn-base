package com.nspl.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.domain.TemplAttributeMapping;

import com.nspl.app.repository.TemplAttributeMappingRepository;
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
 * REST controller for managing TemplAttributeMapping.
 */
@RestController
@RequestMapping("/api")
public class TemplAttributeMappingResource {

    private final Logger log = LoggerFactory.getLogger(TemplAttributeMappingResource.class);

    private static final String ENTITY_NAME = "templAttributeMapping";
        
    private final TemplAttributeMappingRepository templAttributeMappingRepository;

    public TemplAttributeMappingResource(TemplAttributeMappingRepository templAttributeMappingRepository) {
        this.templAttributeMappingRepository = templAttributeMappingRepository;
    }

    /**
     * POST  /templ-attribute-mappings : Create a new templAttributeMapping.
     *
     * @param templAttributeMapping the templAttributeMapping to create
     * @return the ResponseEntity with status 201 (Created) and with body the new templAttributeMapping, or with status 400 (Bad Request) if the templAttributeMapping has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/templ-attribute-mappings")
    @Timed
    public ResponseEntity<TemplAttributeMapping> createTemplAttributeMapping(@RequestBody TemplAttributeMapping templAttributeMapping) throws URISyntaxException {
        log.debug("REST request to save TemplAttributeMapping : {}", templAttributeMapping);
        if (templAttributeMapping.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new templAttributeMapping cannot already have an ID")).body(null);
        }
        TemplAttributeMapping result = templAttributeMappingRepository.save(templAttributeMapping);
        return ResponseEntity.created(new URI("/api/templ-attribute-mappings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /templ-attribute-mappings : Updates an existing templAttributeMapping.
     *
     * @param templAttributeMapping the templAttributeMapping to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated templAttributeMapping,
     * or with status 400 (Bad Request) if the templAttributeMapping is not valid,
     * or with status 500 (Internal Server Error) if the templAttributeMapping couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/templ-attribute-mappings")
    @Timed
    public ResponseEntity<TemplAttributeMapping> updateTemplAttributeMapping(@RequestBody TemplAttributeMapping templAttributeMapping) throws URISyntaxException {
        log.debug("REST request to update TemplAttributeMapping : {}", templAttributeMapping);
        if (templAttributeMapping.getId() == null) {
            return createTemplAttributeMapping(templAttributeMapping);
        }
        TemplAttributeMapping result = templAttributeMappingRepository.save(templAttributeMapping);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, templAttributeMapping.getId().toString()))
            .body(result);
    }

    /**
     * GET  /templ-attribute-mappings : get all the templAttributeMappings.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of templAttributeMappings in body
     */
    @GetMapping("/templ-attribute-mappings")
    @Timed
    public ResponseEntity<List<TemplAttributeMapping>> getAllTemplAttributeMappings(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of TemplAttributeMappings");
        Page<TemplAttributeMapping> page = templAttributeMappingRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/templ-attribute-mappings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /templ-attribute-mappings/:id : get the "id" templAttributeMapping.
     *
     * @param id the id of the templAttributeMapping to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the templAttributeMapping, or with status 404 (Not Found)
     */
    @GetMapping("/templ-attribute-mappings/{id}")
    @Timed
    public ResponseEntity<TemplAttributeMapping> getTemplAttributeMapping(@PathVariable Long id) {
        log.debug("REST request to get TemplAttributeMapping : {}", id);
        TemplAttributeMapping templAttributeMapping = templAttributeMappingRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(templAttributeMapping));
    }

    /**
     * DELETE  /templ-attribute-mappings/:id : delete the "id" templAttributeMapping.
     *
     * @param id the id of the templAttributeMapping to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/templ-attribute-mappings/{id}")
    @Timed
    public ResponseEntity<Void> deleteTemplAttributeMapping(@PathVariable Long id) {
        log.debug("REST request to delete TemplAttributeMapping : {}", id);
        templAttributeMappingRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
