package com.nspl.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.domain.RuleGroupDetails;

import com.nspl.app.repository.RuleGroupDetailsRepository;
import com.nspl.app.repository.search.RuleGroupDetailsSearchRepository;
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
 * REST controller for managing RuleGroupDetails.
 */
@RestController
@RequestMapping("/api")
public class RuleGroupDetailsResource {

    private final Logger log = LoggerFactory.getLogger(RuleGroupDetailsResource.class);

    private static final String ENTITY_NAME = "ruleGroupDetails";
        
    private final RuleGroupDetailsRepository ruleGroupDetailsRepository;

    private final RuleGroupDetailsSearchRepository ruleGroupDetailsSearchRepository;

    public RuleGroupDetailsResource(RuleGroupDetailsRepository ruleGroupDetailsRepository, RuleGroupDetailsSearchRepository ruleGroupDetailsSearchRepository) {
        this.ruleGroupDetailsRepository = ruleGroupDetailsRepository;
        this.ruleGroupDetailsSearchRepository = ruleGroupDetailsSearchRepository;
    }

    /**
     * POST  /rule-group-details : Create a new ruleGroupDetails.
     *
     * @param ruleGroupDetails the ruleGroupDetails to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ruleGroupDetails, or with status 400 (Bad Request) if the ruleGroupDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/rule-group-details")
    @Timed
    public ResponseEntity<RuleGroupDetails> createRuleGroupDetails(@RequestBody RuleGroupDetails ruleGroupDetails) throws URISyntaxException {
        log.debug("REST request to save RuleGroupDetails : {}", ruleGroupDetails);
        if (ruleGroupDetails.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new ruleGroupDetails cannot already have an ID")).body(null);
        }
        RuleGroupDetails result = ruleGroupDetailsRepository.save(ruleGroupDetails);
        ruleGroupDetailsSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/rule-group-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rule-group-details : Updates an existing ruleGroupDetails.
     *
     * @param ruleGroupDetails the ruleGroupDetails to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ruleGroupDetails,
     * or with status 400 (Bad Request) if the ruleGroupDetails is not valid,
     * or with status 500 (Internal Server Error) if the ruleGroupDetails couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/rule-group-details")
    @Timed
    public ResponseEntity<RuleGroupDetails> updateRuleGroupDetails(@RequestBody RuleGroupDetails ruleGroupDetails) throws URISyntaxException {
        log.debug("REST request to update RuleGroupDetails : {}", ruleGroupDetails);
        if (ruleGroupDetails.getId() == null) {
            return createRuleGroupDetails(ruleGroupDetails);
        }
        RuleGroupDetails result = ruleGroupDetailsRepository.save(ruleGroupDetails);
        ruleGroupDetailsSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ruleGroupDetails.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rule-group-details : get all the ruleGroupDetails.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of ruleGroupDetails in body
     */
    @GetMapping("/rule-group-details")
    @Timed
    public List<RuleGroupDetails> getAllRuleGroupDetails() {
        log.debug("REST request to get all RuleGroupDetails");
        List<RuleGroupDetails> ruleGroupDetails = ruleGroupDetailsRepository.findAll();
        return ruleGroupDetails;
    }

    /**
     * GET  /rule-group-details/:id : get the "id" ruleGroupDetails.
     *
     * @param id the id of the ruleGroupDetails to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ruleGroupDetails, or with status 404 (Not Found)
     */
    @GetMapping("/rule-group-details/{id}")
    @Timed
    public ResponseEntity<RuleGroupDetails> getRuleGroupDetails(@PathVariable Long id) {
        log.debug("REST request to get RuleGroupDetails : {}", id);
        RuleGroupDetails ruleGroupDetails = ruleGroupDetailsRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(ruleGroupDetails));
    }

    /**
     * DELETE  /rule-group-details/:id : delete the "id" ruleGroupDetails.
     *
     * @param id the id of the ruleGroupDetails to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/rule-group-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteRuleGroupDetails(@PathVariable Long id) {
        log.debug("REST request to delete RuleGroupDetails : {}", id);
        ruleGroupDetailsRepository.delete(id);
        ruleGroupDetailsSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/rule-group-details?query=:query : search for the ruleGroupDetails corresponding
     * to the query.
     *
     * @param query the query of the ruleGroupDetails search 
     * @return the result of the search
     */
    @GetMapping("/_search/rule-group-details")
    @Timed
    public List<RuleGroupDetails> searchRuleGroupDetails(@RequestParam String query) {
        log.debug("REST request to search RuleGroupDetails for query {}", query);
        return StreamSupport
            .stream(ruleGroupDetailsSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
