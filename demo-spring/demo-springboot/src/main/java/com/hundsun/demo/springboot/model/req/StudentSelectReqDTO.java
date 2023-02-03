package com.hundsun.demo.springboot.model.req;

import lombok.Data;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.model.req
 * @className: StudentSelectReqDTO
 * @description:
 * @author: h1123
 * @createDate: 2022/11/5 13:54
 * @updateUser: h1123
 * @updateDate: 2022/11/5 13:54
 * @updateRemark:
 * @version: v1.0
 * @see :
 */
@Data
public class StudentSelectReqDTO {

    // todo 关于子类访问父类成员变量的问题, 子类中可以直接 .父类的方法 却不能 .父类的属性?
    protected String name;

    protected Integer age;
}
