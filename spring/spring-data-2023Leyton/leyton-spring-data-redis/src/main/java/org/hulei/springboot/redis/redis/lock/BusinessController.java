// package org.hulei.springboot.redis.redis.lock;
//
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RestController;
//
// @Slf4j
// @RestController
// public class BusinessController {
//
//     private final LockService lockService;
//     private static final String ORDER_LOCK_PREFIX = "order_lock:";
//
//     public BusinessController(LockService lockService) {
//         this.lockService = lockService;
//     }
//
//     @GetMapping("/createOrder")
//     public String createOrder(String orderId) {
//         String lockKey = ORDER_LOCK_PREFIX + orderId;
//         String lockId = null;
//         try {
//             // 尝试获取锁（3000ms过期，200ms重试间隔，最多重试3次）
//             lockId = lockService.acquireLockWithRetry(lockKey, 3000);
//
//             // 获取锁成功，执行业务逻辑
//             return doBusiness(orderId);
//         } catch (LockService.LockFailedException e) {
//             return "系统繁忙，请稍后再试";
//         } finally {
//             // 释放锁
//             if (lockId != null) {
//                 boolean unlock = lockService.getRedisLock().unlock(lockKey, lockId);
//                 if (unlock) {
//                     log.info("成功释放锁id为：{}", lockId);
//                 }
//             }
//         }
//     }
//
//     private String doBusiness(String orderId) {
//         // 模拟业务处理
//         try {
//             Thread.sleep(1000);
//         } catch (InterruptedException e) {
//             Thread.currentThread().interrupt();
//         }
//         return "订单创建成功: " + orderId;
//     }
// }