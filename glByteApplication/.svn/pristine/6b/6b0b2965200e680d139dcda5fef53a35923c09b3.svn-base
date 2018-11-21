package com.nspl.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.domain.ReconciliationDuplicateResult;

import com.nspl.app.repository.ReconciliationDuplicateResultRepository;
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
 * REST controller for managing ReconciliationDuplicateResult.
 */
@RestController
@RequestMapping("/api")
public class ReconciliationDuplicateResultResource {

    private final Logger log = LoggerFactory.getLogger(ReconciliationDuplicateResultResource.class);

    private static final String ENTITY_NAME = "reconciliationDuplicateResult";

    private final ReconciliationDuplicateResultRepository reconciliationDuplicateResultRepository;

    public ReconciliationDuplicateResultResource(ReconciliationDuplicateResultRepository reconciliationDuplicateResultRepository) {
        this.reconciliationDuplicateResultRepository = reconciliationDuplicateResultRepository;
    }

    /**
     * POST  /reconciliation-duplicate-results : Create a new reconciliationDuplicateResult.
     *
     * @param reconciliationDuplicateResult the reconciliationDuplicateResult to create
     * @return the ResponseEntity with status 201 (Created) and with body the new reconciliationDuplicateResult, or with status 400 (Bad Request) if the reconciliationDuplicateResult has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/reconciliation-duplicate-results")
    @Timed
    public ResponseEntity<ReconciliationDuplicateResult> createReconciliationDuplicateResult(@RequestBody ReconciliationDuplicateResult reconciliationDuplicateResult) throws URISyntaxException {
        log.debug("REST request to save ReconciliationDuplicateResult : {}", reconciliationDuplicateResult);
        if (reconciliationDuplicateResult.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new reconciliationDuplicateResult cannot already have an ID")).body(null);
        }
        ReconciliationDuplicateResult result = reconciliationDuplicateResultRepository.save(reconciliationDuplicateResult);
        return ResponseEntity.created(new URI("/api/reconciliation-duplicate-results/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /reconciliation-duplicate-results : Updates an existing reconciliationDuplicateResult.
     *
     * @param reconciliationDuplicateResult the reconciliationDuplicateResult to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated reconciliationDuplicateResult,
     * or with status 400 (Bad Request) if the reconciliationDuplicateResult is not valid,
     * or with status 500 (Internal Server Error) if the reconciliationDuplicateResult couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/reconciliation-duplicate-results")
    @Timed
    public ResponseEntity<ReconciliationDuplicateResult> updateReconciliationDuplicateResult(@RequestBody ReconciliationDuplicateResult reconciliationDuplicateResult) throws URISyntaxException {
        log.debug("REST request to update ReconciliationDuplicateResult : {}", reconciliationDuplicateResult);
        if (reconciliationDuplicateResult.getId() == null) {
            return createReconciliationDuplicateResult(reconciliationDuplicateResult);
        }
        ReconciliationDuplicateResult result = reconciliationDuplicateResultRepository.save(reconciliationDuplicateResult);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, reconciliationDuplicateResult.getId().toString()))
            .body(result);
    }

    /**
     * GET  /reconciliation-duplicate-results : get all the reconciliationDuplicateResults.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of reconciliationDuplicateResults in body
     */
    @GetMapping("/reconciliation-duplicate-results")
    @Timed
    public ResponseEntity<List<ReconciliationDuplicateResult>> getAllReconciliationDuplicateResults(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of ReconciliationDuplicateResults");
        Page<ReconciliationDuplicateResult> page = reconciliationDuplicateResultRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/reconciliation-duplicate-results");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /reconciliation-duplicate-results/:id : get the "id" reconciliationDuplicateResult.
     *
     * @param id the id of the reconciliationDuplicateResult to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the reconciliationDuplicateResult, or with status 404 (Not Found)
     */
    @GetMapping("/reconciliation-duplicate-results/{id}")
    @Timed
    public ResponseEntity<ReconciliationDuplicateResult> getReconciliationDuplicateResult(@PathVariable Long id) {
        log.debug("REST request to get ReconciliationDuplicateResult : {}", id);
        ReconciliationDuplicateResult reconciliationDuplicateResult = reconciliationDuplicateResultRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(reconciliationDuplicateResult));
    }

    /**
     * DELETE  /reconciliation-duplicate-results/:id : delete the "id" reconciliationDuplicateResult.
     *
     * @param id the id of the reconciliationDuplicateResult to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/reconciliation-duplicate-results/{id}")
    @Timed
    public ResponseEntity<Void> deleteReconciliationDuplicateResult(@PathVariable Long id) {
        log.debug("REST request to delete ReconciliationDuplicateResult : {}", id);
        reconciliationDuplicateResultRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
