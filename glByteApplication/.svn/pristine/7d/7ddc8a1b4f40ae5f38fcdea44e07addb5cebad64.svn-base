package com.nspl.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.domain.DataViewsSrcMappings;

import com.nspl.app.repository.DataViewsSrcMappingsRepository;
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
 * REST controller for managing DataViewsSrcMappings.
 */
@RestController
@RequestMapping("/api")
public class DataViewsSrcMappingsResource {

    private final Logger log = LoggerFactory.getLogger(DataViewsSrcMappingsResource.class);

    private static final String ENTITY_NAME = "dataViewsSrcMappings";
        
    private final DataViewsSrcMappingsRepository dataViewsSrcMappingsRepository;

    public DataViewsSrcMappingsResource(DataViewsSrcMappingsRepository dataViewsSrcMappingsRepository) {
        this.dataViewsSrcMappingsRepository = dataViewsSrcMappingsRepository;
    }

    /**
     * POST  /data-views-src-mappings : Create a new dataViewsSrcMappings.
     *
     * @param dataViewsSrcMappings the dataViewsSrcMappings to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dataViewsSrcMappings, or with status 400 (Bad Request) if the dataViewsSrcMappings has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/data-views-src-mappings")
    @Timed
    public ResponseEntity<DataViewsSrcMappings> createDataViewsSrcMappings(@RequestBody DataViewsSrcMappings dataViewsSrcMappings) throws URISyntaxException {
        log.debug("REST request to save DataViewsSrcMappings : {}", dataViewsSrcMappings);
        if (dataViewsSrcMappings.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new dataViewsSrcMappings cannot already have an ID")).body(null);
        }
        DataViewsSrcMappings result = dataViewsSrcMappingsRepository.save(dataViewsSrcMappings);
        return ResponseEntity.created(new URI("/api/data-views-src-mappings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /data-views-src-mappings : Updates an existing dataViewsSrcMappings.
     *
     * @param dataViewsSrcMappings the dataViewsSrcMappings to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated dataViewsSrcMappings,
     * or with status 400 (Bad Request) if the dataViewsSrcMappings is not valid,
     * or with status 500 (Internal Server Error) if the dataViewsSrcMappings couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/data-views-src-mappings")
    @Timed
    public ResponseEntity<DataViewsSrcMappings> updateDataViewsSrcMappings(@RequestBody DataViewsSrcMappings dataViewsSrcMappings) throws URISyntaxException {
        log.debug("REST request to update DataViewsSrcMappings : {}", dataViewsSrcMappings);
        if (dataViewsSrcMappings.getId() == null) {
            return createDataViewsSrcMappings(dataViewsSrcMappings);
        }
        DataViewsSrcMappings result = dataViewsSrcMappingsRepository.save(dataViewsSrcMappings);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dataViewsSrcMappings.getId().toString()))
            .body(result);
    }

    /**
     * GET  /data-views-src-mappings : get all the dataViewsSrcMappings.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of dataViewsSrcMappings in body
     */
    @GetMapping("/data-views-src-mappings")
    @Timed
    public ResponseEntity<List<DataViewsSrcMappings>> getAllDataViewsSrcMappings(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of DataViewsSrcMappings");
        Page<DataViewsSrcMappings> page = dataViewsSrcMappingsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/data-views-src-mappings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /data-views-src-mappings/:id : get the "id" dataViewsSrcMappings.
     *
     * @param id the id of the dataViewsSrcMappings to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dataViewsSrcMappings, or with status 404 (Not Found)
     */
    @GetMapping("/data-views-src-mappings/{id}")
    @Timed
    public ResponseEntity<DataViewsSrcMappings> getDataViewsSrcMappings(@PathVariable Long id) {
        log.debug("REST request to get DataViewsSrcMappings : {}", id);
        DataViewsSrcMappings dataViewsSrcMappings = dataViewsSrcMappingsRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(dataViewsSrcMappings));
    }

    /**
     * DELETE  /data-views-src-mappings/:id : delete the "id" dataViewsSrcMappings.
     *
     * @param id the id of the dataViewsSrcMappings to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/data-views-src-mappings/{id}")
    @Timed
    public ResponseEntity<Void> deleteDataViewsSrcMappings(@PathVariable Long id) {
        log.debug("REST request to delete DataViewsSrcMappings : {}", id);
        dataViewsSrcMappingsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
