package com.hundsun.demo.common.autoconfigure.commom;

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
 * @Date: 2022-06-10 17:14
 */

@ConfigurationProperties(prefix = "result")
public class ResultAspectConfiguration {

    private Properties properties = new Properties();

    public String getScanRange() {
        return properties.getProperty("scanRange");
    }

    public void setScanRange(String scanRange) {
        properties.setProperty("scanRange", new String(scanRange.getBytes(ISO_8859_1), StandardCharsets.UTF_8));
    }
}
