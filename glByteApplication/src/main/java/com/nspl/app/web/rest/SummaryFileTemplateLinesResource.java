package com.nspl.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.domain.SummaryFileTemplateLines;

import com.nspl.app.repository.SummaryFileTemplateLinesRepository;
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
 * REST controller for managing SummaryFileTemplateLines.
 */
@RestController
@RequestMapping("/api")
public class SummaryFileTemplateLinesResource {

    private final Logger log = LoggerFactory.getLogger(SummaryFileTemplateLinesResource.class);

    private static final String ENTITY_NAME = "summaryFileTemplateLines";

    private final SummaryFileTemplateLinesRepository summaryFileTemplateLinesRepository;

    public SummaryFileTemplateLinesResource(SummaryFileTemplateLinesRepository summaryFileTemplateLinesRepository) {
        this.summaryFileTemplateLinesRepository = summaryFileTemplateLinesRepository;
    }

    /**
     * POST  /summary-file-template-lines : Create a new summaryFileTemplateLines.
     *
     * @param summaryFileTemplateLines the summaryFileTemplateLines to create
     * @return the ResponseEntity with status 201 (Created) and with body the new summaryFileTemplateLines, or with status 400 (Bad Request) if the summaryFileTemplateLines has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/summary-file-template-lines")
    @Timed
    public ResponseEntity<SummaryFileTemplateLines> createSummaryFileTemplateLines(@RequestBody SummaryFileTemplateLines summaryFileTemplateLines) throws URISyntaxException {
        log.debug("REST request to save SummaryFileTemplateLines : {}", summaryFileTemplateLines);
        if (summaryFileTemplateLines.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new summaryFileTemplateLines cannot already have an ID")).body(null);
        }
        SummaryFileTemplateLines result = summaryFileTemplateLinesRepository.save(summaryFileTemplateLines);
        return ResponseEntity.created(new URI("/api/summary-file-template-lines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /summary-file-template-lines : Updates an existing summaryFileTemplateLines.
     *
     * @param summaryFileTemplateLines the summaryFileTemplateLines to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated summaryFileTemplateLines,
     * or with status 400 (Bad Request) if the summaryFileTemplateLines is not valid,
     * or with status 500 (Internal Server Error) if the summaryFileTemplateLines couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/summary-file-template-lines")
    @Timed
    public ResponseEntity<SummaryFileTemplateLines> updateSummaryFileTemplateLines(@RequestBody SummaryFileTemplateLines summaryFileTemplateLines) throws URISyntaxException {
        log.debug("REST request to update SummaryFileTemplateLines : {}", summaryFileTemplateLines);
        if (summaryFileTemplateLines.getId() == null) {
            return createSummaryFileTemplateLines(summaryFileTemplateLines);
        }
        SummaryFileTemplateLines result = summaryFileTemplateLinesRepository.save(summaryFileTemplateLines);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, summaryFileTemplateLines.getId().toString()))
            .body(result);
    }

    /**
     * GET  /summary-file-template-lines : get all the summaryFileTemplateLines.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of summaryFileTemplateLines in body
     */
    @GetMapping("/summary-file-template-lines")
    @Timed
    public List<SummaryFileTemplateLines> getAllSummaryFileTemplateLines() {
        log.debug("REST request to get all SummaryFileTemplateLines");
        return summaryFileTemplateLinesRepository.findAll();
    }

    /**
     * GET  /summary-file-template-lines/:id : get the "id" summaryFileTemplateLines.
     *
     * @param id the id of the summaryFileTemplateLines to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the summaryFileTemplateLines, or with status 404 (Not Found)
     */
    @GetMapping("/summary-file-template-lines/{id}")
    @Timed
    public ResponseEntity<SummaryFileTemplateLines> getSummaryFileTemplateLines(@PathVariable Long id) {
        log.debug("REST request to get SummaryFileTemplateLines : {}", id);
        SummaryFileTemplateLines summaryFileTemplateLines = summaryFileTemplateLinesRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(summaryFileTemplateLines));
    }

    /**
     * DELETE  /summary-file-template-lines/:id : delete the "id" summaryFileTemplateLines.
     *
     * @param id the id of the summaryFileTemplateLines to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/summary-file-template-lines/{id}")
    @Timed
    public ResponseEntity<Void> deleteSummaryFileTemplateLines(@PathVariable Long id) {
        log.debug("REST request to delete SummaryFileTemplateLines : {}", id);
        summaryFileTemplateLinesRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
