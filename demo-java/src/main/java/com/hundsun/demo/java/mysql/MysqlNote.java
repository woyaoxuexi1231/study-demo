package com.hundsun.demo.java.mysql;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.mysql
 * @className: MysqlNote
 * @description:
 * @author: h1123
 * @createDate: 2023/3/5 13:41
 */

public class MysqlNote {

    /*
    undo日志 - 可以保证事务的原子性和持久性, 但是磁盘 I/O 有点大
        首先把数据备份到 undo_log, 然后进行数据的修改, 如果出现错误或者用户执行了 rollback 语句, 系统可以利用 undo_log 中的备份把数据恢复到事务开始之前的状态
            假如有两个数据 A=1, B=2
            1. 事务开始
            2. 记录 A=1 到 undo_log_buffer
            3. 修改 A=3
            4. 记录 B=2 到 undo_log_buffer
            5. 修改 B=4
            6. 将 undo_log 写到磁盘
            7. 将数据写到磁盘
            8. 事务提交 - 也就是说在事务一旦提交成功, 那就必然数据已经持久化了
            如果在 7-8 之间宕机, 开机后在 undo_log 读取数据进行回滚, 在 7 之前宕机没关系, 数据都是在内存中的
    redo_log - 记录新数据的备份, 在事务提交前只持久化 redo_log, 不持久化数据, 减少 I/O 的次数
            假如有两个数据 A=1, B=2
            1. 事务开始
            2. 记录 A=1 到 undo_log_buffer
            3. 修改 A=3
            4. 记录 A=3 到 redo_log_buffer
            5. 记录 B=2 到 undo_log_buffer
            6. 修改 B=4
            7. 记录 B=4 到 redo_log_buffer
            8. 将 undo_log 写到磁盘
            9. 将 redo_log 写到磁盘(事务提交前, 或者 redo_log_buffer 满了) - 顺序写
            10. 事务提交
            11. 异步写数据 - 随机写, 需要寻址, 效率低, mysql表结构的索引数据结构决定了数据库数据的随机写
            如果事务提交后宕机, 数据还没写入磁盘, 利用 redo_log 持久化更新后的数据
            如果 redo_log 中记录的数据, 包括了未提交的事务, 如果此时数据库崩溃, 那么如何完成数据恢复
                两种策略
                1. 恢复时, 只重做已经提交的事务
                2. 恢复时, 重做所有事务包括未提交的事务和回滚的事务, 然后通过 undo_log 回滚那些未提交的事务
                innodb 采用第二种方案, 因此 undo_log 在 redo_log 之前持久化


    innodb锁机制
        共享锁 S LOCK - 允许事务读取一行数据, 可以与其他共享锁兼容
        排他锁 X LOCK - 允许事务删除或者更新一行数据, 与其他锁都不兼容
        意向锁 Intention Lock - 表级别的锁, 设计目的主要是为了在一个事务中揭示下一行将被请求的锁类型
        一致性非锁定读 - 就是快照读, 快照数据是通过 undo 段来完成
        一致性锁定读 - 即某些情况下用户需要显式的对数据库读取操作进行加锁以保证数据逻辑的一致性
            select for update - 对读取的记录加一个 X 锁, 其他事务不能对已锁定的行加上任何锁
            select lock in share mode - 对锁定的记录加一个 S 锁, 其他事务可以加 S 锁, 不能加 X 锁

        MVCC - 多版本并发控制
        快照读 - 普通的 select查询 SQL语句
        当前读 - 执行 insert, update, delete, select for update, select lock in share mode时进行读取数据的方式

        ReadView - 快照读sql 执行时 mvcc提取数据的依据, 就是在执行查询 sql的时候一个事务表, 通过事务表和数据保存的事务信息, 来判断当前的 sql应该获取哪个时刻的数据才是安全的
            m_ids - 当前活跃的事务编号, 还没有被提交的事务
            min_trx_id - 最小活跃事务编号
            max_trx_id - 预分配事务编号, 当前最大的事务编号 + 1
            creator_trx_id - ReadView创建者的事务编号
        可重复读和读提交 - 基于 undo_log 的版本链, 在表内会额外的增加两个字段 trx_id(这数据属于哪个事务编号, 修改操作的事务编号), db_roll_ptr(指向上一个进行版本变化时的数据镜像)
            1. 判断当前事务编号是否等于 creator_trx_id, 成立说明是当前事务更改的, 直接返回 - 也就是说, 第一步判断当前数据库的数据是不是当前事务所修改的, 自己修改的数据自己肯定可以访问的
            2. 判断 trx_id < min_trx_id, 成立说明数据已经提交, 可以返回 - 这个说明我们的 ReadView 最小的事务都要比这个数据的事务小, 对于当前的我们来说, 这个数据百分百安全
            3. 判断 trx_id > max_trx_id, 成立则说明事务是在 ReadView 生成之后才开启的, 不允许访问数据 - 当前数据库里的数据, 是在我们的 ReadView 生成之后才被提交过, 我们不能在过去查看未来的数据, 所以是不可以用这个数据的
            4. 判断 min_trx_id <= trx_id <= max_trx_id, 成立则在 m_ids 对比, 如果不存在数据则代表是已提交的, 可以访问 - 这个数据如果是在最小事务和最大事务之间修改的, 就是说这个数据是在这个 ReadView 被创建的期间被改的, 如果不在活跃事务里, 就说明数据对于当前的 ReadView 来说安全, 当然可以访问
            读提交(RC)每次都会生成 ReadView 来获取数据, 也就意味着, 在同一个事务内, 会出现不可重复读
            可重复读(RR)仅在第一次会生成 ReadView, 之后都会复用这个 ReadView, 所以这意味着避免了不可重复读和幻读
                但是有特例, 在同一个事务内, 两次快照读中间穿插一次当前读会导致幻读, 这种情况, 会在第二次快照读的时候重新创建 ReadView
                案例: 在第一次查询的时候创建 ReadView, 然后事务二新插入了一条数据并提交成功, 事务一更新全表数据的某个字段, 然后执行查询操作这时就产生了幻读

        自增长与锁
            innodb 中对每个含有自增长值的表都有一个自增长计数器, 当对含有自增长的计数器的表进行插入操作的时候, 这个计数器会被初始化:
                select max(auto_inc_col) from t for update
                插入操作会依据这个自增长的计数器值加 1 赋予自增长列, 这个实现方式叫做 AUTO-INC Locking, 这个锁会在事务内的每一个 insert语句执行完之后释放, 而不是在整个事务结束时释放
            innodb_autoinc_lock_mode 参数
                0 - 对于所有的 insert 都采用 AUTO-INC Locking, 会持有锁一直到语句结束
                1 - 对于 simple insert 采用 mutex 对计数器进行访问, 拿到释放锁, 这个对于复制来说是安全的, 可以一次性拿到确定数目的自增 ID
                2 - 对于所有类型的 insert 都采用 mutex 来, 性能最好, 但是不安全, 对于同一个 insert 来说, 得到的 auto_increment 可能不是连续的, 这是因为如果基于语句复制, 在数据库 A执行的时候 语句 1先获得 id, 语句 2后获得 id, 而在数据库 B执行的时候是语句 2先获得 id, 所以会导致数据不一致
            Q: id什么时候会出现自增不连续?
            A: 1. 插入的事务回滚会导致不连续(包括主动回滚和主键冲突报错) 2. 批量插入, 这个由 mysql自增 ID获取策略导致, id申请呈 2的指数呗增长, 插入的数据非 2的倍数时会导致 id不连续自增
            其他 id 的生成方法: 雪花算法 //todo - 2023/03/07
            Q: 自增 id用完了怎么办
            A: 1. 改自增 id的类型, int 可以改为 bigint  2. 如果没有设置自增 id, 那么问题比较麻烦, 数据库会默认使用自己生成的一个 row_id, 超出后会从 0开始计算, 导致新插入的数据覆盖原有的数据

        外键与锁
            todo

         锁的算法
            Record Lock - 单个行记录上的锁, 会锁定索引记录
            Gap Lock - 间隙锁, 锁定一个范围, 但不包含记录本身
                间隙锁的目的是为了防止幻读，其主要通过两个方面实现这个目的：
                （1）防止间隙内有新数据被插入
                （2）防止已存在的数据，更新成间隙内的数据
            Next-Key Lock - Record Lock + Gap Lock, 锁定一个范围, 并且锁定记录本身
            mysql 默认使用 Next-Key Lock 算法
            当有唯一索引的时候, innodb 引擎会自动把 Next-Key Lock 降级为 Record Lock

     */

}
