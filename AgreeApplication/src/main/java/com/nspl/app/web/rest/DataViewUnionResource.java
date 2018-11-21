package com.nspl.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.domain.DataViewUnion;

import com.nspl.app.repository.DataViewUnionRepository;
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
 * REST controller for managing DataViewUnion.
 */
@RestController
@RequestMapping("/api")
public class DataViewUnionResource {

    private final Logger log = LoggerFactory.getLogger(DataViewUnionResource.class);

    private static final String ENTITY_NAME = "dataViewUnion";
        
    private final DataViewUnionRepository dataViewUnionRepository;

    public DataViewUnionResource(DataViewUnionRepository dataViewUnionRepository) {
        this.dataViewUnionRepository = dataViewUnionRepository;
    }

    /**
     * POST  /data-view-unions : Create a new dataViewUnion.
     *
     * @param dataViewUnion the dataViewUnion to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dataViewUnion, or with status 400 (Bad Request) if the dataViewUnion has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/data-view-unions")
    @Timed
    public ResponseEntity<DataViewUnion> createDataViewUnion(@RequestBody DataViewUnion dataViewUnion) throws URISyntaxException {
        log.debug("REST request to save DataViewUnion : {}", dataViewUnion);
        if (dataViewUnion.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new dataViewUnion cannot already have an ID")).body(null);
        }
        DataViewUnion result = dataViewUnionRepository.save(dataViewUnion);
        return ResponseEntity.created(new URI("/api/data-view-unions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /data-view-unions : Updates an existing dataViewUnion.
     *
     * @param dataViewUnion the dataViewUnion to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated dataViewUnion,
     * or with status 400 (Bad Request) if the dataViewUnion is not valid,
     * or with status 500 (Internal Server Error) if the dataViewUnion couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/data-view-unions")
    @Timed
    public ResponseEntity<DataViewUnion> updateDataViewUnion(@RequestBody DataViewUnion dataViewUnion) throws URISyntaxException {
        log.debug("REST request to update DataViewUnion : {}", dataViewUnion);
        if (dataViewUnion.getId() == null) {
            return createDataViewUnion(dataViewUnion);
        }
        DataViewUnion result = dataViewUnionRepository.save(dataViewUnion);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dataViewUnion.getId().toString()))
            .body(result);
    }

    /**
     * GET  /data-view-unions : get all the dataViewUnions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of dataViewUnions in body
     */
    @GetMapping("/data-view-unions")
    @Timed
    public ResponseEntity<List<DataViewUnion>> getAllDataViewUnions(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of DataViewUnions");
        Page<DataViewUnion> page = dataViewUnionRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/data-view-unions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /data-view-unions/:id : get the "id" dataViewUnion.
     *
     * @param id the id of the dataViewUnion to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dataViewUnion, or with status 404 (Not Found)
     */
    @GetMapping("/data-view-unions/{id}")
    @Timed
    public ResponseEntity<DataViewUnion> getDataViewUnion(@PathVariable Long id) {
        log.debug("REST request to get DataViewUnion : {}", id);
        DataViewUnion dataViewUnion = dataViewUnionRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(dataViewUnion));
    }

    /**
     * DELETE  /data-view-unions/:id : delete the "id" dataViewUnion.
     *
     * @param id the id of the dataViewUnion to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/data-view-unions/{id}")
    @Timed
    public ResponseEntity<Void> deleteDataViewUnion(@PathVariable Long id) {
        log.debug("REST request to delete DataViewUnion : {}", id);
        dataViewUnionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
