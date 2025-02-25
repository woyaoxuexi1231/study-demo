package org.hulei.elasticsearch.repository;

import org.hulei.elasticsearch.entity.Book;
import org.hulei.elasticsearch.entity.MyEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author hulei
 * @since 2025/1/16 17:26
 */

public interface MyEntityRepository extends ElasticsearchRepository<MyEntity, String> {
}
