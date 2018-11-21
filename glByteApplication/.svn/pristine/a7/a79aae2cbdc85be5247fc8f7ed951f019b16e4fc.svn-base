package com.nspl.app.service;

import com.nspl.app.domain.AccountedSummary;
import com.nspl.app.repository.AccountedSummaryRepository;
import com.nspl.app.repository.search.AccountedSummarySearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing AccountedSummary.
 */
@Service
@Transactional
public class AccountedSummaryService {

    private final Logger log = LoggerFactory.getLogger(AccountedSummaryService.class);
    
    private final AccountedSummaryRepository accountedSummaryRepository;

    private final AccountedSummarySearchRepository accountedSummarySearchRepository;

    public AccountedSummaryService(AccountedSummaryRepository accountedSummaryRepository, AccountedSummarySearchRepository accountedSummarySearchRepository) {
        this.accountedSummaryRepository = accountedSummaryRepository;
        this.accountedSummarySearchRepository = accountedSummarySearchRepository;
    }

    /**
     * Save a accountedSummary.
     *
     * @param accountedSummary the entity to save
     * @return the persisted entity
     */
    public AccountedSummary save(AccountedSummary accountedSummary) {
        log.debug("Request to save AccountedSummary : {}", accountedSummary);
        AccountedSummary result = accountedSummaryRepository.save(accountedSummary);
        accountedSummarySearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the accountedSummaries.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<AccountedSummary> findAll(Pageable pageable) {
        log.debug("Request to get all AccountedSummaries");
        Page<AccountedSummary> result = accountedSummaryRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one accountedSummary by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public AccountedSummary findOne(Long id) {
        log.debug("Request to get AccountedSummary : {}", id);
        AccountedSummary accountedSummary = accountedSummaryRepository.findOne(id);
        return accountedSummary;
    }

    /**
     *  Delete the  accountedSummary by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete AccountedSummary : {}", id);
        accountedSummaryRepository.delete(id);
        accountedSummarySearchRepository.delete(id);
    }

    /**
     * Search for the accountedSummary corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<AccountedSummary> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AccountedSummaries for query {}", query);
        Page<AccountedSummary> result = accountedSummarySearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
