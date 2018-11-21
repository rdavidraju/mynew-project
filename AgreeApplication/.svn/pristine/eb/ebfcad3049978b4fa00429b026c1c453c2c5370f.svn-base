package com.nspl.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.domain.JeLines;

import com.nspl.app.repository.JeLinesRepository;
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
 * REST controller for managing JeLines.
 */
@RestController
@RequestMapping("/api")
public class JeLinesResource {

    private final Logger log = LoggerFactory.getLogger(JeLinesResource.class);

    private static final String ENTITY_NAME = "jeLines";
        
    private final JeLinesRepository jeLinesRepository;

    public JeLinesResource(JeLinesRepository jeLinesRepository) {
        this.jeLinesRepository = jeLinesRepository;
    }

    /**
     * POST  /je-lines : Create a new jeLines.
     *
     * @param jeLines the jeLines to create
     * @return the ResponseEntity with status 201 (Created) and with body the new jeLines, or with status 400 (Bad Request) if the jeLines has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/je-lines")
    @Timed
    public ResponseEntity<JeLines> createJeLines(@RequestBody JeLines jeLines) throws URISyntaxException {
        log.debug("REST request to save JeLines : {}", jeLines);
        if (jeLines.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new jeLines cannot already have an ID")).body(null);
        }
        JeLines result = jeLinesRepository.save(jeLines);
        return ResponseEntity.created(new URI("/api/je-lines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /je-lines : Updates an existing jeLines.
     *
     * @param jeLines the jeLines to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated jeLines,
     * or with status 400 (Bad Request) if the jeLines is not valid,
     * or with status 500 (Internal Server Error) if the jeLines couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/je-lines")
    @Timed
    public ResponseEntity<JeLines> updateJeLines(@RequestBody JeLines jeLines) throws URISyntaxException {
        log.debug("REST request to update JeLines : {}", jeLines);
        if (jeLines.getId() == null) {
            return createJeLines(jeLines);
        }
        JeLines result = jeLinesRepository.save(jeLines);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, jeLines.getId().toString()))
            .body(result);
    }

    /**
     * GET  /je-lines : get all the jeLines.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of jeLines in body
     */
    @GetMapping("/je-lines")
    @Timed
    public ResponseEntity<List<JeLines>> getAllJeLines(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of JeLines");
        Page<JeLines> page = jeLinesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/je-lines");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /je-lines/:id : get the "id" jeLines.
     *
     * @param id the id of the jeLines to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the jeLines, or with status 404 (Not Found)
     */
    @GetMapping("/je-lines/{id}")
    @Timed
    public ResponseEntity<JeLines> getJeLines(@PathVariable Long id) {
        log.debug("REST request to get JeLines : {}", id);
        JeLines jeLines = jeLinesRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(jeLines));
    }

    /**
     * DELETE  /je-lines/:id : delete the "id" jeLines.
     *
     * @param id the id of the jeLines to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/je-lines/{id}")
    @Timed
    public ResponseEntity<Void> deleteJeLines(@PathVariable Long id) {
        log.debug("REST request to delete JeLines : {}", id);
        jeLinesRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
