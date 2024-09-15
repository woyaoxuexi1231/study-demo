package org.hulei.jdk.root.jvm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author hulei42031
 * @since 2024-04-08 18:33
 */

public class JvmHeapOOM {

    public static void main(String[] args) {
        // 最大10mb,初始10mb
        // -Xmx10m -Xms10m
        // 直接报错 java.lang.OutOfMemoryError: GC overhead limit exceeded
        List<SimpleObject> objects = new ArrayList<>();
        for (int i = 0; i < 1000000; i++) {
            objects.add(SimpleObject.builder().integer(i).string(new Date().toString()).build());
        }
        System.out.println(111);
    }
}
