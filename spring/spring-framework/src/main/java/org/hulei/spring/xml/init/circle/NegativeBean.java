package org.hulei.spring.xml.init.circle;

import lombok.Getter;
import lombok.Setter;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.spring.init.circle
 * @className: NegativeBean
 * @description:
 * @author: h1123
 * @createDate: 2023/2/8 0:34
 */

@Setter
@Getter
public class NegativeBean {

    /**
     * NegativeBean 依赖于 positiveBean
     */
    private PositiveBean positiveBean;
}
