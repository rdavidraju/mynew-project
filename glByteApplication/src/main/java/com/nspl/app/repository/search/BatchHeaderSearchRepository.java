package com.nspl.app.repository.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.nspl.app.domain.BatchHeader;

public interface BatchHeaderSearchRepository extends ElasticsearchRepository<BatchHeader, Long> {

}
