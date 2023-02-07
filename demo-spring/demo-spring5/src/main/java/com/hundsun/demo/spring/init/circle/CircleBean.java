package com.hundsun.demo.spring.init.circle;

import lombok.Data;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.spring.init.circle
 * @className: CircleBean
 * @description:
 * @author: h1123
 * @createDate: 2023/2/8 0:40
 */

@Data
public class CircleBean {

    /**
     * 自己注入自己
     */
    private CircleBean circleBean;
}
