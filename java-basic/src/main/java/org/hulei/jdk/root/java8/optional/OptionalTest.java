package org.hulei.jdk.root.java8.optional;

import java.util.Optional;

/**
 * @author hulei
 * @since 2024/9/11 11:28
 */

public class OptionalTest {
    public static void main(String[] args) {

        // 传统方式：
        String value = "null";
        if (value != null) {
            System.out.println(value.length());
        } else {
            System.out.println(0);
        }

        // 使用 Optional：
        Optional<String> optionalValue = Optional.ofNullable("null");
        System.out.println(optionalValue.map(String::length).orElse(0));

        // 主要提供一种更清晰简便的方式来避免空指针
    }
}
