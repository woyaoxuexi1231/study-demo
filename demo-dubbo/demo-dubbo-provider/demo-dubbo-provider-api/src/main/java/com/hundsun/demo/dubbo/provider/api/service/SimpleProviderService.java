package com.hundsun.demo.dubbo.provider.api.service;

import com.hundsun.demo.dubbo.common.api.model.dto.ResultDTO;
import com.hundsun.demo.dubbo.provider.api.model.request.UserRequestDTO;
import com.hundsun.demo.dubbo.provider.api.model.request.UserSelectReqDTO;

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

    /**
     * 返回一个简单的字符串
     * @param hello req
     * @return result
     */
    ResultDTO<?> getHelloWorld(String hello);

    /**
     * 插入一个新用户
     * @param userRequestDTO req
     * @return result
     */
    ResultDTO<?> insertUser(UserRequestDTO userRequestDTO);

    /**
     * 查询一个用户 -DB
     * @param req req
     * @return result
     */
    ResultDTO<?> selectUser(UserSelectReqDTO req);
}
