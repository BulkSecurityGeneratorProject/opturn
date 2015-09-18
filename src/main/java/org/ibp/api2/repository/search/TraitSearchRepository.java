package org.ibp.api2.repository.search;

import org.ibp.api2.domain.Trait;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Trait entity.
 */
public interface TraitSearchRepository extends ElasticsearchRepository<Trait, Long> {
}
