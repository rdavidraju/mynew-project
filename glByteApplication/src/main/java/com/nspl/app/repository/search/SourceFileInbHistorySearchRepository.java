package com.nspl.app.repository.search;

import com.nspl.app.domain.SourceFileInbHistory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SourceFileInbHistory entity.
 */
public interface SourceFileInbHistorySearchRepository extends ElasticsearchRepository<SourceFileInbHistory, Long> {
}
