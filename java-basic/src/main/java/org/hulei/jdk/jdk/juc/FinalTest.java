package org.hulei.jdk.jdk.juc;

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
    /*
    String 是一个不可变的类，表示字符序列. 使用双引号 "" 创建字符串时，我们实际上是在直接使用一个字符串字面量
    在Java中，除了String类被设计成不可变的之外，基本类型的包装类（如Integer、Double、Boolean等）也是不可变的。这意味着一旦创建了这些对象，它们的值就不能被修改。
    这种设计有着类似于字符串不可变性的优势：
    1. **线程安全性：** 不可变对象可以在多线程环境中安全地共享，因为它们的值不会改变。
    2. **缓存利用：** 因为不可变对象的值不会改变，所以它们可以被安全地缓存，以便在多次使用时重复利用。
    3. **参数传递：** 不可变对象在作为方法参数传递时更加安全，因为调用者不必担心对象在方法中被修改。
    4. **Hash算法安全：** 不可变对象的哈希值是固定的，因此可以安全地用作哈希映射的键。
    虽然在某些情况下，可变对象可能会提供更灵活的解决方案，但不可变对象的设计可以减少代码中的错误，并促进更好的线程安全性和可维护性。
     */
    String string;
}
