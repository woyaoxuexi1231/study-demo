package org.hulei.basic.jdk.java8.lambda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author hulei
 * @since 2024/12/23 21:59
 */

public class ComparableTest {

    public static void main(String[] args) {
        /*
        jdk 提供了 Comparable 接口来实现对象之间的大小比较
        调用 x.compareTo(y) 方法：
            1. 两者相等 = 0
            2. x小于y < 0
            3. x大于y > 0
        实现这个接口的类，可以被很容易的排序，比如 Array.sort() 默认升序

        Comparable提供了单一的比较方法，如果需要一些定制化的比较方案，就比较麻烦了。
        Array提供了第二种比较方案，接受一个 数组 + 比较器(comparator)
         */
        Integer[] origin = new Integer[5];
        origin[0] = 4;
        origin[1] = 3;
        origin[2] = 1;
        origin[3] = 10;
        origin[4] = 11;

        Integer[] needComparable = Arrays.copyOf(origin, origin.length);
        Arrays.sort(needComparable);
        System.out.println(Arrays.toString(needComparable));

        // 使用 comparator 来比较大小，这里直接以降序排列
        Integer[] needComparator = Arrays.copyOf(origin, origin.length);
        Arrays.sort(needComparator, (o1, o2) -> -o1.compareTo(o2));
        System.out.println(Arrays.toString(needComparator));
    }
}
