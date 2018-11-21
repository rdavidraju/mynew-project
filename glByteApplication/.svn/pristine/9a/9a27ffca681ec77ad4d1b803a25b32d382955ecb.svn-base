package com.nspl.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.domain.JeLdrDetails;

import com.nspl.app.repository.JeLdrDetailsRepository;
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
 * REST controller for managing JeLdrDetails.
 */
@RestController
@RequestMapping("/api")
public class JeLdrDetailsResource {

    private final Logger log = LoggerFactory.getLogger(JeLdrDetailsResource.class);

    private static final String ENTITY_NAME = "jeLdrDetails";

    private final JeLdrDetailsRepository jeLdrDetailsRepository;

    public JeLdrDetailsResource(JeLdrDetailsRepository jeLdrDetailsRepository) {
        this.jeLdrDetailsRepository = jeLdrDetailsRepository;
    }

    /**
     * POST  /je-ldr-details : Create a new jeLdrDetails.
     *
     * @param jeLdrDetails the jeLdrDetails to create
     * @return the ResponseEntity with status 201 (Created) and with body the new jeLdrDetails, or with status 400 (Bad Request) if the jeLdrDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/je-ldr-details")
    @Timed
    public ResponseEntity<JeLdrDetails> createJeLdrDetails(@RequestBody JeLdrDetails jeLdrDetails) throws URISyntaxException {
        log.debug("REST request to save JeLdrDetails : {}", jeLdrDetails);
        if (jeLdrDetails.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new jeLdrDetails cannot already have an ID")).body(null);
        }
        JeLdrDetails result = jeLdrDetailsRepository.save(jeLdrDetails);
        return ResponseEntity.created(new URI("/api/je-ldr-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /je-ldr-details : Updates an existing jeLdrDetails.
     *
     * @param jeLdrDetails the jeLdrDetails to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated jeLdrDetails,
     * or with status 400 (Bad Request) if the jeLdrDetails is not valid,
     * or with status 500 (Internal Server Error) if the jeLdrDetails couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/je-ldr-details")
    @Timed
    public ResponseEntity<JeLdrDetails> updateJeLdrDetails(@RequestBody JeLdrDetails jeLdrDetails) throws URISyntaxException {
        log.debug("REST request to update JeLdrDetails : {}", jeLdrDetails);
        if (jeLdrDetails.getId() == null) {
            return createJeLdrDetails(jeLdrDetails);
        }
        JeLdrDetails result = jeLdrDetailsRepository.save(jeLdrDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, jeLdrDetails.getId().toString()))
            .body(result);
    }

    /**
     * GET  /je-ldr-details : get all the jeLdrDetails.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of jeLdrDetails in body
     */
    @GetMapping("/je-ldr-details")
    @Timed
    public ResponseEntity<List<JeLdrDetails>> getAllJeLdrDetails(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of JeLdrDetails");
        Page<JeLdrDetails> page = jeLdrDetailsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/je-ldr-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /je-ldr-details/:id : get the "id" jeLdrDetails.
     *
     * @param id the id of the jeLdrDetails to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the jeLdrDetails, or with status 404 (Not Found)
     */
    @GetMapping("/je-ldr-details/{id}")
    @Timed
    public ResponseEntity<JeLdrDetails> getJeLdrDetails(@PathVariable Long id) {
        log.debug("REST request to get JeLdrDetails : {}", id);
        JeLdrDetails jeLdrDetails = jeLdrDetailsRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(jeLdrDetails));
    }

    /**
     * DELETE  /je-ldr-details/:id : delete the "id" jeLdrDetails.
     *
     * @param id the id of the jeLdrDetails to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/je-ldr-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteJeLdrDetails(@PathVariable Long id) {
        log.debug("REST request to delete JeLdrDetails : {}", id);
        jeLdrDetailsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
