package com.hundsun.demo.dubbo.provider.api.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.dubbo.consumer.api.model.request
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-10-20 16:50
 */
@Data
public class UserSelectReqDTO implements Serializable {

    private String name;
}
