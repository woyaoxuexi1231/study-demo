package org.hulei.jdk.root.generic;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.java.jdk.generic
 * @Description:
 * @Author: hulei42031
 * @Date: 2023-12-19 10:56
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright 2023 Hundsun Technologies Inc. All Rights Reserved
 */

public class GenericExtendTest {

    /*
    当使用通配符进行泛型约束时，? extends D 和 ? super D 是两种不同的通配符限定方式。

    ? extends D: 通配符 ? 表示未知类型，而 extends 关键字则表示限定类型的上界。
    ? extends D 表示可以是 D 类型或者 D 的子类类型。
    通过使用 ? extends D，我们可以保证获取的值是 D 类型或者 D 的子类类型，但我们不能对这个类型进行写操作（添加元素），因为编译器无法确定具体的类型。

    ? super D: 通配符 ? 表示未知类型，而 super 关键字则表示限定类型的下界。
    ? super D 表示可以是 D 类型或者 D 的父类类型。
    通过使用 ? super D，我们可以保证接受的值是 D 类型或者 D 的父类类型，可以对这个类型进行写操作（添加元素），因为保证了具体类型是 D 的父类。
     */

    public static void main(String[] args) {
        GenericExtendTest genericExtendTest = new GenericExtendTest();
        genericExtendTest.processData2(new DataTransferReq<>());
    }


    // public <S> void processData(DataTransferReq<S> transferReq) {
    //     /*
    //     Inferred type 'D' for type parameter 'D' is not within its bound;
    //     should extend 'com.hundsun.demo.java.jdk.generic.GenericExtendTest.DataTransfer<S,java.lang.Object>'
    //
    //     推断类型 D 并不在其边界内，应该扩展自 com.hundsun.demo.java.jdk.generic.Test.DataTransfer<S,java.lang.Object>。
    //     这是因为在 getSingleRefreshProcess 和 getSingleRefreshPushMsgProcess 方法中，返回的泛型类型 D 并没有使用 T 的约束，导致类型推断出现问题。
    //      */
    //     singleDataProcess(
    //             transferReq,
    //             getSingleRefreshProcess(),
    //             getSingleRefreshPushMsgProcess());
    // }

    public <S, T, D extends DataTransfer<S, T>> void processData2(DataTransferReq<S> transferReq) {
        Function<DataTransferReq<S>, D> singleRefreshProcess = getSingleRefreshProcess();
        DataTransfer<S, T> dataTransfer = new SubDataTransfer<>();
        DataTransferReq<Integer> transferReq2 = new DataTransferReq<>();
        Function<DataTransferReq<Integer>, SubDataTransfer<Integer, Integer>> test = test();
        singleDataProcess(
                transferReq2,
                test,
                getSingleRefreshPushMsgProcess());

    }

    public <S, T, D extends DataTransfer<S, T>> Function<DataTransferReq<S>, D> getSingleRefreshProcess() {
        return standDataTransferReq -> null;
    }

    public Function<DataTransferReq<Integer>, SubDataTransfer<Integer, Integer>> test() {
        return integerDataTransferReq -> null;
    }

    public <S, T, D extends DataTransfer<S, T>> Consumer<D> getSingleRefreshPushMsgProcess() {
        return transfer -> {
        };
    }

    public <S, T, D extends DataTransfer<S, T>> void singleDataProcess(
            DataTransferReq<S> transferReq,
            Function<DataTransferReq<S>, D> subProcess,
            Consumer<D> msgProcess) {
        D apply = subProcess.apply(transferReq);
        msgProcess.accept(apply);
    }

    static class DataTransferReq<T> {

    }

    static class DataTransfer<S, T> {

    }

    static class SubDataTransfer<S, T> extends DataTransfer<S, T> {

    }
}
