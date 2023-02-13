package com.hundsun.demo.spring.jdbc;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.spring.jdbc
 * @className: TransactionTest
 * @description:
 * @author: h1123
 * @createDate: 2023/2/13 21:50
 */

public class TransactionTest {

    /*
    Java 事务


    > 四种事务的隔离级别
    > 1. Read uncommitted(读未提交) -- 可能出现脏读,不可重复读
    > 2. Read committed(读提交) -- 避免了脏读,出现了不可重复读
    > 3. Repeatable read(可重复读取) -- 避免了不可重复读,可能出现幻读
    > 4. Serializable(可序化,串行化) -- 避免了脏读,不可重复读和幻读
    >
    > 丢失更新 : 两个事务同时针对统一数据进行修改, 会存在数据丢失的情况
    > 脏读 : 对于两个事务, t1 读取到了 t2 未提交的数据, 之后如果 t2 回滚了,导致 t1 读取的数据是无效的
    > 不可重复读 : 对于两个事务, t1 读取到了某一个数据, 但是之后 t2 进行了更新, 导致 t1 再次读取这个数据的时候, 这个数据与之前读取的不一致
    > 幻读 : 对于两个事务, t1 通过条件读取到了某些数据, 但是之后 t2 进行了插入操作, 导致 t1 再次通过同一个条件或者数据的时候, 发现获取到的数据比之前的数据多了
     */
}
