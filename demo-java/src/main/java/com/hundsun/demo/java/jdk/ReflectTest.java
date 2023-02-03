package com.hundsun.demo.java.jdk;

import lombok.Data;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.java.reflect
 * @Description:
 * @Author: hulei42031
 * @Date: 2023-01-30 10:15
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright  2022 Hundsun Technologies Inc. All Rights Reserved
 */

public class ReflectTest {


    public static void main(String[] args) {

        try {


            Class<?> clazz = Class.forName("com.hundsun.demo.java.reflect.ReflectTest$Book");
            Object object = clazz.newInstance();

            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                System.out.println(method);
            }

            System.out.println(object);
            Constructor<?> constructor = clazz.getConstructor();
            Object object2 = constructor.newInstance();
            System.out.println(object2);

            Field[] fields = clazz.getFields();
            Field[] declaredFields = clazz.getDeclaredFields();

            System.out.println("-----------field------------");
            for (Field field : fields) {
                System.out.println(field);
            }
            System.out.println("-----------field------------");

            System.out.println("-----------declaredField------------");
            for (Field declaredField : declaredFields) {
                declaredField.setAccessible(true);
                if (declaredField.getType().equals(String.class)) {
                    declaredField.set(object, "string");
                } else if (declaredField.getType().equals(Integer.class)) {
                    declaredField.set(object, 0);
                }
            }
            System.out.println("-----------declaredField------------");

            Constructor<?>[] constructors = clazz.getDeclaredConstructors();
            for (Constructor<?> constructor1 : constructors) {
                System.out.println(constructor1);
            }
            System.out.println(object);

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Data
    static class Book {

        private String name;

        private String title;

        private Integer price;
    }
}
