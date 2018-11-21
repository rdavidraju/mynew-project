package com.nspl.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.domain.JobActions;

import com.nspl.app.repository.JobActionsRepository;
import com.nspl.app.repository.search.JobActionsSearchRepository;
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
 * REST controller for managing JobActions.
 */
@RestController
@RequestMapping("/api")
public class JobActionsResource {

    private final Logger log = LoggerFactory.getLogger(JobActionsResource.class);

    private static final String ENTITY_NAME = "jobActions";
        
    private final JobActionsRepository jobActionsRepository;

    private final JobActionsSearchRepository jobActionsSearchRepository;

    public JobActionsResource(JobActionsRepository jobActionsRepository, JobActionsSearchRepository jobActionsSearchRepository) {
        this.jobActionsRepository = jobActionsRepository;
        this.jobActionsSearchRepository = jobActionsSearchRepository;
    }

    /**
     * POST  /job-actions : Create a new jobActions.
     *
     * @param jobActions the jobActions to create
     * @return the ResponseEntity with status 201 (Created) and with body the new jobActions, or with status 400 (Bad Request) if the jobActions has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/job-actions")
    @Timed
    public ResponseEntity<JobActions> createJobActions(@RequestBody JobActions jobActions) throws URISyntaxException {
        log.debug("REST request to save JobActions : {}", jobActions);
        if (jobActions.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new jobActions cannot already have an ID")).body(null);
        }
        JobActions result = jobActionsRepository.save(jobActions);
        jobActionsSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/job-actions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /job-actions : Updates an existing jobActions.
     *
     * @param jobActions the jobActions to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated jobActions,
     * or with status 400 (Bad Request) if the jobActions is not valid,
     * or with status 500 (Internal Server Error) if the jobActions couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/job-actions")
    @Timed
    public ResponseEntity<JobActions> updateJobActions(@RequestBody JobActions jobActions) throws URISyntaxException {
        log.debug("REST request to update JobActions : {}", jobActions);
        if (jobActions.getId() == null) {
            return createJobActions(jobActions);
        }
        JobActions result = jobActionsRepository.save(jobActions);
        jobActionsSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, jobActions.getId().toString()))
            .body(result);
    }

    /**
     * GET  /job-actions : get all the jobActions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of jobActions in body
     */
    @GetMapping("/job-actions")
    @Timed
    public List<JobActions> getAllJobActions() {
        log.debug("REST request to get all JobActions");
        List<JobActions> jobActions = jobActionsRepository.findAll();
        return jobActions;
    }

    /**
     * GET  /job-actions/:id : get the "id" jobActions.
     *
     * @param id the id of the jobActions to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the jobActions, or with status 404 (Not Found)
     */
    @GetMapping("/job-actions/{id}")
    @Timed
    public ResponseEntity<JobActions> getJobActions(@PathVariable Long id) {
        log.debug("REST request to get JobActions : {}", id);
        JobActions jobActions = jobActionsRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(jobActions));
    }

    /**
     * DELETE  /job-actions/:id : delete the "id" jobActions.
     *
     * @param id the id of the jobActions to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/job-actions/{id}")
    @Timed
    public ResponseEntity<Void> deleteJobActions(@PathVariable Long id) {
        log.debug("REST request to delete JobActions : {}", id);
        jobActionsRepository.delete(id);
        jobActionsSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/job-actions?query=:query : search for the jobActions corresponding
     * to the query.
     *
     * @param query the query of the jobActions search 
     * @return the result of the search
     */
    @GetMapping("/_search/job-actions")
    @Timed
    public List<JobActions> searchJobActions(@RequestParam String query) {
        log.debug("REST request to search JobActions for query {}", query);
        return StreamSupport
            .stream(jobActionsSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
    
    
    @GetMapping("/jobActionsByJobId")
    @Timed
    public List<JobActions> jobActionsBasedOnJobId(@RequestParam String JobId) {
        log.info("REST request to get the Job Actions based on Scheduler Id: "+ JobId);
        List<JobActions> jobActionsList=jobActionsRepository.findByJobId(JobId);
        
        return jobActionsList;
    }


}
