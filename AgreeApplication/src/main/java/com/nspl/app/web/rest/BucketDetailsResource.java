package com.nspl.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.domain.BucketDetails;

import com.nspl.app.repository.BucketDetailsRepository;
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
 * REST controller for managing BucketDetails.
 */
@RestController
@RequestMapping("/api")
public class BucketDetailsResource {

    private final Logger log = LoggerFactory.getLogger(BucketDetailsResource.class);

    private static final String ENTITY_NAME = "bucketDetails";

    private final BucketDetailsRepository bucketDetailsRepository;

    public BucketDetailsResource(BucketDetailsRepository bucketDetailsRepository) {
        this.bucketDetailsRepository = bucketDetailsRepository;
    }

    /**
     * POST  /bucket-details : Create a new bucketDetails.
     *
     * @param bucketDetails the bucketDetails to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bucketDetails, or with status 400 (Bad Request) if the bucketDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/bucket-details")
    @Timed
    public ResponseEntity<BucketDetails> createBucketDetails(@RequestBody BucketDetails bucketDetails) throws URISyntaxException {
        log.debug("REST request to save BucketDetails : {}", bucketDetails);
        if (bucketDetails.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new bucketDetails cannot already have an ID")).body(null);
        }
        BucketDetails result = bucketDetailsRepository.save(bucketDetails);
        return ResponseEntity.created(new URI("/api/bucket-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bucket-details : Updates an existing bucketDetails.
     *
     * @param bucketDetails the bucketDetails to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bucketDetails,
     * or with status 400 (Bad Request) if the bucketDetails is not valid,
     * or with status 500 (Internal Server Error) if the bucketDetails couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bucket-details")
    @Timed
    public ResponseEntity<BucketDetails> updateBucketDetails(@RequestBody BucketDetails bucketDetails) throws URISyntaxException {
        log.debug("REST request to update BucketDetails : {}", bucketDetails);
        if (bucketDetails.getId() == null) {
            return createBucketDetails(bucketDetails);
        }
        BucketDetails result = bucketDetailsRepository.save(bucketDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, bucketDetails.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bucket-details : get all the bucketDetails.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of bucketDetails in body
     */
    @GetMapping("/bucket-details")
    @Timed
    public ResponseEntity<List<BucketDetails>> getAllBucketDetails(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of BucketDetails");
        Page<BucketDetails> page = bucketDetailsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bucket-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /bucket-details/:id : get the "id" bucketDetails.
     *
     * @param id the id of the bucketDetails to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bucketDetails, or with status 404 (Not Found)
     */
    @GetMapping("/bucket-details/{id}")
    @Timed
    public ResponseEntity<BucketDetails> getBucketDetails(@PathVariable Long id) {
        log.debug("REST request to get BucketDetails : {}", id);
        BucketDetails bucketDetails = bucketDetailsRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(bucketDetails));
    }

    /**
     * DELETE  /bucket-details/:id : delete the "id" bucketDetails.
     *
     * @param id the id of the bucketDetails to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/bucket-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteBucketDetails(@PathVariable Long id) {
        log.debug("REST request to delete BucketDetails : {}", id);
        bucketDetailsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
