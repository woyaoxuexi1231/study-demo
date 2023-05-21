package com.hundsun.demo.java.jdk.generic;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.jdk
 * @className: Generic
 * @description:
 * @author: hl
 * @createDate: 2023/5/20 11:41
 */

public class GenericMain {

    public static void main(String[] args) {

        Generic<GenericImpl> generic = new Generic<>(new GenericImpl());
        // Type parameter 'java.lang.Object' is not within its bound; should implement 'com.hundsun.demo.java.jdk.generic.GenericInterface'
        // Generic<Object> objectGeneric = new Generic<>();

        generic.print();
        generic.getClass().getTypeParameters();

        GenericObject genericObject = new GenericObject(new GenericImpl());
        genericObject.print();
    }

    static class GenericImpl implements GenericInterface {

        @Override
        public void print() {
            System.out.println("hello");
        }
    }
}
