package org.hulei.common.core.utils;

import org.hulei.common.core.model.dto.ResultDTO;
import lombok.Data;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.dubbo.common.api.utils
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-06-10 16:03
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

    public static <Rsp> ResultDTO<Rsp> resultErrorBuild(String msg) {
        ResultDTO<Rsp> resultDTO = new ResultDTO<>();
        resultDTO.setCode(-1);
        resultDTO.setMsg(msg);
        return resultDTO;
    }

    public static <Rsp> ResultDTO<Rsp> resultErrorBuild(int code, String msg) {
        ResultDTO<Rsp> resultDTO = new ResultDTO<>();
        resultDTO.setCode(-1);
        resultDTO.setMsg(msg);
        return resultDTO;
    }
}
