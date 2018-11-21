package com.nspl.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.domain.JournalBatches;
import com.nspl.app.service.JournalBatchesService;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing JournalBatches.
 */
@RestController
@RequestMapping("/api")
public class JournalBatchesResource {

    private final Logger log = LoggerFactory.getLogger(JournalBatchesResource.class);

    private static final String ENTITY_NAME = "journalBatches";
        
    private final JournalBatchesService journalBatchesService;

    public JournalBatchesResource(JournalBatchesService journalBatchesService) {
        this.journalBatchesService = journalBatchesService;
    }

    /**
     * POST  /journal-batches : Create a new journalBatches.
     *
     * @param journalBatches the journalBatches to create
     * @return the ResponseEntity with status 201 (Created) and with body the new journalBatches, or with status 400 (Bad Request) if the journalBatches has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/journal-batches")
    @Timed
    public ResponseEntity<JournalBatches> createJournalBatches(@RequestBody JournalBatches journalBatches) throws URISyntaxException {
        log.debug("REST request to save JournalBatches : {}", journalBatches);
        if (journalBatches.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new journalBatches cannot already have an ID")).body(null);
        }
        JournalBatches result = journalBatchesService.save(journalBatches);
        return ResponseEntity.created(new URI("/api/journal-batches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /journal-batches : Updates an existing journalBatches.
     *
     * @param journalBatches the journalBatches to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated journalBatches,
     * or with status 400 (Bad Request) if the journalBatches is not valid,
     * or with status 500 (Internal Server Error) if the journalBatches couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/journal-batches")
    @Timed
    public ResponseEntity<JournalBatches> updateJournalBatches(@RequestBody JournalBatches journalBatches) throws URISyntaxException {
        log.debug("REST request to update JournalBatches : {}", journalBatches);
        if (journalBatches.getId() == null) {
            return createJournalBatches(journalBatches);
        }
        JournalBatches result = journalBatchesService.save(journalBatches);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, journalBatches.getId().toString()))
            .body(result);
    }

    /**
     * GET  /journal-batches : get all the journalBatches.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of journalBatches in body
     */
    @GetMapping("/journal-batches")
    @Timed
    public ResponseEntity<List<JournalBatches>> getAllJournalBatches(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of JournalBatches");
        Page<JournalBatches> page = journalBatchesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/journal-batches");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /journal-batches/:id : get the "id" journalBatches.
     *
     * @param id the id of the journalBatches to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the journalBatches, or with status 404 (Not Found)
     */
    @GetMapping("/journal-batches/{id}")
    @Timed
    public ResponseEntity<JournalBatches> getJournalBatches(@PathVariable Long id) {
        log.debug("REST request to get JournalBatches : {}", id);
        JournalBatches journalBatches = journalBatchesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(journalBatches));
    }

    /**
     * DELETE  /journal-batches/:id : delete the "id" journalBatches.
     *
     * @param id the id of the journalBatches to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/journal-batches/{id}")
    @Timed
    public ResponseEntity<Void> deleteJournalBatches(@PathVariable Long id) {
        log.debug("REST request to delete JournalBatches : {}", id);
        journalBatchesService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/journal-batches?query=:query : search for the journalBatches corresponding
     * to the query.
     *
     * @param query the query of the journalBatches search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/journal-batches")
    @Timed
    public ResponseEntity<List<JournalBatches>> searchJournalBatches(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of JournalBatches for query {}", query);
        Page<JournalBatches> page = journalBatchesService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/journal-batches");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
