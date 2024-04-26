package com.hundsun.demo.spring.jdk.juc;

import lombok.Data;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.spring.jdk.juc
 * @Description:
 * @Author: hulei42031
 * @Date: 2024-04-26 15:40
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright 2023 Hundsun Technologies Inc. All Rights Reserved
 */

public class FinalTest {

    static String string = "123";

    static final String finalString = "123";

    static final FinalObject finalObject = new FinalObject();

    public static void main(String[] args) {

        System.out.println(string);
        string = "234";
        System.out.println(string);


        // 这个代码会编译报错,提示为final类型的
        // finalString = "234";

        System.out.println(finalObject);
        // 对象类型的这种操作是被允许的
        finalObject.setString("123");
        System.out.println(finalObject);
        // 但是直接改变这个对象的引用将不被允许,所以这行代码会编译报错
        // finalObject = new FinalObject();


        // 综上,final仅做到不能改变对象本身,但是如果要改变对象内的值编译器并不会阻止这种操作
    }
}

@Data
class FinalObject {
    String string;
}
