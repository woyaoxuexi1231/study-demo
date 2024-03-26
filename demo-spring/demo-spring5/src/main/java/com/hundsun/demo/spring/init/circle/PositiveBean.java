package com.hundsun.demo.spring.init.circle;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.spring.init.circle
 * @className: PositiveBean
 * @description:
 * @author: h1123
 * @createDate: 2023/2/8 0:34
 */

@Setter
@Getter
public class PositiveBean {

    /**
     * PositiveBean 依赖于 NegativeBean
     */
    private NegativeBean negativeBean;
}
