package org.hulei.jdk.jdk.java8.stream;

import lombok.Data;

import java.io.IOException;
import java.util.Arrays;
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

        System.out.println(strings.stream().filter(String::isBlank).count());

        /*
        当 strings.stream() 执行, ArrayList会生成一个 ArrayListSpliterator 的类，这个类实现了Spliterator接口
         */
        List<String> filtered = strings.stream()
                .filter((i)->{
                    return i.isEmpty();
                })
                .map((i)->{
                    return i.toLowerCase();
                })
                .toList();
        System.out.println(strings.stream().filter(string -> !string.isEmpty()).count());

        System.out.println(Stream.of(null).count());

    }

    @Data
    static class Test {
        private int size;
    }
}
