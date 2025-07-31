package org.hulei.elasticsearch.controller;

import lombok.RequiredArgsConstructor;
import org.hulei.elasticsearch.entity.Book;
import org.hulei.elasticsearch.repository.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
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

    // @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    ElasticsearchOperations operations;
    @Autowired
    BooksRepository repository;

    @GetMapping("/createIndex")
    public void createIndex() {
        IndexOperations indexOps = elasticsearchRestTemplate.indexOps(Book.class);
        indexOps.create();
    }

    @GetMapping("/deleteIndex")
    public void deleteIndex() {
        IndexOperations indexOps = elasticsearchRestTemplate.indexOps(Book.class);
        indexOps.delete();
    }

    @GetMapping("/createDocument")
    public void createDocument() {
        elasticsearchRestTemplate.save(new Book().setAuthor("hulei").setId(1L).setTitle("Hulei Book"));
        elasticsearchRestTemplate.save(new Book().setAuthor("hulei").setId(2L).setTitle("JAVA"));
        elasticsearchRestTemplate.save(new Book().setAuthor("hulei").setId(3L).setTitle("Spring Boot"));
    }

    public void deleteDocument() {
        elasticsearchRestTemplate.delete(Book.class);
    }

}
