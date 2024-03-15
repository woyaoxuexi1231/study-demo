package com.hundsun.demo.dubbo.provider.service.impl;

import com.hundsun.demo.commom.core.annotation.DoneTime;
import com.hundsun.demo.commom.core.model.dto.ResultDTO;
import com.hundsun.demo.commom.core.utils.ResultDTOBuild;
import com.hundsun.demo.dubbo.provider.api.service.SimpleProviderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: dubbo-demo
 * @Package: com.hundsun.amust.dubbodemo.service.impl
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-05-21 15:15
 */

@DubboService
@Slf4j
public class SimpleProviderServiceImpl implements SimpleProviderService {

    @DoneTime
    @Override
    public ResultDTO<?> RpcSimpleInvoke() {
        return ResultDTOBuild.resultSuccessBuild("hello rpc!");
    }

}
