package org.hulei.elasticsearch.controller;

import lombok.RequiredArgsConstructor;
import org.hulei.elasticsearch.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
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



}
