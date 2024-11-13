// package org.hulei.springboot.spring.transactional;
//
// import lombok.SneakyThrows;
// import org.hulei.common.mapper.entity.pojo.EmployeeDO;
// import org.hulei.common.mapper.mapper.EmployeeMapperPlus;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.transaction.annotation.Transactional;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
//
// import java.util.concurrent.ThreadPoolExecutor;
//
// /**
//  * @author hulei
//  * @since 2024/9/17 23:21
//  */
//
// @SuppressWarnings("CallToPrintStackTrace")
// @RestController
// @RequestMapping("/spring_transactionlost")
// public class TransactionLostController {
//
//     @Autowired
//     ThreadPoolExecutor commonPool;
//
//     @Autowired
//     TransactionLostController controller;
//
//     @Autowired
//     EmployeeMapperPlus employeeMapperPlus;
//
//     /**
//      * 测试注解失效的场景
//      * <p>
//      * `@Transactional` 注解可能会失效的一些场景包括：<p>
//      * 1. 类没有在spring的IOC容器中,这样spring无法生成事务管理的代理对象,也就失效了: 可能类是new出来的,没有使用bean声明
//      * 2. 方法不是公共方法或者被final,static修饰,意味着无法override,代理对象也就无法override这个方法,不过 2024.1.6的IDEA是不允许这种行为的,编译器会提示报错
//      * 3. 异常未被正确捕获,默认捕获RuntimeException,其他异常是不会触发回滚的.所以需要配置rollbackFor参数
//      * 请确保在正确的场景下使用 `@Transactional` 注解，并合理配置事务的传播行为、隔离级别和异常处理，以确保事务生效。
//      */
//     @SneakyThrows
//     @GetMapping("/transactionalLost")
//     public void transactionalLost() {
//         // 1. 事务会失效,这里的仅仅只是另起了一个线程然后调用了 run方法, 并没有生成可以进行事务管理的代理对象和逻辑
//         // commonPool.execute(getSimpleRunnable());
//
//         // 2. 事务会失效,默认捕获RuntimeException,但是方法抛出了一个非免检异常,并且没有指定需要回滚这种异常,所以这里会失效
//         commonPool.execute(() -> {
//             try {
//                 controller.runWithException();
//             } catch (CustomException e) {
//                 e.printStackTrace();
//             }
//         });
//     }
//
//     public Runnable getSimpleRunnable() {
//         return new Runnable() {
//             @Transactional
//             @Override
//             public void run() {
//                 EmployeeDO employeeDO = new EmployeeDO();
//                 employeeDO.setEmployeeNumber(1002L);
//                 employeeDO.setFirstName("getTransactionLostTask");
//                 employeeMapperPlus.updateById(employeeDO);
//                 throw new RuntimeException("阻止提交");
//             }
//         };
//     }
//
//     @Transactional
//     public void runWithException() throws CustomException {
//         EmployeeDO employeeDO = new EmployeeDO();
//         employeeDO.setEmployeeNumber(1002L);
//         employeeDO.setFirstName("runWithException");
//         employeeMapperPlus.updateById(employeeDO);
//         throw new CustomException("this is a check exception");
//     }
//
// }
