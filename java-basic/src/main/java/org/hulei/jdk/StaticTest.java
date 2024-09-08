package org.hulei.jdk;

import java.util.Random;

public class StaticTest {

    public static void main(String[] args) {
        printsfn();
        subClass.sb.append(1);
        printsfn();
    }

    static void prints() {
        System.out.println(subClass.VALUE);
        /*
        类加载了
        静态变量
         */
    }

    static void printsf() {
        System.out.println(subClass.FINAL_VALUE);
        /*
        静态变量
         */
    }

    static void printsfn() {
        // System.out.println(subClass.FINAL_VALUE_INT);
        System.out.println(subClass.sb);
        /*
        类加载了
        -1157793070
         */
    }
}


class subClass {

    public static String VALUE = "静态变量";
    public static final String FINAL_VALUE = "静态常量";
    public static final int FINAL_VALUE_INT = new Random(10).nextInt();
    // public static final int in  = Integer.parseInt("1");
    public static final StringBuffer sb = new StringBuffer("init");

    static {
        System.out.println("类加载了");
    }
}