package com.hundsun.demo.spring.jdk.jvm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author hulei42031
 * @since 2024-04-08 18:33
 */

public class ObjectHeap {

    public static void main(String[] args) {

        List<SimpleObject> objects = new ArrayList<>();
        for (int i = 0; i < 1000000; i++) {
            objects.add(SimpleObject.builder().integer(i).string(new Date().toString()).build());
        }
        System.out.println(111);
    }
}
