package com.nspl.app.service;

import com.nspl.app.domain.AccountingEvents;
import com.nspl.app.repository.AccountingEventsRepository;
import com.nspl.app.repository.search.AccountingEventsSearchRepository;
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
 * Service Implementation for managing AccountingEvents.
 */
@Service
@Transactional
public class AccountingEventsService {

    private final Logger log = LoggerFactory.getLogger(AccountingEventsService.class);
    
    private final AccountingEventsRepository accountingEventsRepository;

    private final AccountingEventsSearchRepository accountingEventsSearchRepository;

    public AccountingEventsService(AccountingEventsRepository accountingEventsRepository, AccountingEventsSearchRepository accountingEventsSearchRepository) {
        this.accountingEventsRepository = accountingEventsRepository;
        this.accountingEventsSearchRepository = accountingEventsSearchRepository;
    }

    /**
     * Save a accountingEvents.
     *
     * @param accountingEvents the entity to save
     * @return the persisted entity
     */
    public AccountingEvents save(AccountingEvents accountingEvents) {
        log.debug("Request to save AccountingEvents : {}", accountingEvents);
        AccountingEvents result = accountingEventsRepository.save(accountingEvents);
        accountingEventsSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the accountingEvents.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<AccountingEvents> findAll(Pageable pageable) {
        log.debug("Request to get all AccountingEvents");
        Page<AccountingEvents> result = accountingEventsRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one accountingEvents by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public AccountingEvents findOne(Long id) {
        log.debug("Request to get AccountingEvents : {}", id);
        AccountingEvents accountingEvents = accountingEventsRepository.findOne(id);
        return accountingEvents;
    }

    /**
     *  Delete the  accountingEvents by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete AccountingEvents : {}", id);
        accountingEventsRepository.delete(id);
        accountingEventsSearchRepository.delete(id);
    }

    /**
     * Search for the accountingEvents corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<AccountingEvents> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AccountingEvents for query {}", query);
        Page<AccountingEvents> result = accountingEventsSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
