package com.nspl.app.repository.search;

import com.nspl.app.domain.ReconciliationResult;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ReconciliationResult entity.
 */
public interface ReconciliationResultSearchRepository extends ElasticsearchRepository<ReconciliationResult, Long> {
}
