package com.nspl.app.repository.search;

import com.nspl.app.domain.DataMaster;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the DataMaster entity.
 */
public interface DataMasterSearchRepository extends ElasticsearchRepository<DataMaster, Long> {
}
