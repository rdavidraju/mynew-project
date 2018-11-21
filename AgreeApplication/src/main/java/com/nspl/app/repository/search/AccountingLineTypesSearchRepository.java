package com.nspl.app.repository.search;

import com.nspl.app.domain.AccountingLineTypes;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the AccountingLineTypes entity.
 */
public interface AccountingLineTypesSearchRepository extends ElasticsearchRepository<AccountingLineTypes, Long> {
}
