package com.hundsun.demo.java.jdk.generic;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.jdk.generic
 * @className: GenericObject
 * @description:
 * @author: hl
 * @createDate: 2023/5/20 15:04
 */

public class GenericObject {

    Object object;

    public GenericObject(Object object) {
        this.object = object;
    }

    public void print() {
        System.out.println(object.getClass().getName());
    }
}
