package com.nspl.app.repository.search;

import com.nspl.app.domain.SchedulerDetails;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SchedulerDetails entity.
 */
public interface SchedulerDetailsSearchRepository extends ElasticsearchRepository<SchedulerDetails, Long> {
}
