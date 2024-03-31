package com.hundsun.demo.spring.jdk.generic;


import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.java.jdk.generic
 * @Description:
 * @Author: hulei42031
 * @Date: 2023-12-19 13:44
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright 2023 Hundsun Technologies Inc. All Rights Reserved
 */

public class GenericSuperTest {

    public <S> void doProcess(DataTransferReq<S> transferReq) {

        singleDataProcess(transferReq);

        singleDataProcess(transferReq, getSingleRefreshPushMsgProcess());

        singleDataProcess(transferReq, this.getSingleRefreshProcess());

        singleDataProcess(
                transferReq,
                this.getSingleRefreshProcess(),
                this.getSingleRefreshPushMsgProcess());
    }

    public <S, T, D extends DataTransfer<S, ? super T>> Function<DataTransferReq<S>, ? super D> getSingleRefreshProcess() {
        return standDataTransferReq -> null;
    }

    public <S, T, D extends DataTransfer<S, ? super T>> Consumer<? super D> getSingleRefreshPushMsgProcess() {
        return transfer -> {
        };
    }

    public <S, T, D extends DataTransfer<S, ? super T>> void singleDataProcess(
            DataTransferReq<S> transferReq,
            Function<DataTransferReq<S>, D> subProcess,
            Consumer<D> msgProcess) {
    }

    public <S, T, D extends DataTransfer<S, ? super T>> void singleDataProcess(
            DataTransferReq<S> transferReq) {
    }

    public <S, T, D extends DataTransfer<S, ? super T>> void singleDataProcess(
            DataTransferReq<S> transferReq,
            Consumer<D> msgProcess) {
    }

    public <S, T, D extends DataTransfer<S, ? super T>> void singleDataProcess(
            DataTransferReq<S> transferReq,
            Function<DataTransferReq<S>, D> subProcess) {
    }

    static class DataTransferReq<T> {

    }

    static class DataTransfer<S, T> {

    }
}
