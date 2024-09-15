package org.hulei.jdk.root.generic;

import org.apache.poi.ss.formula.functions.T;

/**
 * 泛型是实现了参数化类型的概念,参数化类型就是允许在类,接口,或者方法中使用占位符来代表某种具体类型,直到实例化或者调用的时候再指定具体的类型
 * 为什么要定义泛型呢?
 * 1. 对于只能持有单个对象的类,如果明确指定了其持有对象的类型,那这个类的重用性就会很低
 * 2. 在泛型出现之前呢,我们可以把这个持有的对象的类型改为Object,但是这又出现了另一个问题,通常我们只想让容器存储同一种类型的对象
 * 所以泛型的主要目的就是指定容器(不仅仅是容器,在使用泛型的类型)能够只有我们指定的对象,并且由编译器来保证类型的正确性
 *
 * @author hulei
 * @since 2024/9/15 14:40
 */

public class GenericBasic {

    public static void main(String[] args) {
        // 这样我们获取是没有任何问题的,因为我们没有进行任何操作,也不涉及通配符的运算
        MethodResp<?> method = method(new MethodReq<>());

        MethodResp<?> methodResp = method2(new MethodReq<>());
    }

    /**
     * 对于入参来说,使用了?作为通配符,表示我们接收一个MethodReq类型的参数,但是不关心内部的泛型具体是什么类型
     * 对于出餐来说,我们只是返回一个包含?通配符的MethodResp类型,也不管具体类型是什么
     *
     * @param req req
     * @return rsp
     */
    public static MethodResp<?> method(MethodReq<?, ?> req) {
        return new MethodResp<String>() {
            public void print() {
                System.out.println("这是一个泛型类型为String的MethodResp");
            }
        };
    }

    /**
     * 入参我们要求
     * 1.必须是一个MethodReq类型的对象
     * 2.这个对象的第一个泛型类型他必须是以GenericBasicInterface为上界,意思就是说,至少要是GenericBasicInterface,或者是其子类型
     *
     * @param req req
     * @return rsp
     */
    public static MethodResp<?> method2(MethodReq<? extends GenericBasicInterface<?>, ?> req) {
        return new MethodResp<String>() {
            public void print() {
                System.out.println("这是一个泛型类型为String的MethodResp");
            }
        };
    }
}

class MethodReq<T, U> {

}

class MethodResp<rsp> {

}

interface GenericBasicInterface<T> {
    void print();
}
