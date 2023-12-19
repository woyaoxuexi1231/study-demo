// package com.hundsun.demo.java.jdk.generic;
//
// import java.util.function.Consumer;
// import java.util.function.Function;
//
// /**
//  * @ProductName: Hundsun amust
//  * @ProjectName: study-demo
//  * @Package: com.hundsun.demo.java.jdk.generic
//  * @Description:
//  * @Author: hulei42031
//  * @Date: 2023-12-19 14:22
//  * @UpdateRemark:
//  * @Version: 1.0
//  * <p>
//  * Copyright 2023 Hundsun Technologies Inc. All Rights Reserved
//  */
//
// public class GenericExtendTest2 {
//     public <S, D extends DataTransfer<S>> void processData(DataTransferReq<S> transferReq) {
//         Function<DataTransferReq<S>, D> singleRefreshProcess = getSingleRefreshProcess();
//         Consumer<D> singleRefreshPushMsgProcess = getSingleRefreshPushMsgProcess();
//         singleDataProcess(transferReq, singleRefreshProcess, singleRefreshPushMsgProcess);
//     }
//
//     public <S, D extends DataTransfer<S>> Function<DataTransferReq<S>, D> getSingleRefreshProcess() {
//         return standDataTransferReq -> null;
//     }
//
//     public <S, D extends DataTransfer<S>> Consumer<D> getSingleRefreshPushMsgProcess() {
//         return transfer -> {
//         };
//     }
//
//     public <S, D extends DataTransfer<S>> void singleDataProcess(
//             DataTransferReq<S> transferReq,
//             Function<DataTransferReq<S>, D> subProcess,
//             Consumer<D> msgProcess) {
//         D apply = subProcess.apply(transferReq);
//         msgProcess.accept(apply);
//     }
//
//     class DataTransferReq<T> {
//
//     }
//
//     class DataTransfer<S> {
//
//     }
// }
