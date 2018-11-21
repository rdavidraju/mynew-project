package com.nspl.app.repository.search;

import com.nspl.app.domain.DataChild;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the DataChild entity.
 */
public interface DataChildSearchRepository extends ElasticsearchRepository<DataChild, Long> {
}
