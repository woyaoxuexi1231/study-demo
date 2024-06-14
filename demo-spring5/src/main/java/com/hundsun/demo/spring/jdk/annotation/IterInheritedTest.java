package com.hundsun.demo.spring.jdk.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;

public class IterInheritedTest {

    @Inherited
    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface InheritedAnnotationType {
    }

    @Inherited
    @Target({ElementType.FIELD, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ABC {
        String value() default "";
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface UnInheritedAnnotationType {
    }

    @UnInheritedAnnotationType // 无 @Inherited
    static
    class A {
    }

    @InheritedAnnotationType // 有 @Inherited
    static
    class B extends A {
    }

    static class C extends B {
    }

    @UnInheritedAnnotationType // 无 @Inherited
    interface Z {
        @ABC()
            // 有 @Inherited
        void he();
    }

    @InheritedAnnotationType // @Inherited
    interface Y extends Z {
        @ABC("hhhh")
            // 覆盖
        void he();
    }

    interface X extends Y {
    }

    static class C1 {
        @ABC("c1")
        public void print() {
        }
    }

    static class C2 extends C1 {
        @Override
        public void print() {
        }
    }

    public static void main(String[] args) throws NoSuchMethodException, NoSuchFieldException {
        System.out.println(X.class.getAnnotation(InheritedAnnotationType.class)); // jdk8, interface无法继承
        System.out.println(Y.class.getAnnotation(InheritedAnnotationType.class)); // interface本身有
        System.out.println(Z.class.getAnnotation(InheritedAnnotationType.class)); // interface本身没有
        System.out.println("_________________________________");
        System.out.println(X.class.getAnnotation(UnInheritedAnnotationType.class)); // 1.interface无法继承 2.UnInheritedAnnotationType没有继承属性
        System.out.println(Y.class.getAnnotation(UnInheritedAnnotationType.class)); // 1.interface无法继承 2.UnInheritedAnnotationType没有继承属性
        System.out.println(Z.class.getAnnotation(UnInheritedAnnotationType.class)); // interface本身有
        System.out.println("_________________________________");
        System.out.println(Arrays.toString(Z.class.getMethod("he").getAnnotations())); // 方法本身有
        System.out.println(Arrays.toString(Y.class.getMethod("he").getAnnotations())); // 方法覆盖了父类
        System.out.println(Arrays.toString(X.class.getMethod("he").getAnnotations())); // 继承得来


        System.out.println(Arrays.toString(C1.class.getMethod("print").getAnnotations())); // 方法本身有
        System.out.println(Arrays.toString(C2.class.getMethod("print").getAnnotations())); // 重写了方法, 但是方法上面没有注解, 所以没有  只要是重写了并且没有显式写出来, 都无法继承了
    }
}
