package org.hulei.jdk.stream;

import lombok.Data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.java.stream
 * @Description:
 * @Author: hulei42031
 * @Date: 2023-01-30 13:24
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright  2022 Hundsun Technologies Inc. All Rights Reserved
 */

public class StreamTest {

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd", "", "jkl");
        List<String> filtered = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.toList());


        ArrayList<String> list = new ArrayList<>();
        list.add("1");

        Test test = new Test();
        System.out.println(test);

        System.out.println(0b11111111111111111111111111111111);

        Integer[] integers = new Integer[10];

        for (int i = 0; i < 10; i++) {
            integers[i] = i;
        }

        Integer[] integers2 = Arrays.copyOf(integers, 11);
        System.out.println(integers2.length);

        System.arraycopy(integers, 3, integers, 2, integers.length - 2 - 1);
        integers[integers.length - 1] = null;

        Arrays.stream(integers).forEach(System.out::print);

        LinkedList<String> list1 = new LinkedList<>();

        list1.add("1");

        ArrayList<String> arrayList = null;
        final ArrayList<String> arrayList1 = arrayList;
        arrayList = new ArrayList<>();
        arrayList.add("1");


        ArrayList<String> arrayList2 = (ArrayList<String>) arrayList.clone();


        System.out.println();
        System.out.println(arrayList);
        System.out.println(arrayList1);
        // System.out.println(arrayList2.hashCode());

        HashMap<Integer, String> hashMap = new HashMap<>();
        hashMap.put(1, "1");

    }

    @Data
    static class Test {
        private int size;
    }
}
