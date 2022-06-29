package com.hundsun.demo.dubbo.provider.model.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @projectName: dubbo-demo
 * @package: com.hundsun.dubbodemo.provider.model
 * @className: UserDO
 * @description:
 * @author: h1123
 * @createDate: 2022/5/22 2:42
 * @updateUser: h1123
 * @updateDate: 2022/5/22 2:42
 * @updateRemark:
 * @version: v1.0
 * @see :
 */

@Data
@Table(name = "user")
public class UserDO {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;
}
