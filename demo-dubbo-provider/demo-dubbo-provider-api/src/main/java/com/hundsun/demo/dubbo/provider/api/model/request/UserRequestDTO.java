package com.hundsun.demo.dubbo.provider.api.model.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @projectName: dubbo-demo
 * @package: com.hundsun.dubbodemo.common.model.request
 * @className: UserRequestDTO
 * @description:
 * @author: h1123
 * @createDate: 2022/5/22 2:53
 * @updateUser: h1123
 * @updateDate: 2022/5/22 2:53
 * @updateRemark:
 * @version: v1.0
 * @see :
 */
@Data
public class UserRequestDTO implements Serializable {

    @NotNull(message = "ID不能为空")
    private Integer id;

    private String name;
}
