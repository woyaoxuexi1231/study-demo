package org.hulei.basic.jdk.spi;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.jdk.spi
 * @className: Translate
 * @description:
 * @author: h1123
 * @createDate: 2023/2/5 1:32
 */

public interface Translate {

    /**
     * SPI接口对外提供的方法, 以供服务调用方实现该方法, 这里模拟一个翻译场景
     */
    void translate();
}
