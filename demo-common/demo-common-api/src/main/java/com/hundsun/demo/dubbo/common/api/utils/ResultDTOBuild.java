package com.hundsun.demo.dubbo.common.api.utils;

import com.hundsun.demo.dubbo.common.api.model.dto.ResultDTO;
import lombok.Data;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.dubbo.common.api.utils
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-06-10 16:03
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright  2022 Hundsun Technologies Inc. All Rights Reserved
 */
@Data
public class ResultDTOBuild {

    public static ResultDTO<?> resultDefaultBuild() {
        ResultDTO<?> resultDTO = new ResultDTO<>();
        resultDTO.setCode(200);
        resultDTO.setMsg("success");
        return resultDTO;
    }

    public static <Rsp> ResultDTO<Rsp> resultSuccessBuild(Rsp rsp) {
        ResultDTO<Rsp> resultDTO = new ResultDTO<>();
        resultDTO.setCode(200);
        resultDTO.setMsg("success");
        resultDTO.setData(rsp);
        return resultDTO;
    }

    public static <Rsp> ResultDTO<Rsp> resultErrorBuild(String msg, Rsp rsp) {
        ResultDTO<Rsp> resultDTO = new ResultDTO<>();
        resultDTO.setCode(-1);
        resultDTO.setMsg(msg);
        resultDTO.setData(rsp);
        return resultDTO;
    }
}
