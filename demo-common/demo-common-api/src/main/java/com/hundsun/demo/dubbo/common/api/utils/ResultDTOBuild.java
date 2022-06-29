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

    public static ResultDTO<Object> resultDefaultBuild() {
        ResultDTO<Object> resultDTO = new ResultDTO<>();
        resultDTO.setCode(200);
        resultDTO.setMsg("success");
        return resultDTO;
    }

    public static ResultDTO<Object> resultSuccessBuild(Object data) {
        ResultDTO<Object> resultDTO = new ResultDTO<>();
        resultDTO.setCode(200);
        resultDTO.setMsg("success");
        resultDTO.setData(data);
        return resultDTO;
    }

    public static ResultDTO<Object> resultErrorBuild(String msg, Object data) {
        ResultDTO<Object> resultDTO = new ResultDTO<>();
        resultDTO.setCode(-1);
        resultDTO.setMsg(msg);
        resultDTO.setData(data);
        return resultDTO;
    }
}
