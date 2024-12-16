// package com.hundsun.demo.dubbo.consumer.filter;
//
// import com.hundsun.demo.commom.core.model.dto.ResultDTO;
// import com.hundsun.demo.commom.core.utils.ResultDTOBuild;
// import lombok.extern.slf4j.Slf4j;
// import org.aspectj.lang.ProceedingJoinPoint;
// import org.aspectj.lang.annotation.Around;
// import org.aspectj.lang.annotation.Aspect;
// import org.aspectj.lang.annotation.Pointcut;
// import org.springframework.stereotype.Component;
//
// /**
//  * @projectName: study-demo
//  * @package: com.hundsun.demo.dubbo.consumer.filter
//  * @className: ResultResolveFilter
//  * @description:
//  * @author: h1123
//  * @createDate: 2023/2/19 0:20
//  */
//
// // @Component
// @Aspect
// @Slf4j
// public class ResultResolveFilter {
//
//     @Pointcut(value = "execution(* com.hundsun.demo.dubbo.consumer.controller.*.*(..))")
//     public void pointCut() {
//     }
//
//     @Around(value = "pointCut()")
//     public ResultDTO<?> around(ProceedingJoinPoint joinPoint) {
//         try {
//             return (ResultDTO<?>) joinPoint.proceed();
//         } catch (Throwable e) {
//             log.error("请求报错! ", e);
//             return ResultDTOBuild.resultErrorBuild(e.getMessage());
//         }
//     }
// }
