package com.nspl.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.domain.RowConditions;

import com.nspl.app.repository.RowConditionsRepository;
import com.nspl.app.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing RowConditions.
 */
@RestController
@RequestMapping("/api")
public class RowConditionsResource {

    private final Logger log = LoggerFactory.getLogger(RowConditionsResource.class);

    private static final String ENTITY_NAME = "rowConditions";

    private final RowConditionsRepository rowConditionsRepository;

    public RowConditionsResource(RowConditionsRepository rowConditionsRepository) {
        this.rowConditionsRepository = rowConditionsRepository;
    }

    /**
     * POST  /row-conditions : Create a new rowConditions.
     *
     * @param rowConditions the rowConditions to create
     * @return the ResponseEntity with status 201 (Created) and with body the new rowConditions, or with status 400 (Bad Request) if the rowConditions has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/row-conditions")
    @Timed
    public ResponseEntity<RowConditions> createRowConditions(@RequestBody RowConditions rowConditions) throws URISyntaxException {
        log.debug("REST request to save RowConditions : {}", rowConditions);
        if (rowConditions.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new rowConditions cannot already have an ID")).body(null);
        }
        RowConditions result = rowConditionsRepository.save(rowConditions);
        return ResponseEntity.created(new URI("/api/row-conditions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /row-conditions : Updates an existing rowConditions.
     *
     * @param rowConditions the rowConditions to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated rowConditions,
     * or with status 400 (Bad Request) if the rowConditions is not valid,
     * or with status 500 (Internal Server Error) if the rowConditions couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/row-conditions")
    @Timed
    public ResponseEntity<RowConditions> updateRowConditions(@RequestBody RowConditions rowConditions) throws URISyntaxException {
        log.debug("REST request to update RowConditions : {}", rowConditions);
        if (rowConditions.getId() == null) {
            return createRowConditions(rowConditions);
        }
        RowConditions result = rowConditionsRepository.save(rowConditions);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, rowConditions.getId().toString()))
            .body(result);
    }

    /**
     * GET  /row-conditions : get all the rowConditions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of rowConditions in body
     */
    @GetMapping("/row-conditions")
    @Timed
    public List<RowConditions> getAllRowConditions() {
        log.debug("REST request to get all RowConditions");
        return rowConditionsRepository.findAll();
    }

    /**
     * GET  /row-conditions/:id : get the "id" rowConditions.
     *
     * @param id the id of the rowConditions to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the rowConditions, or with status 404 (Not Found)
     */
    @GetMapping("/row-conditions/{id}")
    @Timed
    public ResponseEntity<RowConditions> getRowConditions(@PathVariable Long id) {
        log.debug("REST request to get RowConditions : {}", id);
        RowConditions rowConditions = rowConditionsRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rowConditions));
    }

    /**
     * DELETE  /row-conditions/:id : delete the "id" rowConditions.
     *
     * @param id the id of the rowConditions to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/row-conditions/{id}")
    @Timed
    public ResponseEntity<Void> deleteRowConditions(@PathVariable Long id) {
        log.debug("REST request to delete RowConditions : {}", id);
        rowConditionsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
