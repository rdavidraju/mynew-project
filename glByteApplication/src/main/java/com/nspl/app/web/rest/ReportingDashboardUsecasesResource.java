package com.nspl.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.domain.ReportingDashboardUsecases;

import com.nspl.app.repository.ReportingDashboardUsecasesRepository;
import com.nspl.app.repository.search.ReportingDashboardUsecasesSearchRepository;
import com.nspl.app.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing ReportingDashboardUsecases.
 */
@RestController
@RequestMapping("/api")
public class ReportingDashboardUsecasesResource {

    private final Logger log = LoggerFactory.getLogger(ReportingDashboardUsecasesResource.class);

    private static final String ENTITY_NAME = "reportingDashboardUsecases";

    private final ReportingDashboardUsecasesRepository reportingDashboardUsecasesRepository;

    private final ReportingDashboardUsecasesSearchRepository reportingDashboardUsecasesSearchRepository;

    public ReportingDashboardUsecasesResource(ReportingDashboardUsecasesRepository reportingDashboardUsecasesRepository, ReportingDashboardUsecasesSearchRepository reportingDashboardUsecasesSearchRepository) {
        this.reportingDashboardUsecasesRepository = reportingDashboardUsecasesRepository;
        this.reportingDashboardUsecasesSearchRepository = reportingDashboardUsecasesSearchRepository;
    }

    /**
     * POST  /reporting-dashboard-usecases : Create a new reportingDashboardUsecases.
     *
     * @param reportingDashboardUsecases the reportingDashboardUsecases to create
     * @return the ResponseEntity with status 201 (Created) and with body the new reportingDashboardUsecases, or with status 400 (Bad Request) if the reportingDashboardUsecases has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/reporting-dashboard-usecases")
    @Timed
    public ResponseEntity<ReportingDashboardUsecases> createReportingDashboardUsecases(@RequestBody ReportingDashboardUsecases reportingDashboardUsecases) throws URISyntaxException {
        log.debug("REST request to save ReportingDashboardUsecases : {}", reportingDashboardUsecases);
        if (reportingDashboardUsecases.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new reportingDashboardUsecases cannot already have an ID")).body(null);
        }
        ReportingDashboardUsecases result = reportingDashboardUsecasesRepository.save(reportingDashboardUsecases);
        reportingDashboardUsecasesSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/reporting-dashboard-usecases/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    
    /**	@Rk
     * POST  /reporting-dashboard-usecases : Updating dashboard preferences.
     *
     * @param preferences maps array
     * @return String succss/not
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/updatePreferences")
    @Timed
    public ResponseEntity<ArrayList<String>> updatePreferences(@RequestBody ArrayList<HashMap> preferencesList) throws URISyntaxException {
        log.debug("REST request to updated : {}", preferencesList);
        ArrayList<String> updatedList=new ArrayList<String>(); 
        if(preferencesList.size()>0) {
        	for(int i=0, len=preferencesList.size();i<len;i++) {
        		ReportingDashboardUsecases eachCase= reportingDashboardUsecasesRepository.findOne(Long.parseLong((String) preferencesList.get(i).get("id")));
        		eachCase.setxAxis((int)preferencesList.get(i).get("x"));
        		eachCase.setyAxis((int)preferencesList.get(i).get("y"));
        		eachCase.setWidth((int)preferencesList.get(i).get("w"));
        		eachCase.setHeight((int)preferencesList.get(i).get("h"));
        		ReportingDashboardUsecases result = reportingDashboardUsecasesRepository.save(eachCase);
        		updatedList.add(result.getId().toString());
        	}
        }
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, String.join(", ", updatedList)))
                .body(updatedList);
    }

    /**
     * PUT  /reporting-dashboard-usecases : Updates an existing reportingDashboardUsecases.
     *
     * @param reportingDashboardUsecases the reportingDashboardUsecases to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated reportingDashboardUsecases,
     * or with status 400 (Bad Request) if the reportingDashboardUsecases is not valid,
     * or with status 500 (Internal Server Error) if the reportingDashboardUsecases couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/reporting-dashboard-usecases")
    @Timed
    public ResponseEntity<ReportingDashboardUsecases> updateReportingDashboardUsecases(@RequestBody ReportingDashboardUsecases reportingDashboardUsecases) throws URISyntaxException {
        log.debug("REST request to update ReportingDashboardUsecases : {}", reportingDashboardUsecases);
        if (reportingDashboardUsecases.getId() == null) {
            return createReportingDashboardUsecases(reportingDashboardUsecases);
        }
        ReportingDashboardUsecases result = reportingDashboardUsecasesRepository.save(reportingDashboardUsecases);
        reportingDashboardUsecasesSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, reportingDashboardUsecases.getId().toString()))
            .body(result);
    }

    /**
     * GET  /reporting-dashboard-usecases : get all the reportingDashboardUsecases.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of reportingDashboardUsecases in body
     */
    @GetMapping("/reporting-dashboard-usecases")
    @Timed
    public List<ReportingDashboardUsecases> getAllReportingDashboardUsecases() {
        log.debug("REST request to get all ReportingDashboardUsecases");
        return reportingDashboardUsecasesRepository.findAll();
    }

    /**
     * GET  /reporting-dashboard-usecases/:id : get the "id" reportingDashboardUsecases.
     *
     * @param id the id of the reportingDashboardUsecases to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the reportingDashboardUsecases, or with status 404 (Not Found)
     */
    @GetMapping("/reporting-dashboard-usecases/{id}")
    @Timed
    public ResponseEntity<ReportingDashboardUsecases> getReportingDashboardUsecases(@PathVariable Long id) {
        log.debug("REST request to get ReportingDashboardUsecases : {}", id);
        ReportingDashboardUsecases reportingDashboardUsecases = reportingDashboardUsecasesRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(reportingDashboardUsecases));
    }

    /**
     * DELETE  /reporting-dashboard-usecases/:id : delete the "id" reportingDashboardUsecases.
     *
     * @param id the id of the reportingDashboardUsecases to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/reporting-dashboard-usecases/{id}")
    @Timed
    public ResponseEntity<Void> deleteReportingDashboardUsecases(@PathVariable Long id) {
        log.debug("REST request to delete ReportingDashboardUsecases : {}", id);
        reportingDashboardUsecasesRepository.delete(id);
        reportingDashboardUsecasesSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/reporting-dashboard-usecases?query=:query : search for the reportingDashboardUsecases corresponding
     * to the query.
     *
     * @param query the query of the reportingDashboardUsecases search
     * @return the result of the search
     */
    @GetMapping("/_search/reporting-dashboard-usecases")
    @Timed
    public List<ReportingDashboardUsecases> searchReportingDashboardUsecases(@RequestParam String query) {
        log.debug("REST request to search ReportingDashboardUsecases for query {}", query);
        return StreamSupport
            .stream(reportingDashboardUsecasesSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
