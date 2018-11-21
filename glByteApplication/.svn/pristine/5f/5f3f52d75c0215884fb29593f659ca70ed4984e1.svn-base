package com.nspl.app.service;

import com.nspl.app.domain.JournalBatches;
import com.nspl.app.repository.JournalBatchesRepository;
import com.nspl.app.repository.search.JournalBatchesSearchRepository;
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
 * Service Implementation for managing JournalBatches.
 */
@Service
@Transactional
public class JournalBatchesService {

    private final Logger log = LoggerFactory.getLogger(JournalBatchesService.class);
    
    private final JournalBatchesRepository journalBatchesRepository;

    private final JournalBatchesSearchRepository journalBatchesSearchRepository;

    public JournalBatchesService(JournalBatchesRepository journalBatchesRepository, JournalBatchesSearchRepository journalBatchesSearchRepository) {
        this.journalBatchesRepository = journalBatchesRepository;
        this.journalBatchesSearchRepository = journalBatchesSearchRepository;
    }

    /**
     * Save a journalBatches.
     *
     * @param journalBatches the entity to save
     * @return the persisted entity
     */
    public JournalBatches save(JournalBatches journalBatches) {
        log.debug("Request to save JournalBatches : {}", journalBatches);
        JournalBatches result = journalBatchesRepository.save(journalBatches);
        journalBatchesSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the journalBatches.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<JournalBatches> findAll(Pageable pageable) {
        log.debug("Request to get all JournalBatches");
        Page<JournalBatches> result = journalBatchesRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one journalBatches by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public JournalBatches findOne(Long id) {
        log.debug("Request to get JournalBatches : {}", id);
        JournalBatches journalBatches = journalBatchesRepository.findOne(id);
        return journalBatches;
    }

    /**
     *  Delete the  journalBatches by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete JournalBatches : {}", id);
        journalBatchesRepository.delete(id);
        journalBatchesSearchRepository.delete(id);
    }

    /**
     * Search for the journalBatches corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<JournalBatches> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of JournalBatches for query {}", query);
        Page<JournalBatches> result = journalBatchesSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
