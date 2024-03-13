package com.hundsun.demo.spring.jdk.spi;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.jdk.spi
 * @className: EngTranslate
 * @description:
 * @author: h1123
 * @createDate: 2023/2/5 1:57
 */

public class EngTranslate implements Translate {

    /**
     * 这里模拟英语翻译
     */
    @Override
    public void translate() {
        System.out.println("Hello world");
    }
}
