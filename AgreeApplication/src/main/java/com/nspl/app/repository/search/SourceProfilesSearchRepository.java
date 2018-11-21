package com.nspl.app.repository.search;

import com.nspl.app.domain.SourceProfiles;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SourceProfiles entity.
 */
public interface SourceProfilesSearchRepository extends ElasticsearchRepository<SourceProfiles, Long> {

	List<SourceProfiles> findByTenantId(Long tenantId);
}
