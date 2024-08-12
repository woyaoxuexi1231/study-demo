package com.hundsun.demo.spring;

import cn.hutool.core.collection.CollectionUtil;
import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.mapping.IntegerNumberProperty;
import co.elastic.clients.elasticsearch._types.mapping.KeywordProperty;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch._types.mapping.TextProperty;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexRequest;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import co.elastic.clients.elasticsearch.indices.GetIndexResponse;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import co.elastic.clients.util.NamedValue;
import com.hundsun.demo.spring.es.Product;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author hulei
 * @since 2024/8/2 16:41
 */

@SpringBootTest
@Slf4j
public class ElasticsearchClientMain {
    //
    private static ElasticsearchClient client;

    private static ElasticsearchAsyncClient asyncClient;


    @Test
    public synchronized void test() throws Exception {
        makeConnection();
        // documentQuery();
        // documentMatchAll();
        // documentMatch();
        // documentTermAndRange();
        // documentBool();
        // documentAgg();
        documentMetrics();
    }

    private static synchronized void makeConnection() {
        // 这里创建了一个BasicCredentialsProvider对象，用于管理认证凭据。
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        // 这里设置了认证范围为AuthScope.ANY，并且使用用户名"elastic"和密码"qNQwXe9TV1gLFRImusVi"来设置认证凭据。
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("elastic", "qNQwXe9TV1gLFRImusVi"));

