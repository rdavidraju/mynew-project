package com.nspl.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.domain.DataViewFilters;

import com.nspl.app.repository.DataViewFiltersRepository;
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
 * REST controller for managing DataViewFilters.
 */
@RestController
@RequestMapping("/api")
public class DataViewFiltersResource {

    private final Logger log = LoggerFactory.getLogger(DataViewFiltersResource.class);

    private static final String ENTITY_NAME = "dataViewFilters";

    private final DataViewFiltersRepository dataViewFiltersRepository;

    public DataViewFiltersResource(DataViewFiltersRepository dataViewFiltersRepository) {
        this.dataViewFiltersRepository = dataViewFiltersRepository;
    }

    /**
     * POST  /data-view-filters : Create a new dataViewFilters.
     *
     * @param dataViewFilters the dataViewFilters to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dataViewFilters, or with status 400 (Bad Request) if the dataViewFilters has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/data-view-filters")
    @Timed
    public ResponseEntity<DataViewFilters> createDataViewFilters(@RequestBody DataViewFilters dataViewFilters) throws URISyntaxException {
        log.debug("REST request to save DataViewFilters : {}", dataViewFilters);
        if (dataViewFilters.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new dataViewFilters cannot already have an ID")).body(null);
        }
        DataViewFilters result = dataViewFiltersRepository.save(dataViewFilters);
        return ResponseEntity.created(new URI("/api/data-view-filters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /data-view-filters : Updates an existing dataViewFilters.
     *
     * @param dataViewFilters the dataViewFilters to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated dataViewFilters,
     * or with status 400 (Bad Request) if the dataViewFilters is not valid,
     * or with status 500 (Internal Server Error) if the dataViewFilters couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/data-view-filters")
    @Timed
    public ResponseEntity<DataViewFilters> updateDataViewFilters(@RequestBody DataViewFilters dataViewFilters) throws URISyntaxException {
        log.debug("REST request to update DataViewFilters : {}", dataViewFilters);
        if (dataViewFilters.getId() == null) {
            return createDataViewFilters(dataViewFilters);
        }
        DataViewFilters result = dataViewFiltersRepository.save(dataViewFilters);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dataViewFilters.getId().toString()))
            .body(result);
    }

    /**
     * GET  /data-view-filters : get all the dataViewFilters.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of dataViewFilters in body
     */
    @GetMapping("/data-view-filters")
    @Timed
    public List<DataViewFilters> getAllDataViewFilters() {
        log.debug("REST request to get all DataViewFilters");
        return dataViewFiltersRepository.findAll();
    }

    /**
     * GET  /data-view-filters/:id : get the "id" dataViewFilters.
     *
     * @param id the id of the dataViewFilters to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dataViewFilters, or with status 404 (Not Found)
     */
    @GetMapping("/data-view-filters/{id}")
    @Timed
    public ResponseEntity<DataViewFilters> getDataViewFilters(@PathVariable Long id) {
        log.debug("REST request to get DataViewFilters : {}", id);
        DataViewFilters dataViewFilters = dataViewFiltersRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(dataViewFilters));
    }

    /**
     * DELETE  /data-view-filters/:id : delete the "id" dataViewFilters.
     *
     * @param id the id of the dataViewFilters to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/data-view-filters/{id}")
    @Timed
    public ResponseEntity<Void> deleteDataViewFilters(@PathVariable Long id) {
        log.debug("REST request to delete DataViewFilters : {}", id);
        dataViewFiltersRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
