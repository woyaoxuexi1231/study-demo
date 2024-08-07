package com.hundsun.demo.spring.mybatis;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.github.pagehelper.PageHelper;
import com.hundsun.demo.commom.core.model.CustomerDO;
import com.hundsun.demo.spring.mybatis.mapper.CustomerMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.stat.descriptive.summary.Product;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.db.mybatis
 * @className: MybatisTest
 * @description:
 * @author: h1123
 * @createDate: 2023/10/15 19:14
 */

@Slf4j
public class MybatisTest {

    public static void main(String[] args) throws IOException {
        SqlSessionFactory sessionFactory = getSessionFactory();
        // staticInvoke(sessionFactory);
        // update(sessionFactory);
        documentCreate_customer(sessionFactory);
    }

    private static SqlSessionFactory getSessionFactory() {
        // 读取 mybatis-config.xml 配置文件
        String resource = "mybatis-config.xml";
        InputStream in;
        try {
            in = Resources.getResourceAsStream(resource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 加载 mybatis-config.xml 配置文件, 并创建 SqlSessionFactory 对象
        return new SqlSessionFactoryBuilder().build(in);
    }

    /**
     * 原生mybatis查询
     *
     * @param sessionFactory sessionFactory
     */
    private static void staticInvoke(SqlSessionFactory sessionFactory) {

        /*两种不同的调用方式*/
        try (SqlSession session = sessionFactory.openSession()) {
            // 对于原生Java, 使用pageHelper需要在mybatis的配置文件配置该插件
            PageHelper.startPage(1, 10);
            List<CustomerDO> customerDOS = session.selectList("com.hundsun.demo.spring.mybatis.mapper.CustomerMapper.selectAll");
            log.info("{}", customerDOS);
        }
        try (SqlSession sqlSession = sessionFactory.openSession()) {
            // 获取Mapper
            CustomerMapper customerMapper = sqlSession.getMapper(CustomerMapper.class);
            // 调用Mapper方法
            List<CustomerDO> customerDOS = customerMapper.selectAll();
            log.info("{}", customerDOS);
        }
    }

    /**
     * 原生mybatis更新操作(包括回滚)
     *
     * @param sessionFactory sessionFactory
     */
    private static void update(SqlSessionFactory sessionFactory) {
        try (SqlSession session = sessionFactory.openSession()) {
            boolean isRollback = false;
            try {
                CustomerDO customerDO = new CustomerDO();
                customerDO.setCustomernumber(103);
                customerDO.setPhone("40.32.251");
                session.update("com.hundsun.demo.spring.mybatis.mapper.CustomerMapper.updateOne", customerDO);
            } catch (Exception e) {
                isRollback = true;
                log.error("更新出现异常! 正在尝试回滚...", e);
            }
            if (isRollback) {
                session.rollback();
            } else {
                // mybatis 不主动提交的话, 是不会自动提交的, session关闭后会自动回滚
                session.commit();
            }
        }
    }

    /**
     * 给es新增文档内容
     *
     * @throws IOException
     */
    private static synchronized void documentCreate_customer(SqlSessionFactory sessionFactory) throws IOException {
        // Index data to an index products

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
        ElasticsearchClient client = new ElasticsearchClient(transport);

        List<CustomerDO> customerList;
        try (SqlSession sqlSession = sessionFactory.openSession()) {
            CustomerMapper mapper = sqlSession.getMapper(CustomerMapper.class);
            customerList = mapper.selectAll();
        }

        if (!CollectionUtils.isEmpty(customerList)) {
            // 创建倒排索引
            BulkResponse bulkResponse = client.bulk(fn -> {
                for (CustomerDO customer : customerList) {
                    fn.operations(bop -> bop.index(idx -> idx.index("customers").id(customer.getCustomernumber().toString()).document(customer)));
                }
                return fn;
            });
            if (bulkResponse.errors()) {
                log.error("Bulk had errors");
                for (BulkResponseItem item : bulkResponse.items()) {
                    if (item.error() != null) {
                        log.error("{}", item.error().reason());
                    }
                }
            } else {
                log.info("Bulk write success!");
            }
        }
    }
}
