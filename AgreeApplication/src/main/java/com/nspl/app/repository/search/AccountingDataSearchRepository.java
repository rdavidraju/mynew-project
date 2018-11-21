package com.nspl.app.repository.search;

import com.nspl.app.domain.AccountingData;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the AccountingData entity.
 */
public interface AccountingDataSearchRepository extends ElasticsearchRepository<AccountingData, Long> {
}
