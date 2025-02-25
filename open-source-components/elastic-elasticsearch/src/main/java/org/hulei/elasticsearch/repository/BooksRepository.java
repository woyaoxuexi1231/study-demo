package org.hulei.elasticsearch.repository;

import org.hulei.elasticsearch.entity.Book;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author hulei
 * @since 2025/1/16 16:43
 */

public interface BooksRepository extends ElasticsearchRepository<Book, Long> {
}
