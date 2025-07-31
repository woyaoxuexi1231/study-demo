package org.hulei.basic.jdk.generic.work;

import java.util.Collections;
import java.util.List;
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
        genericExtendTest.processData(new DataTransferReq<>());
    }

    // public static void extendTest(DataTransfer<>)


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

    /**
     * 这是一个之前开的流程中出现一个比较复杂的泛型使用场景
     * 入参是一个DataTransferReq类型的一个上下文,这个上下文在整个业务处理过程可能会有内部数据的变化,他是伴随整个业务处理的生命周期的
     * 在中间可以看到
     * <p>
     * 1. 我们的入参是比较简单的,他仅仅要求只接收一个 DataTransferReq<S> 的参数, 但是这个S也是有要求的
     * 2. 方法是没有返回的东西的,但是方法规定了三种泛型类型, S,T,D 并且表面上看,D和S,T还存在某种关联,因为 D的上界必须时一个 泛型参数为 S,T的DataTransfer
     *
     * @param transferReq 入参
     * @param <S>         泛型参数 S
     * @param <T>         泛型参数 T
     */
    public <S, T> void processData(DataTransferReq<S> transferReq) {

        // 在实际业务中,我这里需要去获取一个function接口对象,他内部是一堆业务执行逻辑,在后面需要用到
        // 我这里要求getSingleRefreshProcess这个方法返回的结果,是有用到S,D的
        // 到这里,其实还S,D,还没有啥关联的地方
        Function<DataTransferReq<S>, DataTransfer<S, T>> singleRefreshProcess = getSingleRefreshProcess();
        singleDataProcess(
                transferReq,
                singleRefreshProcess,
                getSingleRefreshPushMsgProcess());

    }


    public <S, T> Function<DataTransferReq<S>, DataTransfer<S, T>> getSingleRefreshProcess() {
        return (req) -> {
            List<S> sourceList = req.getSourceList();
            // 这里应该还有一个转换的方法,这里不写
            List<T> list = Collections.emptyList();
            DataTransfer<S, T> transfer = new DataTransfer<>();
            transfer.setSourceList(sourceList);
            transfer.setTargetList(list);

            SubDataTransfer<S, T> subDataTransfer = new SubDataTransfer<>();

            // return transfer;
            return subDataTransfer;
        };
    }

    public <S, T, D extends DataTransfer<S, T>> Consumer<D> getSingleRefreshPushMsgProcess() {
        return transfer -> {
        };
    }

    public <S, T> void singleDataProcess(
            DataTransferReq<S> transferReq,
            Function<DataTransferReq<S>, DataTransfer<S, T>> subProcess,
            Consumer<DataTransfer<S, T>> msgProcess) {
        DataTransfer<S, T> apply = subProcess.apply(transferReq);
        msgProcess.accept(apply);
    }

}
