package com.nspl.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.domain.AcctRuleDerivations;

import com.nspl.app.repository.AcctRuleDerivationsRepository;
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
 * REST controller for managing AcctRuleDerivations.
 */
@RestController
@RequestMapping("/api")
public class AcctRuleDerivationsResource {

    private final Logger log = LoggerFactory.getLogger(AcctRuleDerivationsResource.class);

    private static final String ENTITY_NAME = "acctRuleDerivations";
        
    private final AcctRuleDerivationsRepository acctRuleDerivationsRepository;

    public AcctRuleDerivationsResource(AcctRuleDerivationsRepository acctRuleDerivationsRepository) {
        this.acctRuleDerivationsRepository = acctRuleDerivationsRepository;
    }

    /**
     * POST  /acct-rule-derivations : Create a new acctRuleDerivations.
     *
     * @param acctRuleDerivations the acctRuleDerivations to create
     * @return the ResponseEntity with status 201 (Created) and with body the new acctRuleDerivations, or with status 400 (Bad Request) if the acctRuleDerivations has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/acct-rule-derivations")
    @Timed
    public ResponseEntity<AcctRuleDerivations> createAcctRuleDerivations(@RequestBody AcctRuleDerivations acctRuleDerivations) throws URISyntaxException {
        log.debug("REST request to save AcctRuleDerivations : {}", acctRuleDerivations);
        if (acctRuleDerivations.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new acctRuleDerivations cannot already have an ID")).body(null);
        }
        AcctRuleDerivations result = acctRuleDerivationsRepository.save(acctRuleDerivations);
        return ResponseEntity.created(new URI("/api/acct-rule-derivations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /acct-rule-derivations : Updates an existing acctRuleDerivations.
     *
     * @param acctRuleDerivations the acctRuleDerivations to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated acctRuleDerivations,
     * or with status 400 (Bad Request) if the acctRuleDerivations is not valid,
     * or with status 500 (Internal Server Error) if the acctRuleDerivations couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/acct-rule-derivations")
    @Timed
    public ResponseEntity<AcctRuleDerivations> updateAcctRuleDerivations(@RequestBody AcctRuleDerivations acctRuleDerivations) throws URISyntaxException {
        log.debug("REST request to update AcctRuleDerivations : {}", acctRuleDerivations);
        if (acctRuleDerivations.getId() == null) {
            return createAcctRuleDerivations(acctRuleDerivations);
        }
        AcctRuleDerivations result = acctRuleDerivationsRepository.save(acctRuleDerivations);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, acctRuleDerivations.getId().toString()))
            .body(result);
    }

    /**
     * GET  /acct-rule-derivations : get all the acctRuleDerivations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of acctRuleDerivations in body
     */
    @GetMapping("/acct-rule-derivations")
    @Timed
    public ResponseEntity<List<AcctRuleDerivations>> getAllAcctRuleDerivations(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of AcctRuleDerivations");
        Page<AcctRuleDerivations> page = acctRuleDerivationsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/acct-rule-derivations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /acct-rule-derivations/:id : get the "id" acctRuleDerivations.
     *
     * @param id the id of the acctRuleDerivations to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the acctRuleDerivations, or with status 404 (Not Found)
     */
    @GetMapping("/acct-rule-derivations/{id}")
    @Timed
    public ResponseEntity<AcctRuleDerivations> getAcctRuleDerivations(@PathVariable Long id) {
        log.debug("REST request to get AcctRuleDerivations : {}", id);
        AcctRuleDerivations acctRuleDerivations = acctRuleDerivationsRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(acctRuleDerivations));
    }

    /**
     * DELETE  /acct-rule-derivations/:id : delete the "id" acctRuleDerivations.
     *
     * @param id the id of the acctRuleDerivations to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/acct-rule-derivations/{id}")
    @Timed
    public ResponseEntity<Void> deleteAcctRuleDerivations(@PathVariable Long id) {
        log.debug("REST request to delete AcctRuleDerivations : {}", id);
        acctRuleDerivationsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
