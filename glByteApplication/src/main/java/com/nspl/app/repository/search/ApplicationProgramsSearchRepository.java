package com.nspl.app.repository.search;

import com.nspl.app.domain.ApplicationPrograms;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ApplicationPrograms entity.
 */
public interface ApplicationProgramsSearchRepository extends ElasticsearchRepository<ApplicationPrograms, Long> {
}
