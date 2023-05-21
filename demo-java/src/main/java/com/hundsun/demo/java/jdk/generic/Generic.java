package com.hundsun.demo.java.jdk.generic;

import java.util.Arrays;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.jdk.generic
 * @className: Generic
 * @description:
 * @author: hl
 * @createDate: 2023/5/20 11:43
 */

public class Generic<T extends GenericInterface> {

    T t;

    public Generic(T t) {
        this.t = t;
    }

    public T getT() {
        return t;
    }

    public void print() {
        System.out.println(t.getClass().getName());
        System.out.println(Arrays.toString(t.getClass().getTypeParameters()));
        t.print();
    }

}
