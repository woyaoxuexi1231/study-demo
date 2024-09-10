// package com.hundsun.demo.springboot.lock;
//
// import redis.clients.jedis.Jedis;
// import redis.clients.jedis.exceptions.JedisConnectionException;
// import redis.clients.jedis.params.SetParams;
//
// import java.util.List;
// import java.util.concurrent.TimeUnit;
// import java.util.concurrent.locks.Condition;
// import java.util.concurrent.locks.Lock;
//
// /**
//  * 使用分布式缓存（如Redis）的<b>原子操作（如SETNX）</b>来实现分布式锁。
//  * <p>
//  * 优点：性能较高，易于实现。
//  * <p>
//  * 缺点：可能会出现死锁[无超时时间导致的]，无法保证强一致性和锁的释放可能不及时[有超时时间导致的]。
//  */
//
// public class RedisLock implements Lock {
//
//     private final Jedis jedis;
//
//     public RedisLock() {
//         // 连接到 Redis 服务器
//         Jedis jedis = new Jedis("192.168.80.128", 6379);
//         jedis.auth("123456");
//         this.jedis = jedis;
//     }
//
//     public void close() {
//         jedis.close();
//     }
//
//     /**
//      * 阻塞式获取锁。
//      *
//      * @param timeout 超时时间，单位毫秒
//      * @return 是否成功获取到锁
//      * @throws InterruptedException 当线程被中断时抛出
//      */
//     public boolean blockingGetLock(long timeout) throws InterruptedException {
//         long start = System.currentTimeMillis();
//         while (System.currentTimeMillis() - start < timeout) {
//             try {
//                 // 如果锁不存在，BLPOP会在指定的列表上阻塞指定的毫秒数，直到有元素被推入列表
//                 // 在这里，我们用一个空的列表来模拟阻塞行为，因为锁的值就是列表中的元素
//                 List<String> response = jedis.blpop(0, "my_lock");
//                 if (response != null && response.size() > 0 && response.get(0).equals(LOCK_VALUE)) {
//                     // 设置锁的过期时间，防止死锁
//                     jedis.expire(LOCK_KEY, LOCK_TIMEOUT / 1000);
//                     return true;
//                 }
//             } catch (JedisConnectionException e) {
//                 // 处理连接异常，可能需要重试或抛出异常
//                 handleConnectionException(e);
//             }
//         }
//         return false;
//     }
//
//
//     public static void main(String[] args) {
//
//         RedisLock lock = new RedisLock();
//
//         boolean isLocked = false;
//         try {
//             // 1. 尝试获取锁
//             if (lock.acquireLock()) {
//                 isLocked = true;
//                 System.out.println("获取锁成功，执行业务逻辑...");
//                 Thread.sleep(15000);
//                 // 在这里执行业务逻辑
//             } else {
//                 System.out.println("获取锁失败，未能执行业务逻辑");
//             }
//         } catch (Exception e) {
//             throw new RuntimeException(e);
//         } finally {
//             // 2. 只有在成功获取锁才释放锁
//             if (isLocked) {
//                 lock.releaseLock();
//             }
//             lock.close();
//         }
//     }
//
//     @Override
//     public void lock() {
//
//     }
//
//     @Override
//     public void lockInterruptibly() throws InterruptedException {
//
//     }
//
//     @Override
//     public boolean tryLock() {
//         // .nx() 只在不存在的时候进行设置 .px() 设置键的过期时间(30*1000 ms)
//         SetParams params = SetParams.setParams().nx().px(30000);
//         String result = jedis.set("my_lock", "locked", params);
//         return "OK".equals(result);
//     }
//
//     @Override
//     public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
//         return false;
//     }
//
//     @Override
//     public void unlock() {
//         jedis.del("my_lock");
//     }
//
//     @Override
//     public Condition newCondition() {
//         return null;
//     }
// }
//
// /*
// 这段代码使用的是 Redis 的 `SET` 指令和 Java 客户端 Jedis 来设置一个键值对（key-value pair），并附带了一些参数来控制它的行为。具体来说：
//
// 1. **键（Key）**：`"my_lock"`
// 2. **值（Value）**：`"locked"`
// 3. **参数（Params）**：
//    - `nx()`: 仅当键不存在时才进行设置，即 "Not Exists"。
//    - `px(30000)`: 设定键的过期时间为 30000 毫秒（即 30 秒）。
//
// `String result = jedis.set("my_lock", "locked", params);` 这一行代码会返回 Redis 命令执行的结果，如果设置成功，返回值是 `"OK"`，否则返回 `null`。
//
// ` return "OK".equals(result);` 这一行代码则是用来判断是否设置成功，如果成功返回 `true`，否则返回 `false`。
//
// ### 使用这种方式和 Lua 脚本在 Redis 执行过程中的区别：
//
// #### 1. **操作原子性**
// - **Jedis `SET` 方法**：`SET` 指令本身是一个原子操作，即这些操作（设置值、条件检查和设置过期时间）是以原子方式一起执行的。在大多数情况下，这足以满足简单的分布式锁需求，但在更复杂的情景下（例如需要同时检查和操作多个键），可能会不如 Lua 脚本灵活。
// - **Lua 脚本**：Lua 脚本是完全原子的。在 Redis 中，整个 Lua 脚本会被作为一个单一的 Redis 命令来执行。因此，脚本内的一系列指令要么全部执行成功，要么全部不执行。这对处理复杂的事务逻辑非常有用。
//
// #### 2. **复杂性和灵活性**
// - **Jedis `SET` 方法**：使用 Jedis 提供的参数化 `SET` 方法相对简单和直接，适合处理单个键的原子操作。
// - **Lua 脚本**：Lua 脚本更为灵活，适用于复杂逻辑和需要操作多个键的情况。脚本可以包含任意的 Redis 命令和条件逻辑，适合在一个操作中完成多步操作。
//
// #### 3. **性能**
// - **Jedis `SET` 方法**：这是一条普通的 Redis 命令，性能表现和 Redis 其他命令类似。
// - **Lua 脚本**：相对来说，Lua 脚本的执行时间更长，因为它可能包含多个操作与逻辑判断。但由于它在 Redis 层面是一个命令，所以可以减少网络往返时间的开销。
//
// #### 4. **重用性**
// - **Jedis `SET` 方法**：直接使用方便，但逻辑固定，缺乏重用性。
// - **Lua 脚本**：脚本可以存储在 Redis 服务器上，并通过哈希 key 调用，便于重用和维护。
//
// ### 结论
// - 对于简单的分布式锁和基本的键操作，Jedis 提供的 `SET` 方法结合参数足够且高效。
// - 如果需要更复杂的操作逻辑或者必须保证更高的原子性和一致性，Lua 脚本是更优的选择。
//  */
