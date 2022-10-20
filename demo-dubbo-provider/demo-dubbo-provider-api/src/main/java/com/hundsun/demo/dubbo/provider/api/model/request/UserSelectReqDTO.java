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
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright  2022 Hundsun Technologies Inc. All Rights Reserved
 */
@Data
public class UserSelectReqDTO implements Serializable {

    private String name;
}
