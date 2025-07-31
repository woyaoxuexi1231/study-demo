# mysql默认有四个系统库
# information_schema 数据库、表、列、索引、权限等元数据（如`TABLES`、`COLUMNS`、`SCHEMATA`表）。
# mysql 存储用户账户、权限、系统配置及核心数据。用户与权限表（如`user`、`db`、`tables_priv`）。
# performance_schema 收集MySQL服务器的运行时性能数据，用于监控与分析。
# sys 基于`information_schema`和`performance_schema`的预定义视图（如`schema_table_statistics`）。常用性能指标汇总（如高负载查询、索引使用情况）。



# 监控 row_lock_waits 指标
show status like 'row_lock_waits%';
# 分析 processlist 中的等待状态
show full processlist;


-- 查看当前锁等待关系
SELECT r.trx_id                                         AS waiting_trx_id,
       r.trx_mysql_thread_id                            AS waiting_thread,
       r.trx_query                                      AS waiting_query,
       b.trx_id                                         AS blocking_trx_id,
       b.trx_mysql_thread_id                            AS blocking_thread,
       b.trx_query                                      AS blocking_query,
       TIMESTAMPDIFF(SECOND, r.trx_wait_started, NOW()) AS wait_seconds,
       CONCAT('KILL ', b.trx_mysql_thread_id)           AS kill_blocking_query
FROM performance_schema.data_lock_waits w
         INNER JOIN information_schema.innodb_trx b ON b.trx_id = w.blocking_engine_transaction_id
         INNER JOIN information_schema.innodb_trx r ON r.trx_id = w.requesting_engine_transaction_id
WHERE r.trx_wait_started IS NOT NULL;

select *
from performance_schema.data_lock_waits w
         INNER JOIN information_schema.innodb_trx b ON b.trx_id = w.blocking_engine_transaction_id;
#          INNER JOIN information_schema.innodb_trx r ON r.trx_id = w.requesting_engine_transaction_id;


select *
from information_schema.innodb_trx;

select *
from performance_schema.data_lock_waits w
         inner join information_schema.INNODB_TRX b on w.REQUESTING_ENGINE_TRANSACTION_ID = b.trx_id;



#