        // 指定了Elasticsearch的主机地址为"192.168.80.128"，端口为9200，使用HTTPS协议。
        // 通过setHttpClientConfigCallback方法设置了HTTP客户端的配置回调，用于设置默认的认证凭据提供者为之前创建的credentialsProvider。
        RestClientBuilder builder = RestClient.builder(new HttpHost("192.168.80.128", 9200, "https")).setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));

        RestClient restClient = builder.build();

        // Create the transport with a Jackson mapper
        // 使用了RestClientTransport作为实现，传入了之前创建的restClient和一个JacksonJsonpMapper对象，用于处理JSON的映射。
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());

        // And create the API client
        client = new ElasticsearchClient(transport);
        asyncClient = new ElasticsearchAsyncClient(transport);
    }

    private static synchronized void indexCreate() throws IOException {

        if (indexIsExist()) {
            indexDelete();
        }

        Map<String, Property> documentMap = new HashMap<>();
        documentMap.put("id", Property.of(property -> property.keyword(KeywordProperty.of(keyword ->
                // textProperty.index(true).analyzer("ik_max_word"))//这里设置分词
                keyword.index(true)))));
        documentMap.put("name", Property.of(property -> property.text(TextProperty.of(text -> text.analyzer("ik_max_word").index(true)))));
        documentMap.put("price", Property.of(property -> property.integer(IntegerNumberProperty.of(integerNumberProperty -> integerNumberProperty.index(false)))));

        // CreateIndexResponse createIndexResponse = client.indices().create(createIndexBuilder ->
        //         createIndexBuilder.index("products").mappings(mappings ->
        //                         mappings.properties(documentMap))
        //                 .aliases("Products", aliases ->
        //                         aliases.isWriteIndex(true))
        // );


        CreateIndexResponse createIndexResponse = client.indices().create(c -> c.index("products").mappings(mappings -> mappings.properties(documentMap)));
        boolean acknowledged = createIndexResponse.acknowledged();
        System.out.println("acknowledged = " + acknowledged);
    }

    private static synchronized void indexDelete() throws IOException {
        DeleteIndexResponse deleteIndexResponse = client.indices().delete(new DeleteIndexRequest.Builder().index("products").build());
        log.info("acknowledged = {}", deleteIndexResponse.acknowledged());
    }

    private static synchronized boolean indexIsExist() throws IOException {
        BooleanResponse booleanResponse = client.indices().exists(req -> req.index("products"));
        return booleanResponse.value();
    }

    private static synchronized void indexQuery() throws IOException {
        GetIndexResponse getIndexResponse = client.indices().get(index -> index.index("products"));
        log.info("getIndexResponse = {}", getIndexResponse.toString());
    }

    private static synchronized void documentCreate() throws IOException {
        // Index data to an index products
        Product product = new Product("1", "耐克背包", 42);

        IndexRequest<Object> indexRequest = new IndexRequest.Builder<>().index("products") // 设置索引
                .id(product.getId()) // 文档id为abc
                .document(product) // 文档内容为 product
                .build();

        // 创建倒排索引
        client.index(indexRequest);

        Product product1 = new Product("2", "李宁T恤", 44);

        // 类似操作再新建一个文档内容
        client.index(builder -> builder.index("products").id(product1.getId()).document(product1));

        // 相当于
        // PUT products/_doc/abc
        // {
        //   "id": "abc",
        //   "name": "Bag",
        //   "price": 42
        // }
    }


    public synchronized void documentQuery() throws IOException {
        GetResponse<Product> products = client.get(req -> req.index("products").id("1"), Product.class);
        log.info("products = {}", products.source());
    }

    public static synchronized void documentUpdate() throws IOException {
        Product product = new Product("1", String.format("%s%d", "耐克背包", System.currentTimeMillis()), 44);
        client.update(req -> req.index("products").id("1").doc(product), Product.class);
    }

    /**
     * 查询指定索引的所有文档
     *
     * @throws IOException
     */
    public static synchronized void documentMatchAll() throws IOException {
        SearchResponse<Product> products = client.search(req -> req.index("products").query(q -> q.matchAll(mq -> mq)), Product.class);
        // 或者
        // SearchResponse<Product> products = client.search(req -> req.index("products"), Product.class);
        products.hits().hits().forEach(i -> log.info(Objects.requireNonNull(i.source()).toString()));
        /*
        get /products/_search
        {
          "query": {
            "match_all": {}
          }
        }
         */
    }

    /**
     * 查询指定索引包含指定关键字的文档
     *
     * @throws IOException
     */
    private static synchronized void documentMatch() throws IOException {
        // Search for a data
        SearchRequest request = new SearchRequest.Builder()
                .index("products")
                .query((t) -> t.match(
                                mq -> mq.field("name").query("耐克")
                        )
                )
                .build();
        SearchResponse<Product> search = client.search(request, Product.class);

        for (Hit<Product> hit : search.hits().hits()) {
            Product pd = hit.source();
            log.info("pd = {}", pd);
        }
    }

    /**
     * 查询指定索引 精准匹配关键字的文档
     *
     * @throws IOException
     */
    private static synchronized void documentTermAndRange() throws IOException {
        // Splitting complex DSL
        // TermQuery termQuery = TermQuery.of(t -> t.field("name").value("bag"));

        SearchResponse<Product> termQueryRsp = client.search(s -> s.index("products").query(
                q -> q.term(tq -> tq.field("id").value("2"))
        ), Product.class);

        for (Hit<Product> hit : termQueryRsp.hits().hits()) {
            Product pd = hit.source();
            System.out.println(pd);
        }

        // GET /products/_search
        // {
        //   "query": {
        //     "term": {
        //       "id": {
        //         "value": "2"
        //       }
        //     }
        //   }
        // }

        SearchResponse<Product> rangeQueryRsp = client.search(s -> s.index("products").query(
                q -> q.range(rq -> rq.field("price").gte(JsonData.of(10)).lte(JsonData.of(43)))
        ), Product.class);
        for (Hit<Product> hit : rangeQueryRsp.hits().hits()) {
            Product pd = hit.source();
            System.out.println(pd);
        }

        // GET /products/_search
        // {
        //   "query": {
        //     "range": {
        //       "price": {
        //         "gte": 10,
        //         "lte": 43
        //       }
        //     }
        //   }
        // }
    }

    /**
     * 复合查询
     *
     * @throws IOException
     */
    public static synchronized void documentBool() throws IOException {
        SearchResponse<Product> searchResponse = client.search(s -> s.index("products").query(
                        b -> b.bool(
                                bq -> bq
                                        .must(mq -> mq.match(mq1 -> mq1.field("name").query("耐克")))
                                        .filter(fq -> fq.range(rq -> rq.field("price").gte(JsonData.of(10)).lte(JsonData.of(44))))
                        )).from(0).size(10)
                .highlight(hl -> hl.fields("name", hlf -> hlf)), Product.class);
        for (Hit<Product> hit : searchResponse.hits().hits()) {
            Product pd = hit.source();
            System.out.println(pd);
            System.out.println(hit.highlight());
        }
    }

    /**
     * 聚合查询
     *
     * @throws IOException
     */
    public static synchronized void documentAgg() throws IOException {
        SearchResponse<Void> searchResponse = client.search(s -> s.index("products")
                        .size(0)
                        .query(ob -> ob.matchAll(ma -> ma))
                        .aggregations("priceag", ag -> ag.terms(ta -> ta.field("price")))
                , Void.class);
        System.out.println(searchResponse.aggregations().get("priceag"));
        // GET /products/_search
        // {
        //  "size": 2,
        //  "aggs": {
        //    "priceAgg": {
        //      "terms": {
        //        "field": "price",
        //        "size": 3
        //      }
        //    }
        //  }
        // }
    }

    /**
     * 复合聚合,计算总和
     *
     * @throws IOException
     */
    public static synchronized void documentMetrics() throws IOException {
        SearchResponse<Void> searchResponse = client.search(s -> s.index("customers")
                        .size(0)
                        .query(ob -> ob.matchAll(ma -> ma))
                        .aggregations("cityagg", ag -> ag
                                .terms(ta -> ta.field("city")
                                        .size(20)
                                        .order(CollectionUtil.newArrayList(new NamedValue<>("limitagg.sum", SortOrder.Desc))))
                                .aggregations("limitagg", ag1 -> ag1.stats(sag -> sag.field("creditlimit"))))
                , Void.class);
        System.out.println(searchResponse.aggregations().get("cityagg"));

        // GET /customers/_search
        // {
        //   "size": 0,
        //   "aggs": {
        //     "cityagg": { 
        //       "terms": {
        //         "field": "city",
        //         "size": 20,
        //         "order": {
        //           "limitagg.sum": "desc"
        //         }
        //       },
        //       "aggs": {
        //         "limitagg": {
        //           "stats": {
        //             "field": "creditlimit"
        //           }
        //         }
        //       }
        //     }
        //   }
        // }
    }

}
