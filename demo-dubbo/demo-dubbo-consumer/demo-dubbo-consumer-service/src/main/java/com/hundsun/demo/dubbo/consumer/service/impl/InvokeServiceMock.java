package com.hundsun.demo.dubbo.consumer.service.impl;

import com.hundsun.demo.commom.core.model.dto.ResultDTO;
import com.hundsun.demo.commom.core.utils.ResultDTOBuild;
import com.hundsun.demo.dubbo.provider.api.service.ProviderService;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.dubbo.consumer.service.impl
 * @className: MockService
 * @description:
 * @author: woaixuexi
 * @createDate: 2024/3/16 13:24
 */

public class InvokeServiceMock implements ProviderService {

    @Override
    public ResultDTO<?> RpcInvoke() {
        return ResultDTOBuild.resultSuccessBuild("Sorry, the service is currently unavailable.");
    }
}
