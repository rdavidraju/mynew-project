package com.nspl.app.repository.search;

import com.nspl.app.domain.LookUpType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the LookUpType entity.
 */
public interface LookUpTypeSearchRepository extends ElasticsearchRepository<LookUpType, Long> {
}
