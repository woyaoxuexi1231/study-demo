package com.hundsun.demo.dubbo.provider.api.service;

import com.hundsun.demo.dubbo.common.api.model.dto.ResultDTO;
import com.hundsun.demo.dubbo.provider.api.model.request.UserRequestDTO;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: dubbo-demo
 * @Package: com.hundsun.amust.dubbodemo.service
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-05-21 15:14
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright  2022 Hundsun Technologies Inc. All Rights Reserved
 */

public interface SimpleProviderService {

    ResultDTO getHelloWorld(String hello);

    ResultDTO insertUser(UserRequestDTO userRequestDTO);
}
