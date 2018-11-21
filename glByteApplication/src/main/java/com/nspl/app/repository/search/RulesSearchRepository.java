package com.nspl.app.repository.search;

import com.nspl.app.domain.Rules;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Rules entity.
 */
public interface RulesSearchRepository extends ElasticsearchRepository<Rules, Long> {
}
