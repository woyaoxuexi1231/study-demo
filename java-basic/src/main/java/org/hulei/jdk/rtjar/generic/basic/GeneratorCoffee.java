package org.hulei.jdk.rtjar.generic.basic;

import lombok.SneakyThrows;

import java.util.Random;

/**
 * @author hulei
 * @since 2024/9/15 14:55
 */

public class GeneratorCoffee implements Generator<Coffee> {

    Random random = new Random();

    Class<Coffee>[] classes = new Class[]{Latte.class, Mocha.class};

    @SneakyThrows
    @Override
    public Coffee generateRandom() {
        return classes[(random.nextInt() & (Integer.MAX_VALUE >> 1)) % 2].newInstance();
    }

    public static void main(String[] args) {
        /*
        Generator泛型接口定义了一个返回泛型的generateRandom方法
        实现这个接口的类如果指定了实际类型,都需要实现一个返回指定类型的generateRandom方法,这样可以通过Generator接口创建各种各样的生成对象的工厂类
         */
        GeneratorCoffee generator = new GeneratorCoffee();
        for (int i = 0; i < 10; i++) {
            generator.generateRandom();
        }
    }

}

interface Generator<T> {
    T generateRandom();
}

class Coffee {
    public Coffee() {
    }
}

class Latte extends Coffee {
    public Latte() {
        System.out.println("this is latte");
    }
}

class Mocha extends Coffee {
    public Mocha() {
        System.out.println("this is mocha");
    }
}