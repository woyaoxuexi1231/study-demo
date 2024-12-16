package org.hulei.elasticsearch.controller;

import lombok.RequiredArgsConstructor;
import org.hulei.elasticsearch.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hulei
 * @since 2024/11/18 18:23
 */

@RequiredArgsConstructor
@RequestMapping("/crud")
@RestController
public class CrudController {

    private final ElasticsearchTemplate elasticsearchTemplate;

    @GetMapping("/createIndex")
    public void createIndex() {
        IndexOperations indexOps = elasticsearchTemplate.indexOps(Book.class);
        indexOps.create();
    }

    @GetMapping("/deleteIndex")
    public void deleteIndex() {
        IndexOperations indexOps = elasticsearchTemplate.indexOps(Book.class);
        indexOps.delete();
    }

    @GetMapping("/createDocument")
    public void createDocument() {
        elasticsearchTemplate.save(new Book().setAuthor("hulei").setId(1L).setTitle("Hulei Book"));
        elasticsearchTemplate.save(new Book().setAuthor("hulei").setId(2L).setTitle("JAVA"));
        elasticsearchTemplate.save(new Book().setAuthor("hulei").setId(3L).setTitle("Spring Boot"));
    }

    public void deleteDocument() {
        elasticsearchTemplate.delete(Book.class);
    }

}
