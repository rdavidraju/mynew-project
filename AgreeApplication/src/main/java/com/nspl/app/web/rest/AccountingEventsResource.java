package com.nspl.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.domain.AccountingEvents;
import com.nspl.app.service.AccountingEventsService;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing AccountingEvents.
 */
@RestController
@RequestMapping("/api")
public class AccountingEventsResource {

    private final Logger log = LoggerFactory.getLogger(AccountingEventsResource.class);

    private static final String ENTITY_NAME = "accountingEvents";
        
    private final AccountingEventsService accountingEventsService;

    public AccountingEventsResource(AccountingEventsService accountingEventsService) {
        this.accountingEventsService = accountingEventsService;
    }

    /**
     * POST  /accounting-events : Create a new accountingEvents.
     *
     * @param accountingEvents the accountingEvents to create
     * @return the ResponseEntity with status 201 (Created) and with body the new accountingEvents, or with status 400 (Bad Request) if the accountingEvents has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/accounting-events")
    @Timed
    public ResponseEntity<AccountingEvents> createAccountingEvents(@RequestBody AccountingEvents accountingEvents) throws URISyntaxException {
        log.debug("REST request to save AccountingEvents : {}", accountingEvents);
        if (accountingEvents.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new accountingEvents cannot already have an ID")).body(null);
        }
        AccountingEvents result = accountingEventsService.save(accountingEvents);
        return ResponseEntity.created(new URI("/api/accounting-events/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /accounting-events : Updates an existing accountingEvents.
     *
     * @param accountingEvents the accountingEvents to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated accountingEvents,
     * or with status 400 (Bad Request) if the accountingEvents is not valid,
     * or with status 500 (Internal Server Error) if the accountingEvents couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/accounting-events")
    @Timed
    public ResponseEntity<AccountingEvents> updateAccountingEvents(@RequestBody AccountingEvents accountingEvents) throws URISyntaxException {
        log.debug("REST request to update AccountingEvents : {}", accountingEvents);
        if (accountingEvents.getId() == null) {
            return createAccountingEvents(accountingEvents);
        }
        AccountingEvents result = accountingEventsService.save(accountingEvents);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, accountingEvents.getId().toString()))
            .body(result);
    }

    /**
     * GET  /accounting-events : get all the accountingEvents.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of accountingEvents in body
     */
    @GetMapping("/accounting-events")
    @Timed
    public ResponseEntity<List<AccountingEvents>> getAllAccountingEvents(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of AccountingEvents");
        Page<AccountingEvents> page = accountingEventsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/accounting-events");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /accounting-events/:id : get the "id" accountingEvents.
     *
     * @param id the id of the accountingEvents to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the accountingEvents, or with status 404 (Not Found)
     */
    @GetMapping("/accounting-events/{id}")
    @Timed
    public ResponseEntity<AccountingEvents> getAccountingEvents(@PathVariable Long id) {
        log.debug("REST request to get AccountingEvents : {}", id);
        AccountingEvents accountingEvents = accountingEventsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(accountingEvents));
    }

    /**
     * DELETE  /accounting-events/:id : delete the "id" accountingEvents.
     *
     * @param id the id of the accountingEvents to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/accounting-events/{id}")
    @Timed
    public ResponseEntity<Void> deleteAccountingEvents(@PathVariable Long id) {
        log.debug("REST request to delete AccountingEvents : {}", id);
        accountingEventsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/accounting-events?query=:query : search for the accountingEvents corresponding
     * to the query.
     *
     * @param query the query of the accountingEvents search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/accounting-events")
    @Timed
    public ResponseEntity<List<AccountingEvents>> searchAccountingEvents(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of AccountingEvents for query {}", query);
        Page<AccountingEvents> page = accountingEventsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/accounting-events");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
