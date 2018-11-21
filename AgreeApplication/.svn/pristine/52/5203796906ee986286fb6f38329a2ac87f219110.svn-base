package com.nspl.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.domain.AppRuleConditions;

import com.nspl.app.repository.AppRuleConditionsRepository;
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
 * REST controller for managing AppRuleConditions.
 */
@RestController
@RequestMapping("/api")
public class AppRuleConditionsResource {

    private final Logger log = LoggerFactory.getLogger(AppRuleConditionsResource.class);

    private static final String ENTITY_NAME = "appRuleConditions";
        
    private final AppRuleConditionsRepository appRuleConditionsRepository;

    public AppRuleConditionsResource(AppRuleConditionsRepository appRuleConditionsRepository) {
        this.appRuleConditionsRepository = appRuleConditionsRepository;
    }

    /**
     * POST  /app-rule-conditions : Create a new appRuleConditions.
     *
     * @param appRuleConditions the appRuleConditions to create
     * @return the ResponseEntity with status 201 (Created) and with body the new appRuleConditions, or with status 400 (Bad Request) if the appRuleConditions has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/app-rule-conditions")
    @Timed
    public ResponseEntity<AppRuleConditions> createAppRuleConditions(@RequestBody AppRuleConditions appRuleConditions) throws URISyntaxException {
        log.debug("REST request to save AppRuleConditions : {}", appRuleConditions);
        if (appRuleConditions.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new appRuleConditions cannot already have an ID")).body(null);
        }
        AppRuleConditions result = appRuleConditionsRepository.save(appRuleConditions);
        return ResponseEntity.created(new URI("/api/app-rule-conditions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /app-rule-conditions : Updates an existing appRuleConditions.
     *
     * @param appRuleConditions the appRuleConditions to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated appRuleConditions,
     * or with status 400 (Bad Request) if the appRuleConditions is not valid,
     * or with status 500 (Internal Server Error) if the appRuleConditions couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/app-rule-conditions")
    @Timed
    public ResponseEntity<AppRuleConditions> updateAppRuleConditions(@RequestBody AppRuleConditions appRuleConditions) throws URISyntaxException {
        log.debug("REST request to update AppRuleConditions : {}", appRuleConditions);
        if (appRuleConditions.getId() == null) {
            return createAppRuleConditions(appRuleConditions);
        }
        AppRuleConditions result = appRuleConditionsRepository.save(appRuleConditions);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, appRuleConditions.getId().toString()))
            .body(result);
    }

    /**
     * GET  /app-rule-conditions : get all the appRuleConditions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of appRuleConditions in body
     */
    @GetMapping("/app-rule-conditions")
    @Timed
    public ResponseEntity<List<AppRuleConditions>> getAllAppRuleConditions(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of AppRuleConditions");
        Page<AppRuleConditions> page = appRuleConditionsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/app-rule-conditions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /app-rule-conditions/:id : get the "id" appRuleConditions.
     *
     * @param id the id of the appRuleConditions to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the appRuleConditions, or with status 404 (Not Found)
     */
    @GetMapping("/app-rule-conditions/{id}")
    @Timed
    public ResponseEntity<AppRuleConditions> getAppRuleConditions(@PathVariable Long id) {
        log.debug("REST request to get AppRuleConditions : {}", id);
        AppRuleConditions appRuleConditions = appRuleConditionsRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(appRuleConditions));
    }

    /**
     * DELETE  /app-rule-conditions/:id : delete the "id" appRuleConditions.
     *
     * @param id the id of the appRuleConditions to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/app-rule-conditions/{id}")
    @Timed
    public ResponseEntity<Void> deleteAppRuleConditions(@PathVariable Long id) {
        log.debug("REST request to delete AppRuleConditions : {}", id);
        appRuleConditionsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
