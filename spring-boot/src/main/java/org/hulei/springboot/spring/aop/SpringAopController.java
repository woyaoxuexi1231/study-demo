// package org.hulei.springboot.spring.aop;
//
// import org.hulei.springboot.spring.aop.service.impl.AopServiceImpl;
// import org.hulei.springboot.spring.aop.service.impl.AopServiceSubImpl;
// import org.hulei.springboot.spring.aop.service.impl.AopServiceWithOutInterface;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
//
// /**
//  * @ProductName: Hundsun amust
//  * @ProjectName: study-demo
//  * @Package: com.hundsun.demo.springboot.controller
//  * @Description:
//  * @Author: hulei42031
//  * @Date: 2024-02-21 15:20
//  * @UpdateRemark:
//  * @Version: 1.0
//  * <p>
//  * Copyright 2023 Hundsun Technologies Inc. All Rights Reserved
//  */
//
// @RestController
// @RequestMapping("/aop")
// public class SpringAopController {
//
//     /**
//      * 有接口实现的bean
//      */
//     @Autowired
//     AopServiceImpl aopServiceImpl;
//
//     @Autowired
//     AopServiceSubImpl aopServiceSubImpl;
//     /**
//      * 没有实现接口的一个bean
//      */
//     @Autowired
//     AopServiceWithOutInterface aopServiceWithOutInterface;
//
//     @GetMapping("/test")
//     public void test() {
//         // 代理的实现不会拘泥于是否有接口实现
//         // aopServiceImpl.print();
//         aopServiceSubImpl.print();
//         // aopServiceWithOutInterface.print();
//     }
// }
