package com.nspl.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.domain.BalanceType;

import com.nspl.app.repository.BalanceTypeRepository;
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
 * REST controller for managing BalanceType.
 */
@RestController
@RequestMapping("/api")
public class BalanceTypeResource {

    private final Logger log = LoggerFactory.getLogger(BalanceTypeResource.class);

    private static final String ENTITY_NAME = "balanceType";

    private final BalanceTypeRepository balanceTypeRepository;

    public BalanceTypeResource(BalanceTypeRepository balanceTypeRepository) {
        this.balanceTypeRepository = balanceTypeRepository;
    }

    /**
     * POST  /balance-types : Create a new balanceType.
     *
     * @param balanceType the balanceType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new balanceType, or with status 400 (Bad Request) if the balanceType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/balance-types")
    @Timed
    public ResponseEntity<BalanceType> createBalanceType(@RequestBody BalanceType balanceType) throws URISyntaxException {
        log.debug("REST request to save BalanceType : {}", balanceType);
        if (balanceType.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new balanceType cannot already have an ID")).body(null);
        }
        BalanceType result = balanceTypeRepository.save(balanceType);
        return ResponseEntity.created(new URI("/api/balance-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /balance-types : Updates an existing balanceType.
     *
     * @param balanceType the balanceType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated balanceType,
     * or with status 400 (Bad Request) if the balanceType is not valid,
     * or with status 500 (Internal Server Error) if the balanceType couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/balance-types")
    @Timed
    public ResponseEntity<BalanceType> updateBalanceType(@RequestBody BalanceType balanceType) throws URISyntaxException {
        log.debug("REST request to update BalanceType : {}", balanceType);
        if (balanceType.getId() == null) {
            return createBalanceType(balanceType);
        }
        BalanceType result = balanceTypeRepository.save(balanceType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, balanceType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /balance-types : get all the balanceTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of balanceTypes in body
     */
    @GetMapping("/balance-types")
    @Timed
    public ResponseEntity<List<BalanceType>> getAllBalanceTypes(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of BalanceTypes");
        Page<BalanceType> page = balanceTypeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/balance-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /balance-types/:id : get the "id" balanceType.
     *
     * @param id the id of the balanceType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the balanceType, or with status 404 (Not Found)
     */
    @GetMapping("/balance-types/{id}")
    @Timed
    public ResponseEntity<BalanceType> getBalanceType(@PathVariable Long id) {
        log.debug("REST request to get BalanceType : {}", id);
        BalanceType balanceType = balanceTypeRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(balanceType));
    }

    /**
     * DELETE  /balance-types/:id : delete the "id" balanceType.
     *
     * @param id the id of the balanceType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/balance-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteBalanceType(@PathVariable Long id) {
        log.debug("REST request to delete BalanceType : {}", id);
        balanceTypeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
