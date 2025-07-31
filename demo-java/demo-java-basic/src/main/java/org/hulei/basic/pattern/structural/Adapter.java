package org.hulei.basic.pattern.structural;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.pattern.structural.proxy
 * @className: Charge
 * @description:
 * @author: h1123
 * @createDate: 2023/2/5 19:46
 */

public interface Adapter {

    /**
     * 获得电量
     *
     * @return power
     */
    Power getPower();
}
