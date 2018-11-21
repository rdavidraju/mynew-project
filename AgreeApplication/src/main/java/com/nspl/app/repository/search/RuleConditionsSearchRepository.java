package com.nspl.app.repository.search;

import com.nspl.app.domain.RuleConditions;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the RuleConditions entity.
 */
public interface RuleConditionsSearchRepository extends ElasticsearchRepository<RuleConditions, Long> {
}
