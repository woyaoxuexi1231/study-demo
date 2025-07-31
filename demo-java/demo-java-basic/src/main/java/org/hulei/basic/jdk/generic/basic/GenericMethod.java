package org.hulei.basic.jdk.generic.basic;

import org.hulei.basic.jdk.generic.work.DataTransferReq;

/**
 * @author hulei
 * @since 2024/9/15 15:12
 */

public class GenericMethod {

    public static void main(String[] args) {
        DataTransferReq<GenericSimpleObject> dataTransferReq = new DataTransferReq<>();
        extendTest(dataTransferReq);
    }

    /**
     * 这里是一个使用 extends 的场景,我们需要在方法内部使用到 GenericSimpleObject这个边界的方法print
     * 所以这里规定泛型D的上界是GenericSimpleObject,而当类型擦除的时候,会擦除到他的第一个边界,也就是GenericSimpleObject
     * 所以在这里才可以安全的调用print方法而不会报错
     *
     * @param req req
     * @param <D> GenericSimpleObject或其子类
     */
    public static <D extends GenericSimpleObject> void extendTest(DataTransferReq<D> req) {
        for (D d : req.getSourceList()) {
            d.print();
        }
    }

}

