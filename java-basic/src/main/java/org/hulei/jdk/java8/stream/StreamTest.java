package org.hulei.jdk.java8.stream;

import lombok.Data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        System.out.println(strings.stream().filter(string -> !string.isEmpty()).count());

        System.out.println(Stream.of(null).count());

    }

    @Data
    static class Test {
        private int size;
    }
}
