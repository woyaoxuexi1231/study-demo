package com.hundsun.demo.dubbo.common.redis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

import static java.nio.charset.StandardCharsets.ISO_8859_1;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.dubbo.common.api.config
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-06-16 16:24
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright  2022 Hundsun Technologies Inc. All Rights Reserved
 */
@ConfigurationProperties(prefix = "spring.redis.sentinel")
public class RedissonClientConfiguration {

    /**
     * 存储配置文件信息的类
     *
     * <p>
     * Properties 继承于 Hashtable。表示一个持久的属性集.属性列表中每个键及其对应值都是一个字符串。
     * Properties 类被许多 Java 类使用。例如，在获取环境变量时它就作为 System.getProperties() 方法的返回值。
     * <p/>
     */
    private Properties properties = new Properties();

    /**
     * 主哨兵
     */
    private String master;

    public String getMaster() {
        return properties.getProperty("master");
    }

    public void setMaster(String master) {
        properties.setProperty("master", new String(master.getBytes(ISO_8859_1), StandardCharsets.UTF_8));
    }

    private String nodes;

    public String getNodes() {
        return properties.getProperty("nodes");
    }

    public void setNodes(String nodes) {
        properties.setProperty("nodes", new String(nodes.getBytes(ISO_8859_1), StandardCharsets.UTF_8));
    }

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.password}")
    private String password;

    public String getHost() {
        return host;
    }

    public String getPassword() {
        return password;
    }
}
