package org.hulei.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.TransportOptions;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.hulei.elasticsearch.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * @author hulei
 * @since 2024/10/29 18:02
 */

@SpringBootApplication
public class ElasticSearchApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(ElasticSearchApplication.class, args);
    }

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        Book book = new Book();
        book.setId(UUID.randomUUID().toString());
        book.setTitle("Java Elasticsearch");
        book.setAuthor("Hulei");
        elasticsearchTemplate.save(book);
    }
}
