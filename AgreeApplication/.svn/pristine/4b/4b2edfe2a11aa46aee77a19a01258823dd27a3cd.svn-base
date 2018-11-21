package com.nspl.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nspl.app.domain.AccountingLineTypes;

import com.nspl.app.repository.AccountingLineTypesRepository;
import com.nspl.app.repository.search.AccountingLineTypesSearchRepository;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing AccountingLineTypes.
 */
@RestController
@RequestMapping("/api")
public class AccountingLineTypesResource {

    private final Logger log = LoggerFactory.getLogger(AccountingLineTypesResource.class);

    private static final String ENTITY_NAME = "accountingLineTypes";
        
    private final AccountingLineTypesRepository accountingLineTypesRepository;

    private final AccountingLineTypesSearchRepository accountingLineTypesSearchRepository;

    public AccountingLineTypesResource(AccountingLineTypesRepository accountingLineTypesRepository, AccountingLineTypesSearchRepository accountingLineTypesSearchRepository) {
        this.accountingLineTypesRepository = accountingLineTypesRepository;
        this.accountingLineTypesSearchRepository = accountingLineTypesSearchRepository;
    }

    /**
     * POST  /accounting-line-types : Create a new accountingLineTypes.
     *
     * @param accountingLineTypes the accountingLineTypes to create
     * @return the ResponseEntity with status 201 (Created) and with body the new accountingLineTypes, or with status 400 (Bad Request) if the accountingLineTypes has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/accounting-line-types")
    @Timed
    public ResponseEntity<AccountingLineTypes> createAccountingLineTypes(@RequestBody AccountingLineTypes accountingLineTypes) throws URISyntaxException {
        log.debug("REST request to save AccountingLineTypes : {}", accountingLineTypes);
        if (accountingLineTypes.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new accountingLineTypes cannot already have an ID")).body(null);
        }
        AccountingLineTypes result = accountingLineTypesRepository.save(accountingLineTypes);
        accountingLineTypesSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/accounting-line-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /accounting-line-types : Updates an existing accountingLineTypes.
     *
     * @param accountingLineTypes the accountingLineTypes to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated accountingLineTypes,
     * or with status 400 (Bad Request) if the accountingLineTypes is not valid,
     * or with status 500 (Internal Server Error) if the accountingLineTypes couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/accounting-line-types")
    @Timed
    public ResponseEntity<AccountingLineTypes> updateAccountingLineTypes(@RequestBody AccountingLineTypes accountingLineTypes) throws URISyntaxException {
        log.debug("REST request to update AccountingLineTypes : {}", accountingLineTypes);
        if (accountingLineTypes.getId() == null) {
            return createAccountingLineTypes(accountingLineTypes);
        }
        AccountingLineTypes result = accountingLineTypesRepository.save(accountingLineTypes);
        accountingLineTypesSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, accountingLineTypes.getId().toString()))
            .body(result);
    }

    /**
     * GET  /accounting-line-types : get all the accountingLineTypes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of accountingLineTypes in body
     */
    @GetMapping("/accounting-line-types")
    @Timed
    public List<AccountingLineTypes> getAllAccountingLineTypes() {
        log.debug("REST request to get all AccountingLineTypes");
        List<AccountingLineTypes> accountingLineTypes = accountingLineTypesRepository.findAll();
        return accountingLineTypes;
    }

    /**
     * GET  /accounting-line-types/:id : get the "id" accountingLineTypes.
     *
     * @param id the id of the accountingLineTypes to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the accountingLineTypes, or with status 404 (Not Found)
     */
    @GetMapping("/accounting-line-types/{id}")
    @Timed
    public ResponseEntity<AccountingLineTypes> getAccountingLineTypes(@PathVariable Long id) {
        log.debug("REST request to get AccountingLineTypes : {}", id);
        AccountingLineTypes accountingLineTypes = accountingLineTypesRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(accountingLineTypes));
    }

    /**
     * DELETE  /accounting-line-types/:id : delete the "id" accountingLineTypes.
     *
     * @param id the id of the accountingLineTypes to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/accounting-line-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteAccountingLineTypes(@PathVariable Long id) {
        log.debug("REST request to delete AccountingLineTypes : {}", id);
        accountingLineTypesRepository.delete(id);
        accountingLineTypesSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/accounting-line-types?query=:query : search for the accountingLineTypes corresponding
     * to the query.
     *
     * @param query the query of the accountingLineTypes search 
     * @return the result of the search
     */
    @GetMapping("/_search/accounting-line-types")
    @Timed
    public List<AccountingLineTypes> searchAccountingLineTypes(@RequestParam String query) {
        log.debug("REST request to search AccountingLineTypes for query {}", query);
        return StreamSupport
            .stream(accountingLineTypesSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
