package com.nspl.app.repository.search;

import com.nspl.app.domain.JobActions;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the JobActions entity.
 */
public interface JobActionsSearchRepository extends ElasticsearchRepository<JobActions, Long> {
}
