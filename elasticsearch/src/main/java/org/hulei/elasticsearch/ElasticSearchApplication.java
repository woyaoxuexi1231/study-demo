package org.hulei.elasticsearch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;

/**
 * @author hulei
 * @since 2024/10/29 18:02
 */

@SpringBootApplication
public class ElasticSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElasticSearchApplication.class, args);
    }

    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;


}
