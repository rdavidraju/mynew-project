package com.nspl.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.domain.RuleConditions;

import com.nspl.app.repository.RuleConditionsRepository;
import com.nspl.app.repository.search.RuleConditionsSearchRepository;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing RuleConditions.
 */
@RestController
@RequestMapping("/api")
public class RuleConditionsResource {

    private final Logger log = LoggerFactory.getLogger(RuleConditionsResource.class);

    private static final String ENTITY_NAME = "ruleConditions";
        
    private final RuleConditionsRepository ruleConditionsRepository;

    private final RuleConditionsSearchRepository ruleConditionsSearchRepository;

    public RuleConditionsResource(RuleConditionsRepository ruleConditionsRepository, RuleConditionsSearchRepository ruleConditionsSearchRepository) {
        this.ruleConditionsRepository = ruleConditionsRepository;
        this.ruleConditionsSearchRepository = ruleConditionsSearchRepository;
    }

    /**
     * POST  /rule-conditions : Create a new ruleConditions.
     *
     * @param ruleConditions the ruleConditions to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ruleConditions, or with status 400 (Bad Request) if the ruleConditions has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/rule-conditions")
    @Timed
    public ResponseEntity<RuleConditions> createRuleConditions(@RequestBody RuleConditions ruleConditions) throws URISyntaxException {
        log.debug("REST request to save RuleConditions : {}", ruleConditions);
        if (ruleConditions.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new ruleConditions cannot already have an ID")).body(null);
        }
        RuleConditions result = ruleConditionsRepository.save(ruleConditions);
        ruleConditionsSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/rule-conditions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rule-conditions : Updates an existing ruleConditions.
     *
     * @param ruleConditions the ruleConditions to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ruleConditions,
     * or with status 400 (Bad Request) if the ruleConditions is not valid,
     * or with status 500 (Internal Server Error) if the ruleConditions couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/rule-conditions")
    @Timed
    public ResponseEntity<RuleConditions> updateRuleConditions(@RequestBody RuleConditions ruleConditions) throws URISyntaxException {
        log.debug("REST request to update RuleConditions : {}", ruleConditions);
        if (ruleConditions.getId() == null) {
            return createRuleConditions(ruleConditions);
        }
        RuleConditions result = ruleConditionsRepository.save(ruleConditions);
        ruleConditionsSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ruleConditions.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rule-conditions : get all the ruleConditions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of ruleConditions in body
     */
    @GetMapping("/rule-conditions")
    @Timed
    public List<RuleConditions> getAllRuleConditions() {
        log.debug("REST request to get all RuleConditions");
        List<RuleConditions> ruleConditions = ruleConditionsRepository.findAll();
        return ruleConditions;
    }

    /**
     * GET  /rule-conditions/:id : get the "id" ruleConditions.
     *
     * @param id the id of the ruleConditions to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ruleConditions, or with status 404 (Not Found)
     */
    @GetMapping("/rule-conditions/{id}")
    @Timed
    public ResponseEntity<RuleConditions> getRuleConditions(@PathVariable Long id) {
        log.debug("REST request to get RuleConditions : {}", id);
        RuleConditions ruleConditions = ruleConditionsRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(ruleConditions));
    }

    /**
     * DELETE  /rule-conditions/:id : delete the "id" ruleConditions.
     *
     * @param id the id of the ruleConditions to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/rule-conditions/{id}")
    @Timed
    public ResponseEntity<Void> deleteRuleConditions(@PathVariable Long id) {
        log.debug("REST request to delete RuleConditions : {}", id);
        ruleConditionsRepository.delete(id);
        ruleConditionsSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/rule-conditions?query=:query : search for the ruleConditions corresponding
     * to the query.
     *
     * @param query the query of the ruleConditions search 
     * @return the result of the search
     */
    @GetMapping("/_search/rule-conditions")
    @Timed
    public List<RuleConditions> searchRuleConditions(@RequestParam String query) {
        log.debug("REST request to search RuleConditions for query {}", query);
        return StreamSupport
            .stream(ruleConditionsSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
