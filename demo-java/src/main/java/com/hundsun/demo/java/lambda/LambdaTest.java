package com.hundsun.demo.java.lambda;


import lombok.extern.slf4j.Slf4j;

/**
 * Lambda测试类
 *
 * @ProductName: Java
 * @Package:
 * @Description:
 * @Author: HL
 * @Date: 2021/10/12 16:13
 * @Version: 1.0
 * <p>
 */

@Slf4j
public class LambdaTest {

    public static void main(String[] args) {

        LambdaTest tester = new LambdaTest();

        // 类型声明
        MathOperation addition = (int a, int b) -> a + b;

        // 不用类型声明
        MathOperation subtraction = (a, b) -> a - b;

        // 大括号中的返回语句
        MathOperation multiplication = (int a, int b) -> {
            return a * b;
        };

        // 没有大括号及返回语句
        MathOperation division = (int a, int b) -> a / b;

        log.info("10 + 5 = " + tester.operate(10, 5, addition));
        log.info("10 - 5 = " + tester.operate(10, 5, subtraction));
        log.info("10 x 5 = " + tester.operate(10, 5, multiplication));
        log.info("10 / 5 = " + tester.operate(10, 5, division));

        // 不用括号
        GreetingService greetService1 = message ->
                log.info("Hello " + message);

        // 用括号
        GreetingService greetService2 = (message) ->
                log.info("Hello " + message);

        greetService1.sayMessage("Runoob");
        greetService2.sayMessage("Google");
    }

    interface MathOperation {
        /**
         * 接受两个int 返回一个int 的方法
         *
         * @param a
         * @param b
         * @return
         */
        int operation(int a, int b);
    }

    interface GreetingService {
        /**
         * 接受一个String 无返回的方法
         *
         * @param message
         */
        void sayMessage(String message);
    }

    private int operate(int a, int b, MathOperation mathOperation) {
        return mathOperation.operation(a, b);
    }

}
