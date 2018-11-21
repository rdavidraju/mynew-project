package com.nspl.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.domain.LineCriteria;

import com.nspl.app.repository.LineCriteriaRepository;
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
 * REST controller for managing LineCriteria.
 */
@RestController
@RequestMapping("/api")
public class LineCriteriaResource {

    private final Logger log = LoggerFactory.getLogger(LineCriteriaResource.class);

    private static final String ENTITY_NAME = "lineCriteria";
        
    private final LineCriteriaRepository lineCriteriaRepository;

    public LineCriteriaResource(LineCriteriaRepository lineCriteriaRepository) {
        this.lineCriteriaRepository = lineCriteriaRepository;
    }

    /**
     * POST  /line-criteria : Create a new lineCriteria.
     *
     * @param lineCriteria the lineCriteria to create
     * @return the ResponseEntity with status 201 (Created) and with body the new lineCriteria, or with status 400 (Bad Request) if the lineCriteria has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/line-criteria")
    @Timed
    public ResponseEntity<LineCriteria> createLineCriteria(@RequestBody LineCriteria lineCriteria) throws URISyntaxException {
        log.debug("REST request to save LineCriteria : {}", lineCriteria);
        if (lineCriteria.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new lineCriteria cannot already have an ID")).body(null);
        }
        LineCriteria result = lineCriteriaRepository.save(lineCriteria);
        return ResponseEntity.created(new URI("/api/line-criteria/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /line-criteria : Updates an existing lineCriteria.
     *
     * @param lineCriteria the lineCriteria to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated lineCriteria,
     * or with status 400 (Bad Request) if the lineCriteria is not valid,
     * or with status 500 (Internal Server Error) if the lineCriteria couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/line-criteria")
    @Timed
    public ResponseEntity<LineCriteria> updateLineCriteria(@RequestBody LineCriteria lineCriteria) throws URISyntaxException {
        log.debug("REST request to update LineCriteria : {}", lineCriteria);
        if (lineCriteria.getId() == null) {
            return createLineCriteria(lineCriteria);
        }
        LineCriteria result = lineCriteriaRepository.save(lineCriteria);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, lineCriteria.getId().toString()))
            .body(result);
    }

    /**
     * GET  /line-criteria : get all the lineCriteria.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of lineCriteria in body
     */
    @GetMapping("/line-criteria")
    @Timed
    public ResponseEntity<List<LineCriteria>> getAllLineCriteria(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of LineCriteria");
        Page<LineCriteria> page = lineCriteriaRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/line-criteria");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /line-criteria/:id : get the "id" lineCriteria.
     *
     * @param id the id of the lineCriteria to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the lineCriteria, or with status 404 (Not Found)
     */
    @GetMapping("/line-criteria/{id}")
    @Timed
    public ResponseEntity<LineCriteria> getLineCriteria(@PathVariable Long id) {
        log.debug("REST request to get LineCriteria : {}", id);
        LineCriteria lineCriteria = lineCriteriaRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(lineCriteria));
    }

    /**
     * DELETE  /line-criteria/:id : delete the "id" lineCriteria.
     *
     * @param id the id of the lineCriteria to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/line-criteria/{id}")
    @Timed
    public ResponseEntity<Void> deleteLineCriteria(@PathVariable Long id) {
        log.debug("REST request to delete LineCriteria : {}", id);
        lineCriteriaRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
