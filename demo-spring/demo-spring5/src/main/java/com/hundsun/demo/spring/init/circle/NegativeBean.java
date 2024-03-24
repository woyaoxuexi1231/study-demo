package com.hundsun.demo.spring.init.circle;

import lombok.Data;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.spring.init.circle
 * @className: NegativeBean
 * @description:
 * @author: h1123
 * @createDate: 2023/2/8 0:34
 */

@Data
public class NegativeBean {

    /**
     * NegativeBean 依赖于 positiveBean
     */
    private PositiveBean positiveBean;

    // public NegativeBean(PositiveBean positiveBean) {
    //     this.positiveBean = positiveBean;
    // }

    public void setPositiveBean(PositiveBean positiveBean) {
        this.positiveBean = positiveBean;
    }
}
