package com.nspl.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.domain.AcctRuleConditions;

import com.nspl.app.repository.AcctRuleConditionsRepository;
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
 * REST controller for managing AcctRuleConditions.
 */
@RestController
@RequestMapping("/api")
public class AcctRuleConditionsResource {

    private final Logger log = LoggerFactory.getLogger(AcctRuleConditionsResource.class);

    private static final String ENTITY_NAME = "acctRuleConditions";
        
    private final AcctRuleConditionsRepository acctRuleConditionsRepository;

    public AcctRuleConditionsResource(AcctRuleConditionsRepository acctRuleConditionsRepository) {
        this.acctRuleConditionsRepository = acctRuleConditionsRepository;
    }

    /**
     * POST  /acct-rule-conditions : Create a new acctRuleConditions.
     *
     * @param acctRuleConditions the acctRuleConditions to create
     * @return the ResponseEntity with status 201 (Created) and with body the new acctRuleConditions, or with status 400 (Bad Request) if the acctRuleConditions has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/acct-rule-conditions")
    @Timed
    public ResponseEntity<AcctRuleConditions> createAcctRuleConditions(@RequestBody AcctRuleConditions acctRuleConditions) throws URISyntaxException {
        log.debug("REST request to save AcctRuleConditions : {}", acctRuleConditions);
        if (acctRuleConditions.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new acctRuleConditions cannot already have an ID")).body(null);
        }
        AcctRuleConditions result = acctRuleConditionsRepository.save(acctRuleConditions);
        return ResponseEntity.created(new URI("/api/acct-rule-conditions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /acct-rule-conditions : Updates an existing acctRuleConditions.
     *
     * @param acctRuleConditions the acctRuleConditions to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated acctRuleConditions,
     * or with status 400 (Bad Request) if the acctRuleConditions is not valid,
     * or with status 500 (Internal Server Error) if the acctRuleConditions couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/acct-rule-conditions")
    @Timed
    public ResponseEntity<AcctRuleConditions> updateAcctRuleConditions(@RequestBody AcctRuleConditions acctRuleConditions) throws URISyntaxException {
        log.debug("REST request to update AcctRuleConditions : {}", acctRuleConditions);
        if (acctRuleConditions.getId() == null) {
            return createAcctRuleConditions(acctRuleConditions);
        }
        AcctRuleConditions result = acctRuleConditionsRepository.save(acctRuleConditions);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, acctRuleConditions.getId().toString()))
            .body(result);
    }

    /**
     * GET  /acct-rule-conditions : get all the acctRuleConditions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of acctRuleConditions in body
     */
    @GetMapping("/acct-rule-conditions")
    @Timed
    public ResponseEntity<List<AcctRuleConditions>> getAllAcctRuleConditions(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of AcctRuleConditions");
        Page<AcctRuleConditions> page = acctRuleConditionsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/acct-rule-conditions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /acct-rule-conditions/:id : get the "id" acctRuleConditions.
     *
     * @param id the id of the acctRuleConditions to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the acctRuleConditions, or with status 404 (Not Found)
     */
    @GetMapping("/acct-rule-conditions/{id}")
    @Timed
    public ResponseEntity<AcctRuleConditions> getAcctRuleConditions(@PathVariable Long id) {
        log.debug("REST request to get AcctRuleConditions : {}", id);
        AcctRuleConditions acctRuleConditions = acctRuleConditionsRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(acctRuleConditions));
    }

    /**
     * DELETE  /acct-rule-conditions/:id : delete the "id" acctRuleConditions.
     *
     * @param id the id of the acctRuleConditions to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/acct-rule-conditions/{id}")
    @Timed
    public ResponseEntity<Void> deleteAcctRuleConditions(@PathVariable Long id) {
        log.debug("REST request to delete AcctRuleConditions : {}", id);
        acctRuleConditionsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
