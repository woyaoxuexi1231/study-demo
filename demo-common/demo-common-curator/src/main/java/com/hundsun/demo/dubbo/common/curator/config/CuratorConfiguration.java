package com.hundsun.demo.dubbo.common.curator.config;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Properties;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.duubo.common.curator.config
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-06-27 13:34
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright  2022 Hundsun Technologies Inc. All Rights Reserved
 */
@Data
@ConfigurationProperties(prefix = "curator")
public class CuratorConfiguration {

    /**
     *
     */
    private Properties properties = new Properties();

    /**
     * 服务注册地址，默认获取
     */
    @Value("${dubbo.registry.address}")
    private String appRegistryAddress;

    /**
     * 重试次数
     *
     * @param retryCount
     */
    public void setRetryCount(String retryCount) {
        properties.setProperty("retryCount", retryCount);
    }

    /**
     * 重试次数-默认4
     *
     * @return
     */
    public String getRetryCount() {
        return StringUtils.isBlank(properties.getProperty("retryCount")) ? "4" : properties.getProperty("retryCount");
    }

    /**
     * 重试间隔时间
     *
     * @param elapsedTimeMs
     */
    public void setElapsedTimeMs(String elapsedTimeMs) {
        properties.setProperty("elapsedTimeMs", elapsedTimeMs);
    }

    /**
     * 重试间隔时间-默认500ms
     *
     * @return
     */
    public String getElapsedTimeMs() {
        return StringUtils.isBlank(properties.getProperty("elapsedTimeMs")) ? "500" : properties.getProperty("elapsedTimeMs");
    }


    /**
     * zookeeper地址
     *
     * @param connectString
     */
    public void setConnectString(String connectString) {
        properties.setProperty("connectString", connectString);
    }

    /**
     * zookeeper地址-默认返回服务注册地址
     *
     * @return
     */
    public String getConnectString() {
        return StringUtils.isBlank(properties.getProperty("connectString")) ? this.getAppRegistryAddress().substring(this.appRegistryAddress.indexOf("/") + 2) : properties.getProperty("connectString");
    }

    /**
     * session超时时间
     *
     * @param sessionTimeoutMs
     */
    public void setSessionTimeoutMs(String sessionTimeoutMs) {
        properties.setProperty("sessionTimeoutMs", sessionTimeoutMs);
    }

    /**
     * session超时时间-默认60000ms
     *
     * @return
     */
    public String getSessionTimeoutMs() {
        return StringUtils.isBlank(properties.getProperty("sessionTimeoutMs")) ? "60000" : properties.getProperty("sessionTimeoutMs");
    }


    /**
     * 连接超时时间
     *
     * @param connectionTimeoutMs
     */
    public void setConnectionTimeoutMs(String connectionTimeoutMs) {
        properties.setProperty("connectionTimeoutMs", connectionTimeoutMs);
    }

    public String getConnectionTimeoutMs() {
        return StringUtils.isBlank(properties.getProperty("connectionTimeoutMs")) ? "10000" : properties.getProperty("connectionTimeoutMs");
    }
}
