package com.nspl.app.repository.search;

import com.nspl.app.domain.SourceConnectionDetails;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SourceConnectionDetails entity.
 */
public interface SourceConnectionDetailsSearchRepository extends ElasticsearchRepository<SourceConnectionDetails, Long> {
}
