package com.nspl.app.repository.search;

import com.nspl.app.domain.ReportingDashboardUsecases;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ReportingDashboardUsecases entity.
 */
public interface ReportingDashboardUsecasesSearchRepository extends ElasticsearchRepository<ReportingDashboardUsecases, Long> {
}
