package com.hundsun.demo.common.autoconfigure.commom;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.dubbo.common.api.config
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-06-10 17:14
 */

@Data
// @ConfigurationProperties(prefix = "result")
public class ResultAspectConfiguration {

    /**
     * 统一结果处理扫描范围
     */
    private String scanRange;
}
