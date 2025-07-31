package org.hulei.basic.jdk.spi;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.jdk.spi
 * @className: RusTranslate
 * @description:
 * @author: h1123
 * @createDate: 2023/2/5 2:00
 */

public class RusTranslate implements Translate {

    /**
     * 这里模拟俄语翻译
     */
    @Override
    public void translate() {
        System.out.println("Всем привет");
    }
}
