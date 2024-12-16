package org.hulei.common.autoconfigure.curator;

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
 */
@ConfigurationProperties(prefix = "curator")
public class CuratorConfiguration {

    /**
     *
     */
    private final Properties properties = new Properties();

    /**
     * dubbo 注册地址
     */
    @Value("${dubbo.registry.address:127.0.0.1:2181}")
    private String appRegistryAddress;

    /**
     * connectString - 连接信息
     */
    public void setConnectString(String connectString) {
        properties.setProperty("connectString", connectString);
    }

    /**
     * session timeout - 默认 60 * 1000 ms
     */
    public void setSessionTimeoutMs(String sessionTimeoutMs) {
        properties.setProperty("sessionTimeoutMs", sessionTimeoutMs);
    }

    /**
     * connection timeout - if null, default 10 * 1000 ms
     */
    public void setConnectionTimeoutMs(String connectionTimeoutMs) {
        properties.setProperty("connectionTimeoutMs", connectionTimeoutMs);
    }

    /**
     * curator 重连次数, 默认 4
     */
    public void setRetryCount(String retryCount) {
        properties.setProperty("retryCount", retryCount);
    }

    /**
     * curator 重试间隔时间
     */
    public void setElapsedTimeMs(String elapsedTimeMs) {
        properties.setProperty("elapsedTimeMs", elapsedTimeMs);
    }


    public String getConnectString() {
        return StringUtils.isBlank(properties.getProperty("connectString")) ? this.appRegistryAddress.substring(this.appRegistryAddress.indexOf("/") + 2) : properties.getProperty("connectString");
    }

    public String getSessionTimeoutMs() {
        return StringUtils.isBlank(properties.getProperty("sessionTimeoutMs")) ? "60000" : properties.getProperty("sessionTimeoutMs");
    }

    public String getConnectionTimeoutMs() {
        return StringUtils.isBlank(properties.getProperty("connectionTimeoutMs")) ? "10000" : properties.getProperty("connectionTimeoutMs");
    }

    public String getRetryCount() {
        return StringUtils.isBlank(properties.getProperty("retryCount")) ? "4" : properties.getProperty("retryCount");
    }

    public String getElapsedTimeMs() {
        return StringUtils.isBlank(properties.getProperty("elapsedTimeMs")) ? "500" : properties.getProperty("elapsedTimeMs");
    }

}
