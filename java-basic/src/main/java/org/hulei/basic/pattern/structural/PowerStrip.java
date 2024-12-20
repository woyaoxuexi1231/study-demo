package org.hulei.basic.pattern.structural;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.pattern.structural
 * @className: PowerStrip
 * @description:
 * @author: h1123
 * @createDate: 2023/2/5 19:35
 */

public class PowerStrip {

    private static class HolderClass {
        private final static PowerStrip instance = new PowerStrip();
    }

    public static PowerStrip getInstance() {
        return PowerStrip.HolderClass.instance;
    }

    public Power220 offerPower() {
        return new Power220(1);
    }
}
