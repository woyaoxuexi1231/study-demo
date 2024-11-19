package org.hulei.elasticsearch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hulei
 * @since 2024/10/29 18:02
 */

@RestController
@SpringBootApplication
public class ElasticSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElasticSearchApplication.class, args);
    }

}
