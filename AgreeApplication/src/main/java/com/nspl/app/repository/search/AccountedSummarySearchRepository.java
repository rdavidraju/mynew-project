package com.nspl.app.repository.search;

import com.nspl.app.domain.AccountedSummary;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the AccountedSummary entity.
 */
public interface AccountedSummarySearchRepository extends ElasticsearchRepository<AccountedSummary, Long> {
}
