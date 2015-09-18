package org.ibp.api2.repository.search;

import org.ibp.api2.domain.Study;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Study entity.
 */
public interface StudySearchRepository extends ElasticsearchRepository<Study, Long> {
}
