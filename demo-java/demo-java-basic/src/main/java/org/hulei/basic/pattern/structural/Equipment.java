package org.hulei.basic.pattern.structural;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.pattern.structural
 * @className: Equipment
 * @description:
 * @author: h1123
 * @createDate: 2023/2/5 20:46
 */

public abstract class Equipment {

    /**
     * 设备的电量
     */
    protected int electricity;

    /**
     * 通过使用适配器进行充电
     *
     * @param adapter adapter
     */
    public abstract void getCharge(Adapter adapter);
}
