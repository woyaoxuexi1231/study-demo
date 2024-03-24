package com.hundsun.demo.spring.init.circle;

import lombok.Data;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.spring.init.circle
 * @className: PositiveBean
 * @description:
 * @author: h1123
 * @createDate: 2023/2/8 0:34
 */

@Data
public class PositiveBean {

    /**
     * PositiveBean 依赖于 NegativeBean
     */
    private NegativeBean negativeBean;

    // public PositiveBean(NegativeBean negativeBean) {
    //     this.negativeBean = negativeBean;
    // }

    public void setNegativeBean(NegativeBean negativeBean) {
        this.negativeBean = negativeBean;
    }

    public void injectDependency(NegativeBean negativeBean) {
        this.negativeBean = negativeBean;
    }

}
